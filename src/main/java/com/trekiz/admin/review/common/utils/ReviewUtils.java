package com.trekiz.admin.review.common.utils;

import java.util.*;

import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.review.configuration.config.ReviewContext;
import com.trekiz.admin.review.rebates.singleGroup.service.RebatesNewService;
import org.springframework.util.Assert;

import com.quauq.review.core.engine.ReviewLogService;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.entity.ReviewLogNew;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 *
 * 新审批模块，审批公共方法
 * @author shijun.liu
 * @date 2015年11月18日
 */
public class ReviewUtils {

	private static ReviewLogService reviewLogService=SpringContextHolder.getBean(ReviewLogService.class);
	private static ReviewService reviewService = SpringContextHolder.getBean(ReviewService.class);
	private static MoneyAmountService moneyAmountService = SpringContextHolder.getBean(MoneyAmountService.class);
	private static RebatesNewService rebatesNewService = SpringContextHolder.getBean(RebatesNewService.class);
	
	/**
	 * 审批状态转换
	 * @param reviewStatus	审批状态标识
	 * @param userIds		review_new 当前审批人(current_reviewer)
	 * @return
	 * @author shijun.liu
	 * @date  2015.11.18
	 */
	public static String getChineseReviewStatus(Integer reviewStatus, String userIds){
		String status = "";
		String nextReviewers = "";
		if(null == reviewStatus){
			return status;
		}
		
		if(StringUtils.isNotBlank(userIds)){
			nextReviewers = UserUtils.getUserNameByIds(userIds);
		}
		switch (reviewStatus) {
	    	case 0 : 
	    		status = ReviewConstant.REVIEW_STATUS_REJECTED_DES;
	    		break;
	    	case 1 : 
	    		if(StringUtils.isBlank(nextReviewers)){
	    			status = "未分配审批人";
	    		}else{
	    			status = "待 " + nextReviewers + " 审批";
	    		}
	    		break;
	    	case 2 : 
	    		status = ReviewConstant.REVIEW_STATUS_PASSED_DES;
	    		break;
	    	case 3 : 
	    		status = ReviewConstant.REVIEW_STATUS_CANCELED_DES;
	    		break;
	    	default : 
	    		status = "["+reviewStatus+"]无对应审批状态";
	    }
		return status;
	}
	
	/**
	 * 审批状态转换
	 * @param reviewUuid	新审批review_new id字段值
	 * @return
	 * @author shijun.liu
	 * @date  2015.12.03
	 */
	public static String getChineseReviewStatusByUuid(String reviewUuid){
		if(null == reviewUuid){
			return "";
		}
		ReviewNew reviewNew = reviewService.getReview(reviewUuid);
		if(null == reviewNew){
			return "";
		}
		return getChineseReviewStatus(reviewNew.getStatus(), reviewNew.getCurrentReviewer());
	}
	
	/**
	 * 判断当前用户是否有撤销的权限
	 * @param reviewId
	 * @return
	 */
	public static boolean isBackReview(String reviewId){
		
		List<ReviewLogNew> reviewLogList = reviewLogService.findReviewLogByReviewIdActiveDesc(reviewId, ReviewConstant.REVIEW_OPERATION_PASS);
		if(reviewLogList == null || reviewLogList.size() == Context.NumberDef.NUMER_ZERO){
			return false;
		}
		/*获取上一环节审核通过的人*/
		String createBy = reviewLogList.get(Context.NumberDef.NUMER_ZERO).getCreateBy();
		if(StringUtils.isBlank(createBy)){
			return false;
		}
		ReviewNew reviewNew = reviewService.getReview(reviewId);
		if(reviewNew == null){
			return false;
		}
		/*当前登录用户如果等于上一环节审核通过的人 则证明可以撤销 新增 如果审核流程已经是已通过状态 则不能撤销*/
		if(UserUtils.getUser().getId().toString().equals(createBy) && ReviewConstant.REVIEW_STATUS_PROCESSING == reviewNew.getStatus()){
			return true;
		}
		return false;
	}
	
	/**
	 * 查询 当前用户是否有审批权限
	 * @param object 当前审核的待审核人字符串
	 * @return
	 */
	public static boolean isCurReviewer(Object object) {
		if(object == null || StringUtils.isBlank(object.toString())){
			return false;
		}
		String[] reviewers = object.toString().split(",");
		if(reviewers == null || reviewers.length == 0){
			return false;
		}
		String loginUserId = UserUtils.getUser().getId().toString();
		for(String reviewer : reviewers){
			if(StringUtils.isBlank(reviewer)){
				continue;
			}
			if(loginUserId.trim().equals(reviewer.trim())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 当前登录用户是否，是否是当前审批人
	 * @param reviewUuid
	 * @author shijun.liu
	 */
	public static boolean isCurrentReviewer(String reviewUuid){
		ReviewNew reviewNew = getReviewNewByUuid(reviewUuid);
		if(null == reviewNew){
			return false;
		}
		return isCurReviewer(reviewNew.getCurrentReviewer()) && 
				reviewNew.getStatus() == ReviewConstant.REVIEW_STATUS_PROCESSING;
	}
	
	/**
	 * 根据review_new 表的uuid查询审批对象
	 * @param reviewUuid			新审批表的UUID值
	 * @return
	 * @author shijun.liu
	 */
	public static ReviewNew getReviewNewByUuid(String reviewUuid){
		if(StringUtils.isBlank(reviewUuid)){
			return new ReviewNew();
		}
		ReviewNew reviewNew = reviewService.getReview(reviewUuid);
		if(null == reviewNew){
			return new ReviewNew();
		}
		return reviewNew;
	}

	/**
	 * 根据新审批的reviewId查询审批日志
	 * @author zhenxing.yan
	 * @param reviewId
	 * @return
     */
	public static List<ReviewLogNew> obtainReviewLogs(String reviewId){
		Assert.hasText(reviewId,"reviewId should not be empty!");
		return reviewService.getReviewLogByReviewId(reviewId);
	}

	/**
	 * 根据新审批的reviewId查询审批日志,如果审批日志中的备注不为空就返回该List，否则返回null。
	 * @author yudong.xu 2016.12.5
	 * @param reviewId
	 * @return
	 */
	public static List<ReviewLogNew> getHavingRemarkReviewLogs(String reviewId){
		Assert.hasText(reviewId,"reviewId should not be empty!");
		List<ReviewLogNew> reviewLogs = reviewService.getReviewLogByReviewId(reviewId);
		for (ReviewLogNew reviewLog : reviewLogs) {
			if (StringUtils.isNotBlank(reviewLog.getRemark())){
				return reviewLogs;
			}
		}
		return null;
	}
	
	/**
	 * 新审批根据reviewUuid查询多币种情况下带币种符号的金额。例如：$200.00,     $200.00 + ¥300.00
	 * @param reviewUuid
	 * @param type          0 表示币种符号，1表示币种名称
	 * @author shijun.liu
	 * @return
	 */
	public static String getMoneyByReviewUuid(String reviewUuid, String type){
		if(StringUtils.isNotBlank(reviewUuid)){
			ReviewNew reviewNew = reviewService.getReview(reviewUuid);
			if(reviewNew == null){
				return null;
			}
			if(String.valueOf(Context.ORDER_TYPE_HOTEL).equals(reviewNew.getProductType())) {//酒店
				List<Map<String, String>> list = moneyAmountService.getMoneyHotelByReviewUuid(reviewUuid);
				if("0".equals(type)){
					return list.get(0).get("mark_money");
				}else if("1".equals(type)){
					return list.get(0).get("name_money");
				}
			} else if(String.valueOf(Context.ORDER_TYPE_ISLAND).equals(reviewNew.getProductType())) {//海岛游
				List<Map<String, String>> list = moneyAmountService.getMoneyIslandByReviewUuid(reviewUuid);
				if("0".equals(type)){
					return list.get(0).get("mark_money");
				}else if("1".equals(type)){
					return list.get(0).get("name_money");
				}
			} else {//其它
				List<Map<String, String>> list = moneyAmountService.getMoneyByReviewUuid(reviewUuid);
				if("0".equals(type)){
					return list.get(0).get("mark_money");
				}else if("1".equals(type)){
					return list.get(0).get("name_money");
				}
			}
		}
		return null;
	}

	/**
	 * 获取当前用户待审批记录的数量
	 * @author zhenxing.yan
	 * @return Map<String,String> key ：流程类型，value：待审记录数量
     */
	public static Map<String,String> getAllReviewCount(){
		/**
		 * TODO 有风险，如果不是http请求线程尽量不要使用此方法
		 */
		Map<String,String> reviewCountMap=new HashMap<>();
		String userId=UserUtils.getUser().getId().toString();
		String companyId=UserUtils.getUser().getCompany().getUuid();
		Set<String> processTypes4BatchNo=new HashSet<String>(){
			{
				add(Context.REVIEW_FLOWTYPE_VISA_RETURNRECEIPT.toString());
				add(Context.REVIEW_FLOWTYPE_VISA_BORROWMONEY.toString());
			}
		};
		Map<String,Long> reviewCount=reviewService.getReviewCount(companyId,userId,processTypes4BatchNo);
		if (reviewCount!=null&&reviewCount.size()>0){
			for(String key:reviewCount.keySet()){
				reviewCountMap.put(key,reviewCount.get(key).toString());
			}
		}
		//用值为0补充没有的流程记录
		for (Integer key: ReviewContext.reviewFlowTypeMap.keySet()){
			if (!reviewCountMap.containsKey(key.toString())){
				reviewCountMap.put(key.toString(),"0");
			}
		}

		return reviewCountMap;
	}
	
	/**
	 * 新审批是否显示取消按钮，如果当前登录人是审批发起人，并且审批状态在处理中则显示，否则不显示
	 * @param reviewUuid         新审批uuid
	 * @return
	 * @author shijun.liu
	 * @date 2015.12.15
	 */
	public static boolean isShowCancel(String reviewUuid){
		if(StringUtils.isBlank(reviewUuid)){
			return false;
		}
		Long currentUserId = UserUtils.getUser().getId();
		ReviewNew reviewNew = reviewService.getReview(reviewUuid);
		String createBy = reviewNew.getCreateBy();
		Integer status = reviewNew.getStatus();
		return createBy.equals(currentUserId.toString()) && status == ReviewConstant.REVIEW_STATUS_PROCESSING;
	}

	public static List<Map<String, Object>> getPayedMoney(String reviewUuid) {
		return rebatesNewService.getPayedMoney(reviewUuid);
	}

	public static String getCurrencyId(String reviewUuid) {
		String currencyId = "";
		List<Map<String, Object>> list = rebatesNewService.getPayedMoney(reviewUuid);
		for(Map<String, Object> map : list) {
			currencyId += map.get("currencyId") + ",";
		}
		return currencyId;
	}

	public static String getMoney(String reviewUuid) {
		String money = "";
		List<Map<String, Object>> list = rebatesNewService.getPayedMoney(reviewUuid);
		for(Map<String, Object> map : list) {
			money += map.get("rebatesDiff") + ",";
		}
		return money;
	}
	
	public static void printJSON(String json, HttpServletResponse response)
			throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(json);
	}
}
