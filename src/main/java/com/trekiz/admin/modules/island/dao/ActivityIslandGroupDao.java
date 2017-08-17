/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface ActivityIslandGroupDao  extends BaseDao<ActivityIslandGroup> {
	
	public ActivityIslandGroup getByUuid(String uuid);
	
	/**
	 * 根据海岛产品uuid获取所有团期信息
	*<p>Title: findGroupByActivityIslandUuid</p>
	* @return List<ActivityIslandGroup> 返回类型
	* @author majiancheng
	* @date 2015-5-27 下午6:03:17
	* @throws
	 */
	public List<ActivityIslandGroup> findGroupByActivityIslandUuid(String activityIslandUuid);
	
	/**
	 * 批量更新海岛游产品状态
	*<p>Title: batchUpdateStatusByIslandUuidArr</p>
	* @param uuidArray 海岛游产品uuid集合
	* @return int 返回类型
	* @author majiancheng
	* @date 2015-5-28 上午11:42:27
	* @throws
	 */
	public int batchUpdateStatusByIslandUuidArr(String[] islandUuidArray, String status);
	
	/**
	 * 批量更新海岛游团期状态
	*<p>Title: updateGroupStatusByUuid</p>
	* @param uuidArray 海岛游团期uuid集合
	* @return int 返回类型
	* @author majiancheng
	* @date 2015-5-28 上午11:55:30
	* @throws
	 */
	public int batchUpdateStatusByGroupUuidArr(String[] uuidArray, String status);
	/**
	 * 更新计算提成状态
	 * @param groupId 团期Id
	 * @param iscommission 计算提成状态
	 * @return int
	 * @author zhaohaiming
	 * */
	public int updateIscommission(Integer id,int iscommission);
}
