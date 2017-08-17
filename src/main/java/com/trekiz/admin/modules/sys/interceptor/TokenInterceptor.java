package com.trekiz.admin.modules.sys.interceptor;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.utils.UuidUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 解决数据重复提交的问题，Token+锁的方式
 * @author shijun.liu
 *
 */
public class TokenInterceptor implements HandlerInterceptor {

	private static final byte[] b = new byte[0];
	
	private static final String SESSION_TOKKEN = "sessionToken";
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String token = request.getParameter("token");
		String method = request.getMethod();
		boolean returnValue = true;
		if(null != token && null != method && "POST".equalsIgnoreCase(method)){
			synchronized (b) {
				Object sessionToken = request.getSession().getAttribute(SESSION_TOKKEN);
				request.getSession().setAttribute(SESSION_TOKKEN, token);
				if(token.equals(sessionToken)){//重复提交
					//String newToken = UUID.randomUUID().toString().replaceAll("-", "");
					//request.getSession().setAttribute(SESSION_TOKKEN, newToken);
					returnValue = false;
				}
			}
		}
		return returnValue;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		request.setAttribute("token", UuidUtils.generUuid());
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
