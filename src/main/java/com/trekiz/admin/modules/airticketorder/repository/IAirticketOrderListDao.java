package com.trekiz.admin.modules.airticketorder.repository;

import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.Page;

public interface IAirticketOrderListDao extends BaseDao<Map<String,Object>> {
	
	/**
	 * 根据查询条件查询机票订单列表
	 * @param request
	 * @param response
	 * @param whereSql
	 * @param condMap
	 * @return
	 */
	Page<Map<String,Object>> queryAirticketOrderListByCond(Page<Map<String,Object>> orderListPage, Map<String,String> condMap);
	
}
