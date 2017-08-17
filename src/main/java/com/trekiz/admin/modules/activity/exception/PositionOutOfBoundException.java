package com.trekiz.admin.modules.activity.exception;

public class PositionOutOfBoundException extends Exception{
	
	private static final long serialVersionUID = -4902248440223715682L;
	
	public PositionOutOfBoundException() {
		super();
	}
	public PositionOutOfBoundException(String description){
		super(description);
	}
}
