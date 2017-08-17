package com.trekiz.admin.modules.review.visareturndepositreceipt.repository;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Repository
public class VisaReturnDepositReceiptDaoImpl extends BaseDaoImpl<Map<String, Object>>
		implements IVisaReturnDepositReceiptDao {
	
	/**
	 * 签证还押金收据申请列表页查询
	 */
	@Override
	public Page<Map<String, Object>> queryVisaReturnDepositReceiptReviewList(
			HttpServletRequest request, HttpServletResponse response,
			String groupCode, String startTime, String endtime, String agent,
			String travlerName,String orderNum,String depositeAmount,
			String orderCreateBy,String saler, String jdsaler, String status, 
			String visaType, List<Integer> levels, UserJob userJob, Long reviewCompanyId,List<Long> subIds) {
		/**
		 * SQL说明：
		 * 订单创建人为 销售：vorder.create_by AS salecreateby
		 * 产品创建人为 签务：vp.createBy AS jidcreateby
		 * 还签证押金收据申请的产品类型为6： rev.productType = 6
		 * 还签证押金收据申请流程类型为13：rev.flowType = 13
		 * 还签证押金收据申请金额：revd.myKey='depositReceiptAmount'
		 * 
		 * -------2015-03-12----------------------------
		 * sql修改说明：
		 * 1.表 review_detail 字段由  revd.rid  改为  revd.review_id
		 * 2.表 review  字段由 rev.companyId 改为  rev.review_company_id
		 * 
		 */
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT rev.id as revid, rev.productType as prdtype, rev.`status` as revstatus, rev.updateBy as lastoperator, ")
		   .append(" rev.nowLevel as curlevel, rev.flowType as fltype, rev.createDate as createtime, rev.updateDate as updatetime, ")
		   .append(" vorder.order_no as orderno, vorder.id as orderid, vorder.group_code as groupcode, vorder.total_money as totalmoney, ")
		   .append(" vorder.payed_money as payedmoney, vorder.accounted_money as accountedmoney, vorder.create_date as orderdate, ")
		   .append(" vorder.agentinfo_id AS agentid, vorder.create_by AS orderCreateby, vorder.salerId AS salecreateby, ")
		   .append(" vp.createBy AS jidcreateby, t.name as tname, t.id as tid, t.payPriceSerialNum as payprice, revd.myValue ")
		   .append("FROM review rev, visa_order vorder, visa_products vp, traveler t, review_detail revd ")
		   .append("WHERE rev.orderId = vorder.id AND vorder.visa_product_id = vp.id AND rev.travelerId = t.id ")
		   .append("AND rev.productType = 6 AND rev.flowType = 13 AND revd.review_id= rev.id AND revd.myKey ='depositReceiptAmount' ")
		   .append("AND rev.review_company_id = ").append(reviewCompanyId).append(" AND rev.companyId = ").append(companyId).append(" ");
		
		// rev.deptId 拼接
		String subIdsStr = StringUtils.join(subIds, ",");
		
		if (subIdsStr != null && !"".equals(subIdsStr.trim())) {
			sql.append(" AND rev.deptId in(" + subIdsStr + ") ");
		}
		if (groupCode != null && !"".equals(groupCode.trim())) {
			sql.append(" AND vorder.group_code like '%" + groupCode + "%' ");
		}
		if (startTime != null && !"".equals(startTime.trim())) {
			sql.append(" AND rev.createDate > '" + startTime + " 00:00:00' ");
		}
		if (endtime != null && !"".equals(endtime)) {
			sql.append(" AND rev.createDate < '" + endtime + " 23:59:59' ");
		}
		if (agent != null && !"".equals(agent.trim())) {
			sql.append(" AND vorder.agentinfo_id = ").append(Long.parseLong(agent));
		}
		//------wxw added 2015-07-29 游客姓名------
		if (travlerName != null && !"".equals(travlerName.trim())) {
			sql.append(" AND t.name like '%" + travlerName + "%' ");
		}
		//------wxw added 2015-07-29  订单编号------
		if (orderNum != null && !"".equals(orderNum.trim())) {
			sql.append(" AND vorder.order_no = '" + orderNum + "' ");
		}
		//------wxw added 2015-07-29  押金金额------
		if (depositeAmount != null && !"".equals(depositeAmount.trim())) {
			sql.append(" AND revd.myValue = '" + depositeAmount + "' ");
		}
		//------wxw added 2015-08-20   下单人------
		if (orderCreateBy != null && !"".equals(orderCreateBy.trim())) {
			sql.append(" AND vorder.create_by = ").append(Long.parseLong(orderCreateBy));
		}
		if (saler != null && !"".equals(saler.trim())) {
			sql.append(" AND vorder.salerId = ").append(Long.parseLong(saler));
		}
		if (jdsaler != null && !"".equals(jdsaler.trim())) {
			sql.append(" AND vp.createBy = ").append(Long.parseLong(jdsaler));
		}
		
		/*根据不同的状态 添加不同的SQL过滤条件*/
		if (status != null && !"".equals(status)) {// 0 已驳回 1 待审核 2 审核成功 3 操作完成
			//审核状态过滤条件不为空
			Integer statusInt = Integer.parseInt(status);
			//1 如果为0 已驳回 1 待审核 取对应层级和对应状态
			if(statusInt == 1){//待审核
				sql.append(" AND rev.`status` ='" + status + "' ");//审核状态
				sql.append(" AND rev.nowLevel = ").append(levels.get(0));//审核层级
			}else if (statusInt == 5) {//审核中
				sql.append(" AND rev.`status` = 1 ");//审核状态
			}else if (statusInt == 0) {//未通过
				sql.append(" AND rev.`status` = 0 ");//审核状态
			}else if (statusInt == 2) {//已通过
				sql.append(" AND (rev.`status` = '2' or rev.`status` = '3') ");//审核状态
			}else if (statusInt == 4) {//已取消
				sql.append(" AND rev.`status` = 4 ");//审核状态
			}
		} else if ("".equals(status)) {
			//审核过滤条件为空   处理有取消状态的情况
		} else {
			sql.append(" AND rev.`status` = '1' ");//审核状态
			sql.append(" AND rev.nowLevel = ").append(levels.get(0));//审核层级
		}
		
		if (visaType != null && !"".equals(visaType.trim())) {
			sql.append(" AND vp.visaType = ").append(Long.parseLong(visaType));
		}

		if(StringUtils.isNotBlank(request.getParameter("paymentType"))){
			sql.append(" AND vorder.agentinfo_id IN(SELECT id FROM agentinfo WHERE paymenttype = ")
			   .append(request.getParameter("paymentType")).append(") ");
		}
		String cOrderBy  = request.getParameter("orderCreateDateSort");// 创建日期排序标识
		String uOrderBy = request.getParameter("orderUpdateDateSort");// 更新日期排序标识
		
		// 排序条件
		sql.append(" ORDER BY 1=1 ");
		if(cOrderBy!=null && !"".equals(cOrderBy)){
			sql.append(", createtime ").append(cOrderBy);
		}
		if(uOrderBy!=null && !"".equals(uOrderBy)){
			sql.append(", updatetime ").append(uOrderBy);
		}

		return findBySql(new Page<Map<String, Object>>(request, response),
				sql.toString(), Map.class);
	}
	
	
}
