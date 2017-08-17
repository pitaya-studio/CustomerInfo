package com.trekiz.admin.modules.mtourOrder.jsonbean;

public class PNRRecordJsonBean {
	//修改时间
	private String modifyDate;
	//修改人
	private String modifier;
	//修改内容
	private String modifyContent;
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getModifyContent() {
		return modifyContent;
	}
	public void setModifyContent(String modifyContent) {
		this.modifyContent = modifyContent;
	}
}
