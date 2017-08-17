package com.trekiz.admin.modules.finance.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 *
 * Copyright 2016 QUAUQ Technology Co. Ltd.
 *
 * 财务模块，结算单的实体类
 * @author shijun.liu
 * @date 2016年05月05日
 */

@Table(name = "finance_settle")
@Entity
public class Settle {
    private Long id;                        //主键id

    private String uuid;                    //唯一标识符uuid
    private Integer orderType;              //订单类型
    private String groupIdUuid;             //团期的id，机票签证的产品id。海岛酒店的uuid。都放入该字段。

    private String operator;                //操作
    private String operatorManagers;        //操作负责人
    private String operatorManagersFull;    //操作负责人全写
    private String salers;                  //销售

    private String productName;			//线路
    private String groupCode;			//团号
    private String personNum;       	//人数
    private String personDay;			//人天数

    private String openCloseDate;       //日期
    private String activityDuration;    //天数
    private String grouplead;           //领队
    private String supplier;			//地接社
    private String price;               //单价(拉美图)
    private String remark;				//备注

    private String invoiceMoney;        //发票税款。懿洋假期
    private String profitAfterTax;      //减去发票税款后的利润。懿洋假期

    //收款列表
    private List<SettleOrder> actualIncome;         //订单列表
    private String totalMoneySum;				    //预计收款合计
    private String backMoneySum;				    //预计退款合计
    private String accountedMoneySum;			    //实际收款合计
    private String notAccountedMoneySum;		    //未收款合计

    //境内付款
    private List<SettleIn> actualInList;		    //成本列表
    private String actualInMoneySum;				//境内付款合计

    //境外付款
    private List<SettleOut> actualOutList;          //成本列表
    private String actualOutMoneySum;				//境外付款合计

    //总合计
    private String realMoneySum;		//实际收入
    private String outMoneySum;		    //支出合计
    private String profitSum;			//毛利
    private String profitRate;          //毛利率

    private Integer version;            //版本号，锁定时对应的结算单改动版本

    private Long createBy;              //创建人(第一次锁定的人)
    private Date createDate;            //创建时间(第一次锁定的时间)
    private Long updateBy;              //更新人
    private Date updateDate;            //更新时间
    
    private String groupRefundSum;//团队退款备注（骡子假期专用）
    
    private List<SettleOther> otherRecordList;//其他收入收款
    
    private String otherSum;//其他收入收款合计
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Column(name = "order_type")
    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getGroupIdUuid() {
        return groupIdUuid;
    }

    public void setGroupIdUuid(String groupIdUuid) {
        this.groupIdUuid = groupIdUuid;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatorManagers() {
        return operatorManagers;
    }

    public void setOperatorManagers(String operatorManagers) {
        this.operatorManagers = operatorManagers;
    }

    public String getOperatorManagersFull() {
        return operatorManagersFull;
    }

    public void setOperatorManagersFull(String operatorManagersFull) {
        this.operatorManagersFull = operatorManagersFull;
    }

    public String getSalers() {
        return salers;
    }

    public void setSalers(String salers) {
        this.salers = salers;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getPersonNum() {
        return personNum;
    }

    public void setPersonNum(String personNum) {
        this.personNum = personNum;
    }

    public String getPersonDay() {
        return personDay;
    }

    public void setPersonDay(String personDay) {
        this.personDay = personDay;
    }

    public String getOpenCloseDate() {
        return openCloseDate;
    }

    public void setOpenCloseDate(String openCloseDate) {
        this.openCloseDate = openCloseDate;
    }

    public String getActivityDuration() {
        return activityDuration;
    }

    public void setActivityDuration(String activityDuration) {
        this.activityDuration = activityDuration;
    }

    public String getGrouplead() {
        return grouplead;
    }

    public void setGrouplead(String grouplead) {
        this.grouplead = grouplead;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInvoiceMoney() {
        return invoiceMoney;
    }

    public void setInvoiceMoney(String invoiceMoney) {
        this.invoiceMoney = invoiceMoney;
    }

    public String getProfitAfterTax() {
        return profitAfterTax;
    }

    public void setProfitAfterTax(String profitAfterTax) {
        this.profitAfterTax = profitAfterTax;
    }

    @Transient      //因为使用uuid作为关联，但是uuid不是主键，无法进行关系映射。所以不映射该字段。后期手动保存数据。
    public List<SettleOrder> getActualIncome() {
        return actualIncome;
    }

    public void setActualIncome(List<SettleOrder> actualIncome) {
        this.actualIncome = actualIncome;
    }

    public String getTotalMoneySum() {
        return totalMoneySum;
    }

    public void setTotalMoneySum(String totalMoneySum) {
        this.totalMoneySum = totalMoneySum;
    }

    public String getBackMoneySum() {
        return backMoneySum;
    }

    public void setBackMoneySum(String backMoneySum) {
        this.backMoneySum = backMoneySum;
    }

    public String getAccountedMoneySum() {
        return accountedMoneySum;
    }

    public void setAccountedMoneySum(String accountedMoneySum) {
        this.accountedMoneySum = accountedMoneySum;
    }

    public String getNotAccountedMoneySum() {
        return notAccountedMoneySum;
    }

    public void setNotAccountedMoneySum(String notAccountedMoneySum) {
        this.notAccountedMoneySum = notAccountedMoneySum;
    }

    @Transient      //因为使用uuid作为关联，但是uuid不是主键，无法进行关系映射。所以不映射该字段。后期手动保存数据。
    public List<SettleIn> getActualInList() {
        return actualInList;
    }

    public void setActualInList(List<SettleIn> actualInList) {
        this.actualInList = actualInList;
    }

    public String getActualInMoneySum() {
        return actualInMoneySum;
    }

    public void setActualInMoneySum(String actualInMoneySum) {
        this.actualInMoneySum = actualInMoneySum;
    }

    @Transient      //因为使用uuid作为关联，但是uuid不是主键，无法进行关系映射。所以不映射该字段。后期手动保存数据。
    public List<SettleOut> getActualOutList() {
        return actualOutList;
    }

    public void setActualOutList(List<SettleOut> actualOutList) {
        this.actualOutList = actualOutList;
    }

    public String getActualOutMoneySum() {
        return actualOutMoneySum;
    }

    public void setActualOutMoneySum(String actualOutMoneySum) {
        this.actualOutMoneySum = actualOutMoneySum;
    }

    public String getRealMoneySum() {
        return realMoneySum;
    }

    public void setRealMoneySum(String realMoneySum) {
        this.realMoneySum = realMoneySum;
    }

    public String getOutMoneySum() {
        return outMoneySum;
    }

    public void setOutMoneySum(String outMoneySum) {
        this.outMoneySum = outMoneySum;
    }

    public String getProfitSum() {
        return profitSum;
    }

    public void setProfitSum(String profitSum) {
        this.profitSum = profitSum;
    }

    public String getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(String profitRate) {
        this.profitRate = profitRate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Column(name = "create_by")
    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    @Column(name = "create_date")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "update_by")
    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "update_date")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

	public String getGroupRefundSum() {
		return groupRefundSum;
	}

	public void setGroupRefundSum(String groupRefundSum) {
		this.groupRefundSum = groupRefundSum;
	}
	@Transient 
	public List<SettleOther> getOtherRecordList() {
		return otherRecordList;
	}

	public void setOtherRecordList(List<SettleOther> otherRecordList) {
		this.otherRecordList = otherRecordList;
	}

	public String getOtherSum() {
		return otherSum;
	}

	public void setOtherSum(String otherSum) {
		this.otherSum = otherSum;
	}
}
