package com.trekiz.admin.modules.island.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
/**
 * 1 非空判断
 * 2 非“指定字符”判断,可以指定一个字符或多个字符
 * @author LiuXueLiang
 *
 */
public class StringUtil extends StringUtils{
	public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0 || "null".equals(cs)) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
	/**
	 * cs为null或''或指定的字符串flag返回true,如果flag为null或''则使用父类的isBlank判断，同直接使用StringUtils的isBlank(cs)方法 
	 * @param cs
	 * @param flag
	 * @return
	 */
	public static boolean isBlank(CharSequence cs,String flag) {
		if(StringUtils.isBlank(flag)){
			return StringUtils.isBlank(cs);
		}
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0 || flag.equals(cs)) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
	public static boolean isBlank(CharSequence cs,String... flags) {
		if(StringUtils.isBlank(cs)){
			return true;
		}
		for(String flag:flags){
			if(StringUtils.isBlank(flag)){
				continue;
			}
			if(flag.equals(cs)){
				return true;
			}
		}
        return false;
    }
	
	 public static boolean isNotBlank(CharSequence cs) {
	        return !StringUtil.isBlank(cs);
	 }
	 public static boolean isNotBlank(CharSequence cs,String flag){
		 return !StringUtil.isBlank(cs,flag);
	 }
	 public static boolean isNotBlank(CharSequence cs,String... flags){
		 return !StringUtil.isBlank(cs, flags);
	 }
	 /**
	  * 判断字符串是否是汉字
	  * @author gao
	  * 2015年10月20日
	  * @param cs
	  * @return
	  */
	 public static boolean judgeChineseChar(CharSequence cs) {
		 if(cs!=null && StringUtils.isNotBlank(cs.toString().trim())){
			 Pattern p_str = Pattern.compile("[\\u4e00-\\u9fa5]+");
			 String str = cs.toString();
			 Matcher m = p_str.matcher(str);
			 if(m.find() && m.group(0).equals(str)){
				 return true;
			 }
		 }
		 return false;
	 }
	 
	 public static boolean judgeEnglishChar(CharSequence cs){
		 if(cs!=null && StringUtils.isNotBlank(cs.toString().trim())){
			 Pattern p_str = Pattern.compile("^[a-zA-Z0-9_-]*");
			 String str = cs.toString().trim();
			 Matcher m = p_str.matcher(str);
			 if(m.find()&&m.group(0).equals(str)){
				 return true;
			 }
		 }
		 return false;
	 }
}
