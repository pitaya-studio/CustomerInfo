package com.trekiz.admin.modules.review.visaborrowmoney.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.order.formBean.PrintParamBean;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.traveler.entity.Traveler;

import freemarker.template.TemplateException;

/**
 * 签证借款service
 * @author  xinwei.wang
 */
public interface IVisaBorrowMoneyService {

	Page<Map<String, Object>> queryVisaBorrowMoneyReviewInfo(HttpServletRequest request, HttpServletResponse response, String roleID);
	
	//List<Role>  getWorkFlowRolesByFlowType(Integer productType,Integer flowType);
	
	public Role  getWorkFlowRoleByFlowTypeAndLevel(Integer productType,Integer flowType,Integer level);
	
	public File createVisaBorrowMoneySheetDownloadFile(Long revid) throws IOException, TemplateException;
	
	public void updateReviewPrintInfoById (Long revid,Date printdate) throws Exception ;
	
	//签证批量借款
	public Map<String, Object> visaBatchJk(String visaIds, String travellerIds,
			String persons, String dates, String moneys, String others);
	
	/*********************   新行者借款相关     开始      *********************/
	public List<Traveler> getTravelerList(String orderId);
	
	Page<Map<String, Object>> queryVisaBorrowMoney4XXZReviewInfo(HttpServletRequest request, HttpServletResponse response, String roleID);
	
	public File createVisaBorrowMoney4XXZSheetDownloadFile(Long revid, String payId, String option) throws IOException, TemplateException;
	
	/*********************   新行者借款相关     结束      *********************/
	
	
	
	/**
	 * 其他币种转换成人民币
	 */
	public float currencyConverter(String count,String currencyId);
	
	public String getXinXingZheActiveReview(String orderId);
	
	
	/*********************   环球行批次审核相关     开始      *********************/
	
	Page<Map<String, Object>> queryVisaBorrowMoneyBatchReviewInfo(HttpServletRequest request, HttpServletResponse response, String roleID);
	
	//签证批量借款
	public Map<String, Object> visaBatchJk(String batchNo,String visaIds, String travellerIds,
				String persons, String dates, String moneys, String others);
		
	/**
	 * 通过批次号查询该批次下游客的信息(针对签证借款)
	 * @author jiachen
	 * @DateTime 2015年5月28日 上午10:07:17
	 * @param batchNo
	 * @return List<Map<String, String>>
	 */
	public void getTravelerList(String batchNo, List<Map<String, String>> travelerList);
	
	/**
	 * 通过批次号查询该批次下游客的信息(针对还押金收据)
	 * @author jiachen
	 * @DateTime 2015年5月28日 上午10:07:17
	 * @param batchNo
	 * @return List<Map<String, String>>
	 */
	public void getTravelerListForReturnReceipt(String batchNo, List<Map<String, String>> travelerList);
	

	public File createBatchVisaBorrowMoneySheetDownloadFile(Long revid,String botchno, String payId, String option) throws IOException, TemplateException;
	

	/**
	 * 签证借款批次审批导出游客信息
	 * @author jiachen
	 * @DateTime 2015年5月29日 上午11:03:54
	 * @param batchNo
	 * @return void
	 */
	public void exportTravelerInfo(String batchNo, HttpServletRequest request, HttpServletResponse response);
	
	public void visaBorrowMoneyPostReviewPassedTackle4XXZ(Review review);
	public void visaBorrowMoneyPostReviewPassedTackle4HQX(Review review);

	/*********************    环球行批次审核相关     结束      *********************/
	
	public String getHQXActiveReview(String orderId,Long trvelerId);
	
	//批次批量审批
	public boolean multiBatchReviewVisaBorrowMoneybyBatchNo(String result,String remarks, String batchnons);
	
	//--- wxw added 2015-09-18 处理批次获取审核数量不准确的问题---
	public int getJobReviewCountbyUserJob(UserJob userJob);
	public int getBatchReviewCount4Menu();
	
	
	/**
	 * 处理签证借款审核打印（批次），借款理由只能显示一个游客借款理由的问题：
	 * 20151102 王新伟 added
	 */
	public String getVisaBorrowBatchPrintAppReason(String batchNo);

	/**
	 * 把原有的对应的打印方法内容复制过来，保持原有的业务不变，接收返回的Map数据。该方法用于批量打印借款付款单，需要
	 * 把Controller里面的单个打印方法内容提取到Service层。
	 * @param paramBean
	 * @return
	 */
	public Map<String,Object> getVisaBorrowMoney4XXZPrintMap(PrintParamBean paramBean);

	/**
	 * 把原有的Controller里面对应的打印方法内容，提取到Service层的该方法中，用于批量打印，方法内的业务逻辑不变。
	 * @param paramBean
	 * @return
	 */
	public Map<String,Object> getVisaBorrowMoneyBatchFeePrint(PrintParamBean paramBean);

	/**
	 * 更新签证借款付款单的打印状态，打印时间等。
	 * @param batchNo
	 * @param printDateStr
	 */
	public boolean updatePrintStatus(String batchNo,String printDateStr);
	
}
