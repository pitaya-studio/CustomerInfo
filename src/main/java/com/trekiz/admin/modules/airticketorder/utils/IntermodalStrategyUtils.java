package com.trekiz.admin.modules.airticketorder.utils;

import java.util.List;

import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.IntermodalStrategy;
import com.trekiz.admin.modules.activity.service.IntermodalStrategyService;
import com.trekiz.admin.modules.sys.entity.Currency;

public class IntermodalStrategyUtils {
	
	public static List<IntermodalStrategy> getIntermodalStrategyList(String airticketId){
		
		IntermodalStrategyService intermodalStrategyService = SpringContextHolder.getBean("intermodalStrategyService");
		
		List<IntermodalStrategy> intermodalStrategies = intermodalStrategyService.getActivityIntermodalStrategies(Long.parseLong(airticketId));
		
		return intermodalStrategies;
	}

	public static Currency getCurrencyByIntermodalId(String intermodalId) {
		IntermodalStrategyService intermodalStrategyService = SpringContextHolder.getBean("intermodalStrategyService");
		Currency currency = null;
		if(StringUtils.isNotBlank(intermodalId)) {
			IntermodalStrategy intermodalStrategy = intermodalStrategyService.getOne(Long.parseLong(intermodalId));
			currency = intermodalStrategy.getPriceCurrency();
		}
		return currency;
	}
	
	public static String getCurrencyAmountByIntermodalId(String intermodalId) {
		IntermodalStrategyService intermodalStrategyService = SpringContextHolder.getBean("intermodalStrategyService");
		String price = null;
		if(StringUtils.isNotBlank(intermodalId)) {
			IntermodalStrategy intermodalStrategy = intermodalStrategyService.getOne(Long.parseLong(intermodalId));
			price = intermodalStrategy.getPrice().toString();
		}
		return price;
	}


}
