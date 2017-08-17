package com.trekiz.admin.modules.mtourfinance.pojo;
/**
 * 付款凭证POJO
 * @author 赵海明
 * */
public class Dict {
     public String attachmentUuid;
     public String fileName;//付款凭证name
     public String attachmentUrl;//付款凭证url
     public String uploadUserName;
     
	public String getUploadUserName() {
		return uploadUserName;
	}
	public void setUploadUserName(String uploadUserName) {
		this.uploadUserName = uploadUserName;
	}
	public String getAttachmentUuid() {
		return attachmentUuid;
	}
	public void setAttachmentUuid(String attachmentUuid) {
		this.attachmentUuid = attachmentUuid;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getAttachmentUrl() {
		return attachmentUrl;
	}
	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}
     
     
}
