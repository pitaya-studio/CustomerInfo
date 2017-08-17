package com.trekiz.admin.modules.activity.task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.quauq.multi.tenant.hibernate.FacesContext;
import com.quauq.multi.tenant.task.ScheduledTask;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;

@Service("travelActivityOfflineTask")
@DependsOn("activityGroupService")
public class TravelActivityOfflineTask extends ScheduledTask{
	private static final Log logger = LogFactory.getLog(TravelActivityOfflineTask.class);
	@Autowired
	@Qualifier("activityGroupSyncService")
//	@Qualifier("activityGroupService")
	private IActivityGroupService activityGroupService;
	
	/**
	 * 对过期的产品下架
	 * 创建人：Administrator   
	 * 创建时间：2014-3-7 下午3:00:46   
	 *
	 */
	@Override
	protected void task() {
		logger.info("exec task(execOffLineTask) in tenant["+FacesContext.getCurrentTenant()+"]");
		activityGroupService.execOffLineTask();
	}
}
