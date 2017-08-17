/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupMealRise;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface ActivityIslandGroupMealRiseDao  extends BaseDao<ActivityIslandGroupMealRise> {
	
	public ActivityIslandGroupMealRise getByUuid(String uuid);
	
	/**
	 * 根据团期餐型uuid获取升餐集合
	*<p>Title: getbyGroupMealUuid</p>
	* @return List<ActivityIslandGroupMealRise> 返回类型
	* @author majiancheng
	* @date 2015-6-25 上午9:57:41
	* @throws
	 */
	public List<ActivityIslandGroupMealRise> getbyGroupMealUuid(String mealuuid);

	public int updateByActivityIslandGroupUUid(String islandGroupUuid);

	public List<ActivityIslandGroupMealRise> getMealRiseListByGroupUuid(String uuid);
	
	public List<ActivityIslandGroupMealRise> getMealRiseByMealUuid(String uuid);
}
