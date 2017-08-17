package com.trekiz.admin.review.prdreturn.singleGroup.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao;
import com.trekiz.admin.modules.statisticAnalysis.home.service.OrderDateSaveOrUpdateService;
import com.trekiz.admin.modules.statisticAnalysis.home.service.StatisticAnalysisService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.CommonResult;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.MoneyAmountUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.review.money.entity.NewProcessMoneyAmount;
import com.trekiz.admin.review.money.service.NewProcessMoneyAmountService;
import com.trekiz.admin.review.mutex.ReviewMutexContext;
import com.trekiz.admin.review.mutex.ReviewMutexService;
import com.trekiz.admin.review.prdreturn.common.service.IProductReturnService;
import com.trekiz.admin.review.prdreturn.singleGroup.service.ISingleGroupOrderExitGroupService;

@Service("singleGroupOrderExitGroupService")
public class SingleGroupOrderExitGroupServiceImpl implements ISingleGroupOrderExitGroupService {
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private TravelerService travelerService;
	@Autowired
	private TravelerDao travelerDao;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private NewProcessMoneyAmountService newProcessMoneyAmountService;
    @Autowired
    private UserReviewPermissionChecker permissionChecker;
    @Autowired
    public ReviewMutexService reviewMutexService;
    @Autowired
	private IProductReturnService productReturnService;
    @Autowired
    private ProductOrderCommonDao productorderDao;
    @Autowired
    private CurrencyDao currencyDao;
    @Autowired
   	private OrderDateSaveOrUpdateDao orderDateSaveOrUpdateDao;

	protected Logger logger = LoggerFactory
			.getLogger(SingleGroupOrderExitGroupServiceImpl.class);

	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Map<String, Object> startExitGroupAndHandleTravler(ProductOrderCommon productOrder,
			ActivityGroup productGroup, TravelActivity product, Map<String, String[]> parameters, Long deptId,
			Integer productType, HttpServletRequest request) throws Exception {
		// 要返回的结果
		Map<String, Object> result = new HashMap<>();

		// 从 parameters 中获取参数
		String[] travelerNames = parameters.get("travelerNames");
		String[] travelerIds = parameters.get("travelerIds");
		String[] exitReasons = parameters.get("exitReasons");
		String[] _afterExitGroupMoneys = parameters.get("afterExitGroupMoneys");
		String[] _refundMoneys = parameters.get("refundMoneyso");

		// 获取订单信息数据(下单人,销售,下单时间,订单编号,订单团号,订单总额,计调)
		Map<String, Object> orderInfo = OrderCommonUtil.getOrderInfo(productOrder, productGroup);
		// 获取产品信息(已：产品id,产品名称,出团日期,行程天数,目的地
		// 未：成人单价,儿童单价,特殊人群单价,成人出行人数,儿童出行人数,特殊人群出行人数,出发城市)
		Map<String, Object> productInfo = OrderCommonUtil.getProductInfo(product);
		// 遍历游客数组，将数据进行存储，同时发起审核
		try {
			for (int i = 0; i < travelerIds.length; i++) {
                Long travelerId = Long.parseLong(travelerIds[i]);
                String travelerName = travelerNames[i];
                String exitReason = exitReasons[i];
                // 退团后应收金额，如：1701#33,34#500,1000
                String afterExitGroupMoneyStr = _afterExitGroupMoneys[i];
                String refundMoneyStr = _refundMoneys[i];
                Traveler traveler = travelerService.findTravelerById(travelerId);

                String priceSerialNum = traveler.getPayPriceSerialNum();
                String travelerPayPrice = moneyAmountService.getMoneyStr(priceSerialNum);

                // 对退团后应收金额进行拆分
                String[] afterExitGroupMoneyArr = afterExitGroupMoneyStr.split("#");
                String[] refundMoneyArr = refundMoneyStr.split("#");
                String travelerIdStr = afterExitGroupMoneyArr[0];
                String afterExitGroupCurrencyId = afterExitGroupMoneyArr[1];  // 可能多币种（逗号隔开）
                String afterExitGroupMoney = afterExitGroupMoneyArr[2];  // 可能多金额（逗号隔开）
                String refundCurrencyId = refundMoneyArr[1];  // 可能多币种（逗号隔开）
                String refundMoney = refundMoneyArr[2];  // 可能多金额（逗号隔开）
                // 如果travelerId 和 travelerIdStr 值不相同，则说明数据出错。返回
                if (!travelerId.toString().equals(travelerIdStr) || !travelerIdStr.equals(refundMoneyArr[0])) {
                    throw new Exception("游客信息出错！");
                }
                // 组织 review 数据
                Map<String, Object> variables = new HashMap<String, Object>();
                // 流程类型
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PROCESS_TYPE, Context.REVIEW_FLOWTYPE_EXIT_GROUP);
                // 产品类型
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, productType);
                // 备注
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK, exitReason);
                // 部门id
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_DEPT_ID, deptId);
                // 游客id
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, travelerId);
                // 游客名称
                variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, travelerName);
                // 游客应收金额
                variables.put(Context.REVIEW_VARIABLE_KEY_TRAVELER_PAY_PRICE, travelerPayPrice);
                // 退团后应收金额（币种）
                variables.put(Context.REVIEW_VARIABLE_KEY_AFTER_EXIT_GROUP_CURRENCY_ID, afterExitGroupCurrencyId);
                // 退团后应收金额（数量）
                variables.put(Context.REVIEW_VARIABLE_KEY_AFTER_EXIT_GROUP_MONEY, afterExitGroupMoney);
                // 退款字符串
                variables.put(Context.REVIEW_VARIABLE_KEY_AFTER_EXIT_GROUP_REFUND_MONEY, refundMoneyStr);

                variables.putAll(orderInfo);
                variables.putAll(productInfo);

                String userId = UserUtils.getUser().getId().toString();
                String companyUuid = UserUtils.getUser().getCompany().getUuid();
                Long companyId = UserUtils.getUser().getCompany().getId();
                // 调用 start()，发起退团申请
                ReviewResult reviewResult = reviewService.start(userId, companyUuid, permissionChecker, null, productType,
                        Context.REVIEW_FLOWTYPE_EXIT_GROUP, deptId, "", variables);
                if (!reviewResult.getSuccess()) {
					throw new Exception(reviewResult.getMessage());
                } else {
                	String[] afterCurrencyIdArray = afterExitGroupCurrencyId.split(",");
                	String[] refundCurrencyIdArray = refundCurrencyId.split(",");
                	String[] afterMoneyArray = afterExitGroupMoney.split(",");
                	String[] refundMoneyArray = refundMoney.split(",");
                	if (afterCurrencyIdArray == null || refundCurrencyIdArray == null || afterCurrencyIdArray.length != refundCurrencyIdArray.length) {
                		throw new Exception("币种信息出错！");
                	}
                	if (afterMoneyArray == null || refundMoneyArray == null || afterMoneyArray.length != refundMoneyArray.length) {
                		throw new Exception("金额信息出错！");
                	}
                	List<Currency> currencyList = currencyService.findCompanyCurrencyList(companyId);
                	// start 成功后 保存退款金额
                	for (int j = 0; j < refundCurrencyIdArray.length; j++) {
                		for (Currency currency : currencyList) {
                			if (refundCurrencyIdArray[j].equals(currency.getId())) {
                				if (StringUtils.isNotBlank(refundMoneyArray[j]) && Double.parseDouble(refundMoneyArray[j]) != 0) {
                					MoneyAmount refundMoneyAmount = new MoneyAmount();
                                	refundMoneyAmount.setAmount(new BigDecimal(refundMoneyArray[j]));  // 退款金额
                                	refundMoneyAmount.setCurrencyId(Integer.parseInt(refundCurrencyIdArray[j]));  // 退款币种
                                	refundMoneyAmount.setExchangerate(currency.getConvertLowest());  // 币种汇率
                                	refundMoneyAmount.setCreatedBy(Long.parseLong(userId));
                                	refundMoneyAmount.setCreateTime(new Date());
                                	refundMoneyAmount.setDelFlag(Context.DEL_FLAG_NORMAL);
                                	refundMoneyAmount.setMoneyType(Context.MONEY_TYPE_TTTK);  // 退团退款
                                	refundMoneyAmount.setOrderType(productType);
                                	refundMoneyAmount.setReviewUuid(reviewResult.getReviewId());
                                	moneyAmountService.saveOrUpdateMoneyAmount(refundMoneyAmount);  // 保存退款金额
								}
                				break;
                			}
                		}
					}
                    // 将游客退团后应收保存到 traveler subtractMoneySerialNum 字段中
                    String subtractMoneySerialNum = null;
                    if (StringUtils.isNotBlank(traveler.getSubtractMoneySerialNum())) {
                    	subtractMoneySerialNum = traveler.getSubtractMoneySerialNum();
                    } else {
                    	subtractMoneySerialNum = UuidUtils.generUuid();
                    }
                    traveler.setSubtractMoneySerialNum(subtractMoneySerialNum);
                    List<MoneyAmount> substractMoneyAmounts = moneyAmountService.findAmountBySerialNum(subtractMoneySerialNum);  // 扣减金额
                    // start 成功后 保存退团后应收金额
                	if (afterCurrencyIdArray != null && afterCurrencyIdArray.length > 0) {						
                		for (int j = 0; j < afterCurrencyIdArray.length; j++) {
                			String tempCurrencyId = afterCurrencyIdArray[j];
                			// 获取退团后应收钱对应的汇率
                            BigDecimal exchangerate = null;
                            for (Currency currency : currencyList) {
                                if (tempCurrencyId.equals(currency.getId())) {
                                    exchangerate = currency.getConvertLowest();
                                    break;
                                }
                            }
                            NewProcessMoneyAmount afterExitGroupMoneyAmount = new NewProcessMoneyAmount();
                            afterExitGroupMoneyAmount.setAmount(new BigDecimal(afterMoneyArray[j]));
                            afterExitGroupMoneyAmount.setCurrencyId(Integer.parseInt(tempCurrencyId));
                            afterExitGroupMoneyAmount.setCompanyId(companyUuid);
                            afterExitGroupMoneyAmount.setCreatedBy(Long.parseLong(userId));
                            afterExitGroupMoneyAmount.setCreateTime(new Date());
                            afterExitGroupMoneyAmount.setDelFlag(Context.DEL_FLAG_NORMAL);
                            afterExitGroupMoneyAmount.setExchangerate(exchangerate);
                            afterExitGroupMoneyAmount.setMoneyType(Context.MONEY_TYPE_TTHYS);  // 退团后应收
                            afterExitGroupMoneyAmount.setOrderType(productType);
                            afterExitGroupMoneyAmount.setReviewId(reviewResult.getReviewId());
                            afterExitGroupMoneyAmount.setId(UuidUtils.generUuid());
                            newProcessMoneyAmountService.saveNewProcessMoneyAmount(afterExitGroupMoneyAmount);  // 保存退团后应收金额





							// 存储游客的扣减金额
                            if (CollectionUtils.isEmpty(substractMoneyAmounts)) {
                            	MoneyAmount moneyAmount = new MoneyAmount();
                            	moneyAmount.setAmount(new BigDecimal(afterMoneyArray[j]));
                            	moneyAmount.setCurrencyId(Integer.parseInt(tempCurrencyId));
                                moneyAmount.setCreatedBy(Long.parseLong(userId));
                                moneyAmount.setCreateTime(new Date());
                                moneyAmount.setDelFlag(Context.DEL_FLAG_NORMAL);
                                moneyAmount.setBusindessType(1);  // 1.游客 2.订单
                                moneyAmount.setExchangerate(exchangerate);
                                moneyAmount.setMoneyType(Context.MONEY_TYPE_KJJE);  // 扣减金额
                                moneyAmount.setOrderType(productType);
                                moneyAmount.setSerialNum(subtractMoneySerialNum);
                                moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount);
							} else {
								for (MoneyAmount oldSubMoney : substractMoneyAmounts) {
									if (oldSubMoney.getCurrencyId() == Integer.parseInt(tempCurrencyId)) {
										oldSubMoney.getAmount().add(new BigDecimal(afterMoneyArray[j]));
										moneyAmountService.saveOrUpdateMoneyAmount(oldSubMoney);
										break;
									}
								}
							}

						}
					}
                    // 更改游客状态 →删除标记 0:正常 1：删除 2:退团审核中 3：已退团
                    travelerDao.updateTravelerDelFlag(Context.TRAVELER_DELFLAG_EXIT, traveler.getId());
                }
                // 如果审核通过并且当前层级为最高层级 则更改对应业务数据
    			if (ReviewConstant.REVIEW_STATUS_PASSED == reviewResult.getReviewStatus()) {
    				// 退团审核成功后调用
    				productReturnService.handleFreePositionAndTraveler(travelerIdStr, productOrder, reviewResult.getReviewId(), request);
    				//-------by------junhao.zhao-----2017-03-26-----------退团之后，团类要将表order_data_statistics中对应的收客人数与订单金额修改---开始---
    				orderDateSaveOrUpdateDao.updatePeopleAndMoneyPro(productOrder.getId(), productOrder.getOrderStatus());
    				//-------by------junhao.zhao-----2017-03-26-----------退团之后，团类要将表order_data_statistics中对应的收客人数与订单金额修改---结束---
    			}
//				////已占位的订单退团、转团后，更新预统计表中的相关订单金额（不包括差额返还） mbmr 2017-3-22
//              if(null!=productOrder.getId()&&StringUtils.isNotBlank(productOrder.getId().toString())
//            		  &&(productOrder.getPayStatus()==3 || productOrder.getPayStatus()==4 || productOrder.getPayStatus()==5)){
//				  // 更新预统计表中的对应记录 mbmr 2017.1.18
//				  statisticAnalysisService.updateStatisticRecord(productOrder.getId(),productOrder.getOrderStatus()); // 更新原有订单数据
//
//			  }



			}
		} catch (Exception e) {
            e.printStackTrace();
			logger.error(e.getMessage());
            throw new Exception(e.getMessage());
		}

		result.put("msg", "success");
		return result;
	}

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Map<String, Object> checkSingleGroupExitGroup(ProductOrderCommon productOrder, ActivityGroup productGroup,
                                                         TravelActivity product, Map<String, String[]> parameters, Long deptId,
                                                         Integer productType) {
        Map<String, Object> tempMapAll = new HashMap<>();
        // 获取订单信息数据(下单人,销售,下单时间,订单编号,订单团号,订单总额,计调)
        Map<String, Object> orderInfo = OrderCommonUtil.getOrderInfo(productOrder, productGroup);
        String[] travelerIds = parameters.get("travelerIds");
        try {
            for (int i = 0; i < travelerIds.length; i++) {
                Map<String, Object> tempMap = new HashMap<>();
                Long travelerId = Long.parseLong(travelerIds[i]);
                // 进行退团申请互斥验证
                CommonResult checkResult = reviewMutexService.check(orderInfo.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID).toString(),
                                                                    travelerId.toString(),productType.toString(),
                                                                    Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString(), false);
                if(!checkResult.getSuccess()) {
                    Map<String, Object> params = checkResult.getParams();
                    Boolean canCancel = (boolean)params.get("canCancel");
                    //互斥的审批中的记录
                    List<ReviewNew> processingReviews = (List<ReviewNew>) params.get(ReviewMutexContext.PROCESSING_REVIEWS);
                    //互斥的审批中的审批类型列表
                    Set<String> processingProcessTypes = (Set<String>) params.get(ReviewMutexContext.PROCESSING_PROCESS_TYPES);
                    //互斥的已通过的记录
                    List<ReviewNew> passedReviews = (List<ReviewNew>) params.get(ReviewMutexContext.PASSED_REVIEWS);
                    //互斥的已通过的审批类型列表
                    Set<String> passedProcessTypes = (Set<String>) params.get(ReviewMutexContext.PASSED_PROCESS_TYPES);
                    Traveler traveler=travelerService.findTravelerById(travelerId);
                    StringBuffer stringBuffer = new StringBuffer();
                    if(canCancel) {
                        if (processingProcessTypes != null && processingProcessTypes.size() > 0){
                            stringBuffer.append(traveler.getName() + " ");
                            for (String passingProcessType : processingProcessTypes) {
                                stringBuffer.append("[" + Context.getREVIEW_FLOW().get(passingProcessType) + "]");
                            }
                            stringBuffer.append("审批中 "+ "(申请人" + traveler.getName() + ")");
                            tempMap.put("message", stringBuffer);
                            tempMap.put("processingReviews", processingReviews);
                        }
                    } else {
                        if (passedProcessTypes != null && passedProcessTypes.size() > 0) {
                            stringBuffer.append(traveler.getName() + " ");
                            for (String passedProcessType : passedProcessTypes) {
                                stringBuffer.append("[" + Context.getREVIEW_FLOW().get(passedProcessType) + "]");
                            }
                            stringBuffer.append("审批已通过 "+ "(申请人" + traveler.getName() + ")");
                            stringBuffer.append("不允许取消");
                            tempMap.put("message", stringBuffer);
                            tempMap.put("passedReviews", passedReviews);
                        }
                    }
                    tempMap.put("canCancel", canCancel);
                    tempMapAll.put(travelerId.toString(), tempMap);
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return tempMapAll;
    }

    /**
     * TODO
     */
	@Override
	@Transactional(readOnly=false)
	public void handleTravelerAndMoneyAmount(Long travelerId, String reviewId) {
		Traveler traveler = travelerService.findTravelerById(travelerId);
		String subtractMoneySerialNum = traveler.getSubtractMoneySerialNum();
//		MoneyAmount moneyAmount = moneyAmountService.findOneAmountBySerialNum(subtractMoneySerialNum);
		List<MoneyAmount> moneyAmount = moneyAmountService.findBySerialNum(subtractMoneySerialNum);
		if (CollectionUtils.isNotEmpty(moneyAmount)) {	
			for (MoneyAmount moneyAmount2 : moneyAmount) {				
				moneyAmount2.setDelFlag("1");
			}
		}
		traveler.setSubtractMoneySerialNum(null);
		traveler.setDelFlag(0);
		// 改变金额状态
		List<NewProcessMoneyAmount> newMoneyAmount = newProcessMoneyAmountService.findListByReviewId(reviewId);
		if(CollectionUtils.isNotEmpty(newMoneyAmount)) {
			for (NewProcessMoneyAmount newProcessMoneyAmount : newMoneyAmount) {
				newProcessMoneyAmount.setDelFlag("1");
			}
		}
	}

	@Override
	public void handleAfterAndRefund(List<Map<String, Object>> processList) {
		// TODO Auto-generated method stub
		if (CollectionUtils.isEmpty(processList)) {
			return;
		}
		for (Map<String, Object> map : processList) {
			handleAfterAndRefundEvery(map);
		}
	}

	@Override
	public void handleAfterAndRefundEvery(Map<String, Object> oneMap) {
		// 退团后应收
		String afterString = "";
		String afterCurrencyId = oneMap.get("afterExitGroupCurrencyId") == null ? null : oneMap.get("afterExitGroupCurrencyId").toString();
		String afterMoney = oneMap.get("afterExitGroupMoney") == null ? null : oneMap.get("afterExitGroupMoney").toString();
		if (StringUtils.isNotBlank(afterCurrencyId) && StringUtils.isNotBlank(afterMoney)) {
			String[] currencyIdArray = afterCurrencyId.split(",");
			String[] amount = afterMoney.split(",");
			if (currencyIdArray != null && currencyIdArray.length > 0 && amount != null && amount.length > 0 && currencyIdArray.length == amount.length) {
				List<Currency> currencies = currencyService.getCurrencysByIds(currencyIdArray);
				if (CollectionUtils.isNotEmpty(currencies)) {
					for (int i = 0; i < currencies.size(); i++) {
						if (i != 0) {
							afterString += "+"; 
						}
						afterString += currencies.get(i).getCurrencyMark() + MoneyAmountUtils.formatAmountString(StringUtils.isBlank(amount[i]) ? "0" : amount[i]); 
					}
				}
			}
		}
		// 如果为空则显示 币种符号 + 金额
		if (StringUtils.isBlank(afterString)) {
			afterString += oneMap.get("afterExitGroupMoneyMark") + "" + MoneyAmountUtils.formatAmountString(oneMap.get("afterExitGroupMoney") == null ? "0" : oneMap.get("afterExitGroupMoney").toString());
		}
		oneMap.put("afterString", afterString);
		
		// 退团退款
		String refundString = "";
		String refundMoneyStr = oneMap.get("refundMoneyStr") == null ? null : oneMap.get("refundMoneyStr").toString();
		if (StringUtils.isNotBlank(refundMoneyStr)) {
			String[] refundInfo = refundMoneyStr.split("#");
			String[] refundCurrencyIdArr = refundInfo[1].split(",");
			String[] refundMoneyArr = refundInfo[2].split(",");
			if (refundCurrencyIdArr != null && refundCurrencyIdArr.length > 0) {
				for (int i = 0; i < refundCurrencyIdArr.length; i++) {
					Currency currency = currencyService.findById(Long.parseLong(refundCurrencyIdArr[i]));
					if (refundMoneyArr[i] != null && Double.parseDouble(refundMoneyArr[i]) != 0) {
						// 用以展示的币种金额字符串
						if (i > 0) {
							refundString += " + ";
						}
						refundString += currency.getCurrencyMark() + MoneyAmountUtils.formatAmountString(refundMoneyArr[i]);
					}
				}
			}
		}
		oneMap.put("refundString", refundString);
	}

	
	@Override
	public List<Map<Object, Object>> getTravelerByOrderId4ExitGroup(Long orderId, Integer orderType) {
		StringBuffer sbf = new StringBuffer();
		sbf
		.append("SELECT p.orderTime,tt.id,tt.orderId,tt.name,tt.sex,tt.payPriceSerialNum,tt.personType,tt.delFlag,tt.srcPriceCurrency,")
		.append(" a.payPrice , a.currency_name as currencyName, a.currency_mark as currencyMark, a.currencyId, a.exchangerate, a.currency_exchangerate from traveler tt LEFT JOIN productorder p on tt.orderId= p.id ")
		.append(" LEFT JOIN( ")
		.append(" select t.id, IFNULL(ma.amount, 0) as payPrice, c.currency_name, c.currency_mark, ma.currencyId, ma.exchangerate, c.currency_exchangerate ")
		.append(" from traveler t LEFT  JOIN money_amount ma on t.payPriceSerialNum=ma.serialNum")
		.append(" JOIN currency c on ma.currencyId=c.currency_id")
		.append(" where t.orderId =? and t.delFlag not in(1,3,5))a on tt.id=a.id ")
		.append(" WHERE tt.orderId=? and tt.delFlag not in(1,3,5) and tt.order_type=?  order by tt.id asc ");
		List<Map<Object, Object>> ls = productorderDao.findBySql(sbf.toString(), Map.class, orderId, orderId, orderType);
		for (Map<Object, Object> m : ls) {
			String payPrice=m.get("payPrice").toString();
			if (StringUtils.isNotBlank(payPrice)) {
				m.put("payPrice", MoneyAmountUtils.getMoneyStr(payPrice));
			}
		}
		//处理 ls，在多币种情况下，将相同订单进行合并，返回带有多币种的 currencyId
		List<Map<Object,Object>> list = refundMergerTravelerList(ls);
		return list;
	}
	
	/**
	 * 处理根据订单id，查询出来的游客信息，返回带有多币种的查询结果
	 * @param travelerList
	 * @return 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private List<Map<Object, Object>> refundMergerTravelerList(List<Map<Object, Object>> travelerList) {
		List<Map<Object, Object>> resultList = new ArrayList<Map<Object, Object>>();
		Map<Integer, Map> resultMap = new LinkedHashMap();
		
		// 根据id获取对应Map
		for (int i = 0; i < travelerList.size(); i++) {
			Map item = travelerList.get(i);
			Integer id = (Integer) item.get("id");
			Map result = resultMap.get(id);
			
			if (result != null) {
				String payPrice = result.get("payPrices").toString();
				String currencyId = result.get("currencyIds").toString();
				String currencyName = result.get("currencyName").toString();
				String currencyMark = result.get("currencyMark").toString();
				String exchangerate = result.get("exchangerate").toString();
				String payPriceSerialNum = result.get("payPriceSerialNum").toString();
				//Integer currencyId = (Integer)result.get("currencyId");
				result.put("payPrices", payPrice + "," + item.get("payPrice"));
				result.put("currencyIds", currencyId + "," + item.get("currencyId"));
				result.put("currencyName", currencyName + "," + item.get("currencyName"));
				result.put("currencyMark", currencyMark + "," + item.get("currencyMark"));
				result.put("exchangerate", exchangerate + "," + item.get("exchangerate"));
				result.put("payPriceSerialNum", payPriceSerialNum + "," + item.get("payPriceSerialNum"));
				resultMap.put(id, result);
			}else {
				item.put("payPrices", item.get("payPrice"));
				item.put("currencyIds", item.get("currencyId"));
				item.put("currencyName", item.get("currencyName"));
				item.put("currencyMark", item.get("currencyMark"));
				item.put("srcPriceCurrency", item.get("srcPriceCurrency"));
				item.put("exchangerate", item.get("exchangerate"));
				item.put("payPriceSerialNum", item.get("payPriceSerialNum"));
				resultMap.put(id, item);
			}
		}
		for (Entry entry : resultMap.entrySet()) {
			List<MoneyAmount> tempMoneyAmounts = new ArrayList<>();
			// 应收币种
			String[] currencyIds = ((Map<Object, Object>) entry.getValue()).get("currencyIds").toString().split(",");
			// 应收金额getValue
			String[] prices = ((Map<Object, Object>) entry.getValue()).get("payPrices").toString().split(",");
			String[] exchangerates = ((Map<Object, Object>) entry.getValue()).get("exchangerate").toString().split(",");
			String[] payPriceSerialNums = ((Map<Object, Object>) entry.getValue()).get("payPriceSerialNum").toString().split(",");
			for (int j = 0; j < currencyIds.length; j++) {
				// 组织moneyAmount用以多币种换汇计算
				MoneyAmount moneyAmount = new MoneyAmount();
				moneyAmount.setAmount(new BigDecimal(prices[j]));
				moneyAmount.setCurrencyId(Integer.parseInt(currencyIds[j]));
				Currency currency = currencyDao.findById(Long.parseLong(currencyIds[j]));
				if (currency != null) {
					moneyAmount.setExchangerate(currency.getConvertLowest());
				} else {
					moneyAmount.setExchangerate(new BigDecimal(exchangerates[j]));					
				}
				moneyAmount.setDelFlag(Context.DEL_FLAG_NORMAL);
				moneyAmount.setSerialNum(payPriceSerialNums[j]);
				moneyAmount.setCreateTime(new Date());
				tempMoneyAmounts.add(moneyAmount);
			}
			MoneyAmount specifiedMoney = MoneyAmountUtils.translateMultplyCurrency2Specified(tempMoneyAmounts, ((Map<Object, Object>) entry.getValue()).get("srcPriceCurrency").toString());
			Map<Object, Object> tempValue = ((Map<Object, Object>) entry.getValue());
			tempValue.put("asPayCurrency", specifiedMoney.getAmount());
			// 添加同行价币种信息
			Currency orgCurrency = currencyService.findById(Long.parseLong(tempValue.get("srcPriceCurrency").toString()));
			tempValue.put("srcPriceCurrencyName", orgCurrency.getCurrencyName());
			tempValue.put("srcPriceCurrencyMark", orgCurrency.getCurrencyMark());
			entry.setValue(tempValue);
			
			resultList.add((Map<Object, Object>) entry.getValue());
		}
		return resultList;
	}
	
}
