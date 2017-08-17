package com.trekiz.admin.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * jdbc方式连接数据库的工具类
 * @author shijun.liu
 *
 */
public class JDBCUtils {

	private static final JDBCUtils JDBC_UTILS = new JDBCUtils();
	private static final Properties P = new Properties();
	static{
		InputStream in = null;
		try {
			in = JDBCUtils.class.getClassLoader().getResourceAsStream("application.properties");
			P.load(new InputStreamReader(in));
			Class.forName(P.getProperty("jdbc.driver")).newInstance();
			in.close();
		} catch (IOException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e1) {
					in = null;
					e1.printStackTrace();
				}
			}
			e.printStackTrace();
		}
	}
	
	/**
	 * 构造方法私有化
	 */
	private JDBCUtils(){}
	
	/**
	 * 获取该类的实例
	 * @return
	 */
	public static JDBCUtils getInstance(){
		return JDBC_UTILS;
	}
	
	/**
	 * 获取数据库连接
	 * @return
	 */
	public Connection getMysqlConn(){
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(P.getProperty("jdbc.url"), 
					P.getProperty("jdbc.username"), 
					P.getProperty("jdbc.password"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 获取数据库连接
	 * @return
	 */
	public Connection getMysqlConn1_1(){
		String url = "jdbc:mysql://10.10.20.52:3306/trekizwholesaler?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull";
		String user = "trekizeuser";
		String password = "siruichuangtu20140606";
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 关闭数据库连接，以及数据库对象
	 * @param conn
	 * @param stmt
	 * @param rs
	 */
	public static void close(Connection conn, Statement stmt, ResultSet rs){
		
		try {
			if(null != rs){
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != stmt){
					stmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					if(null != conn){
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 关闭数据库连接，以及数据库对象
	 * @param conn
	 * @param stmt
	 * @param rs
	 */
	public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs){
		
		try {
			if(null != rs){
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != pstmt){
					pstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					if(null != conn){
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 关闭数据库连接，以及数据库对象
	 * @param conn
	 * @param stmt
	 * @param rs
	 */
	public static void close(Connection conn, CallableStatement cstmt, ResultSet rs){
		
		try {
			if(null != rs){
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			try {
				if(null != cstmt){
					cstmt.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}finally{
				try {
					if(null != conn){
						conn.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
