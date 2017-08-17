package com.trekiz.admin.modules.sys.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntityTTS;

@Entity
@Table(name="sys_airline_info")
public class AirlineInfo extends DataEntityTTS {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	/**
	 * 所属批发商
	 */
	private Long companyId;
	/**
	 * 地区
	 */
	private Integer area;
	/**
	 * 国家
	 */
	//private Country country;
	
	/**
	 * 国家Id
	 */
	private Long countryId;
	
	/**
	 * 航空公司名称
	 */
	private String airlineName;
	/**
	 * 航空公司二字码
	 */
	private String airlineCode;
	/**
	 * 舱位
	 */
	private String space;
	/**
	 * 舱位等级
	 */
	private String spaceLevel;
	/**
	 * 启用状态
	 */
	private Integer userMode;
	/**
	 * 航班号
	 */
	private String flightnumber;
	/**
	 * 出发时间
	 */
	private Date departuretime;
	/**
	 * 到达时间
	 */
	private Date arrivaltime;
	/**
	 * 天数
	 */
	private Integer dayNum;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="company_id")
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	
	public Integer getArea() {
		return area;
	}
	public void setArea(Integer area) {
		this.area = area;
	}
	
//	@ManyToOne
//	@JoinColumn(name="country_id")
//	@NotFound(action = NotFoundAction.IGNORE)
//	public Country getCountry() {
//		return country;
//	}
//	public void setCountry(Country country) {
//		this.country = country;
//	}
	
	@Column(name="country_id")
	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	
	@Column(name="airline_name")
	public String getAirlineName() {
		return airlineName;
	}
	public void setAirlineName(String airlineName) {
		this.airlineName = airlineName;
	}
	
	@Column(name="airline_code")
	public String getAirlineCode() {
		return airlineCode;
	}
	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
	}
	public String getSpace() {
		return space;
	}
	public void setSpace(String space) {
		this.space = space;
	}
	
	@Column(name="space_level")
	public String getSpaceLevel() {
		return spaceLevel;
	}
	public void setSpaceLevel(String spaceLevel) {
		this.spaceLevel = spaceLevel;
	}
	
	@Column(name="user_mode")
	public Integer getUserMode() {
		return userMode;
	}
	public void setUserMode(Integer userMode) {
		this.userMode = userMode;
	}
	
	@Column(name="flight_number")
	public String getFlightnumber() {
		return flightnumber;
	}
	public void setFlightnumber(String flightnumber) {
		this.flightnumber = flightnumber;
	}
	
	@Column(name="departure_time")
	public Date getDeparturetime() {
		return departuretime;
	}
	public void setDeparturetime(Date departuretime) {
		this.departuretime = departuretime;
	}
	
	@Column(name="arrival_time")
	public Date getArrivaltime() {
		return arrivaltime;
	}
	public void setArrivaltime(Date arrivaltime) {
		this.arrivaltime = arrivaltime;
	}
	@Column(name="day_num")
	public Integer getDayNum() {
		return dayNum;
	}
	public void setDayNum(Integer dayNum) {
		this.dayNum = dayNum;
	}
	
}
