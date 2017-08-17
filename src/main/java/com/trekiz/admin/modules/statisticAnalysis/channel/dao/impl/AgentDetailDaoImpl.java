package com.trekiz.admin.modules.statisticAnalysis.channel.dao.impl;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.order.entity.OrderProgressTracking;
import com.trekiz.admin.modules.order.repository.OrderProgressTrackingDao;
import com.trekiz.admin.modules.statisticAnalysis.channel.dao.AgentDetailDao;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.AskOrderNumDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Repository
public class AgentDetailDaoImpl extends BaseDaoImpl implements AgentDetailDao {

	@Autowired
	private AskOrderNumDao askOrderNumDao;
	@Autowired
	private OrderProgressTrackingDao orderProgressTrackingDao;
	
	/**
	 * 渠道详情页
	 * @author yang.wang
	 * @date 2017.3.9
	 * */
	@Override
	public Page<Map<String, Object>> getAgentDetailList(
			Page<Map<String, Object>> page, Map<String, Object> map,
			String orderBy) {
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		String searchValue = (String) map.get("searchValue");	// 搜索值
		String searchDate = (String) map.get("searchDate");		// 搜索时间范围
		String startDate = (String) map.get("startDate");		// 起始时间
		String endDate = (String) map.get("endDate");			// 结束时间
		String orderType = (String) map.get("orderType");		// 订单种类
		
		String order = null;		// 排序项
		String by = null;			// 排序方式（反序）
		if (StringUtils.isNotBlank(orderBy)) {	// 拆分排序
			order = orderBy.split(" ")[0];
			by = orderBy.split(" ")[1];
			if ("DESC".equals(by)) {
				by = "ASC";
			} else {
				by = "DESC";
			}
		}
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ")
		   .append("	ttt.rownum AS rankNum, ")				// 排名
		   .append(" 	ttt.agentName AS analysisTypeName, ")	// 渠道名称
		   .append(" 	ttt.orderNum, ")						// 订单数量
		   .append(" 	ttt.orderPersonNum, ")					// 订单人数
		   .append(" 	ttt.orderAmount AS orderMoney, ")		// 订单金额
		   .append(" 	ttt.enquiryNum AS orderPreNum ")		// 询单数
		   .append("FROM ( ")
		   .append("	SELECT ")
		   .append("		@rownum \\:= @rownum + 1 AS rownum, ")// 用于排名字段，从1开始自增
		   .append("		tt.agentId, ")
		   .append("  		tt.agentName, ")
		   .append("  		FORMAT(tt.orderNum, 0) AS orderNum, ")
		   .append("  		FORMAT(tt.orderPersonNum, 0) AS orderPersonNum, ")
		   .append("  		FORMAT(tt.orderAmount, 2) AS orderAmount, ")
		   .append("  		FORMAT(tt.enquiryNum, 0) AS enquiryNum ")
		   .append(" 	FROM ( ")
		   .append("  		SELECT ")
		   .append("   			@rownum \\:= 0, ")
		   .append("   			t_agent.agentId, ")
		   .append("   			t_agent.agentName, ")
		   .append("   			IFNULL(order_detail.orderNum, 0) AS orderNum, ")
		   .append("   			IFNULL( order_detail.orderPersonNum, 0 ) AS orderPersonNum, ")
		   .append("   			IFNULL(order_detail.amount, 0) AS orderAmount, ")
		   .append("   			IFNULL(enquiry.enquiryNum, 0) AS enquiryNum ")
		   .append("  		FROM ( ")
		   //--------------  获取渠道   --------------------------
		   // ========已启用的全部实时连通渠道=============
		   .append("   			SELECT ")
		   .append("    			ai.id AS agentId, ")
		   .append("    			ai.agentName AS agentName ")
		   .append("   			FROM sys_user u LEFT JOIN agentinfo ai ON ai.id = u.agentId ")
		   .append("   			WHERE ai.id IS NOT NULL ")
		   .append("   			AND ai.delFlag = 0 ")
		   .append("   			AND u.delFlag = 0 ")
		   .append("   			AND ai.is_quauq_agent = 1 ")
		   .append("   			AND ai.enable_quauq_agent = 1 ")
		   .append("   			AND ai.status = 1 ")
		   .append("   			UNION ")
		   // =========未启用但有有效询单的实时连通渠道======
		   .append("			SELECT ")
		   .append("				agent_t.agentId AS agentId, ")
		   .append("				agent_t.agentName AS agentName ")
		   .append("			FROM ( ")
		   .append("				SELECT ")
		   .append("					ai.id AS agentId, ")
		   .append("					ai.agentName AS agentName ")
		   .append("				FROM sys_user u LEFT JOIN agentinfo ai ON ai.id = u.agentId ")
		   .append("				WHERE ai.id IS NOT NULL ")
		   .append("				AND u.delFlag = 0 ")
		   .append("				AND ai.delFlag = 0 ")
		   .append("				AND ai.is_quauq_agent = 1 ")
		   .append("				AND ai.enable_quauq_agent = 0 ")
		   .append("				AND ai.`status` = 1 ")
		   .append("			) agent_t LEFT JOIN ( ")
		   .append("				SELECT ")
		   .append("					o.ask_agent_id AS o_agentId, ")
		   .append("					COUNT(o.id) AS orderCount ")
		   .append("				FROM order_progress_tracking o ")
		   .append("				WHERE o.company_id = ").append(companyId)
		   .append("				AND o.ask_num IS NOT NULL ")
		   .append("				AND o.ask_time IS NOT NULL ")
		   .append("				GROUP BY o.ask_agent_id ")
		   .append("				HAVING orderCount > 0 ")
		   .append("			) order_count ON order_count.o_agentId = agent_t.agentId ")
		   .append("			WHERE order_count.o_agentId IS NOT NULL ")
		   .append("  		) t_agent LEFT JOIN ( ")
		   //---------------  获取询单数  --------------------------
		   .append("   SELECT ")
		   .append("    o.ask_agent_id AS agentId, ")
		   .append("    COUNT(o.id) AS enquiryNum ")
		   .append("   FROM ")
		   .append("    order_progress_tracking o ")
		   .append("   WHERE o.company_id = ").append(companyId)
		   .append("   AND o.ask_num IS NOT NULL ")
		   .append("   AND o.ask_time IS NOT NULL ");
		getDateSql(sql, startDate, endDate, searchDate, "o.ask_time");
		sql.append("   GROUP BY ask_agent_id ")
		   .append("  ) enquiry ON t_agent.agentId = enquiry.agentId ")
		   .append("  LEFT JOIN ( ")
		   //----------------  获取订单总量、订单金额、订单人数  -------------------
		   .append("   SELECT ")
		   .append("    opt.ask_agent_id AS agentId, ")
		   .append("    COUNT(po.id) AS orderNum, ")
		   .append("    SUM(po.orderPersonNum) AS orderPersonNum, ")
		   .append("    SUM( ")	// 订单总额
		   .append("		(SELECT IFNULL(SUM(ma.amount*ma.exchangerate),0) FROM money_amount ma WHERE ma.serialNum = po.total_money) ")
		   .append(" 		- ")// 差额返还
		   .append("  		(SELECT IFNULL(SUM(ma.amount*ma.exchangerate),0) FROM money_amount ma WHERE ma.serialNum = po.differenceMoney) ")
		   .append("	) AS amount ")
		   .append("   FROM ")
		   .append("    order_progress_tracking opt ")
		   .append("   LEFT JOIN productorder po ON opt.order_id = po.id ")
//		   .append("   LEFT JOIN money_amount ma ON ma.serialNum = po.total_money ")
//		   .append("   LEFT JOIN money_amount md ON md.serialNum = po.differenceMoney ")
		   .append("   WHERE opt.ask_num IS NOT NULL ")
		   .append("   AND opt.ask_time IS NOT NULL ")
		   .append("   AND opt.order_id <> '' ")
		   .append("   AND opt.company_id = ").append(companyId);
		getDateSql(sql, startDate, endDate, searchDate, "opt.order_create_time");
		sql.append("   AND po.payStatus IN (3,4,5) ")
		   .append("   AND po.orderStatus = ").append(orderType)
		   .append("   GROUP BY ask_agent_id ")
		   .append("  ) order_detail ON t_agent.agentId = order_detail.agentId ")
		   .append("  ORDER BY ").append(order).append(" DESC, ")
		   .append("  CONVERT (t_agent.agentName USING gbk) COLLATE gbk_chinese_ci ") 		// sprint2 排名修改
		   .append(" ) tt ")
		   .append(") ttt WHERE 1=1 ");
		// 搜索值
		if (StringUtils.isNotBlank(searchValue)) {
			sql.append(" AND ttt.agentName LIKE ? ");
		}
		sql.append(" ORDER BY rankNum ").append(by);
		
		if (StringUtils.isNotBlank(searchValue)) {
			return this.findPageBySql(page, sql.toString(), Map.class, "%"+searchValue+"%");
		} else {
			return this.findPageBySql(page, sql.toString(), Map.class);
		}
	}

	/**
	 * 根据选择不同时间间隔 拼接sql
	 * @param sql SQL
	 * @param startDate 起始时间
	 * @param endDate 结束时间
	 * @param column 字段名
	 * @author yang.wang
	 * @date 2017.3.10
	 * */
	private void getDateSql(StringBuffer sql, String startDate, String endDate, String searchDate, String column) {
		
		if (StringUtils.isNotBlank(startDate) || StringUtils.isNotBlank(endDate)) {
			// 自定义时间
			if (StringUtils.isNotBlank(startDate)) {	// 起始时间
				sql.append(" AND " + column + " >= '" + startDate + " 00:00:00' ");
			}
			if (StringUtils.isNotBlank(endDate)) {		// 结束日期
				sql.append(" AND " + column + " <= '" + endDate + " 23:59:59' ");
			}
		} else if (Context.ORDER_DATA_STATISTICS_ALL.equals(searchDate)){
			// 全部--不做时间限制
		} else {
			// 选择日期
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
	 * 获取第一条询单时间 yyyy-MM-dd
	 * @author yang.wang
	 * @date 2017.3.10
	 * */
	@Override
	public String getEarliestAskTime() {
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		
		List<OrderProgressTracking> earliestAskTime = orderProgressTrackingDao.getEarliestAskTime(companyId);
		
		if (earliestAskTime == null || earliestAskTime.size() == 0) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(earliestAskTime.get(0).getAskTime());
	}

}
