package com.trekiz.admin.modules.distribution.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 二维码url对应接口的参数表
 * */
@Entity
@Table(name = "qr_code_param")
public class QrCodeParam {

	private Long id;//主键
	private String qrCodeUuid;//关联qr_code_info表中uuid字段
	private Long companyId;//批发商id
	private Integer productId;//产品id
	private Integer groupId;//团期id
	private Integer orderType;//订单类型
	private Integer createBy;//创建人
	private Date createDate;//创建时间
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "qr_code_info_uuid")
	public String getQrCodeUuid() {
		return qrCodeUuid;
	}
	public void setQrCodeUuid(String qrCodeUuid) {
		this.qrCodeUuid = qrCodeUuid;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getOrderType() {
		return orderType;
	}
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}
	public Integer getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
}
