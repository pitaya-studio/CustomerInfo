/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.flight.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.flight.entity.FlightControlHotelDetail;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface FlightControlHotelDetailService{
	
	public void save (FlightControlHotelDetail flightControlHotelDetail);
	
	public void update (FlightControlHotelDetail flightControlHotelDetail);
	
	public FlightControlHotelDetail getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<FlightControlHotelDetail> find(Page<FlightControlHotelDetail> page, FlightControlHotelDetail flightControlHotelDetail);
	
	public List<FlightControlHotelDetail> find( FlightControlHotelDetail flightControlHotelDetail);
	
	public FlightControlHotelDetail getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public List<FlightControlHotelDetail> findByFlightControlDetailUuid(String flightControlDetailUuid);
	
	public List<FlightControlHotelDetail> getByFlightControlDetailUuid(String flightControlDetailUuid);
}
