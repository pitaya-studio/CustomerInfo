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
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.query.service.ISelectService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.finance.entity.ReturnDifference;
import com.trekiz.admin.modules.finance.service.ReturnDifferenceService;
import com.trekiz.admin.modules.hotel.service.HotelOrderService;
import com.trekiz.admin.modules.invoice.repository.OrderinvoiceDao;
import com.trekiz.admin.modules.invoice.service.OrderinvoiceService;
import com.trekiz.admin.modules.island.service.IslandOrderService;
import com.trekiz.admin.modules.order.airticket.service.IAirticketPreOrderService;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.entity.PlatBankInfo;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.entity.ZhifubaoInfo;
import com.trekiz.admin.modules.order.formBean.OrderPayForm;
import com.trekiz.admin.modules.order.pojo.OrderPayDetail;
import com.trekiz.admin.modules.order.pojo.OrderPayInput;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.service.OrderPayMoreService;
import com.trekiz.admin.modules.order.service.OrderPayService;
import com.trekiz.admin.modules.order.service.PlatBankInfoService;
import com.trekiz.admin.modules.order.service.ProductOrderService;
import com.trekiz.admin.modules.order.service.RefundService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.pay.service.PayFeeService;
import com.trekiz.admin.modules.pay.service.PayProductOrderService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 订单公共付款(批量支付，当然一件也应该是可以的)
 */
@Controller
@RequestMapping(value = "${adminPath}/orderPayMore")
public class OrderPayMoreController extends BaseController{

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private OrderPayMoreService orderPayMoreService;
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
	private OrderPayService orderPayService;
	@Autowired
	private RefundService refundService;
	@Autowired
	private PlatBankInfoService bankInfoService;

	@Autowired
	private IAirticketPreOrderService airticketPreOrderService;
	@Autowired
	private IslandOrderService islandOrderService;
	@Autowired
	private HotelOrderService hotelOrderService;
	@Autowired
	private PayProductOrderService payProductOrderService;
	@Autowired
	private PayFeeService payFeeService;
	@Autowired
	private OrderinvoiceService orderinvoiceService;
	@Autowired
	private OrderinvoiceDao orderinvoiceDao;
	@Autowired
	private ISelectService selectService;
	@Autowired
	private ReturnDifferenceService returnDifferenceService;
	@Autowired
	private ProductOrderService productOrderService;
	/**
	 * 支付页面初始化
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "pay")
	public String orderPay(HttpServletRequest request, ModelMap model) {

		logger.debug("[orderPay]===========开始");
		model.addAttribute("payPriceType", request.getParameter("payPriceType"));
		
		OrderPayInput orderPayInput = (OrderPayInput) request.getAttribute("pay");
		List<OrderPayDetail> orderPayDetailList = orderPayInput.getOrderPayDetailList();
		//因公支付宝 针对拉美途
		List<ZhifubaoInfo> list = selectService.getDistinctAlipay(UserUtils.getUser().getCompany().getId());
		model.addAttribute("alipay",list);
		for(ZhifubaoInfo info:list){
			if(info.getDelFlag().equals("0")){
				model.addAttribute("account",selectService.getAlipayByName(info.getName()));
			}
		}
		//因公支付宝 针对拉美图 增加收款单位
		model.addAttribute("comOffice",UserUtils.getUser().getCompany().getName());
		logger.debug("OrderPayInput对象:" + orderPayInput.toString());
		
		// ////////////////////////////////////////////////////////////////////////////////////////////////////
		/*
		 * 传入参数检查
		 */
		// ////////////////////////////////////////////////////////////////////////////////////////////////////
//		String serviceClassName = orderPayInput.getServiceClassName();
//		if (StringUtils.isEmpty(serviceClassName)) {
//			model.addAttribute("errorMsg", "请提供操作模块的service类名（包含包名），待会我要调用");
//			return "modules/order/notCanPay";
//		}

		if (orderPayDetailList == null || orderPayDetailList.size() == 0) {
			model.addAttribute("errorMsg", "没有请求的支付信息，请检查List<OrderPayDetail>对象是否有值");
			return "modules/order/notCanPay";
		}
		
		// 0444需求 orderId存到model
		model.addAttribute("orderId", orderPayInput.getOrderPayDetailList().get(0).getOrderId());
		// 付款类型,目前只对签证设置为"1",走里面添加hasPreOpeninvoice(针对运控成本录入，返佣付款等不用显示关联发票)
		if("1".equals(orderPayInput.getComingPay())){
			// 0444需求 判断在订单中是否有预开发票
			model.addAttribute("hasPreOpeninvoice", orderPayInput.getHasPreOpeninvoice());
		}

		// 检查金额信息
		for (int i = 0; i < orderPayDetailList.size(); i++) {
			OrderPayDetail orderPayDetail = orderPayDetailList.get(i);
			model.addAttribute("orderNum", orderPayDetail.getOrderNum());//订单号add by jiangyang 20150716
			
			//签证批量付款时可以更改付款金额，如果付款金额被改了，那么需要使用改后的金额
			//add by jiachen
			String payCurrencyId = "";
			String payCurrencyPrice = "";
			
			/*String currencyIds = request.getParameter("currencyIds");
			String prices = request.getParameter("prices");*/
			/*if(Context.ORDER_TYPE_QZ.equals(orderPayDetail.getOrderType()) 
					&& StringUtils.isNotBlank(currencyIds) && StringUtils.isNotBlank(prices)) {
				payCurrencyId = currencyIds.split(",")[i];
				payCurrencyPrice = prices.split(",")[i];
				
				orderPayDetail.setPayCurrencyId(payCurrencyId);
				orderPayDetail.setPayCurrencyPrice(payCurrencyPrice);
			}else{*/
				payCurrencyId = orderPayDetail.getPayCurrencyId();
				payCurrencyPrice = orderPayDetail.getPayCurrencyPrice();
			/*}*/

			if (StringUtils.isEmpty(payCurrencyId) || StringUtils.isEmpty(payCurrencyPrice)) {
				model.addAttribute("errorMsg", "orderPayDetailList中第" + (i + 1) + "条数据中应付金额信息为null或空字符串");
				return "modules/order/notCanPay";
			}

			if (payCurrencyId.split(",").length != payCurrencyPrice.split(",").length) {
				model.addAttribute(
						"errorMsg",
						"orderPayDetailList中第"
								+ (i + 1)
								+ "条数据中应付金额的币种ID[payCurrencyId]和金额[payCurrencyPrice]参数个数不匹配。详情内容：币种ID="
								+ orderPayMoreService
										.contactArrayVal(payCurrencyId
												.split(","))
								+ ";金额="
								+ orderPayMoreService
										.contactArrayVal(payCurrencyPrice
												.split(",")));
				return "modules/order/notCanPay";
			}

			String totalCurrencyId = orderPayDetail.getTotalCurrencyId();
			String totalCurrencyPrice = orderPayDetail.getTotalCurrencyPrice();
			if (StringUtils.isEmpty(totalCurrencyId) || StringUtils.isEmpty(totalCurrencyPrice)) {
				model.addAttribute("errorMsg", "orderPayDetailList中第" + (i + 1) + "条数据中总金额信息为null或空字符串");
				return "modules/order/notCanPay";
			}
			if (totalCurrencyId.split(",").length != totalCurrencyPrice.split(",").length) {
				model.addAttribute( "errorMsg",
						"orderPayDetailList中第"
								+ (i + 1)
								+ "条数据中总金额的币种ID[totalCurrencyId]和金额[totalCurrencyPrice]参数个数不匹配。详情内容：币种ID="
								+ orderPayMoreService
										.contactArrayVal(totalCurrencyId
												.split(","))
								+ ";金额="
								+ orderPayMoreService
										.contactArrayVal(totalCurrencyPrice
												.split(",")));
				return "modules/order/notCanPay";
			}

			if (!orderPayMoreService.validateMoney(payCurrencyPrice)) {
				model.addAttribute("errorMsg", "orderPayDetailList中第" + (i + 1) + "条数据中应付金额不为有效的金额，即不是double类型的数据");
				return "modules/order/notCanPay";
			}
			
			orderPayMoreService.processNegativeMoney(orderPayDetail, payCurrencyPrice);

			if (!orderPayMoreService.validateMoney(totalCurrencyPrice)) {
				model.addAttribute("errorMsg", "orderPayDetailList中第" + (i + 1) + "条数据中总金额不为有效的金额，即不是double类型的数据");
				return "modules/order/notCanPay";
			}
		}

		// 业务区分（收款、付款）
		String payType = orderPayInput.getPayType();

		// 检查我的订单、订单列表的URL
		if ("1".equals(payType)) {
			if (StringUtils.isBlank(orderPayInput.getOrderListUrl())) {
				model.addAttribute("errorMsg", "请在OrderPayInput对象中设置订单列表的URL(orderListUrl)，支付页面的暂不支付按钮要用");
				return "modules/order/notCanPay";
			}
		} else if ("2".equals(payType)) {
			if (StringUtils.isBlank(orderPayInput.getRefundMoneyTypeDesc())) {
				model.addAttribute("errorMsg", "请在OrderPayInput对象中设置款项(refundMoneyTypeDesc)，该项内容会显示在支付成功的款项部分");
				return "modules/order/notCanPay";
			}
			
			for (int i = 0; i < orderPayDetailList.size(); i++) {
				OrderPayDetail orderPayDetail = orderPayDetailList.get(i);

				if (orderPayDetail.getProjectId() == null || orderPayDetail.getProjectId() <= 0) {
					model.addAttribute("errorMsg", "请在OrderPayInput对象中设置业务表的ID(projectId)");
					return "modules/order/notCanPay";
				}

				if (orderPayDetail.getRefundMoneyType() == null || orderPayDetail.getRefundMoneyType() <= 0) {
					model.addAttribute("errorMsg", "请在OrderPayInput对象中设置业务的款项类型(moneyType)");
					return "modules/order/notCanPay";
				}
			}
		}
		// ////////////////////////////////////////////////////////////////////////////////////////////////////
		/*
		 * 页面参数的保存
		 */
		// ////////////////////////////////////////////////////////////////////////////////////////////////////
		model.addAttribute("payType", payType);
		model.addAttribute("payTypeDesc", orderPayMoreService.getPayTypeDesc(payType));
		model.addAttribute("payOrderTitle", orderPayMoreService.getPayOrderTitle(payType));

		// 导航图片显示状态
		model.addAttribute("navigationImgFlag", orderPayInput.getNavigationImgFlag());
		
		// 支付方式不同批发商的控制
		Long companyId = UserUtils.getUser().getCompany().getId();
		model.addAttribute("companyId", companyId);

		/*
		 * 金额部分
		 */
		boolean totalCurrencyFlag = orderPayInput.isTotalCurrencyFlag();
		model.addAttribute("totalCurrencyFlag", totalCurrencyFlag);
		//签证批量付款时可以更改付款金额，如果付款金额被改了，那么需要使用改后的金额
		//add by jiachen
		Map<String, String[]> moneyMap = new HashMap<String, String[]>();
		/*String currencyIds = request.getParameter("currencyIds");
		String prices = request.getParameter("prices");
		if(Context.ORDER_TYPE_QZ.equals(orderPayDetailList.get(0).getOrderType())
				&& StringUtils.isNotBlank(currencyIds) && StringUtils.isNotBlank(prices)) {
			moneyMap = orderPayMoreService.countMoney(orderPayMoreService.convertMoneyList2PayMoneyList(currencyIds, prices));
		}else{*/
			moneyMap = orderPayMoreService.countMoney(orderPayMoreService.convertMoneyList2PayMoneyList(orderPayDetailList));
		/*}*/
		
		
		String[] currencyArr = moneyMap.get("totalCurrencyId");
		model.addAttribute("paramCurrencyId", currencyArr);
		String[] priceList = moneyMap.get("totalCurrencyPrice");
		model.addAttribute("paramCurrencyPrice",priceList);

		moneyMap = orderPayMoreService.countMoney(orderPayMoreService.convertMoneyList2TotalMoneyList(orderPayDetailList));
		model.addAttribute("paramTotalCurrencyId", moneyMap.get("totalCurrencyId"));
		model.addAttribute("paramTotalCurrencyPrice", moneyMap.get("totalCurrencyPrice"));
		orderPayInput.setTotalCurrencyId(moneyMap.get("totalCurrencyId"));
		orderPayInput.setTotalCurrencyPrice(moneyMap.get("totalCurrencyPrice"));

		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		if (currencylist == null || currencylist.size() == 0) {
			model.addAttribute("errorMsg", "请在基础信息维护/币种信息功能中添加币种");
			return "modules/order/notCanPay";
		}
		model.addAttribute("curlist", currencylist);
		
		if(currencyArr != null && currencyArr.length > 0) {
			for(String currStr : currencyArr) {
				boolean flag = false;
				
				for(Currency currency : currencylist) {
					if(currency.getId().intValue() == Integer.parseInt(currStr)) {
						flag = true;
						break;
					}
				}
				
				if(!flag) {
					model.addAttribute("errorMsg", "支付信息中有本批发商不包含的币种信息，请重新选择支付记录！！！");
					return "modules/order/notCanPay";
				}
			}
		}
		
		
		// 金额部分的汇率是否显示Flag（0：不显示；1：显示）
		String convertFlag = orderPayMoreService.getConvertFlag(payType);
		model.addAttribute("convertFlag", convertFlag);
		
		if ("2".equals(payType)) {
			// 新行者的情况下
			if ("1".equals(convertFlag)) {
				//判断是否合并支付
				String mergePayFlag = orderPayMoreService.getMergePayFlag(orderPayDetailList.get(0).getProjectId(), orderPayDetailList.get(0).getRefundMoneyType());
				model.addAttribute("mergePayFlag", mergePayFlag);

				// 是否有支付记录，有的情况下，合并支付操作按上一次操作来处理（0:没有记录;1:有记录）
				model.addAttribute("convertUseFlag", orderPayMoreService.getConvertUseFlag(orderPayDetailList.get(0).getProjectId(), orderPayDetailList.get(0).getRefundMoneyType()));

				BigDecimal[] convertList = orderPayMoreService.getConvertLowest(orderPayDetailList.get(0).getProjectId(), orderPayDetailList.get(0).getRefundMoneyType(), moneyMap.get("totalCurrencyId"));
				if (convertList != null) {
					model.addAttribute("convertList", convertList);
					Map<String, Object> sumMoney = orderPayMoreService.sumMoney(priceList, convertList);
					model.addAttribute("subSum", sumMoney.get("subSum"));
					model.addAttribute("totalSum", sumMoney.get("totalSum"));
				}
			}
		}

		model.addAttribute("moneyFlag", orderPayInput.getMoneyFlag());

		/*
		 * 月结/后付费部分
		 */
		Integer paymentStatusFlag = orderPayMoreService.processPaymentStatusFlag(orderPayInput.getPayType(), orderPayInput.getAgentId());
		model.addAttribute("paymentStatusFlag", paymentStatusFlag);
		if (paymentStatusFlag.intValue() == 1) {
			model.addAttribute("paymentTypeRadioFlag", orderPayInput.getPaymentTypeRadioFlag());
			Integer paymentStatus = orderPayMoreService.getAgentPaymentType(orderPayInput.getAgentId());
			model.addAttribute("paymentStatus", paymentStatus);
			model.addAttribute("paymentStatusLblDesc", orderPayMoreService.processPaymentStatusDesc(paymentStatus));
		}

		//(收款单位) 来款单位
		model.addAttribute("payerName", orderPayMoreService.getPayerName(orderPayInput.getAgentId()));
		
		//收款单位默认账户
		List<PlatBankInfo> accList = bankInfoService.findDefaultBankInfo(Long.valueOf(orderPayInput.getAgentId()), orderPayInput.getSupplyType()+1);
		if(accList.size() >0){
			model.addAttribute("defaultAcc",accList.get(0));
		}else{
			model.addAttribute("defaultAcc",null);
		}
		
		// 汇款部分专用
		model.addAttribute("fmBankList", orderPayMoreService.getFmBankInfo(orderPayInput));
		/** // for 89
		Integer supplyType = orderPayInput.getSupplyType();
		switch (supplyType) {
		case 0:
			supplyType = 1;
		case 1:
			supplyType = 2;
		}
		request.getSession().setAttribute("platTypebank", supplyType);
		request.getSession().setAttribute("beLongPlatIdbank", orderPayInput.getAgentId());
		*/
		model.addAttribute("toBankList", orderPayMoreService.getToBankInfo(orderPayInput));
		model.addAttribute("fmBelongPlatId", orderPayMoreService.getFmBelongPlatId(orderPayInput));
		model.addAttribute("toBelongPlatId", orderPayMoreService.getToBelongPlatId(orderPayInput));

		JSONObject payInput = JSONObject.fromObject(orderPayInput);
		model.addAttribute("orderPayInputJson", payInput.toString());

		// 订单列表页URL
		model.addAttribute("orderListUrl", orderPayInput.getOrderListUrl());
		//暂不支付跳转链接
		model.addAttribute("entryOrderUrl", orderPayInput.getEntryOrderUrl());
		
		//订单中包含的币种id集合
		List<Currency> orderCurrencys = currencyService.getCurrencysByIds(currencyArr);
		model.addAttribute("orderCurrencys", orderCurrencys);
		
		model.addAttribute("isSHZL", false);
		if(Context.SUPPLIER_UUID_SHZL.equals(UserUtils.getUser().getCompany().getUuid())){
			model.addAttribute("isSHZL", true);
		}
		//代收服务费的结算方 add by zhangchao 2016-09-05
		model.addAttribute("settleName",orderPayInput.getSettleName());
		logger.debug("[orderPay]===========正常处理完");

		return "modules/order/payOrderMore";
	}

	/**
	 * 支付信息保存
	 * 
	 * @param orderPayForm
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "savePay")
	public String savePay(OrderPayForm orderPayForm, HttpServletRequest request, ModelMap model) {

		// ////////////////////////////////////////////////////////////////////////////////////////////////////
		/*
		 * 支付信息保存
		 */
		// ////////////////////////////////////////////////////////////////////////////////////////////////////
		boolean bFlag = orderPayMoreService.savePayInfo(orderPayForm, model);
		if (!bFlag) {
			return "modules/order/notCanPay";
		}

		// ////////////////////////////////////////////////////////////////////////////////////////////////////
		/*
		 * 跳转至成功页面的参数保存
		 */
		// ////////////////////////////////////////////////////////////////////////////////////////////////////
		orderPayMoreService.saveOutputParameter(orderPayForm, model);
		if (!"1".equals(orderPayForm.getPaymentStatus().toString())) {
			return "forward:" + Global.getAdminPath() + orderPayForm.getOrderPayInput().getOrderListUrl();
		}
		
		// 0444 关联发票操作 
		if(StringUtils.isNotBlank(orderPayForm.getRelationInvoiceIds())){
			orderinvoiceService.changeInvoiceReceivedPayStatus(orderPayForm.getRelationInvoiceIds());
		}
        
		//操作日志记录
//		List<String> list = genLogInfo(orderPayForm);
//		int num =list.size();
//		for(int i=0;i<num;i++){
//			saveUserOpeLog("5","财务",list.get(i),"3");
//		}
		return "modules/order/payMoreSuccess";
	}
      //拼装支付和付款日志
	  public List<String> genLogInfo(OrderPayForm orderPayForm){
		  List<OrderPayDetail> list = orderPayForm.getOrderPayInput().getOrderPayDetailList();
		  String payType=orderPayForm.getOrderPayInput().getPayType();
		  List<String> logInfoList = new ArrayList<String>();
		  StringBuffer buffer = null;
		  int num = list.size();
		  for(int i =0;i<num;i++){
			 
				  buffer = new StringBuffer();
//					销售签证订单号JHG150206032 支票方式付全款：人民币金额：9.00付款单位：思锐 开票号：000000开票日期：2015-02-06 支票图片：XXX.jpg 备注信息:付款XXXXX
//					现金支付，支票，汇款同上；
				  
				  buffer.append(OrderCommonUtil.getChineseOrderType(list.get(i).getOrderType().toString()) + "订单");
				  buffer.append(list.get(i).getOrderNum());
				  switch (orderPayForm.getPayType()) {
					case 1:// 支票支付
						buffer.append(" 支票支付");
						break;
					case 3:// 现金支付
						buffer.append(" 现金支付");
						break;
					case 4:// 汇款
						buffer.append(" 汇款支付");	
						
						break;
					case 5:// 快速支付
						buffer.append(" 快速支付");
						break;
					}
				  switch(list.get(i).getPayPriceType()){
					  case 1://全款
						  buffer.append("全款 ").append("金额:").append("");
						  break;
					  case 2://尾款
						  buffer.append("尾款").append("金额:").append("");
						  break;
					  case 3://定金
						  buffer.append("定金").append("金额:").append("");
						  break;
				  }
				  switch (orderPayForm.getPayType()) {
					case 1:// 支票支付
						buffer.append(" 支票号：").append(orderPayForm.getCheckNumber()).append(" 开票日期：").append(orderPayForm.getInvoiceDate());
						break;
					case 3:// 现金支付
						
						break;
					case 4:// 汇款
						buffer.append(" 开户行名称").append(orderPayForm.getBankName()).append(" 开户行账户：").append(orderPayForm.getBankAccount()).append(" 转入航名称：")
						.append(orderPayForm.getToBankNname()).append(" 转入行账户：").append(orderPayForm.getToBankAccount());	
						
						break;
					case 5:// 快速支付
						break;
					}
				  
				  buffer.append(" 操作时间：").append(DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
				  if(StringUtils.isNotBlank(payType) && "1".equals(payType)){
					  if(StringUtils.isNotBlank(orderPayForm.getPayerName())){
					     buffer.append(" 付款单位：").append(orderPayForm.getPayerName());
					  }
				  }
				  logInfoList.add(buffer.toString());
				 
		  }
		  return logInfoList;
	  }
	
	/**
	 * 由支付方式和支付ID取得相应的支付信息详情6
	 * 
	 * @param payId
	 * @param payType
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "showPayDetailInfo")
	public String showPayDetailInfo(HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		PayInfoDetail payInfoDetail = new PayInfoDetail();
		String payType = request.getParameter("payType");
		String payId = request.getParameter("payId");
		String orderType = request.getParameter("orderType");
		// 付款
		if ("2".equals(payType)) {
			payInfoDetail = refundService.getPayInfoByPayId(payId,orderType);
		}

		orderPayMoreService.saveModelParameter(payInfoDetail, model);
		return "modules/order/payDetailInfo";
	}

	/**
	 * 由所属平台id和银行名称取得银行的账号信息
	 * 
	 * @param belongPlatId
	 * @param response
	 * @param bankName
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/getBankAccount/{belongPlatId}")
	@ResponseBody
	public void getBankAccount(@PathVariable String belongPlatId,
			HttpServletResponse response,
			@RequestParam(required = false) String bankName) {
		PrintWriter out = null;
		try {
			bankName = URLDecoder.decode(bankName, "UTF-8");
			List banks = orderPayMoreService.getBankAccountInfo(Long.parseLong(belongPlatId), bankName);
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

	/**
	 * 撤消支付确认操作
	 * 
	 * @param payId
	 * @param payType
	 * @return
	 * @throws Exception 
	 * @throws PositionOutOfBoundException 
	 * @throws OptimisticLockHandleException 
	 */
	@RequestMapping(value = "/cancelConfirmOper")
	@ResponseBody
	public Object cancelConfirmOper(@RequestParam String payId,
			@RequestParam String payType,
			@RequestParam("ordertype") String ordertype,
			@RequestParam("orderId") String orderId) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {

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

		// 收款
		if ("1".equals(payType)) {
			/**
			 * @author:wuqiang 
			 *                 机票订单撤销，调用徐展-宋杨的接口。service层：AirticketPreOrderService.airticketOrderCancel
			 *                 
			 */
			if (StringUtils.isNotBlank(ordertype) && Context.ORDER_STATUS_AIR_TICKET.equals(ordertype)) {
				/** 判断是否是机票订单（订单类型：机票，ordertype=7） */
				if (StringUtils.isNotBlank(orderId) && StringUtils.isNotBlank(payId)) {
					/** 调用徐展-宋杨的接口 */
					boolean result = airticketPreOrderService.airticketOrderCancel(Long.parseLong(orderId), Long.parseLong(payId));
					if (result) {
						data.put("flag", "ok");
					} else {
						data.put("flag", "error");
					}
				} else {
					data.put("flag", "error");
				}
			}else {
				//撤销的时候减去差额
				Orderpay orderPay = orderPayService.findOrderpayById(Long.parseLong(payId));
				ProductOrderCommon orderCommon = productOrderService.getProductorderById(orderPay.getOrderId());
				if(orderPay.getOrderType() == 2 && StringUtils.isNotBlank(orderPay.getDifferenceUuid())){
					ReturnDifference difference = returnDifferenceService.getReturnDifferenceByUuid(orderPay.getDifferenceUuid());
					difference.setToAccountType(0);
					difference.setUpdateBy(UserUtils.getUser().getId().intValue());
					difference.setUpdateDate(new Date());
					returnDifferenceService.updateToAccountType(difference);
					orderPayService.differenceMoneySubToAccountedMoney(orderCommon.getAccountedMoney(), difference);
				}
				// 其他类型订单（不包括签证）
				orderPayMoreService.cancelConfirmOper(payId, payType);
			}

		}

		data.put("flag", "ok");
		return data;
	}
	
	/**
	 * 新产品撤消支付确认操作
	 * 
	 * @param payId
	 * @param payType
	 * @return
	 */
	@RequestMapping(value = "/newProductCancelConfirmOper")
	@ResponseBody
	public Object newProductCancelConfirmOper(@RequestParam String payUuid,
			@RequestParam String payType,
			@RequestParam("ordertype") String ordertype,
			@RequestParam("orderUuid") String orderUuid) {

		Map<String, String> data = new HashMap<String, String>();
		if (StringUtils.isBlank(payUuid)) {
			data.put("flag", "error");
			data.put("msg", "支付uuid不能为空");
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
		
		if(StringUtils.isEmpty(ordertype)) {
			data.put("flag", "error");
			data.put("msg", "订单类型不能为空（11、酒店；12、海岛游）");
			return data;
		}
		
		if(StringUtils.isEmpty(orderUuid)) {
			data.put("flag", "error");
			data.put("msg", "订单uuid不能为空）");
			return data;
		}

		// 收款
		if ("1".equals(payType)) {
			//修改支付表订单的达帐状态
			payProductOrderService.updateAccountStatus(Integer.parseInt(ordertype), payUuid, Context.ORDERPAY_ACCOUNT_STATUS_YCX);
			
			if(Context.ORDER_TYPE_ISLAND == Integer.parseInt(ordertype)) {
				islandOrderService.islandOrderCancel(payUuid, orderUuid);
			} else if(Context.ORDER_TYPE_HOTEL == Integer.parseInt(ordertype)) {
				hotelOrderService.hotelOrderCancel(payUuid, orderUuid);
			}
		}

		data.put("flag", "ok");
		return data;
	}

	/**
	 * 驳回支付确认操作
	 * 
	 * @param payId
	 *            orderpay表的主键ID
	 * @param payType
	 *            收款/付款标识（1：收款；2：付款）
	 * @return
	 */
	@RequestMapping(value = "/rejectConfirmOper")
	@ResponseBody
	public Object rejectConfirmOper(@RequestParam String payId,
			@RequestParam String payType) {

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

		// 收款
		if ("1".equals(payType)) {
			orderPayMoreService.rejectConfirmOper(payId, payType, null);
		}

		data.put("flag", "ok");
		return data;
	}
	
	/**
	 * 付款-撤消操作
	 * 
	 * @param payId
	 * @param payType
	 * @return
	 */
	@RequestMapping(value = "/cancelOper")
	@ResponseBody
	public Object cancelOper(@RequestParam String payId) {

		Map<String, String> data = new HashMap<String, String>();
		if (StringUtils.isBlank(payId)) {
			data.put("flag", "error");
			data.put("msg", "支付ID不能为空");
			return data;
		}

		orderPayMoreService.cancelOper(payId, "2");

		data.put("flag", "ok");
		return data;
	}
}
