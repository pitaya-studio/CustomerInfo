package com.trekiz.admin.modules.finance.service;

import java.util.Map;

import com.trekiz.admin.common.persistence.Page;

/**
 * Created by quauq on 2016/7/14.
 */
public interface IFinanceCommonService {

    public void getSalesPerformance(Page<Map<String,Object>> page, Map<String,Object> params);

    public  Map<String,Object> getSalesPerformance( Map<String,Object> params);

}
