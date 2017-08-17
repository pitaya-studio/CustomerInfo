package com.trekiz.admin.modules.activity.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.validator.constraints.Length;

import com.trekiz.admin.agentToOffice.T2.entity.Rate;
import com.trekiz.admin.agentToOffice.T2.utils.RateUtils;
import com.trekiz.admin.common.persistence.DataEntity;
import com.trekiz.admin.common.utils.Collections3;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.statemachine.state.MutableStateBean;
import com.trekiz.admin.modules.stock.entity.ActivityGroupReserve;
import com.trekiz.admin.modules.stock.entity.ActivityReserveFile;


/**
 * 文件名: Activitygroup 功能: 产品Entity 修改记录:
 * 
 * @author liangjingming
 * @DateTime 2014-01-13
 * @version 1.0
 */
@Entity
@Table(name = "activitygroup")
@DynamicInsert
public class ActivityGroup extends DataEntity implements MutableStateBean{

	private static final long serialVersionUID = 1L;
	
	private Long id; // 编号

	/** 产品信息表ID外键 */
	private Integer srcActivityId;
	/** 出团编号 */
	private String groupCode;
	/** 产品中一个确定的出团日期 */
	private Date groupOpenDate;
	/** 产品中的一个确定的截团日期 */
	private Date groupCloseDate;
	/** 产品的签证国家名称 */
	private String visaCountry;
	/** 送达签证的日期 */
	private Date visaDate;
	/** 成人的产品结算价 */
	private BigDecimal settlementAdultPrice;
	/** 儿童的产品结算价 */
	private BigDecimal settlementcChildPrice;
//	/** trekiz成人价 */
//	private BigDecimal trekizPrice;
//	/** trekiz儿童价 */
//	private BigDecimal trekizChildPrice;
	/** 建议成人零售价 */
	private BigDecimal suggestAdultPrice;
	/** 建议儿童零售价 */
	private BigDecimal suggestChildPrice;
	/** 单房差 */
	private BigDecimal singleDiff;
	/** 产品的预收订金 */
	private BigDecimal payDeposit;
	/** 产品的预收人数 */
	private Integer planPosition;
	/** 产品的剩余位置 */
	private Integer freePosition;
	/** 产品的团控板的收客数量  0524需求  已收人数 = 收客数量 + 占位切位人数*/
	private Integer receptAmount;
	/** 各渠道总的占位人数 */
	private int nopayReservePosition;
	/**各渠道儿童占位人数*/
	private int nopayChildrenPosition;
	/**各渠道特殊人群占位人数*/
	private int nopayPeoplePosition;
	/** 各渠道总的切位人数置 */
	private int payReservePosition;
	/** 售出占位 */
	private int soldNopayPosition;
	/** 售出切位 */
	private int soldPayPosition;
	/** 产品上传出团通知书 */
	private Integer srcDocId;
	
	/**成本录入审核:0未通过,1待审核,2已通过, 4待录入*/
	private Integer review;
	
	/**成本审核当前层级*/
	private Integer nowLevel;
	
	private String income;
	
	private String cost;
	
	/**成本录入锁定：0:没锁定，1：锁定*/
	private Integer lockStatus;

	private TravelActivity travelActivity;

	private ActivityGroupReserve activityGroupReserve = null;

	private List<ActivityGroupReserve> activityGroupReserveList = null;
	
	private List<ActivityReserveFile> activityReserveFileList = null;

	private String remarks;
	
	/** 同业价特殊人群 */
	private BigDecimal settlementSpecialPrice;
	/** 建议特殊人群零售价 */
	private BigDecimal suggestSpecialPrice;
	/** 金额币种ID(按照表中出现的顺序，用逗号隔开)*/
	private String currencyType;
	/** 预报名人数分配情况 */
	private Integer orderPersonNum;
	/**	出团通知文件Id*/
	private Long openDateFile;
	/**	游轮产品团期舱型*/
	private Long spaceType;
	/** 某团期是否需要被选中为:有推荐团期状态，0不需要推荐，1需要推荐 */
	private Integer recommend;
	
	/**特殊人群最高人数**/
	private Integer maxPeopleCount;
	/**儿童最高人数**/
	private Integer maxChildrenCount;
	
	/**
	 * 当前团期剩余切位人数（总切位-占切位）
	 */
	private Integer leftpayReservePosition;

	/**提成状态 0为计算提成，1已计算提成*/
	private int iscommission;
    /**
	 * 应付日期
	 */
	private Date payableDate;
	
	/**
	 * 团期备注
	 */
	private String groupRemark;
	
	
	/**
	 * 对应需求号  223
	 */
	private Integer cruiseshipStockDetailId;
	
	/**
	 * 对应需求号  258
	 */
	private BigDecimal invoiceTax;
	
	/**
	 * 成人优惠
	 */
	private BigDecimal adultDiscountPrice;
	/**
	 * 儿童优惠
	 */
	private BigDecimal childDiscountPrice;
	/**
	 * 特殊人群优惠
	 */
	private BigDecimal specialDiscountPrice;
	
	/**
	 * 价格方案json
	 */
	private String priceJson;
	
	/**
	 * 团期酒店（多值逗号隔开）varchar 500
	 */
	private String groupHotel;
	
	/**
	 * 团期房型（多值逗号隔开）varchar 500
	 */
	private String groupHouseType;

	/** 已收人数（当前团期下的预定总人数） */
	private String totalOrderPersonNum;
	
	private List<Map<String, String>> hotelHouseList;
	
	//补位人数
	private Integer groupcoverNum;
	
	//补位流水号
	private Integer coverSerNum;
	
	/** quauq成人价 */
	private BigDecimal quauqAdultPrice;
	/** quauq儿童价 */
	private BigDecimal quauqChildPrice;
	/** quauq特殊人群价 */
	private BigDecimal quauqSpecialPrice;
	/** 成人 供应价 */
	private BigDecimal adultRetailPrice;
	/** 儿童 供应价 */
	private BigDecimal childRetailPrice;
	/** 特殊人群 供应价 */
	private BigDecimal specialRetailPrice;
	
	/**团期价格策略    addby:djw*/
	private Map<String, String> pricingStrategy;
	
		/** 是否上架到T1平台*/
	private Integer isT1;

	/**设置定价策略状态 0表示未设置状态，1表示需重新设置状态，2表示不需重新设置状态*/
	private Integer pricingStrategyStatus;

	@Column(name = "pricingStrategyStatus")
	public Integer getPricingStrategyStatus() {
		return pricingStrategyStatus;
	}

	public void setPricingStrategyStatus(Integer pricingStrategyStatus) {
		this.pricingStrategyStatus = pricingStrategyStatus;
	}

	@Column(name = "is_t1", nullable = false)
	public Integer getIsT1() {
		return isT1;
	}

	public void setIsT1(Integer isT1) {
		this.isT1 = isT1;
	}

	@Column(name = "iscommission", unique = false, nullable = false)
	public int getIscommission() {
		return iscommission;
	}

	public void setIscommission(int iscommission) {
		this.iscommission = iscommission;
	}

	public Date getPayableDate() {
		return payableDate;
	}

	public void setPayableDate(Date payableDate) {
		this.payableDate = payableDate;
	}

	@Transient
	public Integer getLeftpayReservePosition() {
		return leftpayReservePosition;
	}

	public void setLeftpayReservePosition(Integer leftpayReservePosition) {
		this.leftpayReservePosition = leftpayReservePosition;
	}

	public Integer getMaxPeopleCount() {
		return maxPeopleCount;
	}

	public void setMaxPeopleCount(Integer maxPeopleCount) {
		this.maxPeopleCount = maxPeopleCount;
	}
	

	/**
	 * 0:未录入成本
	 * 1:计调成本已提交
	 * 2:计调成本已保存
	 * 3:财务成本已保存
	 * 4:财务成本已提交/待总经理审核
	 * 7:审批通过
	 * 8:计调成本被驳回
	 * 9:财务成本被驳回
	 * 10:计调财务成本被驳回
	 * 11:计调财务成本被驳回，计调成本已重新修改
	 * 
	 */
	private Integer costStatus;
	
	private String forcastStatus;
	
	public String getForcastStatus() {
		return forcastStatus;
	}

	public void setForcastStatus(String forcastStatus) {
		this.forcastStatus = forcastStatus;
	}

	public ActivityGroup() {
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setSrcActivityId(Integer srcActivityId) {
		this.srcActivityId = srcActivityId;
	}

	@Column(name = "srcActivityId", unique = false, nullable = false, insertable = false, updatable = false)
	public Integer getSrcActivityId() {
		return this.srcActivityId;
	}

	@Column(name = "groupCode", unique = false, nullable = false)
	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public void setGroupOpenDate(Date groupOpenDate) {
		this.groupOpenDate = groupOpenDate;
	}

	@Column(name = "groupOpenDate", unique = false, nullable = false)
	public Date getGroupOpenDate() {
		return this.groupOpenDate;
	}

	/**
	 * 
	* @Title: getLeftdays
	* @Description: TODO(团期剩余天数)
	* 产品发布时选择出团日期（必填）、截团日期（非必填）；若只填写了出团日期，则团期剩余天数=出团日期—今日日期；
	* 若同时填写了出团日期及截团日期，则团期剩余天数=截团日期—今日日期
	* @return int    返回类型
	* @throws
	 */
	@Transient
	public int getLeftdays() {
		//当前时间
		long nowDateTime = DateUtils.parseDate(DateUtils.getDate("yyyy-MM-dd")).getTime();
		//团期剩余天数
		Long diffDate = null;
		if(getGroupOpenDate() == null) {
			return 0;
		}else if(getGroupOpenDate() != null && getGroupCloseDate() == null) {
			long openDateTime = getGroupOpenDate().getTime();
			diffDate = (nowDateTime - openDateTime) / (24 * 60 * 60 * 1000);
		}else if(getGroupOpenDate() != null && getGroupCloseDate() != null) {
			long closeDateTime = getGroupCloseDate().getTime();
			diffDate = (closeDateTime - nowDateTime) / (24 * 60 * 60 * 1000);
		}else {
			return 0;
		}
        return diffDate.intValue() ;
	}

	public void setGroupCloseDate(Date groupCloseDate) {
		this.groupCloseDate = groupCloseDate;
	}

	@Column(name = "groupCloseDate", unique = false, nullable = false)
	public Date getGroupCloseDate() {
		return this.groupCloseDate;
	}

	public void setVisaCountry(String visaCountry) {
		this.visaCountry = visaCountry;
	}

	@Length(min = 0, max = 50)
	@Column(name = "visaCountry", unique = false, nullable = false)
	public String getVisaCountry() {
		return this.visaCountry;
	}

	public void setVisaDate(Date visaDate) {
		this.visaDate = visaDate;
	}

	@Column(name = "visaDate", unique = false, nullable = false)
	public Date getVisaDate() {
		return this.visaDate;
	}

	public void setSettlementAdultPrice(BigDecimal settlementAdultPrice) {
		this.settlementAdultPrice = settlementAdultPrice;
	}

	@Column(name = "settlementAdultPrice", unique = false, nullable = false)
	public BigDecimal getSettlementAdultPrice() {
		return this.settlementAdultPrice;
	}

	public void setSettlementcChildPrice(BigDecimal settlementcChildPrice) {
		this.settlementcChildPrice = settlementcChildPrice;
	}

	@Column(name = "settlementcChildPrice", unique = false, nullable = false)
	public BigDecimal getSettlementcChildPrice() {
		return this.settlementcChildPrice;
	}
	
//	public void setTrekizPrice(Integer trekizPrice) {
//		this.trekizPrice = trekizPrice;
//	}
//	
//	@Column(name = "trekizPrice", unique = false, nullable = false)
//	public Integer getTrekizPrice() {
//		return this.trekizPrice;
//	}
//	
//	public void setTrekizChildPrice(Integer trekizChildPrice) {
//		this.trekizChildPrice = trekizChildPrice;
//	}
//	
//	@Column(name = "trekizChildPrice", unique = false, nullable = false)
//	public Integer getTrekizChildPrice() {
//		return this.trekizChildPrice;
//	}

	public void setSuggestAdultPrice(BigDecimal suggestAdultPrice) {
		this.suggestAdultPrice = suggestAdultPrice;
	}

	@Column(name = "suggestAdultPrice", unique = false, nullable = false)
	public BigDecimal getSuggestAdultPrice() {
		return this.suggestAdultPrice;
	}

	public void setSuggestChildPrice(BigDecimal suggestChildPrice) {
		this.suggestChildPrice = suggestChildPrice;
	}

	@Column(name = "suggestChildPrice", unique = false, nullable = false)
	public BigDecimal getSuggestChildPrice() {
		return this.suggestChildPrice;
	}

	@Column(name = "singleDiff", unique = false, nullable = false)
	public BigDecimal getSingleDiff() {
		return singleDiff;
	}

	public void setSingleDiff(BigDecimal singleDiff) {
		this.singleDiff = singleDiff;
	}

	public void setPayDeposit(BigDecimal payDeposit) {
		this.payDeposit = payDeposit;
	}

	@Column(name = "payDeposit", unique = false, nullable = false)
	public BigDecimal getPayDeposit() {
		return this.payDeposit;
	}

	public void setPlanPosition(Integer planPosition) {
		this.planPosition = planPosition;
	}

	@Column(name = "planPosition", unique = false, nullable = false)
	public Integer getPlanPosition() {
		return this.planPosition;
	}

	public void setFreePosition(Integer freePosition) {
		this.freePosition = freePosition;
	}

	@Column(name = "freePosition", unique = false, nullable = false)
	public Integer getFreePosition() {
		return this.freePosition;
	}
	
	@Column(name = "receptAmount", unique = false, nullable = false)
	public Integer getReceptAmount() {
		return receptAmount;
	}

	public void setReceptAmount(Integer receptAmount) {
		this.receptAmount = receptAmount;
	}

	@Column(name = "nopayReservePosition", unique = false, nullable = false)
	public int getNopayReservePosition() {
		return nopayReservePosition;
	}

	public void setNopayReservePosition(int nopayReservePosition) {
		this.nopayReservePosition = nopayReservePosition;
	}

	@Column(name = "payReservePosition", unique = false, nullable = false)
	public int getPayReservePosition() {
		return payReservePosition;
	}

	public void setPayReservePosition(int payReservePosition) {
		this.payReservePosition = payReservePosition;
	}

	@Column(name = "soldNopayPosition", unique = false, nullable = false)
	public int getSoldNopayPosition() {
		return soldNopayPosition;
	}

	public void setSoldNopayPosition(int soldNopayPosition) {
		this.soldNopayPosition = soldNopayPosition;
	}

	@Column(name = "soldPayPosition", unique = false, nullable = false)
	public int getSoldPayPosition() {
		return soldPayPosition;
	}

	public void setSoldPayPosition(int soldPayPosition) {
		this.soldPayPosition = soldPayPosition;
	}

	public void setSrcDocId(Integer srcDocId) {
		this.srcDocId = srcDocId;
	}

	@Column(name = "srcDocId", unique = false, nullable = false, insertable = false, updatable = false)
	public Integer getSrcDocId() {
		return this.srcDocId;
	}
	
	@Column(name = "remarks", unique = false, nullable = false)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@ContainedIn
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "srcActivityId", referencedColumnName = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	public TravelActivity getTravelActivity() {
		return travelActivity;
	}

	public void setTravelActivity(TravelActivity travelActivity) {
		this.travelActivity = travelActivity;
	}

	@Transient
	public ActivityGroupReserve getActivityGroupReserve() {
		return activityGroupReserve;
	}

	public void setActivityGroupReserve(ActivityGroupReserve activityGroupReserve) {
		this.activityGroupReserve = activityGroupReserve;
	}

	@Transient
	public List<ActivityReserveFile> getActivityReserveFileList() {
		return activityReserveFileList;
	}

	public void setActivityReserveFileList(
			List<ActivityReserveFile> activityReserveFileList) {
		this.activityReserveFileList = activityReserveFileList;
	}

	@Transient
	public String getPayVoucherIds(){
		if(this.activityReserveFileList!=null)
			return Collections3.extractToString(getActivityReserveFileList(),"srcDocId",",");
		else return "";
	}

	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "activityGroupId", referencedColumnName = "id")
	public List<ActivityGroupReserve> getActivityGroupReserveList() {
		return activityGroupReserveList;
	}

	public void setActivityGroupReserveList(
			List<ActivityGroupReserve> activityGroupReserveList) {
		this.activityGroupReserveList = activityGroupReserveList;
	}

	
	/////////////////////////////////////
	@Column(name = "cost_status", unique = false, nullable = false, columnDefinition = "int(3) default 0")
	public Integer getCostStatus() {
		return costStatus;
	}

	public void setCostStatus(Integer costStatus) {
		this.costStatus = costStatus;
	}

	private String versionNumber;
	
	@Column(name = "version_number", unique = false, nullable = true)
	public String getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}
	
	private Integer plusFreePosition = 0;
	private Integer plusNopayReservePosition = 0;
	private Integer plusPayReservePosition = 0;
	private Integer plusPlanPosition = 0;
	private Integer plusSoldNopayPosition = 0;
	private Integer plusSoldPayPosition = 0;
	
	private boolean hasEyelessAgents;//不能查看该团期的T1渠道商

	public boolean isHasEyelessAgents() {
		return hasEyelessAgents;
	}

	public void setHasEyelessAgents(boolean hasEyelessAgents) {
		this.hasEyelessAgents = hasEyelessAgents;
	}

	@Transient
	public Integer getPlusFreePosition() {
		return plusFreePosition;
	}
	@Transient
	public Integer getPlusNopayReservePosition() {
		return plusNopayReservePosition;
	}
	@Transient
	public Integer getPlusPayReservePosition() {
		return plusPayReservePosition;
	}
	@Transient
	public Integer getPlusPlanPosition() {
		return plusPlanPosition;
	}
	@Transient
	public Integer getPlusSoldNopayPosition() {
		return plusSoldNopayPosition;
	}
	@Transient
	public Integer getPlusSoldPayPosition() {
		return plusSoldPayPosition;
	}

	public void setPlusFreePosition(Integer plusFreePosition) {
		this.plusFreePosition = plusFreePosition;
	}


	public void setPlusNopayReservePosition(Integer plusNopayReservePosition) {
		this.plusNopayReservePosition = plusNopayReservePosition;
	}


	public void setPlusPayReservePosition(Integer plusPayReservePosition) {
		this.plusPayReservePosition = plusPayReservePosition;
	}

	public void setPlusPlanPosition(Integer plusPlanPosition) {
		this.plusPlanPosition = plusPlanPosition;
	}


	public void setPlusSoldNopayPosition(Integer plusSoldNopayPosition) {
		this.plusSoldNopayPosition = plusSoldNopayPosition;
	}


	public void setPlusSoldPayPosition(Integer plusSoldPayPosition) {
		this.plusSoldPayPosition = plusSoldPayPosition;
	}

	@Override
	@Transient
    public void setStatus(Integer status) {
	    this.setCostStatus(status);
    }

	@Override
	@Transient
    public Integer getStatus() {
	    return this.getCostStatus();
    }
	@Column(name = "settlementSpecialPrice", unique = false, nullable = false)
	public BigDecimal getSettlementSpecialPrice() {
		return settlementSpecialPrice;
	}

	public void setSettlementSpecialPrice(BigDecimal settlementSpecialPrice) {
		this.settlementSpecialPrice = settlementSpecialPrice;
	}
	@Column(name = "suggestSpecialPrice", unique = false, nullable = false)
	public BigDecimal getSuggestSpecialPrice() {
		return suggestSpecialPrice;
	}

	public void setSuggestSpecialPrice(BigDecimal suggestSpecialPrice) {
		this.suggestSpecialPrice = suggestSpecialPrice;
	}
	
	@Column(name = "currency_type", unique = false, nullable = false)
	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	@Column(name = "open_date_file")
	public Long getOpenDateFile() {
		return openDateFile;
	}

	public Long getSpaceType() {
		return spaceType;
	}

	public void setSpaceType(Long spaceType) {
		this.spaceType = spaceType;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public void setOpenDateFile(Long openDateFile) {
		this.openDateFile = openDateFile;
	}
	
	@Transient
	public Integer getOrderPersonNum() {
		return orderPersonNum;
	}

	public void setOrderPersonNum(Integer orderPersonNum) {
		this.orderPersonNum = orderPersonNum;
	}
	@Column(name = "review")
	public Integer getReview() {
		return review;
	}

	public void setReview(Integer review) {
		this.review = review;
	}
	@Column(name = "lockStatus")
	public Integer getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(Integer lockStatus) {
		this.lockStatus = lockStatus;
	}

	public Integer getNowLevel() {
		return nowLevel;
	}

	public void setNowLevel(Integer nowLevel) {
		this.nowLevel = nowLevel;
	}

	@Column(name = "recommend")
	public Integer getRecommend() {
		return recommend;
	}

	public void setRecommend(Integer recommend) {
		this.recommend = recommend;
	}

	/*@Column(name = "groupremark")
	public String getGroupRemark() {
		return groupRemark;
	}

	public void setGroupRemark(String groupRemark) {
		this.groupRemark = groupRemark;
	}*/

	@Column(name = "groupremark")
	public String getGroupRemark() {
		return groupRemark;
	}

	public void setGroupRemark(String groupRemark) {
		this.groupRemark = groupRemark;
	}

	@Column(name = "adultDiscountPrice")
	public BigDecimal getAdultDiscountPrice() {
		return adultDiscountPrice;
	}

	public void setAdultDiscountPrice(BigDecimal adultDiscountPrice) {
		this.adultDiscountPrice = adultDiscountPrice;
	}

	@Column(name = "childDiscountPrice")
	public BigDecimal getChildDiscountPrice() {
		return childDiscountPrice;
	}

	public void setChildDiscountPrice(BigDecimal childDiscountPrice) {
		this.childDiscountPrice = childDiscountPrice;
	}

	@Column(name = "specialDiscountPrice")
	public BigDecimal getSpecialDiscountPrice() {
		return specialDiscountPrice;
	}

	public void setSpecialDiscountPrice(BigDecimal specialDiscountPrice) {
		this.specialDiscountPrice = specialDiscountPrice;
	}

	@Column(name = "priceJson")
	public String getPriceJson() {
		return priceJson;
	}

	public void setPriceJson(String priceJson) {
		this.priceJson = priceJson;
	}

	@Column(name = "cruiseship_stock_detail_id")
	public Integer getCruiseshipStockDetailId() {
		return cruiseshipStockDetailId;
	}

	public void setCruiseshipStockDetailId(Integer cruiseshipStockDetailId) {
		this.cruiseshipStockDetailId = cruiseshipStockDetailId;
	}
	
	@Column(name = "invoice_tax")
	public BigDecimal getInvoiceTax() {
		return invoiceTax;
	}

	public void setInvoiceTax(BigDecimal invoiceTax) {
		this.invoiceTax = invoiceTax;
	}

	@Column(name = "group_hotel")
	public String getGroupHotel() {
		return groupHotel;
	}

	public void setGroupHotel(String groupHotel) {
		this.groupHotel = groupHotel;
	}

	@Column(name = "group_house_type")
	public String getGroupHouseType() {
		return groupHouseType;
	}

	public void setGroupHouseType(String groupHouseType) {
		this.groupHouseType = groupHouseType;
	}

	@Transient
	public String getTotalOrderPersonNum() {
		return totalOrderPersonNum;
	}

	public void setTotalOrderPersonNum(String totalOrderPersonNum) {
		this.totalOrderPersonNum = totalOrderPersonNum;
	}

	@Transient
	public List<Map<String, String>> getHotelHouseList() {
		if (CollectionUtils.isEmpty(hotelHouseList)) {
			hotelHouseList = new ArrayList<>();
			// 拆分酒店房型
			String[] hotels = StringUtils.isNotBlank(groupHotel) ? groupHotel.split(",") : null;
			String[] houseTypes = StringUtils.isNotBlank(groupHouseType) ? groupHouseType.split(",") : null;
			int maxLength;  // 使用酒店房型最大的长度
			if (houseTypes != null && hotels != null) {
				if (houseTypes.length > hotels.length) {
					maxLength = houseTypes.length;
				} else {
					maxLength = hotels.length;
				}
			} else {
				if (hotels != null) {
					maxLength = hotels.length;
				} else if (houseTypes != null) {
					maxLength = houseTypes.length;
				} else {
					maxLength = 0;
				}
			}
			// 组装
			if (maxLength > 0) {
				for (int i = 0; i < maxLength; i++) {
					Map<String, String> hotelHouseTypeMap = new HashMap<String, String>();
					if (hotels!=null && hotels.length > i) {
						hotelHouseTypeMap.put("hotel", hotels[i]);
					} else {
						hotelHouseTypeMap.put("hotel", "");						
					}
					if (houseTypes!=null && houseTypes.length > i) {
						hotelHouseTypeMap.put("houseType", houseTypes[i]);
					} else {						
						hotelHouseTypeMap.put("houseType", "");
					}
					hotelHouseList.add(hotelHouseTypeMap);
				}
			} else {
				Map<String, String> hotelHouseTypeMap = new HashMap<String, String>();
				hotelHouseTypeMap.put("hotel", "");
				hotelHouseTypeMap.put("houseType", "");
				hotelHouseList.add(hotelHouseTypeMap);
			}
		}
		return hotelHouseList;
	}

	public void setHotelHouseList(List<Map<String, String>> hotelHouseList) {
		this.hotelHouseList = hotelHouseList;
	}
	

	@Transient
	public Integer getGroupcoverNum() {
		return groupcoverNum;
	}

	public void setGroupcoverNum(Integer groupcoverNum) {
		this.groupcoverNum = groupcoverNum;
	}

	public Integer getCoverSerNum() {
		return coverSerNum;
	}

	public void setCoverSerNum(Integer coverSerNum) {
		this.coverSerNum = coverSerNum;
	}
	
	@Column(name = "quauqAdultPrice")
	public BigDecimal getQuauqAdultPrice() {
		return quauqAdultPrice;
	}

	public void setQuauqAdultPrice(BigDecimal quauqAdultPrice) {
		this.quauqAdultPrice = quauqAdultPrice;
	}

	@Column(name = "quauqChildPrice")
	public BigDecimal getQuauqChildPrice() {
		return quauqChildPrice;
	}

	public void setQuauqChildPrice(BigDecimal quauqChildPrice) {
		this.quauqChildPrice = quauqChildPrice;
	}

	@Column(name = "quauqSpecialPrice")
	public BigDecimal getQuauqSpecialPrice() {
		return quauqSpecialPrice;
	}

	public void setQuauqSpecialPrice(BigDecimal quauqSpecialPrice) {
		this.quauqSpecialPrice = quauqSpecialPrice;
	}

	@Transient
	public BigDecimal getAdultRetailPrice() {
		if (quauqAdultPrice != null) {
			Rate rate = RateUtils.getRate(id, travelActivity.getActivityKind());
			//BigDecimal rate = UserUtils.getUser().getCompany().getChargeRate();
			if (rate != null) {
				if (rate.getQuauqRateType() == 0) {
					adultRetailPrice = quauqAdultPrice.add(quauqAdultPrice.multiply(rate.getQuauqRate()));
				} else {
					adultRetailPrice = quauqAdultPrice.add(rate.getQuauqRate());
				}
			}else{
				adultRetailPrice = quauqAdultPrice.add(quauqAdultPrice.multiply(new BigDecimal(0.01)));
			}
		}
		return adultRetailPrice;
	}

	@Transient
	public BigDecimal getChildRetailPrice() {
		if (quauqChildPrice != null) {
			Rate rate = RateUtils.getRate(id, travelActivity.getActivityKind());
			//BigDecimal rate = UserUtils.getUser().getCompany().getChargeRate();
			if(rate != null){
				if (rate.getQuauqRateType() == 0) {
					childRetailPrice = quauqChildPrice.add(quauqChildPrice.multiply(rate.getQuauqRate()));
				} else {
					childRetailPrice = quauqChildPrice.add(rate.getQuauqRate());
				}
			}else{
				childRetailPrice = quauqChildPrice.add(quauqChildPrice.multiply(new BigDecimal(0.01)));
			}
		}
		return childRetailPrice;
	}

	@Transient
	public BigDecimal getSpecialRetailPrice() {
		if (quauqSpecialPrice != null) {
			Rate rate = RateUtils.getRate(id, travelActivity.getActivityKind());
			if(rate != null){
				if (rate.getQuauqRateType() == 0) {
					specialRetailPrice = quauqSpecialPrice.add(quauqSpecialPrice.multiply(rate.getQuauqRate()));
				} else {
					specialRetailPrice = quauqSpecialPrice.add(rate.getQuauqRate());
				}
			}else{
				specialRetailPrice = quauqSpecialPrice.add(quauqSpecialPrice.multiply(new BigDecimal(0.01)));
			}
		}
		return specialRetailPrice;
	}
	
	@Transient
	public Map<String, String> getPricingStrategy() {
		return pricingStrategy;
	}
	
	public void setPricingStrategy(Map<String, String> pricingStrategy) {
		this.pricingStrategy = pricingStrategy;
	}

	public Integer getMaxChildrenCount() {
		return maxChildrenCount;
	}

	public void setMaxChildrenCount(Integer maxChildrenCount) {
		this.maxChildrenCount = maxChildrenCount;
	}
	@Transient
	public int getNopayChildrenPosition() {
		return nopayChildrenPosition;
	}

	public void setNopayChildrenPosition(int nopayChildrenPosition) {
		this.nopayChildrenPosition = nopayChildrenPosition;
	}
	@Transient
	public int getNopayPeoplePosition() {
		return nopayPeoplePosition;
	}

	public void setNopayPeoplePosition(int nopayPeoplePosition) {
		this.nopayPeoplePosition = nopayPeoplePosition;
	}

}
