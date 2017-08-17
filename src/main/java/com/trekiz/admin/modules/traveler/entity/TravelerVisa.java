/**
 *
 */
package com.trekiz.admin.modules.traveler.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Indexed;

import com.trekiz.admin.common.persistence.DataEntity;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Dict;

  /**
 *  文件名: TravelerVisa
 *  功能:
 *  游客自备签信息Entity
 *  修改记录:   
 *  
 *  @author taoxiaoyang
 *  @DateTime 2014-10-20
 *  @version 1.0
 */
@Entity
@Table(name = "travelervisa")
public class TravelerVisa extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Long travelerId;
    /** 游客信息 */
    private Traveler traveler;
    /** 申请国家 Area */
    private Area applyCountry;
    /** 申请国家 Country */
    private Country applyCountryNew;
    /** 国家Id */
    private Long sysCountryId;
    /** 领地 */
    private Integer manorId;
 	/* 领区名称*/
    private String manorName;
    /** 签证类型 */
    private Integer visaTypeId;
    /** 预计出团时间 */
	private Date groupOpenDate;
	/** 预计回团时间 */
//	private Date groupBackDate;
	/** 预计约签时间 */
    private Date contractDate;

    /** 自备签类型 0：代表没有 1：代表自备 */
    private Integer zbqType;
    /** 签证到期时间*/
    private Date visaDate;
    /** 领地 */
    private List<Dict> manorList;
	/** 签证类型 */
    private List<Dict> visaTypeList;
    
	public TravelerVisa() {
		super();
	}

	public TravelerVisa(Long id){
		this();
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="travelerId",unique=false,nullable=false,insertable=false,updatable=false)
	public Long getTravelerId() {
		return travelerId;
	}

	public void setTravelerId(Long travelerId) {
		this.travelerId = travelerId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travelerId", referencedColumnName = "id")
    @NotFound(action = NotFoundAction.IGNORE)
	public Traveler getTraveler() {
		return traveler;
	}

	public void setTraveler(Traveler traveler) {
		this.traveler = traveler;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applyCountryId", referencedColumnName = "id")
    @NotFound(action = NotFoundAction.IGNORE)
	public Area getApplyCountry() {
		return applyCountry;
	}

	public void setApplyCountry(Area applyCountry) {
		this.applyCountry = applyCountry;
	}
	
	@Transient
	public Long getSysCountryId() {
		return sysCountryId;
	}

	public void setSysCountryId(Long sysCountryId) {
		this.sysCountryId = sysCountryId;
	}

	public Integer getManorId() {
		return manorId;
	}

	public void setManorId(Integer manorId) {
		this.manorId = manorId;
	}

    public String getManorName() {
	    return manorName;
    }

    public void setManorName(String manorName) {
	    this.manorName = manorName;
    }

    public Integer getVisaTypeId() {
    	return visaTypeId;
	}

	public void setVisaTypeId(Integer visaTypeId) {
		this.visaTypeId = visaTypeId;
	}

	public Date getGroupOpenDate() {
		return groupOpenDate;
	}

	public void setGroupOpenDate(Date groupOpenDate) {
		this.groupOpenDate = groupOpenDate;
	}
	
//	public Date getGroupBackDate() {
//		return groupBackDate;
//	}
//
//	public void setGroupBackDate(Date groupBackDate) {
//		this.groupBackDate = groupBackDate;
//	}

	public Date getContractDate() {
		return contractDate;
	}

	public void setContractDate(Date contractDate) {
		this.contractDate = contractDate;
	}
	  /*@Column(name="travelervisa_deposit")
	  public Integer getDepositValue() {
		  return depositValue;
	  }

	  public void setDepositValue(Integer depositValue) {
		  this.depositValue = depositValue;
	  }
	  @Column(name="travelervisa_datum")
	  public Integer getDatumValue() {
		  return datumValue;
	  }

	  public void setDatumValue(Integer datumValue) {
		  this.datumValue = datumValue;
	  }*/

	  public Integer getZbqType() {
		return zbqType;
	}

	public void setZbqType(Integer zbqType) {
		this.zbqType = zbqType;
	}

	public Date getVisaDate() {
		return visaDate;
	}

	public void setVisaDate(Date visaDate) {
		this.visaDate = visaDate;
	}
	
	@Transient
	public List<Dict> getManorList() {
		return manorList;
	}

	public void setManorList(List<Dict> manorList) {
		this.manorList = manorList;
	}
	
	@Transient
	public List<Dict> getVisaTypeList() {
		return visaTypeList;
	}

	public void setVisaTypeList(List<Dict> visaTypeList) {
		this.visaTypeList = visaTypeList;
	}

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applyCountryId1", referencedColumnName = "id",insertable=true,updatable=true)
    @NotFound(action = NotFoundAction.IGNORE)
	public Country getApplyCountryNew() {
		return applyCountryNew;
	}

	public void setApplyCountryNew(Country applyCountryNew) {
		this.applyCountryNew = applyCountryNew;
	}

}


