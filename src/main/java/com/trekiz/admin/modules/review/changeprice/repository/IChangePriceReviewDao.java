package com.trekiz.admin.modules.review.changeprice.repository;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.sys.entity.UserJob;

public interface IChangePriceReviewDao {

	Page<Map<String, Object>> findRefundReviewList(HttpServletRequest request,
			HttpServletResponse response, String groupCode,
			String startTime, String endTime, String channel, String saler,
			String meter, String inCondition, List<Integer> level, String flowType, String cOrderBy, String uOrderBy, UserJob userJob, Long reviewCompanyId, List<Long> subIds);
	
	Map<String, Object> queryAirticketReviewOrderDetail(String prdOrderId, String prdType);

	Map<String, Object> queryGroupReviewOrderDetail(String prdOrderId);
	
	/**
	 * 产品详情查询：散拼
	 * @param prdOrderId
	 * @return
	 */
	Map<String, Object> querySanPinReviewOrderDetail(String prdOrderId);
	
//	/**  改价无切位  注释掉
//	 * 产品详情查询：散拼切位
//	 */
//	Map<String, Object> querySanPinReserveOrderDetail(String prdOrderId);

	/**
	 * 产品详情查询： 签证
	 * 根据订单id查询签证详情信息
	 */
	Map<String, Object> queryVisaReviewOrderDetail(String prdOrderId);

}
