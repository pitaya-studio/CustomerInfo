/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.dao;
import com.trekiz.admin.common.persistence.BaseDao;
import java.util.*;

import com.trekiz.admin.modules.hotelPl.entity.*;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface HotelPlPreferentialTaxDao  extends BaseDao<HotelPlPreferentialTax> {
	
	public HotelPlPreferentialTax getByUuid(String uuid);
	
	/**
	 * 根据优惠信息uuid获取税金集合
	*<p>Title: getTaxsByPreferentialUuid</p>
	* @return List<HotelPlPreferentialTax> 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午9:49:07
	* @throws
	 */
	public List<HotelPlPreferentialTax> getTaxsByPreferentialUuid(String preferentialUuid);
	
	/**
	 * 根据优惠信息uuid获取税金Map集合
	*<p>Title: getTaxsByPreferentialUuid</p>
	* @return Map<String,List<HotelPlPreferentialTax>> 返回类型
	* @author majiancheng
	* @date 2015-7-15 下午12:20:53
	* @throws
	 */
	public Map<String, List<HotelPlPreferentialTax>> getTaxMapByPreferentialUuid(String preferentialUuid);
	
	/**
	 * 根据价单uuid集合删除税金信息
	*<p>Title: deleteByPlPreferentialUuids</p>
	* @return int 返回类型
	* @author majiancheng
	* @date 2015-7-16 上午10:53:31
	* @throws
	 */
	public int deleteByPlPreferentialUuids(List<String> preferentialUuids);
	
	
}
