package com.trekiz.admin.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.trekiz.admin.agentToOffice.T2.utils.JudgeStringType;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.activity.entity.TargetArea;
import com.trekiz.admin.modules.activity.repository.ActivityGroupViewDao;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.repository.AreaDao;

/**
 * @author Administrator 服务加载类 缓存国家信息、省、城市、区域信息
 */
@Service
public class AreaUtil extends BaseService {

	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
	private static ActivityGroupViewDao activityGroupViewDao = SpringContextHolder.getBean(ActivityGroupViewDao.class);

	/**
	 * findAreaNameById 根据id查询到达城市名称 add by chy 2014年11月10日10:43:23
	 * 
	 * @param args
	 */
	public static String findAreaNameById(Long areaId) {
		Area area = null;
		if(areaId != null){
			area = areaDao.findOne(areaId);
		}
		if(area == null) {
			area = new Area();
		}
		String name = area.getName();
		if(StringUtil.isNotBlank(name)){
			return name;
		}else{
			return "";
		}
	}
	
	public static String findAreaNameById(String areaId) {
		Area area = null;
		if(StringUtil.isNotBlank(areaId)){
			if(JudgeStringType.isPlusMinusInteger(areaId)){
				area = areaDao.findOne(new Long(areaId));
			}else{
				return areaId;
			}
		}
		if(area == null) {
			area = new Area();
		}
		String name = area.getName();
		if(StringUtil.isNotBlank(name)){
			return name;
		}else{
			return "";
		}
	}
	
	 public  static List<TargetArea> findTargetAreaById(Long id){
		 return activityGroupViewDao.findTargetAreaById(id);
	 }

	 /**
	  * 获取散拼产品目的地sql：sql优化，替换请小心 
	  * @author yakun.bai
	  * @Date 2016-9-12
	  */
	 public static StringBuffer getLooseActivityTargetAreaSql() {
		 StringBuffer sb = new StringBuffer("");
		
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 String curDate = sdf.format(new Date());
		 
		 sb.append("SELECT t1.srcActivityId AS travelactivity_id, GROUP_CONCAT(t1.targetAreaId) AS sys_area_id, GROUP_CONCAT(sa.parentIds) parentIds ")
		 		.append("FROM activitytargetarea t1, sys_area sa, travelactivity t, activitygroup a ")
		 		.append("WHERE t1.targetAreaId = sa.id AND a.srcActivityId = t.id AND t.id = t1.srcActivityId AND ")
		 		.append("t.activity_kind = '2' AND a.groupOpenDate >= '" + curDate + "' GROUP BY a.srcActivityId");
		 return sb;
	 }
}
