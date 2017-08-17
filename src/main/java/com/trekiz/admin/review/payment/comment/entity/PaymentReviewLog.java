package com.trekiz.admin.review.payment.comment.entity;

import java.util.List;

import com.quauq.review.core.engine.entity.ReviewLogNew;

/**
 * 付款审核日志
 * @author shijun.liu
 *
 */
public class PaymentReviewLog {
	private String reviewUuid;					//审核记录UUIDID
	private String costName;					//款项名称
	private List<ReviewLogNew> reviewLogNew;	//审核日志
	public String getReviewUuid() {
		return reviewUuid;
	}
	public void setReviewUuid(String reviewUuid) {
		this.reviewUuid = reviewUuid;
	}
	public String getCostName() {
		return costName;
	}
	public void setCostName(String costName) {
		this.costName = costName;
	}
	public List<ReviewLogNew> getReviewLogNew() {
		return reviewLogNew;
	}
	public void setReviewLogNew(List<ReviewLogNew> reviewLogNew) {
		this.reviewLogNew = reviewLogNew;
	}
}
