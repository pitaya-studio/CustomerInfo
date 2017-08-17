/** 
* Copyright 2015 QUAUQ Technology Co. Ltd. 
* All right reserved.
*/
package com.trekiz.admin.common.async.supply;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author ning.zhang@quauq.com
 * @createDate 2015-10-24
 * 消息发布工厂类
 */ 
public class MessageSupplierFactory {
	private ThreadPoolTaskExecutor executor; 
	private JmsTemplate asyncJmsTemplate; 
	private TreeMap<String ,ActiveMQQueue> nameDestinationMap = new TreeMap<>();
	private Map<String, IMessageSupplier> userDefinedMessageSupplier = null;
	
	/**
	 * 设置线程池
	 * @param executor
	 */
	public void setExecutor(ThreadPoolTaskExecutor executor) {
		this.executor = executor;
	}
	
	/**
	 * 设置jms template
	 * @param asyncJmsTemplate
	 */
	public void setAsyncJmsTemplate(JmsTemplate asyncJmsTemplate) {
		this.asyncJmsTemplate = asyncJmsTemplate;
	}
	
	/**
	 * 设置自定义消息发布类
	 * @param userDefinedMessageSupplier map.value为消息发布类
	 */
	public void setUserDefinedMessageSupplier(
			Map<String, IMessageSupplier> userDefinedMessageSupplier) {
		this.userDefinedMessageSupplier = userDefinedMessageSupplier;
	}
	
	/**
	 * 设置amq的队列名称。这些队列将使用DefaultMessageSupplier做为消息发布类
	 * @param names
	 */
	public void setQueueNames(List<String> names){
		if(null != names){
			for(String name : names){
				if(this.nameDestinationMap.containsKey(name))
					throw new IllegalArgumentException("发现有重复申明的amq 队列名称："+name);
				ActiveMQQueue q = new ActiveMQQueue(name);
				this.nameDestinationMap.put(name, q);
			}
		}
	}
	
	/**
	 * 获得默认的消息发布类
	 * @param messageQueueName is AMQ's queue-name
	 * @return
	 */
	public IMessageSupplier getDefaultMessageSupplier(String messageQueueName){
		if(StringUtils.isEmpty(messageQueueName))
			throw new IllegalArgumentException("参数不能为空 messageQueueName");
		ActiveMQQueue q = this.nameDestinationMap.get(messageQueueName);
		if(null == q)
			throw new RuntimeException("找不到参数对应的队列" + messageQueueName);
		if(null == this.executor)
			throw new RuntimeException("MessageSupplierFactory.executor 不能为空");
		if(null == this.asyncJmsTemplate)
			throw new RuntimeException("MessageSupplierFactory.jmsTemplate 不能为空");
		return new DefaultMessageSupplier(executor, asyncJmsTemplate, q) ;
	}
	
	/** 
	 * 获取用户自定义消息发送类
	 * @param key 
	 * @return
	 */
	public IMessageSupplier getUserDefinedMessageSupplier(String key){
		if(null == this.userDefinedMessageSupplier)
			throw new RuntimeException("MessageSupplierFactory.userDefinedMessageSupplier 为空");
		return this.userDefinedMessageSupplier.get(key);
	}
	 
}
