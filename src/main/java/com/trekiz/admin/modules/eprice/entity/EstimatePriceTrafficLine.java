package com.trekiz.admin.modules.eprice.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.search.annotations.Indexed;

/**
 * 交通路线记录
 * 多段机票路线里的一条路线
 * @lihua.xu
 */
@Entity
@Table(name = "estimate_price_traffic_line")
@DynamicInsert @DynamicUpdate
public class EstimatePriceTrafficLine implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//交通路线地域类型 start
	/**
	 *交通路线地域类型:内陆  1
	 */
	public static final int AREA_TYPE_INLAND = 1;
	
	/**
	 *交通路线地域类型:国际 2
	 */
	public static final int AREA_TYPE_INTERNAT = 2;
	
	/**
	 * 交通路线地域类型:内陆+国际 3
	 */
	public static final int AREA_TYPE_INLAND_INTERNAT = 3;
	//交通路线地域类型 end
	
	
	//交通路线记录数据状态start
	
	/**
	 * 交通路线记录数据状态:正常 1
	 */
	public static final int STATUS_NORMAL = 1;
	
	/**
	 *交通路线记录数据状态:删除  0
	 */
	public static final int STATUS_DEL = 0;
	//交通路线记录数据状态end
	
	//交通路线记录舱位等级start
	/**
	 * 舱位等级:头等舱 1
	 */
	public static final int AIRCRAFT_SPACE_LEVEL_FIRST = 1;
	
	/**
	 * 舱位等级:公务舱  2
	 */
	public static final int AIRCRAFT_SPACE_LEVEL_SECOND = 2;
	
	/**
	 * 舱位等级:经济舱  3
	 */
	public static final int AIRCRAFT_SPACE_LEVEL_THIRD = 3;
	//交通路线记录舱位等级end
	
	
	//交通路线出发时刻 start
	
	/**
	 * 交通路线出发时刻:早  1
	 */
	public static final int START_TIME_TYPE_M = 1;
	
	/**
	 *交通路线出发时刻:中 2
	 */
	public static final int START_TIME_TYPE_N = 2;
	
	/**
	 *交通路线出发时刻:晚 3
	 */
	public static final int START_TIME_TYPE_NIGNT = 3;
	//交通路线出发时刻 end
	
	
	/**
	 * ID 主键ID
	 */
	private Long id;
	
	/**
	 * 父级——机票询价内容id
	 */
	private Long pfid;
	
	/**
	 * 编号
	 * 标识第几段
	 */
	private Integer no;
	
	/**
	 * 出发城市id
	 */
	private Long startCityId;
	
	/**
	 * 出发城市name
	 */
	private String startCityName;
	
	/**
	 * 终点城市id
	 */
	private Long endCityId;
	
	/**
	 * 终点城市name
	 */
	private String endCityName;
	
	/**
	 * 出发日期（不含时分秒，只标识日期）
	 */
	private Date startDate;
	
	/**
	 * 出发时间区间开始时间（不含日期，只标识时间）
	 */
	private String startTime1;
	
	/**
	 * 出发时间区间结束时间（不含日期，只标识时间）
	 */
	private String startTime2;
	
	/**
	 * 出发时刻：1 早 2中 3 晚
	 */
	private Integer startTimeType;
	
	/**
	 * 地域类型
	 * 1 内陆，2 国际，3 内陆+国际
	 */
	private Integer areaType;
	
	/**
     * 交通方式类型
     * 1、汽车；2 火车；3 航班；4 混合交通方式
     */
	private Integer trafficType;
	
	/**
	 * 交通路线类型
	 * 1 往返，2 单程，3 多段
	 */
	private Integer trafficLineType;
	
	/**
	 * 数据状态
	 * -1 草稿 1 正常，0 被删除
	 */
	private Integer status;
	
	/**
	 * 总人数
	 */
	private Integer allPersonSum;
	
	/**
	 * 成人数
	 */
	private Integer adultSum;
	
	/**
	 * 儿童数
	 */
	private Integer childSum;
	
	/**
	 * 特殊人群
	 */
	private Integer specialPersonSum;
	
	
	/**
	 * 线路总金额
	 */
	private Integer amount;
	
	/**
	 * 线路被销售选中的价格
	 */
	private Integer price;
	
	/**
	 * 舱位等级:1 头等舱；2 公务舱；3 经济舱 0不限
	 */
	private Integer aircraftSpaceLevel;
	
	/**
	 * 舱位：Y（Y舱），K（K舱）0 不限
	 */
	private String  aircraftSpace;
	
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
	public EstimatePriceTrafficLine() {
	}

	/** full constructor */
	public EstimatePriceTrafficLine(Long pfid, Integer no,
			Long startCityId, String startCityName, Long endCityId,
			String endCityName, Date startDate, String startTime1,
			String startTime2,Integer startTimeType, Integer areaType, Integer trafficType,
			Integer trafficLineType, Integer status, Integer allPersonSum,
			Integer adultSum, Integer childSum, Integer specialPersonSum,
			Integer amount, Integer price, Integer aircraftSpaceLevel,String aircraftSpace,
			Date createTime, Date modifyTime, String remark) {
		this.pfid = pfid;
		this.no = no;
		this.startCityId = startCityId;
		this.startCityName = startCityName;
		this.endCityId = endCityId;
		this.endCityName = endCityName;
		this.startDate = startDate;
		this.startTime1 = startTime1;
		this.startTime2 = startTime2;
		this.startTimeType = startTimeType;
		this.areaType = areaType;
		this.trafficType = trafficType;
		this.trafficLineType = trafficLineType;
		this.status = status;
		this.allPersonSum = allPersonSum;
		this.adultSum = adultSum;
		this.childSum = childSum;
		this.specialPersonSum = specialPersonSum;
		this.amount = amount;
		this.price = price;
		this.aircraftSpaceLevel = aircraftSpaceLevel;
		this.aircraftSpace = aircraftSpace;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.remark = remark;
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

	@Column(name = "pfid")
	public Long getPfid() {
		return this.pfid;
	}

	public void setPfid(Long pfid) {
		this.pfid = pfid;
	}

	@Column(name = "no")
	public Integer getNo() {
		return this.no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	@Column(name = "start_city_id")
	public Long getStartCityId() {
		return this.startCityId;
	}

	public void setStartCityId(Long startCityId) {
		this.startCityId = startCityId;
	}

	@Column(name = "start_city_name")//, length = 128
	public String getStartCityName() {
		return this.startCityName;
	}

	public void setStartCityName(String startCityName) {
		this.startCityName = startCityName;
	}

	@Column(name = "end_city_id")
	public Long getEndCityId() {
		return this.endCityId;
	}

	public void setEndCityId(Long endCityId) {
		this.endCityId = endCityId;
	}

	@Column(name = "end_city_name")//, length = 128
	public String getEndCityName() {
		return this.endCityName;
	}

	public void setEndCityName(String endCityName) {
		this.endCityName = endCityName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "start_date")//, length = 10
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "start_time1")//, length = 64
	public String getStartTime1() {
		return this.startTime1;
	}

	public void setStartTime1(String startTime1) {
		this.startTime1 = startTime1;
	}

	@Column(name = "start_time2")//, length = 64
	public String getStartTime2() {
		return this.startTime2;
	}

	public void setStartTime2(String startTime2) {
		this.startTime2 = startTime2;
	}
	
	
	
	@Column(name = "start_time_type")
	public Integer getStartTimeType() {
		return startTimeType;
	}

	public void setStartTimeType(Integer startTimeType) {
		this.startTimeType = startTimeType;
	}

	@Column(name = "area_type")
	public Integer getAreaType() {
		return this.areaType;
	}

	public void setAreaType(Integer areaType) {
		this.areaType = areaType;
	}

	@Column(name = "traffic_type")
	public Integer getTrafficType() {
		return this.trafficType;
	}

	public void setTrafficType(Integer trafficType) {
		this.trafficType = trafficType;
	}

	@Column(name = "traffic_line_type")
	public Integer getTrafficLineType() {
		return this.trafficLineType;
	}

	public void setTrafficLineType(Integer trafficLineType) {
		this.trafficLineType = trafficLineType;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "all_person_sum")
	public Integer getAllPersonSum() {
		return this.allPersonSum;
	}

	public void setAllPersonSum(Integer allPersonSum) {
		this.allPersonSum = allPersonSum;
	}

	@Column(name = "adult_sum")
	public Integer getAdultSum() {
		return this.adultSum;
	}

	public void setAdultSum(Integer adultSum) {
		this.adultSum = adultSum;
	}

	@Column(name = "child_sum")
	public Integer getChildSum() {
		return this.childSum;
	}

	public void setChildSum(Integer childSum) {
		this.childSum = childSum;
	}

	@Column(name = "special_person_sum")
	public Integer getSpecialPersonSum() {
		return this.specialPersonSum;
	}

	public void setSpecialPersonSum(Integer specialPersonSum) {
		this.specialPersonSum = specialPersonSum;
	}

	@Column(name = "amount")
	public Integer getAmount() {
		return this.amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	@Column(name = "price")
	public Integer getPrice() {
		return this.price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	@Column(name = "aircraft_space_level")
	public Integer getAircraftSpaceLevel() {
		return this.aircraftSpaceLevel;
	}

	public void setAircraftSpaceLevel(Integer aircraftSpaceLevel) {
		this.aircraftSpaceLevel = aircraftSpaceLevel;
	}
	
	@Column(name = "aircraft_space")
	public String getAircraftSpace() {
		return this.aircraftSpace;
	}

	public void setAircraftSpace(String aircraftSpace) {
		this.aircraftSpace = aircraftSpace;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_time", length = 10)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modify_time", length = 10)
	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "remark", length = 256)
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	

}