/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.entity;

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
@Table(name = "hotel_pl_preferential_matter_value")
@DynamicInsert(true)
@DynamicUpdate(true)
public class HotelPlPreferentialMatterValue   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "HotelPlPreferentialMatterValue";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "UUID";
	public static final String ALIAS_HOTEL_PL_UUID = "酒店价单UUID";
	public static final String ALIAS_ISLAND_UUID = "岛屿UUID";
	public static final String ALIAS_HOTEL_UUID = "酒店UUID";
	public static final String ALIAS_HOTEL_PL_PREFERENTIAL_UUID = "酒店价单优惠信息uuid";
	public static final String ALIAS_HOTEL_PL_PREFERENTIAL_MATTER_UUID = "酒店价单优惠信息优惠事项表uuid";
	public static final String ALIAS_MY_KEY = "定义的模板生成的输入框key值";
	public static final String ALIAS_MY_VALUE = "定义的模板生成的输入框value值";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_DEL_FLAG = "删除标识";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//根据模板自定义key值，可根据key值得到value值
	
	/**住*/
	public static final String MYKEY_LIVE = "liveNights";
	/**免*/
	public static final String MYKEY_EXEMPT = "freeNights";
	/**提前*/
	public static final String MYKEY_BEFORE = "earlyDays";
	/**总额*/
	public static final String MYKEY_TOTAL = "totalPrice";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"UUID"
	private java.lang.String uuid;
	//"酒店价单UUID"
	private java.lang.String hotelPlUuid;
	//"岛屿UUID"
	private java.lang.String islandUuid;
	//"酒店UUID"
	private java.lang.String hotelUuid;
	//"酒店价单优惠信息uuid"
	private java.lang.String hotelPlPreferentialUuid;
	//"酒店价单优惠信息优惠事项表uuid"
	private java.lang.String hotelPlPreferentialMatterUuid;
	//"定义的模板生成的输入框key值"
	private java.lang.String myKey;
	//"定义的模板生成的输入框value值"
	private java.lang.String myValue;
	//"创建人"
	private java.lang.Integer createBy;
	//"创建时间"
	private java.util.Date createDate;
	//"修改人"
	private java.lang.Integer updateBy;
	//"修改时间"
	private java.util.Date updateDate;
	//"删除标识"
	private java.lang.String delFlag;
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
	public HotelPlPreferentialMatterValue(){
	}

	public HotelPlPreferentialMatterValue(
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
	
		
	public void setHotelPlUuid(java.lang.String value) {
		this.hotelPlUuid = value;
	}
	@Column(name="hotel_pl_uuid")
	public java.lang.String getHotelPlUuid() {
		return this.hotelPlUuid;
	}
	
		
	public void setIslandUuid(java.lang.String value) {
		this.islandUuid = value;
	}
	@Column(name="island_uuid")
	public java.lang.String getIslandUuid() {
		return this.islandUuid;
	}
	
		
	public void setHotelUuid(java.lang.String value) {
		this.hotelUuid = value;
	}
	@Column(name="hotel_uuid")
	public java.lang.String getHotelUuid() {
		return this.hotelUuid;
	}
	
		
	public void setHotelPlPreferentialUuid(java.lang.String value) {
		this.hotelPlPreferentialUuid = value;
	}
	@Column(name="hotel_pl_preferential_uuid")
	public java.lang.String getHotelPlPreferentialUuid() {
		return this.hotelPlPreferentialUuid;
	}
	
		
	public void setHotelPlPreferentialMatterUuid(java.lang.String value) {
		this.hotelPlPreferentialMatterUuid = value;
	}
	@Column(name="hotel_pl_preferential_matter_uuid")
	public java.lang.String getHotelPlPreferentialMatterUuid() {
		return this.hotelPlPreferentialMatterUuid;
	}
	
		
	public void setMyKey(java.lang.String value) {
		this.myKey = value;
	}
	@Column(name="myKey")
	public java.lang.String getMyKey() {
		return this.myKey;
	}
	
		
	public void setMyValue(java.lang.String value) {
		this.myValue = value;
	}
	@Column(name="myValue")
	public java.lang.String getMyValue() {
		return this.myValue;
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


	
}

