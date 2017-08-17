package com.trekiz.admin.modules.statisticAnalysis.sale.dao;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.statisticAnalysis.sale.pojo.SaleParamBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 统计分析模块，销售统计分析
 */
public interface StatisticAnalysisSaleDao extends BaseDao {

    /**
     * 获取指定领域的前多少名的销售。
     * @param paramBean 前端请求参数的封装
     * @param saleIds 给定销售的id
     * @param topNum 指定前topNum名的销售
     * @author yudong.xu 2016.12.22
     */
    public List<Map<String,Object>> getSaleTop(SaleParamBean paramBean,List<Integer> saleIds, Integer topNum);

    /**
     * 获取满足请求参数条件下的单一方面总数的统计，即订单总数，收客总数，订单总金额当中的其一。
     * @param paramBean
     * @param saleIds
     * @author yudong.xu 2016.12.22
     */
    public BigDecimal getSingleTotalInfo(SaleParamBean paramBean,List<Integer> saleIds);

    /**
     * 获取销售统计详情信息
     * @param paramBean
     */
    public Map<String,Object> getSaleStatisticInfo(SaleParamBean paramBean);



}
