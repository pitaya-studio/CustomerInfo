package com.trekiz.admin.agentToOffice.T2.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.agentToOffice.PricingStrategy.repository.PriceStrategyDao;
import com.trekiz.admin.agentToOffice.T2.dao.QuauqServiceFeeDao;
import com.trekiz.admin.agentToOffice.T2.entity.OfficeRate;
import com.trekiz.admin.agentToOffice.T2.entity.Rate;
import com.trekiz.admin.agentToOffice.T2.pojo.CompanyRateItem;
import com.trekiz.admin.agentToOffice.T2.service.QuauqServiceFeeService;
import com.trekiz.admin.agentToOffice.T2.utils.ExportExcelForAgentUtils;
import com.trekiz.admin.agentToOffice.T2.utils.RateUtils;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.Collections3;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.log.entity.QuauqBusinessLog;
import com.trekiz.admin.modules.log.service.QuauqBusinessLogService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.sys.repository.LogOperateDao;
import com.trekiz.admin.modules.sys.repository.OfficeDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class QuauqServiceFeeServiceImpl extends BaseService implements QuauqServiceFeeService {

	@Autowired
	private QuauqServiceFeeDao quauqServiceFeeDao;
	@Autowired
	private LogOperateDao logOperateDao;
	@Autowired
	private PriceStrategyDao priceStrategyDao;
	@Autowired
    private ProductOrderCommonDao productorderDao;
	@Autowired
	private QuauqBusinessLogService quauqBusinessLogService;
	@Autowired
	private OfficeDao officeDao;

	@SuppressWarnings("all")
	public Page<Map<String,Object>> getQuauqServiceFeeStatistics(Page page, boolean groupBy, String officeId) {
		StringBuilder sql = new StringBuilder();
		if(groupBy){
			sql.append(" SELECT GROUP_CONCAT(CONCAT(total.currencyMark,total.totalMoney) SEPARATOR '+') serviceFeeTotalCount, ");
			sql.append(" GROUP_CONCAT(CONCAT(total.currencyMark,total.unPayMoney) SEPARATOR '+') serviceFeeUnsettled, ");
			sql.append(" GROUP_CONCAT(CONCAT(total.currencyMark,total.payedMoney) SEPARATOR '+') serviceFeeSettled ");
		}else{
			sql.append(" SELECT total.id officeId,total.NAME officeName,totalCount.orderNum orderCount, ");
			sql.append(" totalCount.sumOrderPersonNum personCount, ");
			sql.append(" GROUP_CONCAT(CONCAT(total.currencyMark,total.totalMoney) SEPARATOR '+') serviceFeeTotalCount, ");
			sql.append(" GROUP_CONCAT(CONCAT(total.currencyMark,total.unPayMoney) SEPARATOR '+') serviceFeeUnsettled, ");
			sql.append(" GROUP_CONCAT(CONCAT(total.currencyMark,total.payedMoney) SEPARATOR '+') serviceFeeSettled ");
			sql.append(" ,quauqTotal.quauqTotalMoney ");
			sql.append(" ,agentTotal.agentTotalMoney ");
			sql.append(" ,cutTotal.cutTotalMoney ");
		}
		sql.append(" FROM( ");
		sql.append(" SELECT office.id,office.name,COUNT(pro.id) orderNum,SUM(pro.orderPersonNum) orderPersonNum,pro.isPayedCharge, ");
		sql.append(" mao.serialNum,c.currency_name currencyName,c.currency_mark currencyMark, ");
		sql.append(" SUM(CASE pro.isPayedCharge WHEN 1 THEN mao.amount ELSE 0 END) payedMoney, ");
		sql.append(" SUM(CASE pro.isPayedCharge WHEN 0 THEN mao.amount ELSE 0 END) unPayMoney, ");
		sql.append(" SUM(mao.amount) totalMoney ");
		sql.append(" FROM travelactivity activity LEFT JOIN sys_office office ON activity.proCompany = office.id ");
		sql.append(" LEFT JOIN productorder pro ON activity.id = pro.productId ");
		sql.append(" LEFT JOIN money_amount mao ON pro.service_charge = mao.serialNum ");
		sql.append(" LEFT JOIN currency c ON mao.currencyId = c.currency_id ");
		sql.append(" AND mao.moneyType = 31 AND mao.orderType = 2 AND mao.businessType = 1 ");
		sql.append(" where pro.priceType = 2 AND pro.delFlag = 0 AND pro.payStatus < 99 ");
		if(StringUtils.isNotBlank(officeId)){ // 根据批发商查询
			sql.append(" AND office.id =" + officeId);
		}
		if(groupBy){
			sql.append(" GROUP BY c.currency_name) total ");
			return quauqServiceFeeDao.findBySql(page, sql.toString(), Map.class);
		}else{
			sql.append(" GROUP BY office.id,c.currency_name ")
				.append(" ) total ")
				
				// quauq费用查询
				.append(" LEFT JOIN ( ")
				.append(" SELECT quauqCharge.id,GROUP_CONCAT(CONCAT(quauqCharge.currencyMark,quauqCharge.totalMoney) SEPARATOR '+') quauqTotalMoney ")
				.append(" FROM( ")
				.append(" SELECT office.id,c.currency_name,c.currency_mark currencyMark,SUM(mao.amount) totalMoney ")
				.append(" FROM travelactivity activity LEFT JOIN sys_office office ON activity.proCompany = office.id  ")
				.append(" LEFT JOIN productorder pro ON activity.id = pro.productId LEFT JOIN money_amount mao ON pro.quauq_service_charge = mao.serialNum ")
				.append(" LEFT JOIN currency c ON mao.currencyId = c.currency_id ")
				.append(" AND mao.moneyType = 31 AND mao.orderType = 2 AND mao.businessType = 1 ")
				.append(" where pro.delFlag = 0 AND pro.payStatus < 99 AND pro.priceType = 2 ")
				.append(" GROUP BY office.id,c.currency_name) quauqCharge ")
				.append(" GROUP BY id) quauqTotal ")
				.append(" ON total.id = quauqTotal.id ")
				
				// 渠道费用查询
				.append(" LEFT JOIN ( ")
				.append(" SELECT agentCharge.id,GROUP_CONCAT(CONCAT(agentCharge.currencyMark,agentCharge.totalMoney) SEPARATOR '+') agentTotalMoney ")
				.append(" FROM( ")
				.append(" SELECT office.id,c.currency_name,c.currency_mark currencyMark,SUM(mao.amount) totalMoney ")
				.append(" FROM travelactivity activity LEFT JOIN sys_office office ON activity.proCompany = office.id  ")
				.append(" LEFT JOIN productorder pro ON activity.id = pro.productId LEFT JOIN money_amount mao ON pro.partner_service_charge = mao.serialNum ")
				.append(" LEFT JOIN currency c ON mao.currencyId = c.currency_id ")
				.append(" AND mao.moneyType = 31 AND mao.orderType = 2 AND mao.businessType = 1 ")
				.append(" where pro.delFlag = 0 AND pro.payStatus < 99 AND pro.priceType = 2 ")
				.append(" GROUP BY office.id,c.currency_name) agentCharge ")
				.append(" GROUP BY id) agentTotal ")
				.append(" ON total.id = agentTotal.id ")
				
				// 渠道费用查询
				.append(" LEFT JOIN ( ")
				.append(" SELECT cutCharge.id,GROUP_CONCAT(CONCAT(cutCharge.currencyMark,cutCharge.totalMoney) SEPARATOR '+') cutTotalMoney ")
				.append(" FROM( ")
				.append(" SELECT office.id,c.currency_name,c.currency_mark currencyMark,SUM(mao.amount) totalMoney ")
				.append(" FROM travelactivity activity LEFT JOIN sys_office office ON activity.proCompany = office.id  ")
				.append(" LEFT JOIN productorder pro ON activity.id = pro.productId LEFT JOIN money_amount mao ON pro.cut_service_charge = mao.serialNum ")
				.append(" LEFT JOIN currency c ON mao.currencyId = c.currency_id ")
				.append(" AND mao.moneyType = 31 AND mao.orderType = 2 AND mao.businessType = 1 ")
				.append(" where pro.delFlag = 0 AND pro.payStatus < 99 AND pro.priceType = 2 ")
				.append(" GROUP BY office.id,c.currency_name) cutCharge ")
				.append(" GROUP BY id) cutTotal ")
				.append(" ON total.id = cutTotal.id ")
				
				.append(" LEFT JOIN ( ")
				.append(" SELECT office.id,COUNT(pro.id) orderNum,SUM(pro.orderPersonNum) sumOrderPersonNum ")
				.append(" FROM travelactivity activity LEFT JOIN sys_office office ON activity.proCompany = office.id ")
				.append(" LEFT JOIN productorder pro ON activity.id = pro.productId ")
				.append(" WHERE pro.priceType = 2 AND pro.delFlag = 0 AND pro.payStatus < 99 AND pro.orderStatus = 2 ")
				.append(" GROUP BY office.id ")
				.append(" ) totalCount ON total.id = totalCount.id ")
				.append(" GROUP BY officeId ");
			return quauqServiceFeeDao.findBySql(page, sql.toString(), Map.class);
		}
		
	}
	
	/**
	 * 批发商QUAUQ服务费的总计
	 */
	@Override
	public String findSum2QuauqCharge(String officeId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT GROUP_CONCAT( ");
		sql.append(" CONCAT(quauqCharge.currencyMark,quauqCharge.totalMoney) SEPARATOR '+') quauqTotalMoney ");
		sql.append(" FROM( ");
		sql.append(" SELECT office.id,c.currency_name currencyName,c.currency_mark currencyMark,SUM(mao.amount) totalMoney ");
		sql.append(" FROM travelactivity activity LEFT JOIN sys_office office ON activity.proCompany = office.id LEFT JOIN productorder pro ON activity.id = pro.productId ");
		sql.append(" LEFT JOIN money_amount mao ON pro.quauq_service_charge = mao.serialNum LEFT JOIN currency c ON mao.currencyId = c.currency_id ");
		sql.append(" WHERE mao.moneyType = 31 AND mao.orderType = 2 AND mao.businessType = 1 AND pro.priceType = 2 ");
		if(StringUtils.isNotBlank(officeId)){ // 根据批发商查询
			sql.append(" AND office.id =" + officeId);
		}
		sql.append(" AND pro.delFlag = 0 AND pro.payStatus < 99 GROUP BY c.currency_name) quauqCharge ");
		List<?> list = quauqServiceFeeDao.findBySql(sql.toString());
		if(list.size() > 0){
			return StringUtils.blankReturnEmpty(list.get(0));
		}else{
			return "";
		}
	}
	
	/**
	 * 批发商渠道服务费的总计
	 */
	@Override
	public String findSum2AgentCharge(String officeId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT GROUP_CONCAT( ");
		sql.append(" CONCAT(agentCharge.currencyMark,agentCharge.totalMoney) SEPARATOR '+') agentTotalMoney ");
		sql.append(" FROM( ");
		sql.append(" SELECT office.id,c.currency_name currencyName,c.currency_mark currencyMark,SUM(mao.amount) totalMoney ");
		sql.append(" FROM travelactivity activity LEFT JOIN sys_office office ON activity.proCompany = office.id LEFT JOIN productorder pro ON activity.id = pro.productId ");
		sql.append(" LEFT JOIN money_amount mao ON pro.partner_service_charge = mao.serialNum LEFT JOIN currency c ON mao.currencyId = c.currency_id ");
		sql.append(" WHERE mao.moneyType = 31 AND mao.orderType = 2 AND mao.businessType = 1 AND pro.priceType = 2 ");
		if(StringUtils.isNotBlank(officeId)){ // 根据批发商查询
			sql.append(" AND office.id =" + officeId);
		}
		sql.append(" AND pro.delFlag = 0 AND pro.payStatus < 99 GROUP BY c.currency_name) agentCharge ");
		List<?> list = quauqServiceFeeDao.findBySql(sql.toString());
		if(list.size() > 0){
			return StringUtils.blankReturnEmpty(list.get(0));
		}else{
			return "";
		}
	}
	
	/**
	 * 批发商抽成服务费的总计
	 */
	@Override
	public String findSum2CutCharge(String officeId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT GROUP_CONCAT( ");
		sql.append(" CONCAT(cutCharge.currencyMark,cutCharge.totalMoney) SEPARATOR '+') cutTotalMoney ");
		sql.append(" FROM( ");
		sql.append(" SELECT office.id,c.currency_name currencyName,c.currency_mark currencyMark,SUM(mao.amount) totalMoney ");
		sql.append(" FROM travelactivity activity LEFT JOIN sys_office office ON activity.proCompany = office.id LEFT JOIN productorder pro ON activity.id = pro.productId ");
		sql.append(" LEFT JOIN money_amount mao ON pro.cut_service_charge = mao.serialNum LEFT JOIN currency c ON mao.currencyId = c.currency_id ");
		sql.append(" WHERE mao.moneyType = 31 AND mao.orderType = 2 AND mao.businessType = 1 AND pro.priceType = 2 ");
		if(StringUtils.isNotBlank(officeId)){ // 根据批发商查询
			sql.append(" AND office.id =" + officeId);
		}
		sql.append(" AND pro.delFlag = 0 AND pro.payStatus < 99 GROUP BY c.currency_name) cutCharge ");
		List<?> list = quauqServiceFeeDao.findBySql(sql.toString());
		if(list.size() > 0){
			return StringUtils.blankReturnEmpty(list.get(0));
		}else{
			return "";
		}
	}
	
	/**
	 * 批发商订单数和人数总计
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> findSum2OrderAndPersonCount(){
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT office.id,COUNT(pro.id) orderCount,SUM(pro.orderPersonNum) personCount ")
			.append(" FROM travelactivity activity LEFT JOIN sys_office office ON activity.proCompany = office.id ")
			.append(" LEFT JOIN productorder pro ON activity.id = pro.productId ")
			.append(" WHERE pro.priceType = 2 ")
			.append(" AND pro.delFlag = 0 AND pro.payStatus < 99 AND pro.orderStatus = 2 ");
		
		List<Map<String, Object>> list = quauqServiceFeeDao.findBySql(sb.toString(), Map.class);
		
		return list.get(0);
	}
	
	
	/**
	 * @Description quauq后台交易明细
	 * @Date 2016-6-16
	 */
	@Override
	public Page<Map<Object, Object>> quauqTradeDetail(Page<Map<Object, Object>> page, Map<String, String> mapRequest) {
		
		// 获取查询条件
		String where = getTradeDetailWhere(mapRequest);
		
		// 获取查询语句
		String sql = getTradeDetailSql(where , mapRequest.get("officeId"));
		
		// 数据查询
		Page<Map<Object, Object>> pageMap = priceStrategyDao.findPageBySql(page, sql, Map.class);
		
		// 查询订金应收、quauq费率、渠道费率
		selectMoney(pageMap);
		
		return pageMap;
	}
	
	/**
	 * 查询订单金额：订金应收、quauq费率、渠道费率
	 * @param pageMap
	 * @author yakun.bai
	 * @Date 2016-11-11
	 */
	private void selectMoney(Page<Map<Object, Object>> pageMap) {
		if (null != pageMap) {
			List<Map<Object, Object>> list = pageMap.getList();
			// 判断是否为空
			if (CollectionUtils.isNotEmpty(list)) {
				// 订单应收sql、quauq费率sql、渠道费率sql
				String totalInSql = "(";
				String quauqChargeInSql = "(";
				String agentChargeInSql = "(";
				for (int i = 0; i < list.size(); i++) {
					Map<Object, Object> map = list.get(i);
					String totalMoney = map.get("totalMoney").toString();
					Object quauqChargeMoney = map.get("quauqChargeMoney");
					Object agentChargeMoney = map.get("agentChargeMoney");
					
					totalInSql += "'" + totalMoney + "',";
					if (null != quauqChargeMoney) {
						quauqChargeInSql += "'" + quauqChargeMoney.toString() + "',";
					}
					if (null != agentChargeMoney) {
						agentChargeInSql += "'" + agentChargeMoney.toString() + "',";
					}
					
					if (i == list.size() -1) {
						totalInSql = totalInSql.substring(0, totalInSql.length() -1);
						quauqChargeInSql = quauqChargeInSql.substring(0, quauqChargeInSql.length() -1);
						agentChargeInSql = agentChargeInSql.substring(0, agentChargeInSql.length() -1);
					}
				}
				totalInSql += ")";
				quauqChargeInSql += ")";
				agentChargeInSql += ")";
				
				// 订单应收金额查询
				StringBuffer totalMoneysql = new StringBuffer();
				totalMoneysql.append(" SELECT mao.uid orderId, ")
				.append(" GROUP_CONCAT( ")
				.append(" CONCAT(c.currency_mark,' ',mao.amount) ")
				.append(" ORDER BY mao.currencyId SEPARATOR '+' ")
				.append(" ) moneyStr ")
				.append(" FROM money_amount mao ")
				.append(" LEFT JOIN currency c ON mao.currencyId = c.currency_id ")
				.append(" WHERE serialNum IN " + totalInSql)
				.append(" GROUP BY mao.serialNum ");
				List<Map<Object, Object>> totalMoneyList = priceStrategyDao.findBySql(totalMoneysql.toString(), Map.class);
				
				// quauq费率金额查询
				StringBuffer quauqChargeMoneysql = new StringBuffer();
				quauqChargeMoneysql.append(" SELECT mao.uid orderId, ")
				.append(" GROUP_CONCAT( ")
				.append(" CONCAT(c.currency_mark,' ',mao.amount) ")
				.append(" ORDER BY mao.currencyId SEPARATOR '+' ")
				.append(" ) moneyStr ")
				.append(" FROM money_amount mao ")
				.append(" LEFT JOIN currency c ON mao.currencyId = c.currency_id ")
				.append(" WHERE serialNum IN " + quauqChargeInSql)
				.append(" GROUP BY mao.serialNum ");
				List<Map<Object, Object>> quauqChargeMoneyList = priceStrategyDao.findBySql(quauqChargeMoneysql.toString(), Map.class);
				
				// 渠道费率金额查询
				StringBuffer agentChargeMoneysql = new StringBuffer();
				agentChargeMoneysql.append(" SELECT mao.uid orderId, ")
				.append(" GROUP_CONCAT( ")
				.append(" CONCAT(c.currency_mark,' ',mao.amount) ")
				.append(" ORDER BY mao.currencyId SEPARATOR '+' ")
				.append(" ) moneyStr ")
				.append(" FROM money_amount mao ")
				.append(" LEFT JOIN currency c ON mao.currencyId = c.currency_id ")
				.append(" WHERE serialNum IN " + agentChargeInSql)
				.append(" GROUP BY mao.serialNum ");
				
				List<Map<Object, Object>> agentChargeMoneyList = null;
				if (agentChargeInSql.length() > 2) {
					agentChargeMoneyList = priceStrategyDao.findBySql(agentChargeMoneysql.toString(), Map.class);
				}
				
				// 查询数据循环：替换原有应收、quauq费率、渠道费率
				for (Map<Object, Object> map : list) {
					int flag1 = 0;
					for (Map<Object, Object> totalMoneyMap : totalMoneyList) {
						if (map.get("orderId").toString().equals(totalMoneyMap.get("orderId").toString())) {
							map.put("totalMoney", totalMoneyMap.get("moneyStr"));
							flag1 = 1;
							break;
						}
					}
					if(flag1 == 0){ // 此时,订单总额无数据,则设为0 forbug 16792
						map.put("totalMoney", "¥ 0.00");
					}
					int flag2 = 0;
					for (Map<Object, Object> quauqChargeMoneyMap : quauqChargeMoneyList) {
						if (map.get("orderId").toString().equals(quauqChargeMoneyMap.get("orderId").toString())) {
							map.put("quauqChargeMoney", quauqChargeMoneyMap.get("moneyStr"));
							flag2 = 1;
							break;
						}
					}
					if(flag2 == 0){ // 此时,订单quauq服务费无数据,则设为0 forbug 16792
						map.put("quauqChargeMoney", "¥ 0.00");
					}
					if (CollectionUtils.isNotEmpty(agentChargeMoneyList)) {
						int flag3 = 0;
						for (Map<Object, Object> agentChargeMoneyMap : agentChargeMoneyList) {
							if (map.get("orderId").toString().equals(agentChargeMoneyMap.get("orderId").toString())) {
								map.put("agentChargeMoney", agentChargeMoneyMap.get("moneyStr"));
								flag3 = 1;
								break;
							}
						}
						if(flag3 == 0){ // 此时,订单渠道服务费无数据,则设为0 forbug 16792
							map.put("agentChargeMoney", "¥ 0.00");
						}
					}
				}
			}
		}
	}
	
	@Override
	public Map<String, Object> findSum2Office(Map<String, String> mapRequest) {
		// 订单人数
		String personCountSql = getPersonCountSql(mapRequest);
		// 根据currencyName分组
		mapRequest.put("groupBy", "groupBy");
		// 订单总额
		String totalMoneySql = createMoneySQL(mapRequest,"orderTotalMoney");
		// 交易服务费总额
		String chargeTotalMoneySql = createMoneySQL(mapRequest,"chargeMoney");
		// quauq交易服务费
		String quauqChargeTotalMoneySql = createMoneySQL(mapRequest,"quauqChargeMoney");
		// 渠道交易服务费
		String agentChargeTotalMoneySql = createMoneySQL(mapRequest,"agentChargeMoney");
		// 抽成交易服务费
		String cutChargeTotalMoneySql = createMoneySQL(mapRequest,"cutChargeMoney");
		
		List<String> totalMoney = priceStrategyDao.findBySql(totalMoneySql);
		List<Map<String, Object>> chargeTotalMoney = priceStrategyDao.findBySql(chargeTotalMoneySql, Map.class);
		List<Map<String, Object>> quauqChargeTotalMoney = priceStrategyDao.findBySql(quauqChargeTotalMoneySql);
		List<String> agentChargeTotalMoney = priceStrategyDao.findBySql(agentChargeTotalMoneySql);
		List<String> cutChargeTotalMoney = priceStrategyDao.findBySql(cutChargeTotalMoneySql);
		List<BigDecimal> personCount = priceStrategyDao.findBySql(personCountSql); 
		Map<String, Object> map = null;
		if(totalMoney.size() > 0 && agentChargeTotalMoney.size() > 0 && chargeTotalMoney.size() > 0 && personCount.size() > 0){
			map = chargeTotalMoney.get(0);
			map.put("orderTotalMoney", totalMoney.get(0));
			map.put("quauqTotalCharge", quauqChargeTotalMoney.get(0));
			map.put("agentTotalCharge", agentChargeTotalMoney.get(0));
			map.put("cutTotalCharge", cutChargeTotalMoney.get(0));
			map.put("personCount", personCount.get(0));
		}
		return map;
	}
	
	public String getPersonCountSql(Map<String, String> mapRequest){
		StringBuilder sql = new StringBuilder();
		mapRequest.put("personCount", "personCount");
		sql.append(" SELECT SUM(pro.orderPersonNum) AS personCount ")
			.append(" from productorder pro LEFT JOIN travelactivity activity ON pro.productId = activity.id ")
			.append(" LEFT JOIN activitygroup agp ON pro.productGroupId = agp.id ")
			.append(" LEFT JOIN sys_office office ON activity.proCompany = office.id ")
			//正常的订单
			.append(" WHERE pro.delFlag = " + ProductOrderCommon.DEL_FLAG_NORMAL )
			.append(" AND pro.payStatus < 99 ")
			.append(" AND pro.priceType = 2 ")
			
			.append(getTradeDetailWhere(mapRequest));
		return sql.toString();
	}
	
	public String createMoneySQL(Map<String, String> mapRequest, String type){
		StringBuilder sql = new StringBuilder();
		if("orderTotalMoney".equals(type)){  // 查询单个批发商订单总额
			sql.append(" SELECT GROUP_CONCAT(sumMoney.currencyMark,FORMAT(sumMoney.totalMoney, 2) SEPARATOR '+') totalMoney ");
			sql.append(" FROM( ");
			sql.append(" SELECT SUM(mao.amount) totalMoney, ");
		}else if("quauqChargeMoney".equals(type)){ // 查询单个批发商渠道服务费
			sql.append(" SELECT GROUP_CONCAT(sumMoney.currencyMark,FORMAT(sumMoney.totalCharge, 2) SEPARATOR '+') quauqChargeMoney ");
			sql.append(" FROM( ");
			sql.append(" SELECT SUM(mao.amount) totalCharge, ");
		}else if("agentChargeMoney".equals(type)){ // 查询单个批发商渠道服务费
			sql.append(" SELECT GROUP_CONCAT(sumMoney.currencyMark,FORMAT(sumMoney.totalCharge, 2) SEPARATOR '+') agentTotalCharge ");
			sql.append(" FROM( ");
			sql.append(" SELECT SUM(mao.amount) totalCharge, ");
		}else if("cutChargeMoney".equals(type)){ // 查询单个批发商渠道服务费
			sql.append(" SELECT GROUP_CONCAT(sumMoney.currencyMark,FORMAT(sumMoney.totalCharge, 2) SEPARATOR '+') cutTotalCharge ");
			sql.append(" FROM( ");
			sql.append(" SELECT SUM(mao.amount) totalCharge, ");
		}else{  // 查询单个批发商服务费总额
			sql.append(" SELECT GROUP_CONCAT(sumMoney.currencyMark,FORMAT(sumMoney.allCharge, 2) SEPARATOR '+') allCharge, ");
			sql.append(" GROUP_CONCAT(sumMoney.currencyMark,FORMAT(sumMoney.payedCharge, 2) SEPARATOR '+') payedCharge, ");
			sql.append(" GROUP_CONCAT(sumMoney.currencyMark,FORMAT(sumMoney.unpayedCharge, 2) SEPARATOR '+') unpayedCharge ");
			sql.append(" FROM( ");
			sql.append(" SELECT SUM(CASE pro.isPayedCharge WHEN 1 THEN mao.amount ELSE 0 END) payedCharge, ");
			sql.append(" SUM(CASE pro.isPayedCharge WHEN 0 THEN mao.amount ELSE 0 END) unpayedCharge, ");
			sql.append(" SUM(mao.amount) allCharge, ");
		}	
		sql.append(" cu.currency_name currencyName, ");
		sql.append(" cu.currency_mark currencyMark ");
		sql.append(" FROM ");
		sql.append(" productorder pro LEFT JOIN travelactivity activity ON activity.id = pro.productId ");
		sql.append(" LEFT JOIN sys_office office ON activity.proCompany = office.id ");
		sql.append(" LEFT JOIN activitygroup agp ON pro.productGroupId = agp.id ");
//		sql.append(" LEFT JOIN agentinfo agent ON pro.orderCompany = agent.id ");
		if("orderTotalMoney".equals(type)){
			sql.append(" LEFT JOIN money_amount mao ON mao.serialNum = pro.total_money ");
		}else if("chargeMoney".equals(type)){
			sql.append(" LEFT JOIN money_amount mao ON mao.serialNum = pro.service_charge ");
		}else if("agentChargeMoney".equals(type)){
			sql.append(" LEFT JOIN money_amount mao ON mao.serialNum = pro.partner_service_charge ");
		}else if("quauqChargeMoney".equals(type)){
			sql.append(" LEFT JOIN money_amount mao ON mao.serialNum = pro.quauq_service_charge ");
		}else{
			sql.append(" LEFT JOIN money_amount mao ON mao.serialNum = pro.cut_service_charge ");
		}
		sql.append(" LEFT JOIN currency cu ON mao.currencyId = cu.currency_id ");
		if("orderTotalMoney".equals(type)){
			sql.append(" where mao.moneyType = 13 ");
		}else{
			sql.append(" where mao.moneyType = 31 ");
		}
		sql.append(" AND pro.delFlag = 0 AND pro.payStatus < 99 AND pro.priceType = 2 AND mao.orderType = 2 AND mao.businessType = 1 ");
		sql.append(getTradeDetailWhere(mapRequest));
		sql.append(" ) sumMoney ");
		return sql.toString();
	}
		
	/**
     * @Description 交易明细查询sql
     * @Date 2016-6-16
     */
    @SuppressWarnings("all")
	private String getTradeDetailSql(String where, String officeId) {
    	StringBuilder sql = new StringBuilder();
    	sql.append(" SELECT pro.id orderId,office.name officeName, pro.delFlag delFlag, agent.agentName agentName, pro.createDate createDate, pro.orderNum orderNum, ")
					.append("pro.orderPersonNum orderPersonNum, pro.payStatus orderStatus, agp.groupCode groupCode, ")
					.append("pro.salerName salename, pro.isPayedCharge, agp.groupOpenDate, ")
					.append("pro.total_money totalMoney, pro.quauq_service_charge quauqChargeMoney, pro.partner_service_charge agentChargeMoney, ")
					.append("chargeOuter.moneyStr chargeMoney, cutChargeOuter.moneyStr cutChargeMoney ")
			    
    				.append(" FROM productorder pro LEFT JOIN agentinfo agent ON pro.orderCompany = agent.id ")
			    	.append(" LEFT JOIN activitygroup agp ON pro.productGroupId = agp.id ")
			    	.append(" LEFT JOIN travelactivity activity ON pro.productId = activity.id ")
			    	.append(" LEFT JOIN sys_office office ON activity.proCompany = office.id ")
			    	
			    	// 订单服务费总额查询
					.append(" LEFT JOIN ( ")
					.append(" SELECT")
					.append(" mao.serialNum,")
					.append(" GROUP_CONCAT( ")
					.append(" CONCAT(c.currency_mark,' ',mao.amount) ")
					.append(" ORDER BY mao.currencyId SEPARATOR '+' ")
					.append(" ) moneyStr")
					.append(" FROM ")
					.append(" money_amount mao")
					.append(" LEFT JOIN currency c ON mao.currencyId = c.currency_id")
					.append(" WHERE")
					.append(" mao.moneyType = " + Context.MONEY_TYPE_CHARGE)
					.append(" AND mao.orderType = " + Context.ORDER_TYPE_SP)
					.append(" AND mao.businessType = " + Context.MONEY_BUSINESSTYPE_ORDER);
					if(StringUtils.isNotBlank(officeId)){
			    		sql.append(" AND c.create_company_id = " + officeId );
			    	}
					sql.append(" GROUP BY")
					.append(" mao.serialNum")
					.append(") chargeOuter ON chargeOuter.serialNum = pro.service_charge ")
    	
					// 订单quauq服务费查询
					.append(" LEFT JOIN ( ")
					.append(" SELECT")
					.append(" mao.serialNum,")
					.append(" GROUP_CONCAT( ")
					.append(" CONCAT(c.currency_mark,' ',mao.amount) ")
					.append(" ORDER BY mao.currencyId SEPARATOR '+' ")
					.append(" ) moneyStr")
					.append(" FROM ")
					.append(" money_amount mao")
					.append(" LEFT JOIN currency c ON mao.currencyId = c.currency_id")
					.append(" WHERE")
					.append(" mao.moneyType = " + Context.MONEY_TYPE_CHARGE)
					.append(" AND mao.orderType = " + Context.ORDER_TYPE_SP)
					.append(" AND mao.businessType = " + Context.MONEY_BUSINESSTYPE_ORDER);
					if(StringUtils.isNotBlank(officeId)){
			    		sql.append(" AND c.create_company_id = " + officeId );
			    	}
					sql.append(" GROUP BY")
					.append(" mao.serialNum")
					.append(") quauqChargeOuter ON quauqChargeOuter.serialNum = pro.quauq_service_charge ")
					
					// 订单渠道服务费查询
					.append(" LEFT JOIN ( ")
					.append(" SELECT")
					.append(" mao.serialNum,")
					.append(" GROUP_CONCAT( ")
					.append(" CONCAT(c.currency_mark,' ',mao.amount) ")
					.append(" ORDER BY mao.currencyId SEPARATOR '+' ")
					.append(" ) moneyStr")
					.append(" FROM ")
					.append(" money_amount mao")
					.append(" LEFT JOIN currency c ON mao.currencyId = c.currency_id")
					.append(" WHERE")
					.append(" mao.moneyType = " + Context.MONEY_TYPE_CHARGE)
					.append(" AND mao.orderType = " + Context.ORDER_TYPE_SP)
					.append(" AND mao.businessType = " + Context.MONEY_BUSINESSTYPE_ORDER);
					if(StringUtils.isNotBlank(officeId)){
			    		sql.append(" AND c.create_company_id = " + officeId );
			    	}
					sql.append(" GROUP BY")
					.append(" mao.serialNum")
					.append(") agentChargeOuter ON agentChargeOuter.serialNum = pro.partner_service_charge ")
					
					// 订单抽成服务费查询
					.append(" LEFT JOIN ( ")
					.append(" SELECT")
					.append(" mao.serialNum,")
					.append(" GROUP_CONCAT( ")
					.append(" CONCAT(c.currency_mark,' ',mao.amount) ")
					.append(" ORDER BY mao.currencyId SEPARATOR '+' ")
					.append(" ) moneyStr")
					.append(" FROM ")
					.append(" money_amount mao")
					.append(" LEFT JOIN currency c ON mao.currencyId = c.currency_id")
					.append(" WHERE")
					.append(" mao.moneyType = " + Context.MONEY_TYPE_CHARGE)
					.append(" AND mao.orderType = " + Context.ORDER_TYPE_SP)
					.append(" AND mao.businessType = " + Context.MONEY_BUSINESSTYPE_ORDER);
					if(StringUtils.isNotBlank(officeId)){
			    		sql.append(" AND c.create_company_id = " + officeId );
			    	}
					sql.append(" GROUP BY")
					.append(" mao.serialNum")
					.append(") cutChargeOuter ON cutChargeOuter.serialNum = pro.cut_service_charge ")
				
					//正常的订单
				.append(" WHERE pro.delFlag = " + ProductOrderCommon.DEL_FLAG_NORMAL )
					.append(" AND pro.payStatus < 99 ")
					.append(" AND pro.priceType = 2 ")
					.append(where);
    	return sql.toString();
    }
    
    /**
     * @Description 获取查询条件针对交易明细
     * @Date 2016-6-16
     */
    public String getTradeDetailWhere(Map<String,String> map) {
    	
        StringBuffer sqlWhere = new StringBuffer("");
        
        // 订单种类
        sqlWhere.append(" and pro.orderStatus = " + Context.ORDER_STATUS_LOOSE);
        
        // 供应商id
      /*  String officeId = map.get("officeId");
        if(StringUtils.isNotBlank(officeId)){
        	sqlWhere.append(" and office.id = " + officeId + " ");
        }*/
        String officeId = map.get("officeIds");
        if(officeId == null ){
        	officeId = map.get("officeId");
        }
        if(StringUtils.isNotBlank(officeId)){
        	sqlWhere.append(" and office.id = " + officeId + " ");
        }
        
        
        // 渠道
        String agentId = map.get("agentId");
        if (StringUtils.isNotBlank(agentId)){
        	sqlWhere.append(" AND pro.orderCompany = " + agentId + " ");
        }
        
        // 订单状态
        String orderStatus = map.get("orderStatus");
        if (StringUtils.isNotBlank(orderStatus)){
        	sqlWhere.append(" and pro.payStatus = " + orderStatus + " ");
        }
        
        // 订单创建时间
        String orderTimeBegin = map.get("orderTimeBegin");
        String orderTimeEnd = map.get("orderTimeEnd");
        if (StringUtils.isNotBlank(orderTimeBegin)) {
            sqlWhere.append( " and pro.orderTime >= '" + orderTimeBegin + " 00:00:00" + "'");
        }
        if (StringUtils.isNotBlank(orderTimeEnd)) {
            sqlWhere.append( " and pro.orderTime <= '" + orderTimeEnd + " 23:59:59" + "'");
        }
        //出团日期
        String groupTimeBegin = map.get("groupTimeBegin");
        String groupTimeEnd = map.get("groupTimeEnd");
        if (StringUtils.isNotBlank(groupTimeBegin)) {
            sqlWhere.append( " and agp.groupOpenDate >= '" + groupTimeBegin + " 00:00:00" + "'");
        }
        if (StringUtils.isNotBlank(groupTimeEnd)) {
            sqlWhere.append( " and agp.groupOpenDate <= '" + groupTimeEnd + " 23:59:59" + "'");
        }
        
        // 订单号
        String orderNum = map.get("orderNum");
        if (StringUtils.isNotBlank(orderNum)){
        	sqlWhere.append(" and pro.orderNum like '%" + orderNum + "%' ");
        }
        
        // 缴费状态
        String isPayedCharge = map.get("isPayedCharge");
        if (StringUtils.isNotBlank(isPayedCharge)) {
        	sqlWhere.append(" and pro.isPayedCharge = " + isPayedCharge + " ");
        }
        
        // 团号
        String groupCode = map.get("groupCode");
        if (StringUtils.isNotBlank(groupCode)){
        	sqlWhere.append(" and agp.groupCode like '%" + groupCode + "%' ");
        }
        
        // 是否根据currency_name分组   查总计那边分组
        if("groupBy".equals(map.get("groupBy"))){
        	sqlWhere.append(" group by currencyName ");
        }else if("personCount".equals(map.get("personCount"))){ // 查询总人数不用排序或者分组

        }else{ // 查列表根据createDate排序
        	sqlWhere.append(" order by pro.createDate DESC ");
        }
        
        return sqlWhere.toString();
    }
    
    /**
	 * @Description 设置改变缴费状态
	 * @Date 2016-6-17
	 */
    @Override
    public void changeOrderIsPayedCharge(String orderIds,Integer changeType){
    	String[] orderList = orderIds.split(",");
    	for (String id : orderList) {
    		 productorderDao.changeOrderIsPayedCharge(Long.parseLong(id), changeType);
		}
	}

	@Override
	public net.sf.json.JSONArray getPayPriceLogData(String orderType, String orderId) {
		
		ProductOrderCommon order = productorderDao.getById(Long.parseLong(orderId));
		
		// 查询订单结算价操作记录 。主要信息：id、操作人 id name、操作功能、操作时间、操作内容
		List<Map<String, Object>> subInfos = logOperateDao.getPayPriceLog(order.getCreateBy().getCompany().getId(), orderType, orderId);
		// 转化 list 为 jsonarray
		net.sf.json.JSONArray jsonArray = new net.sf.json.JSONArray();
		for (Map<String, Object> map : subInfos) {
			net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(map);
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	/**
	 * 查询批发商的默认费率值。然后对查询出来的原始数据值进行处理。根据费率类型，来转换成最终送显的值。
	 * @author yudong.xu 2016.8.10
     */
	@Override
	public void getOfficeRate(Page<Map<Object, Object>> page, Map<String,String> mapRequest) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT o.id AS companyId,o.uuid AS companyUuid,o.`name` AS companyName,")
		.append("t1.quauqRateType1,t1.quauqRate1,t1.quauqOtherRateType1,t1.quauqOtherRate1,")
		.append("t1.agentRateType1,t1.agentRate1,t1.agentOtherRateType1,t1.agentOtherRate1,")
		.append("t2.quauqRateType2,t2.quauqRate2,t2.quauqOtherRateType2,t2.quauqOtherRate2,")
		.append("t2.agentRateType2,t2.agentRate2,t2.agentOtherRateType2,t2.agentOtherRate2,")
		.append("t3.quauqRateType3,t3.quauqRate3,t3.quauqOtherRateType3,t3.quauqOtherRate3,")
		.append("t3.agentRateType3,t3.agentRate3,t3.agentOtherRateType3,t3.agentOtherRate3,t1.chouchengRate1, ")
		.append("t1.chouchengRateType1, ")
		.append("t2.chouchengRate2, ")
		.append("t2.chouchengRateType2, ")
		.append("t3.chouchengRate3, ")
		.append("t3.chouchengRateType3 ")
		.append("FROM sys_office o LEFT JOIN ")
		.append("(SELECT r.quauq_rate_type AS quauqRateType1,r.quauq_rate AS quauqRate1,")
		.append("r.quauq_other_rate_type AS quauqOtherRateType1,r.quauq_other_rate AS quauqOtherRate1,")
		.append("r.agent_rate_type AS agentRateType1,r.agent_rate AS agentRate1,")
		.append("r.agent_other_rate_type AS agentOtherRateType1,r.agent_other_rate AS agentOtherRate1,r.company_uuid,r.chouchengRate AS chouchengRate1,r.chouchengRateType AS chouchengRateType1  ")
		.append("FROM sys_office_rate r WHERE r.agent_type = 1) t1 ON o.uuid = t1.company_uuid ")
		.append("LEFT JOIN (SELECT r.quauq_rate_type AS quauqRateType2,r.quauq_rate AS quauqRate2,")
		.append("r.quauq_other_rate_type AS quauqOtherRateType2,r.quauq_other_rate AS quauqOtherRate2,")
		.append("r.agent_rate_type AS agentRateType2,r.agent_rate AS agentRate2,")
		.append("r.agent_other_rate_type AS agentOtherRateType2,r.agent_other_rate AS agentOtherRate2,r.company_uuid,r.chouchengRate AS chouchengRate2,r.chouchengRateType AS chouchengRateType2 ")
		.append("FROM sys_office_rate r WHERE r.agent_type = 2) t2 ON o.uuid = t2.company_uuid ")
		.append("LEFT JOIN (SELECT r.quauq_rate_type AS quauqRateType3,r.quauq_rate AS quauqRate3,")
		.append("r.quauq_other_rate_type AS quauqOtherRateType3,r.quauq_other_rate AS quauqOtherRate3,")
		.append("r.agent_rate_type AS agentRateType3,r.agent_rate AS agentRate3,")
		.append("r.agent_other_rate_type AS agentOtherRateType3,r.agent_other_rate AS agentOtherRate3,r.company_uuid,r.chouchengRate AS chouchengRate3,r.chouchengRateType AS chouchengRateType3 ")
		.append("FROM sys_office_rate r WHERE r.agent_type = 3) t3 ON o.uuid = t3.company_uuid ")
		.append("WHERE 1=1 AND o.id != 1 AND o.delFlag='0'");
		String companyName = mapRequest.get("companyName");
		if (StringUtils.isNotBlank(companyName)){
			sql.append("AND o.`name` LIKE '%").append(companyName).append("%'");
		}
		String chouchengRateMin = mapRequest.get("chouchengRateMin");
		if(StringUtils.isNotBlank(chouchengRateMin)){
			sql.append(" AND (t1.chouchengRate1>="+chouchengRateMin+" or t2.chouchengRate2>="+chouchengRateMin+" or t3.chouchengRate3>="+chouchengRateMin+") ");
		}
		String chouchengRateMax = mapRequest.get("chouchengRateMax");
		if(StringUtils.isNotBlank(chouchengRateMax)){
			sql.append(" AND (t1.chouchengRate1<="+chouchengRateMax+" or t2.chouchengRate2<="+chouchengRateMax+" or t3.chouchengRate3<="+chouchengRateMax+") ");
		}
		String quauqRateMin = mapRequest.get("quauqRateMin");
		if(StringUtils.isNotBlank(quauqRateMin)){
			sql.append(" AND (t1.quauqRate1>="+quauqRateMin+" or t2.quauqRate2>="+quauqRateMin+" or t3.quauqRate3>="+quauqRateMin+") ");
		}
		String quauqRateMax = mapRequest.get("quauqRateMax");
		if(StringUtils.isNotBlank(quauqRateMax)){
			sql.append(" AND (t1.quauqRate1<="+quauqRateMax+" or t2.quauqRate2<="+quauqRateMax+" or t3.quauqRate3<="+quauqRateMax+") ");
		}
		quauqServiceFeeDao.findBySql(page,sql.toString(),Map.class);
		List<Map<Object,Object>> list = page.getList();
		for (Map<Object, Object> map : list) {
			handleRateValForList("quauqRateType1","quauqRate1",map);
			handleRateValForList("quauqOtherRateType1","quauqOtherRate1",map);
			handleRateValForList("agentRateType1","agentRate1",map);
			handleRateValForList("agentOtherRateType1","agentOtherRate1",map);
			handleRateValForList("chouchengRateType1","chouchengRate1",map);
			handleRateValForList("quauqRateType2","quauqRate2",map);
			handleRateValForList("quauqOtherRateType2","quauqOtherRate2",map);
			handleRateValForList("agentRateType2","agentRate2",map);
			handleRateValForList("agentOtherRateType2","agentOtherRate2",map);
			handleRateValForList("chouchengRateType2","chouchengRate2",map);
			handleRateValForList("quauqRateType3","quauqRate3",map);
			handleRateValForList("quauqOtherRateType3","quauqOtherRate3",map);
			handleRateValForList("agentRateType3","agentRate3",map);
			handleRateValForList("agentOtherRateType3","agentOtherRate3",map);
			handleRateValForList("chouchengRateType3","chouchengRate3",map);
		}
	}

	/**
	 * 私有方法，用于处理转换原始查询结果数据，供上面方法调用。
	 * @author yudong.xu 2016.8.10
     */
	private void handleRateValForList(String typeName,String valName,Map<Object,Object> map){
		Short typeVal = (Short)map.get(typeName);
		if (typeVal == null || typeVal == -1){
			map.put(valName,"0");
			return;
		}
		BigDecimal val = (BigDecimal)map.get(valName);
		if (typeVal == 0){
			val = val.multiply(new BigDecimal(100));
			String moneyStr = MoneyNumberFormat.getRoundMoney(val,2,BigDecimal.ROUND_HALF_UP);
			map.put(valName,moneyStr + "%"); // 显示如1%
			return;
		}
		if (typeVal == 1){
			String moneyStr = MoneyNumberFormat.getRoundMoney(val,2,BigDecimal.ROUND_HALF_UP);
			map.put(valName," 金额￥：" + moneyStr); // 显示如￥50.00
			return;
		}
	}

	@Override
	public Rate getCompanyRate(String companyUuid, Integer agentType) {
		if(StringUtils.isBlank(companyUuid)){
			throw new RuntimeException("批发商UUID不能为空");
		}
		if(null == agentType){
			throw new RuntimeException("渠道类型不能为空");
		}
		List<Map<String, Object>> list = quauqServiceFeeDao.getCompanyRate(companyUuid, agentType);
		Rate rate = new Rate();
		if(!Collections3.isEmpty(list)){
			Map<String, Object> map = list.get(0);
			rate = RateUtils.assignmentRate(map);
		}
		return rate;
	}

	/**
	 * 保存用户输入的默认费率。将前端用户输入的数据进行解析，然后保存或者更新数据库中的记录。
	 * @param  itemStr 待解析的json格式的item数据。
	 * @param  uuidStr 待解析的json格式的公司uuid数据。
	 * @author yudong.xu
	 * @date 2016.8.15
	 */
	@Transactional(readOnly = false,rollbackFor ={Exception.class})
	public void saveDefaultRate(String itemStr,String uuidStr){
		JSONArray itemArr = JSONArray.parseArray(itemStr);
		JSONArray uuidArr = JSONArray.parseArray(uuidStr);
		Set<String> uuidSet = new HashSet<>();
		for (int i = 0; i < uuidArr.size(); i++) {
			uuidSet.add(uuidArr.getString(i));
		}

		Map<String,List<CompanyRateItem>> group = new HashMap<>();
		for (int i = 0; i < itemArr.size(); i++) {
			JSONObject item = itemArr.getJSONObject(i);
			if(StringUtils.isBlank(item.getString("rate"))){
				continue; // 如果用户对该项没有输入值，直接pass掉。
			}
			CompanyRateItem itemPOJO = new CompanyRateItem();
			String agentName = item.getString("agentName");
			itemPOJO.setAgentName(agentName);
			itemPOJO.setRateName(item.getString("rateName"));
			itemPOJO.setRateType(item.getInteger("rateType"));
			itemPOJO.setRate(item.getBigDecimal("rate"));
			// 根据渠道类型进行分组
			if (group.containsKey(agentName)){
				group.get(agentName).add(itemPOJO);
			}else {
				List<CompanyRateItem> pojoList = new ArrayList<>();
				pojoList.add(itemPOJO);
				group.put(agentName,pojoList);
			}
		}

		if (group.containsKey("sales")){ //门店
			List<CompanyRateItem> itemList = group.get("sales");
			saveAndUpdateOfficeRate(uuidSet,itemList,1); // 1对应门店
		}
		if (group.containsKey("moffice")){ // 总社
			List<CompanyRateItem> itemList = group.get("moffice");
			saveAndUpdateOfficeRate(uuidSet,itemList,2); // 2对应总社
		}
		if (group.containsKey("group")){ // 集团
			List<CompanyRateItem> itemList = group.get("group");
			saveAndUpdateOfficeRate(uuidSet,itemList,3); // 3对应集团
		}
	}

	/**
	 * 保存或者更新给的公司uuid的某一渠道下的所有输入值。对应的对象为OfficeRate
	 * @param uuidSet 前端传入的公司uuid的Set集合。
	 * @param itemList 前端传入的进行分组预处理的某一渠道的输入值。
	 * @param agentType 渠道类型。
	 * @author yudong.xu
	 * @date 2016.8.15
     */
	private void saveAndUpdateOfficeRate(Set<String> uuidSet,List<CompanyRateItem> itemList,Integer agentType){
		List<OfficeRate> resultList = quauqServiceFeeDao.getCompanyRates(uuidSet,agentType);
		if (uuidSet.size() != resultList.size()){
			// 如果查询出来的结果数和前端传入的公司uuid数量不一致。说明表中还没有对应记录，这时检查哪些公司没有创建，然后创建新对象保存。
			Set<String> missingSet = new HashSet<>(uuidSet); // 原有的Set不能改动，需复制一下，创建新的set。
			for (OfficeRate officeRate : resultList) {
				missingSet.remove(officeRate.getCompanyUuid());
			}
			handleRateItemForCreate(missingSet,itemList,agentType);
		}
		//对数据库中已有的记录进行更新。
		handleRateItemForUpdate(itemList,resultList);
	}
	/**
	 * 按照itemList中的值，更新OfficeRate对象,只更新前端传入的值，其他的值需调用方法自己更新。
	 * @param itemList 对应某一渠道的所有输入值的list.
	 * @param officeRate 待更新的OfficeRate对象。
	 * @author yudong.xu
	 * @date 2016.8.15
     */
	private void updateOfficeRate(List<CompanyRateItem> itemList,OfficeRate officeRate){
		BigDecimal hundred = new BigDecimal(100); // 100,用于作为除数
		for (CompanyRateItem item : itemList) {
			String rateName = item.getRateName();
			Integer rateType = item.getRateType();
			BigDecimal rate = item.getRate();
			if (rateType == 0){ // 百分比类型,除以100
				rate = rate.divide(hundred);
			}
			if("quauq-pr".equals(rateName)){
				officeRate.setQuauqRateType(rateType);
				officeRate.setQuauqRate(rate);
			}else if ("quauq-or".equals(rateName)) {
				officeRate.setQuauqOtherRateType(rateType);
				officeRate.setQuauqOtherRate(rate);
			}else if ("channel-pr".equals(rateName)){
				officeRate.setAgentRateType(rateType);
				officeRate.setAgentRate(rate);
			}else if ("channel-or".equals(rateName)){
				officeRate.setAgentOtherRateType(rateType);
				officeRate.setAgentOtherRate(rate);
			}else if("choucheng-pr".equals(rateName)){
				officeRate.setChouchengRateType(rateType);
				officeRate.setChouchengRate(rate);
			}
		}
	}

	/**
	 * 根据公司uuid的数量,创建相应数量的OfficeRate对象，并按照itemList输入值和agentType值进行填充数据,并保存到数据库中。
	 * @param uuidSet 待进行创建的公司uuid的Set集合。
	 * @param itemList 对应某一渠道的所有输入值的list。
	 * @param agentType 渠道类型，1门店，2总社，3集团。
	 * @author yudong.xu
	 * @date 2016.8.15
     */
	private void handleRateItemForCreate(Set<String> uuidSet,List<CompanyRateItem> itemList,Integer agentType){
		Long userId = UserUtils.getUser().getId();
		Date now = new Date();
		BigDecimal quauqRate = new BigDecimal(0.01);
		for (String comUuid : uuidSet) {
			OfficeRate officeRate = new OfficeRate();
			updateOfficeRate(itemList,officeRate); // 调用前端传入数据更新方法。

			officeRate.setCompanyUuid(comUuid); // 设置对应公司的uuid
			officeRate.setUuid(UuidUtils.generUuid()); // 设置本身的uuid
			officeRate.setAgentType(agentType); // 设置渠道类型
			officeRate.setQuauqRateType(0); // 0百分比，默认是百分之1
			officeRate.setQuauqRate(quauqRate);

			officeRate.setCreateBy(userId);
			officeRate.setUpdateBy(userId);
			quauqServiceFeeDao.saveObj(officeRate);
			quauqBusinessLogService.log("新增" + officeRate,QuauqBusinessLog.ADD_DEFAULT_FEE_RATE);
		}
	}

	/**
	 * 私有方法,把resultList中字段根据itemList的值,调用持久层方法进行更新。
	 * @param itemList 对应某一渠道的所有输入值的list。
	 * @param resultList 对应从数据库中查询出来的待更新的OfficeRate持久化对象的list;
	 * @author yudong.xu
	 * @date 2016.8.15
     */
	private void handleRateItemForUpdate(List<CompanyRateItem> itemList,List<OfficeRate> resultList){
		Long userId = UserUtils.getUser().getId();
		Date now = new Date();
		String oldStr;
		String newStr;
		for (OfficeRate officeRate : resultList) {
			oldStr = officeRate.toString();
			updateOfficeRate(itemList,officeRate); // 调用前端传入数据更新方法。
			officeRate.setUpdateBy(userId);
			officeRate.setUpdateDate(now);
			newStr = officeRate.toString();
			quauqServiceFeeDao.updateObj(officeRate);
			quauqBusinessLogService.log(oldStr + "变为" + newStr, QuauqBusinessLog.UPDATE_DEFAULT_FEE_RATE);
		}
	}

	/**
	 * 根据公司(渠道商)的uuid来新增所有类型的费率。将quauq的默认费率设置为1%,其它不进行设置。
	 * @param companyUuid
	 * @author yudong.xu 2016.8.29
	 */
	@Transactional(readOnly = false,propagation = Propagation.REQUIRED,rollbackFor ={Exception.class})
	@Override
	public void saveAllTypeCompanyRate(String companyUuid) {
		Long userId = UserUtils.getUser().getId();
		BigDecimal quauqRate = new BigDecimal(0.01);
		for (int i = 1; i <= 3; i++) {
			OfficeRate officeRate = new OfficeRate();

			officeRate.setCompanyUuid(companyUuid); // 所属公司的uuid
			officeRate.setAgentType(i); // 渠道类型，1门店,2总社,3集团
			officeRate.setQuauqRateType(0); // 0百分比，默认是百分之1
			officeRate.setQuauqRate(quauqRate);

			officeRate.setUuid(UuidUtils.generUuid());
			officeRate.setCreateBy(userId);
			officeRate.setUpdateBy(userId);

			quauqServiceFeeDao.saveObj(officeRate); // 保存到数据库
		}
	}
	
	/**
	 * 导出excel 查询列表
	 * 查询批发商的默认费率值。然后对查询出来的原始数据值进行处理。根据费率类型，来转换成最终送显的值。
	 * @author chao.zhang
     */
	@Override
	public List<Map<Object,Object>> getOfficeRateList( Map<String,String> mapRequest) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT o.id AS companyId,o.uuid AS companyUuid,o.`name` AS companyName,")
		.append("t1.quauqRateType1,t1.quauqRate1,t1.quauqOtherRateType1,t1.quauqOtherRate1,")
		.append("t1.agentRateType1,t1.agentRate1,t1.agentOtherRateType1,t1.agentOtherRate1,")
		.append("t2.quauqRateType2,t2.quauqRate2,t2.quauqOtherRateType2,t2.quauqOtherRate2,")
		.append("t2.agentRateType2,t2.agentRate2,t2.agentOtherRateType2,t2.agentOtherRate2,")
		.append("t3.quauqRateType3,t3.quauqRate3,t3.quauqOtherRateType3,t3.quauqOtherRate3,")
		.append("t3.agentRateType3,t3.agentRate3,t3.agentOtherRateType3,t3.agentOtherRate3,t1.chouchengRate1, ")
		.append("t1.chouchengRateType1, ")
		.append("t2.chouchengRate2, ")
		.append("t2.chouchengRateType2, ")
		.append("t3.chouchengRate3, ")
		.append("t3.chouchengRateType3 ")
		.append("FROM sys_office o LEFT JOIN ")
		.append("(SELECT r.quauq_rate_type AS quauqRateType1,r.quauq_rate AS quauqRate1,")
		.append("r.quauq_other_rate_type AS quauqOtherRateType1,r.quauq_other_rate AS quauqOtherRate1,")
		.append("r.agent_rate_type AS agentRateType1,r.agent_rate AS agentRate1,")
		.append("r.agent_other_rate_type AS agentOtherRateType1,r.agent_other_rate AS agentOtherRate1,r.company_uuid,r.chouchengRate AS chouchengRate1,r.chouchengRateType AS chouchengRateType1  ")
		.append("FROM sys_office_rate r WHERE r.agent_type = 1) t1 ON o.uuid = t1.company_uuid ")
		.append("LEFT JOIN (SELECT r.quauq_rate_type AS quauqRateType2,r.quauq_rate AS quauqRate2,")
		.append("r.quauq_other_rate_type AS quauqOtherRateType2,r.quauq_other_rate AS quauqOtherRate2,")
		.append("r.agent_rate_type AS agentRateType2,r.agent_rate AS agentRate2,")
		.append("r.agent_other_rate_type AS agentOtherRateType2,r.agent_other_rate AS agentOtherRate2,r.company_uuid,r.chouchengRate AS chouchengRate2,r.chouchengRateType AS chouchengRateType2 ")
		.append("FROM sys_office_rate r WHERE r.agent_type = 2) t2 ON o.uuid = t2.company_uuid ")
		.append("LEFT JOIN (SELECT r.quauq_rate_type AS quauqRateType3,r.quauq_rate AS quauqRate3,")
		.append("r.quauq_other_rate_type AS quauqOtherRateType3,r.quauq_other_rate AS quauqOtherRate3,")
		.append("r.agent_rate_type AS agentRateType3,r.agent_rate AS agentRate3,")
		.append("r.agent_other_rate_type AS agentOtherRateType3,r.agent_other_rate AS agentOtherRate3,r.company_uuid,r.chouchengRate AS chouchengRate3,r.chouchengRateType AS chouchengRateType3 ")
		.append("FROM sys_office_rate r WHERE r.agent_type = 3) t3 ON o.uuid = t3.company_uuid ")
		.append("WHERE 1=1 AND o.id != 1 AND o.delFlag='0'");
		String companyName = mapRequest.get("companyName");
		if (StringUtils.isNotBlank(companyName)){
			sql.append("AND o.`name` LIKE '%").append(companyName).append("%'");
		}
		String chouchengRateMin = mapRequest.get("chouchengRateMin");
		if(StringUtils.isNotBlank(chouchengRateMin)){
			sql.append(" AND (t1.chouchengRate1>="+chouchengRateMin+" or t2.chouchengRate2>="+chouchengRateMin+" or t3.chouchengRate3>="+chouchengRateMin+") ");
		}
		String chouchengRateMax = mapRequest.get("chouchengRateMax");
		if(StringUtils.isNotBlank(chouchengRateMax)){
			sql.append(" AND (t1.chouchengRate1<="+chouchengRateMax+" or t2.chouchengRate2<="+chouchengRateMax+" or t3.chouchengRate3<="+chouchengRateMax+") ");
		}
		String quauqRateMin = mapRequest.get("quauqRateMin");
		if(StringUtils.isNotBlank(quauqRateMin)){
			sql.append(" AND (t1.quauqRate1>="+quauqRateMin+" or t2.quauqRate2>="+quauqRateMin+" or t3.quauqRate3>="+quauqRateMin+") ");
		}
		String quauqRateMax = mapRequest.get("quauqRateMax");
		if(StringUtils.isNotBlank(quauqRateMax)){
			sql.append(" AND (t1.quauqRate1<="+quauqRateMax+" or t2.quauqRate2<="+quauqRateMax+" or t3.quauqRate3<="+quauqRateMax+") ");
		}
		List<Map<Object,Object>> list = quauqServiceFeeDao.findBySql(sql.toString(),Map.class);
		for (Map<Object, Object> map : list) {
			handleRateValForList("quauqRateType1","quauqRate1",map);
			handleRateValForList("quauqOtherRateType1","quauqOtherRate1",map);
			handleRateValForList("agentRateType1","agentRate1",map);
			handleRateValForList("agentOtherRateType1","agentOtherRate1",map);
			handleRateValForList("chouchengRateType1","chouchengRate1",map);
			handleRateValForList("quauqRateType2","quauqRate2",map);
			handleRateValForList("quauqOtherRateType2","quauqOtherRate2",map);
			handleRateValForList("agentRateType2","agentRate2",map);
			handleRateValForList("agentOtherRateType2","agentOtherRate2",map);
			handleRateValForList("chouchengRateType2","chouchengRate2",map);
			handleRateValForList("quauqRateType3","quauqRate3",map);
			handleRateValForList("quauqOtherRateType3","quauqOtherRate3",map);
			handleRateValForList("agentRateType3","agentRate3",map);
			handleRateValForList("agentOtherRateType3","agentOtherRate3",map);
			handleRateValForList("chouchengRateType3","chouchengRate3",map);
		}
		return list;
	}
	
	/**
	 * 生成Excel
	 * @param map
	 * chao.zhang
	 */
	public  HSSFWorkbook createRateExcel(List<Map<Object,Object>> list){
		 int rowNum = 0;
		HSSFWorkbook workBook = new HSSFWorkbook();
		HSSFSheet sheet = ExportExcelForAgentUtils.creatSheet(workBook);
		
		//设置剧中
		HSSFCellStyle cellStyle = workBook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		List<String[]> titleList = new ArrayList<String[]>();
		String[] titleNames1 = {"序号","批发商名称","QUAUQ产品费率","抽成费率","门店（默认费率）","","","总社（默认费率）","","","集团公司（默认费率）","",""};
		String[] titleNames2 = {"","","","","QUAUQ其他费率","渠道产品费率","渠道其他费率","QUAUQ其他费率","渠道产品费率","渠道其他费率","QUAUQ其他费率","渠道产品费率","渠道其他费率"};
		titleList.add(titleNames1);
		titleList.add(titleNames2);
	
		//合并单元格 所用数组集合 每一个数组中的每一个元素为CellRangeAddress的一个参数
		List<int[]> regions = new ArrayList<int[]>();
		int[] region1 = {0, 0, 4, 6};
		int[] region2 = {0, 0, 7, 9};
		int[] region3 = {0, 0, 10, 12};
		int[] region4 = {0, 1, 0, 0};
		int[] region5 = {0, 1, 1, 1};
		int[] region6 = {0, 1, 2, 2};
		int[] region7 = {0, 1, 3, 3};
		regions.add(region1);
		regions.add(region2);
		regions.add(region3);
		regions.add(region4);
		regions.add(region5);
		regions.add(region6);
		regions.add(region7);

		//创建表头
		rowNum = createTitleRow(sheet, titleList, rowNum, cellStyle,regions);
		
		List<String[]> bodyList = new ArrayList<String[]>();
		//将表中所需数据组装成String数组放入List
		for(int i=0;i<list.size();i++){
			Map<Object, Object> map = list.get(i);
			String quauqRate =""; 
			if(!map.get("quauqRate1").toString().equals("-1")){
				quauqRate=map.get("quauqRate1").toString();
			}else if(!map.get("quauqRate2").toString().equals("-1")){
				quauqRate=map.get("quauqRate2").toString();
			}else if(!map.get("quauqRate3").toString().equals("-1")){
				quauqRate=map.get("quauqRate3").toString();
			}else{
				quauqRate = "0";
			}
			String chouchengRate ="";
			if(!map.get("chouchengRate1").toString().equals("-1")){
				chouchengRate=map.get("chouchengRate1").toString();
			}else if(!map.get("chouchengRate2").toString().equals("-1")){
				chouchengRate=map.get("chouchengRate2").toString();
			}else if(!map.get("chouchengRate3").toString().equals("-1")){
				chouchengRate=map.get("chouchengRate3").toString();
			}else{
				chouchengRate = "0";
			}
			String[] bodyDatas = {i+1+"",map.get("companyName").toString(),quauqRate,chouchengRate,map.get("quauqOtherRate1").toString(),map.get("agentRate1").toString(),
												map.get("agentOtherRate1").toString(),map.get("quauqOtherRate2").toString(),map.get("agentRate2").toString(),map.get("agentOtherRate2").toString(),
												map.get("quauqOtherRate3").toString(),map.get("agentRate3").toString(),map.get("agentOtherRate3").toString()};
			bodyList.add(bodyDatas);
		}
		//填充表中数据
		createBodyRow(sheet, bodyList, rowNum, cellStyle);
		return workBook;
	}
	
	/**
	 * 创建表头(若存在合并的请在合并的单元格数据中给 "")
	 * @param titleNames 表头中字段的名字
	 * @param list 为（int[]:为每一个CellRangeAddress的参数 数组的长度为4）的集合
	 * @return rowNum 当前行所在下一行的下标
	 * @author chao.zhang
	 */
	@Override
	public int createTitleRow(HSSFSheet sheet,List<String[]> titleList, int rowNum,HSSFCellStyle cellStyle,List<int[]> list){
		for(int i = 0 ; i < titleList.size() ; i++){
			HSSFRow titleRow = sheet.createRow((short) rowNum++);
			String[] titleNames = titleList.get(i);
			for(int j = 0 ; j < titleNames.length ; j++){
				HSSFCell cell = titleRow.createCell(j);
				cell.setCellValue(titleNames[j]);
				cell.setCellStyle(cellStyle);
			}
		} 
		mergeCell(list, sheet);
		return rowNum;
	}
	
	/**
	 * 合并单元格
	 * @param list 为（int[]:为每一个CellRangeAddress的参数 数组的长度为4）的集合
	 */
	private void mergeCell(List<int[]> list,HSSFSheet sheet){
		for(int[] param : list ){
			CellRangeAddress region = new CellRangeAddress(param[0], param[1], param[2], param[3]);
			sheet.addMergedRegion(region);
		}
	}
	
	/**
	 * 创建表的body
	 * @param sheet
	 * @param bodyList 表所需数据 （String[] 中的一个元素为每一行中的一个单元格的值）
	 * @param rowNum 表的body从第几行开始
	 * @param cellStyle 样式
	 * @author chao.zhang
	 */
	@Override
	public void createBodyRow(HSSFSheet sheet,List<String[]> bodyList,int rowNum,HSSFCellStyle cellStyle){
		for(int i = 0 ; i < bodyList.size() ; i ++){
			HSSFRow bodyRow = sheet.createRow((short)rowNum++);
			String[] bodyDatas = bodyList.get(i);
			for(int j = 0 ; j < bodyDatas.length ; j ++){
				HSSFCell cell = bodyRow.createCell(j);
				cell.setCellValue(bodyDatas[j]);
				cell.setCellStyle(cellStyle);
			}
		}
	}
}
