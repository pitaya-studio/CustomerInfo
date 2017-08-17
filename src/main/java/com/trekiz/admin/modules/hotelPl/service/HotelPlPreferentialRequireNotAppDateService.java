/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPreferentialRequireNotAppDate;
import com.trekiz.admin.modules.hotelPl.input.HotelPlPreferentialRequireNotAppDateInput;
import com.trekiz.admin.modules.hotelPl.query.HotelPlPreferentialRequireNotAppDateQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelPlPreferentialRequireNotAppDateService{
	
	public void save (HotelPlPreferentialRequireNotAppDate hotelPlPreferentialRequireNotAppDate);
	
	public void save (HotelPlPreferentialRequireNotAppDateInput hotelPlPreferentialRequireNotAppDateInput);
	
	public void update (HotelPlPreferentialRequireNotAppDate hotelPlPreferentialRequireNotAppDate);
	
	public HotelPlPreferentialRequireNotAppDate getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelPlPreferentialRequireNotAppDate> find(Page<HotelPlPreferentialRequireNotAppDate> page, HotelPlPreferentialRequireNotAppDateQuery hotelPlPreferentialRequireNotAppDateQuery);
	
	public List<HotelPlPreferentialRequireNotAppDate> find( HotelPlPreferentialRequireNotAppDateQuery hotelPlPreferentialRequireNotAppDateQuery);
	
	public HotelPlPreferentialRequireNotAppDate getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public boolean batchDelete(String[] uuids);
}
