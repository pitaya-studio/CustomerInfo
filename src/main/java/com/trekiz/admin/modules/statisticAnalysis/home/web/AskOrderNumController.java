package com.trekiz.admin.modules.statisticAnalysis.home.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.modules.statisticAnalysis.home.service.AskOrderNumService;
import com.trekiz.admin.modules.statisticAnalysis.home.service.CountAndRateService;

@Controller
@RequestMapping(value = "${adminPath}/ask/orderNum")
public class AskOrderNumController {
	@Autowired
	private AskOrderNumService askOrderNumService;
	
	@Autowired
	private CountAndRateService countAndRateService;
	
	@RequestMapping(value="toDetail")
	public String toDetail(HttpServletRequest request,Model model){
		model.addAttribute("pageTab", request.getParameter("pageTab"));
		model.addAttribute("searchDate", request.getParameter("searchDate"));
		model.addAttribute("startDate", request.getParameter("startDate"));
		model.addAttribute("endDate", request.getParameter("endDate"));
		model.addAttribute("analysisType", request.getParameter("analysisType"));
		return "/modules/statisticAnalysis/statisticDetail/statisticManagerList";
	}
	
	/**
	 * 询单数据展示 -- 折线图
	 * @param request
	 * @return
	 * @author chao.zhang
	 * @date 2017-3-7
	 */
	@ResponseBody
	@RequestMapping(value = "getAskOrderNumPicture")
	public String getAskOrderNumPicture(HttpServletRequest request){
		String param = request.getParameter("param"); 
		JSONObject jsonObject = JSON.parseObject(param);
		
		String searchDate = jsonObject.getString("searchDate");//时间 1：今日 -1：昨日 3：本月 -3：上月 4：本年 -4：去年  5：全部 
		String startDate = jsonObject.getString("startDate");//自定义开始时间
		String endDate = jsonObject.getString("endDate");//自定义结束时间
		String month = jsonObject.getString("month");//月
		String year = jsonObject.getString("year");//年
		//获得需要的数据
		Map<String, Object> map = new HashMap<>();
		map.put("searchDate", searchDate);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		map.put("month", month);
		map.put("year", year);
		try {
			map = askOrderNumService.getAskOrderNum(searchDate, startDate, endDate, year, month);
		} catch (Exception e) {
			e.printStackTrace();
			return "暂无数据";
		}
		return JSON.toJSONString(map);
	}
	
	/**
	 * 询单新增单数及增长率处理
	 * @author gaoyang
	 * @Time 2017-3-10 下午3:15:39
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getAskOrderCountAndRate")
	public String getAskOrderCountAndRate(HttpServletRequest request) {
		String param = request.getParameter("param"); 
		JSONObject jsonObject = JSON.parseObject(param);
		String searchDate = jsonObject.getString("searchDate"); // 时间 1：今日 -1：昨日 3：本月 -3：上月 4：本年 -4：去年  5：全部 
		String startDate = jsonObject.getString("startDate"); // 自定义开始时间
		String endDate = jsonObject.getString("endDate"); // 自定义结束时间
		Map<String, String> mapStr = new HashMap<String, String>();
		mapStr.put("searchDate", searchDate);
		mapStr.put("startDate", startDate);
		mapStr.put("endDate", endDate);
		Map<String, Object> map = countAndRateService.getPreOrderCountAndRate(mapStr);
		SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd");
		String nowDate = sdf.format(new Date());
		// 当条件为搜索全部时候需要开始时间与当前时间
		if (searchDate.equals(Context.ORDER_DATA_STATISTICS_ALL) 
				&& StringUtils.isBlank(startDate) 
				&& StringUtils.isBlank(endDate)
				) {
		    List<Map<String,Object>> fdo = countAndRateService.getFirstDateAskOrderList();
		    if (fdo.size() > 0 && fdo.get(0).get("ask_time") != null) {
		    	map.put("startDateSpec", fdo.get(0).get("ask_time").toString().substring(0, 11));
		    	map.put("endDateSpec", nowDate);
		    } else {
		    	map.put("startDateSpec", "");
		    	map.put("endDateSpec", "");
		    }
			
		}
		// 当搜索条件为自定义时
		// 自定义时间都不为空
		if (StringUtils.isNotBlank(startDate) && StringUtils.isNotBlank(endDate)) {
			map.put("startDateSpec", startDate);
			map.put("endDateSpec", endDate);
		}
		// 自定义时间startDate不为空
		if (StringUtils.isNotBlank(startDate) && StringUtils.isBlank(endDate)) {
			map.put("startDateSpec", startDate);
			map.put("endDateSpec", nowDate);
		}
		// 自定义时间endDate不为空
		if (StringUtils.isBlank(startDate) && StringUtils.isNotBlank(endDate)) {
			List<Map<String,Object>> fdo = countAndRateService.getFirstDateAskOrderList();
			if (fdo.size() > 0 && fdo.get(0).get("ask_time") != null) {
			    String askTime = fdo.get(0).get("ask_time").toString().substring(0, 11);
				try {
					Date fstDate = sdf.parse(askTime);
					Date sedDate = sdf.parse(endDate);
					// 开始时间不能大于结束时间
					if (fstDate.getTime() > sedDate.getTime()) {
						map.put("startDateSpec", "");
					} else {
						map.put("startDateSpec", askTime);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				map.put("endDateSpec", endDate);
			} else {
				map.put("startDateSpec", "");
				map.put("endDateSpec", endDate);
			}
		}
		
		// newNum 当前周期
		// incrementRate 增长率 如果不需要字段设置为-1
		return JSON.toJSONString(map);
	}
}