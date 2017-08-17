package com.trekiz.admin.review.changePrice.singleGroup.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.Costchange;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.CostchangeService;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.review.changePrice.singleGroup.service.IChangePriceNewService;

@Controller
@RequestMapping(value="${adminPath}/newChangePrice")
public class ChangePriceNewController {

	@Autowired
	private IChangePriceNewService changePriceService;
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
	private CostchangeService costchangeService;
	@Autowired
	private TravelerService travelerService;
	
	/** 改价申请列表地址 */
	private static final String LIST_PAGE = "/review/changePrice/singleGroup/changePriceList";
	/** 改价申请页面地址 */
	private static final String CHANGE_PRICE_PAGE = "/review/changePrice/singleGroup/changePricePage";
	/** 改价详情地址 */
	private static final String CHANGE_PRICE_INFO_PAGE = "/review/changePrice/singleGroup/changePriceInfo";
	
	
	/**
	 * @Description 改价申请列表
	 * @param orderId 订单id
	 * @param productType 订单类型
	 * @author yakun.bai
	 * @Date 2015-11-20
	 */
	@RequestMapping(value="list")
	public String list(@RequestParam("orderId") Long orderId, @RequestParam("productType") Integer productType, Model model) {
		
		//查询订单
		ProductOrderCommon productOrder = orderService.getProductorderById(orderId);
		
		//查询改价申请列表
		List<Map<String, Object>> changePriceList = changePriceService.getReviewList(orderId, productType);
		handlerChangePriceList(changePriceList, productOrder);
		
		//值传递
		model.addAttribute("changePriceList", changePriceList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("productType", productType);
		model.addAttribute("orderTypeStr", OrderCommonUtil.getChineseOrderType(productType.toString()));
		return LIST_PAGE;		
	}

	private void handlerChangePriceList(List<Map<String, Object>> changePriceList, ProductOrderCommon productOrder) {
		for (int i = 0; i < changePriceList.size(); i++) {
			Map<String, Object> map = changePriceList.get(i);
			Traveler traveller = travelerService.findTravelerById(Long.parseLong(map.get("travellerId").toString()));
			//改前金额
			List<MoneyAmount> originalMoneyList = moneyAmountService.findAmountBySerialNum(traveller.getOriginalPayPriceSerialNum());
			map.put("originalMoneyList", originalMoneyList);

			//游客结算价
			List<MoneyAmount> payMoneyList = moneyAmountService.findAmountBySerialNum(traveller.getPayPriceSerialNum());
			map.put("payMoneyList", payMoneyList);

			//改价差额
			changePriceService.handlerMap(map, productOrder);
		}
	}
	
	/**
	 * @Description 改价申请页面
	 * @param orderId 订单id
	 * @param productType 订单类型
	 * @author yakun.bai
	 * @Date 2015-11-20
	 */
	@RequestMapping("/changePricePage")
	public String changePricePage(@RequestParam("orderId") Long orderId, @RequestParam("productType") Integer productType, Model model) {
		
		//查询订单
		ProductOrderCommon productOrder = orderService.getProductorderById(orderId);
		//查询产品
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		//查询团期
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		
		//查询游客信息（只查询正常游客：包括退团审核中和转团审核中游客）
		List<Traveler> travelerList = changePriceService.queryTravelerList(orderId, productOrder.getOrderStatus());
		//查询币种信息
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		//原始应收价和结算价map封装
		handlePrice(travelerList, currencylist, model);

		JSONArray jsonArray = new JSONArray();
		for (int i = 0; i < currencylist.size(); i++) {
			Currency currency =  currencylist.get(i);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("currencyId", currency.getId());
			jsonObject.put("currencyMark", currency.getCurrencyMark());
			jsonObject.put("currencyName", currency.getCurrencyName());
			jsonArray.add(jsonObject);
		}
		model.addAttribute("currencyString", jsonArray);

		//值传递
		model.addAttribute("orderType", productOrder.getOrderStatus());// 订单类型
		model.addAttribute("priceType", productOrder.getPriceType());// 价格类型
		model.addAttribute("retailAdultPrice", productGroup.getAdultRetailPrice());// 成人供应价
		model.addAttribute("retailChildPrice", productGroup.getChildRetailPrice());// 儿童供应价
		model.addAttribute("retailSpecialPrice", productGroup.getSpecialRetailPrice());// 特殊人群供应价
		model.addAttribute("travelerList", travelerList);
		model.addAttribute("currencylist", currencylist);
		model.addAttribute("productOrder", productOrder);
		model.addAttribute("product", product);
		model.addAttribute("productGroup", productGroup);
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		
		return CHANGE_PRICE_PAGE;
	}
	
	/**
	 * @Description 获取用户所在批发商下币种信息、获取原始金额和结算金额信息
	 * @author yakun.bai
	 * @Date 2015-11-23
	 */
	private void handlePrice(List<Traveler> travelerList, List<Currency> currencylist, Model model) {
		
		//币种标志map
		Map<Integer, String> currencyMarkMap = Maps.newHashMap();
		//币种名称map
		Map<Integer, String> currencyNameMap = Maps.newHashMap();
		//游客列表
		List<Map<String, Object>> travelers = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(currencylist)) {
			for (Currency currency : currencylist) {
				currencyMarkMap.put(currency.getId().intValue(), currency.getCurrencyMark());
				currencyNameMap.put(currency.getId().intValue(), currency.getCurrencyName());
			}
		}
		model.addAttribute("currencylist", currencylist);
		model.addAttribute("currencyMarkMap", currencyMarkMap);
		model.addAttribute("currencyNameMap", currencyNameMap);
		if (CollectionUtils.isNotEmpty(travelerList)) {
			for (Traveler t : travelerList) {
				Map<String, Object> traveler = Maps.newHashMap();
				//游客id
				traveler.put("id", t.getId());
				//游客姓名
				traveler.put("travelerName", t.getName());
				//游客原始应收价格
				List<MoneyAmount> originalMoneyList = moneyAmountService.findAmountBySerialNum(t.getOriginalPayPriceSerialNum());
				traveler.put("originalMoneyList", originalMoneyList);
				
				// forbug 15474
				//游客原始应收币种标志map
				Map<Integer, String> originalCurrencyMarkMap = Maps.newHashMap();
				//游客原始应收金额数量标志map
				Map<Integer, String> originalMonyAmoutMap = Maps.newHashMap();
				List<Currency> currencys = currencyService.findCurrcyListBySerium(t.getOriginalPayPriceSerialNum());
				if (CollectionUtils.isNotEmpty(currencys)) {
					for (Currency currency : currencys) {
						originalCurrencyMarkMap.put(currency.getId().intValue(), currency.getCurrencyMark());
					}
				}
				traveler.put("originalCurrencyMarkMap", originalCurrencyMarkMap);
				if (CollectionUtils.isNotEmpty(originalMoneyList) && CollectionUtils.isNotEmpty(currencys)) {
					for (int i = 0; i < currencys.size(); i++) {
						originalCurrencyMarkMap.put(currencys.get(i).getId().intValue(), currencys.get(i).getCurrencyMark());
						originalMonyAmoutMap.put(currencys.get(i).getId().intValue(), originalMoneyList.get(i).getAmount().toString());
					}
				}
				traveler.put("originalMonyAmoutMap", originalMonyAmoutMap);
				// forbug 15474
				
				//游客结算价
				List<MoneyAmount> payMoneyList = moneyAmountService.findAmountBySerialNum(t.getPayPriceSerialNum());
				traveler.put("payMoneyList", payMoneyList);
				//备注
				traveler.put("remark", t.getRemark());
				//审批状态
				List<Costchange> costchangeList = costchangeService.findCostchangeBytravelerIdAndStatus(t.getId(), 1);
                traveler.put("costchangeList", costchangeList);
				if (costchangeList != null && costchangeList.size() > 0) {
					traveler.put("isReviewing", 1);
				} else {
					traveler.put("isReviewing", 0);
				}

				//添加到游客信息map中
				travelers.add(traveler);
			}
		}
		model.addAttribute("travelers", travelers);
	}
	
	/**
	 * @Description 改价申请
	 * @author yakun.bai
	 * @Date 2015-11-24
	 */
	@RequestMapping(value="applyChangePrice")
	@ResponseBody
	public Object applyChangePrice(HttpServletRequest request, @RequestParam("orderId") Long orderId, @RequestParam("productType") Integer productType) {
		
		//获取前台数据并封装成map数据
		String travelerArr = request.getParameter("travelerArr");

		//返回申请结果信息
		Map<String,String> resultMap = null;
		
		//游客改价申请
		try {
			resultMap = changePriceService.travelerForApply(travelerArr, orderId, productType);
		} catch (Exception e) {
			resultMap = Maps.newHashMap();
			resultMap.put("result", "error");
			resultMap.put("msg", e.getMessage());
		}
		return resultMap;
	}
	
	/**
	 * @Description 查询单个改价信息
	 * @author yakun.bai
	 * @Date 2015-12-2
	 */
	@RequestMapping(value="changePriceInfo")
	public String changePriceInfo(@RequestParam("reviewId") String reviewId, @RequestParam("orderId") Long orderId, 
			@RequestParam("productType") Integer productType, Model model, HttpServletRequest request) {
		
		//查询订单、团期、产品信息
		ProductOrderCommon productOrder = orderService.getProductorderById(orderId);
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		
		//查询改价申请信息
		Map<String, Object> reviewDetailMap = changePriceService.getReviewDetail(reviewId);
		Traveler traveller = travelerService.findTravelerById(Long.parseLong(reviewDetailMap.get("travellerId").toString()));

		//改前金额
		List<MoneyAmount> payMoneyList = moneyAmountService.findAmountBySerialNum(traveller.getPayPriceSerialNum());
		reviewDetailMap.put("payMoneyList", payMoneyList);

		//改价差额
		changePriceService.handlerMap(reviewDetailMap, productOrder);
		model.addAttribute("changePrice", reviewDetailMap);
		
		//值传递
		model.addAttribute("productOrder", productOrder);
		model.addAttribute("productGroup", productGroup);
		model.addAttribute("product", product);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productType.toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		model.addAttribute("orderId", orderId);
		
		return CHANGE_PRICE_INFO_PAGE;
	}
	
	/**
	 * @Description 取消改价申请
	 * @author yakun.bai
	 * @Date 2015-11-25
	 */
	@ResponseBody
	@RequestMapping("/cancelChangePrice")
	public Map<String, Object> cancelChangePrice(String reviewId) {
		String companyId = UserUtils.getUser().getCompany().getId().toString();
		String userId = UserUtils.getUser().getId().toString();
		Map<String, Object> result = Maps.newHashMap();

		//取消改价申请
		ReviewResult reviewResult = processReviewService.cancel(userId, companyId, "", reviewId, "", null);

		boolean flag = reviewResult.getSuccess();
		if (flag) {
			costchangeService.updateStatusByReviewUuid(reviewResult.getReviewStatus(), reviewId);
			result.put("result", "success");
			result.put("msg", reviewResult.getMessage());
		} else {
			result.put("result", "faild");
			result.put("msg", reviewResult.getMessage());
		}

		return result;
	}
	
}