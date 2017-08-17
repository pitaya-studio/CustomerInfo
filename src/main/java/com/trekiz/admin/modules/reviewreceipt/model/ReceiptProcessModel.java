package com.trekiz.admin.modules.reviewreceipt.model;

/**
 * 用于显示单据流程关系的model
 * @author yanzhenxing
 * @date 2015/11/30
 */
public class ReceiptProcessModel {
    private String receiptType;
    private String receiptDescription;
    private String processNames;

    public ReceiptProcessModel() {
    }

    public String getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(String receiptType) {
        this.receiptType = receiptType;
    }

    public String getReceiptDescription() {
        return receiptDescription;
    }

    public void setReceiptDescription(String receiptDescription) {
        this.receiptDescription = receiptDescription;
    }

    public String getProcessNames() {
        return processNames;
    }

    public void setProcessNames(String processNames) {
        this.processNames = processNames;
    }
}
