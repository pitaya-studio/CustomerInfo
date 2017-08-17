/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.input;

import java.util.HashMap;
import java.util.Map;

import com.trekiz.admin.common.input.BaseInput;
import com.trekiz.admin.common.input.BaseInputBean;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroupControlDetail;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class ActivityHotelGroupControlDetailInput  extends BaseInput {
	private static final long serialVersionUID = 1L;
	//自定义拆分规则定义
	public static Map<String,BaseInputBean> baseInputBeanMap=new HashMap<String,BaseInputBean>();
	public void put(String key,BaseInputBean value){
		baseInputBeanMap.put(key, value);
	}
	public boolean containsKey(String key){
		return baseInputBeanMap.containsKey(key);
	}
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"uuid"
	private java.lang.String uuid;
	//"酒店产品uuid"
	private java.lang.String activityHotelUuid;
	//"酒店产品团期uuid"
	private java.lang.String activityHotelGroupUuid;
	//"酒店控房明细uuid"
	private java.lang.String hotelControlDetailUuid;
	//"使用库存数"
	private java.lang.Integer num;
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
	private ActivityHotelGroupControlDetail dataObj ;
	
	public ActivityHotelGroupControlDetailInput(){
	}
	//数据库映射bean转换成表单提交bean
	public ActivityHotelGroupControlDetailInput(ActivityHotelGroupControlDetail obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public ActivityHotelGroupControlDetail getActivityHotelGroupControlDetail() {
		dataObj = new ActivityHotelGroupControlDetail();
		BeanUtil.copySimpleProperties(dataObj, this,true);
		return dataObj;
	}
	
	
	
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	public java.lang.Integer getId() {
		return this.id;
	}
	public void setUuid(java.lang.String value) {
		this.uuid = value;
	}
	public java.lang.String getUuid() {
		return this.uuid;
	}
	public void setActivityHotelUuid(java.lang.String value) {
		this.activityHotelUuid = value;
	}
	public java.lang.String getActivityHotelUuid() {
		return this.activityHotelUuid;
	}
	public void setActivityHotelGroupUuid(java.lang.String value) {
		this.activityHotelGroupUuid = value;
	}
	public java.lang.String getActivityHotelGroupUuid() {
		return this.activityHotelGroupUuid;
	}
	public void setHotelControlDetailUuid(java.lang.String value) {
		this.hotelControlDetailUuid = value;
	}
	public java.lang.String getHotelControlDetailUuid() {
		return this.hotelControlDetailUuid;
	}
	public void setNum(java.lang.Integer value) {
		this.num = value;
	}
	public java.lang.Integer getNum() {
		return this.num;
	}
	public void setCreateBy(java.lang.Integer value) {
		this.createBy = value;
	}
	public java.lang.Integer getCreateBy() {
		return this.createBy;
	}
	public void setCreateDate(java.util.Date value) {
		this.createDate = value;
	}
	public java.util.Date getCreateDate() {
		return this.createDate;
	}
	public void setUpdateBy(java.lang.Integer value) {
		this.updateBy = value;
	}
	public java.lang.Integer getUpdateBy() {
		return this.updateBy;
	}
	public void setUpdateDate(java.util.Date value) {
		this.updateDate = value;
	}
	public java.util.Date getUpdateDate() {
		return this.updateDate;
	}
	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}
	public java.lang.String getDelFlag() {
		return this.delFlag;
	}


	
}

