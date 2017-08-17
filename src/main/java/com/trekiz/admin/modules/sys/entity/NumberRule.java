package com.trekiz.admin.modules.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import com.trekiz.admin.common.persistence.DataEntity;

/**
 * 编号规则
 *
 */
@Entity
@Table(name = "numberRule")
public class NumberRule extends DataEntity {
	private static final long serialVersionUID = 1L;
	
	private Long id;	//编号
	private String numberType;	//编号类型
	private String markName;	//标示名
	private String numberValue;	//编号规则

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="numberType")
	public String getNumberType() {
		return numberType;
	}

	public void setNumberType(String numberType) {
		this.numberType = numberType;
	}

	@Column(name="markName")
	public String getMarkName() {
		return markName;
	}

	public void setMarkName(String markName) {
		this.markName = markName;
	}

	@Column(name="numberValue")
	public String getNumberValue() {
		return numberValue;
	}

	public void setNumberValue(String numberValue) {
		this.numberValue = numberValue;
	}

}
