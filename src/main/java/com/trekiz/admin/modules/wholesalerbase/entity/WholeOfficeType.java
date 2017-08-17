package com.trekiz.admin.modules.wholesalerbase.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.persistence.DataEntity;

/**
 * 批发商分类保存表
 * @author gao
 *  2015年4月10日
 */
@Entity
@Table(name = "sys_office_type")
@DynamicInsert @DynamicUpdate
public class WholeOfficeType extends DataEntity{
	
	private static final long serialVersionUID = 1L;
	private Long id;
	// 批发商分类ID
	private String sysdefinedictUUID;
	// 批发商公司ID
	private Long companyID;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="sysdefinedict_uuid")
	public String getSysdefinedictUUID() {
		return sysdefinedictUUID;
	}
	public void setSysdefinedictUUID(String sysdefinedictUUID) {
		this.sysdefinedictUUID = sysdefinedictUUID;
	}

	@Column(name="company_id")
	public Long getCompanyID() {
		return companyID;
	}
	public void setCompanyID(Long companyID) {
		this.companyID = companyID;
	}
}
