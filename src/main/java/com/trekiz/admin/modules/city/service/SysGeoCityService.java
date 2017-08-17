/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.city.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.city.entity.SysGeoCity;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface SysGeoCityService{
	
	public void save (SysGeoCity sysGeoCity);
	
	public void update (SysGeoCity sysGeoCity);
	
	public SysGeoCity getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<SysGeoCity> find(Page<SysGeoCity> page, SysGeoCity sysGeoCity);
	
	public List<SysGeoCity> find( SysGeoCity sysGeoCity);
	
	public List<Map<String,Object>> getGeoCityList(String type);

	public void updateAreas(Integer[] startCityId, Integer[] transCityId,
			Integer[] leaveCityId, Integer[] interCityId);
	
	public List<Map<String,Object>> getDestSelectList();
	
	public List<Map<String,Object>> getDestinationList();
	
	public void addDest(String id);
	
	public void delDest(String id);
}
