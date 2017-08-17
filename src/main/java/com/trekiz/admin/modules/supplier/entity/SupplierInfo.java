package com.trekiz.admin.modules.supplier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntity;

@Entity
@Table(name = "supplier_info")
public class SupplierInfo extends DataEntity {

	private static final long serialVersionUID = 1L;
	// 编号
	private Long id;
	// 地接社名称
	private String supplierName;
	// 地接社品牌
	private String supplierBrand;
	// 公司英文名称
	private String companyEnName;
	// 所属地区
	private String belongArea;
	// 公司地址
	private String companyAddr;
	// 公司地址（英文）
	private String companyEnAddr;
	// 公司地址（省）
	private String companyAddrProvince;
	// 公司地址（市）
	private String companyAddrCity;
	// 详细地址
	private String detailAddr;
	// 邮政编码
	private String postCode;
	// 电话区号
	private String phoneCode;
	// 电话
	private String phone;
	// 传真区号
	private String faxCode;
	// 传真
	private String fax;
	// 公司描述
	private String description;
	// 公司LOGO
	private Long logo;
	// 备注
	private String remarks;
	// 地接社营业额UUID
	private String bussinessUUID;
	// 地接社类型
	private String supplierType;
	// 地接社其他类型
	private String supplierOtherType;
	// 保存标识
	private String status;
	// 营业执照
	private Long businessLicense;
	// 经营许可证
	private Long businessCertificate;
	// 税收登记证
	private Long taxCertificate;
	// 组织机构代码证
	private Long organizeCertificate;
	// 企业法人身份证
	private Long idCard;
	// 银行开户许可证
	private Long bankOpenLicense;
	// 旅游业资质
	private Long travelAptitudes;
	// 其他上传文件
	private String elseFile;
	// 所属公司ID
	private Long companyId;
	// 地接社等级
	private Integer supplierLevel;
	
	//地接社Uuid
	private String uuid;

	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="supplierName",unique=false,nullable=true)
	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	@Column(name="supplierBrand",unique=false,nullable=true)
	public String getSupplierBrand() {
		return supplierBrand;
	}

	public void setSupplierBrand(String supplierBrand) {
		this.supplierBrand = supplierBrand;
	}

	@Column(name="companyEnName",unique=false,nullable=true)
	public String getCompanyEnName() {
		return companyEnName;
	}

	public void setCompanyEnName(String companyEnName) {
		this.companyEnName = companyEnName;
	}

	@Column(name="belongArea",unique=false,nullable=true)
	public String getBelongArea() {
		return belongArea;
	}

	public void setBelongArea(String belongArea) {
		this.belongArea = belongArea;
	}

	@Column(name="companyAddr",unique=false,nullable=true)
	public String getCompanyAddr() {
		return companyAddr;
	}

	public void setCompanyAddr(String companyAddr) {
		this.companyAddr = companyAddr;
	}

	@Column(name="companyEnAddr",unique=false,nullable=true)
	public String getCompanyEnAddr() {
		return companyEnAddr;
	}

	public void setCompanyEnAddr(String companyEnAddr) {
		this.companyEnAddr = companyEnAddr;
	}

	@Column(name="companyAddrProvince",unique=false,nullable=true)
	public String getCompanyAddrProvince() {
		return companyAddrProvince;
	}

	public void setCompanyAddrProvince(String companyAddrProvince) {
		this.companyAddrProvince = companyAddrProvince;
	}

	@Column(name="companyAddrCity",unique=false,nullable=true)
	public String getCompanyAddrCity() {
		return companyAddrCity;
	}

	public void setCompanyAddrCity(String companyAddrCity) {
		this.companyAddrCity = companyAddrCity;
	}

	@Column(name="detailAddr",unique=false,nullable=true)
	public String getDetailAddr() {
		return detailAddr;
	}

	public void setDetailAddr(String detailAddr) {
		this.detailAddr = detailAddr;
	}

	@Column(name="postCode",unique=false,nullable=true)
	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	@Column(name="phoneCode",unique=false,nullable=true)
	public String getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

	@Column(name="phone",unique=false,nullable=true)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Column(name="faxCode",unique=false,nullable=true)
	public String getFaxCode() {
		return faxCode;
	}

	public void setFaxCode(String faxCode) {
		this.faxCode = faxCode;
	}

	@Column(name="fax",unique=false,nullable=true)
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Column(name="description",unique=false,nullable=true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name="logo",unique=false,nullable=true)
	public Long getLogo() {
		return logo;
	}

	public void setLogo(Long logo) {
		this.logo = logo;
	}

	@Column(name="remarks",unique=false,nullable=true)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name="bussinessUUID",unique=false,nullable=true)
	public String getBussinessUUID() {
		return bussinessUUID;
	}

	public void setBussinessUUID(String bussinessUUID) {
		this.bussinessUUID = bussinessUUID;
	}

	@Column(name="supplierType",unique=false,nullable=true)
	public String getSupplierType() {
		return supplierType;
	}

	public void setSupplierType(String supplierType) {
		this.supplierType = supplierType;
	}

	@Column(name="supplierOtherType",unique=false,nullable=true)
	public String getSupplierOtherType() {
		return supplierOtherType;
	}

	public void setSupplierOtherType(String supplierOtherType) {
		this.supplierOtherType = supplierOtherType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getBusinessCertificate() {
		return businessCertificate;
	}

	public void setBusinessCertificate(Long businessCertificate) {
		this.businessCertificate = businessCertificate;
	}

	public Long getBusinessLicense() {
		return businessLicense;
	}

	public void setBusinessLicense(Long businessLicense) {
		this.businessLicense = businessLicense;
	}

	public Long getTaxCertificate() {
		return taxCertificate;
	}

	public void setTaxCertificate(Long taxCertificate) {
		this.taxCertificate = taxCertificate;
	}

	public Long getOrganizeCertificate() {
		return organizeCertificate;
	}

	public void setOrganizeCertificate(Long organizeCertificate) {
		this.organizeCertificate = organizeCertificate;
	}

	public Long getIdCard() {
		return idCard;
	}

	public void setIdCard(Long idCard) {
		this.idCard = idCard;
	}

	public Long getBankOpenLicense() {
		return bankOpenLicense;
	}

	public void setBankOpenLicense(Long bankOpenLicense) {
		this.bankOpenLicense = bankOpenLicense;
	}

	public Long getTravelAptitudes() {
		return travelAptitudes;
	}

	public void setTravelAptitudes(Long travelAptitudes) {
		this.travelAptitudes = travelAptitudes;
	}

	public String getElseFile() {
		return elseFile;
	}

	public void setElseFile(String elseFile) {
		this.elseFile = elseFile;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@Column(name="supplierLevel",unique=false,nullable=true)
	public Integer getSupplierLevel() {
		return supplierLevel;
	}

	public void setSupplierLevel(Integer supplierLevel) {
		this.supplierLevel = supplierLevel;
	}
	
	@Column(name="uuid")
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}