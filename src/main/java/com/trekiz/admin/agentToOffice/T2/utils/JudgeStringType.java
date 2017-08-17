package com.trekiz.admin.agentToOffice.T2.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.trekiz.admin.modules.island.util.StringUtil;

/**
 * 
 * @author lixin
 * 判断字符串类型 eg:数字、日期
 */
public class JudgeStringType {
	
	/**
	 * 判断字符串是否是正整数
	 * @param str 要判断的字符串
	 * @return
	 */
	public static boolean isPositiveInteger(String str){
		if(StringUtil.isNotBlank(str)){
			str = str.trim();
			Pattern pattern = Pattern.compile("[0-9]+");   
			Matcher matcher = pattern.matcher((CharSequence) str);   
			boolean result = matcher.matches();   
			return result;
		}
		return false;
	}
	
	/**
	 * 判断字符串是否是整数
	 * @param str
	 * @return
	 */
	public static boolean isPlusMinusInteger(String str){
		if(StringUtil.isNotBlank(str)){
			str = str.trim();
		    Pattern pattern = Pattern.compile("^(-|\\+)?\\d+$");   
		    Matcher matcher = pattern.matcher((CharSequence) str);   
		    boolean result = matcher.matches();   
		    return result;
		}
		return false;
	}
	
	/**
	 * 判断字符串是否是yyyy-MM-dd格式的日期
	 * @param str
	 * @return
	 */
	public static boolean isDate(String str) {
		if(StringUtil.isNotBlank(str)){
			str = str.trim();
			boolean convertSuccess = true;
			// 指定日期格式为四位年/两位月份/两位日期，注意yyyy/MM/dd区分大小写；
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				// 设置lenient为false.
				// 否则SimpleDateFormat会比较宽松地验证日期，比如2007/02/29会被接受，并转换成2007/03/01
				format.setLenient(false);
				format.parse(str);
			} catch (ParseException e) {
				// e.printStackTrace();
				// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
				convertSuccess = false;
			}
			return convertSuccess;
		}
		return false;
	}
	
	/*public static void main(String[] args) {
		String str = "123";
		String str1 = "2014-03-23";
		System.out.println(JudgeStringType.isDate(str1));
		System.out.println(JudgeStringType.isPositiveInteger(str));
		String str2 = "0";
		System.out.println(JudgeStringType.isPlusMinusInteger(str2));
	}*/
}
