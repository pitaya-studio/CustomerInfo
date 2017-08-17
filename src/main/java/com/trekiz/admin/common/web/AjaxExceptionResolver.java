package com.trekiz.admin.common.web;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.exception.util.LogMessageUtil;
import com.trekiz.admin.common.exception.util.LogUtil;
import com.trekiz.admin.common.input.BaseOut4MT;
/**
 * ajax请求，异常后统一处理的封装类
 * @author ning.zhang@quauq.com
 *
 */
@Controller 
public class AjaxExceptionResolver implements HandlerExceptionResolver {
	private final static Log logger = LogFactory.getLog(AjaxExceptionResolver.class);
	/**
	 * 异常处理接口 ，ajax异常在此处理，非ajax请求按原逻辑转入500 页面
	 */
	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		
		//统一异常日志输出 begin
		logger.error(LogMessageUtil.getInstance().getLogMessage());
		ex.printStackTrace(LogUtil.getErrorStream(logger));
		//统一异常日志输出 end
		
		String requestType = request.getHeader("X-Requested-With");
		String meituRequestType = request.getParameter("requestType");
		
		//美途国际的ajax请求返回指定的输出格式
		if("XMLHttpRequest".equals(requestType)&&"mtour data".equals(meituRequestType)){ 
			//ajax请求
			BaseOut4MT out = new BaseOut4MT();
			out.setResponseType(request.getParameter("requestType"));
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("系统处理异常"); 
			String str = JSON.toJSONString(out);
			try {
				response.getWriter().print(str);
				response.getWriter().flush();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			ModelAndView mv = new ModelAndView("error/ajaxException");
			return mv;
		}else if("XMLHttpRequest".equals(requestType)){
//			ModelAndView mv = new ModelAndView("error/ajaxException");
			return null;
		}else if(ex instanceof FileNotFoundException){ 
			//非ajax请求
			ModelAndView mv = new ModelAndView("error/error");
			mv.addObject("error_message_key", "您要下载的文件不存在");
			return mv;
		} else{
			ModelAndView mv = new ModelAndView("error/500");
			return mv;
		}
	} 
}
