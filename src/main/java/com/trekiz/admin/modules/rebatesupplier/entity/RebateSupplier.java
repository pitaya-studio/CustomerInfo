package com.trekiz.admin.modules.rebatesupplier.entity;

import com.trekiz.admin.modules.rebatesupplier.service.Rebatedable;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 供应商实体(暂时给返佣使用)
 * @author yanzhenxing
 * @date 2016/1/7
 */

@Entity
@Table(name = "rebate_supplier")
public class RebateSupplier implements Serializable,Rebatedable{

    /**
     * 删除标识：正常状态
     */
    public static final Integer DEL_FLAG_NORMAL=0;
    /**
     * 删除标识：已删除
     */
    public static final Integer DEL_FLAG_DELETED=1;
    /**
     * 草稿状态，未提交
     */
    public static final Integer STATUS_DRAFT=0;
    /**
     * 已提交保存状态
     */
    public static final Integer STATUS_COMMITED=1;

    @Transient
    public Integer getRebatedableType() {
        return Rebatedable.REBATEDABLE_TYPE_SUPPLIER;
    }

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false,unique = true)
    private Long id;
    @Column(name = "uuid",nullable = false,unique = true)
    private String uuid;
    @Column(name = "name")
    private String name;
    @Column(name = "en_name")
    private String enName;
    @Column(name = "brand")
    private String brand;
    @Column(name = "address")
    private String address;
    @Column(name = "person_in_charge")
    private String personInCharge;
    @Column(name = "operator_id")
    private Long operatorId;
    @Column(name = "operator_name")
    private String operatorName;
    @Column(name = "postcode")
    private String postcode;
    @Column(name = "telephone")
    private String telephone;
    @Column(name = "fax")
    private String fax;
    @Column(name = "email")
    private String email;
    @Column(name = "description")
    private String description;
    @Column(name = "status")
    private Integer status;
    @Column(name = "company_id")
    private Long companyId;
    @Column(name = "company_uuid")
    private String companyUuid;
    @Column(name = "create_by")
    private Long createBy;
    @Column(name = "create_date")
    private Date createDate;
    @Column(name = "update_by")
    private Long updateBy;
    @Column(name = "update_date")
    private Date updateDate;
    @Column(name = "del_flag")
    private Integer delFlag;

    public RebateSupplier() {
        delFlag=0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPersonInCharge() {
        return personInCharge;
    }

    public void setPersonInCharge(String personInCharge) {
        this.personInCharge = personInCharge;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
        this.companyUuid = companyUuid;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
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
}
