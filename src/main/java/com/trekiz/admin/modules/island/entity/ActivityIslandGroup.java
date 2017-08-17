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
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.persistence.BaseEntity;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Entity
@Table(name = "activity_island_group")
@DynamicInsert(true)
@DynamicUpdate(true)
public class ActivityIslandGroup   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "ActivityIslandGroup";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "唯一uuid";
	public static final String ALIAS_ACTIVITY_ISLAND_UUID = "海岛游产品uuid";
	public static final String ALIAS_GROUP_CODE = "团期编号";
	public static final String ALIAS_GROUP_OPEN_DATE = "出团日期";
	public static final String ALIAS_GROUP_END_DATE = "结束时期";
	public static final String ALIAS_ISLAND_WAY = "上岛方式（字典，多个用“；”分隔）";
	public static final String ALIAS_ADV_NUMBER = "预收人数";
	public static final String ALIAS_SINGLE_PRICE = "单房差";
	public static final String ALIAS_CURRENCY_ID = "单房差币种";
	public static final String ALIAS_SINGLE_PRICE_UNIT = "单房差单位";
	public static final String ALIAS_PRIORITY_DEDUCTION = "优先扣减：1 控票数 2 非控票数";
	public static final String ALIAS_FRONT_MONEY = "订金";
	public static final String ALIAS_FRONT_MONEY_CURRENCY_ID = "订金币种";
	public static final String ALIAS_MEMO = "团期备注";
	public static final String ALIAS_STATUS = "1：上架；2：下架；3：草稿；4：已删除";
	public static final String ALIAS_LOCK_STATUS="成本录入锁  0 未锁 1锁定 默认为0";
	public static final String ALIAS_FORCAST_STATUS="00 未锁 10 锁定 默认为00";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建日期 ";
	public static final String ALIAS_UPDATE_BY = "更新人";
	public static final String ALIAS_UPDATE_DATE = "更新日期";
	public static final String ALIAS_DEL_FLAG = "删除标志（0:正常，1:删除）";
	
	// 提交状态（1：上架；2：下架；3：草稿；4：已删除）
	/** 上架 */
	public static final int STATUS_PUTAWAY_FLAG = 1;
	/** 下架 */
	public static final int STATUS_OFFSHELF_FLAG = 2;
	/** 草稿 */
	public static final int STATUS_DRAFT_FLAG = 3;
	/** 已删除 */
	public static final int STATUS_DEL_FLAG = 4;
		
	//date formats
	public static final String FORMAT_GROUP_OPEN_DATE = "yyyy-MM-dd";
	public static final String FORMAT_DEPARTURE_TIME = "yyyy-MM-dd";
	public static final String FORMAT_ARRIVE_TIME = "yyyy-MM-dd";
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"唯一uuid"
	private java.lang.String uuid;
	//"海岛游产品uuid"
	private java.lang.String activityIslandUuid;
	//"团期编号"
	private java.lang.String groupCode;
	//"出团日期"
	private java.util.Date groupOpenDate;
	//"结束时期"
	private java.util.Date groupEndDate;
	//"上岛方式（字典，多个用“；”分隔）"
	private java.lang.String islandWay;
	//"预收人数"
	private java.lang.Integer advNumber;
	//"单房差"
	private Double singlePrice;
	//"单房差币种"
	private java.lang.Integer currencyId;
	//单房差单位
	private java.lang.Integer singlePriceUnit;
	//优先扣减 系统常量 1控票数 2非控票数
	private java.lang.Integer priorityDeduction;
	//备注
	private java.lang.String memo;

	//"1：上架；2：下架；3：草稿；4：已删除"
	private java.lang.String status;
	//"成本录入锁"
	private java.lang.Integer lockStatus;
	//""
	private java.lang.String forcastStatus;
	//"创建人"
	private java.lang.Long createBy;
	//"创建日期 "
	private java.util.Date createDate;
	//"更新人"
	private java.lang.Long updateBy;
	//"更新日期"
	private java.util.Date updateDate;
	//"删除标志（0:正常，1:删除）"
	private java.lang.String delFlag;
	//"frontMoney"
	private Double frontMoney;
	//"frontMoneyCurrencyId"
	private java.lang.Integer frontMoneyCurrencyId;
	
	private List<ActivityIslandGroupRoom> activityIslandGroupRoomList; //海岛游产品团期房型表
	private List<ActivityIslandGroupPrice> activityIslandGroupPriceList;  //海岛游产品团期价格表
	private List<ActivityIslandGroupAirline> activityIslandGroupAirlineList;//海岛游产品团期参考航班表
	private Map<String, List<Map<String,Object>>> spaceMap;//仓位等级
	private List<ActivityIslandGroupMeal> activityIslandGroupMealList;
	private List<ActivityIslandGroupMealRise> activityIslandGroupMealRiseList;
	private List<List<ActivityIslandGroupPrice>> jsonActivityIslandGroupPriceList;
	
	private Integer orderNum;
	private Integer baseMealNum;//基础餐个数
	private Integer totalRemNum;//总预收人数
	private Integer bookingNum;//预报名个数
	/**提成状态 0为计算提成，1已计算提成*/
	private int iscommission;
	
	
	@Column(name = "iscommission", unique = false, nullable = false)
	public int getIscommission() {
		return iscommission;
	}

	public void setIscommission(int iscommission) {
		this.iscommission = iscommission;
	}
	@Transient
	public Integer getBookingNum() {
		return bookingNum;
	}
	public void setBookingNum(Integer bookingNum) {
		this.bookingNum = bookingNum;
	}
	@Transient
	public Integer getTotalRemNum() {
		return totalRemNum;
	}
	public void setTotalRemNum(Integer totalRemNum) {
		this.totalRemNum = totalRemNum;
	}
	@Transient
	public Integer getBaseMealNum() {
		return baseMealNum;
	}
	@Transient
	public List<List<ActivityIslandGroupPrice>> getJsonActivityIslandGroupPriceList() {
		return jsonActivityIslandGroupPriceList;
	}
	public void setJsonActivityIslandGroupPriceList(
			List<List<ActivityIslandGroupPrice>> jsonActivityIslandGroupPriceList) {
		this.jsonActivityIslandGroupPriceList = jsonActivityIslandGroupPriceList;
	}
	@Transient
	public List<ActivityIslandGroupMealRise> getActivityIslandGroupMealRiseList() {
		return activityIslandGroupMealRiseList;
	}

	public void setActivityIslandGroupMealRiseList(
			List<ActivityIslandGroupMealRise> activityIslandGroupMealRiseList) {
		this.activityIslandGroupMealRiseList = activityIslandGroupMealRiseList;
	}

	public void setBaseMealNum(Integer baseMealNum) {
		this.baseMealNum = baseMealNum;
	}

	@Transient
	public List<ActivityIslandGroupMeal> getActivityIslandGroupMealList() {
		return activityIslandGroupMealList;
	}

	public void setActivityIslandGroupMealList(
			List<ActivityIslandGroupMeal> activityIslandGroupMealList) {
		this.activityIslandGroupMealList = activityIslandGroupMealList;
	}

	@Transient
	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	/** 团期余位数(根据航班表里的remNumber累加所得) */
	private Integer remNumber;
	
	
	@Transient
	public Map<String, List<Map<String, Object>>> getSpaceMap() {
		return spaceMap;
	}

	public void setSpaceMap(Map<String, List<Map<String, Object>>> spaceMap) {
		this.spaceMap = spaceMap;
	}

	@Transient
	public List<ActivityIslandGroupRoom> getActivityIslandGroupRoomList() {
		return activityIslandGroupRoomList;
	}

	public void setActivityIslandGroupRoomList(
			List<ActivityIslandGroupRoom> activityIslandGroupRoomList) {
		this.activityIslandGroupRoomList = activityIslandGroupRoomList;
	}
	@Transient
	public List<ActivityIslandGroupPrice> getActivityIslandGroupPriceList() {
		return activityIslandGroupPriceList;
	}

	public void setActivityIslandGroupPriceList(
			List<ActivityIslandGroupPrice> activityIslandGroupPriceList) {
		this.activityIslandGroupPriceList = activityIslandGroupPriceList;
	}
	
	
	
	@Transient
	public List<ActivityIslandGroupAirline> getActivityIslandGroupAirlineList() {
		return activityIslandGroupAirlineList;
	}

	public void setActivityIslandGroupAirlineList(
			List<ActivityIslandGroupAirline> activityIslandGroupAirlineList) {
		this.activityIslandGroupAirlineList = activityIslandGroupAirlineList;
	}
	@Transient
	public Integer getRemNumber() {
		return remNumber;
	}

	public void setRemNumber(Integer remNumber) {
		this.remNumber = remNumber;
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
	public ActivityIslandGroup(){
	}

	public ActivityIslandGroup(
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
	
		
	public void setActivityIslandUuid(java.lang.String value) {
		this.activityIslandUuid = value;
	}
	@Column(name="activity_island_uuid")
	public java.lang.String getActivityIslandUuid() {
		return this.activityIslandUuid;
	}
	
		
	public void setGroupCode(java.lang.String value) {
		this.groupCode = value;
	}
	@Column(name="groupCode")
	public java.lang.String getGroupCode() {
		return this.groupCode;
	}
	@Transient	
	public String getGroupOpenDateString() {
		if(getGroupOpenDate() != null) {
			return this.date2String(getGroupOpenDate(), FORMAT_GROUP_OPEN_DATE);
		} else {
			return null;
		}
	}
	public void setGroupOpenDateString(String value) {
		setGroupOpenDate(this.string2Date(value, FORMAT_GROUP_OPEN_DATE));
	}
	
		
	public void setGroupOpenDate(java.util.Date value) {
		this.groupOpenDate = value;
	}
	@Column(name="groupOpenDate")
	public java.util.Date getGroupOpenDate() {
		return this.groupOpenDate;
	}
	
		
	public void setIslandWay(java.lang.String value) {
		this.islandWay = value;
	}
	@Column(name="island_way")
	public java.lang.String getIslandWay() {
		return this.islandWay;
	}
	
		
	public void setAdvNumber(java.lang.Integer value) {
		this.advNumber = value;
	}
	@Column(name="advNumber")
	public java.lang.Integer getAdvNumber() {
		return this.advNumber;
	}
	public void setSinglePrice(Double value) {
		this.singlePrice = value;
	}
	@Column(name="singlePrice")
	public Double getSinglePrice() {
		return this.singlePrice;
	}
	
		
	public void setCurrencyId(java.lang.Integer value) {
		this.currencyId = value;
	}
	@Column(name="currency_id")
	public java.lang.Integer getCurrencyId() {
		return this.currencyId;
	}

	public void setLockStatus(Integer value) {
		this.lockStatus = value;
	}
	@Column(name="lockStatus")
	public Integer getLockStatus() {
		return this.lockStatus;
	}
	
		
	public void setForcastStatus(java.lang.String value) {
		this.forcastStatus = value;
	}
	@Column(name="forcastStatus")
	public java.lang.String getForcastStatus() {
		return this.forcastStatus;
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
	
		
	public void setFrontMoney(Double value) {
		this.frontMoney = value;
	}
	@Column(name="front_money")
	public Double getFrontMoney() {
		return this.frontMoney;
	}
	
		
	public void setFrontMoneyCurrencyId(java.lang.Integer value) {
		this.frontMoneyCurrencyId = value;
	}
	@Column(name="front_money_currency_id")
	public java.lang.Integer getFrontMoneyCurrencyId() {
		return this.frontMoneyCurrencyId;
	}

	public void setStatus(java.lang.String value) {
		this.status = value;
	}
	@Column(name="status")
	public java.lang.String getStatus() {
		return this.status;
	}
	@Column(name="groupEndDate")
	public java.util.Date getGroupEndDate() {
		return groupEndDate;
	}

	public void setGroupEndDate(java.util.Date groupEndDate) {
		this.groupEndDate = groupEndDate;
	}
	@Column(name="singlePriceUnit")
	public java.lang.Integer getSinglePriceUnit() {
		return singlePriceUnit;
	}

	public void setSinglePriceUnit(java.lang.Integer singlePriceUnit) {
		this.singlePriceUnit = singlePriceUnit;
	}
	@Column(name="priority_deduction")
	public java.lang.Integer getPriorityDeduction() {
		return priorityDeduction;
	}

	public void setPriorityDeduction(java.lang.Integer priorityDeduction) {
		this.priorityDeduction = priorityDeduction;
	}
	public java.lang.String getMemo() {
		return memo;
	}
	@Column(name="memo")
	public void setMemo(java.lang.String memo) {
		this.memo = memo;
	}

}

