package com.trekiz.admin.modules.statistics.databse.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.statistics.databse.service.ITableToExcelService;

@Controller
@RequestMapping(value = "${adminPath}/statistics/database/controller/exportToExcel")
public class TableToExcelController {

	@Autowired
	private ITableToExcelService tableToExcelService;
	
	@ResponseBody
	@RequestMapping(value="exportToExcel")
	public void exportToExcel(HttpServletRequest request, HttpServletResponse response){
		try {
			String path = tableToExcelService.exportDataToExcel();
			//long start = System.currentTimeMillis();
			//String path = tableToExcelService.exportDataToExcelByThread();
			//System.out.println("Time is:" + (System.currentTimeMillis() - start));
			String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
			String fileName = "table_to_excel_" + nowDate + ".xlsx";
			ServletUtil.downLoadFile(response, fileName, path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
