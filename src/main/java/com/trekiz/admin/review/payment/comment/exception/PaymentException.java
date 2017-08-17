package com.trekiz.admin.review.payment.comment.exception;

/**
 * 付款审批的异常类
 * @author shijun.liu
 *
 */
public class PaymentException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PaymentException() {
		super();
	}

	public PaymentException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PaymentException(String message, Throwable cause) {
		super(message, cause);
	}

	public PaymentException(String message) {
		super(message);
	}

	public PaymentException(Throwable cause) {
		super(cause);
	}

}
