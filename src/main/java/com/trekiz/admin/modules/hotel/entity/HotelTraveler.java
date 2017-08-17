/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.persistence.BaseEntity;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "hotel_traveler")
@DynamicInsert(true)
@DynamicUpdate(true)
public class HotelTraveler   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "HotelTraveler";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = " 游客UUID";
	public static final String ALIAS_ORDER_UUID = " 订单UUID";
	public static final String ALIAS_NAME = "游客姓名";
	public static final String ALIAS_NAME_SPELL = "游客名称拼音";
	public static final String ALIAS_PERSON_TYPE = "游客类型";
	public static final String ALIAS_SEX = "性别 1-男 2-女";
	public static final String ALIAS_NATIONALITY = "国籍";
	public static final String ALIAS_BIRTH_DAY = "出生日期";
	public static final String ALIAS_TELEPHONE = "手机号";
	public static final String ALIAS_REMARK = "备注";
	public static final String ALIAS_SRC_PRICE = "单价（发布产品时的定价）";
	public static final String ALIAS_SRC_PRICE_CURRENCY = "单价币种";
	public static final String ALIAS_ORIGINAL_PAY_PRICE_SERIAL_NUM = "游客原始应收价 一次生成 永不改变";
	public static final String ALIAS_COST_PRICE_SERIAL_NUM = "游客成本价UUID";
	public static final String ALIAS_PAY_PRICE_SERIAL_NUM = "游客结算价UUID";
	public static final String ALIAS_REBATES_MONEY_SERIAL_NUM = "游客返佣UUID";
	public static final String ALIAS_JK_SERIAL_NUM = "游客借款UUID";
	public static final String ALIAS_STATUS = "正常 0；删除 1；退团审核 2；已退团 3；转团审核 4；已转团 5";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建时间";
	public static final String ALIAS_UPDATE_BY = "修改人";
	public static final String ALIAS_UPDATE_DATE = "修改时间";
	public static final String ALIAS_DEL_FLAG = "删除状态";
	
	//date formats
	public static final String FORMAT_BIRTH_DAY = "yyyy-MM-dd";
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"主键"
	private java.lang.Integer id;
	//" 游客UUID"
	private java.lang.String uuid;
	//" 订单UUID"
	private java.lang.String orderUuid;
	//"游客姓名"
	private java.lang.String name;
	//"游客名称拼音"
	private java.lang.String nameSpell;
	//"游客类型"
	private java.lang.String personType;
	//"性别 1-男 2-女"
	private Integer sex;
	//"国籍"
	private java.lang.Integer nationality;
	//"出生日期"
	private java.util.Date birthDay;
	//"手机号"
	private java.lang.String telephone;
	//"备注"
	private java.lang.String remark;
	//"单价（发布产品时的定价）"
	private java.lang.Double srcPrice;
	//"单价币种"
	private java.lang.Integer srcPriceCurrency;
	//"游客原始应收价 一次生成 永不改变"
	private java.lang.String originalPayPriceSerialNum;
	//"游客成本价UUID"
	private java.lang.String costPriceSerialNum;
	//"游客结算价UUID"
	private java.lang.String payPriceSerialNum;
	//"游客返佣UUID"
	private java.lang.String rebatesMoneySerialNum;
	//"游客借款UUID"
	private java.lang.String jkSerialNum;
	//"正常 0；删除 1；退团审核 2；已退团 3；转团审核 4；已转团 5"
	private java.lang.String status;
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
	//columns END
	
	//数据结构
	//签证信息
	private List<HotelTravelervisa> hotelTravelervisaList;
	//附件信息
	private List<HotelAnnex> hotelTravelerFilesList;
	//游客金额信息
	private List<HotelMoneyAmount> hotelMoneyAmountList;
	//游客的证件信息
	private List<HotelTravelerPapersType> hotelTravelerPapersTypeList;
	
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
	public HotelTraveler(){
	}

	public HotelTraveler(
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
	
		
	public void setName(java.lang.String value) {
		this.name = value;
	}
	@Column(name="name")
	public java.lang.String getName() {
		return this.name;
	}
	
		
	public void setNameSpell(java.lang.String value) {
		this.nameSpell = value;
	}
	@Column(name="nameSpell")
	public java.lang.String getNameSpell() {
		return this.nameSpell;
	}
	
		
	public void setPersonType(java.lang.String value) {
		this.personType = value;
	}
	@Column(name="personType")
	public java.lang.String getPersonType() {
		return this.personType;
	}
	
		
	public void setSex(Integer value) {
		this.sex = value;
	}
	@Column(name="sex")
	public Integer getSex() {
		return this.sex;
	}
	
		
	public void setNationality(java.lang.Integer value) {
		this.nationality = value;
	}
	@Column(name="nationality")
	public java.lang.Integer getNationality() {
		return this.nationality;
	}
	@Transient	
	public String getBirthDayString() {
		if(getBirthDay() != null) {
			return this.date2String(getBirthDay(), FORMAT_BIRTH_DAY);
		} else {
			return null;
		}
	}
	public void setBirthDayString(String value) {
		setBirthDay(this.string2Date(value, FORMAT_BIRTH_DAY));
	}
	
		
	public void setBirthDay(java.util.Date value) {
		this.birthDay = value;
	}
	@Column(name="birthDay")
	public java.util.Date getBirthDay() {
		return this.birthDay;
	}
	
		
	public void setTelephone(java.lang.String value) {
		this.telephone = value;
	}
	@Column(name="telephone")
	public java.lang.String getTelephone() {
		return this.telephone;
	}
	
		
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	@Column(name="remark")
	public java.lang.String getRemark() {
		return this.remark;
	}
	
		
	public void setSrcPrice(java.lang.Double value) {
		this.srcPrice = value;
	}
	@Column(name="srcPrice")
	public java.lang.Double getSrcPrice() {
		return this.srcPrice;
	}
	
		
	public void setSrcPriceCurrency(java.lang.Integer value) {
		this.srcPriceCurrency = value;
	}
	@Column(name="srcPriceCurrency")
	public java.lang.Integer getSrcPriceCurrency() {
		return this.srcPriceCurrency;
	}
	
		
	public void setOriginalPayPriceSerialNum(java.lang.String value) {
		this.originalPayPriceSerialNum = value;
	}
	@Column(name="original_payPriceSerialNum")
	public java.lang.String getOriginalPayPriceSerialNum() {
		return this.originalPayPriceSerialNum;
	}
	
		
	public void setCostPriceSerialNum(java.lang.String value) {
		this.costPriceSerialNum = value;
	}
	@Column(name="costPriceSerialNum")
	public java.lang.String getCostPriceSerialNum() {
		return this.costPriceSerialNum;
	}
	
		
	public void setPayPriceSerialNum(java.lang.String value) {
		this.payPriceSerialNum = value;
	}
	@Column(name="payPriceSerialNum")
	public java.lang.String getPayPriceSerialNum() {
		return this.payPriceSerialNum;
	}
	
		
	public void setRebatesMoneySerialNum(java.lang.String value) {
		this.rebatesMoneySerialNum = value;
	}
	@Column(name="rebates_moneySerialNum")
	public java.lang.String getRebatesMoneySerialNum() {
		return this.rebatesMoneySerialNum;
	}
	
		
	public void setJkSerialNum(java.lang.String value) {
		this.jkSerialNum = value;
	}
	@Column(name="jkSerialNum")
	public java.lang.String getJkSerialNum() {
		return this.jkSerialNum;
	}
	
		
	public void setStatus(java.lang.String value) {
		this.status = value;
	}
	@Column(name="status")
	public java.lang.String getStatus() {
		return this.status;
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
	public List<HotelTravelervisa> getHotelTravelervisaList() {
		return hotelTravelervisaList;
	}

	public void setHotelTravelervisaList(List<HotelTravelervisa> hotelTravelervisaList) {
		this.hotelTravelervisaList = hotelTravelervisaList;
	}

	@Transient
	public List<HotelAnnex> getHotelTravelerFilesList() {
		return hotelTravelerFilesList;
	}

	public void setHotelTravelerFilesList(List<HotelAnnex> hotelTravelerFilesList) {
		this.hotelTravelerFilesList = hotelTravelerFilesList;
	}

	@Transient
	public List<HotelMoneyAmount> getHotelMoneyAmountList() {
		return hotelMoneyAmountList;
	}

	public void setHotelMoneyAmountList(List<HotelMoneyAmount> hotelMoneyAmountList) {
		this.hotelMoneyAmountList = hotelMoneyAmountList;
	}

	@Transient
	public List<HotelTravelerPapersType> getHotelTravelerPapersTypeList() {
		return hotelTravelerPapersTypeList;
	}

	public void setHotelTravelerPapersTypeList(List<HotelTravelerPapersType> hotelTravelerPapersTypeList) {
		this.hotelTravelerPapersTypeList = hotelTravelerPapersTypeList;
	}
	

	
}

