/**
 *
 */
package com.trekiz.admin.modules.activity.entity;

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
import org.hibernate.search.annotations.ContainedIn;
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
@Table(name = "activityfile")
public class ActivityFile extends DataEntity {
	
	/**行程介绍产品类型*/
	public static Integer INTRODUCTION_TYPE = 1;
	/**自费补充协议文件类型*/
	public static Integer COSTAGREEMENT_TYPE = 2;
	/**其他补充协议文件类型*/
	public static Integer OTHERAGREEMENT_TYPE = 3;
	/**其他文件类型*/
	public static Integer OTHER_TYPE = 5;
	/**微信分销广告图片 添加产品图片类型 yang.gao 2017.01.06 */
	public static Integer DISTRIBUTION_TYPE = 6;
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	
    /** 产品信息表ID外键 */
    private Integer srcActivityId;
    
    /**
     *  机票产品信息表ID外键  
     * add by xiaojun 
     * 2014-009-19
     * 
     * */
    private Integer srcAirticketId;
   
	@Column(name="srcAirticketId",unique=false,nullable=false,insertable=false,updatable=false)
    public Integer getSrcAirticketId() {
		return srcAirticketId;
	}

	public void setSrcAirticketId(Integer srcAirticketId) {
		this.srcAirticketId = srcAirticketId;
	}

	@ContainedIn
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "srcAirticketId", referencedColumnName = "id")
	@NotFound(action = NotFoundAction.IGNORE)
	public ActivityAirTicket getActivityAirTicket() {
		return activityAirTicket;
	}

	public void setActivityAirTicket(ActivityAirTicket activityAirTicket) {
		this.activityAirTicket = activityAirTicket;
	}

	/** 文件上传附件表ID外键 */
    private Integer srcDocId;
    /** 上传的文件名称 */
    private String fileName;
    /** 上传的文件资料类型 */
    private Integer fileType;
    
    private TravelActivity travelActivity;
    
    
    private DocInfo docInfo;
    
    private ActivityAirTicket activityAirTicket;
    
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
                                              


	public ActivityFile() {
		super();
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false)
	//@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_activityfile")
	//@SequenceGenerator(name = "seq_activityfile", sequenceName = "seq_activityfile")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setSrcActivityId(Integer srcActivityId ){
        this.srcActivityId = srcActivityId ;
    }

	@Column(name="srcActivityId",unique=false,nullable=false,insertable=false,updatable=false)
    public Integer getSrcActivityId(){
        return this.srcActivityId;
    }

    public void setSrcDocId(Integer srcDocId ){
        this.srcDocId = srcDocId ;
    }

    @Column(name="srcDocId",unique=false,nullable=false,insertable=false,updatable=false)
    public Integer getSrcDocId(){
        return this.srcDocId;
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

    @ContainedIn
    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="srcActivityId",referencedColumnName="id")
	@NotFound(action=NotFoundAction.IGNORE)
	public TravelActivity getTravelActivity() {
		return travelActivity;
	}

	public void setTravelActivity(TravelActivity travelActivity) {
		this.travelActivity = travelActivity;
	}
	
	
	
	@OneToOne(cascade=CascadeType.ALL,optional=false,fetch=FetchType.LAZY)
	@JoinColumn(name="srcDocId",referencedColumnName="id")
	@NotFound(action=NotFoundAction.IGNORE)
    public DocInfo getDocInfo() {
		return docInfo;
	}

	public void setDocInfo(DocInfo docInfo) {
		this.docInfo = docInfo;
	}

	
}


