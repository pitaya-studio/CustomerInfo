package com.trekiz.admin.modules.t1.service.impl;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.t1.repository.IT1Dao;
import com.trekiz.admin.modules.t1.service.IT1Service;
/**
 * Created by zzk on 2016/10/9.
 */
@Service
@Transactional(readOnly = true)
public class T1ServiceImpl implements IT1Service {
    @Autowired
    private IT1Dao t1Dao;
    @Autowired
    private AgentinfoService agentinfoService;

    /**
     * 供应商logo读取
     * @param tourOutIn
     * @param tourDistrictId
     * @return
     */
    @Override
    public List<Map<String, Object>> getT1LogoList(String tourOutIn, String tourDistrictId, String flag) {
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT sd.id tourDistrictId, sd.name tourDistrictName FROM sys_district sd WHERE sd.tourInOut = ");
        // 判断出境游/国内游
        if (StringUtils.isNotBlank(tourOutIn) && Context.FREE_TRAVEL_FOREIGN.equals(tourOutIn)) {
            sb.append(Context.FREE_TRAVEL_FOREIGN);
        } else if (StringUtils.isNotBlank(tourOutIn) && Context.FREE_TRAVEL_INLAND.equals(tourOutIn)) {
            sb.append(Context.FREE_TRAVEL_INLAND);
        }

        // 查询某区域
        if(StringUtils.isNotBlank(tourDistrictId) && "true".equals(flag)) {
            sb.append(" AND sd.id = " + tourDistrictId + " ");
        }

        DateTime dt = new DateTime();
        sb.append(" AND EXISTS ( ")
                .append("       SELECT ")
                .append("districtIds ")
                .append("        FROM ")
                .append("( ")
                .append("        SELECT ata.districtIds FROM	activitygroup a ")
                .append("LEFT JOIN travelactivity t ON a.srcActivityId = t.id ")
                .append("LEFT JOIN ( ")
                .append("        SELECT ")
                .append("su.id AS 'createBy', ")
                .append("        so.id AS 'sys_office_id', ")
                .append("        so. NAME AS 'supplierName', ")
                .append("        so.shelfRightsStatus AS 'shelfRightsStatus', ")
                .append("        su.quauqBookOrderPermission AS 'quauqBookOrderPermission', ")
                .append("        so.charge_rate ")
                .append("FROM ")
                .append("sys_user su ")
                .append("LEFT JOIN sys_office so ON su.companyId = so.id ")
                .append("WHERE ")
                .append("so.delFlag = 0 ")
                .append("	) sup ON sup.createBy = a.createBy ")
                .append("LEFT JOIN ( ")
                .append("        SELECT ")
                .append("a.srcActivityId AS 'travelactivity_id', ")
                .append("       GROUP_CONCAT(t1.targetAreaId) AS 'sys_area_id', ")
                .append("        GROUP_CONCAT(s1. NAME) NAME, ")
                .append("        GROUP_CONCAT(s1.`sys_district_id`) districtIds, ")
                .append("        GROUP_CONCAT(s1.parentId) parentId, ")
                .append("       GROUP_CONCAT(sa.`name`) parentName, ")
                .append("       GROUP_CONCAT(s1.parentIds) parentIds ")
                .append("       FROM ")
                .append("activitytargetarea t1 ")
                .append("LEFT JOIN sys_area s1 ON t1.targetAreaId = s1.id ")
                .append("LEFT JOIN travelactivity t ON t1.srcActivityId = t.id ")
                .append("LEFT JOIN activitygroup a ON a.srcActivityId = t.id ")
                .append("LEFT JOIN sys_area sa ON sa.id = s1.parentId ")
                .append("WHERE ")
                .append("t.activity_kind = '2' ")
                .append("AND a.groupOpenDate >= '"+dt.toString("yyyy-MM-dd")+"' ")
                .append("GROUP BY ")
                .append("a.srcActivityId ")
                .append("	) ata ON t.id = ata.travelactivity_id ")
                .append("WHERE ")
                .append("t.activity_kind = '2' ")
                .append("AND t.delFlag = 0 ")
                .append("AND t.activityStatus = 2 ")
                .append("AND a.delFlag = 0 ");
//        if (StringUtils.isNotBlank(tourOutIn) && Context.FREE_TRAVEL_FOREIGN.equals(tourOutIn)) {
//            sb.append("AND FIND_IN_SET(100000, ata.parentIds) ");
//        } else if (StringUtils.isNotBlank(tourOutIn) && Context.FREE_TRAVEL_INLAND.equals(tourOutIn)) {
//            sb.append("AND FIND_IN_SET(200000, ata.parentIds) ");
//        }
        sb.append("AND sup.shelfRightsStatus = 0 ")
                .append("AND a.is_t1 = 1 ")
                .append("AND a.groupOpenDate >= '"+dt.toString("yyyy-MM-dd")+"' ")
                .append(") t ")
                .append("        WHERE ")
                .append("FIND_IN_SET(sd.id, t.districtIds) ")
                .append(") ");
        return t1Dao.findBySql(sb.toString(), Map.class);
    }

    /**
     * 获取供应商信息
     * @param tourDistrictId 区域id
     */
    @Override
    public List<Map<String, Object>> getSuppliers(String tourDistrictId, String tourOutIn) {
        StringBuffer sb = new StringBuffer();
        DateTime dt = new DateTime();
        sb.append("select so.id, so.name title, IFNULL(so.logo,'') logoUrl, ")
                .append(" IFNULL((select docPath from docinfo where id = so.logo),'') path,")
                .append(" so.name text from sys_office so, ")
                .append("( ")
                .append("        SELECT ")
                .append("t.id, ")
                .append("        t.proCompany, ")
                .append("        t.proCompanyName ")
                .append("FROM ")
                .append("activitygroup a ")
                .append("LEFT JOIN travelactivity t ON a.srcActivityId = t.id ")
                .append("LEFT JOIN ( ")
                .append("        SELECT ")
                .append("su.id AS 'createBy', ")
                .append("        so.id AS 'sys_office_id', ")
                .append("        so. NAME AS 'supplierName', ")
                .append("        so.shelfRightsStatus AS 'shelfRightsStatus', ")
                .append("        su.quauqBookOrderPermission AS 'quauqBookOrderPermission', ")
                .append("        so.charge_rate ")
                .append("FROM ")
                .append("sys_user su ")
                .append("LEFT JOIN sys_office so ON su.companyId = so.id ")
                .append("WHERE ")
                .append("so.delFlag = 0 ")
                .append(") sup ON sup.createBy = a.createBy ")
                .append("LEFT JOIN ( ")
                .append("        SELECT ")
                .append("t1.srcActivityId AS travelactivity_id, ")
                .append("        GROUP_CONCAT(t1.targetAreaId) AS sys_area_id, ")
                .append("GROUP_CONCAT(sa.parentIds) parentIds, ")
                .append(" GROUP_CONCAT(sa.sys_district_id) districtIds ")
                .append("        FROM ")
                .append("activitytargetarea t1, ")
                .append("sys_area sa, ")
                .append("travelactivity t, ")
                .append("activitygroup a ")
                .append("WHERE ")
                .append("t1.targetAreaId = sa.id ")
                .append("AND a.srcActivityId = t.id ")
                .append("AND t.id = t1.srcActivityId ")
                .append("AND t.activity_kind = '2' ")
                .append("AND a.groupOpenDate >= '"+dt.toString("yyyy-MM-dd")+"' ")
                .append("GROUP BY ")
                .append("a.srcActivityId ")
                .append(") ata ON t.id = ata.travelactivity_id ")
                .append("WHERE ")
                .append("t.activity_kind = '2' ")
                .append("AND t.delFlag = 0 ")
                .append("AND t.activityStatus = 2 ")
                .append("AND a.delFlag = 0 ")
                .append("AND FIND_IN_SET(?, ata.districtIds) ")
//                .append("AND FIND_IN_SET(?, ata.parentIds) ")
                .append("AND sup.shelfRightsStatus = 0 ")
                .append("AND a.is_t1 = 1 ")
                .append("AND a.groupOpenDate >= '"+dt.toString("yyyy-MM-dd")+"' ")
                .append(") t ")
                .append("where t.proCompany = so.id ")
                .append("group by so.id ")
                .append("ORDER BY count(so.id) DESC ");

        return t1Dao.findBySql(sb.toString(), Map.class, tourDistrictId);
    }

    @Override
    public Page<Map<String, Object>> queryList(HttpServletRequest request, Page<Map<String, Object>> page, Map<String, Object> paramsMap) {
 
        StringBuffer sql=new StringBuffer();
        sql.append("  SELECT  ")
                .append("  activity.id AS 'travelactivity_id',") //产品主键id
                .append("  agp.id AS 'activitygroup_id', ")//团期主键id
                .append("  agp.groupCode, ")//团号
                .append("  agp.groupOpenDate AS 'groupOpenDate', ")//团期的出团日期(一个产品可以有多个团期)
                .append(" activity.activityDuration AS 'activityDuration', ") //行程天数
                .append(" activity.acitivityName AS 'activityName', ") //产品名称
                .append(" agp.freePosition AS 'freePosition', ")  //余位
                .append(" agp.currency_type currencyIdStr, ") // 币种字符串
                .append(" agp.settlementAdultPrice settlementPrice, ") // 同行价不带币种 0522需求
                .append(" (SELECT currency_mark from currency where currency_id = SUBSTRING_INDEX(agp.currency_type,',',1)) AS 'currencyids', ") //包含表中所有币种的id,逗号分隔
                .append(" agp.quauqAdultPrice AS 'quauqAdultPrice', ")   //quauq价(仅仅是数额)
                .append(" getSettlePrice (IFNULL(groupRate.quauq_rate_type,officeRate.quauq_rate_type),IFNULL(groupRate.quauq_rate,officeRate.quauq_rate),IFNULL(groupRate.agent_rate_type,officeRate.agent_rate_type),IFNULL(groupRate.agent_rate,officeRate.agent_rate),agp.quauqAdultPrice,agp.settlementAdultPrice) AS 'quauqPrice', ")
//                .append(" IFNULL(a.suggestAdultPrice,'') AS 'suggestAdultPrice', ") //建议直客价
//                .append(" IFNULL(a.settlementAdultPrice,'') AS settlementAdultPrice,")//同行价
                .append(" CASE WHEN agp.suggestAdultPrice IS NULL THEN '' ELSE CONCAT((SELECT currency_mark FROM currency " +
                        "WHERE currency_id = reverse(substring_index(reverse(substring_index(agp.currency_type,',',4)),',',1))),agp.suggestAdultPrice) END suggestAdultPrice,")//建议直客价
                .append(" CASE WHEN agp.settlementAdultPrice IS NULL THEN '' ELSE CONCAT((SELECT currency_mark FROM currency " +
                        "WHERE currency_id = SUBSTRING_INDEX(agp.currency_type,',',1)),agp.settlementAdultPrice) END settlementAdultPrice,")//同行价
                .append(" (SELECT label from sys_dict where type = 'from_area' and `value` = activity.fromArea) AS 'fromArea', ")   //出发城市,从发布来看,出发城市只有一个
                .append(" ata.sys_area_id AS 'targetArea', ")//到达城市ids,逗号分隔
                .append(" ata.districtIds, ")//区域id
                // .append(" a.createBy   AS  'sys_user_id', ")  //供应商对应的主键id
                .append(" office.id AS 'sys_office_id', ")//供应商对应的主键id
                .append(" office.name AS 'supplierName', ") //供应商名称
                .append(" office.t1_freePosion_status AS t1FreePosionStatus, ") //T1平台余位状态
                .append("agp.settlementAdultPrice/getSettlePrice (IFNULL(groupRate.quauq_rate_type,officeRate.quauq_rate_type),IFNULL(groupRate.quauq_rate,officeRate.quauq_rate),IFNULL(groupRate.agent_rate_type,officeRate.agent_rate_type),IFNULL(groupRate.agent_rate,officeRate.agent_rate),agp.quauqAdultPrice,agp.settlementAdultPrice) as defaultSort, ")
                .append("activity.touristLineId lineId, ")
                .append("(SELECT line_name from tourist_line tl where tl.id = activity.touristLineId) lineName ");
        
        String groupSql = getListSql(request, page, paramsMap, "group");
        sql.append(groupSql);
        
        Page<Map<String, Object>> returnPage = t1Dao.findBySql(page, sql.toString(), Map.class);
        return returnPage;
    }

    @Override
	public Page<Map<String,Object>> queryActivityList(HttpServletRequest request, Page<Map<String,Object>> page, Map<String, Object> paramsMap) {
    	
        StringBuffer sql=new StringBuffer();
        sql.append(" select activity.id activityId, ")  //产品主键id
        	.append(" activity.acitivityName, ") 		//产品名称
        	.append(" (SELECT label FROM sys_dict WHERE type = 'from_area' AND value = activity.fromArea) fromArea, ") //出发城市
        	.append(" activity.activityDuration, ") // 行程天数
        	.append(" office.id officeId, ") //批发商id
        	.append(" office.name officeName, ")//批发商名称
        	.append(" GROUP_CONCAT(CONCAT(MONTH (agp.groupOpenDate),'月',DAYOFMONTH(agp.groupOpenDate),'日') ORDER BY agp.groupOpenDate SEPARATOR ' , ' ) groupOpenDate, ")//出团日期
        	.append(" GROUP_CONCAT(agp.id ORDER BY agp.groupOpenDate) nearestGroupIds, ")//最近出团日期,还得处理
        	.append(" MIN(agp.groupOpenDate) minGroupOpenDate, ")
        	.append(" MAX(agp.groupOpenDate) maxGroupOpenDate, ")
        	
        	.append(" MIN( ")
        	.append(" getSettlePrice ( ")
        	.append(" IFNULL( groupRate.quauq_rate_type,officeRate.quauq_rate_type), ")
        	.append(" IFNULL( groupRate.quauq_rate,officeRate.quauq_rate), ")
        	.append(" IFNULL( groupRate.agent_rate_type,officeRate.agent_rate_type), ")
        	.append(" IFNULL( groupRate.agent_rate,officeRate.agent_rate), ")
        	.append(" agp.quauqAdultPrice, ")
        	.append(" agp.settlementAdultPrice) ")
        	.append(" ) quauqPrice, ")// 最低供应价
        		
//        	.append(" GROUP_CONCAT(CONCAT(agp.quauqAdultPrice,':',agp.settlementAdultPrice) ORDER BY agp.quauqAdultPrice,agp.groupOpenDate SEPARATOR ',' ) quauqAndSettleAdultPrice, ")
        	.append(" SUBSTRING_INDEX( ")
        	.append(" GROUP_CONCAT( agp.settlementAdultPrice ORDER BY ")
        	.append(" isnull(getSettlePrice ( IFNULL( groupRate.quauq_rate_type, officeRate.quauq_rate_type ), IFNULL( groupRate.quauq_rate, officeRate.quauq_rate ), IFNULL( groupRate.agent_rate_type, officeRate.agent_rate_type ), IFNULL( groupRate.agent_rate, officeRate.agent_rate ), agp.quauqAdultPrice, agp.settlementAdultPrice )), ")
        	.append(" getSettlePrice ( IFNULL( groupRate.quauq_rate_type, officeRate.quauq_rate_type ), IFNULL( groupRate.quauq_rate, officeRate.quauq_rate ), IFNULL( groupRate.agent_rate_type, officeRate.agent_rate_type ), IFNULL( groupRate.agent_rate, officeRate.agent_rate ), agp.quauqAdultPrice, agp.settlementAdultPrice ), ") // 去除空值影响
        	.append(" agp.groupOpenDate ), ")
        	.append(" ',', 1 ) settleAdultPrice, ")// 同行价
        	 
//        	.append(" GROUP_CONCAT( ")
//        	.append(" CONCAT( ")
//        	.append(" getSettlePrice ( ")
//        	.append(" IFNULL( groupRate.quauq_rate_type,officeRate.quauq_rate_type), ")
//        	.append(" IFNULL( groupRate.quauq_rate,officeRate.quauq_rate), ")
//        	.append(" IFNULL( groupRate.agent_rate_type,officeRate.agent_rate_type), ")
//        	.append(" IFNULL( groupRate.agent_rate,officeRate.agent_rate), ")
//        	.append(" agp.quauqAdultPrice, ")
//        	.append(" agp.settlementAdultPrice ")
//        	.append(" ), ")
//        	.append(" ':', ")
//        	.append(" agp.settlementAdultPrice ")
//        	.append(" ) ")
//        	.append(" ORDER BY ")
//        	.append(" getSettlePrice ( ")
//        	.append(" IFNULL( groupRate.quauq_rate_type,officeRate.quauq_rate_type), ")
//        	.append(" IFNULL( groupRate.quauq_rate,officeRate.quauq_rate), ")
//        	.append(" IFNULL( groupRate.agent_rate_type,officeRate.agent_rate_type), ")
//        	.append(" IFNULL( groupRate.agent_rate,officeRate.agent_rate), ")
//        	.append(" agp.quauqAdultPrice, ")
//        	.append(" agp.settlementAdultPrice ")
//        	.append(" ), ")
//        	.append(" agp.groupOpenDate SEPARATOR ',' ")
//        	.append(" ) quauqAndSettleAdultPrice, ")//供应价和成人同行价
//        	.append(" MIN(agp.settlementAdultPrice) minSettlementAdultPrice, ") // 产品中团期的最低成人同行价,如果产品下所有团期都不存在成人的QUAUQ供应价,展示的成人同行价就取这个值
        	.append(" agp.currency_type currencyIds, ")//包含表中所有币种的id,逗号分隔
        	.append(" (SELECT currency_mark from currency where currency_id = SUBSTRING_INDEX(agp.currency_type, ',', 1)) AS 'quauqCurrencyMark', ") //成人同行价币种
        	.append(" SUBSTRING_INDEX( GROUP_CONCAT( agp.settlementAdultPrice ORDER BY isnull(getSettlePrice ( IFNULL( groupRate.quauq_rate_type, officeRate.quauq_rate_type ), IFNULL( groupRate.quauq_rate, officeRate.quauq_rate ), IFNULL( groupRate.agent_rate_type, officeRate.agent_rate_type ), IFNULL( groupRate.agent_rate, officeRate.agent_rate ), agp.quauqAdultPrice, agp.settlementAdultPrice ))," +
        			"getSettlePrice ( IFNULL( groupRate.quauq_rate_type, officeRate.quauq_rate_type ), IFNULL( groupRate.quauq_rate, officeRate.quauq_rate ), IFNULL( groupRate.agent_rate_type, officeRate.agent_rate_type ), IFNULL( groupRate.agent_rate, officeRate.agent_rate ), agp.quauqAdultPrice, agp.settlementAdultPrice )), ',', 1 )  ")// 去除空值影响
            .append(" / MIN( getSettlePrice ( IFNULL( groupRate.quauq_rate_type, officeRate.quauq_rate_type ), IFNULL( groupRate.quauq_rate, officeRate.quauq_rate ), IFNULL( groupRate.agent_rate_type, officeRate.agent_rate_type ), IFNULL( groupRate.agent_rate, officeRate.agent_rate ), agp.quauqAdultPrice, agp.settlementAdultPrice )) AS defaultSort "); // 成人同行价(最低成人供应价所对应的)除以最低成人供应价
        
        String activitySql = getListSql(request, page, paramsMap, "activity");
        sql.append(activitySql);
        
        Page<Map<String, Object>> returnPage = t1Dao.findBySql(page, sql.toString(), Map.class);
        return returnPage;
	}
    
    /**
     * 抽取团期列表和产品列表公共的sql
     * @return
     */
    private String getListSql(HttpServletRequest request, Page<Map<String,Object>> page, Map<String, Object> paramsMap, String groupOrActivity){
    	// 查询条件
        String keyword = paramsMap.get("keyword").toString();
        String tourOutIn = paramsMap.get("tourOutIn").toString();							// 出境游
        String travelAreaId = paramsMap.get("travelAreaId").toString();                     // 日本  
        String startCityParas = paramsMap.get("startCityParas").toString();					// 出发城市  fromarea
        String countryParas = paramsMap.get("endCityParas").toString(); 					// 目的地      FIND_IN_SET("+c+",ata.parentId) OR FIND_IN_SET("+c+", ata.sys_area_id)
        String targetCitys = paramsMap.get("targetCitys").toString(); 						// 抵达城市  a.targetAreaId IN ("+targetCitys+")
        String linePlays = paramsMap.get("linePlays").toString();							// 线路玩法  t.touristLineId
        String supplierParas = paramsMap.get("supplierParas").toString();					// 批发商id
        String groupDateParas = paramsMap.get("groupDateParas").toString();
        String dayParas = paramsMap.get("dayParas").toString();								// 行程天数
        String priceParas = paramsMap.get("priceParas").toString();							// 价格区间
        String freeParas = paramsMap.get("freeParas").toString();							// 余位
        String pageNo = paramsMap.get("pageNo").toString();
        String pageSize = paramsMap.get("pageSize").toString();
        String orderBy = paramsMap.get("orderBy").toString();
        
        Long agentId = -1L;
        String agentType = "-1";
        Agentinfo agent = agentinfoService.findOne(UserUtils.getUser().getAgentId());
		if (agent != null) {
			agentId = agent.getId();
			agentType = agent.getAgentType();
		}

        //当前日期
        DateTime dt = new DateTime();
        StringBuffer sql=new StringBuffer();
    	
	    sql.append(" FROM travelactivity activity ")
	    	.append(" LEFT JOIN activitygroup agp ON agp.srcActivityId = activity.id ")
	    	.append(" LEFT JOIN sys_office office ON activity.proCompany = office.id ")
	        .append(" LEFT JOIN ")//将产品和目的表进行组装
	        .append(" (SELECT ")
	        .append(" a.srcActivityId AS 'travelactivity_id', ")
	        .append(" GROUP_CONCAT(a.targetAreaId) AS 'sys_area_id', ")
	        .append(" GROUP_CONCAT(IFNULL(a.districtId,'')) districtIds, ")
	        .append(" GROUP_CONCAT(a.name) name, ")
	        .append(" GROUP_CONCAT(sa.parentId) parentId, ")
	        .append(" GROUP_CONCAT(a.parentName) parentName, ")
	        .append(" GROUP_CONCAT(sa.parentIds) parentIds ")
	        .append(" FROM (").append(getLooseActivityTargetAreaSql(dt.toString("yyyy-MM-dd"))).append(") a ")
	        .append(" LEFT JOIN sys_area sa on a.targetAreaId = sa.id ");
	    
	
	    sql.append(" where 1=1 ");
	    if (StringUtils.isNotBlank(travelAreaId)) {
	        sql.append(" and sa.sys_district_id = " + travelAreaId + " ");
	    }
	
	    startCityParas = startCityParas.replace("[", "").replace("]", "").replace("\"", "");
	    targetCitys = targetCitys.replace("[", "").replace("]", "").replace("\"", "");
	    countryParas = countryParas.replace("[", "").replace("]", "").replace("\"", "");
	    supplierParas = supplierParas.replace("[", "").replace("]", "").replace("\"", "");
	    groupDateParas = groupDateParas.replace("[", "").replace("]", "").replace("\"", "");
	    linePlays = linePlays.replace("[", "").replace("]", "").replace("\"", "");
	    dayParas = dayParas.replace("[", "").replace("]", "").replace("\"", "");
	    priceParas = priceParas.replace("[", "").replace("]", "").replace("\"", "");
	    freeParas = freeParas.replace("[", "").replace("]", "").replace("\"", "");
	    orderBy = orderBy.replace("[", "").replace("]", "").replace("\"", "");
//		if ("activity".equals(groupOrActivity)) { // 针对产品的出团日期排序
//	    	if("groupOpenDate  DESC".equals(orderBy)){
//	    		orderBy = "maxGroupOpenDate  DESC";
//	    	}else if("groupOpenDate  ASC".equals(orderBy)){
//	    		orderBy = "minGroupOpenDate  ASC";
//	    	}
//	    }
	
	    if(StringUtils.isNotBlank(targetCitys) || StringUtils.isNotBlank(keyword) || StringUtils.isNotBlank(countryParas)) {
	        if(StringUtils.isNotBlank(targetCitys)){
	            sql.append("  and a.targetAreaId IN ("+targetCitys+") ");
	        }
	        if ("产品名称 / 供应商 / 团号 / 目的地".equals(keyword)) {
	            keyword = "";
	        }
	
	        if(StringUtils.isNotBlank(countryParas)) {
	            sql.append("  and (");
	            String[] countryArr = countryParas.split(",");
	            for (int i = 0; i < countryArr.length; i++) {
	                String country = countryArr[i];
	                if (i != countryArr.length - 1) {
	                    sql.append(" FIND_IN_SET('"+country+"',parentId) OR FIND_IN_SET('"+country+"', targetAreaId) or ");
	                } else {
	                    sql.append(" FIND_IN_SET('"+country+"',parentId) OR FIND_IN_SET('"+country+"', targetAreaId) ");
	                }
	            }
	            sql.append(" ) ");
	        }
	    }
	    sql.append(" GROUP BY a.srcActivityId) ata ")
	            .append(" ON activity.id = ata.travelactivity_id ")
	            // 费率表
	            .append(" LEFT JOIN quauq_group_strategy groupRate ON groupRate.activity_id = agp.id ")
	            .append(" AND groupRate.product_type = 2  AND groupRate.agent_id = " + agentId)
	            .append(" LEFT JOIN sys_office_rate officeRate ON activity.proCompany = officeRate.companyId ")
	            .append(" AND officeRate.agent_type = " + agentType);
	    
	    sql.append(" WHERE activity.activity_kind='2' AND activity.delFlag = 0 AND activity.activityStatus = 2 AND agp.delFlag = 0 ")
	    	.append(" AND agp.quauqAdultPrice < 100000000 ");//过滤手动造的金额大的数据
	    
	    if (StringUtils.isNotBlank(travelAreaId)) {
	        sql.append(" AND FIND_IN_SET(" + travelAreaId + ", ata.districtIds) ");
	    } else {
	        if (Context.FREE_TRAVEL_FOREIGN.equals(tourOutIn)) {
	            sql.append(" and FIND_IN_SET(100000, ata.parentIds) ");
	        } else if (Context.FREE_TRAVEL_INLAND.equals(tourOutIn)){
	            sql.append(" and FIND_IN_SET(200000, ata.parentIds) ");
	        }
	    }
	
	    //1.批发商上架权限(0:上架;1:下架);2.用户具有报名权限(0:无报名;1:有报名);3.批发商在价格策略中,且该策略的启用状态为启用(1:启用;2:禁用).
	    sql.append(" AND office.shelfRightsStatus=0 ")//AND sup.quauqBookOrderPermission=1
	    		.append(" AND office.delFlag = 0 ")
	            .append(" AND agp.is_t1 = 1");
	    //如果出发城市不为空的话或者不为默认的全选时,则拼接上出发城市的条件
	    if(StringUtils.isNotBlank(startCityParas)){
	        sql.append(" AND activity.fromArea in (" + startCityParas + ")");
	    }
	    //当出发地不为空的时候,除了拼接上出发地的id,还需要去除出发地为空值的记录
	    if(StringUtils.isNotBlank(targetCitys)){
	        sql.append(" AND ata.sys_area_id in (" + targetCitys + ")");
	    }
	    //当供应商搜索条件不为空的时候,拼接上供应商的id
	    if(StringUtils.isNotBlank(supplierParas)){
	        sql.append(" AND office.id in (" + supplierParas + ") ");
	    }
	    // 线路玩法搜索条件
	    if (StringUtils.isNotBlank(linePlays)) {
	        sql.append(" and activity.touristLineId in (" + linePlays + ") ");
	    }
	    
	    // 暂时策略，待删除
	//    Integer lingxianwangshuai = UserUtils.getUser().getLingxianwangshuai();
	//    if (lingxianwangshuai == 1) {
	//    	sql.append(" and office.id != 426 ");
	//    } else {
	//    	sql.append(" and office.id != 451 ");
	//    }
	    
	    // 辉腾国际临时策略
		String url = request.getRequestURL().toString();
		if (url.contains("huitengguoji.com")) {
			sql.append(" and office.id = 441 ");
		}
		//金陵国旅travel.jsjbt.com
		if (url.contains("travel.jsjbt.com")) {
		//if (url.contains("localhost:8080")) {
			sql.append(" and office.id = 454 ");
		}
		//出团日期
	    if (StringUtils.isNotBlank(groupDateParas)) {
	        sql.append(" AND ");
	        String groupDateStr = "";
	        if (groupDateParas.charAt(groupDateParas.length() - 1) == '-') {
	            groupDateStr = " agp.groupOpenDate >= '" + groupDateParas.substring(0, groupDateParas.length()-1) + "'";
	        } else if (groupDateParas.charAt(0) == '-') {
	            groupDateStr = " agp.groupOpenDate <= '" + groupDateParas.substring(1) + "'";
	        } else {
	            groupDateStr = " (agp.groupOpenDate >= '" + groupDateParas.substring(0, 10)
	                    + "' and agp.groupOpenDate <= '" + groupDateParas.substring(11) + "')";
	        }
	        sql.append(groupDateStr);
	    }
	    if (StringUtils.isNotBlank(countryParas)) {
	        sql.append(" and (");
	        String[] cs = countryParas.split(",");
	        for (int i = 0; i < cs.length; i++) {
	            String c = cs[i];
	            if (i != cs.length - 1) {
	                sql.append(" FIND_IN_SET("+c+",ata.parentId) OR FIND_IN_SET("+c+", ata.sys_area_id) or ");
	            } else {
	                sql.append(" FIND_IN_SET("+c+",ata.parentId) OR FIND_IN_SET("+c+", ata.sys_area_id) ");
	            }
	        }
	        sql.append(" ) ");
	    }
	
	    //行程天数
	    if (StringUtils.isNotBlank(dayParas)) {
	        sql.append(" AND ");
	        String[] days = dayParas.split(",");
	        for (int i = 0; i < days.length; i++) {
	            String day = days[i];
	            switch (day) {
	                case "d1":
	                    days[i] = " activityDuration = 1 ";
	                    break;
	                case "d2":
	                    days[i] = " activityDuration = 2 ";
	                    break;
	                case "d3":
	                    days[i] = " activityDuration = 3 ";
	                    break;
	                case "d4":
	                    days[i] = " activityDuration = 4 ";
	                    break;
	                case "d5":
	                    days[i] = " activityDuration = 5 ";
	                    break;
	                case "d6":
	                    days[i] = " activityDuration = 6 ";
	                    break;
	                case "d7":
	                    days[i] = " activityDuration = 7 ";
	                    break;
	                case "d8":
	                    days[i] = " activityDuration = 8 ";
	                    break;
	                case "d9":
	                    days[i] = " activityDuration = 9 ";
	                    break;
	                case "d0":
	                    days[i] = " activityDuration >= 10 ";
	                    break;
	                default:
	                    days[i] = days[i].replaceAll("天", "");
	                    int pos = returnPos(days[i]);
	                    String[] dayInterval = days[i].split("-");
	                    if (pos == 1) {
	                        days[i] = " (activityDuration >= " + dayInterval[0] + ") ";
	                    } else if(pos == -1) {
	                        days[i] = " (activityDuration <= " + dayInterval[1] + ") ";
	                    } else {
	                        days[i] = " (activityDuration >= " + dayInterval[0] + " AND activityDuration <= " + dayInterval[1] + ") ";
	                    }
	                    break;
	            }
	        }
	        StringBuffer daysPara = new StringBuffer("");
	        for (int i = 0; i < days.length; i++) {
	            if(i != days.length -1) {
	                daysPara.append(days[i] + " OR ");
	            } else {
	                daysPara.append(days[i]);
	            }
	        }
	        sql.append(daysPara.insert(0, "(").append(")"));
	    }
	
	    //价格区间
	    if (StringUtils.isNotBlank(priceParas)) {
	        sql.append(" AND ");
	        String[] prices = priceParas.split(",");
	        for (int i = 0; i < prices.length; i++) {
	            String price = prices[i];
	            switch (price) {
	                case "p0":
	                    prices[i] = " (getSettlePrice(IFNULL(groupRate.quauq_rate_type,officeRate.quauq_rate_type),IFNULL(groupRate.quauq_rate,officeRate.quauq_rate),IFNULL(groupRate.agent_rate_type,officeRate.agent_rate_type),IFNULL(groupRate.agent_rate,officeRate.agent_rate),agp.quauqAdultPrice,agp.settlementAdultPrice) < 3000) ";
	                    break;
	                case "p1":
	                    prices[i] = " (getSettlePrice(IFNULL(groupRate.quauq_rate_type,officeRate.quauq_rate_type),IFNULL(groupRate.quauq_rate,officeRate.quauq_rate),IFNULL(groupRate.agent_rate_type,officeRate.agent_rate_type),IFNULL(groupRate.agent_rate,officeRate.agent_rate),agp.quauqAdultPrice,agp.settlementAdultPrice) >= 3000 AND getSettlePrice(IFNULL(groupRate.quauq_rate_type,officeRate.quauq_rate_type),IFNULL(groupRate.quauq_rate,officeRate.quauq_rate),IFNULL(groupRate.agent_rate_type,officeRate.agent_rate_type),IFNULL(groupRate.agent_rate,officeRate.agent_rate),agp.quauqAdultPrice,agp.settlementAdultPrice) <= 4999) ";
	                    break;
	                case "p2":
	                    prices[i] = " (getSettlePrice(IFNULL(groupRate.quauq_rate_type,officeRate.quauq_rate_type),IFNULL(groupRate.quauq_rate,officeRate.quauq_rate),IFNULL(groupRate.agent_rate_type,officeRate.agent_rate_type),IFNULL(groupRate.agent_rate,officeRate.agent_rate),agp.quauqAdultPrice,agp.settlementAdultPrice) >= 5000 AND getSettlePrice(IFNULL(groupRate.quauq_rate_type,officeRate.quauq_rate_type),IFNULL(groupRate.quauq_rate,officeRate.quauq_rate),IFNULL(groupRate.agent_rate_type,officeRate.agent_rate_type),IFNULL(groupRate.agent_rate,officeRate.agent_rate),agp.quauqAdultPrice,agp.settlementAdultPrice) <= 7999) ";
	                    break;
	                case "p3":
	                    prices[i] = " (getSettlePrice(IFNULL(groupRate.quauq_rate_type,officeRate.quauq_rate_type),IFNULL(groupRate.quauq_rate,officeRate.quauq_rate),IFNULL(groupRate.agent_rate_type,officeRate.agent_rate_type),IFNULL(groupRate.agent_rate,officeRate.agent_rate),agp.quauqAdultPrice,agp.settlementAdultPrice) >= 8000 AND getSettlePrice(IFNULL(groupRate.quauq_rate_type,officeRate.quauq_rate_type),IFNULL(groupRate.quauq_rate,officeRate.quauq_rate),IFNULL(groupRate.agent_rate_type,officeRate.agent_rate_type),IFNULL(groupRate.agent_rate,officeRate.agent_rate),agp.quauqAdultPrice,agp.settlementAdultPrice) <= 9999) ";
	                    break;
	                case "p4":
	                    prices[i] = " (getSettlePrice(IFNULL(groupRate.quauq_rate_type,officeRate.quauq_rate_type),IFNULL(groupRate.quauq_rate,officeRate.quauq_rate),IFNULL(groupRate.agent_rate_type,officeRate.agent_rate_type),IFNULL(groupRate.agent_rate,officeRate.agent_rate),agp.quauqAdultPrice,agp.settlementAdultPrice) >= 10000) ";
	                    break;
	                default:
	                    prices[i] = prices[i].replaceAll("元", "");
	                    int pos = returnPos(prices[i]);
	                    String[] priceInterval = prices[i].split("-");
	                    if (pos == 1) {
	                        prices[i] = " (getSettlePrice(IFNULL(groupRate.quauq_rate_type,officeRate.quauq_rate_type),IFNULL(groupRate.quauq_rate,officeRate.quauq_rate),IFNULL(groupRate.agent_rate_type,officeRate.agent_rate_type),IFNULL(groupRate.agent_rate,officeRate.agent_rate),agp.quauqAdultPrice,agp.settlementAdultPrice) >= " + priceInterval[0] + ") ";
	                    } else if(pos == -1) {
	                        prices[i] = " (getSettlePrice(IFNULL(groupRate.quauq_rate_type,officeRate.quauq_rate_type),IFNULL(groupRate.quauq_rate,officeRate.quauq_rate),IFNULL(groupRate.agent_rate_type,officeRate.agent_rate_type),IFNULL(groupRate.agent_rate,officeRate.agent_rate),agp.quauqAdultPrice,agp.settlementAdultPrice) <= " + priceInterval[1] + ") ";
	                    } else {
	                        prices[i] = " (getSettlePrice(IFNULL(groupRate.quauq_rate_type,officeRate.quauq_rate_type),IFNULL(groupRate.quauq_rate,officeRate.quauq_rate),IFNULL(groupRate.agent_rate_type,officeRate.agent_rate_type),IFNULL(groupRate.agent_rate,officeRate.agent_rate),agp.quauqAdultPrice,agp.settlementAdultPrice) >= " + priceInterval[0] + " AND getSettlePrice(IFNULL(groupRate.quauq_rate_type,officeRate.quauq_rate_type),IFNULL(groupRate.quauq_rate,officeRate.quauq_rate),IFNULL(groupRate.agent_rate_type,officeRate.agent_rate_type),IFNULL(groupRate.agent_rate,officeRate.agent_rate),agp.quauqAdultPrice,agp.settlementAdultPrice) <= " + priceInterval[1] + ") ";
	                    }
	                    break;
	
	            }
	        }
	        String priceStr = "";
	        for (int i = 0; i < prices.length; i++) {
	            if(i != prices.length -1) {
	                priceStr += prices[i] + " OR ";
	            } else {
	                priceStr += prices[i];
	            }
	        }
	        if (prices.length == 1) {
	            sql.append(priceStr);
	        } else {
	            sql.append("(" + priceStr + ")");
	        }
	    }
	
	    //余位
	    if (StringUtils.isNotBlank(freeParas)) {
	        sql.append(" AND ");
	        String[] frees = freeParas.split(",");
	        for (int i = 0; i < frees.length; i++) {
	            String free = frees[i];
	            switch (free) {
	                case "f0":
	                    frees[i] = " (agp.freePosition < 10) ";
	                    break;
	                case "f1":
	                    frees[i] = " (agp.freePosition >= 10 AND agp.freePosition <= 19) ";
	                    break;
	                case "f2":
	                    frees[i] = " (agp.freePosition >= 20 AND agp.freePosition <= 29) ";
	                    break;
	                case "f3":
	                    frees[i] = " (agp.freePosition >= 30) ";
	                    break;
	                default:
	                    int pos = returnPos(frees[i]);
	//					frees[i] = frees[i].replaceAll("元", "");
	                    String[] freeInterval = frees[i].split("-");
	                    if (pos == 1) {
	                        frees[i] = " (agp.freePosition >= " + freeInterval[0] + ") ";
	                    } else if(pos == -1) {
	                        frees[i] = " (agp.freePosition <= " + freeInterval[1] + ") ";
	                    } else {
	                        frees[i] = " (agp.freePosition >= " + freeInterval[0] + " AND agp.freePosition <= " + freeInterval[1] + ") ";
	                    }
	                    break;
	
	            }
	        }
	        String freeStr = "";
	        for (int i = 0; i < frees.length; i++) {
	            if(i != frees.length -1) {
	                freeStr += frees[i] + " OR ";
	            } else {
	                freeStr += frees[i];
	            }
	        }
	        if (frees.length == 1) {
	            sql.append(freeStr);
	        } else {
	            sql.append("(" + freeStr + ")");
	        }
	    }
	
	    //当搜索关键字不为空的时候,拼接上关键字条件:产品名称,供应商,团号
	    if(StringUtils.isNotBlank(keyword) && !"产品名称 / 供应商 / 团号 / 目的地".equals(keyword)){
	        keyword=StringUtils.trim(keyword);
	        keyword = keyword.replace("'", "''");
	        keyword = keyword.replace("%", "\\%");
	        keyword = keyword.replace("_", "\\_");
	        keyword = keyword.replace(" ", "%");
	        if (tourOutIn == null || "100000".equals(tourOutIn)) {
	            sql.append(" AND (( activity.acitivityName LIKE '%"+keyword+"%') OR  (office.name LIKE '%"+keyword+"%') OR (agp.groupCode LIKE '%"+keyword+"%') OR FIND_IN_SET('"+keyword+"', ata.parentName) OR FIND_IN_SET('"+keyword+"',ata.name) )  ");
	        } else {
	            sql.append(" AND (( activity.acitivityName LIKE '%"+keyword+"%')OR  (office.name LIKE '%"+keyword+"%') OR(agp.groupCode LIKE '%"+keyword+"%') OR FIND_IN_SET('"+keyword+"',ata.name))  ");
	        }
	    }
	    sql.append(" AND agp.groupOpenDate>='" + dt.toString("yyyy-MM-dd") + "' ");
		if ("activity".equals(groupOrActivity)) {
			sql.append(" GROUP BY activity.id ");
	    }
	    
	    if(StringUtils.isBlank(orderBy) && StringUtils.isBlank(supplierParas)) {
	        orderBy = "defaultSort DESC";
	    } else if(StringUtils.isBlank(orderBy) && StringUtils.isNotBlank(supplierParas)) {
	        orderBy = "groupOpenDate ASC";
	    }
	    orderBy = orderBy.replace("  ", " ");
	    if ("activity".equals(groupOrActivity)) { // 针对产品的出团日期排序
	    	if("groupOpenDate DESC".equals(orderBy)){
	    		orderBy = "maxGroupOpenDate DESC";
	    	}else if("groupOpenDate ASC".equals(orderBy)){
	    		orderBy = "minGroupOpenDate ASC";
	    	}
	    }
		if ("getConditionOrder".equals(orderBy)) { // 针对反查条件不排序
			orderBy = "";
		}
	    page.setOrderBy(orderBy);
	
	    if(StringUtils.isNotBlank(pageNo)){
	        page.setPageNo(Integer.valueOf(pageNo));
	    }
	    if(StringUtils.isNotBlank(pageSize)){
	        page.setPageSize(Integer.valueOf(pageSize));
	    }
	    
	    return sql.toString();
    }
    
    /**
     * 获取散拼产品目的地sql：sql优化，替换请小心
     * @author yakun.bai
     * @Date 2016-9-12
     */
    private StringBuffer getLooseActivityTargetAreaSql(String curDate) {
        StringBuffer sb = new StringBuffer("");
        sb.append("SELECT t1.id AS id, t1.srcActivityId AS srcActivityId, t1.targetAreaId AS targetAreaId, s1.name AS name, ")
                .append("(SELECT sa.name FROM sys_area sa WHERE sa.id = s1.parentId) AS parentName, s1.sys_district_id districtId FROM ")
                .append("activitytargetarea t1, sys_area s1, travelactivity t, activitygroup a ")
                .append("WHERE t1.targetAreaId = s1.id AND a.srcActivityId = t.id AND t.id = t1.srcActivityId AND ")
                .append("t.activity_kind = '2' AND a.groupOpenDate >= '" + curDate + "' ");
        return sb;
    }

    /**
     * “-”位于最左侧返回-1，位于中间返回0，位于末尾返回1
     * @param price
     * @return
     */
    private int returnPos(String price) {
        if (price.charAt(price.length() - 1) == '-') {
            return 1;
        } else if(price.charAt(0) == '-') {
            return -1;
        } else {
            return 0;
        }
    }
}
