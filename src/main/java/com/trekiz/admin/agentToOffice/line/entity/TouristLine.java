package com.trekiz.admin.agentToOffice.line.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.persistence.BaseEntity;

/**
 * 游玩线路实体类
 * @author yang.wang@quauq.com
 * @Date 2016-10-13
 */
@Entity
@Table(name = "tourist_line")
@DynamicInsert(true)
@DynamicUpdate(true)
public class TouristLine extends BaseEntity{

	private static final long serialVersionUID = 1L;
	
	// alias
	public static final String TABLE_ALIAS = "PriceStrategyLine";
	public static final String ALIAS_ID = "主键";
	public static final String ALIAS_LINENAME = "线路名称";
	public static final String ALIAS_AREA_IDS = "所含区域ids";
	public static final String ALIAS_DESTINATION_IDS = "目的地ids";
	public static final String ALIAS_UUID = "uuid";
	public static final String ALIAS_COMPANY_ID = "companyId";
	public static final String ALIAS_CREATE_BY = "createBy";
	public static final String ALIAS_CREATE_DATE = "createDate";
	public static final String ALIAS_UPDATE_BY = "updateBy";
	public static final String ALIAS_UPDATE_DATE = "updateDate";
	public static final String ALIAS_DEL_FLAG = "delFlag";
	
	// date formats
	public static final String FORMAT_CREATE_DATE = "yyyy-MM-dd";
	public static final String FORMAT_UPDATE_DATE = "yyyy-MM-dd";
	
	// column
	private Long id;				// 主键
	private String lineName;		// 线路名称
	private String areaIds;			// 区域id集合（id1,id2,...）
	private String destinationIds;	// 目的地id集合，数据形式同areaIds
	private String uuid;			// uuid
	private Integer companyId;		// 批发商id
	private Long createBy;
	private Date createDate;
	private Long updateBy;
	private Date updateDate;
	private String delFlag;
	
	// constructor 
	public TouristLine() {
		super();
	}
	
	public TouristLine(Long id) {
		super();
		this.id = id;
	}
	
	// setter and getter
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="line_name")
	public String getLineName() {
		return lineName;
	}
	
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	
	@Column(name = "area_ids")
	public String getAreaIds() {
		return areaIds;
	}

	public void setAreaIds(String areaIds) {
		this.areaIds = areaIds;
	}

	@Column(name = "destination_ids")
	public String getDestinationIds() {
		return destinationIds;
	}

	public void setDestinationIds(String destinationIds) {
		this.destinationIds = destinationIds;
	}

	@Column(name = "uuid")
	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Column(name="company_id")
	public Integer getCompanyId() {
		return companyId;
	}
	
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	
	@Column(name="createBy")
	public Long getCreateBy() {
		return createBy;
	}
	
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	
	@Column(name="createDate")
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	@Column(name="updateBy")
	public Long getUpdateBy() {
		return updateBy;
	}
	
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}
	
	@Column(name="updateDate")
	public Date getUpdateDate() {
		return updateDate;
	}
	
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	@Column(name="delFlag")
	public String getDelFlag() {
		return delFlag;
	}
	
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
}
