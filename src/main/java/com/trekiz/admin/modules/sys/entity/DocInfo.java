/**
 *
 */
package com.trekiz.admin.modules.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.Length;

import com.trekiz.admin.common.persistence.DataEntity;
import com.trekiz.admin.common.utils.StringUtils;

  /**
 *  文件名: Docinfo
 *  功能:
 *  产品Entity
 *  修改记录:   
 *  
 *  @author liangjingming
 *  @DateTime 2014-01-13
 *  @version 1.0
 */
@Entity
@Table(name = "docinfo")
public class DocInfo extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	
    /** 上传的文件名称 */
    private String docName;
    /** 存放文件的路径 */
    private String docPath;
    /** 上传的文件类型 */
    private Integer docType;
    /** 对应支付项ID */
    private Long payOrderId;
	/**  */
//    private Integer createBy;
//    /**  */
//    private Date createDate;
//    /**  */
//    private Integer updateBy;
//    /**  */
//    private Date updateDate;
//    /**  */
//    private String delFlag;
//    /**  */
//    private String remarks;
//=======================================================
                                             
    private String elseFileName;

	public DocInfo() {
		super();
	}

	public DocInfo(Long id){
		this();
		this.id = id;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_docinfo")
	//@SequenceGenerator(name = "seq_docinfo", sequenceName = "seq_docinfo")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public void setDocName(String docName ){
	    if(StringUtils.isNotBlank(docName)&&docName.length()>255){
	        docName = StringUtils.substring(docName, 0, 254);
	    }
        this.docName = docName ;
    }

    @Length(min=0, max=255)
    @Column(name="docName",unique=false,nullable=false)
    public String getDocName(){
        return this.docName;
    }

    public void setDocPath(String docPath ){
        this.docPath = docPath ;
    }

    @Length(min=0, max=500)
    @Column(name="docPath",unique=false,nullable=false)
    public String getDocPath(){
        return this.docPath;
    }

    public void setDocType(Integer docType ){
        this.docType = docType ;
    }

    @Column(name="docType",unique=false,nullable=false)
    public Integer getDocType(){
        return this.docType;
    }
    
    @Column(name="payOrderId",unique=false,nullable=true)
    public Long getPayOrderId() {
		return payOrderId;
	}

	public void setPayOrderId(Long payOrderId) {
		this.payOrderId = payOrderId;
	}
	
	@Transient
	public String getElseFileName() {
		return elseFileName;
	}

	public void setElseFileName(String elseFileName) {
		this.elseFileName = elseFileName;
	}

}


