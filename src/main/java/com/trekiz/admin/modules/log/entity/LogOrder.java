package com.trekiz.admin.modules.log.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "log_order")
public class LogOrder {

	private Long id; //id
	private String uuid; //uuid
	private Integer orderType; //订单类型
	private Integer bussinessType; //业务类型
	private Long bussinessId; //业务id
	private Integer opType; //操作类型
	private String fieldName; //字段名称
    private String content; //日志内容
    private Long createBy; //日志创建人
	private Date createDate; //日志创建日期
	private Long companyId; //批发商id
	private String expand1;
	private String expand2;
	private String expand3;

	public LogOrder() {}

	public LogOrder(Long id, String uuid, Integer orderType, Integer bussinessType, Long bussinessId, Integer opType, String fieldName, String content, Long createBy, Date createDate, Long companyId) {
		this.id = id;
		this.uuid = uuid;
		this.orderType = orderType;
		this.bussinessType = bussinessType;
		this.bussinessId = bussinessId;
		this.opType = opType;
		this.fieldName = fieldName;
		this.content = content;
		this.createBy = createBy;
		this.createDate = createDate;
		this.companyId = companyId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
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

	@Column(name = "order_type")
	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	@Column(name = "bussiness_type")
	public Integer getBussinessType() {
		return bussinessType;
	}

	public void setBussinessType(Integer bussinessType) {
		this.bussinessType = bussinessType;
	}

	@Column(name = "bussiness_id")
	public Long getBussinessId() {
		return bussinessId;
	}

	public void setBussinessId(Long bussinessId) {
		this.bussinessId = bussinessId;
	}

	@Column(name = "op_type")
	public Integer getOpType() {
		return opType;
	}

	public void setOpType(Integer opType) {
		this.opType = opType;
	}

	@Column(name = "field_name")
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Column(name = "content")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "create_by")
	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	@Column(name = "create_date")
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "company_id")
	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@Column(name = "expand_1")
	public String getExpand1() {
		return expand1;
	}

	public void setExpand1(String expand1) {
		this.expand1 = expand1;
	}

	@Column(name = "expand_2")
	public String getExpand2() {
		return expand2;
	}

	public void setExpand2(String expand2) {
		this.expand2 = expand2;
	}

	@Column(name = "expand_3")
	public String getExpand3() {
		return expand3;
	}

	public void setExpand3(String expand3) {
		this.expand3 = expand3;
	}
}
