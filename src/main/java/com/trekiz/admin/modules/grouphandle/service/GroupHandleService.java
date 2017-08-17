package com.trekiz.admin.modules.grouphandle.service;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.impl.cookie.PublicSuffixListParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.grouphandle.dao.GroupHandleDao;
import com.trekiz.admin.modules.grouphandle.dao.GroupHandleVisaDao;
import com.trekiz.admin.modules.grouphandle.entity.GroupHandle;
import com.trekiz.admin.modules.grouphandle.form.GroupHandleSearchForm;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**  
 * @Title: GroupHandleService.java
 * @Package com.trekiz.admin.modules.grouphandle.service
 * @Description: 团控service
 * @author  
 * @date 2016-2016年1月28日 上午11:14:20
 * @version V1.0  
 */
@Service
public class GroupHandleService {
	
	@Autowired
	private GroupHandleDao groupHandleDao;
	
	@Autowired
	private GroupHandleVisaDao groupHandleVisaDao;
	
	@Autowired
	private GroupBatchHandleService groupBatchHandleService;
	
	
	/**
	 * @Description: 签务团控列表
	 * @author 
	 * @date 2016年2月4日下午3:37:45
	 * @param request
	 * @param response
	 * @param searchForm
	 * @param shenfen
	 * @param common
	 * @return    
	 * @throws
	 */
	public Page<Map<String,Object>> getGroupHandleList(HttpServletRequest request,HttpServletResponse response,
			GroupHandleSearchForm searchForm, String shenfen, DepartmentCommon common) {
		
		
		//根据条件查询订单信息
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("SELECT ")
				 .append("gc.id, ")
				 .append("gc.activity_group_id, ")
				 .append("gc.activity_group_code, ")
				 .append("gc.activity_product_id, ")
				 .append("gc.activity_product_name, ")
				 .append("gc.op_name, ")
				 .append("gc.activity_product_kind, ")
				 .append("ag.groupOpenDate, ")
				 .append("ag.groupCloseDate, ")
				 .append("gc_gcv_1.grouptravle_num ")
				 .append("FROM ")
				 .append("group_control gc ")
				 .append("LEFT JOIN activitygroup ag ON gc.activity_group_id = ag.id ")
				 .append("LEFT JOIN sys_user su on gc.createBy = su.id ")
				 .append("LEFT JOIN group_control_visa gcv_1 on gc.activity_group_code = gcv_1.activity_group_code ")
				 .append("LEFT JOIN productorder porder ON gc.order_id = porder.id ")  //不显示不占位 取消  和删除订单的数据
				 
				 /*
				  * 分组计算  各个团期  的  游客数   grouptravle_num
				  * 如果 计算结果为  null 说明  没有  游客，页面做处理
				  */
				 .append("LEFT JOIN ( ")
				 .append("SELECT gc_gcv_1.activity_group_code, COUNT(gc_gcv_1.activity_group_code) AS grouptravle_num ")
				 .append("FROM ")
				 .append("( ")
				 .append("SELECT ")
				 .append("gcv.activity_group_code,gcv.traveler_id ")
				 .append(" FROM group_control_visa gcv LEFT JOIN group_control g ON gcv.grouphandle_id=g.id LEFT JOIN sys_user su1 ON g.saler_id=su1.id")
				 .append(" LEFT JOIN productorder porder ON gcv.order_id = porder.id ") //不显示不占位 取消  和删除订单的数据
				 .append(" WHERE gcv.delFlag = 0 AND su1.companyId="+UserUtils.getUser().getCompany().getId())
				 .append(" AND (porder.payStatus in (3,4,5) or porder.placeHolderType = 1 or porder.paymentType != 1) ") //不显示不占位 取消  和删除订单的数据
				 .append(" GROUP BY gcv.traveler_id ")
				 .append(") gc_gcv_1 ")
				 .append("GROUP BY gc_gcv_1.activity_group_code ")
				 .append(") gc_gcv_1 ON gc.activity_group_code = gc_gcv_1.activity_group_code ")
				 
				 .append("WHERE su.companyId = "+UserUtils.getUser().getCompany().getId())
				 .append(" AND (porder.payStatus in (3,4,5) or porder.placeHolderType = 1 or porder.paymentType != 1)") //不显示不占位 取消  和删除订单的数据
		         .append(" AND gc.delFlag='0' ");
		         
				 
		//处理搜索条件
		sqlBuffer = searchGroupHandelParam(searchForm,sqlBuffer,"");
				 
		//部门ID不为空时拼接部门SQL的where条件
		if (StringUtils.isNotBlank(common.getDepartmentId())) {
			groupBatchHandleService.departmentSQL(common,sqlBuffer,2);
		}
		
		sqlBuffer.append("GROUP BY gc.activity_group_code ");

		
		//处理排序条件
		String orderBy = request.getParameter("orderBy");
		Page<Map<String,Object>> page = new Page<Map<String,Object>>(request, response);
		
		if (StringUtils.isBlank(orderBy)) {
			page.setOrderBy("gc.createDate DESC");
		} else {
			page.setOrderBy(orderBy);
		}
		
		Page<Map<String,Object>> resultPage = groupHandleDao.findBySql(page, sqlBuffer.toString(),Map.class);
		
		resultPage = getGroupHandleExpendTravllers(resultPage);

		
		return resultPage;
		
	}
	
	

	/**
	 * @Description: 处理团控的查询条件
	 * @author 
	 * @date 2016年2月1日上午10:07:22
	 * @param visaOrderForm
	 * @param sqlBuffer
	 * @param shenfen
	 * @param mainOrderId
	 * @return    
	 * @throws
	 */
	private StringBuffer searchGroupHandelParam(GroupHandleSearchForm searchForm, StringBuffer sqlBuffer, String shenfen) {
		
		//验证参数是否合法
		if(null == searchForm){
			return sqlBuffer;
		}
		
		//团号，产品名称的查询条件 
		if(null != searchForm.getCommonCode() && !"".equals(searchForm.getCommonCode().trim())){
			String commonCode = searchForm.getCommonCode().replace("'", "''");
			sqlBuffer.append(" AND (gc.activity_product_name like '%"+commonCode.trim()+"%' or gc.activity_group_code like '%"+commonCode.trim()+"%' )");
		}
		
		//销售
		if(null != searchForm.getSalerId() && !"".equals(searchForm.getSalerId().trim())){
			sqlBuffer.append(" AND gc.saler_id = "+searchForm.getSalerId().trim()+" ");
		}
		
		//渠道
		if(null != searchForm.getAgentinfoId() && !"".equals(searchForm.getAgentinfoId().trim())){
			sqlBuffer.append(" AND gc.agentinfo_id = "+searchForm.getAgentinfoId()+" ");
		}
		
		//团队类型 
		if(null != searchForm.getActivityProductKind() && !"".equals(searchForm.getActivityProductKind().trim())){
			sqlBuffer.append(" AND gc.activity_product_kind = "+searchForm.getActivityProductKind().trim()+" ");
		}
		
		//游客名称
		if(null != searchForm.getTravelerName() && !"".equals(searchForm.getTravelerName().trim())){
			String travelName = searchForm.getTravelerName().replace("'", "''");
			sqlBuffer.append(" AND gcv_1.traveler_name LIKE  '%"+travelName.trim()+"%' ");
		}
		
		//签证国家
		if(null != searchForm.getVisaCountryId() && !"".equals(searchForm.getVisaCountryId().trim())){
			sqlBuffer.append(" AND gcv_1.visa_country_id = "+searchForm.getVisaCountryId()+" ");
		}
		
		//签证类型
		if(null != searchForm.getVisaTypeId() && !"".equals(searchForm.getVisaTypeId().trim())){
			sqlBuffer.append(" AND gcv_1.visa_type_id = "+searchForm.getVisaTypeId()+" ");
		}
		
		
		//预计约签时间-开始
		if(null != searchForm.getAboutSigningTimeStart() && !"".equals(searchForm.getAboutSigningTimeStart().trim())){
			sqlBuffer.append(" AND gcv_1.about_signing_time >= '"+searchForm.getAboutSigningTimeStart()+" 00:00:00"+"' ");
		}
		//预计约签时间-结束
		if(null != searchForm.getAboutSigningTimeEnd() && !"".equals(searchForm.getAboutSigningTimeEnd().trim())){
			sqlBuffer.append(" AND gcv_1.about_signing_time <= '"+searchForm.getAboutSigningTimeEnd()+" 23:59:59"+"' ");
		}
		
		
		//实际签约时间-开始
		if(null != searchForm.getSigningTimeStart() && !"".equals(searchForm.getSigningTimeStart().trim())){
			sqlBuffer.append(" AND gcv_1.signing_time >= '"+searchForm.getSigningTimeStart()+" 00:00:00"+"' ");
		}
		//实际签约时间-结束
		if(null != searchForm.getSigningTimeEnd() && !"".equals(searchForm.getSigningTimeEnd().trim())){
			sqlBuffer.append(" AND gcv_1.signing_time <= '"+searchForm.getSigningTimeEnd()+" 23:59:59"+"' ");
		}
		
		//签证类型
		if(null != searchForm.getVisaStauts() && !"".equals(searchForm.getVisaStauts().trim())){
			sqlBuffer.append(" AND gcv_1.visa_stauts = "+searchForm.getVisaStauts()+" ");
		}

		
		return sqlBuffer;
	}
	
	
	private  Page<Map<String,Object>>  getGroupHandleExpendTravllers(Page<Map<String,Object>> resultPage){
		
		List<Map<String,Object>> pageList = resultPage.getList();
		for (Map<String, Object> map : pageList) {
			
			List<Map<String,Object>> groupHandleTravellers = new ArrayList<Map<String,Object>>();
			
			String groupCode  = (String)map.get("activity_group_code");
			groupHandleTravellers = getGroupHandleTravllers(groupCode);
			
			map.put("groupHandleTravellers", groupHandleTravellers);
		}
		return  resultPage;
	}
	
	/**
	 * @Description: 处理二级游客的按销售,游客分组 展开结构
	 * @author xinwei.wang
	 * @date 2016年1月29日上午9:58:05
	 * @param groupCode
	 * @return    
	 * @throws
	 */
	private List<Map<String,Object>> getGroupHandleTravllers(String groupCode){
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("SELECT ")
		         .append("GROUP_CONCAT(DISTINCT gcv.visa_country_name SEPARATOR '/') as country_names, ")
		         .append("GROUP_CONCAT(gcv.visa_country_id SEPARATOR ',') as country_ids, ")
		         .append("gcv.traveler_id, ")
		         .append("gcv.passport_num, ")
		         .append("gcv.visa_country_id, ")
		         .append("gcv.visa_country_name, ")
		         .append("gc.saler_id, ")
		         .append("gc.saler_name, ")
		         .append("gc.agentinfo_id, ")
		         .append("gc.agentinfo_name, ")
		         .append("gc.activity_group_code, ")
		         .append("travel.payed_moneySerialNum, ")
		         .append("travel.passportStatus, ")
		         .append("travel.`name` as tname, ")
		         .append("travel.sex, ")
		         .append("travel.delFlag ")
		         .append("FROM ")
		         .append("group_control_visa gcv ")
		         .append("LEFT JOIN group_control gc ON gcv.grouphandle_id = gc.id  ")
		         .append("LEFT JOIN sys_user su ON gc.saler_id=su.id ")
		         .append("LEFT JOIN traveler travel ON travel.id = gcv.traveler_id ")
		         .append("LEFT JOIN productorder porder ON gcv.order_id = porder.id ")
		         .append(" WHERE gcv.delFlag=0 AND travel.delFlag in (0,3,5) ")
		         .append(" AND (porder.payStatus in (3,4,5) or porder.placeHolderType = 1 or porder.paymentType != 1) ")
		         .append(" AND su.companyId="+UserUtils.getUser().getCompany().getId())
		         .append(" AND gcv.activity_group_code = '"+groupCode+"' GROUP BY gc.saler_id,gcv.traveler_id ");
		
		List<Map<String,Object>> groupHandleTravllers = groupHandleVisaDao.findBySql(sqlBuffer.toString(), Map.class);
		
		//处理销售分组  开始
		Map<String, Integer> salerTravelCount = new HashMap<String, Integer>();
		for (Map<String, Object> map : groupHandleTravllers) {
			if (salerTravelCount.get(map.get("saler_id").toString())!=null) {
				salerTravelCount.put(map.get("saler_id").toString(), salerTravelCount.get(map.get("saler_id").toString())+1);
			}else {
				salerTravelCount.put(map.get("saler_id").toString(), 1);
			}
		}
		
		for (Map<String, Object> map : groupHandleTravllers) {
			map.put("salerTravelRowCount",salerTravelCount.get(map.get("saler_id").toString()));
		}
		
		
		//处理三级浮窗结构
		groupHandleTravllers = getGroupHandleTravllerVisas(groupHandleTravllers,groupCode);
		
		return groupHandleTravllers;
		
	}
	
	/**
	 * @Description: 处理三级游客签证的浮窗结构
	 * @author xinwei.wang
	 * @date 2016年1月29日上午10:00:07
	 * @param groupCode
	 * @return    
	 * @throws
	 */
	private List<Map<String,Object>> getGroupHandleTravllerVisas(List<Map<String,Object>> groupHandleTravllers,String groupCode){
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("SELECT ")
		         .append("gcv.id, ")
				 .append("gcv.visa_country_name, ")
				 .append("gcv.visa_type_name, ")
				 .append("gcv.visa_consulardistric_name, ")
				 .append("gcv.visa_handle_unit, ")
				 .append("gcv.visa_stauts, ")
				 .append("about_signing_time, ")
				 .append("signing_time, ")
				 .append("visa_delivery_time, ")
				 .append("visa_got_time, ")
				 .append("supplementaryinfo_time ")
				 .append("FROM ")
				 .append("group_control_visa gcv ")
				 .append("WHERE ");
		
		boolean flag = false;//是否为游客列表调用，默认否
        for (Map<String, Object> map : groupHandleTravllers) {
        	//这里处理同游客列表二级悬浮框查询方法共用
        	if(StringUtils.isBlank(groupCode)){
        		flag = true;
        		groupCode = map.get("groupCode").toString();//游客列表-团号
        	}
        	
        	StringBuffer _sqlBuffer = new StringBuffer();
        	_sqlBuffer.append("gcv.activity_group_code = '"+groupCode+"' ") 
			         .append("AND gcv.traveler_id = "+map.get("traveler_id").toString()+" ")
			         .append("AND gcv.visa_country_id IN ("+map.get("country_ids").toString()+") ")
			         .append(" AND gcv.delFlag='0' ");
			
        	List<Map<String,Object>> groupHandleTravllerVisas = groupHandleVisaDao.findBySql(sqlBuffer.toString()+_sqlBuffer.toString(), Map.class);
			
			map.put("groupHandleTravllerVisas", groupHandleTravllerVisas);
			if(flag){
				groupCode = null;
			}
		}
		
		return groupHandleTravllers;
		
	}
	
	/**
	 * 签证团控游客列表
	 * @param request
	 * @param response
	 * @param visaOrderForm
	 * @param shenfen
	 * @param common
	 * @return
	 */
	public Page<Map<String,Object>> searchGroupControlTravelList(HttpServletRequest request,HttpServletResponse response,
			GroupHandleSearchForm searchForm, String shenfen, DepartmentCommon common) {
		
		StringBuffer sqlBuffer = new StringBuffer();
		
		sqlBuffer.append(" SELECT ");
		sqlBuffer.append(" gc.id groupControlId,");//团控Id
		sqlBuffer.append(" gc.saler_id salerId,");// 销售Id
		sqlBuffer.append(" gc.saler_name salerName,");// 销售姓名
		sqlBuffer.append(" gc.agentinfo_id agentInfoId,");// 渠道id
		sqlBuffer.append(" gc.agentinfo_name agentInfoName,");//渠道名称
		sqlBuffer.append(" gc.activity_group_code groupCode,");// 团号
		sqlBuffer.append(" gc.activity_product_kind productType,");// 产品类型
		sqlBuffer.append(" gc.activity_product_name productName,");// 产品名称
		sqlBuffer.append(" gcv.traveler_id traveler_id,");//游客Id
		sqlBuffer.append(" gcv.traveler_name travellerName,");//游客姓名
		sqlBuffer.append(" gcv.passport_num passportNum,");// 护照号
		sqlBuffer.append(" GROUP_CONCAT(DISTINCT gcv.visa_country_name SEPARATOR '/') countryName,");//签证国家名称
		sqlBuffer.append(" GROUP_CONCAT(gcv.visa_country_id) country_ids,");//签证国家id
		sqlBuffer.append(" t.passportStatus,");//护照状态
		sqlBuffer.append(" t.sex,");//性别
		sqlBuffer.append(" t.payed_moneySerialNum payedMoneyUuid,");//已付款uuid
		sqlBuffer.append(" t.delFlag ");
		
		sqlBuffer.append(" FROM group_control_visa gcv ");
		sqlBuffer.append(" LEFT JOIN group_control gc ON gcv.grouphandle_id = gc.id ");
		sqlBuffer.append(" LEFT JOIN traveler t ON  gcv.traveler_id=t.id ");
		sqlBuffer.append(" LEFT JOIN sys_user su ON gc.saler_id=su.id ");
		sqlBuffer.append(" LEFT JOIN activitygroup ag ON gc.activity_group_id = ag.id ");
		sqlBuffer.append(" LEFT JOIN productorder porder ON gc.order_id = porder.id ");  //不显示不占位 取消  和删除订单的数据
		
		sqlBuffer.append(" WHERE su.companyId="+UserUtils.getUser().getCompany().getId());
		sqlBuffer.append(" AND gc.delFlag='0' AND gcv.delFlag='0' AND t.delFlag in(0,3,5) ");
		sqlBuffer.append(" AND (porder.payStatus in (3,4,5) or porder.placeHolderType = 1 or porder.paymentType != 1) ");
		
		//部门ID不为空时拼接部门SQL的where条件
		if (StringUtils.isNotBlank(common.getDepartmentId())) {
			groupBatchHandleService.departmentSQL(common,sqlBuffer,2);
		}
		
		//拼接查询条件
		sqlBuffer = searchTravelParam(searchForm, sqlBuffer, "");
		
		sqlBuffer.append(" GROUP BY gcv.traveler_id ");
		
		//分页查询订单数据
		String orderBy = request.getParameter("youkeOrderBy");
		
		Page<Map<String,Object>> page = new Page<Map<String,Object>>(request, response);
				
		if (StringUtils.isBlank(orderBy)) {
			page.setOrderBy("gc.createDate DESC");
		} else {
			page.setOrderBy(orderBy);
		}
				
		Page<Map<String,Object>> resultPage = groupHandleDao.findBySql(page, sqlBuffer.toString(),Map.class);
				
		//处理二级浮窗结构
		List<Map<String,Object>> resultList = getGroupHandleTravllerVisas(resultPage.getList(),null);
		
		//处理查询结果
		Map<String,String> orderTypeMap = new HashMap<String, String>();//产品类型
		orderTypeMap.put("1", "单团");
		orderTypeMap.put("2", "散拼");
		orderTypeMap.put("3", "游学");
		orderTypeMap.put("4", "大客户");
		orderTypeMap.put("5", "自由行");
		orderTypeMap.put("6", "单办签");
		orderTypeMap.put("10", "游轮");
		for(Map<String,Object> resultMap : resultList){
			//处理团队类型
			resultMap.put("orderTypeName", orderTypeMap.get(resultMap.get("productType").toString()));
		}
		
		resultPage.setList(resultList);		
		
		return resultPage;
	}
	
	/**
	 * 团控游客列表 搜索条件拼接
	 * @param searchForm
	 * @param sqlBuffer
	 * @param shenfen
	 * @return
	 */
	private StringBuffer searchTravelParam(GroupHandleSearchForm searchForm, StringBuffer sqlBuffer, String shenfen) {
		//验证参数是否合法
		if(null == searchForm){
			return sqlBuffer;
		}
				
		//团号，产品名称的查询条件 
		if(null != searchForm.getCommonCode() && !"".equals(searchForm.getCommonCode().trim())){
			String commonCode = searchForm.getCommonCode().replace("'", "''");
			sqlBuffer.append(" AND (gc.activity_product_name like '%"+commonCode.trim()+"%' or gc.activity_group_code like '%"+commonCode.trim()+"%' )");
		}
				
		//游客名称
		if(null != searchForm.getTravelerName() && !"".equals(searchForm.getTravelerName().trim())){
			String travelName = searchForm.getTravelerName().replace("'", "''");
			sqlBuffer.append(" AND gcv.traveler_name LIKE  '%"+travelName.trim()+"%' ");
		}
		
		//销售
		if(StringUtils.isNotBlank(searchForm.getSalerId())){
			sqlBuffer.append(" AND gc.saler_id="+searchForm.getSalerId());
		}
		
		//渠道
		if(StringUtils.isNotBlank(searchForm.getAgentinfoId())){
			sqlBuffer.append(" AND gc.agentinfo_id="+searchForm.getAgentinfoId());
		}
		
		//团队类型
		if(StringUtils.isNotBlank(searchForm.getActivityProductKind())){
			sqlBuffer.append(" AND gc.activity_product_kind="+searchForm.getActivityProductKind());
		}
		
		//签证国家
		if(null != searchForm.getVisaCountryId() && !"".equals(searchForm.getVisaCountryId().trim())){
			sqlBuffer.append(" AND gcv.visa_country_id = "+searchForm.getVisaCountryId()+" ");
		}
				
	    //签证类型
		if(null != searchForm.getVisaTypeId() && !"".equals(searchForm.getVisaTypeId().trim())){
			sqlBuffer.append(" AND gcv.visa_type_id = "+searchForm.getVisaTypeId()+" ");
		}
				
		//预计约签时间-开始
		if(null != searchForm.getAboutSigningTimeStart() && !"".equals(searchForm.getAboutSigningTimeStart().trim())){
			sqlBuffer.append(" AND gcv.about_signing_time >= '"+searchForm.getAboutSigningTimeStart()+" 00:00:00"+"' ");
		}
		//预计约签时间-结束
		if(null != searchForm.getAboutSigningTimeEnd() && !"".equals(searchForm.getAboutSigningTimeEnd().trim())){
			sqlBuffer.append(" AND gcv.about_signing_time <= '"+searchForm.getAboutSigningTimeEnd()+" 23:59:59"+"' ");
		}
				
		//实际签约时间-开始
		if(null != searchForm.getSigningTimeStart() && !"".equals(searchForm.getSigningTimeStart().trim())){
			sqlBuffer.append(" AND gcv.signing_time >= '"+searchForm.getSigningTimeStart()+" 00:00:00"+"' ");
		}
		//实际签约时间-结束
		if(null != searchForm.getSigningTimeEnd() && !"".equals(searchForm.getSigningTimeEnd().trim())){
			sqlBuffer.append(" AND gcv.signing_time <= '"+searchForm.getSigningTimeEnd()+" 23:59:59"+"' ");
		}
				
		//签证类型
		if(null != searchForm.getVisaStauts() && !"".equals(searchForm.getVisaStauts().trim())){
			sqlBuffer.append(" AND gcv.visa_stauts = "+searchForm.getVisaStauts()+" ");
		}
		
		return sqlBuffer;
	}


    /**
     * 根据group_control_visa表的id查询信息
     * @param arrGVCIds
     * @return
     */
	public List<Map<String,Object>>  getInfoByGCVIds(String arrGCVIds) {
		
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("SELECT ")
				 .append("gcv.id, ") //这里取group_control_visa表的id是为保存操作准备的
		         .append("gcv.traveler_id, ")
		         .append("gcv.traveler_name, ")
				 .append("gcv.passport_num, ")
				 .append("gcv.visa_country_name, ")
				 .append("gcv.visa_type_name, ")
				 .append("gcv.visa_consulardistric_name, ")
				 .append("gcv.visa_handle_unit, ")
				 .append("gcv.visa_stauts, ")
				 .append("gcv.about_signing_time, ")
				 .append("gcv.signing_time, ")
				 .append("gcv.visa_delivery_time, ")
				 .append("gcv.visa_got_time, ")
				 .append("gcv.supplementaryinfo_time ")
				 .append("FROM ")
				 .append("group_control_visa gcv ")
				 .append("WHERE ")
				 .append("gcv.id IN ("+arrGCVIds+") ");
		List<Map<String,Object>> groupHandleTravllerVisas = groupHandleVisaDao.findBySql(sqlBuffer.toString(), Map.class);
		for (Map<String, Object> map : groupHandleTravllerVisas) {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		/*	Date newDate = (Date)map.get("about_signing_time");
			String newDateStr = sdf.format(newDate);
			map.put("about_signing_time", newDateStr);*/
			Date aboutSigningTime2Date=(Date)map.get("about_signing_time");
			Date signingTime2Date=(Date)map.get("signing_time");
			Date visaDeliveryTime2Date=(Date)map.get("visa_delivery_time");
			Date visaGotTime2Date=(Date)map.get("visa_got_time");
			Date supplementaryInfoTime2Date=(Date)map.get("supplementaryinfo_time");
			if(aboutSigningTime2Date!=null){ //Avoid for null pointer exception
				String aboutSigningTime2Date2Str=sdf.format(aboutSigningTime2Date);
				map.put("about_signing_time", aboutSigningTime2Date2Str);
			}
			if(signingTime2Date!=null){//Avoid for null pointer exception
				String signingTime2Date2Str=sdf.format(signingTime2Date);
				map.put("signing_time", signingTime2Date2Str);
			}
			if(visaDeliveryTime2Date!=null){//Avoid for null pointer exception
				String visaDeliveryTime2Date2Str=sdf.format(visaDeliveryTime2Date);
				map.put("visa_delivery_time", visaDeliveryTime2Date2Str);
			}
			if(visaGotTime2Date!=null){//Avoid for null pointer exception
				String visaGotTime2Date2Str=sdf.format(visaGotTime2Date);
				map.put("visa_got_time", visaGotTime2Date2Str);
			}
			if(supplementaryInfoTime2Date!=null){//Avoid for null pointer exception
				String supplementaryInfoTime2Date2Str=sdf.format(supplementaryInfoTime2Date);;
				map.put("supplementaryinfo_time", supplementaryInfoTime2Date2Str);
			}
		}
		return groupHandleTravllerVisas;
	}


    /**
     * 更新group_control_visa表中的部分信息
     * @param strVisaHandleUnitIds
     * @param strVisaHandleUnitsValues
     * @param strVisaStatusSelectedValues
     * @param strSigningTimeValues
     * @param strVisaDeliveryTimeValues
     * @param strVisaGotTimeValues
     * @param strSupplementaryInfoTimeValues
     */
	@Transactional
	public void updatePartVisaInfo(String strGroupControlVisaIdValues, String strVisaHandleUnitsValues,
			String strVisaStatusSelectedValues, String strSigningTimeValues, String strVisaDeliveryTimeValues,
			String strVisaGotTimeValues, String strSupplementaryInfoTimeValues) {
		
		//将传过来的string类型的字符串转换为String类型的数组
		String[]arrGroupControlVisaIdValues=strGroupControlVisaIdValues.split(",");
		String[]arrVisaHandleUnitsValues=strVisaHandleUnitsValues.split(",");
		String[]arrVisaStatusSelectedValues=strVisaStatusSelectedValues.split(",");
		String[]arrSigningTimeValues=strSigningTimeValues.split(",");
		String[]arrVisaDeliveryTimeValues=strVisaDeliveryTimeValues.split(",");
		String[]arrVisaGotTimeValues=strVisaGotTimeValues.split(",");
		String[]arrSupplementaryInfoTimeValues=strSupplementaryInfoTimeValues.split(",");
		
		//**************************设置更新时间的格式-tgy-s*****************************//
		SimpleDateFormat sdf4UpdateDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//**************************设置更新时间的格式-tgy-e****************************//
		
		//***************************获得更新人的id-tgy-s*****************************//
		   Long idOfUpdateBy=UserUtils.getUser().getId();
		//***************************获得更新人的id-tgy-e*****************************//
		StringBuffer sqlBuffer = new StringBuffer();
		for(int i=0;i<arrGroupControlVisaIdValues.length;i++){
			sqlBuffer.append("UPDATE  group_control_visa gcv  ");
			         if(arrVisaHandleUnitsValues[i].contains("null")){//处理办签单位为空值的情况
			        	 sqlBuffer.append("SET gcv.visa_handle_unit ='' ");
			         }else{ //处理办签单位不为空值的情况
			        	 sqlBuffer.append("SET gcv.visa_handle_unit = '"+arrVisaHandleUnitsValues[i]+"' ");
			         }
		         	 sqlBuffer.append(",gcv.visa_stauts='"+arrVisaStatusSelectedValues[i]+"'");
			         //********处理时间值为空的情况-tgy-start***********************//
			         if(arrSigningTimeValues[i].contains("null")){
			        	 sqlBuffer.append(",gcv.signing_time=NULL ");
			         }else{
			        	 sqlBuffer.append(",gcv.signing_time='"+arrSigningTimeValues[i]+"'");
			         }
			         if(arrVisaDeliveryTimeValues[i].contains("null")){
			        	 sqlBuffer.append(",gcv.visa_delivery_time=NULL ");
			         }else{
			        	 sqlBuffer.append(",gcv.visa_delivery_time='"+arrVisaDeliveryTimeValues[i]+"'"); 
			         }
			         if(arrVisaGotTimeValues[i].contains("null")){
			        	 sqlBuffer.append(",gcv.visa_got_time=NULL ");
			         }else{
			        	 sqlBuffer.append(",gcv.visa_got_time='"+arrVisaGotTimeValues[i]+"' "); 
			         }
			         if(arrSupplementaryInfoTimeValues[i].contains("null")){
			        	 sqlBuffer.append(",gcv.supplementaryinfo_time=NULL ");
			         }else{
			        	 sqlBuffer.append(",gcv.supplementaryinfo_time='"+arrSupplementaryInfoTimeValues[i]+"' "); 
			         }
			        //***********处理时间值为空的情况-tgy-end***********************//
		    sqlBuffer.append(",gcv.updateDate='"+sdf4UpdateDate.format(new Date())+"' ")//更新时间
		             .append(",gcv.updateBy='"+idOfUpdateBy+"' ")//更新人的id
		         	 .append("WHERE gcv.id ='"+arrGroupControlVisaIdValues[i]+"'");
		    groupHandleVisaDao.updateBySql(sqlBuffer.toString());
		    sqlBuffer.delete(0, sqlBuffer.length());//StringBuffer具有缓存,每次更新需要清空缓存. 
		}
		
	}
	
	/**
	 * 依据订单id获取团控记录
	 * @param orderId
	 * @return
	 */
	public GroupHandle findByOrderId(Long orderId) {
		return groupHandleDao.findByOrderId(Integer.parseInt(orderId.toString()));
	}
	
}
