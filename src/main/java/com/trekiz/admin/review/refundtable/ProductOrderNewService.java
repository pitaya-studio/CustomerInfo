package com.trekiz.admin.review.refundtable;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.google.common.collect.Maps;
import com.quauq.review.core.engine.ReviewService;
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
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;
import com.trekiz.admin.modules.hotel.service.HotelMoneyAmountService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.entity.ProductOrder;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.entity.Refund;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.repository.OrderContactsDao;
import com.trekiz.admin.modules.order.repository.OrderpayDao;
import com.trekiz.admin.modules.order.repository.PlatBankInfoDao;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.service.RefundService;
import com.trekiz.admin.modules.reviewreceipt.common.ReviewReceiptContext;
import com.trekiz.admin.modules.reviewreceipt.service.ReviewReceiptService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
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
public class ProductOrderNewService extends BaseService {
	
	@Autowired
	private ProductOrderCommonDao poc;
	@Autowired
	private MoneyAmountService moneyAmountService;
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
	private OrderContactsDao orderContactsDao;
	
	@Autowired
	private HotelMoneyAmountService hotelMoneyAmountService;
	
	@Autowired
	private VisaDao visaDao;
	
	@Autowired
	private RefundService refundService;
	
	@Autowired
	private ReviewReceiptService reviewReceiptService;
	
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
		//判断是否是批量打印
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
		
		Map<String, Object> reviewInfo = reviewService.getReviewDetailMapByReviewId(reviewId);
		//第一次打印时间
		map.put("printTime", reviewInfo.get("printDate") == null ? "" : DateUtils.formatCustomDate(DateUtils.dateFormat(reviewInfo.get("printDate").toString()),"yyyy/MM/dd HH:mm"));
		//填写日期
		Date date = DateUtils.dateFormat(reviewInfo.get("createDate") == null ? "" : reviewInfo.get("createDate").toString(), "yyyy-MM-dd HH:mm:ss");
	    DateUtils.formatCustomDate(date, "yyyy年MM月dd日");
	    map.put("applyDate",  DateUtils.formatCustomDate(date, "yyyy年MM月dd日"));
	    //团号、人数、出发日期、返回日期、产品线路、应收金额、已收金额、客户、电话、经办人
	    String groupCodePrint = FreeMarkerUtil.StringFilter(groupCode);
	    String personNum = "";
	    String startDate = "";
	    String endDate = "";
	    String productLine = FreeMarkerUtil.StringFilter("");
	    String totalMoney = "";
	    String payedMoney = "";
	    String customer = "";
	    String telephone = "";
	    String applyPerson = "";
	    
	    DecimalFormat  df =new DecimalFormat("#.00");//取两位小数
	    
	    Agentinfo agentinfo = agentinfoDao.findOne(Long.parseLong(agentId));  //客户
	   //产品类型: 1表示 单团产品, 2表示 散拼产品,3表示 游学产品,4表示 大客户产品,5表示 自由行产品,6表示 签证产品, 7表示 机票产品, 8表示 机票切位, 9表示 散拼切位
		if (StringUtils.isNotBlank(prdType) && (prdType.equals("1") || prdType.equals("2") || prdType.equals("3") || prdType.equals("4") || prdType.equals("5") || prdType.equals("10"))) {
			ProductOrder po = poc.findByOrderId(Long.valueOf(orderId)).get(0);
			groupCodePrint = FreeMarkerUtil.StringFilter(po.getActivityGroup().getGroupCode());// 团号
			personNum = po.getOrderPersonNum().toString();// 人数
			startDate = DateUtils.formatCustomDate(po.getActivityGroup().getGroupOpenDate(), "yyyy-MM-dd");// 出发日期
			
			int travelDays = travelActivityDao.findOne(po.getProductId()).getActivityDuration();
			endDate = DateUtils.formatCustomDate(new Date(po.getActivityGroup().getGroupOpenDate().getTime() + travelDays * 24 * 60 * 60 * 1000), "yyyy-MM-dd");// 返回日期
			
			if(po.getActivityGroup().getTravelActivity().getAcitivityName() != null) {
				productLine = FreeMarkerUtil.StringFilter(po.getActivityGroup().getTravelActivity().getAcitivityName());// 产品线路
			}
			
			totalMoney = "¥ " + MoneyNumberFormat.getThousandsMoney(Double.parseDouble(df.format(getRMBMoney(po.getTotalMoney()))),MoneyNumberFormat.THOUSANDST_POINT_TWO);// 应收金额
			payedMoney = "¥ " + MoneyNumberFormat.getThousandsMoney(Double.parseDouble(df.format(getRMBMoney(po.getPayedMoney()))),MoneyNumberFormat.THOUSANDST_POINT_TWO);// 已付金额
			
			if(po.getOrderCompanyName() != null) {
				customer = po.getOrderCompanyName();
			}
			
			applyPerson = FreeMarkerUtil.StringFilter(UserUtils.getUserNameById(Long.valueOf(po.getSalerId()))); // 申请人变更为销售
		    
		} else if (StringUtils.isNotBlank(prdType) && "6".equals(prdType)) {
			// 签证
			VisaOrder vo = visaOrderDao.findOne(Long.valueOf(orderId));
			VisaProducts visaProducts = visaProductDao.findOne(vo.getVisaProductId());
			
			personNum = vo.getTravelNum().toString();// 人数
			
			productLine = FreeMarkerUtil.StringFilter(visaProducts.getProductName());// 产品名称借用产品线路字段
			
			if (reviewInfo.get("processType") != null && "1".equals(reviewInfo.get("processType"))) {// 如果是退款的话
				totalMoney = "¥ " + MoneyNumberFormat.getThousandsMoney(Double.parseDouble(df.format(getRMBMoney(vo.getTotalMoney()))),MoneyNumberFormat.THOUSANDST_POINT_TWO);// 应收金额
				payedMoney = "¥ " + MoneyNumberFormat.getThousandsMoney(Double.parseDouble(df.format(getRMBMoney(vo.getPayedMoney()))),MoneyNumberFormat.THOUSANDST_POINT_TWO);// 已付金额
			} else if(reviewInfo.get("processType") != null && "7".equals(reviewInfo.get("processType"))){
				personNum = "1";
				if (reviewInfo.get("travelerId") != null && !"".equals(reviewInfo.get("travelerId").toString().trim())) {
					Visa visa = visaDao.findByTravelerId(Long.parseLong(reviewInfo.get("travelerId").toString()));
					totalMoney = "¥ " + MoneyNumberFormat.getThousandsMoney(Double.parseDouble(df.format(getRMBMoney(visa.getTotalDeposit()))),MoneyNumberFormat.THOUSANDST_POINT_TWO);// 游客的应收押金
					payedMoney = "¥ "+ MoneyNumberFormat.getThousandsMoney(Double.parseDouble(df.format(getRMBMoney(visa.getPayedDeposit()))),MoneyNumberFormat.THOUSANDST_POINT_TWO);// 游客的已收押金
				}
			}
			
			if(agentinfo!=null && agentinfo.getAgentName()!=null) {
				customer = agentinfo.getAgentName();
			}			
			
			applyPerson = FreeMarkerUtil.StringFilter(UserUtils.getUserNameById(Long.valueOf(vo.getSalerId())));  // 申请人变更为销售
		} else if (StringUtils.isNotBlank(prdType) && "7".equals(prdType)) {
			// 机票
			AirticketOrder airticketOrder = airticketOrderDao.getAirticketOrderById(Long.parseLong(orderId));
			ActivityAirTicket activityAirTicket = activityAirTicketDao.findOne(airticketOrder.getAirticketId());
			
			personNum = airticketOrder.getPersonNum() + "";// 人数
			startDate = DateUtils.formatCustomDate(activityAirTicket.getStartingDate(), "yyyy-MM-dd");// 出发日期
			
			String fromCity = dictService.findByValueAndType(activityAirTicket.getDepartureCity(), "from_area").getLabel();// 出发城市
			String arriveCity = AreaUtil.findAreaNameById(Long.parseLong(activityAirTicket.getArrivedCity()));// 到达城市
			String airTypeId = activityAirTicket.getAirType();
			String airType = "";// 机票类型
			if (Integer.parseInt(airTypeId) == 1) {
				airType = "多段";
				endDate = DateUtils.formatCustomDate(activityAirTicket.getReturnDate(), "yyyy-MM-dd");// 返回日期
			} else if (Integer.parseInt(airTypeId) == 2) {
				airType = "往返";
				endDate = DateUtils.formatCustomDate(activityAirTicket.getReturnDate(), "yyyy-MM-dd");// 返回日期
			} else if (Integer.parseInt(airTypeId) == 3) {
				airType = "单程";
			} 			
			productLine = FreeMarkerUtil.StringFilter(fromCity + "-->" + arriveCity + " " + airType + " " + startDate);// 产品线路
			
			totalMoney = "¥ " + MoneyNumberFormat.getThousandsMoney(Double.parseDouble(df.format(getRMBMoney(airticketOrder.getTotalMoney()))),MoneyNumberFormat.THOUSANDST_POINT_TWO);// 应收金额
			payedMoney = "¥ " + MoneyNumberFormat.getThousandsMoney(Double.parseDouble(df.format(getRMBMoney(airticketOrder.getPayedMoney()))),MoneyNumberFormat.THOUSANDST_POINT_TWO);// 已付金额

			if(agentinfo!=null && agentinfo.getAgentName()!=null) {
				customer = agentinfo.getAgentName();
			}
			
			applyPerson = FreeMarkerUtil.StringFilter(UserUtils.getUserNameById(Long.valueOf(airticketOrder.getSalerId()))); // 申请人变更为销售
		}
		
		//退款说明
		String refundRemark = FreeMarkerUtil.StringFilter("");
		if(reviewInfo.get("processType") != null && "7".equals(reviewInfo.get("processType"))){
			List<Refund> list = refundService.findLastPayByRecordIdAndStatus(Long.parseLong(reviewId));
			if(list != null && list.size() != 0){
				refundRemark = list.get(0).getRemarks();
			}
		} else if (reviewInfo.get("remark") != null ){
			refundRemark = FreeMarkerUtil.StringFilter(reviewInfo.get("remark").toString());
		}
		
		//电话	
		List<OrderContacts> oContacts = orderContactsDao.findOrderContactsByOrderIdAndOrderType(Long.valueOf(orderId), Integer.parseInt(prdType));
		if (oContacts != null && oContacts.size() > 0) {
			telephone = oContacts.get(0).getContactsTel();
		}
		
		//获取汇率
		Map<String, BigDecimal> rateMap = Maps.newHashMap();
		List<MoneyAmount> moneyAmounts = moneyAmountService.findAmountsByReviewUuId(reviewId);
		if(CollectionUtils.isNotEmpty(moneyAmounts)){
			for(MoneyAmount amount : moneyAmounts) {
				Integer currencyId = amount.getCurrencyId();
				String currencyMark = currencyService.findCurrencyMark(currencyId.toString());
				if(!rateMap.containsKey(currencyMark)) {
					rateMap.put(currencyMark, amount.getExchangerate());
				}
			}
		}
		
		//收款人、开户行、账户名、账号
		String shoukuanrenStr = "";
		String bankInfo = "";
		String bankAccount = "";
		String strMoney = "";
		if("pay".equals(option)) {
			if(StringUtils.isNotBlank(payId)) {
				PayInfoDetail payDetail = refundService.getPayInfoByPayId(payId, prdType);
				if(payDetail != null) {
					shoukuanrenStr = payDetail.getPayerName() == null? "":payDetail.getPayerName();
					
					Integer payType = payDetail.getPayType();				
					if(payType==4 || payType==6 || payType==7 || payType==8) {   //只有汇款、银行转账、汇票和pos机支付产生收款账户					
						String bankName = payDetail.getTobankName() == null? "":payDetail.getTobankName();
						bankAccount = payDetail.getTobankAccount() == null? "":payDetail.getTobankAccount();
					
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
						strMoney = MoneyNumberFormat.getThousandsMoney(Double.valueOf(totalMoneyDecimal.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO);
					}				
				}
			}
		}else if("order".equals(option)) {
			List<Orderpay> orderpays = orderpayDao.findLastDateOrderpay(Long.parseLong(orderId), Integer.parseInt(prdType));
			if(orderpays != null && orderpays.size() != 0){
				String bankName = orderpays.get(0).getToBankNname() == null ? (orderpays.get(0).getReceiveBankName()==null? "":orderpays.get(0).getReceiveBankName()) : orderpays.get(0).getToBankNname();
				bankAccount = orderpays.get(0).getToBankAccount() == null ? (orderpays.get(0).getReceiveAccount()==null? "":orderpays.get(0).getReceiveAccount()) : orderpays.get(0).getToBankAccount();			
				shoukuanrenStr = orderpays.get(0).getPayerName() == null ? "" : orderpays.get(0).getPayerName();
				bankInfo = bankName + "</br>" +bankAccount;
			}
			
			Currency currency = currencyService.findCurrency(Long.valueOf(reviewInfo.get("currencyId").toString()));
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
			strMoney = MoneyNumberFormat.getThousandsMoney(Double.valueOf(multiply.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO);
		}
		
					
		String refundPrice = "";
		String refundUpCase = "";
		if(StringUtils.isNotBlank(strMoney)) {
			refundPrice = "¥ " + strMoney;
			refundUpCase = "人民币" + MoneyNumberFormat.digitUppercase(Double.parseDouble(strMoney.replaceAll(",", "")));
		}
		
		//财务、审批、总经理		
		String companyUUid = UserUtils.getUser().getCompany().getUuid();
		MultiValueMap<Integer, User> valueMap = reviewReceiptService.obtainReviewer4Receipt(companyUUid, ReviewReceiptContext.RECEIPT_TYPE_REFUND, reviewId); 		
		String cwzg = getNames(valueMap.get(ReviewReceiptContext.RefundReviewElement.FINANCIAL));//财务
		String bmjl = getNames(valueMap.get(ReviewReceiptContext.RefundReviewElement.REVIEWER)); //审批
		String sp = getNames(valueMap.get(ReviewReceiptContext.RefundReviewElement.MANAGER));//经理
		
		//确认付款日期
		String spDate = "   年   月   日";//付款日期 置为空
		String officeName = UserUtils.getUser().getCompany().getName();
		boolean doFlag = true;
		if(officeName != null && !"".equals(officeName) && (officeName.contains("环球行") || officeName.contains("拉美途"))){//除环球行外的供应商 显示 确认付款时间
			doFlag = false;
		}
		if("1".equals(reviewInfo.get("payStatus").toString()) && doFlag){
		    Date spdate = DateUtils.dateFormat(reviewInfo.get("payConfirmDate") == null ? "" : reviewInfo.get("payConfirmDate").toString(), "yyyy-MM-dd HH:mm:ss");
		    DateUtils.formatCustomDate(spdate, "yyyy年MM月dd日");
			spDate = DateUtils.formatCustomDate(spdate, "yyyy年MM月dd日");
		}
		
		//添加入map中
		map.put("groupCodePrint", groupCodePrint);
		map.put("personNum", personNum);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("productName", productLine);
		map.put("totalMoney", totalMoney);
		map.put("payedMoney", payedMoney);
		map.put("refundPrice", refundPrice);
		map.put("remarks", refundRemark);
		map.put("upcase", refundUpCase);
		map.put("keHu", customer);
		map.put("tel", telephone);
		map.put("shouKuanRen", FreeMarkerUtil.StringFilter(shoukuanrenStr));
		map.put("bankInfo", bankInfo);
		map.put("bankAccount", bankAccount);
		map.put("applyPerson", applyPerson);
		map.put("cwzg", cwzg);
		map.put("bmjl", bmjl);
		map.put("sp", sp);
		map.put("spDate", spDate);
		
		//环球行用户更改团号为订单团号
		String groupCodeName = "团号";
		if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())) {
			groupCodeName = "订单团号";
		}
		
		map.put("groupCodeName", groupCodeName);
				
		return map;
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
	/**
	 * 多币种的金额转化为人民币
	 * @param totalMoney type 1酒店 2海岛游
	 */
	private Object getRMBHotelIslandMoney(String totalMoney, Integer type) {
		BigDecimal result = new BigDecimal(0);
		List<HotelMoneyAmount> list = hotelMoneyAmountService.getMoneyAmonutBySerialNum(totalMoney);
		BigDecimal rate;
		for(HotelMoneyAmount temp : list){
			rate = moneyAmountService.getExchangerateByUuid(totalMoney, temp.getCurrencyId());
			if(rate == null){
				rate = new BigDecimal(1);
			}
			result = result.add(new BigDecimal(temp.getAmount()).multiply(rate));
		}
		return result;
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
}
