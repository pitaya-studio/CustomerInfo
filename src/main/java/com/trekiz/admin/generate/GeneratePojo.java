package com.trekiz.admin.generate;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.druid.pool.DruidDataSource;
import com.trekiz.admin.common.utils.FileUtils;

 /**
 *  文件名: GeneratePojo.java
 *  功能:
 *      创建pojo类方法   
 *      如果单个pojo生成 可以支持类名和表名不一致
 *      如果是多个pojo生成只支持类名和表名一致的情况   使用前请熟悉该类功能。<br>
 *      存在问题:1、由于使用的是spring的datasource 用到了hibernate的缓存<br>   
 *      程序在运行结束后会产生后台运行线程，需要主动关闭才能停止。
 *  修改记录:   
 *  
 *  @author xuziqian
 *  @DateTime 2013-11-28 下午3:38:23
 *  @version 1.0
 */
public class GeneratePojo {
    
    private static Logger logger = LoggerFactory.getLogger(GeneratePojo.class);
    ApplicationContext acx = null;
    DruidDataSource datasource = null;
    
    public GeneratePojo() {
        //production   使用前需要开启该域
        System.setProperty("spring.profiles.active", "production");
        if (acx == null) {
            acx = new ClassPathXmlApplicationContext("/applicationContext_bak.xml");
        }
        if (datasource == null) {
            datasource = (DruidDataSource) acx.getBean("dataSource");
        }
    }
    
    public DruidDataSource getDatasource(){
        return datasource;
    }
    
    /**
     * 将内容写入文件
     * @param content
     * @param filePath
     */
    public static void writeFile(String content, String filePath) {
        try {
            if (FileUtils.createFile(filePath)) {
                FileWriter fileWriter = new FileWriter(filePath, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                // 处理get set
                bufferedWriter.write(content);
                 bufferedWriter.close();
                 fileWriter.close();
            } else {
                logger.info("生成失败，文件已存在！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
     /**
     *  功能:
     *  获取连接
     *  @author xuziqian
     *  @DateTime 2013-11-28 下午2:21:35
     *  @return
     *  @throws SQLException
     */
    public Connection getConnect() throws SQLException {
        // 获取链接
        Connection connect = datasource.getConnection().getConnection();
        return connect;
    }
    
    /**
     * 生成首字母大写的字符串 例如：Username
     * 
     * @param str
     *            格式化字符串
     * @return str 式后的字符串
     */
    public String makeSpecialColname(String str) {
        String tempStr = str;
        tempStr = tempStr.substring(1, tempStr.length());
        str = str.substring(0, 1).toUpperCase() + tempStr;
        return str;
    }
    
}
