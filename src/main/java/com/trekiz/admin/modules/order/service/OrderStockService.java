package com.trekiz.admin.modules.order.service;

import java.util.Calendar;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Context.StockOpType;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.repository.TravelActivityDao;
import com.trekiz.admin.modules.activity.service.GroupControlBoardService;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.stock.entity.ActivityGroupReserve;
import com.trekiz.admin.modules.stock.repository.ActivityGroupReserveDao;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Service
@Transactional(readOnly = true)
public class OrderStockService  extends BaseService {
    
	@Autowired
	@Qualifier("activityGroupSyncService")
    private IActivityGroupService activityGroupService;
	
	@Autowired
    private ActivityGroupReserveDao activityGroupReserveDao;
	
	@Autowired
	private TravelActivityDao travelActivityDao;

	@Autowired
	private GroupControlBoardService groupControlBoardService;
	
	private final static Log logger = LogFactory.getLog(OrderStockService.class);
	
	/** 请检查输入参数 */
	private static String paramInfo = "请检查输入参数";
	/** 余位不足 */
	private static String  freeLack= "余位不足";
	/** 切位不足 */
	private static String parkLack = "切位不足";
	/** 机票余位数不足 */
	private static String airTicketFreeLack = "机票余位数不足";
	/** 超过出团日期 */
	private static String overDate = "超过出团日期";
	
	/**
	 * 判断团期是否有足够余位：第一步保存订单、最后一步保存订单、订单激活及订单计调确认占位、订单支付、预报名订单转正式订单
	 * 
	 * 普通占位：保存新订单与订单激活与预报名订单转正式订单（订金占位、全款占位不做任何操作，预占位扣减团期余位并增加渠道占位人数）、修改订单不做任何操作；
	 * 			 计调确认占位（扣减团期余位并增加渠道占位人数）；
	 * 			 订单支付（订金占位、全款占位扣减团期余位并增加渠道占位人数，售出占位增加，预占位增加售出占位，ps：二次支付的时候不做任何操作）；
	 * 切位订单：保存新订单与订单激活与预报名转正式订单与计调确认占位与预报名转正式订单（增加团期渠道切位人数，扣减团期切位余位）；
	 * 			 订单支付（增加团期售出切位，ps：二次支付不做任何操作）；
	 * 结算方式：如果订单结算方式时按月结算或担保结算或后付费任何一种，则都需要及时扣减余位；
	 * 预占位：统指真实意义上预占位，包括预占位、资料占位、担保占位、确认单占位
	 * @param productOrder 订单实体
	 * @param stockOpType 判断类型
	 * @throws Exception 
	 */
	void ifCanCut(ProductOrderCommon productOrder, Integer stockOpType) throws Exception {
		 
		if (productOrder == null || productOrder.getProductId() == null) {
			throw new Exception(paramInfo);
		}
		try {			
			//产品团期
			ActivityGroup activityGroup = activityGroupService.findById(productOrder.getProductGroupId());
			//切位产品  
			ActivityGroupReserve groupReserve = activityGroupReserveDao.findByAgentIdAndSrcActivityIdAndActivityGroupId
					(productOrder.getOrderCompany(), productOrder.getProductId(), activityGroup.getId());
			
			//结算方式：即时结算 1；按月结算 2；担保结算 3；后续费 4
			Integer paymentType = productOrder.getPaymentType();
			
			if (Context.PAYMENT_TYPE_JS == paymentType) {
				
				/** 创建订单 */
				if (Context.StockOpType.CREATE == stockOpType) {
					// 如果需要校验余位，则校验余位是否足够
					if (validateZWFlag(productOrder) || validateQWFlag(productOrder)) {
						validateIsEnough(productOrder, activityGroup, groupReserve, null);
					}
					// 校验机票余位是否足够
					validateAirticketIsEnough(productOrder, activityGroup);
				}
				/** 订单激活 */
				if (Context.StockOpType.INVOKE == stockOpType) {
					// 如果需要校验余位，则校验余位是否足够
					if ((invokeZWFlag(productOrder) || invokeQWFlag(productOrder)) && productOrder.getPayStatus() == 99) {
						validateIsEnough(productOrder, activityGroup, groupReserve, null);
					}
					// 校验是否过了出团日期
					validateInvoke(productOrder, activityGroup, paymentType);
				}
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	 
	/**
	 * @Description 判断订单是否是占位订单
	 * @author yakun.bai
	 * @Date 2016-1-28
	 */
	private boolean isZWOrder(ProductOrderCommon productOrder) {
		if (productOrder.getPlaceHolderType() == null 
				|| StringUtils.equals(Context.PLACEHOLDERTYPE_ZW, productOrder.getPlaceHolderType().toString())) {
			return true;
		}
		return false;
	}
	 
	/**
	 * @Description 是否要验证占位余位：创建订单
	 * @author yakun.bai
	 * @Date 2016-1-28
	 */
	private boolean validateZWFlag(ProductOrderCommon productOrder) {
		//结算方式：即时结算 1；按月结算 2；担保结算 3；后续费 4
		Integer paymentType = productOrder.getPaymentType();
		if ((Context.PAY_MODE_BEFOREHAND.equals(productOrder.getPayMode()) 
				|| Context.PAY_MODE_INFO.equals(productOrder.getPayMode()) 
				|| Context.PAY_MODE_GUARANTY.equals(productOrder.getPayMode()) 
				|| Context.PAY_MODE_CODE.equals(productOrder.getPayMode())
				|| Context.PAYMENT_TYPE_JS != paymentType) && isZWOrder(productOrder)) {
			return true;
		}
		return false;
	}
	 
	/**
	 * @Description 是否要验证切位余位：创建订单
	 * @author yakun.bai
	 * @Date 2016-1-28
	 */
	private boolean validateQWFlag(ProductOrderCommon productOrder) {
		String payStatus = productOrder.getPayStatus().toString();
		if (!(Context.ORDER_PAYSTATUS_OP.equals(payStatus) || Context.ORDER_PAYSTATUS_CW.equals(payStatus)) && !isZWOrder(productOrder)) {
			return true;
		}
		return false;
	}
	
	/**
	 * @Description 验证占位订单修改是否要扣减余位
	 * @author yakun.bai
	 * @Date 2016-2-2
	 */
	private boolean modifyZWFlag(ProductOrderCommon productOrder) {
		String payStatus = productOrder.getPayStatus().toString();
		if (Context.ORDER_PAYSTATUS_YZW.equals(payStatus)
				|| Context.ORDER_PAYSTATUS_YZFDJ.equals(payStatus)
				|| Context.ORDER_PAYSTATUS_YZF.equals(payStatus)
				&& isZWOrder(productOrder)) {
			return true;
		}
		return false;
	}
	
	/**
	 * @Description 验证切位订单修改是否要扣减余位
	 * @author yakun.bai
	 * @Date 2016-2-2
	 */
	private boolean modifyQWFlag(ProductOrderCommon productOrder) {
		String payStatus = productOrder.getPayStatus().toString();
		if (!(Context.ORDER_PAYSTATUS_OP.equals(payStatus) || Context.ORDER_PAYSTATUS_CW.equals(payStatus)) && !isZWOrder(productOrder)) {
			return true;
		}
		return false;
	}
	
	/**
	 * @Description 验证是否要扣减售出切位或占位
	 * @author yakun.bai
	 * @Date 2016-2-2
	 */
	private boolean modifyPayFlag(ProductOrderCommon productOrder) {
		String payStatus = productOrder.getPayStatus().toString();
		if (Context.ORDER_PAYSTATUS_YZFDJ.equals(payStatus)
				|| Context.ORDER_PAYSTATUS_YZF.equals(payStatus)) {
			return true;
		}
		return false;
	}
	 
	/**
	 * @Description 激活确定判断
	 * @author yakun.bai
	 * @Date 2016-2-1
	 */
	private boolean invokeZWFlag(ProductOrderCommon productOrder) {
		//结算方式：即时结算 1；按月结算 2；担保结算 3；后续费 4
		Integer paymentType = productOrder.getPaymentType();
		if ((Context.PAY_MODE_BEFOREHAND.equals(productOrder.getPayMode()) 
				|| Context.PAY_MODE_INFO.equals(productOrder.getPayMode()) 
				|| Context.PAY_MODE_GUARANTY.equals(productOrder.getPayMode()) 
				|| Context.PAY_MODE_CODE.equals(productOrder.getPayMode())
				|| Context.PAYMENT_TYPE_JS != paymentType) && isZWOrder(productOrder)) {
			return true;
		}
		return false;
	}
	 
	/**
	 * @Description 激活确定判断
	 * @author yakun.bai
	 * @Date 2016-2-1
	 */
	private boolean invokeQWFlag(ProductOrderCommon productOrder) {
		String payStatus = productOrder.getPayStatus().toString();
		if (!(Context.ORDER_PAYSTATUS_OP.equals(payStatus) || Context.ORDER_PAYSTATUS_CW.equals(payStatus)) && !isZWOrder(productOrder)) {
			return true;
		}
		return false;
	}
	 
	/**
	 * @Description 是否要验证余位：订单支付
	 * @author yakun.bai
	 * @Date 2016-1-28
	 */
	private boolean validateZWPayFlag(ProductOrderCommon productOrder) {
		 
		if ((StringUtils.equals(Context.ORDER_PAYSTATUS_WZF, productOrder.getPayStatus().toString()) 
				|| StringUtils.equals(Context.ORDER_PAYSTATUS_DJWZF, productOrder.getPayStatus().toString()))
				&& isZWOrder(productOrder)) {
			return true;
		}
		return false;
	}
	
	/**
	 * @Description 是否要验证余位：订单支付
	 * @author yakun.bai
	 * @Date 2016-1-28
	 */
	private boolean validateQWPayFlag(ProductOrderCommon productOrder) {
		 
		if (!(StringUtils.equals(Context.ORDER_PAYSTATUS_OP, productOrder.getPayStatus().toString()) 
				|| StringUtils.equals(Context.ORDER_PAYSTATUS_CW, productOrder.getPayStatus().toString()))
				&& !isZWOrder(productOrder)) {
			return true;
		}
		return false;
	}
	 
	/**
	 * @Description 校验扣减余位时余位或切位是否足够
	 * @author yakun.bai
	 * @Date 2016-1-22
	 */
	private void validateIsEnough(ProductOrderCommon productOrder, ActivityGroup activityGroup, ActivityGroupReserve groupReserve, Integer orderNum) throws Exception {
		 
		// 判断团期余位数是否足够，如果余位数不足则报错返回（邮轮扣减的是房间数）
		if (orderNum == null) {
			orderNum = Integer.parseInt(Context.ORDER_STATUS_CRUISE) != productOrder.getOrderStatus() 
					? productOrder.getOrderPersonNum() : productOrder.getRoomNumber() != null ? productOrder.getRoomNumber() : 0;
		}
		// 判断团期余位
		if (isZWOrder(productOrder)) {
			int num = activityGroup.getFreePosition() - orderNum;
			if (num < 0) {
				throw new Exception(freeLack);
			}
		} 
		// 判断团期切位
		else if (StringUtils.equals(Context.PLACEHOLDERTYPE_QW, productOrder.getPlaceHolderType().toString())) {
			if (groupReserve == null || (orderNum > groupReserve.getLeftpayReservePosition())) {
				throw new Exception(parkLack);
			}
		}
		validateChildAndSpecial(productOrder, activityGroup);
	}
	
	/**
	 * @Description 校验是否过了最高人数
	 * @author yakun.bai
	 * @Date 2016-8-13
	 */
	private void validateChildAndSpecial(ProductOrderCommon productOrder, ActivityGroup activityGroup) throws Exception {
		Integer maxChildCount = activityGroup.getMaxChildrenCount();
		Integer maxSpecialCount = activityGroup.getMaxPeopleCount();
		Integer childNum = productOrder.getOrderPersonNumChild();
		Integer specialNum = productOrder.getOrderPersonNumSpecial();
		
		int countChildNum = 0;
		int countSpecialNum = 0;
		
		if ((childNum != null && childNum > 0 && maxChildCount != null && maxChildCount > 0)
				|| (specialNum != null && specialNum > 0 && maxSpecialCount != null && maxSpecialCount > 0)) {
			Map<String, Object>  counts = activityGroupService.countOrderChildAndSpecialNum(activityGroup.getId(),null);
			countChildNum = counts.get("orderPersonNumChild") == null ? 0 : new Integer(counts.get("orderPersonNumChild").toString());
			countSpecialNum = counts.get("orderPersonNumSpecial") == null ? 0 : new Integer(counts.get("orderPersonNumSpecial").toString());
		}
		
		if (childNum != null && childNum > 0 && maxChildCount != null && maxChildCount > 0) {
			
			if (maxChildCount - countChildNum < 0) {
				throw new Exception("儿童人群最高人数为" + maxChildCount + "，剩余" + (maxChildCount - countChildNum) + "个，请重新填写");
			}
		}
		
		if (specialNum != null && specialNum > 0 && maxSpecialCount != null && maxSpecialCount > 0) {
			if (maxSpecialCount - countSpecialNum < 0) {
				throw new Exception("特殊人群最高人数为" + maxSpecialCount + "，剩余" + (maxSpecialCount - countSpecialNum) + "个，请重新填写");
			}
		}
	}
	 
	/**
	 * @Description 校验机票余位是否足够
	 * @author yakun.bai
	 * @Date 2016-1-28
	 */
	private void validateAirticketIsEnough(ProductOrderCommon productOrder, ActivityGroup activityGroup) throws Exception {
		 
		// 判断团期余位数是否足够，如果余位数不足则报错返回（邮轮扣减的是房间数）
		int orderNum = Integer.parseInt(Context.ORDER_STATUS_CRUISE) != productOrder.getOrderStatus() 
				? productOrder.getOrderPersonNum() : productOrder.getRoomNumber() != null ? productOrder.getRoomNumber() : 0;
		TravelActivity travelActivity =  travelActivityDao.findOne(activityGroup.getSrcActivityId().longValue());
		if (null != travelActivity.getActivityAirTicket()) {
			//如果预定人数大于机票余位数，则不能预定
			int airTicketFreePerson = travelActivity.getActivityAirTicket().getFreePosition();
			if (orderNum > airTicketFreePerson) {
				throw new Exception(airTicketFreeLack);
			}
		}
	}
	 
	 
	 
	/**
	 * @Description 判断订单是否过了截团日期：激活
	 * @author yakun.bai
	 * @Date 2016-1-28
	 */
	private void validateInvoke(ProductOrderCommon productOrder, ActivityGroup group, Integer paymentType) throws Exception {

			
		/** 如果批发商允许系统自动取消订单，则激活的时候需判断订单是否已过了出团日期 */
			
		//批发商是否允许自动取消订单
		boolean isCancleOrder = false;
		if (1 == UserUtils.getUser().getCompany().getIsCancleOrder()) {
			isCancleOrder = true;
		}
		
		//如果保留天数加上现在时间超过了出团日期则返回失败
		Calendar currentDate = Calendar.getInstance();
		Calendar groupOpenDate = Calendar.getInstance();
		if (group.getGroupOpenDate() != null) {
			groupOpenDate.setTime(group.getGroupOpenDate());
		} else {
			return;
		}
		
		if (!Context.PAY_MODE_CW.equals(productOrder.getPayMode())) {
			if (currentDate.getTimeInMillis() - groupOpenDate.getTimeInMillis() > 0 && Context.PAYMENT_TYPE_JS == paymentType) {
				//如果批发商不允许自动取消订单或订单为补单产品则不需判断团期出团日期限制
				if (isCancleOrder && 1 != productOrder.getIsAfterSupplement()) {
					throw new Exception(overDate);
				}
			}
		}
		
	}
	 
	
	/**
	 * 团期余位扣减：订单保存、订单激活、签证参团、订单转团、订单支付、计调确认订单、预报名订单转正式订单
	 * 
	 * 普通占位：保存新订单与订单激活与预报名订单转正式订单（订金占位、全款占位不做任何操作，预占位扣减团期余位并增加渠道占位人数）、修改订单不做任何操作；
	 * 			 计调确认占位（扣减团期余位并增加渠道占位人数）；
	 * 			 订单支付（订金占位、全款占位扣减团期余位并增加渠道占位人数，售出占位增加，预占位增加售出占位，ps：二次支付的时候不做任何操作）；
	 * 切位订单：保存新订单与订单激活与预报名转正式订单与计调确认占位与预报名转正式订单（增加团期渠道切位人数，扣减团期切位余位）；
	 * 			 订单支付（增加团期售出切位，ps：二次支付不做任何操作）；
	 * 结算方式：如果订单结算方式时按月结算或担保结算或后付费任何一种，则都需要及时扣减余位；
	 * 预占位：统指真实意义上预占位，包括预占位、资料占位、担保占位、确认单占位
	 * 
	 * @param productOrder 订单实体
	 * @param isCreate 是否是新建订单
	 * @param isPay 是否是订单支付操作
	 * @param isCutSold 是否要更新团期售出占位
	 * @param opeFlag 操作标志目 前用于524需求区分转团之后的模拟支付和订金或全款占位订单支付之后扣减余位操作
	 * @throws Exception 异常
	 */
	public synchronized void changeGroupFreeNum(ProductOrderCommon productOrder, Integer orderNum, Integer stockOpType) throws Exception {
		
		if (productOrder == null || productOrder.getProductId() == null) {
			throw new Exception(paramInfo);
		}
		//产品团期
		ActivityGroup activityGroup = activityGroupService.findById(productOrder.getProductGroupId());
		//切位产品  
		ActivityGroupReserve groupReserve = activityGroupReserveDao.findByAgentIdAndSrcActivityIdAndActivityGroupId
						(productOrder.getOrderCompany(), productOrder.getProductId(), activityGroup.getId());
		
		// 判断团期余位数是否足够，如果余位数不足则报错返回（邮轮扣减的是房间数）
		if (orderNum == null) {
			orderNum = Integer.parseInt(Context.ORDER_STATUS_CRUISE) != productOrder.getOrderStatus() 
					? productOrder.getOrderPersonNum() : productOrder.getRoomNumber() != null ? productOrder.getRoomNumber() : 0;
		}
		
		//结算方式：即时结算 1；按月结算 2；担保结算 3；后续费 4
		Integer paymentType = productOrder.getPaymentType();
		
		if (Context.PAYMENT_TYPE_JS == paymentType) {
			 
			 /** 创建订单 */
			 if (Context.StockOpType.CREATE == stockOpType 
					 || Context.StockOpType.JOIN_GROUP == stockOpType || Context.StockOpType.TRANSFER_GROUP == stockOpType) {
				 // 校验机票余位是否足够
				 validateAirticketIsEnough(productOrder, activityGroup);
				 // 如果需要校验余位，则校验余位是否足够
				 if (validateZWFlag(productOrder)) {
					 // 校验余位是否足够
					 validateIsEnough(productOrder, activityGroup, groupReserve, orderNum);
					 // 扣减占位余位
					 cutZW(productOrder, activityGroup, orderNum, true, false, stockOpType);
				 }
				 if (validateQWFlag(productOrder)) {
					 // 校验余位是否足够
					 validateIsEnough(productOrder, activityGroup, groupReserve, orderNum);
					 // 扣减切位余位
					 cutQW(productOrder, activityGroup, groupReserve, orderNum, true, false, stockOpType);
				 }
			 }
			 
			 /** 修改订单 */
			 if (Context.StockOpType.MODIFY == stockOpType) {
				 // 如果需要校验余位，则校验余位是否足够
				 if (modifyZWFlag(productOrder)) {
					 // 校验余位是否足够
					 validateIsEnough(productOrder, activityGroup, groupReserve, orderNum);
					 // 扣减占位余位
					 cutZW(productOrder, activityGroup, orderNum, true, modifyPayFlag(productOrder), stockOpType);
				 }
				 if (modifyQWFlag(productOrder)) {
					 // 校验余位是否足够
					 validateIsEnough(productOrder, activityGroup, groupReserve, orderNum);
					 // 扣减切位余位
					 cutQW(productOrder, activityGroup, groupReserve, orderNum, true, modifyPayFlag(productOrder), stockOpType);
				 }
			 }
			 
			 /** 计调确认 */
			 if (Context.StockOpType.JD_CONFIRM == stockOpType) {
				 // 校验余位是否足够
				 validateIsEnough(productOrder, activityGroup, groupReserve, orderNum);
				 // 扣减占位余位
				 cutZW(productOrder, activityGroup, orderNum, true, false, stockOpType);
				 // 扣减切位余位
				 cutQW(productOrder, activityGroup, groupReserve, orderNum, true, false, stockOpType);
			 }
			 
			 /** 财务确认 */
			 if (Context.StockOpType.CW_CONFIRM == stockOpType) {
				 // 校验余位是否足够
				 validateIsEnough(productOrder, activityGroup, groupReserve, orderNum);
				 // 扣减占位余位
				 cutZW(productOrder, activityGroup, orderNum, true, true, stockOpType);
				 // 扣减切位余位
				 cutQW(productOrder, activityGroup, groupReserve, orderNum, true, true, stockOpType);
			 }
			 /** 订单激活 */
			 if (Context.StockOpType.INVOKE == stockOpType) {
				 // 如果需要校验余位，则校验余位是否足够
				 if (invokeZWFlag(productOrder)) {
					 validateIsEnough(productOrder, activityGroup, groupReserve, orderNum);
					 // 扣减占位余位
					 cutZW(productOrder, activityGroup, orderNum, true, false, stockOpType);
				 }
				 if (invokeQWFlag(productOrder)) {
					 validateIsEnough(productOrder, activityGroup, groupReserve, orderNum);
					 // 扣减切位余位
					 cutZW(productOrder, activityGroup, orderNum, true, false, stockOpType);
				 }
			 }
			 /** 订单支付 */
			 if (Context.StockOpType.PAY == stockOpType) {
				 // 如果需要校验余位且是占位订单，则校验余位是否足够
				 if (validateZWPayFlag(productOrder)) {
					 validateIsEnough(productOrder, activityGroup, groupReserve, orderNum);
					 cutZW(productOrder, activityGroup, orderNum, true, true, stockOpType);
				 }
				 if (validateQWPayFlag(productOrder)) {
					 cutQW(productOrder, activityGroup, groupReserve, orderNum, false, true, stockOpType);
				 }
			 }
		 } else {
			 /** 修改订单 */
			 if (Context.StockOpType.MODIFY == stockOpType) {
				 // 校验余位是否足够
				 validateIsEnough(productOrder, activityGroup, groupReserve, orderNum);
				 // 扣减占位余位
				 cutZW(productOrder, activityGroup, orderNum, true, modifyPayFlag(productOrder), stockOpType);
				 // 扣减切位余位
				 cutQW(productOrder, activityGroup, groupReserve, orderNum, true, modifyPayFlag(productOrder), stockOpType);
			 }
		 }
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
	private void cutZW(ProductOrderCommon productOrder, 
			ActivityGroup activityGroup, int orderNum, boolean freeFlag, boolean soldFlag, Integer stockOpType) throws Exception {
		synchronized (activityGroup) {
			// 占位订单：如果订单状态为预占位，则团期余位减少、团期已分配占位增加
			if (!Context.PLACEHOLDERTYPE_QW.equals(productOrder.getPlaceHolderType().toString())) {
				if (freeFlag) {
					// 更新团期余位
					activityGroup.setFreePosition(activityGroup.getFreePosition() - orderNum);
					activityGroup.setPlusFreePosition(-orderNum);
					// 更新已分配占位
					activityGroup.setPlusNopayReservePosition(orderNum);
					activityGroup.setNopayReservePosition(activityGroup.getNopayReservePosition() + orderNum);
					// 添加日志
					getMessage(productOrder, stockOpType, orderNum, activityGroup);
				}
				if (soldFlag) {
					// 更新已售出占位
					activityGroup.setSoldNopayPosition(activityGroup.getSoldNopayPosition() + orderNum);
					activityGroup.setPlusSoldNopayPosition(orderNum);
				}
				if (freeFlag || soldFlag) {
					String newVersion = com.trekiz.admin.common.utils.StringUtils.getVersionNumber();
					activityGroupService.updatePositionNumByOptLock(activityGroup, newVersion);
				}
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
	private void cutQW(ProductOrderCommon productOrder, ActivityGroup activityGroup, 
			ActivityGroupReserve groupReserve, int orderNum, boolean freeFlag, boolean soldFlag, Integer stockOpType) throws Exception {
		synchronized (activityGroup) {
			// 切位订单
			if (Context.PLACEHOLDERTYPE_QW.equals(productOrder.getPlaceHolderType().toString())) {
				if (freeFlag) {
					// 更新切位剩余人数
					groupReserve.setLeftpayReservePosition(groupReserve.getLeftpayReservePosition() - orderNum);
					// 添加日志
					getMessage(productOrder, stockOpType, orderNum, activityGroup);
				}
				if (soldFlag) {
					// 更新切位售出人数
					groupReserve.setSoldPayPosition(groupReserve.getSoldPayPosition() + orderNum);
					// 更新售出切位
					activityGroup.setSoldPayPosition(activityGroup.getSoldPayPosition() + orderNum);
					
					String newVersion = com.trekiz.admin.common.utils.StringUtils.getVersionNumber();
					activityGroupService.updatePositionNumByOptLock(activityGroup, newVersion);
				}
				// 保存切位
				activityGroupReserveDao.save(groupReserve);
			}
		}
	}
	
	/**
	 * @Description 添加日志
	 * @author yakun.bai
	 * @Date 2016-2-4
	 */
	private void getMessage(ProductOrderCommon productOrder, Integer stockOpType, Integer orderNum, ActivityGroup activityGroup) {
		StringBuffer message = new StringBuffer("");
		Integer orderType = productOrder.getOrderStatus();
		String zwOrQw = "占位余位";
		if (!isZWOrder(productOrder)) {
			zwOrQw = "切位余位";
		}
		message.append(DateUtils.getDate(DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS))
			.append("  订单编号：" + productOrder.getOrderNum() + " ")
			.append(" 团期id："+ productOrder.getProductGroupId() + " ")
			.append(" 产品id："+ productOrder.getProductId() + " ")
			.append("  用户：" + UserUtils.getUser().getName())
			.append("在" + OrderCommonUtil.getChineseOrderType(orderType.toString()))
			.append("模块，进行" + Context.opMap.get(stockOpType) + "时，扣减" + zwOrQw + "：" + orderNum + "个；")
			.append("剩余余位：" + activityGroup.getFreePosition() + "个")
			.append("总占位为：" + activityGroup.getNopayReservePosition() + "个")
			.append("总切位为：" + activityGroup.getPayReservePosition() + "个")
			.append("售出占位为：" + activityGroup.getSoldNopayPosition() + "个")
			.append("售出切位为：" + activityGroup.getSoldPayPosition() + "个");
		logger.info(message);
		saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name, message.toString(), Context.log_state_add,
				productOrder.getOrderStatus(), productOrder.getId());
		
		// 0524需求 团期余位变化,记录在团控板中
		if (StockOpType.PAY != stockOpType) { // 订单支付包括两种情况 1.未占位订单付款时真实支付,扣减余位 2.转团生成的订单模拟支付,扣减余位,确保占位 这两种情况要区分开来,在别处进行记录
			if (StockOpType.MODIFY == stockOpType) { // 系统订单修改
				groupControlBoardService.insertGroupControlBoard(4, orderNum, "系统订单修改,订单号"+productOrder.getOrderNum()+"人数从"+(productOrder.getOrderPersonNum()-orderNum)+"调整为"+productOrder.getOrderPersonNum()+",扣减余位 " + orderNum + "个", activityGroup.getId(), -1);
			} else if(StockOpType.CREATE == stockOpType){ // 系统报名生成订单--生成订单有两种情况:1订金或全款占位不扣减余位，等到付款时才进行扣减 2其他情况生成订单立即扣减余位 此处记录的为情况2
				groupControlBoardService.insertGroupControlBoard(2, orderNum, "系统报名生成订单 扣减余位 " + orderNum + "个", activityGroup.getId(), -1);
			} else if(StockOpType.JD_CONFIRM == stockOpType){ // 计调确认 暂时归纳到生成订单
				groupControlBoardService.insertGroupControlBoard(2, orderNum, "系统报名生成订单 计调确认 扣减余位 " + orderNum + "个", activityGroup.getId(), -1);
			} else if(StockOpType.CW_CONFIRM == stockOpType){ // 财务确认收款  暂时归纳到生成订单
				groupControlBoardService.insertGroupControlBoard(2, orderNum, "系统报名生成订单 财务确认 扣减余位 " + orderNum + "个", activityGroup.getId(), -1);
			} else { // 此种情况为需求中未提到，系统中可能出现的,归纳为其他
				groupControlBoardService.insertGroupControlBoard(0, orderNum, "系统中进行" + Context.opMap.get(stockOpType) + "时扣减余位 " + orderNum + "个", activityGroup.getId(), -1);
			}
		}
		// 0524需求 团期余位变化,记录在团控板中

	}
}