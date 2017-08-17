/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.entity;

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

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.utils.DictUtils;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "activity_island_group_airline")
@DynamicInsert(true)
@DynamicUpdate(true)
public class ActivityIslandGroupAirline   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	//date formats
	private static final String FORMAT_DEPARTURE_TIME = "yyyy-MM-dd";
	private static final String FORMAT_ARRIVE_TIME = "yyyy-MM-dd";
	private static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	private static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"唯一uuid"
	private java.lang.String uuid;
	//"海岛游产品uuid"
	private java.lang.String activityIslandUuid;
	//"海岛游产品团期uuid"
	private java.lang.String activityIslandGroupUuid;
	//"关联航班（航空公司信息表sys_airline_info的id）"
	private java.lang.String airline;
	//"航班号（sys_airline_info中的flight_number 根据航空公司选择带出，级联下面的舱位）"
	private java.lang.String flightNumber;
	//"出发时间"
	private java.util.Date departureTime;
	//"到达时间"
	private java.util.Date arriveTime;
	//天数
	private java.lang.Integer dayNum;
	//舱位等级
	private java.lang.String spaceLevel;
	//控票数量
	private java.lang.Integer controlNum;
	//非控票数量
	private java.lang.Integer uncontrolNum;
	//余位数
	private java.lang.Integer remNumber;
	//"创建人"
	private java.lang.Long createBy;
	//"创建时间"
	private java.util.Date createDate;
	//"更新人"
	private java.lang.Long updateBy;
	//"更新日期"
	private java.util.Date updateDate;
	//"删除标志（0:正常，1:删除）"
	private java.lang.String delFlag;
	//"机票价格"
	private java.util.List<ActivityIslandGroupPrice> activityIslandGroupPriceList;
	//舱位等级的中文名
	private java.lang.String spaceLevelName;
	
	@Transient	
	public java.lang.String getSpaceLevelName() {
		return spaceLevelName;
	}

	public void setSpaceLevelName(java.lang.String spaceLevelName) {
		this.spaceLevelName = spaceLevelName;
	}

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
	public ActivityIslandGroupAirline(){
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
	
		
	public void setActivityIslandUuid(java.lang.String value) {
		this.activityIslandUuid = value;
	}
	@Column(name="activity_island_uuid")
	public java.lang.String getActivityIslandUuid() {
		return this.activityIslandUuid;
	}
	
		
	public void setActivityIslandGroupUuid(java.lang.String value) {
		this.activityIslandGroupUuid = value;
	}
	@Column(name="activity_island_group_uuid")
	public java.lang.String getActivityIslandGroupUuid() {
		return this.activityIslandGroupUuid;
	}

	@Transient	
	public String getDepartureTimeString() {
		if(getDepartureTime() != null) {
			return this.date2String(getDepartureTime(), FORMAT_DEPARTURE_TIME);
		} else {
			return null;
		}
	}
	public void setDepartureTimeString(String value) {
		setDepartureTime(this.string2Date(value, FORMAT_DEPARTURE_TIME));
	}
	
		
	public void setDepartureTime(java.util.Date value) {
		this.departureTime = value;
	}
	@Column(name="departureTime")
	public java.util.Date getDepartureTime() {
		return this.departureTime;
	}
	@Transient	
	public String getArriveTimeString() {
		if(getArriveTime() != null) {
			return this.date2String(getArriveTime(), FORMAT_ARRIVE_TIME);
		} else {
			return null;
		}
	}
	public void setArriveTimeString(String value) {
		setArriveTime(this.string2Date(value, FORMAT_ARRIVE_TIME));
	}
	
		
	public void setArriveTime(java.util.Date value) {
		this.arriveTime = value;
	}
	@Column(name="arriveTime")
	public java.util.Date getArriveTime() {
		return this.arriveTime;
	}
	
		
	public void setCreateBy(java.lang.Long value) {
		this.createBy = value;
	}
	@Column(name="createBy")
	public java.lang.Long getCreateBy() {
		return this.createBy;
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
	
		
	public void setUpdateBy(java.lang.Long value) {
		this.updateBy = value;
	}
	@Column(name="updateBy")
	public java.lang.Long getUpdateBy() {
		return this.updateBy;
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
	
		
	public void setDelFlag(java.lang.String value) {
		this.delFlag = value;
	}
	@Column(name="delFlag")
	public java.lang.String getDelFlag() {
		return this.delFlag;
	}
	@Column(name="airline")
	public java.lang.String getAirline() {
		return airline;
	}

	public void setAirline(java.lang.String airline) {
		this.airline = airline;
	}
	@Column(name="flight_number")
	public java.lang.String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(java.lang.String flightNumber) {
		this.flightNumber = flightNumber;
	}
	@Column(name="dayNum")
	public java.lang.Integer getDayNum() {
		return dayNum;
	}

	public void setDayNum(java.lang.Integer dayNum) {
		this.dayNum = dayNum;
	}
	@Column(name="space_level")
	public java.lang.String getSpaceLevel() {
		return spaceLevel;
	}

	public void setSpaceLevel(java.lang.String spaceLevel) {
		this.spaceLevel = spaceLevel;
	}
	
	/**
	 * 获取舱位等级label显示值
	*<p>Title: getSpaceLevelStr</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-6-15 下午12:21:13
	* @throws
	 */
	@Transient
	public String getSpaceLevelStr() {
		if(StringUtils.isNotBlank(this.spaceLevel)){
			Dict dict = DictUtils.getDict(this.spaceLevel, "spaceGrade_Type");
			if(dict != null) {
				return dict.getLabel();
			}
			return null;
		}
		return null;
	}
	@Column(name="control_num")
	public java.lang.Integer getControlNum() {
		return controlNum;
	}

	public void setControlNum(java.lang.Integer controlNum) {
		this.controlNum = controlNum;
	}
	@Column(name="uncontrol_num")
	public java.lang.Integer getUncontrolNum() {
		return uncontrolNum;
	}

	public void setUncontrolNum(java.lang.Integer uncontrolNum) {
		this.uncontrolNum = uncontrolNum;
	}
	@Column(name="remNumber")
	public java.lang.Integer getRemNumber() {
		return remNumber;
	}

	public void setRemNumber(java.lang.Integer remNumber) {
		this.remNumber = remNumber;
	}
	@Transient
	public java.util.List<ActivityIslandGroupPrice> getActivityIslandGroupPriceList() {
		return activityIslandGroupPriceList;
	}

	public void setActivityIslandGroupPriceList(
			java.util.List<ActivityIslandGroupPrice> activityIslandGroupPriceList) {
		this.activityIslandGroupPriceList = activityIslandGroupPriceList;
	}
}

