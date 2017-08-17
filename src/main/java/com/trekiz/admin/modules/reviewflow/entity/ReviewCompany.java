package com.trekiz.admin.modules.reviewflow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "review_company")
public class ReviewCompany {	
	private Long id;
	private Long companyId;		
    private Integer reviewFlowId;    
	private Integer topLevel;    
    private Integer redo;
    private Integer forces; 
    private Long deptId; 
    private String delFlag;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
    
	@Column(name = "companyId", unique = false, nullable = false)
    public Long getCompanyId(){
        return this.companyId;
    }

	@Column(name = "topLevel", unique = false, nullable = false)
    public Integer getTopLevel(){
        return this.topLevel;
    }
	@Column(name = "redo", unique = false, nullable = false)
	public Integer getRedo() {
		return redo;
	}
	@Column(name = "forces", unique = false, nullable = false)
    public Integer getForces(){
        return this.forces;
    }
	
	public void setCompanyId(Long companyId ){
        this.companyId =companyId;
    }
	
	public void getDeptName(){
		
	}	

	public void setRedo(Integer redo) {
		this.redo = redo;
	}
	public void setTopLevel(Integer topLevel ){
        this.topLevel = topLevel ;
    }
	public void setForces(Integer forces ){
        this.forces = forces ;
    }
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	@Column(name = "review_flow_id", unique = false, nullable = false)
	public Integer getReviewFlowId() {
		return reviewFlowId;
	}
	public void setReviewFlowId(Integer reviewFlowId) {
		this.reviewFlowId = reviewFlowId;
	}
	
	

	
	
	

}
