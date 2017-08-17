package com.trekiz.admin.common.utils;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ContextClassLoaderLocal;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author majiancheng
 * @Time 2015-4-2
 */
public class BeanUtil {


	static BeanUtilsBean beanUtils = null;
	static {
		final DateConverter dc = new DateConverter();
		dc.setPatterns(new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm", "HH:mm", "yyyy/MM/dd" });
		Converter dtConverter = new Converter() {
			@SuppressWarnings("rawtypes")
			@Override
			public Object convert(Class arg0, Object arg1) {
				if (StringUtils.isBlank((String) arg1)) {
					return null;
				}
				return dc.convert(arg0, arg1);
			}
		};
		ConvertUtilsBean convertUtilsBean = new ConvertUtilsBean();
		convertUtilsBean.deregister(Date.class);
		convertUtilsBean.register(dtConverter, Date.class);
		beanUtils = new BeanUtilsBean(convertUtilsBean);
	}

	private static Log log = LogFactory.getLog(BeanUtil.class);

	/**
	 * 金额格式化 add by zhanghao
	 * @param s 金额 默认2位小数
	 * @return 格式后的金额
	 */
	public static String numberFormat2String(Double s){
		return numberFormat2String(s, 2);
	}
	/**
	 * 金额格式化 add by zhanghao
	 * @param s 金额 默认2位小数
	 * @return 格式后的金额
	 */
	public static String numberFormat2String(String s){
		return numberFormat2String(s, 2);
	}
	/**
	 * 金额格式化 add by zhanghao
	 * @param s 金额
	 * @param len 小数位数
	 * @return 格式后的金额
	 */
	public static String numberFormat2String(Double s, int len){
		if(s==null || s==0){
			return "0.00";
		}
		return numberFormat2String(s.toString(), 2);
	}
	/**
	 * 金额格式化 add by zhanghao
	 * @param s 金额
	 * @param len 小数位数
	 * @return 格式后的金额
	 */
	public static String numberFormat2String(String s, int len) {
	    if (s == null || s.length() < 1) {
	        return "";
	    }
	    NumberFormat formater = null;
	    double num = Double.parseDouble(s);
	    if (len == 0) {
	        formater = new DecimalFormat("###,###");
	 
	    } else {
	        StringBuffer buff = new StringBuffer();
	        buff.append("###,###.");
	        for (int i = 0; i < len; i++) {
	            buff.append("0");
	        }
	        formater = new DecimalFormat(buff.toString());
	    }
	    return formater.format(num);
	}
	/**
	 * 金额去掉“,” add by zhanghao
	 * @param s 金额
	 * @return 去掉“,”后的金额
	 */
	public static String revertNumberFormat(String s) {
	    String formatString = "";
	    if (s != null && s.length() >= 1) {
	        formatString = s.replaceAll(",", "");
	    }
	    return formatString;
	}
	
	private static final ContextClassLoaderLocal BEANS_BY_CLASSLOADER = new ContextClassLoaderLocal() {
		protected Object initialValue() {
			return new BeanUtil();
		}
	};

	public static BeanUtil getInstance() {
		return (BeanUtil) BEANS_BY_CLASSLOADER.get();
	}

	public static BeanUtilsBean get() {
		return beanUtils;
	}

	public static void copyMap(Object object, Map<String, Object> dataMap) {
		if (object == null || dataMap == null || dataMap.isEmpty())
			return;
		Iterator<Entry<String, Object>> iter = dataMap.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, Object> entry = iter.next();
			String key = entry.getKey();
			try {
				beanUtils.copyProperty(object, key, entry.getValue());
			} catch (Exception e) {
				log.error(e);
			}
		}
	}

	
	/**
	 * 简单javabean属性复制工具类（包括String、两种Date、基本类型以及其包装类型）<br/>
	 * 字符串转日期类型和日期转字符串类型仅用于 mm
	 * null覆盖目标属性对应值
	 *@author majiancheng
	 *@time 2015-4-2,上午11:24:03
	 * @param dest 目标对象
	 * @param src 源对象
	 */
	public static void copySimpleProperties(Object dest,Object src){
		copySimpleProperties(dest, src, false);
	}
	
	/**
	 * 简单javabean属性复制工具类（包括String、两种Date、基本类型以及其包装类型）<br/>
	 * 字符串转日期类型和日期转字符串类型仅用于 mm
	 *@author majiancheng
	 *@time 2015-4-2,上午11:24:03
	 * @param dest 目标对象
	 * @param src 源对象
	 * @param b null值是否覆盖目标对象的属性  false:覆盖，true：不覆盖
	 * 
	 */
	public static void copySimpleProperties(Object dest,Object src,boolean b){
		if(src==null||dest==null){
			return ;
		}
         PropertyDescriptor[] srcprops = null;
		PropertyDescriptor[] destprops = null;
		try {
			srcprops = Introspector.getBeanInfo(src.getClass()).getPropertyDescriptors();  
			destprops = Introspector.getBeanInfo(dest.getClass()).getPropertyDescriptors();
			if(srcprops==null||destprops==null){
				return ;
			}
			for (int i = 0; i < srcprops.length; i++) {
				 for (int j = 0; j < destprops.length; j++) {
					if(srcprops[i].getName().equals(destprops[j].getName())
						&&srcprops[i].getReadMethod()!=null&&srcprops[i].getWriteMethod()!=null
						&&destprops[j].getReadMethod()!=null&&destprops[j].getWriteMethod()!=null
						&&srcprops[i].getPropertyType()!=null&&destprops[j].getPropertyType()!=null
						&&destprops[j].getPropertyType().equals(srcprops[i].getPropertyType())
						&&(srcprops[i].getPropertyType().equals(Integer.class)
								||srcprops[i].getPropertyType().equals(int.class)
								||srcprops[i].getPropertyType().equals(Double.class)
								||srcprops[i].getPropertyType().equals(double.class)
								||srcprops[i].getPropertyType().equals(Float.class)
								||srcprops[i].getPropertyType().equals(float.class)
								||srcprops[i].getPropertyType().equals(String.class)
								||srcprops[i].getPropertyType().equals(Date.class)
								||srcprops[i].getPropertyType().equals(java.sql.Date.class)
								||srcprops[i].getPropertyType().equals(short.class)
								||srcprops[i].getPropertyType().equals(Short.class)
								||srcprops[i].getPropertyType().equals(byte.class)
								||srcprops[i].getPropertyType().equals(Byte.class)
								||srcprops[i].getPropertyType().equals(char.class)
								||srcprops[i].getPropertyType().equals(Character.class)
								||srcprops[i].getPropertyType().equals(long.class)
								||srcprops[i].getPropertyType().equals(Long.class)
								)){
						Method srcGet = srcprops[i].getReadMethod();
						Object srcReturn = srcGet.invoke(src);
						//null是否覆盖目标对象属性
						if(b){
							if(srcReturn!=null){
								Method destSet = destprops[j].getWriteMethod();
								destSet.invoke(dest,srcReturn);
							}
						}else{
							Method destSet = destprops[j].getWriteMethod();
							destSet.invoke(dest,srcReturn);
						}
						
						break;
					}
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IntrospectionException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
        
	}
	/**
	 * 根据key对集合进行分组  add by zhanghao
	 * @param keyString 集合中的对象属性名
	 * @param list 要分组的源集合
	 * @return 分组后的Map key对应的是源集合对象中的属性名对应的属性值。
	 * @throws Exception 
	 */
	public static <T>Map<Object,List<T>> groupByKeyString(String keyString,List<T>sourceList){
		return groupByKeyString(keyString, sourceList, null);
	}
	public static <T>Map<Object,List<T>> groupByKeyString(String keyString,List<T>sourceList,Boolean b){
		if(CollectionUtils.isNotEmpty(sourceList)){
			Map<Object,List<T>> result = null;
			if(b==null){//按put顺序
				result = new LinkedHashMap<Object,List<T>>();
			}else if(b){//按自然顺序
				result = new TreeMap<Object,List<T>>();
			}else{//无序
				result = new HashMap<Object,List<T>>();
			}
			for(T t:sourceList){
				try {
					Field field = t.getClass().getDeclaredField(keyString);
					Method method = t.getClass().getMethod(getGetterMethod(field));
					Object obj = method.invoke(t);
					if(obj==null){
						continue;
					}
					if(result.containsKey(obj)){
						List<T> list = result.get(obj);
						list.add(t);
					}else{
						List<T> list = new ArrayList<T>();
						list.add(t);
						result.put(obj, list);
					}
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				} 
			}
			return result;
		}
		return null;
	}
	public static <T>Map<Object,T> groupByKeyString2Obj(String keyString,List<T>sourceList){
		return groupByKeyString2Obj(keyString, sourceList, false);
	}
	public static <T>Map<Object,T> groupByKeyString2Obj(String keyString,List<T>sourceList,Boolean b){
		if(CollectionUtils.isNotEmpty(sourceList)){
			Map<Object,T> result = null;
			if(b==null){//按put顺序
				result = new LinkedHashMap<Object,T>();
			}else if(b){//按自然顺序
				result = new TreeMap<Object,T>();
			}else{//无序
				result = new HashMap<Object,T>();
			}
			for(T t:sourceList){
				try {
					Field field = t.getClass().getDeclaredField(keyString);
					Method method = t.getClass().getMethod(getGetterMethod(field));
					Object obj = method.invoke(t);
					result.put(obj, t);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				} 
			}
			return result;
		}
		return null;
	}
	public static String getSetterMethod(Field field){
		String methodname = "set"+field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1);
		return methodname;
	}
	public static String getGetterMethod(Field field){
		String methodname = "get"+field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1);
		return methodname;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> void copyCollection(Collection<T> dests, Collection<T> srcs, Class<T> clazz) {
		try{
			if(CollectionUtils.isNotEmpty(srcs)) {
				for(Object src : srcs) {
					Object dest = clazz.newInstance();
					copySimpleProperties(dest, src);
					dests.add((T) dest);
				} 
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将集合根据符号拼接成字符串
		 * @Title: joinListWithSign
	     * @return String
	     * @author majiancheng       
	     * @date 2015-10-23 下午12:57:50
	 */
	public static String joinListWithSign(List<String> list, String sign) {
		StringBuffer sb = new StringBuffer();
		
		if(CollectionUtils.isNotEmpty(list)) {
			for(String str : list) {
				sb.append(str);
				sb.append(sign);
			}
			sb.delete(sb.lastIndexOf(sign), sb.length()-1);
		}
		
		return sb.toString();
	}
	
	/**
	 * 将字符串数组根据符号拼接成字符串
		 * @Title: joinArrayWithSign
	     * @return String
	     * @author majiancheng       
	     * @date 2015-10-23 下午1:00:54
	 */
	public static String joinArrayWithSign(String[] array, String sign) {
		StringBuffer sb = new StringBuffer();
		
		if(array != null && array.length > 0) {
			for(String str : array) {
				sb.append(str);
				sb.append(sign);
			}
			sb.delete(sb.lastIndexOf(sign), sb.length()-1);
		}
		
		return sb.toString();
	}
	
}
