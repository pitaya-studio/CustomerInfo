package com.trekiz.admin.modules.visa.form;

import java.util.List;


/***
 * 签务和销售身份管理订单功能
 * 页面查询 结果类的子类
 * 游客信息结果集
 * wenjianye
 * 2014-12-03
 * 
 * */
public class VisaOrderTravelerResultForm {

		/**游客信息结果集**/
	//游客所在订单是否展示"月结付费"的标示位
	private String paymentTypeFlag;
	//游客支付类型的标签
	private String paymentType;
	//游客参团后的参团团号
	private String  cantuantuanhao;
	//游客参团后的主订单id
	private String  mainOrderId;
	//游客参团后的主订单号
	private String mainOrderNum;
	//借款状态(1:未借2:已借3:已还)
	private String jiekuanStatus;
	//订单付款总金额金额列表
	private String totalOrderJine;
	//订单付款总金额货币编号列表
	private String totalOrderHuobi;
	//交押金总金额金额列表
	private String totalYajinJine;
	//交押金总金额货币编号列表
	private String totalYajinHuobi;
	//付款按钮 显示 与 隐藏
	private String fukuanButtonFlag;
	//付款 货币id列表
	private String fukuanhuobiId;
	//付款 金额列表
	private String fukuanjine;
	//游客所属订单参团类型ID
	private String groupTypeId;
	//游客所属订单参团类型
	private String groupType;
	//游客所属订单下单人
	private String creatUser;
	//游客所属订单下单时间
	private String createTime;
	//游客所属订单更新时间
	private String updateDate;
	//游客所属订单是否处于锁定状态
	private String orderStatus;
	//游客所属visa_order表id
	private String visaorderId;
	//游客所属visa_order表的订单编号
	private String visaorderNo;
	//游客所属visa_order表agentId
	private String agentinfoId;
	//交押金按钮 显示 与 隐藏
	private String payButtonFlag;
	//货币id列表
	private String huobiId;
	//金额列表
	private String jine;
	//签证产品的产品id
	private String visaProductId;
	//visa表id
	private String visaId;
	//游客id
	private String id;
	//姓名
	private String travelerName;
	//护照号
	private String	passportId;
	//AA码
	private String	AACode ="";
	//签证类别
	private String	visaType;
	//签证国家
	private String	visaCountry;
	//领区
	private String collarZoning;
	
	//预定出团时间
	private String	forecastStartOut;
	//预计约签时间
	private String	forecastContract;
	//实际出团时间
	private String	startOut;
	//实际约签时间
	private String	contract;
	//实际送签时间
	private String deliveryTime;
	//签证状态
	private String	visaStatus;
	//护照状态
	private String	passportStatus;
	//担保
	private String	guaranteeStatus;
	//应收押金
	private String	totalDeposit;
	//达账押金
	private String	accountedDeposit;
	//应收押金uuid
	private String	totalDepositUUID;
	//借款币种
	private String jiekuanBizhong;
	//借款金额
	private String jiekuanAmount;
	//借款创建者
	private String jiekuanCreateUser;
	//借款备注
	private String jiekuanRemarks;
	//借款时间
	private String jiekuanTime;

	//是否需要展示付款按钮
	private String showFlag = "T";
	//锁定状态
	private String activityLockStatus;
	
	//应收金额
	private String yingshouMoney;
	//应收金额币种id
	private String yingshouCurrencyId;
	//应收金额多币种：
	private List<String> markAndMoney;
	//已付金额
	private String yifuMoney;
	//已付金额币种id
	private String yifuCurrencyId;
	//已付金额多币种：
	private List<String> yfmarkAndMoney;
	//达账金额
	private String dazhangMoney;
	//达账金额币种id
	private String dazhangCurrencyId;
	//达账金额多币种：
	private List<String> dzmarkAndMoney;
	//付款状态
	private String fukuanStatus;
	//预计返佣
	private String yujiRebates;
		
	/**
	 *获取限制标志
	 * */
	public String getShowFlag() {
		return showFlag;
	}
	/**
	 *设置限制标志
	 * */
	public void setShowFlag(String showFlag) {
		this.showFlag = showFlag;
	}
		
	/***按钮显示状态设置**/
	/**
	 *获得姓名 
	 * */
	public String getTravelerName() {
		return travelerName;
	}
	/**
	 *设置姓名
	 * */
	public void setTravelerName(String travelerName) {
		this.travelerName = travelerName;
	}
	/**
	 *获得护照Id
	 * */
	public String getPassportId() {
		return passportId;
	}
	/**
	 *设置护照ID
	 * */
	public void setPassportId(String passportId) {
		this.passportId = passportId;
	}
	/**
	 *获得AA码
	 * */
	public String getAACode() {
		return AACode;
	}
	/**
	 *设置AA码
	 * */
	public void setAACode(String aACode) {
		AACode = aACode;
	}
	/**
	 *获得签证类型
	 * */
	public String getVisaType() {
		return visaType;
	}
	/**
	 *设置签证类型
	 * */
	public void setVisaType(String visaType) {
		this.visaType = visaType;
	}
	/**
	 *获取签证国家
	 * */
	public String getVisaCountry() {
		return visaCountry;
	}
	/**
	 *设置签证国家
	 * */
	public void setVisaCountry(String visaCountry) {
		this.visaCountry = visaCountry;
	}
	/**
	 *获得预定出团时间
	 * */
	public String getForecastStartOut() {
		return forecastStartOut;
	}
	/**
	 *设置预定出团时间
	 * */
	public void setForecastStartOut(String forecastStartOut) {
		this.forecastStartOut = forecastStartOut;
	}
	/**
	 *获取预计约签时间
	 * */
	public String getForecastContract() {
		return forecastContract;
	}
	/**
	 *设置预计约签时间
	 * */
	public void setForecastContract(String forecastContract) {
		this.forecastContract = forecastContract;
	}
	/**
	 *获得预计约签时间
	 * */
	public String getStartOut() {
		return startOut;
	}
	/**
	 *设置实际出团时间
	 * */
	public void setStartOut(String startOut) {
		this.startOut = startOut;
	}
	/**
	 *获得实际约签时间
	 * */
	public String getContract() {
		return contract;
	}
	/**
	 *设置实际约签时间
	 * */
	public void setContract(String contract) {
		this.contract = contract;
	}
	/**
	 *获得签证状态
	 * */
	public String getVisaStatus() {
		return visaStatus;
	}
	/**
	 *设置签证状态
	 * */
	public void setVisaStatus(String visaStatus) {
		this.visaStatus = visaStatus;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getVisaorderId() {
		return visaorderId;
	}
	public void setVisaorderId(String visaorderId) {
		this.visaorderId = visaorderId;
	}
	public String getVisaorderNo() {
		return visaorderNo;
	}
	public void setVisaorderNo(String visaorderNo) {
		this.visaorderNo = visaorderNo;
	}
	public String getAgentinfoId() {
		return agentinfoId;
	}
	public void setAgentinfoId(String agentinfoId) {
		this.agentinfoId = agentinfoId;
	}
	/**
	 *获取护照状态
	 * */
	public String getPassportStatus() {
		return passportStatus;
	}
	/**
	 *设置护照状态
	 * */
	public void setPassportStatus(String passportStatus) {
		this.passportStatus = passportStatus;
	}
	/**
	 *获取担保状态
	 * */
	public String getGuaranteeStatus() {
		return guaranteeStatus;
	}
	/**
	 *设置担保状态
	 * */
	public void setGuaranteeStatus(String guaranteeStatus) {
		this.guaranteeStatus = guaranteeStatus;
	}
	public String getPayButtonFlag() {
		return payButtonFlag;
	}
	public void setPayButtonFlag(String payButtonFlag) {
		this.payButtonFlag = payButtonFlag;
	}
	/**
	 *获取应收押金
	 * */
	public String getTotalDeposit() {
		return totalDeposit;
	}
	/**
	 *设置应收押金
	 * */
	public void setTotalDeposit(String totalDeposit) {
		this.totalDeposit = totalDeposit;
	}
	/**
	 *获取达账押金
	 * */
	public String getAccountedDeposit() {
		return accountedDeposit;
	}
	/**
	 *设置达账押金
	 * */
	public void setAccountedDeposit(String accountedDeposit) {
		this.accountedDeposit = accountedDeposit;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVisaId() {
		return visaId;
	}
	public String getGroupTypeId() {
		return groupTypeId;
	}
	public void setGroupTypeId(String groupTypeId) {
		this.groupTypeId = groupTypeId;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	public String getCreatUser() {
		return creatUser;
	}
	public void setCreatUser(String creatUser) {
		this.creatUser = creatUser;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public void setVisaId(String visaId) {
		this.visaId = visaId;
	}
	public String getTotalDepositUUID() {
		return totalDepositUUID;
	}
	public void setTotalDepositUUID(String totalDepositUUID) {
		this.totalDepositUUID = totalDepositUUID;
	}
	public String getVisaProductId() {
		return visaProductId;
	}
	public void setVisaProductId(String visaProductId) {
		this.visaProductId = visaProductId;
	}
	public String getJiekuanBizhong() {
		return jiekuanBizhong;
	}
	public void setJiekuanBizhong(String jiekuanBizhong) {
		this.jiekuanBizhong = jiekuanBizhong;
	}
	public String getHuobiId() {
		return huobiId;
	}
	public void setHuobiId(String huobiId) {
		this.huobiId = huobiId;
	}
	public String getFukuanhuobiId() {
		return fukuanhuobiId;
	}
	public void setFukuanhuobiId(String fukuanhuobiId) {
		this.fukuanhuobiId = fukuanhuobiId;
	}
	public String getFukuanjine() {
		return fukuanjine;
	}
	public void setFukuanjine(String fukuanjine) {
		this.fukuanjine = fukuanjine;
	}
	public String getJine() {
		return jine;
	}
	public String getJiekuanStatus() {
		return jiekuanStatus;
	}
	public void setJiekuanStatus(String jiekuanStatus) {
		this.jiekuanStatus = jiekuanStatus;
	}
	public void setJine(String jine) {
		this.jine = jine;
	}
	public String getMainOrderId() {
		return mainOrderId;
	}
	public void setMainOrderId(String mainOrderId) {
		this.mainOrderId = mainOrderId;
	}
	public String getMainOrderNum() {
		return mainOrderNum;
	}
	public void setMainOrderNum(String mainOrderNum) {
		this.mainOrderNum = mainOrderNum;
	}
	public String getJiekuanAmount() {
		return jiekuanAmount;
	}
	public void setJiekuanAmount(String jiekuanAmount) {
		this.jiekuanAmount = jiekuanAmount;
	}
	public String getJiekuanCreateUser() {
		return jiekuanCreateUser;
	}
	public void setJiekuanCreateUser(String jiekuanCreateUser) {
		this.jiekuanCreateUser = jiekuanCreateUser;
	}
	public String getJiekuanRemarks() {
		return jiekuanRemarks;
	}
	public void setJiekuanRemarks(String jiekuanRemarks) {
		this.jiekuanRemarks = jiekuanRemarks;
	}
	public String getTotalOrderJine() {
		return totalOrderJine;
	}
	public void setTotalOrderJine(String totalOrderJine) {
		this.totalOrderJine = totalOrderJine;
	}
	public String getTotalOrderHuobi() {
		return totalOrderHuobi;
	}
	public void setTotalOrderHuobi(String totalOrderHuobi) {
		this.totalOrderHuobi = totalOrderHuobi;
	}
	public String getTotalYajinJine() {
		return totalYajinJine;
	}
	public void setTotalYajinJine(String totalYajinJine) {
		this.totalYajinJine = totalYajinJine;
	}
	public String getTotalYajinHuobi() {
		return totalYajinHuobi;
	}
	public String getCantuantuanhao() {
		return cantuantuanhao;
	}
	public void setCantuantuanhao(String cantuantuanhao) {
		this.cantuantuanhao = cantuantuanhao;
	}
	public String getPaymentTypeFlag() {
		return paymentTypeFlag;
	}
	public void setPaymentTypeFlag(String paymentTypeFlag) {
		this.paymentTypeFlag = paymentTypeFlag;
	}
	public void setTotalYajinHuobi(String totalYajinHuobi) {
		this.totalYajinHuobi = totalYajinHuobi;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public String getFukuanButtonFlag() {
		return fukuanButtonFlag;
	}
	public String getJiekuanTime() {
		return jiekuanTime;
	}
	public void setJiekuanTime(String jiekuanTime) {
		this.jiekuanTime = jiekuanTime;
	}
	public void setFukuanButtonFlag(String fukuanButtonFlag) {
		this.fukuanButtonFlag = fukuanButtonFlag;
	}
	public String getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	
	public String getCollarZoning() {
		return collarZoning;
	}
	public void setCollarZoning(String collarZoning) {
		this.collarZoning = collarZoning;
	}

	public String getActivityLockStatus() {
		return activityLockStatus;
	}
	public void setActivityLockStatus(String activityLockStatus) {
		this.activityLockStatus = activityLockStatus;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
	public String getYingshouMoney() {
		return yingshouMoney;
	}
	public void setYingshouMoney(String yingshouMoney) {
		this.yingshouMoney = yingshouMoney;
	}
	public String getYifuMoney() {
		return yifuMoney;
	}
	public void setYifuMoney(String yifuMoney) {
		this.yifuMoney = yifuMoney;
	}
	public String getDazhangMoney() {
		return dazhangMoney;
	}
	public void setDazhangMoney(String dazhangMoney) {
		this.dazhangMoney = dazhangMoney;
	}
	public String getYingshouCurrencyId() {
		return yingshouCurrencyId;
	}
	public void setYingshouCurrencyId(String yingshouCurrencyId) {
		this.yingshouCurrencyId = yingshouCurrencyId;
	}
	public String getYifuCurrencyId() {
		return yifuCurrencyId;
	}
	public void setYifuCurrencyId(String yifuCurrencyId) {
		this.yifuCurrencyId = yifuCurrencyId;
	}
	public String getDazhangCurrencyId() {
		return dazhangCurrencyId;
	}
	public void setDazhangCurrencyId(String dazhangCurrencyId) {
		this.dazhangCurrencyId = dazhangCurrencyId;
	}
	public List<String> getMarkAndMoney() {
		return markAndMoney;
	}
	public void setMarkAndMoney(List<String> markAndMoney) {
		this.markAndMoney = markAndMoney;
	}
	public List<String> getYfmarkAndMoney() {
		return yfmarkAndMoney;
	}
	public void setYfmarkAndMoney(List<String> yfmarkAndMoney) {
		this.yfmarkAndMoney = yfmarkAndMoney;
	}
	public List<String> getDzmarkAndMoney() {
		return dzmarkAndMoney;
	}
	public void setDzmarkAndMoney(List<String> dzmarkAndMoney) {
		this.dzmarkAndMoney = dzmarkAndMoney;
	}
	public String getFukuanStatus() {
		return fukuanStatus;
	}
	public void setFukuanStatus(String fukuanStatus) {
		this.fukuanStatus = fukuanStatus;
	}
	public String getYujiRebates() {
		return yujiRebates;
	}
	public void setYujiRebates(String yujiRebates) {
		this.yujiRebates = yujiRebates;
	}
	
}


