package com.trekiz.admin.modules.statisticAnalysis.channel.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.bcel.generic.SIPUSH;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ibm.icu.text.SimpleDateFormat;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.statisticAnalysis.channel.service.AgentStatisticService;

@Controller
@RequestMapping(value = "${adminPath}/agent/statistic")
public class AgentStatisticController {
	@Autowired
	private AgentStatisticService agentStatisticService;
	
	@RequiresPermissions("home:statistic")
	@RequestMapping(value = "statisticHome")
	public String statisticHome(){
		return "modules/sys/sysIndexV1";
	}
	/**
	 * 跳转到分析详情页
	 * @return
	 * @author chao.zhang
	 */
	@RequestMapping(value="agentStatistic")
	public String agentStatistic(HttpServletRequest request,Model model){
		model.addAttribute("orderType",request.getParameter("orderType"));
		model.addAttribute("analysisType",request.getParameter("analysisType"));
		model.addAttribute("searchDate",request.getParameter("searchDate"));
		model.addAttribute("startDate",request.getParameter("startDate"));
		model.addAttribute("endDate",request.getParameter("endDate"));
		model.addAttribute("pageTab",request.getParameter("pageTab"));
		return "modules/statisticAnalysis/statisticDetail/statisticDetail";
	}
	
	/**
	 * 渠道分析详情
	 * @param request
	 * @return
	 * @author chao.zhang
	 * @date 2016-12-22
	 */
	@ResponseBody
	@RequestMapping(value = "agentStatisticList")
	public String agentStatisticList(HttpServletRequest request,HttpServletResponse response){
		//将参数封装到returnMap中，同时返回到页面中
		Map<String, Object> returnMap = getParamMap(request);
		String orderBy = getOrderBy(returnMap);
		Page<Map<String,Object>> page = new Page<Map<String,Object>>(request, response);
		//分页
		if(returnMap.get("pageNo") != null && StringUtils.isNotBlank(returnMap.get("pageNo").toString())){
			page.setPageNo(Integer.parseInt(returnMap.get("pageNo").toString()));
		}
		if(returnMap.get("pageSize") != null && StringUtils.isNotBlank(returnMap.get("pageSize").toString())){
			page.setPageSize(Integer.parseInt(returnMap.get("pageSize").toString()));
		}
		//分页查询列表
		page = agentStatisticService.getAgentStatisticPageList(page,returnMap, orderBy);
		returnMap.put("list", page.getList());
		returnMap.put("count", page.getCount());//总条数
		//查询总数（订单总数、订单金额总和、收客总人数）
		Map<String, Object> totalNum = agentStatisticService.getTotalNum(returnMap);
		returnMap.put("orderTotalNum", totalNum.get("orderTotalNum"));//订单总数
		returnMap.put("orderTotalMoney", totalNum.get("orderTotalMoney"));//订单总额
		returnMap.put("orderTotalPersonNum", totalNum.get("orderTotalPersonNum"));//收客总人数
		returnMap.put("pageTab", 1);//前端区分渠道页面
		return JSON.toJSONString(returnMap);
	} 
	
	/**
	 * 根据条件组装 orderBy字符串 用于排序
	 * @param returnMap
	 * @return
	 * @author chao.zhang
	 * @data 2016-12-30
	 */
	private String getOrderBy(Map<String,Object> returnMap){
		Object str = returnMap.get("analysisType");
		//排序
		String orderBy = "";
		if(str != null){
			String analysisType = str.toString();
			if(returnMap.get("orderBy")==null){
				if(analysisType.equals(Context.ORDER_DATA_STATISTICS_ORDER_NUM)){
					orderBy = " ORDER BY t.orderNum DESC";
					returnMap.put("orderBy", 1);
				}else if(analysisType.equals(Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM)){
					orderBy = " ORDER BY t.orderPersonNum DESC";
					returnMap.put("orderBy", 3);
				}else if(analysisType.equals(Context.ORDER_DATA_STATISTICS_ORDER_MONEY)){
					orderBy = " ORDER BY t.orderMoney DESC";
					returnMap.put("orderBy", 5);
				}
			}else{
				String oby = returnMap.get("orderBy").toString();
				if(oby.equals("1")){//订单数倒序
					orderBy = " ORDER BY t.orderNum DESC";
					returnMap.put("orderBy", 1);
				}else if(oby.equals("2")){//订单数正序
					orderBy = " ORDER BY t.orderNum ASC";
					returnMap.put("orderBy", 2);
				}else if(oby.equals("3")){//收客人数倒序
					orderBy = " ORDER BY t.orderPersonNum DESC";
					returnMap.put("orderBy", 3);
				}else if(oby.equals("4")){//收客人正序
					orderBy = " ORDER BY t.orderPersonNum ASC";
					returnMap.put("orderBy", 4);
				}else if(oby.equals("5")){//订单金额倒序
					orderBy = " ORDER BY t.orderMoney DESC";
					returnMap.put("orderBy", 5);
				}else if(oby.equals("6")){//订单金额正序
					orderBy = " ORDER BY t.orderMoney ASC";
					returnMap.put("orderBy", 6);
				}
			}
		}else{
			orderBy = " ORDER BY t.orderNum DESC";
			returnMap.put("orderBy", 1);
		}
		return orderBy;
	}
	
	/**
	 * 将接受的参数封装到map中
	 * @param request
	 * @return
	 * @author chao.zhang
	 * @date 2016-12-22
	 */
	private Map<String,Object> getParamMap(HttpServletRequest request){
		Map<String,Object> returnMap = new HashMap<String, Object>();
		String param = request.getParameter("param");
		JSONObject paramObject = JSON.parseObject(param);
		if(paramObject.getString("orderType")!=null && StringUtils.isNotBlank(paramObject.getString("orderType"))){
			returnMap.put("orderType", paramObject.getString("orderType"));//订单类型
		}
		if(paramObject.getString("searchDate") != null && StringUtils.isNotBlank(paramObject.getString("searchDate"))){
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
			returnMap.put("searchDateReal",searchDate);//时间
		}
		if(paramObject.getString("startDate") != null && StringUtils.isNotBlank(paramObject.getString("startDate"))){
			returnMap.put("startDate", paramObject.getString("startDate"));//自定义开始时间
		}
		if( paramObject.getString("endDate") != null && StringUtils.isNotBlank(paramObject.getString("startDate"))){
			returnMap.put("endDate", paramObject.getString("endDate"));//自定义结束时间
		}
		if( paramObject.getString("analysisType") !=  null && StringUtils.isNotBlank(paramObject.getString("analysisType"))){
			returnMap.put("analysisType", paramObject.getString("analysisType"));//分析类型
		}
		if(paramObject.getString("orderNumBegin") != null && StringUtils.isNotBlank(paramObject.getString("orderNumBegin"))){
			returnMap.put("orderNumBegin", paramObject.getString("orderNumBegin"));//订单数开始
		}
		if(paramObject.getString("orderNumEnd") != null && StringUtils.isNotBlank(paramObject.getString("orderNumEnd"))){
			returnMap.put("orderNumEnd", paramObject.getString("orderNumEnd"));//订单数结束
		}
		if(paramObject.getString("orderPersonNumBegin") != null && StringUtils.isNotBlank(paramObject.getString("orderPersonNumBegin"))){
			returnMap.put("orderPersonNumBegin", paramObject.getString("orderPersonNumBegin"));//游客人数开始
		}
		if(paramObject.getString("orderPersonNumEnd") != null && StringUtils.isNotBlank(paramObject.getString("orderPersonNumEnd"))){
			returnMap.put("orderPersonNumEnd", paramObject.getString("orderPersonNumEnd"));//游客人数结束
		}
		if( paramObject.getString("orderMoneyBegin") != null && StringUtils.isNotBlank(paramObject.getString("orderMoneyBegin"))){
			returnMap.put("orderMoneyBegin", paramObject.getString("orderMoneyBegin"));//订单金额开始
		}
		if( paramObject.getString("orderMoneyEnd") != null && StringUtils.isNotBlank(paramObject.getString("orderMoneyEnd"))){
			returnMap.put("orderMoneyEnd", paramObject.getString("orderMoneyEnd"));//订单金额结束
		}
		if(paramObject.getString("pageNo") != null && StringUtils.isNotBlank(paramObject.getString("pageNo"))){
			returnMap.put("pageNo", paramObject.getString("pageNo"));//当前页
		}
		if( paramObject.getString("orderBy") != null && StringUtils.isNotBlank(paramObject.getString("orderBy"))){
			returnMap.put("orderBy", paramObject.getString("orderBy"));//排序
		}
		if(paramObject.getString("pageSize") != null && StringUtils.isNotBlank(paramObject.getString("pageSize"))){
			returnMap.put("pageSize", paramObject.getString("pageSize"));//每页显示条数
		}
		if(paramObject.getString("searchValue") != null && StringUtils.isNotBlank(paramObject.getString("searchValue"))){
			returnMap.put("searchValue", paramObject.getString("searchValue"));//产品名称
		}
		
		return returnMap;
	}
	
	/**
	 * 首页渠道渠道分析图
	 * @param request
	 * @return
	 * @author chao.zhang
	 * @data 2016-12-23
	 */
	@ResponseBody
	@RequestMapping(value = "agentStatisticPicture")
	public String agentStatisticPicture(HttpServletRequest request){
		//将参数封装进map
		Map<String, Object> returnMap = getParamMap(request);
		//将数据封装进map
		Map<String, Object> map = agentStatisticService.getListForOrderType(returnMap);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		map.put("serverTime", format.format(new Date()));
		return JSON.toJSONString(map);
	}
}
