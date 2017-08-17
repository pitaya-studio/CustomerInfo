package com.trekiz.admin.modules.order.service;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.hotel.service.HotelMoneyAmountService;
import com.trekiz.admin.modules.island.service.IslandMoneyAmountService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.entity.Pay;
import com.trekiz.admin.modules.order.entity.PayCheck;
import com.trekiz.admin.modules.order.entity.PayConvert;
import com.trekiz.admin.modules.order.entity.PayRemittance;
import com.trekiz.admin.modules.order.entity.PlatBankInfo;
import com.trekiz.admin.modules.order.entity.Refund;
import com.trekiz.admin.modules.order.formBean.OrderPayForm;
import com.trekiz.admin.modules.order.pojo.OrderPayDetail;
import com.trekiz.admin.modules.order.pojo.OrderPayInput;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.repository.OrderpayDao;
import com.trekiz.admin.modules.order.repository.PayCheckDao;
import com.trekiz.admin.modules.order.repository.PayDao;
import com.trekiz.admin.modules.order.repository.PayRemittanceDao;
import com.trekiz.admin.modules.order.repository.PlatBankInfoDao;
import com.trekiz.admin.modules.order.repository.RefundDao;
import com.trekiz.admin.modules.pay.dao.PayAlipayDao;
import com.trekiz.admin.modules.pay.dao.PayBanktransferDao;
import com.trekiz.admin.modules.pay.dao.PayDraftDao;
import com.trekiz.admin.modules.pay.dao.PayFeeDao;
import com.trekiz.admin.modules.pay.dao.PayPosDao;
import com.trekiz.admin.modules.pay.entity.PayAlipay;
import com.trekiz.admin.modules.pay.entity.PayBanktransfer;
import com.trekiz.admin.modules.pay.entity.PayDraft;
import com.trekiz.admin.modules.pay.entity.PayFee;
import com.trekiz.admin.modules.pay.entity.PayGroup;
import com.trekiz.admin.modules.pay.entity.PayPos;
import com.trekiz.admin.modules.pay.model.ProductMoneyAmount;
import com.trekiz.admin.modules.pay.service.PayGroupService;
import com.trekiz.admin.modules.pay.service.PayHotelOrderService;
import com.trekiz.admin.modules.pay.service.PayIslandOrderService;
import com.trekiz.admin.modules.pay.service.ProductMoneyAmountService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.sys.repository.ZhifubaoDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;

/**
 * 订单公共付款(批量支付，当然一件也应该是可以的)
 */
@Service
@Transactional(readOnly = true)
public class OrderPayMoreService extends BaseService {

	@Autowired
	private OrderCommonService orderService;
	@Autowired
	private OrderpayDao orderpayDao;
	@Autowired
	private MoneyAmountDao moneyAmountDao;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private PayDao payDao;
	@Autowired
	private RefundDao refundDao;
	@Autowired
	private PayCheckDao payCheckDao;
	@Autowired
	private PayRemittanceDao payRemittanceDao;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private TravelerDao travelerDao;
	@Autowired
	private DocInfoDao docInfoDao;
	@Autowired
	private DocInfoService docInfoService;
	@Autowired
	private PlatBankInfoService platBankInfoService;
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
	private PayConvertService payConvertService;
	@Autowired
	private PayGroupService payGroupService;
	@Autowired
	private PayIslandOrderService payIslandOrderService;
	@Autowired
	private PayHotelOrderService payHotelOrderService;
	@Autowired
	private IslandMoneyAmountService islandMoneyAmountService;
	@Autowired
	private HotelMoneyAmountService hotelMoneyAmountService;
	@Autowired
	private ProductMoneyAmountService productMoneyAmountService;
	@Autowired
	private PayBanktransferDao payBanktransferDao;
	@Autowired
	private PayDraftDao payDraftDao;
	@Autowired
	private PayPosDao payPosDao;
	@Autowired
	private PayAlipayDao payAlipayDao;
	@Autowired
	private PlatBankInfoDao platBankInfoDao;
	@Autowired
	private PayFeeDao payFeeDao;
	@Autowired
	private CostRecordDao costRecordDao;
	@Autowired
	private ZhifubaoDao zhifubaoDao;
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * 支付页面初始化处理部分
	 */
	// ////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 检查是否为正确的金额
	 * 
	 * @param moneyStr
	 * @return
	 */
	public boolean validateMoney(String moneyStr) {
		String[] moneys = moneyStr.split(",");
		for (String money : moneys) {
			try {
				new BigDecimal(money);
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 取得收款/付款的描述文字
	 * 
	 * @param payType
	 * @return
	 */
	public String getPayTypeDesc(String payType) {
		if ("1".equals(payType)) {
			return "收款";
		} else if ("2".equals(payType)) {
			return "付款";
		} else if("3".equals(payType)) {
			return "收款";
		}

		return "";
	}
	
	public String getPayOrderTitle(String payType) {
		if ("1".equals(payType)) {
			return "订单管理-订单收款";
		} else if ("2".equals(payType)) {
			return "结算管理-付款";
		} else if("3".equals(payType)) {
			return "运控-成本录入-其他收入收款";
		}

		return "订单管理-订单收款";
	}

	/**
	 * 取得月结/后付费的状态
	 * 
	 * @param agentId
	 * @return 1 即时结算 2 按月结算 3 担保结算 4 后续费
	 */
	public Integer getAgentPaymentType(Integer agentId) {
		if (agentId == null || agentId.intValue() <= 0) {
			return 1;
		}
		Agentinfo agentinfo = agentinfoService.findAgentInfoById(Long
				.parseLong(agentId + ""));

		if (agentinfo == null) {
			return 1;
		}

		String paymentType = agentinfo.getPaymentType();
		if ("1299".equals(paymentType)) {// 即时结算
			return 1;
		} else if ("1300".equals(paymentType)) {// 按月结算
			return 2;
		} else if ("1301".equals(paymentType)) {// 后续费
			return 3;
		} else if ("1302".equals(paymentType)) {// 后续费
			return 4;
		}
		return 1;
	}

	/**
	 * 取得来款单位名称
	 * 
	 * @param agentId
	 * @return
	 */
	public String getPayerName(Integer agentId) {
		if (agentId == null || agentId.intValue() == -1) {
			return "";
		}

		Agentinfo agentinfo = agentinfoService.findAgentInfoById(Long
				.parseLong(agentId + ""));

		if (agentinfo == null) {
			return "";
		}

		return agentinfo.getAgentName();
	}

	/**
	 * 转换成所属平台类型
	 * 
	 * @param supplyType
	 * @return
	 */
	private Integer convertSupplyType2PlatType(Integer supplyType) {
		// 0: 地接社,1:渠道商
		// 1:地接社,2:渠道商
		switch (supplyType) {
		case 0:
			return 1;
		case 1:
			return 2;
		}

		return 2;
	}

	/**
	 * 取得来款行的信息ID
	 * 
	 * @param orderPayInput
	 * @return
	 */
	public String getFmBelongPlatId(OrderPayInput orderPayInput) {
		String payType = orderPayInput.getPayType();
		Integer agentId = orderPayInput.getAgentId();

		if ("1".equals(payType) || "3".equals(payType)) {
			return agentId.toString();
		} else if ("2".equals(payType)) {
			Long supplierId = UserUtils.getUser().getCompany().getId();
			return supplierId.toString();
		}

		return "-1";
	}

	/**
	 * 取得收款行的信息ID
	 * 
	 * @param orderPayInput
	 * @return
	 */
	public String getToBelongPlatId(OrderPayInput orderPayInput) {
		String payType = orderPayInput.getPayType();
		Integer supplyType = orderPayInput.getSupplyType();
		supplyType = this.convertSupplyType2PlatType(supplyType);
		Integer agentId = orderPayInput.getAgentId();

		if ("1".equals(payType) || "3".equals(payType)) {
			Long supplierId = UserUtils.getUser().getCompany().getId();
			return supplierId.toString();
		} else if ("2".equals(payType)) {
			return agentId.toString();
		}

		return "-1";
	}

	/**
	 * 取得来款行的银行信息
	 * 
	 * @param orderPayInput
	 * @return
	 */
	public List<String> getFmBankInfo(OrderPayInput orderPayInput) {

		String payType = orderPayInput.getPayType();
		Integer supplyType = orderPayInput.getSupplyType();
		supplyType = this.convertSupplyType2PlatType(supplyType);
		Integer agentId = orderPayInput.getAgentId();

		List<String> bankInfoList = new ArrayList<String>();
		if (agentId == null) { //bug14069  原条件为 agentId == null || agentId == -1（非签约渠道）
			return bankInfoList;
		}

		if ("1".equals(payType) || "3".equals(payType)) {
			bankInfoList = getBankInfo(supplyType, agentId);
		} else if ("2".equals(payType)) {
			Long supplierId = UserUtils.getUser().getCompany().getId();
			bankInfoList = getBankInfo(0, supplierId.intValue());
		}

		return bankInfoList;
	}

	/**
	 * 取得收款行的银行信息
	 * 
	 * @param orderPayInput
	 * @return
	 */
	public List<String> getToBankInfo(OrderPayInput orderPayInput) {

		String payType = orderPayInput.getPayType();
		Integer supplyType = orderPayInput.getSupplyType();
		supplyType = this.convertSupplyType2PlatType(supplyType);
		Integer agentId = orderPayInput.getAgentId();

		List<String> bankInfoList = new ArrayList<String>();
		if (("2".equals(payType)) && (agentId == null || agentId == -1)) {
			return bankInfoList;
		}

		if ("1".equals(payType)) {
			Long supplierId = UserUtils.getUser().getCompany().getId();
			bankInfoList = getBankInfo(0, supplierId.intValue());
		} else if ("2".equals(payType)) {
			bankInfoList = getBankInfo(supplyType, agentId);
		}

		return bankInfoList;
	}

	/**
	 * 取得相应指定参数的银行信息
	 * 
	 * @param platType
	 * @param belongParentPlatId
	 * @return
	 */
	public List<String> getBankInfo(Integer platType, Integer belongParentPlatId) {
		List<String> bankInfoList = new ArrayList<String>();
		bankInfoList = platBankInfoService.getPlatBankNameByPlatTypeAndId(
				belongParentPlatId, platType);
		return bankInfoList;
	}

	/**
	 * 取得银行账号信息
	 * 
	 * @param belongParentPlatId
	 * @param bankName
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List getBankAccountInfo(Long belongParentPlatId, String bankName) {
		return platBankInfoService.getBankInfo(
				Long.valueOf(belongParentPlatId), bankName);
	}

	/**
	 * 处理页面取得Form信息
	 * 
	 * @param orderPayForm
	 */
	private void processFormData(OrderPayForm orderPayForm) {
		String orderPayInputJson = orderPayForm.getOrderPayInputJson();
		JSONObject jSONObject = JSONObject.fromObject(orderPayInputJson);
		Map<String, Class<OrderPayDetail>> classMap = new HashMap<String, Class<OrderPayDetail>>();
		classMap.put("orderPayDetailList", OrderPayDetail.class);
		OrderPayInput orderPayInput = (OrderPayInput) JSONObject.toBean(jSONObject, OrderPayInput.class, classMap);
		orderPayForm.setOrderPayInput(orderPayInput);
		// 支付方式名称
		orderPayForm.setPayTypeName(this.getPayTypeName(orderPayForm.getPaymentStatus(), orderPayForm.getPayType()));
		// 合并支付
		String mergePayFlag = orderPayForm.getMergePayFlag();
		if (StringUtils.isBlank(mergePayFlag)) {
			String payType = orderPayInput.getPayType();
			// 收款
			if ("1".equals(payType)) {
				mergePayFlag = "0";

				// 付款
			} else if ("2".equals(payType)) {
				Long companyId = UserUtils.getUser().getCompany().getId();
				if (companyId.longValue() != 71L) {
					mergePayFlag = "0";
					// 新行者的情况下
				} else {
					List<OrderPayDetail> orderPayDetailList = orderPayInput.getOrderPayDetailList();
					mergePayFlag = getMergePayFlag(orderPayDetailList.get(0).getProjectId(), orderPayDetailList.get(0).getRefundMoneyType());
				}
				//团期支付
			} else if("3".equals(payType)) {
				mergePayFlag = "0";
			}

		}
		orderPayForm.setMergePayFlag(mergePayFlag);
	}

	/**
	 * 统计金额
	 * 
	 * @param moneyList
	 * @return
	 */
	public Map<String, String[]> countMoney(List<Money> moneyList) {
		Map<String, String[]> retMap = new LinkedHashMap<String, String[]>();

		Map<String, BigDecimal> map = new LinkedHashMap<String, BigDecimal>();

		for (Money money : moneyList) {
			String currencyId = money.getCurrencyId();
			String currencyPrice = money.getCurrencyPrice();

			String[] currIds = currencyId.split(",");
			String[] currPrices = currencyPrice.split(",");

			for (int j = 0; j < currIds.length; j++) {
				String currId = currIds[j];
				String currPrice = currPrices[j];

				if (!map.containsKey(currId)) {
					map.put(currId, new BigDecimal(currPrice));
				} else {
					map.put(currId,
							map.get(currId).add(new BigDecimal(currPrice)));
				}
			}
		}

		List<String> currIdLst = new ArrayList<String>();
		List<String> currPriceLst = new ArrayList<String>();

		for (String key : map.keySet()) {
			currIdLst.add(key);
			currPriceLst.add(map.get(key).toString());
		}

		retMap.put("totalCurrencyId",
				currIdLst.toArray(new String[currIdLst.size()]));
		retMap.put("totalCurrencyPrice",
				currPriceLst.toArray(new String[currPriceLst.size()]));

		return retMap;
	}

	/**
	 * 判定月结/后付费项目的状态
	 * 
	 * @return
	 */
	public Integer processPaymentStatusFlag(String payType, Integer agentId) {
		Integer paymentStatusFlag = 0;

		// 收款情况下
		if ("1".equals(payType)) {
			Integer paymentStatus = this.getAgentPaymentType(agentId);
			if (paymentStatus != 1) {
				paymentStatusFlag = 1;
			}
		}

		return paymentStatusFlag;
	}

	/**
	 * 取得月结/后续费部分Label标签的描述
	 * 
	 * @param paymentStatus
	 * @return
	 */
	public String processPaymentStatusDesc(Integer paymentStatus) {

		if (paymentStatus == null || paymentStatus.intValue() == 1) {
			return "";
		}

		StringBuffer sb = new StringBuffer();

		sb.append("是否");
		sb.append(DictUtils.getDictLabel(paymentStatus.toString(),
				Context.PAY_MENT_TYPE, ""));
		sb.append("支付");

		return sb.toString();
	}

	/**
	 * 转换金额的信息（总金额）
	 * 
	 * @param orderPayDetailList
	 * @return
	 */
	public List<Money> convertMoneyList2TotalMoneyList(
			List<OrderPayDetail> orderPayDetailList) {
		List<Money> moneyList = new ArrayList<Money>();
		for (OrderPayDetail data : orderPayDetailList) {
			Money money = new Money();
			money.setCurrencyId(data.getTotalCurrencyId());
			money.setCurrencyPrice(data.getTotalCurrencyPrice());
			moneyList.add(money);
		}

		return moneyList;
	}

	/**
	 * 转换金额的信息（应付金额）
	 * 
	 * @param orderPayDetailList
	 * @return
	 */
	public List<Money> convertMoneyList2PayMoneyList(
			List<OrderPayDetail> orderPayDetailList) {
		List<Money> moneyList = new ArrayList<Money>();
		for (OrderPayDetail data : orderPayDetailList) {
			Money money = new Money();
			money.setCurrencyId(data.getPayCurrencyId());
			money.setCurrencyPrice(data.getPayCurrencyPrice());
			moneyList.add(money);
		}

		return moneyList;
	}

	/**
	 * 金额Bean
	 */
	private class Money {
		/** 币种ID（币种间用逗号分隔） */
		private String currencyId;
		/** 支付金额（币种间用逗号分隔） */
		private String currencyPrice;

		public String getCurrencyId() {
			return currencyId;
		}

		public void setCurrencyId(String currencyId) {
			this.currencyId = currencyId;
		}

		public String getCurrencyPrice() {
			return currencyPrice;
		}

		public void setCurrencyPrice(String currencyPrice) {
			this.currencyPrice = currencyPrice;
		}
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * 支付页面支付操作部分
	 */
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 保存支付信息及各业务模块的操作
	 * 
	 * @param orderPayForm
	 * @param model
	 * @return
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public boolean savePayInfo(OrderPayForm orderPayForm, ModelMap model) {

		// ///////////////////////////////////////////////////////////////////////
		/*
		 * 处理form过来的页面数据
		 */
		// ///////////////////////////////////////////////////////////////////////
		processFormData(orderPayForm);

		// ///////////////////////////////////////////////////////////////////////
		/*
		 * 执行其他模块的前置业务逻辑
		 */
		// ///////////////////////////////////////////////////////////////////////
		OrderPayInput orderPayInput = orderPayForm.getOrderPayInput();
		Map<String, Object> map = processBussicess(orderPayInput.getServiceClassName(), orderPayInput.getServiceBeforeMethodName(), orderPayForm);
		// 返回值中有错误信息的情况下
		if (map != null && map.get("isSuccess") != null && (Boolean) (map.get("isSuccess")) == false) {
			model.addAttribute("errorMsg", map.get("errorMsg"));
			return false;
		}

		// ///////////////////////////////////////////////////////////////////////
		/*
		 * 保存收款/付款信息
		 */
		// ///////////////////////////////////////////////////////////////////////
		try {
			savePayRefundInfo(orderPayForm);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", "支付信息保存失败！");
			return false;
		}
		
		// ///////////////////////////////////////////////////////////////////////
		/*
		 * 回写银行账号(如果不存在银行账号信息则保存)
		 */
		// ///////////////////////////////////////////////////////////////////////
		try {
			savePlatBankInfo(orderPayForm);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMsg", "支付信息保存失败！");
			return false;
		}

		// ///////////////////////////////////////////////////////////////////////
		/*
		 * 执行其他模块的后置业务逻辑
		 */
		// ///////////////////////////////////////////////////////////////////////
		map = processBussicess(orderPayInput.getServiceClassName(), orderPayInput.getServiceAfterMethodName(), orderPayForm);
		// 返回值中有错误信息的情况下
		if (map != null && map.get("isSuccess") != null && (Boolean) (map.get("isSuccess")) == false) {
			model.addAttribute("errorMsg", map.get("errorMsg"));
			return false;
		}
		// ///////////////////////////////////////////////////////////////////////

		return true;
	}

	/**
	 * 回写银行账号(如果不存在银行账号信息则保存)
	 */
	private void savePlatBankInfo(OrderPayForm orderPayForm) {
		OrderPayInput orderPayInput = orderPayForm.getOrderPayInput();
		String payType = orderPayInput.getPayType();
		Integer platType = this.convertSupplyType2PlatType(orderPayInput.getSupplyType());
		if("1".equals(payType) || "3".equals(payType)){	// 收款
			String bankName = orderPayForm.getRealBankName();
			String bankAccount = orderPayForm.getRealBankAccount();
			if(StringUtils.isNotBlank(bankName) && StringUtils.isNotBlank(bankAccount)){
				String fmBelongPlatId = getFmBelongPlatId(orderPayInput);
				savePlatBankInfoIfNotExist(Long.valueOf(fmBelongPlatId), bankName, bankAccount, platType);
			}
		}else if("2".equals(payType)){	// 付款
			String toBankName = orderPayForm.getReceiveBankName();
			String toBankAccount = orderPayForm.getReceiveAccount();
//			String toBankName = orderPayForm.getBankName();
//			String toBankAccount = orderPayForm.getRealBankAccount();
			if(StringUtils.isNotBlank(toBankName) && StringUtils.isNotBlank(toBankAccount)){
				String toBelongPlatId = getToBelongPlatId(orderPayInput);
				savePlatBankInfoIfNotExist(Long.valueOf(toBelongPlatId), toBankName, toBankAccount, platType);
			}
		}
	} 

	/**
	 * 回写银行账号(如果不存在银行账号信息则保存)
	 */
	private void savePlatBankInfoIfNotExist(Long beLongPlatId, String bankName, String bankAccountCode, Integer platType) {
		if (beLongPlatId == null || platType == null || StringUtils.isBlank(bankName) || StringUtils.isBlank(bankAccountCode)) {
			logger.error("取消回写银行账号,参数存在空值:beLongPlatId="+beLongPlatId+",bankName="+bankName+",bankAccountCode"+bankAccountCode+",platType="+platType);
			return;
		}
//		List<PlatBankInfo> platBankInfoList = platBankInfoDao.findPlatBankInfoByBeLongPlatId(beLongPlatId, platType, bankName, bankAccountCode);
		List<PlatBankInfo> platBankInfoList = platBankInfoDao.findPlatBankInfoByBeLongPlatIdAndBankAccountCode(beLongPlatId, bankAccountCode);
		if(platBankInfoList.isEmpty()){
			PlatBankInfo platBankInfo = new PlatBankInfo();
			platBankInfo.setBeLongPlatId(beLongPlatId);
			platBankInfo.setBankName(bankName);
			platBankInfo.setBankAccountCode(bankAccountCode);
			platBankInfo.setCreateDate(new Date());
			platBankInfo.setDelFlag("0");
			platBankInfo.setPlatType(platType);
			platBankInfoDao.save(platBankInfo);
		}
	}

	/**
	 * 保存支付信息（收款及付款）
	 * 
	 * @param orderPayForm
	 * @throws Exception
	 */
	private void savePayRefundInfo(OrderPayForm orderPayForm) throws Exception {

		OrderPayInput orderPayInput = orderPayForm.getOrderPayInput();
		String payType = orderPayInput.getPayType();

		
		// 收款
		if (StringUtils.isBlank(payType) || "1".equals(payType)) {
			if (orderPayForm.getPaymentStatus() != null && orderPayForm.getPaymentStatus() == 1) {
				
				if(orderPayInput.getOrderType() != null && Context.ORDER_TYPE_ISLAND.intValue() == orderPayInput.getOrderType()) {
					//保存支付表（海岛游）
					payIslandOrderService.saveByOrderPayForm(orderPayForm);
				} else if(orderPayInput.getOrderType() != null && Context.ORDER_TYPE_HOTEL.intValue() == orderPayInput.getOrderType()) {
					//保存支付表（酒店）
					payHotelOrderService.saveByOrderPayForm(orderPayForm);
				} else {
					// orderpay表中保存数据
					this.saveOrderPayInfo(orderPayForm);
					// pay表中保存数据
					this.savePayInfo(orderPayForm);
				}
			}

			orderPayForm.setMsg("恭喜！您已成功下单");

			// 付款
		} else if ("2".equals(payType)) {
			saveRefundInfo(orderPayForm);

			orderPayForm.setMsg("您已成功付款！");
			//团期收款
		} else if ("3".equals(payType)) {
			savePayGroupInfo(orderPayForm);
			
			orderPayForm.setMsg("您已成功收款！");
		}
	}

	/**
	 * 保存Pay的信息
	 */
	private void savePayInfo(OrderPayForm orderPayForm) {
		// 支付类型信息保存
		savePayTypeDetailInfo(orderPayForm);
		// 支付基本信息保存
		savePayBaseInfo(orderPayForm);
	}

	/**
	 * 保存输出的参数信息
	 * 
	 * @param orderPayForm
	 * @param model
	 */
	public void saveOutputParameter(OrderPayForm orderPayForm, ModelMap model) {
		OrderPayInput orderPayInput = orderPayForm.getOrderPayInput();
		List<OrderPayDetail> orderPayDetailList = orderPayInput.getOrderPayDetailList();
		model.addAttribute("orderNum", orderPayDetailList.get(0).getOrderNum());
		model.put("orderNum", orderPayDetailList.get(0).getOrderNum());
		int payDetailListSize = orderPayDetailList.size();
		
		if (payDetailListSize == 1) {
			model.addAttribute("orderNum", orderPayDetailList.get(0).getOrderNum());
			model.put("orderNum", orderPayDetailList.get(0).getOrderNum());
		}

		//合并支付
		String mergePayFlag = orderPayForm.getMergePayFlag();
		if ("0".equals(mergePayFlag)) {
			if (orderPayDetailList != null && orderPayDetailList.size() > 1) {
				Map<String, String[]> moneyMap = countMoney(convertMoneyList2PayMoneyList(orderPayDetailList));

				List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
				model.addAttribute("currencyIdPrice", moneyMap.get("totalCurrencyId"));
				model.addAttribute("dqzfprice", moneyMap.get("totalCurrencyPrice"));
				model.addAttribute("curlist", currencylist);
				model.put("curlist", currencylist);
			} else if (orderPayDetailList != null && orderPayDetailList.size() == 1) {
				List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
				model.addAttribute("currencyIdPrice", orderPayForm.getCurrencyIdPrice());
				model.addAttribute("dqzfprice", orderPayForm.getDqzfprice());
				model.addAttribute("curlist", currencylist);
			}
		} else if ("1".equals(mergePayFlag)) {
			model.addAttribute("mergeCurrencyPrice", orderPayForm.getMergeCurrencyPrice());
		}
		
		// 导航图片显示状态
		model.addAttribute("navigationImgFlag", orderPayInput.getNavigationImgFlag());
		// 页面显示的消息提示
		model.addAttribute("msg", orderPayForm.getMsg());
		model.addAttribute("payDetailListSize", payDetailListSize);
		model.addAttribute("orderDetailUrl", orderPayForm.getOrderPayInput().getOrderDetailUrl());
		model.addAttribute("orderListUrl", orderPayForm.getOrderPayInput().getOrderListUrl());
		model.addAttribute("paymentListUrl", orderPayForm.getOrderPayInput().getPaymentListUrl());
		model.addAttribute("payTypeDesc", getPayTypeDesc(orderPayInput.getPayType()));
		model.addAttribute("refundMoneyTypeDesc", orderPayInput.getRefundMoneyTypeDesc());
		model.addAttribute("payee", orderPayForm.getPayee());
		if(orderPayForm.getPayType()==9){
			model.addAttribute("payTypeName","因公支付宝");
		}else{
			model.addAttribute("payTypeName", orderPayForm.getPayTypeName());
		}
		model.addAttribute("mergePayFlag", mergePayFlag);
		model.addAttribute("entryOrderUrl", orderPayInput.getEntryOrderUrl());
	}

	/**
	 * 保存收款信息
	 * 
	 * @param orderPayForm
	 * @throws IOException
	 */
	private void saveRefundInfo(OrderPayForm orderPayForm) throws IOException {
		OrderPayInput orderPayInput = orderPayForm.getOrderPayInput();
		List<OrderPayDetail> orderPayDetailList = orderPayInput.getOrderPayDetailList();

		//手续费集合
//		List<PayFee> payFees = new ArrayList<PayFee>();
		int i = 0;
//		for (int i = 0; i < orderPayDetailList.size(); i++) {
		{

			OrderPayDetail orderPayDetail = orderPayDetailList.get(i);

			Refund refund = new Refund();

			String id = this.getUUID();
			refund.setId(id);
			orderPayDetail.setRefundId(id);

			// 支付方式
			refund.setPayType(orderPayForm.getPayType());
			savePayTypeDetailInfo(orderPayForm);
			refund.setPayTypeId(orderPayDetail.getPayTypeId());

			String moneySerialNum = this.getUUID();
			orderPayDetail.setMoneySerialNum(moneySerialNum);
			refund.setMoneySerialNum(moneySerialNum);
			
			//手续费处理
			if(orderPayForm.getFeeAmount() != null && orderPayForm.getFeeAmount().length != 0) {
				String[] currencyIds = orderPayForm.getCurrencyIdPrice();
				String[] dqzfPrices = orderPayForm.getDqzfprice();
				
				//将付款手续费进行统一整理
				Map<String, BigDecimal> feePrices = new HashMap<String, BigDecimal>();
				String[] feeCurrencyIds = orderPayForm.getFeeCurrencyId();
				String[] feeAmounts = orderPayForm.getFeeAmount();
				for(int j=0; j<feeAmounts.length; j++) {
					if(StringUtils.isEmpty(feeAmounts[j])) {
						continue;
					}
					if(feePrices.containsKey(feeCurrencyIds[j])) {
						BigDecimal allPrices = feePrices.get(feeCurrencyIds[j]);
						feePrices.put(feeCurrencyIds[j], allPrices.add(new BigDecimal(feeAmounts[j])));
					} else {
						feePrices.put(feeCurrencyIds[j], new BigDecimal(feeAmounts[j]));
					}
				}
				
				for(int j=0; j<currencyIds.length; j++) {
					if(feePrices.containsKey(currencyIds[j])) {
						dqzfPrices[j] = String.valueOf(feePrices.get(currencyIds[j]).add(new BigDecimal(dqzfPrices[j])));
					}
				}
				orderPayForm.setDqzfprice(dqzfPrices);
			}
			
			if (orderPayDetailList.size() == 1) {
				if(Context.ORDER_TYPE_ISLAND.intValue() == orderPayDetail.getOrderType() || Context.ORDER_TYPE_HOTEL.intValue() == orderPayDetail.getOrderType()) {
					this.saveProductMoneyAmount(moneySerialNum, orderPayForm.getCurrencyIdPrice(), orderPayForm.getDqzfprice(), orderPayDetail, orderPayDetail.getOrderType());
				} else {
					this.saveMoneyAmount(moneySerialNum, orderPayForm.getCurrencyIdPrice(), orderPayForm.getDqzfprice(), orderPayDetail);
				}

				if (StringUtils.isNotBlank(orderPayForm.getMergePayFlag()) && "1".equals(orderPayForm.getMergePayFlag())) {
					savePayConvert(moneySerialNum, orderPayForm.getCurrencyIdPrice(), orderPayForm.getConvertLowest());
				}
			} else {
				if(Context.ORDER_TYPE_ISLAND.intValue() == orderPayDetail.getOrderType() || Context.ORDER_TYPE_HOTEL.intValue() == orderPayDetail.getOrderType()) {
					this.saveProductMoneyAmount(moneySerialNum, orderPayDetail.getPayCurrencyId().split(","), orderPayDetail.getPayCurrencyPrice().split(","), orderPayDetail, orderPayDetail.getOrderType());
				} else {
					this.saveMoneyAmount(moneySerialNum, orderPayDetail.getPayCurrencyId().split(","), orderPayDetail.getPayCurrencyPrice().split(","), orderPayDetail);
				}
			}

			// 凭证信息保存
			// 保存支付凭证ids
			String payVoucher = "";
			Long[] docInfoIds = orderPayForm.getDocInfoIds();
			if (i == 0) {
				if (docInfoIds != null && docInfoIds.length > 0) {
					for (Long docInfoId : docInfoIds) {
						payVoucher += docInfoId + ",";
					}
				}
			} else {
				if (docInfoIds != null && docInfoIds.length > 0) {
					for (int k = 0; k < docInfoIds.length; k++) {
						Long docInfoId = docInfoIds[k];
						Long docId = copySaveUploadInfo(docInfoId);
						payVoucher += docId + ",";
					}
				}
			}
			refund.setPayVoucher(payVoucher);

			// 备注信息
			refund.setRemarks(orderPayForm.getRemarks());
			// 是否合并支付
			String mergePayFlag = orderPayForm.getMergePayFlag();
			if (StringUtils.isBlank(mergePayFlag)) {
				mergePayFlag = "0";
			}
			refund.setMergePayFlag(mergePayFlag);
			if ("1".equals(mergePayFlag)) {
				// 合并金额UUID
				String mergeMoneySerialNum = this.getUUID();
				refund.setMergeMoneySerialNum(mergeMoneySerialNum);
				String mergeCurrencyPrice = orderPayForm.getMergeCurrencyPrice();
				if (StringUtils.isNotBlank(mergeCurrencyPrice)) {
					mergeCurrencyPrice = mergeCurrencyPrice.replace("￥", "").replaceAll(",", "").trim();
				}
				
				int rmbCurrencyId = 0;
				//当前系统没有默认币种功能，所以只能将人民币设置为默认币种
				Currency rmbCurrency = currencyService.getRMBCurrencyId();
				if (rmbCurrency != null) {
					rmbCurrencyId = rmbCurrency.getId().intValue();
				} else {
					throw new IllegalArgumentException("此供应商没有人民币币种");
				}
				if(Context.ORDER_TYPE_ISLAND.intValue() == orderPayDetail.getOrderType() || Context.ORDER_TYPE_HOTEL.intValue() == orderPayDetail.getOrderType()) {
					this.saveProductMoneyAmount(mergeMoneySerialNum, rmbCurrencyId, mergeCurrencyPrice, orderPayDetail.getOrderType());
				} else {
					this.saveMoneyAmount(mergeMoneySerialNum, rmbCurrencyId, mergeCurrencyPrice);
				}
			}
			// 款项类型
			refund.setMoneyType(orderPayDetail.getRefundMoneyType());
			// 业务表ID
			refund.setRecordId(orderPayDetail.getProjectId());
			// 收款单位
			refund.setPayee(orderPayForm.getPayee());
			// 订单类型
			refund.setOrderType(orderPayDetail.getOrderType());
			
			//设置批发商uuid
			refund.setCompanyUuid(UserUtils.getUser().getCompany().getUuid());

			refundDao.save(refund);
			
			//保存支付手续费信息
			savePayFee(orderPayForm, refund.getId());
		}
	}

	/**
	 * 保存支付手续费信息集合
	 * @Description: 
	 * @param @param orderPayForm
	 * @param @param refundId   
	 * @return void  
	 * @throws
	 * @author majiancheng
	 * @date 2016-1-7
	 */
	private void savePayFee(OrderPayForm orderPayForm, String refundId) {
		List<PayFee> payFees = new ArrayList<PayFee>();
		String[] feeNames = orderPayForm.getFeeName();
		String[] feeCurrencyIds = orderPayForm.getFeeCurrencyId();
		String[] feeAmounts = orderPayForm.getFeeAmount();
		if(feeAmounts != null && feeAmounts.length!=0) {
			for(int i=0; i<feeAmounts.length; i++) {
				if(StringUtils.isEmpty(feeAmounts[i])) {
					continue;
				}
				PayFee payFee = new PayFee();
				payFee.setFeeName(feeNames[i]);
				payFee.setFeeCurrencyId(Integer.parseInt(feeCurrencyIds[i]));
				payFee.setFeeAmount(new BigDecimal(feeAmounts[i]));
				payFee.setRefundId(refundId);
				super.setOptInfo(payFee, BaseService.OPERATION_ADD);
				
				payFees.add(payFee);
			}
			
			payFeeDao.batchSave(payFees);
		}
	}
	
	/**
	 * 支付基本信息保存
	 * 
	 * @param orderPayForm
	 */
	private void savePayBaseInfo(OrderPayForm orderPayForm) {
		OrderPayInput orderPayInput = orderPayForm.getOrderPayInput();
		List<OrderPayDetail> orderPayDetailList = orderPayInput
				.getOrderPayDetailList();

		List<Pay> payList = new ArrayList<Pay>();
		Pay pay = null;
		for (OrderPayDetail orderPayDetail : orderPayDetailList) {
			pay = new Pay();

			String payId = this.getUUID();
			pay.setId(payId);
			orderPayDetail.setPayId(payId);
			
			pay.setPayType(orderPayForm.getPayType());
			pay.setPayTypeId(orderPayDetail.getPayTypeId());
			pay.setMoneySerialNum(orderPayDetail.getMoneySerialNum());
			pay.setPayVoucher(orderPayDetail.getPayVoucher());
			pay.setRemarks(orderPayForm.getRemarks());

			payList.add(pay);
		}
		payDao.save(payList);
	}

	/**
	 * 支付类型信息保存
	 * 
	 * @param orderPayForm
	 */
	private void savePayTypeDetailInfo(OrderPayForm orderPayForm) {

		OrderPayInput orderPayInput = orderPayForm.getOrderPayInput();
		List<OrderPayDetail> orderPayDetailList = orderPayInput.getOrderPayDetailList();

		for (OrderPayDetail orderPayDetail : orderPayDetailList) {
			String id = UuidUtils.generUuid();

			switch (orderPayForm.getPayType()) {
			case 1:// 支票支付
				PayCheck payCheck = new PayCheck();
				payCheck.setId(id);
				payCheck.setPayerName(orderPayForm.getPayerName());
				payCheck.setCheckNumber(orderPayForm.getCheckNumber());
				payCheck.setInvoiceDate(orderPayForm.getInvoiceDate());
				payCheckDao.save(payCheck);

				break;
			case 3:// 现金支付
				break;
			case 4:// 汇款
				PayRemittance payRemittance = new PayRemittance();
				payRemittance.setId(id);
				payRemittance.setBankName(orderPayForm.getBankName());
				payRemittance.setBankAccount(orderPayForm.getBankAccount());
				payRemittance.setTobankName(orderPayForm.getToBankNname());
				payRemittance.setTobankAccount(orderPayForm.getToBankAccount());
				payRemittanceDao.save(payRemittance);

				break;
			case 5:// 快速支付
				break;
			case 6://银行转账
				PayBanktransfer payBanktransfer = new PayBanktransfer();
				payBanktransfer.setPayBankName(orderPayForm.getPayBankName());
				payBanktransfer.setPayAccount(orderPayForm.getPayAccount());
				payBanktransfer.setReceiveCompanyName(orderPayForm.getPayee());
				payBanktransfer.setReceiveBankName(orderPayForm.getReceiveBankName());
				payBanktransfer.setReceiveAccount(orderPayForm.getReceiveAccount());
				super.setOptInfo(payBanktransfer, BaseService.OPERATION_ADD);
				payBanktransfer.setUuid(id);
				
				payBanktransferDao.saveObj(payBanktransfer);
				break;
			case 7://汇票
				PayDraft payDraft = new PayDraft();
				payDraft.setDrawerName(orderPayForm.getDrawerName());
				payDraft.setDrawerAccount(orderPayForm.getDrawerAccount());
				payDraft.setPayBankName(orderPayForm.getPayBankName());
				payDraft.setDraftAccountedDate(orderPayForm.getDraftAccountedDate());
				payDraft.setReceiveCompanyName(orderPayForm.getPayee());
				payDraft.setReceiveBankName(orderPayForm.getReceiveBankName());
				payDraft.setReceiveAccount(orderPayForm.getReceiveAccount());
				super.setOptInfo(payDraft, BaseService.OPERATION_ADD);
				payDraft.setUuid(id);
				
				payDraftDao.saveObj(payDraft);
				break;
			case 8://POS机刷卡
				PayPos payPos = new PayPos();
				payPos.setReceiveCompanyName(orderPayForm.getPayee());
				payPos.setReceiveBankName(orderPayForm.getReceiveBankName());
				payPos.setReceiveAccount(orderPayForm.getReceiveAccount());
				super.setOptInfo(payPos, BaseService.OPERATION_ADD);
				payPos.setUuid(id);
				
				payPosDao.saveObj(payPos);
				break;
			case 9://支付宝
				PayAlipay alipay = new PayAlipay();
				alipay.setFromAlipayName(orderPayForm.getFromAlipayName());
				alipay.setFromAlipayAccount(orderPayForm.getFromAlipayAccount());
				alipay.setToAlipayName(orderPayForm.getToAlipayName());
				alipay.setToAlipayAccount(orderPayForm.getToAlipayAccount());
				alipay.setReceiveCompanyName(orderPayForm.getPayee());
				super.setOptInfo(alipay,BaseService.OPERATION_ADD);
				alipay.setUuid(id);

				payAlipayDao.saveObj(alipay);
				break;
			default:
				//do nothing.
			}

			orderPayDetail.setPayTypeId(id);
		}
	}

	/**
	 * 保存OrderPay的信息
	 * 
	 * @param orderPayForm
	 * @throws IOException
	 */
	private void saveOrderPayInfo(OrderPayForm orderPayForm) throws IOException {
		OrderPayInput orderPayInput = orderPayForm.getOrderPayInput();
		List<OrderPayDetail> orderPayDetailList = orderPayInput.getOrderPayDetailList();

		//签证游客付款时，每条支付记录对于同一次支付操作生成一个UUID
		//add by jiachen
		String paySerialNum = getUUID();
		
		for (int i = 0; i < orderPayDetailList.size(); i++) {

			OrderPayDetail orderPayDetail = orderPayDetailList.get(i);

			Orderpay orderPay = new Orderpay();
			
			//签证游客付款时，每条支付记录对于同一次支付操作生成一个UUID
			//add by jiachen
			if(Context.ORDER_TYPE_QZ.equals(orderPayDetail.getOrderType())) {
				orderPay.setOrderPaySerialNum(paySerialNum);
				orderPay.setIsAsAccount(99);
				orderPayInput.setMoneySerialNum(paySerialNum);
				orderPayDetail.setOrderPaySerialNum(paySerialNum);
			}
			
			// 支付方式
			orderPay.setPayType(orderPayForm.getPayType());
			Integer paymentStatus = orderPayForm.getPaymentStatus();
			if (paymentStatus == null) {
				paymentStatus = 1;
			}
			orderPay.setPaymentStatus(paymentStatus);
			orderPay.setPayTypeName(this.getPayTypeName(paymentStatus, orderPayForm.getPayType()));
			if(orderPayForm.getPayType()==9){
				orderPay.setPayTypeName("因公支付宝");
			}
			// 即时支付场合
			if (paymentStatus == 1) {
				// 来款单位
				if (StringUtils.isNotEmpty(orderPayForm.getPayerName())) {
					orderPay.setPayerName(orderPayForm.getPayerName());
				}
				
				switch (orderPayForm.getPayType()) {
				case 1:// 支票支付
						// 支票号
					if (StringUtils.isNotEmpty(orderPayForm.getCheckNumber())) {
						orderPay.setCheckNumber(orderPayForm.getCheckNumber());
					}
					// 开票日期
					if (orderPayForm.getInvoiceDate() != null && StringUtils.isNotEmpty(orderPayForm.getInvoiceDate().toString())) {
						orderPay.setInvoiceDate(orderPayForm.getInvoiceDate());
					}
					break;
				case 3:// 现金支付
					break;
				case 4:// 汇款
						// 开户行名称
					if (StringUtils.isNotEmpty(orderPayForm.getBankName())) {
						orderPay.setBankName(orderPayForm.getBankName());
					}
					// 开户行账户
					if (StringUtils.isNotEmpty(orderPayForm.getBankAccount())) {
						orderPay.setBankAccount(orderPayForm.getBankAccount());
					}
					// 转入行名称
					if (StringUtils.isNotEmpty(orderPayForm.getToBankNname())) {
						orderPay.setToBankNname(orderPayForm.getToBankNname());
					}
					// 转入行账号
					if (StringUtils.isNotEmpty(orderPayForm.getToBankAccount())) {
						orderPay.setToBankAccount(orderPayForm.getToBankAccount());
					}
					break;
				case 5:// 快速支付
						// 支付方式
					if (StringUtils.isNotEmpty(orderPayForm.getFastPayType())) {
						orderPay.setFastPayType(orderPayForm.getFastPayType());
					}
					break;
				case 9:
					//支付宝账户（来款）
					if(StringUtils.isNotEmpty(orderPayForm.getFromAlipayName())){
						orderPay.setFromAlipayName(orderPayForm.getFromAlipayName());
					}
					if(StringUtils.isNotEmpty(orderPayForm.getFromAlipayAccount())){
						orderPay.setFromAlipayAccount(orderPayForm.getFromAlipayAccount());
					}
					if(StringUtils.isNotEmpty(orderPayForm.getToAlipayName())){
						orderPay.setToAlipayName(orderPayForm.getToAlipayName());
					}else{
						orderPay.setToAlipayName(orderPayForm.getToAlipayName1());
					}
					if(StringUtils.isNotEmpty(orderPayForm.getToAlipayAccount())){
						orderPay.setToAlipayAccount(orderPayForm.getToAlipayAccount());
					}else{
						orderPay.setToAlipayAccount(orderPayForm.getToAlipayAccount1());
					}
					if(StringUtils.isNotEmpty(orderPayForm.getComeOfficeName())){
						orderPay.setComeOfficeName(orderPayForm.getComeOfficeName());
					}
				}
				
				//多个支付方式设置值--------
				//付款单位
				if(StringUtils.isNotEmpty(orderPayForm.getPayerName())) {
					orderPay.setPayerName(orderPayForm.getPayerName());
				}
				// 开户行名称
				if (StringUtils.isNotEmpty(orderPayForm.getBankName())) {
					orderPay.setBankName(orderPayForm.getBankName());
				}
				// 开户行账户
				if (StringUtils.isNotEmpty(orderPayForm.getBankAccount())) {
					orderPay.setBankAccount(orderPayForm.getBankAccount());
				}
				// 转入行名称
				if (StringUtils.isNotEmpty(orderPayForm.getToBankNname())) {
					orderPay.setToBankNname(orderPayForm.getToBankNname());
				}
				// 转入行账号
				if (StringUtils.isNotEmpty(orderPayForm.getToBankAccount())) {
					orderPay.setToBankAccount(orderPayForm.getToBankAccount());
				}
				//汇票到账日期
				if(orderPayForm.getDraftAccountedDate() != null) {
					orderPay.setDraftAccountedDate(orderPayForm.getDraftAccountedDate());
				}
				//多个支付方式设置值--------

				// 保存支付订单金额
				String moneyUuid = UUID.randomUUID().toString();
				orderPay.setMoneySerialNum(moneyUuid);
				orderPayDetail.setMoneySerialNum(moneyUuid);

				// 备注信息
				if (StringUtils.isNotEmpty(orderPayForm.getRemarks())) {
					orderPay.setRemarks(orderPayForm.getRemarks());
				}

				/*
				 * 其他和支付无直接关系，各模块相关的业务字段
				 */
				// 订单id
				orderPay.setOrderId(orderPayDetail.getOrderId());
				// 订单类型
				orderPay.setOrderType(orderPayDetail.getOrderType());
				// 订单号
				orderPay.setOrderNum(orderPayDetail.getOrderNum());
				// 游客id
				orderPay.setTravelerId(orderPayDetail.getTravelerId());
				// 支付款类型（全款、定金、尾款）
				orderPay.setPayPriceType(orderPayDetail.getPayPriceType());
				//保存款项 131需求

				/*if(orderPayForm.getPayPriceType()!=null){

					orderPay.setPayPriceType(orderPayForm.getPayPriceType());
				}else{
					orderPay.setPayPriceType(orderPayDetail.getPayPriceType());
				}*/
			}

			// 即时支付场合
			if (paymentStatus == 1) {
				//保存订单支付信息
				orderpayDao.save(orderPay);
				
				// 保存支付凭证ids
				String payVoucher = "";
				Long[] docInfoIds = orderPayForm.getDocInfoIds();
				if (i == 0) {
					if (docInfoIds != null && docInfoIds.length > 0) {
						for (Long docInfoId : docInfoIds) {
							payVoucher += docInfoId + ",";
						}
					}
				} 
				/*else {
					if (docInfoIds != null && docInfoIds.length > 0) {
						for (int k = 0; k < docInfoIds.length; k++) {
							Long docInfoId = docInfoIds[k];
							Long docId = copySaveUploadInfo(docInfoId);
							payVoucher += docId + ",";
							docInfoIds[k] = docId;
						}
					}
				}*/

				orderPay.setPayVoucher(payVoucher);
				orderPayDetail.setPayVoucher(payVoucher);

				// 为上传资料设置payorderid属性
				if(i == 0) {
					saveDocInfoByPayUUID(orderPay.getMoneySerialNum(), docInfoIds);
				}

				if (orderPayDetailList.size() > 1) {
					this.saveMoneyAmount(orderPay.getMoneySerialNum(), orderPayDetail.getPayCurrencyId().split(","), orderPayDetail.getPayCurrencyPrice().split(","), orderPayDetail);
				} else {
					this.saveMoneyAmount(orderPay.getMoneySerialNum(), orderPayForm.getCurrencyIdPrice(), orderPayForm.getDqzfprice(), orderPayDetail);
				}
			}

			orderPayDetail.setOrderPayId(orderPay.getId());
			orderPayDetail.setPayTypeName(getPayTypeName(paymentStatus, orderPayForm.getPayType()));
		}
	}
	
	/**
	 * 保存payGroup的信息
	 * 
	 * @param orderPayForm
	 * @throws IOException
	 */
	private void savePayGroupInfo(OrderPayForm orderPayForm) throws IOException {
		OrderPayInput orderPayInput = orderPayForm.getOrderPayInput();
		List<OrderPayDetail> orderPayDetailList = orderPayInput.getOrderPayDetailList();

		for (int i = 0; i < orderPayDetailList.size(); i++) {

			OrderPayDetail orderPayDetail = orderPayDetailList.get(i);

			PayGroup payGroup = new PayGroup();
			// 支付方式
			payGroup.setPayType(orderPayForm.getPayType());
			Integer paymentStatus = orderPayForm.getPaymentStatus();
			if (paymentStatus == null) {
				paymentStatus = 1;
			}
			payGroup.setPaymentStatus(paymentStatus);
			payGroup.setPayTypeName(this.getPayTypeName(paymentStatus, orderPayForm.getPayType()));

			// 即时支付场合
			if (paymentStatus == 1) {
				// 来款单位
				if (StringUtils.isNotEmpty(orderPayForm.getPayerName())) {
					payGroup.setPayerName(orderPayForm.getPayerName());
				}

				switch (orderPayForm.getPayType()) {
				case 1:// 支票支付
						// 支票号
					if (StringUtils.isNotEmpty(orderPayForm.getCheckNumber())) {
						payGroup.setCheckNumber(orderPayForm.getCheckNumber());
					}
					// 开票日期
					if (orderPayForm.getInvoiceDate() != null && StringUtils.isNotEmpty(orderPayForm.getInvoiceDate().toString())) {
						payGroup.setInvoiceDate(orderPayForm.getInvoiceDate());
					}
					break;
				case 3:// 现金支付
					break;
				case 4:// 汇款
						// 开户行名称
					if (StringUtils.isNotEmpty(orderPayForm.getBankName())) {
						payGroup.setBankName(orderPayForm.getBankName());
					}
					// 开户行账户
					if (StringUtils.isNotEmpty(orderPayForm.getBankAccount())) {
						payGroup.setBankAccount(orderPayForm.getBankAccount());
					}
					// 转入行名称
					if (StringUtils.isNotEmpty(orderPayForm.getToBankNname())) {
						payGroup.setToBankNname(orderPayForm.getToBankNname());
					}
					// 转入行账号
					if (StringUtils.isNotEmpty(orderPayForm.getToBankAccount())) {
						payGroup.setToBankAccount(orderPayForm.getToBankAccount());
					}
					break;
				case 5:// 快速支付
						// 支付方式
					if (StringUtils.isNotEmpty(orderPayForm.getFastPayType())) {
						payGroup.setFastPayType(orderPayForm.getFastPayType());
					}
					break;
				case 9://因公支付宝
					payGroup.setPayTypeName("因公支付宝");
					if(StringUtils.isNotEmpty(orderPayForm.getFromAlipayName())){
						payGroup.setFromAlipayName(orderPayForm.getFromAlipayName());
					}
					if(StringUtils.isNotEmpty(orderPayForm.getFromAlipayAccount())){
						payGroup.setFromAlipayAccount(orderPayForm.getFromAlipayAccount());
					}
					if(StringUtils.isNotEmpty(orderPayForm.getToAlipayName())){
						payGroup.setToAlipayName(orderPayForm.getToAlipayName());
					}else{
						payGroup.setToAlipayName(orderPayForm.getToAlipayName1());
					}
					if(StringUtils.isNotEmpty(orderPayForm.getToAlipayAccount())){
						payGroup.setToAlipayAccount(orderPayForm.getToAlipayAccount());
					}else{
						payGroup.setToAlipayAccount(orderPayForm.getToAlipayAccount1());
					}
					if(StringUtils.isNotEmpty(orderPayForm.getComeOfficeName())){
						payGroup.setComeOfficeName(orderPayForm.getComeOfficeName());
					}
				}
				
				//多个支付方式设置值--------
				//付款单位
				if(StringUtils.isNotEmpty(orderPayForm.getPayerName())) {
					payGroup.setPayerName(orderPayForm.getPayerName());
				}
				// 开户行名称
				if (StringUtils.isNotEmpty(orderPayForm.getBankName())) {
					payGroup.setBankName(orderPayForm.getBankName());
				}
				// 开户行账户
				if (StringUtils.isNotEmpty(orderPayForm.getBankAccount())) {
					payGroup.setBankAccount(orderPayForm.getBankAccount());
				}
				// 转入行名称
				if (StringUtils.isNotEmpty(orderPayForm.getToBankNname())) {
					payGroup.setToBankNname(orderPayForm.getToBankNname());
				}
				// 转入行账号
				if (StringUtils.isNotEmpty(orderPayForm.getToBankAccount())) {
					payGroup.setToBankAccount(orderPayForm.getToBankAccount());
				}
				//汇票到账日期
				if(orderPayForm.getDraftAccountedDate() != null) {
					payGroup.setDraftAccountedDate(orderPayForm.getDraftAccountedDate());
				}
				//多个支付方式设置值--------
				
				// 备注信息
				if (StringUtils.isNotEmpty(orderPayForm.getRemarks())) {
					payGroup.setRemarks(orderPayForm.getRemarks());
				}
				
				String payPrice = UuidUtils.generUuid();
				orderPayDetail.setPayPrice(payPrice);
				payGroup.setPayPrice(payPrice);

				if (orderPayDetailList.size() == 1) {
					//新产品订单支付
					if(Context.ORDER_TYPE_ISLAND.intValue() == orderPayDetail.getOrderType() || Context.ORDER_TYPE_HOTEL.intValue() == orderPayDetail.getOrderType()) {
						this.saveProductMoneyAmount(payPrice, orderPayForm.getCurrencyIdPrice(), orderPayForm.getDqzfprice(), orderPayDetail, orderPayDetail.getOrderType());
						//其他订单
					}else {
						this.saveMoneyAmount(payPrice, orderPayForm.getCurrencyIdPrice(), orderPayForm.getDqzfprice(), orderPayDetail);
					}

					if (StringUtils.isNotBlank(orderPayForm.getMergePayFlag()) && "1".equals(orderPayForm.getMergePayFlag())) {
						savePayConvert(payPrice, orderPayForm.getCurrencyIdPrice(), orderPayForm.getConvertLowest());
					}
				}
				
				// 凭证信息保存
				// 保存支付凭证ids
				String payVoucher = "";
				Long[] docInfoIds = orderPayForm.getDocInfoIds();
				if (i == 0) {
					if (docInfoIds != null && docInfoIds.length > 0) {
						for (Long docInfoId : docInfoIds) {
							payVoucher += docInfoId + ",";
						}
					}
				} else {
					if (docInfoIds != null && docInfoIds.length > 0) {
						for (int k = 0; k < docInfoIds.length; k++) {
							Long docInfoId = docInfoIds[k];
							Long docId = copySaveUploadInfo(docInfoId);
							payVoucher += docId + ",";
						}
					}
				}
				payGroup.setPayVoucher(payVoucher);

				// 备注信息
				payGroup.setRemarks(orderPayForm.getRemarks());

				
				// 团期id(单团存团期id，签证和机票暂存产品id)
				payGroup.setGroupId(orderPayDetail.getGroupId());
				// 订单类型
				payGroup.setOrderType(orderPayDetail.getOrderType());
				// 支付款类型（全款、定金、尾款）
				payGroup.setPayPriceType(orderPayDetail.getPayPriceType());
				//团期成本ID
				payGroup.setCostRecordId(orderPayDetail.getCostRecordId());
			}
			
			
			orderPayDetail.setPayGroupUuid(payGroup.getUuid());
			orderPayDetail.setPayTypeName(getPayTypeName(paymentStatus, orderPayForm.getPayType()));
			
			//保存订单支付信息
			payGroupService.save(payGroup);
		}
	}

	/**
	 * 取得支付类型名称
	 * 
	 * @param paymentStatus
	 * @param payType
	 * @return
	 */
	String getPayTypeName(Integer paymentStatus, Integer payType) {
		// 即时支付
		if (paymentStatus == null || paymentStatus == 1) {
			return DictUtils.getDictLabel(payType.toString(), Context.ORDER_PAYTYPE, "");
		}

		// 其他支付方式
		return DictUtils.getDictLabel(paymentStatus.toString(), Context.PAY_MENT_TYPE, "");
	}

	/**
	 * 调用相关模块的保存支付信息前的业务
	 * 
	 * @param className
	 * @param methodName
	 * @param orderPayForm
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> processBussicess(String className, String methodName, OrderPayForm orderPayForm) {
		Map<String, Object> map = new HashMap<String, Object>();

		if (StringUtils.isBlank(className) || StringUtils.isBlank(methodName)) {
			return map;
		}

		try {
			Class<?> clazz = Class.forName(className);
			Method method = clazz.getMethod(methodName, OrderPayForm.class);
			map = (Map<String, Object>) method.invoke(SpringContextHolder.getBean(clazz), orderPayForm);
		} catch (ClassNotFoundException e) {
			map.put("isSuccess", false);
			map.put("errorMsg", "提供的[" + className + "]类无法实例化<br>" + e);
			return map;
		} catch (NoSuchMethodException e) {
			map.put("isSuccess", false);
			map.put("errorMsg", "提供的[" + className + ":" + methodName
					+ "]无法调用，请检查类和方法及传入参数的正确性<br>" + e);
			return map;
		} catch (SecurityException e) {
			map.put("isSuccess", false);
			map.put("errorMsg", "提供的[" + className + ":" + methodName
					+ "]无法调用，请检查类和方法及传入参数的正确性<br>" + e);
			return map;
		} catch (Exception e) {
			map.put("isSuccess", false);
			map.put("errorMsg", "调用[" + className + ":" + methodName
					+ "]内部出现错误，请检查方法内部代码的正确性<br>" + e);
			return map;
		}

		return map;
	}

	/**
	 * 合并支付金额保存
	 * 
	 * @param serialNum
	 * @param payCurrencyPrice
	 */
	private void saveMoneyAmount(String serialNum, Integer payCurrencyId,
			String payCurrencyPrice) {

		MoneyAmount moneyAmount = new MoneyAmount();
		// 流水号UUID
		moneyAmount.setSerialNum(serialNum);
		// 币种ID
		moneyAmount.setCurrencyId(payCurrencyId);
		// 金额
		moneyAmount.setAmount(new BigDecimal(payCurrencyPrice));
		// 记录人ID
		moneyAmount.setCreatedBy(UserUtils.getUser().getId());

		moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount);
	}
	
	private void saveProductMoneyAmount(String serialNum, Integer payCurrencyId, String payCurrencyPrice, int orderType) {
		ProductMoneyAmount moneyAmount = new ProductMoneyAmount();
		// 流水号UUID
		moneyAmount.setSerialNum(serialNum);
		// 币种ID
		moneyAmount.setCurrencyId(payCurrencyId);
		// 金额
		moneyAmount.setAmount(new Double(payCurrencyPrice));

		productMoneyAmountService.saveOrUpdateMoneyAmount(moneyAmount, orderType);
	}

	/**
	 * 保存金额
	 * 
	 * @param serialNum
	 * @param payCurrencyIds
	 * @param payCurrencyPrice
	 */
	private void saveMoneyAmount(String serialNum, String[] payCurrencyIds,
			String[] payCurrencyPrice, OrderPayDetail orderPayDetail) {

		List<MoneyAmount> moneyAmountList = new ArrayList<MoneyAmount>();

		Integer busindessType = null;
		
		//452-- 其他收入收款汇率优化  modify by wangyang  2016.7.11
		Long costRecordId = Long.valueOf(orderPayDetail.getCostRecordId());
		BigDecimal exchangerate = null;
		if (costRecordId != 0) {
			exchangerate = costRecordDao.findOne(costRecordId).getRate();
		}
		
		for (int i = 0; i < payCurrencyIds.length; i++) {
			MoneyAmount moneyAmount = new MoneyAmount();
			// 流水号UUID
			moneyAmount.setSerialNum(serialNum);
			// 币种ID
			moneyAmount.setCurrencyId(Integer.parseInt(payCurrencyIds[i]));
			// 金额
			moneyAmount.setAmount(new BigDecimal(payCurrencyPrice[i]));
			// 订单ID、游客ID或团期ID
			busindessType = orderPayDetail.getBusindessType();
			if (busindessType == null || busindessType.intValue() == 1) {
				// 保存订单ID
				moneyAmount.setUid(orderPayDetail.getOrderId());
			} else if (busindessType.intValue() == 2) {
				// 保存游客ID
				moneyAmount.setUid(orderPayDetail.getTravelerId());
			} else if (busindessType.intValue() == 4) {
				// 保存团期ID
				Integer groupId = orderPayDetail.getGroupId();
				moneyAmount.setUid(groupId == null ? null:groupId.longValue());
			}

			// 款项类型
			moneyAmount.setMoneyType(orderPayDetail.getPayPriceType());
			// 订单产品类型
			moneyAmount.setOrderType(orderPayDetail.getOrderType());
			// 业务类型(1表示订单，2表示游客,3表示询价报价,4表示团期)
			moneyAmount.setBusindessType(busindessType);
			// 记录人ID
			moneyAmount.setCreatedBy(UserUtils.getUser().getId());
			
			//订单支付批量号
			//add by jiachen
			moneyAmount.setOrderPaySerialNum(orderPayDetail.getOrderPaySerialNum());
			//452-- 其他收入收款汇率优化  modify by wangyang  2016.7.11
			if (exchangerate != null) {				
				moneyAmount.setExchangerate(exchangerate);
			}
			
			moneyAmountList.add(moneyAmount);
		}
		moneyAmountService.saveOrUpdateMoneyAmounts(serialNum, moneyAmountList);
	}
	
	/**
	 * 保存金额
	 * 
	 * @param serialNum
	 * @param payCurrencyIds
	 * @param payCurrencyPrice
	 */
	private void saveProductMoneyAmount(String serialNum, String[] payCurrencyIds,String[] payCurrencyPrice, OrderPayDetail orderPayDetail, Integer orderType) {

		List<ProductMoneyAmount> moneyAmountList = new ArrayList<ProductMoneyAmount>();

		Integer businessType = null;

		for (int i = 0; i < payCurrencyIds.length; i++) {
			ProductMoneyAmount moneyAmount = new ProductMoneyAmount();
			// 流水号UUID
			moneyAmount.setSerialNum(serialNum);
			// 币种ID
			moneyAmount.setCurrencyId(Integer.parseInt(payCurrencyIds[i]));
			// 金额
			moneyAmount.setAmount(new Double(payCurrencyPrice[i]));
			// 订单ID、游客ID或团期ID
			businessType = orderPayDetail.getBusindessType();
			if (businessType == null || businessType.intValue() == 1) {
				// 保存订单ID
				moneyAmount.setBusinessUuid(orderPayDetail.getOrderUuid());
			} else if (businessType.intValue() == 2) {
				// 保存游客ID
				moneyAmount.setBusinessUuid(orderPayDetail.getTravelerUuid());
			} else if (businessType.intValue() == 4) {
				// 保存团期ID
				Integer groupId = orderPayDetail.getGroupId();
				moneyAmount.setBusinessUuid(groupId == null ? null:groupId.toString());
			}

			// 款项类型
			moneyAmount.setMoneyType(orderPayDetail.getPayPriceType());
			// 订单产品类型
			moneyAmount.setOrderType(orderPayDetail.getOrderType());
			// 业务类型(1表示订单，2表示游客,3表示询价报价,4表示团期)
			moneyAmount.setBusinessType(businessType);

			moneyAmountList.add(moneyAmount);
		}
		productMoneyAmountService.saveOrUpdateMoneyAmounts(serialNum, moneyAmountList, orderType);
	}

	private void savePayConvert(String serialNum, String[] payCurrencyIds,
			String[] convertLowest) {
		for (int i = 0; i < payCurrencyIds.length; i++) {
			PayConvert pc = new PayConvert();
			pc.setSerialNum(serialNum);
			pc.setCurrencyId(Integer.parseInt(payCurrencyIds[i]));
			pc.setConvertLowest(new BigDecimal(convertLowest[i]));

			payConvertService.save(pc);
		}
	}

	/**
	 * 根据uuid找到订单支付表的主键id,保存到上传资料表中 uuid 订单支付表的付款uuid docIds 资料表的id数组
	 * 
	 * */
	private void saveDocInfoByPayUUID(String uuid, Long[] docIds) {
		if (null == docIds || docIds.length == 0 || null == uuid
				|| "".equals(uuid)) {
			return;
		}
		String paySQL = "select id from orderpay where moneySerialNum = ? ";
		String docinfoSQL = "update docinfo set payOrderId =? where id = ? ";
		List<Object> par = new ArrayList<Object>();
		par.add(uuid);
		List<Map<String, Object>> resultList = travelerDao.findBySql(paySQL,
				Map.class, par.toArray());

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
	 * 取得UUID
	 * 
	 * @return
	 */
	public String getUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 保存输出参数的信息
	 * 
	 * @param payInfoDetail
	 * @param model
	 */
	public void saveModelParameter(PayInfoDetail payInfoDetail, ModelMap model) {
		model.addAttribute("payId", payInfoDetail.getId());
		model.addAttribute("payType", payInfoDetail.getPayType());
		model.addAttribute("payTypeName", payInfoDetail.getPayTypeName());
		model.addAttribute("payTypeId", payInfoDetail.getPayTypeId());
		model.addAttribute("moneySerialNum", payInfoDetail.getMoneySerialNum());
		model.addAttribute("moneyDispStyle", payInfoDetail.getMoneyDispStyle());
		model.addAttribute("payVoucher", payInfoDetail.getPayVoucher());
		model.addAttribute("docInfoList", payInfoDetail.getDocInfoList());
		model.addAttribute("remarks", payInfoDetail.getRemarks());
		
		model.addAttribute("mergePayFlag", payInfoDetail.getMergePayFlag());
		model.addAttribute("mergeMoneySerialNum", payInfoDetail.getMergeMoneySerialNum());
		model.addAttribute("moneyType", payInfoDetail.getMoneyType());
		model.addAttribute("status", payInfoDetail.getStatus());
		model.addAttribute("recordId", payInfoDetail.getRecordId());

		model.addAttribute("payerName", payInfoDetail.getPayerName());
		model.addAttribute("checkNumber", payInfoDetail.getCheckNumber());
		model.addAttribute("invoiceDate", payInfoDetail.getInvoiceDate());

		model.addAttribute("bankName", payInfoDetail.getBankName());
		model.addAttribute("bankAccount", payInfoDetail.getBankAccount());
		model.addAttribute("tobankName", payInfoDetail.getTobankName());
		model.addAttribute("tobankAccount", payInfoDetail.getTobankAccount());
		
		model.addAttribute("drawerName", payInfoDetail.getDrawerName());
		model.addAttribute("drawerAccount",payInfoDetail.getDrawerAccount());
		model.addAttribute("draftAccountedDate", payInfoDetail.getDraftAccountedDate());
		model.addAttribute("payBankName",payInfoDetail.getPayBankName());
		model.addAttribute("payFees", payInfoDetail.getPayFees());
		model.addAttribute("refundDispStyle", payInfoDetail.getRefundDispStyle());

		model.addAttribute("fromAlipayName",payInfoDetail.getFromAlipayName());
		model.addAttribute("fromAlipayAccount",payInfoDetail.getFromAlipayAccount());
		model.addAttribute("receiveCompanyName",payInfoDetail.getReceiveCompanyName());
		model.addAttribute("toAlipayName",payInfoDetail.getToAlipayName());
		model.addAttribute("toAlipayAccount",payInfoDetail.getToAlipayAccount());
	}

	/**
	 * 复制指定ID的上传文件并保存该复制数据至表中
	 * 
	 * @param docInfoId
	 * @return
	 * @throws IOException
	 */
	private Long copySaveUploadInfo(Long docInfoId) throws IOException {
		// 根据ID取得相应的信息
		DocInfo docInfo = docInfoDao.findOne(docInfoId);
		String docPath = docInfo.getDocPath();
		File srcFile = new File(Global.getBasePath() + File.separator + docPath);
		String path = FilenameUtils.getFullPath(srcFile.getAbsolutePath());
		String ext = FilenameUtils.getExtension(srcFile.getName()).toLowerCase(
				Locale.ENGLISH);
		// 使用唯一标识码生成文件名
		String newFilePath = path + this.getUUID() + "."
				+ (ext != null ? ext : "");
		// 复制并保存上传文件
		FileCopyUtils.copy(srcFile, new File(newFilePath));

		// 保存相应的上传文件信息
		DocInfo doc = new DocInfo();
		doc.setDocName(docInfo.getDocName());
		doc.setDocPath(newFilePath.replace(
				new File(Global.getBasePath()).getAbsolutePath()
						+ File.separator, ""));
		Long docId = docInfoService.saveDocInfo(doc).getId();
		return docId;
	}

	/**
	 * 撤消支付确认操作
	 * 
	 * @param payId
	 * @param payType
	 *            1：收款；2：付款
	 * @throws Exception 
	 * @throws PositionOutOfBoundException 
	 * @throws OptimisticLockHandleException 
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void cancelConfirmOper(String payId, String payType) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
		Orderpay orderpay = orderpayDao.findOne(Long.parseLong(payId));
		//update by shijun.liu 2016-03-01 如果已经撤销则不再进行第二次撤销，解决订单收款，开两个页面进行撤销的问题
		if(null != orderpay.getIsAsAccount() && 
				Context.ORDERPAY_ACCOUNT_STATUS_YCX == orderpay.getIsAsAccount()){
			return;
		}
		orderpay.setIsAsAccount(Context.ORDERPAY_ACCOUNT_STATUS_YCX);
		//wxw 20150817  added 进行驳回操作时要更新时间
		orderpay.setUpdateDate(new Date());
		//0405 撤销时清空收款确认日期
		orderpay.setReceiptConfirmationDate(null);
		orderpayDao.save(orderpay);

		// 单团撤销：订单达帐金额减去撤销金额
		orderService.DTcancelOrder(orderpay);

		moneyAmountDao.updateMoneyType(orderpay.getMoneySerialNum(), Context.MONEY_TYPE_CANCEL);
	}

	/**
	 * 驳回支付确认操作
	 * 
	 * @param payId
	 * @param payType
	 *            1：收款；2：付款
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void rejectConfirmOper(String payId, String payType, String reason) {
		Orderpay orderpay = orderpayDao.findOne(Long.parseLong(payId));
		orderpay.setIsAsAccount(Context.ORDERPAY_ACCOUNT_STATUS_YBH);
		//wxw added 20150817 进行驳回操作更新时间
		orderpay.setUpdateDate(new Date());
		orderpay.setRejectReason(reason);
		orderpayDao.save(orderpay);

		moneyAmountDao.updateMoneyType(orderpay.getMoneySerialNum(),
				Context.MONEY_TYPE_REJECT);
	}

	/**
	 * 将数组拼接成SQL文可识别的字符串
	 * @param arr
	 * @return
	 */
	private String convertArr2Str(String[] arr) {
		StringBuffer sb = new StringBuffer();
		for (String s : arr) {
			sb.append(s);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}

	/**
	 * 判断金额部分的汇率项目是否显示
	 * @param payType
	 * @return
	 */
	public String getConvertFlag(String payType) {
		if ("1".equals(payType)) {
			return "0";
		}
		Long companyId = UserUtils.getUser().getCompany().getId();
		// 新行者的场合
		if ("71".equals(companyId.toString())) {
			return "1";
		} else if("92".equals(companyId.toString())){
			return "1";
		}
		return "0";
	}
	
	public Map<String, Object> sumMoney(String[] priceList,
			BigDecimal[] convertList) {

		Map<String, Object> map = new HashMap<String, Object>();

		BigDecimal[] subSum = new BigDecimal[priceList.length];
		for (int i = 0; i < priceList.length; i++) {
			BigDecimal bd = (new BigDecimal(priceList[i]))
					.multiply(convertList[i]);
			subSum[i] = bd.setScale(2, BigDecimal.ROUND_UP);
		}

		BigDecimal totalSum = new BigDecimal(0);
		for (int i = 0; i < subSum.length; i++) {
			totalSum = totalSum.add(subSum[i]);
		}
		map.put("subSum", subSum);
		map.put("totalSum", totalSum);

		return map;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * 共用部分
	 */
	// ////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 拼接数组字符串
	 * 
	 * @param arr
	 * @return
	 */
	public String contactArrayVal(String[] arr) {
		StringBuffer sb = new StringBuffer();

		sb.append("[");
		for (String str : arr) {
			sb.append(str);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");

		return sb.toString();
	}
	
	public void processNegativeMoney(OrderPayDetail orderPayDetail,
			String moneyStr) {

		String[] moneys = moneyStr.split(",");

//		for (int i = 0; i < moneys.length; i++) {
//			String money = moneys[i];
//			if ((new BigDecimal(money)).compareTo(new BigDecimal(0)) < 0) {
//				moneys[i] = "0";
//			}
//		}

		String payCurrencyPrice = contactMoneyVal(moneys);

		orderPayDetail.setPayCurrencyPrice(payCurrencyPrice);
	}
	
	private String contactMoneyVal(String[] arr) {
		StringBuffer sb = new StringBuffer();

		for (String str : arr) {
			sb.append(str);
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);

		return sb.toString();
	}
	
	
	/**
	 * 取得支付时所需要的汇率信息
	 * @param recordId
	 * @param currencyIds
	 * @return
	 */
	public BigDecimal[] getConvertLowest(Long recordId, Integer moneyType,
			String[] currencyIds) {

		BigDecimal[] convertList = null;

		List<Refund> refundList = refundDao.findByRecordIdWithOrder(recordId,
				moneyType);
		if (refundList == null || refundList.size() == 0) {
			convertList = currencyService
					.findConvertLowestByIds(convertArr2Str(currencyIds));
			return convertList;
		}

		Refund refund = refundList.get(0);
		if ("0".equals(refund.getMergePayFlag())) {
			return null;
		}
		List<PayConvert> payConvertList = payConvertService
				.findInfoBySerialNum(refund.getMoneySerialNum());

		if (payConvertList == null || payConvertList.size() == 0) {
			return null;
		}
		convertList = new BigDecimal[payConvertList.size()];

		for (int i = 0; i < payConvertList.size(); i++) {
			PayConvert pc = payConvertList.get(i);
			convertList[i] = pc.getConvertLowest();
		}

		return convertList;
	}
	
	/**
	 * 是否合并支付Flag取得
	 * @param recordId
	 * @param moneyType
	 * @return
	 */
	public String getMergePayFlag(Long recordId, Integer moneyType) {
		List<Refund> refundList = refundDao.findByRecordIdWithOrder(recordId,
				moneyType);
		if (refundList == null || refundList.size() == 0) {
			return "0";
		}

		Refund refund = refundList.get(0);
		return refund.getMergePayFlag();
	}
	
	/**
	 * 汇率是否可用
	 * @param recordId
	 * @param moneyType
	 * @return
	 */
	public String getConvertUseFlag(Long recordId, Integer moneyType) {
		List<Refund> refundList = refundDao.findByRecordIdWithOrder(recordId,
				moneyType);
		if (refundList == null || refundList.size() == 0) {
			return "0";
		}

		return "1";
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	/*
	 * 撤消/驳回功能部分
	 */
	// ////////////////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * 撤消支付确认操作（主要是其他功能的controller或service来调用）
	 * 
	 * @param payId
	 * @param payType
	 *            1：收款；2：付款
	 */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void cancelOper(String payId, String payType) {
		Refund refund = refundDao.findPayInfoByPayId(payId);
		refund.setStatus("0");
		refundDao.save(refund);

		moneyAmountDao.updateMoneyType(refund.getMoneySerialNum(),
				Context.MONEY_TYPE_CANCEL);
	}
}
