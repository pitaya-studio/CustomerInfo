package com.trekiz.admin.review.changePrice.common.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.AreaUtil;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.OrderContactsDao;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.service.CostchangeService;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.SysOfficeProductType;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.service.SysOfficeConfigurationService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.review.changePrice.common.service.IChangePriceReviewService;
import com.trekiz.admin.review.changePrice.singleGroup.service.IChangePriceNewService;

@Controller
@RequestMapping(value = "${adminPath}/changepricenew")
public class ChangePriceReviewNewController {

	@Autowired
	private IChangePriceReviewService changePriceReviewService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private OrderContactsDao orderContactsDao;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private VisaProductsService visaProductsService;
	@Autowired
	private DictService dictService;
	@Autowired
	private UserReviewPermissionChecker userReviewPermissionChecker;
	@Autowired
	private ActivityAirTicketDao activityAirTicketDao;
	@Autowired
	private SysOfficeConfigurationService sysOfficeConfigurationService;
	
	@Autowired 
	private TravelerService travelerService;
	@Autowired
	private CostchangeService costchangeService;
	@Autowired
	private IChangePriceNewService changePriceService;
	@Autowired
	private ProductOrderCommonDao orderDao;
	
	/**
	 * 查询改价审核列表
	 */
	@RequestMapping(value = "/changpricereviewnewlist")
	public String getChangePriceReviewList(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		List<SysOfficeProductType> processTypes = sysOfficeConfigurationService.obtainOfficeProductTypes(UserUtils.getUser().getCompany().getUuid());
		Map<String, Object> params = prepareParams(request, response);
		Page<Map<String, Object>> page = changePriceReviewService
				.queryChangePriceReviewList(params,changePriceService);
		model.addAttribute("conditionsMap", params);
		model.addAttribute("processTypes", processTypes);
		model.addAttribute("page", page);
		return "review/changePrice/common/reviewChangePriceNewList";
	}

	/**
	 * 组织参数
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	private Map<String, Object> prepareParams(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> result = new HashMap<String, Object>();
		/** 获取参数 start */
		// 团号/产品名称/订单号
		String groupCode = request.getParameter("groupCode") == null ? null
				: request.getParameter("groupCode").toString();
		// 产品类型(id)
		String productType = request.getParameter("productType") == null ? null
				: request.getParameter("productType").toString();
		// 渠道商(id)
		String agentId = request.getParameter("agentId") == null ? null
				: request.getParameter("agentId").toString();
		// 申请日期（from）
		String applyDateFrom = request.getParameter("applyDateFrom") == null ? null
				: request.getParameter("applyDateFrom").toString();
		// 申请日期（to）
		String applyDateTo = request.getParameter("applyDateTo") == null ? null
				: request.getParameter("applyDateTo").toString();
		// 审批发起人(id)
		String applyPerson = request.getParameter("applyPerson") == null ? null
				: request.getParameter("applyPerson").toString();
		// 计调(id)
		String operator = request.getParameter("operator") == null ? null
				: request.getParameter("operator").toString();
		// 审批状态
		String reviewStatus = request.getParameter("reviewStatus") == null ? null
				: request.getParameter("reviewStatus").toString();
		// 页签选择状态
		String tabStatus = request.getParameter("tabStatus") == null ? null
				: request.getParameter("tabStatus").toString();
		if(tabStatus == null || "".equals(tabStatus)){//默认为待本人审核
			tabStatus = Context.NumberDef.NUMER_ONE.toString();
		}
		// 创建日期排序标识
		String orderCreateDateSort = request
				.getParameter("orderCreateDateSort");
		// 更新日期排序标识
		String orderUpdateDateSort = request
				.getParameter("orderUpdateDateSort");
		// 订单创建日期排序标识
		String orderCreateDateCss = request.getParameter("orderCreateDateCss");
		// 订单更新日期排序标识
		String orderUpdateDateCss = request.getParameter("orderUpdateDateCss");
		if(StringUtils.isBlank(orderCreateDateSort) && StringUtils.isBlank(orderUpdateDateSort) && StringUtils.isBlank(orderCreateDateCss) && StringUtils.isBlank(orderUpdateDateCss)){
			orderCreateDateSort = "desc";
			orderCreateDateCss = "activitylist_paixu_moren";
		}
		// page对象
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(request,
				response);
		/** 获取参数 end */
		/** 组装参数 start */
		result.put("groupCode", groupCode);
		result.put("productType", productType);
		result.put("agentId", agentId);
		result.put("applyDateFrom", applyDateFrom);
		result.put("applyDateTo", applyDateTo);
		result.put("applyPerson", applyPerson);
		result.put("operator", operator);
		result.put("reviewStatus", reviewStatus);
		result.put("tabStatus", tabStatus);
		result.put("orderCreateDateSort", orderCreateDateSort);
		result.put("orderUpdateDateSort", orderUpdateDateSort);
		result.put("orderCreateDateCss", orderCreateDateCss);
		result.put("orderUpdateDateCss", orderUpdateDateCss);
		result.put("paymentType",request.getParameter("paymentType"));
		result.put("pageP", page);
		/** 组装参数 end */
		return result;
	}

	/**
	 * 改价审核详情页
	 */
	@RequestMapping(value = "/changpricereviewnewdetail")
	public String changePriceReviewDetail(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderid");// 订单id
		String reviewId = request.getParameter("revid");// 审核表id
		String nowlevel = request.getParameter("nowlevel");
		// 查询审批详情信息
		// 产品类型 单办 还是参团 所查询的信息是不同的
		String prdType = request.getParameter("prdType");
		if (prdType == null || "".equals(prdType.trim())) {
			return null;
		}
		if ("7".equals(prdType.trim())) {// 7代表机票 相当于单办 查询单办信息
			// 查询机票订单详情
			Map<String, Object> orderDetail = changePriceReviewService
					.queryAirticketorderDeatail(orderId, prdType);
//			ActivityAirTicket airTicket = activityAirTicketDao.findOne(Long.parseLong(orderDetail.get("proId").toString()));
			// 处理多币种信息 start
			String totalMoney = orderDetail.get("totalmoney") == null ? null
					: orderDetail.get("totalmoney").toString();
			totalMoney = moneyAmountService.getMoney(totalMoney);
			orderDetail.remove("totalmoney");
			orderDetail.put("totalmoney", totalMoney);
			// 处理多币种信息 end
			model.addAttribute("orderDetail", orderDetail);
			model.addAttribute("groupCode", orderDetail.get("proGroupCode"));  // 使用产品的团号
		} else if ("6".equals(prdType.trim())) {// 6代表签证 查询签证信息
			Map<String, Object> orderDetail = changePriceReviewService
					.queryVisaorderDeatail(orderId);
			// 处理多币种信息 start
			String totalMoney = orderDetail.get("totalmoney") == null ? null
					: orderDetail.get("totalmoney").toString();
			totalMoney = moneyAmountService.getMoney(totalMoney);
			orderDetail.remove("totalmoney");
			orderDetail.put("totalmoney", totalMoney);
			// 产品相关信息
			VisaProducts visaProduct = visaProductsService
					.findByVisaProductsId(Long.parseLong(orderDetail.get(
							"visaproductid").toString()));
			Dict visaType = dictService.findByValueAndType(visaProduct
					.getVisaType().toString(), "new_visa_type");
			Country country = CountryUtils.getCountry(Long
					.parseLong(visaProduct.getSysCountryId().toString()));
			model.addAttribute("visaProduct", visaProduct);
			model.addAttribute("visaType", visaType);
			model.addAttribute("country", country);
			// 处理多币种 end
			model.addAttribute("orderDetail", orderDetail);
			model.addAttribute("groupCode", visaProduct.getGroupCode());  // 使用产品的团号
		} else if ("2".equals(prdType.trim())) {// 2代表 散拼 10 游轮
			Map<String, Object> orderDetail = changePriceReviewService
					.querySanPinReviewOrderDetail(orderId);
			// 处理多币种信息 start
			String totalMoney = orderDetail.get("totalmoney") == null ? null
					: orderDetail.get("totalmoney").toString();
			totalMoney = moneyAmountService.getMoney(totalMoney);
			orderDetail.remove("totalmoney");
			orderDetail.put("totalmoney", totalMoney);
			// 处理多币种信息 end
			// 处理目的地信息 start
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> targetArea = orderDetail
					.get("targetAreas") == null ? null
					: (List<Map<String, Object>>) orderDetail
							.get("targetAreas");
			if (targetArea != null && targetArea.size() != 0) {
				String areaString = "";
				int tempN = 0;
				for (Map<String, Object> tempS : targetArea) {
					if (tempN != 0) {
						areaString += ",";
					}
					areaString += AreaUtil.findAreaNameById(Long
							.parseLong(tempS.get("targetAreaId").toString()));
					tempN++;
				}
				orderDetail.remove("targetarea");
				orderDetail.put("targetarea", areaString);
			}
			// 处理目的地信息 end
			model.addAttribute("orderDetail", orderDetail);
			model.addAttribute("groupCode", orderDetail.get("groupno"));  // 使用产品的团号
		} else {// 查询参团信息 1、3、4、5
			Map<String, Object> grouporderDeatail = changePriceReviewService
					.queryGrouporderDeatail(orderId);
			// 处理多币种信息
			String totalMoney = grouporderDeatail.get("totalmoney") == null ? null
					: grouporderDeatail.get("totalmoney").toString();
			totalMoney = moneyAmountService.getMoney(totalMoney);
			grouporderDeatail.remove("totalmoney");
			grouporderDeatail.put("totalmoney", totalMoney);
			// 处理targetArea 目标城市 是数组
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> targetArea = grouporderDeatail
					.get("targetAreas") == null ? null
					: (List<Map<String, Object>>) grouporderDeatail
							.get("targetAreas");
			if (targetArea != null && targetArea.size() != 0) {
				String areaString = "";
				int tempN = 0;
				for (Map<String, Object> tempS : targetArea) {
					if (tempN != 0) {
						areaString += ",";
					}
					areaString += AreaUtil.findAreaNameById(Long
							.parseLong(tempS.get("targetAreaId").toString()));
					tempN++;
				}
				grouporderDeatail.remove("targetarea");
				grouporderDeatail.put("targetarea", areaString);
			}
			// 处理targetArea end
			model.addAttribute("orderDetail", grouporderDeatail);
			model.addAttribute("groupCode", grouporderDeatail.get("groupno"));  // 使用产品的团号
		}
		// 查询订单联系人信息
		List<OrderContacts> orderContacts = orderContactsDao
				.findOrderContactsByOrderIdAndOrderType(
						Long.parseLong(orderId),
						Integer.parseInt(prdType.trim()));
		model.addAttribute("orderContacts", orderContacts);
		// 查询改价信息
		Map<String, Object> review = reviewService
				.getReviewDetailMapByReviewId(reviewId);
		Object object = review.get("travellerId");
		String originalTotalMoneyString = "0.00";
		if(object != null && !"".equals(object.toString().trim())){
			Traveler traveler = travelerService.findTravelerById(Long.parseLong(object.toString()));
			if(traveler != null && traveler.getOriginalPayPriceSerialNum()!= null && !"".equals(traveler.getOriginalPayPriceSerialNum())){
				originalTotalMoneyString = moneyAmountService.getMoney(traveler.getOriginalPayPriceSerialNum());
			}
		}
		review.put("originalTravelerMoney", originalTotalMoneyString);
		Map<String, Object> reviewDetailMap = changePriceService.getReviewDetail(reviewId);
		if(null != reviewDetailMap) {
			Traveler traveller = travelerService.findTravelerById(Long.parseLong(reviewDetailMap.get("travellerId").toString()));
			//改前金额
			List<MoneyAmount> originalMoneyList = moneyAmountService.findAmountBySerialNum(traveller.getOriginalPayPriceSerialNum());
			reviewDetailMap.put("originalMoneyList", originalMoneyList);

			//改后金额
			if ("2".equals(prdType.trim())) {
				ProductOrderCommon productOrder = orderDao.findOne(Long.parseLong(orderId));
				changePriceService.handlerMap(reviewDetailMap, productOrder);
			} else {
				handlerMap(reviewDetailMap);
			}
			model.addAttribute("changePrice", reviewDetailMap);
		}
		model.addAttribute("productType", prdType);
		model.addAttribute("flag", request.getParameter("flag"));
		model.addAttribute("reviewdetail", review);
		model.addAttribute("nowlevel", nowlevel);
		model.addAttribute("from_Areas", areaService.findFromCityList(""));// 出发城市
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
		model.addAttribute("rid", reviewId);
		return "review/changePrice/common/reviewChangePriceDetail";
	}

	/**
	 * 改价审核
	 */
	@ResponseBody
	@RequestMapping(value = "/changepricenewreview")
	@Transactional
	public Map<String, Object> changePriceReview(Model model,
			HttpServletRequest request, HttpServletResponse response) {

		Map<String, Object> result = new HashMap<String, Object>();
		// 1 组织参数
		String revId = request.getParameter("revId");// 审核表id
		String strResult = request.getParameter("result");// 审批结果 1 通过 0 驳回
		String denyReason = request.getParameter("denyReason");// 驳回原因
		String amount = request.getParameter("moneyAmount");// 改价数额
		String orderType = request.getParameter("orderTypeSub");// 产品类型
		String orderId = request.getParameter("orderId");// 订单id
		String currencyId = request.getParameter("currencyId");// 币种ids
		String travelerId = request.getParameter("travelerId");// 游客id
		// 2 调用审核接口处理
		ReviewResult reviewResult = new ReviewResult();
		String userId = UserUtils.getUser().getId().toString();
		String companyId = UserUtils.getUser().getCompany().getUuid();
		if (strResult.equals(ReviewConstant.REVIEW_OPERATION_PASS.toString())) {
			reviewResult = reviewService.approve(userId, companyId, null, userReviewPermissionChecker,
					revId, denyReason, null);
		} else {
			reviewResult = reviewService.reject(userId, companyId, null, revId,
					denyReason, null);
		}
		costchangeService.updateStatusByReviewUuid(reviewResult.getReviewStatus(), revId);
		if (!reviewResult.getSuccess()) {
			result.put("flag", "fail");
			result.put("err", reviewResult.getMessage());
		}
		// 3如果审核通过并且当前层级为最高层级 则更改对应业务数据
		if (ReviewConstant.REVIEW_STATUS_PASSED == reviewResult
				.getReviewStatus()) {
			Map<String, String> params = new HashMap<String, String>();
			params.put("travelerId", travelerId);
			params.put("orderId", orderId);
			params.put("orderType", orderType);
//			params.put("currencyId", currencyId);
//			params.put("amount", amount);
			if (currencyId != null) {
				String[] currencyIds = currencyId.split(",");
				String[] amounts = amount.split(",");
				for (int i = 0; i < currencyIds.length; i++) {
					currencyId = currencyIds[i];
					amount = amounts[i];
					params.put("currencyId", currencyId);
					params.put("amount", amount);
					if (!changePriceReviewService.doChangePrice(params)) {
						result.put("flag", "fail");
						result.put("err", "改价失败");
					}
				}
			}

		}
		result.put("flag", "success");
		return result;
	}

	/**
	 * 批量改价审核
	 */
	@ResponseBody
	@RequestMapping(value = "/batchchangepricenewreview")
	public Map<String, Object> batchChangePriceReview(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 1 组织参数
		String revIds = request.getParameter("revIds");// 审核表ids
		String remark = request.getParameter("remark");// 通过/驳回原因
		String strResult = request.getParameter("result");// 通过/驳回
		String currencyIdString = request.getParameter("currencyIdString");
		String amountString = request.getParameter("amountString");
		if (Context.NumberDef.NUMER_TWO.toString().equals(strResult)) {
			strResult = ReviewConstant.REVIEW_OPERATION_PASS.toString();
		} else {
			strResult = ReviewConstant.REVIEW_OPERATION_REFUSE.toString();
		}
		Map<String, Object> review = null;
		String[] revidArr = revIds.split(",");
		String[] currencyIdArr = currencyIdString.split(",");
		String[] amountArr = amountString.split(",");
//		for (String revid : revidArr) {
		for(int i = 0; i < revidArr.length; i++) {
			String revid = revidArr[i];
			if (revid == null || "".equals(revid)) {
				map.put("flag", "fail");
				map.put("msg", "错误的参数,审核ID为空");
				continue;
			}
			// 2 调用审核接口处理
			ReviewResult reviewResult = new ReviewResult();
			String userId = UserUtils.getUser().getId().toString();
			String companyId = UserUtils.getUser().getCompany().getUuid();
			if (ReviewConstant.REVIEW_OPERATION_PASS.toString().equals(strResult)) {
				reviewResult = reviewService.approve(userId, companyId, null,
						userReviewPermissionChecker, revid, remark, null);
			} else {
				reviewResult = reviewService.reject(userId, companyId, null,
						revid, remark, null);
			}
			costchangeService.updateStatusByReviewUuid(reviewResult.getReviewStatus(), revid);
			review = reviewService.getReviewDetailMapByReviewId(revid);
			// 3如果审核通过并且当前层级为最高层级 则更改对应业务数据
			if (ReviewConstant.REVIEW_STATUS_PASSED == reviewResult
					.getReviewStatus()) {
				Map<String, String> params = new HashMap<String, String>();
				params.put("travelerId", review.get("travellerId") == null ? ""
						: review.get("travellerId").toString());
				params.put("orderId", review.get("orderId") == null ? ""
						: review.get("orderId").toString());
				params.put("orderType", review.get("productType") == null ? ""
						: review.get("productType").toString());
//				params.put("currencyId", review.get("currencyid") == null ? ""
//						: review.get("currencyid").toString());
//				params.put("amount", review.get("changedprice") == null ? ""
//						: review.get("changedprice").toString());
				String[] currencyIds = currencyIdArr[i].split("/");
				String[] amounts = amountArr[i].split("/");
				for(int j = 0; j < currencyIds.length; j++) {
					params.put("currencyId", currencyIds[j]);
					params.put("amount", amounts[j]);
					boolean back = changePriceReviewService.doChangePrice(params);
					if (!back) {
						map.put("flag", "fail"); // 改价失败
						map.put("msg", "改价失败，请稍候再试");
						return map;
					}
				}
			}
			review = null;
		}
		map.put("flag", "success");
		return map;
	}

	/**
	 * 审核撤销
	 */
	@ResponseBody
	@RequestMapping(value = "/backchangepricenewreview/{reviewId}")
	public Map<String, Object> backChangePriceReview(
			@PathVariable String reviewId, HttpServletRequest request,
			HttpServletResponse response) {

		/* 声明返回对象 */
		Map<String, Object> result = new HashMap<String, Object>();
		String companyId = UserUtils.getUser().getCompany().getUuid();
		/* 调用审核接口 */
		ReviewResult reviewResult = reviewService.back(UserUtils.getUser()
				.getId().toString(), companyId, null, reviewId, null, null);
		if (reviewResult.getSuccess()) {
			/* 撤销成功 组织数据返回 */
			result.put("flag", "success");
			return result;
		}
		/* 失败 组织数据返回 */
		result.put("flag", "error");
		result.put("msg", reviewResult.getMessage());
		return result;
	}
	private void handlerMap(Map<String, Object> map) {
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
			oneMap.put("key", entry.getKey());
			oneMap.put("value", entry.getValue());
			oneMap.put("initValue", entry.getValue());
			moneyList.add(oneMap);
		}
		map.put("moneyList", moneyList);
	}
}
