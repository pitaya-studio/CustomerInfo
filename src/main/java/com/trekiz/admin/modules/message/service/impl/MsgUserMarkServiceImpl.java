package com.trekiz.admin.modules.message.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.message.entity.MsgAnnouncement;
import com.trekiz.admin.modules.message.entity.MsgUserMark;
import com.trekiz.admin.modules.message.repository.MsgUserMarkDao;
import com.trekiz.admin.modules.message.service.HttpClientService;
import com.trekiz.admin.modules.message.service.MsgUserMarkService;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.UserDao;

@Service("msgUserMarkService")
@Transactional(readOnly = true)
public class MsgUserMarkServiceImpl   extends BaseService implements MsgUserMarkService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private MsgUserMarkDao msgUserMarkDao;
	@Resource
	private HttpClientService httpClientService;
	
	
	@Override
	public MsgUserMark addMsgUserMark(MsgAnnouncement msg,Long userId) {
		// TODO Auto-generated method stub
		MsgUserMark msgUserMark = new MsgUserMark();
		msgUserMark.setMsgAnnouncementId(msg.getId());
		msgUserMark.setIfRead(Context.MSG_IFREAD_NO);
		msgUserMark.setMessageStatus(msg.getStatus());
		msgUserMark.setUserId(userId);
		msgUserMark = msgUserMarkDao.save(msgUserMark);
		msgUserMarkDao.flush();
		// 对用户发布消息
	   httpClientService.sendMsgAccountToRemote(userId, 1, msg.getMsgType());
		if(msgUserMark.getId()!=null){
			// 日志
			this.saveLogOperate(Context.log_type_notice, Context.log_type_notice_name,
					"增加‘MsgUserMark’类实体 成功！新增id为："+msgUserMark.getId(), Context.log_state_add, null, null);
			return msgUserMark;
		}
		return null;
	}
	
	@Override
	public List<MsgUserMark> findMsgUserMarkByMsgId(Long msgId) {
		if (msgId == null) {
			return null;
		}
		return msgUserMarkDao.findMsgUserMarkByMsgId(msgId);
	}

	@Override
	public List<User> findExceptUsersByMsgId(Long id) {
		List<User> resultList = new ArrayList<>();
		if (id == null) {
			return resultList;
		}
		List<MsgUserMark> exceptUserMark = msgUserMarkDao.findExceptUserMarkByMsgId(id);
		for (MsgUserMark msgUserMark : exceptUserMark) {
			resultList.add(userDao.findById(msgUserMark.getUserId()));
		}
		return resultList;
	}


}
