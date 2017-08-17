package com.trekiz.admin.modules.eprice.entity;

import java.math.BigDecimal;
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
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Indexed;

/**
 * 询价记录，由销售发起
 * @lihua.xu
 */
@Entity
@Table(name = "estimate_price_record")
@DynamicInsert @DynamicUpdate
public class EstimatePriceRecord implements java.io.Serializable {

	

	private static final long serialVersionUID = 1L;
	
	
	//询价记录的询价状态start
	/**
	 * 询价记录的询价状态：已经取消 0
	 */
	public static final int ESTIMATE_STATUS_CANCEL = 0;
	
	/**
	 * 询价记录的询价状态：待报价 1
	 */
	public static final int ESTIMATE_STATUS_WAITING = 1;
	
	/**
	 * 询价记录的询价状态：已经报价 2
	 */
	public static final int ESTIMATE_STATUS_GIVEN = 2;
	
	/**
	 * 询价记录的询价状态：确定报价 3
	 */
	public static final int ESTIMATE_STATUS_SURE = 3;
	
	/**
	 * 询价记录的询价状态：发布产品 4
	 */
	public static final int ESTIMATE_STATUS_PUBLISH = 4;
	
	/**
	 * 询价记录的询价状态：生成订单 5
	 */
	public static final int ESTIMATE_STATUS_ORDER_PUBLISH = 5;
	
	/**
	 * 询价项目询价状态：（计调主管专用）等待计调主管分配 6
	 */
	public static final int ESTIMATE_STATUS_FORMANAGER = 6;
	
	//询价记录的询价状态end
		
	
	//询价记录数据状态start
	
	/**
	 * 询价记录数据状态:正常 1
	 */
	public static final int STATUS_NORMAL = 1;
	
	/**
	 * 询价记录数据状态:删除  0
	 */
	public static final int STATUS_DEL = 0;
	
	/**
	 *数据状态:草稿  -1
	 */
	public static final int STATUS_DRAFT = -1;
	//询价记录数据状态end
	
	//询价记录类型start
	
	/**
	 * 询价记录类型:单团询价  1
	 */
	public static final int TYPE_ALONE = 1;
	
	/**
	 * 询价记录类型:机票询价  7
	 */
	public static final int TYPE_FLIGHT = 7;
	//询价记录类型end
	
	//询价记录是否申请机票start
	/**
	 * 询价记录是否申请机票:不申请 0
	 */
	public static final int IS_APP_FLIGHT_NO = 0;
	
	/**
	 * 询价记录是否申请机票:申请 1
	 */
	public static final int IS_APP_FLIGHT_YES = 1;	
	//询价记录是否申请机票end
	
	
	/**
	 * ID 主键ID
	 */
	private Long id;
	
	/**
	 * 询价记录title
	 */
	private String title;
	
	/**
	 * 父级——询价项目id
	 */
	private Long pid;
	
	/**
	 * 询价项目创建人id
	 */
	private Long userId;
	
	/**
	 * 询价项目创建name
	 */
	private String userName;
	
	
	/**
	 * 询价项目归属的公司id
	 */
	private Long companyId;
	
	/**
	 * 询价项目归属的公司name
	 */
	private String companyName;
	
	
	/**
	 * 询价基本信息id
	 */
	//private Long baseInfoId;
	
	/**
	 * 接待社询价内容id
	 */
	//private Long admitRequirementsId;
	
	/**
	 * 交通询价内容id
	 */
	//private Long trafficRequirementsId;
	
	/**
	 * 询价基本信息对象
	 */
	private EstimatePriceBaseInfo baseInfo;
	
	/**
	 * 接待社询价内容对象
	 */
	private EstimatePriceAdmitRequirements admitRequirements;
	
	/**
	 * 交通询价内容对象
	 */
	private EstimatePriceTrafficRequirements trafficRequirements;
	
	/**
	 * 询价项目的询价状态
	 * 0 已经取消，1 待报价，2 已报价，3 确定报价，4 发布产品
	 * 注：只记录项目记录生命周期内的询价项目状态
	 */
	private Integer estimateStatus;
	
	/**
	 * 询价记录数据状态
	 * -1 草稿 1 正常，0 被删除
	 */
	private Integer status;
	
	/**
	 * 询价记录类型
	 * 1 单团询价，3游学询价 4,大客户询价 5 自由行询价 ，7  机票内容
	 */
	private Integer type;
	
	/**
	 * 是否申请机票
	 * 0 不申请  1 申请
	 */
	private Integer isAppFlight;
	
	/**
	 * 接待计调成员json
	 * jsonArray
	 * [{"userId":12323,"userName":"aaa"},{"userId":2222,"userName":"aaa2"}]
	 */
	private String aoperatorUserJson;
	
	/**
	 * 票务计调成员json
	 * jsonArray
	 * [{"userId":12323,"userName":"aaa"},{"userId":2222,"userName":"aaa2"}]
	 */
	private String toperatorUserJson;
	
	/**
	 * 最新接待计调报价时间
	 */
	private Date lastAoperatorPriceTime;
	
	
	/**
	 * 最新票务计调报价时间
	 */
	private Date lastToperatorPriceTime;
	
	/**
	 * 被采纳的接待回复
	 */
	private EstimatePricerReply acceptAoperatorReply;
	
	/**
	 * 被销售选中的票务报价回复
	 */
	private EstimatePricerReply acceptToperatorReply;
	
	/**
	 * 计调报价：被采纳的接待计调报价+被采纳的票务报价
	 */
	private BigDecimal operatorPrice;
	
	/**
	 * 地接计调报价
	 */
	private BigDecimal operatorAopPrice;
	
	/**
	 * 机票计调报价
	 */
	private BigDecimal operatorTopPrice;
	
	/**
	 * 销售外报价
	 */
	private BigDecimal outPrice;
	
	/**
	 * 采纳时间
	 */
	private Date acceptTime;
	
	/**
	 * 外报价时间
	 */
	private Date outPriceTime;
	
	/**
	 * 取消时间
	 */
	private Date cancelTime;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 更新时间
	 */
	private Date modifyTime;
	
	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 出发城市，抵达城市，机票出发时间,这三项 ，仅为页面显示而存在
	 */
	
	private String startCity;								// 出发城市
	private String endCity;							// 抵达城市
	private Date lastToperatorStartOutTime; //机票出发时间
	
	
	
	// Constructors
	@Column(name = "start_city")
	public String getStartCity() {
		return startCity;
	}

	public void setStartCity(String startCity) {
		this.startCity = startCity;
	}

	@Column(name = "end_city")
	public String getEndCity() {
		return endCity;
	}

	public void setEndCity(String endCity) {
		this.endCity = endCity;
	}

	@Column(name = "last_toperator_start_out_time")	
	public Date getLastToperatorStartOutTime() {
		return lastToperatorStartOutTime;
	}

	public void setLastToperatorStartOutTime(Date lastToperatorStartOutTime) {
		this.lastToperatorStartOutTime = lastToperatorStartOutTime;
	}

	/** default constructor */
	public EstimatePriceRecord() {
	}

	/** full constructor */
	public EstimatePriceRecord(String title, Long pid, Long userId,
			String userName, Long companyId, String companyName,
			Long baseInfoId, Long admitRequirementsId,
			Long trafficRequirementsId, Integer estimateStatus, Integer status,
			Integer type, Integer isAppFlight, String aoperatorUserJson,
			String toperatorUserJson, Date lastAoperatorPriceTime, 
			Date lastToperatorPriceTime, BigDecimal operatorPrice, BigDecimal operatorAopPrice,BigDecimal operatorTopPrice,
			BigDecimal outPrice, Date acceptTime,
			Date outPriceTime, Date cancelTime, Date createTime,
			Date modifyTime, String remark,Date lastToperatorStartOutTime,
			String startCity,String endCity) {
		this.title = title;
		this.pid = pid;
		this.userId = userId;
		this.userName = userName;
		this.companyId = companyId;
		this.companyName = companyName;
		/*this.baseInfoId = baseInfoId;
		this.admitRequirementsId = admitRequirementsId;
		this.trafficRequirementsId = trafficRequirementsId;*/
		this.estimateStatus = estimateStatus;
		this.status = status;
		this.type = type;
		this.isAppFlight = isAppFlight;
		this.aoperatorUserJson = aoperatorUserJson;
		this.toperatorUserJson = toperatorUserJson;
		this.lastAoperatorPriceTime = lastAoperatorPriceTime;
		this.lastToperatorPriceTime = lastToperatorPriceTime;
		//this.acceptAoperatorReplyId = acceptAoperatorReplyId;
		//this.acceptToperatorReplyId = acceptToperatorReplyId;
		this.operatorPrice = operatorPrice;
		this.operatorTopPrice = operatorTopPrice;
		this.operatorAopPrice = operatorAopPrice;
		this.outPrice = outPrice;
		this.acceptTime = acceptTime;
		this.outPriceTime = outPriceTime;
		this.cancelTime = cancelTime;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.remark = remark;
		this.lastToperatorStartOutTime = lastToperatorStartOutTime;
		this.startCity = startCity;
		this.endCity = endCity;
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

	@Column(name = "title")//, length = 128
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "pid")
	public Long getPid() {
		return this.pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	@Column(name = "user_id")
	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "user_name")//, length = 64
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "company_id")
	public Long getCompanyId() {
		return this.companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@Column(name = "company_name")//, length = 128
	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	
	
	/*@Column(name = "base_info_id")
	public Long getBaseInfoId() {
		return baseInfoId;
	}

	public void setBaseInfoId(Long baseInfoId) {
		this.baseInfoId = baseInfoId;
	}

	@Column(name = "admit_requirements_id")
	public Long getAdmitRequirementsId() {
		return admitRequirementsId;
	}

	public void setAdmitRequirementsId(Long admitRequirementsId) {
		this.admitRequirementsId = admitRequirementsId;
	}

	@Column(name = "traffic_requirements_id")
	public Long getTrafficRequirementsId() {
		return trafficRequirementsId;
	}

	public void setTrafficRequirementsId(Long trafficRequirementsId) {
		this.trafficRequirementsId = trafficRequirementsId;
	}*/
	
	@ContainedIn
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "base_info_id", referencedColumnName = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	public EstimatePriceBaseInfo getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(EstimatePriceBaseInfo baseInfo) {
		this.baseInfo = baseInfo;
	}
	
	//@ContainedIn
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "admit_requirements_id", referencedColumnName = "id")
	@Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@NotFound(action = NotFoundAction.IGNORE)
	public EstimatePriceAdmitRequirements getAdmitRequirements() {
		return admitRequirements;
	}

	public void setAdmitRequirements(EstimatePriceAdmitRequirements admitRequirements) {
		this.admitRequirements = admitRequirements;
	}
	
	//@ContainedIn
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "traffic_requirements_id", referencedColumnName = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	public EstimatePriceTrafficRequirements getTrafficRequirements() {
		return trafficRequirements;
	}

	public void setTrafficRequirements(EstimatePriceTrafficRequirements trafficRequirements) {
		this.trafficRequirements = trafficRequirements;
	}

	@Column(name = "estimate_status")
	public Integer getEstimateStatus() {
		return this.estimateStatus;
	}

	public void setEstimateStatus(Integer estimateStatus) {
		this.estimateStatus = estimateStatus;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = "type")
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	@Column(name = "is_app_flight")
	public Integer getIsAppFlight() {
		return this.isAppFlight;
	}

	public void setIsAppFlight(Integer isAppFlight) {
		this.isAppFlight = isAppFlight;
	}

	@Column(name = "aoperator_user_json")//, length = 2048
	public String getAoperatorUserJson() {
		return this.aoperatorUserJson;
	}

	public void setAoperatorUserJson(String aoperatorUserJson) {
		this.aoperatorUserJson = aoperatorUserJson;
	}

	@Column(name = "toperator_user_json")//, length = 2048
	public String getToperatorUserJson() {
		return this.toperatorUserJson;
	}

	public void setToperatorUserJson(String toperatorUserJson) {
		this.toperatorUserJson = toperatorUserJson;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_aoperator_price_time")//, length = 10
	public Date getLastAoperatorPriceTime() {
		return this.lastAoperatorPriceTime;
	}

	public void setLastAoperatorPriceTime(Date lastAoperatorPriceTime) {
		this.lastAoperatorPriceTime = lastAoperatorPriceTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_toperator_price_time")//, length = 10
	public Date getLastToperatorPriceTime() {
		return this.lastToperatorPriceTime;
	}

	public void setLastToperatorPriceTime(Date lastToperatorPriceTime) {
		this.lastToperatorPriceTime = lastToperatorPriceTime;
	}

	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accept_aoperator_reply_id", referencedColumnName = "id")
	@Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@NotFound(action = NotFoundAction.IGNORE)
	public EstimatePricerReply getAcceptAoperatorReply() {
		return acceptAoperatorReply;
	}

	public void setAcceptAoperatorReply(EstimatePricerReply acceptAoperatorReply) {
		this.acceptAoperatorReply = acceptAoperatorReply;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "accept_toperator_reply_id", referencedColumnName = "id")
	@Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@NotFound(action = NotFoundAction.IGNORE)
	public EstimatePricerReply getAcceptToperatorReply() {
		return acceptToperatorReply;
	}

	public void setAcceptToperatorReply(EstimatePricerReply acceptToperatorReply) {
		this.acceptToperatorReply = acceptToperatorReply;
	}

	@Column(name = "operator_price")
	public BigDecimal getOperatorPrice() {
		return operatorPrice;
	}

	public void setOperatorPrice(BigDecimal operatorPrice) {
		this.operatorPrice = operatorPrice;
	}
	
	@Column(name = "operator_top_price")
	public BigDecimal getOperatorTopPrice() {
		return operatorTopPrice;
	}

	public void setOperatorTopPrice(BigDecimal operatorTopPrice) {
		this.operatorTopPrice = operatorTopPrice;
	}
	
	@Column(name = "operator_aop_price")
	public BigDecimal getOperatorAopPrice() {
		return operatorAopPrice;
	}

	public void setOperatorAopPrice(BigDecimal operatorAopPrice) {
		this.operatorAopPrice = operatorAopPrice;
	}

	@Column(name = "out_price")//, precision = 12, scale = 0
	public BigDecimal getOutPrice() {
		return this.outPrice;
	}

	public void setOutPrice(BigDecimal outPrice) {
		this.outPrice = outPrice;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "accept_time")//, length = 10
	public Date getAcceptTime() {
		return this.acceptTime;
	}

	public void setAcceptTime(Date acceptTime) {
		this.acceptTime = acceptTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "out_price_time")//, length = 10
	public Date getOutPriceTime() {
		return this.outPriceTime;
	}

	public void setOutPriceTime(Date outPriceTime) {
		this.outPriceTime = outPriceTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "cancel_time")//, length = 10
	public Date getCancelTime() {
		return this.cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
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