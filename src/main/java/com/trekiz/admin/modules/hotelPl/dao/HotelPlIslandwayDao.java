/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.dao;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlIslandway;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface HotelPlIslandwayDao  extends BaseDao<HotelPlIslandway> {
	
	public HotelPlIslandway getByUuid(String uuid);
	
	/**
	 * 根据酒店uuid获取交通费用集合(使用上岛方式和日期进行分组)->用于价单详情
	*<p>Title: findIslandWaysByHotelPlUuid</p>
	* @return Map<String, List<HotelPlIslandway>> 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午3:53:17
	* @throws
	 */
	public Map<String, Map<String, List<HotelPlIslandway>>> findIslandWaysByHotelPlUuid(String hotelPlUuid);
	
	
	/**
	 * 自动报价 根据条件筛选 符合条件的交通价格 add by zhanghao
	 * @return
	 */
	public List<HotelPlIslandway> getIslandWay4AutoQuotedPrice(String islandwayUuid,Date inDate,String travelerTypeUuid,String hotelPlUuid );
	
	/**
	 * 根据酒店uuid获取交通费用集合(使用上岛方式进行分组)->用于价单修改
	*<p>Title: findHotelPlIslandwaysByHotelPlUuid</p>
	* @return Map<String,Map<String,List<HotelPlIslandway>>> 返回类型
	* @author majiancheng
	* @date 2015-7-14 下午10:02:39
	* @throws
	 */
	public Map<String, List<HotelPlIslandway>> findHotelPlIslandwaysByHotelPlUuid(String hotelPlUuid);
	
	/**
	 * 根据价单uuid删除交通费用集合
	*<p>Title: deletePlIslandwayByPlUuid</p>
	* @return int 返回类型
	* @author majiancheng
	* @date 2015-7-15 下午9:20:09
	* @throws
	 */
	public int deletePlIslandwayByPlUuid(String hotelPlUuid);
}
