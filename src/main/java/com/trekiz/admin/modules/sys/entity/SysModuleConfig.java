package com.trekiz.admin.modules.sys.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 模块的配置  
 * @author wangxk
 */
import com.trekiz.admin.common.persistence.BaseEntity;

@Entity
@Table(name="sys_config_company")
public class SysModuleConfig extends BaseEntity{

	private static final long serialVersionUID = 1L;
	
	private String id;
	private Long companyId;
	private String fmoduleId;
	private String fmodulename;
	private String moduleId;
	private String modulename;
	private String pageName;
	private String path;
	private String prePath;
	private String createBy;
	private Date createDate;
	private String updateBy;
	private Date updateDate;
	
	public SysModuleConfig(){
		
	}
	
	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public String getFmoduleId() {
		return fmoduleId;
	}
	public void setFmoduleId(String fmoduleId) {
		this.fmoduleId = fmoduleId;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getFmodulename() {
		return fmodulename;
	}

	public void setFmodulename(String fmodulename) {
		this.fmodulename = fmodulename;
	}

	public String getModulename() {
		return modulename;
	}

	public void setModulename(String modulename) {
		this.modulename = modulename;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getPrePath() {
		return prePath;
	}
	public void setPrePath(String prePath) {
		this.prePath = prePath;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
}
