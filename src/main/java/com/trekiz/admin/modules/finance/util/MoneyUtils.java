package com.trekiz.admin.modules.finance.util;

import java.text.DecimalFormat;
import java.util.regex.Pattern;

/**
 * 金额工具类
 * @author gaoyang
 * @Time 2017-3-24 下午5:24:11
 */
public class MoneyUtils {

	/**
     * 去掉金额前面的币种符号
     * @author gaoyang
     * @Time 2017-3-24 下午5:21:01
     * @param moneyWithCurrency
     */
    public static String getMoneyRemoveCurrency(String moneyWithCurrency) {
    	// 验证是doule类型数据
    	Pattern pattern = Pattern.compile("[-+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?");
    	// 替换掉千位符
    	String moneyWithCurrencyStr = moneyWithCurrency.trim().replace(",", "");
    	String money = "";
    	int length = moneyWithCurrencyStr.length();
    	for (int i = 0; i < length; i ++) {
    		money = moneyWithCurrencyStr.substring(i, length);
    		if (pattern.matcher(money.trim()).matches()) {
        		break;
        	}
    	}
    	DecimalFormat df = new DecimalFormat(",###,##0.00");
    	return df.format(Double.parseDouble(money.trim()));
    }
}