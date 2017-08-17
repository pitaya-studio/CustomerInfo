package com.trekiz.admin.review.changePrice.singleGroup.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.support.CommonResult;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.Arith;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.activity.repository.TravelActivityDao;
import com.trekiz.admin.modules.airticket.service.UPPricesGenerateHTML;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.Costchange;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.CostchangeDao;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.TravelerUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.review.changePrice.common.service.IChangePriceReviewService;
import com.trekiz.admin.review.changePrice.singleGroup.service.IChangePriceNewService;
import com.trekiz.admin.review.mutex.ReviewMutexService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChangePriceNewServiceImpl implements IChangePriceNewService {

	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ReviewMutexService reviewMutexService;
	@Autowired
	private IChangePriceReviewService changePriceReviewService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private TravelActivityDao travelActivityDao;
	@Autowired
	private ActivityGroupDao activityGroupDao;
	@Autowired
	private ProductOrderCommonDao orderDao;
	@Autowired
	private TravelerDao travelerDao;
	@Autowired
	private UserReviewPermissionChecker userReviewPermissionChecker;
	@Autowired
	private CostchangeDao costchangeDao;
	/**
	 * @Description 获取游客列表：只查询正常游客（包括退团审核中和转团审核中游客）
	 * @author yakun.bai
	 * @Date 2015-11-20
	 */
	@Override
	public List<Traveler> queryTravelerList(Long orderId, Integer orderType) {
		
		//查询正常游客
		List<Integer> delFlag = Lists.newArrayList();
		delFlag.add(Context.TRAVELER_DELFLAG_NORMAL);
		delFlag.add(Context.TRAVELER_DELFLAG_EXIT);
		delFlag.add(Context.TRAVELER_DELFLAG_TURNROUND);
		List<Traveler> travelerList = travelerDao.findTravelerByOrderIdAndOrderType(orderId, orderType, delFlag);
				
		return travelerList;
	}
	
	/**
	 * @Description 游客改价申请
	 * @author yakun.bai
	 * @throws Exception 
	 * @Date 2015-11-24
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public Map<String, String>  travelerForApply(Map<String, Object> param, Long orderId, Integer productType) throws Exception {
		
		//返回map
		Map<String, String> resultMap = Maps.newHashMap();
		
		//游客差价数组
		String [] plusys  = (String[]) param.get("plusystrue");
		//游客ID数组
		String [] travelerIds = (String[]) param.get("travelerids"); 
		//游客币种数组
		String [] gaijiacurency =(String[]) param.get("gaijiacurency");
		//游客备注数组
		String [] travelerRemark = (String[]) param.get("travelerremark");
		
		//把差价为0的金额统一改为double类型金额
		for (int i = 0; i < plusys.length; i++) {
			if (Double.parseDouble(plusys[i]) == 0) {
				plusys[i] = "0.00";
			}
		}
		
		//如果选中的游客币种对应金额都没改变，则返回
		if (loopCheckArrs(plusys)) {
			resultMap.put("result", "error");
			resultMap.put("msg", "改价差额不能为零");
			return resultMap;
		}
		
		// 是否已经经过流程互斥校验
		List<String> checkList = Lists.newArrayList();
		
		//查询游客信息（只查询正常游客：不包括退团中和转团中游客）
		List<Traveler> travelerList = queryTravelerList(orderId, productType);
		
		//获取订单、团期、产品信息
		ProductOrderCommon productOrder = orderDao.findOne(orderId);
		ActivityGroup productGroup = activityGroupDao.findOne(productOrder.getProductGroupId());
		TravelActivity product = travelActivityDao.findOne(productOrder.getProductId());
		
		//获取订单信息数据
		Map<String, Object> orderInfo = OrderCommonUtil.getOrderInfo(productOrder, productGroup);
		//获取产品信息
		Map<String, Object> productInfo = OrderCommonUtil.getProductInfo(product);
		
		//循环改价差额数组，并申请改价
		if (CollectionUtils.isNotEmpty(travelerList)) {
			for (int i = 0; i < plusys.length; i++) {
				//如果改价差额为0，则表明金额没改动，不走申请流程
				if (plusys[i].equals("0.00")) {
					continue;
				} else {
					//游客id
					String travelerId = travelerIds[i];
					//游客名称
					String travellerName = "";
					//游客原始应收价
					String travelerOriginalPayPrice = "0.00";
					//游客币种id
					String currencyId = gaijiacurency[i];
					//获取用户所在批发商的货币信息
					Map<String, String>  moneyTypes = UPPricesGenerateHTML.getCurrencyParam(currencyService);
					//币种名称
					String currencyName = String.valueOf(moneyTypes.get(String.valueOf(gaijiacurency[i])));
					//当前应收价
					String curTotalMoney = "0.00";
					//改后应收价
					String changedTotalMoney = "0.00";
					//改价差额
					String changedPrice = plusys[i];
					//改价备注
					String remark = travelerRemark[i];
					//改价款项
					String changedFund = "应收价";
					
					//计算游客改前和改后应收价格
					for (Traveler traveler : travelerList) {
						//判断是否是要修改的游客
						if (travelerId.equals(traveler.getId().toString())) {
							travellerName = traveler.getName();
							
							//游客结算价
							List<MoneyAmount> payMoneyList = moneyAmountService.findAmountBySerialNum(traveler.getPayPriceSerialNum());
							for (MoneyAmount moneyAmount : payMoneyList) {
								if (gaijiacurency[i].equals(moneyAmount.getCurrencyId().toString())) {
									//获取当前币种应收价
									curTotalMoney = String.valueOf(moneyAmount.getAmount().doubleValue());
									//获取当前币种改后应收价
									double f = Arith.add(Double.parseDouble(curTotalMoney), Double.parseDouble(plusys[i]));
									changedTotalMoney = String.valueOf(f);
									break;
								} else {
									//获取当前币种改后应收价
									double f = Arith.add(Double.parseDouble("0.00"), Double.parseDouble(plusys[i]));
									changedTotalMoney = String.valueOf(f);
									continue;
								}
							}
						}
					}
					
					// 组织 review 数据
					Map<String, Object> variables = new HashMap<String, Object>();
					
					/** 通用参数赋值 */
					
					// 流程类型
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PROCESS_TYPE, Context.REVIEW_FLOWTYPE_CHANGE_PRICE);
					// 产品类型
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, productType);
					// 备注
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK, remark);
					// 部门id
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_DEPT_ID, product.getDeptId());
					// 游客id
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, travelerId);
					// 游客名称
					variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, travellerName);
					
					/** 自定义参数赋值 */
					
					// 游客id
					variables.put(Context.REVIEW_VARIABLE_KEY_CHANGE_TRAVELER_ID, travelerId);
					// 游客名称
					variables.put(Context.REVIEW_VARIABLE_KEY_CHANGE_TRAVELER_NAME, travellerName);
					// 改价币种id
					variables.put(Context.REVIEW_VARIABLE_KEY_CHANGE_CURRENCY_ID, currencyId);
					// 改价币种名称
					variables.put(Context.REVIEW_VARIABLE_KEY_CHANGE_CURRENCY_NAME, currencyName);
					// 游客原始应收
					variables.put(Context.REVIEW_VARIABLE_KEY_OLD_TOTAL_MONEY, travelerOriginalPayPrice);
					// 游客改前金额
					variables.put(Context.REVIEW_VARIABLE_KEY_CUR_TOTAL_MONEY, curTotalMoney);
					// 游客改后金额
					variables.put(Context.REVIEW_VARIABLE_KEY_CHANGED_TOTAL_MONEY, changedTotalMoney);
					// 游客改价差额
					variables.put(Context.REVIEW_VARIABLE_KEY_CHANGED_PRICE, changedPrice);
					// 改价款项
					variables.put(Context.REVIEW_VARIABLE_KEY_CHANGED_FUND, changedFund);
					// 是否只有人民币（审核配置参数）
					variables.put(Context.REVIEW_VARIABLE_KEY_CHANGE_IS_RMB_ONLY, isRmbOnly(plusys, gaijiacurency));
					// 改价差额（审核配置参数）
					variables.put(Context.REVIEW_VARIABLE_KEY_CHANGE_PRICE, Double.parseDouble(plusys[i]));

					variables.putAll(orderInfo);
					variables.putAll(productInfo);
					
					//如果是高改，则直接通过；如果低改则走申请
					String userId = UserUtils.getUser().getId().toString();
					String companyId = UserUtils.getUser().getCompany().getUuid();
					Map<String, String> params = new HashMap<String, String>();
					params.put("travelerId", travelerIds[i]);
					params.put("orderId", orderId.toString());
					params.put("orderType", productType.toString());
					params.put("currencyId", gaijiacurency[i]);
					params.put("amount", plusys[i]);
					
					
					if (!checkList.contains(travelerId)) {
						// 流程互斥校验
						if (travelerIds != null && travelerIds.length > 0) {
							CommonResult result = reviewMutexService.check(orderId.toString(), travelerId, productType.toString(), 
									Context.REVIEW_FLOWTYPE_CHANGE_PRICE.toString(), false);
							if (!result.getSuccess()) {
								throw new Exception(result.getMessage() + "</br>" + "请重新选择游客");
							}
						}
						checkList.add(travelerId);
					}
					ReviewResult reviewResult = reviewService.start(userId, companyId, userReviewPermissionChecker, null, productType,
							Context.REVIEW_FLOWTYPE_CHANGE_PRICE, product.getDeptId(), "", variables);
					
					// 如果审核直接通过，则需改价
					if (ReviewConstant.REVIEW_STATUS_PASSED == reviewResult.getReviewStatus()) {
						boolean flag = changePriceReviewService.doChangePrice(params);
						if (!flag) {
							resultMap.put("result", "error");
							resultMap.put("msg", "改价报错");
						}
					} 
					
					//如果改价申请成功
					if (reviewResult.getSuccess()) {
						resultMap.put("result", "success");
					} else {
						resultMap.put("result", "error");
						resultMap.put("msg", reviewResult.getMessage());
					}
				}
			}
		}
		return resultMap;
	}

	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public Map<String, String>  travelerForApply(String travelerArr, Long orderId, Integer productType) throws Exception {

		//返回map
		Map<String, String> resultMap = Maps.newHashMap();

		// 是否已经经过流程互斥校验
		List<String> checkList = Lists.newArrayList();

		//查询游客信息（只查询正常游客：不包括退团中和转团中游客）
//		List<Traveler> travelerList = queryTravelerList(orderId, productType);

		//获取订单、团期、产品信息
		ProductOrderCommon productOrder = orderDao.findOne(orderId);
		ActivityGroup productGroup = activityGroupDao.findOne(productOrder.getProductGroupId());
		TravelActivity product = travelActivityDao.findOne(productOrder.getProductId());

		//获取订单信息数据
		Map<String, Object> orderInfo = OrderCommonUtil.getOrderInfo(productOrder, productGroup);
		//获取产品信息
		Map<String, Object> productInfo = OrderCommonUtil.getProductInfo(product);

		JSONArray jsonArray = JSON.parseArray(travelerArr);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObject = (JSONObject) jsonArray.get(i);
			Long travelerId = Long.parseLong(jsonObject.get("travelerId").toString());
			String remark = jsonObject.get("travelerremark").toString();

			// 组织 review 数据
			Map<String, Object> variables = new HashMap<String, Object>();

			/** 通用参数赋值 */

			// 流程类型
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PROCESS_TYPE, Context.REVIEW_FLOWTYPE_CHANGE_PRICE);
			// 产品类型
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, productType);
			// 备注
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK, remark);
			// 部门id
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_DEPT_ID, product.getDeptId());
			// 游客id
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, travelerId);
			// 游客名称
			Traveler traveler = travelerDao.findOne(travelerId);
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, traveler.getName());
			//由于多币种原因，此处改为如果改价此处只要有负值，则需要审批，如果改价差额值全部为正值，则直接通过审批
			/**
			 * 1、  改价时如果添加的每一笔费用均大于0的数值，则改价不走审批流程；
			 *	2、  当添加了一笔费用小于0的数值（负数），则改价走审批流程；
			 */
			JSONArray feeArr1 = (JSONArray) jsonObject.get("feeArr");
			//用于判断是否有负值
			boolean payee = false;
			for (int j = 0; j < feeArr1.size(); j++) {
				JSONObject jObject = (JSONObject) feeArr1.get(j);
				BigDecimal sum = new BigDecimal(jObject.get("sum").toString());
				if(sum.doubleValue()<0){
					payee = true;
				}
			}
			//如果有负值，则审批，否则审批通过
			if(payee){
				variables.put(Context.REVIEW_VARIABLE_KEY_CHANGE_PRICE, -1);
			}else{
				variables.put(Context.REVIEW_VARIABLE_KEY_CHANGE_PRICE, 1);
			}
			variables.putAll(orderInfo);
			variables.putAll(productInfo);

			if (!checkList.contains(travelerId)) {
				// 流程互斥校验
//				if (travelerIds != null && travelerIds.length > 0) {
				CommonResult result = reviewMutexService.check(orderId.toString(), travelerId.toString(), productType.toString(),
						Context.REVIEW_FLOWTYPE_CHANGE_PRICE.toString(), false);
				if (!result.getSuccess()) {
					throw new Exception(result.getMessage() + "</br>" + "请重新选择游客");
				}
//				}
				checkList.add(travelerId.toString());
			}

			String userId = UserUtils.getUser().getId().toString();
			String companyId = UserUtils.getUser().getCompany().getUuid();
			ReviewResult reviewResult = reviewService.start(userId, companyId, userReviewPermissionChecker, null, productType,
					Context.REVIEW_FLOWTYPE_CHANGE_PRICE, product.getDeptId(), "", variables);

			//如果改价申请成功
			if (reviewResult.getSuccess()) {
				List<Costchange> costchangeList = new ArrayList<Costchange>();
				JSONArray feeArr = (JSONArray) jsonObject.get("feeArr");
				for (int j = 0; j < feeArr.size(); j++) {
					Costchange costchange = new Costchange();
					JSONObject jObject = (JSONObject) feeArr.get(j);
					String name = jObject.get("name").toString();
					Long currencyId = Long.parseLong(jObject.get("currencyId").toString());
					BigDecimal number = new BigDecimal(jObject.get("number").toString());
					BigDecimal price = new BigDecimal(jObject.get("price").toString());
					BigDecimal sum = new BigDecimal(jObject.get("sum").toString());

					costchange.setCostName(name);
					costchange.setCostSum(sum);
					costchange.setTravelerId(travelerId);
					costchange.setPriceCurrency(currencyService.findCurrency(currencyId));
					costchange.setCostPrice(price);
					costchange.setCostNum(number);
					costchange.setStatus(reviewResult.getReviewStatus());
					costchange.setBusinessType(1);
					costchange.setReviewUuid(reviewResult.getReviewId());

					costchangeList.add(costchange);
				}

				saveCostChanges(costchangeList, orderId, productType, reviewResult.getReviewStatus());
				resultMap.put("result", "success");
			} else {
				resultMap.put("result", "error");
				resultMap.put("msg", reviewResult.getMessage());
			}
		}

		return resultMap;
	}

	@Override
	public List<Map<String, Object>> getReviewList(Long orderId, Integer productType) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT " +
				" rn.create_date createDate, " +
				" rn.traveller_id travellerId, " +
				" rn.traveller_name travellerName, " +
				" GROUP_CONCAT(c.price_currency) currencyIds, " +
				" GROUP_CONCAT(c.costSum) prices, " +//修改by chao.zhang 原来sql为GROUP_CONCAT(c.costPrice) prices   2017-03-24
				" rn.create_by createBy, " +
				" rn.status, " +
				" rn.current_reviewer currentReviewer, " +
				" rn.remark, " +
				" rn.id, " +
				" rn.order_id orderId, " +
				" rn.product_type productType " +
				"FROM " +
				" costchange c, " +
				" review_new rn " +
				"WHERE " +
				" c.review_uuid = rn.id " +
				"AND rn.process_type = 10 " +
				" and rn.order_id = ? " +
				" and rn.product_type = ? " +
				"GROUP BY " +
				" c.travelerId, c.review_uuid" +
				" order by rn.create_date DESC ");
		return costchangeDao.findBySql(sb.toString(), Map.class, orderId, productType);
	}

	@Override
	public Map<String, Object> getReviewDetail(String reviewUuid) {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT " +
				" rn.create_date createDate, " +
				" rn.traveller_id travellerId, " +
				" rn.traveller_name travellerName, " +
				" GROUP_CONCAT(c.price_currency) currencyIds, " +
				" GROUP_CONCAT(c.costSum) prices, " +//修改审批详情 改价差额 add by chao.zhang 源代码 是 c.costPrice  2017-03-24
				" rn.create_by createBy, " +
				" rn.status, " +
				" rn.current_reviewer currentReviewer, " +
				" rn.remark, " +
				" rn.id, " +
				" rn.order_id orderId, " +
				" rn.product_type productType " +
				"FROM " +
				" costchange c, " +
				" review_new rn " +
				"WHERE " +
				" c.review_uuid = rn.id " +
				"AND rn.process_type = 10 " +
				" and rn.id = ? " +
				"GROUP BY " +
				" c.travelerId" +
				" order by rn.create_date DESC ");
		List<Map<String, Object>> list = costchangeDao.findBySql(sb.toString(), Map.class, reviewUuid);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	private void saveCostChanges(List<Costchange> costchangeList, Long orderId, Integer productType, Integer result) {
		for (int i = 0; i < costchangeList.size(); i++) {
			Costchange costchange =  costchangeList.get(i);
			costchangeDao.saveObj(costchange);
			if (ReviewConstant.REVIEW_STATUS_PASSED == result) {
				Map<String, String> params = Maps.newHashMap();
				params.put("travelerId", costchange.getTravelerId().toString());
				params.put("orderId", orderId.toString());
				params.put("orderType", productType.toString());
				params.put("currencyId", costchange.getPriceCurrency().getId().toString());
				params.put("amount", costchange.getCostSum().toString());
				
				changePriceReviewService.doChangePrice(params);
			}
		}
	}

	/**
	 * @Description 判断改价是否有差额
	 * @author yakun.bai
	 * @Date 2015-11-24
	 */
	private boolean loopCheckArrs(String [] arrs) {
		if (arrs == null ) {
			return true;
		} 
		int count = 0;
		int len = arrs.length;
		for (int i = 0; i < len; i++) {
			if (arrs[i].equals("0.00")) {
				count++;
			}
		}
		if (count == len) {
			return true;
		}
		return false;
	}
	
	/**
	 * @Description 是否只有人民币
	 * @author yakun.bai
	 * @Date 2015-12-14
	 */
	private boolean isRmbOnly(String[] pricess, String[] gaijiaCurrencies) {
		boolean flag = true;
		for (int i =0; i<pricess.length; i++) {
			double temp = Double.parseDouble(pricess[i]);
			if (temp != 0.0) {
				String currencyId = gaijiaCurrencies[i];
				Currency currency = currencyService.findCurrency(Long.parseLong(currencyId));
				String currencyName = currency.getCurrencyName();
				if (!"人民币".equals(currencyName)) {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}
	
	//同币种合并
	public void handlerMap(Map<String, Object> map, ProductOrderCommon productOrder) {
		String prices = map.get("prices").toString();
		String[] priceArr = prices.split(",");
		String currencyIds = map.get("currencyIds").toString();
		String[] currencyIdArr = currencyIds.split(",");
		Map<String, Object> moneyMap = new HashMap<String, Object>();
		for (int j = 0; j < currencyIdArr.length; j++) {
			String currencyId = currencyIdArr[j];
			BigDecimal priceSum = new BigDecimal(moneyMap.get(currencyId)==null?"0":moneyMap.get(currencyId).toString());
			moneyMap.put(currencyId, priceSum.add(new BigDecimal(priceArr[j].toString())));
		}

		List<Map<String, Object>> moneyList = new ArrayList<Map<String, Object>>();
		for (Map.Entry<String, Object> entry : moneyMap.entrySet()) {
			Map<String, Object> oneMap = new HashMap<String, Object>();
			
			if (productOrder.getPriceType() == 2) {
				Currency currency = currencyService.findById(Long.parseLong(entry.getKey()));
				oneMap.put("key", entry.getKey());
				oneMap.put("initValue", entry.getValue());
				oneMap.put("value", TravelerUtils.getTravelerChargeRate(productOrder, currency, entry.getValue().toString()));
			} else {
				oneMap.put("key", entry.getKey());
				oneMap.put("value", entry.getValue());
				oneMap.put("initValue", entry.getValue());
			}
			moneyList.add(oneMap);
		}
		map.put("moneyList", moneyList);
	}
	
}