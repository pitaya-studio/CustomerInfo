package com.trekiz.admin.modules.pay.entity;

import com.trekiz.admin.common.persistence.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 支付宝结算记录表对应的实体类 yudong.xu 2016.7.26
 */
@Entity
@Table(name = "pay_alipay")
@DynamicInsert(true)
@DynamicUpdate(true)
public class PayAlipay extends BaseEntity {

    // 主键
    private Integer id;
    // UUID
    private String uuid;
    // 来款支付宝名称
    private String fromAlipayName;
    // 来款支付宝账号
    private String fromAlipayAccount;
    // 收款单位
    private String receiveCompanyName;
    // 收款支付宝名称
    private String toAlipayName;
    // 收款支付宝账号
    private String toAlipayAccount;
    // 创建者
    private Integer createBy;
    // 创建日期
    private Date createDate;
    // 更新者
    private Integer updateBy;
    // 更新日期
    private Date updateDate;
    // 删除标记
    private String delFlag;

    public PayAlipay() { }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name="uuid")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Column(name="from_alipay_name")
    public String getFromAlipayName() {
        return fromAlipayName;
    }

    public void setFromAlipayName(String fromAlipayName) {
        this.fromAlipayName = fromAlipayName;
    }

    @Column(name="from_alipay_account")
    public String getFromAlipayAccount() {
        return fromAlipayAccount;
    }

    public void setFromAlipayAccount(String fromAlipayAccount) {
        this.fromAlipayAccount = fromAlipayAccount;
    }

    @Column(name="receive_company_name")
    public String getReceiveCompanyName() {
        return receiveCompanyName;
    }

    public void setReceiveCompanyName(String receiveCompanyName) {
        this.receiveCompanyName = receiveCompanyName;
    }

    @Column(name="to_alipay_name")
    public String getToAlipayName() {
        return toAlipayName;
    }

    public void setToAlipayName(String toAlipayName) {
        this.toAlipayName = toAlipayName;
    }

    @Column(name="to_alipay_account")
    public String getToAlipayAccount() {
        return toAlipayAccount;
    }

    public void setToAlipayAccount(String toAlipayAccount) {
        this.toAlipayAccount = toAlipayAccount;
    }


    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
}
