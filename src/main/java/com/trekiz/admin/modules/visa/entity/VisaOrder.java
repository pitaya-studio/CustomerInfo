package com.trekiz.admin.modules.visa.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.trekiz.admin.common.mapper.CustomDateSerializer;
import com.trekiz.admin.common.persistence.DataEntityTTS;
import com.trekiz.admin.common.utils.StringUtils;
/**
 * 签证订单类
 * @author Administrator
 *
 */
@Entity
@Table(name = "visa_order")
public class VisaOrder extends DataEntityTTS {
	
	private static final long serialVersionUID = 1L;
	
	/** 主键ID */
	private Long id;
	
	/** 团期号 */
	private Long activityCode;
	
	/** 所属地接订单 的ID (仅当签证订单不是单签类型时，该属性有效)*/
	private Long groupId;
	
	/** 预订渠道ID */
	private Long agentinfoId;
	
	/** 预订渠道名称 */
	private String agentinfoName;
	
	/** 签证订单类型 （关联产品分类表 ProductType）*/
	private Long productTypeID;
	
	/** 产品ID （关联产品表） */
	private Long visaProductId;
	
	/** 游客人数 */
	private Integer travelNum;
	
	/** 签证订单状态   0：未支付;1:已支付;2:已取消;100:订单创建中（创建未完成，不能使用）*/	
	private Integer visaOrderStatus;

	/** 备注 */
	private String remark;
	

	//---------------------
	/**
	 * 主订单编号
	 */
	//private Integer mainOrderCode;

	/**
	 * 订单总价
	 */
	private String totalMoney;
	/**
	 * 已付金额
	 */
	private String payedMoney;
	/**
	 * 到账金额
	 */
	private String accountedMoney;
	
	/**
	 * 订单编号
	 */
	private String orderNo;
	/**
	 * 订单团号
	 */
	private String groupCode;
	
	/**
	 * '支付状态1-未支付全款 2-未支付订金 3-已占位 4-已支付订金 5-已支付全款 99-已取消'
	 */
	private Integer payStatus;
	
	
	/** 币种ID（关联币种表 currency） */
	private Integer proOriginCurrencyId;
	
	/** 应收价格 */
	private BigDecimal proOriginVisaPay;
	
	
	/**
	 * 订单原始总价总价 alt shift s
	 */
	private String originalTotalMoney;
	//锁定状态
	private Integer lockStatus;
	//激活时间
	private Date activationDate;
	//签证订单总成本价
	private String costTotalMoney;
	
	//签证订单借款UUID
	private String jkSerialnum;
	//订单是否已读
	private String isRead = "0";
	
	//订单取消前支付状态
	private Integer oldPayStatus;
	
	//团队返佣UUID
	private String groupRebate;
	
	//销售Id
	private Integer salerId;
	//销售名称
	private String salerName;
	//其他产品线关联生成签证订单时对应的订单编号
	private String mainOrderId;
	
	/**
	 * wxw added 2016-01-04   对应需求号c457,c486
	 * 预定时产品预报单锁定情况--0：未锁定（默认），1：锁定
	 */
	private Character forecastLockedIn;
	/** 确认单文件ID */
    private String confirmationFileId;
    private String downloadFileIds;
	
	private Boolean confirmFlag;
	
	/**
	 * wxw added 2016-01-04   对应需求号c457,c486
	 * 预定时产品结算单锁定情况--0：未锁定（默认），1：锁定
	 */
	private Character settleLockedIn;
	
	
    public String getMainOrderId() {
		return mainOrderId;
	}

	public void setMainOrderId(String mainOrderId) {
		this.mainOrderId = mainOrderId;
	}

	/**    
     * isRead      
     */
    @Column(name="isRead")
	public String getIsRead() {
		return isRead;
	}

	public void setIsRead(String isRead) {
		this.isRead = isRead;
	}

	/**    
     * activationDate 
     */
	@Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = CustomDateSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name="activationDate")
    public Date getActivationDate() {
        return activationDate;
    }

    /**    
     * @param activationDate   
     */
    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    /**    
     * lockStatus      
     */
    @Column(name="lockStatus")
    public Integer getLockStatus() {
        return lockStatus;
    }

    /**    
     * @param lockStatus  
     */
    public void setLockStatus(Integer lockStatus) {
        this.lockStatus = lockStatus;
    }

    @Column(name="payStatus")
	public Integer getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public VisaOrder() {
		super();
	}

	public VisaOrder(Long id){
		this();
		this.id = id;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="visa_product_id")
	public Long getVisaProductId() {
		return visaProductId;
	}

	public void setVisaProductId(Long visaProductId) {
		this.visaProductId = visaProductId;
	}

	@Column(name="travel_num")
	public Integer getTravelNum() {
		return travelNum;
	}

	public void setTravelNum(Integer travelNum) {
		this.travelNum = travelNum;
	}

	@Column(name="visa_order_status")
	public Integer getVisaOrderStatus() {
		return visaOrderStatus;
	}

	public void setVisaOrderStatus(Integer visaOrderStatus) {
		this.visaOrderStatus = visaOrderStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name="activity_code")
	public Long getActivityCode() {
		return activityCode;
	}

	public void setActivityCode(Long activityCode) {
		this.activityCode = activityCode;
	}
	
	@Column(name="group_id")
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Column(name="agentinfo_id")
	public Long getAgentinfoId() {
		return agentinfoId;
	}

	public void setAgentinfoId(Long agentinfoId) {
		this.agentinfoId = agentinfoId;
	}

	@Column(name="product_type_id")
	public Long getProductTypeID() {
		return productTypeID;
	}

	public void setProductTypeID(Long productTypeID) {
		this.productTypeID = productTypeID;
	}
	
	//金额相关
	@Column(name="total_money")
	public String getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(String totalMoney) {
		this.totalMoney = totalMoney;
	}

	@Column(name="payed_money")
	public String getPayedMoney() {
		return payedMoney;
	}

	public void setPayedMoney(String payedMoney) {
		this.payedMoney = payedMoney;
	}

	@Column(name="accounted_money")
	public String getAccountedMoney() {
		return accountedMoney;
	}

	public void setAccountedMoney(String accountedMoney) {
		this.accountedMoney = accountedMoney;
	}

	@Column(name="order_no")
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@Column(name="group_code")
	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

/*	@Column(name="main_order_code")
	public Integer getMainOrderCode() {
		return mainOrderCode;
	}

	public void setMainOrderCode(Integer mainOrderCode) {
		this.mainOrderCode = mainOrderCode;
	}*/

	@Column(name="original_total_money")
	public String getOriginalTotalMoney() {
		return originalTotalMoney;
	}

	public void setOriginalTotalMoney(String originalTotalMoney) {
		this.originalTotalMoney = originalTotalMoney;
	}

	@Column(name="visapro_origin_currencyid")
	public Integer getProOriginCurrencyId() {
		return proOriginCurrencyId;
	}

	public void setProOriginCurrencyId(Integer proOriginCurrencyId) {
		this.proOriginCurrencyId = proOriginCurrencyId;
	}

	@Column(name="visapro_origin_pay")
	public BigDecimal getProOriginVisaPay() {
		return proOriginVisaPay;
	}

	public void setProOriginVisaPay(BigDecimal proOriginVisaPay) {
		this.proOriginVisaPay = proOriginVisaPay;
	}


	@Column(name="agentinfo_name")
	public String getAgentinfoName() {
		return agentinfoName;
	}

	public void setAgentinfoName(String agentinfoName) {
		this.agentinfoName = agentinfoName;
	}
	
	@Column(name="cost_total_money") 
	public String getCostTotalMoney() {
		return costTotalMoney;
	}

	public void setCostTotalMoney(String costTotalMoney) {
		this.costTotalMoney = costTotalMoney;
	}

	@Column(name="jk_serialnum") 
	public String getJkSerialnum() {
		return jkSerialnum;
	}

	public void setJkSerialnum(String jkSerialnum) {
		this.jkSerialnum = jkSerialnum;
	}

	@Column(name="oldPayStatus") 
	public Integer getOldPayStatus() {
		return oldPayStatus;
	}

	public void setOldPayStatus(Integer oldPayStatus) {
		this.oldPayStatus = oldPayStatus;
	}
	

	@Column(name="groupRebate") 
	public String getGroupRebate() {
		return groupRebate;
	}

	public void setGroupRebate(String groupRebate) {
		this.groupRebate = groupRebate;
	}
	
	@Column(name="salerId") 
	public Integer getSalerId() {
		return salerId;
	}

	public void setSalerId(Integer salerId) {
		this.salerId = salerId;
	}
	
	@Column(name="salerName") 
	public String getSalerName() {
		return salerName;
	}

	public void setSalerName(String salerName) {
		this.salerName = salerName;
	}

	@Column(name="forecast_locked_in")
	public Character getForecastLockedIn() {
		return forecastLockedIn;
	}

	public void setForecastLockedIn(Character forecastLockedIn) {
		this.forecastLockedIn = forecastLockedIn;
	}

	
	@Column(name="settle_locked_in")
	public Character getSettleLockedIn() {
		return settleLockedIn;
	}

	public void setSettleLockedIn(Character settleLockedIn) {
		this.settleLockedIn = settleLockedIn;
	}

	public String getConfirmationFileId() {
		return confirmationFileId;
	}

	public void setConfirmationFileId(String confirmationFileId) {
		this.confirmationFileId = confirmationFileId;
	}

	public String getDownloadFileIds() {
		return downloadFileIds;
	}

	public void setDownloadFileIds(String downloadFileIds) {
		this.downloadFileIds = downloadFileIds;
	}

	public Boolean getConfirmFlag() {
		return confirmFlag;
	}

	public void setConfirmFlag(Boolean confirmFlag) {
		this.confirmFlag = confirmFlag;
	}

	@Override
	public void prePersist() {
		if(StringUtils.isBlank(this.confirmationFileId) && StringUtils.isBlank(this.downloadFileIds)){
			this.confirmFlag = false;
		}else if(!StringUtils.isBlank(this.confirmationFileId) && !StringUtils.isBlank(this.downloadFileIds) && this.confirmationFileId.equals(this.downloadFileIds)){
			this.confirmFlag = false;
		}else{
			this.confirmFlag = true;
		}
		super.prePersist();
	}

//	@PreUpdate
	@Override
	public void preUpdate() {
		if(StringUtils.isBlank(this.confirmationFileId) && StringUtils.isBlank(this.downloadFileIds)){
			this.confirmFlag = false;
		}else if(!StringUtils.isBlank(this.confirmationFileId) && !StringUtils.isBlank(this.downloadFileIds) && this.confirmationFileId.equals(this.downloadFileIds)){
			this.confirmFlag = false;
		}else{
			this.confirmFlag = true;
		}
		super.preUpdate();
	}
	
}
