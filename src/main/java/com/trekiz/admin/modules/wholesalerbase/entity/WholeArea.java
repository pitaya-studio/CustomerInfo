package com.trekiz.admin.modules.wholesalerbase.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.persistence.DataEntity;
/**
 * 批发商覆盖区域
 * @author gao
 *  2015年4月20日
 */
@Entity
 @Table(name = "sys_office_area")
 @DynamicInsert @DynamicUpdate
public class WholeArea extends DataEntity {

	/**
	 * serialVersionUID
	 * @author gao
	 * 2015年4月20日
	 */
	private static final long serialVersionUID = 1L;
	/** 所属区域在境内*/
	public final static Integer AREA_INTERNAL = 1; 
	/** 所属区域在境外*/
	public final static Integer AREA_OVERSEAS = 2; 
	private Long id;
	private Long companyID; // 所属公司ID
	private Integer frontier;	// 1:境内；2：境外
	private Long areaid; // 地区ID
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="company_id")
	public Long getCompanyID() {
		return companyID;
	}
	public void setCompanyID(Long companyID) {
		this.companyID = companyID;
	}
	public Integer getFrontier() {
		return frontier;
	}
	public void setFrontier(Integer frontier) {
		this.frontier = frontier;
	}
	@Column(name="area_id")
	public Long getAreaid() {
		return areaid;
	}
	public void setAreaid(Long areaid) {
		this.areaid = areaid;
	}
}
