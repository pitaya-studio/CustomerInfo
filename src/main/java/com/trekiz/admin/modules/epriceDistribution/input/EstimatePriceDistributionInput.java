/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.epriceDistribution.input;

import com.trekiz.admin.common.input.BaseInput;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.epriceDistribution.entity.EstimatePriceDistribution;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class EstimatePriceDistributionInput  extends BaseInput {

	private static final long serialVersionUID = 1L;
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
	private java.lang.Integer opId;
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
	private EstimatePriceDistribution dataObj ;
	
	public EstimatePriceDistributionInput(){
	}
	//数据库映射bean转换成表单提交bean
	public EstimatePriceDistributionInput(EstimatePriceDistribution obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public EstimatePriceDistribution getEstimatePriceDistribution() {
		dataObj = new EstimatePriceDistribution();
		BeanUtil.copySimpleProperties(dataObj, this,true);
		return dataObj;
	}
	
	public void setId(java.lang.Integer value) {
		this.id = value;
	}
	public java.lang.Integer getId() {
		return this.id;
	}
	public void setEstimateBaseId(java.lang.Integer value) {
		this.estimateBaseId = value;
	}
	public java.lang.Integer getEstimateBaseId() {
		return this.estimateBaseId;
	}
	public void setEstimateRecordId(java.lang.Integer value) {
		this.estimateRecordId = value;
	}
	public java.lang.Integer getEstimateRecordId() {
		return this.estimateRecordId;
	}
	public void setOpManagerId(java.lang.Integer value) {
		this.opManagerId = value;
	}
	public java.lang.Integer getOpManagerId() {
		return this.opManagerId;
	}
	public void setOpId(java.lang.Integer value) {
		this.opId = value;
	}
	public java.lang.Integer getOpId() {
		return this.opId;
	}
	public void setType(java.lang.Integer value) {
		this.type = value;
	}
	public java.lang.Integer getType() {
		return this.type;
	}
	public void setCreateDate(java.util.Date value) {
		this.createDate = value;
	}
	public java.util.Date getCreateDate() {
		return this.createDate;
	}
	public void setUpdateDate(java.util.Date value) {
		this.updateDate = value;
	}
	public java.util.Date getUpdateDate() {
		return this.updateDate;
	}
	public void setCreateBy(java.lang.Integer value) {
		this.createBy = value;
	}
	public java.lang.Integer getCreateBy() {
		return this.createBy;
	}
	public void setUpdateBy(java.lang.Integer value) {
		this.updateBy = value;
	}
	public java.lang.Integer getUpdateBy() {
		return this.updateBy;
	}
	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}
	public java.lang.String getDelFlag() {
		return this.delFlag;
	}


	
}

