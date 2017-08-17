package com.trekiz.admin.modules.order.service;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.AreaUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.word.FreeMarkerUtil;
import com.trekiz.admin.modules.activity.repository.TravelActivityDao;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ActivityInfo;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.entity.ProductOrder;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.entity.Refund;
import com.trekiz.admin.modules.order.pojo.OrderPayDetail;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.repository.OrderContactsDao;
import com.trekiz.admin.modules.order.repository.OrderpayDao;
import com.trekiz.admin.modules.order.repository.PlatBankInfoDao;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.repository.ReviewLogDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaDao;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;
import com.trekiz.admin.modules.visa.repository.VisaProductDao;

@Service
@Transactional(readOnly = true)
public class ProductOrderService extends BaseService {
	
	@Autowired
	private ProductOrderCommonDao poc;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private ReviewLogDao reviewLogDao;
	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	@Autowired
	private ProductOrderCommonDao productOrderCommonDao;
	
	@Autowired
	private ReviewService reviewService;
	
	@Autowired
	private VisaOrderDao visaOrderDao;
	
	@Autowired
	private VisaProductDao visaProductDao;
	
	@Autowired
	private AgentinfoDao agentinfoDao;
	
	@Autowired
	private ActivityAirTicketDao activityAirTicketDao;
	
	@Autowired
	private DictService dictService;
	
	@Autowired
	private TravelActivityDao travelActivityDao;
	@Autowired
	private ReviewCommonService reviewCommonService;
	
	@Autowired
	private OrderContactsDao orderContactsDao;
	
	@Autowired
	private VisaDao visaDao;
	
	@Autowired
	private RefundService refundService;
	
	@Autowired
	private PlatBankInfoDao bankInfoDao;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private OrderpayDao orderpayDao;
	
	public Map<String,String> findProductOrderById(HttpServletRequest request){
		/**
		 * 产品类型: 1表示 单团产品, 2表示 散拼产品,3表示 游学产品,4表示 大客户产品,5表示 自由行产品,6表示 签证产品, 7表示 机票产品, 8表示 机票切位, 9表示 散拼切位
		 * */
		Map<String,String> requestMap = request.getParameterMap();
		String prdType = "";
		String agentId = "";
		String orderId = "";
		String reviewId = "";
		String groupCode = "";
		String payId = "";
		String option = "";
		if(requestMap.get("batch") != null && requestMap.get("batch").toString().equals("1")){
			prdType = requestMap.get("productType");//产品类型
			agentId = requestMap.get("agentId");
			orderId = requestMap.get("orderId");//订单ID 
			reviewId = requestMap.get("reviewId");//审批ID
			groupCode = requestMap.get("groupCodePrint");//团号
			payId = requestMap.get("payId");   //支付id
			option = requestMap.get("option");  //用于判断点击的是退款列表的打印按钮还是支付记录里面的打印按钮
		}else{
			prdType = request.getParameter("productType");//产品类型
			agentId = request.getParameter("agentId");
			orderId = request.getParameter("orderId");//订单ID 
			reviewId = request.getParameter("reviewId");//审批ID
			groupCode = request.getParameter("groupCodePrint");//团号
			payId = request.getParameter("payId");   //支付id
			option = request.getParameter("option");  //用于判断点击的是退款列表的打印按钮还是支付记录里面的打印按钮
		}
		
		Map<String,String> map= new HashMap<String,String>();
		map.put("productType", prdType);
		map.put("agentId", agentId);
		map.put("orderId", orderId);
		map.put("reviewId", reviewId);
		map.put("groupCodePrint", groupCode);
		map.put("payId", payId);
		map.put("option", option);
		
		Map<String, String> reviewInfo = reviewService.findReview(Long.parseLong(reviewId));
		DecimalFormat  df =new DecimalFormat("#.00");//取两位小数点
		/*第一次打印时间*/
		map.put("printTime", reviewInfo.get("printTime") == null ? "" : DateUtils.formatCustomDate(DateUtils.dateFormat(reviewInfo.get("printTime")),"yyyy/MM/dd HH:mm"));
		//查询这个审核的记录
		List<ReviewLog> logs = reviewLogDao.findReviewLog(Long.parseLong(reviewId));
		//获取申请人信息，经产品部确认第一个审核的人为申请人
		Map<Integer, String> reviewJobNames = reviewCommonService.getReviewJobName(Integer.parseInt(reviewInfo.get("flowType")), logs);
		if(reviewJobNames.get(7)!=null){
			map.put("bmjl", reviewJobNames.get(7));//审核 （部门经理）
		} else {
			map.put("bmjl", "");//审核 （部门经理）
		}
		if(reviewJobNames.get(8)!=null){
			map.put("cwzg", reviewJobNames.get(8));//财务
		} else if(reviewJobNames.get(9)!=null) {
			map.put("cwzg", reviewJobNames.get(9));//财务主管 
		} else {
			map.put("cwzg", "");//财务主管 （财务）
		}
		if(reviewJobNames.get(10)!=null){
			map.put("sp", reviewJobNames.get(10));//审核 （总经理）
		} else {
			map.put("sp", "");//审核 （总经理）
		}
		
		Agentinfo agentinfo = agentinfoDao.findOne(Long.parseLong(agentId));
		//产品类型: 1表示 单团产品, 2表示 散拼产品,3表示 游学产品,4表示 大客户产品,5表示 自由行产品,6表示 签证产品, 7表示 机票产品, 8表示 机票切位, 9表示 散拼切位
		if(StringUtils.isNotBlank(prdType) && (prdType.equals("1")|| prdType.equals("2")||prdType.equals("3")||prdType.equals("4")||prdType.equals("5")||prdType.equals("10"))){
			ProductOrder po = poc.findByOrderId(Long.valueOf(orderId)).get(0);
			po.getOrderCompany();
			int travelDays = travelActivityDao.findOne(po.getProductId()).getActivityDuration();
			
			List<OrderContacts> oContacts = orderContactsDao.findOrderContactsByOrderIdAndOrderType(Long.valueOf(orderId) ,Integer.parseInt(prdType));
			String telPhone = "";
			if(oContacts != null && oContacts.size() > 0){
				telPhone = oContacts.get(0).getContactsTel();
			} 
			map.put("tel", telPhone);//电话
			
		    map.put("groupCodePrint", FreeMarkerUtil.StringFilter(po.getActivityGroup().getGroupCode()));//团号
		    map.put("personNum",po.getOrderPersonNum().toString());//人数
		    String pName = po.getActivityGroup().getTravelActivity().getAcitivityName() == null ? "" : po.getActivityGroup().getTravelActivity().getAcitivityName();
		    map.put("productName", FreeMarkerUtil.StringFilter(pName));//产品线路
		    map.put("startDate",DateUtils.formatCustomDate(po.getActivityGroup().getGroupOpenDate(),"yyyy-MM-dd") );//出发日期
		    map.put("endDate",DateUtils.formatCustomDate(new Date(po.getActivityGroup().getGroupOpenDate().getTime() + travelDays * 24 * 60 * 60 * 1000),"yyyy-MM-dd") );//返回日期
		    map.put("totalMoney", "¥ " + MoneyNumberFormat.getThousandsMoney(Double.parseDouble(df.format(getRMBMoney(po.getTotalMoney()))), MoneyNumberFormat.THOUSANDST_POINT_TWO));//应收金额
		    map.put("payedMoney", "¥ " + MoneyNumberFormat.getThousandsMoney(Double.parseDouble(df.format(getRMBMoney(po.getPayedMoney()))), MoneyNumberFormat.THOUSANDST_POINT_TWO));//已付金额
		    map.put("keHu",po.getOrderCompanyName() == null ? "" : po.getOrderCompanyName());//客户
		    map.put("applyPerson", FreeMarkerUtil.StringFilter(UserUtils.getUserNameById(Long.valueOf(po.getSalerId()))));  //申请人 变更为 销售
		    Date date = DateUtils.dateFormat(reviewInfo.get("createDate"), "yyyy-MM-dd HH:mm:ss");
		    DateUtils.formatCustomDate(date, "yyyy年MM月dd日");
		    map.put("applyDate",  DateUtils.formatCustomDate(date, "yyyy年MM月dd日"));
		}else if(StringUtils.isNotBlank(prdType) && "6".equals(prdType)){
			//签证
			VisaOrder po = visaOrderDao.findOne(Long.valueOf(orderId));
			VisaProducts visaProducts = visaProductDao.findOne(po.getVisaProductId());
			
			List<OrderContacts> oContacts = orderContactsDao.findOrderContactsByOrderIdAndOrderType(Long.valueOf(orderId) ,6);
			String telPhone = "";
			if(oContacts != null && oContacts.size() > 0){
				oContacts.get(0).getContactsName();
				telPhone = oContacts.get(0).getContactsTel();
			} 
			map.put("tel", telPhone);//电话
			
		    map.put("groupCodePrint", FreeMarkerUtil.StringFilter(groupCode));//团号
		    map.put("productName", FreeMarkerUtil.StringFilter(visaProducts.getProductName()));//产品名称  签证国家 + 签证类型 visaCountryStr + visaTypeStr  改为了产品名称
		    map.put("startDate", "");//出发日期
		    map.put("endDate", "");//返回日期  
		    if(reviewInfo.get("flowType") != null && "1".equals(reviewInfo.get("flowType"))){//如果是退款的话
		    	map.put("personNum",po.getTravelNum().toString());//人数
		    	map.put("totalMoney", "¥ " + MoneyNumberFormat.getThousandsMoney(Double.parseDouble(df.format(getRMBMoney(po.getTotalMoney()))), MoneyNumberFormat.THOUSANDST_POINT_TWO));//应收金额
		    	map.put("payedMoney", "¥ " + MoneyNumberFormat.getThousandsMoney(Double.parseDouble(df.format(getRMBMoney(po.getPayedMoney()))), MoneyNumberFormat.THOUSANDST_POINT_TWO));//已付金额
		    } else if(reviewInfo.get("flowType") != null && "7".equals(reviewInfo.get("flowType"))){ //退签证押金
		    	map.put("personNum", "1");//人数
		    	if(reviewInfo.get("travelerId") != null && !"".equals(reviewInfo.get("travelerId").trim())){
		    		Visa visa = visaDao.findByTravelerId(Long.parseLong(reviewInfo.get("travelerId")));
		    		map.put("totalMoney", "¥ " + MoneyNumberFormat.getThousandsMoney(Double.parseDouble(df.format(getRMBMoney(visa.getTotalDeposit()))), MoneyNumberFormat.THOUSANDST_POINT_TWO));//游客的应收押金
		    		map.put("payedMoney", "¥ " + MoneyNumberFormat.getThousandsMoney(Double.parseDouble(df.format(getRMBMoney(visa.getPayedDeposit()))), MoneyNumberFormat.THOUSANDST_POINT_TWO));//游客的已收押金
		    	}
		    } else {// 其它默认空值
		    	map.put("personNum", "");//人数
		    	map.put("totalMoney", "");
		    	map.put("payedMoney", "");
		    }
		    map.put("keHu",FreeMarkerUtil.StringFilter(agentinfo.getAgentName() == null ? "" : agentinfo.getAgentName()));//客户
		    map.put("applyPerson", FreeMarkerUtil.StringFilter(UserUtils.getUserNameById(Long.valueOf(po.getSalerId()))));  //申请人 变更为销售
		    Date date = DateUtils.dateFormat(reviewInfo.get("createDate"), "yyyy-MM-dd HH:mm:ss");
		    DateUtils.formatCustomDate(date, "yyyy年MM月dd日");
		    map.put("applyDate",  DateUtils.formatCustomDate(date, "yyyy年MM月dd日"));
		}else if(StringUtils.isNotBlank(prdType) && "7".equals(prdType)){
			//机票
			AirticketOrder airticketOrder = airticketOrderDao.getAirticketOrderById(Long.parseLong(orderId));
			ActivityAirTicket activityAirTicket = activityAirTicketDao.findOne(airticketOrder.getAirticketId());
			
			List<OrderContacts> oContacts = orderContactsDao.findOrderContactsByOrderIdAndOrderType(Long.valueOf(orderId) ,Integer.parseInt(prdType));
			String telPhone = "";
			
			if(oContacts != null && oContacts.size() > 0){
				oContacts.get(0).getContactsName();
				telPhone = oContacts.get(0).getContactsTel();
			} 

			map.put("tel", telPhone);//电话
			
		    map.put("groupCodePrint", FreeMarkerUtil.StringFilter(groupCode));//团号
		    map.put("personNum", airticketOrder.getPersonNum() + "");//人数
		    String fromCity = dictService.findByValueAndType(activityAirTicket.getDepartureCity(), "from_area").getLabel();//出发城市
		    String arriveCity = AreaUtil.findAreaNameById(Long.parseLong(activityAirTicket.getArrivedCity()));//到达城市
		    String airTypeId = activityAirTicket.getAirType();
		    String airType = "";//机票类型
		    if(Integer.parseInt(airTypeId) == 1){
		    	airType = "多段";
		    } else if(Integer.parseInt(airTypeId) == 2){
		    	airType = "往返";
		    } else if(Integer.parseInt(airTypeId) == 3){
		    	airType = "单程";
		    }
		    String startDate = DateUtils.formatCustomDate(activityAirTicket.getStartingDate(),"yyyy-MM-dd");//出发日期
		    map.put("productName", FreeMarkerUtil.StringFilter(fromCity + "-->" + arriveCity + " " + airType + " " + startDate));//产品线路
		    map.put("startDate",DateUtils.formatCustomDate(activityAirTicket.getStartingDate(),"yyyy-MM-dd") );//出发日期
		    if("3".equals(activityAirTicket.getAirType())) {  //单程
		    	map.put("endDate", "");
		    }else {
		    	map.put("endDate",DateUtils.formatCustomDate(activityAirTicket.getReturnDate(),"yyyy-MM-dd") );//返回日期
		    }
		    
		    map.put("totalMoney", "¥ " + MoneyNumberFormat.getThousandsMoney(Double.parseDouble(df.format(getRMBMoney(airticketOrder.getTotalMoney()))), MoneyNumberFormat.THOUSANDST_POINT_TWO));//应收金额
		    map.put("payedMoney", "¥ " + MoneyNumberFormat.getThousandsMoney(Double.parseDouble(df.format(getRMBMoney(airticketOrder.getPayedMoney()))), MoneyNumberFormat.THOUSANDST_POINT_TWO));//已付金额
		    map.put("keHu",FreeMarkerUtil.StringFilter(agentinfo.getAgentName() == null ? "" : agentinfo.getAgentName()));//客户
		    map.put("applyPerson", FreeMarkerUtil.StringFilter(UserUtils.getUserNameById(Long.valueOf(airticketOrder.getSalerId()))));  //申请人 变更为销售
		    Date date = DateUtils.dateFormat(reviewInfo.get("createDate"), "yyyy-MM-dd HH:mm:ss");
		    DateUtils.formatCustomDate(date, "yyyy年MM月dd日");
		    map.put("applyDate",  DateUtils.formatCustomDate(date, "yyyy年MM月dd日"));	
		}
		
		String spDate = "   年   月   日";//付款日期 置为空
		String officeName = UserUtils.getUser().getCompany().getName();
		boolean doFlag = true;
		if(officeName != null && !"".equals(officeName) && (officeName.contains("环球行") || officeName.contains("拉美途"))){//除环球行外的供应商 显示 确认付款时间
			doFlag = false;
		}
		if("1".equals(reviewInfo.get("payStatus")) && doFlag){
		    Date date = DateUtils.dateFormat(reviewInfo.get("updateDate"), "yyyy-MM-dd HH:mm:ss");
		    DateUtils.formatCustomDate(date, "yyyy年MM月dd日");
			spDate = DateUtils.formatCustomDate(date, "yyyy年MM月dd日");
		}		
		map.put("spDate", spDate);
		
		//获取汇率
		Map<String, BigDecimal> rateMap = Maps.newHashMap();
		List<MoneyAmount> moneyAmounts = moneyAmountService.findAmountsByReviewId(Long.valueOf((reviewId)));
		if(CollectionUtils.isNotEmpty(moneyAmounts)){
			for(MoneyAmount amount : moneyAmounts) {
				Integer currencyId = amount.getCurrencyId();
				String currencyMark = currencyService.findCurrencyMark(currencyId.toString());
				if(!rateMap.containsKey(currencyMark)) {
					rateMap.put(currencyMark, amount.getExchangerate());
				}
			}
		}
		
		String bankInfo = "";
		String bankAccount = "";
		String shoukuanrenStr = "";
		String strMoney = "";
		if("pay".equals(option)) {            //点击支付记录的打印按钮
			if(StringUtils.isNotBlank(payId)) {
				PayInfoDetail payDetail = refundService.getPayInfoByPayId(payId, prdType);		
				if(payDetail != null){
					Integer payType = payDetail.getPayType();
					if(payType==4 || payType==6 || payType==7 || payType==8) {   //只有汇款、银行转账、汇票和pos机支付产生收款账户
						String bankName = payDetail.getTobankName()==null? "":payDetail.getTobankName();
						bankAccount = payDetail.getTobankAccount()==null? "" : payDetail.getTobankAccount();			
						shoukuanrenStr = payDetail.getPayerName()==null? "":payDetail.getPayerName();
					
						String accountName = bankInfoDao.getAccountName(agentinfo.getId(), Context.PLAT_TYPE_QD, bankName, bankAccount, "");
						bankInfo = bankName + "</br>" + accountName;
					}
					
					//若为人民币 则转化为大写 --》新业务变更为 不管是什么币种 需转换为人民币 展示
					// 转换退款金额为人民币,金额为0时，不显示
					String moneyStr = payDetail.getMoneyDispStyle();
					List<Object[]> moneys = MoneyNumberFormat.getMoneyFromString(moneyStr, "\\+");
					BigDecimal totalMoneyDecimal = new BigDecimal(0);
					if(CollectionUtils.isNotEmpty(moneys)) {
						for(Object[] money :moneys) {
							String currencyMark = money[0].toString();
							BigDecimal moneyNumber = new BigDecimal(Double.valueOf(money[1].toString()));
							if(rateMap.containsKey(currencyMark)) {
								totalMoneyDecimal = totalMoneyDecimal.add(rateMap.get(currencyMark).multiply(moneyNumber));
							}else {
								Currency currency = currencyService.findCurrencyByCurrencyMark(currencyMark,UserUtils.getUser().getCompany().getId());
								BigDecimal rate = null==currency? new BigDecimal(0):currency.getCurrencyExchangerate();
								totalMoneyDecimal = totalMoneyDecimal.add(rate.multiply(moneyNumber));
							}
						}
					}
					if(totalMoneyDecimal.doubleValue() != BigDecimal.ZERO.doubleValue()) {
						strMoney =  MoneyNumberFormat.getThousandsMoney(Double.parseDouble(totalMoneyDecimal.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO);
					}
				}
			}
		}else if("order".equals(option)) {		//点击退款列表中的打印按钮
			List<Orderpay> orderpays = orderpayDao.findLastDateOrderpay(Long.parseLong(orderId), Integer.parseInt(prdType));
			if(orderpays != null && orderpays.size() != 0){
				String bankName = orderpays.get(0).getBankName() == null ? "" : orderpays.get(0).getBankName();
				bankAccount = orderpays.get(0).getBankAccount() == null ? "" : orderpays.get(0).getBankAccount();			
				shoukuanrenStr = orderpays.get(0).getPayerName() + "";
				bankInfo = bankName+"</br>"+bankAccount;
			}
			Currency currency = currencyService.findCurrency(Long.valueOf(reviewInfo.get("currencyId")));
			BigDecimal exchangerate = new BigDecimal(1);
			if(currency != null) {
				if(rateMap.containsKey(currency.getCurrencyMark())) {
					exchangerate = rateMap.get(currency.getCurrencyMark());
				}else {
					exchangerate = currency.getCurrencyExchangerate();
				}
			}
			String money = reviewInfo.get("refundPrice").toString().replace(",", "");
			BigDecimal multiply = exchangerate.multiply(BigDecimal.valueOf(Double.parseDouble(money)));
			strMoney = MoneyNumberFormat.getThousandsMoney(Double.parseDouble(multiply.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO);
		}
				
		map.put("shouKuanRen", FreeMarkerUtil.StringFilter(shoukuanrenStr));//收款人
		map.put("bankInfo", bankInfo);//银行信息
		map.put("bankAccount", bankAccount);
		
		String remarDeposite = "";
		if(reviewInfo.get("flowType") != null && "7".equals(reviewInfo.get("flowType"))){
			List<Refund> list = refundService.findLastPayByRecordIdAndStatus(Long.parseLong(reviewId));
			if(list != null && list.size() != 0){
				remarDeposite = list.get(0).getRemarks();
			}
			map.put("remarks", remarDeposite);//退款说明
		} else {
			map.put("remarks", FreeMarkerUtil.StringFilter(reviewInfo.get("remark") == null ? "" : reviewInfo.get("remark")));//退款说明
		}
		
		String refundPrice = "";
		String refundUpCase = "";
		if(StringUtils.isNotBlank(strMoney)) {
			refundPrice  = "¥ " + strMoney; 
			refundUpCase = "人民币" + MoneyNumberFormat.digitUppercase(Double.parseDouble(strMoney.replaceAll(",", "")));
		}
		
		map.put("upcase", refundUpCase);
		map.put("refundPrice", refundPrice);//退款金额
		
		//判断是否是环球行用户
		if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())) {
			map.put("groupCodeName", "订单团号");
		}else {
			map.put("groupCodeName", "团号");
		}

		return map;
	}
	/**
	 * 根据产品产品ID，获取该产品包括的订单列表
	 * @author gao
	 * @date 20151112 
	 * @param activityID
	 * @return
	 */
	public List<ActivityInfo> getActivityInfo(Long activityGroupID){
		List<ActivityInfo> activityInfoList = new ArrayList<ActivityInfo>(); // 返回列表
		if(activityGroupID!=null){
			List<ProductOrderCommon> proList = productOrderCommonDao.getProductOrderList(activityGroupID);
			if(!proList.isEmpty()){
				Iterator<ProductOrderCommon> iter = proList.iterator();
				while(iter.hasNext()){
					ProductOrderCommon comm = iter.next();
					ActivityInfo info = new ActivityInfo();
					info.setOrderNo(comm.getOrderNum()); // 订单号
					info.setShell(comm.getSalerName()); // 销售名
					info.setAgentName(comm.getOrderCompanyName()); // 渠道名
					if(comm.getCreateBy()!=null){
						info.setOrderUser(comm.getCreateBy().getName()); // 下单人
					}
					if(comm.getOrderTime()!=null){
						info.setReserveDate(DateUtils.formatCustomDate(comm.getOrderTime(), DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS)); // 预订时间
					}
					info.setPersonNum(comm.getOrderPersonNum()); // 总人数
					info.setOrderStatus(backOrderType(comm.getPayStatus())); // 订单状态
					info.setTotalAmountUuid(comm.getTotalMoney());
					info.setPayedAmountUuid(comm.getPayedMoney());
					info.setAccountedAmountUuid(comm.getAccountedMoney());
					info.setTotalAmount(backMoneyAmount(comm.getTotalMoney()));
					info.setPayedAmount(backMoneyAmount(comm.getPayedMoney()));
					info.setAccountedAmount(backMoneyAmount(comm.getAccountedMoney()));
					activityInfoList.add(info);
				}
			}
		}
		return activityInfoList;
	}
	/**
	 * 根据uuid，获取多币种字符串
	 * @param uuid
	 * @return
	 */
	private String backMoneyAmount(String uuid){
		if(StringUtils.isBlank(uuid)){
			return "￥0.00";
		}
		String strAmount = moneyAmountService.getMoney(uuid);
		if(StringUtils.isBlank(strAmount)){
			strAmount = "￥0.00";
		}
		return strAmount;
	}
	/**
	 * 根据订单状态数值,返回文字
	 * 支付状态1-未支付全款 2-未支付订金 3-已占位 4-已支付订金 5-已支付全款 99-已取消
	 * @param orderStatus
	 * @return
	 */
	private String backOrderType(Integer orderStatus){
		String str = null;
		if(orderStatus!=null){
			switch(orderStatus){
				case 1:
					str =  "未支付全款";
				break;
				case 2:
					str =  "未支付订金";
				break;
				case 3:
					str =  "已占位";
				break;
				case 4:
					str =  "已支付订金";
				break;
				case 5:
					str =  "已支付全款";
				break;
				case 7:
					str =  "待计调确认";
					break;
				case 8:
					str =  "待财务确认";
					break;
				case 9:
					str =  "已撤销占位";
					break;
				case 99:
					str =  "已取消";
				break;
				case 111:
					str =  "已删除";
					break;
			}
		}
		return str;
	}
	
	/**
	 * 多币种的金额转化为人民币
	 * @param totalMoney
	 */
	private BigDecimal getRMBMoney(String totalMoney) {
		
		BigDecimal result = new BigDecimal(0);
		List<MoneyAmount> list = moneyAmountService.findAmountBySerialNum(totalMoney);
		BigDecimal rate;
		for(MoneyAmount temp : list){
			rate = moneyAmountService.getExchangerateByUuid(totalMoney, temp.getCurrencyId());
			if(rate == null){
				rate = new BigDecimal(1);
			}
			result = result.add(temp.getAmount().multiply(rate));
		}
		return result;
	}

	/**
	 * 根据订单id查询参团订单
	 * @param orderId
	 * @return
	 */
	public ProductOrderCommon getProductorderById(Long orderId){
		
		return productOrderCommonDao.findOne(orderId);
	}

	/**
	 * 根据渠道id查找单团订单信息，并且排除99：已取消， 111：已删除的订单
	 */
	public List<ProductOrderCommon> findByOrderCompany(Long agentId) {
		return productOrderCommonDao.findByOrderCompany(agentId);
	}

	/**
	 * 查找当前团期下的预定总人数
	 * @date 2016年4月12日
	 */
	public Long findTotalNumByGroupId(Long groupId){
		Long orderPersonNum = new Long(0);
		try{
			orderPersonNum= productOrderCommonDao.findTotalOrderPersonNumByGroupId(groupId);
		}catch (Exception e){
			e.printStackTrace();
		}
		return orderPersonNum == null ? 0 : orderPersonNum;
	}
	public boolean hasOrder(Long groupId) {
		StringBuffer sb = new StringBuffer();
        sb.append("select * from productorder where productGroupId =? and payStatus in (3,4,5) and delFlag = 0 ");
		List<Map <String, Object>> map =  productOrderCommonDao.findBySql(sb.toString(), Map.class, groupId);
        return map.size() > 0;
	}
	
	/**
	 * 根据团号查找订单
	 * @param groupcode
	 * @return
	 */
	public ProductOrderCommon getProductOrderByGroupCode(String groupCode,String productId,String groupId){
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT o.* FROM productorder o LEFT JOIN activitygroup ag ON o.productGroupId = ag.id ")
			 .append("WHERE ag.groupCode = ? AND o.payStatus NOT IN (99, 111)  and o.delFlag = 0 and o.productId=? and o.productGroupId = ?");
		List<ProductOrderCommon> orderCommons = productOrderCommonDao.findBySql(sbf.toString(),ProductOrderCommon.class, groupCode,productId,groupId);
		if(orderCommons.size() > 0){
			return orderCommons.get(0);
		}
		return null;
	}
	
	/**
	 * 获得currencyId and currencyPrice
	 * @param list
	 * @return
	 * @author chao.zhang
	 */
	public Map<String,String> getTotalCurrencyPriceAndCurrencyId(List list){
		String currencyId = "";
		String currencyPrice = "";
		for(int n=0;n<list.size();n++) {
			try {
				Class<?> clazz = list.get(n).getClass();
				Method method1 = clazz.getDeclaredMethod("getCurrencyId");
				Method method2 = clazz.getDeclaredMethod("getAmount");
				if(n==0){
					currencyId +=method1.invoke(list.get(n));
					currencyPrice += method2.invoke(list.get(n)).toString();
				}else{
					currencyId += "," + method1.invoke(clazz).toString();
					currencyPrice += "," + method2.invoke(clazz).toString();
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		Map<String,String> map = new HashMap<String,String>();
		map.put("currencyId", currencyId);
		map.put("currencyPrice", currencyPrice);
		return map;
	}
	
	/**
	 * 将金额存入orderpayDetail
	 * @param curs
	 * @param pays
	 * @param list
	 * @param orderPayDetail
	 * @author chao.zhang
	 */
	public void setOrderPayCurrencyPrice(String[] curs,String[] pays,List list,OrderPayDetail orderPayDetail){
		String payPrice2 = "";
		Integer cur;
		boolean flag = false;
		BigDecimal payedMoney = null;
		BigDecimal payMoney = null;
		if(curs != null && curs.length != 0 && curs.length == pays.length){
			for(int i = 0; i < curs.length; i++){
				cur = Integer.parseInt(curs[i]);
				flag = false;
				for(int n=0;n<list.size();n++){
					Class<?> clazz = list.get(n).getClass();
					try {
						Method method = clazz.getDeclaredMethod("getCurrencyId");
						Integer currencyId = Integer.parseInt(method.invoke(list.get(n)).toString());
						if(cur == currencyId){
							Method method1 = clazz.getDeclaredMethod("getAmount");
							payedMoney = new BigDecimal(method1.invoke(list.get(n)).toString());//已付金额
							payMoney = new BigDecimal(pays[i]);//应付金额
							if(i == 0){
								payPrice2 += payMoney.subtract(payedMoney).toString();
							} else {
								payPrice2 += "," + payMoney.subtract(payedMoney).toString();
							}
							flag = true;
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					} 
				}
				if(!flag){
					if(i == 0){
						payPrice2 += pays[i];
					} else {
						payPrice2 += "," + pays[i];
					}
				}
			}
			orderPayDetail.setPayCurrencyPrice(payPrice2);
		}
	}
	
	public Map<String,Object> getTravelerCount(Long orderId){
		StringBuffer sbf = new StringBuffer();
		sbf.append("SELECT  ")
			.append("CASE WHEN tr.personType =1 THEN tr.id ELSE -1 END AS adultNum, ")
			.append("CASE WHEN tr.personType = 2 THEN tr.id ELSE -1 END AS childrenNum, ")
			.append("CASE WHEN tr.personType = 2 THEN tr.id ELSE -1 END AS specialNum ")
			.append("FROM traveler tr WHERE tr.orderId =? ");
		List<Map<String,Object>> list = productOrderCommonDao.findBySql(sbf.toString(),Map.class, orderId);
		if(list.size() > 0){
			Integer adultNum = 0 ;
			Integer childrenNum = 0 ;
			Integer specialNum = 0 ;
			for(Map<String,Object> map : list){
				if(! map.get("adultNum").toString().equals("-1")){
					adultNum++;
				}
				if(! map.get("childrenNum").toString().equals("-1")){
					childrenNum++;
				}
				if(! map.get("specialNum").toString().equals("-1")){
					specialNum++;
				}
			}
			Map<String,Object> trMap = new HashMap<String, Object>();
			trMap.put("adultNum", adultNum);
			trMap.put("childrenNum", childrenNum);
			trMap.put("specialNum", specialNum);
			return trMap;
		}
		Map<String,Object> trMap = new HashMap<String, Object>();
		trMap.put("adultNum", 0);
		trMap.put("childrenNum", 0);
		trMap.put("specialNum", 0);
		return trMap;
	}
}
