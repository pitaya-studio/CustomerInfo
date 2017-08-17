package com.trekiz.admin.modules.order.airticket.repository;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;

public interface IAirticketPreOrderDao extends BaseDao<AirticketOrder>{
	
	/**
	 * 查询未付款的占位订单
	 * @param type
	 * @param occupyType
	 * @return
	 */
	public List<Map<String,Object>> getOrderListByNoPay(int type,int occupyType,int rownum);
	
	/**
	 * 修改订单状态
	 * @param id
	 * @param payStatus
	 * @param lockStatus
	 * @return
	 */
	public int updateOrder(Long id,String lockStatus);
}
