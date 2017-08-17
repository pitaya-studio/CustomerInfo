/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.service;

import java.util.List;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockLog;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipStockLogInput;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipStockLogQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface CruiseshipStockLogService{
	
	public void save (CruiseshipStockLog cruiseshipStockLog);
	
	public void save (CruiseshipStockLogInput cruiseshipStockLogInput);
	
	public void update (CruiseshipStockLog cruiseshipStockLog);
	
	public CruiseshipStockLog getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<CruiseshipStockLog> find(Page<CruiseshipStockLog> page, CruiseshipStockLogQuery cruiseshipStockLogQuery);
	
	public List<CruiseshipStockLog> find( CruiseshipStockLogQuery cruiseshipStockLogQuery);
	
	public CruiseshipStockLog getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public boolean batchDelete(String[] uuids);
}
