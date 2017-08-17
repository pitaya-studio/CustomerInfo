/**
 *
 */
package com.trekiz.admin.common.config;

import com.trekiz.admin.common.utils.PropertiesLoader;

/**
 * 全局配置类
 * @author zj
 * @version 2013-11-19
 */
public class Global {
	
	/**
	 * 属性文件加载对象
	 */
	private static PropertiesLoader propertiesLoader;
	
	/**
	 * 获取配置
	 */
	public static String getConfig(String key) {
		if (propertiesLoader == null){
			propertiesLoader = new PropertiesLoader("application.properties");
		}
		return propertiesLoader.getProperty(key);
	}

	/**
	 * 获取配置
	 */
	public static String getMessageConfig(String key) {
		if (propertiesLoader == null){
			propertiesLoader = new PropertiesLoader("application.properties");
		}
		return propertiesLoader.getProperty(key);
	}
	/////////////////////////////////////////////////////////
	
	public static String getAdminPath() {
		return getConfig("adminPath");
	}
	public static String getMtourPath(){
		return getConfig("mtourPath");
	}
	public static String getFrontPath() {
		return getConfig("frontPath");
	}
	public static String getUrlSuffix() {
		return getConfig("urlSuffix");
	}
	public static String getMtourLogSwitch(){
		return getConfig("mtourLogSwitch");
	}
	/**
	 * 获取上传文件相对路径跟路径
	 * @return
	 */
	public static String getUploadPath() {
		return getConfig("web.file.upload");
	}
	
	/**
	 * 获取上文文件绝对根路径   
	 * 创建人：liangjingming   
	 *
	 */
	public static String getBasePath(){
		return getConfig("web.file.base");
	}
	
	/**
	 * 获取一个excel最多多少行数据
	 */
	public static String getOneExcelNum(){
		return getConfig("web.one.excel.num");
	}
	
	/**
	 * 获取jsp页面根路径
	 * @return
	 */
	public static String getViewPath(){
		return getConfig("web.view.prefix");
	}
	
}
