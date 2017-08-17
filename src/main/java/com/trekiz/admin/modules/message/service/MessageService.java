package com.trekiz.admin.modules.message.service;

import java.util.List;


import com.trekiz.admin.modules.message.entity.MsgAnnouncement;
/**
 * 消息发布接口
 * @author gao
 *  2015年3月18日
 */
public interface MessageService {
	
	/**
	 * 保存消息(MsgAnnouncement.msgType == 5)
	 * 保存发给指定人的的消息 
	 * msgAnnouncement 消息实体
	 * userIdList 指定人的 id列表
	 * @return true:操作成功;false:操作失败
	 * 
	 * */
	public boolean saveMessage(MsgAnnouncement msgAnnouncement,List<Long> userIdList);
	
	
	
}
