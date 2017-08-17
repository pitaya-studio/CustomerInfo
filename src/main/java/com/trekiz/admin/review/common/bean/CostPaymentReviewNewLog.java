package com.trekiz.admin.review.common.bean;

import java.util.List;

import com.quauq.review.core.engine.entity.ReviewLogNew;

/**
 * 
 * @author shijun.liu
 * @date 2015.12.17
 *
 */
public class CostPaymentReviewNewLog {

	private String costName;			//成本名称
	private String deleteFlag;			//成本是否删除
	private List<ReviewLogNew> logs;    //成本项对应的日志
	public String getCostName() {
		return costName;
	}
	public void setCostName(String costName) {
		this.costName = costName;
	}
	public String getDeleteFlag() {
		return deleteFlag;
	}
	public void setDeleteFlag(String deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	public List<ReviewLogNew> getLogs() {
		return logs;
	}
	public void setLogs(List<ReviewLogNew> logs) {
		this.logs = logs;
	}
	
}
