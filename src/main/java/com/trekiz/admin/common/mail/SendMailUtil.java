package com.trekiz.admin.common.mail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;


/**
 * 
 * 发送邮件的工具类
 * @author shijun.liu
 *
 */
public class SendMailUtil {

	/**
	 * 发送简单邮件的工具类
	 * @param addressArray    收件人列表
	 * @param title           邮件标题
	 * @param content         邮件内容
	 * @author shijun.liu
	 */
	public static void sendSimpleMail(String[] addressArray, final String title, final String content){
		if(null == title){
			throw new RuntimeException("邮件标题不能为空");
		}
		if(null == content){
			throw new RuntimeException("邮件内容不能为空");
		}
		if(null == addressArray){
			throw new RuntimeException("收件人地址不能为空");
		}
		//过滤掉空的email
		final List<String> list = new ArrayList<String>();
		Set<String> sets = new HashSet<String>();
		for (String address:addressArray) {
			if(null != address && !"".equals(address)){
				if(sets.contains(address)){
					continue;
				}
				sets.add(address);
				list.add(address);
			}
		}
		final String[] newAddressArray = new String[list.size()];
		if(newAddressArray.length == 0){
			return;
		}
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					Email email = EmailSingleton.getSimpleEmailInstance();
					email.addTo(list.toArray(newAddressArray));
					email.setSubject(title);
					email.setMsg(content);
//					MimeMessage mimeMessage = email.getMimeMessage();
//					if(mimeMessage == null){
//						email.buildMimeMessage();
//						email.sendMimeMessage();
						email.send();
//					} else {
//						email.sendMimeMessage();
//					}
				} catch (EmailException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
