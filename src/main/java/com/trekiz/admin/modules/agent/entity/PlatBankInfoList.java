package com.trekiz.admin.modules.agent.entity;

import java.util.ArrayList;
import java.util.List;
import com.trekiz.admin.modules.order.entity.PlatBankInfo;

public class PlatBankInfoList {
	private List<PlatBankInfo> banks = new ArrayList<PlatBankInfo>();

	public List<PlatBankInfo> getBanks() {
		return banks;
	}

	public void setBanks(List<PlatBankInfo> banks) {
		this.banks = banks;
	}

	public PlatBankInfoList() {
		super();
	}
	
	
}
