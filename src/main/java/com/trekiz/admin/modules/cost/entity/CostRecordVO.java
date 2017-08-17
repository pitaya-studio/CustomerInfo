package com.trekiz.admin.modules.cost.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class CostRecordVO {
    /**
     * 预报单，结算单的基本信息，包括团号，线路，报价，人数，日期，接待社.....信息
     * */
	private Map<String,Object> baseinfo;
	
	/**
	 * 预计收款的信息
	 * */
	private  List<Map<String, Object>> refundInfo;
	
	/**
	 * 境内付款明细
	 * */
	private List<CostRecord> actualInList;
	
	/**
	 * 境外付款明细
	 * */
	private List<CostRecord> actualoutList;
	/**
	 * 状态为审核中、待审核的成本记录、退款记录，返佣记录
	 * */
	private List<CostRecord> otherCostRecords;
	
	/**
	 * 境内成本总合计
	 * */
	private String inTotal;

	/**
	 * 境外成本总合计(使用汇率转换后的)
	 * */
	private String outTotal;
	/**
	 *  境外成本总合计(使用汇率转换前的)
	 * */
	private String outPriceBefore;
	/**返佣*/
	private List<Map<String, Object>> fYlistList;
	
	/**团队退款总额*/
	private BigDecimal groupRefundSum;
	
	/**其他收入 （暂时骡子假期使用）*/
	private List<Map<String,Object>> otherRecordList;
	
	public Map<String, Object> getBaseinfo() {
		return baseinfo;
	}
	public void setBaseinfo(Map<String, Object> baseinfo) {
		this.baseinfo = baseinfo;
	}
	public List<Map<String, Object>> getRefundInfo() {
		return refundInfo;
	}
	public void setRefundInfo(List<Map<String, Object>> refundInfo) {
		this.refundInfo = refundInfo;
	}
	public List<CostRecord> getActualInList() {
		return actualInList;
	}
	public void setActualInList(List<CostRecord> actualInList) {
		this.actualInList = actualInList;
	}
	public List<CostRecord> getActualoutList() {
		return actualoutList;
	}
	public void setActualoutList(List<CostRecord> actualoutList) {
		this.actualoutList = actualoutList;
	}
	public String getInTotal() {
		return inTotal;
	}
	public void setInTotal(String inTotal) {
		this.inTotal = inTotal;
	}
	public String getOutTotal() {
		return outTotal;
	}
	public void setOutTotal(String outTotal) {
		this.outTotal = outTotal;
	}
	public String getOutPriceBefore() {
		return outPriceBefore;
	}
	public void setOutPriceBefore(String outPriceBefore) {
		this.outPriceBefore = outPriceBefore;
	}
	public List<Map<String, Object>> getfYlistList() {
		return fYlistList;
	}
	public void setfYlistList(List<Map<String, Object>> fYlistList) {
		this.fYlistList = fYlistList;
	}
	public List<CostRecord> getOtherCostRecords() {
		return otherCostRecords;
	}
	public void setOtherCostRecords(List<CostRecord> otherCostRecords) {
		this.otherCostRecords = otherCostRecords;
	}
	public BigDecimal getGroupRefundSum() {
		return groupRefundSum;
	}
	public void setGroupRefundSum(BigDecimal groupRefundSum) {
		this.groupRefundSum = groupRefundSum;
	}
	public List<Map<String,Object>> getOtherRecordList() {
		return otherRecordList;
	}
	public void setOtherRecordList(List<Map<String,Object>> otherRecordList) {
		this.otherRecordList = otherRecordList;
	}
	
}
