package com.trekiz.admin.modules.statistics.databse.service.impl;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.common.utils.JDBCUtils;
import com.trekiz.admin.modules.statistics.databse.repository.ITableToExcelDao;
import com.trekiz.admin.modules.statistics.databse.service.ITableToExcelService;
import com.trekiz.admin.modules.statistics.databse.thread.ExportDataThread;
import com.trekiz.admin.modules.statistics.utils.Utils;

@Service
public class TableToExcelServiceImpl implements ITableToExcelService {

	//每次查询的数量
	private static final int SIZE = 10000;
	@Autowired
	private ITableToExcelDao tableToExcel;
	
	@Override
	public Workbook columnsToExcel(Workbook workbook, String tableName, List<String> columns) {
		String sheetName = Utils.getSheetName(tableName);
		Sheet existSheet = workbook.getSheet(sheetName);
		if(null == existSheet){
			Sheet sheet = workbook.createSheet(sheetName);
			Row row = sheet.createRow(0);
			for (int i = 0; i < columns.size(); i++) {
				row.createCell(i).setCellValue(columns.get(i));
			}
		}
		return workbook;
	}

	@Override
	public Workbook dataToExcel(Workbook workbook, String tableName, List<List<String>> data, int index) {
		String sheetName = Utils.getSheetName(tableName);
		Sheet existSheet = workbook.getSheet(sheetName);
		if(null != existSheet){
			for (int i = 0; i < data.size(); i++) {
				List<String> list = data.get(i);
				//略过表头
				Row row = existSheet.createRow(index + i + 1);
				for (int j = 0; j < list.size(); j++) {
					row.createCell(j).setCellValue(list.get(j));
				}
			}
		}
		return workbook;
	}

	@Override
	public String exportDataToExcel() {
		String dataFileName = Utils.createDataFileName();
		//防止内存溢出，使用SXSSFWorkbook对象
		SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(),1000);
		//查询当前公司所有数据所在的数据库
		String dataBase = tableToExcel.getDataBase();
		//查询该数据库下面的表
		List<String> tables = tableToExcel.getTables(dataBase);
		Connection conn = JDBCUtils.getInstance().getMysqlConn();
		for (int i = 0; i < tables.size(); i++) {
			//查询每个表的所有字段
			List<String> columns = tableToExcel.getColumns(conn, tables.get(i), dataBase);
			//将表结构写入到Excel文件中
			columnsToExcel(workbook, tables.get(i), columns);
			int totalCount = tableToExcel.getTableDataCount(conn, dataBase, tables.get(i));
			if(0 == totalCount){
				continue;
			}
			int page = totalCount/SIZE;
			if(0 == page){
				//查询表的数据
				List<List<String>> dataList = tableToExcel.getTableData(conn, dataBase, tables.get(i), columns, 0, totalCount);
				//将数据写入到Excel中
				dataToExcel(workbook, tables.get(i), dataList, 0);
			}else{
				for (int j = 0; j <= page; j++) {
					int start = j * SIZE;
					//查询表的数据
					List<List<String>> dataList = tableToExcel.getTableData(conn, dataBase, tables.get(i), columns, start, SIZE);
					//将数据写入到Excel中
					dataToExcel(workbook, tables.get(i), dataList, start);
				}
			}
		}
		//将机票产品表的数据写入到Excel文件中
		writeActivityAirticketToExcel(conn, dataBase, workbook);
		//将机票订单表的数据写入到Excel文件中
		writeAirticketOrderToExcel(conn, dataBase, workbook);
		//将单团产品表的数据写入到Excel中
		writeTravelactivityToExcel(conn, dataBase, workbook);
		//将团期表的数据写入到Excel中
		writeActivityGroupToExcel(conn, dataBase, workbook);
		//将团期类订单表的数据写入到Excel中
		writeProductorderToExcel(conn, dataBase, workbook);
		//将签证产品表的数据写入到Excel中
		writeVisaProductsToExcel(conn, dataBase, workbook);
		//将签证订单表的数据写入到Excel中
		writeVisaOrderToExcel(conn, dataBase, workbook);
		//将老审批的数据写入到Excle中
		writeReviewToExcel(conn, dataBase, workbook);
		//将老审批详细数据写入到Excel中
		writeReviewDetailToExcel(conn, dataBase, workbook);
		//将新审批的数据导入到Excel中
		writeReviewNewToExcel(conn, dataBase, workbook);
		//将审批处理的数据导入到Excel中
		writeReviewProcessToExcel(conn, dataBase, workbook);
		//将地接社的信息导入的Excel中
		writeSupplierinfoToExcel(conn, dataBase, workbook);
		//将渠道商的数据写入到Excel文件中
		writeAgentinfoToExcel(conn, dataBase, workbook);
		//将用户信表的数据写入到Excel文件中
		writeSysuserToExcel(conn, dataBase, workbook);
		
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(dataFileName));
			workbook.write(out);
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != conn){
					conn.close();
				}
				if(null != out){
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dataFileName;
	}

	/**
	 * 将机票表的数据写入到Excel
	 * @param conn			数据库连接
	 * @param dataBase		数据库
	 * @param workbook		workBook对象
	 * @author shijun.liu
	 * @date 2016.01.28
	 */
	private void writeActivityAirticketToExcel(Connection conn, String dataBase, Workbook workbook){
		String tableName = "activity_airticket";
		//查询每个表的所有字段
		List<String> columns = tableToExcel.getColumns(conn, tableName, dataBase);
		//将表结构写入到Excel文件中
		columnsToExcel(workbook, tableName, columns);
		int totalCount = tableToExcel.getActivityAirticketTableDataCount(conn, dataBase);
		if(0 == totalCount){
			return;
		}
		int page = totalCount/SIZE;
		if(0 == page){
			//查询表的数据
			List<List<String>> dataList = tableToExcel.getActivityAirticketTableData(conn, dataBase, columns, 0, totalCount);
			//将数据写入到Excel中
			dataToExcel(workbook, tableName, dataList, 0);
		}else{
			for (int j = 0; j <= page; j++) {
				int start = j * SIZE;
				//查询表的数据
				List<List<String>> dataList = tableToExcel.getActivityAirticketTableData(conn, dataBase, columns, start, SIZE);
				//将数据写入到Excel中
				dataToExcel(workbook, tableName, dataList, start);
			}
		}
	}
	
	/**
	 * 将机票订单表的数据写入到Excel
	 * @param conn			数据库连接
	 * @param dataBase		数据库
	 * @param workbook		workBook对象
	 * @author shijun.liu
	 * @date 2016.01.28
	 */
	private void writeAirticketOrderToExcel(Connection conn, String dataBase, Workbook workbook){
		String tableName = "airticket_order";
		//查询每个表的所有字段
		List<String> columns = tableToExcel.getColumns(conn, tableName, dataBase);
		//将表结构写入到Excel文件中
		columnsToExcel(workbook, tableName, columns);
		int totalCount = tableToExcel.getAirticketOrderTableDataCount(conn, dataBase);
		if(0 == totalCount){
			return;
		}
		int page = totalCount/SIZE;
		if(0 == page){
			//查询表的数据
			List<List<String>> dataList = tableToExcel.getAirticketOrderTableData(conn, dataBase, columns, 0, totalCount);
			//将数据写入到Excel中
			dataToExcel(workbook, tableName, dataList, 0);
		}else{
			for (int j = 0; j <= page; j++) {
				int start = j * SIZE;
				//查询表的数据
				List<List<String>> dataList = tableToExcel.getAirticketOrderTableData(conn, dataBase, columns, start, SIZE);
				//将数据写入到Excel中
				dataToExcel(workbook, tableName, dataList, start);
			}
		}
	}
	
	/**
	 * 将单团产品表的数据写入到Excel
	 * @param conn			数据库连接
	 * @param dataBase		数据库
	 * @param workbook		workBook对象
	 * @author shijun.liu
	 * @date 2016.01.28
	 */
	private void writeTravelactivityToExcel(Connection conn, String dataBase, Workbook workbook){
		String tableName = "travelactivity";
		//查询每个表的所有字段
		List<String> columns = tableToExcel.getColumns(conn, tableName, dataBase);
		//将表结构写入到Excel文件中
		columnsToExcel(workbook, tableName, columns);
		int totalCount = tableToExcel.getTravelactivityTableDataCount(conn, dataBase);
		if(0 == totalCount){
			return;
		}
		int page = totalCount/SIZE;
		if(0 == page){
			//查询表的数据
			List<List<String>> dataList = tableToExcel.getTravelactivityTableData(conn, dataBase, columns, 0, totalCount);
			//将数据写入到Excel中
			dataToExcel(workbook, tableName, dataList, 0);
		}else{
			for (int j = 0; j <= page; j++) {
				int start = j * SIZE;
				//查询表的数据
				List<List<String>> dataList = tableToExcel.getTravelactivityTableData(conn, dataBase, columns, start, SIZE);
				//将数据写入到Excel中
				dataToExcel(workbook, tableName, dataList, start);
			}
		}
	}
	
	/**
	 * 将单团团期表的数据写入到Excel
	 * @param conn			数据库连接
	 * @param dataBase		数据库
	 * @param workbook		workBook对象
	 * @author shijun.liu
	 * @date 2016.01.28
	 */
	private void writeActivityGroupToExcel(Connection conn, String dataBase, Workbook workbook){
		String tableName = "activitygroup";
		//查询每个表的所有字段
		List<String> columns = tableToExcel.getColumns(conn, tableName, dataBase);
		//将表结构写入到Excel文件中
		columnsToExcel(workbook, tableName, columns);
		int totalCount = tableToExcel.getActivityGroupTableDataCount(conn, dataBase);
		if(0 == totalCount){
			return;
		}
		int page = totalCount/SIZE;
		if(0 == page){
			//查询表的数据
			List<List<String>> dataList = tableToExcel.getActivityGroupTableData(conn, dataBase, columns, 0, totalCount);
			//将数据写入到Excel中
			dataToExcel(workbook, tableName, dataList, 0);
		}else{
			for (int j = 0; j <= page; j++) {
				int start = j * SIZE;
				//查询表的数据
				List<List<String>> dataList = tableToExcel.getActivityGroupTableData(conn, dataBase, columns, start, SIZE);
				//将数据写入到Excel中
				dataToExcel(workbook, tableName, dataList, start);
			}
		}
	}
	
	/**
	 * 将单团订单表的数据写入到Excel
	 * @param conn			数据库连接
	 * @param dataBase		数据库
	 * @param workbook		workBook对象
	 * @author shijun.liu
	 * @date 2016.01.28
	 */
	private void writeProductorderToExcel(Connection conn, String dataBase, Workbook workbook){
		String tableName = "productorder";
		//查询每个表的所有字段
		List<String> columns = tableToExcel.getColumns(conn, tableName, dataBase);
		//将表结构写入到Excel文件中
		columnsToExcel(workbook, tableName, columns);
		int totalCount = tableToExcel.getProductOrderTableDataCount(conn, dataBase);
		if(0 == totalCount){
			return;
		}
		int page = totalCount/SIZE;
		if(0 == page){
			//查询表的数据
			List<List<String>> dataList = tableToExcel.getProductOrderTableData(conn, dataBase, columns, 0, totalCount);
			//将数据写入到Excel中
			dataToExcel(workbook, tableName, dataList, 0);
		}else{
			for (int j = 0; j <= page; j++) {
				int start = j * SIZE;
				//查询表的数据
				List<List<String>> dataList = tableToExcel.getProductOrderTableData(conn, dataBase, columns, start, SIZE);
				//将数据写入到Excel中
				dataToExcel(workbook, tableName, dataList, start);
			}
		}
	}
	
	/**
	 * 将签证产品表的数据写入到Excel
	 * @param conn			数据库连接
	 * @param dataBase		数据库
	 * @param workbook		workBook对象
	 * @author shijun.liu
	 * @date 2016.01.28
	 */
	private void writeVisaProductsToExcel(Connection conn, String dataBase, Workbook workbook){
		String tableName = "visa_products";
		//查询每个表的所有字段
		List<String> columns = tableToExcel.getColumns(conn, tableName, dataBase);
		//将表结构写入到Excel文件中
		columnsToExcel(workbook, tableName, columns);
		int totalCount = tableToExcel.getVisaProductsTableDataCount(conn, dataBase);
		if(0 == totalCount){
			return;
		}
		int page = totalCount/SIZE;
		if(0 == page){
			//查询表的数据
			List<List<String>> dataList = tableToExcel.getVisaProductsTableData(conn, dataBase, columns, 0, totalCount);
			//将数据写入到Excel中
			dataToExcel(workbook, tableName, dataList, 0);
		}else{
			for (int j = 0; j <= page; j++) {
				int start = j * SIZE;
				//查询表的数据
				List<List<String>> dataList = tableToExcel.getVisaProductsTableData(conn, dataBase, columns, start, SIZE);
				//将数据写入到Excel中
				dataToExcel(workbook, tableName, dataList, start);
			}
		}
	}
	
	/**
	 * 将签证订单表的数据写入到Excel
	 * @param conn			数据库连接
	 * @param dataBase		数据库
	 * @param workbook		workBook对象
	 * @author shijun.liu
	 * @date 2016.01.28
	 */
	private void writeVisaOrderToExcel(Connection conn, String dataBase, Workbook workbook){
		String tableName = "visa_order";
		//查询每个表的所有字段
		List<String> columns = tableToExcel.getColumns(conn, tableName, dataBase);
		//将表结构写入到Excel文件中
		columnsToExcel(workbook, tableName, columns);
		int totalCount = tableToExcel.getVisaOrderTableDataCount(conn, dataBase);
		if(0 == totalCount){
			return;
		}
		int page = totalCount/SIZE;
		if(0 == page){
			//查询表的数据
			List<List<String>> dataList = tableToExcel.getVisaOrderTableData(conn, dataBase, columns, 0, totalCount);
			//将数据写入到Excel中
			dataToExcel(workbook, tableName, dataList, 0);
		}else{
			for (int j = 0; j <= page; j++) {
				int start = j * SIZE;
				//查询表的数据
				List<List<String>> dataList = tableToExcel.getVisaOrderTableData(conn, dataBase, columns, start, SIZE);
				//将数据写入到Excel中
				dataToExcel(workbook, tableName, dataList, start);
			}
		}
	}
	
	/**
	 * 将旧审批表的数据写入到Excel
	 * @param conn			数据库连接
	 * @param dataBase		数据库
	 * @param workbook		workBook对象
	 * @author shijun.liu
	 * @date 2016.01.28
	 */
	private void writeReviewToExcel(Connection conn, String dataBase, Workbook workbook){
		String tableName = "review";
		//查询每个表的所有字段
		List<String> columns = tableToExcel.getColumns(conn, tableName, dataBase);
		//将表结构写入到Excel文件中
		columnsToExcel(workbook, tableName, columns);
		int totalCount = tableToExcel.getReviewTableDataCount(conn, dataBase);
		if(0 == totalCount){
			return;
		}
		int page = totalCount/SIZE;
		if(0 == page){
			//查询表的数据
			List<List<String>> dataList = tableToExcel.getReviewTableData(conn, dataBase, columns, 0, totalCount);
			//将数据写入到Excel中
			dataToExcel(workbook, tableName, dataList, 0);
		}else{
			for (int j = 0; j <= page; j++) {
				int start = j * SIZE;
				//查询表的数据
				List<List<String>> dataList = tableToExcel.getReviewTableData(conn, dataBase, columns, start, SIZE);
				//将数据写入到Excel中
				dataToExcel(workbook, tableName, dataList, start);
			}
		}
	}
	
	/**
	 * 将旧审批详情表的数据写入到Excel
	 * @param conn			数据库连接
	 * @param dataBase		数据库
	 * @param workbook		workBook对象
	 * @author shijun.liu
	 * @date 2016.01.28
	 */
	private void writeReviewDetailToExcel(Connection conn, String dataBase, Workbook workbook){
		String tableName = "review_detail";
		//查询每个表的所有字段
		List<String> columns = tableToExcel.getColumns(conn, tableName, dataBase);
		//将表结构写入到Excel文件中
		columnsToExcel(workbook, tableName, columns);
		int totalCount = tableToExcel.getReviewDetailTableDataCount(conn, dataBase);
		if(0 == totalCount){
			return;
		}
		int page = totalCount/SIZE;
		if(0 == page){
			//查询表的数据
			List<List<String>> dataList = tableToExcel.getReviewDetailTableData(conn, dataBase, columns, 0, totalCount);
			//将数据写入到Excel中
			dataToExcel(workbook, tableName, dataList, 0);
		}else{
			for (int j = 0; j <= page; j++) {
				int start = j * SIZE;
				//查询表的数据
				List<List<String>> dataList = tableToExcel.getReviewDetailTableData(conn, dataBase, columns, start, SIZE);
				//将数据写入到Excel中
				dataToExcel(workbook, tableName, dataList, start);
			}
		}
	}
	
	/**
	 * 将新审批表的数据写入到Excel
	 * @param conn			数据库连接
	 * @param dataBase		数据库
	 * @param workbook		workBook对象
	 * @author shijun.liu
	 * @date 2016.01.28
	 */
	private void writeReviewNewToExcel(Connection conn, String dataBase, Workbook workbook){
		String tableName = "review_new";
		//查询每个表的所有字段
		List<String> columns = tableToExcel.getColumns(conn, tableName, dataBase);
		//将表结构写入到Excel文件中
		columnsToExcel(workbook, tableName, columns);
		int totalCount = tableToExcel.getReviewNewTableDataCount(conn, dataBase);
		if(0 == totalCount){
			return;
		}
		int page = totalCount/SIZE;
		if(0 == page){
			//查询表的数据
			List<List<String>> dataList = tableToExcel.getReviewNewTableData(conn, dataBase, columns, 0, totalCount);
			//将数据写入到Excel中
			dataToExcel(workbook, tableName, dataList, 0);
		}else{
			for (int j = 0; j <= page; j++) {
				int start = j * SIZE;
				//查询表的数据
				List<List<String>> dataList = tableToExcel.getReviewNewTableData(conn, dataBase, columns, start, SIZE);
				//将数据写入到Excel中
				dataToExcel(workbook, tableName, dataList, start);
			}
		}
	}
	
	/**
	 * 将新审批处理流程表的数据写入到Excel
	 * @param conn			数据库连接
	 * @param dataBase		数据库
	 * @param workbook		workBook对象
	 * @author shijun.liu
	 * @date 2016.01.28
	 */
	private void writeReviewProcessToExcel(Connection conn, String dataBase, Workbook workbook){
		String tableName = "review_process";
		//查询每个表的所有字段
		List<String> columns = tableToExcel.getColumns(conn, tableName, dataBase);
		//将表结构写入到Excel文件中
		columnsToExcel(workbook, tableName, columns);
		int totalCount = tableToExcel.getReviewProcessTableDataCount(conn, dataBase);
		if(0 == totalCount){
			return;
		}
		int page = totalCount/SIZE;
		if(0 == page){
			//查询表的数据
			List<List<String>> dataList = tableToExcel.getReviewProcessTableData(conn, dataBase, columns, 0, totalCount);
			//将数据写入到Excel中
			dataToExcel(workbook, tableName, dataList, 0);
		}else{
			for (int j = 0; j <= page; j++) {
				int start = j * SIZE;
				//查询表的数据
				List<List<String>> dataList = tableToExcel.getReviewProcessTableData(conn, dataBase, columns, start, SIZE);
				//将数据写入到Excel中
				dataToExcel(workbook, tableName, dataList, start);
			}
		}
	}
	
	/**
	 * 将地接社表的数据写入到Excel
	 * @param conn			数据库连接
	 * @param dataBase		数据库
	 * @param workbook		workBook对象
	 * @author shijun.liu
	 * @date 2016.01.28
	 */
	private void writeSupplierinfoToExcel(Connection conn, String dataBase, Workbook workbook){
		String tableName = "supplier_info";
		//查询每个表的所有字段
		List<String> columns = tableToExcel.getColumns(conn, tableName, dataBase);
		//将表结构写入到Excel文件中
		columnsToExcel(workbook, tableName, columns);
		int totalCount = tableToExcel.getSupplierInfoTableDataCount(conn, dataBase);
		if(0 == totalCount){
			return;
		}
		int page = totalCount/SIZE;
		if(0 == page){
			//查询表的数据
			List<List<String>> dataList = tableToExcel.getSupplierInfoTableData(conn, dataBase, columns, 0, totalCount);
			//将数据写入到Excel中
			dataToExcel(workbook, tableName, dataList, 0);
		}else{
			for (int j = 0; j <= page; j++) {
				int start = j * SIZE;
				//查询表的数据
				List<List<String>> dataList = tableToExcel.getSupplierInfoTableData(conn, dataBase, columns, start, SIZE);
				//将数据写入到Excel中
				dataToExcel(workbook, tableName, dataList, start);
			}
		}
	}
	
	/**
	 * 将渠道商表的数据写入到Excel
	 * @param conn			数据库连接
	 * @param dataBase		数据库
	 * @param workbook		workBook对象
	 * @author shijun.liu
	 * @date 2016.01.28
	 */
	private void writeAgentinfoToExcel(Connection conn, String dataBase, Workbook workbook){
		String tableName = "agentinfo";
		//查询每个表的所有字段
		List<String> columns = tableToExcel.getColumns(conn, tableName, dataBase);
		//将表结构写入到Excel文件中
		columnsToExcel(workbook, tableName, columns);
		int totalCount = tableToExcel.getAgentInfoTableDataCount(conn, dataBase);
		if(0 == totalCount){
			return;
		}
		int page = totalCount/SIZE;
		if(0 == page){
			//查询表的数据
			List<List<String>> dataList = tableToExcel.getAgentInfoTableData(conn, dataBase, columns, 0, totalCount);
			//将数据写入到Excel中
			dataToExcel(workbook, tableName, dataList, 0);
		}else{
			for (int j = 0; j <= page; j++) {
				int start = j * SIZE;
				//查询表的数据
				List<List<String>> dataList = tableToExcel.getAgentInfoTableData(conn, dataBase, columns, start, SIZE);
				//将数据写入到Excel中
				dataToExcel(workbook, tableName, dataList, start);
			}
		}
	}
	
	/**
	 * 将用户表的数据写入到Excel
	 * @param conn			数据库连接
	 * @param dataBase		数据库
	 * @param workbook		workBook对象
	 * @author shijun.liu
	 * @date 2016.01.28
	 */
	private void writeSysuserToExcel(Connection conn, String dataBase, Workbook workbook){
		String tableName = "sys_user";
		//查询每个表的所有字段
		List<String> columns = tableToExcel.getColumns(conn, tableName, dataBase);
		//将表结构写入到Excel文件中
		columnsToExcel(workbook, tableName, columns);
		int totalCount = tableToExcel.getSysUserTableDataCount(conn, dataBase);
		if(0 == totalCount){
			return;
		}
		int page = totalCount/SIZE;
		if(0 == page){
			//查询表的数据
			List<List<String>> dataList = tableToExcel.getSysUserTableData(conn, dataBase, columns, 0, totalCount);
			//将数据写入到Excel中
			dataToExcel(workbook, tableName, dataList, 0);
		}else{
			for (int j = 0; j <= page; j++) {
				int start = j * SIZE;
				//查询表的数据
				List<List<String>> dataList = tableToExcel.getSysUserTableData(conn, dataBase, columns, start, SIZE);
				//将数据写入到Excel中
				dataToExcel(workbook, tableName, dataList, start);
			}
		}
	}
	
	@Override
	public String exportDataToExcelByThread() {
		
		//new ArrayBlockingQueue<Runnable>(10)
		//ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 10, 200, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
		ExecutorService service = Executors.newFixedThreadPool(10);
		String dataFileName = Utils.createDataFileName();
		//防止内存溢出，使用SXSSFWorkbook对象
		SXSSFWorkbook workbook = new SXSSFWorkbook(new XSSFWorkbook(),1000);
		//查询当前公司所有数据所在的数据库
		String dataBase = tableToExcel.getDataBase();
		//查询该数据库下面的表
		List<String> tables = tableToExcel.getTables(dataBase);
		Connection conn = JDBCUtils.getInstance().getMysqlConn();
		for (int i = 0; i < tables.size(); i++) {
			String sheetName = Utils.getSheetName(tables.get(i));
			Sheet sheet = workbook.createSheet(sheetName);
			service.submit(new ExportDataThread(sheet, conn, dataBase, tables.get(i)));
		}
//		System.out.println("over");
		//将机票产品表的数据写入到Excel文件中
		writeActivityAirticketToExcel(conn, dataBase, workbook);
		//将机票订单表的数据写入到Excel文件中
		writeAirticketOrderToExcel(conn, dataBase, workbook);
		//将单团产品表的数据写入到Excel中
		writeTravelactivityToExcel(conn, dataBase, workbook);
		//将团期表的数据写入到Excel中
		writeActivityGroupToExcel(conn, dataBase, workbook);
		//将团期类订单表的数据写入到Excel中
		writeProductorderToExcel(conn, dataBase, workbook);
		//将签证产品表的数据写入到Excel中
		writeVisaProductsToExcel(conn, dataBase, workbook);
		//将签证订单表的数据写入到Excel中
		writeVisaOrderToExcel(conn, dataBase, workbook);
		//将老审批的数据写入到Excle中
		writeReviewToExcel(conn, dataBase, workbook);
		//将老审批详细数据写入到Excel中
		writeReviewDetailToExcel(conn, dataBase, workbook);
		//将新审批的数据导入到Excel中
		writeReviewNewToExcel(conn, dataBase, workbook);
		//将审批处理的数据导入到Excel中
		writeReviewProcessToExcel(conn, dataBase, workbook);
		//将地接社的信息导入的Excel中
		writeSupplierinfoToExcel(conn, dataBase, workbook);
		//将渠道商的数据写入到Excel文件中
		writeAgentinfoToExcel(conn, dataBase, workbook);
		//将用户信表的数据写入到Excel文件中
		writeSysuserToExcel(conn, dataBase, workbook);
		
		OutputStream out = null;
		try {
			out = new BufferedOutputStream(new FileOutputStream(dataFileName));
			workbook.write(out);
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != conn){
					conn.close();
				}
				if(null != out){
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return dataFileName;
	}
}
