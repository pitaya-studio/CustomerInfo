/** 
 * Copyright 2015 QUAUQ Technology Co. Ltd. 
 * All right reserved.
 */
package com.trekiz.admin.common.async.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
  
/**
 * @author ning.zhang@quauq.com
 * @createDate 2015-10-27
 * 
 * 解析获取amq.properties
 */
public final class AMQGlobal {
	private static Properties properties = new Properties();
	private static Logger logger = LoggerFactory.getLogger(AMQGlobal.class);
	static {
		InputStream is = null;
		try {
			Resource resource = new DefaultResourceLoader().getResource("amq.properties");
			is = resource.getInputStream();
			properties.load(is);
		} catch (IOException ex) {
			logger.info("Could not load properties from path:amq.properties, "
					+ ex.getMessage());
		} finally {
			IOUtils.closeQuietly(is);
		}
	}
	
	/**
	 * 获取配置
	 */
	public static String getConfig(String key) {
		 return properties.getProperty(key);
	}
}
