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
import com.trekiz.admin.modules.hotelPl.query.HotelPlPreferentialQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface HotelPlPreferentialDao  extends BaseDao<HotelPlPreferential> {
	
	public HotelPlPreferential getByUuid(String uuid);
	
	/**
	 * 根据酒店价单uuid获取酒店价单优惠信息集合
	*<p>Title: findPlPreferentialsByHotelPlUuid</p>
	* @return List<HotelPlPreferential> 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午4:02:37
	* @throws
	 */
	public List<HotelPlPreferential> findPlPreferentialsByHotelPlUuid(String hotelPlUuid);
	
	/**
	 * 自动报价 根据条件筛选 符合条件的优惠信息 add by zhanghao
	 * 只筛选符合日期的优惠，具体是否适用会进一步做筛选
	 * @return
	 */
	public List<HotelPlPreferential> getHotelPlPreferentials4AutoQuotedPrice( HotelPlPreferentialQuery hotelPlPreferentialQuery);
	
	/**
	 * 删除同一价单下的不属于该多个优惠下的优惠信息
	*<p>Title: deleteNotContainPreferentialUuids</p>
	* @return int 返回类型
	* @author majiancheng
	* @date 2015-7-16 下午8:24:57
	* @throws
	 */
	public int deleteNotContainPreferentialUuids(List<String> preferentialUuids, String hotelPlUuid);
	
	/**
	 * 根据价单uuid获取可关联的优惠信息
	*<p>Title: getRelPlPreferentialsByPlUuid</p>
	* @return List<HotelPlPreferential> 返回类型
	* @author majiancheng
	* @date 2015-7-23 上午11:59:18
	* @throws
	 */
	public List<HotelPlPreferential> getRelPlPreferentialsByPlUuid(String hotelPlUuid);
}
