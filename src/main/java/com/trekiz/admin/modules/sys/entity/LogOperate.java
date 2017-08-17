package com.trekiz.admin.modules.sys.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.trekiz.admin.common.persistence.BaseEntity;

/**
 * 操作日志
 * @author wangxk
 *
 */
@Entity
@Table(name="log_operate")
public class LogOperate extends BaseEntity{
	
	private static final long serialVersionUID = 1L;
	private String id ;
	private long ope_id;
	private String ope_loginname;//操作人  当前用户的登录名 loginName
	private String ope_name;
	private long ope_comid;
	private String ope_comname;
	private String modular_id;
	private String modular_name;
	private String ope_type;
	private String content;
	private String create_date;
	private Integer bussiness_type;
	private Long bussiness_id;
	
	public String getOpe_type() {
		return ope_type;
	}

	public void setOpe_type(String ope_type) {
		this.ope_type = ope_type;
	}

	public LogOperate() {
		super();
	}
	
	public LogOperate(String id) {
		this();
		this.id = id;
	}
	@Id
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public long getOpe_id() {
		return ope_id;
	}

	public void setOpe_id(long ope_id) {
		this.ope_id = ope_id;
	}

	public String getOpe_loginname() {
		return ope_loginname;
	}

	public void setOpe_loginname(String ope_loginname) {
		this.ope_loginname = ope_loginname;
	}

	public String getOpe_name() {
		return ope_name;
	}

	public void setOpe_name(String ope_name) {
		this.ope_name = ope_name;
	}

	public long getOpe_comid() {
		return ope_comid;
	}

	public void setOpe_comid(long ope_comid) {
		this.ope_comid = ope_comid;
	}

	public String getOpe_comname() {
		return ope_comname;
	}

	public void setOpe_comname(String ope_comname) {
		this.ope_comname = ope_comname;
	}

	public String getModular_id() {
		return modular_id;
	}

	public void setModular_id(String modular_id) {
		this.modular_id = modular_id;
	}

	public String getModular_name() {
		return modular_name;
	}

	public void setModular_name(String modular_name) {
		this.modular_name = modular_name;
	}

	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public Integer getBussiness_type() {
		return bussiness_type;
	}

	public void setBussiness_type(Integer bussiness_type) {
		this.bussiness_type = bussiness_type;
	}

	public Long getBussiness_id() {
		return bussiness_id;
	}

	public void setBussiness_id(Long bussiness_id) {
		this.bussiness_id = bussiness_id;
	}

}
