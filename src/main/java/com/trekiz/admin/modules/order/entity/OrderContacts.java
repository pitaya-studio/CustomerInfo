/**
 *
 */
package com.trekiz.admin.modules.order.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;


 /**
 *  文件名: OrderContacts.java
 *  功能:  订单联系人pojo
 *  
 *  修改记录:   
 *  
 *  @author jianghaili
 *  @DateTime 2014-10-28 
 *  @version 1.0
 */
@Entity
@Table(name = "ordercontacts")
public class OrderContacts  implements Serializable  {
	
	private static final long serialVersionUID = 1L;
	/**  主键*/
	private Long id; 		// 编号
	
	/**  订单id*/
    private Long orderId;
    /**  名称*/
    private String contactsName;
    /**  电话*/
    private String contactsTel;
    /**  固定电话*/
    private String contactsTixedTel ;
    /**  地址*/
    private String contactsAddress;
    /**  传真*/
    private String contactsFax;
    /**  QQ*/
    private String contactsQQ;
    /**  邮箱*/
    private String contactsEmail;
    /**  邮编*/
    private String contactsZipCode;
    /**  其他*/
    private String remark;
             
    
    /**----------渠道联系人-----------------*/
    private Long agentId;
    
    /**---------------------------*/
    
    /**  订单类型  */
    private Integer orderType;
    
    
	@Column(name="orderType")
	public Integer getOrderType() {
		return orderType;
	}

	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	public OrderContacts() {
		super();
	}

	public OrderContacts(Long id){
		this();
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id",unique=true,nullable=false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setOrderId(Long orderId ){
        this.orderId = orderId ;
    }
	@Column(name="orderId",unique=true,nullable=false)
    public Long getOrderId(){
        return this.orderId;
    }
	@Length(min=0, max=50)
	@Column(name="contactsName",unique=true,nullable=false)
	public String getContactsName() {
		return contactsName;
	}

	public void setContactsName(String contactsName) {
		this.contactsName = contactsName;
	}
	@Length(min=0, max=20)
	@Column(name="contactsTel",unique=true,nullable=false)
	public String getContactsTel() {
		return contactsTel;
	}

	public void setContactsTel(String contactsTel) {
		this.contactsTel = contactsTel;
	}
	@Length(min=0, max=20)
	@Column(name="contactsTixedTel",unique=true,nullable=false)
	public String getContactsTixedTel() {
		return contactsTixedTel;
	}
	
	public void setContactsTixedTel(String contactsTixedTel) {
		this.contactsTixedTel = contactsTixedTel;
	}
	@Length(min=0, max=50)
	@Column(name="contactsAddress",unique=true,nullable=false)
	public String getContactsAddress() {
		return contactsAddress;
	}

	public void setContactsAddress(String contactsAddress) {
		this.contactsAddress = contactsAddress;
	}
	@Length(min=0, max=50)
	@Column(name="contactsFax",unique=true,nullable=false)
	public String getContactsFax() {
		return contactsFax;
	}

	public void setContactsFax(String contactsFax) {
		this.contactsFax = contactsFax;
	}
	@Length(min=0, max=20)
	@Column(name="contactsQQ",unique=true,nullable=false)
	public String getContactsQQ() {
		return contactsQQ;
	}

	public void setContactsQQ(String contactsQQ) {
		this.contactsQQ = contactsQQ;
	}
	@Length(min=0, max=50)
	@Column(name="contactsEmail",unique=true,nullable=false)
	public String getContactsEmail() {
		return contactsEmail;
	}

	public void setContactsEmail(String contactsEmail) {
		this.contactsEmail = contactsEmail;
	}
	@Length(min=0, max=20)
	@Column(name="contactsZipCode",unique=true,nullable=false)
	public String getContactsZipCode() {
		return contactsZipCode;
	}

	public void setContactsZipCode(String contactsZipCode) {
		this.contactsZipCode = contactsZipCode;
	}
	@Column(name="remark",unique=true,nullable=false)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/*******************************/
	@Column(name="agentId")
	public Long getAgentId() {
		return agentId;
	}

	public void setAgentId(Long agentId) {
		this.agentId = agentId;
	}

	
	
}


