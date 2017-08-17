package com.trekiz.admin.modules.statistics.databse.service;

import java.util.List;

import org.apache.poi.ss.usermodel.Workbook;

public interface ITableToExcelService {

	/**
	 * 将数据表的列名称
	 * @param columns
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public Workbook columnsToExcel(Workbook workbook, String tableName, List<String> columns);
	
	/**
	 * 将数据写入到Excel
	 * @param workbook		Excel文件
	 * @param tableName		表名称
	 * @param data			数据
	 * @param index			写入数据的行数
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public Workbook dataToExcel(Workbook workbook, String tableName, List<List<String>> data, int index);
	
	/**
	 * 将数据导出到Excel文件
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public String exportDataToExcel();
	
	/**
	 * 将数据导出到Excel文件（多线程）
	 * @return
	 * @author shijun.liu
	 * @date 2016.02.02
	 */
	public String exportDataToExcelByThread();
	
}
