/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.trekiz.admin.modules.sys.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import com.quauq.multi.tenant.hibernate.FacesContext;
import com.quauq.multi.tenant.manage.entity.UserTenant;
import com.quauq.multi.tenant.manage.service.TenantService;
import com.quauq.multi.tenant.util.MultiTenantUtil;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.sys.security.SystemAuthorizingRealm.Principal;
import com.trekiz.admin.modules.sys.service.SystemService;


/**
 * 登录状态过滤类
 * @author zj
 * @version 2013-11-19
 */
@Service
@DependsOn({"userDao","roleDao","menuDao","sysDefineDictDao","departmentDao","userPermissionDao","userProcessPermissionDao","sysJobNewDao","userDeptJobNewDao","mobileUserDao"})
public class UserFilter extends org.apache.shiro.web.filter.authc.UserFilter {

	@Autowired
	private SystemService systemService;
	
	/**
	 * 鉴权失败处理
	 * 如果是反向平台则定向到登录页面，如果是美途则返回特定的json串
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws Exception { 
		HttpServletRequest  httpRequest = org.apache.shiro.web.util.WebUtils.toHttp(request);
		String requestType = httpRequest.getHeader("X-Requested-With"); 
		String requestURI  = httpRequest.getRequestURI() ; 
		String mtourPath = com.trekiz.admin.common.config.Global.getMtourPath();
		if("XMLHttpRequest".equals(requestType) 
				&& null!= mtourPath
				&& null != requestURI
				&& requestURI.contains(mtourPath+"/") ){
			String bizRequestType = httpRequest.getParameter("requestType");
			String responseMessage = "{\"responseType\":\""+bizRequestType+"\",\"responseCode\":\"authentication\"}";
			response.getWriter().write(responseMessage);
			response.getWriter().flush();
			return false;
		} else if ("XMLHttpRequest".equals(requestType) && requestURI.contains("/t1")) {//t1首页session失效
            String bizRequestType = httpRequest.getParameter("requestType");
            String responseMessage = "{\"responseType\":\""+bizRequestType+"\",\"responseCode\":\"authentication\"}";
			response.getWriter().write(responseMessage);
			response.getWriter().flush();
			return false;
		}else{
			return super.onAccessDenied(request, response);	
		}
	}
	
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
    	
    	if (isLoginRequest(request, response)) {
            return true;
        } else {
            Subject subject = getSubject(request, response);
        	boolean success=false;
        	if (subject.getPrincipal() != null) {
				/**
				 * 多租户开关，为"true"时关闭，默认打开
				 */
				if (MultiTenantUtil.turnOnMulitTenant()) {
					PrincipalCollection principals=subject.getPrincipals();
	        		for (Object principal : principals) {
	        			if (principal instanceof Principal) {
	        				String userName=((Principal)principal).getLoginName();
	        				//前段系统的多租户设置
	        				TenantService tenantService=(TenantService)SpringContextHolder.getBean("tenantService");
	                		UserTenant userTenant=tenantService.findUserTenant(userName);
	                		if (userTenant!=null) {
	                			FacesContext.setCurrentTenant(userTenant.getTenantId());
	                		}else{
	                			return false;
	                		}
						}
					}
	        		success=true;
				}else{
					return true;
				}
			}
            return success;
        }
    }

}
