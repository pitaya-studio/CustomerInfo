package com.trekiz.admin.modules.sys.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * // 连接 mysql 数据库，数据库地址、用户名和密码写在配置文件中
 * @author Administrator
 *
 */
public class ConnectionMysqlByJDBC {
	
	public static Connection getConnectDB() {
		Connection conn = null;
		try {
			String driver = PropertyManager.getProperty("jdbc.driver");
			String url = PropertyManager.getProperty("jdbc.url");
			String user = PropertyManager.getProperty("jdbc.username");
			String pwd = PropertyManager.getProperty("jdbc.password");
			
			Class.forName(driver).newInstance();
			
			conn = DriverManager.getConnection(url, user, pwd);
//			System.out.println("url: " + url + "\nuser: " + user + "\npwd: "  + pwd);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	public void closeConn(Connection conn){
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void closeConn(Connection conn,Statement stat,ResultSet rs){
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(stat!=null){
			try {
				stat.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		getConnectDB();
	}
}