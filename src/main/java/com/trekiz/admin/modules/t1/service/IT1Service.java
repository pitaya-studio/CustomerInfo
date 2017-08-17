package com.trekiz.admin.modules.t1.service;

import java.util.List;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import com.trekiz.admin.common.persistence.Page;

/**
 * Created by zzk on 2016/10/9.
 */
public interface IT1Service {
    List<Map<String, Object>> getT1LogoList(String tourOutIn, String tourDistrictId, String flag);

    List<Map<String, Object>> getSuppliers(String tourDistrictId, String tourOutIn);

    Page<Map<String,Object>> queryList(HttpServletRequest request, Page<Map<String,Object>> page, Map<String, Object> paramsMap);
    
    /**
     * t1首页产品产品列表
     * @param request
     * @param page
     * @param paramsMap
     * @return
     */
    public Page<Map<String,Object>> queryActivityList(HttpServletRequest request, Page<Map<String,Object>> page, Map<String, Object> paramsMap);

}
