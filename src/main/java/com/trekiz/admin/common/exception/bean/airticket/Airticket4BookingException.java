package com.trekiz.admin.common.exception.bean.airticket;


/**
 * 机票产品预订异常类
 * @author police
 *
 */
public class Airticket4BookingException extends AirticketException {

	public Airticket4BookingException(String message){
		super(message);
	}
	public Airticket4BookingException(Throwable e){
		super(e);
	}
	public Airticket4BookingException(String message,Throwable e){
		super(message, e);
	}

}
