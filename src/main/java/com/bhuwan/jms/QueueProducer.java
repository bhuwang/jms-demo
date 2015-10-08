/**
 * 
 */
package com.bhuwan.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * @author bhuwan
 *
 */
public class QueueProducer {

	private Session session;
	private Queue requestQueue;
	private Queue responseQueue;
	private Connection connection;

	public QueueProducer() throws JMSException {
		// connect to jms server, make sure the server is running in the defined
		// port.
		ConnectionFactory factory = new ActiveMQConnectionFactory("admin",
				"admin", "tcp://localhost:61616");

		// create connection
		connection = factory.createConnection();
		// making transaction auto-commit by false, if true you have to make
		// sure the session persist at the end.
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		requestQueue = session.createQueue("fuel.request.queue");
		responseQueue = session.createQueue("fuel.response.queue");

		// start connection after all the resources ready
		connection.start();
	}

	public void produce(String message) throws JMSException {
		MessageProducer producer = session.createProducer(requestQueue);
		TextMessage textMessage = session.createTextMessage(message);
		textMessage.setJMSReplyTo(this.responseQueue);
		textMessage.setJMSCorrelationID("111");

		// send message to queue
		System.out.println("Sending Message: " + textMessage + "\n\n");
		producer.send(textMessage);
		producer.close();

		MessageConsumer messageConsumer = this.session
				.createConsumer(responseQueue);
		TextMessage replyMessage = (TextMessage) messageConsumer.receive(5000);

		System.out.println("Producer - Reply message from consumer: "
				+ replyMessage);
		messageConsumer.close();

	}
}
