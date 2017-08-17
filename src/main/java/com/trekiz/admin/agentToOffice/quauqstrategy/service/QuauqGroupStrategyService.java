package com.trekiz.admin.agentToOffice.quauqstrategy.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.trekiz.admin.agentToOffice.T2.entity.Rate;
import com.trekiz.admin.agentToOffice.quauqstrategy.entity.QuauqGroupStrategy;
import com.trekiz.admin.agentToOffice.quauqstrategy.entity.QueryQuauqStrategy;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.activity.entity.TravelActivity;

public interface QuauqGroupStrategyService {
	/**
	 * 设置策略列表的客户查询(未设置)
	 * @return
	 */
	public Page<Map<String,Object>> getAllQuauqAgentStrate(Page<Map<String,Object>> page,QueryQuauqStrategy quauqStrategy);
	
	/**
	 * 设置策略列表的客户查询(已设置)
	 * @param page
	 * @param request
	 * @return
	 */
	public Page<Map<String,Object>> getQuauqAgentStrate(Page<Map<String,Object>> page,QueryQuauqStrategy quauqStrategy);
	
	/**
	 * 保存和批量保存费率
	 * @param array
	 * @author chao.zhang@quauq.com
	 * @time 2016/08/10
	 */
	public void batchSaveStrategy(JSONArray array);
	
	/**
	 * 设置费率页面查看
	 * @param request
	 * @return
	 * @author chao.zhang@quauq.com
	 * @time 2016/08/10
	 */
	public List<Map<String,Object>> getChildrenAgentList(QueryQuauqStrategy quauqStrategy);
	
	/**
	 * 查询相应团期设置的汇率
	 * @param companyUuid	批发商UUID
	 * @param activityId	团期或者产品ID
	 * @param productType	产品类型
	 * @param agentId		渠道ID
     * @return
	 * @author	shijun.liu
	 * @date	2016.08.11
     */
	public Rate getGroupRate(String companyUuid, Long activityId, Integer productType, Long agentId);
	
	/**
	 * 通过groupId团期ID获得产品
	 * @param groupId
	 * @return
	 * @author chao.zhang
	 */
	public TravelActivity getTravelActivityName(String groupId);
	
	public Page<Map<String,Object>> getAllQuauqAgent(Page<Map<String,Object>> page, QueryQuauqStrategy quauqStrategy);

	public void batchSaveGroupRate(String groupIds, JSONArray array, String productType,String companyUuid);

	/**
	 * 获取所有的T1渠道商  不分页
	 * @return
	 */
	public List<Map<Object, Object>> getAllT1Agent();
	
	public QuauqGroupStrategy queryAgentByOfficeUUIdAndActivityId(String offficeUUID,Long activityId);

	public Rate getMaxRate(Long groupId, Integer productType,
			BigDecimal quauqAdultPrice); 
		  
}
