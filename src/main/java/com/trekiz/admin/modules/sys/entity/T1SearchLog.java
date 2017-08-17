package com.trekiz.admin.modules.sys.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.utils.excel.annotation.ExcelField;

/**
 * T1用户搜索记录
 * @author quauq
 *
 */
@Entity
@Table(name = "T1_search_log")
@DynamicInsert @DynamicUpdate
public class T1SearchLog {
	private Long id;
	private String options;
	private String optionsValue;
	private int optionType;
	private Date createTime;
	private int batchNum;
	private String openId;
	private Long t1UserId;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ExcelField(title="ID", type=1, align=2, sort=1)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	public String getOptionsValue() {
		return optionsValue;
	}
	public void setOptionsValue(String optionsValue) {
		this.optionsValue = optionsValue;
	}
	public int getOptionType() {
		return optionType;
	}
	public void setOptionType(int optionType) {
		this.optionType = optionType;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public int getBatchNum() {
		return batchNum;
	}
	public void setBatchNum(int batchNum) {
		this.batchNum = batchNum;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Long getT1UserId() {
		return t1UserId;
	}
	public void setT1UserId(Long t1UserId) {
		this.t1UserId = t1UserId;
	}
	
	@PrePersist
	public void prePersist() {
		if (createTime == null) {
			this.createTime = new Date();
		}
		this.createTime = this.createTime;
	}

}
