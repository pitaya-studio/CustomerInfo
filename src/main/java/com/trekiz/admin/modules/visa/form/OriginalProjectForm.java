package com.trekiz.admin.modules.visa.form;

/***
 * 签务和销售身份管理订单功能
 * 签收签证资料类
 * wenjianye
 * 2014-12-03
 * 
 * */
public class OriginalProjectForm {
	//签证资料的id
	private String id;
	//签证资料的中文名称
	private String name;
	//是否签收的标示位 0:未签收;   1:已签收
	private String signFlag = "0";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSignFlag() {
		return signFlag;
	}
	public void setSignFlag(String signFlag) {
		this.signFlag = signFlag;
	}
}
