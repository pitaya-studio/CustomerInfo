package com.trekiz.admin.common.exception.bean.airticket;

import com.trekiz.admin.common.exception.bean.BaseException4Quauq;

/**
 * 机票产品线异常类
 * @author police
 *
 */
public class AirticketException extends BaseException4Quauq {

	public AirticketException(String message){
		super(message);
	}
	public AirticketException(Throwable e){
		super(e);
	}
	public AirticketException(String message,Throwable e){
		super(message, e);
	}

}
