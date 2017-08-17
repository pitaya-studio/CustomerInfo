package com.trekiz.admin.modules.sys.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * t1用户收藏产品记录
 * @author quauq
 *
 */
@Entity
@Table(name = "T1_user_forward")
@DynamicInsert @DynamicUpdate
public class T1UserForward {
	private Long id;
	private String openId;
	private Long ProductId;
	private Long mobileUserId;
	private Date createDate;
	private Long t1UserId;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	public Date getCreateDate() {
		return createDate;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getT1UserId() {
		return t1UserId;
	}
	public void setT1UserId(Long t1UserId) {
		this.t1UserId = t1UserId;
	}
	public Long getProductId() {
		return ProductId;
	}
	public void setProductId(Long productId) {
		ProductId = productId;
	}
	
}
