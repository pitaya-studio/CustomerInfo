package com.trekiz.admin.modules.statistics.databse.repository;

import java.sql.Connection;
import java.util.List;

/**
 * 数据库表导出Excel文件的Dao接口
 * @author shijun.liu 
 * @date 2016.01.26
 */
public interface ITableToExcelDao {

	/**
	 * 根据批发商ID或者UUID其数据所在的库
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.26
	 */
	public String getDataBase();
	
	/**
	 * 根据数据库查询数据库中的表名称
	 * @param database
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.26
	 */
	public List<String> getTables(String database);
	
	/**
	 * 查询不需要导出的数据库表
	 * @param database
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public List<String> noNeedExport(String database);
	
	/**
	 * 查询需要单独处理的数据库表
	 * @param database
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public List<String> aloneExport(String database);
	
	/**
	 * 根据表名称查询其表字段
	 * @param conn			数据库连接
	 * @param tableName		表名称
	 * @param database		数据库
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public List<String> getColumns(Connection conn, String tableName, String database);
	
	/**
	 * 查询表数据
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @param tableName		表名称
	 * @param columns		列字段
	 * @param start			开始行
	 * @param size			行数
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public List<List<String>> getTableData(Connection conn, String dataBase, String tableName, List<String> columns, int start, int size);
	
	/**
	 * 查询表数据
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @param tableName		表名称
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.28
	 */
	public int getTableDataCount(Connection conn, String dataBase, String tableName);
	
	/**
	 * 根据批发商查询机票产品表数据
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @param columns		列字段
	 * @param start			开始行
	 * @param size			行数
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public List<List<String>> getActivityAirticketTableData(Connection conn, String dataBase, List<String> columns, int start, int size);
	
	/**
	 * 根据批发商查询机票产品表数据总数
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public int getActivityAirticketTableDataCount(Connection conn, String dataBase);
	
	/**
	 * 根据批发商查询机票订单数据总数
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @param columns		列字段
	 * @param start			开始行
	 * @param size			行数
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public List<List<String>> getAirticketOrderTableData(Connection conn, String dataBase, List<String> columns, int start, int size);
	
	/**
	 * 根据批发商查询机票订单数据
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public int getAirticketOrderTableDataCount(Connection conn, String dataBase);
	
	/**
	 * 根据批发商查询团期产品表信息
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @param columns		列字段
	 * @param start			开始行
	 * @param size			行数
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public List<List<String>> getTravelactivityTableData(Connection conn, String dataBase, List<String> columns, int start, int size);
	
	/**
	 * 根据批发商查询团期产品表信息总数
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public int getTravelactivityTableDataCount(Connection conn, String dataBase);
	
	/**
	 * 根据批发商查询团期数据
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @param columns		列字段
	 * @param start			开始行
	 * @param size			行数
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public List<List<String>> getActivityGroupTableData(Connection conn, String dataBase, List<String> columns, int start, int size);
	
	/**
	 * 根据批发商查询团期数据总数
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public int getActivityGroupTableDataCount(Connection conn, String dataBase);
	
	/**
	 * 根据批发商查询团期类订单数据
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @param columns		列字段
	 * @param start			开始行
	 * @param size			行数
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public List<List<String>> getProductOrderTableData(Connection conn, String dataBase, List<String> columns, int start, int size);
	
	/**
	 * 根据批发商查询团期类订单数据总数
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public int getProductOrderTableDataCount(Connection conn, String dataBase);
	
	/**
	 * 根据批发商查询签证产品表信息
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @param columns		列字段
	 * @param start			开始行
	 * @param size			行数
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public List<List<String>> getVisaProductsTableData(Connection conn, String dataBase, List<String> columns, int start, int size);
	
	/**
	 * 根据批发商查询签证产品表信息总数
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public int getVisaProductsTableDataCount(Connection conn, String dataBase);
	
	/**
	 * 根据批发商查询签证订单表信息
	 * @param dataBase		数据库名称
	 * @param columns		列字段
	 * @param start			开始行
	 * @param size			行数
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public List<List<String>> getVisaOrderTableData(Connection conn, String dataBase, List<String> columns, int start, int size);
	
	/**
	 * 根据批发商查询签证订单表信息总数
	 * @param dataBase		数据库名称
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public int getVisaOrderTableDataCount(Connection conn, String dataBase);
	
	/**
	 * 根据批发商查询旧审批数据
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @param columns		列字段
	 * @param start			开始行
	 * @param size			行数
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public List<List<String>> getReviewTableData(Connection conn, String dataBase, List<String> columns, int start, int size);
	
	/**
	 * 根据批发商查询旧审批数据总数
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public int getReviewTableDataCount(Connection conn, String dataBase);
	
	/**
	 * 根据批发商查询旧审批详情数据
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @param columns		列字段
	 * @param start			开始行
	 * @param size			行数目
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public List<List<String>> getReviewDetailTableData(Connection conn, String dataBase, List<String> columns, int start, int size);
	
	/**
	 * 根据批发商查询旧审批详情数据总数
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public int getReviewDetailTableDataCount(Connection conn, String dataBase);
	
	/**
	 * 根据批发商查询新审批数据
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @param columns		列字段
	 * @param start			开始行
	 * @param size			行数
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public List<List<String>> getReviewNewTableData(Connection conn, String dataBase, List<String> columns, int start, int size);
	
	/**
	 * 根据批发商查询新审批数据总数
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public int getReviewNewTableDataCount(Connection conn, String dataBase);
	
	/**
	 * 根据批发商查询审批处理流程表信息
	 * @param dataBase		数据库名称
	 * @param columns		列字段
	 * @param start			开始行
	 * @param size			行数
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public List<List<String>> getReviewProcessTableData(Connection conn, String dataBase, List<String> columns, int start, int size);
	
	/**
	 * 根据批发商查询审批处理流程表信息总数
	 * @param dataBase		数据库名称
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public int getReviewProcessTableDataCount(Connection conn, String dataBase);
	
	/**
	 * 根据批发商查询地接社数据信息
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @param columns		列字段
	 * @param start			开始行
	 * @param size			行数
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public List<List<String>> getSupplierInfoTableData(Connection conn, String dataBase, List<String> columns, int start, int size);
	
	/**
	 * 根据批发商查询地接社数据信息总数
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public int getSupplierInfoTableDataCount(Connection conn, String dataBase);
	
	/**
	 * 根据批发商查询渠道商数据信息
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @param columns		列字段
	 * @param start			开始行
	 * @param size			行数
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public List<List<String>> getAgentInfoTableData(Connection conn, String dataBase, List<String> columns, int start, int size);
	
	/**
	 * 根据批发商查询渠道商数据信息总数
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public int getAgentInfoTableDataCount(Connection conn, String dataBase);
	
	/**
	 * 根据批发商查询用户数据信息
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @param columns		列字段
	 * @param start			开始行
	 * @param size			数目
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public List<List<String>> getSysUserTableData(Connection conn, String dataBase, List<String> columns, int start, int size);
	
	/**
	 * 根据批发商查询用户数据信息总数
	 * @param conn			数据库连接
	 * @param dataBase		数据库名称
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.27
	 */
	public int getSysUserTableDataCount(Connection conn, String dataBase);
}
