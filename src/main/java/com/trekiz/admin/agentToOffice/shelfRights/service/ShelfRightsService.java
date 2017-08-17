package com.trekiz.admin.agentToOffice.shelfRights.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.sys.entity.Office;

public interface ShelfRightsService {
	/**
	 * 分页查询
	 * @param page
	 * @param office
	 * @return
	 */
	public Page<Map<String,Object>> find(Page<Map<String,Object>> page,Office office,String type,String big);
	
	/**
	 * 查询此批发商下所有已上架并且团期大于当前日期的 总团期数
	 * @param whoSalerId
	 * @return
	 */
	public Integer getProductCountByWholeSalerId(Integer whoSalerId);
	
	/**
	 * 查询T1平台上架产品
	 * @param whosalerId
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public Integer getT1ProductCountByActivityId(Integer activityId);
	
	/**
	 * 查询所有散拼的产品
	 * @param whosalerId
	 * @param priceStrategy
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public List<TravelActivity> getAllTravelActivities(Long whosalerId);
	/**
	 * 获得所有的批发商
	 * @return
	 */
	public List<Map<String,Object>> getAllOffice();
	
	/**
	 * 根据批发商id修改上架权限状态
	 * @param companyId
	 * @author chao.zhang@quauq.com
	 */
	public void updatShelRightsStatusByCompanyId(Long companyId,Integer status);
	
	public Integer getBaoMing(Integer companyId);
	
	/**
	 * 查询该产品下T1订单总数
	 * @param productId
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public Integer getOrderCount(Integer productId);
	
	/**
	 * 查询quauq渠道预定人数
	 * @param companyId
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	public Integer getQuauqPersonCount(Integer companyId);
	
	public Integer getCompanyCount(Office office);
}
