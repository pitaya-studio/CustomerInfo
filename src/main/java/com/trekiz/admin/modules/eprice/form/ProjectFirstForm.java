package com.trekiz.admin.modules.eprice.form;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;

/**
 * 新增询价项目表单的第一个表单——询价基本信息表单数据的form类
 * @author lihua.xu
 * @时间 2014年9月18日
 *
 */
public class ProjectFirstForm {
	
	/**
	 * 询价项目id，修改时使用
	 */
	private Long projectId;
	
	
	
	/**
	 * 询价项目类型
	 */
	private int type;
	

	/**
	 * 新增询价时，选择的“询价模式”
	 */
	private int emode;
	
	/**
	 * 销售用户id
	 */
	@NotNull
	private Long salerId;
	
	/**
	 * 团队类型
	 */
	//@NotNull
	@Min(value=1)
	private Integer teamType;
	
	/**
	 * 询价客户类型
	 */
	@NotNull
	@Min(value=0)
	private Integer customerType;
	
	/**
	 * 询价客户
	 */	
	private String customerName;
	
	/**
	 * 询价客户联系人
	 */
	private String contactPerson;
	
	/**
	 * 询价客户联系人电话
	 */
	private String contactMobile;
	/**
	 * 询价客户
	 */	
	private String customerNametogether;
	
	/**
	 * 询价客户联系人
	 */
	private String contactPersontogether;
	
	/**
	 * 询价客户联系人电话
	 */
	private String contactMobiletogether;
	
	/**
	 * 企业合作伙伴id
	 */
	private Long customerAgentId;
	
	
	
	/**
	 * 询价客户类型name
	 */
	private String otherContactWay;
	
	/**
	 * 询价客户
	 */	
	private String otherCustomerName;
	
	/**
	 * 询价客户联系人
	 */
	private String otherContactPerson;
	
	/**
	 * 询价客户联系人电话
	 */
	private String otherContactMobile;
	
	/**
	 * 预算金额
	 */
	@Min(value=0)
	private BigDecimal budget;
	
	/**
	 * 预算类型
	 */
	private Integer budgetType;
	
	/**
	 * 预算币种
	 */
	private Integer budgetPayTypeId;
	
	/**
	 * 预算备注
	 */
	private String budgetRemark;
	
	/**
	 * 申请总人数
	 */
	@NotNull
	@Min(value=1)
	private Integer allPersonSum;
	
	/**
	 * 成人总数
	 */
	@NotNull
	@Min(value=0)
	private Integer adultSum;
	
	/**
	 * 儿童总数
	 */
	@NotNull
	@Min(value=0)
	private Integer childSum;
	
	/**
	 * 特殊人群总数
	 */
	@NotNull
	@Min(value=0)
	private Integer specialPersonSum;
	
	/**
	 * 特殊人群说明
	 */
	@Size(max=50,min=0)
	private String specialRemark;
	
	/*
	 * 是否是再次询价 1-是 0-否
	 */
	private Integer onceAgain  =0;
	/**
	 * 是否为计调主管分配计调 1-是 0-否
	 */
	private Integer truemanager =0;
	
	public String check(){
		String msg = null;
//		//接待计调和票务计调不能同时没有  目前计调选择已经移至第二步，这段验证也移至第二步
//		if((aoperatorUserId==null || aoperatorUserId.length==0) && (toperatorUserId==null || toperatorUserId.length==0)){
//			back = false;			
//		}
		// 判断询价客户类型 1：直客；2：同行
		if(customerType==1){
			if(StringUtils.isBlank(customerName)){
				return "询价客户不能为空";
			}
			if(StringUtils.isBlank(contactPerson)){
				return "询价客户联系人不能为空";
			}
			if(StringUtils.isBlank(contactMobile)){
				return "询价客户联系人电话不能为空";
			}
		}else if(customerType==2){
			if(customerAgentId<=0){
				return "请选择询价客户";
			}
			if( StringUtils.isBlank(contactPersontogether) ){
				return "询价客户联系人电话不能为空";
			}
			if( StringUtils.isBlank(contactMobiletogether) ){
				return "询价客户联系人不能为空";
			}
		}
		// 判断总人数
		if(allPersonSum!=(adultSum+childSum+specialPersonSum) && allPersonSum>0){
//			back=false;
			return "申请总人数应等于成人人数+儿童人数+特殊人群";
		}
		return msg;
	}
	
	
	public Long getProjectId() {
		return projectId;
	}

	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public int getEmode() {
		return emode;
	}

	public void setEmode(int emode) {
		this.emode = emode;
	}

	public Long getSalerId() {
		return salerId;
	}

	public void setSalerId(Long salerId) {
		this.salerId = salerId;
	}

	public Integer getTeamType() {
		return teamType;
	}

	public void setTeamType(Integer teamType) {
		this.teamType = teamType;
	}

	public Integer getCustomerType() {
		return customerType;
	}

	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getContactMobile() {
		return contactMobile;
	}

	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}

	public Long getCustomerAgentId() {
		return customerAgentId;
	}

	public void setCustomerAgentId(Long customerAgentId) {
		this.customerAgentId = customerAgentId;
	}

	public String getOtherContactWay() {
		return otherContactWay;
	}

	public void setOtherContactWay(String otherContactWay) {
		this.otherContactWay = otherContactWay;
	}
	
	

	public String getOtherCustomerName() {
		return otherCustomerName;
	}




	public void setOtherCustomerName(String otherCustomerName) {
		this.otherCustomerName = otherCustomerName;
	}




	public String getOtherContactPerson() {
		return otherContactPerson;
	}




	public void setOtherContactPerson(String otherContactPerson) {
		this.otherContactPerson = otherContactPerson;
	}




	public String getOtherContactMobile() {
		return otherContactMobile;
	}




	public void setOtherContactMobile(String otherContactMobile) {
		this.otherContactMobile = otherContactMobile;
	}




	public BigDecimal getBudget() {
		return budget;
	}

	public void setBudget(BigDecimal budget) {
		this.budget = budget;
	}

	public Integer getBudgetType() {
		return budgetType;
	}

	public void setBudgetType(Integer budgetType) {
		this.budgetType = budgetType;
	}

	public String getBudgetRemark() {
		return budgetRemark;
	}

	public void setBudgetRemark(String budgetRemark) {
		this.budgetRemark = budgetRemark;
	}

	public Integer getAllPersonSum() {
		return allPersonSum;
	}

	public void setAllPersonSum(Integer allPersonSum) {
		this.allPersonSum = allPersonSum;
	}

	public Integer getAdultSum() {
		return adultSum;
	}

	public void setAdultSum(Integer adultSum) {
		this.adultSum = adultSum;
	}

	public Integer getChildSum() {
		return childSum;
	}

	public void setChildSum(Integer childSum) {
		this.childSum = childSum;
	}

	public Integer getSpecialPersonSum() {
		return specialPersonSum;
	}

	public void setSpecialPersonSum(Integer specialPersonSum) {
		this.specialPersonSum = specialPersonSum;
	}






	public String getCustomerNametogether() {
		return customerNametogether;
	}






	public void setCustomerNametogether(String customerNametogether) {
		this.customerNametogether = customerNametogether;
	}






	public String getContactPersontogether() {
		return contactPersontogether;
	}






	public void setContactPersontogether(String contactPersontogether) {
		this.contactPersontogether = contactPersontogether;
	}






	public String getContactMobiletogether() {
		return contactMobiletogether;
	}






	public void setContactMobiletogether(String contactMobiletogether) {
		this.contactMobiletogether = contactMobiletogether;
	}

	public String getSpecialRemark() {
		return specialRemark;
	}






	public void setSpecialRemark(String specialRemark) {
		this.specialRemark = specialRemark;
	}






	public Integer getBudgetPayTypeId() {
		return budgetPayTypeId;
	}






	public void setBudgetPayTypeId(Integer budgetPayTypeId) {
		this.budgetPayTypeId = budgetPayTypeId;
	}






	public Integer getOnceAgain() {
		return onceAgain;
	}






	public void setOnceAgain(Integer onceAgain) {
		this.onceAgain = onceAgain;
	}






	public Integer getTruemanager() {
		return truemanager;
	}






	public void setTruemanager(Integer truemanager) {
		this.truemanager = truemanager;
	}

}
