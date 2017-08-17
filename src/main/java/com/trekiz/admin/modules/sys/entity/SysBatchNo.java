package com.trekiz.admin.modules.sys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
	 *  文件名: SysBatchNo
	 *  功能:
	 *  批次号Entity
	 *  
	 *  @author yue.wang
	 *  @DateTime 2015-03-03
	 */
@Entity
@Table(name = "sys_batch_no")
public class SysBatchNo {
	
	//主键：批次借护照
	public final static String CODE_BORROW_PASSPORT="CODE_BORROW_PASSPORT";
	

	//签证批次借款批次号
	public final static String CODE_VISA_BORROWMONEY_BACTCH="CODE_VISA_BORROWMONEY_BACTCH";
	

	//签证批次换护照批次号
	public final static String CODE_VISA_RETURNRECEIPT_BACTCH="CODE_VISA_RETURNRECEIPT_BACTCH";
	
	//签证批量设置面签通知批次号
	public final static String CODE_VISA_INTERVIEW_NOTICE_BACTCH="CODE_VISA_INTERVIEW_NOTICE_BACTCH";

	//签证批量设置面签通知批次号
	public final static String CODE_VISA_GUARANTEE_BACTCH="CODE_VISA_GUARANTEE_BACTCH";

	private Long id;
	//日期
	private String curDate;
	//主键
	private String code;
	//值
	private Integer value;
	//缓存数（当前不用)
	private Integer step;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",unique=true,nullable=false)
	public Long getId() {
		return id;
	}
	
	@Column(name="cur_date")
	public String getCurDate() {
		return curDate;
	}
	
	@Column(name="code")
	public String getCode() {
		return code;
	}
	
	@Column(name="value")
	public Integer getValue() {
		return value;
	}
	
	@Column(name="step")
	public Integer getStep() {
		return step;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public void setCurDate(String curDate) {
		this.curDate = curDate;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public void setValue(Integer value) {
		this.value = value;
	}
	
	public void setStep(Integer step) {
		this.step = step;
	}
	

}
