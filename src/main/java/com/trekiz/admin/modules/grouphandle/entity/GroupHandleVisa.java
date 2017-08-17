package com.trekiz.admin.modules.grouphandle.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntity;

/**  
 * @Title: GroupControllVisa.java
 * @Package com.trekiz.admin.modules.groupcontroll.entity
 * @Description: 团控签证
 * @author xinwei.wang  
 * @date 2016-2016年1月25日 下午5:30:09
 * @version V1.0  
 */
@Entity
@Table(name = "group_control_visa")
public class GroupHandleVisa  extends DataEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	
	private Integer orderId; //订单id
	private String orderNum; //订单Num
	
	private Integer groupHandleId; //团控ID
	
	private Integer travelerId; //游客ID
	private String travelerName; //游客姓名	
	private String passportNum; //护照号
	
	/** 
	 *  签证状态 
	 *  0未送签，1送签，2约签，3出签，4申请撤签，5撤签成功，6撤签失败，7拒签
	 * */
	private Integer visaStauts;
	private String visaHandleUnit; //办签单位
	/** 签证类型 
	 * 个签	      1
	 * 照会	      4
	 * 邀请	      5
	 * 照会+邀请     6
	 * 探亲	      7
	 * 续签	      8
	 * 个签+探亲+邀请   9
	 * 旅游	      10
	 * 商务	      11
	 * */
	private Integer visaTypeId; //签证类型（sys_dict 表的  value 值）
	private String visaTypeName; //签证名称
	
	private Integer visaCountryId; //签证国家
	private String visaCountryName; //签证国家中文名
	
	private Integer visaConsularDistricId; //签证领区（sys_dict 表的  value 值）
	private String visaConsularDistricName; //签证领区名称
	
	private Date aboutSigningTime; //预计约签时间
	private Date signingTime; //实际约签时间
	private Date visaDeliveryTime; //送签时间
	private Date visaGotTime; //出签时间
	private Date supplementaryInfoTime; //续补资料时间
	private String activityGroupCode; //团号
	
	/** 主键ID */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name="order_id")
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	
	@Column(name="order_num")
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	
	@Column(name="grouphandle_id")
	public Integer getGroupHandleId() {
		return groupHandleId;
	}
	public void setGroupHandleId(Integer groupHandleId) {
		this.groupHandleId = groupHandleId;
	}
	
	
	@Column(name="traveler_id")
	public Integer getTravelerId() {
		return travelerId;
	}
	public void setTravelerId(Integer travelerId) {
		this.travelerId = travelerId;
	}
	
	@Column(name="traveler_name")
	public String getTravelerName() {
		return travelerName;
	}
	public void setTravelerName(String travelerName) {
		this.travelerName = travelerName;
	}
	
	@Column(name="passport_num")
	public String getPassportNum() {
		return passportNum;
	}
	public void setPassportNum(String passportNum) {
		this.passportNum = passportNum;
	}
	
	@Column(name="visa_stauts")
	public Integer getVisaStauts() {
		return visaStauts;
	}
	public void setVisaStauts(Integer visaStauts) {
		this.visaStauts = visaStauts;
	}
	
	@Column(name="visa_handle_unit")
	public String getVisaHandleUnit() {
		return visaHandleUnit;
	}
	public void setVisaHandleUnit(String visaHandleUnit) {
		this.visaHandleUnit = visaHandleUnit;
	}
	
	@Column(name="visa_type_id")
	public Integer getVisaTypeId() {
		return visaTypeId;
	}
	public void setVisaTypeId(Integer visaTypeId) {
		this.visaTypeId = visaTypeId;
	}
	
	@Column(name="visa_type_name")
	public String getVisaTypeName() {
		return visaTypeName;
	}
	public void setVisaTypeName(String visaTypeName) {
		this.visaTypeName = visaTypeName;
	}
	
	@Column(name="visa_country_id")
	public Integer getVisaCountryId() {
		return visaCountryId;
	}
	public void setVisaCountryId(Integer visaCountryId) {
		this.visaCountryId = visaCountryId;
	}
	
	@Column(name="visa_country_name")
	public String getVisaCountryName() {
		return visaCountryName;
	}
	public void setVisaCountryName(String visaCountryName) {
		this.visaCountryName = visaCountryName;
	}
	
	@Column(name="visa_consulardistric_id")
	public Integer getVisaConsularDistricId() {
		return visaConsularDistricId;
	}
	public void setVisaConsularDistricId(Integer visaConsularDistricId) {
		this.visaConsularDistricId = visaConsularDistricId;
	}
	
	@Column(name="visa_consulardistric_name")
	public String getVisaConsularDistricName() {
		return visaConsularDistricName;
	}
	public void setVisaConsularDistricName(String visaConsularDistricName) {
		this.visaConsularDistricName = visaConsularDistricName;
	}
	
	@Column(name="about_signing_time")
	public Date getAboutSigningTime() {
		return aboutSigningTime;
	}
	public void setAboutSigningTime(Date aboutSigningTime) {
		this.aboutSigningTime = aboutSigningTime;
	}
	
	@Column(name="signing_time")
	public Date getSigningTime() {
		return signingTime;
	}
	public void setSigningTime(Date signingTime) {
		this.signingTime = signingTime;
	}
	
	@Column(name="visa_delivery_time")
	public Date getVisaDeliveryTime() {
		return visaDeliveryTime;
	}
	public void setVisaDeliveryTime(Date visaDeliveryTime) {
		this.visaDeliveryTime = visaDeliveryTime;
	}
	
	@Column(name="visa_got_time")
	public Date getVisaGotTime() {
		return visaGotTime;
	}
	public void setVisaGotTime(Date visaGotTime) {
		this.visaGotTime = visaGotTime;
	}
	
	@Column(name="supplementaryinfo_time")
	public Date getSupplementaryInfoTime() {
		return supplementaryInfoTime;
	}
	public void setSupplementaryInfoTime(Date supplementaryInfoTime) {
		this.supplementaryInfoTime = supplementaryInfoTime;
	}
	
	@Column(name="activity_group_code")
	public String getActivityGroupCode() {
		return activityGroupCode;
	}
	public void setActivityGroupCode(String activityGroupCode) {
		this.activityGroupCode = activityGroupCode;
	}
	
	
}
