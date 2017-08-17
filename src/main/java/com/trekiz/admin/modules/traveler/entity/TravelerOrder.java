/**
 *
 */
package com.trekiz.admin.modules.traveler.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Indexed;

import com.trekiz.admin.common.persistence.DataEntity;

  /**
 *  文件名: TravelerOrder
 *  功能: 游客订单关系表
 *  修改记录:   
 *  
 *  @author taoxiaoyang
 *  @DateTime 2014-11-13
 *  @version 1.0
 */
@Entity
@Table(name = "travelerorder")
public class TravelerOrder extends DataEntity{
	private static final long serialVersionUID = 1L;
	/** 申请国家ID */
    private Long id;
    /** 申请国家名称 */
    private Long travelerId;
    /** 领地 */
    private Long orderId;
	/** 签证类型 */
    private  Integer travelerKind;
    
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTravelerId() {
		return travelerId;
	}
	public void setTravelerId(Long travelerId) {
		this.travelerId = travelerId;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Integer getTravelerKind() {
		return travelerKind;
	}
	public void setTravelerKind(Integer travelerKind) {
		this.travelerKind = travelerKind;
	}

}


