package com.trekiz.admin.modules.statisticAnalysis.product.web;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.statisticAnalysis.product.service.IProductAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 产品分析控制器
 *
 * @author mbmr
 * @date 2016-12-22
 */
@Controller
@RequestMapping("${adminPath}/statistic/productList")
public class ProductAnalysisController extends BaseController {

    @Autowired
    private IProductAnalysisService iProductAnalysisService;

    @RequestMapping(value = "/list")
    @ResponseBody
     public String productList(HttpServletRequest request, HttpServletResponse response) {


        Map<String,Object> resultMap=new HashMap<>();

        Page<Map<String, Object>> pageForProductData = new Page<Map<String,Object>>(request, response);

        Page<Map<String, Object>> pageList=iProductAnalysisService.getOrderDataStatisticsList(pageForProductData,request,response);


        if(pageList==null){
            resultMap.put("list","");
            resultMap.put("count","");
        }else{
            resultMap.put("list",pageList.getList());
            resultMap.put("count",pageList.getCount());
        }



        Map<String,Object> map=iProductAnalysisService.getSummaryData(request,response);
        if(map==null){
            resultMap.put("orderTotalNum",0);
            resultMap.put("orderTotalPersonNum",0);
            resultMap.put("orderTotalMoney",0);
        }else{
            resultMap.put("orderTotalNum",map.get("orderTotalNum"));
            resultMap.put("orderTotalPersonNum",map.get("orderTotalPersonNum"));
            resultMap.put("orderTotalMoney","￥"+ map.get("orderTotalMoney"));
        }

       return JSON.toJSONString(resultMap);
    }


}
