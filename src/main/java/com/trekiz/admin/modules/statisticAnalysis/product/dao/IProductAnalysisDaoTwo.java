package com.trekiz.admin.modules.statisticAnalysis.product.dao;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.Page;

/**
 * 产品分析DAO接口
 * @author
 * @date 2016-12-22
 */
public interface IProductAnalysisDaoTwo<T> extends BaseDao<T> {


    /**
     * 分页查询产品列表详情
     * @param page
     * @param sql
     * @return
     */
    public <T> Page<T> findPageListBySql(Page<T> page, String sql);
}
