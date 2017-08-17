package com.trekiz.admin.modules.order.task;

import org.springframework.beans.factory.annotation.Autowired;

import com.quauq.multi.tenant.hibernate.FacesContext;
import com.quauq.multi.tenant.task.ScheduledTask;
import com.trekiz.admin.modules.order.airticket.service.AirticketPreOrderService;
import com.trekiz.admin.modules.order.service.OrderCommonService;

/**
 * 
 * @description 取消订单：超过保留天数则取消此订单
 * 
 * @author baiyakun
 * 
 * @create_time 2014-7-7
 */
public class OrdertTask extends ScheduledTask {

	@Autowired
	private OrderCommonService orderService;

	@Autowired
	private AirticketPreOrderService airticketPreOrderService;

	/**
	 * 功能: 如果订单已经过的天数》保留天数 那么这个订单取消 不算订单日
	 * 
	 * @author xuziqian
	 * @DateTime 2014-2-18 下午5:04:58
	 */
	public void cancelOrderWithRemainDays() {

		orderService.cancelOrderWithRemainDays();
		airticketPreOrderService.scheduledAirticketOrderService();

	}

	/**
	 * 功能: 如果订单已经过的天数》保留天数 那么这个订单取消 不算订单日
	 * 
	 * @author xuziqian
	 * @DateTime 2014-2-18 下午5:04:58
	 */
	@Override
	protected void task() {
		System.out.println("==========执行定时任务 OrdertTask[" + FacesContext.getCurrentTenant() + "]==========");
		orderService.cancelOrderWithRemainDays();
		airticketPreOrderService.scheduledAirticketOrderService();
	}

}
