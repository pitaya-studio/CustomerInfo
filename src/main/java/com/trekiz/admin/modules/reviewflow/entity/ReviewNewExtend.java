package com.trekiz.admin.modules.reviewflow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * review_new扩展
 * */
@Entity
@Table(name = "review_new_extend")
public class ReviewNewExtend {

	/** 主键 */
	private Integer id;
	/** 对应review_new中id */
	private String reviewId;
	/** 删除标记 */
	private Integer delFlag;
	/** 显示标记 0:不显示; 1:显示 */
	private Integer isShowRemark;
	/** 批发商uuid */
	private String company;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "review_id")
	public String getReviewId() {
		return reviewId;
	}
	public void setReviewId(String reviewId) {
		this.reviewId = reviewId;
	}
	
	@Column(name = "del_flag")
	public Integer getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	
	@Column(name = "is_show_remark")
	public Integer getIsShowRemark() {
		return isShowRemark;
	}
	public void setIsShowRemark(Integer isShowRemark) {
		this.isShowRemark = isShowRemark;
	}
	
	@Column(name = "company")
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	
	
}
