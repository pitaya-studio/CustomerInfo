package com.trekiz.admin.modules.message.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.message.entity.MsgToDepartment;
import com.trekiz.admin.modules.message.repository.MsgToDepartmentDao;
import com.trekiz.admin.modules.message.service.MsgToDepartmentService;
@Service("MsgToDepartmentService")
@Transactional(readOnly = true)
public class MsgToDepartmentServiceImpl extends BaseService implements MsgToDepartmentService{

	@Autowired
	private MsgToDepartmentDao msgToDepartmentDao;
	@Override
	public MsgToDepartment addMsgToDepartment(MsgToDepartment msgToDepartment) {
		MsgToDepartment msgToDe = msgToDepartmentDao.addMsgToDepartment(msgToDepartment);
		if(msgToDe.getId()!=null){
			this.saveLogOperate(Context.log_type_notice, Context.log_type_notice_name,
					"增加‘MsgToDepartment’类实体 成功！新增id为："+msgToDe.getId(), Context.log_state_add, null, null);
		}
		return msgToDe;
	}
	@Override
	public List<MsgToDepartment> findMsgToDepartmentList(Long id) {
		List<MsgToDepartment> msglist = msgToDepartmentDao.findMsgToDepartmentList(id);
		return msglist;
	}
	@Override
	public int delMsgToDepartment(Long msgId) {
		return msgToDepartmentDao.delMsgToDepartment(msgId);
	}

}
