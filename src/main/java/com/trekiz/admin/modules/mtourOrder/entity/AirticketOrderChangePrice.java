/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.entity;

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
@Table(name = "airticket_order_changePrice")
@DynamicInsert(true)
@DynamicUpdate(true)
public class AirticketOrderChangePrice   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "AirticketOrderChangePrice";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "UUID";
	public static final String ALIAS_AIRTICKET_ORDER_ID = "机票订单id";
	public static final String ALIAS_CHANGE_TYPE = "修改类型（0：成本，1：外报价）";
	public static final String ALIAS_FUNDS_NAME = "款项名称";
	public static final String ALIAS_COMPUTE_TYPE = "计算类型（0：增加，1：减少）";
	public static final String ALIAS_CURRENCY_ID = "币种";
	public static final String ALIAS_PRICE = "价格";
	public static final String ALIAS_MEMO = "备注";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "创建时间";
	public static final String ALIAS_DEL_FLAG = "删除标识";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"UUID"
	private java.lang.String uuid;
	//"机票订单id"
	private java.lang.Integer airticketOrderId;
	//"修改类型（0：成本，1：外报价）"
	//private java.lang.Integer changeType;
	//"款项名称"
	//private java.lang.String fundsName;
	//"计算类型（0：增加，1：减少）"
	private java.lang.Integer computeType;
	//"币种"
	private java.lang.Integer currencyId;
	//"价格"
	private Double price;
	//"备注"
	private java.lang.String memo;
	//"创建人"
	private java.lang.Integer createBy;
	//"创建时间"
	private java.util.Date createDate;
	//"修改人"
	private java.lang.Integer updateBy;
	//"创建时间"
	private java.util.Date updateDate;
	//"删除标识"
	private java.lang.String delFlag;
	private Double exchangerate;
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
	public AirticketOrderChangePrice(){
	}

	public AirticketOrderChangePrice(
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
	
		
	public void setAirticketOrderId(java.lang.Integer value) {
		this.airticketOrderId = value;
	}
	@Column(name="airticket_order_id")
	public java.lang.Integer getAirticketOrderId() {
		return this.airticketOrderId;
	}
	
		
	/*public void setChangeType(java.lang.Integer value) {
		this.changeType = value;
	}
	@Column(name="change_type")
	public java.lang.Integer getChangeType() {
		return this.changeType;
	}
	
		
	public void setFundsName(java.lang.String value) {
		this.fundsName = value;
	}
	@Column(name="funds_name")
	public java.lang.String getFundsName() {
		return this.fundsName;
	}*/
	
		
	public void setComputeType(java.lang.Integer value) {
		this.computeType = value;
	}
	@Column(name="compute_type")
	public java.lang.Integer getComputeType() {
		return this.computeType;
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
	
		
	public void setMemo(java.lang.String value) {
		this.memo = value;
	}
	@Column(name="memo")
	public java.lang.String getMemo() {
		return this.memo;
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
	@Column(name="exchangerate")
	public Double getExchangerate() {
		return exchangerate;
	}

	public void setExchangerate(Double exchangerate) {
		this.exchangerate = exchangerate;
	}
	
}

