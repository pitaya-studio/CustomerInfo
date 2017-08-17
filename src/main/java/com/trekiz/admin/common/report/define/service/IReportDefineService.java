package com.trekiz.admin.common.report.define.service;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.report.define.entity.ReportDefine;

import java.util.List;
import java.util.Map;

/**
 * Created by zzk on 2016/4/25.
 */

public interface IReportDefineService {

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

    /**
     * 根据id获取ReportDefine
     * @param id
     * @return
     */
    ReportDefine getReportDefineById(Long id);

    /**
     * 保存或更新ReportDefine
     * @param reportDefine
     */
    void saveOrUpdateReportDefine(ReportDefine reportDefine);

    /**
     * 预览数据
     * @param sqls
     */
    List<List<Map<String, Object>>> preview(String sqls);
}
