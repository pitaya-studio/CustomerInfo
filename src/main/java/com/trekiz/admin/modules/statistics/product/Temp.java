package com.trekiz.admin.modules.statistics.product;

import com.trekiz.admin.common.utils.JDBCUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Temp {

	public static void main(String[] args){
		StringBuffer str = new StringBuffer();
		str.append("SELECT office.NAME AS companyName, tra.acitivityName, ")
		   .append(" (SELECT label FROM trekizwholesalertts.sys_dict WHERE type = 'from_area' AND VALUE = tra.fromArea ) AS fromArea, ")
		   .append(" tra.activityDuration, agp.groupOpenDate, agp.planPosition, agp.freePosition, agp.settlementAdultPrice,")
		   .append(" agp.settlementcChildPrice, agp.settlementSpecialPrice, tra.id AS productId ")
		   .append(" FROM trekizwholesalertts.activitygroup agp, trekizwholesalertts.sys_office office, ")
		   .append(" trekizwholesalertts.travelactivity tra WHERE agp.srcActivityId = tra.id ")
		   .append(" AND office.id = tra.proCompany AND AGP.groupOpenDate IS NOT NULL  ")
		   .append(" AND agp.groupOpenDate > '2016-06-11' AND agp.groupOpenDate <= '2016-09-30' ")
		   .append(" AND office.id IN (68, 353, 122, 379, 383, 356) ORDER BY office.id ");
		List<Map<String,String>> list = new ArrayList<>();
		Connection conn = JDBCUtils.getInstance().getMysqlConn();
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(str.toString());
			while(rs.next()){
				String companyName = rs.getString("companyName");
				String productName = rs.getString("acitivityName");
				String fromArea = rs.getString("fromArea");
				String activityDuration = rs.getString("activityDuration");
				String groupOpenDate = rs.getString("groupOpenDate");
				String planPosition = rs.getString("planPosition");
				String freePosition = rs.getString("freePosition");
				String settlementAdultPrice = rs.getString("settlementAdultPrice");
				String settlementcChildPrice = rs.getString("settlementcChildPrice");
				String settlementSpecialPrice = rs.getString("settlementSpecialPrice");
				String productId = rs.getString("productId");

				Map<String, String> map = new HashMap<>();
				map.put("companyName", companyName);
				map.put("productName", productName);
				map.put("fromArea", fromArea);
				map.put("activityDuration", activityDuration);
				map.put("groupOpenDate", groupOpenDate);
				map.put("planPosition", planPosition);
				map.put("freePosition", freePosition);
				map.put("settlementAdultPrice", settlementAdultPrice);
				map.put("settlementcChildPrice", settlementcChildPrice);
				map.put("settlementSpecialPrice", settlementSpecialPrice);
				String targetAddress = getArriviedAddressByProductId(conn, productId);
				map.put("departArea", targetAddress);
				list.add(map);
			}
		}catch (SQLException e){
			e.printStackTrace();
		}finally {
			JDBCUtils.close(conn, stmt, rs);
		}
		Workbook workBook = createOrderExcel(list);

		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(new File("d:\\product.xls")));
			workBook.write(bos);
			bos.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


	}

	private static String getArriviedAddressByProductId(Connection conn, String productId){
		StringBuffer targetAddress = new StringBuffer();
		StringBuffer str = new StringBuffer();
		//目的地如果有多个则只取一个即可
		str.append("SELECT t.srcActivityId, r.name FROM ").append(" trekizwholesalertts.activitytargetarea t, trekizwholesalertts.sys_area r ")
		   .append(" WHERE t.srcActivityId = ? AND t.targetAreaId = r.id ");
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(str.toString());
			pstmt.setInt(1, Integer.parseInt(productId));
			rs = pstmt.executeQuery();
			while(rs.next()){
				String name = rs.getString("name");
				if(targetAddress.length() == 0){
					targetAddress.append(name);
				}else{
					targetAddress.append(",").append(name);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			JDBCUtils.close(null, pstmt, rs);
		}
		return targetAddress.toString();
	}

	private static Workbook createOrderExcel(List<Map<String, String>> products){
		String fontStyle = "Courier New";
		//表头
		String[] heads = {"供应商", "产品名称", "出发地", "目的地",
				"行程天数", "出团日期", "预收", "余位",
				"成人同行价", "儿童同行价", "特殊人群同行价"};
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

		HSSFSheet sheet = workBook.createSheet();

		HSSFRow headRow = sheet.createRow(0);
		headRow.setHeightInPoints((short)24);
		for (int i = 0; i<heads.length; i++) {
			HSSFCell headCell = headRow.createCell(i);
			headCell.setCellValue(heads[i]);
			headCell.setCellStyle(headStyle);
			sheet.setColumnWidth(i, 5800);
		}
		for (int i = 0; i < products.size(); i++) {
			HSSFRow dataRow = sheet.createRow(i+1);
			dataRow.setHeightInPoints((short)20);
			createCell(dataRow, 0, products.get(i).get("companyName"), dataStyle);
			createCell(dataRow, 1, products.get(i).get("productName"), dataStyle);
			createCell(dataRow, 2, products.get(i).get("fromArea"), dataStyle);
			createCell(dataRow, 3, products.get(i).get("departArea"), dataStyle);
			createCell(dataRow, 4, products.get(i).get("activityDuration"), dataStyle);
			createCell(dataRow, 5, products.get(i).get("groupOpenDate"), dataStyle);
			createCell(dataRow, 6, products.get(i).get("planPosition"), dataStyle);
			createCell(dataRow, 7, products.get(i).get("freePosition"), dataStyle);
			createCell(dataRow, 8, products.get(i).get("settlementAdultPrice"), dataStyle);
			createCell(dataRow, 9, products.get(i).get("settlementcChildPrice"), dataStyle);
			createCell(dataRow, 10, products.get(i).get("settlementSpecialPrice"), dataStyle);
		}
		return workBook;
	}

	private static void createCell(HSSFRow row, int index, String value, CellStyle cellStyle){
		HSSFCell cell = row.createCell(index);
		cell.setCellValue(value);
		cell.setCellStyle(cellStyle);
	}

}
