package com.trekiz.admin.modules.reviewreceipt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 单据、流程关系实体
 * @author yanzhenxing
 * @date 2015/11/30
 */
//@Entity
//@Table(name = "receipt_process")
public class ReceiptProcess implements Serializable{


    /**
     * id，uuid
     */
    @Id
    @Column(name = "id",unique = true,nullable = false,length = 36)
    private String id;

    /**
     * 单据类型
     */
    @Column(name = "receipt_type",nullable = false)
    private String receiptType;

    /**
     * 单据类型描述
     */
    @Column(name = "receipt_description",length = 20)
    private String receiptDescription;

    /**
     * 流程类型
     */
    @Column(name = "process_type",nullable = false)
    private Integer processType;

    /**
     * 流程类型名称
     */
    @Column(name = "process_type")
    private String processName;

    /**
     * 创建者
     */
    @Column(name="create_by",nullable = false,length = 50)
    private String createBy;

    /**
     * 创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 更新者
     */
    @Column(name="update_by",length = 50)
    private String updateBy;

    /**
     * 更新时间
     */
    @Column(name = "update_date")
    private Date updateDate;

    /**
     * 删除标识
     */
    @Column(name = "del_flag",nullable = false)
    private Integer delFalg;

    public ReceiptProcess() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getProcessType() {
        return processType;
    }

    public void setProcessType(Integer processType) {
        this.processType = processType;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getDelFalg() {
        return delFalg;
    }

    public void setDelFalg(Integer delFalg) {
        this.delFalg = delFalg;
    }
}
