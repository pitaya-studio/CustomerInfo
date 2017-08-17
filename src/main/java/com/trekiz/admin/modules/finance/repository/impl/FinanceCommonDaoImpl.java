package com.trekiz.admin.modules.finance.repository.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.finance.repository.IFinanceCommonDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * Created by quauq on 2016/7/14.
 */
@Repository
public class FinanceCommonDaoImpl extends BaseDaoImpl implements IFinanceCommonDao  {

    private String getSalesPerformanceSql(Map<String,Object> params){
        Long companyId = UserUtils.getUser().getCompany().getId();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT t.salerId,t.productName,t.groupCode,GROUP_CONCAT(DISTINCT t.unAgentName) AS unAgentNames,")
        .append("GROUP_CONCAT(DISTINCT t.agentName) AS agentNames,GROUP_CONCAT(t.accountedMoney) AS accountedMoney,")
        .append("SUM(t.orderPersonNum) AS personNum,t.orderType,t.activityId FROM (")
        .append("SELECT o.salerId,p.acitivityName AS productName,g.groupCode,CASE WHEN o.orderCompany =- 1 THEN ")
        .append("orderCompanyName END AS unAgentName,CASE WHEN o.orderCompany!=-1 THEN orderCompanyName END AS agentName,")
        .append("o.accounted_money AS accountedMoney,o.orderPersonNum,p.activity_kind AS orderType,g.id AS activityId,")
        .append("o.createDate,p.createDate AS pCreateDate FROM productorder o,activitygroup g,travelactivity p ")
        .append("WHERE o.delFlag = '0' AND o.productId = p.id AND o.productGroupId = g.id AND p.id = g.srcActivityId")
        .append(" AND p.proCompany=").append(companyId).append(" AND o.payStatus IN (5, 4, 1, 2, 3) ")
        .append(" AND o.salerId IS NOT NULL ")
        .append("UNION ALL ")
        .append("SELECT o.salerId,p.activity_airticket_name AS productName,p.group_code AS groupCode,")
        .append("CASE WHEN o.agentinfo_id =- 1 THEN o.nagentName END AS unAgentName,CASE WHEN o.agentinfo_id !=- 1 ")
        .append("THEN (SELECT a.agentName FROM agentinfo a WHERE a.id = o.agentinfo_id) END AS agentName,")
        .append("o.accounted_money AS accountedMoney,o.person_num AS orderPersonNum,7 AS orderType,p.id AS activityId,")
        .append("o.create_date AS createDate,p.createDate as pCreateDate FROM airticket_order o,activity_airticket p ")
        .append("WHERE o.airticket_id = p.id AND p.proCompany=").append(companyId)
        .append(" AND o.del_flag = '0' AND o.order_state <> 111 AND o.salerId IS NOT NULL ")
        .append("UNION ALL ")
        .append("SELECT o.salerId,p.productName,p.groupCode,CASE WHEN o.agentinfo_id =-1 THEN o.agentinfo_name ")
        .append("END AS unAgentName,CASE WHEN o.agentinfo_id !=- 1 THEN (SELECT a.agentName FROM agentinfo a WHERE ")
        .append("a.id = o.agentinfo_id) END AS agentName,o.accounted_money AS accountedMoney,o.travel_num AS groupPersonNum,")
        .append("6 AS orderType,p.id AS activityId,o.create_date AS createDate,p.createDate AS pCreateDate ")
        .append("FROM visa_order o,visa_products p ")
        .append("WHERE o.visa_product_id = p.id AND p.proCompanyId=").append(companyId).append(" AND o.del_flag ='0' ")
        .append("AND o.visa_order_status <> 100 AND o.mainOrderId IS NULL AND o.payStatus IN ('1', '3', '5') ")
        .append("AND o.salerId IS NOT NULL  ")
        .append(") t WHERE 1=1 ");
        if (StringUtils.isNotBlank((String)params.get("salerId"))){
            sql.append(" AND t.salerId=").append(params.get("salerId"));
        }
        if (StringUtils.isNotBlank((String)params.get("groupCode"))){
            sql.append(" AND t.groupCode LIKE '%").append(params.get("groupCode")).append("%'");
        }
        if (StringUtils.isNotBlank((String)params.get("productName"))){
            sql.append(" AND t.productName LIKE '%").append(params.get("productName")).append("%'");
        }
        if (StringUtils.isNotBlank((String)params.get("departmentId"))){
            sql.append(" AND EXISTS(SELECT 'x' FROM sys_user_dept_job_new udj,department d WHERE udj.dept_id = d.id ")
                    .append("AND udj.user_id = t.salerId AND udj.del_flag='0' AND d.delFlag='0' AND d.id=")
                    .append(params.get("departmentId"))
            /*.append(" AND NOT FIND_IN_SET(d.id,(SELECT GROUP_CONCAT(DISTINCT d.parent_ids SEPARATOR '') FROM ")
            .append("sys_user_dept_job_new udj,department d WHERE udj.dept_id = d.id AND udj.user_id = t.salerId))")*/
                    .append(")");
        }
        if (StringUtils.isNotBlank((String)params.get("beginDate"))){ //2016年04月
            String  beginDate = (String)params.get("beginDate");
            String year = beginDate.substring(0,4);
            String month = beginDate.substring(5,7);
            sql.append(" AND t.createDate >='").append(year).append("-").append(month).append("-01 00:00:00'");
            sql.append(" AND t.createDate <='").append(year).append("-").append(month).append("-31 23:59:59'");
        }
        sql.append(" GROUP BY t.salerId,t.activityId,t.orderType ORDER BY t.pCreateDate DESC");
        return sql.toString();
    }

    /**
     * 页面分页查询调用。 yudong.xu 2016.7.22
     */
    public Page getSalesPerformance(Page page,Map<String,Object> params){
        String sql = getSalesPerformanceSql(params);
        return findBySql(page,sql.toString(),Map.class);
    }

    /**
     * 导出Excel时调用，查询所有的记录，比较耗时。 yudong.xu 2016.7.22
     */
    public List<Map<String,Object>> getSalesPerformance(Map<String,Object> params){
        String sql = getSalesPerformanceSql(params);
        List<Map<String,Object>> list = findBySql(sql,Map.class);
        return list;
    }

    public Map<Object,String> getUserNameDept(Integer userId){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT u.`name` AS userName,GROUP_CONCAT(DISTINCT d.`name` SEPARATOR ',') AS deptNames FROM ")
        .append("sys_user_dept_job_new udj,department d,sys_user u WHERE udj.dept_id=d.id AND udj.user_id=u.id ")
        .append(" AND udj.del_flag = '0' AND udj.user_id=").append(userId).append(" AND d.`name` !='接待社内部'")
        .append(" AND d.delFlag='0' ")
        .append(" AND NOT FIND_IN_SET(d.id,(SELECT GROUP_CONCAT(DISTINCT d.parent_ids SEPARATOR '') FROM ")
        .append("sys_user_dept_job_new udj,department d WHERE udj.dept_id=d.id AND udj.del_flag='0' AND d.delFlag='0'")
        .append(" AND udj.user_id=").append(userId).append("))");
        List<Map<Object,String>> result = findBySql(sql.toString(),Map.class);
        if (CollectionUtils.isNotEmpty(result))
            return result.get(0);
        return new HashMap<>();
    }

    public List<Map<String,Object>> getCurrencyAmount(String serials){
        if (StringUtils.isBlank(serials)){
            return new ArrayList<>();
        }
        StringBuilder builder = new StringBuilder();
        String[] serialArr = serials.split(",");
        int idx = serialArr.length -1;
        for (int i = 0; i < idx; i++) {
            builder.append("'").append(serialArr[i]).append("',");
        }
        builder.append("'").append(serialArr[idx]).append("'");
        serials = builder.toString();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT c.currency_mark AS mark,ma.currencyId,ma.amount FROM money_amount ma,currency c ")
        .append("WHERE ma.currencyId=c.currency_id AND ma.serialNum in (").append(serials).append(")");
        return findBySql(sql.toString(),Map.class);
    }

}
