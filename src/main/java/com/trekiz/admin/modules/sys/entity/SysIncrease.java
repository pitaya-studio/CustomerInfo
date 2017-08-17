package com.trekiz.admin.modules.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "sysincrease")
@OptimisticLocking(type=OptimisticLockType.VERSION)
public class SysIncrease {

	
	/**
	 * 
	 */
	@SuppressWarnings("unused")
    private static final long serialVersionUID = 1L;
	private Long id;
	
	/**编码名称*/
	private String codeName;
	/**编码数值*/
	private Integer codeNum;
	/**版本号*/
	private Integer version;
	/**日期标志*/
	private String dateMark;
	/**编号长度*/
	private Integer numLen;
	/**批发商ID*/
	private Long proCompanyId;
	/**编码类型*/
	private Integer codeType;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id",unique=true,nullable=false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Length(min=0, max=10)
	@Column(name="codeName",unique=false,nullable=false)
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	
	@Column(name="codeNum",unique=false,nullable=false)
	public Integer getCodeNum() {
		return codeNum;
	}
	public void setCodeNum(Integer codeNum) {
		this.codeNum = codeNum;
	}
	
	@Version
	@Column(name="version",unique=false,nullable=false)
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Column(name="dateMark",unique=false,nullable=true)
	public String getDateMark() {
		return dateMark;
	}
	public void setDateMark(String dateMark) {
		this.dateMark = dateMark;
	}
	
	@Column(name="numLen",unique=false,nullable=true)
	public Integer getNumLen() {
		return numLen;
	}
	public void setNumLen(Integer numLen) {
		this.numLen = numLen;
	}
	
	@Column(name="proCompanyId",unique=false,nullable=true)
	public Long getProCompanyId() {
		return proCompanyId;
	}
	public void setProCompanyId(Long proCompanyId) {
		this.proCompanyId = proCompanyId;
	}
	@Column(name="codeType",unique=false,nullable=false)
	public Integer getCodeType() {
		return codeType;
	}
	public void setCodeType(Integer codeType) {
		this.codeType = codeType;
	}
}
