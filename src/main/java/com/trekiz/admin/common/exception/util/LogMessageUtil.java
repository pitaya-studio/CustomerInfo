package com.trekiz.admin.common.exception.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.trekiz.admin.common.exception.common.ExceptionConstants;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.order.entity.ProductOrder;
import com.trekiz.admin.modules.sys.utils.UserUtils;


public class LogMessageUtil {
	private final static Log logger = LogFactory.getLog(LogMessageUtil.class);
	private LogMessageUtil(){};
	public static LogMessageUtil getInstance(){
		return new LogMessageUtil();
	}
	
	/**
	 * 默认提示信息的MAP封装
	 * @author police
	 * @param map
	 * @param productType
	 * @param module
	 */
	private void setDefaultParam(Map<String,String> map,Integer productType ,Integer module){
		setDefaultParam(map);
		map.put(LogTempatleUtil.TEMP_PRODUCT_TYPE, ExceptionConstants.productTypeMap.get(productType));
		map.put(LogTempatleUtil.TEMP_MODULE, ExceptionConstants.moduleMap.get(module));
	}
	private void setDefaultParam(Map<String,String> map){
		map.put(LogTempatleUtil.TEMP_DATE_TIME, DateUtils.date2String(new Date(),DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS));
		map.put(LogTempatleUtil.TEMP_OPERATOR, UserUtils.getUser().getName());
		map.put(LogTempatleUtil.TEMP_COMPANY, UserUtils.getUser().getCompany().getCompanyName());
	}
	/**
	 * 根据产品线类型、业务模块返回默认的log提示信息
	 * @author police
	 * @param productType 产品线类型
	 * @param module 业务模块
	 * @return
	 */
	public String getLogMessage(Integer productType ,Integer module){
		Map<String,String> map = new HashMap<String,String>();
		setDefaultParam(map, productType, module);
		LogTempatleUtil temp = new LogTempatleUtil();
		String message = null;
		try {
			message = temp.transferTemplate2Message(map, LogTempatleUtil.TEMP_TEMPLATE_DEFAULT);
		} catch (Exception e) {
			logger.error("日志模板解析错误！");
			e.printStackTrace(LogUtil.getErrorStream(logger));
		}
		return message;
	}
	public String getLogMessage(){
		Map<String,String> map = new HashMap<String,String>();
		setDefaultParam(map);
		LogTempatleUtil temp = new LogTempatleUtil();
		String message = null;
		try {
			message = temp.transferTemplate2Message(map, LogTempatleUtil.TEMP_TEMPLATE);
		} catch (Exception e) {
			logger.error("日志模板解析错误！");
			e.printStackTrace(LogUtil.getErrorStream(logger));
		}
		return message;
	}
	/**
	 * 根据产品线类型、业务模块返回默认的log提示信息
	 * @author police
	 * @param productType 产品线类型
	 * @param module 业务模块
	 * @param objs 业务数据对象
	 * @return
	 */
	public String getLogMessage4yuwei(Integer productType ,Integer module, Object... objs){
		
		Map<String,String> map = new HashMap<String,String>();
		setDefaultParam(map, productType, module);
		
		if(ArrayUtils.isNotEmpty(objs)){
			for(Object obj : objs){
				if(obj instanceof TravelActivity){
					String productName = ((TravelActivity) obj).getAcitivityName();
					map.put(LogTempatleUtil.TEMP_PRODUCT_NAME, productName);
					
				}else if(obj instanceof ActivityAirTicket){
					String productName = ((ActivityAirTicket) obj).getActivityName();
					map.put(LogTempatleUtil.TEMP_PRODUCT_NAME, productName);
					
				}else if(obj instanceof AirticketOrder){
					String orderNo = ((AirticketOrder) obj).getOrderNo();
					map.put(LogTempatleUtil.TEMP_ORDER_NO, orderNo);
					
				}else if(obj instanceof ProductOrder){
					String orderNo = ((ProductOrder) obj).getOrderNum();
					map.put(LogTempatleUtil.TEMP_ORDER_NO, orderNo);
				}
			}
		}
		LogTempatleUtil temp = new LogTempatleUtil();
		String message = null;
		try {
			message = temp.transferTemplate2Message(map, LogTempatleUtil.TEMP_TEMPLATE_YUWEI);
		} catch (Exception e) {
			logger.error("日志模板解析错误！");
			e.printStackTrace(LogUtil.getErrorStream(logger));
		}
		return message;
	}
	
	/**
	 * 根据产品线类型、业务模块返回默认的log提示信息
	 * 游轮库存订单相关操作的库存余位变更日志信息输出
	 * @author police
	 * @param productType 产品线类型
	 * @param module 业务模块
	 * @param optType 操作类型
	 * @param optStockNum 操作数量，增加或者减少的数量。变更的时候需要传入变更的差值，可为负数
	 * @return
	 */
	public String getLogMessage4Stock(Integer productType ,Integer module,Integer optType, Integer optStockNum){
		
		Map<String,String> map = new HashMap<String,String>();
		setDefaultParam(map, productType, module);
		map.put(LogTempatleUtil.TEMP_OPT_TYPE, ExceptionConstants.optTypeMap.get(optType));
		
		
		if(optType==ExceptionConstants.OPT_TYPE_STOCK_KOUJIAN){//库存订单报名扣减余位
			map.put(LogTempatleUtil.TEMP_OPERATE_STOCK_TYPE, "减少");
			map.put(LogTempatleUtil.TEMP_OPERATE_STOCK_NUM, String.valueOf(Math.abs(optStockNum)));
		}else if(optType==ExceptionConstants.OPT_TYPE_STOCK_BIANGENG){//库存订单修改变更余位
			if(optStockNum>0){//高改的时候减少库存余位
				map.put(LogTempatleUtil.TEMP_OPERATE_STOCK_TYPE, "减少");
				map.put(LogTempatleUtil.TEMP_OPERATE_STOCK_NUM, String.valueOf(Math.abs(optStockNum)));
			}else if(optStockNum<0){//低改的时候增加库存余位
				map.put(LogTempatleUtil.TEMP_OPERATE_STOCK_TYPE, "增加");
				map.put(LogTempatleUtil.TEMP_OPERATE_STOCK_NUM, String.valueOf(Math.abs(optStockNum)));
			}else{
				map.put(LogTempatleUtil.TEMP_OPERATE_STOCK_TYPE, "增加");
				map.put(LogTempatleUtil.TEMP_OPERATE_STOCK_NUM, String.valueOf(0));
			}
		}else if(optType==ExceptionConstants.OPT_TYPE_STOCK_GUIHUAN){//库存订单删除归还余位
			map.put(LogTempatleUtil.TEMP_OPERATE_STOCK_TYPE, "增加");
			map.put(LogTempatleUtil.TEMP_OPERATE_STOCK_NUM, String.valueOf(Math.abs(optStockNum)));
		}else{//默认增加0个余位
			map.put(LogTempatleUtil.TEMP_OPERATE_STOCK_TYPE, "增加");
			map.put(LogTempatleUtil.TEMP_OPERATE_STOCK_NUM, String.valueOf(0));
		}
		
		LogTempatleUtil temp = new LogTempatleUtil();
		String message = null;
		try {
			message = temp.transferTemplate2Message(map, LogTempatleUtil.TEMP_TEMPLATE_STOCK_YUWEI);
		} catch (Exception e) {
			logger.error("日志模板解析错误！");
			e.printStackTrace(LogUtil.getErrorStream(logger));
		}
		return message;
	}
	
}
