package com.trekiz.admin.modules.supplier.entity;

public class SupplierContactsView {
	
	// 编号
	private Long id;
	// 地接社Id
	private Long supplierId;
	// 联系人姓名
	private String contactName;
	// 联系人手机号码
	private String contactMobile;
	// 联系人固定电话
	private String contactPhone;
	// 联系人传真
	private String contactFax;
	// 联系人Email
	private String contactEmail;
	// 联系人QQ
	private String contactQQ;
	// 备注
	private String remarks;
	// 联系人类型（渠道商：0；批发商：1）
	private String type;
	/** 渠道商地址全称(填充到联系人地址中) */
    private String agentAddressFull;
    
    private String agentPostcode;

	public SupplierContactsView() {
	
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Long supplierId) {
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getAgentAddressFull() {
		return agentAddressFull;
	}

	public void setAgentAddressFull(String agentAddressFull) {
		this.agentAddressFull = agentAddressFull;
	}

	public String getAgentPostcode() {
		return agentPostcode;
	}

	public void setAgentPostcode(String agentPostcode) {
		this.agentPostcode = agentPostcode;
	}
	
	

}
