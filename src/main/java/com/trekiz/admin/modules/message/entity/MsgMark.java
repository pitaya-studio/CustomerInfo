package com.trekiz.admin.modules.message.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "msg_mark_view")
public class MsgMark implements Serializable{
	/**
	 * serialVersionUID
	 * @author gao
	 * 2015年3月10日
	 */
	private static final long serialVersionUID = 1L;
	//主键(msg_user_mark表主键)
	private Long id;
	//msgId(msg_announcement表主键)
	private Long msgId;
	//标题
	private String title;
	// 标题样式 0:无加粗；1：有加粗
	private Integer titleVulgarCss=0;
	// 标题样式 0:无高亮；1：有高亮
	private Integer titleLightCss=0;
	//内容
	private String content;
	//缩略内容
    private String contentinfo;
	// 内容链接 (通知/消息专用)
	private String contentUrl;
	// 记录状态 0:保存；1：发布；2：已删除；3：已过期
	private Integer status;
	//公司ID
	private Long companyId;
	// 渠道商ID
	private Long agntinfoId;
	// 类型  1：全站公告；2：部门公告;3：渠道公告; 4：约签公告；5:消息；6:财务信息; 7:提醒
	private Integer msgType=1;
	// 公告类型：1:单团；2：散拼；3：游学;4:大客户；5：自由行；6：签证；7：机票；8：其他；
	private Integer msgNoticeType;
	// 提醒类型 0：还款提醒，1：收款提醒
	private Integer remindType;
	// 过期时间
	private Date overTime;
	// 附件ID
	private String docinfoIds;
	//通告编号
	private Long msgAnnouncementId;
	//查看消息的用户编号
	private Long userId;
	//是否已读 0 未读，1 已读
	private int ifRead;
	//是否提醒 0:提醒, 1:不提醒
	private int isShow;
	//已读时间
	private Date readTime;
	// 通知状态 0 未通知，1 已通知 
	private Integer messageStatus;

	private Date createDate;
	
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	private Long createBy;
	
	public MsgMark(){}
	public MsgMark(Long id,Long msgId,String title,Integer titleVulgarCss,Integer titleLightCss,
			String content,String contentinfo,String contentUrl,Integer status,Long companyId,
			Long agntinfoId,Integer msgType,Integer msgNoticeType,Date overTime,String docinfoIds,
			Long msgAnnouncementId,Long userId,int ifRead,Date readTime,Integer messageStatus,
			Date createDate){
		this.id = id;
		this.msgId = msgId;
		this.title = title;
		this.titleVulgarCss = titleVulgarCss;
		this.titleLightCss = titleLightCss;
		this.content = content;
		this.contentinfo = contentinfo;
		this.contentUrl = contentUrl;
		this.status = status;
		this.companyId = companyId;
		this.agntinfoId = agntinfoId;
		this.msgType = msgType;
		this.msgNoticeType = msgNoticeType;
		this.overTime = overTime;
		this.docinfoIds = docinfoIds;
		this.msgAnnouncementId = msgAnnouncementId;
		this.userId = userId;
		this.ifRead = ifRead;
		this.readTime = readTime;
		this.messageStatus = messageStatus;
		this.createDate = createDate;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getTitleVulgarCss() {
		if(titleVulgarCss!=null){
			return titleVulgarCss;
		}else{
			return 0;
		}
	}
	public void setTitleVulgarCss(Integer titleVulgarCss) {
		this.titleVulgarCss = titleVulgarCss;
	}
	public Integer getTitleLightCss() {
		if(titleLightCss!=null){
			return titleLightCss;
		}else{
			return 0;
		}
	}
	public void setTitleLightCss(Integer titleLightCss) {
		this.titleLightCss = titleLightCss;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContentUrl() {
		return contentUrl;
	}
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public Long getAgntinfoId() {
		return agntinfoId;
	}
	public void setAgntinfoId(Long agntinfoId) {
		this.agntinfoId = agntinfoId;
	}
	public Integer getMsgType() {
		return msgType;
	}
	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}
	public Integer getMsgNoticeType() {
		return msgNoticeType;
	}
	public void setMsgNoticeType(Integer msgNoticeType) {
		this.msgNoticeType = msgNoticeType;
	}
	public Date getOverTime() {
		return overTime;
	}
	public void setOverTime(Date overTime) {
		this.overTime = overTime;
	}
	public String getDocinfoIds() {
		return docinfoIds;
	}
	public void setDocinfoIds(String docinfoIds) {
		this.docinfoIds = docinfoIds;
	}
	public Long getMsgAnnouncementId() {
		return msgAnnouncementId;
	}
	public void setMsgAnnouncementId(Long msgAnnouncementId) {
		this.msgAnnouncementId = msgAnnouncementId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public int getIfRead() {
		return ifRead;
	}
	public void setIfRead(int ifRead) {
		this.ifRead = ifRead;
	}
	public Date getReadTime() {
		return readTime;
	}
	public void setReadTime(Date readTime) {
		this.readTime = readTime;
	}
	public Integer getMessageStatus() {
		return messageStatus;
	}
	public void setMessageStatus(Integer messageStatus) {
		this.messageStatus = messageStatus;
	}
	public Long getMsgId() {
		return msgId;
	}
	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}
	public String getContentinfo() {
		return contentinfo;
	}
	public void setContentinfo(String contentinfo) {
		this.contentinfo = contentinfo;
	}
	public Integer getRemindType() {
		return remindType;
	}
	public void setRemindType(Integer remindType) {
		this.remindType = remindType;
	}
	public int getIsShow() {
		return isShow;
	}
	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}
}
