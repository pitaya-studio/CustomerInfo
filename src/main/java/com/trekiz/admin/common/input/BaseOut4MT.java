package com.trekiz.admin.common.input;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.MapUtils;

import com.alibaba.fastjson.JSON;

public class BaseOut4MT implements java.io.Serializable{
	
	//业务状态码，需要增加可以按顺序自增
	public final static String CODE_0001="0001";//传递参数异常
	public final static String CODE_0002="0002";//系统处理异常
	public final static String CODE_0003="0003";//菜单权限接口，发现有无法关联到父节点的menu时使用
	
	private String responseType;//请求类型,前台识别请求类型用的
	
	private String responseCode;//相应状态码 success:成功,fail:请求失败,error:发生异常,authentication:没有登录或者session过期
	
	private Map<String,String> msg;// code:'业务状态码',fail时,返回的失败状态码,根据不同的api再定义;description:'返回信息'
	
	private Object data;//响应的数据 对象或者数组

	public BaseOut4MT(){
		
	}
	public BaseOut4MT(BaseInput4MT input){
		this.setResponseType(input.getRequestType());
	}
	
	public void putMsgCode(String code){
		if(MapUtils.isEmpty(msg)){
			msg = new HashMap<String,String>();
		}
		msg.put("code", code);
	}
	public void putMsgDescription(String description){
		if(MapUtils.isEmpty(msg)){
			msg = new HashMap<String,String>();
		}
		msg.put("description", description);
	}
	
	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public String getResponseCode() {
		return responseCode;
	}

	private void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	
	public void setResponseCode4Success(){
		this.setResponseCode("success");
	}
	public void setResponseCode4Error(){
		this.setResponseCode("error");
	}
	public void setResponseCode4Fail(){
		this.setResponseCode("fail");
	}
	public void setResponseCode4Authentication(){
		this.setResponseCode("authentication");
	}
	public Map<String, String> getMsg() {
		return msg;
	}

	public void setMsg(Map<String, String> msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
