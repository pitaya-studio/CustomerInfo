package com.trekiz.admin.modules.operator.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.sys.entity.User;

public interface OperatorAchievementService {

	/**
	 * 构建团期sql
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public String getTravlerSql();
	
	/**
	 * 构建机票sql
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public String getAirTicketSql();
	
	/**
	 * 构建签证sql
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public String getVisaSql();
	
	/**
	 * 构建部门与机票sql，团期sql，签证sql连用
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public String getDepartmentSql();
	
	/**
	 * 获得操作人业绩总列表
	 * @param page
	 * @param request
	 * @return
	 */
	public Page<Map<String,Object>> getOperatorAchievementList(Page<Map<String,Object>> page,HttpServletRequest request);
	
	/**
	 * 获得操作员
	 * @return
	 */
	public List<User> getUserList();
	
	/**
	 * 获得总人数
	 * @param request
	 * @return
	 */
	public List<Map<String,Object>> getPersonSum(HttpServletRequest request);	
}
