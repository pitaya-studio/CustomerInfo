/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.traveler.service;

import com.trekiz.admin.common.persistence.Page;
import java.util.*;

import com.trekiz.admin.modules.traveler.entity.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

public interface TravelerTypeService{
	
	public void save (TravelerType travelerType);
	
	public void update (TravelerType travelerType);
	
	public TravelerType getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<TravelerType> find(Page<TravelerType> page, TravelerType travelerType);
	
	public List<TravelerType> find( TravelerType travelerType);
	
	/**
	* 根据uuid查询游客类型信息
	* @param uuid UUID
	* @return 
	* @author 
	* @Time 
	*/
	public TravelerType getByUuid(String uuid);
	
	/**
	* 根据uuid删除游客类型信息（逻辑删除）
	* @param uuid UUID
	* @return 
	* @author 
	* @Time 
	*/
	public void removeByUuid(String uuid);
	
	/**
	* 查询游客类型中名称是否存在(名称存在返回true，反之返回false)
	* @param id 字典ID
	* @param name 游客类型名称
	* @param wholesalerId 供应商ID
	* @return 
	* @author majiancheng
	* @Time 2015-4-7
	*/
	public boolean findIsExist(String uuid, String name, Long wholesalerId);
	
	/**
	 * 根据系统游客类型查看该批发商是否包含成人
	*<p>Title: findIsExistBySysTravelerType</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-6-2 上午10:51:24
	* @throws
	 */
	public boolean findIsExistBySysTravelerType(String uuid, String sysTravelerType, int companyId);
	
	/**
	 * 根据批发商id获取游客类型集合
	*<p>Title: getTravelerTypesByWholesalerId</p>
	* @return List<TravelerType> 返回类型
	* @author majiancheng
	* @date 2015-6-30 下午4:50:26
	* @throws
	 */
	public List<TravelerType> getTravelerTypesByWholesalerId(Integer wholesalerId);

	/**
	* 根据uuid查询游客类型信息(忽略数据的删除状态)
	* @param uuid UUID
	* @return 
	* @author wangxv
	* @Time 
	*/
	public TravelerType getTravelerName(String uuid);
	
	/**
	 * 根据批发商将游客类型sort进行累加操作
	 * @Description: 
	 * @param @param wholesalerId
	 * @param @return   
	 * @return boolean  
	 * @throws
	 * @author majiancheng
	 * @date 2015-11-18 下午1:48:25
	 */
	public boolean cumulationSortByWholesalerId(Integer wholesalerId);
}
