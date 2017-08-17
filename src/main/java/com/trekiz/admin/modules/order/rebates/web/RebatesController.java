package com.trekiz.admin.modules.order.rebates.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.hotel.entity.ActivityHotel;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.hotel.entity.HotelTraveler;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelService;
import com.trekiz.admin.modules.hotel.service.HotelMoneyAmountService;
import com.trekiz.admin.modules.hotel.service.HotelOrderService;
import com.trekiz.admin.modules.hotel.service.HotelTravelerService;
import com.trekiz.admin.modules.island.entity.ActivityIsland;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.island.entity.HotelRebates;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.entity.IslandRebates;
import com.trekiz.admin.modules.island.entity.IslandTraveler;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupService;
import com.trekiz.admin.modules.island.service.ActivityIslandService;
import com.trekiz.admin.modules.island.service.IslandMoneyAmountService;
import com.trekiz.admin.modules.island.service.IslandOrderService;
import com.trekiz.admin.modules.island.service.IslandReviewService;
import com.trekiz.admin.modules.island.service.IslandTravelerService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.rebates.entity.Rebates;
import com.trekiz.admin.modules.order.rebates.service.RebatesService;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderReviewService;
import com.trekiz.admin.modules.order.service.ProductOrderService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;

@Controller
@RequestMapping(value = "${adminPath}/order/rebates")
public class RebatesController extends BaseController {
	
	public static final Integer REBATES_PROCESS_TYPE = 9;
	@Autowired
	private RebatesService rebatesService;
	@Autowired
    private OrderCommonService orderService;
	@Autowired
	@Qualifier("travelActivitySyncService")
    private ITravelActivityService travelActivityService;
	@Autowired
	@Qualifier("activityGroupSyncService")
    private IActivityGroupService activityGroupService;
	@Autowired
	private TravelerService travelerService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private OrderReviewService orderReviewService;
	@Autowired
	private IslandTravelerService islandTravelerService;
	@Autowired
	private IslandReviewService islandReviewService;
	@Autowired
	private IslandOrderService islandOrderService;
	@Autowired
	private IslandMoneyAmountService islandMoneyAmountService;
	@Autowired
	private HotelTravelerService hotelTravelerService;
	@Autowired
	private HotelOrderService hotelOrderService;
	@Autowired
	private HotelMoneyAmountService hotelMoneyAmountService;
	@Autowired
	private ActivityIslandService activityIslandService;
	@Autowired
	private ActivityIslandGroupService activityIslandGroupService;
	@Autowired
	private ActivityHotelService activityHotelService;
	@Autowired
	private ActivityHotelGroupService activityHotelGroupService;
	@Autowired
	private ProductOrderService productOrderService;
	/**
	 * 跳转到返佣列表
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="showRebatesList")
	public String showRebatesList(Model model, HttpServletRequest request){
		Long orderId = Long.parseLong(request.getParameter("orderId"));
		int orderType = Integer.parseInt(request.getParameter("orderType"));
		
		ProductOrderCommon product = productOrderService.getProductorderById(orderId);
		if(product!=null && StringUtils.isNotBlank(product.getScheduleBackUuid())){
			MoneyAmount mo = moneyAmountService.findOneAmountBySerialNum(product.getScheduleBackUuid());
			if(mo!=null){
				Currency currency = currencyService.findCurrency(Long.valueOf(mo.getCurrencyId()));
				if(currency!=null){
					model.addAttribute("currencyName",currency.getCurrencyName());
					model.addAttribute("currencyMark",currency.getCurrencyMark());
					model.addAttribute("amount",mo.getAmount());
				}
			}
		}
		model.addAttribute("orderId", orderId);
		model.addAttribute("orderType", orderType);
		List<Rebates> rebatesList = rebatesService.findRebatesList(orderId, orderType);
		model.addAttribute("rebatesList", rebatesList);
		return "modules/order/rebates/rebatesList";
	}
	
	/**
	 * 跳转到返佣详情界面
	 * @param orderId
	 * @param orderStatus
	 * @return
	 */
	@RequestMapping(value ="showRebatesDetail/{rebatesId}")
	public String showRebatesDetail(@PathVariable Long rebatesId, Model model, HttpServletRequest request){
		Rebates rebates = rebatesService.findRebatesById(rebatesId);
		ProductOrderCommon productOrder = orderService.getProductorderById(rebates.getOrderId());
		model.addAttribute("productOrder", productOrder);
		TravelActivity product = null;
		if(productOrder!=null && productOrder.getProductId()!=null){
			product = travelActivityService.findById(productOrder.getProductId());
		}
		model.addAttribute("product", product);
		ActivityGroup productGroup = null;
		if(productOrder!=null && productOrder.getProductGroupId()!=null){
			productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		}
		model.addAttribute("productGroup",productGroup);
		if(productOrder!=null && StringUtils.isNotBlank(productOrder.getOrderStatus().toString())){
			model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		}
		if(productOrder!=null && StringUtils.isNotBlank(productOrder.getPayMode())){
			model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		}
		model.addAttribute("rebates", rebates);
		
		if(productOrder!=null && StringUtils.isNotBlank(productOrder.getScheduleBackUuid())){
			MoneyAmount mo = moneyAmountService.findOneAmountBySerialNum(productOrder.getScheduleBackUuid());
			if(mo!=null){
				Currency currency = currencyService.findCurrency(Long.valueOf(mo.getCurrencyId()));
				if(currency!=null){
					model.addAttribute("currencyName",currency.getCurrencyName());
					model.addAttribute("currencyMark",currency.getCurrencyMark());
					model.addAttribute("amount",mo.getAmount());
				}
			}
		}
		List<ReviewLog> reviewLogList =  reviewService.findReviewLog(rebates.getReview().getId());
		model.addAttribute("reviewLogList", reviewLogList);
		return "modules/order/rebates/rebatesDetail";
	}
	
	/**
	 * 验证是否能再次返佣信息
	 * @param response
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="validRebates",method = RequestMethod.POST)
	public String validRebates(HttpServletResponse response, HttpServletRequest request){
		String orderId = request.getParameter("orderId");
		Integer orderType = Integer.parseInt(request.getParameter("orderType"));
		List<Rebates> rebatesList = rebatesService.findRebatesListByStatus(orderType, REBATES_PROCESS_TYPE, orderId);
		if(rebatesList != null && rebatesList.size() > 0){
			return "false";
		}
		return "true";
	}
	
	/**
	 * 跳转到返佣申请界面
	 * @param orderId
	 * @param orderStatus
	 * @return
	 */
	@RequestMapping(value ="addRebates/{orderId}/{orderStatus}")
	public String addRebates(@PathVariable Long orderId, @PathVariable int orderStatus, Model model, HttpServletRequest request){
		ProductOrderCommon productOrder = orderService.getProductorderById(orderId);
		model.addAttribute("productOrder", productOrder);
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		model.addAttribute("product", product);
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		List<MoneyAmount> totalMoneyList = moneyAmountService.findAmountBySerialNum(productOrder.getTotalMoney());
		// 定义订单应收币种集合
		List<Currency> currencyList = Lists.newArrayList();
		for(MoneyAmount moneyAmount: totalMoneyList){
			Currency currency = currencyService.findCurrency(moneyAmount.getCurrencyId().longValue());
			currencyList.add(currency);
		}
		model.addAttribute("currencyList", currencyList);
		// 获取团队原返佣金额
		List<Object[]> teamList = rebatesService.getTeamRebates(orderId);
		model.addAttribute("teamList", teamList);
		List<Rebates> travelerRebatesList = Lists.newArrayList();
		List<Traveler> travelerList = travelerService.findTravelerByOrderIdAndOrderType(orderId, orderStatus);
		Rebates rebates = null;
		Long currencyId = currencyList.get(0).getId();
		for(Traveler traveler: travelerList){
			List<Rebates> rebatesList = rebatesService.findRebatesByTravelerAndStatus(traveler.getId());
			Rebates travelerOldRebates =  null;
			if(rebatesList != null && rebatesList.size() > 0){
				travelerOldRebates = rebatesService.findRebatesByTravelerAndStatus(traveler.getId()).get(0);
			}
			String oldRebates = "";
			if(travelerOldRebates != null && StringUtils.isNotBlank(travelerOldRebates.getNewRebates())){
				oldRebates = travelerOldRebates.getNewRebates();
			}
			rebates =  new Rebates(orderId, traveler, currencyId, oldRebates);
			travelerRebatesList.add(rebates);
		}
		model.addAttribute("travelerRebatesList", travelerRebatesList);
		// 20150716 获取报名时的团队返佣参考金额
		List<Rebates> groupReList = new ArrayList<Rebates>();
		groupReList = rebatesService.getGroupReList(orderId);
		if(groupReList!=null && !groupReList.isEmpty()){
			model.addAttribute("groupReList",groupReList);
		}
		// 20150812 获取预订团队返佣
		if(productOrder!=null && StringUtils.isNotBlank(productOrder.getScheduleBackUuid())){
			MoneyAmount mo = moneyAmountService.findOneAmountBySerialNum(productOrder.getScheduleBackUuid());
			if(mo!=null){
				Currency currency = currencyService.findCurrency(Long.valueOf(mo.getCurrencyId()));
				if(currency!=null){
					model.addAttribute("currencyName",currency.getCurrencyName());
					model.addAttribute("currencyMark",currency.getCurrencyMark());
					model.addAttribute("amount",mo.getAmount());
				}
			}
		}
		return "modules/order/rebates/rebatesAdd";
	}
	
	/**
	 * 申请返佣
	 * @param response
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="saveRebates",method = RequestMethod.POST)
	public String saveRebates(HttpServletResponse response, HttpServletRequest request){
		String strmsg = "";
		try{
			JSONArray rebatesJSONArr = JSONArray.fromObject(request.getParameter("rebatesList"));
			List<Rebates> rebatesList = Lists.newArrayList();
			Rebates rebates = null;
			Traveler traveler;
			Map<Long,String> travelerMap = Maps.newHashMap();
			for(int i = 0; i < rebatesJSONArr.size(); i++){
				traveler = null;
				String totalMoney = "";
				JSONObject rebatesJSONObj = rebatesJSONArr.getJSONObject(i);
				String orderId =rebatesJSONObj.getString("orderId");
				Long currencyId = Long.parseLong(rebatesJSONObj.getString("currencyId"));
				BigDecimal rebatesDiff = new BigDecimal(rebatesJSONObj.getString("rebatesDiff"));
				String oldRebates = rebatesJSONObj.getString("oldRebates");
				String costname = rebatesJSONObj.getString("costname");
				String remark = rebatesJSONObj.getString("remark");
				Integer orderType = rebatesJSONObj.getInt("orderType");
				String newRebates = rebatesJSONObj.getString("newRebates");
				if(StringUtils.isNotBlank(rebatesJSONObj.getString("travelerId"))){
					traveler = travelerService.findTravelerById(Long.parseLong(rebatesJSONObj.getString("travelerId")));
					totalMoney = traveler.getPayPriceSerialNum();
					travelerMap.put(traveler.getId(), traveler.getName());
				}else{
					totalMoney = orderService.getProductorderById(Long.parseLong(orderId)).getTotalMoney();
				}
				rebates = new Rebates(Long.parseLong(orderId), traveler, currencyId, totalMoney, costname, oldRebates, rebatesDiff, newRebates, remark, orderType);
				rebatesList.add(rebates);
			}
			
			Map<String,Object> rMap = orderReviewService.getTravelerReviewMutexInfo(rebatesList.get(0).getOrderId(), rebatesList.get(0).getOrderType().toString(), Context.REBATES_FLOW_TYPE, travelerMap);
			if("1".equals(rMap.get(Context.MUTEX_CODE))){
				return rMap.get(Context.MESSAGE).toString();
			}
			strmsg = rebatesService.save(rebatesList);
		}catch(Exception e){
			e.printStackTrace();  
			return e.toString();
		}
		return strmsg;
	}
	
	@ResponseBody
	@RequestMapping(value="cancleRebates")
	public String cancleRebates(HttpServletResponse response, HttpServletRequest request){
		try{
			Long id = Long.parseLong(request.getParameter("rid"));
			Review r = reviewService.findReviewInfo(id);
			if (null != r && 1 == r.getStatus().intValue()) {
				reviewService.removeReview(id);
			}
			
		}catch(Exception e){
			e.printStackTrace();  
			return "error";
		}
		return "success";
	}
	/**
	 * 跳转到海岛游返佣列表
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="showIslandRebatesList")
	public String showIslandRebatesList(Model model, HttpServletRequest request){
		Long orderId = Long.parseLong(request.getParameter("orderId"));
		int orderType = Integer.parseInt(request.getParameter("orderType"));
		String orderUuid = request.getParameter("orderUuid");
		model.addAttribute("orderId", orderId);
		model.addAttribute("orderType", orderType);
		model.addAttribute("orderUuid", orderUuid);
		List<IslandRebates> rebatesList = islandReviewService.findRebatesList(orderId, orderType);
		if (CollectionUtils.isNotEmpty(rebatesList)) {
			Map<String, String> t4priceMap = Maps.newHashMap();//不同游客累计金额
			String totalMoney = "";
			for (IslandRebates rebate : rebatesList) {
				//获取累计返佣金额
				String currencyMark = currencyService.findCurrency(rebate.getCurrencyId()).getCurrencyMark();
				String currencyMoney = rebate.getRebatesDiff().toString();
				String travelerId = rebate.getTravelerId().toString();
				if (t4priceMap.containsKey(travelerId)) {
					totalMoney = t4priceMap.get(travelerId);
				} else {
					totalMoney = "";
				}
				totalMoney = islandMoneyAmountService.addOrSubtract(currencyMark + " " + currencyMoney, totalMoney, true);
				t4priceMap.put(travelerId, totalMoney);
				islandOrderService.clearObject(rebate);
				rebate.setOldRebates(totalMoney);
			}
		}
		//对结果集进行翻转（前台按创建时间倒叙排列）
		Collections.reverse(rebatesList);
		model.addAttribute("rebatesList", rebatesList);
		return "modules/order/rebates/islandRebatesList";
	}
	/**
	 * 跳转到酒店返佣列表
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="showHotelRebatesList")
	public String showHotelRebatesList(Model model, HttpServletRequest request){
		Long orderId = Long.parseLong(request.getParameter("orderId"));
		int orderType = Integer.parseInt(request.getParameter("orderType"));
		String orderUuid = request.getParameter("orderUuid");
		model.addAttribute("orderId", orderId);
		model.addAttribute("orderType", orderType);
		model.addAttribute("orderUuid", orderUuid);
		List<HotelRebates> rebatesList = islandReviewService.findHotelRebatesList(orderId, orderType);
		if (CollectionUtils.isNotEmpty(rebatesList)) {
			Map<String, String> t4priceMap = Maps.newHashMap();//不同游客累计金额
			String totalMoney = "";
			for (HotelRebates rebate : rebatesList) {
				//获取累计返佣金额
				String currencyMark = currencyService.findCurrency(rebate.getCurrencyId()).getCurrencyMark();
				String currencyMoney = rebate.getRebatesDiff().toString();
				String travelerId = rebate.getTravelerId().toString();
				if (t4priceMap.containsKey(travelerId)) {
					totalMoney = t4priceMap.get(travelerId);
				} else {
					totalMoney = "";
				}
				totalMoney = islandMoneyAmountService.addOrSubtract(currencyMark + " " + currencyMoney, totalMoney, true);
				t4priceMap.put(travelerId, totalMoney);
				hotelOrderService.clearObject(rebate);
				rebate.setOldRebates(totalMoney);
			}
		}
		//对结果集进行翻转（前台按创建时间倒叙排列）
		Collections.reverse(rebatesList);
		model.addAttribute("rebatesList", rebatesList);
		return "modules/order/rebates/hotelRebatesList";
	}
	
	/**
	 * 跳转到海岛游返佣详情界面
	 * @param orderId
	 * @param orderStatus
	 * @return
	 */
	@RequestMapping(value ="showIslandRebatesDetail/{rebatesId}/{orderUuid}")
	public String showIslandRebatesDetail(@PathVariable Long rebatesId,@PathVariable String orderUuid, Model model, HttpServletRequest request){
		IslandRebates rebates = rebatesService.findIslandRebatesById(rebatesId);
		model.addAttribute("rebates", rebates);
		model.addAttribute("travelerInfo", islandTravelerService.getById(rebates.getTravelerId().intValue()));
		List<ReviewLog> reviewLogList =  reviewService.findReviewLog(rebates.getReview().getId());
		model.addAttribute("reviewLogList", reviewLogList);
		//订单基本信息
		islandOrderService.getOrderBaseInfo(orderUuid, model);
		return "modules/order/rebates/islandRebatesDetail";
	}
	/**
	 * 跳转到酒店返佣详情界面
	 * @param orderId
	 * @param orderStatus
	 * @return
	 */
	@RequestMapping(value ="showHotelRebatesDetail/{rebatesId}/{orderUuid}")
	public String showHotelRebatesDetail(@PathVariable Long rebatesId,@PathVariable String orderUuid, Model model, HttpServletRequest request){
		IslandRebates rebates = rebatesService.findIslandRebatesById(rebatesId);
		model.addAttribute("rebates", rebates);
		model.addAttribute("travelerInfo", hotelTravelerService.getById(rebates.getTravelerId().intValue()));
		List<ReviewLog> reviewLogList =  reviewService.findReviewLog(rebates.getReview().getId());
		model.addAttribute("reviewLogList", reviewLogList);
		
		//订单基本信息
		hotelOrderService.getOrderBaseInfo(orderUuid, model);
		return "modules/order/rebates/hotelRebatesDetail";
	}
	/**
	 * 跳转到海岛游返佣申请界面
	 * @param orderId
	 * @param orderStatus
	 * @return
	 */
	@RequestMapping(value ="addIslandRebates/{orderId}/{orderStatus}/{orderUuid}")
	public String addIslandRebates(@PathVariable Long orderId, @PathVariable int orderStatus, @PathVariable String orderUuid,Model model, HttpServletRequest request){
		
		//海岛游产品、订单信息
		IslandOrder islandOrder = islandOrderService.getById(orderId.intValue());		
		ActivityIsland product = activityIslandService.getByUuid(islandOrder.getActivityIslandUuid());
		ActivityIslandGroup productGroup = activityIslandGroupService.getByUuid(islandOrder.getActivityIslandGroupUuid());
		
		model.addAttribute("productOrder", islandOrder);
		model.addAttribute("product", product);
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(islandOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(islandOrder.getPayMode()));
//		List<IslandMoneyAmount> totalMoneyList = islandMoneyAmountService.getMoneyAmonutBySerialNum(islandOrder.getTotalMoney());
		// 定义订单应收币种集合
		List<Currency> currencyList = currencyService.findSortCurrencyList(UserUtils.getUser().getCompany().getId());
		model.addAttribute("currencyList", currencyList);
		// 获取团队原返佣金额
		List<Object[]> teamList = rebatesService.getIslandTeamRebates(orderId);
		model.addAttribute("teamList", teamList);
		List<IslandRebates> travelerRebatesList = Lists.newArrayList();
		List<IslandTraveler> travelerList = islandTravelerService.findTravelerByOrderUuid(islandOrder.getUuid());
		IslandRebates rebates = null;
		Long currencyId = currencyList.get(0).getId();
		List<String> travelUuidList = Lists.newArrayList();
		List<String> travelPriceList = Lists.newArrayList();
		for(IslandTraveler traveler: travelerList){
			travelUuidList.add(traveler.getUuid());
			travelPriceList.add(traveler.getPayPriceSerialNum());
			List<IslandRebates> rebatesList = islandReviewService.findRebatesByTravelerAndStatus(traveler.getId().longValue());
			IslandRebates travelerOldRebates =  null;
			if(rebatesList != null && rebatesList.size() > 0){
				travelerOldRebates = rebatesList.get(0);
			}
			String oldRebates = "";
			if(travelerOldRebates != null && StringUtils.isNotBlank(travelerOldRebates.getNewRebates())){
				oldRebates = travelerOldRebates.getNewRebates();
			}
			rebates =  new IslandRebates(orderId, traveler, currencyId, oldRebates);
			travelerRebatesList.add(rebates);
		}
		model.addAttribute("travelerRebatesList", travelerRebatesList);
		
		List<IslandRebates> rebatesList = islandReviewService.findRebatesList(orderId, 12);
		if (CollectionUtils.isNotEmpty(rebatesList)) {
			Map<String, String> t4priceMap = Maps.newHashMap();//不同游客累计金额
			String totalMoney = "";
			for (IslandRebates rebate : rebatesList) {
				IslandTraveler traveler = rebate.getTraveler();
				if (traveler != null) {
					Dict dict = DictUtils.getDict(traveler.getSpaceLevel(), "spaceGrade_Type");
					if (dict != null) {
						islandOrderService.clearObject(traveler);
						traveler.setSpaceLevel(dict.getLabel());
					}
				}
				//获取累计返佣金额
				String currencyMark = currencyService.findCurrency(rebate.getCurrencyId()).getCurrencyMark();
				String currencyMoney = rebate.getRebatesDiff().toString();
				String travelerId = rebate.getTravelerId().toString();
				if (t4priceMap.containsKey(travelerId)) {
					totalMoney = t4priceMap.get(travelerId);
				} else {
					totalMoney = "";
				}
				totalMoney = islandMoneyAmountService.addOrSubtract(currencyMark + " " + currencyMoney, totalMoney, true);
				t4priceMap.put(travelerId, totalMoney);
				islandOrderService.clearObject(rebate);
				rebate.setOldRebates(totalMoney);
			}
		}
		
		//对结果集进行翻转（前台按创建时间倒叙排列）
		Collections.reverse(rebatesList);
		model.addAttribute("rebatesList", rebatesList);
		
		String totalRebatesMoney = OrderCommonUtil.getTravelsRebatesByOrderType(travelUuidList, 12+"");
		model.addAttribute("totalRebatesMoney",totalRebatesMoney);
		String totalTravelerPrice = OrderCommonUtil.getIslandMoneyAmountBySerialNums(travelPriceList,2);
		model.addAttribute("totalTravelerPrice",totalTravelerPrice);
		//订单基本信息
		islandOrderService.getOrderBaseInfo(orderUuid, model);
		return "modules/order/rebates/islandRebatesAdd";
	}
	/**
	 * 跳转到返佣申请界面
	 * @param orderId
	 * @param orderStatus
	 * @return
	 */
	@RequestMapping(value ="addHotelRebates/{orderId}/{orderStatus}/{orderUuid}")
	public String addHotelRebates(@PathVariable Long orderId, @PathVariable int orderStatus,@PathVariable String orderUuid, Model model, HttpServletRequest request){
		
		//海岛游产品、订单信息
		HotelOrder hotelOrder = hotelOrderService.getById(orderId.intValue());		
		ActivityHotel product = activityHotelService.getByUuid(hotelOrder.getActivityHotelUuid());
		ActivityHotelGroup productGroup = activityHotelGroupService.getByUuid(hotelOrder.getActivityHotelGroupUuid());
		model.addAttribute("productOrder", hotelOrder); 
		model.addAttribute("product", product);
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(hotelOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(hotelOrder.getPayMode()));
		// 定义订单应收币种集合
		List<Currency> currencyList = currencyService.findSortCurrencyList(UserUtils.getUser().getCompany().getId());
		model.addAttribute("currencyList", currencyList);
		// 获取团队原返佣金额
		List<Object[]> teamList = rebatesService.getIslandTeamRebates(orderId);
		model.addAttribute("teamList", teamList);
		List<HotelRebates> travelerRebatesList = Lists.newArrayList();
		List<HotelTraveler> travelerList = hotelTravelerService.findTravelerByOrderUuid(hotelOrder.getUuid(), false);
		HotelRebates rebates = null;
		Long currencyId = currencyList.get(0).getId();
		
		List<String> travelUuidList = Lists.newArrayList();
		List<String> travelPriceList = Lists.newArrayList();
		for(HotelTraveler traveler: travelerList){
			travelUuidList.add(traveler.getUuid());
			travelPriceList.add(traveler.getPayPriceSerialNum());
			List<HotelRebates> rebatesList = islandReviewService.findHotelRebatesByTravelerAndStatus(traveler.getId().longValue());
			HotelRebates travelerOldRebates =  null;
			if(rebatesList != null && rebatesList.size() > 0){
				travelerOldRebates = rebatesList.get(0);
			}
			String oldRebates = "";
			if(travelerOldRebates != null && StringUtils.isNotBlank(travelerOldRebates.getNewRebates())){
				oldRebates = travelerOldRebates.getNewRebates();
			}
			rebates =  new HotelRebates(orderId, traveler, currencyId, oldRebates);
			travelerRebatesList.add(rebates);
		}
		model.addAttribute("travelerRebatesList", travelerRebatesList);
		
		List<HotelRebates> rebatesList = islandReviewService.findHotelRebatesList(orderId, Context.ORDER_TYPE_HOTEL);
		if (CollectionUtils.isNotEmpty(rebatesList)) {
			Map<String, String> t4priceMap = Maps.newHashMap();//不同游客累计金额
			String totalMoney = "";
			for (HotelRebates rebate : rebatesList) {
				//获取累计返佣金额
				String currencyMark = currencyService.findCurrency(rebate.getCurrencyId()).getCurrencyMark();
				String currencyMoney = rebate.getRebatesDiff().toString();
				String travelerId = rebate.getTravelerId().toString();
				if (t4priceMap.containsKey(travelerId)) {
					totalMoney = t4priceMap.get(travelerId);
				} else {
					totalMoney = "";
				}
				totalMoney = islandMoneyAmountService.addOrSubtract(currencyMark + " " + currencyMoney, totalMoney, true);
				t4priceMap.put(travelerId, totalMoney);
				hotelOrderService.clearObject(rebate);
				rebate.setOldRebates(totalMoney);
			}
		}
		
		//对结果集进行翻转（前台按创建时间倒叙排列）
		Collections.reverse(rebatesList);
		model.addAttribute("rebatesList", rebatesList);
		
		String totalRebatesMoney = OrderCommonUtil.getTravelsRebatesByOrderType(travelUuidList, 11+"");
		model.addAttribute("totalRebatesMoney",totalRebatesMoney);
		String totalTravelerPrice = OrderCommonUtil.getHotelMoneyAmountBySerialNums(travelPriceList,2);
		model.addAttribute("totalTravelerPrice",totalTravelerPrice);
		//订单基本信息
		hotelOrderService.getOrderBaseInfo(orderUuid, model);
		return "modules/order/rebates/hotelRebatesAdd";
	}
	/**
	 * 海岛游申请返佣
	 * @param response
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="saveIslandRebates",method = RequestMethod.POST)
	public String saveIslandRebates(HttpServletResponse response, HttpServletRequest request){
		String strmsg = "";
		try{
			JSONArray rebatesJSONArr = JSONArray.fromObject(request.getParameter("rebatesList"));
			List<IslandRebates> rebatesList = Lists.newArrayList();
			IslandRebates rebates = null;
			IslandTraveler traveler;
			Map<Long,String> travelerMap = Maps.newHashMap();
			for(int i = 0; i < rebatesJSONArr.size(); i++){
				traveler = null;
				String totalMoney = "";
				JSONObject rebatesJSONObj = rebatesJSONArr.getJSONObject(i);
				String orderId =rebatesJSONObj.getString("orderId");
				Long currencyId = Long.parseLong(rebatesJSONObj.getString("currencyId"));
				BigDecimal rebatesDiff = new BigDecimal(rebatesJSONObj.getString("rebatesDiff"));
				String oldRebates = rebatesJSONObj.getString("oldRebates");
				String costname = rebatesJSONObj.getString("costname");
				String remark = rebatesJSONObj.getString("remark");
				Integer orderType = 12;
				String newRebates = rebatesJSONObj.getString("newRebates");
				if(StringUtils.isNotBlank(rebatesJSONObj.getString("uuid"))){
					traveler = islandTravelerService.getByUuid(rebatesJSONObj.getString("uuid"));
					totalMoney = traveler.getPayPriceSerialNum();
					//兼容之前游客互斥类操作判断
					travelerMap.put(traveler.getId().longValue(), traveler.getName());
				}else{
					totalMoney = islandOrderService.getById(Integer.parseInt(orderId)).getTotalMoney();
				}
				rebates = new IslandRebates(Long.parseLong(orderId), traveler, currencyId, totalMoney, costname, oldRebates, rebatesDiff, newRebates, remark, orderType);
				rebatesList.add(rebates);
			}
			
//			Map<String,Object> rMap = orderReviewService.getTravelerReviewMutexInfo(rebatesList.get(0).getOrderId(), rebatesList.get(0).getOrderType().toString(), Context.REBATES_FLOW_TYPE, travelerMap);
//			if("1".equals(rMap.get(Context.MUTEX_CODE))){
//				return rMap.get(Context.MESSAGE).toString();
//			}
			strmsg = rebatesService.saveIslandRebates(rebatesList);
		}catch(Exception e){
			e.printStackTrace();  
			return e.toString();
		}
		return strmsg;
	}
	/**
	 * 酒店申请返佣
	 * @param response
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="saveHotelRebates",method = RequestMethod.POST)
	public String saveHotelRebates(HttpServletResponse response, HttpServletRequest request){
		String strmsg = "";
		try{
			JSONArray rebatesJSONArr = JSONArray.fromObject(request.getParameter("rebatesList"));
			List<HotelRebates> rebatesList = Lists.newArrayList();
			HotelRebates rebates = null;
			HotelTraveler traveler;
			Map<Long,String> travelerMap = Maps.newHashMap();
			for(int i = 0; i < rebatesJSONArr.size(); i++){
				traveler = null;
				String totalMoney = "";
				JSONObject rebatesJSONObj = rebatesJSONArr.getJSONObject(i);
				String orderId =rebatesJSONObj.getString("orderId");
				Long currencyId = Long.parseLong(rebatesJSONObj.getString("currencyId"));
				BigDecimal rebatesDiff = new BigDecimal(rebatesJSONObj.getString("rebatesDiff"));
				String oldRebates = rebatesJSONObj.getString("oldRebates");
				String costname = rebatesJSONObj.getString("costname");
				String remark = rebatesJSONObj.getString("remark");
				Integer orderType = rebatesJSONObj.getInt("orderType");
				String newRebates = rebatesJSONObj.getString("newRebates");
				if(StringUtils.isNotBlank(rebatesJSONObj.getString("uuid"))){
					traveler = hotelTravelerService.getByUuid(rebatesJSONObj.getString("uuid"));
					totalMoney = traveler.getPayPriceSerialNum();
					//兼容之前游客互斥类操作判断
					travelerMap.put(traveler.getId().longValue(), traveler.getName());
				}else{
					totalMoney = hotelOrderService.getById(Integer.parseInt(orderId)).getTotalMoney();
				}
				rebates = new HotelRebates(Long.parseLong(orderId), traveler, currencyId, totalMoney, costname, oldRebates, rebatesDiff, newRebates, remark, orderType);
				rebatesList.add(rebates);
			}
			
//			Map<String,Object> rMap = orderReviewService.getTravelerReviewMutexInfo(rebatesList.get(0).getOrderId(), rebatesList.get(0).getOrderType().toString(), Context.REBATES_FLOW_TYPE, travelerMap);
//			if("1".equals(rMap.get(Context.MUTEX_CODE))){
//				return rMap.get(Context.MESSAGE).toString();
//			}
			strmsg = rebatesService.saveHotelRebates(rebatesList);
		}catch(Exception e){
			e.printStackTrace();  
			return e.toString();
		}
		return strmsg;
	}
}
