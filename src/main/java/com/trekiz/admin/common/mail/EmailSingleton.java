package com.trekiz.admin.common.mail;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * 邮件单例类
 * @author shijun.liu
 *
 */
public class EmailSingleton {

	/* 邮件主机  */
	private static final String MAIL_HOST_NAME = "smtp.quauq.com";
	
	/* 邮件认证用户名称  */
	private static final String MAIL_AUTHOR_USER = "services@quauq.com";
	
	/* 邮件认证用户密码  */
	private static final String MAIL_AUTHOR_PASSWD = "quauq.123";
	
	/* 邮件编码 */
	private static final String MAIL_CHARSET = "UTF-8";
	
	/* 发件人地址  */
	private static final String MAIL_FROM = "services@quauq.com";
	
	/* 发件人地址别名   */
	private static final String MAIL_FROM_ALIAS = "QUAUQ_Service（平台客服邮箱）";
	
	/* 简单邮件类     */
	private static final SimpleEmail SIMPLE_EMAIL = new SimpleEmail();
	
	/**
	 * 构造方法私有化
	 */
	private EmailSingleton(){
		
	}
	
	static{
		try {
			SIMPLE_EMAIL.setHostName(MAIL_HOST_NAME);
			SIMPLE_EMAIL.setAuthentication(MAIL_AUTHOR_USER, MAIL_AUTHOR_PASSWD);
			SIMPLE_EMAIL.setCharset(MAIL_CHARSET);
			SIMPLE_EMAIL.setFrom(MAIL_FROM, MAIL_FROM_ALIAS);
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 简单邮件对象的实例化方法
	 * @author shijun.liu
	 * @return
	 * @throws EmailException 
	 */
	public static SimpleEmail getSimpleEmailInstance() throws EmailException{
		SimpleEmail SIMPLE_EMAIL1 = new SimpleEmail();
		SIMPLE_EMAIL1.setHostName(MAIL_HOST_NAME);
		SIMPLE_EMAIL1.setAuthentication(MAIL_AUTHOR_USER, MAIL_AUTHOR_PASSWD);
		SIMPLE_EMAIL1.setCharset(MAIL_CHARSET);
		SIMPLE_EMAIL1.setFrom(MAIL_FROM, MAIL_FROM_ALIAS);
		return SIMPLE_EMAIL1;
	}
}
