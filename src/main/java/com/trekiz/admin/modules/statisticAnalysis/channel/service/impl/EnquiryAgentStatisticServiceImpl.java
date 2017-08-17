/**
 * agentPercentChartServiceImpl.java
 */
package com.trekiz.admin.modules.statisticAnalysis.channel.service.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.modules.statisticAnalysis.channel.dao.EnquiryAgentStatisticDao;
import com.trekiz.admin.modules.statisticAnalysis.channel.service.EnquiryAgentStatisticService;

/**
 * @author junhao.zhao
 *
 * 2017年3月8日  下午6:10:53
 */
@Service
public class EnquiryAgentStatisticServiceImpl implements EnquiryAgentStatisticService{

	@Autowired
	private EnquiryAgentStatisticDao enquiryAgentStatisticDao;

	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.channel.service.EnquiryAgentStatisticService#getListForAgentPercentChart(java.util.Map, java.lang.Long)
	 * EnquiryAgentStatisticServiceImpl getListForAgentPercentChart
	 * 
	 * 2017年3月8日  下午7:05:39 
	 */
	@Override
	public List<Map<String, Object>> getListForAgentPercentChart(Map<String, Object> map, Long companyId) {
		
		//根据条件查询最多6条-询单-数据
		List<Map<String,Object>> allList = enquiryAgentStatisticDao.getListForAgentPercentChart(map, companyId);
		//返回的最多六条数据
		List<Map<String, Object>> sixList = Lists.newArrayList();
		//格式化数字
		//NumberFormat nf = new DecimalFormat("#,###");
//		NumberFormat nfm = new DecimalFormat("#,###.00");
		//sql最后有LIMIT 0, 6。所以allList最多等于6条-------大于5条的，合并为其他
		if(allList.size() == 6){
			//循环标示
			int n=0;
			//前五条询单之和
			int enquiryFiveNum = 0;
			//询单总数
			int enquiryTotalNum = 0;
			for(Map<String,Object> mapKey : allList){
				n++;if(n>5){break;}
				if(mapKey.get("enquiryNum")!= null && StringUtils.isNotBlank(mapKey.get("enquiryNum").toString())){
					enquiryFiveNum += Integer.parseInt(mapKey.get("enquiryNum").toString());
					mapKey.put("value", mapKey.get("enquiryNum"));
				}else{
					mapKey.put("value", 0);
				}
				
				if(mapKey.get("agentName")!= null){
					mapKey.put("name", mapKey.get("agentName"));
				}else{
					mapKey.put("name", "");
				}
				//删除不需要的数据
				mapKey.remove("enquiryNum");
				mapKey.remove("agentName");
				sixList.add(mapKey);
			}
			//根据条件查询所有询单数
			List<Map<String,Object>> enquiryTotalNumList = enquiryAgentStatisticDao.getEnquiryTotalNum(map, companyId);
			for(Map<String,Object> mapNum : enquiryTotalNumList){
				if(mapNum.get("enquiryTotalNum")!= null && StringUtils.isNotBlank(mapNum.get("enquiryTotalNum").toString())){
					enquiryTotalNum += Integer.parseInt(mapNum.get("enquiryTotalNum").toString());
				}
			}
			//其他询单数
			int otherNum = enquiryTotalNum - enquiryFiveNum;
			//拼装"其他"
			Map<String,Object> mapOther = new HashMap<String,Object>();
			mapOther.put("name", "其他");
			mapOther.put("value", otherNum);
			sixList.add(mapOther);
		}else{
			for(Map<String,Object> mapKey : allList){
				if(mapKey.get("enquiryNum")!= null){
					mapKey.put("value", mapKey.get("enquiryNum"));
				}else{
					mapKey.put("value", 0);
				}
				
				if(mapKey.get("agentName")!= null){
					mapKey.put("name", mapKey.get("agentName"));
				}else{
					mapKey.put("name", "");
				}
				//删除不需要的数据
				mapKey.remove("enquiryNum");
				mapKey.remove("agentName");
				sixList.add(mapKey);
			}
			
		}
		
		return sixList;
	}

	/* (non-Javadoc)
	 * @see com.trekiz.admin.modules.statisticAnalysis.channel.service.EnquiryAgentStatisticService#getListForOrderPercentChart(java.util.Map, java.lang.Long)
	 * EnquiryAgentStatisticServiceImpl getListForOrderPercentChart
	 * 
	 * 2017年3月9日  下午5:09:32 
	 */
	@Override
	public List<Map<String, Object>> getListForOrderPercentChart(Map<String, Object> map, Long companyId) {
		//TODO Auto-generated method stub
		//EnquiryAgentStatisticService getListForOrderPercentChart
		//
		//2017年3月9日  下午5:09:32 
		//根据条件查询最多6条-订单-数据
		List<Map<String,Object>> allList = enquiryAgentStatisticDao.getListForOrderPercentChart(map, companyId);
		//返回的最多六条数据
		List<Map<String, Object>> sixList = Lists.newArrayList();
		
		//根据分析类型（1：订单数，2：收客人数，3：订单金额）返回结果
		
		if(map.get("analysisType") != null && StringUtils.isNotBlank(map.get("analysisType").toString())){
			String analysisType = map.get("analysisType").toString();
			switch (analysisType) {
			case Context.ORDER_DATA_STATISTICS_ORDER_NUM:
				commonNum(allList, sixList, map, companyId, "orderNum","orderPersonNum","orderMoney");
				break;
			case Context.ORDER_DATA_STATISTICS_CUSTOMER_NUM:
				commonNum(allList, sixList, map, companyId, "orderPersonNum","orderNum","orderMoney");
				break;
			default:
				commonNum(allList, sixList, map, companyId, "orderMoney","orderPersonNum","orderNum");;
			}
		}
		
		return sixList;
	}
	

	/**根据分析类型（1：订单数，2：收客人数，3：订单金额）不同，而调用的共有方法
	 * @param allList
	 * @param sixList
	 * @param map
	 * @param companyId
	 * @param orderNum
	 * @param orderPersonNum
	 * @param orderMoney
	 */
	private void commonNum(List<Map<String,Object>> allList, List<Map<String,Object>> sixList,
			Map<String, Object> map, Long companyId, String orderNum, String orderPersonNum, String orderMoney){
		//sql最后有LIMIT 0, 6。所以allList最多等于6条-------大于5条的，合并为其他
		if(allList.size() == 6){
			//循环标示
			int n=0;
			//前五条订单之和
			BigDecimal orderFiveNum = BigDecimal.ZERO;
			//订单总数
			BigDecimal orderTotalNum = BigDecimal.ZERO;
			for(Map<String,Object> mapKey : allList){
				n++;if(n>5){break;}
				if(mapKey.get(orderNum)!= null && StringUtils.isNotBlank(mapKey.get(orderNum).toString())){
					orderFiveNum = orderFiveNum.add(new BigDecimal(mapKey.get(orderNum).toString()));
					mapKey.put("value", mapKey.get(orderNum));
					if("orderMoney".equals(orderNum)){
						mapKey.put("value", new BigDecimal(mapKey.get(orderNum).toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
				}else{
					mapKey.put("value", 0);
					if("orderMoney".equals(orderNum)){
						mapKey.put("value", 0.00);
					}
				}
				
				if(mapKey.get("agentName")!= null){
					mapKey.put("name", mapKey.get("agentName"));
				}else{
					mapKey.put("name", "");
				}
				//删除不需要的数据
				mapKey.remove(orderNum);
				mapKey.remove("agentName");
				mapKey.remove(orderPersonNum);
				mapKey.remove(orderMoney);
				sixList.add(mapKey);
			}
			//根据条件查询所有订单数
			List<Map<String,Object>> orderTotalNumList = enquiryAgentStatisticDao.getOrderTotalNum(map, companyId);
			for(Map<String,Object> mapNum : orderTotalNumList){
				if(mapNum.get(orderNum)!= null && StringUtils.isNotBlank(mapNum.get(orderNum).toString())){
					orderTotalNum = orderTotalNum.add(new BigDecimal(mapNum.get(orderNum).toString()));
				}
			}
			//其他订单数
			BigDecimal otherNum = orderTotalNum.subtract(orderFiveNum);
			//拼装"其他"
			Map<String,Object> mapOther = new HashMap<String,Object>();
			mapOther.put("name", "其他");
			mapOther.put("value", otherNum);
			if("orderMoney".equals(orderNum)){
				mapOther.put("value", otherNum.setScale(2, BigDecimal.ROUND_HALF_UP));
			}
			sixList.add(mapOther);
		}else{
			for(Map<String,Object> mapKey : allList){
				if(mapKey.get(orderNum)!= null && StringUtils.isNotBlank(mapKey.get(orderNum).toString())){
					mapKey.put("value", mapKey.get(orderNum));
					if("orderMoney".equals(orderNum)){
						mapKey.put("value", new BigDecimal(mapKey.get(orderNum).toString()).setScale(2, BigDecimal.ROUND_HALF_UP));
					}
				}else{
					mapKey.put("value", 0);
					if("orderMoney".equals(orderNum)){
						mapKey.put("value", 0.00);
					}
				}
				
				if(mapKey.get("agentName")!= null){
					mapKey.put("name", mapKey.get("agentName"));
				}else{
					mapKey.put("name", "");
				}
				//删除不需要的数据
				mapKey.remove(orderNum);
				mapKey.remove("agentName");
				mapKey.remove(orderPersonNum);
				mapKey.remove(orderMoney);
				sixList.add(mapKey);
			}
			
		}
	}
	
	
	
	
	
	
}
