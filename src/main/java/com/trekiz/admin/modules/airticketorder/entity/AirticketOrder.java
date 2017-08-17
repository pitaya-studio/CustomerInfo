
package com.trekiz.admin.modules.airticketorder.entity;

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
 * 机票订单实体类
 * @author wl
 */
@Entity
@Table(name = "airticket_order")
public class AirticketOrder extends DataEntityTTS{

	private static final long serialVersionUID = 1L;
	
	//类型 1-单办 2-参团
	public static final int TYPE_DB=1;
	public static final int TYPE_CT=2;

	//订单方式 0-占位 1-切位
	public static final int PLACEHOLDERTYPE_ZW=0;
	public static final int PLACEHOLDERTYPE_QW=1;
	
	//预定方式 0-正常 1-锁定 2-取消状态3-草稿状态
	public static final int LOCKSTATUS_ZC=0;
	public static final int LOCKSTATUS_SD=1;
	public static final int LOCKSTATUS_QX=2;
	public static final int LOCKSTATUS_CG=3;
	//4-订单未确认状态   2016.6.15 wangyang
	public static final int LOCKSTATUS_WQR = 4;
	
	/**
	 * 非签约渠道名称
	 */
	
	private String nagentName;
	
	public String getNagentName() {
		return nagentName;
	}

	public void setNagentName(String nagentName) {
		this.nagentName = nagentName;
	}
	
	private String nagentcode;
	
	public String getNagentcode() {
		return nagentcode;
	}

	public void setNagentcode(String nagentcode) {
		this.nagentcode = nagentcode;
	}

	/**
	 * 主键id
	 */
	private Long id;
	
	/**
	 * 预定渠道id
	 */
	private Long agentinfoId;
	
	/**
	 * 机票产品id
	 */
	private Long airticketId;
	
	/**
	 * 团期id
	 */
	private Long activitygroupId;
	
	/**
	 * 产品类型id
	 */
	private Long productTypeId;
	
	/**
	 * 订单编号
	 */
	private String orderNo;
	
	/**
	 * 订单状态
	 */
	private Integer orderState;
	
	/**
	 * 已占位订单的确认状态   0:未确认   1:已确认
	 */
    private Integer seizedConfirmationStatus;
	
	/**
	 * 定金金额UUID
	 */
	private String frontMoney;
	/**
	 * 订单总价UUID
	 */
	private String totalMoney;
	
	/**
	 * 订单总结算价UUID
	 */
	private String costTotalMoney;
	
	/**
	 * 团队预定返佣金额uuid
	 */
	private String scheduleBackUuid;
	/** 销售id*/
	private Integer salerId;
	
	/** 销售名称*/
	private String salerName;
		
	/** 确认单文件ID */
    private String confirmationFileId;  //C358屏蔽
	

	@Column(name="cost_total_money")
	public String getCostTotalMoney() {
		return costTotalMoney;
	}
	
	public void setCostTotalMoney(String costTotalMoney) {
		this.costTotalMoney = costTotalMoney;
	}
	
	/**
	 * 已付金额
	 */
	private String payedMoney;
	
	/**
	 * 到账金额
	 */
	private String accountedMoney;
	
	/**
	 * 备注
	 */
	
	private String comments;
	
	/**
	 * 类型 1-单办 2-参团
	 */
	private int type;
	
	/**
	 * 总人数
	 */
	private int personNum;
	/**
	 * 成人数
	 */
	private int adultNum;
	/**
	 * 小孩数
	 */
	private int childNum;
	/**
	 * 特殊人数
	 */
	private int specialNum;
	
	private Integer placeHolderType;
	
	private String groupCode;
	
	private Long mainOrderId;
	private int remaindDays;
	private int lockStatus;
	
	/*原始订金*/
	private String originalFrontMoney;
	
	/*原始应收价*/
	private String originalTotalMoney;
	
	/**
	 * 1-全款 2-定金占位 3-预占位
	 */
	private int occupyType;
	
	/** 激活日期 */
    private Date activationDate;
    
    /**
     * 取消原因
     */
    private String cancelDescription;
    
    /** 预报单锁定后生成的订单数据对应此字段值为1(目前只针对拉美途) */
	private Integer forecastLockedIn;
	
	/** 结算单锁定后生成的订单数据对应此字段值为1(目前只针对拉美途) */
	private Integer settleLockedIn;
	
	/**    
     * activationDate    
     *       
     */
    
    
    
    private Integer paymentStatus = 1;
    
    private Integer seenFlag;  //订单是否被查看状态
    
    private Integer refundFlag;//订单付款状态（目前只针对美途）0、未付全款；1、已付全款
    
    private String downloadFileIds;
	
	private Boolean confirmFlag;

    /**
     * 未付全款
     */
    public static final int REFUNDFLAG_NOT_PAYED = 0;
    /**
     * 已付全款
     */
    public static final int REFUNDFLAG_PAYED = 1;
    
    /** 订单锁定后退款与返佣录入成本值为1*/
//	private String isLockedIn;
    
    @Column(name="paymentStatus")
    public Integer getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(Integer paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

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

    @Column(name="original_front_money")
	public String getOriginalFrontMoney() {
		return originalFrontMoney;
	}

	public void setOriginalFrontMoney(String originalFrontMoney) {
		this.originalFrontMoney = originalFrontMoney;
	}
	@Column(name="original_total_money")
	public String getOriginalTotalMoney() {
		return originalTotalMoney;
	}

	public void setOriginalTotalMoney(String originalTotalMoney) {
		this.originalTotalMoney = originalTotalMoney;
	}

	@Column(name="group_code")
	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	@Column(name="place_holder_type")
	public Integer getPlaceHolderType() {
		return placeHolderType;
	}

	public void setPlaceHolderType(Integer placeHolderType) {
		this.placeHolderType = placeHolderType;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="person_num")
	public int getPersonNum() {
		return personNum;
	}

	public void setPersonNum(int personNum) {
		this.personNum = personNum;
	}
	@Column(name="adult_num")
	public int getAdultNum() {
		return adultNum;
	}

	public void setAdultNum(int adultNum) {
		this.adultNum = adultNum;
	}
	@Column(name="child_num")
	public int getChildNum() {
		return childNum;
	}

	public void setChildNum(int childNum) {
		this.childNum = childNum;
	}
	@Column(name="special_num")
	public int getSpecialNum() {
		return specialNum;
	}

	public void setSpecialNum(int specialNum) {
		this.specialNum = specialNum;
	}
	@Column(name="type")
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	@Column(name="agentinfo_id")
	public Long getAgentinfoId() {
		return agentinfoId;
	}

	public void setAgentinfoId(Long agentinfoId) {
		this.agentinfoId = agentinfoId;
	}
	@Column(name="airticket_id")
	public Long getAirticketId() {
		return airticketId;
	}

	public void setAirticketId(Long airticketId) {
		this.airticketId = airticketId;
	}
	@Column(name="activitygroup_id")
	public Long getActivitygroupId() {
		return activitygroupId;
	}

	public void setActivitygroupId(Long activitygroupId) {
		this.activitygroupId = activitygroupId;
	}
	@Column(name="product_type_id")
	public Long getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(Long productTypeId) {
		this.productTypeId = productTypeId;
	}
	@Column(name="order_no")
	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	@Column(name="order_state")
	public Integer getOrderState() {
		return orderState;
	}

	public void setOrderState(Integer orderState) {
		this.orderState = orderState;
	}
	
	@Column(name="seized_confirmation_status")
	public Integer getSeizedConfirmationStatus() {
		return seizedConfirmationStatus;
	}

	public void setSeizedConfirmationStatus(Integer seizedConfirmationStatus) {
		this.seizedConfirmationStatus = seizedConfirmationStatus;
	}

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
	@Column(name="comments")
	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getOccupyType() {
		return occupyType;
	}

	public void setOccupyType(int occupyType) {
		this.occupyType = occupyType;
	}

	@Column(name="main_order_id")
	public Long getMainOrderId() {
		return mainOrderId;
	}

	public void setMainOrderId(Long mainOrderId) {
		this.mainOrderId = mainOrderId;
	}

	@Column(name="front_money")
	public String getFrontMoney() {
		return frontMoney;
	}

	public void setFrontMoney(String frontMoney) {
		this.frontMoney = frontMoney;
	}

	@Column(name="remaind_days")
	public int getRemaindDays() {
		return remaindDays;
	}

	public void setRemaindDays(int remaindDays) {
		this.remaindDays = remaindDays;
	}

	public int getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(int lockStatus) {
		this.lockStatus = lockStatus;
	}
	
    @Column(name="cancel_description", nullable=true, unique=false)
    public String getCancelDescription() {
    	return cancelDescription;
    }
    
    public void setCancelDescription(String cancelDescription) {
    	this.cancelDescription = cancelDescription;
    }
    
    @Column(name = "seen_flag", columnDefinition = "int default 0")
	public Integer getSeenFlag() {
		return seenFlag;
	}

	public void setSeenFlag(Integer seenFlag) {
		this.seenFlag = seenFlag;
	}
    
    @Column(name = "schedule_back_uuid")
	public String getScheduleBackUuid() {
		return scheduleBackUuid;
	}

	public void setScheduleBackUuid(String scheduleBackUuid) {
		this.scheduleBackUuid = scheduleBackUuid;
	}
	

	public Integer getSalerId() {
		return salerId;
	}

	public void setSalerId(Integer salerId) {
		this.salerId = salerId;
	}

	public String getSalerName() {
		return salerName;
	}

	public void setSalerName(String salerName) {
		this.salerName = salerName;
	}
	
	@Column(name="forecast_locked_in",unique=false,nullable=true)
	public Integer getForecastLockedIn() {
		return forecastLockedIn;
	}

	public void setForecastLockedIn(Integer forecastLockedIn) {
		this.forecastLockedIn = forecastLockedIn;
	}

	@Column(name="settle_locked_in",unique=false,nullable=true)
	public Integer getSettleLockedIn() {
		return settleLockedIn;
	}

	public void setSettleLockedIn(Integer settleLockedIn) {
		this.settleLockedIn = settleLockedIn;
	}
	
//	@Column(name="is_locked_in",unique=false,nullable=true)
//	public String getIsLockedIn() {
//		return isLockedIn;
//	}
//
//	public void setIsLockedIn(String isLockedIn) {
//		this.isLockedIn = isLockedIn;
//	}

	
	

	@Column(name = "refund_flag")
	public Integer getRefundFlag() {
		return refundFlag;
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

	public void setRefundFlag(Integer refundFlag) {
		this.refundFlag = refundFlag;
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
