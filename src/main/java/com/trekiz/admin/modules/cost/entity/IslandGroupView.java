package com.trekiz.admin.modules.cost.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntity;

@Entity
@Table(name = "island_group_view")
public class IslandGroupView extends DataEntity  {

	
	private static final long serialVersionUID = 1L;
    private Long id;	    
    private String uuid;   
    private String islandUuid; 
    private String activityIslandUuid; 
	private String hotelUuid;
    private String activityName;
    private Long wholesalerId;
    private String groupCode;
    private String hotelName;
	private Date groupOpenDate;
	
	private Date groupEndDate;
	
	private String status;
	

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getUuid() {
		return uuid;
	}


	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public String getActivityName() {
		return activityName;
	}


	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}


	public Date getGroupOpenDate() {
		return groupOpenDate;
	}


	public void setGroupOpenDate(Date groupOpenDate) {
		this.groupOpenDate = groupOpenDate;
	}


	public Date getGroupEndDate() {
		return groupEndDate;
	}


	public void setGroupEndDate(Date groupEndDate) {
		this.groupEndDate = groupEndDate;
	}

	
	public String getHotelUuid() {
		return hotelUuid;
	}

	public void setHotelUuid(String hotelUuid) {
		this.hotelUuid = hotelUuid;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}


	public String getIslandUuid() {
		return islandUuid;
	}

	public void setIslandUuid(String islandUuid) {
		this.islandUuid = islandUuid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getActivityIslandUuid() {
		return activityIslandUuid;
	}

	public void setActivityIslandUuid(String activityIslandUuid) {
		this.activityIslandUuid = activityIslandUuid;
	}	
	
	public Long getWholesalerId() {
		return wholesalerId;
	}

	public void setWholesalerId(Long wholesalerId) {
		this.wholesalerId = wholesalerId;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}	
	
	
		
}
