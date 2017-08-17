package com.trekiz.admin.common.exception.bean.group;

import com.trekiz.admin.common.exception.bean.BaseException4Quauq;

/**
 * 团期类产品线异常类
 * @author police
 *
 */
public class GroupException extends BaseException4Quauq {

	public GroupException(String message){
		super(message);
	}
	public GroupException(Throwable e){
		super(e);
	}
	public GroupException(String message,Throwable e){
		super(message, e);
	}

}
