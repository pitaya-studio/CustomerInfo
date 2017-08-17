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
 * t1联系供应商下单记录
 * @author quauq
 *
 */
@Entity
@Table(name = "T1_dial_agent_phone")
@DynamicInsert @DynamicUpdate
public class T1DialAgentPhone {
	private Long id;
	private Long productId;
	private Long salerId;
	private Date createTime;
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
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getSalerId() {
		return salerId;
	}
	public void setSalerId(Long salerId) {
		this.salerId = salerId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
