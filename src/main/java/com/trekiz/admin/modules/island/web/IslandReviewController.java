package com.trekiz.admin.modules.island.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.service.IActivityUpPricesService;
import com.trekiz.admin.modules.hotel.entity.ActivityHotel;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.hotel.entity.HotelTraveler;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupRoomService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelGroupService;
import com.trekiz.admin.modules.hotel.service.ActivityHotelService;
import com.trekiz.admin.modules.hotel.service.HotelOrderService;
import com.trekiz.admin.modules.hotel.service.HotelTravelerService;
import com.trekiz.admin.modules.island.entity.ActivityIsland;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.entity.IslandTraveler;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupService;
import com.trekiz.admin.modules.island.service.ActivityIslandService;
import com.trekiz.admin.modules.island.service.IslandMoneyAmountService;
import com.trekiz.admin.modules.island.service.IslandOrderService;
import com.trekiz.admin.modules.island.service.IslandReviewService;
import com.trekiz.admin.modules.island.service.IslandTravelerService;
import com.trekiz.admin.modules.order.formBean.BorrowingBean;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewDetail;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
/**
 * @author  ruyi.chen	
 * @version 1.0
 * add date 2015-06-09
 * describe 海岛游业务审核相关申请、审核内容
 */

@Controller
@RequestMapping(value = "${adminPath}/island/review")
public class IslandReviewController extends BaseController{

	@Autowired
	private IslandOrderService islandOrderService;
	@Autowired
	private ActivityIslandGroupService activityIslandGroupService;
	@Autowired
	private ActivityIslandService activityIslandService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private IslandReviewService islandReviewService;
	@Autowired
	private HotelOrderService hotelOrderService;
	@Autowired
	private ActivityHotelService activityHotelService;
	@Autowired
	private ActivityHotelGroupService activityHotelGroupService;
	@Autowired
	private IslandTravelerService islandTravelerService;
	@Autowired
	private HotelTravelerService hotelTravelerService;
	@Autowired
	private ActivityHotelGroupRoomService activityHotelGroupRoomService;
	@Autowired
	private IActivityUpPricesService activityUpPricesService;
	@Autowired
	private IslandMoneyAmountService islandMoneyAmountService;
	
	/**
	 * 查看借款列表
	 * @author  chenry
	 * add Date 2015-06-14
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@RequestMapping(value = "viewBorrowingList")
	public String viewBorrowingList(Model model,HttpServletRequest request,HttpServletResponse response,Long orderId,String orderUuid,Integer productType,Integer flowType) throws IllegalArgumentException, IllegalAccessException{
		IslandOrder islandOrder = islandOrderService.getByUuid(orderUuid);
		ActivityIsland activityIsland = activityIslandService.getByUuid(islandOrder.getActivityIslandUuid());
		
		List<Map<String, String>> reviewMapList = reviewService.findReviewListMap(productType,flowType, islandOrder.getId()+"", false,activityIsland.getDeptId().longValue());
		
		List<BorrowingBean> reviewList = getBorrowingBeanList(reviewMapList);
		for(BorrowingBean borr :reviewList){
			if(borr.getTravelerId().contains(BorrowingBean.REGEX)|| "0".equals(borr.getTravelerId())){
				borr.setTravelerName("团队");
			}
			if(StringUtils.isNotBlank(borr.getCurrencyIds())&&borr.getCurrencyIds().contains(BorrowingBean.REGEX)){
				String compPrice = "";
				if(StringUtils.isNotBlank(borr.getCurrencyMarks())&&StringUtils.isNotBlank(borr.getBorrowPrices())){
					String[] cMarks = borr.getCurrencyMarks().split(BorrowingBean.REGEX);	
					String[] cPrices = borr.getBorrowPrices().split(BorrowingBean.REGEX);
					for(int i=0;i<cMarks.length;i++){
						compPrice+=cMarks[i]+cPrices[i]+"+";
					}
					borr.setCurrencyIds(compPrice.substring(0, compPrice.length()-1));
				}
				
			}
			else{
			    borr.setCurrencyIds(borr.getCurrencyMarks()+borr.getBorrowPrices());
			}
		}
		List<Map <String,Object>> rMap = Lists.newArrayList();
		
		List<IslandTraveler> travelerList = islandTravelerService.findTravelerByOrderUuid(orderUuid);
		
		Map<String, String> t4priceMap = Maps.newHashMap();//不同游客累计金额
		String sumLendPrice = ""; //累计借款金额
		for(BorrowingBean borr :reviewList){
			for(IslandTraveler t : travelerList){
				if(null != borr.getTravelerId() && t.getId().toString().equals(borr.getTravelerId())){
					Map <String,Object> map = Maps.newHashMap();
					map.put("currencyName", borr.getCurrencyName());
					map.put("currencyMark", borr.getCurrencyMark());
					map.put("lendPrice", borr.getLendPrice());
					map.put("lendName", borr.getLendName());
					map.put("reviewId", borr.getReviewId());
					map.put("applyDate", borr.getApplyDate());
					map.put("remark", borr.getRemark());
					map.put("createBy", borr.getCreateBy());
					
					map.put("totalMoney", t.getPayPriceSerialNum());
					map.put("personType", t.getPersonType());
					map.put("travelerName", t.getName());
					map.put("travelerId", t.getId());
					map.put("spaceLevel", t.getSpaceLevel());
					map.put("travelerUuid", t.getUuid());
					
					//获取累计借款金额
					String travelerId = borr.getTravelerId();
					if (t4priceMap.containsKey(travelerId)) {
						sumLendPrice = t4priceMap.get(travelerId);
					} else {
						sumLendPrice = "";
					}
					sumLendPrice = islandMoneyAmountService.addOrSubtract(borr.getCurrencyMark() + " " + borr.getLendPrice(), sumLendPrice, true);
					t4priceMap.put(borr.getTravelerId(), sumLendPrice);
					map.put("sumLendPrice", sumLendPrice);
					rMap.add(map);
				}
			}
		}
		//对结果集进行翻转（前台按创建时间倒叙排列）
		Collections.reverse(rMap);
		model.addAttribute("bAList", rMap);
		model.addAttribute("orderUuid", orderUuid);
		model.addAttribute("orderId", orderId);
		model.addAttribute("productType", productType);
		return "modules/island/review/viewBorrowing";
	}
	/**
	 * 借款详情展示
	 * @param request
	 * @param response
	 * @param modasdel
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value="viewBorrowingInfo")
	public String viewBorrowingInfo(HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException {
		
		String orderUuid = request.getParameter("orderUuid");
		String orderId = request.getParameter("orderId");
		String reviewId = request.getParameter("reviewId");
		
		//加载订单基本信息
		islandOrderService.getOrderBaseInfo(orderUuid, model);
		
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		
		List<ReviewDetail> rdlist = new ArrayList<ReviewDetail>();
		try{
			if(reviewId!=null){
				rdlist= reviewService.queryReviewDetailList(reviewId);
			}
		}catch(Exception e){
			logger.error("根据reviewid： " + reviewId + " 查询出来reviewDetail明细报错 ",e);
		}
		//将总金额和币种显示出来
		List<ReviewDetail> borrowPricesList = new ArrayList<ReviewDetail>();
		if(rdlist!=null&&rdlist.size()>0){
			for(int i = 0;i<rdlist.size();i++){
				if("borrowPrices".equals(rdlist.get(i).getMykey())){
					borrowPricesList.add(rdlist.get(i));
				}
				if("currencyMarks".equals(rdlist.get(i).getMykey())){
					borrowPricesList.add(rdlist.get(i));
				}
			}
		}
		
		List<BorrowingBean> blist = BorrowingBean.transferReviewDetail2BorrowingBean(rdlist);
		List<BorrowingBean> tralist = new ArrayList<BorrowingBean>();
		List<BorrowingBean> teamlist= new ArrayList<BorrowingBean>();
		List<BorrowingBean> borrowList = BorrowingBean.transferReviewDetail2BorrowingBean(borrowPricesList);
		//将blist 拆分为团队借款和游客借款两部分
		if(blist!=null&&blist.size()>0){
			int size = blist.size();
			model.addAttribute("applyDate", blist.get(0).getApplyDate());
			for(int i = 0;i<size;i++){
				BorrowingBean bean = blist.get(i);
				if("0".equals(bean.getTravelerId())){//团队借款
					teamlist.add(bean);
				}else{//游客借款
					tralist.add(bean);
				}
			}
			
		}
		
		if(reviewId!=null&&!"".equals(reviewId)){//显示动态审核的标志
	    	List<ReviewLog> rLog=reviewService.findReviewLog(Long.parseLong(reviewId));
			model.addAttribute("rLog",rLog);
			Review review = reviewService.findReviewInfo(Long.parseLong(reviewId));
			model.addAttribute("review",review);
	    }
		IslandTraveler t = islandTravelerService.getById(Integer.parseInt(tralist.get(0).getTravelerId()));
		model.addAttribute("traveler",t);
		model.addAttribute("tralist", tralist);
		model.addAttribute("teamlist",teamlist);
		model.addAttribute("borrowList", borrowList);
		model.addAttribute("totalsize", borrowList.size());
		model.addAttribute("reviewId", reviewId);
		List<Currency> currencyList = currencyService.findCurrencyList(companyId);
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("orderUuid", orderUuid);
		model.addAttribute("orderId", orderId);
		IslandOrder islandOrder = islandOrderService.getByUuid(orderUuid);
//		ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
//		model.addAttribute("productOrder", productOrder);
//		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
//		model.addAttribute("product", product);
//		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
//		model.addAttribute("productGroup",productGroup);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(islandOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(islandOrder.getPayMode()));
		model.addAttribute("productType", Context.ORDER_TYPE_ISLAND);
		islandOrderService.getOrderBaseInfo(orderUuid, model);
		return "modules/island/review/viewBorrowingInfo";
	}
	/**
	 * 订单借款申请
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@RequestMapping(value="applyBorrowing")
	public String applyBorrowing(HttpServletRequest request,String productType, HttpServletResponse response,Model model) throws ParseException, IllegalArgumentException, IllegalAccessException {
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		String orderUuid= request.getParameter("orderUuid");
		String orderId= request.getParameter("orderId");
		IslandOrder islandOrder = islandOrderService.getByUuid(orderUuid);
		model.addAttribute("productOrder", islandOrder);
		ActivityIsland activityIsland = activityIslandService.getByUuid(islandOrder.getActivityIslandUuid());
		model.addAttribute("product", activityIsland);
		ActivityIslandGroup productGroup = activityIslandGroupService.getByUuid(islandOrder.getActivityIslandGroupUuid());
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(islandOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(islandOrder.getPayMode()));
		List<Currency> currencyList = currencyService.findSortCurrencyList(companyId);
		model.addAttribute("orderUuid", orderUuid);
		model.addAttribute("orderId", orderId);
		model.addAttribute("currencyList", currencyList);
		
		
		List<IslandTraveler> travelerList=islandTravelerService.findTravelerByOrderUuid(orderUuid);
		
		//添加海岛游借款申请相关信息
		List<Map<String, String>> reviewMapList = reviewService.findReviewListMap(12,Context.REVIEW_FLOWTYPE_BORROWMONEY, islandOrder.getId()+"", false,activityIsland.getDeptId().longValue());		
		List<BorrowingBean> reviewList = getBorrowingBeanList(reviewMapList);
		List<Map <String,Object>> rMap = Lists.newArrayList();
		Map<String, String> t4priceMap = Maps.newHashMap();//不同游客累计金额
		String sumLendPrice = ""; //累计借款金额
		for(BorrowingBean borr :reviewList){
			Map <String,Object> map = Maps.newHashMap();
			map.put("currencyName", borr.getCurrencyName());
			map.put("currencyMark", borr.getCurrencyMark());
			map.put("lendPrice", borr.getLendPrice());
			map.put("lendName", borr.getLendName());
			map.put("reviewId", borr.getReviewId());
			map.put("applyDate", borr.getApplyDate());
			map.put("remark", borr.getRemark());
			//获取累计借款金额
			String travelerId = borr.getTravelerId();
			if (t4priceMap.containsKey(travelerId)) {
				sumLendPrice = t4priceMap.get(travelerId);
			} else {
				sumLendPrice = "";
			}
			sumLendPrice = islandMoneyAmountService.addOrSubtract(borr.getCurrencyMark() + " " + borr.getLendPrice(), sumLendPrice, true);
			t4priceMap.put(borr.getTravelerId(), sumLendPrice);
			map.put("sumLendPrice", sumLendPrice);
			for(IslandTraveler t : travelerList){
				if(null != borr.getTravelerId() && t.getId().toString().equals(borr.getTravelerId())){
					map.put("totalMoney", t.getPayPriceSerialNum());
					map.put("personType", t.getPersonType());
					map.put("travelerName", t.getName());
					map.put("travelerId", t.getId());
					map.put("travelerUuid", t.getUuid());
					map.put("spaceLevel", t.getSpaceLevel());
				}
				
			}
			rMap.add(map);
		}		
		
		List<String> travelUuidList = Lists.newArrayList();
		List<String> travelPriceList = Lists.newArrayList();
		for(IslandTraveler t: travelerList){
			travelUuidList.add(t.getUuid());
			travelPriceList.add(t.getPayPriceSerialNum());
		}
		String allBorrowingMoney = OrderCommonUtil.getBorrowPayMoneyTravelByOrderType(orderUuid, travelUuidList, 12+"");
		model.addAttribute("totalBorrowingMoney",allBorrowingMoney);
		String totalTravelerPrice = OrderCommonUtil.getIslandMoneyAmountBySerialNums(travelPriceList,2);
		model.addAttribute("totalTravelerPrice",totalTravelerPrice);
		//订单基本信息
		islandOrderService.getOrderBaseInfo(orderUuid, model);
		model.addAttribute("currencyList", currencyList);
		//对结果集进行翻转（前台按创建时间倒叙排列）
		Collections.reverse(rMap);
		model.addAttribute("bAList", rMap);
		model.addAttribute("flowType", Context.REVIEW_FLOWTYPE_BORROWMONEY);
		model.addAttribute("productType",productType);
		model.addAttribute("travelerList", travelerList);
		return "modules/island/review/applyBorrowing";
	}
	/**
	 * 借款流程申请
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@Deprecated
	@ResponseBody
	@RequestMapping(value="saveBorrowing")
	public String saveBorrowing(HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException {
		
		return islandReviewService.saveBorrowing(request);
	}
	/**
	 * 组装BorrowingBean对象
	 */
	private List<BorrowingBean> getBorrowingBeanList(List<Map<String, String>> reviewMapList) {
		List<BorrowingBean> aList = Lists.newArrayList();
		if (CollectionUtils.isEmpty(reviewMapList)) {
			return aList;
		}
		for (Map<String, String> map : reviewMapList) {
			aList.add(new BorrowingBean(map));
		}
		return aList;
	}
///////////////////////////////////海岛游借款申请、审核部分结束//////////////////////////////////////////////////
	/**
	 * 查看借款列表
	 * @author  chenry
	 * add Date 2015-06-14
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@RequestMapping(value = "viewHotelBorrowingList")
	public String viewHotelBorrowingList(Model model,HttpServletRequest request,HttpServletResponse response,Long orderId,String orderUuid,Integer productType,Integer flowType) throws IllegalArgumentException, IllegalAccessException{
		HotelOrder hotelOrder = hotelOrderService.getByUuid(orderUuid);
		ActivityHotel activityHotel = activityHotelService.getByUuid(hotelOrder.getActivityHotelUuid());
		
		List<Map<String, String>> reviewMapList = reviewService.findReviewListMap(productType,flowType, hotelOrder.getId()+"", false,activityHotel.getDeptId().longValue());
		
		List<BorrowingBean> reviewList = getBorrowingBeanList(reviewMapList);
		for(BorrowingBean borr :reviewList){
			if(borr.getTravelerId().contains(BorrowingBean.REGEX)|| "0".equals(borr.getTravelerId())){
				borr.setTravelerName("团队");
			}
			if(StringUtils.isNotBlank(borr.getCurrencyIds())&&borr.getCurrencyIds().contains(BorrowingBean.REGEX)){
				String compPrice = "";
				if(StringUtils.isNotBlank(borr.getCurrencyMarks())&&StringUtils.isNotBlank(borr.getBorrowPrices())){
					String[] cMarks = borr.getCurrencyMarks().split(BorrowingBean.REGEX);	
					String[] cPrices = borr.getBorrowPrices().split(BorrowingBean.REGEX);
					for(int i=0;i<cMarks.length;i++){
						compPrice+=cMarks[i]+cPrices[i]+"+";
					}
					borr.setCurrencyIds(compPrice.substring(0, compPrice.length()-1));
				}
				
			}
			else{
			    borr.setCurrencyIds(borr.getCurrencyMarks()+borr.getBorrowPrices());
			}
		}
		List<Map <String,Object>> rMap = Lists.newArrayList();
		
		List<HotelTraveler> travelerList = hotelTravelerService.findTravelerByOrderUuid(orderUuid, false);
		
		Map<String, String> t4priceMap = Maps.newHashMap();//不同游客累计金额
		String sumLendPrice = ""; //累计借款金额
		for (BorrowingBean borr : reviewList) {
			Map <String,Object> map = Maps.newHashMap();
			map.put("currencyName", borr.getCurrencyName());
			map.put("currencyMark", borr.getCurrencyMark());
			map.put("lendPrice", borr.getLendPrice());
			map.put("lendName", borr.getLendName());
			map.put("reviewId", borr.getReviewId());
			map.put("applyDate", borr.getApplyDate());
			map.put("remark", borr.getRemark());
			map.put("createBy", borr.getCreateBy());
			//获取累计借款金额
			String travelerId = borr.getTravelerId();
			if (t4priceMap.containsKey(travelerId)) {
				sumLendPrice = t4priceMap.get(travelerId);
			} else {
				sumLendPrice = "";
			}
			sumLendPrice = islandMoneyAmountService.addOrSubtract(borr.getCurrencyMark() + " " + borr.getLendPrice(), sumLendPrice, true);
			t4priceMap.put(borr.getTravelerId(), sumLendPrice);
			map.put("sumLendPrice", sumLendPrice);
			for (HotelTraveler t : travelerList) {
				if (null != borr.getTravelerId() && t.getId().toString().equals(borr.getTravelerId())) {
					map.put("totalMoney", t.getPayPriceSerialNum());
					map.put("personType", t.getPersonType());
					map.put("travelerName", t.getName());
					map.put("travelerId", t.getId());
					map.put("travelerUuid", t.getUuid());
				}
				
			}
			rMap.add(map);
		}
		//对结果集进行翻转（前台按创建时间倒叙排列）
		Collections.reverse(rMap);
		model.addAttribute("bAList", rMap);
		model.addAttribute("orderUuid", orderUuid);
		model.addAttribute("orderId", orderId);
		model.addAttribute("productType", productType);
		return "modules/island/review/viewHotelBorrowing";
	}
	/**
	 * 酒店借款详情展示
	 * @param request
	 * @param response
	 * @param modasdel
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value="viewHotelBorrowingInfo")
	public String viewHotelBorrowingInfo(HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException {
		String orderUuid = request.getParameter("orderUuid");
		String orderId = request.getParameter("orderId");
		String reviewId = request.getParameter("reviewId");
		
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		
		List<ReviewDetail> rdlist = new ArrayList<ReviewDetail>();
		try{
			if(reviewId!=null){
				rdlist= reviewService.queryReviewDetailList(reviewId);
			}
		}catch(Exception e){
			logger.error("根据reviewid： " + reviewId + " 查询出来reviewDetail明细报错 ",e);
		}
		//将总金额和币种显示出来
		List<ReviewDetail> borrowPricesList = new ArrayList<ReviewDetail>();
		if(rdlist!=null&&rdlist.size()>0){
			for(int i = 0;i<rdlist.size();i++){
				if("borrowPrices".equals(rdlist.get(i).getMykey())){
					borrowPricesList.add(rdlist.get(i));
				}
				if("currencyMarks".equals(rdlist.get(i).getMykey())){
					borrowPricesList.add(rdlist.get(i));
				}
			}
		}
		
		List<BorrowingBean> blist = BorrowingBean.transferReviewDetail2BorrowingBean(rdlist);
		List<BorrowingBean> tralist = new ArrayList<BorrowingBean>();
		List<BorrowingBean> teamlist= new ArrayList<BorrowingBean>();
		List<BorrowingBean> borrowList = BorrowingBean.transferReviewDetail2BorrowingBean(borrowPricesList);
		//将blist 拆分为团队借款和游客借款两部分
		if(blist!=null&&blist.size()>0){
			int size = blist.size();
			model.addAttribute("applyDate", blist.get(0).getApplyDate());
			for(int i = 0;i<size;i++){
				BorrowingBean bean = blist.get(i);
				if("0".equals(bean.getTravelerId())){//团队借款
					teamlist.add(bean);
				}else{//游客借款
					tralist.add(bean);
				}
			}
			
		}
		
		if(reviewId!=null&&!"".equals(reviewId)){//显示动态审核的标志
	    	List<ReviewLog> rLog=reviewService.findReviewLog(Long.parseLong(reviewId));
			model.addAttribute("rLog",rLog);
			Review review = reviewService.findReviewInfo(Long.parseLong(reviewId));
			model.addAttribute("review",review);
	    }
		HotelTraveler t = hotelTravelerService.getById(Integer.parseInt(tralist.get(0).getTravelerId()));
		model.addAttribute("traveler",t);
		model.addAttribute("tralist", tralist);
		model.addAttribute("teamlist",teamlist);
		model.addAttribute("borrowList", borrowList);
		model.addAttribute("totalsize", borrowList.size());
		model.addAttribute("reviewId", reviewId);
		List<Currency> currencyList = currencyService.findCurrencyList(companyId);
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("orderUuid", orderUuid);
		model.addAttribute("orderId", orderId);
		model.addAttribute("productType", Context.ORDER_TYPE_HOTEL);
		
		//订单基本信息
		hotelOrderService.getOrderBaseInfo(orderUuid, model);
		return "modules/island/review/viewHotelBorrowingInfo";
	}
	/**
	 * 酒店借款申请
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value="applyHotelBorrowing")
	public String applyHotelBorrowing(HttpServletRequest request,String productType, HttpServletResponse response,Model model) throws ParseException {
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		String orderUuid= request.getParameter("orderUuid");
		String orderId= request.getParameter("orderId");
		HotelOrder hotelOrder = hotelOrderService.getByUuid(orderUuid);
		
		ActivityHotel activityHotel = activityHotelService.getByUuid(hotelOrder.getActivityHotelUuid());
		ActivityHotelGroup productGroup = activityHotelGroupService.getByUuid(hotelOrder.getActivityHotelGroupUuid());
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(hotelOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(hotelOrder.getPayMode()));
		List<Currency> currencyList = currencyService.findSortCurrencyList(companyId);
		model.addAttribute("orderUuid", orderUuid);
		model.addAttribute("orderId", orderId);
		model.addAttribute("currencyList", currencyList);
		
		List<HotelTraveler> travelerList = hotelTravelerService.findTravelerByOrderUuid(orderUuid, true);
		
		model.addAttribute("productOrder", hotelOrder);
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("product", activityHotel);
		List<String> travelUuidList = Lists.newArrayList();
		List<String> travelPriceList = Lists.newArrayList();
		for(HotelTraveler t: travelerList){
			travelUuidList.add(t.getUuid());
			travelPriceList.add(t.getPayPriceSerialNum());
			
		}
		//添加酒店借款申请相关信息
		List<Map<String, String>> reviewMapList = reviewService.findReviewListMap(11,Context.REVIEW_FLOWTYPE_BORROWMONEY, hotelOrder.getId()+"", false,activityHotel.getDeptId().longValue());		
		List<BorrowingBean> reviewList = getBorrowingBeanList(reviewMapList);
		List<Map <String,Object>> rMap = Lists.newArrayList();
		Map<String, String> t4priceMap = Maps.newHashMap();//不同游客累计金额
		String sumLendPrice = ""; //累计借款金额
		for(BorrowingBean borr :reviewList){
			Map <String,Object> map = Maps.newHashMap();
			map.put("currencyName", borr.getCurrencyName());
			map.put("currencyMark", borr.getCurrencyMark());
			map.put("lendPrice", borr.getLendPrice());
			map.put("lendName", borr.getLendName());
			map.put("reviewId", borr.getReviewId());
			map.put("applyDate", borr.getApplyDate());
			map.put("remark", borr.getRemark());
			map.put("createBy", borr.getCreateBy());
			//获取累计借款金额
			String travelerId = borr.getTravelerId();
			if (t4priceMap.containsKey(travelerId)) {
				sumLendPrice = t4priceMap.get(travelerId);
			} else {
				sumLendPrice = "";
			}
			sumLendPrice = islandMoneyAmountService.addOrSubtract(borr.getCurrencyMark() + " " + borr.getLendPrice(), sumLendPrice, true);
			t4priceMap.put(borr.getTravelerId(), sumLendPrice);
			map.put("sumLendPrice", sumLendPrice);
			for(HotelTraveler t : travelerList){
				if(null != borr.getTravelerId() && t.getId().toString().equals(borr.getTravelerId())){
					map.put("totalMoney", t.getPayPriceSerialNum());
					map.put("personType", t.getPersonType());
					map.put("travelerName", t.getName());
					map.put("travelerId", t.getId());
					map.put("travelerUuid", t.getUuid());
				}
				
			}
			rMap.add(map);
		}
		//对结果集进行翻转（前台按创建时间倒叙排列）
		Collections.reverse(rMap);
		model.addAttribute("bAList", rMap);
		model.addAttribute("orderUuid", orderUuid);
		model.addAttribute("orderId", orderId);
		//订单基本信息                                                                                                                                                                                      
		hotelOrderService.getOrderBaseInfo(orderUuid, model);
		String allBorrowingMoney = OrderCommonUtil.getBorrowPayMoneyTravelByOrderType(orderUuid, travelUuidList, 11+"");
		model.addAttribute("totalBorrowingMoney",allBorrowingMoney);
		String totalTravelerPrice = OrderCommonUtil.getHotelMoneyAmountBySerialNums(travelPriceList,2);
		model.addAttribute("totalTravelerPrice",totalTravelerPrice);
		model.addAttribute("flowType", Context.REVIEW_FLOWTYPE_BORROWMONEY);
		model.addAttribute("productType",productType);
		model.addAttribute("travelerList", travelerList);
		return "modules/island/review/applyHotelBorrowing";
	}
	/**
	 * 借款流程申请
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value="saveHotelBorrowing")
	public String saveHotelBorrowing(HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException {
		
		return islandReviewService.saveHotelBorrowing(request);
	}
	
	/**
	 * 查询海岛游改价记录
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="islandChangePriceList")
	public String queryHistroyAUP(Model model,HttpServletResponse response,HttpServletRequest request){
		String orderId = request.getParameter("orderId") ; // 订单编号
		String productType = request.getParameter("productType"); //订单类型
		String flowType = request.getParameter("flowType");  // 流程类型
		String orderUuid = request.getParameter("orderUuid");
		if(StringUtils.isBlank(flowType)) {
			flowType = "10";
		}
		boolean flag = reviewService.verifyWorkFlowStatus(orderId, Integer.parseInt(productType), Integer.parseInt(flowType));
		// 调用公用工作流查询方法,取到流程表里的机票改价记录信息
		List<Map<String, Object>> list = reviewService.findReviewListMap(Integer.parseInt(productType), Integer.parseInt(flowType),orderId, false,"");
		Map<String,String> map = new HashMap<String, String>();
		map.put("orderId", orderId);
		map.put("orderUuid", orderUuid);
		map.put("productType",productType);
		map.put("flowType", flowType);
		model.addAttribute("changePriceList", list);
		model.addAllAttributes(map);
		model.addAttribute("flag", flag);
		return "modules/island/review/viewChangePriceList";
	}
	/**
	 * 查询海岛游改价记录
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="hotelChangePriceList")
	public String queryHotelHistroyAUP(Model model,HttpServletResponse response,HttpServletRequest request){
		String orderId = request.getParameter("orderId") ; // 订单编号
		String productType = request.getParameter("productType"); //订单类型
		String flowType = request.getParameter("flowType");  // 流程类型
		String orderUuid = request.getParameter("orderUuid");
		if(StringUtils.isBlank(flowType)) {
			flowType = "10";
		}
		boolean flag = reviewService.verifyWorkFlowStatus(orderId, Integer.parseInt(productType), Integer.parseInt(flowType));
		// 调用公用工作流查询方法,取到流程表里的机票改价记录信息
		List<Map<String, Object>> list = reviewService.findReviewListMap(Integer.parseInt(productType), Integer.parseInt(flowType),orderId, false,"");
		Map<String,String> map = new HashMap<String, String>();
		map.put("orderId", orderId);
		map.put("orderUuid", orderUuid);
		map.put("productType",productType);
		map.put("flowType", flowType);
		model.addAttribute("changePriceList", list);
		model.addAllAttributes(map);
		model.addAttribute("flag", flag);
		return "modules/island/review/viewHotelChangePriceList";
	}
	/**
	 * 改价取消申请
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="cancelIslandChangePriceRequest")
	public String cancelIslandChangePriceRequest(Model model,HttpServletResponse response,HttpServletRequest request){
		
		String orderId = request.getParameter("orderId") ; // 订单编号
		String productType = request.getParameter("productType"); //产品类型
		String flowType = request.getParameter("flowType");  // 流程类型
		String revid = request.getParameter("revid");
		reviewService.removeReview(Long.parseLong(revid));
		return "redirect:"+Global.getAdminPath()+"/island/review/islandChangePriceList?orderId=" + orderId + "&productType=" + productType + "&flowType=" + flowType;
	}
	/**
	 * 改价取消申请
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="cancelHotelChangePriceRequest")
	public String cancelHotelChangePriceRequest(Model model,HttpServletResponse response,HttpServletRequest request){
		
		String orderId = request.getParameter("orderId") ; // 订单编号
		String productType = request.getParameter("productType"); //产品类型
		String flowType = request.getParameter("flowType");  // 流程类型
		String revid = request.getParameter("revid");
		reviewService.removeReview(Long.parseLong(revid));
		return "redirect:"+Global.getAdminPath()+"/island/review/hotelChangePriceList?orderId=" + orderId + "&productType=" + productType + "&flowType=" + flowType;
	}
	/**
	 * 查看海岛游改价详情
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="viewChangePriceInfo")
	public String viewChangePriceInfo(Model model,HttpServletResponse response,HttpServletRequest request){
		String orderId = request.getParameter("orderId") ; // 订单编号
		String productType = request.getParameter("productType"); //订单类型
		String flowType = request.getParameter("flowType");  // 流程类型
		String idStr = request.getParameter("id");
		Long id = Long.parseLong(idStr);
		String orderUuid = request.getParameter("orderUuid");
		if(StringUtils.isBlank(flowType)) {
			flowType = "10";
		}
		// 调用公用工作流查询方法,取到流程表里的机票改价记录信息
		List<Map<String, Object>> list = reviewService.findReviewListMapById(Integer.parseInt(productType), Integer.parseInt(flowType),orderId,id);
		Map<String,String> map = new HashMap<String, String>();
		map.put("orderId", orderId);
		map.put("productType",productType);
		map.put("flowType", flowType);
		model.addAttribute("airticketReturnList", list);
		model.addAllAttributes(map);
		
		//订单基本信息
				islandOrderService.getOrderBaseInfo(orderUuid, model);
		model.addAttribute("rid", idStr);
		
		return "modules/island/review/viewChangePriceInfo";
	}
	/**
	 * 查看酒店改价详情
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="viewHotelChangePriceInfo")
	public String viewHotelChangePriceInfo(Model model,HttpServletResponse response,HttpServletRequest request){
		String orderId = request.getParameter("orderId") ; // 订单编号
		String productType = request.getParameter("productType"); //订单类型
		String flowType = request.getParameter("flowType");  // 流程类型
		String idStr = request.getParameter("id");
		Long id = Long.parseLong(idStr);
		String orderUuid = request.getParameter("orderUuid");
		if(StringUtils.isBlank(flowType)) {
			flowType = "10";
		}
		// 调用公用工作流查询方法,取到流程表里的机票改价记录信息
		List<Map<String, Object>> list = reviewService.findReviewListMapById(Integer.parseInt(productType), Integer.parseInt(flowType),orderId,id);
		Map<String,String> map = new HashMap<String, String>();
		map.put("orderId", orderId);
		map.put("productType",productType);
		map.put("flowType", flowType);
		model.addAttribute("airticketReturnList", list);
		model.addAllAttributes(map);
		
		
		model.addAttribute("rid", idStr);
		//订单基本信息
		hotelOrderService.getOrderBaseInfo(orderUuid, model);
		return "modules/island/review/viewHotelChangePriceInfo";
	}
	/**
	 * 跳转到海岛游申请改价页面
	 * @param model
	 * @param response
	 * @param request
	 * @return 
	 */
	@RequestMapping(value="upIslandPrices")
	public String gotoUpIslandPrices(Model model,HttpServletResponse response,HttpServletRequest request){
		String orderId = request.getParameter("orderId") ; // 订单编号
		String productType = request.getParameter("productType"); //订单类型
		String flowType = request.getParameter("flowType");  // 流程类型
		String productTypeSecond = request.getParameter("productType");
		String orderUuid = request.getParameter("orderUuid");
		if(StringUtils.isBlank(orderId)	) {
			throw new RuntimeException("订单id不能为空");
		}
		
		IslandOrder islandOrder = islandOrderService.getById(Integer.parseInt(orderId));		
		// 获取游客列表信息~
		List<Map<String,Object>> travelerList = activityUpPricesService.queryIslandTravelerList(orderUuid);
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		
		//订单基本信息
		islandOrderService.getOrderBaseInfo(orderUuid, model);
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("orderId", orderId);
		map.put("productType",productType);
		map.put("flowType", flowType);
		map.put("orderTotalMoney", islandOrder.getTotalMoney());
		map.put("orderUuid", orderUuid);
		
		model.addAttribute("travelerList",travelerList); //添加游客信息,游客信息在service层已经做过处理了，直接加入返回就可以了。		
		model.addAttribute("productTypeSecond", productTypeSecond);
		model.addAttribute("curlist", currencylist);		
		model.addAllAttributes(map);						
		
		return "modules/activity/upIslandActivityDetails";
	}
	/**
	 * 跳转到酒店申请改价页面
	 * @param model
	 * @param response
	 * @param request
	 * @return 
	 */
	@RequestMapping(value="upHotelPrices")
	public String gotoUpHotelPrices(Model model,HttpServletResponse response,HttpServletRequest request){
		String orderId = request.getParameter("orderId") ; // 订单编号
		String productType = request.getParameter("productType"); //订单类型
		String flowType = request.getParameter("flowType");  // 流程类型
		String productTypeSecond = request.getParameter("productType");
		String orderUuid = request.getParameter("orderUuid");
		if(StringUtils.isBlank(orderId)	) {
			throw new RuntimeException("订单id不能为空");
		}
				
		HotelOrder hotelOrder = hotelOrderService.getById(Integer.parseInt(orderId));
		Map<String, Object> resultMaps = new HashMap<String, Object>() ;  // 返回结果集合 
		//Map<String,Object> rawMapDJ = new HashMap<String, Object>();  // 定金
		
		// 获取游客列表信息~
		List<Map<String,Object>> travelerList = activityUpPricesService.queryHotelTravelerList(orderUuid);
		resultMaps.put("travelers",travelerList); //添加游客信息,游客信息在service层已经做过处理了，直接加入返回就可以了。/		resultMaps.put("html", travelers);
		
		/* 订金如果是空的会赋上默认值*/
		/*
		if(null == rawMapDJ.get(ChangePriceBean.CHANGED_FUND)){
			rawMapDJ.put(ChangePriceBean.CHANGED_FUND, "订金");
		} if(null == rawMapDJ.get(ChangePriceBean.CURRENCY_NAME)){
			rawMapDJ.put(ChangePriceBean.CURRENCY_NAME, "人民币");
		} if(null == rawMapDJ.get(ChangePriceBean.OLD_TOTAL_MONEY)){
			rawMapDJ.put(ChangePriceBean.OLD_TOTAL_MONEY, "0.00");
		} if(null == rawMapDJ.get(ChangePriceBean.CUR_TOTAL_MONEY)){
			rawMapDJ.put(ChangePriceBean.CUR_TOTAL_MONEY, "0.00");
		}if(null == rawMapDJ.get(ChangePriceBean.CURRENCY_ID)){
			rawMapDJ.put(ChangePriceBean.CURRENCY_ID, "1");
		}
		*/
		//resultMaps.put("frontmoney", rawMapDJ);
		model.addAttribute("travelerList", travelerList);
		model.addAttribute("resultMaps", resultMaps);
		model.addAttribute("productTypeSecond", productTypeSecond);
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		model.addAttribute("curlist", currencylist);
		Map<String,String> map = new HashMap<String, String>();
		map.put("orderId", orderId);
		map.put("orderUuid", orderUuid);
		map.put("productType",productType);
		map.put("flowType", flowType);
		if (hotelOrder != null) {			
			map.put("orderTotalMoney", hotelOrder.getTotalMoney());
		}
		model.addAllAttributes(map);
		
		//订单基本信息
		hotelOrderService.getOrderBaseInfo(orderUuid, model);
		return "modules/activity/upHotelActivityDetails";
	}	
	
	/**
	 * 获取前台请求参数
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getRequestParamater(HttpServletRequest request) {
		Map parmap = request.getParameterMap();
		Set  set = parmap.entrySet();
		Iterator it = set.iterator();
		Map map = new HashMap();
		while(it.hasNext()){
			Entry<String, Object> entry = (Entry<String, Object>) it.next();
			map.put(entry.getKey().toLowerCase(), entry.getValue());
		}
		return  map;
	}
	
}
