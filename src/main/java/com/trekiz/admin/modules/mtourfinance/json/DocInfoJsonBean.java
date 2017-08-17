package com.trekiz.admin.modules.mtourfinance.json;

public class DocInfoJsonBean {
	
	private String attachmentUuid;//'收款凭证uuid'
	private String fileName;//'收款凭证'
	private String attachmentUrl;//'收款凭证Url'
	private String uploadUserName;//上传者
	
	public String getAttachmentUuid() {
		return this.attachmentUuid;
	}
	public void setAttachmentUuid(String attachmentUuid) {
		this.attachmentUuid = attachmentUuid;
	}
	public String getFileName() {
		return this.fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getAttachmentUrl() {
		return this.attachmentUrl;
	}
	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}
	public String getUploadUserName() {
		return uploadUserName;
	}
	public void setUploadUserName(String uploadUserName) {
		this.uploadUserName = uploadUserName;
	}
	
	
}
