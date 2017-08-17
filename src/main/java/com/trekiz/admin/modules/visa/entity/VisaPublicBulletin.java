package com.trekiz.admin.modules.visa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntity;

/**
 * 签证公告实体类
 * @author xinwei.wang
 */
@Entity
@Table(name = "visa_public_bulletin")
public class VisaPublicBulletin extends DataEntity {
	
	private static final long serialVersionUID = 1L;

	/** 主键ID */
	private Long id;
	
	/** 签证公告标题 */
	private String  title;
	
	/** 签证公告内容 */
	private String  content;
	
	/** 公告的机构ID */
	private Long  companyId;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
    
}
