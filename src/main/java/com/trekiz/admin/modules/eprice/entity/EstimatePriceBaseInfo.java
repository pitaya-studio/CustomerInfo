package com.trekiz.admin.modules.eprice.entity;

import java.math.BigDecimal;
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
 * 询价基本信息
 *  @author lihua.xu
 */
@Entity
@Table(name = "estimate_price_base_info")
@DynamicInsert @DynamicUpdate
public class EstimatePriceBaseInfo implements java.io.Serializable {

	
	private static final long serialVersionUID = 1L;
	
	
	//询价基础信息数据状态start
	
	/**
	 * 询价基础信息数据状态:正常 1
	 */
	public static final int STATUS_NORMAL = 1;
	
	/**
	 * 询价基础信息数据状态:删除  0
	 */
	public static final int STATUS_DEL = 0;
	/**
	 *数据状态:草稿  -1
	 */
	public static final int STATUS_DRAFT = -1;
	//询价基础信息数据状态end
	
	//询价基础信息团队类型 start
	/**
	 * 询价基础信息团队类型:单团 1
	 */
	public static final int TEAM_TYPE_ALONE = 1;
	
	/**
	 * 询价基础信息团队类型:大客户 2
	 */
	public static final int TEAM_TYPE_BCUSTOMER = 2;
	
	/**
	 * 询价基础信息团队类型:游学 3
	 */
	public static final int TEAM_TYPE_STUDY_TOUR = 3;
	
	/**
	 * 询价基础信息团队类型:自由行 4
	 */
	public static final int TEAM_TYPE_STUDY_FREE_TRA = 4;
	
	/**
	 * 询价基础信息团队类型:机票 5
	 */
	public static final int TEAM_TYPE_TRAFFIC = 5;
	
	//询价基础信息团队类型 end
	
	
	//询价基础信息询价客户类型 start
	
	/**
	 * 询价基础信息询价客户类型:直客 1
	 */
	public static final int CUSTOMER_TYPE_STRAIGHT = 1;
	
	/**
	 * 询价基础信息询价客户类型:同行 2
	 */
	public static final int CUSTOMER_TYPE_PEER = 2;
	
	/**
	 * 询价基础信息询价客户类型:其他 0
	 */
	public static final int CUSTOMER_TYPE_OTHER = 0;
	
	//询价基础信息询价客户类型 end
	
	
	//询价基础信息预算类型 start
	/**
	 * 询价基础信息预算类型:按人 1
	 */
	public static final int BUDGET_TYPE_PERSON = 1;
	/**
	 * 询价基础信息预算类型:按团 2
	 */
	public static final int BUDGET_TYPE_TEAM = 2;
	//询价基础信息预算类型 end
	
	
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
	 * 询价销售用户id
	 */
	private Long salerId;
	
	/**
	 * 询价销售用户name
	 */
	private String salerName;
	
	/**
	 * 询价销售用户手机号
	 */
	private String salerPhone;
	
	/**
	 * 询价销售用户邮箱
	 */
	private String salerEmail;
	
	/**
	 * 接待计调ids 
	 * jsonArray
	 * 接待计调ids [111,2222,333,33,444]
	 */
	private String aoperatorUserJson;
	
	/**
	 * 票务计调ids 
	 * jsonArray
	 * 票务计调ids [111,2222,333,33,444]
	 */
	private String toperatorUserJson;
	
	/**
	 * 询价基础数据状态：1 正常，0 被删除 -1 草稿
	 */
	private Integer status;
	
	/**
	 * 团队类型：
	 * 1 单团
	 * 2 大客户
	 * 3 游学
	 * 4 自由行 
	 */
	private Integer teamType;
	
	/**
	 * 询价客户类型
	 * 1 直客
	 * 2 同行
	 * 0  其他
	 */
	private Integer customerType;
	
	/**
	 * 询价客户类型name
	 * 当询价客户类型 customer_type值为3（其他时），此自动有效，记录其他的类型名
	 */
	private String customerTypeName;
	
	/**
	 * 渠道商id
	 * 当且只有当 custpmer_type 为2时，本字段才有值，才有意义
	 */
	private Long customerAgentId;
	
	/**
	 * 询价客户名称
	 */
	private String customerName;
	
	/**
	 * 联系人名字
	 */
	private String contactPerson;
	
	/**
	 * 联系人电话
	 */
	private String contactMobile;
	
	/**
	 * 其他联系方式
	 * 询价客户类型为0（其他）时，使用此字段
	 */
	private String otherContactWay;
	
	/**
	 * 预算金额
	 * 询价客户类型为0（其他）时，使用此字段
	 */
	private BigDecimal budget;
	
	/**
	 * 预算类型:
	 * 1 按人
	 * 2 按团
	 * 询价客户类型为0（其他）时，使用此字段
	 */
	private Integer budgetType;
	
	/**
	 * 预算备注
	 * 询价客户类型为0（其他）时，使用此字段
	 */
	private String budgetRemark;
	
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
	 * 特殊人群说明
	 */
	private String specialRemark;
	
	/**
	 * 其他人群
	 */
	private String otherPersonJson;
	
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
	
	/**
	 * 0:直接选取计调；1：选取计调主管
	 */
	private Integer formanager;

	// Constructors

	/** default constructor */
	public EstimatePriceBaseInfo() {
	}

	/** full constructor */
	public EstimatePriceBaseInfo(Long id, Long pid, Long rid,
			Long salerId, String salerName, String salerPhone,
			String salerEmail, Integer status, Integer teamType,String aoperatorUserJson,String toperatorUserJson,
			Integer customerType, String customerTypeName,
			Long customerAgentId, String customerName, String contactPerson,
			String contactMobile, String otherContactWay, BigDecimal budget,
			Integer budgetType, String budgetRemark, Integer allPersonSum,
			Integer adultSum, Integer childSum, Integer specialPersonSum,
			String otherPersonJson, Date createTime, Date modifyTime,
			String remark,Integer formanager) {
		this.id = id;
		this.pid = pid;
		this.rid = rid;
		this.salerId = salerId;
		this.salerName = salerName;
		this.salerPhone = salerPhone;
		this.aoperatorUserJson=aoperatorUserJson;
		this.toperatorUserJson=toperatorUserJson;
		this.salerEmail = salerEmail;
		this.status = status;
		this.teamType = teamType;
		this.customerType = customerType;
		this.customerTypeName = customerTypeName;
		this.customerAgentId = customerAgentId;
		this.customerName = customerName;
		this.contactPerson = contactPerson;
		this.contactMobile = contactMobile;
		this.otherContactWay = otherContactWay;
		this.budget = budget;
		this.budgetType = budgetType;
		this.budgetRemark = budgetRemark;
		this.allPersonSum = allPersonSum;
		this.adultSum = adultSum;
		this.childSum = childSum;
		this.specialPersonSum = specialPersonSum;
		this.otherPersonJson = otherPersonJson;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
		this.remark = remark;
		this.formanager = formanager;
	}
	public EstimatePriceBaseInfo(EstimatePriceBaseInfo info) {
		this.id = info.id;
		this.pid = info.pid;
		this.rid = info.rid;
		this.salerId = info.salerId;
		this.salerName = info.salerName;
		this.salerPhone = info.salerPhone;
		this.aoperatorUserJson=info.aoperatorUserJson;
		this.toperatorUserJson=info.toperatorUserJson;
		this.salerEmail = info.salerEmail;
		this.status = info.status;
		this.teamType = info.teamType;
		this.customerType = info.customerType;
		this.customerTypeName = info.customerTypeName;
		this.customerAgentId = info.customerAgentId;
		this.customerName = info.customerName;
		this.contactPerson = info.contactPerson;
		this.contactMobile = info.contactMobile;
		this.otherContactWay = info.otherContactWay;
		this.budget = info.budget;
		this.budgetType = info.budgetType;
		this.budgetRemark = info.budgetRemark;
		this.allPersonSum = info.allPersonSum;
		this.adultSum = info.adultSum;
		this.childSum = info.childSum;
		this.specialPersonSum = info.specialPersonSum;
		this.otherPersonJson = info.otherPersonJson;
		this.createTime = info.createTime;
		this.modifyTime = info.modifyTime;
		this.remark = info.remark;
		this.formanager = info.formanager;
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

	@Column(name = "saler_id")
	public Long getSalerId() {
		return this.salerId;
	}

	public void setSalerId(Long salerId) {
		this.salerId = salerId;
	}

	@Column(name = "saler_name")//, length = 64
	public String getSalerName() {
		return this.salerName;
	}

	public void setSalerName(String salerName) {
		this.salerName = salerName;
	}

	@Column(name = "saler_phone")//, length = 32
	public String getSalerPhone() {
		return this.salerPhone;
	}

	public void setSalerPhone(String salerPhone) {
		this.salerPhone = salerPhone;
	}

	@Column(name = "saler_email")//, length = 64
	public String getSalerEmail() {
		return this.salerEmail;
	}

	public void setSalerEmail(String salerEmail) {
		this.salerEmail = salerEmail;
	}
	
	
	@Column(name = "aoperator_user_id_json")
	public String getAoperatorUserJson() {
		return aoperatorUserJson;
	}

	public void setAoperatorUserJson(String aoperatorUserJson) {
		this.aoperatorUserJson = aoperatorUserJson;
	}

	@Column(name = "toperator_user_id_json")
	public String getToperatorUserJson() {
		return toperatorUserJson;
	}

	public void setToperatorUserJson(String toperatorUserJson) {
		this.toperatorUserJson = toperatorUserJson;
	}

	@Column(name = "status")
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "team_type")
	public Integer getTeamType() {
		return this.teamType;
	}

	public void setTeamType(Integer teamType) {
		this.teamType = teamType;
	}

	@Column(name = "customer_type")
	public Integer getCustomerType() {
		return this.customerType;
	}

	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
	}
	
	
	@Column(name = "customer_type_name")//, length = 128
	public String getCustomerTypeName() {
		return this.customerTypeName;
	}

	public void setCustomerTypeName(String customerTypeName) {
		this.customerTypeName = customerTypeName;
	}

	@Column(name = "customer_agent_id")
	public Long getCustomerAgentId() {
		return this.customerAgentId;
	}

	public void setCustomerAgentId(Long customerAgentId) {
		this.customerAgentId = customerAgentId;
	}

	@Column(name = "customer_name")//, length = 32
	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	@Column(name = "contact_person")//, length = 32
	public String getContactPerson() {
		return this.contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	@Column(name = "contact_mobile")//, length = 32
	public String getContactMobile() {
		return this.contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}
	
	
	
	@Column(name = "other_contact_way")//, length = 64
	public String getOtherContactWay() {
		return otherContactWay;
	}

	public void setOtherContactWay(String otherContactWay) {
		this.otherContactWay = otherContactWay;
	}

	@Column(name = "budget")
	public BigDecimal getBudget() {
		return budget;
	}

	public void setBudget(BigDecimal budget) {
		this.budget = budget;
	}

	@Column(name = "budget_type")
	public Integer getBudgetType() {
		return budgetType;
	}

	public void setBudgetType(Integer budgetType) {
		this.budgetType = budgetType;
	}

	@Column(name = "budget_remark")//,length=1024
	public String getBudgetRemark() {
		return budgetRemark;
	}

	public void setBudgetRemark(String budgetRemark) {
		this.budgetRemark = budgetRemark;
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

	@Column(name = "special_remark")//, length = 256
	public String getSpecialRemark() {
		return specialRemark;
	}

	public void setSpecialRemark(String specialRemark) {
		this.specialRemark = specialRemark;
	}

	@Column(name = "other_person_json")//, length = 16777215
	public String getOtherPersonJson() {
		return this.otherPersonJson;
	}

	public void setOtherPersonJson(String otherPersonJson) {
		this.otherPersonJson = otherPersonJson;
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

	@Column(name = "for_manager")
	public Integer getFormanager() {
		return formanager;
	}

	public void setFormanager(Integer formanager) {
		this.formanager = formanager;
	}

}