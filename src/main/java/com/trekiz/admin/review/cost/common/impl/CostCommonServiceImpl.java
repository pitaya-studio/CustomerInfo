package com.trekiz.admin.review.cost.common.impl;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.Collections3;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.activity.repository.TravelActivityDao;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaProductsDao;
import com.trekiz.admin.review.cost.common.ICostCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CostCommonServiceImpl implements ICostCommonService {
	
	@Autowired
	private TravelActivityDao travelActivityDao;
	@Autowired
	private ActivityGroupDao activityGroupDao;
	@Autowired
	private VisaProductsDao visaProductsDao;
	@Autowired
	private ActivityAirTicketDao activityAirTicketDao;
	@Autowired
	private CostRecordDao costRecordDao;
	@Autowired
	private ProductOrderCommonDao productorderDao;
	@Autowired
	private MoneyAmountDao moneyAmountDao;
	@Autowired
	private AgentinfoService agentinfoService;

	/**
	 * 获取预报单锁定状态
	 * @param productType 产品类型
	 * @param groupId 单团：团期id；签证、机票：产品id
	 */
	@Override
	public String getLockStatus(Integer productType, Long groupId) {
		
		String lock = "0";
		if(productType < Context.ORDER_TYPE_QZ || productType == Context.ORDER_TYPE_CRUISE) {
			ActivityGroup activityGroup = activityGroupDao.findOne(groupId);
			if(null != activityGroup && "10".equals(activityGroup.getForcastStatus())) {
				lock = "1";
			}
		}else if(productType == Context.ORDER_TYPE_QZ){
			VisaProducts visaProducts = visaProductsDao.findOne(groupId);
			if(null != visaProducts && "10".equals(visaProducts.getForcastStatus())) {
				lock = "1";
			}
		}else if(productType == Context.ORDER_TYPE_JP){
			ActivityAirTicket airTicket = activityAirTicketDao.findOne(groupId);
			if(null != airTicket && "10".equals(airTicket.getForcastStatus())) {
				lock = "1";
			}
		}
		
		return lock;
	}
	
	/**
	 * 更新实际成本附件
	 */
	public void updateCostVoucher(String docIds, Long costId) {
		String sql = "update cost_record set costVoucher = CONCAT(IFNULL(costVoucher,''),'" + docIds + "') where id = " + costId;
		costRecordDao.updateBySql(sql);
	}
	
	/**
	 * 删除实际成本附件
	 */
	@Override
	public void deleteCostVoucher(String docId, Long costId) {
		String sql = "update cost_record set costVoucher = replace(costVoucher,'"+ docId + ",','') where id=" + costId;
		costRecordDao.updateBySql(sql);
	}

	/**
	 * 获取实际成本附件
	 */
	@Override
	public String getCostVouchers(Long costId) {
		CostRecord costRecord = costRecordDao.getById(costId);
		if (costRecord != null) {
			return costRecord.getCostVoucher();
		}
		return null;
	}

	/**
	 * 查询对应团期或者产品的Quauq服务费
	 * @param orderType			产品类型
	 * @param activityId		产品或者团期ID
     * @return
	 * @author	shijun.liu
	 * @date	2016.08.11
     */
	public List<Map<String, String>> getQuauqServiceAmount(Integer orderType, Long activityId){
		if(null == orderType){
			throw new RuntimeException("订单类型不能为空");
		}
		if(null == activityId){
			throw new RuntimeException("团期ID不能为空");
		}
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		List<ProductOrderCommon> orders = productorderDao.findByActivityGroupId(activityId);
		for (ProductOrderCommon order : orders) {
			Integer status = order.getPayStatus();
			//去除已取消，已删除的订单
			if(99 == status.intValue() || 111 == status.intValue()){
				continue;
			}
			Map<String, String> map = new HashMap<String, String>();
//			BigDecimal money = new BigDecimal(0);
			String money = "";
			map.put("itemName", "代收服务费");							//项目名称
			map.put("settleName", "QUAUQ");								//结算方
			map.put("orderNum", order.getOrderNum());					//订单号
			map.put("personNumber", order.getOrderPersonNum().toString());//人数
			map.put("salerName", order.getSalerName());					  //销售
			if(StringUtils.isNotBlank(order.getQuauqServiceCharge())){
				//改为多币种
				List<Map<String,Object>> quauqAmount = getSumQuauqAndCutAmount(order.getQuauqServiceCharge(),order.getCutServiceCharge());
				if(!Collections3.isEmpty(quauqAmount)){
					for (Map<String,Object> amount : quauqAmount){
						if(amount.get("amounts") != null && Double.parseDouble(amount.get("amounts").toString())>0){
							if(StringUtils.isBlank(money)){
								money = amount.get("currencyMark")+""+amount.get("amounts");
							}else{
								money = money+"+"+amount.get("currencyMark")+""+amount.get("amounts");
							}
						}	
					}
				}
			}
			//服务费只显示大于0的数据
//			int index = money.compareTo(new BigDecimal(0));
//			if(index != 1){
//				continue;
//			}
			if(StringUtils.isBlank(money)){
				continue;
			}
			map.put("amount", money);
			data.add(map);
		}
		return data;
	}

	/**
	 * 查询产品或者团期的渠道服务费
	 * @param orderType		产品类型
	 * @param activityId	产品或者团期ID
     * @return
	 * @author	shijun.liu
	 * @date	2016.08.11
     */
	public List<Map<String, String>> getAgentServiceAmount(Integer orderType, Long activityId){
		if(null == orderType){
			throw new RuntimeException("订单类型不能为空");
		}
		if(null == activityId){
			throw new RuntimeException("团期ID不能为空");
		}
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		List<ProductOrderCommon> orders = productorderDao.findByActivityGroupId(activityId);
		for (ProductOrderCommon order : orders) {
			Integer status = order.getPayStatus();
			//去除已取消，已删除的订单
			if(99 == status.intValue() || 111 == status.intValue()){
				continue;
			}
			Map<String, String> map = new HashMap<String, String>();
//			BigDecimal money = new BigDecimal(0);
			String money = "";
			if(StringUtils.isNotBlank(order.getPartnerServiceCharge())){
//				List<MoneyAmount> agentAmount = moneyAmountDao.findAmountBySerialNum(order.getPartnerServiceCharge());
//				if(!Collections3.isEmpty(agentAmount)){
//					for (MoneyAmount amount : agentAmount){
//						BigDecimal temp = amount.getAmount().multiply(amount.getExchangerate());
//						money = money.add(temp);
//					}
//				}
				List<Map<String,Object>> agentAmount = getMoneyAmount(order.getPartnerServiceCharge());
				if(!Collections3.isEmpty(agentAmount)){
					for (Map<String,Object> amount : agentAmount){
						if(amount.get("amounts") != null && Double.parseDouble(amount.get("amounts").toString())>0){
							if(StringUtils.isBlank(money)){
								money = amount.get("currencyMark")+""+amount.get("amounts");
							}else{
								money = money+"+"+amount.get("currencyMark")+""+amount.get("amounts");
							}
						}
					}
				}
			}

			//服务费只显示大于0的数据
//			if(money.compareTo(new BigDecimal(0)) != 1){
//				continue;
//			}
			if(StringUtils.isBlank(money)){
				continue;
			}
			Long agentId = order.getOrderCompany();
			String settleName = "";
			//结算方取对应下单渠道的父级名称，如果没有父级则取其本身
			Agentinfo agentinfo = agentinfoService.findOne(agentId);
			if("-1".equals(agentinfo.getAgentParent())){
				settleName = agentinfo.getAgentName();
			}else{
				Agentinfo parentAgentInfo = agentinfoService.findOne(Long.valueOf(agentinfo.getAgentParent()));
				settleName = parentAgentInfo.getAgentName();
			}
//			map.put("amount", Context.CURRENCY_MARK_RMB + MoneyNumberFormat.getThousandsByRegex(money.toString(), 2));
			map.put("amount", money);
			map.put("itemName", "代收服务费");							//项目名称
			map.put("settleName", settleName);							//结算方
			map.put("orderNum", order.getOrderNum());					//订单号
			map.put("personNumber", order.getOrderPersonNum().toString());//人数
			map.put("salerName", order.getSalerName());					  //销售
			data.add(map);
		}
		return data;
	}
	
	/**
	 * 通过serialNum 获得金额
	 * @param serialNum
	 * @return
	 * @author chao.zhang
	 * @time 2016-09-01
	 */
	private List<Map<String,Object>> getMoneyAmount(String serialNum){
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT ma.*,sum(ma.amount) AS amounts,c.currency_mark as currencyMark FROM money_amount ma LEFT JOIN currency c  " );
		sbf.append("ON ma.currencyId = c.currency_id  " );
		sbf.append(	"WHERE ma.serialNum = ? GROUP BY ma.currencyId ORDER BY ma.currencyId");
		return costRecordDao.findBySql(sbf.toString(),Map.class, serialNum);
	}
	
	/**
	 * 查询服务费总额：（多币种展示）
	 * quauq服务费+渠道服务费
	 * @author chao.zhang
	 * @time 2016-09-01
	 */
	@Override
	public List<Map<String, Object>> getTotalPrice(Integer productType,
			Long groupId) {
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT sum(a.amount) AS amount,a.currencyMark FROM (");
		sbf.append("SELECT sum(ma.amount) AS amount,c.currency_mark as currencyMark FROM  productorder p  ");
		sbf.append("LEFT JOIN money_amount ma ON ma.serialNum = p.quauq_service_charge  ");
		sbf.append("LEFT JOIN currency c ON c.currency_id = ma.currencyId  ");
		sbf.append("WHERE p.delFlag = 0 AND  p.productGroupId = ? AND p.payStatus !=99 AND p.payStatus != 111 ");
		sbf.append("GROUP BY ma.currencyId ");
		sbf.append("UNION ALL ");
		sbf.append("SELECT sum(ma.amount) AS amount,c.currency_mark as currencyMark FROM  productorder p  ");
		sbf.append("LEFT JOIN money_amount ma ON ma.serialNum = p.partner_service_charge   ");
		sbf.append("LEFT JOIN currency c ON c.currency_id = ma.currencyId  ");
		sbf.append("WHERE p.delFlag = 0 AND  p.productGroupId = ? AND p.payStatus !=99 AND p.payStatus != 111 ");
		sbf.append("GROUP BY ma.currencyId ");
		sbf.append("UNION ALL ");
		sbf.append("SELECT ");
		sbf.append("sum(ma.amount) AS amount, ");
		sbf.append("c.currency_mark AS currencyMark ");
		sbf.append("FROM ");
		sbf.append("productorder p ");
		sbf.append("LEFT JOIN money_amount ma ON ma.serialNum = p.cut_service_charge ");
		sbf.append("LEFT JOIN currency c ON c.currency_id = ma.currencyId ");
		sbf.append("WHERE ");
		sbf.append("p.delFlag = 0 ");
		sbf.append("AND p.productGroupId = ? ");
		sbf.append("AND p.payStatus != 99 ");
		sbf.append("AND p.payStatus != 111 ");
		sbf.append("GROUP BY ");
		sbf.append("ma.currencyId ");
		sbf.append(")a GROUP BY currencyMark ");
		return costRecordDao.findBySql(sbf.toString(),Map.class,groupId,groupId,groupId);
	}
	/**
	 * 查询对应团期或者产品的Quauq服务费
	 * @param orderType			产品类型
	 * @param activityId		产品或者团期ID
     * @return
	 * @author	chao.zhang
	 * @date	2016.09.13
     */
	public List<Map<String, String>> getQuauqServiceAmount1(Integer orderType, Long activityId){
		if(null == orderType){
			throw new RuntimeException("订单类型不能为空");
		}
		if(null == activityId){
			throw new RuntimeException("团期ID不能为空");
		}
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		List<ProductOrderCommon> orders = productorderDao.findByActivityGroupId(activityId);
		for (ProductOrderCommon order : orders) {
			Integer status = order.getPayStatus();
			//去除已取消，已删除的订单
			if(99 == status.intValue() || 111 == status.intValue()){
				continue;
			}
			Map<String, String> map = new HashMap<String, String>();
			BigDecimal money = new BigDecimal(0);
//			String money = "";
			map.put("itemName", "代收服务费");							//项目名称
			map.put("settleName", "QUAUQ");								//结算方
			map.put("orderNum", order.getOrderNum());					//订单号
			map.put("personNumber", order.getOrderPersonNum().toString());//人数
			map.put("salerName", order.getSalerName());					  //销售
			if(StringUtils.isNotBlank(order.getQuauqServiceCharge())){
				//单币种
				List<MoneyAmount> quauqAmount = moneyAmountDao.findAmountBySerialNum(order.getQuauqServiceCharge());
				if(!Collections3.isEmpty(quauqAmount)){
					for (MoneyAmount amount : quauqAmount){
						BigDecimal temp = amount.getAmount().multiply(amount.getExchangerate());
						money = money.add(temp);
					}
				}
			}
			if(StringUtils.isNotBlank(order.getCutServiceCharge())){
				List<MoneyAmount> cutAmount = moneyAmountDao.findAmountBySerialNum(order.getCutServiceCharge());
				if(!Collections3.isEmpty(cutAmount)){
					for(MoneyAmount amount : cutAmount){
						BigDecimal cutPrice = amount.getAmount().multiply(amount.getExchangerate());
						money = money.add(cutPrice);
					}
				}
			}
			//服务费只显示大于0的数据
			int index = money.compareTo(new BigDecimal(0));
			if(index != 1){
				continue;
			}
			map.put("amount",  Context.CURRENCY_MARK_RMB + MoneyNumberFormat.getThousandsByRegex(money.toString(), 2));
			data.add(map);
		}
		return data;
	}

	/**
	 * 查询产品或者团期的渠道服务费
	 * @param orderType		产品类型
	 * @param activityId	产品或者团期ID
     * @return
	 * @author	shijun.liu
	 * @date	2016.08.11
     */
	public List<Map<String, String>> getAgentServiceAmount1(Integer orderType, Long activityId){
		if(null == orderType){
			throw new RuntimeException("订单类型不能为空");
		}
		if(null == activityId){
			throw new RuntimeException("团期ID不能为空");
		}
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		List<ProductOrderCommon> orders = productorderDao.findByActivityGroupId(activityId);
		for (ProductOrderCommon order : orders) {
			Integer status = order.getPayStatus();
			//去除已取消，已删除的订单
			if(99 == status.intValue() || 111 == status.intValue()){
				continue;
			}
			Map<String, String> map = new HashMap<String, String>();
			BigDecimal money = new BigDecimal(0);
			if(StringUtils.isNotBlank(order.getPartnerServiceCharge())){
				List<MoneyAmount> agentAmount = moneyAmountDao.findAmountBySerialNum(order.getPartnerServiceCharge());
				if(!Collections3.isEmpty(agentAmount)){
					for (MoneyAmount amount : agentAmount){
						BigDecimal temp = amount.getAmount().multiply(amount.getExchangerate());
						money = money.add(temp);
					}
				}
			}

			//服务费只显示大于0的数据
			if(money.compareTo(new BigDecimal(0)) != 1){
				continue;
			}
			Long agentId = order.getOrderCompany();
			String settleName = "";
			//结算方取对应下单渠道的父级名称，如果没有父级则取其本身
			Agentinfo agentinfo = agentinfoService.findOne(agentId);
			if("-1".equals(agentinfo.getAgentParent())){
				settleName = agentinfo.getAgentName();
			}else{
				Agentinfo parentAgentInfo = agentinfoService.findOne(Long.valueOf(agentinfo.getAgentParent()));
				settleName = parentAgentInfo.getAgentName();
			}
			map.put("amount", Context.CURRENCY_MARK_RMB + MoneyNumberFormat.getThousandsByRegex(money.toString(), 2));
			map.put("itemName", "代收服务费");							//项目名称
			map.put("settleName", settleName);							//结算方
			map.put("orderNum", order.getOrderNum());					//订单号
			map.put("personNumber", order.getOrderPersonNum().toString());//人数
			map.put("salerName", order.getSalerName());					  //销售
			data.add(map);
		}
		return data;
	}
	
	private List<Map<String,Object>> getSumQuauqAndCutAmount(String quauqServiceCharge,String cutServiceCharge){
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT ma.*,sum(ma.amount) AS amounts,c.currency_mark as currencyMark FROM money_amount ma LEFT JOIN currency c  " );
		sbf.append("ON ma.currencyId = c.currency_id  " );
		sbf.append(	"WHERE ma.serialNum = ? OR ma.serialNum = ? GROUP BY ma.currencyId ORDER BY ma.currencyId");
		return costRecordDao.findBySql(sbf.toString(),Map.class, quauqServiceCharge,cutServiceCharge);
	}
}
