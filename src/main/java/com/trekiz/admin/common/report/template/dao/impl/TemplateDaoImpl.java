package com.trekiz.admin.common.report.template.dao.impl;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.common.report.template.dao.ITemplateDao;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * Created by zzk on 2016/4/25.
 */
@Repository
public class TemplateDaoImpl extends BaseDaoImpl<Map<String,Object>> implements ITemplateDao {

    /**
     * 获取报表定义列表
     * @return
     */
    @Override
    public Page<Map<String, Object>> getReportList() {
        Long companyId = UserUtils.getUser().getCompany().getId();
        String sql = "select id, name, description, template_id templateId, report_query reportQuery from report where company_id = ?";
        Page<Map<String, Object>> page = findBySql(new Page<Map<String, Object>>(null, null), sql, Map.class, companyId);
        return null;
    }
}
