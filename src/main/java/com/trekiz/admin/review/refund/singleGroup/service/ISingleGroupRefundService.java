package com.trekiz.admin.review.refund.singleGroup.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.airticketorder.entity.RefundBean;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;

public interface ISingleGroupRefundService {

	/**
	 * 发起单团类退款申请，在申请成功后处理游客相关信息
	 * 
	 * @author yunpeng.zhang
	 * @createDate 2015年11月30日
	 * @param productOrder
	 * @param productGroup
	 * @param product
	 * @param refundRecords
	 * @param deptId
	 * @param productType
	 * @return
	 */
	Map<String, Object> startGroupRefundAndHandleTravler(ProductOrderCommon productOrder, ActivityGroup productGroup,
			TravelActivity product, List<RefundBean> refundRecords, Long deptId, Integer productType) throws Exception;

	/**
	 * 取消申请退款后，更改newProcessMoneyAmountService 对应的数据
	 * @param reviewId
	 */
	void handleMoneyAmount(String reviewId);

}
