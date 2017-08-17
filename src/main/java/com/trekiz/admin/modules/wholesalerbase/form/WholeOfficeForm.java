package com.trekiz.admin.modules.wholesalerbase.form;

import javax.validation.constraints.NotNull;



/**
 * 新增供应商第一步，基本信息
 * @author gao
 *  2015年4月10日
 */
public class WholeOfficeForm {
	private Long id;	// 批发商ID
	
	private Long parentId; // 上级节点
	
	private String code; 	// 批发商编码
	private String name; 	// 批发商名称
	private String enname; // 批发商英文名称
	
	private String supplierBrand; // 批发商品牌
	private String companyName; // 公司名称
	private Integer frontier;	// 1国内、2国外
	
	private String country;		// 公司所在国家
	private String province;	// 公司所在的省
	private String city;				// 公司所在的市
	private String district;				// 公司所在的区
	private String shortAddress; // 联系地址
	private String districtCode;	// 公司所属行政区编码
	
	private String zipCode; // 邮政编码
	private String master; 	// 负责人
	private String phone; 	// 电话
	private String fax; 	// 传真
	private String remarks;	// 备注
	
	private String theName;
	// 批发商状态 0 
	private Integer status;
	
	private String[] typeUuid; // 批发商类型 必填
	private String levelUuid;// 批发商等级 必填
	
	private String menuIds;	// 批发商国内覆盖区域
	private String menuIds2;// 批发商国外覆盖区域
	/**'
	 * 如果值为0，表示提交后进入下一步，值为1，直接提交后结束
	 */
	private Integer saveOrNext;
	
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEnname() {
		return enname;
	}
	public void setEnname(String enname) {
		this.enname = enname;
	}
	@NotNull(message="品牌不要为空")
	public String getSupplierBrand() {
		return supplierBrand;
	}
	public void setSupplierBrand(String supplierBrand) {
		this.supplierBrand = supplierBrand;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Integer getFrontier() {
		return frontier;
	}
	public void setFrontier(Integer frontier) {
		this.frontier = frontier;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getShortAddress() {
		return shortAddress;
	}
	public void setShortAddress(String shortAddress) {
		this.shortAddress = shortAddress;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public String getDistrictCode() {
		return districtCode;
	}
	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getMaster() {
		return master;
	}
	public void setMaster(String master) {
		this.master = master;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@NotNull(message="请选择供应商分类")
	public String[] getTypeUuid() {
		return typeUuid;
	}
	public void setTypeUuid(String[] typeUuid) {
		this.typeUuid = typeUuid;
	}
	public String getLevelUuid() {
		return levelUuid;
	}
	public void setLevelUuid(String levelUuid) {
		this.levelUuid = levelUuid;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getSaveOrNext() {
		return saveOrNext;
	}
	public void setSaveOrNext(Integer saveOrNext) {
		this.saveOrNext = saveOrNext;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTheName() {
		return theName;
	}
	public void setTheName(String theName) {
		this.theName = theName;
	}
	public String getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}
	public String getMenuIds2() {
		return menuIds2;
	}
	public void setMenuIds2(String menuIds2) {
		this.menuIds2 = menuIds2;
	}
}
