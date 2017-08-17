package com.trekiz.admin.modules.distribution.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.activity.repository.TravelActivityDao;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.distribution.service.DistributionService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
public class DistributionServiceImpl implements DistributionService {

	@Autowired
	private ActivityGroupDao activityGroupDao;
	@Autowired
	private TravelActivityDao travelActivityDao;
	
	@Override
	public Page<Map<String, Object>> findActivityGroupList(Page<Map<String, Object>> page, 
			TravelActivity travelActivity, Map<String, String> paramMap) {
		Long companyId = UserUtils.getUser().getCompany().getId(); // 批发商id
		StringBuffer sql = new StringBuffer();
//		String orderByprice = page.getOrderBy(); // 同行价和直客价orderBy
		// 团期列表SQL
		sql.append("    SELECT ");
		sql.append("		   g.id, ");
		sql.append("		   p.id as acitivityId, "); 			 // 产品Id
		sql.append("		   g.groupCode, "); 					 // 团号
		sql.append("		   p.acitivityName, ");					 // 产品名称
		sql.append("		   u.`name` as opName, ");				 // 计调人员名称
		sql.append("		   u.`mobile` as opMobile, ");			 // 计调人员手机号
		sql.append("		   d.label as fromAreaName, ");			 // 出发城市
		sql.append("		   g.groupOpenDate, ");					 // 出团日期
		sql.append("		   g.groupCloseDate, ");				 // 截团日期
		sql.append("		   g.settlementAdultPrice, ");			 // 同业价成人
		sql.append("		   g.settlementcChildPrice, ");		 	 // 同业价儿童
		sql.append("		   g.settlementSpecialPrice, ");		 // 同业价特殊人群
		sql.append("		   g.suggestAdultPrice, ");				 // 成人直客价
		sql.append("		   g.suggestChildPrice, ");		 		 // 儿童直客价
		sql.append("		   g.suggestSpecialPrice, ");		 	 // 特殊人群直客价
		sql.append("		   g.currency_type as currencyType, ");	 // 金额币种ID(按照表中出现的顺序，用逗号隔开)
		sql.append("		   g.planPosition, ");		 			 // 产品的预收人数
		sql.append("		   p.activity_kind AS activityKind, ");	 // 产品类型
		// 同行价转RMB
//		if (orderByprice.indexOf("settlementPriceRMB") != -1) {
//			sql.append("		   (g.settlementAdultPrice * c.currency_exchangerate) as settlementPriceRMB, ");
//		}
//		// 直客价转RMB
//		if (orderByprice.indexOf("suggestPriceRMB") != -1) {
//			sql.append("		   (g.suggestAdultPrice * c.currency_exchangerate) as suggestPriceRMB, ");
//		}
		sql.append("		   g.freePosition ");		 			 // 产品的剩余位置
		sql.append("	  FROM activitygroup g ");				     // 产品团期表		 
		sql.append(" LEFT JOIN sys_user u ");						 // 用户表
		sql.append("	    ON u.id = g.createBy ");
		sql.append(" LEFT JOIN travelactivity p ");					 // 产品基本信息表
		sql.append("	    ON g.srcActivityId = p.id ");	
		sql.append("	   AND p.delFlag = 0 ");
		sql.append(" LEFT JOIN sys_dict d ");						 // 字典表
		sql.append("	    ON d.`value` = p.fromArea ");
		sql.append("       AND d.type = 'from_area' ");
		sql.append(" LEFT JOIN sys_office so ");
		sql.append(" 		ON so.id = p.proCompany");
		// 如果需要按价格排序需要连接币种表
//		if (orderByprice.indexOf("settlementPriceRMB") != -1 || orderByprice.indexOf("suggestPriceRMB") != -1) {
//			sql.append(" LEFT JOIN currency c ");
//			sql.append("		ON SUBSTR(g.currency_type, 1, LOCATE(',', g.currency_type) -1) = c.currency_id ");
//		}
		sql.append("	 WHERE g.suggestAdultPrice > 0 ");			 // 直客价
		sql.append("	   AND g.is_t1 = 1 ");						 // T1已上架团期
		sql.append("	   AND g.delFlag = 0 ");
		sql.append("	   AND p.activityStatus = 2 ");				 // 产品已上架状态
		sql.append("	   AND p.proCompany = " + companyId);		 // 批发商id
		sql.append(" 	   AND so.shelfRightsStatus = 0 ");			 // 批发商是否启用T1上架功能
		sql.append(" 	   AND g.groupOpenDate >= date_format(NOW(), '%y-%m-%d 00:00:00') ");// 出团日期大于当前时间 
		
		// 查询条件
		// 产品名称或团号
		String wholeSalerKey = paramMap.get("wholeSalerKey");
		if (StringUtils.isNotBlank(wholeSalerKey)) {
			sql.append(" AND (p.acitivityName LIKE '%" + wholeSalerKey.trim() + "%' OR g.groupCode LIKE '%" + wholeSalerKey.trim() + "%') ");
		}
		
		// 出团日期开始/结束
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date groupOpenDate = travelActivity.getGroupOpenDate();
		if (groupOpenDate != null) {
			sql.append(" AND g.groupOpenDate >= '" + sdf.format(groupOpenDate) + " 00:00:00' ");
		}
		Date groupCloseDate = travelActivity.getGroupCloseDate();
		if (groupCloseDate != null) {
			sql.append(" AND g.groupOpenDate <= '" + sdf.format(groupCloseDate) + " 23:59:59' ");
		}
		
		// 计调
		String createName = paramMap.get("createName");
		if (StringUtils.isNotBlank(createName)) {
			sql.append(" AND g.createBy in (SELECT u.id FROM sys_user u WHERE u.delFlag = 0 AND u.`name` = '" + createName + "') ");
		}
		
		// 同行价开始/结束
		String settlementAdultPriceStart = paramMap.get("settlementAdultPriceStart");
		if (StringUtils.isNotBlank(settlementAdultPriceStart)) {
			sql.append(" AND g.settlementAdultPrice >= " + settlementAdultPriceStart);
		}
		String settlementAdultPriceEnd = paramMap.get("settlementAdultPriceEnd");
		if (StringUtils.isNotBlank(settlementAdultPriceEnd)) {
			sql.append(" AND g.settlementAdultPrice <= " + settlementAdultPriceEnd);
		}
		
		// 币种处理
		String currencyId = "-1"; // 币种ID
		if (StringUtils.isNotBlank(travelActivity.getCurrencyType())) {
			currencyId = travelActivity.getCurrencyType();
		}
		if (StringUtils.isNotBlank(settlementAdultPriceStart) || StringUtils.isNotBlank(settlementAdultPriceEnd)) {
			sql.append(" AND SUBSTR(g.currency_type, 1, LOCATE(',', g.currency_type) -1) = " + currencyId); // 返回子串在全字符串中的位置
		}
		
		// 处理共同参数
		handleParams(sql, paramMap, travelActivity);
		return activityGroupDao.findBySql(page, sql.toString(), Map.class);
	}

	
	@Override
	public Page<TravelActivity> findTravelActivityList(Page<TravelActivity> page, 
			TravelActivity travelActivity, Map<String, String> paramMap) {

		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT p.* ")
		   .append("FROM travelactivity p LEFT JOIN sys_office so ON so.id = p.proCompany ")
		   .append("WHERE p.delFlag = 0 ")
		   .append(" AND p.activity_kind = 2 ") // 散拼产品
		   .append(" AND p.activityStatus = 2 ") // 产品已上架
		   .append(" AND so.shelfRightsStatus = 0 ") // 批发商具有产品上架权限
		   .append(" AND p.proCompany = ").append(companyId) // 批发商id
		   .append(" AND p.id IN ( SELECT DISTINCT g.srcActivityId ")
		   .append(" FROM activitygroup g ")
		   .append(" LEFT JOIN travelactivity p ON g.srcActivityId = p.id ")
		   .append(" WHERE p.delFlag = 0 ")
		   .append(" AND g.is_t1 = 1 ") // T1已上架团期
		   .append(" AND g.delFlag = 0 ")
		   .append(" AND g.suggestAdultPrice > 0 ") // 存在成人直客价
		   .append(" AND g.groupOpenDate >= date_format(NOW(), '%y-%m-%d 00:00:00') ") // 排除已过期团期
		   .append(" AND p.proCompany = " + companyId + ") ");
		
		// 产品名称或团号
		String wholeSalerKey = paramMap.get("wholeSalerKey");
		if (StringUtils.isNotBlank(wholeSalerKey)) {
			sql.append(" AND (p.acitivityName LIKE '%" + wholeSalerKey.trim() + "%' ")
			   .append(" OR p.id IN (SELECT DISTINCT g.srcActivityId ")
			   .append(" FROM activitygroup g LEFT JOIN travelactivity t ON g.srcActivityId = t.id ")
			   .append(" WHERE t.delFlag = 0 AND g.is_t1 = 1 AND g.delFlag = 0 AND g.suggestAdultPrice > 0 ")
			   .append(" AND g.groupOpenDate >= date_format(NOW(), '%y-%m-%d 00:00:00') ")
			   .append(" AND t.proCompany = ").append(companyId)
			   .append(" AND g.groupCode LIKE '%" + wholeSalerKey.trim() + "%')) ");
		}
		// 计调
		String createName = paramMap.get("createName");
		if (StringUtils.isNotBlank(createName)) {
			sql.append(" AND p.createBy in (SELECT u.id ")
			   .append(" FROM sys_user u ")
			   .append(" WHERE u.delFlag = 0 ")
			   .append(" AND u.`name` = '" + createName + "') ");
		}
		// 出团日期开始/结束
		Date groupOpenDate = travelActivity.getGroupOpenDate();
		Date groupCloseDate = travelActivity.getGroupCloseDate();
		// 同行价开始/结束
		String settlementAdultPriceStart = paramMap.get("settlementAdultPriceStart");
		String settlementAdultPriceEnd = paramMap.get("settlementAdultPriceEnd");
		// 币种
		String currencyType = paramMap.get("currencyType"); 
		if (groupOpenDate != null || groupCloseDate != null 
				|| StringUtils.isNotBlank(settlementAdultPriceStart) 
				|| StringUtils.isNotBlank(settlementAdultPriceEnd)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sql.append(" AND p.id IN (SELECT DISTINCT g.srcActivityId ")
			   .append(" FROM activitygroup g LEFT JOIN travelactivity t ON g.srcActivityId = t.id ")
			   .append(" WHERE t.delFlag = 0 AND g.is_t1 = 1 AND g.delFlag = 0 AND g.suggestAdultPrice > 0 ")
			   .append(" AND g.groupOpenDate >= date_format(NOW(), '%y-%m-%d 00:00:00') ")
			   .append(" AND t.proCompany = ").append(companyId);
			if (groupOpenDate != null) { // 出团日期开始
				sql.append(" AND g.groupOpenDate >= '" + sdf.format(groupOpenDate) + " 00:00:00' ");
			}
			if (groupCloseDate != null) { // 出团日期结束
				sql.append(" AND g.groupOpenDate <= '" + sdf.format(groupCloseDate) + " 23:59:59' ");
			}
			
			if (StringUtils.isNotBlank(settlementAdultPriceStart) || StringUtils.isNotBlank(settlementAdultPriceEnd)) {
				sql.append(" AND SUBSTR(g.currency_type, 1, LOCATE(',', g.currency_type) -1) = " + currencyType);
				if (StringUtils.isNotBlank(settlementAdultPriceStart)) { // 同行价格开始
					sql.append(" AND g.settlementAdultPrice >= " + settlementAdultPriceStart);
				}
				if (StringUtils.isNotBlank(settlementAdultPriceEnd)) { // 同行价格结束
					sql.append(" AND g.settlementAdultPrice <= " + settlementAdultPriceEnd);
				}
			}
			sql.append(" ) ");
		}
		
		// 处理共同参数
		handleParams(sql, paramMap, travelActivity);
		page = travelActivityDao.findBySql(page, sql.toString(), TravelActivity.class);
		// 处理产品列表--展开团期下的团期列表
		for(TravelActivity activity : page.getList()) {
			List<ActivityGroup> activityGroupList = new ArrayList<>();
			List<ActivityGroup> activityGroupListTemp = activityGroupDao.findWeixinGroupByActivityId(activity.getId().intValue());
			for (ActivityGroup group : activityGroupListTemp) {
				boolean flag = true;
				// 团号搜索
				if (StringUtils.isNotBlank(wholeSalerKey)){
					if (!activity.getAcitivityName().toLowerCase().contains(wholeSalerKey.toLowerCase()) 
							&& !group.getGroupCode().toLowerCase().contains(wholeSalerKey.toLowerCase())) {
						flag = false;
					}
				}
				
				// 出团日期筛选
				if (groupOpenDate != null && groupOpenDate.compareTo(group.getGroupOpenDate()) > 0) {
					flag = false;
				}
				if (groupCloseDate != null && groupCloseDate.compareTo(group.getGroupOpenDate()) < 0) {
					flag = false;
				}
				
				// 同行价筛选
				if (StringUtils.isNotBlank(settlementAdultPriceStart) || StringUtils.isNotBlank(settlementAdultPriceEnd)) {
					String currencyId = group.getCurrencyType().split(",")[0];
					if (StringUtils.isNotBlank(currencyType) && !currencyType.equals(currencyId)) {// 币种
						flag = false;
					} else {// 金额
						if (StringUtils.isNotBlank(settlementAdultPriceStart) 
								&& group.getSettlementAdultPrice().compareTo(new BigDecimal(settlementAdultPriceStart)) < 0) {
							flag = false;
						}
						if (StringUtils.isNotBlank(settlementAdultPriceEnd) 
								&& group.getSettlementAdultPrice().compareTo(new BigDecimal(settlementAdultPriceEnd)) > 0) {
							flag = false;
						}
					}
				}
				
				if (flag) {
					activityGroupList.add(group);
				}
			}
			activity.setActivityGroupList(activityGroupList);
			Set<ActivityGroup> activityGroups = new HashSet<>(activityGroupList);
			activity.setActivityGroups(activityGroups);
		}
		return page;
	}
		
	/**
	 * 处理共同参数<br>
	 * 出发地、目的地、领队、航空公司、行程天数
	 * */
	private void handleParams(StringBuffer sql, Map<String, String> paramMap, TravelActivity travelActivity) {
		// 出发地
		Integer fromArea = travelActivity.getFromArea();
		if (fromArea != null) {
			sql.append(" AND p.fromArea = " + fromArea + " ");
		}
		// 目的地
		String targetAreaIds = travelActivity.getTargetAreaIds();
		if (StringUtils.isNotBlank(targetAreaIds)) {
			sql.append(" AND p.id IN(SELECT srcActivityId FROM activitytargetarea WHERE targetAreaId IN(" + targetAreaIds + "))");
		}
		// 领队
		String groupLead = travelActivity.getGroupLead();
		if (StringUtils.isNotBlank(groupLead)) {
			sql.append(" AND p.group_lead = '" + groupLead + "' ");
		}
		// 航空公司
		ActivityAirTicket activityAirTicket = travelActivity.getActivityAirTicket();
		if (activityAirTicket != null) {
			String airlines = activityAirTicket.getAirlines();
			if (StringUtils.isNotBlank(airlines)) {
				sql.append(" AND p.airticket_id IN (select airticketId from activity_flight_info where airlines like '%"+airlines+"%')");
			}
		}
		// 行程天数
		Integer activityDuration = travelActivity.getActivityDuration();
		if (activityDuration != null) {
			sql.append(" AND p.activityDuration = " + activityDuration);
		}
//		// 产品类型
//		Integer activityTypeId = travelActivity.getActivityTypeId();
//		if (activityTypeId != null) {
//			sql.append(" AND p.activityTypeId = " + activityTypeId);
//		}
//		// 产品系列
//		Integer activityLevelId = travelActivity.getActivityLevelId();
//		if (activityLevelId != null) {
//			sql.append(" AND p.activityLevelId = " + activityLevelId);
//		}
//		// 旅游类型
//		Integer travelTypeId = travelActivity.getTravelTypeId();
//		if (travelTypeId != null) {
//			sql.append(" AND p.travelTypeId = " + travelTypeId);
//		}
	}


	@Override
	public List<Map<String, Object>> getDistributionActivityGroupList(String groupIds) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT g.id AS id,p.id AS acitivityId,p.activity_kind AS activityKind ")
		   .append("FROM activitygroup g LEFT JOIN travelactivity p ON g.srcActivityId = p.id ")
		   .append("WHERE p.delFlag = 0 AND g.delFlag = 0 AND p.proCompany = ").append(companyId)
		   .append(" AND g.id IN(" + groupIds + ")");
		return activityGroupDao.findBySql(sql.toString(), Map.class);
	}
}