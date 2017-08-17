package com.trekiz.admin.modules.statisticAnalysis.sale.service.impl;

import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.statisticAnalysis.sale.dao.StatisticAnalysisSaleDao;
import com.trekiz.admin.modules.statisticAnalysis.sale.json.SaleTopJsonBean;
import com.trekiz.admin.modules.statisticAnalysis.sale.pojo.SaleParamBean;
import com.trekiz.admin.modules.statisticAnalysis.sale.service.StatisticAnalysisSaleService;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 统计分析模块的销售分析方面的Service实现类。
 */
@Service
public class StatisticAnalysisSaleServiceImpl implements StatisticAnalysisSaleService{

    @Autowired
    private StatisticAnalysisSaleDao analysisSaleDao;
    @Autowired
    private UserDao userDao;


    /**
     * 获取指定领域的前多少名的销售。
     * @param paramBean 前端请求参数的封装
     * @param topNum 指定前topNum名的销售
     * @author yudong.xu 2016.12.22
     */
    public SaleTopJsonBean getSaleTop(SaleParamBean paramBean, Integer topNum){
        // 查询该公司下的职务为销售和销售主管的用户id。
        String uuid = UserUtils.getUser().getCompany().getUuid();
        String[] arr = {"销售","销售主管"};
        List<Integer> saleIds = userDao.getUserIdByJobNameArr(uuid,arr);
        if (saleIds == null || saleIds.size() == 0){
            return new SaleTopJsonBean();
        }

        List<Map<String,Object>> topList = analysisSaleDao.getSaleTop(paramBean,saleIds,topNum);

        DecimalFormat pointZeroDf = new DecimalFormat("###,###,###");
        DecimalFormat pointTwoDf = new DecimalFormat("###,###,##0.00");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String serverTime = dateFormat.format(new Date());

        String otherTotalStr; // 格式化的其他销售总额的字符串
        if (saleIds.size() <= topNum){ // 如果该公司的销售人数很少，小于topNum，则otherTotal的值为0。
            // 为3表示金额，需要保留两位小数点。
            otherTotalStr = paramBean.getAnalysisType() == 3 ? "0.00" : "0";
            return new SaleTopJsonBean(topList, otherTotalStr, saleIds.size(), serverTime);
        }

        BigDecimal allTotal = analysisSaleDao.getSingleTotalInfo(paramBean,saleIds); // 统计全体销售的总额
        BigDecimal topTotal = BigDecimal.ZERO;
        for (Map<String, Object> map : topList) { // 统计前topNum名的总额
            topTotal = topTotal.add(new BigDecimal(map.get("saleNum").toString()));
        }

        BigDecimal otherTotal = allTotal.subtract(topTotal);
        otherTotalStr = paramBean.getAnalysisType() == 3 ? pointTwoDf.format(otherTotal) : pointZeroDf.format(otherTotal);

        return new SaleTopJsonBean(topList, otherTotalStr, saleIds.size(), serverTime);
    }

    /**
     * 获取销售统计详情信息
     * @param paramBean
     */
    public Map<String,Object> getSaleStatisticInfo(SaleParamBean paramBean){
       return analysisSaleDao.getSaleStatisticInfo(paramBean);
    }

}
