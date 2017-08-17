package com.trekiz.admin.common.exception.bean.finance;

import com.trekiz.admin.common.exception.bean.BaseException4Quauq;

/**
 * 签证产品线异常类
 * @author police
 *
 */
public class FinanceException extends BaseException4Quauq {

	public FinanceException(String message){
		super(message);
	}
	public FinanceException(Throwable e){
		super(e);
	}
	public FinanceException(String message,Throwable e){
		super(message, e);
	}

}
