package com.trekiz.admin.modules.reviewflow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "review_detail")
public class ReviewDetail {	
	private Long id;
	
	
	private Long reviewId;
   
	private String mykey; 
    private String myvalue;   
    public ReviewDetail(){}   
    public ReviewDetail(long reviewId,String mykey,String myvalue){ 	  
 	   this.reviewId=reviewId;
 	   this.mykey=mykey;
 	   this.myvalue=myvalue;
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
    
	@Column(name = "mykey", unique = false, nullable = false)
    public String getMykey(){
        return this.mykey;
    }
	@Column(name = "myvalue", unique = false, nullable = false)
    public String getMyvalue(){
        return this.myvalue;
    }
	
	public void setMykey(String mykey ){
        this.mykey = mykey;
    }	
	
	public void setMyvalue(String myvalue ){
        this.myvalue = myvalue ;
    }	
	@Column(name = "review_id", unique = false, nullable = false)
	public Long getReviewId() {
		return reviewId;
	}
	public void setReviewId(Long reviewId) {
		this.reviewId = reviewId;
	}
	
	

}
