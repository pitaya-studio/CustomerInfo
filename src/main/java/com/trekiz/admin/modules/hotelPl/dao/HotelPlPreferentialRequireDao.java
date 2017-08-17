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





public interface HotelPlPreferentialRequireDao  extends BaseDao<HotelPlPreferentialRequire> {
	
	public HotelPlPreferentialRequire getByUuid(String uuid);
	
	/**
	 * 根据优惠信息uuid获取优惠要求信息
	*<p>Title: findRequireByPreferentialUuid</p>
	* @return HotelPlPreferentialRequire 返回类型
	* @author majiancheng
	* @date 2015-6-25 下午9:38:04
	* @throws
	 */
	public HotelPlPreferentialRequire findRequireByPreferentialUuid(String preferentialUuid);
	
	/**
	 * 根据价单uuid集合删除优惠要求信息
	*<p>Title: deleteByPlPreferentialUuids</p>
	* @return int 返回类型
	* @author majiancheng
	* @date 2015-7-16 上午10:52:51
	* @throws
	 */
	public int deleteByPlPreferentialUuids(List<String> preferentialUuids);
}
