package com.trekiz.admin.review.refund.common.web;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.utils.AreaUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.service.ProductOrderService;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.SysOfficeProductType;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.service.SysOfficeConfigurationService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.review.common.service.ICommonReviewService;
import com.trekiz.admin.review.refund.common.service.IRefundReviewService;
import com.trekiz.admin.review.refundtable.ProductOrderNewService;

import freemarker.template.TemplateException;

@Controller
@RequestMapping(value = "${adminPath}/refundnew")
public class RefundReviewController {

	@Autowired
	private IRefundReviewService refundReviewService;

	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Autowired
	private VisaProductsService visaProductsService;
	
	@Autowired
	private DictService dictService;
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private ProductOrderNewService productOrderService;
	
	@Autowired
	private UserReviewPermissionChecker userReviewPermissionChecker;
	
	@Autowired
	private ICommonReviewService commonReviewService;
	
	@Autowired
	private SysOfficeConfigurationService sysOfficeConfigurationService;
	
	@Autowired
	private ProductOrderService orderService;
	
	@Autowired
	private com.trekiz.admin.modules.reviewflow.service.ReviewService iReviewService; 
	/**
	 * 退款审批列表(新) by chy 2015年11月9日11:32:13
	 */
	@RequestMapping(value = "refundReviewListNew")
	public String refundReviewListNew(Model model, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> params = prepareParams(request, response);
		List<SysOfficeProductType> processTypes = sysOfficeConfigurationService.obtainOfficeProductTypes(UserUtils.getUser().getCompany().getUuid());
		Page<Map<String, Object>> page = refundReviewService.queryRefundReviewListNew(params);
		model.addAttribute("page", page);
		model.addAttribute("processTypes", processTypes);
		model.addAttribute("conditionsMap", params);
		return "review/refund/refundreviewnewlist";
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
		//产品类型(id)
		String productType = request.getParameter("productType") == null ? null : request.getParameter("productType").toString();
		//渠道商(id)
		String agentId = request.getParameter("agentId") == null ? null : request.getParameter("agentId").toString();
		//申请日期（from）
		String applyDateFrom = request.getParameter("applyDateFrom") == null ? null : request.getParameter("applyDateFrom").toString();
		//申请日期（to）
		String applyDateTo = request.getParameter("applyDateTo") == null ? null : request.getParameter("applyDateTo").toString();
		//审批发起人(id)
		String applyPerson = request.getParameter("applyPerson") == null ? null : request.getParameter("applyPerson").toString();
		//计调(id)
		String operator = request.getParameter("operator") == null ? null : request.getParameter("operator").toString();
		//退款金额 (from)
		String refundMoneyFrom = request.getParameter("refundMoneyFrom") == null ? null : request.getParameter("refundMoneyFrom").toString();
		//退款金额 (to)
		String refundMoneyTo = request.getParameter("refundMoneyTo") == null ? null : request.getParameter("refundMoneyTo").toString();
		//审批状态
		String reviewStatus = request.getParameter("reviewStatus") == null ? null : request.getParameter("reviewStatus").toString();
		//出纳确认
		String cashConfirm = request.getParameter("cashConfirm") == null ? null : request.getParameter("cashConfirm").toString();
		//打印状态
		String printStatus = request.getParameter("printStatus") == null ? null : request.getParameter("printStatus").toString();
		//页签选择状态
		String tabStatus = request.getParameter("tabStatus") == null ? null : request.getParameter("tabStatus").toString();
		if(tabStatus == null || "".equals(tabStatus)){//默认为待本人审批
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
		if(StringUtils.isBlank(orderCreateDateSort) && StringUtils.isBlank(orderUpdateDateSort) && StringUtils.isBlank(orderCreateDateCss) && StringUtils.isBlank(orderUpdateDateCss)){
			orderCreateDateSort = "desc";
			orderCreateDateCss = "activitylist_paixu_moren";
		}
		//page对象
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(request, response);
		/**获取参数 end*/
		/**组装参数 start*/
		result.put("groupCode", groupCode);
		result.put("productType", productType);
		result.put("agentId", agentId);
		result.put("applyDateFrom", applyDateFrom);
		result.put("applyDateTo", applyDateTo);
		result.put("applyPerson", applyPerson);
		result.put("operator", operator);
		result.put("refundMoneyFrom", refundMoneyFrom);
		result.put("refundMoneyTo", refundMoneyTo);
		result.put("reviewStatus", reviewStatus);
		result.put("cashConfirm", cashConfirm);
		result.put("printStatus", printStatus);
		result.put("tabStatus", tabStatus);
		result.put("orderCreateDateSort", orderCreateDateSort);
		result.put("orderUpdateDateSort", orderUpdateDateSort);
		result.put("orderCreateDateCss", orderCreateDateCss);
		result.put("orderUpdateDateCss", orderUpdateDateCss);
		result.put("paymentType",  request.getParameter("paymentType"));
		result.put("pageP", page);
		/**组装参数 end*/
		return result;
	}
	
	/**
	 * 审批方法 通过 或驳回
	 */
	@ResponseBody
	@RequestMapping(value = "refundReview")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public String refundReview(Model model, HttpServletRequest request, HttpServletResponse response){
		// 1 组织参数
		String revId = request.getParameter("revId");//审批表id
		String strResult = request.getParameter("result");//审批结果 1 通过 0 驳回
		String denyReason = request.getParameter("denyReason");//驳回原因
		String amount = request.getParameter("moneyAmount");//退款数额
		String orderType = request.getParameter("orderTypeSub");//产品类型
		String orderId = request.getParameter("orderId");//订单id
		String currencyId = request.getParameter("currencyId");//币种id
		Map<String, Object> variables = new HashMap<String, Object>();
		ReviewResult reviewResult;
		String companyId = UserUtils.getUser().getCompany().getUuid();
		// 2 调用审批接口处理
		if(Context.REVIEW_ACTION_PASS.equals(strResult)){//通过
			reviewResult = reviewService.approve(UserUtils.getUser().getId().toString(), companyId, null, userReviewPermissionChecker, revId, denyReason, variables);
			// 审核驳回之后对成本进行更改
			ReviewNew review = reviewService.getReview(reviewResult.getReviewId());
			commonReviewService.updateCostRecordStatus(review, reviewResult.getReviewStatus());
		} else {//驳回
			reviewResult = reviewService.reject(UserUtils.getUser().getId().toString(), companyId, null, revId, denyReason, variables);
			// 审核驳回之后对成本进行更改
			ReviewNew review = reviewService.getReview(reviewResult.getReviewId());
			commonReviewService.updateCostRecordStatus(review, reviewResult.getReviewStatus());
		}
		// 3如果审批通过并且当前层级为最高层级 则直接调用退款接口退款
		if(ReviewConstant.REVIEW_STATUS_PASSED == reviewResult.getReviewStatus()){
			List<MoneyAmount> moneyList = new ArrayList<MoneyAmount>();
			MoneyAmount ma = new MoneyAmount();
			ma.setAmount(BigDecimal.valueOf(Double.valueOf(amount)));//款数
			ma.setOrderType(Integer.parseInt(orderType));//订单类型 即 产品类型
			ma.setMoneyType(Context.MONEY_TYPE_TK);//款项类型 退款是11 这里写死
			ma.setUid(Long.parseLong(orderId));//订单id
			ma.setReviewUuid(revId);//revId
			ma.setCurrencyId(Integer.parseInt(currencyId));//币种
			ma.setBusindessType(1);//1标示订单退款
			moneyList.add(ma);
			moneyAmountService.saveMoneyAmounts(moneyList);
//			// 审核通过之后对成本进行更改 注释掉了 by chy 2015年12月18日19:53:18 改为审批操作是通过时调用
//			ReviewNew review = reviewService.getReview(reviewResult.getReviewId());
//			commonReviewService.updateCostRecordStatus(review, reviewResult.getReviewStatus());
		}
		return "success";
	}
	
	/**
	 * 进入退款审批详情页
	 */
	@RequestMapping(value = "refundReviewDetail")
	public String queryRefundReviewDetail(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		String orderId = request.getParameter("orderid");// 订单id
		String reviewId = request.getParameter("revid");// 审批表id
		String nowlevel = request.getParameter("nowlevel");
		// 查询审批详情信息
		// 产品类型 单办 还是参团 所查询的信息是不同的
		String prdType = request.getParameter("prdType");
		if (prdType == null || "".equals(prdType.trim())) {
			return null;
		}
		if ("7".equals(prdType.trim()) || "8".equals(prdType.trim())) {// 7代表机票 相当于单办 查询单办信息 8 代表机票切位
			// 由于这个内容和申请退款一致 所以调用了申请退款的这个查询
			Map<String, Object> orderDetail = refundReviewService.queryAirticketorderDeatail(orderId, prdType);
			//处理多币种信息 start
			String totalMoney = orderDetail.get("totalmoney") == null ? null : orderDetail.get("totalmoney").toString();
			totalMoney = moneyAmountService.getMoney(totalMoney);
			orderDetail.remove("totalmoney");
			orderDetail.put("totalmoney", totalMoney);
			//处理多币种信息 end
			model.addAttribute("orderDetail", orderDetail);
			
		} else if ("6".equals(prdType.trim())) {// 6代表签证 查询签证信息
			Map<String, Object> orderDetail = refundReviewService.queryVisaorderDeatail(orderId);
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
//			VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderDetail.get("orderid").toString()));
//			model.addAttribute("visaOrder", visaOrder);
			model.addAttribute("visaType", visaType);
			model.addAttribute("country", country);
			
			
			//处理多币种 end
			model.addAttribute("orderDetail", orderDetail);
		} else if ("2".equals(prdType.trim())){// 2代表 散拼
			Map<String, Object> orderDetail = refundReviewService.querySanPinReviewOrderDetail(orderId);
			if(orderDetail != null){
				// 处理多币种信息 start
				String totalMoney = orderDetail.get("totalmoney") == null ? null : orderDetail.get("totalmoney").toString();
				totalMoney = moneyAmountService.getMoney(totalMoney);
				orderDetail.remove("totalmoney");
				orderDetail.put("totalmoney", totalMoney);
				// 处理多币种信息 end
				// 处理目的地信息 start
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> targetArea = orderDetail.get("targetAreas") == null ? null : (List<Map<String, Object>>)orderDetail.get("targetAreas");
				if (targetArea != null && targetArea.size()!=0) {
					String areaString = "";
					int tempN = 0;
					for (Map<String, Object> tempS : targetArea) {
						if (tempN != 0) {
							areaString += ",";
						}
						areaString += AreaUtil.findAreaNameById(Long.parseLong(tempS.get("targetAreaId").toString()));
						tempN++;
					}
					orderDetail.remove("targetarea");
					orderDetail.put("targetarea", areaString);
				}
				// 处理目的地信息 end
				model.addAttribute("orderDetail", orderDetail);
			}
		} /**else if ("9".equals(prdType.trim())){// 9代表散拼切位  暂无散拼切位 不予考虑
			Map<String, Object> orderDetail = refundReviewService.querySanPinReserveOrderDetail(orderId);
			// 处理目的地信息 start
			String targetArea = orderDetail.get("targetArea") == null ? null : orderDetail.get("targetarea").toString();
			if (targetArea != null && !"".equals(targetArea.trim())) {
				String[] strings = targetArea.split(",");
				String areaString = "";
				int tempN = 0;
				for (String tempS : strings) {
					if (tempN != 0) {
						areaString += ",";
					}
					areaString += AreaUtil.findAreaNameById(Long.parseLong(tempS));
					tempN++;
				}
				orderDetail.remove("targetarea");
				orderDetail.put("targetarea", areaString);
			}
			// 处理目的地信息 end
			model.addAttribute("orderDetail", orderDetail);
		} */else {// 查询参团信息 1、3、4、5
			Map<String, Object> grouporderDeatail = refundReviewService.queryGrouporderDeatail(orderId);
			if(grouporderDeatail != null){
				// 处理多币种信息
				String totalMoney = grouporderDeatail.get("totalmoney") == null ? null : grouporderDeatail.get("totalmoney").toString();
				totalMoney = moneyAmountService.getMoney(totalMoney);
				grouporderDeatail.remove("totalmoney");
				grouporderDeatail.put("totalmoney", totalMoney);
				// 处理targetArea 目标城市 是数组
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> targetArea = grouporderDeatail.get("targetAreas") == null ? null : (List<Map<String, Object>>)grouporderDeatail.get("targetAreas");
				if (targetArea != null && targetArea.size()!=0) {
					String areaString = "";
					int tempN = 0;
					for (Map<String, Object> tempS : targetArea) {
						if (tempN != 0) {
							areaString += ",";
						}
						areaString += AreaUtil.findAreaNameById(Long.parseLong(tempS.get("targetAreaId").toString()));
						tempN++;
					}
					grouporderDeatail.remove("targetarea");
					grouporderDeatail.put("targetarea", areaString);
				}
				//处理targetArea end
				model.addAttribute("orderDetail", grouporderDeatail);
			}
		}
		// 查询退款信息
		Map<String, Object> review = reviewService.getReviewDetailMapByReviewId(reviewId);
		model.addAttribute("flag", request.getParameter("flag"));
		model.addAttribute("reviewdetail", review);
		model.addAttribute("nowlevel", nowlevel);
		model.addAttribute("rid",reviewId);
		model.addAttribute("arrivedareas", areaService.findAirportCityList());// 到达城市
		return "review/refund/refundReviewDetail";
	}
	
	/**
	 * 批量退款审批的审批通过或驳回，test
	 */
	@ResponseBody
	@RequestMapping(value = "batchrefundReview")
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public String batchRefundReview(Model model, HttpServletRequest request, HttpServletResponse response){
		// 1 组织参数
		String revIds = request.getParameter("revIds");//审批表ids
		String remark = request.getParameter("remark");//通过/驳回原因
		String strResult = request.getParameter("result");//通过/驳回原因
		Integer reviewOperation = 0;
		if("2".equals(strResult)){
			reviewOperation = ReviewConstant.REVIEW_OPERATION_PASS;
		}
		String[] revidArr = revIds.split(",");
		for(String revid : revidArr){
			if(revid == null || "".equals(revid)){
				System.err.println("错误的参数reviewid不能为空 airticketRefundReviewContriller line 718");
				continue;
			}
			Map<String, Object> review = reviewService.getReviewDetailMapByReviewId(revid);
			ReviewResult reviewResult;
			String companyId = UserUtils.getUser().getCompany().getUuid();
			// 2 调用审批接口处理
			if(ReviewConstant.REVIEW_OPERATION_PASS == reviewOperation){
				reviewResult = reviewService.approve(UserUtils.getUser().getId().toString(), companyId, null, userReviewPermissionChecker, revid, remark, null);
				// 审核通过之后对成本进行更改
				ReviewNew reviewInfo = reviewService.getReview(reviewResult.getReviewId());
				commonReviewService.updateCostRecordStatus(reviewInfo, reviewResult.getReviewStatus());
			} else {
				reviewResult = reviewService.reject(UserUtils.getUser().getId().toString(), companyId, null, revid, remark, null);
				// 审核驳回之后对成本进行更改
				ReviewNew reviewInfo = reviewService.getReview(reviewResult.getReviewId());
				commonReviewService.updateCostRecordStatus(reviewInfo, reviewResult.getReviewStatus());
			}
			// 3如果审批通过并且审批流程结束 则直接调用退款接口退款
			if(ReviewConstant.REVIEW_STATUS_PASSED == reviewResult.getReviewStatus()){
				List<MoneyAmount> moneyList = new ArrayList<MoneyAmount>();
				MoneyAmount ma = new MoneyAmount();
				Object refundPriceObj = review.get("refundPrice");
				String refundPrice = "0";
				if(refundPriceObj != null && NumberUtils.isNumber(refundPriceObj.toString())){
					refundPrice = refundPriceObj.toString();
				}
				ma.setAmount(BigDecimal.valueOf(Double.valueOf(refundPrice)));//款数
				if(review.get("productType") == null || StringUtils.isBlank(review.get("productType").toString())){
					continue;
				}
				ma.setOrderType(Integer.parseInt(review.get("productType").toString()));//订单类型 即 产品类型
				ma.setMoneyType(Context.MONEY_TYPE_TK);//款项类型 退款是11 这里写死
				if(review.get("orderId") == null || StringUtils.isBlank(review.get("orderId").toString())){
					continue;
				}
				ma.setUid(Long.parseLong(review.get("orderId").toString()));//订单id
				ma.setReviewUuid(revid);//revId
				if(review.get("currencyId") == null || StringUtils.isBlank(review.get("currencyId").toString())){
					continue;
				}
				ma.setCurrencyId(Integer.parseInt(review.get("currencyId").toString()));//币种
				ma.setBusindessType(1);//1标示订单退款
				moneyList.add(ma);
				moneyAmountService.saveMoneyAmounts(moneyList);
//				// 审核通过之后对成本进行更改 注释掉了 by chy 2015年12月18日19:52:09 执行通过时调用
//				ReviewNew reviewInfo = reviewService.getReview(reviewResult.getReviewId());
//				commonReviewService.updateCostRecordStatus(reviewInfo, Integer.parseInt(strResult));
			}
		}
		return "success";
	}
	
	/**
	 * 审批撤销方法
	 */
	@ResponseBody
	@RequestMapping(value="backrefundreview/{reviewId}")
	public Map<String, Object> backRefundReview(@PathVariable String reviewId, HttpServletRequest request, HttpServletResponse response){
		
		/*声明返回对象*/
		Map<String, Object> result = new HashMap<String, Object>();
		String companyId = UserUtils.getUser().getCompany().getUuid();
		/*调用审批接口*/
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
	
	   /**
	    * 退款单详情
	    * */
		@RequestMapping(value="refundReviewInfo")
		public String printRefundReviewInfo(Model model,HttpServletRequest request, HttpServletResponse response){
			
		    Map<String,String> refundInfo = productOrderService.findProductOrderById(request);
			model.addAttribute("refundInfo", refundInfo);
			
			return "review/refund/refundReviewInfo";
		}
		
		/**
		 * 更新打印标志
		 * @param request
		 * @return
		 */
		@RequestMapping(value="updatePrint")
		public String updatePrint(HttpServletRequest request, HttpServletResponse response) throws Exception{
			 ReviewNew review = reviewService.getReview(request.getParameter("reviewId"));
			 if(review.getPrintStatus() == null || review.getPrintStatus() == 0){
				 reviewService.updatePrintFlag(UserUtils.getUser().getId().toString(), "1", request.getParameter("reviewId"));
			 }
			return "success";
		}
		/**
		 * 
		 * @param request
		 * @return
		 */
		@RequestMapping(value="downloadList")
		public ResponseEntity<byte[]> downloadList(HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateException{

			Map<String,String> refundInfo = productOrderService.findProductOrderById(request);
		    //处理开户行/账户名信息
		    String bankInfo = refundInfo.get("bankInfo");
		    bankInfo = bankInfo.replace("</br>", " ");
		    refundInfo.put("bankInfo", bankInfo);
		    
			String option = request.getParameter("option");
			String ftl = "refundlist_new.ftl";
			if("order".equals(option)) {
				ftl = "refundlist.ftl";
			}
			File file = FreeMarkerUtil.generateFile(ftl, "refundlist.doc", refundInfo);
			
			String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
			String fileName =  "退款单" + nowDate + ".doc" ;
			OutputStream os = null;
	    	try {
				if(file != null && file.exists()){
					response.reset();
					response.setHeader("Content-Disposition", "attachment; filename="+new String(fileName.getBytes("gb2312"), "ISO-8859-1"));
					response.setContentType("application/octet-stream; charset=utf-8");
			    	os = response.getOutputStream();
					os.write(FileUtils.readFileToByteArray(file));
		            os.flush();
				}       		
	    		
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				if(os!=null)
					try {
						os.close();
					} catch (Exception e) {
					}
			}
			return null;
		}
		
		  /**
		   * 批量退款单详情
		 * @throws SecurityException 
		 * @throws NoSuchMethodException 
		 * @throws InvocationTargetException 
		 * @throws IllegalArgumentException 
		 * @throws IllegalAccessException 
		    * */
			@RequestMapping(value="batchRefundReviewInfo")
			public String batchPrintRefundReviewInfo(Model model,HttpServletRequest request, HttpServletResponse response) {
				List<Map<String,String>> list = new ArrayList<Map<String,String>>();
				List<Map<String,Object>> reviewIds = new ArrayList<Map<String,Object>>();
				try {
					String datas = request.getParameter("params");
					JSONArray array = JSONArray.parseArray(datas);
					for(int i = 0 ; i < array.size() ; i++){
						Map<String,Object> reviewMap = new HashMap<String, Object>();
						@SuppressWarnings("unchecked")
						Map<String,String> map = request.getParameterMap();
						Method method = map.getClass().getMethod("setLocked",new Class[]{boolean.class});
						method.invoke(map,new Object[]{new Boolean(false)});
						JSONObject object = array.getJSONObject(i);
						reviewMap.put("reviewId", object.getString("reviewId"));
						reviewMap.put("nFlag", object.getString("nFlag"));
						reviewIds.add(reviewMap);
						map.put("productType", object.getString("productType"));
						map.put("agentId", object.getString("agentId"));
						map.put("orderId", object.getString("orderId"));
						map.put("reviewId", object.getString("reviewId"));
						map.put("groupCodePrint", object.getString("groupCodePrint"));
						map.put("payId", object.getString("payId"));
						map.put("option", "order");
						map.put("batch", "1");
						Map<String,String> refundInfo = null;
						if(object.get("nFlag") != null && object.getInteger("nFlag") == 1){
							refundInfo = orderService.findProductOrderById(request);
						}else{
							refundInfo = productOrderService.findProductOrderById(request);
						}
						list.add(refundInfo);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} 
				model.addAttribute("list", list);
				model.addAttribute("reviewIds", JSON.toJSONString(reviewIds));
				return "review/refund/batchRefundReviewInfo";
			}
			
			/**
			 *批量更新打印标志
			 * @param request
			 * @return
			 */
			@RequestMapping(value="batchUpdatePrint")
			public String batchUpdatePrint(HttpServletRequest request, HttpServletResponse response) throws Exception{
				JSONArray array = JSONArray.parseArray(request.getParameter("reviewId"));
				for(int i = 0 ; i < array.size() ; i ++){
					JSONObject object = array.getJSONObject(i);
					if(object.get("nFlag") != null && object.getInteger("nFlag") == 1){
						 Review review = iReviewService.findReviewInfo(object.getLong("reviewId"));
						 if(review.getPrintFlag() == null || review.getPrintFlag() == 0){
							 Date date = new Date();
							 review.setPrintFlag(1);//第一次打印更改标志
							 review.setPrintTime(date);//设置打印时间
							 review.setUpdateDate(date);
							 review.setUpdateBy(UserUtils.getUser().getId());
						 }
						 iReviewService.updateRivew(review);
					}else{
						 ReviewNew review = reviewService.getReview(object.getString("reviewId"));
						 System.out.println(review.getPrintStatus() == 0);
						 if(review.getPrintStatus() == null || review.getPrintStatus() == 0){
							 System.out.println(object.getString("reviewId"));
							 reviewService.updatePrintFlag(UserUtils.getUser().getId().toString(), "1", object.getString("reviewId"));
						 }
					}	 
				}
				return "success";
			}
}
