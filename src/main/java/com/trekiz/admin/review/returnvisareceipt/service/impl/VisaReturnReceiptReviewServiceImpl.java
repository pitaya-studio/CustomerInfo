package com.trekiz.admin.review.returnvisareceipt.service.impl;

import java.util.*;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.review.borrowing.visahqxborrowmoney.service.IVisaBorrowMoneyReviewService;
import com.trekiz.admin.review.returnvisareceipt.service.IVisaReturnReceiptReviewService;

/**
 * Created by yunpeng.zhang on 2015/12/10.
 */
@Service
public class VisaReturnReceiptReviewServiceImpl implements IVisaReturnReceiptReviewService {

    @Autowired
    private ReviewService processReviewService;
    @Autowired
    private TravelerService travelerService;
    @Autowired
    private IVisaBorrowMoneyReviewService visaBorrowMoneyReviewService;
    @Autowired
	private UserReviewPermissionChecker userReviewPermissionChecker;

    protected org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 批量还签证审核
     * @author yunpeng.zhang
     * @createData 2015年12月14日14:26:40
     * @param batchNoStrs
     * @throws Exception 
     */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void batchReturnVisaReceiptReview(String result, String remark, String[] batchNoStrs) throws Exception {
		for (String batchNo : batchNoStrs) {
			try {
				returnVisaReceiptReview(result, batchNo, remark);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage());
				throw new Exception("fail");
			}
		}
		
	}

    /**
     * 根据批次号对还签证审核进行撤销
     *
     * @param batchNo 批次号
     * @return
     * @author yunpeng.zhang
     * @createDate 2015年12月22日12:24:57
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Map<String, Object> backReivew(String batchNo) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        String companyUuid = UserUtils.getUser().getCompany().getUuid();
        List<Map<String, String>> travelerList = new ArrayList<Map<String, String>>();
        visaBorrowMoneyReviewService.getTravelerList4Receipt(batchNo, travelerList);
        try {
            if(travelerList == null || travelerList.size() <= 0) {
                throw new Exception("撤销失败!");
            }
            for (Map<String, String> travelerMap : travelerList) {
                String reviewId = travelerMap.get("reviewId");
                ReviewResult reviewResult = processReviewService.back(UserUtils.getUser().getId().toString(), companyUuid, null, reviewId, null, null);
                if(!reviewResult.getSuccess()) {
                    throw new Exception("撤销失败!");
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(e);
        }

        resultMap.put("msg", "撤销成功！");
        resultMap.put("result", "success");
        return resultMap;
    }


    /**
     * 还签证收据审核
     *
     * @param result     1 审核 0 驳回
     * @param batchNo    批次号
     * @param denyReason 备注或驳回原因
     * @return
     * @author yunpeng.zhang
     * @throws Exception 
     * @createDate 2015年12月10日16:54:50
     */
    @SuppressWarnings("unused")
	@Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public Map<String, Object> returnVisaReceiptReview(String result, String batchNo, String denyReason) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, String>> travelerList = new ArrayList<Map<String, String>>();
        visaBorrowMoneyReviewService.getTravelerList4Receipt(batchNo, travelerList);
        try {
            for (Map<String, String> travelerMap : travelerList) {
                Map<String, Object> variables = new HashMap<String, Object>();
                ReviewResult reviewResult;
                String companyUuid = UserUtils.getUser().getCompany().getUuid();
                if (Context.REVIEW_ACTION_PASS.equals(result)) {                  //通过
                    reviewResult = processReviewService.approve(UserUtils.getUser().getId().toString(), companyUuid, null, 
                    		userReviewPermissionChecker,travelerMap.get("reviewId"), denyReason, variables);
                } else {                                                        //驳回
                    reviewResult = processReviewService.reject(UserUtils.getUser().getId().toString(), companyUuid, null,
                                                               travelerMap.get("reviewId"), denyReason, variables);
                }
                if (!reviewResult.getSuccess()) {
                    throw new Exception("审核或驳回失败！");
                }
                // 3如果审核通过并且当前层级为最高层级
                if (ReviewConstant.REVIEW_STATUS_PASSED == reviewResult.getReviewStatus()) {
                	Traveler traveler = null;
                	if(travelerMap.get("travellerId") != null) {
                		traveler = travelerService.findTravelerById(Long.parseLong(travelerMap.get("travellerId")));
                	}
                    //TODO 当前没有逻辑要处理
                }

            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception("fail");
        }

        resultMap.put("result", "success");
        return resultMap;

    }

    


}
