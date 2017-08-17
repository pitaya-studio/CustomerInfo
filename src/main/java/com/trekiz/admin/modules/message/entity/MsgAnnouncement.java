package com.trekiz.admin.modules.message.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.DataEntity;
import com.trekiz.admin.common.utils.StringUtils;

/**
*  文件名: MsgAnnouncement
*  功能:消息内容
*  产品Entity
*  修改记录:   
*  
*  @author gao
*  @DateTime 2015-02-06
*  @version 1.0
*/
@Entity
@Table(name = "msg_announcement")
public class MsgAnnouncement extends DataEntity{
	/**
	 * 消息内容
	 */
	private static final long serialVersionUID = 1L;
	//主键
	private Long id;
	//标题
	private String title;
	// 标题样式 0:无加粗；1：有加粗
	private Integer titleVulgarCss=0;
	// 标题样式 0:无高亮；1：有高亮
	private Integer titleLightCss=0;
	//内容
	private String content;
	// 缩略内容
	private String contentinfo;
	// 内容链接 (通知/消息专用)
	private String contentUrl;
	// 记录状态 0:保存；1：发布；2：已删除；3：已过期
	private Integer status;
	//公司ID
	private Long companyId;
	// 渠道商ID
	private Long agntinfoId;
	// 类型  1：全站公告；2：部门公告;3：渠道公告; 4：约签公告；5:消息；
	private Integer msgType=1;
	// 公告类型：1:单团；2：散拼；3：游学;4:大客户；5：自由行；6：签证；7：机票；8：套餐；9：其他；
	private Integer msgNoticeType;
	// 业务类型
	private Integer businessType;
	// 提醒生成规则表的主键
	private Integer remindId;
	// 业务id
	private String businessData;
	// 过期时间
	private Date overTime;
	// 附件ID
	private String docinfoIds;
	
	public MsgAnnouncement(){
		super();
	}
	
	public MsgAnnouncement(Long id,String title,Integer titleVulgarCss,Integer titleLightCss,
			String content,String contentinfo,String contentUrl, Integer status,Long companyId,
			Long agntinfoId,Integer msgType,Integer msgNoticeType,Date overTime,String docinfoIds,
			Date createDate) {
		super();
		this.id = id;
		this.title = title;
		this.titleVulgarCss =titleVulgarCss;
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
		this.createDate = new Date();
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
	@Column(name = "title")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column(name ="title_vulgar_css")
	public Integer getTitleVulgarCss() {
		return titleVulgarCss;
	}
	public void setTitleVulgarCss(Integer titleVulgarCss) {
		this.titleVulgarCss = titleVulgarCss;
	}
	@Column(name ="title_light_css")
	public Integer getTitleLightCss() {
		return titleLightCss;
	}
	public void setTitleLightCss(Integer titleLightCss) {
		this.titleLightCss = titleLightCss;
	}
	@Column(name = "content")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
		// 控制公告正文缩略长度
		if(content.length()>Context.STR_LENGTH){
			contentinfo = StringUtils.abbr(content, Context.STR_LENGTH);
		}else{
			contentinfo = content;
		}
	}
	@Column(name = "content_url")
	public String getContentUrl() {
		return contentUrl;
	}
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
	@Column(name="status")
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	@Column(name = "company_id")
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	@Column(name="agntinfo_id")
	public Long getAgntinfoId() {
		return agntinfoId;
	}
	public void setAgntinfoId(Long agntinfoId) {
		this.agntinfoId = agntinfoId;
	}
	@Column(name = "msg_type")
	public Integer getMsgType() {
		return msgType;
	}
	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}
	@Column(name="msg_notice_type")
	public Integer getMsgNoticeType() {
		return msgNoticeType;
	}
	public void setMsgNoticeType(Integer msgNoticeType) {
		this.msgNoticeType = msgNoticeType;
	}
	@Column(name="business_type")
	public Integer getBusinessType() {
		return businessType;
	}

	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}
	@Column(name="business_data")
	public String getBusinessData() {
		return businessData;
	}

	public void setBusinessData(String businessData) {
		this.businessData = businessData;
	}
	@Column(name="sys_remind_id")
	public Integer getRemindId() {
		return remindId;
	}

	public void setRemindId(Integer remindId) {
		this.remindId = remindId;
	}

	@Column(name="over_time")
	public Date getOverTime() {
		return overTime;
	}
	public void setOverTime(Date overTime) {
		this.overTime = overTime;
	}
	@Column(name="docinfo_ids")
	public String getDocinfoIds() {
		return docinfoIds;
	}
	public void setDocinfoIds(String docinfoIds) {
		this.docinfoIds = docinfoIds;
	}

	public String getContentinfo() {
		return contentinfo;
	}

	public void setContentinfo(String contentinfo) {
		this.contentinfo = contentinfo;
	}
}
