package com.trekiz.admin.modules.operator.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.operator.dao.OperatorAchievementDao;
import com.trekiz.admin.modules.operator.service.OperatorAchievementService;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class OperatorAchievementServiceImpl extends BaseService implements  OperatorAchievementService{
	
	@Autowired
	private OperatorAchievementDao operatorAchievementDao;
	
	/**
	 * 构建团期sql
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public String getTravlerSql() {
		StringBuffer sbf=new StringBuffer();
//		sbf.append("SELECT * ");
//		sbf.append("FROM ");
//		sbf.append("( ");
		sbf.append("SELECT ");
		sbf.append("p.id AS activityId, ");
		sbf.append("p.delFlag AS delFlag, ");
		sbf.append("p.acitivityName, ");
		sbf.append("p.activity_kind AS kind, ");
		sbf.append("g.groupCode, ");
		sbf.append("g.id AS groupId, ");
		sbf.append("g.createBy, ");
		sbf.append("sum(o.orderPersonNum) AS personNum, ");
		sbf.append("p.createDate AS createDate, ");
		sbf.append("g.groupOpenDate AS groupOpenDate ");
		//sbf.append("g.groupCloseDate AS groupCloseDate, ");
		sbf.append("FROM ");
		sbf.append("productorder o,activitygroup g,travelactivity p ");
		sbf.append("WHERE ");
		sbf.append("o.productGroupId = g.id ");
		sbf.append("AND p.id = g.srcActivityId ");
		sbf.append("AND o.productId = p.id ");
		sbf.append("AND p.delFlag=0 ");
		sbf.append("AND g.delFlag=0 ");
		sbf.append("AND o.delFlag=0 ");
		sbf.append("AND p.proCompany=? ");
		sbf.append("GROUP BY g.id ");
//		sbf.append(") t, ");
//		sbf.append(this.getDepartmentSql());
		return sbf.toString();
	}
	
	/**
	 * 构建机票sql
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public String getAirTicketSql() {
		StringBuffer sbf=new StringBuffer();
//		sbf.append("SELECT * ");
//		sbf.append("FROM ( ");
		sbf.append("SELECT ");
		sbf.append("a.id AS activityId, ");
		sbf.append("a.delflag AS delFlag, ");
		sbf.append("'' AS activityName, ");
		sbf.append("7 AS kind, ");
		sbf.append("a.group_code AS groupCode, ");
		sbf.append("'' AS groupId, ");
		sbf.append("a.createBy, ");
		sbf.append("sum(o.person_num) AS personNum, ");
		sbf.append("a.createDate AS createDate, ");
		sbf.append("a.outTicketTime AS groupOpenDate ");
		//sbf.append("a.returnDate AS groupCloseDate ");
		sbf.append("FROM ");
		sbf.append("activity_airticket a, ");
		sbf.append("airticket_order o ");
		sbf.append("WHERE ");
		sbf.append("a.id = o.airticket_id ");
		sbf.append("AND a.delflag = 0 ");
		sbf.append("AND o.del_flag = 0 ");
		sbf.append("AND a.proCompany = ? ");
		sbf.append("GROUP BY a.id ");
//		sbf.append(") t, ");
//		sbf.append(this.getDepartmentSql());
		return sbf.toString();
	}
	
	/**
	 * 构建签证sql
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public String getVisaSql() {
		StringBuffer sbf=new StringBuffer();
//		sbf.append("SELECT * ");
//		sbf.append("FROM ");
//		sbf.append("( ");
		sbf.append("SELECT ");
		sbf.append("v.id AS activityId, ");
		sbf.append("v.delFlag AS delFlag, ");
		sbf.append("v.productName AS activityName, ");
		sbf.append("6 AS kind, ");
		sbf.append("v.groupCode AS groupCode, ");
		sbf.append("'' AS groupId, ");
		sbf.append("v.createBy AS createBy, ");
		sbf.append("SUM(o.travel_num) AS personNum, ");
		sbf.append("v.createDate AS createDate, ");
		sbf.append("v.createDate AS groupOpenDate ");
		sbf.append("FROM ");
		sbf.append("visa_products v, ");
		sbf.append("visa_order o ");
		sbf.append("WHERE ");
		sbf.append("o.visa_product_id = v.id ");
		sbf.append("AND v.delFlag = 0 ");
		sbf.append("AND o.del_flag = 0 ");
		sbf.append("AND v.proCompanyId = ? ");
		sbf.append("GROUP BY v.id ");
//		sbf.append(") t, ");
//		sbf.append(this.getDepartmentSql());
		return sbf.toString();
	}

	/**
	 * 构建部门sql,与机票sql，团期sql，签证sql连用
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@Override
	public String getDepartmentSql() {
		StringBuffer sbf=new StringBuffer();
		//sbf.append("( ");
		sbf.append("SELECT ");
		sbf.append(" DISTINCT sudj.user_id AS userId, ");
		sbf.append("sudj.del_flag as delFlag, ");
		sbf.append("GROUP_CONCAT(DISTINCT d.id) AS departmentId, ");
		sbf.append("GROUP_CONCAT(DISTINCT d. NAME) AS departmentName ");
		sbf.append("FROM ");
		sbf.append("sys_user_dept_job_new sudj ");
		sbf.append("LEFT JOIN department d ON sudj.dept_id = d.id ");
		sbf.append("WHERE sudj.user_id=? and sudj.del_flag=0 and ");
		sbf.append("NOT FIND_IN_SET( ");
		sbf.append("d.id, ");
		sbf.append("( ");
		sbf.append("SELECT ");
		sbf.append("GROUP_CONCAT( ");
		sbf.append("DISTINCT d.parent_ids SEPARATOR '' ");
		sbf.append(") ");
		sbf.append("FROM ");
		sbf.append("sys_user_dept_job_new sudj ");
		sbf.append("LEFT JOIN department d ON sudj.dept_id = d.id where sudj.del_flag=0 and sudj.user_id=? ");
		sbf.append(")) and d.office_id="+UserUtils.getUser().getCompany().getId()+"  group by sudj.user_id ");
		//sbf.append("WHERE t.createBy=y.userId ");
		return sbf.toString();
	}
	
	/**
	 * 获得操作人业绩总列表
	 * @param page
	 * @param request
	 * @return
	 */
	@Override
	public Page<Map<String, Object>> getOperatorAchievementList(
			Page<Map<String, Object>> page, HttpServletRequest request) {
		Integer companyId=UserUtils.getUser().getCompany().getId().intValue();
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT c.activityId,c.acitivityName,c.kind,c.groupCode,c.groupId,c.createBy, ");
		sbf.append("c.personNum,c.createDate  ");
		sbf.append(" FROM ( ");
		sbf.append(this.getTravlerSql());
		sbf.append("UNION ");
		sbf.append(this.getAirTicketSql());
		sbf.append("UNION ");
		sbf.append(this.getVisaSql());
		sbf.append(")c WHERE c.delFlag=0 "); 
		if(StringUtils.isNotEmpty(request.getParameter("groupCode"))){
			sbf.append("AND c.groupCode LIKE '%"+request.getParameter("groupCode")+"%' ");
		}
		if(StringUtils.isNotEmpty(request.getParameter("departmentId"))){
			sbf.append("AND c.createBy in (select sudj.user_id from sys_user_dept_job_new sudj where sudj.dept_id="+request.getParameter("departmentId")+" ) ");
		}
		if(StringUtils.isNotEmpty(request.getParameter("salerId"))){
			sbf.append("AND c.createBy="+request.getParameter("salerId")+" ");
		}
		if(StringUtils.isNotEmpty(request.getParameter("productName"))){
			sbf.append("AND c.acitivityName LIKE '%"+request.getParameter("productName")+"%' ");
		}
		if(StringUtils.isNotEmpty(request.getParameter("groupOpenDate"))){
			String groupOpenDate=request.getParameter("groupOpenDate");
			String year=groupOpenDate.split("年")[0];
			String month=groupOpenDate.split("年")[1].split("月")[0];
			String date=year+"-"+month+"-01 00:00:00";
			sbf.append("AND c.groupOpenDate>='"+date+"' ");
		}
		if(StringUtils.isNotEmpty(request.getParameter("groupOpenDate"))){
			String groupCloseDate=request.getParameter("groupOpenDate");
			String year=groupCloseDate.split("年")[0];
			String month=groupCloseDate.split("年")[1].split("月")[0];
			String date=year+"-"+month+"-31 23:59:59";
			sbf.append("AND c.groupOpenDate<='"+date+"' ");
		}
		sbf.append("GROUP BY c.groupCode ");
		sbf.append("ORDER BY c.createDate DESC ");
		page=operatorAchievementDao.findPageBySql(page,sbf.toString(),Map.class, companyId, companyId, companyId);
		for(Map<String,Object> map:page.getList()){
			String sql = this.getDepartmentSql();
			List<Map<String,Object>> departs=operatorAchievementDao.findBySql(sql.toString(),Map.class,map.get("createBy"),map.get("createBy"));
			if(departs.size()!=0){
				Map<String, Object> depart = departs.get(0);
				map.put("userId", depart.get("userId"));
				map.put("departmentId", depart.get("departmentId"));
				map.put("departmentName", depart.get("departmentName"));
			}
		}
		return page;
	}

	@Override
	public List<User> getUserList() {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * FROM sys_user WHERE companyId=? AND delFlag=?");
		List<User> list=operatorAchievementDao.findBySql(sbf.toString(), User.class,UserUtils.getUser().getCompany().getId(),0);
		return list;
	}
	
	/**
	 * 获得总人数
	 * @param request
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getPersonSum(HttpServletRequest request) {
		Integer companyId=UserUtils.getUser().getCompany().getId().intValue();
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT c.activityId,c.acitivityName,c.kind,c.groupCode,c.groupId,c.createBy, ");
		sbf.append("c.personNum,c.createDate  ");
		sbf.append(" FROM ( ");
		sbf.append(this.getTravlerSql());
		sbf.append("UNION ");
		sbf.append(this.getAirTicketSql());
		sbf.append("UNION ");
		sbf.append(this.getVisaSql());
		sbf.append(")c WHERE c.delFlag=0 "); 
		if(StringUtils.isNotEmpty(request.getParameter("groupCode"))){
			sbf.append("AND c.groupCode LIKE '%"+request.getParameter("groupCode")+"%' ");
		}
		if(StringUtils.isNotEmpty(request.getParameter("departmentId"))){
			sbf.append("AND c.createBy in (select sudj.user_id from sys_user_dept_job_new sudj where sudj.dept_id="+request.getParameter("departmentId")+" ) ");
		}
		if(StringUtils.isNotEmpty(request.getParameter("salerId"))){
			sbf.append("AND c.createBy="+request.getParameter("salerId")+" ");
		}
		if(StringUtils.isNotEmpty(request.getParameter("productName"))){
			sbf.append("AND c.acitivityName LIKE '%"+request.getParameter("productName")+"%' ");
		}
		if(StringUtils.isNotEmpty(request.getParameter("groupOpenDate"))){
			String groupOpenDate=request.getParameter("groupOpenDate");
			String year=groupOpenDate.split("年")[0];
			String month=groupOpenDate.split("年")[1].split("月")[0];
			String date=year+"-"+month+"-01 00:00:00";
			sbf.append("AND c.groupOpenDate>='"+date+"' ");
		}
		if(StringUtils.isNotEmpty(request.getParameter("groupOpenDate"))){
			String groupCloseDate=request.getParameter("groupOpenDate");
			String year=groupCloseDate.split("年")[0];
			String month=groupCloseDate.split("年")[1].split("月")[0];
			String date=year+"-"+month+"-31 23:59:59";
			sbf.append("AND c.groupOpenDate<='"+date+"' ");
		}
		sbf.append("GROUP BY c.groupCode ");
		sbf.append("ORDER BY c.createDate DESC ");
		List<Map<String,Object>> list=operatorAchievementDao.findBySql(sbf.toString(), Map.class,companyId,companyId,companyId);
		for(Map<String,Object> map:list){
			String sql = this.getDepartmentSql();
			List<Map<String,Object>> departs=operatorAchievementDao.findBySql(sql.toString(),Map.class,map.get("createBy"),map.get("createBy"));
			if(departs.size()!=0){
				Map<String, Object> depart = departs.get(0);
				map.put("userId", depart.get("userId"));
				map.put("departmentId", depart.get("departmentId"));
				map.put("departmentName", depart.get("departmentName"));
			}else{
				map.put("userId", "");
				map.put("departmentId", "");
				map.put("departmentName", "");
			}
		}
		return list;
	}
	
}
