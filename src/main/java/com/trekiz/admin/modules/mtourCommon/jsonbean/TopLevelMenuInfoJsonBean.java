package com.trekiz.admin.modules.mtourCommon.jsonbean;

import java.util.LinkedList;
import java.util.List;

/**
 * 顶级menu
 * @author ning.zhang@quauq.com
 * @date 2015年10月22日17:06:56
 */
public class TopLevelMenuInfoJsonBean {
	private String menuName ; // 名称
	private String menuCode; // id 
	private List<SecondLevelMenuInfoJsonBean> subMenus = new LinkedList<>(); //子菜单
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
	public void addSubMenu(SecondLevelMenuInfoJsonBean subMenu){
		subMenus.add(subMenu);
	}
	
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public List<SecondLevelMenuInfoJsonBean> getSubMenus() {
		return subMenus;
	}
	
	
}
