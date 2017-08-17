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
import java.util.List;
import java.util.Map;

import javax.persistence.*;


import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.hotel.service.HotelService;
import com.trekiz.admin.modules.hotelPl.module.bean.HotelPlRoom;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "hotel_pl")
@DynamicInsert(true)
@DynamicUpdate(true)
public class HotelPl   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "HotelPl";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "UUID";
	public static final String ALIAS_WHOLESALER_ID = "批发商id";
	public static final String ALIAS_SUPPLIER_INFO_ID = "地接供应商（批发商维护的地接社supplier_info表中的id）";
	public static final String ALIAS_CURRENCY_ID = "币种ID";
	public static final String ALIAS_POSITION = "位置（1、境内；2、境外）";
	public static final String ALIAS_COUNTRY = "国家";
	public static final String ALIAS_AREA_TYPE = "类型（1、内陆；2、海岛）";
	public static final String ALIAS_PURCHASE_TYPE = "采购类型（0内采1外采）";
	public static final String ALIAS_ISLAND_UUID = "岛屿UUID";
	public static final String ALIAS_HOTEL_UUID = "酒店UUID";
	public static final String ALIAS_MIXLIVE_CURRENCY_ID = "混住费币种ID，为了后期支持多币种所有的金额都有对应的币种ID。V1默认都存储默认的币种，即：价单的币种";
	public static final String ALIAS_MIXLIVE_AMOUNT = "混住费金额";
	public static final String ALIAS_TAX_ARITHMETIC = "税金算法（1、连乘；2、分乘）";
	public static final String ALIAS_GALAMEAL_MEMO = "节日餐备注";
	public static final String ALIAS_MEMO = "备注";
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
	//"UUID"
	private java.lang.String uuid;
	//"批发商id"
	private java.lang.Integer wholesalerId;
	//"价单名称"
	private java.lang.String name;
	//"地接供应商（批发商维护的地接社supplier_info表中的id）"
	private java.lang.Integer supplierInfoId;
	//"币种ID"
	private java.lang.Integer currencyId;
	//"位置（1、境内；2、境外）"
	private java.lang.Integer position;
	//"国家"
	private java.lang.String country;
	//"类型（1、内陆；2、海岛）"
	private java.lang.Integer areaType;
	//"采购类型（0内采1外采）"
	private java.lang.Integer purchaseType;
	//"岛屿UUID"
	private java.lang.String islandUuid;
	//"酒店UUID"
	private java.lang.String hotelUuid;
	//"混住费币种ID，为了后期支持多币种所有的金额都有对应的币种ID。V1默认都存储默认的币种，即：价单的币种"
	private java.lang.Integer mixliveCurrencyId;
	//"混住费金额"
	private Double mixliveAmount;
	//"税金算法（1、连乘；2、分乘）"
	private java.lang.Integer taxArithmetic;
	//"节日餐备注"
	private java.lang.String galamealMemo;
	//"备注"
	private java.lang.String memo;
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
	
	
	//常量
	/** 税金算法（连乘） */
	public final static int TAX_ARITHMETIC_CONNECTION = 1;
	/** 税金算法（分乘） */
	public final static int TAX_ARITHMETIC_SEPARATE = 2;
	
	//价单组装数据 START
	/** 酒店税金集合 */
	private List<HotelPlTaxPrice> hotelPlTaxPrices = null;
	/** 酒店税金例外集合 */
	private List<HotelPlTaxException> hotelPlTaxExceptions = null;
	/** 酒店房型价格集合 */
	private Map<String, Map<String, List<HotelPlPrice>>> hotelPlPrices = null;
	/** 交通费用集合 */
	private Map<String, Map<String, List<HotelPlIslandway>>> hotelPlIslandways = null;
	/** 升餐费用集合 */
	private Map<String, Map<String, List<HotelPlMealrise>>> hotelPlMealrises = null;
	/** 强制性节日餐集合 */
	private List<HotelPlHolidayMeal> hotelPlHolidayMeals = null;
	/** 优惠信息集合 */
	private List<HotelPlPreferential> hotelPlPreferentials = null;
	/** 房型备注 */
	private List<HotelPlRoomMemo> hotelPlRoomMemos;
	/** 上岛方式备注 */
	private List<HotelPlIslandwayMemo> hotelPlIslandwayMemos;
	/** 升餐备注 */
	private List<HotelPlRisemealMemo> hotelPlRisemealMemos;
	/** 去重后的酒店房型集合 */
	private List<HotelPlRoom> hotelPlRooms;
	/** 酒店价单价格集合（保存时使用） */
	private List<HotelPlPrice> hotelPlPriceList;
	/**  优惠节日餐的map* */
	private Map<String,List<HotelPlHolidayMeal>> mealMap=null;
	
	
	private String supplierInfoText;//地接社供应商
	private String currencyMark;//币种符号
	private String currencyText;//币种名称
	private String countryText;//国家显示文本
	private String islandText;//海岛游名称
	private String hotelText;//酒店名称
	private String hotelGroup;//"酒店集团"
	private String hotelStar;//"酒店星级"
	private String hotelAddress;//"酒店地址"
	private String contactPhone;//"联系电话"
	private String mixliveCurrencyText;//"混住费用币种符号"
	private String createByText;//创建人显示文本
	private String updateByText;//更新人显示文本
	//价单组装数据 END
	
	private Date string2Date(String dateStr,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return  sdf.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	public HotelPl(){
	}

	public HotelPl(
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
	
		
	public void setWholesalerId(java.lang.Integer value) {
		this.wholesalerId = value;
	}
	@Column(name="wholesaler_id")
	public java.lang.Integer getWholesalerId() {
		return this.wholesalerId;
	}

	public java.lang.String getName() {
		return name;
	}
	public void setName(java.lang.String name) {
		this.name = name;
	}
		
	public void setSupplierInfoId(java.lang.Integer value) {
		this.supplierInfoId = value;
	}
	@Column(name="supplier_info_id")
	public java.lang.Integer getSupplierInfoId() {
		return this.supplierInfoId;
	}
	
		
	public void setCurrencyId(java.lang.Integer value) {
		this.currencyId = value;
	}
	@Column(name="currency_id")
	public java.lang.Integer getCurrencyId() {
		return this.currencyId;
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
	
		
	public void setAreaType(java.lang.Integer value) {
		this.areaType = value;
	}
	@Column(name="area_type")
	public java.lang.Integer getAreaType() {
		return this.areaType;
	}
	
		
	public void setPurchaseType(java.lang.Integer value) {
		this.purchaseType = value;
	}
	@Column(name="purchase_type")
	public java.lang.Integer getPurchaseType() {
		return this.purchaseType;
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
	
		
	public void setMixliveCurrencyId(java.lang.Integer value) {
		this.mixliveCurrencyId = value;
	}
	@Column(name="mixlive_currency_id")
	public java.lang.Integer getMixliveCurrencyId() {
		return this.mixliveCurrencyId;
	}
	
		
	public void setMixliveAmount(Double value) {
		this.mixliveAmount = value;
	}
	@Column(name="mixlive_amount")
	public Double getMixliveAmount() {
		return this.mixliveAmount;
	}
	
		
	public void setGalamealMemo(java.lang.String value) {
		this.galamealMemo = value;
	}
	@Column(name="galameal_memo")
	public java.lang.String getGalamealMemo() {
		return this.galamealMemo;
	}
	

	public void setTaxArithmetic(java.lang.Integer value) {
		this.taxArithmetic = value;
	}
	@Column(name="tax_arithmetic")
	public java.lang.Integer getTaxArithmetic() {
		return this.taxArithmetic;
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

	@Transient
	public List<HotelPlTaxPrice> getHotelPlTaxPrices() {
		return hotelPlTaxPrices;
	}

	public void setHotelPlTaxPrices(List<HotelPlTaxPrice> hotelPlTaxPrices) {
		this.hotelPlTaxPrices = hotelPlTaxPrices;
	}
	@Transient
	public List<HotelPlTaxException> getHotelPlTaxExceptions() {
		return hotelPlTaxExceptions;
	}

	public void setHotelPlTaxExceptions(
			List<HotelPlTaxException> hotelPlTaxExceptions) {
		this.hotelPlTaxExceptions = hotelPlTaxExceptions;
	}
	@Transient
	public Map<String, Map<String, List<HotelPlPrice>>> getHotelPlPrices() {
		return hotelPlPrices;
	}
	public void setHotelPlPrices(
			Map<String, Map<String, List<HotelPlPrice>>> hotelPlPrices) {
		this.hotelPlPrices = hotelPlPrices;
	}
	@Transient
	public Map<String, Map<String, List<HotelPlIslandway>>> getHotelPlIslandways() {
		return hotelPlIslandways;
	}

	public void setHotelPlIslandways(Map<String, Map<String, List<HotelPlIslandway>>> hotelPlIslandways) {
		this.hotelPlIslandways = hotelPlIslandways;
	}
	
	
	@Transient
	public Map<String, Map<String, List<HotelPlMealrise>>> getHotelPlMealrises() {
		return hotelPlMealrises;
	}
	public void setHotelPlMealrises(
			Map<String, Map<String, List<HotelPlMealrise>>> hotelPlMealrises) {
		this.hotelPlMealrises = hotelPlMealrises;
	}
	@Transient
	public List<HotelPlPreferential> getHotelPlPreferentials() {
		return hotelPlPreferentials;
	}

	public void setHotelPlPreferentials(
			List<HotelPlPreferential> hotelPlPreferentials) {
		this.hotelPlPreferentials = hotelPlPreferentials;
	}


	@Transient
	public List<HotelPlRoomMemo> getHotelPlRoomMemos() {
		return hotelPlRoomMemos;
	}

	public void setHotelPlRoomMemos(List<HotelPlRoomMemo> hotelPlRoomMemos) {
		this.hotelPlRoomMemos = hotelPlRoomMemos;
	}
	@Transient
	public List<HotelPlIslandwayMemo> getHotelPlIslandwayMemos() {
		return hotelPlIslandwayMemos;
	}

	public void setHotelPlIslandwayMemos(List<HotelPlIslandwayMemo> hotelPlIslandwayMemos) {
		this.hotelPlIslandwayMemos = hotelPlIslandwayMemos;
	}
	@Transient
	public List<HotelPlRisemealMemo> getHotelPlRisemealMemos() {
		return hotelPlRisemealMemos;
	}

	public void setHotelPlRisemealMemos(List<HotelPlRisemealMemo> hotelPlRisemealMemos) {
		this.hotelPlRisemealMemos = hotelPlRisemealMemos;
	}
	

	private String date2String(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	@Transient
	public String getHotelGroup() {
		if(StringUtils.isEmpty(this.hotelGroup)) {
			HotelService hotelService = SpringContextHolder.getBean(HotelService.class);
			this.hotelGroup = hotelService.getHotelGroupByUuid(hotelUuid);
		}
		
		return hotelGroup;
	}

	public void setHotelGroup(String hotelGroup) {
		this.hotelGroup = hotelGroup;
	}
	@Transient
	public String getHotelStar() {
		return hotelStar;
	}

	public void setHotelStar(String hotelStar) {
		this.hotelStar = hotelStar;
	}
	@Transient
	public String getHotelAddress() {
		return hotelAddress;
	}

	public void setHotelAddress(String hotelAddress) {
		this.hotelAddress = hotelAddress;
	}
	@Transient
	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
	@Transient
	public String getSupplierInfoText() {
		return supplierInfoText;
	}
	public void setSupplierInfoText(String supplierInfoText) {
		this.supplierInfoText = supplierInfoText;
	}
	@Transient
	public String getCurrencyText() {
		return currencyText;
	}
	public void setCurrencyText(String currencyText) {
		this.currencyText = currencyText;
	}
	@Transient
	public String getCountryText() {
		return countryText;
	}
	public void setCountryText(String countryText) {
		this.countryText = countryText;
	}
	@Transient
	public String getIslandText() {
		return islandText;
	}
	public void setIslandText(String islandText) {
		this.islandText = islandText;
	}
	@Transient
	public String getHotelText() {
		return hotelText;
	}
	public void setHotelText(String hotelText) {
		this.hotelText = hotelText;
	}
	@Transient
	public String getMixliveCurrencyText() {
		return mixliveCurrencyText;
	}
	public void setMixliveCurrencyText(String mixliveCurrencyText) {
		this.mixliveCurrencyText = mixliveCurrencyText;
	}

	@Transient
	public String getCurrencyMark() {
		return currencyMark;
	}
	public void setCurrencyMark(String currencyMark) {
		this.currencyMark = currencyMark;
	}
	@Transient
	public String getCreateByText() {
		return createByText;
	}
	public void setCreateByText(String createByText) {
		this.createByText = createByText;
	}
	@Transient
	public String getUpdateByText() {
		return updateByText;
	}
	public void setUpdateByText(String updateByText) {
		this.updateByText = updateByText;
	}
	@Transient
	public List<HotelPlRoom> getHotelPlRooms() {
		return hotelPlRooms;
	}
	public void setHotelPlRooms(List<HotelPlRoom> hotelPlRooms) {
		this.hotelPlRooms = hotelPlRooms;
	}
	@Transient
	public List<HotelPlPrice> getHotelPlPriceList() {
		return hotelPlPriceList;
	}
	public void setHotelPlPriceList(List<HotelPlPrice> hotelPlPriceList) {
		this.hotelPlPriceList = hotelPlPriceList;
	}
	@Transient
	public List<HotelPlHolidayMeal> getHotelPlHolidayMeals() {
		return hotelPlHolidayMeals;
	}
	public void setHotelPlHolidayMeals(List<HotelPlHolidayMeal> hotelPlHolidayMeals) {
		this.hotelPlHolidayMeals = hotelPlHolidayMeals;
	}
	@Transient
	public Map<String, List<HotelPlHolidayMeal>> getMealMap() {
		return mealMap;
	}
	public void setMealMap(Map<String, List<HotelPlHolidayMeal>> mealMap) {
		this.mealMap = mealMap;
	}
	
	
}

