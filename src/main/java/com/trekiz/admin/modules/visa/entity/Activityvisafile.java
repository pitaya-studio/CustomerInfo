/**
 *
 */
package com.trekiz.admin.modules.visa.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.trekiz.admin.common.persistence.DataEntity;


 /**
 *  文件名: Activityvisafile.java
 *  功能:
 *          产品签证信息附件表
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2014-1-14 上午10:43:10
 *  @version 1.0
 */
@Entity
@Table(name = "activityvisafile")
public class Activityvisafile extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	
	                                              
    /** 产品id */
    private Long srcActivityId;
    /** 签证类型 */
    private Integer visaType;
    /** 国家id */
    private Long countryId;
    /** 国家名称 */
    private String countryName;
    /** 附件表id */
    private Long srcDocId;

	public Activityvisafile() {
		super();
	}

	public Activityvisafile(Long id){
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

	    public void setSrcActivityId(Long srcActivityId ){
        this.srcActivityId = srcActivityId ;
    }

    public Long getSrcActivityId(){
        return this.srcActivityId;
    }

    public void setVisaType(Integer visaType ){
        this.visaType = visaType ;
    }

    public Integer getVisaType(){
        return this.visaType;
    }

    public void setCountryId(Long countryId ){
        this.countryId = countryId ;
    }

    public Long getCountryId(){
        return this.countryId;
    }

    public void setCountryName(String countryName ){
        this.countryName = countryName ;
    }

    @Length(min=0, max=50)
    public String getCountryName(){
        return this.countryName;
    }

    public void setSrcDocId( Long srcDocId ){
        this.srcDocId = srcDocId ;
    }

    public Long getSrcDocId(){
        return this.srcDocId;
    }
}


