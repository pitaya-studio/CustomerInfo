package com.trekiz.admin.modules.activity.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 文件名: ActivityGroupCompare 功能: 产品团期对比Entity
 * 
 * @author zhangyp
 * @DateTime 2015年10月20
 * @version 1.0
 */
@Entity
@Table(name = "activity_group_compare")
public class ActivityGroupCompare{

	/**
	 * 产品团期对比 id
	 */
	private Long id;
	/**
	 * 产品团期 id
	 */
	private Long activityGroupId;
	/**
	 * 操作员 id
	 */
	private Long operatorId;
	/**
	 * 排序值
	 */
	private String sortName;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "activity_group_id")
	public Long getActivityGroupId() {
		return activityGroupId;
	}

	public void setActivityGroupId(Long activityGroupId) {
		this.activityGroupId = activityGroupId;
	}

	@Column(name = "operator_id")
	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}
}
