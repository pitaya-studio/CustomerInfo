package com.trekiz.admin.common.web;

import java.sql.SQLException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.quauq.multi.tenant.hibernate.FacesContext;
import com.quauq.multi.tenant.manage.entity.UserTenant;
import com.quauq.multi.tenant.manage.service.TenantService;
import com.quauq.multi.tenant.util.MultiTenantUtil;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;

public class OnlineListener implements ServletContextListener,
		HttpSessionListener {
	private static ApplicationContext ctx = null;

	/**
	 * 获取登录用户session，同时修改数据库中字段，成为“在线状态”
	 */
	@Override
	public void sessionCreated(HttpSessionEvent se) {
	}
	/**
	 * 获取登录用户session，同时修改数据库中字段，成为“离线状态”
	 */
	@Override
	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		HttpSession session = httpSessionEvent.getSession();
		Long userId = (Long)session.getAttribute(UserUtils.ONLINE_USERID);
		/**
		 * 添加多租户支持，根据session中的用户登录名进行租户切换 TODO
		 * by yanzhenxing
		 */
		String userLoginName=(String)session.getAttribute(UserUtils.ONLINE_USER_LOGIN_NAME);
		if(userId!=null){
			try {
				/**
				 * 多租户开关，为"true"时关闭，默认打开
				 */
				if (MultiTenantUtil.turnOnMulitTenant()) {
					TenantService tenantService=(TenantService)ctx.getBean("tenantService");
					UserTenant userTenant=tenantService.findUserTenant(userLoginName);
					if (userTenant==null) {
						throw new IllegalStateException("tenant info is wrong, there is not tenant with user["+userLoginName+"]");
					}
					FacesContext.setCurrentTenant(userTenant.getTenantId());
				}
				
				User user = UserUtils.getUser(userId);
				user.setLoginStatus(0);
				@SuppressWarnings("unused")
	            boolean back = UserUtils.changeLoginStatus(user);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}catch (IllegalStateException e) {
				e.printStackTrace();
			}
//			if(back){
//				System.out.println("用户"+user.getName()+"离线状态修改成功");
//			}else{
//				System.out.println("用户"+user.getName()+"离线状态修改失败");
//			}
		}
	}
	@Override
	public void contextInitialized(ServletContextEvent evt) {
		// TODO Auto-generated method stub
		ctx = WebApplicationContextUtils.getWebApplicationContext(evt.getServletContext());
	}
	
	public static ApplicationContext getCtx() {
		return ctx;
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

}
