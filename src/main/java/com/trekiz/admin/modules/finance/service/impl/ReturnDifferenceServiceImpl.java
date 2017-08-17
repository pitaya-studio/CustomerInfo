package com.trekiz.admin.modules.finance.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.modules.finance.entity.ReturnDifference;
import com.trekiz.admin.modules.finance.repository.ReturnDifferenceDao;
import com.trekiz.admin.modules.finance.service.ReturnDifferenceService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.ProductOrderService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.MoneyAmountUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
public class ReturnDifferenceServiceImpl extends BaseService implements ReturnDifferenceService{
	@Autowired
	private ReturnDifferenceDao returnDifferenceDao;
	@Autowired
	private ProductOrderService productOrderService;
	@Autowired
	private CurrencyService currencyService;
	/**
	 * 保存差额收款
	 * @author chao.zhang
	 * @time 2016-10-13
	 */
	@Override
	public void saveDifference(ReturnDifference returnDifference) {
		returnDifferenceDao.saveObj(returnDifference);
	}
	
	/**
	 * 根据uuid查询差额
	 */
	@Override
	public ReturnDifference getReturnDifferenceByUuid(String uuid) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT * FROM return_difference WHERE uuid=?");
		List<ReturnDifference> returnDifferences=returnDifferenceDao.findBySql(sbf.toString(), ReturnDifference.class, uuid);
		return returnDifferences.get(0);
	}
	
	/**
	 * 更改达账状态
	 * @param returnDifference
	 */
	@Override
	public void updateToAccountType(ReturnDifference returnDifference) {
		returnDifferenceDao.updateObj(returnDifference);
	}
	
	/**
	 * 获得差额返还列表
	 * @author chao.zhang
	 */
	@Override
	public Page<Map<String, Object>> getReturnPriceDetail(
			Page<Map<String, Object>> page,String input,String agentId,String agentContactId,String payType) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT ")
			 .append("pro.id AS orderId, ")
			 .append("tr.acitivityName, ")
			 .append("ag.groupCode AS groupNum, ")
			 .append("pro.orderNum, ")
			 .append("pro.orderPersonNum AS personNum, ")
			 .append("pro.salerId AS salerId, ")
			 .append("pro.salerName AS saler, ")
			 .append("pro.orderCompany, ")
			 .append("agentinfo.agentName, ")
			 .append("su.contactsName AS agentContactName, ")
			 .append("SUM(rd.return_price) AS returnPrice, ")
			 .append("rd.currency_id AS currencyId, ")
			 .append("c.currency_mark AS returnPriceMark, ")
			 .append("pro.differencePayStatus AS payType, ")
			 .append("ma.amount AS forcastReturnPrice, ")
			 .append("currency.currency_mark AS forcastReturnPriceMark ")
			 .append("FROM ")
			 .append("return_difference rd ")
			 .append("LEFT JOIN productorder pro ON rd.order_id = pro.id ")
			 .append("LEFT JOIN activitygroup ag ON pro.productGroupId = ag.id ")
			 .append("LEFT JOIN travelactivity tr ON tr.id = ag.srcActivityId ")
			 .append("LEFT JOIN agentinfo agentinfo ON agentinfo.id = pro.orderCompany ")
			 .append("LEFT JOIN ordercontacts su ON pro.id = su.orderId ")
			 .append("LEFT JOIN currency c ON c.currency_id = rd.currency_id ")
			 .append("LEFT JOIN money_amount ma ON ma.serialNum = pro.differenceMoney ")
			 .append("LEFT JOIN currency currency ON ma.currencyId = currency.currency_id ")
			 .append("WHERE ")
			 .append("rd.toAccountType = 1 ")
			 .append("AND pro.payStatus NOT IN (99, 111) ")
			 .append("AND pro.delFlag = 0 ")
			 .append("AND pro.differenceFlag = 1 ");
			sbf.append("AND rd.company_id = "+UserUtils.getUser().getCompany().getId()+" ");
		if(StringUtils.isNotBlank(input)){
			sbf.append("AND (tr.acitivityName LIKE '%").append(input).append("%' ")
				.append("OR ag.groupCode LIKE '%").append(input).append("%' ")
				.append("OR pro.orderNum LIKE '%").append(input).append("%' ")
				.append("OR pro.salerName LIKE '%").append(input).append("%') ");
		}
		if(StringUtils.isNotBlank(agentId)){
			sbf.append("AND agentinfo.id = ").append(agentId).append(" ");
		}
		//改为用名称查询
		if(StringUtils.isNotBlank(agentContactId)){
			sbf.append("AND su.contactsName = '").append(agentContactId).append("' ");
		}
		if(StringUtils.isNotBlank(payType)){
			sbf.append("AND pro.differencePayStatus=").append(payType).append(" ");
		}
		sbf.append("GROUP BY ")
			 .append("pro.id ");
		sbf.append("ORDER BY rd.updateDate ");
		page = returnDifferenceDao.findPageBySql(page, sbf.toString(), Map.class);
		return page;
	}
	
	/**
	 * 更改差额状态
	 * @param productOrderCommon
	 */
	@Override
	public void updateDifferencePayStatus(ProductOrderCommon productOrderCommon) {
		returnDifferenceDao.updateObj(productOrderCommon);
		
	}
	
	/**
	 * 根据orderId获得未被驳回的returndifference （求差额的和） 
	 */
	@Override
	public ReturnDifference getDifferenceByOrderId(Integer orderId) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT id,uuid,company_id,order_id,orderNum,orderType,sum(return_price) as return_price,currency_id,toAccountType,toAccountDate,createBy,")
			.append("createDate,updateBy,updateDate,delFlag FROM return_difference where order_id=? and orderType=2 and toAccountType in(0,1) and delFlag = 0");
		List<ReturnDifference> list = returnDifferenceDao.findBySql(sbf.toString(),ReturnDifference.class ,orderId);
		if(list.size() != 0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据订单id获得差额(达账)
	 * @param orderId
	 * @return
	 */
	@Override
	public ReturnDifference getDifferenceSumAccountByOrderId(Integer orderId) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT id,uuid,company_id,order_id,orderNum,orderType,sum(return_price) as return_price,currency_id,toAccountType,toAccountDate,createBy, ");
		sb.append("createDate,updateBy,updateDate,delFlag FROM return_difference where order_id=? and orderType=2 and toAccountType=1 and delFlag = 0");
		List<ReturnDifference> list = returnDifferenceDao.findBySql(sb.toString(), ReturnDifference.class,orderId);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据订单id获得订单总额(减去差额)
	 * @param orderId
	 * @return
	 * @author chao.zhang
	 */
	@Override
	public String getOrderTotalMoneyStrByOrderId(Long orderId) {
		ProductOrderCommon orderCommon = productOrderService.getProductorderById(orderId);
		String strTotalMoney = "";
    	if(orderCommon != null && StringUtils.isNotBlank(orderCommon.getTotalMoney())){
    		
    		if (orderCommon.getDifferenceFlag() == 1 && StringUtils.isNotBlank(orderCommon.getDifferenceMoney())) {
    			// 该订单存在差额，对应币种减去差额后拼接为多币种形式
    			List<MoneyAmount> mas = MoneyAmountUtils.getMoneyAmountsBySerialNum(orderCommon.getTotalMoney());
    			DecimalFormat d= new DecimalFormat(",##0.00");
    			List<String> strTotalMoneyArray = new ArrayList<>();
    			
    			for (MoneyAmount ma : mas) {
    				Currency currency = currencyService.findById(ma.getCurrencyId().longValue());
    				
    				if (ma.getCurrencyId() != null) {
    	    			List<MoneyAmount> moneyAmounts = MoneyAmountUtils.getMoneyAmountsBySerialNum(orderCommon.getDifferenceMoney());
    	    			BigDecimal amount = new BigDecimal(ma.getAmount().toString());
    	    			for (MoneyAmount moneyAmount : moneyAmounts) {
    	    				if (ma.getCurrencyId().toString().equals(moneyAmount.getCurrencyId().toString())) {
    	    					amount = amount.subtract(moneyAmount.getAmount());// 减去差额
    	    				}
    	    			}
    	    			strTotalMoneyArray.add(currency.getCurrencyMark() + d.format(amount));
    				}
    			}
    			strTotalMoney = StringUtils.join(strTotalMoneyArray.toArray(), " + ");	// 多币种用" + "连接
    		} else {
    			// 该订单不存在差额，返回其订单总额
    			strTotalMoney = MoneyAmountUtils.getMoneyAmount(orderCommon.getTotalMoney());
    		}
    	}
    	return strTotalMoney;
	}
	
	/**
	 * 根据订单id获得订单各种金额(减去差额)
	 * @param orderId
	 * @return
	 * @author chao.zhang
	 */
	@Override
	public Map<String, Object> getOrderMoneyByOrderId(Map<String,Object> refundMap) {
		Map<String,Object> map = new HashMap<String, Object>();
		//获得订单
		ProductOrderCommon orderCommon = productOrderService.getProductorderById(Long.parseLong(refundMap.get("id").toString()));
		if(orderCommon != null && orderCommon.getDifferenceFlag() == 1){
			//获得订单总的差额
			MoneyAmount moneyAmount = MoneyAmountUtils.getMoneyAmountByUUID(orderCommon.getDifferenceMoney());
			//获得减去差额后的订单总额（原始订单总额）
			BigDecimal bigDecimal = new BigDecimal(refundMap.get("totalMoney").toString()).subtract(moneyAmount.getAmount());
			map.put("totalMoney", bigDecimal);
			//获得达账的差额的总和
			ReturnDifference returnDifference = this.getDifferenceSumAccountByOrderId(orderCommon.getId().intValue());
			//判断是否为空，若为空减去已达账的差额，若不为空取原来的达账金额
			if(returnDifference == null ){
				BigDecimal decimal = new BigDecimal(refundMap.get("accountedMoney").toString());
				map.put("accountedMoney", decimal);
				map.put("notAccountedMoney", bigDecimal.subtract(decimal));
			}else{
				BigDecimal decimal = new BigDecimal(refundMap.get("accountedMoney").toString()).subtract(returnDifference.getReturnPrice());
				map.put("accountedMoney", decimal);
				map.put("notAccountedMoney", bigDecimal.subtract(decimal));
			}
			return map;
		}
		return null;
	}
	
	/**
	 * 根据serialNum的uuid获得总差额
	 * @param serialNum
	 * @return
	 */
	@Override
	public Map<String,Object> getReturnDifferenceBySerialNum(String serialNum){
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT ")
			 .append("r.currency_id AS currencyId,c.currency_mark AS currencymark,sum(r.return_price) amount ")
             .append("FROM ")
	         .append("orderpay o LEFT JOIN return_difference r ON o.differenceUuid = r.uuid ")
	         .append("LEFT JOIN currency c ON r.currency_id = c.currency_id ")
             .append("WHERE ")
	         .append("o.moneySerialNum = ? ")
	         .append("AND r.toAccountType IN(0,1)");
		List<Map<String,Object>> list  = returnDifferenceDao.findBySql(sbf.toString(),Map.class,serialNum);
		if(list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
}
