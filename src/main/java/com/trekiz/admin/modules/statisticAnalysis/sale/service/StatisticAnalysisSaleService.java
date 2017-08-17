package com.trekiz.admin.modules.statisticAnalysis.sale.service;

import com.trekiz.admin.modules.statisticAnalysis.sale.json.SaleTopJsonBean;
import com.trekiz.admin.modules.statisticAnalysis.sale.pojo.SaleParamBean;

import java.util.Map;

/**
 * Created by quauq on 2016/12/22.
 */
public interface StatisticAnalysisSaleService {

    /**
     * 获取指定领域的前多少名的销售。
     * @param paramBean 前端请求参数的封装
     * @param topNum 指定前topNum名的销售
     * @author yudong.xu 2016.12.22
     */
    public SaleTopJsonBean getSaleTop(SaleParamBean paramBean, Integer topNum);

    /**
     * 获取销售统计详情信息,包括销售列表和汇总信息
     * @param paramBean
     */
    public Map<String,Object> getSaleStatisticInfo(SaleParamBean paramBean);

}
