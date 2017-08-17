package com.trekiz.admin.modules.review.visareturnreceipt.repository;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.reviewflow.repository.ReviewCompanyDao;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Repository
public class VisaReturnReceiptDaoImpl extends BaseDaoImpl<Map<String, Object>>
		implements IVisaReturnReceiptDao {
	
	@Autowired
	  private ReviewCompanyDao reviewCompanyDao;


	/**
	 * 还签证收据申请
	 */
	@Override
	public Page<Map<String, Object>> queryVisaReturnReceiptReviewList(
			HttpServletRequest request, HttpServletResponse response,
			String groupCode, String startTime, String endtime, String agent,
			String orderCreateBy,String saler, String jdsaler, String status, 
			List<Integer>levels, UserJob userJob, Long reviewCompanyId,List<Long> subIds) {
		/**
		 * SQL说明：
		 * 订单创建人为销售：vorder.create_by AS salecreateby
		 * 产品创建人为计调：vp.createBy AS jidcreateby
		 * 还签证收据申请的产品类型为6： rev.productType = 6
		 * 还签证收据的流程类型为4：rev.flowType = 4
		 * 收据金额：revd.myKey='receiptAmount
		 * 
		 * -----------2015-03-13-----------
		 * sql修改：
		 * 1.表 review_detail 字段由 rid 改为 review_id
		 * 2.表review 字段 companyId 改为review_company_id
		 * 
		 * ---------- 2015-04-20----------
		 * 1.添加对打印时间和打印状态的查询：rev.printFlag as isPrintFlag, rev.printTime as printTime
		 * 2.审核列表添加带引状态列
		 * 3.打印后，mouse hover 后显示首次打印时间
		 */
		String querySql = "SELECT rev.id as revid, rev.productType as prdtype, rev.`status` as revstatus,rev.`status` as revstatusforprint, rev.updateBy as lastoperator,"
				+ " rev.nowLevel as curlevel, rev.flowType as fltype, rev.createDate as createtime, rev.updateDate as updatetime, rev.printFlag as isPrintFlag, rev.printTime as printTime,"
				+ " vorder.order_no as orderno, vorder.id as orderid, vorder.group_code as groupcode, vorder.create_date as orderdate, vorder.agentinfo_id AS agentid,"
				+ " vorder.create_by AS orderCreateby,vorder.salerId AS salecreateby, vp.createBy AS jidcreateby, t.name as tname,t.id as tid, t.payPriceSerialNum as payprice, revd.myValue "
				+ "FROM review rev, visa_order vorder, visa_products vp, traveler t, review_detail revd "
				+ "WHERE rev.orderId = vorder.id AND vorder.visa_product_id = vp.id AND rev.travelerId = t.id  "
				+ "AND rev.productType = 6 AND rev.flowType = 4 and revd.review_id= rev.id  and revd.myKey='receiptAmount' "
		        + "AND rev.review_company_id = " + reviewCompanyId + " AND rev.companyId = "+UserUtils.getUser().getCompany().getId() +" ";//适应多批发商
		
		String subIdsStr = "";
		int nFlag = 0;
		for(Long subId : subIds){
			if(nFlag == 0) {
				subIdsStr += subId;
				nFlag++;
			} else {
				subIdsStr += "," + subId;
			}
		}
		if (subIdsStr != null && !"".equals(subIdsStr.trim())) {
			querySql += " and rev.deptId in(" + subIdsStr + ") ";
		}
		
		if (groupCode != null && !"".equals(groupCode.trim())) {
			querySql += " and vorder.group_code like '%" + groupCode + "%' ";
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			startTime += " 00:00:00";
			querySql += " and rev.createDate > '" + startTime + "'";
		}
		if (endtime != null && !"".equals(endtime)) {
			endtime += " 23:59:59";
			querySql += " and rev.createDate <= '" + endtime + "'";
		}
		
		//-------------------------------------------------非常重要--------------------------------------------------
		//用于过滤批次审核的记录：批次的上线后，非批次审核列表不应能看到批次提交的记录
		//
		if (68==UserUtils.getUser().getCompany().getId()) {
			querySql += " and rev.createDate < '2015-06-04 23:59:59'";
		}
		
		if (agent != null && !"".equals(agent.trim())) {
			querySql += " and vorder.agentinfo_id = " + Long.parseLong(agent);
		}
		
		
		//------wxw added 2015-08-20   下单人------
		if (orderCreateBy != null && !"".equals(orderCreateBy.trim())) {
			querySql += " and vorder.create_by = " + Long.parseLong(orderCreateBy);
		}
		
		if (saler != null && !"".equals(saler.trim())) {
			querySql += " and vorder.salerId = " + Long.parseLong(saler);
		}
		
		if (jdsaler != null && !"".equals(jdsaler.trim())) {
			querySql += " and vp.createBy = " + Long.parseLong(jdsaler);
		}
		
		//-------added for adapt deptId  2015-2-6--------
		//querySql += " and rev.deptId = '" + dept + "' ";
		
		/**
		 * 获取职位部门，根据部门id获取 topLevel，否可可能出现审核通过查不到记录的问题
		 */
		Integer deptLevel = userJob.getDeptLevel();
		Long deptId;
		if(deptLevel == 1){//一级部门
			deptId = userJob.getDeptId();
		} else {
			deptId = userJob.getParentDept();
		}
		
		/*根据不同的状态 添加不同的SQL过滤条件*/
		if (status != null && !"".equals(status)) {// 0 已驳回 1 待审核 2 审核成功 3 操作完成
			//审核状态过滤条件不为空
			Integer statusInt = Integer.parseInt(status);
			//1 如果为0 已驳回 1 待审核 取对应层级和对应状态
/*			if(statusInt == 0 || statusInt == 1){
				querySql += "and rev.`status` ='" + status + "' ";//审核状态
				querySql += "and rev.nowLevel = " + levels.get(0);//审核层级
			}
			//如果为2 审核成功
			if (statusInt == 2){
				Long companyId = UserUtils.getUser().getCompany().getId();
				//List<Long> listCompanyID = reviewCompanyDao.findReviewCompanyList(companyId, Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT);
				List<ReviewCompany> listCompany = reviewCompanyDao.findReviewCompanyDept(companyId, Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT, deptId);
				//ReviewCompany  listCompany = reviewCompanyDao.findOne(listCompanyID.get(0));
				if(null==listCompany||listCompany.size()<1){
					return null;
				}
				//Integer topLevel = listCompany.getTopLevel();
				Integer topLevel = listCompany.get(0).getTopLevel();
				//如果审核层级为最高层级 则取等于当前level 且和对应状态  否则 取大于当前level的记录
				if(topLevel == levels.get(0)){
					querySql += "and rev.`status` ='" + status + "' ";//审核状态
				
					querySql += "and (rev.`status` ='" + status + "' or (rev.`status` ='3'))";
					
					querySql += " and rev.nowLevel = " + levels.get(0);//审核层级
				} else {
					querySql += " and rev.nowLevel > " + levels.get(0);//审核层级
				}
			}*/
			
			
			if(statusInt == 1){//待审核
				querySql += " and rev.`status` ='" + status + "' ";//审核状态
				querySql += " and rev.nowLevel = " + levels.get(0);//审核层级
			}else if (statusInt == 5) {//审核中
				querySql += " and rev.`status` =1";//审核状态
			}else if (statusInt == 0) {//未通过
				querySql += " and rev.`status` =0";//审核状态
			}else if (statusInt == 2) {//已通过
				querySql += " and (rev.`status` ='2' or rev.`status` ='3')";//审核状态
			}else if (statusInt == 4) {//已取消
				querySql += " and rev.`status` =4";//审核状态
			}
	
			
			
		}else if("".equals(status)) {
			//审核过滤条件为空   处理有取消状态的情况
			//querySql += " and (rev.nowLevel > " + levels.get(0);
			//querySql += " or (rev.nowLevel = " + levels.get(0);
			//querySql += " and rev.active =1)) ";
			
		} else {
			//审核过滤条件为空
			//querySql += " and (rev.nowLevel > " + levels.get(0);
			//querySql += " or (rev.nowLevel = " + levels.get(0);
			//querySql += " and rev.active =1)) ";
			//querySql += " )) ";
			querySql += " and rev.`status` ='1' ";//审核状态
			querySql += " and rev.nowLevel = " + levels.get(0);//审核层级
		}

		String cOrderBy  = request.getParameter("orderCreateDateSort");// 创建日期排序标识
		String uOrderBy = request.getParameter("orderUpdateDateSort");// 更新日期排序标识
		
		// 排序条件
		querySql += " ORDER BY 1=1";
		if(cOrderBy!=null && !"".equals(cOrderBy)){
			querySql+=", createtime "+cOrderBy;
		}
		if(uOrderBy!=null && !"".equals(uOrderBy)){
			querySql+=", updatetime "+uOrderBy;
		}
		
		//System.out.println("querySql==="+querySql);
		return findBySql(new Page<Map<String, Object>>(request, response),
				querySql, Map.class);
	}
	
	
	/*********************   批次借款审核相关     开始      *********************/
	/*********************   批次借款审核相关     开始       *********************/

	/**
	 * 还签证收据申请
	 */
	@Override
	public Page<Map<String, Object>> queryVisaReturnReceiptBatchReviewList(
			HttpServletRequest request, HttpServletResponse response,
			String groupCode, String startTime, String endtime, String agent,String printstatus,
			String saler, String jdsaler, String status, List<Integer>levels, UserJob userJob, Long reviewCompanyId,List<Long> subIds) {
		/**
		 * SQL说明：
		 * 订单创建人为销售：vorder.create_by AS salecreateby
		 * 产品创建人为计调：vp.createBy AS jidcreateby
		 * 还签证收据申请的产品类型为6： rev.productType = 6
		 * 还签证收据的流程类型为4：rev.flowType = 4
		 * 收据金额：revd.myKey='receiptAmount
		 * 
		 * -----------2015-03-13-----------
		 * sql修改：
		 * 1.表 review_detail 字段由 rid 改为 review_id
		 * 2.表review 字段 companyId 改为review_company_id
		 * 
		 * ---------- 2015-04-20----------
		 * 1.添加对打印时间和打印状态的查询：rev.printFlag as isPrintFlag, rev.printTime as printTime
		 * 2.审核列表添加带引状态列
		 * 3.打印后，mouse hover 后显示首次打印时间
		 * 
		 * 
		 * 		//vfbo.batch_no AS batchno,vfbo.batch_person_count AS batchpersoncount,vfbo.batch_total_money AS batchtotalmoney,vfbo.create_user_id AS batchcreateuserid,
			//vfbo.create_user_name AS batchcreateusername,vfbo.print_status batchprintstatus,vfbo.print_time AS batchprinttime,vfbo.review_status AS batchreviewstatus,
		//  and revd.myKey='visaBorrowMoneyBatchNo' AND revd.myValue = vfbo.batch_no AND vfbo.busyness_type = 2 
		//   GROUP BY vfbo.batch_no 
		 */
		String querySql = "SELECT vfbo.batch_no AS batchno,vfbo.batch_person_count AS batchpersoncount,vfbo.batch_total_money AS batchtotalmoney,vfbo.create_user_id AS batchcreateuserid,"
		        +"vfbo.create_user_name AS batchcreateusername,vfbo.print_status batchprintstatus,vfbo.print_time AS batchprinttime,vfbo.review_status AS batchreviewstatus,"
				+ "rev.id as revid, rev.productType as prdtype, rev.`status` as revstatus,rev.`status` as revstatusforprint, rev.updateBy as lastoperator,"
				+ " rev.nowLevel as curlevel, rev.flowType as fltype, rev.createDate as createtime, rev.updateDate as updatetime, rev.printFlag as isPrintFlag, rev.printTime as printTime,"
				+ " vorder.order_no as orderno, vorder.id as orderid, vorder.group_code as groupcode, vorder.create_date as orderdate, vorder.agentinfo_id AS agentid,"
				+ "vorder.create_by AS salecreateby, vp.createBy AS jidcreateby, t.name as tname,t.id as tid, t.payPriceSerialNum as payprice, revd.myValue "
				+ "FROM review rev, visa_order vorder, visa_products vp, traveler t, review_detail revd, visa_flow_batch_opration vfbo  "
				+ "WHERE rev.orderId = vorder.id AND vorder.visa_product_id = vp.id AND rev.travelerId = t.id  "
				+ "AND rev.productType = 6 AND rev.flowType = 4 and revd.review_id= rev.id  and revd.myKey='visaReturnReceiptBatchNo' AND revd.myValue = vfbo.batch_no AND vfbo.busyness_type = 1 "
		        + "AND rev.review_company_id = " + reviewCompanyId + " AND rev.companyId = "+UserUtils.getUser().getCompany().getId() +" ";//适应多批发商
		
		String subIdsStr = "";
		int nFlag = 0;
		for(Long subId : subIds){
			if(nFlag == 0) {
				subIdsStr += subId;
				nFlag++;
			} else {
				subIdsStr += "," + subId;
			}
		}
		if (subIdsStr != null && !"".equals(subIdsStr.trim())) {
			querySql += " and rev.deptId in(" + subIdsStr + ") ";
		}
		
		if (groupCode != null && !"".equals(groupCode.trim())) {
			querySql += " and vorder.group_code like '%" + groupCode + "%' ";
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			startTime += " 00:00:00";
			querySql += " and rev.createDate > '" + startTime + "'";
		}
		if (endtime != null && !"".equals(endtime)) {
			endtime += " 23:59:59";
			querySql += " and rev.createDate <= '" + endtime + "'";
		}
		if (agent != null && !"".equals(agent.trim())) {
			querySql += " and vorder.agentinfo_id = " + Long.parseLong(agent);
		}
		
		//------wxw added 2015-07-29打印状态------
		if (printstatus != null && !"".equals(printstatus.trim())) {
			if ("0".equals(printstatus)) {
				querySql += " and (rev.printFlag = " + Integer.parseInt(printstatus)+" or rev.printFlag is null)";
			}else {
				querySql += " and rev.printFlag = " + Integer.parseInt(printstatus);
			}
		}
		
				
		if (saler != null && !"".equals(saler.trim())) {
			querySql += " and vorder.create_by = " + Long.parseLong(saler);
		}
		if (jdsaler != null && !"".equals(jdsaler.trim())) {
			querySql += " and vp.createBy = " + Long.parseLong(jdsaler);
		}
		
		//-------added for adapt deptId  2015-2-6--------
		//querySql += " and rev.deptId = '" + dept + "' ";
		
		/**
		 * 获取职位部门，根据部门id获取 topLevel，否可可能出现审核通过查不到记录的问题
		 */
		Integer deptLevel = userJob.getDeptLevel();
		Long deptId;
		if(deptLevel == 1){//一级部门
			deptId = userJob.getDeptId();
		} else {
			deptId = userJob.getParentDept();
		}
		
		/*根据不同的状态 添加不同的SQL过滤条件*/
		if (status != null && !"".equals(status)) {// 0 已驳回 1 待审核 2 审核成功 3 操作完成
			//审核状态过滤条件不为空
			Integer statusInt = Integer.parseInt(status);
			//1 如果为0 已驳回 1 待审核 取对应层级和对应状态
/*			if(statusInt == 0 || statusInt == 1){
				querySql += "and rev.`status` ='" + status + "' ";//审核状态
				querySql += "and rev.nowLevel = " + levels.get(0);//审核层级
			}
			//如果为2 审核成功
			if (statusInt == 2){
				Long companyId = UserUtils.getUser().getCompany().getId();
				//List<Long> listCompanyID = reviewCompanyDao.findReviewCompanyList(companyId, Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT);
				List<ReviewCompany> listCompany = reviewCompanyDao.findReviewCompanyDept(companyId, Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT, deptId);
				//ReviewCompany  listCompany = reviewCompanyDao.findOne(listCompanyID.get(0));
				if(null==listCompany||listCompany.size()<1){
					return null;
				}
				//Integer topLevel = listCompany.getTopLevel();
				Integer topLevel = listCompany.get(0).getTopLevel();
				//如果审核层级为最高层级 则取等于当前level 且和对应状态  否则 取大于当前level的记录
				if(topLevel == levels.get(0)){
					querySql += "and rev.`status` ='" + status + "' ";//审核状态
				
					querySql += "and (rev.`status` ='" + status + "' or (rev.`status` ='3'))";
					
					querySql += " and rev.nowLevel = " + levels.get(0);//审核层级
				} else {
					querySql += " and rev.nowLevel > " + levels.get(0);//审核层级
				}
			}*/
			
			
			if(statusInt == 1){//待审核
				querySql += " and rev.`status` ='" + status + "' ";//审核状态
				querySql += " and rev.nowLevel = " + levels.get(0);//审核层级
			}else if (statusInt == 5) {//审核中
				querySql += " and rev.`status` =1";//审核状态
			}else if (statusInt == 0) {//未通过
				querySql += " and rev.`status` =0";//审核状态
			}else if (statusInt == 2) {//已通过
				querySql += " and (rev.`status` ='2' or rev.`status` ='3')";//审核状态
			}else if (statusInt == 4) {//已取消
				querySql += " and rev.`status` =4";//审核状态
			}
	
			
			
		}else if("".equals(status)) {
			//审核过滤条件为空   处理有取消状态的情况
			//querySql += " and (rev.nowLevel > " + levels.get(0);
			//querySql += " or (rev.nowLevel = " + levels.get(0);
			//querySql += " and rev.active =1)) ";
			
		} else {
			//审核过滤条件为空
			//querySql += " and (rev.nowLevel > " + levels.get(0);
			//querySql += " or (rev.nowLevel = " + levels.get(0);
			//querySql += " and rev.active =1)) ";
			//querySql += " )) ";
			
			querySql += " and rev.`status` ='1' ";//审核状态
			querySql += " and rev.nowLevel = " + levels.get(0);//审核层级
		}

		String cOrderBy  = request.getParameter("orderCreateDateSort");// 创建日期排序标识
		String uOrderBy = request.getParameter("orderUpdateDateSort");// 更新日期排序标识
		
		// 排序条件
		querySql += " GROUP BY vfbo.batch_no ORDER BY 1=1";
		if(cOrderBy!=null && !"".equals(cOrderBy)){
			querySql+=", createtime "+cOrderBy;
		}
		if(uOrderBy!=null && !"".equals(uOrderBy)){
			querySql+=", updatetime "+uOrderBy;
		}
		
		//System.out.println("querySql==="+querySql);
		return findBySql(new Page<Map<String, Object>>(request, response),
				querySql, Map.class);
	}
	
	
	/*********************   批次借款审核相关     结束      *********************/
	/*********************   批次借款审核相关     结束       *********************/
	
	
}
