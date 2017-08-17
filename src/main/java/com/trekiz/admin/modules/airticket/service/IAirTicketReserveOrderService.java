package com.trekiz.admin.modules.airticket.service;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.entity.AirTicketReserveOrder;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;


public interface IAirTicketReserveOrderService{
	
	
       Page<AirTicketReserveOrder> findAirTicketReserveOrder(Page<AirTicketReserveOrder> page,ActivityAirTicket airTicket,
   			String departureCity, String arrivedCity,String agentId, String minprice,
   			String maxprice, String airType, String startTime,
   			String endTime, String paymentType,String orderby, DepartmentCommon common);
}
