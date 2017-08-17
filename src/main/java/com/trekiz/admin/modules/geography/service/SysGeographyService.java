/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.geography.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.geography.entity.SysGeography;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface SysGeographyService{
	
	public void save (SysGeography sysGeography);
	
	public void update (SysGeography sysGeography);
	
	public SysGeography getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<SysGeography> find(Page<SysGeography> page, SysGeography sysGeography);
	
	public List<SysGeography> find( SysGeography sysGeography);

	public List<SysGeography> getAllByContion(Map<String, String> conditon);
	
	public List<SysGeography> getSearchList(Map<String, String> conditon);
	
	public  List<Map<String,Object>> getChildList(Map<String, String> conditon);
	
	public  String getParentName(Map<String, String> conditon);
	
	public  Map<String, String> delGeography(Long id);
	
	public  List<Map<String,Object>> getModifyGeography(Map<String, String> conditon);

	public   void saveSort(String[] id,String[] sort, SysGeography sysGeography);

	public List<Object[]> getGeographyList(String lable, String parentUuids);

	/**
	 * 获取所有的国家名称
		* 
		* @param 
		* @return Map<String,String>
		* @author majiancheng
		* @Time 2015-4-15
	 */
	public Map<String, String> getAllCountry();

	public  String getGeoIds(String id);
	
	public List<Object[]> getAllGeographyList();
	
	public List<Object[]> getGeographyLevel(String lable);

	public List<Object[]> getSecondList(int id);

	public List<Object[]> getSecondList1(int id);

	public String getParentUuids(String id);

	public String getParentUuidsForRegion(String ids);
	
	public void updateGeographys(List<SysGeography> sysGeographys);
	
	public List<Map<String, Object>> getGeographysBySql(String sql, Object... parameter);
	
	/**
	 * 根据uuid获取地理信息名称
		* 
		* @param uuid
		* @return String
		* @author majiancheng
		* @Time 2015-5-8
	 */
	public String getNameCnByUuid(String uuid);
	
	/**
	 * 获取所有的国家名称
	 */
	public List<SysGeography> getAllCountryName();
}
