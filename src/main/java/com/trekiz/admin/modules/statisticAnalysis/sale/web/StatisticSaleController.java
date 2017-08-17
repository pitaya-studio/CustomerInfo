package com.trekiz.admin.modules.statisticAnalysis.sale.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.statisticAnalysis.common.ExportExcelUtils;
import com.trekiz.admin.modules.statisticAnalysis.common.ObtainDetailStatisticsExcelUtils;
import com.trekiz.admin.modules.statisticAnalysis.sale.json.SaleTopJsonBean;
import com.trekiz.admin.modules.statisticAnalysis.sale.pojo.SaleAnalysisParam;
import com.trekiz.admin.modules.statisticAnalysis.sale.pojo.SaleTopParamDTO;
import com.trekiz.admin.modules.statisticAnalysis.sale.service.StatisticSaleService;

/**
 *  数据统计分析2.0 销售查询Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/statisticSale")
public class StatisticSaleController {

    @Autowired
    private StatisticSaleService statisticSaleService;

    /**
     * 查询询单或订单前五名的销售数据
     * @param dto
     * @return
     * @author yudong.xu 2017.3.8
     */
    @ResponseBody
    @RequestMapping(value = "getSaleTop")
    public SaleTopJsonBean getSaleTop(SaleTopParamDTO dto) {
        SaleTopJsonBean jsonBean = null;
        try {
            if (Context.ORDER_DATA_STATISTICS_ENQUIRY_OVERVIEW.equals(dto.getOverView())) {
                jsonBean = statisticSaleService.getSaleTop4Ask(dto);
            } else if (Context.ORDER_DATA_STATISTICS_ORDER_OVERVIEW.equals(dto.getOverView())) {
                jsonBean = statisticSaleService.getSaleTop4Order(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            jsonBean = new SaleTopJsonBean();
        }
        return jsonBean;
    }
	
	/**
	 * 获取销售统计详情页数据(带询单)
	 * @author gaoyang
	 * @Time 2017-3-9 下午8:25:12
	 * @param request
	 * @return Map<String,Object>
	 */
	@ResponseBody
    @RequestMapping(value = "saleDetailList")
    public Map<String,Object> getSaleDetailList(HttpServletRequest request) {
        String paramJson = request.getParameter("param");
        // 拼装返回数据
        Map<String, Object> resultMap = null;
        try {
        	SaleAnalysisParam saleAnalysisParam = JSON.parseObject(paramJson, SaleAnalysisParam.class);
        	List<Map<String,Object>> list = statisticSaleService.getSaleDetailList(saleAnalysisParam);
        	String count = statisticSaleService.getSaleDetailListCount(saleAnalysisParam);
        	resultMap = getParamMap4Detail(request);
            resultMap.put("list", list); // 添加列表数据
            resultMap.put("count", count); // 分页总条数
        } catch (Exception e) {
        	e.printStackTrace();
        }

        return resultMap;
    }

	/**
	 * 回传页面参数
	 * @author 
	 * @Time 2017-3-9 下午8:26:06
	 * @param request
	 * @return Map<String, Object>
	 */
	private Map<String, Object> getParamMap4Detail(HttpServletRequest request) {
	    Map<String, Object> returnMap = new HashMap<String, Object>();
	    String param = request.getParameter("param");
	    JSONObject paramObject = JSON.parseObject(param);
	    if (null != paramObject) {
		    // dd:订单总览 xd:询单总览
			if (StringUtils.isNotBlank(paramObject.getString("overView"))) {
				returnMap.put("overView", paramObject.getString("overView")); 
			}
			// 时间  1：今日  -1：昨日  3：本月  -3：上月  4：本年  -4：去年    5：全部 
			if (StringUtils.isNotBlank(paramObject.getString("searchDate"))) {
				returnMap.put("searchDate", paramObject.getString("searchDate"));
			}
			// 自定义开始时间
			if (StringUtils.isNotBlank(paramObject.getString("startDate"))) {
				returnMap.put("startDate", paramObject.getString("startDate"));
			}
			// 自定义结束时间
			if (StringUtils.isNotBlank(paramObject.getString("endDate"))) {
				returnMap.put("endDate", paramObject.getString("endDate"));
			}
			// 搜索框值
			if (StringUtils.isNotBlank(paramObject.getString("searchValue"))) {
				returnMap.put("searchValue", paramObject.getString("searchValue"));
			}
			// 排序
			// 1：订单总数降序3：订单金额降序5：收客人数降序 7：询单数降序
			// 2：订单总数升序4：订单金额升序6：收客人数升序 8：询单数升序
			if (StringUtils.isNotBlank(paramObject.getString("orderBy"))) {
				returnMap.put("orderBy", paramObject.getString("orderBy"));
			}
			// 订单类型
			if (StringUtils.isNotBlank(paramObject.getString("orderType"))) {
				returnMap.put("orderType", paramObject.getString("orderType"));
			}
			// 当前页数
			if (StringUtils.isNotBlank(paramObject.getString("pageNo"))) {
				returnMap.put("pageNo", paramObject.getString("pageNo"));
			}
			// 页面大小
			if (StringUtils.isNotBlank(paramObject.getString("pageSize"))) {
				returnMap.put("pageSize", paramObject.getString("pageSize"));
			}
			// 详情页类型
			if (StringUtils.isNotBlank(paramObject.getString("pageTab"))) {
				returnMap.put("pageTab", "2"); // 1：渠道  2：销售  3：产品
			}
			// 分析类型，-1：代表询单1：订单数2：收客人数3：订单金额
			if (StringUtils.isNotBlank(paramObject.getString("analysisType"))) {
				returnMap.put("analysisType", paramObject.getString("analysisType"));
			}
	    }
	    
	    return returnMap;
	}
	
	/**
	 * 获取销售统计详情页Excel列表
	 * @author gaoyang
	 * @Time 2017-3-13 上午10:30:21
	 * @param
	 */
	@RequestMapping(value = "getSaleDetailListExcel")
	public void getSaleDetailListExcel(HttpServletRequest request, HttpServletResponse response) {
		String paramJson = request.getParameter("param");
        try {
        	SaleAnalysisParam saleAnalysisParam = JSON.parseObject(paramJson, SaleAnalysisParam.class);
        	saleAnalysisParam.setPageNo("1");
        	saleAnalysisParam.setPageSize(String.valueOf(Integer.MAX_VALUE));
        	// 获取销售统计详情页列表查询结果
        	List<Map<String, Object>> saleList = statisticSaleService.getSaleDetailList(saleAnalysisParam);
			
			Map<String, Object> mapParam = new HashMap<String, Object>();
			mapParam.put("searchDate", saleAnalysisParam.getSearchDate());
			mapParam.put("startDate", saleAnalysisParam.getStartDate());
			mapParam.put("endDate", saleAnalysisParam.getEndDate());
			// 文件名称
			String fileName = ObtainDetailStatisticsExcelUtils.getExcelFileName(mapParam, "销售统计");
			
			// 当导出数据超出65535行时,打包下载,下面命名zip压缩包的中文名称部分
			String zipChineseName = "销售统计";
			// 表头数据
			String[] secondTitle = new String[]{"排名", "销售名称", "订单数量（单）", "收客人数（人）", "订单金额（元）", "询单数量（单）"};
			
			// 每个Map<String, Object>中的所有键
			String[]  commonName = {"rankNum","analysisTypeName","orderNum","orderPersonNum","orderMoney","orderPreNum"};
			
			//下载
			ExportExcelUtils.downLoadFile(fileName, zipChineseName, secondTitle, commonName, saleList, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}