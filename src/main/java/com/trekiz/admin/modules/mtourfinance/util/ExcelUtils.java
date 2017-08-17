package com.trekiz.admin.modules.mtourfinance.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.modules.mtourfinance.json.OperatingRevenueData;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;


/**
 * Excel
 * */
public class ExcelUtils {

	/**
	 * 单元格合并后，设置4边框样式
	 * @author zhaohaiming
	 * */
	public static void SetMergedCellBorder(int border,CellRangeAddress cellRangeAddress, XSSFSheet sheet,Workbook wb){
		RegionUtil.setBorderLeft(border, cellRangeAddress, sheet, wb);  
        RegionUtil.setBorderBottom(border, cellRangeAddress, sheet, wb);  
        RegionUtil.setBorderRight(border, cellRangeAddress, sheet, wb);  
        RegionUtil.setBorderTop(border, cellRangeAddress, sheet, wb); 
	}
	
	/**
	 * 单元格合并后，设置4边框样式
	 * @author zhaohaiming
	 * */
	public static void SetMergedCellBorder(int borderTop,int borderBottom,int borderLeft,int borderRight,CellRangeAddress cellRangeAddress, XSSFSheet sheet,Workbook wb){
		RegionUtil.setBorderTop(borderTop, cellRangeAddress, sheet, wb); 
	    RegionUtil.setBorderBottom(borderBottom, cellRangeAddress, sheet, wb);  
        RegionUtil.setBorderLeft(borderLeft, cellRangeAddress, sheet, wb);  
        RegionUtil.setBorderRight(borderRight, cellRangeAddress, sheet, wb);  
       
	}
	
	
	public static Map<String, CellStyle> createStyles(Workbook wb) {
		Map<String, CellStyle> styles = new HashMap<String, CellStyle>();  
	    CellStyle style;
	    Font titleFont = wb.createFont();
	    titleFont.setFontHeightInPoints((short)10);
	    titleFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
	    style = wb.createCellStyle();
		style.setFont(titleFont);//单元格字体样式
		style.setBorderBottom((short)1);
		style.setBorderLeft((short)1);
		style.setBorderRight((short)1);
		style.setBorderTop((short)1);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setWrapText(true);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		styles.put("titleStyle", style);
		
		
		Font tipstyle = wb.createFont();
		tipstyle.setFontHeightInPoints((short)10);
		tipstyle.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
	    style = wb.createCellStyle();
		style.setFont(tipstyle);//单元格字体样式
		style.setBorderBottom((short)10);
		style.setBorderLeft((short)1);
		style.setBorderRight((short)1);
		style.setBorderTop((short)1);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setWrapText(true);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		styles.put("tipStyle", style) ;
		
		
		Font contentFont = wb.createFont();
		contentFont.setFontHeightInPoints((short)10);
	    style = wb.createCellStyle();
		style.setFont(contentFont);//单元格字体样式
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setWrapText(true);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		styles.put("contentStyle", style);
		
		Font bankInfoFont = wb.createFont();
		bankInfoFont.setFontHeightInPoints((short)10);
	    style = wb.createCellStyle();
		style.setFont(bankInfoFont);//单元格字体样式
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setWrapText(true);
		styles.put("bankInfoFont", style);

		Font headFont = wb.createFont();
		headFont.setFontHeightInPoints((short)13);
		headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = wb.createCellStyle();
		style.setFont(headFont);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());//LAVENDER
		style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styles.put("headStyle", style);

		style = wb.createCellStyle();
		style.setFont(bankInfoFont);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		style.setWrapText(true);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		DataFormat format = wb.createDataFormat();
		style.setDataFormat(format.getFormat("￥#,##0.00"));
		styles.put("moneyStyle", style);

		style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		Font fontTitle = wb.createFont();
		fontTitle.setFontHeightInPoints((short) 20);
		style.setFont(fontTitle);
		styles.put("titleStyle1", style);

		style = wb.createCellStyle();
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		style.setFont(contentFont);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setWrapText(true);
		styles.put("commonCR", style);//center right

		style = styles.get("headStyle");
		headFont.setFontHeightInPoints((short)10);
		style.setFont(headFont);
		style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		styles.put("headStyle1",style);

	    return styles;
	}
	 /**
     * 创建单元格
     * @param row          row对象
     * @param index        列索引
     * @param value        单元格值
     * @param cellStyle    单元格样式
     * @author shijun.liu
     */
    public static void createCell(Row row, int index, String value, CellStyle cellStyle){
    	Cell cell = row.createCell(index);
    	cell.setCellValue(value);
    	cell.setCellStyle(cellStyle);
    }

	public static void createCell(Row row, int index, Integer value, CellStyle cellStyle){
		Cell cell = row.createCell(index);
		cell.setCellValue(value);
		cell.setCellStyle(cellStyle);
	}

	/**
	 * 美途国际的营业收入统计表，根据销售进行分组显示，需要合并单元格。yudong.xu 2016.6.20
     */
	public static Workbook getOperatingRevenueWB(Map<Integer,List<OperatingRevenueData>> data,String title){
		String[] heads = {"销售","团号","营业收入","营业成本","毛利","应收账款"};
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("营业统计");
		Map<String, CellStyle> styles = createStyles(wb);

		createTitle(title,heads.length,styles,sheet);
		createWBHeader(heads,sheet,styles.get("headStyle"),3);//创建表头
		sheet.createFreezePane(0,4,0,4);//固定表头
		CellStyle contentStyle = styles.get("contentStyle");

		int rowIdx = 4; //记录行号
		for (Map.Entry<Integer, List<OperatingRevenueData>> entry : data.entrySet()) {
			List<OperatingRevenueData> list = entry.getValue();
			for (OperatingRevenueData item : list) {
				Row row = sheet.createRow(rowIdx);
				fillingRow(item,row,styles);//填充数据的子方法
				rowIdx++; //行号加1
			}
			int size = list.size();
			sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rowIdx - size,rowIdx - 1,0,0));
			Cell c0 = sheet.getRow(rowIdx - size).getCell(0);
			c0.setCellStyle(contentStyle);
			c0.setCellValue(list.get(0).getSalerName());
		}
		sheet.autoSizeColumn(0,true);
		sheet.autoSizeColumn(1);
		return wb;
	}

	private static void createTitle(String titleName,int length,Map<String, CellStyle> styles,Sheet sheet){
		Row title = sheet.createRow(0);
		createCell(title,0,titleName,styles.get("titleStyle1"));
		sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0,1,0,length - 1));
		Row unit = sheet.createRow(2);
		createCell(unit,0,"单位：元",styles.get("commonCR"));
		sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(2,2,0,length - 1));
	}

	//使用传入sheet的第一个row创建表头,并固定表头，设置列宽
	public static void createWBHeader(String[] heads,Sheet sheet,CellStyle style,int rowNum){
		Row head = sheet.createRow(rowNum);
		for (int i = 0; i < heads.length; i++) {
			Cell cell = head.createCell(i);
			cell.setCellValue(heads[i]);
			cell.setCellStyle(style);
			sheet.setColumnWidth(i,5800);
		}
	}

	//填充一行的数据及样式，私有方法。
	private static void fillingRow(OperatingRevenueData data,Row row,Map<String, CellStyle> styles){
		CellStyle moneyStyle = styles.get("moneyStyle");
		CellStyle contentStyle = styles.get("contentStyle");

		Cell c0 = row.createCell(0);//销售列，不设置值,在父方法中设置合并单元格的值;这里创建cell主要是为了显示合并后的边框。
		c0.setCellStyle(contentStyle);

		Cell c1 = row.createCell(1);
		c1.setCellStyle(contentStyle);
		c1.setCellValue(data.getGroupCode());

		Cell c2 = row.createCell(2);
		c2.setCellStyle(moneyStyle);
		c2.setCellValue(((BigDecimal)data.getTotalMoney()).doubleValue());

		Cell c3 = row.createCell(3);
		c3.setCellStyle(moneyStyle);
		c3.setCellValue(((BigDecimal)data.getCost()).doubleValue());

		Cell c4 = row.createCell(4);
		c4.setCellStyle(moneyStyle);
		c4.setCellValue(((BigDecimal)data.getProfit()).doubleValue());

		Cell c5 = row.createCell(5);
		c5.setCellStyle(moneyStyle);
		c5.setCellValue(((BigDecimal)data.getReceivable()).doubleValue());
	}
    
}
