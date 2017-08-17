package com.trekiz.admin.modules.eprice.form;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class RecordPriceForm {

	// estimate_price_record 主键ID
	@NotNull
	private Long prid;
	// 计调报价
//	@NotNull
//	@Min(value=-99999999)
	private BigDecimal operatorPrice;
	// 销售对外报价
	@NotNull
	@Min(value=-99999999)
	private BigDecimal outPrice;
	@NotNull
	private Integer status = 3;
	
	private Integer acceptAopId;
	
	private Integer acceptTopId;
	
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getPrid() {
		return prid;
	}
	public void setPrid(Long prid) {
		this.prid = prid;
	}
 
	
	public BigDecimal getOperatorPrice() {
		return operatorPrice;
	}
	public void setOperatorPrice(BigDecimal operatorPrice) {
		this.operatorPrice = operatorPrice;
	}
	public BigDecimal getOutPrice() {
		return outPrice;
	}
	public void setOutPrice(BigDecimal outPrice) {
		this.outPrice = outPrice;
	}
	
	
	
	public Integer getAcceptAopId() {
		return acceptAopId;
	}
	public void setAcceptAopId(Integer acceptAopId) {
		this.acceptAopId = acceptAopId;
	}
	public Integer getAcceptTopId() {
		return acceptTopId;
	}
	public void setAcceptTopId(Integer acceptTopId) {
		this.acceptTopId = acceptTopId;
	}
	/**
	 * 额外的验证检查
	 * @return boolean
	 */
	public String check(){
		if(outPrice==null){
			return "请填写外报价";
		}
//		if(operatorPrice==null){
//			return "请选择报价";
//		}
		return null;
	}
	
	
}
