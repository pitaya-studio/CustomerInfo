package com.trekiz.admin.review.warrantForm;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.common.collect.Maps;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.engine.entity.ReviewLogNew;
import com.quauq.review.core.support.ReviewResult;
import com.quauq.review.core.type.OrderByDirectionType;
import com.quauq.review.core.type.OrderByPropertiesType;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaService;
import com.trekiz.admin.review.money.entity.NewProcessMoneyAmount;
import com.trekiz.admin.review.money.service.NewProcessMoneyAmountService;
@Controller
@RequestMapping(value = "${adminPath}/order/manager/visaNew")
public class DepositToWarrantControllerNew extends BaseController{
	@Autowired
	ReviewService processReviewService;
	@Autowired
	VisaOrderService visaOrderService;
	@Autowired
	VisaProductsService visaProductsService;
	@Autowired
	private NewProcessMoneyAmountService processMoneyAmountService;
	@Autowired
	private CurrencyService  currencyService;
	@Autowired
	private VisaService visaService;
	@Autowired
	private UserReviewPermissionChecker permissionChecker;

	/**
	 * 押金转担保申请列表页
	 * @param orderId
	 * @param reviewId
	 * @param model
	 * @author xudong.he
	 * @date 2015-11-19
	 */
	@RequestMapping(value = "warrantFormNew")
	public String warrantFormNew(String orderId, String reviewId, Model model) {
		VisaOrder visaOrder = visaOrderService.findVisaOrder(StringUtils.toLong(orderId));
		VisaProducts visaProdcut = null;
		if(visaOrder.getVisaProductId() != null) {
			visaProdcut = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
		}
//		//获得游客列表
		List<Map<String, Object>> travelerList =  visaOrderService.getTravellerInfoByOrderId(orderId);
		//出于对性能的考量对travelerList进行间接的金额处理
		visaProductsService.dealTravelerList(travelerList);
		Long deptId = visaOrderService.getDeptId(orderId);
		//已经产生申请的记录
		List<Map<String, Object>> processList =  processReviewService.getReviewDetailMapListByOrderId(deptId, 6, 6, orderId,
				  OrderByPropertiesType.CREATE_DATE,OrderByDirectionType.DESC);
		//当产生申请记录时间，过滤掉在审核中的记录
        for (Map<String, Object> aProcessList : processList)
            for (int j = 0; j < travelerList.size(); j++) {
                if (travelerList.get(j).get("tid").toString().equals(aProcessList.get("travellerId").toString()) && "1".equals(aProcessList.get("status").toString()))
                    travelerList.remove(j);
            }
		model.addAttribute("travelerList", travelerList);
		model.addAttribute("visaProdcut",visaProdcut);
		model.addAttribute("visaOrder", visaOrder);
		model.addAttribute("proId", orderId);
		return "review/warrant/warrantForm";
	}
	/**
	 * 押金转担保申请记录列表页
	 * @param orderId
	 * @param model
	 * @author xudong.he
	 * @date 2015-11-19
	 */
	@RequestMapping(value = "warrantListNew")
	public String warrantListNew(@RequestParam(value = "orderId", required = true) String orderId, Model model) {
		//产品类型
		int productType =6;
		//流程
		int processType =6;
		model.addAttribute("orderId", orderId);
		Long deptId = visaOrderService.getDeptId(orderId);
		List<Map<String, Object>> processList =  processReviewService.getReviewDetailMapListByOrderId(deptId, productType, processType, orderId,
				  OrderByPropertiesType.CREATE_DATE,OrderByDirectionType.DESC);
		model.addAttribute("reviewDetailList", processList);
		return "review/warrant/warrantList";
	}
	/**
	 * 押金转担保申请展示页
	 * @param orderId
	 * @param request
	 * @author xudong.he
	 * @date  2015-11-19
	 */
	@RequestMapping(value = "warrantApply")
	@Transactional(rollbackFor=Exception.class)
	public String warrantApply(@RequestParam(value = "proId", required = true) String orderId, HttpServletRequest request) {
		//游客id
		String[] travelerIds = request.getParameterValues("travelerIds");
		String[] priceCurrency = request.getParameterValues("priceCurrency");
		//游客名称
		String[] travelerNames = request.getParameterValues("travelerName");
		//价格
		String[] prices = request.getParameterValues("price");
		//签证状态
		String[] visaStatus = request.getParameterValues("visaStatus");
		//担保类型
		String[] warrantTypes = request.getParameterValues("warrantType");
		//备注
		String[] reasonMarks = request.getParameterValues("reasonMark");
		//币种ID
		String[] currencyId = request.getParameterValues("currencyId");
		//获取签证订单数据
		VisaOrder visaOrder = visaOrderService.findVisaOrder(StringUtils.toLong(orderId));
        VisaProducts products  = visaProductsService.getVisaProductById(visaOrder.getVisaProductId());
		Long deptId = visaOrderService.getDeptId(orderId);
		//申请之前先要过滤已经在审批中的但未结束的记录
		for(int i=0;i<travelerIds.length;i++)
		{
			Map<String, Object> variables = new HashMap<>();
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT, visaOrder.getAgentinfoId());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID, orderId);
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, "6");
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID,visaOrder.getVisaProductId());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO, visaOrder.getOrderNo());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, visaOrder.getGroupCode());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, visaOrder.getCreateBy().getId());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR, visaOrder.getCreateBy().getId());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR_NAME, UserUtils.getUser(visaOrder.getCreateBy().getId()).getName());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER_NAME, visaOrder.getSalerName());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, travelerIds[i]);
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, travelerNames[i]);
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_1, visaStatus[i]);
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_2, warrantTypes[i]);
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK, reasonMarks[i]);
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_3, prices[i]);
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_4, priceCurrency[i]);
            variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_5, visaOrder.getAgentinfoName());
            variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME,products.getProductName());
			//创建审批流程
			ReviewResult result  = processReviewService.start(UserUtils.getUser().getId().toString(),UserUtils.getUser().getCompany().getUuid(), permissionChecker, "", 
					6, Context.REVIEW_FLOWTYPE_DEPOSITTOWARRANT, deptId, "", variables);
			NewProcessMoneyAmount newProcessMoneyAmount =new NewProcessMoneyAmount();
			newProcessMoneyAmount.setId(UuidUtils.generUuid()); 
			newProcessMoneyAmount.setCurrencyId(Integer.parseInt(currencyId[i]));
			newProcessMoneyAmount.setAmount(new BigDecimal(prices[i]));
			newProcessMoneyAmount.setMoneyType(Context.REVIEW_FLOWTYPE_DEPOSITTOWARRANT);
			newProcessMoneyAmount.setOrderType(6);
			newProcessMoneyAmount.setCreatedBy(UserUtils.getUser().getId());
			newProcessMoneyAmount.setCreateTime(new Date());
			Currency c= currencyService.findCurrency(Long.parseLong(currencyId[i]));
			newProcessMoneyAmount.setExchangerate(c.getCurrencyExchangerate());
			newProcessMoneyAmount.setReviewId(result.getReviewId());
			//保存押金转担保金额
			processMoneyAmountService.saveNewProcessMoneyAmount(newProcessMoneyAmount);
		}
		return "redirect:"+Global.getAdminPath()+"/order/manager/visaNew/warrantListNew?orderId=" + orderId;
	}
	/**
	 * 取消申请
	 * @param reviewId
	 * @param orderId
	 *  @author xudong.he
	 * @return
	 */
	@RequestMapping("removeWarrantReview")
	public String removeWarrantReview(@RequestParam(value="reviewId", required=true) String reviewId,@RequestParam(value="orderId", required=true) String orderId) {
		processReviewService.cancel(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getId().toString(), "", reviewId, "", null);
		return "redirect:"+Global.getAdminPath()+"/order/manager/visaNew/warrantListNew?orderId="+orderId;
	}
	/**
	 * 获取审批详情
	 * @param reviewId
	 * @param orderId
	 * @author xudong.he
	 * @return
	 */
	@RequestMapping("getReviewDetailByReviewId")
	public String getReviewDetailByReviewId(@RequestParam(value="reviewId", required=true) String reviewId,
			@RequestParam(value="orderId", required=true) String orderId,Model model,
			@RequestParam(value="travellerId", required=true) String travellerId) {
		Map<String, Object> map = processReviewService.getReviewDetailMapByReviewId(reviewId);
		VisaOrder visaOrder = visaOrderService.findVisaOrder(StringUtils.toLong(orderId));
		VisaProducts visaProdcut = null;
		if(visaOrder.getVisaProductId() != null) {
			visaProdcut = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
		}
		String visaStatus= visaProductsService.getVisaType(travellerId); 
		//处理审核动态信息
		if(reviewId!=null&&!"".equals(reviewId)){//显示动态审核的标志
	    	List<ReviewLogNew> rLog = processReviewService.getReviewLogByReviewId(reviewId);
	    	model.addAttribute("rLog",rLog);
	    }
		model.addAttribute("visaStatus", visaStatus);
		model.addAttribute("map", map);
		model.addAttribute("visaOrder", visaOrder);
		model.addAttribute("visaProdcut", visaProdcut);
		return "review/warrant/depositToWarrantReviewDetailNew";
	}

    /**
     * @param model
     * @param response
     * @param request
     * @return
     */
	@RequestMapping("showDepositToWarrantList")
	public String showDepositToWarrantList(Model model, HttpServletResponse response, HttpServletRequest request)
	{
		Map<String, String> mapRequest = quereyParam(request);
		Page<Map<Object, Object>>    pageOrder  = visaService.getReviewList(new Page<Map<Object, Object>>(request, response),mapRequest);
        List<Map<String, Object>> createByNameList = visaOrderService.findVisaOrderCreateBy1();
        String appliFlag = request.getParameter("appliType");
		if(StringUtils.isBlank(appliFlag)) {
			appliFlag = "1";
		}
        model.addAttribute("appliFlag",appliFlag);
        model.addAttribute("travellerList",visaService.getTravellerList());
        model.addAttribute("appStarterList",visaService.getAppStarterList());
        model.addAttribute("createByList", createByNameList);
        model.addAttribute("page",pageOrder);
        setReturnValue(model,request);
		return "review/warrant/showDepositToWarrantList";
	}
	/**
	 * 处理查询参数
	 * @param request
	 * @return
	 */
	private Map<String, String> quereyParam(HttpServletRequest request) {
		Map<String,String> mapRequest = Maps.newHashMap();
        mapRequest.put("orderNum", request.getParameter("orderNum"));
        mapRequest.put("agent", request.getParameter("agent"));
        mapRequest.put("agent", request.getParameter("agent"));
        mapRequest.put("createByName", request.getParameter("createByName"));
        mapRequest.put("timeBegin", request.getParameter("timeBegin"));
        mapRequest.put("timeEnd", request.getParameter("timeEnd"));
        mapRequest.put("applyPromoter", request.getParameter("applyPromoter"));
        mapRequest.put("travellerId", request.getParameter("travellerId"));
        mapRequest.put("status", request.getParameter("status"));
        mapRequest.put("appliType", request.getParameter("appliType"));
        mapRequest.put("paymentType", request.getParameter("paymentType"));
		return mapRequest;
	}
	/**
	 * 审核弹出框
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "returnMoneyConfirm", method = RequestMethod.GET)
	public Object returnMoneyConfirm(HttpServletRequest request,HttpServletResponse response,Model model){
		
		 String chosenNum = request.getParameter("chosenNum");
		 model.addAttribute("chosenNum", chosenNum);
		 return "review/warrant/returnMoneyConfirm";
	}
	
	
	/**
	 * 设定页面返回值
	 * @param model
	 * @param request
	 * @param pageOrder
	 * @param jd
	 * @param saler
	 * @param visaTypeList
	 */
	private void setReturnValue(Model model, HttpServletRequest request) {
    	model.addAttribute("orderNum", request.getParameter("orderNum"));
    	model.addAttribute("agent",request.getParameter("agent"));
    	model.addAttribute("createByName",request.getParameter("createByName"));
    	model.addAttribute("timeBegin",request.getParameter("timeBegin"));
    	model.addAttribute("timeEnd",request.getParameter("timeEnd"));
		model.addAttribute("isAccounted",request.getParameter("isAccounted"));
		model.addAttribute("applyPromoter",request.getParameter("applyPromoter"));
		model.addAttribute("travellerName",request.getParameter("travellerName"));
		model.addAttribute("status",request.getParameter("status"));
		model.addAttribute("paymentType",request.getParameter("paymentType"));
	}
		
}
