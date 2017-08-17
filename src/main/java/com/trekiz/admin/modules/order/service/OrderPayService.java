package com.trekiz.admin.modules.order.service;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.finance.entity.ReturnDifference;
import com.trekiz.admin.modules.finance.repository.ReturnDifferenceDao;
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;
import com.trekiz.admin.modules.hotel.service.HotelMoneyAmountService;
import com.trekiz.admin.modules.island.dao.IslandTravelerDao;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;
import com.trekiz.admin.modules.island.service.IslandMoneyAmountService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.airticket.service.AirticketPreOrderService;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.entity.PlatBankInfo;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.OrderpayDao;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.pay.model.PayProductOrder;
import com.trekiz.admin.modules.statisticAnalysis.home.service.OrderDateSaveOrUpdateService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.sys.repository.ZhifubaoDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.repository.VisaDao;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;

@Service
@Transactional(readOnly = true)
public class OrderPayService {
	@Autowired
	private OrderpayDao orderpayDao;
	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	@Autowired
	private VisaOrderDao visaOrderDao;
	@Autowired
	private OrderCommonService orderService;
	@Autowired
	private OrderPayMoreService orderPayMoreService;
	@Autowired
	private AirticketPreOrderService airticketPreOrderService;
	@Autowired
	private TravelActivityService travelActivityService;
	@Autowired
	private VisaDao visaDao;
	@Autowired
	private MoneyAmountDao moneyAmountDao;
	@Autowired
	private TravelerDao travelerDao;
	@Autowired
	private ActivityAirTicketDao activityAirTicketDao;
	@Autowired
	private DocInfoDao docInfoDao;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private MoneyAmountService monryAmountService;
	@Autowired
	private IslandMoneyAmountService islandMoneyAmountService;
	@Autowired
	private HotelMoneyAmountService hotelMoneyAmountService;
	@Autowired
	private OrderStockService orderStockService;
	@Autowired
	private AirticketOrderStockService airticketOrderStockService;
	@Autowired
	private IslandTravelerDao islandTravelerDao;
	@Autowired
	private PlatBankInfoService platBankInfoService;
	@Autowired
	private ZhifubaoDao zhifubaoDao;
	@Autowired
    private OrderProgressTrackingService progressService;
	@Autowired
	private ReturnDifferenceDao returnDifferenceDao;
	@Autowired
	private ProductOrderService productOrderService;
	@Autowired
	private OrderDateSaveOrUpdateService orderDateSaveOrUpdateService;
	/**
	 * 订单支付保存
	 * 
	 * @return
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public String savePay(String agentId, Long orderId, String orderNum, Integer businessType,
			Integer orderType, Integer payType, Integer payPriceType,
			Integer paymentStatus, Integer[] currencyIdPrice,
			BigDecimal[] dqzfprice, String remarks, Long[] DocInfoIds,
			String fastPayType, String bankName, String bankAccount,
			String toBankNname, String toBankAccount, String payerName,
			String checkNumber, Date invoiceDate, String isCommonOrder,
			String visaId, HttpServletRequest request, ModelMap model) throws Exception {

		// 给签证交押金的时候专用
		String visaid = request.getParameter("visaId");
		Orderpay orderPay = new Orderpay();
		// 订单类型
		orderPay.setOrderType(orderType);
		// 订单id
		orderPay.setOrderId(orderId);
		// 订单号
		orderPay.setOrderNum(orderNum);
		// 支付方式
		orderPay.setPayType(payType);
		// 支付款类型（全款、定金、尾款）
		orderPay.setPayPriceType(payPriceType);
		//orderPay.setPayPriceType(moneyType);//新添加的
		//判断是否有差额
		ProductOrderCommon orderCommon = productOrderService.getProductorderById(orderId);
		ReturnDifference returnDifference = null;
		if(orderCommon.getDifferenceFlag()==1 && orderType == 2){
			try {
				Date date = new Date();
				String uuid = UuidUtils.generUuid();
				String returnPrice = request.getParameter("returnPrice");
				returnDifference = new ReturnDifference();
				returnDifference.setCompanyId(UserUtils.getUser().getCompany().getId().intValue());
				returnDifference.setCreateBy(UserUtils.getUser().getId().intValue());
				returnDifference.setCreateDate(date);
				returnDifference.setCurrencyId(Integer.parseInt(request.getParameter("differenceCurrencyId")));
				returnDifference.setDelFlag(0);
				returnDifference.setOrderId(orderId.intValue());
				returnDifference.setOrderNum(orderNum);
				returnDifference.setOrderType(orderType);
				returnDifference.setReturnPrice(new BigDecimal(returnPrice));
				returnDifference.setToAccountType(0);
				returnDifference.setUpdateBy(UserUtils.getUser().getId().intValue());
				returnDifference.setUpdateDate(date);
				returnDifference.setUuid(uuid);
				returnDifferenceDao.saveObj(returnDifference);
				orderPay.setDifferenceUuid(uuid);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 结算方式
		if (paymentStatus == null) {
			paymentStatus = 1;
		}
		orderPay.setPaymentStatus(paymentStatus);
		// 支付方式名称
		orderPay.setPayTypeName(orderPayMoreService.getPayTypeName(paymentStatus, payType));
		if(UserUtils.getUser().getCompany().getUuid().equals("7a81a26b77a811e5bc1e000c29cf2586") && payType==9){
			orderPay.setPayTypeName("因公支付宝");
		}

		if (paymentStatus == 1) {

			// 保存支付订单金额
			String moneyUuid = UUID.randomUUID().toString();
			orderPay.setMoneySerialNum(moneyUuid);
			
			// 付款单位
			if (StringUtils.isNotEmpty(payerName)) {
				orderPay.setPayerName(payerName);
			}
 			switch (payType) {
			case 1:// 支票支付
//				orderPay.setPayTypeName("支票支付");
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
//				orderPay.setPayTypeName("现金支付");
				break;
			case 4:// 汇款
//				orderPay.setPayTypeName("汇款");
				// 开户行名称
				if (StringUtils.isNotBlank(bankName) && !"-1".equals(bankName)) {
					orderPay.setBankName(bankName.trim());
				}
				// 开户行账户
//				bankAccount = "66666699999999999999999000000000000000";
				if (StringUtils.isNotBlank(bankAccount) && !"-1".equals(bankAccount)) {
					bankName = bankName.trim();
					bankAccount = bankAccount.trim();
					orderPay.setBankAccount(bankAccount);
					//来款行名称、来款账户反写到该渠道商信息中 for 89
//					List<PlatBankInfo> plantBankList = platBankInfoService.getPlatBankInfo(bankName, bankAccount);
					List<PlatBankInfo> plantBankList = platBankInfoService.getPlantBankInfoForAgintidAndBankAccount(Long.parseLong(agentId), bankAccount);
					PlatBankInfo platBankInfo = new PlatBankInfo();
					if(null == plantBankList || plantBankList.size() == 0){
						platBankInfo.setBeLongPlatId(Long.parseLong(agentId));
						platBankInfo.setBankName(bankName);
						platBankInfo.setBankAccountCode(bankAccount.trim());
						platBankInfo.setCreateBy(UserUtils.getUser());
						Date date = new Date();
						platBankInfo.setCreateDate(date);
						platBankInfo.setPlatType(Context.PLAT_TYPE_QD);
						platBankInfo.setDelFlag("0");
						platBankInfoService.save(platBankInfo);
					}
				}
				// 转入行名称
				if (StringUtils.isNotEmpty(toBankNname) && !"-1".equals(toBankNname)) {
					orderPay.setToBankNname(toBankNname);
				}
				// 转入行账号
				if (StringUtils.isNotEmpty(toBankAccount) && !"-1".equals(toBankAccount)) {
					orderPay.setToBankAccount(toBankAccount);
				}
				break;
			case 5:// 快速支付
//				orderPay.setPayTypeName("快速支付");
				// 支付方式
				if (StringUtils.isNotEmpty(fastPayType)) {
					orderPay.setFastPayType(fastPayType);
				}
				break;
			case 9://因公支付宝支付	
				//支付宝账户（来款）
				if(StringUtils.isNotEmpty(request.getParameter("fromAlipayName"))){
					orderPay.setFromAlipayName(request.getParameter("fromAlipayName"));
				}
				if(StringUtils.isNotEmpty(request.getParameter("fromAlipayAccount"))){
					orderPay.setFromAlipayAccount(request.getParameter("fromAlipayAccount"));
				}
				if(StringUtils.isNotEmpty(request.getParameter("toAlipayName"))){
					orderPay.setToAlipayName(request.getParameter("toAlipayName"));
				}else{
					orderPay.setToAlipayName(request.getParameter("toAlipayName1"));
				}
				if(StringUtils.isNotEmpty(request.getParameter("toAlipayAccount"))){
					orderPay.setToAlipayAccount(request.getParameter("toAlipayAccount"));
				}else{
					orderPay.setToAlipayAccount(request.getParameter("toAlipayAccount1"));
				}
				if(StringUtils.isNotEmpty(request.getParameter("comeOfficeName"))){
					orderPay.setComeOfficeName(request.getParameter("comeOfficeName"));
				}
			}

			// 备注信息
			if (StringUtils.isNotEmpty(remarks)) {
				orderPay.setRemarks(remarks);
			}
		}

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (StringUtils.isNotBlank(isCommonOrder)) {
				// 机票支付
				if ("airticket".equals(isCommonOrder)) {
					map = airticketOrderPay(orderPay, request);
				}
				// 单团、散拼、游学、大客户、自由行支付
				else {
					map = saveOrderPay(orderPay, request);// 此方法中有余位扣减操作
				}
			} else {
				// 款项类型是"交押金"的时候,按照交押金的处理方式计算
				if (payPriceType != null && Context.MONEY_TYPE_SYJ.intValue() == payPriceType) {
					map = saveYajinPay(orderPay, visaid, dqzfprice[0], currencyIdPrice[0]);
				} else {
					// 签证支付
					// TODO 	计划删除
					// map = saveOrderPay(orderPay);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("isSuccess", false);
			if (StringUtils.isNotBlank(e.getMessage())) {
				model.addAttribute("errorMsg", e.getMessage());
			} else {
				model.addAttribute("errorMsg", "订单支付失败");
			}
			throw e;
		}

		// 返回值中有错误信息的情况下
		if ((Boolean) (map.get("isSuccess")) == false) {
 			model.addAttribute("errorMsg", map.get("errorMsg"));
			return "modules/order/notCanPay";
		}

		// ///////////////////////////////////////////////////////////////////////////////////////////////////////////

		if (paymentStatus == 1 && (null == visaid || "".equals(visaid) || null == payPriceType || Context.MONEY_TYPE_SYJ.intValue() != payPriceType)) {

			List<MoneyAmount> moneyAmounts = new ArrayList<MoneyAmount>();
			for (int i = 0; i < dqzfprice.length; i++) {
				MoneyAmount moneyAmount = new MoneyAmount();
				// 流水号UUID
				moneyAmount.setSerialNum(orderPay.getMoneySerialNum());
				// 币种ID
				moneyAmount.setCurrencyId(currencyIdPrice[i]);
				// 金额
				moneyAmount.setAmount(dqzfprice[i]);
				// 订单ID或游客ID
				moneyAmount.setUid(orderId);
				// 款项类型
				moneyAmount.setMoneyType(payPriceType);
				// 订单产品类型
				moneyAmount.setOrderType(orderType);
				// 业务类型(1表示订单，2表示游客)
				moneyAmount.setBusindessType(businessType);
				// 记录人ID
				moneyAmount.setCreatedBy(UserUtils.getUser().getId());
				moneyAmounts.add(moneyAmount);
			}
			moneyAmountService.saveMoneyAmounts(moneyAmounts);
			
			// 散拼订单添加预报名订单门店返还差额
			Integer differenceFlag = orderCommon.getDifferenceFlag();
			Long preOrderId = orderCommon.getPreOrderId();
			if (null != differenceFlag && null != preOrderId) {
				differenceMoneyAddToPayedMoney(orderCommon.getPayedMoney(), returnDifference);
			}
		}

		if (paymentStatus == 1) {
			save(orderPay);
		}
		
		if (paymentStatus == 1) {
			// 保存支付凭证ids
			String payVoucher = "";
			if (DocInfoIds != null && DocInfoIds.length > 0) {
				for (Long docInfoId : DocInfoIds) {
					payVoucher += docInfoId + ",";
				}
			}
			orderPay.setPayVoucher(payVoucher);

			// 为上传资料设置payorderid属性
			saveDocInfoByPayUUID(orderPay.getMoneySerialNum(), DocInfoIds);
		}
		
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		model.addAttribute("curlist", currencylist);
		model.addAttribute("orderId", orderId);
		model.addAttribute("orderNum", orderNum);
		model.addAttribute("orderType", orderType);
		model.addAttribute("payTypeName", orderPay.getPayTypeName());
		model.addAttribute("payPriceType", payPriceType);
		model.addAttribute("currencyIdPrice", currencyIdPrice);
		model.addAttribute("dqzfprice", dqzfprice);
		model.addAttribute("orderListUrl", getOrderListUrl(orderType));
		model.addAttribute("orderDetailUrl", request.getParameter("orderDetailUrl"));
		model.addAttribute("paymentStatus", paymentStatus);
		
		if (!"1".equals(paymentStatus.toString())) {
			return "redirect:" + Global.getAdminPath() + getOrderListUrl(orderType);
		}

		return "modules/order/paySuccess";
	}

	/**
	 * 散拼订单添加预报名订单门店返还差额
	 * @param payedMoney
	 * @param returnDifference
	 * @param moneyAmounts
	 * @author yakun.bai
	 * @Date 2016-10-26
	 */
	private void differenceMoneyAddToPayedMoney(String payedMoney, ReturnDifference returnDifference) {
		if (StringUtils.isNotBlank(payedMoney) && null != returnDifference) {
			
			MoneyAmount moneyAmount = new MoneyAmount();
			// 流水号UUID
			moneyAmount.setSerialNum(payedMoney);
			// 币种ID
			moneyAmount.setCurrencyId(returnDifference.getCurrencyId());
			// 金额
			moneyAmount.setAmount(returnDifference.getReturnPrice());
			// 订单ID或游客ID
			moneyAmount.setUid(returnDifference.getOrderId().longValue());
			// 订单产品类型
			moneyAmount.setOrderType(2);
			// 业务类型(1表示订单，2表示游客)
			moneyAmount.setBusindessType(1);
			// 记录人ID
			moneyAmount.setCreatedBy(UserUtils.getUser().getId());
			
			moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount, payedMoney, "add", 5);
		}
		
	}
	
	public String getOrderListUrl(Integer orderType) {

		switch (orderType) {
		case 1:
			return "/orderList/manage/showOrderList/0/1";
		case 2:
			return "/orderList/manage/showOrderList/0/2";
		case 3:
			return "/orderList/manage/showOrderList/0/3";
		case 4:
			return "/orderList/manage/showOrderList/0/4";
		case 5:
			return "/orderList/manage/showOrderList/0/5";
		case 6:
			return "/visa/order/searchxs";
		case 7:
			return "/airticketOrderList/manage/airticketOrderList/1";
		case 10:
			return "/orderList/manage/showOrderList/0/10";

		}

		return "/activity/managerforOrder/list/1/2";
	}

	/**
	 * 根据uuid找到订单支付表的主键id,保存到上传资料表中 uuid 订单支付表的付款uuid docIds 资料表的id数组
	 * 
	 * */
	private void saveDocInfoByPayUUID(String uuid, Long[] docIds) {
		if (docIds == null || docIds.length == 0 || StringUtils.isEmpty(uuid)) {
			return;
		}
		String paySQL = "select id from orderpay where moneySerialNum = ? ";
		String docinfoSQL = "update docinfo set payOrderId =? where id = ? ";
		List<Object> par = new ArrayList<Object>();
		par.add(uuid);
		List<Map<String, Object>> resultList = travelerDao.findBySql(paySQL, Map.class, par.toArray());

		for (Map<String, Object> listin : resultList) {
			if (null != listin.get("id") && !"".equals(listin.get("id"))) {
				for (int i = 0; i < docIds.length; i++) {
					List<Object> parameter = new ArrayList<Object>();
					parameter.add(listin.get("id"));
					parameter.add(docIds[i]);
					docInfoDao.updateBySql(docinfoSQL, parameter.toArray());
				}
			}
		}
	}
	
	/**
	 * 根据uuid找到订单支付表的主键id,保存到上传资料表中 uuid 订单支付表的付款uuid docIds 资料表的id数组
	 * 
	 * */
	private void saveDocInfoByPayIslandUuid(String uuid, Long[] docIds) {
		if (docIds == null || docIds.length == 0 || StringUtils.isEmpty(uuid)) {
			return;
		}
		String paySQL = "select id from pay_island_order where moneySerialNum = ? ";
		String docinfoSQL = "update docinfo set payOrderId =? where id = ? ";
		List<Object> par = new ArrayList<Object>();
		par.add(uuid);
		List<Map<String, Object>> resultList = travelerDao.findBySql(paySQL, Map.class, par.toArray());

		for (Map<String, Object> listin : resultList) {
			if (null != listin.get("id") && !"".equals(listin.get("id"))) {
				for (int i = 0; i < docIds.length; i++) {
					List<Object> parameter = new ArrayList<Object>();
					parameter.add(listin.get("id"));
					parameter.add(docIds[i]);
					docInfoDao.updateBySql(docinfoSQL, parameter.toArray());
				}
			}
		}
	}

	/**
	 * 单团、散拼、游学、大客户、自由行支付
	 * 
	 * @param orderPay
	 * @param request
	 * @return
	 * @throws OptimisticLockHandleException
	 * @throws PositionOutOfBoundException
	 * @throws Exception
	 */
	private Map<String, Object> saveOrderPay(Orderpay orderPay,
			HttpServletRequest request) throws Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		Long orderId = orderPay.getOrderId();
		ProductOrderCommon pra = orderService.getProductorderById(orderId);
		String payStatus = pra.getPayStatus() != null ? pra.getPayStatus().toString() : "";

		// 如果订单已经被取消或被删除则不能支付订单
		if (pra.getPayStatus() != null
				&& Context.ORDER_PAYSTATUS_YQX.equals(pra.getPayStatus().toString())) {
			map.put("isSuccess", false);
			map.put("errorMsg", "订单已取消，不能支付");
			return map;
		}
		if (pra.getPayStatus() != null
				&& Context.ORDER_PAYSTATUS_DEL.equals(pra.getPayStatus().toString())) {
			map.put("isSuccess", false);
			map.put("errorMsg", "订单已删除，不能支付");
			return map;
		}

		map = orderService.changeOrderStatusAndGroupFreeNum(pra, orderPay, request); // 此方法中有余位扣减操作
		
		//如果有错误提醒，则停止流程并返回错误信息
		if (map.get("isSuccess") != null && !Boolean.parseBoolean(map.get("isSuccess").toString())) {
			return map;
		}

		// 子订单处理
		if (StringUtils.isNotBlank(payStatus)
				&& orderPay.getPaymentStatus() == Context.PAYMENT_TYPE_JS) {

			// 如果订单支付状态为"全款未支付"或"订金未支付"，则创建签证子订单、机票子订单
			if (Context.ORDER_PAYSTATUS_WZF.equals(payStatus)
					|| Context.ORDER_PAYSTATUS_DJWZF.equals(payStatus)
					|| Context.ORDER_PAYSTATUS_YZW.equals(payStatus)) {
				if (UserUtils.getUser().getCompany().getCreateSubOrder().contains("2")) {
					//C281青岛凯撒关闭签证子订单功能 changed by 2015-10-27
					if(UserUtils.getUser().getCompany().getId() != 72){
						String companyUuid = UserUtils.getUser().getCompany().getUuid();
						if(!("75895555346a4db9a96ba9237eae96a5".equals(companyUuid) || "7a81c5d777a811e5bc1e000c29cf2586".equals(companyUuid))) {
							orderService.createSingleSubOrder(pra.getId());
						}
					}
				}
				TravelActivity activity = travelActivityService.findById(pra.getProductId());
				ActivityAirTicket airticket = activity.getActivityAirTicket();
				if (airticket != null && UserUtils.getUser().getCompany().getCreateSubOrder().contains("1")) {
					try {
						airticketPreOrderService.createAirticketOrder(airticket.getId(), pra);
					} catch (Exception e) {
						map.put("isSuccess", false);
						map.put("errorMsg", "机票子订单创建失败");
						throw e;
					}
					
				}
			}
		}
		
		// 订单跟踪记录添加
		progressService.addPayInfoTime(pra, true);

		map.put("isSuccess", true);
		return map;
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	private void save(Orderpay orderPay) {
		orderpayDao.save(orderPay);
	}

	/**
	 * 机票支付
	 * 
	 * @param orderPay
	 * @param request
	 * @return
	 * @throws OptimisticLockHandleException
	 * @throws PositionOutOfBoundException
	 * @throws Exception
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	private Map<String, Object> airticketOrderPay(Orderpay orderPay,
			HttpServletRequest request) throws OptimisticLockHandleException,
			PositionOutOfBoundException, Exception {

		Map<String, Object> map = new HashMap<String, Object>();
		Long orderId = orderPay.getOrderId();
		AirticketOrder pra = airticketOrderDao.getAirticketOrderById(orderId);
		// 全款-1 尾款-2 订金-3
		Integer payPriceType = orderPay.getPayPriceType();

		// 如果订单已经被取消或被删除则不能支付订单
		if (pra.getOrderState() != null
				&& Context.ORDER_PAYSTATUS_YQX.equals(pra.getOrderState().toString())) {
			map.put("isSuccess", false);
			map.put("errorMsg", "订单已取消，不能支付");
			return map;
		}
		if (pra.getOrderState() != null
				&& Context.ORDER_PAYSTATUS_DEL.equals(pra.getOrderState().toString())) {
			map.put("isSuccess", false);
			map.put("errorMsg", "订单已删除，不能支付");
			return map;
		}
		
		ActivityAirTicket activityAirTicket = activityAirTicketDao.findOne(pra.getAirticketId());
		
		//只有当订单不是再次支付的时候才会扣减余位
		//只有当订单是立即支付时才扣减余位售出占位（针对月结、后付费，因为订单初始是立即支付，扣减余位再次支付时订单为月结或后付费）；
		if (!Context.ORDER_PAYSTATUS_YZFDJ.equals(pra.getOrderState().toString()) 
				&& !Context.ORDER_PAYSTATUS_YZF.equals(pra.getOrderState().toString())
				&& !Context.ORDER_PAYSTATUS_CW.equals(pra.getOrderState().toString()) && pra.getPaymentStatus() == 1) {
			
			airticketOrderStockService.changeGroupFreeNum(activityAirTicket, pra, null, Context.StockOpType.PAY);
			
			List<Traveler> travelers = travelerDao.getTicketTravelersByOrderIdAndOrderType(orderId, Context.ORDER_TYPE_JP, -1);
			for (Traveler tr : travelers) {// 游客占票
				if(!"0".equals(tr.getIsAirticketFlag())){
					tr.setIsAirticketFlag("1");	
				}else{
					tr.setIsAirticketFlag("0");	
				}
				travelerDao.save(tr);
			}
		}

		Integer paymentStatus = orderPay.getPaymentStatus();

		if (payPriceType != null && paymentStatus == 1) {

			// 如果是订金支付，则订单支付后状态为"订金已支付"，且支付订单状态改为"订金已经支付 "
			if (Context.ORDER_ORDERTYPE_ZFDJ.equals(payPriceType.toString())) {
				if (pra.getOrderState() != null
						&& Context.ORDER_PAYSTATUS_YZFDJ.equals(pra.getOrderState().toString())) {
					map.put("isSuccess", false);
					return map;
				}
				orderPay.setPayPriceType(Integer.parseInt(Context.ORDER_ORDERTYPE_ZFDJ));
				//如果财务占位订单，支付不改变订单状态
				if (Integer.parseInt(Context.ORDER_PAYSTATUS_CW) != pra.getOccupyType()) {
					pra.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_YZFDJ));
				}
			}

			// 如果是全款支付，则订单支付后状态为"已支付"，且支付订单状态改为"支付全款"
			else if (Context.ORDER_ORDERTYPE_ZFQK.equals(payPriceType.toString())) {
				if (pra.getOrderState() != null
						&& Context.ORDER_PAYSTATUS_YZF.equals(pra.getOrderState().toString())) {
					map.put("isSuccess", false);
					return map;
				}
				//如果财务占位订单，支付不改变订单状态
				if (Integer.parseInt(Context.ORDER_PAYSTATUS_CW) != pra.getOccupyType()) {
					pra.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_YZF));
				}
				orderPay.setPayPriceType(Integer.parseInt(Context.ORDER_ORDERTYPE_ZFQK));
			}

			// 如果是尾款支付，则订单支付后状态为"已支付"，且支付订单状态改为"支付尾款"
			else if (Context.ORDER_ORDERTYPE_ZFWK.equals(payPriceType.toString())) {
				//如果财务占位订单，支付不改变订单状态
				if (Integer.parseInt(Context.ORDER_PAYSTATUS_CW) != pra.getOccupyType()) {
					pra.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_YZF));
				}
				orderPay.setPayPriceType(Integer.parseInt(Context.ORDER_ORDERTYPE_ZFWK));
			}
			//订单支付方式 0 1月付
			Integer ps = orderPay.getPaymentStatus();
			pra.setPaymentStatus(ps);
			airticketOrderDao.saveOrUpdateAirticketOrderById(pra);
		}else{
			Integer ps = orderPay.getPaymentStatus();
			pra.setPaymentStatus(ps);
			airticketOrderDao.saveOrUpdateAirticketOrderById(pra);
		}

		map.put("isSuccess", true);
		return map;
	}

	/**
	 * 签证押金支付
	 * 
	 * @param orderPay
	 * @return
	 */
	private Map<String, Object> saveYajinPay(Orderpay orderPay, String visaId,
			BigDecimal dqzfprice, int currencyId) {
		Map<String, Object> map = new HashMap<String, Object>();
		Visa pra = visaDao.findOne(Long.valueOf(visaId));
		// 先保存orderpay流水表
		orderPay.setPayPriceType(Context.MONEY_TYPE_YSYJ);
		orderPay.setTravelerId(pra.getTravelerId());
		orderpayDao.save(orderPay);

		// 保存moneyamount金额表
		MoneyAmount newAmount = new MoneyAmount();
		newAmount.setAmount(dqzfprice);
		newAmount.setBusindessType(2);
		newAmount.setCurrencyId(currencyId);
		newAmount.setMoneyType(Context.MONEY_TYPE_YSYJ);
		newAmount.setOrderType(Integer.valueOf(Context.ORDER_STATUS_VISA));
		newAmount.setSerialNum(orderPay.getMoneySerialNum());
		newAmount.setUid(pra.getTravelerId());
		newAmount.setCreatedBy(UserUtils.getUser().getId());
		moneyAmountService.saveOrUpdateMoneyAmount(newAmount);

		// 判断visa表里面没有已付uuid 向moneyamount生成一条记录
		// 更新visa表中的已付uuid
		String moneyUuid = UUID.randomUUID().toString();
		if (null == pra.getPayedDeposit() || "".equals(pra.getPayedDeposit())) {
			visaDao.updateBySql("update visa set payed_deposit = '" + moneyUuid
					+ "' where id ='" + visaId + "'");

			// 保存moneyamount金额表
			MoneyAmount amount = new MoneyAmount();
			amount.setAmount(dqzfprice);
			amount.setBusindessType(2);
			amount.setCurrencyId(currencyId);
			amount.setMoneyType(Context.MONEY_TYPE_YSYJ);
			amount.setOrderType(Integer.valueOf(Context.ORDER_STATUS_VISA));
			amount.setSerialNum(moneyUuid);
			amount.setUid(pra.getTravelerId());
			amount.setCreatedBy(UserUtils.getUser().getId());
			moneyAmountService.saveOrUpdateMoneyAmount(amount);
			// visa表中有已付uuid,只变动money_amount表的金额
		} else {
			// 累计后的金额总数
			BigDecimal jine = BigDecimal.ZERO;
			String moneyId = "";
			// 根据已支付uuid找到之前支付的记录
			String sql = "select id,amount from money_amount where  serialNum = '"
					+ pra.getPayedDeposit() + "'";
			List<Map<String, Object>> resultList = visaDao.findBySql(sql,
					Map.class);
			// 累计计算金额
			for (Map<String, Object> listin : resultList) {
				if (null != listin.get("amount")
						&& !"".equals(listin.get("amount"))) {
					jine = dqzfprice.add((BigDecimal) listin.get("amount"));
				}
				moneyId = listin.get("id").toString();
			}
			// 累计后的金额更新到表中
			visaDao.updateBySql("update money_amount set amount = '" + jine
					+ "' where id ='" + moneyId + "'");
		}
		map.put("isSuccess", true);
		return map;
	}

	public List<Orderpay> findOrderPayOrderById(Long orderId, Integer orderType) {
		return orderpayDao.findOrderPayOrderById(orderId, orderType);
	}
	
	public List<Orderpay> findOrderPayOrderByUpdate(Long orderId, Integer orderType) {
		return orderpayDao.findOrderPayOrderByUpdate(orderId, orderType);
	}

	public List<Orderpay> findOrderPay(String orderNum,String orderPaySerialNum){
		return orderpayDao.findOrderpay(orderNum, orderPaySerialNum);
	}
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public List<Orderpay> findOrderpayByOrderId(Long orderId, Integer orderType) {
		return orderpayDao.findOrderpayByOrderId(orderId, orderType);
	}

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public Orderpay findOrderpayById(Long id) {
		return orderpayDao.findOne(id);
	}


	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void payedConfirm(Orderpay orderpay, Long[] args,List<MoneyAmount> list) throws Exception {
        
        if(orderpay.getTravelerId() !=null){
        	//签证订单收款确认  ;
        	//保存orderpay
        	//orderpayDao.save(orderpay); 
        	//更新MoneyAmount标的达帐状态
        	List<MoneyAmount> malist = moneyAmountDao.findAmountBySerialNum(orderpay.getMoneySerialNum());
            if(!malist.isEmpty()){
            	int size = malist.size();
            	for(int i=0;i<size;i++){
            		malist.get(i).setMoneyType(4);
            	}
            }
            
            //更新游客的达帐金额
            String uuid = UUID.randomUUID().toString();
        	Traveler traveler = travelerDao.findById(orderpay.getTravelerId());
        	
        	List<MoneyAmount> ta = moneyAmountService.addMoney(traveler.getAccountedMoney(), orderpay.getMoneySerialNum());
        	if(!ta.isEmpty()){
        		int tasize =ta.size();
        	    for(int num =0;num<tasize;num++){
        	    	ta.get(num).setSerialNum(uuid);
            		ta.get(num).setCreatedBy(UserUtils.getUser().getId());
            		ta.get(num).setBusindessType(2);
            		ta.get(num).setUid(orderpay.getOrderId());
            		ta.get(num).setMoneyType(Context.MONEY_TYPE_DZ);
            		ta.get(num).setOrderType(Context.ORDER_TYPE_QZ);;
        	    	
        	    }
        	    traveler.setAccountedMoney(uuid);
        	    moneyAmountService.saveOrUpdateMoneyAmounts(uuid, ta);
        	}
        	
        	//更新订单的达帐金额
        	VisaOrder vo = visaOrderDao.findOne(orderpay.getOrderId());
        	List<MoneyAmount> list1 =  moneyAmountService.addMoney(vo.getAccountedMoney(), orderpay.getMoneySerialNum());
        	if(!list1.isEmpty()){
	        	String sn= UUID.randomUUID().toString();
	        	for(int index=0;index<list1.size();index++){
	        		list1.get(index).setSerialNum(sn);
	        		list1.get(index).setCreatedBy(UserUtils.getUser().getId());
	        		list1.get(index).setBusindessType(1);
	        		list1.get(index).setUid(orderpay.getOrderId());
	        		list1.get(index).setMoneyType(Context.MONEY_TYPE_DZ);
	        		list1.get(index).setOrderType(Context.ORDER_TYPE_QZ);;
	        	}
	        	moneyAmountService.saveOrUpdateMoneyAmounts(sn, list1);
	        	vo.setAccountedMoney(sn);
	        	
        	}
        	
        }else{//订单收款确认
        	orderpayDao.save(orderpay);     
	        //获取订单的达帐金额和已付金额，money[0]达帐金额，money[1]已付金额
			String[] money = monryAmountService.getAccountedMoneyUid(
					orderpay.getOrderId(), orderpay.getOrderType().intValue());
			String accStr = money[0];  
			List<MoneyAmount> payedMoney = moneyAmountDao
					.findAmountBySerialNum(orderpay.getMoneySerialNum());
	
			saveDocInfoByPayUUID(orderpay.getMoneySerialNum(), args);
			if (payedMoney != null && payedMoney.size() > 0) {
				for (int i = 0; i < payedMoney.size(); i++) {
	
					MoneyAmount payed = payedMoney.get(i);
					List<MoneyAmount> result = moneyAmountDao
							.findAmountBySerialNumAndCurrencyId(accStr,
									payed.getCurrencyId());
					if (result != null && result.size() > 0) {
						result.get(0).setAmount(
								result.get(0).getAmount().add(payed.getAmount()));
						result.get(0).setMoneyType(Context.MONEY_TYPE_DZ);
					} else {
						MoneyAmount accountedMoney = new MoneyAmount();
						accountedMoney.setSerialNum(accStr);
						accountedMoney.setMoneyType(Context.MONEY_TYPE_DZ);
						accountedMoney.setAmount(payed.getAmount());
						accountedMoney.setBusindessType(payed.getBusindessType());
						accountedMoney.setCreateTime(new Date());
						accountedMoney.setCreatedBy(UserUtils.getUser().getId());
						accountedMoney.setCurrencyId(payed.getCurrencyId());
						accountedMoney.setOrderType(payed.getOrderType());
						accountedMoney.setUid(payed.getUid());
	
						moneyAmountService.saveOrUpdateMoneyAmount(accountedMoney);
					}
				}
			}
			
			// 如果是单团类订单或机票且订单状态为待财务确认，则需要更改订单状态且扣减余位
			changeOrder(orderpay);
			// 订单跟踪记录添加
			if (orderpay.getOrderType() == 2 && orderpay.getOrderId() != null) {
				progressService.addPayInfoTime(orderService.getProductorderById(orderpay.getOrderId()), false);
			}
        }
	}
	
	/**
	 * 散拼订单添加预报名订单门店返还差额
	 * @param payedMoney
	 * @param returnDifference
	 * @param moneyAmounts
	 * @author yakun.bai
	 * @Date 2016-10-26
	 */
	public void differenceMoneyAddToAccountedMoney(String accountedMoney, ReturnDifference returnDifference) {
		
		if (StringUtils.isNotBlank(accountedMoney) && null != returnDifference) {
			
			MoneyAmount moneyAmount = new MoneyAmount();
			// 流水号UUID
			moneyAmount.setSerialNum(accountedMoney);
			// 币种ID
			moneyAmount.setCurrencyId(returnDifference.getCurrencyId());
			// 金额
			moneyAmount.setAmount(returnDifference.getReturnPrice());
			// 订单ID或游客ID
			moneyAmount.setUid(returnDifference.getOrderId().longValue());
			// 订单产品类型
			moneyAmount.setOrderType(2);
			// 业务类型(1表示订单，2表示游客)
			moneyAmount.setBusindessType(1);
			// 记录人ID
			moneyAmount.setCreatedBy(UserUtils.getUser().getId());
			
			moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount, accountedMoney, "add", 5);
		}
		
	}
	/**
	 * 财务订单收款撤销 减差额
	 * @param payedMoney
	 * @param returnDifference
	 * @param moneyAmounts
	 * @author yakun.bai
	 * @Date 2016-10-26
	 */
	public void differenceMoneySubToAccountedMoney(String accountedMoney, ReturnDifference returnDifference) {
		
		if (StringUtils.isNotBlank(accountedMoney) && null != returnDifference) {
			
			MoneyAmount moneyAmount = new MoneyAmount();
			// 流水号UUID
			moneyAmount.setSerialNum(accountedMoney);
			// 币种ID
			moneyAmount.setCurrencyId(returnDifference.getCurrencyId());
			// 金额
			moneyAmount.setAmount(returnDifference.getReturnPrice());
			// 订单ID或游客ID
			moneyAmount.setUid(returnDifference.getOrderId().longValue());
			// 订单产品类型
			moneyAmount.setOrderType(2);
			// 业务类型(1表示订单，2表示游客)
			moneyAmount.setBusindessType(1);
			// 记录人ID
			moneyAmount.setCreatedBy(UserUtils.getUser().getId());
			
			moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount, accountedMoney, "subtract", 5);
		}
		
	}
	
	
	/**
	 * @Description 单团类和机票订单财务确认付款，需扣减余位
	 * @author yakun.bai
	 * @Date 2015-12-18
	 */
	private void changeOrder(Orderpay orderpay) throws Exception {
		
		// 只有单团类和机票订单才需要在财务确认占位的时候扣减余位
		Integer orderType = orderpay.getOrderType();
		if (orderType != null 
				&& (Context.ORDER_TYPE_DT == orderType 
						|| Context.ORDER_TYPE_SP == orderType
						|| Context.ORDER_TYPE_YX == orderType
						|| Context.ORDER_TYPE_DKH == orderType
						|| Context.ORDER_TYPE_ZYX == orderType
						|| Context.ORDER_TYPE_JP == orderType
						|| Context.ORDER_TYPE_CRUISE == orderType)) {
			
			// 机票订单财务确认占位扣减余位
			if (Context.ORDER_TYPE_JP == orderType) {
				AirticketOrder order = airticketOrderDao.getAirticketOrderById(orderpay.getOrderId());
				ActivityAirTicket activity = activityAirTicketDao.getById(order.getAirticketId());
				Integer orderStatus = order.getOrderState();
				if (Integer.parseInt(Context.ORDER_PAYSTATUS_CW) == orderStatus) {
					
					airticketOrderStockService.changeGroupFreeNum(activity, order, null, Context.StockOpType.CW_CONFIRM);
					
					if (Integer.parseInt(Context.ORDER_ORDERTYPE_ZFDJ) == orderpay.getPayPriceType()) {
						order.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_YZFDJ));
					} else if (Integer.parseInt(Context.ORDER_ORDERTYPE_ZFQK) == orderpay.getPayPriceType()
							|| Integer.parseInt(Context.ORDER_ORDERTYPE_ZFWK) == orderpay.getPayPriceType()) {
						order.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_YZF));
					}
				} else if (Integer.parseInt(Context.ORDER_PAYSTATUS_YZFDJ) == orderStatus) {
					if (Integer.parseInt(Context.ORDER_ORDERTYPE_ZFQK) == orderpay.getPayPriceType()
							|| Integer.parseInt(Context.ORDER_ORDERTYPE_ZFWK) == orderpay.getPayPriceType()) {
						order.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_YZF));
					}
				}
				airticketOrderDao.saveOrUpdateAirticketOrderById(order);
			} else {
				ProductOrderCommon order = orderService.getProductorderById(orderpay.getOrderId());
				Integer payStatus = order.getPayStatus();
				if (Integer.parseInt(Context.ORDER_PAYSTATUS_CW) == payStatus) {
					
					orderStockService.changeGroupFreeNum(order, null, Context.StockOpType.CW_CONFIRM);
					
					if (Integer.parseInt(Context.ORDER_ORDERTYPE_ZFDJ) == orderpay.getPayPriceType()) {
						order.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_YZFDJ));
					} else if (Integer.parseInt(Context.ORDER_ORDERTYPE_ZFQK) == orderpay.getPayPriceType()
							|| Integer.parseInt(Context.ORDER_ORDERTYPE_ZFWK) == orderpay.getPayPriceType()) {
						order.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_YZF));
					}
				} else if (Integer.parseInt(Context.ORDER_PAYSTATUS_YZFDJ) == payStatus) {
					if (Integer.parseInt(Context.ORDER_ORDERTYPE_ZFQK) == orderpay.getPayPriceType()
							|| Integer.parseInt(Context.ORDER_ORDERTYPE_ZFWK) == orderpay.getPayPriceType()) {
						order.setPayStatus(Integer.parseInt(Context.ORDER_PAYSTATUS_YZF));
					}
				}
				orderService.saveProductorder(order);
				
				//-------by------junhao.zhao-----2017-03-22-----向表order_data_statistics中添加数据---开始-------------------------------------------------------------
				// “财务确认占位”确认之后加到表order_data_statistics中。
				if (orderDateSaveOrUpdateService.whetherAddOrUpdate(order)) {
					orderDateSaveOrUpdateService.addOrderDataStatistics(order);
				}
				//-------by------junhao.zhao-----2017-03-22-----向表order_data_statistics中添加数据---结束-------------------------------------------------------------
			}
		}
	}
	
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void payedConfirmForVisaOrder(List<Orderpay> orderpayList, Long[] args) {
        if(CollectionUtils.isNotEmpty(orderpayList)){
        	int orderpayListSize = orderpayList.size();
        	Orderpay orderpay = null;
        	for(int opsize=0;opsize<orderpayListSize;opsize++ ){
        		orderpay=orderpayList.get(opsize);
        	//签证订单收款确认  ;
        	//保存orderpay
        	orderpayDao.save(orderpay); 
        	//更新MoneyAmount标的达帐状态
        	List<MoneyAmount> malist = moneyAmountDao.findAmountBySerialNum(orderpay.getMoneySerialNum());
            if(!malist.isEmpty()){
            	int size = malist.size();
            	for(int i=0;i<size;i++){
            		malist.get(i).setMoneyType(4);
            	}
            }
            
            //更新游客的达帐金额
            String uuid = UUID.randomUUID().toString();
        	Traveler traveler = travelerDao.findById(orderpay.getTravelerId());
        	
        	List<MoneyAmount> ta = moneyAmountService.addMoney(traveler.getAccountedMoney(), orderpay.getMoneySerialNum());
        	if(!ta.isEmpty()){
        		int tasize =ta.size();
        	    for(int num =0;num<tasize;num++){
        	    	ta.get(num).setSerialNum(uuid);
            		ta.get(num).setCreatedBy(UserUtils.getUser().getId());
            		ta.get(num).setBusindessType(2);
            		ta.get(num).setUid(orderpay.getOrderId());
            		ta.get(num).setMoneyType(Context.MONEY_TYPE_DZ);
            		ta.get(num).setOrderType(Context.ORDER_TYPE_QZ);;
        	    	
        	    }
        	    traveler.setAccountedMoney(uuid);
        	    moneyAmountService.saveOrUpdateMoneyAmounts(uuid, ta);
        	}
        	
        	//更新订单的达帐金额
        	VisaOrder vo = visaOrderDao.findOne(orderpay.getOrderId());
        	List<MoneyAmount> list1 =  moneyAmountService.addMoney(vo.getAccountedMoney(), orderpay.getMoneySerialNum());
        	if(!list1.isEmpty()){
	        	String sn= UUID.randomUUID().toString();
	        	for(int index=0;index<list1.size();index++){
	        		list1.get(index).setSerialNum(sn);
	        		list1.get(index).setCreatedBy(UserUtils.getUser().getId());
	        		list1.get(index).setBusindessType(1);
	        		list1.get(index).setUid(orderpay.getOrderId());
	        		list1.get(index).setMoneyType(Context.MONEY_TYPE_DZ);
	        		list1.get(index).setOrderType(Context.ORDER_TYPE_QZ);;
	        	}
	        	moneyAmountService.saveOrUpdateMoneyAmounts(sn, list1);
	        	vo.setAccountedMoney(sn);
	     	
        }
        }}
	}
	
	/**
	 * 海岛游订单支付确认
	*<p>Title: payedConfirmForIsland</p>
	* @return void 返回类型
	* @author majiancheng
	* @date 2015-6-16 下午8:58:20
	* @throws
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void payedConfirmForIsland(PayProductOrder payProductOrder, Long[] args, Integer orderType, List<IslandMoneyAmount> amounts) {
        
		// 订单收款确认
		// 获取订单的达帐金额和已付金额，money[0]达帐金额，money[1]已付金额
		String[] money = monryAmountService.getAccountedMoneyUid(payProductOrder.getOrderUuid(), orderType);
		String accStr = money[0];
		List<IslandMoneyAmount> payedMoney = islandMoneyAmountService.getMoneyAmonutBySerialNum(payProductOrder.getMoneySerialNum());
		
		saveDocInfoByPayIslandUuid(payProductOrder.getMoneySerialNum(), args);
		
		if (payedMoney != null && payedMoney.size() > 0) {
			for (int i = 0; i < payedMoney.size(); i++) {
				IslandMoneyAmount payed = payedMoney.get(i);
				List<IslandMoneyAmount> result = islandMoneyAmountService.findAmountBySerialNumAndCurrencyId(accStr, payed.getCurrencyId());
				
				if (result != null && result.size() > 0) {
					IslandMoneyAmount firstMoneyAmount = result.get(0);
					firstMoneyAmount.setAmount(firstMoneyAmount.getAmount() + payed.getAmount());
					firstMoneyAmount.setMoneyType(Context.MONEY_TYPE_DZ);
					islandMoneyAmountService.update(firstMoneyAmount);
					
				} else {
					IslandMoneyAmount accountedMoney = new IslandMoneyAmount();
					accountedMoney.setSerialNum(accStr);
					accountedMoney.setMoneyType(Context.MONEY_TYPE_DZ);
					accountedMoney.setAmount(payed.getAmount());
					accountedMoney.setBusinessType(payed.getBusinessType());
					accountedMoney.setCurrencyId(payed.getCurrencyId());
					accountedMoney.setBusinessUuid(payed.getBusinessUuid());
					accountedMoney.setExchangerate(payed.getExchangerate());

					islandMoneyAmountService.save(accountedMoney);
				}
			}
		}
	}
	
	/**
	 * 酒店订单支付确认
	*<p>Title: payedConfirmForHotel</p>
	* @return void 返回类型
	* @author majiancheng
	* @date 2015-6-18 下午3:24:24
	* @throws
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void payedConfirmForHotel(PayProductOrder payProductOrder, Long[] args, Integer orderType, List<HotelMoneyAmount> amounts) {
        
		// 订单收款确认
		// 获取订单的达帐金额和已付金额，money[0]达帐金额，money[1]已付金额
		String[] money = monryAmountService.getAccountedMoneyUid(payProductOrder.getOrderUuid(), orderType);
		String accStr = money[0];
		List<HotelMoneyAmount> payedMoney = hotelMoneyAmountService.getMoneyAmonutBySerialNum(payProductOrder.getMoneySerialNum());
		
		saveDocInfoByPayIslandUuid(payProductOrder.getMoneySerialNum(), args);
		
		if (payedMoney != null && payedMoney.size() > 0) {
			for (int i = 0; i < payedMoney.size(); i++) {
				HotelMoneyAmount payed = payedMoney.get(i);
				List<HotelMoneyAmount> result = hotelMoneyAmountService.findAmountBySerialNumAndCurrencyId(accStr, payed.getCurrencyId());
				
				if (result != null && result.size() > 0) {
					HotelMoneyAmount firstMoneyAmount = result.get(0);
					firstMoneyAmount.setAmount(firstMoneyAmount.getAmount() + payed.getAmount());
					firstMoneyAmount.setMoneyType(Context.MONEY_TYPE_DZ);
					hotelMoneyAmountService.update(firstMoneyAmount);
					
				} else {
					HotelMoneyAmount accountedMoney = new HotelMoneyAmount();
					accountedMoney.setSerialNum(accStr);
					accountedMoney.setMoneyType(Context.MONEY_TYPE_DZ);
					accountedMoney.setAmount(payed.getAmount());
					accountedMoney.setBusinessType(payed.getBusinessType());
					accountedMoney.setCurrencyId(payed.getCurrencyId());
					accountedMoney.setBusinessUuid(payed.getBusinessUuid());
					accountedMoney.setExchangerate(payed.getExchangerate());

					hotelMoneyAmountService.save(accountedMoney);
				}
			}
		}
	}
	
	
	/**获取签证押金订单的达帐金额和已付金额
	 * @param travelerid  游客ID
	 * @return money   
	 * @author haiming.zhao
	 * 
	 * */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	private String[] getVisaMoney(Long travelerId){
		Visa visa = visaDao.findByTravelerId(travelerId);
		String accountedMoney = visa.getAccountedDeposit();
		String payedMoney = visa.getPayedDeposit();
		
		if(StringUtils.isEmpty(accountedMoney)){
			accountedMoney = UUID.randomUUID().toString();
			visa.setAccountedDeposit(accountedMoney);
			visaDao.getSession().update(visa);
		}
		String[] money = {accountedMoney,payedMoney};
		return money;
	}
	
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void payedConfirmForVisa(Orderpay orderpay, Long[] args,List<MoneyAmount> list) {
        
            //订单收款确认
        	orderpayDao.save(orderpay);     
	        //获取订单的达帐金额和已付金额，money[0]达帐金额，money[1]已付金额
			String[] money = getVisaMoney(orderpay.getTravelerId());
			String accStr = money[0];  
			List<MoneyAmount> payedMoney = moneyAmountDao
					.findAmountBySerialNum(orderpay.getMoneySerialNum());
	
			saveDocInfoByPayUUID(orderpay.getMoneySerialNum(), args);
			if (payedMoney != null && payedMoney.size() > 0) {
				for (int i = 0; i < payedMoney.size(); i++) {
	
					MoneyAmount payed = payedMoney.get(i);
					List<MoneyAmount> result = moneyAmountDao
							.findAmountBySerialNumAndCurrencyId(accStr,
									payed.getCurrencyId());
					if (result != null && result.size() > 0) {
						result.get(0).setAmount(
								result.get(0).getAmount().add(payed.getAmount()));
					} else {
						MoneyAmount accountedMoney = new MoneyAmount();
						accountedMoney.setSerialNum(accStr);
						accountedMoney.setMoneyType(4);
						accountedMoney.setAmount(payed.getAmount());
						accountedMoney.setBusindessType(payed.getBusindessType());
						accountedMoney.setCreateTime(new Date());
						accountedMoney.setCreatedBy(UserUtils.getUser().getId());
						accountedMoney.setCurrencyId(payed.getCurrencyId());
						accountedMoney.setOrderType(payed.getOrderType());
						accountedMoney.setUid(payed.getUid());
	
						moneyAmountService.saveOrUpdateMoneyAmount(accountedMoney);
					}
				}
		}
        
	}
	
	public Map<String, String> cancelVisaOrderOper(String payId) {
		Map<String, String> data = new HashMap<String, String>();

		try {
			Orderpay orderpay = orderpayDao.findOne(Long.parseLong(payId));

			// 游客
			Long travelerId = orderpay.getTravelerId();
			Traveler traveler = travelerDao.findOne(travelerId);
			String travelerAccountedMoney = traveler.getAccountedMoney();
			
			List<MoneyAmount> moneyAmountList = moneyAmountService
					.decreaseMoney(travelerAccountedMoney, orderpay.getMoneySerialNum());
			moneyAmountService.updateMoneyAmount(travelerAccountedMoney,
					moneyAmountList);

			// visaorder
			VisaOrder vo = visaOrderDao.findOne(orderpay.getOrderId());
			String orderAccountedMoney = vo.getAccountedMoney();
			moneyAmountList = moneyAmountService
					.decreaseMoney(orderAccountedMoney, orderpay.getMoneySerialNum());
			moneyAmountService.updateMoneyAmount(orderAccountedMoney,
					moneyAmountList);
		} catch (Exception e) {
			data.put("flag", "error");
			return data;
		}
		data.put("flag", "ok");
		return data;
	}
	
	public Map<String, String> cancelVisaOper(String payId) {
		Map<String, String> data = new HashMap<String, String>();

		try {
			Orderpay orderpay = orderpayDao.findOne(Long.parseLong(payId));
			String moneySerialNum = orderpay.getMoneySerialNum();

			// visa
			Visa visa = visaDao.findByTravelerId(orderpay.getTravelerId());
			String visaAccountedMoney = visa.getAccountedDeposit();

			List<MoneyAmount> moneyAmountList = moneyAmountService
					.decreaseMoney(visaAccountedMoney, moneySerialNum);

			moneyAmountService.updateMoneyAmount(visaAccountedMoney,
					moneyAmountList);
		} catch (Exception e) {
			data.put("flag", "error");
			return data;
		}
		data.put("flag", "ok");
		return data;
	}
	/**
	 * 组装收款确认弹出页orderpay参数
	 * @param list
	 * @param payType
	 * @param num
	 * */
	public Orderpay copyValue2OrderPay(List<Orderpay> list,String payType,int num){
		Orderpay orderPay = new Orderpay();
		if(CollectionUtils.isNotEmpty(list)){
			List<String> m = new ArrayList<String>();
			for(int i=0;i<list.size();i++){
				m.add(list.get(i).getMoneySerialNum());
			}
			String money = OrderCommonUtil.getMoneyAmountBySerialNum(m,2);
			if(StringUtils.isNotBlank(payType) && num==1){
				if("3".equals(payType)){ //3 现金支付
					orderPay.setPayerName(list.get(0).getPayerName());
					orderPay.setAccountDate(list.get(0).getAccountDate());
				}else if("1".equals(payType)){//1 支票
					orderPay.setPayerName(list.get(0).getPayerName());
					orderPay.setAccountDate(list.get(0).getAccountDate());
					orderPay.setCheckNumber(list.get(0).getCheckNumber());
					orderPay.setInvoiceDate(list.get(0).getInvoiceDate());
				}else if("4".equals(payType)){// 4 汇款
					orderPay.setPayerName(list.get(0).getPayerName());
					orderPay.setAccountDate(list.get(0).getAccountDate());
					orderPay.setBankName(list.get(0).getBankName());
					orderPay.setBankAccount(list.get(0).getBankAccount());
					orderPay.setToBankNname(list.get(0).getToBankNname());
					orderPay.setToBankAccount(list.get(0).getToBankAccount());
				}else if("6".equals(payType)){//6银行转账
					orderPay.setPayerName(list.get(0).getPayerName());
					orderPay.setAccountDate(list.get(0).getAccountDate());
					orderPay.setBankName(list.get(0).getBankName());
					orderPay.setBankAccount(list.get(0).getBankAccount());
					orderPay.setToBankNname(list.get(0).getToBankNname());
					orderPay.setToBankAccount(list.get(0).getToBankAccount());
				}else if("7".equals(payType)){//6银行转账
					orderPay.setPayerName(list.get(0).getPayerName());
					orderPay.setDraftAccountedDate(list.get(0).getDraftAccountedDate());
					orderPay.setBankName(list.get(0).getBankName());
					orderPay.setBankAccount(list.get(0).getBankAccount());
					orderPay.setToBankNname(list.get(0).getToBankNname());
					orderPay.setToBankAccount(list.get(0).getToBankAccount());
				}else if("8".equals(payType)){
					orderPay.setPayerName(list.get(0).getPayerName());
					orderPay.setAccountDate(list.get(0).getAccountDate());
					orderPay.setBankName(list.get(0).getBankName());
					orderPay.setBankAccount(list.get(0).getBankAccount());
				}else if(Context.PAYTYPE_ALIPAY.equals(payType)) {//224-因公支付宝
					orderPay.setPayerName(list.get(0).getPayerName());
					orderPay.setAccountDate(list.get(0).getAccountDate());
					orderPay.setFromAlipayName(list.get(0).getFromAlipayName());
					orderPay.setFromAlipayAccount(list.get(0).getFromAlipayAccount());
					orderPay.setToAlipayName(list.get(0).getToAlipayName());
					orderPay.setToAlipayAccount(list.get(0).getToAlipayAccount());
					orderPay.setComeOfficeName(list.get(0).getComeOfficeName());
				}
			}
			orderPay.setMoneyAmount(money);
			orderPay.setPayType(Integer.valueOf(payType));
			orderPay.setOrderType(6);
			orderPay.setOrderNum(list.get(0).getOrderNum());
			orderPay.setOrderPaySerialNum(list.get(0).getOrderPaySerialNum());
			orderPay.setRemarks(list.get(0).getRemarks());
			orderPay.setCreateDate(list.get(0).getCreateDate());
		}
		return orderPay;
	}
    /**判断属性值是否有不一样的*/
	public  int foreach(List list,String[] col){
		int num=0;
		if(list.size()>0 && col.length>0){
			for(int i =0;i<col.length;i++){
				num = method(list,col[i]);
				if(num>1){
					break;
				}
			}
		}
		return num;
	}
	
	private  int method(List<Object> obj,String filed){
		int size = obj.size();
		Set<Object> set = new HashSet<Object>();
		try{
			for(int i=0;i<size;i++){
				Class clazz= obj.get(i).getClass();
				PropertyDescriptor pd = new PropertyDescriptor(filed, clazz);  
		        Method getMethod = pd.getReadMethod();//获得get方法 
		        if(pd !=null){
		        	Object o = getMethod.invoke(obj.get(i));
		        	set.add(o);
		        }
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return set.size();
	}
	
	/**
	 * 撤销订单收款（财务模块）->修改达帐金额
	 * @Title: cancelOrderReceive
	 * @return boolean
	 * @author majiancheng
	 * @date 2015-11-4 下午8:18:39
	 */
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public boolean cancelOrderReceive(Integer orderpayId, Integer orderId, int orderType) {
		boolean flag = true;
		
		if(orderpayId == null || orderId == null) {
			return false;
		}
		
		//更新付款表状态（变为默认状态）
		Orderpay orderpay = orderpayDao.getById(orderpayId.longValue());
		orderpay.setIsAsAccount(Orderpay.ISASACCOUNT_DEFAULT);
		orderpay.setUpdateBy(UserUtils.getUser());
		orderpay.setUpdateDate(new Date());
		orderpayDao.save(orderpay);
		
		String payedMoneySerialNum = orderpay.getMoneySerialNum();
		String accountedMoneySerialNum = "";
		//获取订单达帐金额serialNum
		if(Context.ORDER_TYPE_JP == orderType) {
			AirticketOrder airticketOrder = airticketOrderDao.getAirticketOrderById(orderId.longValue());
			accountedMoneySerialNum = airticketOrder.getAccountedMoney();
		}

		if(StringUtils.isEmpty(accountedMoneySerialNum)) {
			return false;
		}
		if(UserUtils.isMtourUser()){
			flag = moneyAmountService.subtractMoneyAmountForMtour(accountedMoneySerialNum, payedMoneySerialNum, Context.MONEY_TYPE_CANCEL);
		}else{
			flag = moneyAmountService.subtractMoneyAmount(accountedMoneySerialNum, payedMoneySerialNum);
		}
		return flag;
	}
	
	/**
	 * 撤销订单收款（订单模块）->修改已付金额
		 * @Title: cancelOrderReceive
	     * @return boolean
	     * @author majiancheng       
	     * @date 2015-11-4 下午8:18:39
	 */
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public boolean orderReceiptCancel(Integer orderpayId, Integer orderId, Integer orderType) {
		boolean flag = true;
		
		AirticketOrder order = airticketOrderDao.getAirticketOrderById(Long
				.valueOf(orderId));
		Orderpay orderpay = orderpayDao.getOrderPayById(Long
				.valueOf(orderpayId));
		if(UserUtils.isMtourUser()){
			flag = moneyAmountService.subtractMoneyAmountForMtour(order.getPayedMoney(),
					orderpay.getMoneySerialNum(), Context.MONEY_TYPE_CANCEL);
		}else{
			flag = moneyAmountService.subtractMoneyAmount(order.getPayedMoney(),
					orderpay.getMoneySerialNum(), Context.MONEY_TYPE_CANCEL);
		}
				
		return flag;
	}
	/**
	 * 
	 * @param orderId
	 * @param orderType
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public List<Orderpay> findOrderpayByOrderIdAndOrderType(Long orderId,Integer orderType){
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT * From orderpay WHERE orderId=? AND orderType=? AND delFlag=? AND (isAsAccount is  null OR isAsAccount IN (1,2)) ");
		List<Orderpay> list = orderpayDao.findBySql(sbf.toString(),Orderpay.class,orderId,orderType,0);
		return list;
	}

	/**
	 * 根据订单付款序列号查询所有游客姓名。目前用于签证详情和修改页面的付款信息栏显示所有的付款游客名称。
	 * @param orderPaySerialNum
	 * @return
	 * @author yudong.xu 2016.10.11
	 */
	public String getOrderPayTravelerNames(String orderPaySerialNum) {
		return orderpayDao.getOrderPayTravelerNames(orderPaySerialNum);
	}
	
	/**
	 * 根据订单id和类型查找订单记录（将签证收押金从group by中拆出）
	 * @param orderId 订单id
	 * @param orderType 订单类型
	 * @author yang.wang
	 * @date 2016.10.12
	 * */
	public List<Orderpay> getOrderPayByOrderIdAndOrderType(Long orderId, Integer orderType) {
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT * FROM ( ")
		      .append(" SELECT * FROM orderpay WHERE orderPaySerialNum IS NOT NULL AND payPriceType NOT IN (7, 16) GROUP BY orderPaySerialNum ")
		      .append(" UNION ")
		      .append(" SELECT * FROM orderpay WHERE payPriceType IN (7, 16)) temp ")
		      .append(" WHERE temp.orderId = ").append(orderId)
		      .append(" AND temp.orderType = ").append(orderType)
		      .append(" AND temp.delFlag = '0' ORDER BY temp.id DESC");
		
		return orderpayDao.findBySql(buffer.toString(), Orderpay.class);
		
	}
}
