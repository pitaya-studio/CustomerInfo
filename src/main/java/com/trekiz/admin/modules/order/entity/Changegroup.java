/**
 *
 */
package com.trekiz.admin.modules.order.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.trekiz.admin.common.persistence.BaseEntity;

  /**
 *  文件名: Changegroup
 *  功能:
 *  产品Entity
 *  修改记录:   
 *  
 *  @author zj
 *  @DateTime 2014-01-21
 *  @version 1.0
 */
@Entity
@Table(name = "changegroup")
public class Changegroup extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	
	                                              
    /** 订单表Id */
    private Long orderId;
    /** 退换团类型 */
    private Integer groupChangeType;
    /** 申请说明 */
    private String groupChangeRemark;
    /** 申请人 */
    private Long applicant;
    /** 申请时间 */
    private Date applicantTime;
    /** 处理人 */
    private Integer handlerPerson;
    /** 处理时间 */
    private Date handlerTime;
    /** 处理说明 */
    private String handlerRemark;

	public Changegroup() {
		super();
	}

	public Changegroup(Long id){
		this();
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	    public void setOrderId(Long orderId ){
        this.orderId = orderId ;
    }

    public Long getOrderId(){
        return this.orderId;
    }

    public void setGroupChangeType(Integer groupChangeType ){
        this.groupChangeType = groupChangeType ;
    }

    public Integer getGroupChangeType(){
        return this.groupChangeType;
    }

    public void setGroupChangeRemark(String groupChangeRemark ){
        this.groupChangeRemark = groupChangeRemark ;
    }

    @Length(min=0, max=1000)
    public String getGroupChangeRemark(){
        return this.groupChangeRemark;
    }

    public void setApplicant(Long applicant ){
        this.applicant = applicant ;
    }

    public Long getApplicant(){
        return this.applicant;
    }

    public void setApplicantTime(Date applicantTime ){
        this.applicantTime = applicantTime ;
    }

    public Date getApplicantTime(){
        return this.applicantTime;
    }

    public void setHandlerPerson(Integer handlerPerson ){
        this.handlerPerson = handlerPerson ;
    }

    public Integer getHandlerPerson(){
        return this.handlerPerson;
    }

    public void setHandlerTime(Date handlerTime ){
        this.handlerTime = handlerTime ;
    }

    public Date getHandlerTime(){
        return this.handlerTime;
    }

    public void setHandlerRemark(String handlerRemark ){
        this.handlerRemark = handlerRemark ;
    }

    @Length(min=0, max=1000)
    public String getHandlerRemark(){
        return this.handlerRemark;
    }
}


