package com.trekiz.admin.modules.statistics.order.service;

import org.apache.poi.ss.usermodel.Workbook;

import com.trekiz.admin.common.exception.bean.BaseException4Quauq;

public interface OrderDataStatisticsService {

	/**
	 * 导出所有供应商的订单数据信息
	 * @param beginDate		下单时间
	 * @param endDate		下单时间
	 * @return
	 * @author shijun.liu
	 */
	public Workbook exportAllOrderDetail(String beginDate, String endDate) throws BaseException4Quauq;
}
