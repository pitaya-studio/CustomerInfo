package com.trekiz.admin.modules.statisticAnalysis.sale.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.trekiz.admin.common.config.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.modules.statisticAnalysis.sale.dao.StatisticSaleDao;
import com.trekiz.admin.modules.statisticAnalysis.sale.json.SaleTopJsonBean;
import com.trekiz.admin.modules.statisticAnalysis.sale.pojo.SaleAnalysisParam;
import com.trekiz.admin.modules.statisticAnalysis.sale.pojo.SaleTopParamDTO;
import com.trekiz.admin.modules.statisticAnalysis.sale.service.StatisticSaleService;

/**
 * 数据统计分析2.0 销售查询Service层接口实现
 */
@Service
public class StatisticSaleServiceImpl implements StatisticSaleService{

    @Autowired
    private StatisticSaleDao statisticSaleDao;

    /**
     * 根据请求参数，返回符合条件的前五名的询单数据以及相关的其他信息。组装成对象返回。
     * @param dto 销售排名模块请求参数的数据传输对象
     * @return
     * @author yudong.xu 2017.3.8
     */
    @Override
    public SaleTopJsonBean getSaleTop4Ask(SaleTopParamDTO dto) {
        BigDecimal saleSum = statisticSaleDao.getSaleSum(dto);
        BigDecimal allAskNum = statisticSaleDao.getAllNum4Ask(dto);
        List<Map<String, Object>> list = statisticSaleDao.getSaleTop4Ask(dto);

        // 其他销售询单数 = 总询单数 - 前五名总询单
        BigDecimal topTotal = BigDecimal.ZERO;
        for (Map<String, Object> map : list) {
            topTotal = topTotal.add(new BigDecimal(map.get("num").toString()));
        }
        BigDecimal otherTotal = allAskNum.subtract(topTotal);

        SaleTopJsonBean json = new SaleTopJsonBean(list, otherTotal.toString(), saleSum.intValue(), null);
        return json;
    }

    /**
     * 查询由询单产生的订单数量前五名的销售信息。
     * @param dto
     * @return
     * @author yudong.xu 2017.3.8
     */
    @Override
    public SaleTopJsonBean getSaleTop4Order(SaleTopParamDTO dto) {
        BigDecimal saleSum = statisticSaleDao.getSaleSum(dto);
        BigDecimal allNum = statisticSaleDao.getAllNum4Order(dto);
        List<Map<String, Object>> list = statisticSaleDao.getSaleTop4Order(dto);

        // 其他销售询单数 = 总询单数 - 前五名总询单
        BigDecimal topTotal = BigDecimal.ZERO;
        for (Map<String, Object> map : list) {
            topTotal = topTotal.add(new BigDecimal(map.get("num").toString()));
        }
        BigDecimal otherTotal = allNum.subtract(topTotal);

        SaleTopJsonBean json = new SaleTopJsonBean(list, otherTotal.toString(), saleSum.intValue(), null);
        return json;
    }

	@Override
	public  List<Map<String,Object>> getSaleDetailList(SaleAnalysisParam saleAnalysisParam) {
		return statisticSaleDao.getSaleDetailList(saleAnalysisParam);
	}

	@Override
	public String getSaleDetailListCount(SaleAnalysisParam saleAnalysisParam) {
		return statisticSaleDao.getSaleDetailListCount(saleAnalysisParam);
	}
}