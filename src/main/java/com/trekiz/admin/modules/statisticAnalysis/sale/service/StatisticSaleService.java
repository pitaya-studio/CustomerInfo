package com.trekiz.admin.modules.statisticAnalysis.sale.service;

import java.util.List;
import java.util.Map;

import com.trekiz.admin.modules.statisticAnalysis.sale.json.SaleTopJsonBean;
import com.trekiz.admin.modules.statisticAnalysis.sale.pojo.SaleAnalysisParam;
import com.trekiz.admin.modules.statisticAnalysis.sale.pojo.SaleTopParamDTO;

/**
 * 数据统计分析2.0 销售查询Service层接口
 */
public interface StatisticSaleService {

    /**
     * 根据请求参数，返回符合条件的前五名的询单数据以及相关的其他信息。组装成对象返回。
     * @param dto 销售排名模块请求参数的数据传输对象
     * @return
     * @author yudong.xu 2017.3.8
     */
    public SaleTopJsonBean getSaleTop4Ask(SaleTopParamDTO dto);

    /**
     * 查询由询单产生的订单数量前五名的销售信息。
     * @param dto
     * @return
     * @author yudong.xu 2017.3.8
     */
    public SaleTopJsonBean getSaleTop4Order(SaleTopParamDTO dto);
    
    /**
     * 获取销售详情页信息(带询单)
     * @author gaoyang
     * @Time 2017-3-8 下午3:21:19
     * @param saleAnalysisParam
     * @return Map<String,Object>
     */
    public List<Map<String,Object>> getSaleDetailList(SaleAnalysisParam saleAnalysisParam);
    
    /**
     * 获取销售统计详情页列表数据总数(带询单)
     * @author gaoyang
     * @Time 2017-3-8 下午2:18:56
     * @param saleAnalysisParam
     * @return
     */
    public String getSaleDetailListCount(SaleAnalysisParam saleAnalysisParam);

}
