package com.trekiz.admin.modules.order.task;


import org.springframework.beans.factory.annotation.Autowired;

import com.quauq.multi.tenant.task.ScheduledTask;
import com.trekiz.admin.modules.cost.service.IReceivePayService;
/**
 * 
* @ClassName: ReceivePayTask
* @Description: TODO(还款日期到期时，自动生成预警消息提示还款)
* @author yang.jiang
* @date 2016-3-30 11:53:06
*
 */
public class RefundDateTask extends ScheduledTask {
	
	@Autowired
	IReceivePayService ireceivePayServiceImpl;

	@Override
	protected void task() {
		// TODO Auto-generated method stub
		System.out.println("=====RefundDateTask begin!!!!!=====");
		ireceivePayServiceImpl.getRemaindListByRefundDate();
		System.out.println("=====RefundDateTask end!!!!!=====");
	}

}
