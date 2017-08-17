package com.trekiz.admin.modules.eprice.form;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ReplyEPrice4TrafficForm {

	private Long pid;		// 询价项目ID
	public Long getPid() {
		return pid;
	}

	public void setPid(Long pid) {
		this.pid = pid;
	}

	public Long getRid() {
		return rid;
	}

	public void setRid(Long rid) {
		this.rid = rid;
	}

	public Long getRpid() {
		return rpid;
	}

	public void setRpid(Long rpid) {
		this.rpid = rpid;
	}

	private Long rid;		// 询价记录ID
	private Long rpid;	// 回复记录ID
	@NotNull (message="成人单价不能为空")
	private BigDecimal adultPrice;
	@NotNull (message="儿童单价不能为空")
	private BigDecimal childPrice;
	@NotNull (message="特殊人群单价不能为空")
	private BigDecimal specialPersonPrice;
	@NotNull (message="结算价不能为空")
	private BigDecimal operatorTotalPrice;
	private Integer adultSum=0;	// 成人总数
	private Integer childSum=0;	// 成人总数
	private Integer specialPersonSum=0;	// 成人总数
	@Size(max=500,min=0)
	private String remark;// 备注

	public BigDecimal getAdultPrice() {
		return adultPrice;
	}

	public void setAdultPrice(BigDecimal adultPrice) {
		this.adultPrice = adultPrice;
	}

	public BigDecimal getChildPrice() {
		return childPrice;
	}

	public void setChildPrice(BigDecimal childPrice) {
		this.childPrice = childPrice;
	}

	public BigDecimal getSpecialPersonPrice() {
		return specialPersonPrice;
	}

	public void setSpecialPersonPrice(BigDecimal specialPersonPrice) {
		this.specialPersonPrice = specialPersonPrice;
	}

	public BigDecimal getOperatorTotalPrice() {
		return operatorTotalPrice;
	}

	public void setOperatorTotalPrice(BigDecimal operatorTotalPrice) {
		this.operatorTotalPrice = operatorTotalPrice;
	}

	public Integer getAdultSum() {
		return adultSum;
	}

	public void setAdultSum(Integer adultSum) {
		this.adultSum = adultSum;
	}

	public Integer getChildSum() {
		return childSum;
	}

	public void setChildSum(Integer childSum) {
		this.childSum = childSum;
	}

	public Integer getSpecialPersonSum() {
		return specialPersonSum;
	}

	public void setSpecialPersonSum(Integer specialPersonSum) {
		this.specialPersonSum = specialPersonSum;
	}

	public String getRemark() {
		if("undefined".equals(this.remark) || "null".equals(this.remark)){
			this.remark =null;
		}
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
