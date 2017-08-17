package com.trekiz.admin.modules.sys.service;

/**
 * 
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 *
 * 公司配置执行结果，用于配置公司产品类型、公司流程类型映射等校验场景
 *
 * @author zhenxing.yan
 * @date 2015年11月16日
 */
public class OfficeConfigExecutionResult {

	private boolean success;

	private String message;

	public OfficeConfigExecutionResult() {
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
