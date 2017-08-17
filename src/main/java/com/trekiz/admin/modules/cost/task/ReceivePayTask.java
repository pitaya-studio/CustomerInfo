package com.trekiz.admin.modules.cost.task;


import org.springframework.beans.factory.annotation.Autowired;

import com.quauq.multi.tenant.task.ScheduledTask;
import com.trekiz.admin.modules.cost.service.IReceivePayService;
/**
 * 
* @ClassName: ReceivePayTask
* @Description: TODO(应收账款到期时，自动生成预警消息提示应收账款到期)
* @author kai.xiao
* @date 2015年11月17日 下午2:07:06
*
 */
public class ReceivePayTask extends ScheduledTask {
	
	@Autowired 
	IReceivePayService receivePayService;

	@Override
	protected void task() {
		// TODO Auto-generated method stub
		System.out.println("=====task begin!!!!!=====");
		receivePayService.getPayListByPayDate();
		System.out.println("=====task end!!!!!=====");
	}

}
