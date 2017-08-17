/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlIslandway;
import com.trekiz.admin.modules.hotelPl.input.HotelPlIslandwayInput;
import com.trekiz.admin.modules.hotelPl.query.HotelPlIslandwayQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelPlIslandwayService{
	
	public void save (HotelPlIslandway hotelPlIslandway);
	
	public void save (HotelPlIslandwayInput hotelPlIslandwayInput);
	
	public void update (HotelPlIslandway hotelPlIslandway);
	
	public HotelPlIslandway getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelPlIslandway> find(Page<HotelPlIslandway> page, HotelPlIslandwayQuery hotelPlIslandwayQuery);
	
	public List<HotelPlIslandway> find( HotelPlIslandwayQuery hotelPlIslandwayQuery);
	
	public HotelPlIslandway getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 根据酒店uuid获取交通费用集合(使用上岛方式进行分组)
	*<p>Title: findIslandWaysByHotelPlUuid</p>
	* @return List<HotelPlIslandway> 返回类型
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
	
}
