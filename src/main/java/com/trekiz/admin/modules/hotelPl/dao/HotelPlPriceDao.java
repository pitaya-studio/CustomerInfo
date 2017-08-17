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
import com.trekiz.admin.modules.hotelPl.module.bean.HotelPlPriceJsonBean;
import com.trekiz.admin.modules.hotelPl.query.HotelPlPriceQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface HotelPlPriceDao  extends BaseDao<HotelPlPrice> {
	
	public HotelPlPrice getByUuid(String uuid);
	
	/**
	 * 根据酒店价单uuid获取酒店价单价格集合(使用房型和日期分组)->详情使用接口
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
	
	/**
	 * 根据酒店价单uuid获取酒店价单价格集合(只使用房型分组)->修改使用接口
	*<p>Title: findHotelPlPricesByHotelPlUuid</p>
	* @return Map<String,List<HotelPlPrice>> 返回类型
	* @author majiancheng
	* @date 2015-7-14 下午5:31:05
	* @throws
	 */
	public Map<String, List<HotelPlPriceJsonBean>> findHotelPlPricesByHotelPlUuid(String hotelPlUuid);
	
	/**
	 * 根据酒店价单uuid删除价单价格表记录
	*<p>Title: deleteHotelPlPriceByHotelPlUuid</p>
	* @return int 返回类型
	* @author majiancheng
	* @date 2015-7-15 下午9:12:49
	* @throws
	 */
	public int deleteHotelPlPriceByHotelPlUuid(String hotelPlUuid);
	
	
	
	public List<HotelPlPrice> getPriceList (String hotelPlUuid);
	
}
