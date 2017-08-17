package com.trekiz.admin.modules.mtourfinance.json;


/**
 * 金额信息json结构（用于美途国际）
     * Title: AmountJsonBean.java    
     * Description: 
     * @author majiancheng       
     * @created 2015-10-24 下午3:49:48
 */
public class AmountJsonBean {
	private String currencyUuid;//'借款币种Uuid'
	private Double amount;//'借款金额'
	public String getCurrencyUuid() {
		return this.currencyUuid;
	}
	public void setCurrencyUuid(String currencyUuid) {
		this.currencyUuid = currencyUuid;
	}
	public Double getAmount() {
		return this.amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	

}
