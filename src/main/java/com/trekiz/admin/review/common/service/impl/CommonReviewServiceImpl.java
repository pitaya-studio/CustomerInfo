package com.trekiz.admin.review.common.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.config.ReviewErrorCode;
import com.quauq.review.core.engine.entity.ReviewLogNew;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.ServiceException;
import com.trekiz.admin.common.utils.Collections3;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.repository.ActivityGroupDao;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.island.dao.ActivityIslandGroupDao;
import com.trekiz.admin.modules.island.dao.IslandOrderDao;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroup;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;
import com.trekiz.admin.modules.visa.repository.VisaProductDao;
import com.trekiz.admin.review.common.bean.CostPaymentReviewNewLog;
import com.trekiz.admin.review.common.exception.CommonReviewException;
import com.trekiz.admin.review.common.service.ICommonReviewService;

/**
 * 
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 *
 * 新审批公共Service接口实现类
 * @author shijun.liu
 * @date 2015年11月30日
 */
@Service
public class CommonReviewServiceImpl implements ICommonReviewService {

	private static final Logger log = Logger.getLogger(CommonReviewServiceImpl.class);
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ActivityGroupDao activityGroupDao;
	@Autowired
	private ActivityAirTicketDao activityAirTicketDao;
	@Autowired
	private VisaOrderDao visaOrderDao;
	@Autowired
	private VisaProductDao visaProductDao;
	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	@Autowired
	private ProductOrderCommonDao orderDao;
	@Autowired
	private IslandOrderDao islandOrderDao;
	@Autowired
	private ActivityIslandGroupDao activityIslandGroupDao;
	@Autowired
	private CostRecordDao costRecordDao;
	@Autowired
	private ReviewDao reviewOldDao;
	
	@Override
	public void confimOrCancelPay(String reviewId, String status,Date payConfirmDate) {
		if(StringUtils.isBlank(reviewId)){
			log.error("审批ID不能为空");
			throw new CommonReviewException("审批ID不能为空");
		}
		if(StringUtils.isBlank(status)){
			log.error("付款状态不能为空");
			throw new CommonReviewException("付款状态不能为空");
		}
		Long userId = UserUtils.getUser().getId();
		ReviewResult result = reviewService.updatePayStatus(userId + "", status, reviewId,payConfirmDate);
		if(!result.getSuccess()){
			log.error("操作失败：" + result.getMessage());
			throw new CommonReviewException("操作失败：" + result.getMessage());
		}
	}
	
	
	@Override
	public void confimOrCancelInvoice(String reviewId, String status) {
		if(StringUtils.isBlank(reviewId)){
			log.error("审批ID不能为空");
			throw new CommonReviewException("审批ID不能为空");
		}
		if(StringUtils.isBlank(status)){
			log.error("付款状态不能为空");
			throw new CommonReviewException("付款状态不能为空");
		}
		Long userId = UserUtils.getUser().getId();
		ReviewResult result = updateInvoiceStatus(userId + "", status, reviewId);
		if(!result.getSuccess()){
			log.error("操作失败：" + result.getMessage());
			throw new CommonReviewException("操作失败：" + result.getMessage());
		}
	}
	
	@Override
	public void confimOrCancelInvoiceAll(String reviewId) {
		if(StringUtils.isBlank(reviewId)){
			log.error("审批ID不能为空");
			throw new CommonReviewException("审批ID不能为空");
		}
		Long userId = UserUtils.getUser().getId();
		ReviewResult result = updateInvoiceStatusAll(userId + "", reviewId);
		if(!result.getSuccess()){
			log.error("操作失败：" + result.getMessage());
			throw new CommonReviewException("操作失败：" + result.getMessage());
		}
	}
	
	
	private ReviewResult updateInvoiceStatusAll(String userId, String reviewId) {

		ReviewResult result = new ReviewResult();
		ReviewNew review_new = reviewService.getReview(reviewId);
		boolean isNum = reviewId.matches("[0-9]+");
		List<Review> reviewList = null;
		if(isNum){
			reviewList = reviewOldDao.findReview(Long.parseLong(reviewId));
		}
		// 判断改变的状态是那张表
		if (review_new == null && reviewList == null) {
			result.setCode(ReviewErrorCode.ERROR_CODE_REVIEW_NOT_FOUND);
			result.setReviewId(reviewId);
			result.setMessage("review or review_new not found! reviewId: "+reviewId);
			result.setSuccess(false);
			return result;
		}
		if(review_new == null){
			Review review = reviewList.get(0);
			// 更新发票状态   status 0:未收发票   1：已收发票
			review.setinvoice_status(1);
			review.setUpdateBy(Long.parseLong(userId));
			Date currentDate=new Date();
			// 设置更新时间
			review.setUpdateDate(currentDate);
			// 更新  review
			reviewOldDao.updateObj(review);
		}else{
			review_new.setInvoiceStatus(1);
			review_new.setUpdateBy(userId);
			Date currentDate=new Date();
			// 设置更新时间
			review_new.setUpdateDate(currentDate);
			reviewService.saveReviewNew(review_new);
		}
		
		// 组织数据返回
		result.setCode(ReviewErrorCode.ERROR_CODE_SUCCESS);
		result.setReviewId(reviewId);
		result.setSuccess(true);
		return result;
	
	}


	private ReviewResult updateInvoiceStatus(String userId, String status,
			String reviewId) {
		ReviewResult result = new ReviewResult();
		ReviewNew review_new = reviewService.getReview(reviewId);
		// 判断改变的状态是那张表
		if(review_new != null){
			// 更新发票状态   status 0:未收发票   1：已收发票
			if("0".equals(status)){
				review_new.setInvoiceStatus(1);
			}else{
				review_new.setInvoiceStatus(0);
			}
			// 设置更新人
			review_new.setUpdateBy(userId);
			Date currentDate=new Date();
			// 设置更新时间
			review_new.setUpdateDate(currentDate);
			// 更新  review_new
			reviewService.saveReviewNew(review_new);
		}else{
			List<Review> reviewList = reviewOldDao.findReview(Long.parseLong(reviewId));
			Review review = reviewList.get(0);
			if("0".equals(status)){
				review.setinvoice_status(1);
			}else{
				review.setinvoice_status(0);
			}
			review.setUpdateBy(Long.parseLong(userId));
			Date currentDate=new Date();
			review.setUpdateDate(currentDate);
			// 更新  review
			reviewOldDao.updateObj(review);
		}
		
		
		// 组织数据返回
		result.setCode(ReviewErrorCode.ERROR_CODE_SUCCESS);
		result.setReviewId(reviewId);
		result.setSuccess(true);
		return result;
	}


	/**
	 * 
	 * @return
	 */
	public boolean checkApplyStart(String orderId,Integer productType,Integer flowType){
		boolean result = true;
		//判断结算单锁定状态 如果锁定 不能继续审核 add by chy 2015年6月8日17:21:56
		if(productType == 7) {//机票
			AirticketOrder airticketOrder = airticketOrderDao.getAirticketOrderById(Long.parseLong(orderId));
			ActivityAirTicket airTicket = activityAirTicketDao.findOne(airticketOrder.getAirticketId());
			if(airTicket.getLockStatus() != null && 1 == airTicket.getLockStatus()){
				result=false;
			}
		} else if(productType == 6) {//签证
			if (flowType != 4 && flowType != 5) { // 批量操作：借款和还收据不判断结算单（蔡）
				VisaOrder visaOrder = visaOrderDao.findByOrderId(Long.parseLong(orderId));
				VisaProducts visaProducts = visaProductDao.findOne(visaOrder.getVisaProductId());
				if(visaProducts.getLockStatus() != null && 1 == visaProducts.getLockStatus()){
					result=false;
				}
			}
		} else if(productType == 11) {//酒店
			
		} else if(productType == 12) {//海岛游
			Integer id = Integer.valueOf(orderId);
			IslandOrder order = islandOrderDao.getById(id);
			ActivityIslandGroup activityGroup = activityIslandGroupDao.getByUuid(order.getActivityIslandGroupUuid());
			if(activityGroup.getLockStatus() != null && 1 == activityGroup.getLockStatus()){
				result=false;
			}
		} else {//参团 等、、、
			ProductOrderCommon order = orderDao.findOne(Long.parseLong(orderId));
			ActivityGroup activityGroup = activityGroupDao.findOne(order.getProductGroupId());
			if(activityGroup.getLockStatus() != null && 1 == activityGroup.getLockStatus()){
				result=false;
			}
		}
		return result;
	}
	
	/**
	 * 返佣或退款审核通过或驳回或取消时对成本操作
	 * @author yakun.bai
	 * @Date 2015-12-12
	 */
	@Transactional(readOnly = false, rollbackFor = { ServiceException.class })
	public void updateCostRecordStatus(ReviewNew review, Integer result) {
		
		String reviewStatus = "";
		if (result.intValue() == 2) {//审核通过时
			reviewStatus = "审批通过";
		} else if (result.intValue() == 3) {
			reviewStatus = "已取消";
		} else if(result.intValue() == 0) {//驳回时
			reviewStatus = "已驳回";
		} else {//待审核时
			reviewStatus = getReviewerDesc(review.getCurrentReviewer());
		}

		String orderId = review.getOrderId();
		Integer orderType = Integer.parseInt(review.getProductType());
		
//		boolean yubao_locked = false; //预报单是否锁定标识
		boolean jiesuan_locked = false; //结算单是否锁定标识
		
		if (StringUtils.isNotBlank(orderId) && orderType != null) {
			//机票
			if (Context.ORDER_TYPE_JP == orderType) {
				AirticketOrder airOrder = airticketOrderDao.getAirticketOrderById(Long.parseLong(orderId));
				ActivityAirTicket activityAirTicket = activityAirTicketDao.findOne(airOrder.getAirticketId());
				
				//对预报单状态进行判断
				if ("10".equals(activityAirTicket.getForcastStatus())) {
//					yubao_locked = true;
				}
				//对结算单状态进行判断
				if (1 == activityAirTicket.getLockStatus()) {
					jiesuan_locked = true;
				}
				
			} 
			//签证
			else if(Context.ORDER_TYPE_QZ == orderType) {
				VisaOrder visaOrder = visaOrderDao.findOne(Long.parseLong(orderId));
				VisaProducts visaProducts = visaProductDao.findOne(visaOrder.getVisaProductId());
				//对预报单状态进行判断
				if ("10".equals(visaProducts.getForcastStatus())) {
//					yubao_locked = true;
				}
				//对结算单状态进行判断
				if (1 == visaProducts.getLockStatus()) {
					jiesuan_locked = true;
				}
			} 
			//单团
			else {
				ProductOrderCommon order = orderDao.findOne(Long.parseLong(orderId));
				ActivityGroup group = activityGroupDao.findOne(order.getProductGroupId());
				//对预报单状态进行判断
				if ("10".equals(group.getForcastStatus())) {
//					yubao_locked = true;
				}
				//对结算单状态进行判断
				if (1 == group.getLockStatus()) {
					jiesuan_locked = true;
				}
			}
		}
		//如果取消或驳回申请为退款或返佣，则更新costrecord表对应记录的状态
//		if (!yubao_locked) {
			List<CostRecord> costRecordList = costRecordDao.findCostRecordList(review.getId(), 0);
			if (CollectionUtils.isNotEmpty(costRecordList)) {
				for (CostRecord record : costRecordList) {
					if ("审批通过".equals(reviewStatus)) {
						record.setReview(2);
					}
					record.setReviewStatus(reviewStatus);
					costRecordDao.updateCostRecord(record.getId(), record.getReviewStatus(), record.getReview());
				}
			}
//		}
		
		if (!jiesuan_locked) {
			List<CostRecord> costList = costRecordDao.findCostRecordList(review.getId(), 1);
			if (CollectionUtils.isNotEmpty(costList)) {
				for (CostRecord record : costList) {
					if ("审批通过".equals(reviewStatus)) {
						record.setReview(2);
//						record.setReviewStatus("待提交");
//						costRecordDao.updateCostRecord(record.getId(), record.getReviewStatus(), record.getReview());
//						continue;
					}
					record.setReviewStatus(reviewStatus);
					costRecordDao.updateCostRecord(record.getId(), record.getReviewStatus(), record.getReview());
				}
			}
		}
	}
	
	/**
	 * 获取当前审核人描述 由id转化为name
	 * @param cReviewer
	 * @return
	 */
	private String getReviewerDesc(String reviewers) {
		String[] reviewArr = reviewers.split(",");
		String result = "";
		int n = 0;
		String tName = "";
		for(String temp : reviewArr){
			if(StringUtils.isBlank(temp)){
				continue;
			}
			tName = UserUtils.getUserNameById(Long.parseLong(temp));
			if(n == 0){
				result += tName;
			} else {
				result += "," + tName;
			}
		}
		if(!"".equals(result)){//新加待XXX审批 by chy 2015-12-18 18:26:39
			result = "待" + result + "审批";
		}
		return result;
	}

	@Override
	public List<CostPaymentReviewNewLog> getBudgetReviewLog(Long activityId, Integer orderType, Long costId) throws CommonReviewException {
		if(null == activityId || null == orderType){
			throw new CommonReviewException("产品团期ID,订单类型不能为空");
		}
		List<CostRecord> list = costRecordDao.getNewReviewCostRecord(activityId, orderType, 0);
		//此需求只针对起航假期，需求号是 0425
		stickCostRecord(list, costId);
		return getCostReviewNewLog(list);
	}

	@Override
	public List<CostPaymentReviewNewLog> getActualReviewLog(Long activityId, Integer orderType, Long costId) throws CommonReviewException {
		if(null == activityId || null == orderType){
			throw new CommonReviewException("产品团期ID,订单类型不能为空");
		}
		List<CostRecord> list = costRecordDao.getNewReviewCostRecord(activityId, orderType, 1);
		//此需求只针对起航假期，需求号是 0425
		stickCostRecord(list, costId);
		return getCostReviewNewLog(list);
	}

	@Override
	public List<CostPaymentReviewNewLog> getPaymentReviewLog(Long activityId, Integer orderType, Long costId) throws CommonReviewException {
		if(null == activityId || null == orderType){
			throw new CommonReviewException("产品团期ID,订单类型不能为空");
		}
		List<CostRecord> list = costRecordDao.getNewReviewCostRecord(activityId, orderType, 1);
		//此需求只针对起航假期，需求号是 0425
		stickCostRecord(list, costId);
		return getPaymentReviewNewLog(list);
	}

	/**
	 * 将某项成本项置顶显示
	 * @param list
	 * @param costId
	 * @author shijun.liu
	 * @date   2016.05.31
	 */
	private void stickCostRecord(List<CostRecord> list, Long costId){
		//此需求只针对起航假期，需求号是 0425
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		if(Context.SUPPLIER_UUID_QHJQ.equals(companyUuid)){
			if(Collections3.isEmpty(list)){
				return;
			}
			if(null == costId){
				return;
			}
			for (int i = 0; i<list.size(); i++){
				CostRecord target = list.get(i);
				if(target.getId().longValue() == costId.longValue()){
					//将匹配的对象设置为第一个，并将原来的第一个元素返回
					CostRecord oldCostRecord0 = list.set(0, target);
					//将原来的元素设置为第i的位置，匹配元素设置为第0个位置
					list.set(i, oldCostRecord0);
				}
			}
		}
	}
	
	/**
	 * 根据成本记录查询其新审批日志
	 * @param costs				成本记录
	 * @return
	 * @author shijun.liu
	 * @date 2015.12.17
	 */
	private List<CostPaymentReviewNewLog> getCostReviewNewLog(List<CostRecord> costs){
		List<CostPaymentReviewNewLog> list = new ArrayList<CostPaymentReviewNewLog>();
		if(null == costs || costs.size() == 0){
			return list;
		}
		for(CostRecord costRecord:costs){
			//新成本审批UUID
			String reviewUuid = costRecord.getReviewUuid();
			if(StringUtils.isBlank(reviewUuid)){
				continue;
			}
			CostPaymentReviewNewLog paymentLog = new CostPaymentReviewNewLog();
			List<ReviewLogNew> logs = reviewService.getReviewLogByReviewId(reviewUuid);
			//无审批日志时不显示
			if(null == logs || logs.size() == 0){
				continue;
			}
			paymentLog.setDeleteFlag(costRecord.getDelFlag());
			paymentLog.setCostName(costRecord.getName());
			paymentLog.setLogs(logs);
			list.add(paymentLog);
		}
		return list;
	}
	
	/**
	 * 根据成本付款记录查询其新审批日志
	 * @param costs				成本付款记录
	 * @return
	 * @author shijun.liu
	 * @date 2015.12.17
	 */
	private List<CostPaymentReviewNewLog> getPaymentReviewNewLog(List<CostRecord> costs){
		List<CostPaymentReviewNewLog> list = new ArrayList<CostPaymentReviewNewLog>();
		if(null == costs || costs.size() == 0){
			return list;
		}
		for(CostRecord costRecord:costs){
			//新成本付款审批UUID
			String payReviewUuid = costRecord.getPayReviewUuid();
			if(StringUtils.isBlank(payReviewUuid)){
				continue;
			}
			CostPaymentReviewNewLog paymentLog = new CostPaymentReviewNewLog();
			List<ReviewLogNew> logs = reviewService.getReviewLogByReviewId(payReviewUuid);
			//无审批日志时不显示
			if(null == logs || logs.size() == 0){
				continue;
			}
			paymentLog.setDeleteFlag(costRecord.getDelFlag());
			paymentLog.setCostName(costRecord.getName());
			paymentLog.setLogs(logs);
			list.add(paymentLog);
		}
		return list;
	}
}
