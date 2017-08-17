package com.trekiz.admin.modules.statisticAnalysis.product.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.statisticAnalysis.product.dao.IProductAnalysisDao;
import com.trekiz.admin.modules.statisticAnalysis.product.service.IProductAnalysisService;
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
public class IProductAnalysisServiceImpl implements IProductAnalysisService {

    @Autowired
    private IProductAnalysisDao iProductAnalysisDao;



    private final static String ORDER_DATA_STATISTICS_default="0";//数据分析类型默认为null
    private final static String orderBy_orderNum_DESC ="1";//订单数降序
    private final static String OrderBy_orderNum_ASC = "2";//订单数升序
    private final static String OrderBy_personNum_DESC = "3";//收客人数降序
    private final static String OrderBy_personNum_ASC = "4";//收客人数升序
    private final static String OrderBy_orderMoney_DESC = "5";//收款金额降序
    private final static String OrderBy_orderMoney_ASC = "6";//收款金额升序

    private final static String orderType_all="0";//全部产品

    public Map<String, Object> getSummaryData(HttpServletRequest request, HttpServletResponse response) {
        StringBuffer sumSql = new StringBuffer("select IFNULL(SUM(sumTemp.orderNum),0) AS orderNum, IFNULL(SUM(sumTemp.orderPersonNum),0) AS orderPersonNum,FORMAT(IFNULL(SUM(REPLACE(sumTemp.orderMoney,',','')),0.0), 2) AS orderMoney FROM (");
        StringBuffer sql = new StringBuffer("Select temp.id,temp.productName,temp.orderNum,temp.orderPersonNum,temp.orderMoney,'￥' AS currencyMark  from ");
        SqlCondition(sql,request);
        sumSql.append(sql).append(") as sumTemp");
        Map<String, Object> map = iProductAnalysisDao.findSummaryDataBySql(sumSql.toString());
        return map;
    }

    public Page<Map<String, Object>> getOrderDataStatisticsList(Page<Map<String, Object>> page, HttpServletRequest request, HttpServletResponse response) {

        StringBuffer sql = new StringBuffer("Select temp.id,temp.productName,temp.orderNum,temp.orderPersonNum,temp.orderMoney,'￥' AS currencyMark  from ");
        String pageNo ="";
        String pageSize="";
        JSONObject jsonObject =null;
        String params = request.getParameter("param");
        if(StringUtils.isNotBlank(params)){
           jsonObject = JSONObject.parseObject(params);
            pageNo =jsonObject.getString("pageNo");//当前页
            pageSize = jsonObject.getString("pageSize");//每页显示条数
        }




        SqlCondition(sql,request);

        if(StringUtils.isNotBlank(pageNo)){
            page.setPageNo(Integer.parseInt(pageNo));
        }
        if(StringUtils.isNotBlank(pageSize)){
            page.setPageSize(Integer.parseInt(pageSize));
        }

        Page<Map<String, Object>> pageList = iProductAnalysisDao.findPageListBySql(page, sql.toString());
        if (pageList != null) {
            return pageList;
        }
        return null;
    }


    private void SqlCondition(StringBuffer sql,HttpServletRequest request) {

        String params = request.getParameter("param");

        String searchValue = "";//搜索内容
        String orderNumBegin = "";//起始订单数
        String orderNumEnd = "";//截止订单数
        String personNumBegin = "";//起始收客人数
        String personNumEnd ="";//截止收客人数
        String orderMoneyBegin = "";//起始订单金额
        String orderMoneyEnd ="";//截止订单金额
        String orderType = "";//产品类型（类似：0:全部 1：单团 2：散拼  3：游学 4：大客户 5：自由行 6：签证 7：机票 10：游轮）
        String orderBy = "";//排序规则(1：订单数倒序   2：订单数正序  3：收客人数倒序 4：收客人数正序 5订单金额倒序 6：订单金额正序)

        String analysisType = "";//分析类型(1：订单数，2：收客人数，3：订单金额)
        String searchDate ="";//时间(1：今日 2：本周 3：本月 4：本年 5：全部)
        String startDate = "";//自定义起始日期
        String endDate ="";//自定义截止日期
        if(StringUtils.isNotBlank(params)){
            JSONObject jsonObject = JSONObject.parseObject(params);
            searchValue = jsonObject.getString("searchValue");//搜索内容
            orderNumBegin = jsonObject.getString("orderNumBegin");//起始订单数
            orderNumEnd = jsonObject.getString("orderNumEnd");//截止订单数
            personNumBegin = jsonObject.getString("orderPersonNumBegin");//起始收客人数
            personNumEnd = jsonObject.getString("orderPersonNumEnd");//截止收客人数
            orderMoneyBegin = jsonObject.getString("orderMoneyBegin");//起始订单金额
            orderMoneyEnd = jsonObject.getString("orderMoneyEnd");//截止订单金额
            orderType = jsonObject.getString("orderType");//产品类型（类似：0:全部 1：单团 2：散拼  3：游学 4：大客户 5：自由行 6：签证 7：机票 10：游轮）
            orderBy = jsonObject.getString("orderBy");//排序规则(1：订单数倒序   2：订单数正序  3：收客人数倒序 4：收客人数正序 5订单金额倒序 6：订单金额正序)

            analysisType = jsonObject.getString("analysisType");//分析类型(1：订单数，2：收客人数，3：订单金额)
            searchDate = jsonObject.getString("searchDate");//时间(1：今日 2：本周 3：本月 4：本年 5：全部)
            startDate = jsonObject.getString("startDate");//自定义起始日期
            endDate = jsonObject.getString("endDate");//自定义截止日期
        }

        if (StringUtils.isNotBlank(orderType)) {
            if(orderType.equals(orderType_all)){
                   sql.append("( SELECT ta.id,IFNULL(odss.product_name,ta.acitivityName) AS productName," +
                        "IFNULL(odss.orderNum,0) AS orderNum,IFNULL(odss.orderPersonNum,0) AS orderPersonNum," +
                        "IFNULL(odss.orderMoney,0.00) AS orderMoney," +
                        "IFNULL(odss.order_createtime,0) AS order_createtime," +
                        "ta.proCompany," +
                        "IFNULL(odss.order_type,ta.activity_kind) AS order_type" +
                        " from travelactivity ta LEFT JOIN " +
                        "(SELECT ods.product_id,ods.product_name,COUNT(*) AS orderNum,SUM(order_person_num) AS orderPersonNum," +
                        "FORMAT(IFNULL(SUM(amount),0),2) AS orderMoney,ods.order_createtime,ods.order_type" +
                        " FROM order_data_statistics AS ods  where ods.order_type!='6' and ods.order_type!='7'" +
                         " and ods.order_status!='99' and ods.order_status!='111' "+
                        " GROUP BY  " );
                        /*" UNION ALL " +
                        "( SELECT aa.id,IFNULL(odss.product_name,concat('机票',aa.product_code)) AS productName," +
                        "IFNULL(odss.orderNum,0) AS orderNum,IFNULL(odss.orderPersonNum,0) AS orderPersonNum," +
                        "IFNULL(odss.orderMoney,0.0) AS orderMoney," +
                        "IFNULL(odss.order_createtime,aa.createDate) AS order_createtime," +
                        "aa.proCompany," +
                        "IFNULL(odss.order_type,7) AS order_type" +
                        " from activity_airticket aa LEFT JOIN " +
                        "(SELECT ods.product_id,ods.product_name,COUNT(*) AS orderNum,SUM(order_person_num) AS orderPersonNum, " +
                        "FORMAT(IFNULL(SUM(amount),0),2) AS orderMoney,ods.order_createtime,ods.order_type " +
                        "FROM order_data_statistics AS ods where ods.order_type='7' GROUP BY ods.product_id " +
                        ") AS odss ON odss.product_id=aa.id  ) " +
                        */
                SearchDate_ta(sql, searchDate, startDate, endDate);
                sql.append("WHERE ta.delFlag=0");
                sql.append(" UNION ALL " +
                        "( SELECT vs.id,IFNULL(odss.product_name,vs.productName) AS productName," +
                        "IFNULL(odss.orderNum,0) AS orderNum,IFNULL(odss.orderPersonNum,0) AS orderPersonNum, " +
                        "IFNULL(odss.orderMoney,0.00) AS orderMoney," +
                        "IFNULL(odss.order_createtime,0) AS order_createtime," +
                        "vs.proCompanyId AS proCompany," +
                        "IFNULL(odss.order_type,6) AS order_type " +
                        " from visa_products vs LEFT JOIN  " +
                        "( SELECT ods.product_id,ods.product_name,COUNT(*) AS orderNum,SUM(order_person_num) AS orderPersonNum, " +
                        "FORMAT(IFNULL(SUM(amount),0),2) AS orderMoney,ods.order_createtime,ods.order_type" +
                        " FROM order_data_statistics AS ods where ods.order_type='6' and ods.order_status!='99' and ods.order_status!='2'" +
                        " GROUP BY " );

                SearchDate_vs(sql, searchDate, startDate, endDate);
                sql.append("WHERE vs.delFlag=0");
                sql.append(" )");
            }else if(orderType.equals(Context.ORDER_STATUS_VISA)){
                sql.append(" ( SELECT vs.id,IFNULL(odss.product_name,vs.productName) AS productName," +
                        "IFNULL(odss.orderNum,0) AS orderNum,IFNULL(odss.orderPersonNum,0) AS orderPersonNum, " +
                        "IFNULL(odss.orderMoney,0.00) AS orderMoney," +
                        "IFNULL(odss.order_createtime,0) AS order_createtime," +
                        "vs.proCompanyId AS proCompany," +
                        "IFNULL(odss.order_type,"+orderType+") AS order_type" +
                        " from visa_products vs LEFT JOIN  " +
                        "(SELECT ods.product_id,ods.product_name,COUNT(*) AS orderNum," +
                        "SUM(order_person_num) AS orderPersonNum, " +
                        "FORMAT(IFNULL(SUM(amount),0),2) AS orderMoney," +
                        "ods.order_createtime,ods.order_type FROM order_data_statistics AS ods" );
                sql.append(" where ods.order_type ='" + orderType + "' and ods.order_status!='99' and ods.order_status!='2' ");
                sql.append( " GROUP BY  ");

                SearchDate_vs(sql, searchDate, startDate, endDate);
                sql.append("WHERE vs.delFlag=0");
            }/*else if(orderType.equals(Context.ORDER_STATUS_AIR_TICKET)){
                sql.append(" ( SELECT aa.id,IFNULL(odss.product_name,concat('机票',aa.product_code)) AS productName," +
                        "IFNULL(odss.orderNum,0) AS orderNum,IFNULL(odss.orderPersonNum,0) AS orderPersonNum, " +
                        "IFNULL(odss.orderMoney,0.0) AS orderMoney," +
                        "IFNULL(odss.order_createtime,aa.createDate) AS order_createtime," +
                        "aa.proCompany," +
                        "IFNULL(odss.order_type,"+orderType+") AS order_type" +
                        " from activity_airticket aa LEFT JOIN  " +
                        "( SELECT ods.product_id,ods.product_name,COUNT(*) AS orderNum,SUM(order_person_num) AS orderPersonNum,\n" +
                        "FORMAT(IFNULL(SUM(amount),0),2) AS orderMoney,ods.order_createtime,ods.order_type " +
                        " FROM order_data_statistics AS ods ");
                sql.append(" where ods.order_type ='" + orderType + "' ");
                sql.append(" GROUP BY ods.product_id ) AS odss ON odss.product_id=aa.id");
            }*/else{
                sql.append(" ( SELECT ta.id,IFNULL(odss.product_name,ta.acitivityName) AS productName," +
                        "IFNULL(odss.orderNum,0) AS orderNum,IFNULL(odss.orderPersonNum,0) AS orderPersonNum, " +
                        "IFNULL(odss.orderMoney,0.00) AS orderMoney," +
                        "IFNULL(odss.order_createtime,0) AS order_createtime," +
                        "ta.proCompany," +
                        "IFNULL(odss.order_type,ta.activity_kind) AS order_type " +
                        " from travelactivity ta LEFT JOIN  " +
                        "( SELECT ods.product_id,ods.product_name,COUNT(*) AS orderNum,SUM(order_person_num) AS orderPersonNum,\n" +
                        "FORMAT(IFNULL(SUM(amount),0),2) AS orderMoney,ods.order_createtime,ods.order_type " +
                        " FROM order_data_statistics AS ods ");
                sql.append(" where ods.order_type ='" + orderType + "' and ods.order_status!='99' and ods.order_status!='111' ");
                sql.append(" GROUP BY ");
                SearchDate_ta(sql, searchDate, startDate, endDate);
                sql.append("WHERE ta.delFlag=0");
            }

        }



        if (StringUtils.isNotBlank(orderType)) {
            if(orderType.equals(orderType_all)){
                sql.append(" ) AS temp");
            }else if(orderType.equals(Context.ORDER_STATUS_VISA)){
                sql.append(" ) AS temp ");
            }else if(orderType.equals(Context.ORDER_STATUS_AIR_TICKET)){
                sql.append(" ) AS temp ");
            }else{
                sql.append(" ) AS temp ");
            }

        }else{
            sql.append(" ) AS temp ");
        }

        sql.append(" where 1=1 ");


            if (StringUtils.isNotBlank(searchValue)) {
                sql.append(" and "+" temp.productName LIKE '%" + searchValue + "%'");
            }
            if (StringUtils.isNotBlank(orderNumBegin)) {
                   sql.append(" and "+"temp.orderNum >='"+ orderNumBegin +"' ");

            }
        if(StringUtils.isNotBlank(orderNumEnd)){
            sql.append(" and "+"temp.orderNum <= '" + orderNumEnd+"' ");
        }
            if (StringUtils.isNotBlank(personNumBegin)) {
                   sql.append(" and "+" temp.orderPersonNum >= '" + personNumBegin + "' ");

            }
        if (StringUtils.isNotBlank(personNumEnd)) {
            sql.append(" and "+" temp.orderPersonNum <='" + personNumEnd+"' ");

        }
            if (StringUtils.isNotBlank(orderMoneyBegin)) {
                    sql.append(" and "+" temp.orderMoney >='" + orderMoneyBegin + "' " );

            }

        if (StringUtils.isNotBlank(orderMoneyEnd)) {
            sql.append(" and "+" temp.orderMoney <= '" + orderMoneyEnd+"' " );

        }




        sql.append(" and temp.proCompany = '").append(UserUtils.getUser().getCompany().getId()).append("' ");


        if(orderType.equals(orderType_all)){
            sql.append("");
        }else if(orderType.equals(Context.ORDER_STATUS_VISA)){
            sql.append("");
        }else {
            sql.append(" AND temp.order_type='"+ orderType +"' ");
        }

        if (StringUtils.isNotBlank(analysisType)) {
           if(analysisType.equals(ORDER_DATA_STATISTICS_default)){
               if (StringUtils.isNotBlank(orderBy)) {
                   if (orderBy.equals(orderBy_orderNum_DESC)) {
                       sql.append(" ORDER BY temp.orderNum DESC ");
                   } else if (orderBy.equals(OrderBy_orderNum_ASC)) {
                       sql.append(" ORDER BY temp.orderNum ASC ");
                   } else if (orderBy.equals(OrderBy_personNum_DESC)) {
                       sql.append(" ORDER BY temp.orderPersonNum DESC ");
                   } else if (orderBy.equals(OrderBy_personNum_ASC)) {
                       sql.append(" ORDER BY temp.orderPersonNum ASC ");
                   } else if (orderBy.equals(OrderBy_orderMoney_DESC)) {
                       sql.append(" ORDER BY REPLACE(temp.orderMoney,',','')+0 DESC ");
                   } else if (orderBy.equals(OrderBy_orderMoney_ASC)) {
                       sql.append(" ORDER BY REPLACE(temp.orderMoney,',','')+0 ASC ");
                   }
               } else {
                   sql.append(" ORDER BY temp.orderPersonNum DESC ");
               }
            }else if (analysisType.equals(Context.ORDER_DATA_STATISTICS_ORDER_NUM)) {
               if (StringUtils.isNotBlank(orderBy)) {
                   if (orderBy.equals(orderBy_orderNum_DESC)) {
                       sql.append(" ORDER BY temp.orderNum DESC ");
                   } else if (orderBy.equals(OrderBy_orderNum_ASC)) {
                       sql.append(" ORDER BY temp.orderNum ASC ");
                   } else if (orderBy.equals(OrderBy_personNum_DESC)) {
                       sql.append(" ORDER BY temp.orderPersonNum DESC ");
                   } else if (orderBy.equals(OrderBy_personNum_ASC)) {
                       sql.append(" ORDER BY temp.orderPersonNum ASC ");
                   } else if (orderBy.equals(OrderBy_orderMoney_DESC)) {
                       sql.append(" ORDER BY REPLACE(temp.orderMoney,',','')+0 DESC ");
                   } else if (orderBy.equals(OrderBy_orderMoney_ASC)) {
                       sql.append(" ORDER BY REPLACE(temp.orderMoney,',','')+0 ASC ");
                   }
               } else {
                   sql.append(" ORDER BY temp.orderNum DESC ");
               }

            } else if (analysisType.equals(Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM)) {
               if (StringUtils.isNotBlank(orderBy)) {
                   if (orderBy.equals(orderBy_orderNum_DESC)) {
                       sql.append(" ORDER BY temp.orderNum DESC ");
                   } else if (orderBy.equals(OrderBy_orderNum_ASC)) {
                       sql.append(" ORDER BY temp.orderNum ASC ");
                   } else if (orderBy.equals(OrderBy_personNum_DESC)) {
                       sql.append(" ORDER BY temp.orderPersonNum DESC ");
                   } else if (orderBy.equals(OrderBy_personNum_ASC)) {
                       sql.append(" ORDER BY temp.orderPersonNum ASC ");
                   } else if (orderBy.equals(OrderBy_orderMoney_DESC)) {
                       sql.append(" ORDER BY REPLACE(temp.orderMoney,',','')+0 DESC ");
                   } else if (orderBy.equals(OrderBy_orderMoney_ASC)) {
                       sql.append(" ORDER BY REPLACE(temp.orderMoney,',','')+0 ASC ");
                   }
               } else {
                   sql.append(" ORDER BY temp.orderPersonNum DESC ");
               }

            } else if (analysisType.equals(Context.ORDER_DATA_STATISTICS_ORDER_MONEY)) {
               if (StringUtils.isNotBlank(orderBy)) {
                   if (orderBy.equals(orderBy_orderNum_DESC)) {
                       sql.append(" ORDER BY temp.orderNum DESC ");
                   } else if (orderBy.equals(OrderBy_orderNum_ASC)) {
                       sql.append(" ORDER BY temp.orderNum ASC ");
                   } else if (orderBy.equals(OrderBy_personNum_DESC)) {
                       sql.append(" ORDER BY temp.orderPersonNum DESC ");
                   } else if (orderBy.equals(OrderBy_personNum_ASC)) {
                       sql.append(" ORDER BY temp.orderPersonNum ASC ");
                   } else if (orderBy.equals(OrderBy_orderMoney_DESC)) {
                       sql.append(" ORDER BY REPLACE(temp.orderMoney,',','')+0 DESC ");
                   } else if (orderBy.equals(OrderBy_orderMoney_ASC)) {
                       sql.append(" ORDER BY REPLACE(temp.orderMoney,',','')+0 ASC ");
                   }
               } else {
                   sql.append(" ORDER BY REPLACE(temp.orderMoney,',','')+0 DESC ");
               }

            }else{

               if (StringUtils.isNotBlank(orderBy)) {
                   if (orderBy.equals(orderBy_orderNum_DESC)) {
                       sql.append(" ORDER BY temp.orderNum DESC ");
                   } else if (orderBy.equals(OrderBy_orderNum_ASC)) {
                       sql.append(" ORDER BY temp.orderNum ASC ");
                   } else if (orderBy.equals(OrderBy_personNum_DESC)) {
                       sql.append(" ORDER BY temp.orderPersonNum DESC ");
                   } else if (orderBy.equals(OrderBy_personNum_ASC)) {
                       sql.append(" ORDER BY temp.orderPersonNum ASC ");
                   } else if (orderBy.equals(OrderBy_orderMoney_DESC)) {
                       sql.append(" ORDER BY REPLACE(temp.orderMoney,',','')+0 DESC ");
                   } else if (orderBy.equals(OrderBy_orderMoney_ASC)) {
                       sql.append(" ORDER BY REPLACE(temp.orderMoney,',','')+0 ASC ");
                   }
               } else {
                   sql.append(" ORDER BY temp.orderPersonNum DESC ");
               }

            }
        }
    }

    private void SearchDate_ta(StringBuffer sql, String searchDate, String startDate, String endDate) {
        if (StringUtils.isNotBlank(searchDate)) {
            if (searchDate.equals(Context.ORDER_DATA_STATISTICS_TODAY)) {
                sql.append(" TO_DAYS(ods.order_createtime), ods.product_id ) AS odss ON odss.product_id=ta.id " +
                        " and TO_DAYS(odss.order_createtime) = TO_DAYS(now()) ");
            } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_WEEK)) {
                sql.append("  YEARWEEK(ods.order_createtime,1), ods.product_id ) AS odss ON odss.product_id=ta.id " +
                        " and YEARWEEK(odss.order_createtime,1)=YEARWEEK(NOW()) ");
            } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_MONTH)) {
                sql.append("  DATE_FORMAT(ods.order_createtime,'%Y%m'), ods.product_id ) AS odss ON odss.product_id=ta.id " +
                        " and DATE_FORMAT(odss.order_createtime,'%Y%m') = DATE_FORMAT(CURDATE(),'%Y%m') ");
            }else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_YEAR)) {
                sql.append("  DATE_FORMAT(ods.order_createtime,'%Y'), ods.product_id ) AS odss ON odss.product_id=ta.id " +
                        " and DATE_FORMAT(odss.order_createtime,'%Y') = DATE_FORMAT(CURDATE(),'%Y') ");
            }else if(searchDate.equals(Context.ORDER_DATA_STATISTICS_ALL)){
                sql.append(" ods.product_id ) AS odss ON odss.product_id=ta.id ");
            }
        }

        if(StringUtils.isNotBlank(startDate)&&StringUtils.isNotBlank(endDate)){
            sql.append(" UNIX_TIMESTAMP(DATE_FORMAT(ods.order_createtime,'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "') and UNIX_TIMESTAMP(DATE_FORMAT(ods.order_createtime,'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "'),ods.product_id ) AS odss ON ta.id=odss.product_id" +
                    " and UNIX_TIMESTAMP(DATE_FORMAT(odss.order_createtime,'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "') and UNIX_TIMESTAMP(DATE_FORMAT(odss.order_createtime,'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "') ");

        }else {
            if (StringUtils.isNotBlank(startDate)) {
                sql.append(" UNIX_TIMESTAMP(DATE_FORMAT(ods.order_createtime,'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "'),ods.product_id ) AS odss ON ta.id=odss.product_id" +
                        " and UNIX_TIMESTAMP(DATE_FORMAT(odss.order_createtime,'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "')  ");

            }
            if (StringUtils.isNotBlank(endDate)) {
                sql.append(" UNIX_TIMESTAMP(DATE_FORMAT(ods.order_createtime,'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "'),ods.product_id ) AS odss ON ta.id=odss.product_id" +
                        " and UNIX_TIMESTAMP(DATE_FORMAT(odss.order_createtime,'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "') ");
            }
        }
    }

    private void SearchDate_vs(StringBuffer sql, String searchDate, String startDate, String endDate) {
        if (StringUtils.isNotBlank(searchDate)) {
            if (searchDate.equals(Context.ORDER_DATA_STATISTICS_TODAY)) {
                sql.append(" TO_DAYS(ods.order_createtime),ods.product_id ) AS odss ON vs.id=odss.product_id" +
                        " and TO_DAYS(odss.order_createtime) = TO_DAYS(now()) ");
            } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_WEEK)) {
                sql.append(" YEARWEEK(ods.order_createtime,1),ods.product_id ) AS odss ON vs.id=odss.product_id" +
                        " and YEARWEEK(odss.order_createtime,1)=YEARWEEK(NOW()) ");
            } else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_MONTH)) {
                sql.append(" DATE_FORMAT(ods.order_createtime,'%Y%m'),ods.product_id ) AS odss ON vs.id=odss.product_id" +
                        " and DATE_FORMAT(odss.order_createtime,'%Y%m') = DATE_FORMAT(CURDATE(),'%Y%m') ");
            }else if (searchDate.equals(Context.ORDER_DATA_STATISTICS_YEAR)) {
                sql.append(" DATE_FORMAT(ods.order_createtime,'%Y'),ods.product_id ) AS odss ON vs.id=odss.product_id" +
                        " and DATE_FORMAT(odss.order_createtime,'%Y') = DATE_FORMAT(CURDATE(),'%Y') ");
            }else if(searchDate.equals(Context.ORDER_DATA_STATISTICS_ALL)){
                sql.append(" ods.product_id ) AS odss ON vs.id=odss.product_id ");
            }
        }
        if(StringUtils.isNotBlank(startDate)&&StringUtils.isNotBlank(endDate)){
            sql.append(" UNIX_TIMESTAMP(DATE_FORMAT(ods.order_createtime,'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "') and UNIX_TIMESTAMP(DATE_FORMAT(ods.order_createtime,'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "'),ods.product_id ) AS odss ON vs.id=odss.product_id" +
                    " and UNIX_TIMESTAMP(DATE_FORMAT(odss.order_createtime,'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "') and UNIX_TIMESTAMP(DATE_FORMAT(odss.order_createtime,'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "') ");

        }else {
            if (StringUtils.isNotBlank(startDate)) {
                sql.append(" UNIX_TIMESTAMP(DATE_FORMAT(ods.order_createtime,'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "'),ods.product_id ) AS odss ON vs.id=odss.product_id" +
                        " and UNIX_TIMESTAMP(DATE_FORMAT(odss.order_createtime,'%Y-%m-%d'))>=UNIX_TIMESTAMP('" + startDate + "')  ");

            }
            if (StringUtils.isNotBlank(endDate)) {
                sql.append(" UNIX_TIMESTAMP(DATE_FORMAT(ods.order_createtime,'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "'),ods.product_id ) AS odss ON vs.id=odss.product_id" +
                        " and UNIX_TIMESTAMP(DATE_FORMAT(odss.order_createtime,'%Y-%m-%d'))<=UNIX_TIMESTAMP('" + endDate + "') ");
            }
        }
    }
}
