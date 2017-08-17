/**
 *
 */
package com.trekiz.admin.modules.sys.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.trekiz.admin.common.persistence.DataEntity;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.sys.utils.TreeEntity;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.CascadeType;
import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 机构Entity
 * @author zj
 * @version 2013-11-19
 */
@Entity
@Table(name = "sys_office")
@DynamicInsert @DynamicUpdate
public class Office extends DataEntity implements TreeEntity{

	private static final long serialVersionUID = 1L;
	private Long id;		// 编号
	private Office parent;	// 父级编号
	private String parentIds; // 所有父级编号
	private String code; 	// 机构编码
	private String name; 	// 机构名称
	private String address; // 联系地址
	private String enAddress; //英文地址
	private String zipCode; // 邮政编码
	private String master; 	// 负责人
	private String phone; 	// 电话
	private String fax; 	// 传真
	private String email; 	// 邮箱
	private String domainName; //批发商域名
	private Integer isValidateDoma; //根据域名权限登陆用户
	protected String remarks;	// 备注
	private String supplierType; // 批发商类型
	private String supplierBrand; // 批发商品牌
	private String companyName; // 公司名称
	private Long logo; // 公司LOGO
	private Long isCancleOrder; // 是否允许系统自动取消订单
	private Long isAllowSupplement; // 是否允许补单
	private String createSubOrder; // 是否生成机票子订单
	private Long orderPayMode; // 订单支付方式
	private Long queryCommonOrderList; //是否查看已收明细（除机票产品）
	private Long queryAirticketOrderList; //是否查看已收明细（机票产品）
	private String queryCommonFields; //订单查询字段
	private String queryAirticketFields; //机票订单查询字段
	private List<Office> childList = Lists.newArrayList();// 拥有子机构列表

	private String enname; // 机构英文名称
	private Integer budgetCostAutoPass; // 预算成本审核自动通过
	private Integer costAutoPass; // 实际成本审核自动通过
	private Integer whenToSheet; //预算成本、实际成本何时写入预报单、结算单

	private Integer budgetCostWhenUpdate;	//预算成本何时写入预报单，0：保存时；1：提交审批时
	private Integer actualCostWhenUpdate;	//实际成本何时写入结算单，0：保存时；1：提交审批时；2：提交付款申请(仅针对拉美途)


	private Integer frontier;	// 国内、国外
	private String countryId;		// 公司所在国家
	private String provinceId;	// 公司所在的省
	private String cityId;				// 公司所在的市
	private String districtId;				// 公司所在的区
	private String districtCode;	// 公司所属行政区编码
	// 批发商状态
	private Integer status=0;
	// 等级
	private SysCompanyDictView level;
	//private Long levelId;
	/**   网站信息 */
	private String loginCode; // 登录账号
	private String loginPW; // 登陆密码
	private String loginArr; // 网站地址
	private String loginMaster; // 网站负责人
	private String loginMPhone; // 网站负责人电话
	private String loginSPhone;  // 网站客服电话
	private String loginAMail;  // 网站管理员邮箱
	private String loginSQQ;	// 网站客服QQ

	private String loginStatus; // 网站状态 1：启用；2 停用
	private String loginShow;	// 浏览权限 1：开放浏览；2：关闭浏览
	private String loginLogoName; // 网站logo名称
	private String loginLogoPath; // 网站logo文件地址
	private String loginName; // 网站名称

	private String supplierTypeNames; // 批发商类型名称组

	private String areaInternal; // 批发商国内覆盖区域
	private String areaOverseas; // 批发商国外覆盖区域

	private Integer isNeedAttention; //是否下单提醒
	private Integer isAllowModifyAgentInfo; //是否允许订单渠道联系人信息输入修改
	private Integer isAllowAddAgentInfo; //是否允许添加订单渠道联系人信息
	private Integer isAllowModify; //是否允许修改渠道
	private Integer isChangeAgent;//是否允许供应商下的订单修改预定渠道
	private Integer isNeedGroupCode;//是否需要团号库配置
	private Integer isNeedCruiseshipControll;//是否需要游轮团控
	private Integer isRemoveApplyInvoiceLimit;//是否解除发票申请限制
	private Integer estimateModel;//询价模式
	
	/*团号规则配置S  c460 */
	private Integer groupCodeRuleDT;//団期
	private Integer groupCodeRuleJP;//机票
	private Integer groupCodeRuleQZ;//签证
	/*团号规则配置E*/

	/*是否允许多对象返佣*/
	private Integer isAllowMultiRebateObject;

	private Integer isShowCancelOrder; // 是否展示已取消的订单 是否显示已取消的订单 0展示 1不展示 默认为0
	private Integer isShowDeleteOrder; // 是否显示已删除的订单 0展示 1不展示 默认为0
	private Integer isSeizedConfirmation; // 是否使用客户确认占位功能 ,默认：否。(0：否 1：是)
	//0318新增是否允许修改销售签证订单下的游客信息--s//
	private Integer isAllowModifyXSVisaOrder;//是否允许修改销售签证订单下的游客信息:0-否,1-是
	//0318新增是否允许修改销售签证订单下的游客信息--e//
	private Integer isMustRefundDate;  //还款日期是否必填
	//private Integer isMailRemind;// 是否开通邮件提醒
	
	private Date exportTime;	//导出excel的时间

	private String uuid;//批发商uuid
	private Integer confirmPay;  //是否需要确认付款:0不需要，1需要

	private Integer visaCostPrice;//报名模块、订单模块是否显示签证成本价格 :0不显示，1显示
	
	private Integer shelfRightsStatus;//批发商权限是否启用：0：启用，1：未启用
	
	private Integer t1FreePosionStatus;//T1平台余位状态：0.实时 1.现询

	private Integer preOpenInvoice;// 0444需求 '预开发票 0：否，1：是'
	/** 服务费率（供应服务费占QUAUQ价的比率） */
	private BigDecimal chargeRate = new BigDecimal("0.0100");
	/** 签证订单的三级菜单tab页“全部订单”中是否展示“已取消”等的订单(数据形式： 99) */
	private String banedVisaOrderOfAllTab;
	
	//--S-0517-----------------
	/** 许可证号*/
	private String licenseNumber;
	/** 简介*/
	private String summary;
	/** 网址*/
	private String webSite;
	/** 营业执照*/
	private String businessLicense;
	/** 业务资质证书*/
	private String businessCertificate;
	/** 合作协议*/
	private String cooperationProtocol;
	//--E-0517-----------------
	//=========================
	//
	//=========================
	
	public Office(){
		super();
	}

	public Office(Long id){
		this();
		this.id = id;
	}

	public Integer getPreOpenInvoice() {
		return preOpenInvoice;
	}
	
	public void setPreOpenInvoice(Integer preOpenInvoice) {
		this.preOpenInvoice = preOpenInvoice;
	}

	@Length(min=0, max=255)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_sys_office")
	//	@SequenceGenerator(name = "seq_sys_office", sequenceName = "seq_sys_office")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="parentId")
	@NotFound(action = NotFoundAction.IGNORE)
	@NotNull
	public Office getParent() {
		return parent;
	}

	public void setParent(Office parent) {
		this.parent = parent;
	}

	@Length(min=1, max=255)
	public String getParentIds() {
		return parentIds;
	}

	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	//	@Length(min=1, max=100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Length(min=0, max=255)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Length(min=0, max=100)
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Length(min=0, max=100)
	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	@Length(min=0, max=200)
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Length(min=0, max=200)
	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	@Length(min=0, max=200)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	@Column(name="domain_name")
	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}
	
	@Column(name="export_time")
	public Date getExportTime() {
		return exportTime;
	}

	public void setExportTime(Date exportTime) {
		this.exportTime = exportTime;
	}

	@Column(name="is_check_domainName", nullable=true, unique=false)
	public Integer getIsValidateDoma() {
		return isValidateDoma;
	}

	public void setIsValidateDoma(Integer isValidateDoma) {
		this.isValidateDoma = isValidateDoma;
	}

	@Length(min=0, max=100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@JsonIgnore
	@OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE},fetch=FetchType.LAZY,mappedBy="parent")
	@Where(clause="delFlag='"+DEL_FLAG_NORMAL+"'")
	@OrderBy(value="code")
	@NotFound(action = NotFoundAction.IGNORE)
	//	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	public List<Office> getChildList() {
		return childList;
	}

	public void setChildList(List<Office> childList) {
		this.childList = childList;
	}

	@Transient
	public static void sortList(List<Office> list, List<Office> sourcelist, Long parentId){
		for (int i=0; i<sourcelist.size(); i++){
			Office e = sourcelist.get(i);
			if (e.getParent()!=null && e.getParent().getId()!=null
					&& e.getParent().getId().equals(parentId)){
				list.add(e);
				// 判断是否还有子节点, 有则继续获取子节点
				for (int j=0; j<sourcelist.size(); j++){
					Office child = sourcelist.get(j);
					if (child.getParent()!=null && child.getParent().getId()!=null
							&& child.getParent().getId().equals(e.getId())){
						sortList(list, sourcelist, e.getId());
						break;
					}
				}
			}
		}
	}

	@Transient
	public boolean isRoot(){
		return isRoot(this.id);
	}

	@Transient
	public static boolean isRoot(Long id){
		return id != null && id.equals(1L);
	}

	@Override
	public Long fetchIdentity() {
		return this.getId();
	}

	@Override
	public Long fetchFatherIdentity() {
		return this.getParent().getId();
	}

	@Column(name="supplierType",unique=false,nullable=true)
	public String getSupplierType() {
		return supplierType;
	}

	public void setSupplierType(String supplierType) {
		this.supplierType = supplierType;
	}

	@Column(name="supplierBrand",unique=false,nullable=true)
	public String getSupplierBrand() {
		return supplierBrand;
	}

	public void setSupplierBrand(String supplierBrand) {
		this.supplierBrand = supplierBrand;
	}

	@Column(name="companyName",unique=false,nullable=true)
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	@Column(name="logo",unique=false,nullable=true)
	public Long getLogo() {
		return logo;
	}

	public void setLogo(Long logo) {
		this.logo = logo;
	}

	@Column(name = "is_cancle_order", columnDefinition = "tinyint default 1")
	public Long getIsCancleOrder() {
		return isCancleOrder;
	}

	public void setIsCancleOrder(Long isCancleOrder) {
		this.isCancleOrder = isCancleOrder;
	}

	@Column(name = "is_allow_supplement", columnDefinition = "tinyint default 1")
	public Long getIsAllowSupplement() {
		return isAllowSupplement;
	}

	public void setIsAllowSupplement(Long isAllowSupplement) {
		this.isAllowSupplement = isAllowSupplement;
	}

	@Column(name = "is_allow_multi_rebate_object")
	public Integer getIsAllowMultiRebateObject() {
		return isAllowMultiRebateObject;
	}

	public void setIsAllowMultiRebateObject(Integer isAllowMultiRebateObject) {
		this.isAllowMultiRebateObject = isAllowMultiRebateObject;
	}

	@Column(name = "is_show_cancel_order")
	public Integer getIsShowCancelOrder() {
		return isShowCancelOrder;
	}

	public void setIsShowCancelOrder(Integer isShowCancelOrder) {
		this.isShowCancelOrder = isShowCancelOrder;
	}

	@Column(name = "is_show_delete_order")
	public Integer getIsShowDeleteOrder() {
		return isShowDeleteOrder;
	}

	public void setIsShowDeleteOrder(Integer isShowDeleteOrder) {
		this.isShowDeleteOrder = isShowDeleteOrder;
	}
	
	@Column(name = "is_seized_confirmation")
	public Integer getIsSeizedConfirmation() {
		return isSeizedConfirmation;
	}
	
	//0318新增是否允许修改销售签证订单下的游客信息--s//
	@Column(name="is_allow_modify_xs_vo")
	public Integer getIsAllowModifyXSVisaOrder() {
		return isAllowModifyXSVisaOrder;
	}

	public void setIsAllowModifyXSVisaOrder(Integer isAllowModifyXSVisaOrder) {
		this.isAllowModifyXSVisaOrder = isAllowModifyXSVisaOrder;
	}
	//0318新增是否允许修改销售签证订单下的游客信息--e//
	
	@Column(name="is_must_refundDate")
	public Integer getIsMustRefundDate() {
		return isMustRefundDate;
	}

	public void setIsSeizedConfirmation(Integer isSeizedConfirmation) {
		this.isSeizedConfirmation = isSeizedConfirmation;
	}

	public void setIsMustRefundDate(Integer isMustRefundDate) {
		this.isMustRefundDate = isMustRefundDate;
	}

	/*@Column(name = "is_mail_remind")
	public Integer getIsMailRemind() {
		return isMailRemind;
	}

	public void setIsMailRemind(Integer isMailRemind) {
		this.isMailRemind = isMailRemind;
	}*/

	public Integer getFrontier() {
		return frontier;
	}

	public void setFrontier(Integer frontier) {
		this.frontier = frontier;
	}
	@Column(name="country_id")
	public String getCountryId() {
		return countryId;
	}

	public void setCountryId(String countryId) {
		this.countryId = countryId;
	}
	@Column(name="province_id")
	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	@Column(name="city_id")
	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	@Column(name="district_id")
	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	@Column(name="district_code")
	public String getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(String districtCode) {
		this.districtCode = districtCode;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	//	@Column(name="level_id")
	//	public Long getLevelId() {
	//		return levelId;
	//	}
	//
	//	public void setLevelId(Long levelId) {
	//		this.levelId = levelId;
	//	}
	@JsonIgnore
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "level_id", referencedColumnName = "uuid")
	public SysCompanyDictView getLevel() {
		return level;
	}
	public void setLevel(SysCompanyDictView level) {
		this.level = level;
	}
	@Column(name="login_code")
	public String getLoginCode() {
		return loginCode;
	}

	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}

	@Column(name="login_pw")
	public String getLoginPW() {
		return loginPW;
	}

	public void setLoginPW(String loginPW) {
		this.loginPW = loginPW;
	}

	@Column(name="login_arr")
	public String getLoginArr() {
		return loginArr;
	}

	public void setLoginArr(String loginArr) {
		this.loginArr = loginArr;
	}

	@Column(name="login_master")
	public String getLoginMaster() {
		return loginMaster;
	}

	public void setLoginMaster(String loginMaster) {
		this.loginMaster = loginMaster;
	}

	@Column(name="login_mphone")
	public String getLoginMPhone() {
		return loginMPhone;
	}

	public void setLoginMPhone(String loginMPhone) {
		this.loginMPhone = loginMPhone;
	}

	@Column(name="login_sphone")
	public String getLoginSPhone() {
		return loginSPhone;
	}

	public void setLoginSPhone(String loginSPhone) {
		this.loginSPhone = loginSPhone;
	}

	@Column(name="login_amail")
	public String getLoginAMail() {
		return loginAMail;
	}

	public void setLoginAMail(String loginAMail) {
		this.loginAMail = loginAMail;
	}

	@Column(name="login_sqq")
	public String getLoginSQQ() {
		return loginSQQ;
	}

	public void setLoginSQQ(String loginSQQ) {
		this.loginSQQ = loginSQQ;
	}

	public String getEnname() {
		return enname;
	}

	public void setEnname(String enname) {
		this.enname = enname;
	}

	@Column(name="login_status")
	public String getLoginStatus() {
		return loginStatus;
	}

	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}
	@Column(name="login_show")
	public String getLoginShow() {
		return loginShow;
	}

	public void setLoginShow(String loginShow) {
		this.loginShow = loginShow;
	}
	@Column(name="login_name")
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	@Column(name="login_logo_name")
	public String getLoginLogoName() {
		return loginLogoName;
	}

	public void setLoginLogoName(String loginLogoName) {
		this.loginLogoName = loginLogoName;
	}
	@Column(name="login_logo_path")
	public String getLoginLogoPath() {
		return loginLogoPath;
	}

	public void setLoginLogoPath(String loginLogoPath) {
		this.loginLogoPath = loginLogoPath;
	}

	@Transient
	public String getSupplierTypeNames() {
		return supplierTypeNames;
	}

	public void setSupplierTypeNames(String supplierTypeNames) {
		this.supplierTypeNames = supplierTypeNames;
	}
	@Column(name="area_internal")
	public String getAreaInternal() {
		return areaInternal;
	}

	public void setAreaInternal(String areaInternal) {
		this.areaInternal = areaInternal;
	}

	@Column(name="area_overseas")
	public String getAreaOverseas() {
		return areaOverseas;
	}

	public void setAreaOverseas(String areaOverseas) {
		this.areaOverseas = areaOverseas;
	}

	public Integer getCostAutoPass() {
		return costAutoPass;
	}

	public void setCostAutoPass(Integer costAutoPass) {
		this.costAutoPass = costAutoPass;
	}

	@Column(name="when_to_sheet")
	public Integer getWhenToSheet() { return whenToSheet; }

	public void setWhenToSheet(Integer whenToSheet) { this.whenToSheet = whenToSheet; }

	@Column(name="budget_cost_when_update")
	public Integer getBudgetCostWhenUpdate() {
		return budgetCostWhenUpdate;
	}

	public void setBudgetCostWhenUpdate(Integer budgetCostWhenUpdate) {
		this.budgetCostWhenUpdate = budgetCostWhenUpdate;
	}

	@Column(name="actual_cost_when_update")
	public Integer getActualCostWhenUpdate() {
		return actualCostWhenUpdate;
	}

	public void setActualCostWhenUpdate(Integer actualCostWhenUpdate) {
		this.actualCostWhenUpdate = actualCostWhenUpdate;
	}

	@Column(name="is_need_attention")
	public Integer getIsNeedAttention() {
		return isNeedAttention;
	}

	public void setIsNeedAttention(Integer isNeedAttention) {
		this.isNeedAttention = isNeedAttention;
	}

	@Column(name="is_allow_modify_agentInfo")
	public Integer getIsAllowModifyAgentInfo() {
		return isAllowModifyAgentInfo;
	}

	public void setIsAllowModifyAgentInfo(Integer isAllowModifyAgentInfo) {
		this.isAllowModifyAgentInfo = isAllowModifyAgentInfo;
	}

	@Column(name="is_allow_add_agentInfo")
	public Integer getIsAllowAddAgentInfo() {
		return isAllowAddAgentInfo;
	}

	public void setIsAllowAddAgentInfo(Integer isAllowAddAgentInfo) {
		this.isAllowAddAgentInfo = isAllowAddAgentInfo;
	}

	@Column(name="is_allow_modify")
	public Integer getIsAllowModify() {
		return isAllowModify;
	}

	public void setIsAllowModify(Integer isAllowModify) {
		this.isAllowModify = isAllowModify;
	}

	@Column(name="is_change_agent")
	public Integer getIsChangeAgent() {
		return isChangeAgent;
	}

	public void setIsChangeAgent(Integer isChangeAgent) {
		this.isChangeAgent = isChangeAgent;
	}

	@Column(name="create_sub_order")
	public String getCreateSubOrder() {
		return createSubOrder;
	}

	public void setCreateSubOrder(String createSubOrder) {
		this.createSubOrder = createSubOrder;
	}

	@Column(name="order_pay_mode")
	public Long getOrderPayMode() {
		return orderPayMode;
	}

	public void setOrderPayMode(Long orderPayMode) {
		this.orderPayMode = orderPayMode;
	}

	@Column(name="query_common_order_list")
	public Long getQueryCommonOrderList() {
		return queryCommonOrderList;
	}

	public void setQueryCommonOrderList(Long queryCommonOrderList) {
		this.queryCommonOrderList = queryCommonOrderList;
	}

	@Column(name="query_airticket_order_list")
	public Long getQueryAirticketOrderList() {
		return queryAirticketOrderList;
	}

	public void setQueryAirticketOrderList(Long queryAirticketOrderList) {
		this.queryAirticketOrderList = queryAirticketOrderList;
	}

	@Column(name="query_common_fields")
	public String getQueryCommonFields() {
		return queryCommonFields;
	}

	public void setQueryCommonFields(String queryCommonFields) {
		this.queryCommonFields = queryCommonFields;
	}

	@Column(name="query_airticket_fields")
	public String getQueryAirticketFields() {
		return queryAirticketFields;
	}

	public void setQueryAirticketFields(String queryAirticketFields) {
		this.queryAirticketFields = queryAirticketFields;
	}

	@Column(name="estimate_model")
	public Integer getEstimateModel() {
		return estimateModel;
	}

	public void setEstimateModel(Integer estimateModel) {
		this.estimateModel = estimateModel;
	}

	@Column(name="uuid")
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Column(name="confirmPay")
	public Integer getConfirmPay() {
		return confirmPay;
	}

	public void setConfirmPay(Integer confirmPay) {
		this.confirmPay = confirmPay;
	}

	@Column(name="is_show_visa_costPrice")
	public Integer getVisaCostPrice() {
		return visaCostPrice;
	}

	public void setVisaCostPrice(Integer visaCostPrice) {
		this.visaCostPrice = visaCostPrice;
	}

	@Column(name="enAddress")
	public String getEnAddress() {
		return enAddress;
	}

	public void setEnAddress(String enAddress) {
		this.enAddress = enAddress;
	}

	@Column(name="is_need_groupCode")
	public Integer getIsNeedGroupCode() {
		return isNeedGroupCode;
	}

	public void setIsNeedGroupCode(Integer isNeedGroupCode) {
		this.isNeedGroupCode = isNeedGroupCode;
	}

	@Column(name="budgetCostAutoPass")
	public Integer getBudgetCostAutoPass() {
		return budgetCostAutoPass;
	}

	public void setBudgetCostAutoPass(Integer budgetCostAutoPass) {
		this.budgetCostAutoPass = budgetCostAutoPass;
	}
	
	@Column(name="is_need_cruiseshipControll")
	public Integer getIsNeedCruiseshipControll() {
		return isNeedCruiseshipControll;
	}

	public void setIsNeedCruiseshipControll(Integer isNeedCruiseshipControll) {
		this.isNeedCruiseshipControll = isNeedCruiseshipControll;
	}
	
	@Column(name="is_remove_applyInvoice_limit")
	public Integer getIsRemoveApplyInvoiceLimit() {
		return isRemoveApplyInvoiceLimit;
	}

	public void setIsRemoveApplyInvoiceLimit(Integer isRemoveApplyInvoiceLimit) {
		this.isRemoveApplyInvoiceLimit = isRemoveApplyInvoiceLimit;
	}

	@Column(name="groupCodeRule_dantuan")
	public Integer getGroupCodeRuleDT() {
		return groupCodeRuleDT;
	}

	public void setGroupCodeRuleDT(Integer groupCodeRuleDT) {
		this.groupCodeRuleDT = groupCodeRuleDT;
	}
	
	@Column(name="groupCodeRule_jipiao")
	public Integer getGroupCodeRuleJP() {
		return groupCodeRuleJP;
	}

	public void setGroupCodeRuleJP(Integer groupCodeRuleJP) {
		this.groupCodeRuleJP = groupCodeRuleJP;
	}
	
	@Column(name="groupCodeRule_qianzheng")
	public Integer getGroupCodeRuleQZ() {
		return groupCodeRuleQZ;
	}

	public void setGroupCodeRuleQZ(Integer groupCodeRuleQZ) {
		this.groupCodeRuleQZ = groupCodeRuleQZ;
	}
	
	@Column(name="shelfRightsStatus")
	public Integer getShelfRightsStatus() {
		return shelfRightsStatus;
	}

	public void setShelfRightsStatus(Integer shelfRightsStatus) {
		this.shelfRightsStatus = shelfRightsStatus;
	}
	
	@Column(name="t1_freePosion_status")
	public Integer getT1FreePosionStatus() {
		return t1FreePosionStatus;
	}

	public void setT1FreePosionStatus(Integer t1FreePosionStatus) {
		this.t1FreePosionStatus = t1FreePosionStatus;
	}

	@Column(name="charge_rate")
	public BigDecimal getChargeRate() {
		return chargeRate;
	}

	public void setChargeRate(BigDecimal chargeRate) {
		this.chargeRate = chargeRate;
	}
	
	@Column(name="banedVisaOrder_of_allTab")
	public String getBanedVisaOrderOfAllTab() {
		return banedVisaOrderOfAllTab;
	}

	public void setBanedVisaOrderOfAllTab(String banedVisaOrderOfAllTab) {
		this.banedVisaOrderOfAllTab = banedVisaOrderOfAllTab;
	}

	@Column(name="license_number")
	public String getLicenseNumber() {
		return licenseNumber;
	}

	public void setLicenseNumber(String licenseNumber) {
		this.licenseNumber = licenseNumber;
	}

	@Column(name = "summary")
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	@Column(name = "web_site")
	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	@Column(name = "business_license")
	public String getBusinessLicense() {
		return businessLicense;
	}

	public void setBusinessLicense(String businessLicense) {
		this.businessLicense = businessLicense;
	}

	@Column(name = "business_certificate")
	public String getBusinessCertificate() {
		return businessCertificate;
	}

	public void setBusinessCertificate(String businessCertificate) {
		this.businessCertificate = businessCertificate;
	}

	@Column(name = "cooperation_protocol")
	public String getCooperationProtocol() {
		return cooperationProtocol;
	}

	public void setCooperationProtocol(String cooperationProtocol) {
		this.cooperationProtocol = cooperationProtocol;
	}
	
	
}