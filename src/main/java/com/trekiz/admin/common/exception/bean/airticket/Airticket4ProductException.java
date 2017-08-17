package com.trekiz.admin.common.exception.bean.airticket;


/**
 * 机票产品异常类
 * @author police
 *
 */
public class Airticket4ProductException extends AirticketException {

	public Airticket4ProductException(String message){
		super(message);
	}
	public Airticket4ProductException(Throwable e){
		super(e);
	}
	public Airticket4ProductException(String message,Throwable e){
		super(message, e);
	}

}
