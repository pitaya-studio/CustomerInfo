package com.trekiz.admin.modules.sys.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.BaseEntity;

/**
 * 保存自由配置文件的路径
 * @author wangxk
 */
@Entity
@Table(name="sys_config_uri_path")
public class SysModulePath extends BaseEntity{	
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String modulepath;
	
	@Id
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getModulepath() {
		return modulepath;
	}
	public void setModulepath(String modulepath) {
		this.modulepath = modulepath;
	}
	
}
