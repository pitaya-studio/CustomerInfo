package com.trekiz.admin.modules.visa.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 文件名: visa_flow_batch_opration
 * 功能: 签证批量操作表Entity
 */
@Entity
@Table(name = "visa_flow_batch_opration")
public class VisaFlowBatchOpration {

	private Long id;
	private String uuid;
	private String batchNo;//batch_record表id
	private String busynessType;//批次号
	private int batchPersonCount;//批次总人数
	private Date printTime;//打印时间
	private String batchTotalMoney;//批次总金额
	private Long createUserId;//创建人ID
	private String createUserName;//创建人名称
	private Date createTime;//批次创建时间
	private String printStatus;//打印状态
	private int currencyId;//币种ID
	private String currencyName;//币种名称
	private String reviewStatus;//审核状态
	private String isSubmit;//是否已提交
	private Date updateTime;
	private Long updateBy;
	private Integer isNewReview;
	private Date refundDate;
	

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

	@Column(name="batch_no")
	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	@Column(name="busyness_type")
	public String getBusynessType() {
		return busynessType;
	}

	public void setBusynessType(String busynessType) {
		this.busynessType = busynessType;
	}

	@Column(name="batch_person_count")
	public int getBatchPersonCount() {
		return batchPersonCount;
	}

	public void setBatchPersonCount(int batchPersonCount) {
		this.batchPersonCount = batchPersonCount;
	}

	@Column(name="print_time")
	public Date getPrintTime() {
		return printTime;
	}

	public void setPrintTime(Date printTime) {
		this.printTime = printTime;
	}
	

	@Column(name="batch_total_money")
	public String getBatchTotalMoney() {
		return batchTotalMoney;
	}

	public void setBatchTotalMoney(String batchTotalMoney) {
		this.batchTotalMoney = batchTotalMoney;
	}

	@Column(name="create_user_id")
	public Long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Long createUserId) {
		this.createUserId = createUserId;
	}

	@Column(name="create_user_name")
	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	@Column(name="create_time")
	public Date getCreateTime(Date date) {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name="print_status")
	public String getPrintStatus() {
		return printStatus;
	}

	public void setPrintStatus(String printStatus) {
		this.printStatus = printStatus;
	}

	@Column(name="currency_id")
	public int getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(int currencyId) {
		this.currencyId = currencyId;
	}

	@Column(name="currency_name")
	public String getCurrencyName() {
		return currencyName;
	}

	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}

	@Column(name="review_status")
	public String getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	@Column(name="is_submit")
	public String getIsSubmit() {
		return isSubmit;
	}

	public void setIsSubmit(String isSubmit) {
		this.isSubmit = isSubmit;
	}
	
	@Column(name="update_time")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Column(name="updateBy")
	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	@Column(name="is_new")
	public Integer getIsNewReview() {
		return isNewReview;
	}

	public void setIsNewReview(Integer isNewReview) {
		this.isNewReview = isNewReview;
	}

	@Column(name="refund_date")
	public Date getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(Date refundDate) {
		this.refundDate = refundDate;
	}
	
	//TODO 270需求,临时性关闭,后续使用
	/*@Column(name="refund_date")
	public Date getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(Date refundDate) {
		this.refundDate = refundDate;
	}*/

}
