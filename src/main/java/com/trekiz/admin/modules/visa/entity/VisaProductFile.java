package com.trekiz.admin.modules.visa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntityTTS;

/**
 *  文件名: VisaProductFile
 *  功能:
 *  签证产品文件
 *  修改记录:   
 *  
 *  @author jiachen
 *  @DateTime 2014-11-19
 *  @version 1.0
 */
@Entity
@Table(name="visa_product_file")
public class VisaProductFile extends DataEntityTTS {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	/**	签证产品信息表ID外键*/
	private Long srcVisaProductId;
	/** 文件上传附件表ID外键*/
	private Long docInfo;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="visa_product_id")
	public Long getSrcVisaProductId() {
		return srcVisaProductId;
	}
	public void setSrcVisaProductId(Long srcVisaProductId) {
		this.srcVisaProductId = srcVisaProductId;
	}
	
	@Column(name="docInfo_id")
	public Long getdocInfo() {
		return docInfo;
	}
	public void setdocInfo(Long docInfo) {
		this.docInfo = docInfo;
	}
	
	
}
