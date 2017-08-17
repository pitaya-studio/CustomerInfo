package com.trekiz.admin.modules.eprice.entity;

import java.util.Date;
import java.util.List;

public class EstimatePriceStatistics {

	/**
	 * 询单起始日期
	 */
	private Date beginTime;
	/**
	 * 询单结束日期
	 */
	private Date endTime;
	/**
	 * 部门ID
	 */
	private Integer deptId;
	/**
	 * 部门名称
	 */
	private String deptName;
	/**
	 * 销售ID
	 */
	private Integer salerId;
	/**
	 * 线路国家ID
	 */
	private Integer countryId;
	/**
	 * 线路国家名称
	 */
	private String countryName;
	/**
	 * 销售姓名
	 */
	private List<String> salerName;
	/**
	 * 统计人群数量 
	 */
	private List<Integer> statisticsList;
	
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Integer getDeptId() {
		return deptId;
	}
	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
	public Integer getSalerId() {
		return salerId;
	}
	public void setSalerId(Integer salerId) {
		this.salerId = salerId;
	}
	public Integer getCountryId() {
		return countryId;
	}
	public void setCountryId(Integer countryId) {
		this.countryId = countryId;
	}
	public List<String> getSalerName() {
		return salerName;
	}
	public void setSalerName(List<String> salerName) {
		this.salerName = salerName;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public List<Integer> getStatisticsList() {
		return statisticsList;
	}
	public void setStatisticsList(List<Integer> statisticsList) {
		this.statisticsList = statisticsList;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
}
