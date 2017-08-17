package com.trekiz.admin.modules.statemachine.context;

import com.trekiz.admin.modules.statemachine.state.SpecificState;

import java.util.HashMap;
import java.util.Map;

public class StateMachineContext {
	
	private static Map<String, SpecificState> stateMap = new HashMap<String, SpecificState>();
	
	private static Map<String, Integer> stateValueMap = new HashMap<String, Integer>();
	
	private static Map<String, Class<?>> mutableBeanMap = new HashMap<String, Class<?>>();
	
	private static Map<String, String> mutableBeanStartStateMap = new HashMap<String, String>();
	
	private static Map<String, String> stateDescMap = new HashMap<String, String>();
	
	public static void addState(String key, SpecificState stateBean, Integer value, String desc){
		stateMap.put(key, stateBean);
		stateValueMap.put(key, value);
		stateDescMap.put(key, desc);
	}
	
	public static void removeState(String stateName){
		stateMap.remove(stateName);
		stateValueMap.remove(stateName);
	}
	
	public static SpecificState getState(String stateName){
		return stateMap.get(stateName);
	}
	
	public static Integer getStateValue(String stateName){
		return stateValueMap.get(stateName);
	}
	
	public static String getStateDesc(String stateName){
		return stateDescMap.get(stateName);
	}
	
	public static String getStateDescByNum(Integer state){
		for(String key : stateValueMap.keySet()){
			if(state.equals(stateValueMap.get(key))){
				return stateDescMap.get(key);
			}
		}
		return "";
	}
	
	public static void addMutableBeanType(String beanName, Class<?> type, String startState){
		mutableBeanMap.put(beanName, type);
		mutableBeanStartStateMap.put(beanName, startState);
	}
	
	public static Class<?> getMutableBeanType(String beanName){
		return mutableBeanMap.get(beanName);
	}
	
	public static String getMutableBeanStartState(String beanName){
		return mutableBeanStartStateMap.get(beanName);
	}
	
}
