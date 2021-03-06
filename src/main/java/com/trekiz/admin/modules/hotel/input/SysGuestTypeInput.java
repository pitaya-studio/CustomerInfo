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
import com.trekiz.admin.modules.hotel.entity.SysGuestType;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class SysGuestTypeInput  extends BaseInput {
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
	//"住客类型名称"
	private java.lang.String name;
	//"属性值（对应的是游客人数）"
	private java.lang.Integer value;
	//"类型（0：共；1：增。即0共几人，1第几人）"
	private java.lang.Integer type;
	//"描述"
	private java.lang.String description;
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
	//"批发商id"
	private java.lang.Integer wholesalerId;
	//columns END
	private SysGuestType dataObj ;
	//人员类型
	private java.lang.Integer personType;
	
	public SysGuestTypeInput(){
	}
	//数据库映射bean转换成表单提交bean
	public SysGuestTypeInput(SysGuestType obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public SysGuestType getSysGuestType() {
		dataObj = new SysGuestType();
		BeanUtil.copySimpleProperties(dataObj, this,true);
		return dataObj;
	}
	
	
	
	public java.lang.Integer getPersonType() {
		return personType;
	}
	public void setPersonType(java.lang.Integer personType) {
		this.personType = personType;
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
	public void setName(java.lang.String value) {
		this.name = value;
	}
	public java.lang.String getName() {
		return this.name;
	}
	public void setValue(java.lang.Integer value) {
		this.value = value;
	}
	public java.lang.Integer getValue() {
		return this.value;
	}
	public void setType(java.lang.Integer value) {
		this.type = value;
	}
	public java.lang.Integer getType() {
		return this.type;
	}
	public void setDescription(java.lang.String value) {
		this.description = value;
	}
	public java.lang.String getDescription() {
		return this.description;
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
	public void setWholesalerId(java.lang.Integer value) {
		this.wholesalerId = value;
	}
	public java.lang.Integer getWholesalerId() {
		return this.wholesalerId;
	}


	
}

