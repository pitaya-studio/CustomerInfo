package com.trekiz.admin.review.prdreturn.common.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.ProductOrderService;
import com.trekiz.admin.modules.statisticAnalysis.home.service.OrderDateSaveOrUpdateService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.airticketChange.service.NewAirticketChangeServiceImpl;

/**
 * @Copyright: 2016 www.quauq.com Inc. All rights reserved.
 * @date 2016/2/26
 */
@Service
public class ProductReturnAppService {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserReviewPermissionChecker userReviewPermissionChecker;

    @Autowired
    private NewAirticketChangeServiceImpl newAirticketChangeService;

    @Autowired
    private ProductOrderService orderService;

    @Autowired
    private IProductReturnService productReturnService;
    
	@Autowired
	private OrderDateSaveOrUpdateService orderDateSaveOrUpdateService;

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Map<String, Object> batchReturnReview(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<String, Object>();
        String revIds = request.getParameter("revIds");//审核表ids
        String remark = request.getParameter("remark");//通过/驳回原因
        String strResult = request.getParameter("result");//通过/驳回
        StringBuffer reply = new StringBuffer();
        if(Context.NumberDef.NUMER_TWO.toString().equals(strResult)){
            strResult = Context.REVIEW_ACTION_PASS;
        } else {
            strResult = Context.REVIEW_ACTION_REJECT;
        }
        int n = Context.NumberDef.NUMER_ZERO;
        String[] revidArr = revIds.split(",");
        int revNum = revidArr.length;
        ReviewNew reviewNew = null;
        for(String revid : revidArr){
            if(revid == null || "".equals(revid)){
                reply.append("错误的参数reviewid不能为空 airticketRefundReviewContriller");
                n++;
                continue;
            }
            //当前登录用户id
            String userId = UserUtils.getUser().getId().toString();
            String companyId = UserUtils.getUser().getCompany().getUuid();
            ReviewResult reviewResult = new ReviewResult();
            if(Context.REVIEW_ACTION_PASS.equals(strResult.trim())){
                reviewResult = reviewService.approve(userId, companyId, null, userReviewPermissionChecker, revid, remark, null);
            } else {
                reviewResult = reviewService.reject(userId, companyId, null, revid, remark, null);
            }
            if(!reviewResult.getSuccess()){
                result.put("flag", "fail");
                result.put("msg", reviewResult.getMessage());
            }
            if(ReviewConstant.REVIEW_STATUS_PASSED == reviewResult.getReviewStatus()){//退票审核审批完毕后 处理业务
                try{
                    reviewNew = reviewService.getReview(revid);
                    // 退票
                    if(Context.REVIEW_FLOWTYPE_AIRTICKET_RETURN.toString().equals(reviewNew.getProcessType())){
                        //还余位
                        newAirticketChangeService.changeOrExit(null, reviewNew.getOrderId(), reviewNew.getTravellerId());
                        // 退团
                    } else if (Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString().equals(reviewNew.getProcessType())) {
                        //退团审核流程通过后，更新余位，更新游客状态，更新审核流程状态
                        ProductOrderCommon productOrder =
                                orderService.getProductorderById(Long.parseLong(reviewNew.getOrderId()));
						productReturnService.handleFreePositionAndTraveler(reviewNew.getTravellerId(), productOrder, reviewNew.getId(), request);
						//-------by------junhao.zhao-----2017-01-20-----主要通过productorder更改表order_data_statistics订单金额与人数---开始-----
						if(productOrder != null){
							orderDateSaveOrUpdateService.updatePeopleAndMoneyPro(productOrder.getId(), productOrder.getOrderStatus());
						}
						//-------by------junhao.zhao-----2017-01-20-----主要通过productorder更改表order_data_statistics订单金额与人数---结束-----
						
//                        handleFreePositionAndTraveler(reviewNew.getTravellerId(), productOrder, request);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    result.put("flag", "fail");
                    result.put("msg", "批量审核失败!");
                    return result;
                }
                //退团批量审批驳回后有逻辑需要处理, add by yunpeng.zhang, add date 2015年12月24日11:55:35
            } else if (ReviewConstant.REVIEW_STATUS_REJECTED == reviewResult.getReviewStatus()) {
                try {
                    reviewNew = reviewService.getReview(revid);
                    // 退团
                    if(Context.REVIEW_FLOWTYPE_EXIT_GROUP.toString().equals(reviewNew.getProcessType())) {
						productReturnService.updateTravelerStatus(Context.TRAVELER_DELFLAG_NORMAL, Long.parseLong(reviewNew.getTravellerId()));
//                        updateTravelerStatus(Context.TRAVELER_DELFLAG_NORMAL, Long.parseLong(reviewNew.getTravellerId()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    result.put("flag", "fail");
                    result.put("msg", "批量驳回失败!");
                    return result;
                }
            }
        }
        if(!StringUtils.isBlank(reply)){
            result.put("flag", "fail");
            result.put("msg", "成功" + (revNum - n) + "个，失败" + n + "个:" + reply);
            return result;
        }
        result.put("flag", "success");
        return result;
    }
}
