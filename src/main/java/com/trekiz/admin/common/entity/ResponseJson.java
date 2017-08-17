package com.trekiz.admin.common.entity;

/**
 * 
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 *
 * Ajax请求时向前端返回数据信息为JSON 例如：{"flag":true} ,{"flag":true,"msg":"操作成功"} ,{"flag":false,"msg":"操作失败"}
 * @author shijun.liu
 * @date 2015年11月19日
 */
public class ResponseJson {

	private boolean flag = true;
	private String msg;
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
