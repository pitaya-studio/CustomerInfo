/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.input;

import java.util.HashMap;
import java.util.Map;

import com.trekiz.admin.common.input.BaseInput;
import com.trekiz.admin.common.input.BaseInputBean;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.mtourOrder.entity.TravelerPapersType;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class TravelerPapersTypeInput  extends BaseInput {
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
	//"UUID"
	private java.lang.String uuid;
	//"游客信息表ID"
	private java.lang.Integer travelerId;
	//"订单ID"
	private java.lang.Integer orderId;
	//"证件类型1、身份证2、护照3、警官证4、军官证5、其他"
	private java.lang.Integer papersType;
	//"有效期"
	private java.util.Date validityDate;
	//"证件号"
	private java.lang.String idCard;
	//"发证时间"
	private java.util.Date issueDate;
	//"发证地点"
	private java.lang.String issuePlace;
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
	private TravelerPapersType dataObj ;
	
	public TravelerPapersTypeInput(){
	}
	//数据库映射bean转换成表单提交bean
	public TravelerPapersTypeInput(TravelerPapersType obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public TravelerPapersType getTravelerPapersType() {
		dataObj = new TravelerPapersType();
		BeanUtil.copySimpleProperties(dataObj, this,true);
		return dataObj;
	}
	
	public boolean validateInput(){
		if(this.getUuid().length() > 50) {
			return false;
		}
		if(this.getIdCard().length() > 200) {
			return false;
		}
		if(this.getIssuePlace().length() > 200) {
			return false;
		}
		if(this.getDelFlag().length() > 1) {
			return false;
		}
		return true;
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
	public void setTravelerId(java.lang.Integer value) {
		this.travelerId = value;
	}
	public java.lang.Integer getTravelerId() {
		return this.travelerId;
	}
	public void setOrderId(java.lang.Integer value) {
		this.orderId = value;
	}
	public java.lang.Integer getOrderId() {
		return this.orderId;
	}
	public void setPapersType(java.lang.Integer value) {
		this.papersType = value;
	}
	public java.lang.Integer getPapersType() {
		return this.papersType;
	}
	public void setValidityDate(java.util.Date value) {
		this.validityDate = value;
	}
	public java.util.Date getValidityDate() {
		return this.validityDate;
	}
	public void setIdCard(java.lang.String value) {
		this.idCard = value;
	}
	public java.lang.String getIdCard() {
		return this.idCard;
	}
	public void setIssueDate(java.util.Date value) {
		this.issueDate = value;
	}
	public java.util.Date getIssueDate() {
		return this.issueDate;
	}
	public void setIssuePlace(java.lang.String value) {
		this.issuePlace = value;
	}
	public java.lang.String getIssuePlace() {
		return this.issuePlace;
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

