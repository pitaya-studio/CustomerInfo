package com.trekiz.admin.modules.sys.utils;

import javax.servlet.http.HttpServletRequest;


 /**
 *  Class Name: RequestUtils.java
 *  Function:
 *  
 *     Modifications:   
 *  
 *  @author zj   DateTime 2013-11-22 上午11:56:18    
 *  @version 1.0
 */
public class RequestUtils {
	
    public static String getCookie(HttpServletRequest request, String cookieName) {
        javax.servlet.http.Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (javax.servlet.http.Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
