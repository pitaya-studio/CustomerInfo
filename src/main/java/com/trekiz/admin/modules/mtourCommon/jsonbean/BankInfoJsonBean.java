package com.trekiz.admin.modules.mtourCommon.jsonbean;

import java.util.List;

public class BankInfoJsonBean {
	//银行名称
	public String bankName;
	//银行账号
	public List<AccountInfoJsonBean> accounts;
	
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public List<AccountInfoJsonBean> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<AccountInfoJsonBean> accounts) {
		this.accounts = accounts;
	}


	

	
}

