package com.trekiz.admin.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.collect.Lists;

/**
 * 
 * 主要用于金额数据的格式化，一般为千分位形式
 * @author shijun.liu
 *
 */
public class MoneyNumberFormat {

	//千分位格式，并且保留两位小数
	public static final String THOUSANDST_POINT_TWO = ",###.00";
	
	//保留两位小数
	public static final String POINT_TWO = "#.00";
	
	//保留三位小数
	public static final String POINT_THREE = "#.000";
	
	public static final String FMT_MICROMETER = "###,##0.00";

	public static final Integer FMT_TYPE_THOUSAND = 1;
	public static final Integer FMT_TYPE_THOUSAND_POINT = 2;

	
	/**
	 * 将Long类型数据转换为千分位显示的形式,假如为零Java默认显示为 .00
	 * 格式如下：0         --> 0.00
	 *        0.123     --> 0.12
	 *        0.456     --> 0.46
	 *        123.456   --> 123.46
	 *        56789.00  --> 56,789.00
	 *        45,456.456--> 45,456.46
	 * @param
	 * @return
	 */
	public static String getThousandsMoney(Double d, String pattern){
		StringBuilder str = new StringBuilder();
		NumberFormat thousandsFormat = new DecimalFormat(pattern);
		if(null == d){
			throw new RuntimeException("输入值 [" + d + "] 不能为空");
		}
		String value = thousandsFormat.format(d);
		if(value.startsWith(".")){
			str.append("0");
		}
		if(value.startsWith("-.")){
			value = value.replace("-.", "-0.");
		}
		return str.append(value).toString();
	}
	
	/**
	 * 根据样式，格式化金额
		* 
		* @param 
		* @return String
		* @author majiancheng
		* @Time 2015-5-7
	 */
	public static String fmtMicrometer(String text, String pattern){
		DecimalFormat df = null;
		df = new DecimalFormat(pattern);
		double number = 0.0;
		try {
			number = Double.parseDouble(text);
		} catch (Exception e) {
			number = 0.0;
		}
		return df.format(number);
	}

	/**
	 * 把BigDecimal转换成带千分位的字符串。根据fmtType参数可以转换为带小数点的和不带小数的形式。
	 * @param fmtType FMT_TYPE_THOUSAND 不带小数点。FMT_TYPE_THOUSAND_POINT带小数点。
	 * @param value
     * @return
     */
	public static String thousandSymbol(Integer fmtType, BigDecimal value) {
		if (fmtType == null || value == null) {
			return null;
		}
		DecimalFormat fmt;
		if (fmtType == FMT_TYPE_THOUSAND) {
			// 千分位不带小数点的格式
			fmt = new DecimalFormat("###,###,###");
			return fmt.format(value);
		}
		if (fmtType == FMT_TYPE_THOUSAND_POINT) {
			// 千分位带小数点的格式
			fmt = new DecimalFormat("###,###,##0.00");
			return fmt.format(value);
		}
		return null;
	}
	
	/**
	 * 将金额数据四舍五入
	 * @param d                数据
	 * @param pattern          转换格式
	 * @return
	 */
	public static String getRoundMoney(Double d, String pattern){
		StringBuilder str = new StringBuilder();
		NumberFormat thousandsFormat = new DecimalFormat(pattern);
		if(null == d){
			throw new RuntimeException("输入值 [" + d + "] 不能为空");
		}
		String value = thousandsFormat.format(d);
		if(value.startsWith(".")){
			str.append("0");
		}
		if(value.startsWith("-.")){
			value = value.replace("-.", "-0.");
		}
		return str.append(value).toString();
	}
	/**
	 * 小数点处理
	 * @param num 
	 * @param index 保留的小数点的位数
	 * @param type  BigDecimal.ROUND_DOWN 直接删除多余的小数位，如 getRoundMoney(2.35,1,BigDecimal.ROUND_DOWN)会变成2.3 
	 *              BigDecimal.ROUND_UP   进位处理，getRoundMoney(2.351,1,BigDecimal.ROUND_UP)变成2.36
	 *              BigDecimal.ROUND_HALF_UP 四舍五入
	 *@return String
	 *@author wangyang
	 * */
	public static String getRoundMoney(BigDecimal num, int index, int type){
		if(num == null){
			throw new NullPointerException();
		}else{
			return num.setScale(index, type).toString();
		}
	}
	/**
	 * 格式化汇率
	 * @param rate            汇率值
	 * @param pattern         格式
	 * @return
	 */
	public static String getFormatRate(Double rate, String pattern){
		StringBuilder str = new StringBuilder();
		NumberFormat rateFormat = new DecimalFormat(pattern);
		if(null == rate){
			throw new RuntimeException("输入值 [" + rate + "] 不能为空");
		}
		String value = rateFormat.format(rate);
		if(value.startsWith(".")){
			str.append("0");
		}
		if(value.startsWith("-.")){
			value = value.replace("-.", "-0.");
		}
		return str.append(value).toString();
	}
	
	/**
	 * 将输入的数值转化为千分位格式(进行四舍五入)
	 * @param value		被转化的数值
	 * @param decimals	保留小数位,数值大于0
	 * @return
	 * @author shijun.liu
	 */
	public static String getThousandsByRegex(String value, int decimals){
		if(null == value){
			return value;
		}
		if(decimals <= 0){
			throw new RuntimeException("小数位数必须大于0");
		}
		//判断传入的值是否为数值
		boolean b = isNumber(value);
		//转化千分位的正则表达式
		String regex = "(\\d{1,3})(?=(\\d{3})+(\\.\\d{"+decimals+"})$)";
		//匹配数据的正则表达式
		String findRegex = "[\\-|+]?\\d+(\\.\\d{"+decimals+"})";
		if(b){
			BigDecimal data = new BigDecimal(value);
			data = data.setScale(decimals, BigDecimal.ROUND_HALF_UP);
			value = data.toString();
			int index = value.indexOf(".");
			if(-1 == index){//是整数，不是小数
				String str = ".";
				for (int i = 0; i < decimals; i++) {
					str += "0";
				}
				value += str;
				return value.replaceAll(regex, "$1,");
			}else{
				//数据的小数位数
				int decimalCount = value.length() - value.indexOf(".") - 1;
				if((decimalCount - decimals) < 0){//已有的小数位数不到要保留的小数位数时，补零
					for (int i = 0; i < decimals - decimalCount; i++) {
						value += "0";
					}
				}
				Pattern p = Pattern.compile(findRegex);
				Matcher matcher = p.matcher(value);
				if(matcher.find()){//注意一定要先执行find(),才能执行 group()，并且matcher对象为一个， 否则报错。
					/**
					 *  取出匹配到的值
					 *  例如：123456.342  保留2位小数，取得s的值为 123456.34
					 */
					String s = matcher.group();
					return s.replaceAll(regex, "$1,");
				}
				return value.replaceAll(regex, "$1,");
			}
		}else{
			throw new RuntimeException("输入的值["+value+"]不为数值，请检查");
		}
	}
	
	/**
	 * 是否是数值
	 * @param value	格式为：+23.900，-23.90,23.98，23456 均为合法
	 * @return 是否合法
	 * @author shijun.liu
	 */
	public static boolean isNumber(String value){
		String regex = "^[\\-\\+]?\\d*\\.?(\\d+)$";
		return Pattern.matches(regex, value);
	}
	
	/*
	 * 将字符串拼接形成的金额拆分成币种和数值组成的数组列表，List中的Object[]为币种和金额数字的二维数组
	 * 例如：“￥10+$10”拆分成[{“$”,“10”},{“￥”，“10”}]
	 * @param money 金额字符串
	 * @param split 拼接符
	 * added by xianglei.dong 2016-03-29
	 */
	public static List<Object[]> getMoneyFromString(String money, String split) {
		if(StringUtils.isNotBlank(money) && StringUtils.isNotBlank(split)) {
			List<Object[]> list = Lists.newArrayList();
			String[] moneys = money.split(split);
			for(int i=0; i<moneys.length; i++) {
				String mon = moneys[i];
				int j = 0;
				while((mon.charAt(j)<'0' || mon.charAt(j)>'9') && mon.charAt(j)!='-') {
					j++;
				}
				Object[] objects = new Object[]{mon.substring(0, j).replaceAll(" ", ""), mon.substring(j).replaceAll(",","").replaceAll(" ", "")};
				list.add(objects);
			}
			return list;
		}
		return null;
	}
	
	/*
	 * 将double类型的金额进行大写转换， 工具
	 * @param n 金额
	 * @author xianglei.dong
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
	 * 解析字符串："$500.00"解析为数组[0]为"$",[1]为"500.00"
	 * */
	public static String[] resolveMoney(String money) {
		
		int split = 0;
		
		for (int i = 0; i < money.length(); i++) {
			if ((money.charAt(i) >= '0' && money.charAt(i) <= '9') || money.charAt(i) == '-') {
				split = i;
				break;
			}
		}
		
		return new String[]{money.substring(0, split), money.substring(split)};
		
	}
	public static void main(String[] args) {
		//System.out.println(getThousandsByRegex("6.325", 2));
		//System.out.println(getThousandsMoney(Double.valueOf("6.325"), THOUSANDST_POINT_TWO));
		
//		System.out.println(getRoundMoney(new BigDecimal(68.257), 2, BigDecimal.ROUND_DOWN));
		
	}
}
