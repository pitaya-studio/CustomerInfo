/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.hotel.entity.ActivityHotelGroup;
import com.trekiz.admin.modules.hotel.input.ActivityHotelGroupInput;
import com.trekiz.admin.modules.hotel.query.ActivityHotelGroupQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface ActivityHotelGroupService{
	
	public void save (ActivityHotelGroup activityHotelGroup);
	
	public void save (ActivityHotelGroupInput activityHotelGroupInput);
	
	public void update (ActivityHotelGroup activityHotelGroup);
	
	public ActivityHotelGroup getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<ActivityHotelGroup> find(Page<ActivityHotelGroup> page, ActivityHotelGroupQuery activityHotelGroupQuery);
	
	public List<ActivityHotelGroup> find( ActivityHotelGroupQuery activityHotelGroupQuery);
	
	public ActivityHotelGroup getByUuid(String uuid);
	
	public void removeByUuid(String uuid);

	public List<ActivityHotelGroup> findGroupsByActivityHotelUuid(String uuid);
	
	/**
	 * 根据团期UUID查询产品部分信息，只适用于预报单、结算单的查询
	 * @param groupUUID         团期UUID
	 * @author shijun.liu
	 * @return
	 */
	public List<Map<String, Object>> getProductInfoForSettleForcast(String groupUUID);
	
	/**
	 * 酒店产品--预报单预计收款和结算单收款明细
	 * @param budgetType          成本类型，0表示预算成本，1表示结算成本
	 * @param groupUUID           团期ID
	 * @param orderType           订单类型
	 * @author shijun.liu
	 * @return
	 */
	public List<Map<String, Object>> getRefundInfoForcastAndSettle(Integer budgetType,String groupUUID,Integer orderType);

	/**
	 * 根据产品的uuid来跟新产品下团期的状态
	 * @param uuid
	 * @param status
	 * @return
	 */
	public int updateGroupStatusByActivityUuid(String uuid, String status);
	/**
	 * 根据uuid来更新团期的状态
	 * @param uuid
	 * @param status
	 * @return
	 */
	public int updateGroupStatusByUuid(String uuid, String status);
}
