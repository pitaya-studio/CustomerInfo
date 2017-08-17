package com.trekiz.admin.agentToOffice.PricingStrategy.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.persistence.DataEntity;

/**
 * 价格策略实体类
 * @author yakun.bai
 * @Date 2016-4-28
 */
@Entity
@Table(name = "pricingStrategy")
@DynamicInsert @DynamicUpdate
public class PricingStrategy extends DataEntity {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Integer personType;//成人 0， 儿童 1，特殊2
	private Integer favorableType;
	private BigDecimal favorableNum;
	private String favorableTypeStr;
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getPersonType() {
		return personType;
	}
	public void setPersonType(Integer personType) {
		this.personType = personType;
	}
	@Transient
	public String getFavorableTypeStr() {
		if(this.favorableType == 2){
			this.favorableTypeStr = "直减";
		}else if(this.favorableType == 3){
			this.favorableTypeStr = "折扣";
		}
		return favorableTypeStr;
	}
	public void setFavorableTypeStr(String favorableTypeStr) {
		this.favorableTypeStr = favorableTypeStr;
	}
	public Integer getFavorableType() {
		return favorableType;
	}
	public void setFavorableType(Integer favorableType) {
		this.favorableType = favorableType;
	}
	public BigDecimal getFavorableNum() {
		return favorableNum;
	}
	public void setFavorableNum(BigDecimal favorableNum) {
		this.favorableNum = favorableNum;
	}
	public PricingStrategy(Integer personType, Integer favorableType,
			BigDecimal favorableNum) {
		super();
		this.personType = personType;
		this.favorableType = favorableType;
		this.favorableNum = favorableNum;
	}
	public PricingStrategy() {
		super();
	}
	
}