package com.trekiz.admin.modules.finance.repository;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.common.persistence.Page;

import java.util.List;
import java.util.Map;

/**
 * Created by quauq on 2016/7/14.
 */
public interface IFinanceCommonDao extends BaseDao {

    public Page getSalesPerformance(Page page, Map<String,Object> params);

    public List<Map<String,Object>> getSalesPerformance(Map<String,Object> params);

    public Map<Object,String> getUserNameDept(Integer userId);

    public List<Map<String,Object>> getCurrencyAmount(String serials);

}
