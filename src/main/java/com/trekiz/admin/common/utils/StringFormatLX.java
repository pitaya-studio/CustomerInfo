package com.trekiz.admin.common.utils;

import com.trekiz.admin.modules.island.util.StringUtil;

public class StringFormatLX {
	public static String getStrFromVal(String val){
		if(val != null){
			String valTemp = val.replaceAll(" ", "").replaceAll(",", " ").trim().replaceAll(" ", ",");
			valTemp = valTemp.replaceAll(",{1,}+", "、");
			return valTemp;
		}else{
			return val;
		}
	}
	
	public static String getStrFromOne2two(String val){
		if(val != null){
			String valTemp = val.replaceAll(" ", "").replaceAll(",", " ").trim().replaceAll(" ", ",").replaceAll(",{1,}+", "、");
			String[] arrTemp = valTemp.split("、");
			if(arrTemp != null && arrTemp.length == 2 ){
				return arrTemp[0]+"、"+arrTemp[1];
			}if(arrTemp != null && arrTemp.length >2 ){
				return arrTemp[0]+"、"+arrTemp[1]+"...";
			}else{
				return valTemp;
			}
		}else{
			return val;
		}
	}
	
	/**
	 * 去掉以字符串中以parentStr开头的字符串
	 * @param str
	 * @param parentStr
	 * @return
	 */
	public static String getChildString(String str,String parentStr){
		if(StringUtil.isBlank(str)||StringUtil.isBlank(parentStr)){
			return str;
		}
		if(str.trim().startsWith(parentStr.trim())){
			return str.replaceFirst(parentStr, "");
		}
		return str;
	}
	
	/**
	 * 合并字符串如果字符串str1不以str2开头，则合并字符串
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static String meragerStr(String str1,String str2){
		str1 = StringFormatLX.getChildString(str1, str2);
		return str2+str1;
	}
}
