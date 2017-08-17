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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.persistence.BaseEntity;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "activity_hotel_share")
@DynamicInsert(true)
@DynamicUpdate(true)
public class ActivityHotelShare   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "ActivityHotelShare";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "唯一uuid";
	public static final String ALIAS_ACTIVITY_HOTEL_UUID = "酒店产品uuid";
	public static final String ALIAS_SHARE_USER = "产品分享人";
	public static final String ALIAS_ACCEPT_SHARE_USER = "接受分享人";
	public static final String ALIAS_DEL_FLAG = "删除标志（0:正常，1:删除）";
	
	//date formats
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"唯一uuid"
	private java.lang.String uuid;
	//"酒店产品uuid"
	private java.lang.String activityHotelUuid;
	//"产品分享人"
	private java.lang.Long shareUser;
	//"接受分享人"
	private java.lang.Long acceptShareUser;
	//"删除标志（0:正常，1:删除）"
	private java.lang.String delFlag;
	//columns END
	@SuppressWarnings("unused")
	private String date2String(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	@SuppressWarnings("unused")
	private Date string2Date(String dateStr,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return  sdf.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	public ActivityHotelShare(){
	}

	public ActivityHotelShare(
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
	
		
	public void setActivityHotelUuid(java.lang.String value) {
		this.activityHotelUuid = value;
	}
	@Column(name="activity_hotel_uuid")
	public java.lang.String getActivityHotelUuid() {
		return this.activityHotelUuid;
	}
	
		
	public void setShareUser(java.lang.Long value) {
		this.shareUser = value;
	}
	@Column(name="share_user")
	public java.lang.Long getShareUser() {
		return this.shareUser;
	}
	
		
	public void setAcceptShareUser(java.lang.Long value) {
		this.acceptShareUser = value;
	}
	@Column(name="accept_share_user")
	public java.lang.Long getAcceptShareUser() {
		return this.acceptShareUser;
	}
	
		
	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}
	@Column(name="delFlag")
	public java.lang.String getDelFlag() {
		return this.delFlag;
	}


	
}

