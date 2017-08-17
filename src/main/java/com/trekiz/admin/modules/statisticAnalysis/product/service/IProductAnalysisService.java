package com.trekiz.admin.modules.statisticAnalysis.product.service;

import com.trekiz.admin.common.persistence.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 产品分析业务接口
 * @author mbmr
 * @date 2016-12-22
 */
@Service
public interface IProductAnalysisService {

    /**
     * 获取产品分析列表详情
     * @param page
     * @param request
     * @param response
     * @return
     */
    public Page<Map<String, Object>> getOrderDataStatisticsList(Page<Map<String, Object>> page,HttpServletRequest request, HttpServletResponse response);

    /**
     *获取有关产品列表的合计总数
     * @param request
     * @param response
     * @return
     */
    public Map<String,Object> getSummaryData(HttpServletRequest request, HttpServletResponse response);
}
