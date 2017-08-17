package com.trekiz.admin.modules.statistics.product.service;

import org.apache.poi.ss.usermodel.Workbook;

/**
 * 所有供应商的产品统计Service类
 * @author shijun.liu
 *
 */
public interface ProductStatisticsService {

	/**
	 * 查询所有供应商的所有产品信息
	 * @return
	 * @author shijun.liu
	 */
	public Workbook getAllProductInfo();

	/**
	 * 指定时间区间内，统计所有公司创建产品的数量。 返回Excel的Workbook.
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public Workbook getProductSumPerOffice(String beginDate, String endDate);
}
