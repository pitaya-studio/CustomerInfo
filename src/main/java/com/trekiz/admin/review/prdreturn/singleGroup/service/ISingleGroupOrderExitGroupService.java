package com.trekiz.admin.review.prdreturn.singleGroup.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;

public interface ISingleGroupOrderExitGroupService {

	/**
	 * 发起单团退团申请，同时在申请成功后更改游客状态等
	 * 
	 * @author yunpeng.zhang
	 * @createDate 2015年11月23日
	 * @param productOrder
	 * @param productGroup
	 * @return
	 */
	Map<String, Object> startExitGroupAndHandleTravler(ProductOrderCommon productOrder, ActivityGroup productGroup,
			TravelActivity product, Map<String, String[]> parameters, Long deptId, Integer productType, HttpServletRequest request) throws Exception;

	/**
	 * 改变游客状态和所涉及的金额（包括新的金额表→NewProcessMoneyAmount：通过reviewId来确定 和 旧的金额表→
	 * traveler表SubtractMoneySerialNum字段对应的moneyAmount）
	 * 
	 * @author yunpeng.zhang
	 * @createDate 2015年11月25日
	 * @param travelerId
	 * @param reviewId
	 */
	void handleTravelerAndMoneyAmount(Long travelerId, String reviewId);

	/**
	 * 在退团申请前，进行互斥验证
	 * @auhor yunpeng.zhang
	 * @createDate 2015年12月15日10:55:38
	 * @param productOrder
	 * @param productGroup
	 * @param product
	 * @param parameters
	 * @param deptId
	 * @param productType
     * @return
     */
	Map<String,Object> checkSingleGroupExitGroup(ProductOrderCommon productOrder, ActivityGroup productGroup, TravelActivity product,
								   Map<String, String[]> parameters, Long deptId, Integer productType);

	/**
	 * 退团业务数据list<map>
	 * @param processList
	 */
	void handleAfterAndRefund(List<Map<String, Object>> processList);
	
	/**
	 * 退团业务数据map 单个游客
	 * @param oneMap
	 */
	void handleAfterAndRefundEvery(Map<String, Object> oneMap);

	/**
	 * 根据订单唯一标识,订单类型查询游客退团信息，符合条件的游客有：delFlag 0 正常 4 转团审核中 的游客
	 * @param orderId
	 * @param orderType
	 * @author yunpeng.zhang
     * @createDate 2015年12月23日14:22:13
	 */
	List<Map<Object, Object>> getTravelerByOrderId4ExitGroup(Long id, Integer productType);
	
}
