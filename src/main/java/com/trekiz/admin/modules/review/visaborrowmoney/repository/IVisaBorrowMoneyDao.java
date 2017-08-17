package com.trekiz.admin.modules.review.visaborrowmoney.repository;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.sys.entity.UserJob;

public interface IVisaBorrowMoneyDao {

	Page<Map<String, Object>> queryVisaBorrowMoneyReviewList(
			HttpServletRequest request, HttpServletResponse response,
			String groupCode, String startTime, String endtime, String agent,
			String orderCreateBy,String saler, String jdsaler, String status,
			String visaType, List<Integer> levels, UserJob userJob, Long reviewCompanyId,List<Long> subIds);
	
	
	/*********************   新行者借款相关     开始      *********************/
	/*********************   新行者借款相关     开始      *********************/
	Page<Map<String, Object>> queryVisaBorrowMoney4XXZReviewList(
			HttpServletRequest request, HttpServletResponse response,
			String groupCode, String startTime, String endtime, String agent,
			String orderCreateBy,String saler, String jdsaler, String status,String visaType, List<Integer> levels, UserJob userJob, Long reviewCompanyId,List<Long> subIds);
	
	/*********************   新行者借款相关     开始      *********************/
	/*********************   新行者借款相关     开始      *********************/
	
	
	
	/*********************   批次借款审核相关     开始      *********************/
	/*********************   批次借款审核相关     开始       *********************/
	Page<Map<String, Object>> queryVisaBorrowMoneyBatchReviewList(
			HttpServletRequest request, HttpServletResponse response,
			String groupCode, String startTime, String endtime, String agent,String printstatus,String travlerName,
			String batchBorrowAmountStart,String batchBorrowAmountEnd,
			String saler,String sysCountryId,String jdsaler, String status,String visaType, List<Integer> levels, UserJob userJob, Long reviewCompanyId,List<Long> subIds);
	
	/*********************   批次借款审核相关     结束      *********************/
	/*********************   批次借款审核相关     结束       *********************/

}
