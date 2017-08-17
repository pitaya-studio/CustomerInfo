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

/**
 * 
 * @author zhenxing.yan
 *
 */
@Service("travelActivityUpdateTask")
@DependsOn("activityGroupService")
public class TravelActivityUpdateTask extends ScheduledTask{

	private static final Log logger = LogFactory.getLog(TravelActivityUpdateTask.class);
	
	@Autowired
	@Qualifier("activityGroupSyncService")
//	@Qualifier("activityGroupService")
	private IActivityGroupService activityGroupService;
	
	/**
	 * 对未过期的产品更新相应团期里的最早出团日期、
	 * 最低成人同行价和最低成人零售价 
	 * 创建人：Administrator   
	 * 创建时间：2014-3-7 下午2:18:41   
	 *
	 */
	@Override
	protected void task() {
		logger.info("exec task(execTravelActivityTask) in tenant["+FacesContext.getCurrentTenant()+"]");
		activityGroupService.execTravelActivityTask();
	}
}
