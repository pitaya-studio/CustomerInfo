/** 
* Copyright 2015 QUAUQ Technology Co. Ltd. 
* All right reserved.
*/
package com.trekiz.admin.common.async.receive;

import java.util.Queue;

/**
 * @author ning.zhang@quauq.com
 * @createDate 2015-10-26
 * 
 * AMQ消息处理Hander
 */
public interface IMessageHander {
	/**
	 * 返回hander对应的AMQ Queue Name
	 * @return AMQ Queue Name 
	 */
	public String getMessageQueueName();
	
	/**
	 * 返回该Hander一次处理的最大数据量
	 * 如果返回值小于1 ，则表示不限制数量
	 * 注意：该值与业务紧密相关,对AMQ 来说返回的值所对应的消息集是同一个事务。
	 * @return 
	 */
	public long maxMessagePerBatch();
	
	/**
	 * 处理消息，回调函数，有消息需要处理的时候回调。
	 * 参数Queue的顺序与AMQ-QUEUE的的顺序一致。数量不会大于maxMessagePerBatch的返回值。
	 * 注意：
	 * 1 、对应AMQ来说，参数Queue的消息集是一个事务。
	 * 2、 回调函数未处理多租户切换，所以需要实现者自己实现租户切换
	 * @param messages 消息集
	 * @throws Exception 需要回滚AMQ事务，须抛出异常。
	 */
	public void handMessage(Queue<String> messages) throws Exception;
}
