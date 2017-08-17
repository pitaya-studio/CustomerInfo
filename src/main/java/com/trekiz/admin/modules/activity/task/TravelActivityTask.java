package com.trekiz.admin.modules.activity.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.trekiz.admin.modules.activity.service.IActivityGroupService;

@Service
@DependsOn("activityGroupService")
public class TravelActivityTask {

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
//	@Scheduled(cron="0 0 0 * * ?")
	public void execTravelActivityTask(){
		
		activityGroupService.execTravelActivityTask();
	}
	
	/**
	 * 对过期的产品下架
	 * 创建人：Administrator   
	 * 创建时间：2014-3-7 下午3:00:46   
	 *
	 */
//	@Scheduled(cron="0 0 0 * * ?")
	public void execOffLineTask(){
		
		activityGroupService.execOffLineTask();
	}
}
