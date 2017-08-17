package com.trekiz.admin.common.utils.excel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

/**
 * 通用excel下载工具类
 * @author wangyang
 * @date 2016.7.12
 * */
public class CommonExcelUtils {
	
	/**
	 * 生成excel文件<br>
	 * 不可接收自定义样式，使用默认样式<br>
	 * 默认单元格宽度为20字节<br>
	 * 标题：合并两行上下左右居中，字体大小为20<br>
	 * 表头和内容：上下左右居中<br>
	 * @param caption 标题
	 * @param headers 表头
	 * @param title 工作表名称
	 * @param datalist 文件内容
	 * */
	public static HSSFWorkbook createExcel(String caption, String[] headers, String title, 
			List<List<Object>> datalist) {
				
		return createExcel(caption, headers, title, datalist, null);
		
	}
	
	/**
	 * 返回CommonExcel类，其包括设置样式，添加“合计”行等方法<br>
	 * 若未设置任何样式，采用默认样式,参考{@link #createExcel(String, String[], String, List)}方法
	 * @param caption 标题
	 * @param headers 表头
	 * @param title 工作表名称
	 * @param datalist 文件内容
	 * @param workbook 工作簿对象
	 * */
	public static CommonExcel createExcelWithEnclosure(String caption, String[] headers, String title, 
			List<List<Object>> datalist, HSSFWorkbook workbook) {
		
		HSSFWorkbook workBook =  createExcel(caption, headers, title, datalist, workbook);
		
		return new CommonExcel(workBook);
		
		
	}
	
	/**
	 * 返回excel工作簿对象，以便直接对工作簿对象操作<br>
	 * 不建议直接调用该方法<br>
	 * 可使用{@link #createExcelWithEnclosure(String, String[], String, List, HSSFWorkbook)}方法<br>
	 * 上述方法可直接对返回的封装类进行操作，包括设置样式等。
	 * @param caption 标题
	 * @param headers 表头
	 * @param title 工作表名称
	 * @param datalist 文件内容
	 * @param workbook 文件对象
	 * */
	public static HSSFWorkbook createExcel(String caption, String[] headers, String title, 
			List<List<Object>> datalist, HSSFWorkbook workbook) {
		
		if(workbook == null){
			workbook = new HSSFWorkbook();
		}
		
		HSSFSheet sheet = null;
		
		if(StringUtils.isBlank(title)){
			sheet = workbook.createSheet();
		} else {
			sheet = workbook.createSheet(title);
		}
		sheet.setActive(true);
		
		
		//设置表格默认列宽度为20个字节
		sheet.setDefaultColumnWidth(20);
		//默认单元格样式
		HSSFCellStyle defaultStyle = workbook.createCellStyle();
		defaultStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		defaultStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		setCellBorder(defaultStyle);
		
		//标题样式
		HSSFCellStyle defaultCaptionStyle = workbook.createCellStyle();
		defaultCaptionStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		defaultCaptionStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFFont fontTitle = workbook.createFont();
		fontTitle.setFontHeightInPoints((short) 20);
		defaultCaptionStyle.setFont(fontTitle);
		
		//创建标题行		
		HSSFRow row = sheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellStyle(defaultCaptionStyle);
		cell.setCellValue(caption);
		
		CellRangeAddress region = new CellRangeAddress(0, 1, 0, headers.length - 1);
		sheet.addMergedRegion(region);
		int border = 1;
		setRegionBorderStyle(workbook, sheet, region, border, HSSFColor.BLACK.index);
		
		//表头行
		row = sheet.createRow(2);
		for(int i = 0; i < headers.length; i++){
			cell = row.createCell(i);
			cell.setCellStyle(defaultStyle);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		
		//内容
		int rowIndex = 3;
		for(List<Object> data : datalist){
			row = sheet.createRow(rowIndex);
			for(int i = 0; i < data.size(); i++){
				cell = row.createCell(i);
				cell.setCellValue(data.get(i)+"");
				cell.setCellStyle(defaultStyle);
			}
			rowIndex++;
		}
		
		return workbook;
	}

	/**
	 * 设置合并单元格边框大小及颜色
	 * @param color 单元格边框颜色index值
	 * @param border 边框宽度
	 * @param region 单元格
	 * @param workbook excel工作簿对象
	 * @param sheet 工作表对象
	 * */
	private static void setRegionBorderStyle(HSSFWorkbook workbook,
			HSSFSheet sheet, CellRangeAddress region, int border, int color) {
		RegionUtil.setBorderBottom(border, region, sheet, workbook);
		RegionUtil.setBorderLeft(border, region, sheet, workbook);
		RegionUtil.setBorderRight(border, region, sheet, workbook);
		RegionUtil.setBorderTop(border, region, sheet, workbook);
		
		RegionUtil.setBottomBorderColor(color, region, sheet, workbook);
		RegionUtil.setLeftBorderColor(color, region, sheet, workbook);
		RegionUtil.setRightBorderColor(color, region, sheet, workbook);
		RegionUtil.setTopBorderColor(color, region, sheet, workbook);
	}
	
	private static void setCellBorder(HSSFCellStyle defaultStyle) {
		defaultStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		defaultStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		defaultStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		defaultStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		
		defaultStyle.setTopBorderColor(HSSFColor.BLACK.index);
		defaultStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		defaultStyle.setLeftBorderColor(HSSFColor.BLACK.index);
		defaultStyle.setRightBorderColor(HSSFColor.BLACK.index);
	}
	
	/**
	 * 根据当前用户的浏览器不同，对文件的名字进行不同的编码设置，从而解决不同浏览器下文件名中文乱码问题
	 * */
	public static HttpServletResponse setFileDownloadHeader(HttpServletRequest request,HttpServletResponse response, String fileName) {
		
		String encodedfileName;
		response.setContentType("octets/stream");
		try {
			//中文文件名支持  
			if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) {//IE浏览器 
				encodedfileName = URLEncoder.encode(fileName, "UTF-8");
			} else if(request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0 
					|| request.getHeader("User-Agent").toLowerCase().indexOf("opera") > 0) {//google,火狐浏览器  
				encodedfileName = new String(fileName.getBytes(), "ISO8859-1");  
			} else {//其他浏览器 
				encodedfileName = URLEncoder.encode(fileName, "UTF-8"); 
			}
			response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");//这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
		} catch (UnsupportedEncodingException e) { 
			e.printStackTrace();
		}
		return response;
	}
	
	public static void main(String[] args){
		
		Object obj = 12 + "";
		
		System.out.println(obj instanceof Number);
	}
}
