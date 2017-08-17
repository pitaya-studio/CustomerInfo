package com.trekiz.admin.modules.sys.utils;

import java.util.regex.Pattern;


 /**
 *  Class Name: StringUtils.java
 *  Function:
 *  
 *     Modifications:   
 *  
 *  @author zj   DateTime 2013-11-22 下午5:16:51    
 *  @version 1.0
 */
public class PatternUtils {

	public static Pattern PAT_VENDORID = Pattern.compile("vendorId\\|s:\\d+:\"(\\d+)\";"); 
	
	public static Pattern PAT_COMPANYID = Pattern.compile("companyId\\|s:\\d+:\"(\\d+)\";"); 
	
	public static Pattern PAT_AGENTID = Pattern.compile("agentId\\|i:(\\d+);"); 


}
