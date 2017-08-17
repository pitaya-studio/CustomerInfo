package com.trekiz.admin.modules.wholesalerbase.form;

/**
 * 批发商表单查询条件
 * @author gao
 *  2015年4月8日
 */
public class OfficeSelectForm {
	/** 按创建时间排序 */
	public static final Integer ORDER_CREATE= 1;
	/** 按修改时间排序 */
	public static final Integer ORDER_UPDATE= 2;
	// 模糊查询输入
	private String conn;
	// 机构名称
	private String name;
	// 公司名称
	private String companyName;
	// 创建时间开始于
	private String startDate;
	// 创建时间截止于
	private String endDate;
	// 批发商（原供应商）账号
	private String supplierCode;
	// 批发商（原供应商）境内、境外区分参数 1:境内；2:境外
	private Integer frontier;
	// 创建时间:1;更新时间:2;
	private Integer orderby;
	// 批发商状态
	private Integer status;
	// 供应商类型
	private String typevalue; 
	// 批发商等级(类型)
	private String levelvalue;
	// 父级节点编号
	private String parentName;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getSupplierCode() {
		return supplierCode;
	}
	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}
	public Integer getFrontier() {
		return frontier;
	}
	public void setFrontier(Integer frontier) {
		this.frontier = frontier;
	}
	public Integer getOrderby() {
		return orderby;
	}
	public void setOrderby(Integer orderby) {
		this.orderby = orderby;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getTypevalue() {
		return typevalue;
	}
	public void setTypevalue(String typevalue) {
		this.typevalue = typevalue;
	}
	public String getLevelvalue() {
		return levelvalue;
	}
	public void setLevelvalue(String levelvalue) {
		this.levelvalue = levelvalue;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
	public String getConn() {
		return conn;
	}
	public void setConn(String conn) {
		this.conn = conn;
	}
}
