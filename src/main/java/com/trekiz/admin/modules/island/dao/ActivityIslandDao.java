/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.dao;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.island.entity.ActivityIsland;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */





public interface ActivityIslandDao  extends BaseDao<ActivityIsland> {
	
	public ActivityIsland getByUuid(String uuid);
	
	public <E> Page<E> findBySql(Page<E> page, String sqlString,String oldsqlString, Class<?> resultClass, Object... parameter);
}
