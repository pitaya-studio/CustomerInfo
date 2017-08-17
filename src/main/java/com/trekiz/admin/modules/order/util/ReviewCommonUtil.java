package com.trekiz.admin.modules.order.util;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.cost.service.PayManagerService;
import com.trekiz.admin.modules.finance.service.IServiceChargeService;
import com.trekiz.admin.modules.finance.service.impl.ServiceChargeServiceImpl;
import com.trekiz.admin.modules.invoice.service.OrderinvoiceService;
import com.trekiz.admin.modules.order.service.OrderReviewService;
import com.trekiz.admin.modules.receipt.service.OrderReceiptService;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.review.visaborrowmoney.service.VisaBorrowMoneyServiceImpl;
import com.trekiz.admin.modules.review.visareturnreceipt.service.VisaReturnReceiptServiceImpl;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.UserJob;

/**
 * 
 *  文件名: ReviewCommonUtil.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xiaoyang.tao
 *  @DateTime 2014-12-23 上午10:57:11 
 *  @version 1.0
 */
public class ReviewCommonUtil {
	
	private static ReviewService reviewService = SpringContextHolder.getBean(ReviewService.class);
	private static ReviewCommonService reviewCommonService = SpringContextHolder.getBean(ReviewCommonService.class);
	private static OrderReviewService orderReviewService = SpringContextHolder.getBean(OrderReviewService.class);
	private static CostManageService costManageService = SpringContextHolder.getBean(CostManageService.class);
	private static PayManagerService payManagerService = SpringContextHolder.getBean(PayManagerService.class);
	

	private static VisaReturnReceiptServiceImpl visaReturnReceiptServiceImpl = SpringContextHolder.getBean(VisaReturnReceiptServiceImpl.class);
	private static VisaBorrowMoneyServiceImpl visaBorrowMoneyServiceImpl = SpringContextHolder.getBean(VisaBorrowMoneyServiceImpl.class);
	
	private static OrderReceiptService orderReceiptService = SpringContextHolder.getBean(OrderReceiptService.class);
	private static OrderinvoiceService orderInvoiceService = SpringContextHolder.getBean(OrderinvoiceService.class);

	private static IServiceChargeService serviceChargeService = SpringContextHolder.getBean(ServiceChargeServiceImpl.class);

	/**
	 * 获取审核状态sql条件
	 * @param reviewStatus
	 * @param userLevel
	 * @return
	 */
	public static String getReviewCheckSql(int reviewStatus, int userLevel){
		StringBuffer sbf=new StringBuffer();
		switch(reviewStatus){
			case 0:
				sbf.append(" and r.nowLevel >= " + userLevel);
				break;
			case 1:
				sbf.append(" and r.status = 1 and r.nowLevel = " + userLevel);
				break;
			case 2:
				sbf.append(" and r.status = 0 and r.nowLevel = " + userLevel);
				break;
			case 3:
				sbf.append(" and (r.nowLevel > " + userLevel + " or (r.nowLevel = r.topLevel and r.status in(2,3)))");
				break;
			default:
				break;
		}
		return sbf.toString();
	}
	/**
	 * add by ruyi.chen
	 * add date 2015-01-31
	 * 根据审核流程唯一标识获取审核动态
	 * @param rid
	 * @return
	 */
	public static List<ReviewLog>getReviewLog(Long rid){
		
		return reviewService.findReviewLog(rid);
	}
	/**
	 * add by ruyi.chen
	 * add date 2015-01-31
	 * 根据审核流程唯一标识获取流程实例
	 * @param rid
	 * @return
	 */
	public static Map<String,String> getReviewInfo(Long rid){
		return reviewService.findReview(rid);
	}
	/**
	 * add by ruyi.chen
	 * add date 2015-04-19
	 * 根据审核流程类型获取当前用户相应审核类型的条数(多个审核类型请以逗号分隔形式，例如：1,2,3)
	 * @param reviewTypes
	 * @return
	 */
	public static Integer getReviewCountByTypes(String reviewTypes){
		int count = 0;
		Integer reviewStatus = 1;//审核状态对于当前审核人的职务 为   待审核
		String [] flowTypes = reviewTypes.split(",");
		if(0 < flowTypes.length){
			for(String flowType : flowTypes){
				//获取部门职务列表		
				List<UserJob> userJobs = reviewCommonService. getWorkFlowJobByFlowType(Integer.parseInt(flowType));
				count = count + orderReviewService.getReviewCountByType(Integer.parseInt(flowType),userJobs,reviewStatus);
			}
		}				
		return count;
	}
	/**
	 * add by ruyi.chen
	 * add date 2015-04-19
	 * 根据审核流程类型获取当前用户相应审核类型的条数(多个审核类型请以逗号分隔形式，例如：1,2,3)
	 * @return
	 */
	public static Map<String,String> getAllReviewCount(){
		String reviewTypes = "1,3,4,5,6,7,8,9,10,11,12,13,14,16,17,18,19,20,200";  //200  表示签证订单的未读订单数量
		Map<String,String> map = Maps.newHashMap();
		Integer reviewStatus = 1;//审核状态对于当前审核人的职务 为   待审核
		String [] flowTypes = reviewTypes.split(",");
		if(0 < flowTypes.length){
			for(String flowType : flowTypes){
				int count = 0;
				//获取部门职务列表		
				List<UserJob> userJobs = reviewCommonService. getWorkFlowJobByFlowType(Integer.parseInt(flowType));
				count = orderReviewService.getReviewCountByType(Integer.parseInt(flowType),userJobs,reviewStatus);
				map.put(flowType, count+"");
			}
		}
		int costCount = costManageService.getReviewCountByType(15);
		map.put("15", costCount+"");
		costCount = costManageService.getReviewCountByType(18);
		map.put("18", costCount+"");
//		int visaCount = costManageService.getVisaCount();
//		map.put("200", visaCount+"");
		 
		/*wxw  added 签证借款，还签证收据的  待审核数量  2015-09-29---
		签证借款，还签证收据 菜单审核数量  显示批次的数量,审核列表的角色上也显示批次的数量*/
		int visaReturnReviewCount = visaReturnReceiptServiceImpl.getBatchReviewCount4Menu();
		map.put("4", visaReturnReviewCount+"");
		
		int visaBorrowMoneyReviewCount = visaBorrowMoneyServiceImpl.getBatchReviewCount4Menu();
		map.put("5", visaBorrowMoneyReviewCount+"");
		
		
		int value1 = Integer.parseInt(map.get("19"));
		int value2 = Integer.parseInt(map.get("20"));
		map.put("19", (value1+value2)+"");
		return map;
	}
	public static List<UserJob> getUserJobsReviewCountByType(Integer flowType,List<UserJob>userJobs,Integer reviewStatus){
		
		return orderReviewService.getUserJobsReviewCountByType(flowType, userJobs, reviewStatus);
	}
	public static List<UserJob> getUserJobsCostReviewCountByType(List<UserJob>userJobs){
		
		return costManageService.getCostUserJobsReviewCountByType(userJobs);
	}

	/*
	 * 获得下一个审核人信息
	 */
public static String getNextReview(Long reviewId){
		
	   return reviewService.getNextReviewJob(reviewId);
	}


/*
 * 获得下一个成本审核 审核人信息
 */
public static String getNextCostReview(Long reviewId){
	
   return reviewService.getNextCostReviewJob(reviewId);
}

/*
 * 获得下一个付款审核 审核人信息
 */
public static String getNextPayReview(Long reviewId){
	
   return reviewService.getNextPayReviewJob(reviewId);
}


/*
 * 获得某类成本审核或付款  待审核记录总数
 */
public static String getReviewTotal(Integer reviewFlow,Integer orderType){
	
   return costManageService.getReviewTotal( reviewFlow, orderType);
}


/*
* 获得下一个付款审核 审核人信息
*/
public static String getNextPayHotelReview(Long reviewId){
	
  return reviewService.getNextPayHotelReviewJob(reviewId);
}

/*
* 获得下一个付款审核 审核人信息
*/
public static String getNextCostHotelReview(Long reviewId){
	
  return reviewService.getNextCostHotelReviewJob(reviewId);
}

/*
* 获得下一个付款审核 审核人信息
*/
public static String getNextPayIslandReview(Long reviewId){
	
  return reviewService.getNextPayIslandReviewJob(reviewId);
}

/*
* 获得下一个付款审核 审核人信息
*/
public static String getNextCostIslandReview(Long reviewId){
	
  return reviewService.getNextCostIslandReviewJob(reviewId);
}

/*成本项已付款金额
 * 
 */
public static String getPayCost(String costId, String orderType,String moneyType){
	return costManageService.getPayCost(costId, orderType,moneyType);
}

/*获得退款金额
 * 
 */
public static String getRefundTotal(String reviewId){
	return costManageService.getRefundTotal(reviewId);
}

/*获得退款金额
 * 
 */
public static  String getOrderTotal(String orderId,String prdType){
	return costManageService.getOrderTotal(orderId,prdType);
}

	/**
	 * 查询其他收入收款未达账金额的数目
	 * @return
	 * @author shijun.liu
	 */
	public static long getNotAccountedCountOtherIncome(){
		return costManageService.findNotAccountedCountOtherIncome();
	}

//	/**
//	 * add by ruyi.chen
//	 * add date 2015-04-19
//	 * 根据审核流程类型获取当前用户相应审核类型的条数(多个审核类型请以逗号分隔形式，例如：1,2,3)
//	 * @param reviewTypes
//	 * @return
//	 */
//	public Map<String,String>  getUserJobReviewCountByTypes(String reviewTypes){
//		int count = 0;
//		Map<String,String> map = Maps.newHashMap();
//		Integer reviewStatus = 1;//审核状态对于当前审核人的职务 为   待审核
//		orderReviewService.getUserJobReviewCountByTypes(reviewTypes,);
//		return map;
//	}

	/**
	 * 新增成本付款总数
	 * @return
	 */
	public static Integer getOrderPayCount() {
		return reviewService.getOrderPayCount();
	}
	
	/**
	 * 新增退款付款总数
	 * @return
	 */
	public static Integer getRefundCount() {
		return reviewService.getRefundCount();
	}

	/**
	 * 新增返佣付款总数
	 * @return
	 */
	public static Integer getRebateCount() {
		return reviewService.getRebateCount();
	}
	
	/**
	 * 查询公版借款付款未付款的数目
	 * @return
	 * @author shijun.liu
	 */
	public static long getBorrowMoneyNotPayedCount(){
		return payManagerService.getBorrowMoneyNotPayedCount();
	}

	/**
	 * 查询签证批量借款付款未付款的数目
	 * @return
	 * @author shijun.liu
	 */
	public static long getBatchBorrowMoneyNotPayedCount(){
		return payManagerService.getBatchBorrowMoneyNotPayedCount();
	}

	/**
	 * 查询代收服务费未付款的数目
	 * @return
	 * @author yudong.xu 2016.9.14
	 */
	public static Integer getServiceChargeCount(){
		return serviceChargeService.getServiceChargeCount();
	}
	
	/**
	 * add by yang.jiang
	 * add date 2015-08-20
	 * describe 根据review ID, 获取对应机票的预计返佣总额（包括个人和团队，仅限申请了的）
	 * @param rid
	 * @return
	 */
	public static String getPreRebatesApplied(Long rid){
		if(rid == null) return null;
		return reviewService.getPreRebatesApplied(rid);
	}
	
	public static String getPreRebatesAppliedNew(String rid){
		if(rid == null) return null;
		return reviewService.getPreRebatesAppliedNew(rid);
	}
	
	
	/**
	 * add by chy 2015年11月20日15:45:53
	 * 获取待处理的收据数量
	 * @return
	 */
	public static Integer getAllReceiptNum(){
		return getToBeReviewedReceiptNum() + getReviewedReceiptNum();
	}
	
	/**
	 * add by chy 2015年11月20日15:45:53
	 * 获取待审核的收据数量
	 * @return
	 */
	public static Integer getToBeReviewedReceiptNum(){
		return orderReceiptService.getToBeReviewReceiptNum();
	}
	
	/**
	 * add by chy 2015年11月20日15:45:53
	 * 获取待开票的收据数量
	 * @return
	 */
	public static Integer getReviewedReceiptNum(){
		return orderReceiptService.getReviewedReceiptNum();
	}
	
	/**
	 * add by chy 2015年11月20日15:45:53
	 * 获取待处理的发票数量
	 * @return
	 */
	public static Integer getAllInvoiceNum(){
		return getToBeReviewedInvoiceNum() + getReviewedInvoiceNum();
	}
	
	/**
	 * add by chy 2015年11月20日15:45:53
	 * 获取待审核的发票数量
	 * @return
	 */
	public static Integer getToBeReviewedInvoiceNum(){
		return orderInvoiceService.getToBeReviewInvoiceNum();
	}
	
	/**
	 * add by chy 2015年11月20日15:45:53
	 * 获取待开票的发票数量
	 * @return
	 */
	public static Integer getReviewedInvoiceNum(){
		return orderInvoiceService.getReviewedInvoiceNum();
	}
	
}
