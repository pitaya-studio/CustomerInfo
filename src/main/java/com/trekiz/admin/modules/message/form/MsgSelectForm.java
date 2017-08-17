package com.trekiz.admin.modules.message.form;

/**
 * 查询公告/消息 参数类
 * @author gao
 *
 */
public class MsgSelectForm {

	// 开始时间
	private String startDate;
	// 结束时间
	private String endDate;
	// 关键字查询（标题/内容）
	private String conn;
	// 分类查询（全站公告/部门公告/渠道公告/约签公告/消息）
	private Integer msgType;
	// 状态（已读/未读）
	private Integer ifRead;
	//提醒类型
	private Integer remindType;
	/**
	 * 保存状态：1：发布；0：草稿
	 */
	private Integer saveStatus=1;
	// 类型 公告类型：1:单团；2：散拼；3：游学;4:大客户；5：自由行；6：签证；7：机票；8：其他；
	private Integer msgNoticeType;
	// 部门ID数组
	private Integer[] depIds;
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getConn() {
		
		return conn;
	}
	public void setConn(String conn) {
		this.conn = conn;
	}
	public Integer getMsgNoticeType() {
		return msgNoticeType;
	}
	public void setMsgNoticeType(Integer msgNoticeType) {
		this.msgNoticeType = msgNoticeType;
	}
	public Integer getIfRead() {
		return ifRead;
	}
	public void setIfRead(Integer ifRead) {
		this.ifRead = ifRead;
	}
	public Integer getMsgType() {
		return msgType;
	}
	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}
	public Integer[] getDepIds() {
		return depIds;
	}
	public void setDepIds(Integer[] depIds) {
		this.depIds = depIds;
	}
	public Integer getSaveStatus() {
		return saveStatus;
	}
	public void setSaveStatus(Integer saveStatus) {
		this.saveStatus = saveStatus;
	}
	public Integer getRemindType() {
		return remindType;
	}
	public void setRemindType(Integer remindType) {
		this.remindType = remindType;
	}
	
}
