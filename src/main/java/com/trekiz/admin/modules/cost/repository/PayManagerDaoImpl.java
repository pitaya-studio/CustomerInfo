package com.trekiz.admin.modules.cost.repository;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Repository;

import com.quauq.review.core.engine.config.ReviewConstant;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.order.entity.Refund;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 支付接口DAO实现类 实现SQL
 * 
 * @author Administrator
 * 
 */
@Repository
public class PayManagerDaoImpl extends BaseDaoImpl<Map<String, Object>>
		implements IPaymanagerDao {

	/**
	 * 查询审核通过的返佣支付列表
	 */
	@Override
	public Page<Map<String, Object>> getRebatePayList(
			Map<String, Object> params, HttpServletRequest request,
			HttpServletResponse response) {
		// 支付类型是必传的 如果没有 抛异常
		Object payTypeObj = params.get("payType");
		Integer flowtype;
		if (payTypeObj == null || "".equals(payTypeObj.toString())) {
			throw new RuntimeException("支付类型不能是空的");
		}
		if ("201".equals(payTypeObj.toString())) {
			flowtype = 1;// 退款
		} else if ("202".equals(payTypeObj.toString())) {
			flowtype = 9;// 返佣
		} else {
			throw new RuntimeException("不能识别的支付类型");
		}
		Long companyId = UserUtils.getUser().getCompany().getId();
		// 组织查询SQL
		Object orderTypes = params.get("orderTypes");
		StringBuffer querySql = new StringBuffer();
//		if(orderTypes == null || "0".equals(orderTypes)) {
			querySql.append("select groupcode,groupid,orderid,orderno,comments,prdtype,flowtype,createdate,agentid,salecreateby,jidcreateby," +
					"supplycom,chanpid,chanpname,revid,revcom,lastoperator,revactive,createtime,updatetime,curlevel,toplevel,revstatus,printTime," +
					"cpid,deptId,printFlag,travelerid,payStatus,salerId,salerName,costname,currencyId,rebatesDiff " +
					"from (SELECT rv.groupcode,rv.groupid,rv.orderid,rv.orderno,rv.comments,rv.prdtype,rv.flowtype,rv.createdate,rv.agentid," +
					"rv.salecreateby,rv.jidcreateby,rv.supplycom,rv.chanpid,rv.chanpname,rv.revid,rv.revcom,rv.lastoperator,rv.revactive,rv.createtime,rv.updatetime," +
					"rv.curlevel,rv.toplevel,rv.revstatus,rv.printTime,rv.cpid,rv.deptId,rv.printFlag,rv.travelerid,rv.payStatus,rv.salerId,rv.salerName, " +
					"o.costname, o.currencyId, o.rebatesDiff FROM refundreview_view rv, order_rebates o " + 
					"WHERE	rv.revid = o.rid AND rv.revstatus IN (2, 3) AND rv.flowtype = " + flowtype +" AND prdtype<>7 AND rv.revcom = " + companyId +
					" UNION " +
					" SELECT " +
					" rv.groupcode, " +
					" rv.groupid, " +
					" rv.orderid, " +
					" rv.orderno, " +
					" rv.comments, " +
					" rv.prdtype, " +
					" rv.flowtype, " +
					" rv.createdate, " +
					" rv.agentid, " +
					" rv.salecreateby, " +
					" rv.jidcreateby, " +
					" rv.supplycom, " +
					" rv.chanpid, " +
					" rv.chanpname, " +
					" rv.revid, " +
					" rv.revcom, " +
					" rv.lastoperator, " +
					" rv.revactive, " +
					" rv.createtime, " +
					" rv.updatetime, " +
					" rv.curlevel, " +
					" rv.toplevel, " +
					" rv.revstatus, " +
					" rv.printTime, " +
					" rv.cpid, " +
					" rv.deptId, " +
					" rv.printFlag, " +
					" rv.travelerid, " +
					" rv.payStatus, " +
					" rv.salerId, " +
					" rv.salerName, " +
					" o.costname, " +
					" o.currencyId, " +
					" sum(o.rebatesDiff) " +
					" FROM " +
					" refundreview_view rv, " +
					" order_rebates o " +
					" WHERE " +
					" rv.revid = o.rid " +
					" AND rv.revstatus IN (2, 3) " +
					" AND rv.flowtype = " + flowtype +
					" AND rv.revcom = " + companyId + " and prdtype=7 group by rv.revid " +
//					" UNION " + "SELECT rv.*, '', m.currencyId, m.amount rebatesDiff FROM refundreview_view rv, island_money_amount m" +
//					" WHERE rv.revid = m.reviewId AND rv.revstatus IN (2, 3) AND rv.flowtype = " + flowtype + " AND rv.revcom = " + companyId +
//					" UNION " + "SELECT rv.*, '', m.currencyId, m.amount rebatesDiff FROM refundreview_view rv, hotel_money_amount m" +
//					" WHERE rv.revid = m.reviewId AND rv.revstatus IN (2, 3) AND rv.flowtype = " + flowtype + " AND rv.revcom = " + companyId +
					" UNION " + "SELECT rv.groupcode,rv.groupid,rv.orderid,rv.orderno,rv.comments,rv.prdtype,rv.flowtype,rv.createdate,rv.agentid," +
					"rv.salecreateby,rv.jidcreateby,rv.supplycom,rv.chanpid,rv.chanpname,rv.revid,rv.revcom,rv.lastoperator,rv.revactive,rv.createtime,rv.updatetime," +
					"rv.curlevel,rv.toplevel,rv.revstatus,rv.printTime,rv.cpid,rv.deptId,rv.printFlag,rv.travelerid,rv.payStatus,rv.salerId,rv.salerName, " +
					"'', m.currencyId, m.amount rebatesDiff FROM refundreview_view rv, money_amount m" +
					" WHERE rv.revid = m.reviewId AND rv.revstatus IN (2, 3) AND rv.flowtype = " + flowtype + " AND prdtype<>7 AND rv.revcom = " + companyId +") rv where 1=1 ");
//		} else if("6".equals(orderTypes) || "7".equals(orderTypes)) {
//			querySql.append("select rv.*, '', m.currencyId, m.amount rebatesDiff from refundreview_view rv, money_amount m "
//					+ "where rv.revid = m.reviewId and rv.revstatus in (2, 3) "
//					+ "and rv.flowtype = " + flowtype + " and rv.revcom = " + companyId);
//		} else if("12".equals(orderTypes)) {//海岛游
//			querySql.append("select rv.*, '', m.currencyId, m.amount rebatesDiff from refundreview_view rv, island_money_amount m "
//					+ "where rv.revid = m.reviewId and rv.revstatus in (2, 3) "
//					+ "and rv.flowtype = " + flowtype + " and rv.revcom = " + companyId);
//		} else if("11".equals(orderTypes)) {// 酒店
//			querySql.append("select rv.*, '', m.currencyId, m.amount rebatesDiff from refundreview_view rv, hotel_money_amount m "
//					+ "where rv.revid = m.reviewId and rv.revstatus in (2, 3) "
//					+ "and rv.flowtype = " + flowtype + " and rv.revcom = " + companyId);
//		} else {
//			querySql.append("select rv.*, o.costname, o.currencyId, o.rebatesDiff from refundreview_view rv, order_rebates o "
//					+ "where rv.revid = o.rid and rv.revstatus in (2, 3) "
//					+ "and rv.flowtype = " + flowtype + " and rv.revcom = " + companyId);
//		}
		
		// 根据页面参数设置不同的查询条件
		Object groupCode = params.get("groupCode");
		if (groupCode != null && !"".equals(groupCode)) {
			querySql.append(" and rv.groupcode like '%" + groupCode + "%'");
		}
		String name = UserUtils.getUser().getCompany().getName();
		if(!(name.contains("俄风行") || name.contains("九州风行"))) {
			if (orderTypes != null && !"".equals(orderTypes) && !"0".equals(orderTypes)) {
				querySql.append(" and rv.prdtype = '" + orderTypes + "'");
			}
		}else{
			if (orderTypes != null && !"".equals(orderTypes) && !"0".equals(orderTypes)) {
				querySql.append(" and rv.prdtype = '" + orderTypes + "'");
			}else if (orderTypes != null && !"".equals(orderTypes) && "0".equals(orderTypes)) {
				querySql.append(" and (rv.prdtype = 11 or rv.prdtype = 12) ");
			}
		}
		
		Object agents = params.get("agents");
		if (agents != null && !"".equals(agents)) {
			querySql.append(" and rv.agentid = '" + agents + "'");
		}
		Object jds = params.get("jds");
		if (jds != null && !"".equals(jds)) {
			querySql.append(" and rv.jidcreateby = '" + jds + "'");
		}
		Object payStatus = params.get("payStatus");
		if (payStatus != null && !"".equals(payStatus)) {
			querySql.append(" and rv.payStatus = " + payStatus + "");
		}
		Object currency = params.get("currency");
		Object startMoney = params.get("startMoney");
		Object endMoney = params.get("endMoney");
		if(orderTypes == null || "0".equals(orderTypes)) {
			if (currency != null && !"".equals(currency)) {
				querySql.append(" and rv.currencyId = '" + currency + "'");
			}
			
			if (startMoney != null && !"".equals(startMoney)) {
				querySql.append(" and rv.rebatesDiff between " + startMoney + " ");
			}
			
			if (endMoney != null && !"".equals(endMoney)) {
				querySql.append(" and " + endMoney + " ");
			}
		} else if("6".equals(orderTypes) || "7".equals(orderTypes)) {
			if (currency != null && !"".equals(currency)) {
				querySql.append(" and m.currencyId = '" + currency + "'");
			}
			
			if (startMoney != null && !"".equals(startMoney)) {
				querySql.append(" and m.amount between " + startMoney + " ");
			}
			
			if (endMoney != null && !"".equals(endMoney)) {
				querySql.append(" and " + endMoney + " ");
			}
		} else {
			if (currency != null && !"".equals(currency)) {
				querySql.append(" and o.currencyId = '" + currency + "'");
			}
			
			if (startMoney != null && !"".equals(startMoney)) {
				querySql.append(" and o.rebatesDiff between " + startMoney + " ");
			}
			
			if (endMoney != null && !"".equals(endMoney)) {
				querySql.append(" and " + endMoney + " ");
			}
		}
		
		Object creators = params.get("creators");
		if (creators != null && !"".equals(creators)) {
			querySql.append(" and rv.salecreateby = '" + creators + "'");
		}
		
		Object salers = params.get("salers");
		if (salers != null && !"".equals(salers)) {
			querySql.append(" and rv.salerId = '" + salers + "'");
		}
		Object payedMoney = params.get("payedMoney");
		if(payedMoney != null && payedMoney.toString().equals("1")) {
			querySql.append(" and rv.revid not in (SELECT DISTINCT record_id FROM refund where moneyType = 3)");
		}
		Object printFlag = params.get("printFlag");
		if (printFlag != null && !"".equals(printFlag)) {
			if ("0".equals(printFlag)) {
				querySql.append(" and (rv.printFlag = " + printFlag + " or rv.printFlag is null)");
			} else {
				querySql.append(" and rv.printFlag = " + printFlag);
			}
		}
		Page<Map<String, Object>> pageParam = new Page<Map<String, Object>>(request, response);
		if(StringUtils.isBlank(pageParam.getOrderBy())){
			pageParam.setOrderBy("updateTime DESC");
		}
		// 根据SQL 查询出对应的page信息
		Page<Map<String, Object>> page = findBySql(pageParam,
				querySql.toString(), Map.class);
		return page;
	}
	
	/**
	 * 查询审核通过的支付列表
	 * @author chy
	 */
	@Override
	public Page<Map<String, Object>> getReviewPayList(
			Map<String, Object> params) {
		// 支付类型是必传的 如果没有 抛异常
		Object payTypeObj = params.get("payType");
		if (payTypeObj == null || "".equals(payTypeObj.toString())) {
			throw new RuntimeException("支付类型不能是空的");
		}
		// 组织查询SQL
		StringBuffer querySql = new StringBuffer("select " +
				"s.groupcode," +
				"s.orderid," +
				"s.orderno," +
				"s.comments," +
				"s.curlevel," +
				"s.groupid," +
				"s.prdtype," +
				"s.flowtype," +
				"s.createdate," +
				"s.agentid," +
				"s.salecreateby," +
				"s.jidcreateby," +
				"s.supplycom," +
				"s.chanpid," +
				"s.chanpname," +
				"s.revid," +
				"s.revcom," +
				"s.lastoperator," +
				"s.createtime," +
				"s.updatetime," +
				"s.printTime," +
				"s.cpid," +
				"s.printFlag," +
				"s.travelerid," +
				"s.payStatus," +
				"s.salerId," +
				"s.salerName," +
				"s.refundname," +
				"s.mcurid," +
				"s.mamount from (");
		//退款SQL
		StringBuffer querySql1 = new StringBuffer();
		querySql1.append("select a.groupid,a.groupcode,a.orderid,a.orderno,a.comments,a.curlevel,a.prdtype,a.flowtype,a.createdate,a.agentid,a.salecreateby,a.jidcreateby,a.supplycom,a.chanpid,a.chanpname,a.revid,a.revcom,a.lastoperator,a.createtime,a.updatetime,a.printTime,a.cpid,a.printFlag,a.travelerid,a.payStatus,a.salerId,a.salerName," + "b.myValue refundname, "
				+ "c.currencyId mcurid,c.amount mamount from "
				+ "refundreview_view a,review_detail b , money_amount c "
				+ "where " + "a.revid = b.review_id "
				+ "and a.revid = c.reviewId and b.myKey = 'refundName' "
				+ "and a.revstatus in (2,3) " + "and a.flowtype = 1 " 
				+ " and a.revcom = " + UserUtils.getUser().getCompany().getId() + " ");
		querySql1.append("union select a.groupid,a.groupcode,a.orderid,a.orderno,a.comments,a.curlevel,a.prdtype,a.flowtype,a.createdate,a.agentid,a.salecreateby,a.jidcreateby,a.supplycom,a.chanpid,a.chanpname,a.revid,a.revcom,a.lastoperator,a.createtime,a.updatetime,a.printTime,a.cpid,a.printFlag,a.travelerid,a.payStatus,a.salerId,a.salerName," + "b.myValue refundname, "
				+ "c.currencyId mcurid,c.amount mamount from "
				+ "refundreview_view a,review_detail b , hotel_money_amount c "
				+ "where " + "a.revid = b.review_id "
				+ "and a.revid = c.reviewId and b.myKey = 'refundName' "
				+ "and a.revstatus in (2,3) " + "and a.flowtype = 1 " 
				+ " and a.revcom = " + UserUtils.getUser().getCompany().getId() + " ");
		querySql1.append("union select a.groupid,a.groupcode,a.orderid,a.orderno,a.comments,a.curlevel,a.prdtype,a.flowtype,a.createdate,a.agentid,a.salecreateby,a.jidcreateby,a.supplycom,a.chanpid,a.chanpname,a.revid,a.revcom,a.lastoperator,a.createtime,a.updatetime,a.printTime,a.cpid,a.printFlag,a.travelerid,a.payStatus,a.salerId,a.salerName," + "b.myValue refundname, "
				+ "c.currencyId mcurid,c.amount mamount from "
				+ "refundreview_view a,review_detail b , island_money_amount c "
				+ "where " + "a.revid = b.review_id "
				+ "and a.revid = c.reviewId and b.myKey = 'refundName' "
				+ "and a.revstatus in (2,3) " + "and a.flowtype = 1 " 
				+ " and a.revcom = " + UserUtils.getUser().getCompany().getId() + " ");
		//退签证押金 SQL
		StringBuffer querySql2 = new StringBuffer();
		querySql2.append("select a.groupid,a.groupcode,a.orderid,a.orderno,a.comments,a.curlevel,a.prdtype,a.flowtype,a.createdate,a.agentid,a.salecreateby,a.jidcreateby,a.supplycom,a.chanpid,a.chanpname,a.revid,a.revcom,a.lastoperator,a.createtime,a.updatetime,a.printTime,a.cpid,a.printFlag,a.travelerid,a.payStatus,a.salerId,a.salerName," + "b.myValue refundname, "
				+ "c.currencyId mcurid,c.amount mamount from "
				+ "refundreview_view a,review_detail b , money_amount c "
				+ "where " + "a.revid = b.review_id "
				+ "and a.revid = c.reviewId and b.myKey = 'remark' "
				+ "and a.revstatus in (2,3) " + "and a.flowtype = 7 " 
				+ " and a.revcom = " + UserUtils.getUser().getCompany().getId() + " ");
		//根据查询类型 组装SQL
		String payType = params.get("payType2") == null ? "" : params.get("payType2").toString().trim();
		if("".equals(payType) || "0".equals(payType)){// 查询全部
			querySql.append(querySql1).append(" union ").append(querySql2);
		} else if("1".equals(payType)){//退款
			querySql.append(querySql1);
		} else {//退签证押金
			querySql.append(querySql2);
		}
		querySql.append(" ) s where 1=1 ");
		// 根据页面参数 设置不同的查询条件
		if (params.get("groupCode") != null
				&& !"".equals(params.get("groupCode").toString().trim())) {// 团号选择
			querySql.append(" and groupcode like '%"
					+ params.get("groupCode").toString() + "%' ");
		}
		if (params.get("prdType") != null
				&& !"".equals(params.get("prdType").toString().trim())&&!"0".equals(params.get("prdType").toString().trim())) {// 团队类型选择 0代表全部
			querySql.append(" and prdtype = "
					+ Integer.parseInt(params.get("prdType").toString().trim()));
		} else if(UserUtils.getUser().getCompany().getName().contains("俄风行")){//俄风行 全部只有海岛游和酒店  modify by chy 2015年8月31日21:11:39
			querySql.append(" and prdtype in (11,12) ");
		}
		if (params.get("currencyid") != null
				&& !"".equals(params.get("currencyid").toString().trim())) {// 币种
			querySql.append(" and mcurid = "
					+ Integer.parseInt(params.get("currencyid").toString().trim()));
		}
		if (params.get("cAmountStart") != null
				&& !"".equals(params.get("cAmountStart").toString().trim())) {// 钱范围
			querySql.append(" and mamount >= "
					+ Double.parseDouble(params.get("cAmountStart")
							.toString()));
		}
		if (params.get("cAmountEnd") != null
				&& !"".equals(params.get("cAmountEnd").toString().trim())) {// 钱范围
			querySql.append(" and mamount <= "
					+ Double.parseDouble(params.get("cAmountEnd").toString()));
		}
		if (params.get("companyId") != null
				&& !"".equals(params.get("companyId").toString().trim())) {// 供应商选择
			querySql.append(" and revcom = "
					+ Integer.parseInt(params.get("companyId").toString().trim()));
		}
		if (params.get("agentId") != null
				&& !"-99999".equals(params.get("agentId").toString().trim())) {// 渠道商选择
			querySql.append(" and agentid = "
					+ Integer.parseInt(params.get("agentId").toString().trim()));
		}
		if (params.get("creator") != null
				&& !"-99999".equals(params.get("creator").toString().trim())) {// 下单人选择
			querySql.append(" and salecreateby = "
					+ Integer.parseInt(params.get("creator").toString().trim()));
		}
		if (params.get("saler") != null
				&& !"-99999".equals(params.get("saler").toString().trim())) {// 销售选择
			querySql.append(" and salerId = "
					+ Integer.parseInt(params.get("saler").toString().trim()));
		}
		if (params.get("jdsaler") != null
				&& !"-99999".equals(params.get("jdsaler").toString().trim())) {// 计调选择
			querySql.append(" and jidcreateby = "
					+ Integer.parseInt(params.get("jdsaler").toString().trim()));
		}
		if (params.get("payStatus") != null
				&& !"".equals(params.get("payStatus").toString().trim())) {// 支付状态选择
			if("2".equals(params.get("payStatus").toString().trim())){
				querySql.append(" and (payStatus is null or payStatus = '' or  payStatus= "
						+ 0 + ") ");
			} else {
				querySql.append(" and payStatus = "
						+ Integer.parseInt(params.get("payStatus").toString().trim()));
			}
		}
		if (params.get("printStatus") != null
				&& !"".equals(params.get("printStatus").toString().trim())) {// 支付状态选择
			if("0".equals(params.get("printStatus"))){
				querySql.append(" and (printFlag = "
						+ Integer.parseInt(params.get("printStatus").toString().trim()) + " or printFlag is null)");
			} else {
				querySql.append(" and printFlag = "
						+ Integer.parseInt(params.get("printStatus").toString().trim()));
			}
		}
		if (params.get("payedStatus") != null && !"".equals(params.get("payedStatus").toString().trim())) {// 已付金额
			if("2".equals(params.get("payedStatus").toString().trim())){
				if("".equals(payType) || "0".equals(payType)){// 查询全部
					querySql.append(" and revid not in (select record_id from refund where (moneyType = '2' or moneyType = '5')) ");
				} else if("1".equals(payType)){//退款
					querySql.append(" and revid not in (select record_id from refund where moneyType = '2') ");
				} else {//退签证押金
					querySql.append(" and revid not in (select record_id from refund where moneyType = '5') ");
				}
			} 
		}

		@SuppressWarnings("unchecked")
		Page<Map<String, Object>> pageP = (Page<Map<String, Object>>) params.get("pageP");
		if(pageP != null && StringUtils.isBlank(pageP.getOrderBy())){
			pageP.setOrderBy("updatetime DESC");
		}
		// 根据SQL 查询出对应的page信息
		Page<Map<String, Object>> page = findBySql(pageP, querySql.toString(), Map.class);
		return page;
	}

	@Override
	public Page<Map<String, Object>> findBorrowMoneyListTTSQZ(
			Map<String, String> params, Long companyId, Page<Map<String, Object>> page) {
		StringBuffer str = new StringBuffer();
		str.append("SELECT batch.id as batch_id,batch.batch_no,batch.create_user_name as proposer,")
		   .append(" r.createDate , batch.batch_total_money,batch.batch_person_count,")
		   .append(" case when batch.batch_person_count = 0 then '0.00'")
		   .append(" else batch.batch_total_money/batch.batch_person_count")
		   .append(" end as price,")
		   .append(" r.updateDate ,p.id AS productId,o.id AS orderId,")
		   .append(" r.payStatus, o.agentinfo_id as agentId,")
		   .append(" r.id as reviewId, r.printFlag as isPrintFlag, r.printTime ")
		   .append(" FROM review r,review_detail d,visa_order o,")
		   .append(" visa_products p,visa_flow_batch_opration batch ")
		   .append(" WHERE r.id = d.review_id AND d.myKey = 'visaBorrowMoneyBatchNo'")
		   .append(" AND r.orderId = o.id AND p.id = o.visa_product_id")
		   .append(" AND batch.batch_no = d.myValue")
		   .append(" and p.proCompanyId = ").append(companyId)
		   .append(" and r.companyId = ").append(companyId)
		   .append(" and batch.busyness_type = 2 ")
		   .append(" and r.flowType = 5 and r.status in (2, 3)");
		if (StringUtils.isNotBlank(params.get("groupCode"))) {
			str.append(" and p.groupCode like '%").append(params.get("groupCode"))
			   .append("%'");
		}
		if (StringUtils.isNotBlank(params.get("visaType"))) {
			str.append(" and p.visaType = ").append(params.get("visaType"));
		}
		if (StringUtils.isNotBlank(params.get("payStatus"))) {
			str.append(" and r.payStatus = ").append(params.get("payStatus"));
		}
		if (StringUtils.isNotBlank(params.get("reportDateStart"))) {
			str.append(" and r.createDate >= '")
			   .append(params.get("reportDateStart"))
			   .append(" 00:00:01'");
		}
		if (StringUtils.isNotBlank(params.get("reportDateEnd"))) {
			str.append(" and r.createDate <= '")
			   .append(params.get("reportDateEnd"))
			   .append(" 23:59:59'");
		}
		if (StringUtils.isNotBlank(params.get("visaContry"))) {
			str.append(" and p.sysCountryId = ").append(params.get("visaContry"));
		}
		if (StringUtils.isNotBlank(params.get("salerId"))) {
			str.append(" and o.create_by = ").append(params.get("salerId"));
		}
		if (StringUtils.isNotBlank(params.get("operatorId"))) {
			str.append(" and p.createBy = ").append(params.get("operatorId"));
		}
		if (StringUtils.isNotBlank(params.get("moneyBegin"))) {
			str.append(" and batch.batch_total_money >= ").append(params.get("moneyBegin"));
		}
		if (StringUtils.isNotBlank(params.get("moneyEnd"))) {
			str.append(" and batch.batch_total_money <= ").append(params.get("moneyEnd"));
		}
		if (StringUtils.isNotBlank(params.get("printStatus"))) {
			if ("0".equals(params.get("printStatus"))) {
				str.append(" and (r.printFlag = ").append(params.get("printStatus")).append(" or r.printFlag is null)");
			} else if ("1".equals(params.get("printStatus"))) {
				str.append(" and r.printFlag = ").append(params.get("printStatus"));
			}
		}
		//payedMoney == N 表示未付  C354需求 已付金额为未付
		if(StringUtils.isNotBlank(params.get("payedMoneyStatus")) && "N".equals(params.get("payedMoneyStatus"))){
			str.append(" and not exists (select id from refund rf where rf.record_id = batch.id ")
			   .append("  and rf.moneyType = 4 and rf.status is null and rf.del_flag = '0' ")
			   .append(" and rf.orderType = r.productType ) ");
		}
		str.append(" GROUP BY batch.batch_no ");
		String orderBy = page.getOrderBy();
		if(StringUtils.isBlank(orderBy)){
			page.setOrderBy(" updateDate DESC ");
		}
		return findBySql(page, str.toString(), Map.class);
	}

	@Override
	public Page<Map<String, Object>> findBorrowMoneyListDT(
			Map<String, String> params, Page<Map<String, Object>> page) {
		StringBuffer str = new StringBuffer();
		String where = borrowMoneyWhere(params);
		String moneyWhere = getMoneyWhere(params);
		str.append("SELECT t.reviewId, t.orderId, t.createDate, t.updateDate,")
		   .append(" t.groupCode, t.operatorId, t.productId, t.orderType, t.payStatus,t.groupId, t.travelerId, t.agentId FROM ")
		   .append(" ( SELECT r.id AS reviewId, r.orderId, r.createDate, r.updateDate,")
		   .append(" g.groupCode AS groupCode, g.createBy AS operatorId, o.productId AS productId,")
		   .append(" m.orderType, r.payStatus, g.id as groupId, r.travelerId, o.orderCompany as agentId FROM money_amount m ")
		   .append(" LEFT JOIN review r ON r.productType = m.orderType AND r.orderId = m.uid and r.productType not in (6,7) ")
		   .append(" AND m.reviewId = r.id, productorder o, activitygroup g ")
		   .append(" WHERE o.id = r.orderId AND g.id = o.productGroupId ")
		   .append(" and m.orderType = ").append(Integer.parseInt(params.get("orderType")))
		   .append(" AND m.moneyType = ").append(Context.MONEY_TYPE_JK).append(" AND m.businessType = 1 ")
		   .append(" and r.companyId = ").append(Integer.parseInt(params.get("companyId")))
		   .append(" AND r.STATUS in (2,3) ");
		if(!"".equals(moneyWhere)){
			str.append(moneyWhere);
		}
		str.append(" group by r.id ) t where 1 = 1 ").append(where);
		String orderBy = page.getOrderBy();
		if(StringUtils.isBlank(orderBy)){
			page.setOrderBy("createDate DESC");
		}
		return findBySql(page, str.toString(), Map.class);
	}

	@Override
	public Page<Map<String, Object>> findBorrowMoneyListQZ(
			Map<String, String> params, Page<Map<String, Object>> page) {
		StringBuffer str = new StringBuffer();
		String where = borrowMoneyWhere(params);
		String moneyWhere = getMoneyWhere(params);
		str.append("SELECT t.reviewId, t.orderId, t.createDate, t.updateDate,")
		   .append("  t.groupCode, t.operatorId, t.productId, t.orderType, t.payStatus, t.travelerId, t.agentId FROM ")
		   .append(" ( SELECT r.id AS reviewId, r.orderId, r.createDate, r.updateDate, p.groupCode,")
		   .append(" p.createBy AS operatorId, p.id AS productId, m.orderType, r.payStatus, r.travelerId, o.agentinfo_id as agentId ")
		   .append(" FROM money_amount m LEFT JOIN review r ON r.productType = m.orderType ")
		   .append(" AND r.id = m.reviewId, visa_order o, visa_products p WHERE  o.id = r.orderId ")
		   .append(" AND p.id = o.visa_product_id AND r.STATUS in (2,3) and p.productStatus = 2 ")
		   .append(" and r.companyId = p.proCompanyId ")
		   .append(" and p.proCompanyId = ").append(Integer.parseInt(params.get("companyId")))
		   .append(" and orderType = ").append(Context.ORDER_TYPE_QZ).append(" AND m.moneyType = ")
		   .append(Context.MONEY_TYPE_JK);
		if(!"".equals(moneyWhere)){
			str.append(moneyWhere);
		}
		str.append(" group by r.id ) t where 1 = 1 ").append(where);
		String orderBy = page.getOrderBy();
		if(StringUtils.isBlank(orderBy)){
			page.setOrderBy("createDate DESC");
		}
		return findBySql(page, str.toString(), Map.class);
	}

	@Override
	public Page<Map<String, Object>> findBorrowMoneyListJP(
			Map<String, String> params, Page<Map<String, Object>> page) {
		StringBuffer str = new StringBuffer();
		String where = borrowMoneyWhere(params);
		String moneyWhere = getMoneyWhere(params);
		str.append("SELECT t.reviewId, t.orderId, t.createDate, t.updateDate,")
		   .append(" t.groupCode, t.operatorId, t.productId, t.orderType, t.payStatus, t.travelerId, t.agentId ")
		   .append(" FROM ( SELECT m.currencyId, m.amount, r.id AS reviewId, r.orderId, r.createDate,")
		   .append(" r.updateDate, p.group_code AS groupCode,p.createBy AS operatorId,")
		   .append(" p.id AS productId, m.orderType, r.payStatus, r.travelerId, o.agentinfo_id as agentId ")
		   .append(" FROM money_amount m LEFT JOIN review r ON r.productType = orderType ")
		   .append(" AND r.orderId = m.uid AND m.reviewId = r.id, airticket_order o, activity_airticket p ")
		   .append(" WHERE o.id = r.orderId AND p.id = o.airticket_id AND m.businessType = 1 ")
		   .append(" AND r.STATUS in (2,3) and p.productStatus = 2 and m.orderType = ").append(Context.ORDER_TYPE_JP)
		   .append(" and r.companyId = p.proCompany ")
		   .append(" AND m.moneyType = ").append(Context.MONEY_TYPE_JK).append(" and p.proCompany = ")
		   .append(Integer.parseInt(params.get("companyId")));
		if(!"".equals(moneyWhere)){
			str.append(moneyWhere);
		}
		str.append(" group by r.id ) t where 1 = 1 ").append(where);
		String orderBy = page.getOrderBy();
		if(StringUtils.isBlank(orderBy)){
			page.setOrderBy("createDate DESC");
		}
		return findBySql(page, str.toString(), Map.class);
	}
	
	@Override
	public Page<Map<String, Object>> findBorrowMoneyListAll(
			Map<String, String> params, Page<Map<String, Object>> page) {
		StringBuffer str = new StringBuffer();
		String where = borrowMoneyWhere(params);
		String moneyWhere = getMoneyWhere(params);
		Integer companyId = Integer.parseInt(params.get("companyId"));
		str.append("SELECT tt.currencyId, tt.amount, tt.reviewId, tt.orderId, tt.createDate, tt.updateDate, ")
		   .append(" tt.groupCode, tt.operatorId, tt.productId, tt.orderType, tt.payStatus, tt.travelerId, ")
		   .append(" tt.agentId,tt.chanpname, tt.groupId, tt.isShowPrint, tt.printFlag, tt.printTime ")
		   .append(" FROM ( SELECT t.currencyId, t.amount, t.reviewId, t.orderId, t.createDate, t.updateDate,")
		   .append(" t.groupCode, t.operatorId, t.productId, t.orderType, t.payStatus, t.travelerId, t.agentId,t.chanpname,")
		   .append(" t.groupId,t.isShowPrint,t.printFlag,t.printTime FROM ( SELECT m.currencyId, m.amount, r.id AS reviewId, r.orderId, r.createDate,")
		   .append(" r.updateDate, g.groupCode AS groupCode, g.createBy AS operatorId, '' AS chanpname, o.productId AS productId, m.orderType,")
		   .append(" r.payStatus, g.id AS groupId, r.travelerId, o.orderCompany AS agentId, ")
		   .append(" r.status as isShowPrint,r.printFlag,r.printTime FROM review r ")
		   .append(" LEFT JOIN money_amount m ON r.productType = orderType AND r.orderId = m.uid AND m.reviewId = r.id and r.productType in (1,2,3,4,5,10),")
		   .append(" productorder o, activitygroup g WHERE o.id = r.orderId AND g.id = o.productGroupId ")
		   .append(" AND m.moneyType = ").append(Context.MONEY_TYPE_JK).append(" AND m.businessType = 1 ")
		   .append(" AND r.STATUS in (2,3) AND r.companyId = ").append(companyId);
		if(!"".equals(moneyWhere)){
				str.append(moneyWhere);
			}
	    str.append(" group by r.id ) t ")
		   .append(" UNION ")
		   .append(" SELECT t.currencyId, t.amount, t.reviewId, t.orderId, t.createDate, t.updateDate, t.groupCode,")
		   .append(" t.operatorId, t.productId, t.orderType, t.payStatus, t.travelerId, t.agentId, t.chanpname,'' AS groupId, ")
		   .append(" t.isShowPrint,t.printFlag,t.printTime FROM ( SELECT m.currencyId, m.amount, r.id AS reviewId, r.orderId, r.createDate, r.updateDate,")
		   .append(" p.groupCode, p.createBy AS operatorId, '' AS chanpname, p.id AS productId, m.orderType, r.payStatus, r.travelerId,")
		   .append(" o.agentinfo_id AS agentId,r.status as isShowPrint,r.printFlag,r.printTime FROM ")
		   .append(" review r LEFT JOIN money_amount m ON r.productType = orderType ")
		   .append(" AND r.productType = ").append(Context.ORDER_TYPE_QZ)
		   .append(" AND r.orderId = m.uid AND r.id = m.reviewId, visa_order o, visa_products p WHERE o.id = r.orderId ")
		   .append(" AND p.id = o.visa_product_id AND m.businessType = 1 AND r.STATUS in (2,3) AND p.productStatus = 2 ")
		   .append(" AND r.companyId = p.proCompanyId AND m.moneyType = ").append(Context.MONEY_TYPE_JK)
		   .append(" AND p.proCompanyId = ").append(companyId);
	    if(!"".equals(moneyWhere)){
				str.append(moneyWhere);
			}
	    str.append(" group by r.id ) t ")
		   .append(" UNION ")
		   .append(" SELECT t.currencyId, t.amount, t.reviewId, t.orderId, t.createDate, t.updateDate, t.groupCode, t.operatorId,")
		   .append(" t.productId, t.orderType, t.payStatus, t.travelerId, t.agentId, t.chanpname, '' AS groupId, ")
		   .append(" t.isShowPrint,t.printFlag,t.printTime FROM ( SELECT m.currencyId, m.amount, r.id AS reviewId, r.orderId, r.createDate, r.updateDate,")
		   .append(" p.group_code AS groupCode, p.createBy AS operatorId, '' AS chanpname, p.id AS productId, m.orderType, r.payStatus, ")
		   .append(" r.travelerId, o.agentinfo_id AS agentId,r.status as isShowPrint,r.printFlag,")
		   .append(" r.printTime FROM review r LEFT JOIN money_amount m ")
		   .append(" ON r.productType = orderType AND r.orderId = m.uid AND m.reviewId = r.id and r.productType = ").append(Context.ORDER_TYPE_JP)
		   .append(" , airticket_order o, activity_airticket p WHERE o.id = r.orderId AND p.id = o.airticket_id AND m.businessType = 1 ")
		   .append(" AND r.STATUS in (2,3) AND p.productStatus = 2 AND r.companyId = p.proCompany AND m.moneyType = ").append(Context.MONEY_TYPE_JK)
		   .append(" AND p.proCompany = ").append(companyId);
	    if(!"".equals(moneyWhere)){
			str.append(moneyWhere);
		}
		/*新加支持海岛游借款的查询 add by chy 2015年6月15日11:30:12*/
		str.append(" group by r.id ) t ")
		   .append(" UNION ");
		str.append(" SELECT t.currencyId, t.amount, t.reviewId, t.orderId, t.createDate, t.updateDate,")
		   .append(" t.groupCode, t.operatorId, t.productId, t.orderType, t.payStatus, t.travelerId, t.agentId,t.chanpname,")
		   .append(" t.groupId,t.isShowPrint,t.printFlag,t.printTime FROM ( SELECT m.currencyId, m.amount, r.id AS reviewId, r.orderId, r.createDate,")
		   .append(" r.updateDate, g.groupCode AS groupCode, g.createBy AS operatorId, ai.activityName AS chanpname, ai.uuid AS productId, '12' as orderType,")
		   .append(" r.payStatus, g.uuid AS groupId, r.travelerId, o.orderCompany AS agentId,")
		   .append(" r.status as isShowPrint,r.printFlag,r.printTime FROM review r")
		   .append(" LEFT JOIN island_money_amount m ON r.productType = 12 AND m.reviewId = r.id ,")
		   .append(" island_order o, activity_island_group g,activity_island ai WHERE o.id = r.orderId AND ai.uuid = o.activity_island_uuid AND g.uuid = o.activity_island_group_uuid ")
		   .append(" AND m.moneyType = ").append(Context.MONEY_TYPE_JK).append(" AND m.businessType = 1 ")
		   .append(" AND r.STATUS = 2 AND r.companyId = ").append(companyId);
		
		   if(!"".equals(moneyWhere)){
				str.append(moneyWhere);
			}
		   /*新加支持酒店借款的查询  add by chy 2015年6月15日11:30:12*/
		   str.append(" group by r.id ) t ")
		   .append(" UNION ");
		str.append(" SELECT t.currencyId, t.amount, t.reviewId, t.orderId, t.createDate, t.updateDate,")
		   .append(" t.groupCode, t.operatorId, t.productId, t.orderType, t.payStatus, t.travelerId, t.agentId,t.chanpname,")
		   .append(" t.groupId,t.isShowPrint,t.printFlag,t.printTime FROM ( SELECT m.currencyId, m.amount, r.id AS reviewId, r.orderId, r.createDate,")
		   .append(" r.updateDate, g.groupCode AS groupCode, g.createBy AS operatorId, ht.activityName AS chanpname, ht.uuid AS productId, '11' as orderType,")
		   .append(" r.payStatus, g.uuid AS groupId, r.travelerId, o.orderCompany AS agentId ,")
		   .append(" r.status as isShowPrint,r.printFlag,r.printTime FROM review r ")
		   .append(" LEFT JOIN hotel_money_amount m ON r.productType = 11 AND m.reviewId = r.id ,")
		   .append(" hotel_order o, activity_hotel_group g, activity_hotel ht WHERE o.id = r.orderId AND ht.uuid = o.activity_hotel_uuid AND g.uuid = o.activity_hotel_group_uuid ")
		   .append(" AND m.moneyType = ").append(Context.MONEY_TYPE_JK).append(" AND m.businessType = 1 ")
		   .append(" AND r.STATUS = 2 AND r.companyId = ").append(companyId);
		if(!"".equals(moneyWhere)){
				str.append(moneyWhere);
			}
		str.append(" group by r.id ) t ) tt where 1 = 1 ").append(where);
		String orderBy = page.getOrderBy();
		if(StringUtils.isBlank(orderBy)){
			page.setOrderBy("updateDate DESC");
		}
		return findBySql(page, str.toString(), Map.class);
	}

	/**
	 * 生成借款查询的where条件SQL语句
	 * 
	 * @param params
	 * @author shijun.liu
	 * @date 2015.05.14
	 * @return
	 */
	private String borrowMoneyWhere(Map<String, String> params){
		String groupCode = params.get("groupCode");             //团号
		String operatorId = params.get("operatorId");           //计调
		String payStatus = params.get("payStatus");             //付款状态
		String printStatus = params.get("printStatus");         //打印状态
		StringBuffer str = new StringBuffer();
		if (StringUtils.isNotBlank(groupCode)) {
			str.append(" and groupCode like '%").append(groupCode).append("%'");
		}
		if (StringUtils.isNotBlank(operatorId)) {
			str.append(" and operatorId = ").append(
					Integer.parseInt(operatorId));
		}
		if (StringUtils.isNotBlank(payStatus)) {
			str.append(" and payStatus =").append(Integer.parseInt(payStatus));
		}
		if (StringUtils.isNotBlank(printStatus)) {
			if ("0".equals(printStatus)) {
				str.append(" and (printFlag = ").append(printStatus).append(" or printFlag is null)");
			} else if ("1".equals(printStatus)) {
				str.append(" and printFlag = ").append(printStatus);
			}
		}
		return str.toString();
	}

	/**
	 * 借款选择币种，金额条件时，条件语句单独拼接
	 * @param params
	 * @author shijun.liu
	 * @date 2015.05.25
	 * @return
	 */
	private String getMoneyWhere(Map<String, String> params){
		StringBuffer str = new StringBuffer();
		String currencyId = params.get("currencyId");           //币种ID
		String moneyBegin = params.get("moneyBegin");           //开始金额
		String moneyEnd = params.get("moneyEnd");               //结束金额
		String payedMoneyStatus = params.get("payedMoneyStatus");
		/*新加过滤条件产品类型 by chy 2015年6月15日16:03:00*/
		String orderType = params.get("orderType");      //产品类型
		if (StringUtils.isNotBlank(orderType) && !"0".equals(orderType)) {
			str.append(" and r.productType = ").append(
					Integer.parseInt(orderType));
		}
		if (StringUtils.isNotBlank(currencyId)) {
			str.append(" and m.currencyId = ").append(
					Integer.parseInt(currencyId));
		}
		if (StringUtils.isNotBlank(moneyBegin)) {
			str.append(" and m.amount >= ").append(Double.valueOf(moneyBegin));
		}
		if (StringUtils.isNotBlank(moneyEnd)) {
			str.append(" and m.amount <= ").append(Double.valueOf(moneyEnd));
		}
		//payedMoney == N 表示未付  C354需求 已付金额为未付
		if(StringUtils.isNotBlank(payedMoneyStatus) && "N".equals(payedMoneyStatus)){
			str.append(" and not exists (select id from refund rf where rf.record_id = r.id ")
			   .append("  and rf.moneyType = 4 and rf.status is null and rf.del_flag = '0' ")
			   .append(" and rf.orderType = r.productType ) ");
		}
		return str.toString();
	}

	@Override
	@Deprecated
	public List<Map<String, Object>> getTTSQZPayRecord(Integer batchId,
			Integer orderType) {
		StringBuffer str = new StringBuffer();
		str.append("SELECT r.id, d.label as payType,FORMAT(amount,2) as amount,")
		   .append(" create_date as payDate,pay_voucher as payVoucher,")
		   .append(" `status` FROM refund r ")
		   .append(" LEFT JOIN money_amount m ON m.serialNum = r.money_serial_num")
		   .append(" AND r.orderType = m.orderType")
		   .append(" LEFT JOIN sys_dict d ON r.pay_type = d.`VALUE`")
		   .append(" AND d.type = 'offlineorder_pay_type' WHERE")
		   .append(" r.moneyType = ").append(Refund.MONEY_TYPE_BATCHBORROW).append(" and r.orderType = ").append(orderType)
		   .append(" AND r.record_id = ").append(batchId);
		return findBySql(str.toString(), Map.class);
	}

	@Override
	public long getBorrowMoneyNotPayedCountForTTS(Long companyId) {
		StringBuffer str = new StringBuffer();
		String companyCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		str.append("SELECT count(*) FROM  ")
		   .append(" ( SELECT batch_no FROM visa_flow_batch_opration b, review r, review_detail d")
		   .append(" WHERE r.id = d.review_id AND d.myKey = 'visaBorrowMoneyBatchNo' ")
		   .append(" AND b.batch_no = d.myValue AND r.companyId = ").append(companyId.intValue())
		   .append(" AND b.busyness_type = 2 AND b.review_status IN (2, 3) ")//review_status 与 review 表的status一致
		   .append(" AND r.flowType = ").append(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY)
		   .append(" AND r.payStatus = 0 GROUP BY b.batch_no ")
		   .append(" union ")
		   .append(" SELECT DISTINCT b.batch_no FROM visa_flow_batch_opration b, review_new r ")
		   .append(" WHERE r.batch_no = b.batch_no AND b.busyness_type = 2 ")
		   .append(" AND r.`status` = ").append(ReviewConstant.REVIEW_STATUS_PASSED)
		   .append(" AND r.company_id = '").append(companyCompanyUuid).append("' ")
		   .append(" AND r.process_type = '").append(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY + "' ")
		   .append(" AND r.pay_status = 0 ) t ");
		return getCountBySQL(str.toString());
	}

	@Override
	public long getBorrowMoneyNotPayedCount(Long companyId) {
		StringBuffer str = new StringBuffer();
		String companyCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		str.append(" SELECT ")
		   .append(" 	count(*) as count ")
		   .append(" FROM ")
		   .append(" 	( ")
		   .append(" 		SELECT ")
		   .append(" 			r.id ")
		   .append(" 		FROM ")
		   .append(" 			review r ")
		   .append(" 		WHERE ")
		   .append(" 			r.companyId = ").append(companyId.intValue())
		   .append(" 		AND r.`status` IN (2, 3) ")
		   .append(" 		AND r.active = 1 ")
		   .append(" 		AND r.flowType IN (19, 20) ")
		   .append(" 		AND r.payStatus = 0 ")
		   .append(" 		UNION ")
		   .append(" 			SELECT ")
		   .append(" 				r.id ")
		   .append(" 			FROM ")
		   .append(" 				review_new r ")
		   .append(" 				LEFT JOIN money_amount m ON r.product_type = m.orderType AND r.order_id = m.uid AND m.review_uuid = r.id ")
		   .append(" 			WHERE ")
		   .append(" 				r.company_id = '").append(companyCompanyUuid).append("' ")
		   .append(" 			AND r.`status` = ").append(ReviewConstant.REVIEW_STATUS_PASSED)
		   .append(" 			AND r.process_type = '").append(Context.REVIEW_FLOWTYPE_BORROWMONEY + "' ")
		   .append(" 			AND r.pay_status = 0 ")
		   .append(" 			AND m.businessType = 1 AND m.moneyType = 12 ")
		   .append(" 	) t ");
		return getCountBySQL(str.toString());
	}
}
