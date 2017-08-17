package com.trekiz.admin.modules.airticketorder.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.quauq.multi.tenant.hibernate.FacesContext;
import com.quauq.multi.tenant.task.ScheduledTask;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;

public class OrderStatus extends ScheduledTask{

	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private IAirTicketOrderService airTicketOrderService;

	public void changeOrderStatus() {
		airTicketOrderService.changeOrderStatus();
	}

	@Override
	protected void task() {
		System.out.println("==========执行定时任务 OrderStatus["+FacesContext.getCurrentTenant()+"]==========");
		airTicketOrderService.changeOrderStatus();
	}

}
