/** 
* Copyright 2015 QUAUQ Technology Co. Ltd. 
* All right reserved.
*/
package com.trekiz.admin.modules.mtourCommon.utils;

/**
 * @author ning.zhang@quauq.com
 * @createDate 2015-10-27
 * 
 * 美途项目-ajax请求日志记录
 */
public final class ThreadVariable {
	
	private ThreadVariable(){
	}
	
	/**
	 * ajaxResponse的线程变量
	 */
	private static ThreadLocal<String> mtourAjaxResponse = new ThreadLocal<>();
	
	public static void setMtourAjaxResponse(String str){
		mtourAjaxResponse.set(str);
	}
	
	public static String getMtourAjaxResponse(){
		return mtourAjaxResponse.get();
	}
	
	public static void removeMtourAjaxResponse(){ 
		mtourAjaxResponse.remove();
	}
}
