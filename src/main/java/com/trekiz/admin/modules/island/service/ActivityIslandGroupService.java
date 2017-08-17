/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupAirline;
import com.trekiz.admin.modules.island.input.ActivityIslandGroupInput;
import com.trekiz.admin.modules.island.query.ActivityIslandGroupQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface ActivityIslandGroupService{
	
	public void save (ActivityIslandGroup activityIslandGroup);
	
	public void save (ActivityIslandGroupInput activityIslandGroupInput);
	
	public void update (ActivityIslandGroup activityIslandGroup);
	
	public ActivityIslandGroup getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<ActivityIslandGroup> find(Page<ActivityIslandGroup> page, ActivityIslandGroupQuery activityIslandGroupQuery);
	
	public List<ActivityIslandGroup> find( ActivityIslandGroupQuery activityIslandGroupQuery);
	
	public List<ActivityIslandGroup> getByActivityIslandUuid(String ActivityIslandUuid);
	
	public ActivityIslandGroup getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 根据海岛产品uuid获取所有团期信息
	*<p>Title: findGroupByActivityIslandUuid</p>
	* @return List<ActivityIslandGroup> 返回类型
	* @author majiancheng
	* @date 2015-5-27 下午6:03:17
	* @throws
	 */
	public List<ActivityIslandGroup> findGroupByActivityIslandUuid(String activityIslandUuid);
	
	/**
	 * 批量更新海岛游团期状态
	*<p>Title: updateGroupStatusByUuid</p>
	* @param uuidArray 海岛游团期uuid集合
	* @return int 返回类型
	* @author majiancheng
	* @date 2015-5-28 上午11:55:30
	* @throws
	 */
	public int batchUpdateStatusByGroupUuidArr(String[] uuidArray, String status);
	
	/**
	 * 根据团期航班表获取团期余位数
	*<p>Title: getRemNumberByGroupAirlineList</p>
	* @return int 返回类型
	* @author majiancheng
	* @date 2015-6-12 下午5:23:26
	* @throws
	 */
	public int getRemNumberByGroupAirlineList(List<ActivityIslandGroupAirline> groupAirlineList);
	
	/**
	 * 根据团期UUID获取团期余位数
	*<p>Title: getRemNumberByGroupAirlineList</p>
	* @return int 返回类型
	* @author hhx
	* @date 2015-6-12 下午5:23:26
	* @throws
	 */
	public int getRemNumberByGroupUuid(String groupUuid);
	
	/**
	 * 根据团期UUID查询产品部分信息，只适用于预报单、结算单的查询
	 * @param groupUUID         团期UUID
	 * @author shijun.liu
	 * @return
	 */
	public List<Map<String, Object>> getProductInfoForSettleForcast(String groupUUID);
	
	/**
	 * 海岛产品--预报单预计收款和结算单收款明细
	 * @param budgetType          成本类型，0表示预算成本，1表示结算成本
	 * @param groupUUID           团期ID
	 * @param orderType           订单类型
	 * @author shijun.liu
	 * @return
	 */
	public List<Map<String, Object>> getRefundInfoForcastAndSettle(Integer budgetType,String groupUUID,Integer orderType);
	/**
	 * 根据团号模糊查询上架状态的团号(转团专用)
	 * @author gao
	 * @param groupCode
	 * @return
	 */
	public List<ActivityIslandGroup> find(String groupCode);
}
