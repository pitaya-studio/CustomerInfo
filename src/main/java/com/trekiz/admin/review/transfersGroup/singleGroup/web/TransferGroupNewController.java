package com.trekiz.admin.review.transfersGroup.singleGroup.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.CommonResult;
import com.quauq.review.core.support.ReviewResult;
import com.quauq.review.core.type.OrderByDirectionType;
import com.quauq.review.core.type.OrderByPropertiesType;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.review.common.service.ICommonReviewService;
import com.trekiz.admin.review.money.entity.NewProcessMoneyAmount;
import com.trekiz.admin.review.money.service.NewProcessMoneyAmountService;
import com.trekiz.admin.review.mutex.ReviewMutexContext;
import com.trekiz.admin.review.mutex.ReviewMutexService;
import com.trekiz.admin.review.transfersGroup.singleGroup.service.TransferGroupNewService;

/**
 * 新转团申请
 * @author yakun.bai
 * @Date 2015-12-5
 */
@Controller
@RequestMapping(value = "${adminPath}/newTransferGroup")
public class TransferGroupNewController {

	@Autowired
	private ReviewService reviewService;
	@Autowired
	private TransferGroupNewService transferGroupService;
	@Autowired
	private ActivityGroupService activityGroupService;
	@Autowired
	private TravelerService travelerService;
	@Autowired
	@Qualifier("orderCommonService")
    private OrderCommonService orderService;
	@Autowired
	private TravelActivityService travelActivityService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private TravelerDao travelerDao;
	@Autowired
	private AreaService areaServce;
	@Autowired
	private com.quauq.review.core.engine.ReviewService processReviewService;
	@Autowired
	private ICommonReviewService commonReviewService;
	@Autowired
	private NewProcessMoneyAmountService newAmountService;
	@Autowired
	private ReviewMutexService reviewMutexService;
	@Autowired
	private OrderCommonService orderCommonService;
	
	/** 转团申请列表地址 */
	private static final String LIST_PAGE = "/review/transferGroup/singleGroup/transferGroupList";
	/** 转团申请页面地址 */
	private static final String TRANSFER_GROUP_PRICE_PAGE = "/review/transferGroup/singleGroup/transferGroupPage";
	/** 转团详情地址 */
	private static final String TRANSFER_GROUP_INFO_PAGE = "/review/transferGroup/singleGroup/transferGroupInfo";
	
	/**
	 * @Description 转团申请列表
	 * @param orderId 订单id
	 * @author yakun.bai
	 * @Date 2015-12-5
	 */
	@RequestMapping(value = "list/{orderId}")
	public String list(@PathVariable Long orderId, Model model) {
		
		//查询订单
		ProductOrderCommon order = orderService.getProductorderById(Long.valueOf(orderId));
		//查询产品及部门ID
		TravelActivity product = travelActivityService.findById(order.getProductId());
		Long deptId = product.getDeptId();
		
		//查询转团申请列表
		List<Map<String, Object>> transferGroupList = processReviewService.getReviewDetailMapListByOrderId(deptId, product.getActivityKind(), 
						Context.REVIEW_FLOWTYPE_TRANSFER_GROUP, orderId.toString(), OrderByPropertiesType.CREATE_DATE, OrderByDirectionType.DESC);
		// 考虑到转团多对一的变动，在此处理相关数据
		transferGroupService.handleData4ManyTraveler(transferGroupList);
		//值传递
		model.addAttribute("transferGroupList", transferGroupList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("productType", product.getActivityKind());
		model.addAttribute("orderTypeStr", OrderCommonUtil.getChineseOrderType(product.getActivityKind().toString()));

		return LIST_PAGE;
	}
	
	/**
	 * @Description 转团申请页面
	 * @param orderId 订单id
	 * @param productType 订单类型
	 * @author yakun.bai
	 * @Date 2015-12-7
	 */
	@RequestMapping("/transferGroupPage")
	public String transferGroupPage(@RequestParam("orderId") Long orderId, @RequestParam("productType") Integer productType, Model model) {
		
		//查询订单
		ProductOrderCommon order = orderService.getProductorderById(orderId);
		//查询产品
		TravelActivity travelActivity = travelActivityService.findById(order.getProductId());
		//查询团期
		ActivityGroup activityGroup = activityGroupService.findById(order.getProductGroupId());
		// 获取订单应付款总额
		String totalMoney = moneyAmountService.getMoney(order.getTotalMoney());
		
		handlePrice(order, model);
		
		// 获取游客信息
		List<Integer> delFlag = Lists.newArrayList();
		delFlag.add(Context.TRAVELER_DELFLAG_NORMAL);
		delFlag.add(Context.TRAVELER_DELFLAG_EXIT);
		delFlag.add(Context.TRAVELER_DELFLAG_TURNROUND);
		List<Traveler> list = travelerService.findTravelerByOrderIdAndOrderType(Long.valueOf(orderId), order.getOrderStatus(), delFlag);
		
		List<Traveler> backList = Lists.newArrayList();
		
		if (CollectionUtils.isNotEmpty(list)) {
			Iterator<Traveler>  iter = list.iterator();
			while(iter.hasNext()){
				Traveler tra = iter.next();
				String money = moneyAmountService.getMoney(tra.getPayPriceSerialNum());
				List<Object[]> objList = moneyAmountService.getMoneyAmonut(tra.getPayPriceSerialNum());
				if (CollectionUtils.isNotEmpty(objList)) {
					String currencyId = "";
					for (Object[] obj : objList) {
						currencyId += obj[0] + ",";
					}
					tra.setSubtractMoneySerialNum(currencyId);
				}
				tra.setPayPriceSerialNumInfo(money);
				backList.add(tra);
			}
		}
		
		//值传递
		model.addAttribute("orderType", order.getOrderStatus());// 订单类型
		model.addAttribute("priceType", order.getPriceType());// 价格类型
		model.addAttribute("retailAdultPrice", activityGroup.getAdultRetailPrice());// 成人供应价
		model.addAttribute("retailChildPrice", activityGroup.getChildRetailPrice());// 儿童供应价
		model.addAttribute("retailSpecialPrice", activityGroup.getSpecialRetailPrice());// 特殊人群供应价
		model.addAttribute("product",travelActivity);
		model.addAttribute("group",activityGroup);
		model.addAttribute("order",order);
		model.addAttribute("travelList",backList);
		model.addAttribute("totalMoney",totalMoney);
		model.addAttribute("orderStatus", OrderCommonUtil.getChineseOrderType(order.getOrderStatus().toString()));
		model.addAttribute("user",UserUtils.getUser());
		model.addAttribute("orderId",orderId);
		model.addAttribute("orderStatusNum",order.getOrderStatus().toString());
		// 出发城市
		if (travelActivity.getFromArea() != null) {
			Area fromArea = areaServce.get(Long.valueOf(travelActivity.getFromArea()));
			model.addAttribute("fromArea",fromArea);
		}
		// 离境城市
		if (travelActivity.getOutArea() != null) {
			Area outArea = areaServce.get(Long.valueOf(travelActivity.getOutArea()));
			model.addAttribute("outArea",outArea);
		}
		
		return TRANSFER_GROUP_PRICE_PAGE;
	}
	
	/**
	 * @Description 订单金额千位符处理：成人价、儿童价、特殊人群价格
	 * @author yakun.bai
	 * @Date 2015-12-7
	 */
	private void handlePrice(ProductOrderCommon order, Model model) {
		
		//成人价格
		if (StringUtils.isNotBlank(order.getSettlementAdultPrice())) {
			String settlementAdultPrice = moneyAmountService.getMoneyStr(order.getSettlementAdultPrice());
			model.addAttribute("settlementAdultPrice", settlementAdultPrice);
		}
		//儿童价格
		if (StringUtils.isNotBlank(order.getSettlementcChildPrice())) {
			String settlementcChildPrice = moneyAmountService.getMoneyStr(order.getSettlementcChildPrice());
			model.addAttribute("settlementcChildPrice", settlementcChildPrice);
		}
		//特殊人群价格
		if (StringUtils.isNotBlank(order.getSettlementSpecialPrice())) {
			String settlementSpecialPrice = moneyAmountService.getMoneyStr(order.getSettlementSpecialPrice());
			model.addAttribute("settlementSpecialPrice", settlementSpecialPrice);
		}
	}
	
	/**
	 * @Description 转团申请
	 * @author yakun.bai
	 * @Date 2015-12-7
	 */
	@ResponseBody
	@RequestMapping(value = "applyTransferGroup", method=RequestMethod.POST)
	public Map<String,String> applyTransferGroup(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		response.setContentType("application/json; charset=UTF-8");
		
		Map<String,String> map = Maps.newHashMap();
		//游客ID数组
		String travelIds = request.getParameter("paramTravelId"); 
		String[] travelId = travelIds.split(",");
		//新团期code
		String groupCode = request.getParameter("paramGroupCode"); 
		//转团后应付金额,格式：游客ID#币种ID#币种金额,游客ID#币种ID#币种金额,游客ID#币种ID#币种金额,……
		String subtractMoneys = request.getParameter("subtractMoneyArr");
		 //转团备注数组
		String remarks = request.getParameter("paramRemark");
		String[] remark = remarks.split(",");
		//支付方式
		String payType = request.getParameter("paramPayType");
		//保留天数
		String remainDays = request.getParameter("paramRemainDays"); 
		
		Long orderId = null;
		//获取全部要转团的游客
		List<Traveler> travelList = Lists.newArrayList();
		//获取新团期实体
		ActivityGroup activityGroup = activityGroupService.findByGroupCode(groupCode);

		for (String id : travelId) {
			Traveler tra = travelerService.findTravelerById(Long.valueOf(id));
			// 判断新团期里是否有该游客类型的报价（成人同行价，儿童同行价，特殊同行价）
			if (tra.getPersonType() == 1) {
				if (activityGroup.getSettlementAdultPrice() == null) {
					map.put("result", "data_error");
					map.put("msg", "申请失败，新团期不存在成人价格。");
					return map;
				}
			}
			if (tra.getPersonType() == 2) {
				if (activityGroup.getSettlementcChildPrice() == null) {
					map.put("result", "data_error");
					map.put("msg", "申请失败，新团期不存在儿童价格。");
					return map;
				}
			}
			if (tra.getPersonType() == 3) {
				if (activityGroup.getSettlementSpecialPrice() == null) {
					map.put("result", "data_error");
					map.put("msg", "申请失败，新团期不存在特殊人群价格。");
					return map;
				}
			}
			orderId = tra.getOrderId();	// 获取原订单ID
			travelList.add(tra);
		}
		// 获取原订单实体
		ProductOrderCommon oldOrder = orderService.getProductorderById(orderId);
		// 获取原产品实体
		TravelActivity ta = travelActivityService.findById(oldOrder.getProductId());
		
		// 如果是quauq渠道，则新团需有quauq价
		if (oldOrder.getOrderStatus() == 2 && oldOrder.getPriceType() == 2) {
			BigDecimal quauqAultPrice = activityGroup.getQuauqAdultPrice();
			BigDecimal quauqChildPrice = activityGroup.getQuauqChildPrice();
			BigDecimal specialPrice = activityGroup.getQuauqSpecialPrice();
			if (quauqAultPrice == null && quauqChildPrice == null && specialPrice == null) {
				map.put("result", "data_error");
				map.put("msg", "申请失败，新团期暂无供应价");
				return map;
			}
		}
		
		try {
			map = transferGroupService.reviewApply(subtractMoneys, map, travelList, activityGroup, remark, payType, remainDays, oldOrder, ta, request);
		} catch (Exception e) {
			map.put("result", "error");
			map.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * @Description 转团流程互斥校验
	 * @author yakun.bai
	 * @Date 2015-12-22
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("/checkSingleGroupExitGroup")
	public Map<String, Object> checkSingleGroupExitGroup(String orderId, String productType, String travelerIds) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("flag", true);
		if (StringUtils.isBlank(travelerIds)) {
			resultMap.put("flag", false);
			resultMap.put("msg", "游客信息不能为空");
			return resultMap;
		}
		String[] tras = travelerIds.split(",");
		if (tras == null || tras.length == 0) {
			resultMap.put("flag", false);
			resultMap.put("msg", "游客信息不能为空");
			return resultMap;
		}
		//遍历调用接口组织返回的数据
		CommonResult checkResult = null;
		StringBuffer showMsg = new StringBuffer();
		//所有互斥的流程idlist
		String idStrings = "";
		int nFlag = 0;
		List<ReviewNew> reviewNews = new ArrayList<ReviewNew>();
		//是否互斥标志
		Boolean isMutexBoolean = false;
		//是否有取消权限
		Boolean isCanCancel = true;
		for(String travellerId : tras){
			//调用互斥接口
			checkResult = reviewMutexService.check(orderId, travellerId, productType, Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.toString(), false);
			if(!checkResult.getSuccess()){
				isMutexBoolean = true;
				Map<String, Object> params = checkResult.getParams();
				//互斥的审批流程list
				reviewNews = (List<ReviewNew>) params.get(ReviewMutexContext.PROCESSING_REVIEWS);
				if(reviewNews != null && reviewNews.size() > 0){
					for(ReviewNew r : reviewNews){
						if(nFlag == 0){
							idStrings += r.getId();
							nFlag++;
						} else {
							idStrings += "," + r.getId();
						}
					}
				}
				showMsg.append(checkResult.getMessage() + "</br>");
				reviewNews = new ArrayList<ReviewNew>();
				//是否有取消权限
				Boolean canCancel = (Boolean) params.get("canCancel");
				if(!canCancel){
					isCanCancel = false;
				}
			}
//			} else {
//				resultMap.put("flag", false);
//				resultMap.put("msg", checkResult.getMessage());
//				return resultMap;
//			}
			//清空互斥返回结果
			checkResult = null;
		}
		if(isCanCancel){
			showMsg.append("确定要取消以上流程，并发起转团流程吗？");
		} else {
			showMsg.append("请重新选择游客");
		}
		resultMap.put("showMsg", showMsg);
		resultMap.put("isMutexBoolean", isMutexBoolean);
		resultMap.put("canCancel", isCanCancel);
		resultMap.put("ids", idStrings);
		return resultMap;
	}

	/**
	 * @Description QUAUQ价格为空不能转（ 转团流程校验）
	 * @author yang.jiang
	 * @Date 2016-7-4
	 * @param orderId 转出订单
	 * @param productType 订单类型
	 * @param travelerIds 转团游客
	 * @param newGroupCode 转入团号
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/checkQuauqPrice4TransferGroup")
	public Map<String, Object> checkQuauqPrice4TransferGroup(String orderId, String travelerIds, String newGroupCode) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("flag", "success");
		// 请求信息不能缺失
		if (StringUtils.isBlank(travelerIds)) {
			resultMap.put("flag", "faild");
			resultMap.put("message", "游客信息不能为空");
			return resultMap;
		}
		String[] tras = travelerIds.split(",");
		if (tras == null || tras.length == 0) {
			resultMap.put("flag", "faild");
			resultMap.put("message", "游客信息不能为空");
			return resultMap;
		}
		if (StringUtils.isBlank(newGroupCode)) {
			resultMap.put("flag", "faild");
			resultMap.put("message", "转入团号不能为空");
			return resultMap;
		}
		// 
		ProductOrderCommon order = orderCommonService.getProductorderById(Long.parseLong(orderId));
		Office orderOffice = order.getCreateBy().getCompany();
		ActivityGroup group = activityGroupService.findByGroupCodeAndOffice(newGroupCode, orderOffice.getId());
		// 如果根本没有quauq价，则直接返回
		if (group.getQuauqAdultPrice() == null && group.getQuauqChildPrice() == null && group.getQuauqSpecialPrice() == null) {
			resultMap.put("flag", "faild");
			resultMap.put("message", "团期供应价不存在");
			return resultMap;
		}
		// 依据游客类型，判断quauq价
		String flagString = "success";
		String adultMessage = "";
		String childMessage = "";
		String specialMessage = "";
		for (String travelerId : tras) {
			Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerId));
			if (traveler.getPersonType().intValue() == 1 && group.getQuauqAdultPrice() == null) {
				flagString = "faild";
				if (StringUtils.isBlank(adultMessage)) {					
					adultMessage += "转入团中成人QUAUQ价不存在。";
				}
			} else if (traveler.getPersonType().intValue() == 2 && group.getQuauqChildPrice() == null) {
				flagString = "faild";
				if (StringUtils.isBlank(childMessage)) {					
					childMessage += "转入团中儿童QUAUQ价不存在。";
				}
			} else if (traveler.getPersonType().intValue() == 3 && group.getQuauqSpecialPrice() == null) {
				flagString = "faild";
				if (StringUtils.isBlank(specialMessage)) {					
					specialMessage += "转入团中特殊人群QUAUQ价不存在。";
				}
				
			}
		}
		resultMap.put("flag", flagString);
		resultMap.put("message", adultMessage + childMessage + specialMessage);
		return resultMap;
	}
	
	/**
	 * @Description 流程互斥
	 * @author yakun.bai
	 * @Date 2015-12-22
	 */
	@ResponseBody
	@RequestMapping(value="/cancelOtherReview")
	public Map<String, Object> cancelOtherReview(String ids){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(StringUtils.isBlank(ids)){
			resultMap.put("success", "success");
			return resultMap;
		}
		//遍历取消
		String[] strings = ids.split(",");
		Long userId = UserUtils.getUser().getId();
		String uuid = UserUtils.getUser().getCompany().getUuid();
		StringBuffer msg = new StringBuffer();
		for(String reviewId : strings){
			ReviewNew reviewNew = reviewService.getReview(reviewId);
			Integer processType = Integer.parseInt(reviewNew.getProcessType());
			// 退团申请与转团、转款、退款、改价、返佣、借款互斥
			ReviewResult reviewResult = null;
			if(Context.REVIEW_FLOWTYPE_TRANSFER_GROUP == processType) {
				// 转团没有后续的逻辑，所以直接取消
				reviewResult = processReviewService.cancel(userId.toString(), uuid, "", reviewId, "", null);
			} else if(Context.REVIEW_FLOWTYPE_TRANSFER_MONEY == processType) {
				reviewResult = processReviewService.cancel(userId.toString(), uuid, "", reviewId, "", null);
				if (reviewResult.getSuccess()) {
					String travellerId = reviewNew.getTravellerId();
					//改变游客状态
					Traveler traveler = travelerService.findTravelerById(Long.parseLong(travellerId));
					traveler.setDelFlag(Integer.valueOf(Context.DEL_FLAG_NORMAL));
					travelerService.saveTraveler(traveler);
					//金额状态改变
					NewProcessMoneyAmount moneyAmount = newAmountService.findByReviewId(reviewId);
					moneyAmount.setDelFlag(Context.DEL_FLAG_DELETE);
				}
			} else if(Context.REVIEW_FLOWTYPE_CHANGE_PRICE == processType) {
				// 改价也没有后续的逻辑处理
				reviewResult = processReviewService.cancel(userId.toString(), uuid, "", reviewId, "", null);
			} else if(Context.REBATES_FLOW_TYPE == processType) {
				// 返佣之后有逻辑处理
				reviewResult = processReviewService.cancel(userId.toString(), uuid, "", reviewId, "", null);
				boolean flag = reviewResult.getSuccess();
				if (flag) {
					// 对成本录入进行更改
					ReviewNew review = reviewService.getReview(reviewId);
					commonReviewService.updateCostRecordStatus(review, 3);
				}
			} else if(Context.REVIEW_FLOWTYPE_BORROWMONEY == processType) {
				// 借款之后有逻辑处理
				reviewResult = processReviewService.cancel(userId.toString(), uuid, "", reviewId, "", null);
			} else if(Context.REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE == processType) {
				// 优惠之后没有逻辑处理
				reviewResult = processReviewService.cancel(userId.toString(), uuid, "", reviewId, "", null);
			}
			if(!reviewResult.getSuccess()){
				msg.append(reviewResult.getMessage());
			}
		}
		if(StringUtils.isBlank(msg)){
			resultMap.put("success", "success");
			return resultMap;
		}
		resultMap.put("success", "fail");
		resultMap.put("msg", msg);
		return resultMap;
	}

	/**
	 * @Description 转团申请详情
	 * @author yakun.bai
	 * @Date 2015-12-7
	 */
	@RequestMapping(value="transferGroupInfo")
	public String transferGroupInfo(@RequestParam("reviewId") String reviewId, Model model, HttpServletRequest request) {
		
		//查询转团申请信息
		Map<String, Object> reviewDetailMap = reviewService.getReviewDetailMapByReviewId(reviewId);
		
		//订单ID
		Long orderId = Long.parseLong(reviewDetailMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID).toString());
		//转入团ID
		Long groupId = Long.parseLong(reviewDetailMap.get(Context.REVIEW_VARIABLE_TRANSFER_GROUP_NEW_GROUP_ID).toString());

		//查询订单
		ProductOrderCommon order = orderService.getProductorderById(orderId);
		handlePrice(order, model);
		//查询产品
		TravelActivity travelActivity = travelActivityService.findById(order.getProductId());
		//查询团期
		ActivityGroup activityGroup = activityGroupService.findById(order.getProductGroupId());
		//查询订单总额
		String totalMoney = moneyAmountService.getMoney(order.getTotalMoney());
		
		//查询转入团期
		ActivityGroup newGroup =  activityGroupService.findById(groupId);
		// 处理map
		transferGroupService.handleMap4ManyTraveler(reviewDetailMap);
		//游客信息（分情况）
		List<Traveler> travelers = new ArrayList<>();
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> travelerMapList = (List<Map<String, Object>>) reviewDetailMap.get("travelerMapList");  // 诸位游客信息 map
		for (Map<String, Object> map : travelerMapList) {
			travelers.add((Traveler)map.get("traveler"));
		}
		
		//值传递
		model.addAttribute("orderStatus", OrderCommonUtil.getChineseOrderType(order.getOrderStatus().toString()));
		model.addAttribute("product", travelActivity);
		model.addAttribute("travelerList", travelers);
		model.addAttribute("reviewDetailMap", reviewDetailMap);
		model.addAttribute("group", activityGroup);
		model.addAttribute("totalMoney", totalMoney);
		model.addAttribute("order", order);
		model.addAttribute("newGroup", newGroup);
		model.addAttribute("isReview", request.getParameter("isReview"));
		//出发城市
		if (travelActivity.getFromArea() != null) {
			Area fromArea = areaServce.get(Long.valueOf(travelActivity.getFromArea()));
			model.addAttribute("fromArea", fromArea);
		}
		//离境城市
		if (travelActivity.getOutArea() != null) {
			Area outArea = areaServce.get(Long.valueOf(travelActivity.getOutArea()));
			model.addAttribute("outArea", outArea);
		}
		
		return TRANSFER_GROUP_INFO_PAGE;
	}
	
	/**
	 * @Description 取消转团申请
	 * @author yakun.bai
	 * @Date 2015-12-7
	 */
	@ResponseBody
	@RequestMapping("/cancelTransferGroup")
	public Map<String, Object> cancelTransferGroup(String reviewId, String travelerId) {
		return transferGroupService.cancelTransferGroup(reviewId, travelerId);
	}
}
