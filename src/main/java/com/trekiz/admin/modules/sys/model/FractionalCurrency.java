package com.trekiz.admin.modules.sys.model;

/**
 * 辅币进位制
 * @author majiancheng
 *
 */
public class FractionalCurrency{
	private String currencyMark;//币种单位
	private Integer cractionalCount;//辅币进位
	public String getCurrencyMark() {
		return currencyMark;
	}
	public void setCurrencyMark(String currencyMark) {
		this.currencyMark = currencyMark;
	}
	public Integer getCractionalCount() {
		return cractionalCount;
	}
	public void setCractionalCount(Integer cractionalCount) {
		this.cractionalCount = cractionalCount;
	}
	
	
}
