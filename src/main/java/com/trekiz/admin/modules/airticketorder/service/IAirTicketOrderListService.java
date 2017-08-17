package com.trekiz.admin.modules.airticketorder.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.order.entity.ActivityInfo;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;


/**
 * 机票订单专用
 * @author yakun.bai
 * @Date 2015-9-25
 */
public interface IAirTicketOrderListService {
    
	/**
	 * 根据查询条件查询机票订单列表
	 * @param request
	 * @param response
	 * @param userInfo
	 * @param paramsMap
	 * @param inMap
	 * @return
	 */
	Page<Map<Object, Object>> queryOrder(Page<Map<Object, Object>> page, Map<String,String> paramsMap, DepartmentCommon common);
	
	Page<Map<Object, Object>> findByActivityIds(Page<Map<Object, Object>> page, List<String> groupList, String orderSql);
	
	public List<ActivityInfo> getAirticketInfo(Long ticketGroupID);
}
