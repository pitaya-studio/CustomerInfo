package com.trekiz.admin.modules.statisticAnalysis.product.dao.impl;

import com.trekiz.admin.common.persistence.BaseDaoImpl;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.statisticAnalysis.product.dao.IProductAnalysisDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品分析DAO接口实现
 *
 * @author mbmr
 * @date 2016-12-22
 */
@Repository
public class IProductAnalysisDaoImpl<T> extends BaseDaoImpl<T> implements IProductAnalysisDao<T> {

    public <T> Page<T> findPageListBySql(Page<T> page, String sqlString) {
        page=this.findPageBySql(page, sqlString,Map.class);
        return page;
    }

    public Map<String,Object> findSummaryDataBySql(String sql){
        Map<String,Object> map=new HashMap<String,Object>();
        Query query = getSession().createSQLQuery(sql);
        List  list=query.list();
        if(list.size()>0){
           for(int i=0;i<list.size();i++){
               map.put("orderTotalNum",((Object[])list.get(i))[0]);
               map.put("orderTotalPersonNum",((Object[])list.get(i))[1]);
               map.put("orderTotalMoney",((Object[])list.get(i))[2]);
           }
            return map;
        }
        return  null;
    }



}
