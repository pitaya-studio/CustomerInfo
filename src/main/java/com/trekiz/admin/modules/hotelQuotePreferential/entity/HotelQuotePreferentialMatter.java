/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelQuotePreferential.entity;

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
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialMatterValue;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialTax;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "hotel_quote_preferential_matter")
@DynamicInsert(true)
@DynamicUpdate(true)
public class HotelQuotePreferentialMatter   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "HotelQuotePreferentialMatter";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_HOTEL_PL_UUID = "酒店价单UUID";
	public static final String ALIAS_ISLAND_UUID = "岛屿UUID";
	public static final String ALIAS_HOTEL_UUID = "酒店UUID";
	public static final String ALIAS_HOTEL_QUOTE_PREFERENTIAL_UUID = "酒店价单优惠信息uuid";
	public static final String ALIAS_PREFERENTIAL_TEMPLATES_UUID = "优惠模板uuid";
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
	//"uuid"
	private java.lang.String uuid;
	//"酒店价单UUID"
	private java.lang.String hotelPlUuid;
	//"岛屿UUID"
	private java.lang.String islandUuid;
	//"酒店UUID"
	private java.lang.String hotelUuid;
	//"酒店价单优惠信息uuid"
	private java.lang.String hotelQuotePreferentialUuid;
	//"优惠模板uuid"
	private java.lang.String preferentialTemplatesUuid;
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
	
	private String preferentialTemplatesText;//优惠类型
	private String preferentialTemplatesDetailText;//详细优惠信息
	private String type;//优惠模板类型（1、住付优惠2、提前预定3、打包价格4、房/餐/交通优惠）
	private Map<String,List<HotelPlPreferentialTax>> preferentialTaxMap;//税金列表 KEY 1、房费；2、餐费；3、交通费
	private List<HotelPlPreferentialMatterValue> matterValues;//优惠事项输入信息集合
	
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
	public HotelQuotePreferentialMatter(){
	}

	public HotelQuotePreferentialMatter(
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
	
		
	public void setHotelQuotePreferentialUuid(java.lang.String value) {
		this.hotelQuotePreferentialUuid = value;
	}
	@Column(name="hotel_quote_preferential_uuid")
	public java.lang.String getHotelQuotePreferentialUuid() {
		return this.hotelQuotePreferentialUuid;
	}
	
		
	public void setPreferentialTemplatesUuid(java.lang.String value) {
		this.preferentialTemplatesUuid = value;
	}
	@Column(name="preferential_templates_uuid")
	public java.lang.String getPreferentialTemplatesUuid() {
		return this.preferentialTemplatesUuid;
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
	public String getPreferentialTemplatesText() {
		return preferentialTemplatesText;
	}

	public void setPreferentialTemplatesText(String preferentialTemplatesText) {
		this.preferentialTemplatesText = preferentialTemplatesText;
	}
	@Transient	
	public String getPreferentialTemplatesDetailText() {
		return preferentialTemplatesDetailText;
	}

	public void setPreferentialTemplatesDetailText(
			String preferentialTemplatesDetailText) {
		this.preferentialTemplatesDetailText = preferentialTemplatesDetailText;
	}
	@Transient	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	@Transient	
	public Map<String, List<HotelPlPreferentialTax>> getPreferentialTaxMap() {
		return preferentialTaxMap;
	}

	public void setPreferentialTaxMap(
			Map<String, List<HotelPlPreferentialTax>> preferentialTaxMap) {
		this.preferentialTaxMap = preferentialTaxMap;
	}
	@Transient	
	public List<HotelPlPreferentialMatterValue> getMatterValues() {
		return matterValues;
	}

	public void setMatterValues(List<HotelPlPreferentialMatterValue> matterValues) {
		this.matterValues = matterValues;
	}
	
}

