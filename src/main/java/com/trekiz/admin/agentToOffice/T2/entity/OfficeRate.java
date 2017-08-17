package com.trekiz.admin.agentToOffice.T2.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by quauq on 2016/8/10.
 */
@Entity
@Table(name = "sys_office_rate")
@DynamicUpdate
@DynamicInsert
public class OfficeRate {

    private Long id; // id
    private String uuid; // uuid
    private String companyUuid; // 对应批发商的uuid
    private Integer agentType; // 渠道类型，目前是1门店,2总社,3集团客户
    private Integer quauqRateType; // quauq产品费率收费类型,0百分比,1定额
    private BigDecimal quauqRate; // quauq产品费率的值,结合收费类型。百分比类型则存百分比。定额存定额。
    private Integer quauqOtherRateType; // quauq其他费率收费类型
    private BigDecimal quauqOtherRate; // quauq其他费率的值
    private Integer agentRateType; // 渠道产品费率类型
    private BigDecimal agentRate; // 渠道产品费率的值
    private Integer agentOtherRateType; // 渠道其他费率类型
    private BigDecimal agentOtherRate; // 渠道其他费率的值

    private Long createBy; // 创建人
    private Date createDate; // 创建时间
    private Long updateBy; // 更新人
    private Date updateDate; //更新时间
    private String delFlag; //删除标志
    
    //560 需求 抽成费率。 add by chao.zhang
    private Integer chouchengRateType; //抽成费率类型，0百分比，1定额
    private BigDecimal chouchengRate;//抽成费率的值，结合费率类型
    
    public Integer getChouchengRateType() {
		return chouchengRateType;
	}

	public void setChouchengRateType(Integer chouchengRateType) {
		this.chouchengRateType = chouchengRateType;
	}

	

	public OfficeRate() { }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "uuid")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Column(name = "company_uuid")
    public String getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
        this.companyUuid = companyUuid;
    }

    @Column(name = "agent_type")
    public Integer getAgentType() {
        return agentType;
    }

    public void setAgentType(Integer agentType) {
        this.agentType = agentType;
    }

    @Column(name = "quauq_rate_type")
    public Integer getQuauqRateType() {
        return quauqRateType;
    }

    public void setQuauqRateType(Integer quauqRateType) {
        this.quauqRateType = quauqRateType;
    }

    @Column(name = "quauq_rate")
    public BigDecimal getQuauqRate() {
        return quauqRate;
    }

    public void setQuauqRate(BigDecimal quauqRate) {
        this.quauqRate = quauqRate;
    }

    @Column(name = "quauq_other_rate_type")
    public Integer getQuauqOtherRateType() {
        return quauqOtherRateType;
    }

    public void setQuauqOtherRateType(Integer quauqOtherRateType) {
        this.quauqOtherRateType = quauqOtherRateType;
    }

    @Column(name = "quauq_other_rate")
    public BigDecimal getQuauqOtherRate() {
        return quauqOtherRate;
    }

    public void setQuauqOtherRate(BigDecimal quauqOtherRate) {
        this.quauqOtherRate = quauqOtherRate;
    }

    @Column(name = "agent_rate_type")
    public Integer getAgentRateType() {
        return agentRateType;
    }

    public void setAgentRateType(Integer agentRateType) {
        this.agentRateType = agentRateType;
    }

    @Column(name = "agent_rate")
    public BigDecimal getAgentRate() {
        return agentRate;
    }

    public void setAgentRate(BigDecimal agentRate) {
        this.agentRate = agentRate;
    }

    @Column(name = "agent_other_rate_type")
    public Integer getAgentOtherRateType() {
        return agentOtherRateType;
    }

    public void setAgentOtherRateType(Integer agentOtherRateType) {
        this.agentOtherRateType = agentOtherRateType;
    }

    @Column(name = "agent_other_rate")
    public BigDecimal getAgentOtherRate() {
        return agentOtherRate;
    }

    public void setAgentOtherRate(BigDecimal agentOtherRate) {
        this.agentOtherRate = agentOtherRate;
    }

    @Column(name = "create_by")
    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    @Column(name = "create_date")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "update_by")
    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "update_date")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name = "del_flag")
    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @Override
    public String toString() {
        return "OfficeRate{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", companyUuid='" + companyUuid + '\'' +
                ", agentType=" + agentType +
                ", quauqRateType=" + quauqRateType +
                ", quauqRate=" + quauqRate +
                ", quauqOtherRateType=" + quauqOtherRateType +
                ", quauqOtherRate=" + quauqOtherRate +
                ", agentRateType=" + agentRateType +
                ", agentRate=" + agentRate +
                ", agentOtherRateType=" + agentOtherRateType +
                ", agentOtherRate=" + agentOtherRate +
                ", delFlag='" + delFlag + '\'' +
                '}';
    }

	public BigDecimal getChouchengRate() {
		return chouchengRate;
	}

	public void setChouchengRate(BigDecimal chouchengRate) {
		this.chouchengRate = chouchengRate;
	}
}
