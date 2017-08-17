package com.trekiz.admin.modules.mtourfinance.json;

public class BigCode {

	private String invoiceOriginalTypeCode; // PNR或者地接社的的代码 大编号(票源类型)
	private String PNR; // PNR编号
	private String tourOperatorUuid; // 地接社id
	private String tourOperatorName; // 地接社Name

	public String getInvoiceOriginalTypeCode() {
		return invoiceOriginalTypeCode;
	}

	public void setInvoiceOriginalTypeCode(String invoiceOriginalTypeCode) {
		this.invoiceOriginalTypeCode = invoiceOriginalTypeCode;
	}

	public String getPNR() {
		return PNR;
	}

	public void setPNR(String pNR) {
		PNR = pNR;
	}

	public String getTourOperatorUuid() {
		return tourOperatorUuid;
	}

	public void setTourOperatorUuid(String tourOperatorUuid) {
		this.tourOperatorUuid = tourOperatorUuid;
	}

	public String getTourOperatorName() {
		return tourOperatorName;
	}

	public void setTourOperatorName(String tourOperatorName) {
		this.tourOperatorName = tourOperatorName;
	}

}
