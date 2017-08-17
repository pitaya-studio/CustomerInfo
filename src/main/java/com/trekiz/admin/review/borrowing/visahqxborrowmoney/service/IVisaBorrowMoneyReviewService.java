package com.trekiz.admin.review.borrowing.visahqxborrowmoney.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.persistence.Page;

public interface IVisaBorrowMoneyReviewService {

    /**
     * 查询符合条件的借款记录
     *
     * @param params
     * @return
     * @author yunpeng.zhang
     * @createDate 2015年12月3日20:50:17
     */
    Page<Map<String, Object>> queryBorrowMoneyReviewList(Map<String, Object> params);

    /**
     * 通过批次号查询该批次下游客的信息(针对签证借款)
     *
     * @param batchNo
     * @return List<Map<String, String>>
     * @author yunpeng.zhang
     * @DateTime 2015年12月4日17:02:48
     */
    void getTravelerList(String batchNo, List<Map<String, String>> travelerList);

    /**
     * 通过批次号查询该批次下游客的信息(针对签证还收据)
     *
     * @param batchNo
     * @return List<Map<String, String>>
     * @author yang.jiang
     * @DateTime 2015-12-5 16:36:16
     */
    void getTravelerList4Receipt(String batchNo, List<Map<String, String>> travelerList);

    /**
     * 签证借款批次审批导出游客信息
     *
     * @param batchNo
     * @return void
     * @author Bin
     * @DateTime 2015年12月5日
     */
    public void exportTravelerInfo(String batchNo, HttpServletRequest request, HttpServletResponse response);

    /**
     * 环球行签证借款审核
     *
     * @param result     1 审核 0 驳回
     * @param batchNo    批次号
     * @param denyReason 备注或驳回原因
     * @return
     * @author yunpeng.zhang
     * @createDate 2015年12月8日13:54:00
     */
    Map<String, Object> reviewVisaBorrowMoneybyBatchNo(String result, String batchNo, String denyReason) throws Exception;

    /**
     * 根据批次号对签证借款审核进行撤销
     * @param batchNo 批次号
     * @return
     * @author yunpeng.zhang
     * @createDate 2015年12月9日11:25:23
     */
    Map<String, Object> backReivew(String batchNo) throws Exception;

    /**
     * 查找符合条件的批次号，返回只有待本人审核的批次号集合
     * @author yunpeng.zhang
     * @createDate 2015年12月12日17:25:20
     * @param batchNos 批量审核批次号
     * @return
     */
    List<String> findMatchConditionBatchNos(String batchNos);

    /**
     * 批量签证借款审核
     * @param canBeBatchNos
     * @return
     */
    Map<String,Object> batchReview(List<String> canBeBatchNos, String result, String remark) throws Exception;
}
