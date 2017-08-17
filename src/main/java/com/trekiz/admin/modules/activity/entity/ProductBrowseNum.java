package com.trekiz.admin.modules.activity.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name = "product_browse_number")
public class ProductBrowseNum implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Long id;	
	private Long productId;		//产品Id
	private Long browseNumber;	//浏览次数
	private Date updateTime;	//最近更新时间
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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
	public Long getBrowseNumber() {
		return browseNumber;
	}
	public void setBrowseNumber(Long browseNumber) {
		this.browseNumber = browseNumber;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	@Override
	public String toString() {
		return "ProductBrowseNum [id=" + id + ", productId=" + productId
				+ ", browseNumber=" + browseNumber + ", updateTime="
				+ updateTime + "]";
	}
	
	
}
