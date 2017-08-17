package com.trekiz.admin.modules.statistics.order.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.exception.bean.BaseException4Quauq;
import com.trekiz.admin.common.exception.util.LogUtil;
import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.statistics.order.service.OrderDataStatisticsService;

/**
 * 用于订单数据统计的Controller
 * @author shijun.liu
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/statistics/order/controller/odsc")
public class OrderDataStatisticsController {

	private static Log log = LogFactory.getLog(OrderDataStatisticsController.class);
	
	@Autowired
	private OrderDataStatisticsService orderDataStatisticsService;
	
	/**
	 * 导出平台所有供应商的订单数据
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="exportAllOrderDetail")
	public void exportAllOrderDetail(HttpServletRequest request, HttpServletResponse response,Model model){
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		long start = System.currentTimeMillis();
		Workbook workBook = null;
		try {
			workBook = orderDataStatisticsService.exportAllOrderDetail(beginDate, endDate);
		} catch (BaseException4Quauq e) {
			e.printStackTrace(LogUtil.getErrorStream(log));
		}
		log.info("订单数据导出总时间: " + (System.currentTimeMillis() - start)/1000);
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		ServletUtil.downLoadExcel(response, "订单统计信息-"+nowDate + ".xls", workBook);
	}
	
	/**
	 * 导出平台所有供应商的订单数据
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author shijun.liu
	 */
	@RequestMapping(value="queryAllOrderDetail",method = RequestMethod.GET)
	public String queryAllOrderDetail(HttpServletRequest request, HttpServletResponse response,Model model){
		return "modules/statistics/allOrderDetail";
	}
}
