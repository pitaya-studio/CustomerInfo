package com.trekiz.admin.modules.statisticAnalysis.channel.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.modules.statisticAnalysis.channel.dao.AgentStatisticDao;
import com.trekiz.admin.modules.statisticAnalysis.channel.service.AgentStatisticService;

@Service
public class AgentStatisticServiceImpl implements AgentStatisticService{
	@Autowired
	private AgentStatisticDao agentStatisticDao;
	
	/**
	 * 渠道分析详情列表
	 * @param page
	 * @param map 存在查询条件
	 * @param oderBy 排序
	 * @return
	 * @author chao.zhang
	 * @data 2016-12-22
	 */
	@Override
	public Page<Map<String, Object>> getAgentStatisticPageList(
			Page<Map<String, Object>> page, Map<String, Object> map,
			String orderBy) {
		return agentStatisticDao.getAgentStatisticDatas(page, map, orderBy);
	}
	
	/**
	 * 查询订单总数，收客总人数，订单总金额
	 * @param map
	 * @return
	 * @author chao.zhang
	 * @data 2016-12-22
	 */
	@Override
	public Map<String, Object> getTotalNum(Map<String, Object> map) {
		Map<String, Object> map2 = agentStatisticDao.getTotalNum(map);
		return map2;
	}
	
	/**
	 * 渠道分析图表的数据查询
	 * @param map
	 * @return
	 * @author chao.zhang
	 * @data 2016-12-22
	 */
	@Override
	public Map<String, Object> getListForOrderType(Map<String, Object> map) {
		//根据条件查询所有的数据
		List<Map<String,Object>> allList = agentStatisticDao.getListForOrderType(map);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		BigDecimal money = new BigDecimal("0.00");
		Integer count = 0;
		//将图所需要展示的数据封装进map 判断查询的总数是否大于5，大于5取前5条，小于5取全部
		for(int i = 0 ; i < allList.size() ; i++){
			Map<String, Object> map2 = allList.get(i);
			String analysisType = map.get("analysisType").toString();
			//非签约渠道单独显示
			if(map2.get("agentName") == null){
				System.out.println(map2.get("agentName"));
				map2.put("agentName","");
			}
			if("非签约渠道".equals(map2.get("agentName").toString())){
				continue;
			}
			if(list.size() == 5){
				break;
			}
			Map<String,Object> map1 = new HashMap<String, Object>();
			if(analysisType.equals(Context.ORDER_DATA_STATISTICS_ORDER_NUM)){
				map1.put("value", map2.get("orderNum"));
				count+=Integer.parseInt(map2.get("orderNum").toString());
			}else if(analysisType.equals(Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM)){
				map1.put("value", map2.get("orderPersonNum"));
				count+=Integer.parseInt(map2.get("orderPersonNum").toString());
			}else{
				map1.put("value", new BigDecimal(map2.get("orderMoney").toString()).setScale(2,BigDecimal.ROUND_HALF_UP));
				money = money.add(new BigDecimal(map2.get("orderMoney").toString()));
			}
			map1.put("name", map2.get("agentName"));
			list.add(map1);
		}
		//将图显示的数据装入map
		map.put("list", list);
		//计算总额
		BigDecimal big = new BigDecimal("0");
		for(Map<String,Object> m : allList){
			String analysisType = map.get("analysisType").toString();
			if(analysisType.equals(Context.ORDER_DATA_STATISTICS_ORDER_NUM)){
				big = big.add(new BigDecimal(m.get("orderNum").toString()));
			}else if(analysisType.equals(Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM)){
				big = big.add(new BigDecimal(m.get("orderPersonNum").toString()));
			}else{
				big = big.add(new BigDecimal(m.get("orderMoney").toString()));
			}
		}
		String analysisType = map.get("analysisType").toString();
		if(analysisType.equals(Context.ORDER_DATA_STATISTICS_ORDER_NUM)){
			BigDecimal other = big.subtract(new BigDecimal(count+""));
			map.put("otheragent", other);
		}else if(analysisType.equals(Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM)){
			BigDecimal other = big.subtract(new BigDecimal(count+""));
			map.put("otheragent", other);
		}else{
			BigDecimal other = big.subtract(money).setScale(2, BigDecimal.ROUND_HALF_UP);
			map.put("otheragent", MoneyNumberFormat.getThousandsByRegex(other.toString(),2));
		}
		return map;
	}
}
