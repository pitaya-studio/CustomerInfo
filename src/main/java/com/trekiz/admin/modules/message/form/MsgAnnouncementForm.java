package com.trekiz.admin.modules.message.form;

import org.hibernate.validator.constraints.NotBlank;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.StringUtils;

public class MsgAnnouncementForm {

	private Long msgId;
	@NotBlank(message="标题不能为空")
	//@Size(min=3,max=30)
	private String title;
	private String titleVulgarCss;
	private String titleLightCss;
	@NotBlank(message="公告内容不能为空")
	//@Size(min=3,max=2000)
	private String content;
	@NotBlank(message="公告概括内容不能为空")
	private String index;
	//公司ID
	private Long companyId;
	//部门ids
	private Long[] departmentIds;
	// 渠道商ID
	private Long agntinfoId;
	// 类型，1：全站公告2：部门公告;3：渠道公告 4:约签； 5：消息
	private Integer msgType=1;
	// 公告类型：1:单团；2：散拼；3：游学;4:大客户；5：自由行；6：签证；7：机票；8：套餐；9：其他；
	private Integer msgNoticeType=1;
	// 附件ids
	private Long[] docinfoIds;
	/**
	 * 保存状态：1：发布；0：草稿
	 */
	private Integer saveStatus=0;
	// 过期时间
	private String overTime;
	public String getTitle() {
		if(StringUtils.isNotBlank(title)){
			title=title.trim();
		}
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitleVulgarCss() {
		return titleVulgarCss;
	}
	public void setTitleVulgarCss(String titleVulgarCss) {
		this.titleVulgarCss = titleVulgarCss;
	}
	public String getTitleLightCss() {
		return titleLightCss;
	}
	public void setTitleLightCss(String titleLightCss) {
		this.titleLightCss = titleLightCss;
	}
	public String getContent() {
		if(StringUtils.isNotBlank(content)){
			content=content.trim();
		}
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public Long[] getDepartmentIds() {
		return departmentIds;
	}
	public void setDepartmentIds(Long[] departmentIds) {
		this.departmentIds = departmentIds;
	}
	public Long getAgntinfoId() {
		return agntinfoId;
	}
	public void setAgntinfoId(Long agntinfoId) {
		this.agntinfoId = agntinfoId;
	}
	public Integer getMsgNoticeType() {
		return msgNoticeType;
	}
	public void setMsgNoticeType(Integer msgNoticeType) {
		this.msgNoticeType = msgNoticeType;
	}
	public Long[] getDocinfoIds() {
		return docinfoIds;
	}
	public void setDocinfoIds(Long[] docinfoIds) {
		this.docinfoIds = docinfoIds;
	}
	public Integer getSaveStatus() {
		return saveStatus;
	}
	public void setSaveStatus(Integer saveStatus) {
		this.saveStatus = saveStatus;
	}
	public Integer getMsgType() {
		return msgType;
	}
	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}
	public String getOverTime() {
		return overTime;
	}
	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}
	public String check(){
		if(StringUtils.isBlank(title)){
			return "公告标题不能为空";
		}
		if(StringUtils.isBlank(content)){
			return "公告内容不能为空";
		}
		return null;
	}
	public Long getMsgId() {
		return msgId;
	}
	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}
	public String getIndex() {
		// 控制公告正文缩略长度
		if(index.length()>Context.STR_LENGTH){
			index = StringUtils.abbr(index, Context.STR_LENGTH);
		}
		return index;
	}
	public void setIndex(String index) {
		this.index = index;
	}
}
