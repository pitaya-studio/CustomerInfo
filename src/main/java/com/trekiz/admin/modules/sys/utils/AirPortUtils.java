/**
 *
 */
package com.trekiz.admin.modules.sys.utils;

import java.util.Iterator;

import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.sys.entity.AirportInfo;
import com.trekiz.admin.modules.sys.repository.AirportInfoDao;

/**
 * 字典工具类
 * 
 * @author zj
 * @version 2013-11-19
 */
public class AirPortUtils {

	private static AirportInfoDao airPortInfoDao = SpringContextHolder
			.getBean(AirportInfoDao.class);

	public static String getAirportName(Long airPortId) {
		String airPortName = "";
		if (airPortId == null) {
			return airPortName;
		}
		AirportInfo airportInfo = airPortInfoDao.findOne(airPortId);
		if(null != airportInfo){
			airPortName = airportInfo.getAirportName();
		}
		return airPortName;
	}

}
