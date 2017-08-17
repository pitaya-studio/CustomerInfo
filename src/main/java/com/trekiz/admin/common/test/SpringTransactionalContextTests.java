package com.trekiz.admin.common.test;

import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.trekiz.admin.modules.activity.entity.ActivityFile;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.IActivityFileService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.service.SystemService;

/**
 * Spring 单元测试基类
 * @author zj
 * @version 2013-11-19
 */
@ActiveProfiles("production")
@ContextConfiguration(locations = {"/applicationContext.xml", "/applicationContext-shiro.xml"})
public class SpringTransactionalContextTests extends AbstractJUnit4SpringContextTests {

	protected DataSource dataSource;
	
	protected IActivityFileService activityFileService;
	
	protected DocInfoService docInfoService;
	
	protected ITravelActivityService travelActivityService;
	
	protected SystemService systemService;

	@Autowired
	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}

	@Autowired
	@Qualifier("travelActivitySyncService")
	public void setTravelActivityService(ITravelActivityService travelActivityService) {
		this.travelActivityService = travelActivityService;
	}

	@Autowired
	public void setActivityFileService(IActivityFileService activityFileService) {
		this.activityFileService = activityFileService;
	}

	@Test
	public void test(){
		com.trekiz.admin.modules.activity.entity.TravelActivity travelActivity = travelActivityService.findById(407L);
		travelActivity.setAcitivityName("产品同步test-zzy-mod1");
		try {
			travelActivityService.save(travelActivity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	@Test
	public void deleteTest(){
		ActivityFile f= activityFileService.findById(270l);
		TravelActivity activity = travelActivityService.findById(147L);
		Set<ActivityFile> activityFiles1 = new HashSet<ActivityFile>();
		f = new ActivityFile();
		f.setTravelActivity(new TravelActivity(147l));
		f.setFileName("abc");
		activityFiles1.add(f);
		f = new ActivityFile();
		f.setTravelActivity(new TravelActivity(147l));
		f.setFileName("def");
		activityFiles1.add(f);
		activity.setActivityFiles(activityFiles1);
		try {
			travelActivityService.save(activity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("==============================2:"+activity.getActivityFiles().size());

	}
}
