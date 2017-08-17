package com.trekiz.admin.review.rebates.common.web;

import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.rebates.entity.RebatesNew;
import com.trekiz.admin.modules.order.rebates.service.RebatesService;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.PlatBankInfoService;
import com.trekiz.admin.modules.order.service.RefundService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.rebatesupplier.entity.RebateSupplier;
import com.trekiz.admin.modules.rebatesupplier.service.RebateSupplierService;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.review.visaRebates.service.IVisaRebatesService;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewLogDao;
import com.trekiz.admin.modules.reviewreceipt.common.ReviewReceiptContext;
import com.trekiz.admin.modules.reviewreceipt.service.ReviewReceiptService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.review.borrowing.airticket.web.ActivityAirTicketOrderLendMoneyController;
import com.trekiz.admin.review.rebates.airticket.service.NewAirticketrebatesService;
import com.trekiz.admin.review.rebates.common.service.RebatesReviewNewService;
import com.trekiz.admin.review.rebates.singleGroup.service.RebatesNewService;

/**
 * 新返佣审批
 * @author yakun.bai
 * @Date 2015-12-3
 */
@Controller
@RequestMapping(value = "${adminPath}/newRebatesReview")
public class RebatesReviewNewController extends BaseController {
	
	private static final Logger log = Logger.getLogger(ActivityAirTicketOrderLendMoneyController.class);
	
	@Autowired
	private RebatesReviewNewService rebatesReviewService;
	@Autowired
	private RebatesNewService rebatesNewService;
	@Autowired
    private OrderCommonService orderService;
	@Autowired
	@Qualifier("travelActivitySyncService")
    private ITravelActivityService travelActivityService;
	@Autowired
	@Qualifier("activityGroupSyncService")
    private IActivityGroupService activityGroupService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private RebatesReviewNewService rebatesReviewNewService; 
	@Autowired
	private ReviewReceiptService reviewReceiptService;
	@Autowired
	private ReviewService processReviewService;
	@Autowired
	private VisaOrderService visaOrderService;
	@Autowired
	private VisaProductsService visaProductsService;
	@Autowired
	private RebateSupplierService rebateSupplierService;
	@Autowired
	private AgentinfoService agentInfoService;
	@Autowired
	private PlatBankInfoService bankInfoService;
	@Autowired
	private RefundService refundService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private NewAirticketrebatesService newAirticketrebatesService;
	@Autowired
	private RebatesService rebatesService;
	@Autowired
	private ReviewLogDao reviewLogDao;
	@Autowired
	private com.trekiz.admin.modules.reviewflow.service.ReviewService ireviewService;
	@Autowired
	private CostManageService costManageService;
	@Autowired
	private ReviewCommonService reviewCommonService;
	@Autowired
	private ReviewDao reviewDao;
	@Autowired
	private IVisaRebatesService visaRebatesService;
	/** 返佣审批列表地址 */
	private static final String LIST_PAGE = "/review/rebates/common/rebatesReviewList";
	/** 单团类返佣审批地址 */
	private static final String SINGLE_GROUP_REBATES_REVIEW = "/review/rebates/common/singleGroupRebatesReview";
	/** 返佣打印 */
	private static final String REBATES_REVIEW_PRINT = "/review/rebates/common/rebatesReviewPrint";

	
	/**
	 * @Description 查询返佣审核记录
	 * @author yakun.bai
	 * @Date 2015-12-3
	 */
	@RequestMapping(value ="list")
	public String list(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		//查询条件
        Map<String, String> conditionsMap = Maps.newHashMap();
        
        //参数处理：去除空格和处理特殊字符并传递到后台
        //参数解释：团号、产品类型、渠道商、返佣供应商、申请开始时间、申请结束时间、申请人、计调、审核状态、打印状态、返佣开始金额、返佣结束金额、排序、切换框
        String paras = "groupCode,productType,agentId,supplierId,applyDateFrom,applyDateTo,applyPerson,operator,reviewStatus,printflag," +
        		"rebatesDiffBegin,rebatesDiffEnd,payStatus,orderBy,tabStatus,paymentType";
        OrderCommonUtil.handlePara(paras, conditionsMap, model, request);
        
        //返佣审核记录查询
        Page<Map<String, Object>> pagePara = new Page<Map<String, Object>>(request, response);
		Page<Map<String, Object>> page = rebatesReviewService.getRebatesReviewList(pagePara, conditionsMap);
		
		//查询单团类和机票订单金额
		page = rebatesReviewService.setMoney(page);

		//返佣对象 added by zhenxing.yan
		User user=UserUtils.getUser();
		Office company=user.getCompany();
		if(Integer.valueOf(1).equals(company.getIsAllowMultiRebateObject())){//允许多返佣对象
			List<RebateSupplier> suppliers=rebateSupplierService.obtainSupplier(company.getUuid());
			model.addAttribute("multiRebateObject",true);
			model.addAttribute("suppliers",suppliers);
		}else{
			model.addAttribute("multiRebateObject",false);
		}

		//值传递
		model.addAttribute("conditionsMap", conditionsMap);
		model.addAttribute("page", page);
		return LIST_PAGE;
	}
	
	/**
	 * 改价审核详情页
	 */
	@RequestMapping(value = "/rebatesDetail")
	public String rebatesDetail(@RequestParam("rebatesId") Long rebatesId, @RequestParam("productType") Long productType,
			Model model, HttpServletRequest request, HttpServletResponse response) {

		//单团、散拼、游学、大客户、自由行
		if (productType <= 5 || productType == 10) {
			singleGroupRebatesDetail(rebatesId, model);
			return SINGLE_GROUP_REBATES_REVIEW;
		}
		return null;
	}
	
	/**
	 * @Description 获取单团类返佣详情
	 * @author yakun.bai
	 * @Date 2015-12-5
	 */
	private void singleGroupRebatesDetail(Long rebatesId, Model model) {
		
		//查询返佣信息
		RebatesNew rebates = rebatesNewService.findRebatesById(rebatesId);
		//查询产品、团期、订单信息
		ProductOrderCommon productOrder = orderService.getProductorderById(rebates.getOrderId());
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		
		//获取游客预计返佣金额
		String travelerId = null;
		if (null != rebates.getTravelerId() && 0 < rebates.getTravelerId()) {
			travelerId = rebates.getTravelerId().toString();
		}
		String rebatesStr = OrderCommonUtil.getRebatesMoney(productOrder.getId(), productOrder.getOrderStatus(), travelerId);
		if (null == rebatesStr || 0 == rebatesStr.trim().length()) {
			model.addAttribute("rebatesSign", 0);
		} else {
			model.addAttribute("rebatesSign", 1);
		}

		//返佣对象 added by zhenxing.yan
		User user=UserUtils.getUser();
		Office company=user.getCompany();
		if(Integer.valueOf(1).equals(company.getIsAllowMultiRebateObject())){//允许多返佣对象
			model.addAttribute("multiRebateObject",true);
			//组装返佣对象信息
			Map<String,Object> reviewInfos=reviewService.getReviewDetailMapByReviewId(rebates.getReview().getId());
			model.addAttribute("reviewInfos",reviewInfos);
		}else{
			model.addAttribute("multiRebateObject",false);
		}

		//值传递
		model.addAttribute("product", product);
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("productOrder", productOrder);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		model.addAttribute("rebates", rebates);
		model.addAttribute("rid", rebates.getRid());
		model.addAttribute("rebatesStr", rebatesStr);
	}
	
	
	/**
	 * @Description 审核通过或驳回
	 * @author yakun.bai
	 * @Date 2015-12-5
	 */
	@ResponseBody
	@RequestMapping(value = "/rebatesReview")
	public Map<String, String> rebatesReview(@RequestParam("reviewId") String reviewId, @RequestParam("result") String strResult,
			Model model, HttpServletRequest request) {

		//驳回原因
		String denyReason = request.getParameter("denyReason");
		//转团审核
		Map<String, String> result = rebatesReviewService.rebatesReview(reviewId, strResult, denyReason, request);

		return result;
	}
	
	/**
	 * @Description 撤销审核
	 * @author yakun.bai
	 * @Date 2015-12-5
	 */
	@ResponseBody
	@RequestMapping(value = "/backReview/{reviewId}")
	public Map<String, Object> backReview(@PathVariable String reviewId) {

		Map<String, Object> result = Maps.newHashMap();
		String companyId = UserUtils.getUser().getCompany().getUuid();
		ReviewResult reviewResult = reviewService.back(UserUtils.getUser().getId().toString(), companyId, null, reviewId, null, null);
		if (reviewResult.getSuccess()) {
			result.put("result", "success");
		} else {
			result.put("result", "error");
			result.put("msg", reviewResult.getMessage());
		}
		return result;
	}
	
	/**
	 * @Description 批量审核
	 * @author yakun.bai
	 * @Date 2015-12-9
	 */
	@ResponseBody
	@RequestMapping(value = "/batchReview")
	public Map<String, String> batchReview(HttpServletRequest request) {

		Map<String, String> result = Maps.newHashMap();
		
		// 审核ID(ID@订单类型)
		String revids = request.getParameter("revids");
		// 备注
		String remark = request.getParameter("remark");
		// 审核通过或驳回标识
		String flag = request.getParameter("result");
		
		// 校验为空
		if (StringUtils.isNotBlank(revids) && StringUtils.isNotBlank(flag)) {
			result = rebatesReviewService.batchReview(revids, remark, flag, request);
		} else {
			result.put("result", "error");
			result.put("msg", "参数错误");
		}
		
		return result;
	}
	
	/**
	 * @Description 返佣打印---仅供返佣审批页面使用
	 * @author yakun.bai
	 * @Date 2015-12-11
	 */
	@RequestMapping(value = "groupRebatesPrintforReview")
	public String groupRebatesPrintforReview(Model model, HttpServletRequest request) {

		String reviewId = request.getParameter("reviewId");		//返佣申请审核ID
		String groupCode = request.getParameter("groupCode");	//团号
		
		Map<String,Object> map = rebatesReviewService.buildPrintData(reviewId);
		map.put("reviewId", reviewId);
		map.put("groupCode", groupCode);
		
		model.addAttribute("map", map);
	
		return "/review/rebates/common/groupRebatesPrintforReview";
	}
	
	
	/**
	 * @Description 返佣打印
	 * @author yakun.bai
	 * @Date 2015-12-11
	 */
	@RequestMapping(value = "rebatesReviewPrint")
	public String rebatesReviewPrint(Model model, HttpServletRequest request) {

		String reviewId = request.getParameter("reviewId");		//返佣申请审核ID
		String groupCode = request.getParameter("groupCode");	//团号
		String payId = request.getParameter("payId");           //refund id
		
		Map<String,Object> map = rebatesReviewService.buildPrintData(reviewId, payId);
		map.put("reviewId", reviewId);
		map.put("groupCode", groupCode);
		map.put("payId", payId);
		
		model.addAttribute("map", map);
	
		return REBATES_REVIEW_PRINT;
	}
	
	
	/**
	 * @Description 下载支出凭单---仅供返佣审批页面使用
	 * @author yakun.bai
	 * @Date 2015-12-11
	 */
	@RequestMapping(value="groupRebatesDownloadforReview")
	public ResponseEntity<byte[]> groupRebatesDownloadforReview(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String reviewId = request.getParameter("reviewId");		//返佣申请审核ID
		String groupCode = request.getParameter("groupCode");	//团号
		
		Map<String,Object> map = rebatesReviewService.buildPrintData(reviewId);
		map.put("reviewId", reviewId);
		map.put("groupCode", groupCode);
		map.put("createDate", DateUtils.formatDate((Date)map.get("createDate"), "yyyy年 MM月dd日 "));
		
		//更新打印时间
		this.updatePrintTime(reviewId);
		
		File file = rebatesNewService.createRebatesSheetDownloadFile(map);
		
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName =  "支出凭单" + nowDate + ".doc";
		OutputStream os = null;
    	try {
			if (file != null && file.exists()) {
				response.reset();
				response.setHeader("Content-Disposition", "attachment; filename="+new String(fileName.getBytes("gb2312"), "ISO-8859-1"));
				response.setContentType("application/octet-stream; charset=utf-8");
		    	os = response.getOutputStream();
				os.write(FileUtils.readFileToByteArray(file));
	            os.flush();
			}       		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null)
				try {
					os.close();
				} catch (Exception e) {
					os.close();
				}
		}
		return null;
	}
	
	/**
	 * @Description 下载支出凭单
	 * @author yakun.bai
	 * @Date 2015-12-11
	 */
	@RequestMapping(value="rebatesReviewDownload")
	public ResponseEntity<byte[]> downloadBorrowMoneySheet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String reviewId = request.getParameter("reviewId");		//返佣申请审核ID
		String groupCode = request.getParameter("groupCode");	//团号
		String payId = request.getParameter("payId");
		
		Map<String,Object> map = rebatesReviewService.buildPrintData(reviewId,payId);
		map.put("reviewId", reviewId);
		map.put("groupCode", groupCode);
		map.put("createDate", DateUtils.formatDate((Date)map.get("createDate"), "yyyy年 MM月dd日 "));
		
		//更新打印时间
		this.updatePrintTime(reviewId);
		
		File file = rebatesNewService.createRebatesSheetDownloadFile(map);
		
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName =  "支出凭单" + nowDate + ".doc";
		OutputStream os = null;
    	try {
			if (file != null && file.exists()) {
				response.reset();
				response.setHeader("Content-Disposition", "attachment; filename="+new String(fileName.getBytes("gb2312"), "ISO-8859-1"));
				response.setContentType("application/octet-stream; charset=utf-8");
		    	os = response.getOutputStream();
				os.write(FileUtils.readFileToByteArray(file));
	            os.flush();
			}       		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null)
				try {
					os.close();
				} catch (Exception e) {
					os.close();
				}
		}
		return null;
	}
	
	/**
	 * @Description 更新打印时间
	 * @author yakun.bai
	 * @Date 2015-12-11
	 */
	@RequestMapping(value="updatePrintTime")
	@ResponseBody
	public String  updatePrintTime(String reviewId) {
		ReviewNew review = reviewService.getReview(reviewId);
		if (review.getPrintStatus() == null || review.getPrintStatus() == 0) {
			 reviewService.updatePrintFlag(UserUtils.getUser().getId().toString(), "1", reviewId);
		}
		return "success";
	}
	/***************************************************
	 * @Description:处理返佣审核下的签证产品打印请求(新).
	 * @author: Admin
	 * @Date:2015-12-11 17:26
	 * @return:跳转到支出凭单页面.
	 ****************************************************/
	@RequestMapping("rebatesReviewPrint4forVisaProduct")
	public String RebatesReviewPrint4VisaProduct(Model model,
			HttpServletRequest request, HttpServletResponse response) {
			/*
			 * 填写日期取签务（计调人员）发起借款申请的日期；（ok） 
			 * 借款单位指签务（计调人员）所在部门；（ok） ---默认 签证部（暂不从系统取） 
			 * 经办人指申请人；（ok） 
			 * 审核暂时为空； ----------- 
			 * 财务主管暂时为空；-----------  
			 * 复核（审批）指最后一个审批人； （ok） 
			 * 借款金额人民币大写；（ok）  
			 * 领款人指申请人；（ok）  
			 * 主管审批（总经理）指最后一个审批人； （ok） 
			 * 出纳暂时为空；------------ 
			 * 确认付款日期即财务人员最后确认付款的日期； （ok）
			 */
		    String companyUuid = UserUtils.getUser().getCompany().getUuid();
			String revid = request.getParameter("reviewId");
			String payId = request.getParameter("payId");
			
			model.addAttribute("revid", revid);//把revid 传到模板模板的html页面，下载模板时使用
			model.addAttribute("payId", payId);
			model.addAttribute("costname",  "报销");//款项
			
			// 签证借款申请相关信息
			Map<String,Object>  reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(revid);			
			if (reviewAndDetailInfoMap != null) {				
				//首次打印时间
				if ("0".equals(reviewAndDetailInfoMap.get("printStatus").toString())) { 
					Date printDate = new Date();
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
					String printDateStr = simpleDateFormat.format(printDate);
					model.addAttribute("printDate",printDateStr );
				}else {
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
					String printDateStr = simpleDateFormat.format(reviewAndDetailInfoMap.get("printDate"));
					model.addAttribute("printDate",printDateStr);
				}
				//填写日期
				model.addAttribute("revCreateDate",reviewAndDetailInfoMap.get("createDate"));
				//团号
				String orderid = reviewAndDetailInfoMap.get("orderId").toString();
				VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));

				VisaProducts visaProducts =  visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
				
				// C460V3 签证订单团号统一取点单所关联的产品团号
				//model.addAttribute("ordergroupcode", visaOrder.getGroupCode());// 团号
				if(Context.SUPPLIER_UUID_HQX.equals(companyUuid)){
					// 针对环球行用户，团号规则改成C460V3之前的取值 update by shijun.liu 2016.05.12
					model.addAttribute("ordergroupcode", visaOrder.getGroupCode());// 团号
					model.addAttribute("groupCodeName", "订单团号");
				}else{
					model.addAttribute("ordergroupcode", visaProducts.getGroupCode());// 团号
					model.addAttribute("groupCodeName", "团号");
				}

				//经办人
				String productCreater = visaProducts.getCreateBy().getName();
				model.addAttribute("productCreater", productCreater); //经办人
				//领款人
				User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy").toString());
				if (null != user) {
					model.addAttribute("operatorName", user.getName());// 经办人、领款人都为借款申请人
				} else {
					model.addAttribute("operatorName", "未知");
				}
				//摘要
				String trvrebatesnotes = reviewAndDetailInfoMap.get("trvrebatesnotes").toString();
				String groupcurrencyMarks = reviewAndDetailInfoMap.get("grouprebatesnodes").toString();
				String remark = (trvrebatesnotes+groupcurrencyMarks).replace("#@!#!@#", " ");
				model.addAttribute("remark", remark);
				//确认付款日期
				model.addAttribute("revUpdateDate",reviewAndDetailInfoMap.get("updateDate"));
				
				//----- wxw added 20151008 -----单需求C221， 新行者签证借款付款状态，payStatus：1 显示upDateDate时间，0不显示
				//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
				String  payStatus = reviewAndDetailInfoMap.get("payStatus").toString();
				if (Context.SUPPLIER_UUID_HQX.equals(companyUuid)||Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
					model.addAttribute("payStatus","0");
				}else {
					model.addAttribute("payStatus",payStatus);
				}
			}
			
			Map<String, BigDecimal> rateMap = Maps.newHashMap();
			List<RebatesNew> rebatesList = rebatesNewService.findRebatesListByRid(revid);
			if(CollectionUtils.isNotEmpty(rebatesList)) {
				for(RebatesNew rebates : rebatesList) {
					String currencyMark = rebates.getCurrency().getCurrencyMark().trim();   //币种标识
					if(!rateMap.containsKey(currencyMark)) {
						//-----解决bug#13724中,当人民币对应的汇率为空时,报null异常---start------------
						if ("¥".equals(currencyMark)) {
							rateMap.put(currencyMark, new BigDecimal(1));
						}else{
							rateMap.put(currencyMark, rebates.getCurrencyExchangerate());
						}
						//-----解决bug#13724中,当人民币对应的汇率为空时,报null异常---end------------
					}
				}
			}
			
			//45需求，凭单中的金额以每次支付的金额为准
			List<Object[]> moneys = null;
			PayInfoDetail payDetail = null;
			if(StringUtils.isNotBlank(payId)) {
				payDetail = refundService.getPayInfoByPayId(payId, Context.PRODUCT_TYPE_QIAN_ZHENG.toString());
				if(payDetail != null && StringUtils.isNotBlank(payDetail.getMoneyDispStyle())) {
					moneys = MoneyNumberFormat.getMoneyFromString(payDetail.getMoneyDispStyle(), "\\+");
				}
			}
			if(CollectionUtils.isNotEmpty(moneys)) {
				BigDecimal totalRMBMoney = new BigDecimal(0);
				for(Object[] money : moneys) {
					String currencyMark = money[0].toString();
					if(rateMap.containsKey(currencyMark)){
						Object obj = money[1] != null ? money[1] : 0;
						BigDecimal RMBMoney = rateMap.get(currencyMark).multiply(new BigDecimal(Double.valueOf(obj.toString())));
						totalRMBMoney = totalRMBMoney.add(RMBMoney);	
					}else {
						Currency currency = currencyService.findCurrencyByCurrencyMark(currencyMark,UserUtils.getUser().getCompany().getId());
						BigDecimal rate = null==currency? new BigDecimal(0):currency.getCurrencyExchangerate();
						BigDecimal RMBMoney = rate.multiply(new BigDecimal(Double.valueOf(money[1].toString())));
						totalRMBMoney = totalRMBMoney.add(RMBMoney);						
					}
				}
				DecimalFormat myformat = new DecimalFormat();
				myformat.applyPattern("##,###.00");
				model.addAttribute("payRebatesAmount", MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO));
				model.addAttribute("currencyName", "人民币");
				model.addAttribute("currencyExchangerate", "1.0000");//汇率默认为1
				
				model.addAttribute("rebatesAmount", MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO));// 返佣金额
				model.addAttribute("rebatesAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.POINT_TWO))));// 返佣金额大写
				
			}else {
				model.addAttribute("currencyExchangerate", "");
				model.addAttribute("payRebatesAmount", "");
				model.addAttribute("currencyName", "");
				
				model.addAttribute("rebatesAmount", "");// 返佣金额
				model.addAttribute("rebatesAmountDx", "");// 返佣金额大写
			}
			
			//----0419需求---djw-----------
			model.addAttribute("payType", payDetail.getPayType());
			
			//渠道名称或者供应商名称
			String orderCompanyName = "";
			String accountName = "";  //账户名称
			//判断是供应商还是渠道商
			String relatedObjectType = objToString(reviewAndDetailInfoMap.get("relatedObjectType"));
			if(StringUtils.isNotBlank(relatedObjectType) && relatedObjectType.equals("2")) {    //供应商
				orderCompanyName = objToString(reviewAndDetailInfoMap.get("relatedObjectName"));
				String supplierId = objToString(reviewAndDetailInfoMap.get("relatedObject"));     //供应商id 
				String bankType = objToString(reviewAndDetailInfoMap.get(Context.REBATES_OBJECT_ACCOUNT_TYPE));   //境内账户or境外账户
				String bankName = objToString(reviewAndDetailInfoMap.get(Context.REBATES_OBJECT_ACCOUNT_BANK));   //银行名称
				String bankAccount = objToString(reviewAndDetailInfoMap.get(Context.REBATES_OBJECT_ACCOUNT_CODE));   //银行账号
				accountName = bankInfoService.getAccountName(Long.valueOf(supplierId), Context.PLAT_TYPE_SUP, bankName, bankAccount, bankType);
			}else {
				if (StringUtils.isNotBlank(objToString(reviewAndDetailInfoMap.get("agent")))) {
					Agentinfo agentInfo = agentInfoService.findAgentInfoById(Long.parseLong(objToString(reviewAndDetailInfoMap.get("agent"))));
					if (agentInfo != null) {
						orderCompanyName = agentInfo.getAgentName();
						accountName = bankInfoService.getAccountName(agentInfo.getId(), Context.PLAT_TYPE_QD, payDetail==null? "":payDetail.getTobankName(), payDetail==null? "":payDetail.getTobankAccount(), "");
					}
				}	
			}
			
			model.addAttribute("orderCompanyName", orderCompanyName);
			model.addAttribute("accountName", accountName);
			
			//获取单据审批人员Map
	        String companyUUid = UserUtils.getUser().getCompany().getUuid();	        
	        model.addAttribute("companyUUid", companyUUid);
	        MultiValueMap<Integer, User> valueMap = reviewReceiptService.obtainReviewer4Receipt(companyUUid, ReviewReceiptContext.RECEIPT_TYPE_PAYMENT, revid);//e5dbd01ec2f649e39d458540a91aa03b	        
	        List<User> executives = valueMap.get(ReviewReceiptContext.PaymentReviewElement.EXECUTIVE);//主管审批
	        List<User> general_managers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.GENERAL_MANAGER);//总经理
	        List<User> financial_executives = valueMap.get(ReviewReceiptContext.PaymentReviewElement.FINANCIAL_EXECUTIVE);//财务主管
	        List<User> cashiers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.CASHIER);//出纳
	        List<User> reviewers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.REVIEWER);//审核
	        
	        String  executive = getNames(executives);//主管审批
	        String general_manager = getNames(general_managers);//总经理 
	        String financial_executive = getNames(financial_executives);//财务主管
	        String cashier = getNames(cashiers);//出纳
	        String  reviewer = getNames(reviewers);//审核
			
			model.addAttribute("cw", financial_executive);//财务
			model.addAttribute("jdmanager", executive);//主管审批	
			model.addAttribute("cashier", cashier);//出纳			
			model.addAttribute("majorCheckPerson",general_manager);//总经理			
		    model.addAttribute("deptmanager", executive);//审核  部门经理
		    model.addAttribute("reviewer",reviewer);		//审核者
																		
			return "review/rebates/common/visaRebatesReviewPrint_new";
		}
	
	/***************************************************
	 * @Description:处理返佣审核下的签证产品打印请求(新).仅供返佣审批中打印使用
	 * @author: Admin
	 * @Date:2015-12-11 17:26
	 * @return:跳转到支出凭单页面.
	 ****************************************************/
	@RequestMapping("visaRebatesPrintforReview")
	public String visaRebatesPrintforReview(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		/*
		 * 填写日期取签务（计调人员）发起借款申请的日期；（ok） 
		 * 借款单位指签务（计调人员）所在部门；（ok） ---默认 签证部（暂不从系统取） 
		 * 经办人指申请人；（ok） 
		 * 审核暂时为空； ----------- 
		 * 财务主管暂时为空；-----------  
		 * 复核（审批）指最后一个审批人； （ok） 
		 * 借款金额人民币大写；（ok）  
		 * 领款人指申请人；（ok）  
		 * 主管审批（总经理）指最后一个审批人； （ok） 
		 * 出纳暂时为空；------------ 
		 * 确认付款日期即财务人员最后确认付款的日期； （ok）
		 */
		String revid = request.getParameter("reviewId");
		String rebatesId=request.getParameter("rebatesId");
		model.addAttribute("revid", revid);//把revid 传到模板模板的html页面，下载模板时使用
		model.addAttribute("rebatesId",rebatesId);
		model.addAttribute("costname",  "报销");//款项
		
		// 签证借款申请相关信息
		Map<String,Object>  reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(revid);				
		if (reviewAndDetailInfoMap != null) {
			model.addAttribute("revCreateDate",reviewAndDetailInfoMap.get("createDate"));// 填写日期			
			//----- wxw added 20151008 -----单需求C221 ， 处理付款确认时间
			model.addAttribute("revUpdateDate",reviewAndDetailInfoMap.get("updateDate"));// 更新日期
			
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy").toString());
			
			/**
			 * 经办人显示应为产品发布人员
			 */
			String orderid = reviewAndDetailInfoMap.get("orderId").toString();
			VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
			model.addAttribute("ordergroupcode", visaOrder.getGroupCode());// 团号
			VisaProducts visaProducts =  visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			String productCreater = visaProducts.getCreateBy().getName();
			model.addAttribute("productCreater", productCreater); //经办人
					
			//--------------支持凭单摘要--------------
			String trvrebatesnotes = reviewAndDetailInfoMap.get("trvrebatesnotes").toString();
			String groupcurrencyMarks = reviewAndDetailInfoMap.get("grouprebatesnodes").toString();
			String remark = (trvrebatesnotes+groupcurrencyMarks).replace("#@!#!@#", " ");
			model.addAttribute("remark", remark); //摘要
			
			//--------------获取收款单位--------------
			String orderCompanyName = "";
			String agentId = reviewAndDetailInfoMap.get("agent").toString();
			if(StringUtils.isNotBlank(agentId)) {
				Agentinfo agentInfo = agentInfoService.findOne(Long.parseLong(agentId));
				if(agentInfo != null) {
					orderCompanyName = agentInfo.getAgentName();
				}					
			}
			model.addAttribute("orderCompanyName", orderCompanyName);
							
			if (null != user) {
				model.addAttribute("operatorName", user.getName());// 经办人、领款人都为借款申请人
			} else {
				model.addAttribute("operatorName", "未知");
			}
			model.addAttribute("payDate", reviewAndDetailInfoMap.get("updateDate"));// 付款日期
			
			model.addAttribute("currencyName", "人民币"); //币种中文名称
			model.addAttribute("currencyExchangerate", "1.0000"); //汇率
		}
		
		BigDecimal totalRMBMoney = new BigDecimal(0);
		List<RebatesNew> rebatesList = rebatesNewService.findRebatesListByRid(revid);
		if(CollectionUtils.isNotEmpty(rebatesList)) {
			for(RebatesNew rebates : rebatesList) {
				BigDecimal rebatesDiff = rebates.getRebatesDiff();
				BigDecimal exchangerate = rebates.getCurrencyExchangerate();
				if(exchangerate == null) {
					exchangerate = currencyService.findCurrency(rebates.getCurrencyId()).getCurrencyExchangerate();
				}
				totalRMBMoney = totalRMBMoney.add(rebatesDiff.multiply(exchangerate));
			}
		}
		model.addAttribute("rebatesAmount", MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO));// 返佣金额
		model.addAttribute("rebatesAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.POINT_TWO))));// 返佣金额大写
		
		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳 
		// 7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
		  /**
         * 通过性方式获取审核人职务 
         */
        String companyUUid = UserUtils.getUser().getCompany().getUuid();
        //获取单据审批人员Map
        MultiValueMap<Integer, User> valueMap = reviewReceiptService.obtainReviewer4Receipt(companyUUid, ReviewReceiptContext.RECEIPT_TYPE_PAYMENT, revid);//e5dbd01ec2f649e39d458540a91aa03b
        
        List<User> executives = valueMap.get(ReviewReceiptContext.PaymentReviewElement.EXECUTIVE);//主管审批
        List<User> general_managers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.GENERAL_MANAGER);//总经理
        List<User> financial_executives = valueMap.get(ReviewReceiptContext.PaymentReviewElement.FINANCIAL_EXECUTIVE);//财务主管
        List<User> cashiers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.CASHIER);//出纳
        List<User> reviewers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.REVIEWER);//审核
        
        String  executive = getNames(executives);//主管审批
        String general_manager = getNames(general_managers);//总经理 
        String financial_executive = getNames(financial_executives);//财务主管
        String cashier = getNames(cashiers);//出纳
        String  reviewer = getNames(reviewers);//审核
		
		model.addAttribute("cw", financial_executive);//财务		
		model.addAttribute("jdmanager", executive);//主管审批
		model.addAttribute("cashier", cashier);//出纳					
		model.addAttribute("majorCheckPerson",general_manager);//总经理		
	    model.addAttribute("deptmanager", executive);//审核  部门经理
		//审核者
	    model.addAttribute("reviewer",reviewer);
		
		if ("0".equals(reviewAndDetailInfoMap.get("printStatus").toString())) { 
			Date printDate = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
			String printDateStr = simpleDateFormat.format(printDate);
			model.addAttribute("printDate",printDateStr );
		}else {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
			String printDateStr = simpleDateFormat.format(reviewAndDetailInfoMap.get("printDate"));
			model.addAttribute("printDate",printDateStr);
		}
		
		//----- wxw added 20151008 -----单需求C221， 新行者签证借款付款状态，payStatus：1 显示upDateDate时间，0不显示
		//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		String  payStatus = reviewAndDetailInfoMap.get("payStatus").toString();
		if (Context.SUPPLIER_UUID_LMT.equals(companyUuid) || Context.SUPPLIER_UUID_HQX.equals(companyUuid)) {
			model.addAttribute("payStatus","0");
		}else {
			model.addAttribute("payStatus",payStatus);
		}
		
		//环球行团号改为订单团号
		if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())) {
			model.addAttribute("groupCodeName", "订单团号");
		}else {
			model.addAttribute("groupCodeName", "团号");
		}
				
		return "review/rebates/common/visaRebatesPrintforReview";
	}
		
	
	
	 /**
     * 获取user的名称
     * @param Users
     * @return
     */
    private String getNames(List<User> users) {
        String res = " ";
        int n = 0;
        if(users == null || users.size() == 0){
            return res;
        }
        for(User user : users){
            if(n==0){
                res = res.trim();
                res += user.getName();
                n++;
            } else {
                res += "," + user.getName();
            }
        }
        return res;
    }
	
	/***********************************************
	 * 签证返佣支出凭单-打印功能(新)-for visa product
	 **********************************************/
	@RequestMapping(value = "visaRebatesReviewPrintAjax_new")
	@ResponseBody
	public Map<String, Object> visaRebatesReviewPrintAjax_new(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
	    
	    String revid = request.getParameter("revid");
		String printDatestr = request.getParameter("printDate");
		
		Map<String,Object>  reviewAndDetailInfoMap = null;
		try{
			if(revid!=null){
				reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(revid);
			}
		}catch(Exception e){
			log.error("根据reviewid： " + revid + " 查询出来reviewDetail明细报错 ",e);
		}
		                  
		if ("0".equals(reviewAndDetailInfoMap.get("printStatus").toString())) { 
			try {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
				Date printDate = simpleDateFormat.parse(printDatestr);
			    //TODO 未完成的,不知应该调用哪个方法
				processReviewService.updatePrintFlag(UserUtils.getUser().getId().toString(), "1", revid, printDate);
			} catch (NumberFormatException e) {
				e.printStackTrace();
				log.error("签证返佣支出凭单日期格式化错", e);
				throw e;
			} catch (Exception e) {
				map.put("result",2);
				e.printStackTrace();			
			}
		}
		map.put("result",1);//1成功   2为申请失败
		
		return map;
	}
	
	/*******************************************
	    * 签证返佣支出凭单-下载功能(新)-for visa product  仅供返佣审批页面使用
	    *******************************************/
	@RequestMapping(value="visaRebatesDownloadforReview")
	public ResponseEntity<byte[]> visaRebatesDownloadforReview(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception{
		String revid = request.getParameter("reviewId");
		File file = rebatesReviewNewService.createRebatesReviewSheetDownloadFile4Visa(revid);		
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName =  "签证返佣支出凭单" + nowDate + ".doc";
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
	
   /*******************************************
    * 签证返佣支出凭单-下载功能(新)-for visa product
    *******************************************/
	@RequestMapping(value="downloadRebatesPrintSheet_new")
	public ResponseEntity<byte[]> downloadRebatesPrintSheet_new(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception{
		
		
		String revid = request.getParameter("reviewId");
		String payId = request.getParameter("payId");
		File file = rebatesReviewNewService.createRebatesReviewSheetDownloadFile4Visa(revid, payId);
		
		//签证费借款单生成后,更新借款单 的  打印状态和首次打印时间
/*		if(file != null && file.exists()){
			visaBorrowMoneyService.updateReviewPrintInfoById(Long.parseLong(revid));
		} */
		
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName =  "签证返佣支出凭单" + nowDate + ".doc";
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
	 * @Description 把对象转为字符串，如果对象为空则返回空字符串
	 * @author xianglei.dong
	 * @copy from yakun.bai
	 * @Date 2016-04-27
	 */
	private String objToString(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "";
		}
	}
	
	
	/**
	 * @Description 返佣批量打印
	 * @author chao.zhang
	 * @Date 2016-10-27
	 */
	@RequestMapping(value = "batchGroupRebatesPrintforReview")
	public String batchGroupRebatesPrintforReview(Model model, HttpServletRequest request) {
		String datas = request.getParameter("params");
		JSONArray array = JSONArray.parseArray(datas);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> results = new ArrayList<Map<String,Object>>();
		for(int i = 0 ; i < array.size() ; i ++){
			JSONObject object = array.getJSONObject(i);
			Map<String,Object> map = new HashMap<String, Object>();
			Map<String,Object> resultMap = new HashMap<String, Object>();
			resultMap.put("reviewFlag", object.getString("reviewFlag"));
			resultMap.put("revstatus", object.getInteger("revstatus"));
			resultMap.put("productType", object.getInteger("productType"));
			resultMap.put("reviewId", object.getString("reviewId"));
			if(object.getString("reviewFlag").equals("2")){
				//团期类
				if(object.getInteger("revstatus") == 2 && object.getInteger("productType") != 6 && object.getInteger("productType") != 7 
						&& object.getInteger("productType") != 11 && object.getInteger("productType") != 12 ){
					String reviewId = object.getString("reviewId");		//返佣申请审核ID
					String groupCode = object.getString("groupCode");	//团号
					map = rebatesReviewService.buildPrintData(reviewId);
					map.put("reviewId", reviewId);
					map.put("groupCode", groupCode);
				}
				//机票
				if((object.getInteger("revstatus") == 2 || object.getInteger("revstatus") == 3) && object.getInteger("productType") == 7){
					String reviewId = object.getString("reviewId");		//返佣申请审核ID
					String groupCode = object.getString("groupCode");	//团号
					
					map = newAirticketrebatesService.airticketRebatesPrintData(reviewId);
					map.put("reviewId", reviewId);
					map.put("groupCode", groupCode);
				}
				//签证
				if((object.getInteger("revstatus") == 2 || object.getInteger("revstatus") == 3) && object.getInteger("productType") == 6){
					/*
					 * 填写日期取签务（计调人员）发起借款申请的日期；（ok） 
					 * 借款单位指签务（计调人员）所在部门；（ok） ---默认 签证部（暂不从系统取） 
					 * 经办人指申请人；（ok） 
					 * 审核暂时为空； ----------- 
					 * 财务主管暂时为空；-----------  
					 * 复核（审批）指最后一个审批人； （ok） 
					 * 借款金额人民币大写；（ok）  
					 * 领款人指申请人；（ok）  
					 * 主管审批（总经理）指最后一个审批人； （ok） 
					 * 出纳暂时为空；------------ 
					 * 确认付款日期即财务人员最后确认付款的日期； （ok）
					 */
					String revid = object.getString("reviewId");
					String rebatesId = "";
					if(StringUtils.isNotBlank(object.getString("rebatesId"))){
						rebatesId = object.getString("rebatesId").split("_")[1];
					}
					map.put("revid", revid);//把revid 传到模板模板的html页面，下载模板时使用
					map.put("rebatesId",rebatesId);
					map.put("costname",  "报销");//款项
					
					// 签证借款申请相关信息
					Map<String,Object>  reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(revid);				
					if (reviewAndDetailInfoMap != null) {
						map.put("revCreateDate",reviewAndDetailInfoMap.get("createDate"));// 填写日期			
						//----- wxw added 20151008 -----单需求C221 ， 处理付款确认时间
						map.put("revUpdateDate",reviewAndDetailInfoMap.get("updateDate"));// 更新日期
						
						User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy").toString());
						
						/**
						 * 经办人显示应为产品发布人员
						 */
						String orderid = reviewAndDetailInfoMap.get("orderId").toString();
						VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
						map.put("ordergroupcode", visaOrder.getGroupCode());// 团号
						VisaProducts visaProducts =  visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
						String productCreater = visaProducts.getCreateBy().getName();
						map.put("productCreater", productCreater); //经办人
								
						//--------------支持凭单摘要--------------
						String trvrebatesnotes = reviewAndDetailInfoMap.get("trvrebatesnotes").toString();
						String groupcurrencyMarks = reviewAndDetailInfoMap.get("grouprebatesnodes").toString();
						String remark = (trvrebatesnotes+groupcurrencyMarks).replace("#@!#!@#", " ");
						map.put("remark", remark); //摘要
						
						//--------------获取收款单位--------------
						String orderCompanyName = "";
						String agentId = reviewAndDetailInfoMap.get("agent").toString();
						if(StringUtils.isNotBlank(agentId)) {
							Agentinfo agentInfo = agentInfoService.findOne(Long.parseLong(agentId));
							if(agentInfo != null) {
								orderCompanyName = agentInfo.getAgentName();
							}					
						}
						map.put("orderCompanyName", orderCompanyName);
										
						if (null != user) {
							map.put("operatorName", user.getName());// 经办人、领款人都为借款申请人
						} else {
							map.put("operatorName", "未知");
						}
						map.put("payDate", reviewAndDetailInfoMap.get("updateDate"));// 付款日期
						
						map.put("currencyName", "人民币"); //币种中文名称
						map.put("currencyExchangerate", "1.0000"); //汇率
					}
					
					BigDecimal totalRMBMoney = new BigDecimal(0);
					List<RebatesNew> rebatesList = rebatesNewService.findRebatesListByRid(revid);
					if(CollectionUtils.isNotEmpty(rebatesList)) {
						for(RebatesNew rebates : rebatesList) {
							BigDecimal rebatesDiff = rebates.getRebatesDiff();
							BigDecimal exchangerate = rebates.getCurrencyExchangerate();
							if(exchangerate == null) {
								exchangerate = currencyService.findCurrency(rebates.getCurrencyId()).getCurrencyExchangerate();
							}
							totalRMBMoney = totalRMBMoney.add(rebatesDiff.multiply(exchangerate));
						}
					}
					map.put("rebatesAmount", MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO));// 返佣金额
					map.put("rebatesAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.POINT_TWO))));// 返佣金额大写
					
					// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳 
					// 7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
					  /**
			         * 通过性方式获取审核人职务 
			         */
			        String companyUUid = UserUtils.getUser().getCompany().getUuid();
			        //获取单据审批人员Map
			        MultiValueMap<Integer, User> valueMap = reviewReceiptService.obtainReviewer4Receipt(companyUUid, ReviewReceiptContext.RECEIPT_TYPE_PAYMENT, revid);//e5dbd01ec2f649e39d458540a91aa03b
			        
			        List<User> executives = valueMap.get(ReviewReceiptContext.PaymentReviewElement.EXECUTIVE);//主管审批
			        List<User> general_managers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.GENERAL_MANAGER);//总经理
			        List<User> financial_executives = valueMap.get(ReviewReceiptContext.PaymentReviewElement.FINANCIAL_EXECUTIVE);//财务主管
			        List<User> cashiers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.CASHIER);//出纳
			        List<User> reviewers = valueMap.get(ReviewReceiptContext.PaymentReviewElement.REVIEWER);//审核
			        
			        String  executive = getNames(executives);//主管审批
			        String general_manager = getNames(general_managers);//总经理 
			        String financial_executive = getNames(financial_executives);//财务主管
			        String cashier = getNames(cashiers);//出纳
			        String  reviewer = getNames(reviewers);//审核
					
			        map.put("cw", financial_executive);//财务		
			        map.put("jdmanager", executive);//主管审批
			        map.put("cashier", cashier);//出纳					
			        map.put("majorCheckPerson",general_manager);//总经理		
			        map.put("deptmanager", executive);//审核  部门经理
					//审核者
			        map.put("reviewer",reviewer);
					
					if ("0".equals(reviewAndDetailInfoMap.get("printStatus").toString())) { 
						Date printDate = new Date();
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
						String printDateStr = simpleDateFormat.format(printDate);
						map.put("printDate",printDateStr );
						resultMap.put("printDate", printDateStr);
					}else {
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
						String printDateStr = simpleDateFormat.format(reviewAndDetailInfoMap.get("printDate"));
						map.put("printDate",printDateStr);
						resultMap.put("printDate", printDateStr);
					}
					
					//----- wxw added 20151008 -----单需求C221， 新行者签证借款付款状态，payStatus：1 显示upDateDate时间，0不显示
					//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
					String companyUuid = UserUtils.getUser().getCompany().getUuid();
					String  payStatus = reviewAndDetailInfoMap.get("payStatus").toString();
					if (Context.SUPPLIER_UUID_LMT.equals(companyUuid) || Context.SUPPLIER_UUID_HQX.equals(companyUuid)) {
						map.put("payStatus","0");
					}else {
						map.put("payStatus",payStatus);
					}
					
					//环球行团号改为订单团号
					if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())) {
						map.put("groupCodeName", "订单团号");
					}else {
						map.put("groupCodeName", "团号");
					}
				}
			}else{
				//团期类
				if((object.getInteger("revstatus") == 2 || object.getInteger("revstatus") == 3)&& object.getInteger("productType") != 6 && object.getInteger("productType") != 7
						&& object.getInteger("productType") != 11 && object.getInteger("productType") != 12){
					String reviewId = object.getString("reviewId");		//返佣申请审核ID
					String payId = object.getString("payId");	//返佣申请ID
					String groupCode = object.getString("groupCode");	//团号
					String option = object.getString("option");
					
					//批量打印只做列表的 不用判断option
					map = rebatesService.buildPrintData(reviewId);
					
					map.put("reviewId", reviewId);
					map.put("payId", payId);
					map.put("groupCode", groupCode);
					map.put("option", option);
				}
				//机票类
				if((object.getInteger("revstatus") == 2 || object.getInteger("revstatus") == 3) && object.getInteger("productType") == 7){
					String reviewId = object.getString("reviewId");		//返佣申请审核ID
					String payId = object.getString("payId");	//返佣申请ID
					String option = object.getString("option");
					String groupCode = object.getString("groupCode");	//团号
					//批量打印只做列表的 不用判断option
					map = rebatesService.airticketRebatesPrintData(reviewId);
					
					map.put("reviewId", reviewId);
					map.put("payId", payId);
					map.put("groupCode", groupCode);
					map.put("option", option);
				}
				//签证类
				if((object.getInteger("revstatus") == 2 || object.getInteger("revstatus") == 3) && object.getInteger("productType") == 6){
					/*
					 * 填写日期取签务（计调人员）发起借款申请的日期；（ok） 
					 * 借款单位指签务（计调人员）所在部门；（ok） ---默认 签证部（暂不从系统取） 
					 * 经办人指申请人；（ok） 
					 * 审核暂时为空； ----------- 
					 * 财务主管暂时为空；-----------  
					 * 复核（审批）指最后一个审批人； （ok） 
					 * 借款金额人民币大写；（ok）  
					 * 领款人指申请人；（ok）  
					 * 主管审批（总经理）指最后一个审批人； （ok） 
					 * 出纳暂时为空；------------ 
					 * 确认付款日期即财务人员最后确认付款的日期； （ok）
					 */
					String revid = object.getString("reviewId");
					String payId = object.getString("payId");
					String option = object.getString("option");   //用于判断点击的是返佣列表中的打印按钮还是支付记录里面的打印按钮
					
					map.put("revid", revid);//把revid 传到模板模板的html页面，下载模板时使用
					map.put("payId", payId);
					map.put("option", option);
					map.put("costname",  "报销");//款项
							
					// 签证借款申请相关信息
					Map<String, String> reviewAndDetailInfoMap = ireviewService.findReview(Long.parseLong(revid));
					String remark1 = ireviewService.findRemark1(revid);
						
					if (reviewAndDetailInfoMap != null) {
						map.put("revCreateDate",DateUtils.dateFormat(reviewAndDetailInfoMap.get("createDate")));// 填写日期		
						//----- wxw added 20151008 -----单需求C221 ， 处理付款确认时间
						map.put("revUpdateDate",DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate")));// 更新日期
						
						User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy"));
						
						/**
						 * 经办人显示应为产品发布人员
						 */
						String orderid = reviewAndDetailInfoMap.get("orderId");
						VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
						map.put("ordergroupcode", visaOrder.getGroupCode());// 团号
						VisaProducts visaProducts =  visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
						String productCreater = visaProducts.getCreateBy().getName();
						map.put("productCreater", productCreater); //经办人
									
						//--------------支持凭单摘要--------------
						String trvrebatesnotes = reviewAndDetailInfoMap.get("trvrebatesnotes");
						String groupcurrencyMarks = reviewAndDetailInfoMap.get("grouprebatesnodes");
						String remark = (trvrebatesnotes+groupcurrencyMarks).replace("#@!#!@#", " ");
						map.put("remark", remark); //摘要
						map.put("remark1", remark1);
						
						//收款单位
						Agentinfo agentInfo = agentInfoService.findOne(visaOrder.getAgentinfoId());
						String orderCompanyName = agentInfo.getAgentName();
						if(StringUtils.isNotBlank(orderCompanyName)) {				
							map.put("orderCompanyName", orderCompanyName);	//渠道名称
						}else{
							map.put("orderCompanyName", "");
						}			
								
						if (null != user) {
							map.put("operatorName", user.getName());// 经办人、领款人都为借款申请人
						} else {
							map.put("operatorName", "未知");
						}
								
						/**
						 * 签证返佣支出凭单的汇率取成本录入的汇率,对应需求编号C139/C293
						 * wangxinwei  20151026 added
						 * 鉴于成本表中字段rate数据有空（null）的情况，如果成本的汇率为空时还按照原来的方式处理
						 */
						BigDecimal exchangerate = new BigDecimal(1);
						String currencyId = reviewAndDetailInfoMap.get("currencyId");
						List<CostRecord> costRecords = costManageService.findCostRecordList(Long.parseLong(revid));
						if (null!=costRecords&&costRecords.size()>0) {
							CostRecord costRecord = costRecords.get(0);
							if (null != currencyId) {
								Currency currency = currencyService.findCurrency(Long.parseLong(currencyId));
								map.put("currencyName", currency.getCurrencyName()); //币种中文名称
								if (null!=costRecord.getRate()) {
									exchangerate = costRecord.getRate();
								}else {
									exchangerate = currency.getCurrencyExchangerate();
								}
							}
						}else {
							if (null != currencyId) {
								Currency currency = currencyService.findCurrency(Long.parseLong(currencyId));
								exchangerate = currency.getCurrencyExchangerate();
								map.put("currencyName", currency.getCurrencyName()); //币种中文名称
							}
						}
						map.put("currencyExchangerate", MoneyNumberFormat.fmtMicrometer(exchangerate.toString(), "#,##0.0000")); //汇率
						
						//批量打印只做列表，不用判断option
						map.put("accountName", "");
						BigDecimal rebatesAmount = new BigDecimal(Double.valueOf(reviewAndDetailInfoMap.get("rebatesAmount")));
						map.put("payRebatesAmount", MoneyNumberFormat.getThousandsMoney(rebatesAmount.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO));
						BigDecimal totalRMB = exchangerate.multiply(rebatesAmount);
						map.put("rebatesAmount",MoneyNumberFormat.getThousandsMoney(totalRMB.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO));// 返佣金额
						map.put("rebatesAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(MoneyNumberFormat.getThousandsMoney(totalRMB.doubleValue(), 
								MoneyNumberFormat.POINT_TWO))));// 返佣金额大写
						
						//----- wxw added 20151008 -----单需求C221， 新行者签证借款付款状态，payStatus：1 显示upDateDate时间，0不显示
						//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
						String companyUuid = UserUtils.getUser().getCompany().getUuid();
						String payStatus = reviewAndDetailInfoMap.get("payStatus");
						if (Context.SUPPLIER_UUID_HQX.equals(companyUuid) || Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
							map.put("payStatus","0");
						}else {
							if(StringUtils.isNotBlank(payStatus)) {
								map.put("payStatus",payStatus);
							}else {
								map.put("payStatus","0");
							}
						}
					}
							
					//出纳以外的最后一个审批人：对签证借款流程来说level为3		
					List<ReviewLog> reviewLogs = reviewLogDao.findReviewLog(Long.parseLong(revid));				
					// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳 
					// 7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
					Map<Integer, String> jobtypeusernameMap =reviewCommonService.getReviewJobName(Context.REBATES_FLOW_TYPE,reviewLogs);		
					if (null!=jobtypeusernameMap.get(8)) {//财务
						map.put("cw", jobtypeusernameMap.get(8));
					}else {
						map.put("cw", "");
					}
					
					//lihong  123
					//2015-04-09王新伟添加
					/**
					 * 需求变更2015-04-22：如果为环球行用户出纳为空
					 */
					if (null!=jobtypeusernameMap.get(4)) {//主管审批
						if (68!=UserUtils.getUser().getCompany().getId()) {
							map.put("jdmanager", jobtypeusernameMap.get(4));
						}else {
							map.put("jdmanager", "");
						}
					}else {
						map.put("jdmanager", "");
					}
						
					if (null!=jobtypeusernameMap.get(6)) {//出纳
						if (68!=UserUtils.getUser().getCompany().getId()) {
							map.put("cashier", jobtypeusernameMap.get(6));
						}else {
							map.put("cashier", "");
						}
					}else {
						map.put("cashier", "");
					}
					
					if (null!=jobtypeusernameMap.get(10)) {//总经理
						map.put("majorCheckPerson", jobtypeusernameMap.get(10));
					}else {
						map.put("majorCheckPerson", "");
					}
					
					if (null!=jobtypeusernameMap.get(7)) {//部门经理
						map.put("deptmanager", jobtypeusernameMap.get(7));
					}else {
						map.put("deptmanager", "");
					}
					
					if (null!=jobtypeusernameMap.get(10)) {//总经理
						map.put("majorCheckPerson", jobtypeusernameMap.get(10));
					}else {
						map.put("majorCheckPerson", "");
					}
					
					if (null!=jobtypeusernameMap.get(7)) {//部门经理
						map.put("deptmanager", jobtypeusernameMap.get(7));
					}else {
						map.put("deptmanager", "");
					}
					
					Review review =  reviewDao.findOne(Long.parseLong(revid));
					if (null!=review&&(null==review.getPrintFlag() || 0 == review.getPrintFlag())) { 
						Date printDate = new Date();
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
						String printDateStr = simpleDateFormat.format(printDate);
						map.put("printDate",printDateStr );
						resultMap.put("printDate", printDateStr);
					}else {
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
						String printDateStr = simpleDateFormat.format(review.getPrintTime());
						map.put("printDate",printDateStr);
						resultMap.put("printDate", printDateStr);
					}
					
					//环球行用户将团号改为订单团号
					if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())) {
						map.put("groupCodeName", "订单团号");
					}else {
						map.put("groupCodeName", "团号");
					}
				}
			}
			map.put("reviewFlag", object.getString("reviewFlag"));
			map.put("revstatus", object.getInteger("revstatus"));
			map.put("prdtype", object.getInteger("productType"));
			list.add(map);
			results.add(resultMap);
		}
		
		model.addAttribute("list", list);
		model.addAttribute("results", JSON.toJSONString(results));
		
		return "/review/rebates/common/batchGroupRebatesPrintForReview";
	}
	
	/**
	 * @Description 批量更新打印时间
	 * @author yakun.bai
	 * @Date 2015-12-11
	 */
	@RequestMapping(value="batchUpdatePrintTime")
	@ResponseBody
	public String  batchUpdatePrintTime(String results) {
		JSONArray array = JSONArray.parseArray(results);
		for(int i = 0 ; i < array.size() ; i ++){
			JSONObject object = array.getJSONObject(i);
			String reviewId = object.getString("reviewId");
			if(object.getInteger("reviewFlag")==1){
				//签证
				if((object.getInteger("revstatus") == 2 || object.getInteger("revstatus") == 3) && object.getInteger("productType") == 6){
					String revid = reviewId;
					String printDatestr = object.getString("printDate");
					Review review =  reviewDao.findOne(Long.parseLong(revid));
					if (null!=review&&(null==review.getPrintFlag() || 0 == review.getPrintFlag())) { 		
						try {
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
							Date printDate = simpleDateFormat.parse(printDatestr);
							visaRebatesService.updateReviewPrintInfoById(Long.parseLong(revid),printDate);
						} catch (NumberFormatException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							log.error("签证返佣支出凭单日期格式化错", e);
							throw e;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				//机票
				if((object.getInteger("revstatus") == 2 || object.getInteger("revstatus") == 3) && object.getInteger("productType") == 7){
					Review review = ireviewService.findReviewInfo(Long.parseLong(reviewId));
					if (null != review && (null == review.getPrintFlag() || 0 == review.getPrintFlag())) { 
						Date printDate = new Date();
						ireviewService.updateReviewPrintInfoById(printDate,Long.parseLong(reviewId));
					}
				}
				//团期
				if((object.getInteger("revstatus") == 2 || object.getInteger("revstatus") == 3)&& object.getInteger("productType") != 6 && object.getInteger("productType") != 7
						&& object.getInteger("productType") != 11 && object.getInteger("productType") != 12){
					Review review = ireviewService.findReviewInfo(Long.parseLong(reviewId));
					if (null != review && (null == review.getPrintFlag() || 0 == review.getPrintFlag())) { 
						Date printDate = new Date();
						try {
							ireviewService.updateReviewPrintInfoById(printDate,Long.parseLong(reviewId));
						} catch (Exception e) {
							e.printStackTrace();
							logger.error("格式化日期错误", e);
							throw e;
						} 
					}
					
				}
			}else{
				//签证
				if((object.getInteger("revstatus") == 2 || object.getInteger("revstatus") == 3) && object.getInteger("productType") == 6){
					String revid = reviewId;
					String printDatestr = object.getString("printDate");
					
					Map<String,Object>  reviewAndDetailInfoMap = null;
					if(revid!=null){
						reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(revid);
					}
					                  
					if ("0".equals(reviewAndDetailInfoMap.get("printStatus").toString())) { 
						try {
							SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
							Date printDate = simpleDateFormat.parse(printDatestr);
						    //TODO 未完成的,不知应该调用哪个方法
							processReviewService.updatePrintFlag(UserUtils.getUser().getId().toString(), "1", revid, printDate);
						} catch (NumberFormatException e) {
							e.printStackTrace();
							log.error("签证返佣支出凭单日期格式化错", e);
							throw e;
						} catch (Exception e) {
							e.printStackTrace();			
						}
					}
				}
				//机票
				if((object.getInteger("revstatus") == 2 || object.getInteger("revstatus") == 3) && object.getInteger("productType") == 7){
					String userId = UserUtils.getUser().getId().toString();
					reviewService.updatePrintFlag(userId,"1",reviewId);
				}
				//团期
				if(object.getInteger("revstatus") == 2 && object.getInteger("productType") != 6 && object.getInteger("productType") != 7
						&& object.getInteger("productType") != 11 && object.getInteger("productType") != 12){
					ReviewNew review = reviewService.getReview(reviewId);
					if (review.getPrintStatus() == null || review.getPrintStatus() == 0) {
						 reviewService.updatePrintFlag(UserUtils.getUser().getId().toString(), "1", reviewId);
					}
				}
			}
		}
		return "success";
	}
}
