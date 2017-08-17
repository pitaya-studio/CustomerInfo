package com.trekiz.admin.common.exception.bean.group;


/**
 * 团期类产品 库存 异常类
 * @author police
 *
 */
public class Group4StockException extends GroupException {

	public Group4StockException(String message){
		super(message);
	}
	public Group4StockException(Throwable e){
		super(e);
	}
	public Group4StockException(String message,Throwable e){
		super(message, e);
	}

}
