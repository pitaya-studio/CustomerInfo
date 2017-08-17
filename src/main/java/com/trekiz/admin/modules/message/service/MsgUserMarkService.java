package com.trekiz.admin.modules.message.service;

import java.util.List;

import com.trekiz.admin.modules.message.entity.MsgAnnouncement;
import com.trekiz.admin.modules.message.entity.MsgUserMark;
import com.trekiz.admin.modules.sys.entity.User;

public interface MsgUserMarkService {

	/**
	 * 增加公告分配项
	 * @author gao
	 * @param msg 公告实体
	 * @param userId 发布人
	 * @return
	 */
	public MsgUserMark addMsgUserMark(MsgAnnouncement msg,Long userId);
	
	/**
	 * 获取msg对应的usermark
	 * @param msg
	 * @return
	 */
	public List<MsgUserMark> findMsgUserMarkByMsgId(Long msgId);

	/**
	 * 找出msg对应不予提醒的人
	 * @param id
	 * @return
	 */
	public List<User> findExceptUsersByMsgId(Long id);
}
