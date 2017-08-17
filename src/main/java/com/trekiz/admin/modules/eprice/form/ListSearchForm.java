package com.trekiz.admin.modules.eprice.form;

import java.util.HashMap;
import java.util.Map;

public class ListSearchForm {
	
	/**
	 * 搜索关键字（目前搜索计调姓名/国家/客户）
	 */
	private String keyword;
	
	/**
	 * 计调id
	 */
	private Long operatorUid;
	
	/**
	 * 销售id
	 */
	private Long salerId;
	
	/**
	 * 询价项目状态 0 已经取消，1 待报价，2 已报价，3 确定报价，4 发布产品
	 */
	private Integer estimateStatus;
	
	/**
	 * 询价项目类型 1 单团询价  2 机票询价
	 */
	private Integer type;
	
	/**
	 * 线路国家id
	 */
	private Long travelCountryId;
	
	/**
	 * 出团时间区间——起始时间
	 */
	private String groupOpenDate;
	
	/**
	 * 出团时间区间——结束始时间
	 */
	private String groupCloseDate;
	
	/**
	 * 客户id
	 */
	private String custId;
	/**
	 * 客户id
	 */
	private String departmentId;
	
	/**
	 * 最进询价时间--开始时间
	 */
	private String epriceStartDate;
	
	/**
	 * 最近询价时间--结束时间
	 */
	private String epriceEndDate;
	
	/**
	 * 排序字段
	 * 排序方式 0 倒序 1正序
	 * 例如："createTime-0" "createTime-1" 
	 */
	private String[] orderColumn;
	
	//回复类型 1-地接  7-机票
	private String replayType;
	
	
	public boolean check(){
		
		return true;
	}
	
	
	public Map<String,Integer> getSorts(){
		if(orderColumn==null || orderColumn.length==0){
			return null;
		}
		
		Map<String,Integer> map = new HashMap<String,Integer>(orderColumn.length);
		String[] t;
		
		for(int i=0,len=orderColumn.length;i<len;i++){
			try {
				t = orderColumn[i].split("-");
				map.put(t[0], Integer.valueOf(t[1]));
			} catch (NumberFormatException e) {
				continue;
			}
			
		}
		
		return map;
	}
	
	public String getEpriceStartDate() {
		return epriceStartDate;
	}


	public void setEpriceStartDate(String epriceStartDate) {
		this.epriceStartDate = epriceStartDate;
	}


	public String getEpriceEndDate() {
		return epriceEndDate;
	}


	public void setEpriceEndDate(String epriceEndDate) {
		this.epriceEndDate = epriceEndDate;
	}


	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Long getOperatorUid() {
		return operatorUid;
	}

	public void setOperatorUid(Long operatorUid) {
		this.operatorUid = operatorUid;
	}

	public Long getSalerId() {
		return salerId;
	}

	public void setSalerId(Long salerId) {
		this.salerId = salerId;
	}

	public Integer getEstimateStatus() {
		return estimateStatus;
	}

	public void setEstimateStatus(Integer estimateStatus) {
		this.estimateStatus = estimateStatus;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getTravelCountryId() {
		return travelCountryId;
	}

	public void setTravelCountryId(Long travelCountryId) {
		this.travelCountryId = travelCountryId;
	}

	public String getGroupOpenDate() {
		return groupOpenDate;
	}

	public void setGroupOpenDate(String groupOpenDate) {
		this.groupOpenDate = groupOpenDate;
	}

	public String getGroupCloseDate() {
		return groupCloseDate;
	}

	public void setGroupCloseDate(String groupCloseDate) {
		this.groupCloseDate = groupCloseDate;
	}

	public String[] getOrderColumn() {
		return orderColumn;
	}

	public void setOrderColumn(String[] orderColumn) {
		this.orderColumn = orderColumn;
	}

	public String getCustId() {
		return custId;
	}


	public void setCustId(String custId) {
		this.custId = custId;
	}


	public String getDepartmentId() {
		return departmentId;
	}


	public void setDepartmentId(String departmentId) {
		this.departmentId = departmentId;
	}


	public String getReplayType() {
		return replayType;
	}


	public void setReplayType(String replayType) {
		this.replayType = replayType;
	}


	 

	
	
	
	
	
}
