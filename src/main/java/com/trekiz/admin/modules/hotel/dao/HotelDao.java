/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotel.entity.Hotel;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */





public interface HotelDao  extends BaseDao<Hotel> {
	public Hotel getByUuid(String value);
	
	/**
	 * 根据岛屿和公司Id查询酒店集合
	 * @param islandUuid	岛屿uuid
	 * @param companyId		公司id
	 * @return
	 */
	public List<Hotel> findByIslandUuidAndCompany(String islandUuid, int companyId);
	
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
	 * 根据批发商Id获取酒店集合
	*<p>Title: findHotelsByCompanyId</p>
	* @return List<Hotel> 返回类型
	* @author majiancheng
	* @date 2015-7-18 下午3:24:53
	* @throws
	 */
	public List<Hotel> findHotelsByCompanyId(Integer companyId);
	
	/**
	 * 根据酒店集团和公司id查询所有酒店
	*<p>Title: findHotelsByGroupAndCompany</p>
	* @return List<Hotel> 返回类型
	* @author majiancheng
	* @date 2015-7-21 下午3:12:26
	* @throws
	 */
	public List<Hotel> findHotelsByGroupAndCompany(String hotelGroup, Integer companyId);
	
	/**
	 * 根据批发商id和国家获取所有酒店
	*<p>Title: findHotelsByCompanyIdAndCountry</p>
	* @return List<Hotel> 返回类型
	* @author majiancheng
	* @date 2015-7-30 下午4:02:34
	* @throws
	 */
	public List<Hotel> findHotelsByCompanyIdAndCountry(Integer companyId, String country);
}
