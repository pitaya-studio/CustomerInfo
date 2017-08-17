/**
 *
 */
package com.trekiz.admin.modules.activity.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.Where;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.trekiz.admin.agentToOffice.line.entity.TouristLine;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.DataEntity;
import com.trekiz.admin.common.utils.Collections3;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.entity.FlightInfo;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceRecord;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.SysDefineDict;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.DictUtils;

/**
 * 文件名: Travelactivity 功能: 产品Entity 修改记录:
 * 
 * @author liangjingming
 * @DateTime 2014-01-13
 * @version 1.0
 */
@Entity
@Table(name = "travelactivity")
@DynamicInsert
public class TravelActivity extends DataEntity {

	private static final long serialVersionUID = 1L;

	public static final Integer IS_ADD_SYNC_SUCCESS = 1;

	/** 产品ID */
	private Long id;

	/** 产品编号,如SG0001 */
	private String activitySerNum;
	
	/** 旅游产品的名称 */
	private String acitivityName;
	
	/** 产品所属的类型ID */
	private Integer activityTypeId;
	
	/** 产品所属的类型名称 */
	private String activityTypeName;
	
	/** 产品系列ID */
	private Integer activityLevelId;
	
	/** 产品系列名称 */
	private String activityLevelName;
	
	/** 付款方式 */
	private String payMode;
	
	/** 保留天数 */
	private Integer remainDays;
	
	/** 旅游类型ID */
	private Integer travelTypeId;
	
	/** 旅游类型名称 */
	private String travelTypeName;
	
	/** 旅游出发城市 */
	private Integer fromArea;
	
	/** 旅游离境城市 */
	private Integer outArea;
	
	/** 旅游产品行程天数 */
	private Integer activityDuration;
	
	/** 产品的主要交通方式 */
	private Integer trafficMode;
	
	/** 产品的主要交通方式具体对应的ID */
	private String trafficName;
	
	/** 产品机场信息ID*/
	private Long flightInfo;
	
	/** 该产品的最早出团团编码 */
	private String groupOpenCode;
	
	/** 该产品的最早出团日期 */
	private Date groupOpenDate;
	
	/** 该产品的最晚出团团编码 */
	private String groupCloseCode;
	
	/** 该产品的最晚出团日期 */
	private Date groupCloseDate;
	
	/** 产品的上下架状态 */
	private Integer activityStatus;
	
	/** 产品信息的更新时间 */
	private Date recentUpdateTime;
	
	/** 产品所属批发商的ID */
	private Integer wholeSalerId;
	
	/** 成人的产品最低结算价 */
	private BigDecimal settlementAdultPrice;
	
	/** 建议成人最低零售价 */
	private BigDecimal suggestAdultPrice;
	
	/** 领队人名称  */
	private String groupLead;
	
	/** 金额币种ID(按照表中出现的顺序，用逗号隔开)*/
	private String currencyType;
	
	/** 产品种类ID*/
	private Integer activityKind;
	
	/** 产品种类名称*/
	private String activityKindName;
	
	/** 用户当前机构Id */
	private Long proCompany;

	/** 用户当前机构名称 */
	private String proCompanyName;
	
	/** 付款方式：预占位 0表示没有使用，1表示使用 */
	private Integer payMode_advance;
	/** 付款方式：订金支付 0表示没有使用，1表示使用 */
	private Integer payMode_deposit;
	/** 支付方式：全款支付 0表示没有使用，1表示使用 */
	private Integer payMode_full;
	/** 支付方式：计调确认占位 0表示没有使用，1表示使用 */
	private Integer payMode_op;
	/** 付款方式：资料占位 0表示没有使用，1表示使用 */
	private Integer payMode_data;
	/** 付款方式：担保占位 0表示没有使用，1表示使用 */
	private Integer payMode_guarantee;
	/** 支付方式：确认单占位 0表示没有使用，1表示使用 */
	private Integer payMode_express;
	
	
	/** '支付方式：财务确认占位 0表示没有使用，1表示使用' */
	private Integer payMode_cw;
	
	

	@Column(name = "payMode_cw", unique = false, nullable = false)
	public Integer getPayMode_cw()
	{
		return payMode_cw;
	}

	public void setPayMode_cw(Integer payMode_cw)
	{
		this.payMode_cw = payMode_cw;
	}
	
	
	/** 预占位保留天数 */
	private Integer remainDays_advance;
	/**预占位保留小时*/
	private Integer remainDays_advance_hour;
	/**预占位保留分钟*/
	private Integer remainDays_advance_fen;
	/** 订金占位保留天数 */
	private Integer remainDays_deposit;
	/** 订金占位保留小时 */
	private Integer remainDays_deposit_hour;
	/** 订金占位保留分钟 */
	private Integer remainDays_deposit_fen;
	/** 担保占位保留天数 */
	private Integer remainDays_guarantee;
	/** 担保占位保留小时*/
	private Integer remainDays_guarantee_hour;
	/** 担保占位保留分钟*/
	private Integer remainDays_guarantee_fen;
	/** 确认单占位保留天数 */
	private Integer remainDays_express;
	/** 确认单占位保留小时 */
	private Integer remainDays_express_hour;
	/** 确认单占位保留分钟 */
	private Integer remainDays_express_fen;
	/** 资料占位保留天数 */
	private Integer remainDays_data;
	/** 资料占位保留小时 */
	private Integer remainDays_data_hour;
	/** 资料占位保留分钟*/
	private Integer remainDays_data_fen;
	
	private Integer overseasFlag;

	private Integer isSyncSuccess;

	private Integer isAfterSupplement;
	
	private Integer opUserId;
	
	/** 特殊人群备注*/
	private String specialRemark;
	
	/** 单房差单位*/
	private String singleDiffUnit;
	
	/**	仅用于判断当前统计数据是否生成过*/
	private boolean sumflag = false;

	/**	预收人数*/
	private int planPosition;

	/**	切位人数*/
	private int reservePosition;

	/**	余位人数*/
	private int freePosition;
	
	/**	产品所属部门id*/
	private Long deptId;

	/** 关联机票产品*/
	private ActivityAirTicket activityAirTicket;
	
	/** 单团产品询价记录*/
	private EstimatePriceRecord estimatePriceRecord;
	
	/** 旅游出发城市 */
	private Integer backArea;
	
	/** 产品备注 */
	private String remarks;

	/**产品相关文件*/
	private Set<ActivityFile> activityFiles;

	/**	目的地列表*/
	private List<Area> targetAreaList;

	/**	目的地名称列表*/
	private List<String> targetAreaNameList;
	
	/**	可见用户 */
	private List<User> opUserList;
	
	/**	可见用户名称列表*/
	private List<String> opUserNameList;
	
	/**	产品团期*/
	private Set<ActivityGroup> activityGroups;

	/**产品团期list*/
	private List<ActivityGroup> activityGroupList = new ArrayList<>();
	/** 团号标识 */
	private String groupNoMark;

	/**应付账期*/
	private Date payableDate;
	
	/**特殊人群最高人数**/
	private Integer maxPeopleCount;
	
	/** isT1 如果团期中有下架的则产品视为T1下架 **/
	private Integer isT1 = 0;

	private Long touristLineId;

	private TouristLine touristLine;
	
	public Integer getMaxPeopleCount() {
		return maxPeopleCount;
	}

	public void setMaxPeopleCount(Integer maxPeopleCount) {
		this.maxPeopleCount = maxPeopleCount;
	}
	
	

	public TravelActivity() {
		super();
	}

	public TravelActivity(Long id) {
		this();
		this.id = id;
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

	public void setActivitySerNum(String activitySerNum) {
		this.activitySerNum = activitySerNum;
	}

	@Length(min = 0, max = 500)
	@Column(name = "activitySerNum", unique = true, nullable = false)
	public String getActivitySerNum() {
		return this.activitySerNum;
	}

	public void setAcitivityName(String acitivityName) {
		this.acitivityName = acitivityName;
	}

	@Length(min = 0, max = 256)
	@Column(name = "acitivityName", unique = false, nullable = false)
	public String getAcitivityName() {
		return this.acitivityName;
	}

	public void setActivityTypeId(Integer activityTypeId) {
		this.activityTypeId = activityTypeId;
	}

	@Column(name = "activityTypeId", unique = false, nullable = false)
	public Integer getActivityTypeId() {
		return this.activityTypeId;
	}

	public void setActivityTypeName(String activityTypeName) {
		this.activityTypeName = activityTypeName;
	}

	@Length(min = 0, max = 50)
	@Column(name = "activityTypeName", unique = false, nullable = false)
	public String getActivityTypeName() {
		return this.activityTypeName;
	}

	@Column(name = "activityLevelId", unique = false, nullable = false)
	public Integer getActivityLevelId() {
		return activityLevelId;
	}

	public void setActivityLevelId(Integer activityLevelId) {
		this.activityLevelId = activityLevelId;
	}

	@Length(min = 0, max = 50)
	@Column(name = "activityLevelName", unique = false, nullable = false)
	public String getActivityLevelName() {
		return activityLevelName;
	}

	public void setActivityLevelName(String activityLevelName) {
		this.activityLevelName = activityLevelName;
	}

	@Column(name = "payMode", unique = false, nullable = false)
	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	@Column(name = "remainDays", unique = false, nullable = false)
	public Integer getRemainDays() {
		return remainDays;
	}

	public void setRemainDays(Integer remainDays) {
		this.remainDays = remainDays;
	}

	@Column(name = "travelTypeId", unique = false, nullable = false)
	public Integer getTravelTypeId() {
		return travelTypeId;
	}

	public void setTravelTypeId(Integer travelTypeId) {
		this.travelTypeId = travelTypeId;
	}

	@Length(min = 0, max = 50)
	@Column(name = "travelTypeName", unique = false, nullable = false)
	public String getTravelTypeName() {
		return travelTypeName;
	}

	public void setTravelTypeName(String travelTypeName) {
		this.travelTypeName = travelTypeName;
	}

	public void setFromArea(Integer fromArea) {
		this.fromArea = fromArea;
	}

	@Column(name = "outArea", unique = false, nullable = false)
	public Integer getOutArea() {
		return this.outArea;
	}

    public void setOutArea(Integer outArea) {
        this.outArea = outArea;
    }

    @Column(name = "fromArea", unique = false, nullable = false)
    public Integer getFromArea() {
        return this.fromArea;
    }


	@Transient
	public String getFromAreaName() {
		if (getFromArea() == null)
			return "";
		return DictUtils
				.getDictLabel(getFromArea().toString(), "from_area", "");
	}
	
	@Transient
	public String getBackAreaName() {
		if (getBackArea() == null)
			return "";
		return DictUtils
				.getDictLabel(getBackArea().toString(), "from_area", "");
	}
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "activitytargetarea", joinColumns = { @JoinColumn(name = "srcActivityId") }, inverseJoinColumns = { @JoinColumn(name = "targetAreaId") })
	@Where(clause = "delFlag='" + DEL_FLAG_NORMAL + "'")
	@Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	public List<Area> getTargetAreaList() {
		return targetAreaList;
	}

	public void setTargetAreaList(List<Area> targetAreaList) {
		this.targetAreaList = targetAreaList;
	}

	@Transient
	public String getTargetAreaIds() {
		return Collections3.convertToString(getTargetAreaIdList(), ",");
	}

	@Transient
	public String getTargetAreaNames() {

		return Collections3.extractToString(getTargetAreaList(), "name", ",");
	}

	@Transient
	public String getTargetAreaNamess() {
		return Collections3.convertToString(getTargetAreaNameList(), ",");
	}

	@Transient
	public List<String> getTargetAreaNameList() {
		return targetAreaNameList;
	}

	@Transient
	public void setTargetAreaNameList(List<String> targetAreaNameList) {
		this.targetAreaNameList = targetAreaNameList;
	}

	@Transient
	public List<Long> getTargetAreaIdList() {
		List<Long> targetAreaIdList = Lists.newArrayList();
		if (targetAreaList != null) {
			for (Area area : targetAreaList) {
				targetAreaIdList.add(area.getId());
			}
		}
		return targetAreaIdList;
	}

	@Transient
	public void setTargetAreaIdList(List<Long> targetAreaIdList) {
		targetAreaList = Lists.newArrayList();
		if (targetAreaIdList != null) {
			for (Long areaId : targetAreaIdList) {
				Area area = new Area();
				area.setId(areaId);
				targetAreaList.add(area);
			}
		}
	}

	/*******************************可见用户开始************************************/
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "activity_op_user", joinColumns = { @JoinColumn(name = "srcActivityId") }, inverseJoinColumns = { @JoinColumn(name = "opUserId") })
	@Where(clause = "delFlag='" + DEL_FLAG_NORMAL + "'")
	@Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	public List<User> getOpUserList() {
		return opUserList;
	}

	public void setOpUserList(List<User> opUserList) {
		this.opUserList = opUserList;
	}

	@Transient
	public String getOpUserIds() {
		return Collections3.convertToString(getOpUserIdList(), ",");
	}

	@Transient
	public String getOpUserNames() {

		return Collections3.extractToString(getOpUserList(), "name", ",");
	}

	@Transient
	public String getOpUserNamess() {
		return Collections3.convertToString(getOpUserNameList(), ",");
	}

	@Transient
	public List<String> getOpUserNameList() {
		return opUserNameList;
	}

	@Transient
	public void setOpUserNameList(List<String> opUserNameList) {
		this.opUserNameList = opUserNameList;
	}

	@Transient
	public List<Long> getOpUserIdList() {
		List<Long> opUserIdList = Lists.newArrayList();
		if (opUserList != null) {
			for (User user : opUserList) {
				opUserIdList.add(user.getId());
			}
		}
		return opUserIdList;
	}

	@Transient
	public void setOpUserIdList(List<Long> opUserIdList) {
		opUserList = Lists.newArrayList();
		if (opUserIdList != null) {
			for (Long userId : opUserIdList) {
				User user = new User();
				boolean flag = true;
				user.setId(userId);
				for(int i=0;i<opUserList.size();i++)
				{
					if(opUserList.get(i).getId().toString().equals(userId.toString())  )
					{
						flag =false;
						continue;
					}
				}
				if(flag == true)
				opUserList.add(user);
			}
			
		}
	}
	
	/***********************************可见用户结束*************************************************/
	
	public void setActivityDuration(Integer activityDuration) {
		this.activityDuration = activityDuration;
	}

	@Column(name = "activityDuration", unique = false, nullable = false)
	public Integer getActivityDuration() {
		return this.activityDuration;
	}

	public void setTrafficMode(Integer trafficMode) {
		this.trafficMode = trafficMode;
	}

	@Column(name = "trafficMode", unique = false, nullable = false)
	public Integer getTrafficMode() {
		return this.trafficMode;
	}

	@Column(name = "trafficName", unique = false, nullable = false)
	public String getTrafficName() {
		return trafficName;
	}
	
	@Column(name = "flightInfo", unique = false, nullable = false)
	public Long getFlightInfo() {
		return flightInfo;
	}

	public void setFlightInfo(Long flightInfo) {
		this.flightInfo = flightInfo;
	}
	
	
	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	@Transient
	public String getTrafficNameLabel() {
		if (getTrafficName() == null)
			return "";
		Dict dict = DictUtils.getDict(getTrafficName(), Context.TRAFFIC_NAME);
		if (dict == null)
			return null;
		else
			return dict.getLabel();
	}

	@Transient
	public String getTrafficNameDesc() {
		if (getActivityAirTicket() == null || StringUtils.isBlank(getActivityAirTicket().getAirlines())) {
			return "";
		}
		List<FlightInfo> FlightInfoList = getActivityAirTicket().getFlightInfos();
		StringBuffer description = new StringBuffer();
		Dict dict = new Dict();
		for(FlightInfo flightInfo : FlightInfoList) {
			dict = DictUtils.getDict(flightInfo.getAirlines(), Context.TRAFFIC_NAME);
			if (dict != null) {
				description.append(dict.getDescription() + ",");
			}
		}
		if(description.length() > 0){
			return description.toString().substring(0, description.length() - 1);
		}else{
			return "";
		}
	}

	@Transient
	public String getTrafficModeName() {
		SysDefineDict dict = DictUtils.getDefineDict(Context.TRAFFIC_MODE,
				getTrafficMode() == null ? "" : getTrafficMode().toString(), getProCompany());
		if (dict == null)
			return null;
		else
			return dict.getLabel();
	}

	public void setTrafficName(String trafficName) {
		this.trafficName = trafficName;
	}

	public void setGroupOpenDate(Date groupOpenDate) {
		this.groupOpenDate = groupOpenDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "groupOpenDate", unique = false, nullable = false)
	public Date getGroupOpenDate() {
		return this.groupOpenDate;
	}

	public void setGroupCloseDate(Date groupCloseDate) {
		this.groupCloseDate = groupCloseDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "groupCloseDate", unique = false, nullable = false)
	public Date getGroupCloseDate() {
		return this.groupCloseDate;
	}

	public void setActivityStatus(Integer activityStatus) {
		this.activityStatus = activityStatus;
	}

	@Column(name = "activityStatus", unique = false, nullable = false)
	public Integer getActivityStatus() {
		return this.activityStatus;
	}

	@Transient
	public String getActivityStatusLabel() {
		return DictUtils.getDictLabel(String.valueOf(this.activityStatus),
				"product_type", "");
	}

	public void setRecentUpdateTime(Date recentUpdateTime) {
		this.recentUpdateTime = recentUpdateTime;
	}

	@Column(name = "recentUpdateTime", unique = false, nullable = false)
	public Date getRecentUpdateTime() {
		return this.recentUpdateTime;
	}

	public void setWholeSalerId(Integer wholeSalerId) {
		this.wholeSalerId = wholeSalerId;
	}

	@Column(name = "wholeSalerId", unique = false, nullable = false)
	public Integer getWholeSalerId() {
		return this.wholeSalerId;
	}

	@Column(name = "proCompany", unique = false, nullable = false)
	public Long getProCompany() {
		return proCompany;
	}

	public void setProCompany(Long proCompany) {
		this.proCompany = proCompany;
	}

	@Column(name = "proCompanyName", unique = false, nullable = false)
	public String getProCompanyName() {
		return proCompanyName;
	}

	public void setProCompanyName(String proCompanyName) {
		this.proCompanyName = proCompanyName;
	}
	
	@Column(name = "group_lead", unique = false, nullable = false)
	public String getGroupLead() {
		return groupLead;
	}

	public void setGroupLead(String groupLead) {
		this.groupLead = groupLead;
	}
	
	@Column(name = "currency_type", unique = false, nullable = false)
	public String getCurrencyType() {
		return currencyType;
	}

	public void setCurrencyType(String currencyType) {
		this.currencyType = currencyType;
	}

	@Column(name = "activity_kind", unique = false, nullable = false, columnDefinition = "tinyint default 1")
	public Integer getActivityKind() {
		return activityKind;
	}

	public void setActivityKind(Integer activityKind) {
		this.activityKind = activityKind;
	}

	@Column(name = "activity_kind_name", unique = false, nullable = false)
	public String getActivityKindName() {
		return activityKindName;
	}

	public void setActivityKindName(String activityKindName) {
		this.activityKindName = activityKindName;
	}	

	// @Transient
	@Column(name = "settlementAdultPrice", unique = false, nullable = false)
	public BigDecimal getSettlementAdultPrice() {
		// Integer minValue = Integer.MAX_VALUE;
		// Iterator<ActivityGroup> ite = activityGroups.iterator();
		// while(ite.hasNext()){
		// ActivityGroup group = ite.next();
		// if(minValue > group.getMinSettlementAdultPrice())
		// minValue = group.getMinSettlementAdultPrice();
		// }
		// if(minValue == Integer.MAX_VALUE)return 0;
		// return minValue;
		return settlementAdultPrice;
	}

	public void setSettlementAdultPrice(BigDecimal settlementAdultPrice) {
		this.settlementAdultPrice = settlementAdultPrice;
	}

	// @Transient
	@Column(name = "suggestAdultPrice", unique = false, nullable = false)
	public BigDecimal getSuggestAdultPrice() {
		// Integer minValue = Integer.MAX_VALUE;
		// Iterator<ActivityGroup> ite = activityGroups.iterator();
		// while(ite.hasNext()){
		// ActivityGroup group = ite.next();
		// if(minValue > group.getSuggestAdultPrice())
		// minValue = group.getSuggestAdultPrice();
		// }
		// if(minValue == Integer.MAX_VALUE)return 0;
		// return minValue;
		return suggestAdultPrice;
	}

	public void setSuggestAdultPrice(BigDecimal suggestAdultPrice) {
		this.suggestAdultPrice = suggestAdultPrice;
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "travelActivity", targetEntity = ActivityGroup.class, fetch = FetchType.LAZY)
	@BatchSize(size = 10)
	@NotFound(action = NotFoundAction.IGNORE)
	@Where(clause = "delFlag='" + DEL_FLAG_NORMAL + "'")
	@OrderBy(value = "groupOpenDate")
	public Set<ActivityGroup> getActivityGroups() {
		return activityGroups;
	}

	public void setActivityGroups(Set<ActivityGroup> activityGroups) {
		this.activityGroups = activityGroups;
	}

	private synchronized void initSum() {
		if (!sumflag) {
			getActivityGroups();
			for (ActivityGroup group : activityGroups) {
				planPosition += group.getPlanPosition() == null ? 0 : group
						.getPlanPosition();
				freePosition += group.getFreePosition() == null ? 0 : group
						.getFreePosition();
				reservePosition += group.getPayReservePosition();
			}
			sumflag = true;
		}
	}

	@Transient
	public int getPlanPosition() {
		initSum();
		return planPosition;
	}

	@Transient
	public int getReservePosition() {
		initSum();
		return reservePosition;
	}

	@Transient
	public int getFreePosition() {
		initSum();
		return freePosition;
	}

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "travelActivity", targetEntity = ActivityFile.class, fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	public Set<ActivityFile> getActivityFiles() {
		return activityFiles;
	}

	public void setActivityFiles(Set<ActivityFile> activityFiles) {
		this.activityFiles = activityFiles;
	}

	public void setOverseasFlag(Integer overseasFlag) {
		this.overseasFlag = overseasFlag;
	}

	@Column(name = "overseasFlag", unique = false, nullable = false)
	public Integer getOverseasFlag() {
		return overseasFlag;
	}

	@Column(name = "is_sync_success", unique = false, nullable = true)
	public Integer getIsSyncSuccess() {
		return isSyncSuccess;
	}

	public void setIsSyncSuccess(Integer isSyncSuccess) {
		this.isSyncSuccess = isSyncSuccess;
	}

	@Column(name = "is_after_supplement", columnDefinition = "tinyint default 0")
	public Integer getIsAfterSupplement() {
		return isAfterSupplement;
	}

	public void setIsAfterSupplement(Integer isAfterSupplement) {
		this.isAfterSupplement = isAfterSupplement;
	}

	@Column(name = "payMode_advance", unique = false, nullable = false)
	public Integer getPayMode_advance() {
		return payMode_advance;
	}

	public void setPayMode_advance(Integer payModeAdvance) {
		payMode_advance = payModeAdvance;
	}

	@Column(name = "payMode_deposit", unique = false, nullable = false)
	public Integer getPayMode_deposit() {
		return payMode_deposit;
	}

	public void setPayMode_deposit(Integer payModeDeposit) {
		payMode_deposit = payModeDeposit;
	}

	@Column(name = "payMode_full", unique = false, nullable = false)
	public Integer getPayMode_full() {
		return payMode_full;
	}

	public void setPayMode_full(Integer payModeFull) {
		payMode_full = payModeFull;
	}
	
	@Column(name = "payMode_op", unique = false, nullable = false)
	public Integer getPayMode_op() {
		return payMode_op;
	}

	public void setPayMode_op(Integer payModeOp) {
		payMode_op = payModeOp;
	}

	@Column(name = "remainDays_advance", unique = false, nullable = false)
	public Integer getRemainDays_advance() {
		return remainDays_advance;
	}

	public void setRemainDays_advance(Integer remainDaysAdvance) {
		remainDays_advance = remainDaysAdvance;
	}

	@Column(name = "remainDays_deposit", unique = false, nullable = false)
	public Integer getRemainDays_deposit() {
		return remainDays_deposit;
	}

	public void setRemainDays_deposit(Integer remainDaysDeposit) {
		remainDays_deposit = remainDaysDeposit;
	}
	@Column(name = "payMode_data", unique = false, nullable = false)
	public Integer getPayMode_data() {
		return payMode_data;
	}

	public void setPayMode_data(Integer payMode_data) {
		this.payMode_data = payMode_data;
	}
	@Column(name = "payMode_guarantee", unique = false, nullable = false)
	public Integer getPayMode_guarantee() {
		return payMode_guarantee;
	}

	public void setPayMode_guarantee(Integer payMode_guarantee) {
		this.payMode_guarantee = payMode_guarantee;
	}
	@Column(name = "payMode_express", unique = false, nullable = false)
	public Integer getPayMode_express() {
		return payMode_express;
	}

	public void setPayMode_express(Integer payMode_express) {
		this.payMode_express = payMode_express;
	}
	@Column(name = "remainDays_guarantee", unique = false, nullable = false)
	public Integer getRemainDays_guarantee() {
		return remainDays_guarantee;
	}

	public void setRemainDays_guarantee(Integer remainDays_guarantee) {
		this.remainDays_guarantee = remainDays_guarantee;
	}
	@Column(name = "remainDays_express", unique = false, nullable = false)
	public Integer getRemainDays_express() {
		return remainDays_express;
	}

	public void setRemainDays_express(Integer remainDays_express) {
		this.remainDays_express = remainDays_express;
	}
	@Column(name = "remainDays_data", unique = false, nullable = false)
	public Integer getRemainDays_data() {
		return remainDays_data;
	}

	public void setRemainDays_data(Integer remainDays_data) {
		this.remainDays_data = remainDays_data;
	}

	@ManyToOne
	@JoinColumn(name="airticket_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public ActivityAirTicket getActivityAirTicket() {
		return activityAirTicket;
	}

	public void setActivityAirTicket(ActivityAirTicket activityAirTicket) {
		this.activityAirTicket = activityAirTicket;
	}
	
	@ManyToOne
	@JoinColumn(name="estimate_price_id")
	@NotFound(action = NotFoundAction.IGNORE)
	public EstimatePriceRecord getEstimatePriceRecord() {
		return estimatePriceRecord;
	}

	public void setEstimatePriceRecord(EstimatePriceRecord estimatePriceRecord) {
		this.estimatePriceRecord = estimatePriceRecord;
	}

	public Integer getBackArea() {
		return backArea;
	}

	public void setBackArea(Integer backArea) {
		this.backArea = backArea;
	}
	
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	
	@Column(name = "special_remark", unique = false, nullable = false)
	public String getSpecialRemark() {
		return specialRemark;
	}

	public void setSpecialRemark(String specialRemark) {
		this.specialRemark = specialRemark;
	}

	@Column(name = "singleDiff_unit", unique = false, nullable = false)
	public String getSingleDiffUnit() {
		return singleDiffUnit;
	}

	public void setSingleDiffUnit(String singleDiffUnit) {
		this.singleDiffUnit = singleDiffUnit;
	}


	public String getGroupOpenCode() {
		return groupOpenCode;
	}

	public void setGroupOpenCode(String groupOpenCode) {
		this.groupOpenCode = groupOpenCode;
	}

	public String getGroupCloseCode() {
		return groupCloseCode;
	}

	public void setGroupCloseCode(String groupCloseCode) {
		this.groupCloseCode = groupCloseCode;
	}

	public Integer getOpUserId() {
		return opUserId;
	}

	public void setOpUserId(Integer opUserId) {
		this.opUserId = opUserId;
	}

	public String getGroupNoMark() {
		return groupNoMark;
	}

	public void setGroupNoMark(String groupNoMark) {
		this.groupNoMark = groupNoMark;
	}

	public Integer getRemainDays_advance_hour() {
		return remainDays_advance_hour;
	}

	public void setRemainDays_advance_hour(Integer remainDays_advance_hour) {
		this.remainDays_advance_hour = remainDays_advance_hour;
	}

	public Integer getRemainDays_advance_fen() {
		return remainDays_advance_fen;
	}

	public void setRemainDays_advance_fen(Integer remainDays_advance_fen) {
		this.remainDays_advance_fen = remainDays_advance_fen;
	}

	public Integer getRemainDays_deposit_hour() {
		return remainDays_deposit_hour;
	}

	public void setRemainDays_deposit_hour(Integer remainDays_deposit_hour) {
		this.remainDays_deposit_hour = remainDays_deposit_hour;
	}

	public Integer getRemainDays_deposit_fen() {
		return remainDays_deposit_fen;
	}

	public void setRemainDays_deposit_fen(Integer remainDays_deposit_fen) {
		this.remainDays_deposit_fen = remainDays_deposit_fen;
	}

	public Integer getRemainDays_guarantee_hour() {
		return remainDays_guarantee_hour;
	}

	public void setRemainDays_guarantee_hour(Integer remainDays_guarantee_hour) {
		this.remainDays_guarantee_hour = remainDays_guarantee_hour;
	}

	public Integer getRemainDays_guarantee_fen() {
		return remainDays_guarantee_fen;
	}

	public void setRemainDays_guarantee_fen(Integer remainDays_guarantee_fen) {
		this.remainDays_guarantee_fen = remainDays_guarantee_fen;
	}

	public Integer getRemainDays_express_hour() {
		return remainDays_express_hour;
	}

	public void setRemainDays_express_hour(Integer remainDays_express_hour) {
		this.remainDays_express_hour = remainDays_express_hour;
	}

	public Integer getRemainDays_express_fen() {
		return remainDays_express_fen;
	}

	public void setRemainDays_express_fen(Integer remainDays_express_fen) {
		this.remainDays_express_fen = remainDays_express_fen;
	}

	public Integer getRemainDays_data_hour() {
		return remainDays_data_hour;
	}

	public void setRemainDays_data_hour(Integer remainDays_data_hour) {
		this.remainDays_data_hour = remainDays_data_hour;
	}

	public Integer getRemainDays_data_fen() {
		return remainDays_data_fen;
	}

	public void setRemainDays_data_fen(Integer remainDays_data_fen) {
		this.remainDays_data_fen = remainDays_data_fen;
	}

	public Date getPayableDate() {
		return payableDate;
	}

	public void setPayableDate(Date payableDate) {
		this.payableDate = payableDate;
	}

    @Transient
    public Integer getIsT1() {
    	Set<ActivityGroup> groupSet = this.getActivityGroups();
    	for (ActivityGroup group : groupSet) {
    		if (1 == group.getIsT1()) {
    			this.isT1 = 1;
    			break;
    		} else {
    			this.isT1 = 0;
    		}
    	}
        return this.isT1;
    }
    
    @Column(name="touristLineId")
	public Long getTouristLineId() {
		return touristLineId;
	}

	public void setTouristLineId(Long touristLineId) {
		this.touristLineId = touristLineId;
	}

	@Transient
	public TouristLine getTouristLine() {
		return touristLine;
	}

	public void setTouristLine(TouristLine touristLine) {
		this.touristLine = touristLine;
	}

	@Transient
	public List<ActivityGroup> getActivityGroupList(){
		return activityGroupList;
	}
	public void setActivityGroupList(List<ActivityGroup> activityGroupList){
		this.activityGroupList = activityGroupList;
	}
}
