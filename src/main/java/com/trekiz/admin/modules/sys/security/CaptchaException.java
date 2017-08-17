/**
 *
 */
package com.trekiz.admin.modules.sys.security;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 验证码异常处理类
 * @author zj
 * @version 2013-11-19
 */
public class CaptchaException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public CaptchaException() {
		super();
	}

	public CaptchaException(String message, Throwable cause) {
		super(message, cause);
	}

	public CaptchaException(String message) {
		super(message);
	}

	public CaptchaException(Throwable cause) {
		super(cause);
	}

}
