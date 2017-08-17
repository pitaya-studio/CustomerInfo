/**
 *
 */
package com.trekiz.admin.modules.agent.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;

import com.trekiz.admin.common.persistence.DataEntity;
import com.trekiz.admin.common.utils.ChineseToEnglish;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.rebatesupplier.service.Rebatedable;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.UserDao;

  /**
 *  文件名: Agentinfo
 *  功能:
 *  产品Entity
 *  修改记录:   
 *  
 *  @author liangjingming
 *  @DateTime 2014-01-24
 *  @version 1.0
 */
@Entity
@Table(name = "agentinfo")
public class Agentinfo extends DataEntity implements Rebatedable {
	
	/** 门店 */
	public static final String T1_AGENT_TYPE_STORES = "1";
	/** 总社*/
	public static final String T1_AGENT_TYPE_HEADOFFICE = "2";
	/** 集团公司 */
	public static final String T1_AGENT_TYPE_GROUP = "3";
	
	@Autowired
	private UserDao userDao;

	@Transient
	public Integer getRebatedableType() {
	  return Rebatedable.REBATEDABLE_TYPE_AGENT;
	}
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	
    /** 渠道商名称 */
    private String agentName;
	/** 渠道商地址 */
    private String agentAddress;
    /** 渠道商地址全称 */
    private String agentAddressFull;
    /** 渠道商联系人 */
    private String agentContact;
    /** 渠道电话 */
    private String agentTel;
    /** 渠道传真 */
    private String agentFax;
    /** 渠道邮箱 */
    private String agentEmail;
    /** 渠道qq */
    private String agentQQ;
    /** 备注 */
    private String remarks;
    /** 渠道跟进销售员 */
    private String agentSalerId;
    /** 跟进销售集合(List<User>) */
    private List<User> agentSalerUser;
    /** 跟进销售id数组 */
    private String[] salerIdArray;
    /** 跟进销售name数组 */
    private String[] salerNameArray;
    /** 渠道当年营业额 */
    private Float agentBussiness;
    /** 渠道当年游客数 */
    private Integer agentTourists;
    /**  */
    private Integer currentYear;
    /** 渠道等级 */
    private Integer agentLevel;
    /** 渠道等级名称 */
    private String agentLevelName;
    /** 合作状态 */
    private Integer cooperateStatus;
    /** 合作状态名称 */
    private String cooperateStatusName;
    /** 返佣比例 */
    private Float agentRetComRadio;
    /**  */
    private Long supplyId;
    /**  */
    private String supplyName;
    /**策略等级*/
    private String strategyLevel;
    /**策略等级名称*/
    private String strategyLevelName;
    /**渠道固定电话*/
    private String agentFixedPhone;
    /**渠道渠道邮编*/
    private String agentPostcode;
    /**门市名称*/
    private String salesRoom;
    /**账号是否与微信端账号绑定**/
    private String isBound;	//全部：0，已关联：1，未关联：2	该字段仅为从页面获取对应参数而添加，agentInfo表中并无对应字段
    //账号来源
    private String accountFrom;	//全部：0，内部：1，微信：2      该字段仅为从页面获取对应参数而添加，agentInfo表中并无对应字段
    
////== 2014/12/24新增 ============================================================================
    private String agentNameEn;//英文名称
    private String agentBrand;//品牌
    private String paymentType;//结款方式
    private String paymentDay;//结款日
    private Long agentAddressProvince;//
    private Long agentAddressCity;//
    private String agentAddressStreet;//公司详细地址(街道)
    private Long belongsArea;//所属地
    private Long belongsAreaProvince;//所属地省市
    private Long belongsAreaCity;// 所属地城市
    private String agentContactMobile;//联系人手机
    private String agentContactTel;//联系人固定电话
    private String agentContactFax;//联系人传真
    private String agentContactEmail;//联系人邮箱
    private String agentContactQQ;//联系人QQ
    private String agentTelAreaCode;//渠道电话的区号
    private String agentFaxAreaCode;//渠道传真的区号
    private Long logo;//公司Logo
    private Long license;//经营许可证
    private Long businessLicense;//公司营业执照
    private Long taxCertificate;//税收登记证
    private Long organizeCertificate;//组织机构代码证
    private Long idCard;//企业法人身份证
    private Long bankOpenLicense;//银行开户许可证
    private Long travelAptitudes;//旅游业资质
    private String elseFile;//其他上传文件
    private String status;//所有字段是否全部保存完（0：未完全保存；1：完全保存了）
    
    private String startDate;//查询开始时间
   
    private String endDate;//查询结束时间
    
    private String paymentName;//
    
    private String orderByCreateDate;
    private String orderByUpdateDate;
//    private Set<SupplyContacts> supplyContacts = new HashSet<SupplyContacts>();
    
//=======================================================
    //add by majiancheng 2015-11-19
    private String agentNameShort;//简称（美途专用）
    
    //add 2015-12-23  渠道名称首字母
    private String agentFirstLetter;
    
    //add 2016-08-10  渠道类型
    private String agentType = "-1";
    //add 2016-08-10  渠道上级关系
    private String agentParent = "-1";
    //add 2016-11-29  t1首页列表展示类型  0491需求
    private Integer t1ListFlag;
    
    //保存完整的“1”，未保存完整的“0”
  	public static final String SAVE_FLAG_NORMAL = "0";
  	public static final String SAVE_FLAG_SAVED = "1";                                          
    public static final String IS_SYNCHRONIZED = "1";
    public static final String NOT_SYNCHRONIZED = "0";
    
	private Integer isSynchronize;
	
	private String abbreviation;	//简称
	
	/**
	 * 是否是QUAUQ渠道 0：否    1：是
	 */
	private String isQuauqAgent;
	/**
	 * 是否启用QUAUQ渠道 0：否    1：是
	 */
	private String enableQuauqAgent;
	/**
	 * quauq渠道的登陆人id
	 */
	private Long loginId;
	/**
	 * quauq渠道的登陆人姓名
	 */
	private String loginName;
	/**
	 * quauq渠道的联系人id
	 */
	private Long contactId;
	/**
	 * quauq渠道的联系人姓名
	 */
	private String contactName;
	

	//(目前只针对美途、华尔)新增字段,是否是非签约渠道：1是,null不是
	private String isUncontract;

	public Agentinfo() {
		super();
	}

	public Agentinfo(Long id){
		this();
		this.id = id;
	}

	public Agentinfo(String agentName, String agentSalerId, String agentNameShort, Long supplyId, String isUncontract) {
	  this.agentName = agentName;
	  this.agentSalerId = agentSalerId;
	  this.agentNameShort = agentNameShort;
	  this.supplyId = supplyId;
	  this.isUncontract = isUncontract;
	}

	  /** 新增  渠道联系人固定电话 */
	private String agentFixedLine;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_agentinfo")
	//@SequenceGenerator(name = "seq_agentinfo", sequenceName = "seq_agentinfo")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="abbreviation")
	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public void setAgentName(String agentName ){
        this.agentName = agentName ;
    }

    @Length(min=0, max=50)
    @Column(name="agentName",unique=false,nullable=true)
    public String getAgentName(){
        return this.agentName;
    }

    public void setAgentAddress(String agentAddress ){
        this.agentAddress = agentAddress ;
    }

    @Length(min=0, max=100)
    @Column(name="agentAddress",unique=false,nullable=true)
    public String getAgentAddress(){
        return this.agentAddress;
    }

    public void setAgentContact(String agentContact ){
        this.agentContact = agentContact ;
    }

    @Length(min=0, max=20)
    @Column(name="agentContact",unique=false,nullable=true)
    public String getAgentContact(){
        return this.agentContact;
    }

    public void setAgentTel(String agentTel ){
        this.agentTel = agentTel ;
    }

    @Length(min=0, max=20)
    @Column(name="agentTel",unique=false,nullable=true)
    public String getAgentTel(){
        return this.agentTel;
    }

    public void setAgentFax(String agentFax ){
        this.agentFax = agentFax ;
    }

    @Length(min=0, max=20)
    @Column(name="agentFax",unique=false,nullable=true)
    public String getAgentFax(){
        return this.agentFax;
    }

    public void setAgentEmail(String agentEmail ){
        this.agentEmail = agentEmail ;
    }

    @Length(min=0, max=50)
    @Column(name="agentEmail",unique=false,nullable=true)
    public String getAgentEmail(){
        return this.agentEmail;
    }

    
    @Length(min=0, max=50)
    @Column(name="agentQQ",unique=false,nullable=true)
    public String getAgentQQ() {
		return agentQQ;
	}

	public void setAgentQQ(String agentQQ) {
		this.agentQQ = agentQQ;
	}

    @Length(min=0, max=65530)
    @Column(name="remarks",unique=false,nullable=true)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Transient
	public List<User> getAgentSalerUser() {
		return agentSalerUser;
	}

	public void setAgentSalerUser(List<User> agentSalerUser) {
		this.agentSalerUser = agentSalerUser;
	}

	@Column(name="agentSalerId")
	public String getAgentSalerId() {
		return agentSalerId;
	}

	public void setAgentSalerId(String agentSalerId) {
		this.agentSalerId = agentSalerId;
	}
	
	@Transient
	public String[] getSalerIdArray() {
		return salerIdArray;
	}

	public void setSalerIdArray(String[] salerIdArray) {
		this.salerIdArray = salerIdArray;
	}
	
	@Transient
	public String getIsBound() {
		return isBound;
	}

	public void setIsBound(String isBound) {
		this.isBound = isBound;
	}
	@Transient
	public String getAccountFrom() {
		return accountFrom;
	}

	public void setAccountFrom(String accountFrom) {
		this.accountFrom = accountFrom;
	}

	@Transient
	public String[] getSalerNameArray() {
		return salerNameArray;
	}

	public void setSalerNameArray(String[] salerNameArray) {
		this.salerNameArray = salerNameArray;
	}

	public void setAgentBussiness(Float agentBussiness ){
        this.agentBussiness = agentBussiness ;
    }

    @Column(name="agentBussiness",unique=false,nullable=true)
    public Float getAgentBussiness(){
        return this.agentBussiness;
    }

    public void setAgentTourists(Integer agentTourists ){
        this.agentTourists = agentTourists ;
    }

    @Column(name="agentTourists",unique=false,nullable=true)
    public Integer getAgentTourists(){
        return this.agentTourists;
    }

    public void setCurrentYear(Integer currentYear ){
        this.currentYear = currentYear ;
    }

    @Column(name="currentYear",unique=false,nullable=true)
    public Integer getCurrentYear(){
        return this.currentYear;
    }

    public void setAgentLevel(Integer agentLevel ){
        this.agentLevel = agentLevel ;
    }

    @Column(name="agentLevel",unique=false,nullable=true)
    public Integer getAgentLevel(){
        return this.agentLevel;
    }

    public void setAgentLevelName(String agentLevelName ){
        this.agentLevelName = agentLevelName ;
    }

    @Length(min=0, max=10)
    @Column(name="agentLevelName",unique=false,nullable=true)
    public String getAgentLevelName(){
        return this.agentLevelName;
    }

    public void setCooperateStatus(Integer cooperateStatus ){
        this.cooperateStatus = cooperateStatus ;
    }

    @Column(name="cooperateStatus",unique=false,nullable=true)
    public Integer getCooperateStatus(){
        return this.cooperateStatus;
    }

    public void setCooperateStatusName(String cooperateStatusName ){
        this.cooperateStatusName = cooperateStatusName ;
    }

    @Length(min=0, max=10)
    @Column(name="cooperateStatusName",unique=false,nullable=true)
    public String getCooperateStatusName(){
        return this.cooperateStatusName;
    }

    public void setAgentRetComRadio(Float agentRetComRadio ){
        this.agentRetComRadio = agentRetComRadio ;
    }

    @Column(name="agentRetComRadio",unique=false,nullable=true)
    public Float getAgentRetComRadio(){
        return this.agentRetComRadio;
    }

    public void setSupplyId(Long supplyId ){
        this.supplyId = supplyId ;
    }

    @Column(name="supplyId",unique=false,nullable=true)
    public Long getSupplyId(){
        return this.supplyId;
    }

    public void setSupplyName(String supplyName ){
        this.supplyName = supplyName ;
    }

    @Length(min=0, max=100)
    @Column(name="supplyName",unique=false,nullable=true)
    public String getSupplyName(){
        return this.supplyName;
    }

    @Column(name="strategyLevel",unique=false,nullable=true)
	public String getStrategyLevel() {
		return strategyLevel;
	}

	public void setStrategyLevel(String strategyLevel) {
		this.strategyLevel = strategyLevel;
	}

	@Column(name="strategyLevelName",unique=false,nullable=true)
	public String getStrategyLevelName() {
		return strategyLevelName;
	}

	public void setStrategyLevelName(String strategyLevelName) {
		this.strategyLevelName = strategyLevelName;
	}
	
	@Column(name="is_synchronize",unique=false,nullable=true)
	public Integer getIsSynchronize() {
		return isSynchronize;
	}

	public void setIsSynchronize(Integer isSynchronize) {
		this.isSynchronize = isSynchronize;
	}
	
	@Length(min=0, max=20)
    @Column(name="agentFixedLine",unique=false,nullable=true)
	public String getAgentFixedLine() {
		return agentFixedLine;
	}

	public void setAgentFixedLine(String agentFixedLine) {
		this.agentFixedLine = agentFixedLine;
	}

	public String getAgentFixedPhone() {
		return agentFixedPhone;
	}

	public void setAgentFixedPhone(String agentFixedPhone) {
		this.agentFixedPhone = agentFixedPhone;
	}

	public String getAgentPostcode() {
		return agentPostcode;
	}

	public void setAgentPostcode(String agentPostcode) {
		this.agentPostcode = agentPostcode;
	}
	
	
////== 2014/12/24新增 ============================================================================

	public String getAgentNameEn() {
		return agentNameEn;
	}

	public void setAgentNameEn(String agentNameEn) {
		this.agentNameEn = agentNameEn;
	}

	public String getAgentBrand() {
		return agentBrand;
	}

	public void setAgentBrand(String agentBrand) {
		this.agentBrand = agentBrand;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentDay() {
		return paymentDay;
	}

	public void setPaymentDay(String paymentDay) {
		this.paymentDay = paymentDay;
	}

	public String getAgentAddressStreet() {
		return agentAddressStreet;
	}

	public void setAgentAddressStreet(String agentAddressStreet) {
		this.agentAddressStreet = agentAddressStreet;
	}

	public Long getBelongsArea() {
		return belongsArea;
	}

	public void setBelongsArea(Long belongsArea) {
		this.belongsArea = belongsArea;
	}

	public String getAgentContactMobile() {
		return agentContactMobile;
	}

	public void setAgentContactMobile(String agentContactMobile) {
		this.agentContactMobile = agentContactMobile;
	}

	public String getAgentContactTel() {
		return agentContactTel;
	}

	public void setAgentContactTel(String agentContactTel) {
		this.agentContactTel = agentContactTel;
	}

	public String getAgentContactFax() {
		return agentContactFax;
	}

	public void setAgentContactFax(String agentContactFax) {
		this.agentContactFax = agentContactFax;
	}

	public String getAgentContactEmail() {
		return agentContactEmail;
	}

	public void setAgentContactEmail(String agentContactEmail) {
		this.agentContactEmail = agentContactEmail;
	}

	public String getAgentContactQQ() {
		return agentContactQQ;
	}

	public void setAgentContactQQ(String agentContactQQ) {
		this.agentContactQQ = agentContactQQ;
	}

	public String getAgentTelAreaCode() {
		return agentTelAreaCode;
	}

	public void setAgentTelAreaCode(String agentTelAreaCode) {
		this.agentTelAreaCode = agentTelAreaCode;
	}

	public String getAgentFaxAreaCode() {
		return agentFaxAreaCode;
	}

	public void setAgentFaxAreaCode(String agentFaxAreaCode) {
		this.agentFaxAreaCode = agentFaxAreaCode;
	}

	public Long getLogo() {
		return logo;
	}

	public void setLogo(Long logo) {
		this.logo = logo;
	}

	public Long getBusinessLicense() {
		return businessLicense;
	}

	public void setBusinessLicense(Long businessLicense) {
		this.businessLicense = businessLicense;
	}

	public Long getTaxCertificate() {
		return taxCertificate;
	}

	public void setTaxCertificate(Long taxCertificate) {
		this.taxCertificate = taxCertificate;
	}

	public Long getOrganizeCertificate() {
		return organizeCertificate;
	}

	public void setOrganizeCertificate(Long organizeCertificate) {
		this.organizeCertificate = organizeCertificate;
	}

	public Long getIdCard() {
		return idCard;
	}

	public void setIdCard(Long idCard) {
		this.idCard = idCard;
	}

	public Long getBankOpenLicense() {
		return bankOpenLicense;
	}

	public void setBankOpenLicense(Long bankOpenLicense) {
		this.bankOpenLicense = bankOpenLicense;
	}

	public Long getTravelAptitudes() {
		return travelAptitudes;
	}

	public void setTravelAptitudes(Long travelAptitudes) {
		this.travelAptitudes = travelAptitudes;
	}

	public String getElseFile() {
		return elseFile;
	}

	public void setElseFile(String elseFile) {
		this.elseFile = elseFile;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getAgentAddressProvince() {
		return agentAddressProvince;
	}

	public void setAgentAddressProvince(Long agentAddressProvince) {
		this.agentAddressProvince = agentAddressProvince;
	}

	public Long getAgentAddressCity() {
		return agentAddressCity;
	}

	public void setAgentAddressCity(Long agentAddressCity) {
		this.agentAddressCity = agentAddressCity;
	}

	public Long getBelongsAreaProvince() {
		return belongsAreaProvince;
	}

	public void setBelongsAreaProvince(Long belongsAreaProvince) {
		this.belongsAreaProvince = belongsAreaProvince;
	}

	public Long getBelongsAreaCity() {
		return belongsAreaCity;
	}

	public void setBelongsAreaCity(Long belongsAreaCity) {
		this.belongsAreaCity = belongsAreaCity;
	}

	public Long getLicense() {
		return license;
	}

	public void setLicense(Long license) {
		this.license = license;
	}
	 @Transient
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	 @Transient
	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	@Transient
	public String getPaymentName() {
		return paymentName;
	}

	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}
	
	@Transient
	public String getOrderByCreateDate() {
		return orderByCreateDate;
	}

	public void setOrderByCreateDate(String orderByCreateDate) {
		this.orderByCreateDate = orderByCreateDate;
	}
	
	@Transient
	public String getOrderByUpdateDate() {
		return orderByUpdateDate;
	}

	public void setOrderByUpdateDate(String orderByUpdateDate) {
		this.orderByUpdateDate = orderByUpdateDate;
	}

	@Column(name="agentNameShort")
	public String getAgentNameShort() {
		return agentNameShort;
	}

	public void setAgentNameShort(String agentNameShort) {
		this.agentNameShort = agentNameShort;
	}
	
	
    @Column(name="agentFirstLetter")
    public String getAgentFirstLetter() {
		return agentFirstLetter;
	}

	public void setAgentFirstLetter(String agentFirstLetter) {
		this.agentFirstLetter = agentFirstLetter;
	}
	
	@Transient
	public String getAgentAddressFull() {
		return agentAddressFull;
	}

	public void setAgentAddressFull(String agentAddressFull) {
		
		this.agentAddressFull = agentAddressFull;
	}

	@Column(name="is_quauq_agent")
	public String getIsQuauqAgent() {
		return isQuauqAgent;
	}

	public void setIsQuauqAgent(String isQuauqAgent) {
		this.isQuauqAgent = isQuauqAgent;
	}
	
	@Column(name="enable_quauq_agent")
	public String getEnableQuauqAgent() {
		return enableQuauqAgent;
	}

	public void setEnableQuauqAgent(String enableQuauqAgent) {
		this.enableQuauqAgent = enableQuauqAgent;
	}

	@Transient
	public Long getLoginId() {
		return loginId;
	}

	public void setLoginId(Long loginId) {
		this.loginId = loginId;
	}

	@Transient
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@Transient
	public Long getContactId() {
		return contactId;
	}

	public void setContactId(Long contactId) {
		this.contactId = contactId;
	}

	@Transient
	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getSalesRoom() {
		return salesRoom;
	}

	public void setSalesRoom(String salesRoom) {
		this.salesRoom = salesRoom;
	}

	@Column(name = "is_uncontract")
	public String getIsUncontract() {
	  return isUncontract;
	}

	public void setIsUncontract(String isUncontract) {
	  this.isUncontract = isUncontract;
	}

	@Column(name = "agent_type")
	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}
	
	@Column(name = "t1_list_flag")
	public Integer getT1ListFlag() {
		return t1ListFlag;
	}

	public void setT1ListFlag(Integer t1ListFlag) {
		this.t1ListFlag = t1ListFlag;
	}

	@Column(name = "agent_parent")
	public String getAgentParent() {
		return agentParent;
	}

	public void setAgentParent(String agentParent) {
		this.agentParent = agentParent;
	}
	
	@Override
	public void prePersist() {
		if(StringUtil.isBlank(this.agentFirstLetter)){
			this.agentFirstLetter = ChineseToEnglish.getFirstLetter(this.agentName);
		}
		super.prePersist();
	}
	@Override
	public void preUpdate() {
		this.agentFirstLetter = ChineseToEnglish.getFirstLetter(this.agentName);
		super.preUpdate();
	}
	
  }


