package com.trekiz.admin.review.transfersMoney.singlegroup.web;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.engine.entity.ReviewLogNew;
import com.quauq.review.core.support.CommonResult;
import com.quauq.review.core.support.ReviewResult;
import com.quauq.review.core.type.OrderByDirectionType;
import com.quauq.review.core.type.OrderByPropertiesType;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.entity.TransferMoneyApplyForm;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.TransferMoneyService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.SysOfficeProductType;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.SysOfficeConfigurationService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.review.borrowing.airticket.service.NewOrderReviewService;
import com.trekiz.admin.review.money.entity.NewProcessMoneyAmount;
import com.trekiz.admin.review.money.service.NewProcessMoneyAmountService;
import com.trekiz.admin.review.mutex.ReviewMutexService;
import com.trekiz.admin.review.transfersMoney.singlegroup.service.NewTransferMoneyService;

/**
 * 转款审批（申请/审批）
 * @author yang.jiang 2015-12-18 17:54:56
 */
@Controller
@RequestMapping(value = "${adminPath}/singlegrouporder/transfermoney")
public class ActivitySingleGOTransferMoneyController {

	private static final Logger log = Logger.getLogger(ActivitySingleGOTransferMoneyController.class);
	 
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private OrderCommonService orderService;
	@Autowired
	private ActivityGroupService activityGroupService;
	@Autowired
	private NewProcessMoneyAmountService processMoneyAmountService;
    @Autowired
	@Qualifier("travelActivitySyncService")
    private ITravelActivityService travelActivityService;
    @Autowired
    private ProductOrderCommonDao productorderDao;
    @Autowired
	private NewTransferMoneyService transferMoneyService;
    @Autowired
	private TransferMoneyService oldTransferMoneyService;
    @Autowired
	private TravelerDao travelerDao;
    @Autowired
	private NewOrderReviewService newOrderReviewService;
    @Autowired
	private CurrencyService currencyService;
    @Autowired
    private NewTransferMoneyService newTransferMoneyService;
    @Autowired
	private UserReviewPermissionChecker permissionChecker;
    @Autowired
    private ReviewMutexService reivewMutexService;
    @Autowired
	private SysOfficeConfigurationService sysOfficeConfigurationService;
	@Autowired
	private UserReviewPermissionChecker userReviewPermissionChecker;
	@Autowired
	private TravelerService travelerService;
	
    /**
	 * 跳转至转款申请记录页面
	 * @author yang.jiang
	 * @param orderId 订单Id
	 * @param model
	 * @param request
	 * @return
	 */
    @RequestMapping(value ="transfersMoneyApplyList/{orderId}")
	public String transfersMoneyApplyList(@PathVariable String orderId,  Model model, HttpServletRequest request,HttpServletResponse response) {
    	
		Map<String, String> condition = new HashMap<String, String>();
		condition.put("orderId", orderId);
		//查询 订单、团期、产品、部门ID、产品类型
		ProductOrderCommon productOrder = orderService.getProductorderById(Long.valueOf(orderId));
		TravelActivity product = new TravelActivity();
		Integer orderType = null;
		if (productOrder != null) {
			product = travelActivityService.findById(productOrder.getProductId());
			orderType = productOrder.getOrderStatus();
		}
		Long deptId = null;
		if (product != null) {
			deptId = product.getDeptId();
		}

		//获取转款审批流程
		List<Map<String, Object>> processList = reviewService.getReviewDetailMapListByOrderId(deptId, orderType, Context.REVIEW_FLOWTYPE_TRANSFER_MONEY, orderId, OrderByPropertiesType.CREATE_DATE, OrderByDirectionType.DESC);

		model.addAttribute("processList", processList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("order", productOrder);
		model.addAttribute("orderId",orderId);
		
	    return "review/transfermoney/singlegroup/transferMoneyApplyList";
	}
    
    /**
	 * 验证订单到账金额是否存在
	 * @author yang.jiang
	 * @param orderId
	 * @param model
	 * @param request
	 * @return
	 */
    @ResponseBody
	@RequestMapping(value ="validAccountCurrent")
	public Map<String, String> validAccountCurrent(HttpServletRequest request, HttpServletResponse response) {
		Map<String, String> result = new HashMap<String, String>();
		String orderId = request.getParameter("orderId");
		result.put("res", "success");
		result.put("orderId", orderId);
		if(StringUtils.isBlank(orderId)){
			result.put("res", "转出（oldOrderId)订单id不存在");
			return result;
		}
		ProductOrderCommon productOrder = productorderDao.findOne(Long.parseLong(orderId));
		if (productOrder == null) {
			result.put("res", "转出（oldOrderId)订单不存在");
			return result;
		}
		String accountedMoneyUuid = productOrder.getAccountedMoney();
		if (StringUtils.isBlank(accountedMoneyUuid)) {
			result.put("res", "订单无到帐金额，不支持转款！");
			return result;
		}
		List<MoneyAmount> accountedMoneyList = moneyAmountService.findAmountBySerialNum(accountedMoneyUuid);
		if (CollectionUtils.isEmpty(accountedMoneyList)) {
			result.put("res", "获取订单到帐金额失败！");
			return result;
		}
	    return result;
	}
	
	/**
	 * 前往转款申请页面
	 * @param orderId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="toTransfersMoneyApply/{orderId}")
	public String toTtransfersMoneyApply(@PathVariable String orderId,  Model model, HttpServletRequest request) {
		Map<String, String> condition = new HashMap<String, String>();
		condition.put("orderId", orderId);
		//新审批流程 resultMap获取方法
		Map<String, Object> resultMapOld = oldTransferMoneyService.getResultMapOld2New(condition);
		Map<String, Object> resultMap = transferMoneyService.getResultMap(condition, resultMapOld);
		
		model.addAttribute("orderId",orderId);
		model.addAllAttributes(resultMap);
	    return "review/transfermoney/singlegroup/toTransferMoneyApply";
	}
	
	/**
	 * 流程申请
	 * @author yang.jiang
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value="transfersMoneyApply")
	public Map<String, String> transferMoneyApply(@Valid TransferMoneyApplyForm appForm, HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException {
		
		Map<String, String> resultMap = new HashMap<String, String>();
		//校验提交的表单数据是否合法
		boolean validateResult = newTransferMoneyService.validateApplyForm(appForm, resultMap);
		if (!validateResult) {
			return resultMap;
		}
		/** 转出团信息 */
		String orderId = appForm.getOrderId();  //转出订单id
		ProductOrderCommon order = orderService.getProductorderById(Long.valueOf(orderId));  //订单
		ActivityGroup group = activityGroupService.findById(order.getProductGroupId());  //团期
		TravelActivity product = travelActivityService.findById(order.getProductId());  //产品
		Long deptId = product.getDeptId();  //部门
		resultMap.put("res", "success");
		resultMap.put("orderId", orderId);
		String accountedMoneyUuid = order.getAccountedMoney();  //获取原订单的到账金额
		List<MoneyAmount> accountedMoneyList = moneyAmountService.findAmountBySerialNum(accountedMoneyUuid);
		if (CollectionUtils.isEmpty(accountedMoneyList)) {
			resultMap.put("res", "获取订单到帐金额失败！");
			resultMap.put("msg", "faild");
			return resultMap;
		}
		//本订单已审批中的转款款项总额
		List<NewProcessMoneyAmount> processingMoneyList = newTransferMoneyService.getProcessingTransferMoney(Long.parseLong(orderId), order.getOrderStatus());
		//订单累计申请转款金额（多币种）（ 审批中金额 + 申请过的金额 + 正在申请的金额 ）
		//TODO 未完成
		List<Map<String, Object>> totalApplyMoneyList = newTransferMoneyService.initialTotalApplyMoney(processingMoneyList, accountedMoneyList);
		
		/** 转款申请信息 */
		Long[] inOrderIds = appForm.getInOrderId();  //转入订单Id
		Integer[] travolerIds = appForm.getTravelorId();  //游客Id
		Long[] newTravellerIds = appForm.getNewTravellerIds();  //新游客Id
		String[] travolerNames = appForm.getTravelorName();  //游客name
		String[] tranMoneys = appForm.getTransferMoney();  //转款金额
		String[] remarks = appForm.getRemarks();  //备注
		Integer applyNum = inOrderIds.length;  //申请
		
		//TODO 组装转款申请游客Map ，暂时未用上
		Map<Long,String> travelerMap = Maps.newHashMap();
		List<Long> travelerIds = Lists.newArrayList();
		if(travolerIds.length > 0){
			for(int i =0;i<travolerIds.length;i++){
				travelerMap.put(travolerIds[i].longValue(), travolerNames[i]);
				travelerIds.add(travolerIds[i].longValue());
			}
		}
		//获取 登录用户、公司id、产品类型
		Long userId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid().toString();
		Integer productType = order.getOrderStatus();		
		// 获取订单信息数据(下单人,销售,下单时间,订单编号,订单团号,订单总额,计调等)
		Map<String, Object> orderInfo = getOrderInfo(order, group);
		// 获取产品信息(已：产品id,产品名称,出团日期,行程天数,目的地  // 未：成人单价,儿童单价,特殊人群单价,成人出行人数,儿童出行人数,特殊人群出行人数,出发城市)
		Map<String, Object> productInfo = getProductInfo(product);
		//遍历诸条转款申请，进一步校验转款金额，组织审批数据，发起审批流程
		for (int i = 0 ; i < applyNum; i++) {
			Long inOrderId = inOrderIds[i];  //转入订单Id
			Integer travellerId = travolerIds[i];  //游客Id
			Long newTravellerId = newTravellerIds[i];  //新游客Id
			String travellerName = travolerNames[i];  //游客name
			String tranMoney = tranMoneys[i];  //转款金额，拼接格式如：（:31|200:32|300:33|400）
			String remark = "";  //备注
			if (remarks != null && remarks.length > 0 && i <= remarks.length) {
				remark = remarks[i];
			}
			//当金额为空或者0时，表示此条申请无效
			if(StringUtils.isBlank(tranMoney) || tranMoney.trim().equals("0")){
				continue;
			}
			/**转款条件(金额部分):
			 * 原订单的到账金额（存在且） >= 所有审批中的转款累计金额；
			 * 累计转款金额，分币种进行比较，每种货币对应金额都不应大于到账金额对应币种的金额；
			 * 申请币种种类与到账金额币种种类保持一致； 
			 * 转款金额汇率按照到账金额对应币种汇率； 
			 **/						
			//处理金额和CurrentID
			tranMoney = tranMoney.replaceFirst(":", "");
			String[] moneys = tranMoney.split(":");
			int size = moneys.length;
			
			//遍历转款金额（多币种拼接），同币种间进行比较，要求 到账 >= 转款			
			for(int j = 0; j < size; j++){
				String[] money = moneys[j].split("\\|");
				for (int k = 0; k < accountedMoneyList.size(); k++) {
					MoneyAmount accMoney = accountedMoneyList.get(k);  //对应币种的达账金额					
					if (Integer.parseInt(money[0]) == accMoney.getCurrencyId().intValue()) {
						//本条转款申请之前要求，累计金额 + 本条申请 <= 达账金额，每一种货币都应满足
						for (Map<String, Object> applyedMoney : totalApplyMoneyList) {
							if (Integer.parseInt(applyedMoney.get("currencyId").toString()) == Integer.parseInt(money[0])) {
								if (new BigDecimal(money[1]).add(new BigDecimal(applyedMoney.get("amount").toString())).compareTo(accMoney.getAmount()) == 1) {
									resultMap.put("res", currencyService.findCurrencyName(Long.parseLong(money[0])) + "转款金额 大于到账金额，转款失败！");
									resultMap.put("msg", "faild");
									return resultMap;
								}
							}
						}
						break;
					} else if (k == accountedMoneyList.size() - 1) {
						resultMap.put("res", "转款金额中无对应的币种，转款失败！");
						resultMap.put("msg", "faild");
						return resultMap;
					}
				}
			}
			//预先生成 转款金额uuid，用于在流程成功后存入review_process_money_amount中。但最终不会存也是有可能的。
			String transferMoneyUuid = UuidUtils.generUuid();
			//
			Traveler traveler = travelerDao.findOne((long)travellerId);  //旧游客（参与转款的主体）
			String priceSerialNum = traveler.getPayPriceSerialNum();  //旧游客游客结算价uuid
			String oldTravelerPayPrice = moneyAmountService.getMoneyStr(priceSerialNum);  //转团后游客结算价字符串
			//转入订单
			ProductOrderCommon inOrder = productorderDao.findOne(inOrderId);  //新订单
			Traveler newTraveler;  //游客在新订单上的记录
			String newTravelerPayPrice = "";  //转团前游客结算价字符串
			if (newTravellerId == null) {
				newTraveler = new Traveler();  //获取订单第一位游客
//				newTraveler = travelerService.findFirstTravelerByOrderIdAndOrderType(inOrderId, inOrder.getOrderStatus());  //获取订单第一位游客
				if (Context.PERSON_TYPE_ADULT == traveler.getPersonType()) {
					newTravelerPayPrice = moneyAmountService.getMoneyStr(inOrder.getSettlementAdultPrice());
				} else if (Context.PERSON_TYPE_CHILD == traveler.getPersonType()) {
					newTravelerPayPrice = moneyAmountService.getMoneyStr(inOrder.getSettlementcChildPrice());
				} else if (Context.PERSON_TYPE_SPECIAL == traveler.getPersonType()) {
					newTravelerPayPrice = moneyAmountService.getMoneyStr(inOrder.getSettlementSpecialPrice());
				}
			} else {
				newTraveler = travelerDao.findOne(newTravellerId);  //新游客（展示信息需要）			
				newTravelerPayPrice = moneyAmountService.getMoneyStr(newTraveler.getPayPriceSerialNum());
			}
			//依据 订单、游客、流程 获取 detail。待用
//			List<ReviewNew> transferGroupReviewList = reviewService.getReviewList(orderId, travolerIds[i].toString(), productType.toString(), Context.REVIEW_FLOWTYPE_TRANSFER_MONEY.toString());
			//组织业务数据
			Map<String, Object> variables = new HashMap<String, Object>();
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID, orderId);  //订单id
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO,order.getOrderNum());  //订单编号
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, productType);  //订单类型、产品类型
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID,product.getId());   //产品id
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, product.getAcitivityName());  //产品名称
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_ID, group.getId());  //团号
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, group.getGroupCode());  //团号
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, product.getCreateBy().getId());  //计调
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR,order.getCreateBy().getId());  //下单人
			// 游客id
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, travellerId);
			// 游客名称
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, travellerName);
			variables.put(Context.REVIEW_VARIABLE_KEY_OLD_TRAVELER_PAY_PRICE, oldTravelerPayPrice);  //转团后游客结算价
			variables.put(Context.REVIEW_VARIABLE_KEY_NEW_TRAVELER_PAY_PRICE, newTravelerPayPrice);  //转入团游客结算价
			//转款金额uuid
			variables.put(Context.REVIEW_VARIABLE_KEY_TRANSFER_MONEY_UUID, transferMoneyUuid);
			//转款备注
			variables.put(Context.REVIEW_VARIABLE_KEY_TRANSFER_MONEY_REMARK, remark);
			//转团前游客所在订单id
			variables.put(Context.REVIEW_VARIABLE_KEY_OLD_ORDERID, orderId);
			//转团后游客所在订单id
			variables.put(Context.REVIEW_VARIABLE_KEY_NEW_ORDERID, inOrderId);
			//转团后新游客id
			variables.put(Context.REVIEW_VARIABLE_KEY_NEW_TRAVELLER_ID, newTravellerId);
			
			variables.putAll(orderInfo);
			variables.putAll(productInfo);
			
			ReviewResult reviewResult = null;
			CommonResult commonResult = reivewMutexService.check(orderId, travellerId.toString(), productType.toString(), Context.REVIEW_FLOWTYPE_TRANSFER_MONEY.toString(), false);
			if (commonResult != null && commonResult.getSuccess()) {				
				//发起申请审批流程
				reviewResult = reviewService.start(userId.toString(), companyUuid, permissionChecker, null, productType, Context.REVIEW_FLOWTYPE_TRANSFER_MONEY, deptId, "", variables);
			} else {
				resultMap.put("res", commonResult.getMessage() + "<//br>请重新选择游客！");
				resultMap.put("msg", "faild");
				return resultMap;
			}
			
			//如果发起流程失败，则返回；如果成功则处理存储金额等
			if (!reviewResult.getSuccess()) {
				resultMap.put("res", reviewResult.getMessage());
				resultMap.put("msg", "faild");
				return resultMap;
			} else {
				//流程ID
				String rid = reviewResult.getReviewId();
				//保存转款金额到 新表
				for(int j = 0; j < size; j++){
					String[] money = moneys[j].split("\\|");
					NewProcessMoneyAmount amount = new NewProcessMoneyAmount();
					amount.setId(UuidUtils.generUuid());
					amount.setSerialNum(transferMoneyUuid);
					amount.setCurrencyId(Integer.parseInt(money[0]));
					amount.setAmount(new BigDecimal(money[1]));
					amount.setMoneyType(Context.MONEY_TYPE_ZK);
					amount.setOrderType(productType);  //此处存储的是old订单的类型
					amount.setUid(Long.parseLong(orderId));  //old订单的id
					amount.setBusindessType(1);
					amount.setCreatedBy(userId);
					amount.setCreateTime(new Date());
					for (int k = 0; k < accountedMoneyList.size(); k++) {
						MoneyAmount accMoney = accountedMoneyList.get(k);
						if (Integer.parseInt(money[0]) == accMoney.getCurrencyId()) {						
							amount.setExchangerate(accMoney.getExchangerate());
							break;
						}
					}
					amount.setReviewId(rid);
					amount.setCompanyId(companyUuid);
					amount.setDelFlag(Context.DEL_FLAG_NORMAL);
					processMoneyAmountService.saveNewProcessMoneyAmount(amount);
					
					//本条转款申请成功，添加到累计金额中，以便下一条申请进行条件判断
					for (Map<String, Object> applyedMoney : totalApplyMoneyList) {
						if (Integer.parseInt(applyedMoney.get("currencyId").toString()) == Integer.parseInt(money[0])) {
							applyedMoney.put("amount", new BigDecimal(money[1]).add(new BigDecimal(applyedMoney.get("amount").toString())));
							break;
						}
					}					
				}
			}
		}
		return resultMap;
	}
	
	/**
	 * 转款审批详情（申请、审批公用）
	 * @author yang.jiang 2014年12月30日
	 * @param reviewId
	 * @return
	 */
	@RequestMapping(value ="transferMoneyDetails/{reviewId}")
	public String transferMoneyDetails(@PathVariable String reviewId,  Model model, HttpServletRequest request) {
		Map<String, String> condition = new HashMap<String, String>();
		condition.put("reviewId", reviewId);
		Map<String, Object> reviewDetailMap = reviewService.getReviewDetailMapByReviewId(reviewId);
		Map<String, Object> baseInfoMap = transferMoneyService.transferMoneyDetails(condition);
		model.addAttribute("reviewDetail", reviewDetailMap);
		model.addAllAttributes(baseInfoMap);
		model.addAttribute("rid", reviewId);  //日志页专用
	    return "review/transfermoney/singlegroup/transferMoneyDetials";
	}
	
	/**
	 * 取消申请
	 * @author jyang
	 * @时间 2014年12月30日
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value ="transfersMoneyCancel" , method=RequestMethod.POST)
	public Map<String, String> transfersMoneyCancel(Model model, HttpServletRequest request) {
		Map<String, String> condition = new HashMap<String, String>();
		String companyUuid = UserUtils.getUser().getCompany().getUuid().toString();
		String userId = UserUtils.getUser().getId().toString();
		condition.put("companyUuid", companyUuid);
		condition.put("userId", userId);
		condition.put("reviewId", request.getParameter("reviewId"));
		condition.put("travellerId", request.getParameter("travellerId"));
		Map<String, String> resultMap = transferMoneyService.transfersMoneyCancel(condition);
	    return  resultMap;
	}
	
	/**
	 * 转款审批列表
	 * add by jyang 2015年11月4日13:47:11
	 * @param menuName 菜单模块名
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="transferMoneyReviewList/{menuName}")
	public String reviewTransferMoneyList(@PathVariable String menuName, Model model, HttpServletResponse response, HttpServletRequest request){
		List<SysOfficeProductType> processTypes = sysOfficeConfigurationService.obtainOfficeProductTypes(UserUtils.getUser().getCompany().getUuid());
		//转团审核列表产品类型屏蔽：机票，签证，酒店 addby yunpeng.zhang 2015年12月29日
		List<SysOfficeProductType> resultProcessTypes = new ArrayList<>();
		for(SysOfficeProductType processType : processTypes) {
			if(!(processType.getProductType() == 6 || processType.getProductType() == 7 || processType.getProductType() == 10)) {
				resultProcessTypes.add(processType);
			}
		}
		Map<String, Object> params = prepareParams(request, response);
		Page<Map<String, Object>> pageInfo=newOrderReviewService.getTransferMoneyReviewList(params);
		model.addAttribute("conditionsMap", params);
		model.addAttribute("page", pageInfo);
		model.addAttribute("processTypes", resultProcessTypes);
		if (Context.MENU_FLAG_REVIEW.equals(menuName)) {			
			return "review/transfermoney/singlegroup/transferMoneyReviewList";  //审核模块
		} else if (Context.MENU_FLAG_REVIEW4CW.equals(menuName)) {
			return "review/transfermoney/singlegroup/transferMoneyReviewList4CW";  //财务审核模块
		}
		return "";
	}
	
	/**
	 * to转款审批页面
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="toTransferMoneyReview")
	public String toReviewTransferMoney(HttpServletResponse response, Model model, HttpServletRequest request){
		String reviewId = request.getParameter("rid");
		String orderId = request.getParameter("orderId");
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		//TODO
		
		Map<String, String> condition = new HashMap<String, String>();
		condition.put("reviewId", reviewId);
		Map<String, Object> baseInfoMap = transferMoneyService.transferMoneyDetails(condition);//订单详情（转入、转出）
		Map<String, Object> reviewDetailMap = reviewService.getReviewDetailMapByReviewId(reviewId);//审批详情
		model.addAttribute("reviewDetail", reviewDetailMap);
		model.addAllAttributes(baseInfoMap);
		
		List<Map<String,Object>> rdlist = new ArrayList<Map<String,Object>>();
		Map<String,Object>  processMap = null;
		try{
			if(reviewId!=null){
				processMap = reviewService.getReviewDetailMapByReviewId(reviewId);
				rdlist.add(processMap);
			}
		}catch(Exception e){
			log.error("根据reviewid： " + reviewId + " 查询出来reviewDetail明细报错 ",e);
		}
		//TODO 显示的业务数据
		if(reviewId!=null&&!"".equals(reviewId)){//显示动态审批的标志
			
	    	List<ReviewLogNew> rLog = reviewService.getReviewLogByReviewId(reviewId);
	    	
	    	model.addAttribute("rLog",rLog);
	    }
		model.addAttribute("reviewId", reviewId);
		List<Currency> currencyList = currencyService.findCurrencyList(companyId);
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("spaceGradelist", DictUtils.getDictList("spaceGrade_Type"));// 舱位等级
		model.addAttribute("airspacelist", DictUtils.getDictList("airspace_Type"));// 舱位
		
		model.addAttribute("rid",reviewId);
		return "review/transfermoney/singlegroup/toTransferMoneyReview";
	}	
	
	/**
	 * 审批转款，审批通过或驳回
	 * @author yang.jiang  2015年11月4日14:17:30
	 * @param rid 审批id
	 */
	@ResponseBody
	@RequestMapping(value ="transferMoneyReview")
	public Map<String, Object> reviewTransferMoney(HttpServletResponse response, Model model, HttpServletRequest request,String rid,Integer result){
		
		String remark = request.getParameter("remark");
		Map<String, Object> map =new HashMap<String,Object>();
		ReviewResult r = null;
		try {
			// result 等于  1 审批通过  ， 等于 0  驳回
			if(result==1){
				 r=reviewService.approve(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid().toString(),"", userReviewPermissionChecker, rid, remark, null);
				 
				 //审批通过时候的业务数据操作				 
				 if(r.getSuccess()){
					 if(r.getReviewStatus()==2){
						 map = newTransferMoneyService.reviewTransferMoneyDone(r.getReviewId());
					 }
					 map.put("flag", 1);
				 } else {
					 map.put("flag", 0);
					 map.put("message", r.getMessage());
				 }
			}else if(result==0){
				 r=reviewService.reject(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid().toString(), "", rid, remark, null);
				 if(r.getSuccess()){
					 map.put("flag", 1);
				 }else {
					 map.put("flag", 0);
					 map.put("message", r.getMessage());
				}
			}
		} catch (Exception e) {
			map.put("flag", 0);
			map.put("message", e.getMessage());
			return map;
		}
		return map;
	}	
	
	/**
	 * 批量转款审批的审批通过或驳回
	 * @author yang.jiang 2015-12-2 18:24:11
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "transferMoneyBatchReview")
	public Map<String, Object> transferMoneyBatchReview(Model model, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> map =new HashMap<String,Object>();
		// 1 组织参数		
		String revIds = request.getParameter("revIds");//审批表ids
		String remark = request.getParameter("remark");//通过/驳回原因
		String strResult = request.getParameter("result");//通过/驳回选择
		
		Map<String, Object> param = Maps.newHashMap();
		param.put("revIds", revIds);
		param.put("remark", remark);
		param.put("strResult", strResult);
		
		//批量审批，成功后进行转款
		map = newTransferMoneyService.batchTransferMoneyReview(param);		
		return map;
	}
	
	/**
	 * 转款撤销审批
	 * @author yang.jiang  2015年11月4日15:25:31
	 */
	@ResponseBody
	@RequestMapping(value = "backReviewTransferAmount")
	public Map<String, Object> backReviewTransferAmount(HttpServletResponse response, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		String rid = request.getParameter("rid");
		String statusStr = request.getParameter("status");
		Integer status = -1;
		if (StringUtils.isNotBlank(statusStr)) {
			status = Integer.parseInt(statusStr);
		}
		ReviewResult r = null;
		if(status!=1){
			result.put("flag", "error");
			result.put("msg", "审批状态不正确");
		    return result;
		}
		//撤销 备注  撤销暂无备注
		String comment = "";
		r =reviewService.back(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid(), "", rid, comment, null);
		
		if(r.getSuccess()){
			result.put("flag", "success");
			result.put("msg", r.getMessage());
			return result;
		}else if(!r.getSuccess()){
			result.put("flag", "error");
			result.put("msg", r.getMessage());
		}
		
        return result;
	}
	
	/**
	 * 封装订单信息数据
	 * 
	 * @author yunpeng.zhang
	 * @CreateTime 2015年11月18日
	 * @param productOrder
	 * @return
	 */
	private Map<String, Object> getOrderInfo(ProductOrderCommon productOrder, ActivityGroup productGroup) {
		
		Map<String, Object> orderInfo = new HashMap<String, Object>();
		// 订单id
		Long productId = productOrder.getId();
		orderInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID, productId);
		// 下单人
		Long createById = productOrder.getCreateBy().getId();
		orderInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR, createById);
		// 销售
		Integer salerId = productOrder.getSalerId();
		orderInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR, salerId);
		// 下单时间
		Date orderTime = productOrder.getOrderTime();
		orderInfo.put(Context.REVIEW_VARIABLE_KEY_ORDER_TIME, orderTime);
		// 订单编号
		String orderNum = productOrder.getOrderNum();
		orderInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO, orderNum);
		// 订单团号
		String groupCode = productGroup.getGroupCode();
		orderInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, groupCode);
		// 订单总额
		String totalMoneySerialNum = productOrder.getTotalMoney();
		String totalMoney = moneyAmountService.getMoneyStr(totalMoneySerialNum);
		orderInfo.put(Context.REVIEW_VARIABLE_KEY_TOTAL_MONEY, totalMoney);
		// 渠道商
		Long agentId = productOrder.getOrderCompany();
		orderInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT, agentId);

		return orderInfo;
	}

	/**
	 * 封装产品信息
	 * 
	 * @author yunpeng.zhang
	 * @CreateTime 2015年11月18日
	 * @param product
	 * @return
	 */
	private Map<String, Object> getProductInfo(TravelActivity product) {
		Map<String, Object> productInfo = new HashMap<String, Object>();
		// 产品id
		Long productId = product.getId();
		productInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID, productId);
		// 产品名称
		String acitivityName = product.getAcitivityName();
		productInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, acitivityName);
		// 出团日期
		Date groupOpenDate = product.getGroupOpenDate();
		productInfo.put(Context.REVIEW_VARIABLE_KEY_GROUP_OPEN_DATE, groupOpenDate);
		// 行程天数
		Integer activityDuration = product.getActivityDuration();
		productInfo.put(Context.REVIEW_VARIABLE_KEY_ACTIVITY_DURATION, activityDuration);
		// 目的地
		String targetAreaNames = product.getTargetAreaNames();
		productInfo.put(Context.REVIEW_VARIABLE_KEY_TARGETAREANAMES, targetAreaNames);
		// 操作人 即 计调
		Long operatorId = product.getCreateBy().getId();
		productInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, operatorId);
		// 成人单价
		// 儿童单价
		// 特殊人群单价
		// 成人出行人数
		// 儿童出行人数
		// 特殊人群出行人数
		// 出发城市

		return productInfo;
	}
	
	/**
	 * 组织操作参数
	 * @param request
	 * @return
	 */
	private Map<String, Object> prepareParams(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		/**获取参数 start*/
		//团号/产品名称/订单号
		String orderCdGroupCdProductNm = request.getParameter("orderCdGroupCdProductNm") == null ? null : request.getParameter("orderCdGroupCdProductNm").toString();
		//产品类型(id)
		String productType = request.getParameter("productType") == null ? null : request.getParameter("productType").toString();
		//渠道商(id)
		String agentId = request.getParameter("agentId") == null ? null : request.getParameter("agentId").toString();
		//申请日期（from）
		String applyDateFrom = request.getParameter("applyDateFrom") == null ? null : request.getParameter("applyDateFrom").toString();
		//申请日期（to）
		String applyDateTo = request.getParameter("applyDateTo") == null ? null : request.getParameter("applyDateTo").toString();
		//审批发起人(id)
		String applyPerson = request.getParameter("applyPerson") == null ? null : request.getParameter("applyPerson").toString();
		//计调(id)
		String operator = request.getParameter("operator") == null ? null : request.getParameter("operator").toString();
		//转款金额 (from)
		String transferMoneyFrom = request.getParameter("transferMoneyFrom") == null ? null : request.getParameter("transferMoneyFrom").toString();
		//转款金额 (to)
		String transferMoneyTo = request.getParameter("transferMoneyTo") == null ? null : request.getParameter("transferMoneyTo").toString();
		//审批状态
		String reviewStatus = StringUtils.isBlank(request.getParameter("reviewStatus")) ? null : request.getParameter("reviewStatus").toString();
		//出纳确认
		String cashConfirm = request.getParameter("cashConfirm") == null ? null : request.getParameter("cashConfirm").toString();
		//打印状态
		String printStatus = request.getParameter("printStatus") == null ? null : request.getParameter("printStatus").toString();
		//页签选择状态
		String statusChoose = request.getParameter("statusChoose") == null ? "1" : request.getParameter("statusChoose").toString();
		// 创建日期排序标识
		String orderCreateDateSort  = request.getParameter("orderCreateDateSort");
		// 更新日期排序标识
		String orderUpdateDateSort = request.getParameter("orderUpdateDateSort");
		//分配排序
		if ((StringUtils.isBlank(orderCreateDateSort) && StringUtils.isBlank(orderUpdateDateSort)) 
				|| (StringUtils.isNotBlank(orderCreateDateSort) && StringUtils.isNotBlank(orderUpdateDateSort))) {  //都为空，则默认是创建时间
			orderCreateDateSort = "desc";
		}
		//订单更新日期排序标识
		String orderUpdateDateCss = request.getParameter("orderUpdateDateCss");
		//订单创建日期排序标识
		String orderCreateDateCss  = request.getParameter("orderCreateDateCss") == null ? "activitylist_paixu_moren" : StringUtils.isBlank(orderUpdateDateCss) ? "activitylist_paixu_moren" : request.getParameter("orderCreateDateCss"); //默认按照创建日期排序
		//page对象
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(request, response);
		/**获取参数 end*/
		/**组装参数 start*/
		result.put("orderCdGroupCdProductNm", orderCdGroupCdProductNm);
		result.put("productType", productType);
		result.put("agentId", agentId);
		result.put("applyDateFrom", applyDateFrom);
		result.put("applyDateTo", applyDateTo);
		result.put("applyPerson", applyPerson);
		result.put("operator", operator);
		result.put("transferMoneyFrom", transferMoneyFrom);
		result.put("transferMoneyTo", transferMoneyTo);
		result.put("reviewStatus", reviewStatus);
		result.put("cashConfirm", cashConfirm);
		result.put("printStatus", printStatus);
		result.put("statusChoose", statusChoose);
		result.put("orderCreateDateSort", orderCreateDateSort);
		result.put("orderUpdateDateSort", orderUpdateDateSort);
		result.put("orderCreateDateCss", orderCreateDateCss);
		result.put("orderUpdateDateCss", orderUpdateDateCss);
		result.put("paymentType", request.getParameter("paymentType"));
		result.put("pageP", page);
		/**组装参数 end*/
		return result;
	}

}
