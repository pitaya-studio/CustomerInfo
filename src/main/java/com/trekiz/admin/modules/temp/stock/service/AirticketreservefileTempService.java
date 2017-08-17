/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.temp.stock.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.temp.stock.entity.AirticketreservefileTemp;
import com.trekiz.admin.modules.temp.stock.input.AirticketreservefileTempInput;
import com.trekiz.admin.modules.temp.stock.query.AirticketreservefileTempQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface AirticketreservefileTempService{
	
	public void save (AirticketreservefileTemp airticketreservefileTemp);
	
	public void save (AirticketreservefileTempInput airticketreservefileTempInput);
	
	public void update (AirticketreservefileTemp airticketreservefileTemp);
	
	public AirticketreservefileTemp getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<AirticketreservefileTemp> find(Page<AirticketreservefileTemp> page, AirticketreservefileTempQuery airticketreservefileTempQuery);
	
	public List<AirticketreservefileTemp> find( AirticketreservefileTempQuery airticketreservefileTempQuery);
	
	public AirticketreservefileTemp getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public boolean batchDelete(String[] uuids);
}
