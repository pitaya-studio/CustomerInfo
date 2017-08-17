/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.HotelAnnex;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.island.entity.Island;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface IslandService{
	
public void save (Island Island);
	
	public void update (Island Island);
	
	public Island getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<Island> find(Page<Island> page, Island Island);
	
	public List<Island> find(Island Island);
	
	/**
	* 根据uuid查询海岛信息
	* @param uuid UUID
	* @return 
	* @author majiancheng
	* @Time 2015-4-14
	*/
	public Island getByUuid(String uuid);
	
	
	/**
	* 根据uuid删除海岛信息（逻辑删除）
	* @param uuid UUID
	* @return 
	* @author majiancheng
	* @Time 2015-4-14
	*/
	public void removeByUuid(String uuid);
	
	/**
	* 查询海岛中名称是否存在(名称存在返回true，反之返回false)
	* @param id 字典ID
	* @param islandName 海岛名称
	* @param companyId 公司ID
	* @return 
	* @author majiancheng
	* @Time 2015-4-14
	*/
	public boolean findIsExist(String uuid, String islandName, Long companyId);
	
	/**
	 * 保存岛屿信息以及上传文件信息
		* 
		* @param island 岛屿
		* @param annexList 上传文件集合
		* @return void
		* @author majiancheng
		* @Time 2015-5-5
	 */
	public void saveIsland(Island island, List<HotelAnnex> annexList);
	
	/**
	 * 更新岛屿信息以及上传文件信息
		* 
		* @param island 岛屿
		* @param annexList 上传文件集合
		* @return void
		* @author majiancheng
		* @Time 2015-5-5
	 */
	public void updateIsland(Island island, List<HotelAnnex> annexList);
	
	/**
	 * 根据公司ID获取岛屿列表
	 * @param companyId
	 * @return
	 */
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
