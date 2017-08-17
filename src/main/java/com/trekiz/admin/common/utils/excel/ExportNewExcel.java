package com.trekiz.admin.common.utils.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExportNewExcel {
	/**
	 * 創建Xssfworkbook,并創建sheet表
	 */
	public static XSSFSheet creatXSSFWorkbook() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("sheet1");
		sheet.setDefaultColumnWidth(12);
		sheet.setDefaultColumnWidth(20);
		sheet.setDefaultRowHeight((short) 600);
		return sheet;
	}
	/**
	 * 定制单元格边框
	 * 
	 */
	@SuppressWarnings("unused")
	private static void getNewCenterStyle(XSSFCellStyle style) {

		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);

		/* return style; */
	}
	public static void exportExcel(String fileName, XSSFWorkbook workbook,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// 导出excel文档
		OutputStream op = null;
		fileName = fileName + ".xlsx";
		response.reset();
		response.setContentType("application/vnd.ms-excel");
		setFileDownloadHeader(request, response, fileName);

		op = response.getOutputStream();
		workbook.write(op);
		op.flush();
		op.close();
	}
	
	/**
	 * 522需求--
	 * 
	 * @param fileName
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public static void exportOfficeActivityExcel(String fileName,List<Object[]> resultList,List<String> agnetName,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		XSSFWorkbook workBook = new XSSFWorkbook();
		int rowNum = 0;
		// 创建一个sheet表
		XSSFSheet sheet = workBook.createSheet("sheet1");
		sheet.setDefaultColumnWidth(12);
		sheet.setDefaultColumnWidth(20);
		sheet.setDefaultRowHeight((short) 600);

		String[] firstRowTitle = { "批发商名称", "产品名称", "团号", "出团日期", "出发城市",
				"同行价", " ", " ", "定价策略", " ", " " };
		String[] secondRowTitle = { "成人价", "儿童价", "特殊人群价" };

		// 创建行
		XSSFRow titleRow = sheet.createRow((short) rowNum++);

		// 字体设置
		XSSFFont font = workBook.createFont();
		font.setFontName("宋体");
		font.setColor(XSSFFont.COLOR_NORMAL);

		// 样式
		XSSFCellStyle cellStyle = workBook.createCellStyle();
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setFont(font);
		cellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT
				.getIndex()); // 背景色-灰色
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		getNewCenterStyle(cellStyle);
		// 设置第一行表头
		for (int i = 0; i < firstRowTitle.length; i++) {
			XSSFCell rowFirstCell = titleRow.createCell(i);
			rowFirstCell.setCellStyle(cellStyle);
			rowFirstCell.setCellValue(firstRowTitle[i]);
		}
		XSSFRow secondRow = sheet.createRow(rowNum++);
		// 设置第二行的价格
		for (int i = 0; i < firstRowTitle.length; i++) {
			XSSFCell rowFirstCell = secondRow.createCell(i);
			rowFirstCell.setCellStyle(cellStyle);
		}
		for (int a = 0; a < secondRowTitle.length; a++) {
			secondRow.getCell(5 + a).setCellValue(secondRowTitle[a]);
		}
		for (int a = 0; a < secondRowTitle.length; a++) {
			secondRow.getCell(8 + a).setCellValue(secondRowTitle[a]);
		}
		//设置第一行 渠道名称
		XSSFCellStyle titleCellStyle = workBook.createCellStyle();
		titleCellStyle.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
		titleCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		titleCellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		titleCellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		getNewCenterStyle(titleCellStyle);
		//填充渠道名称
		for(int i = 0; i < agnetName.size() ;i++){
			//渠道名称行
			short firstCellNum = titleRow.getLastCellNum();
			XSSFCell firstTitleCell = titleRow.createCell(firstCellNum);
			firstTitleCell.setCellValue(agnetName.get(i));
			firstTitleCell.setCellStyle(titleCellStyle);
			XSSFCell firstTitleCell1 = titleRow.createCell(titleRow.getLastCellNum());
			firstTitleCell1.setCellValue(agnetName.get( i ));
			firstTitleCell1.setCellStyle(titleCellStyle);
			short lastCellNum = titleRow.getLastCellNum();
			XSSFCell firstTitleCell2 = titleRow.createCell(titleRow.getLastCellNum());
			firstTitleCell2.setCellValue(agnetName.get( i ));
			firstTitleCell2.setCellStyle(titleCellStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 0, firstCellNum, lastCellNum));
		}
		int size = agnetName.size();
		//創建渠道价格標題行并賦值
		for(int j = 0; j < size ;j++){
			short lastCellNum = secondRow.getLastCellNum();
			XSSFCell secondTitleCell = secondRow.createCell(lastCellNum);
			secondTitleCell.setCellStyle(titleCellStyle);
			secondTitleCell.setCellValue("成人价");
			short lastCellNum1 = secondRow.getLastCellNum();
			XSSFCell secondTitleCell1 = secondRow.createCell(lastCellNum1);
			secondTitleCell1.setCellStyle(titleCellStyle);
			secondTitleCell1.setCellValue("儿童价");
			short lastCellNum2 = secondRow.getLastCellNum();
			XSSFCell secondTitleCell2 = secondRow.createCell(lastCellNum2);
			secondTitleCell2.setCellStyle(titleCellStyle);
			secondTitleCell2.setCellValue("特殊人群价");
		}
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 5, 7));
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 8, 10));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 2, 2));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 3));
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 4));
		
		//System.out.println("开始导出数据"+new Date());
		//填充数据
		for(Object[] o : resultList){
			int length = o.length-1;
			XSSFRow valueRow = sheet.createRow(rowNum++);
			for(int a = 0; a < firstRowTitle.length; a++){
				XSSFCell valueCell = valueRow.createCell(a);
				valueCell.setCellValue(o[a] == null ? "" : o[a].toString());
			}
			//System.out.println("开始导出数据"+new Date());
			Object price = o[length];
			String[] split = price.toString().split(" ");
			
			for(int i = 0; i < split.length;i++){
				short lastCellNum = valueRow.getLastCellNum();
				//System.out.println("第"+lastCellNum+"单元格");
				XSSFCell valueCell = valueRow.createCell(lastCellNum);	
				valueCell.setCellValue(split[i] == null ? "" : split[i].toString());
			}
		}
		//System.out.println("开始导出数据2"+new Date());
		// 导出excel文档
		exportExcel(fileName, workBook, request, response);
		//System.out.println("结束"+new Date());
	}

	// 根据当前用户的浏览器不同，对文件的名字进行不同的编码设置，从而解决不同浏览器下文件名中文乱码问题
	public static void setFileDownloadHeader(HttpServletRequest request,
			HttpServletResponse response, String fileName) {
		String encodedfileName;
		try {
			// 中文文件名支持
			if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {// IE浏览器
				encodedfileName = URLEncoder.encode(fileName, "UTF-8");
			} else if (request.getHeader("User-Agent").toLowerCase()
					.indexOf("firefox") > 0
					|| request.getHeader("User-Agent").toLowerCase()
							.indexOf("opera") > 0) {// google,火狐浏览器
				encodedfileName = new String(fileName.getBytes(), "ISO8859-1");
			} else {
				encodedfileName = URLEncoder.encode(fileName, "UTF-8");// 其他浏览器
			}

			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ encodedfileName + "\"");// 这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
		} catch (UnsupportedEncodingException e) {
		}
	}
}
