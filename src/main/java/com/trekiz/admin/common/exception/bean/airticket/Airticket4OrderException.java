package com.trekiz.admin.common.exception.bean.airticket;


/**
 * 机票产品订单异常类
 * @author police
 *
 */
public class Airticket4OrderException extends AirticketException {

	public Airticket4OrderException(String message){
		super(message);
	}
	public Airticket4OrderException(Throwable e){
		super(e);
	}
	public Airticket4OrderException(String message,Throwable e){
		super(message, e);
	}

}
