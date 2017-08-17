package com.trekiz.admin.modules.sys.transfer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.sys.entity.Menu;
import com.trekiz.admin.modules.sys.model.MenuInfoJsonBean;

public class MenuTransfer {
	/**
	 * 将jsonbean对象转换成菜单数据库对象
		 * @Title: transfer2MenuByJsonBean
	     * @return Menu
	     * @author majiancheng       
	     * @date 2015-10-28 上午10:12:03
	 */
	public static Menu transfer2MenuByJsonBean(MenuInfoJsonBean jsonBean) {
		if(jsonBean == null) {
			return null;
		}
		
		Menu menu = new Menu();
		BeanUtil.copySimpleProperties(menu, jsonBean);
		
		return menu;
	}
	
	/**
	 * 将菜单jsonbeans集合转换成数据库菜单对象
		 * @Title: transfer2JsonBeanByMenu
	     * @return MenuInfoJsonBean
	     * @author majiancheng       
	     * @date 2015-10-28 上午10:06:24
	 */
	public static List<Menu> transfer2MenusByJsonBeans(List<MenuInfoJsonBean> menuJsonBeans) {
		List<Menu> menus = new ArrayList<Menu>();
		if(CollectionUtils.isEmpty(menuJsonBeans)) {
			return menus;
		}
		
		for(MenuInfoJsonBean jsonBean : menuJsonBeans) {
			menus.add(transfer2MenuByJsonBean(jsonBean));
		}
		
		return menus;
	}
	
	/**
	 * 将数据库菜单对象转换成jsonbean
		 * @Title: transfer2JsonBeanByMenu
	     * @return MenuInfoJsonBean
	     * @author majiancheng       
	     * @date 2015-10-28 上午10:06:24
	 */
	public static MenuInfoJsonBean transfer2JsonBeanByMenu(Menu menu) {
		if(menu == null) {
			return null;
		}
		
		MenuInfoJsonBean menuJsonBean = new MenuInfoJsonBean();
		
		BeanUtil.copySimpleProperties(menuJsonBean, menu);
		return menuJsonBean;
	}
	
	/**
	 * 将数据库菜单集合转换成jsonbeans
		 * @Title: transfer2JsonBeanByMenu
	     * @return MenuInfoJsonBean
	     * @author majiancheng       
	     * @date 2015-10-28 上午10:06:24
	 */
	public static List<MenuInfoJsonBean> transfer2JsonBeansByMenus(List<Menu> menus) {
		List<MenuInfoJsonBean> menuJsonBeans = new ArrayList<MenuInfoJsonBean>();
		if(CollectionUtils.isEmpty(menus)) {
			return menuJsonBeans;
		}
		
		for(Menu menu : menus) {
			menuJsonBeans.add(transfer2JsonBeanByMenu(menu));
		}
		
		return menuJsonBeans;
	}

}
