package com.trekiz.admin.modules.mtourCommon.entity;

/**
 * Menu临时数据结构 ，供mtour项目权限接口解析前使用
 * @author ning.zhang@quauq.com
 * @date 2015年10月22日16:50:54
 */
public class MenuInfoTempBean {
	private long id ;
	private long parentId;
	private String parentIds;
	private String name;
	private String href;
	private String level;
	private String permission;
	private String isShow;
	private String icon;
	private long sort;
	
	public void setSort(long sort) {
		this.sort = sort;
	}
	public long getSort() {
		return sort;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	public String getIsShow() {
		return isShow;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getParentId() {
		return parentId;
	}
	public void setParentId(long parentId) {
		this.parentId = parentId;
	}
	public String getParentIds() {
		return parentIds;
	}
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	
	
}
