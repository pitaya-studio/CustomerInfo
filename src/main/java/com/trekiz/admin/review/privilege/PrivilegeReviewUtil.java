package com.trekiz.admin.review.privilege;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.SpringContextHolder;

import java.util.List;

/**
 * 散拼优惠审批工具类
 * @author yanzhenxing
 * @date 2016/1/29
 */
public class PrivilegeReviewUtil {

    private static ReviewService reviewService= SpringContextHolder.getBean(ReviewService.class);
    /**
     * 判断一个订单是否有优惠审批流程正在进行中
     * @param orderId
     * @return
     */
    public static Boolean hasPrivilegeProcessingReviews(Long orderId){
        if(orderId!=null){
            List<ReviewNew> orderReviewList = reviewService.getOrderReviewList(orderId.toString(),Context.ProductType.PRODUCT_LOOSE.toString(),Context.REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE.toString());
            if(orderReviewList!=null&&orderReviewList.size()>0){
                for (ReviewNew reviewNew : orderReviewList) {
                    if(reviewNew.getStatus().equals( ReviewConstant.REVIEW_STATUS_PROCESSING)){//如果有正在审批中的流程，返回true
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
