package com.trekiz.admin.common.filter;

import com.trekiz.admin.common.config.Global;
import org.apache.shiro.web.servlet.AdviceFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 请求过滤器，匹配出主菜单选中状态。
 * Created by ZhengZiyu on 2014/9/26.
 */
public class MenuRequestFilter extends AdviceFilter {
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

        String adminPath = Global.getAdminPath();
        String contextPath = ((HttpServletRequest)request).getContextPath();
        String requestUrl = ((HttpServletRequest)request).getRequestURI();
        if(requestUrl.startsWith(contextPath)){
            requestUrl = requestUrl.replaceFirst(contextPath, "");
            if(requestUrl.startsWith(adminPath)){
                requestUrl = requestUrl.replaceFirst(adminPath, "");
            }
        }
//        System.out.println(requestUrl);
        return super.preHandle(request, response);
    }

    @Override
    protected void postHandle(ServletRequest request, ServletResponse response) throws Exception {
        super.postHandle(request, response);
    }

    @Override
    public void afterCompletion(ServletRequest request, ServletResponse response, Exception exception) throws Exception {
        super.afterCompletion(request, response, exception);
    }
}
