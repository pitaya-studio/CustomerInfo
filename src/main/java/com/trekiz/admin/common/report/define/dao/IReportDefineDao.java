package com.trekiz.admin.common.report.define.dao;

import com.trekiz.admin.common.persistence.Page;

import java.util.Map;

/**
 * Created by zzk on 2016/4/25.
 */
public interface IReportDefineDao {

    /**
     * 获取报表定义列表
     * @return
     */
    Page<Map<String, Object>> getReportDefineList(Page<Map<String, Object>> pageParam);

    /**
     * 删除报表定义
     * @param id
     */
    void deleteReportDefine(Long id);
}
