package com.trekiz.admin.agentToOffice.T2.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExportExcelForAgentUtils {
	
	/**
	 * 创建sheet表并设置默认的行高和列宽
	 * @param workbook
	 */
	public static HSSFSheet creatSheet(HSSFWorkbook workbook){
		
		HSSFSheet sheet = workbook.createSheet("sheet1");
		sheet.setDefaultColumnWidth(20);
		sheet.setDefaultRowHeight((short) 450);
		return sheet;
	}
	/**
	 * 设置字体
	 * @param fontHeight 字体宽度 	HSSFFont.BOLDWEIGHT_NORMAL
	 * 		  fontcolor	字体颜色	HSSFFont.COLOR_NORMAL
	 */
	public static HSSFFont setFont(HSSFWorkbook workbook,short fontHeight,short fontcolor){
		HSSFFont font = workbook.createFont();
		font.setFontName("宋体");
		font.setFontHeight(fontHeight);
		font.setColor(fontcolor);
		return font;
	}
	/**
	 * 绘制边框 输入单元格坐标和要设置的边框位置(适合定制特殊单元格边框)
	 * 
	 * @param sheet
	 * @param rowNum
	 * @param cellNum
	 * @param cellStyle
	 * @param border
	 */
	public static void setBorder(HSSFWorkbook workBook, HSSFSheet sheet,
			int rowNum, int cellNum, String border, short borderLineStyle) {
		// 创建字体样式
		HSSFFont font = workBook.createFont();
		font.setFontName("宋体");
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFFont.COLOR_NORMAL);
		// 创建单元格样式
		HSSFCellStyle cellStyle = workBook.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setFont(font);
		if (border == "bottom") {
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_HAIR); // 下边框
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_HAIR);// 左边框
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_HAIR);// 上边框
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_HAIR);// 右边框
		} else if (border == "right") {
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_HAIR); // 下边框
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_HAIR);// 左边框
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_HAIR);// 上边框
		}

		HSSFRow row = sheet.getRow(rowNum);
		HSSFCell cell = row.getCell(cellNum);

		if (border == "top") {
			cellStyle.setBorderTop(borderLineStyle);// 上边框
			cell.setCellStyle(cellStyle);
		} else if (border == "bottom") {
			cellStyle.setBorderBottom(borderLineStyle); // 下边框
			cell.setCellStyle(cellStyle);
		} else if (border == "left") {
			cellStyle.setBorderLeft(borderLineStyle);// 左边框
			cell.setCellStyle(cellStyle);
		} else if (border == "right") {
			cellStyle.setBorderRight(borderLineStyle);// 右边框
			cell.setCellStyle(cellStyle);
		}

	}
	/**
	 * 定制单元格边框，四个边框都要
	 * @param cellstyle
	 */
	private static void setBorder(HSSFCellStyle cellstyle,short border){
		if(border == 0){
			cellstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellstyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellstyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellstyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		}else{
			cellstyle.setBorderBottom(border);
			cellstyle.setBorderLeft(border);
			cellstyle.setBorderRight(border);
			cellstyle.setBorderTop(border);
		}
		
	}

	/**
	 * 设置单元格的对齐方式
	 * @param alignment
	 * @param VerticalAlign
	 * @return
	 */
	public static HSSFCellStyle setCellStyle(HSSFWorkbook workbook,short alignment,short VerticalAlign){
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(alignment);
		cellStyle.setVerticalAlignment(VerticalAlign);
		return cellStyle;
	}
	/**
	 * 导出Excel
	 * @param fileName
	 * @param request
	 * @param response
	 */
	public static void exportExcel(HSSFWorkbook workbook,String fileName,HttpServletRequest request, HttpServletResponse response){
		//导出excel文档
		try {
			OutputStream op = null;
			fileName = fileName + ".xls";
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			setFileDownloadHeader(request, response, fileName);
			op = response.getOutputStream();
			workbook.write(op);
			op.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	/**
	 * 制作sheet表
	 * @param request
	 * @param response
	 * @param fileName
	 */
	public static void createExcel(String fileName, List<Object[]> list,
			String[] cellTitle, HttpServletRequest request, HttpServletResponse response) throws Exception{
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = creatSheet(workbook);
		int rowNum = 0;
		HSSFRow titleRow = sheet.createRow(rowNum++);
		//设置表头 的对齐方式和字体样式并赋值
		HSSFCellStyle rowStyle = setCellStyle(workbook,HSSFCellStyle.ALIGN_CENTER,HSSFCellStyle.VERTICAL_CENTER);
		setBorder(rowStyle, (short) 0);
		HSSFFont titleFont = workbook.createFont();
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		rowStyle.setFont(titleFont);
		//titleRow.setHeight((short)600);
		for(int i = 0;i < cellTitle.length; i++){
			HSSFCell createCell = titleRow.createCell(i);
			createCell.setCellStyle(rowStyle);
		}
		/*rowStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); // 背景色-灰色
		rowStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);*/
		HSSFCell firstCell = titleRow.getCell(0);
		firstCell.setCellValue(fileName);
		firstCell.setCellStyle(rowStyle);
		//合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,10));
		
		//设置第二行表头
		HSSFCellStyle secondCellStyle = setCellStyle(workbook,HSSFCellStyle.ALIGN_CENTER,HSSFCellStyle.VERTICAL_CENTER);
		secondCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		secondCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		HSSFRow secondTitleRow = sheet.createRow(rowNum++);
		for(int i = 0;i < cellTitle.length; i++){
			HSSFCell titleCell = secondTitleRow.createCell(i);
			titleCell.setCellValue(cellTitle[i]);
			titleCell.setCellStyle(secondCellStyle);
			setBorder(secondCellStyle, (short) 0);
		}
		//输出数据
		HSSFCellStyle valueCellStyle = setCellStyle(workbook,HSSFCellStyle.ALIGN_CENTER,HSSFCellStyle.VERTICAL_CENTER);
		for(Object[] o:list){
			HSSFRow valueRow = sheet.createRow(rowNum++);
			for(int j = 0; j < o.length; j++){
				HSSFCell cell = valueRow.createCell(j);
				cell.setCellValue(o[j].toString());
				cell.setCellStyle(valueCellStyle);
				setBorder(valueCellStyle, (short) 0);
			}
		}
		setFileDownloadHeader(request,response,fileName);
		exportExcel(workbook,fileName,request,response);
		
	}
	
	//根据当前用户的浏览器不同，对文件的名字进行不同的编码设置，从而解决不同浏览器下文件名中文乱码问题
    public static void setFileDownloadHeader(HttpServletRequest request,HttpServletResponse response, String fileName) {  
        String encodedfileName;  
        try {  
            //中文文件名支持  
            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {//IE浏览器  
                encodedfileName = URLEncoder.encode(fileName, "UTF-8");  
            } else if(request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0 || request.getHeader("User-Agent").toLowerCase().indexOf("opera") > 0) {//google,火狐浏览器  
                encodedfileName = new String(fileName.getBytes(), "ISO8859-1");  
            } else {  
                encodedfileName = URLEncoder.encode(fileName, "UTF-8");//其他浏览器  
            }  
              
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");//这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
        } catch (UnsupportedEncodingException e) {  
        }  
    }
	
	
}
