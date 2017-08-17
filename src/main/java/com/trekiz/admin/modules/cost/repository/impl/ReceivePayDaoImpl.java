package com.trekiz.admin.modules.cost.repository.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.quauq.review.core.engine.config.ReviewConstant;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.cost.entity.GroupManagerEntity;
import com.trekiz.admin.modules.cost.entity.ParamBean;
import com.trekiz.admin.modules.cost.repository.IReceivePayDao;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.order.entity.Refund;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 *
 * 财务模块，应收，应付功能对应的Dao接口实现类
 * @author shijun.liu
 * @date 2015年11月12日
 */
@Repository
public class ReceivePayDaoImpl extends BaseDaoImpl<Map<String, Object>> implements IReceivePayDao {
	
	@Override
	public Page<Map<String, Object>> getReceive(Page<Map<String, Object>> page, ParamBean paramBean) {
		Integer orderType = paramBean.getOrderType();
		if(null == orderType || 0 == orderType){//查询所有
			page = getReceiveALL(page, paramBean);
		}else if(Context.ORDER_TYPE_QZ == orderType){
			page = getReceiveQZ(page, paramBean);
		}else if(Context.ORDER_TYPE_JP == orderType){
			page = getReceiveJP(page, paramBean);
		}else if(Context.ORDER_TYPE_HOTEL == orderType){
			page = getReceiveHotel(page, paramBean);
		}else if(Context.ORDER_TYPE_ISLAND == orderType){
			page = getReceiveIsland(page, paramBean);
		}else{//单团类
			page = getReceiveDT(page, paramBean);
		}
		return page;
	}
	
	/**
	 * 查询所有产品的应收账款账龄数据
	 * @param page			分页Page对象
	 * @param paramBean		参数对象
	 * @return
	 * @author shijun.liu
	 */
	private Page<Map<String, Object>> getReceiveALL(Page<Map<String, Object>> page, ParamBean paramBean){
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		String where = optionParam(paramBean);
		str.append(" SELECT ")
		   .append(" 	department,")
		   .append(" 	operator,")
		   .append(" 	salerName,")
		   .append(" 	groupCode,")
		   .append(" 	orderNum,")
		   .append(" 	agentName,")
		   .append(" 	totalMoney,")
		   .append(" 	accountedMoney,")
		   .append(" 	unreceivedMoney,")
		   .append(" 	rate,")
		   .append(" 	receivePayDate,")
		   .append(" 	days,")
		   .append(" 	accountAge30,")
		   .append(" 	accountAge60,")
		   .append(" 	accountAge90,")
		   .append(" 	accountAge180,")
		   .append(" 	accountAge360,")
		   .append(" 	orderType,")
		   .append(" 	orderTime,")
		   .append(" 	createBy,")
		   .append(" 	agentId,")
		   .append(" 	salerId,")
		   .append(" 	deptId,")
		   .append(" 	operatorId,")
		   .append(" 	companyId ")
		   .append(" FROM ")
		   .append(" 	( ")
		   .append(" 		SELECT ")
		   .append(" 			department,")
		   .append(" 			operator,")
		   .append(" 			salerName,")
		   .append(" 			groupCode,")
		   .append(" 			orderNum,")
		   .append(" 			agentName,")
		   .append(" 			totalMoney,")
		   .append(" 			accountedMoney,")
		   .append(" 			(totalMoney - accountedMoney) AS unreceivedMoney,")
		   .append(" 			(CASE WHEN totalMoney > 0 THEN ROUND(accountedMoney / totalMoney * 100, 3) ELSE '0.000' END) AS rate,")
		   .append(" 			receivePayDate,")
		   .append(" 			days,")
		   .append(" 			(CASE WHEN -1 < days AND days < 31 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge30,")
		   .append(" 			(CASE WHEN 30 < days AND days < 61 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge60,")
		   .append(" 			(CASE WHEN 60 < days AND days < 91 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge90,")
		   .append(" 			(CASE WHEN 90 < days AND days < 181 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge180,")
		   .append(" 			(CASE WHEN 180 < days AND days < 361 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge360,")
		   .append(" 			orderType,")
		   .append(" 			orderTime,")
		   .append(" 			createBy,")
		   .append(" 			agentId,")
		   .append(" 			salerId,")
		   .append(" 			deptId,")
		   .append(" 			operatorId,")
		   .append(" 			companyId ")
		   .append(" 		FROM ")
		   .append(" 			( ")
		   .append(" 				SELECT ")
		   .append(" 					(SELECT d. NAME FROM department d WHERE d.id = p.deptId) AS department,")
		   .append(" 					(SELECT su. NAME FROM sys_user su WHERE su.id = p.createBy) AS operator,")
		   .append(" 					o.salerName,")
		   .append(" 					g.groupCode,")
		   .append(" 					o.orderNum,")
		   .append(" 					o.orderCompanyName AS agentName,")
		   .append(" 					(SELECT ROUND(ifnull(sum(m.amount * m.exchangerate), 0), 2) FROM money_amount m WHERE m.serialNum = o.total_money) AS totalMoney,")
		   .append(" 					(SELECT ROUND(ifnull(sum(m.amount * m.exchangerate), 0), 2) FROM money_amount m WHERE m.serialNum = o.accounted_money) AS accountedMoney,")
		   .append(" 					date_FORMAT(g.groupOpenDate, '%Y-%m-%d') AS receivePayDate,")
		   .append(" 					TO_DAYS(NOW()) - TO_DAYS(g.groupOpenDate) AS days,")
		   .append(" 					o.orderStatus AS orderType,")
		   .append(" 					o.orderTime,")
		   .append(" 					o.createBy,")
		   .append(" 					o.orderCompany AS agentId,")
		   .append(" 					o.salerId,")
		   .append(" 					p.deptId,")
		   .append(" 					p.createBy operatorId,")
		   .append(" 					p.proCompany AS companyId ")
		   .append(" 				FROM ")
		   .append(" 					travelactivity p,")
		   .append(" 					activitygroup g,")
		   .append(" 					productorder o")
		   .append(" 				WHERE ")
		   .append(" 					g.srcActivityId = p.id ")
		   .append(" 				AND o.productGroupId = g.id ")
		   .append(" 				AND o.productId = p.id ")
		   .append(" 				AND p.activityStatus != 0 ")
		   .append(" 				AND o.delFlag = '").append(Dict.DEL_FLAG_NORMAL).append("' ")
		   .append(" 				AND p.proCompany = " + currentCompanyId)
		   .append(" 			) t ")
		   .append(" 		UNION ")
		   .append(" 			SELECT ")
		   .append(" 				department,")
		   .append(" 				operator,")
		   .append(" 				salerName,")
		   .append(" 				groupCode,")
		   .append(" 				orderNum,")
		   .append(" 				agentName,")
		   .append(" 				totalMoney,")
		   .append(" 				accountedMoney,")
		   .append(" 				(totalMoney - accountedMoney) AS unreceivedMoney,")
		   .append(" 				(CASE WHEN totalMoney > 0 THEN ROUND(accountedMoney / totalMoney * 100, 3) ELSE '0.000' END) AS rate,")
		   .append(" 				receivePayDate,")
		   .append(" 				days,")
		   .append(" 				(CASE WHEN -1 < days AND days < 31 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge30,")
		   .append(" 				(CASE WHEN 30 < days AND days < 61 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge60,")
		   .append(" 				(CASE WHEN 60 < days AND days < 91 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge90,")
		   .append(" 				(CASE WHEN 90 < days AND days < 181 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge180,")
		   .append(" 				(CASE WHEN 180 < days AND days < 361 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge360,")
		   .append(" 				orderType,")
		   .append(" 				orderTime,")
		   .append(" 				createBy,")
		   .append(" 				agentId,")
		   .append(" 				salerId,")
		   .append(" 				deptId,")
		   .append(" 				operatorId,")
		   .append(" 				companyId ")
		   .append(" 			FROM ")
		   .append(" 				( ")
		   .append(" 					SELECT ")
		   .append(" 						(SELECT d. NAME FROM department d WHERE d.id = p.deptId) AS department,")
		   .append(" 						(SELECT su. NAME FROM sys_user su WHERE su.id = p.createBy) AS operator,")
		   .append(" 						o.salerName,")
		   .append(" 						p.groupCode,")
		   .append(" 						o.order_no AS orderNum,")
		   .append(" 						(SELECT ai.agentName FROM agentinfo ai WHERE ai.id = o.agentinfo_id) AS agentName,")
		   .append(" 						(SELECT ROUND(ifnull(sum(m.amount * m.exchangerate), 0), 2) FROM money_amount m WHERE m.serialNum = o.total_money) AS totalMoney,")
		   .append(" 						(SELECT ROUND(ifnull(sum(m.amount * m.exchangerate), 0), 2) FROM money_amount m WHERE m.serialNum = o.accounted_money) AS accountedMoney,")
		   .append(" 						date_FORMAT(p.createDate, '%Y-%m-%d') AS receivePayDate,")
		   .append(" 						TO_DAYS(NOW()) - TO_DAYS(p.createDate) AS days,")
		   .append(" 						o.product_type_id AS orderType,")
		   .append(" 						o.create_date AS orderTime,")
		   .append(" 						o.create_by AS createBy,")
		   .append(" 						o.agentinfo_id AS agentId,")
		   .append(" 						o.salerId,")
		   .append(" 						p.deptId,")
		   .append(" 						p.createBy operatorId,")
		   .append(" 						p.proCompanyId AS companyId ")
		   .append(" 					FROM ")
		   .append(" 						visa_products p,")
		   .append(" 						visa_order o ")
		   .append(" 					WHERE ")
		   .append(" 						p.id = o.visa_product_id ")
		   .append(" 					AND o.del_flag = '").append(Dict.DEL_FLAG_NORMAL).append("' ")
		   .append(" 					AND o.visa_order_status <> 100 ")
		   .append(" 					AND p.productStatus != 0 ")
		   .append(" 					AND p.proCompanyId = " + currentCompanyId)
		   .append(" 				) t ")
		   .append(" 			UNION ")
		   .append(" 				SELECT ")
		   .append(" 					department,")
		   .append(" 					operator,")
		   .append(" 					salerName,")
		   .append(" 					groupCode,")
		   .append(" 					orderNum,")
		   .append(" 					agentName,")
		   .append(" 					totalMoney,")
		   .append(" 					accountedMoney,")
		   .append(" 					(totalMoney - accountedMoney) AS unreceivedMoney,")
		   .append(" 					(CASE WHEN totalMoney > 0 THEN ROUND(accountedMoney / totalMoney * 100, 3) ELSE '0.000' END) AS rate,")
		   .append(" 					receivePayDate,")
		   .append(" 					days,")
		   .append(" 					(CASE WHEN -1 < days AND days < 31 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge30,")
		   .append(" 					(CASE WHEN 30 < days AND days < 61 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge60,")
		   .append(" 					(CASE WHEN 60 < days AND days < 91 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge90,")
		   .append(" 					(CASE WHEN 90 < days AND days < 181 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge180,")
		   .append(" 					(CASE WHEN 180 < days AND days < 361 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge360,")
		   .append(" 					orderType,")
		   .append(" 					orderTime,")
		   .append(" 					createBy,")
		   .append(" 					agentId,")
		   .append(" 					salerId,")
		   .append(" 					deptId,")
		   .append(" 					operatorId,")
		   .append(" 					companyId ")
		   .append(" 				FROM ")
		   .append(" 					( ")
		   .append(" 						SELECT ")
		   .append(" 							(SELECT d. NAME FROM department d WHERE d.id = p.deptId) AS department,")
		   .append(" 							(SELECT su. NAME FROM sys_user su WHERE su.id = p.createBy) AS operator,")
		   .append(" 							o.salerName,")
		   .append(" 							p.group_code AS groupCode,")
		   .append(" 							o.order_no AS orderNum,")
		   .append(" 							(SELECT ai.agentName FROM agentinfo ai WHERE ai.id = o.agentinfo_id) AS agentName,")
		   .append(" 							(SELECT ROUND(ifnull(sum(m.amount * m.exchangerate), 0), 2) FROM money_amount m WHERE m.serialNum = o.total_money) AS totalMoney,")
		   .append(" 							(SELECT ROUND(ifnull(sum(m.amount * m.exchangerate), 0), 2) FROM money_amount m WHERE m.serialNum = o.accounted_money) AS accountedMoney,")
		   .append(" 							date_FORMAT(p.outTicketTime, '%Y-%m-%d') AS receivePayDate,")
		   .append(" 							TO_DAYS(NOW()) - TO_DAYS(p.outTicketTime) AS days,")
		   .append(" 							o.product_type_id AS orderType,")
		   .append(" 							o.create_date AS orderTime,")
		   .append(" 							o.create_by AS createBy,")
		   .append(" 							o.agentinfo_id AS agentId,")
		   .append(" 							o.salerId,")
		   .append(" 							p.deptId,")
		   .append(" 							p.createBy operatorId,")
		   .append(" 							p.proCompany AS companyId ")
		   .append(" 						FROM ")
		   .append(" 							activity_airticket p,")
		   .append(" 							airticket_order o ")
		   .append(" 						WHERE ")
		   .append(" 							p.id = o.airticket_id ")
		   .append("						AND	p.productStatus != 0 ")
		   .append(" 						AND o.del_flag = '").append(Dict.DEL_FLAG_NORMAL).append("' ")
		   .append(" 						AND p.proCompany = " + currentCompanyId)
		   .append(" 					) t ")
		   .append(" 				UNION ")
		   .append(" 					SELECT ")
		   .append(" 						department,")
		   .append(" 						operator,")
		   .append(" 						salerName,")
		   .append(" 						groupCode,")
		   .append(" 						orderNum,")
		   .append(" 						agentName,")
		   .append(" 						totalMoney,")
		   .append(" 						accountedMoney,")
		   .append(" 						(totalMoney - accountedMoney) AS unreceivedMoney,")
		   .append(" 						(CASE WHEN totalMoney > 0 THEN ROUND(accountedMoney / totalMoney * 100, 3) ELSE '0.000' END) AS rate,")
		   .append(" 						receivePayDate,")
		   .append(" 						days,")
		   .append(" 						(CASE WHEN -1 < days AND days < 31 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge30,")
		   .append(" 						(CASE WHEN 30 < days AND days < 61 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge60,")
		   .append(" 						(CASE WHEN 60 < days AND days < 91 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge90,")
		   .append(" 						(CASE WHEN 90 < days AND days < 181 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge180,")
		   .append(" 						(CASE WHEN 180 < days AND days < 361 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge360,")
		   .append(" 						orderType,")
		   .append(" 						orderTime,")
		   .append(" 						createBy,")
		   .append(" 						agentId,")
		   .append(" 						salerId,")
		   .append(" 						deptId,")
		   .append(" 						operatorId,")
		   .append(" 						companyId ")
		   .append(" 					FROM ")
		   .append(" 						( ")
		   .append(" 							SELECT ")
		   .append(" 								(SELECT d. NAME FROM department d WHERE d.id = p.deptId) AS department,")
		   .append(" 								(SELECT su. NAME FROM sys_user su WHERE su.id = p.createBy) AS operator,")
		   .append(" 								o.salerName,")
		   .append(" 								g.groupCode,")
		   .append(" 								o.orderNum,")
		   .append(" 								(SELECT ai.agentName FROM agentinfo ai WHERE ai.id = o.orderCompany) AS agentName,")
		   .append(" 								(SELECT ROUND(ifnull(sum(m.amount * m.exchangerate), 0), 2) FROM money_amount m WHERE m.serialNum = o.total_money) AS totalMoney,")
		   .append(" 								(SELECT ROUND(ifnull(sum(m.amount * m.exchangerate), 0), 2) FROM money_amount m WHERE m.serialNum = o.accounted_money) AS accountedMoney,")
		   .append(" 								date_FORMAT(g.createDate, '%Y-%m-%d') AS receivePayDate,")
		   .append(" 								TO_DAYS(NOW()) - TO_DAYS(g.createDate) AS days,")
		   .append(" 								11 AS orderType,")
		   .append(" 								o.createDate AS orderTime,")
		   .append(" 								o.createBy,")
		   .append(" 								o.orderCompany AS agentId,")
		   .append(" 								o.salerId,")
		   .append(" 								p.deptId,")
		   .append(" 								p.createBy operatorId,")
		   .append(" 								p.wholesaler_id AS companyId")
		   .append(" 							FROM ")
		   .append(" 								activity_hotel p,")
		   .append(" 								activity_hotel_group g,")
		   .append(" 								hotel_order o ")
		   .append(" 							WHERE ")
		   .append(" 								g.activity_hotel_uuid = p.uuid ")
		   .append(" 							AND o.activity_hotel_group_uuid = g.uuid ")
		   .append(" 							AND o.activity_hotel_uuid = p.uuid ")
		   .append(" 							AND p.wholesaler_id = " + currentCompanyId)
		   .append(" 						) t ")
		   .append(" 					UNION ")
		   .append(" 						SELECT ")
		   .append(" 							department, ")
		   .append(" 							operator, ")
		   .append(" 							salerName, ")
		   .append(" 							groupCode,")
		   .append(" 							orderNum,")
		   .append(" 							agentName,")
		   .append(" 							totalMoney,")
		   .append(" 							accountedMoney,")
		   .append(" 							(totalMoney - accountedMoney) AS unreceivedMoney,")
		   .append(" 							(CASE WHEN totalMoney > 0 THEN ROUND(accountedMoney / totalMoney * 100, 3) ELSE '0.000' END) AS rate,")
		   .append(" 							receivePayDate,")
		   .append(" 							days,")
		   .append(" 							(CASE WHEN -1 < days AND days < 31 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge30,")
		   .append(" 							(CASE WHEN 30 < days AND days < 61 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge60,")
		   .append(" 							(CASE WHEN 60 < days AND days < 91 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge90,")
		   .append(" 							(CASE WHEN 90 < days AND days < 181 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge180,")
		   .append(" 							(CASE WHEN 180 < days AND days < 361 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge360,")
		   .append(" 							orderType,")
		   .append(" 							orderTime,")
		   .append(" 							createBy,")
		   .append(" 							agentId,")
		   .append(" 							salerId,")
		   .append(" 							deptId,")
		   .append(" 							operatorId,")
		   .append(" 							companyId ")
		   .append(" 						FROM ")
		   .append(" 							( ")
		   .append(" 								SELECT ")
		   .append(" 									(SELECT d. NAME FROM department d WHERE d.id = p.deptId) AS department,")
		   .append(" 									(SELECT su. NAME FROM sys_user su WHERE su.id = p.createBy) AS operator,")
		   .append(" 									o.salerName,")
		   .append(" 									g.groupCode,")
		   .append(" 									o.orderNum,")
		   .append(" 									(SELECT ai.agentName FROM agentinfo ai WHERE ai.id = o.orderCompany) AS agentName,")
		   .append(" 									(SELECT ROUND(ifnull(sum(m.amount * m.exchangerate), 0), 2) FROM money_amount m WHERE m.serialNum = o.total_money) AS totalMoney,")
		   .append(" 									(SELECT ROUND(ifnull(sum(m.amount * m.exchangerate), 0), 2) FROM money_amount m WHERE m.serialNum = o.accounted_money) AS accountedMoney,")
		   .append(" 									date_FORMAT(g.createDate, '%Y-%m-%d') AS receivePayDate,")
		   .append(" 									TO_DAYS(NOW()) - TO_DAYS(g.createDate) AS days,")
		   .append(" 									12 AS orderType,")
		   .append(" 									o.createDate AS orderTime,")
		   .append(" 									o.createBy,")
		   .append(" 									o.orderCompany AS agentId,")
		   .append(" 									o.salerId,")
		   .append(" 									p.deptId,")
		   .append(" 									p.createBy operatorId,")
		   .append(" 									p.wholesaler_id AS companyId ")
		   .append(" 								FROM ")
		   .append(" 									activity_island p,")
		   .append(" 									activity_island_group g,")
		   .append(" 									island_order o ")
		   .append(" 								WHERE ")
		   .append(" 									g.activity_island_uuid = p.uuid ")
		   .append(" 								AND o.activity_island_group_uuid = g.uuid ")
		   .append(" 								AND o.activity_island_uuid = p.uuid ")
		   .append(" 								AND p.wholesaler_id = " + currentCompanyId)
		   .append(" 							) t ")
		   .append(" 	) rr ").append(where);
		return findBySql(page, str.toString(), Map.class);
	}
	
	/**
	 * 查询单团类产品的应收账款账龄数据
	 * @param page			分页Page对象
	 * @param paramBean		参数对象
	 * @return
	 * @author shijun.liu
	 */
	private Page<Map<String, Object>> getReceiveDT(Page<Map<String, Object>> page, ParamBean paramBean){
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		String where = optionParam(paramBean);
		str.append(" SELECT ")
		   .append(" 	department, ")
		   .append(" 	operator,")
		   .append(" 	salerName,")
		   .append(" 	groupCode,")
		   .append(" 	orderNum,")
		   .append(" 	agentName,")
		   .append(" 	totalMoney,")
		   .append(" 	accountedMoney, ")
		   .append(" 	(totalMoney - accountedMoney) AS unreceivedMoney,")
		   .append(" 	(CASE WHEN totalMoney > 0 THEN ROUND(accountedMoney / totalMoney * 100, 3) ELSE '0.000' END) AS rate,")
		   .append(" 	receivePayDate, ")
		   .append(" 	days, ")
		   .append(" 	(CASE WHEN -1 < days AND days < 31 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge30,")
		   .append(" 	(CASE WHEN 30 < days AND days < 61 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge60,")
		   .append(" 	(CASE WHEN 60 < days AND days < 91 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge90,")
		   .append(" 	(CASE WHEN 90 < days AND days < 181 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge180,")
		   .append(" 	(CASE WHEN 180 < days AND days < 361 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge360,")
		   .append("    orderType,")
		   .append("    orderTime,")
		   .append("    createBy,")
		   .append("    agentId,")
		   .append("    salerId,")
		   .append("    deptId,")
		   .append("    operatorId,")
		   .append("    companyId ")
		   .append(" FROM ")
		   .append(" 	( ")
		   .append(" 		SELECT  ")
		   .append(" 			(SELECT d. NAME FROM department d WHERE d.id = p.deptId) AS department, ")
		   .append(" 			(SELECT su. NAME FROM sys_user su WHERE su.id = p.createBy) AS operator,")
		   .append(" 			o.salerName,  ")
		   .append(" 			g.groupCode, ")
		   .append(" 			o.orderNum, ")
		   .append(" 			o.orderCompanyName AS agentName,")
		   .append(" 			(SELECT ROUND(ifnull(sum(m.amount * m.exchangerate), 0), 2) FROM money_amount m WHERE m.serialNum = o.total_money) AS totalMoney,")
		   .append(" 			(SELECT ROUND(ifnull(sum(m.amount * m.exchangerate), 0), 2) FROM money_amount m WHERE m.serialNum = o.accounted_money) AS accountedMoney,")
		   .append(" 			date_FORMAT(g.groupOpenDate, '%Y-%m-%d') AS receivePayDate, ")
		   .append(" 			TO_DAYS(NOW()) - TO_DAYS(g.groupOpenDate) AS days, ")
		   .append(" 			o.orderStatus AS orderType, ")
		   .append(" 			o.orderTime, ")
		   .append(" 			o.createBy, ")
		   .append(" 			o.orderCompany AS agentId, ")
		   .append(" 			o.salerId, ")
		   .append(" 			p.deptId, ")
		   .append(" 			p.createBy operatorId, ")
		   .append(" 			p.proCompany AS companyId ")
		   .append(" 		FROM  ")
		   .append(" 			travelactivity p,")
		   .append(" 			activitygroup g, ")
		   .append(" 			productorder o ")
		   .append(" 		WHERE ")
		   .append(" 			g.srcActivityId = p.id ")
		   .append(" 		AND o.productGroupId = g.id ")
		   .append(" 		AND o.productId = p.id ")
		   .append(" 		AND p.activityStatus !=0 ")
		   .append("        AND o.delFlag = '").append(Dict.DEL_FLAG_NORMAL).append("'")
		   .append("        AND p.proCompany = " + currentCompanyId)
		   .append(" 	) t ").append(where);
		return findBySql(page, str.toString(), Map.class);
	}
	
	/**
	 * 查询签证产品的应收账款账龄数据
	 * @param page			分页Page对象
	 * @param paramBean		参数对象
	 * @return
	 * @author shijun.liu
	 */
	private Page<Map<String, Object>> getReceiveQZ(Page<Map<String, Object>> page, ParamBean paramBean){
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		String where = optionParam(paramBean);
		str.append(" SELECT ")
		   .append(" 	department, ")
		   .append(" 	operator,")
		   .append(" 	salerName,")
		   .append(" 	groupCode,")
		   .append(" 	orderNum,")
		   .append(" 	agentName,")
		   .append(" 	totalMoney,")
		   .append(" 	accountedMoney, ")
		   .append(" 	(totalMoney - accountedMoney) AS unreceivedMoney,")
		   .append(" 	(CASE WHEN totalMoney > 0 THEN ROUND(accountedMoney / totalMoney * 100, 3) ELSE '0.000' END) AS rate,")
		   .append(" 	receivePayDate, ")
		   .append(" 	days, ")
		   .append(" 	(CASE WHEN -1 < days AND days < 31 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge30,")
		   .append(" 	(CASE WHEN 30 < days AND days < 61 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge60,")
		   .append(" 	(CASE WHEN 60 < days AND days < 91 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge90,")
		   .append(" 	(CASE WHEN 90 < days AND days < 181 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge180,")
		   .append(" 	(CASE WHEN 180 < days AND days < 361 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge360,")
		   .append("    orderType,")
		   .append("    orderTime,")
		   .append("    createBy,")
		   .append("    agentId,")
		   .append("    salerId,")
		   .append("    deptId,")
		   .append("    operatorId,")
		   .append("    companyId ")
		   .append(" FROM ")
		   .append(" 	( ")
		   .append(" 		SELECT  ")
		   .append(" 			(SELECT d. NAME FROM department d WHERE d.id = p.deptId) AS department, ")
		   .append(" 			(SELECT su. NAME FROM sys_user su WHERE su.id = p.createBy) AS operator,")
		   .append(" 			o.salerName,  ")
		   .append(" 			p.groupCode, ")
		   .append(" 			o.order_no AS orderNum,")
		   .append(" 			(SELECT ai.agentName FROM agentinfo ai WHERE ai.id = o.agentinfo_id) AS agentName,")
		   .append(" 			(SELECT ROUND(ifnull(sum(m.amount * m.exchangerate), 0), 2) FROM money_amount m WHERE m.serialNum = o.total_money) AS totalMoney,")
		   .append(" 			(SELECT ROUND(ifnull(sum(m.amount * m.exchangerate), 0), 2) FROM money_amount m WHERE m.serialNum = o.accounted_money) AS accountedMoney,")
		   .append(" 			date_FORMAT(p.createDate, '%Y-%m-%d') AS receivePayDate, ")
		   .append(" 			TO_DAYS(NOW()) - TO_DAYS(p.createDate) AS days, ")
		   .append(" 			o.product_type_id AS orderType, ")
		   .append(" 			o.create_date as orderTime, ")
		   .append(" 			o.create_by as createBy,")
		   .append(" 			o.agentinfo_id AS agentId, ")
		   .append(" 			o.salerId, ")
		   .append(" 			p.deptId, ")
		   .append(" 			p.createBy operatorId, ")
		   .append(" 			p.proCompanyId AS companyId ")
		   .append(" 		FROM  ")
		   .append(" 			visa_products p,")
		   .append(" 			visa_order o ")
		   .append(" 		WHERE ")
		   .append(" 			p.id = o.visa_product_id ")
		   .append("        AND o.del_flag = '").append(Dict.DEL_FLAG_NORMAL).append("'")
		   .append(" 		AND o.visa_order_status <> 100 ")
		   .append(" 		AND p.productStatus != 0 ")
		   .append(" 		AND p.proCompanyId = " + currentCompanyId)
		   .append(" 	) t ").append(where);
		return findBySql(page, str.toString(), Map.class);
	}
	
	/**
	 * 查询机票产品的应收账款账龄数据
	 * @param page			分页Page对象
	 * @param paramBean		参数对象
	 * @return
	 * @author shijun.liu
	 */
	private Page<Map<String, Object>> getReceiveJP(Page<Map<String, Object>> page, ParamBean paramBean){
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		String where = optionParam(paramBean);
		str.append(" SELECT ")
		   .append(" 	department, ")
		   .append(" 	operator,")
		   .append(" 	salerName,")
		   .append(" 	groupCode,")
		   .append(" 	orderNum,")
		   .append(" 	agentName,")
		   .append(" 	totalMoney,")
		   .append(" 	accountedMoney, ")
		   .append(" 	(totalMoney - accountedMoney) AS unreceivedMoney,")
		   .append(" 	(CASE WHEN totalMoney > 0 THEN ROUND(accountedMoney / totalMoney * 100, 3) ELSE '0.000' END) AS rate,")
		   .append(" 	receivePayDate, ")
		   .append(" 	days, ")
		   .append(" 	(CASE WHEN -1 < days AND days < 31 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge30,")
		   .append(" 	(CASE WHEN 30 < days AND days < 61 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge60,")
		   .append(" 	(CASE WHEN 60 < days AND days < 91 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge90,")
		   .append(" 	(CASE WHEN 90 < days AND days < 181 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge180,")
		   .append(" 	(CASE WHEN 180 < days AND days < 361 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge360,")
		   .append("    orderType,")
		   .append("    orderTime,")
		   .append("    createBy,")
		   .append("    agentId,")
		   .append("    salerId,")
		   .append("    deptId,")
		   .append("    operatorId,")
		   .append("    companyId ")
		   .append(" FROM ")
		   .append(" 	( ")
		   .append(" 		SELECT  ")
		   .append(" 			(SELECT d. NAME FROM department d WHERE d.id = p.deptId) AS department, ")
		   .append(" 			(SELECT su. NAME FROM sys_user su WHERE su.id = p.createBy) AS operator,")
		   .append(" 			o.salerName,  ")
		   .append(" 			p.group_code as groupCode, ")
		   .append(" 			o.order_no AS orderNum,")
		   .append(" 			(SELECT ai.agentName FROM agentinfo ai WHERE ai.id = o.agentinfo_id) AS agentName,")
		   .append(" 			(SELECT ROUND(ifnull(sum(m.amount * m.exchangerate), 0), 2) FROM money_amount m WHERE m.serialNum = o.total_money) AS totalMoney,")
		   .append(" 			(SELECT ROUND(ifnull(sum(m.amount * m.exchangerate), 0), 2) FROM money_amount m WHERE m.serialNum = o.accounted_money) AS accountedMoney,")
		   .append(" 			date_FORMAT(p.outTicketTime, '%Y-%m-%d') AS receivePayDate, ")
		   .append(" 			TO_DAYS(NOW()) - TO_DAYS(p.outTicketTime) AS days, ")
		   .append(" 			o.product_type_id AS orderType, ")
		   .append(" 			o.create_date as orderTime, ")
		   .append(" 			o.create_by as createBy, ")
		   .append(" 			o.agentinfo_id AS agentId, ")
		   .append(" 			o.salerId, ")
		   .append(" 			p.deptId, ")
		   .append(" 			p.createBy operatorId, ")
		   .append(" 			p.proCompany AS companyId ")
		   .append(" 		FROM  ")
		   .append(" 			activity_airticket p,")
		   .append(" 			airticket_order o ")
		   .append(" 		WHERE ")
		   .append(" 			p.id = o.airticket_id ")
		   .append(" 		AND p.productStatus != 0 ")
		   .append("        AND o.del_flag = '").append(Dict.DEL_FLAG_NORMAL).append("'")
		   .append("        AND p.proCompany = " + currentCompanyId)
		   .append(" 	) t ").append(where);
		return findBySql(page, str.toString(), Map.class);
	}
	
	/**
	 * 查询酒店产品的应收账款账龄数据
	 * @param page			分页Page对象
	 * @param paramBean		参数对象
	 * @return
	 * @author shijun.liu
	 */
	private Page<Map<String, Object>> getReceiveHotel(Page<Map<String, Object>> page, ParamBean paramBean){
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		String where = optionParam(paramBean);
		str.append(" SELECT ")
		   .append(" 	department, ")
		   .append(" 	operator,")
		   .append(" 	salerName,")
		   .append(" 	groupCode,")
		   .append(" 	orderNum,")
		   .append(" 	agentName,")
		   .append(" 	totalMoney,")
		   .append(" 	accountedMoney, ")
		   .append(" 	(totalMoney - accountedMoney) AS unreceivedMoney,")
		   .append(" 	(CASE WHEN totalMoney > 0 THEN ROUND(accountedMoney / totalMoney * 100, 3) ELSE '0.000' END) AS rate,")
		   .append(" 	receivePayDate, ")
		   .append(" 	days, ")
		   .append(" 	(CASE WHEN -1 < days AND days < 31 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge30,")
		   .append(" 	(CASE WHEN 30 < days AND days < 61 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge60,")
		   .append(" 	(CASE WHEN 60 < days AND days < 91 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge90,")
		   .append(" 	(CASE WHEN 90 < days AND days < 181 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge180,")
		   .append(" 	(CASE WHEN 180 < days AND days < 361 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge360,")
		   .append("    orderType,")
		   .append("    orderTime,")
		   .append("    createBy,")
		   .append("    agentId,")
		   .append("    salerId,")
		   .append("    deptId,")
		   .append("    operatorId,")
		   .append("    companyId ")
		   .append(" FROM ")
		   .append(" 	( ")
		   .append(" 		SELECT  ")
		   .append(" 			(SELECT d. NAME FROM department d WHERE d.id = p.deptId) AS department, ")
		   .append(" 			(SELECT su. NAME FROM sys_user su WHERE su.id = p.createBy) AS operator,")
		   .append(" 			o.salerName,  ")
		   .append(" 			g.groupCode, ")
		   .append(" 			o.orderNum, ")
		   .append(" 			(SELECT ai.agentName FROM agentinfo ai WHERE ai.id = o.orderCompany) AS agentName,")
		   .append(" 			(SELECT ROUND(ifnull(sum(m.amount * m.exchangerate), 0), 2) FROM money_amount m WHERE m.serialNum = o.total_money) AS totalMoney,")
		   .append(" 			(SELECT ROUND(ifnull(sum(m.amount * m.exchangerate), 0), 2) FROM money_amount m WHERE m.serialNum = o.accounted_money) AS accountedMoney,")
		   .append(" 			date_FORMAT(g.createDate, '%Y-%m-%d') AS receivePayDate, ")
		   .append(" 			TO_DAYS(NOW()) - TO_DAYS(g.createDate) AS days, ")
		   .append(" 			11 AS orderType, ")
		   .append(" 			o.createDate as orderTime, ")
		   .append(" 			o.createBy, ")
		   .append(" 			o.orderCompany AS agentId, ")
		   .append(" 			o.salerId, ")
		   .append(" 			p.deptId, ")
		   .append(" 			p.createBy operatorId, ")
		   .append(" 			p.wholesaler_id AS companyId ")
		   .append(" 		FROM  ")
		   .append(" 			activity_hotel p,")
		   .append(" 			activity_hotel_group g, ")
		   .append(" 			hotel_order o ")
		   .append(" 		WHERE ")
		   .append(" 			g.activity_hotel_uuid = p.uuid ")
		   .append(" 		AND o.activity_hotel_group_uuid = g.uuid ")
		   .append(" 		AND o.activity_hotel_uuid = p.uuid ")
		   .append("        AND o.delFlag = '").append(Dict.DEL_FLAG_NORMAL).append("'")
		   .append("        AND p.wholesaler_id = " + currentCompanyId)
		   .append(" 	) t ").append(where);
		return findBySql(page, str.toString(), Map.class);
	}
	
	/**
	 * 查询海岛游产品的应收账款账龄数据
	 * @param page			分页Page对象
	 * @param paramBean		参数对象
	 * @return
	 * @author shijun.liu
	 */
	private Page<Map<String, Object>> getReceiveIsland(Page<Map<String, Object>> page, ParamBean paramBean){
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		String where = optionParam(paramBean);
		str.append(" SELECT ")
		   .append(" 	department, ")
		   .append(" 	operator,")
		   .append(" 	salerName,")
		   .append(" 	groupCode,")
		   .append(" 	orderNum,")
		   .append(" 	agentName,")
		   .append(" 	totalMoney,")
		   .append(" 	accountedMoney, ")
		   .append(" 	(totalMoney - accountedMoney) AS unreceivedMoney,")
		   .append(" 	(CASE WHEN totalMoney > 0 THEN ROUND(accountedMoney / totalMoney * 100, 3) ELSE '0.000' END) AS rate,")
		   .append(" 	receivePayDate, ")
		   .append(" 	days, ")
		   .append(" 	(CASE WHEN -1 < days AND days < 31 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge30,")
		   .append(" 	(CASE WHEN 30 < days AND days < 61 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge60,")
		   .append(" 	(CASE WHEN 60 < days AND days < 91 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge90,")
		   .append(" 	(CASE WHEN 90 < days AND days < 181 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge180,")
		   .append(" 	(CASE WHEN 180 < days AND days < 361 THEN(totalMoney - accountedMoney) ELSE '0.00' END) AS accountAge360,")
		   .append("    orderType,")
		   .append("    orderTime,")
		   .append("    createBy,")
		   .append("    agentId,")
		   .append("    salerId,")
		   .append("    deptId,")
		   .append("    operatorId,")
		   .append("    companyId ")
		   .append(" FROM ")
		   .append(" 	( ")
		   .append(" 		SELECT  ")
		   .append(" 			(SELECT d. NAME FROM department d WHERE d.id = p.deptId) AS department, ")
		   .append(" 			(SELECT su. NAME FROM sys_user su WHERE su.id = p.createBy) AS operator,")
		   .append(" 			o.salerName,  ")
		   .append(" 			g.groupCode, ")
		   .append(" 			o.orderNum, ")
		   .append(" 			(SELECT ai.agentName FROM agentinfo ai WHERE ai.id = o.orderCompany) AS agentName,")
		   .append(" 			(SELECT ROUND(ifnull(sum(m.amount * m.exchangerate), 0), 2) FROM money_amount m WHERE m.serialNum = o.total_money) AS totalMoney,")
		   .append(" 			(SELECT ROUND(ifnull(sum(m.amount * m.exchangerate), 0), 2) FROM money_amount m WHERE m.serialNum = o.accounted_money) AS accountedMoney,")
		   .append(" 			date_FORMAT(g.createDate, '%Y-%m-%d') AS receivePayDate, ")
		   .append(" 			TO_DAYS(NOW()) - TO_DAYS(g.createDate) AS days, ")
		   .append(" 			12 AS orderType, ")
		   .append(" 			o.createDate as orderTime, ")
		   .append(" 			o.createBy, ")
		   .append(" 			o.orderCompany AS agentId, ")
		   .append(" 			o.salerId, ")
		   .append(" 			p.deptId, ")
		   .append(" 			p.createBy operatorId, ")
		   .append(" 			p.wholesaler_id AS companyId ")
		   .append(" 		FROM  ")
		   .append(" 			activity_island p,")
		   .append(" 			activity_island_group g, ")
		   .append(" 			island_order o ")
		   .append(" 		WHERE ")
		   .append(" 			g.activity_island_uuid = p.uuid ")
		   .append(" 		AND o.activity_island_group_uuid = g.uuid ")
		   .append(" 		AND o.activity_island_uuid = p.uuid ")
		   .append("        AND o.delFlag = '").append(Dict.DEL_FLAG_NORMAL).append("'")
		   .append("        AND p.wholesaler_id = " + currentCompanyId)
		   .append(" 	) t ").append(where);
		return findBySql(page, str.toString(), Map.class);
	}
	
	/**
	 * 处理参数数据，将参数数据转换成字符串
	 * @param paramBean
	 * @return
	 * @author shijun.liu
	 */
	private String optionParam(ParamBean paramBean){
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		StringBuffer where = new StringBuffer();
		String groupCode = paramBean.getGroupCode();		//团号
		Integer orderType = paramBean.getOrderType();		//订单类型
		String orderNum = paramBean.getOrderNum();			//订单编号
		Long agentId = paramBean.getAgentId();				//渠道ID
		Long salerId = paramBean.getSalerId();				//销售ID
		Long deptId = paramBean.getDeptId();				//部门ID
		Long operatorId = paramBean.getOperatorId();		//计调
		String isBadAccount = paramBean.getBadAccount();	//是否坏账
		String paymentType = paramBean.getPaymentType();     // 渠道结款方式
		where.append(" where companyId = ").append(currentCompanyId);
		if(StringUtils.isNotBlank(groupCode)){
			where.append(" AND groupCode like '%").append(groupCode).append("%' ");
		}
		if(null != orderType && 0 != orderType ){
			where.append(" AND orderType = ").append(orderType);
		}
		if(StringUtils.isNotBlank(orderNum)){
			where.append(" AND orderNum like '%").append(orderNum).append("%' ");
		}
		if(null != agentId){
			where.append(" AND agentId = ").append(agentId);
		}
		if(null != salerId){
			where.append(" AND salerId = ").append(salerId);
		}
		if(null != deptId){
			where.append(" AND deptId = ").append(deptId);
		}
		if(null != operatorId){
			where.append(" AND operatorId = ").append(operatorId);
		}
		//如果超过360天，应收余额还有数值则表示坏账
		if("Y".equalsIgnoreCase(isBadAccount)){
			where.append(" AND (totalMoney - accountedMoney) != 0.00 ")
			     .append(" AND days > 360 ");
		}
		if("N".equalsIgnoreCase(isBadAccount)){
			where.append(" AND days < 361 ");
		}
		if(StringUtils.isNotBlank(paymentType)){
			where.append(" AND agentId in (select id from agentinfo where paymentType =  " + paymentType + " )");
		}
		return where.toString();
	}


	@Override
	public List<Map<Object, Object>> getPayListByPayDate() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		//团类是出团日期 机票是出票日期 签证取发布日期
		sb.append("select t1.id,t1.orderNum,t1.orderType,t1.payableDate,t1.createBy from (")
			.append("select a.id,a.orderNum,a.orderStatus orderType,date(b.groupOpenDate) payableDate,a.createBy ")
			.append(" from productorder a , activitygroup b where a.productGroupId = b.id " )
			.append(" and b.groupOpenDate = curdate() ")
			.append(" union ")
			.append(" select c.id,c.order_no orderNum,6 orderType,date(d.createDate) payableDate,c.create_by createBy ")
			.append(" from visa_order c , visa_products d where c.visa_product_id = d.id ")
			.append(" and date(d.createDate) = curdate() ")
			.append(" union ")
			.append(" select e.id,e.order_no orderNum,7 orderType,date(f.outTicketTime) payableDate,e.create_by createBy ")
			.append(" from airticket_order e , activity_airticket f where e.airticket_id = f.id ")
			.append(" and f.outTicketTime = curdate() ) t1");
		try {
			//20151212处理定时任务执行两次抛出的异常
			List<Map<Object, Object>> list = this.findBySql(sb.toString(), Map.class);
			if(list != null && list.size() > 0) {
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 
	* @Title: findUserIdByReceivePay
	* @Description: TODO(获取应收账款到期的消息可查看人员包括：财务、销售、计调、总经理)
	* @param companyId 批发商id
	* @param @return    设定文件
	* @return List<Long>    返回类型
	* @throws
	 */
	@Override
	public List<Long> findUserIdByReceivePay(Long companyId) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("select u.id,u.name,r.name roleName from sys_user u,sys_user_role ur,sys_role r "
				+ "where u.id=ur.userId and ur.roleId=r.id"
				+ " and r.companyId=" + companyId
				+ " and r.delFlag = '" + Role.DEL_FLAG_NORMAL + "'" 
				+ " and u.delFlag = '" + User.DEL_FLAG_NORMAL + "'"
				+ " and  (r.`name` like '%财务%' or r.`name` like '%销售%' " 
				+ " or r.`name` like '%计调%' or r.`name` like '%总经理%') "
				+ " group by u.id,u.name   order by u.name");
		List<Map<Object, Object>> list = this.findBySql(sb.toString(), Map.class);
		List<Long> userIdList = new ArrayList<Long>();
		if(list != null && list.size() > 0) {
			for (Map<Object, Object> map : list) {
				String id = map.get("id") == null ? "" : map.get("id").toString();
				if(StringUtil.isNotBlank(id)) {
					userIdList.add(Long.parseLong(id));
				}
			}
			return userIdList;
		}
		return null;
	}

	@Override
	public List<Map<String, Object>> getActivityOrderRealReceiveSumMoney(GroupManagerEntity entity, Integer orderType) {
		StringBuffer str = new StringBuffer();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		StringBuffer where = new StringBuffer();
		if(StringUtils.isNotBlank(entity.getGroupCode())){
			where.append(" and g.groupCode like '%").append(entity.getGroupCode()).append("%' ");
		}
		if(StringUtils.isNotBlank(entity.getProductName())){
			where.append(" and p.acitivityName like '%").append(entity.getProductName()).append("%' ");
		}
		if(null != entity.getOperatorId()){
			where.append(" and g.createBy = ").append(entity.getOperatorId());
		}
		if(null != entity.getSalerId()){
			where.append(" and o.salerId = ").append(entity.getSalerId());
		}
		if(StringUtils.isNotBlank(entity.getIscommission())){
			where.append(" and g.iscommission = ").append(entity.getIscommission());
		}
		if(null != entity.getDeptId()){
			where.append(" and p.deptId = ").append(entity.getDeptId());
		}
		if(StringUtils.isNotBlank(entity.getGroupOpenDate())){
			where.append(" and g.groupOpenDate >= '").append(entity.getGroupOpenDate()).append("'");
		}
		if(StringUtils.isNotBlank(entity.getGroupCloseDate())){
			where.append(" and g.groupOpenDate <= '").append(entity.getGroupCloseDate()).append("'");
		}
		
		str.append(" SELECT ")
		   .append("     sum(m.amount * m.exchangerate) AS amount, ")
		   .append("     '¥' AS currency_mark ")
		   .append("     FROM ")
		   .append("     money_amount m ")
		   .append("     WHERE ")
		   .append("        EXISTS ( ")
		   .append("             SELECT ")
		   .append("             o.accounted_money ")
		   .append("             FROM ")
		   .append("             productorder o, ")
		   .append("             activitygroup g, ")
		   .append("             travelactivity p ")
		   .append("             WHERE ")
		   .append("             g.srcActivityId = p.id ")
		   .append("             AND o.productGroupId = g.id ")
		   .append("             AND o.productId = p.id ")
		   .append("             AND p.proCompany = ").append(currentCompanyId)
		   .append("             AND g.delFlag = '0' ")
		   .append("             AND o.delFlag = '0' ")
	       .append("             AND p.delFlag = '0' ")
		   .append("             AND o.payStatus NOT IN (99, 111) ")
		   .append("             AND p.activityStatus = 2 ")
		   .append("             AND o.accounted_money IS NOT NULL ")
		   .append("             AND o.accounted_money = m.serialNum ");
		if(Context.ORDER_TYPE_ALL != orderType){
			str.append(" AND o.orderStatus = ").append(orderType)
			   .append(" AND p.activity_kind = ").append(orderType);
		}
		str.append(where.toString()).append(" ) ");
		return findBySql(str.toString(), Map.class);
	}

	@Override
	public List<Map<String, Object>> getVisaOrderRealReceiveSumMoney(GroupManagerEntity entity) {
		StringBuffer str = new StringBuffer();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		StringBuffer where = new StringBuffer();
		if(StringUtils.isNotBlank(entity.getGroupCode())){
			where.append(" and p.groupCode like '%").append(entity.getGroupCode()).append("%' ");
		}
		if(StringUtils.isNotBlank(entity.getProductName())){
			where.append(" and p.productName like '%").append(entity.getProductName()).append("%' ");
		}
		if(null != entity.getOperatorId()){
			where.append(" and p.createBy = ").append(entity.getOperatorId());
		}
		if(null != entity.getSalerId()){
			where.append(" and o.salerId = ").append(entity.getSalerId());
		}
		if(StringUtils.isNotBlank(entity.getIscommission())){
			where.append(" and p.iscommission = ").append(entity.getIscommission());
		}
		if(null != entity.getDeptId()){
			where.append(" and p.deptId = ").append(entity.getDeptId());
		}
		if(StringUtils.isNotBlank(entity.getGroupOpenDate())){
			where.append(" and p.createDate >= '").append(entity.getGroupOpenDate()).append("'");
		}
		if(StringUtils.isNotBlank(entity.getGroupCloseDate())){
			where.append(" and p.createDate <= '").append(entity.getGroupCloseDate()).append("'");
		}
		str.append(" SELECT  ")
		   .append("     sum(m.amount * m.exchangerate) AS amount, ") 
		   .append("     '¥' AS currency_mark ")
		   .append("     FROM  ")
		   .append("     money_amount m ")
		   .append("     WHERE  ")
		   .append("     	 EXISTS (  ")
		   .append("             SELECT  ")
		   .append("             o.accounted_money  ")
		   .append("             FROM  ")
		   .append("             visa_order o,  ")
		   .append("             visa_products p  ")
		   .append("             WHERE  ")
		   .append("             o.visa_product_id = p.id ")
		   .append("             AND p.proCompanyId = ").append(currentCompanyId)
		   .append("             AND o.del_flag = '0' ").append(" AND p.delFlag = '0' ")
		   .append("             AND o.payStatus not in (99,111) AND o.visa_order_status <> 100 ")
		   .append("             AND p.productStatus = 2 ")
		   .append("             AND o.accounted_money IS NOT NULL  ")
		   .append("             AND o.accounted_money = m.serialNum  ").append(where.toString())
		   .append("             )  ");
		return findBySql(str.toString(), Map.class);
	}

	@Override
	public List<Map<String, Object>> getAirticketOrderRealReceiveSumMoney(GroupManagerEntity entity) {
		StringBuffer str = new StringBuffer();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		StringBuffer where = new StringBuffer();
		if(StringUtils.isNotBlank(entity.getGroupCode())){
			where.append(" and p.group_code like '%").append(entity.getGroupCode()).append("%' ");
		}
		if(StringUtils.isNotBlank(entity.getProductName())){
			where.append(" and p.activity_airticket_name like '%").append(entity.getProductName()).append("%' ");
		}
		if(null != entity.getOperatorId()){
			where.append(" and p.createBy = ").append(entity.getOperatorId());
		}
		if(null != entity.getSalerId()){
			where.append(" and o.salerId = ").append(entity.getSalerId());
		}
		if(StringUtils.isNotBlank(entity.getIscommission())){
			where.append(" and p.iscommission = ").append(entity.getIscommission());
		}
		if(null != entity.getDeptId()){
			where.append(" and p.deptId = ").append(entity.getDeptId());
		}
		if(StringUtils.isNotBlank(entity.getGroupOpenDate())){
			where.append(" and p.outTicketTime >= '").append(entity.getGroupOpenDate()).append("'");
		}
		if(StringUtils.isNotBlank(entity.getGroupCloseDate())){
			where.append(" and p.outTicketTime <= '").append(entity.getGroupCloseDate()).append("'");
		}
		str.append(" SELECT  ")
		   .append("     sum(m.amount * m.exchangerate) AS amount,  ")
		   .append("     '¥' AS currency_mark  ")
		   .append("     FROM  ")
		   .append("     money_amount m ")
		   .append("     WHERE  ")
		   .append("     	 EXISTS (  ")
		   .append("             SELECT  ")
		   .append("             o.accounted_money  ")
		   .append("             FROM  ")
		   .append("             airticket_order o,  ")
		   .append("             activity_airticket p  ")
		   .append("             WHERE  ")
		   .append("             o.airticket_id = p.id ")
		   .append("             AND p.proCompany = ").append(currentCompanyId)
		   .append("             AND o.del_flag = '0' ")
		   .append("             AND p.delflag = '0' ")
		   .append("             AND o.order_state not in (99,111) ")
		   .append("             AND p.productStatus = 2 ")
		   .append("             AND o.accounted_money IS NOT NULL  ")
		   .append("             AND o.accounted_money = m.serialNum  ").append(where.toString())
		   .append("             )  ");
		return findBySql(str.toString(), Map.class);
	}

	@Override
	public List<Map<String, Object>> getSPReserveReceiveSumMoney(GroupManagerEntity entity) {
		StringBuffer str = new StringBuffer();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		StringBuffer where = new StringBuffer();
		if(StringUtils.isNotBlank(entity.getGroupCode())){
			where.append(" and g.groupCode like '%").append(entity.getGroupCode()).append("%' ");
		}
		if(StringUtils.isNotBlank(entity.getProductName())){
			where.append(" and p.acitivityName like '%").append(entity.getProductName()).append("%' ");
		}
		if(null != entity.getOperatorId()){
			where.append(" and g.createBy = ").append(entity.getOperatorId());
		}
		/**if(null != entity.getSalerId()){
			where.append(" and o.createBy = ").append(entity.getSalerId());
		}*/
		if(StringUtils.isNotBlank(entity.getIscommission())){
			where.append(" and g.iscommission = ").append(entity.getIscommission());
		}
		if(null != entity.getDeptId()){
			where.append(" and p.deptId = ").append(entity.getDeptId());
		}
		if(StringUtils.isNotBlank(entity.getGroupOpenDate())){
			where.append(" and g.groupOpenDate >= '").append(entity.getGroupOpenDate()).append("'");
		}
		if(StringUtils.isNotBlank(entity.getGroupCloseDate())){
			where.append(" and g.groupOpenDate <= '").append(entity.getGroupCloseDate()).append("'");
		}
		str.append(" SELECT ")
		   .append("     sum(payMoney) as amount, ")
		   .append("     '¥' AS currency_mark ")
		   .append("  FROM ")
		   .append("     activityreserveorder ao, ")
		   .append("     travelactivity p, ")
		   .append("     activitygroup g ")
		   .append("  WHERE ")
		   .append("     ao.srcActivityId = p.id ")
		   .append("   AND p.id = g.srcActivityId ")
		   .append("   AND p.activityStatus = 2 ")
		   .append("   AND reserveType = 0 ")
		   .append("   AND p.proCompany = ").append(currentCompanyId)
		   .append("   AND ao.confirm = 1 ")
		   .append("   AND p.delFlag = '0' ").append(where.toString());
		return findBySql(str.toString(), Map.class);
	}
	
	@Override
	public List<Map<String, Object>> getAirticketReserveReceiveSumMoney(GroupManagerEntity entity) {
		StringBuffer str = new StringBuffer();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		StringBuffer where = new StringBuffer();
		if(StringUtils.isNotBlank(entity.getGroupCode())){
			where.append(" and p.group_code like '%").append(entity.getGroupCode()).append("%' ");
		}
		if(StringUtils.isNotBlank(entity.getProductName())){
			where.append(" and p.activity_airticket_name like '%").append(entity.getProductName()).append("%' ");
		}
		if(null != entity.getOperatorId()){
			where.append(" and p.createBy = ").append(entity.getOperatorId());
		}
		/**if(null != entity.getSalerId()){
			where.append(" and o.create_by = ").append(entity.getSalerId());
		}*/
		if(StringUtils.isNotBlank(entity.getIscommission())){
			where.append(" and p.iscommission = ").append(entity.getIscommission());
		}
		if(null != entity.getDeptId()){
			where.append(" and p.deptId = ").append(entity.getDeptId());
		}
		if(StringUtils.isNotBlank(entity.getGroupOpenDate())){
			where.append(" and p.outTicketTime >= '").append(entity.getGroupOpenDate()).append("'");
		}
		if(StringUtils.isNotBlank(entity.getGroupCloseDate())){
			where.append(" and p.outTicketTime <= '").append(entity.getGroupCloseDate()).append("'");
		}
		str.append(" SELECT ")
		   .append("     sum(payMoney) as amount, ")
		   .append("     '¥' AS currency_mark ")
		   .append("  FROM ")
		   .append("     activityreserveorder ao, ")
		   .append("     activity_airticket p")
		   .append("  WHERE ")
		   .append("     ao.srcActivityId = p.id ")
		   .append("   AND reserveType = 1 ")
		   .append("   AND p.proCompany = ").append(currentCompanyId)
		   .append("   AND ao.confirm = 1 ")
		   .append("   AND p.productStatus = 2 ")
		   .append("   AND p.delFlag = '0' ").append(where.toString());
		return findBySql(str.toString(), Map.class);
	}

	@Override
	public List<Map<String, Object>> getActivityOtherIncomeRecevieSumMoney(GroupManagerEntity entity, Integer orderType) {
		StringBuffer str = new StringBuffer();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		String orderTypeWhere = "";
		if(Context.ORDER_TYPE_ALL == orderType){
			orderTypeWhere = " AND pay.orderType in (1,2,3,4,5,10) ";
		}else{
			orderTypeWhere = " AND pay.orderType = " + orderType;
		}
		StringBuffer where = new StringBuffer();
		if(StringUtils.isNotBlank(entity.getGroupCode())){
			where.append(" and g.groupCode like '%").append(entity.getGroupCode()).append("%' ");
		}
		if(StringUtils.isNotBlank(entity.getProductName())){
			where.append(" and p.acitivityName like '%").append(entity.getProductName()).append("%' ");
		}
		if(null != entity.getOperatorId()){
			where.append(" and g.createBy = ").append(entity.getOperatorId());
		}
		/**if(null != entity.getSalerId()){
			where.append(" and o.createBy = ").append(entity.getSalerId());
		}*/
		if(StringUtils.isNotBlank(entity.getIscommission())){
			where.append(" and g.iscommission = ").append(entity.getIscommission());
		}
		if(null != entity.getDeptId()){
			where.append(" and p.deptId = ").append(entity.getDeptId());
		}
		if(StringUtils.isNotBlank(entity.getGroupOpenDate())){
			where.append(" and g.groupOpenDate >= '").append(entity.getGroupOpenDate()).append("'");
		}
		if(StringUtils.isNotBlank(entity.getGroupCloseDate())){
			where.append(" and g.groupOpenDate <= '").append(entity.getGroupCloseDate()).append("'");
		}
		str.append(" SELECT ")
		   .append("     sum(m.amount * m.exchangerate) AS amount, ")
		   .append("     '¥' AS currency_mark ")
		   .append("     FROM ")
		   .append("     pay_group pay, ")
		   .append("     money_amount m ")
		   .append("     WHERE ")
		   .append("     pay.payPrice = m.serialNum ")
		   .append("     AND pay.isAsAccount = 1 ")
		   .append(orderTypeWhere)
		   .append("     AND EXISTS ( ")
		   .append("             SELECT ")
		   .append("             c.id ")
		   .append("             FROM ")
		   .append("             cost_record c, ")
		   .append("             activitygroup g, ")
		   .append("             travelactivity p ")
		   .append("             WHERE ")
		   .append("             c.budgetType = '2' ")
		   .append("             AND c.delFlag = '0' ")
		   .append("             AND p.delFlag = '0' ")
	       .append("             AND c.activityId = g.id ")
		   .append("             AND g.srcActivityId = p.id ")
		   .append("   			 AND p.activityStatus = 2 ")
		   .append("             AND p.proCompany = ").append(currentCompanyId)
		   .append("             AND c.id = pay.cost_record_id ")
		   .append("             AND pay.orderType = c.orderType ").append(where.toString())
		   .append("             ) ");
		return findBySql(str.toString(), Map.class);
	}

	@Override
	public List<Map<String, Object>> getAirticketOtherIncomeRecevieSumMoney(GroupManagerEntity entity) {
		StringBuffer str = new StringBuffer();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		StringBuffer where = new StringBuffer();
		if(StringUtils.isNotBlank(entity.getGroupCode())){
			where.append(" and p.group_code like '%").append(entity.getGroupCode()).append("%' ");
		}
		if(StringUtils.isNotBlank(entity.getProductName())){
			where.append(" and p.activity_airticket_name like '%").append(entity.getProductName()).append("%' ");
		}
		if(null != entity.getOperatorId()){
			where.append(" and p.createBy = ").append(entity.getOperatorId());
		}
		/**if(null != entity.getSalerId()){
			where.append(" and o.create_by = ").append(entity.getSalerId());
		}*/
		if(StringUtils.isNotBlank(entity.getIscommission())){
			where.append(" and p.iscommission = ").append(entity.getIscommission());
		}
		if(null != entity.getDeptId()){
			where.append(" and p.deptId = ").append(entity.getDeptId());
		}
		if(StringUtils.isNotBlank(entity.getGroupOpenDate())){
			where.append(" and p.outTicketTime >= '").append(entity.getGroupOpenDate()).append("'");
		}
		if(StringUtils.isNotBlank(entity.getGroupCloseDate())){
			where.append(" and p.outTicketTime <= '").append(entity.getGroupCloseDate()).append("'");
		}
		str.append(" SELECT ")
				.append("     sum(m.amount * m.exchangerate) AS amount, ")
		   .append("     '¥' AS currency_mark ")
		   .append("     FROM ")
		   .append("     pay_group pay, ")
		   .append("     money_amount m ")
		   .append("     WHERE ")
		   .append("     pay.payPrice = m.serialNum ")
		   .append("     AND pay.isAsAccount = 1 ")
				.append("     AND pay.orderType = ").append(Context.ORDER_TYPE_JP)
		   .append("     AND EXISTS ( ")
		   .append("             SELECT ")
		   .append("             c.id ")
		   .append("             FROM ")
		   .append("             cost_record c, ")
		   .append("             activity_airticket p ")
		   .append("             WHERE ")
		   .append("             c.budgetType = '2' ")
		   .append("             AND c.delFlag = '0' ")
		   .append("             AND c.activityId = p.id ")
		   .append("             AND p.proCompany = ").append(currentCompanyId)
				.append("             AND c.id = pay.cost_record_id ")
		   .append("   			 AND p.productStatus = 2 ")
		   .append("   			 AND p.delflag = '0' ")
				.append("             AND pay.orderType = c.orderType ").append(where.toString())
		   .append("             ) ");
		return findBySql(str.toString(), Map.class);
	}

	@Override
	public List<Map<String, Object>> getVisaOtherIncomeRecevieSumMoney(GroupManagerEntity entity) {
		StringBuffer str = new StringBuffer();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		StringBuffer where = new StringBuffer();
		if(StringUtils.isNotBlank(entity.getGroupCode())){
			where.append(" and p.groupCode like '%").append(entity.getGroupCode()).append("%' ");
		}
		if(StringUtils.isNotBlank(entity.getProductName())){
			where.append(" and p.productName like '%").append(entity.getProductName()).append("%' ");
		}
		if(null != entity.getOperatorId()){
			where.append(" and p.createBy = ").append(entity.getOperatorId());
		}
		/**if(null != entity.getSalerId()){
			where.append(" and o.create_by = ").append(entity.getSalerId());
		}*/
		if(StringUtils.isNotBlank(entity.getIscommission())){
			where.append(" and p.iscommission = ").append(entity.getIscommission());
		}
		if(null != entity.getDeptId()){
			where.append(" and p.deptId = ").append(entity.getDeptId());
		}
		if(StringUtils.isNotBlank(entity.getGroupOpenDate())){
			where.append(" and p.createDate >= '").append(entity.getGroupOpenDate()).append("'");
		}
		if(StringUtils.isNotBlank(entity.getGroupCloseDate())){
			where.append(" and p.createDate <= '").append(entity.getGroupCloseDate()).append("'");
		}
		str.append(" SELECT ")
		   .append("     sum(m.amount * m.exchangerate) AS amount, ")
		   .append("     '¥' AS currency_mark ")
		   .append("     FROM ")
		   .append("     pay_group pay, ")
		   .append("     money_amount m ")
		   .append("     WHERE ")
		   .append("     pay.payPrice = m.serialNum ")
		   .append("     AND pay.isAsAccount = 1 ")
		   .append("     AND pay.orderType = ").append(Context.ORDER_TYPE_QZ)
		   .append("     AND EXISTS ( ")
		   .append("             SELECT ")
		   .append("             c.id ")
		   .append("             FROM ")
		   .append("             cost_record c, ")
		   .append("             visa_products p ")
		   .append("             WHERE ")
		   .append("             c.budgetType = '2' ")
		   .append("             AND c.delFlag = '0' ")
		   .append("             AND c.activityId = p.id ")
		   .append("             AND p.productStatus = 2 ")
		   .append("             AND p.delFlag = 0 ")
		   .append("             AND p.proCompanyId = ").append(currentCompanyId)
		   .append("             AND c.id = pay.cost_record_id ")
		   .append("             AND pay.orderType = c.orderType ").append(where.toString())
		   .append("             ) ")
		   .append("     GROUP BY ")
		   .append("     currency_mark ");
		return findBySql(str.toString(), Map.class);
	}

	@Override
	public List<Map<String, Object>> getActivityCostPayedSumMoney(GroupManagerEntity entity, Integer orderType) {
		StringBuffer str = new StringBuffer();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		String orderTypeWhere = "";
		if(Context.ORDER_TYPE_ALL != orderType){
			orderTypeWhere = " AND r.orderType = " + orderType;
		}else{
			orderTypeWhere = " AND r.orderType in (1,2,3,4,5,10) ";
		}
		StringBuffer where = new StringBuffer();
		if(StringUtils.isNotBlank(entity.getGroupCode())){
			where.append(" and g.groupCode like '%").append(entity.getGroupCode()).append("%' ");
		}
		if(StringUtils.isNotBlank(entity.getProductName())){
			where.append(" and p.acitivityName like '%").append(entity.getProductName()).append("%' ");
		}
		if(null != entity.getOperatorId()){
			where.append(" and g.createBy = ").append(entity.getOperatorId());
		}
		/*if(null != entity.getSalerId()){
			where.append(" and o.createBy = ").append(entity.getSalerId());
		}*/
		if(StringUtils.isNotBlank(entity.getIscommission())){
			where.append(" and g.iscommission = ").append(entity.getIscommission());
		}
		if(null != entity.getDeptId()){
			where.append(" and p.deptId = ").append(entity.getDeptId());
		}
		if(StringUtils.isNotBlank(entity.getGroupOpenDate())){
			where.append(" and g.groupOpenDate >= '").append(entity.getGroupOpenDate()).append("'");
		}
		if(StringUtils.isNotBlank(entity.getGroupCloseDate())){
			where.append(" and g.groupOpenDate <= '").append(entity.getGroupCloseDate()).append("'");
		}
		str.append(" SELECT ")
		   .append("     sum(m.amount * m.exchangerate) AS amount, ")
		   .append("     '¥' as currency_mark ")
		   .append("     FROM ")
		   .append("     money_amount m ")
		   .append("     WHERE ")
		   .append("     	 EXISTS ( ")
		   .append("             SELECT ")
		   .append("             money_serial_num ")
		   .append("             FROM ")
		   .append("             refund r ")
		   .append("             WHERE ")
		   .append("             moneyType = ").append(Refund.MONEY_TYPE_COST)
		   .append("             AND `status` IS NULL ")
		   .append("             AND del_flag = 0 ")
		   .append("             AND r.money_serial_num = m.serialNum ")
		   .append("             AND EXISTS ( ")
		   .append("                 SELECT ")
		   .append("                 c.id ")
		   .append("                 FROM ")
		   .append("                 cost_record c, ")
		   .append("                 activitygroup g, ")
		   .append("                 travelactivity p ")
		   .append("                 WHERE ")
		   .append("                 g.srcActivityId = p.id ")
		   .append("                 AND c.activityId = g.id ")
		   .append("                 AND c.delFlag = '0' ")
		   .append("                 AND c.budgetType = 1 ")
		   .append("                 AND c.payReview = 2 ")
		   .append("                 AND c.reviewType = 0 ")
		   .append("                 AND c.id = r.record_id ")
		   .append("                 AND r.orderType = c.orderType ")
		   .append("                 AND c.orderType = p.activity_kind ")
		   .append("                 AND p.proCompany = ").append(currentCompanyId).append(where.toString())
		   .append("                 ) ")
		   .append(orderTypeWhere).append(" ) ");
		return findBySql(str.toString(), Map.class);
	}

	@Override
	public List<Map<String, Object>> getAirticketCostPayedSumMoney(GroupManagerEntity entity) {
		StringBuffer str = new StringBuffer();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		StringBuffer where = new StringBuffer();
		if(StringUtils.isNotBlank(entity.getGroupCode())){
			where.append(" and p.group_code like '%").append(entity.getGroupCode()).append("%' ");
		}
		if(StringUtils.isNotBlank(entity.getProductName())){
			where.append(" and p.activity_airticket_name like '%").append(entity.getProductName()).append("%' ");
		}
		if(null != entity.getOperatorId()){
			where.append(" and p.createBy = ").append(entity.getOperatorId());
		}
		/**if(null != entity.getSalerId()){
			where.append(" and o.create_by = ").append(entity.getSalerId());
		}*/
		if(StringUtils.isNotBlank(entity.getIscommission())){
			where.append(" and p.iscommission = ").append(entity.getIscommission());
		}
		if(null != entity.getDeptId()){
			where.append(" and p.deptId = ").append(entity.getDeptId());
		}
		if(StringUtils.isNotBlank(entity.getGroupOpenDate())){
			where.append(" and p.outTicketTime >= '").append(entity.getGroupOpenDate()).append("'");
		}
		if(StringUtils.isNotBlank(entity.getGroupCloseDate())){
			where.append(" and p.outTicketTime <= '").append(entity.getGroupCloseDate()).append("'");
		}
		str.append(" SELECT ")
		   .append("     sum(m.amount * m.exchangerate) AS amount, ")
		   .append("     '¥' as currency_mark ")
		   .append("     FROM ")
		   .append("     money_amount m ")
		   .append("     WHERE ")
		   .append("      EXISTS ( ")
		   .append("             SELECT ")
		   .append("             money_serial_num ")
		   .append("             FROM ")
		   .append("             refund r ")
		   .append("             WHERE ")
		   .append("             moneyType = ").append(Refund.MONEY_TYPE_COST)
		   .append("             AND `status` IS NULL ")
		   .append("             AND del_flag = 0 ")
		   .append("             AND r.money_serial_num = m.serialNum ")
		   .append("             AND EXISTS ( ")
		   .append("                 SELECT ")
		   .append("                 c.id ")
		   .append("                 FROM ")
		   .append("                 cost_record c, ")
		   .append("                 activity_airticket p ")
		   .append("                 WHERE ")
		   .append("                 c.activityId = p.id ")
		   .append("                 AND c.delFlag = '0' ")
		   .append("                 AND c.budgetType = 1 ")
		   .append("                 AND c.payReview = 2 ")
		   .append("                 AND c.reviewType = 0 ")
		   .append("                 AND c.id = r.record_id ")
		   .append("                 AND r.orderType = c.orderType ")
		   .append("                 AND p.proCompany = ").append(currentCompanyId).append(where.toString())
		   .append("                 ) ")
		   .append("             AND r.orderType = ").append(Context.ORDER_TYPE_JP)
		   .append("             ) ");
		return findBySql(str.toString(), Map.class);
	}

	@Override
	public List<Map<String, Object>> getVisaCostPayedSumMoney(GroupManagerEntity entity) {
		StringBuffer str = new StringBuffer();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		StringBuffer where = new StringBuffer();
		if(StringUtils.isNotBlank(entity.getGroupCode())){
			where.append(" and p.groupCode like '%").append(entity.getGroupCode()).append("%' ");
		}
		if(StringUtils.isNotBlank(entity.getProductName())){
			where.append(" and p.productName like '%").append(entity.getProductName()).append("%' ");
		}
		if(null != entity.getOperatorId()){
			where.append(" and p.createBy = ").append(entity.getOperatorId());
		}
		/**if(null != entity.getSalerId()){
			where.append(" and o.create_by = ").append(entity.getSalerId());
		}*/
		if(StringUtils.isNotBlank(entity.getIscommission())){
			where.append(" and p.iscommission = ").append(entity.getIscommission());
		}
		if(null != entity.getDeptId()){
			where.append(" and p.deptId = ").append(entity.getDeptId());
		}
		if(StringUtils.isNotBlank(entity.getGroupOpenDate())){
			where.append(" and p.createDate >= '").append(entity.getGroupOpenDate()).append("'");
		}
		if(StringUtils.isNotBlank(entity.getGroupCloseDate())){
			where.append(" and p.createDate <= '").append(entity.getGroupCloseDate()).append("'");
		}
		str.append(" SELECT ")
		   .append("     sum(m.amount * m.exchangerate) AS amount, ")
		   .append("     '¥' AS currency_mark ")
		   .append("     FROM ")
		   .append("     money_amount m ")
		   .append("     WHERE ")
		   .append("     	 EXISTS ( ")
		   .append("             SELECT ")
		   .append("             money_serial_num ")
		   .append("             FROM ")
		   .append("             refund r ")
		   .append("             WHERE ")
		   .append("             moneyType = ").append(Refund.MONEY_TYPE_COST)
		   .append("             AND `status` IS NULL ")
		   .append("             AND del_flag = 0 ")
		   .append("             AND r.money_serial_num = m.serialNum ")
		   .append("             AND EXISTS ( ")
		   .append("                 SELECT ")
		   .append("                 c.id ")
		   .append("                 FROM ")
		   .append("                 cost_record c, ")
		   .append("                 visa_products p ")
		   .append("                 WHERE ")
		   .append("                 c.activityId = p.id ")
		   .append("                 AND c.delFlag = '0' ")
		   .append("                 AND c.budgetType = 1 ")
		   .append("                 AND c.payReview = 2 ")
		   .append("                 AND c.reviewType = 0 ")
		   .append("                 AND c.id = r.record_id ")
		   .append("                 AND r.orderType = c.orderType ")
		   .append("                 AND p.delFlag=0 ")
		   .append("                 AND p.proCompanyId = ").append(currentCompanyId).append(where.toString())
		   .append("                 ) ")
		   .append("              AND r.orderType = ").append(Context.ORDER_TYPE_QZ)
		   .append("             ) ");
		return findBySql(str.toString(), Map.class);
	}

	@Override
	public List<Map<String, Object>> getActivityRefundPayedSumMoney(GroupManagerEntity entity, Integer orderType) {
		StringBuffer str = new StringBuffer();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		String orderTypeWhere = "";
		if(Context.ORDER_TYPE_ALL != orderType){
			orderTypeWhere = " AND r.orderType = " + orderType;
		}else {
			orderTypeWhere = " AND r.orderType in (1,2,3,4,5,10) ";
		}
		StringBuffer where = new StringBuffer();
		if(StringUtils.isNotBlank(entity.getGroupCode())){
			where.append(" and g.groupCode like '%").append(entity.getGroupCode()).append("%' ");
		}
		if(StringUtils.isNotBlank(entity.getProductName())){
			where.append(" and p.acitivityName like '%").append(entity.getProductName()).append("%' ");
		}
		if(null != entity.getOperatorId()){
			where.append(" and g.createBy = ").append(entity.getOperatorId());
		}
		if(null != entity.getSalerId()){
			where.append(" and o.salerId = ").append(entity.getSalerId());
		}
		if(StringUtils.isNotBlank(entity.getIscommission())){
			where.append(" and g.iscommission = ").append(entity.getIscommission());
		}
		if(null != entity.getDeptId()){
			where.append(" and p.deptId = ").append(entity.getDeptId());
		}
		if(StringUtils.isNotBlank(entity.getGroupOpenDate())){
			where.append(" and g.groupOpenDate >= '").append(entity.getGroupOpenDate()).append("'");
		}
		if(StringUtils.isNotBlank(entity.getGroupCloseDate())){
			where.append(" and g.groupOpenDate <= '").append(entity.getGroupCloseDate()).append("'");
		}
		str.append(" SELECT ")
		   .append(" 	SUM(amount) AS amount, ")
		   .append(" 	currency_mark ")
		   .append(" FROM ")
		   .append(" 	( ")
		   .append(" 		SELECT ")
		   .append(" 			sum(m.amount * m.exchangerate) AS amount, ")
		   .append(" 			'¥' AS currency_mark ")
		   .append(" 		FROM ")
		   .append(" 			money_amount m ")
		   .append(" 		WHERE ")
		   .append(" 			EXISTS ( ")
		   .append(" 			SELECT ")
		   .append(" 				r.money_serial_num ")
		   .append(" 			FROM ")
		   .append(" 				refund r ")
		   .append(" 			WHERE ")
		   .append(" 				r.del_flag = 0 ")
		   .append(" 			AND r.`status` IS NULL ")
		   .append(" 			AND r.moneyType = ").append(Refund.MONEY_TYPE_RETURNMONEY)
		   .append(" 			AND r.money_serial_num = m.serialNum ")
		   .append("            ").append(orderTypeWhere)
		   .append(" 			AND EXISTS ( ")
		   .append(" 				SELECT ")
		   .append(" 					rr.id ")
		   .append(" 				FROM ")
		   .append(" 					review rr, ")
		   .append(" 					productorder o, ")
		   .append(" 					activitygroup g,")
		   .append(" 					travelactivity p ")
		   .append(" 				WHERE ")
		   .append(" 					p.id = g.srcActivityId ")
		   .append(" 				  AND o.productGroupId = g.id ")
		   .append(" 				  AND o.productId = p.id ")
		   .append(" 				  AND rr.orderId = o.id ")
		   .append(" 				  AND rr.productType = p.activity_kind ")
		   .append("             	  AND o.payStatus NOT IN (99, 111) ")
		   .append("             	  AND p.activityStatus = 2 ")
		   .append(" 			    AND rr.companyId = ").append(currentCompanyId)
		   .append(" 				AND rr.flowType = ").append(Context.REVIEW_FLOWTYPE_REFUND)
		   .append(" 				AND rr.active = 1 ")
		   .append(" 				AND rr.`status` IN (2, 3) ")
		   .append(" 				AND r.record_id = rr.id ")
		   .append(" 				AND r.orderType = rr.productType ").append(where.toString())
		   .append(" 			) ")
		   .append(" 		) group by currency_mark ")
		   .append(" 		UNION ")
		   .append(" 			SELECT ")
		   .append(" 				sum(m.amount * m.exchangerate) AS amount, ")
		   .append(" 				'¥' AS currency_mark ")
		   .append(" 			FROM ")
		   .append(" 				money_amount m ")
		   .append(" 			WHERE ")
		   .append(" 				EXISTS ( ")
		   .append(" 				SELECT ")
		   .append(" 					r.money_serial_num ")
		   .append(" 				FROM ")
		   .append(" 					refund r ")
		   .append(" 				WHERE ")
		   .append(" 					r.del_flag = 0 ")
		   .append(" 				AND r.`status` IS NULL ")
		   .append(" 				AND r.moneyType = ").append(Refund.MONEY_TYPE_NEWRETURNMONEY)
		   .append(" 				AND r.money_serial_num = m.serialNum ")
		   .append("            ").append(orderTypeWhere)
		   .append(" 				AND EXISTS ( ")
		   .append(" 					SELECT ")
		   .append(" 						rr.id ")
		   .append(" 					FROM ")
		   .append(" 						review_new rr, ")
		   .append(" 						productorder o, ")
		   .append(" 						activitygroup g,")
		   .append(" 						travelactivity p ")
		   .append(" 					WHERE ")
		   .append("  					   p.id = g.srcActivityId ")
		   .append(" 				  	  AND o.productGroupId = g.id ")
		   .append(" 				      AND o.productId = p.id ")
		   .append(" 				      AND rr.order_id = o.id ")
		   .append(" 				      AND rr.product_type = p.activity_kind ")
		   .append("             	      AND o.payStatus NOT IN (99, 111) ")
		   .append("             	      AND p.activityStatus = 2 ")
		   .append(" 					AND rr.company_id = '").append(currentCompanyUuid).append("'")
		   .append(" 					AND rr.process_type = '").append(Context.REVIEW_FLOWTYPE_REFUND + "' ")
		   .append(" 					AND rr.del_flag = '0' ")
		   .append(" 					AND rr.`status` = ").append(ReviewConstant.REVIEW_STATUS_PASSED)
		   .append(" 					AND r.record_id = rr.id_long ")
		   .append(" 					AND r.orderType = rr.product_type ").append(where.toString())
		   .append(" 				) ")
		   .append(" 			) group by currency_mark ")
		   .append(" 	) t ")
		   .append(" GROUP BY ")
		   .append(" 	currency_mark ");
		return findBySql(str.toString(), Map.class);
	}

	@Override
	public List<Map<String, Object>> getAirticketRefundPayedSumMoney(GroupManagerEntity entity) {
		StringBuffer str = new StringBuffer();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		StringBuffer where = new StringBuffer();
		if(StringUtils.isNotBlank(entity.getGroupCode())){
			where.append(" and p.group_code like '%").append(entity.getGroupCode()).append("%' ");
		}
		if(StringUtils.isNotBlank(entity.getProductName())){
			where.append(" and p.activity_airticket_name like '%").append(entity.getProductName()).append("%' ");
		}
		if(null != entity.getOperatorId()){
			where.append(" and p.createBy = ").append(entity.getOperatorId());
		}
		if(null != entity.getSalerId()){
			where.append(" and o.salerId = ").append(entity.getSalerId());
		}
		if(StringUtils.isNotBlank(entity.getIscommission())){
			where.append(" and p.iscommission = ").append(entity.getIscommission());
		}
		if(null != entity.getDeptId()){
			where.append(" and p.deptId = ").append(entity.getDeptId());
		}
		if(StringUtils.isNotBlank(entity.getGroupOpenDate())){
			where.append(" and p.outTicketTime >= '").append(entity.getGroupOpenDate()).append("'");
		}
		if(StringUtils.isNotBlank(entity.getGroupCloseDate())){
			where.append(" and p.outTicketTime <= '").append(entity.getGroupCloseDate()).append("'");
		}
		
		str.append(" SELECT ")
		   .append(" 	SUM(amount) AS amount, ")
		   .append(" 	currency_mark ")
		   .append(" FROM ")
		   .append(" 	( ")
		   .append(" 		SELECT ")
		   .append(" 			sum(m.amount * m.exchangerate) AS amount, ")
		   .append(" 			'¥' AS currency_mark ")
		   .append(" 		FROM ")
		   .append(" 			money_amount m ")
		   .append(" 		WHERE ")
		   .append(" 		 EXISTS ( ")
		   .append(" 			SELECT ")
		   .append(" 				r.money_serial_num ")
		   .append(" 			FROM ")
		   .append(" 				refund r ")
		   .append(" 			WHERE ")
		   .append(" 				r.del_flag = 0 ")
		   .append(" 			AND r.`status` IS NULL ")
		   .append(" 			AND r.moneyType = ").append(Refund.MONEY_TYPE_RETURNMONEY)
		   .append(" 			AND r.money_serial_num = m.serialNum ")
		   .append("            AND r.orderType =").append(Context.ORDER_TYPE_JP)
		   .append(" 			AND EXISTS ( ")
		   .append(" 				SELECT ")
		   .append(" 					rr.id ")
		   .append(" 				FROM ")
		   .append(" 					review rr, ")
		   .append(" 					airticket_order o, ")
		   .append(" 					activity_airticket p ")
		   .append(" 				WHERE ")
		   .append(" 					 o.airticket_id = p.id ")
		   .append(" 				  AND rr.orderId = o.id ")
		   .append(" 				  AND rr.productType = ").append(Context.ORDER_TYPE_JP)
		   .append("             	  AND o.order_state not in (99,111) ")
		   .append("             	  AND p.productStatus = 2 ")
		   .append(" 			    AND rr.companyId = ").append(currentCompanyId)
		   .append(" 				AND rr.flowType = ").append(Context.REVIEW_FLOWTYPE_REFUND)
		   .append(" 				AND rr.active = 1 ")
		   .append(" 				AND rr.`status` IN (2, 3) ")
		   .append(" 				AND r.record_id = rr.id ")
		   .append(" 				AND r.orderType = rr.productType ").append(where.toString())
		   .append(" 			) ")
		   .append(" 		) group by currency_mark  ")
		   .append(" 		UNION ")
		   .append(" 			SELECT ")
		   .append(" 				sum(m.amount * m.exchangerate) AS amount, ")
		   .append(" 				'¥' AS currency_mark ")
		   .append(" 			FROM ")
		   .append(" 				money_amount m ")
		   .append(" 			WHERE ")
		   .append(" 				EXISTS ( ")
		   .append(" 				SELECT ")
		   .append(" 					r.money_serial_num ")
		   .append(" 				FROM ")
		   .append(" 					refund r ")
		   .append(" 				WHERE ")
		   .append(" 					r.del_flag = 0 ")
		   .append(" 				AND r.`status` IS NULL ")
		   .append(" 				AND r.moneyType = ").append(Refund.MONEY_TYPE_NEWRETURNMONEY)
		   .append(" 				AND r.money_serial_num = m.serialNum ")
		   .append("                AND r.orderType = ").append(Context.ORDER_TYPE_JP)
		   .append(" 				AND EXISTS ( ")
		   .append(" 					SELECT ")
		   .append(" 						rr.id ")
		   .append(" 					FROM ")
		   .append(" 						review_new rr, ")
		   .append(" 						airticket_order o, ")
		   .append(" 						activity_airticket p ")
		   .append(" 					WHERE ")
		   .append("  					   o.airticket_id = p.id ")
		   .append(" 				      AND rr.order_id = o.id ")
		   .append(" 				      AND rr.product_type = ").append(Context.ORDER_TYPE_JP)
		   .append("             	      AND o.order_state not in (99,111) ")
		   .append("             	      AND p.productStatus = 2 ")
		   .append(" 					AND rr.company_id = '").append(currentCompanyUuid).append("'")
		   .append(" 					AND rr.process_type = '").append(Context.REVIEW_FLOWTYPE_REFUND + "' ")
		   .append(" 					AND rr.del_flag = '0' ")
		   .append(" 					AND rr.`status` = ").append(ReviewConstant.REVIEW_STATUS_PASSED)
		   .append(" 					AND r.record_id = rr.id_long ")
		   .append(" 					AND r.orderType = rr.product_type ").append(where.toString())
		   .append(" 				) ")
		   .append(" 			) group by currency_mark ")
		   .append(" 	) t ")
		   .append(" GROUP BY ")
		   .append(" 	currency_mark ");
		return findBySql(str.toString(), Map.class);
	}
	
	@Override
	public List<Map<String, Object>> getAirticketRefundPayedSumMoneyForXXZ(GroupManagerEntity entity) {
		StringBuffer str = new StringBuffer();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		StringBuffer where = new StringBuffer();
		if(StringUtils.isNotBlank(entity.getGroupCode())){
			where.append(" and p.group_code like '%").append(entity.getGroupCode()).append("%' ");
		}
		/**if(StringUtils.isNotBlank(entity.getProductName())){
			where.append(" and p.acitivityName like '%").append(entity.getProductName()).append("%' ");
		}*/
		if(null != entity.getOperatorId()){
			where.append(" and p.createBy = ").append(entity.getOperatorId());
		}
		if(null != entity.getSalerId()){
			where.append(" and o.salerId = ").append(entity.getSalerId());
		}
		if(StringUtils.isNotBlank(entity.getIscommission())){
			where.append(" and p.iscommission = ").append(entity.getIscommission());
		}
		if(null != entity.getDeptId()){
			where.append(" and p.deptId = ").append(entity.getDeptId());
		}
		if(StringUtils.isNotBlank(entity.getGroupOpenDate())){
			where.append(" and p.outTicketTime >= '").append(entity.getGroupOpenDate()).append("'");
		}
		if(StringUtils.isNotBlank(entity.getGroupCloseDate())){
			where.append(" and p.outTicketTime <= '").append(entity.getGroupCloseDate()).append("'");
		}
		str.append(" SELECT ")
		   .append(" 	SUM(amount) AS amount, ")
		   .append(" 	currency_mark ")
		   .append(" FROM ")
		   .append(" 	( ")
		   .append(" 		SELECT ")
		   .append(" 			sum(m.amount * m.exchangerate) AS amount, ")
		   .append(" 			'¥' as currency_mark ")
		   .append(" 		FROM ")
		   .append(" 			money_amount m ")
		   .append(" 		WHERE ")
		   .append(" 			EXISTS ( ")
		   .append(" 			SELECT ")
		   .append(" 				r.money_serial_num ")
		   .append(" 			FROM ")
		   .append(" 				refund r ")
		   .append(" 			WHERE ")
		   .append(" 				r.del_flag = 0 ")
		   .append(" 			AND r.`status` IS NULL ")
		   .append(" 			AND r.moneyType = ").append(Refund.MONEY_TYPE_RETURNMONEY)
		   .append(" 			AND r.money_serial_num = m.serialNum ")
		   .append("            AND r.orderType = ").append(Context.ORDER_TYPE_JP)
		   .append(" 			AND EXISTS ( ")
		   .append(" 				SELECT ")
		   .append(" 					rr.id ")
		   .append(" 				FROM ")
		   .append(" 					review rr, ")
		   .append(" 					airticket_order o, ")
		   .append(" 					activity_airticket p ")
		   .append(" 				WHERE ")
		   .append(" 					 o.airticket_id = p.id ")
		   .append(" 				  AND rr.orderId = o.id ")
		   .append(" 				  AND rr.productType = ").append(Context.ORDER_TYPE_JP)
		   .append("             	  AND o.order_state not in (99,111) ")
		   .append("             	  AND p.productStatus = 2 ")
		   .append(" 				AND rr.companyId = ").append(currentCompanyId)
		   .append(" 				AND rr.flowType = ").append(Context.REVIEW_FLOWTYPE_OPER_REFUND)
		   .append(" 				AND rr.active = 1 ")
		   .append(" 				AND rr.`status` IN (2, 3) ")
		   .append(" 				AND r.record_id = rr.id ")
		   .append(" 				AND r.orderType = rr.productType ").append(where.toString())
		   .append(" 			) ")
		   .append(" 		) group by currency_mark ")
		   .append(" 	) t ")
		   .append(" GROUP BY ")
		   .append(" 	currency_mark ");
		return findBySql(str.toString(), Map.class);
	}

	@Override
	public List<Map<String, Object>> getVisaRefundPayedSumMoney(GroupManagerEntity entity) {
		StringBuffer str = new StringBuffer();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		StringBuffer where = new StringBuffer();
		if(StringUtils.isNotBlank(entity.getGroupCode())){
			where.append(" and p.groupCode like '%").append(entity.getGroupCode()).append("%' ");
		}
		if(StringUtils.isNotBlank(entity.getProductName())){
			where.append(" and p.productName like '%").append(entity.getProductName()).append("%' ");
		}
		if(null != entity.getOperatorId()){
			where.append(" and p.createBy = ").append(entity.getOperatorId());
		}
		if(null != entity.getSalerId()){
			where.append(" and o.salerId = ").append(entity.getSalerId());
		}
		if(StringUtils.isNotBlank(entity.getIscommission())){
			where.append(" and p.iscommission = ").append(entity.getIscommission());
		}
		if(null != entity.getDeptId()){
			where.append(" and p.deptId = ").append(entity.getDeptId());
		}
		if(StringUtils.isNotBlank(entity.getGroupOpenDate())){
			where.append(" and p.createDate >= '").append(entity.getGroupOpenDate()).append("'");
		}
		if(StringUtils.isNotBlank(entity.getGroupCloseDate())){
			where.append(" and p.createDate <= '").append(entity.getGroupCloseDate()).append("'");
		}
		str.append(" SELECT ")
		   .append(" 	SUM(amount) AS amount, ")
		   .append(" 	currency_mark ")
		   .append(" FROM ")
		   .append(" 	( ")
		   .append(" 		SELECT ")
		   .append(" 			sum(m.amount * m.exchangerate) AS amount, ")
		   .append(" 			'¥' AS currency_mark ")
		   .append(" 		FROM ")
		   .append(" 			money_amount m ")
		   .append(" 		WHERE ")
		   .append(" 			EXISTS ( ")
		   .append(" 			SELECT ")
		   .append(" 				r.money_serial_num ")
		   .append(" 			FROM ")
		   .append(" 				refund r ")
		   .append(" 			WHERE ")
		   .append(" 				r.del_flag = 0 ")
		   .append(" 			AND r.`status` IS NULL ")
		   .append(" 			AND r.moneyType = ").append(Refund.MONEY_TYPE_RETURNMONEY)
		   .append(" 			AND r.money_serial_num = m.serialNum ")
		   .append("            AND r.orderType =").append(Context.ORDER_TYPE_QZ)
		   .append(" 			AND EXISTS ( ")
		   .append(" 				SELECT ")
		   .append(" 					rr.id ")
		   .append(" 				FROM ")
		   .append(" 					review rr, ")
		   .append(" 					visa_order o, ")
		   .append(" 					visa_products p ")
		   .append(" 				WHERE ")
		   .append(" 					o.visa_product_id = p.id  ")
		   .append(" 				   AND p.delFlag=0  ")
		   .append(" 				   AND o.del_flag=0  ")
		   .append(" 				  AND rr.orderId = o.id ")
		   .append(" 				  AND rr.productType = ").append(Context.ORDER_TYPE_QZ)
		   .append("             	  AND o.payStatus not in (99,111) AND o.visa_order_status <> 100 ")
		   .append("             	  AND p.productStatus = 2 ")
		   .append(" 			    AND rr.companyId = ").append(currentCompanyId)
		   .append(" 				AND rr.flowType = ").append(Context.REVIEW_FLOWTYPE_REFUND)
		   .append(" 				AND rr.active = 1 ")
		   .append(" 				AND rr.`status` IN (2, 3) ")
		   .append(" 				AND r.record_id = rr.id ")
		   .append(" 				AND r.orderType = rr.productType ").append(where.toString())
		   .append(" 			) ")
		   .append(" 		) group by currency_mark ")
		   .append(" 		UNION ")
		   .append(" 			SELECT ")
		   .append(" 				sum(m.amount * m.exchangerate) AS amount, ")
		   .append(" 				'¥' AS currency_mark ")
		   .append(" 			FROM ")
		   .append(" 				money_amount m ")
		   .append(" 			WHERE ")
		   .append(" 				EXISTS ( ")
		   .append(" 				SELECT ")
		   .append(" 					r.money_serial_num ")
		   .append(" 				FROM ")
		   .append(" 					refund r ")
		   .append(" 				WHERE ")
		   .append(" 					r.del_flag = 0 ")
		   .append(" 				AND r.`status` IS NULL ")
		   .append(" 				AND r.moneyType = ").append(Refund.MONEY_TYPE_NEWRETURNMONEY)
		   .append(" 				AND r.money_serial_num = m.serialNum ")
		   .append("                AND r.orderType = ").append(Context.ORDER_TYPE_QZ)
		   .append(" 				AND EXISTS ( ")
		   .append(" 					SELECT ")
		   .append(" 						rr.id ")
		   .append(" 					FROM ")
		   .append(" 						review_new rr, ")
		   .append(" 						visa_order o, ")
		   .append(" 						visa_products p ")
		   .append(" 					WHERE ")
		   .append("  					   o.visa_product_id = p.id ")
		   .append("  				  AND  p.delFlag=0 ")
		   .append(" 				       AND o.del_flag=0  ")
		   .append(" 				      AND rr.order_id = o.id ")
		   .append(" 				      AND rr.product_type = ").append(Context.ORDER_TYPE_QZ)
		   .append("             	      AND o.payStatus not in (99,111) AND o.visa_order_status <> 100 ")
		   .append("             	      AND p.productStatus = 2 ")
		   .append(" 					AND rr.company_id = '").append(currentCompanyUuid).append("'")
		   .append(" 					AND rr.process_type = '").append(Context.REVIEW_FLOWTYPE_REFUND + "' ")
		   .append(" 					AND rr.del_flag = '0' ")
		   .append(" 					AND rr.`status` = ").append(ReviewConstant.REVIEW_STATUS_PASSED)
		   .append(" 					AND r.record_id = rr.id_long ")
		   .append(" 					AND r.orderType = rr.product_type ").append(where.toString())
		   .append(" 				) ")
		   .append(" 			) group by currency_mark ")
		   .append(" 	) t ")
		   .append(" GROUP BY ")
		   .append(" 	currency_mark ");
		return findBySql(str.toString(), Map.class);
	}

	@Override
	public List<Map<String, Object>> getActivityRebatePayedSumMoney(GroupManagerEntity entity, Integer orderType) {
		StringBuffer str = new StringBuffer();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		String orderTypeWhere = "";
		if(Context.ORDER_TYPE_ALL != orderType){
			orderTypeWhere = " AND r.orderType = " + orderType;
		}else{
			orderTypeWhere = " AND r.orderType in (1,2,3,4,5,10) ";
		}
		StringBuffer where = new StringBuffer();
		if(StringUtils.isNotBlank(entity.getGroupCode())){
			where.append(" and g.groupCode like '%").append(entity.getGroupCode()).append("%' ");
		}
		if(StringUtils.isNotBlank(entity.getProductName())){
			where.append(" and p.acitivityName like '%").append(entity.getProductName()).append("%' ");
		}
		if(null != entity.getOperatorId()){
			where.append(" and g.createBy = ").append(entity.getOperatorId());
		}
		if(null != entity.getSalerId()){
			where.append(" and o.salerId = ").append(entity.getSalerId());
		}
		if(StringUtils.isNotBlank(entity.getIscommission())){
			where.append(" and g.iscommission = ").append(entity.getIscommission());
		}
		if(null != entity.getDeptId()){
			where.append(" and p.deptId = ").append(entity.getDeptId());
		}
		if(StringUtils.isNotBlank(entity.getGroupOpenDate())){
			where.append(" and g.groupOpenDate >= '").append(entity.getGroupOpenDate()).append("'");
		}
		if(StringUtils.isNotBlank(entity.getGroupCloseDate())){
			where.append(" and g.groupOpenDate <= '").append(entity.getGroupCloseDate()).append("'");
		}
		str.append(" SELECT ")
		   .append(" 	SUM(amount) AS amount, ")
		   .append(" 	currency_mark ")
		   .append(" FROM ")
		   .append(" 	( ")
		   .append(" 		SELECT ")
		   .append(" 			sum(m.amount * m.exchangerate) AS amount, ")
		   .append(" 			'¥' AS currency_mark ")
		   .append(" 		FROM ")
		   .append(" 			money_amount m ")
		   .append(" 		WHERE ")
		   .append(" 			EXISTS ( ")
		   .append(" 			SELECT ")
		   .append(" 				r.money_serial_num ")
		   .append(" 			FROM ")
		   .append(" 				refund r ")
		   .append(" 			WHERE ")
		   .append(" 				r.del_flag = 0 ")
		   .append(" 			AND r.`status` IS NULL ")
		   .append(" 			AND r.moneyType = ").append(Refund.MONEY_TYPE_PAYBACK)
		   .append(" 			AND r.money_serial_num = m.serialNum ")
		   .append("            ").append(orderTypeWhere)
		   .append(" 			AND EXISTS ( ")
		   .append(" 				SELECT ")
		   .append(" 					rr.id ")
		   .append(" 				FROM ")
		   .append(" 					review rr, ")
		   .append(" 					productorder o, ")
		   .append(" 					activitygroup g,")
		   .append(" 					travelactivity p ")
		   .append(" 				WHERE ")
		   .append(" 					p.id = g.srcActivityId ")
		   .append(" 				  AND o.productGroupId = g.id ")
		   .append(" 				  AND o.productId = p.id ")
		   .append(" 				  AND rr.orderId = o.id ")
		   .append(" 				  AND rr.productType = p.activity_kind ")
		   .append("             	  AND o.payStatus NOT IN (99, 111) ")
		   .append("             	  AND p.activityStatus = 2 ")
		   .append(" 			    AND rr.companyId = ").append(currentCompanyId)
		   .append(" 				AND rr.flowType = ").append(Context.REBATES_FLOW_TYPE)
		   .append(" 				AND rr.active = 1 ")
		   .append(" 				AND rr.`status` IN (2, 3) ")
		   .append(" 				AND r.record_id = rr.id ")
		   .append(" 				AND r.orderType = rr.productType ").append(where.toString())
		   .append(" 			) ")
		   .append(" 		) group by currency_mark ")
		   .append(" 		UNION ")
		   .append(" 			SELECT ")
		   .append(" 				sum(m.amount * m.exchangerate) AS amount, ")
		   .append(" 				'¥' AS currency_mark ")
		   .append(" 			FROM ")
		   .append(" 				money_amount m ")
		   .append(" 			WHERE ")
		   .append(" 				EXISTS ( ")
		   .append(" 				SELECT ")
		   .append(" 					r.money_serial_num ")
		   .append(" 				FROM ")
		   .append(" 					refund r ")
		   .append(" 				WHERE ")
		   .append(" 					r.del_flag = 0 ")
		   .append(" 				AND r.`status` IS NULL ")
		   .append(" 				AND r.moneyType = ").append(Refund.MONEY_TYPE_NEWPAYBACK)
		   .append(" 				AND r.money_serial_num = m.serialNum ")
		   .append("            ").append(orderTypeWhere)
		   .append(" 				AND EXISTS ( ")
		   .append(" 					SELECT ")
		   .append(" 						rr.id ")
		   .append(" 					FROM ")
		   .append(" 						review_new rr, ")
		   .append(" 						productorder o, ")
		   .append(" 						activitygroup g,")
		   .append(" 						travelactivity p ")
		   .append(" 					WHERE ")
		   .append("  					   p.id = g.srcActivityId ")
		   .append(" 				  	  AND o.productGroupId = g.id ")
		   .append(" 				      AND o.productId = p.id ")
		   .append(" 				      AND rr.order_id = o.id ")
		   .append(" 				      AND rr.product_type = p.activity_kind ")
		   .append("             	      AND o.payStatus NOT IN (99, 111) ")
		   .append("             	      AND p.activityStatus = 2 ")
		   .append(" 					AND rr.company_id = '").append(currentCompanyUuid).append("'")
		   .append(" 					AND rr.process_type = '").append(Context.REBATES_FLOW_TYPE + "' ")
		   .append(" 					AND rr.del_flag = '0' ")
		   .append(" 					AND rr.`status` = ").append(ReviewConstant.REVIEW_STATUS_PASSED)
		   .append(" 					AND r.record_id = rr.id_long ")
		   .append(" 					AND r.orderType = rr.product_type ").append(where.toString())
		   .append(" 				) ")
		   .append(" 			) group by currency_mark ")
		   .append(" 	) t ")
		   .append(" GROUP BY ")
		   .append(" 	currency_mark ");
		return findBySql(str.toString(), Map.class);
	}

	@Override
	public List<Map<String, Object>> getAirticketRebatePayedSumMoney(GroupManagerEntity entity) {
		StringBuffer str = new StringBuffer();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		StringBuffer where = new StringBuffer();
		if(StringUtils.isNotBlank(entity.getGroupCode())){
			where.append(" and p.group_code like '%").append(entity.getGroupCode()).append("%' ");
		}
		if(StringUtils.isNotBlank(entity.getProductName())){
			where.append(" and p.activity_airticket_name like '%").append(entity.getProductName()).append("%' ");
		}
		if(null != entity.getOperatorId()){
			where.append(" and p.createBy = ").append(entity.getOperatorId());
		}
		if(null != entity.getSalerId()){
			where.append(" and o.salerId = ").append(entity.getSalerId());
		}
		if(StringUtils.isNotBlank(entity.getIscommission())){
			where.append(" and p.iscommission = ").append(entity.getIscommission());
		}
		if(null != entity.getDeptId()){
			where.append(" and p.deptId = ").append(entity.getDeptId());
		}
		if(StringUtils.isNotBlank(entity.getGroupOpenDate())){
			where.append(" and p.outTicketTime >= '").append(entity.getGroupOpenDate()).append("'");
		}
		if(StringUtils.isNotBlank(entity.getGroupCloseDate())){
			where.append(" and p.outTicketTime <= '").append(entity.getGroupCloseDate()).append("'");
		}
		
		str.append(" SELECT ")
		   .append(" 	SUM(amount) AS amount, ")
		   .append(" 	currency_mark ")
		   .append(" FROM ")
		   .append(" 	( ")
		   .append(" 		SELECT ")
		   .append(" 			sum(m.amount * m.exchangerate) AS amount, ")
		   .append(" 			'¥' AS currency_mark ")
		   .append(" 		FROM ")
		   .append(" 			money_amount m ")
		   .append(" 		WHERE ")
		   .append(" 			EXISTS ( ")
		   .append(" 			SELECT ")
		   .append(" 				r.money_serial_num ")
		   .append(" 			FROM ")
		   .append(" 				refund r ")
		   .append(" 			WHERE ")
		   .append(" 				r.del_flag = 0 ")
		   .append(" 			AND r.`status` IS NULL ")
		   .append(" 			AND r.moneyType = ").append(Refund.MONEY_TYPE_PAYBACK)
		   .append(" 			AND r.money_serial_num = m.serialNum ")
		   .append("            AND r.orderType =").append(Context.ORDER_TYPE_JP)
		   .append(" 			AND EXISTS ( ")
		   .append(" 				SELECT ")
		   .append(" 					rr.id ")
		   .append(" 				FROM ")
		   .append(" 					review rr, ")
		   .append(" 					airticket_order o, ")
		   .append(" 					activity_airticket p ")
		   .append(" 				WHERE ")
		   .append(" 					 o.airticket_id = p.id ")
		   .append(" 				  AND rr.orderId = o.id ")
		   .append(" 				  AND rr.productType = ").append(Context.ORDER_TYPE_JP)
		   .append("             	  AND o.order_state not in (99,111) ")
		   .append("             	  AND p.productStatus = 2 ")
		   .append(" 			    AND rr.companyId = ").append(currentCompanyId)
		   .append(" 				AND rr.flowType = ").append(Context.REBATES_FLOW_TYPE)
		   .append(" 				AND rr.active = 1 ")
		   .append(" 				AND rr.`status` IN (2, 3) ")
		   .append(" 				AND r.record_id = rr.id ")
		   .append(" 				AND r.orderType = rr.productType ").append(where.toString())
		   .append(" 			) ")
		   .append(" 		) group by currency_mark ")
		   .append(" 		UNION ")
		   .append(" 			SELECT ")
		   .append(" 				sum(m.amount * m.exchangerate) AS amount, ")
		   .append(" 				'¥' AS currency_mark ")
		   .append(" 			FROM ")
		   .append(" 				money_amount m ")
		   .append(" 			WHERE ")
		   .append(" 				EXISTS ( ")
		   .append(" 				SELECT ")
		   .append(" 					r.money_serial_num ")
		   .append(" 				FROM ")
		   .append(" 					refund r ")
		   .append(" 				WHERE ")
		   .append(" 					r.del_flag = 0 ")
		   .append(" 				AND r.`status` IS NULL ")
		   .append(" 				AND r.moneyType = ").append(Refund.MONEY_TYPE_NEWPAYBACK)
		   .append(" 				AND r.money_serial_num = m.serialNum ")
		   .append("                AND r.orderType = ").append(Context.ORDER_TYPE_JP)
		   .append(" 				AND EXISTS ( ")
		   .append(" 					SELECT ")
		   .append(" 						rr.id ")
		   .append(" 					FROM ")
		   .append(" 						review_new rr, ")
		   .append(" 						airticket_order o, ")
		   .append(" 						activity_airticket p ")
		   .append(" 					WHERE ")
		   .append("  					   o.airticket_id = p.id ")
		   .append(" 				      AND rr.order_id = o.id ")
		   .append(" 				      AND rr.product_type = ").append(Context.ORDER_TYPE_JP)
		   .append("             	      AND o.order_state not in (99,111) ")
		   .append("             	      AND p.productStatus = 2 ")
		   .append(" 					AND rr.company_id = '").append(currentCompanyUuid).append("'")
		   .append(" 					AND rr.process_type = '").append(Context.REBATES_FLOW_TYPE + "' ")
		   .append(" 					AND rr.del_flag = '0' ")
		   .append(" 					AND rr.`status` = ").append(ReviewConstant.REVIEW_STATUS_PASSED)
		   .append(" 					AND r.record_id = rr.id_long ")
		   .append(" 					AND r.orderType = rr.product_type ").append(where.toString())
		   .append(" 				) ")
		   .append(" 			) group by currency_mark ")
		   .append(" 	) t ")
		   .append(" GROUP BY ")
		   .append(" 	currency_mark ");
		return findBySql(str.toString(), Map.class);
	}

	@Override
	public List<Map<String, Object>> getVisaRebatePayedSumMoney(GroupManagerEntity entity) {
		StringBuffer str = new StringBuffer();
		Long currentCompanyId = UserUtils.getUser().getCompany().getId();
		String currentCompanyUuid = UserUtils.getUser().getCompany().getUuid();
		StringBuffer where = new StringBuffer();
		if(StringUtils.isNotBlank(entity.getGroupCode())){
			where.append(" and p.groupCode like '%").append(entity.getGroupCode()).append("%' ");
		}
		if(StringUtils.isNotBlank(entity.getProductName())){
			where.append(" and p.productName like '%").append(entity.getProductName()).append("%' ");
		}
		if(null != entity.getOperatorId()){
			where.append(" and p.createBy = ").append(entity.getOperatorId());
		}
		if(null != entity.getSalerId()){
			where.append(" and o.salerId = ").append(entity.getSalerId());
		}
		if(StringUtils.isNotBlank(entity.getIscommission())){
			where.append(" and p.iscommission = ").append(entity.getIscommission());
		}
		if(null != entity.getDeptId()){
			where.append(" and p.deptId = ").append(entity.getDeptId());
		}
		if(StringUtils.isNotBlank(entity.getGroupOpenDate())){
			where.append(" and p.createDate >= '").append(entity.getGroupOpenDate()).append("'");
		}
		if(StringUtils.isNotBlank(entity.getGroupCloseDate())){
			where.append(" and p.createDate <= '").append(entity.getGroupCloseDate()).append("'");
		}
		str.append(" SELECT ")
		   .append(" 	SUM(amount) AS amount, ")
		   .append(" 	currency_mark ")
		   .append(" FROM ")
		   .append(" 	( ")
		   .append(" 		SELECT ")
		   .append(" 			sum(m.amount * m.exchangerate) AS amount, ")
		   .append(" 			'¥' AS currency_mark ")
		   .append(" 		FROM ")
		   .append(" 			money_amount m ")
		   .append(" 		WHERE ")
		   .append(" 			EXISTS ( ")
		   .append(" 			SELECT ")
		   .append(" 				r.money_serial_num ")
		   .append(" 			FROM ")
		   .append(" 				refund r ")
		   .append(" 			WHERE ")
		   .append(" 				r.del_flag = 0 ")
		   .append(" 			AND r.`status` IS NULL ")
		   .append(" 			AND r.moneyType = ").append(Refund.MONEY_TYPE_PAYBACK)
		   .append(" 			AND r.money_serial_num = m.serialNum ")
		   .append("            AND r.orderType =").append(Context.ORDER_TYPE_QZ)
		   .append(" 			AND EXISTS ( ")
		   .append(" 				SELECT ")
		   .append(" 					rr.id ")
		   .append(" 				FROM ")
		   .append(" 					review rr, ")
		   .append(" 					visa_order o, ")
		   .append(" 					visa_products p ")
		   .append(" 				WHERE ")
		   .append(" 					o.visa_product_id = p.id  ")
		   .append(" 				  AND p.delFlag=0  ")
		   .append(" 				  AND o.del_flag=0  ")
		   .append(" 				  AND rr.orderId = o.id ")
		   .append(" 				  AND rr.productType = ").append(Context.ORDER_TYPE_QZ)
		   .append("             	  AND o.payStatus not in (99,111) AND o.visa_order_status <> 100 ")
		   .append("             	  AND p.productStatus = 2 ")
		   .append(" 			    AND rr.companyId = ").append(currentCompanyId)
		   .append(" 				AND rr.flowType = ").append(Context.REBATES_FLOW_TYPE)
		   .append(" 				AND rr.active = 1 ")
		   .append(" 				AND rr.`status` IN (2, 3) ")
		   .append(" 				AND r.record_id = rr.id ")
		   .append(" 				AND r.orderType = rr.productType ").append(where.toString())
		   .append(" 			) ")
		   .append(" 		) group by currency_mark ")
		   .append(" 		UNION ")
		   .append(" 			SELECT ")
		   .append(" 				sum(m.amount * m.exchangerate) AS amount, ")
		   .append(" 				'¥' AS currency_mark ")
		   .append(" 			FROM ")
		   .append(" 				money_amount m ")
		   .append(" 			WHERE ")
		   .append(" 				EXISTS ( ")
		   .append(" 				SELECT ")
		   .append(" 					r.money_serial_num ")
		   .append(" 				FROM ")
		   .append(" 					refund r ")
		   .append(" 				WHERE ")
		   .append(" 					r.del_flag = 0 ")
		   .append(" 				AND r.`status` IS NULL ")
		   .append(" 				AND r.moneyType = ").append(Refund.MONEY_TYPE_NEWPAYBACK)
		   .append(" 				AND r.money_serial_num = m.serialNum ")
		   .append("                AND r.orderType = ").append(Context.ORDER_TYPE_QZ)
		   .append(" 				AND EXISTS ( ")
		   .append(" 					SELECT ")
		   .append(" 						rr.id ")
		   .append(" 					FROM ")
		   .append(" 						review_new rr, ")
		   .append(" 						visa_order o, ")
		   .append(" 						visa_products p ")
		   .append(" 					WHERE ")
		   .append("  					   o.visa_product_id = p.id ")
		   .append(" 				       AND p.delFlag=0  ")
		   .append(" 				       AND o.del_flag=0  ")
		   .append(" 				      AND rr.order_id = o.id ")
		   .append(" 				      AND rr.product_type = ").append(Context.ORDER_TYPE_QZ)
		   .append("             	      AND o.payStatus not in (99,111) AND o.visa_order_status <> 100 ")
		   .append("             	      AND p.productStatus = 2 ")
		   .append(" 					AND rr.company_id = '").append(currentCompanyUuid).append("'")
		   .append(" 					AND rr.process_type = '").append(Context.REBATES_FLOW_TYPE + "' ")
		   .append(" 					AND rr.del_flag = '0' ")
		   .append(" 					AND rr.`status` = ").append(ReviewConstant.REVIEW_STATUS_PASSED)
		   .append(" 					AND r.record_id = rr.id_long ")
		   .append(" 					AND r.orderType = rr.product_type ").append(where.toString())
		   .append(" 				) ")
		   .append(" 			) group by currency_mark ")
		   .append(" 	) t ")
		   .append(" GROUP BY ")
		   .append(" 	currency_mark ");
		return findBySql(str.toString(), Map.class);
	}

}
