package com.trekiz.admin.modules.preferential.utils;

import org.apache.commons.lang3.StringUtils;

import com.trekiz.admin.modules.preferential.entity.PreferentialDict;

public class PreferentialRuleUtil {
	
	/** 字典与单位分隔符，如（住...晚）  */
	public static final String JOIN_SIGN ="______";

	public static final String JOIN_CAUSE_INPUT_SIGN = "<input type=\"text\" name=\"cause\" id=\"cause\" >";
	public static final String JOIN_EFFECT_INPUT_SIGN = "<input type=\"text\" name=\"effect\" id=\"effect\" >";

	public static final String JOIN_INDATE_INPUT_SIGN = "<input type=\"text\" id=\"inDate\" class=\"wdate dateinput\" name=\"inDate\" onfocus=\"WdatePicker()\" readonly=\"readonly\" />";
	public static final String JOIN_OUTDATE_INPUT_SIGN = "<input type=\"text\" id=\"outDate\" class=\"wdate dateinput\" name=\"outDate\" onfocus=\"WdatePicker()\" readonly=\"readonly\" />";
	
	/**
	 * 拼接因果规则（文本格式），列表展示时使用
	*<p>Title: dictNameJoinUnitNameByType</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-6-19 下午9:40:53
	* @throws
	 */
	public static String dictNameJoinUnitName(String dictName, String unitName) {
		if(StringUtils.isEmpty(dictName) || StringUtils.isEmpty(unitName)) {
			return "";
		}
		
		return dictName + JOIN_SIGN + unitName;
	}
	
	/**
	 * 拼接因果规则（日期格式），列表展示时使用
	*<p>Title: dictNameJoinUnitNameByType</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-6-19 下午9:40:53
	* @throws
	 */
	public static String dictNameJoinUnitNameAsDate(String dictName, String unitName) {
		if(StringUtils.isEmpty(dictName) || StringUtils.isEmpty(unitName)) {
			return "";
		}
		
		return dictName + JOIN_SIGN + "至" + JOIN_SIGN;
	}
	
	/**
	 * 拼接因果关联（输入框格式），模板保存时使用
	*<p>Title: dictNameJoinUnitNameByType</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-6-19 下午9:40:53
	* @throws
	 */
	public static String dictNameJoinUnitNameByType(String dictName, String unitName, Integer type) {
		if(StringUtils.isEmpty(dictName) || StringUtils.isEmpty(unitName)) {
			return "";
		}
		if(type == PreferentialDict.TYPE_CAUSE) {
			return dictName + "：" + JOIN_CAUSE_INPUT_SIGN + unitName;
		} else if(type == PreferentialDict.TYPE_EFFECT) {
			return dictName + "：" + JOIN_EFFECT_INPUT_SIGN + unitName;
		}
		
		return dictName + JOIN_SIGN + unitName;
	}
	
	/**
	 * 拼接因果关联（日期输入框格式），模板保存时使用
	*<p>Title: dictNameJoinUnitNameByType</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-6-19 下午9:40:53
	* @throws
	 */
	public static String dictNameJoinUnitNameAsDateInput(String dictName, String unitName) {
		if(StringUtils.isEmpty(dictName) || StringUtils.isEmpty(unitName)) {
			return "";
		}
		
		return dictName + JOIN_INDATE_INPUT_SIGN + "至" + JOIN_OUTDATE_INPUT_SIGN;
	}

}
