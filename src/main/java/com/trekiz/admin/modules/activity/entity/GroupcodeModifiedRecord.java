package com.trekiz.admin.modules.activity.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

/**  
 * @Title: GroupcodeModifiedRecord.java
 * @Package com.trekiz.admin.modules.activity.entity
 * @Description: TODO(用一句话描述该文件做什么)
 * @author xinwei.wang  
 * @date 2015-2015年11月25日 下午5:18:03
 * @version V1.0  
 */
@Entity
@Table(name = "groupcode_modified_record")
public class GroupcodeModifiedRecord  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Integer id;
    private String groupcodeOld; //'修改前旧团号',
    private String groupcodeNew; //'修改后新团号',
    private Integer productType;// '产品类型，与系统产品类型保持一致',
    private Integer productId; // '产品id',
    private Integer activityGroupId; // '团期id',
    private Date createDate;// '修改团号时间',
    private Integer createBy; //'修改人',
    private String  updateByName;//'修改人姓名',
    private Integer delFlag = 0;//'删除标记：0正常；1删除',
    
	@PrePersist
	public void prePersist() {
		if (createDate == null) {
			this.createDate = new Date();
		}
	}
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "group_code_old", nullable = false)
	public String getGroupcodeOld() {
		return groupcodeOld;
	}
	public void setGroupcodeOld(String groupcodeOld) {
		this.groupcodeOld = groupcodeOld;
	}
	@Column(name = "group_code_new", nullable = false)
	public String getGroupcodeNew() {
		return groupcodeNew;
	}
	public void setGroupcodeNew(String groupcodeNew) {
		this.groupcodeNew = groupcodeNew;
	}
	@Column(name = "product_type", nullable = false)
	public Integer getProductType() {
		return productType;
	}
	public void setProductType(Integer productType) {
		this.productType = productType;
	}
	@Column(name = "product_id",  nullable = false)
	public Integer getProductId() {
		return productId;
	}
	
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	@Column(name = "create_date", nullable = false)
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Column(name = "update_by_name", nullable = true)
	public String getUpdateByName() {
		return updateByName;
	}
	public void setUpdateByName(String updateByName) {
		this.updateByName = updateByName;
	}
	@Column(name = "activity_group_id", nullable = true)
	public Integer getActivityGroupId() {
		return activityGroupId;
	}
	public void setActivityGroupId(Integer activityGroupId) {
		this.activityGroupId = activityGroupId;
	}

	@Column(name = "create_by", nullable = false)
	public Integer getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}

	@Column(name = "del_flag", nullable = true)
	public Integer getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	
	@Override
	public String toString() {
		return "GroupcodeModifiedRecord [id=" + id + ", groupcodeOld=" + groupcodeOld + ", groupcodeNew=" + groupcodeNew
				+ ", productType=" + productType + ", productId=" + productId + ", activityGroupId=" + activityGroupId
				+ ", create_date=" + createDate + ", createBy=" + createBy + ", update_by_name=" + updateByName
				+ ", delFlag=" + delFlag + "]";
	}
	
	  
}
