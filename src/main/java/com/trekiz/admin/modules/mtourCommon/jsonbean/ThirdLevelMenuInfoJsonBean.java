package com.trekiz.admin.modules.mtourCommon.jsonbean;

/**
 * 第三级菜单
 * @author ning.zhang@quauq.com
 * @date 2015年10月22日17:14:05
 */
public class ThirdLevelMenuInfoJsonBean {
	private String roleName; //权限名称 按钮显示的内容
	private String roleCode ;//权限代码 按钮对应的前端行为
	private String level; //级别
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
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getRoleCode() {
		return roleCode;
	}
	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	 
}
