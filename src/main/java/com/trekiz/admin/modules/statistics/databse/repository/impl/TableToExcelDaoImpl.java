package com.trekiz.admin.modules.statistics.databse.repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.trekiz.admin.common.utils.JDBCUtils;
import com.trekiz.admin.modules.statistics.databse.repository.ITableToExcelDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Repository
public class TableToExcelDaoImpl implements ITableToExcelDao {

	@Override
	public String getDataBase() {
		String database = null;
		String sql = "select t.schema_name from company_tenant c, tenant t where c.tenant_id = t.id and c.company_id = ?";
		Connection conn = JDBCUtils.getInstance().getMysqlConn();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql);
			stmt.setLong(1, UserUtils.getUser().getCompany().getId());
			rs = stmt.executeQuery();
			while(rs.next()){
				database = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(conn, stmt, rs);
		}
		return database;
	}

	@Override
	public List<String> getTables(String database) {
		List<String> list = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		//去除acivity审批相关的表，海岛游酒店的表
		sql.append("select table_name from information_schema.tables where table_schema=?  ")
		   .append(" and table_type='base table' and table_name not like 'ACT\\_%' ")
		   .append(" and table_name not like 'cms%' and table_name not like 'combo%' and table_name not like 'msg\\_%'")
		   .append(" and table_name not like 'estimate%' and table_name not like '%island%' and table_name not like '%hotel%' ");
		Connection conn = JDBCUtils.getInstance().getMysqlConn();
		
		List<String> filterList = noNeedExport(database);
		List<String> needAloneExport = aloneExport(database);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, database);
			rs = stmt.executeQuery();
			while(rs.next()){
				String tableName = rs.getString(1);
				boolean b = filterList.contains(tableName);
				boolean b1 = needAloneExport.contains(tableName);
				if(b || b1){
					continue;
				}else{
					list.add(tableName);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(conn, stmt, rs);
		}
		return list;
	}
	
	@Override
	public List<String> noNeedExport(String database){
		List<String> filterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select table_name from ").append(database).append(".sys_no_export where del_flag = '0' ");
		Connection conn = JDBCUtils.getInstance().getMysqlConn();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				String tableName = rs.getString(1);
				filterList.add(tableName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(conn, stmt, rs);
		}
		return filterList;
	}

	@Override
	public List<String> getColumns(Connection conn, String tableName, String database) {
		List<String> list = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		//去除acivity审批相关的表，海岛游酒店的表
		sql.append("SELECT COLUMN_NAME FROM information_schema.COLUMNS WHERE table_name = ? AND table_schema = ? ");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, tableName);
			stmt.setString(2, database);
			rs = stmt.executeQuery();
			while(rs.next()){
				String columnName = rs.getString(1);
				list.add(columnName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return list;
	}

	@Override
	public List<String> aloneExport(String database) {
		List<String> filterList = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select table_name from ").append(database).append(".sys_alone_export where del_flag = '0' ");
		Connection conn = JDBCUtils.getInstance().getMysqlConn();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(sql.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				String tableName = rs.getString(1);
				filterList.add(tableName);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(conn, stmt, rs);
		}
		return filterList;
	}

	@Override
	public List<List<String>> getTableData(Connection conn, String dataBase, String tableName, List<String> columns, int start, int end) {
		List<List<String>> dataList = new ArrayList<List<String>>();
		StringBuffer str = new StringBuffer();
		str.append("select ");
		for (int i = 0; i < columns.size(); i++) {
			if(i == columns.size()-1){
				str.append(columns.get(i)).append(" ");
			}else{
				str.append(columns.get(i)).append(", ");
			}
		}
		str.append(" from ").append(dataBase).append(".").append(tableName)
		.append(" limit ").append(start).append(",").append(end);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < columns.size(); i++) {
					String value = rs.getString(columns.get(i));
					list.add(value);
				}
				dataList.add(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return dataList;
	}
	
	@Override
	public int getTableDataCount(Connection conn, String dataBase, String tableName) {
		StringBuffer str = new StringBuffer();
		str.append("select count(*) ")
		   .append(" from ").append(dataBase).append(".").append(tableName);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int size = 0;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			if(rs.next()){
				size = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return size;
	}

	@Override
	public List<List<String>> getActivityAirticketTableData(Connection conn, String dataBase, List<String> columns, int start, int end) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<List<String>> dataList = new ArrayList<List<String>>();
		StringBuffer str = new StringBuffer();
		str.append("select ");
		for (int i = 0; i < columns.size(); i++) {
			if(i == columns.size()-1){
				str.append(columns.get(i)).append(" ");
			}else{
				str.append(columns.get(i)).append(", ");
			}
		}
		str.append(" from ").append(dataBase).append(".").append("activity_airticket ")
		   .append(" where proCompany = ").append(companyId).append(" limit ").append(start)
		   .append(",").append(end);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < columns.size(); i++) {
					String value = rs.getString(columns.get(i));
					list.add(value);
				}
				dataList.add(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return dataList;
	}
	
	@Override
	public int getActivityAirticketTableDataCount(Connection conn, String dataBase) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		str.append("select count(*) ")
		   .append(" from ").append(dataBase).append(".").append("activity_airticket ")
		   .append(" where proCompany = ").append(companyId);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int size = 0;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			if(rs.next()){
				size = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return size;
	}

	@Override
	public List<List<String>> getAirticketOrderTableData(Connection conn, String dataBase, List<String> columns, int start, int end) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<List<String>> dataList = new ArrayList<List<String>>();
		StringBuffer str = new StringBuffer();
		str.append("select ");
		for (int i = 0; i < columns.size(); i++) {
			if(i == columns.size()-1){
				str.append("o.").append(columns.get(i)).append(" ");
			}else{
				str.append("o.").append(columns.get(i)).append(", ");
			}
		}
		str.append(" from ").append(dataBase).append(".airticket_order o,").append(dataBase).append(".activity_airticket p ")
		   .append(" where p.proCompany = ").append(companyId).append(" and p.id = o.airticket_id  limit ").append(start)
		   .append(",").append(end);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < columns.size(); i++) {
					String value = rs.getString(columns.get(i));
					list.add(value);
				}
				dataList.add(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return dataList;
	}
	
	@Override
	public int getAirticketOrderTableDataCount(Connection conn, String dataBase) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		str.append("select count(*) ")
		   .append(" from ").append(dataBase).append(".airticket_order o,").append(dataBase).append(".activity_airticket p ")
		   .append(" where p.proCompany = ").append(companyId).append(" and p.id = o.airticket_id ");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int size = 0;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			if(rs.next()){
				size = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return size;
	}

	@Override
	public List<List<String>> getTravelactivityTableData(Connection conn, String dataBase, List<String> columns, int start, int end) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<List<String>> dataList = new ArrayList<List<String>>();
		StringBuffer str = new StringBuffer();
		str.append("select ");
		for (int i = 0; i < columns.size(); i++) {
			if(i == columns.size()-1){
				str.append(columns.get(i)).append(" ");
			}else{
				str.append(columns.get(i)).append(", ");
			}
		}
		str.append(" from ").append(dataBase).append(".travelactivity ")
		   .append(" where proCompany = ").append(companyId).append(" limit ").append(start)
		   .append(",").append(end);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < columns.size(); i++) {
					String value = rs.getString(columns.get(i));
					list.add(value);
				}
				dataList.add(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return dataList;
	}
	
	@Override
	public int getTravelactivityTableDataCount(Connection conn, String dataBase) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		str.append("select count(*) ")
		   .append(" from ").append(dataBase).append(".travelactivity ")
		   .append(" where proCompany = ").append(companyId);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int size = 0;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			if(rs.next()){
				size = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return size;
	}
	
	@Override
	public List<List<String>> getActivityGroupTableData(Connection conn, String dataBase, List<String> columns, int start, int end) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<List<String>> dataList = new ArrayList<List<String>>();
		StringBuffer str = new StringBuffer();
		str.append("select ");
		for (int i = 0; i < columns.size(); i++) {
			if(i == columns.size()-1){
				str.append("g.").append(columns.get(i)).append(" ");
			}else{
				str.append("g.").append(columns.get(i)).append(", ");
			}
		}
		str.append(" from ").append(dataBase).append(".activitygroup g,").append(dataBase).append(".travelactivity p ")
		   .append(" where p.proCompany = ").append(companyId).append(" and p.id = g.srcActivityId  limit ").append(start)
		   .append(",").append(end);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < columns.size(); i++) {
					String value = rs.getString(columns.get(i));
					list.add(value);
				}
				dataList.add(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return dataList;
	}
	
	@Override
	public int getActivityGroupTableDataCount(Connection conn, String dataBase) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		str.append("select count(*) ")
		   .append(" from ").append(dataBase).append(".activitygroup g,").append(dataBase).append(".travelactivity p ")
		   .append(" where p.proCompany = ").append(companyId).append(" and p.id = g.srcActivityId ");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int size = 0;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			if(rs.next()){
				size = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return size;
	}

	@Override
	public List<List<String>> getProductOrderTableData(Connection conn, String dataBase, List<String> columns, int start, int end) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<List<String>> dataList = new ArrayList<List<String>>();
		StringBuffer str = new StringBuffer();
		str.append("select ");
		for (int i = 0; i < columns.size(); i++) {
			if(i == columns.size()-1){
				str.append("o.").append(columns.get(i)).append(" ");
			}else{
				str.append("o.").append(columns.get(i)).append(", ");
			}
		}
		str.append(" from ").append(dataBase).append(".productorder o, ").append(dataBase).append(".travelactivity p ")
		   .append(" where p.proCompany = ").append(companyId).append(" and p.id = o.productId limit ").append(start)
		   .append(",").append(end);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < columns.size(); i++) {
					String value = rs.getString(columns.get(i));
					list.add(value);
				}
				dataList.add(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return dataList;
	}
	
	@Override
	public int getProductOrderTableDataCount(Connection conn, String dataBase) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		str.append("select count(*) ")
		   .append(" from ").append(dataBase).append(".productorder o, ").append(dataBase).append(".travelactivity p ")
		   .append(" where p.proCompany = ").append(companyId).append(" and p.id = o.productId ");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int size = 0;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			if(rs.next()){
				size = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return size;
	}
	
	@Override
	public List<List<String>> getVisaProductsTableData(Connection conn, String dataBase, List<String> columns, int start, int end) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<List<String>> dataList = new ArrayList<List<String>>();
		StringBuffer str = new StringBuffer();
		str.append("select ");
		for (int i = 0; i < columns.size(); i++) {
			if(i == columns.size()-1){
				str.append(columns.get(i)).append(" ");
			}else{
				str.append(columns.get(i)).append(", ");
			}
		}
		str.append(" from ").append(dataBase).append(".visa_products ")
		   .append("where proCompanyId = ").append(companyId)
		   .append(" limit ").append(start).append(",").append(end);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < columns.size(); i++) {
					String value = rs.getString(columns.get(i));
					list.add(value);
				}
				dataList.add(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return dataList;
	}
	
	@Override
	public int getVisaProductsTableDataCount(Connection conn, String dataBase) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		str.append("select count(*) ")
		   .append(" from ").append(dataBase).append(".visa_products ")
		   .append(" where proCompanyId = ").append(companyId);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int size = 0;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			if(rs.next()){
				size = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return size;
	}
	
	@Override
	public List<List<String>> getVisaOrderTableData(Connection conn, String dataBase, List<String> columns, int start, int end) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<List<String>> dataList = new ArrayList<List<String>>();
		StringBuffer str = new StringBuffer();
		str.append("select ");
		for (int i = 0; i < columns.size(); i++) {
			if(i == columns.size()-1){
				str.append("o.").append(columns.get(i)).append(" ");
			}else{
				str.append("o.").append(columns.get(i)).append(", ");
			}
		}
		str.append(" from ").append(dataBase).append(".visa_order o, ").append(dataBase).append(".visa_products p ")
		   .append("where p.proCompanyId = ").append(companyId).append(" and o.visa_product_id = p.id ")
		   .append(" limit ").append(start).append(",").append(end);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < columns.size(); i++) {
					String value = rs.getString(columns.get(i));
					list.add(value);
				}
				dataList.add(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return dataList;
	}
	
	@Override
	public int getVisaOrderTableDataCount(Connection conn, String dataBase) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		str.append("select count(*) ")
		   .append(" from ").append(dataBase).append(".visa_order o, ").append(dataBase).append(".visa_products p ")
		   .append("where p.proCompanyId = ").append(companyId).append(" and o.visa_product_id = p.id ");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int size = 0;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			if(rs.next()){
				size = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return size;
	}

	@Override
	public List<List<String>> getReviewTableData(Connection conn, String dataBase, List<String> columns, int start, int end) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<List<String>> dataList = new ArrayList<List<String>>();
		StringBuffer str = new StringBuffer();
		str.append("select ");
		for (int i = 0; i < columns.size(); i++) {
			if(i == columns.size()-1){
				str.append(columns.get(i)).append(" ");
			}else{
				str.append(columns.get(i)).append(", ");
			}
		}
		str.append(" from ").append(dataBase).append(".review ")
		   .append(" where companyId = ").append(companyId).append(" limit ").append(start)
		   .append(",").append(end);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < columns.size(); i++) {
					String value = rs.getString(columns.get(i));
					list.add(value);
				}
				dataList.add(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return dataList;
	}
	
	@Override
	public int getReviewTableDataCount(Connection conn, String dataBase) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		str.append("select count(*) ")
		   .append(" from ").append(dataBase).append(".review ")
		   .append(" where companyId = ").append(companyId);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int size = 0;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			if(rs.next()){
				size = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return size;
	}

	@Override
	public List<List<String>> getReviewDetailTableData(Connection conn, String dataBase, List<String> columns, int start, int end) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<List<String>> dataList = new ArrayList<List<String>>();
		StringBuffer str = new StringBuffer();
		str.append("select ");
		for (int i = 0; i < columns.size(); i++) {
			if(i == columns.size()-1){
				str.append("d.").append(columns.get(i)).append(" ");
			}else{
				str.append("d.").append(columns.get(i)).append(", ");
			}
		}
		str.append(" from ").append(dataBase).append(".review_detail d, ").append(dataBase).append(".review r ")
		   .append(" where r.companyId = ").append(companyId).append(" and r.id = d.review_id limit ").append(start)
		   .append(",").append(end);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < columns.size(); i++) {
					String value = rs.getString(columns.get(i));
					list.add(value);
				}
				dataList.add(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return dataList;
	}
	
	@Override
	public int getReviewDetailTableDataCount(Connection conn, String dataBase) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		str.append("select count(*) ")
		   .append(" from ").append(dataBase).append(".review_detail d, ").append(dataBase).append(".review r ")
		   .append(" where r.companyId = ").append(companyId).append(" and r.id = d.review_id ");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int size = 0;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			if(rs.next()){
				size = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return size;
	}

	@Override
	public List<List<String>> getReviewNewTableData(Connection conn, String dataBase, List<String> columns, int start, int end) {
		String companyId = UserUtils.getUser().getCompany().getUuid();
		List<List<String>> dataList = new ArrayList<List<String>>();
		StringBuffer str = new StringBuffer();
		str.append("select ");
		for (int i = 0; i < columns.size(); i++) {
			if(i == columns.size()-1){
				str.append(columns.get(i)).append(" ");
			}else{
				str.append(columns.get(i)).append(", ");
			}
		}
		str.append(" from ").append(dataBase).append(".review_new  ")
		   .append(" where company_id = '").append(companyId).append("' limit ").append(start)
		   .append(",").append(end);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < columns.size(); i++) {
					String value = rs.getString(columns.get(i));
					list.add(value);
				}
				dataList.add(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return dataList;
	}
	
	@Override
	public int getReviewNewTableDataCount(Connection conn, String dataBase) {
		String companyId = UserUtils.getUser().getCompany().getUuid();
		StringBuffer str = new StringBuffer();
		str.append("select count(*) ")
		   .append(" from ").append(dataBase).append(".review_new  ")
		   .append(" where company_id = '").append(companyId).append("' ");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int size = 0;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			if(rs.next()){
				size = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return size;
	}
	
	@Override
	public List<List<String>> getReviewProcessTableData(Connection conn, String dataBase, List<String> columns, int start, int end) {
		String companyId = UserUtils.getUser().getCompany().getUuid();
		List<List<String>> dataList = new ArrayList<List<String>>();
		StringBuffer str = new StringBuffer();
		str.append("select ");
		for (int i = 0; i < columns.size(); i++) {
			if(i == columns.size()-1){
				str.append(columns.get(i)).append(" ");
			}else{
				str.append(columns.get(i)).append(", ");
			}
		}
		str.append(" from ").append(dataBase).append(".review_process  ")
		   .append("where company_id = '").append(companyId).append("' ")
		   .append(" limit ").append(start).append(",").append(end);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < columns.size(); i++) {
					String value = rs.getString(columns.get(i));
					list.add(value);
				}
				dataList.add(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return dataList;
	}
	
	@Override
	public int getReviewProcessTableDataCount(Connection conn, String dataBase) {
		String companyId = UserUtils.getUser().getCompany().getUuid();
		StringBuffer str = new StringBuffer();
		str.append("select count(*) ")
		   .append(" from ").append(dataBase).append(".review_process  ")
		   .append("where company_id = '").append(companyId).append("' ");
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int size = 0;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			if(rs.next()){
				size = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return size;
	}

	@Override
	public List<List<String>> getSupplierInfoTableData(Connection conn, String dataBase, List<String> columns, int start, int end) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<List<String>> dataList = new ArrayList<List<String>>();
		StringBuffer str = new StringBuffer();
		str.append("select ");
		for (int i = 0; i < columns.size(); i++) {
			if(i == columns.size()-1){
				str.append(columns.get(i)).append(" ");
			}else{
				str.append(columns.get(i)).append(", ");
			}
		}
		str.append(" from ").append(dataBase).append(".supplier_info  ")
		   .append(" where companyId = ").append(companyId).append(" limit ").append(start)
		   .append(",").append(end);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < columns.size(); i++) {
					String value = rs.getString(columns.get(i));
					list.add(value);
				}
				dataList.add(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return dataList;
	}
	
	@Override
	public int getSupplierInfoTableDataCount(Connection conn, String dataBase) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		str.append("select count(*) ")
		   .append(" from ").append(dataBase).append(".supplier_info  ")
		   .append(" where companyId = ").append(companyId);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int size = 0;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			if(rs.next()){
				size = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return size;
	}
	
	@Override
	public List<List<String>> getAgentInfoTableData(Connection conn, String dataBase, List<String> columns, int start, int end) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<List<String>> dataList = new ArrayList<List<String>>();
		StringBuffer str = new StringBuffer();
		str.append("select ");
		for (int i = 0; i < columns.size(); i++) {
			if(i == columns.size()-1){
				str.append(columns.get(i)).append(" ");
			}else{
				str.append(columns.get(i)).append(", ");
			}
		}
		str.append(" from ").append(dataBase).append(".agentinfo  ")
		   .append(" where supplyId = ").append(companyId).append(" limit ").append(start)
		   .append(",").append(end);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < columns.size(); i++) {
					String value = rs.getString(columns.get(i));
					list.add(value);
				}
				dataList.add(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return dataList;
	}
	
	@Override
	public int getAgentInfoTableDataCount(Connection conn, String dataBase) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		str.append("select count(*) ")
		   .append(" from ").append(dataBase).append(".agentinfo  ")
		   .append(" where supplyId = ").append(companyId);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int size = 0;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			if(rs.next()){
				size = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return size;
	}

	@Override
	public List<List<String>> getSysUserTableData(Connection conn, String dataBase, List<String> columns, int start, int end) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<List<String>> dataList = new ArrayList<List<String>>();
		StringBuffer str = new StringBuffer();
		str.append("select ");
		for (int i = 0; i < columns.size(); i++) {
			if(i == columns.size()-1){
				if("password".equalsIgnoreCase(columns.get(i))){
					str.append(" '1' as password ").append(" ");
				}else{
					str.append(columns.get(i)).append(" ");
				}
			}else{
				if("password".equalsIgnoreCase(columns.get(i))){
					str.append(" '1' as password ").append(", ");
				}else{
					str.append(columns.get(i)).append(", ");
				}
			}
		}
		str.append(" from ").append(dataBase).append(".sys_user  ")
		   .append(" where companyId = ").append(companyId).append(" limit ").append(start)
		   .append(",").append(end);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			while(rs.next()){
				List<String> list = new ArrayList<String>();
				for (int i = 0; i < columns.size(); i++) {
					String value = rs.getString(columns.get(i));
					list.add(value);
				}
				dataList.add(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return dataList;
	}
	
	@Override
	public int getSysUserTableDataCount(Connection conn, String dataBase) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer str = new StringBuffer();
		str.append("select count(*) ")
		   .append(" from ").append(dataBase).append(".sys_user  ")
		   .append(" where companyId = ").append(companyId);
		PreparedStatement stmt = null;
		ResultSet rs = null;
		int size = 0;
		try {
			stmt = conn.prepareStatement(str.toString());
			rs = stmt.executeQuery();
			if(rs.next()){
				size = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			JDBCUtils.close(null, stmt, rs);
		}
		return size;
	}
}
