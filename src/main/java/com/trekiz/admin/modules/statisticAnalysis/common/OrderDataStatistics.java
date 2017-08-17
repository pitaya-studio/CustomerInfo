package com.trekiz.admin.modules.statisticAnalysis.common;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
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
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.trekiz.admin.common.mapper.CustomDateSerializer;


@Entity
@Table(name = "order_data_statistics")
@DynamicInsert(true)
@DynamicUpdate(true)
public class OrderDataStatistics implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id; // 这张表的id

	private String uuid; // 这张表的uuid

	private Long orderId; // 订单id

	private Long productId; // 产品id

	private String productName; // 产品名

	private String companyUuid; // 批发商uuid

	private Integer orderType; // 订单类型

	private BigDecimal amount; // 金额

	private String amountUuid; // 金额uuid

	private Integer orderPersonNum; // 订单人数

	private Date orderCreatetime; // 订单创建时间

	private Long agentinfoId; // 渠道id

	private String agentinfoName; // 渠道名字

	private Integer salerId; // 销售人员id

	private String salerName; // 销售人员名字

	private Date createDate; // 创建时间

	private Integer orderStatus; // 订单状态：0 全部订单；1 全款未支付；2 订金未支付；3 已占位；4 订金已支付；5
									// 已经支付；99 已取消；111 已删除订单
	private String delFlag; // 删除标记（0：正常；1：删除；2：审核）

	@Id
	@Column(name = "id", unique = true, nullable = false, length = 11)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "uuid")
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Column(name = "order_id")
	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	@Column(name = "product_id")
	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@Column(name = "product_name")
	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	@Column(name = "company_uuid")
	public String getCompanyUuid() {
		return companyUuid;
	}

	public void setCompanyUuid(String companyUuid) {
		this.companyUuid = companyUuid;
	}

	@Column(name = "order_type")
	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	@Column(name = "amount")
	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Column(name = "amount_uuid")
	public String getAmountUuid() {
		return amountUuid;
	}

	public void setAmountUuid(String amountUuid) {
		this.amountUuid = amountUuid;
	}

	@Column(name = "order_person_num")
	public Integer getOrderPersonNum() {
		return orderPersonNum;
	}

	public void setOrderPersonNum(Integer orderPersonNum) {
		this.orderPersonNum = orderPersonNum;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="order_createtime")
    @JsonSerialize(using = CustomDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getOrderCreatetime() {
		return orderCreatetime;
	}

	public void setOrderCreatetime(Date orderCreatetime) {
		this.orderCreatetime = orderCreatetime;
	}

	@Column(name = "agentinfo_id")
	public Long getAgentinfoId() {
		return agentinfoId;
	}

	public void setAgentinfoId(Long agentinfoId) {
		this.agentinfoId = agentinfoId;
	}

	@Column(name = "agentinfo_name")
	public String getAgentinfoName() {
		return agentinfoName;
	}

	public void setAgentinfoName(String agentinfoName) {
		this.agentinfoName = agentinfoName;
	}

	@Column(name = "saler_id")
	public Integer getSalerId() {
		return salerId;
	}

	public void setSalerId(Integer salerId) {
		this.salerId = salerId;
	}

	@Column(name = "saler_name")
	public String getSalerName() {
		return salerName;
	}

	public void setSalerName(String salerName) {
		this.salerName = salerName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="create_date")
    @JsonSerialize(using = CustomDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "order_status")
	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Column(name = "del_flag")
	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

}
