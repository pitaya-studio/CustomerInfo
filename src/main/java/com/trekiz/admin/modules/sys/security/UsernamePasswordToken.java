/**
 *
 */
package com.trekiz.admin.modules.sys.security;

/**
 * 用户和密码（包含验证码）令牌类
 * @author zj
 * @version 2013-11-19
 */
public class UsernamePasswordToken extends org.apache.shiro.authc.UsernamePasswordToken {

	private static final long serialVersionUID = 1L;

	private String captcha;
	
	private String phpsessionId;
	
	private int userType;

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getPhpsessionId() {
		return phpsessionId;
	}

	public void setPhpsessionId(String phpsessionId) {
		this.phpsessionId = phpsessionId;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public UsernamePasswordToken() {
		super();
	}

	public UsernamePasswordToken(String username, char[] password,
			boolean rememberMe, String host, String captcha) {
		super(username, password, rememberMe, host);
		this.captcha = captcha;
	}

	public UsernamePasswordToken(String username, char[] password,
			boolean rememberMe, String host, String captcha, String phpsessionId) {
		this(username, password, rememberMe, host, captcha);
		this.phpsessionId = phpsessionId;
	}

}