package com.trekiz.admin.modules.order.task;

import org.springframework.beans.factory.annotation.Autowired;

import com.quauq.multi.tenant.hibernate.FacesContext;
import com.quauq.multi.tenant.task.ScheduledTask;
import com.trekiz.admin.modules.sys.service.SystemService;

/**
 * 
 * @description 修改用户登录状态
 * 
 * @author baiyakun
 * 
 * @create_time 2016-11-21
 */
public class ChangeUserStatus extends ScheduledTask {

	@Autowired
	private SystemService systemService;

	@Override
	protected void task() {
		System.out.println("==========执行定时任务 修改用户状态[" + FacesContext.getCurrentTenant() + "]==========");
		systemService.changeUserLoginStatus();
	}

}
