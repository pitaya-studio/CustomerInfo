package com.trekiz.admin.modules.review.visareturnreceipt.repository;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.sys.entity.UserJob;

public interface IVisaReturnReceiptDao {


	Page<Map<String, Object>> queryVisaReturnReceiptReviewList(
			HttpServletRequest request, HttpServletResponse response,
			String groupCode, String startTime, String endtime, String agent,
			String orderCreateBy,String saler, String jdsaler, String status, 
			List<Integer> levels, UserJob userJob, Long reviewCompanyId,List<Long> subIds);
	
	/*********************   批次借款审核相关     开始      *********************/
	/*********************   批次借款审核相关     开始       *********************/
	Page<Map<String, Object>> queryVisaReturnReceiptBatchReviewList(
			HttpServletRequest request, HttpServletResponse response,
			String groupCode, String startTime, String endtime, String agent,String printstatus,
			String saler, String jdsaler, String status, List<Integer> levels, UserJob userJob, Long reviewCompanyId,List<Long> subIds);
	
	
	/*********************   批次借款审核相关     结束      *********************/
	/*********************   批次借款审核相关     结束       *********************/

}
