package com.trekiz.admin.modules.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 参数规则
 */
@Entity
@Table(name = "ruleParameter")
public class RuleParameter{
	
	private Long id;	//编号
	private String paraName;	//参数名称
	private String paraValue;	//参数值
	private String paraExample;	//参数样例
	private String paraDesc;	//参数描述

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="paraName")
	public String getParaName() {
		return paraName;
	}

	public void setParaName(String paraName) {
		this.paraName = paraName;
	}

	@Column(name="paraValue")
	public String getParaValue() {
		return paraValue;
	}

	public void setParaValue(String paraValue) {
		this.paraValue = paraValue;
	}

	@Column(name="paraExample")
	public String getParaExample() {
		return paraExample;
	}

	public void setParaExample(String paraExample) {
		this.paraExample = paraExample;
	}

	@Column(name="paraDesc")
	public String getParaDesc() {
		return paraDesc;
	}

	public void setParaDesc(String paraDesc) {
		this.paraDesc = paraDesc;
	}

	

}
