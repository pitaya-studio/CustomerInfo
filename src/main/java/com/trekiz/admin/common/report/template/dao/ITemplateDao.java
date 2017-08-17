package com.trekiz.admin.common.report.template.dao;

import com.trekiz.admin.common.persistence.Page;

import java.util.Map;

/**
 * Created by zzk on 2016/4/25.
 */
public interface ITemplateDao {

    /**
     * 获取报表定义列表
     * @return
     */
    Page<Map<String, Object>> getReportList();
}
