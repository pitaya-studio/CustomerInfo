package com.trekiz.admin.modules.mtourOrder.entity;
/**
 * 附件
 * @author gao
 * @Date 2015-11-02
 *
 */
public class MtourOrderAttachment {

	private Integer attachmentUuid; //文件Uuid
	private String attachmentUrl;  //文件地址
	private String fileName; //文件名称
	private String uploadUserName; // 上传者名称
	public Integer getAttachmentUuid() {
		return attachmentUuid;
	}
	public void setAttachmentUuid(Integer attachmentUuid) {
		this.attachmentUuid = attachmentUuid;
	}
	public String getAttachmentUrl() {
		return attachmentUrl;
	}
	public void setAttachmentUrl(String attachmentUrl) {
		this.attachmentUrl = attachmentUrl;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getUploadUserName() {
		return uploadUserName;
	}
	public void setUploadUserName(String uploadUserName) {
		this.uploadUserName = uploadUserName;
	}
}
