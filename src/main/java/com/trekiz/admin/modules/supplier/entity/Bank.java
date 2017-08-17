package com.trekiz.admin.modules.supplier.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "plat_bank_info")
public class Bank {	    
		private Long id;		 
		private Integer  bankId;
		private Integer defaultFlag;
		private String bankName;		 
		private String bankAccountCode;	 
		private Integer platType;
		private Integer belongPlatId;
		private String delFlag;
		
		@Id
		@GeneratedValue(strategy=GenerationType.IDENTITY)
		@Column(name="id",unique=true,nullable=false)
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		
		@Column(name="bankId",unique=false,nullable=true)
		public Integer getBankId() {
			return bankId;
		}
		public void setBankId(Integer bankId) {
			this.bankId = bankId;
		}
		@Column(name="defaultFlag",unique=false,nullable=true)
		public Integer getDefaultFlag() {
			return defaultFlag;
		}
		public void setDefaultFlag(Integer defaultFlag) {
			this.defaultFlag = defaultFlag;
		}
		@Column(name="bankName",unique=false,nullable=true)
		public String getBankName() {
			return bankName;
		}
		public void setBankName(String bankName) {
			this.bankName = bankName;
		}
		@Column(name="bankAccountCode",unique=false,nullable=true)
		public String getBankAccountCode() {
			return bankAccountCode;
		}
		public void setBankAccountCode(String bankAccountCode) {
			this.bankAccountCode = bankAccountCode;
		}
		@Column(name="platType",unique=false,nullable=true)
		public Integer getPlatType() {
			return platType;
		}
		public void setPlatType(Integer platType) {
			this.platType = platType;
		}
		@Column(name="beLongPlatId",unique=false,nullable=true)
		public Integer getBelongPlatId() {
			return belongPlatId;
		}
		public void setBelongPlatId(Integer beLongPlatId) {
			this.belongPlatId = beLongPlatId;
		}
		@Column(name="delFlag",unique=false,nullable=true)
		public String getDelFlag() {
			return delFlag;
		}
		public void setDelFlag(String delFlag) {
			this.delFlag = delFlag;
		}
		
		

}
