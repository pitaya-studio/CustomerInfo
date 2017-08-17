/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotel.entity.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */





public interface HotelRoomOccuRateDetailDao  extends BaseDao<HotelRoomOccuRateDetail> {
	
	public HotelRoomOccuRateDetail getByUuid(String uuid);
	
	/**
	 * 根据容住率主表uuid和游客类型表uuid获取容住率详情表信息
		* 
		* @param roomOccuRateUuid 容住率主表uuid
		* @param travelerTypeUuid 游客类型表uuid
		* @return HotelRoomOccuRateDetail
		* @author majiancheng
		* @Time 2015-5-5
	 */
	public HotelRoomOccuRateDetail getByRoomOccuRateUuidAndTravelerTypeUuid(String roomOccuRateUuid, String travelerTypeUuid);
	
	/**
	 * 根据酒店容住率主表uuid删除酒店容住率明细表数据
		* 
		* @param roomOccuRateUuid 酒店容住率uuid
		* @return int
		* @author majiancheng
		* @Time 2015-5-5
	 */
	public int removeByRoomOccuRateUuid(String roomOccuRateUuid);
	
	/**
	 * 根据酒店房型uuid获取酒店容住率详情uuid集合
	     * <p>@Description TODO</p>
		 * @Title: getDetailUuidsByRoomUuid
	     * @return List<String>
	     * @author majiancheng       
	     * @date 2015-10-12 下午12:17:52
	 */
	public List<String> getDetailUuidsByRoomUuid(String hotelRoomUuid);
	
	/**
	 * 根据酒店容住率明细表uuid集合删除数据
	     * <p>@Description TODO</p>
		 * @Title: removeByOccuRateDetailUuids
	     * @return int
	     * @author majiancheng       
	     * @date 2015-10-12 下午12:24:03
	 */
	public int removeByOccuRateDetailUuids(List<String> toDelOccuRateDetailUuids);
}
