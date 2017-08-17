package com.trekiz.admin.modules.visa.form;

import java.util.ArrayList;
import java.util.List;

/***
 * 签务和销售身份管理订单功能
 * 页面查询 结果类
 * wenjianye
 * 2014-12-03
 * 
 * */
public class VisaOrderResultForm {

	
	/**订单列表 结果集**/
	//订单支付类型的标签
	private String paymentType;
	//签证产品名称
	private String productName;
	//签证订单状态
	private String visaOrderStatus;
	//订单锁死状态
	private String orderStatus;
	//货币id列表
	private String huobiId;
	//金额列表
	private String jine;
	//订单类型的id
	private String  groupTypeId;
	//产品详情url
	private String  productUrl;
	//渠道id
	private String  agentId;
	//产品id
	private String  productId;
	//主订单id
	//private String  mainOrderCode;
	//visa_order的主键
	private String orderId;
	//预定渠道
	private String agentinfoName;
	//订单号
	private String orderCode;
	//领区联系人
	private String contactPerson;
	//产品编号
	private String productCode;
	//参团类型
	private String groupType;
	//下单人
	private String creatUser;
	//下单时间
	private String createTime;
	//人数
	private String travelerCount;
	//应收金额
	private String visaPay;
	//已付总金额
	private String payedMoney;
	//到账总金额
	private String accountedMoney;	
	//客户信息列表
	private List<VisaOrderTravelerResultForm> visaOrderTravelerResultForm = new ArrayList<VisaOrderTravelerResultForm>();
	
	/*****销售管理订单添加   开始*****/
	//订单团号visa_order表的group_code字段
	private String orderTuanhao;
	//参团团号 团期表中的groupcode
	private String cantuanTuanhao;
	//计调 产品发布者
	private String productCreateUser;
	//出团日期
	private String groupOpenDate;
	//截团日期
	private String groupCloseDate;
	//主订单编号
	//private String mainOrderNum;
	//参团的办签人数
	private String cantuanTravelerCount;
	//参团列表数据个数
	private String cantuanPageCount;
	//锁定状态
	private int lockStatus;
	//锁定状态
	private int activityLockStatus;
	//付款状态
	private Integer payStatus;
	//付款按钮 显示 与 隐藏
	private String payButtonFlag;
	//达帐和撤销提示
	private String promptStr;
	
	//是否被查看
	private String isRead;
	//发票状态
	private String invoiceStatus;
	//收据状态
	private String receiptStatus;
	//预计返佣
	private String yujiRebates;
	//实际返佣
	private String shijiRebates;
	//销售名称
	private String salerName;
	
	
	/*****销售管理订单添加   结束*****/
	
	public String getIsRead()
	{
		return isRead;
	}
	public void setIsRead(String isRead)
	{
		this.isRead = isRead;
	}
	/**
	 * 获取渠道编号
	 * */
	public String getAgentinfoName() {
		return agentinfoName;
	}
	/**    
     * payStatus    
     *    
     * @return  the payStatus   
     */
    
    public Integer getPayStatus() {
        return payStatus;
    }
    /**    
     * @param payStatus the payStatus to set    
     */
    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }
    /**    
     * lockStatus    
     *    
     * @return  the lockStatus  
     */
    
    public int getLockStatus() {
        return lockStatus;
    }
    /**    
     * @param lockStatus the lockStatus to set    
     */
    public void setLockStatus(int lockStatus) {
        this.lockStatus = lockStatus;
    }
    public int getActivityLockStatus() {
		return activityLockStatus;
	}
	public void setActivityLockStatus(int activityLockStatus) {
		this.activityLockStatus = activityLockStatus;
	}
	/**
	 * 设置渠道编号
	 * */
	public void setAgentinfoName(String agentinfoName) {
		this.agentinfoName = agentinfoName;
	}
	/**
	 * 得到订单编号
	 * */
	public String getOrderCode() {
		return orderCode;
	}
	/**
	 * 设置订单编号
	 * */
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getVisaOrderStatus() {
		return visaOrderStatus;
	}
	public void setVisaOrderStatus(String visaOrderStatus) {
		this.visaOrderStatus = visaOrderStatus;
	}
	/**
	 * 获得产品编号
	 * */
	public String getProductCode() {
		return productCode;
	}
	/**
	 * 设置产品编号
	 * */
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	/**
	 * 获取参团类型
	 * */
	public String getGroupType() {
		return groupType;
	}
	/**
	 * 设置参团类型
	 * */
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	/**
	 * 获取创建者
	 * */
	public String getCreatUser() {
		return creatUser;
	}
	/**
	 * 设置创建者
	 * */
	public void setCreatUser(String creatUser) {
		this.creatUser = creatUser;
	}
	/**
	 * 获取创建时间
	 * */
	public String getCreateTime() {
		return createTime;
	}
	/**
	 * 设置创建时间
	 * */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获得人数
	 * */
	public String getTravelerCount() {
		return travelerCount;
	}
	/**
	 * 设置人数
	 * */
	public void setTravelerCount(String travelerCount) {
		this.travelerCount = travelerCount;
	}
	/**
	 * 获得应付金额
	 * */
	public String getVisaPay() {
		return visaPay;
	}
	/**
	 * 设置应付金额
	 * */
	public void setVisaPay(String visaPay) {
		this.visaPay = visaPay;
	}
	/**
	 * 获取已付总金额
	 * */
	public String getPayedMoney() {
		return payedMoney;
	}
	/**
	 * 设置已付总金额
	 * */
	public void setPayedMoney(String payedMoney) {
		this.payedMoney = payedMoney;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	/**
	 * 获取到账总金额
	 * */
	public String getAccountedMoney() {
		return accountedMoney;
	}
	/**
	 * 设置到账总金额
	 * */
	public void setAccountedMoney(String accountedMoney) {
		this.accountedMoney = accountedMoney;
	}
	/**
	 * 得到游客列表
	 * */
	public List<VisaOrderTravelerResultForm> getVisaOrderTravelerResultForm() {
		return visaOrderTravelerResultForm;
	}
	/**
	 * 设置游客列表
	 * */
	public void setVisaOrderTravelerResultForm(
			List<VisaOrderTravelerResultForm> visaOrderTravelerResultForm) {
		this.visaOrderTravelerResultForm = visaOrderTravelerResultForm;
	}
	public String getOrderTuanhao() {
		return orderTuanhao;
	}
	public void setOrderTuanhao(String orderTuanhao) {
		this.orderTuanhao = orderTuanhao;
	}
	public String getCantuanTuanhao() {
		return cantuanTuanhao;
	}
	public void setCantuanTuanhao(String cantuanTuanhao) {
		this.cantuanTuanhao = cantuanTuanhao;
	}
	public String getProductCreateUser() {
		return productCreateUser;
	}
	public void setProductCreateUser(String productCreateUser) {
		this.productCreateUser = productCreateUser;
	}
	public String getGroupOpenDate() {
		return groupOpenDate;
	}
	public void setGroupOpenDate(String groupOpenDate) {
		this.groupOpenDate = groupOpenDate;
	}
	public String getGroupCloseDate() {
		return groupCloseDate;
	}
	public void setGroupCloseDate(String groupCloseDate) {
		this.groupCloseDate = groupCloseDate;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getCantuanTravelerCount() {
		return cantuanTravelerCount;
	}
	public void setCantuanTravelerCount(String cantuanTravelerCount) {
		this.cantuanTravelerCount = cantuanTravelerCount;
	}
	public String getProductUrl() {
		return productUrl;
	}
	public String getHuobiId() {
		return huobiId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public void setHuobiId(String huobiId) {
		this.huobiId = huobiId;
	}
	public String getJine() {
		return jine;
	}
	public void setJine(String jine) {
		this.jine = jine;
	}
	public void setProductUrl(String productUrl) {
		this.productUrl = productUrl;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getPayButtonFlag() {
		return payButtonFlag;
	}
	public void setPayButtonFlag(String payButtonFlag) {
		this.payButtonFlag = payButtonFlag;
	}
	public String getCantuanPageCount() {
		return cantuanPageCount;
	}
	public void setCantuanPageCount(String cantuanPageCount) {
		this.cantuanPageCount = cantuanPageCount;
	}
	public String getAgentId() {
		return agentId;
	}
	public String getGroupTypeId() {
		return groupTypeId;
	}
	public String getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	public void setGroupTypeId(String groupTypeId) {
		this.groupTypeId = groupTypeId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	/**
	 * 达帐和撤销提示
	 * */
	public String getPromptStr() {
		return promptStr;
	}
	public void setPromptStr(String promptStr) {
		this.promptStr = promptStr;
	}
	public String getContactPerson() {
		return contactPerson;
	}
	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	/****按钮展示状态控制 开始*****/

	public String getInvoiceStatus() {
		return invoiceStatus;
	}
	public void setInvoiceStatus(String invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}
	public String getReceiptStatus() {
		return receiptStatus;
	}
	public void setReceiptStatus(String receiptStatus) {
		this.receiptStatus = receiptStatus;
	}
	public String getYujiRebates() {
		return yujiRebates;
	}
	public void setYujiRebates(String yujiRebates) {
		this.yujiRebates = yujiRebates;
	}
	public String getShijiRebates() {
		return shijiRebates;
	}
	public void setShijiRebates(String shijiRebates) {
		this.shijiRebates = shijiRebates;
	}
	public String getSalerName() {
		return salerName;
	}
	public void setSalerName(String salerName) {
		this.salerName = salerName;
	}
}


