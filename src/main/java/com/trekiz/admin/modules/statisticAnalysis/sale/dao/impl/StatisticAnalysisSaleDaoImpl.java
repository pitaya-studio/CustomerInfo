package com.trekiz.admin.modules.statisticAnalysis.sale.dao.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.statisticAnalysis.sale.dao.StatisticAnalysisSaleDao;
import com.trekiz.admin.modules.statisticAnalysis.sale.pojo.SaleParamBean;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 统计分析模块的销售方面的Dao。
 */
@Repository
public class StatisticAnalysisSaleDaoImpl extends BaseDaoImpl implements StatisticAnalysisSaleDao {

    /**
     * 获取指定领域的前多少名的销售。
     * @param paramBean 前端请求参数的封装
     * @param saleIds 给定销售的id
     * @param topNum 指定前topNum名的销售
     * @author yudong.xu 2016.12.22
     */
    public List<Map<String,Object>> getSaleTop(SaleParamBean paramBean,List<Integer> saleIds, Integer topNum){
        String sqlName = getSqlNameByType(paramBean.getAnalysisType());
        String companyUuid = UserUtils.getUser().getCompany().getUuid();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT u.id AS saleId, u.`name` AS saleName,IFNULL(temp.saleNum, 0) AS saleNum ")
           .append("FROM sys_user u LEFT JOIN ( SELECT s.saler_id,").append(sqlName).append("AS saleNum ")
           .append("FROM order_data_statistics s WHERE s.company_uuid='").append(companyUuid).append("'")
           .append(" AND s.saler_id IN ( ");
        for (Integer userId : saleIds) { // 已经确保saleIds不为null和size不等于0
            sql.append(userId).append(",");
        }
        sql.deleteCharAt(sql.length() - 1); // 去除最后一个逗号
        sql.append(")");

        buildingWhereSQL(sql,paramBean); // 拼接条件sql

        sql.append(" GROUP BY s.saler_id ORDER BY saleNum DESC LIMIT ?) temp ON temp.saler_id = u.id WHERE u.id IN (");
        for (Integer userId : saleIds) {
            sql.append(userId).append(",");
        }
        sql.deleteCharAt(sql.length() - 1); // 去除最后一个逗号
        sql.append(") ORDER BY saleNum DESC, CONVERT(u.`name` USING gbk) LIMIT ?");

        return findBySql(sql.toString(),Map.class,topNum,topNum);
    }

    /**
     * 获取满足请求参数条件下的单一方面总数的统计，即订单总数，收客总数，订单总金额当中的其一。
     * @param paramBean
     * @param saleIds
     * @author yudong.xu 2016.12.22
     */
    public BigDecimal getSingleTotalInfo(SaleParamBean paramBean,List<Integer> saleIds){
        String sqlName = getSqlNameByType(paramBean.getAnalysisType());
        String companyUuid = UserUtils.getUser().getCompany().getUuid();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(sqlName).append("AS total FROM order_data_statistics s")
           .append(" WHERE s.company_uuid='").append(companyUuid).append("'")
        .append(" AND s.saler_id IN ( ");
        for (Integer userId : saleIds) { // 已经确保saleIds不为null和size不等于0
            sql.append(userId).append(",");
        }
        sql.deleteCharAt(sql.length() - 1); // 删除最后一个逗号
        sql.append(")");
        buildingWhereSQL(sql,paramBean); // 拼接条件sql

        List<Object> totalInfo = findBySql(sql.toString());
        if (totalInfo != null && totalInfo.get(0) != null){
            return new BigDecimal(totalInfo.get(0).toString());
        }
        return BigDecimal.ZERO;
    }

    /**
     * 跳转到销售分析详情页面的sql查询语句。先查询了该请求参数下的统计信息。然后是查询列表信息。
     * 因为需要用到请求参数拼装下的sql语句，所以统计信息也在该方法中进行了查询。
     * @param paramBean
     * @author yudong.xu 2016.12.23
     */
    public Map<String,Object> getSaleStatisticInfo(SaleParamBean paramBean){
        String companyUuid = UserUtils.getUser().getCompany().getUuid();
        String saleName = paramBean.getSearchValue();
        Integer orderNumBegin = paramBean.getOrderNumBegin();
        Integer orderNumEnd = paramBean.getOrderNumEnd();
        Integer personNumBegin = paramBean.getOrderPersonNumBegin();
        Integer personNumEnd = paramBean.getOrderPersonNumEnd();
        BigDecimal orderMoneyBegin = paramBean.getOrderMoneyBegin();
        BigDecimal orderMoneyEnd = paramBean.getOrderMoneyEnd();
        Integer orderBy = paramBean.getOrderBy();
        Integer pageNo = paramBean.getPageNo();
        Integer pageSize = paramBean.getPageSize();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT u.`name` AS saleName, IFNULL(tmp.orderNum, 0) AS orderNum,IFNULL(tmp.orderPersonNum, 0)")
        .append(" AS orderPersonNum, '￥' AS currencyMark,IFNULL(tmp.orderMoney, 0) AS orderMoneyTemp, ")
        .append("FORMAT(IFNULL(tmp.orderMoney, 0), 2) AS orderMoney FROM sys_user u LEFT JOIN (")
        .append("SELECT s.saler_id, COUNT(*) AS orderNum,SUM(s.order_person_num) AS orderPersonNum, ")
        .append("SUM(s.amount) AS orderMoney FROM order_data_statistics s WHERE s.company_uuid ='")
        .append(companyUuid).append("'");

        buildingWhereSQL(sql,paramBean);

        sql.append(" GROUP BY s.saler_id) tmp ON u.id = tmp.saler_id WHERE u.id IN (");
        sql.append("SELECT DISTINCT m.user_id FROM sys_user_dept_job_new m, sys_job_new j WHERE ")
        .append("m.company_uuid = j.company_uuid AND m.job_id = j.id AND j.`name` IN ('销售','销售主管') ")
         .append(" AND m.company_uuid ='").append(companyUuid).append("')");

        if (StringUtils.isNotBlank(saleName)){
            sql.append(" AND u.`name` LIKE '%").append(saleName).append("%'");
        }

        /*说明：因为使用该公司下的销售作为主表进行左连接（left join），如果右表没有，使用 0 表示NULL值。所以如果请求参数中
        有0值，则需要使用 IS NULL来过滤。而且begin和end只要有一个为0，则需要两边都得加IS NULL。*/
        if (orderNumBegin != null){
            if (orderNumBegin == 0 || (orderNumEnd != null && orderNumEnd == 0)){
                sql.append(" AND (orderNum >= ").append(orderNumBegin).append(" OR orderNum IS NULL)");
            }else {
                sql.append(" AND orderNum >= ").append(orderNumBegin);
            }
        }
        if (orderNumEnd != null){
            if (orderNumEnd == 0 || (orderNumBegin != null && orderNumBegin == 0)){
                sql.append(" AND (orderNum <= ").append(orderNumEnd).append(" OR orderNum IS NULL)");
            }else {
                sql.append(" AND orderNum <= ").append(orderNumEnd);
            }
        }

        if (personNumBegin != null){
            if (personNumBegin == 0 || (personNumEnd != null && personNumEnd == 0)){
                sql.append(" AND (orderPersonNum >= ").append(personNumBegin).append(" OR orderPersonNum IS NULL)");
            }else {
                sql.append(" AND orderPersonNum >= ").append(personNumBegin);
            }
        }
        if (personNumEnd != null){
            if (personNumEnd == 0 || (personNumBegin != null && personNumBegin == 0)){
                sql.append(" AND (orderPersonNum <= ").append(personNumEnd).append(" OR orderPersonNum IS NULL)");
            }else {
                sql.append(" AND orderPersonNum <= ").append(personNumEnd);
            }
        }

        if (orderMoneyBegin != null){
            if (orderMoneyBegin.compareTo(BigDecimal.ZERO) == 0 ||
                    (orderMoneyEnd != null && orderMoneyEnd.compareTo(BigDecimal.ZERO) == 0)){
                sql.append(" AND (orderMoney >= ").append(orderMoneyBegin).append(" OR orderMoney IS NULL)");
            }else {
                sql.append(" AND orderMoney >= ").append(orderMoneyBegin);
            }
        }
        if (orderMoneyEnd != null){
            if (orderMoneyEnd.compareTo(BigDecimal.ZERO) == 0 ||
                    (orderMoneyBegin != null && orderMoneyBegin.compareTo(BigDecimal.ZERO) == 0) ){
                sql.append(" AND (orderMoney <= ").append(orderMoneyEnd).append(" OR orderMoney IS NULL)");
            }else {
                sql.append(" AND orderMoney <= ").append(orderMoneyEnd);
            }
        }

        // 查询该请求参数下的汇总信息。满足条件的查询结果记录数，订单总数，收客总人数，订单总金额。
        StringBuilder totalSql = new StringBuilder();
        totalSql.append("SELECT count(*) AS count,IFNULL(SUM(orderNum),0) AS orderTotalNum,")
                .append("IFNULL(SUM(orderPersonNum),0) AS orderTotalPersonNum, ")
                .append("CONCAT('￥',FORMAT(IFNULL(SUM(orderMoneyTemp),0),2)) AS orderTotalMoney FROM ( ")
                .append(sql).append(" ) temp");
        List<Map<String,Object>> totalList = findBySql(totalSql.toString(),Map.class);

        // 1：订单数倒序   2：订单数正序  3：收客人数倒序 4：收客人数正序 5订单金额倒序 6：订单金额正序
        if (orderBy != null){
            switch (orderBy){
                case 1:
                    sql.append(" ORDER BY orderNum DESC ");
                    break;
                case 2:
                    sql.append(" ORDER BY orderNum ");
                    break;
                case 3:
                    sql.append(" ORDER BY orderPersonNum DESC ");
                    break;
                case 4:
                    sql.append(" ORDER BY orderPersonNum ");
                    break;
                case 5:
                    sql.append(" ORDER BY orderMoneyTemp DESC ");
                    break;
                case 6:
                    sql.append(" ORDER BY orderMoneyTemp ");
                    break;
                default:
                    // do nothing ...
            }
        }
        pageNo = pageNo == null ? 0 : pageNo - 1;
        pageSize = pageSize == null ? 10 : pageSize;
        sql.append(" LIMIT ").append(pageNo*pageSize).append(",").append(pageSize);

        List<Map<String,Object>> rowList = findBySql(sql.toString(),Map.class); // 列表数据

        // 拼装返回数据
        Map<String,Object> resultMap = totalList.get(0); // 该map中包含统计数据
        resultMap.put("list",rowList); // 添加列表数据
        resultMap.put("pageNo",pageNo);
        resultMap.put("pageSize",pageSize);
        resultMap.put("orderBy",orderBy);

        return resultMap;
    }

    /**
     * 根据参数拼接where后的条件sql
     * @param sql
     * @param paramBean
     */
    private void buildingWhereSQL(StringBuilder sql,SaleParamBean paramBean){
        Integer orderType = paramBean.getOrderType();
        String searchDate = paramBean.getSearchDate();
        String startDate = paramBean.getStartDate();
        String endDate = paramBean.getEndDate();

        sql.append(" AND s.del_flag = '0'");
        // 订单类型，和系统原先定义的一致
        if (orderType != null && orderType != 0){
            sql.append(" AND s.order_type = ").append(orderType);
        }

        sql.append(" AND CASE WHEN s.order_type = 6 THEN s.order_status !=").append(Context.VISA_ORDER_PAYSTATUS_CANCEL)
            .append(" ELSE s.order_status NOT IN(").append(Context.ORDER_PAYSTATUS_YQX).append(",")
            .append(Context.ORDER_PAYSTATUS_DEL).append(") END ");

        // 分析的时间范围，1今日，2本周，3本月，4.本年，5.全部
        if (searchDate == null){
            // do nothing ...
        }else if (Context.ORDER_DATA_STATISTICS_TODAY.equals(searchDate)){
            sql.append(" AND DATE(s.order_createtime) = CURDATE() ");
        }else if (Context.ORDER_DATA_STATISTICS_WEEK.equals(searchDate)){
        	sql.append("AND s.order_createtime>=(select subdate(curdate(),date_format(curdate(),'%w')-1))").append(" ");
			sql.append(" AND s.order_createtime<=(select subdate(curdate(),date_format(curdate(),'%w')-7))").append(" ");
        }else if (Context.ORDER_DATA_STATISTICS_MONTH.equals(searchDate)){
            sql.append(" AND DATE_FORMAT(s.order_createtime,'%Y%m') = DATE_FORMAT(CURDATE(),'%Y%m') ");
        }else if (Context.ORDER_DATA_STATISTICS_YEAR.equals(searchDate)){
            sql.append(" AND YEAR(s.order_createtime) = YEAR(CURDATE()) ");
        }

        // 用户自定义时间范围的开始时间,结束时间
        if (StringUtils.isNotBlank(startDate)){
            sql.append(" AND s.order_createtime >= '").append(startDate).append("'");
        }
        if (StringUtils.isNotBlank(endDate)){
            sql.append(" AND s.order_createtime <= '").append(endDate).append(" 23:59:59'");
        }
    }

    /**
     * 根据分析类型来获取对应的组成sql的字符串。
     * @param type 分析类型，1订单数，2收客人数，3订单金额
     * @author yudong.xu 2016.12.22
     */
    private String getSqlNameByType(Integer type){
        if (type == null){
            return " COUNT(*) ";
        }else if (type == 1){ // 订单数
            return " COUNT(*) ";
        } else if (type == 2){ // 收客人数
            return " SUM(s.order_person_num) ";
        } else if (type == 3){ // 订单金额
            return " SUM(s.amount) ";
        }else {
            return " COUNT(*) "; // 默认订单数
        }
    }






}
