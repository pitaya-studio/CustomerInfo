package com.trekiz.admin.common.config;

import java.util.Date;

import com.trekiz.admin.common.utils.DateUtils;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * json日期格式转换
 * @author liangjingming
 *
 */
public class DateJsonValueConfig implements JsonValueProcessor {

	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return null;
	}

	public Object processObjectValue(String key, Object value,
			JsonConfig jsonConfig) {
		return process(value);
	}

	private Object process(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return DateUtils.formatDate((Date)obj,"yyyy-MM-dd");
		}
	}
}
