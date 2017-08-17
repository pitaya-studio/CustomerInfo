package com.trekiz.admin.modules.statisticAnalysis.product.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.statisticAnalysis.common.ExportExcelUtils;
import com.trekiz.admin.modules.statisticAnalysis.common.ObtainDetailStatisticsExcelUtils;
import com.trekiz.admin.modules.statisticAnalysis.product.service.IProductAnalysisServiceTwo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 产品分析控制器
 *
 * @author mbmr
 * @date 2016-12-22
 */
@Controller
@RequestMapping("${adminPath}/statisticOrder/productList")
public class ProductAnalysisTwoController extends BaseController {

    @Autowired
    private IProductAnalysisServiceTwo iProductAnalysisServiceTwo;

    @RequestMapping(value = "/list")
    @ResponseBody
    public String productList(HttpServletRequest request, HttpServletResponse response) {


        Map<String, Object> resultMap = new HashMap<>();

        Page<Map<String, Object>> pageForProductData = new Page<Map<String, Object>>(request, response);
        
        String pageNo = "";
        String pageSize = "";
        JSONObject jsonObject = null;
        String params = request.getParameter("param");
        if (StringUtils.isNotBlank(params)) {
            jsonObject = JSONObject.parseObject(params);
            pageNo = jsonObject.getString("pageNo");//当前页
            pageSize = jsonObject.getString("pageSize");//每页显示条数
        }

        if (StringUtils.isNotBlank(pageNo)) {
        	pageForProductData.setPageNo(Integer.parseInt(pageNo));
        }
        if (StringUtils.isNotBlank(pageSize)) {
        	pageForProductData.setPageSize(Integer.parseInt(pageSize));
        }

        Page<Map<String, Object>> pageList = iProductAnalysisServiceTwo.getOrderDataStatisticsList(pageForProductData, request, response);


        if (pageList == null) {
            resultMap.put("list", "");
            resultMap.put("count", "");
        } else {
            resultMap.put("list", pageList.getList());
            resultMap.put("count", pageList.getCount());
        }


        return JSON.toJSONString(resultMap);
    }

    /**
     * 导出Excel
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/exportExcel")
    public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Map<String, Object>> dataMapList;
        Map<String, Object> map = new HashMap<>();
        String params = request.getParameter("param");
        if (StringUtils.isNotBlank(params)) {
            JSONObject jsonObject = JSONObject.parseObject(params);
            map.put("startDate", jsonObject.getString("startDate") == null ? "" : jsonObject.getString("startDate"));
            map.put("searchDate", jsonObject.getString("searchDate") == null ? "" : jsonObject.getString("searchDate"));
            map.put("endDate", jsonObject.getString("endDate") == null ? "" : jsonObject.getString("endDate"));
        }

        //Excel表名称
        String fileName = ObtainDetailStatisticsExcelUtils.getExcelFileName(map, "产品统计");
        // 当导出数据超出65535行时,打包下载,下面命名zip压缩包的中文名称部分
     	String zipChineseName = "产品统计";
        Page<Map<String, Object>> pageForProductData = new Page<Map<String, Object>>(request, response);
        pageForProductData.setPageNo(1);
        pageForProductData.setMaxSize(Integer.MAX_VALUE);

        Page<Map<String, Object>> pageList = iProductAnalysisServiceTwo.getOrderDataStatisticsList(pageForProductData, request, response);


        if (pageList == null) {
            //统计表数据
            dataMapList = null;
        } else {
            //统计表数据
            dataMapList = pageList.getList();
        }

        String[] secondTitle = new String[]{"排名", "产品名称", "订单数", "收客人数" , "收款金额", "询单数"};
        // 每个Map<String, Object>中的所有键
     	String[]  commonName = {"rankNum","analysisTypeName","orderNum","orderPersonNum","orderMoney","orderPreNum"};
     		
     	//下载
     	ExportExcelUtils.downLoadFile(fileName, zipChineseName, secondTitle, commonName, dataMapList, request, response);

    }


}
