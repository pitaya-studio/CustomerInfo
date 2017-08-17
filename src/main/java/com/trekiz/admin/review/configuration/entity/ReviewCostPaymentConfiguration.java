package com.trekiz.admin.review.configuration.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 成本付款审批配置类
 * @author yanzhenxing
 * @date 2015/12/9
 */
@Entity
@Table(name = "review_cost_payment_configuration")
public class ReviewCostPaymentConfiguration implements Serializable {
    private static final long serialVersionUID = 1L;

    /* 主键id */
    private String id;
    /* 部门id */
    private String deptId;
    /* 产品类型 */
    private String productType;
    /* 流程类型 */
    private String processType;
    /* 公司id */
    private String companyId;
    /* 付款金额是否等于实际成本金额 */
    private Integer isPaymentEqualsCost;
    /* 创建人 */
    private String createBy;
    /* 创建时间 */
    private Date createDate;
    /* 更新人 */
    private String updateBy;
    /* 更新时间 */
    private Date updateDate;
    /* 删除标示 默认0 正常 1 已删除 */
    private Integer delFlag = 0;

    public ReviewCostPaymentConfiguration() {
    }

    @Id
    @Column(name = "id", unique = true, nullable = false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Column(name = "dept_id", nullable = false)
    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    @Column(name = "product_type", nullable = false)
    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    @Column(name = "process_type", nullable = false)
    public String getProcessType() {
        return processType;
    }

    public void setProcessType(String processType) {
        this.processType = processType;
    }

    @Column(name = "company_id")
    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    @Column(name = "is_payment_equals_cost")
    public Integer getIsPaymentEqualsCost() {
        return isPaymentEqualsCost;
    }

    public void setIsPaymentEqualsCost(Integer isPaymentEqualsCost) {
        this.isPaymentEqualsCost = isPaymentEqualsCost;
    }

    @Column(name = "create_by", nullable = false)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "create_date", nullable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "update_by")
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "update_date")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name = "del_flag", nullable = false)
    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }
}
