package com.trekiz.admin.modules.cost.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.ibm.icu.math.BigDecimal;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.review.pay.bean.TravelerBean;

/**
 * 
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 *
 * 财务模块，操作Excel表格的工具类
 * @author shijun.liu
 * @date 2015年11月13日
 */
public class CostExcelUtils {

	/**
	 * 将应收账款账龄数据导入到Excel文件中
	 * @param list
	 * @return
	 * @author shijun.liu
	 * @date 2015年11月13日
	 */
	public static Workbook createReceiveExcel(List<Map<String, Object>> list){
		String fontStyle = "Courier New";
    	//表头
    	String[] heads = {"部门","计调","销售","团号",
    			"订单号","渠道","应收总额","回款总额",
    			"应收余额","回款率","应收账期","1-30天","31-60天",
    			"61-90天","91-180天","181-360天","坏账标识"};
    	//创建Excel
    	HSSFWorkbook workBook = new HSSFWorkbook();
    	
    	//表头字体和样式
    	HSSFFont headFont = workBook.createFont();
    	HSSFCellStyle headStyle = workBook.createCellStyle();
    	headFont.setFontHeightInPoints((short)10);
    	headFont.setFontName(fontStyle);
    	headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
    	headStyle.setAlignment(CellStyle.ALIGN_CENTER);
    	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	headStyle.setFont(headFont);
    	headStyle.setBorderBottom(CellStyle.BORDER_THIN);
    	headStyle.setBorderLeft(CellStyle.BORDER_THIN);
    	headStyle.setBorderTop(CellStyle.BORDER_THIN);
    	headStyle.setBorderRight(CellStyle.BORDER_THIN);
    	
    	//订单数据字体和样式
    	HSSFFont dataFont = workBook.createFont();
    	HSSFCellStyle dataStyle = workBook.createCellStyle();
    	dataFont.setFontHeightInPoints((short)10);
    	dataFont.setFontName(fontStyle);
    	dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
    	dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	dataStyle.setFont(dataFont);
    	dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
    	dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
    	dataStyle.setBorderTop(CellStyle.BORDER_THIN);
    	dataStyle.setBorderRight(CellStyle.BORDER_THIN);
    	
    	HSSFCellStyle moneyStyle = workBook.createCellStyle();
    	moneyStyle.setAlignment(CellStyle.ALIGN_RIGHT);
    	moneyStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	moneyStyle.setFont(dataFont);
    	moneyStyle.setBorderBottom(CellStyle.BORDER_THIN);
    	moneyStyle.setBorderLeft(CellStyle.BORDER_THIN);
    	moneyStyle.setBorderTop(CellStyle.BORDER_THIN);
    	moneyStyle.setBorderRight(CellStyle.BORDER_THIN);
    	
    	HSSFSheet sheet = workBook.createSheet();
    	    	
    	HSSFRow headRow = sheet.createRow(0);
    	headRow.setHeightInPoints((short)24);
    	for (int i = 0; i<heads.length; i++) {
    		HSSFCell headCell = headRow.createCell(i);
    		headCell.setCellValue(heads[i]);
        	headCell.setCellStyle(headStyle);
        	sheet.setColumnWidth(i, 5800);
		}
    	if(null == list){
    		return workBook;
    	}
    	for (int i = 0; i < list.size(); i++) {
    		String badAccount = "否";
    		HSSFRow dataRow = sheet.createRow(i+1);
    		dataRow.setHeightInPoints((short)20);
    		createCell(dataRow, 0, list.get(i).get("department"),dataStyle);
    		createCell(dataRow, 1, list.get(i).get("operator"),dataStyle);
    		createCell(dataRow, 2, list.get(i).get("salerName"),dataStyle);
    		createCell(dataRow, 3, list.get(i).get("groupCode"),dataStyle);
    		createCell(dataRow, 4, list.get(i).get("orderNum"),dataStyle);
    		createCell(dataRow, 5, list.get(i).get("agentName"),dataStyle);
    		createCell(dataRow, 6, Context.CURRENCY_MARK_RMB + list.get(i).get("totalMoney"),moneyStyle);
    		createCell(dataRow, 7, Context.CURRENCY_MARK_RMB + list.get(i).get("accountedMoney"),moneyStyle);
    		createCell(dataRow, 8, Context.CURRENCY_MARK_RMB + list.get(i).get("unreceivedMoney"),moneyStyle);
    		createCell(dataRow, 9, list.get(i).get("rate") + "%",dataStyle);
    		createCell(dataRow, 10, list.get(i).get("receivePayDate"),dataStyle);
    		createCell(dataRow, 11, Context.CURRENCY_MARK_RMB + list.get(i).get("accountAge30"),moneyStyle);
    		createCell(dataRow, 12, Context.CURRENCY_MARK_RMB + list.get(i).get("accountAge60"),moneyStyle);
    		createCell(dataRow, 13, Context.CURRENCY_MARK_RMB + list.get(i).get("accountAge90"),moneyStyle);
    		createCell(dataRow, 14, Context.CURRENCY_MARK_RMB + list.get(i).get("accountAge180"),moneyStyle);
    		createCell(dataRow, 15, Context.CURRENCY_MARK_RMB + list.get(i).get("accountAge360"),moneyStyle);
    		Object days = list.get(i).get("days");
    		//去除千分位，然后将其转化为数值
    		BigDecimal unreceive = new BigDecimal(String.valueOf(list.get(i).get("unreceivedMoney")).replaceAll(",", ""));
    		//坏账：大于360天，并且 应收余额 还有值
    		if(null != days && Integer.parseInt(String.valueOf(days)) > 360 
    				&& unreceive.compareTo(new BigDecimal(0)) != 0){
    			badAccount = "是";
    		}
    		createCell(dataRow, 16, badAccount,dataStyle);
		}
    	return workBook;
	}
	
	/**
     * 创建单元格
     * @param row          row对象
     * @param index        列索引
     * @param value        单元格值
     * @param cellStyle    单元格样式
     * @author shijun.liu
     */
    private static void createCell(HSSFRow row, int index, Object value, CellStyle cellStyle){
    	if(null == value){
    		value = "";
    	}
    	HSSFCell cell = row.createCell(index);
    	cell.setCellValue(String.valueOf(value));
    	cell.setCellStyle(cellStyle);
    }
    
    /**
	 * 将应收账款账龄数据导入到Excel文件中
	 * @param list
	 * @return
	 * @author shijun.liu
	 * @date 2015年11月13日
	 */
	public static Workbook createTravelerExcel(Map<String, List<TravelerBean>> map){
		String fontStyle = "Courier New";
    	//表头
    	String[] heads = {"日期", "团号", "客户", "销售", "汇总客人名单", "人数", "借款单价"};
    	//创建Excel
    	HSSFWorkbook workBook = new HSSFWorkbook();
    	
    	//表头字体和样式
    	HSSFFont headFont = workBook.createFont();
    	HSSFCellStyle headStyle = workBook.createCellStyle();
    	headFont.setFontHeightInPoints((short)10);
    	headFont.setFontName(fontStyle);
    	headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
    	headStyle.setAlignment(CellStyle.ALIGN_CENTER);
    	headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	headStyle.setFont(headFont);
    	headStyle.setBorderBottom(CellStyle.BORDER_THIN);
    	headStyle.setBorderLeft(CellStyle.BORDER_THIN);
    	headStyle.setBorderTop(CellStyle.BORDER_THIN);
    	headStyle.setBorderRight(CellStyle.BORDER_THIN);
    	
    	//订单数据字体和样式
    	HSSFFont dataFont = workBook.createFont();
    	HSSFCellStyle dataStyle = workBook.createCellStyle();
    	dataFont.setFontHeightInPoints((short)10);
    	dataFont.setFontName(fontStyle);
    	dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
    	dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	dataStyle.setFont(dataFont);
    	dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
    	dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
    	dataStyle.setBorderTop(CellStyle.BORDER_THIN);
    	dataStyle.setBorderRight(CellStyle.BORDER_THIN);
    	dataStyle.setWrapText(true);
    	
    	HSSFCellStyle moneyStyle = workBook.createCellStyle();
    	moneyStyle.setAlignment(CellStyle.ALIGN_RIGHT);
    	moneyStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
    	moneyStyle.setFont(dataFont);
    	moneyStyle.setBorderBottom(CellStyle.BORDER_THIN);
    	moneyStyle.setBorderLeft(CellStyle.BORDER_THIN);
    	moneyStyle.setBorderTop(CellStyle.BORDER_THIN);
    	moneyStyle.setBorderRight(CellStyle.BORDER_THIN);
    	
    	Set<Entry<String, List<TravelerBean>>> entrySet = map.entrySet();
    	Iterator<Entry<String, List<TravelerBean>>> it = entrySet.iterator();
    	while(it.hasNext()){
    		BigDecimal sumMoney = new BigDecimal("0");
    		Entry<String, List<TravelerBean>> entry = it.next();
    		String visaCountry = entry.getKey();
    		List<TravelerBean> travelers = entry.getValue();
    		String sheetName = visaCountry + "签证明细表";
    		if(sheetName.length() > 32){
    			sheetName = sheetName.substring(0,32);
    		}
    		HSSFSheet sheet = workBook.createSheet(sheetName);
    		HSSFRow headRow = sheet.createRow(0);
        	headRow.setHeightInPoints((short)24);
        	for (int i = 0; i<heads.length; i++) {
        		HSSFCell headCell = headRow.createCell(i);
        		headCell.setCellValue(heads[i]);
            	headCell.setCellStyle(headStyle);
            	if(i == 4){
            		sheet.setColumnWidth(i, 7800);
            	}else{
            		sheet.setColumnWidth(i, 5000);
            	}
    		}
        	//存储销售对应的客户，key:销售名称，value:客户名称
        	Map<String, String> travelerMap = new HashMap<String, String>();
        	int firstRow = 0;			//合并单元格的开始行
        	int lastRow = 1;			//合并单元格的结束行
        	int index = 0;				//每个销售拥有的客户数目
        	String lastTravelerNames = "";	//上一个销售所拥有的所有客户名称
        	for (int i = 0; i < travelers.size(); i++) {
        		int dataRowIndex = i + 1;
        		HSSFRow dataRow = sheet.createRow(dataRowIndex);
        		dataRow.setHeightInPoints((short)20);
        		String dateTime = travelers.get(i).getCreateDate();
        		String formatDate = "";
        		if(StringUtils.isNotBlank(dateTime)){
        			Date date = DateUtils.string2Date(dateTime);
            		formatDate = DateUtils.date2String(date, DateUtils.DATE_PATTERN_YYYY_YEAR_MM_MONTH_DD_DAY);
        		}
        		createCell(dataRow, 0, formatDate, dataStyle);
        		createCell(dataRow, 1, travelers.get(i).getGroupCode(),dataStyle);
        		createCell(dataRow, 2, travelers.get(i).getTraveler(),dataStyle);
        		createCell(dataRow, 3, travelers.get(i).getSalerName(),dataStyle);
        		String travelerNames = travelerMap.get(travelers.get(i).getSalerName());
        		if(null == travelerNames){
        			//此时销售还无对应的客户
        			firstRow = dataRowIndex;
        			if(firstRow > lastRow){
        				//开始行大于结束行，说明销售已更换
            			sheet.addMergedRegion(new CellRangeAddress(firstRow - index, lastRow, 4, 4));
            			//上一个销售的客户名称存储到上一个销售合并单元格的开始行处。
            			createCell(sheet.getRow(firstRow - index), 4, lastTravelerNames, dataStyle);
            			lastRow = firstRow;
            		}
        			travelerNames = travelers.get(i).getTraveler();
        			lastTravelerNames = travelerNames;
        			travelerMap.put(travelers.get(i).getSalerName(), lastTravelerNames);
        			index = 1;
        		}else{
        			lastRow = dataRowIndex;
        			lastTravelerNames = travelerNames += "、" + travelers.get(i).getTraveler();
        			travelerMap.put(travelers.get(i).getSalerName(), lastTravelerNames);
        			index++;
        		}
        		createCell(dataRow, 5, travelers.get(i).getPersonNum(),dataStyle);
        		createCell(dataRow, 6, travelers.get(i).getPrice(),moneyStyle);
        		String price = travelers.get(i).getPrice().replaceAll(",", "").replaceAll(Context.CURRENCY_MARK_RMB, "");
        		sumMoney = sumMoney.add(new BigDecimal(price));
        		//当游客条数是最后一条时，
        		if(dataRowIndex == travelers.size()){
        			sheet.addMergedRegion(new CellRangeAddress(dataRowIndex-index+1, dataRowIndex, 4, 4));
        			createCell(sheet.getRow(dataRowIndex-index+1), 4, lastTravelerNames, dataStyle);
        			if(index != 1){
        				//如果销售对应的客户不是一个，则最后一行的边框要设置。如果销售对应的客户是一个，则不需要此设置，否则会覆盖销售对应的客户名称
        				createCell(sheet.getRow(dataRowIndex), 4, "", dataStyle);
        			}
        		}
    		}
        	//添加合计一行
        	HSSFRow dataRow = sheet.createRow(travelers.size()+1);
        	dataRow.setHeightInPoints((short)20);
        	sheet.addMergedRegion(new CellRangeAddress(travelers.size()+1, travelers.size()+1 , 0, 1));
        	createCell(dataRow, 0, "", dataStyle);
        	createCell(dataRow, 1, "", dataStyle);
    		createCell(dataRow, 2, "合计", headStyle);
    		sheet.addMergedRegion(new CellRangeAddress(travelers.size()+1, travelers.size()+1 , 3, 4));
    		createCell(dataRow, 3, "", dataStyle);
    		createCell(dataRow, 4, "", dataStyle);
    		createCell(dataRow, 5, travelers.size(), dataStyle);
    		createCell(dataRow, 6, Context.CURRENCY_MARK_RMB + MoneyNumberFormat.getThousandsByRegex(sumMoney.toString(), 2), moneyStyle);
    	}
    	
    	return workBook;
	}
}
