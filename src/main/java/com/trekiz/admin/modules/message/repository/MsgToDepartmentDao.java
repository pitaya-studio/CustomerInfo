package com.trekiz.admin.modules.message.repository;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Component;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.modules.message.entity.MsgToDepartment;
@Component("msgToDepartmentDao")
public class MsgToDepartmentDao extends BaseDaoImpl<MsgToDepartment>{

	/**
	 * 保存消息和部门链接表
	 * @param msgToDepartment
	 * @return
	 */
	public MsgToDepartment addMsgToDepartment(MsgToDepartment msgToDepartment){
		try{
			getSession().save(msgToDepartment);
			getSession().flush();
		}catch(Exception e){
			e.printStackTrace();
		}
		return msgToDepartment;
	}
	/**
	 * 根据id 获取关联的部门
	 * @author gao
	 * @param id  MsgAnnouncement 主键
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<MsgToDepartment> findMsgToDepartmentList(Long id){
		try{
			StringBuffer buf = new StringBuffer("from MsgToDepartment where 1=1 and msgId=?");
			Query queryObject = getSession().createQuery(buf.toString());
			queryObject.setParameter(0, id);
			return queryObject.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 根据MsgAnnouncement 主键 删除MsgToDepartment表中关联的部门
	 * @author gao
	 * @param msgId MsgAnnouncement 主键
	 * @return
	 */
	public int delMsgToDepartment(Long msgId){
		try{
			StringBuffer buf = new StringBuffer("delete  MsgToDepartment where  msgId=?");
			Query queryObject = getSession().createQuery(buf.toString());
			queryObject.setParameter(0, msgId);
			return queryObject.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		return -1;
	}
}
