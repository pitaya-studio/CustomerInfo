/**
 *
 */
package com.trekiz.admin.modules.sys.entity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.trekiz.admin.common.persistence.BaseEntity;


 /**
 *  文件名: Country.java
 *  功能:
 *  
 *  修改记录:   
 *          国家表
 *  @author xuziqian
 *  @DateTime 2014-1-13 下午7:56:38
 *  @version 1.0
 */
@Entity
@Table(name = "sys_country")
public class Country extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 主键
	
	                                              
    /**  */
    private String cc_fips;
    /**  */
    private String cc_iso;
    /**  */
    private String tld;
    /** 国家名英文 */
    private String countryName;
    /**  */
    private Integer continent;
    /**  */
    private Integer isHotel;
    /**  国家名中文*/
    private String countryName_cn;
    /** 所属洲 */
    private String continentName;
    /**  */
    private Integer displayStatus;
//=======================================================
                                              


	public Country() {
		super();
	}

	public Country(Long id){
		this();
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	    public void setCc_fips(String cc_fips ){
        this.cc_fips = cc_fips ;
    }

    @Length(min=0, max=2)
    public String getCc_fips(){
        return this.cc_fips;
    }

    public void setCc_iso(String cc_iso ){
        this.cc_iso = cc_iso ;
    }

    @Length(min=0, max=2)
    public String getCc_iso(){
        return this.cc_iso;
    }

    public void setTld(String tld ){
        this.tld = tld ;
    }

    @Length(min=0, max=3)
    public String getTld(){
        return this.tld;
    }

    public void setCountryName(String countryName ){
        this.countryName = countryName ;
    }

    @Length(min=0, max=100)
    public String getCountryName(){
        return this.countryName;
    }

    public void setContinent(Integer continent ){
        this.continent = continent ;
    }

    public Integer getContinent(){
        return this.continent;
    }

    public void setIsHotel(Integer isHotel ){
        this.isHotel = isHotel ;
    }

    public Integer getIsHotel(){
        return this.isHotel;
    }

    public void setCountryName_cn(String countryName_cn ){
        this.countryName_cn = countryName_cn ;
    }

    @Length(min=0, max=100)
    public String getCountryName_cn(){
        return this.countryName_cn;
    }

    public void setContinentName(String continentName ){
        this.continentName = continentName ;
    }

    @Length(min=0, max=255)
    public String getContinentName(){
        return this.continentName;
    }

    public void setDisplayStatus(Integer displayStatus ){
        this.displayStatus = displayStatus ;
    }

    public Integer getDisplayStatus(){
        return this.displayStatus;
    }

}


