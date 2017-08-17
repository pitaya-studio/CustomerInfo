package com.trekiz.admin.modules.statistics.product.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.modules.island.util.StringUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.statistics.product.service.ProductStatisticsService;

/**
 * 用于订单数据统计的Controller
 * @author shijun.liu
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/statistics/product/controller/psc")
public class ProductStatisticsController {

	private static Log log = LogFactory.getLog(ProductStatisticsController.class);
	
	@Autowired
	private ProductStatisticsService productStatisticsService;
	/**
	 * 导出平台所有供应商的订单数据
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="exportAllProduct")
	public void exportAllProduct(HttpServletRequest request, HttpServletResponse response,Model model){
		Workbook workBook = productStatisticsService.getAllProductInfo();
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		ServletUtil.downLoadExcel(response, "产品统计信息-"+nowDate + ".xls", workBook);
	}

	@RequestMapping(value="exportProductNumPerOffice")
	public void exportProductNumPerOffice(HttpServletRequest request,HttpServletResponse response){
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		Workbook workBook = productStatisticsService.getProductSumPerOffice(beginDate,endDate);
		String today = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String mark;
		if (StringUtil.isBlank(beginDate)){
			if (StringUtil.isBlank(endDate)){
				mark="全部";
			}else {
				mark="开始至"+endDate;
			}
		}else {
			if (StringUtil.isBlank(endDate)){
				mark=beginDate+"至"+today;
			}else {
				mark=beginDate+"至"+endDate;
			}
		}
		StringBuilder fileName = new StringBuilder();
		fileName.append("客户创建产品数量统计表(").append(mark).append(").xls");
		ServletUtil.downLoadExcel(response,fileName.toString(),workBook);
	}
}
