package com.trekiz.admin.modules.visa.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Entity
@Table(name="visa_interview_notice")
public class VisaInterviewNotice implements Serializable {
	
	//主键ID
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	//签证订单ID
	@Column(name="order_id")
	private Long orderId;
	
	//批次号
	@Column(name="batch_no")
	private String batchNo;
	
	//约签国家
	@Column(name="country")
	private String country;
	
	//预约领区
	@Column(name="area")
	private String area;
	
	//预约地点
	@Column(name="address")
	private String address;
	
	//约签时间
	@Column(name="interview_time")
	private Date interviewTime;
	
	//说明会时间
	@Column(name="explanation_time")
	private Date explainationTime;
	
	//联系人
	@Column(name="contact_man")
	private String contactMan;
	
	//联系方式
	@Column(name="contact_way")
	private String contactWay;
	
	//创建时间
	@Column(name="create_time")
	private Date createTime;
	
	//删除标识
	@Column(name="del_flag")
	private String delFlag="0";
	
	//办签人数
	@Transient
	private Integer num;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getInterviewTime() {
		return interviewTime;
	}

	public void setInterviewTime(Date interviewTime) {
		this.interviewTime = interviewTime;
	}

	public Date getExplainationTime() {
		return explainationTime;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public void setExplainationTime(Date explainationTime) {
		this.explainationTime = explainationTime;
	}

	public String getContactMan() {
		return contactMan;
	}

	public void setContactMan(String contactMan) {
		this.contactMan = contactMan;
	}

	public String getContactWay() {
		return contactWay;
	}

	public void setContactWay(String contactWay) {
		this.contactWay = contactWay;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
	
}
