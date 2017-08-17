package com.trekiz.admin.modules.groupCover.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.DataEntity;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;

/**
 * 散拼团期补位实体类
 * 
 */
@Entity
@Table(name = "groupcover")
public class GroupCover extends DataEntity {

	private static final long serialVersionUID = 1L;
	private Long id; // 主键
	/** 产品出团信息表id */
	private ActivityGroup activityGroup;
	/** 团号 */
	private String groupCode;
	/** 补位号 */
	private String coverCode;
	/** 补位人数 */
	private Integer coverPosition;
	/** 补位状态,1待补位，2已补位，3已驳回，4已取消，5已生成订单 */
	private Integer coverStatus;
	/** 订单ID */
	private Long orderId;
	/** 备注 */
	private String remarks;
	/** 补位状态名称 */
	private String coverStatusName;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@OneToOne
	@JoinColumn(name = "activityGroupId")
	@JsonIgnore
	@NotFound(action = NotFoundAction.IGNORE)
	public ActivityGroup getActivityGroup() {
		return activityGroup;
	}
	public void setActivityGroup(ActivityGroup activityGroup) {
		this.activityGroup = activityGroup;
	}
	
	@Column(name="groupCode")
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	
	@Column(name="coverCode")
	public String getCoverCode() {
		return coverCode;
	}
	public void setCoverCode(String coverCode) {
		this.coverCode = coverCode;
	}
	
	@Column(name="coverPosition")
	public Integer getCoverPosition() {
		return coverPosition;
	}
	public void setCoverPosition(Integer coverPosition) {
		this.coverPosition = coverPosition;
	}
	
	@Column(name="coverStatus")
	public Integer getCoverStatus() {
		return coverStatus;
	}
	public void setCoverStatus(Integer coverStatus) {
		this.coverStatus = coverStatus;
	}
	
	@Column(name="orderId")
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	@Column(name="remarks")
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	@Transient
	public String getCoverStatusName() {
		coverStatusName = "待补位";
		if (this.coverStatus != null) {
			if (Context.COVER_STATUS_DBW == coverStatus) {
				coverStatusName = "待补位";
			} else if (Context.COVER_STATUS_YBW == coverStatus) {
				coverStatusName = "已补位";
			} else if (Context.COVER_STATUS_YBH == coverStatus) {
				coverStatusName = "已驳回";
			} else if (Context.COVER_STATUS_YQX == coverStatus) {
				coverStatusName = "已取消";
			} else if (Context.COVER_STATUS_SCDD == coverStatus) {
				coverStatusName = "生成订单";
			}
		} 
		return coverStatusName;
	}
	
	public void setCoverStatusName(String coverStatusName) {
		this.coverStatusName = coverStatusName;
	}
	
}
