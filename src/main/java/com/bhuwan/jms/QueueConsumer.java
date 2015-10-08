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
public class QueueConsumer {

	private Session session;
	private Queue requestQueue;
	private Connection connection;

	public QueueConsumer() throws JMSException {
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

		// start connection after all the resources ready
		connection.start();
	}

	public void consumeAndSendReady() throws JMSException {
		MessageConsumer messageConsumer = this.session
				.createConsumer(this.requestQueue);
		TextMessage getMessage = (TextMessage) messageConsumer.receive();
		messageConsumer.close();
		
		System.out.println("Consumer - message frmo producer: "
				+ getMessage);
		
		MessageProducer messageProducer = this.session.createProducer(getMessage.getJMSReplyTo());
		
		TextMessage respoMessage =  this.session.createTextMessage("Ok! I got your message");
		respoMessage.setJMSCorrelationID(getMessage.getJMSCorrelationID());
		messageProducer.send(respoMessage);

	}
}
