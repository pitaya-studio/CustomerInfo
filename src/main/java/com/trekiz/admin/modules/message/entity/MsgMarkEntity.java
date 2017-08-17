package com.trekiz.admin.modules.message.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


/**
*  文件名: MsgUserMark
*  功能:用户标记
*  产品Entity
*  修改记录:   
*  
*  @author wjy
*  @DateTime 2015-01-29
*  @version 1.0
*/
@Entity
@Table(name = "msg_user_mark")
public class MsgMarkEntity{
	/**
	 * 消息和用户关联表
	 */
	private static final long serialVersionUID = 1L;
	//主键
	private Long id;
	//通告编号
	private Long msgAnnouncementId;
	//查看消息的用户编号
	private Long userId;
	//是否已读 0 未读，1 已读
	private int ifRead;
	//是否提醒
	private int isShow;
	//已读时间
	private Date readTime;
	// 通知状态 0 未通知，1 已通知 
	private Integer messageStatus;
	//创建时间
	private Date createDate;
	// 删除标记（0：正常；1：删除；2：审核）
	private String delFlag; 

	@Column(name="message_status")
	public Integer getMessageStatus() {
		return messageStatus;
	}
	public void setMessageStatus(Integer messageStatus) {
		this.messageStatus = messageStatus;
	}
	public MsgMarkEntity() {
		super();
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "msg_announcement_id")
	public Long getMsgAnnouncementId() {
		return msgAnnouncementId;
	}
	public void setMsgAnnouncementId(Long msgAnnouncementId) {
		this.msgAnnouncementId = msgAnnouncementId;
	}
	@Column(name = "user_id")
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	@Column(name = "if_read")
	public int getIfRead() {
		return ifRead;
	}
	public void setIfRead(int ifRead) {
		this.ifRead = ifRead;
	}
	@Column(name = "is_show")
	public int getIsShow() {
		return isShow;
	}
	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}
	@Column(name = "read_time",nullable = false )
	public Date getReadTime() {
		return readTime;
	}
	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
}
