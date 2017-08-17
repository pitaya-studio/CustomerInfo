/** 
 * Copyright 2015 QUAUQ Technology Co. Ltd. 
 * All right reserved.
 */
package com.trekiz.admin.common.async.receive;

import java.util.LinkedList;
import java.util.Queue;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQQueue;

/**
 * @author ning.zhang@quauq.com
 * @createDate 2015-10-26
 * 
 * amq 消息收集类
 */
public class MessageCollector {
	private ConnectionFactory connectionFactory;
	private IMessageHander hander; 
	/**
	 * 设置AMQ Connection Factory
	 * @param connectionFactory
	 */
	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	/**
	 * 设置消息处理Hander
	 * @param hander
	 */
	public void setHander(IMessageHander hander) {
		this.hander = hander;
	} 
	
	/**
	 * 从AMQ receive 消息 ，并调用Hander处理
	 * @throws JMSException
	 */
	public void collect() throws JMSException {
		Connection conn = null;
		Session session = null;
		MessageConsumer consumer = null;
		String queueName = this.hander.getMessageQueueName();
		if (null == queueName)
			throw new IllegalArgumentException(
					"IMessageHander.getMessageQueueName 不能为空");
		try {
			conn = this.connectionFactory.createConnection();
			conn.start();
			session = conn.createSession(true, Session.CLIENT_ACKNOWLEDGE);
			consumer = session.createConsumer(new ActiveMQQueue(queueName));

			outner:while (true) {
				long receiveSize = Long.MAX_VALUE;
				// hander处理最大数量
				long handerSize = this.hander.maxMessagePerBatch();
				if (handerSize > 0) { 
					receiveSize = handerSize; 
				} 
				Queue<String> messageQueue = new LinkedList<>();
				try {
					inner: for(long i=0l ;i<receiveSize ;i++) {
						TextMessage message = (TextMessage) consumer.receiveNoWait();
						if(null != message)
							messageQueue.add(message.getText()); 
						else
							if(i==0l) //队列已无数据
								break outner;
							else	
								break inner;
					}
					this.hander.handMessage(messageQueue);
					session.commit();
				} catch (Throwable e) {
					e.printStackTrace();
					session.rollback();
				}

			}

		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != consumer)
					consumer.close();
			} catch (Exception e) {
			}
			try {
				if (null != session)
					session.close();
			} catch (Exception e) {
			}
			try {
				if (null != conn)
					conn.stop();
			} catch (Exception e) {
			}
			try {
				if (null != conn)
					conn.close();
			} catch (Exception e) {
			}
			
		}

	}
	
}
