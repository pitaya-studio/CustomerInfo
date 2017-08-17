package com.trekiz.admin.modules.finance.result;

import java.util.Date;

/**
 * 服务费付款列表查询返回的结果映射对象
 * @author  yudong.xu
 * @date    2016.09.2
 */
public class ServiceChargePayListResult {

    private String settleName; // 结算方
    private Object agentId; // 结算方(渠道)id,使用Object类型是因为Hibernate转换一会儿Integer，一会儿BigInteger。

    private Integer orderType; // 团队类型
    private String groupCode; // 团号
    private String groupName; // 团队名称
    private String orderNum; // 订单号

    private String jd; // 计调
    private String saler; // 销售
    private String creator; // 下单人

    private String payMoney; // 付款金额
    private String accountedMoney; // 已付金额

    private Short chargeType; // 收费类型，0是QUAUQ服务费，1是总社服务费。

    private Short payStatus; // 出纳确认状态
    private Date payTime; // 确认付款时间
    private Short printStatus; // 打印状态
    private Date printTime; // 打印时间

    private Integer chargeId; // 结算表的id
    private Integer groupId; // 团期表的id
    private Integer orderId; // 订单表的id
    private Integer productId; // 产品表的id

    private String currencyIds; // 应付金额的对应的币种id,用逗号隔开
    private String amounts; // 对应币种的应付金额,用逗号隔开

    private Date updateTime; // 更新时间,排序使用
    private Date createTime; // 创建时间,排序使用

    public String getSettleName() {
        return settleName;
    }

    public void setSettleName(String settleName) {
        this.settleName = settleName;
    }

    public Object getAgentId() {
        return agentId;
    }

    public void setAgentId(Object agentId) {
        this.agentId = agentId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getJd() {
        return jd;
    }

    public void setJd(String jd) {
        this.jd = jd;
    }

    public String getSaler() {
        return saler;
    }

    public void setSaler(String saler) {
        this.saler = saler;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public String getAccountedMoney() {
        return accountedMoney;
    }

    public void setAccountedMoney(String accountedMoney) {
        this.accountedMoney = accountedMoney;
    }

    public Short getChargeType() {
        return chargeType;
    }

    public void setChargeType(Short chargeType) {
        this.chargeType = chargeType;
    }

    public Short getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Short payStatus) {
        this.payStatus = payStatus;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Short getPrintStatus() {
        return printStatus;
    }

    public void setPrintStatus(Short printStatus) {
        this.printStatus = printStatus;
    }

    public Date getPrintTime() {
        return printTime;
    }

    public void setPrintTime(Date printTime) {
        this.printTime = printTime;
    }

    public Integer getChargeId() {
        return chargeId;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getCurrencyIds() {
        return currencyIds;
    }

    public void setCurrencyIds(String currencyIds) {
        this.currencyIds = currencyIds;
    }

    public String getAmounts() {
        return amounts;
    }

    public void setAmounts(String amounts) {
        this.amounts = amounts;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
