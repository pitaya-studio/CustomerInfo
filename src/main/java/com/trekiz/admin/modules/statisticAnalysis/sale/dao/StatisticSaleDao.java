package com.trekiz.admin.modules.statisticAnalysis.sale.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.persistence.BaseDao;
import com.trekiz.admin.modules.statisticAnalysis.sale.pojo.SaleAnalysisParam;
import com.trekiz.admin.modules.statisticAnalysis.sale.pojo.SaleTopParamDTO;

/**
 * 数据统计分析2.0 销售查询Dao
 */
public interface StatisticSaleDao extends BaseDao {

    /**
     * 在当前用户所属公司中，询单请求：查询满足 未删除状态并且拥有t1下单权限的 或者 已删除状态但是询单记录中有被询过单的 用户人数。
     * 订单请求则已删除用户需满足有询单产生订单的才统计。
     * @return 返回满足条件销售个数。
     * @author yudong.xu 2017.3.8
     */
    public BigDecimal getSaleSum(SaleTopParamDTO dto);

    /**
     * 查询当前用户所属公司中，满足询单请求参数dto的询单记录数。
     * @param dto 销售排名模块请求参数的数据传输对象
     * @return 返回满足条件的询单总数。
     * @author yudong.xu 2017.3.8
     */
    public BigDecimal getAllNum4Ask(SaleTopParamDTO dto);

    /**
     * 订单总览中，查询该公司下的满足查询条件的订单数/收客人数/订单金额的总值。
     * @param dto
     * @return
     * @author yudong.xu 2017.3.9
     */
    public BigDecimal getAllNum4Order(SaleTopParamDTO dto);

    /**
     * 在用户所属公司中，根据询单请求参数查询询单记录，返回被询单数最大的前5名销售人员信息。包括：销售id，销售名称，询单数。
     * @param dto 销售排名模块请求参数的数据传输对象
     * @return 返回销售前五名的list数据
     * @author yudong.xu 2017.3.8
     */
    public List<Map<String,Object>> getSaleTop4Ask(SaleTopParamDTO dto);

    /**
     * 订单总览页面，查询订单数或订单收客人数或订单金额前五名的销售信息。
     * @param dto
     * @return
     * @author yudong.xu 2017.3.8
     */
    public List<Map<String,Object>> getSaleTop4Order(SaleTopParamDTO dto);
    
    /**
     * 获取销售统计详情信息列表(带询单字段)
     * @author gaoyang
     * @Time 2017-3-8 下午2:18:56
     * @param saleAnalysisParam
     * @return
     */
    public  List<Map<String,Object>> getSaleDetailList(SaleAnalysisParam saleAnalysisParam);
    
    /**
     * 获取销售统计详情页列表数据总数(带询单字段)
     * @author gaoyang
     * @Time 2017-3-8 下午2:18:56
     * @param saleAnalysisParam
     * @return
     */
    public String getSaleDetailListCount(SaleAnalysisParam saleAnalysisParam);

}
