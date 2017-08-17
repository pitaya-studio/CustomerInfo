package com.trekiz.admin.modules.statisticAnalysis.product.service;

import com.trekiz.admin.common.persistence.BaseDao;

import java.util.List;
import java.util.Map;

/**
 * Created by quauq on 2017/3/8.
 */
public interface EnquiryProductAnalysisService {

    List<Map<String,Object>> getEnquiryProduct(Map<String, String> map);
    String getAllEnquiryProductNum(Map<String, String> map);
}
