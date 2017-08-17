package com.trekiz.admin.modules.message.service;

import java.util.List;

import com.trekiz.admin.modules.message.entity.MsgToDepartment;

public interface MsgToDepartmentService {

	/**
	 * 公告发布部门
	 * @author gao
	 * @param msgToDepartment 部门链接表
	 * @return
	 */
	public MsgToDepartment addMsgToDepartment(MsgToDepartment msgToDepartment);
	/**
	 * 根据id 获取关联的部门
	 * @author gao
	 * @param id MsgAnnouncement 主键
	 * @return
	 */
	public List<MsgToDepartment> findMsgToDepartmentList(Long id);
	
	/**
	 * 根据MsgAnnouncement 主键 删除MsgToDepartment表中关联的部门
	 * @author gao
	 * @param msgId MsgAnnouncement 主键
	 * @return
	 */
	public int delMsgToDepartment(Long msgId);
}
