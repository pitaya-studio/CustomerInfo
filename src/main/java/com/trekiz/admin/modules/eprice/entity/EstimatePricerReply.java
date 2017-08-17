package com.trekiz.admin.modules.eprice.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Transient;

/**
 * EstimatePricerReply entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "estimate_pricer_reply")
@DynamicInsert @DynamicUpdate
public class EstimatePricerReply implements java.io.Serializable {

	
	private static final long serialVersionUID = 1L;
	
	//询价回复数据状态start
	
	/**
	 * 询价记录数据状态:删除  0
	 */
	public static final int STATUS_DEL = 0;
	
	/**
	 * 询价记录数据状态:待回复  1
	 */
	public static final int STATUS_WAITTING = 1;
	
//	/**
//	 * 询价记录数据状态:已查看 2
//	 */
//	public static final int STATUS_READED = 2;
	
	/**
	 * 询价记录数据状态:已回复 2
	 */
	public static final int STATUS_REPLYED = 2;
	
	/**
	 * 询价记录数据状态:被销售采纳  3
	 */
	public static final int STATUS_ADOPTED = 3;
	
	/**
	 * 询价记录数据状态:被销售采纳  4
	 */
	public static final int STATUS_PRODUCTED = 4;
	
	/**
	 * 询价记录数据状态:被销售采纳  5
	 */
	public static final int STATUS_ORDERED = 5;
	//询价记录数据状态end
	
	//询价记录类型 start
	/**
	 * 询价记录类型:询价  1
	 */
	public static final int TYPE_ADMIT_DT = 1;
	
	/**
	 * 询价项目类型:游学询价  3
	 */
	public static final int TYPE_ADMIT_YX = 3;
	
	/**
	 * 询价项目类型:大客户   4
	 */
	public static final int TYPE_ADMIT_DKH = 4;
	
	/**
	 * 询价项目类型:自由行  5
	 */
	public static final int TYPE_ADMIT_ZYX = 5;
	
	
	
	/**
	 * 询价记录类型:机票询价  7
	 */
	public static final int TYPE_FLIGHT = 7;
	//询价记录类型end
	
	
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
	 * 创建数据的用户id
	 * 一般是由销售发起
	 */
	private Long userId;
	
	/**
	 * 创建数据的用户name
	 * 一般是由销售发起
	 */
	private String userName;
	
	/**
	 * 计调用户id
	 */
	private Long operatorUserId;
	
	/**
	 * 计调用户name
	 */
	private String operatorUserName;
	
	/**
	 * 计调角色类型
	 */
	private String operatorRole;
	
	/**
	 * 状态
	 * 0 被删除；1 待回复；  2  已经回复；3 被销售采纳 4.已发产品  5.已生成订单
	 */
	private Integer status;
	
	/**
	 * 消息类型
	 * 1 接待内容 ，2  机票内容
	 */
	private Integer type;
	
	/**
	 * 成人单价
	 */
	private BigDecimal adultPrice;
	
	/**
	 * 成人数
	 */
	private Integer adultSum;
	
	/**
	 * 儿童单价
	 */
	private BigDecimal childPrice;
	
	/**
	 * 儿童数
	 */
	private Integer childSum;
	
	/**
	 * 特殊人群单价
	 */
	private BigDecimal specialPersonPrice;
	
	/**
	 * 特殊人群
	 */
	private Integer specialPersonSum;
	
	/**
	 * 回复内容
	 */
	private String content;
	
	/**
	 * 销售报价
	 */
	private BigDecimal salerPrice;
	
	/**
	 * 计调回复总报价:单位：元
	 */
	private BigDecimal operatorTotalPrice;
	
	/**
	 * 报价明细:
	 * 当type为1时即接待询价，报价明细为 接待报价配置（成人总价、儿童总价、特殊人群总价）{“”}
	 *  {"adult":{"price":1000},"child":{"price":200},"specialPerson":{"price":300},"other":[{"title":"优惠价1","price":200,"sum":200}]}
	 * 当type为2时即机票询价，报价明细为 机票报价配置（成人总价、儿童总价、特殊人群总价，并且机票为多段时(每一段对应的价格金额)）
	 */
	private String priceDetail;
	
	/**
	 * 销售行程文档
	 */
	private EstimatePriceFile tripFile;
	
	/**
	 * 计调报价时间
	 */
	private Date operatorPriceTime;
	
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

	private List<EstimatePrice> adult;
	private List<EstimatePrice> child;
	private List<EstimatePrice> special;
	private List<EstimatePrice> adultTop;
	private List<EstimatePrice> childTop;
	private List<EstimatePrice> specialTop;
	// Constructors

	/** default constructor */
	public EstimatePricerReply() {
	}

	/** full constructor */
	public EstimatePricerReply(Long pid, Long rid, Long userId,
			String userName, Long operatorUserId, String operatorUserName,
			String operatorRole, Integer status, Integer type,
			Integer adultSum, Integer childSum, Integer specialPersonSum,
			BigDecimal adultPrice,BigDecimal childPrice,BigDecimal specialPersonPrice,
			String content, BigDecimal salerPrice, BigDecimal operatorTotalPrice,
			String priceDetail, Date operatorPriceTime, Date cancelTime,
			Date createTime, Date modifyTime, String remark) {
		this.pid = pid;
		this.rid = rid;
		this.userId = userId;
		this.userName = userName;
		this.operatorUserId = operatorUserId;
		this.operatorUserName = operatorUserName;
		this.operatorRole = operatorRole;
		this.status = status;
		this.type = type;
		this.adultSum = adultSum;
		this.childSum = childSum;
		this.specialPersonSum = specialPersonSum;
		this.adultPrice = adultPrice;
		this.childPrice = childPrice;
		this.specialPersonPrice = specialPersonPrice;
		this.content = content;
		this.salerPrice = salerPrice;
		this.operatorTotalPrice = operatorTotalPrice;
		this.priceDetail = priceDetail;
		this.operatorPriceTime = operatorPriceTime;
		this.cancelTime = cancelTime;
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

	@Column(name = "operator_user_id")
	public Long getOperatorUserId() {
		return this.operatorUserId;
	}

	public void setOperatorUserId(Long operatorUserId) {
		this.operatorUserId = operatorUserId;
	}

	@Column(name = "operator_user_name")//, length = 64
	public String getOperatorUserName() {
		return this.operatorUserName;
	}

	public void setOperatorUserName(String operatorUserName) {
		this.operatorUserName = operatorUserName;
	}

	@Column(name = "operator_role", length = 244)
	public String getOperatorRole() {
		return this.operatorRole;
	}

	public void setOperatorRole(String operatorRole) {
		this.operatorRole = operatorRole;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "trip_file_id", referencedColumnName = "id")
	@Cascade(value = {org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@NotFound(action = NotFoundAction.IGNORE)
	public EstimatePriceFile getTripFile() {
		return tripFile;
	}

	public void setTripFile(EstimatePriceFile tripFile) {
		this.tripFile = tripFile;
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

	@Column(name = "content")//, length = 16777215
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "saler_price")
	public BigDecimal getSalerPrice() {
		return this.salerPrice;
	}

	public void setSalerPrice(BigDecimal salerPrice) {
		this.salerPrice = salerPrice;
	}

	

	@Column(name = "price_detail")//, length = 16777215
	public String getPriceDetail() {
		return this.priceDetail;
	}

	public void setPriceDetail(String priceDetail) {
		this.priceDetail = priceDetail;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "operator_price_time")//, length = 10
	public Date getOperatorPriceTime() {
		return this.operatorPriceTime;
	}

	public void setOperatorPriceTime(Date operatorPriceTime) {
		this.operatorPriceTime = operatorPriceTime;
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

	@Column(name = "operator_total_price")
	public BigDecimal getOperatorTotalPrice() {
		return operatorTotalPrice;
	}

	
	public void setOperatorTotalPrice(BigDecimal operatorTotalPrice) {
		this.operatorTotalPrice = operatorTotalPrice;
	}
	
	@Column(name = "adult_price")
	public BigDecimal getAdultPrice() {
		return adultPrice;
	}

	public void setAdultPrice(BigDecimal adultPrice) {
		this.adultPrice = adultPrice;
	}

	@Column(name = "child_price")
	public BigDecimal getChildPrice() {
		return childPrice;
	}

	public void setChildPrice(BigDecimal childPrice) {
		this.childPrice = childPrice;
	}

	@Column(name = "special_person_price")
	public BigDecimal getSpecialPersonPrice() {
		return specialPersonPrice;
	}

	public void setSpecialPersonPrice(BigDecimal specialPersonPrice) {
		this.specialPersonPrice = specialPersonPrice;
	}
	@Transient
	public List<EstimatePrice> getAdult() {
		return adult;
	}

	public void setAdult(List<EstimatePrice> adult) {
		this.adult = adult;
	}
	@Transient
	public List<EstimatePrice> getChild() {
		return child;
	}

	public void setChild(List<EstimatePrice> child) {
		this.child = child;
	}
	@Transient
	public List<EstimatePrice> getSpecial() {
		return special;
	}

	public void setSpecial(List<EstimatePrice> special) {
		this.special = special;
	}
	@Transient
	public List<EstimatePrice> getAdultTop() {
		return adultTop;
	}

	public void setAdultTop(List<EstimatePrice> adultTop) {
		this.adultTop = adultTop;
	}
	@Transient
	public List<EstimatePrice> getChildTop() {
		return childTop;
	}

	public void setChildTop(List<EstimatePrice> childTop) {
		this.childTop = childTop;
	}
	@Transient
	public List<EstimatePrice> getSpecialTop() {
		return specialTop;
	}

	public void setSpecialTop(List<EstimatePrice> specialTop) {
		this.specialTop = specialTop;
	}
	
}