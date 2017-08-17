package com.trekiz.admin.modules.review.depositereview.web;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.review.depositereview.service.IDepositeRefundReviewService;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaDao;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;

/**
 * 退款审批列表页
 * 
 * @author chy
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/deposite")
public class DepositeRefundReviewController {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory
			.getLog(DepositeRefundReviewController.class);

	@Autowired
	private IDepositeRefundReviewService depositeRefundReviewService;

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Autowired
	private MoneyAmountDao moneyAmountDao;

	@Autowired
	private ReviewCommonService reviewCommonService;
	
	@Autowired
	private VisaDao visaDao;
	
	@Autowired
	private CurrencyDao currencyDao;
	
	@Autowired
    private VisaProductsService visaProductsService;
	
	@Autowired
	private DictService dictService;
	@Autowired
	private VisaOrderService visaOrderService;
	
	/**
	 * 财务审核 -- 退签证押金审批列表
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @author shijun.liu
	 */
	@RequestMapping(value = "costDepositeRefundReviewList")
	public String costDepositeRefundReviewList(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		// 获取查询参数
		Map<String, String> conditionsMap = optionParams(request);
		Page<Map<String, Object>> refundReviewList = depositeRefundReviewService
				.queryCostRefundReviewList(conditionsMap, new Page<Map<String, Object>>(request, response));
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_DEPOSITE_REFUND);
		String userJobId = conditionsMap.get("userJobId");
		if(userJobId == null && userJobs.size() > 0){//如果userJobs为空的  则说明前台没有传递这个参数 取所有userJobs的第一个的Id
			conditionsMap.put("userJobId", String.valueOf(userJobs.get(userJobs.size()-1).getId()));
		}
	    Collections.sort(userJobs, new Comparator<UserJob>() {  
            public int compare(UserJob arg0, UserJob arg1) {  
                long hits0 = arg0.getId();  
                long hits1 = arg1.getId();  
                if (hits1 > hits0) {  
                    return 1;  
                } else if (hits1 == hits0) {  
                    return 0;  
                } else {  
                    return -1;  
                }  
            }  
        });
	    model.addAttribute("userJobs", userJobs);//当前用户的职位
		model.addAttribute("page", refundReviewList);
		model.addAttribute("conditionsMap", conditionsMap);

		model.addAttribute("orderPersonList", UserUtils.getSalers(UserUtils.getUser().getCompany().getId()));
		List<Map<String, Object>> createByNameList = visaOrderService.findVisaOrderCreateBy1();
		model.addAttribute("createByList", createByNameList);
		return "modules/cost/depositeRefundReviewList";
	}

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
		Page<Map<String, Object>> refundReviewList = depositeRefundReviewService
				.queryRefundReviewList(request, response);
		List<UserJob> userJobs = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_DEPOSITE_REFUND);
		// 获取查询参数
		Map<String, Object> conditionsMap = prepareQueryCond(request);
		String userJobId = request.getParameter("userJobId");
		if((userJobId == null || "".equals(userJobId)) && userJobs.size() > 0){//如果userJobs为空的  则说明前台没有传递这个参数 取所有userJobs的第一个的Id
			UserJob userJob = userJobs.get(userJobs.size()-1);
			conditionsMap.put("userJobId", userJob.getId());
			List<Integer> jobs = reviewService.getJobLevel(userJob.getDeptId(),userJob.getJobId(),
					Context.REVIEW_FLOWTYPE_DEPOSITE_REFUND);
			if(jobs != null && jobs.size() > 0){
				conditionsMap.put("myCheckLevel", jobs.get(0));
			}
		}else{
			for (UserJob userJob:userJobs) {
				if(userJob.getId().intValue() == 
						Long.valueOf(String.valueOf(conditionsMap.get("userJobId"))).intValue()){
					List<Integer> jobs = reviewService.getJobLevel(userJob.getDeptId(),userJob.getJobId(),
							Context.REVIEW_FLOWTYPE_DEPOSITE_REFUND);
					if(jobs != null && jobs.size() > 0){
						conditionsMap.put("myCheckLevel", jobs.get(0));
					}
					break;
				}
			}
		}
	    Collections.sort(userJobs, new Comparator<UserJob>() {  
            public int compare(UserJob arg0, UserJob arg1) {  
                long hits0 = arg0.getId();  
                long hits1 = arg1.getId();  
                if (hits1 > hits0) {  
                    return 1;  
                } else if (hits1 == hits0) {  
                    return 0;  
                } else {  
                    return -1;  
                }  
            }  
        });
	    model.addAttribute("userJobs", userJobs);//当前用户的职位
		model.addAttribute("page", refundReviewList);
		model.addAttribute("conditionsMap", conditionsMap);
		model.addAttribute("orderPersonList", UserUtils.getSalers(UserUtils.getUser().getCompany().getId()));
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		return "modules/depositereview/depositeRefundReviewList";
	}

	/**
	 * 处理请求参数
	 * @param request
	 * @return
	 * @author shijun.liu
	 */
	private Map<String, String> optionParams(HttpServletRequest request) {
		Map<String, String> conditionsMap = new HashMap<String, String>();
		conditionsMap.put("groupCode", request.getParameter("groupCode"));//团号
		conditionsMap.put("orderNum", request.getParameter("orderNum"));  //订单编号
		String statusChoose = request.getParameter("statusChoose");
		if(null == statusChoose){
			statusChoose = "1";
		}
		conditionsMap.put("statusChoose", statusChoose);
		conditionsMap.put("startTime", request.getParameter("startTime"));//报批日期开始
		conditionsMap.put("endTime", request.getParameter("endTime"));    //报批日期结束
		conditionsMap.put("moneyBegin", request.getParameter("moneyBegin"));//押金金额开始
		conditionsMap.put("moneyEnd", request.getParameter("moneyEnd"));    //押金金额结束
		conditionsMap.put("channel", request.getParameter("channel"));	    //渠道
		conditionsMap.put("salerId", request.getParameter("salerId"));      //销售
		conditionsMap.put("operatorId", request.getParameter("operatorId"));//计调
		conditionsMap.put("travelerName", request.getParameter("travelerName"));// 游客
		conditionsMap.put("orderBy", request.getParameter("orderBy"));      //排序
		conditionsMap.put("userJobId", request.getParameter("userJobId"));  //角色ID
		conditionsMap.put("createDate", request.getParameter("createDate"));// 创建日期排序标识
		conditionsMap.put("updateDate", request.getParameter("updateDate"));// 更新日期排序标识
		conditionsMap.put("orderPersonId", request.getParameter("orderPersonId"));// 下单人
		conditionsMap.put("create_by", request.getParameter("create_by"));// 更新日期排序标识
		return conditionsMap;
	}
	
	private Map<String, Object> prepareQueryCond(HttpServletRequest request) {
		Map<String, Object> conditionsMap = new HashMap<String, Object>();
//		conditionsMap.put("orderType", request.getParameter("orderType"));
		conditionsMap.put("groupCode", request.getParameter("groupCode"));
		String statusChoose = request.getParameter("statusChoose");
		if(statusChoose == null){
			statusChoose = "1";
		}
		conditionsMap.put("statusChoose", statusChoose);
//		conditionsMap.put("statusChoose", request.getParameter("statusChoose"));
		// conditionsMap.put("flowType", request.getParameter("flowType"));
		conditionsMap.put("channel", request.getParameter("channel") == null || "".equals(request.getParameter("channel").trim()) ? null
				: Integer.parseInt(request.getParameter("channel")));
		String saler = request.getParameter("saler");
		conditionsMap.put("saler", saler == null || "".equals(saler.trim()) ? null : Integer.parseInt(saler));
		String meter = request.getParameter("meter");
		conditionsMap.put("meter", meter == null || "".equals(meter.trim()) ? null : Integer.parseInt(meter));
		// conditionsMap.put("active", request.getParameter("active"));
		conditionsMap.put("startTime", request.getParameter("startTime"));
		conditionsMap.put("endTime", request.getParameter("endTime"));
		conditionsMap.put("orderBy", request.getParameter("orderBy"));
		String userJobIdStr = request.getParameter("userJobId");
		Long userJobId = null;
		if(userJobIdStr != null && !"".equals(userJobIdStr)){
			userJobId = Long.parseLong(userJobIdStr);
		}
		conditionsMap.put("userJobId", userJobId);
		String orderCreateDateSort  = request.getParameter("orderCreateDateSort");// 创建日期排序标识
		String orderUpdateDateSort = request.getParameter("orderUpdateDateSort");// 更新日期排序标识
		String orderCreateDateCss  = request.getParameter("orderCreateDateCss");//订单创建日期排序标识
		String orderUpdateDateCss = request.getParameter("orderUpdateDateCss");//订单更新日期排序标识
		conditionsMap.put("orderCreateDateSort", orderCreateDateSort);
		conditionsMap.put("orderUpdateDateSort", orderUpdateDateSort);
		conditionsMap.put("orderCreateDateCss", orderCreateDateCss);
		conditionsMap.put("orderUpdateDateCss", orderUpdateDateCss);
		conditionsMap.put("orderPersonId", request.getParameter("orderPersonId"));// 下单人
		return conditionsMap;
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
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "costDepositeRefundReviewDetail")
	public String costDepositeRefundReviewDetail(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderid");// 订单id
		String reviewId = request.getParameter("revid");// 审核表id
		String nowlevel = request.getParameter("nowlevel");
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
		Map<String, String> review = reviewService.findReview(Long
				.parseLong(reviewId));
		model.addAttribute("flag", request.getParameter("flag"));
		//把币种id转为币种名称
		Currency currency = currencyDao.findOne(Long.parseLong(review.get("depositPriceCurrency")));
		review.put("currencyName", currency.getCurrencyName());
		model.addAttribute("reviewdetail", review);
		model.addAttribute("nowlevel", nowlevel);
		model.addAttribute("rid",reviewId);
		return "modules/cost/depositeRefundReviewDetail";
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
		String nowlevel = request.getParameter("nowlevel");
		// 查询审批详情信息
		Map<String, Object> orderDetail = depositeRefundReviewService.queryVisaorderDeatail(orderId);
		// 处理多币种信息 start
		String totalMoney = orderDetail.get("totalmoney") == null ? null : orderDetail.get("totalmoney").toString();
		totalMoney = moneyAmountService.getMoney(totalMoney);
		orderDetail.remove("totalmoney");
		orderDetail.put("totalmoney", totalMoney);
//		String visapay = orderDetail.get("visapay") == null ? null : orderDetail.get("visapay").toString();
//		visapay = moneyAmountService.getMoney(visapay);
//		orderDetail.remove("visapay");
//		orderDetail.put("visapay", visapay);
		
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
		Map<String, String> review = reviewService.findReview(Long
				.parseLong(reviewId));
		model.addAttribute("flag", request.getParameter("flag"));
		//把币种id转为币种名称
		Currency currency = currencyDao.findOne(Long.parseLong(review.get("depositPriceCurrency")));
		review.put("currencyName", currency.getCurrencyName());
		model.addAttribute("reviewdetail", review);
		model.addAttribute("nowlevel", nowlevel);
		model.addAttribute("rid",reviewId);
		return "modules/depositereview/depositeRefundReviewDetail";
	}
	
	/**
	 * 退款审核的审核通过或驳回
	 */
	@RequestMapping(value = "costDepositeReview")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public String costDepositeReview(Model model, HttpServletRequest request, HttpServletResponse response){
		// 1 组织参数
		String revId = request.getParameter("revId");//审核表id
		String curLevel = request.getParameter("nowlevel");//当前层级
		String strResult = request.getParameter("result");//审批结果 1 通过 0 驳回
		String denyReason = request.getParameter("denyReason");//驳回原因
		String amount = request.getParameter("moneyAmount");//退款数额
		String orderType = request.getParameter("orderTypeSub");//产品类型
		String currencyId = request.getParameter("currencyId");//币种id
		String travelerId = request.getParameter("travelerId");//游客id
		// 2 调用审核接口处理
		int lastLevelFlagNum = reviewService.UpdateReview(Long.parseLong(revId), Integer.parseInt(curLevel), Integer.parseInt(strResult), denyReason);
		// 3如果审核通过并且当前层级为最高层级 则直接操作退签证押金 往moneyamount表存储一条款项数据  并更新对应visa表记录的已退签证押金
		if(lastLevelFlagNum == 1){
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
			ma.setBusindessType(2);//2标示游客退款
			//moneyamount表存储一条数据
			moneyAmountService.saveOrUpdateMoneyAmount(ma);
			//visa表的已退押金字段
			Visa visa = visaDao.findByTravelerId(Long.parseLong(travelerId));
			visa.setReturnedDeposit(uuid);
			visaDao.save(visa);
			String payedUUID = visa.getPayedDeposit();//已收押金UUID
			String accountedUUID = visa.getAccountedDeposit();//达账押金UUID
			List<MoneyAmount> payedMoneys = moneyAmountDao.findAmountBySerialNum(payedUUID);
			List<MoneyAmount> accountedMoneys = moneyAmountDao.findAmountBySerialNum(accountedUUID);
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
		return costDepositeRefundReviewList(model, request, response);
	}
	
	/**
	 * 退款审核的审核通过或驳回
	 */
	@ResponseBody
	@RequestMapping(value = "depositeReview")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public String refundReview(Model model, HttpServletRequest request, HttpServletResponse response){
		// 1 组织参数
		String revId = request.getParameter("revId");//审核表id
		String curLevel = request.getParameter("nowlevel");//当前层级
		String strResult = request.getParameter("result");//审批结果 1 通过 0 驳回
		String denyReason = request.getParameter("denyReason");//驳回原因
		String amount = request.getParameter("moneyAmount");//退款数额
		String orderType = request.getParameter("orderTypeSub");//产品类型
//		String orderId = request.getParameter("orderId");//订单id
		String currencyId = request.getParameter("currencyId");//币种id
		String travelerId = request.getParameter("travelerId");//游客id
		// 2 调用审核接口处理
		int lastLevelFlagNum = reviewService.UpdateReview(Long.parseLong(revId), Integer.parseInt(curLevel), Integer.parseInt(strResult), denyReason);
		// 3如果审核通过并且当前层级为最高层级 则直接操作退签证押金 往moneyamount表存储一条款项数据  并更新对应visa表记录的已退签证押金
//		List<Review> list = reviewDao.findReviewActive(Long.parseLong(revId));
		// strResult 为1 代表审核通过
//		if(Integer.parseInt(strResult) == 1 && list.get(0).getTopLevel() == Integer.parseInt(curLevel)){
		if(lastLevelFlagNum == 1){
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
			ma.setReviewId(Long.parseLong(revId));
			ma.setBusindessType(2);//2标示游客退款
			//moneyamount表存储一条数据
			moneyAmountService.saveOrUpdateMoneyAmount(ma);
			//visa表的已退押金字段
			Visa visa = visaDao.findByTravelerId(Long.parseLong(travelerId));
			visa.setReturnedDeposit(uuid);
			visaDao.save(visa);
			String payedUUID = visa.getPayedDeposit();//已收押金UUID
			String accountedUUID = visa.getAccountedDeposit();//达账押金UUID
			List<MoneyAmount> payedMoneys = moneyAmountDao.findAmountBySerialNum(payedUUID);
			List<MoneyAmount> accountedMoneys = moneyAmountDao.findAmountBySerialNum(accountedUUID);
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
		return "success";
	}
	
	/**
	 * 退款审核的审核批量通过或驳回
	 */
	@ResponseBody
	@RequestMapping(value = "batchdepositeReview")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public String batchRefundReview(Model model, HttpServletRequest request, HttpServletResponse response){
		// 1 组织参数
		String revIds = request.getParameter("revIds");//审核表ids
		String remark = request.getParameter("remark");//通过/驳回原因
		String strResult = request.getParameter("result");//通过/驳回原因
		if("2".equals(strResult)){
			strResult = "1";
		}else {
			strResult = "0";
		}
		String[] revidArr = revIds.split(",");
		for(String revid : revidArr){
			if(revid == null || "".equals(revid)){
				System.err.println("错误的参数reviewid不能为空 airticketRefundReviewContriller line 718");
				continue;
			}
			Map<String, String> review = reviewService.findReview(Long.parseLong(revid));
			// 2 调用审核接口处理
			int lastLevelFlagNum = reviewService.UpdateReview(Long.parseLong(revid), Integer.parseInt(review.get("curLevel")), Integer.parseInt(strResult), remark);
			// 3如果审核通过并且当前层级为最高层级 则直接操作退签证押金 往moneyamount表存储一条款项数据  并更新对应visa表记录的已退签证押金
	//		List<Review> list = reviewDao.findReviewActive(Long.parseLong(revId));
			// strResult 为1 代表审核通过
	//		if(Integer.parseInt(strResult) == 1 && list.get(0).getTopLevel() == Integer.parseInt(curLevel)){
			if(lastLevelFlagNum == 1){
				MoneyAmount ma = new MoneyAmount();
				String uuid = UUID.randomUUID().toString();
				ma.setSerialNum(uuid);
				if(review.get("refundPrice") != null && !"".equals(review.get("refundPrice"))){
					ma.setAmount(BigDecimal.valueOf(Double.valueOf(review.get("refundPrice"))));//款数
				} else {
					ma.setAmount(BigDecimal.valueOf(0));//款数
				}
				ma.setOrderType(Integer.parseInt(review.get("productType")));//订单类型 即 产品类型
				ma.setMoneyType(Context.MONEY_TYPE_TYJ);//款项类型退签证押金 是15
				ma.setUid(Long.parseLong(review.get("travelerId")));//游客id
				ma.setCurrencyId(Integer.parseInt(review.get("depositPriceCurrency")));//币种
				ma.setBusindessType(2);//2标示游客退款
				ma.setReviewId(Long.parseLong(revid));
				//moneyamount表存储一条数据
				moneyAmountService.saveOrUpdateMoneyAmount(ma);
				//visa表的已退押金字段
				Visa visa = visaDao.findByTravelerId(Long.parseLong(review.get("travelerId")));
				visa.setReturnedDeposit(uuid);
				visaDao.save(visa);
				String payedUUID = visa.getPayedDeposit();//已收押金UUID
				String accountedUUID = visa.getAccountedDeposit();//达账押金UUID
				List<MoneyAmount> payedMoneys = moneyAmountDao.findAmountBySerialNum(payedUUID);
				List<MoneyAmount> accountedMoneys = moneyAmountDao.findAmountBySerialNum(accountedUUID);
				for(MoneyAmount temp : payedMoneys){//更改已付押金
					if(temp.getCurrencyId() == Integer.parseInt(review.get("depositPriceCurrency"))){
						temp.setAmount(temp.getAmount().subtract(BigDecimal.valueOf(Double.valueOf(review.get("refundPrice")))));
						moneyAmountService.saveOrUpdateMoneyAmount(temp);
						break;
					}
				}
				for(MoneyAmount temp : accountedMoneys){//更改达账押金
					if(temp.getCurrencyId() == Integer.parseInt(review.get("depositPriceCurrency"))){
						temp.setAmount(temp.getAmount().subtract(BigDecimal.valueOf(Double.valueOf(review.get("refundPrice")))));
						moneyAmountService.saveOrUpdateMoneyAmount(temp);
						break;
					}
				}
			}
		}
		return "success";
	}

	
	/**
	 * 退签证押金审核的撤销功能
	 * @param model
	 * @param request
	 * @param response
	 * @author shijun.liu
	 */
	@RequestMapping(value = "cancelDeposite")
	public void cancelDeposite(Model model, HttpServletRequest request, HttpServletResponse response){
		String json = "{\"flag\":\"success\"}";
		String revId = request.getParameter("reviewId");      //审核表id
		String curLevel = request.getParameter("myCheckLevel");//当前层级
		if(StringUtils.isNotBlank(curLevel) && StringUtils.isNotBlank(revId)){
			Integer currentLevel = Integer.parseInt(curLevel);
			Long reviewId = Long.valueOf(revId);
			if(currentLevel > 0){
				StringBuffer str = new StringBuffer();
				int index = reviewService.backOutReview(reviewId, currentLevel, "", str);
				if(index != 1 && str.toString().length() != 0){
					json = "{\"flag\":\"fail\",\"msg\":\""+str.toString()+"\"}";
				}
			}else{
				json = "{\"flag\":\"fail\",\"msg\":\"当前层级不能进行撤销操作\"}";
			}
		}else{
			json = "{\"flag\":\"fail\",\"msg\":\"审核ID或当前层级不能为空\"}";
		}
		ServletUtil.print(response, json);
	}	

}
