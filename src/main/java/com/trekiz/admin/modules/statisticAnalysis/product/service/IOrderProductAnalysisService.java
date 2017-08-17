package com.trekiz.admin.modules.statisticAnalysis.product.service;

import java.util.List;
import java.util.Map;

/**
 * Created by quauq on 2017/3/13.
 */
public interface IOrderProductAnalysisService {

    List<Map<String, Object>> getOrderProductList(Map map);
    String getAllOrderProductNum(Map<String, String> map);
}
