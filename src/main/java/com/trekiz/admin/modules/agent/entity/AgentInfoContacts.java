package com.trekiz.admin.modules.agent.entity;

import com.trekiz.admin.modules.sys.entity.User;




public class AgentInfoContacts {
	
	private Agentinfo agentinfo ;
	
	private SupplyContactsList contacts;
	
	private String quauqAgentUserLoginName;
	
	private String newPassword;
	
	private Long loginId;
	
	private User loginUser;
	
	private Integer differenceRights;
	
	/** 临时策略（待删除） */
	private Integer lingxianwangshuai;

	// 微信用户id
	private Long mobileUserId;

	public Agentinfo getAgentinfo() {
		return agentinfo;
	}

	public void setAgentinfo(Agentinfo agentinfo) {
		this.agentinfo = agentinfo;
	}

	public SupplyContactsList getContacts() {
		return contacts;
	}

	public void setContacts(SupplyContactsList contacts) {
		this.contacts = contacts;
	}

	public String getQuauqAgentUserLoginName() {
		return quauqAgentUserLoginName;
	}

	public void setQuauqAgentUserLoginName(String quauqAgentUserLoginName) {
		this.quauqAgentUserLoginName = quauqAgentUserLoginName;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public Long getLoginId() {
		return loginId;
	}

	public void setLoginId(Long loginId) {
		this.loginId = loginId;
	}

	public User getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(User loginUser) {
		this.loginUser = loginUser;
	}

	public Integer getDifferenceRights() {
		return differenceRights;
	}

	public void setDifferenceRights(Integer differenceRights) {
		this.differenceRights = differenceRights;
	}

	/** 临时策略（待删除） */
	public Integer getLingxianwangshuai() {
		return lingxianwangshuai;
	}

	public void setLingxianwangshuai(Integer lingxianwangshuai) {
		this.lingxianwangshuai = lingxianwangshuai;
	}

	public Long getMobileUserId() {
		return mobileUserId;
	}

	public void setMobileUserId(Long mobileUserId) {
		this.mobileUserId = mobileUserId;
	}
}
