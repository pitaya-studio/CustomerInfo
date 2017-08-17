/** 
 * Copyright 2015 QUAUQ Technology Co. Ltd. 
 * All right reserved.
 */
package com.trekiz.admin.modules.mtourCommon.interceptor;

import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.quauq.multi.tenant.hibernate.FacesContext;
import com.trekiz.admin.common.async.supply.IMessageSupplier;
import com.trekiz.admin.common.async.supply.MessageSupplierFactory;
import com.trekiz.admin.common.async.utils.AMQGlobal;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.mtourCommon.entity.MtourInterfaceLog;
import com.trekiz.admin.modules.mtourCommon.utils.ThreadVariable;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author ning.zhang@quauq.com
 * @createDate 2015-10-27
 * 
 * 美途项目-ajax请求日志连接器
 */
public class MtourInterfaceLogInterceptor implements HandlerInterceptor {

	private static Logger logger = LoggerFactory.getLogger(MtourInterfaceLogInterceptor.class);
	
	@Autowired
	@Qualifier("messageSupplierFactory")
	private MessageSupplierFactory factory;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet
	 * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object)
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet
	 * .http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax
	 * .servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 * java.lang.Object, java.lang.Exception)
	 */
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		String requestType = request.getHeader("X-Requested-With");
		String requestURI = request.getRequestURI();
		String mtourPath = com.trekiz.admin.common.config.Global.getMtourPath();
		String mtourLogSwitch = com.trekiz.admin.common.config.Global.getMtourLogSwitch();
		if ("XMLHttpRequest".equals(requestType) && null != mtourPath
				&& null != requestURI && requestURI.contains(mtourPath + "/")
				&&	null != mtourLogSwitch && "true".equals(mtourLogSwitch)) {//记录日志开关打开
			//美途项目且请求类型为ajax请求进入日志
			try { 
				//构建request parameter
				StringBuilder params = new StringBuilder();
				int index = 0;
				for (Object param : request.getParameterMap().keySet()) { 
					params.append((index++ == 0 ? "" : "&") + param + "=");
					params.append(StringUtils.endsWithIgnoreCase(
							(String) param, "password") ? "" : request
							.getParameter((String) param));
				}
				User user = UserUtils.getUser();
				MtourInterfaceLog log = new MtourInterfaceLog();
				log.setUuid(UuidUtils.generUuid());
				log.setRemoteAddr(StringUtils.getRemoteAddr(request));
				log.setUserAgent(request.getHeader("user-agent"));
				log.setRequestUri(requestURI);
				log.setMethod(request.getMethod());
				log.setParams(params.toString());
				log.setResponse(ThreadVariable.getMtourAjaxResponse());
				log.setException(null == ex ? "" : ex.toString());
				log.setCreateBy(null == user ? null : user.getId());
				log.setCreateDate(new Date());
				log.setDelFlag("0");
				log.setAsyncSysParameterUserID(user.getId());
				log.setAsyncSysParameterTenantID(FacesContext.getCurrentTenant());
				
				String queueName = AMQGlobal.getConfig("amq.mtour.log.key");
				if(StringUtils.isBlank(queueName)){
					logger.error("尚未指定 amq.mtour.log.key 的值,无法记录日志信息");
					return;
				}
				IMessageSupplier  supplier = this.factory.getDefaultMessageSupplier(queueName);
				supplier.asynchronizedSend(JSON.toJSONStringWithDateFormat(log, "yyyy-MM-dd HH:mm:ss"));
			} finally {
				//清理上下文 ，如果非美途 非ajax请求，使用了该上下文需自己负责处理
				ThreadVariable.removeMtourAjaxResponse();
			}
		}
	}
}
