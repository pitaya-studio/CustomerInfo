package com.trekiz.admin.modules.statisticAnalysis.product.dao.impl;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.statisticAnalysis.product.dao.IProductAnalysisDao;
import com.trekiz.admin.modules.statisticAnalysis.product.dao.IProductAnalysisDaoTwo;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 产品分析DAO接口实现
 *
 * @author mbmr
 * @date 2016-12-22
 */
@Repository
public class IProductAnalysisDaoTwoImpl<T> extends BaseDaoImpl<T> implements IProductAnalysisDaoTwo<T> {

    public <T> Page<T> findPageListBySql(Page<T> page, String sqlString) {
        page=this.findPageBySql(page, sqlString,Map.class);
        return page;
    }



}