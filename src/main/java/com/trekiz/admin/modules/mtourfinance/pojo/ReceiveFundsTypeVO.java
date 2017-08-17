package com.trekiz.admin.modules.mtourfinance.pojo;

public class ReceiveFundsTypeVO {

	private String receiveFundsTypeCode;// 收款类型
	private String receiveFundsTypeName;// 收款类型名称

	public String getReceiveFundsTypeCode() {
		return receiveFundsTypeCode;
	}

	public void setReceiveFundsTypeCode(String receiveFundsTypeCode) {
		this.receiveFundsTypeCode = receiveFundsTypeCode;
	}

	public String getReceiveFundsTypeName() {
		return receiveFundsTypeName;
	}

	public void setReceiveFundsTypeName(String receiveFundsTypeName) {
		this.receiveFundsTypeName = receiveFundsTypeName;
	}

	public ReceiveFundsTypeVO() {
		super();
	}

	public ReceiveFundsTypeVO(String receiveFundsTypeCode, String receiveFundsTypeName) {
		super();
		this.receiveFundsTypeCode = receiveFundsTypeCode;
		this.receiveFundsTypeName = receiveFundsTypeName;
	}
	
	
}
