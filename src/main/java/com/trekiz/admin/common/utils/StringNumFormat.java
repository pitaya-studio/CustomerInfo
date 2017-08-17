package com.trekiz.admin.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;

public class StringNumFormat {

	//不存在舍去
	private final static String[] FORMATS_N = { "0", "0.0", "0.00", "0.000",
			"0.0000", "0.00000", "0.000000", "0.0000000", "0.00000000" };
	//不存在显示为0
	private final static String[] FORMATS_Y = { "#", "#.#", "#.##", "#.###",
			"#.####", "#.#####", "#.######", "#.#######", "#.########" };
	
	/**
	 * 将double转换为字符串,如果不存在相应的位数，则省去
	 * @param d double数值
	 * @param num 指定的格式化
	 * @return
	 */
	public static double parseDoubleN(double d,int num) {
		DecimalFormat fmt = getFormat(FORMATS_N[num]);
		String s = fmt.format(d);
		return Double.parseDouble(s);
	}
	
	/**
	 * 将double转换为字符串,如果不存在相应的位数,则显示为0
	 * @param d double数值
	 * @param num 指定的格式化
	 * @return
	 */
	public static double parseDoubleY(double d,int num) {
		DecimalFormat fmt = getFormat(FORMATS_Y[num]);
		String s = fmt.format(d);
		return Double.parseDouble(s);
	}
	
	/**
	 * 返回相应的格式化类型
	 * @param format 格式化字符串
	 * @return
	 */
	public static DecimalFormat getFormat(String format) {
		DecimalFormat fmt = new DecimalFormat(format);
		fmt.setDecimalSeparatorAlwaysShown(false);
		return fmt;
	}
	
	/**
	 * 将数字字符串转为指定精度的大数据
	 * 如果不存在相应的位数，则省去
	 * @param d 数字字符串
	 * @param num 指定的格式化
	 * @return
	 */
	public static BigDecimal getBigDecimalN(String d,int num){
		BigDecimal dci=new BigDecimal(d);
		DecimalFormat fmt = getFormat(FORMATS_N[num]);
		return new BigDecimal(fmt.format(dci.doubleValue()));
	}
	
	/**
	 * 将数字字符串转为指定精度的大数据
	 * 如果不存在相应的位数,则显示为0
	 * @param d 数字字符串
	 * @param num 指定的格式化
	 * @return
	 */
	public static BigDecimal getBigDecimalY(String d,int num){
		BigDecimal dci = new BigDecimal(d);
		DecimalFormat fmt = getFormat(FORMATS_Y[num]);
		return new BigDecimal(fmt.format(dci.doubleValue()));
	}
	
	/**
	 * 将数字字符串转为指定精度的浮点数
	 * 如果不存在相应的位数，则省去
	 * @param d 数字字符串
	 * @param num 指定的格式化
	 * @return
	 */
	public static Double getDoubleN(String d,int num){
		BigDecimal dci = getBigDecimalN(d, num);
		double value = dci.doubleValue();
		return value;
	}
	
	/**
	 * 将数字字符串转为指定精度的浮点数
	 * 如果不存在相应的位数,则显示为0
	 * @param d 数字字符串
	 * @param num 指定的格式化
	 * @return
	 */
	public static Double getDoubleY(String d,int num){
		BigDecimal dci = getBigDecimalY(d, num);
		double value = dci.doubleValue();
		return value;
	}
	
	/**
	 * 将数字字符串转为指定精度的浮点数
	 * 如果不存在相应的位数，则省去
	 * @param d 数字字符串
	 * @param num 指定的格式化
	 * @return
	 */
	public static Float getFloatN(String d,int num){
		BigDecimal dci = getBigDecimalN(d, num);
		float value = dci.floatValue();
		return value;
	}
	
	/**
	 * 将数字字符串转为指定精度的浮点数
	 * 如果不存在相应的位数,则显示为0
	 * @param d 数字字符串
	 * @param num 指定的格式化
	 * @return
	 */
	public static Float getFloatY(String d,int num){
		BigDecimal dci = getBigDecimalY(d, num);
		float value = dci.floatValue();
		return value;
	}
	
	public static int getIntValue(String d){
		
		int value = 0;
		try {
			value = Integer.parseInt(d);
		} catch (Exception e) {
			value = 0;
		}
		return value;
	}
	
	public static Integer getIntegerValue(String d){
		
		Integer value = new Integer(getIntValue(d));
		return value;
	}
	
	public static BigDecimal getBigDecimalValue(String s) {
		BigDecimal value = new BigDecimal(s);
		return value;
	}
	
	public static String getFmtToString(String d){
		try {
			Double.valueOf(d);
		} catch (Exception e) {
			// TODO: handle exception
			return "0";
		}
		return d;
	}

	/**
	 * 处理 两个 BigDecimal 参数相加，并支持 null 情况
	 * @param bigmal_one
	 * @param bigmal_two
	 * @return
	 */
	public static BigDecimal getBigDecimalAdd(BigDecimal bigmal_one,BigDecimal bigmal_two){
		BigDecimal result = null;
		if(bigmal_one==null && bigmal_two==null){
			return result;
		}else{
			if(bigmal_one==null){
				return bigmal_two;
			}else if(bigmal_two==null){
				return bigmal_one;
			}else{
				return bigmal_one.add(bigmal_two);
			}
			
		}
	}
	
	/**
	 * 处理 两个 BigDecimal 参数相减，并支持 null 情况
	 * @param bigmal_one
	 * @param bigmal_two
	 * @return
	 */
	public static BigDecimal getBigDecimalSubTract(BigDecimal bigmal_one,BigDecimal bigmal_two){
		BigDecimal result = null;
		if(bigmal_one==null && bigmal_two==null){
			return result;
		}else{
			
			if(bigmal_one==null){
				return bigmal_two.negate();//取负数
			}else if(bigmal_two==null){
				return bigmal_one.negate();//取负数
			}else{
				return bigmal_one.subtract(bigmal_two);
			}
			
		}
	}
	
	/**
	 * 处理 两个 BigDecimal 参数相乘，并支持 null 情况
	 * @param bigmal_one
	 * @param bigmal_two
	 * @return
	 */
	public static BigDecimal getBigDecimalMultiply(BigDecimal bigmal_one,BigDecimal bigmal_two){
		BigDecimal result = null;
		if(bigmal_one!=null && bigmal_two!=null){
				return bigmal_one.multiply(bigmal_two);
		}else{
			return result;
		}
	}
	
	/**
	 * 处理 两个 BigDecimal 参数相除，并支持 null 情况
	 * @param bigmal_one
	 * @param bigmal_two
	 * @return
	 */
	public static BigDecimal getBigDecimalDivide(BigDecimal bigmal_one,BigDecimal bigmal_two,int len){
		BigDecimal result = null;
		if(bigmal_one!=null && bigmal_two!=null){
			if(bigmal_two==BigDecimal.ZERO)
				return result;
			else{
				return bigmal_one.divide(bigmal_two,len,BigDecimal.ROUND_HALF_UP);//四舍五入
			}
		}else{
			return result;
		}
	}
	
	/**
	 * 将数字字符串转为精度2的大数据
	 * 如果不存在相应的位数,则显示为0
	 * @param d 数字字符串
	 * @param num 指定的格式化
	 * @return
	 */
	public static BigDecimal getBigDecimalForTow(String d){
		if(StringUtils.isNotBlank(d)){
			BigDecimal dci = new BigDecimal(d);
			DecimalFormat fmt = getFormat(FORMATS_N[2]);
			return new BigDecimal(fmt.format(dci.doubleValue()));
		}
		return BigDecimal.ZERO;
	}
	
	private static final String UNIT = "万仟佰拾亿仟佰拾万仟佰拾元角分";  
    private static final String DIGIT = "零壹贰叁肆伍陆柒捌玖";  
    private static final double MAX_VALUE = 9999999999999.99D;
    /**
     * 
    * @Title: change
    * @Description: TODO(将小写金额转换为大写)
    * @param @param v
    * @return String    返回类型
    * @throws
     */
    public static String changeAmount(String amount) {
    	if(StringUtils.isNotBlank(amount)) {
			double v = Double.parseDouble(amount);
			if (v < 0 || v > MAX_VALUE) {
				return "参数非法!";
			}
			long l = Math.round(v * 100);
			if (l == 0) {
				return "零元整";
			}
			String strValue = l + "";
			// i用来控制数
			int i = 0;
			// j用来控制单位
			int j = UNIT.length() - strValue.length();
			String rs = "";
			boolean isZero = false;
			for (; i < strValue.length(); i++, j++) {
				char ch = strValue.charAt(i);
				if (ch == '0') {
					isZero = true;
					if (UNIT.charAt(j) == '亿' || UNIT.charAt(j) == '万'
							|| UNIT.charAt(j) == '元') {
						rs = rs + UNIT.charAt(j);
						isZero = false;
					}
				} else {
					if (isZero) {
						rs = rs + "零";
						isZero = false;
					}
					rs = rs + DIGIT.charAt(ch - '0') + UNIT.charAt(j);
				}
			}
			if (!rs.endsWith("分")) {
				rs = rs + "整";
			}
			rs = rs.replaceAll("亿万", "亿");
			return rs;
		}
    	return "";
    }
	
}
