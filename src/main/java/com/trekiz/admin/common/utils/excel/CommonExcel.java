package com.trekiz.admin.common.utils.excel;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringUtils;


/**
 * excel公用类
 * @author wangyang
 * @date 2016.7.14
 * */
public class CommonExcel {

	private HSSFWorkbook workbook;
	
	private HSSFSheet sheet;
	/** 表格总列数 */
	private int columnNum;
	/** 表格总行数 */
	private int rowNum;
	
	public CommonExcel(){
	}
			
	public CommonExcel(HSSFWorkbook workbook) {
		super();
		this.workbook = workbook;
		this.sheet = workbook.getSheetAt(0);
		this.rowNum = sheet.getLastRowNum();
		this.columnNum = sheet.getRow(2).getPhysicalNumberOfCells();
	}

	/**
	 * excel下载方法
	 * @param os 输出流
	 * */
	public void write(OutputStream os) throws IOException {
		workbook.write(os);
	}
	
	/**
	 * 获取修改后的HSSFWorkbook对象
	 * */
	public HSSFWorkbook getWorkbook() {
		return workbook;
	}
	
	/**
	 * 设置列宽
	 * @param columnIndex 列号（0开始）
	 * @param width 宽度（字节）
	 * */
	public void setColumnWith(int columnIndex, int width) {
		sheet.setColumnWidth(columnIndex, width*256);
	}
	
	/**
	 * 设置标题样式
	 * */
	public void setCaptionStyle(HSSFCellStyle style) {
		
		HSSFRow row = sheet.getRow(0);
		HSSFCell cell = row.getCell(0);
		
		if(style != null) {
			cell.setCellStyle(style);
		}
	}
	
	/**
	 * 设置表头样式
	 * */
	public void setHeaderStyle(HSSFCellStyle style) {
		
		HSSFRow row = sheet.getRow(2);
		HSSFCell cell = null;
		
		if (style != null) {
			setCellBorder(style);
			for(int i = 0; i < columnNum; i++) {
				cell = row.getCell(i);
				cell.setCellStyle(style);
			}
		}
	}
	
	/**
	 * 设置内容综合样式
	 * */
	public void setContentStyle(HSSFCellStyle style) {
		
		if(style == null){
			return;
		}
		
		int rowIndex = 3;
		
		for(int i = rowIndex; i <= rowNum; i++) {
			HSSFRow row = sheet.getRow(i);
			HSSFCell cell = null;
			for(int j = 0; j < columnNum; j++) {
				cell = row.getCell(j);
				cell.setCellStyle(style);
			}
		}
	}
	
	/**
	 * 设置内容中某一列的样式
	 * @param columnIndex 列号（以0开始）
	 * */
	public void setContentStyleForColumn(int columnIndex, HSSFCellStyle style) {
		
		if(style == null) {
			return;
		}
		
		int rowIndex = 3;
		for(int i = rowIndex; i <= rowNum; i++) {
			HSSFRow row = sheet.getRow(i);
			HSSFCell cell = row.getCell(columnIndex);
			cell.setCellStyle(style);
		}
	}
	
	/**
	 * 在表格末行后加入合计行
	 * @param mergedNum 合并的单元格个数
	 * @param mergedContent 合并单元格后的内容
	 * @param columns 要求和的列号列表（以0开始）
	 * */
	public void addSumRow(int mergedNum, String mergedContent, Integer[] columns) {
		
		CellRangeAddress region = new CellRangeAddress(rowNum + 1, rowNum + 1, 0, mergedNum - 1);
		sheet.addMergedRegion(region);
		
		HSSFRow row = sheet.createRow(rowNum + 1);
		HSSFCell cell = row.createCell(0);
		
		HSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		setCellBorder(style);
		cell.setCellStyle(style);
		cell.setCellValue(mergedContent);
		
		int border = 1;
		setRegionBorderStyle(region, border, HSSFColor.BLACK.index);//设置合并单元格边框
		
		for (int k = mergedNum; k < columnNum; k++) {
			cell = row.createCell(k);
			cell.setCellStyle(style);
			
			for (int column : columns) {
				if (k == column) {
					int sum = 0;
					BigDecimal big = new BigDecimal("0.00");
					HSSFRow rowIt = sheet.getRow(3);
					HSSFCell cellIt = rowIt.getCell(column);
					
					String flag = "";//用于判断普通数字或金额
					for (int i = 3; i <= rowNum; i++) {
						rowIt = sheet.getRow(i);
						cellIt = rowIt.getCell(column);
						
						if (isNumeric(cellIt.getStringCellValue())
								&& StringUtils.isNotEmpty(cellIt.getStringCellValue())) {
							sum += Integer.parseInt(cellIt.getStringCellValue());
							flag = "number";
							
						} else if (StringUtils.isNotEmpty(cellIt.getStringCellValue()) 
								&& "￥".equals(cellIt.getStringCellValue().charAt(0) + "")) {
							big = big.add(new BigDecimal(toNumeric(cellIt.getStringCellValue())));
							flag = "money";
						}
					}
					
					if ("money".equals(flag)) {//金额格式
						cell.setCellValue("￥" + MoneyNumberFormat.getThousandsByRegex(big.toString(), 2));
					} else if ("number".equals(flag)) {//纯数字格式
						cell.setCellValue(sum + "");
					} else {//空
						cell.setCellValue("");
					}
					
				}
			}
		}
		
	}

	/**
	 * 设置合并单元格边框大小及颜色
	 * @param color 单元格边框颜色index值
	 * @param border 边框宽度
	 * @param region 单元格
	 * */
	private void setRegionBorderStyle(CellRangeAddress region, int border, int color) {
		RegionUtil.setBorderBottom(border, region, sheet, workbook);
		RegionUtil.setBorderLeft(border, region, sheet, workbook);
		RegionUtil.setBorderRight(border, region, sheet, workbook);
		RegionUtil.setBorderTop(border, region, sheet, workbook);
		
		RegionUtil.setBottomBorderColor(color, region, sheet, workbook);
		RegionUtil.setLeftBorderColor(color, region, sheet, workbook);
		RegionUtil.setRightBorderColor(color, region, sheet, workbook);
		RegionUtil.setTopBorderColor(color, region, sheet, workbook);
	}

	/**
	 * 设置单元格边框大小及颜色
	 * */
	private void setCellBorder(HSSFCellStyle style) {
		style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		style.setBorderRight(HSSFCellStyle.BORDER_THIN);
		style.setBorderTop(HSSFCellStyle.BORDER_THIN);
		
		style.setTopBorderColor(HSSFColor.BLACK.index);
		style.setBottomBorderColor(HSSFColor.BLACK.index);
		style.setLeftBorderColor(HSSFColor.BLACK.index);
		style.setRightBorderColor(HSSFColor.BLACK.index);
	}

	/**
	 * 将带有'￥'的千分位金额字符串转成双精度数值  "￥900,000.0"--->900000.0
	 * */
	private Double toNumeric(String value) {
		if(value.equals("")){
			value="0";
		}else{
			value = value.substring(1, value.length()).replace(",", "");
		}
		
		return Double.valueOf(value);
	}

	/**
	 * 判断字符串是否为数字
	 * */
	private boolean isNumeric(String str){  
	    Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");  
	    return pattern.matcher(str).matches();     
	} 
	
	/**
	 * 将数字转换为对应的大写英文字母<br>
	 * 0转换为A，1转换为B...以此类推
	 * */
	private String turnToE(int column) {

		char e = (char) (column + 65);
		return String.valueOf(e);
	}
	
	public static void main(String[] args) {
		
		String str = "5954100.0";
		System.out.println("￥" + MoneyNumberFormat.getThousandsByRegex(str, 2));
		//double a = CommonExcel.toNumeric(str);
		//System.out.println(a);
		//System.out.println(CommonExcel.isNumeric("416869.84"));
	}
	
}
