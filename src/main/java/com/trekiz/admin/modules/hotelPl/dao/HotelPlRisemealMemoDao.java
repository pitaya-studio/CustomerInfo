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





public interface HotelPlRisemealMemoDao  extends BaseDao<HotelPlRisemealMemo> {
	
	public HotelPlRisemealMemo getByUuid(String uuid);
	
	/**
	 * 根据酒店价单uuid获取升餐备注信息
	*<p>Title: findPlRisemealMemoByHotelPlUuid</p>
	* @return HotelPlRisemealMemo 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午6:23:04
	* @throws
	 */
	public List<HotelPlRisemealMemo> findPlRisemealMemosByHotelPlUuid(String hotelPlUuid);
	
	/**
	 * 根据价单uuid删除价单升餐备注信息
	*<p>Title: deletePlRisemealMemo</p>
	* @return int 返回类型
	* @author majiancheng
	* @date 2015-7-15 下午9:23:34
	* @throws
	 */
	public int deletePlRisemealMemoByPlUuid(String hotelPlUuid);
}
