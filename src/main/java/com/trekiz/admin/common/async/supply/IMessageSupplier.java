/** 
* Copyright 2015 QUAUQ Technology Co. Ltd. 
* All right reserved.
*/
package com.trekiz.admin.common.async.supply;

import org.springframework.jms.JmsException;

/**
 * @author ning.zhang@quauq.com
 * @createDate 2015-10-24
 * 
 * 消息发布接口
 */
public interface IMessageSupplier {
	
	/**
	 * 基于异步机制发送消息
	 * 高性能 跨线程 无异常机制
	 * @param message
	 */
	public void asynchronizedSend(String message);
	
	/**
	 * 基于同步机制发送消息
	 * 低性能 非跨线程 有异常机制
	 * @param message
	 * @throws JmsException
	 */
	public void synchronizedSend(String message) throws JmsException; 
}
