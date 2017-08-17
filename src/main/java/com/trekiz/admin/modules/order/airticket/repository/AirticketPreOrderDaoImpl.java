package com.trekiz.admin.modules.order.airticket.repository;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;

@Repository
public class AirticketPreOrderDaoImpl extends BaseDaoImpl<AirticketOrder>
		implements IAirticketPreOrderDao {
	
	 /** 
	 * 查询单办机票预占位且未付款的订单
	 */
	public List<Map<String,Object>> getOrderListByNoPay(int type,int occupyType,int rownum){
		 String sqlString="select o.* from airticket_order o where  del_flag =? and lockStatus!=?"
				 	+" and type=? and occupyType=? and place_holder_type=?"
				 	+" and not EXISTS (select 1 from orderpay pay where pay.orderId=o.id and pay.orderType=?)"
				 	+" order by create_date asc limit ?";
		 //新加了类型定义Map add by chy 2014-12-16 12:26:46
		 List<Map<String,Object>> list = findBySql(sqlString, Map.class, Context.DEL_FLAG_NORMAL,AirticketOrder.LOCKSTATUS_QX,type,occupyType,AirticketOrder.PLACEHOLDERTYPE_ZW,Context.ORDER_PAYSTATUS_YZW,rownum);
		 return list;
	 }
	
	/** 
	 * 修改订单状态
	 */
	public int updateOrder(Long id,String lockStatus){
		if(StringUtils.isEmpty(lockStatus)){
			return 1;
		}else{
			String sqlString="update airticket_order set lockStatus=? where id=?";
			return this.updateBySql(sqlString,lockStatus,id);
		}
	}
}
