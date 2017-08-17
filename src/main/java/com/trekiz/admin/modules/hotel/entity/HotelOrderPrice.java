/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.entity;

import java.math.BigDecimal;
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

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "hotel_order_price")
@DynamicInsert(true)
@DynamicUpdate(true)
public class HotelOrderPrice   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "HotelOrderPrice";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_ORDER_UUID = "订单uuid";
	public static final String ALIAS_PRICE_TYPE = "价格类型：1 团期价格类型；2 返佣类型；3 优惠类型 4 其他类型";
	public static final String ALIAS_PRICE_NAME = "金额名称";
	public static final String ALIAS_ACTIVITY_HOTEL_GROUP_PRICE_UUID = "酒店产品团期价格表UUID";
	public static final String ALIAS_CURRENCY_ID = "币种";
	public static final String ALIAS_PRICE = "价格";
	public static final String ALIAS_NUM = "个数";
	public static final String ALIAS_REMARK = "备注";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_DEL_FLAG = "删除状态";
	public static final String ALIAS_TRAVELER_TYPE = "游客类型";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"订单uuid"
	private java.lang.String orderUuid;
	//"价格类型：1 团期价格类型；2 返佣类型；3 优惠类型 4 其他类型"
	private java.lang.Integer priceType;
	//"金额名称"
	private java.lang.String priceName;
	//"酒店产品团期价格表UUID"
	private java.lang.String activityHotelGroupPriceUuid;
	//"币种"
	private java.lang.Integer currencyId;
	//"价格"
	private Double price;
	//"个数"
	private java.lang.Integer num;
	//"备注"
	private java.lang.String remark;
	//"创建人"
	private java.lang.Integer createBy;
	//"创建时间"
	private java.util.Date createDate;
	//"修改人"
	private java.lang.Integer updateBy;
	//"修改时间"
	private java.util.Date updateDate;
	//"删除状态"
	private java.lang.String delFlag;
	//"游客类型"
	private java.lang.String travelerType;

	private BigDecimal subTotal;	//对应页面中的小计(人数 *同行价)
	
	/** 团期价格 */
	public static final int PRICE_TYPE_GROUP = 1;
	/** 返佣 */
	public static final int PRICE_TYPE_RETURN = 2;
	/** 优惠 */
	public static final int PRICE_TYPE_PREFERENTIAL = 3;
	/** 其他 */
	public static final int PRICE_TYPE_OTHER = 4;
	/** 退款 */
	public static final int PRICE_TYPE_REFUND = 5;
	
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
	public HotelOrderPrice(){
	}

	public HotelOrderPrice(
		java.lang.Integer id
	){
		this.id = id;
	}

	
		
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public java.lang.Integer getId() {
		return this.id;
	}
	
		
	public void setUuid(java.lang.String value) {
		this.uuid = value;
	}
	@Column(name="uuid")
	public java.lang.String getUuid() {
		return this.uuid;
	}
	
		
	public void setOrderUuid(java.lang.String value) {
		this.orderUuid = value;
	}
	@Column(name="order_uuid")
	public java.lang.String getOrderUuid() {
		return this.orderUuid;
	}
	
		
	public void setPriceType(java.lang.Integer value) {
		this.priceType = value;
	}
	@Column(name="price_type")
	public java.lang.Integer getPriceType() {
		return this.priceType;
	}
	
		
	public void setPriceName(java.lang.String value) {
		this.priceName = value;
	}
	@Column(name="price_name")
	public java.lang.String getPriceName() {
		return this.priceName;
	}
	
		
	public void setActivityHotelGroupPriceUuid(java.lang.String value) {
		this.activityHotelGroupPriceUuid = value;
	}
	@Column(name="activity_hotel_group_price_uuid")
	public java.lang.String getActivityHotelGroupPriceUuid() {
		return this.activityHotelGroupPriceUuid;
	}
	
		
	public void setCurrencyId(java.lang.Integer value) {
		this.currencyId = value;
	}
	@Column(name="currency_id")
	public java.lang.Integer getCurrencyId() {
		return this.currencyId;
	}
	
		
	public void setPrice(Double value) {
		this.price = value;
	}
	@Column(name="price")
	public Double getPrice() {
		return this.price;
	}
	
		
	public void setNum(java.lang.Integer value) {
		this.num = value;
	}
	@Column(name="num")
	public java.lang.Integer getNum() {
		return this.num;
	}
	
		
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	@Column(name="remark")
	public java.lang.String getRemark() {
		return this.remark;
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
	public java.lang.String getPriceTypeStr() {
		String priceTypeStr = "";
		switch(this.priceType){
			case PRICE_TYPE_GROUP : priceTypeStr = "团期"; break;
			case PRICE_TYPE_RETURN : priceTypeStr = "返佣"; break;
			case PRICE_TYPE_PREFERENTIAL : priceTypeStr = "优惠"; break;
			case PRICE_TYPE_OTHER : priceTypeStr = "其他"; break;
			case PRICE_TYPE_REFUND : priceTypeStr = "退款"; break;
		}
		return priceTypeStr;
	}

	@Column(name="travelerType")
	public java.lang.String getTravelerType() {
		return travelerType;
	}

	public void setTravelerType(java.lang.String travelerType) {
		this.travelerType = travelerType;
	}

	@Transient
	public BigDecimal getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}
	
	
	
}

