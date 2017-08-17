package com.trekiz.admin.modules.traveler.entity;

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
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.validator.constraints.Length;
import com.trekiz.admin.common.persistence.DataEntity;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.traveler.entity.Traveler;

  /**
 *  文件名: TravelerFile
 *  功能:
 *  产品Entity
 *  修改记录:   
 *  
 *  @author taoxiaoyang
 *  @DateTime 2014-10-20
 *  @version 1.0
 */
@Entity
@Table(name = "travelerfile")
public class TravelerFile extends DataEntity {
	
	/**护照类型*/
	public static Integer PASSPORTS_TYPE = 1;
	/**电子相片类型*/
	public static Integer PHOTO_TYPE = 2;
	/**身份证正面类型*/
	public static Integer IDCARD_FRONT_TYPE = 3;
	/**身份证反面类型*/
	public static Integer IDCARD_BACK_TYPE = 4;
	/**申请表格类型*/
	public static Integer ENTRY_FORM_TYPE = 5;
	/**房产证*/
	public static Integer HOUSE_TYPE = 6;
	/**户口本*/
	public static Integer RESIDENCE_TYPE = 7;
	/**其他文件类型*/
	public static Integer OTHER_TYPE = 0;
	/**签证附件*/
	public static Integer VISA_TYPE = 8;
	/**自备签：护照首页*/
	public static Integer FILETYPE_ZBQ_PASSPORT_HOMEPAGE = 9;
	/**自备签：签证页*/
	public static Integer FILETYPE_ZBQ_VISAPAGE = 10;
	/**自备签：其他*/
	public static Integer FILETYPE_ZBQ_OTHER = 11;
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	
    /** 游客信息表ID外键 */
    private Long srcTravelerId;
    
	/** 文件上传附件表ID外键 */
    private Long srcDocId;
    /** 上传的文件名称 */
    private String fileName;
    /** 上传的文件资料类型 */
    private Integer fileType;
    
    private Traveler traveler;
    
    private DocInfo docInfo;
    

	public TravelerFile() {
		super();
	}

	public TravelerFile(Long id){
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

	public void setSrcTravelerId(Long srcTravelerId ){
        this.srcTravelerId = srcTravelerId ;
    }

	@Column(name="srcTravelerId",unique=false,nullable=false,insertable=false,updatable=false)
    public Long getSrcTravelerId(){
        return this.srcTravelerId;
    }

    public void setSrcDocId(Long srcDocId ){
        this.srcDocId = srcDocId ;
    }

    @Column(name="srcDocId",unique=false,nullable=false,insertable=false,updatable=false)
    public Long getSrcDocId(){
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

    @ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="srcTravelerId",referencedColumnName="id")
	@NotFound(action=NotFoundAction.IGNORE)
	public Traveler getTraveler() {
		return traveler;
	}

	public void setTraveler(Traveler traveler) {
		this.traveler = traveler;
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


