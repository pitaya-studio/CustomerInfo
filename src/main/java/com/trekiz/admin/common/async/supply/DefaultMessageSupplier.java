/** 
 * Copyright 2015 QUAUQ Technology Co. Ltd. 
 * All right reserved.
 */
package com.trekiz.admin.common.async.supply;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author ning.zhang@quauq.com
 * @createDate 2015-10-24
 * 
 * 默认的消息发送类
 */
public final class DefaultMessageSupplier implements
		IMessageSupplier {
	
	private ThreadPoolTaskExecutor executor ;
	private JmsTemplate asyncJmsTemplate ; 
	private ActiveMQQueue destination;
	
	DefaultMessageSupplier(ThreadPoolTaskExecutor executor,
			JmsTemplate asyncJmsTemplate, ActiveMQQueue destination) {
		this.executor = executor;
		this.asyncJmsTemplate = asyncJmsTemplate; 
		this.destination = destination;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.trekiz.admin.common.async.supply.AsynchronizedMessageSupplier#send
	 * (java.lang.String)
	 */ 
	@Override
	public void asynchronizedSend(final String message) {
		executor.execute(new Runnable() {
			@Override
			public void run() {
				try{
					asyncJmsTemplate.send(destination, new MessageCreator() {
						@Override
						public Message createMessage(Session session) throws JMSException {
							return session.createTextMessage(message);
						}
					});	
				}catch(Throwable e){
					e.printStackTrace();
				}
			}
		}); 
	}

	/* (non-Javadoc)
	 * @see com.trekiz.admin.common.async.supply.AsynchronizedMessageSupplier#synchronizedSend(java.lang.String)
	 */
	@Override
	public void synchronizedSend(final String message) throws JmsException {
		asyncJmsTemplate.send(destination, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage(message);
			}
		});
	} 
}
