package com.trekiz.admin.modules.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sys_job_new")
public class SysJobNew {
	//id,自增id
	private Long id;
	//uuid
	private String uuid;
	//公司id
	private Long companyId;
	//公司uuid
	private String companyUuid;
	//职务名称
	private String name;
	//职务类型id(0:计调类,1:销售类,2:财务类,3:管理类,4:签证类,5:行政类)
	private Integer type;
	//创建者
	private Long createBy;
	//创建时间
	private Date createDate;
	//更新者
	private Long updateBy;
	//更新时间
	private Date updateDate;
	//删除标志 '0':正常 '1':删除
	private String delFlag;
	 
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		@Column(name="id",unique=true,nullable=false)
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}
		
		@Column(name="company_id")
		public Long getCompanyId() {
			return companyId;
		}

		public void setCompanyId(Long companyId) {
			this.companyId = companyId;
		}
		
		@Column(name="del_flag")
		public String getDelFlag() {
			return delFlag;
		}

		public void setDelFlag(String delFlag) {
			this.delFlag = delFlag;
		}
		
		@Column(name="uuid")
		public String getUuid() {
			return uuid;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
		}
		
		@Column(name="company_uuid")
		public String getCompanyUuid() {
			return companyUuid;
		}

		public void setCompanyUuid(String companyUuid) {
			this.companyUuid = companyUuid;
		}
		
		@Column(name="name")
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		@Column(name="type")
		public Integer getType() {
			return type;
		}

		public void setType(Integer type) {
			this.type = type;
		}
		
		@Column(name="create_by")
		public Long getCreateBy() {
			return createBy;
		}

		public void setCreateBy(Long createBy) {
			this.createBy = createBy;
		}
		
		@Column(name="create_date")
		public Date getCreateDate() {
			return createDate;
		}

		public void setCreateDate(Date createDate) {
			this.createDate = createDate;
		}
		
		@Column(name="update_by")
		public Long getUpdateBy() {
			return updateBy;
		}

		public void setUpdateBy(Long updateBy) {
			this.updateBy = updateBy;
		}
		
		@Column(name="update_date")
		public Date getUpdateDate() {
			return updateDate;
		}

		public void setUpdateDate(Date updateDate) {
			this.updateDate = updateDate;
		}
}
