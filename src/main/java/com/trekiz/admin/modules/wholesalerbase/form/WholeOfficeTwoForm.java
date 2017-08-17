package com.trekiz.admin.modules.wholesalerbase.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;

/**
 * 新增供应商第二步，网站信息
 * @author gao
 *  2015年4月13日
 */
public class WholeOfficeTwoForm {
	/**  第二步 网站信息 */
	@NotNull(message="该批发商不存在")
	private Long companyId;	// 批发商ID
	@Size(min=0,max=50)
	private String loginMaster; // 网站负责人
	@Size(min=0,max=30)
	private String loginMPhone; // 网站负责人电话
	@Size(min=0,max=30)
	private String loginSPhone;  // 网站客服电话
	@Email
	private String loginAMail;  // 网站管理员邮箱
	@Size(min=0,max=20)
	private String loginSQQ;	// 网站客服QQ
	private String loginStatus; // 网站状态 1：启用；2 停用
	private String loginShow;	// 浏览权限 1：开放浏览；2：关闭浏览；
	private String salerTripFileId; // 网站LOGO ID
	private String salerTripFileName;// 网站LOGO文件名
	private String salerTipFilePath; // 网站LOGO文件地址
	@Size(min=0,max=50)
	private String loginName; // 网站名称
	@Size(min=0,max=150)
	private String loginArr; // 网站地址
	private Integer saveOrNext;
	
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	public String getLoginMaster() {
		if(StringUtils.isNotBlank(this.loginMaster)){
			this.loginMaster = this.loginMaster.trim();
		}
		return loginMaster;
	}
	public void setLoginMaster(String loginMaster) {
		this.loginMaster = loginMaster;
	}
	
	public String getLoginMPhone() {
		if(StringUtils.isNotBlank(this.loginMPhone)){
			this.loginMPhone = this.loginMPhone.trim();
		}
		return loginMPhone;
	}
	public void setLoginMPhone(String loginMPhone) {
		this.loginMPhone = loginMPhone;
	}
	
	public String getLoginSPhone() {
		if(StringUtils.isNotBlank(this.loginSPhone)){
			this.loginSPhone = this.loginSPhone.trim();
		}
		return loginSPhone;
	}
	public void setLoginSPhone(String loginSPhone) {
		this.loginSPhone = loginSPhone;
	}
	
	public String getLoginAMail() {
		if(StringUtils.isNotBlank(this.loginAMail)){
			this.loginAMail = this.loginAMail.trim();
		}
		return loginAMail;
	}
	public void setLoginAMail(String loginAMail) {
		this.loginAMail = loginAMail;
	}
	
	public String getLoginSQQ() {
		if(StringUtils.isNotBlank(this.loginSQQ)){
			this.loginSQQ = this.loginSQQ.trim();
		}
		return loginSQQ;
	}
	public void setLoginSQQ(String loginSQQ) {
		this.loginSQQ = loginSQQ;
	}
	public Integer getSaveOrNext() {
		return saveOrNext;
	}
	public void setSaveOrNext(Integer saveOrNext) {
		this.saveOrNext = saveOrNext;
	}
	public String getLoginStatus() {
		return loginStatus;
	}
	public void setLoginStatus(String loginStatus) {
		this.loginStatus = loginStatus;
	}
	public String getLoginShow() {
		return loginShow;
	}
	public void setLoginShow(String loginShow) {
		this.loginShow = loginShow;
	}

	public String getSalerTripFileId() {
		return salerTripFileId;
	}

	public void setSalerTripFileId(String salerTripFileId) {
		this.salerTripFileId = salerTripFileId;
	}

	public String getSalerTripFileName() {
		return salerTripFileName;
	}

	public void setSalerTripFileName(String salerTripFileName) {
		this.salerTripFileName = salerTripFileName;
	}

	public String getSalerTipFilePath() {
		return salerTipFilePath;
	}

	public void setSalerTipFilePath(String salerTipFilePath) {
		this.salerTipFilePath = salerTipFilePath;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginArr() {
		return loginArr;
	}

	public void setLoginArr(String loginArr) {
		this.loginArr = loginArr;
	}
	
}
