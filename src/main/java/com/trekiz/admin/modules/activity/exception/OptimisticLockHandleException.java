package com.trekiz.admin.modules.activity.exception;

public class OptimisticLockHandleException extends Exception{


	private static final long serialVersionUID = -7956554569057092699L;

	
	public OptimisticLockHandleException() {
		super();
	}
	public OptimisticLockHandleException(String description){
		super(description);
	}
}
