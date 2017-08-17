package com.trekiz.admin.common.report.define.service.impl;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.report.define.dao.IReportDefineDao;
import com.trekiz.admin.common.report.define.dao.ReportDefineDao;
import com.trekiz.admin.common.report.define.entity.ReportDefine;
import com.trekiz.admin.common.report.define.service.IReportDefineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zzk on 2016/4/25.
 */
@Service
public class ReportDefineServiceImpl implements IReportDefineService {

    @Autowired
    private IReportDefineDao reportDefineDao;
    @Autowired
    private ReportDefineDao reportDefineDao2;

    /**
     * 获取报表定义列表
     *
     * @return
     */
    @Override
    public Page<Map<String, Object>> getReportDefineList(Page<Map<String, Object>> pageParam) {
        return reportDefineDao.getReportDefineList(pageParam);
    }

    /**
     * 删除报表定义
     *
     * @param id
     */
    @Override
    public void deleteReportDefine(Long id) {
        reportDefineDao.deleteReportDefine(id);
    }

    /**
     * @param id
     * @return
     */
    @Override
    public ReportDefine getReportDefineById(Long id) {
        return reportDefineDao2.getReportDefineById(id);
    }

    @Override
    public void saveOrUpdateReportDefine(ReportDefine reportDefine) {
        if (reportDefine.getId() != null) {
            reportDefineDao2.updateObj(reportDefine);
        } else {
            reportDefineDao2.save(reportDefine);
        }
    }

    /**
     * 预览数据
     *
     * @param sqls
     */
    @Override
    public List<List<Map<String, Object>>> preview(String sqls) {
        List<List<Map<String, Object>>> lists = new ArrayList<List<Map<String, Object>>>();
        String[] sqlArr = sqls.split(";");
        for (int i = 0; i < sqlArr.length; i++) {
            List<Map<String, Object>> list = reportDefineDao2.findBySql(sqlArr[i], Map.class);
            lists.add(list);
        }
        return lists;
    }
}
