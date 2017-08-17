package com.trekiz.admin.review.common.exception;

/**
 * 付款审批公共异常类
 * @author shijun.liu
 *
 */
public class CommonReviewException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CommonReviewException() {
		super();
	}

	public CommonReviewException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CommonReviewException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommonReviewException(String message) {
		super(message);
	}

	public CommonReviewException(Throwable cause) {
		super(cause);
	}

}
