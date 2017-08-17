/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotelPl.entity.HotelPlPrice;
import com.trekiz.admin.modules.hotelPl.query.HotelPlPriceQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface HotelPlPriceService{
	
	public void save (HotelPlPrice hotelPlPrice);
	
	public void update (HotelPlPrice hotelPlPrice);
	
	public HotelPlPrice getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<HotelPlPrice> find(Page<HotelPlPrice> page, HotelPlPriceQuery hotelPlPriceQuery);
	
	public List<HotelPlPrice> find( HotelPlPriceQuery hotelPlPriceQuery);
	
	public HotelPlPrice getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public List<HotelPlPrice> getPriceList(String hotelPlUuid);
	
	
	/**
	 * 根据酒店价单uuid获取酒店价单价格集合(使用房型分组)
	*<p>Title: findPlPricesByHotelPlUuid</p>
	* @return List<HotelPlPrice> 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午3:29:11
	* @throws
	 */
	public Map<String, Map<String, List<HotelPlPrice>>> findPlPricesByHotelPlUuid(String hotelPlUuid);
	
	/**
	 * 自动报价 根据条件筛选 符合条件的房费 add by zhanghao
	 * @return
	 */
	public List<HotelPlPrice> getHotelPlPriceQuery4AutoQuotedPrice(HotelPlPriceQuery hotelPlPriceQuery);
	
}
