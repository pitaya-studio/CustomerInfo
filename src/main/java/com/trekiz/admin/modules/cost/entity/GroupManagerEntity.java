package com.trekiz.admin.modules.cost.entity;

/**
 * 团队管理实体类
 * @author shijun.liu
 *
 */
public class GroupManagerEntity {

	private Long groupId;          //团ID
	private String groupCode;      //团号
	private Long productId;        //产品ID
	private String productName;    //产品名称
	private Integer supplyId;      //供应商ID
	private Integer agentId;       //渠道商ID
	private Integer supplyType;    //供应商/渠道商类别
	private String supplyName;     //供应商/渠道商名称
	private String operator;       //计调
	private Integer operatorId;    //计调ID
	private String saler;			//销售
	private Integer salerId;		//销售ID
	private String costName;       //款项
	private Integer quantity;      //数量
	private String currencyName;   //币种
	private String comment;        //备注
	private String totalMoney;     //应收金额
	private String actualMoney;    //头部应付金额
	private String detailPayedMoney; //明细应付金额
	private String profit;           //利润
	private String profitRate;       //毛利率
	private String activityUuid;	//团期uuid
	private String productUuid;	//产品uuid
	private String lockStatus;	//结算单锁
	private String forcastStatus;	//预报单锁
	private String orderType;	//团队类型
	private String iscommission; //提成状态
	private String groupOpenDate;	//出团日期--开始
	private String groupCloseDate;	//出团日期--结束
	private Integer deptId;	//部门id
	private Integer personNum;	//人数
	private String realMoney;			//实收金额
	private String realPayMoney;		//实付金额
	private String realProfit;			//实际利润
	private String realProfitRate;      //实际毛利率

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getSupplyId() {
		return supplyId;
	}

	public void setSupplyId(Integer supplyId) {
		this.supplyId = supplyId;
	}

	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public Integer getSupplyType() {
		return supplyType;
	}

	public void setSupplyType(Integer supplyType) {
		this.supplyType = supplyType;
	}

	public String getSupplyName() {
		return supplyName;
	}

	public void setSupplyName(String supplyName) {
		this.supplyName = supplyName;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}

	public String getSaler() {
		return saler;
	}

	public void setSaler(String saler) {
		this.saler = saler;
	}

	public Integer getSalerId() {
		return salerId;
	}

	public void setSalerId(Integer salerId) {
		this.salerId = salerId;
	}

	public String getCostName() {
		return costName;
	}

	public void setCostName(String costName) {
		this.costName = costName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	public String getActualMoney() {
		return actualMoney;
	}

	public void setActualMoney(String actualMoney) {
		this.actualMoney = actualMoney;
	}

	public String getDetailPayedMoney() {
		return detailPayedMoney;
	}

	public void setDetailPayedMoney(String detailPayedMoney) {
		this.detailPayedMoney = detailPayedMoney;
	}

	public String getProfit() {
		return profit;
	}

	public void setProfit(String profit) {
		this.profit = profit;
	}

	public String getProfitRate() {
		return profitRate;
	}

	public void setProfitRate(String profitRate) {
		this.profitRate = profitRate;
	}

	public String getActivityUuid() {
		return activityUuid;
	}

	public void setActivityUuid(String activityUuid) {
		this.activityUuid = activityUuid;
	}

	public String getProductUuid() {
		return productUuid;
	}

	public void setProductUuid(String productUuid) {
		this.productUuid = productUuid;
	}

	public String getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
	}

	public String getForcastStatus() {
		return forcastStatus;
	}

	public void setForcastStatus(String forcastStatus) {
		this.forcastStatus = forcastStatus;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getIscommission() {
		return iscommission;
	}

	public void setIscommission(String iscommission) {
		this.iscommission = iscommission;
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

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getPersonNum() {
		return personNum;
	}

	public void setPersonNum(Integer personNum) {
		this.personNum = personNum;
	}

	public String getRealMoney() {
		return realMoney;
	}

	public void setRealMoney(String realMoney) {
		this.realMoney = realMoney;
	}

	public String getRealPayMoney() {
		return realPayMoney;
	}

	public void setRealPayMoney(String realPayMoney) {
		this.realPayMoney = realPayMoney;
	}

	public String getRealProfit() {
		return realProfit;
	}

	public void setRealProfit(String realProfit) {
		this.realProfit = realProfit;
	}

	public String getRealProfitRate() {
		return realProfitRate;
	}

	public void setRealProfitRate(String realProfitRate) {
		this.realProfitRate = realProfitRate;
	}
}
