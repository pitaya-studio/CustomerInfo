package com.trekiz.admin.modules.invoice.utils;

import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;
import java.util.Map;

public class InvoiceExcelUtils {
	public static Workbook createInvoiceExcel(List<Map<Object, Object>> list) {
		String fontStyle = "Courier New";
		// 表头
		String[] heads = { "序号", "开票日期", "申请人", "团号", "单号", "开票项目", "开票客户", "金额", "发票号", "审核状态", "到账情况", "备注" };
		// 创建Excel
		HSSFWorkbook workBook = new HSSFWorkbook();
		// 表头字体和样式
		HSSFFont headFont = workBook.createFont();
		HSSFCellStyle headStyle = workBook.createCellStyle();
		headFont.setFontHeightInPoints((short) 10);
		headFont.setFontName(fontStyle);
		headFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headStyle.setAlignment(CellStyle.ALIGN_CENTER);
		headStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		headStyle.setFont(headFont);
		headStyle.setBorderBottom(CellStyle.BORDER_THIN);
		headStyle.setBorderLeft(CellStyle.BORDER_THIN);
		headStyle.setBorderTop(CellStyle.BORDER_THIN);
		headStyle.setBorderRight(CellStyle.BORDER_THIN);

		// 订单数据字体和样式
		HSSFFont dataFont = workBook.createFont();
		HSSFCellStyle dataStyle = workBook.createCellStyle();
		dataFont.setFontHeightInPoints((short) 10);
		dataFont.setFontName(fontStyle);
		dataStyle.setAlignment(CellStyle.ALIGN_CENTER);
		dataStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		dataStyle.setFont(dataFont);
		dataStyle.setBorderBottom(CellStyle.BORDER_THIN);
		dataStyle.setBorderLeft(CellStyle.BORDER_THIN);
		dataStyle.setBorderTop(CellStyle.BORDER_THIN);
		dataStyle.setBorderRight(CellStyle.BORDER_THIN);

		HSSFCellStyle moneyStyle = workBook.createCellStyle();
//		HSSFDataFormat format = workBook.createDataFormat();//数据格式
		moneyStyle.setAlignment(CellStyle.ALIGN_RIGHT);
		moneyStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		moneyStyle.setFont(dataFont);
		moneyStyle.setBorderBottom(CellStyle.BORDER_THIN);
		moneyStyle.setBorderLeft(CellStyle.BORDER_THIN);
		moneyStyle.setBorderTop(CellStyle.BORDER_THIN);
		moneyStyle.setBorderRight(CellStyle.BORDER_THIN);
		moneyStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));

		HSSFSheet sheet = workBook.createSheet();

		// 设置表头信息，填充表头信息
		HSSFRow headRow = sheet.createRow(0);
		headRow.setHeightInPoints((short) 30);
		for (int i = 0; i < heads.length; i++) {
			HSSFCell headCell = headRow.createCell(i);
			headCell.setCellValue(heads[i]);
			headCell.setCellStyle(headStyle);
			sheet.setColumnWidth(i, 5800);
		}

		if (null == list) {
			return workBook;
		}

		// 填充list中的数据到excel的cell中
		for (int i = 0,size = list.size() ; i < size; i++) {
			HSSFRow dataRow = sheet.createRow(i + 1);
			dataRow.setHeightInPoints((short) 20);

			// "序号"
			HSSFCell cellNum = dataRow.createCell(0);
			cellNum.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
			cellNum.setCellValue(i + 1);
			cellNum.setCellStyle(dataStyle);
			
			// 开票日期
			String createDate = "";
			if("1".equals(list.get(i).get("createStatus").toString())){
				createDate = list.get(i).get("updateDate") == null ? ""
						: DateUtils.formatCustomDate(DateUtils.dateFormat(list.get(i).get("updateDate").toString()), "yyyy-MM-dd");
			}
			
			createCell(dataRow, 1, createDate, dataStyle);

			createCell(dataRow, 2, list.get(i).get("createName"), dataStyle);// 申请人
			createCell(dataRow, 3, list.get(i).get("groupCode"), dataStyle);// 团号,varchar
			createCell(dataRow, 4, list.get(i).get("orderNum"), dataStyle);// 单号,varchar
			
			//开票项目
			if(null != list.get(i).get("invoiceSubject")){
				String subject =String.valueOf(list.get(i).get("invoiceSubject"));
				String subjectLabel = DictUtils.getDictLabel(subject,"invoice_subject", null);
				createCell(dataRow, 5, subjectLabel, dataStyle);
			}else{
				createCell(dataRow, 5, "", dataStyle);
			}
			
			createCell(dataRow, 6, list.get(i).get("invoiceCustomer"), dataStyle);// 开票客户,varchar
			
			// 金额,decimal
			HSSFCell cellAmount = dataRow.createCell(7);
			Double invoiceAmount = Double.parseDouble((String.valueOf(list.get(i).get("invoiceAmount"))).replaceAll(",", ""));
			cellAmount.setCellStyle(moneyStyle);
			cellAmount.setCellValue(invoiceAmount);
//			createCell(dataRow, 7, list.get(i).get("invoiceAmount"), moneyStyle);
			
			// 发票号,varchar
			short verifyStatus = (short) list.get(i).get("verifyStatus");
			short createStatus = (short) list.get(i).get("createStatus");
			if (verifyStatus == 1 && createStatus == 1){
				createCell(dataRow, 8, list.get(i).get("invoiceNum"), dataStyle);
			}else {
				createCell(dataRow, 8,"", dataStyle);
			}

			// 审核状态
			if (null != list.get(i).get("verifyStatus")) {
				if (verifyStatus == 0) {
					createCell(dataRow, 9, "未审核", dataStyle);
				} else if (verifyStatus == 1) {
					createCell(dataRow, 9, "审核通过", dataStyle);
				} else {
					createCell(dataRow, 9, "被驳回", dataStyle);
				}
			}
			
			// 到账状态,smallint,发票领取状态:0表示未领取，1表示已领取
//			if (null != list.get(i).get("receiveStatus")) {
//				short receiveStatus = (short) list.get(i).get("receiveStatus");
//
//				if (receiveStatus == 0) {
//					createCell(dataRow, 10, "未到账", dataStyle);
//				} else {
//					createCell(dataRow, 10, "已到账", dataStyle);
//				}
//			}
			createCell(dataRow, 10, "", dataStyle);
			//每个单元格只能容纳 32767个字符
			Object obj = list.get(i).get("remarks");
			String remark = StringUtils.blankReturnEmpty(obj);
			if(remark.length() > 32767){
				remark = remark.substring(0, 32767);
			}
			createCell(dataRow, 11, remark, dataStyle);// 备注
		}

		return workBook;
	}

	private static void createCell(HSSFRow row, int index, Object value, CellStyle cellStyle) {
		if (null == value) {
			value = "";
		}
		HSSFCell cell = row.createCell(index);
		cell.setCellValue(String.valueOf(value));
		cellStyle.setWrapText(true);
		cell.setCellStyle(cellStyle);
	}

}
