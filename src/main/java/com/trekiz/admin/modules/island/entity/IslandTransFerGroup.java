package com.trekiz.admin.modules.island.entity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.trekiz.admin.common.utils.DateUtils;

/**
 * 海岛游游客审核详情类
 * @author gao
 *  2015年6月15日
 */
public class IslandTransFerGroup {
	
	/**
	 * 游客Id
	 */
	public static final String KEY_TRAVELERID = "travelerId";
	/**
	 * 游客UUId
	 */
	public static final String KEY_TRAVELERUUID = "travelerUuid";
	/**
	 * 游客姓名
	 */
	public static final String KEY_TRAVELERNAME = "travelerName";
	/**
	 * 游客类型（1-成人 2-儿童 3-特殊人群）
	 */
	private static final String KEY_TRAVELERTYPE = "travelerType";
	/**
	 * 游客舱位等级
	 */
	private static final String KEY_TRAVELERLEVEL= "travelerLevel";
	/**
	 * 游客转团申请时间
	 */
	private static final String KEY_APPLYDATE = "applyDate";
	/**
	 * 游客签证国家
	 */
	private static final String KEY_VISACOUNTRY = "visaCountry";
	/**
	 * 游客签证类型(字典表type:new_visa_type)
	 */
	private static final String KEY_VISATYPEID = "visaTypeId";
	/**
	 * 游客签证国家和类型
	 */
	private static final String KEY_VISACOUNTRYTYPE = "visaCountryType";
	/**
	 * 游客证件类型/证件号/有效期
	 */
	private static final String KEY_PAPERSTYPECODEDATE = "paperTypeCodeDate";
	/**
	 * 备注（转团原因）
	 */
	private static final String KEY_REMARK = "remark";
	/**
	 * 转入团期号
	 * 
	 */
	private static final String KEY_NEWGROUPCODE="newGroupCode";
	/**
	 * 审批状态
	 */
	private static final String KEY_STATUS="status";
	/**
	 * 审批UUID
	 */
	private static final String KEY_REVIEWUUID = "reviewUuid";
	/**
	 * 审批ID
	 */
	private static final String KEY_REVIEWID = "reviewId";
	/**
	 * 申请人
	 */
	private static final String KEY_PERSON = "person";
	/**
	 * 申请人ID
	 */
	private static final String KEY_PERSONID = "personId";
	/**
	 * 申请人UUID
	 */
	private static final String KEY_PERSONUUID = "personUuid";
	
	private static Map<String, String> statusMap = new HashMap<String, String>();
	static {
		statusMap.put("0", "已驳回");
		statusMap.put("1", "审核中");
		statusMap.put("2", "审核成功");
		statusMap.put("3", "已退款");
	}
	private static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	
	/**
	 * 游客Id
	 */
	private String travelerId;
	private String travelerUuid;
	/**
	 * 游客名称
	 */
	private String travelerName;
	/**
	 * 游客类型 （1-成人 2-儿童 3-特殊人群）
	 */
	private String  travelerType;
	/**
	 * 游客舱位等级
	 */
	private String travelerLevel;
	/**
	 * 游客转团申请时间
	 */
	private Date applyDate;
	/**
	 * 游客签证国家
	 */
	private String visaCountry;
	private String visaCountryType;
	/**
	 * 游客签证类型 (字典表type:new_visa_type)
	 */
	private String visaTypeId;
	/**
	 * 游客证件类型/证件号/有效期
	 */
	private String paperTypeCodeDate;
	/**
	 * 备注（转团原因）
	 */
	private String remark;
	/**
	 * 转入团期号
	 */
	private String newGroupCode;
	
	public IslandTransFerGroup(){}

	public IslandTransFerGroup(Map<String, String> reviewDetailMap){
		this.travelerId = reviewDetailMap.get("KEY_TRAVELERID")+"";
		this.travelerUuid = reviewDetailMap.get("KEY_TRAVELERUUID")+"";
		this.travelerName = reviewDetailMap.get("KEY_TRAVELERNAME")+"";
		this.travelerType = reviewDetailMap.get("KEY_TRAVELERTYPE")+"";
		this.travelerLevel = reviewDetailMap.get("KEY_TRAVELERLEVEL")+"";
		this.applyDate = reviewDetailMap.get("KEY_APPLYDATE")==null?null:DateUtils.dateFormat(reviewDetailMap.get("KEY_APPLYDATE").toString());
		this.visaCountry = reviewDetailMap.get("KEY_VISACOUNTRY")+"";
		this.visaTypeId = reviewDetailMap.get("KEY_VISATYPEID")+"";
		this.visaCountryType = reviewDetailMap.get("KEY_VISACOUNTRYTYPE")+"";
		this.paperTypeCodeDate = reviewDetailMap.get("KEY_PAPERSTYPECODEDATE");
		this.remark = reviewDetailMap.get("KEY_REMARK")+"";
		this.newGroupCode = reviewDetailMap.get("KEY_NEWGROUPCODE")+"";
	}
	
	public Map<String, String> getReviewDetailMap(){
		Map<String, String> reviewDetailMap = new HashMap<String, String>();
		reviewDetailMap.put("KEY_TRAVELERID", travelerId);
		reviewDetailMap.put("KEY_TRAVELERUUID", travelerUuid);
		reviewDetailMap.put("KEY_TRAVELERNAME", travelerName);
		reviewDetailMap.put("KEY_TRAVELERTYPE", travelerType);
		reviewDetailMap.put("KEY_TRAVELERLEVEL", travelerLevel);
		reviewDetailMap.put("KEY_APPLYDATE", DateUtils.formatCustomDate(applyDate, "yyyy-MM-dd"));
		reviewDetailMap.put("KEY_VISACOUNTRY", visaCountry);
		reviewDetailMap.put("KEY_VISATYPEID", visaTypeId);
		reviewDetailMap.put("KEY_VISACOUNTRYTYPE", visaCountryType);
		reviewDetailMap.put("KEY_PAPERSTYPECODEDATE", paperTypeCodeDate);
		reviewDetailMap.put("KEY_REMARK", remark);
		reviewDetailMap.put("KEY_NEWGROUPCODE", newGroupCode);
		return reviewDetailMap;
	}

	public String getTravelerId() {
		return travelerId;
	}

	public void setTravelerId(String travelerId) {
		this.travelerId = travelerId;
	}

	public String getTravelerUuid() {
		return travelerUuid;
	}

	public void setTravelerUuid(String travelerUuid) {
		this.travelerUuid = travelerUuid;
	}

	public String getTravelerName() {
		return travelerName;
	}

	public void setTravelerName(String travelerName) {
		this.travelerName = travelerName;
	}

	public String getTravelerType() {
		return travelerType;
	}

	public void setTravelerType(String travelerType) {
		this.travelerType = travelerType;
	}

	public String getTravelerLevel() {
		return travelerLevel;
	}

	public void setTravelerLevel(String travelerLevel) {
		this.travelerLevel = travelerLevel;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getVisaCountry() {
		return visaCountry;
	}

	public void setVisaCountry(String visaCountry) {
		this.visaCountry = visaCountry;
	}

	public String getVisaTypeId() {
		return visaTypeId;
	}

	public void setVisaTypeId(String visaTypeId) {
		this.visaTypeId = visaTypeId;
	}

	public String getPaperTypeCodeDate() {
		return paperTypeCodeDate;
	}

	public void setPaperTypeCodeDate(String paperTypeCodeDate) {
		this.paperTypeCodeDate = paperTypeCodeDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getNewGroupCode() {
		return newGroupCode;
	}

	public void setNewGroupCode(String newGroupCode) {
		this.newGroupCode = newGroupCode;
	}

	public String getVisaCountryType() {
		return visaCountryType;
	}

	public void setVisaCountryType(String visaCountryType) {
		this.visaCountryType = visaCountryType;
	}
	
	
}
