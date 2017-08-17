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
import com.trekiz.admin.modules.flight.entity.FlightControlDetail;
import com.trekiz.admin.modules.flight.query.FlightControlQuery;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface FlightControlDetailService{
	
	public void save (FlightControlDetail flightControlDetail);
	
	public void update (FlightControlDetail flightControlDetail);
	
	public FlightControlDetail getById(java.lang.Integer value);
	
	public Page<Map<String, Object>> FlightControlList(FlightControlQuery query,HttpServletRequest request,HttpServletResponse response);
	
	public void removeById(java.lang.Integer value);
	
	public Page<FlightControlDetail> find(Page<FlightControlDetail> page, FlightControlDetail flightControlDetail);
	
	public List<FlightControlDetail> find( FlightControlDetail flightControlDetail);
	
	public FlightControlDetail getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public void delStatusByUuid(String uuid);
	
	public List<FlightControlDetail> getByFlightControlUuid(String flightControlUuid);
	
	
}
