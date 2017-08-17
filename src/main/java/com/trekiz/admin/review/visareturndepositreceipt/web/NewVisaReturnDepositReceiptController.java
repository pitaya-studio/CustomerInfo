package com.trekiz.admin.review.visareturndepositreceipt.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaService;
import com.trekiz.admin.review.borrowing.airticket.service.NewOrderReviewService;

/**
 * xinwei.wang added
 * @author
 */
@Controller
@RequestMapping(value = "${adminPath}/newreviewvisa/returndepositreceipt")
public class NewVisaReturnDepositReceiptController extends BaseController {
	
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private TravelerService travelerService;
	@Autowired
	private VisaOrderService visaOrderService;
	@Autowired
	private VisaService visaService;
	@Autowired
	private MoneyAmountService  moneyAmountService;
	@Autowired
    private VisaProductsService visaProductsService;
	@Autowired
	private DictService dictService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private NewOrderReviewService newOrderReviewService;
	@Autowired
	private UserReviewPermissionChecker permissionChecker;
	
	/**
	 *还签证押金收据申请
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException
	 */
	@ResponseBody
	@RequestMapping(value = "createVisaHYJSJ")
	public Map<String, String> createVisaHYJSJ(HttpServletRequest request,	
		HttpServletResponse response) throws JSONException {
		
		Map<String, String> resultMap = new HashMap<String, String>();
		
		String travelerID = request.getParameter("travelerID");//游客ID
		String depositReceiptAmount = request.getParameter("depositReceiptAmount");//收据金额
		String depositReceiptor = request.getParameter("depositReceiptor");//接收人
		String remark = request.getParameter("remark");//接收人
		String depositReceiptReturnTime = request.getParameter("depositReceiptReturnTime");
		
		//申请时的addReview 要用添加dept的方法
		Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerID));
		if (traveler == null) {
			resultMap.put("code", "1");
			resultMap.put("message", "获取游客信息失败！");
			return resultMap;
		}
		//订单
		Long orderId = traveler.getOrderId();//订单ID
		VisaOrder visaOrder = null;
		if (orderId != null) {
			visaOrder = visaOrderService.findVisaOrder(orderId);
		}
		if (visaOrder == null) {
			resultMap.put("code", "1");
			resultMap.put("message", "获取订单信息失败！");
			return resultMap;
		}
		//产品
		VisaProducts product = null;
		if (visaOrder.getVisaProductId() != null) {
			product = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
		}
		if (product == null) {
			resultMap.put("code", "1");
			resultMap.put("message", "获取产品信息失败！");
			return resultMap;
		}		
		//产品部门Id	
		Long deptId = product.getDeptId();
		if (deptId == null) {
			resultMap.put("code", "1");
			resultMap.put("message", "获取部门ID失败！");
			return resultMap;
		}
		
		//获取 登录用户、公司id、产品类型
		Long userId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid().toString();
		
		//组织业务数据
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID, orderId);  //订单id
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO,visaOrder.getOrderNo());  //订单编号
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, Context.ORDER_TYPE_QZ);  //订单类型、产品类型
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID,product.getId());   //产品id
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, product.getProductName());  //产品名称
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, visaOrder.getGroupCode());  //订单团号（此虚拟团号），另产品也有团号，与此不同
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT, visaOrder.getAgentinfoId());  //计调
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR,visaOrder.getCreateBy().getId());  //下单人
		// 游客id
		variables.put(Context.REVIEW_VARIABLE_KEY_RETURNDEPOSITRECEIPT_TRAVELERID, travelerID);
		// 游客名称
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, traveler.getName());
		// 游客应收金额 uuid
		variables.put(Context.REVIEW_VARIABLE_KEY_TRAVELER_PAY_PRICE, traveler.getPayPriceSerialNum());
		// 收据金额 (此处填写金额，默认币种人民币)
		variables.put(Context.REVIEW_VARIABLE_KEY_RETURNDEPOSITRECEIPT_AMOUNT, depositReceiptAmount);
		//还签证押金收据 归还人
		variables.put(Context.REVIEW_VARIABLE_KEY_RETURNDEPOSITRECEIPT_RECEIPTOR, depositReceiptor);
		//还签证押金收据 备注
		variables.put(Context.REVIEW_VARIABLE_KEY_RETURNDEPOSITRECEIPT_REMARK, remark);
		//还签证押金收据 归还时间
		variables.put(Context.REVIEW_VARIABLE_KEY_RETURNDEPOSITRECEIPT_TIME, depositReceiptReturnTime);
		
		//发起申请审核流程
		ReviewResult reviewResult = reviewService.start(userId.toString(), companyUuid, permissionChecker, null, Context.ORDER_TYPE_QZ, Context.REVIEW_FLOWTYPE_VISA_RETURNDEPOSITRECEIPT, deptId, remark, variables);
		
		//如果发起流程失败，则返回；如果成功则处理存储金额等
		if (!reviewResult.getSuccess()) {
			resultMap.put("code", "1");
			resultMap.put("message", reviewResult.getMessage());
			return resultMap;
		} 
		
		//0为申请成功 ,1为申请失败
		resultMap.put("code", "0");
		resultMap.put("message", "申请成功！");
		return resultMap;
	}
	
	/**
	 * 还签证押金收据申请审批列表
	 * wxw added
	 *  2014年12月25日11:11:49
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "visaReturnDepositReceiptReviewList4CW")
	public String visaReturnDepositReceiptReviewList4CW(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> conditionsMap = prepareQueryCond(request, response);
		Page<Map<String, Object>> page = newOrderReviewService.getReturnDepositReceiptReviewList(conditionsMap);
		model.addAttribute("conditionsMap", conditionsMap);
		model.addAttribute("page", page);
		
		List<Map<String, Object>> reviewList = page.getList();
		for(Map<String, Object> tMap : reviewList){
			//start 把游客的结算价的UUID转化为多币种的款项值 //TODO ?
			String payprice = tMap.get("travelerPayPrice") == null ? null : tMap.get("travelerPayPrice").toString();
			payprice = moneyAmountService.getMoney(payprice);
			tMap.remove("travelerPayPrice");
			tMap.put("travelerPayPrice", payprice);
			
			String revid = tMap.get("revid").toString();
			Map<String, Object> reviewAndDetailInfoMap = reviewService.getReviewDetailMapByReviewId(revid);
			tMap.put("denyReason", reviewAndDetailInfoMap.get("denyReason") != null ? reviewAndDetailInfoMap.get("denyReason").toString() : "");
		}

		page.setList(reviewList);
		List<String> createByNameList = visaOrderService.findVisaOrderCreateBy();
		model.addAttribute("createByList", createByNameList);
		
		return "review/visaReturndepositreceipt/visaReturnDepositReceiptReviewList4CW";
	}

	
	/**
	 * 查询条件处理 
	 * @param request
	 * @return
	 */
	private Map<String, Object> prepareQueryCond(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> conditionsMap = new HashMap<String, Object>();
		conditionsMap.put("orderType", request.getParameter("orderType"));
		conditionsMap.put("groupCode", request.getParameter("groupCode"));
		String statusChoose = request.getParameter("statusChoose");
		if (null==statusChoose) {
			statusChoose = "1";
		}
		conditionsMap.put("statusChoose", statusChoose);
		//团号/产品名称/订单号
		String orderCdGroupCd = request.getParameter("orderCdGroupCd") == null ? null : request.getParameter("orderCdGroupCd").toString();
		conditionsMap.put("orderCdGroupCd", orderCdGroupCd);
		//渠道商(id)
		String agentId = request.getParameter("agentId") == null ? null : request.getParameter("agentId").toString();
		conditionsMap.put("agentId", agentId);
		
		//-----wxw 2015-07-29 added 游客姓名------
		conditionsMap.put("travlerName", request.getParameter("travlerName") == null|| "".equals(request.getParameter("travlerName").trim())
				? null: request.getParameter("travlerName"));
		//-----wxw 2015-07-29 added 订单编号------
		conditionsMap.put("orderNum", request.getParameter("orderNum") == null|| "".equals(request.getParameter("orderNum").trim())
				? null: request.getParameter("orderNum"));
		//-----wxw 2015-07-29 added 押金金额------
		conditionsMap.put("depositeAmount", request.getParameter("depositeAmount") == null|| "".equals(request.getParameter("depositeAmount").trim())
				? null: request.getParameter("depositeAmount"));
		//审批状态
		String reviewStatus = StringUtils.isBlank(request.getParameter("reviewStatus")) ? null : request.getParameter("reviewStatus").toString();
		conditionsMap.put("reviewStatus", reviewStatus);
		//------wxw 2015-08-19 added 下单人------
		String orderCreateBy = request.getParameter("orderCreateBy");
		conditionsMap.put("orderCreateBy", orderCreateBy == null || "".equals(orderCreateBy.trim())? null: Integer.parseInt(orderCreateBy));
		
		//------wxw 2015-08-19 added 销售------
		String saler = request.getParameter("saler");
		conditionsMap.put("saler", saler == null || "".equals(saler.trim())? null: Integer.parseInt(saler));
		//审批发起人(id)
		String applyPerson = request.getParameter("applyPerson") == null ? null : request.getParameter("applyPerson").toString();
		conditionsMap.put("applyPerson", applyPerson);
		
		String meter = request.getParameter("meter");
		conditionsMap.put("meter", meter == null || "".equals(meter.trim())? null: Integer.parseInt(meter));
		
		// conditionsMap.put("active", request.getParameter("active"));
		conditionsMap.put("applyDateFrom", request.getParameter("applyDateFrom"));
		conditionsMap.put("applyDateTo", request.getParameter("applyDateTo"));
		conditionsMap.put("orderBy", request.getParameter("orderBy"));
		
		//处理从url传递的这两个字段
		conditionsMap.put("userJobId", request.getParameter("userJobId"));
		conditionsMap.put("flowType", request.getParameter("flowType"));
		
		String orderCreateDateSort  = request.getParameter("orderCreateDateSort");// 创建日期排序标识
		String orderUpdateDateSort = request.getParameter("orderUpdateDateSort");// 更新日期排序标识
		String orderUpdateDateCss = request.getParameter("orderUpdateDateCss");//订单更新日期排序标识
		//订单创建日期排序标识
		String orderCreateDateCss  = request.getParameter("orderCreateDateCss") == null ? "activitylist_paixu_moren"
				: StringUtils.isBlank(orderUpdateDateCss) ? "activitylist_paixu_moren" : request.getParameter("orderCreateDateCss"); //默认按照创建日期排序
		conditionsMap.put("orderCreateDateSort", orderCreateDateSort);
		conditionsMap.put("orderUpdateDateSort", orderUpdateDateSort);
		conditionsMap.put("orderCreateDateCss", orderCreateDateCss);
		conditionsMap.put("orderUpdateDateCss", orderUpdateDateCss);
		
		//page对象
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(request, response);
		conditionsMap.put("page", page);
		
		return conditionsMap;
	}
	
	
	/**
	 * 还签证押金收据审批详情页 
	 * wangxinwei
	 *  2014年12月24日15:15:00
	 */
	@RequestMapping(value = "visaReturnDepositeReceiptReviewDetail")
	public String visaReturnDepositeReceiptReviewDetail(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderId");
		String travelerId = request.getParameter("travelerId");
		String revid = request.getParameter("revid");
		String nowLevel = request.getParameter("nowLevel");
		String flowType = request.getParameter("flowType");
		String flag = request.getParameter("flag");
		String fromflag = request.getParameter("fromflag");
		
		//订单相关信息
		VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderId));
		String totalMoney = moneyAmountService.getMoney(visaOrder.getTotalMoney());
		model.addAttribute("totalMoney", totalMoney);
		model.addAttribute("visaOrder", visaOrder);
		
		//产品相关信息
		VisaProducts visaProduct = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
		Dict visaType = dictService.findByValueAndType(visaProduct.getVisaType().toString(), "new_visa_type");
		Dict collarZoning = dictService.findByValueAndType(visaProduct.getCollarZoning().toString(), "from_area");
		Country country = CountryUtils.getCountry(Long.parseLong(visaProduct.getSysCountryId().toString()));
		model.addAttribute("visaProduct", visaProduct);
		model.addAttribute("visaType", visaType);
		model.addAttribute("collarZoning", collarZoning);
		model.addAttribute("country", country);
		
		//游客相关信息
		Traveler traveler = null;
		Visa visa = null;
		if (StringUtils.isNotBlank(travelerId)) {
			traveler = travelerService.findTravelerById(Long.parseLong(travelerId));
			visa = visaService.findVisaByTravlerId(Long.parseLong(travelerId));
		}
		model.addAttribute("traveler", traveler);
		
		//签证相关信息
		Dict visaStauts = null;
		if (visa != null) {
			visaStauts = dictService.findByValueAndType(visa.getVisaStauts()+"", "visa_status");
		}
		model.addAttribute("visa", visa);
		model.addAttribute("visaStauts", visaStauts);
		
		//还签证押金收据申请相关信息
		Map<String, Object> reviewAndDetailInfoMap = reviewService.getReviewDetailMapByReviewId(revid);
		if (reviewAndDetailInfoMap!=null) {
			model.addAttribute("revCreateDate", reviewAndDetailInfoMap.get("createDate").toString().subSequence(0, 19));//报批日期
			model.addAttribute("revCreateReason", reviewAndDetailInfoMap.get("createReason"));//申报原因
			model.addAttribute("revDepositReceiptAmount", reviewAndDetailInfoMap.get("depositReceiptAmount"));//收据金额
			model.addAttribute("revReceiptor", reviewAndDetailInfoMap.get("receiptor"));//还收据人
			model.addAttribute("remark", reviewAndDetailInfoMap.get(Context.REVIEW_VARIABLE_KEY_RETURNDEPOSITRECEIPT_REMARK));
			String currencyId = reviewAndDetailInfoMap.get("currencyId") != null ? reviewAndDetailInfoMap.get("currencyId").toString() : null;
			if (StringUtils.isNotBlank(currencyId)) {
				Currency currency = currencyService.findCurrency(Long.parseLong(currencyId));
				model.addAttribute("revCurrency", currency);//还收据人
			}
		}
		model.addAttribute("nowLevel",nowLevel);
		model.addAttribute("orderId",orderId);
		model.addAttribute("travelerId",travelerId);
		model.addAttribute("revid",revid);
		model.addAttribute("flowType",flowType);
		model.addAttribute("flag", flag);
		model.addAttribute("rid",revid);
		
		model.addAttribute("fromflag",fromflag);
		
		return "review/visaReturndepositreceipt/visaReturnDepositReceiptReviewDetail";
	}
	
	/**
	 * add by sy  2015年11月4日14:17:30
	 * 审核转款申请信息Done
	 */
	@ResponseBody
	@RequestMapping(value ="reviewVisaDepositReturnReceipt")
	public Map<String, Object> reviewVisaDepositReturnReceipt(HttpServletResponse response, Model model, HttpServletRequest request,String rid,Integer result){
		
		Map<String, Object> map =new HashMap<String,Object>();
		ReviewResult r = null;
		String remark = request.getParameter("remark");
		try {
			// result 等于  1 审核通过  ， 等于 0  驳回
			if(result==1){
				 r=reviewService.approve(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid().toString(),"", permissionChecker, rid, remark, null);
				 
				 //审核通过时候的业务数据操作				 
				 if(r.getSuccess()){
					 if(r.getReviewStatus()==2){
						 //原代码中无业务处理
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
	 * 批量还签证押金收据审批通过或驳回
	 * @author yang.jiang 2015-12-2 2015-12-2 18:26:45
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "multiReviewVisaDepositReturnReceipt")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public Map<String, Object> multiReviewVisaDepositReturnReceipt(Model model, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> map =new HashMap<String,Object>();
		String msgStr = "</br>";
		boolean flag = true;
		// 1 组织参数
		String revIds = request.getParameter("revIds");//审核表ids
		String remark = request.getParameter("remark");//通过/驳回原因
		String strResult = request.getParameter("result");//通过/驳回选择
		Integer reviewOperation = 0;
		if("2".equals(strResult)){
			reviewOperation = ReviewConstant.REVIEW_OPERATION_PASS;
		}
		String[] revidArr = revIds.split(",");
		for(String revid : revidArr){
			if(revid == null || "".equals(revid)){
				map.put("flag", 0);
				map.put("message", msgStr += "错误的参数,reviewid不能为空");
				return map;
			}
			ReviewResult reviewResult;
			String companyId = UserUtils.getUser().getCompany().getUuid();
			// 2 调用审核接口处理
			if(ReviewConstant.REVIEW_OPERATION_PASS == reviewOperation){
				reviewResult = reviewService.approve(UserUtils.getUser().getId().toString(), companyId, null, permissionChecker, revid, remark, null);
			} else {
				reviewResult = reviewService.reject(UserUtils.getUser().getId().toString(), companyId, null, revid, remark, null);
			}
			
			if(reviewResult.getSuccess()){
				map.put("flag", 1);
				map.put("message", msgStr += "Success:" + reviewResult.getReviewId() + "</br>");
			} else {
				flag = false;
				map.put("message", msgStr += "Faild:" + reviewResult.getMessage() + "</br>");
			}
		}
		map.put("flag", flag ? 1 : 0);
		return map;
	}
	
	/**
	 *
	 * 转款撤销审批
	 */
	@ResponseBody
	@RequestMapping(value = "returnDepositReceiptCancelAjax")
	public Map<String, Object> backReviewTransferAmount(HttpServletResponse response, HttpServletRequest request) {
		Map<String, Object> result = new HashMap<String, Object>();
		String revid = request.getParameter("revid");
		ReviewResult r =reviewService.back(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid(), "", revid, null,null);
		
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

}
