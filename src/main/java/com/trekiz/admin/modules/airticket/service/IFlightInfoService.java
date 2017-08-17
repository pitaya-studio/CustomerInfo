package com.trekiz.admin.modules.airticket.service;

import java.util.List;
import java.util.Set;

import com.trekiz.admin.modules.airticket.entity.FlightInfo;

/**
 * 
 * @Description:TODO

 * @author:midas

 * @time:2014-9-18 下午08:46:12
 */
public interface IFlightInfoService {
	
	/**
	 * 
	 * @Description:TODO void
	 * @exception:
	 * @author: midas
	 * @time:2014-9-18 下午08:45:54
	 */
	public List<FlightInfo> findByFlightInfoByAirTicketId(Long airticketId);

	public abstract void save(Set<FlightInfo> flightInfo);
	
	public abstract FlightInfo save(FlightInfo flightInfo);

	public abstract void delFlightInfoList(List<FlightInfo> flightInfos);

	public abstract FlightInfo findById(Long id);

	public abstract void delFlightInfo(FlightInfo flightInfo);

	public abstract void delFlightInfoById(Long id);

	/*public abstract void delFlightInfoByIds(List<Long> ids);*/

}
