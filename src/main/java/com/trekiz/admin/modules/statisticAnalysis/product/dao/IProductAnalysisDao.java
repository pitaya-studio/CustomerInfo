package com.trekiz.admin.modules.statisticAnalysis.product.dao;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.Page;

import java.util.Map;

/**
 * 产品分析DAO接口
 * @author
 * @date 2016-12-22
 */
public interface IProductAnalysisDao<T> extends BaseDao<T> {

    /**
     * 查询合计总数（如:订单总数、收客人数总数、收款金额总数）
     * @param sql
     * @return
     */
    public Map<String,Object> findSummaryDataBySql(String sql);

    /**
     * 分页查询产品列表详情
     * @param page
     * @param sql
     * @return
     */
    public <T> Page<T> findPageListBySql(Page<T> page, String sql);
}
