package com.trekiz.admin.modules.sys.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;

import com.quauq.multi.tenant.util.MultiTenantConcurrentHashMap;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;

public class CurrencyUtils {

	private static CurrencyDao currencyDao = SpringContextHolder
			.getBean(CurrencyDao.class);

	static Map<Long, Currency> currencyMarkerStyleMap = new MultiTenantConcurrentHashMap<Long, Currency>();
	public static String REGEX = "#";
	// 获取所有币种列表
	static {
		if (currencyMarkerStyleMap.isEmpty()) {
			Iterable<Currency> currencyIterable = currencyDao.findAllAvailableCurrencies();
			Iterator<Currency> currencyIterator = null;
			if (currencyIterable != null) {
				currencyIterator = currencyIterable.iterator();
			}
			while (currencyIterator.hasNext()) {
				Currency currency = currencyIterator.next();
				currencyMarkerStyleMap.put(currency.getId(), currency);
			}
		}
	}
	
	/**
	 * 多币种 金额 转换成人民币
	 * 默认分隔符 #
	 * add by zhanghao
	 * @param currencyIds
	 * @param amounts
	 * @return
	 */
	public static Double currencyConverter(String currencyIds,String amounts){
		return currencyConverter(currencyIds, amounts, REGEX);
	}
	public static Double currencyConverter(String currencyIds,String amounts,String regex){
		double totalMoney = 0d;
		if(StringUtils.isNotBlank(currencyIds)&&StringUtils.isNotBlank(amounts)){
			String[] currencyIdArray = currencyIds.split(regex);
			String[] amountArray = amounts.split(regex);
			if(currencyIdArray.length==amountArray.length){
				for(int i=0;i<currencyIdArray.length;i++){
					String currencyId = currencyIdArray[i];
					String amount = amountArray[i];
					Currency currency = currencyDao.findById(Long.parseLong(currencyId));
					totalMoney= totalMoney +currency.getConvertLowest().doubleValue()*Double.parseDouble(amount);
				}
			}
		}
		BigDecimal   result   =   new   BigDecimal(totalMoney); 
		return result.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	
	/**
	 * 获取对应供应商的币种列表
	 * by chy 2015年5月18日15:09:44
	 * @return
	 */
	public static List<Currency> findCurrencyList() {
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<Currency> list = currencyDao.findListByID(
				Currency.DEL_FLAG_NORMAL, companyId);
		return list;
	}
	
	// 获取币种信息
	public static String getCurrencyInfo(String currencyType, Integer index,
			String infoType) {
		if (StringUtils.isNotBlank(currencyType) && index != null) {
			
			// 如果是新用户或者新增的币种，则需要重新获取一次
			if (currencyMarkerStyleMap.isEmpty()) {
				Iterable<Currency> currencyIterable = currencyDao.findAllAvailableCurrencies();
				Iterator<Currency> currencyIterator = null;
				if (currencyIterable != null) {
					currencyIterator = currencyIterable.iterator();
				}
				while (currencyIterator.hasNext()) {
					Currency currency=(Currency)currencyIterator.next();
					currencyMarkerStyleMap.put(currency.getId(),currency);
				}
			}
			// 如果是 定金、单房差，则重新赋index值
			if (index == 99) {
				index = currencyType.split(",").length - 2;
			}
			if (index == 100) {
				index = currencyType.split(",").length - 1;
			}
			Currency currency = null;
			if (!currencyMarkerStyleMap.isEmpty()) {
				if (currencyType.split(",").length > index) {
					currency = currencyMarkerStyleMap.get(Long
							.valueOf(currencyType.split(",")[index]));
				} else {
					currency = new Currency();
				}
				// 如果没有找到对应的币种信息，说明是新增加的币种，需要重新获取一次。
				if (currency == null) {
					currency = currencyDao.findById(Long.valueOf(currencyType
							.split(",")[index]));
				}
			}
			if ("style".equals(infoType)) {
				return currency != null ? currency.getCurrencyStyle() : "";
			} else if ("mark".equals(infoType)) {
				return currency != null ? currency.getCurrencyMark() : "";
			} else if ("name".equals(infoType)) {
				return currency != null ? currency.getCurrencyName() : "";
			} else if("id".equals(infoType)) {
				return currency != null ? currency.getId().toString() : "";
			}
			else {
				new RuntimeException("需要指定一个获取的数据字段名");
				return "";
			}
		} else {
			return "";
		}
	}

	/**
	 * 获取币种的名称或符号
	 * 
	 * @param id
	 *            币种Id
	 * @param flag
	 *            1表示取币种名称，0表示取币种符号
	 * */
	public static String getCurrencyNameOrFlag(Long currencyId, String flag) {

		Long companyId = UserUtils.getUser().getCompany().getId();
		Currency currency = currencyDao.findID(currencyId, companyId);
		if (currency != null) {
			if ("1".equals(flag)) {
				return currency.getCurrencyName();
			} else if ("0".equals(flag)) {
				return currency.getCurrencyMark();
			} else {
				return currency.getCurrencyMark();
			}
		} else {
			return "";
		}
	}

	/**
	 * 通过币种符号获取币种id
	 * @param CMark
	 * @return
	 */
	public static Map<String,Object> getCurrnecyIdByMarks(String CMark){
		String sql = "select c.currency_id,c.currency_name,c.currency_mark FROM currency c WHERE c.currency_mark=?  AND c.create_company_id=? AND c.del_flag=0 ";
		List<Map<String,Object>> list = currencyDao.findBySql(sql,Map.class, CMark,UserUtils.getUser().getCompany().getId());
		
		return list.get(0);
	}
	
	/**
	 * 将数据库中的币种加价格的json数据转换成供前台显示的展示字符数据
	 * @author jiachen
	 * @DateTime 2015年6月3日 下午9:27:45
	 * @param dataPrice
	 * @return
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	public static String jsonDataPrice2ShwoPrice(String jsonDataPrice) {
		StringBuffer htmlStr = new StringBuffer();
		if(StringUtils.isNotBlank(jsonDataPrice)) {
			jsonDataPrice = jsonDataPrice.replace("'", "\"");
			try {
				JSONObject jsonObject = JSONObject.fromObject(jsonDataPrice);
				Iterator it = jsonObject.keys();
					while (it.hasNext()) {
						String key = String.valueOf(it.next());
						String value = (String) jsonObject.get(key);
						if(StringUtils.isNotBlank(key)) {
							Currency currency = currencyMarkerStyleMap.get(key);
							// 如果没有找到对应的币种信息，说明是新增加的币种，需要重新获取一次。
							if (currency == null) {
								currency = currencyDao.findById(Long.valueOf(key));
							}
							htmlStr.append(currency.getCurrencyMark());
							DecimalFormat df = new DecimalFormat("#,##0.00");
							String val = df.format(new BigDecimal(value));
							htmlStr.append(val);
							htmlStr.append("+");
						}
					}
				} catch (Exception e) {
					new Exception("json数据异常");
				}
			}
			if(StringUtils.isNotBlank(htmlStr.toString())) {
				return htmlStr.substring(0, htmlStr.length()-1);
			}else{
				return "-";
			}
		}

	
	/**
	 * 多币种 金额 转换成人民币
	 * @author zhangyp
	 * @param currencyType 
	 * @param amount 
	 * @return
	 */
	public static Double currencyConverter4RMB(String currencyType, Integer index, String amount) {
		String currencyId = getCurrencyInfo(currencyType, index, "id");
		Double convertedPrice = currencyConverter(currencyId, amount.trim());
		return convertedPrice;
	}
}
