package com.trekiz.admin.modules.message.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.message.entity.MsgAnnouncement;
import com.trekiz.admin.modules.message.entity.MsgUserMark;
import com.trekiz.admin.modules.message.repository.MsgAnnouncementDao;
import com.trekiz.admin.modules.message.repository.MsgUserMarkDao;
import com.trekiz.admin.modules.message.service.MessageService;
@Service("messageService")
@Transactional(readOnly = true)
public class MessageServiceImpl extends BaseService implements  MessageService{
	@Autowired
	private  MsgUserMarkDao msgUserMarkDao;
	@Autowired
	private  MsgAnnouncementDao msgAnnouncementDao;
	
	/**
	 * 撤销消息
	 * messageId 消息id
	 * @return true:操作成功;false:操作失败
	 * 
	 * */
//	@Override
//	public boolean  cancelMessage(Long messageId){
//		if(null == messageId){
//			return false;
//		}
//		try{
////		//根据消息编号,删除用户标示表的信息
////		msgUserMarkDao.deleteById(messageId);
////		//根据消息编号,删除消息表的信息
////		msgAnnouncementDao.deleteById(messageId);
//		return true;
//		}catch(Exception e){
//			e.printStackTrace();
//			return false;
//		}
//	}
	/**
	 * 保存消息 (MsgAnnouncement.msgType == 5)
	 * 保存发给指定人的的消息
	 * msgAnnouncement 消息实体
	 * userIdList 指定人的 id列表
	 * @return true:操作成功;false:操作失败
	 * 
	 * */
	@Override
	public boolean saveMessage(MsgAnnouncement msgAnnouncement,List<Long> userIdList){
		//对数据进行合法性判断
		if(null == msgAnnouncement || null == userIdList || userIdList.isEmpty()  ){
			return false;
		}
		//对信息进行保存
		MsgAnnouncement message = msgAnnouncementDao.save(msgAnnouncement);
		// 如果保存失败，返回false
		if(message==null){
			return false;
		}
		try{
			//循环接收信息的人的列表,逐条保存
			for(int i=0;i<userIdList.size();i++){
				//保存用户标示表
				saveMsgUserMark(userIdList.get(i),message.getId());
			}
			return true;
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
//	
//	/**
//	 * 保存消息
//	 * 保存部门.公司等类型的消息
//	 * @return true:操作成功;false:操作失败
//	 * */
//	@Override
//	public boolean saveMessage(MsgAnnouncement msgAnnouncement){
//		if(null == msgAnnouncement ){
//			return false;
//		}
//		MsgAnnouncement message = msgAnnouncementDao.save(msgAnnouncement);
//		try{
//			//部门可见的公告
//			if(context.msg.equals(message.getMsgType())){
//				
//			//公司可见的公告
//			}else if ("4".equals(message.getMsgType())){
//				String sql = "select id from sys_user where companyId = ? and delFlag = 0 ";
//				List<Object> par = new ArrayList<Object>();
//				par.add(message.getCompanyId());
//				List<Map<Object, Object>> resultList =  msgUserMarkDao.findBySql(sql,Map.class, par.toArray());
//				
//				for(Map<Object, Object> listin : resultList) {
//					String id = listin.get("id").toString();
//					saveMsgUserMark(Long.valueOf(id),message.getId());
//				}
//			}
//			return true;
//		}catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//		
//	}
//	
	/**
	 * 保存用户标示
	 * */
	private void saveMsgUserMark(Long userId,Long messageId){
		MsgUserMark msgUserMark =new MsgUserMark();
		msgUserMark.setIfRead(0);
		msgUserMark.setMsgAnnouncementId(messageId);
		msgUserMark.setUserId(userId);
		msgUserMarkDao.save(msgUserMark);
	}
}
