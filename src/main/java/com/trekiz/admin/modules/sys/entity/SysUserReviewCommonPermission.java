package com.trekiz.admin.modules.sys.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.BaseEntity;

@Entity
@Table(name = "sys_user_review_common_permission")
public class SysUserReviewCommonPermission extends BaseEntity implements Serializable{
	
	private static final long serialVersionUID = -6685431190918968093L;
	private Long id;
	private String uuid;
	private Long company_id;
	private String company_uuid;
	private Long user_id;
	private Integer is_jump_task_permit;//是否允许越级审批
	private Integer is_applier_auto_approve;//是否申请人审批自动通过
	private String create_by;
	private Date create_date;
	private String update_by;
	private Date update_date;
	private Integer del_flag;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Long getCompany_id() {
		return company_id;
	}
	public void setCompany_id(Long company_id) {
		this.company_id = company_id;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public String getCompany_uuid() {
		return company_uuid;
	}
	public void setCompany_uuid(String company_uuid) {
		this.company_uuid = company_uuid;
	}
	public Integer getIs_jump_task_permit() {
		return is_jump_task_permit;
	}
	public void setIs_jump_task_permit(Integer is_jump_task_permit) {
		this.is_jump_task_permit = is_jump_task_permit;
	}
	public Integer getIs_applier_auto_approve() {
		return is_applier_auto_approve;
	}
	public void setIs_applier_auto_approve(Integer is_applier_auto_approve) {
		this.is_applier_auto_approve = is_applier_auto_approve;
	}
	public String getCreate_by() {
		return create_by;
	}
	public void setCreate_by(String create_by) {
		this.create_by = create_by;
	}
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	public String getUpdate_by() {
		return update_by;
	}
	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}
	public Integer getDel_flag() {
		return del_flag;
	}
	public void setDel_flag(Integer del_flag) {
		this.del_flag = del_flag;
	}
	public Date getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}
	
	
}
