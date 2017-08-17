/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.flight.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.flight.entity.FlightControl;
import com.trekiz.admin.modules.flight.input.FlightControlInput;
import com.trekiz.admin.modules.flight.query.FlightControlQuery;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;


/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface FlightControlService{
	
	public void save (FlightControl flightControl);
	
	public void update (FlightControl flightControl);
	
	public FlightControl getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<FlightControl> find(Page<FlightControl> page, FlightControl flightControl);
	
	public List<FlightControl> find( FlightControl flightControl);
	
	public FlightControl getByUuid(String uuid);
	
	public Map<String, String> updateFlightControl(FlightControlInput flightControl,boolean updateFlag, List<HotelAnnex> axList);
	
	public void removeByUuid(String uuid);
	
	public Page<Map<String, Object>> getFlightList(HttpServletRequest request, HttpServletResponse response,
			FlightControlQuery flightControlQuery);
	
	public List<List<Map<String, String>>> getFlightListSubs(List<Map<String, Object>> loop);
    
	public Map<String,String> saveFlightControl(FlightControlInput input) throws Exception;
	
	public Map<String,String> saveFlightControlDate(FlightControlInput input) throws Exception;
	
}
