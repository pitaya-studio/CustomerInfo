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
 *  文件名: Visapersonneltype
 *  功能:
 *  产品Entity
 *  修改记录:   
 *  
 *  @author zj
 *  @DateTime 2014-01-13
 *  @version 1.0
 */
@Entity
@Table(name = "visapersonneltype")
public class Visapersonneltype extends DataEntity {
	
	private static final long serialVersionUID = 1L;
	private Long id; 		// 编号
	                                              
    /** 人员类型 */
    private Integer personnelType;
    /** 基础表id */
    private Long visabasicsId;
    /** 护照 */
    private String passport;
    /** 身份证 */
    private String idCard;
    /** 照片 */
    private String photo;
    /** 签证申请表 */
    private String visaApplicationForm;
    /** 其他补充材料 */
    private String otherSupplementaryMaterials;
    /** 存款证明 */
    private String certificateDeposit;
    /** 在职证明/学校准假信/退休证 */
    private String service;
    /** 备注 */
    private String remarks;

	public Visapersonneltype() {
		super();
	}

	public Visapersonneltype(Long id){
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

	public void setPersonnelType(Integer personnelType ){
        this.personnelType = personnelType ;
    }

    public Integer getPersonnelType(){
        return this.personnelType;
    }

    public void setVisabasicsId(Long visabasicsId ){
        this.visabasicsId = visabasicsId ;
    }

    public Long getVisabasicsId(){
        return this.visabasicsId;
    }

    public void setPassport(String passport ){
        this.passport = passport ;
    }

    @Length(min=0, max=65535)
    public String getPassport(){
        return this.passport;
    }

    public void setIdCard(String idCard ){
        this.idCard = idCard ;
    }

    @Length(min=0, max=65535)
    public String getIdCard(){
        return this.idCard;
    }

    public void setPhoto(String photo ){
        this.photo = photo ;
    }

    @Length(min=0, max=65535)
    public String getPhoto(){
        return this.photo;
    }

    public void setVisaApplicationForm(String visaApplicationForm ){
        this.visaApplicationForm = visaApplicationForm ;
    }

    @Length(min=0, max=65535)
    public String getVisaApplicationForm(){
        return this.visaApplicationForm;
    }

    public void setOtherSupplementaryMaterials(String otherSupplementaryMaterials ){
        this.otherSupplementaryMaterials = otherSupplementaryMaterials ;
    }

    @Length(min=0, max=65535)
    public String getOtherSupplementaryMaterials(){
        return this.otherSupplementaryMaterials;
    }

    public void setCertificateDeposit(String certificateDeposit ){
        this.certificateDeposit = certificateDeposit ;
    }

    @Length(min=0, max=65535)
    public String getCertificateDeposit(){
        return this.certificateDeposit;
    }

    public void setService(String service ){
        this.service = service ;
    }

    @Length(min=0, max=65535)
    public String getService(){
        return this.service;
    }

    public void setRemarks(String remarks ){
        this.remarks = remarks ;
    }

    @Length(min=0, max=65535)
    public String getRemarks(){
        return this.remarks;
    }
}


