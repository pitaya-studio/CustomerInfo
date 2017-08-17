package com.trekiz.admin.modules.statistics.product.repository.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.trekiz.admin.modules.statistics.product.bean.ProductCount;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.utils.JDBCUtils;
import com.trekiz.admin.modules.statistics.product.bean.ProductInfo;
import com.trekiz.admin.modules.statistics.product.repository.ProductStatisticsDao;

/**
 * 
 * 产品数据统计的DAO对象
 * @author shijun.liu
 *
 */
@Repository
public class ProductStatisticsDaoImpl implements ProductStatisticsDao{

	private static Log log = LogFactory.getLog(ProductStatisticsDaoImpl.class);

	@Override
	public List<ProductInfo> getAllProductInfoFor1_5() {
		List<ProductInfo> list = new ArrayList<ProductInfo>();
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		conn = JDBCUtils.getInstance().getMysqlConn();
		String procedure = "call p_query_all_product(?)";
		String procedureSQL = "";
		try {
			cstmt = conn.prepareCall(procedure);
			cstmt.execute();
			//过程执行的SQL语句
			procedureSQL = cstmt.getString(1);
			log.info("存储过程执行的SQL:"+procedureSQL);
			rs = cstmt.getResultSet();
			while(rs.next()){
				ProductInfo product = new ProductInfo();
				product.setCompanyName(rs.getString("companyName"));
				product.setProductName(rs.getString("productName"));
				product.setOperator(rs.getString("operator"));
				product.setDepartureCity(rs.getString("departureCity"));
				product.setArrivedCity(rs.getString("arrivedCity"));
				product.setVisa(rs.getString("visa"));
				product.setGroupCode(rs.getString("groupCode"));
				product.setGroupOpenDate(rs.getString("groupOpenDate"));
				product.setGroupCloseDate(rs.getString("groupCloseDate"));
				product.setVisaCountry(rs.getString("visaCountry"));
				product.setVisaType(rs.getString("visaType"));
				product.setVisaArea(rs.getString("visaArea"));
				product.setCostMoney(rs.getString("costMoney"));
				product.setReceiveMoney(rs.getString("receiveMoney"));
				product.setCreateDate(rs.getString("createDate"));
				product.setInfoDate(rs.getString("infoDate"));
				product.setSettlementAdultPrice(rs.getString("settlementAdultPrice"));
				product.setSettlementChildPrice(rs.getString("settlementcChildPrice"));
				product.setSettlementSpecialPrice(rs.getString("settlementSpecialPrice"));
				product.setSuggestAdultPrice(rs.getString("suggestAdultPrice"));
				product.setSuggestChildPrice(rs.getString("suggestChildPrice"));
				product.setSuggestSpecialPrice(rs.getString("suggestSpecialPrice"));
				product.setPayDeposit(rs.getString("payDeposit"));
				product.setSingleDiff(rs.getString("singleDiff"));
				product.setPlanPosition(rs.getString("planPosition"));
				product.setFreePosition(rs.getString("freePosition"));
				product.setPayReservePosition(rs.getString("payReservePosition"));
				product.setProductCode(rs.getString("productCode"));
				product.setAirType(rs.getString("airType"));
				product.setTaxamt(rs.getString("taxamt"));
				product.setDepositTime(rs.getString("depositTime"));
				product.setCancelTimeLimit(rs.getString("cancelTimeLimit"));
				product.setOutTicketTime(rs.getString("outTicketTime"));
				product.setAirCompany(rs.getString("airCompany"));
				product.setAirSpace(rs.getString("airSpace"));
				product.setLeaveAirport(rs.getString("leaveAirport"));
				product.setArrivedAirport(rs.getString("arrivedAirport"));
				product.setStartTime(rs.getString("startTime"));
				product.setArrivalTime(rs.getString("arrivalTime"));
				product.setOrderType(rs.getString("orderType"));
				product.setProductId(rs.getString("productId"));
				product.setVersion(rs.getString("version"));
				list.add(product);
			}
		} catch (SQLException e) {
			log.error("数据库执行错误", e);
		} finally {
			JDBCUtils.close(conn, cstmt, rs);
		}
		return list;
	}

	@Override
	public List<ProductInfo> getAllProductInfoFor1_1() {
		StringBuffer str = new StringBuffer();
		str.append(" SELECT																									 ")
		   .append(" 	p.proCompanyName AS companyName,                                                                     ")
		   .append(" 	p.acitivityName AS productName,                                                                      ")
		   .append(" 	(SELECT NAME FROM sys_user su WHERE su.id = p.createBy) AS operator,                                 ")
		   .append(" 	(select label from sys_dict sd where sd.type = 'from_area' and value = p.fromArea) AS departureCity, ")
		   .append(" 	null AS arrivedCity,                                                                                 ")
		   .append(" 	NULL AS visa,                                                                                        ")
		   .append(" 	g.groupCode,                                                                                         ")
		   .append(" 	g.groupOpenDate,                                                                                     ")
		   .append(" 	g.groupCloseDate,                                                                                    ")
		   .append(" 	g.visaCountry,                                                                                       ")
		   .append(" 	NULL AS visaType,                                                                                    ")
		   .append(" 	NULL AS visaArea,                                                                                    ")
		   .append(" 	NULL AS costMoney,                                                                                   ")
		   .append(" 	NULL AS receiveMoney,                                                                                ")
		   .append(" 	p.createDate,                                                                                        ")
		   .append(" 	g.visaDate AS infoDate,                                                                              ")
		   .append(" 	g.settlementAdultPrice,                                                                              ")
		   .append(" 	g.settlementcChildPrice,                                                                             ")
		   .append(" 	g.settlementSpecialPrice,                                                                            ")
		   .append(" 	g.suggestAdultPrice,                                                                                 ")
		   .append(" 	g.suggestChildPrice,                                                                                 ")
		   .append(" 	g.suggestSpecialPrice,                                                                               ")
		   .append(" 	g.payDeposit,                                                                                        ")
		   .append(" 	g.singleDiff,                                                                                        ")
		   .append(" 	g.planPosition,                                                                                      ")
		   .append(" 	g.freePosition,                                                                                      ")
		   .append(" 	g.payReservePosition,                                                                                ")
		   .append(" 	activitySerNum AS productCode,                                                                       ")
		   .append(" 	NULL AS airType,                                                                                     ")
		   .append(" 	NULL AS taxamt,                                                                                      ")
		   .append(" 	NULL AS depositTime,                                                                                 ")
		   .append(" 	NULL AS cancelTimeLimit,                                                                             ")
		   .append(" 	NULL AS outTicketTime,                                                                               ")
		   .append(" 	NULL AS airCompany,                                                                                  ")
		   .append(" 	NULL AS airSpace,                                                                                    ")
		   .append(" 	NULL AS leaveAirport,                                                                                ")
		   .append(" 	NULL AS arrivedAirport,                                                                              ")
		   .append(" 	NULL AS startTime,                                                                                   ")
		   .append(" 	NULL AS arrivalTime,                                                                                 ")
		   .append(" 	p.activity_kind AS orderType,                                                                        ")
		   .append(" 	p.id as productId,                                                                                   ")
		   .append(" 	'1.1' as version                                                                                     ")
		   .append(" FROM                                                                                                    ")
		   .append(" 	travelactivity p,                                                                                    ")
		   .append(" 	activitygroup g                                                                                      ")
		   .append(" WHERE                                                                                                   ")
		   .append(" 	p.id = g.srcActivityId                                                                               ")
		   .append(" AND p.delFlag = '0'                                                                                     ")
		   .append(" AND p.activityStatus = 2                                                                                ")
		   .append(" AND p.proCompanyName NOT LIKE 'TEST_%'                                                                  ");
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<ProductInfo> list = new ArrayList<ProductInfo>();
		try {
			conn = JDBCUtils.getInstance().getMysqlConn1_1();
			stmt = conn.createStatement();
			rs = stmt.executeQuery(str.toString());
			while(rs.next()){
				ProductInfo product = new ProductInfo();
				product.setCompanyName(rs.getString("companyName"));
				product.setProductName(rs.getString("productName"));
				product.setOperator(rs.getString("operator"));
				product.setDepartureCity(rs.getString("departureCity"));
				product.setArrivedCity(rs.getString("arrivedCity"));
				product.setVisa(rs.getString("visa"));
				product.setGroupCode(rs.getString("groupCode"));
				product.setGroupOpenDate(rs.getString("groupOpenDate"));
				product.setGroupCloseDate(rs.getString("groupCloseDate"));
				product.setVisaCountry(rs.getString("visaCountry"));
				product.setVisaType(rs.getString("visaType"));
				product.setVisaArea(rs.getString("visaArea"));
				product.setCostMoney(rs.getString("costMoney"));
				product.setReceiveMoney(rs.getString("receiveMoney"));
				product.setCreateDate(rs.getString("createDate"));
				product.setInfoDate(rs.getString("infoDate"));
				product.setSettlementAdultPrice(rs.getString("settlementAdultPrice"));
				product.setSettlementChildPrice(rs.getString("settlementcChildPrice"));
				product.setSettlementSpecialPrice(rs.getString("settlementSpecialPrice"));
				product.setSuggestAdultPrice(rs.getString("suggestAdultPrice"));
				product.setSuggestChildPrice(rs.getString("suggestChildPrice"));
				product.setSuggestSpecialPrice(rs.getString("suggestSpecialPrice"));
				product.setPayDeposit(rs.getString("payDeposit"));
				product.setSingleDiff(rs.getString("singleDiff"));
				product.setPlanPosition(rs.getString("planPosition"));
				product.setFreePosition(rs.getString("freePosition"));
				product.setPayReservePosition(rs.getString("payReservePosition"));
				product.setProductCode(rs.getString("productCode"));
				product.setAirType(rs.getString("airType"));
				product.setTaxamt(rs.getString("taxamt"));
				product.setDepositTime(rs.getString("depositTime"));
				product.setCancelTimeLimit(rs.getString("cancelTimeLimit"));
				product.setOutTicketTime(rs.getString("outTicketTime"));
				product.setAirCompany(rs.getString("airCompany"));
				product.setAirSpace(rs.getString("airSpace"));
				product.setLeaveAirport(rs.getString("leaveAirport"));
				product.setArrivedAirport(rs.getString("arrivedAirport"));
				product.setStartTime(rs.getString("startTime"));
				product.setArrivalTime(rs.getString("arrivalTime"));
				product.setOrderType(rs.getString("orderType"));
				product.setProductId(rs.getString("productId"));
				product.setVersion(rs.getString("version"));
				list.add(product);
			}
		} catch (SQLException e) {
			log.error("数据库执行错误", e);
		}finally{
			JDBCUtils.close(conn, stmt, rs);
		}
		return list;
	}

	/**
	 * 根据指定时间，查询每个客户下的创建产品的数量。
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<ProductCount> getProductSumPerOffice(String beginDate, String endDate){
		Connection conn = JDBCUtils.getInstance().getMysqlConn();
		String callSql = "call p_query_product_count('"+beginDate+"','"+endDate+"')";
		List<ProductCount> result = new ArrayList<>();
		CallableStatement call = null;
		ResultSet rs  = null;
		try {
			call = conn.prepareCall(callSql);
			call.execute();
			rs = call.getResultSet();
			while (rs.next()){
				ProductCount bean = new ProductCount();
				bean.setCompanyName(rs.getString("name"));
				bean.setProductNum(rs.getInt("count"));
				result.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(conn,call,rs);
		}
		return result;
	}
}
