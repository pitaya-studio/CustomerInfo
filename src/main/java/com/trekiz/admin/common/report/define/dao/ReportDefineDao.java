package com.trekiz.admin.common.report.define.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.report.define.entity.ReportDefine;

public interface ReportDefineDao extends ReportDefineDaoCustom, CrudRepository<ReportDefine, Long> {

    @Query("from ReportDefine where id=?1 and delFlag = " + Context.DEL_FLAG_NORMAL)
    public ReportDefine getReportDefineById(Long id);

}

interface ReportDefineDaoCustom extends BaseDao<ReportDefine> {
}