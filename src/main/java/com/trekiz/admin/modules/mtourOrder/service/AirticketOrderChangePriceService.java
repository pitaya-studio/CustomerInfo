/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.mtourOrder.entity.AirticketOrderChangePrice;
import com.trekiz.admin.modules.mtourOrder.input.AirticketOrderChangePriceInput;
import com.trekiz.admin.modules.mtourOrder.query.AirticketOrderChangePriceQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface AirticketOrderChangePriceService{
	
	public void save (AirticketOrderChangePrice airticketOrderChangePrice);
	
	public void save (AirticketOrderChangePriceInput airticketOrderChangePriceInput);
	
	public void update (AirticketOrderChangePrice airticketOrderChangePrice);
	
	public AirticketOrderChangePrice getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<AirticketOrderChangePrice> find(Page<AirticketOrderChangePrice> page, AirticketOrderChangePriceQuery airticketOrderChangePriceQuery);
	
	public List<AirticketOrderChangePrice> find( AirticketOrderChangePriceQuery airticketOrderChangePriceQuery);
	
	public AirticketOrderChangePrice getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	/**
	 * 删除操作
	 * @param uuid
	 */
	public void deleteByOrderId(Long id);
}
