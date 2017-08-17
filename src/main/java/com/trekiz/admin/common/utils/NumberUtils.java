package com.trekiz.admin.common.utils;

import org.apache.shiro.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NumberUtils {
	
	/**
	 * 数字排序并去重累加
	 * @Description: 
	 * @param @param fromList
	 * @param @return   
	 * @return List<Integer>  
	 * @throws
	 * @author majiancheng
	 * @date 2015-11-18 上午11:47:31
	 */
	public static List<Integer> cumulationNumber(List<Integer> fromList) {
		if(CollectionUtils.isEmpty(fromList)) {
			return null;
		}
		
		List<Integer> toList = new ArrayList<Integer>();
		Collections.sort(fromList);
		for(int i=0; i<fromList.size(); i++) {
			if(i == 0) {
				toList.add(fromList.get(i));
			} else {
				int prev = toList.get(i-1);
				int curr = fromList.get(i);
				
				if(curr <= prev) {
					toList.add(++prev);
				} else {
					toList.add(curr);
				}
			}
		}
		
		return toList;
	}
	
	/**
	 * 如果值为空则默认赋值为0 
	 * @author yakun.bai
	 * @Date 2016-5-19
	 */
	public static Double integerToDouble(Integer value) {
		if (value == null) {
			return 0.00;
		} else {
			return value.doubleValue();
		}
	}

	/**
	 *  判断该字符串是否是数字，并且还不超越Integer所表示的最大值
	 * @param number		待校验数值
	 * @return	b b==true,表示合法，否则非法，默认非法
	 * @author	shijun.liu
	 * @date	2016.08.22
     */
	public static boolean isInteger(String number){
		boolean b = false;
		if(StringUtils.isNumeric(number)){
			try {
				Integer.parseInt(number);
				b = true;
			}catch (NumberFormatException e){
				b = false;
			}
		}
		return b;
	}

	/**
	 *  判断该字符串是否是数字，并且还不超越Double所表示的最大值
	 * @param number		待校验数值
	 * @return	b b==true,表示合法，否则非法，默认非法
	 * @author	shijun.liu
	 * @date	2016.08.22
	 */
	public static boolean isDouble(String number){
		boolean b = false;
		try {
			Double.parseDouble(number);
			b = true;
		}catch (NumberFormatException e){
			b = false;
		}
		return b;
	}

	/**
	 * 判断该字符串是否是decimal数据类型
	 * @param number	待校验的数值
	 * @param total		总位数
	 * @param scale		小数点位数
     * @return
	 * @author	shijun.liu
	 * @date	2016.08.22
     */
	public static boolean isDecimal(String number, int total, int scale){
		boolean b = false;
		String reg = "^-{0,1}\\d{1,"+(total-scale)+"}";
		if(number.contains(".")){
			reg += "\\.\\d{1,"+scale+"}";
		}
		reg += "$";
		if(number.matches(reg)){
			b = true;
		}
		return b;
	}

	public static void main(String[] args){
		boolean b = isDecimal("3.3345", 3, 2);
		System.out.println(b);
	}

	/**
	 *  判断该字符串是否是数字，并且还不超越Long所表示的最大值
	 * @param number	数字
	 * @return	b b==true,表示合法，否则非法，默认非法
	 * @author	shijun.liu
	 * @date	2016.08.22
	 */
	public static boolean isLong(String number){
		boolean b = false;
		if(StringUtils.isNumeric(number)){
			try {
				Long.parseLong(number);
				b = true;
			}catch (NumberFormatException e){
				b = false;
			}
		}
		return b;
	}

}
