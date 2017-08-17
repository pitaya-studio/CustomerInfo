package com.trekiz.admin.review.depositrefund.dao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.sys.entity.UserJob;

public interface IDepositeRefundReviewNewDao extends BaseDao<Map<String, Object>>{

	/**
	 * 退签证押金审核
	 * @param params	页面参数
	 * @param page      返回对象
	 * @param level     审核层级
	 * @param userJob   角色
	 * @param reviewCompanyId
	 * @param subIds
	 * @return
	 * @author shijun.liu
	 */
	public Page<Map<String, Object>> findCostRefundReviewList(Map<String, String> params, 
			Page<Map<String, Object>> page, List<Integer> level, 
			UserJob userJob,Long reviewCompanyId, List<Long> subIds);
	
	Page<Map<String, Object>> findRefundReviewList(HttpServletRequest request,
			HttpServletResponse response, String groupCode, String productType,
			String startTime, String endTime, String channel, String saler,
			String meter, String inCondition, List<Integer> level, String flowType, String cOrderBy, String uOrderBy, UserJob userJob,Long reviewCompanyId, List<Long> subIds);

	/**
	 * 产品详情查询： 签证
	 * 根据订单id查询签证详情信息
	 */
	Map<String, Object> queryVisaReviewOrderDetail(String prdOrderId);

}
