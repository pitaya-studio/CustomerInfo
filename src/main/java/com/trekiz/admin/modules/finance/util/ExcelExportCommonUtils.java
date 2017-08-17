package com.trekiz.admin.modules.finance.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.FileUtils;
import com.trekiz.admin.common.utils.ZipUtils;

public class ExcelExportCommonUtils {

	/**
	 * 创建sheet表并设置默认的行高和列宽
	 * @param workbook
	 */
	public static HSSFSheet creatSheet(HSSFWorkbook workbook) {
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
	public static HSSFFont setFont(HSSFWorkbook workbook,short fontHeight,short fontcolor) {
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
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_HAIR); // 左边框
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_HAIR); // 上边框
			cellStyle.setBorderRight(HSSFCellStyle.BORDER_HAIR); // 右边框
		} else if (border == "right") {
			cellStyle.setBorderBottom(HSSFCellStyle.BORDER_HAIR); // 下边框
			cellStyle.setBorderLeft(HSSFCellStyle.BORDER_HAIR); // 左边框
			cellStyle.setBorderTop(HSSFCellStyle.BORDER_HAIR); // 上边框
		}
		HSSFRow row = sheet.getRow(rowNum);
		HSSFCell cell = row.getCell(cellNum);
		if (border == "top") {
			cellStyle.setBorderTop(borderLineStyle); // 上边框
			cell.setCellStyle(cellStyle);
		} else if (border == "bottom") {
			cellStyle.setBorderBottom(borderLineStyle); // 下边框
			cell.setCellStyle(cellStyle);
		} else if (border == "left") {
			cellStyle.setBorderLeft(borderLineStyle); // 左边框
			cell.setCellStyle(cellStyle);
		} else if (border == "right") {
			cellStyle.setBorderRight(borderLineStyle); // 右边框
			cell.setCellStyle(cellStyle);
		}

	}

	/**
	 * 定制单元格边框，四个边框都要
	 * @param cellstyle
	 */
	private static void setBorder(HSSFCellStyle cellstyle,short border) {
		if (border == 0) {
			cellstyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			cellstyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			cellstyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
			cellstyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		} else {
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
	public static HSSFCellStyle setCellStyle(HSSFWorkbook workbook,short alignment,short VerticalAlign) {
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(alignment);
		cellStyle.setVerticalAlignment(VerticalAlign);
		return cellStyle;
	}

	/**
	 * 制作sheet表
	 * @param request
	 * @param response
	 * @param fileName
	 */
	public static HSSFWorkbook createExcel(String fileName, List<Object[]> list, String[] cellTitle) throws Exception {
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = creatSheet(workbook);
		int rowNum = 0;
		HSSFRow titleRow = sheet.createRow(rowNum++);
		// 设置表头 的对齐方式和字体样式并赋值
		HSSFCellStyle rowStyle = setCellStyle(workbook,HSSFCellStyle.ALIGN_CENTER,HSSFCellStyle.VERTICAL_CENTER);
		setBorder(rowStyle, (short) 0);
		HSSFFont titleFont = workbook.createFont();
		titleFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		rowStyle.setFont(titleFont);
		// titleRow.setHeight((short)600);
		for (int i = 0;i < cellTitle.length; i++) {
			HSSFCell createCell = titleRow.createCell(i);
			createCell.setCellStyle(rowStyle);
		}
		/*rowStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); // 背景色-灰色
		rowStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);*/
		HSSFCell firstCell = titleRow.getCell(0);
		firstCell.setCellValue(fileName);
		firstCell.setCellStyle(rowStyle);
		// 合并单元格
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, cellTitle.length - 1));
		// 设置第二行表头
		HSSFCellStyle secondCellStyle = setCellStyle(workbook,HSSFCellStyle.ALIGN_CENTER,HSSFCellStyle.VERTICAL_CENTER);
		secondCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		secondCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		HSSFRow secondTitleRow = sheet.createRow(rowNum++);
		for (int i = 0;i < cellTitle.length; i++) {
			HSSFCell titleCell = secondTitleRow.createCell(i);
			titleCell.setCellValue(cellTitle[i]);
			titleCell.setCellStyle(secondCellStyle);
			setBorder(secondCellStyle, (short) 0);
		}
		// 输出数据
		HSSFCellStyle valueCellStyle = setCellStyle(workbook,HSSFCellStyle.ALIGN_CENTER,HSSFCellStyle.VERTICAL_CENTER);
		for (Object[] o:list) {
			HSSFRow valueRow = sheet.createRow(rowNum++);
			for (int j = 0; j < o.length; j++) {
				HSSFCell cell = valueRow.createCell(j);
				cell.setCellValue(o[j].toString());
				cell.setCellStyle(valueCellStyle);
				setBorder(valueCellStyle, (short) 0);
			}
		}
		return workbook;
	}

	// 根据当前用户的浏览器不同，对文件的名字进行不同的编码设置，从而解决不同浏览器下文件名中文乱码问题
    public static void setFileDownloadHeader(HttpServletRequest request,HttpServletResponse response, String fileName) {  
        String encodedfileName;  
        try {
            // 中文文件名支持  
            if (request.getHeader("User-Agent").toUpperCase().indexOf("MSIE") > 0) { // IE浏览器  
                encodedfileName = URLEncoder.encode(fileName, "UTF-8");  
            } else if (request.getHeader("User-Agent").toLowerCase().indexOf("firefox") > 0 
            		|| request.getHeader("User-Agent").toLowerCase().indexOf("opera") > 0) { // google,火狐浏览器  
                encodedfileName = new String(fileName.getBytes(), "ISO8859-1");  
            } else {  
                encodedfileName = URLEncoder.encode(fileName, "UTF-8"); // 其他浏览器  
            }
            // 这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
            
        } catch (UnsupportedEncodingException e) {
			throw new RuntimeException("系统不支持该文件名称的编码");
		}
    }

    /**
	 * 导出Excel
	 * @param fileName
	 * @param request
	 * @param response
	 */
	public static void exportExcel(HSSFWorkbook workbook,String fileName
			,HttpServletRequest request, HttpServletResponse response) {
		// 导出excel文档
		try {
			OutputStream op = null;
			response.reset();
			response.setContentType("application/vnd.ms-excel");
			setFileDownloadHeader(request, response, fileName);
			op = response.getOutputStream();
			workbook.write(op);
			op.flush();
			op.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 导出excel或zip压缩文件
	 * 将字段获取形式改成了for循环获取的形式,字段值如果为NULL, 则设置为" "
	 * 但是需要注意的是本方法没有做字段默认值处理(例如：金额类型的字段需要自行转换成金额格式)
	 * @author gaoyang
	 * @Time 2017-3-21 下午5:46:08
	 * @param fileName
	 * @param zipChineseName
	 * @param secondTitle
	 * @param commonName
	 * @param commonList
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public static void downLoadExcelFile(String fileNameParam, String zipChineseName
			,String[] secondTitle, String[] commonName, List<Map<Object, Object>> commonList
			,HttpServletRequest request, HttpServletResponse response) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		// Excel文件名称 名称+当前日期  例如：(成本付款20170101.xls)
		String fileName = fileNameParam + sdf.format(new Date());
				
		// 设置每个excel的数据条数（不含两行表头）
//		int length = Integer.valueOf(Global.getOneExcelNum());
		int length = Integer.valueOf("65000");
        // 盛装文件名陈
        Map<String, String> fileNameMap = new HashMap<String, String>();
        // 封装查询结果
		List<Object[]> resultList = new ArrayList<Object[]>();
		HSSFWorkbook workBook = null;
		// 要下载的文件条数
		int commonSize = 0;
		if (null != commonList && !commonList.isEmpty()){
			commonSize = commonList.size();
		}
		// 要下载的文件条数大于length，先打包再下载
		if (commonSize > length){
	        // 盛装多少File，生成多少excel
	        List<File> fileList = new ArrayList<File>();
	        File zipFile = null;
	        // 获取一个临时目录（注意用过之后，要把此文件的临时文件删除掉。）
	        String prefix = FileUtils.getTempDir();
	        // 第一层循环：获取excel个数
	        // 第二层循环：获取每个excel放多少数据
	        for (int j = 0, n = (commonSize % length == 0) ? (commonSize / length) : (commonSize / length + 1) ; j < n; j++) {
	        	int jlength = j * length;
	        	for (int i = 1, min = (commonSize - jlength) > length ? (length + 1) : (commonSize - jlength + 1); i < min; i++) {
					Map<Object, Object> map = commonList.get(jlength + i - 1);
					Object[] result = new Object[commonName.length];
					for (int t = 0; t < commonName.length; t ++) {
						result[t] = map.get(commonName[t]) == null ? " " : map.get(commonName[t]);
					}
					resultList.add(result);
	        	}
	        	// 制作sheet表
	        	workBook = createExcel(fileNameParam, resultList, secondTitle);
	        	// 当导出数据超出65533行时，分成两个excel文件导出(两个表头占两行)；
	        	// 前65533条数据导出的excel命名为“起始日期（格式为YYYY-MM-DD）-结束日期（格式为YYYY-MM-DD）渠道统计-1.xlsx”，
	        	// 之后的数据导出的excel命名为“起始日期（格式为YYYY-MM-DD）-结束日期（格式为YYYY-MM-DD）渠道统计-2.xlsx
	        	// 根据数据多少，更换文件名称
	        	fileNameMap.put("fileName", fileName + "-" + (j + 1) + ".xls");
	        	// 得到excel文件在服务器上的完整路径
	        	File excelFile = new File(prefix, fileNameMap.get("fileName"));
	        	OutputStream out = null;
				try {
					out = new BufferedOutputStream(new FileOutputStream(excelFile));
					workBook.write(out);
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					FileUtils.closeStream(null, out);
					if (null != resultList) {
		        		resultList.clear();
		        	}
				}
				fileList.add(excelFile);
	        }
	        // 为zip命名
	        String nowDate = DateUtils.formatCustomDate(new Date(), "yyyyMMddHHmmss");
			String zipName = zipChineseName + nowDate + ".zip";
			// 得到zip文件在服务器上的完整路径
			zipFile = new File(prefix, zipName);
			File deleteZipFile = zipFile;
			// 将文件压缩为zip文件
			ZipUtils.zipFileList(fileList, zipFile);
			// 单独启动一个线程经过delay秒之后删除文件
			FileUtils.timingDeleteFile(60*10, deleteZipFile);
			FileUtils.timingDeleteFile(60*10, fileList);
			// 下载文件
			ServletUtil.downLoadFile(response, zipFile);
		} else {
			// 要下载的文件条数不大于length，直接下载
    		fileNameMap.put("fileName", fileName + ".xls");
    		String fileNameAll = fileNameMap.get("fileName");
    		for (Map<Object, Object> map : commonList) {
				Object[] result = new Object[commonName.length];
				for (int t = 0; t < commonName.length; t ++) {
					result[t] = map.get(commonName[t]) == null ? " " : map.get(commonName[t]);
				}
				resultList.add(result);
			}
			// 制作sheet表与下载
        	workBook = createExcel(fileNameParam, resultList, secondTitle);
        	exportExcel(workBook,fileNameAll,request,response);
        	if (null != resultList) {
        		resultList.clear();
        	}
    	}
	}

	/**
	 * 导出excel或zip压缩文件
	 * 将字段获取形式改成了for循环获取的形式,字段值如果为NULL, 则设置为" "
	 * 但是需要注意的是本方法没有做字段默认值处理(例如：金额类型的字段需要自行转换成金额格式)
	 * @author gaoyang
	 * @Time 2017-3-21 下午5:46:08
	 * @param fileName
	 * @param zipChineseName
	 * @param secondTitle
	 * @param commonName
	 * @param commonList
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public static void downLoadExcelFileStr(String fileNameParam, String zipChineseName
			,String[] secondTitle, String[] commonName, List<Map<String, Object>> commonList
			,HttpServletRequest request, HttpServletResponse response) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		// Excel文件名称 名称+当前日期  例如：(成本付款20170101.xls)
		String fileName = fileNameParam + sdf.format(new Date());
				
		// 设置每个excel的数据条数（不含两行表头）
//		int length = Integer.valueOf(Global.getOneExcelNum());
		int length = Integer.valueOf("65000");
        // 盛装文件名陈
        Map<String, String> fileNameMap = new HashMap<String, String>();
        // 封装查询结果
		List<Object[]> resultList = new ArrayList<Object[]>();
		HSSFWorkbook workBook = null;
		// 要下载的文件条数
		int commonSize = 0;
		if (null != commonList && !commonList.isEmpty()){
			commonSize = commonList.size();
		}
		// 要下载的文件条数大于length，先打包再下载
		if (commonSize > length){
	        // 盛装多少File，生成多少excel
	        List<File> fileList = new ArrayList<File>();
	        File zipFile = null;
	        // 获取一个临时目录（注意用过之后，要把此文件的临时文件删除掉。）
	        String prefix = FileUtils.getTempDir();
	        // 第一层循环：获取excel个数
	        // 第二层循环：获取每个excel放多少数据
	        for (int j = 0, n = (commonSize % length == 0) ? (commonSize / length) : (commonSize / length + 1) ; j < n; j++) {
	        	int jlength = j * length;
	        	for (int i = 1, min = (commonSize - jlength) > length ? (length + 1) : (commonSize - jlength + 1); i < min; i++) {
					Map<String, Object> map = commonList.get(jlength + i - 1);
					Object[] result = new Object[commonName.length];
					for (int t = 0; t < commonName.length; t ++) {
						result[t] = map.get(commonName[t]) == null ? " " : map.get(commonName[t]);
					}
					resultList.add(result);
	        	}
	        	// 制作sheet表
	        	workBook = createExcel(fileNameParam, resultList, secondTitle);
	        	// 当导出数据超出65533行时，分成两个excel文件导出(两个表头占两行)；
	        	// 前65533条数据导出的excel命名为“起始日期（格式为YYYY-MM-DD）-结束日期（格式为YYYY-MM-DD）渠道统计-1.xlsx”，
	        	// 之后的数据导出的excel命名为“起始日期（格式为YYYY-MM-DD）-结束日期（格式为YYYY-MM-DD）渠道统计-2.xlsx
	        	// 根据数据多少，更换文件名称
	        	fileNameMap.put("fileName", fileName + "-" + (j + 1) + ".xls");
	        	// 得到excel文件在服务器上的完整路径
	        	File excelFile = new File(prefix, fileNameMap.get("fileName"));
	        	OutputStream out = null;
				try {
					out = new BufferedOutputStream(new FileOutputStream(excelFile));
					workBook.write(out);
					out.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					FileUtils.closeStream(null, out);
					if (null != resultList) {
		        		resultList.clear();
		        	}
				}
				fileList.add(excelFile);
	        }
	        // 为zip命名
	        String nowDate = DateUtils.formatCustomDate(new Date(), "yyyyMMddHHmmss");
			String zipName = zipChineseName + nowDate + ".zip";
			// 得到zip文件在服务器上的完整路径
			zipFile = new File(prefix, zipName);
			File deleteZipFile = zipFile;
			// 将文件压缩为zip文件
			ZipUtils.zipFileList(fileList, zipFile);
			// 单独启动一个线程经过delay秒之后删除文件
			FileUtils.timingDeleteFile(60*10, deleteZipFile);
			FileUtils.timingDeleteFile(60*10, fileList);
			// 下载文件
			ServletUtil.downLoadFile(response, zipFile);
		} else {
			// 要下载的文件条数不大于length，直接下载
    		fileNameMap.put("fileName", fileName + ".xls");
    		String fileNameAll = fileNameMap.get("fileName");
    		for (Map<String, Object> map : commonList) {
				Object[] result = new Object[commonName.length];
				for (int t = 0; t < commonName.length; t ++) {
					result[t] = map.get(commonName[t]) == null ? " " : map.get(commonName[t]);
				}
				resultList.add(result);
			}
			// 制作sheet表与下载
        	workBook = createExcel(fileNameParam, resultList, secondTitle);
        	exportExcel(workBook,fileNameAll,request,response);
        	if (null != resultList) {
        		resultList.clear();
        	}
    	}
	}
}