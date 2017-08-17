package com.trekiz.admin.modules.finance.param;

/**
 * 交易明细&账龄查询参数集合对象（筛选项）
 * @author wangyang
 * @date 2016.8.24
 * */
public class DetailParams extends ParamEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** 下单时间开始*/
	private String orderTimeBegin;
	/** 下单时间结束*/
	private String orderTimeEnd;
	/** 下单人*/
	private String creator;
	/** 渠道选择*/
	private String agentId;
	/** 部门*/
	private String queryDepartmentId;
	/** 收款状态*/
	private String payStatus;
	/** 达账状态*/
	private String isAccounted;
	/** 出团日期开始*/
	private String groupOpenDateBegin;
	/** 出团日期结束*/
	private String groupOpenDateEnd;
	/** 是否确认占位*/
	private String isSeizedConfirmed;
	/** 签证国家*/
	private String sysCountry;
	
	
	public String getOrderTimeBegin() {
		return orderTimeBegin;
	}
	public void setOrderTimeBegin(String orderTimeBegin) {
		this.orderTimeBegin = orderTimeBegin;
	}
	public String getOrderTimeEnd() {
		return orderTimeEnd;
	}
	public void setOrderTimeEnd(String orderTimeEnd) {
		this.orderTimeEnd = orderTimeEnd;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getAgentId() {
		return agentId;
	}
	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}
	public String getQueryDepartmentId() {
		return queryDepartmentId;
	}
	public void setQueryDepartmentId(String queryDepartmentId) {
		this.queryDepartmentId = queryDepartmentId;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getIsAccounted() {
		return isAccounted;
	}
	public void setIsAccounted(String isAccounted) {
		this.isAccounted = isAccounted;
	}
	public String getGroupOpenDateBegin() {
		return groupOpenDateBegin;
	}
	public void setGroupOpenDateBegin(String groupOpenDateBegin) {
		this.groupOpenDateBegin = groupOpenDateBegin;
	}
	public String getGroupOpenDateEnd() {
		return groupOpenDateEnd;
	}
	public void setGroupOpenDateEnd(String groupOpenDateEnd) {
		this.groupOpenDateEnd = groupOpenDateEnd;
	}
	public String getIsSeizedConfirmed() {
		return isSeizedConfirmed;
	}
	public void setIsSeizedConfirmed(String isSeizedConfirmed) {
		this.isSeizedConfirmed = isSeizedConfirmed;
	}
	public String getSysCountry() {
		return sysCountry;
	}
	public void setSysCountry(String sysCountry) {
		this.sysCountry = sysCountry;
	}
	
	
}
