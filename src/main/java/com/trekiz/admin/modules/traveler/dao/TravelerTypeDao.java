/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.traveler.dao;
import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.traveler.entity.TravelerType;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */





public interface TravelerTypeDao  extends BaseDao<TravelerType> {
	
	/**
	* 根据uuid查询游客类型信息
	* @param uuid UUID
	* @return 
	* @author majiancheng
	* @Time 2015-5-4
	*/
	public TravelerType getByUuid(String uuid);
	
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
	* 根据uuid查询游客类型信息(忽略是否处于删除状态)
	* @param uuid UUID
	* @return 
	* @author wangxv
	*/
	public TravelerType getTravelerName(String uuid);
	
	/**
	 * 根据系统游客类型批量更新历史游客类型数据
	*<p>Title: batchUpdateHistoryTravelerType</p>
	* @return boolean 返回类型
	* @author majiancheng
	* @date 2015-8-31 下午2:15:01
	* @throws
	 */
	public boolean batchUpdateTravelerType(String sysTravelerTypeUuid);
	
	/**
	 * 根据批发商id获取所有的游客类型
	 * @Description: 
	 * @param @param wholesalerId
	 * @param @return   
	 * @return List<TravelerType>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-11-18 下午1:50:19
	 */
	public List<TravelerType> findAllByWholesalerId(Integer wholesalerId);
	
}
