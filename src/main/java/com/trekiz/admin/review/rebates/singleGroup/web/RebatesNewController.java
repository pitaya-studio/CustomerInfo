package com.trekiz.admin.review.rebates.singleGroup.web;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.trekiz.admin.modules.rebatesupplier.entity.RebateSupplier;
import com.trekiz.admin.modules.rebatesupplier.service.RebateSupplierService;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.User;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.rebates.entity.Rebates;
import com.trekiz.admin.modules.order.rebates.entity.RebatesNew;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.review.common.service.ICommonReviewService;
import com.trekiz.admin.review.rebates.singleGroup.service.RebatesNewService;

@Controller
@RequestMapping(value="${adminPath}/rebatesNew")
public class RebatesNewController {

	@Autowired
	private RebatesNewService rebatesService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private OrderCommonService orderService;
	@Autowired
	private TravelActivityService travelActivityService;
	@Autowired
	private com.quauq.review.core.engine.ReviewService processReviewService;
	@Autowired
	private ActivityGroupService activityGroupService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private TravelerService travelerService;
	@Autowired
	private ICommonReviewService commonReviewService;
	@Autowired
	private RebateSupplierService rebateSupplierService;
	
	/** 返佣申请列表地址 */
	private static final String LIST_PAGE = "/review/rebates/singleGroup/rebatesList";
	/** 返佣申请页面地址 */
	private static final String REBATES_PAGE = "/review/rebates/singleGroup/rebatesPage";
	/** 返佣详情地址 */
	private static final String REBATES_INFO_PAGE = "/review/rebates/singleGroup/rebatesInfo";
	
	
	/**
	 * @Description 返佣申请列表
	 * @param orderId 订单id
	 * @author yakun.bai
	 * @Date 2015-11-25
	 */
	@RequestMapping(value="list")
	public String list(@RequestParam("orderId") Long orderId, @RequestParam("orderType") Integer orderType, Model model) {
		
		//订单查询
		ProductOrderCommon order = orderService.getProductorderById(orderId);
		//返佣记录查询
		List<RebatesNew> rebatesList = rebatesService.findRebatesList(orderId, orderType);
		
		//查询订单预计返佣金额
		if (order != null && StringUtils.isNotBlank(order.getScheduleBackUuid())) {
			//查询预计返佣金额实体并获取相应币种名称、币种符号、金额
			MoneyAmount mo = moneyAmountService.findOneAmountBySerialNum(order.getScheduleBackUuid());
			if (mo != null) {
				Currency currency = currencyService.findCurrency(Long.valueOf(mo.getCurrencyId()));
				if (currency != null) {
					model.addAttribute("currencyName", currency.getCurrencyName());
					model.addAttribute("currencyMark", currency.getCurrencyMark());
					model.addAttribute("amount", mo.getAmount());
				}
			}
		}
		
		//值传递
		model.addAttribute("orderId", orderId);
		model.addAttribute("orderType", orderType);
		model.addAttribute("orderTypeStr", OrderCommonUtil.getChineseOrderType(orderType.toString()));
		model.addAttribute("rebatesList", rebatesList);
		
		return LIST_PAGE;		
	}
	
	/**
	 * @Description 返佣申请页面
	 * @param orderId 订单id
	 * @param orderType 订单类型
	 * @author yakun.bai
	 * @Date 2015-11-27
	 */
	@RequestMapping("/rebatesPage")
	public String rebatesPage(@RequestParam("orderId") Long orderId, @RequestParam("orderType") Integer orderType, Model model) {
		
		//查询订单、团期、产品
		ProductOrderCommon productOrder = orderService.getProductorderById(orderId);
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		
		//返佣币种获取：订单应收金额的币种
		List<MoneyAmount> totalMoneyList = moneyAmountService.findAmountBySerialNum(productOrder.getTotalMoney());
		List<Currency> currencyList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(totalMoneyList)) {
			for (MoneyAmount moneyAmount : totalMoneyList) {
				Currency currency = currencyService.findCurrency(moneyAmount.getCurrencyId().longValue());
				currencyList.add(currency);
			}
		}
		
		//获取团队原返佣金额
		List<Object[]> teamList = rebatesService.getTeamRebates(orderId);
		
		List<Rebates> travelerRebatesList = Lists.newArrayList();
		// 获取游客信息
		List<Integer> delFlag = Lists.newArrayList();
		delFlag.add(Context.TRAVELER_DELFLAG_NORMAL);
		delFlag.add(Context.TRAVELER_DELFLAG_EXIT);
		delFlag.add(Context.TRAVELER_DELFLAG_TURNROUND);
		List<Traveler> travelerList = travelerService.findTravelerByOrderIdAndOrderType(Long.valueOf(orderId), orderType, delFlag);
		Rebates rebates = null;
		Long currencyId = currencyList.get(0).getId();
		for (Traveler traveler: travelerList) {
			
			//根据游客ID查询审核通过的返佣记录
			List<RebatesNew> rebatesList = rebatesService.findRebatesByTravelerAndStatus(traveler.getId());
			//游客原返佣实体
			RebatesNew travelerOldRebates =  null;
			if (CollectionUtils.isNotEmpty(rebatesList)) {
				travelerOldRebates = rebatesList.get(0);
			}
			//返佣原返佣金额
			String oldRebates = "";
			if (travelerOldRebates != null && StringUtils.isNotBlank(travelerOldRebates.getNewRebates())) {
				oldRebates = travelerOldRebates.getNewRebates();
			}
			//组合成一个返佣实体（用于前台展示）
			rebates = new Rebates(orderId, traveler, currencyId, oldRebates);
			travelerRebatesList.add(rebates);
		}
		
		//获取报名时的团队返佣参考金额
		List<RebatesNew> groupReList = rebatesService.getGroupReList(orderId);
		
		//获取预订团队返佣
		if (productOrder != null && StringUtils.isNotBlank(productOrder.getScheduleBackUuid())) {
			MoneyAmount mo = moneyAmountService.findOneAmountBySerialNum(productOrder.getScheduleBackUuid());
			if (mo != null) {
				Currency currency = currencyService.findCurrency(Long.valueOf(mo.getCurrencyId()));
				if (currency != null) {
					model.addAttribute("currencyName",currency.getCurrencyName());
					model.addAttribute("currencyMark",currency.getCurrencyMark());
					model.addAttribute("amount",mo.getAmount());
				}
			}
		}

		//返佣对象 added by zhenxing.yan
		User user=UserUtils.getUser();
		Office company=user.getCompany();
		if(Integer.valueOf(1).equals(company.getIsAllowMultiRebateObject())){//允许多返佣对象
			List<RebateSupplier> suppliers=rebateSupplierService.obtainSupplierWithOperator(company.getUuid(),user.getId());
			model.addAttribute("multiRebateObject",true);
			model.addAttribute("suppliers",suppliers);
		}else{
			model.addAttribute("multiRebateObject",false);
		}


		
		//值传递
		model.addAttribute("orderTypeStr", OrderCommonUtil.getChineseOrderType(orderType.toString()));
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("teamList", teamList);
		model.addAttribute("travelerRebatesList", travelerRebatesList);
		model.addAttribute("groupReList",groupReList);
		model.addAttribute("productOrder", productOrder);
		model.addAttribute("product", product);
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(orderType.toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		
		return REBATES_PAGE;
	}
	
	/**
	 * @Description 返佣申请
	 * @author yakun.bai
	 * @Date 2015-12-1
	 */
	@RequestMapping(value="applyRebate")
	@ResponseBody
	public Object applyRebate(HttpServletRequest request) {
		String strmsg = "";
		try {
			//获取返佣参数的json数组
			JSONArray rebatesJSONArr = JSONArray.fromObject(request.getParameter("rebatesList"));
			List<RebatesNew> rebatesList = Lists.newArrayList();
			RebatesNew rebates = null;
			Traveler traveler;
			Map<Long,String> travelerMap = Maps.newHashMap();
			/**
			 * 返佣对象信息
			 */
			Map<String,String> rebatesObjectInfo=new HashMap<>();
			boolean setFlag=true;
			for (int i = 0; i < rebatesJSONArr.size(); i++) {
				traveler = null;
				String totalMoney = "";
				JSONObject rebatesJSONObj = rebatesJSONArr.getJSONObject(i);
				String orderId = rebatesJSONObj.getString("orderId");
				Long currencyId = Long.parseLong(rebatesJSONObj.getString("currencyId"));
				BigDecimal rebatesDiff = new BigDecimal(rebatesJSONObj.getString("rebatesDiff"));
				String oldRebates = rebatesJSONObj.getString("oldRebates");
				String costname = rebatesJSONObj.getString("costname");
				String remark = rebatesJSONObj.getString("remark");
				Integer orderType = rebatesJSONObj.getInt("orderType");
				String newRebates = rebatesJSONObj.getString("newRebates");
				if (StringUtils.isNotBlank(rebatesJSONObj.getString("travelerId"))) {
					traveler = travelerService.findTravelerById(Long.parseLong(rebatesJSONObj.getString("travelerId")));
					totalMoney = traveler.getPayPriceSerialNum();
					travelerMap.put(traveler.getId(), traveler.getName());
				} else {
					totalMoney = orderService.getProductorderById(Long.parseLong(orderId)).getTotalMoney();
				}
				rebates = new RebatesNew(Long.parseLong(orderId), traveler, currencyId, totalMoney, costname, oldRebates, rebatesDiff, newRebates, remark, orderType);
				rebatesList.add(rebates);

				/**
				 * 组装返佣对象信息
				 */
				if (setFlag){
					if(rebatesJSONObj.has(Context.REBATES_OBJECT_TYPE)){
						rebatesObjectInfo.put(Context.REBATES_OBJECT_TYPE,rebatesJSONObj.getString(Context.REBATES_OBJECT_TYPE));
					}
					if(rebatesJSONObj.has(Context.REBATES_OBJECT_ID)){
						rebatesObjectInfo.put(Context.REBATES_OBJECT_ID,rebatesJSONObj.getString(Context.REBATES_OBJECT_ID));
					}
					if(rebatesJSONObj.has(Context.REBATES_OBJECT_NAME)){
						rebatesObjectInfo.put(Context.REBATES_OBJECT_NAME,rebatesJSONObj.getString(Context.REBATES_OBJECT_NAME));
					}
					if(rebatesJSONObj.has(Context.REBATES_OBJECT_ACCOUNT_ID)){
						rebatesObjectInfo.put(Context.REBATES_OBJECT_ACCOUNT_ID,rebatesJSONObj.getString(Context.REBATES_OBJECT_ACCOUNT_ID));
					}
					if(rebatesJSONObj.has(Context.REBATES_OBJECT_ACCOUNT_TYPE)){
						rebatesObjectInfo.put(Context.REBATES_OBJECT_ACCOUNT_TYPE,rebatesJSONObj.getString(Context.REBATES_OBJECT_ACCOUNT_TYPE));
					}
					if(rebatesJSONObj.has(Context.REBATES_OBJECT_ACCOUNT_BANK)){
						rebatesObjectInfo.put(Context.REBATES_OBJECT_ACCOUNT_BANK,rebatesJSONObj.getString(Context.REBATES_OBJECT_ACCOUNT_BANK));
					}
					if(rebatesJSONObj.has(Context.REBATES_OBJECT_ACCOUNT_CODE)){
						rebatesObjectInfo.put(Context.REBATES_OBJECT_ACCOUNT_CODE,rebatesJSONObj.getString(Context.REBATES_OBJECT_ACCOUNT_CODE));
					}
					setFlag=false;
				}
			}
			strmsg = rebatesService.save(rebatesList,rebatesObjectInfo);
		} catch(Exception e) {
			e.printStackTrace();  
			return e.toString();
		}
		return strmsg;
	}
	
	/**
	 * 查询单个返佣信息
	 * @param model
	 * @return
	 */
	@RequestMapping(value="rebatesInfo")
	public String rebatesInfo(@RequestParam("rebatesId") Long rebatesId, Model model) {
		
		//查询返佣及订单信息
		RebatesNew rebates = rebatesService.findRebatesById(rebatesId);
		ProductOrderCommon productOrder = orderService.getProductorderById(rebates.getOrderId());
		//查询产品信息
		TravelActivity product = null;
		if (productOrder != null && productOrder.getProductId() != null) {
			product = travelActivityService.findById(productOrder.getProductId());
		}
		//查询团期信息
		ActivityGroup productGroup = null;
		if (productOrder != null && productOrder.getProductGroupId() != null) {
			productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		}
		
		//获取预订团队返佣
		if (productOrder != null && StringUtils.isNotBlank(productOrder.getScheduleBackUuid())) {
			MoneyAmount mo = moneyAmountService.findOneAmountBySerialNum(productOrder.getScheduleBackUuid());
			if (mo != null) {
				Currency currency = currencyService.findCurrency(Long.valueOf(mo.getCurrencyId()));
				if (currency != null) {
					model.addAttribute("currencyName",currency.getCurrencyName());
					model.addAttribute("currencyMark",currency.getCurrencyMark());
					model.addAttribute("amount",mo.getAmount());
				}
			}
		}

		//返佣对象 added by zhenxing.yan
		User user=UserUtils.getUser();
		Office company=user.getCompany();
		if(Integer.valueOf(1).equals(company.getIsAllowMultiRebateObject())){//允许多返佣对象
			model.addAttribute("multiRebateObject",true);
			//组装返佣对象信息
			Map<String,Object> reviewInfos=reviewService.getReviewDetailMapByReviewId(rebates.getReview().getId());
			model.addAttribute("reviewInfos",reviewInfos);
		}else{
			model.addAttribute("multiRebateObject",false);
		}

		//值传递
		model.addAttribute("product", product);
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("productOrder", productOrder);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		model.addAttribute("rebates", rebates);
		
		return REBATES_INFO_PAGE;
	}
	
	/**
	 * @Description 取消返佣申请
	 * @author yakun.bai
	 * @Date 2015-12-5
	 */
	@ResponseBody
	@RequestMapping("/cancelRebates")
	public Map<String, Object> cancelRebates(String reviewId) {
		String companyId = UserUtils.getUser().getCompany().getId().toString();
		String userId = UserUtils.getUser().getId().toString();
		Map<String, Object> result = Maps.newHashMap();

		//取消改价申请
		ReviewResult reviewResult = processReviewService.cancel(userId, companyId, "", reviewId, "", null);

		boolean flag = reviewResult.getSuccess();
		if (flag) {
			result.put("result", "success");
			result.put("msg", reviewResult.getMessage());
			// 对成本录入进行更改
			ReviewNew review = reviewService.getReview(reviewId);
			commonReviewService.updateCostRecordStatus(review, 3);
		} else {
			result.put("result", "faild");
			result.put("msg", reviewResult.getMessage());
		}

		return result;
	}
	
}