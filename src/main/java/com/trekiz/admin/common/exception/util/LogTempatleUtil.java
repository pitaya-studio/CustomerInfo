package com.trekiz.admin.common.exception.util;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class LogTempatleUtil {
	private final static Log logger = LogFactory.getLog(LogTempatleUtil.class);
	//模板常量
	public static String TEMP_TEMPLATE="template";
	public static String TEMP_TEMPLATE_DEFAULT="template_default";
	public static String TEMP_TEMPLATE_YUWEI="template_yuwei";
	public static String TEMP_TEMPLATE_STOCK_YUWEI="template_stock_yuwei";
	
	//默认的日志记录模板常量
	public static String TEMP_DATE_TIME="${dateTime}";//时间
	public static String TEMP_OPERATOR="${operator}";//操作人
	public static String TEMP_COMPANY="${company}";//操作人
	public static String TEMP_PRODUCT_TYPE="${productType}";//产品线类型
	public static String TEMP_MODULE="${module}";//业务模块
	public static String TEMP_OPT_TYPE="${optType}";//操作类型
	
	//不同业务需要的模板常量
	public static String TEMP_PRODUCT_NAME="${productName}";//产品名称
	public static String TEMP_ORDER_NO="${orderNo}";//订单编号
	
	public static String TEMP_OPERATE_STOCK_TYPE="${operateStockType}";//操作类型。增加或减少
	public static String TEMP_OPERATE_STOCK_NUM="${operateStockNum}";//操作库存余位数量
	
	
	/**
	 * 根据传递的参数、自定义模板 生成提示信息 
	 * @author police
	 * @param map 封装模板中需要的数据的MAP
	 * @param template 自定义模板
	 * @return
	 */
	public String transferTemplate2Message(Map<String,String> map,String template) throws Exception{
		String result = "";
		
		if(MapUtils.isNotEmpty(map)){
			result = this.getTemplate(template);
			Set<Entry<String, String>> entrySet = map.entrySet();
			Iterator<Entry<String, String>> it = entrySet.iterator();
			while(it.hasNext()){
				Entry<String, String> entry = it.next();
				if(entry.getKey()==null)continue;
				
				if(result.contains(entry.getKey())){
					if(entry.getValue()==null)
						result=result.replace(entry.getKey(), "");
					else
						result=result.replace(entry.getKey(), entry.getValue());
				}
			}
		}
		
		return result;
	}
	/**
	 * 根据模板类型返回对应的自定义模板 
	 * @author police
	 * @param templateType 自定义模板类型
	 * @return 
	 */
	private String getTemplate(String templateType){
		StringBuffer sb = new StringBuffer();
		if(TEMP_TEMPLATE.equals(templateType)){
			sb.append(TEMP_DATE_TIME);
			sb.append("用户：").append(TEMP_OPERATOR);
			sb.append("("+TEMP_COMPANY+")");
			sb.append("。");
		}else if(TEMP_TEMPLATE_DEFAULT.equals(templateType)){
			sb.append(TEMP_DATE_TIME);
			sb.append("用户：").append(TEMP_OPERATOR);
			sb.append("("+TEMP_COMPANY+")");
			sb.append("。进行");
			sb.append("“");
			sb.append(TEMP_PRODUCT_TYPE);
			sb.append("”");
			sb.append("“");
			sb.append(TEMP_MODULE);
			sb.append("”");
			sb.append("操作!");
		}else if(TEMP_TEMPLATE_YUWEI.equals(templateType)){
			sb.append(TEMP_DATE_TIME);
			sb.append("用户：").append(TEMP_OPERATOR);
			sb.append("对");
			sb.append("“");
			sb.append(TEMP_PRODUCT_TYPE);
			sb.append("”");
			sb.append("“");
			sb.append(TEMP_MODULE);
			sb.append("”的产品：");
			sb.append("“");
			sb.append(TEMP_PRODUCT_NAME);
			sb.append("”生成的订单");
			sb.append("“");
			sb.append(TEMP_ORDER_NO);
			sb.append("”");
			sb.append("进行操作!");
		}else if(TEMP_TEMPLATE_STOCK_YUWEI.equals(templateType)){
			sb.append(TEMP_DATE_TIME);
			sb.append("用户：").append(TEMP_OPERATOR);
			sb.append("("+TEMP_COMPANY+")");
			sb.append("。进行");
			sb.append("“");
			sb.append(TEMP_PRODUCT_TYPE);
			sb.append("”业务的");
			sb.append("“");
			sb.append(TEMP_MODULE);
			sb.append("”模块");
			sb.append(TEMP_OPT_TYPE);
			sb.append("操作!(");
			sb.append(TEMP_OPERATE_STOCK_TYPE);
			sb.append(TEMP_OPERATE_STOCK_NUM);
			sb.append("库存余位)");
		}
		
		return sb.toString();
	}
	
}
