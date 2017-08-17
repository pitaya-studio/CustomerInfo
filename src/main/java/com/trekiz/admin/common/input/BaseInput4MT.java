package com.trekiz.admin.common.input;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.security.Base64Util;

public class BaseInput4MT implements java.io.Serializable{
	
	private String requestType;//请求类型,前台识别请求类型用的
	
	private String param;//请求参数
	
	private Map<String, Object> paramMap = null;

	/**
	 * 根据参数json字符串格式成指定的class
	 * @param classz
	 * @return
	 */
	public <T> T getParamObj(Class<T> classz){
		try {
			if(StringUtils.isNotBlank(param)){
				return JSON.parseObject(param, classz);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 根据参数json字符串格式成Map
	 * @param classz
	 * @return
	 */
	public Map<String,Object> getParamMap(){
		try {
			if(StringUtils.isNotBlank(param)){
				if(paramMap == null) {
					paramMap = JSON.parseObject(param, Map.class);
				}
				return paramMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 根据Key得到String类型的参数值
	 * @param classz
	 * @return
	 */
	public String getParamValue(String key){
		try {
			if(StringUtils.isNotBlank(key)){
				if(MapUtils.isNotEmpty(getParamMap())){
					if(getParamMap().containsKey(key)){
						return String.valueOf(getParamMap().get(key));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 根据Key得到指定数据类型（Integer,Double,Float,Date）
	 * @param key
	 * @param classz
	 * @return
	 */
	public <T>T getParamValue4Object(String key,Class<T> classz){
		try {
			if(StringUtils.isNotBlank(key)){
				if(MapUtils.isNotEmpty(getParamMap())){
					if(getParamMap().containsKey(key)){
						return (T)BaseInput.methodMap.get(classz).invoke(null, getParamMap().get(key).toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	public String getRequestType() {
		return requestType;
	}
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		if(StringUtils.isNotBlank(param)){
			param = param.replaceAll(" ", "+");
			param = Base64Util.getFromBase64(param);
		}
		this.param = param;
	}
	
	
	
	
}
