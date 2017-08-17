package com.trekiz.admin.modules.wholesalerbase.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.trekiz.admin.common.persistence.DataEntity;

/**
 * 批发商资质附件关联表
 * @author gao
 *  2015年4月13日
 */
@Entity
@Table(name = "sys_office_qualifications")
@DynamicInsert @DynamicUpdate
public class Qualifications extends DataEntity{

	/**
	 * serialVersionUID
	 * @author gao
	 * 2015年4月13日
	 */
	private static final long serialVersionUID = 1L;
	/**  营业执照 */
	public static final Integer TITLE_TYPE_1 = 1;
	/**  经营许可证 */
	public static final Integer TITLE_TYPE_2 = 2;
	/** 税务登记证  */
	public static final Integer TITLE_TYPE_3 = 3;
	/** 组织机构代码证  */
	public static final Integer TITLE_TYPE_4 = 4;
	/** 公司法人身份证（正反面在一起）  */
	public static final Integer TITLE_TYPE_5 = 5;
	/** 公司银行开户许可证  */
	public static final Integer TITLE_TYPE_6 = 6;
	/**  旅游业资质 */
	public static final Integer TITLE_TYPE_7 = 7;
	/**  其他文件 */
	public static final Integer TITLE_TYPE_8 = 8;
	
	// 主键
	private Long id;
	// 附件表ID
	private Long docInfoId;
	// 附件名称
	private String docInfoName;
	// 附件地址
	private String docInfoPath;
	// 资质附件类别名称
	private String title;
	// 资质附件类别代码
	private Integer titleType;
	// UUID
	private String uuid;
	// 对应批发商ID
	private Long companyId;
	public Qualifications(){
		super();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="docinfo_id")
	public Long getDocInfoId() {
		return docInfoId;
	}

	public void setDocInfoId(Long docInfoId) {
		this.docInfoId = docInfoId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name="title_type")
	public Integer getTitleType() {
		return titleType;
	}

	public void setTitleType(Integer titleType) {
		this.titleType = titleType;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Column(name="company_id")
	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	@Column(name="docinfo_name")
	public String getDocInfoName() {
		return docInfoName;
	}

	public void setDocInfoName(String docInfoName) {
		this.docInfoName = docInfoName;
	}

	@Column(name="docinfo_path")
	public String getDocInfoPath() {
		return docInfoPath;
	}

	public void setDocInfoPath(String docInfoPath) {
		this.docInfoPath = docInfoPath;
	}
	
}
