package com.trekiz.admin.modules.mtourfinance.repository.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.mtourOrder.dao.AirticketOrderMoneyAmountDao;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderMoneyAmount;
import com.trekiz.admin.modules.mtourfinance.json.*;
import com.trekiz.admin.modules.mtourfinance.pojo.AccountAgeParam;
import com.trekiz.admin.modules.mtourfinance.pojo.ReceiveOrderListParam;
import com.trekiz.admin.modules.mtourfinance.repository.FinanceDao;
import com.trekiz.admin.modules.pay.dao.PayGroupDao;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
/**
 * 
* @ClassName: FinanceDaoImpl
* @Description: TODO(美途国际财务通用接口实现)
* @author kai.xiao
* @date 2015年10月15日 下午7:31:31
*
 */
@SuppressWarnings("rawtypes")
@Repository
public class FinanceDaoImpl extends BaseDaoImpl implements FinanceDao {
	@Autowired
	private CostRecordDao costRecordDao;
	@Autowired
	private AirticketOrderMoneyAmountDao airticketOrderMoneyAmountDao;
	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	@Autowired
	private PayGroupDao payGroupDao;

	private static final String DJ = "1";		//订单收款类型--定金
	private static final String WK = "2";		//订单收款类型--尾款
	private static final String QK = "3";		//订单收款类型--全款
	private static final String ZS = "4";		//订单收款类型--追散
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> fetchinfo(Integer id) {
		String sql = "SELECT ao.id orderId, ao.agentinfo_id agentid, t.journey, ao.create_date, t.group_code, " +
				"CASE ao.agentinfo_id WHEN - 1 THEN ao.nagentName ELSE ( SELECT agent.agentName FROM agentinfo agent " +
				"WHERE agent.id = ao.agentinfo_id ) END agentName, CASE WHEN agentid > 0 THEN ( SELECT a.agentContactMobile " +
				"FROM agentinfo a WHERE a.id = ao.agentinfo_id ) ELSE ( SELECT GROUP_CONCAT(DISTINCT oc.contactsTel) FROM ordercontacts oc WHERE " +
				"oc.orderId = ao.id AND orderType = 7 ) END agentTel, sysuser. NAME, sysuser.mobile, sysuser.phone, ( SELECT " +
				"s.name_cn FROM sys_geography s WHERE s.UUID = t.country ) country, ( SELECT s.name_short_pinyin FROM " +
				"sys_geography s WHERE s.UUID = t.departureCity ) fa, ( SELECT s.name_short_pinyin FROM sys_geography s " +
				"WHERE s.UUID = t.arrivedCity ) aa, t.activity_airticket_name productName, ao.comments ordercomments, " +
				"t.remark remark FROM airticket_order ao LEFT JOIN activity_airticket t ON ao.airticket_id = t.id LEFT " +
				"JOIN sys_user sysuser ON ao.create_by = sysuser.id WHERE ao.del_flag = 0 AND ao.id = ?";


		return findBySql(sql,Map.class,id);
	}

	@Override
	public List<CostRecordJsonBean> getCostRecordList(Integer orderId) {
		String sql = "SELECT orderId orderUuid, cr.id costUuid, cr.name fundsName, supplyType tourOperatorChannelCategoryCode, " +
				"supplierType tourOperatorOrChannelTypeCode,supplyName tourOperatorOrChannelName, supplyId tourOperatorOrChannelUuid, " +
				"isJoin invoiceOriginalTypeCode, bigCode PNR, supplyId tourOperatorUuid, supplyName tourOperatorName,cr.airline_name airlineName,quantity peopleCount,currencyId currencyUuid," +
				"price amount,rate exchangeRate, currencyAfter convertedCurrencyUuid,priceAfter convertedAmount,cr.createBy inputPerson,su.name userName," +
				"CASE payStatus WHEN 0 THEN '已付款' WHEN 1 THEN '已付款' WHEN 2 THEN '待提交' WHEN 3 THEN '已提交' WHEN 4 THEN '已撤回' ELSE '' END state,"
				+ "CASE payStatus WHEN 0 THEN 1 WHEN payStatus IS NULL THEN '' ELSE payStatus END stateCode,comment memo,cr.createDate,cr.cost_total_deposit costTotalDeposit,IFNULL(airticket.lockStatus,0) lockStatus " +
				" FROM cost_record cr,sys_user su,activity_airticket airticket"
				+ " where cr.activityId = airticket.id and orderId = ? and budgetType = 1 and reviewType = 0 and cr.delFlag=0 AND cr.createBy = su.id order by createDate desc";
		List<CostRecordJsonBean> list = findBySql(sql, Map.class, orderId);
		return list;
	}
	
	@Override
	public List<CostRecordJsonBean> getOtherCostRecordList(Integer orderId) {
		String sql = "SELECT orderId orderUuid, cr.id otherRevenueUuid, pay.id as receiveUuid, cr.name fundsName, supplyType tourOperatorChannelCategoryCode, " +
				"supplierType tourOperatorOrChannelTypeCode,supplyName tourOperatorOrChannelName, supplyId tourOperatorOrChannelUuid, " +
				"isJoin invoiceOriginalTypeCode, bigCode PNR, supplyId tourOperatorUuid, supplyName tourOperatorName, quantity peopleCount,currencyId currencyUuid," +
				"price amount,rate exchangeRate, currencyAfter convertedCurrencyUuid,priceAfter convertedAmount,cr.createBy inputPerson,su.name userName," +
				"CASE payStatus WHEN 1 THEN '已确认' WHEN 2 THEN '待提交' WHEN 3 THEN '已提交' WHEN 4 THEN '已驳回' WHEN 5 THEN '已删除' ELSE '' END state,"
				+ "CASE WHEN payStatus IS NULL THEN '' ELSE payStatus END stateCode,comment memo,cr.createDate,IFNULL(airticket.lockStatus,0) lockStatus " +
				" FROM cost_record cr "+
				//添加receiveUuid:'收款记录uuid' add by majiancheng 2016-02-15
				" LEFT JOIN pay_group pay ON pay.cost_record_id = cr.id and pay.orderType = cr.orderType " +
				" LEFT JOIN sys_user su ON cr.createBy = su.id " +
				" LEFT JOIN activity_airticket airticket ON  cr.activityId = airticket.id "
				+ " where orderId = ? and budgetType = 2 and reviewType = 0 and cr.delFlag=0 order by createDate desc";
		List<CostRecordJsonBean> list = findBySql(sql, Map.class, orderId);
		
		return list;
	}

	@Override
	public List<BigCode> getBigCodeList(Integer orderId) {
		String sql = "SELECT flight_pnr invoiceOriginalTypeCode, flight_pnr PNR, '' tourOperatorUuid, '' tourOperatorName " +
				" FROM airticket_order_pnr where airticket_order_id = ?";
		List<BigCode> list = findBySql(sql, Map.class, orderId);
		return list;
	}

	@Override
	public SettlementJsonBean getSettlementInfo(Long orderId) {
		SettlementJsonBean jsonBean = new SettlementJsonBean();
		//结算单基础数据
		List<Map<String, Object>> baseInfo = getSettlementBaseInfo(orderId);
		//结算单基础信息部分需要处理的数据，有，出票日期，供应商名称
		List<Map<String, Object>> extraBaseInfo = getSettlementExtraBaseInfo(orderId);
		//订单收款
		List<Map<String, Object>> orderInComes = getSettlementInComeOrder(orderId);
		//其他收入收款
		List<Map<String, Object>> otherInComes = getSettlementInComeOther(orderId);
		//成本项
		List<Map<String, Object>> costs = getSettlementCost(orderId);
		//追加成本
		List<Map<String, Object>> additionalCosts = getAdditionalCost(orderId);
		//退款
		List<Map<String, Object>> refunds = getRefund(orderId);
		setSettlementBaseInfo(jsonBean, baseInfo);
		setSettlementExtraBaseInfo(jsonBean, extraBaseInfo);
		setSettlementOrderInCome(jsonBean, orderInComes);
		setSettlementOtherInCome(jsonBean, otherInComes);
		setSettlementCost(jsonBean, costs);
		setSettmentAdditionalCost(jsonBean, additionalCosts);
		setSettmentRefund(jsonBean, refunds);
		return jsonBean;
	}
	
	/**
	 * 设置结算单退款的数据
	 * @param jsonBean			结算单对象
	 * @param refunds			退款数据
	 * @author shijun.liu
	 */
	private void setSettmentRefund(SettlementJsonBean jsonBean, List<Map<String, Object>> refunds){
		if(null != refunds && refunds.size()>0){
			List<AdditionCostRefundJsonBean> list = new ArrayList<AdditionCostRefundJsonBean>();
			for (int i=0; i<refunds.size();i++) {
				AdditionCostRefundJsonBean refund = new AdditionCostRefundJsonBean();
				Object amountObj = refunds.get(i).get("amount");
				Object exchangerateObj = refunds.get(i).get("exchangerate");
				Object currencyIdObj = refunds.get(i).get("currency_id");
				Object currencyMarkObj = refunds.get(i).get("currency_mark");
				Object rmbObj = refunds.get(i).get("rmb");
				
				String amount = (String)(amountObj == null?"":amountObj.toString());
				String exchangerate = (String)(exchangerateObj == null?"":exchangerateObj.toString());
				String currencyId = (String)(currencyIdObj == null?"":currencyIdObj.toString());
				String currencyMark = (String)(currencyMarkObj == null?"":currencyMarkObj.toString());
				String rmb = (String)(rmbObj == null?"":rmbObj.toString());
				refund.setAmount(amount);
				refund.setExchangeRate(exchangerate);
				refund.setCurrencyUuid(currencyId);
				refund.setCurrencyMark(currencyMark);
				refund.setRmb(rmb);
				list.add(refund);
			}
			jsonBean.setRefunds(list);
		}
	}
	
	/**
	 * 设置结算单追加成本的数据
	 * @param jsonBean			结算单对象
	 * @param additionalCosts	追加成本数据
	 * @author shijun.liu
	 */
	private void setSettmentAdditionalCost(SettlementJsonBean jsonBean, List<Map<String, Object>> additionalCosts){
		if(null != additionalCosts && additionalCosts.size()>0){
			List<AdditionCostRefundJsonBean> list = new ArrayList<AdditionCostRefundJsonBean>();
			for (int i=0; i<additionalCosts.size();i++) {
				AdditionCostRefundJsonBean additional = new AdditionCostRefundJsonBean();
				Object amountObj = additionalCosts.get(i).get("amount");
				Object exchangerateObj = additionalCosts.get(i).get("exchangerate");
				Object currencyIdObj = additionalCosts.get(i).get("currency_id");
				Object currencyMarkObj = additionalCosts.get(i).get("currency_mark");
				Object rmbObj = additionalCosts.get(i).get("rmb");
				
				String amount = String.valueOf(amountObj == null?"":amountObj);
				String exchangerate = String.valueOf(exchangerateObj == null?"":exchangerateObj);
				String currencyId = String.valueOf(currencyIdObj == null?"":currencyIdObj);
				String currencyMark = String.valueOf(currencyMarkObj == null?"":currencyMarkObj);
				String rmb = String.valueOf(rmbObj == null?"":rmbObj);
				additional.setAmount(amount);
				additional.setExchangeRate(exchangerate);
				additional.setCurrencyUuid(currencyId);
				additional.setCurrencyMark(currencyMark);
				additional.setRmb(rmb);
				list.add(additional);
			}
			jsonBean.setAdditionalCosts(list);
		}
	}
	
	/**
	 * 设置结算单收款项的其他收入收款的数据
	 * @param jsonBean		结算单对象
	 * @author shijun.liu
	 */
	private void setSettlementCost(SettlementJsonBean jsonBean, List<Map<String, Object>> costs){
		if(null != costs && costs.size() > 0){
			List<CostJsonBean> list = new ArrayList<CostJsonBean>();
			Set<String> airLines = new HashSet<String>();	 //航空公司二字码
			for (int i = 0; i < costs.size(); i++) {
				CostJsonBean cost = new CostJsonBean();
				Object fundsNameObj = costs.get(i).get("name");
				Object codeTypeObj = costs.get(i).get("code_type");
				Object PNROrAgentNameObj = costs.get(i).get("flight_pnr");
				Object airLineNameObj = costs.get(i).get("airline_name");
				Object quantityObj = costs.get(i).get("quantity");
				Object priceObj = costs.get(i).get("price");
				Object totalAmountObj = costs.get(i).get("totalAmount");
				Object currencyMarkObj = costs.get(i).get("currencyMark");
				Object currencyIdObj = costs.get(i).get("currencyId");
				Object rateObj = costs.get(i).get("rate");
				Object rmbamountObj = costs.get(i).get("priceAfter");
				Object supplyName = costs.get(i).get("supplyName");
				String fundsName = String.valueOf(fundsNameObj==null?"":fundsNameObj);
				String invoiceOriginalTypeCode = String.valueOf(codeTypeObj==null?"":codeTypeObj);
				String PNROrAgentName = String.valueOf(PNROrAgentNameObj==null?"":PNROrAgentNameObj);
				String airLineName = String.valueOf(airLineNameObj==null?"":airLineNameObj);
				String quantity = String.valueOf(quantityObj==null?"":quantityObj);
				String price = String.valueOf(priceObj==null?"":priceObj.toString());
				String totalAmount = String.valueOf(totalAmountObj==null?"":totalAmountObj.toString());
				String currencyMark = String.valueOf(currencyMarkObj==null?"":currencyMarkObj);
				String currencyId = String.valueOf(currencyIdObj==null?"":currencyIdObj.toString());
				String exchangerate = String.valueOf(rateObj==null?"":rateObj.toString());
				String rmbamount = String.valueOf(rmbamountObj==null?"0":rmbamountObj.toString());
				String supplyNamee = String.valueOf(supplyName==null?"":supplyName.toString());
				cost.setFundsName(fundsName);
				cost.setInvoiceOriginalTypeCode(invoiceOriginalTypeCode);
				if("0".equals(invoiceOriginalTypeCode)){//PNR
					cost.setPNR(PNROrAgentName);
				}else if("1".equals(invoiceOriginalTypeCode)){//地接社
					cost.setTourOperatorName(PNROrAgentName);
				}
				cost.setTourOperatorName(supplyNamee);
				cost.setAirlineCompany(airLineName);
				cost.setPeopleCount(quantity);
				cost.setPrice(price);
				cost.setTotalAmount(totalAmount);
				cost.setCurrencyUuid(currencyId);
				cost.setCurrencyMark(currencyMark);
				cost.setExchangeRate(exchangerate);
				cost.setRmb(rmbamount);
				list.add(cost);
				//表头的航空公司和成本项的航空公司一致    update by shijun.liu 20160111
				airLines.add(airLineName);
			}
			//表头的航空公司和成本项的航空公司一致    update by shijun.liu 20160111
			Iterator<String> it = airLines.iterator();
			String airLineCompany = "";
			while(it.hasNext()){
				if("".equals(airLineCompany)){
					airLineCompany = it.next();
				}else{
					airLineCompany += "," + it.next();
				}
			}
			jsonBean.setAirlineCompany(airLineCompany);
			jsonBean.setCosts(list);
		}
	}
	
	/**
	 * 设置结算单收款项的其他收入收款的数据
	 * @param jsonBean		结算单对象
	 * @param otherInComes  其他收入收款数据
	 * @author shijun.liu
	 */
	private void setSettlementOtherInCome(SettlementJsonBean jsonBean, List<Map<String, Object>> otherInComes){
		if(null != otherInComes && otherInComes.size() > 0){
			List<InComeJsonBean> list = new ArrayList<InComeJsonBean>();
			for (int i = 0; i < otherInComes.size(); i++) {
				InComeJsonBean income = new InComeJsonBean();
				Object reviceDateObj = otherInComes.get(i).get("reviceDate");
				Object agentNameObj = otherInComes.get(i).get("agentName");
				Object amountObj = otherInComes.get(i).get("amount");
				Object currencyIdObj = otherInComes.get(i).get("currencyId");
				Object exchangerateObj = otherInComes.get(i).get("exchangerate");
				Object rmbamountObj = otherInComes.get(i).get("rmbamount");
				Object currencyMarkObj = otherInComes.get(i).get("currencyMark");
				
				String receiveDate = String.valueOf(reviceDateObj==null?"":reviceDateObj);
				String customer = String.valueOf(agentNameObj==null?"":agentNameObj);
				String amount = String.valueOf(amountObj==null?"":amountObj);
				String currencyId = String.valueOf(currencyIdObj==null?"":currencyIdObj);
				String currencyMark = String.valueOf(currencyMarkObj==null?"":currencyMarkObj);
				String exchangerate = String.valueOf(exchangerateObj==null?"":exchangerateObj);
				String rmbamount = String.valueOf(rmbamountObj==null?"0":rmbamountObj);
				
				income.setReceiveDate(receiveDate);
				income.setCustomer(customer);
				income.setPeopleCount("");//收入项人数，如果是其他收入收款时，人数为空
				income.setBalancePayment(amount);
				income.setTotalAmount(amount);
				income.setCurrencyMark(currencyMark);
				income.setCurrencyUuid(currencyId);
				income.setExchangeRate(exchangerate);
				income.setRmb(rmbamount);
				list.add(income);
			}
			List<InComeJsonBean> incomes = jsonBean.getIncomes();
			if(null == incomes){
				jsonBean.setIncomes(list);
			}else{
				incomes.addAll(list);
				jsonBean.setIncomes(incomes);
			}
		}
	}
	
	/**
	 * 设置结算单收款项的订单收款
	 * @param jsonBean		结算单对象
	 * @param orderInComes  订单收款数据
	 * @author shijun.liu
	 */
	private void setSettlementOrderInCome(SettlementJsonBean jsonBean, List<Map<String, Object>> orderInComes){
		if(null != orderInComes && orderInComes.size() > 0){
			List<InComeJsonBean> list = new ArrayList<InComeJsonBean>();
			for (int i = 0; i < orderInComes.size(); i++) {
				InComeJsonBean income = new InComeJsonBean();
				Object reviceDateObj = orderInComes.get(i).get("reviceDate");
				Object agentNameObj = orderInComes.get(i).get("agentName");
				Object amountObj = orderInComes.get(i).get("amount");
				Object currencyIdObj = orderInComes.get(i).get("currencyId");
				Object exchangerateObj = orderInComes.get(i).get("exchangerate");
				Object rmbamountObj = orderInComes.get(i).get("rmbamount");
				Object currencyMarkObj = orderInComes.get(i).get("currencyMark");
				Object payPriceTypeObj = orderInComes.get(i).get("payPriceType");
				Object receivePeopleCount = orderInComes.get(i).get("receivePeopleCount");
				String receiveDate = String.valueOf(reviceDateObj==null?"":reviceDateObj);
				String customer =  String.valueOf(agentNameObj==null?"":agentNameObj);
				String amount =  String.valueOf(amountObj==null?"":amountObj);
				String currencyId =  String.valueOf(currencyIdObj==null?"":currencyIdObj);
				String currencyMark =  String.valueOf(currencyMarkObj==null?"":currencyMarkObj);
				String exchangerate =  String.valueOf(exchangerateObj==null?"":exchangerateObj);
				String payPriceType =  String.valueOf(payPriceTypeObj==null?"":payPriceTypeObj);
				String rmbamount =  String.valueOf(rmbamountObj==null?"0":rmbamountObj);
				String peopleCount = String.valueOf((receivePeopleCount==null?0:receivePeopleCount));
				
				income.setReceiveDate(receiveDate);
				income.setCustomer(customer);
				income.setPeopleCount(peopleCount);
				if(DJ.equals(payPriceType)){
					income.setDeposit(amount);
				}else{
					income.setBalancePayment(amount);
				}
				income.setTotalAmount(amount);
				income.setCurrencyMark(currencyMark);
				income.setCurrencyUuid(currencyId);
				income.setExchangeRate(exchangerate);
				income.setRmb(rmbamount);
				list.add(income);
			}
			List<InComeJsonBean> incomes = jsonBean.getIncomes();
			if(null == incomes){
				jsonBean.setIncomes(list);
			}else{
				incomes.addAll(list);
				jsonBean.setIncomes(incomes);
			}
		}
	}
	
	/**
	 * 对结算单其他基础部分进行赋值，主要有出票日期，供应商名称
	 * @param jsonBean      结算单对象
	 * @param extraBaseInfo		基础信息
	 * @author shijun.liu
	 */
	private void setSettlementExtraBaseInfo(SettlementJsonBean jsonBean, List<Map<String, Object>> extraBaseInfo){
		if(null != extraBaseInfo && extraBaseInfo.size() > 0){
			StringBuffer airticketDates = new StringBuffer();//出票日期
			Set<String> supplierNames = new HashSet<String>(); //供应商名称
			for (int i = 0; i < extraBaseInfo.size(); i++) {
				Object airticketDateObj = extraBaseInfo.get(i).get("airtick_date");
				Object supplierNameObj = extraBaseInfo.get(i).get("supplierName");
				Object codeTypeObj = extraBaseInfo.get(i).get("code_type");
				String airticketDate = String.valueOf(airticketDateObj==null?"":airticketDateObj);
				String supplierName = String.valueOf(supplierNameObj==null?"":supplierNameObj);
				String codeType = String.valueOf(codeTypeObj==null?"":codeTypeObj);
				if(airticketDates.toString().indexOf(airticketDate)==-1){
					airticketDates.append(airticketDate).append(",");
				}
				if("0".equals(codeType)){
					supplierNames.add("GTT");
				}else if(supplierNames.toString().indexOf(supplierName)==-1){
					supplierNames.add(supplierName);
				}
			}
			Iterator<String> supplierIt = supplierNames.iterator();
			String supplierName = "";
			while(supplierIt.hasNext()){
				if("".equals(supplierName)){
					supplierName = supplierIt.next();
				}else{
					supplierName += "," + supplierIt.next();
				}
			}
			jsonBean.setSupplierName(supplierName);
			if(airticketDates.length()>0){
				//选择最早的出票时限
				int len = airticketDates.length();
				String invoiceDate = airticketDates.delete(len-1, len).toString();
				String[] dates = invoiceDate.split(",");
				int size = dates.length;
				if(size == 1){//如果集合中只有一条数据则该数据就是最早的出票时限
					jsonBean.setInvoiceDate(dates[0]);
				}else{
					//寻找数组中最小的日期
					String minDate = DateUtils.getMinDate(dates);
					jsonBean.setInvoiceDate(minDate);
				}
			}
		}
	}
	
	/**
	 * 对结算单基础部分进行赋值
	 * @param jsonBean      结算单对象
	 * @param baseInfo		基础信息
	 * @author shijun.liu
	 */
	private void setSettlementBaseInfo(SettlementJsonBean jsonBean, List<Map<String, Object>> baseInfo){
		if(null != baseInfo && baseInfo.size()==1){
			Object groupNoObj = baseInfo.get(0).get("group_code");
			Object peopleCountObj = baseInfo.get(0).get("person_num");
			Object orderNoObj = baseInfo.get(0).get("order_no");
			Object journeyObj = baseInfo.get(0).get("journey");
			Object travelPeriodObj = baseInfo.get(0).get("groupOpenDate");
			Object ordererObj = baseInfo.get(0).get("orderer");
			Object ticketNameObj = baseInfo.get(0).get("operator");
			Object lockStatusObj = baseInfo.get(0).get("lockStatus");
			String groupNo = String.valueOf(groupNoObj==null?"":groupNoObj);
			String peopleCount = String.valueOf((peopleCountObj==null?"0":peopleCountObj));
			String orderNo = String.valueOf((orderNoObj==null?"":orderNoObj));
			String journey =  String.valueOf(journeyObj==null?"":journeyObj);
			String travelPeriod =  String.valueOf(travelPeriodObj==null?"":travelPeriodObj);
			String orderer =  String.valueOf(ordererObj==null?"":ordererObj);
			String ticketName =  String.valueOf(ticketNameObj==null?"":ticketNameObj);
			String lockStatus =  String.valueOf(lockStatusObj==null?"0":lockStatusObj);
			jsonBean.setGroupNo(groupNo);
			jsonBean.setPeopleCount(peopleCount+"");
			jsonBean.setOrderNo(orderNo);
			jsonBean.setItinerary(journey);
			jsonBean.setTravelPeriod(travelPeriod);
			jsonBean.setOrderer(orderer);
			jsonBean.setTicketName(ticketName);
			jsonBean.setLockStatus(lockStatus);
		}
	}
	
	/**
	 * 查询美图国际结算单数据的基础信息
	 * @param orderId  机票订单ID
	 * @return
	 * @author shijun.liu
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getSettlementBaseInfo(Long orderId){
		StringBuffer str = new StringBuffer();
		str.append("SELECT")
		   .append("  p.group_code,")
		   .append("  o.person_num,")
		   .append("  o.order_no,")
		   .append("  p.journey,")
		   .append("  p.startingDate as groupOpenDate,")
		   .append("  (SELECT NAME FROM sys_user su WHERE su.id = o.create_by) AS orderer,")
		   .append("  (SELECT NAME FROM sys_user su WHERE su.id = p.operator) AS operator, ")
		   .append("  p.lockStatus ")
		   .append("FROM")
		   .append("  airticket_order o,")
		   .append("  activity_airticket p ")
		   .append(" WHERE ")
		   .append("  o.airticket_id = p.id")
		   .append(" AND o.del_flag = '").append(BaseEntity.DEL_FLAG_NORMAL).append("'")
		   .append(" AND o.id = ? ");
		return findBySql(str.toString(), Map.class, orderId);
	}
	
	/**
	 * 查询美图国际结算单数据的一些基础信息，例如。出票日期 ，供应商名称
	 * @param orderId   机票订单ID
	 * @return 
	 * @author shijun.liu
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getSettlementExtraBaseInfo(Long orderId){
		StringBuffer str = new StringBuffer();
		str.append("SELECT")
		   .append("  DATE_FORMAT(ticket_deadline,'%Y-%m-%d') AS airtick_date,")
		   .append("  code_type, ")
		   .append("  CASE code_type ")
		   .append("  WHEN 0 THEN 'GTT' ")
		   .append("  WHEN 1 THEN ")
		   .append("  	(SELECT supplierName FROM supplier_info WHERE id = flight_pnr) ")
		   .append("  END AS supplierName ")
		   .append("FROM")
		   .append("  airticket_order_pnr p ")
		   .append("WHERE")
		   .append("  p.airticket_order_id = ? ");
		return findBySql(str.toString(), Map.class, orderId);
	}
	
	/**
	 * 查询订单收款的收入数据
	 * @param orderId    机票订单ID
	 * @return
	 * @author shijun.liu
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getSettlementInComeOrder(Long orderId){
		StringBuffer str = new StringBuffer();
		str.append("SELECT")
		   .append("  DATE_FORMAT(o.createDate,'%Y-%m-%d') as reviceDate,")
		   .append("  (SELECT ai.agentName FROM agentinfo ai, airticket_order o WHERE ai.id = o.agentinfo_id AND o.id = ?) AS agentName,")
		   .append("  o.receivePeopleCount,")
		   .append("  m.amount,")
		   .append("  m.currencyId,")
		   .append("  m.exchangerate,")
		   .append("  round(ifnull((m.amount * m.exchangerate), 0),2) rmbamount,")
		   .append("  (SELECT cny.currency_mark FROM currency cny WHERE cny.currency_id = m.currencyId) AS currencyMark,")
		   .append("  payPriceType ")
		   .append("FROM")
		   .append("  orderpay o ")
		   .append("LEFT JOIN money_amount m ON o.moneySerialNum = m.serialNum")
		   .append(" WHERE o.delFlag = '").append(BaseEntity.DEL_FLAG_NORMAL).append("'")
		   .append("   AND (isAsAccount NOT IN (0, 2) or isAsAccount is null)")
		   .append("   AND o.orderType = ").append(Context.ORDER_TYPE_JP)
		   .append("   AND o.orderId = ? ");
		return findBySql(str.toString(), Map.class, orderId, orderId);
	}
	
	/**
	 * 查询其他收入收款的数据
	 * @param orderId   机票订单ID
	 * @return
	 * @author shijun.liu
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getSettlementInComeOther(Long orderId){
		StringBuffer str = new StringBuffer();
		//其他收入收款状态: 1:已付款, 2:待提交, 3:已提交, 4:已驳回, 5:已删除		2015.11.18
		str.append("SELECT")
		   .append("    g.cost_record_id,")
		   .append("    DATE_FORMAT(g.createDate,'%Y-%m-%d') as reviceDate,")
		   .append("    c.supplyName as agentName,")
		   .append("    c.quantity ,")
		   .append("    m.amount,")
		   .append("    m.currencyId,")
		   .append("    (SELECT currency_mark FROM currency cny WHERE cny.currency_id = m.currencyId) AS currencyMark,")
		   .append("    m.exchangerate,")
		   .append("    round(ifnull((m.amount * m.exchangerate), 0),2) AS rmbamount ")
		   .append("FROM ")
		   .append("    pay_group g ")
		   .append(" LEFT JOIN cost_record c ON c.id = g.cost_record_id")
		   .append(" AND c.budgetType = 2,")
		   .append("  money_amount m ")
		   .append(" WHERE")
		   .append("     g.payPrice = m.serialNum ")
		   .append("   AND g.orderType = ").append(Context.ORDER_TYPE_JP)
		   .append("   AND c.payStatus not in (2,4) ")
		   .append("   AND c.orderId = ? ");
		return findBySql(str.toString(), Map.class, orderId);
	}
	
	/**
	 * 查询成本项数据信息
	 * @param orderId	机票订单ID
	 * @return
	 * @author shijun.liu
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getSettlementCost(Long orderId){
		StringBuffer str = new StringBuffer();
		//成本状态payStatus只有一下三种，1:已付款，2：待提交，3：已提交 	2015.11.18
		str.append("SELECT")
		   .append("    c.name,")
			.append("    c.supplyName,")
		   .append("    o.code_type,")
		   .append(" 	CASE o.code_type")
		   .append(" WHEN 0 THEN ")
		   .append(" 	o.flight_pnr")
		   .append(" WHEN 1 THEN ")
		   .append("    (SELECT supplierName FROM supplier_info WHERE id = o.flight_pnr)")
		   .append(" END AS flight_pnr,");
		   if(UserUtils.isMtourUser()){
			  str.append("  c.airline AS airline_name,");//update by zhanghao 20160109 成本项中的航空公司抓取成本记录中的数据
		   }else{
			  str.append("  o.airline AS airline_name,");
		   }
		   str.append("  c.quantity,")
		   .append("  c.price,")
		   .append("  round(ifnull(c.price * c.quantity,0),2) as totalAmount,")
		   .append("  c.currencyId,")
		   .append("  (SELECT currency_mark FROM currency cny WHERE cny.currency_id = c.currencyId) AS currencyMark,")
		   .append("  c.rate,")
		   .append("  c.priceAfter ")
		   .append("FROM ")
		   .append("    cost_record c ")
		   .append("LEFT JOIN airticket_order_pnr o ON c.pnr_uuid = o.uuid ")
		   .append("WHERE ")
		   .append("    c.delFlag = '").append(BaseEntity.DEL_FLAG_NORMAL).append("' ")
		   .append(" AND c.budgetType = 1 ")
		   .append(" AND c.payStatus <> 2 and c.orderId = o.airticket_order_id ")
		   .append(" AND c.orderId = ? order by o.code_type ");
		return findBySql(str.toString(), Map.class, orderId);
	}
	
	/**
	 * 查询追加成本数据信息
	 * @param orderId	机票订单ID
	 * @return
	 * @author shijun.liu
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getAdditionalCost(Long orderId){
		StringBuffer str = new StringBuffer();
		str.append("SELECT")
		   .append("    amount,")
		   .append("    exchangerate,")
		   .append(" 	currency_id,")
		   .append("    (SELECT currency_mark FROM currency cny WHERE cny.currency_id = m.currency_id) AS currency_mark,")
		   .append("    IFNULL(amount * exchangerate, 0) as rmb ")
		   .append(" FROM ")
		   .append(" 	 airticket_order_moneyAmount m")
		   .append(" WHERE ")
		   .append("     m.moneyType = 3")
		   .append(" AND m.status <> 0")
		   .append(" AND m.airticket_order_id = ?");
		return findBySql(str.toString(), Map.class, orderId);
	}
	
	/**
	 * 查询退款数据信息
	 * @param orderId	机票订单ID
	 * @return
	 * @author shijun.liu
	 */
	@SuppressWarnings("unchecked")
	private List<Map<String, Object>> getRefund(Long orderId){
		StringBuffer str = new StringBuffer();
		str.append("SELECT")
		   .append("    amount,")
		   .append("    exchangerate,")
		   .append(" 	currency_id,")
		   .append("    (SELECT currency_mark FROM currency cny WHERE cny.currency_id = m.currency_id) AS currency_mark,")
		   .append("    IFNULL(amount * exchangerate,0) as rmb ")
		   .append(" FROM ")
		   .append(" 	 airticket_order_moneyAmount m")
		   .append(" WHERE ")
		   .append("     m.moneyType = 2 ")
		   .append(" AND m.status <> 0")
		   .append(" AND m.airticket_order_id = ?");
		return findBySql(str.toString(), Map.class, orderId);
	}

	@Override
	public void submitCostRecord(Integer id) {
		String sql = "update cost_record set payStatus =" + CostRecord.PAY_STATUS_SUBMIT + " where id = ?";
		updateBySql(sql, id);
	}
	
	@Override
	public void cancelCostRecord(Integer id) {
		String sql = "update cost_record set payStatus =" + CostRecord.PAY_STATUS_TOBESUBMIT + " where id = ?";
		updateBySql(sql, id);
	}

	@Override
	public List<OrderDetailJsonBean> getOrderDetail(String channelId) {
		// TODO Auto-generated method stub
		List<OrderDetailJsonBean> beanList = new ArrayList<OrderDetailJsonBean>();
		StringBuffer sql = new StringBuffer();
		sql.append("select `order`.id as id," // 订单id
				+ "`order`.order_no as orderNum," // 订单号
				+ "`activity`.group_code as groupCode,"// 团号'
				+ "activity.activity_airticket_name as activity_airticket_name,"// 产品名称'
				+ "`order`.salerName as salerName,"// 销售'
				+ "activity.operator,"// 计调'
				+ "`order`.create_by as create_by ," // 下单人'
				+ "`order`.create_date as create_date,"//  下单时间'
				+ "`order`.person_num as person_num,"// 人数'
				+ "`activity`.startingDate as groupOpenDate,");// '出团日期'
		sql.append(" `order`.total_money as total_money," // 外报总金额
				+ "`order`.payed_money as payed_money," // 已收金额
				+ "`order`.accounted_money as accounted_money "); // 到帐金额
		sql.append(" from airticket_order `order`,activity_airticket activity ");
		sql.append(" where `order`.airticket_id = activity.id ");
		// 当前用户所属批发商
		Long myselfCompanyID = UserUtils.getUser().getCompany().getId();
		sql.append(" and activity.proCompany= "+myselfCompanyID)
		   .append(" and `order`.lockstatus <> 2 ")
		   //0312 待确认订单不计入账龄数据  2016.6.16 王洋 
		   .append(" AND `order`.lockstatus <> 4 ");
		if(StringUtils.isNotBlank(channelId)){
			sql.append(" and `order`.agentinfo_id = ?");
			List<Map<String,Object>> templist = new ArrayList<Map<String,Object>>();  
			templist = findBySql(sql.toString(), Map.class,channelId);
			if(!templist.isEmpty()){
				for(Map<String,Object> map :templist){
					OrderDetailJsonBean bean = new OrderDetailJsonBean();
					if(map.get("id")!=null && StringUtils.isNotBlank(map.get("id").toString())){
						bean.setOrderUuid(map.get("id").toString());
					}
					if(map.get("orderNum")!=null && StringUtils.isNotBlank(map.get("orderNum").toString())){
						bean.setOrderNum(map.get("orderNum").toString());
					}
					if(map.get("groupCode")!=null && StringUtils.isNotBlank(map.get("groupCode").toString())){
						bean.setGroupNo(map.get("groupCode").toString());
					}
					if(map.get("activity_airticket_name")!=null && StringUtils.isNotBlank(map.get("activity_airticket_name").toString())){
						bean.setProductName(map.get("activity_airticket_name").toString());
					}
					if(map.get("salerName")!=null && StringUtils.isNotBlank(map.get("salerName").toString())){
						bean.setSalesName(map.get("salerName").toString());
					}
					if(map.get("operator")!=null && StringUtils.isNotBlank(map.get("operator").toString())){
						List<String> names = getOperators(map.get("operator").toString());
						bean.setOperator(names);
					}
					if(map.get("create_by")!=null && StringUtils.isNotBlank(map.get("create_by").toString())){
						User user = UserUtils.getUser(Long.valueOf(map.get("create_by").toString()));
						bean.setOrderer(user.getName());
					}
					if(map.get("create_date")!=null && StringUtils.isNotBlank(map.get("create_date").toString())){
						bean.setOrderTime(DateUtils.formatCustomDate((Date)map.get("create_date"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
					}
					if(map.get("person_num")!=null && StringUtils.isNotBlank(map.get("person_num").toString())){
						bean.setPeopleCount(map.get("person_num").toString());
					}
					if(map.get("groupOpenDate")!=null && StringUtils.isNotBlank(map.get("groupOpenDate").toString())){
						bean.setDepartureDate(DateUtils.formatCustomDate((Date)map.get("groupOpenDate"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
					}
					if(map.get("total_money")!=null && StringUtils.isNotBlank(map.get("total_money").toString())){
						bean.setOrderAmountUuid(map.get("total_money").toString());
					}
					if(map.get("payed_money")!=null && StringUtils.isNotBlank(map.get("payed_money").toString())){
						bean.setReceivedAmountUuid(map.get("payed_money").toString());
					}
					if(map.get("accounted_money")!=null && StringUtils.isNotBlank(map.get("accounted_money").toString())){
						bean.setArrivedAmountUuid(map.get("accounted_money").toString());
					}
					beanList.add(bean);
				}
			}
		}
		return beanList;
	}

	//私有方法，辅助方法。根据类似 12,21,34 的字符串id。获取对应的username. yudong.xu 2016.6.21
	private List<String> getOperators(String userIds){
		if (null == userIds)
			return new ArrayList<>();
		String sql = "SELECT u.`name` FROM sys_user u WHERE FIND_IN_SET(u.id,?)";
		return findBySql(sql,userIds);
	}

	@Override
	public List<Map<String, Object>> getPaymentCost(Integer costId) {
		String sql = "SELECT '成本录入付款' fundsType, name fundsName, isJoin invoiceOriginalTypeCode, bigCode PNR, supplyId tourOperatorUuid, " +
				"supplyName tourOperatorName, quantity peopleCount, price, currencyId currencyUuid, supplierType tourOperatorType, " +
				"supplyName tourOperator, bankName remitBank, bankAccount remitAccount, comment memo,cost.rate as exchangeRate " +
				" FROM cost_record cost where cost.id = ?";
		return findBySql(sql, Map.class, costId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> getAccountAgeList(AccountAgeParam accountAgeParam, Long companyId) {
		StringBuffer str = new StringBuffer();
		StringBuffer where = new StringBuffer();
		String agentWhere = "";
		if("2".equals(accountAgeParam.getChannelType())){//非签约渠道
			agentWhere = " AND ai.is_uncontract = 1 AND ai.supplyId = " + companyId;
		}else if("1".equals(accountAgeParam.getChannelType())){//签约渠道
			agentWhere = " AND ai.is_uncontract IS NULL AND ai.supplyId = " + companyId;
		}
		if(StringUtils.isNotBlank(accountAgeParam.getChannelId())){
			//渠道
			where.append(" and bb.agentId in (").append(accountAgeParam.getChannelId()).append(")");
		}
		if(StringUtils.isNotBlank(accountAgeParam.getSalerId())){
			//跟进销售
			where.append(" and bb.agentSalerId in (").append(accountAgeParam.getSalerId()).append(")");
		}
		String searchValue = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" exists (")
		   .append(" select ai.agentName from ")
		   .append(" airticket_order o,")
		   .append(" agentinfo ai,")
		   .append(" activity_airticket p ")
		   .append(" WHERE ")
		   .append("     p.id = o.airticket_id")
		   .append(" AND o.agentinfo_id = ai.id ")
		   .append(agentWhere)
		   .append(" AND o.del_flag = '0' ")
		   .append(" AND o.lockstatus <> 2 ")
		   .append(" AND o.lockstatus <> 4 ")
		   .append(" AND p.proCompany =  ").append(companyId)
		   .append(" AND ai.agentName = rr.channelName ");
		boolean flag = false;
		if(StringUtils.isNotBlank(accountAgeParam.getSearchKey())){
			searchValue = accountAgeParam.getSearchKey();
			if(Context.MTOUR_SEARCHTYPE_ORDERNUM.equals(accountAgeParam.getSearchType())){
				//订单号
				sql.append(" and o.order_no like '%").append(searchValue).append("%' ");
				flag = true;
			}else if(Context.MTOUR_SEARCHTYPE_GROUPCODE.equals(accountAgeParam.getSearchType())){
				//团号
				sql.append(" and p.group_code like '%").append(searchValue).append("%' ");
				flag = true;
			}else if(Context.MTOUR_SEARCHTYPE_AGENTNAME.equals(accountAgeParam.getSearchType())){
				//渠道名称
				sql.append(" and rr.channelName like '%").append(searchValue).append("%' ");
				flag = true;
			}
			sql.append(" ) ");
		}
		boolean receiveFlag = false;
		String receiveSQL = "";
		StringBuffer receiveStr = new StringBuffer();
		receiveStr.append(" exists (")
		          .append("select agentinfo_id, notReceiviedMoney FROM ")
		          .append(" ( select o.agentinfo_id,  ")
		          .append(" 		((SELECT ROUND(sum(ifnull(m.amount * m.exchangerate, 0)), 2) ")
		          .append("				 FROM money_amount m where m.serialNum = o.total_money) - ")
		          .append(" 		ifnull((SELECT ROUND(sum(ifnull(m.amount * m.exchangerate, 0)), 2) FROM money_amount m where ")
		          .append(" 		m.serialNum = o.accounted_money),0)) ")
		          .append(" 		AS notReceiviedMoney  ")
		          .append("   from ")
		          .append(" 	airticket_order o, activity_airticket p ")
		          .append("  where p.id = o.airticket_id ")
		          .append("		and o.del_flag = '0' ")
		          .append("		and o.lockstatus <> 2 ")
		          .append(" 	AND o.lockstatus <> 4 ")
		          .append("     AND p.proCompany =  ").append(companyId)
		          .append("    ) t ")
		          .append("  where t.agentinfo_id = rr.channelUuid ")
		          .append(" and t.notReceiviedMoney > 0 )");
		if(Context.MTOUR_ACCOUTAGE_0.equals(accountAgeParam.getAccountAgeStatus())){
			receiveFlag = true;
			receiveSQL = receiveStr.toString();
			//未结清(该渠道下只要有一个订单的未收金额大于0，则表示未结清)
		}else if(Context.MTOUR_ACCOUTAGE_1.equals(accountAgeParam.getAccountAgeStatus())){
			//已结清(该渠道下所有订单的未收金额都不大于0，则表示已结清)
			receiveFlag = true;
			receiveSQL = " not " + receiveStr.toString();
		}
		str.append(" SELECT                                                                                                                    ")
		   .append(" 	tt.channelName,                                                                                                        ")
		   .append(" 	tt.channelUuid,                                                                                                        ")
		   .append(" 	tt.saleId,                                                                                                             ")
		   .append(" 	tt.salesName,                                                                                                          ")
		   .append(" 	tt.person_num as totalTravelerCount,                                                                                   ")
		   .append(" 	tt.totalMoney,                                                                                                         ")
		   .append(" 	tt.payedMoney,                                                                                                         ")
		   .append(" 	tt.accountedMoney,                                                                                                     ")
		   .append(" 	(tt.totalMoney - IFNULL(tt.accountedMoney,0)) AS notReceiviedMoney                                                     ")
		   .append(" FROM                                                                                                                      ")
		   .append(" 	(                                                                                                                      ")
		   .append(" 		SELECT                                                                                                             ")
		   .append(" 			rr.channelName,                                                                                                ")
		   .append(" 			rr.channelUuid,                                                                                                ")
		   .append(" 			rr.saleId,                                                                                                     ")
		   .append(" 			rr.salesName,                                                                                                  ")
		   .append(" 			sum(rr.person_num) AS person_num,                                                                              ")
		   .append(" 			ROUND(sum(rr.totalMoney),2) AS totalMoney,                                                                     ")
		   .append(" 			ROUND(sum(rr.payedMoney),2) AS payedMoney,                                                                     ")
		   .append(" 			ROUND(sum(rr.accountedMoney),2) AS accountedMoney,                                                             ")
		   .append(" 			ROUND(sum(rr.notReceiviedMoney),2) AS notReceiviedMoney                                                        ")
		   .append(" 		FROM                                                                                                               ")
		   .append(" 			(                                                                                                              ")
		   .append(" 				SELECT                                                                                                     ")
		   .append(" 					bb.agentName AS channelName,                                                                           ")
		   .append(" 					bb.agentId AS channelUuid,                                                                             ")
		   .append(" 					bb.agentSalerId AS saleId,                                                                             ")
		   .append(" 					bb.agentSalerName AS salesName,                                                                        ")
		   .append(" 					bb.person_num AS person_num,                                                                           ")
		   .append(" 					aa.totalMoney,                                                                                         ")
		   .append(" 					aa.payedMoney,                                                                                         ")
		   .append(" 					aa.accountedMoney,                                                                                     ")
		   .append(" 					aa.notReceiviedMoney                                                                                   ")
		   .append(" 				FROM                                                                                                       ")
		   .append(" 					(                                                                                                      ")
		   .append(" 						SELECT                                                                                             ")
		   .append(" 							o.id,                                                                                          ")
		   .append(" 							(SELECT sum(ifnull(m.amount * m.exchangerate, 0)) FROM money_amount m where m.serialNum = o.total_money) AS totalMoney,")
		   .append(" 							(SELECT sum(ifnull(m.amount * m.exchangerate, 0)) FROM money_amount m where m.serialNum = o.payed_money) AS payedMoney,")
		   .append(" 							(SELECT sum(ifnull(m.amount * m.exchangerate, 0)) FROM money_amount m where m.serialNum = o.accounted_money) AS accountedMoney,")
		   .append(" 							((SELECT sum(ifnull(m.amount * m.exchangerate, 0)) FROM money_amount m where m.serialNum = o.total_money) -")
		   .append(" 							ifnull((SELECT sum(ifnull(m.amount * m.exchangerate, 0)) FROM money_amount m where m.serialNum = o.accounted_money),0)) AS notReceiviedMoney ")
		   .append(" 						FROM                                                                                               ")
		   .append(" 							airticket_order o                                                                              ")
		   .append(" 						GROUP BY                                                                                           ")
		   .append(" 							o.id                                                                                           ")
		   .append(" 					) aa, ")
		   .append(" 					(                                                                                                      ")
		   .append(" 						SELECT                                                                                             ")
		   .append(" 							o.id,                                                                                          ")
		   .append(" 							o.order_no AS orderNum,                                                                        ")
		   .append(" 							p.group_code AS groupCode,                                                                     ")
		   .append(" 							o.person_num AS person_num,                                                                    ")
		   .append(" 							ai.id AS agentId,                                                                              ")
		   .append(" 							ai.agentName,                                                                                  ")
		   .append(" 							(SELECT NAME FROM sys_user su WHERE su.id = ai.agentSalerId) AS agentSalerName,                ")
		   .append(" 							ai.agentSalerId                                                                                ")
		   .append(" 						FROM                                                                                               ")
		   .append(" 							airticket_order o,                                                                             ")
		   .append(" 							agentinfo ai ,                                                                                 ")
		   .append(" 						    activity_airticket p                                                                           ")
		   .append(" 					WHERE                                                                                                  ")
		   .append(" 						p.id = o.airticket_id                                                                              ")
		   .append(" 					AND	ai.id = o.agentinfo_id                                                                             ")
		   .append(agentWhere)
		   .append(" 					AND o.del_flag = '0'                                                                                   ")
		   .append(" 					AND o.lockstatus <> 2                                                                                  ")
		   .append("                    AND o.lockstatus <> 4                                                                                  ")
		   .append(" 					AND p.proCompany = ").append(companyId)
		   .append(" 					) bb                                                                                                   ")
		   .append(" 				WHERE                                                                                                      ")
		   .append(" 					aa.id = bb.id                                                                                          ")
		   .append(where.toString())
		   .append(" 			) rr                                                                                                           ");
		   if(flag){//前端页面输入了团号或者渠道名词的条件
			   str.append(" where ").append(sql.toString());
			   if(receiveFlag){//选择了账款未结清或者账款已结清
				   str.append(" and ").append(receiveSQL);
			   }
		   }else if(receiveFlag){//选择了账款未结清或者账款已结清
			   str.append(" where ").append(receiveSQL);
		   }
		str.append(" 		GROUP BY                                                                                                           ")
		   .append(" 			rr.channelName                                                                                                 ")
		   .append(" 	) tt                                                                                                                   ");
		//查询总条数的SQL语句
		String countSQL = "select count(channelName) as count from ( " + str.toString() + " ) as rrr ";
		//查询总条数
		Long count = getCountBySQL(countSQL);
		//页数
		Long pageSize = count/accountAgeParam.getPageCount();
		//余数
		Long residue = count%accountAgeParam.getPageCount();
		//如果输入的当前页数大于最大页数，则显示最大页数内容，Bug：10273
		if(residue == 0){
			if(accountAgeParam.getPageNow()>pageSize && pageSize != 0){
				accountAgeParam.setPageNow(pageSize.intValue());
			}
		}else{
			if(accountAgeParam.getPageNow()>(pageSize + 1)){
				accountAgeParam.setPageNow(pageSize.intValue() + 1);
			}
		}
		String limit = " limit " + (accountAgeParam.getPageNow() -1 )* accountAgeParam.getPageCount()
			 	+ "," + accountAgeParam.getPageCount();
		List<Map<String, Object>> list = findBySql(str.toString() + accountAgeParam.getOrderBy() + limit, Map.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", list);
		map.put("count", count);
		return map;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> getAccountAgeListNotMtour(AccountAgeParam accountAgeParam, Long companyId) {
		StringBuffer str = new StringBuffer();
		StringBuffer where = new StringBuffer();
		String agentWhere = "";
		if("2".equals(accountAgeParam.getChannelType())){//非签约渠道
			agentWhere = " AND ai.is_uncontract = 1 AND ai.supplyId = " + companyId;
		}else if("1".equals(accountAgeParam.getChannelType())){//签约渠道
			agentWhere = " AND ai.is_uncontract IS NULL AND ai.supplyId = " + companyId;
		}
		if(StringUtils.isNotBlank(accountAgeParam.getChannelId())){
			//渠道
			where.append(" and bb.agentId in (").append(accountAgeParam.getChannelId()).append(")");
		}
		if(StringUtils.isNotBlank(accountAgeParam.getSalerId())){
			//跟进销售
			where.append(" and bb.agentSalerId in (").append(accountAgeParam.getSalerId()).append(")");
		}
		String searchValue = null;
		StringBuffer sql = new StringBuffer();
		sql.append(" where exists (")
		   .append(" select ai.agentName from ")
		   .append(" airticket_order o,")
		   .append(" agentinfo ai,")
		   .append(" activity_airticket p ")
		   .append(" WHERE ")
		   .append("     p.id = o.airticket_id")
		   .append(" AND o.agentinfo_id = ai.id ").append(agentWhere)
		   .append(" AND o.del_flag = '0' ")
		   .append(" AND o.lockstatus <> 2 ")
		   .append(" AND p.proCompany =  ").append(companyId)
		   .append(" AND ai.agentName = rr.channelName ");
		boolean flag = false;
		if(StringUtils.isNotBlank(accountAgeParam.getSearchKey())){
			searchValue = accountAgeParam.getSearchKey();
			if(Context.MTOUR_SEARCHTYPE_ORDERNUM.equals(accountAgeParam.getSearchType())){
				//订单号
				sql.append(" and o.order_no like '%").append(searchValue).append("%' ");
				flag = true;
			}else if(Context.MTOUR_SEARCHTYPE_GROUPCODE.equals(accountAgeParam.getSearchType())){
				//团号
				sql.append(" and p.group_code like '%").append(searchValue).append("%' ");
				flag = true;
			}else if(Context.MTOUR_SEARCHTYPE_AGENTNAME.equals(accountAgeParam.getSearchType())){
				//渠道名称
				sql.append(" and rr.channelName like '%").append(searchValue).append("%' ");
				flag = true;
			}
			sql.append(" ) ");
		}
		String reciveOrUnreceive = "";
		if(Context.MTOUR_ACCOUTAGE_0.equals(accountAgeParam.getAccountAgeStatus())){
			//未结清
			reciveOrUnreceive = " where tt.notReceiviedMoney <> '0.00' ";
		}else if(Context.MTOUR_ACCOUTAGE_1.equals(accountAgeParam.getAccountAgeStatus())){
			//已结清
			reciveOrUnreceive = " where tt.notReceiviedMoney = '0.00' ";
		}
		str.append(" SELECT                                                                                                                    ")
		   .append(" 	tt.channelName,                                                                                                        ")
		   .append(" 	tt.channelUuid,                                                                                                        ")
		   .append(" 	tt.saleId,                                                                                                             ")
		   .append(" 	tt.salesName,                                                                                                          ")
		   .append(" 	tt.person_num as totalTravelerCount,                                                                                   ")
		   .append(" 	tt.totalMoney,                                                                                                         ")
		   .append(" 	tt.payedMoney,                                                                                                         ")
		   .append(" 	tt.accountedMoney,                                                                                                     ")
		   .append(" 	tt.notReceiviedMoney                                                                                                   ")
		   .append(" FROM                                                                                                                      ")
		   .append(" 	(                                                                                                                      ")
		   .append(" 		SELECT                                                                                                             ")
		   .append(" 			rr.channelName,                                                                                                ")
		   .append(" 			rr.channelUuid,                                                                                                ")
		   .append(" 			rr.saleId,                                                                                                     ")
		   .append(" 			rr.salesName,                                                                                                  ")
		   .append(" 			sum(rr.person_num) AS person_num,                                                                              ")
		   .append(" 			sum(rr.totalMoney) AS totalMoney,                                                                              ")
		   .append(" 			sum(rr.payedMoney) AS payedMoney,                                                                              ")
		   .append(" 			sum(rr.accountedMoney) AS accountedMoney,                                                                      ")
		   .append(" 			sum(rr.notReceiviedMoney) AS notReceiviedMoney                                                                 ")
		   .append(" 		FROM                                                                                                               ")
		   .append(" 			(                                                                                                              ")
		   .append(" 				SELECT                                                                                                     ")
		   .append(" 					bb.agentName AS channelName,                                                                           ")
		   .append(" 					bb.agentId AS channelUuid,                                                                             ")
		   .append(" 					bb.agentSalerId AS saleId,                                                                             ")
		   .append(" 					bb.agentSalerName AS salesName,                                                                        ")
		   .append(" 					bb.person_num AS person_num,                                                                           ")
		   .append(" 					aa.totalMoney,                                                                                         ")
		   .append(" 					aa.payedMoney,                                                                                         ")
		   .append(" 					aa.accountedMoney,                                                                                     ")
		   .append(" 					aa.notReceiviedMoney                                                                                   ")
		   .append(" 				FROM                                                                                                       ")
		   .append(" 					(                                                                                                      ")
		   .append(" 						SELECT                                                                                             ")
		   .append(" 							o.id,                                                                                          ")
		   .append(" 							(SELECT ROUND(sum(ifnull(m.amount * m.exchangerate, 0)), 2) FROM money_amount m where m.serialNum = o.total_money) AS totalMoney,")
		   .append(" 							(SELECT ROUND(sum(ifnull(m.amount * m.exchangerate, 0)), 2) FROM money_amount m where m.serialNum = o.payed_money) AS payedMoney,")
		   .append(" 							(SELECT ROUND(sum(ifnull(m.amount * m.exchangerate, 0)), 2) FROM money_amount m where m.serialNum = o.accounted_money) AS accountedMoney,")
		   .append(" 							((SELECT ROUND(sum(ifnull(m.amount * m.exchangerate, 0)), 2) FROM money_amount m where m.serialNum = o.total_money) -")
		   .append(" 							ifnull((SELECT ROUND(sum(ifnull(m.amount * m.exchangerate, 0)), 2) FROM money_amount m where m.serialNum = o.accounted_money),0)) AS notReceiviedMoney ")
		   .append(" 						FROM                                                                                               ")
		   .append(" 							airticket_order o                                                                              ")
		   .append(" 						GROUP BY                                                                                           ")
		   .append(" 							o.id                                                                                           ")
		   .append(" 					) aa,                                                                                                  ")
		   .append(" 					(                                                                                                      ")
		   .append(" 						SELECT                                                                                             ")
		   .append(" 							o.id,                                                                                          ")
		   .append(" 							o.order_no AS orderNum,                                                                        ")
		   .append(" 							p.group_code AS groupCode,                                                                     ")
		   .append(" 							o.person_num AS person_num,                                                                    ")
		   .append(" 							ai.id AS agentId,                                                                              ")
		   .append(" 							ai.agentName,                                                                                  ")
		   .append(" 							(SELECT NAME FROM sys_user su WHERE su.id = ai.agentSalerId) AS agentSalerName,                ")
		   .append(" 							ai.agentSalerId                                                                                ")
		   .append(" 						FROM                                                                                               ")
		   .append(" 							airticket_order o,                                                                             ")
		   .append(" 						agentinfo ai ,                                                                                     ")
		   .append(" 						activity_airticket p                                                                               ")
		   .append(" 					WHERE                                                                                                  ")
		   .append(" 						p.id = o.airticket_id                                                                              ")
		   .append(" 					AND	ai.id = o.agentinfo_id                                                                             ")
		   .append(agentWhere)
		   .append(" 					AND o.del_flag = '0'                                                                                   ")
		   .append(" 					AND o.lockstatus <> 2                                                                                  ")
		   .append(" 					AND p.proCompany = ").append(companyId)
		   .append(" 					) bb                                                                                                   ")
		   .append(" 				WHERE                                                                                                      ")
		   .append(" 					aa.id = bb.id                                                                                          ")
		   .append(where.toString())
		   .append(" 			) rr                                                                                                           ");
		   if(flag){
			   str.append(sql.toString());
		   }
		str.append(" 		GROUP BY                                                                                                           ")
		   .append(" 			rr.channelName                                                                                                 ")
		   .append(" 	) tt                                                                                                                   ")
		   .append(reciveOrUnreceive);
		//查询总条数的SQL语句
		String countSQL = "select count(channelName) as count from ( " + str.toString() + " ) as rrr ";
		//查询总条数
		Long count = getCountBySQL(countSQL);
		//页数
		Long pageSize = count/accountAgeParam.getPageCount();
		//余数
		Long residue = count%accountAgeParam.getPageCount();
		//如果输入的当前页数大于最大页数，则显示最大页数内容，Bug：10273
		if(residue == 0){
			if(accountAgeParam.getPageNow()>pageSize && pageSize != 0){
				accountAgeParam.setPageNow(pageSize.intValue());
			}
		}else{
			if(accountAgeParam.getPageNow()>(pageSize + 1)){
				accountAgeParam.setPageNow(pageSize.intValue() + 1);
			}
		}
		String limit = " limit " + (accountAgeParam.getPageNow() -1 )* accountAgeParam.getPageCount()
			 	+ "," + accountAgeParam.getPageCount();
		List<Map<String, Object>> list = findBySql(str.toString() + accountAgeParam.getOrderBy() + limit, Map.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", list);
		map.put("count", count);
		return map;
	}
	
	@Override
	public void lockSettlement(Long orderId) {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE activity_airticket p ")
		   .append(" SET lockStatus = 1 ")
		   .append(" WHERE ")
		   .append(" EXISTS (SELECT airticket_id FROM airticket_order o WHERE o.airticket_id = p.id AND o.id = ? ) ");
		updateBySql(sql.toString(), orderId);
	}
	
	@Override
	public void unlockSettlement(Long orderId) {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE activity_airticket p ")
		   .append(" SET lockStatus = 0 ")
		   .append(" WHERE ")
		   .append(" EXISTS (SELECT airticket_id FROM airticket_order o WHERE o.airticket_id = p.id AND o.id = ? ) ");
		updateBySql(sql.toString(), orderId);
	}
	
	/**
	 * 更新机票订单的付款状态
	 * @Description: 
	 * @param @param orderId
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-25
	 */
	@SuppressWarnings("unchecked")
	public boolean updateOrderRefundFlag(Long orderId) {
		if(orderId == null) {
			return false;
		}
		boolean orderRefundFlag = true;
		
		//获取已提交和待付款的成本记录
		List<CostRecord> costRecords = costRecordDao.getByOrderIdAndOrderType(orderId, Context.ORDER_TYPE_JP);
		if(CollectionUtils.isNotEmpty(costRecords)) {
			orderRefundFlag = false;
		}
		//获取待付款的借款、退款和追加成本记录
		List<AirticketOrderMoneyAmount> moneyAmounts = airticketOrderMoneyAmountDao.getByOrderIdAndStatus(orderId.intValue(),
				AirticketOrderMoneyAmount.STATUS_CONFIRM, AirticketOrderMoneyAmount.PAYSTATUS_WAIT);
 		if(CollectionUtils.isNotEmpty(moneyAmounts)) {
			orderRefundFlag = false;
		}
		
		AirticketOrder airticketOrder = airticketOrderDao.getAirticketOrderById(orderId);
		//订单已全部付款
		if(orderRefundFlag) {
			airticketOrder.setRefundFlag(AirticketOrder.REFUNDFLAG_PAYED);
		//订单未全部付款
		} else {
			airticketOrder.setRefundFlag(AirticketOrder.REFUNDFLAG_NOT_PAYED);
		}
		
		super.updateObj(airticketOrder);
		
		return true;
	}

	@Override
	public Map<String, Object> receiveOrderList(ReceiveOrderListParam param) {
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		StringBuffer sqls = new StringBuffer();
		StringBuffer where = new StringBuffer();
		//团号搜索
		if(Context.MTOUR_SEARCHTYPE_GROUPCODE.equals(param.getSearchType())){
			where.append(" AND p.group_code like '%").append(param.getSearchKey()).append("%' ");
		}else if(Context.MTOUR_SEARCHTYPE_PRODUCTNAME.equals(param.getSearchType())){
			//产品名称搜索
			where.append(" AND p.activity_airticket_name like '%").append(param.getSearchKey()).append("%' ");
		}
		//下单人
		if(StringUtils.isNotBlank(param.getOrderedId())){
			where.append(" AND o.create_by in (").append(param.getOrderedId()).append(") ");
		}
		//订单收款状态
		if(StringUtils.isNotBlank(param.getReceiveStatus())){
			where.append(" AND o.paymentStatus in (").append(param.getReceiveStatus()).append(") ");
		}
		//下单时间
		if(StringUtils.isNotBlank(param.getOrderDateTime())){
			String orderDateTime = param.getOrderDateTime();
			String[] startAndEnd = orderDateTime.split("~");
			if(startAndEnd.length == 1){
				where.append(" AND o.create_date >= '").append(startAndEnd[0]).append(" 00:00:00' ");
			}else{
				if("".equals(startAndEnd[0])){
					where.append(" AND o.create_date <= '").append(startAndEnd[1]).append(" 23:59:59' ");
				}else{
					where.append(" AND o.create_date >= '").append(startAndEnd[0]).append(" 00:00:00' ");
					where.append(" AND o.create_date <= '").append(startAndEnd[1]).append(" 23:59:59' ");
				}
			}
		}
		//出团日期
		if(StringUtils.isNotBlank(param.getDepartureDate())){
			String departureDate = param.getDepartureDate();
			String[] startAndEnd = departureDate.split("~");
			if(startAndEnd.length == 1){
				where.append(" AND o.create_date >= '").append(startAndEnd[0]).append(" 00:00:00' ");
			}else{
				if("".equals(startAndEnd[0])){
					where.append(" AND o.create_date <= '").append(startAndEnd[1]).append(" 23:59:59' ");
				}else{
					where.append(" AND o.create_date >= '").append(startAndEnd[0]).append(" 00:00:00' ");
					where.append(" AND o.create_date <= '").append(startAndEnd[1]).append(" 23:59:59' ");
				}
			}
		}
		sqls.append(" SELECT ")
			.append(" 	o.id AS orderUuid, ")
			.append(" 	p.group_code AS groupNo, ")
			.append(" 	o.create_date AS orderDateTime, ")
			.append(" 	p.startingDate AS departureDate, ")
			.append(" 	(SELECT NAME FROM sys_user u WHERE u.id = o.create_by) AS orderer, ")
			.append(" 	(SELECT sum(amount * IFNULL(ma.exchangerate, 1)) FROM money_amount ma WHERE ma.serialNum = o.total_money) AS totalMoney, ")
			.append(" 	(SELECT sum(amount * IFNULL(ma.exchangerate, 1)) FROM money_amount ma WHERE ma.serialNum = o.accounted_money) AS accountedMoney, ")
			.append(" 	o.paymentStatus ")
			.append(" FROM ")
			.append(" 	activity_airticket p, ")
			.append(" 	airticket_order o ")
			.append(" WHERE ")
			.append(" 	p.id = o.airticket_id ")
			.append(" AND p.delflag = 0 ").append(" AND o.del_flag = 0 ")
			.append(" AND p.proCompany = ").append(currentCompanyId).append(where.toString());
		//查询总条数的SQL语句
		String countSQL = "select count(*) as count from ( " + sqls.toString() + " ) as rrr ";
		//查询总条数
		Long count = getCountBySQL(countSQL);
		//页数
		Long pageSize = count/param.getPageCount();
		//余数
		Long residue = count%param.getPageCount();
		//如果输入的当前页数大于最大页数，则显示最大页数内容，Bug：10273
		if(residue == 0){
			if(param.getPageNow()>pageSize && pageSize != 0){
				param.setPageNow(pageSize.intValue());
			}
		}else{
			if(param.getPageNow()>(pageSize + 1)){
				param.setPageNow(pageSize.intValue() + 1);
			}
		}
		String limit = " limit " + (param.getPageNow() -1 )* param.getPageCount()
				+ "," + param.getPageCount();
		List<Map<String, Object>> list = findBySql(sqls.toString() + param.getOrderBy() + limit, Map.class);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("data", list);
		map.put("count", count);
		return map;
	}
}
