/**
 *
 */
package com.trekiz.admin.modules.sys.utils;

import java.util.Date;
import java.util.List;

import com.trekiz.admin.common.utils.Collections3;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.airticket.entity.FlightInfo;
import com.trekiz.admin.modules.airticket.service.FlightInfoServiceImpl;
import com.trekiz.admin.modules.airticket.service.IFlightInfoService;
import com.trekiz.admin.modules.sys.repository.AirlineInfoDao;

/**
 * 字典工具类
 * 
 * @author lixinyun
 * @version 2015-01-28
 */
public class AirlineUtils {

	private static AirlineInfoDao airlineInfoDao = SpringContextHolder
			.getBean(AirlineInfoDao.class);

	private static IFlightInfoService flightInfoService = SpringContextHolder
			.getBean(FlightInfoServiceImpl.class);
	/**
	 * 由航空公司二字码取得航空公司名称
	 * 
	 * @param airlineCode
	 * @return
	 */
	public static String getAirlineNameByAirlineCode(String airlineCode,
			String defaultVal) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<String> list = airlineInfoDao.getAirlineNameByAirlineCode(
				companyId, airlineCode);
		if (list == null || list.size() == 0) {
			return defaultVal;
		}
		return list.get(0);
	}

	/**
	 * 由航空公司二字码取得航空公司名称
	 * 
	 * @param airlineCode
	 * @return
	 */
	public static String getAirlineNameByAirlineCode(String airlineCode) {
		return getAirlineNameByAirlineCode(airlineCode, "不限");
	}

	/**
	 * 根据机票产品ID查询其航空公司名称
	 * @param airticketId		机票产品ID
	 * @return
	 * @author	shijun.liu
	 * @date 	2016.05.27
	 */
	public static String getAirLineName(Long airticketId){
		StringBuffer sb = new StringBuffer();
		List<FlightInfo> list = flightInfoService.findByFlightInfoByAirTicketId(airticketId);
		if(!Collections3.isEmpty(list)){
			for (FlightInfo item : list){
				String airLineCode = item.getAirlines();
				if(StringUtils.isNotBlank(airLineCode)){
					String airLineName = getAirlineNameByAirlineCode(airLineCode);
					airLineName = airLineName == null? "": airLineName.replaceAll("不限", "");
					if("".equals(airLineName)){
						continue;
					}
					if(0 == sb.toString().length()){
						sb.append(airLineName);
					}else{
						sb.append("<br>").append(airLineName);
					}
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 根据机票产品ID查询其航班的起飞时间
	 * @param airticketId		机票产品ID
	 * @return
	 * @author	shijun.liu
	 * @date 	2016.05.27
	 */
	public static String getFlightInfoStartTime(Long airticketId){
		List<FlightInfo> list = flightInfoService.findByFlightInfoByAirTicketId(airticketId);
		return getFlightDateTime(list, 0);
	}

	/**
	 * 根据机票产品ID查询其航班的起飞时间
	 * @param airticketId		机票产品ID
	 * @return
	 * @author	shijun.liu
	 * @date 	2016.05.27
	 */
	public static String getFlightInfoArrivalTime(Long airticketId){
		List<FlightInfo> list = flightInfoService.findByFlightInfoByAirTicketId(airticketId);
		return getFlightDateTime(list, 1);
	}

	/**
	 * 将航班的出发时间或者到达时间合并成字符串
	 * @param list			航班信息
	 * @param type			0:起飞时间，1:到达时间
     * @return
	 * @author 		shijun.liu
	 * @date 		2016.05.27
     */
	private static String getFlightDateTime(List<FlightInfo> list, Integer type){
		StringBuffer str = new StringBuffer();
		if (Collections3.isEmpty(list)) {
			return str.toString();
		}
		for (FlightInfo item : list){
			Date date = null;
			if(0 == type){
				date = item.getStartTime();
			}else if(1 == type){
				date = item.getArrivalTime();
			}
			if(null != date){
				String time = DateUtils.date2String(date, DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS);
				if(0 == str.length()){
					str.append(time);
				}else{
					str.append("<br>").append(time);
				}
			}
		}
		return str.toString();
	}

	/**
	 * 根据机票ID查询其舱位信息
	 * @param airticketId		机票ID
	 * @return
	 * @author		shijun.liu
	 * @date		2016.05.27
     */
	public static String getFlightAirspace(Long airticketId){
		StringBuffer str = new StringBuffer();
		List<FlightInfo> list = flightInfoService.findByFlightInfoByAirTicketId(airticketId);
		if(Collections3.isEmpty(list)){
			return str.toString();
		}
		for (FlightInfo item : list){
			String airSpace = item.getAirspace();
			if(StringUtils.isNotBlank(airSpace)){
				String airSpaceValue = DictUtils.getLabelById(Long.valueOf(airSpace), "airspace_Type");
				if(StringUtils.isBlank(airSpaceValue)){
					continue;
				}
				if(0 == str.length()){
					str.append(airSpaceValue);
				}else{
					str.append(",").append(airSpaceValue);
				}
			}
		}
		return str.toString();
	}

	/**
	 * 根据机票ID查询其出发机场
	 * @param airticketId		机票ID
	 * @return
	 * @author		shijun.liu
	 * @date		2016.05.27
	 */
	public static String getFlightLeaveAirPort(Long airticketId){
		List<FlightInfo> list = flightInfoService.findByFlightInfoByAirTicketId(airticketId);
		return getFilghtAirPortName(list, 0);
	}

	/**
	 * 根据机票ID查询其到达机场
	 * @param airticketId		机票ID
	 * @return
	 * @author		shijun.liu
	 * @date		2016.05.27
	 */
	public static String getFlightArrivalAirPort(Long airticketId){
		List<FlightInfo> list = flightInfoService.findByFlightInfoByAirTicketId(airticketId);
		return getFilghtAirPortName(list, 1);
	}

	/**
	 * 将航班的出发机场或者到达机场合并成字符串
	 * @param list			航班信息
	 * @param type			0:出发机场，1:到达机场
	 * @return
	 * @author 		shijun.liu
	 * @date 		2016.05.27
	 */
	private static String getFilghtAirPortName(List<FlightInfo> list, Integer type){
		StringBuffer str = new StringBuffer();
		if(Collections3.isEmpty(list)){
			return str.toString();
		}
		for (FlightInfo item : list){
			String airPort = null;
			if(0 == type){
				airPort = item.getLeaveAirport();
			}else if(1 == type){
				airPort = item.getDestinationAirpost();
			}
			if(StringUtils.isNotBlank(airPort)){
				String airPortName = AirPortUtils.getAirportName(Long.valueOf(airPort));
				if(0 == str.length()){
					str.append(airPortName);
				}else{
					str.append("<br>").append(airPortName);
				}
			}
		}
		return str.toString();
	}

}
