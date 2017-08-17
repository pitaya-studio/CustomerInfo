
package com.trekiz.admin.modules.sys.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "user_job_view")

public class UserJob {
	 private Long id; 
	 private Long userId;
	 private Long deptId;	
	 private Long parentDept;
	 private Integer deptLevel;
	 private Long jobId;
	 private String deptName;
	 private String jobName;
	 private Integer jobType;
	 private Integer orderType;
	 private int count = 0;//审核条数计数(当前审核职务、审核状态的条数)
	 
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		@Column(name="id",unique=true,nullable=false)
		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public Long getDeptId() {
			return deptId;
		}

		public void setDeptId(Long deptId) {
			this.deptId = deptId;
		}

		public Long getJobId() {
			return jobId;
		}

		public void setJobId(Long jobId) {
			this.jobId = jobId;
		}

		public String getDeptName() {
			return deptName;
		}

		public void setDeptName(String deptName) {
			this.deptName = deptName;
		}

		public String getJobName() {
			return jobName;
		}

		public void setJobName(String jobName) {
			this.jobName = jobName;
		}

	

		public Integer getDeptLevel() {
			return deptLevel;
		}

		public void setDeptLevel(Integer deptLevel) {
			this.deptLevel = deptLevel;
		}

		public Long getParentDept() {
			return parentDept;
		}

		public void setParentDept(Long parentDept) {
			this.parentDept = parentDept;
		}

		public Integer getOrderType() {
			return orderType;
		}

		public void setOrderType(Integer orderType) {
			this.orderType = orderType;
		}

		public Integer getJobType() {
			return jobType;
		}

		public void setJobType(Integer jobType) {
			this.jobType = jobType;
		}
        @Transient
		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		
	
}
