package com.trekiz.admin.common.params;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;


@SuppressWarnings("all")
public class Params extends HashMap<String, Object> {
	/**
	 * 根据配置参数 支持单元格数据太长时，用...代替
	 * 
	 */

	public Params(HttpServletRequest request) {
		Enumeration names = request.getParameterNames();
		while (names.hasMoreElements()) {
			String name = (String) names.nextElement();
			String value = request.getParameter(name);
			put(name, value);
		}
	}
}