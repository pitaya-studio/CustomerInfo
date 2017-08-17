package com.trekiz.admin.modules.order.service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.stock.entity.AirticketActivityReserve;
import com.trekiz.admin.modules.stock.repository.AirticketActivityReserveDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class AirticketOrderStockService  extends BaseService {
    
	@Autowired
	private ActivityAirTicketDao airTicketDao;
	
	@Autowired
	private AirticketActivityReserveDao airticketActivityReserveDao;
	
	private final static Log logger = LogFactory.getLog(AirticketOrderStockService.class);
	
	/** 余位不足 */
	private static String  freeLack = "余位不足";
	/** 切位不足 */
	private static String parkLack = "切位不足";
	/** 超过截团日期 */
	private static String overDate = "超过截团日期";
	
	
	public void ifCanCut(ActivityAirTicket activity, AirticketOrder order, Integer stockOpType) throws Exception {
		 
		//团期切位
		AirticketActivityReserve groupReserve = airticketActivityReserveDao.findAgentReserve(activity.getId(), order.getAgentinfoId());
		
		int orderNum = order.getPersonNum();
		
		/** 创建订单 */
		if (Context.StockOpType.CREATE == stockOpType) {
			// 如果需要校验余位，则校验余位是否足够
			if (validateZWFlag(order) || validateQWFlag(order)) {
				validateIsEnough(activity, order, groupReserve, orderNum);
			}
		}
		/** 订单激活 */
		if (Context.StockOpType.INVOKE == stockOpType) {
			// 如果需要校验余位，则校验余位是否足够
			if ((invokeZWFlag(order) || invokeQWFlag(order)) && order.getOrderState() == 99) {
				validateIsEnough(activity, order, groupReserve, orderNum);
			}
			// 校验是否过了出团日期
			validateInvoke(activity, order);
		}
	}
	
	/**
	 * @Description 是否要验证占位余位：创建订单
	 * @author yakun.bai
	 * @Date 2016-2-1
	 */
	private boolean validateZWFlag(AirticketOrder order) {
		if (Integer.parseInt(Context.ORDER_PAYSTATUS_YZW) == order.getOrderState() && isZWOrder(order)) {
			return true;
		}
		return false;
	}
	 
	/**
	 * @Description 是否要验证切位余位：创建订单
	 * @author yakun.bai
	 * @Date 2016-2-1
	 */
	private boolean validateQWFlag(AirticketOrder order) {
		String orderState = order.getOrderState().toString();
		if (!(Context.ORDER_PAYSTATUS_OP.equals(orderState) || Context.ORDER_PAYSTATUS_CW.equals(orderState)) && !isZWOrder(order)) {
			return true;
		}
		return false;
	}
	
	/**
	 * @Description 验证占位订单修改是否要扣减余位
	 * @author yakun.bai
	 * @Date 2016-2-2
	 */
	private boolean modifyZWFlag(AirticketOrder order) {
		String orderState = order.getOrderState().toString();
		if (Context.ORDER_PAYSTATUS_YZW.equals(orderState)
				|| Context.ORDER_PAYSTATUS_YZFDJ.equals(orderState)
				|| Context.ORDER_PAYSTATUS_YZF.equals(orderState)
				&& isZWOrder(order)) {
			return true;
		}
		return false;
	}
	
	/**
	 * @Description 验证切位订单修改是否要扣减余位
	 * @author yakun.bai
	 * @Date 2016-2-2
	 */
	private boolean modifyQWFlag(AirticketOrder order) {
		String orderState = order.getOrderState().toString();
		if (!(Context.ORDER_PAYSTATUS_OP.equals(orderState) || Context.ORDER_PAYSTATUS_CW.equals(orderState)) && !isZWOrder(order)) {
			return true;
		}
		return false;
	}
	
	/**
	 * @Description 验证是否要扣减售出切位或占位
	 * @author yakun.bai
	 * @Date 2016-2-2
	 */
	private boolean modifyPayFlag(AirticketOrder order) {
		String orderState = order.getOrderState().toString();
		if (Context.ORDER_PAYSTATUS_YZFDJ.equals(orderState)
				|| Context.ORDER_PAYSTATUS_YZF.equals(orderState)) {
			return true;
		}
		return false;
	}
	 
	/**
	 * @Description 判断订单是否是占位订单
	 * @author yakun.bai
	 * @Date 2016-2-1
	 */
	private boolean isZWOrder(AirticketOrder order) {
		if (order.getPlaceHolderType() == null 
				|| StringUtils.equals(Context.PLACEHOLDERTYPE_ZW, order.getPlaceHolderType().toString())) {
			return true;
		}
		return false;
	}
	 
	/**
	 * @Description 激活确定判断
	 * @author yakun.bai
	 * @Date 2016-2-1
	 */
	private boolean invokeZWFlag(AirticketOrder order) {
		if (Integer.parseInt(Context.ORDER_PAYSTATUS_YZW) == order.getOrderState() && isZWOrder(order)) {
			return true;
		}
		return false;
	}
	 
	/**
	 * @Description 激活确定判断
	 * @author yakun.bai
	 * @Date 2016-2-1
	 */
	private boolean invokeQWFlag(AirticketOrder order) {
		String orderState = order.getOrderState().toString();
		if (!(Context.ORDER_PAYSTATUS_OP.equals(orderState) || Context.ORDER_PAYSTATUS_CW.equals(orderState)) && !isZWOrder(order)) {
			return true;
		}
		return false;
	}
	
	/**
	 * @Description 判断订单是否过了截团日期：激活
	 * @author yakun.bai
	 * @Date 2016-2-1
	 */
	private void validateInvoke(ActivityAirTicket activity, AirticketOrder order) throws Exception {
		
		/** 如果批发商允许系统自动取消订单，则激活的时候需判断订单是否已过了截团日期 */
		
		//批发商是否允许自动取消订单
		boolean isCancleOrder = false;
		if (1 == UserUtils.getUser().getCompany().getIsCancleOrder()) {
			isCancleOrder = true;
		}
		
		//如果保留天数加上现在时间超过了截团日期则返回失败
		Calendar currentDate = Calendar.getInstance();
		Calendar invokeDate = Calendar.getInstance();
		invokeDate.setTime(order.getActivationDate());
		
		//保留天数
		long remains = 0;
		if (Integer.parseInt(Context.ORDER_PAYSTATUS_YZW) == order.getOccupyType()) {
			remains = Long.valueOf(activity.getRemainDays_deposit() != null ? activity.getRemainDays_advance() : 0) * (1000*3600*24);
		} else if (Integer.parseInt(Context.ORDER_PAYSTATUS_DJWZF) == order.getOccupyType()) {
			remains = Long.valueOf(activity.getRemainDays_advance() != null ? activity.getRemainDays_advance() : 0) * (1000*3600*24);
		}
		
		if (Integer.parseInt(Context.ORDER_PAYSTATUS_WZF) != order.getOccupyType() 
				&& Integer.parseInt(Context.ORDER_PAYSTATUS_CW) != order.getOccupyType()
				&& Integer.parseInt(Context.ORDER_PAYSTATUS_OP) != order.getOccupyType()) {
			if (currentDate.getTimeInMillis() - invokeDate.getTimeInMillis() - remains > 0) {
				//如果批发商不允许自动取消订单或订单为补单产品则不需判断团期截团日期限制
				if (isCancleOrder) {
					throw new Exception(overDate);
				}
			}
		}
	
	}
	 
	/**
	 * 校验财务确认占位订单是否可以收款：如果余位不够则抛出异常
	 * @param activity 产品
	 * @param order 订单
	 * @param groupReserve 切位
	 * @throws Exception 
	 * @author yakun.bai
	 * @Date 2015-12-18
	 */
	private void validateIsEnough(ActivityAirTicket activity, AirticketOrder order, 
			AirticketActivityReserve groupReserve, Integer orderNum) throws Exception {
		 
		// 订单人数
		if (orderNum == null) {
			orderNum = order.getPersonNum();
		}
		// 普通占位判断
		if (order.getPlaceHolderType() == null 
				|| StringUtils.equals(Context.PLACEHOLDERTYPE_ZW, order.getPlaceHolderType().toString())) {
			int num = activity.getFreePosition() - orderNum;
			if (num < 0) {
				throw new Exception(freeLack);
			}
		} 
		// 切位判断
		else if (StringUtils.equals(Context.PLACEHOLDERTYPE_QW, order.getPlaceHolderType().toString())) {
			if (groupReserve == null || (orderNum > groupReserve.getLeftpayReservePosition())) {
				throw new Exception(parkLack);
			}
		}
		validateChildAndSpecial(activity, order);
	}
	
	/**
	 * @Description 校验是否过了最高人数
	 * @author yakun.bai
	 * @Date 2016-8-13
	 */
	private void validateChildAndSpecial(ActivityAirTicket activity, AirticketOrder order) throws Exception {
		Integer maxChildCount = activity.getMaxChildrenCount();
		Integer maxSpecialCount = activity.getMaxPeopleCount();
		Integer childNum = order.getChildNum();
		Integer specialNum = order.getSpecialNum();
		
		int countChildNum = 0;
		int countSpecialNum = 0;
		
		if ((childNum != null && childNum > 0 && maxChildCount != null && maxChildCount > 0)
				|| (specialNum != null && specialNum > 0 && maxSpecialCount != null && maxSpecialCount > 0)) {
			StringBuffer sb = new StringBuffer();
	        sb.append("select SUM(child_num) orderPersonNumChild,sum(special_num) orderPersonNumSpecial from airticket_order where airticket_id =?  and order_state in (3,4,5)and del_flag = 0");
			List<Map <String, Object>> map =  airticketActivityReserveDao.findBySql(sb.toString(), Map.class, activity.getId());
			Map<String, Object>  counts = map.get(0);
			countChildNum = counts.get("orderPersonNumChild") == null ? 0 : new Integer(counts.get("orderPersonNumChild").toString());
			countSpecialNum = counts.get("orderPersonNumSpecial") == null ? 0 : new Integer(counts.get("orderPersonNumSpecial").toString());
		}
		
		if (childNum != null && childNum > 0 && maxChildCount != null && maxChildCount > 0) {
			
			if (maxChildCount - countChildNum < childNum) {
				throw new Exception("儿童人群最高人数为" + maxChildCount + "，剩余" + (maxChildCount - countChildNum) + "个，请重新填写");
			}
		}
		
		if (specialNum != null && specialNum > 0 && maxSpecialCount != null && maxSpecialCount > 0) {
			if (maxSpecialCount - countSpecialNum < specialNum) {
				throw new Exception("特殊人群最高人数为" + maxChildCount + "，剩余" + (maxChildCount - countChildNum) + "个，请重新填写");
			}
		}
	}
	 
	public synchronized void changeGroupFreeNum(ActivityAirTicket activity, AirticketOrder order, Integer orderNum, Integer stockOpType) throws Exception {
			
		//团期切位
		AirticketActivityReserve groupReserve = airticketActivityReserveDao.findAgentReserve(activity.getId(), order.getAgentinfoId());
		
		if (orderNum == null) {
			orderNum = order.getPersonNum();
		}
		
		/** 创建订单 */
		if (Context.StockOpType.CREATE == stockOpType) {
			// 如果需要校验余位，则校验余位是否足够
			if (validateZWFlag(order)) {
				// 校验余位是否足够
				validateIsEnough(activity, order, groupReserve, orderNum);
				// 扣减占位余位
				cutZW(activity, order, orderNum, true, false, stockOpType);
			}
			if (validateQWFlag(order)) {
				// 校验余位是否足够
				validateIsEnough(activity, order, groupReserve, orderNum);
				// 扣减切位余位
				cutQW(activity, order, groupReserve, orderNum, true, false, stockOpType);
			}
		}
		
		/** 修改订单 */
		 if (Context.StockOpType.MODIFY == stockOpType) {
			 // 如果需要校验余位，则校验余位是否足够
			 if (modifyZWFlag(order)) {
				 // 校验余位是否足够
				 validateIsEnough(activity, order, groupReserve, orderNum);
				 // 扣减占位余位
				 cutZW(activity, order, orderNum, true, modifyPayFlag(order), stockOpType);
			 }
			 if (modifyQWFlag(order)) {
				 // 校验余位是否足够
				 validateIsEnough(activity, order, groupReserve, orderNum);
				 // 扣减切位余位
				 cutQW(activity, order, groupReserve, orderNum, true, modifyPayFlag(order), stockOpType);
			 }
		 }
			 
		/** 计调确认 */
		if (Context.StockOpType.JD_CONFIRM == stockOpType) {
			// 校验余位是否足够
			validateIsEnough(activity, order, groupReserve, orderNum);
			// 扣减占位余位
			cutZW(activity, order, orderNum, true, false, stockOpType);
			// 扣减切位余位
			cutQW(activity, order, groupReserve, orderNum, true, false, stockOpType);
		}
			 
		/** 财务确认 */
		if (Context.StockOpType.CW_CONFIRM == stockOpType) {
			// 校验余位是否足够
			validateIsEnough(activity, order, groupReserve, orderNum);
			// 扣减占位余位
			cutZW(activity, order, orderNum, true, true, stockOpType);
			// 扣减切位余位
			cutQW(activity, order, groupReserve, orderNum, true, true, stockOpType);
		}
		/** 订单激活 */
		if (Context.StockOpType.INVOKE == stockOpType) {
			// 如果需要校验余位，则校验余位是否足够
			if (invokeZWFlag(order)) {
				validateIsEnough(activity, order, groupReserve, orderNum);
				// 扣减占位余位
				cutZW(activity, order, orderNum, true, false, stockOpType);
			}
			if (invokeQWFlag(order)) {
				validateIsEnough(activity, order, groupReserve, orderNum);
				// 扣减切位余位
				cutZW(activity, order, orderNum, true, false, stockOpType);
			}
		}
		/** 订单支付 */
		if (Context.StockOpType.PAY == stockOpType) {
			// 如果需要校验余位且是占位订单，则校验余位是否足够
			if (validateZWPayFlag(order)) {
				validateIsEnough(activity, order, groupReserve, orderNum);
				cutZW(activity, order, orderNum, true, true, stockOpType);
			}
			if (validateQWPayFlag(order)) {
				cutQW(activity, order, groupReserve, orderNum, false, true, stockOpType);
			}
		}
	}
	 
	/**
	 * @Description 是否要验证余位：订单支付
	 * @author yakun.bai
	 * @Date 2016-1-28
	 */
	private boolean validateZWPayFlag(AirticketOrder order) {
			 
		if ((StringUtils.equals(Context.ORDER_PAYSTATUS_WZF, order.getOrderState().toString()) 
				|| StringUtils.equals(Context.ORDER_PAYSTATUS_DJWZF, order.getOrderState().toString()))
				&& isZWOrder(order)) {
			return true;
		}
		return false;
	}
	
	/**
	 * @Description 是否要验证余位：订单支付
	 * @author yakun.bai
	 * @Date 2016-1-28
	 */
	private boolean validateQWPayFlag(AirticketOrder order) {
		
		if (!(StringUtils.equals(Context.ORDER_PAYSTATUS_OP, order.getOrderState().toString()) 
				|| StringUtils.equals(Context.ORDER_PAYSTATUS_CW, order.getOrderState().toString()))
				&& !isZWOrder(order)) {
			return true;
		}
		return false;
	}
	 
	/**
	 * @Description 扣减占位订单余位
	 * @param productOrder 订单
	 * @param activityGroup 团期
	 * @param orderNum 需扣减人数或房间数
	 * @param freeFlag 扣减余位标识
	 * @param soldFlag 售出占位标识
	 * @author yakun.bai
	 * @Date 2016-1-29
	 */
	private void cutZW(ActivityAirTicket activity, 
			AirticketOrder order, int orderNum, boolean freeFlag, boolean soldFlag, Integer stockOpType) throws Exception {
		//占位订单：如果订单状态为预占位，则团期余位减少、团期已分配占位增加
		if (!Context.PLACEHOLDERTYPE_QW.equals(order.getPlaceHolderType().toString())) {
			if (freeFlag) {
				//更新团期余位
				activity.setFreePosition(activity.getFreePosition() - orderNum);
				//更新已分配占位
				activity.setNopayReservePosition(activity.getNopayReservePosition() + orderNum);
				getMessage(order, stockOpType, orderNum, activity.getFreePosition());
			}
			if (soldFlag) {
				//更新已售出占位
				activity.setSoldNopayPosition(activity.getSoldNopayPosition() + orderNum);
			}
			if (freeFlag || soldFlag) {
				airTicketDao.save(activity);
			}
		}
	}
		
	/**
	 * @Description 扣减切位订单余位
	 * @param productOrder 订单
	 * @param activityGroup 团期
	 * @param activityGroup 团期切位
	 * @param orderNum 需扣减人数或房间数
	 * @param freeFlag 扣减余位标识
	 * @param soldFlag 售出占位标识
	 * @author yakun.bai
	 * @Date 2016-1-29
	 */
	private void cutQW(ActivityAirTicket activity, AirticketOrder order, 
			AirticketActivityReserve groupReserve, int orderNum, boolean freeFlag, boolean soldFlag, Integer stockOpType) throws Exception {
		//切位订单
		if (Context.PLACEHOLDERTYPE_QW.equals(order.getPlaceHolderType().toString())) {
			if (freeFlag) {
				//更新切位剩余人数
				groupReserve.setLeftpayReservePosition(groupReserve.getLeftpayReservePosition() - orderNum);
				getMessage(order, stockOpType, orderNum, activity.getFreePosition());
			}
			if (soldFlag) {
				//更新切位售出人数
				groupReserve.setSoldPayPosition(groupReserve.getSoldPayPosition() + orderNum);
				//更新售出切位
				activity.setSoldPayPosition(activity.getSoldPayPosition() + orderNum);
			}
			//保存切位
			airticketActivityReserveDao.save(groupReserve);
			airTicketDao.save(activity);
		}
	}
	
	/**
	 * @Description 添加日志
	 * @author yakun.bai
	 * @Date 2016-2-4
	 */
	private void getMessage(AirticketOrder order, Integer stockOpType, Integer orderNum, Integer freeNum) {
		StringBuffer message = new StringBuffer("");
		String zwOrQw = "占位余位";
		if (!isZWOrder(order)) {
			zwOrQw = "切位余位";
		}
		message.append(DateUtils.getDate(DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS))
			.append("  订单编号：" + order.getOrderNo() + " ")
			.append("  团期id：" + order.getActivitygroupId() + " ")
			.append("  产品id：" + order.getAirticketId() + " ")
			.append("  用户：" + UserUtils.getUser().getName())
			.append("在" + OrderCommonUtil.getChineseOrderType("7"))
			.append("模块，进行" + Context.opMap.get(stockOpType) + "时，扣减" + zwOrQw + "：" + orderNum + "个；")
			.append("剩余余位：" + freeNum + "个");
		logger.info(message);
		saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name, message.toString(), Context.log_state_add,
				7, order.getId());
	}
}