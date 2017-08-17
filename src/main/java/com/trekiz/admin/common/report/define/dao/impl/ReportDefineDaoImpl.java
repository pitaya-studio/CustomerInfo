package com.trekiz.admin.common.report.define.dao.impl;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.common.report.define.dao.IReportDefineDao;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by zzk on 2016/4/25.
 */
@Repository
public class ReportDefineDaoImpl extends BaseDaoImpl<Map<String,Object>> implements IReportDefineDao {

    /**
     * 获取报表定义列表
     * @return
     */
    @Override
    public Page<Map<String, Object>> getReportDefineList(Page<Map<String, Object>> pageParam) {
        Long companyId = UserUtils.getUser().getCompany().getId();
        String sql = "select id, name, description, template_id templateId, report_query reportQuery, createBy, createDate, updateDate" +
                " from report_define where company_id = ? and delFlag = " + Context.DEL_FLAG_NORMAL;
        String orderBy = pageParam.getOrderBy();
        if(StringUtils.isBlank(orderBy)) {
            pageParam.setOrderBy("createDate DESC");
        }
        Page<Map<String, Object>> page = findBySql(pageParam, sql, Map.class, companyId);
        return page;
    }

    /**
     * 删除报表定义
     * @param id
     */
    @Override
    public void deleteReportDefine(Long id) {
        String sql = "UPDATE report_define SET delFlag = 1 WHERE id = ?";
        updateBySql(sql, id);
    }
}
