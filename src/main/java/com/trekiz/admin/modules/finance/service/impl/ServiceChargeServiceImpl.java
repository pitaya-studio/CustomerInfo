package com.trekiz.admin.modules.finance.service.impl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.finance.param.ServiceChargePayParam;
import com.trekiz.admin.modules.finance.result.ServiceChargePayListResult;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.repository.RefundDao;
import com.trekiz.admin.modules.order.service.IOrderServiceChargeService;
import com.trekiz.admin.modules.order.service.ProductOrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.trekiz.admin.common.utils.Collections3;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.finance.repository.IServiceChargeDao;
import com.trekiz.admin.modules.finance.service.IServiceChargeService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.order.entity.OrderServiceCharge;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import freemarker.template.TemplateException;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务费付款实现类
 * @author  shijun.liu
 * @date    2016.08.30
 */
@Service
@Transactional(readOnly = true)
public class ServiceChargeServiceImpl implements IServiceChargeService{

	@Autowired
	private IServiceChargeDao iServiceChargeDao;
	@Autowired
	private MoneyAmountDao moneyAmountDao;
	@Autowired
	private CurrencyDao currencyDao;
	@Autowired
	private RefundDao refundDao;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private IOrderServiceChargeService iOrderServiceChargeService;
	@Autowired
	private ProductOrderService productOrderService;
	@Autowired
	private CurrencyService currencyService;
	@Override
	public Map<String, Object> getServiceCharge4Confirm(Long orderId, Integer serviceChargeType) {
		
		List<Map<String, Object>> maplist = iServiceChargeDao.getServiceCharge4Confirm(orderId, serviceChargeType);
		Map<String, Object> map = new HashMap<>();
		
		if (maplist != null && maplist.size() > 0) {
			map = maplist.get(0);
			//获取订单服务费流水号
			String serviceChargeSerial = (String) map.get("serviceChargeSerial");
			//多币种统计
//			String[] moneySum = moneyAmountService.getCurrencyAndMoneySum(serviceChargeSerial);
			//抽成服务费 560
			String money = "";
			if(map.get("agentName").toString().equals("QUAUQ")){
				String cutServiceCharge = (String) map.get("cut_service_charge");
					List<Map<String,Object>> quauqAmount = getSumQuauqAndCutAmount(serviceChargeSerial,cutServiceCharge);
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
			}else{
				String[] moneyArr = moneyAmountService.getCurrencyAndMoneySum(serviceChargeSerial);
				money = moneyArr[2];
			}
			map.put("serviceChargeSerialNum", money);
		}
		return map;
	}

	@Override
	public Map<String, Object> getPrintPaymentInfo(Long orderId, Integer serviceChargeType) {

		List<Map<String, Object>> printInfo = iServiceChargeDao.getPrintPaymentInfo(orderId, serviceChargeType);
		Map<String, Object> map = new HashMap<>();

		if (printInfo != null && printInfo.size() > 0) {
			map = printInfo.get(0);
			//获取订单服务费流水号
			String serviceChargeSerial = (String) map.get("serviceChargeSerial");
			List<MoneyAmount> amountlist = moneyAmountDao.getAmountByUid(serviceChargeSerial);
			if (amountlist != null && amountlist.size() > 0) {
				BigDecimal money = BigDecimal.ZERO;
				for (MoneyAmount ma : amountlist) {
					money = money.add(ma.getAmount().multiply(ma.getExchangerate()));
				}
				
				String moneyStr = money.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
				//人民币
				map.put("money", MoneyNumberFormat.getThousandsByRegex(moneyStr, 2));
				//汇率
				map.put("currencyExchangerate", "1.0000");
				//合计人民币
				map.put("totalRMBMoney", MoneyNumberFormat.getThousandsByRegex(moneyStr, 2));
				//合计人民币（大写）
				map.put("totalRMBMoneyName", StringNumFormat.changeAmount(moneyStr));
			}
		}
		return map;
	}

	@Override
	public File createServiceChargePaymentDownloadFile(Map<String, Object> map) throws IOException, TemplateException {

		return FreeMarkerUtil.generateFile("serviceChargePay.ftl", "serviceChargePay.doc", map);
	}
	
	/**
	 * 根据id查找代收服务费的确认付款表中的数据
	 * @param serviceChargeIdStr
	 * @return
	 * @author chao.zhang
	 */
	@Override
	public OrderServiceCharge getServiceChargePayById(String serviceChargeIdStr) {
		Assert.hasText(serviceChargeIdStr, "serviceChargeId should not be empty!");
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT * FROM order_service_charge WHERE ID = ?");
		List<OrderServiceCharge> list =iServiceChargeDao.findBySql(sbf.toString(),OrderServiceCharge.class, serviceChargeIdStr);
		if(list == null && list.size() == 0){
			throw new RuntimeException("不存在OrderServiceCharge");
		}
		return list.get(0);
	}

	/**
	 * 获取服务费付款列表
	 * @param page
	 * @author yudong.xu 2016.9.2
	 */
	@Override
	public void getServiceChargePayList(Page<ServiceChargePayListResult> page, ServiceChargePayParam param){
		iServiceChargeDao.getServiceChargePayList(page,param);
		List<ServiceChargePayListResult> list = page.getList();
		for (ServiceChargePayListResult result : list) {
			// 根据serial进行应付金额的多币种金额统计
//			List<MoneyAmount> moneyAmounts = moneyAmountService.findAmountBySerialNum(result.getPayMoney());//
			ProductOrderCommon orderCommon = productOrderService.getProductorderById(result.getOrderId().longValue());
//			List<MoneyAmount> cutAmounts = moneyAmountService.findAmountBySerialNum(orderCommon.getCutServiceCharge());
//			if(result.getSettleName().equals("QUAUQ")){
//				moneyAmounts.addAll(cutAmounts);
//			}
			String money = "";
			if(StringUtils.isNotBlank(orderCommon.getCutServiceCharge()) && result.getSettleName().equals("QUAUQ")){
				List<Map<String,Object>> quauqAmount = getSumQuauqAndCutAmount(result.getPayMoney(),orderCommon.getCutServiceCharge());
				if(!Collections3.isEmpty(quauqAmount)){
					String currencyIds = "";
					String amounts = "" ;
					for (Map<String,Object> amount : quauqAmount){
						if(amount.get("amounts") != null && Double.parseDouble(amount.get("amounts").toString())>0){
							if(StringUtils.isBlank(money)){
								money = amount.get("currencyMark")+""+amount.get("amounts");
							}else{
								money = money+"+"+amount.get("currencyMark")+""+amount.get("amounts");
							}
						}	
						if(StringUtils.isBlank(currencyIds)){
							currencyIds = amount.get("currencyId").toString();
						}else{
							currencyIds = currencyIds + "," + amount.get("currencyId").toString();
						}
						if(StringUtils.isBlank(amounts)){
							amounts = amount.get("amounts").toString();
						}else{
							amounts = amounts + "," + amount.get("amounts").toString();
						}
					}
					result.setCurrencyIds(currencyIds);
					result.setAmounts(amounts);
				}
			}else{
				String[] moneyArr = moneyAmountService.getCurrencyAndMoneySum(result.getPayMoney());
				money = moneyArr[2];
				result.setCurrencyIds(moneyArr[0]);
				result.setAmounts(moneyArr[1]);
			}
//			if(StringUtils.isNotBlank(orderCommon.getCutServiceCharge()) && result.getSettleName().equals("QUAUQ")){
//				List<MoneyAmount> moneyAmounts = moneyAmountService.getMoneyAmonutListIgnoreDelflag(orderCommon.getCutServiceCharge());
//				String[] cutArr = moneyAmountService.getCurrencyAndMoneySum(orderCommon.getCutServiceCharge());
//				if(cutArr[0].equals(moneyArr[0])){
//					if(StringUtils.isNotBlank(moneyArr[1]) && StringUtils.isNotBlank(cutArr[1])){
//						moneyArr[1] = (new BigDecimal(moneyArr[1]).add(new BigDecimal(cutArr[1])))+"";
//						Currency currency = currencyDao.findById(Long.parseLong(moneyArr[0]));
//						moneyArr[2] = currency.getCurrencyMark()+moneyArr[1];
//					}
//				}
//			}
			// 根据chargeId 和 moneyType 进行已付金额的多币种金额统计
			Integer moneyType = result.getChargeType() + 14; // 14是quauq服务费，15是总社服务费
			String accountedMoney = refundDao.getMultiCurrencyRefundSum(result.getChargeId(),moneyType);

			result.setPayMoney(money);
			result.setAccountedMoney(accountedMoney);
		}
	}

	/**
	 * 统计出当前公司未付款的服务费数量。
	 * @return
	 * @author yudong.xu 2016.9.14
	 */
	public Integer getServiceChargeCount(){
		return iServiceChargeDao.getServiceChargeCount();
	}
	private List<Map<String,Object>> getSumQuauqAndCutAmount(String quauqServiceCharge,String cutServiceCharge){
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT ma.*,sum(ma.amount) AS amounts,c.currency_mark as currencyMark FROM money_amount ma LEFT JOIN currency c  " );
		sbf.append("ON ma.currencyId = c.currency_id  " );
		sbf.append(	"WHERE ma.serialNum = ? OR ma.serialNum = ? GROUP BY ma.currencyId ORDER BY ma.currencyId");
		return iServiceChargeDao.findBySql(sbf.toString(),Map.class, quauqServiceCharge,cutServiceCharge);
	}
}
