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
 * 询价项目
 * 询价项目，由销售发起
 *  @author lihua.xu
 */
@Entity
@Table(name = "estimate_price_project")
@DynamicInsert @DynamicUpdate
public class EstimatePriceProject implements java.io.Serializable {

	

	private static final long serialVersionUID = 1L;
	
	//询价项目的询价状态start
	/**
	 * 询价项目询价状态：已经取消 0
	 */
	public static final int ESTIMATE_STATUS_CANCEL = 0;
	
	/**
	 * 询价项目询价状态：待报价 1
	 */
	public static final int ESTIMATE_STATUS_WAITING = 1;
	
	/**
	 * 询价项目询价状态：已经报价 2
	 */
	public static final int ESTIMATE_STATUS_GIVEN = 2;
	
	/**
	 * 询价项目询价状态：确定报价 3
	 */
	public static final int ESTIMATE_STATUS_SURE = 3;
	
	/**
	 * 询价项目询价状态：发布产品 4
	 */
	public static final int ESTIMATE_STATUS_PUBLISH = 4;
	
	/**
	 * 询价项目询价状态：生成订单 5
	 */
	public static final int ESTIMATE_STATUS_ORDER_PUBLISH = 5;
	
	/**
	 * 询价项目询价状态：（计调主管专用）等待计调主管分配 6
	 */
	public static final int ESTIMATE_STATUS_FORMANAGER = 6;
	
	//询价项目的询价状态end
	
	//询价项目数据状态start
	
	/**
	 * 询价项目数据状态:正常 1
	 */
	public static final int STATUS_NORMAL = 1;
	
	/**
	 * 询价项目数据状态:删除  0
	 */
	public static final int STATUS_DEL = 0;
	
	/**
	 *数据状态:草稿  -1
	 */
	public static final int STATUS_DRAFT = -1;
	//询价项目数据状态end
	
	
	//询价项目类型start
	
	/**
	 * 询价项目类型:单团询价  1
	 */
	public static final int TYPE_ALONE = 1;
	/**
	 * 询价项目类型:游学询价  3
	 */
	public static final int TYPE_YX = 3;
	
	/**
	 * 询价项目类型:大客户   4
	 */
	public static final int TYPE_DKH = 4;
	
	/**
	 * 询价项目类型:自由行  5
	 */
	public static final int TYPE_ZYX = 5;
	
	
	/**
	 * 询价项目类型:机票询价  7
	 */
	public static final int TYPE_FLIGHT = 7;
	//询价项目类型end
	
	
	
	/**
	 * id 主键
	 */
	private Long id;
	
	/**
	 * 询价项目title
	 */
	private String title;
	
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
	 * 最新的询价记录id
	 */
	private Long lastRecordId;
	
	/**
	 * 最新的询价基本信息id
	 */
	//private Long lastBaseInfoId;
	
	/**
	 * 最新的询价基本信息对象
	 */
	private EstimatePriceBaseInfo lastBaseInfo;
	
	/**
	 * 最新接待社询价内容id
	 */
	//private Long lastAdmitRequirementsId;
	
	/**
	 * 最新接待社询价内容对象
	 */
	private EstimatePriceAdmitRequirements lastAdmitRequirements;
	
	/**
	 * 最新交通询价内容id
	 */
	//private Long lastTrafficRequirementsId;
	
	/**
	 * 最新交通询价内容对象
	 */
	private EstimatePriceTrafficRequirements lastTrafficRequirements;
	
	/**
	 * 询价项目的询价状态
	 * 0 已经取消，1 待报价，2 已报价，3 确定报价，4 发布产品, 5 已生成订单，6 待分配
	 */
	private Integer estimateStatus;
	
	/**
	 * 询价项目数据状态
	 * 1 正常，0 被删除, -1 草稿
	 */
	private Integer status;
	
	private Integer estimatePriceSum;
	
	/**
	 * 询价项目类型
	 * 1 单团询价  7 机票询价
	 */
	private Integer type;
	
	/**
	 * 最新询价时间
	 */
	private Date lastEstimatePriceTime;
	
	/**
	 * 最新计调报价时间
	 */
	private Date lastOperatorGivenTime;
	
	/**
	 * 最新生产产品时间
	 */
	private Date lastCreateProductTime;
	
	/**
	 * 最新取消询价时间
	 */
	private Date lastCancelTime;
	
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

	// Constructors

	/** default constructor */
	public EstimatePriceProject() {
	}

	/** full constructor */
	public EstimatePriceProject(String title, Long userId, String userName,
			Long companyId, String companyName, Long lastRecordId,Long lastBaseInfoId,
			Long lastAdmitRequirementsId,Long lastTrafficRequirementsId, Integer estimateStatus,Integer type,Integer estimatePriceSum,
			Integer status, Date lastEstimatePriceTime, Date lastOperatorGivenTime,
			Date lastCreateProductTime, Date lastCancelTime, Date createTime,
			Date modifyTime, String remark) {
		this.title = title;
		this.userId = userId;
		this.userName = userName;
		this.companyId = companyId;
		this.companyName = companyName;
		this.lastRecordId = lastRecordId;
		/*this.lastBaseInfoId = lastBaseInfoId;
		this.lastAdmitRequirementsId = lastAdmitRequirementsId;
		this.lastTrafficRequirementsId = lastTrafficRequirementsId;*/
		this.estimateStatus = estimateStatus;
		this.type = type;
		this.estimatePriceSum = estimatePriceSum;
		this.status = status;
		this.lastEstimatePriceTime = lastEstimatePriceTime;
		this.lastOperatorGivenTime = lastOperatorGivenTime;
		this.lastCreateProductTime = lastCreateProductTime;
		this.lastCancelTime = lastCancelTime;
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

	@Column(name = "title")//, length = 128
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "user_id")
	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "user_name", length = 64)
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

	@Column(name = "company_name", length = 128)
	public String getCompanyName() {
		return this.companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	
	@Column(name = "last_record_id")
	public Long getLastRecordId() {
		return lastRecordId;
	}

	public void setLastRecordId(Long lastRecordId) {
		this.lastRecordId = lastRecordId;
	}

	/*@Column(name = "last_base_info_id")
	public Long getLastBaseInfoId() {
		return this.lastBaseInfoId;
	}

	public void setLastBaseInfoId(Long lastBaseInfoId) {
		this.lastBaseInfoId = lastBaseInfoId;
	}*/
	
	//@ContainedIn
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "last_base_info_id", referencedColumnName = "id")
	@Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@NotFound(action = NotFoundAction.IGNORE)
	public EstimatePriceBaseInfo getLastBaseInfo() {
		return lastBaseInfo;
	}

	public void setLastBaseInfo(EstimatePriceBaseInfo lastBaseInfo) {
		this.lastBaseInfo = lastBaseInfo;
	}

	/*@Column(name = "last_admit_requirements_id")
	public Long getLastAdmitRequirementsId() {
		return this.lastAdmitRequirementsId;
	}
	

	public void setLastAdmitRequirementsId(Long lastAdmitRequirementsId) {
		this.lastAdmitRequirementsId = lastAdmitRequirementsId;
	}*/
	
	
	//@ContainedIn
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "last_admit_requirements_id", referencedColumnName = "id")
	@Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@NotFound(action = NotFoundAction.IGNORE)
	public EstimatePriceAdmitRequirements getLastAdmitRequirements() {
		return lastAdmitRequirements;
	}

	public void setLastAdmitRequirements(
			EstimatePriceAdmitRequirements lastAdmitRequirements) {
		this.lastAdmitRequirements = lastAdmitRequirements;
	}
	
	
	/*@Column(name = "last_traffic_requirements_id")
	public Long getLastTrafficRequirementsId() {
		return this.lastTrafficRequirementsId;
	}
	

	public void setLastTrafficRequirementsId(Long lastTrafficRequirementsId) {
		this.lastTrafficRequirementsId = lastTrafficRequirementsId;
	}*/
	
	
	//@ContainedIn
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "last_traffic_requirements_id", referencedColumnName = "id")
	@Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@NotFound(action = NotFoundAction.IGNORE)
	public EstimatePriceTrafficRequirements getLastTrafficRequirements() {
		return lastTrafficRequirements;
	}

	public void setLastTrafficRequirements(
			EstimatePriceTrafficRequirements lastTrafficRequirements) {
		this.lastTrafficRequirements = lastTrafficRequirements;
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
	
	
	@Column(name = "estimate_price_sum")
	public Integer getEstimatePriceSum() {
		return estimatePriceSum;
	}

	public void setEstimatePriceSum(Integer estimatePriceSum) {
		this.estimatePriceSum = estimatePriceSum;
	}

	@Column(name = "type")
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_estimate_price_time")//, length = 10
	public Date getLastEstimatePriceTime() {
		return this.lastEstimatePriceTime;
	}

	public void setLastEstimatePriceTime(Date lastEstimatePriceTime) {
		this.lastEstimatePriceTime = lastEstimatePriceTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_operator_given_time")//, length = 10
	public Date getLastOperatorGivenTime() {
		return this.lastOperatorGivenTime;
	}

	public void setLastOperatorGivenTime(Date lastOperatorGivenTime) {
		this.lastOperatorGivenTime = lastOperatorGivenTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_create_product_time")//, length = 10
	public Date getLastCreateProductTime() {
		return this.lastCreateProductTime;
	}

	public void setLastCreateProductTime(Date lastCreateProductTime) {
		this.lastCreateProductTime = lastCreateProductTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "last_cancel_time")//, length = 10
	public Date getLastCancelTime() {
		return this.lastCancelTime;
	}

	public void setLastCancelTime(Date lastCancelTime) {
		this.lastCancelTime = lastCancelTime;
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