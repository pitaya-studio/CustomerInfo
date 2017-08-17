/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderPnrAirlinePrice;
import com.trekiz.admin.modules.mtourOrder.input.AirticketOrderPnrAirlinePriceInput;
import com.trekiz.admin.modules.mtourOrder.query.AirticketOrderPnrAirlinePriceQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface AirticketOrderPnrAirlinePriceService{
	
	public void save (AirticketOrderPnrAirlinePrice airticketOrderPnrAirlinePrice);
	
	public void save (AirticketOrderPnrAirlinePriceInput airticketOrderPnrAirlinePriceInput);
	
	public void update (AirticketOrderPnrAirlinePrice airticketOrderPnrAirlinePrice);
	
	public AirticketOrderPnrAirlinePrice getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<AirticketOrderPnrAirlinePrice> find(Page<AirticketOrderPnrAirlinePrice> page, AirticketOrderPnrAirlinePriceQuery airticketOrderPnrAirlinePriceQuery);
	
	public List<AirticketOrderPnrAirlinePrice> find( AirticketOrderPnrAirlinePriceQuery airticketOrderPnrAirlinePriceQuery);
	
	public AirticketOrderPnrAirlinePrice getByUuid(String uuid);
	
	public AirticketOrderPnrAirlinePrice getByCostRecordUuid(String costRecordUuid);
	
	public void removeByUuid(String uuid);
}
