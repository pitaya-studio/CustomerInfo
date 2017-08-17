package com.trekiz.admin.modules.eprice.form;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.utils.DateUtils;

public class EstimatePriceStatisticsForm {

	/**
	 * 询单起始日期
	 */
	private String beginTime = DateUtils.formatCustomDate(DateUtils.addDays(new Date(), -1), "yyyy-MM-dd");
	/**
	 * 询单结束日期
	 */
	private String endTime = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");;
	/**
	 * 部门ID
	 */
	private Integer deptId;
	/**
	 * 销售ID
	 */
	private Integer salerId;
	/**
	 * 线路国家ID
	 */
	private Integer countryId;
	/**
	 * 可看到的全部线路国家
	 */
	private List<Map<String,Object>> countryList;
	/**
	 * 线路国家名称
	 */
	private Integer countryName;
	/**
	 * 销售姓名
	 */
	private String[] salerName;
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
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
	public String[] getSalerName() {
		return salerName;
	}
	public void setSalerName(String[] salerName) {
		this.salerName = salerName;
	}
	public Integer getCountryName() {
		return countryName;
	}
	public void setCountryName(Integer countryName) {
		this.countryName = countryName;
	}
	public List<Map<String,Object>> getCountryList() {
		return countryList;
	}
	public void setCountryList(List<Map<String,Object>> countryList) {
		this.countryList = countryList;
	}
}
