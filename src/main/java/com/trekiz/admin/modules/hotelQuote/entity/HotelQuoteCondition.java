/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuote.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import com.trekiz.admin.modules.supplier.entity.SupplierInfo;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "hotel_quote_condition")
@DynamicInsert(true)
@DynamicUpdate(true)
public class HotelQuoteCondition   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "HotelQuoteCondition";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_HOTEL_QUOTE_UUID = "报价表UUID";
	public static final String ALIAS_POSITION = "位置（1、境内；2、境外）";
	public static final String ALIAS_COUNTRY = "国家";
	public static final String ALIAS_ISLAND_UUID = "岛屿";
	public static final String ALIAS_HOTEL_UUID = "酒店";
	public static final String ALIAS_SUPPLIER_INFO_ID = "地接供应商";
	public static final String ALIAS_PURCHASE_TYPE = "采购类型";
	public static final String ALIAS_DEPARTURE_ISLAND_WAY = "去程交通";
	public static final String ALIAS_ARRIVAL_ISLAND_WAY = "去程交通";
	public static final String ALIAS_ROOM_NUM = "房间数  ";
	public static final String ALIAS_MIXLIVE_NUM = "混住次数";
	public static final String ALIAS_SORT = "排序（报价的次数）";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_DEL_FLAG = "删除标识";
	
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"报价表UUID"
	private java.lang.String hotelQuoteUuid;
	//"位置（1、境内；2、境外）"
	private java.lang.Integer position;
	//"国家"
	private java.lang.String country;
	//"岛屿"
	private java.lang.String islandUuid;
	//"酒店"
	private java.lang.String hotelUuid;
	//"地接供应商"
	private java.lang.Integer supplierInfoId;
	//"采购类型"
	private java.lang.Integer purchaseType;
	//"去程交通"
	private java.lang.String departureIslandWay;
	//"去程交通"
	private java.lang.String arrivalIslandWay;
	//"房间数  "
	private java.lang.Integer roomNum;
	//"混住次数"
	private java.lang.Integer mixliveNum;
	//"排序（报价的次数）"
	private java.lang.Integer sort;
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
	//报价结果表
	private List<HotelQuoteResult> hotelQuoteResult;
	//报价结果明细表
	private Map<String, List<HotelQuoteResultDetail>> relMap;
	//条件明细人数表
	private List<HotelQuoteConditionDetailPersonNum> conditionDetailPersonNum ;
	//供应商
	private SupplierInfo supplierInfo;
	//酒店价单UUID
	private java.lang.String hotelPlUuid;
	
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
	public HotelQuoteCondition(){
	}

	public HotelQuoteCondition(
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
	
		
	public void setHotelQuoteUuid(java.lang.String value) {
		this.hotelQuoteUuid = value;
	}
	@Column(name="hotel_quote_uuid")
	public java.lang.String getHotelQuoteUuid() {
		return this.hotelQuoteUuid;
	}
	
		
	public void setPosition(java.lang.Integer value) {
		this.position = value;
	}
	@Column(name="position")
	public java.lang.Integer getPosition() {
		return this.position;
	}
	
		
	public void setCountry(java.lang.String value) {
		this.country = value;
	}
	@Column(name="country")
	public java.lang.String getCountry() {
		return this.country;
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
	
		
	public void setSupplierInfoId(java.lang.Integer value) {
		this.supplierInfoId = value;
	}
	@Column(name="supplier_info_id")
	public java.lang.Integer getSupplierInfoId() {
		return this.supplierInfoId;
	}
	
		
	public void setPurchaseType(java.lang.Integer value) {
		this.purchaseType = value;
	}
	@Column(name="purchase_type")
	public java.lang.Integer getPurchaseType() {
		return this.purchaseType;
	}
	
		
	public void setDepartureIslandWay(java.lang.String value) {
		this.departureIslandWay = value;
	}
	@Column(name="departure_islandWay")
	public java.lang.String getDepartureIslandWay() {
		return this.departureIslandWay;
	}
	
		
	public void setArrivalIslandWay(java.lang.String value) {
		this.arrivalIslandWay = value;
	}
	@Column(name="arrival_islandWay")
	public java.lang.String getArrivalIslandWay() {
		return this.arrivalIslandWay;
	}
	
		
	public void setRoomNum(java.lang.Integer value) {
		this.roomNum = value;
	}
	@Column(name="roomNum")
	public java.lang.Integer getRoomNum() {
		return this.roomNum;
	}
	
		
	public void setMixliveNum(java.lang.Integer value) {
		this.mixliveNum = value;
	}
	@Column(name="mixliveNum")
	public java.lang.Integer getMixliveNum() {
		return this.mixliveNum;
	}
	
		
	public void setSort(java.lang.Integer value) {
		this.sort = value;
	}
	@Column(name="sort")
	public java.lang.Integer getSort() {
		return this.sort;
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
	public List<HotelQuoteResult> getHotelQuoteResult() {
		return hotelQuoteResult;
	}

	public void setHotelQuoteResult(List<HotelQuoteResult> hotelQuoteResult) {
		this.hotelQuoteResult = hotelQuoteResult;
	}
	@Transient
	public Map<String, List<HotelQuoteResultDetail>> getRelMap() {
		return relMap;
	}

	public void setRelMap(Map<String, List<HotelQuoteResultDetail>> relMap) {
		this.relMap = relMap;
	}
	@Transient
	public List<HotelQuoteConditionDetailPersonNum> getConditionDetailPersonNum() {
		return conditionDetailPersonNum;
	}

	public void setConditionDetailPersonNum(
			List<HotelQuoteConditionDetailPersonNum> conditionDetailPersonNum) {
		this.conditionDetailPersonNum = conditionDetailPersonNum;
	}
	@Transient
	public SupplierInfo getSupplierInfo() {
		return supplierInfo;
	}

	public void setSupplierInfo(SupplierInfo supplierInfo) {
		this.supplierInfo = supplierInfo;
	}
	
	@Column(name="hotel_pl_uuid")
	public java.lang.String getHotelPlUuid() {
		return hotelPlUuid;
	}

	public void setHotelPlUuid(java.lang.String hotelPlUuid) {
		this.hotelPlUuid = hotelPlUuid;
	}
	
}

