package com.trekiz.admin.modules.sys.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;
import com.trekiz.admin.modules.hotel.service.HotelMoneyAmountService;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;
import com.trekiz.admin.modules.island.service.IslandMoneyAmountService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.pay.model.ProductMoneyAmount;
import com.trekiz.admin.modules.pay.transfer.MoneyAmountTransfer;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.model.FractionalCurrency;
import com.trekiz.admin.modules.sys.model.GeneralCurrencyInfo;
import com.trekiz.admin.modules.sys.service.CurrencyService;

public class MoneyAmountUtils {
	
	private static MoneyAmountDao moneyAmountDao = SpringContextHolder.getBean(MoneyAmountDao.class);
	private static MoneyAmountService moneyAmountService = SpringContextHolder.getBean(MoneyAmountService.class);
	private static IslandMoneyAmountService islandMoneyAmountService = SpringContextHolder.getBean(IslandMoneyAmountService.class);
	private static HotelMoneyAmountService hotelMoneyAmountService = SpringContextHolder.getBean(HotelMoneyAmountService.class);
	private static CurrencyService currencyService = SpringContextHolder.getBean(CurrencyService.class);
	
	private static final String regex = ",";
	
	/** 币种显示格式1、表示币种名称，如：人民币 */
	public static final int SHOW_TYPE_NAME = 1;
	/** 币种显示格式1、表示币种符号，如：￥ */
	public static final int SHOW_TYPE_MARK = 2;
	
	private static List<GeneralCurrencyInfo> currencyInfos;
	
	/**
	 * 公用接口，根据流水号取金额，对多币种进行拼接
	 * @param serialNums 流水号UUID
	 * @return 币种符合 币种金额  + 币种符合 币种金额 + ...
	 */
	
	@SuppressWarnings({ "unchecked" })
	@Transactional(readOnly = true)
	public static String getMoneyAmount(String serialNum){
		String sql = "SELECT m.currencyId,c.currency_name,sum(m.amount),c.currency_mark from money_amount m,currency c where m.currencyId=c.currency_id and m.serialNum = '"
				+ serialNum +"' GROUP BY m.currencyId ORDER BY m.currencyId";
		List<Object[]> results = moneyAmountDao.getSession().createSQLQuery(sql).list();
		
		StringBuffer money = new StringBuffer();
		
		if(results!=null && results.size()>0){
			for (int i = 0; i < results.size(); i++) {
				Object[] amount = results.get(i);
				DecimalFormat d= new DecimalFormat(",###.00");
				//money = money + amount[1] + d.format(new BigDecimal(amount[2].toString())) + amount[3] + " ";
				money = money.append(amount[3]).append(d.format(new BigDecimal(amount[2].toString())));
				if(i != results.size()-1){
					money = money.append(" + ");
				}
			}
		}else{
			money = money.append("¥ 0.00");
		}
		
		return money.toString();
	}
	
	
	/**
	 * 币种格式转换，由数组转换为字符串
	*<p>Title: getFormatCurrency</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-6-15 下午7:53:53
	* @throws
	 */
	public static String getFormatCurrency(String[] currencyIdArr) {
		return formatArr2Str(currencyIdArr);
	}
	
	/**
	 * 金额格式转换，由数组转换为字符串
	*<p>Title: getFormatMoney</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-6-15 下午7:53:53
	* @throws
	 */
	public static String getFormatMoney(String[] moneyArr) {
		return formatArr2Str(moneyArr);
	}
	
	/**
	 * 将数组以特定格式转换为字符串
	*<p>Title: formatArr2Str</p>
	* @return String 返回类型
	* @author majiancheng
	* @date 2015-6-15 下午7:55:56
	* @throws
	 */
	private static String formatArr2Str(String[] arr) {
		StringBuffer sb = new StringBuffer();
		if(arr != null && arr.length > 0) {
			for(String str : arr) {
				sb.append(str);
				sb.append(regex);
			}
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	
	/**
	 * 根据流水号获取多币种金额加号连接
	 * @param serialNum
	 * @param index 1：币种名称 例如：美元  2：币种符号 例如：$
	 * @return
	 */
	public static String getMoneyAmountBySerialNum(String serialNum, Integer showType, Integer orderType){
		String moneyAmount = "";
		if(orderType.intValue() == Context.ORDER_TYPE_ISLAND) {
			List<IslandMoneyAmount> islandMoneyAmounts = islandMoneyAmountService.getMoneyAmonutBySerialNum(serialNum);
			
			moneyAmount = joinMoneyAmount(MoneyAmountTransfer.transfer2ProductMoneyAmountListFromIsland(islandMoneyAmounts), showType, orderType);
		} else if(orderType.intValue() == Context.ORDER_TYPE_HOTEL) {
			List<HotelMoneyAmount> hotelMoneyAmounts = hotelMoneyAmountService.getMoneyAmonutBySerialNum(serialNum);
			
			moneyAmount = joinMoneyAmount(MoneyAmountTransfer.transfer2ProductMoneyAmountListFromHotel(hotelMoneyAmounts), showType, orderType);
		} else {
			moneyAmount = OrderCommonUtil.getMoneyAmountBySerialNum(serialNum, showType);
		}
		
		return moneyAmount;
	}
	
	/**
	 * 根据多个金额信息获取多币种金额加号连接
	 * @param serialNum
	 * @param index 1：币种名称 例如：美元  2：币种符号 例如：$
	 * @author majiancheng
	 * @return
	 */
	public static String joinMoneyAmount(List<ProductMoneyAmount> productMoneyAmounts,Integer showType, Integer orderType) {
		
		String strMoney = "";
		if(CollectionUtils.isNotEmpty(productMoneyAmounts)) {
			DecimalFormat d= new DecimalFormat(",##0.00");
			for(int i=0; i<productMoneyAmounts.size(); i++) {
				ProductMoneyAmount amount = productMoneyAmounts.get(i);
				String amonutMoney = "0";
				if(amount.getAmount() != null){
					amonutMoney = amount.getAmount().toString();
				}
				
				BigDecimal money = new BigDecimal(amonutMoney);
				String strAmount = d.format(money.abs());
				String currencyStr = "";
				
				Currency currency = currencyService.findCurrency(amount.getCurrencyId().longValue());
				if(showType == SHOW_TYPE_NAME) {
					currencyStr = currency.getCurrencyName();
				} else if(showType == SHOW_TYPE_MARK) {
					currencyStr = currency.getCurrencyMark();
				}
				
				if(money.compareTo(BigDecimal.ZERO) < 0){
					strMoney += "-" + currencyStr + strAmount;
				}else{
					strMoney += (i==0?"":"+") + currencyStr + strAmount;
				}
			}
		}else{
			if(showType == SHOW_TYPE_NAME){
				strMoney = "人民币 0";
			} else if(showType == -1){
				strMoney = "";
			} else{
				strMoney = "¥ 0";
			}
		}
		return strMoney;
	}
	/**
	 * 根据流水号获取MoneyAmount 实体
	 * @author gao
	 * @param serialNum
	 * @return
	 */
	public static MoneyAmount getMoneyAmountByUUID(String serialNum){
		MoneyAmount back = new MoneyAmount();
		if(StringUtils.isNotBlank(serialNum)){
			List<MoneyAmount> list = moneyAmountDao.getAmountByUid(serialNum);
			if(list!=null && !list.isEmpty()){
				back = list.get(0);
			}
		}
		return back;
	}
	/**
	 * 根据流水号获取预订游客返佣值（币种符号+币值）
	 * @author gao
	*  2015年8月13日
	 * @param serialNum
	 * @return
	 */
	public static String getScheduleByUUID(String serialNum){
		StringBuffer str  = new StringBuffer();
		MoneyAmount back = new MoneyAmount();
		if(StringUtils.isNotBlank(serialNum)){
			List<MoneyAmount> list = moneyAmountDao.getAmountByUid(serialNum);
			if(list!=null && !list.isEmpty()){
				back = list.get(0);
				Currency currency = currencyService.findCurrency(Long.valueOf(back.getCurrencyId()));
				str.append(currency.getCurrencyMark()+" ");
				str.append(back.getAmount());
			}
		}
		return str.toString();
	}
	
	/**
	 * 根据流水号获取预订游客返佣值币种ID,和前端选择币种ID进行判断是否相等
	 * @author gao
	*  2015年8月19日
	 * @param serialNum
	 * @return
	 */
	public static String getScheduleCurrencyID(String serialNum,String currencyID){
		String bool = new String();
		MoneyAmount back = new MoneyAmount();
		if(StringUtils.isNotBlank(serialNum)){
			List<MoneyAmount> list = moneyAmountDao.getAmountByUid(serialNum);
			if(list!=null && !list.isEmpty()){
				back = list.get(0);
				Currency currency = currencyService.findCurrency(Long.valueOf(back.getCurrencyId()));
				if(currencyID.equals(currency.getId().toString())){
					bool = "1";
				}
			}
		}
		return bool;
	}

	/**
	 * 根据流水号获取预订游客返佣值币种ID
	 *  2015年8月19日
	 * @param serialNum
	 * @return
	 */
	public static Integer getCurrencyIdBySerialNum(String serialNum){
		Integer result = null;
		MoneyAmount back = new MoneyAmount();
		if(StringUtils.isNotBlank(serialNum)){
			List<MoneyAmount> list = moneyAmountDao.getAmountByUid(serialNum);
			if(list!=null && !list.isEmpty()){
				back = list.get(0);
				result = back.getCurrencyId();
			}
		}
		return result;
	}
	/**
	 * 根据流水号获取MoneyAmont 金额数值
	 * @author gao
	 * @param serialNum
	 * @return
	 */
	public static String getMoneyAmountNUM(String serialNum){
		String amount = new String();
		if(StringUtils.isNotBlank(serialNum)){
			List<MoneyAmount> list = moneyAmountDao.getAmountByUid(serialNum);
			if(list!=null && !list.isEmpty()){
				MoneyAmount back = new MoneyAmount();
				back = list.get(0);
				if(back!=null && back.getAmount()!=null){
					amount = back.getAmount().toString();
				}
			}
		}
		return amount;
	}
	
	/**
     * 数字金额大写转换，思想先写个完整的然后将如零拾替换成零
     * 要用到正则表达式
     */
    public static String digitUppercase(double n){
        String fraction[] = {"角", "分"};
        String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        String unit[][] = {{"元", "万", "亿"}, {"", "拾", "佰", "仟"}};
        String head = n < 0? "红字": ""; ////负 -》红字
        n = Math.abs(n);  
        String s = "";
        for (int i = 0; i < fraction.length; i++) {
            s += (digit[(int)(Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(零.)+", "");
        }
        if(s.length()<1){
            s = "整";   
        }
        int integerPart = (int)Math.floor(n);
 
        for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
            String p ="";
            for (int j = 0; j < unit[1].length && n > 0; j++) {
                p = digit[integerPart%10]+unit[1][j] + p;
                integerPart = integerPart/10;
            }
            s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
        }
        return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
    }
    
    /**
     * 数字金额大写转换，仅适用于单币种（适用于所有的币种信息）返回格式：外币大写；如：$100的大写为壹佰美元整
     * moneyAmounts参数为： currencyId,currency_name,currency_mark,m.amount,currency_exchangerate
     */
    public static String digitUppercase(String currencyName, BigDecimal totalMoney){
    	
    	double n = totalMoney.doubleValue();
    	
    	String fraction[] = {"角", "分"};
        String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        String unit[][] = {{currencyName, "万", "亿"}, {"", "拾", "佰", "仟"}};
        String head = n < 0? "红字": ""; ////负 -》红字
        n = Math.abs(n);  
        String s = "";
        for (int i = 0; i < fraction.length; i++) {
            s += (digit[(int)(Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(零.)+", "");
        }
        if(s.length()<1){
            s = "整";   
        }
        int integerPart = (int)Math.floor(n);
 
        for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
            String p ="";
            for (int j = 0; j < unit[1].length && n > 0; j++) {
                p = digit[integerPart%10]+unit[1][j] + p;
                integerPart = integerPart/10;
            }
            s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
        }
        return head + s.replaceAll("(零.)*零"+currencyName, currencyName).replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零" + currencyName + "整");
    }
    
    /**
     *  多币种相加
     * @param multMap1	多币种数据1，key: 多币符号，value:金额
     * @param multMap2  多币种数据2，key: 多币符号，value:金额
     * @return          多币种数据 ，key: 多币符号，value:金额
     * @date 2015.12.23
     * @author shijun.liu
     */
    public static Map<String, BigDecimal> multiplyCurrencyPlus(Map<String, BigDecimal> multMap1, 
    								Map<String, BigDecimal> multMap2){
    	Map<String, BigDecimal> multMap = new HashMap<String, BigDecimal>();
    	if(null != multMap1 && multMap1.size() != 0){
    		Set<Entry<String, BigDecimal>> set = multMap1.entrySet();
    		Iterator<Entry<String, BigDecimal>> it = set.iterator();
    		while(it.hasNext()){
    			Entry<String, BigDecimal> entry = it.next();
    			String currencyMark = entry.getKey();
    			BigDecimal amount = entry.getValue();
    			BigDecimal existAmount = multMap.get(currencyMark);
    			if(null == existAmount){
    				multMap.put(currencyMark, amount);
    			}else{
    				multMap.put(currencyMark, amount.add(multMap.get(currencyMark)));
    			}
    		}
    	}
    	if(null != multMap2 && multMap2.size() != 0){
    		Set<Entry<String, BigDecimal>> set = multMap2.entrySet();
    		Iterator<Entry<String, BigDecimal>> it = set.iterator();
    		while(it.hasNext()){
    			Entry<String, BigDecimal> entry = it.next();
    			String currencyMark = entry.getKey();
    			BigDecimal amount = entry.getValue();
    			BigDecimal existAmount = multMap.get(currencyMark);
    			if(null == existAmount){
    				multMap.put(currencyMark, amount);
    			}else{
    				multMap.put(currencyMark, amount.add(multMap.get(currencyMark)));
    			}
    		}
    	}
    	return multMap;
    }
    
    /**
     * 多币种相减的方法.
     * 算法思路
     * 		multMap1 与 multMap2 相同币种相减，存储到subMap中，
     *  	multMap1 有而 multMap2 无的币种，则重新遍历multMap1，并将其存储到subMap中，
     *  	multMap1 无而 multMap2 有的币种，则重新遍历multMap2，并将其取反存储到subMap中，
     *  	返回 subMap
     * @param multMap1	多币种数据1，key: 多币符号，value:金额
     * @param multMap2  多币种数据2，key: 多币符号，value:金额
     * @return			多币种数据 ，key: 多币符号，value:金额
     * @date   2015.12.23
     * @author shijun.liu
     */
    public static Map<String, BigDecimal> multiplyCurrencySubtraction(Map<String, BigDecimal> multMap1, 
			Map<String, BigDecimal> multMap2){
    	Map<String, BigDecimal> subMap = new HashMap<String, BigDecimal>();
    	if(null == multMap1){
    		return multMap2;
    	}
    	if(null == multMap2){
    		return multMap1;
    	}
    	boolean mult1 = false;
    	//遍历multMap1 并和 multMap2 做差
    	Set<Entry<String, BigDecimal>> set = multMap1.entrySet();
		Iterator<Entry<String, BigDecimal>> it = set.iterator();
		while(it.hasNext()){
			Entry<String, BigDecimal> entry = it.next();
			String currencyMark = entry.getKey();
			BigDecimal amount = entry.getValue();
			//取得multMap2中有currencyMark币种的金额数据
			BigDecimal existAmount = multMap2.get(currencyMark);
			if(null == existAmount){//如果multMap2中无currencyMark币种，则还需要遍历multMap1，将其加到subMap中
				mult1 = true;
				continue;
			}else{
				subMap.put(currencyMark, amount.subtract(existAmount));
			}
		}
		if(mult1){//multMap1中没有和multMap2做差的币种，金额
			Set<Entry<String, BigDecimal>> multSet1 = multMap1.entrySet();
			Iterator<Entry<String, BigDecimal>> multIt1 = multSet1.iterator();
			while(multIt1.hasNext()){
				Entry<String, BigDecimal> entry = multIt1.next();
				String currencyMark = entry.getKey();
				BigDecimal amount = entry.getValue();
				BigDecimal existAmount = subMap.get(currencyMark);
				if(null == existAmount){
					subMap.put(currencyMark, amount);
				}
			}
		}
		//遍历multMap2中还未和multMap1做差的数据，并将其数据取反放到subMap中
		Set<Entry<String, BigDecimal>> multSet2 = multMap2.entrySet();
		Iterator<Entry<String, BigDecimal>> multIt2 = multSet2.iterator();
		while(multIt2.hasNext()){
			Entry<String, BigDecimal> entry = multIt2.next();
			String currencyMark = entry.getKey();
			BigDecimal amount = entry.getValue();
			BigDecimal existAmount = subMap.get(currencyMark);
			if(null == existAmount){
				subMap.put(currencyMark, amount.multiply(new BigDecimal(-1)));
			}
		}
    	return subMap;
    }
    
    /**
     * 将多币种数据转换成字符串显示
     * $ 100.00
     * ￥ 200.00    -->  $100.00 + ￥200.00
     * @param map				多币种数据
     * @param isShowZero		0，是否显示, true:表示显示，false:表示不显示
     * @return $100+￥300
     * @author shijun.liu
     * @date 2015.12.23
     */
    public static String translateMultplyCurrencyToString(Map<String, BigDecimal> map, boolean isShowZero){
    	StringBuffer str = new StringBuffer();
    	if(null == map || map.isEmpty()){
    		return str.toString();
    	}
    	//遍历map
    	Set<Entry<String, BigDecimal>> set = map.entrySet();
		Iterator<Entry<String, BigDecimal>> it = set.iterator();
		while(it.hasNext()){
			Entry<String, BigDecimal> entry = it.next();
			String currencyMark = entry.getKey();
			BigDecimal amount = entry.getValue();
			//金额是0，并且 不显示零数据
			if(!isShowZero && amount.compareTo(new BigDecimal(0)) == 0){
				continue;
			}
			String thousandMoney = MoneyNumberFormat.getThousandsByRegex(amount.toString(), 2);
			str.append(currencyMark).append(thousandMoney).append("+");
		}
		if(str.toString().length() != 0){
			str.delete(str.toString().length()-1, str.toString().length());
		}
		return str.toString();
    }
    
    /**
     * 多币查询列表相减
     * @param multMap1   多币种数据1，key: 字段是currency_mark列表值，  值:字段是amount列表值
     * @param multMap2   多币种数据2，key: 字段是currency_mark列表值，  值:字段是amount列表值
     * @return
     * @date 2015.12.23
     * @author shijun.liu
     */
    public static Map<String, BigDecimal> multiplyCurrencySubtraction(List<Map<String, Object>> multMap1,
			List<Map<String, Object>> multMap2){
    	//将list转换为map,相同币种求和
    	Map<String, BigDecimal> map1 = mulitplyCurrencyListToMap(multMap1);
    	//将list转换为map,相同币种求和
    	Map<String, BigDecimal> map2 = mulitplyCurrencyListToMap(multMap2);	
    	//多币种做差
    	return multiplyCurrencySubtraction(map1, map2);
    }
    
    /**
     * 多币种查询列表相加
     * @param list1   多币种数据1，key: 字段是currency_mark列表值，  值:字段是amount列表值
     * @param list2   多币种数据2，key: 字段是currency_mark列表值，  值:字段是amount列表值
     * @return
     * @date 2015.12.23
     * @author shijun.liu
     */
    public static Map<String, BigDecimal> multiplyCurrencyPlus(List<Map<String, Object>> list1,
			List<Map<String, Object>> list2){
    	//将list转换为map,相同币种求和
    	Map<String, BigDecimal> map1 = mulitplyCurrencyListToMap(list1);
    	//将list转换为map,相同币种求和
    	Map<String, BigDecimal> map2 = mulitplyCurrencyListToMap(list2);
    	//多币种相加
    	return multiplyCurrencyPlus(map1, map2);
    }
    
    /**
     * 多币种查询列表转换成Map，
     * @param list    多币种数据1，key: 字段是currency_mark列表值，  值:字段是amount列表值
     * @return        map, key为币种符号，value为金额
     * @date 2015.12.23
     * @author shijun.liu
     */
    private static Map<String, BigDecimal> mulitplyCurrencyListToMap(List<Map<String, Object>> list){
    	Map<String, BigDecimal> returnMap = new HashMap<String,BigDecimal>();
    	if(null != list && list.size() > 0){
    		for(Map<String, Object> map: list){
    			Object currencyMark = map.get("currency_mark");
    			if(null == currencyMark || "".equals(currencyMark.toString())){
    				continue;
    			}
    			Object amount = map.get("amount");
    			if(null == amount || "".equals(amount.toString())){
    				amount = "0";
    			}
    			BigDecimal existAmount = returnMap.get(currencyMark);
    			if(null == existAmount){
    				returnMap.put(currencyMark.toString(), new BigDecimal(amount.toString()));
    			}else{
    				returnMap.put(currencyMark.toString(), new BigDecimal(amount.toString()).add(existAmount));
    			}
    		}
    	}
    	return returnMap;
    }

    /**
	 *金额转换成大写	
	 *@author chao.zhang
	 *@param currencyInfo 常用币种进位信息
	 *@param currencyName 默认的币种名称
	 *@param amount 金额
	 */
    public static  String digitUppercase(String currencyName,GeneralCurrencyInfo currencyInfo,Double amount){
		 String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
		 String unit[][] = {{"","万", "亿"}, {"", "拾", "佰", "仟"}};
		 String[] uun={"","拾"} ;
		 //获得金钱的整数
		 java.math.BigDecimal db1 =java.math.BigDecimal.valueOf(amount);
//		 String ww=bd1+"";
        double n = Math.abs(amount); 
		 //最终返回值所用
		 String s="";
		 //小数部分所用
		 String s1="";
		 //获得金额的整数部分
		 int integerPart = (int)Math.floor(n);
		 //将金额转成字符串类型并按小数点拆分
		 String[] strs=(db1+"").split("\\.");
		 //将小数后部分拼接成小数
		 String str="";
		 BigDecimal d=null;
		 if(strs.length>1){
				str="0."+strs[1];
				//将str字符串转换成double,d即金额的小数
				d=new BigDecimal(str);
		}else{
				d=new BigDecimal(0);
		}
		
		 //将str字符串转换成double,d即金额的小数
		// double d = Double.parseDouble(str);
		
		 //整数部分拼接字符所用
		 String s2="";
		 //判断辅币种类数量为0(无)
		 /*if(currencyInfo.getFractionalCurrency()==null){
			 int floor =(int) Math.floor(d*100);
			 String p1="";
			 if(floor%10==0&&floor/10%10==0){
				 s1="整";
			 }else{
				 int k=1;
				 for(int i=0;i<uun.length;i++){
					 if(floor%10==0&&k==1){
						 p1=uun[i]+p1;
					 }else{
						 p1=digit[floor%10]+uun[i]+p1;
					 }
					 if(k>1){
						 p1=p1.replace("零拾", "");
					 }
					 floor=floor/10;
					 k++;
				 }
				 //小数部分转成大写拼接字符串
				 s1=p1+"分";
			 }
		 }
		 //判断辅币种类数为1
		 else*/ if(currencyInfo.getFractionalCurrencys().size()==1){
			 //获得小数位上的前两位
			 BigDecimal d1=new BigDecimal(currencyInfo.getFractionalCurrencys().get(0).getCractionalCount());
			 int floor =(int) Math.floor(d.multiply(d1).doubleValue());
			 String p1="";
			 if(floor%10==0&&floor/10%10==0){
				 s1="整";
			 }else{
				 int k=1;
				 for(int i=0;i<uun.length;i++){
					 if(floor%10==0&&k==1){
						 p1=uun[i]+p1;
					 }else{
						 p1=digit[floor%10]+uun[i]+p1;
					 }
					 if(k>1){
						 p1=p1.replace("零拾", "");
					 }
					 floor=floor/10;
					 k++;
				 }
				 //小数部分转成大写拼接字符串
				 s1=p1+currencyInfo.getFractionalCurrencys().get(0).getCurrencyMark();
			 }
		
		 }
		 //判断辅币币种数为2
		 else if(currencyInfo.getFractionalCurrencys().size()==2){
			 //获得第一位小数上的数字的int类型
			 BigDecimal d1=new BigDecimal(currencyInfo.getFractionalCurrencys().get(0).getCractionalCount());
			 double a=d.multiply(d1).doubleValue();
			 Integer i1=(int)Math.floor(a);
			 //通过计算获得小数第二位上的小数数字的int类型
			 BigDecimal d2=new BigDecimal(currencyInfo.getFractionalCurrencys().get(1).getCractionalCount());
			 double b=(d.multiply(d2).doubleValue())-((Math.floor(d.multiply(d1).doubleValue()))*currencyInfo.getFractionalCurrencys().get(1).getCractionalCount()/currencyInfo.getFractionalCurrencys().get(0).getCractionalCount());
			Integer i2=(int)Math.floor(b);
			//小数部分转成大写，拼接字符串
			s1=digit[i1]+currencyInfo.getFractionalCurrencys().get(0).getCurrencyMark()+digit[i2]+currencyInfo.getFractionalCurrencys().get(1).getCurrencyMark();
			//判断i1，i2若都为0，则输出整
			if(i1==0&&i2==0){
				s1="整";
			}
		 }
		//获得整数位上的大写并拼接字符串
		 for(int i=0;i<unit[0].length;i++){
			 String p="";
			 int x=1;
			 for(int j=0;j<unit[1].length;j++){
				 p=digit[integerPart%10]+unit[1][j]+p;
				 integerPart=integerPart/10;
				 if(integerPart==0){
						break;
					}
			}	
		    s2=p+unit[0][i]+s2;
			if(integerPart==0){
				break;
			}
		}
		 String regex1[] = {"零仟","零佰","零拾"};
		 String regex2[] = {"零亿","零万","零"+currencyInfo.getCurrencyCapital()};
		 String regex3[] = {"亿","万",currencyInfo.getCurrencyCapital()};
		List<String> regex4=new ArrayList<String>();
		if(currencyInfo.getFractionalCurrencys()!=null){
			 for(int f=0;f<currencyInfo.getFractionalCurrencys().size();f++){
				regex4.add("零"+currencyInfo.getFractionalCurrencys().get(f).getCurrencyMark());
			 }
		}	 
		 //判断主币名是否为空，若为空，则按照默认币种名算，若不为空，则为主币名称
		if(currencyName.equals("人民币")){
			if(amount>0){
				s=s2+currencyInfo.getCurrencyCapital()+s1;
			}else{
				s="负"+s2+currencyInfo.getCurrencyCapital()+s1;
			}
			
		}else{
			if(amount>0){
				s=currencyName+s2+currencyInfo.getCurrencyCapital()+s1;
			}else{
				s=currencyName+"负"+s2+currencyInfo.getCurrencyCapital()+s1;
			}
			
		}
		for(int i=0;i<regex1.length;i++){
			s=s.replace(regex1[i], "零");
		}
		for(int j=0;j<3;j++){
			s=s.replaceAll("零零零", "零");
			s=s.replaceAll("零零", "零");
			if(n>=1){
				s=s.replaceAll(regex2[j], regex3[j]);
			}
		}
		String regex5[] = {"零分","零"+currencyInfo.getCurrencyCapital(),"零元","零角"};
		 for(int l=0;l<regex5.length;l++){
			 s=s.replaceAll(regex5[l], "");
		 }
		if(currencyInfo.getFractionalCurrencys().size()==2){
			for(int k=0;k<regex4.size();k++){
				s=s.replace(regex4.get(1),"");
			}
		}	
		for(int m=0;m<currencyInfo.getFractionalCurrencys().size();m++){
			s=s.replace("零"+currencyInfo.getFractionalCurrencys().get(m).getCurrencyMark(), "");
		}
		return s;
	}
    
    /**
     * 根据币种名称和金额生成币种大写字符串
     * @Description: 
     * @param @param currencyName
     * @param @param amount
     * @param @return   
     * @return String  
     * @throws
     * @author majiancheng
     * @date 2016-1-4
     */
    public static String generUppercase(String currencyName, Double amount) {
    	GeneralCurrencyInfo currencyInfo = getCurrencyInfoByName(currencyName);
    	
    	return digitUppercase(currencyName, currencyInfo, amount);
    }

    /**
     * 根据币种名称获取系统维护币种详细信息
     * @Description: 
     * @param @param currencyName
     * @param @return   
     * @return GeneralCurrencyInfo  
     * @throws IOException 
     * @throws
     * @author majiancheng
     * @date 2016-1-4
     */
    public static GeneralCurrencyInfo getCurrencyInfoByName(String currencyName) {
    	if(StringUtils.isEmpty(currencyName)) {
    		return null;
    	}
        
        try{
            //初始化系统维护币种信息
        	if(currencyInfos == null) {
            	InputStream is = MoneyAmountUtils.class.getResourceAsStream("/generalJson/currency.js");
            	BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                StringBuilder jsonSb = new StringBuilder();
                String line = null;
                try {
                    while ((line = reader.readLine()) != null) {
                    	jsonSb.append(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                    	reader.close();
                    	is.close();
                    } catch (IOException e) {
                    	reader.close();
                    	is.close();
                        e.printStackTrace();
                    }
                }
                
                currencyInfos = JSON.parseArray(jsonSb.toString(), GeneralCurrencyInfo.class);
        	}
        } catch(Exception e) {
        	e.printStackTrace();
        }
    	
        if(CollectionUtils.isNotEmpty(currencyInfos)) {
        	for(GeneralCurrencyInfo currencyInfo : currencyInfos) {
        		if(currencyName.equals(currencyInfo.getCurrencyName())) {
        			return currencyInfo;
        		}
        	}
        }
        
        GeneralCurrencyInfo currencyInfo = new GeneralCurrencyInfo();
        currencyInfo.setCurrencyName(currencyName);
        currencyInfo.setCurrencyCapital("元");
        currencyInfo.setFractionalCurrency("分");
        List<FractionalCurrency> fractionalCurrencys = new ArrayList<>();
        FractionalCurrency fractionalCurrency = new FractionalCurrency();
        fractionalCurrency.setCurrencyMark("分");
        fractionalCurrency.setCractionalCount(100);
        fractionalCurrencys.add(fractionalCurrency);
        currencyInfo.setFractionalCurrencys(fractionalCurrencys);
        
    	return currencyInfo;
    }

	public static String getCurrencyNameBySerialNum(String uuid) {
		String currencyName = null;
		if(StringUtils.isNotBlank(uuid)){
			List<MoneyAmount> moneyAmounts = moneyAmountDao.findAmountBySerialNum(uuid);
			if(moneyAmounts != null && moneyAmounts.size() > 0) {
				MoneyAmount moneyAmount = moneyAmounts.get(0);
				if(moneyAmount != null) {
					Integer currencyId = moneyAmount.getCurrencyId();
					Currency currency = currencyService.findCurrency(currencyId.longValue());
					if(currency != null) {
						currencyName = currency.getCurrencyName();
					}
				}
			}
		}
		return currencyName;
	}
	
	public static List<MoneyAmount> getMoneyAmountsBySerialNum(String uuid) {
		List<MoneyAmount> moneyAmounts = moneyAmountDao.findAmountBySerialNum(uuid);
		return moneyAmounts;
	}
	
	/**
	 * 把无格式金额转成 金额格式字符串
	 * @return
	 */
	public static String formatAmountString(String srcAmount) {
		DecimalFormat myformat = new DecimalFormat();
		myformat.applyPattern("##,##0.00");
		BigDecimal bdAmount = BigDecimal.valueOf(Double.valueOf(srcAmount));
		String resultString = myformat.format(bdAmount);
		return resultString;
	}

	/**
	 * 多币种，按指定币种换汇
	 * @param tempMoneyAmounts
	 * @param string
	 * @return
	 */
	public static MoneyAmount translateMultplyCurrency2Specified(
			List<MoneyAmount> tempMoneyAmounts, String currencyId) {
		
		BigDecimal RMB = translateMultplyCurrency2RMB(tempMoneyAmounts);
		Currency currency = currencyService.findById(Long.parseLong(currencyId));
		BigDecimal resultAmount =  new BigDecimal("0");
		try {
			resultAmount = RMB.divide(currency.getCurrencyExchangerate(), 2);
		} catch (Exception e) {
			System.out.println(currencyId + "sss" + RMB);
		}
		
		MoneyAmount resultMoneyAmount = new MoneyAmount();
		resultMoneyAmount.setAmount(resultAmount);
		resultMoneyAmount.setCurrencyId(Integer.parseInt(currencyId));
		resultMoneyAmount.setExchangerate(currency.getCurrencyExchangerate());
		return resultMoneyAmount;
	}
	
	/**
	 * 多币种，按指定币种换汇(换人民币)
	 * @param tempMoneyAmounts
	 * @param string
	 * @return
	 */
	public static BigDecimal translateMultplyCurrency2RMB(
			List<MoneyAmount> tempMoneyAmounts) {
		BigDecimal amount = new BigDecimal(0);
		for (MoneyAmount moneyAmount : tempMoneyAmounts) {
			BigDecimal tempNum = moneyAmount.getAmount().multiply(moneyAmount.getExchangerate());
			amount = amount.add(tempNum);
		}
		return amount;
	}
	
	public  static String getMoneyStr(String moneyStr){
		moneyStr=moneyStr.replaceAll("-", "del");
		moneyStr=moneyStr.replaceAll("money,", "add");
		moneyStr=moneyStr.replaceAll("adddel", "-");
		moneyStr=moneyStr.replaceAll("add", "+");
		moneyStr=moneyStr.replaceAll("money", "");
		moneyStr=moneyStr.replaceAll("del", "-");
		return moneyStr;
	}
	
	/**
	 * 获取quauq结算价和服务费字符串
	 * @return
	 */
	public static String getQuauqClearChargeStr(String totalSerial, String chargeSerial) {
		String clearString = "总结算价：￥0.00";
		String chargeString = "服务费：￥0.00";
		if (StringUtils.isBlank(totalSerial) || StringUtils.isBlank(chargeSerial)) {
			return clearString + " " + chargeString;
		}
		List<MoneyAmount> totalAmounts = moneyAmountDao.findAmountBySerialNum(totalSerial);  // 总额
		List<MoneyAmount> chargeAmounts = moneyAmountDao.findAmountBySerialNum(chargeSerial);  // 服务费
		if (CollectionUtils.isEmpty(totalAmounts) || CollectionUtils.isEmpty(chargeAmounts)) {
			return clearString + " " + chargeString;
		}
		List<MoneyAmount> clearAmounts = moneyAmountService.calculation4MoneyAmountList(Context.SUBTRACT, totalAmounts, chargeAmounts);
		
		clearString = "总结算价：" + moneyAmountService.getMoneyStrFromAmountList("mark", clearAmounts);
		chargeString = "服务费" + moneyAmountService.getMoneyStrFromAmountList("mark", chargeAmounts);
		return clearString + " " + chargeString;
	}
	
}
