package com.trekiz.admin.review.pay.dao.impl;

import com.quauq.review.core.engine.config.ReviewConstant;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.order.entity.Refund;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.pay.dao.IPaymanagerNewDao;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 支付接口DAO实现类 实现SQL
 * 
 * @author Administrator
 * 
 */
@Repository
public class PayManagerDaoNewImpl extends BaseDaoImpl<Map<String, Object>>
		implements IPaymanagerNewDao {

	/**
	 * 2：表示批量借款，1：表示批量还收据
	 */
	private static final int BUSYNESS_TYPE_2 = 2;
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
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		// 组织查询SQL
		Object orderTypes = params.get("orderTypes");
		Object payMode = params.get("payMode");				//支付方式
		Object fromBankName = params.get("fromBankName");	//来款银行
		StringBuffer querySql = new StringBuffer();

		querySql.append("select groupcode,groupid,orderid,orderno,comments,prdtype,flowtype,createdate,agentid,salecreateby,jidcreateby,")
			.append("supplycom,chanpid,chanpname,revid,revcom,lastoperator,revactive,createtime,updatetime,curlevel,toplevel,revstatus,")
			.append("printTime,cpid,deptId,printFlag,travelerid,travelerName,payStatus,salerId,salerName,costname,currencyId,rebatesDiff,")
			.append("reviewflag,reviewUuid,rate,payConfirmDate FROM ")
			.append("(SELECT rv.groupcode,rv.groupid,rv.orderid,rv.orderno,rv.comments,rv.prdtype,rv.flowtype,rv.createdate,rv.agentid,")
			.append("rv.salecreateby,rv.jidcreateby,rv.supplycom,rv.chanpid,rv.chanpname,rv.revid,rv.revcom,rv.lastoperator,rv.revactive,")
			.append("rv.createtime,rv.updatetime,rv.curlevel,rv.toplevel,rv.revstatus,rv.printTime,rv.cpid,rv.deptId,rv.printFlag,")
			.append("rv.travelerid,(SELECT t.NAME FROM traveler t WHERE t.id = rv.travelerid) AS travelerName,rv.payStatus,rv.salerId,")
			.append("rv.salerName,o.costname, o.currencyId, o.rebatesDiff, 1 reviewflag, rv.revid reviewUuid, o.rate rate, rv.payConfirmDate")
			.append(" FROM refundreview_view rv, order_rebates o ")
			.append(" WHERE rv.revid = o.rid AND rv.revstatus IN (2, 3) AND rv.flowtype = ").append(flowtype)
			.append(" AND rv.revcom = ").append(companyId);
		if(fromBankName!=null && StringUtils.isNotBlank(fromBankName.toString())) {
			querySql.append(" AND EXISTS(select r.id from refund r, pay_remittance pr where r.record_id=rv.revid and r.moneyType=3 ")
				.append("and r.pay_type=4 and r.pay_type_Id=pr.id and pr.bank_name ='")
				.append(fromBankName.toString()).append("')");
		}else if(payMode!=null && StringUtils.isNotBlank(payMode.toString())) {
			querySql.append(" AND EXISTS(select r.id from refund r, pay_remittance pr where r.record_id=rv.revid and r.moneyType=3 and r.pay_type=").append(Integer.valueOf(payMode.toString()))
				.append(")");
		}
		querySql.append(" UNION ")
			.append("SELECT rv.groupcode,rv.groupid,rv.orderid,rv.orderno,rv.comments,rv.prdtype,rv.flowtype,rv.createdate,rv.agentid,")
			.append("rv.salecreateby,rv.jidcreateby,rv.supplycom,rv.chanpid,rv.chanpname,rv.revid,rv.revcom,rv.lastoperator,rv.revactive,")
			.append("rv.createtime,rv.updatetime,rv.curlevel,rv.toplevel,rv.revstatus,rv.printTime,rv.cpid,rv.deptId,rv.printFlag,rv.travelerid,")
			.append("(SELECT t.NAME FROM traveler t WHERE t.id = rv.travelerid) AS travelerName,rv.payStatus,rv.salerId,rv.salerName,")
			.append("'' costname, m.currencyId, m.amount rebatesDiff, 1 reviewflag, m.reviewId reviewUuid, m.exchangerate rate,rv.payConfirmDate ")
			.append(" FROM refundreview_view rv, money_amount m, currency cur ")
			.append(" WHERE cur.currency_id = m.currencyId AND cur.create_company_id = ").append(companyId)
			.append(" AND rv.revid = m.reviewId AND rv.revstatus IN (2, 3) AND rv.flowtype = ").append(flowtype)
			.append(" AND rv.revcom = ").append(companyId);
		if(fromBankName!=null && StringUtils.isNotBlank(fromBankName.toString())) {
			querySql.append(" AND EXISTS(select r.id from refund r, pay_remittance pr where r.record_id=rv.revid and r.moneyType=3 ")
				.append("and r.pay_type=4 and r.pay_type_Id=pr.id and pr.bank_name ='")
				.append(fromBankName.toString()).append("')");
		}else if(payMode!=null && StringUtils.isNotBlank(payMode.toString())) {
			querySql.append(" AND EXISTS(select r.id from refund r, pay_remittance pr where r.record_id=rv.revid and r.moneyType=3 and r.pay_type=").append(Integer.valueOf(payMode.toString()))
				.append(")");
		}
		querySql.append(" UNION ")
			.append(" SELECT review.group_code groupcode,review.group_id groupid,review.order_id orderid,review.order_no orderno,")
			.append("'' comments,review.product_type prdtype,review.process_type flowtype,review.create_date createdate,review.agent agentid,")
			.append("review.create_by salecreateby,review.operator jidcreateby,'' supplycom,review.product_id chanpid,review.product_name chanpname,")
			.append("review.id_long revid,review.company_id revcom,review.last_reviewer lastoperator,'' revactive,review.create_date createtime,")
			.append("review.update_date updatetime,'' curlevel,'' toplevel,review.`status` revstatus,review.print_date printTime,'' cpid,")
			.append("review.dept_id deptId,review.print_status printFlag,review.traveller_id travelerid,")
			.append("(SELECT t.NAME FROM traveler t WHERE t.id = review.traveller_id) AS travelerName,")
			.append("review.pay_status payStatus,review.saler salerId,review.saler_name salerName,r.costname,r.currencyId,r.rebatesDiff,")
			.append("2 reviewflag, review.id reviewUuid, r.currencyExchangerate rate, review.pay_confirm_date payConfirmDate")
			.append(" FROM review_new review, rebates r ")
			.append(" WHERE review.id = r.rid AND review.company_id = '").append(companyUuid).append("'")
			.append(" AND review.`status` = 2 AND review.process_type = '").append(Context.REBATES_FLOW_TYPE + "' ")
			.append(" AND r.orderType not in (6, 7) ");
		if(fromBankName!=null && StringUtils.isNotBlank(fromBankName.toString())) {
			querySql.append(" AND EXISTS(select refund.id from refund, pay_remittance pr where refund.record_id=review.id_long and refund.moneyType=9")
				.append(" and refund.orderType not in (6,7) and refund.pay_type=4 and refund.pay_type_Id=pr.id and pr.bank_name ='")
				.append(fromBankName.toString()).append("')");
		}else if(payMode!=null && StringUtils.isNotBlank(payMode.toString())) {
			querySql.append(" AND EXISTS(select refund.id from refund, pay_remittance pr where refund.record_id=review.id_long")
				.append(" and refund.orderType not in (6,7) and refund.moneyType=9 and refund.pay_type=").append(Integer.valueOf(payMode.toString()))
				.append(")");
		}
		querySql.append(" UNION ")
			.append(" SELECT review.group_code groupcode,review.group_id groupid,review.order_id orderid,review.order_no orderno,'' comments,")
			.append("review.product_type prdtype,review.process_type flowtype,review.create_date createdate,review.agent agentid,")
			.append("review.create_by salecreateby,review.operator jidcreateby,'' supplycom,review.product_id chanpid,")
			.append("review.product_name chanpname,review.id_long revid,review.company_id revcom,review.last_reviewer lastoperator,")
			.append("'' revactive,review.create_date createtime,review.update_date updatetime,'' curlevel,'' toplevel,")
			.append("review.`status` revstatus,review.print_date printTime,'' cpid,review.dept_id deptId,review.print_status printFlag,")
			.append("(select rb.travelerId from rebates rb where rb.rid = review.id group by orderId HAVING count(*)=1 and travelerId is not null) travelerid,")
			.append("(SELECT t.NAME FROM traveler t WHERE t.id = (SELECT rb.travelerId FROM rebates rb WHERE rb.rid = review.id GROUP BY orderId ")
			.append(" HAVING count(*) = 1 AND travelerId IS NOT NULL)) AS travelerName,review.pay_status payStatus,review.saler salerId,")
			.append("review.saler_name salerName,r.costname,r.currencyId,SUM(r.rebatesDiff) rebatesDiff, 2 reviewflag,")
			.append("review.id reviewUuid, r.currencyExchangerate rate, review.pay_confirm_date payConfirmDate")
			.append(" FROM review_new review,rebates r ")
			.append(" WHERE review.id = r.rid AND review.company_id = '").append(companyUuid).append("'")
			.append(" AND review.`status` = 2 AND review.process_type = '").append(Context.REBATES_FLOW_TYPE + "' ")
			.append(" AND r.orderType = 7");
		if(fromBankName!=null && StringUtils.isNotBlank(fromBankName.toString())) {
			querySql.append(" AND EXISTS(select refund.id from refund, pay_remittance pr where refund.record_id=review.id_long and refund.moneyType=9")
				.append(" and refund.orderType=7 and refund.pay_type=4 and refund.pay_type_Id=pr.id and pr.bank_name ='")
				.append(fromBankName.toString()).append("')");
		}else if(payMode!=null && StringUtils.isNotBlank(payMode.toString())) {
			querySql.append(" AND EXISTS(select refund.id from refund, pay_remittance pr where refund.record_id=review.id_long")
				.append(" and refund.orderType=7 and refund.moneyType=9 and refund.pay_type=").append(Integer.valueOf(payMode.toString()))
				.append(")");
		}
		querySql.append(" GROUP BY review.id");
		querySql.append(" UNION ")//update by yudong.xu 2016-4-25 c460v3需求，签证使用产品团号。
				.append(" SELECT ");
			if(Context.SUPPLIER_UUID_HQX.equals(companyUuid)){
				// 环球行账号，团号规则还是还原成C460V3之前的团号， add by shijun.liu 2016.05.12
				querySql.append(" review.group_code AS groupCode, ");
			}else{
				querySql.append(" p.groupCode AS groupCode, ");
			}
		querySql.append(" review.group_id groupid, review.order_id orderid, review.order_no orderno, '' comments,")
				.append(" review.product_type prdtype,review.process_type flowtype, review.create_date createdate, review.agent agentid,")
				.append(" review.create_by salecreateby, review.operator jidcreateby,'' supplycom, review.product_id chanpid,")
				.append("  review.product_name chanpname,review.id_long revid, review.company_id revcom, review.last_reviewer lastoperator,")
				.append(" '' revactive, review.create_date createtime, review.update_date updatetime, '' curlevel,'' toplevel,")
				.append(" review.`status` revstatus,review.print_date printTime,'' cpid, review.dept_id deptId,  review.print_status printFlag,")
				.append(" ( SELECT rb.travelerId FROM rebates rb WHERE rb.rid = review.id GROUP BY orderId HAVING count(*) = 1 AND travelerId IS NOT NULL ) travelerid,")
				.append(" ( SELECT t. NAME FROM traveler t WHERE t.id = (SELECT rb.travelerId FROM rebates rb WHERE rb.rid = review.id GROUP BY orderId ")
				.append(" HAVING count(*) = 1 AND travelerId IS NOT NULL )) AS travelerName,review.pay_status payStatus, review.saler salerId,")
				.append(" review.saler_name salerName, r.costname, r.currencyId, SUM(r.rebatesDiff) rebatesDiff,")
				.append(" 2 reviewflag, review.id reviewUuid, r.currencyExchangerate rate, review.pay_confirm_date payConfirmDate")
				.append(" FROM review_new review, rebates r, visa_products p ")
				.append(" WHERE review.id = r.rid AND review.product_id = p.id AND review.company_id = '").append(companyUuid).append("'")
				.append(" AND review.`status` = 2 AND review.process_type = '").append(Context.REBATES_FLOW_TYPE + "' ")
				.append(" AND review.product_type = '6' AND r.orderType = 6");
		if(fromBankName!=null && StringUtils.isNotBlank(fromBankName.toString())) {
			querySql.append(" AND EXISTS(select refund.id from refund, pay_remittance pr where refund.record_id=review.id_long and refund.moneyType=9")
				.append(" and refund.orderType=6 and refund.pay_type=4 and refund.pay_type_Id=pr.id and pr.bank_name ='")
				.append(fromBankName.toString()).append("')");
		}else if(payMode!=null && StringUtils.isNotBlank(payMode.toString())) {
			querySql.append(" AND EXISTS(select refund.id from refund, pay_remittance pr where refund.record_id=review.id_long")
				.append(" and refund.orderType=6 and refund.moneyType=9 and refund.pay_type=").append(Integer.valueOf(payMode.toString()))
				.append(")");
		}
		querySql.append(" GROUP BY review.id")
			.append(" ) rv where 1=1 ");
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
				querySql.append(" and (rv.prdtype = 11 or rv.prdType = 12) ");
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
		if (currency != null && !"".equals(currency)) {
			querySql.append(" and rv.currencyId = '" + currency + "'");
		}
			
		if (startMoney != null && !"".equals(startMoney)) {
			querySql.append(" and rv.rebatesDiff between " + startMoney + " ");
		}
			
		if (endMoney != null && !"".equals(endMoney)) {
			querySql.append(" and " + endMoney + " ");
		}

		Object creators = params.get("creators");
		if (creators != null && !"".equals(creators)) {
			querySql.append(" and rv.salecreateby = '" + creators + "'");
		}
		
		Object salers = params.get("salers");
		if (salers != null && !"".equals(salers)) {
			querySql.append(" and rv.salerId = '" + salers + "'");
		}
		Object travelers = params.get("travelers");
		if (travelers != null && !"".equals(travelers)) {
			String s = travelers.toString().replace(" ", "");
			if(!"".equals(s)) {
				if(s.contains("\\")) {
					s = s.replace("\\", "\\\\\\\\");
				}
				if(s.contains("%") || s.contains("?") || s.contains("_") || s.contains("'")) {
					if("".equals(s)) s = s.toString();
					s = s.replace("%", "\\%");
					s = s.replace("?", "\\?");
					s = s.replace("_", "\\_");
					s = s.replace("'", "\\'");
				}
				querySql.append(" and rv.travelerName like '%" + s + "%'");
			}
		}
		
		Object printFlag = params.get("printFlag");
		if (printFlag != null && !"".equals(printFlag)) {
			if ("0".equals(printFlag)) {
				querySql.append(" and (rv.printFlag = " + printFlag + " or rv.printFlag is null)");
			} else {
				querySql.append(" and rv.printFlag = " + printFlag);
			}
		}
		//0409需求
		Object cashierConfirmDateBegin = params.get("cashierConfirmDateBegin");
		if (cashierConfirmDateBegin != null && !"".equals(cashierConfirmDateBegin.toString().trim())){
			querySql.append(" AND payStatus = 1 ");//如果选择出纳确认时间，说明该退款已经确认支付。
			querySql.append(" AND payConfirmDate >= '").append(cashierConfirmDateBegin).append(" 00:00:00'");
		}

		Object cashierConfirmDateEnd = params.get("cashierConfirmDateEnd");
		if (cashierConfirmDateEnd != null && !"".equals(cashierConfirmDateEnd.toString().trim())){
			querySql.append(" AND payConfirmDate <= '").append(cashierConfirmDateEnd).append(" 23:59:59'");
			querySql.append(" AND payStatus = 1 ");//如果选择出纳确认时间，说明该退款已经确认支付。
		}
		
		Object paymentType =  params.get("paymentType");
		if (paymentType != null && !"".equals(paymentType.toString().trim())){
			querySql.append(" and agentid in (select id from agentinfo where paymentType = "+paymentType+") ");//如果选择出纳确认时间，说明该退款已经确认支付。
		}
		
		//0477需求，添加申请日期筛选条件 yang.wang 2016.7.25
		String createTimeMin = (String) params.get("createTimeMin");
		if (StringUtils.isNotBlank(createTimeMin)) {
			querySql.append(" AND createdate >= '").append(createTimeMin).append(" 00:00:00' ");//申请日期开始
		}
		
		String createTimeMax = (String) params.get("createTimeMax");
		if (StringUtils.isNotBlank(createTimeMax)) {
			querySql.append(" AND createdate <= '").append(createTimeMax).append(" 23:59:59' ");//申请日期结束
		}
		
		Page<Map<String, Object>> pageParam = new Page<Map<String, Object>>(request, response);
		if(StringUtils.isBlank(pageParam.getOrderBy())){
			pageParam.setOrderBy("updatetime DESC");
		}
		
		// 579需求:在财务模块的付款类数据列表中增加导出Excel功能(返佣付款) gaoyang 2017-03-23
		if (params.get("isExcelExport") != null && "1".equals(params.get("isExcelExport").toString())) {
			pageParam.setPageNo(1);
			pageParam.setMaxSize(Integer.MAX_VALUE);
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
		Object payMode = params.get(Context.RefundPayParams.PAY_MODE);//支付方式
		Object fromBank = params.get(Context.RefundPayParams.FROM_BANK);
		boolean hasPayType = null != payMode && !"".equals(payMode.toString().trim());
		boolean hasFromBank = null != fromBank && !"".equals(fromBank.toString().trim());

		// 组织查询SQL
		StringBuffer querySql = new StringBuffer();
		querySql.append("select ")
				.append("s.reviewflag,")
				.append("s.groupcode,")
				.append("s.orderid," )
				.append("s.orderno," )
				.append("s.groupid," )
				.append("s.prdtype,")
				.append("s.flowtype,")
				.append("s.agentid,")
				.append("s.salecreateby,")
				.append("s.jidcreateby,")
				.append("s.chanpid,")
				.append("s.chanpname,")
				.append("s.revid," )
				.append("s.lastoperator,")
				.append("s.createtime," )
				.append("s.updatetime," )
				.append("s.printTime," )
				.append("s.printFlag," )
				.append("s.invoiceStatus,")
				.append("s.travelerid," )
				.append("s.payStatus,")
				.append("s.salerId," )
				.append("s.salerName," )
				.append("s.companyId,")
				.append("s.idlong,")
				.append("s.refundname refundName,")//款项
				.append("s.mcurid,")
				.append("s.mamount,s.rate,")
				.append("s.payConfirmDate from (");
		//退款SQL  根据不同产品新旧审核 写出不同的SQL
		//签证的新审核查询 reviewflag 1 标示旧审核 2 标示新审核
		StringBuffer querySqlNewPart = new StringBuffer();
		// C460V3PLUS 只针对环球行，签证团号还原成订单表的团号
		if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())){
			querySqlNewPart_HQX(querySqlNewPart);
			if (hasPayType){  //moneyType = 8 新审批退款付款
				querySqlNewPart.append(" AND EXISTS (SELECT 'x' FROM refund r WHERE r.record_id = a.id_long AND r.moneyType = 8 ")
						.append(" AND r.orderType = a.product_type AND r.pay_type =").append(payMode).append(") ");
			}
			if (hasFromBank){//取支付方式为汇款的来款银行。pay_type = 4
				querySqlNewPart.append(" AND EXISTS (SELECT 'x' FROM refund r,pay_remittance p WHERE r.pay_type_Id = p.id AND r.record_id = a.id_long ")
						.append(" AND r.orderType = a.product_type AND r.pay_type = 4 AND r.moneyType = 8 AND p.bank_name = '").append(fromBank).append("') ");
			}
		}else{
			querySqlNewPartCreate_Not_HQX(querySqlNewPart);
			if (hasPayType){
				querySqlNewPart.append(" AND EXISTS (SELECT 'x' FROM refund r WHERE r.record_id = a.id_long AND r.moneyType = 8 ")
						.append(" AND r.orderType = a.product_type AND r.pay_type =").append(payMode).append(") ");
			}
			if (hasFromBank){//取支付方式为汇款的来款银行。pay_type = 4； moneyType = 8 新审批退款付款
				querySqlNewPart.append(" AND EXISTS (SELECT 'x' FROM refund r,pay_remittance p WHERE r.pay_type_Id = p.id AND r.record_id = a.id_long ")
						.append(" AND r.orderType = a.product_type AND r.pay_type = 4 AND r.moneyType = 8 AND p.bank_name = '").append(fromBank).append("') ");
			}
			
			//机票、团期类退款新审批
			querySqlNewPart.append(" union ");
			querySqlNewPartCreateSql_group_airticket(querySqlNewPart);		
			if (hasPayType){
				querySqlNewPart.append(" AND EXISTS (SELECT 'x' FROM refund r WHERE r.record_id = a.id_long AND r.moneyType = 8 ")
						.append(" AND r.orderType = a.product_type AND r.pay_type =").append(payMode).append(") ");
			}
			if (hasFromBank){
				querySqlNewPart.append(" AND EXISTS (SELECT 'x' FROM refund r,pay_remittance p WHERE r.pay_type_Id = p.id AND r.record_id = a.id_long ")
						.append(" AND r.orderType = a.product_type  AND r.pay_type = 4 AND r.moneyType = 8 AND p.bank_name = '").append(fromBank).append("') ");
			}
		}
		//机票的旧退款审核查询
		StringBuffer querySqlOldAirticket = new StringBuffer();
		querySqlOldAirticketSql(querySqlOldAirticket);
		if (hasPayType){//旧审批的退款付款moneyType = 2
			querySqlOldAirticket.append(" AND EXISTS (SELECT 'x' FROM refund r WHERE r.record_id = r1.id AND r.moneyType = 2 ")
					.append(" AND r.orderType = 7 AND r.pay_type =").append(payMode).append(") ");
		}
		if (hasFromBank){
			querySqlOldAirticket.append(" AND EXISTS (SELECT 'x' FROM refund r,pay_remittance p WHERE r.pay_type_Id = p.id AND r.record_id = r1.id AND r.orderType = 7 ")
					.append(" AND r.pay_type = 4 AND r.moneyType = 2 AND p.bank_name = '").append(fromBank).append("') ");
		}
		//签证的旧退款审核查询
		StringBuffer querySqlOldVisa = new StringBuffer();
		querySqlOldVisa.append(" SELECT '1' as reviewflag,");
		// C460V3PLUS 只针对环球行，签证团号还原成订单表的团号
		if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())){
			querySqlOldVisa.append("a4.group_code AS groupcode,");
		}else{
			querySqlOldVisa.append("b4.groupCode AS groupcode,");
		}
		querySqlOldVisaCreate(querySqlOldVisa);
		if (hasPayType){//旧审批的退款付款moneyType = 2
			querySqlOldVisa.append(" AND EXISTS (SELECT 'x' FROM refund r WHERE r.record_id = r4.id AND r.moneyType = 2 ")
					.append(" AND r.orderType = 6 AND r.pay_type =").append(payMode).append(") ");
		}
		if (hasFromBank){
			querySqlOldVisa.append(" AND EXISTS (SELECT 'x' FROM refund r,pay_remittance p WHERE r.pay_type_Id = p.id AND r.record_id = r4.id AND r.orderType = 6 ")
					.append(" AND r.pay_type = 4 AND r.moneyType = 2 AND p.bank_name = '").append(fromBank).append("') ");
		}
		//单团类的旧退款审核查询
		StringBuffer querySqlOldSingle = new StringBuffer();
		querySqlOldSingleCreate(querySqlOldSingle);
		
		if (hasPayType){//旧审批的退款付款moneyType = 2
			querySqlOldSingle.append(" AND EXISTS (SELECT 'x' FROM refund r WHERE r.record_id = r5.id AND r.moneyType = 2 ")
					.append(" AND r.orderType = r5.productType AND r.pay_type =").append(payMode).append(") ");
		}
		if (hasFromBank){
			querySqlOldSingle.append(" AND EXISTS (SELECT 'x' FROM refund r,pay_remittance p WHERE r.pay_type_Id = p.id AND r.record_id = r5.id ")
					.append(" AND r.orderType = r5.productType AND r.pay_type = 4 AND r.moneyType = 2 AND p.bank_name = '").append(fromBank).append("') ");
		}
		//酒店的旧退款审核查询
		StringBuffer querySqlOldHotel = new StringBuffer();
		querySqlOldHotelCreateSql(querySqlOldHotel);
		if (hasPayType){//旧审批的退款付款moneyType = 2
			querySqlOldHotel.append(" AND EXISTS (SELECT 'x' FROM refund r WHERE r.record_id = r6.id AND r.moneyType = 2 ")
					.append(" AND r.orderType = 11 AND r.pay_type =").append(payMode).append(") ");
		}
		if (hasFromBank){
			querySqlOldHotel.append(" AND EXISTS (SELECT 'x' FROM refund r,pay_remittance p WHERE r.pay_type_Id = p.id AND r.record_id = r6.id ")
					.append(" AND r.orderType = 11 AND r.pay_type = 4 AND r.moneyType = 2 AND p.bank_name = '").append(fromBank).append("') ");
		}
		//海岛游的旧退款审核查询
		StringBuffer querySqlOldIsland = new StringBuffer();
		querySqlOldIslandCreate(querySqlOldIsland);
		if (hasPayType){//旧审批的退款付款moneyType = 2
			querySqlOldIsland.append(" AND EXISTS (SELECT 'x' FROM refund r WHERE r.record_id = r7.id AND r.moneyType = 2 ")
					.append(" AND r.orderType = 12 AND r.pay_type =").append(payMode).append(") ");
		}
		if (hasFromBank){
			querySqlOldIsland.append(" AND EXISTS (SELECT 'x' FROM refund r,pay_remittance p WHERE r.pay_type_Id = p.id AND r.record_id = r7.id ")
					.append(" AND r.orderType = 12 AND r.pay_type = 4 AND r.moneyType = 2 AND p.bank_name = '").append(fromBank).append("') ");
		}
		//退签证押金 SQL 新审核
		StringBuffer querySqlNewDeposite = new StringBuffer();
		querySqlNewDeposite.append(" select '2' as reviewflag,");
		// C460V3PLUS 只针对环球行，签证团号还原成订单表的团号
		if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())){
			querySqlNewDeposite.append("a.group_code groupcode,");
		}else{
			querySqlNewDeposite.append("v.groupCode groupcode,");
		}
		//退款签证押金Sql 新审核
		querySqlNewDepositeCreate(querySqlNewDeposite);
		
		if (hasPayType){
			querySqlNewDeposite.append(" AND EXISTS (SELECT 'x' FROM refund r WHERE r.record_id = a.id_long AND r.moneyType = 11 ")
					.append(" AND r.orderType = a.product_type AND r.pay_type =").append(payMode).append(") ");
		}
		if (hasFromBank){//取支付方式为汇款的来款银行。pay_type = 4； moneyType = 11 新审批退签证押金
			querySqlNewDeposite.append(" AND EXISTS (SELECT 'x' FROM refund r,pay_remittance p WHERE r.pay_type_Id = p.id AND r.record_id = a.id_long ")
					.append(" AND r.orderType = a.product_type AND r.pay_type = 4 AND r.moneyType = 11 AND p.bank_name = '").append(fromBank).append("') ");
		}
		//退签证押金 SQL 旧审核
		StringBuffer querySqlOldDeposite = new StringBuffer();
		querySqlOldDeposite.append(" SELECT '1' as reviewflag,");
		// C460V3PLUS 只针对环球行，签证团号还原成订单表的团号
		if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())){
			querySqlOldDeposite.append("a4.group_code AS groupcode,");
		}else{
			querySqlOldDeposite.append("b4.groupCode AS groupcode,");
		}
		querySqlOldDepositeCreateSql(querySqlOldDeposite);
		if (hasPayType){//旧审批的退签证押金moneyType = 5
			querySqlOldDeposite.append(" AND EXISTS (SELECT 'x' FROM refund r WHERE r.record_id = r4.id AND r.moneyType = 5 ")
					.append(" AND r.orderType = 6 AND r.pay_type =").append(payMode).append(") ");
		}
		if (hasFromBank){
			querySqlOldDeposite.append(" AND EXISTS (SELECT 'x' FROM refund r,pay_remittance p WHERE r.pay_type_Id = p.id AND r.record_id = r4.id ")
					.append(" AND r.orderType = 6 AND r.pay_type = 4 AND r.moneyType = 5 AND p.bank_name = '").append(fromBank).append("') ");
		}
		//根据查询类型 组装SQL
		Object prdType = params.get(Context.RefundPayParams.PRODUCT_TYPE);
		if(prdType == null || "".equals(prdType.toString().trim()) || "0".equals(prdType.toString().trim())){//所有产品
			querySql.append(querySqlNewPart).append(" union ").append(querySqlOldAirticket).
			append(" union ").append(querySqlOldVisa).append(" union ").append(querySqlOldSingle).
			append(" union ").append(querySqlNewDeposite).append(" union ").append(querySqlOldDeposite).
			append(" union ").append(querySqlOldHotel).append(" union ").append(querySqlOldIsland);
		} else if("7".equals(prdType.toString().trim())){//机票类
			querySql.append(querySqlNewPart).append(" union ").append(querySqlOldAirticket);
		} else if("6".equals(prdType.toString().trim())){//签证类
			querySql.append(querySqlNewPart).append(" union ").append(querySqlOldVisa).append(" union ").append(querySqlNewDeposite).append(" union ").append(querySqlOldDeposite);
		} else if("11".equals(prdType.toString().trim())){//酒店
			querySql.append(querySqlOldHotel);
		} else if("12".equals(prdType.toString().trim())){//海岛游
			querySql.append(querySqlOldIsland);
		} else {//单团类
			querySql.append(querySqlNewPart).append(" union ").append(querySqlOldSingle);
		}
		querySql.append(" ) s where 1=1 ");
		Object payRefundType = params.get(Context.RefundPayParams.PAY_REFUND_TYPE);
		String payType = payRefundType == null ? "" : payRefundType.toString().trim();
		if(!Context.BLANK_STR.equals(payType) && !Context.ZERO_DESC.equals(payType)){// 查询全部
			querySql.append(" and flowtype = " + payType + " ");
		}
		// 根据页面参数 设置不同的查询条件
		Object groupCode = params.get(Context.RefundPayParams.GROUP_CODE);
		if (groupCode != null && !"".equals(groupCode.toString().trim())) {// 团号选择
			querySql.append(" and groupcode like '%"
					+ groupCode.toString().trim() + "%' ");
		}
		
		if (prdType != null && !"".equals(prdType.toString().trim())&&!Context.ZERO_DESC.equals(prdType.toString().trim())) {// 团队类型选择 0代表全部
			querySql.append(" and prdtype = "
					+ Integer.parseInt(prdType.toString().trim()));
		} else if(UserUtils.getUser().getCompany().getName().contains(Context.OFFICE_EFX)){//俄风行 全部只有海岛游和酒店  modify by chy 2015年8月31日21:11:39
			querySql.append(" and prdtype in (11,12) ");
		}
		Object currencyid = params.get(Context.RefundPayParams.CURRENCY_ID);
		if (currencyid != null && !"".equals(currencyid.toString().trim())) {// 币种
			querySql.append(" and mcurid = "
					+ Integer.parseInt(currencyid.toString().trim()));
		}
		Object cAmountStart = params.get(Context.RefundPayParams.C_AMOUNT_START);
		if (cAmountStart != null && !"".equals(cAmountStart.toString().trim())) {// 钱范围
			querySql.append(" and mamount >= "
					+ Double.parseDouble(cAmountStart.toString().trim()));
		}
		Object cAmountEnd = params.get(Context.RefundPayParams.C_AMOUNT_END);
		if (cAmountEnd != null && !"".equals(cAmountEnd.toString().trim())) {// 钱范围
			querySql.append(" and mamount <= "
					+ Double.parseDouble(cAmountEnd.toString().trim()));
		}
		Object agentId = params.get(Context.RefundPayParams.AGENT_ID);
		if (agentId != null && !Context.ALL_VALUE.equals(agentId.toString().trim())) {// 渠道商选择
			querySql.append(" and agentid = " + Integer.parseInt(agentId.toString().trim()));
		}
		Object creator = params.get(Context.RefundPayParams.CREATOR_ID);
		if (creator != null && !Context.ALL_VALUE.equals(creator.toString().trim())) {// 下单人选择
			querySql.append(" and salecreateby = " + Integer.parseInt(creator.toString().trim()));
		}
		Object saler = params.get(Context.RefundPayParams.SALER_ID);
		if (saler != null && !Context.ALL_VALUE.equals(saler.toString().trim())) {// 销售选择
			querySql.append(" and salerId = " + Integer.parseInt(saler.toString().trim()));
		}
		Object jdsaler = params.get(Context.RefundPayParams.JD_ID);
		if (jdsaler != null && !Context.ALL_VALUE.equals(jdsaler.toString().trim())) {// 计调选择
			querySql.append(" and jidcreateby = " + Integer.parseInt(jdsaler.toString().trim()));
		}
		Object payStatus = params.get(Context.RefundPayParams.PAY_STATUS);
		if (payStatus != null && !"".equals(payStatus.toString().trim())) {// 支付状态选择
			if(Context.ZERO_DESC.equals(payStatus.toString().trim())){
				querySql.append(" and (payStatus is null or payStatus = '' or  payStatus= "
						+ Integer.parseInt(payStatus.toString().trim()) + ") ");
			} else {
				querySql.append(" and payStatus = " + Integer.parseInt(payStatus.toString().trim()));
			}
		}
		Object printStatus = params.get(Context.RefundPayParams.PRINT_STATUS);
		if (printStatus != null && !"".equals(printStatus.toString().trim())) {// 支付状态选择
			if(Context.ZERO_DESC.equals(printStatus.toString().trim())){
				querySql.append(" and (printFlag = " + Integer.parseInt(printStatus.toString().trim()) + " or printFlag is null)");
			} else {
				querySql.append(" and printFlag = " + Integer.parseInt(printStatus.toString().trim()));
			}
		}

		Object cashierConfirmDateBegin = params.get(Context.RefundPayParams.CASHIER_CONFIRM_DATE_BEGIN);
		if (cashierConfirmDateBegin != null && !"".equals(cashierConfirmDateBegin.toString().trim())){
			querySql.append(" AND payStatus = 1 ");//如果选择出纳确认时间，说明该退款已经确认支付。
			querySql.append(" AND payConfirmDate >= '").append(cashierConfirmDateBegin).append(" 00:00:00'");
		}

		Object cashierConfirmDateEnd = params.get(Context.RefundPayParams.CASHIER_CONFIRM_DATE_END);
		if (cashierConfirmDateEnd != null && !"".equals(cashierConfirmDateEnd.toString().trim())){
			querySql.append(" AND payConfirmDate <= '").append(cashierConfirmDateEnd).append(" 23:59:59'");
			querySql.append(" AND payStatus = 1 ");//如果选择出纳确认时间，说明该退款已经确认支付。
		}
		
		Object paymentType = params.get("paymentType");
		if (paymentType != null && !"".equals(paymentType.toString().trim())){
			querySql.append(" AND s.agentid in (select id from agentinfo where paymentType = "+paymentType+") ");//如果选择出纳确认时间，说明该退款已经确认支付。
		}
		//申请日期
		Object createTimeMin=params.get("createTimeMin");
		Object createTimeMax=params.get("createTimeMax");
		if(createTimeMin!=null && !"".equals(createTimeMin.toString())){
			querySql.append(" and createtime>='"+createTimeMin+" 00:00:00' ");
		}
		if(null!=createTimeMax && !"".equals(createTimeMax.toString())){
			querySql.append(" and createtime<='"+createTimeMax+" 23:59:59' ");
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
	
	/**
	 * 环球行新审批查询 sql
	 * @param querySqlNewPart
	 * @return
	 */
	public String querySqlNewPart_HQX(StringBuffer querySqlNewPart){
		querySqlNewPart.append("select '2' as reviewflag,a.group_code groupcode,a.group_id groupid,a.order_id orderid,a.id_long idlong,").
		append("a.order_no orderno,a.product_type prdtype,").
		append("a.process_type flowtype,a.agent agentid,a.order_creator salecreateby,a.operator jidcreateby,").
		append("a.product_id chanpid,a.product_name chanpname,a.id revid,a.company_id companyId,a.last_reviewer lastoperator,").
		append("a.create_date createtime,a.update_date updatetime,a.print_date printTime,").
		append("a.print_status printFlag,a.invoice_status invoiceStatus,a.traveller_id travelerid,c.currencyId mcurid,c.amount mamount,'' as refundname,a.pay_status payStatus,").
		append("a.saler salerId,a.saler_name salerName,c.exchangerate rate,a.pay_confirm_date payConfirmDate ").
		append(" from ").
		append("review_new a, money_amount c ").
		append("where " ).
		append(" a.id = c.review_uuid ").
		append(" and a.status = 2 and a.company_id = '").append(UserUtils.getUser().getCompany().getUuid())
		.append("' and a.process_type = '").append(Context.REVIEW_FLOWTYPE_REFUND).append("' AND a.del_flag = ")
		.append(Context.DEL_FLAG_NORMAL);
		return querySqlNewPart.toString();
	}
	
	/**
	 * 非环球行新审批查询部分 sql
	 * @param querySqlNewPart
	 * @return
	 */
	public String querySqlNewPartCreate_Not_HQX(StringBuffer querySqlNewPart){
		querySqlNewPart.append("select '2' as reviewflag,(select DISTINCT groupCode from visa_products p where p.id = a.product_id and a.product_type = ")
		.append(Context.ORDER_TYPE_QZ).append(" and a.process_type = '").append(Context.REVIEW_FLOWTYPE_REFUND).append("' ) as groupcode,")
		.append("a.group_id groupid,a.order_id orderid,a.id_long idlong,").
		append("a.order_no orderno,a.product_type prdtype,").
		append("a.process_type flowtype,a.agent agentid,a.order_creator salecreateby,a.operator jidcreateby,").
		append("a.product_id chanpid,a.product_name chanpname,a.id revid,a.company_id companyId,a.last_reviewer lastoperator,").
		append("a.create_date createtime,a.update_date updatetime,a.print_date printTime,").
		append("a.print_status printFlag,a.invoice_status invoiceStatus,a.traveller_id travelerid,c.currencyId mcurid,c.amount mamount,'' as refundname,a.pay_status payStatus,").
		append("a.saler salerId,a.saler_name salerName,c.exchangerate rate,a.pay_confirm_date payConfirmDate ").
		append(" from ").
		append("review_new a, money_amount c ").
		append("where " ).
		append(" a.id = c.review_uuid ").append("and a.product_type =  ").append(Context.ORDER_TYPE_QZ).
		append(" and a.status = 2 and a.company_id = '").append(UserUtils.getUser().getCompany().getUuid())
		.append("' and a.process_type = '").append(Context.REVIEW_FLOWTYPE_REFUND).append("' AND a.del_flag = ")
		.append(Context.DEL_FLAG_NORMAL);
		return querySqlNewPart.toString();
	}
	
	/**
	 * 机票、团期类退款新审批查询sql
	 * @param querySqlNewPart
	 * @return
	 */
	public String querySqlNewPartCreateSql_group_airticket(StringBuffer querySqlNewPart){
		querySqlNewPart.append("select '2' as reviewflag,a.group_code groupcode,a.group_id groupid,a.order_id orderid,a.id_long idlong,").
		append("a.order_no orderno,a.product_type prdtype,").
		append("a.process_type flowtype,a.agent agentid,a.order_creator salecreateby,a.operator jidcreateby,").
		append("a.product_id chanpid,a.product_name chanpname,a.id revid,a.company_id companyId,a.last_reviewer lastoperator,").
		append("a.create_date createtime,a.update_date updatetime,a.print_date printTime,").
		append("a.print_status printFlag,a.invoice_status invoiceStatus,a.traveller_id travelerid,c.currencyId mcurid,c.amount mamount,'' as refundname,a.pay_status payStatus,").
		append("a.saler salerId,a.saler_name salerName,c.exchangerate rate,a.pay_confirm_date payConfirmDate ").
		append(" from ").
		append("review_new a, money_amount c ").
		append("where " ).
		append(" a.id = c.review_uuid ").
		append("and a.product_type <>  ").
		append(Context.ORDER_TYPE_QZ).
		append(" and a.status = 2 and a.company_id = '").append(UserUtils.getUser().getCompany().getUuid())
		.append("' and a.process_type = '").append(Context.REVIEW_FLOWTYPE_REFUND).append("' AND a.del_flag = ")
		.append(Context.DEL_FLAG_NORMAL);
		return querySqlNewPart.toString();
	}
	
	/**
	 * 机票的旧退款审核查询sql
	 * @param querySqlOldAirticket
	 * @return
	 */
	public String querySqlOldAirticketSql(StringBuffer querySqlOldAirticket){
		querySqlOldAirticket.append("SELECT '1' as reviewflag,b1.group_code AS groupcode, ' ' AS groupid, a1.id AS orderid,'' AS idlong,").
		  append("a1.order_no AS orderno, r1.productType AS prdtype,").
		  append("r1.flowType AS flowtype, a1.agentinfo_id AS agentid,").
		  append("a1.create_by AS salecreateby, b1.createBy AS jidcreateby,").
		  append("b1.id AS chanpid, b1.activity_airticket_name AS chanpname, r1.id AS revid,").
		  append("r1.companyId AS companyId,r1.updateBy AS lastoperator, ").
		  append("r1.createDate AS createtime, r1.updateDate AS updatetime,").
		  append("r1.printTime AS printTime,").
		  append("r1.printFlag AS printFlag, r1.invoice_status AS invoiceStatus, r1.travelerId AS travelerid,c.currencyId mcurid,c.amount mamount,b.myValue refundname,").
		  append("r1.payStatus AS payStatus, a1.salerId AS salerId, a1.salerName AS salerName,c.exchangerate rate,r1.pay_confirm_date payConfirmDate ").
		  append("FROM review r1, airticket_order a1, activity_airticket b1,review_detail b , money_amount c ").
		  append("WHERE a1.id = r1.orderId AND a1.airticket_id = b1.id AND r1.id = b.review_id AND r1.orderId = a1.id ").
		  append(" AND r1.id = c.reviewId and b.myKey = 'refundName' and r1.companyId = " + UserUtils.getUser().getCompany().getId() + " ").
		  append(" AND r1.productType = 7 and r1.flowType = 1 and r1.status in (2,3)");
		return querySqlOldAirticket.toString();
	}
	
	/**
	 *新审批的退款付款sql
	 * @param querySqlNewDeposite
	 * @return
	 */
	public String querySqlNewDepositeCreate(StringBuffer querySqlNewDeposite){
		querySqlNewDeposite.append("a.group_id groupid,").
		  append("a.order_id orderid,a.id_long idlong,a.order_no orderno,a.product_type prdtype,").
		  append("a.process_type flowtype,a.agent agentid,a.order_creator salecreateby,a.operator jidcreateby,").
		  append("a.product_id chanpid,a.product_name chanpname,a.id revid,a.company_id companyId,a.last_reviewer lastoperator,").
		  append("a.create_date createtime,a.update_date updatetime,a.print_date printTime,").
		  append("a.print_status printFlag, a.invoice_status invoiceStatus,a.traveller_id travelerid,c.currencyId mcurid,c.amount mamount,'' as refundname,a.pay_status payStatus,").
		  append("a.saler salerId,a.saler_name salerName,c.exchangerate rate,a.pay_confirm_date payConfirmDate ").
		  append(" from ").
		  append("review_new a, money_amount c,visa_products v  ").
		  append("where ").
		  append(" a.id = c.review_uuid ").
		  append(" and a.product_id = v.id ").
		  append("and a.status = 2 and a.company_id = '" + UserUtils.getUser().getCompany().getUuid() + "' and a.process_type = '" + Context.REVIEW_FLOWTYPE_DEPOSITE_REFUND + "' ");
		return querySqlNewDeposite.toString();
	}
	
	/**
	 * 旧审批的退款付款sql
	 * @param querySqlOldVisa
	 * @return
	 */
	public String querySqlOldVisaCreate(StringBuffer querySqlOldVisa){
		querySqlOldVisa.append(" ' ' AS groupid, a4.id AS orderid,'' AS idlong,").
		  append("a4.order_no AS orderno, r4.productType AS prdtype,").
		  append("r4.flowType AS flowtype, ").
		  append("a4.agentinfo_id AS agentid,a4.create_by AS salecreateby,").
		  append("b4.createBy AS jidcreateby, ").
		  append("b4.id AS chanpid, b4.productCode AS chanpname, r4.id AS revid, r4.companyId AS companyId,").
		  append("r4.updateBy AS lastoperator, ").
		  append("r4.createDate AS createtime, r4.updateDate AS updatetime,").
		  append(" r4.printTime AS printTime,").
		  append("r4.printFlag AS printFlag, r4.invoice_status AS invoiceStatus, r4.travelerId AS travelerid,c.currencyId mcurid,c.amount mamount,b.myValue refundname,").
		  append("r4.payStatus AS payStatus, a4.salerId AS salerId,").
		  append("a4.salerName AS salerName,c.exchangerate rate,r4.pay_confirm_date payConfirmDate ").
		  append("FROM review r4, visa_order a4, visa_products b4,review_detail b , money_amount c ").
		  append("WHERE r4.orderId = a4.id AND r4.id = b.review_id AND r4.id = c.reviewId and b.myKey = 'refundName' and r4.companyId = " + UserUtils.getUser().getCompany().getId() + " ").
		  append("AND a4.visa_product_id = b4.id AND r4.productType = 6 and r4.status in(2,3) and r4.flowType = 1 ");
		return querySqlOldVisa.toString();
	}
	
	/**
	 * 单团类的旧退款审核查询sql
	 * @param querySqlOldSingle
	 * @return
	 */
	public String querySqlOldSingleCreate(StringBuffer querySqlOldSingle){
		querySqlOldSingle.append("SELECT '1' as reviewflag,g5.groupCode AS groupcode, g5.id AS groupid, a5.id AS orderid,'' AS idlong, a5.orderNum AS orderno,").
		  append(" r5.productType AS prdtype, r5.flowType AS flowtype,").
		  append("a5.orderCompany AS agentid,  a5.createBy AS salecreateby, b5.createBy AS jidcreateby,").
		  append("b5.id AS chanpid, b5.activitySerNum AS chanpname, r5.id AS revid,r5.companyId AS companyId,r5.updateBy AS lastoperator,").
		  append("r5.createDate AS createtime, r5.updateDate AS updatetime,").
		  append(" r5.printTime AS printTime,").
		  append("r5.printFlag AS printFlag, r5.invoice_status AS invoiceStatus, r5.travelerId AS travelerid, c.currencyId mcurid,c.amount mamount,b.myValue refundname, r5.payStatus AS payStatus,").
		  append("a5.salerId AS salerId, a5.salerName AS salerName,c.exchangerate rate,r5.pay_confirm_date payConfirmDate ").
		  append("FROM review r5,productorder a5,travelactivity b5,activitygroup g5,review_detail b , money_amount c ").
		  append("WHERE r5.id = b.review_id AND r5.id = c.reviewId and b.myKey = 'refundName' ").
		  append(" AND r5.orderId = a5.id AND a5.productId = b5.id").
		  append(" AND g5.id = a5.productGroupId and r5.companyId = " + UserUtils.getUser().getCompany().getId() + " ").
		  append(" AND r5.productType IN (1, 2, 3, 4, 5, 10) and r5.flowType = 1 ").
		  append("");
		return querySqlOldSingle.toString();
	}
	
	/**
	 * 退签证押金 SQL 旧审核
	 * @param querySqlOldDeposite
	 * @return
	 */
	public String querySqlOldDepositeCreateSql(StringBuffer querySqlOldDeposite){
		querySqlOldDeposite.append("' ' AS groupid, a4.id AS orderid,'' AS idlong,").
		  append("a4.order_no AS orderno, r4.productType AS prdtype,").
		  append("r4.flowType AS flowtype, ").
		  append("a4.agentinfo_id AS agentid, a4.create_by AS salecreateby,").
		  append("b4.createBy AS jidcreateby, ").
		  append("b4.id AS chanpid, b4.productCode AS chanpname, r4.id AS revid,").
		  append("r4.companyId AS companyId, r4.updateBy AS lastoperator,").
		  append("r4.createDate AS createtime, r4.updateDate AS updatetime,").
		  append("r4.printTime AS printTime,").
		  append("r4.printFlag AS printFlag, r4.invoice_status AS invoiceStatus, r4.travelerId AS travelerid,c.currencyId mcurid,c.amount mamount,b.myValue refundname,").
		  append("r4.payStatus AS payStatus, a4.salerId AS salerId,").
		  append("a4.salerName AS salerName,c.exchangerate rate,r4.pay_confirm_date payConfirmDate ").
		  append(" FROM review r4,visa_order a4,visa_products b4,review_detail b , money_amount c ").
		  append(" WHERE r4.orderId = a4.id AND r4.id = b.review_id AND r4.id = c.reviewId and b.myKey = 'remark' and r4.companyId = " + UserUtils.getUser().getCompany().getId() + " ").
		  append(" AND a4.visa_product_id = b4.id AND r4.productType = 6 and r4.status in(2,3) and r4.flowType = 7 ");
		return querySqlOldDeposite.toString();
	}
	
	/**
	 * 酒店的旧退款审核查询 sql
	 * @param querySqlOldHotel
	 * @return
	 */
	public String querySqlOldHotelCreateSql(StringBuffer querySqlOldHotel){
		querySqlOldHotel.append(" SELECT '1' as reviewflag, ").
		append("g6.groupCode AS groupcode, g6.uuid AS groupid, a6.id AS orderid,'' AS idlong, a6.orderNum AS orderno, ").
		append("r6.productType AS prdtype, r6.flowType AS flowtype, ").
		append("a6.orderCompany AS agentid, a6.createBy AS salecreateby, b6.createBy AS jidcreateby, ").
		append("b6.uuid AS chanpid, b6.activityName AS chanpname, r6.id AS revid, r6.companyId AS companyId, r6.updateBy AS lastoperator, ").
		append("r6.createDate AS createtime, r6.updateDate AS updatetime, ").
		append(" r6.printTime AS printTime,  ").
		append(" r6.printFlag AS printFlag, r6.invoice_status AS invoiceStatus, r6.travelerId AS travelerid,c.currencyId mcurid,c.amount mamount,b.myValue refundname,  r6.payStatus AS payStatus, ").
		append(" a6.salerId AS salerId, a6.salerName AS salerName,c.exchangerate rate,r6.pay_confirm_date payConfirmDate ").
		append("FROM review r6, hotel_order a6, activity_hotel b6, activity_hotel_group g6,review_detail b , hotel_money_amount c  ").
		append("WHERE r6.orderId = a6.id AND a6.activity_hotel_uuid = b6.uuid and r6.id = b.review_id and r6.id = c.reviewId and b.myKey = 'refundName' ").
		append("AND g6.uuid = a6.activity_hotel_group_uuid AND r6.productType = 11 and r6.flowType = 1 and r6.companyId = " + UserUtils.getUser().getCompany().getId() + " ");
		return querySqlOldHotel.toString();
	}
	
	/**
	 * 海岛游的旧退款审核查询 sql
	 * @param querySqlOldIsland
	 * @return
	 */
	public String querySqlOldIslandCreate(StringBuffer querySqlOldIsland){
		querySqlOldIsland.append(" SELECT '1' as reviewflag, ").
		append(" g7.groupCode AS groupcode, g7.uuid AS groupid, a7.id AS orderid,'' AS idlong, a7.orderNum AS orderno, ").
		append(" r7.productType AS prdtype, r7.flowType AS flowtype, ").
		append(" a7.orderCompany AS agentid, a7.createBy AS salecreateby, b7.createBy AS jidcreateby, ").
		append(" b7.uuid AS chanpid, b7.activityName AS chanpname, r7.id AS revid, r7.companyId AS companyId, ").
		append(" r7.updateBy AS lastoperator, r7.createDate AS createtime, r7.updateDate AS updatetime, ").
		append(" r7.printTime AS printTime, ").
		append(" r7.printFlag AS printFlag, r7.invoice_status AS invoiceStatus, r7.travelerId AS travelerid, c.currencyId mcurid,c.amount mamount,b.myValue refundname,").
		append(" r7.payStatus AS payStatus, a7.salerId AS salerId, a7.salerName AS salerName,c.exchangerate rate,r7.pay_confirm_date payConfirmDate ").
		append(" FROM review r7, island_order a7, activity_island b7, activity_island_group g7,review_detail b , island_money_amount c ").
		append(" WHERE r7.orderId = a7.id AND a7.activity_island_uuid = b7.uuid AND g7.uuid = a7.activity_island_group_uuid and r7.id = b.review_id and r7.id = c.reviewId and b.myKey = 'refundName'  ").
		append(" AND r7.productType = 12 and r7.flowType = 1 and r7.companyId = " + UserUtils.getUser().getCompany().getId() + " ");
		return querySqlOldIsland.toString();
	}
	
	
	@Override
	public Page<Map<String, Object>> findBorrowMoneyListTTSQZ(
			Map<String, String> params, Page<Map<String, Object>> page) {
		StringBuffer str = new StringBuffer();
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		str.append(" SELECT													")
		   .append(" 	batch_id,                                           ")
		   .append(" 	batch_no,                                           ")
		   .append(" 	proposer,                                           ")
		   .append(" 	create_time,                                        ")
		   .append(" 	batch_total_money,                                  ")
		   .append(" 	batch_person_count,                                 ")
		   .append(" 	price,                                              ")
		   .append(" 	payStatus,                                          ")
		   .append(" 	printTime,                                          ")
		   .append(" 	isPrintFlag,                                        ")
		   .append(" 	update_time,                                        ")
		   .append(" 	productId,                                         	")
		   .append(" 	orderId,                                            ")
		   .append(" 	reviewId,                                           ")
		   .append(" 	reviewflag,                                         ")
		   .append(" 	review_uuid                                         ")
		   .append(" FROM                                                   ")
		   .append(" 	(                                                   ")
		   .append(" 		SELECT                                          ")
		   .append(" 			b.id AS batch_id,                           ")
		   .append(" 			b.batch_no,                                 ")
		   .append(" 			r.create_by AS proposer,                    ")
		   .append(" 			r.create_date AS create_time,               ")
		   .append(" 			b.batch_total_money,                        ")
		   .append(" 			b.batch_person_count,                       ")
		   .append(" 			CASE                                        ")
		   .append(" 		WHEN b.batch_person_count = 0 THEN              ")
		   .append(" 			'0.00'                                      ")
		   .append(" 		ELSE                                            ")
		   .append(" 			b.batch_total_money / b.batch_person_count  ")
		   .append(" 		END AS price,                                   ")
		   .append(" 		r.pay_status AS payStatus,                      ")
		   .append(" 		r.print_date AS printTime,                      ")
		   .append(" 		r.print_status AS isPrintFlag,                  ")
		   .append(" 		r.update_date AS update_time,                   ")
		   .append(" 		p.id AS productId,                    			")
		   .append(" 		o.id AS orderId,                    			")
		   .append(" 		r.id_long as reviewId,							")
		   .append(" 		2 as reviewflag,          						")
		   .append(" 		r.id as review_uuid         					")
		   .append(" 	FROM                                                ")
		   .append(" 		review_new r,                                   ")
		   .append(" 		visa_flow_batch_opration b,                     ")
		   .append(" 		visa_order o,                                   ")
		   .append(" 		visa_products p                                 ")
		   .append(" 	WHERE                                               ")
		   .append(" 		r.batch_no = b.batch_no                         ")
		   .append(" 	AND b.busyness_type = ").append(BUSYNESS_TYPE_2)
		   .append(" 	AND r.`status` = ").append(ReviewConstant.REVIEW_STATUS_PASSED)
		   .append(" 	AND r.company_id = '").append(currentCompanyUuid).append("' ")
		   .append(" 	AND r.order_id = o.id                               ")
		   .append(" 	AND p.id = o.visa_product_id                        ")
		   .append(" 	AND r.process_type = '").append(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY + "' ")
		   .append(" 	AND p.proCompanyId =").append(currentCompanyId);
		if (StringUtils.isNotBlank(params.get("groupCode"))) {
			str.append(" and o.group_code like '%").append(params.get("groupCode"))
			   .append("%'");
		}
		if (StringUtils.isNotBlank(params.get("visaType"))) {
			str.append(" and p.visaType = ").append(params.get("visaType"));
		}
		if (StringUtils.isNotBlank(params.get("payStatus"))) {
			str.append(" and r.pay_status = ").append(params.get("payStatus"));
		}
		if (StringUtils.isNotBlank(params.get("reportDateStart"))) {
			str.append(" and r.create_date >= '")
			   .append(params.get("reportDateStart")).append("' ");
		}
		if (StringUtils.isNotBlank(params.get("reportDateEnd"))) {
			str.append(" and r.create_date <= '")
			   .append(params.get("reportDateEnd"))
			   .append(" 23:59:59' ");
		}
		if (StringUtils.isNotBlank(params.get("visaContry"))) {
			str.append(" and p.sysCountryId = ").append(params.get("visaContry"));
		}
		if (StringUtils.isNotBlank(params.get("salerId"))) {
			str.append(" and o.salerId = ").append(params.get("salerId"));
		}
		if (StringUtils.isNotBlank(params.get("operatorId"))) {
			str.append(" and p.createBy = ").append(params.get("operatorId"));
		}
		if (StringUtils.isNotBlank(params.get("moneyBegin"))) {
			str.append(" and b.batch_total_money >= ").append(params.get("moneyBegin"));
		}
		if (StringUtils.isNotBlank(params.get("moneyEnd"))) {
			str.append(" and b.batch_total_money <= ").append(params.get("moneyEnd"));
		}
		if ("0".equals(params.get("printStatus"))) {
			str.append(" and r.print_status = ").append(params.get("printStatus"));
		} else if ("1".equals(params.get("printStatus"))) {
			str.append(" and r.print_status = ").append(params.get("printStatus"));
		}
		if (StringUtils.isNotBlank(params.get("createTimeMin"))) {
			str.append(" and r.create_date >= '").append(params.get("createTimeMin")).append("' ");
		}
		if (StringUtils.isNotBlank(params.get("createTimeMax"))) {
			str.append(" and r.create_date <= '").append(params.get("createTimeMax"))
			   .append(" 23:59:59' ");
		}
		str.append(" GROUP BY b.batch_no ")
		   .append(" union ")
		   .append(" SELECT b.id as batch_id,b.batch_no,r.createBy as proposer,")
		   .append(" r.createDate as create_time, b.batch_total_money,b.batch_person_count,")
		   .append(" case when b.batch_person_count = 0 then '0.00'")
		   .append(" else b.batch_total_money/b.batch_person_count")
		   .append(" end as price, r.payStatus, r.printTime, r.printFlag as isPrintFlag,")
		   .append(" r.updateDate as update_time, p.id AS productId, o.id AS orderId,")
		   .append(" r.id as reviewId, 1 as reviewflag, ' ' as review_uuid ")
		   .append(" FROM review r,review_detail d,visa_order o,")
		   .append(" visa_products p,visa_flow_batch_opration b ")
		   .append(" WHERE r.id = d.review_id AND d.myKey = 'visaBorrowMoneyBatchNo'")
		   .append(" AND r.orderId = o.id AND p.id = o.visa_product_id")
		   .append(" AND b.batch_no = d.myValue")
		   .append(" AND p.proCompanyId = ").append(currentCompanyId)
		   .append(" AND r.companyId = ").append(currentCompanyId)
		   .append(" AND b.busyness_type = ").append(BUSYNESS_TYPE_2)
		   .append(" AND r.flowType = 5 AND r.status in (2, 3)");
		if (StringUtils.isNotBlank(params.get("groupCode"))) {
			str.append(" and o.group_code like '%").append(params.get("groupCode"))
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
			   .append(params.get("reportDateStart")).append("' ");
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
			str.append(" and o.salerId = ").append(params.get("salerId"));
		}
		if (StringUtils.isNotBlank(params.get("operatorId"))) {
			str.append(" and p.createBy = ").append(params.get("operatorId"));
		}
		if (StringUtils.isNotBlank(params.get("moneyBegin"))) {
			str.append(" and b.batch_total_money >= ").append(params.get("moneyBegin"));
		}
		if (StringUtils.isNotBlank(params.get("moneyEnd"))) {
			str.append(" and b.batch_total_money <= ").append(params.get("moneyEnd"));
		}
		if ("0".equals(params.get("printStatus"))) {
			str.append(" and (r.printFlag = ").append(params.get("printStatus")).append(" or r.printFlag is null)");
		} else if ("1".equals(params.get("printStatus"))) {
			str.append(" and r.printFlag = ").append(params.get("printStatus"));
		}
		if (StringUtils.isNotBlank(params.get("createTimeMin"))) {
			str.append(" and r.createDate >= '")
					.append(params.get("createTimeMin")).append("' ");
		}
		if (StringUtils.isNotBlank(params.get("createTimeMax"))) {
			str.append(" and r.createDate <= '")
					.append(params.get("createTimeMax"))
					.append(" 23:59:59' ");
		}
		str.append(" GROUP BY b.batch_no ) t");
		String orderBy = page.getOrderBy();
		if(StringUtils.isBlank(orderBy)){
			page.setOrderBy("update_time DESC");
		}
		return findBySql(page, str.toString(), Map.class);
	}

	@Override
	public Page<Map<String, Object>> findBorrowMoneyListAll(
			Map<String, String> params, Page<Map<String, Object>> page) {
		StringBuffer str = new StringBuffer();
		String where = borrowMoneyWhere(params);
		String newMoneyWhere = getMoneyWhere(params," r.product_type ");
		String oldMoneyWhere = getMoneyWhere(params," r.productType ");
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		//新审批借款付款SQL
		str.append(" SELECT tt.currencyId, tt.amount, tt.reviewId, tt.orderId, tt.createDate, tt.updateDate, ")
		   .append(" tt.groupCode, tt.operatorId, tt.productId, tt.orderType, tt.payStatus, tt.travelerId, ")
		   .append(" tt.agentId,tt.chanpname, tt.groupId, tt.isShowPrint, tt.printFlag, tt.printTime, tt.id_long, tt.reviewflag FROM ")
		   .append(" (SELECT t.currencyId, t.amount, t.reviewId, t.orderId, t.createDate, t.updateDate,")
		   .append(" t.groupCode, t.operatorId, t.productId, t.orderType, t.payStatus, t.travelerId, ")
		   .append(" t.agentId, t.chanpname, t.groupId,t.isShowPrint,t.printFlag,t.printTime, t.id_long, 2 as reviewflag FROM ")
		   .append(" ( SELECT m.currencyId, m.amount, r.id AS reviewId, r.order_id as orderId, r.create_date ")
		   .append(" as createDate, r.update_date as updateDate, g.groupCode, g.createBy AS operatorId,")
		   .append(" o.productId AS productId, m.orderType, r.pay_status as payStatus, ")
		   .append(" r.traveller_id as travelerId, o.orderCompany AS agentId, ' ' AS chanpname, ")
		   .append(" g.id AS groupId, r.status as isShowPrint,r.print_status as printFlag,")
		   .append(" r.print_date as printTime, r.id_long FROM review_new r ")
		   .append(" LEFT JOIN money_amount m ON r.product_type = m.orderType AND r.order_id = m.uid ")
		   .append(" AND m.review_uuid = r.id and r.product_type in (1,2,3,4,5,10), ")
		   .append(" productorder o, activitygroup g WHERE o.id = r.order_id AND g.id = o.productGroupId ")
		   .append(" AND m.moneyType = ").append(Context.MONEY_TYPE_JK)
		   .append(" AND r.status = ").append(ReviewConstant.REVIEW_STATUS_PASSED)
		   .append(" AND r.company_id = '").append(currentCompanyUuid).append("' AND m.businessType = 1 ")
		   .append(newMoneyWhere).append(" group by r.id ) t ")
		   .append(" UNION ")
		   .append(" SELECT t.currencyId, t.amount, t.reviewId, t.orderId, t.createDate, t.updateDate, t.groupCode,")
		   .append(" t.operatorId, t.productId, t.orderType, t.payStatus, t.travelerId, t.agentId, t.chanpname, ")
		   .append(" groupId, t.isShowPrint, t.printFlag, t.printTime, t.id_long, 2 as reviewflag FROM ")
		   .append(" (SELECT m.currencyId, m.amount, r.id AS reviewId, r.order_id as orderId, r.create_date ")
		   .append(" as createDate, r.update_date as updateDate, p.groupCode, p.createBy AS operatorId,")
		   .append(" p.id AS productId, m.orderType, r.pay_status as payStatus, ")
		   .append(" r.traveller_id as travelerId, o.agentinfo_id AS agentId, ' ' AS chanpname,")
		   .append(" -1 AS groupId, r.status as isShowPrint,r.print_status as printFlag, ")
		   .append(" r.print_date as printTime, r.id_long FROM review_new r ")
		   .append(" LEFT JOIN money_amount m ON r.product_type = m.orderType ")
		   .append(" AND r.product_type = ").append(Context.ORDER_TYPE_QZ)
		   .append(" AND r.order_id = m.uid AND r.id = m.review_uuid, visa_order o, visa_products p WHERE o.id = r.order_id ")
		   .append(" AND p.id = o.visa_product_id AND m.businessType = 1 AND r.status = ")
		   .append(ReviewConstant.REVIEW_STATUS_PASSED).append(" AND m.moneyType = ").append(Context.MONEY_TYPE_JK)
		   .append(" AND p.proCompanyId = ").append(currentCompanyId)
		   .append(" AND r.company_id = '").append(currentCompanyUuid).append("' ")
		   .append(newMoneyWhere).append(" group by r.id ) t ")
	       .append(" UNION ")
		   .append(" SELECT t.currencyId, t.amount, t.reviewId, t.orderId, t.createDate, t.updateDate,")
		   .append("  t.groupCode, t.operatorId, t.productId, t.orderType, t.payStatus, t.travelerId, t.agentId,")
		   .append("  t.chanpname, groupId, t.isShowPrint,t.printFlag,t.printTime, t.id_long, 2 as reviewflag FROM ")
		   .append(" ( SELECT m.currencyId, m.amount, r.id AS reviewId, r.order_id as orderId, r.create_date ")
		   .append(" as createDate, r.update_date as updateDate, p.group_code as groupCode, p.createBy AS operatorId,")
		   .append(" p.id AS productId, m.orderType, r.pay_status as payStatus, r.traveller_id as travelerId, ")
		   .append(" o.agentinfo_id AS agentId, ' ' AS chanpname, -1 as groupId, r.status as isShowPrint,")
		   .append(" r.print_status as printFlag, r.print_date as printTime, r.id_long FROM review_new r")
		   .append(" LEFT JOIN money_amount m ")
		   .append(" ON r.product_type = m.orderType AND r.order_id = m.uid AND m.review_uuid = r.id ")
		   .append(" and r.product_type = ").append(Context.ORDER_TYPE_JP)
		   .append(" , airticket_order o, activity_airticket p WHERE o.id = r.order_id AND p.id = o.airticket_id ")
		   .append(" AND m.businessType = 1 AND r.STATUS = ").append(ReviewConstant.REVIEW_STATUS_PASSED)
		   .append(" AND m.moneyType = ").append(Context.MONEY_TYPE_JK)
		   .append(" AND p.proCompany = ").append(currentCompanyId)
		   .append(" AND r.company_id = '").append(currentCompanyUuid).append("' ")
		   .append(newMoneyWhere).append(" group by r.id ) t ")
	       .append(" UNION ")
		   .append(" SELECT t.currencyId, t.amount, t.reviewId, t.orderId, t.createDate, t.updateDate,")
		   .append(" t.groupCode, t.operatorId, t.productId, t.orderType, t.payStatus, t.travelerId, t.agentId, ")
		   .append(" t.chanpname, t.groupId,t.isShowPrint,t.printFlag,t.printTime, t.id_long, 2 as reviewflag FROM ")
		   .append(" ( SELECT m.currencyId, m.amount, r.id AS reviewId, r.order_id as orderId, ")
		   .append(" r.create_date as createDate, r.update_date as updateDate, g.groupCode, g.createBy  ")
		   .append(" AS operatorId, p.uuid AS productId, ").append(Context.ORDER_TYPE_ISLAND)
		   .append(" AS orderType, r.pay_status as payStatus, r.traveller_id as travelerId,")
		   .append(" o.orderCompany AS agentId, p.activityName AS chanpname, g.uuid AS groupId,")
		   .append(" r.status as isShowPrint,r.print_status as printFlag, r.print_date as printTime, r.id_long ")
		   .append(" FROM review_new r LEFT JOIN island_money_amount m ")
		   .append(" ON r.product_type = ").append(Context.ORDER_TYPE_ISLAND)
		   .append(" AND m.review_uuid = r.id, island_order o, activity_island_group g,activity_island p ")
		   .append(" WHERE o.id = r.order_id AND p.uuid = o.activity_island_uuid AND g.uuid = o.activity_island_group_uuid ")
		   .append(" AND m.moneyType = ").append(Context.MONEY_TYPE_JK).append(" AND m.businessType = 1 ")
		   .append(" AND r.status = ").append(ReviewConstant.REVIEW_STATUS_PASSED)
		   .append(" AND r.company_id = '").append(currentCompanyUuid).append("' ")
	       .append(" AND p.wholesaler_id = ").append(currentCompanyId)
		   .append(newMoneyWhere).append(" group by r.id ) t ")
		   .append(" UNION ")
		   .append(" SELECT t.currencyId, t.amount, t.reviewId, t.orderId, t.createDate, t.updateDate,")
		   .append(" t.groupCode, t.operatorId, t.productId, t.orderType, t.payStatus, t.travelerId,")
		   .append(" t.agentId, t.chanpname, t.groupId,t.isShowPrint,t.printFlag,t.printTime, t.id_long, 2 as reviewflag FROM ")
		   .append(" ( SELECT m.currencyId, m.amount, r.id AS reviewId, r.order_id as orderId,")
		   .append(" r.create_date as createDate, r.update_date as updateDate, g.groupCode,")
		   .append(" g.createBy AS operatorId, p.uuid AS productId, ").append(Context.ORDER_TYPE_HOTEL)
		   .append(" as orderType, r.pay_status as payStatus, r.traveller_id as travelerId, ")
		   .append(" o.orderCompany AS agentId, p.activityName AS chanpname, g.uuid AS groupId,")
		   .append(" r.status as isShowPrint, r.print_status as printFlag, r.print_date as printTime, r.id_long ")
		   .append(" FROM review_new r LEFT JOIN hotel_money_amount m ON r.product_type = ")
		   .append(Context.ORDER_TYPE_HOTEL).append(" AND m.review_uuid = r.id, ")
		   .append(" hotel_order o, activity_hotel_group g, activity_hotel p ")
		   .append(" WHERE o.id = r.order_id AND p.uuid = o.activity_hotel_uuid AND g.uuid = o.activity_hotel_group_uuid ")
		   .append(" AND m.moneyType = ").append(Context.MONEY_TYPE_JK).append(" AND m.businessType = 1 ")
		   .append(" AND r.status = ").append(ReviewConstant.REVIEW_STATUS_PASSED)
		   .append(" AND r.company_id = '").append(currentCompanyUuid).append("' ")
	       .append(" AND p.wholesaler_id = ").append(currentCompanyId)
		   .append(newMoneyWhere).append(" group by r.id ) t ");
		// 旧借款付款查询
		str.append(" UNION ")
		   .append(" SELECT t.currencyId, t.amount, t.reviewId, t.orderId, t.createDate, t.updateDate,")
		   .append(" t.groupCode, t.operatorId, t.productId, t.orderType, t.payStatus, t.travelerId, t.agentId,")
		   .append(" t.chanpname, t.groupId,t.isShowPrint,t.printFlag,t.printTime, t.id_long, 1 as reviewflag FROM ")
		   .append(" (SELECT m.currencyId, m.amount, r.id AS reviewId, r.orderId, r.createDate,")
		   .append(" r.updateDate, g.groupCode, g.createBy AS operatorId, o.productId , m.orderType,")
		   .append(" r.payStatus, r.travelerId, o.orderCompany AS agentId, ' ' AS chanpname, g.id AS groupId, ")
		   .append(" r.status as isShowPrint, r.printFlag, r.printTime, -1 as id_long FROM review r ")
		   .append(" LEFT JOIN money_amount m ON r.productType = orderType AND r.orderId = m.uid ")
		   .append(" AND m.reviewId = r.id and r.productType in (1,2,3,4,5,10),")
		   .append(" productorder o, activitygroup g WHERE o.id = r.orderId AND g.id = o.productGroupId ")
		   .append(" AND m.moneyType = ").append(Context.MONEY_TYPE_JK).append(" AND m.businessType = 1 ")
		   .append(" AND r.STATUS in (2,3) AND r.companyId = ").append(currentCompanyId)
		   .append(oldMoneyWhere).append(" group by r.id ) t ")
		   .append(" UNION ")
		   .append(" SELECT t.currencyId, t.amount, t.reviewId, t.orderId, t.createDate, t.updateDate,")
		   .append(" t.groupCode, t.operatorId, t.productId, t.orderType, t.payStatus, t.travelerId, ")
		   .append(" t.agentId, t.chanpname, t.groupId, t.isShowPrint,t.printFlag,t.printTime, t.id_long, 1 as reviewflag FROM ")
		   .append(" (SELECT m.currencyId, m.amount, r.id AS reviewId, r.orderId, r.createDate, r.updateDate,")
		   .append(" p.groupCode, p.createBy AS operatorId, p.id AS productId, m.orderType, r.payStatus, r.travelerId,")
		   .append(" o.agentinfo_id AS agentId, ' ' AS chanpname, -1 as groupId, r.status as isShowPrint,")
		   .append(" r.printFlag,r.printTime, -1 as id_long FROM ")
		   .append(" review r LEFT JOIN money_amount m ON r.productType = orderType ")
		   .append(" AND r.productType = ").append(Context.ORDER_TYPE_QZ)
		   .append(" AND r.orderId = m.uid AND r.id = m.reviewId, visa_order o, ")
		   .append(" visa_products p WHERE o.id = r.orderId AND p.id = o.visa_product_id AND m.businessType = 1 ")
		   .append(" AND r.STATUS in (2,3) AND p.productStatus = 2 AND r.companyId = p.proCompanyId ")
		   .append(" AND m.moneyType = ").append(Context.MONEY_TYPE_JK)
		   .append(" AND p.proCompanyId = ").append(currentCompanyId)
		   .append(oldMoneyWhere).append(" group by r.id ) t ")
		   .append(" UNION ")
		   .append(" SELECT t.currencyId, t.amount, t.reviewId, t.orderId, t.createDate, t.updateDate,")
		   .append(" t.groupCode, t.operatorId, t.productId, t.orderType, t.payStatus, t.travelerId, ")
		   .append(" t.agentId, t.chanpname, t.groupId, t.isShowPrint, t.printFlag, t.printTime, t.id_long, 1 as reviewflag FROM ")
		   .append(" (SELECT m.currencyId, m.amount, r.id AS reviewId, r.orderId, r.createDate, r.updateDate,")
		   .append(" p.group_code AS groupCode, p.createBy AS operatorId, p.id AS productId, m.orderType, r.payStatus,")
		   .append(" r.travelerId, o.agentinfo_id AS agentId, ' ' AS chanpname, -1 as groupId, ")
		   .append(" r.status as isShowPrint, r.printFlag, r.printTime, -1 as id_long FROM ")
		   .append(" review r LEFT JOIN money_amount m ON r.productType = orderType AND r.orderId = m.uid ")
		   .append(" AND m.reviewId = r.id and r.productType = ").append(Context.ORDER_TYPE_JP)
		   .append(" , airticket_order o, activity_airticket p WHERE o.id = r.orderId ")
		   .append(" AND p.id = o.airticket_id AND m.businessType = 1 AND r.STATUS in (2,3) AND p.productStatus=2 ")
		   .append(" AND r.companyId = p.proCompany AND m.moneyType = ").append(Context.MONEY_TYPE_JK)
		   .append(" AND p.proCompany = ").append(currentCompanyId)
		   .append(oldMoneyWhere).append(" group by r.id ) t ")
		   .append(" UNION ")
		   .append(" SELECT t.currencyId, t.amount, t.reviewId, t.orderId, t.createDate, t.updateDate,")
		   .append(" t.groupCode, t.operatorId, t.productId, t.orderType, t.payStatus, t.travelerId, ")
		   .append(" t.agentId,t.chanpname, t.groupId, t.isShowPrint, t.printFlag, t.printTime, t.id_long, 1 as reviewflag FROM ")
		   .append(" (SELECT m.currencyId, m.amount, r.id AS reviewId, r.orderId, r.createDate,")
		   .append(" r.updateDate, g.groupCode, g.createBy AS operatorId, ai.uuid AS productId, ")
		   .append(Context.ORDER_TYPE_ISLAND).append(" as orderType, r.payStatus, r.travelerId,")
		   .append(" o.orderCompany AS agentId, ai.activityName AS chanpname, g.uuid AS groupId, ")
		   .append(" r.status as isShowPrint, r.printFlag,r.printTime, -1 as id_long FROM review r ")
		   .append(" LEFT JOIN island_money_amount m ON r.productType =").append(Context.ORDER_TYPE_ISLAND)
		   .append(" AND m.reviewId = r.id, island_order o, activity_island_group g,activity_island ai ")
		   .append(" WHERE o.id = r.orderId AND ai.uuid = o.activity_island_uuid AND g.uuid = o.activity_island_group_uuid ")
		   .append(" AND m.moneyType = ").append(Context.MONEY_TYPE_JK).append(" AND m.businessType = 1 ")
		   .append(" AND r.STATUS = 2 AND r.companyId = ").append(currentCompanyId)
		   .append(oldMoneyWhere).append(" group by r.id ) t ")
		   .append(" UNION ")
		   .append(" SELECT t.currencyId, t.amount, t.reviewId, t.orderId, t.createDate, t.updateDate,")
		   .append(" t.groupCode, t.operatorId, t.productId, t.orderType, t.payStatus, t.travelerId,")
		   .append(" t.agentId, t.chanpname, t.groupId,t.isShowPrint,t.printFlag,t.printTime,t.id_long, 1 as reviewflag FROM ")
		   .append(" (SELECT m.currencyId, m.amount, r.id AS reviewId, r.orderId, r.createDate,")
		   .append(" r.updateDate, g.groupCode, g.createBy AS operatorId, ht.uuid AS productId, ")
		   .append(Context.ORDER_TYPE_HOTEL).append(" as orderType, r.payStatus, r.travelerId,")
		   .append(" o.orderCompany AS agentId ,ht.activityName AS chanpname, g.uuid AS groupId,")
		   .append(" r.status as isShowPrint,r.printFlag,r.printTime,-1 as id_long FROM review r ")
		   .append(" LEFT JOIN hotel_money_amount m ON r.productType = ").append(Context.ORDER_TYPE_HOTEL)
		   .append(" AND m.reviewId = r.id, hotel_order o, activity_hotel_group g, activity_hotel ht")
		   .append(" WHERE o.id = r.orderId AND ht.uuid = o.activity_hotel_uuid AND g.uuid = o.activity_hotel_group_uuid ")
		   .append(" AND m.moneyType = ").append(Context.MONEY_TYPE_JK).append(" AND m.businessType = 1 ")
		   .append(" AND r.STATUS = 2 AND r.companyId = ").append(currentCompanyId)
		   .append(oldMoneyWhere).append(" group by r.id ) t ) tt where 1 = 1 ").append(where);
		String orderBy = page.getOrderBy();
		if(StringUtils.isBlank(orderBy)){
			//注意updateDate DESC前后不要有空格
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
		String createTimeMin = params.get("createTimeMin");     //申请日期开始
		String createTimeMax = params.get("createTimeMax");     //申请日期结束
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
		if ("0".equals(printStatus)) {
			str.append(" and (printFlag = ").append(printStatus).append(" or printFlag is null)");
		} else if ("1".equals(printStatus)) {
			str.append(" and printFlag = ").append(printStatus);
		}
		if(StringUtils.isNotBlank(createTimeMin)){
			// 包括最大的一天，例如:2016.05.30-2016.07.30 包括2016.05.30和2016.07.30，此处不需要加" 00:00:01"
			str.append(" AND createDate >= '").append(createTimeMin).append("' ");
		}
		if(StringUtils.isNotBlank(createTimeMax)){
			// 包括最大的一天，例如:2016.05.30-2016.07.30 包括2016.05.30和2016.07.30，所以需要加上" 23:59:59"
			str.append(" AND createDate <= '").append(createTimeMax).append(" 23:59:59' ");
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
	private String getMoneyWhere(Map<String, String> params, String productType){
		StringBuffer str = new StringBuffer();
		String currencyId = params.get("currencyId");           //币种ID
		String moneyBegin = params.get("moneyBegin");           //开始金额
		String moneyEnd = params.get("moneyEnd");               //结束金额
		/*新加过滤条件产品类型 by chy 2015年6月15日16:03:00*/
		String orderType = params.get("orderType");      //产品类型
		if (StringUtils.isNotBlank(orderType) && !"0".equals(orderType)) {
			str.append(" and ").append(productType).append(" = ").append(
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
		return str.toString();
	}

	@Override
	public List<Map<String, Object>> getTTSQZPayRecord(Integer batchId,
			Integer orderType) {
		StringBuffer str = new StringBuffer();
		str.append("SELECT r.id, d.label as payType,m.amount,")
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
	public void confimOrCancelTTSQZPay(String batchNo, Integer payStatus) {
		StringBuffer str = new StringBuffer();
		Long userId = UserUtils.getUser().getId();
		Long companyId = UserUtils.getUser().getCompany().getId();
		str.append("update review r set r.payStatus = ?, r.updateBy = ?, r.updateDate = ?")
		   .append(" where r.companyId = ").append(companyId)
		   .append(" and exists (select review_id from review_detail d where d.myKey = 'visaBorrowMoneyBatchNo'")
		   .append(" and d.review_id = r.id and d.myValue = '").append(batchNo).append("') ");
		updateBySql(str.toString(), payStatus, userId, new Date());
	}

	@Override
	public long getBorrowMoneyNotPayedCountForTTS(Long companyId) {
		StringBuffer str = new StringBuffer();
		str.append("SELECT count(*) FROM  ")
		   .append(" ( SELECT batch_no FROM visa_flow_batch_opration b, review r, review_detail d")
		   .append(" WHERE r.id = d.review_id AND d.myKey = 'visaBorrowMoneyBatchNo' ")
		   .append(" AND b.batch_no = d.myValue AND r.companyId = ").append(companyId.intValue())
		   .append(" AND b.busyness_type = 2 AND b.review_status IN (2, 3) ")//review_status 与 review 表的status一致
		   .append(" AND r.payStatus = 0 GROUP BY b.batch_no ) t");
		return getCountBySQL(str.toString());
	}

	@Override
	public long getBorrowMoneyNotPayedCount(Long companyId) {
		StringBuffer str = new StringBuffer();
		str.append("SELECT COUNT(*) AS count FROM review t ")
		   .append(" WHERE t.flowType = 19 AND t. STATUS IN (2, 3) AND t.active = 1")
		   .append(" AND t.companyId = ").append(companyId.intValue())
		   .append(" AND t.payStatus = 0");
		return getCountBySQL(str.toString());
	}

	@Override
	public List<Map<String, Object>> queryTraveler(Map<String, String> params) {
		//游客列表信息包括老审批和新审批两部分数据
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		str.append(" SELECT ")
		   .append(" 	createDate, ")
		   .append(" 	groupCode, ")
		   .append(" 	traveler, ")
		   .append(" 	salerName, ")
		   .append(" 	amount, ")
		   .append(" 	reviewType, ")
		   .append(" 	reviewId, ")
		   .append(" 	sysCountryId ")
		   .append(" FROM ")
		   .append(" 	( ")
		   .append(" 		SELECT ")
		   .append(" 			r.create_date AS createDate, ")
		   .append(" 			o.group_code AS groupCode, ")
		   .append(" 			r.traveller_name AS traveler, ")
		   .append(" 			o.salerName, ")
		   .append(" 			m.amount, ")
		   .append(" 			'2' AS reviewType, ")
		   .append(" 			r.id_long as reviewId, ")
		   .append(" 			p.sysCountryId ")
		   .append(" 		FROM ")
		   .append(" 			review_new r, ")
		   .append(" 			visa_order o, ")
		   .append(" 			visa_products p, ")
		   .append(" 			visa_flow_batch_opration b, ")
		   .append(" 			review_process_money_amount m ")
		   .append(" 		WHERE ")
		   .append(" 			p.id = o.visa_product_id ")
		   .append(" 		AND r.product_id = p.id ")
		   .append(" 		AND r.batch_no = b.batch_no ")
		   .append(" 		AND o.id = r.order_id ")
		   .append(" 		AND m.reviewId = r.id ")
		   .append(" 		AND r.status = ").append(ReviewConstant.REVIEW_STATUS_PASSED)
		   .append(" 		AND b.busyness_type = ").append(BUSYNESS_TYPE_2)
		   .append(" 		AND r.process_type = '").append(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY + "' ")
		   .append(" 		AND r.company_id = '").append(currentCompanyUuid).append("' ")
		   .append(" 		AND p.proCompanyId =").append(currentCompanyId);
			if (StringUtils.isNotBlank(params.get("groupCode"))) {
				str.append(" and o.group_code like '%").append(params.get("groupCode"))
				   .append("%'");
			}
			if (StringUtils.isNotBlank(params.get("visaType"))) {
				str.append(" and p.visaType = ").append(params.get("visaType"));
			}
			if (StringUtils.isNotBlank(params.get("payStatus"))) {
				str.append(" and r.pay_status = ").append(params.get("payStatus"));
			}
			if (StringUtils.isNotBlank(params.get("reportDateStart"))) {
				str.append(" and r.create_date >= '")
				   .append(params.get("reportDateStart")).append("' ");
			}
			if (StringUtils.isNotBlank(params.get("reportDateEnd"))) {
				str.append(" and r.create_date <= '")
				   .append(params.get("reportDateEnd"))
				   .append(" 23:59:59'");
			}
			if (StringUtils.isNotBlank(params.get("visaContry"))) {
				str.append(" and p.sysCountryId = ").append(params.get("visaContry"));
			}
			if (StringUtils.isNotBlank(params.get("salerId"))) {
				str.append(" and o.salerId = ").append(params.get("salerId"));
			}
			if (StringUtils.isNotBlank(params.get("operatorId"))) {
				str.append(" and p.createBy = ").append(params.get("operatorId"));
			}
			if (StringUtils.isNotBlank(params.get("moneyBegin"))) {
				str.append(" and b.batch_total_money >= ").append(params.get("moneyBegin"));
			}
			if (StringUtils.isNotBlank(params.get("moneyEnd"))) {
				str.append(" and b.batch_total_money <= ").append(params.get("moneyEnd"));
			}
			if ("0".equals(params.get("printStatus"))) {
				str.append(" and r.print_status = ").append(params.get("printStatus"));
			} else if ("1".equals(params.get("printStatus"))) {
				str.append(" and r.print_status = ").append(params.get("printStatus"));
			}
			if (StringUtils.isNotBlank(params.get("createTimeMin"))) {
				str.append(" and r.create_date >= '")
					.append(params.get("createTimeMin")).append("' ");
			}
			if (StringUtils.isNotBlank(params.get("createTimeMax"))) {
				str.append(" and r.create_date <= '")
			       .append(params.get("createTimeMax")).append(" 23:59:59' ");
			}
		str.append(" 		UNION ")
		   .append(" 			SELECT ")
		   .append(" 				r.createDate AS createDate, ")
		   .append(" 				o.group_code AS groupCode, ")
		   .append(" 				(SELECT NAME FROM traveler t WHERE t.id = r.travelerId) AS traveler, ")
		   .append(" 				o.salerName, ")
		   .append(" 				'-1' as amount, ")
		   .append(" 				'1' AS reviewType, ")
		   .append(" 				r.id as reviewId, ")
		   .append(" 				p.sysCountryId ")
		   .append(" 			FROM ")
		   .append(" 				review r, ")
		   .append(" 				review_detail rd, ")
		   .append(" 				visa_flow_batch_opration b, ")
		   .append(" 				visa_order o, ")
		   .append(" 				visa_products p ")
		   .append(" 			WHERE ")
		   .append(" 				r.id = rd.review_id ")
		   .append(" 			AND rd.myKey = 'visaBorrowMoneyBatchNo' ")
		   .append(" 			AND rd.myValue = b.batch_no ")
		   .append(" 			AND b.busyness_type = ").append(BUSYNESS_TYPE_2)
		   .append(" 			AND r.flowType = ").append(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY)
		   .append(" 			AND o.visa_product_id = p.id ")
		   .append(" 			AND p.proCompanyId =").append(currentCompanyId)
		   .append(" 			AND o.id = r.orderId ")
		   .append("			AND r.status in (2, 3)");
		if (StringUtils.isNotBlank(params.get("groupCode"))) {
			str.append(" and o.group_code like '%").append(params.get("groupCode"))
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
			str.append(" and o.salerId = ").append(params.get("salerId"));
		}
		if (StringUtils.isNotBlank(params.get("operatorId"))) {
			str.append(" and p.createBy = ").append(params.get("operatorId"));
		}
		if (StringUtils.isNotBlank(params.get("moneyBegin"))) {
			str.append(" and b.batch_total_money >= ").append(params.get("moneyBegin"));
		}
		if (StringUtils.isNotBlank(params.get("moneyEnd"))) {
			str.append(" and b.batch_total_money <= ").append(params.get("moneyEnd"));
		}
		if ("0".equals(params.get("printStatus"))) {
			str.append(" and (r.printFlag = ").append(params.get("printStatus")).append(" or r.printFlag is null)");
		} else if ("1".equals(params.get("printStatus"))) {
			str.append(" and r.printFlag = ").append(params.get("printStatus"));
		}
		if (StringUtils.isNotBlank(params.get("createTimeMin"))) {
			str.append(" and r.createDate >= '")
					.append(params.get("createTimeMin")).append("' ");
		}
		if (StringUtils.isNotBlank(params.get("createTimeMax"))) {
			str.append(" and r.createDate <= '")
					.append(params.get("createTimeMax")).append(" 23:59:59' ");
		}
		str.append(" 	) t order by salerName ");
		return findBySql(str.toString(), Map.class);
	}
	
}
