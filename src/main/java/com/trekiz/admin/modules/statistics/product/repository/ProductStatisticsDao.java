package com.trekiz.admin.modules.statistics.product.repository;

import java.util.List;

import com.trekiz.admin.modules.statistics.product.bean.ProductCount;
import com.trekiz.admin.modules.statistics.product.bean.ProductInfo;


public interface ProductStatisticsDao {

	/**
	 * 查询1.5版本所有供应商下面的所有产品信息
	 * @return
	 */
	public List<ProductInfo> getAllProductInfoFor1_5();
	
	/**
	 * 查询1.1版本下面所有供应商下面的所有产品数据信息
	 * @return
	 */
	public List<ProductInfo> getAllProductInfoFor1_1();

	/**
	 * 根据指定时间，查询每个客户下的创建产品的数量。
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<ProductCount> getProductSumPerOffice(String beginDate, String endDate);
}
