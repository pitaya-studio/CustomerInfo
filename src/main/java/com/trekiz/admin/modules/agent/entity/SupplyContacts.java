package com.trekiz.admin.modules.agent.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.trekiz.admin.common.persistence.DataEntity;

/**
 * 联系人
 * @author 6CR433WKMN
 * 2015 0416 补全DataEntity类继承
 */
@Entity
@Table(name="supplier_contacts")
public class SupplyContacts extends DataEntity{

    private static final long serialVersionUID = 1L;
	/** 联系人分类 渠道商*/
	public static final String PRI_TYPE="2";
	/** 联系人分类 批发商*/
	public static final String WHO_TYPE="0";
	/** 联系人分类 地接社*/
	public static final String TRA_TYPE = "1";
	
	private Long id;
	private Long supplierId;
	private String contactName;
	private String contactMobile;
	private String contactPhone;
	private String contactFax;
	private String contactEmail;
	private String contactQQ;
	private String remarks;
	private String type;
	private String wechatCode;//微信账号
	/** 渠道商地址全称(填充到联系人地址中) */
    private String agentAddressFull;
	
//	private Agentinfo agentinfo;
	
	public SupplyContacts() {
		super();
	}
	public SupplyContacts(Long id, Long supplierId, String contactName,
			String contactMobile, String contactPhone, String contactFax,
			String contactEmail, String contactQQ, String remarks, String type) {
		this.id = id;
//		this.supplierId = supplierId;
		this.contactName = contactName;
		this.contactMobile = contactMobile;
		this.contactPhone = contactPhone;
		this.contactFax = contactFax;
		this.contactEmail = contactEmail;
		this.contactQQ = contactQQ;
		this.remarks = remarks;
		this.type = type;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false)
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
	@Transient
	public String getAgentAddressFull() {
		return agentAddressFull;
	}
	public void setAgentAddressFull(String agentAddressFull) {
		this.agentAddressFull = agentAddressFull;
	}
	public String getWechatCode() {
		return wechatCode;
	}
	public void setWechatCode(String wechatCode) {
		this.wechatCode = wechatCode;
	}
	
	

//	@ManyToOne
//	@JoinColumn(name="supplierId")
//	public Agentinfo getAgentinfo() {
//		return agentinfo;
//	}
//	public void setAgentinfo(Agentinfo agentinfo) {
//		this.agentinfo = agentinfo;
//	}
	
}
