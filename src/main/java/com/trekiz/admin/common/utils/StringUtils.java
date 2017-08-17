/**
 *
 */
package com.trekiz.admin.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 * @author zj
 * @version 2013-11-19
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

	/**
	 * 替换掉HTML标签方法
	 */
	public static String replaceHtml(String html) {
		if (isBlank(html)){
			return "";
		}
		String regEx = "<.+?>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("");
		return s;
	}
	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    
    /**
	 * 替换掉HTML标签,script标签,style标签 的方法
	 */
    public static String delHTMLTag(String htmlStr) {
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        return htmlStr.trim(); // 返回文本字符串
    }

	/**
	 * 缩略字符串（不区分中英文字符）
	 * @param str 目标字符串
	 * @param length 截取长度
	 * @return
	 */
	public static String abbr(String str, int length) {
		if (str == null) {
			return "";
		}
		try {
			StringBuilder sb = new StringBuilder();
			int currentLength = 0;
			for (char c : str.toCharArray()) {
				currentLength += String.valueOf(c).getBytes("GBK").length;
				if (currentLength <= length - 3) {
					sb.append(c);
				} else {
					sb.append("...");
					break;
				}
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 转换为Double类型
	 */
	public static Double toDouble(Object val){
		if (val == null){
			return 0D;
		}
		try {
			return Double.valueOf(trim(val.toString()));
		} catch (Exception e) {
			return 0D;
		}
	}

	/**
	 * 转换为Float类型
	 */
	public static Float toFloat(Object val){
		return toDouble(val).floatValue();
	}

	/**
	 * 转换为Long类型
	 */
	public static Long toLong(Object val){
		return toDouble(val).longValue();
	}

	/**
	 * 转换为Integer类型
	 */
	public static Integer toInteger(Object val){
		return toLong(val).intValue();
	}
	
	/**
	 * 获得i18n字符串
	 */
	public static String getMessage(String code, Object[] args) {
		LocaleResolver localLocaleResolver = (LocaleResolver) SpringContextHolder.getBean(LocaleResolver.class);
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();  
		Locale localLocale = localLocaleResolver.resolveLocale(request);
		return SpringContextHolder.getApplicationContext().getMessage(code, args, localLocale);
	}
	
	/**
	 * 获得用户远程地址
	 */
	public static String getRemoteAddr(HttpServletRequest request){
		String remoteAddr = request.getHeader("X-Real-IP");
        if (isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("X-Forwarded-For");
        }else if (isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("Proxy-Client-IP");
        }else if (isNotBlank(remoteAddr)) {
        	remoteAddr = request.getHeader("WL-Proxy-Client-IP");
        }
        return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
	}
	/**
	 * 按照JAVA_请求客户端ip_当前时间的long型毫秒数_3位随机数规则生成版本号
	 * @return
	 */
	public static String getVersionNumber(HttpServletRequest request){
		String newVersion = "JAVA_" + getRemoteAddr(request).replace("\\.", "-") + "_" + System.currentTimeMillis() + "_" + RandomStringUtils.random(3, false, true);
		return newVersion;
	}
	
	/**
	 * 按照JAVA_system_cancel_当前时间的long型毫秒数_3位随机数规则生成版本号
	 * @return
	 */
	public static String getVersionNumber(){
		String newVersion = "JAVA_SYSTEM_CANCEL_" + System.currentTimeMillis() + "_" + RandomStringUtils.random(3, false, true);
		return newVersion;
	}
	
	/**
	 * 判断该字符串，是不是数字
	 * @param str
	 * @return
	 */
	public static boolean validateInt(String str){
		if(StringUtils.isNotEmpty(str)){
			for(int n=0;n<str.length();n++){
				int chr=str.charAt(n);
				if(chr<48 || chr>57)
			         return false;
			}
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 缩略字符串（不区分中英文字符，审核职务列表展示使用）
	 * @param str 目标字符串
	 * @param length 截取长度
	 * @return
	 */
	public static String abbrs(String str1,String str2, int length) {
		if (null == str1 || null == str2) {
			return "";
		}
		String str = str1+"_"+str2;
		try {
			StringBuilder sb = new StringBuilder();
			int currentLength = 0;
			for (char c : str.toCharArray()) {
				currentLength += String.valueOf(c).getBytes("GBK").length;
				if (currentLength <= length - 3) {
					sb.append(c);
				} else {
					sb.append("...");
					break;
				}
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	/**
	 * 缩略字符串（不区分中英文字符，审核职务列表展示使用）
	 * @param str 目标字符串
	 * @param length 截取长度
	 * @return
	 */
	public static String getStringLength(String str1,String str2) {
		
		if (null == str1 || null == str2) {
			return "0";
		}
		String str = str1+"_"+str2;
		int currentLength = 0;
		for (char c : str.toCharArray()) {
			try {
				currentLength += String.valueOf(c).getBytes("GBK").length;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return currentLength + "";

	}
	
	/**
     * 当浮点型数据位数超过10位之后，数据变成科学计数法显示。用此方法可以使其正常显示。
     * @param value
     * @return Sting
     */
    public static String formatFloatNumber(double value) {
        if(value != 0.00){
            java.text.DecimalFormat df = new java.text.DecimalFormat("########.00");
            return df.format(value);
        }else{
            return "0.00";
        }

    }
    public static String formatFloatNumber(Double value) {
        if(value != null){
            if(value.doubleValue() != 0.00){
                java.text.DecimalFormat df = new java.text.DecimalFormat("########.00");
                return df.format(value.doubleValue());
            }else{
                return "0.00";
            }
        }
        return "";
    }
    
    /**
     * 去除字符串数组重复元素
     * @param array
     * @return
     */
    public static List<String> removeRepeat4Array(String[] array){
    	List<String> list = new ArrayList<String>();  
        for(int i = 0; i < array.length; i++) {  
            if(!list.contains(array[i])) {  
                list.add(array[i]);  
            }  
        }  
    	return list;
    }

	/**
	 * 如果对象是null则返回""
	 * @param obj
	 * @return
	 * @author shijun.liu
	 * @date   2016.07.27
     */
	public static String blankReturnEmpty(Object obj){
		return null == obj ? "" : String.valueOf(obj);
	}

	/**
	 * 判断字符串是否在指定的长度(包含)以内，如果字符串是空，则返回true
	 * @param str	待校验字符串
	 * @param len	指定长度
     * @return		默认false
	 * @author	shijun.liu
	 * @date	2016.08.22
     */
	public static boolean isInnerLength(String str, int len){
		boolean b = false;
		if(StringUtils.isBlank(str)){
			b = true;
		}else {
			if(str.length() <= len){
				b = true;
			}
		}
		return b;
	}

	/**
	 * 首字母大写
	 * @param str   字符串
	 * @return
	 * @author 	shijun.liu
	 * @date    2016.08.25
	 */
	public static String initCap(String str){
		if(isBlank(str) || isEmpty(str)){
			return null;
		}
		char[] chars = str.toCharArray();
		if(chars[0] >= 'a' && chars[0] <= 'z'){
			chars[0] = (char)(chars[0] - 32);
		}
		return new String(chars);
	}

}
