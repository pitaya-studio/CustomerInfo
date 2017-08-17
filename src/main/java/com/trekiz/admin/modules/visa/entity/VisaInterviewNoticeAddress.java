package com.trekiz.admin.modules.visa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="visa_interview_notice_address")
public class VisaInterviewNoticeAddress implements Serializable {
	
	//主键ID
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	//约签国家ID
	@Column(name="country_id")
	private Long countryId;
	
	//预约领区
	@Column(name="area")
	private String area;
	
	//预约地点
	@Column(name="address")
	private String address;
	
	//备注
	@Column(name="remark")
	private String remark;
	
	//公司ID
	@Column(name="company_id")
	private Long companyId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
}
