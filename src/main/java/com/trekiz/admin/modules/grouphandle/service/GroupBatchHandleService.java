/** 
* Copyright 2015 QUAUQ Technology Co. Ltd. 
* All right reserved.
*/
package com.trekiz.admin.modules.grouphandle.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.grouphandle.dao.GroupHandleDao;
import com.trekiz.admin.modules.grouphandle.dao.GroupHandleVisaDao;
import com.trekiz.admin.modules.grouphandle.form.GroupHandleSearchForm;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Menu;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.repository.MenuDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author wenchao.lv@quauq.com
 * @createDate 2016-1-29
 */
@Service
public class GroupBatchHandleService {
	
	
	@Autowired
	private GroupHandleDao groupHandleDao;
	
	@Autowired
	private GroupHandleVisaDao groupHandleVisaDao;
	@Autowired
	private MenuDao menuDao;
	
	@Autowired
	private DepartmentDao departmentDao;

	
	
	/**@author wenchao.lv@quauq.com
	 * 签务团控列表
	 * 批量更新签证状态
	 * visaStatus 签证状态
	 * visaId group-control-visa表的主键id
	 * @return 空(null) 正常结束
	 *         "更新错误" 表示更新异常
	 * */
	public String batchUpdateVisaStatus(String visaIds, String visaStatus) {
		boolean status = groupHandleVisaDao.batchUpdateVisaStatus(visaIds, visaStatus);
		return status==true?"签证状态保存成功":"签证状态保存失败";
	}
	
	/**@author wenchao.lv@quauq.com
	 * 签务团控列表
	 * 批量更新护照状态
	 * @param passportStatus 护照状态
	 * @param travellerIds 游客Ids
	 * @return
	 */
	public String batchUpdatePassportStatus(String passportStatus, String travelerIds) {
		boolean status = groupHandleVisaDao.batchUpdatePassportStatus(passportStatus, travelerIds);
		return status==true?"护照状态保存成功":"护照状态保存失败";
	}
	
	/**
	 * @author wenchao.lv@quauq.com
	 * 签务团控列表
	 * 批量设置时间
	 * @param groupHandleVisa group-control-visa表 映射的实体
	 * @param visaIds group-control-visa表的多个主键id组成的字符串
	 * @return
	 * @throws Exception 
	 */
	public String batchUpdateTime(HttpServletRequest request, String visaIds) throws Exception{
		boolean time = groupHandleVisaDao.batchUpdateTime(request, visaIds);
		return time==true?"时间设置成功":"时间设置失败";
	}
	@Transactional
	public String saveGroupControlVisa(HttpServletRequest request) throws Exception{
		String visaHandleUnit = request.getParameter("visaHandleUnitId");//办签单位
		String visaStatus = request.getParameter("visaStatusIds");//签证状态
		String passportStatus = request.getParameter("passportStatus");//护照状态
		String travelerId = request.getParameter("travlerId");//游客Id
										
		String groupId = request.getParameter("groupId");//签控Id
		String signingTime=request.getParameter("signingTime");
		String visaDeliveryTime = request.getParameter("visaDeliveryTime");
		String visaGotTime=request.getParameter("visaGotTime");
		String supplementaryInfoTime=request.getParameter("supplementaryInfoTime");

		String groupSql="update group_control_visa set updateBy = ? , updateDate = ? , visa_handle_unit = ? ,visa_stauts = ? , signing_time = ? ,visa_delivery_time =? , visa_got_time =? , supplementaryinfo_time=?  where id = ?";
		
		SimpleDateFormat df ;
		List<Object> par = new ArrayList<Object>();
		
		//更新人
		par.add(UserUtils.getUser().getId());
		//更新时间 
    	par.add(new Date());
		
		//办签单位
		if(null==visaHandleUnit||"".equals(visaHandleUnit)){
			par.add(null);
		}else{
			par.add(visaHandleUnit);
		}
		//签证状态
		par.add(Integer.parseInt(visaStatus));
		//实际约签时间
		if(null == signingTime || "".equals(signingTime)){
    		par.add(null);
    	}else{
    		if(signingTime.contains(":"))
    		df = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
    		else
    			df = new SimpleDateFormat("yyyy-MM-dd"); 
    		Date date = df.parse(signingTime);  
    		par.add(date);
    		
    	}
		
		//送签时间
		if(null == visaDeliveryTime || "".equals(visaDeliveryTime)){
			par.add(null);
		}else{
			if(visaDeliveryTime.contains(":"))
        		df = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
        		else
        			df = new SimpleDateFormat("yyyy-MM-dd"); 
			Date date = df.parse(visaDeliveryTime);  
			par.add(date);
			
		}
		
		//出签时间
		if(null == visaGotTime || "".equals(visaGotTime)){
			par.add(null);
		}else{
			if(visaGotTime.contains(":"))
        		df = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
        		else
        			df = new SimpleDateFormat("yyyy-MM-dd"); 
			Date date = df.parse(visaGotTime);  
			par.add(date);
			
		}
		
		//续补资料时间
		if(null == supplementaryInfoTime || "".equals(supplementaryInfoTime)){
			par.add(null);
		}else{
			if(supplementaryInfoTime.contains(":"))
        		df = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
        		else
        			df = new SimpleDateFormat("yyyy-MM-dd"); 
			Date date = df.parse(supplementaryInfoTime);  
			par.add(date);
			
		}
		//group_control_visa 主键
		par.add(Integer.parseInt(groupId));
		//groupHandleVisaDao.updatePassportStatusByTravelerId(Integer.parseInt(passportStatus), Long.parseLong(travelerId));
		boolean status = groupHandleVisaDao.savePassportStatus(Integer.parseInt(passportStatus), travelerId);
		if(status==false){
			return "护照保存失败";
		}else{
			boolean group = groupHandleVisaDao.saveGroup(groupSql, par);
			return group==true?"保存成功":"保存失败" ;
		}
		
	}
	
	/**
	 * 保存护照状态
	 * @param passportStatus
	 * @param travelerId
	 * @return
	 */
	public String saveTravlerPassportStatus(Integer passportStatus,String travelerId){
		boolean status = groupHandleVisaDao.savePassportStatus(passportStatus, travelerId);
		return status==true?"护照保存成功":"护照保存失败";
	}
	
	/**@author wenchao.lv@quauq.com
	 * 批量操作列表展示
	 * @param request
	 * @param response
	 * @param searchForm
	 * @param str
	 * @param common
	 * @return
	 */
	public Page<Map<String,Object>> visaGroupBatchEditView(HttpServletRequest request,HttpServletResponse response,GroupHandleSearchForm searchForm,String str,DepartmentCommon common){
		//v.traveler_id//游客id
		StringBuffer buffer = new StringBuffer();
		//SELECT g.*,v.* FROM group_control g LEFT JOIN group_control_visa v ON v.grouphandle_id = g.id LEFT JOIN traveler t ON t.id=v.traveler_id WHERE v.delFlag=0;
		buffer.append("SELECT gc.saler_name salerName, ");//销售名字
		buffer.append("gc.id gId, ");//groupControl主键
		buffer.append("gc.agentinfo_id agentinfoId, ");//渠道Id
		buffer.append("gc.agentinfo_name agentinfoName, ");//渠道名称
		buffer.append("gc.activity_group_code activityGroupCode, ");//团号
		buffer.append("v.id vId, ");//groupControlvisa主键
		buffer.append("v.traveler_name travelerName, ");//游客姓名
		buffer.append("v.passport_num passportNum, ");//护照号
		buffer.append("v.visa_country_name visaCountryName, ");//签证国家
		buffer.append("v.visa_type_name visaTypeName, ");//签证类型
		buffer.append("v.visa_consulardistric_name visaConsulardistricName, ");//签证领域
		buffer.append("v.visa_handle_unit visaHandleUnit, ");//办签单位
		buffer.append("v.visa_stauts visaStauts, ");//签证状态
		buffer.append("t.passportCode passportId, ");//护照编号
		buffer.append("t.passportStatus passportStatus, ");//游客护照状态
		buffer.append("t.id id, ");//游客主键id
		buffer.append("t.delFlag delFlag, ");//游客状态
		buffer.append("t.payment_type paymentType, ");//游客的结算方式
		buffer.append("v.about_signing_time aboutSigningTime, ");//预计约签时间
		buffer.append("v.signing_time signingTime, ");//实际约签时间		
		buffer.append("v.visa_delivery_time visaDeliveryTime, ");//送签时间
		buffer.append("v.visa_got_time visaGotTime, ");//出签时间
		buffer.append("v.supplementaryinfo_time supplementaryinfoTime ");//补签时间
		buffer.append("FROM group_control gc ");
		buffer.append("LEFT JOIN group_control_visa v ON v.grouphandle_id = gc.id ");
		buffer.append("LEFT JOIN productorder porder ON gc.order_id = porder.id ");
		//LEFT JOIN productorder porder ON gc.order_id = porder.id
		buffer.append("LEFT JOIN activitygroup ag ON gc.activity_group_id = ag.id ");
		buffer.append("LEFT JOIN traveler t ON t.id=v.traveler_id ");
		//buffer.append("LEFT JOIN visa va on t.id = va.traveler_id ");
		buffer.append("LEFT JOIN visa_order vo on vo.id = t.orderId ");
		buffer.append("LEFT JOIN agentinfo a ON vo.agentinfo_id = a.id ");
		buffer.append("LEFT JOIN visa_products vp on vp.id = vo.visa_product_id ");
		if(UserUtils.getUser().getCompany().getId()==68){
			buffer.append("LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE re1.flowType = 5 AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id) re ON t.id = re.travelerId ");
			//buffer.append(" LEFT JOIN (SELECT rr.order_id orderId,rr.status status,rr.traveller_id travelerId,rr.create_date createDate FROM review_new rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review_new re1 WHERE re1.process_type = '5' AND re1.product_type = '6' GROUP BY re1.traveller_id) r ON rr.id = r.id) re ON t.id = re.travelerId ");
		}else if(UserUtils.getUser().getCompany().getId()==71) {
			buffer.append("LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE re1.flowType = 20 AND re1.productType = 6 GROUP BY re1.orderId) r ON rr.id = r.id) re ON vo.id = re.orderId ");
		}else{
			buffer.append("LEFT JOIN (SELECT rr.orderId orderId,rr.status status,rr.travelerId travelerId,rr.createDate createDate FROM review rr RIGHT JOIN (SELECT MAX(re1.id) id FROM review re1 WHERE (re1.flowType = 5 OR re1.flowType = 20) AND re1.productType = 6 GROUP BY re1.travelerId) r ON rr.id = r.id) re ON t.id = re.travelerId ");
		}
		
		buffer.append("WHERE t.delFlag IN (0,3,5) ");
		buffer.append("AND (porder.payStatus in (3,4,5) or porder.placeHolderType = 1 or porder.paymentType != 1) ") ;
		//部门ID不为空时拼接部门SQL
		if (StringUtils.isNotBlank(common.getDepartmentId())) {
					departmentSQL(common,buffer,1);
				}
				
//				buffer.append(" where t.order_type = 6 and t.delFlag=0 and vo.visa_order_status != 100 and vo.del_flag = 0 and vo.create_by IS NOT NULL ");;
//				buffer.append(" and a.status = 1 ");
				
		//部门ID不为空时拼接部门SQL的where条件
		if (StringUtils.isNotBlank(common.getDepartmentId())) {
					departmentSQL(common,buffer,2);
				}
		
		//拼接搜索的SQL条件
		searchVisaGroupTravlelerSQL(searchForm,buffer,str);
		//列表排序
		String orderBy = request.getParameter("orderBy");
		Page<Map<String,Object>> page = new Page<Map<String,Object>>(request, response);
		if (StringUtils.isBlank(orderBy)) {
			page.setOrderBy("v.createDate DESC");
	       } else {
			page.setOrderBy(orderBy);
				  }
		Page<Map<String, Object>> map = groupHandleVisaDao.findBySql(page, buffer.toString(), Map.class);
		return map;
	}

	/**
	 * @param searchForm
	 * @param buffer
	 * @param str
	 * @return 
	 */
	private StringBuffer searchVisaGroupTravlelerSQL(GroupHandleSearchForm searchForm,StringBuffer buffer, String str) {
		// TODO Auto-generated method stub
		//验证参数是否合法
		if(null ==searchForm ){
			return buffer;
		}
		//销售
		if(null !=searchForm.getSalerId()&&!"".equals(searchForm.getSalerId().trim())){
			String salerName = searchForm.getSalerId().replace("'", "''");
			buffer.append(" and gc.saler_name = '"+salerName.trim()+"' ");
		}
		//游客名称
		if(null != searchForm.getTravelerName() &&!"".equals(searchForm.getTravelerName().trim())){
			String travelName = searchForm.getTravelerName().replace("'", "''");
			buffer.append(" and t.name like '%"+travelName.trim()+"%' ");
		}
		//公共编号(订单号,产品编号)的查询条件
		if(null != searchForm.getCommonCode() && !"".equals(searchForm.getCommonCode().trim())){
			String commonCode = searchForm.getCommonCode().replace("'", "''");
			buffer.append(" and ( v.activity_group_code like '%"+commonCode.trim()+"%' or gc.activity_product_name like '%"+commonCode.trim()+"%' )");
		}
		//渠道ID
		if(null != searchForm.getAgentinfoId() && !"".equals(searchForm.getAgentinfoId().trim())){
			buffer.append(" and gc.agentinfo_id = '"+searchForm.getAgentinfoId()+"' ");
		}
//		//创建者编号
//		if(null != searchForm.getCreateBy() && !"".equals(searchForm.getCreateBy().trim())){
//			buffer.append(" and su.name = '"+searchForm.getCreateBy().trim()+"' ");
//		}
		//支付状态
//		if(null != searchForm.getOrderPayStatus() && !"".equals(searchForm.getOrderPayStatus().trim())){
//			buffer.append(" and vo.payStatus = '"+searchForm.getOrderPayStatus()+"' ");
//		}
		//参团类型
		if(null != searchForm.getActivityProductKind() && !"".equals(searchForm.getActivityProductKind().trim())){
			if (Context.ORDER_STATUS_VISA.equals(searchForm.getOrderBy().trim())){
				buffer.append(" and t.main_order_id is null ");
			}else{
				buffer.append(" and gc.activity_product_kind = '"+searchForm.getActivityProductKind()+"' ");
			}
		}
		//签证国家编号
		if(null != searchForm.getVisaCountryId() && !"".equals(searchForm.getVisaCountryId().trim())){
			buffer.append(" and v.visa_country_id = '"+searchForm.getVisaCountryId()+"' ");
		}
		//签证状态
		if(null != searchForm.getVisaStauts() && !"".equals(searchForm.getVisaStauts().trim())){
			buffer.append(" and v.visa_stauts = "+searchForm.getVisaStauts());
		}
		//签证类型
		if(null != searchForm.getVisaTypeId() && !"".equals(searchForm.getVisaTypeId().trim())){
			buffer.append(" and v.visa_type_id = '"+searchForm.getVisaTypeId()+"' ");
		}
		//预计约签时间-开始时间
		if(null != searchForm.getAboutSigningTimeStart() && !"".equals(searchForm.getAboutSigningTimeStart().trim())){
			buffer.append(" and v.about_signing_time >= '"+searchForm.getAboutSigningTimeStart()+" 00:00:00' ");
		}
		//预计约签时间-结束时间
		if(null != searchForm.getAboutSigningTimeEnd() && !"".equals(searchForm.getAboutSigningTimeEnd().trim())){
			buffer.append(" and v.about_signing_time <= '"+searchForm.getAboutSigningTimeEnd()+" 23:59:59' ");
		}
//		//实际出团时间-开始时间
//		if(null != searchForm.getStartOutStart() && !"".equals(searchForm.getStartOutStart().trim())){
//			buffer.append(" and v.start_out >= '"+searchForm.getStartOutStart()+" 00:00:00' ");
//		}
//		//实际出团时间-结束时间
//		if(null != searchForm.getStartOutEnd() && !"".equals(searchForm.getStartOutEnd().trim())){
//			buffer.append(" and v.start_out <= '"+searchForm.getStartOutEnd()+" 23:59:59' ");
//		}
		//实际签约时间-开始时间
		if(null != searchForm.getSigningTimeStart() && !"".equals(searchForm.getSigningTimeStart().trim())){
			buffer.append(" and v.signing_time >= '"+searchForm.getSigningTimeStart()+" 00:00:00' ");
		}
		//实际签约时间-结束时间
		if(null != searchForm.getSigningTimeEnd() && !"".equals(searchForm.getSigningTimeEnd().trim())){
			buffer.append(" and v.signing_time <= '"+searchForm.getSigningTimeEnd()+" 23:59:59' ");
		}
		//下单时间-开始时间
//		if(null != searchForm.getCreateDateStart() && !"".equals(searchForm.getCreateDateStart().trim())){
//			buffer.append(" and vo.create_date >= '"+searchForm.getCreateDateStart()+" 00:00:00' ");
//		}
//		//下单时间-结束时间
//		if(null != searchForm.getCreateDateEnd() && !"".equals(searchForm.getCreateDateEnd().trim())){
//			buffer.append(" and vo.create_date <= '"+searchForm.getCreateDateEnd()+" 23:59:59' ");
//		}
		
		return buffer;
		
	}

	/**
	 * @param common
	 * @param buffer
	 * @param i
	 * @return 
	 */
	//拼接部门SQL type=1:拼接left join type=2：拼接where条件
	public StringBuffer departmentSQL(DepartmentCommon common, StringBuffer buffer,int type) {
		// TODO Auto-generated method stub
		/** 要查询部门ID */
		String departmentId = common.getDepartmentId();
		/** 是否是管理员 */
		boolean isManager = common.getRoleTypeList().contains(Context.ROLE_TYPE_MANAGER);
		/** 是否是计调经理 */
		boolean isOPManager = common.getRoleTypeList().contains(Context.ROLE_TYPE_OP_EXECUTIVE);
		/** 是否是计调 */
		boolean isOP = common.getRoleTypeList().contains(Context.ROLE_TYPE_OP);
		/** 是否是销售经理 */
		boolean isSaleManager = common.getRoleTypeList().contains(Context.ROLE_TYPE_SALES_EXECUTIVE);
		/** 是否是销售 */
		boolean isSale = common.getRoleTypeList().contains(Context.ROLE_TYPE_SALES);
		
		/** 部门内部人员是否能相互查看，默认不能  add by 2016/01/18 for requirement ID 113*/
		boolean flag = whetherSelectAllDeptDate(common);
		
		if (type==1) {
			/*用in语句替代left join ,该处左链接查询有问题*/
//			if (!isManager) {
//				if(flag){//同部门下的不同角色订单可见,相当于同时拥有计调经理和销售经理角色
//					sqlBuffer.append("LEFT JOIN(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0') opdept ON vp.createBy=opdept.id ");
//					sqlBuffer.append("LEFT JOIN(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2) AND dept.delFlag = '0' AND su.delFlag = '0') saledept ON vo.create_by=saledept.id ");
//				}else{
//					if (isOPManager) {
//						sqlBuffer.append("LEFT JOIN(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0') opdept ON vp.createBy=opdept.id ");
//					}
//					if (isSaleManager) {
//						sqlBuffer.append("LEFT JOIN(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2) AND dept.delFlag = '0' AND su.delFlag = '0') saledept ON vo.create_by=saledept.id ");
//					}
//					if(!isSaleManager && !isOPManager && !isOP && !isSale){
//						sqlBuffer.append("LEFT JOIN(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0') opdept ON vp.createBy=opdept.id ");
//						sqlBuffer.append("LEFT JOIN(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2) AND dept.delFlag = '0' AND su.delFlag = '0') saledept ON vo.create_by=saledept.id ");
//					}
//				}
//			}
		}else if(type==2){
			if (!isManager) {
				if(flag){
					buffer.append(" AND (ag.createBy in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2 or sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0' ) ");
					buffer.append(" OR gc.createBy in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2 or sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0' ) ");
					buffer.append(" OR gc.saler_id in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2) AND dept.delFlag = '0' AND su.delFlag = '0' ) ) ");
				}else{//未勾选权限 
					if (isOPManager && isSaleManager) {
						buffer.append(" AND (ag.createBy in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2 or sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0' ) ");
						buffer.append(" OR gc.createBy in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2 or sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0' ) ");
						buffer.append(" OR gc.saler_id in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2) AND dept.delFlag = '0' AND su.delFlag = '0' ) ) ");
					}else if(isOPManager){
						buffer.append(" AND ( ag.createBy in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0' ) ");
						/*下单人是本部门或子部门的计调或基调主管*/
						buffer.append(" OR gc.createBy in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0') ) ");
					}else if(isSaleManager){
						buffer.append(" AND (gc.createBy in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2) AND dept.delFlag = '0' AND su.delFlag = '0' ) ");
						buffer.append(" OR gc.saler_id in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2) AND dept.delFlag = '0' AND su.delFlag = '0' ) ");
						/*本部门或子部门的销售或销售主管 发布产品所属订单*/
						buffer.append(" OR ag.createBy in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2) AND dept.delFlag = '0' AND su.delFlag = '0' ) ) ");
					}
					if(!isSaleManager && !isOPManager && (isOP || isSale)){
						buffer.append(" AND (ag.createBy='"+UserUtils.getUser().getId()+"' OR (gc.createBy='"+UserUtils.getUser().getId()+"' OR gc.saler_id='"+UserUtils.getUser().getId()+"')) ");
					}
					if(!isSaleManager && !isOPManager && !isOP && !isSale){
						//buffer.append("AND (vp.createBy=opdept.id OR (vo.create_by=saledept.id OR vo.salerId=saledept.id)) ");
						buffer.append(" AND (ag.createBy in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2 or sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0' ) ");
						buffer.append(" OR gc.createBy in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2 or sr.roleType=3 or sr.roleType=4) AND dept.delFlag = '0' AND su.delFlag = '0' ) ");
						buffer.append(" OR gc.saler_id in(SELECT DISTINCT su.id id FROM sys_user su LEFT JOIN sys_user_role sur ON su.id = sur.userId LEFT JOIN sys_role sr ON sr.id=sur.roleId LEFT JOIN department dept ON sr.deptId=dept.id WHERE su.companyId = "+UserUtils.getUser().getCompany().getId()+" AND (dept.id = '"+departmentId+"' OR dept.parent_ids LIKE '%,"+departmentId+",%' ) AND (sr.roleType=1 or sr.roleType=2) AND dept.delFlag = '0' AND su.delFlag = '0' ) ) ");
					}
				}
			}
		}
		
		return buffer;
	}

	/**
	 * @param common
	 * @return
	 */
	private boolean whetherSelectAllDeptDate(DepartmentCommon common) {
		/** 判断是否是部门之间人员可以相互查看请求 */
		List<Menu> menuList = menuDao.findByPermission(common.getPermission());//查询请求的菜单
		Department dept = departmentDao.findOne(Long.parseLong(common.getDepartmentId()));//查询用于查询的部门
		if (CollectionUtils.isNotEmpty(menuList)) {
    		List<Integer> list = departmentDao.findSelectIdsByDeptId(dept.getId());
    		if (CollectionUtils.isNotEmpty(list)) {
    			//签务签证订单-批量操作，若签务签证订单勾选权限，则批量操作页也拥有权限
    			boolean flag = false;//是否选中签务签证订单权限，默认否
    			for(Integer menuId : list){
    				Menu menu = menuDao.findOne(menuId.longValue());
    				if("签务签证订单".equals(menu.getName())){
    					flag = true;
    					break;
    				}
    			}
    			if(flag && "visaOrderForOp:batch:operation".equals(common.getPermission())){//隐藏的菜单-批量操作页
    				return true;
    			}
    			
    			return list.contains(menuList.get(0).getId().intValue());
    		}
    	}
		return false;
	}	

}
