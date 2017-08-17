package com.trekiz.admin.modules.statisticAnalysis.sale.web;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.modules.statisticAnalysis.sale.json.SaleTopJsonBean;
import com.trekiz.admin.modules.statisticAnalysis.sale.pojo.SaleParamBean;
import com.trekiz.admin.modules.statisticAnalysis.sale.service.StatisticAnalysisSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 统计分析模块的销售方面的Controller。
 */
@Controller
@RequestMapping(value = "${adminPath}/statisticAnalysis/sale")
public class StatisticAnalysisSaleController {

    @Autowired
    private StatisticAnalysisSaleService analysisSaleService;

    /**
     * 获取销售前5名的销售数据
     * @param request
     * @author yudong.xu 2016.12.26
     */
    @ResponseBody
    @RequestMapping(value = "getSaleTop")
    public SaleTopJsonBean getSaleTop(HttpServletRequest request){
        String paramJson = request.getParameter("param");
        SaleTopJsonBean jsonBean = null;
        try {
            SaleParamBean paramBean = JSON.parseObject(paramJson,SaleParamBean.class);
            jsonBean = analysisSaleService.getSaleTop(paramBean,5);
        }catch (Exception e){
            jsonBean = new SaleTopJsonBean();
            e.printStackTrace();
        }finally {
            return jsonBean;
        }
    }

    /**
     * 获取统计分析销售方面的详情页面数据
     * @param request
     * @author yudong.xu 2016.12.26
     */
    @ResponseBody
    @RequestMapping(value = "saleList")
    public Map<String,Object> getSaleList(HttpServletRequest request){
        String paramJson = request.getParameter("param");
        Map<String,Object> result = null;
        try {
            SaleParamBean paramBean = JSON.parseObject(paramJson,SaleParamBean.class);
            result = analysisSaleService.getSaleStatisticInfo(paramBean);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return result;
        }
    }

}
