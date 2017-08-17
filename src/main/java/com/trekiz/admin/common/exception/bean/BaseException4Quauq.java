package com.trekiz.admin.common.exception.bean;

public class BaseException4Quauq extends Throwable {
	
	public BaseException4Quauq(String message){
		super(message);
	}
	public BaseException4Quauq(Throwable e){
		super(e);
	}
	public BaseException4Quauq(String message,Throwable e){
		super(message, e);
	}
}
