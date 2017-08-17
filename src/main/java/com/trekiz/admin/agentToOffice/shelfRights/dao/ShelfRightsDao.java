package com.trekiz.admin.agentToOffice.shelfRights.dao;

import java.util.List;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.sys.entity.User;

/**
 * 上架权限的dao
 * @author chao.zhang@quauq.com
 */
public interface ShelfRightsDao extends BaseDao{
	/**
	 * 根据批发商id修改上架权限状态
	 * @param companyId
	 * @author chao.zhang@quauq.com
	 */
	public void updatShelRightsStatusByCompanyId(Long companyId,Integer status);
	
	/**
	 * 根据批发商id查询该批发商下所有散拼团中的产品的团期总数（出团日期大于等于当前日期）
	 * @param whosalerId
	 * @return
	 */
	public Integer getProductCountByWhosalerId(Integer whosalerId);
	
	/**
	 * 查询在T1上上架产品团期数
	 * @author chao.zhang@quauq.com
	 */
	public Integer getT1ProductCountByActivityId(Integer activityId);
	
	/**
	 * 查询所有的散拼产品
	 * @param whosalerId
	 * @return
	 */
	public List<TravelActivity> getAllTravelActivities(Long whosalerId); 
	
	public List<User> getUserByCompanyId(Integer companyId);
	
	/**
	 * 查询该产品下T1订单总数
	 * @param companyId
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public Integer getT1OrderCount(Integer productId);
	
	/**
	 * 查询该批发商通过quauq渠道预定总人数；
	 * @param companyId
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public Integer getQuauqPersonCount(Integer companyId);
}
