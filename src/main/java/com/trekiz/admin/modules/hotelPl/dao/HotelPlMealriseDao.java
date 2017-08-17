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
import com.trekiz.admin.modules.hotelPl.query.HotelPlMealriseQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface HotelPlMealriseDao  extends BaseDao<HotelPlMealrise> {
	
	public HotelPlMealrise getByUuid(String uuid);
	
	/**
	 * 根据酒店uuid获取酒店简单升餐表集合(使用餐型和日期进行分组)->用于价单详情
	*<p>Title: findPlMealRisesByHotelPlUuid</p>
	* @return Map<String,List<HotelPlMealrise>> 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午5:51:58
	* @throws
	 */
	public Map<String, Map<String, List<HotelPlMealrise>>> findPlMealRisesByHotelPlUuid(String hotelPlUuid);
	
	/**
	 * 自动报价 根据条件筛选 符合条件的生餐费 add by zhanghao
	 * @return
	 */
	public List<HotelPlMealrise> getHotelPlMealrise4AutoQuotedPrice(HotelPlMealriseQuery hotelPlMealriseQuery);
	
	/**
	 * 根据酒店价单uuid获取升餐集合(使用餐型进行分组)->用于价单修改
	*<p>Title: findHotelPlMealriseByHotelPlUuid</p>
	* @return Map<String,List<HotelPlMealrise>> 返回类型
	* @author majiancheng
	* @date 2015-7-15 上午9:45:00
	* @throws
	 */
	public Map<String, List<HotelPlMealrise>> findHotelPlMealriseByHotelPlUuid(String hotelPlUuid);
	
	/**
	 * 根据价单uuid删除价单升餐集合
	*<p>Title: deletePlMealriseByPlUuid</p>
	* @return int 返回类型
	* @author majiancheng
	* @date 2015-7-15 下午9:22:28
	* @throws
	 */
	public int deletePlMealriseByPlUuid(String hotelPlUuid);
}
