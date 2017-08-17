package com.trekiz.admin.modules.activity.service;

import java.util.List;

import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.ActivityGroupCompare;

public interface IActivityGroupCompareService {

	/**
	 * 保存单个 产品团期对比信息
	 * 
	 * @author zhangyp
	 * @param activityGroupCompare：产品团期对比实体
	 * @throws Exception 
	 */
	public void saveActivityGroupCompare(ActivityGroupCompare activityGroupCompare) throws Exception;

	/**
	 * 删除单个 产品团期对比信息
	 * 
	 * @author zhangyp
	 * @param activityGroupCompare：产品团期对比实体
	 * @throws Exception 
	 */
	public void delActivityGroupCompare(Long operatorId, Long activityGroupId) throws Exception;

	
	/**
	 * 将一个产品下可以加入对比的团期全部加入产品团期对比信息表
	 * 
	 * @author zhangyp
	 * @param activityGroupCompares：所有要加入的 产品团期对比实体
	 */
	public void saveAllActivityGroupCompares(List<ActivityGroupCompare> activityGroupCompares);

	/**
	 * 将一个产品下可以加入对比的团期全部从产品团期对比信息表删除
	 * 
	 * @author zhangyp
	 * @param activityGroupCompares：所有要加入的 产品团期对比实体
	 */
	public void delAllActivityGroupCompares(List<ActivityGroupCompare> activityGroupCompares);

	
	/**
	 * 将当前操作员下所有产品团期对比信息删除
	 * 
	 * @param operatorId 操作员id
	 * @throws Exception
	 */
	public void clearAllActivityGroupCompares(Long operatorId) throws Exception;
	
	/**
	 * 通过 operatorId 和 activityGroupId 查询对应的 ActivityGroupCompare并返回
	 * @param operatorId
	 * @param activityGroupId
	 * @return
	 */
	public ActivityGroupCompare findByProperties(Long operatorId, Long activityGroupId);

	/**
	 * 返回要下载的产品团期对比列表
	 * @param groupCodes
	 * @return
	 */
	public List<Object[]> getActivityGroupCompareList(String[] groupCodes);

	
	/**
	 * @Description 根据用户id查询对比团期
	 * @author yakun.bai
	 * @Date 2015-10-23
	 */
	public List<ActivityGroup> getCompareGroup(Long userId);
}
