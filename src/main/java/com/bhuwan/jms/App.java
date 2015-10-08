package com.bhuwan.jms;

import javax.jms.JMSException;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws JMSException {
		
		// Produce
		QueueProducer producer = new QueueProducer();
		producer.produce("Hi this is my first message - pawal");
		
		// Consume
		/*QueueConsumer consumer = new QueueConsumer();
		consumer.consumeAndSendReady();*/
		
	}
}
