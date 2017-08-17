/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.island.entity.ActivityIslandShare;
import com.trekiz.admin.modules.island.input.ActivityIslandShareInput;
import com.trekiz.admin.modules.island.query.ActivityIslandShareQuery;
import com.trekiz.admin.modules.sys.entity.User;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface ActivityIslandShareService{
	
	public void save (ActivityIslandShare activityIslandShare);
	
	public void save (ActivityIslandShareInput activityIslandShareInput);
	
	public void update (ActivityIslandShare activityIslandShare);
	
	public ActivityIslandShare getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<ActivityIslandShare> find(Page<ActivityIslandShare> page, ActivityIslandShareQuery activityIslandShareQuery);
	
	public List<ActivityIslandShare> find( ActivityIslandShareQuery activityIslandShareQuery);
	
	public ActivityIslandShare getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 根据海岛产品uuid获取产品分享人
	*<p>Title: findShareUserByIsland</p>
	* @return List<ActivityIslandShare> 返回类型
	* @author majiancheng
	* @date 2015-5-28 上午10:06:04
	* @throws
	 */
	public List<User> findShareUserByIsland(String activityIslandUuid);
	
	/**
	 * 保存海岛游产品分享数据
	*<p>Title: saveActivityIslandShareData</p>
	* @param accShareUsers 接受分享人
	* @param shareUser 产品分享人
	* @param islandUuid 海岛游产品uuid
	* @return void 返回类型
	* @author majiancheng
	* @date 2015-5-28 下午5:54:38
	* @throws
	 */
	public boolean saveActivityIslandShareData(String[] accShareUsers, Long shareUser, String islandUuid);
	
	/**
	 * 更新海岛游产品分享数据
	*<p>Title: updateActivityIslandShareData</p>
	* @param accShareUsers 接受分享人
	* @param shareUser 产品分享人
	* @param islandUuid 海岛游产品uuid
	* @return void 返回类型
	* @author majiancheng
	* @date 2015-5-28 下午5:54:38
	* @throws
	 */
	public boolean updateActivityIslandShareData(String[] accShareUsers, Long shareUser, String islandUuid);
	
	public List<ActivityIslandShare> findByActivityIslandUuid(String uuid);
}
