/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.epriceDistribution.entity;

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
@Table(name = "estimate_price_distribution")
@DynamicInsert(true)
@DynamicUpdate(true)
public class EstimatePriceDistribution   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//date formats
	private static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	private static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"id"
	private java.lang.Integer id;
	//"询价基本信息"
	private java.lang.Integer estimateBaseId;
	//"询价记录"
	private java.lang.Integer estimateRecordId;
	//"计调主管"
	private java.lang.Integer opManagerId;
	//"计调"
	private java.lang.String opId;
	//"计调分类 0：地接计调；1：机票计调"
	private java.lang.Integer type;
	//"createDate"
	private java.util.Date createDate;
	//"updateDate"
	private java.util.Date updateDate;
	//"createBy"
	private java.lang.Integer createBy;
	//"updateBy"
	private java.lang.Integer updateBy;
	//"delFlag"
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
	public EstimatePriceDistribution(){
	}

	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public java.lang.Integer getId() {
		return this.id;
	}
	
		
	public void setEstimateBaseId(java.lang.Integer value) {
		this.estimateBaseId = value;
	}
	@Column(name="estimate_base_id")
	public java.lang.Integer getEstimateBaseId() {
		return this.estimateBaseId;
	}
	
		
	public void setEstimateRecordId(java.lang.Integer value) {
		this.estimateRecordId = value;
	}
	@Column(name="estimate_record_id")
	public java.lang.Integer getEstimateRecordId() {
		return this.estimateRecordId;
	}
	
		
	public void setOpManagerId(java.lang.Integer value) {
		this.opManagerId = value;
	}
	@Column(name="op_manager_id")
	public java.lang.Integer getOpManagerId() {
		return this.opManagerId;
	}
	
		
	public void setOpId(java.lang.String value) {
		this.opId = value;
	}
	@Column(name="op_id")
	public java.lang.String getOpId() {
		return this.opId;
	}
	
		
	public void setType(java.lang.Integer value) {
		this.type = value;
	}
	@Column(name="type")
	public java.lang.Integer getType() {
		return this.type;
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
	
		
	public void setCreateBy(java.lang.Integer value) {
		this.createBy = value;
	}
	@Column(name="createBy")
	public java.lang.Integer getCreateBy() {
		return this.createBy;
	}
	
		
	public void setUpdateBy(java.lang.Integer value) {
		this.updateBy = value;
	}
	@Column(name="updateBy")
	public java.lang.Integer getUpdateBy() {
		return this.updateBy;
	}
	
		
	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}
	@Column(name="delFlag")
	public java.lang.String getDelFlag() {
		return this.delFlag;
	}


	
}

