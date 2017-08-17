/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockGroupRel;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipStockGroupRelInput;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipStockGroupRelQuery;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

public interface CruiseshipStockGroupRelService{
	
	public void save (CruiseshipStockGroupRel cruiseshipStockGroupRel);
	
	public void save (CruiseshipStockGroupRelInput cruiseshipStockGroupRelInput);
	
	public void update (CruiseshipStockGroupRel cruiseshipStockGroupRel);
	
	public CruiseshipStockGroupRel getById(java.lang.Integer value);
	
	public void removeById(java.lang.Integer value);
	
	public Page<CruiseshipStockGroupRel> find(Page<CruiseshipStockGroupRel> page, CruiseshipStockGroupRelQuery cruiseshipStockGroupRelQuery);
	
	public List<CruiseshipStockGroupRel> find( CruiseshipStockGroupRelQuery cruiseshipStockGroupRelQuery);
	
	public CruiseshipStockGroupRel getByUuid(String uuid);
	
	public void removeByUuid(String uuid);
	
	public boolean batchDelete(String[] uuids);
    /**
     * 223:tgy
     * 根据团期id查询表cruiseship_group_rel和activitygroup表,获得关联状态,cruiseship_stock_detail表的id,关联日期和操作人
     * @param agId
     * @return
     */
	public List<Map<String, Object>> getRelInfo(String agId);
 
}
