package com.trekiz.admin.modules.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
	 *  文件名: batch_record
	 *  功能:
	 *  批次记录Entity
	 *  
	 *  @author yue.wang
	 *  @DateTime 2015-03-017
	 */
@Entity
@Table(name = "batch_record")
public class BatchRecord {
	//TYPE_1 批量借护照
	public static final int TYPE_1 = 1;
	private Long id;
	private String uuid;
	private String  batchNo ;
	private int    type ;
	private long   createUserId ;
	private String createUserName ;
	private Date   createTime;
	private int    passportStatus;//护照操作状态：0:未借  1:已借  2:未还 3:已还
	private int    isSubmit;//是否提交 0:未提交  1：已提交
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false)
	public Long getId() {
		return id;
	}
	 
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="batch_no")
	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	@Column(name="type")
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	@Column(name="create_user_id")
	public long getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(long createUserId) {
		this.createUserId = createUserId;
	}
	@Column(name="create_user_name")
	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}
	@Column(name="create_time")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name="passport_status")
	public int getPassportStatus() {
		return passportStatus;
	}

	public void setPassportStatus(int passportStatus) {
		this.passportStatus = passportStatus;
	}
	
	@Column(name="is_submit")
	public int getIsSubmit() {
		return isSubmit;
	}

	public void setIsSubmit(int isSubmit) {
		this.isSubmit = isSubmit;
	}

	@Column(name="uuid")
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	 
	

}
