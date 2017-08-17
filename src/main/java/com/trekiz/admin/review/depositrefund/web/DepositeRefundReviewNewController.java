package com.trekiz.admin.review.depositrefund.web;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaService;
import com.trekiz.admin.review.depositrefund.service.IDepositeRefundReviewNewService;

/**
 * 退款审批列表页
 * 
 * @author chy
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/depositenew")
public class DepositeRefundReviewNewController {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory
			.getLog(DepositeRefundReviewNewController.class);

	@Autowired
	private IDepositeRefundReviewNewService depositeRefundReviewService;

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Autowired
	private VisaService visaService;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
    private VisaProductsService visaProductsService;
	
	@Autowired
	private DictService dictService;
	
	@Autowired
	private UserReviewPermissionChecker userReviewPermissionChecker;
	/**
	 * 查询退签证押金审批列表
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "depositeRefundReviewList")
	public String queryRefundReviewList(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> params = prepareParams(request, response);
		Page<Map<String, Object>> page = depositeRefundReviewService.queryRefundReviewList(params);
		model.addAttribute("page", page);
		model.addAttribute("conditionsMap", params);
		return "review/depositereview/depositeRefundReviewNewList";
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
		String groupCode = request.getParameter("groupCode") == null ? null : request.getParameter("groupCode").toString();
		//渠道商(id)
		String agentId = request.getParameter("agentId") == null ? null : request.getParameter("agentId").toString();
		//申请日期（from）
		String applyDateFrom = request.getParameter("applyDateFrom") == null ? null : request.getParameter("applyDateFrom").toString();
		//申请日期（to）
		String applyDateTo = request.getParameter("applyDateTo") == null ? null : request.getParameter("applyDateTo").toString();
		//审批发起人(id)
		String applyPerson = request.getParameter("applyPerson") == null ? null : request.getParameter("applyPerson").toString();
		//游客
		String travelerName = request.getParameter("traveler") == null ? null : request.getParameter("traveler").toString();
		//审批状态
		String reviewStatus = request.getParameter("reviewStatus") == null ? null : request.getParameter("reviewStatus").toString();
		//页签选择状态
		String tabStatus = request.getParameter("tabStatus") == null ? null : request.getParameter("tabStatus").toString();
		if(tabStatus == null || "".equals(tabStatus)){//默认为待本人审核
			tabStatus = Context.NumberDef.NUMER_ONE.toString();
		}
		// 创建日期排序标识
		String orderCreateDateSort  = request.getParameter("orderCreateDateSort");
		// 更新日期排序标识
		String orderUpdateDateSort = request.getParameter("orderUpdateDateSort");
		//订单创建日期排序标识
		String orderCreateDateCss  = request.getParameter("orderCreateDateCss");
		//订单更新日期排序标识
		String orderUpdateDateCss = request.getParameter("orderUpdateDateCss");
		if((orderCreateDateSort == null || "".equals(orderCreateDateSort)) && (orderUpdateDateSort == null || "".equals(orderUpdateDateSort))){
			orderCreateDateSort = "desc";
			orderCreateDateCss = "activitylist_paixu_moren";
		}
		//page对象
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(request, response);
		/**获取参数 end*/
		/**组装参数 start*/
		result.put("groupCode", groupCode);
//		result.put("productType", productType);
		result.put("agentId", agentId);
		result.put("applyDateFrom", applyDateFrom);
		result.put("applyDateTo", applyDateTo);
		result.put("applyPerson", applyPerson);
		result.put("traveler", travelerName);
		result.put("reviewStatus", reviewStatus);
		if("".equals(tabStatus)){
			tabStatus = "1";//默认带本人审核
		}
		result.put("tabStatus", tabStatus);
		result.put("orderCreateDateSort", orderCreateDateSort);
		result.put("orderUpdateDateSort", orderUpdateDateSort);
		result.put("orderCreateDateCss", orderCreateDateCss);
		result.put("orderUpdateDateCss", orderUpdateDateCss);
		result.put("pageP", page);
		/**组装参数 end*/
		return result;
	}

	/**
	 * 出纳确认
	 */
	@RequestMapping(value = "cashConfirm")
	@ResponseBody
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public Map<String, Object> cashConfirm(Model model, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> res = new HashMap<String, Object>();
		String travelerId = request.getParameter("traid");
		depositeRefundReviewService.cashConfirm(travelerId);
		res.put("flag", "success");
		return res;
	}

	/**
	 * 进入退签证押金审批详情页
	 * 签证押金是和游客关联的 一个游客一个签证 一个押金 
	 */
	@RequestMapping(value = "depositeRefundReviewDetail")
	public String queryRefundReviewDetail(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderid");// 订单id
		String reviewId = request.getParameter("revid");// 审核表id
		// 查询审批详情信息
		Map<String, Object> orderDetail = depositeRefundReviewService.queryVisaorderDeatail(orderId);
		// 处理多币种信息 start
		String totalMoney = orderDetail.get("totalmoney") == null ? null : orderDetail.get("totalmoney").toString();
		totalMoney = moneyAmountService.getMoney(totalMoney);
		orderDetail.remove("totalmoney");
		orderDetail.put("totalmoney", totalMoney);
		
		//产品相关信息
		VisaProducts visaProduct = visaProductsService.findByVisaProductsId(Long.parseLong(orderDetail.get("visaproductid").toString()));
		Dict visaType = dictService.findByValueAndType(visaProduct.getVisaType().toString(), "new_visa_type");
		Country country = CountryUtils.getCountry(Long.parseLong(visaProduct.getSysCountryId().toString()));
		model.addAttribute("visaProduct", visaProduct);
		model.addAttribute("visaType", visaType);
		model.addAttribute("country", country);
		
		//处理多币种 end
		model.addAttribute("orderDetail", orderDetail);
		// 查询退款信息
		Map<String, Object> review = reviewService.getReviewDetailMapByReviewId(reviewId);
		model.addAttribute("flag", request.getParameter("flag"));
		//把币种id转为币种名称
		Currency currency = currencyService.findCurrency(Long.parseLong(review.get("depositPriceCurrency").toString()));
		review.put("currencyName", currency.getCurrencyName());
		model.addAttribute("reviewdetail", review);
//		model.addAttribute("nowlevel", nowlevel);
		model.addAttribute("rid",reviewId);
		return "modules/depositereview/depositeRefundReviewDetail";
	}
	
	/**
	 * 退签证押金审核的审核通过或驳回
	 */
	@ResponseBody
	@RequestMapping(value = "depositeReview")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public Map<String, Object> refundReview(Model model, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>(); 
		// 1 组织参数
		String revId = request.getParameter("revId");//审核表id
		String strResult = request.getParameter("result");//审批结果 1 通过 0 驳回
		String denyReason = request.getParameter("denyReason");//驳回原因
		String amount = request.getParameter("moneyAmount");//退款数额
		String orderType = request.getParameter("orderTypeSub");//产品类型
//		String orderId = request.getParameter("orderId");//订单id
		String currencyId = request.getParameter("currencyId");//币种id
		String travelerId = request.getParameter("travelerId");//游客id
		// 2 调用审核接口处理
		ReviewResult reviewResult = null;
		String companyId = UserUtils.getUser().getCompany().getUuid();
		if(Context.REVIEW_ACTION_PASS.equals(strResult)){//通过
			reviewResult = reviewService.approve(UserUtils.getUser().getId().toString(), companyId, null, userReviewPermissionChecker, revId, denyReason, null);
			} else {//驳回
				reviewResult = reviewService.reject(UserUtils.getUser().getId().toString(), companyId, null, revId, denyReason, null);
			}
		if(!reviewResult.getSuccess()){
			result.put("flag", "fail");
			result.put("msg", reviewResult.getMessage());
			return result;
		}
		// 3如果审核通过并且当前层级为最高层级 则直接操作退签证押金 往moneyamount表存储一条款项数据  并更新对应visa表记录的已退签证押金
		if(ReviewConstant.REVIEW_STATUS_PASSED == reviewResult.getReviewStatus()){
			MoneyAmount ma = new MoneyAmount();
			String uuid = UUID.randomUUID().toString();
			ma.setSerialNum(uuid);
			if(amount != null && !"".equals(amount)){
				ma.setAmount(BigDecimal.valueOf(Double.valueOf(amount)));//款数
			} else {
				ma.setAmount(BigDecimal.valueOf(0));//款数
			}
			ma.setOrderType(Integer.parseInt(orderType));//订单类型 即 产品类型
			ma.setMoneyType(Context.MONEY_TYPE_TYJ);//款项类型退签证押金 是15
			ma.setUid(Long.parseLong(travelerId));//游客id
			ma.setCurrencyId(Integer.parseInt(currencyId));//币种
			ma.setReviewUuid(revId);
			ma.setBusindessType(2);//2标示游客退款
			//moneyamount表存储一条数据
			moneyAmountService.saveOrUpdateMoneyAmount(ma);
			//visa表的已退押金字段
			Visa visa = visaService.findVisaByTravlerId(Long.parseLong(travelerId));
			visa.setReturnedDeposit(uuid);
			visaService.saveVisa(visa);
			String payedUUID = visa.getPayedDeposit();//已收押金UUID
			String accountedUUID = visa.getAccountedDeposit();//达账押金UUID
			List<MoneyAmount> payedMoneys = moneyAmountService.findAmountBySerialNum(payedUUID);
			List<MoneyAmount> accountedMoneys = moneyAmountService.findAmountBySerialNum(accountedUUID);
			for(MoneyAmount temp : payedMoneys){//更改已付押金
				if(temp.getCurrencyId() == Integer.parseInt(currencyId)){
					temp.setAmount(temp.getAmount().subtract(BigDecimal.valueOf(Double.valueOf(amount))));
					moneyAmountService.saveOrUpdateMoneyAmount(temp);
					break;
				}
			}
			for(MoneyAmount temp : accountedMoneys){//更改达账押金
				if(temp.getCurrencyId() == Integer.parseInt(currencyId)){
					temp.setAmount(temp.getAmount().subtract(BigDecimal.valueOf(Double.valueOf(amount))));
					moneyAmountService.saveOrUpdateMoneyAmount(temp);
					break;
				}
			}
		}
		result.put("flag", "success");
		return result;
	}
	
	/**
	 * 退款审核的审核批量通过或驳回
	 */
	@ResponseBody
	@RequestMapping(value = "batchdepositeReview")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public Map<String, Object> batchRefundReview(Model model, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> result = new HashMap<String, Object>();
		// 1 组织参数
		String revIds = request.getParameter("revIds");//审核表ids
		String remark = request.getParameter("remark");//通过/驳回原因
		String strResult = request.getParameter("result");//通过/驳回原因
		Integer reviewOperation = 0;
		if(Context.NumberDef.NUMER_TWO.toString().equals(strResult)){
			reviewOperation = ReviewConstant.REVIEW_OPERATION_PASS;
		}
		StringBuffer reply = new StringBuffer();
		int n = Context.NumberDef.NUMER_ZERO;
		String[] revidArr = revIds.split(",");
		int revNum = revidArr.length;
		for(String revid : revidArr){
			if(revid == null || "".equals(revid)){
				reply.append("错误的参数reviewid不能为空");
				n++;
				continue;
			}
			Map<String, Object> review = reviewService.getReviewDetailMapByReviewId(revid);
			ReviewResult reviewResult;
			String companyId = UserUtils.getUser().getCompany().getUuid();
			// 2 调用审核接口处理
			if(ReviewConstant.REVIEW_OPERATION_PASS == reviewOperation){
				reviewResult = reviewService.approve(UserUtils.getUser().getId().toString(), companyId, null, userReviewPermissionChecker, revid, remark, null);
			} else {
				reviewResult = reviewService.reject(UserUtils.getUser().getId().toString(), companyId, null, revid, remark, null);
			}
			// 3如果审核通过并且当前层级为最高层级 则直接操作退签证押金 往moneyamount表存储一条款项数据  并更新对应visa表记录的已退签证押金
			if(ReviewConstant.REVIEW_STATUS_PASSED == reviewResult.getReviewStatus()){
				MoneyAmount ma = new MoneyAmount();
				String uuid = UUID.randomUUID().toString();
				ma.setSerialNum(uuid);
				if(review.get("refundPrice") != null && !"".equals(review.get("refundPrice"))){
					ma.setAmount(BigDecimal.valueOf(Double.valueOf(review.get("refundPrice").toString())));//款数
				} else {
					ma.setAmount(BigDecimal.valueOf(0));//款数
				}
				ma.setOrderType(Integer.parseInt(review.get("productType").toString()));//订单类型 即 产品类型
				ma.setMoneyType(Context.MONEY_TYPE_TYJ);//款项类型退签证押金 是15
				ma.setUid(Long.parseLong(review.get("travelerId").toString()));//游客id
				ma.setCurrencyId(Integer.parseInt(review.get("depositPriceCurrency").toString()));//币种
				ma.setBusindessType(2);//2标示游客退款
				ma.setReviewUuid(revid);
				//moneyamount表存储一条数据
				moneyAmountService.saveOrUpdateMoneyAmount(ma);
				//visa表的已退押金字段
				Visa visa = visaService.findVisaByTravlerId(Long.parseLong(review.get("travelerId").toString()));
				visa.setReturnedDeposit(uuid);
				visaService.saveVisa(visa);
				String payedUUID = visa.getPayedDeposit();//已收押金UUID
				String accountedUUID = visa.getAccountedDeposit();//达账押金UUID
				List<MoneyAmount> payedMoneys = moneyAmountService.findAmountBySerialNum(payedUUID);
				List<MoneyAmount> accountedMoneys = moneyAmountService.findAmountBySerialNum(accountedUUID);
				for(MoneyAmount temp : payedMoneys){//更改已付押金
					if(temp.getCurrencyId() == Integer.parseInt(review.get("depositPriceCurrency").toString())){
						temp.setAmount(temp.getAmount().subtract(BigDecimal.valueOf(Double.valueOf(review.get("refundPrice").toString()))));
						moneyAmountService.saveOrUpdateMoneyAmount(temp);
						break;
					}
				}
				for(MoneyAmount temp : accountedMoneys){//更改达账押金
					if(temp.getCurrencyId() == Integer.parseInt(review.get("depositPriceCurrency").toString())){
						temp.setAmount(temp.getAmount().subtract(BigDecimal.valueOf(Double.valueOf(review.get("refundPrice").toString()))));
						moneyAmountService.saveOrUpdateMoneyAmount(temp);
						break;
					}
				}
			}
		}
		if(!StringUtils.isBlank(reply)){
			result.put("flag", "fail");
			result.put("msg", "成功" + (revNum - n) + "个，失败" + n + "个:" + reply);
			return result;
		}
		result.put("flag", "success");
		return result;
	}

	/**
	 * 审核撤销方法
	 */
	@ResponseBody
	@RequestMapping(value="backrefundreview/{reviewId}")
	public Map<String, Object> backRefundReview(@PathVariable String reviewId, HttpServletRequest request, HttpServletResponse response){
		
		/*声明返回对象*/
		Map<String, Object> result = new HashMap<String, Object>();
		String companyId = UserUtils.getUser().getCompany().getUuid();
		/*调用审核接口*/
		ReviewResult reviewResult = reviewService.back(UserUtils.getUser().getId().toString(), companyId, null, reviewId, null, null);
		if(reviewResult.getSuccess()){
			/*撤销成功 组织数据返回*/
			result.put("flag", "success");
			return result;
		}
		/*失败 组织数据返回*/
		result.put("flag", "error");
		result.put("msg", reviewResult.getMessage());
		return result;
	}

}
