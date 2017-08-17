package com.trekiz.admin.modules.mtourCommon.jsonbean;

public class AgentInfoJsonBean {
	private String channelUuid;//渠道Id
	private String channelName;//渠道名称
	private String contactName;//渠道联系人姓名
	private String contactPhone;//渠道联系人电话
	private String channelTel;//渠道固定电话
	private String channelZipCode;//渠道邮编
	private String channelAddress;//渠道地址
	private String channelTax;//渠道传真
	
	
	public String getChannelUuid() {
		return channelUuid;
	}
	
	public void setChannelUuid(String channelUuid) {
		this.channelUuid = channelUuid;
	}
	
	public String getChannelName() {
		return channelName;
	}
	
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}

	public String getChannelZipCode() {
		return channelZipCode;
	}

	public void setChannelZipCode(String channelZipCode) {
		this.channelZipCode = channelZipCode;
	}

	public String getChannelTel() {
		return channelTel;
	}

	public void setChannelTel(String channelTel) {
		this.channelTel = channelTel;
	}

	public String getChannelAddress() {
		return channelAddress;
	}

	public void setChannelAddress(String channelAddress) {
		this.channelAddress = channelAddress;
	}

	public String getChannelTax() {
		return channelTax;
	}

	public void setChannelTax(String channelTax) {
		this.channelTax = channelTax;
	}
}
