package com.trekiz.admin.review.rebates.airticket.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.google.common.collect.Maps;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.rebates.entity.RebatesNew;
import com.trekiz.admin.modules.order.service.PlatBankInfoService;
import com.trekiz.admin.modules.order.service.RefundService;
import com.trekiz.admin.modules.reviewreceipt.common.ReviewReceiptContext;
import com.trekiz.admin.modules.reviewreceipt.common.ReviewReceiptContext.PaymentReviewElement;
import com.trekiz.admin.modules.reviewreceipt.service.ReviewReceiptService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.rebates.singleGroup.service.RebatesNewService;

@Service
@Transactional(readOnly = true)
public class NewAirticketrebatesService extends BaseService {
	
	@Autowired
    private AgentinfoDao agentinfoDao;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
    private ReviewService processReviewService;
	@Autowired
	private RebatesNewService rebatesService;
	@Autowired
	private ReviewReceiptService reviewReceiptService;
	@Autowired
	private PlatBankInfoService bankInfoService;
	@Autowired
	private RefundService refundService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	
	
	/**
	 * 机票返佣支出凭单 ----仅供返佣审批页面使用
	 * @param reviewId
	 * @return
	 */
	public Map<String,Object> airticketRebatesPrintData(String reviewId){
		Map<String,Object> map = new HashMap<String,Object>();//要返回的返回结果集合
		ReviewNew reviewNew = processReviewService.getReview(reviewId);	//返佣申请审核相关信息review表
		map.put("createDate", reviewNew.getCreateDate());	//填写日期
		if(reviewNew.getPrintDate()!=null){
			map.put("firstPrintTime",reviewNew.getPrintDate());	//首次打印时间
		}else{
			map.put("firstPrintTime",  new Date());	//首次打印时间
		}
		User user = UserUtils.getUser(reviewNew.getCreateBy());
		if (null != user) {
			map.put("operatorName", user.getName());	//经办人(返佣申请人)
			map.put("payee", user.getName());	//领款人(返佣申请人)
		} else {
			map.put("operatorName", "");//经办人(返佣申请人)
			map.put("payee", "");	//领款人(返佣申请人)
		}
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		Integer payStatus = reviewNew.getPayStatus();//payStatus 默认值是0 表示出纳未确认
		if(!Context.SUPPLIER_UUID_LMT.equals(companyUuid) && !Context.SUPPLIER_UUID_HQX.equals(companyUuid) && payStatus!=0) {
			Date confirmPayDate = reviewNew.getUpdateDate();
			map.put("confirmPayDate", DateUtils.formatDate(confirmPayDate, "yyyy 年  MM 月 dd 日"));
		} else {
			map.put("confirmPayDate", "   年   月   日");
		}
		//返佣申请相关信息rebates表
		List<RebatesNew> rebatesList = rebatesService.findRebatesListByRid(reviewId);
		RebatesNew rebate = rebatesList.get(0);
		if(rebate != null ){
			map.put("remark",  rebate.getRemark());//备注
			BigDecimal  totalRMBMoney = new BigDecimal(0);	//合计人民币金额
			for(RebatesNew rebates :rebatesList){
				String currencyName = rebates.getCurrency().getCurrencyName().trim();	//币种名称
			
				List<MoneyAmount> moneyList = moneyAmountService.findAmountBySerialNum(rebates.getNewRebates());
				MoneyAmount money = new MoneyAmount();
				if(moneyList != null && moneyList.size() > 0){
					for(MoneyAmount moneyAmount : moneyList){
						if(rebates.getCurrencyId().longValue() == moneyAmount.getCurrencyId().longValue() ){
							money = moneyAmount;
							break;
						}
					}
				}
				BigDecimal	newRebates = money.getAmount() == null? new BigDecimal(0):money.getAmount();	//金额
				//如果是非人民币
				if(!currencyName.contains("人民币")){
					BigDecimal currencyExchangerate = new BigDecimal(1);
					currencyExchangerate = rebates.getCurrencyExchangerate();	
					//转换成人民币金额 = 金额 * 汇率
					BigDecimal RMBMoney = newRebates.multiply(currencyExchangerate);				
					totalRMBMoney = totalRMBMoney.add(RMBMoney);				
				}else{
					totalRMBMoney = totalRMBMoney.add(newRebates);
				}
			}
			map.put("currencyExchangerate", "1.0000");//汇率默认为1
			
			map.put("currencyName", "人民币");	//币种名称
			map.put("money", MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO));	//币种金额
			map.put("totalRMBMoney", MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO)); //合计人民币金额
			
			int result = totalRMBMoney.compareTo(new BigDecimal(0));
			if(result == 1 || result == 0 ){
				map.put("totalRMBMoneyName", StringNumFormat.changeAmount(MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.POINT_TWO))); //合计人民币金额大写
			}else{
				map.put("totalRMBMoneyName", "红字" + StringNumFormat.changeAmount(MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.POINT_TWO).replaceAll("-",""))); //合计人民币金额大写
			}
			
		}else{
			map.put("currencyName", "");	//币种名称
			map.put("money", "");	//币种金额
			map.put("currencyExchangerate", "");//汇率
			map.put("totalRMBMoney", ""); //合计人民币金额
			map.put("remark",  "");//备注
			map.put("totalRMBMoneyName", ""); //合计人民币金额大写
		}
		map.put("costname",  "报销");//款项
		
		Agentinfo agentInfo = null;
		if( rebate != null &&  rebate.getOrderId() != null){
			long orderId = rebate.getOrderId();
			AirticketOrder productOrder = airticketOrderDao.getAirticketOrderById(orderId);
			agentInfo = agentinfoDao.findOne(productOrder.getAgentinfoId());
			String orderCompanyName = agentInfo.getAgentName();
			map.put("orderCompanyName", orderCompanyName);	//渠道名称
		}else{
			map.put("orderCompanyName", "");
		}
		//银行账户名
		map.put("accountName", "");
		
		//各种审批人员
		MultiValueMap<Integer,User> reviewers = reviewReceiptService.obtainReviewer4Receipt(UserUtils.getUser().getCompany().getUuid(),ReviewReceiptContext.RECEIPT_TYPE_PAYMENT,reviewId);
		map.put("deptmanager", this.getUsrNames(reviewers.get(PaymentReviewElement.EXECUTIVE)));	//主管审批
		map.put("cashier",this.getUsrNames(reviewers.get(PaymentReviewElement.CASHIER)));	//出纳
		map.put("finance", this.getUsrNames(reviewers.get(PaymentReviewElement.FINANCIAL_EXECUTIVE)));//财务
		map.put("financeManage",this.getUsrNames(reviewers.get(PaymentReviewElement.FINANCIAL_EXECUTIVE)));	//财务主管
		map.put("majorCheckPerson", this.getUsrNames(reviewers.get(PaymentReviewElement.GENERAL_MANAGER)));	//总经理
		map.put("auditor", this.getUsrNames(reviewers.get(PaymentReviewElement.REVIEWER)));	//审核
		
		//环球行团号改为订单团号
		if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())) {
			map.put("groupCodeName", "订单团号");
		}else {
			map.put("groupCodeName", "团号");
		}
				
		return map;
	}
	
	/**
	 * 机票返佣支出凭单
	 * @param reviewId
	 * @return
	 */
	public Map<String,Object> airticketRebatesPrintData(String reviewId, String payId){
		Map<String,Object> map = new HashMap<String,Object>();//要返回的返回结果集合

		//返佣申请审核相关信息
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		Map<String, Object> reviewDetailMap = processReviewService.getReviewDetailMapByReviewId(reviewId);				
		if (reviewDetailMap != null) {
			map.put("createDate", DateUtils.dateFormat(reviewDetailMap.get("createDate").toString()));	//填写日期
			String printTime = objToString(reviewDetailMap.get("printDate"));
			if (StringUtils.isNotBlank(printTime)) {
				map.put("firstPrintTime",  DateUtils.dateFormat(printTime));	//首次打印时间
			} else {
				map.put("firstPrintTime",  new Date());	//首次打印时间
			}
				
			String payStatus = objToString(reviewDetailMap.get("payStatus"));
			if (!Context.SUPPLIER_UUID_HQX.endsWith(companyUuid) && !Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid) && Integer.valueOf(payStatus)==1) {
				Date confirmPayDate = DateUtils.dateFormat(objToString(reviewDetailMap.get("updateDate")));
				map.put("confirmPayDate", DateUtils.formatDate(confirmPayDate, "yyyy 年  MM 月 dd 日"));
			} else {
				map.put("confirmPayDate", "   年   月   日");
			}
		}
		
		String currencyName = "";    //币种名称
		String moneyStr= "";		//币种金额
		String currencyExchangerate = "";  //汇率
		String totalRMBMoneyStr = ""; //合计人民币金额
		String totalRMBMoneyName = ""; //合计人民币金额大写
		String remark="";   //备注
		
		//返佣申请相关信息rebates表
		Map<String, BigDecimal> rateMap = Maps.newHashMap();
		List<RebatesNew> rebatesList = rebatesService.findRebatesListByRid(reviewId);
		if(CollectionUtils.isNotEmpty(rebatesList)) {
			remark = rebatesList.get(0).getRemark();
			
			for(RebatesNew rebates : rebatesList) {
				//获取币种和汇率的对应关系
				String currencyMark = rebates.getCurrency().getCurrencyMark();
				if(!rateMap.containsKey(currencyMark)) {
					if ("¥".equals(currencyMark)) {
						rateMap.put(currencyMark, new BigDecimal(1));
					}else{
						rateMap.put(currencyMark, rebates.getCurrencyExchangerate());
					}
				}
			}
		}

		User user = UserUtils.getUser(objToString(reviewDetailMap.get("createBy")));
		if (null != user) {
			map.put("operatorName", user.getName());	//经办人(返佣申请人)
			map.put("payee", user.getName());	//领款人(返佣申请人)
		} else {
			map.put("operatorName", "");//经办人(返佣申请人)
			map.put("payee", "");	//领款人(返佣申请人)
		}
		//45需求，凭单中的金额以每次支付的金额为准 ---多币种---
		List<Object[]> moneys = null;
		PayInfoDetail payDetail = null;
		if (StringUtils.isNotBlank(payId)) {
			payDetail = refundService.getPayInfoByPayId(payId, Context.PRODUCT_TYPE_AIRTICKET.toString());
			if (payDetail != null && StringUtils.isNotBlank(payDetail.getMoneyDispStyle())) {
				moneys = MoneyNumberFormat.getMoneyFromString(payDetail.getMoneyDispStyle(), "\\+");
				if(Context.SUPPLIER_UUID_YJXZ.equals(companyUuid)){
					Integer payType = payDetail.getPayType();
					if(1 == payType || 3 == payType){
						map.put("isYJXZ", "YJXZ");
						map.put("payee", payDetail.getPayerName());
					}
				}
			}
		}
		if(CollectionUtils.isNotEmpty(moneys)) {
			BigDecimal  totalRMBMoney = new BigDecimal(0);	//合计人民币金额			
			for(Object[] money : moneys) {
				String currencyMark = money[0].toString();
				BigDecimal newRebates = new BigDecimal(Double.valueOf(money[1].toString()));
				if(rateMap.containsKey(currencyMark)) {					
					BigDecimal RMBMoney = newRebates.multiply(rateMap.get(currencyMark));
					totalRMBMoney = totalRMBMoney.add(RMBMoney);
				}else {
					Currency currency = currencyService.findCurrencyByCurrencyMark(currencyMark,UserUtils.getUser().getCompany().getId());
					BigDecimal rate = null==currency? new BigDecimal(0):currency.getCurrencyExchangerate();
					BigDecimal RMBMoney = newRebates.multiply(rate);
					totalRMBMoney = totalRMBMoney.add(RMBMoney);
				}
			}
			
			currencyName = "人民币";
			currencyExchangerate = "1.0000";  //汇率
			
			moneyStr = MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO);	//币种金额
			totalRMBMoneyStr = moneyStr;
			int result = totalRMBMoney.compareTo(new BigDecimal(0));
			if(result == 1 || result == 0 ){
				totalRMBMoneyName = StringNumFormat.changeAmount(MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.POINT_TWO)); //合计人民币金额大写
			}else{
				totalRMBMoneyName = "红字" + StringNumFormat.changeAmount(MoneyNumberFormat.getThousandsMoney(totalRMBMoney.doubleValue(), MoneyNumberFormat.POINT_TWO).replaceAll("-","")); //合计人民币金额大写
			}
		}
		
		map.put("currencyName", currencyName);
		map.put("money", moneyStr);
		map.put("currencyExchangerate", currencyExchangerate);
		
		map.put("totalRMBMoney", totalRMBMoneyStr);
		map.put("totalRMBMoneyName", totalRMBMoneyName);

		map.put("remark", remark);		
		map.put("costname",  "报销");//款项
		
		//渠道名称或者供应商名称
		String orderCompanyName = "";
		String accountName = "";  //账户名称
		//判断是供应商还是渠道商
		String relatedObjectType = objToString(reviewDetailMap.get("relatedObjectType"));
		if(StringUtils.isNotBlank(relatedObjectType) && relatedObjectType.equals("2")) {    //供应商
			orderCompanyName = objToString(reviewDetailMap.get("relatedObjectName"));
			String supplierId = objToString(reviewDetailMap.get("relatedObject"));     //供应商id 
			String bankType = objToString(reviewDetailMap.get(Context.REBATES_OBJECT_ACCOUNT_TYPE));   //境内账户or境外账户
			String bankName = objToString(reviewDetailMap.get(Context.REBATES_OBJECT_ACCOUNT_BANK));   //银行名称
			String bankAccount = objToString(reviewDetailMap.get(Context.REBATES_OBJECT_ACCOUNT_CODE));   //银行账号
			accountName = bankInfoService.getAccountName(Long.valueOf(supplierId), Context.PLAT_TYPE_SUP, bankName, bankAccount, bankType);
		}else {
			if (StringUtils.isNotBlank(objToString(reviewDetailMap.get("agent")))) {
				Agentinfo agentInfo = agentinfoDao.findAgentInfoById(Long.parseLong(objToString(reviewDetailMap.get("agent"))));
				if (agentInfo != null) {
					orderCompanyName = agentInfo.getAgentName();
					accountName = bankInfoService.getAccountName(agentInfo.getId(), Context.PLAT_TYPE_QD, payDetail==null? "":payDetail.getTobankName(), payDetail==null? "":payDetail.getTobankAccount(), "");
				}
			}	
		}
		
		map.put("orderCompanyName", orderCompanyName);
		map.put("accountName", accountName);
		
		//各种审批人员
		MultiValueMap<Integer,User> reviewers = reviewReceiptService.obtainReviewer4Receipt(UserUtils.getUser().getCompany().getUuid(),ReviewReceiptContext.RECEIPT_TYPE_PAYMENT,reviewId);
		map.put("deptmanager", this.getUsrNames(reviewers.get(PaymentReviewElement.EXECUTIVE)));	//主管审批
		map.put("cashier",this.getUsrNames(reviewers.get(PaymentReviewElement.CASHIER)));	//出纳
		map.put("finance", this.getUsrNames(reviewers.get(PaymentReviewElement.FINANCIAL_EXECUTIVE)));//财务
		map.put("financeManage",this.getUsrNames(reviewers.get(PaymentReviewElement.FINANCIAL_EXECUTIVE)));	//财务主管
		map.put("majorCheckPerson", this.getUsrNames(reviewers.get(PaymentReviewElement.GENERAL_MANAGER)));	//总经理
		map.put("auditor", this.getUsrNames(reviewers.get(PaymentReviewElement.REVIEWER)));	//审核
		
		//环球行团号改为订单团号
		if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())) {
			map.put("groupCodeName", "订单团号");
		}else {
			map.put("groupCodeName", "团号");
		}
		return map;
	}
	
	//赋值审批人员名称
	private String getUsrNames(List<User> userList){
		if (CollectionUtils.isNotEmpty(userList)) {
			String reviewerNames = "";
			int j= userList.size();
			for(int i =0 ;i<j;i++){
				User u = userList.get(i);
				reviewerNames+=u.getName();
				if (i < j - 1) {
					reviewerNames+=",";
				}
			}
			return reviewerNames;
		} else {
			return "";
		}
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
}
