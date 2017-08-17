package com.trekiz.admin.modules.statisticAnalysis.home.dao.impl;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.utils.Collections3;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.statisticAnalysis.common.OrderDataStatistics;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.StatisticAnalysisDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.springframework.stereotype.Repository;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by quauq on 2016/12/22.
 */
@Repository
public class StatisticAnalysisDaoImpl extends BaseDaoImpl implements StatisticAnalysisDao {

    /**
     * 获取下单时间的最大值（因需求更改，现修改为获取当前时间）
     * @return
     */
    public String getTimeMax(){

        Calendar calendar = Calendar.getInstance();
        Date current = calendar.getTime();
        String currentTime = null;
        try {
            currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(current);
        }catch (Exception e){
            e.printStackTrace();
        }
        return currentTime;
       /* String sql = "SELECT MAX(order_createtime) AS maxTime FROM order_data_statistics ods WHERE del_flag = '0' AND ods.order_createtime != '0000-00-00 00:00:00' AND CASE WHEN order_type = '6' THEN order_status != '2' ELSE order_status NOT IN ('99', '111') END AND ods.company_uuid = '" + UserUtils.getUser().getCompany().getUuid() + "' ";
        List<Map<String, Object>> list = this.findBySql(sql, Map.class);
        if(!Collections3.isEmpty(list)){
            return list.get(0).get("maxTime").toString();
        }
        return null;*/
    }

    /**
     * 获取批发商使用系统最小时间
     * @return
     */
    public String getTimeMin(){
        String sql = "SELECT MIN(su.createDate) AS minTime FROM sys_user su WHERE su.companyId = '"+UserUtils.getUser().getCompany().getId()+"';";
        //String sql = "SELECT MIN(order_createtime) AS minTime FROM order_data_statistics ods WHERE del_flag = '0' AND ods.order_createtime != '0000-00-00 00:00:00' AND CASE WHEN order_type = '6' THEN order_status != '2' ELSE order_status NOT IN ('99', '111') END AND ods.company_uuid = '" + UserUtils.getUser().getCompany().getUuid() + "' ";
        List<Map<String, Object>> list = this.findBySql(sql, Map.class);
        if(!Collections3.isEmpty(list)){
            return list.get(0).get("minTime").toString();
        }
        return null;
    }

    /**
     * 查询某一个小时的订单数
     * @param time：2016-06-06 11
     * @return
     */
    public String findOrderNumByHour(String time, Map map){
        String sql =
                "SELECT num FROM (" +
                        " SELECT" +
                        " COUNT(create_date) AS num," +
                        " create_date," +
                        " company_uuid" +
                        " FROM" +
                        " (" +
                        " SELECT" +
                        " DATE_FORMAT(ods.order_createtime,'%Y-%m-%d %H') AS create_date," +
                        " ods.agentinfo_id AS agentinfo_id," +
                        " ods.company_uuid AS company_uuid," +
                        " ods.del_flag AS del_flag" +
                        " FROM" +
                        " order_data_statistics ods" +
                        " WHERE" +
                        " ods.del_flag = '0'" ;
        if (!"0".equals(map.get("orderType"))){
            sql = sql + "AND ods.order_type = '" + map.get("orderType") + "' ";
        }
        sql = sql +
                        " AND CASE WHEN order_type = '6' THEN order_status != '2' ELSE order_status NOT IN ('99', '111') END" +
                        " ) AS order_num" +
                        " WHERE company_uuid = '" + UserUtils.getUser().getCompany().getUuid() + "'" +
                        " AND create_date = '" + time + "'" +
                        " GROUP BY create_date, company_uuid" +
                        " ) AS order_hour";

        List<Map<String, Object>> list = this.findBySql(sql, Map.class);
        if(!Collections3.isEmpty(list)){
            if (list.get(0).get("num") == null){
                return "0";
            }
            return list.get(0).get("num").toString();
        }
        return "0";
    }

    /**
     * 查询某一天的订单数
     * @param time：2016-06-06
     * @return
     */
    public String findOrderNumByDay(String time, Map map){
        String sql =
                "SELECT num FROM (" +
                        " SELECT" +
                        " COUNT(create_date) AS num," +
                        " create_date," +
                        " company_uuid" +
                        " FROM" +
                        " (" +
                        " SELECT" +
                        " DATE_FORMAT(ods.order_createtime,'%Y-%m-%d') AS create_date," +
                        " ods.company_uuid AS company_uuid," +
                        " ods.del_flag AS del_flag" +
                        " FROM" +
                        " order_data_statistics ods" +
                        " WHERE" +
                        " ods.del_flag = '0'" ;
        if (!"0".equals(map.get("orderType"))){
            sql = sql + "AND ods.order_type = '" + map.get("orderType") + "' ";
        }
        sql = sql +
                        " AND CASE WHEN order_type = '6' THEN order_status != '2' ELSE order_status NOT IN ('99', '111') END" +
                        " ) AS order_num" +
                        " WHERE company_uuid = '"+UserUtils.getUser().getCompany().getUuid()+ "' " +
                        " AND create_date = '"+ time + "'" +
                        " GROUP BY create_date, company_uuid" +
                        " ) AS order_hour";

        List<Map<String, Object>> list = this.findBySql(sql, Map.class);
        if(!Collections3.isEmpty(list)){
            if (list.get(0).get("num") == null){
                return "0";
            }
            return list.get(0).get("num").toString();
        }
        return "0";
    }

    /**
     * 查询某月内的任一周的订单数
     * @param time：2016-12
     * @return
     */
    public String findOrderNumByWeekAndMonth(String time, Map map){
        String sql =
                "SELECT num FROM ( " +
                "SELECT " +
                "COUNT(*) AS num , " +
                "create_date_month, " +
                "create_date_week, " +
                "company_uuid " +
                "FROM " +
                "( " +
                "SELECT " +
                "DATE_FORMAT(ods.order_createtime, '%Y-%m') AS create_date_month, " +
                "DATE_FORMAT(ods.order_createtime, '%u') AS create_date_week, " +
                "ods.agentinfo_id AS agentinfo_id, " +
                "ods.company_uuid AS company_uuid, " +
                "ods.del_flag AS del_flag " +
                "FROM " +
                "order_data_statistics ods " +
                "WHERE " +
                "ods.del_flag = '0' " ;
        if (!"0".equals(map.get("orderType"))){
            sql = sql + "AND ods.order_type = '" + map.get("orderType") + "' ";
        }
        sql = sql +
                "AND CASE WHEN order_type = '6' THEN order_status != '2' ELSE order_status NOT IN ('99', '111') END " +
                ") AS order_num " +
                "WHERE order_num.company_uuid = '" + UserUtils.getUser().getCompany().getUuid() + "' " +
                "AND order_num.create_date_month = DATE_FORMAT('" + time + "','%Y-%m') " +
                "AND order_num.create_date_week = DATE_FORMAT(STR_TO_DATE('" + time + "','%Y-%m-%d'), '%u') " +
                "GROUP BY " +
                "create_date_month, " +
                "create_date_week, " +
                "company_uuid " +
                ") AS order_week ";

        List<Map<String, Object>> list = this.findBySql(sql, Map.class);
        if(!Collections3.isEmpty(list)){
            if (list.get(0).get("num") == null){
                return "0";
            }
            return list.get(0).get("num").toString();
        }
        return "0";
    }

    /**
     * 查询任一周的订单数
     * @param time：2016-12
     * @return
     */
    public String findOrderNumByWeek(String time, Map<String,String> map){
        String sql =
                        "SELECT " +
                        "COUNT(*) AS num  " +
                        "FROM " +
                        "( " +
                        "SELECT " +
                        "DATE_FORMAT(ods.order_createtime, '%u') AS create_date_week, " +
                        "DATE_FORMAT(ods.order_createtime, '%Y-%m-%d') AS create_date, " +
                        "ods.del_flag AS del_flag " +
                        "FROM " +
                        "order_data_statistics ods " +
                        "WHERE " +
                        "ods.del_flag = '0' " +
                        "AND company_uuid = '" + UserUtils.getUser().getCompany().getUuid() + "'  ";
        if (!"0".equals(map.get("orderType"))){
            sql = sql + "AND ods.order_type = '" + map.get("orderType") + "' ";
        }
        sql = sql +
                        "AND CASE WHEN order_type = '6' THEN order_status != '2' ELSE order_status NOT IN ('99', '111') END " +
                        ") AS order_num " +
                        "WHERE order_num.create_date_week = DATE_FORMAT(STR_TO_DATE('" + time + "','%Y-%m-%d'), '%u') " ;
        String beginTime = map.get("beginTime");
        String lastTime = map.get("lastTime");
        if (null != beginTime && null != lastTime){
            //bug17443  此时需要修改年份来对比去年同期
            beginTime = time.substring(0,4) + beginTime.substring(4);
            lastTime = time.substring(0,4) + lastTime.substring(4);
            sql +=  "AND order_num.create_date >= '" + beginTime +"' " +
                    "AND order_num.create_date <= '" + lastTime +"'; ";
        }

        List<Map<String, Object>> list = this.findBySql(sql, Map.class);
        if(!Collections3.isEmpty(list)){
            if (list.get(0).get("num") == null){
                return "0";
            }
            return list.get(0).get("num").toString();
        }
        return "0";
    }

    /**
     * 查询某月内的订单数
     * @param time：2016-12
     * @return
     */
    public String findOrderNumByMonth(String time, Map<String, String> map){
        String sql =
                        "SELECT " +
                        "COUNT(create_date) as num " +
                        "FROM " +
                        "( " +
                        "SELECT " +
                        "DATE_FORMAT(ods.order_createtime, '%Y-%m') AS create_date, " +
                        "DATE_FORMAT(ods.order_createtime,'%Y-%m-%d') AS create_date_month," +
                        "ods.company_uuid AS company_uuid, " +
                        "ods.del_flag AS del_flag " +
                        "FROM " +
                        "order_data_statistics ods " +
                        "WHERE " +
                        "ods.del_flag = '0' " +
                        "AND company_uuid = '" + UserUtils.getUser().getCompany().getUuid() + "' " ;

        if (!"0".equals(map.get("orderType"))){
            sql = sql + "AND ods.order_type = '" + map.get("orderType") + "' ";
        }
        sql = sql +
                        "AND CASE WHEN order_type = '6' THEN order_status != '2' ELSE order_status NOT IN ('99', '111') END " +
                        ") AS order_num " +
                        "WHERE order_num.create_date = DATE_FORMAT(STR_TO_DATE('" + time + "','%Y-%m-%d'), '%Y-%m') " ;
        String beginTime = map.get("beginTime");
        String lastTime = map.get("lastTime");
        if (null != beginTime && null != lastTime){
            beginTime = time.substring(0,4) + beginTime.substring(4);
            lastTime = time.substring(0,4) + lastTime.substring(4);
            sql +=  "AND order_num.create_date_month >= '" + beginTime +"' " +
                    "AND order_num.create_date_month <= '" + lastTime +"' ";
        }


        List<Map<String, Object>> list = this.findBySql(sql, Map.class);
        if(!Collections3.isEmpty(list)){
            if (list.get(0).get("num") == null){
                return "0";
            }
            return list.get(0).get("num").toString();
        }
        return "0";
    }

    /**
     * 查询某年的订单人数
     * @param time
     * @param map
     * @return
     */
    public String findOrderNumByYear(String time, Map map){
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT ")
                .append("COUNT(create_date) AS num ")
                .append("FROM ( ")
                .append("SELECT ")
                .append("DATE_FORMAT(ods.order_createtime,'%Y') AS create_date, ")
                .append("DATE_FORMAT(ods.order_createtime,'%Y-%m-%d') AS create_date_year, ")
                .append("ods.del_flag AS del_flag ")
                .append("FROM ")
                .append("order_data_statistics ods ")
                .append("WHERE ")
                .append("ods.del_flag = '0' ")
                .append("AND company_uuid = '").append(UserUtils.getUser().getCompany().getUuid()).append("' ");
        if (!"0".equals(map.get("orderType"))){
            stringBuffer = stringBuffer.append("AND ods.order_type = '").append(map.get("orderType")).append("' ");
        }
        stringBuffer.append("AND CASE ")
                .append("WHEN order_type = '6' THEN ")
                .append("order_status != '2' ")
                .append("ELSE ")
                .append("order_status NOT IN ('99', '111') ")
                .append("END ")
                .append(") AS order_num ")
                .append("WHERE ")
                .append("create_date = DATE_FORMAT('").append(time).append("','%Y') ");
        if (null != map.get("beginTime") && null != map.get("lastTime")){
            stringBuffer.append("AND order_num.create_date_year >= '").append(map.get("beginTime")).append("' ")
                   .append("AND order_num.create_date_year <= '").append(map.get("lastTime")).append("'; ");
        }
        List<Map<String, Object>> list = this.findBySql(stringBuffer.toString(), Map.class);
        if (!Collections3.isEmpty(list)){
            if (list.get(0).get("num") == null){
                return "0";
            }
            return list.get(0).get("num").toString();
        }
        return "0";
    }
    /**
     * 查询某个小时的收客人数
     * @param time
     * @return
     */
    public String findPersonNumByHour(String time, Map map){

        String sql =
                "SELECT person_num AS personNum FROM ( " +
                        "SELECT " +
                        "SUM(order_person_num) AS person_num, " +
                        "create_date, " +
                        "company_uuid " +
                        "FROM " +
                        "( " +
                        "SELECT " +
                        "DATE_FORMAT(ods.order_createtime,'%Y-%m-%d %H') AS create_date, " +
                        "ods.order_person_num AS order_person_num, " +
                        "ods.agentinfo_id AS agentinfo_id, " +
                        "ods.company_uuid AS company_uuid, " +
                        "ods.del_flag AS del_flag " +
                        "FROM " +
                        "order_data_statistics ods " +
                        "WHERE " +
                        "ods.del_flag = '0' " ;
        if (!"0".equals(map.get("orderType"))){
            sql = sql + "AND ods.order_type = '" + map.get("orderType") + "' ";
        }
        sql = sql +
                        "AND CASE WHEN order_type = '6' THEN order_status != '2' ELSE order_status NOT IN ('99', '111') END " +
                        ") AS order_person " +
                        "WHERE order_person.company_uuid = '" + UserUtils.getUser().getCompany().getUuid() + "' " +
                        "AND order_person.create_date = DATE_FORMAT('" + time + "','%Y-%m-%d %H') " +
                        "GROUP BY create_date, company_uuid " +
                        ") AS person_hour";
        List<Map<String, Object>> list = this.findBySql(sql, Map.class);
        if(!Collections3.isEmpty(list)){
            if (list.get(0).get("personNum") == null){
                return "0";
            }
            return list.get(0).get("personNum").toString();
        }
        return "0";
    }

    /**
     * 查询某天的收客人数
     * @param time
     * @return
     */
    public String findPersonNumByDay(String time, Map map){

        String sql =
                "SELECT person_num AS personNum FROM ( " +
                        "SELECT " +
                        "SUM(order_person_num) AS person_num, " +
                        "create_date, " +
                        "company_uuid " +
                        "FROM " +
                        "( " +
                        "SELECT " +
                        "DATE_FORMAT(ods.order_createtime,'%Y-%m-%d') AS create_date, " +
                        "ods.order_person_num AS order_person_num, " +
                        "ods.agentinfo_id AS agentinfo_id, " +
                        "ods.company_uuid AS company_uuid, " +
                        "ods.del_flag AS del_flag " +
                        "FROM " +
                        "order_data_statistics ods " +
                        "WHERE " +
                        "ods.del_flag = '0' " ;
        if (!"0".equals(map.get("orderType"))){
            sql = sql + "AND ods.order_type = '" + map.get("orderType") + "' ";
        }
        sql = sql +
                        "AND CASE WHEN order_type = '6' THEN order_status != '2' ELSE order_status NOT IN ('99', '111') END " +
                        ") AS order_person " +
                        "WHERE order_person.company_uuid = '" + UserUtils.getUser().getCompany().getUuid() + "' " +
                        "AND order_person.create_date = DATE_FORMAT('" + time + "','%Y-%m-%d') " +
                        //"AND order_person.create_date_week = DATE_FORMAT(STR_TO_DATE('" + time + "','%Y-%m-%d'), '%u') " +
                        "GROUP BY create_date, company_uuid " +
                        ") AS person_day";
        List<Map<String, Object>> list = this.findBySql(sql, Map.class);
        if(!Collections3.isEmpty(list)){
            if (list.get(0).get("personNum") == null){
                return "0";
            }
            return list.get(0).get("personNum").toString();
        }
        return "0";
    }

    /**
     * 查询某月内的任一周的订单人数
     * @param time：2016-12
     * @return
     */
    public String findPersonNumByWeekAndMonth(String time, Map map){
        String sql =
                "SELECT person_num AS personNum FROM ( " +
                        "SELECT " +
                        "SUM(order_person_num) AS person_num, " +
                        //"COUNT(*) AS num , " +
                        "create_date_month, " +
                        "create_date_week, " +
                        "company_uuid " +
                        "FROM " +
                        "( " +
                        "SELECT " +
                        "ods.order_person_num AS order_person_num, " +
                        "DATE_FORMAT(ods.order_createtime, '%Y-%m') AS create_date_month, " +
                        "DATE_FORMAT(ods.order_createtime, '%u') AS create_date_week, " +
                        "ods.agentinfo_id AS agentinfo_id, " +
                        "ods.company_uuid AS company_uuid, " +
                        "ods.del_flag AS del_flag " +
                        "FROM " +
                        "order_data_statistics ods " +
                        "WHERE " +
                        "ods.del_flag = '0' " ;
        if (!"0".equals(map.get("orderType"))){
            sql = sql + "AND ods.order_type = '" + map.get("orderType") + "' ";
        }
        sql = sql +
                        "AND CASE WHEN order_type = '6' THEN order_status != '2' ELSE order_status NOT IN ('99', '111') END " +
                        ") AS order_num " +
                        "WHERE order_num.company_uuid = '" + UserUtils.getUser().getCompany().getUuid() + "' " +
                        "AND order_num.create_date_month = DATE_FORMAT('" + time + "','%Y-%m') " +
                        "AND order_num.create_date_week = DATE_FORMAT(STR_TO_DATE('" + time + "','%Y-%m-%d'), '%u') " +
                        "GROUP BY " +
                        "create_date_month, " +
                        "create_date_week, " +
                        "company_uuid " +
                        ") AS order_week ";

        List<Map<String, Object>> list = this.findBySql(sql, Map.class);
        if(!Collections3.isEmpty(list)){
            if (list.get(0).get("personNum") == null){
                return "0";
            }
            return list.get(0).get("personNum").toString();
        }
        return "0";
    }



    /**
     * 查询某周的收客人数
     * @param time
     * @return
     */
    public String findPersonNumByWeek(String time, Map<String, String> map){

        String sql =
                        "SELECT " +
                        "SUM(order_person_num) AS personNum " +
                        "FROM " +
                        "( " +
                        "SELECT " +
                        "DATE_FORMAT(ods.order_createtime,'%Y-%m-%d') AS create_date_month, " +
                        "DATE_FORMAT(ods.order_createtime, '%u') AS create_date_week, " +
                        "ods.order_person_num AS order_person_num " +
                        "FROM " +
                        "order_data_statistics ods " +
                        "WHERE " +
                        "ods.del_flag = '0' " +
                        "AND company_uuid = '" + UserUtils.getUser().getCompany().getUuid() + "' " ;
        if (!"0".equals(map.get("orderType"))){
            sql = sql + "AND ods.order_type = '" + map.get("orderType") + "' ";
        }
        sql = sql +
                        "AND CASE WHEN order_type = '6' THEN order_status != '2' ELSE order_status NOT IN ('99', '111') END " +
                        ") AS order_person " +
                        "WHERE order_person.create_date_week = DATE_FORMAT(STR_TO_DATE('" + time + "','%Y-%m-%d'), '%u')  ";
        String beginTime = map.get("beginTime");
        String lastTime = map.get("lastTime");
        if (null != beginTime && null != lastTime){
            beginTime = time.substring(0,4) + beginTime.substring(4);
            lastTime = time.substring(0,4) + lastTime.substring(4);
            sql +=  "AND order_person.create_date_month >= '" + beginTime + "' " +
                    "AND order_person.create_date_month <= '" + lastTime + "' ";
        }

        List<Map<String, Object>> list = this.findBySql(sql, Map.class);
        if(!Collections3.isEmpty(list)){
            if (list.get(0).get("personNum") == null){
                return "0";
            }
            return list.get(0).get("personNum").toString();
        }
        return "0";
    }

    /**
     * 查询某月的收客人数
     * @param time
     * @return
     */
    public String findPersonNumByMonth(String time, Map<String, String> map){

        String sql =    "SELECT " +
                        "SUM(order_person_num) personNum " +
                        "FROM " +
                        "( " +
                        "SELECT " +
                        "DATE_FORMAT(ods.order_createtime,'%Y-%m') AS create_date, " +
                        "DATE_FORMAT(ods.order_createtime,'%Y-%m-%d') AS create_date_month, " +
                        "ods.order_person_num AS order_person_num, " +
                        "ods.company_uuid AS company_uuid, " +
                        "ods.del_flag AS del_flag " +
                        "FROM " +
                        "order_data_statistics ods " +
                        "WHERE ods.del_flag = '0' "  +
                        "AND company_uuid = '" + UserUtils.getUser().getCompany().getUuid() + "' ";
        if (!"0".equals(map.get("orderType"))){
            sql = sql + "AND ods.order_type = '" + map.get("orderType") + "' ";
        }
        sql = sql +
                        "AND CASE WHEN order_type = '6' THEN order_status != '2' ELSE order_status NOT IN ('99', '111') END " +
                        ") AS order_persion " +
                        "WHERE order_persion.create_date = DATE_FORMAT(STR_TO_DATE('" + time + "','%Y-%m-%d'),'%Y-%m') ";
        String beginTime = map.get("beginTime");
        String lastTime = map.get("lastTime");
        if (null != beginTime && null != lastTime){
            beginTime = time.substring(0,4) + beginTime.substring(4);
            lastTime = time.substring(0,4) + lastTime.substring(4);
            sql +=  "AND order_persion.create_date_month >= '" + beginTime +"' " +
                    "AND order_persion.create_date_month <= '" + lastTime +"' ";
        }

        List<Map<String, Object>> list = this.findBySql(sql, Map.class);
        if(!Collections3.isEmpty(list)){
            if (list.get(0).get("personNum") == null){
                return "0";
            }else {
                return list.get(0).get("personNum").toString();
            }
        }
        return "0";
    }

    /**
     * 查询某年的收客人数
     * @param time
     * @return
     */
    public String findPersonNumByYear(String time, Map map){

        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ")
                .append("SUM(order_person_num) personNum " )
                .append("FROM ")
                .append("( ")
                .append("SELECT ")
                .append("DATE_FORMAT(ods.order_createtime,'%Y') AS create_date, ")
                .append("DATE_FORMAT(ods.order_createtime,'%Y-%m-%d') AS create_date_year, ")
                .append("ods.order_person_num AS order_person_num, ")
                .append("ods.del_flag AS del_flag ")
                .append("FROM ")
                .append("order_data_statistics ods ")
                .append("WHERE del_flag = '0' ")
                .append("AND company_uuid = '").append(UserUtils.getUser().getCompany().getUuid()).append("' ");
        if (!"0".equals(map.get("orderType"))){
            sql.append("AND ods.order_type = '").append(map.get("orderType")).append("' ");
        }
        sql.append("AND CASE WHEN order_type = '6' THEN order_status != '2' ELSE order_status NOT IN ('99', '111') END ")
                .append(") AS order_persion ");
        sql.append("WHERE order_persion.create_date = DATE_FORMAT(STR_TO_DATE('").append(time).append("','%Y-%m-%d'),'%Y') ");
        if (null != map.get("beginTime") && null != map.get("lastTime")){
            sql.append("AND order_persion.create_date_year >= '").append(map.get("beginTime")).append("' ")
                    .append("AND order_persion.create_date_year <= '").append(map.get("lastTime")).append("' ");
        }


        List<Map<String, Object>> list = this.findBySql(sql.toString(), Map.class);
        if(!Collections3.isEmpty(list)){
            if (list.get(0).get("personNum") == null){
                return "0";
            }
            return list.get(0).get("personNum").toString();
        }
        return "0";
    }

    /**
     * 查询某小时的金额
     * @param time
     * @return
     */
    public  String findMoneyAmountByHour(String time, Map map){
        String sql =
                "SELECT " +
                "SUM(amount) AS moneyAmount " +
                "FROM " +
                "( " +
                "SELECT " +
                "DATE_FORMAT(ods.order_createtime,'%Y-%m-%d %H') AS create_date, " +
                "ods.amount AS amount, " +
                "ods.company_uuid AS company_uuid, " +
                "ods.del_flag AS del_flag " +
                "FROM " +
                "order_data_statistics ods " +
                "WHERE ods.del_flag = '0' " ;
        if (!"0".equals(map.get("orderType"))){
            sql = sql + "AND ods.order_type = '" + map.get("orderType") + "' ";
        }
        sql = sql +
                "AND CASE WHEN order_type = '6' THEN order_status != '2' ELSE order_status NOT IN ('99', '111') END " +
                "AND company_uuid = '" + UserUtils.getUser().getCompany().getUuid() + "' " +
                ") AS order_amount " +
                "WHERE order_amount.create_date = DATE_FORMAT(STR_TO_DATE('" + time + "','%Y-%m-%d %H'),'%Y-%m-%d %H') " +
                "GROUP BY " +
                "create_date " ;
        List<Map<String, Object>> list = this.findBySql(sql, Map.class);
        if(!Collections3.isEmpty(list)){
            if (list.get(0).get("moneyAmount") == null){
                return "0";
            }
            return list.get(0).get("moneyAmount").toString();
        }
        return "0";
    }

    /**
     * 查询某天的金额
     * @param time
     * @return
     */
    public  String findMoneyAmountByDay(String time, Map map){
        String sql =
                "SELECT " +
                "SUM(amount) AS moneyAmount " +
                "FROM " +
                "( " +
                "SELECT " +
                "DATE_FORMAT(ods.order_createtime,'%Y-%m-%d') AS create_date, " +
                "ods.amount AS amount, " +
                "ods.company_uuid AS company_uuid, " +
                "ods.del_flag AS del_flag " +
                "FROM " +
                "order_data_statistics ods " +
                "WHERE ods.del_flag = '0' " ;
        if (!"0".equals(map.get("orderType"))){
            sql = sql + "AND ods.order_type = '" + map.get("orderType") + "' ";
        }
        sql = sql +
                "AND CASE WHEN order_type = '6' THEN order_status != '2' ELSE order_status NOT IN ('99', '111') END " +
                "AND company_uuid = '" + UserUtils.getUser().getCompany().getUuid() + "' " +
                ") AS order_amount " +
                "WHERE order_amount.create_date = DATE_FORMAT(STR_TO_DATE('" + time + "','%Y-%m-%d'),'%Y-%m-%d') " +
                "GROUP BY " +
                "create_date ";
        List<Map<String, Object>> list = this.findBySql(sql, Map.class);
        if(!Collections3.isEmpty(list)){
            if (list.get(0).get("moneyAmount") == null){
                return "0";
            }
            return list.get(0).get("moneyAmount").toString();
        }
        return "0";
    }

    /**
     * 查询某月内的任一周的订单金额
     * @param time：2016-12
     * @return
     */
    public String findMoneyAmountByWeekAndMonth(String time, Map map){
        String sql =
                        "SELECT " +
                        "SUM(amount) AS moneyAmount " +
                        "FROM " +
                        "( " +
                        "SELECT " +
                        "ods.amount AS amount, " +
                        "DATE_FORMAT(ods.order_createtime, '%Y-%m') AS create_date_month, " +
                        "DATE_FORMAT(ods.order_createtime, '%u') AS create_date_week, " +
                        "ods.company_uuid AS company_uuid, " +
                        "ods.del_flag AS del_flag " +
                        "FROM " +
                        "order_data_statistics ods " +
                        "WHERE " +
                        "ods.del_flag = '0' " ;
        if (!"0".equals(map.get("orderType"))){
            sql = sql + "AND ods.order_type = '" + map.get("orderType") + "' ";
        }
        sql = sql +
                        "AND CASE WHEN order_type = '6' THEN order_status != '2' ELSE order_status NOT IN ('99', '111') END " +
                "AND company_uuid = '" + UserUtils.getUser().getCompany().getUuid() + "' " +
                        ") AS order_amount " +
                        "WHERE order_amount.create_date_month = DATE_FORMAT('" + time + "','%Y-%m') " +
                        "AND order_amount.create_date_week = DATE_FORMAT(STR_TO_DATE('" + time + "','%Y-%m-%d'), '%u') " +
                " GROUP BY order_amount.create_date_month ";
        List<Map<String, Object>> list = this.findBySql(sql, Map.class);
        if(!Collections3.isEmpty(list)){
            if (list.get(0).get("moneyAmount") == null){
                return "0";
            }
            return list.get(0).get("moneyAmount").toString();
        }
        return "0";
    }

    /**
     * 查询某周的金额
     * @param time
     * @return
     */
    public  String findMoneyAmountByWeek(String time, Map<String,String> map){
        String sql =
                "SELECT " +
                "SUM(amount) AS moneyAmount " +
                "FROM " +
                "( " +
                "SELECT " +
                "DATE_FORMAT(ods.order_createtime,'%Y-%m-%d') AS create_date_month, " +
                "DATE_FORMAT(ods.order_createtime, '%u') AS create_date_week, " +
                "ods.amount AS amount " +
                "FROM " +
                "order_data_statistics ods " +
                "WHERE ods.del_flag = '0' " ;
        if (!"0".equals(map.get("orderType"))){
            sql = sql + "AND ods.order_type = '" + map.get("orderType") + "' ";
        }
        sql = sql +
                "AND CASE WHEN order_type = '6' THEN order_status != '2' ELSE order_status NOT IN ('99', '111') END " +
                "AND company_uuid = '" + UserUtils.getUser().getCompany().getUuid() +"' " +
                ") AS order_amount " +
                "WHERE order_amount.create_date_week = DATE_FORMAT(STR_TO_DATE('" + time + "','%Y-%m-%d'), '%u')  " ;
        String beginTime = map.get("beginTime");
        String lastTime = map.get("lastTime");
        if (null != beginTime && null != lastTime){
            beginTime = time.substring(0,4) + beginTime.substring(4);
            lastTime = time.substring(0,4) + lastTime.substring(4);
            sql +=  "AND order_amount.create_date_month >= '" + beginTime +"' " +
                    "AND order_amount.create_date_month <= '" + lastTime +"' ";
        }
        List<Map<String, Object>> list = this.findBySql(sql, Map.class);
        if(!Collections3.isEmpty(list)){
            if (list.get(0).get("moneyAmount") == null){
                return "0";
            }
            return list.get(0).get("moneyAmount").toString();
        }
        return "0";
    }

    /**
     * 查询某月的金额
     * @param time
     * @return
     */
    public  String findMoneyAmountByMonth(String time, Map<String, String> map){
        String sql =
                "SELECT " +
                "SUM(amount) AS moneyAmount " +
                "FROM " +
                "( " +
                "SELECT " +
                "DATE_FORMAT(ods.order_createtime,'%Y-%m') AS create_date, " +
                "DATE_FORMAT(ods.order_createtime,'%Y-%m-%d') AS create_date_month, " +
                "ods.amount AS amount, " +
                "ods.company_uuid AS company_uuid, " +
                "ods.del_flag AS del_flag " +
                "FROM " +
                "order_data_statistics ods " +
                "WHERE  ods.del_flag = '0' " ;
        if (!"0".equals(map.get("orderType"))){
            sql = sql + "AND ods.order_type = '" + map.get("orderType") + "' ";
        }
        sql = sql +
                "AND CASE WHEN order_type = '6' THEN order_status != '2' ELSE order_status NOT IN ('99', '111') END " +
                "AND company_uuid = '" + UserUtils.getUser().getCompany().getUuid() +"'  " +

                ") AS order_amount " +
                "WHERE order_amount.create_date = DATE_FORMAT('" + time + "','%Y-%m') " ;
        String beginTime = map.get("beginTime");
        String lastTime = map.get("lastTime");
        if (null != beginTime && null != lastTime){
            beginTime = time.substring(0,4) + beginTime.substring(4);
            lastTime = time.substring(0,4) + lastTime.substring(4);
            sql +=  "AND order_amount.create_date_month >= '" + beginTime +"' " +
                    "AND order_amount.create_date_month <= '" + lastTime +"' ";
        }
        sql += "GROUP BY create_date " ;
        List<Map<String, Object>> list = this.findBySql(sql, Map.class);
        if(!Collections3.isEmpty(list)){
            if (list.get(0).get("moneyAmount") == null){
                return "0";
            }
            return list.get(0).get("moneyAmount").toString();
        }
        return "0";
    }

    /**
     * 查询某年的金额
     * @param time
     * @return
     */
    public  String findMoneyAmountByYear(String time, Map map){
        String sql =
                "SELECT " +
                        "SUM(amount) AS moneyAmount " +
                        "FROM " +
                        "( " +
                        "SELECT " +
                        "DATE_FORMAT(ods.order_createtime,'%Y') AS create_date, " +
                        "DATE_FORMAT(ods.order_createtime,'%Y-%m-%d') AS create_date_year, " +
                        "ods.amount AS amount, " +
                        "ods.company_uuid AS company_uuid, " +
                        "ods.del_flag AS del_flag " +
                        "FROM " +
                        "order_data_statistics ods " +
                        "WHERE  ods.del_flag = '0' " ;
        if (!"0".equals(map.get("orderType"))){
            sql = sql + "AND ods.order_type = '" + map.get("orderType") + "' ";
        }
        sql = sql +
                "AND CASE WHEN order_type = '6' THEN order_status != '2' ELSE order_status NOT IN ('99', '111') END " +
                "AND company_uuid = '" + UserUtils.getUser().getCompany().getUuid() +"'  " +

                ") AS order_amunt " +
                "WHERE order_amunt.create_date = DATE_FORMAT('" + time + "','%Y') " ;
        if (null != map.get("beginTime") && null != map.get("lastTime")){
            sql +=  "AND order_amunt.create_date_year >= '" + map.get("beginTime") +"' " +
                    "AND order_amunt.create_date_year <= '" + map.get("lastTime") +"' ";
        }
            sql +=  "GROUP BY create_date " ;
        List<Map<String, Object>> list = this.findBySql(sql, Map.class);
        if(!Collections3.isEmpty(list)){
            if (list.get(0).get("moneyAmount") == null){
                return "0";
            }
            return list.get(0).get("moneyAmount").toString();
        }
        return "0";
    }

    /**
     * 根据订单号和订单类型定位到预统计表的一条记录，更改其中的amount的值。规则：s.amount = s.amount + increment
     * @param increment
     * @param orderId
     * @param orderType
     */
    public void addAmountByOrderIdAndType(BigDecimal increment,String orderId,String orderType){
        /*String sql = "UPDATE order_data_statistics s SET s.amount=s.amount + ? " +
                "WHERE s.order_id=? AND s.order_type=? AND s.del_flag='0'";*/


        //改价后   订单金额不包括差额返还   修改 by mbmr 2017-3-22

        String sql="UPDATE order_data_statistics s SET s.amount=(SELECT IFNULL(if(pd.differenceFlag='1',(SELECT ma.amount+?-IFNULL(m.amount,0) FROM money_amount AS m WHERE m.serialNum=pd.differenceMoney),ma.amount+?),0)AS moneyAmount " +
                    " FROM productorder AS pd LEFT JOIN money_amount AS ma ON pd.total_money=ma.serialNum) " +
                    " WHERE s.order_id=? AND s.order_type=? AND s.del_flag='0'";


        updateBySql(sql,increment,increment,orderId,orderType);
    }

    /**
     * 根据订单orderId和订单类型orderType来获取最新的订单相关信息，用于保存和更新预统计表中的订单信息
     * @param orderId
     * @param orderType
     * @return
     */
    public Map<String,Object> getTheLatestOrderInfo(Long orderId,Integer orderType){
        // 单团类的
        /*String sql = "SELECT o.id AS orderId,o.productId,p.acitivityName AS productName,o.orderStatus AS orderType," +
            "(SELECT SUM(ma.amount*ma.exchangerate) FROM money_amount ma WHERE ma.serialNum=o.total_money) AS amount," +
            "o.total_money AS amountUuid,o.orderPersonNum,o.createDate AS orderCreateTime,o.orderCompany AS agentId," +
            "o.orderCompanyName AS agentName,o.salerId,o.salerName,o.payStatus FROM productorder o,travelactivity p " +
            "WHERE o.productId=p.id AND o.id = ? AND o.orderStatus= ?";*/

        //退团、转团后  订单金额不包括差额返还部分  修改by mbmr  2017-3-22
    String sql="SELECT temmp.orderId,temmp.productId,ta.acitivityName, temmp.orderType,temmp.moneyAmount,temmp.amountUuid,temmp.orderPersonNum, " +
            " temmp.orderCreateTime,temmp.agentId,temmp.agentName,temmp.salerId,temmp.salerName,temmp.payStatus " +
            " FROM" +
            " (SELECT o.id AS orderId,o.productId,o.orderStatus AS orderType, " +
            " IFNULL(if(o.differenceFlag='1',(SELECT ma.amount-IFNULL(m.amount,0) FROM money_amount AS m WHERE m.serialNum=o.differenceMoney),ma.amount),0)AS moneyAmount, " +
            " o.total_money AS amountUuid,o.orderPersonNum,o.createDate AS orderCreateTime,o.orderCompany AS agentId, " +
            " o.orderCompanyName AS agentName,o.salerId,o.salerName,o.payStatus  " +
            " FROM productorder AS o LEFT JOIN money_amount AS ma ON o.total_money=ma.serialNum) AS temmp,travelactivity AS ta  " +
            " WHERE temmp.productId=ta.id AND temmp.id = ? AND temmp.orderStatus= ?";



        List<Map<String,Object>> result = findBySql(sql,Map.class,orderId,orderType);
        if (result != null && result.size() != 0){
            return result.get(0);
        }
        return null;
    }




    /**
     * 根据订单id和订单类型获取预统计表的对应的一条记录，返回对应的映射对象。
     * @param orderId
     * @param orderType
     * @return
     */
    public OrderDataStatistics getEntityByOrderIdAndType(Long orderId,Integer orderType){
        String hql = "FROM OrderDataStatistics s WHERE s.orderId=? AND s.orderType=? AND s.delFlag='0'";
        List<OrderDataStatistics> entityList = find(hql,orderId,orderType);
        if (entityList != null && entityList.size() != 0){
            return entityList.get(0);
        }
        return null;
    }

    /**
     * 根据entity是否存在id，保存或者更新entity对象。
     * @param entity
     */
    public void saveOrUpdateEntity(OrderDataStatistics entity){
        if (entity.getId() == null){
            super.saveObj(entity);
        }else {
            super.updateObj(entity);
        }
    }
}
