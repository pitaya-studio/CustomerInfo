package com.trekiz.admin.modules.eprice.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.Indexed;

/**
 * 接待社询价内容
 *  @author lihua.xu
 */
@Entity
@Table(name = "estimate_price_admit_requirements")
@DynamicInsert @DynamicUpdate
public class EstimatePriceAdmitRequirements implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	//接待社询价内容 团队类型 start
	/**
	 * 团队类型：单独成团 1
	 */
	public static final int TRAVEL_TEAM_TYPE_ALONE = 1;
	
	/**
	 * 团队类型：参加拼团 2
	 */
	public static final int TRAVEL_TEAM_TYPE_SPELL_GROUP = 2;
	//接待社询价内容 团队类型 end
	
	
	//接待社询价内容数据状态start
	
	/**
	 * 接待社询价内容数据状态:正常 1
	 */
	public static final int STATUS_NORMAL = 1;
	
	/**
	 * 接待社询价内容数据状态:删除  0
	 */
	public static final int STATUS_DEL = 0;
	
	/**
	 *数据状态:草稿  -1
	 */
	public static final int STATUS_DRAFT = -1;
	//接待社询价内容数据状态end

	
	
	
	/**
	 * ID 主键
	 */
	private Long id;
	
	/**
	 * 父级——询价项目id
	 */
	private Long pid;
	
	/**
	 * 父级——询价记录id
	 */
	private Long rid;
	
	/**
	 * 预计出团日期
	 */
	private Date dgroupOutDate;
	
	/**
	 * 出境口岸id
	 */
	private Long outAreaId;
	
	/**
	 * 冗余出境口岸名字
	 */
	private String outAreaName;
	
	/**
	 * 状态
	 * 1 正常
	 * 0 删除
	 */
	private Integer status;
	
	/**
	 * 线路国家id
	 *
	 */
	private String travelCountryId;
	
	/**
	 * 线路国家名字
	 * 
	 */
	private String travelCountry;
	
	/**
	 * 境外停留白天天数
	 */
	private Integer outsideDaySum;
	
	/**
	 * 境外停留夜晚天数
	 */
	private Integer outsideNightSum;
	
	/**
	 * 旅游团队类型
	 * 0 其他 1 单独团队  2 参加拼团
	 */
	private Integer travelTeamType;
	
	/**
	 * 旅游团队类型名称
	 * 主要是旅游团队类型是其它时，本字段意义比较大
	 */
	private String travelTeamTypeName;
	
	/**
	 * 旅游要求
	 * json： {"hotelType":{"type":1,"title":"三星","name":"酒店类型","info":"说明"},"hotelPosition":{"type":1,"title":"三星","name":"酒店类型"}……,“otherRequirements”:[]}
	 * travelTeamType:团队类型 0 其他，1 单独成团，2 参加拼团
	 * visaType:护照种类, 0其他，1 因公护照，2 因公护照
	 * hotelType : 酒店类型  0 其他， 1 三星，  2 四星， 3 五星
	 * hotelPosition:酒店位置  0 其他， 1  郊区，2 市郊，3 市区，4 市中心
	 * roomType:房间要求 0 其他，1 单人间，2 双人间，3 三人间
	 * carType:用车要求 0 其他，1 “9座车”，2 中巴，3 大车 {“type”:3,"title":"大车","sum":18} (type为3时，sum属性起作用)
	 * guideType:导游要求 0 其他，1 司机兼导游，2 一司一导，
	 * leaderType:领队要求 0 其他，1 需要领队，2 不需要领队
	 * attractionType:景点要求 0 其他，1 门票含讲解，2 其他门票
	 * publicWordType:公务要求 0 其他，1 无公务活动，2 有公务活动  当type=2时，info 
	 * breakfastType:早餐要求 0 其他， 1 欧陆自助，2 美式自助
	 * dinnerType:正餐要求 0 其他，1 5菜一汤，特色餐
	 * visaNeedType:签证要求， 1 要求办理签证，2 不要办理签证，descn 签证描述
	 * otherRequirements:其他要求，列表，value:[{"name":"新增要求title","value":"新增要求value"}]
	 * epriceRequirements:询价要求:列表，value:[{"value":"新增询价要求value"}]
	 */
	private String travelRequirements;
	
	/**
	 * 销售行程文档
	 */
	private EstimatePriceFile salerTripFile;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 修改时间
	 */
	private Date modifyTime;
	
	/**
	 * 备注
	 */
	private String remark;

	// Constructors

	/** default constructor */
	public EstimatePriceAdmitRequirements() {
	}

	/** full constructor */
	public EstimatePriceAdmitRequirements(Long id, Long pid, Long rid,
			Date dgroupOutDate, Long outAreaId, String outAreaName,
			Integer status, String travelCountryId, String travelCountry,
			Integer outsideDaySum, Integer outsideNightSum,
			Integer travelTeamType, String travelTeamTypeName,
			String travelRequirements, Date createTime, Date modifyTime,
			String remark) {
		//this.id = id;
		this.pid = pid;
		this.rid = rid;
		this.dgroupOutDate = dgroupOutDate;
		this.outAreaId = outAreaId;
		this.outAreaName = outAreaName;
		this.status = status;
		this.travelCountryId = travelCountryId;
		this.travelCountry = travelCountry;
		this.outsideDaySum = outsideDaySum;
		this.outsideNightSum = outsideNightSum;
		this.travelTeamType = travelTeamType;
		this.travelTeamTypeName = travelTeamTypeName;
		this.travelRequirements = travelRequirements;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.remark = remark;
	}

	public EstimatePriceAdmitRequirements(EstimatePriceAdmitRequirements est) {
		//this.id = id;
		this.pid = est.pid;
		this.rid = est.rid;
		this.dgroupOutDate = est.dgroupOutDate;
		this.outAreaId = est.outAreaId;
		this.outAreaName = est.outAreaName;
		this.status = est.status;
		this.travelCountryId = est.travelCountryId;
		this.travelCountry = est.travelCountry;
		this.outsideDaySum = est.outsideDaySum;
		this.outsideNightSum = est.outsideNightSum;
		this.travelTeamType = est.travelTeamType;
		this.travelTeamTypeName = est.travelTeamTypeName;
		this.travelRequirements = est.travelRequirements;
		this.createTime = est.createTime;
		this.modifyTime = est.modifyTime;
		this.remark = est.remark;
	}
	// Property accessors
	//@GenericGenerator(name = "generator", strategy = "increment")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "pid")
	public Long getPid() {
		return this.pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	@Column(name = "rid")
	public Long getRid() {
		return this.rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "dgroup_out_date")//, length = 10
	public Date getDgroupOutDate() {
		return this.dgroupOutDate;
	}

	public void setDgroupOutDate(Date dgroupOutDate) {
		this.dgroupOutDate = dgroupOutDate;
	}

	@Column(name = "out_area_id")
	public Long getOutAreaId() {
		return this.outAreaId;
	}

	public void setOutAreaId(Long outAreaId) {
		this.outAreaId = outAreaId;
	}

	@Column(name = "out_area_name")//, length = 32
	public String getOutAreaName() {
		return this.outAreaName;
	}

	public void setOutAreaName(String outAreaName) {
		this.outAreaName = outAreaName;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	@Column(name = "travel_country_id")
	public String getTravelCountryId() {
		return travelCountryId;
	}

	public void setTravelCountryId(String travelCountryId) {
		this.travelCountryId = travelCountryId;
	}

	@Column(name = "travel_country")//, length = 32
	public String getTravelCountry() {
		return this.travelCountry;
	}

	public void setTravelCountry(String travelCountry) {
		this.travelCountry = travelCountry;
	}

	@Column(name = "outside_day_sum")
	public Integer getOutsideDaySum() {
		return this.outsideDaySum;
	}

	public void setOutsideDaySum(Integer outsideDaySum) {
		this.outsideDaySum = outsideDaySum;
	}

	@Column(name = "outside_night_sum")
	public Integer getOutsideNightSum() {
		return this.outsideNightSum;
	}

	public void setOutsideNightSum(Integer outsideNightSum) {
		this.outsideNightSum = outsideNightSum;
	}

	@Column(name = "travel_team_type")
	public Integer getTravelTeamType() {
		return this.travelTeamType;
	}

	public void setTravelTeamType(Integer travelTeamType) {
		this.travelTeamType = travelTeamType;
	}

	@Column(name = "travel_team_type_name")//, length = 64
	public String getTravelTeamTypeName() {
		return this.travelTeamTypeName;
	}

	public void setTravelTeamTypeName(String travelTeamTypeName) {
		this.travelTeamTypeName = travelTeamTypeName;
	}

	@Column(name = "travel_requirements")//, length = 16777215
	public String getTravelRequirements() {
		return this.travelRequirements;
	}

	public void setTravelRequirements(String travelRequirements) {
		this.travelRequirements = travelRequirements;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "saler_trip_file_id", referencedColumnName = "id")
	@Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@NotFound(action = NotFoundAction.IGNORE)
	public EstimatePriceFile getSalerTripFile() {
		return salerTripFile;
	}

	public void setSalerTripFile(EstimatePriceFile salerTripFile) {
		this.salerTripFile = salerTripFile;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time")//, length = 10
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_time")//, length = 10
	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "remark")//, length = 256
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}