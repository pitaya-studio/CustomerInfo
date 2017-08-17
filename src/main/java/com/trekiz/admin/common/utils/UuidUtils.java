package com.trekiz.admin.common.utils;

import java.util.UUID;

public class UuidUtils {
	
	/**
	* 
	* @param 
	* @return
	* @author majiancheng
	* @Time 2015-11-20 15:02:29
	*/
	public static String generUuid() {
		String s = UUID.randomUUID().toString();
	    return s.replaceAll("-", "");
	}
	
}
