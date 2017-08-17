package com.trekiz.admin.modules.activity.utils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.agentToOffice.T2.entity.Rate;
import com.trekiz.admin.agentToOffice.T2.utils.JudgeStringType;
import com.trekiz.admin.agentToOffice.T2.utils.RateUtils;
import com.trekiz.admin.agentToOffice.quauqstrategy.service.QuauqGroupStrategyService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.service.sync.ActivityGroupSyncService;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;

public class TravelActivityUtil {
	
	private static QuauqGroupStrategyService quauqGroupStrategyService = SpringContextHolder.getBean(QuauqGroupStrategyService.class);
	private static ActivityGroupSyncService activityGroupSyncService = SpringContextHolder.getBean(ActivityGroupSyncService.class);
	
	public static BigDecimal getMinPrice(String[] prices,BigDecimal[] prices1) {

		BigDecimal minPrice = new BigDecimal(Long.MAX_VALUE);
		if (prices != null && prices.length != 0) {
			for (String price : prices) {
				if(StringUtils.isNotBlank(price)){
					if (minPrice.compareTo(new BigDecimal(price)) == 1)
						minPrice = new BigDecimal(price);
				}
			}
		} else if(prices1 != null && prices1.length!=0) {
			for(BigDecimal price:prices1){
				if(price!=null && price.intValue()!=0){
					if(minPrice.compareTo(new BigDecimal(price.intValue())) == 1)
						minPrice = price;
				}
				
			}
		}else{
			minPrice = new BigDecimal(0);
		}
		if(minPrice.compareTo(new BigDecimal(Long.MAX_VALUE)) == 0)
			minPrice = new BigDecimal(0);
		return minPrice;
	}

	public static BigDecimal getMinPrice(String[] prices) {

		BigDecimal minPrice = new BigDecimal(Long.MAX_VALUE);
		if (prices != null && prices.length != 0) {
			for (String price : prices) {
				if(StringUtils.isNotBlank(price)){
					if (minPrice.compareTo(new BigDecimal(price)) == 1)
						minPrice = new BigDecimal(price);
				}
			}
		}
		// bug 17276 2017.1.23  wangyang
//		} else{
//			minPrice = null;
//		}
		if(minPrice.compareTo(new BigDecimal(Long.MAX_VALUE)) == 0)
			minPrice = null;
		return minPrice;
	}
	
	public static Date getMinDate(String[] dates,Date[] date1s) {
		
		Date minDate = null;
		long minDateTimes = Long.MAX_VALUE;
		if(dates!=null && dates.length!=0){
			for(String date:dates){
				if(StringUtils.isNotBlank(date)){
					if(minDateTimes > DateUtils.parseDate(date).getTime()){
						minDateTimes = DateUtils.parseDate(date).getTime();
						minDate = DateUtils.parseDate(date);
					}
						
				}
			}
		}
		if(date1s!=null && date1s.length!=0){
			for(Date date:date1s){
				if(date!=null){
					if(minDateTimes > date.getTime()){
						minDateTimes = date.getTime();
						minDate = date;
					}
				}
			}
		}
		return minDate;
	}
	
	public static Date getMaxDate(String[] dates,Date[] date1s) {
		
		Date maxDate = null;
		long maxDateTimes = Long.MIN_VALUE;
		if(dates!=null && dates.length!=0){
			for(String date:dates){
				if(StringUtils.isNotBlank(date)){
					if(maxDateTimes < DateUtils.parseDate(date).getTime()){
//						if(maxDateTimes < DateUtils.parseDate(date).getTime() && DateUtils.parseDate(date).getTime()>new Date().getTime()){
						maxDateTimes = DateUtils.parseDate(date).getTime();
						maxDate = DateUtils.parseDate(date);
					}
						
				}
			}
		}
		if(date1s!=null && date1s.length!=0){
			for(Date date:date1s){
				if(date!=null){
					if(maxDateTimes < date.getTime()){
						maxDateTimes = date.getTime();
						maxDate = date;
					}
				}
			}
		}
		return maxDate;
	}

	/**
	 * 参数处理：用request取值、放入条件map
	 * @param paras 参数按逗号分隔
	 * @param mapRequest
	 * @param request
	 * @author jiawei.du
	 */
	public static void handlePara(String paras, Map<String,String> mapRequest, HttpServletRequest request) {
		if (org.apache.commons.lang3.StringUtils.isNotBlank(paras)) {
			String common = "";
			for(String para : paras.split(",")) {
				common = request.getParameter(para);
				if(StringUtils.isNotBlank(common)){
				//if(common != null) {
					common = common.trim().replace("'", "");
					common = common.replace("\\", "\\\\\\\\");
					mapRequest.put(para, common);
				} else {
					mapRequest.put(para, null);
				}
			}
		}
	}
	/**
	 * 当供应价 > 同行价时，将该渠道商的id记录下来 
	 * @param groupId
	 * @param productType
	 * @param settlementAdultPrice
	 * @param settlementcChildPrice
	 * @param settlementSpecialPrice
	 * @param quauqAdultPrice
	 * @param quauqChildrenPrice
	 * @param quauqSpecialPrice
	 * @param currencys
	 * @return
	 */
	public static boolean getAgentIDs(String groupId, Integer productType, BigDecimal settlementAdultPrice,BigDecimal settlementcChildPrice,BigDecimal settlementSpecialPrice,BigDecimal quauqAdultPrice,BigDecimal quauqChildrenPrice,BigDecimal quauqSpecialPrice,String[] currencys){
		if(!JudgeStringType.isPositiveInteger(groupId)){
			throw new RuntimeException("参数数据格式错误");
		}
		//获取渠道商列表
		List<Map<Object,Object>> list = quauqGroupStrategyService.getAllT1Agent();
		//无法查看的渠道数
		boolean tag = false;
		if(quauqAdultPrice == null && quauqChildrenPrice == null  && quauqSpecialPrice == null ){
			tag = true;
		}else{
			for (Map<Object, Object> map : list) {
				//获取批发商id+获取对应的渠道费率
				Rate rate = RateUtils.getRate(new Long(groupId), productType,Long.parseLong(map.get("agentId").toString()));
				BigDecimal tempAdultPrice = null;
				if(quauqAdultPrice != null){
					tempAdultPrice = OrderCommonUtil.getRetailPrice(quauqAdultPrice,rate,Long.parseLong(currencys[0]));
				}
				BigDecimal tempChildrenPrice = null;
				if(quauqChildrenPrice != null){
					tempChildrenPrice = OrderCommonUtil.getRetailPrice(quauqChildrenPrice,rate,Long.parseLong(currencys[1]));
				}
				BigDecimal tempSpecialPrice = null;
				if(quauqSpecialPrice != null){
					tempSpecialPrice = OrderCommonUtil.getRetailPrice(quauqSpecialPrice,rate,Long.parseLong(currencys[2]));
				}
				if((settlementAdultPrice != null && tempAdultPrice != null && settlementAdultPrice.compareTo(tempAdultPrice) < 1 ) || (settlementcChildPrice != null && tempChildrenPrice != null && settlementcChildPrice.compareTo(tempChildrenPrice) < 1) || (settlementSpecialPrice != null && tempSpecialPrice != null && settlementSpecialPrice.compareTo(tempSpecialPrice) < 1)){
					tag = true;
					break;
				}
			}
		}
		//获取批发商对应的产品的费率后的价格
		return tag;
	}
	/**
	 * 获取T1不能查看的渠道商ids（供应价》同行价）
	 * @param groupId
	 * @param productType
	 * @param settlementAdultPrice
	 * @param settlementcChildPrice
	 * @param settlementSpecialPrice
	 * @param quauqAdultPrice
	 * @param quauqChildrenPrice
	 * @param quauqSpecailPrice
	 * @param currencys
	 * @return
	 */
	public static boolean changeValueType(String groupId, Integer productType, String settlementAdultPrice,String settlementcChildPrice,String settlementSpecialPrice,String quauqAdultPrice, String quauqChildrenPrice,String quauqSpecailPrice,String[] currencys){
		BigDecimal tempAdultPrice = null;
		BigDecimal tempChildrenPrice = null;
		BigDecimal tempSpecialPrice = null;
		BigDecimal tempQuauqAdultPrice = null;
		BigDecimal tempQuauqChildrenPrice = null;
		BigDecimal tempQuauqSpecialPrice = null;
		if(StringUtils.isNotBlank(settlementAdultPrice) && !settlementAdultPrice.equals("-")){
			tempAdultPrice = new BigDecimal(settlementAdultPrice);
		}
		if(StringUtils.isNotBlank(settlementcChildPrice) && !settlementcChildPrice.equals("-")){
			tempChildrenPrice = new BigDecimal(settlementcChildPrice);
		}
		if(StringUtils.isNotBlank(settlementSpecialPrice) && !settlementSpecialPrice.equals("-")){
			tempSpecialPrice = new BigDecimal(settlementSpecialPrice);
		}
		if(StringUtils.isNotBlank(quauqAdultPrice) && !quauqAdultPrice.equals("-")){
			tempQuauqAdultPrice = new BigDecimal(quauqAdultPrice);
		}
		if(StringUtils.isNotBlank(quauqChildrenPrice) && !quauqChildrenPrice.equals("-")){
			tempQuauqChildrenPrice = new BigDecimal(quauqChildrenPrice);
		}
		if(StringUtils.isNotBlank(quauqSpecailPrice) && !quauqSpecailPrice.equals("-")){
			tempQuauqSpecialPrice = new BigDecimal(quauqSpecailPrice);
		}
		
		return TravelActivityUtil.getAgentIDs(groupId, productType, tempAdultPrice, tempChildrenPrice, tempSpecialPrice,tempQuauqAdultPrice,tempQuauqChildrenPrice,tempQuauqSpecialPrice, currencys);
	}

	/**
	 * 获取团期在T1中无法查看的渠道数
	 * @param groupId  团期ID
	 * @param productType  团期产品类型
	 * @param settlementAdultPrice  成人同行价
	 * @param settlementcChildPrice   儿童同行价
	 * @param settlementSpecialPrice  特殊人群同行价
	 * @param currencys  货币类型ID数组
	 * @return
	 */
	public static int getEyelessAgentCount(String groupId, Integer productType, BigDecimal settlementAdultPrice,BigDecimal settlementcChildPrice,BigDecimal settlementSpecialPrice,BigDecimal quauqAdultPrice,BigDecimal quauqChildrenPrice,BigDecimal quauqSpecialPrice,String[] currencys){
		if(!JudgeStringType.isPositiveInteger(groupId)){
			throw new RuntimeException("参数数据格式错误");
		}
		
		//获取渠道商列表
		List<Map<Object,Object>> list = quauqGroupStrategyService.getAllT1Agent();
		//无法查看的渠道数
		int count = 0;
		if(quauqAdultPrice == null && quauqChildrenPrice == null  && quauqSpecialPrice == null ){
			count = list.size();
		}else{
			for (Map<Object, Object> map : list) {
				//获取批发商id+获取对应的渠道费率
				Rate rate = RateUtils.getRate(new Long(groupId), productType,Long.parseLong(map.get("agentId").toString()));
				BigDecimal tempAdultPrice = null;
				if(quauqAdultPrice != null){
					tempAdultPrice = OrderCommonUtil.getRetailPrice(quauqAdultPrice,rate,Long.parseLong(currencys[0]));
				}
				BigDecimal tempChildrenPrice = null;
				if(quauqChildrenPrice != null){
					tempChildrenPrice = OrderCommonUtil.getRetailPrice(quauqChildrenPrice,rate,Long.parseLong(currencys[1]));
				}
				BigDecimal tempSpecialPrice = null;
				if(quauqSpecialPrice != null){
					tempSpecialPrice = OrderCommonUtil.getRetailPrice(quauqSpecialPrice,rate,Long.parseLong(currencys[2]));
				}
				if((settlementAdultPrice != null && tempAdultPrice != null && settlementAdultPrice.compareTo(tempAdultPrice) < 1 ) || (settlementcChildPrice != null && tempChildrenPrice != null && settlementcChildPrice.compareTo(tempChildrenPrice) < 1) || (settlementSpecialPrice != null && tempSpecialPrice != null && settlementSpecialPrice.compareTo(tempSpecialPrice) < 1)){
					count++;
				}
			}
		}
		//获取批发商对应的产品的费率后的价格
		return count;
	}
	
	/**
	 * 获取团期在T1中无法查看的渠道数
	 * @param groupId  团期ID
	 * @param productType  团期产品类型
	 * @param settlementAdultPrice  成人同行价
	 * @param settlementcChildPrice   儿童同行价
	 * @param settlementSpecialPrice  特殊人群同行价
	 * @param currencys  货币类型ID数组
	 * @return
	 */
	public static int getEyelessAgentCount(String groupId, Integer productType, String settlementAdultPrice,String settlementcChildPrice,String settlementSpecialPrice,String quauqAdultPrice, String quauqChildrenPrice,String quauqSpecailPrice,String[] currencys){
		BigDecimal tempAdultPrice = null;
		BigDecimal tempChildrenPrice = null;
		BigDecimal tempSpecialPrice = null;
		BigDecimal tempQuauqAdultPrice = null;
		BigDecimal tempQuauqChildrenPrice = null;
		BigDecimal tempQuauqSpecialPrice = null;
		if(StringUtils.isNotBlank(settlementAdultPrice) && !settlementAdultPrice.equals("-")){
			tempAdultPrice = new BigDecimal(settlementAdultPrice);
		}
		if(StringUtils.isNotBlank(settlementcChildPrice) && !settlementcChildPrice.equals("-")){
			tempChildrenPrice = new BigDecimal(settlementcChildPrice);
		}
		if(StringUtils.isNotBlank(settlementSpecialPrice) && !settlementSpecialPrice.equals("-")){
			tempSpecialPrice = new BigDecimal(settlementSpecialPrice);
		}
		if(StringUtils.isNotBlank(quauqAdultPrice) && !quauqAdultPrice.equals("-")){
			tempQuauqAdultPrice = new BigDecimal(quauqAdultPrice);
		}
		if(StringUtils.isNotBlank(quauqChildrenPrice) && !quauqChildrenPrice.equals("-")){
			tempQuauqChildrenPrice = new BigDecimal(quauqChildrenPrice);
		}
		if(StringUtils.isNotBlank(quauqSpecailPrice) && !quauqSpecailPrice.equals("-")){
			tempQuauqSpecialPrice = new BigDecimal(quauqSpecailPrice);
		}
		
		return TravelActivityUtil.getEyelessAgentCount(groupId, productType, tempAdultPrice, tempChildrenPrice, tempSpecialPrice,tempQuauqAdultPrice,tempQuauqChildrenPrice,tempQuauqSpecialPrice, currencys);
	}
	
	public static int getEyelessAgentCount(String groupId,Integer productType ){
		if(!JudgeStringType.isPositiveInteger(groupId)){
			throw new RuntimeException("参数数据格式错误"); 
		}
		ActivityGroup activityGroup = activityGroupSyncService.findById(Long.parseLong(groupId));
		String currency_type = activityGroup.getCurrencyType();
		if(!StringUtil.isBlank(currency_type)){
			String[] currencys = currency_type.split(",");
			return TravelActivityUtil.getEyelessAgentCount(groupId,productType,activityGroup.getSettlementAdultPrice(),activityGroup.getSettlementcChildPrice(),activityGroup.getSettlementSpecialPrice(),activityGroup.getQuauqAdultPrice(),activityGroup.getQuauqChildPrice(),activityGroup.getQuauqSpecialPrice(),currencys);
		}
		return 0;
	}
	
	/**
	 * 是否存在在T1中无法查看团期的渠道
	 * @param groupId  团期ID
	 * @param productType  团期产品类型
	 * @param updateFlag  是否更新团期中的有关字段
	 * @return
	 */
	@Transactional
	public static boolean hasEyelessAgents(String groupId, Integer productType,boolean updateFlag){
		boolean flag = false;
		if(JudgeStringType.isPositiveInteger(groupId)){
			ActivityGroup group = activityGroupSyncService.findById(new Long(groupId));
			 BigDecimal settlementAdultPrice = group.getSettlementAdultPrice();
			 BigDecimal settlementcChildPrice = group.getSettlementcChildPrice();
			 BigDecimal settlementSpecialPrice = group.getSettlementSpecialPrice();
			 BigDecimal quauqAdultPrice = group.getQuauqAdultPrice();
			 BigDecimal quauqChildrenPrice = group.getQuauqChildPrice();
			 BigDecimal quauqSpecialPrice = group.getQuauqSpecialPrice();
			 String tempCurrencys = group.getCurrencyType();
			 if(StringUtil.isBlank(tempCurrencys)){
				 flag = false;
			 }else{
				 String[] currencys = tempCurrencys.split(",");
					//获取渠道商列表
					List<Map<Object,Object>> list = quauqGroupStrategyService.getAllT1Agent();
					//无法查看的渠道数
					if(quauqAdultPrice == null && quauqChildrenPrice == null  && quauqSpecialPrice == null ){
						if(list.size() > 0){
							flag = true;
						}
					}else{
						for (Map<Object, Object> map : list) {
							//获取批发商id+获取对应的渠道费率
							Rate rate = RateUtils.getRate(new Long(groupId), productType,Long.parseLong(map.get("agentId").toString()));
							BigDecimal tempAdultPrice = null;
							if(quauqAdultPrice != null){
								tempAdultPrice = OrderCommonUtil.getRetailPrice(quauqAdultPrice,rate,Long.parseLong(currencys[0]));
							}
							BigDecimal tempChildrenPrice = null;
							if(quauqChildrenPrice != null){
								tempChildrenPrice = OrderCommonUtil.getRetailPrice(quauqChildrenPrice,rate,Long.parseLong(currencys[1]));
							}
							BigDecimal tempSpecialPrice = null;
							if(quauqSpecialPrice != null){
								tempSpecialPrice = OrderCommonUtil.getRetailPrice(quauqSpecialPrice,rate,Long.parseLong(currencys[2]));
							}
							if((settlementAdultPrice != null && tempAdultPrice != null && settlementAdultPrice.compareTo(tempAdultPrice) < 1 ) || (settlementcChildPrice != null && tempChildrenPrice != null && settlementcChildPrice.compareTo(tempChildrenPrice) < 1) || (settlementSpecialPrice != null && tempSpecialPrice != null && settlementSpecialPrice.compareTo(tempSpecialPrice) < 1)){
								flag = true;
								break;
							}
						}
					}
			 }
			 if(updateFlag){
				 group.setHasEyelessAgents(flag);
				 activityGroupSyncService.updateObj(group);
			 }
		}
		return flag;
	}

	public static void hasEyelessAgents(String[] idArray, int productType,
			boolean updateFlag) {
		for(String groupid: idArray){
			hasEyelessAgents(groupid,productType,updateFlag);
		}
	}
	
	/**
	 * 获取团期在T1中无法查看的渠道数
	 * @param groupId  团期ID
	 * @param productType  团期产品类型
	 * @param settlementAdultPrice  成人同行价
	 * @param settlementcChildPrice   儿童同行价
	 * @param settlementSpecialPrice  特殊人群同行价
	 * @param currencys  货币类型ID数组
	 * @return
	 */
	public static int hasEyelessAgents(String groupId, Integer productType, BigDecimal settlementAdultPrice,BigDecimal settlementcChildPrice,BigDecimal settlementSpecialPrice,BigDecimal quauqAdultPrice,BigDecimal quauqChildrenPrice,BigDecimal quauqSpecialPrice,String[] currencys){
		if(!JudgeStringType.isPositiveInteger(groupId)){
			throw new RuntimeException("参数数据格式错误");
		}
		
		int count = 0;
		//无法查看的渠道数
		if(quauqAdultPrice == null && quauqChildrenPrice == null  && quauqSpecialPrice == null ){
			
		}else{
			//获取批发商id+获取对应的渠道费率
			BigDecimal tempAdultPrice = null;
			if(quauqAdultPrice != null){
				Rate rate = RateUtils.getMaxRate(new Long(groupId), productType,quauqAdultPrice);
				if(rate != null){
					tempAdultPrice = OrderCommonUtil.getRetailPrice(quauqAdultPrice,rate,Long.parseLong(currencys[0]));
				}
			}
			BigDecimal tempChildrenPrice = null;
			if(quauqChildrenPrice != null){
				Rate rate = RateUtils.getMaxRate(new Long(groupId), productType,quauqChildrenPrice);
				if(rate != null){
					tempChildrenPrice = OrderCommonUtil.getRetailPrice(quauqChildrenPrice,rate,Long.parseLong(currencys[1]));
				}
			}
			BigDecimal tempSpecialPrice = null;
			if(quauqSpecialPrice != null){
				Rate rate = RateUtils.getMaxRate(new Long(groupId), productType,quauqSpecialPrice);
				if(rate != null){
					tempSpecialPrice = OrderCommonUtil.getRetailPrice(quauqSpecialPrice,rate,Long.parseLong(currencys[2]));
				}
			}
			if((settlementAdultPrice != null && tempAdultPrice != null && settlementAdultPrice.compareTo(tempAdultPrice) < 1 ) || (settlementcChildPrice != null && tempChildrenPrice != null && settlementcChildPrice.compareTo(tempChildrenPrice) < 1) || (settlementSpecialPrice != null && tempSpecialPrice != null && settlementSpecialPrice.compareTo(tempSpecialPrice) < 1)){
				count++;
			}
		}
		//获取批发商对应的产品的费率后的价格
		return count;
	}

	public static int hasEyelessAgents(String groupId, Integer productType, String settlementAdultPrice,String settlementcChildPrice,String settlementSpecialPrice,String quauqAdultPrice, String quauqChildrenPrice,String quauqSpecailPrice,String[] currencys){
		BigDecimal tempAdultPrice = null;
		BigDecimal tempChildrenPrice = null;
		BigDecimal tempSpecialPrice = null;
		BigDecimal tempQuauqAdultPrice = null;
		BigDecimal tempQuauqChildrenPrice = null;
		BigDecimal tempQuauqSpecialPrice = null;
		if(StringUtils.isNotBlank(settlementAdultPrice) && !settlementAdultPrice.equals("-")){
			tempAdultPrice = new BigDecimal(settlementAdultPrice);
		}
		if(StringUtils.isNotBlank(settlementcChildPrice) && !settlementcChildPrice.equals("-")){
			tempChildrenPrice = new BigDecimal(settlementcChildPrice);
		}
		if(StringUtils.isNotBlank(settlementSpecialPrice) && !settlementSpecialPrice.equals("-")){
			tempSpecialPrice = new BigDecimal(settlementSpecialPrice);
		}
		if(StringUtils.isNotBlank(quauqAdultPrice) && !quauqAdultPrice.equals("-")){
			tempQuauqAdultPrice = new BigDecimal(quauqAdultPrice);
		}
		if(StringUtils.isNotBlank(quauqChildrenPrice) && !quauqChildrenPrice.equals("-")){
			tempQuauqChildrenPrice = new BigDecimal(quauqChildrenPrice);
		}
		if(StringUtils.isNotBlank(quauqSpecailPrice) && !quauqSpecailPrice.equals("-")){
			tempQuauqSpecialPrice = new BigDecimal(quauqSpecailPrice);
		}
		return TravelActivityUtil.hasEyelessAgents(groupId, productType, tempAdultPrice, tempChildrenPrice, tempSpecialPrice,tempQuauqAdultPrice,tempQuauqChildrenPrice,tempQuauqSpecialPrice, currencys);
	}
}
