package com.trekiz.admin.common.exception.bean.visa;

import com.trekiz.admin.common.exception.bean.BaseException4Quauq;

/**
 * 签证产品线异常类
 * @author police
 *
 */
public class VisaException extends BaseException4Quauq {

	public VisaException(String message){
		super(message);
	}
	public VisaException(Throwable e){
		super(e);
	}
	public VisaException(String message,Throwable e){
		super(message, e);
	}

}
