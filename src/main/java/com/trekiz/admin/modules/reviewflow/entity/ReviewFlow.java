package com.trekiz.admin.modules.reviewflow.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "review_flow")
public class ReviewFlow {
	private Integer id;
    private String flowName;   
    
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "flowName", unique = false, nullable = false)
	public String getFlowName() {
		return flowName;
	}
	
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}	
	
	
	/*
	public Long getFlowType() {
		return id;
	} */

	
}
