/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderPnrAirline;
import com.trekiz.admin.modules.mtourOrder.input.AirticketOrderPnrAirlineInput;
import com.trekiz.admin.modules.mtourOrder.query.AirticketOrderPnrAirlineQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface AirticketOrderPnrAirlineService{
	
	public void save (AirticketOrderPnrAirline airticketOrderPnrAirline);
	
	public void save (AirticketOrderPnrAirlineInput airticketOrderPnrAirlineInput);
	
	public void update (AirticketOrderPnrAirline airticketOrderPnrAirline);
	
	public AirticketOrderPnrAirline getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<AirticketOrderPnrAirline> find(Page<AirticketOrderPnrAirline> page, AirticketOrderPnrAirlineQuery airticketOrderPnrAirlineQuery);
	
	public List<AirticketOrderPnrAirline> find( AirticketOrderPnrAirlineQuery airticketOrderPnrAirlineQuery);
	
	public AirticketOrderPnrAirline getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
}
