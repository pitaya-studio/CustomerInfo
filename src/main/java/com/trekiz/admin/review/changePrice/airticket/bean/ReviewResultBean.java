package com.trekiz.admin.review.changePrice.airticket.bean;

import com.quauq.review.core.support.ReviewResult;

public class ReviewResultBean {

	private boolean success = false;//获取成功标识
	
	private Integer code;//审批流程中的错误码，详细信息参看错误码常量表

	private StringBuffer message = new StringBuffer();//获取操作反馈信息

	private static final String REGEX = ";";
	
	public ReviewResultBean(){
		
	}
	public ReviewResultBean(ReviewResult result){
		if(result!=null){
			this.success=result.getSuccess();
			this.code = result.getCode();
			this.message.append(result.getMessage());
		}
	}
	
	/**
	 * 审批流程中的错误码，详细信息参看错误码常量表
	 * 
	 * @see com.quauq.review.core.engine.config.ReviewErrorCode
	 * @created_by zhenxing.yan 2015年11月6日
	 *
	 * @return
	 */
	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}


	/**
	 * 获取成功标识
	 * 
	 * @return
	 */
	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * 获取操作反馈信息
	 * 
	 * @return
	 */
	public StringBuffer getMessage() {
		return message;
	}
	public String getMessageString() {
		return message.toString();
	}

	public void setMessage(StringBuffer message) {
		this.message = message;
	}
	public void putMessage(String message){
		this.message.append(message);
	}
	public void putMessage(String message,String regex){
		if(regex==null){
			regex=REGEX;
		}
		this.message.append(message).append(regex);
	}
	public void clearMessage(){
		this.message.setLength(0);
	}
}
