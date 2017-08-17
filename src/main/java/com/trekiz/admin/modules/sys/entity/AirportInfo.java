package com.trekiz.admin.modules.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntityTTS;

@Entity
@Table(name = "sys_airport_info")
public class AirportInfo extends DataEntityTTS {


	private static final long serialVersionUID = 1L;

	
	private Long id;
	/**
	 * 所属批发商
	 */
	private Long companyId;
	/**
	 * 区域，目前只有1：国内、2：国外两种
	 */
	private Integer area;
	/**
	 * 国家
	 */
//	private Country country;
	
	/**
	 * 国家Id
	 */
	private Long countryId;
	
	/**
	 * 城市Id
	 */
	private Long cityId;
	/**
	 * 机场名称
	 */
	private String airportName;
	/**
	 * 机场三字码
	 */
	private String airportCode;
	/**
	 * 启用状态
	 */
	private Integer userMode;

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

	//@ManyToOne
	//@JoinColumn(name="country_id")
	//@NotFound(action = NotFoundAction.IGNORE)
//	public Country getCountry() {
//		return country;
//	}
//
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

	@Column(name="city_id")
	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	@Column(name="airport_name")
	public String getAirportName() {
		return airportName;
	}

	public void setAirportName(String airportName) {
		this.airportName = airportName;
	}

	@Column(name="airport_code")
	public String getAirportCode() {
		return airportCode;
	}

	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

	@Column(name="user_mode")
	public Integer getUserMode() {
		return userMode;
	}

	public void setUserMode(Integer userMode) {
		this.userMode = userMode;
	}
}
