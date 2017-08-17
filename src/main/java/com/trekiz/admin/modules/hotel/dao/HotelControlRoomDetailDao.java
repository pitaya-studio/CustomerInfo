/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotel.entity.HotelControlRoomDetail;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */





public interface HotelControlRoomDetailDao  extends BaseDao<HotelControlRoomDetail> {
	
	public HotelControlRoomDetail getByUuid(String uuid);
	
	/**
	 * 根据控房明细UUID查询所有的房型信息 add by zhanghao
	 * @param detailUuid
	 * @return
	 */
	public List<HotelControlRoomDetail> getListByDetailUuid(String detailUuid);
	
	/**
	 * 根据控房明细UUID查询所有的房型详细信息（包含房型名称）
	*<p>Title: getNamesByDetailUuid</p>
	* @return List<HotelControlRoomDetail> 返回类型
	* @author majiancheng
	* @date 2015-6-19 下午2:33:59
	* @throws
	 */
	public List<HotelControlRoomDetail> getNamesByDetailUuid(String detailUuid);
	
	/**
	 * 根据控房uuid获取控房的房型详细信息
	*<p>Title: getByControlUuid</p>
	* @return List<HotelControlRoomDetail> 返回类型
	* @author majiancheng
	* @date 2015-8-7 下午9:22:32
	* @throws
	 */
	public List<HotelControlRoomDetail> getByHotelControlUuid(String hotelControlUuid);
}
