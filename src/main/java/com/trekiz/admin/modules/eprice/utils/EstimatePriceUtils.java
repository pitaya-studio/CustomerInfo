package com.trekiz.admin.modules.eprice.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EstimatePriceUtils {
	/*
	 * type类型
	 */
	public static Map<String,String> TYPE_SHOW;
	
	
	/*
	 * 再次询价展示用    
	 * 出发时刻
	 */
	public static List<DicBean> startTimeTypeList;
	/*
	 * 再次询价展示用    
	 * 仓位等级
	 */
	public static List<DicBean>  aircraftSpaceLevelList;
	
	/*
	 * 再次询价展示用    
	 * 仓位
	 */
	public static List<DicBean>  aircraftSpaceList;
	
	/*
	 * 再次询价展示用    
	 * 仓位
	 */
	public static List<DicBean>  startTimeList;
	
	static {
//		1 单团\r\n            2 大客户\r\n            3 游学\r\n            4 自由行 ',
		TYPE_SHOW = new HashMap<String,String>();
		TYPE_SHOW.put("1", "单团");
		TYPE_SHOW.put("3", "游学");
		TYPE_SHOW.put("4", "大客户");
		TYPE_SHOW.put("5", "自由行");
		TYPE_SHOW.put("7", "机票");
		
		startTimeTypeList = new ArrayList<DicBean>();
		{
			startTimeTypeList.add(new DicBean("1","早"));
			startTimeTypeList.add(new DicBean("2","中"));
			startTimeTypeList.add(new DicBean("3","晚"));
		}
		
		aircraftSpaceLevelList = new ArrayList<DicBean>();
		{
			aircraftSpaceLevelList.add(new DicBean(0,"不限"));
			aircraftSpaceLevelList.add(new DicBean(1,"头等"));
			aircraftSpaceLevelList.add(new DicBean(2,"公务"));
			aircraftSpaceLevelList.add(new DicBean(3,"经济"));
		}
		
		aircraftSpaceList = new ArrayList<DicBean>();
	
		{
			aircraftSpaceList.add(new DicBean("0","不限"));
			aircraftSpaceList.add(new DicBean("Y","Y舱"));
			aircraftSpaceList.add(new DicBean("K","K舱"));
		}
		
		startTimeList  = new ArrayList<DicBean>();
	
		{
			startTimeList.add(new DicBean("0","0.00"));
			startTimeList.add(new DicBean("1","1.00"));
			startTimeList.add(new DicBean("2","2.00"));
			startTimeList.add(new DicBean("3","3.00"));
			startTimeList.add(new DicBean("4","4.00"));
			startTimeList.add(new DicBean("5","5.00"));
			startTimeList.add(new DicBean("6","6.00"));
			startTimeList.add(new DicBean("7","7.00"));
			startTimeList.add(new DicBean("8","8.00"));
			startTimeList.add(new DicBean("9","9.00"));
			startTimeList.add(new DicBean("10","10.00"));
			startTimeList.add(new DicBean("11","11.00"));
			startTimeList.add(new DicBean("12","12.00"));
			startTimeList.add(new DicBean("13","13.00"));
			startTimeList.add(new DicBean("14","14.00"));
			startTimeList.add(new DicBean("15","15.00"));
			startTimeList.add(new DicBean("16","16.00"));
			startTimeList.add(new DicBean("17","17.00"));
			startTimeList.add(new DicBean("18","18.00"));
			startTimeList.add(new DicBean("19","19.00"));
			startTimeList.add(new DicBean("20","20.00"));
			startTimeList.add(new DicBean("21","21.00"));
			startTimeList.add(new DicBean("22","22.00"));
			startTimeList.add(new DicBean("23","23.00"));
		}
	}
	

	/**
	 * 日志中询价类型中文转换
	 * @author gao
	 * @param type
	 * @return
	 */
	public static String backType(Integer type){
		if(type==1){
			return "单团";
		}else if(type==4){
			return "大客户";
		}else if(type==3){
			return "游学";
		}else if(type==5){
			return "自由行";
		}else if(type==7){
			return "机票";
		}else{
			return "";
		}
	}
	/**
	 *  日志中预算类型中文转换
	 * @author gao
	 * @param type
	 * @return
	 */
	public static String backBudgetType(Integer type){
		if(type==1){
			return "按人报价";
		}else{
			return "按团报价";
		}
	}

}
