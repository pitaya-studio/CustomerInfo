/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface ActivityHotelGroupDao  extends BaseDao<ActivityHotelGroup> {
	
	public ActivityHotelGroup getByUuid(String uuid);

	public List<ActivityHotelGroup> findGroupsByActivityHotelUuid(String uuid);

	/**
	 * 根据产品的uuid来跟新产品下团期的状态
	 * @param uuid
	 * @param status
	 * @return
	 */
	public int updateGroupStatusByActivityUuid(String uuid, String status);
	/**
	 * 根据uuid来更新团期的状态
	 * @param uuid
	 * @param status
	 * @return
	 */
	public int updateGroupStatusByUuid(String uuid, String status);
	/**
	 * 更新计算提成状态
	 * @param groupId 团期Id
	 * @param iscommission 计算提成状态
	 * @return int
	 * @author zhaohaiming
	 * */
	public int updateIscommission(Integer id,int iscommission);
}
