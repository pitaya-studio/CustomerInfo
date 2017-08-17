package com.trekiz.admin.modules.log.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * 系统业务日志对象
 * @author shijun.liu
 * @date    2016.08.09
 */
@Entity
@Table(name = "quauq_business_log")
public class QuauqBusinessLog {

    private Long id;               //自增长ID
    private String companyUuid;    //所属公司ID
    private Long agentId;          //所属渠道ID
    private String type;           //日志类型，即针对不同业务场景的日志
    private String content;        //日志内容
    private Date createDate;       //操作时间
    private Long createBy;         //操作人
    public static final String ADD_GROUP_STRATEGY="add_group_strategy";
    public static final String UPDATE_GROUP_STRATEGY="update_group_strategy";
    public static final String ADD_DEFAULT_FEE_RATE = "add_default_fee_rate";
    public static final String UPDATE_DEFAULT_FEE_RATE = "update_default_fee_rate";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name="company_uuid", nullable = false)
    public String getCompanyUuid() {
        return companyUuid;
    }

    public void setCompanyUuid(String companyUuid) {
        this.companyUuid = companyUuid;
    }

    @Column(name="agent_id", nullable = true)
    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    @Column(name="type", nullable = false)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name="content", nullable = false)
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Column(name="create_date", nullable = false)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name="create_by", nullable = false)
    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }
}
