package com.trekiz.admin.modules.sys.model;

import java.util.List;

/**
 * 菜单jsonbean,当菜单导入导出时使用
     * Title: MenuJsonBean.java    
     * Description: 
     * @author majiancheng       
     * @created 2015-10-27 下午8:35:40
 */
public class MenuJsonBean {
	private List<MenuInfoJsonBean> parentMenus;
	private List<MenuInfoJsonBean> sonMenus;
	
	public List<MenuInfoJsonBean> getParentMenus() {
		return this.parentMenus;
	}
	public void setParentMenus(List<MenuInfoJsonBean> parentMenus) {
		this.parentMenus = parentMenus;
	}
	public List<MenuInfoJsonBean> getSonMenus() {
		return this.sonMenus;
	}
	public void setSonMenus(List<MenuInfoJsonBean> sonMenus) {
		this.sonMenus = sonMenus;
	}

}
