package com.trekiz.admin.modules.statistics.order.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.exception.bean.BaseException4Quauq;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.statistics.order.bean.OrderDetail;
import com.trekiz.admin.modules.statistics.order.repository.OrderDataStatisticsDao;
import com.trekiz.admin.modules.statistics.order.service.OrderDataStatisticsService;
import com.trekiz.admin.modules.statistics.utils.ExcelUtils;

/**
 * 订单数据统计的实体类
 * @author shijun.liu
 *
 */
@Service
public class OrderDataStatisticsServiceImpl implements OrderDataStatisticsService{

	private static Log log = LogFactory.getLog(OrderDataStatisticsServiceImpl.class); 
	
	@Autowired
	private OrderDataStatisticsDao orderDataStatisticsDao;
	/**
	 * 导出平台所有供应商的订单数据
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @author shijun.liu
	 * @throws BaseException4Quauq 
	 */
	public Workbook exportAllOrderDetail(String beginDate, String endDate) throws BaseException4Quauq{
		if(StringUtils.isBlank(beginDate)){
			beginDate = Context.DEFAULT_BEGIN_TIME;
		}else{
			beginDate = beginDate + " 00:00:00";
		}
		if(StringUtils.isBlank(endDate)){
			endDate = Context.DEFAULT_END_TIME;
		}else{
			endDate = endDate + " 23:59:59";
		}
		List<OrderDetail> list = null;
		try {
			long start = System.currentTimeMillis();
			list = orderDataStatisticsDao.exportAllOrderDetail(beginDate, endDate);
			log.info("数据取出时间: " + (System.currentTimeMillis() - start)/1000);
		} catch (BaseException4Quauq e) {
			throw e;
		}
		Workbook workBook =  ExcelUtils.createOrderExcel(list);
		return workBook;
	}
}
