package com.trekiz.admin.modules.order.web;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.query.service.ISelectService;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.GroupControlBoardService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.finance.entity.ReturnDifference;
import com.trekiz.admin.modules.finance.service.ReturnDifferenceService;
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.hotel.service.HotelMoneyAmountService;
import com.trekiz.admin.modules.hotel.service.HotelOrderService;
import com.trekiz.admin.modules.invoice.repository.OrderinvoiceDao;
import com.trekiz.admin.modules.invoice.service.OrderinvoiceService;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.service.IslandMoneyAmountService;
import com.trekiz.admin.modules.island.service.IslandOrderService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.airticket.service.IAirticketPreOrderService;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.entity.PlatBankInfo;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.entity.ZhifubaoInfo;
import com.trekiz.admin.modules.order.formBean.OrderPaySuccessBean;
import com.trekiz.admin.modules.order.repository.OrderpayDao;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderPayMoreService;
import com.trekiz.admin.modules.order.service.OrderPayService;
import com.trekiz.admin.modules.order.service.PlatBankInfoService;
import com.trekiz.admin.modules.order.service.ProductOrderService;
import com.trekiz.admin.modules.pay.entity.PayHotelOrder;
import com.trekiz.admin.modules.pay.entity.PayIslandOrder;
import com.trekiz.admin.modules.pay.model.PayProductOrder;
import com.trekiz.admin.modules.pay.service.PayHotelOrderService;
import com.trekiz.admin.modules.pay.service.PayIslandOrderService;
import com.trekiz.admin.modules.statisticAnalysis.home.service.OrderDateSaveOrUpdateService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.MoneyAmountUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.service.VisaOrderTravelerPayLogService;

/**
 * 订单公共付款
 * 
 * @author xb
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/orderPay")
public class OrderPayController extends BaseController {

	protected static final Logger logger = LoggerFactory
			.getLogger(OrderPayController.class);

	@Autowired
	OrderPayService orderPayService;
	@Autowired
	private OrderPayMoreService orderPayMoreService;
	@Autowired
	PlatBankInfoService platBankInfoService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private DocInfoService docInfoService;
	   
	@Autowired
	private OrderCommonService orderCommonService;
	   
	@Autowired
	private IAirticketPreOrderService airticketPreOrderService;
	
	@Autowired
	private IAirTicketOrderService airticketOrderService;
	   
	@Autowired
	private VisaOrderTravelerPayLogService visaOrderTravelerPayLogService;
	
	@Autowired
	private OrderpayDao orderpayDao;
   
   @Autowired
   private PayIslandOrderService payIslandOrderService;
   @Autowired
   private PayHotelOrderService payHotelOrderService;
   @Autowired
   private IslandMoneyAmountService islandMoneyAmountService;
   @Autowired
   private HotelMoneyAmountService hotelMoneyAmountService;
   @Autowired
   private HotelOrderService hotelOrderService;
   @Autowired
   private IslandOrderService islandOrderService;
   @Autowired
   private OrderinvoiceDao orderinvoiceDao;
   @Autowired
   private OrderinvoiceService orderinvoiceService;
   @Autowired
   private ISelectService selectService;
   @Autowired
   private ReturnDifferenceService returnDifferenceService;
   @Autowired
   private ActivityGroupService activityGroupService;
   @Autowired
   private ProductOrderService productOrderService;
   @Autowired
   private GroupControlBoardService groupControlBoardService;
   @Autowired
   private OrderDateSaveOrUpdateService orderDateSaveOrUpdateService;
   
	@RequestMapping(value = "pay")
	public String orderPay(
			@RequestParam Long orderId,/* 订单id */
			@RequestParam String orderNum,/* 订单号 */
			@RequestParam Integer payPriceType,/* 支付款类型（全款、定金、尾款） */
			@RequestParam Integer orderType,/* 订单类型 */
			@RequestParam Integer businessType,/* 业务类型 1-订单 2-游客 */
			Integer agentId,/* 渠道商ID */
			@RequestParam String paramCurrencyId,
			@RequestParam String paramCurrencyPrice,
			@RequestParam(required = false) String isCommonOrder,/* 是否是单团订单 */
			@RequestParam(required = false) String visaId,/* 交押金的时候用到visa表的主键 */
			@RequestParam(required = false) String paramTotalCurrencyId,/* 订单总额 */
			@RequestParam(required = false) String paramTotalCurrencyPrice,/* 订单总额 */
			@RequestParam(required = false) String orderDetailUrl,/* 订单详情URL（用于支付页面下方的“查看订单”按钮的响应URL）*/
			Model model) {
		
		// 金额信息
		if (StringUtils.isBlank(paramCurrencyId) || StringUtils.isBlank(paramCurrencyPrice) || paramCurrencyId.split(",").length != paramCurrencyPrice.split(",").length) {
			model.addAttribute("errorMsg", "请求的URL中预付金额部分信息不正确！");
			return "modules/order/notCanPay";
		}
		
		// 订单总额
		if (StringUtils.isBlank(paramTotalCurrencyId) || StringUtils.isBlank(paramTotalCurrencyPrice) || paramTotalCurrencyId.split(",").length != paramTotalCurrencyPrice.split(",").length) {
			model.addAttribute("errorMsg", "请求的URL中订单总额Key[paramTotalCurrencyId、paramTotalCurrencyPrice]必须包含并且不能为空！");
			return "modules/order/notCanPay";
		}
		
		if (Context.ORDER_TYPE_JP == orderType) {
			AirticketOrder order = airticketOrderService.getAirticketorderById(orderId);
			if(null != order)
				agentId = order.getAgentinfoId().intValue();
		} else {
			ProductOrderCommon order = orderCommonService.getProductorderById(orderId);
			if(null != order){
				agentId = order.getOrderCompany().intValue();
			}
			//下单的订单的时候
			if(order.getDifferenceFlag() == 1 && orderType == Context.ORDER_TYPE_SP){
				ProductOrderCommon productOrderCommon = orderCommonService.getProductorderById(orderId);
				List<Object[]> amonut = moneyAmountService.getMoneyAmonut(productOrderCommon.getDifferenceMoney());
				Object[] objects = amonut.get(0);
				ActivityGroup activityGroup = activityGroupService.findById(productOrderCommon.getProductGroupId());
				String[] currencyId = activityGroup.getCurrencyType().split(",");
				String differenceCurrencyId = currencyId[0];
				Currency currency = currencyService.findById(Long.parseLong(differenceCurrencyId));
				model.addAttribute("differenceCurrencyId",differenceCurrencyId);
				model.addAttribute("differenceCurrencyMark",currency.getCurrencyMark());
				model.addAttribute("differenceFlag",order.getDifferenceFlag());
				List<MoneyAmount> list = moneyAmountService.findAmountBySerialNum(productOrderCommon.getDifferenceMoney());
				if(list.size() != 0){
					model.addAttribute("differenceMoney",list.get(0).getAmount());
				}
				ReturnDifference returnDifference = returnDifferenceService.getDifferenceByOrderId(productOrderCommon.getId().intValue());
				model.addAttribute("returnDifference", returnDifference);
				String[] paramCurrencys = paramTotalCurrencyId.split(",");
				String[] prices = paramTotalCurrencyPrice.split(",");
				BigDecimal big = new BigDecimal(0);
				String s = "";
				for(int i = 0 ; i < paramCurrencys.length ; i ++){
					if(differenceCurrencyId.equals(paramCurrencys[i])){
						big = new BigDecimal(prices[i]).add(new BigDecimal(objects[3].toString()));
						prices[i] = big.toString();
					}
					if(StringUtils.isBlank(s)){
						s = prices[i];
					}else{
						s =  s+","+prices[i];
					}
				}
				paramTotalCurrencyPrice = s;
			}
		}
		// 把渠道商的账户信息查询出来放到前台
		if (agentId != null && agentId > 0) {
			Agentinfo agentinfo = agentinfoService.findAgentInfoById(agentId.longValue());
			//List<PlatBankInfo> agentBanks = platBankInfoService.findByBeLongPlatIdAndPlatType(agentinfo.getId(), Context.PLAT_TYPE_QD);
			List<Map<String,Object>> agentBanks = platBankInfoService.findByBeLongPlatIdAndPlatTypeforDistinct(agentinfo.getId(), Context.PLAT_TYPE_QD);
			model.addAttribute("agentBanks", agentBanks);
			model.addAttribute("payerName",agentinfo.getAgentName());
			
			//判定月结/后付费项目的状态
			Integer paymentStatusFlag = orderPayMoreService.processPaymentStatusFlag("1", agentId);
			if (orderType != null && orderType.intValue() == Context.ORDER_TYPE_QZ) {
				paymentStatusFlag = 0;
			}
			model.addAttribute("paymentStatusFlag", paymentStatusFlag);
			if (paymentStatusFlag == 1) {
				Integer paymentStatus = orderPayMoreService.getAgentPaymentType(agentId);
				model.addAttribute("paymentStatus", paymentStatus);
				model.addAttribute("paymentStatusLblDesc", orderPayMoreService.processPaymentStatusDesc(paymentStatus));
			}
			
			//paymentTypeRadioFlag表示月结/后付费的状态(0:月结/后续费可操作;1:月结/后续费不可操作-disbled状态)
			if (orderType != null && orderType.intValue() != Context.ORDER_TYPE_QZ) {
				//查看该订单是否已支付
				List<Orderpay> orderPayList = orderPayService.findOrderpayByOrderId(orderId, orderType);
				if (orderPayList == null || orderPayList.size() == 0) {
					model.addAttribute("paymentTypeRadioFlag", 0);
				} else {
					model.addAttribute("paymentTypeRadioFlag", 1);
				}
			} else {
				model.addAttribute("paymentTypeRadioFlag", 0);
			}
		}

		
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		model.addAttribute("curlist", currencylist);
		Long supplierId = UserUtils.getUser().getCompany().getId();
		model.addAttribute("supplierId", supplierId);
		//获取供应商开户行
		List<String> supplierBankNames = officeService.getOfficePlatBankInfoById(supplierId);
		model.addAttribute("supplierBanks", supplierBankNames);
		
		
		// 验证登陆者和订单创建者是否是同一个批发商的,是(true):可以随意支付款项;不是(false):必须全款支付,如果是交押金,不用做验证
		if (String.valueOf(supplierId).equals(String.valueOf(agentId)) || (payPriceType != null && String.valueOf(Context.MONEY_TYPE_SYJ).equals(payPriceType))) {
			model.addAttribute("quankuanValidate", "false");
		} else {
			model.addAttribute("quankuanValidate", "true");
		}
		
		boolean totalCurrencyFlag = true;
		if (orderType != null && orderType.intValue() == Context.ORDER_TYPE_QZ) {
			totalCurrencyFlag = false;
		}
	
		//因公支付宝 针对拉美途
		List<ZhifubaoInfo> list = selectService.getDistinctAlipay(UserUtils.getUser().getCompany().getId());
		model.addAttribute("alipay",list);
		for(ZhifubaoInfo info:list){
			if(info.getDelFlag().equals("0")){
				model.addAttribute("account",selectService.getAlipayByName(info.getName()));
			}
		}
		
		model.addAttribute("totalCurrencyFlag", totalCurrencyFlag);
		
		// 判断在订单中是否有预开发票  0444需求
		model.addAttribute("hasPreOpeninvoice", orderinvoiceDao.findHasPreOpeninvoiceInOrder(Integer.parseInt(orderId.toString())));
		
		//向页面传递传入的参数
		model.addAttribute("orderId", orderId);
		model.addAttribute("businessType", businessType);
		model.addAttribute("orderNum", orderNum);
		model.addAttribute("payPriceType", payPriceType);
		model.addAttribute("orderType", orderType);
		model.addAttribute("agentId", agentId);
		model.addAttribute("isCommonOrder", isCommonOrder);
		model.addAttribute("visaId", visaId);
		model.addAttribute("paramCurrencyId", paramCurrencyId.split(","));
		model.addAttribute("paramCurrencyPrice", paramCurrencyPrice.split(","));
		model.addAttribute("paramTotalCurrencyId", paramTotalCurrencyId.split(","));
		model.addAttribute("paramTotalCurrencyPrice", paramTotalCurrencyPrice.split(","));
		if(orderDetailUrl.indexOf("goUpdateVisaOrderForSales") != -1) {
			orderDetailUrl = orderDetailUrl + "&details=1";
		}
		model.addAttribute("orderDetailUrl",orderDetailUrl);
		model.addAttribute("orderListUrl", orderPayService.getOrderListUrl(orderType));
		//收款单位
		model.addAttribute("comOffice",UserUtils.getUser().getCompany().getName());
		return "modules/order/payOrder";
	}

	/**
	 * 
	 * @param orderId
	 *            订单ID
	 * @param orderNum
	 *            订单号
	 * @param orderType
	 *            订单类型
	 * @param currencyId
	 *            币种ID
	 * @param dqzfprice
	 *            币种对应的价格
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "savePay")
	public String savePay(@RequestParam Long orderId,/* 订单ID */
			@RequestParam String orderNum,/* 订单号 */
			@RequestParam Integer businessType,/* 业务类型 1-订单 2-游客 */
			@RequestParam Integer orderType,/* 订单类型 */
			@RequestParam Integer payType,/* 支付类型，快速支付，支票，现金支付，汇款 */
			@RequestParam Integer payPriceType,/* 全款、尾款、定金等 */
			@RequestParam(required = false) Integer paymentStatus,/* 结算(0表示支付操作;1表示月结/后付费;) */
			@RequestParam(required = false) Integer[] currencyIdPrice,
			@RequestParam(required = false) BigDecimal[] dqzfprice,
			@RequestParam(required = false) String remarks,
			@RequestParam(required = false) Long[] DocInfoIds,
			/* 快速支付时需要填写的支付方式 */
			@RequestParam(required = false) String fastPayType,
			// 汇款时需要的信息
			@RequestParam(required = false) String bankName,/* 开户行bankId */
			@RequestParam(required = false) String bankAccount,/* 开户行账户 */
			@RequestParam(required = false) String toBankNname,/* 转入行bankId */
			@RequestParam(required = false) String toBankAccount,/* 转入行bankId */
			// 支票支付需要的信息
			@RequestParam(required = false) String payerName, /* 付款单位 */
			@RequestParam(required = false) String checkNumber,/* 支票号 */
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date invoiceDate,/* 开票日期 */
			@RequestParam(required = false) String isCommonOrder,/* 是否是单团订单 */
			@RequestParam(required = false) String visaId,/* 交押金的时候用到visa表的主键 */
			// 关联发票的ids 0444需求
			@RequestParam(required = false) String relationInvoiceIds,/* 关联发票ids */
			HttpServletRequest request, ModelMap model) {
		
		if (!(dqzfprice != null && currencyIdPrice != null && dqzfprice.length > 0 && currencyIdPrice.length > 0 && dqzfprice.length == currencyIdPrice.length)) {
			model.addAttribute("errorMsg", "支付金额信息错误，请再次尝试支付。");
			return "modules/order/notCanPay";
		}
		String returnUrl = "";
		
		// 0444需求
		changeInvoiceReceivedPayStatus(relationInvoiceIds);
		// 0444需求
		
		try {
			//-------538--------
			bankName = request.getParameter("realBankName");
			bankAccount = request.getParameter("realBankAccount");
			returnUrl = orderPayService.savePay(request.getParameter("agentId"),orderId, orderNum, businessType, orderType, payType, payPriceType,
					paymentStatus, currencyIdPrice, dqzfprice, remarks, DocInfoIds, fastPayType, bankName, bankAccount, toBankNname, 
					toBankAccount, payerName, checkNumber, invoiceDate, isCommonOrder, visaId, request, model);
			// 0524需求 团期余位变化,记录在团控板中
			ProductOrderCommon order = productOrderService.getProductorderById(orderId);
			groupControlBoardService.insertGroupControlBoard(2, order.getOrderPersonNum(), "系统报名生成订单付款 扣减余位 " + order.getOrderPersonNum() + "个", order.getProductGroupId(), -1);
			// 0524需求 团期余位变化,记录在团控板中
			
			//-------by------junhao.zhao-----2017-03-22-----向表order_data_statistics中添加数据---开始-------------------------------------------------------------
			// “全款支付”、“订金占位”收款成功后加到表order_data_statistics中.
			if (orderDateSaveOrUpdateService.whetherAddOrUpdate(order)) {
				orderDateSaveOrUpdateService.addOrderDataStatistics(order);
			}
			//-------by------junhao.zhao-----2017-03-22-----向表order_data_statistics中添加数据---结束-------------------------------------------------------------
			
		} catch (Exception e) {
			returnUrl = "modules/order/notCanPay";
		}
		return returnUrl;
	}
	
	@ResponseBody
	@RequestMapping(value = "/getAccountByBankId/{bankId}/{agentId}/{type}")
	public Object getAccountByBankId(@PathVariable Long bankId,
			@PathVariable Long agentId, @PathVariable Integer type) {
		List<PlatBankInfo> platBankInfoList = platBankInfoService
				.getPlatBankInfoByBeLongPlatId(agentId, type, bankId);
		return platBankInfoList;
	}

	@RequestMapping(value = "/getOpenBankById/{belongPlatId}")
	@ResponseBody
	public void getOpenBankById(@PathVariable String belongPlatId,
			HttpServletResponse response,
			@RequestParam(required = false) String bankName) {
		PrintWriter out = null;
		try {
			bankName = URLDecoder.decode(bankName, "UTF-8");
			List<String> banks = officeService.getOfficePlatBankInfo(
					Long.parseLong(belongPlatId), bankName);
			JSONArray jsonObj = JSONArray.fromObject(banks);
			out = response.getWriter();
			response.getWriter().println(jsonObj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/getAgentOpenBankById/{belongPlatId}")
	@ResponseBody
	public void getAgentOpenBankById(@PathVariable String belongPlatId,
			HttpServletResponse response,
			@RequestParam(required = false) String bankName) {
		PrintWriter out = null;
		try {
			bankName = URLDecoder.decode(bankName, "UTF-8");
			List banks = orderPayMoreService.getBankAccountInfo(Long.parseLong(belongPlatId), bankName);
			JSONArray jsonObj = JSONArray.fromObject(banks);
			/*bankName = URLDecoder.decode(bankName, "UTF-8");
			List<PlatBankInfo> banks = platBankInfoService.findByBeLongPlatIdAndPlatType(Long.valueOf(belongPlatId), Context.PLAT_TYPE_QD);;
			JSONArray jsonObj = JSONArray.fromObject(banks);*/
			out = response.getWriter();
			response.getWriter().println(jsonObj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	/**
	 * 由ID取得相应的支付记录
	 * @param orderPayId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getOrderPayInfo", method = RequestMethod.POST)
	public Object getOrderPayInfo(@RequestParam Long orderPayId){
		Map<String, Object> data = new HashMap<String, Object>();
		
		Orderpay orderpay = orderPayService.findOrderpayById(orderPayId);
		orderpay.setMoneySerialNum(moneyAmountService.getMoney(orderpay.getMoneySerialNum()));
		data.put("orderpay", orderpay);
		Long supplierId = UserUtils.getUser().getCompany().getId();
		List<DocInfo> list =null ;
	    if(StringUtils.isNotBlank(orderpay.getPayVoucher())){
	    	String[] docId = orderpay.getPayVoucher().split(",");
	    	List<Long> docIdList = new ArrayList<Long>();
	    	for(int index=0;index<docId.length;index++){
	    		docIdList.add(Long.parseLong(docId[index]));
	    	}
	    	list = docInfoService.getDocInfoByIds(docIdList);
	    }
		List<String> supplierBankNames = officeService
				.getOfficePlatBankInfoById(supplierId);
		if(orderpay.getInvoiceDate() != null){
		   data.put("invoiceDate",DateUtils.formatDate(orderpay.getInvoiceDate(), "yyyy-MM-dd"));
		}
	    data.put("supplierBanks", supplierBankNames);
	    data.put("docInfoList", list);
		return data;
	}
	
	/**
	 * 由ID取得相应的支付记录
	 * @param orderPayId
	 * @param agentId   渠道id
	 * @return
	 */
	@RequestMapping(value = "getOrderPayInfo1", method = RequestMethod.GET)
	public Object getOrderPayInfo(HttpServletRequest request,HttpServletResponse response,Model model){
		Map<String, Object> data = new HashMap<String, Object>();
		String orderid = request.getParameter("orderid");
		String agentId = request.getParameter("agentid");
		
		String orderType = request.getParameter("orderType");
		
		String payVoucher = null;
		Date invoiceDate = null;
		String moneySerialNum = null;
		String accountDate = "";
		if(StringUtils.isNotEmpty(orderType) && Integer.parseInt(orderType) == Context.ORDER_TYPE_ISLAND.intValue()) {
			//根据海岛游订单支付ID查找订单支付信息
			PayIslandOrder payIslandOrder = payIslandOrderService.getById(Integer.parseInt(orderid));
			
			payVoucher = payIslandOrder.getPayVoucher();
			invoiceDate = payIslandOrder.getInvoiceDate();
			model.addAttribute("orderpay", payIslandOrder);
			
			moneySerialNum = payIslandOrder.getMoneySerialNum();
			Date date = new Date();
		    accountDate =  DateUtils.formatDate(date, "yyyy-MM-dd");
			
		} else if(StringUtils.isNotEmpty(orderType) && Integer.parseInt(orderType) == Context.ORDER_TYPE_HOTEL.intValue()) {
			//根据酒店订单支付ID查找订单支付信息
			PayHotelOrder payHotelOrder = payHotelOrderService.getById(Integer.parseInt(orderid));
			
			payVoucher = payHotelOrder.getPayVoucher();
			invoiceDate = payHotelOrder.getInvoiceDate();
			model.addAttribute("orderpay", payHotelOrder);
			
			moneySerialNum = payHotelOrder.getMoneySerialNum();
			Date date = new Date();
		    accountDate =  DateUtils.formatDate(date, "yyyy-MM-dd");
		} else {
			//根据订单支付ID查找订单支付信息
			Orderpay orderpay = orderPayService.findOrderpayById(Long.valueOf(orderid));
			//------------538---------
			if(orderpay.getOrderType()==2 && StringUtils.isNotBlank(orderpay.getDifferenceUuid())){
				String differenceUuid = orderpay.getDifferenceUuid();
				if(StringUtils.isNotBlank(differenceUuid)){
					ReturnDifference returnDifference = returnDifferenceService.getReturnDifferenceByUuid(differenceUuid);
					Integer currencyId = returnDifference.getCurrencyId();
					String differenceMark = CurrencyUtils.getCurrencyNameOrFlag(currencyId.longValue(), "0");
					model.addAttribute("returnDifference", returnDifference);
					model.addAttribute("differenceMark", differenceMark);
				}
			}
			//-------------------------
			//wxwadded  orderType如为空要再次负值
			orderType = orderpay.getOrderType()+"";
			payVoucher = orderpay.getPayVoucher();
			invoiceDate = orderpay.getInvoiceDate();
			
			moneySerialNum = orderpay.getMoneySerialNum();
			if(StringUtils.isNotBlank(orderType) && String.valueOf(Context.ORDER_TYPE_QZ).equals(orderType)){
				orderpay.setMoneyAmount(MoneyAmountUtils.getMoneyAmountBySerialNum(moneySerialNum, MoneyAmountUtils.SHOW_TYPE_MARK, Integer.parseInt(orderType)));
			}
			model.addAttribute("orderpay", orderpay);
			String companyName = UserUtils.getUser().getCompany().getCompanyName();
			if(companyName.contains("环球行")) {
				String priceType = orderpay.getPayType() == null ? "" : orderpay.getPayType().toString();
				if(priceType.equals("2")) {
					Date date = new Date();
				    accountDate =  DateUtils.formatDate(date, "yyyy-MM-dd");
				}else {
					accountDate = orderpay.getCreateDate() == null ? "" : orderpay.getCreateDate() .toString();
					if (StringUtils.isNotBlank(accountDate)) {
						Date groupDate = DateUtils.dateFormat(accountDate);
						accountDate = DateUtils.formatDate(groupDate, "yyyy-MM-dd");
					}
					else {
						Date date = new Date();
					    accountDate =  DateUtils.formatDate(date, "yyyy-MM-dd");
					}
				}
				
			}else {
				Date date = new Date();
			    accountDate =  DateUtils.formatDate(date, "yyyy-MM-dd");
			}
		}
		
		
		//获取批发商信息
		Long supplierId = UserUtils.getUser().getCompany().getId();
		List<DocInfo> list =null ;
		//获取订单的支付凭证信息
	    if(StringUtils.isNotBlank(payVoucher)){
	    	String[] docId = payVoucher.split(",");
	    	List<Long> docIdList = new ArrayList<Long>();
	    	for(int index=0;index<docId.length;index++){
	    		docIdList.add(Long.parseLong(docId[index]));
	    	}
	    	list = docInfoService.getDocInfoByIds(docIdList);
	    }
	    model.addAttribute("accountDate",accountDate);
	    //获取批发商银行账户信息
		List<String> supplierBankNames = officeService
				.getOfficePlatBankInfoById(supplierId);
		if(invoiceDate != null){
		   data.put("invoiceDate",DateUtils.formatDate(invoiceDate, "yyyy-MM-dd"));
		}
		// 获取签约渠道的银行信息，-1表示非签约渠道
		List<PlatBankInfo> agentBanks = new ArrayList<PlatBankInfo>();
		if(StringUtils.isNotBlank(agentId) && !agentId.equals("-1")){
			Agentinfo agentinfo = agentinfoService.findAgentInfoById(Long.valueOf(agentId));
			if(agentinfo != null) {
			    agentBanks = platBankInfoService.findByBeLongPlatIdAndPlatType(agentinfo.getId(), Context.PLAT_TYPE_QD);
				
			}
		}
		model.addAttribute("agentBanks", agentBanks);
		model.addAttribute("supplierId", supplierId);
		model.addAttribute("agentId", agentId);
	    model.addAttribute("supplierBanks", supplierBankNames);
	    model.addAttribute("docInfoList", list);
	    //获取收款金额
		model.addAttribute("payPrice", MoneyAmountUtils.getMoneyAmountBySerialNum(moneySerialNum, MoneyAmountUtils.SHOW_TYPE_MARK, Integer.parseInt(orderType)));
	    //订单类型
		model.addAttribute("orderType", orderType);
	    return "modules/order/list/receiptsConfirm";
	}
	
	
	@RequestMapping(value = "getOrderPayInfoForVisaOrder", method = RequestMethod.GET)
	public String getOrderPayInfoForVisaOrder(HttpServletRequest request,HttpServletResponse response,Model model){
		String orderNum = request.getParameter("orderNum");
		String agentId = request.getParameter("agentid");
		String orderPaySerialNum = request.getParameter("orderPaySerialNum");
		String payType = request.getParameter("payType");
		List<Orderpay> orderPayList = orderPayService.findOrderPay(orderNum, orderPaySerialNum);
		Orderpay op = new Orderpay();
		int num=0;
		//签证订单收款最早的版本是按游客支付，按游客收款的，现在按订单收款，为了兼容历史数据，签证组对历史数据做了处理，添加了批次号，
		//签证订单历史数据收款确认的规则是同一批次（批次号相同的）的数据只有所有的收款信息全部一致时才能够进行收款确认
		//以下代码是判断各种收款方式存的信息是否一致
		if("3".equals(payType) || "5".equals(payType)){ //3 现金支付
			String[] fileds={"payerName","accountDate"};
			num = orderPayService.foreach(orderPayList, fileds);
		    op=orderPayService.copyValue2OrderPay(orderPayList, payType,num);
		}else if("1".equals(payType)){//1 支票
			String[] fileds={"payerName","accountDate","checkNumber","invoiceDate"};
			num = orderPayService.foreach(orderPayList,fileds);
			op=orderPayService.copyValue2OrderPay(orderPayList, payType,num);
		}else if("4".equals(payType)){// 4 汇款
		    String[] fileds={"payerName","accountDate","bankName","bankAccount","toBankNname","toBankAccount"};
		    num = orderPayService.foreach(orderPayList,fileds);
		    op=orderPayService.copyValue2OrderPay(orderPayList, payType,num);
		}else if("6".equals(payType)){//银行转账
			String[] fileds={"payerName","accountDate","bankName","bankAccount","toBankNname","toBankAccount"};
		    num = orderPayService.foreach(orderPayList,fileds);
		    op=orderPayService.copyValue2OrderPay(orderPayList, payType,num);
		}else if("7".equals(payType)){
			String[] fileds={"payerName","draftAccountedDate","bankName","bankAccount","toBankNname","toBankAccount"};
		    num = orderPayService.foreach(orderPayList,fileds);
		    op=orderPayService.copyValue2OrderPay(orderPayList, payType,num);
		}else if("8".equals(payType)){
			String[] fileds={"payerName","bankName","bankAccount"};
			num = orderPayService.foreach(orderPayList,fileds);
			op=orderPayService.copyValue2OrderPay(orderPayList, payType,num);
		}else if(Context.PAYTYPE_ALIPAY.equals(payType)) {//224 因公支付宝
			String[] fileds = {"payerName", "accountDate", "fromAlipayName", "fromAlipayAccount", 
					"toAlipayName", "toAlipayAccount", "comeOfficeName"};
			num = orderPayService.foreach(orderPayList, fileds);
			op = orderPayService.copyValue2OrderPay(orderPayList, payType, num);
		}
		

		List<DocInfo> list = null;
		List<Long> docIdList = new ArrayList<Long>();
		int size = orderPayList.size();
		for(int i=0;i<size;i++){
			Orderpay o = orderPayList.get(i);
			if(StringUtils.isNotBlank(o.getPayVoucher())){
				String[] docId=o.getPayVoucher().split(",");
				for(int a=0;a<docId.length;a++){
					docIdList.add(Long.parseLong(docId[a]));
				}
				
			}
		}
		if(CollectionUtils.isNotEmpty(docIdList)){
			list = docInfoService.getDocInfoByIds(docIdList);
		}
		//获取批发商信息
		Long supplierId = UserUtils.getUser().getCompany().getId();
		//获取批发商银行账户信息
		List<String> supplierBankNames = officeService
				.getOfficePlatBankInfoById(supplierId);
		model.addAttribute("orderType", op.getOrderType());
		model.addAttribute("orderpay", op);
		// 获取签约渠道的银行信息，-1表示非签约渠道
		if(StringUtils.isNotBlank(agentId) && !agentId.equals("-1")){
		List<PlatBankInfo> agentBanks = platBankInfoService.findByBeLongPlatIdAndPlatType(Long.valueOf(agentId), Context.PLAT_TYPE_QD);
		model.addAttribute("agentBanks", agentBanks);
		}
		model.addAttribute("supplierId", supplierId);
		model.addAttribute("agentId", agentId);
	    model.addAttribute("supplierBanks", supplierBankNames);
	    model.addAttribute("docInfoList", list);
	    model.addAttribute("num", num);
	    String url="";
	    if(num==1){
	    	url="modules/order/list/receiptsConfirm";
	    }else{
	    	url="";
	    }
	    String accountDate = "";
	    String companyName = UserUtils.getUser().getCompany().getCompanyName();
		if(companyName.contains("环球行")) {
			accountDate = op.getCreateDate() == null ? "" : op.getCreateDate().toString();
			Date groupDate = DateUtils.dateFormat(accountDate);
			accountDate = DateUtils
					.formatCustomDate(groupDate, "yyyy-MM-dd");
		}
	    model.addAttribute("accountDate", accountDate);
		return url;
	}
	
	
	
	
	//returnMoneyConfirm.jsp
	
	@RequestMapping(value = "returnMoneyConfirm", method = RequestMethod.GET)
	public Object returnMoneyConfirm(HttpServletRequest request,HttpServletResponse response,Model model){
		
		 String chosenNum = request.getParameter("chosenNum");
		 model.addAttribute("chosenNum", chosenNum);
		 
		 // 548 549需求 type=1 用于判断是否为548 549涉及页面 添加审批备注30字符限制
		 // modify by 王洋 2016.12.13
		 String type = request.getParameter("type");
		 model.addAttribute("type", type);
		 return "modules/order/list/returnMoneyConfirm";
		
	}
	/**
	 * 根据流水号多币种信息
	 * @param orderPayId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getMoneyAmountBySerialNum", method = RequestMethod.GET)
	public Object getMoneyAmountBySerialNum(@RequestParam String serialNum){
		Map<String, Object> data = new HashMap<String, Object>();
		
		//List<Object[]> moneyAmountList = moneyAmountService.getMoneyAmonut(serialNum);
		return data;
	}
	
	/**
	 * 确认支付信息
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "confirmPayInfo", method = RequestMethod.POST)
	@ResponseBody
	public Object confirmPayInfo(
			@RequestParam Long orderPayId,/* 支付ID */
			@RequestParam Integer payType,/* 支付类型，快速支付，支票，现金支付，汇款，因公支付宝 */
			@RequestParam(required = false) String remarks,
			@RequestParam(required = false) Long[] DocInfoIds,
			/* 快速支付时需要填写的支付方式 */
			@RequestParam(required = false) String fastPayType,
			// 汇款时需要的信息
			@RequestParam(required = false) String bankName,/* 开户行bankId */
			@RequestParam(required = false) String bankAccount,/* 开户行账户 */
			@RequestParam(required = false) String toBankNname,/* 转入行bankId */
			@RequestParam(required = false) String toBankAccount,/* 转入行bankId */
			// 支票支付需要的信息
			@RequestParam(required = false) String payerName, /* 付款单位 */
			@RequestParam(required = false) String checkNumber,/* 支票号 */
			// 支付宝信息
			@RequestParam(required = false) String fromAlipayName,/* 支付宝名称（来款）*/
			@RequestParam(required = false) String fromAlipayAccount,/* 支付宝账号（来款）*/
			@RequestParam(required = false) String toAlipayName,/* 支付宝名称（收款）*/
			@RequestParam(required = false) String toAlipayAccount,/* 支付宝账号（收款）*/
			@RequestParam(required = false) String comeOfficeName,/* 收款单位 */
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date accountDate,//银行到账日期
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date invoiceDate/* 开票日期 */){
		
		Map<String, Object> data = new HashMap<String, Object>();
		try{
		if(accountDate == null){
			 data.put("flag", "false");
		}else{
		Orderpay orderPay = orderPayService.findOrderpayById(orderPayId);
		ReturnDifference difference = null;
		if(orderPay.getOrderType() == 2 && StringUtils.isNotBlank(orderPay.getDifferenceUuid())){
			difference = returnDifferenceService.getReturnDifferenceByUuid(orderPay.getDifferenceUuid());
			difference.setToAccountType(1);
			difference.setToAccountDate(accountDate);
			difference.setUpdateBy(UserUtils.getUser().getId().intValue());
			difference.setUpdateDate(new Date());
			returnDifferenceService.updateToAccountType(difference);
			ProductOrderCommon orderCommon = orderCommonService.getProductorderById(difference.getOrderId().longValue());
			orderPayService.differenceMoneyAddToAccountedMoney(orderCommon.getAccountedMoney(), difference);
		}
		if(orderPay.getIsAsAccount() != null && orderPay.getIsAsAccount() == Context.ORDERPAY_ACCOUNT_STATUS_YDZ) {
			data.put("flag", "false");
			data.put("msg", "该款项已确认收款，请刷新页面重新操作！");
			return data;
		}
		switch (payType) {
		case 1:// 支票支付
			orderPay.setPayTypeName("支票支付");
			
			// 支票号
			if (StringUtils.isNotEmpty(checkNumber)) {
				orderPay.setCheckNumber(checkNumber);
			}
			// 开票日期
			if (invoiceDate != null && StringUtils.isNotEmpty(invoiceDate.toString())) {
				orderPay.setInvoiceDate(invoiceDate);
			}
			break;
		case 3:// 现金支付
			orderPay.setPayTypeName("现金支付");
			
			break;
		case 4:// 汇款
			orderPay.setPayTypeName("汇款");
			// 开户行名称
			if (StringUtils.isNotEmpty(bankName)) {
				orderPay.setBankName(bankName);
			}
			// 开户行账户
			if (StringUtils.isNotEmpty(bankAccount)) {
				orderPay.setBankAccount(bankAccount);
			}
			// 转入行名称
			if (StringUtils.isNotEmpty(toBankNname)) {
				if ("-1".equals(toBankNname)) {
					orderPay.setToBankNname(null);
				} else {
					orderPay.setToBankNname(toBankNname);
				}
			}
			// 转入行账号
			if (StringUtils.isNotEmpty(toBankAccount)) {
				orderPay.setToBankAccount(toBankAccount);
			} else {
				orderPay.setToBankAccount(null);
			}
			
			break;
		case 5:// 快速支付
			orderPay.setPayTypeName("快速支付");
			
			// 支付方式
			if (StringUtils.isNotEmpty(fastPayType)) {
				orderPay.setFastPayType(fastPayType);
			}
			break;
		case 9:// 因公支付宝
			orderPay.setPayTypeName("因公支付宝");
			// 支付宝账号（来款）
			if(StringUtils.isNotBlank(fromAlipayAccount)) {
				orderPay.setFromAlipayAccount(fromAlipayAccount);
			}
			// 支付宝名称（来款）
			if(StringUtils.isNotBlank(fromAlipayName)) {
				orderPay.setFromAlipayName(fromAlipayName);
			}
			// 支付宝账号（收款）
			if(StringUtils.isNotBlank(toAlipayAccount)) {
				orderPay.setToAlipayAccount(toAlipayAccount);
			}
			// 支付宝名称（来款）
			if(StringUtils.isNotBlank(toAlipayName)) {
				orderPay.setToAlipayName(toAlipayName);
			}
			// 收款单位
			if(StringUtils.isNotBlank(comeOfficeName)) {
				orderPay.setComeOfficeName(comeOfficeName);
			}
			
			break;
		}

		// 备注信息
		if (StringUtils.isNotEmpty(remarks)) {
			orderPay.setRemarks(remarks);
		}
		if(DocInfoIds != null && DocInfoIds.length>0){
			String tmp="";
			for(int i=0;i<DocInfoIds.length;i++){
				String docInfoId = DocInfoIds[i].toString();
				tmp+=docInfoId+",";
			}
			orderPay.setPayVoucher(orderPay.getPayVoucher()+tmp);
		}
		orderPay.setUpdateBy(UserUtils.getUser());
		orderPay.setUpdateDate(new Date());
		//达帐是1 未达帐为0
		orderPay.setIsAsAccount(Context.ORDERPAY_ACCOUNT_STATUS_YDZ);
		orderPay.setAccountDate(accountDate);
	//	orderPayService.save(orderPay);
		//付款单位
		if(StringUtils.isNotBlank(payerName)){
			orderPay.setPayerName(payerName);
		}
		List<MoneyAmount> ma = moneyAmountService.findAmountBySerialNum(orderPay.getMoneySerialNum());
		//确认收款时间
		orderPay.setReceiptConfirmationDate(new Date());
        orderPayService.payedConfirm(orderPay, DocInfoIds,ma);
		// 为上传资料设置payorderid属性
		//orderPayService.(orderPay.getMoneySerialNum(), DocInfoIds);
       
       data.put("flag", "ok");
       //拼装日志信息
		StringBuffer sb = new StringBuffer("订单号:");
		sb.append(orderPay.getOrderNum()).append(" 操作时间：").append(DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd HH:mm:ss"))
		.append(" 操作人员：").append(UserUtils.getUser().getName()).append(" 确认到账").append(moneyAmountService.getMoney(orderPay.getMoneySerialNum()));
		saveUserOpeLog("5", "财务", sb.toString(), "3", orderPay.getOrderType(), orderPay.getOrderId());//保存操作日志
		}
		}catch(Exception e){
			e.printStackTrace();
			data.put("flag", "false");
			data.put("msg", e.getMessage());
		}
		return data;
	}
	

/**
	 * 确认支付信息
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "confirmPayInfoForProduct", method = RequestMethod.POST)
	@ResponseBody
	public Object confirmPayInfoForProduct(
			@RequestParam String payProductOrderUuid,/* 支付uuid */
			@RequestParam Integer payType,/* 支付类型，快速支付，支票，现金支付，汇款，因公支付宝 */
			@RequestParam Integer orderType,/* 订单类型 */
			@RequestParam(required = false) String remarks,
			@RequestParam(required = false) Long[] DocInfoIds,
			/* 快速支付时需要填写的支付方式 */
			@RequestParam(required = false) String fastPayType,
			// 汇款时需要的信息
			@RequestParam(required = false) String bankName,/* 开户行bankId */
			@RequestParam(required = false) String bankAccount,/* 开户行账户 */
			@RequestParam(required = false) String toBankNname,/* 转入行bankId */
			@RequestParam(required = false) String toBankAccount,/* 转入行bankId */
			// 支票支付需要的信息
			@RequestParam(required = false) String payerName, /* 付款单位 */
			@RequestParam(required = false) String checkNumber,/* 支票号 */
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date accountDate,//银行到账日期
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date invoiceDate/* 开票日期 */,
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date draftAccountedDate,
			HttpServletRequest request){
		
	
		
		Map<String, Object> data = new HashMap<String, Object>();
		try{
			PayIslandOrder payIslandOrder = null;
			PayHotelOrder payHotelOrder = null;
			
			PayProductOrder payProductOrder = null;
			if(Context.ORDER_TYPE_ISLAND.intValue() == orderType) {
				payIslandOrder = payIslandOrderService.getByUuid(payProductOrderUuid);
				
				payProductOrder = PayProductOrder.convert2PayProductOrder(payIslandOrder);
			} else if(Context.ORDER_TYPE_HOTEL.intValue() == orderType) {
				payHotelOrder = payHotelOrderService.getByUuid(payProductOrderUuid);
				
				payProductOrder = PayProductOrder.convert2PayProductOrder(payHotelOrder);
			}
			
			switch (payType) {
			case 1:// 支票支付
				payProductOrder.setPayTypeName("支票支付");
				
				// 支票号
				if (StringUtils.isNotEmpty(checkNumber)) {
					payProductOrder.setCheckNumber(checkNumber);
				}
				// 开票日期
				if (invoiceDate != null && StringUtils.isNotEmpty(invoiceDate.toString())) {
					payProductOrder.setInvoiceDate(invoiceDate);
				}
				break;
			case 3:// 现金支付
				payProductOrder.setPayTypeName("现金支付");
				
				break;
			case 4:// 汇款
				payProductOrder.setPayTypeName("汇款");
				// 开户行名称
				if (StringUtils.isNotEmpty(bankName)) {
					payProductOrder.setBankName(bankName);
				}
				// 开户行账户
				if (StringUtils.isNotEmpty(bankAccount)) {
					payProductOrder.setBankAccount(bankAccount);
				}
				// 转入行名称
				if (StringUtils.isNotEmpty(toBankNname)) {
					payProductOrder.setToBankNname(toBankNname);
				}
				// 转入行账号
				if (StringUtils.isNotEmpty(toBankAccount)) {
					payProductOrder.setToBankAccount(toBankAccount);
				}
				
				break;
			case 5:// 快速支付
				payProductOrder.setPayTypeName("快速支付");
				
				// 支付方式
				if (StringUtils.isNotEmpty(fastPayType)) {
					payProductOrder.setFastPayType(fastPayType);
				}
				break;
			case 6://银行转账
				payProductOrder.setPayTypeName("银行转账");
				// 开户行名称
				if (StringUtils.isNotEmpty(bankName)) {
					payProductOrder.setBankName(bankName);
				}
				// 开户行账户
				if (StringUtils.isNotEmpty(bankAccount)) {
					payProductOrder.setBankAccount(bankAccount);
				}
				// 转入行名称
				if (StringUtils.isNotEmpty(toBankNname)) {
					payProductOrder.setToBankNname(toBankNname);
				}
				// 转入行账号
				if (StringUtils.isNotEmpty(toBankAccount)) {
					payProductOrder.setToBankAccount(toBankAccount);
				}
				
				break;
				
			case 7://汇票
				payProductOrder.setPayTypeName("汇票");
				// 开户行名称
				if (StringUtils.isNotEmpty(bankName)) {
					payProductOrder.setBankName(bankName);
				}
				// 开户行账户
				if (StringUtils.isNotEmpty(bankAccount)) {
					payProductOrder.setBankAccount(bankAccount);
				}
				// 转入行名称
				if (StringUtils.isNotEmpty(toBankNname)) {
					payProductOrder.setToBankNname(toBankNname);
				}
				// 转入行账号
				if (StringUtils.isNotEmpty(toBankAccount)) {
					payProductOrder.setToBankAccount(toBankAccount);
				}
				if( draftAccountedDate != null && StringUtils.isNotEmpty(draftAccountedDate.toString())){
					payProductOrder.setDraftAccountedDate(draftAccountedDate);
				}
				break;
			case 8:
				payProductOrder.setPayTypeName("POS机刷卡");
				// 开户行名称
				if (StringUtils.isNotEmpty(bankName)) {
					payProductOrder.setBankName(bankName);
				}
				// 开户行账户
				if (StringUtils.isNotEmpty(bankAccount)) {
					payProductOrder.setBankAccount(bankAccount);
				}
				break;
			}
			
			
			// 备注信息
			if (StringUtils.isNotEmpty(remarks)) {
				payProductOrder.setRemarks(remarks);
			}
			if(DocInfoIds != null && DocInfoIds.length>0){
				String tmp="";
				for(int i=0;i<DocInfoIds.length;i++){
					String docInfoId = DocInfoIds[i].toString();
					tmp+=docInfoId+",";
				}
				payProductOrder.setPayVoucher(payProductOrder.getPayVoucher()+tmp);
			}
			//达帐是1 未达帐为0
			payProductOrder.setIsAsAccount(Context.ORDERPAY_ACCOUNT_STATUS_YDZ);
			payProductOrder.setAccountDate(accountDate);
			
			//付款单位
			if(StringUtils.isNotBlank(payerName)){
				payProductOrder.setPayerName(payerName);
			}

			if(Context.ORDER_TYPE_ISLAND.intValue() == orderType) {
				//将修改后的支付信息复制信息到海岛游支付记录
				BeanUtil.copySimpleProperties(payIslandOrder, payProductOrder, true);
				
				List<IslandMoneyAmount> amounts = islandMoneyAmountService.getMoneyAmonutBySerialNum(payIslandOrder.getMoneySerialNum());
				//保存海岛游支付表信息
				payIslandOrderService.update(payIslandOrder);
		        orderPayService.payedConfirmForIsland(payProductOrder, DocInfoIds, orderType, amounts);
			} else if(Context.ORDER_TYPE_HOTEL.intValue() == orderType) {
				//将修改后的支付信息复制信息到酒店支付记录
				BeanUtil.copySimpleProperties(payHotelOrder, payProductOrder, true);

				List<HotelMoneyAmount> amounts = hotelMoneyAmountService.getMoneyAmonutBySerialNum(payHotelOrder.getMoneySerialNum());
				//保存酒店支付表信息
				payHotelOrderService.update(payHotelOrder);
		        orderPayService.payedConfirmForHotel(payProductOrder, DocInfoIds, orderType, amounts);
			}
	       
	        data.put("flag", "ok");
	       //拼装日志信息
			StringBuffer sb = new StringBuffer("订单号:");
			sb.append(payProductOrder.getOrderNum()).append(" 操作时间：").append(DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd HH:mm:ss"))
			.append(" 操作人员：").append(UserUtils.getUser().getName()).append(" 确认到账").append(moneyAmountService.getMoney(payProductOrder.getMoneySerialNum()));
			saveUserOpeLog("5", "财务", sb.toString(), "3", null, null);//保存操作日志
		}catch(Exception e){
			e.printStackTrace();
			data.put("flag", "false");
		}
		return data;
	}
	
	
	/**
	 * 签证押金收款确认
	 * 
	 */
	@RequestMapping(value = "confirmPayInfoForVisa", method = RequestMethod.POST)
	@ResponseBody
	public Object confirmPayInfoForVisa(
			@RequestParam Long orderPayId,/* 支付ID */
			@RequestParam Integer payType,/* 支付类型，快速支付，支票，现金支付，汇款 */
			@RequestParam(required = false) String remarks,
			@RequestParam(required = false) Long[] DocInfoIds,
			/* 快速支付时需要填写的支付方式 */
			@RequestParam(required = false) String fastPayType,
			// 汇款时需要的信息
			@RequestParam(required = false) String bankName,/* 开户行bankId */
			@RequestParam(required = false) String bankAccount,/* 开户行账户 */
			@RequestParam(required = false) String toBankNname,/* 转入行bankId */
			@RequestParam(required = false) String toBankAccount,/* 转入行bankId */
			// 支票支付需要的信息
			@RequestParam(required = false) String payerName, /* 付款单位 */
			@RequestParam(required = false) String checkNumber,/* 支票号 */
			// 支付宝信息
			@RequestParam(required = false) String fromAlipayName,/* 支付宝名称（来款）*/
			@RequestParam(required = false) String fromAlipayAccount,/* 支付宝账号（来款）*/
			@RequestParam(required = false) String toAlipayName,/* 支付宝名称（收款）*/
			@RequestParam(required = false) String toAlipayAccount,/* 支付宝账号（收款）*/
			@RequestParam(required = false) String comeOfficeName,/* 收款单位 */
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date accountDate,//银行到账日期
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date invoiceDate/* 开票日期 */){
		
		Map<String, Object> data = new HashMap<String, Object>();
		try{
		Orderpay orderPay = orderPayService.findOrderpayById(orderPayId);
		
		switch (payType) {
		case 1:// 支票支付
			orderPay.setPayTypeName("支票支付");
			// 付款单位
			if (StringUtils.isNotEmpty(payerName)) {
				orderPay.setPayerName(payerName);
			}
			// 支票号
			if (StringUtils.isNotEmpty(checkNumber)) {
				orderPay.setCheckNumber(checkNumber);
			}
			// 开票日期
			if (invoiceDate != null && StringUtils.isNotEmpty(invoiceDate.toString())) {
				orderPay.setInvoiceDate(invoiceDate);
			}
			break;
		case 3:// 现金支付
			orderPay.setPayTypeName("现金支付");
			break;
		case 4:// 汇款
			orderPay.setPayTypeName("汇款");
			// 开户行名称
			if (StringUtils.isNotEmpty(bankName)) {
				orderPay.setBankName(bankName);
			}
			// 开户行账户
			if (StringUtils.isNotEmpty(bankAccount)) {
				orderPay.setBankAccount(bankAccount);
			}
			// 转入行名称
			if (StringUtils.isNotEmpty(toBankNname)) {
				if ("-1".equals(toBankNname)) {
					orderPay.setToBankNname(null);
				} else {
					orderPay.setToBankNname(toBankNname);
				}
			}
			// 转入行账号
			if (StringUtils.isNotEmpty(toBankAccount)) {
				orderPay.setToBankAccount(toBankAccount);
			} else {
				orderPay.setToBankAccount(null);
			}
			
			if(StringUtils.isNotBlank(payerName)){
				orderPay.setPayerName(payerName);
			}
			break;
		case 5:// 快速支付
			orderPay.setPayTypeName("快速支付");
			// 支付方式
			if (StringUtils.isNotEmpty(fastPayType)) {
				orderPay.setFastPayType(fastPayType);
			}
			break;
		case 9:// 因公支付宝
			orderPay.setPayTypeName("因公支付宝");
			// 支付宝账号（来款）
			if(StringUtils.isNotBlank(fromAlipayAccount)) {
				orderPay.setFromAlipayAccount(fromAlipayAccount);
			}
			// 支付宝名称（来款）
			if(StringUtils.isNotBlank(fromAlipayName)) {
				orderPay.setFromAlipayName(fromAlipayName);
			}
			// 支付宝账号（收款）
			if(StringUtils.isNotBlank(toAlipayAccount)) {
				orderPay.setToAlipayAccount(toAlipayAccount);
			}
			// 支付宝名称（来款）
			if(StringUtils.isNotBlank(toAlipayName)) {
				orderPay.setToAlipayName(toAlipayName);
			}
			// 收款单位
			if(StringUtils.isNotBlank(comeOfficeName)) {
				orderPay.setComeOfficeName(comeOfficeName);
			}
			
			break;
		}

		// 备注信息
		if (StringUtils.isNotEmpty(remarks)) {
			orderPay.setRemarks(remarks);
		}
		if(DocInfoIds != null && DocInfoIds.length>0){
			String tmp="";
			for(int i=0;i<DocInfoIds.length;i++){
				String docInfoId = DocInfoIds[i].toString();
				tmp+=docInfoId+",";
			}
			orderPay.setPayVoucher(orderPay.getPayVoucher()+tmp);
		}
		//达帐是1 未达帐为0
		orderPay.setIsAsAccount(Context.ORDERPAY_ACCOUNT_STATUS_YDZ);
		orderPay.setAccountDate(accountDate);		
		List <MoneyAmount> ma = moneyAmountService.findAmountBySerialNum(orderPay.getMoneySerialNum());
		//收款确认日期
		orderPay.setReceiptConfirmationDate(new Date());
		orderPayService.payedConfirmForVisa(orderPay, DocInfoIds,ma);
	    data.put("flag", "ok");
	    //拼装日志信息
		StringBuffer sb = new StringBuffer("签证押金订单:");
		sb.append(orderPay.getOrderNum()).append("  ").append(DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd HH:mm:ss"))
		.append(" ").append(UserUtils.getUser().getName()).append("确认到账").append(moneyAmountService.getMoney(orderPay.getMoneySerialNum()));
		saveUserOpeLog("5", "财务", sb.toString(), "3", orderPay.getOrderType(), orderPay.getOrderId());//保存操作日志
		}catch(Exception e){
			 data.put("flag", "false");
		}
		return data;
	}
	

	/**
	 * 签证订单收款确认
	 * @param 
	 * @return
	 */
	@RequestMapping(value = "confirmPayInfoForVisaOrder", method = RequestMethod.POST)
	@ResponseBody
	public Object confirmPayInfoForVisaOrder(
			@RequestParam Long orderPayId,/* 支付ID */
			@RequestParam Integer payType,/* 支付类型，快速支付，支票，现金支付，汇款 */
			@RequestParam(required = false) String remarks,
			@RequestParam(required = false) Long[] DocInfoIds,
			/* 快速支付时需要填写的支付方式 */
			@RequestParam(required = false) String fastPayType,
			// 汇款时需要的信息
			@RequestParam(required = false) String bankName,/* 开户行bankId */
			@RequestParam(required = false) String bankAccount,/* 开户行账户 */
			@RequestParam(required = false) String toBankNname,/* 转入行bankId */
			@RequestParam(required = false) String toBankAccount,/* 转入行bankId */
			// 支票支付需要的信息
			@RequestParam(required = false) String payerName, /* 付款单位 */
			@RequestParam(required = false) String checkNumber,/* 支票号 */
			// 支付宝信息
			@RequestParam(required = false) String fromAlipayName,/* 支付宝名称（来款）*/
			@RequestParam(required = false) String fromAlipayAccount,/* 支付宝账号（来款）*/
			@RequestParam(required = false) String toAlipayName,/* 支付宝名称（收款）*/
			@RequestParam(required = false) String toAlipayAccount,/* 支付宝账号（收款）*/
			@RequestParam(required = false) String comeOfficeName,/* 收款单位 */
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date accountDate,//银行到账日期
			@DateTimeFormat(pattern = "yyyy-MM-dd") Date invoiceDate,/* 开票日期 */
			@RequestParam(required = false) String orderNum,
			@RequestParam(required = false) String orderPaySerialNum){
		
		Map<String, Object> data = new HashMap<String, Object>();
		try{
			if(accountDate == null){
				data.put("flag", "false");
			}else{
				List<Orderpay> orderPayList = orderPayService.findOrderPay(orderNum, orderPaySerialNum);
				if(CollectionUtils.isNotEmpty(orderPayList)){
					int size = orderPayList.size();
					for(int a=0;a<size;a++){
						Orderpay existOrderPay = orderPayList.get(a);
						//解决数据重复提交问题，如果已达帐，则无需再次达帐以防数据翻倍  update by shijun.liu 2016.05.19
						if(null != existOrderPay && Context.ORDERPAY_ACCOUNT_STATUS_YDZ == existOrderPay.getIsAsAccount()) {
							continue;
						}
						switch (payType) {
						case 1:// 支票支付
							orderPayList.get(a).setPayTypeName("支票支付");
							// 支票号
							if (StringUtils.isNotEmpty(checkNumber)) {
								orderPayList.get(a).setCheckNumber(checkNumber);
							}
							// 开票日期
							if (invoiceDate != null && StringUtils.isNotEmpty(invoiceDate.toString())) {
								orderPayList.get(a).setInvoiceDate(invoiceDate);
							}
							break;
						case 3:// 现金支付
							orderPayList.get(a).setPayTypeName("现金支付");
							break;
						case 4:// 汇款
							orderPayList.get(a).setPayTypeName("汇款");
							// 开户行名称
							if (StringUtils.isNotEmpty(bankName)) {
								orderPayList.get(a).setBankName(bankName);
							}
							// 开户行账户
							if (StringUtils.isNotEmpty(bankAccount)) {
								orderPayList.get(a).setBankAccount(bankAccount);
							}
							// 转入行名称
							if (StringUtils.isNotEmpty(toBankNname)) {
								if ("-1".equals(toBankNname)) {
									orderPayList.get(0).setToBankNname(null);
								} else {
									orderPayList.get(a).setToBankNname(toBankNname);
								}
							}
							// 转入行账号
							if (StringUtils.isNotEmpty(toBankAccount)) {
								orderPayList.get(a).setToBankAccount(toBankAccount);
							} else {
								orderPayList.get(a).setToBankAccount(null);
							}
							break;
						case 5:// 快速支付
							orderPayList.get(a).setPayTypeName("快速支付");
							// 支付方式
							if (StringUtils.isNotEmpty(fastPayType)) {
								orderPayList.get(a).setFastPayType(fastPayType);
							}
							break;
						case 9:
							orderPayList.get(a).setPayTypeName("因公支付宝");
							// 支付宝账号（来款）
							if(StringUtils.isNotBlank(fromAlipayAccount)) {
								orderPayList.get(a).setFromAlipayAccount(fromAlipayAccount);
							}
							// 支付宝名称（来款）
							if(StringUtils.isNotBlank(fromAlipayName)) {
								orderPayList.get(a).setFromAlipayName(fromAlipayName);
							}
							// 支付宝账号（收款）
							if(StringUtils.isNotBlank(toAlipayAccount)) {
								orderPayList.get(a).setToAlipayAccount(toAlipayAccount);
							}
							// 支付宝名称（来款）
							if(StringUtils.isNotBlank(toAlipayName)) {
								orderPayList.get(a).setToAlipayName(toAlipayName);
							}
							// 收款单位
							if(StringUtils.isNotBlank(comeOfficeName)) {
								orderPayList.get(a).setComeOfficeName(comeOfficeName);
							}
							
							break;
						}
						// 备注信息
						if (StringUtils.isNotEmpty(remarks)) {
							orderPayList.get(a).setRemarks(remarks);
						}
						//0405 收款确认日期
						orderPayList.get(a).setReceiptConfirmationDate(new Date());
						if(DocInfoIds != null && DocInfoIds.length>0){
							String tmp="";
							for(int i=0;i<DocInfoIds.length;i++){
								String docInfoId = DocInfoIds[i].toString();
								tmp+=docInfoId+",";
							}
							orderPayList.get(a).setPayVoucher(orderPayList.get(a).getPayVoucher()+tmp);
						}
						//达帐是1 未达帐为0
						orderPayList.get(a).setIsAsAccount(Context.ORDERPAY_ACCOUNT_STATUS_YDZ);
						orderPayList.get(a).setAccountDate(accountDate);
						//付款单位
						if(StringUtils.isNotBlank(payerName)){
							orderPayList.get(a).setPayerName(payerName);
						}
					}
				}
			   orderPayService.payedConfirmForVisaOrder(orderPayList,DocInfoIds);
			   data.put("flag", "ok");
			
//       //拼装日志信息
//		StringBuffer sb = new StringBuffer("订单号:");
//		sb.append(orderPay.getOrderNum()).append(" 操作时间：").append(DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd HH:mm:ss"))
//		.append(" 操作人员：").append(UserUtils.getUser().getName()).append(" 确认到账").append(moneyAmountService.getMoney(orderPay.getMoneySerialNum()));
//		saveUserOpeLog("5","财务",sb.toString(),"3");//保存操作日志
			}
			}catch(Exception e){
			data.put("flag", "false");
			e.printStackTrace();
		}
		return data;
	}

	
	/**
	 * @author wuqiang
	 * date:2015-03-06
	 * 驳回操作第一步：跳转到驳回操作页面rejectConfirm.jsp
	 * 由ID取得相应的支付记录
	 * @param orderPayId
	 * @param agentId   渠道id
	 * @return
	 */
	@RequestMapping(value = "getOrderPayForRejectOne", method = RequestMethod.GET)
	public Object getOrderPayForRejectOne(HttpServletRequest request,HttpServletResponse response,Model model){

		String orderPayId = request.getParameter("payId");
		
		String sign = request.getParameter("sign");//驳回类型标识，0-订单收款驳回操作，1-签证押金收款驳回操作
		String orderId = request.getParameter("orderId");
		String orderType = request.getParameter("orderType");
		if(StringUtils.isNotBlank(orderType) && Context.ORDER_TYPE_HOTEL==Integer.valueOf(orderType)){
			HotelOrder hotelOrder = hotelOrderService.getById(Integer.valueOf(orderId));
			PayHotelOrder orderpay = payHotelOrderService.getById(Integer.valueOf(orderPayId));
			model.addAttribute("orderpay", orderpay);
//			if(hotelOrder != null && hotelOrder.getOrderStatus()==1){
				sign="1";
//			}else{
//				sign="0";
//			}
			
		}else if(StringUtils.isNotBlank(orderType) && Context.ORDER_TYPE_ISLAND==Integer.valueOf(orderType)){
			
			IslandOrder islandOrder = islandOrderService.getById(Integer.valueOf(orderId));
			PayIslandOrder orderpay = payIslandOrderService.getById(Integer.valueOf(orderPayId));
			model.addAttribute("orderpay", orderpay);
//			(islandOrder != null && islandOrder.getOrderStatus()==1){
				sign="1";
//			}else{
//				sign="0";
//			}
		}else{
			//根据订单支付ID查找订单支付信息
			Orderpay orderpay = orderPayService.findOrderpayById(Long.valueOf(orderPayId));
			model.addAttribute("orderpay", orderpay);
		}
	    
	    model.addAttribute("sign", sign);
	    
	    return "modules/order/list/rejectConfirm";
	}
	
	/**
	 * @author wuqiang
	 * date:2015-03-06
	 * 驳回操作第二步：对驳回操作页面rejectConfirm.jsp提交的数据进行处理
	 * 由ID取得相应的支付记录
	 * @param orderPayId
	 * @param agentId   渠道id
	 * @return
	 */
	@RequestMapping(value = "getOrderPayForRejectTwo")
	@ResponseBody
	public Object getOrderPayForRejectTwo(HttpServletRequest request,HttpServletResponse response,Model model){
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		String payId = request.getParameter("payId");//订单支付ID
		
		String productOrderId = request.getParameter("productOrderId");//订单ID
		
		String rejectRadio = request.getParameter("rejectRadio");//0-保持占位，1-退回占位
		
		String ordertype = request.getParameter("ordertype");//订单类型，7-机票，机票类型需要调用徐展的接口去处理驳回
		
		String sign = request.getParameter("sign");//驳回类型标识，0-订单收款驳回操作，1-签证押金收款驳回操作

		String reason = request.getParameter("reason");//驳回备注
		if(reason != null) {
			reason = reason.replaceAll(" ", "");
		}
		
		if("1".equals(sign)){//sign==1 
			if(StringUtils.isNotBlank(ordertype) && Context.ORDER_TYPE_HOTEL==Integer.valueOf(ordertype) && StringUtils.isNotBlank(payId)){
				boolean result = payHotelOrderService.rejectOrder(productOrderId, payId, Context.REJECT_NO_PLACEHOLDER);//updateAccountStatus(Integer.valueOf(payId), Context.ORDERPAY_ACCOUNT_STATUS_YBH);
				if(result){
					data.put("rejectMark", "收款确认驳回成功");
				}else{
					data.put("rejectMark", "收款确认驳回失败");
				}
			}else if(StringUtils.isNotBlank(ordertype) && Context.ORDER_TYPE_ISLAND==Integer.valueOf(ordertype) && StringUtils.isNotBlank(payId)){
				boolean result = payIslandOrderService.rejectOrder(productOrderId, payId, Context.REJECT_NO_PLACEHOLDER);
				if(result){
					data.put("rejectMark", "收款确认驳回成功");
				}else{
					data.put("rejectMark", "收款确认驳回失败");
				}
			}else if(!String.valueOf(Context.ORDER_TYPE_HOTEL).equals(ordertype) && !String.valueOf(Context.ORDER_TYPE_ISLAND).equals(ordertype) && StringUtils.isNotBlank(payId)) {
				Orderpay orderpay = orderPayService.findOrderpayById(Long.valueOf(payId));
				if(orderpay.getTravelerId() != null) {
					boolean result = visaOrderTravelerPayLogService.depositReject(orderpay.getTravelerId().toString(), payId, reason);
					if(result){
						data.put("rejectMark", "签证押金收款驳回操作成功！");
					}else{
						data.put("rejectMark", "签证押金收款驳回操作失败！");
					}
				}else {
					data.put("rejectMark", "签证押金收款驳回操作失败！");
				}
			}else {
				data.put("rejectMark", "收款驳回操作失败！");
			}
		}else {
		
			/*如果没有选择是否影响占位，这不执行驳回操作*/
			if(null == rejectRadio){
				return null;
			}
			
		    if(StringUtils.isNotBlank(ordertype) && Context.ORDER_TYPE_ISLAND == Integer.valueOf(ordertype)){
//				PayIslandOrder orderPay = payIslandOrderService.getById(Integer.valueOf(payId));
//				//已达帐-1,达账的订单不能驳回
//				Integer isAsAccount = (null == orderPay.getIsAsAccount())?0:orderPay.getIsAsAccount();
//				if(isAsAccount != 1 && isAsAccount != 2){
//					
//				}
			}else if(StringUtils.isNotBlank(ordertype) && Context.ORDER_TYPE_HOTEL == Integer.valueOf(ordertype)){
//				PayHotelOrder orderPay = payHotelOrderService.getById(Integer.valueOf(payId));
//				//已达帐-1,达账的订单不能驳回
//				Integer isAsAccount = (null == orderPay.getIsAsAccount())?0:orderPay.getIsAsAccount();
//				if(isAsAccount != 1 && isAsAccount != 2){
//					payHotelOrderService.rejectOrder(productOrderId,payId,rejectRadio);
//				}
			}else{
				//根据订单支付ID查找订单支付信息
				Orderpay orderpay = orderPayService.findOrderpayById(Long.valueOf(payId));
				//已达帐-1,达账的订单不能驳回
				Integer isAsAccount = (null == orderpay.getIsAsAccount())?0:orderpay.getIsAsAccount();
				if(isAsAccount != 1 && isAsAccount != 2){
					if(orderpay.getOrderType() == 2 && StringUtils.isNotBlank(orderpay.getDifferenceUuid())){
						ReturnDifference difference = returnDifferenceService.getReturnDifferenceByUuid(orderpay.getDifferenceUuid());
						difference.setToAccountType(2);
						difference.setUpdateBy(UserUtils.getUser().getId().intValue());
						difference.setUpdateDate(new Date());
						returnDifferenceService.updateToAccountType(difference);
					}
                    if(ordertype.equals(Context.ORDER_STATUS_AIR_TICKET)){
						//0-保持占位，1-退回占位
						boolean flag = true;
						if(rejectRadio.equals(Context.REJECT_YES_PLACEHOLDER)){
							flag = false;
						}
						
						//机票驳回，调用徐展的接口
						boolean result = airticketPreOrderService.airticketRejected(Long.parseLong(productOrderId), Long.parseLong(payId), flag, reason);
						
						if(result){
							data.put("rejectMark", "机票驳回操作成功！");
						}else{
							data.put("rejectMark", "机票驳回操作失败！");
						}
					}else{
						//0-保持占位，1-退回占位
						boolean isHoldPosition = false;
						if (rejectRadio.equals(Context.REJECT_NO_PLACEHOLDER)) {
							isHoldPosition = true;
						}
						
						try {
							
							String result = orderCommonService.rejectOrder(Long.parseLong(productOrderId), Long.parseLong(payId), isHoldPosition, request);
							
							if ("ok".equals(result)) {
								data.put("rejectMark", (isHoldPosition ? "保留" : "退回") + "占位驳回成功！");
							} else {
								data.put("rejectMark", (isHoldPosition ? "保留" : "退回") + "占位驳回失败！");
							}
							
						} catch (NumberFormatException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				
				}else{
					if(isAsAccount == 2){
						data.put("rejectMark", "订单已驳回！");
					}else{
						data.put("rejectMark", "订单已达账，不能驳回！");
					}
				}
			}
			
			
		}
		
		return data;
	}

	/**
	 * 撤消签证订单的接口
	 * @param payId orderpay表的主键ID
	 * @param payType 收款/付款标识（1：收款；2：付款）
	 * @return
	 * @throws Exception 
	 * @throws PositionOutOfBoundException 
	 * @throws OptimisticLockHandleException 
	 */
	@RequestMapping(value = "/cancelVisaOrderOper")
	@ResponseBody
	public Object cancelVisaOrderOper(@RequestParam String orderPaySerialNum,
			@RequestParam String payType) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {

		Map<String, String> data = new HashMap<String, String>();
		if (StringUtils.isBlank(orderPaySerialNum)) {
			data.put("flag", "error");
			data.put("msg", "支付ID不能为空");
			return data;
		}

		if (StringUtils.isBlank(payType)) {
			data.put("flag", "error");
			data.put("msg", "支付类型不能为空（1：收款；2：付款）");
			return data;
		}

		if (!"1".equals(payType) && !"2".equals(payType)) {
			data.put("flag", "error");
			data.put("msg", "不存在该支付类型（1：收款；2：付款）");
			return data;
		}
		
		// 收款
		/*data = orderPayService.cancelVisaOrderOper(payId);
		if ("error".equals(data.get("flag"))) {
			return data;
		}
		orderPayMoreService.cancelConfirmOper(payId, payType);

		data.put("flag", "ok");*/
		
		
		//-----2015-06-25 批量撤销----
		List<Orderpay> orderpayList = orderpayDao.findOrderpaByOrderPaySerialNum(orderPaySerialNum);
		
		for (Orderpay orderpay : orderpayList) {
			// 收款
			data = orderPayService.cancelVisaOrderOper(orderpay.getId()+"");
			if ("error".equals(data.get("flag"))) {
				return data;
			}
			orderPayMoreService.cancelConfirmOper(orderpay.getId()+"", payType);
		}
		data.put("flag", "ok");

	
		return data;
	}

	/**
	 * 撤消签证押金的接口
	 * @param payId orderpay表的主键ID
	 * @param payType 收款/付款标识（1：收款；2：付款）
	 * @return
	 * @throws Exception 
	 * @throws PositionOutOfBoundException 
	 * @throws OptimisticLockHandleException 
	 */
	@RequestMapping(value = "/cancelVisaOper")
	@ResponseBody
	public Object cancelVisaOper(@RequestParam String payId,
			@RequestParam String payType) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {

		Map<String, String> data = new HashMap<String, String>();
		if (StringUtils.isBlank(payId)) {
			data.put("flag", "error");
			data.put("msg", "支付ID不能为空");
			return data;
		}

		if (StringUtils.isBlank(payType)) {
			data.put("flag", "error");
			data.put("msg", "支付类型不能为空（1：收款；2：付款）");
			return data;
		}

		if (!"1".equals(payType) && !"2".equals(payType)) {
			data.put("flag", "error");
			data.put("msg", "不存在该支付类型（1：收款；2：付款）");
			return data;
		}

		data = orderPayService.cancelVisaOper(payId);
		if ("error".equals(data.get("flag"))) {
			return data;
		}
		orderPayMoreService.cancelConfirmOper(payId, payType);

		data.put("flag", "ok");
		return data;
	}
	
	
	  @ResponseBody
	  @RequestMapping(value="getMoneyType")
	   public List<Orderpay> getMoneyType(Long orderId,Integer orderType){
		  List<Orderpay> list = orderPayService.findOrderpayByOrderIdAndOrderType(orderId, orderType);
		  return list;
	  }
	/**
	 * 跳转页面（支付成功）
	 * zhangchao 
	 * @param data
	 * @param input
	 * @param model
	 * @return
	 * 
	 */
	  @RequestMapping(value="paySuccess")
	  public String paySuccess(String data,String input,String orderDetailUrl,Model model){
	  		if(data.equals("modules/order/notCanPay")){
	  			return data;
	  		}
	  		com.alibaba.fastjson.JSONObject array = JSON.parseObject(input);
	  		if(array != null){
	  			Map<String,Object> orderPaySuccessParent=(Map<String,Object>)array.get("orderPaySuccessParent");
	  			List<Map<String,Object>> listMap=(List<Map<String,Object>>)orderPaySuccessParent.get("bean");
	  			List<OrderPaySuccessBean> list=new ArrayList<OrderPaySuccessBean>();
	  			if (listMap!=null) {
	  				model.addAttribute("orderNum", listMap.get(0).get("orderNum"));
	  				model.addAttribute("payTypeName", listMap.get(0).get("payTypeName").toString());
	  				model.addAttribute("curlist",listMap.get(0).get("curlist"));
	  				model.addAttribute("orderType", listMap.get(0).get("orderType"));
	  				List<BigDecimal> dqzfSumList=new ArrayList<BigDecimal>();
	  				if(listMap.size()>1){
	  					for(int i=0;i<listMap.size()-1;i++){
	  						List<String> dqzfprices1=(List<String>)listMap.get(i).get("dqzfprice");
	  						List<String> dqzfprices2=(List<String>)listMap.get(i+1).get("dqzfprice");
	  						for(int j=0;j<dqzfprices1.size();j++){
	  							for(int k=0;k<dqzfprices2.size();k++){
	  								if(j==k){
	  									BigDecimal b1=new BigDecimal(dqzfprices1.get(j));
	  									BigDecimal b2=new BigDecimal(dqzfprices2.get(k));
	  									BigDecimal sum=b1.add(b2);
	  									dqzfSumList.add(sum);
	  								}
	  							}
	  						}
	  					}
	  					model.addAttribute("dqzfprice", dqzfSumList);
	  				}else{
	  					model.addAttribute("dqzfprice", listMap.get(0).get("dqzfprice"));
	  				}
	  				List<Integer> currencyIdPrices=(List<Integer>)(listMap.get(0).get("currencyIdPrice"));
	  				Integer[] currencyIdPrice=new Integer[currencyIdPrices.size()];
	  				for(int i=0;i<currencyIdPrices.size();i++){
	  					currencyIdPrice[i]=currencyIdPrices.get(i);
	  				}
	  				model.addAttribute("currencyIdPrice",currencyIdPrice );
	  			}
	  			if(StringUtils.isNotBlank(orderDetailUrl)){
	  				model.addAttribute("orderDetailUrl",orderDetailUrl);
	  			}
	  		}else{
	  			return "modules/order/notCanPay";
	  		}
	  		return data;
	  }

	/**
	 * 获取关联发票列表
	 * @param orderId
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="relationInvoiceList")
	public Object relationInvoiceList(Long orderId,Model model){
		return orderinvoiceDao.findOrderinvoiceByOrderIdApplyWay(Integer.parseInt(orderId.toString()));
	}
	
	/**
	 * 关联发票成功 回款状态为 已回款
	 * @param relationInvoiceIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="changeInvoiceReceivedPayStatus")
	public Object changeInvoiceReceivedPayStatus(@RequestParam(required = false) String invoiceIds){
//		HashMap<Object, Object> Map = new HashMap<>();
//		if(StringUtils.isNoneBlank(invoiceIds)){
//			String[] ids = invoiceIds.split(",");
//			for (String id : ids) {
//				Orderinvoice invoice = orderinvoiceService.findOrderinvoiceById(Long.parseLong(id));
//				invoice.setReceivedPayStatus(1);
//				orderinvoiceService.saveOrderInvoice(invoice);
////				orderinvoiceDao.updateInvoicePayStatus(Long.parseLong(id));
//			}	
//			Map.put("flag", "success");
//		}
//		return Map;
		return orderinvoiceService.changeInvoiceReceivedPayStatus(invoiceIds);
	}

	@ResponseBody
	@RequestMapping(value="getAlipayAccount")
	public List<ZhifubaoInfo> getAlipayAccount(String name){
		if(name.equals("")){
			return null;
		}
		List<ZhifubaoInfo> list = selectService.getAlipayByName(name);
		return list;
	}
}

