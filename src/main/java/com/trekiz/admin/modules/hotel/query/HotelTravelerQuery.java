/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.query;


/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public class HotelTravelerQuery  {
	
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
	private java.lang.Long srcPrice;
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
	public void setOrderUuid(java.lang.String value) {
		this.orderUuid = value;
	}
	public java.lang.String getOrderUuid() {
		return this.orderUuid;
	}
	public void setName(java.lang.String value) {
		this.name = value;
	}
	public java.lang.String getName() {
		return this.name;
	}
	public void setNameSpell(java.lang.String value) {
		this.nameSpell = value;
	}
	public java.lang.String getNameSpell() {
		return this.nameSpell;
	}
	public void setPersonType(java.lang.String value) {
		this.personType = value;
	}
	public java.lang.String getPersonType() {
		return this.personType;
	}
	public void setSex(Integer value) {
		this.sex = value;
	}
	public Integer getSex() {
		return this.sex;
	}
	public void setNationality(java.lang.Integer value) {
		this.nationality = value;
	}
	public java.lang.Integer getNationality() {
		return this.nationality;
	}
	public void setBirthDay(java.util.Date value) {
		this.birthDay = value;
	}
	public java.util.Date getBirthDay() {
		return this.birthDay;
	}
	public void setTelephone(java.lang.String value) {
		this.telephone = value;
	}
	public java.lang.String getTelephone() {
		return this.telephone;
	}
	public void setRemark(java.lang.String value) {
		this.remark = value;
	}
	public java.lang.String getRemark() {
		return this.remark;
	}
	public void setSrcPrice(java.lang.Long value) {
		this.srcPrice = value;
	}
	public java.lang.Long getSrcPrice() {
		return this.srcPrice;
	}
	public void setSrcPriceCurrency(java.lang.Integer value) {
		this.srcPriceCurrency = value;
	}
	public java.lang.Integer getSrcPriceCurrency() {
		return this.srcPriceCurrency;
	}
	public void setOriginalPayPriceSerialNum(java.lang.String value) {
		this.originalPayPriceSerialNum = value;
	}
	public java.lang.String getOriginalPayPriceSerialNum() {
		return this.originalPayPriceSerialNum;
	}
	public void setCostPriceSerialNum(java.lang.String value) {
		this.costPriceSerialNum = value;
	}
	public java.lang.String getCostPriceSerialNum() {
		return this.costPriceSerialNum;
	}
	public void setPayPriceSerialNum(java.lang.String value) {
		this.payPriceSerialNum = value;
	}
	public java.lang.String getPayPriceSerialNum() {
		return this.payPriceSerialNum;
	}
	public void setRebatesMoneySerialNum(java.lang.String value) {
		this.rebatesMoneySerialNum = value;
	}
	public java.lang.String getRebatesMoneySerialNum() {
		return this.rebatesMoneySerialNum;
	}
	public void setJkSerialNum(java.lang.String value) {
		this.jkSerialNum = value;
	}
	public java.lang.String getJkSerialNum() {
		return this.jkSerialNum;
	}
	public void setStatus(java.lang.String value) {
		this.status = value;
	}
	public java.lang.String getStatus() {
		return this.status;
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

