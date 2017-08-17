/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.input;

import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.modules.island.entity.ActivityIslandShare;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */


public class ActivityIslandShareInput  {
	
	//columns START
	//"id"
	private java.lang.Integer id;
	//"唯一uuid"
	private java.lang.String uuid;
	//"海岛游产品uuid"
	private java.lang.String activityIslandUuid;
	//"产品分享人"
	private java.lang.Long shareUser;
	//"接受分享人"
	private java.lang.Long acceptShareUser;
	//"删除标志（0:正常，1:删除）"
	private java.lang.String delFlag;
	//columns END
	private ActivityIslandShare dataObj ;
	
	public ActivityIslandShareInput(){
	}
	//数据库映射bean转换成表单提交bean
	public ActivityIslandShareInput(ActivityIslandShare obj ){
		BeanUtil.copySimpleProperties(this, obj);
	}
	//表单提交的bean转换成数据库映射的bean
	public ActivityIslandShare getActivityIslandShare() {
		dataObj = new ActivityIslandShare();
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
	public void setActivityIslandUuid(java.lang.String value) {
		this.activityIslandUuid = value;
	}
	public java.lang.String getActivityIslandUuid() {
		return this.activityIslandUuid;
	}
	public void setShareUser(java.lang.Long value) {
		this.shareUser = value;
	}
	public java.lang.Long getShareUser() {
		return this.shareUser;
	}
	public void setAcceptShareUser(java.lang.Long value) {
		this.acceptShareUser = value;
	}
	public java.lang.Long getAcceptShareUser() {
		return this.acceptShareUser;
	}
	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}
	public java.lang.String getDelFlag() {
		return this.delFlag;
	}


	
}

