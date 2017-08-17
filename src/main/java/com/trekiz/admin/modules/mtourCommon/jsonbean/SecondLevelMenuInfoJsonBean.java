package com.trekiz.admin.modules.mtourCommon.jsonbean;

import java.util.LinkedList;
import java.util.List;


/**
 * 第二级menu
 * @author ning.zhang@quauq.com
 * @date 2015年10月22日17:11:35
 */
public class SecondLevelMenuInfoJsonBean {
	private String subMenuName ; // 2级菜单名称
	private String subMenuCode ; //2级菜单代码
	private String subMenuUrl ;//2级菜单Url
	private List<ThirdLevelMenuInfoJsonBean> roles  = new LinkedList<>(); ////各页面的权限(三级菜单)
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
	public void addRole(ThirdLevelMenuInfoJsonBean role){
		roles.add(role);
	}
	public String getSubMenuName() {
		return subMenuName;
	}
	public void setSubMenuName(String subMenuName) {
		this.subMenuName = subMenuName;
	}
	public String getSubMenuCode() {
		return subMenuCode;
	}
	public void setSubMenuCode(String subMenuCode) {
		this.subMenuCode = subMenuCode;
	}
	public String getSubMenuUrl() {
		return subMenuUrl;
	}
	public void setSubMenuUrl(String subMenuUrl) {
		this.subMenuUrl = subMenuUrl;
	}
	public List<ThirdLevelMenuInfoJsonBean> getRoles() {
		return roles;
	} 
	
	
}
