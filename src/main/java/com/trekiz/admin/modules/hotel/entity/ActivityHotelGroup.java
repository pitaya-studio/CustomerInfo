/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
@Table(name = "activity_hotel_group")
@DynamicInsert(true)
@DynamicUpdate(true)
public class ActivityHotelGroup   extends BaseEntity {
	private static final long serialVersionUID = 5454155825314635342L;
	
	//alias
	public static final String TABLE_ALIAS = "ActivityHotelGroup";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_UUID = "唯一uuid";
	public static final String ALIAS_ACTIVITY_HOTEL_UUID = "酒店产品uuid";
	public static final String ALIAS_GROUP_CODE = "团期编号";
	public static final String ALIAS_GROUP_OPEN_DATE = "出团日期";
	public static final String ALIAS_GROUP_END_DATE = "结束时期（俄风行隐藏）";
	public static final String ALIAS_ISLAND_WAY = "上岛方式（字典，多个用“；”分隔）";
	public static final String ALIAS_SINGLE_PRICE = "单房差";
	public static final String ALIAS_CURRENCY_ID = "单房差币种";
	public static final String ALIAS_SINGLE_PRICE_UNIT = "单房差单位（系统常量：1人2间3晚）";
	public static final String ALIAS_CONTROL_NUM = "控房间数(选择关联控房单中的库存数量)";
	public static final String ALIAS_UNCONTROL_NUM = "非控房间数";
	public static final String ALIAS_REM_NUMBER = "余位数";
	public static final String ALIAS_AIRLINE = "参考航班（文本输入，不和航班有关联）";
	public static final String ALIAS_PRIORITY_DEDUCTION = "优先扣减（系统常量：1控票数2非控票数）";
	public static final String ALIAS_FRONT_MONEY = "定金";
	public static final String ALIAS_FRONT_MONEY_CURRENCY_ID = "定金币种";
	public static final String ALIAS_MEMO = "团期备注";
	public static final String ALIAS_STATUS = "1：上架；2：下架；3：草稿；4：已删除";
	public static final String ALIAS_CREATE_BY = "创建人";
	public static final String ALIAS_CREATE_DATE = "创建日期";
	public static final String ALIAS_UPDATE_BY = "更新人";
	public static final String ALIAS_UPDATE_DATE = "更新日期";
	public static final String ALIAS_DEL_FLAG = "删除标志（0:正常，1:删除）";
	
	//date formats
	public static final String FORMAT_GROUP_OPEN_DATE = "yyyy-MM-dd";
	public static final String FORMAT_GROUP_END_DATE = "yyyy-MM-dd";
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	//可以直接使用: @Length(max=50,message="用户名长度不能大于50")显示错误消息
	//columns START
	//"主键"
	private java.lang.Integer id;
	//"唯一uuid"
	private java.lang.String uuid;
	//"酒店产品uuid"
	private java.lang.String activityHotelUuid;
	//"团期编号"
	private java.lang.String groupCode;
	//"出团日期"
	private java.util.Date groupOpenDate;
	//"结束时期（俄风行隐藏）"
	private java.util.Date groupEndDate;
	//"上岛方式（字典，多个用“；”分隔）"
	private java.lang.String islandWay;
	//"单房差"
	private Double singlePrice;
	//"单房差币种"
	private java.lang.Integer currencyId;
	//"单房差单位（系统常量：1人2间3晚）"
	private java.lang.Integer singlePriceUnit;
	//"控房间数(选择关联控房单中的库存数量)"
	private java.lang.Integer controlNum;
	//"非控房间数"
	private java.lang.Integer uncontrolNum;
	//"余位数"
	private java.lang.Integer remNumber;
	//"参考航班（文本输入，不和航班有关联）"
	private java.lang.String airline;
	//"优先扣减（系统常量：1控票数2非控票数）"
	private java.lang.Integer priorityDeduction;
	//"定金"
	private Double frontMoney;
	//"定金币种"
	private java.lang.Integer frontMoneyCurrencyId;
	//"团期备注"
	private java.lang.String memo;
	//"1：上架；2：下架；3：草稿；4：已删除"
	private java.lang.String status;
	//"创建人"
	private java.lang.Long createBy;
	//"创建日期"
	private java.util.Date createDate;
	//"更新人"
	private java.lang.Long updateBy;
	//"更新日期"
	private java.util.Date updateDate;
	//"删除标志（0:正常，1:删除）"
	private java.lang.String delFlag;
	//"成本录入锁:0没有锁定, 1锁定（默认为 '0'）"
	private java.lang.Integer lockStatus;
	//"00表示未锁，10表示锁定（默认为’00’）"
	private java.lang.String forcastStatus;
	//预报名数
	private java.lang.Integer preApplyNum;
	//columns END
	

	//酒店产品团期房型明细
	private List<ActivityHotelGroupRoom> activityHotelGroupRoomList;
	//酒店产品团期基础餐型明细
	private List<ActivityHotelGroupMeal> activityHotelGroupMealList;
	//酒店产品团期价格明细
	private List<ActivityHotelGroupPrice> activityHotelGroupPriceList;
	//酒店产品团期起价明细
	private List<ActivityHotelGroupLowprice> activityHotelGroupLowprices;
	//酒店产品团期控房明细表
	private List<ActivityHotelGroupControlDetail> activityHotelGroupControlDetail;
	//酒店产品团期升餐明细
	private List<ActivityHotelGroupMealRise> activityHotelGroupMealRiseList;
	//酒店产品团期预报名人数
	private Integer orderNum;
	private int mealRiseRowspan;
	
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
	public List<ActivityHotelGroupMealRise> getActivityHotelGroupMealRiseList() {
		return activityHotelGroupMealRiseList;
	}

	public void setActivityHotelGroupMealRiseList(
			List<ActivityHotelGroupMealRise> activityHotelGroupMealRiseList) {
		this.activityHotelGroupMealRiseList = activityHotelGroupMealRiseList;
	}

	@Transient
	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	@Transient
	public List<ActivityHotelGroupControlDetail> getActivityHotelGroupControlDetail() {
		return activityHotelGroupControlDetail;
	}

	public void setActivityHotelGroupControlDetail(
			List<ActivityHotelGroupControlDetail> activityHotelGroupControlDetail) {
		this.activityHotelGroupControlDetail = activityHotelGroupControlDetail;
	}

	@Transient
	public List<ActivityHotelGroupLowprice> getActivityHotelGroupLowprices() {
		return activityHotelGroupLowprices;
	}

	public void setActivityHotelGroupLowprices(
			List<ActivityHotelGroupLowprice> activityHotelGroupLowprices) {
		this.activityHotelGroupLowprices = activityHotelGroupLowprices;
	}

	@Transient	
	public List<ActivityHotelGroupRoom> getActivityHotelGroupRoomList() {
		return activityHotelGroupRoomList;
	}

	public void setActivityHotelGroupRoomList(
			List<ActivityHotelGroupRoom> activityHotelGroupRoomList) {
		this.activityHotelGroupRoomList = activityHotelGroupRoomList;
	}
	@Transient	
	public List<ActivityHotelGroupMeal> getActivityHotelGroupMealList() {
		return activityHotelGroupMealList;
	}

	public void setActivityHotelGroupMealList(
			List<ActivityHotelGroupMeal> activityHotelGroupMealList) {
		this.activityHotelGroupMealList = activityHotelGroupMealList;
	}
	@Transient	
	public List<ActivityHotelGroupPrice> getActivityHotelGroupPriceList() {
		return activityHotelGroupPriceList;
	}

	public void setActivityHotelGroupPriceList(
			List<ActivityHotelGroupPrice> activityHotelGroupPriceList) {
		this.activityHotelGroupPriceList = activityHotelGroupPriceList;
	}
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
	public ActivityHotelGroup(){
	}

	public ActivityHotelGroup(
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
	
		
	public void setActivityHotelUuid(java.lang.String value) {
		this.activityHotelUuid = value;
	}
	@Column(name="activity_hotel_uuid")
	public java.lang.String getActivityHotelUuid() {
		return this.activityHotelUuid;
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
	@Transient	
	public String getGroupEndDateString() {
		if(getGroupEndDate() != null) {
			return this.date2String(getGroupEndDate(), FORMAT_GROUP_END_DATE);
		} else {
			return null;
		}
	}
	public void setGroupEndDateString(String value) {
		setGroupEndDate(this.string2Date(value, FORMAT_GROUP_END_DATE));
	}
	
		
	public void setGroupEndDate(java.util.Date value) {
		this.groupEndDate = value;
	}
	@Column(name="groupEndDate")
	public java.util.Date getGroupEndDate() {
		return this.groupEndDate;
	}
	
		
	public void setIslandWay(java.lang.String value) {
		this.islandWay = value;
	}
	@Column(name="island_way")
	public java.lang.String getIslandWay() {
		return this.islandWay;
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
	
		
	public void setSinglePriceUnit(java.lang.Integer value) {
		this.singlePriceUnit = value;
	}
	@Column(name="singlePriceUnit")
	public java.lang.Integer getSinglePriceUnit() {
		return this.singlePriceUnit;
	}
	
		
	public void setControlNum(java.lang.Integer value) {
		this.controlNum = value;
	}
	@Column(name="control_num")
	public java.lang.Integer getControlNum() {
		return this.controlNum;
	}
	
		
	public void setUncontrolNum(java.lang.Integer value) {
		this.uncontrolNum = value;
	}
	@Column(name="uncontrol_num")
	public java.lang.Integer getUncontrolNum() {
		return this.uncontrolNum;
	}
	
		
	public void setRemNumber(java.lang.Integer value) {
		this.remNumber = value;
	}
	@Column(name="remNumber")
	public java.lang.Integer getRemNumber() {
		return this.remNumber;
	}
	
		
	public void setAirline(java.lang.String value) {
		this.airline = value;
	}
	@Column(name="airline")
	public java.lang.String getAirline() {
		return this.airline;
	}
	
		
	public void setPriorityDeduction(java.lang.Integer value) {
		this.priorityDeduction = value;
	}
	@Column(name="priority_deduction")
	public java.lang.Integer getPriorityDeduction() {
		return this.priorityDeduction;
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
	
		
	public void setMemo(java.lang.String value) {
		this.memo = value;
	}
	@Column(name="memo")
	public java.lang.String getMemo() {
		return this.memo;
	}
	
		
	public void setStatus(java.lang.String value) {
		this.status = value;
	}
	@Column(name="status")
	public java.lang.String getStatus() {
		return this.status;
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
	@Column(name="lockStatus")
	public java.lang.Integer getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(java.lang.Integer lockStatus) {
		this.lockStatus = lockStatus;
	}
	@Column(name="forcastStatus")
	public java.lang.String getForcastStatus() {
		return forcastStatus;
	}

	public void setForcastStatus(java.lang.String forcastStatus) {
		this.forcastStatus = forcastStatus;
	}
	@Transient	
	public int getMealRiseRowspan() {
		return mealRiseRowspan;
	}

	public void setMealRiseRowspan(int mealRiseRowspan) {
		this.mealRiseRowspan = mealRiseRowspan;
	}
	@Transient
	public java.lang.Integer getPreApplyNum() {
		return preApplyNum;
	}

	public void setPreApplyNum(java.lang.Integer preApplyNum) {
		this.preApplyNum = preApplyNum;
	}
}

