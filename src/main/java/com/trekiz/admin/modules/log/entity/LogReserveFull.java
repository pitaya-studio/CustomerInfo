package com.trekiz.admin.modules.log.entity;


public class LogReserveFull {
	 private String createByName;  //日志创建人姓名
	 private String agentName;  //日志创建人姓名
	 private String groupCode; //团号
	 
	public String getCreateByName() {
		return createByName;
	}
	public void setCreateByName(String createByName) {
		this.createByName = createByName;
	}
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	 
}
