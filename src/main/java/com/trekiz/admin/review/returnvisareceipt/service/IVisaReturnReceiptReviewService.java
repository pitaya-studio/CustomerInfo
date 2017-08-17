package com.trekiz.admin.review.returnvisareceipt.service;

import java.util.Map;

/**
 * Created by yunpeng.zhang on 2015/12/10.
 */
public interface IVisaReturnReceiptReviewService {
    /**
     * 还签证审核
     *
     * @param result     1 审核 0 驳回
     * @param batchNo    批次号
     * @param denyReason 备注或驳回原因
     * @return
     * @author yunpeng.zhang
     * @throws Exception 
     * @createDate 2015年12月8日13:54:00
     */
    Map<String,Object> returnVisaReceiptReview(String result, String batchNo, String denyReason) throws Exception;
    
    /**
     * 批量还签证审核
     * @author yunpeng.zhang
     * @param remark 
     * @param result 
     * @createData 2015年12月14日14:26:40
     * @param batchNoStrs
     */
    void batchReturnVisaReceiptReview(String result, String remark, String[] batchNoStrs)  throws Exception;

    /**
     * 根据批次号对还签证审核进行撤销
     * @param batchNo 批次号
     * @return
     * @author yunpeng.zhang
     * @createDate 2015年12月22日12:23:56
     */
    Map<String,Object> backReivew(String batchNo) throws Exception;
}
