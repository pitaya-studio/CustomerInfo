package com.trekiz.admin.modules.statistics.order.repository.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.exception.bean.BaseException4Quauq;
import com.trekiz.admin.common.exception.common.ExceptionConstants;
import com.trekiz.admin.common.exception.util.QuauqExceptionFactory;
import com.trekiz.admin.common.utils.JDBCUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.statistics.order.bean.OrderDetail;
import com.trekiz.admin.modules.statistics.order.repository.OrderDataStatisticsDao;

/**
 * 
 * 订单数据统计的DAO对象
 * @author shijun.liu
 *
 */
@Repository
public class OrderDataStatisticsDaoImpl implements OrderDataStatisticsDao{

	private static Log log = LogFactory.getLog(OrderDataStatisticsDaoImpl.class);
	private static final String REGEX = ",";
	private static final String CONN_ADDRESS = "-";
	@Override
	public List<OrderDetail> exportAllOrderDetail(String beginDate,
			String endDate) throws BaseException4Quauq{
		List<OrderDetail> list = new ArrayList<OrderDetail>();
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		conn = JDBCUtils.getInstance().getMysqlConn();
		String procedure = "call p_query_all_order('"+beginDate+"','"+endDate+"',?)";
		String procedureSQL = "";
		try {
			cstmt = conn.prepareCall(procedure);
			long start = System.currentTimeMillis();
			cstmt.execute();
			log.info("执行存储过程时间: " + (System.currentTimeMillis() - start)/1000);
			//过程执行的SQL语句
			procedureSQL = cstmt.getString(1);
			//log.info("存储过程执行的SQL:"+procedureSQL);
			rs = cstmt.getResultSet();
			while(rs.next()){
				//去除美途国际产品产品名称是测试或者test的记录
	    		if(StringUtils.isNotBlank(rs.getString("airticketName")) && 
	    				(rs.getString("airticketName").indexOf("测试") != -1 
						|| rs.getString("airticketName").indexOf("test") != -1)){
					continue;
				}
				OrderDetail orderDetail = new OrderDetail();
				orderDetail.setSupplierName(rs.getString("supplier"));
				orderDetail.setAgentName(rs.getString("agentName"));
				orderDetail.setOrderNum(rs.getString("orderNum"));
				orderDetail.setOrderPersonName(rs.getString("orderName"));
				orderDetail.setOrderTime(rs.getString("orderTime"));
				orderDetail.setProductName(rs.getString("acitivityName"));
				orderDetail.setAirticketName(rs.getString("airticketName"));
				orderDetail.setGroupCode(rs.getString("groupCode"));
				orderDetail.setOrderPersonCount(rs.getString("orderPersonNum"));
				orderDetail.setOrderStatus(rs.getString("orderStatus"));
				String totalMoney = String.valueOf(rs.getString("totalMoney")==null?"0.00":rs.getString("totalMoney"));
				String payedMoney = String.valueOf(rs.getString("payedMoney")==null?"0.00":rs.getString("payedMoney"));
				String accountedMoney = String.valueOf(rs.getString("accountedMoney")==null?"0.00":rs.getString("accountedMoney"));
				String productType = rs.getString("productType");//订单类型
				String salerName = rs.getString("salerName");
				String country = rs.getString("country");//国家
				String dataBase = rs.getString("db");//数据库
				String productId = rs.getString("productId");//产品Id
				String departureAddress = rs.getString("departureAddress");//出发地
				String arriviedAddress = rs.getString("arriviedAddress");//目的地(只针对机票产品)，团期类产品只能根据产品Id单独查询
				orderDetail.setDepartureAddress(departureAddress);
				orderDetail.setArriviedAddress(arriviedAddress);
				orderDetail.setArea("");//区域为空
				String agentType = "";
				if("2".equals(rs.getString("priceType"))){
					agentType = "实时连通渠道";
				}else{
					agentType = "非实时渠道";
				}
				orderDetail.setAgentType(agentType);//实时连通渠道
				int tempOrderType = Integer.parseInt(productType);
				if(StringUtils.isNotBlank(dataBase) && 
						(tempOrderType < Context.ORDER_TYPE_QZ || Context.ORDER_TYPE_CRUISE == tempOrderType)){
					//团期类产品单独查询目的地
					String address = getArriviedAddressByProductId(conn, dataBase, productId);
					orderDetail.setArriviedAddress(address + CONN_ADDRESS);
				}
				if(StringUtils.isNotBlank(dataBase) && tempOrderType == Context.ORDER_TYPE_JP){
					StringBuffer address = new StringBuffer();
					getArriviedAddressByAreaId(conn, dataBase, arriviedAddress, address);
					if("".equals(address.toString())){
						orderDetail.setArriviedAddress("");
					}else {
						orderDetail.setArriviedAddress(address.toString() + CONN_ADDRESS);
					}
				}
				String productTypeChineseName = OrderCommonUtil.getChineseOrderType(productType);
				orderDetail.setTotalMoney(totalMoney);
				orderDetail.setPayedMoney(payedMoney);
				orderDetail.setAccountedMoney(accountedMoney);
				orderDetail.setProductType(productTypeChineseName);
				orderDetail.setSalerName(salerName);
				orderDetail.setCountry(country);
				list.add(orderDetail);
			}
		} catch (SQLException e) {
			String message = "查询订单信息调用数据库存储过程时数据库发生异常";
			throw QuauqExceptionFactory.newQuauqException(ExceptionConstants.PRODUCT_TYPE_QUAUQ, ExceptionConstants.MODULE_QUAUQ_ORDER, message, e);
		} finally {
			JDBCUtils.close(conn, cstmt, rs);
		}
		return list;
	}
	
	/**
	 * 团期类产品查询相应的目的地(可能是多个)
	 * @param conn			数据库连接
	 * @param dataBase		所在数据库
	 * @param productId		产品ID
	 * @return
	 * @author shijun.liu
	 */
	private String getArriviedAddressByProductId(Connection conn, String dataBase, String productId) throws BaseException4Quauq{
		StringBuffer targetAddress = new StringBuffer();
		StringBuffer str = new StringBuffer();
		//目的地如果有多个则只取一个即可
		str.append("select targetAreaId from ").append(dataBase).append(".activitytargetarea where srcActivityId = ? limit 0,1 ");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(str.toString());
			pstmt.setInt(1, Integer.parseInt(productId));
			rs = pstmt.executeQuery();
			while(rs.next()){
				StringBuffer address = new StringBuffer();
				String targetAreaId = rs.getString("targetAreaId");
				getArriviedAddressByAreaId(conn, dataBase, targetAreaId, address);
				if("".equals(targetAddress.toString())){
					targetAddress.append(address.toString());
				}else{
					targetAddress.append(REGEX).append(address.toString());
				}
			}
		} catch (SQLException e) {
			String message = "根据产品ID [" + productId + "] 查询其目的地时，数据库发生异常";
			throw QuauqExceptionFactory.newQuauqException(ExceptionConstants.PRODUCT_TYPE_QUAUQ, ExceptionConstants.MODULE_QUAUQ_ORDER, message, e);
		} finally{
			JDBCUtils.close(null, pstmt, rs);
		}
		return targetAddress.toString();
	}
	
	/**
	 * 递归查询每个目的地的父级，（出境游-美国-洛杉矶）
	 * @param conn			数据库连接
	 * @param dataBase		数据库
	 * @param areaId		区域ID
	 * @param address		存储区域的返回地址（出境游-美国-洛杉矶）
	 * @author shijun.liu
	 */
	private void getArriviedAddressByAreaId(Connection conn, String dataBase, String areaId, StringBuffer address) throws BaseException4Quauq{
		//美途国际之前也有目的地，其目的地的值为uuid，所以此处做是否是数字的判断
		if(StringUtils.isBlank(areaId) || !MoneyNumberFormat.isNumber(areaId)){
			return;
		}
		StringBuffer str = new StringBuffer();
		str.append("select parentId, name from ").append(dataBase).append(".sys_area where id = ?");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(str.toString());
			pstmt.setInt(1, Integer.parseInt(areaId));
			rs = pstmt.executeQuery();
			while(rs.next()){
				String name = rs.getString("name");
				String parentAreaId = rs.getString("parentId");
				address.insert(0, CONN_ADDRESS + name);
				if("1".equals(parentAreaId)){
					address.delete(0, CONN_ADDRESS.length());
					break;
				}
				getArriviedAddressByAreaId(conn, dataBase, parentAreaId, address);
			}
		} catch (SQLException e) {
			String message = "根据目的地ID [" + areaId + "] 查询其父级目的地时，数据库发生异常";
			throw QuauqExceptionFactory.newQuauqException(ExceptionConstants.PRODUCT_TYPE_QUAUQ, ExceptionConstants.MODULE_QUAUQ_ORDER, message, e);
		} finally{
			JDBCUtils.close(null, pstmt, rs);
		}
	}
	
	/**
	 * 根据UUID查询相应的金额
	 * @param uuid
	 * @param conn
	 * @return
	 */
	@Deprecated
	private String getMoneyByUUID(String uuid, Connection conn){
		
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String money = "0";
		String procedure = "call p_query_money('"+uuid+"',?)";
		if(StringUtils.isBlank(uuid)){
			return money;
		}
		try {
			cstmt = conn.prepareCall(procedure);
			cstmt.execute();
			rs = cstmt.getResultSet();
			while(rs.next()){
				money = rs.getString("money");
				if(StringUtils.isBlank(money)){
					money = "0";
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			JDBCUtils.close(null, cstmt, rs);
		}
		return money;
	}

}
