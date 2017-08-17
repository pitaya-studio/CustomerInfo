package com.trekiz.admin.modules.reviewflow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "review_role_level")
public class ReviewRoleLevel {
	   private Long id;  
	   private Long jobId;	   
	   private Long reviewCompanyId;
	   private Integer isEnd;
	   private Integer reviewLevel;
	   private String delFlag; // 删除标记（0：正常；1：删除；2：审核）
	   
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false) 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Integer getReviewLevel() {
		return reviewLevel;
	}
	public void setReviewLevel(Integer reviewLevel) {
		this.reviewLevel = reviewLevel;
	}

	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	@Column(name = "review_company_id", unique = false, nullable = false)
	public Long getReviewCompanyId() {
		return reviewCompanyId;
	}
	public void setReviewCompanyId(Long reviewCompanyId) {
		this.reviewCompanyId = reviewCompanyId;
	}
	
	@Column(name = "sys_job_id", unique = false, nullable = false)
	public Long getJobId() {
		return jobId;
	}
	
	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}
	 @Column(name = "is_end", unique = false, nullable = false)
	public Integer getIsEnd() {
		return isEnd;
	}
	public void setIsEnd(Integer isEnd) {
		this.isEnd = isEnd;
	}

	
	
	
	   
}
