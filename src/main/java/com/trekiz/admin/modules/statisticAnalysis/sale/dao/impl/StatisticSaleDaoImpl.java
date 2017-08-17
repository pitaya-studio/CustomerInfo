package com.trekiz.admin.modules.statisticAnalysis.sale.dao.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.AskOrderNumDao;
import com.trekiz.admin.modules.statisticAnalysis.sale.dao.StatisticSaleDao;
import com.trekiz.admin.modules.statisticAnalysis.sale.pojo.SaleAnalysisParam;
import com.trekiz.admin.modules.statisticAnalysis.sale.pojo.SaleTopParamDTO;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 数据统计分析2.0 销售查询Dao实现
 */
@Repository
public class StatisticSaleDaoImpl extends BaseDaoImpl implements StatisticSaleDao {
	
	@Autowired
	private AskOrderNumDao askOrderNumDao;

    /**
     * 在当前用户所属公司中，询单请求：查询满足 未删除状态并且拥有t1下单权限的 或者 已删除状态但是询单记录中有被询过单的 用户人数。
     * 订单请求则已删除用户需满足有询单产生有效订单的才统计。
     * @return 返回满足条件销售个数。
     * @author yudong.xu 2017.3.8
     */
    @Override
    public BigDecimal getSaleSum(SaleTopParamDTO dto) {
        Long companyId = UserUtils.getUser().getCompany().getId();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(*) AS saleSum FROM sys_user u WHERE u.companyId = ? AND (( u.delFlag = '0'")
        .append(" AND u.quauqBookOrderPermission = '1' ) OR EXISTS ( ");
        if (Context.ORDER_DATA_STATISTICS_ORDER_OVERVIEW.equals(dto.getOverView())) { // 订单查询加条件
			sql.append("SELECT 'x' FROM order_progress_tracking t,productorder o WHERE t.order_id = o.id ")
			.append("AND t.company_id = u.companyId AND t.ask_saler_id = u.id AND o.payStatus IN (3,4,5) ")
			.append("AND t.order_id IS NOT NULL AND t.order_id != '' ");
        } else {
			sql.append(" SELECT 'x' FROM order_progress_tracking t WHERE t.company_id = u.companyId ")
			.append("AND t.ask_saler_id = u.id");
		}
        sql.append(" AND t.ask_num IS NOT NULL AND t.ask_time IS NOT NULL))");
        List<Object> list = findBySql(sql.toString(), companyId);
        return new BigDecimal(list.get(0).toString());
    }

    /**
     * 查询当前用户所属公司中，满足询单请求参数dto的询单记录数。
     * @param dto 销售排名模块请求参数的数据传输对象
     * @return 返回满足条件的询单总数。
     * @author yudong.xu 2017.3.8
     */
    @Override
    public BigDecimal getAllNum4Ask(SaleTopParamDTO dto) {
        Long companyId = UserUtils.getUser().getCompany().getId();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(*) AS askNum FROM order_progress_tracking t WHERE t.company_id= ?")
		.append(" AND t.ask_num IS NOT NULL AND t.ask_time IS NOT NULL");

		buildConditionSql4Ask(dto, sql); // 根据请求参数拼接SQL

        List<Object> list = findBySql(sql.toString(), companyId);
        return new BigDecimal(list.get(0).toString());
    }

    /**
     * 订单总览中，查询该公司下的满足查询条件的订单数/收客人数/订单金额的总值。
     * @param dto
     * @return
     * @author yudong.xu 2017.3.9
     */
    @Override
    public BigDecimal getAllNum4Order(SaleTopParamDTO dto) {
        Long companyId = UserUtils.getUser().getCompany().getId();
        StringBuilder sql = new StringBuilder();

        // 分析类型：1订单数，2收客人数，3订单金额
        String analysisType = dto.getAnalysisType();
        if (analysisType == null || Context.ORDER_DATA_STATISTICS_ORDER_NUM.equals(dto.getAnalysisType())) {
            sql.append("SELECT count(*) AS num FROM order_progress_tracking t,productorder o WHERE t.order_id=o.id ")
            .append("AND t.company_id= ? AND t.order_id IS NOT NULL AND t.order_id != '' ")
			.append("AND o.payStatus IN (3, 4, 5) ");
        } else if (Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM.equals(dto.getAnalysisType())) {
            sql.append("SELECT SUM(o.orderPersonNum) AS num FROM order_progress_tracking t,productorder o ")
            .append("WHERE t.order_id=o.id AND t.company_id= ? AND t.order_id IS NOT NULL AND t.order_id != '' ")
            .append(" AND o.payStatus IN (3, 4, 5) ");
        } else { // 3订单金额
			sql.append("SELECT SUM(IFNULL((SELECT SUM( ma1.amount * ma1.exchangerate ) FROM money_amount ma1 ")
			.append("WHERE ma1.serialNum = o.total_money ), 0) - IFNULL((SELECT SUM(ma2.amount * ma2.exchangerate) ")
			.append("FROM money_amount ma2 WHERE ma2.serialNum = o.differenceMoney ), 0 )) AS num ")
			.append("FROM order_progress_tracking t, productorder o WHERE t.order_id = o.id AND t.company_id= ? ")
            .append("AND t.order_id IS NOT NULL AND t.order_id != '' AND o.payStatus IN (3, 4, 5) ");
        }
		sql.append(" AND t.ask_num IS NOT NULL AND t.ask_time IS NOT NULL ");

        buildConditionSql4Order(dto, sql); // 根据请求参数拼接SQL

        List<Object> list = findBySql(sql.toString(), companyId);
        if (list.get(0) != null) {
            return new BigDecimal(list.get(0).toString());
        }
        return BigDecimal.ZERO;
    }

    /**
     * 拼接入参参数到SQL中，针对询单。
     * @param dto
     * @param sql
     */
    private void buildConditionSql4Ask(SaleTopParamDTO dto, StringBuilder sql) {
        // 1：今日  2：昨日  3：本月  4：上月  5：本年  6：去年  7：全部
        String searchDate = dto.getSearchDate();
        if (searchDate != null) {
            switch (searchDate) {
                case Context.ORDER_DATA_STATISTICS_TODAY:
                    sql.append(" AND DATE(t.ask_time) = CURDATE() ");
                    break;
                case Context.ORDER_DATA_STATISTICS_YESTERDAY:
                    sql.append(" AND DATEDIFF(CURDATE(),t.ask_time)=1 ");
                    break;
                case Context.ORDER_DATA_STATISTICS_MONTH:
                    sql.append(" AND DATE_FORMAT(t.ask_time,'%Y%m') = DATE_FORMAT(CURDATE(),'%Y%m') ");
                    break;
                case Context.ORDER_DATA_STATISTICS_LAST_MONTH:
                    sql.append(" AND PERIOD_DIFF(DATE_FORMAT(CURDATE(),'%Y%m'),DATE_FORMAT(t.ask_time,'%Y%m')) = 1 ");
                    break;
                case Context.ORDER_DATA_STATISTICS_YEAR:
                    sql.append(" AND YEAR(t.ask_time) = YEAR(CURDATE()) ");
                    break;
                case Context.ORDER_DATA_STATISTICS_LAST_YEAR:
                    sql.append(" AND YEAR(t.ask_time) = YEAR(CURDATE())-1 ");
                    break;
                default:
                    // do nothing
            }
        }

        // 用户自定义时间范围的开始时间,结束时间
        String startDate = dto.getStartDate();
        String endDate = dto.getEndDate();
        if (StringUtils.isNotBlank(startDate)){
            sql.append(" AND t.ask_time >= '").append(startDate).append("'");
        }
        if (StringUtils.isNotBlank(endDate)){
            sql.append(" AND t.ask_time <= '").append(endDate).append(" 23:59:59'");
        }
    }

	/**
	 * 拼接入参参数到SQL中,针对订单。
	 * @param dto
	 * @param sql
	 */
	private void buildConditionSql4Order(SaleTopParamDTO dto, StringBuilder sql) {
		// 1：今日  2：昨日  3：本月  4：上月  5：本年  6：去年  7：全部
		String searchDate = dto.getSearchDate();
		if (searchDate != null) {
			switch (searchDate) {
				case Context.ORDER_DATA_STATISTICS_TODAY:
					sql.append(" AND DATE(t.order_create_time) = CURDATE() ");
					break;
				case Context.ORDER_DATA_STATISTICS_YESTERDAY:
					sql.append(" AND DATEDIFF(CURDATE(),t.order_create_time)=1 ");
					break;
				case Context.ORDER_DATA_STATISTICS_MONTH:
					sql.append(" AND DATE_FORMAT(t.order_create_time,'%Y%m') = DATE_FORMAT(CURDATE(),'%Y%m') ");
					break;
				case Context.ORDER_DATA_STATISTICS_LAST_MONTH:
					sql.append(" AND PERIOD_DIFF(DATE_FORMAT(CURDATE(),'%Y%m'),DATE_FORMAT(t.order_create_time,'%Y%m')) = 1 ");
					break;
				case Context.ORDER_DATA_STATISTICS_YEAR:
					sql.append(" AND YEAR(t.order_create_time) = YEAR(CURDATE()) ");
					break;
				case Context.ORDER_DATA_STATISTICS_LAST_YEAR:
					sql.append(" AND YEAR(t.order_create_time) = YEAR(CURDATE())-1 ");
					break;
				default:
					// do nothing
			}
		}

		// 用户自定义时间范围的开始时间,结束时间
		String startDate = dto.getStartDate();
		String endDate = dto.getEndDate();
		if (StringUtils.isNotBlank(startDate)){
			sql.append(" AND t.order_create_time >= '").append(startDate).append("'");
		}
		if (StringUtils.isNotBlank(endDate)){
			sql.append(" AND t.order_create_time <= '").append(endDate).append(" 23:59:59'");
		}
	}

    /**
     * 在用户所属公司中，根据询单请求参数查询询单记录，返回被询单数最大的前5名销售人员信息。包括：销售id，销售名称，询单数。
     * @param dto 销售排名模块请求参数的数据传输对象
     * @return 返回销售前五名的list数据
     * @author yudong.xu 2017.3.8
     */
    @Override
    public List<Map<String,Object>> getSaleTop4Ask(SaleTopParamDTO dto) {
        Long companyId = UserUtils.getUser().getCompany().getId();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT u.id AS saleId, u.`name` AS saleName, IFNULL(ask.askNum, 0) AS num FROM sys_user u ")
        .append(" LEFT JOIN ( SELECT COUNT(*) AS askNum, t.ask_saler_id AS userId FROM order_progress_tracking t")
        .append(" WHERE t.company_id = ? AND t.ask_num IS NOT NULL AND t.ask_time IS NOT NULL ");

        buildConditionSql4Ask(dto, sql); // 根据请求参数拼接SQL

        sql.append(" GROUP BY t.ask_saler_id ) ask ON u.id = ask.userId WHERE u.companyId = ?")
        .append(" AND (( u.delFlag = '0' AND u.quauqBookOrderPermission = '1' ) OR EXISTS ( ")
        .append(" SELECT 'x' FROM order_progress_tracking t WHERE t.company_id = u.companyId ")
		.append(" AND t.ask_num IS NOT NULL AND t.ask_time IS NOT NULL AND ")
        .append(" t.ask_saler_id = u.id )) ORDER BY askNum DESC, CONVERT(u.`name` USING gbk) LIMIT 0, 5");

        List<Map<String,Object>> result = findBySql(sql.toString(), Map.class, companyId, companyId);
        return result;
    }

    /**
     * 订单总览页面，查询订单数或订单收客人数或订单金额前五名的销售信息。
     * @param dto
     * @return
     * @author yudong.xu 2017.3.8
     */
    @Override
    public List<Map<String,Object>> getSaleTop4Order(SaleTopParamDTO dto) {
        Long companyId = UserUtils.getUser().getCompany().getId();
        StringBuilder sql = new StringBuilder();

        // 分析类型：1订单数，2收客人数，3订单金额
        String analysisType = dto.getAnalysisType();

        if (analysisType == null || Context.ORDER_DATA_STATISTICS_ORDER_NUM.equals(analysisType)) {
			sql.append("SELECT u.id AS saleId,u.`name` AS saleName,IFNULL(t1.num,0) AS num FROM sys_user u LEFT JOIN ")
            .append("(SELECT t.ask_saler_id,count(*) AS num FROM order_progress_tracking t, productorder o ")
            .append("WHERE t.order_id = o.id AND t.company_id = ? AND t.order_id IS NOT NULL ")
			.append("AND t.ask_num IS NOT NULL AND t.ask_time IS NOT NULL ")
            .append("AND t.order_id != '' AND o.payStatus IN (3, 4, 5) ");

            buildConditionSql4Order(dto, sql); // 根据请求参数拼接SQL

            sql.append(" GROUP BY t.ask_saler_id) t1 ON u.id=t1.ask_saler_id")
			.append(" WHERE u.companyId = ? AND (( u.delFlag = '0' AND u.quauqBookOrderPermission = '1' )")
			.append("OR EXISTS ( SELECT 'x' FROM order_progress_tracking t,productorder o WHERE ")
			.append("t.order_id = o.id AND t.company_id = u.companyId AND t.ask_saler_id = u.id AND o.payStatus IN (3,4,5) ")
			.append("AND t.ask_num IS NOT NULL AND t.ask_time IS NOT NULL ")
			.append("AND t.order_id IS NOT NULL AND t.order_id != '' )) ")
			.append("ORDER BY t1.num DESC,CONVERT(u.`name` USING gbk) LIMIT 0,5");
        } else if (Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM.equals(analysisType)) {
			sql.append("SELECT u.id AS saleId,u.`name` AS saleName,IFNULL(t1.num,0) AS num FROM sys_user u LEFT JOIN ");
            sql.append("(SELECT t.ask_saler_id,SUM(o.orderPersonNum) AS num FROM order_progress_tracking t,")
            .append("productorder o WHERE t.order_id = o.id AND t.company_id = ? AND t.order_id IS NOT NULL ")
			.append("AND t.ask_num IS NOT NULL AND t.ask_time IS NOT NULL ")
            .append("AND t.order_id != '' AND o.payStatus IN (3, 4, 5) ");

			buildConditionSql4Order(dto, sql); // 根据请求参数拼接SQL

            sql.append(" GROUP BY t.ask_saler_id) t1 ON u.id=t1.ask_saler_id")
			.append(" WHERE u.companyId = ? AND (( u.delFlag = '0' AND u.quauqBookOrderPermission = '1' )")
			.append("OR EXISTS ( SELECT 'x' FROM order_progress_tracking t,productorder o WHERE ")
			.append("t.order_id = o.id AND t.company_id = u.companyId AND t.ask_saler_id = u.id AND o.payStatus IN (3,4,5) ")
			.append("AND t.ask_num IS NOT NULL AND t.ask_time IS NOT NULL ")
			.append("AND t.order_id IS NOT NULL AND t.order_id != '' )) ")
			.append("ORDER BY t1.num DESC,CONVERT(u.`name` USING gbk) LIMIT 0,5");
        } else { // 3订单金额
			sql.append("SELECT u.id AS saleId, u.`name` AS saleName,IFNULL( SUM((t1.totalMoney - t1.diffMoney)),0)")
			.append(" AS num FROM sys_user u LEFT JOIN( SELECT t.ask_saler_id,IFNULL(( SELECT ")
			.append(" SUM(ma1.amount * ma1.exchangerate) FROM money_amount ma1 WHERE ma1.serialNum = o.total_money),0)")
			.append(" AS totalMoney,IFNULL(( SELECT SUM(ma2.amount * ma2.exchangerate) FROM money_amount ma2 WHERE ")
			.append(" ma2.serialNum = o.differenceMoney), 0) AS diffMoney FROM order_progress_tracking t, productorder o")
			.append(" WHERE t.order_id = o.id AND t.company_id = ? AND t.order_id IS NOT NULL AND t.order_id != '' ")
			.append(" AND t.ask_num IS NOT NULL AND t.ask_time IS NOT NULL AND o.payStatus IN (3, 4, 5) ");

			buildConditionSql4Order(dto, sql); // 根据请求参数拼接SQL

			sql.append(" ) t1 ON u.id=t1.ask_saler_id ")
			.append(" WHERE u.companyId = ? AND (( u.delFlag = '0' AND u.quauqBookOrderPermission = '1' )")
			.append("OR EXISTS ( SELECT 'x' FROM order_progress_tracking t,productorder o WHERE ")
			.append("t.order_id = o.id AND t.company_id = u.companyId AND t.ask_saler_id = u.id AND o.payStatus IN (3,4,5) ")
			.append("AND t.ask_num IS NOT NULL AND t.ask_time IS NOT NULL ")
			.append("AND t.order_id IS NOT NULL AND t.order_id != '' )) GROUP BY u.id ")
			.append("ORDER BY num DESC,CONVERT(u.`name` USING gbk) LIMIT 0,5");
        }

        List<Map<String,Object>> result = findBySql(sql.toString(), Map.class, companyId, companyId);
        return result;
    }

    @Override
	public  List<Map<String,Object>> getSaleDetailList(SaleAnalysisParam saleAnalysisParam) {
    	// 分页处理
    	int pageNo = 0; // 默认第一页
    	int pageSize = 10; // 每页默认显示10条
    	if (StringUtils.isNotBlank(saleAnalysisParam.getPageNo())) {
    		pageNo = Integer.parseInt(saleAnalysisParam.getPageNo()); // 当前页
    		pageNo = pageNo - 1;
    	}
    	if (StringUtils.isNotBlank(saleAnalysisParam.getPageSize())) {
    		pageSize =  Integer.parseInt(saleAnalysisParam.getPageSize()); // 每页显示条数
    	}
    	StringBuffer sql = getSaleAnalysisSqlCommon(saleAnalysisParam);
    	sql.append(" LIMIT ").append(pageNo * pageSize).append(",").append(pageSize);
    	// 销售统计详情页列表数据
        List<Map<String,Object>> rowList = null;
    	// 搜索框内容
    	if (StringUtils.isNotBlank(saleAnalysisParam.getSearchValue())) {
    		rowList = findBySql(sql.toString(), Map.class, "%" + saleAnalysisParam.getSearchValue() + "%");
    	} else {
    		rowList = findBySql(sql.toString(), Map.class);
    	}
		 
        return rowList;
    }
    
	@Override
	public String getSaleDetailListCount(SaleAnalysisParam saleAnalysisParam) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT COUNT(1) AS COUNT FROM ( ").append(getSaleAnalysisSqlCommon(saleAnalysisParam).toString()).append(") r");
		// 销售统计详情页列表数据总数
		List<Map<String,Object>> rowList = null;
		if (StringUtils.isNotBlank(saleAnalysisParam.getSearchValue())) {
			rowList = findBySql(sql.toString(), Map.class, "%" + saleAnalysisParam.getSearchValue() + "%");
		} else {
			rowList = findBySql(sql.toString(), Map.class);
		}
		
		return rowList.get(0).get("COUNT").toString();
	}
	
    /**
     * 获取销售统计公共sql(通过查询条件)
     * @author gaoyang
     * @Time 2017-3-8 下午5:09:47
     * @param
     * @return
     */
    private StringBuffer getSaleAnalysisSqlCommon (SaleAnalysisParam param) {
    	StringBuffer sql = new StringBuffer();
    	Long companyId = UserUtils.getUser().getCompany().getId(); // 公司ID
    	String searchValue = param.getSearchValue(); // 搜索销售名称
    	sql.append(" SELECT ");
    	sql.append(" 	   lastOrderDate.rankNum, "); 		// 排名
    	sql.append(" 	   lastOrderDate.analysisTypeName, "); 		// 销售名称
    	sql.append("       FORMAT(IFNULL(lastOrderDate.orderPreNum, 0), 0) AS orderPreNum, "); 	// 询单总数
    	sql.append(" 	   FORMAT(IFNULL(lastOrderDate.orderNum, 0), 0) AS orderNum, "); 		// 订单总数
    	sql.append(" 	   FORMAT(IFNULL(lastOrderDate.orderMoney, 0) ,2) AS orderMoney, "); // 订单金额总和
    	sql.append(" 	   FORMAT(IFNULL(lastOrderDate.orderPersonNum, 0), 0) AS orderPersonNum"); // 收客人数总和
    	sql.append("   FROM ( ");
    	sql.append(" 	SELECT ");
    	sql.append(" 		   @rownum \\:=@rownum+1 AS rankNum, "); // 排名
    	sql.append(" 		   newOrderDate.analysisTypeName, ");			  // 销售名称
    	sql.append(" 		   newOrderDate.orderPreNum, ");		  // 询单总数
    	sql.append(" 		   newOrderDate.orderNum, ");			  // 订单总数
    	sql.append(" 		   newOrderDate.orderMoney, ");		  // 订单金额总和
    	sql.append(" 		   newOrderDate.orderPersonNum ");		  // 收客人数总和
    	sql.append(" 	  FROM ( ");
    	// *************当前具有T1平台【实时连通渠道权限】的账号start****************
    	sql.append(" 		SELECT ");
    	sql.append(" 			  u.`name` AS analysisTypeName, ");
    	sql.append(" 			  u.id as userId, ");
    	sql.append(" 			  count(optPre.id) AS orderPreNum, ");
    	sql.append(" 			  orderData.orderNum AS orderNum, ");
    	sql.append(" 			  orderData.orderMoney AS orderMoney, ");
    	sql.append(" 			  orderData.orderPersonNum AS orderPersonNum, ");
    	sql.append(" 			  @rownum \\:=0 AS rnum ");
    	sql.append(" 		 FROM sys_user u "); // 用户表
    	sql.append(" 	LEFT JOIN order_progress_tracking optPre "); // 订单跟踪设置表
    	sql.append(" 		   ON u.id = optPre.ask_saler_id ");
    	sql.append(" 		  AND optPre.ask_num IS NOT NULL ");
    	sql.append(" 		  AND optPre.ask_time IS NOT NULL ");
    	// 追加询单时间搜索条件
    	appendDateCondition(sql, param, "optPre.ask_time");
    	sql.append(" 	LEFT JOIN ( ");
    	sql.append(" 			SELECT ");
    	sql.append(" 				   optOrder.ask_saler_id as askId, ");
    	sql.append(" 				   COUNT(optOrder.order_id) AS orderNum, ");
    	sql.append(" 				   SUM( ");
    	sql.append(" 						(SELECT IFNULL(SUM(ma.amount * ma.exchangerate), 0) "); // 订单总额
    	sql.append(" 		   			   	   FROM money_amount ma ");
    	sql.append("         			   	  WHERE ma.serialNum = odr.total_money) ");
    	sql.append(" 					 	 - ");
    	sql.append("        				(SELECT IFNULL(SUM(ma.amount * ma.exchangerate), 0) "); // 差额返还
    	sql.append("  		   			  	   FROM money_amount ma ");
    	sql.append(" 		  			  	  WHERE ma.serialNum = odr.differenceMoney) ");
    	sql.append("     			   ) AS orderMoney, ");
    	sql.append(" 				   SUM(odr.orderPersonNum) AS orderPersonNum ");
    	sql.append(" 			  FROM order_progress_tracking optOrder "); // 订单跟踪设置表
    	sql.append(" 		INNER JOIN productorder odr "); // 订单表
    	sql.append(" 			    ON optOrder.order_id = odr.id ");
    	sql.append(" 			   AND odr.delFlag = 0 ");
    	sql.append(" 			   AND odr.orderStatus = 2 "); // 现在只有散拼
    	sql.append(" 			   AND odr.payStatus IN (3, 4, 5) ");
    	sql.append(" 			 WHERE optOrder.order_id != '' ");
    	sql.append(" 			   AND optOrder.order_id IS NOT NULL ");
    	sql.append(" 		       AND optOrder.ask_num IS NOT NULL ");
    	sql.append(" 		       AND optOrder.ask_time IS NOT NULL ");
    	// 追加订单时间搜索条件
    	appendDateCondition(sql, param, "optOrder.order_create_time");
    	sql.append(" 	      GROUP BY askId ");
    	sql.append(" 			) orderData ");
    	sql.append(" 		  ON u.id = orderData.askId ");
    	sql.append(" 	   WHERE ((u.delFlag = 0 AND u.quauqBookOrderPermission = 1)");
    	sql.append(" 		  OR (EXISTS (SELECT 1 ");
    	sql.append(" 		               FROM order_progress_tracking optPreIn "); // 订单跟踪设置表
    	sql.append(" 		              WHERE u.id = optPreIn.ask_saler_id ");
    	sql.append(" 		                AND optPreIn.ask_num IS NOT NULL ");
    	sql.append(" 		                AND optPreIn.ask_time IS NOT NULL ");
    	sql.append(" 	                    AND u.companyId = '").append(companyId).append("' "); // 公司ID
    	sql.append(" 		     ))) ");
    	sql.append(" 		 AND u.companyId = '").append(companyId).append("' "); // 公司ID
    	sql.append(" 	GROUP BY userId ");
    	// *****************当前具有T1平台【实时连通渠道权限】的账号end********************
//    	sql.append(" 	   UNION ");
//    	// *************被实时连通渠道询过单的账号；（已删除的T2账号也要纳入统计）start**********
//    	sql.append(" 		SELECT ");
//    	sql.append(" 			  u.`name` AS analysisTypeName, ");
//    	sql.append(" 			  u.id as userId, ");
//    	sql.append(" 			  count(optPre.id) AS orderPreNum, ");
//    	sql.append(" 			  orderData.orderNum AS orderNum, ");
//    	sql.append(" 			  orderData.orderMoney AS orderMoney, ");
//    	sql.append(" 			  orderData.orderPersonNum AS orderPersonNum, ");
//    	sql.append(" 			  @rownum \\:=0 AS rnum ");
//    	sql.append(" 		 FROM sys_user u "); // 用户表
//    	sql.append("   INNER JOIN order_progress_tracking optPreIn "); // 订单跟踪设置表
//    	sql.append(" 		   ON u.id = optPreIn.ask_saler_id ");
//    	sql.append(" 		  AND optPreIn.ask_num IS NOT NULL ");
//    	sql.append(" 		  AND optPreIn.ask_time IS NOT NULL ");
//    	sql.append("    LEFT JOIN order_progress_tracking optPre "); // 订单跟踪设置表
//    	sql.append(" 		   ON u.id = optPre.ask_saler_id ");
//    	sql.append(" 		  AND optPre.ask_num IS NOT NULL ");
//    	sql.append(" 		  AND optPre.ask_time IS NOT NULL ");
//    	// 追加询单时间搜索条件
//    	appendDateCondition(sql, param, "optPre.ask_time");
//    	sql.append(" 	LEFT JOIN ( ");
//    	sql.append(" 			SELECT ");
//    	sql.append(" 				  optOrder.ask_saler_id as askId, ");
//    	sql.append(" 				  count(optOrder.order_id) AS orderNum, ");
//    	sql.append(" 				  sum(ma.amount * ma.exchangerate) as orderMoney, ");
//    	sql.append(" 				  sum(odr.orderPersonNum) AS orderPersonNum ");
//    	sql.append(" 			 FROM order_progress_tracking optOrder "); // 订单跟踪设置表
//    	sql.append(" 	   INNER JOIN productorder odr "); // 订单表
//    	sql.append(" 			   ON optOrder.order_id = odr.id ");
//    	sql.append(" 			  AND odr.delFlag = 0 ");
//    	sql.append(" 			  AND odr.orderStatus = 2 "); // 现在只有散拼
//    	sql.append(" 			  AND odr.payStatus IN (3, 4, 5) ");
//    	sql.append(" 		LEFT JOIN money_amount ma "); // 金额表
//    	sql.append(" 			   ON ma.serialNum = odr.total_money ");
//    	sql.append(" 		    WHERE optOrder.order_id != '' ");
//    	sql.append(" 			  AND optOrder.order_id IS NOT NULL ");
//    	sql.append(" 			  AND optOrder.orderStatus = 0 ");
//    	sql.append(" 		      AND optOrder.ask_num IS NOT NULL ");
//    	sql.append(" 		      AND optOrder.ask_time IS NOT NULL ");
//    	// 追加订单时间搜索条件
//    	appendDateCondition(sql, param, "optOrder.order_create_time");
//    	sql.append(" 	     GROUP BY askId ");
//    	sql.append(" 			) orderData ");
//    	sql.append(" 		      ON u.id = orderData.askId ");
//    	sql.append(" 	   WHERE u.companyId = '").append(companyId).append("' "); // 公司ID
//    	sql.append("     GROUP BY userId ");
    	// **************被实时连通渠道询过单的账号；(已删除的T2账号也要纳入统计)end*****************
    	sql.append(" 			) newOrderDate ");
    	// 追加各个字段排序（只有倒序）做排名用
    	appendOrderByDesc(sql, param);
    	sql.append(" 		) lastOrderDate ");
    	// 销售名称搜索
    	if (StringUtils.isNotBlank(searchValue)){
            sql.append(" WHERE lastOrderDate.analysisTypeName LIKE ? ");
        }
    	// 追加各个字段排序
    	appendOrderBy(sql, param);
    	
    	return sql;
    }
    
    /**
     * 追加时间搜索条件(1：今日-1：昨日3：本月-3：上月4：本年-4：去年 5：全部)
     * @author gaoyang
     * @Time 2017-3-9 下午6:11:39
     * @param sql
     * @param param
     * @param column 要搜索的字段名称
     */
    private void appendDateCondition(StringBuffer sql, SaleAnalysisParam param, String column) {
    	// 1：今日-1：昨日3：本月-3：上月4：本年-4：去年 5：全部
        String searchDate = param.getSearchDate(); // 搜索时间范围
        String startDate = param.getStartDate(); // 自定义开始时间
        String endDate = param.getEndDate(); // 自定义结束时间
        // 用户自定义开始时间~结束时间
        if (StringUtils.isNotBlank(startDate)) {
        	sql.append(" AND " + column + " >= '").append(startDate).append(" 00:00:00'");
        }
        
        if (StringUtils.isNotBlank(endDate)) {
        	sql.append(" AND " + column + " <= '").append(endDate).append(" 23:59:59'");
        }
        
        // 如果自定义为空则加下面搜索条件
        if (StringUtils.isNotBlank(searchDate) && StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
        	// 1：今日-1：昨日3：本月-3：上月4：本年-4：去年 5：全部
        	switch (searchDate) {
        		case Context.ORDER_DATA_STATISTICS_TODAY: // 今日
                    sql.append(" AND DATE_FORMAT(" + column + ", '%Y-%m-%d') = '")
                       .append(askOrderNumDao.getTime("yyyy-MM-dd") + "' ");
                    break;
                  case Context.ORDER_DATA_STATISTICS_YESTERDAY: // 昨日
                    sql.append(" AND DATE_FORMAT(" + column + ", '%Y-%m-%d') = '")
                       .append(askOrderNumDao.getYesterday() + "' ");
                    break;
                  case Context.ORDER_DATA_STATISTICS_MONTH: // 本月
                    sql.append(" AND DATE_FORMAT(" + column + ", '%Y-%m') = '")
                       .append(askOrderNumDao.getTime("yyyy-MM") + "' ");
                    break;
                  case Context.ORDER_DATA_STATISTICS_LAST_MONTH: // 上月
                    sql.append(" AND DATE_FORMAT(" + column + ", '%Y-%m') = '")
                       .append(askOrderNumDao.getLastMonth() + "' ");
                    break;
                  case Context.ORDER_DATA_STATISTICS_YEAR: // 今年
                    sql.append(" AND DATE_FORMAT(" + column + ", '%Y') = '")
                       .append(askOrderNumDao.getTime("yyyy") + "' ");
                    break;
                  case Context.ORDER_DATA_STATISTICS_LAST_YEAR: // 去年
                    sql.append(" AND DATE_FORMAT(" + column + ", '%Y') = '")
                       .append(askOrderNumDao.getLastYear() + "' ");
                    break;
				default:
					break;
	    	}
        }
    }
    
    /**
     * 追加排序
     * 1：订单总数降序  2：订单总数升序  3：订单金额降序  4：订单金额升序  5：收客人数降序  6：收客人数升序  7：询单数降序  8：询单数升序
     * @author gaoyang
     * @Time 2017-3-9 下午8:31:26
     * @param sql
     * @param param
     */
    private void appendOrderBy(StringBuffer sql, SaleAnalysisParam param) {
    	// 排序 1：订单总数降序  2：订单总数升序  3：订单金额降序  4：订单金额升序  5：收客人数降序  6：收客人数升序  7：询单数降序  8：询单数升序
    	String orderBy = param.getOrderBy();
    	if (StringUtils.isNotBlank(orderBy)) {
    		// 排名和数值顺序相反，订单降序，排名则为升序
    		if ("1".equals(orderBy) || "3".equals(orderBy) || "5".equals(orderBy) || "7".equals(orderBy)) {
    			sql.append(" ORDER BY rankNum ");
    		} else if ("2".equals(orderBy) || "4".equals(orderBy) || "6".equals(orderBy) || "8".equals(orderBy)) {
    			sql.append(" ORDER BY rankNum DESC ");
    		}
    	}
    }
    
    /**
     * 追加排序
     * 1：订单总数降序  2：订单总数升序  3：订单金额降序  4：订单金额升序  5：收客人数降序  6：收客人数升序  7：询单数降序  8：询单数升序
     * @author gaoyang
     * @Time 2017-3-9 下午8:31:26
     * @param sql
     * @param param
     */
    private void appendOrderByDesc(StringBuffer                                                                                                                                                                sql, SaleAnalysisParam param) {
    	// 排序 1：订单总数降序  2：订单总数升序  3：订单金额降序  4：订单金额升序  5：收客人数降序  6：收客人数升序  7：询单数降序  8：询单数升序
    	String orderBy = param.getOrderBy();
		if (orderBy != null) {
			switch (orderBy) {
				// 订单总数
				case "1": 
					sql.append(" ORDER BY orderNum DESC, CONVERT(analysisTypeName USING gbk) COLLATE gbk_chinese_ci ");
					break;
				case "2":
					sql.append(" ORDER BY orderNum DESC, CONVERT(analysisTypeName USING gbk) COLLATE gbk_chinese_ci ");
					break;
				// 订单金额
				case "3":
			        sql.append(" ORDER BY orderMoney DESC, CONVERT(analysisTypeName USING gbk) COLLATE gbk_chinese_ci ");
			        break;
				case "4":
			        sql.append(" ORDER BY orderMoney DESC, CONVERT(analysisTypeName USING gbk) COLLATE gbk_chinese_ci ");
			        break;
			    // 收客人数
				case "5":
			        sql.append(" ORDER BY orderPersonNum DESC, CONVERT(analysisTypeName USING gbk) COLLATE gbk_chinese_ci ");
			        break;
				case "6":
					sql.append(" ORDER BY orderPersonNum DESC, CONVERT(analysisTypeName USING gbk) COLLATE gbk_chinese_ci ");
		            break;
		        // 询单数
				case "7":
					sql.append(" ORDER BY orderPreNum DESC, CONVERT(analysisTypeName USING gbk) COLLATE gbk_chinese_ci ");
		            break;
				case "8":
					sql.append(" ORDER BY orderPreNum DESC, CONVERT(analysisTypeName USING gbk) COLLATE gbk_chinese_ci ");
		            break;
				default:
					break;
			}
		}
    }
}