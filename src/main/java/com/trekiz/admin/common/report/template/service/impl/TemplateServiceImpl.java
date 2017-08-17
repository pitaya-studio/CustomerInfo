package com.trekiz.admin.common.report.template.service.impl;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.report.template.dao.ITemplateDao;
import com.trekiz.admin.common.report.template.service.ITemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by zzk on 2016/4/25.
 */
@Service
public class TemplateServiceImpl implements ITemplateService {

    @Autowired
    private ITemplateDao templateDao;

    /**
     * 获取报表定义列表
     * @return
     */
    @Override
    public Page<Map<String, Object>> getReportList() {
        return templateDao.getReportList();
    }
}
