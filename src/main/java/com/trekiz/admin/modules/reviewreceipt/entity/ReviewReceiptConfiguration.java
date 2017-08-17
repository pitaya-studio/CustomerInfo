package com.trekiz.admin.modules.reviewreceipt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 审批单据配置
 * @author yanzhenxing
 * @date 2015/11/30
 */
@Entity
@Table(name = "review_receipt_configuration")
public class ReviewReceiptConfiguration implements Serializable{

    /**
     * id，uuid
     */
    @Id
    @Column(name = "id",unique = true,nullable = false,length = 36)
    private String id;

    /**
     * 公司id，uuid
     */
    @Column(name = "company_id",nullable = false,length = 36)
    private String companyId;

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
     * 单据中的审批元素
     */
    @Column(name = "review_element",nullable = false)
    private Integer reviewElement;

    /**
     * 审批元素对应的审批者的key
     */
    @Column(name = "reviewer_key",nullable = false)
    private String reviewerKey;

    /**
     * 审批元素对应的审批者的key名称
     */
    @Column(name = "reviewer_key_name")
    private String reviewerKeyName;
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
    private Integer delFlag;

    public ReviewReceiptConfiguration() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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

    public Integer getReviewElement() {
        return reviewElement;
    }

    public void setReviewElement(Integer reviewElement) {
        this.reviewElement = reviewElement;
    }

    public String getReviewerKey() {
        return reviewerKey;
    }

    public void setReviewerKey(String reviewerKey) {
        this.reviewerKey = reviewerKey;
    }

    public String getReviewerKeyName() {
        return reviewerKeyName;
    }

    public void setReviewerKeyName(String reviewerKeyName) {
        this.reviewerKeyName = reviewerKeyName;
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

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }


    /**
     * 判断是否是相同的审批元素
     * @param other
     * @return
     */
    public boolean isSameReviewElement(ReviewReceiptConfiguration other){
        if (null==other) return false;
        if (this.companyId.equals(other.companyId)&&
                this.receiptType.equals(other.receiptType)&&
                this.reviewElement.equals(other.reviewElement)&&
                this.delFlag.equals(other.delFlag)) return true;
        return false;
    }

    /**
     * 将对象的id信息以及单据审批元素等信息复制到目标对象中
     * @param dest
     */
    public void copyReviewElement(ReviewReceiptConfiguration dest){
        if (dest!=null){
            dest.setId(this.id);
            dest.setCompanyId(this.companyId);
            dest.setCreateBy(this.createBy);
            dest.setCreateDate(this.createDate);
            dest.setDelFlag(this.delFlag);
            dest.setReceiptType(this.receiptType);
            dest.setReceiptDescription(this.receiptDescription);
            dest.setReviewElement(this.reviewElement);
        }
    }


}
