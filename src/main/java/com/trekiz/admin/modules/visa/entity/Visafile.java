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
 *  文件名: Visafile
 *  功能:
 *  产品Entity
 *  修改记录:   
 *  
 *  @author zj
 *  @DateTime 2014-01-13
 *  @version 1.0
 */
@Entity
@Table(name = "visafile")
public class Visafile extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	                                              
    /** 文件名称 */
    private String fileName;
    /** 附件表Id */
    private Long fileTableId;
    /** 签证基础表id */
    private Integer visabasicsId;
    
    /**
     * 签证表  Id visaProducts
     */
    private Integer visaProductssId;
    
    /**
     * 签证对象ID，对应签证表 visa
     *
     */
    private Long visaId;

	public Integer getVisaProductssId() {
		return visaProductssId;
	}

	public void setVisaProductssId(Integer visaProductssId) {
		this.visaProductssId = visaProductssId;
	}

	public Visafile() {
		super();
	}

	public Visafile(Long id){
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

	public void setFileName(String fileName ){
        this.fileName = fileName ;
    }

    @Length(min=0, max=50)
    public String getFileName(){
        return this.fileName;
    }

    public void setFileTableId(Long fileTableId ){
        this.fileTableId = fileTableId ;
    }

    public Long getFileTableId(){
        return this.fileTableId;
    }

    public void setVisabasicsId(Integer visabasicsId ){
        this.visabasicsId = visabasicsId ;
    }

    public Integer getVisabasicsId(){
        return this.visabasicsId;
    }

	public Long getVisaId() {
		return visaId;
	}

	public void setVisaId(Long visaId) {
		this.visaId = visaId;
	}
}


