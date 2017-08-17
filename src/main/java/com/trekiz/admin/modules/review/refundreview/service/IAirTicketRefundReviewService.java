package com.trekiz.admin.modules.review.refundreview.service;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.reviewflow.entity.Review;

import freemarker.template.TemplateException;

/**
 * 退款审批处理service
 * 
 * @author chy
 * 
 */
public interface IAirTicketRefundReviewService {

	Page<Map<String, Object>> queryRefundReviewList(HttpServletRequest request,
			HttpServletResponse response);

	Map<String, Object> queryGrouporderDeatail(String prdOrderId);
	
	/**
	 * 根据订单id查询签证详情信息
	 */
	Map<String, Object> queryVisaorderDeatail(String prdOrderId);
	
	/**
	 * 根据订单id查询机票订单详情
	 * @param prdOrderId
	 * @return
	 */
	public Map<String, Object> queryAirticketorderDeatail(String prdOrderId, String prdType);
	
	/**
	 * 根据订单id查询散拼订单详情
	 * @param prdOrderId
	 * @return
	 */
	public Map<String, Object> querySanPinReviewOrderDetail(String prdOrderId);
	
	/**
	 * 根据订单id查询散拼切位订单详情
	 */
	public Map<String, Object> querySanPinReserveOrderDetail(String prdOrderId);

	/**
	 * 组织退款单数据  
	 * @param request
	 * @return
	 * @throws TemplateException 
	 * @throws IOException 
	 */
	File createDownloadFile(HttpServletRequest request) throws IOException, TemplateException;
	
	/**
	 * 发起审核申请校验
	 */
	public String beforeAddReview(Map<String, Object> params);
	
	/**
	 * 取消其它审核流程
	 */
	public String cancelOtherReview(Map<String, Object> params);
	
	/**
	 * 退款业务操作公共接口
	 */
	public boolean doRefund(Review review);
	
	/**
	 * 退款审核退款单
	 * @author jiachen
	 * @DateTime 2015年6月15日 下午2:39:49
	 * @param map
	 * @param reviewId
	 * @return void
	 */
	public void refundTable(String reviewId, Map<String, Object> map);
	
	/**
	 * 下载退款审核退款单word版
	 * @author jiachen
	 * @DateTime 2015年6月16日 上午10:24:26
	 * @param reviewId
	 * @return File
	 */
	public File downLoadRefundTable(String reviewId) throws IOException, TemplateException;
}
