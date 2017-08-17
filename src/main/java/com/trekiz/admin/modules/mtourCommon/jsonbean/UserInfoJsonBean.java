package com.trekiz.admin.modules.mtourCommon.jsonbean;

import java.util.Date;
import java.util.List;

public class UserInfoJsonBean {

	private String userId;//用户Id
	private String userName;//用户姓名
	private Date currentDate;//'当前日期'
	private Date lastLoginDateTime;//'最后登录日期'
	private List<BankInfoJsonBean> banks;//批发商的银行信息
	private String companyRoleCode;
	
	public String getUserId() {
		return this.userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return this.userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Date getCurrentDate() {
		return this.currentDate;
	}
	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
	public Date getLastLoginDateTime() {
		return this.lastLoginDateTime;
	}
	public void setLastLoginDateTime(Date lastLoginDateTime) {
		this.lastLoginDateTime = lastLoginDateTime;
	}
	public List<BankInfoJsonBean> getBanks() {
		return this.banks;
	}
	public void setBanks(List<BankInfoJsonBean> banks) {
		this.banks = banks;
	}
	public String getCompanyRoleCode() {
		return companyRoleCode;
	}
	public void setCompanyRoleCode(String companyRoleCode) {
		this.companyRoleCode = companyRoleCode;
	}
	
	
	
}
