package com.trekiz.admin.modules.sys.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.trekiz.admin.common.mapper.CustomDateSerializer;
import com.trekiz.admin.common.utils.excel.annotation.ExcelField;

/**
 * t1用户收藏产品记录
 * @author quauq
 *
 */
@Entity
@Table(name = "T1_user_collect")
@DynamicInsert @DynamicUpdate
public class T1UserCollect {
	private Long id;
	private String openId;
	private Long mobileUserId;
	private String uuid;
	private Date createDate;
	private Date updateDate;
	private String delFlag;
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
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Long getMobileUserId() {
		return mobileUserId;
	}
	public void setMobileUserId(Long mobileUserId) {
		this.mobileUserId = mobileUserId;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Date getCreateDate() {
		return createDate;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
	public Long getT1UserId() {
		return t1UserId;
	}
	public void setT1UserId(Long t1UserId) {
		this.t1UserId = t1UserId;
	}
	
	@PrePersist
	public void prePersist() {
		if (createDate == null) {
			this.createDate = new Date();
		}
		this.updateDate = this.createDate;
	}

	@PreUpdate
	public void preUpdate() {
		this.updateDate = new Date();
	}
}
