package com.trekiz.admin.modules.review.airticketreturn.repository;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.sys.entity.UserJob;

public interface IAirticketReturnDao {

	Map<String, Object> queryAirTicketReturnDetailInfoById(String id);

	Page<Map<String, Object>> queryAirticketRetturnReviewList(
			HttpServletRequest request, HttpServletResponse response,
			String groupCode, String startTime, String endtime, String agent,
			String saler, String jdsaler, String status, List<Integer> level, String cOrderBy, String uOrderBy, String orderId, UserJob userJob,Long reviewCompanyId, List<Long> subIds);

}
