package com.trekiz.admin.modules.reviewflow.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "review_log")
public class ReviewLog  {

   private Long id;
   
   
   private Long reviewId;
   
   private Integer nowLevel;
   private Long createBy;
   private Date createDate;
   private String result;
   private String remark;   
   
   public ReviewLog(){}   
   public ReviewLog(Long reviewId,int nowLevel,Long createBy,Date createDate,String result,String remark){	
	   this.reviewId=reviewId;
	   this.nowLevel=nowLevel;
	   this.createBy=createBy;
	   this.createDate=createDate;
	   this.result=result;
	   this.remark=remark; 
   }   
       

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}	
	
	
	
	@Column(name = "nowLevel", unique = false, nullable = false)
    public Integer getNowLevel(){
        return this.nowLevel;
	}
	
	@Column(name = "createBy", unique = false, nullable = false)
    public Long getCreateBy(){
        return this.createBy;
    }
	
	@Column(name = "createDate", unique = false, nullable = false)
    public Date getCreateDate(){
        return this.createDate;
	}
	
	
	@Column(name = "result", unique = false, nullable = false)
    public String getResult(){
        return this.result;
    }
	
	@Column(name = "remark", unique = false, nullable = true)
    public String getRemark(){
        return this.remark;
	}
	
		
	public void setNowLevel(Integer nowLevel) {
		this.nowLevel = nowLevel;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name = "review_id", unique = false, nullable = false)
	public Long getReviewId() {
		return reviewId;
	}
	public void setReviewId(Long reviewId) {
		this.reviewId = reviewId;
	}
	
	
	
}
