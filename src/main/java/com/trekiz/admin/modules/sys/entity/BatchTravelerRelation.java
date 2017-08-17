package com.trekiz.admin.modules.sys.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *  文件名: batch_traveler_relation
 *  功能: 
 *  游客批次关系Entity
 *  
 *  @author ang.gao
 *  @DateTime 2015-05-25
 */
@Entity
@Table(name = "batch_traveler_relation")
public class BatchTravelerRelation {

	private Long id;
	private String uuid;
	private String batchUuid;//batch_record表id
	private String batchRecordNo;//批次号
	private Long travelerId;// 游客ID
	private String travelerName;
	private int businessType;//业务类型 1：借款 2：还收据  3：借护照 4：还护照
	private BigDecimal travellerBorrowMoney;
	private String remark;
	private Long createbyId;
	private String createbyName;
	private Long submitbyId;
	private String submitbyName;
	private String isSubmit;
	private Date saveTime;
	private Date submitTime;
	private Long visaId;//签证ID
	private Long orderId;//订单ID
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false)
	public Long getId() {
		return id;
	}
	 
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="uuid")
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	@Column(name="batch_uuid")
	public String getBatchUuid() {
		return batchUuid;
	}
	
	public void setBatchUuid(String batchUuid) {
		this.batchUuid = batchUuid;
	}
	
	@Column(name="batch_record_no")
	public String getBatchRecordNo() {
		return batchRecordNo;
	}

	public void setBatchRecordNo(String batchRecordNo) {
		this.batchRecordNo = batchRecordNo;
	}
	
	@Column(name="traveler_id")
	public Long getTravelerId() {
		return travelerId;
	}

	public void setTravelerId(Long travelerId) {
		this.travelerId = travelerId;
	}
	
	@Column(name="traveler_name")
	public String getTravelerName() {
		return travelerName;
	}

	public void setTravelerName(String travelerName) {
		this.travelerName = travelerName;
	}
	
	@Column(name="business_type")
	public int getBusinessType() {
		return businessType;
	}

	public void setBusinessType(int businessType) {
		this.businessType = businessType;
	}
	
	@Column(name="traveller_borrow_money")
	public BigDecimal getTravellerBorrowMoney() {
		return travellerBorrowMoney;
	}

	public void setTravellerBorrowMoney(BigDecimal travellerBorrowMoney) {
		this.travellerBorrowMoney = travellerBorrowMoney;
	}
	
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name="createby_id")
	public Long getCreatebyId() {
		return createbyId;
	}

	public void setCreatebyId(Long createbyId) {
		this.createbyId = createbyId;
	}
	
	@Column(name="createby_name")
	public String getCreatebyName() {
		return createbyName;
	}

	public void setCreatebyName(String createbyName) {
		this.createbyName = createbyName;
	}
	
	@Column(name="submitby_id")
	public Long getSubmitbyId() {
		return submitbyId;
	}

	public void setSubmitbyId(Long submitbyId) {
		this.submitbyId = submitbyId;
	}
	
	@Column(name="submitby_name")
	public String getSubmitbyName() {
		return submitbyName;
	}

	public void setSubmitbyName(String submitbyName) {
		this.submitbyName = submitbyName;
	}
	
	@Column(name="is_submit")
	public String getIsSubmit() {
		return isSubmit;
	}

	public void setIsSubmit(String isSubmit) {
		this.isSubmit = isSubmit;
	}
	
	@Column(name="save_time")
	public Date getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(Date saveTime) {
		this.saveTime = saveTime;
	}
	
	@Column(name="submit_time")
	public Date getSubmitTime() {
		return submitTime;
	}

	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
	}
	
	@Column(name="visa_id")
	public Long getVisaId() {
		return visaId;
	}

	public void setVisaId(Long visaId) {
		this.visaId = visaId;
	}
	
	
	@Column(name="order_id")
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
}
