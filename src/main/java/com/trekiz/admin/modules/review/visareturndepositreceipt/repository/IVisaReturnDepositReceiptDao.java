package com.trekiz.admin.modules.review.visareturndepositreceipt.repository;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.sys.entity.UserJob;

public interface IVisaReturnDepositReceiptDao { 
    /**
     * 还签证押金收据列表页查询
     */
	Page<Map<String, Object>> queryVisaReturnDepositReceiptReviewList(
			HttpServletRequest request, HttpServletResponse response,
			String groupCode, String startTime, String endtime, String agent,
			String travlerName,String orderNum,String depositeAmount,
			String orderCreateBy,String saler, String jdsaler, String status,
			String visaType,List<Integer> levels, UserJob userJob, Long reviewCompanyId,List<Long> subIds);

}
