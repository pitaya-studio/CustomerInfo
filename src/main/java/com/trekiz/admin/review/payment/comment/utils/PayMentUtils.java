package com.trekiz.admin.review.payment.comment.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.entity.ReviewLogNew;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.review.payment.comment.entity.PaymentReviewLog;

import javax.persistence.criteria.CriteriaBuilder;

/**
 * 付款审批工具类
 * @author shijun.liu
 *
 */
public class PayMentUtils {

	public static final String ALL = "0";							//全部
	public static final String CURRENT_USER_REVIEW = "1";			//待本人审批
	public static final String CURRENT_USER_REVIEWED = "2";			//本人已审批
	public static final String NOT_CURRENT_USER_REVIEW = "3";		//非本人审批
	
	private static ReviewService reviewService = SpringContextHolder.getBean(ReviewService.class);
	
	/**
	 * 业务规则生成器(businessKey),规则：'payment_'flowType_orderType_uuid
	 * @param orderType		订单类型
	 * @param uuid			成本表UUID
	 * @return				buinessKey
	 * @author shijun.liu
	 */
	public static String generateBusinessKey(Integer orderType, String uuid){
		return "payment_"+Context.REVIEW_FLOWTYPE_PAYMENT+"_"+orderType+"_"+uuid;
	}
	
	/**
	 * 查询付款审批的日志
	 * @param actualInList		实际成本境内明细
	 * @param actualOutList		实际成本境外明细
	 * @return
	 * @author 	shijun.liu
	 */
	public static List<PaymentReviewLog> getPaymentReviewLog(List<CostRecord> actualInList, List<CostRecord> actualOutList){
		List<PaymentReviewLog> logList = new ArrayList<PaymentReviewLog>();
		if(null != actualInList && actualInList.size() > 0){
			for (CostRecord costRecord : actualInList) {
				if(0 == costRecord.getReviewType()){	//只显示成本数据，0表示成本数据
					PaymentReviewLog log = new PaymentReviewLog();
					List<ReviewLogNew> list = reviewService.getReviewLogByReviewId(costRecord.getPayReviewUuid());
					log.setReviewUuid(costRecord.getPayReviewUuid());
					log.setCostName(costRecord.getName());
					log.setReviewLogNew(list);
					logList.add(log);
				}
			}
		}
		
		if(null != actualOutList && actualOutList.size() > 0){
			for (CostRecord costRecord : actualOutList) {
				if(0 == costRecord.getReviewType()){	//只显示成本数据，0表示成本数据
					PaymentReviewLog log = new PaymentReviewLog();
					List<ReviewLogNew> list = reviewService.getReviewLogByReviewId(costRecord.getPayReviewUuid());
					log.setReviewUuid(costRecord.getPayReviewUuid());
					log.setCostName(costRecord.getName());
					log.setReviewLogNew(list);
					logList.add(log);
				}
			}
		}
		return logList;
	}

	/**
	 * 遍历inList和outList中的元素，根据costId找到指定的CostRecord元素，并和对应List的第一位置的元素交换位置。
	 * 如果在境内inList找到就返回0，否则在境外找到就返回1。yudong.xu 2016.6.1
	 */
	public static Integer preHandleList(List<CostRecord> inList,List<CostRecord> outList,Long costId){
		Boolean fromIn = changeListOrder(inList,costId);
		if (fromIn) return 0;
		changeListOrder(outList,costId);
		return 1;
	}

	/**
	 *遍历List中的costRecord，找到指定的costId的CostRecord，并将其和List中第一位的元素交换位置。
	 * 如果找到了指定的元素，交换位置完毕，返回true；如果未找到返回false；yudong.xu 2016.6.1
	 */
	private static Boolean changeListOrder(List<CostRecord> list,Long costId){
		for (int i = 0,size = list.size(); i < size; i++) {
			if (list.get(i).getId().equals(costId)){//找到目标元素，放到第一的位置上，交换位置。
				CostRecord target = list.get(i);
				CostRecord first = list.set(0,target);
				list.set(i,first);
				return true;
			}
		}
		return false;
	}
}
