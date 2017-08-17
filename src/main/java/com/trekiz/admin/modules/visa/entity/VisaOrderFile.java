package com.trekiz.admin.modules.visa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trekiz.admin.common.persistence.DataEntityTTS;

/**
 * @author qiyonglong
 *
 */
@Entity
@Table(name="visa_order_file")
public class VisaOrderFile extends DataEntityTTS {
   
	private Long id;
	/*visa_order表的主键id*/
	private Long visaOrderId;
	/*docinfo表的主键id*/
	private Long docInfoId;
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="visa_order_id")
	public Long getVisaOrderId() {
		return visaOrderId;
	}
	public void setVisaOrderId(Long visaOrderId) {
		this.visaOrderId = visaOrderId;
	}
	@Column(name="docinfo_id")
	public Long getDocInfoId() {
		return docInfoId;
	}
	public void setDocInfoId(Long docInfoId) {
		this.docInfoId = docInfoId;
	}
	
}
