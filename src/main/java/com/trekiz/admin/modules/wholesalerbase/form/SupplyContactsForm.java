package com.trekiz.admin.modules.wholesalerbase.form;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class SupplyContactsForm {
	private Long id;
	// 批发商ID
	@NotEmpty(message="未找到指定批发商")
	private String supplierId;
	// 联系人姓名
	@NotEmpty(message="联系人姓名为空")
	@Size(min=0,max=40)
	private String contactName;
	// 联系人手机
	@NotEmpty(message="联系人手机为空")
	@Size(min=0,max=20)
	private String contactMobile;
	// 联系人电话
	@Size(min=0,max=20)
	private String contactPhone;
	// 联系人传真
	@Size(min=0,max=20)
	private String contactFax;
	// 联系人邮箱
	@Email
	@Size(min=0,max=50)
	private String contactEmail;
	// 联系人QQ
	@Size(min=0,max=15)
	private String contactQQ;
	// 备注
	private String remarks;
	// 分类（渠道商：0；批发商：1；地接社：2）
	
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	public String getContactName() {
		return contactName;
	}
	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
	public String getContactMobile() {
		return contactMobile;
	}
	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}
	public String getContactPhone() {
		return contactPhone;
	}
	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	public String getContactFax() {
		return contactFax;
	}
	public void setContactFax(String contactFax) {
		this.contactFax = contactFax;
	}
	public String getContactEmail() {
		return contactEmail;
	}
	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}
	public String getContactQQ() {
		return contactQQ;
	}
	public void setContactQQ(String contactQQ) {
		this.contactQQ = contactQQ;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
}
