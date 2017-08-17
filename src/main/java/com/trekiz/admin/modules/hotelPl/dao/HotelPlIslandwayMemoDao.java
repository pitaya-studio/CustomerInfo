/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotelPl.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotelPl.entity.*;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface HotelPlIslandwayMemoDao  extends BaseDao<HotelPlIslandwayMemo> {
	
	public HotelPlIslandwayMemo getByUuid(String uuid);
	
	/**
	 * 根据酒店价单uuid获取上岛方式备注
	*<p>Title: getPlIslandwayMemoByHotelPlUuid</p>
	* @return HotelPlIslandwayMemo 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午6:18:53
	* @throws
	 */
	public List<HotelPlIslandwayMemo> findPlIslandwayMemosByHotelPlUuid(String hotelPlUuid);
	
	/**
	 * 根据价单uuid删除交通费用备注集合
	*<p>Title: deletePlIslandwayMemoByPlUuid</p>
	* @return int 返回类型
	* @author majiancheng
	* @date 2015-7-15 下午9:21:22
	* @throws
	 */
	public int deletePlIslandwayMemoByPlUuid(String hotelPlUuid);
}
