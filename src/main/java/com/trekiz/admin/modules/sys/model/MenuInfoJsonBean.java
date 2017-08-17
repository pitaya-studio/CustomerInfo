package com.trekiz.admin.modules.sys.model;

/**
 * 菜单jsonbean数据，用于菜单管理中的导入和导出操作
     * Title: MenuInfoJsonBean.java    
     * Description: 
     * @author majiancheng       
     * @created 2015-10-28 上午9:49:52
 */
public class MenuInfoJsonBean {
	// columns START
	// "编号"
	private java.lang.Long id;
	// "父级编号"
	private java.lang.Long parentId;
	// "所有父级编号"
	private java.lang.String parentIds;
	// "菜单名称"
	private java.lang.String name;
	// "链接"
	private java.lang.String href;
	// "目标（mainFrame、 _blank、_self、_parent、_top）"
	private java.lang.String target;
	// "图标"
	private java.lang.String icon;
	// "排序（升序）"
	private java.lang.Integer sort;
	// "是否在菜单中显示（1：显示；0：不显示）"
	private java.lang.String isShow;
	// "权限标识"
	private java.lang.String permission;
	// "操作级别（美途项目区分操作权限的级别，分组使用）"
	private java.lang.Integer level;
	// "创建者"
	private java.lang.Long createBy;
	// "创建时间"
	private java.util.Date createDate;
	// "更新者"
	private java.lang.Long updateBy;
	// "更新时间"
	private java.util.Date updateDate;
	// "备注信息"
	private java.lang.String remarks;
	// "删除标记（0：正常；1：删除）"
	private java.lang.String delFlag;

	public void setId(java.lang.Long value) {
		this.id = value;
	}

	public java.lang.Long getId() {
		return this.id;
	}

	public void setParentId(java.lang.Long value) {
		this.parentId = value;
	}

	public java.lang.Long getParentId() {
		return this.parentId;
	}

	public void setParentIds(java.lang.String value) {
		this.parentIds = value;
	}

	public java.lang.String getParentIds() {
		return this.parentIds;
	}

	public void setName(java.lang.String value) {
		this.name = value;
	}

	public java.lang.String getName() {
		return this.name;
	}

	public void setHref(java.lang.String value) {
		this.href = value;
	}

	public java.lang.String getHref() {
		return this.href;
	}

	public void setTarget(java.lang.String value) {
		this.target = value;
	}

	public java.lang.String getTarget() {
		return this.target;
	}

	public void setIcon(java.lang.String value) {
		this.icon = value;
	}

	public java.lang.String getIcon() {
		return this.icon;
	}

	public void setSort(java.lang.Integer value) {
		this.sort = value;
	}

	public java.lang.Integer getSort() {
		return this.sort;
	}

	public void setIsShow(java.lang.String value) {
		this.isShow = value;
	}

	public java.lang.String getIsShow() {
		return this.isShow;
	}

	public void setPermission(java.lang.String value) {
		this.permission = value;
	}

	public java.lang.String getPermission() {
		return this.permission;
	}

	public void setLevel(java.lang.Integer value) {
		this.level = value;
	}

	public java.lang.Integer getLevel() {
		return this.level;
	}

	public void setCreateBy(java.lang.Long value) {
		this.createBy = value;
	}

	public java.lang.Long getCreateBy() {
		return this.createBy;
	}

	public void setCreateDate(java.util.Date value) {
		this.createDate = value;
	}

	public java.util.Date getCreateDate() {
		return this.createDate;
	}

	public void setUpdateBy(java.lang.Long value) {
		this.updateBy = value;
	}

	public java.lang.Long getUpdateBy() {
		return this.updateBy;
	}

	public void setUpdateDate(java.util.Date value) {
		this.updateDate = value;
	}

	public java.util.Date getUpdateDate() {
		return this.updateDate;
	}

	public void setRemarks(java.lang.String value) {
		this.remarks = value;
	}

	public java.lang.String getRemarks() {
		return this.remarks;
	}

	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}

	public java.lang.String getDelFlag() {
		return this.delFlag;
	}
	

}
