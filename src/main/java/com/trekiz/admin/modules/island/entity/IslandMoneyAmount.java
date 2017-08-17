/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "island_money_amount")
@DynamicInsert(true)
@DynamicUpdate(true)
public class IslandMoneyAmount   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "IslandMoneyAmount";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_CURRENCY_ID = "币种ID";
	public static final String ALIAS_AMOUNT = "金额";
	public static final String ALIAS_EXCHANGERATE = "汇率";
	public static final String ALIAS_BUSINESS_UUID = "订单UUID或游客UUID";
	public static final String ALIAS_MONEY_TYPE = "款项类型";
	public static final String ALIAS_BUSINESS_TYPE = "业务类型(1表示订单，2表示游客,3 表示询价报价4表示团期)";
	public static final String ALIAS_REVIEW_ID = "审核review表主键id";
	public static final String ALIAS_SERIALNUM = "流水号UUID";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_DEL_FLAG = "删除状态";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private java.lang.Long id;
	private java.lang.String uuid;
	private Integer currencyId;
	private Double amount;
	private Double exchangerate;
	private java.lang.String businessUuid;
	private Integer moneyType;
	private Integer businessType;
	private java.lang.Integer reviewId;
	private java.lang.String serialNum;
	private java.lang.Integer createBy;
	private java.util.Date createDate;
	private java.lang.Integer updateBy;
	private java.util.Date updateDate;
	private java.lang.String delFlag;
	private String reviewUuid;
	
	/** 业务类型(订单) */
	public static final int BUSINESS_TYPE_ORDER = 1;
	/** 业务类型(游客) */
	public static final int BUSINESS_TYPE_TRAVELER = 2;
	/** 业务类型(询价报价) */
	public static final int BUSINESS_TYPE_ASK_PRICE = 3;
	/** 业务类型(团期) */
	public static final int BUSINESS_TYPE_GROUP = 4;
	
	private String currencyMark;	//币种标识
	//columns END
	private String date2String(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	private Date string2Date(String dateStr,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return  sdf.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	public IslandMoneyAmount(){
	}

	public IslandMoneyAmount(
		java.lang.Long id
	){
		this.id = id;
	}

	
		
	public void setId(java.lang.Long value) {
		this.id = value;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public java.lang.Long getId() {
		return this.id;
	}
	
		
	public void setUuid(java.lang.String value) {
		this.uuid = value;
	}
	@Column(name="uuid")
	public java.lang.String getUuid() {
		return this.uuid;
	}
	
		
	public void setCurrencyId(Integer value) {
		this.currencyId = value;
	}
	@Column(name="currencyId")
	public Integer getCurrencyId() {
		return this.currencyId;
	}
	
		
	public void setAmount(Double value) {
		this.amount = value;
	}
	@Column(name="amount")
	public Double getAmount() {
		return this.amount;
	}
	
		
	public void setExchangerate(Double value) {
		this.exchangerate = value;
	}
	@Column(name="exchangerate")
	public Double getExchangerate() {
		return this.exchangerate;
	}
	
		
	public void setBusinessUuid(java.lang.String value) {
		this.businessUuid = value;
	}
	@Column(name="business_uuid")
	public java.lang.String getBusinessUuid() {
		return this.businessUuid;
	}
	
		
	public void setMoneyType(Integer value) {
		this.moneyType = value;
	}
	@Column(name="moneyType")
	public Integer getMoneyType() {
		return this.moneyType;
	}
	
		
	public void setBusinessType(Integer value) {
		this.businessType = value;
	}
	@Column(name="businessType")
	public Integer getBusinessType() {
		return this.businessType;
	}
	
		
	public void setReviewId(java.lang.Integer value) {
		this.reviewId = value;
	}
	@Column(name="reviewId")
	public java.lang.Integer getReviewId() {
		return this.reviewId;
	}

	public void setSerialNum(java.lang.String serialNum) {
		this.serialNum = serialNum;
	}
	@Column(name="serialNum")
	public java.lang.String getSerialNum() {
		return serialNum;
	}
	
		
	public void setCreateBy(java.lang.Integer value) {
		this.createBy = value;
	}
	@Column(name="createBy")
	public java.lang.Integer getCreateBy() {
		return this.createBy;
	}
	@Transient	
	public String getCreateDateString() {
		if(getCreateDate() != null) {
			return this.date2String(getCreateDate(), FORMAT_CREATE_DATE);
		} else {
			return null;
		}
	}
	public void setCreateDateString(String value) {
		setCreateDate(this.string2Date(value, FORMAT_CREATE_DATE));
	}
	
		
	public void setCreateDate(java.util.Date value) {
		this.createDate = value;
	}
	@Column(name="createDate")
	public java.util.Date getCreateDate() {
		return this.createDate;
	}
	
		
	public void setUpdateBy(java.lang.Integer value) {
		this.updateBy = value;
	}
	@Column(name="updateBy")
	public java.lang.Integer getUpdateBy() {
		return this.updateBy;
	}
	@Transient	
	public String getUpdateDateString() {
		if(getUpdateDate() != null) {
			return this.date2String(getUpdateDate(), FORMAT_UPDATE_DATE);
		} else {
			return null;
		}
	}
	public void setUpdateDateString(String value) {
		setUpdateDate(this.string2Date(value, FORMAT_UPDATE_DATE));
	}
	
		
	public void setUpdateDate(java.util.Date value) {
		this.updateDate = value;
	}
	@Column(name="updateDate")
	public java.util.Date getUpdateDate() {
		return this.updateDate;
	}
	
		
	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}
	@Column(name="delFlag")
	public java.lang.String getDelFlag() {
		return this.delFlag;
	}
	@Transient	
	public String getCurrencyMark() {
		if(getCurrencyId() != null) {
			return CurrencyUtils.getCurrencyNameOrFlag(Long.valueOf(getCurrencyId().toString()), "0");
		} else {
			return null;
		}
	}

	public void setCurrencyMark(String currencyMark) {
		this.currencyMark = currencyMark;
	}
	
	public void setReviewUuid(String reviewUuid) {
		this.reviewUuid = reviewUuid;
	}
	@Column(name="review_uuid")
	public String getReviewUuid() {
		return this.reviewUuid;
	}
}