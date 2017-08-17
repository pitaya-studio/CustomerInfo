/**
 *
 */
package com.trekiz.admin.modules.sys.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Service;

import com.trekiz.admin.modules.sys.utils.RequestUtils;

/**
 * 表单验证（包含验证码）过滤类
 * @author zj
 * @version 2013-11-19
 */
@Service
public class FormAuthenticationFilter extends org.apache.shiro.web.filter.authc.FormAuthenticationFilter {

	public static final String DEFAULT_CAPTCHA_PARAM = "validateCode";

	public static final String DEFAULT_PHPSESSIONID_COOKIE = "TSESSIONID";

	private String captchaParam = DEFAULT_CAPTCHA_PARAM;

	public String getCaptchaParam() {
		return captchaParam;
	}

	protected String getCaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, getCaptchaParam());
	}

	protected String getPhpsessionId(ServletRequest request) {
		return RequestUtils.getCookie((HttpServletRequest)request, DEFAULT_PHPSESSIONID_COOKIE);
	}

	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
		String username = getUsername(request);
		String password = getPassword(request);
		if (password==null){
			password = "";
		}
		boolean rememberMe = isRememberMe(request);
		String host = getHost(request);
		String phpsessionId = getPhpsessionId(request);
		String captcha = getCaptcha(request);
		return new UsernamePasswordToken(username, password.toCharArray(), rememberMe , host, captcha, phpsessionId);
	}

}