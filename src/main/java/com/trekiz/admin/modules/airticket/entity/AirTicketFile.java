/**
 *
 */
package com.trekiz.admin.modules.airticket.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.validator.constraints.Length;

import com.trekiz.admin.common.persistence.DataEntity;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.sys.entity.DocInfo;

  /**
 *  文件名: Activityfile
 *  功能:
 *  产品Entity
 *  修改记录:   
 *  
 *  @author liangjingming
 *  @DateTime 2014-01-13
 *  @version 1.0
 */
@Entity
@Table(name = "airTicketFile")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AirTicketFile extends DataEntity {
	
	/**上传报名表类型*/
	public static Integer  SIGN_UP_TYPE = 1;
	
	private static final long serialVersionUID = 1L;
	
	private Long id; 		// 编号
	
    /**
     *  机票产品信息表ID外键  
     * add by xiaojun 
     * 2014-009-19
     * 
     * */
    private Long airticketId;
    
    /** 文件上传附件表ID外键 */
    private Long docId;
    
	@ContainedIn
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "airticketId", referencedColumnName = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	public ActivityAirTicket getActivityAirTicket() {
		return activityAirTicket;
	}

	public void setActivityAirTicket(ActivityAirTicket activityAirTicket) {
		this.activityAirTicket = activityAirTicket;
	}

    @Column(name="airticketId",unique=false,nullable=false,insertable=false,updatable=false)
    public Long getAirticketId() {
		return airticketId;
	}

	public void setAirticketId(Long airticketId) {
		this.airticketId = airticketId;
	}

	@Column(name="docId",unique=false,nullable=false,insertable=false,updatable=false)
	public Long getDocId() {
		return docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}

	/** 上传的文件名称 */
    private String fileName;
    /** 上传的文件资料类型 */
    private Integer fileType;
    
    private DocInfo docInfo;
    
    private ActivityAirTicket activityAirTicket;


	public AirTicketFile() {
		super();
	}

	public AirTicketFile(Long id){
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


    public void setFileName(String fileName ){
        this.fileName = fileName ;
    }

    @Length(min=0, max=50)
    @Column(name="fileName",unique=false,nullable=false)
    public String getFileName(){
        return this.fileName;
    }

    public void setFileType(Integer fileType ){
        this.fileType = fileType ;
    }

    @Column(name="fileType",unique=false,nullable=false)
    public Integer getFileType(){
        return this.fileType;
    }

	
	@OneToOne(cascade=CascadeType.ALL,optional=false,fetch=FetchType.LAZY)
	@JoinColumn(name="docId",referencedColumnName="id")
	@NotFound(action=NotFoundAction.IGNORE)
    public DocInfo getDocInfo() {
		return docInfo;
	}

	public void setDocInfo(DocInfo docInfo) {
		this.docInfo = docInfo;
	}
}


