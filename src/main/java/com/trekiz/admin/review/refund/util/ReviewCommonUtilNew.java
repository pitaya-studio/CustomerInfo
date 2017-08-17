package com.trekiz.admin.review.refund.util;


import org.springframework.beans.factory.annotation.Autowired;

import com.quauq.review.core.engine.ReviewService;

/**
 * 
 *  文件名: ReviewCommonUtilNew.java
 *  功能:
 *  
 *  修改记录:   
 *  
 *  @author xiaokai.wang
 *  @DateTime 2015-11-23 上午10:57:11 
 *  @version 1.0
 */
public class ReviewCommonUtilNew {
	@Autowired
	private static ReviewService reviewService ;
	/*private static ReviewCommonService reviewCommonService = SpringContextHolder.getBean(ReviewCommonService.class);
	private static OrderReviewService orderReviewService = SpringContextHolder.getBean(OrderReviewService.class);
	private static CostManageService costManageService = SpringContextHolder.getBean(CostManageService.class);
	private static PayManagerService payManagerService = SpringContextHolder.getBean(PayManagerService.class);
	private static VisaReturnReceiptServiceImpl visaReturnReceiptServiceImpl = SpringContextHolder.getBean(VisaReturnReceiptServiceImpl.class);
	private static VisaBorrowMoneyServiceImpl visaBorrowMoneyServiceImpl = SpringContextHolder.getBean(VisaBorrowMoneyServiceImpl.class);
	*/
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


}
