package com.trekiz.admin.modules.statisticAnalysis.product.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.statisticAnalysis.product.dao.IProductAnalysisDaoTwo;
import com.trekiz.admin.modules.statisticAnalysis.product.service.IProductAnalysisServiceTwo;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * 产品分析业务实现类
 *
 * @author mbmr
 * @date 2016-12-22
 */
@Service
public class IProductAnalysisServiceTwoImpl implements IProductAnalysisServiceTwo {


    @Autowired
    private IProductAnalysisDaoTwo iProductAnalysisDaoTwo;

    private final static String orderBy_orderNum_DESC = "1";//订单数降序
    private final static String OrderBy_orderNum_ASC = "2";//订单数升序
    private final static String OrderBy_personNum_DESC = "5";//收客人数降序
    private final static String OrderBy_personNum_ASC = "6";//收客人数升序
    private final static String OrderBy_orderMoney_DESC = "3";//收款金额降序
    private final static String OrderBy_orderMoney_ASC = "4";//收款金额升序
    private final static String OrderBy_inqueryMoney_DESC = "7";//询单数降序
    private final static String OrderBy_inqueryMoney_ASC = "8";//询单数升序

    private static final String ORDER_DATA_STATISTICS_LASTYEAR = "-4";//去年
    private static final String ORDER_DATA_STATISTICS_YESTERDAY = "-1";//昨日
    private static final String ORDER_DATA_STATISTICS_LASTMONTH = "-3";//上月


    public Page<Map<String, Object>> getOrderDataStatisticsList(Page<Map<String, Object>> page, HttpServletRequest request, HttpServletResponse response) {

       /* StringBuffer sql = new StringBuffer("Select orderAndInquery.rankNum, orderAndInquery.analysisTypeName,orderAndInquery.orderNum,orderAndInquery.orderPersonNum,FORMAT(IFNULL(orderAndInquery.orderMoney,0.00),2) AS orderMoney,orderAndInquery.orderPreNum from " +
                "(Select (@rownum\\:=@rownum+1) AS rankNum,");*/
        
       StringBuffer sql=new StringBuffer("SELECT  orderAndInquery.rankNum,orderAndInquery.analysisTypeName,IFNULL(orderAndInquery.orderNum,0) AS orderNum," +
               " IFNULL(orderAndInquery.orderPersonNum,0) AS orderPersonNum,\n" +
               " FORMAT(IFNULL(orderAndInquery.orderMoney,0.00),2) AS orderMoney,  IFNULL(orderAndInquery.orderPreNum,0) AS orderPreNum  \n" +
               " FROM (SELECT (@rownum\\:=@rownum+1) AS rankNum,ordersss.analysisTypeName,ordersss.orderNum,ordersss.orderPersonNum,ordersss.orderMoney,ordersss.orderPreNum \n" +
               " FROM (SELECT @rownum\\:=0, orderss.analysisTypeName,orderss.orderNum,orderss.orderPersonNum,orderss.orderMoney,orderss.orderPreNum \n" +
               " FROM (SELECT trat.id,trat.acitivityName AS analysisTypeName,IFNULL(orderNum,0) AS orderNum,IFNULL(orderPersonNum,0) AS orderPersonNum,\n" +
               " IFNULL(moneyAmount,0.0) AS orderMoney,\n" +
               " IFNULL(orderPreNum,0) AS orderPreNum,trat.proCompany \n" +
               " FROM ( ");
        SqlCondition(sql, request);

        Page<Map<String, Object>> pageList = iProductAnalysisDaoTwo.findPageListBySql(page, sql.toString());
        if (pageList != null) {
            return pageList;
        }
        return null;
    }


    private void SqlCondition(StringBuffer sql, HttpServletRequest request) {
        String params = request.getParameter("param");
        String searchValue = "";//搜索内容
        // String overView = "";//dd：订单总览  xd：询单总览
        //String orderType = "";//产品类型（类似：0:全部 1：单团 2：散拼  3：游学 4：大客户 5：自由行 6：签证 7：机票 10：游轮）
        String orderBy = "";//排序规则(1：订单数倒序   2：订单数正序  3订单金额倒序 4：订单金额正序 5：收客人数倒序 6：收客人数正序  7：询单数降序  8：询单数升序)

        String analysisType = "";//分析类型(1：订单数，2：收客人数，3：订单金额)
        String searchDate = "";//时间 1：今日-1：昨日3：本月 -3：上月4：本年-4：去年 5：全部
        String startDate = "";//自定义起始日期
        String endDate = "";//自定义截止日期

        if (StringUtils.isNotBlank(params)) {
            JSONObject jsonObject = JSONObject.parseObject(params);
            searchValue = jsonObject.getString("searchValue");//搜索内容

            //orderType = jsonObject.getString("orderType");
            orderBy = jsonObject.getString("orderBy");
            analysisType = jsonObject.getString("analysisType");
            searchDate = jsonObject.getString("searchDate");
            startDate = jsonObject.getString("startDate");
            endDate = jsonObject.getString("endDate");
        }

        StringBuffer strSql=new StringBuffer("SELECT pp.id,pp.acitivityName,pp.proCompany  \n" +
                "            FROM travelactivity AS pp LEFT JOIN sys_office so ON so.id = pp.proCompany\n" +
                " WHERE  pp.activity_kind = 2\n" +
                " AND so.shelfRightsStatus = 0\n");
                strSql.append(" AND pp.proCompany = '").append(UserUtils.getUser().getCompany().getId()).append("' ");
                strSql.append(" AND pp.id IN ( SELECT DISTINCT\n" +
                " g.srcActivityId\n" +
                " FROM\n" +
                " activitygroup g\n" +
                " LEFT JOIN travelactivity p ON g.srcActivityId = p.id\n" +
                " WHERE (g.pricingStrategyStatus IN(1,2) or g.is_t1=1) \n ");
            strSql.append(" AND p.proCompany = '").append(UserUtils.getUser().getCompany().getId()).append("' )");

        sql.append(strSql).append(" )AS trat ").append(" LEFT JOIN ");

        StringBuffer inquerySql=new StringBuffer("(SELECT ta.id,COUNT(*) AS orderPreNum, opt.ask_time\n" +
                "   FROM ( ");

        StringBuffer orderSql=new StringBuffer("(SELECT tat.id,COUNT(*) AS orderNum,SUM(IFNULL(pd.orderPersonNum,0)) AS orderPersonNum,\n" +
                " SUM((SELECT IFNULL(SUM(ma.amount*ma.exchangerate),0) FROM money_amount ma WHERE ma.serialNum = pd.total_money)-(SELECT IFNULL(SUM(ma.amount*ma.exchangerate),0) FROM money_amount ma WHERE ma.serialNum = pd.differenceMoney)) AS moneyAmount,\n" +
                " optt.order_create_time\n" +
                " FROM ( ");


        inquerySql.append(strSql);
        inquerySql.append(" ) AS ta\n" +
                " LEFT JOIN order_progress_tracking AS opt ON ta.id=opt.activity_id   WHERE opt.ask_time IS NOT NULL AND opt.ask_num IS NOT NULL \n" +
                " GROUP BY ta.id ");

        orderSql.append(strSql);
        orderSql.append(" )AS tat \n" +
        "  LEFT JOIN order_progress_tracking AS optt ON tat.id=optt.activity_id \n" +
        " LEFT JOIN productorder AS pd ON optt.order_id=pd.id \n" +
        " WHERE optt.ask_time IS NOT NULL AND optt.ask_num IS NOT NULL AND pd.payStatus IN (3,4,5) \n" +
        " GROUP BY tat.id ");

        SearchDate(inquerySql, searchDate, "opt.ask_time", startDate, endDate);
        SearchDate(orderSql, searchDate, "optt.order_create_time", startDate, endDate);


        sql.append(inquerySql);
        sql.append(" ) AS tempp ON tempp.id = trat.id  LEFT JOIN  ");
        sql.append(orderSql);
        sql.append(" )AS temmp  ON temmp.id = trat.id  ) AS orderss  WHERE 1=1 ");

        if (StringUtils.isNotBlank(orderBy)) {
            if (orderBy.equals(orderBy_orderNum_DESC)) {
                sql.append(" ORDER BY orderss.orderNum DESC," +
                        "CONVERT( orderss.analysisTypeName USING gbk ) COLLATE gbk_chinese_ci ");
            } else if (orderBy.equals(OrderBy_orderNum_ASC)) {
                sql.append(" ORDER BY orderss.orderNum DESC, " +
                        " CONVERT( orderss.analysisTypeName USING gbk ) COLLATE gbk_chinese_ci ");
            } else if (orderBy.equals(OrderBy_personNum_DESC)) {
                sql.append(" ORDER BY orderss.orderPersonNum DESC," +
                        "CONVERT( orderss.analysisTypeName USING gbk ) COLLATE gbk_chinese_ci ");
            } else if (orderBy.equals(OrderBy_personNum_ASC)) {
                sql.append(" ORDER BY orderss.orderPersonNum DESC," +
                        "CONVERT( orderss.analysisTypeName USING gbk ) COLLATE gbk_chinese_ci ");
            } else if (orderBy.equals(OrderBy_orderMoney_DESC)) {
                sql.append(" ORDER BY orderss.orderMoney DESC," +
                        "CONVERT( orderss.analysisTypeName USING gbk ) COLLATE gbk_chinese_ci ");
            } else if (orderBy.equals(OrderBy_orderMoney_ASC)) {
                sql.append(" ORDER BY orderss.orderMoney DESC," +
                        "CONVERT( orderss.analysisTypeName USING gbk ) COLLATE gbk_chinese_ci ");
            }else if(orderBy.equals(OrderBy_inqueryMoney_DESC)){
                sql.append(" ORDER BY orderss.orderPreNum DESC," +
                        "CONVERT( orderss.analysisTypeName USING gbk ) COLLATE gbk_chinese_ci ");
            }else if(orderBy.equals(OrderBy_inqueryMoney_ASC)){
                sql.append(" ORDER BY orderss.orderPreNum DESC," +
                        "CONVERT( orderss.analysisTypeName USING gbk ) COLLATE gbk_chinese_ci ");
            }
        } else {
            sql.append(" ORDER BY orderss.inqueryNum DESC," +
                    "CONVERT( orderss.analysisTypeName USING gbk ) COLLATE gbk_chinese_ci ");
        }

        sql.append(" )AS ordersss ) AS orderAndInquery");
        if (StringUtils.isNotBlank(searchValue)) {
            sql.append(" where " + " orderAndInquery.analysisTypeName LIKE '%" + searchValue + "%'");
        }

        if (StringUtils.isNotBlank(orderBy)) {
            if (orderBy.equals(orderBy_orderNum_DESC)) {
                sql.append(" ORDER BY orderAndInquery.rankNum ASC ");
            } else if (orderBy.equals(OrderBy_orderNum_ASC)) {
                sql.append(" ORDER BY orderAndInquery.rankNum DESC ");
            } else if (orderBy.equals(OrderBy_personNum_DESC)) {
                sql.append(" ORDER BY orderAndInquery.rankNum ASC ");
            } else if (orderBy.equals(OrderBy_personNum_ASC)) {
                sql.append(" ORDER BY orderAndInquery.rankNum DESC ");
            } else if (orderBy.equals(OrderBy_orderMoney_DESC)) {
                sql.append(" ORDER BY orderAndInquery.rankNum ASC ");
            } else if (orderBy.equals(OrderBy_orderMoney_ASC)) {
                sql.append(" ORDER BY orderAndInquery.rankNum DESC ");
            }else if(orderBy.equals(OrderBy_inqueryMoney_DESC)){
                sql.append(" ORDER BY orderAndInquery.rankNum ASC");
            }else if(orderBy.equals(OrderBy_inqueryMoney_ASC)){
                sql.append(" ORDER BY orderAndInquery.rankNum DESC ");
            }
        } else {
            sql.append(" ORDER BY orderAndInquery.orderPreNum DESC ");
        }
    }

    private void sqlAppend(StringBuffer sql, String searchDate, String startDate, String endDate) {


        if (StringUtils.isNotBlank(searchDate)) {
            if (searchDate.equals(Context.ORDER_DATA_STATISTICS_TODAY)) {
                sql.append("IF(tem.date_time=TO_DAYS(now()),tem.orderNum,0) AS orderNum, " +
                        "  IF(tem.date_time=TO_DAYS(now()),tem.orderPersonNum,0)AS orderPersonNum, " +
                        "  IF(tem.date_time=TO_DAYS(now()),tem.orderMoney,0.0) AS orderMoney, " +
                        "   IF(tem.date_time=TO_DAYS(now()),tem.orderPreNum,0.0) AS orderPreNum ");

            } else if (searchDate.equals(ORDER_DATA_STATISTICS_YESTERDAY)) {
                sql.append("IF(TO_DAYS(NOW())-tem.date_time<=1,tem.orderNum,0) AS orderNum, " +
                        "  IF(TO_DAYS(NOW())-tem.date_time<=1,tem.orderPersonNum,0)AS orderPersonNum, " +
                        "  IF(TO_DAYS(NOW())-tem.date_time<=1,tem.orderMoney,0.0) AS orderMoney, " +
                        "   IF(TO_DAYS(NOW())-tem.date_time<=1,tem.orderPreNum,0.0) AS orderPreNum ");
            } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_MONTH)) {
                sql.append("IF(tem.date_time=DATE_FORMAT(CURDATE(),'%Y%m'),tem.orderNum,0) AS orderNum, " +
                        "  IF(tem.date_time=DATE_FORMAT(CURDATE(),'%Y%m'),tem.orderPersonNum,0)AS orderPersonNum, " +
                        "  IF(tem.date_time=DATE_FORMAT(CURDATE(),'%Y%m'),tem.orderMoney,0.0) AS orderMoney, " +
                        "   IF(tem.date_time=DATE_FORMAT(CURDATE(),'%Y%m'),tem.orderPreNum,0.0) AS orderPreNum ");
            } else if (searchDate.equals(ORDER_DATA_STATISTICS_LASTMONTH)) {
                sql.append("IF(PERIOD_DIFF(date_format( now(),'%Y%m'),tem.date_time) =1,tem.orderNum,0) AS orderNum, " +
                        "  IF(PERIOD_DIFF(date_format( now(),'%Y%m'),tem.date_time) =1,tem.orderPersonNum,0)AS orderPersonNum, " +
                        "  IF(PERIOD_DIFF(date_format( now(),'%Y%m'),tem.date_time) =1,tem.orderMoney,0.0) AS orderMoney, " +
                        "  IF(PERIOD_DIFF(date_format( now(),'%Y%m'),tem.date_time) =1,tem.orderPreNum,0.0) AS orderPreNum ");
            } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_YEAR)) {
                sql.append("IF(tem.date_time=DATE_FORMAT(CURDATE(),'%Y'),tem.orderNum,0) AS orderNum, " +
                        "  IF(tem.date_time=DATE_FORMAT(CURDATE(),'%Y'),tem.orderPersonNum,0)AS orderPersonNum, " +
                        "  IF(tem.date_time=DATE_FORMAT(CURDATE(),'%Y'),tem.orderMoney,0.0) AS orderMoney, " +
                        "   IF(tem.date_time=DATE_FORMAT(CURDATE(),'%Y'),tem.orderPreNum,0.0) AS orderPreNum ");
            } else if (searchDate.equals(ORDER_DATA_STATISTICS_LASTYEAR)) {

                sql.append("IF(year(tem.date_time)=year(date_sub(now(),interval 1 year)),tem.orderNum,0) AS orderNum, " +
                        "  IF(year(tem.date_time)=year(date_sub(now(),interval 1 year)),tem.orderPersonNum,0)AS orderPersonNum, " +
                        "  IF(year(tem.date_time)=year(date_sub(now(),interval 1 year)),tem.orderMoney,0.0) AS orderMoney, " +
                        "   IF(year(tem.date_time)=year(date_sub(now(),interval 1 year)),tem.orderPreNum,0.0) AS orderPreNum ");
            } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_ALL)) {
                sql.append("IFNULL(tem.orderNum,0) AS orderNum, " +
                        "  IFNULL(tem.orderPersonNum,0)AS orderPersonNum, " +
                        "  IFNULL(tem.orderMoney,0.0) AS orderMoney, " +
                        "  IFNULL(tem.orderPreNum,0.0) AS orderPreNum ");

            }
        }

        if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
            sql.append("IF(UNIX_TIMESTAMP(DATE_FORMAT(tem.date_time,'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "') " +
                    "and UNIX_TIMESTAMP(DATE_FORMAT(tem.date_time,'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "')," +
                    "tem.orderNum,0) AS orderNum, " +
                    "  IF(UNIX_TIMESTAMP(DATE_FORMAT(tem.date_time,'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "') " +
                    "and UNIX_TIMESTAMP(DATE_FORMAT(tem.date_time,'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "')," +
                    "tem.orderPersonNum,0)AS orderPersonNum, " +
                    "  IF(UNIX_TIMESTAMP(DATE_FORMAT(tem.date_time,'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "') " +
                    "and UNIX_TIMESTAMP(DATE_FORMAT(tem.date_time,'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "')," +
                    "tem.orderMoney,0.0) AS orderMoney, " +
                    "   IF(UNIX_TIMESTAMP(DATE_FORMAT(tem.ask_time,'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "') " +
                    "and UNIX_TIMESTAMP(DATE_FORMAT(tem.ask_time,'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "')," +
                    "tem.orderPreNum,0.0) AS orderPreNum ");

        } else {
            if (StringUtils.isNotBlank(startDate)) {
                sql.append("IF(UNIX_TIMESTAMP(DATE_FORMAT(tem.date_time,'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "'),tem.orderNum,0) AS orderNum, " +
                        "  IF(UNIX_TIMESTAMP(DATE_FORMAT(tem.date_time,'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "'),tem.orderPersonNum,0)AS orderPersonNum, " +
                        "  IF(UNIX_TIMESTAMP(DATE_FORMAT(tem.date_time,'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "'),tem.orderMoney,0.0) AS orderMoney, " +
                        "   IF(UNIX_TIMESTAMP(DATE_FORMAT(tem.ask_time,'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "'),tem.orderPreNum,0.0) AS orderPreNum ");

            }
            if (StringUtils.isNotBlank(endDate)) {
                sql.append("IF(UNIX_TIMESTAMP(DATE_FORMAT(tem.date_time,'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "'),tem.orderNum,0) AS orderNum, " +
                        "  IF(UNIX_TIMESTAMP(DATE_FORMAT(tem.date_time,'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "'),tem.orderPersonNum,0)AS orderPersonNum, " +
                        "  IF(UNIX_TIMESTAMP(DATE_FORMAT(tem.date_time,'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "'),tem.orderMoney,0.0) AS orderMoney, " +
                        "   IF(UNIX_TIMESTAMP(DATE_FORMAT(tem.ask_time,'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "'),tem.orderPreNum,0.0) AS orderPreNum ");

            }
        }
    }

    private void dateTime(StringBuffer sql, String searchDate, String tableTime, String startDate, String endDate) {
        if (StringUtils.isNotBlank(searchDate)) {
            if (searchDate.equals(Context.ORDER_DATA_STATISTICS_TODAY)) {
                sql.append(" , TO_DAYS(" + tableTime + ") AS date_time from ");
            } else if (searchDate.equals(ORDER_DATA_STATISTICS_YESTERDAY)) {
                sql.append(" , TO_DAYS(" + tableTime + ") AS date_time from ");
            } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_MONTH)) {
                sql.append(" ,DATE_FORMAT(" + tableTime + ",'%Y%m') AS date_time from ");
            } else if (searchDate.equals(ORDER_DATA_STATISTICS_LASTMONTH)) {
                sql.append(" , DATE_FORMAT(" + tableTime + ",'%Y%m') AS date_time from ");
            } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_YEAR)) {
                sql.append(" , DATE_FORMAT(" + tableTime + ",'%Y') AS date_time from ");
            } else if (searchDate.equals(ORDER_DATA_STATISTICS_LASTYEAR)) {
                sql.append(" , year("+tableTime+") AS date_time from ");
            } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_ALL)) {
                sql.append(" from ");
            }
        }

        if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
            sql.append("  ,inquerys.ask_time AS ask_time,orders.order_create_time AS date_time FROM ");

        } else {
            if (StringUtils.isNotBlank(startDate)) {
                sql.append("  ,inquerys.ask_time AS ask_time,orders.order_create_time AS date_time FROM ");
            }
            if (StringUtils.isNotBlank(endDate)) {
                sql.append("  ,inquerys.ask_time AS ask_time,orders.order_create_time AS date_time FROM ");
            }
        }
    }

    private void Search_Date(StringBuffer sql, String searchDate,String orderTime, String inqueryTime, String startDate, String endDate) {
        if (StringUtils.isNotBlank(searchDate)) {
            if (searchDate.equals(Context.ORDER_DATA_STATISTICS_TODAY)) {
                sql.append(" and TO_DAYS(" + orderTime + ")= TO_DAYS(\" + inqueryTime + \")");
            } else if (searchDate.equals(ORDER_DATA_STATISTICS_YESTERDAY)) {
                sql.append(" and TO_DAYS( "+orderTime+")=TO_DAYS( \"+inqueryTime+\") ");
            } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_MONTH)) {
                sql.append("  and DATE_FORMAT(" + orderTime + ",'%Y%m')=DATE_FORMAT(" + inqueryTime + ",'%Y%m') ");
            } else if (searchDate.equals(ORDER_DATA_STATISTICS_LASTMONTH)) {
                sql.append(" and DATE_FORMAT(" + orderTime + ",'%Y%m')= DATE_FORMAT(\" + inqueryTime + \",'%Y%m')  ");
            } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_YEAR)) {
                sql.append(" and DATE_FORMAT(" + orderTime + ",'%Y')=DATE_FORMAT(\" + inqueryTime + \",'%Y') ");
            } else if (searchDate.equals(ORDER_DATA_STATISTICS_LASTYEAR)) {
                sql.append(" and DATE_FORMAT(\" + orderTime + \",'%Y')=DATE_FORMAT(\" + inqueryTime + \",'%Y') ");
            } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_ALL)) {

            }
        }

        if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
            sql.append(" and UNIX_TIMESTAMP(DATE_FORMAT(" + orderTime + ",'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "') and UNIX_TIMESTAMP(DATE_FORMAT(" + orderTime + ",'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "') ");
            sql.append(" and UNIX_TIMESTAMP(DATE_FORMAT(" + inqueryTime + ",'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "') and UNIX_TIMESTAMP(DATE_FORMAT(" + inqueryTime + ",'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "') ");

        } else {
            if (StringUtils.isNotBlank(startDate)) {
                sql.append(" and UNIX_TIMESTAMP(DATE_FORMAT(" + orderTime + ",'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "')  ");
                sql.append(" and UNIX_TIMESTAMP(DATE_FORMAT(" + inqueryTime + ",'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "')  ");

            }
            if (StringUtils.isNotBlank(endDate)) {
                sql.append("  and UNIX_TIMESTAMP(DATE_FORMAT(" + orderTime + ",'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "') ");
                sql.append("  and UNIX_TIMESTAMP(DATE_FORMAT(" + inqueryTime + ",'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "') ");
            }
        }
    }


    private void SearchDate(StringBuffer sql, String searchDate, String tableTime, String startDate, String endDate) {
        if (StringUtils.isNotBlank(searchDate)) {
            if (searchDate.equals(Context.ORDER_DATA_STATISTICS_TODAY)) {
                sql.append(" ,TO_DAYS(" + tableTime + ") HAVING TO_DAYS(" + tableTime + ") = TO_DAYS(now()) ");
            } else if (searchDate.equals(ORDER_DATA_STATISTICS_YESTERDAY)) {
                sql.append(" ,TO_DAYS( "+tableTime+") HAVING (TO_DAYS(NOW()) - TO_DAYS( "+tableTime+") = 1)");
            } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_MONTH)) {
                sql.append(" , DATE_FORMAT(" + tableTime + ",'%Y%m') HAVING DATE_FORMAT(" + tableTime + ",'%Y%m') = DATE_FORMAT(CURDATE(),'%Y%m') ");
            } else if (searchDate.equals(ORDER_DATA_STATISTICS_LASTMONTH)) {
                sql.append(" ,DATE_FORMAT(" + tableTime + ",'%Y%m') HAVING PERIOD_DIFF(date_format( now(),'%Y%m'),date_format("+tableTime+",'%Y%m')) =1 ");
            } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_YEAR)) {
                sql.append(" ,DATE_FORMAT(" + tableTime + ",'%Y') HAVING DATE_FORMAT(" + tableTime + ",'%Y') = DATE_FORMAT(CURDATE(),'%Y') ");
            } else if (searchDate.equals(ORDER_DATA_STATISTICS_LASTYEAR)) {
                sql.append(" ,DATE_FORMAT(\" + tableTime + \",'%Y') HAVING year("+tableTime+")=year(date_sub(now(),interval 1 year)) ");
            } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_ALL)) {

            }
        }

        if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
            sql.append(" ,UNIX_TIMESTAMP(DATE_FORMAT(" + tableTime + ",'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "') " +
                    "and UNIX_TIMESTAMP(DATE_FORMAT(" + tableTime + ",'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "') " +
                    " HAVING UNIX_TIMESTAMP(DATE_FORMAT(" + tableTime + ",'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "') " +
                    " and UNIX_TIMESTAMP(DATE_FORMAT(" + tableTime + ",'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "')");

        } else {
            if (StringUtils.isNotBlank(startDate)) {
                sql.append(" , UNIX_TIMESTAMP(DATE_FORMAT(" + tableTime + ",'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "')" +
                        " HAVING UNIX_TIMESTAMP(DATE_FORMAT(" + tableTime + ",'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "')");

            }
            if (StringUtils.isNotBlank(endDate)) {
                sql.append(" , UNIX_TIMESTAMP(DATE_FORMAT(" + tableTime + ",'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "') " +
                        " HAVING UNIX_TIMESTAMP(DATE_FORMAT(" + tableTime + ",'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "')");
            }
        }
    }

    /*private void SearchDate(StringBuffer sql, String searchDate, String tableTime, String startDate, String endDate) {
        if (StringUtils.isNotBlank(searchDate)) {
            if (searchDate.equals(Context.ORDER_DATA_STATISTICS_TODAY)) {
                sql.append(" and TO_DAYS(" + tableTime + ") = TO_DAYS(now()) ");
            } else if (searchDate.equals(ORDER_DATA_STATISTICS_YESTERDAY)) {
                sql.append(" and (TO_DAYS(NOW()) - TO_DAYS( "+tableTime+") <= 1) ");
            } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_MONTH)) {
                sql.append(" and DATE_FORMAT(" + tableTime + ",'%Y%m') = DATE_FORMAT(CURDATE(),'%Y%m') ");
            } else if (searchDate.equals(ORDER_DATA_STATISTICS_LASTMONTH)) {
                sql.append(" and PERIOD_DIFF(date_format( now(),'%Y%m'),date_format("+tableTime+",'%Y%m')) =1  ");
            } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_YEAR)) {
                sql.append(" and DATE_FORMAT(" + tableTime + ",'%Y') = DATE_FORMAT(CURDATE(),'%Y') ");
            } else if (searchDate.equals(ORDER_DATA_STATISTICS_LASTYEAR)) {
                sql.append(" and year("+tableTime+")=year(date_sub(now(),interval 1 year)) ");
            } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_ALL)) {

            }
        }

        if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
            sql.append(" and UNIX_TIMESTAMP(DATE_FORMAT(" + tableTime + ",'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "') and UNIX_TIMESTAMP(DATE_FORMAT(" + tableTime + ",'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "') ");

        } else {
            if (StringUtils.isNotBlank(startDate)) {
                sql.append(" and UNIX_TIMESTAMP(DATE_FORMAT(" + tableTime + ",'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "')  ");

            }
            if (StringUtils.isNotBlank(endDate)) {
                sql.append("  and UNIX_TIMESTAMP(DATE_FORMAT(" + tableTime + ",'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "') ");
            }
        }
    }*/

}
