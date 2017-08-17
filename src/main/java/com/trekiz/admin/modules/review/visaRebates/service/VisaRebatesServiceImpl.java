package com.trekiz.admin.modules.review.visaRebates.service;


import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.service.PlatBankInfoService;
import com.trekiz.admin.modules.order.service.RefundService;
import com.trekiz.admin.modules.review.visaRebates.entity.MoreCurrencyComputePrice;
import com.trekiz.admin.modules.review.visaborrowmoney.web.VisaBorrowMoneyController;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewDetail;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDetailDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewLogDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.modules.visa.web.VisaPreOrderController;

import freemarker.template.TemplateException;

@Service
public class VisaRebatesServiceImpl implements IVisaRebatesService {

	private static SimpleDateFormat sdf=new SimpleDateFormat("yyyy 年 MM 月 dd 日");
	
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ReviewDetailDao reviewDetailDao;
	@Autowired
	private ReviewDao reviewDao;
	@Autowired
	private VisaOrderService visaOrderService;
	@Autowired
	private VisaProductsService visaProductsService;
	@Autowired
	private ReviewLogDao reviewLogDao;
	@Autowired
	private ReviewCommonService reviewCommonService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private MoneyAmountService moneyAmountService;	
	@Autowired
	private CostManageService costManageService;
	@Autowired
	private AgentinfoService agentInfoService;
	@Autowired
	private RefundService refundService;
	@Autowired
	private PlatBankInfoService bankInfoService;
	
	/**
	 * 签证返佣审核通过操作
	 */
	@Transient
	@SuppressWarnings("rawtypes")
	@Override
	public void reviewSuccess(Map<String,String> reviewMap) {
		
		MoreCurrencyComputePrice moreCur = null;
		//返佣累计，如果不是第一次申请的话，就把之前的值累加起来
		String totalRebatesJe = reviewMap.get("totalRebatesJe");
		if(!"0".equals(totalRebatesJe)) {
			Map<Object, Object> dataMap = new HashMap<Object, Object>();
			JSONObject jsonObject = JSONObject.fromObject(totalRebatesJe);
			Iterator it = jsonObject.keys();
			while (it.hasNext()) {
				String key = String.valueOf(it.next());  
				String value = (String) jsonObject.get(key);
				dataMap.put(key, value);
			}
			moreCur = new MoreCurrencyComputePrice(dataMap);
		}else{
			moreCur = new MoreCurrencyComputePrice();
		}
		
		//游客返佣
		//金额
		String trvaMounts = reviewMap.get("trvamounts");
		if(null != trvaMounts) {
			//币种
			String rebatestrvcurrents = reviewMap.get("rebatestrvcurrents");
			String[] trvaMountsArr = trvaMounts.split(VisaBorrowMoneyController.SPLITMARK);
			String[] trvaCurrenciesArr = rebatestrvcurrents.split(VisaBorrowMoneyController.SPLITMARK);
			for(int i = 0; i < trvaMountsArr.length; i++) {
				if(StringUtils.isNotBlank(trvaMountsArr[i])) {
					moreCur.addPrice(trvaCurrenciesArr[i], trvaMountsArr[i]);
				}
			}
		}
		
		//团队返佣
		//金额
		String groupRebatesaMounts = reviewMap.get("grouprebatesamounts");
		if(null != groupRebatesaMounts) {
			//币种
			String groupRebatesCurrencies = reviewMap.get("grouprebatescurrents");
			String[] groupMountsArr = groupRebatesaMounts.split(VisaBorrowMoneyController.SPLITMARK);
			String[] groupCurrenciesArr = groupRebatesCurrencies.split(VisaBorrowMoneyController.SPLITMARK);
			for(int i = 0; i < groupMountsArr.length; i++) {
				if(StringUtils.isNotBlank(groupMountsArr[i])) {
					moreCur.addPrice(groupCurrenciesArr[i], groupMountsArr[i]);
				}
			}
		}
		
        JSONObject object = JSONObject.fromObject(moreCur.getPriceMap());
        
        //存库
        List<ReviewDetail> detailList = reviewDetailDao.findReviewDetailByMykey(StringUtils.toLong(reviewMap.get("id")), "totalRebatesJe");
        ReviewDetail deatil = detailList.get(0);
        deatil.setMyvalue(object.toString().replace("\"", "'"));
        reviewDetailDao.save(deatil);
        
        //向money_amount表中插入数据
		MoneyAmount costMoneyAmount = new MoneyAmount(UUID.randomUUID().toString(), //款项UUID
				Integer.parseInt(reviewMap.get("currencyId")),//币种ID
				new BigDecimal(reviewMap.get("rebatesAmount")),//相应币种的金额
	    		Long.valueOf(reviewMap.get("orderid")), //订单或游客ID
	    		Context.MONEY_TYPE_FY, //款项类型: 返佣
	    		Context.ORDER_TYPE_QZ,//订单类型
	    		VisaPreOrderController.BUSINDESS_TYPE_ORDER,//1表示订单，2表示游客
	    		UserUtils.getUser().getId());//记录创建人ID, 这里用订单的ID
		costMoneyAmount.setReviewId(Long.parseLong(reviewMap.get("id")));
	    moneyAmountService.addMoneyAmount(costMoneyAmount);
	}

	/**
	 * 签证返佣审核通过操作(供审核公共接口调用)
	 */
	@Override
	public void reviewSuccess(Long reviewId) {
		if(null != reviewId) {
			Map<String,String> map = reviewService.findReview(reviewId);
			reviewSuccess(map);
		}
	}
	
	
	//-----------------------返佣打印相关开始----------------------
	/**
	 * 如果为第一次打印需要更新打印状态
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	@Override
	public void updateReviewPrintInfoById(Long revid,Date printdate) throws Exception{
		 Review review =  reviewDao.findOne((revid));
		 if (null!=review&&(null==review.getPrintFlag() || 0 == review.getPrintFlag())) { 
			 SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			 Date date = sf.parse(sf.format(printdate));
			 reviewDao.updateReviewPrintInfoById(revid, date, 1, UserUtils.getUser().getId());
		 }
	}
	
	/**
	 * 生成签证借款单文件
	 * @param revid
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	@Override
	public File createRebatesReviewSheetDownloadFile(Long revid, String payId) throws IOException, TemplateException{
		//word文档数据集合
		Map<String, Object> root = new HashMap<String, Object>();
		// 签证借款申请相关信息
		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(revid);
		String remark1 = reviewService.findRemark1(revid.toString());
		if (reviewAndDetailInfoMap != null) {
			root.put("revCreateDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("createDate"))));// 填写日期
			
			// --- wangxinwei 20151008 added：需求C221，处理确认付款日期
			root.put("revUpdateDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate"))));// 更新日期
			
			root.put("grouptotalborrownode",reviewAndDetailInfoMap.get("grouptotalborrownode"));// 申报原因
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy"));
			
			/**
			 * 经办人显示应为产品发布人员
			 */
			String orderid = reviewAndDetailInfoMap.get("orderId");
			VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
			root.put("ordergroupcode", visaOrder.getGroupCode());// 团号
			root.put("costname",  "报销");//款项
			VisaProducts visaProducts =  visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			String productCreater = visaProducts.getCreateBy().getName();
			root.put("productCreater", productCreater);
					
			//--------------支持凭单摘要--------------
			String trvrebatesnotes = reviewAndDetailInfoMap.get("trvrebatesnotes");
			String groupcurrencyMarks = reviewAndDetailInfoMap.get("grouprebatesnodes");
			String remark = (trvrebatesnotes+groupcurrencyMarks).replace("#@!#!@#", " ");
			root.put("remark", remark); //摘要
			root.put("remark1", remark1);
			
			//收款单位
			Agentinfo agentInfo = agentInfoService.findOne(visaOrder.getAgentinfoId());
			String orderCompanyName = agentInfo.getAgentName();
			if(StringUtils.isNotBlank(orderCompanyName)) {				
				root.put("orderCompanyName", orderCompanyName);	//渠道名称
			}else{
				root.put("orderCompanyName", "");
			}
		
			if (null != user) {
				root.put("operatorName", user.getName());// 经办人、领款人都为借款申请人
			} else {
				root.put("operatorName", "未知");
			}
			
			/**
			 * 签证返佣支出凭单的汇率取成本录入的汇率,对应需求编号C139/C293
			 * wangxinwei  20151026 added
			 * 鉴于成本表中字段rate数据有空（null）的情况，如果成本的汇率为空时还按照原来的方式处理
			 */
			BigDecimal exchangerate = new BigDecimal(1);
			String currencyId = reviewAndDetailInfoMap.get("currencyId");
			List<CostRecord> costRecords = costManageService.findCostRecordList(revid);
			if (null!=costRecords&&costRecords.size()>0) {
				CostRecord costRecord = costRecords.get(0);
				if (null != currencyId) {
					Currency currency = currencyService.findCurrency(Long.parseLong(currencyId));
					root.put("currencyName", currency.getCurrencyName()); //币种中文名称
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
					root.put("currencyName", currency.getCurrencyName()); //币种中文名称
				}			
			}
			root.put("currencyExchangerate", MoneyNumberFormat.fmtMicrometer(exchangerate.toString(), "#,##0.0000")); //汇率
			//45需求，返佣金额以每次的支付记录为依据
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
			root.put("payRebatesAmount", MoneyNumberFormat.getThousandsMoney(Double.valueOf(payRebatesAmount), MoneyNumberFormat.THOUSANDST_POINT_TWO));
			BigDecimal totalRMB = exchangerate.multiply(new BigDecimal(Double.valueOf(payRebatesAmount)));
			root.put("rebatesAmount", MoneyNumberFormat.getThousandsMoney(totalRMB.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO));// 返佣金额
			root.put("rebatesAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(MoneyNumberFormat.getThousandsMoney(totalRMB.doubleValue(), 
					MoneyNumberFormat.POINT_TWO))));// 返佣金额大写
			
			//银行账户名
			String accountName="";
			if(agentInfo != null) {
				accountName = bankInfoService.getAccountName(agentInfo.getId(), Context.PLAT_TYPE_QD, payDetail==null? "":payDetail.getTobankName(), payDetail==null? "":payDetail.getTobankAccount(), "");
			}
			root.put("accountName", accountName);
			
			//----- wxw added 20151008 -----需求C221， 处理付款确认时间，payStatus：1 显示update时间，0不显示
			//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
			String companyUuid = UserUtils.getUser().getCompany().getUuid();	
			String payStatus = reviewAndDetailInfoMap.get("payStatus");
			if (Context.SUPPLIER_UUID_HQX.equals(companyUuid)||Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
					root.put("payStatus","0");
			}else {
				if(StringUtils.isNotBlank(payStatus)) {
					root.put("payStatus",payStatus);
				}else {
					root.put("payStatus","0");
				}
			}
		}
		
		//出纳以外的最后一个审批人：对签证借款流程来说level为3
		List<ReviewLog> reviewLogs = reviewLogDao.findReviewLog(revid);
		
		if (null!=reviewLogs&&reviewLogs.size()>0) {
			User user = UserUtils.getUser(reviewLogs.get(reviewLogs.size()-1).getCreateBy());
			if (null!=user) {
				root.put("majorCheckPerson", user.getName());//复合，主管审批  都是最后一个的审批人
			}
		}
		
		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳 
		// 7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
		Map<Integer, String> jobtypeusernameMap =reviewCommonService.getReviewJobName(Context.REBATES_FLOW_TYPE,reviewLogs);
		
		if (jobtypeusernameMap != null) {
			
			/**
			 * 需求变更2015-04-22：如果为环球行用户出纳为空
			 */
			if (null!=jobtypeusernameMap.get(4)) {//主管审批
				if (68!=UserUtils.getUser().getCompany().getId()) {
					root.put("jdmanager", jobtypeusernameMap.get(4));
				}else {
					root.put("jdmanager", "");
				}
			}else {
				root.put("jdmanager", "");
			}
			
			//lihong  123
			//2015-04-09王新伟添加
			/**
			 * 需求变更2015-04-22：如果为环球行用户财务主管为空
			 */
			if (null!=jobtypeusernameMap.get(9)) {//财务主管
				if (68!=UserUtils.getUser().getCompany().getId()) {
					root.put("cwmanager", jobtypeusernameMap.get(9));
				}else {
					root.put("cwmanager", "");
				}
			}else {
				root.put("cwmanager", "");
			}
			
			if (null!=jobtypeusernameMap.get(8)) {//财务
				root.put("cw", jobtypeusernameMap.get(8));
			}else {
				root.put("cw", "");
			}
			
			/**
			 * 需求变更2015-04-22：如果为环球行用户出纳为空
			 */
			if (null!=jobtypeusernameMap.get(6)) {//出纳
				if (68!=UserUtils.getUser().getCompany().getId()) {
					root.put("cashier", jobtypeusernameMap.get(6));
				}else {
					root.put("cashier", "");
				}
			}else {
				root.put("cashier", "");
			}
			
			//model.addAttribute("majorCheckPerson", user.getName());//复合，主管审批  都是最后一个的审批人
			if (null!=jobtypeusernameMap.get(10)) {//总经理
				root.put("majorCheckPerson", jobtypeusernameMap.get(10));
			}else {
				root.put("majorCheckPerson", "");
			}
			
			if (null!=jobtypeusernameMap.get(7)) {//部门经理
				root.put("deptmanager", jobtypeusernameMap.get(7));
			}else {
				root.put("deptmanager", "");
			}
		}
		
		//环球行用户将团号改为订单团号
		if (Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())) {
			root.put("groupCodeName", "订单团号");
		} else {
			root.put("groupCodeName", "团号");
		}
		
	   return FreeMarkerUtil.generateFile("visarebatesreview.ftl", "visarebatesreview.doc", root);
	}
		
	/**
	 * 生成签证借款单文件
	 * @param revid
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	@Transactional(readOnly=false,rollbackFor=Exception.class)
	@Override
	public File createRebatesReviewSheetDownloadFile(String revid) throws IOException, TemplateException{
		//word文档数据集合
		Map<String, Object> root = new HashMap<String, Object>();
		// 签证借款申请相关信息
		Map<String, String> reviewAndDetailInfoMap = reviewService.findReview(Long.parseLong(revid));
		String remark1 = reviewService.findRemark1(revid.toString());
		if (reviewAndDetailInfoMap != null) {
			root.put("revCreateDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("createDate"))));// 填写日期
			
			// --- wangxinwei 20151008 added：需求C221，处理确认付款日期
			root.put("revUpdateDate", sdf.format(DateUtils.dateFormat(reviewAndDetailInfoMap.get("updateDate"))));// 更新日期
			
			root.put("grouptotalborrownode",reviewAndDetailInfoMap.get("grouptotalborrownode"));// 申报原因
			User user = UserUtils.getUser(reviewAndDetailInfoMap.get("createBy"));
			
			/**
			 * 经办人显示应为产品发布人员
			 */
			String orderid = reviewAndDetailInfoMap.get("orderId");
			VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderid));
			root.put("ordergroupcode", visaOrder.getGroupCode());// 团号
			root.put("costname",  "报销");//款项
			VisaProducts visaProducts =  visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			String productCreater = visaProducts.getCreateBy().getName();
			root.put("productCreater", productCreater);
						
			//--------------支持凭单摘要--------------
			String trvrebatesnotes = reviewAndDetailInfoMap.get("trvrebatesnotes");
			String groupcurrencyMarks = reviewAndDetailInfoMap.get("grouprebatesnodes");
			String remark = (trvrebatesnotes+groupcurrencyMarks).replace("#@!#!@#", " ");
			root.put("remark", remark); //摘要
			root.put("remark1", remark1);
			
			//--------------获取收款单位--------------
			Agentinfo agentInfo = agentInfoService.findOne(visaOrder.getAgentinfoId());
			String orderCompanyName = agentInfo.getAgentName();
			if(StringUtils.isNotBlank(orderCompanyName)) {				
				root.put("orderCompanyName", orderCompanyName);	//渠道名称
			}else{
				root.put("orderCompanyName", "");
			}
					
			if (null != user) {
				root.put("operatorName", user.getName());// 经办人、领款人都为借款申请人
			} else {
				root.put("operatorName", "未知");
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
					root.put("currencyName", currency.getCurrencyName()); //币种中文名称
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
					root.put("currencyName", currency.getCurrencyName()); //币种中文名称
				}
			}
			root.put("currencyExchangerate", MoneyNumberFormat.fmtMicrometer(exchangerate.toString(), "#,##0.0000")); //汇率
			
			root.put("accountName", "");
			BigDecimal rebatesAmount = new BigDecimal(Double.valueOf(reviewAndDetailInfoMap.get("rebatesAmount")));
			root.put("payRebatesAmount", MoneyNumberFormat.getThousandsMoney(rebatesAmount.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO));
			BigDecimal totalRMB = exchangerate.multiply(rebatesAmount);
			root.put("rebatesAmount", MoneyNumberFormat.getThousandsMoney(totalRMB.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO));// 返佣金额
			root.put("rebatesAmountDx", MoneyNumberFormat.digitUppercase(Double.parseDouble(MoneyNumberFormat.getThousandsMoney(totalRMB.doubleValue(), 
					MoneyNumberFormat.POINT_TWO))));// 返佣金额大写
			
			//----- wxw added 20151008 -----需求C221， 处理付款确认时间，payStatus：1 显示update时间，0不显示
			//----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
			String companyUuid = UserUtils.getUser().getCompany().getUuid();	
			String payStatus = reviewAndDetailInfoMap.get("payStatus");
			if (Context.SUPPLIER_UUID_HQX.equals(companyUuid)||Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid)) {
					root.put("payStatus","0");
			}else {
				if(StringUtils.isNotBlank(payStatus)) {
					root.put("payStatus",payStatus);
				}else {
					root.put("payStatus","0");
				}
			}
			
		}
		//出纳以外的最后一个审批人：对签证借款流程来说level为3
		List<ReviewLog> reviewLogs = reviewLogDao.findReviewLog(Long.parseLong(revid));
		
		if (null!=reviewLogs&&reviewLogs.size()>0) {
			User user = UserUtils.getUser(reviewLogs.get(reviewLogs.size()-1).getCreateBy());
			if (null!=user) {
				root.put("majorCheckPerson", user.getName());//复合，主管审批  都是最后一个的审批人
			}
		}
		
		// 1-销售  2-销售主管 3-计调 4-计调主管 5- 操作 6-出纳 
		// 7-部门经理 8-财务  9-财务经理 10-总经理 0-其他
		Map<Integer, String> jobtypeusernameMap =reviewCommonService.getReviewJobName(Context.REBATES_FLOW_TYPE,reviewLogs);
		
		if (jobtypeusernameMap != null) {
			
			/**
			 * 需求变更2015-04-22：如果为环球行用户出纳为空
			 */
			if (null!=jobtypeusernameMap.get(4)) {//主管审批
				if (68!=UserUtils.getUser().getCompany().getId()) {
					root.put("jdmanager", jobtypeusernameMap.get(4));
				}else {
					root.put("jdmanager", "");
				}
			}else {
				root.put("jdmanager", "");
			}
			
			//lihong  123
			//2015-04-09王新伟添加
			/**
			 * 需求变更2015-04-22：如果为环球行用户财务主管为空
			 */
			if (null!=jobtypeusernameMap.get(9)) {//财务主管
				if (68!=UserUtils.getUser().getCompany().getId()) {
					root.put("cwmanager", jobtypeusernameMap.get(9));
				}else {
					root.put("cwmanager", "");
				}
			}else {
				root.put("cwmanager", "");
			}
			
			if (null!=jobtypeusernameMap.get(8)) {//财务
				root.put("cw", jobtypeusernameMap.get(8));
			}else {
				root.put("cw", "");
			}
			
			/**
			 * 需求变更2015-04-22：如果为环球行用户出纳为空
			 */
			if (null!=jobtypeusernameMap.get(6)) {//出纳
				if (68!=UserUtils.getUser().getCompany().getId()) {
					root.put("cashier", jobtypeusernameMap.get(6));
				}else {
					root.put("cashier", "");
				}
			}else {
				root.put("cashier", "");
			}
			
			//model.addAttribute("majorCheckPerson", user.getName());//复合，主管审批  都是最后一个的审批人
			if (null!=jobtypeusernameMap.get(10)) {//总经理
				root.put("majorCheckPerson", jobtypeusernameMap.get(10));
			}else {
				root.put("majorCheckPerson", "");
			}
			
			if (null!=jobtypeusernameMap.get(7)) {//部门经理
				root.put("deptmanager", jobtypeusernameMap.get(7));
			}else {
				root.put("deptmanager", "");
			}
		}
		
		//环球行用户将团号改为订单团号
		if (Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())) {
			root.put("groupCodeName", "订单团号");
		} else {
			root.put("groupCodeName", "团号");
		}
		
	   return FreeMarkerUtil.generateFile("visarebatesreview.ftl", "visarebatesreview.doc", root);
	}
	
	//-----------------------返佣打印相关结束----------------------
}
