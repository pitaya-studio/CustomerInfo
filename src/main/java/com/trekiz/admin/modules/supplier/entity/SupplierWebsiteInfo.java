package com.trekiz.admin.modules.supplier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntity;

@Entity
@Table(name = "supplier_website_info")
public class SupplierWebsiteInfo extends DataEntity {	

	/**
	 * 地接社网站信息
	 */
	private static final long serialVersionUID = 1L;
	// 编号
	private Long id;
	//地接社Id
	private Long supplierId;
	//网站状态
	private String siteStatus;
	//浏览权限 	
	private String scanAuthority;
	//登录账号
	private String loginAccount;
	//登录密码
	private String loginPwd;
	//网站标题
	private String siteTitle; 
	//网站关键字
	private String siteKeyWord;
	//网站描述
	private String siteDescription; 
	//网站地址
	private String siteUrl;
	// 网站负责人
	private String siteManager;
	//网站负责人电话
	private String siteManagerPhone; 
	//网站客服电话
	private String siteCustomerServicePhone;
	//网站管理员邮箱
	private String siteAdminEmail;
	// QQ在线客服
	private String qqCustomerService;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="supplierId",unique=false,nullable=true)
	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	@Column(name="siteStatus",unique=false,nullable=true)
	public String getSiteStatus() {
		return siteStatus;
	}

	public void setSiteStatus(String siteStatus) {
		this.siteStatus = siteStatus;
	}

	@Column(name="scanAuthority",unique=false,nullable=true)
	public String getScanAuthority() {
		return scanAuthority;
	}

	public void setScanAuthority(String scanAuthority) {
		this.scanAuthority = scanAuthority;
	}

	@Column(name="loginAccount",unique=false,nullable=true)
	public String getLoginAccount() {
		return loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	@Column(name="loginPwd",unique=false,nullable=true)
	public String getLoginPwd() {
		return loginPwd;
	}

	public void setLoginPwd(String loginPwd) {
		this.loginPwd = loginPwd;
	}

	@Column(name="siteTitle",unique=false,nullable=true)
	public String getSiteTitle() {
		return siteTitle;
	}

	public void setSiteTitle(String siteTitle) {
		this.siteTitle = siteTitle;
	}

	@Column(name="siteKeyWord",unique=false,nullable=true)
	public String getSiteKeyWord() {
		return siteKeyWord;
	}

	public void setSiteKeyWord(String siteKeyWord) {
		this.siteKeyWord = siteKeyWord;
	}

	@Column(name="siteDescription",unique=false,nullable=true)
	public String getSiteDescription() {
		return siteDescription;
	}

	public void setSiteDescription(String siteDescription) {
		this.siteDescription = siteDescription;
	}

	@Column(name="siteUrl",unique=false,nullable=true)
	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	@Column(name="siteManager",unique=false,nullable=true)
	public String getSiteManager() {
		return siteManager;
	}

	public void setSiteManager(String siteManager) {
		this.siteManager = siteManager;
	}

	@Column(name="siteManagerPhone",unique=false,nullable=true)
	public String getSiteManagerPhone() {
		return siteManagerPhone;
	}

	public void setSiteManagerPhone(String siteManagerPhone) {
		this.siteManagerPhone = siteManagerPhone;
	}

	@Column(name="siteCustomerServicePhone",unique=false,nullable=true)
	public String getSiteCustomerServicePhone() {
		return siteCustomerServicePhone;
	}

	public void setSiteCustomerServicePhone(String siteCustomerServicePhone) {
		this.siteCustomerServicePhone = siteCustomerServicePhone;
	}

	@Column(name="siteAdminEmail",unique=false,nullable=true)
	public String getSiteAdminEmail() {
		return siteAdminEmail;
	}

	public void setSiteAdminEmail(String siteAdminEmail) {
		this.siteAdminEmail = siteAdminEmail;
	}

	@Column(name="qqCustomerService",unique=false,nullable=true)
	public String getQqCustomerService() {
		return qqCustomerService;
	}

	public void setQqCustomerService(String qqCustomerService) {
		this.qqCustomerService = qqCustomerService;
	}
}