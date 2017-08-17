package com.trekiz.admin.modules.message.repository;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.message.entity.MsgEntity;
@Component("msgEntityDao")
public class MsgEntityDao extends BaseDaoImpl<MsgEntity>{

	/**
	 * 保存msg表
	 * @param msgEntity
	 * @return
	 */
//	public MsgEntity saveMsgEntity(MsgEntity msgEntity){
//		try{
//			getSession().save(msgEntity);
//			getSession().flush();
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		return msgEntity;
//	}
	
	/**
	 * 根据审批id，规则id，查找msg。然后更新, 如果不存在则新增
	 * @author gao
	 * @param id  MsgEntity 主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<MsgEntity> findMsgEntity(String businessData, Integer remindId){
		try{
			StringBuffer buf = new StringBuffer("from MsgEntity where 1=1 and delFlag=0 and businessData=? and sysRemindId = ?");
			Query queryObject = getSession().createQuery(buf.toString());
			queryObject.setParameter(0, businessData);
			queryObject.setParameter(1, remindId);
			return queryObject.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
}
