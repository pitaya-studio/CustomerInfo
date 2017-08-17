package com.trekiz.admin.modules.groupCover.entity;

public class CoverResult {
	private boolean success;
	
	private Integer code;

	private String message;

	private String processInstanceId;

	private String coverId;

	private Integer coverStatus;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	/**
	 * 鑾峰彇鎴愬姛鏍囪瘑
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
	 * 鑾峰彇鎿嶄綔鍙嶉淇℃伅
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 鑾峰彇娴佺▼瀹炰緥id
	 * 
	 * @return
	 */
	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getCoverId() {
		return coverId;
	}

	public void setCoverId(String coverId) {
		this.coverId = coverId;
	}

	public Integer getCoverStatus() {
		return coverStatus;
	}

	public void setCoverStatus(Integer coverStatus) {
		this.coverStatus = coverStatus;
	}

	/**
	 * 鑾峰彇褰撳墠瀹℃壒鐨勭姸鎬�
	 * @created_by zhenxing.yan 2015骞�1鏈�鏃�
	 *
	 * @return
	 */
}
