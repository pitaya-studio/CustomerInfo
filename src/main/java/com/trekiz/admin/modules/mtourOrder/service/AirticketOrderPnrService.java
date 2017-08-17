/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderPnr;
import com.trekiz.admin.modules.mtourOrder.input.AirticketOrderPnrInput;
import com.trekiz.admin.modules.mtourOrder.query.AirticketOrderPnrQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface AirticketOrderPnrService{
	
	public void save (AirticketOrderPnr airticketOrderPnr);
	
	public void save (AirticketOrderPnrInput airticketOrderPnrInput);
	
	public void update (AirticketOrderPnr airticketOrderPnr);
	
	public AirticketOrderPnr getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<AirticketOrderPnr> find(Page<AirticketOrderPnr> page, AirticketOrderPnrQuery airticketOrderPnrQuery);
	
	public List<AirticketOrderPnr> find( AirticketOrderPnrQuery airticketOrderPnrQuery);
	
	public AirticketOrderPnr getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public List<Map<String,Object>> queryAirticketOrderPNCByOrderUuid(Integer orderId);
	
	/**
	 * 通过PNR组的uuid得到该组下的pnr集合
	 * @param pnrGroupUuid
	 */
	public List<AirticketOrderPnr> findByPNRGroupUuid(String pnrGroupUuid);
	
	/**
	 * 根据订单Id查询pnr信息
	 * @param id
	 * @return
	 */
	public List<AirticketOrderPnr> findByOrderId(Long id);
}
