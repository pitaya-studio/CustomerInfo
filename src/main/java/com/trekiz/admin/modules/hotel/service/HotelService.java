/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import com.trekiz.admin.common.persistence.Page;
import java.util.*;

import com.trekiz.admin.modules.hotel.entity.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface HotelService{
	
	public void save (Hotel hotel,List<HotelContact> hcList,List<HotelAnnex> axList);
	
	public void update (Hotel hotel,List<HotelContact> hcList,List<HotelAnnex> axList);
	
	public Hotel getById(Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public void removeByUuid(java.lang.String value);
	
	public Page<Hotel> find(Page<Hotel> page, Hotel hotel);
	
	public List<Hotel> find( Hotel hotel);
	
	public Hotel getByUuid(String value);
	
	/**
	 * 根据岛屿和公司Id查询酒店集合
	 * @param islandUuid	岛屿uuid
	 * @param companyId		公司id
	 * @return
	 */
	public List<Hotel> findByIslandUuidAndCompany(String islandUuid, int companyId);
	
	/**
	 * 根据酒店uuid获取酒店星级值
	*<p>Title: getHotelStarByHotelUuid</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-6-12 下午5:48:33
	* @throws
	 */
	public Integer getHotelStarValByHotelUuid(String hotelUuid);
	
	/**
	 * 根据酒店uuid获取酒店集团
	*<p>Title: getHotelGroupByUuid</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-6-28 下午4:36:17
	* @throws
	 */
	public String getHotelGroupByUuid(String hotelUuid);
	
	/**
	 * 获取批发商下的所有酒店
	*<p>Title: findByCompany</p>
	* @return List<Hotel> 返回类型
	* @author majiancheng
	* @date 2015-7-18 下午3:23:30
	* @throws
	 */
	public List<Hotel> findHotelsByCompanyId(Integer companyId);
	
	/**
	 * 根据批发商id和国家获取所有酒店
	*<p>Title: findHotelsByCompanyIdAndCountry</p>
	* @return List<Hotel> 返回类型
	* @author majiancheng
	* @date 2015-7-30 下午4:01:09
	* @throws
	 */
	public List<Hotel> findHotelsByCompanyIdAndCountry(Integer companyId, String country);
}
