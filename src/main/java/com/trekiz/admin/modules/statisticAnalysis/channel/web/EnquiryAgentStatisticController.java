/**
 * EnquiryAgentStatistic.java
 */
package com.trekiz.admin.modules.statisticAnalysis.channel.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.statisticAnalysis.channel.service.AgentDetailService;
import com.trekiz.admin.modules.statisticAnalysis.channel.service.EnquiryAgentStatisticService;
import com.trekiz.admin.modules.statisticAnalysis.common.ExportExcelUtils;
import com.trekiz.admin.modules.statisticAnalysis.common.GetEachCycleFirstDay;
import com.trekiz.admin.modules.statisticAnalysis.common.ObtainDetailStatisticsExcelUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;


/**
 * @author junhao.zhao
 *
 * 2017年3月8日  上午10:38:19
 */
@Controller
@RequestMapping(value = "${adminPath}/enquiry/agent/statistic")
public class EnquiryAgentStatisticController {
	@Autowired
	private EnquiryAgentStatisticService enquiryAgentStatisticService;
	@Autowired
	private AgentDetailService agentDetailService;
	
	/**首页询单渠道占比图
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "agentPercentChart", method = RequestMethod.POST)
	public String agentPercentChart(HttpServletRequest request, HttpServletResponse response){
		//将前台接收到的json数据封装进map
		Map<String, Object> returnMap = getParamMap(request);
		//该用户所属公司
		Long companyId = UserUtils.getUser().getCompany().getId();
		//区分询单总览与订单总览
		Object overViewObject = returnMap.get("overView");
		List<Map<String, Object>> listForAgentPercentChart = Lists.newArrayList();
		//是询单总览
		if(Context.ORDER_DATA_STATISTICS_ENQUIRY_OVERVIEW.equals(overViewObject)){
			//根据条件查询前五条数据与“其他”数据
			listForAgentPercentChart = enquiryAgentStatisticService.getListForAgentPercentChart(returnMap, companyId);
		}
		//是订单总览
		if(Context.ORDER_DATA_STATISTICS_ORDER_OVERVIEW.equals(overViewObject)){
			//根据条件查询前五条数据与“其他”数据
			listForAgentPercentChart = enquiryAgentStatisticService.getListForOrderPercentChart(returnMap, companyId);
		}
		//封装返回的数据	
		Map<String,Object> mapReturn = new HashMap<String,Object>();
		mapReturn.put("list", listForAgentPercentChart);
		String stringReturn = JSON.toJSONString(mapReturn);
		return stringReturn;
	}
	
	/**将前台接收到的json数据拆开，封装到map中
	 * @param request
	 * @return
	 */
	private Map<String,Object> getParamMap(HttpServletRequest request){
		Map<String,Object> returnMap = new HashMap<String, Object>();
		String param = request.getParameter("param");
		JSONObject paramObject = JSON.parseObject(param);
		if(null != paramObject ){
			if(StringUtils.isNotBlank(paramObject.getString("overView"))){
				returnMap.put("overView", paramObject.getString("overView"));//dd：订单总览  xd：询单总览
			}
			if(StringUtils.isNotBlank(paramObject.getString("analysisType"))){
				returnMap.put("analysisType", paramObject.getString("analysisType"));//分析类型 1：订单数，2：收客人数，3：订单金额
			}
			if(StringUtils.isNotBlank(paramObject.getString("searchDate"))){
				String searchDateType = paramObject.getString("searchDate");//时间类型  
				String searchDate = "";
				//1：今日 -1：昨日 3：本月 -3：上月 4：本年 -4：去年  5：全部
				switch (searchDateType) {
	            case Context.ORDER_DATA_STATISTICS_TODAY:
					searchDate = GetEachCycleFirstDay.getThisDaySimple();
					break;
	            case Context.ORDER_DATA_STATISTICS_YESTERDAY:
					searchDate = GetEachCycleFirstDay.getYesterdaySimple();
					break;
	            case Context.ORDER_DATA_STATISTICS_MONTH:
	            	searchDate = GetEachCycleFirstDay.getThisMonthSimple();
					break;
	            case Context.ORDER_DATA_STATISTICS_LAST_MONTH:
	            	searchDate = GetEachCycleFirstDay.getLastMonthSimple();
					break;
	            case Context.ORDER_DATA_STATISTICS_YEAR:
	            	searchDate = GetEachCycleFirstDay.getThisYearSimple();
					break;
	            case Context.ORDER_DATA_STATISTICS_LAST_YEAR:
	            	searchDate = GetEachCycleFirstDay.getLastYearSimple();
					break;
	            default:
	            	searchDate = "";
				}
				
				returnMap.put("searchDate", searchDateType);//时间类型
				returnMap.put("searchDateSimple",searchDate);//精简后的时间
			}
			if(StringUtils.isNotBlank(paramObject.getString("startDate"))){
				returnMap.put("startDate", paramObject.getString("startDate"));//自定义开始时间
			}
			if(StringUtils.isNotBlank(paramObject.getString("endDate"))){
				returnMap.put("endDate", paramObject.getString("endDate"));//自定义结束时间
			}
			
		}
		return returnMap;
	}
	
	/**
	 * 渠道详情页控制器
	 * @return json格式数据
	 * @author yang.wang
	 * @date 2017.03.08
	 * */
	@ResponseBody
	@RequestMapping(value = "agentDetailList", method = RequestMethod.POST)
	public String agentDetailList(HttpServletRequest request, HttpServletResponse response) {
		// 获取参数
		Map<String, Object> returnMap = getParamMap4Detail(request);
		Page<Map<String, Object>> page = new Page<>(request, response);
		// 设置分页
		String pageNo = (String) returnMap.get("pageNo");		// 当前页
		if (StringUtils.isNotBlank(pageNo)) {
			page.setPageNo(Integer.parseInt(pageNo));
		}
		String pageSize = (String) returnMap.get("pageSize");	// 页面大小
		if (StringUtils.isNotBlank(pageSize)) {
			page.setPageSize(Integer.parseInt(pageSize));
		}
		
		page = agentDetailService.getAgentDetailList(page, returnMap);
		// 获取列表总条数
		returnMap.put("count", page.getCount());
		// 获取列表内容
		returnMap.put("list", page.getList());
		return JSON.toJSONString(returnMap);
	}
	
	/**
	 * 渠道详情页excel导出
	 * @author yang.wang
	 * @date 2017.3.10
	 * */
	@RequestMapping(value = "exportAgentDetailToExcel")
	public void exportAgentDetailToExcel(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String, Object> returnMap = getParamMap4Detail(request);
		try {
		// excel文件名
		String fileName = ObtainDetailStatisticsExcelUtils.getExcelFileName(returnMap, "渠道统计");
		// 当导出数据超出65535行时,打包下载,下面命名zip压缩包的中文名称部分
		String zipChineseName = "渠道统计";
		// 表头
		String[] secondTitle = {"排名", "渠道名称", "订单数量（单）", "收客人数（人）", "收客金额（元）", "询单数（单）"};
		
		Page<Map<String, Object>> page = new Page<>(request, response);
		// 取消原Page对象的分页限制
		page.setPageNo(1);
		page.setMaxSize(Integer.MAX_VALUE);
		page = agentDetailService.getAgentDetailList(page, returnMap);
		
		// 获取要下载的所有数据,形式:List<Map<String, Object>>
		List<Map<String, Object>> commonList = page.getList();
		// 每个Map<String, Object>中的所有键
		String[]  commonName = {"rankNum","analysisTypeName","orderNum","orderPersonNum","orderMoney","orderPreNum"};
		
		//下载
		ExportExcelUtils.downLoadFile(fileName, zipChineseName, secondTitle, commonName, commonList, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 渠道详情页获取参数
	 * @author yang.wang
	 * @date 2017.03.08
	 * */
	private Map<String, Object> getParamMap4Detail(HttpServletRequest request) {
		Map<String, Object> returnMap = new HashMap<>();
		String param = request.getParameter("param");
		JSONObject paramObject = JSON.parseObject(param);
		
		if (null != paramObject) {
			// dd订单总览  xd询单总览
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
			// 排序  	1：订单总数降序		3：订单金额降序		5：收客人数降序 		7：询单数降序
			//	   	2：订单总数升序		4：订单金额升序  	6：收客人数升序 		8：询单数升序
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
			// 详情页类型   渠道1
			if (StringUtils.isNotBlank(paramObject.getString("pageTab"))) {
				returnMap.put("pageTab", "1"); //渠道
			}
			// 分析类型，-1：代表询单1：订单数2：收客人数3：订单金额
			if (StringUtils.isNotBlank(paramObject.getString("analysisType"))) {
				returnMap.put("analysisType", paramObject.getString("analysisType")); 
			}
		}
		return returnMap;
	}
	
}
