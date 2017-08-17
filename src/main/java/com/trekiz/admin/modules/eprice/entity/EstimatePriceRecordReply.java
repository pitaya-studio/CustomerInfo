package com.trekiz.admin.modules.eprice.entity;

import java.util.Date;


/**
 * 询价表和回复表的联合查询bean
 * @author gao
 *
 */
public class EstimatePriceRecordReply {

	/**
	 * 询价项目ID
	 */
	private Long pid;
	/**
	 * 询价记录ID
	 */
	private Long rid;
	/**
	 * 回复记录ID
	 */
	private Long rpid;
	/**
	 * 询价记录状态
	 */
	private Integer status;
	/**
	 * 计调用户ID
	 */
	private Long operatorUserId;
	/**
	 * 计调用户姓名
	 */
	private String operatorUserName;
	/**
	 * 询价时间
	 */
	private Date recordDate;
	/**
	 * 报价时间
	 */
	private Date replyDate;
	/**
	 * 预定出发时间
	 */
	private Date dgroupOutDate;
	/**
	 * 境外停留白天天数
	 */
	private  Integer outsideDaySum;
	/**
	 * 境外停留夜晚天数
	 */
	private Integer outsideNightSum;
	/**
	 * 线路国家（多个）
	 */
	private String travelCountry;
	/**
	 * 旅游团队类型
	 */
	private Integer travelTeamType;
	/**
	 * 地接计调回复报价
	 */
	private String replyPrice;
	/**
	 * 出发城市-抵达城市
	 */
	private String city;
	/**
	 * 多币种统计
	 */
	private String priceDetail;
	
	public EstimatePriceRecordReply(){}
	
	public EstimatePriceRecordReply(Long pid,Long rid, Long rpid,Integer status,Long operatorUserId,
			Date recordDate,Date replyDate,String operatorUserName,
			Date dgroupOutDate, Integer outsideDaySum,Integer outsideNightSum,String travelCountry,
			Integer travelTeamType, String replyPrice,String startCity,String city,String priceDetail){
		this.pid = pid;
		this.rid = rid;
		this.rpid = rpid;
		this.status = status;
		this.operatorUserId = operatorUserId;
		this.operatorUserName = operatorUserName;
		this.recordDate = recordDate;
		this.replyDate = replyDate;
		this.dgroupOutDate = dgroupOutDate;
		this.outsideDaySum = outsideDaySum;
		this.outsideNightSum = outsideNightSum;
		this.travelCountry = travelCountry;
		this.travelTeamType = travelTeamType;
		this.replyPrice = replyPrice;
		this.city = city;
		this.priceDetail = priceDetail;
	}
	
	public Long getPid() {
		return pid;
	}
	public void setPid(Long pid) {
		this.pid = pid;
	}
	public Long getRid() {
		return rid;
	}
	public void setRid(Long rid) {
		this.rid = rid;
	}
	public Long getRpid() {
		return rpid;
	}
	public void setRpid(Long rpid) {
		this.rpid = rpid;
	}
	public Date getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}
	public Date getReplyDate() {
		return replyDate;
	}
	public void setReplyDate(Date replyDate) {
		this.replyDate = replyDate;
	}
	public Date getDgroupOutDate() {
		return dgroupOutDate;
	}
	public void setDgroupOutDate(Date dgroupOutDate) {
		this.dgroupOutDate = dgroupOutDate;
	}
	public Integer getOutsideDaySum() {
		return outsideDaySum;
	}
	public void setOutsideDaySum(Integer outsideDaySum) {
		this.outsideDaySum = outsideDaySum;
	}
	public Integer getOutsideNightSum() {
		return outsideNightSum;
	}
	public void setOutsideNightSum(Integer outsideNightSum) {
		this.outsideNightSum = outsideNightSum;
	}
	public String getTravelCountry() {
		return travelCountry;
	}
	public void setTravelCountry(String travelCountry) {
		this.travelCountry = travelCountry;
	}
	public Integer getTravelTeamType() {
		return travelTeamType;
	}
	public void setTravelTeamType(Integer travelTeamType) {
		this.travelTeamType = travelTeamType;
	}
	public String getReplyPrice() {
		return replyPrice;
	}
	public void setReplyPrice(String replyPrice) {
		this.replyPrice = replyPrice;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getOperatorUserId() {
		return operatorUserId;
	}

	public void setOperatorUserId(Long operatorUserId) {
		this.operatorUserId = operatorUserId;
	}

	public String getOperatorUserName() {
		return operatorUserName;
	}

	public void setOperatorUserName(String operatorUserName) {
		this.operatorUserName = operatorUserName;
	}

	public String getPriceDetail() {
		return priceDetail;
	}

	public void setPriceDetail(String priceDetail) {
		this.priceDetail = priceDetail;
	}
}
