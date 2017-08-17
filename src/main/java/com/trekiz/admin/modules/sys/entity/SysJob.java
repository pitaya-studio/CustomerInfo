
package com.trekiz.admin.modules.sys.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sys_job")

public class SysJob {
	 private Long id; 
	 private Long companyId;
	 private String jobName;
	 private Integer jobType;
	 private Integer orderType;
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

		public Long getCompanyId() {
			return companyId;
		}

		public void setCompanyId(Long companyId) {
			this.companyId = companyId;
		}

		public String getJobName() {
			return jobName;
		}

		public void setJobName(String jobName) {
			this.jobName = jobName;
		}

		public Integer getJobType() {
			return jobType;
		}

		public void setJobType(Integer jobType) {
			this.jobType = jobType;
		}

		public Integer getOrderType() {
			return orderType;
		}

		public void setOrderType(Integer orderType) {
			this.orderType = orderType;
		}

		public String getDelFlag() {
			return delFlag;
		}

		public void setDelFlag(String delFlag) {
			this.delFlag = delFlag;
		}

	
}
