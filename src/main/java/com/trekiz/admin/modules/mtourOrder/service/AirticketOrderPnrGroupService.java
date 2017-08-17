/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderPnrGroup;
import com.trekiz.admin.modules.mtourOrder.input.AirticketOrderPnrGroupInput;
import com.trekiz.admin.modules.mtourOrder.query.AirticketOrderPnrGroupQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface AirticketOrderPnrGroupService{
	
	public void save (AirticketOrderPnrGroup airticketOrderPnrGroup);
	
	public void save (AirticketOrderPnrGroupInput airticketOrderPnrGroupInput);
	
	public void update (AirticketOrderPnrGroup airticketOrderPnrGroup);
	
	public AirticketOrderPnrGroup getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<AirticketOrderPnrGroup> find(Page<AirticketOrderPnrGroup> page, AirticketOrderPnrGroupQuery airticketOrderPnrGroupQuery);
	
	public List<AirticketOrderPnrGroup> find( AirticketOrderPnrGroupQuery airticketOrderPnrGroupQuery);
	
	public AirticketOrderPnrGroup getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 根据订单id返回pnr组
	 * @param orderId
	 * @return
	 */
	public List<AirticketOrderPnrGroup> getListByOrderId(String orderId);
}
