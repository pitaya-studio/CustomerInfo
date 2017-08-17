package com.trekiz.admin.modules.distribution.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.activity.entity.TravelActivity;

public interface DistributionService {

	/**
	 * 待分销产品--团期列表
	 * @param page
	 * @param travelActivity
	 * @param paramMap 筛选条件Map
	 * @param activityKind 产品类型
	 * @author yang.wang
	 * */
	public Page<Map<String, Object>> findActivityGroupList(Page<Map<String, Object>> page, TravelActivity travelActivity, Map<String, String> paramMap);
	
	
	/**
	 * 待分销产品--产品列表
	 * @param page
	 * @param travelActivity
	 * @param paramMap 筛选条件Map
	 * @param activityKind 产品类型
	 * @author yang.wang
	 * */
	public Page<TravelActivity> findTravelActivityList(Page<TravelActivity> page, TravelActivity travelActivity, Map<String, String> paramMap);
	
	
	/**
	 * 获取前端已勾选的团期数据
	 * @param groupIds 团期ids 如 256,985,654
	 * @author yang.wang
	 * @date 2017.1.11
	 * */
	public List<Map<String, Object>> getDistributionActivityGroupList(String groupIds);
}
