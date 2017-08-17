package com.trekiz.admin.modules.statistics.utils;

import java.io.File;

import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.modules.sys.utils.UserUtils;

public class Utils {

	/**
	 * 根据数据库表名称获取sheetName名称
	 * @param tableName		数据库表名称
	 * @return
	 * @author shijun.liu
	 * @data 2016.01.27
	 */
	public static String getSheetName(String tableName){
		String sheetName = tableName;
		//Excel sheet表名最大长度为31个字符，表名称超过31字符时进行截取
		if(sheetName.length() > 31){
			sheetName = sheetName.substring(0, 31);
		}
		return sheetName;
	}
	
	/**
	 * 获取Excel文件所在路径
	 * @return
	 * @author shijun.liu
	 * @data 2016.01.27
	 */
	public static String getFilePath(){
		//文件绝对路径根目录
		String basePath = Global.getBasePath();		
		//文件根目录
		String fileFolder = Global.getUploadPath();
		String pathPrefix = basePath + File.separator + fileFolder + File.separator + "table_to_excel";
		File file = new File(pathPrefix);
		if(!file.exists()){
			file.mkdirs();
		}
		return pathPrefix;
	}
	
	/**
	 * 生成Excel表结构的Excel文件路径(无数据)
	 * @return
	 * @author shijun.liu
	 * @data 2016.01.27
	 */
	public static String createModelFileName(){
		Long companyId = UserUtils.getUser().getCompany().getId();
		Long userId = UserUtils.getUser().getId();
		String fileName = getFilePath() + File.separator + "templete_" + companyId + "_" + userId + ".xlsx";
		return fileName;
	}
	
	/**
	 * 生成存放数据的Excel文件路径
	 * @return
	 * @author shijun.liu
	 * @data 2016.01.27
	 */
	public static String createDataFileName(){
		Long companyId = UserUtils.getUser().getCompany().getId();
		Long userId = UserUtils.getUser().getId();
		String fileName = getFilePath() + File.separator + "data_" + companyId + "_" + userId + ".xlsx";
		//String fileName = "D:/t" + File.separator + "data_" + companyId + "_" + userId + ".xlsx";
		return fileName;
	}
	
}
