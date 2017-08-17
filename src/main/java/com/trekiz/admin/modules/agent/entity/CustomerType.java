package com.trekiz.admin.modules.agent.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trekiz.admin.modules.sys.entity.User;

/**
*  文件名: CustomerType.java
*  功能: 客户类型表
*  产品Entity
*  修改记录:   
*  
*  @author wangyang
*  @DateTime 2016-08-09
*  @version 1.0
*/
@Entity
@Table(name = "sys_customer_type")
public class CustomerType implements Serializable{

	public static final String DEL_FLAG_NORMAL = "0";
	public static final String DEL_FLAG_DELETE = "1";
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomerType() {
		super();
	}
	
	

	public CustomerType(String name, String remark) {
		super();
		this.name = name;
		this.remark = remark;
	}



	private Long id;
	/** 客户类型名称 */
	private String name;
	/** */
	private String value = "0";
	/** 客户类型uuid */
	private String uuid;
	/** 客户类型备注 */
	private String remark;
	/** 删除标记  0为正常，1为已删除*/
	private String delFlag = "0";
	/** 创建人 */
	private User createBy;
	/** 创建日期 */
	private String createDate;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Length(min = 1, max = 32)
	@Column(name = "name", nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min = 1, max = 50)
	@Column(name = "uuid", nullable = false)
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Length(min = 0, max = 1024)
	@Column(name = "remark")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	@Column(name = "del_flag", nullable = false)
	public String getDelFlag() {
		return delFlag;
	}



	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}


	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "create_by")
	@NotFound(action = NotFoundAction.IGNORE)
	public User getCreateBy() {
		return createBy;
	}



	public void setCreateBy(User createBy) {
		this.createBy = createBy;
	}


	@Column(name = "create_date")
	public String getCreateDate() {
		return createDate;
	}



	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}


	@Column(name = "value")
	public String getValue() {
		return value;
	}



	public void setValue(String value) {
		this.value = value;
	}

	

}
