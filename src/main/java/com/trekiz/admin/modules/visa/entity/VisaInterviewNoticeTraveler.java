package com.trekiz.admin.modules.visa.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name="visa_interview_notice_traveler")
public class VisaInterviewNoticeTraveler implements Serializable {
	
	//主键ID
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	//预约表ID
	@Column(name="interview_id")
	private Long interviewId;
	
	//游客ID
	@Column(name="travaler_id")
	private Long travalerId;
	
	//游客姓名
	@Column(name="travaler_name")
	private String travalerName;

	public Long getId(){
		return id;
	}
	
	public void setId(Long id){
		this.id=id;
	}
	
	public Long getInterviewId() {
		return interviewId;
	}

	public void setInterviewId(Long interviewId) {
		this.interviewId = interviewId;
	}

	public Long getTravalerId() {
		return travalerId;
	}

	public void setTravalerId(Long travalerId) {
		this.travalerId = travalerId;
	}

	public String getTravalerName() {
		return travalerName;
	}

	public void setTravalerName(String travalerName) {
		this.travalerName = travalerName;
	}
	
}
