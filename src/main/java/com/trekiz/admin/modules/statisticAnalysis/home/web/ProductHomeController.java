package com.trekiz.admin.modules.statisticAnalysis.home.web;

import java.util.Date;
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
import com.ibm.icu.text.SimpleDateFormat;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.modules.statisticAnalysis.home.service.ProductHomeService;

import net.sf.json.JSONArray;

/**
 * @author junhao.zhao
 * 2016年12月28日  下午3:34:58
 */
@Controller
@RequestMapping(value = "${adminPath}/statisticHome", method = RequestMethod.POST)
public class ProductHomeController {
	@Autowired
	private ProductHomeService productHomeService;
	
	/**首页产品展示：按产品线(0:全部 1：单团 2：散拼  3：游学 4：大客户 5：自由行 6：签证 (7：机票) 10：游轮)、
	 * 分析类型（1：订单数，2：收客人数，3：订单金额）、时间 （1：今日 2：本周 3：本月 4：本年 5：全部）这三种条件查询的接口
	 * @param request
	 * @param response
	 * @return 
	 */
	@ResponseBody
	@RequestMapping(value = "productHomeList")
	public String productHomeList(HttpServletRequest request, HttpServletResponse response){
		//将前台接收到的json数据封装进map
		Map<String, Object> returnMap = getParamMap(request);
		//根据条件查询前五条数据
		List<Map<String, Object>> listForProductHome = productHomeService.getListForProductHome(returnMap);
		String forProductHomeList = JSONArray.fromObject(listForProductHome).toString();
		return forProductHomeList;
	}
	/**将前台接收到的json数据拆开，封装到map中
	 * @param request
	 * @return
	 */
	private Map<String,Object> getParamMap(HttpServletRequest request){
		Map<String,Object> returnMap = new HashMap<String, Object>();
		String param = request.getParameter("param");
		JSONObject paramObject = JSON.parseObject(param);
		if(paramObject != null && paramObject.getString("orderType")!=null && StringUtils.isNotBlank(paramObject.getString("orderType"))){
			returnMap.put("orderType", paramObject.getString("orderType"));//订单类型
		}
		if(paramObject != null && paramObject.getString("analysisType") !=  null && StringUtils.isNotBlank(paramObject.getString("analysisType"))){
			returnMap.put("analysisType", paramObject.getString("analysisType"));//分析类型
		}
		if(paramObject != null && paramObject.getString("searchDate") != null && StringUtils.isNotBlank(paramObject.getString("searchDate"))){
			String searchDateType = paramObject.getString("searchDate");//时间类型
			String searchDate = "";
			Date date = new Date();
			SimpleDateFormat format = null;
			//1：今日 2：本周 3：本月 4：本年 5：全部
			if(searchDateType.equals(Context.ORDER_DATA_STATISTICS_TODAY)){
				format = new SimpleDateFormat("YYYY-MM-dd");
				searchDate = format.format(date);
			}else if(searchDateType.equals(Context.ORDER_DATA_STATISTICS_WEEK)){
				format = new SimpleDateFormat("YYYY-MM-dd");
				searchDate = format.format(date);
			}else if(searchDateType.equals(Context.ORDER_DATA_STATISTICS_MONTH)){
				format = new SimpleDateFormat("YYYY-MM");
				searchDate = format.format(date);
			}else if(searchDateType.equals(Context.ORDER_DATA_STATISTICS_YEAR)){
				format = new SimpleDateFormat("YYYY");
				searchDate = format.format(date);
			}else{
				searchDate = "";
			}
			returnMap.put("searchDate", searchDateType);//时间类型
			returnMap.put("searchDateSimple",searchDate);//精简后的时间
		}
		if(paramObject != null && paramObject.getString("startDate") != null && StringUtils.isNotBlank(paramObject.getString("startDate"))){
			returnMap.put("startDate", paramObject.getString("startDate"));//自定义开始时间
		}
		if(paramObject != null && paramObject.getString("endDate") != null && StringUtils.isNotBlank(paramObject.getString("endDate"))){
			returnMap.put("endDate", paramObject.getString("endDate"));//自定义结束时间
		}
		
		return returnMap;
	}
		
}
