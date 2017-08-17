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

import com.trekiz.admin.common.persistence.DataEntity;

  /**
 *  文件名: Sysdefinedict
 *  功能:
 *  产品Entity
 *  修改记录:   
 *  
 *  @author liangjingming
 *  @DateTime 2014-02-19
 *  @version 1.0
 */
@Entity
@Table(name = "sysdefinedict")
public class SysDefineDict extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	
	                                              
    /** 字典名称 */
    private String label;
    /** 字典值 */
    private String value;
    /** 字典类型 */
    private String type;
    /** 是否关联航空公司二字码 */
    private String defaultFlag;
	/** 字典描述 */
    private String description;
    
    private Integer sort;
    /**  */
    private Long companyId;                                            

	public SysDefineDict() {
		super();
	}

	public SysDefineDict(Long id){
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

	public void setLabel(String label ){
        this.label = label ;
    }

    @Length(min=0, max=100)
    public String getLabel(){
        return this.label;
    }

    public void setValue(String value ){
        this.value = value ;
    }

    @Length(min=0, max=50)
    public String getValue(){
        return this.value;
    }

    public void setType(String type ){
        this.type = type ;
    }

    @Length(min=0, max=20)
    public String getType(){
        return this.type;
    }
    @Length(min=0, max=1)
    public String getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}

    public void setDescription(String description ){
        this.description = description ;
    }

    @Length(min=0, max=100)
    public String getDescription(){
        return this.description;
    }
    
    public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public void setCompanyId(Long companyId ){
        this.companyId = companyId ;
    }

    public Long getCompanyId(){
        return this.companyId;
    }
	
}


