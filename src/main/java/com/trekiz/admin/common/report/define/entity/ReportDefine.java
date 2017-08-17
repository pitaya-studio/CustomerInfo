package com.trekiz.admin.common.report.define.entity;

import com.trekiz.admin.common.persistence.DataEntity;

import javax.persistence.*;

/**
 * Created by zzk on 2016/4/25.
 */

@Entity
@Table(name = "report_define")
public class ReportDefine extends DataEntity {

    private Long id;
    private String name;
    private String description;
    private Long templateId;
    private String reportQuery;
    private Long companyId;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "template_id")
    public Long getTemplateId() {
        return templateId;
    }

    public void setTemplateId(Long templateId) {
        this.templateId = templateId;
    }

    @Column(name = "report_query")
    public String getReportQuery() {
        return reportQuery;
    }

    public void setReportQuery(String reportQuery) {
        this.reportQuery = reportQuery;
    }

    @Column(name = "company_id")
    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
