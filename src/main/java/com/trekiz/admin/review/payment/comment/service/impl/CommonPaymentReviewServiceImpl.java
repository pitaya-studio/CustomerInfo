package com.trekiz.admin.review.payment.comment.service.impl;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.config.ReviewErrorCode;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.engine.repository.ReviewNewDao;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.payment.comment.dao.ICommonPaymentReviewDao;
import com.trekiz.admin.review.payment.comment.entity.PaymentParam;
import com.trekiz.admin.review.payment.comment.exception.PaymentException;
import com.trekiz.admin.review.payment.comment.service.ICommonPaymentReviewService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 *
 * 审批模块，成本付款审批对应的Service接口的实现类
 * @author shijun.liu
 * @date 2015年11月17日
 */
@Service
public class CommonPaymentReviewServiceImpl implements ICommonPaymentReviewService{

	private static final Log LOG = LogFactory.getLog(CommonPaymentReviewServiceImpl.class);
	
	@Autowired
	private ICommonPaymentReviewDao commonPaymentReviewDao;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ReviewNewDao reviewNewDao;
	@Autowired
	private CostRecordDao costRecordDao;
	@Autowired
	private UserReviewPermissionChecker permissionChecker;
	
	@Override
	public Page<Map<String, Object>> getPaymentReviewList(
			Page<Map<String, Object>> page, PaymentParam paymentParam) {
		Page<Map<String, Object>> pageMap = commonPaymentReviewDao.getPaymentReviewList(page, paymentParam);
		
		for (Map<String, Object> map : pageMap.getList()) {
			BigDecimal price = (BigDecimal) map.get("price");
			if (price != null) {
				map.put("price", MoneyNumberFormat.getThousandsByRegex(price.toString(), 2));
			}
		}
		return pageMap;
	}

	@Override
	public void batchApprove(String reviewId, String comment) throws PaymentException{
		Long currentUserId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		if(null == reviewId){
			LOG.error("无此对应审批流程，reviewId:" + reviewId);
			throw new PaymentException("无此对应审批流程，reviewId:" + reviewId);
		}
		ReviewResult result = reviewService.approve(String.valueOf(currentUserId), companyUuid,
				null, permissionChecker, reviewId, comment, null);
		//审批成功，并且是最后一步审批。如果是审批中则不进行任何处理
		if(result.getSuccess() && result.getReviewStatus()
				== ReviewConstant.REVIEW_STATUS_PASSED){
			StringBuffer str = new StringBuffer();
			str.append("update cost_record set payReview = ?, payUpdateBy= ?, ")
			   .append(" payUpdateDate = ? ,is_new = ? where pay_review_uuid = ? ");
			Date currentDate = new Date();
			costRecordDao.updateBySql(str.toString(), ReviewConstant.REVIEW_STATUS_PASSED, 
					currentUserId, currentDate, 2, reviewId);
		}
		ReviewNew reviewNew = reviewNewDao.findById(reviewId);
		if(reviewNew != null) {
			if(reviewNew.getStatus() == ReviewConstant.REVIEW_STATUS_PASSED) {
				String sql = "UPDATE cost_record set payReview = ? where pay_review_uuid = ?";
				costRecordDao.updateBySql(sql, ReviewConstant.REVIEW_STATUS_PASSED, reviewId);
			}
		}
		if(!result.getSuccess()){
			String message = ReviewErrorCode.ERROR_CODE_MSG_CN.get(result.getCode());
			if(StringUtils.isBlank(message)){
				message = "系统异常，请检查";
			}
			LOG.error("该条记录审批通过失败，reviewId:" + reviewId + ",原因如下：" + message);
			throw new PaymentException("该条记录审批通过失败，reviewId:" + reviewId + ",原因如下：" + message);
		}
	}

	@Override
	public void batchReject(String reviewId, String comment) throws PaymentException{
		Long currentUserId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		if(null == reviewId){
			LOG.error("无此对应审批流程，reviewId:" + reviewId);
			throw new PaymentException("无此对应审批流程，reviewId:" + reviewId);
		}
		ReviewNew reviewNew = reviewService.getReview(reviewId);
		//审批通过的禁止驳回，bug : 14931
		if(ReviewConstant.REVIEW_STATUS_PASSED == reviewNew.getStatus()){
			return;
		}
		ReviewResult result = reviewService.reject(String.valueOf(currentUserId), companyUuid, null, reviewId, comment, null);
		if(result.getSuccess() && result.getReviewStatus()
				== ReviewConstant.REVIEW_STATUS_REJECTED){
			StringBuffer str = new StringBuffer();
			str.append("update cost_record set payReview = ?, payUpdateBy= ?, ")
			   .append(" payUpdateDate = ? where pay_review_uuid = ? ");
			Date currentDate = new Date();
			costRecordDao.updateBySql(str.toString(), ReviewConstant.REVIEW_STATUS_REJECTED, 
					currentUserId, currentDate, reviewId);
		}
		if(!result.getSuccess()){
			String message = ReviewErrorCode.ERROR_CODE_MSG_CN.get(result.getCode());
			if(StringUtils.isBlank(message)){
				message = "系统异常，请检查";
			}
			LOG.error("该条记录驳回失败，reviewId:" + reviewId + ",原因如下：" + message);
			throw new PaymentException("该条记录驳回失败，reviewId:" + reviewId + ",原因如下：" + message);
		}
	}

	@Override
	public void backReview(String reviewId, String comment) throws PaymentException{
		Long currentUserId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		if(null == reviewId){
			LOG.error("无此对应审批流程，reviewId:" + reviewId);
			throw new PaymentException("无此对应审批流程，reviewId:" + reviewId);
		}
		//审批通过的禁止撤销，bug:14931
		ReviewNew reviewNew = reviewService.getReview(reviewId);
		if (ReviewConstant.REVIEW_STATUS_PASSED == reviewNew.getStatus()){
			return;
		}
		ReviewResult result = reviewService.back(String.valueOf(currentUserId), companyUuid, null, reviewId, comment, null);
		if(result.getSuccess()){
			StringBuffer str = new StringBuffer();
			str.append("update cost_record set payReview = ?, payUpdateBy= ?, ")
			   .append(" payUpdateDate = ? where pay_review_uuid = ? ");
			Date currentDate = new Date();
			costRecordDao.updateBySql(str.toString(), "1", currentUserId, currentDate, reviewId);
		}
		if(!result.getSuccess()){
			String message = ReviewErrorCode.ERROR_CODE_MSG_CN.get(result.getCode());
			if(StringUtils.isBlank(message)){
				message = "系统异常，请检查";
			}
			LOG.error("该条记录撤销失败，reviewId:" + reviewId + ",原因如下：" + message);
			throw new PaymentException("该条记录撤销失败，reviewId:" + reviewId + ",原因如下：" + message);
		}
	}

	@Override
	public void cancelReview(String reviewId, String comment) throws PaymentException{
		Long currentUserId = UserUtils.getUser().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		if(null == reviewId){
			LOG.error("无此对应审批流程，reviewId:" + reviewId);
			throw new PaymentException("无此对应审批流程，reviewId:" + reviewId);
		}
		ReviewNew reviewNew = reviewService.getReview(reviewId);
		if(null == reviewNew){
			LOG.error("无此审批流程，请检查，reviewId:" + reviewId);
			throw new PaymentException("无此审批流程，请检查，reviewId:" + reviewId);
		}
		Integer status = reviewNew.getStatus();
		//只有审批中的数据可以进行取消
		if(status != ReviewConstant.REVIEW_STATUS_PROCESSING){
			LOG.error("该审批是审批中的状态，所以不能取消");
			throw new PaymentException("该审批是审批中的状态，所以不能取消");
		}
		ReviewResult result = reviewService.cancel(String.valueOf(currentUserId), companyUuid, null, reviewId, comment, null);
		if(result.getSuccess() && result.getReviewStatus()
				== ReviewConstant.REVIEW_OPERATION_CANCEL){
			StringBuffer str = new StringBuffer();
			str.append("update cost_record set payReview = ?, payUpdateBy= ?, ")
			   .append(" payUpdateDate = ? where pay_review_uuid = ? ");
			Date currentDate = new Date();
			costRecordDao.updateBySql(str.toString(), "5", currentUserId, currentDate, reviewId);
		}
		if(!result.getSuccess()){
			String message = ReviewErrorCode.ERROR_CODE_MSG_CN.get(result.getCode());
			if(StringUtils.isBlank(message)){
				message = "系统异常，请检查";
			}
			LOG.error("该条记录取消失败，reviewId:" + reviewId + ",原因如下：" + message);
			throw new PaymentException("该条记录取消失败，reviewId:" + reviewId + ",原因如下：" + message);
		}
	}

}
