package com.trekiz.admin.modules.mtourOrder.jsonbean;

import java.util.Collection;
import java.util.List;

/**
 * 订单列表批量查询支出单JsonBean.
 * yudong.xu 2016.7.4
 */
public class BatchOrderPaymentJsonBean {

    private Long applicantId; //申请人id ---当前用户
    private String applicantName; //申请人姓名
    private Collection<OrderPaymentJsonBean> expenditureList; //用户批量选中的订单下的所有支出单。

    public BatchOrderPaymentJsonBean() { }

    public Long getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(Long applicantId) {
        this.applicantId = applicantId;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public Collection<OrderPaymentJsonBean> getExpenditureList() {
        return expenditureList;
    }

    public void setExpenditureList(Collection<OrderPaymentJsonBean> expenditureList) {
        this.expenditureList = expenditureList;
    }
}
