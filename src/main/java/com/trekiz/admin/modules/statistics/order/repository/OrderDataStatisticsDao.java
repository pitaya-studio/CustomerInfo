package com.trekiz.admin.modules.statistics.order.repository;

import java.util.List;

import com.trekiz.admin.common.exception.bean.BaseException4Quauq;
import com.trekiz.admin.modules.statistics.order.bean.OrderDetail;

public interface OrderDataStatisticsDao {

	/**
	 * 导出所有订单信息
	 * @param beginDate		下单时间
	 * @param endDate		下单时间
	 * @return
	 * @author shijun.liu
	 */
	public List<OrderDetail> exportAllOrderDetail(String beginDate, String endDate)throws BaseException4Quauq;
}
