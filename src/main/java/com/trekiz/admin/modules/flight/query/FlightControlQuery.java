package com.trekiz.admin.modules.flight.query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FlightControlQuery {
	private String country;//国家
	private Date startDepartureDate;//开始日期
	private Date endDepartureDate;//结束日期
	private String name;//控票名称
	private String islandUuid;//岛屿名称
	private String airline;//航空公司
	private Integer spaceGradeType;//舱位等级
	private Double startPrice;//最低价格
	private Double endPrice;//最高价格
	private Integer startStock;//最少库存
	private Integer endStock;//最多库存
	private String hotelUuid;//参考酒店
	private Integer status;//状态
	private String createUser;//发布人
	private String updateUser;//更新人
	private Integer showType;//展示类型
	
	private static final String FORMAT_DATE = "yyyy-MM-dd";
	
	private String date2String(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	private Date string2Date(String dateStr,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return  sdf.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Date getStartDepartureDate() {
		return startDepartureDate;
	}
	public void setStartDepartureDate(Date startDepartureDate) {
		this.startDepartureDate = startDepartureDate;
	}
	public Date getEndDepartureDate() {
		return endDepartureDate;
	}
	public void setEndDepartureDate(Date endDepartureDate) {
		this.endDepartureDate = endDepartureDate;
	}
	public String getStartDepartureDateString() {
		if(getStartDepartureDate() != null) {
			return this.date2String(getStartDepartureDate(), FORMAT_DATE);
		} else {
			return null;
		}
	}
	public void setStartDepartureDateString(String startDepartureDateString) {
		setStartDepartureDate(this.string2Date(startDepartureDateString, FORMAT_DATE));
	}
	
	public String getEndDepartureDateString() {
		if(getEndDepartureDate() != null) {
			return this.date2String(getEndDepartureDate(), FORMAT_DATE);
		} else {
			return null;
		}
	}
	public void setEndDepartureDateString(String endDepartureDateString) {
		setEndDepartureDate(this.string2Date(endDepartureDateString, FORMAT_DATE));
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIslandUuid() {
		return islandUuid;
	}
	public void setIslandUuid(String islandUuid) {
		this.islandUuid = islandUuid;
	}
	public Integer getSpaceGradeType() {
		return spaceGradeType;
	}
	public void setSpaceGradeType(Integer spaceGradeType) {
		this.spaceGradeType = spaceGradeType;
	}
	public Double getStartPrice() {
		return startPrice;
	}
	public void setStartPrice(Double startPrice) {
		this.startPrice = startPrice;
	}
	public Double getEndPrice() {
		return endPrice;
	}
	public void setEndPrice(Double endPrice) {
		this.endPrice = endPrice;
	}
	public Integer getStartStock() {
		return startStock;
	}
	public void setStartStock(Integer startStock) {
		this.startStock = startStock;
	}
	public Integer getEndStock() {
		return endStock;
	}
	public void setEndStock(Integer endStock) {
		this.endStock = endStock;
	}
	public String getHotelUuid() {
		return hotelUuid;
	}
	public void setHotelUuid(String hotelUuid) {
		this.hotelUuid = hotelUuid;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
	public String getAirline() {
		return airline;
	}

	public void setAirline(String airline) {
		this.airline = airline;
	}

	public Integer getShowType() {
		return showType;
	}
	public void setShowType(Integer showType) {
		this.showType = showType;
	}

	
	
}
