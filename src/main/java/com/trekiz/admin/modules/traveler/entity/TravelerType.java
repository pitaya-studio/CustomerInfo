/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.traveler.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.persistence.BaseEntity;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "traveler_type")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TravelerType   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	public static final String ALIAS_ADULT_UUID = "3b23624f1db94deaa32861d642f56f79";//成人的uuid
	//date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	private static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	private java.lang.Integer id;
	private java.lang.String uuid;
	private java.lang.Integer sort;
	private java.lang.String name;
	private java.lang.Integer rangeFrom;
	private java.lang.Integer rangeTo;
	private java.lang.String sysTravelerType;
	private java.lang.String status;
	private java.lang.String description;
	private java.lang.Integer wholesalerId;
	private java.lang.String shortName;
	private java.lang.Integer createBy;
	private java.util.Date createDate;
	private java.lang.Integer updateBy;
	private java.util.Date updateDate;
	private java.lang.String delFlag;
	private java.lang.String applyProduct;
	private java.lang.String applyProductName;
	private java.lang.Integer personType;//系统 人员类型（0：成人、1婴儿、2儿童）；
	/** 启用状态（停用） */
	public static final String STATUS_OFF = "0";
	/** 启用状态（启用） */
	public static final String STATUS_ON = "1";
	
	public void setApplyProductName(java.lang.String applyProductName) {
		this.applyProductName = applyProductName;
	}
	@Transient
	public java.lang.String getApplyProductName() {
		applyProductName =  getApplyProductName(applyProduct);
		return applyProductName;
	}

	private java.lang.String getApplyProductName(String applyProduct) {
		String applyProductName = "";
		String splitStr = ",";
		if(applyProduct!=null &&!"".equals(applyProduct)){
			String[] strArr = applyProduct.split(",");
			for(int i=0;i<strArr.length;i++){
				if(i!=strArr.length-1){
					if("1".equals(strArr[i])){
						applyProductName = applyProductName + "单团" + splitStr;
					}else if("2".equals(strArr[i])){
						applyProductName = applyProductName + "散拼" + splitStr;
					}else if("3".equals(strArr[i])){
						applyProductName = applyProductName + "游学" + splitStr;
					}else if("4".equals(strArr[i])){
						applyProductName = applyProductName + "大客户" + splitStr;
					}else if("5".equals(strArr[i])){
						applyProductName = applyProductName + "自由行" + splitStr;
					}else if("6".equals(strArr[i])){
						applyProductName = applyProductName + "签证" + splitStr;
					}else if("7".equals(strArr[i])){
						applyProductName = applyProductName + "机票" + splitStr;
					}else if("10".equals(strArr[i])){
						applyProductName = applyProductName + "游轮" + splitStr;
					}else if("11".equals(strArr[i])){
						applyProductName = applyProductName + "酒店" + splitStr;
					}else if("12".equals(strArr[i])){
						applyProductName = applyProductName + "海岛游" + splitStr;
					}
				}else{
					if("1".equals(strArr[i])){
						applyProductName = applyProductName + "单团";
					}else if("2".equals(strArr[i])){
						applyProductName = applyProductName + "散拼";
					}else if("3".equals(strArr[i])){
						applyProductName = applyProductName + "游学";
					}else if("4".equals(strArr[i])){
						applyProductName = applyProductName + "大客户";
					}else if("5".equals(strArr[i])){
						applyProductName = applyProductName + "自由行";
					}else if("6".equals(strArr[i])){
						applyProductName = applyProductName + "签证";
					}else if("7".equals(strArr[i])){
						applyProductName = applyProductName + "机票";
					}else if("10".equals(strArr[i])){
						applyProductName = applyProductName + "游轮";
					}else if("11".equals(strArr[i])){
						applyProductName = applyProductName + "酒店";
					}else if("12".equals(strArr[i])){
						applyProductName = applyProductName + "海岛游";
					}
				}
				
			}
		}
//		System.out.println("applyProduct: " + applyProduct + "| applyProductName: " + applyProductName);
		return applyProductName;
	}


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
	public TravelerType(){
	}

	public TravelerType(Integer wholesalerId,String delFlag){
		this.wholesalerId = wholesalerId;
		this.delFlag = delFlag;
	}
	public TravelerType(Integer wholesalerId,String delFlag,String status){
		this.wholesalerId = wholesalerId;
		this.delFlag = delFlag;
		this.status = status;
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
	
		
	public void setSort(java.lang.Integer value) {
		this.sort = value;
	}
	@Column(name="sort")
	public java.lang.Integer getSort() {
		return this.sort;
	}
	
		
	public void setName(java.lang.String value) {
		this.name = value;
	}
	@Column(name="name")
	public java.lang.String getName() {
		return this.name;
	}
	
		
	public void setRangeFrom(java.lang.Integer value) {
		this.rangeFrom = value;
	}
	@Column(name="range_from")
	public java.lang.Integer getRangeFrom() {
		return this.rangeFrom;
	}
	
		
	public void setRangeTo(java.lang.Integer value) {
		this.rangeTo = value;
	}
	@Column(name="range_to")
	public java.lang.Integer getRangeTo() {
		return this.rangeTo;
	}
	
		
	public void setSysTravelerType(java.lang.String value) {
		this.sysTravelerType = value;
	}
	@Column(name="sys_traveler_type")
	public java.lang.String getSysTravelerType() {
		return this.sysTravelerType;
	}
	
		
	public void setStatus(java.lang.String value) {
		this.status = value;
	}
	@Column(name="status")
	public java.lang.String getStatus() {
		return this.status;
	}
	
		
	public void setDescription(java.lang.String value) {
		this.description = value;
	}
	@Column(name="description")
	public java.lang.String getDescription() {
		return this.description;
	}
	
		
	public void setWholesalerId(java.lang.Integer value) {
		this.wholesalerId = value;
	}
	@Column(name="wholesaler_id")
	public java.lang.Integer getWholesalerId() {
		return this.wholesalerId;
	}
	
	public void setShortName(java.lang.String shortName) {
		this.shortName = shortName;
	}
	@Column(name="short_name")
	public java.lang.String getShortName() {
		return this.shortName;
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
	@Column(name="apply_product")
	public java.lang.String getApplyProduct() {
		return applyProduct;
	}

	public void setApplyProduct(java.lang.String applyProduct) {
		this.applyProduct = applyProduct;
	}
	@Column(name="person_type")	
	public java.lang.Integer getPersonType() {
		return personType;
	}
	public void setPersonType(java.lang.Integer personType) {
		this.personType = personType;
	}
	
	
	
}

