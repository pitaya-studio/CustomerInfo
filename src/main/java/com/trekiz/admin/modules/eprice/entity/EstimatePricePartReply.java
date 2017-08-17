package com.trekiz.admin.modules.eprice.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntity;
/**
 * 多币种、整体报价、细分报价
 * @author gao
 *  2015年5月7日
 */
@Entity
@Table(name = "estimate_price_part_reply")
public class EstimatePricePartReply extends DataEntity{
	/**
	 * serialVersionUID
	 * @author gao
	 * 2015年5月7日
	 */
	private static final long serialVersionUID = 1L;
	public static final String PRICE_TYPE_ALL = "1"; // 整体报价
	public static final String PRICE_TYPE_PART = "2"; // 细分报价
	public static final String PRICE_TYPE_AOP = "3"; // 机票报价
	public static final String PERSON_TYPE_ADULT = "1"; // 成人类型
	public static final String PERSON_TYPE_CHILD = "2"; // 儿童类型
	public static final String PERSON_TYPE_SPECIAL = "3"; // 特殊人群类型
	private Long id;
	// 报价记录ID
	private Long estimatePriceReplyId;
	// 1：整体报价 2：细分报价
	private Integer priceType;
	// 1：成人类型；2：儿童类型；3：特殊人群
	private Integer personType;
	// 人数
	private Integer personNum;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="currency_id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="estimatepricereply_id")
	public Long getEstimatePriceReplyId() {
		return estimatePriceReplyId;
	}
	public void setEstimatePriceReplyId(Long estimatePriceReplyId) {
		this.estimatePriceReplyId = estimatePriceReplyId;
	}
	@Column(name="price_type")
	public Integer getPriceType() {
		return priceType;
	}
	public void setPriceType(Integer priceType) {
		this.priceType = priceType;
	}
	@Column(name="person_type")
	public Integer getPersonType() {
		return personType;
	}
	public void setPersonType(Integer personType) {
		this.personType = personType;
	}
	@Column(name="person_num")
	public Integer getPersonNum() {
		return personNum;
	}
	public void setPersonNum(Integer personNum) {
		this.personNum = personNum;
	}
}
