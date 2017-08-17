package com.trekiz.admin.modules.activity.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 功能: 团控板Entity
 * @author tao.liu
 *
 */
@Entity
@Table(name = "group_control_board")
public class GroupControlBoard implements Serializable{

	
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	private long id;
	
	/**
	 * uuid
	 */
	private String uuid;
	
	/**
	 * 产品id
	 */
	private long travelActivityId;
	
	/**
	 * 产品名称
	 */
	private String travelActivityName;
	
	/**
	 * 团期id
	 */
	private long groupId;
	
	/**
	 * 团号
	 */
	private String groupCode;
	
	/**
	 * 操作类型 1收客 2减余位 3增余位 4退团 5转团
	 */
	private int operateType;
	
	/**
	 * 数量
	 */
	private int amount;
	
	/**
	 * 操作者id
	 */
	private long opeId;
	
	private String opeLoginName;

	private String opeName;
	
	/**
	 * 备注
	 */
	private String remarks;
	
	private Date opeDate;
	
	private Long officeId;
	
	/**
	 * 扩展字段1 
	 */
	private String extend1;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Column(name = "travelactivity_id", unique = false, nullable = false)
	public long getTravelActivityId() {
		return travelActivityId;
	}

	public void setTravelActivityId(long travelActivityId) {
		this.travelActivityId = travelActivityId;
	}

	@Column(name = "travelactivity_name", unique = false)
	public String getTravelActivityName() {
		return travelActivityName;
	}

	public void setTravelActivityName(String travelActivityName) {
		this.travelActivityName = travelActivityName;
	}

	@Column(name = "group_id", unique = false, nullable = false)
	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	@Column(name = "group_code", unique = false)
	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	@Column(name = "operate_type", unique = false, nullable = false)
	public int getOperateType() {
		return operateType;
	}

	public void setOperateType(int operateType) {
		this.operateType = operateType;
	}

	@Column(name = "amount", unique = false, nullable = false)
	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Column(name = "operate_id", unique = false, nullable = false)
	public long getOpeId() {
		return opeId;
	}

	public void setOpeId(long opeId) {
		this.opeId = opeId;
	}

	@Column(name = "operate_loginname", unique = false, nullable = false)
	public String getOpeLoginName() {
		return opeLoginName;
	}

	public void setOpeLoginName(String opeLoginName) {
		this.opeLoginName = opeLoginName;
	}

	@Column(name = "operate_name", unique = false, nullable = false)
	public String getOpeName() {
		return opeName;
	}

	public void setOpeName(String opeName) {
		this.opeName = opeName;
	}

	@Column(name = "remarks", unique = false, nullable = false)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name = "create_date", unique = false, nullable = false)
	public Date getOpeDate() {
		return opeDate;
	}

	public void setOpeDate(Date opeDate) {
		this.opeDate = opeDate;
	}
	
	@Column(name = "officeId", unique = false, nullable = false)
	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	@Column(name = "extend_1", unique = false)
	public String getExtend1() {
		return extend1;
	}

	public void setExtend1(String extend1) {
		this.extend1 = extend1;
	}
	
}
