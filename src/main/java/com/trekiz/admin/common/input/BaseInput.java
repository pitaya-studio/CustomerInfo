package com.trekiz.admin.common.input;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.trekiz.admin.common.utils.DateUtils;

public class BaseInput implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	public static Map<Class<?>,Method> methodMap = new HashMap<Class<?>, Method>();
	public static Map<Class<?>,Class<?>> classMap= new HashMap<Class<?>, Class<?>>();
	
	//空字符串的占位符 , 以免出现 空字符串 切分出现的位置匹配 不对的情况。
	public static String REGEX="_";
	
	private Map<String,BaseInputBean> baseInputBeanMap;
	static{
		try {
			if(!methodMap.containsKey(int.class)){
				//其他数据类型和其转换成String类型的静态方法 映射关系
				methodMap.put(int.class,Integer.class.getMethod("parseInt",java.lang.String.class));
				methodMap.put(java.lang.Integer.class,Integer.class.getMethod("parseInt",java.lang.String.class));
				methodMap.put(double.class,Double.class.getMethod("parseDouble",java.lang.String.class));
				methodMap.put(java.lang.Double.class,Double.class.getMethod("parseDouble",java.lang.String.class));
				methodMap.put(float.class,Float.class.getMethod("parseFloat",java.lang.String.class));
				methodMap.put(java.lang.Float.class,Float.class.getMethod("parseFloat",java.lang.String.class));
				methodMap.put(long.class,Long.class.getMethod("parseLong",java.lang.String.class));
				methodMap.put(java.lang.Long.class,Long.class.getMethod("parseLong",java.lang.String.class));
				methodMap.put(java.util.Date.class,DateUtils.class.getMethod("string2Date",java.lang.String.class));
			}
			
			if(!classMap.containsKey(java.lang.Integer.class)){
				//数据类型和其封装类的映射关系
				classMap.put(java.lang.Integer.class,int.class);
				classMap.put(java.lang.Double.class,double.class);
				classMap.put(java.lang.Float.class,float.class);
				classMap.put(java.lang.Long.class,long.class);
			}
			
			
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * 根据拼接的规则字符串进行切分，并返回对应的class 对象集合 add by zhanghao 
	 * 例如：111#222#333；444#555#666
	 * @param bean 设置拆分规则、返回属性、对应的class映射属性 等。
	 * @param value 要拆分的字符串
	 * @return
	 * @throws Exception
	 */
	public  <T> List<T> transfer2Object (BaseInputBean bean,Object value) throws Exception{
		return transfer2Object(bean, value.toString());
	}
	@SuppressWarnings("unchecked")
	public  <T> List<T> transfer2Object (BaseInputBean bean,String value) throws Exception{
		List<T> list = new ArrayList<T>();
		if(bean!=null && ArrayUtils.isNotEmpty(bean.getPropertyName())&&StringUtils.isNotBlank(value)){
			Class<?> classz = bean.getClassz();
			String[] superArray = value.split(bean.getRegexSuper());
			if(ArrayUtils.isNotEmpty(superArray)){
				for(int i=0;i<superArray.length;i++){
					T t = (T)classz.newInstance();
					
					String[] childArray = superArray[i].split(bean.getRegexChild());
					if(ArrayUtils.isNotEmpty(childArray)){
						for(int j=0;j<childArray.length;j++){
							Field field = classz.getDeclaredField(bean.getPropertyName()[j]);
							Method setMethod = classz.getMethod(getSetterMethod(field), field.getType());

							if(field.getType().isAssignableFrom(childArray[j].getClass())
									||(classMap.containsKey(childArray[j].getClass())
											&&field.getType().isAssignableFrom(classMap.get(childArray[j].getClass())))){
								if(childArray[j].contains(REGEX)){
									setMethod.invoke(t, childArray[j].replace(REGEX, ""));
								}else{
									setMethod.invoke(t, childArray[j]);
								}
									
							}else{
								if(childArray[j].contains(REGEX)){
									Object objValue = methodMap.get(field.getType()).invoke(null, childArray[j].replace(REGEX, "0"));
									setMethod.invoke(t, objValue);
								}else{
									Object objValue = methodMap.get(field.getType()).invoke(null, childArray[j]);
									setMethod.invoke(t, objValue);
								}
							}
						}
					}
					list.add(t);
				}
			}
		}
		
		return list;
	}
	
	/**
	 * 对象中的特殊属性（指定前缀）和指定类 进行映射处理
	 * 支持对属性的中的拼接字符串进行切分并保存到相应的属性list中
	 * 
	 * @param targetClass 转型目标类
	 * @param prefix 指定的属性前缀
	 * @param sourceObj 转换源对象
	 * @param baseBeanMap 设置子表拆分规则的bean ，每个bean对应一个需要切分的属性
	 * @param validataProperName 需要验证的必要字段，不为空时会验证行记录中的此字段是否为空，如果字段值为空则不进行该行记录的对象映射创建
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public  <T,E>List<T> transfer2Object(Class<T> targetClass,String prefix,E sourceObj,String[] validataProperNames) throws Exception  {
		try {
			try {
				Field baseInputBeanMapField = sourceObj.getClass().getDeclaredField("baseInputBeanMap");
				if(Modifier.isStatic(baseInputBeanMapField.getModifiers()) ){
					this.setBaseInputBeanMap((Map<String,BaseInputBean>)baseInputBeanMapField.get(null));
				}
			} catch (Exception e1) {
				System.out.println("查找静态变量baseInputBeanMap时报错。"+e1.toString());
			}
			
			//判断是否需要验证 必填字段
			boolean isVali= false;
			Object[] validataProperValues = null;
			if(ArrayUtils.isNotEmpty(validataProperNames)){
				validataProperValues = new Object[validataProperNames.length];
				for(int i=0;i<ArrayUtils.getLength(validataProperNames);i++){
					Field validataProperNameField = sourceObj.getClass().getDeclaredField(validataProperNames[i]);
					Method validataProperMethod = sourceObj.getClass().getMethod(getGetterMethod(validataProperNameField));
					validataProperValues[i] = validataProperMethod.invoke(sourceObj);
				}
				
				isVali=true;
			}
			
			
			List<T> list = new ArrayList<T>();
			Field[] fields = sourceObj.getClass().getDeclaredFields();
			if(ArrayUtils.isNotEmpty(fields)){
				for(Field field:fields){
					if(field.getName().startsWith(prefix)){//判断需要做切分的属性，以前缀为判断依据
						
						//数据源对象的属性值
						Method sourceMethod = sourceObj.getClass().getMethod(getGetterMethod(field));
						Object fieldValue = sourceMethod.invoke(sourceObj);
						if(fieldValue==null){
							continue;
						}
						//此处判断是否是数组，非数组建议不在此处转换
						if(fieldValue.getClass().isArray()){
							for(int i=0;i<ArrayUtils.getLength(fieldValue);i++){
								
								//验证必填字段，为空跳出循环，不进行属性映射和创建对象。
								if(isVali&&validataProperValues!=null){
									boolean isbreak=false;
									for(Object validataProperValue:validataProperValues){
										if(validataProperValue.getClass().isArray()&&StringUtils.isBlank(((Object[])validataProperValue)[i].toString())){
											isbreak=true;
											break;
										}else if(!validataProperValue.getClass().isArray() && StringUtils.isBlank(validataProperValue.toString())){
											isbreak=true;
											break;
										}
									}
									if(isbreak){
										list.add(null);//如果验证不通过，放入null值，防止下面的属性映射是造成 数组 越界的异常
										break;
									}
								}
								
								
								//返回list中的对象的创建并进行属性的设置
								if(list.size()<ArrayUtils.getLength(fieldValue)){
									T t = targetClass.newInstance();
									list.add(t);
								}
								
								//是否存在需要切分的字表数据字符串 的判断
								if(baseInputBeanMap!=null && baseInputBeanMap.containsKey(field.getName())){
									String childListName = baseInputBeanMap.get(field.getName()).getChildListName();
									Method childListMethod = targetClass.getMethod(getSetterMethod(childListName), targetClass.getDeclaredField(childListName).getType());
									List<T> childList = transfer2Object(baseInputBeanMap.get(field.getName()), ((Object[])fieldValue)[i]);
									childListMethod.invoke(list.get(i), childList);
								}else{
									String propertyName = field.getName().replaceFirst(prefix, "");
									Field targetField=null;
									try {
										targetField = targetClass.getDeclaredField(propertyName);
									} catch (NoSuchFieldException e) {
										System.out.println("映射到目标对象中的属性失败，查找不到该属性。");
									}
									//属性到目标对象的属性设置
									if(targetField!=null){
										Method targetMethod = targetClass.getMethod(getSetterMethod(propertyName), targetField.getType());
										
										//不同类型的转换，支持字符串到封装类，和日期字符串的转换
										if(((Object[])fieldValue)[i].getClass().isAssignableFrom(targetField.getType())
												||(classMap.containsKey(targetField.getType())
														&&((Object[])fieldValue)[i].getClass().isAssignableFrom(classMap.get(targetField.getType())))){
											
											if(((Object[])fieldValue)[i].toString().contains(REGEX)){
												targetMethod.invoke(list.get(i), ((Object[])fieldValue)[i].toString().replace(REGEX, ""));
											}else{
												targetMethod.invoke(list.get(i), ((Object[])fieldValue)[i]);
											}
											
										}else{
											Object value = null;
											if(((Object[])fieldValue)[i].toString().contains(REGEX)){
												value = methodMap.get(targetField.getType()).invoke(null, ((Object[])fieldValue)[i].toString().replace(REGEX, "0"));
											}else{
												if(StringUtils.isBlank(((Object[])fieldValue)[i].toString())){
													value = methodMap.get(targetField.getType()).invoke(null, "0");
												}else{
													value = methodMap.get(targetField.getType()).invoke(null, ((Object[])fieldValue)[i]);
												}
												
											}
											targetMethod.invoke(list.get(i), value);
										}
									}
								}
							}
						}
						
						
						
					}

				}
			}
			return list;
		} catch (InstantiationException e) {
			throw new InstantiationException("转换目标类中缺失默认的构造函数");
		} catch (NoSuchMethodException e) {
			throw new NoSuchMethodException("查询反射方法错误");
		} catch (IllegalAccessException e) {
			throw new IllegalAccessException("反射方法执行错误");
		}
	}
	
	/**
	 * 对象中的特殊属性（指定前缀）和指定类 进行映射处理
	 * @param targetClass 转型目标类
	 * @param sourceObj 转换源对象
	 * @param baseBeanMap 设置子表拆分规则的bean ，每个bean对应一个需要切分的属性
	 * @return
	 * @throws Exception
	 */
	public  <T,E>List<T> transfer2Object(Class<T> targetClass,E sourceObj) throws Exception {
		String prefix = targetClass.getName().substring(targetClass.getName().lastIndexOf(".")+1);
		prefix = prefix.substring(0, 1).toLowerCase()+prefix.substring(1)+"_";
		return transfer2Object(targetClass, prefix, sourceObj,null);
	}
	public  <T,E>List<T> transfer2Object(Class<T> targetClass,String prefix,E sourceObj) throws Exception {
		return transfer2Object(targetClass, prefix, sourceObj,null);
	}
	public  <T,E>List<T> transfer2Object(Class<T> targetClass,E sourceObj,String[] validataProperNames) throws Exception {
		String prefix = targetClass.getName().substring(targetClass.getName().lastIndexOf(".")+1);
		prefix = prefix.substring(0, 1).toLowerCase()+prefix.substring(1)+"_";
		return transfer2Object(targetClass, prefix, sourceObj,validataProperNames);
	}
	/**
	 * 拼接getter和setter方法
	 * @param field
	 * @return
	 */
	private  String getSetterMethod(Field field){
		String methodname = "set"+field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1);
		return methodname;
	}
	private  String getSetterMethod(String propertyName){
		String methodname = "set"+propertyName.substring(0, 1).toUpperCase()+propertyName.substring(1);
		return methodname;
	}
	private  String getGetterMethod(Field field){
		String methodname = "get"+field.getName().substring(0, 1).toUpperCase()+field.getName().substring(1);
		return methodname;
	}
	@SuppressWarnings("unused")
	private  String getGetterMethod(String propertyName){
		String methodname = "get"+propertyName.substring(0, 1).toUpperCase()+propertyName.substring(1);
		return methodname;
	}
	
	public Map<String, BaseInputBean> getBaseInputBeanMap() {
		return baseInputBeanMap;
	}
	public void setBaseInputBeanMap(Map<String, BaseInputBean> baseInputBeanMap) {
		this.baseInputBeanMap = baseInputBeanMap;
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
//		BaseInput bi = new BaseInput();
//		HotelBean hb = new HotelBean();
//		
//		BaseInputBean bean = new BaseInputBean();
//		bean.setClassz(HotelRoomPriceBean.class);
//		bean.setRegexSuper(";");
//		bean.setRegexChild("#");
//		bean.setPropertyName(new String[]{"id","price"});
//		bean.setChildListName("list");//
//		hb.put("room_price", bean);
//		
//		bean = new BaseInputBean();
//		bean.setClassz(HotelRoomBedBean.class);
//		bean.setRegexSuper(";");
//		bean.setRegexChild("#");
//		bean.setPropertyName(new String[]{"id","bedname"});
//		bean.setChildListName("bedList");
//		hb.put("room_bed_detail", bean);
//	
//		List<HotelRoomBean> roomList = bi.transfer2Object(HotelRoomBean.class,"room_", hb);
//		hb.setRoomList(roomList);
//		
//		System.out.println("----"+hb.getRoomList().get(0).getName()+"----");
	}
}
