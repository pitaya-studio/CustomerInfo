package com.trekiz.admin.modules.review.visareturnreceipt.service;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.sys.entity.UserJob;

import freemarker.template.TemplateException;

/**
 * 还签证收据service
 * @author xinwei.wang
 */
public interface IVisaReturnReceiptService {

	Page<Map<String, Object>> queryVisaReturnReceiptReviewInfo(HttpServletRequest request, HttpServletResponse response, String roleID);
	
	/**
	 * 签证批量还收据申请
	 * @param visaIds：订单ids
	 * @param travellerIds：游客ids
	 * @param returnReceiptJe：借款金额
	 * @param returnReceiptName：还收据人姓名
	 * @param returnReceiptTime：还收据时间
	 * @param returnReceiptRemark：还收据备注信息
	 * @return
	 */
	public Map<String, Object> visaBatchHsj(String visaIds, String travellerIds,
				String returnReceiptJe, String returnReceiptName, String returnReceiptTime, String returnReceiptRemark);
	
	public void updateReviewPrintInfoById (Long revid,Date printdate) throws Exception ;
	
	
	public File createVisaReturnMoneySheetDownloadFile(Long revid) throws IOException, TemplateException;
	
	
	/*********************   环球行批次审核相关     开始      *********************/
	
	/**
	 * 签证按批次还收据申
	 * @param visaIds：订单ids
	 * @param travellerIds：游客ids
	 * @param returnReceiptJe：借款金额
	 * @param returnReceiptName：还收据人姓名
	 * @param returnReceiptTime：还收据时间
	 * @param returnReceiptRemark：还收据备注信息
	 * @return
	 */
	public Map<String, Object> visaBatchHsj(String batchNo,String visaIds, String travellerIds,
				String returnReceiptJe, String returnReceiptName, String returnReceiptTime, String returnReceiptRemark);
	
	Page<Map<String, Object>> queryVisaReturnReceiptBatchReviewInfo(HttpServletRequest request, HttpServletResponse response, String roleID);
	
	public File createBatchVisaReturnMoneySheetDownloadFile(Long revid,String batchno) throws IOException, TemplateException;
	
	public void visaReturnReceiptPostReviewPassedTackle4HQX(Review review);
	
	/*********************   环球行批次审核相关     结束      *********************/
	
	public String getHSJActiveReview(String orderId,Long trvelerId);
	
	
	//批次批量审批
	public boolean multiBatchReviewvisaReturnReceiptbyBatchNo(String result,String remarks, String batchnons);
	
	//--- wxw added 2015-09-28 处理批次获取审核数量不准确的问题---
	public int getJobReviewCountbyUserJob(UserJob userJob);
	public int getBatchReviewCount4Menu();
	
	
	/**
	 * 处理还签证收据审核打印（批次），还款说明只能显示一个游客备注的问题：
	 * 20151103王新伟 added
	 */
	public String getReturnReCeiptBatchPrintAppReason(String batchNo);
	
}
