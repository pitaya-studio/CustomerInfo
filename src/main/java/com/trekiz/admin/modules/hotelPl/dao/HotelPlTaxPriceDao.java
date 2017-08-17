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
import com.trekiz.admin.modules.hotelPl.query.HotelPlTaxPriceQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface HotelPlTaxPriceDao  extends BaseDao<HotelPlTaxPrice> {
	
	public HotelPlTaxPrice getByUuid(String uuid);
	
	/**
	 * 根据酒店uuid获取酒店价单税金集合
	*<p>Title: findHotelPlTaxPricesByHotelPlUuid</p>
	* @return List<HotelPlTaxPrice> 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午3:23:02
	* @throws
	 */
	public List<HotelPlTaxPrice> findHotelPlTaxPricesByHotelPlUuid(String hotelPlUuid) ;
	
	/**
	 * 自动报价 根据条件筛选 符合条件的税金 add by zhanghao
	 * @return
	 */
	public List<HotelPlTaxPrice> getHotelPlTaxPrice4AutoQuotedPrice( HotelPlTaxPriceQuery hotelPlTaxPriceQuery);
	
	/**
	 * 根据酒店价单uuid删除价单税金信息
	*<p>Title: deleteHotelPlTaxByHotelPlUuid</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-7-15 下午8:30:59
	* @throws
	 */
	public int deleteHotelPlTaxByHotelPlUuid(String hotelPlUuid); 
}
