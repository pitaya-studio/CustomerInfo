package com.trekiz.admin.modules.review.depositereview.repository;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Repository
public class DepositeRefundReviewDaoImpl extends
		BaseDaoImpl<Map<String, Object>> implements IDepositeRefundReviewDao {
	
	@Override
	public Page<Map<String, Object>> findCostRefundReviewList(
			Map<String, String> params, Page<Map<String, Object>> page, 
			List<Integer> level, UserJob userJob,
			Long reviewCompanyId, List<Long> subIds) {
		String refundReviewSql = "SELECT orderid,groupcode,ordercreatedate," +
									"orderPersonId,salecreateby,salerId,salerName," +
									"jidcreateby,ordercode,agentid,traid,travelerName," +
									"depmoney,returnstatus,revid,lastoperator,revstatus," +
									"toplevel,flowtype,createDate,updateDate,chanpid," +
									"chanpname,curlevel,total_money,payed_money,accounted_money" +
									" FROM (SELECT "
									+ "a.id as orderid, "/*订单id*/
									+ "b.groupCode as groupcode, "/*团号*/
									+ "a.create_date as ordercreatedate, "/*下单时间*/

									+ "a.create_by as orderPersonId, "/*下单人*/
									+ "a.create_by as salecreateby, a.salerId,a.salerName,"/*销售*/

									+ "b.createBy as jidcreateby, "/*计调*/
									+ "a.order_no as ordercode, "/*订单编号*/
									+ "a.agentinfo_id as agentid, "/*渠道名称*/
									+ "rev.travelerId as traid, "/*游客ID*/
									+ "(select name from traveler tt where tt.id = rev.travelerId) as travelerName," 
									+ "v.accounted_deposit depmoney, "/*押金金额*/
									+ "v.returned_deposit_status as returnstatus, "/*出纳确认状态*/
									+ "rev.id as revid, "/*审批表id*/
									+ "rev.updateBy as lastoperator, "/*上一环节操作人*/
									+ "rev.`status` as revstatus, "/*审核状态*/
									+ "rev.topLevel as toplevel, "/*最高层级层级*/
									+ "rev.flowtype as flowtype, "/*流程类型*/
									+ "rev.createDate, "/*创建时间*//*报批日期*/
									+ "rev.updateDate, "/*更新时间*/
									+ "b.id as chanpid,"
									+ "b.productName as chanpname,"
									+ "rev.nowLevel as curlevel, "/*当前层级*/
									+ "a.total_money, "/*订单总金额*/
									+ "a.payed_money,"/*已付金额*/
									+ "a.accounted_money "/*达帐金额*/
								+ "FROM "
									+ "visa_order a, "
									+ "visa_products b, "
									+ "visa v, "
									+ "review rev "
								+ "where rev.orderId = a.id "
								+ " and rev.review_company_id = " + reviewCompanyId + " "
								+ "and rev.flowType = 7 "
								+ "and rev.productType = " + Context.ORDER_TYPE_QZ + " "//签证
								+ "and a.visa_product_id = b.id "
								+ "and rev.companyId = " + UserUtils.getUser().getCompany().getId() + " "
								+ "and rev.travelerId = v.traveler_id ";
		String subIdsStr = "";
		int nFlag = 0;
		for(Long subId : subIds){
			if(nFlag == 0){
				subIdsStr += subId;
				nFlag++;
			} else {
				subIdsStr += "," + subId;
			}
		}
		if (!"".equals(subIdsStr)){
			refundReviewSql += " and rev.deptId in(" + subIdsStr + ") ";//
		}
		/*根据不同的状态 添加不同的SQL过滤条件*/
		if (StringUtils.isNotBlank(params.get("statusChoose"))) {
			//审核状态过滤条件不为空
			//0:全部 1:待本人审核 2:本人审核通过 3:审核中 4:已通过 5:未通过 6:已取消
			// status 0:已驳回 1:待审核 2:审核成功 3:操作完成 4:取消申请
			Integer statusInt = Integer.parseInt(params.get("statusChoose"));
			if(statusInt == 1){//待本人审核
				refundReviewSql += " and rev.`status` = 1 ";
				refundReviewSql += " and rev.nowLevel = " + level.get(0);//当前审核层级
			}else if(statusInt == 2){//本人审核通过 
				refundReviewSql += " and rev.updateBy = " + UserUtils.getUser().getId();
			}else if(statusInt == 3){//审核中
				refundReviewSql += " and rev.`status` = 1 ";
			}else if(statusInt == 4){//已通过
				refundReviewSql += " and rev.`status` in (2,3) " ;
			}else if (statusInt == 5){//未通过 
				refundReviewSql += " and rev.`status` = 0 ";
			}else if(statusInt == 6){//已取消
				refundReviewSql += " and rev.`status` = 4 ";
			}
		}
		// 团号 对应页面的团号查询条件
		if (StringUtils.isNotBlank(params.get("groupCode"))) {
			refundReviewSql += " and b.groupCode like'%" + params.get("groupCode").trim() + "%' ";
		}
		//订单编号
		String orderNum = params.get("orderNum");
		if(StringUtils.isNotBlank(orderNum)){
			refundReviewSql += " and a.order_no like'%" + orderNum.trim() + "%' ";
		}
		// 报批时间
		if (StringUtils.isNotBlank(params.get("startTime"))) {
			refundReviewSql += " and rev.createDate >= '" + params.get("startTime") + " 00:00:00' ";
		}
		if (StringUtils.isNotBlank(params.get("endTime"))) {
			refundReviewSql += " and rev.createDate <= '" + params.get("endTime") + " 23:59:59' ";
		}
		// 押金金额
		if (StringUtils.isNotBlank(params.get("moneyBegin"))) {
			refundReviewSql += " and v.accounted_deposit >= " + params.get("moneyBegin");
		}
		if (StringUtils.isNotBlank(params.get("moneyEnd"))) {
			refundReviewSql += " and v.accounted_deposit <= " + params.get("moneyEnd");
		}
		// 渠道 对应页面的渠道查询条件
		if (StringUtils.isNotBlank(params.get("channel"))) {
			refundReviewSql += " and a.agentinfo_id = " + Integer.parseInt(params.get("channel"));
		}
		// 下单人
		if (StringUtils.isNotBlank(params.get("create_by"))) {
			refundReviewSql += " and a.create_by = " + Integer.parseInt(params.get("create_by"));
		}
		
		
		// 销售 对应页面的渠道查询条件
		if (StringUtils.isNotBlank(params.get("salerId"))) {
			refundReviewSql += " and a.salerId = " + Integer.parseInt(params.get("salerId"));
		}
		// 下单人
		if (StringUtils.isNotBlank(params.get("orderPersonId"))) {
			refundReviewSql += " and a.create_by = " + Integer.parseInt(params.get("orderPersonId"));
		}
		// 计调 对应页面的渠道查询条件
		if (StringUtils.isNotBlank(params.get("operatorId"))) {
			refundReviewSql += " and b.createBy = " + Integer.parseInt(params.get("operatorId"));
		}
		refundReviewSql += " ) t ";
		if (StringUtils.isNotBlank(params.get("travelerName"))) {
			refundReviewSql += " WHERE t.travelerName like '%"+params.get("travelerName")+"%'";
		}
		return findBySql(page, refundReviewSql, Map.class);
	}
	/**
	 * 查询退款审批列表信息 查询信息 groupCode : 团号 productType : 团队类型 startTime : 下单时间 起始
	 * endTime : 下单时间 结束 channel : 渠道 saler : 销售 meter : 计调
	 */
	@Override
	public Page<Map<String, Object>> findRefundReviewList(
			HttpServletRequest request, HttpServletResponse response,
			String groupCode, String productType, String startTime,
			String endTime, String channel, String saler, String meter,
			String status, List<Integer> level, String flowType, String cOrderBy, String uOrderBy, UserJob userJob,Long reviewCompanyId, List<Long> subIds) {

		String orderPersonId = request.getParameter("orderPersonId");// 下单人
		String refundReviewSql = "SELECT "
									+ "a.id as orderid, "/*订单id*/
									+ "a.group_code as groupcode, "/*团号*/
									+ "a.create_date as ordercreatedate, "/*下单时间*/
									+ "a.salerId as salerId, "/*销售*/
									+ "a.create_by as orderPersonId, "/*下单人*/
									+ "b.createBy as jidcreateby, "/*签证员*/
									+ "a.order_no as ordercode, "/*订单编号*/
									+ "a.agentinfo_id as agentid, "/*渠道名称*/
									+ "rev.travelerId as traid, "/*游客*/
									+ "v.accounted_deposit depmoney, "/*押金金额*/
									+ "v.returned_deposit_status as returnstatus, "/*出纳确认状态*/
									+ "rev.id as revid, "/*审批表id*/
									+ "rev.updateBy as lastoperator, "/*上一环节操作人*/
									+ "rev.`status` as revstatus, "/*审核状态*/
									+ "rev.topLevel as toplevel, "/*最高层级层级*/
									+ "rev.flowtype as flowtype, "/*流程类型*/
									+ "rev.createDate as createtime, "/*创建时间*//*报批日期*/
									+ "rev.updateDate as updatetime, "/*更新时间*/
									+ "rev.topLevel as rtoplevel, "/*当前层级*/
									+ "b.id as chanpid,"
									+ "b.productName as chanpname,"
									+ "rev.nowLevel as curlevel "/*当前层级*/
								+ "FROM "
									+ "visa_order a, "
									+ "visa_products b, "
									+ "visa v, "
									+ "review rev "
								+ "where rev.orderId = a.id "
								+ " and rev.review_company_id = " + reviewCompanyId + " "
								+ "and rev.flowType = 7 "
								+ "and rev.productType = " + Context.ORDER_TYPE_QZ + " "//签证
								+ "and a.visa_product_id = b.id "
								+ "and rev.companyId = " + UserUtils.getUser().getCompany().getId() + " "
								+ "and rev.travelerId = v.traveler_id ";
		String subIdsStr = "";
		int nFlag = 0;
		for(Long subId : subIds){
			if(nFlag == 0){
				subIdsStr += subId;
				nFlag++;
			} else {
				subIdsStr += "," + subId;
			}
		}
		if (subIdsStr != null && !"".equals(subIdsStr)){
			refundReviewSql += " and rev.deptId in(" + subIdsStr + ") ";//
		}
		/*根据不同的状态 添加不同的SQL过滤条件*/
		if (status != null && !"".equals(status)) {// 0 已驳回 1 待审核 2 审核成功 3 操作完成
			//审核状态过滤条件不为空
			Integer statusInt = Integer.parseInt(status);
			//1 如果为0 已驳回 1 待审核 取对应层级和对应状态
			if(statusInt == 1){
				refundReviewSql += " and rev.`status` ='" + status + "' ";//审核状态
				refundReviewSql += " and rev.nowLevel = " + level.get(0);//审核层级
			}
			if(statusInt == 5){//审核中的
				refundReviewSql += " and rev.`status` ='1' ";//审核状态
			}
			if(statusInt == 0){//查询已驳回的
				refundReviewSql += " and rev.`status` ='" + status + "' ";//审核状态
			}
			if(statusInt == 4){//查询已取消的的
				refundReviewSql += " and rev.`status` ='" + status + "' ";//审核状态
			}
			//如果为2 审核成功
			if (statusInt == 2){
				refundReviewSql += "and (rev.`status` ='2' or rev.`status` ='3')";//审核状态

			}
		}
		// 团号 对应页面的团号查询条件
		if (groupCode != null && !"".equals(groupCode.trim())) {
			refundReviewSql += " and a.group_code like'%" + groupCode + "%' ";
		}
		// 时间范围 页面查询条件 审核的下单时间
		if (startTime != null) {

			refundReviewSql += " and rev.createDate >= '" + startTime + " 00:00:00' ";
		}
		if (endTime != null) {
			refundReviewSql += " and rev.createDate <= '" + endTime + " 23:59:59' ";
		}
		// 渠道 对应页面的渠道查询条件
		if (channel != null && !"".equals(channel.trim())) {
			refundReviewSql += " and a.agentinfo_id = '" + Integer.parseInt(channel)
					+ "' ";
		}
		// 销售 对应页面的渠道查询条件
		if (saler != null && !"".equals(saler.trim())) {
			refundReviewSql += " and a.salerId = '" + Integer.parseInt(saler)
					+ "' ";
		}
		//下单人
		if (StringUtils.isNotBlank(orderPersonId)) {
			refundReviewSql += " and a.create_by = " + Integer.parseInt(orderPersonId);
		}
		// 计调 对应页面的渠道查询条件
		if (meter != null && !"".equals(meter.trim())) {
			refundReviewSql += " and b.createBy = '" + Integer.parseInt(meter)
					+ "' ";
		}
		// 排序条件
		refundReviewSql += " ORDER BY 1=1";
		if(cOrderBy!=null && !"".equals(cOrderBy)){
			refundReviewSql+=", createtime "+cOrderBy;
		}
		if(uOrderBy!=null && !"".equals(uOrderBy)){
			refundReviewSql+=", updatetime "+uOrderBy;
		}
		return findBySql(new Page<Map<String, Object>>(request, response),
				refundReviewSql, Map.class);
	}

	/**
	 * 查询签证订单审核详情
	 */
	@Override
	public Map<String, Object> queryVisaReviewOrderDetail(String prdOrderId) {
		String visaSql = "SELECT a.id as orderid,a.create_by as ordercreate,a.create_date as orderdate,a.product_type_id as prdtype,"+
					"a.order_no as orderno,a.group_code as groupno,a.total_money as totalmoney,a.visa_order_status as orderstatus, "+
					"b.createBy as updateby,b.productCode as prdcode, b.id as visaproductid,b.productName as prdname,b.sysCountryId as countryid, "+
					"b.visaType as visatype,b.collarZoning as collarea,b.currencyId as visaCurrency,b.visaPay as visapay,a.travel_num as tnum "+
					", a.salerId FROM visa_order a,visa_products b "+
					" WHERE a.visa_product_id = b.id and a.id=" + prdOrderId;
		List<Map<String, Object>> visaOrderDetail = findBySql(visaSql,
				Map.class);
		if (visaOrderDetail == null || visaOrderDetail.size() != 1) {
			return null;
		}
		return visaOrderDetail.get(0);
	}

}
