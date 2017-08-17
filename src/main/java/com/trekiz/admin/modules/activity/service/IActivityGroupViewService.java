package com.trekiz.admin.modules.activity.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.activity.entity.ActivityGroupView;
import com.trekiz.admin.modules.activity.entity.ActivityReserveOrderView;
import com.trekiz.admin.modules.activity.entity.TargetArea;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;


/**
 * 旅游产品信息Service
 * @author liangjingming
 */
@Service
@Transactional(readOnly = true)
public interface IActivityGroupViewService {

 Page<ActivityReserveOrderView> findReserveOrderList(Page<ActivityReserveOrderView> page,TravelActivity travelActivity, String settlementAdultPriceStart,
			String settlementAdultPriceEnd,Long agentId,String paymentType, DepartmentCommon common);

Page<ActivityGroupView> findActivityGroupReview(Page<ActivityGroupView> page,
		TravelActivity travelActivity, String settlementAdultPriceStart,
		String settlementAdultPriceEnd, Long agentId, Integer activityKind,
		String review, Long companyId,DepartmentCommon common);

Page<Map<String, Object>> findActivityGroupCostView(Page<Map<String, Object>> page,
		TravelActivity travelActivity, DepartmentCommon common, Map<String, Object> params);

ActivityReserveOrderView findReserveOrderInfo(Integer aid);

List<TargetArea> findTargetArea();
Page<ActivityGroupView> findActivityGroupReviewC325(Page<ActivityGroupView> page,
		TravelActivity travelActivity,Map<String,String> map,Long agentId, Integer activityKind,DepartmentCommon common);
}
