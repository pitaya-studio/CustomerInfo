/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandShare;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface ActivityIslandShareDao  extends BaseDao<ActivityIslandShare> {
	
	public ActivityIslandShare getByUuid(String uuid);
	
	/**
	 * 根据海岛产品uuid获取产品分享人
	*<p>Title: findShareUserByIsland</p>
	* @return List<ActivityIslandShare> 返回类型
	* @author majiancheng
	* @date 2015-5-28 上午10:06:04
	* @throws
	 */
	public List<ActivityIslandShare> findShareUserByIsland(String activityIslandUuid);
	
	/**
	 * 根据海岛uuid删除对应的分享表数据
	*<p>Title: deleteShareDataByIslandUuid</p>
	* @param islandUuid 海岛uuid
	* @return int 返回类型
	* @author majiancheng
	* @date 2015-5-28 下午6:18:27
	* @throws
	 */
	public int deleteShareDataByIslandUuid(String islandUuid);
}
