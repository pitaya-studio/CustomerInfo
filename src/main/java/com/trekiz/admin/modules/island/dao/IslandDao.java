/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.dao;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.island.entity.Island;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */





public interface IslandDao  extends BaseDao<Island> {
	/**
	* 根据uuid查询海岛信息
	* @param uuid UUID
	* @return 
	* @author majiancheng
	* @Time 2015-4-16
	*/
	public Island getByUuid(String uuid);
	
	@Query("from Island where wholesalerId = ?1 and delFlag = '" + Context.DEL_FLAG_NORMAL+ "'")
	public List<Island> findListByCompanyId(Integer companyId);
	
	/**
	 * 根据海岛游uuid获取上岛方式集合
	*<p>Title: findIslandWaysByIslandUuid</p>
	* @return List<SysCompanyDictView> 返回类型
	* @author majiancheng
	* @date 2015-7-6 上午10:47:27
	* @throws
	 */
	public List<SysCompanyDictView> findIslandWaysByIslandUuid(String islandUuid);
}
