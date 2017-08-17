package com.trekiz.admin.modules.statistics.databse.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.trekiz.admin.common.utils.JDBCUtils;

public class ExportDataThread implements Callable<Boolean>{

	//每次查询的数量
	private static final int SIZE = 10000;
	
	private Sheet sheet;
	private Connection conn;
	private String dataBase;
	private String tableName;
	
	public ExportDataThread(Sheet sheet, Connection conn, String dataBase, 
			String tableName) {
		this.sheet = sheet;
		this.conn = conn;
		this.dataBase = dataBase;
		this.tableName = tableName;
	}
	
	/**
	 * 查表数据
	 * @param conn			数据库连接
	 * @param dataBase		数据库
	 * @param tableName		表名称
	 * @param columns		表对应的列字段
	 * @param start			开始行
	 * @param end			行数
	 * @return
	 * @date 2016.02.03
	 * @author shijun.liu
	 */
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
	
	/**
	 * 将数据写入表中
	 * @param sheet		sheet表
	 * @param data		表数据
	 * @param index		行号
	 * @date  2016.02.03
	 * @author shijun.liu
	 */
	public void dataToExcel(Sheet sheet, List<List<String>> data, int index) {
		if(null != sheet){
			for (int i = 0; i < data.size(); i++) {
				List<String> list = data.get(i);
				//略过表头
				Row row = sheet.createRow(index + i + 1);
				for (int j = 0; j < list.size(); j++) {
					row.createCell(j).setCellValue(list.get(j));
				}
			}
		}
	}

	@Override
	public Boolean call(){
		boolean b = true;
		try {
			tableDataToSheet(conn, sheet, tableName, dataBase);
		} catch (Exception e) {
			b = false;
			e.printStackTrace();
		}
		return b;
	}
	
	/**
	 * 将数据库中表的数据写入到Excel中
	 * @param conn			数据库连接
	 * @param sheet			sheet表
	 * @param tableName		表名称
	 * @param dataBase		数据库
	 * @date 2016.02.03
	 * @author shijun.liu
	 */
	public void tableDataToSheet(Connection conn, Sheet sheet, String tableName, String dataBase){
		//查询每个表的所有字段
		List<String> columns = getColumns(conn, tableName, dataBase);
		columnsToExcel(sheet, tableName, columns);
		int totalCount = getTableDataCount(conn, dataBase, tableName);
		if(0 != totalCount){
			int page = totalCount/SIZE;
			if(0 == page){
				//查询表的数据
				List<List<String>> dataList = getTableData(conn, dataBase, tableName, columns, 0, totalCount);
				//将数据写入到Excel中
				dataToExcel(sheet, dataList, 0);
			}else{
				for (int j = 0; j <= page; j++) {
					int start = j * SIZE;
					//查询表的数据
					List<List<String>> dataList = getTableData(conn, dataBase, tableName, columns, start, SIZE);
					//将数据写入到Excel中
					dataToExcel(sheet, dataList, start);
				}
			}
		}
	}
	
	/**
	 * 查询数据库表的总记录数
	 * @param conn		数据库连接
	 * @param dataBase	数据库
	 * @param tableName	表名称
	 * @return
	 * @author shijun.liu
	 * @date 2016.02.03
	 */
	private int getTableDataCount(Connection conn, String dataBase, String tableName) {
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
	
	/**
	 * 将数据库表的列字段写入到Excel中
	 * @param sheet
	 * @param tableName
	 * @param columns
	 * @author shijun.liu
	 * @date 2016.02.03
	 */
	private void columnsToExcel(Sheet sheet, String tableName, List<String> columns) {
		if(null != sheet){
			Row row = sheet.createRow(0);
			for (int i = 0; i < columns.size(); i++) {
				row.createCell(i).setCellValue(columns.get(i));
			}
		}
	}
	
	/**
	 * 查询表的所有字段
	 * @param conn			数据库连接
	 * @param tableName		表名称
	 * @param database		数据库
	 * @return
	 * @author shijun.liu
	 * @date 2016.02.03
	 */
	private List<String> getColumns(Connection conn, String tableName, String database) {
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
}
