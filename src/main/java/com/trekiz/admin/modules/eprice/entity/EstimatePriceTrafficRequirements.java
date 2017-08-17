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

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.search.annotations.Indexed;

/**
 * 交通询价内容
 * @lihua.xu
 */
@Entity
@Table(name = "estimate_price_traffic_requirements")
@DynamicInsert @DynamicUpdate
public class EstimatePriceTrafficRequirements implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	
	
	//交通方式类型 start
	/**
	 * 交通方式：混合交通  1
	 */
	public static final int TRAFFIC_TYPE_MIX = 1;
	
	/**
	 * 交通方式：汽车 2
	 */
	public static final int TRAFFIC_TYPE_CAR = 2;
	
	/**
	 * 交通方式：火车 3
	 */
	public static final int TRAFFIC_TYPE_TRAIN = 3;
	
	/**
	 * 交通方式：航班 4
	 */
	public static final int TRAFFIC_TYPE_FLIGHT = 4;
	
	//交通方式类型end
	
	
	//交通路线类型 start
	/**
	 * 交通路线类型：往返  1
	 */
	public static final int TRAFFIC_LINE_TYPE_RETURN = 1;
	
	/**
	 * 交通路线类型：单程  2
	 */
	public static final int TRAFFIC_LINE_TYPE_ONE_WAY = 2;
	
	/**
	 * 交通路线类型：多段  3
	 */
	public static final int TRAFFIC_LINE_TYPE_MULTISTAGE = 3;
	
	//交通路线类型end
	
	//交通询价内容数据状态start
	
	/**
	 * 交通询价内容数据状态:正常 1
	 */
	public static final int STATUS_NORMAL = 1;
	
	/**
	 *交通询价内容数据状态:删除  0
	 */
	public static final int STATUS_DEL = 0;
	
	/**
	 *数据状态:草稿  -1
	 */
	public static final int STATUS_DRAFT = -1;
	//交通询价内容数据状态end
	
	
	/**
	 * ID 主键ID
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
	 * 特殊要求
	 */
	private String specialDescn;
	
	/**
	 * 路段数
	 * 路段数，机票类型为多段时，记录实际路段数，
	 */
	private Integer sectionsSum;
	
	
	/**
	 * 询价项目数据状态
	 * 1 正常，0 被删除
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
	 * 特殊人群（婴儿）
	 */
	private Integer specialPersonSum;
	
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
	public EstimatePriceTrafficRequirements() {
	}

	/** full constructor */
	public EstimatePriceTrafficRequirements(Long pid, Long rid,
			Integer trafficType, Integer trafficLineType, String specialDescn,
			Integer sectionsSum, Integer status, Integer allPersonSum,
			Integer adultSum, Integer childSum, Integer specialPersonSum,
			Date createTime, Date modifyTime, String remark) {
		this.pid = pid;
		this.rid = rid;
		this.trafficType = trafficType;
		this.trafficLineType = trafficLineType;
		this.specialDescn = specialDescn;
		this.sectionsSum = sectionsSum;
		this.status = status;
		this.allPersonSum = allPersonSum;
		this.adultSum = adultSum;
		this.childSum = childSum;
		this.specialPersonSum = specialPersonSum;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.remark = remark;
	}
	
	public EstimatePriceTrafficRequirements(EstimatePriceTrafficRequirements est) {
		this.pid = est.pid;
		this.rid = est.rid;
		this.trafficType = est.trafficType;
		this.trafficLineType = est.trafficLineType;
		this.specialDescn = est.specialDescn;
		this.sectionsSum = est.sectionsSum;
		this.status = est.status;
		this.allPersonSum = est.allPersonSum;
		this.adultSum = est.adultSum;
		this.childSum = est.childSum;
		this.specialPersonSum = est.specialPersonSum;
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

	@Column(name = "special_descn", length = 1)
	public String getSpecialDescn() {
		return this.specialDescn;
	}

	public void setSpecialDescn(String specialDescn) {
		this.specialDescn = specialDescn;
	}

	@Column(name = "sections_sum")
	public Integer getSectionsSum() {
		return this.sectionsSum;
	}

	public void setSectionsSum(Integer sectionsSum) {
		this.sectionsSum = sectionsSum;
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
		if(StringUtils.isNotBlank(this.remark)){
			this.remark = this.remark.trim();
		}else{
			this.remark = null;
		}
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}