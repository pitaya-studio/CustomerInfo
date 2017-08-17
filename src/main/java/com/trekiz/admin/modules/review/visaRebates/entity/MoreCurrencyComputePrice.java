package com.trekiz.admin.modules.review.visaRebates.entity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.trekiz.admin.modules.sys.utils.CurrencyUtils;

import net.sf.json.JSONObject;

/**
 * addPrice方法，可以将相同币种的金额相加
* @ClassName: MoreCurrencyComputePrice
* @Description: TODO
* @author jiachen
* @date 2015年6月3日 下午3:42:51
*
 */
public class MoreCurrencyComputePrice{
	
	private Map<Object, Object> priceMap;

	public Map<Object, Object> getPriceMap() {
		return priceMap;
	}

	public void setPriceMap(Map<Object, Object> priceMap) {
		this.priceMap = priceMap;
	}
	
	public MoreCurrencyComputePrice(Map<Object, Object> map) {
		if(null != map) {
			this.priceMap = map;
		}else{
			new MoreCurrencyComputePrice();
		}
	}
	
	public MoreCurrencyComputePrice() {
		this.priceMap = new HashMap<Object, Object>();
	}
	
	public void addPrice(Object currencyId, Object price) {
		Set<Object> priceKeySet = new HashSet<Object>();
		priceKeySet = this.priceMap.keySet();
		//如果是第一次计算，则初始化一个值
		if(priceKeySet.isEmpty()) {
			priceMap.put(currencyId, price.toString());
			return;
		}
		boolean isNewCurrency = true;
		for(Object o : priceKeySet) {
			//判断是否是相同币种，如果是，则相加
			if(o.equals(currencyId)) {
				Object initPirce = priceMap.get(o);
				if(null != initPirce && null != price) {
					BigDecimal bigInitPirce = new BigDecimal(initPirce.toString());
					BigDecimal bigValue = new BigDecimal(price.toString());
					priceMap.put(o, bigInitPirce.add(bigValue).toString());
				}
				isNewCurrency = false;
			}
		}
		//判断是否是相同币种，如果不是，则说明是新增的币种，需要添加到map中
		if(isNewCurrency) {
			priceMap.put(currencyId, price.toString());
		}
	}
	
	//转化成币种+金额的字符串
	public String toString() {
		JSONObject object = JSONObject.fromObject(priceMap);
		object.toString().replace("\"", "'");
		return CurrencyUtils.jsonDataPrice2ShwoPrice(object.toString());
	}
}
