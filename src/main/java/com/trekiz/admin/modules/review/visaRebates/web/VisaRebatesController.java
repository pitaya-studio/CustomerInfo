package com.trekiz.admin.modules.review.visaRebates.web;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.service.PlatBankInfoService;
import com.trekiz.admin.modules.order.service.RefundService;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.review.deposittowarrantreview.service.DepositToWarrantReviewService;
import com.trekiz.admin.modules.review.visaRebates.service.IVisaRebatesService;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewLogDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;

/**
 * 签证返佣审核controller
* @ClassName: VisaRebatesController
* @Description: TODO
* @author jiachen
* @date 2015年6月2日 上午10:16:01
*
 */
@Controller
@RequestMapping("${adminPath}/review/visaRebates")
public class VisaRebatesController extends BaseController{
	
	private static final Logger log = LoggerFactory.getLogger(VisaRebatesController.class);

	@Autowired
	private ReviewCommonService reviewCommonService;
	@Autowired
	private DepositToWarrantReviewService depositToWarrantReviewService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private VisaOrderService visaOrderService;
	@Autowired
	private VisaProductsService visaProductsService;
	@Autowired
	private IVisaRebatesService visaRebatesService;
	@Autowired
	private ReviewDao reviewDao;
	@Autowired
	private ReviewLogDao reviewLogDao;
	@Autowired
	private CurrencyService currencyService;		
	@Autowired
	private CostManageService costManageService;
	@Autowired
	private RefundService refundService;
	@Autowired
	private AgentinfoService agentInfoService;
	@Autowired
	private PlatBankInfoService bankInfoService;
		
	/**
	 * 签证返佣审核列表
	 * @author jiachen
	 * @DateTime 2015年6月2日 上午10:15:36
	 * @param request
	 * @param model
	 * @return String
	 */
	@RequestMapping({"list",""})
	public String list(String reviewStatus, HttpServletRequest request, HttpServletResponse response, Model model) {
		//根据筛选条件查找当前用户权限列表
		Map<String, String> paramMap = new HashMap<String, String>();
		String flag = request.getParameter("flag");
		paramMap.put("o.order_no", request.getParameter("order_no"));
		paramMap.put("p.groupCode", request.getParameter("groupCode"));
		paramMap.put("o.agentinfo_id", request.getParameter("agent"));
		paramMap.put("o.create_by", request.getParameter("saler"));
		paramMap.put("p.createBy", request.getParameter("op"));
		paramMap.put("createByName", request.getParameter("createByName"));
		String orderCreateDateStart = request.getParameter("orderCreateDateStart") == null ? "" : request.getParameter("orderCreateDateStart");
		String orderCreateDateEnd = request.getParameter("orderCreateDateEnd") == null ? "" : request.getParameter("orderCreateDateEnd");
		paramMap.put("orderCreateDateStart", orderCreateDateStart);
		paramMap.put("orderCreateDateEnd", orderCreateDateEnd);
		
		String jobId = request.getParameter("jobId");
		//获取用户对于当前审批流程的审核职位
		List<UserJob> userJobList = reviewCommonService.getWorkFlowJobByFlowType(Context.REBATES_FLOW_TYPE);
		
		/*if(StringUtils.isBlank(jobId) && !userJobList.isEmpty()) {
			jobId = userJobList.get(userJobList.size()-1).getId().toString();
		}
		//查找属于当前用户审批的审批流
		Page<Map<String, Object>> page = depositToWarrantReviewService.findReviewInfoPage(
				new Page<Map<String, Object>>(request, response), jobId, reviewStatus, paramMap, Context.REBATES_FLOW_TYPE);*/
	
		List<UserJob> visaRebatesUserJobList = new ArrayList<UserJob>();
		for(UserJob job : userJobList) {
			if(Context.ORDER_TYPE_QZ.equals(job.getOrderType())) {
				visaRebatesUserJobList.add(job);
			}
		}
		
		
		if(StringUtils.isBlank(jobId) && !visaRebatesUserJobList.isEmpty()) {
			jobId = visaRebatesUserJobList.get(visaRebatesUserJobList.size()-1).getId().toString();
		}
		//查找属于当前用户审批的审批流
		Page<Map<String, Object>> page = depositToWarrantReviewService.findReviewInfoPage(
				new Page<Map<String, Object>>(request, response), jobId, reviewStatus, paramMap, Context.REBATES_FLOW_TYPE);
		
		model.addAttribute("userJobList", visaRebatesUserJobList);
		model.addAttribute("page", page);
		model.addAttribute("jobId", jobId);
		model.addAttribute("paramMap", paramMap);
		model.addAttribute("orderType",6);
		model.addAttribute("flag", flag);
		List<Map<String, Object>> createByNameList = visaOrderService.findVisaOrderCreateBy1();
		model.addAttribute("createByList", createByNameList);
		
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		
		return "modules/review/visaRebates/visaRebatesReviewList";
		
	}
	
	/**
	 * 签证返佣财务审核列表
	 * @DateTime 2015年7月30日
	 * @param request
	 * @param model
	 * @return String
	 */
	@RequestMapping({"financeList"})
	public String financeList(String reviewStatus, HttpServletRequest request, HttpServletResponse response, Model model) {
		//根据筛选条件查找当前用户权限列表
		Map<String, String> paramMap = new HashMap<String, String>();
		String flag = request.getParameter("flag");
		paramMap.put("o.order_no", request.getParameter("order_no"));
		paramMap.put("p.groupCode", request.getParameter("groupCode"));
		paramMap.put("o.agentinfo_id", request.getParameter("agent"));
		paramMap.put("o.create_by", request.getParameter("saler"));
		paramMap.put("p.createBy", request.getParameter("op"));
		//打印状态
		paramMap.put("r.printFlag", request.getParameter("printFlag"));
		//报批日期
		String reviewCreateDateStart = request.getParameter("reviewCreateDateStart") == null ? "" : request.getParameter("reviewCreateDateStart");
		String reviewCreateDateEnd = request.getParameter("reviewCreateDateEnd") == null ? "" : request.getParameter("reviewCreateDateEnd");
		paramMap.put("reviewCreateDateStart", reviewCreateDateStart);
		paramMap.put("reviewCreateDateEnd", reviewCreateDateEnd);
		//返佣差额
		paramMap.put("rebatesAmountStrat", request.getParameter("rebatesAmountStrat"));
		paramMap.put("rebatesAmountEnd", request.getParameter("rebatesAmountEnd"));
		paramMap.put("createByName", request.getParameter("createByName"));
		
		String jobId = request.getParameter("jobId");
		//获取用户对于当前审批流程的审核职位,默认查询一个当前职务的审批待审核
		List<UserJob> userJobList = reviewCommonService.getWorkFlowJobByFlowType(Context.REBATES_FLOW_TYPE);
		List<UserJob> visaRebatesUserJobList = new ArrayList<UserJob>();
		for(UserJob job : userJobList) {
			if(Context.ORDER_TYPE_QZ.equals(job.getOrderType())) {
				visaRebatesUserJobList.add(job);
			}
		}
		if(StringUtils.isBlank(jobId) && !visaRebatesUserJobList.isEmpty()) {
			jobId = visaRebatesUserJobList.get(visaRebatesUserJobList.size()-1).getId().toString();
		}
		//查找属于当前用户审批的审批流
		Page<Map<String, Object>> page = depositToWarrantReviewService.findReviewInfoPage(
				new Page<Map<String, Object>>(request, response), jobId, reviewStatus, paramMap, Context.REBATES_FLOW_TYPE);
		
		model.addAttribute("userJobList", visaRebatesUserJobList);
		model.addAttribute("page", page);
		model.addAttribute("jobId", jobId);
		model.addAttribute("paramMap", paramMap);
		model.addAttribute("orderType",6);
		model.addAttribute("flag", flag);
		List<Map<String, Object>> createByNameList = visaOrderService.findVisaOrderCreateBy1();
		model.addAttribute("createByList", createByNameList);
		return "modules/review/visaRebates/visaRebatesFinanceList";
		
	}
	
	/**
	 * 签证返佣审核审批操作
	 * @author jiachen
	 * @DateTime 2015年6月2日 上午11:09:52
	 * @param result
	 * @param request
	 * @return String
	 */
	@RequestMapping("dispose")
	public String reviewDispose(@RequestParam(value="result", required=true)String result,
			HttpServletRequest request) {
		
		String reviewId = request.getParameter("reviewId");
		String nowLevel = request.getParameter("nowLevel");
		String denyReason = request.getParameter("denyReason");
		String shenfen = request.getParameter("shenfen");
		
		int reviewResult = reviewService.UpdateReview(StringUtils.toLong(reviewId), StringUtils.toInteger(nowLevel), 
				StringUtils.toInteger(result), denyReason);
		
		Map<String,String> map = reviewService.findReview(StringUtils.toLong(reviewId));
		//审核成功状态
		if(reviewResult == 1){
			visaRebatesService.reviewSuccess(map);
		}
		//shenhe:跳转到审核模块下，caiwu:跳转到财务审核模块下
		if("shenhe".equals(shenfen)){
			return "redirect:"+Global.getAdminPath()+"/review/visaRebates";
		}else{
			return "redirect:"+Global.getAdminPath()+"/review/visaRebates/financeList";
		}
	}
	
	
	//----------------打印相关开始---------------------
	/**
	 * 
	 * wangxinwei 2015年06月09日9:41:00
	 */
	@RequestMapping(value = "visaRebatesReviewPrint")
	public String visaRebatesReviewPrint(Model model,
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
		String payId = request.getParameter("payId");
		String option = request.getParameter("option");   //用于判断点击的是返佣列表中的打印按钮还是支付记录里面的打印按钮
		
		model.addAttribute("revid", revid);//把revid 传到模板模板的html页面，下载模板时使用
		model.addAttribute("payId", payId);
		model.addAttribute("option", option);
		model.addAttribute("costname",  "报销");//款项
				
		// 签证借款申请相关信息
		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(revid));
		String remark1 = reviewService.findRemark1(revid);
			
		if (reviewAndDetailInfoMap != null) {
			model.addAttribute("revCreateDate",DateUtils.dateFormat(reviewAndDetailInfoMap.get("createDate")));// 填写日期		
			//----- wxw added 20151008 -----单需求C221 ， 处理付款确认时间
			model.addAttribute("revUpdateDate",DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate")));// 更新日期
			
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy"));
			
			/**
			 * 经办人显示应为产品发布人员
			 */
			String orderid = reviewAndDetailInfoMap.get("orderId");
			VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
			model.addAttribute("ordergroupcode", visaOrder.getGroupCode());// 团号
			VisaProducts visaProducts =  visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			String productCreater = visaProducts.getCreateBy().getName();
			model.addAttribute("productCreater", productCreater); //经办人
						
			//--------------支持凭单摘要--------------
			String trvrebatesnotes = reviewAndDetailInfoMap.get("trvrebatesnotes");
			String groupcurrencyMarks = reviewAndDetailInfoMap.get("grouprebatesnodes");
			String remark = (trvrebatesnotes+groupcurrencyMarks).replace("#@!#!@#", " ");
			model.addAttribute("remark", remark); //摘要
			model.addAttribute("remark1", remark1);
			
			//收款单位
			Agentinfo agentInfo = agentInfoService.findOne(visaOrder.getAgentinfoId());
			String orderCompanyName = agentInfo.getAgentName();
			if(StringUtils.isNotBlank(orderCompanyName)) {				
				model.addAttribute("orderCompanyName", orderCompanyName);	//渠道名称
			}else{
				model.addAttribute("orderCompanyName", "");
			}			
					
			if (null != user) {
				model.addAttribute("operatorName", user.getName());// 经办人、领款人都为借款申请人
			} else {
				model.addAttribute("operatorName", "未知");
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
					model.addAttribute("currencyName", currency.getCurrencyName()); //币种中文名称
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
					model.addAttribute("currencyName", currency.getCurrencyName()); //币种中文名称
				}
			}
			model.addAttribute("currencyExchangerate", MoneyNumberFormat.fmtMicrometer(exchangerate.toString(), "#,##0.0000")); //汇率
			
			//45需求，返佣金额以每次的支付记录为依据
			if("pay".equals(option)) {     //支付记录的打印按钮
				String payRebatesAmount = "";
				PayInfoDetail payDetail = refundService.getPayInfoByPayId(payId, costRecords.get(0).getOrderType().toString());
				if(payDetail != null) {
					List<Object[]> moneys = null;
					String money = payDetail.getMoneyDispStyle();
					if(StringUtils.isNotEmpty(money)) {
						moneys = MoneyNumberFormat.getMoneyFromString(money, "\\+");
						if(CollectionUtils.isNotEmpty(moneys)) {
							payRebatesAmount = moneys.get(0)[1].toString();
						}
					}
				}
				model.addAttribute("payRebatesAmount", MoneyNumberFormat.getThousandsMoney(Double.valueOf(payRebatesAmount), MoneyNumberFormat.THOUSANDST_POINT_TWO));
				BigDecimal totalRMB = exchangerate.multiply(new BigDecimal(Double.valueOf(payRebatesAmount)));
				model.addAttribute("rebatesAmount",MoneyNumberFormat.getThousandsMoney(totalRMB.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO));// 返佣金额
				model.addAttribute("rebatesAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(MoneyNumberFormat.getThousandsMoney(totalRMB.doubleValue(), 
						MoneyNumberFormat.POINT_TWO))));// 返佣金额大写
				
				//银行账户名
				String accountName="";
				if(agentInfo != null) {
					accountName = bankInfoService.getAccountName(agentInfo.getId(), Context.PLAT_TYPE_QD, payDetail==null? "":payDetail.getTobankName(), payDetail==null? "":payDetail.getTobankAccount(),"");
				}
				model.addAttribute("accountName", accountName);
			}else if("order".equals(option)) {
				model.addAttribute("accountName", "");
				BigDecimal rebatesAmount = new BigDecimal(Double.valueOf(reviewAndDetailInfoMap.get("rebatesAmount")));
				model.addAttribute("payRebatesAmount", MoneyNumberFormat.getThousandsMoney(rebatesAmount.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO));
				BigDecimal totalRMB = exchangerate.multiply(rebatesAmount);
				model.addAttribute("rebatesAmount",MoneyNumberFormat.getThousandsMoney(totalRMB.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO));// 返佣金额
				model.addAttribute("rebatesAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(MoneyNumberFormat.getThousandsMoney(totalRMB.doubleValue(), 
						MoneyNumberFormat.POINT_TWO))));// 返佣金额大写
			}
			
			//----- wxw added 20151008 -----单需求C221， 新行者签证借款付款状态，payStatus：1 显示upDateDate时间，0不显示
			//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
			String companyUuid = UserUtils.getUser().getCompany().getUuid();
			String payStatus = reviewAndDetailInfoMap.get("payStatus");
			if (Context.SUPPLIER_UUID_HQX.equals(companyUuid) || Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
				model.addAttribute("payStatus","0");
			}else {
				if(StringUtils.isNotBlank(payStatus)) {
					model.addAttribute("payStatus",payStatus);
				}else {
					model.addAttribute("payStatus","0");
				}
			}
		}
				
		//出纳以外的最后一个审批人：对签证借款流程来说level为3		
		List<ReviewLog> reviewLogs = reviewLogDao.findReviewLog(Long.parseLong(revid));				
		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳 
		// 7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
		Map<Integer, String> jobtypeusernameMap =reviewCommonService.getReviewJobName(Context.REBATES_FLOW_TYPE,reviewLogs);		
		if (null!=jobtypeusernameMap.get(8)) {//财务
			model.addAttribute("cw", jobtypeusernameMap.get(8));
		}else {
			model.addAttribute("cw", "");
		}
		
		//lihong  123
		//2015-04-09王新伟添加
		/**
		 * 需求变更2015-04-22：如果为环球行用户出纳为空
		 */
		if (null!=jobtypeusernameMap.get(4)) {//主管审批
			if (68!=UserUtils.getUser().getCompany().getId()) {
				model.addAttribute("jdmanager", jobtypeusernameMap.get(4));
			}else {
				model.addAttribute("jdmanager", "");
			}
		}else {
			model.addAttribute("jdmanager", "");
		}
			
		if (null!=jobtypeusernameMap.get(6)) {//出纳
			if (68!=UserUtils.getUser().getCompany().getId()) {
				model.addAttribute("cashier", jobtypeusernameMap.get(6));
			}else {
				model.addAttribute("cashier", "");
			}
		}else {
			model.addAttribute("cashier", "");
		}
		
		if (null!=jobtypeusernameMap.get(10)) {//总经理
			model.addAttribute("majorCheckPerson", jobtypeusernameMap.get(10));
		}else {
			model.addAttribute("majorCheckPerson", "");
		}
		
		if (null!=jobtypeusernameMap.get(7)) {//部门经理
			model.addAttribute("deptmanager", jobtypeusernameMap.get(7));
		}else {
			model.addAttribute("deptmanager", "");
		}
		
		if (null!=jobtypeusernameMap.get(10)) {//总经理
			model.addAttribute("majorCheckPerson", jobtypeusernameMap.get(10));
		}else {
			model.addAttribute("majorCheckPerson", "");
		}
		
		if (null!=jobtypeusernameMap.get(7)) {//部门经理
			model.addAttribute("deptmanager", jobtypeusernameMap.get(7));
		}else {
			model.addAttribute("deptmanager", "");
		}
		
		Review review =  reviewDao.findOne(Long.parseLong(revid));
		if (null!=review&&(null==review.getPrintFlag() || 0 == review.getPrintFlag())) { 
			Date printDate = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
			String printDateStr = simpleDateFormat.format(printDate);
			model.addAttribute("printDate",printDateStr );
		}else {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/ MM /dd HH:mm");
			String printDateStr = simpleDateFormat.format(review.getPrintTime());
			model.addAttribute("printDate",printDateStr);
		}
		
		//环球行用户将团号改为订单团号
		if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())) {
			model.addAttribute("groupCodeName", "订单团号");
		}else {
			model.addAttribute("groupCodeName", "团号");
		}
		
		return "modules/review/visaRebates/visaRebatesReviewPrint";
	}
    
	/**
	 * 签证费借款单 wangxinwei 2014年2月3日11:30:00
	 */
	@RequestMapping(value = "visaRebatesReviewPrintAjax")
	@ResponseBody
	public Map<String, Object> visaRebatesReviewPrintAjax(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String revid = request.getParameter("revid");
		String printDatestr = request.getParameter("printDate");
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
				map.put("result",2);
				e.printStackTrace();
				
			}
		}
		
		map.put("result",1);//1成功   2为申请失败
		
		return map;
	}
	
	
	@RequestMapping(value="downloadRebatesPrintSheet")
	public ResponseEntity<byte[]> downloadRebatesPrintSheet(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, Exception{
		
		String revid = request.getParameter("revid");
		String payId = request.getParameter("payId");
		String option = request.getParameter("option");
		File file = null;
		if("pay".equals(option)) {
			file = visaRebatesService.createRebatesReviewSheetDownloadFile(Long.parseLong(revid), payId);
		}else if("order".equals(option)) {
			file = visaRebatesService.createRebatesReviewSheetDownloadFile(revid);
		}
		
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
	
	//----------------打印相关结束---------------------
	
	/**
	 * 审核回退：根据revid进行批次回退操作,即返回上一级审核
	 * 规则如下：
	 * 1.第一层级 和 最后一层级没有退回操作
	 * 2.隔级审核后不能再进行退回操作
	 * 3.只有审核中状态的才可能进行退回操作
	 * wangxinwei 2015年06月01日21:23:00
	 */
	@RequestMapping(value = "visaRevatesCancelAjax")
	@ResponseBody
	public Map<String, Object> visaRevatesCancelAjax(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		String revid = request.getParameter("revid");
		try {
			
			//审核回退
		    if(null!=revid) {
		    	reviewService.CancelReview(Long.parseLong(revid));
			}
				
		}catch (Exception e) {
			map.put("result",2);
			e.printStackTrace();
			return map;
		}	
		map.put("result",1);//1成功   2为申请失败
		
		return map;
	}
	
	//---------------签证返佣批量审核  选择多项同时审核  开始  wxw added -------------------
	/**
	 * wxw added 2015-08-25
	 * 签证返佣审核  选择多项同时审核
	 * @param model
	 * @param request 
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "multiReviewVisaRebates")
	public Object multiReviewVisaRebates(Model model,HttpServletRequest request, HttpServletResponse response) {
		
        String result =  request.getParameter("result"); //1审核通过，0，驳回
		String remarks = request.getParameter("remarks");//批量审核驳回原因
		/**
		 * 参数结构如下：
		 * 1@110,1@112
		 * 说明：(当前审核层级@审核id,当前审核层级@审核id,......)
		 */
		String revids = request.getParameter("revids");
		String[] levelandrevids = revids.split(",");	
		
		
		for (int i = 0; i < levelandrevids.length; i++) {
			StringBuffer reply = new StringBuffer();
			if (result == null || "".equals(result)) {
				reply.append("审批结果不能为空");
			}
			
			String nowLevel = levelandrevids[i].split("@")[0];
			String revid = levelandrevids[i].split("@")[1];
			
			int reviewResult = reviewService.UpdateReview(StringUtils.toLong(revid), StringUtils.toInteger(nowLevel), 
					StringUtils.toInteger(result), remarks);
			
			//审核成功状态，如审核成功进行如下操作，与单条审批保持一致
			if(reviewResult == 1){
				Map<String,String> map = reviewService.findReview(StringUtils.toLong(revid));
				visaRebatesService.reviewSuccess(map);
			}
			
		}
		
		 Map<String, Object> resultMap = new HashMap<String, Object>();
		 resultMap.put("msg", "操作成功！");
		 return resultMap;
		
    }
	//---------------签证返佣批量审核  选择多项同时审核  结束  wxw added -------------------
	
}
