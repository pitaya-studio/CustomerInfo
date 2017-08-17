package com.trekiz.admin.review.payment.airticket.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.config.ReviewErrorCode;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.service.ActivityAirTicketServiceImpl;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.payment.airticket.service.IAirticketPaymentReviewService;
import com.trekiz.admin.review.payment.comment.exception.PaymentException;
import com.trekiz.admin.review.payment.comment.utils.PayMentUtils;

/**
 * 机票产品付款申请的Service实现类
 * @author shijun.liu
 *
 */
@Service
public class AirticketPaymentReviewServiceImpl implements
		IAirticketPaymentReviewService {

	private static final Logger log = Logger.getLogger(AirticketPaymentReviewServiceImpl.class);
	
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private CostRecordDao costRecordDao;
	@Autowired
	private ActivityAirTicketServiceImpl activityAirTicketService; 
	@Autowired
	private UserReviewPermissionChecker permissionChecker;
	
	@Override
	public void apply(String item) throws PaymentException{
		String currentUserId = String.valueOf(UserUtils.getUser().getId());
		String uuid = null;
		Map<String, Object> variables = new HashMap<String, Object>();
		if(StringUtils.isBlank(item)){
			log.error("成本ID不能为空");
			throw new PaymentException("成本ID不能为空");
		}
		CostRecord costRecord = costRecordDao.findOne(Long.valueOf(item));
		if(null == costRecord){
			log.error("[" + item + "]对应的成本记录不存在，请检查");
			throw new PaymentException("[" + item + "]对应的成本记录不存在，请检查");
		}
		//查询机票产品数据信息
		ActivityAirTicket activityAirTicket = activityAirTicketService.findById(costRecord.getActivityId());
		Long deptId = activityAirTicket.getDeptId();//部门ID，取值：产品表的部门ID
		Long operatorId = activityAirTicket.getCreateBy().getId();//计调
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT, costRecord.getSupplyId());//渠道
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, operatorId);//计调
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR_NAME, UserUtils.getUser(operatorId).getName());//计调名称
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, activityAirTicket.getGroupCode());//团号
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID, activityAirTicket.getId());//产品ID
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, activityAirTicket.getActivityName());//产品名称
		if(StringUtils.isBlank(costRecord.getUuid())){
			uuid = UuidUtils.generUuid();
		}
		//标识特定业务的key，此处指 --> 付款审核
		String businessKey = PayMentUtils.generateBusinessKey(costRecord.getOrderType(), uuid);
		//发送流程申请
		ReviewResult result = reviewService.start(currentUserId, UserUtils.getUser().getCompany().getUuid(), permissionChecker, businessKey, 
				costRecord.getOrderType(), Context.REVIEW_FLOWTYPE_PAYMENT, deptId, null, variables);
		if(!result.getSuccess()){
			String message = ReviewErrorCode.ERROR_CODE_MSG_CN.get(result.getCode());
			if(StringUtils.isBlank(message)){
				message = "系统异常，请检查";
			}
			throw new PaymentException(message);
		}
		if(result.getSuccess()){
			Date currentDate = new Date();
			costRecord.setPayReviewUuid(result.getReviewId());
			if(ReviewConstant.REVIEW_STATUS_PASSED == result.getReviewStatus()){
				costRecord.setPayReview(ReviewConstant.REVIEW_STATUS_PASSED);
			}else{
				costRecord.setPayReview(1);
			}
			costRecord.setPayApplyDate(currentDate);
			costRecord.setPayUpdateBy(Integer.parseInt(currentUserId));
			costRecord.setPayUpdateDate(currentDate);
			costRecordDao.save(costRecord);
		}
	}

}
