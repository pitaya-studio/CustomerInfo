package com.trekiz.admin.modules.order.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.trekiz.admin.agentToOffice.T2.entity.Rate;
import com.trekiz.admin.agentToOffice.T2.utils.RateUtils;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.service.ActivityAirTicketServiceImpl;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.finance.service.ReturnDifferenceService;
import com.trekiz.admin.modules.hotel.dao.HotelMoneyAmountDao;
import com.trekiz.admin.modules.hotel.entity.ActivityHotel;
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.hotel.entity.HotelTraveler;
import com.trekiz.admin.modules.hotel.entity.SysDict;
import com.trekiz.admin.modules.hotel.service.ActivityHotelService;
import com.trekiz.admin.modules.hotel.service.HotelMoneyAmountService;
import com.trekiz.admin.modules.hotel.service.HotelOrderService;
import com.trekiz.admin.modules.hotel.service.HotelTravelerService;
import com.trekiz.admin.modules.hotel.service.SysDictService;
import com.trekiz.admin.modules.hotel.service.impl.HotelMoneyAmountServiceImpl;
import com.trekiz.admin.modules.invoice.entity.Orderinvoice;
import com.trekiz.admin.modules.invoice.service.OrderinvoiceService;
import com.trekiz.admin.modules.island.entity.ActivityIsland;
import com.trekiz.admin.modules.island.entity.HotelRebates;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.entity.IslandRebates;
import com.trekiz.admin.modules.island.entity.IslandTraveler;
import com.trekiz.admin.modules.island.service.ActivityIslandService;
import com.trekiz.admin.modules.island.service.IslandMoneyAmountService;
import com.trekiz.admin.modules.island.service.IslandOrderService;
import com.trekiz.admin.modules.island.service.IslandReviewService;
import com.trekiz.admin.modules.island.service.IslandTravelerService;
import com.trekiz.admin.modules.island.service.impl.IslandMoneyAmountServiceImpl;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.PreProductOrderCommon;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.receipt.repository.OrderReceiptDao;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewDetail;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDetailDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.repository.AreaDao;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.MoneyAmountUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.form.SqlConstant;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;

/**
 * 
 *  文件名: OrderCommonUtil.java
 *  功能:订单通用工具类
 *  
 *  修改记录:   
 *  
 *  @author xiaoyang.tao
 *  @DateTime 2014-12-15 下午4:27:44 
 *  @version 1.0
 */
public class OrderCommonUtil {
	private static MoneyAmountService moneyAmountService = SpringContextHolder.getBean(MoneyAmountService.class);
	
	private static IslandMoneyAmountService islandMoneyAmountService = SpringContextHolder.getBean(IslandMoneyAmountServiceImpl.class);
	
	private static HotelMoneyAmountService hotelMoneyAmountService = SpringContextHolder.getBean(HotelMoneyAmountServiceImpl.class);
	
	private static VisaProductsService visaProductsService = SpringContextHolder.getBean(VisaProductsService.class);
	
	private static TravelActivityService travelActivityService = SpringContextHolder.getBean(TravelActivityService.class);
	
	private static ActivityAirTicketServiceImpl activityAirTicketService = SpringContextHolder.getBean(ActivityAirTicketServiceImpl.class);
	
	private static ReviewDetailDao reviewDetailDao = SpringContextHolder.getBean(ReviewDetailDao.class);
	
	private static AreaDao areaDao = SpringContextHolder.getBean(AreaDao.class);
	
	private static ReviewDao reviewDao = SpringContextHolder.getBean(ReviewDao.class);
	
	private static HotelMoneyAmountDao hotelMoneyAmountDao = SpringContextHolder.getBean(HotelMoneyAmountDao.class);
	
	private  static IslandOrderService islandOrderService = SpringContextHolder.getBean(IslandOrderService.class);
	
	private static ReviewService reviewService = SpringContextHolder.getBean(ReviewService.class);
	
	private static HotelOrderService hotelOrderService = SpringContextHolder.getBean(HotelOrderService.class);
	
	private static SysDictService sysDictService = SpringContextHolder.getBean(SysDictService.class);
	
	private static IslandTravelerService islandTravelerService = SpringContextHolder.getBean(IslandTravelerService.class);
	
	private static HotelTravelerService hotelTravelerService = SpringContextHolder.getBean(HotelTravelerService.class);
	
	private static IslandReviewService islandReviewService = SpringContextHolder.getBean(IslandReviewService.class);

	private static CurrencyService currencyService = SpringContextHolder.getBean(CurrencyService.class);
	private static OrderinvoiceService orderInvoiceService = SpringContextHolder.getBean(OrderinvoiceService.class);
	
	private static OrderReceiptDao orderReceiptDao = SpringContextHolder.getBean(OrderReceiptDao.class);
	
	private static IAirticketOrderDao airticketOrderDao = SpringContextHolder.getBean(IAirticketOrderDao.class);
	
	private static VisaOrderDao visaOrderDao = SpringContextHolder.getBean(VisaOrderDao.class);
	
	private static ProductOrderCommonDao productOrderCommonDao = SpringContextHolder.getBean(ProductOrderCommonDao.class);

	private static OrderCommonService orderCommonService = SpringContextHolder.getBean(OrderCommonService.class);

	private static TravelerService travelerService = SpringContextHolder.getBean(TravelerService.class);
	private static IAirTicketOrderService airTicketOrderService = SpringContextHolder.getBean(IAirTicketOrderService.class);
	private static VisaOrderService visaOrderService = SpringContextHolder.getBean(VisaOrderService.class);
	private static CurrencyDao currencyDao = SpringContextHolder.getBean(CurrencyDao.class);
	private static ActivityHotelService hotelService = SpringContextHolder.getBean(ActivityHotelService.class);
	private static ActivityIslandService islandService = SpringContextHolder.getBean(ActivityIslandService.class);
	private static DocInfoService docInfoService = SpringContextHolder.getBean(DocInfoService.class);
	private static ReturnDifferenceService returnDifferenceServiece = SpringContextHolder.getBean(ReturnDifferenceService.class);
	/**
	 * 根据订单占位方式获取订单占位方式字符串
	 * @param payMode
	 * @return
	 */
	public static String getStringPayMode(String payMode) {
		if(StringUtils.isBlank(payMode)){
			return "";
		}
		payMode = payMode.trim();
		switch (payMode) {
	    	case Context.PAY_MODE_EARNEST : 
	    		payMode = "订金占位";
	    		break;
	    	case Context.PAY_MODE_BEFOREHAND : 
	    		payMode = "预占位";
	    		break;
	    	case Context.PAY_MODE_FULL : 
	    		payMode = "全款占位";
	    		break;
	    	case Context.PAY_MODE_INFO : 
	    		payMode = "资料占位";
	    		break;
	    	case Context.PAY_MODE_GUARANTY : 
	    		payMode = "担保占位";
	    	case Context.PAY_MODE_OP : 
	    		payMode = "计调确认占位";
	    		break;
	    	case Context.PAY_MODE_CW : 
	    		payMode = "财务确认占位";
	    		break;
	    	case Context.ORDER_PAYSTATUS_CW_CX : 
	    		payMode = "已撤销占位";
	    		break;
	    	default : 
	    		payMode = "确认单占位";
	    }
		return payMode;
	}
	
	/**
	 * 根据订单类型获取订单中文类型
	 * @param orderType
	 * @return
	 */
	public static String getChineseOrderType(String orderType) {
		switch (orderType) {
	    	case Context.ORDER_STATUS_SINGLE : 
	    		orderType = "单团";
	    		break;
	    	case Context.ORDER_STATUS_LOOSE : 
	    		orderType = "散拼";
	    		break;
	    	case Context.ORDER_STATUS_STUDY : 
	    		orderType = "游学";
	    		break;
	    	case Context.ORDER_STATUS_BIG_CUSTOMER : 
	    		orderType = "大客户";
	    		break;
	    	case Context.ORDER_STATUS_FREE : 
	    		orderType = "自由行";
	    		break;
	    	case Context.ORDER_STATUS_VISA : 
	    		orderType = "签证";
	    		break;
	    	case Context.ORDER_STATUS_AIR_TICKET : 
	    		orderType = "机票";
	    		break;
	    	case Context.ORDER_STATUS_CRUISE : 
	    		orderType = "游轮";
	    		break;
	    	case Context.ORDER_STATUS_HOTEL :
	    		orderType = "酒店";
	    		break;
	    	case Context.ORDER_STATUS_ISLAND :
	    		orderType = "海岛游";
	    		break;
	    	default : 
	    		orderType = "散拼";
	    }
		return orderType;
	}
	
	/**
	 * 根据订单类型获取订单字符串类型
	 * @param orderType
	 * @return
	 */
	public static String getStringOrdeType(String orderType) {
		switch (orderType) {
	    	case Context.ORDER_STATUS_SINGLE : 
	    		orderType = "single";
	    		break;
	    	case Context.ORDER_STATUS_LOOSE : 
	    		orderType = "loose";
	    		break;
	    	case Context.ORDER_STATUS_STUDY : 
	    		orderType = "study";
	    		break;
	    	case Context.ORDER_STATUS_BIG_CUSTOMER : 
	    		orderType = "bigCustomer";
	    		break;
	    	case Context.ORDER_STATUS_FREE : 
	    		orderType = "free";
	    		break;
	    	case Context.ORDER_STATUS_CRUISE : 
	    		orderType = "cruise";
	    		break;
	    	case Context.ORDER_STATUS_HOTEL : 
	    		orderType = "hotel";
	    		break;	    		
	    	case Context.ORDER_STATUS_ISLAND : 
	    		orderType = "island";
	    		break;
	    	default : 
	    		orderType = "loose";
	    }
		return orderType;
	}

	/**
	 * 根据订单类型获取订单字符串类型
	 * @param orderType
	 * @return
	 */
	public static String getStringOrderType(Integer orderType) {
		String orderTypeStr = ""+orderType;
		switch(orderTypeStr) {
			case Context.ORDER_STATUS_SINGLE:     //单团
				return "singleCost";
			case Context.ORDER_STATUS_LOOSE:		//散拼
				return "looseCost";
			case Context.ORDER_STATUS_STUDY:		//游学
				return "studeyCost";
			case Context.ORDER_STATUS_BIG_CUSTOMER:		//大客户
				return "bigCustomerCost";
			case Context.ORDER_STATUS_FREE :		//自由行
				return "freeCost";
			case Context.ORDER_STATUS_VISA:
				return "visaCost";
			case Context.ORDER_STATUS_AIR_TICKET:		//机票
				return "airticketCost";
			case Context.ORDER_STATUS_CRUISE:	//游轮
				return "cruiseCost";
			default:
				return "singleCost";
		}
	}

	/**
     * 参数处理：用request取值、放入条件map、用model返回页面
     * @param paras 参数按逗号分隔
     * @param mapRequest
     * @param model
     * @param request
     */
    public static void handlePara(String paras,  Map<String,String> mapRequest, Model model, HttpServletRequest request) {
    	if (StringUtils.isNotBlank(paras)) {
    		String common = "";
        	for(String para : paras.split(",")) {
        		common = request.getParameter(para);
        		if(common != null) {
        			common = common.trim().replace("'", "");
        			model.addAttribute(para, common);
        			common = common.replace("\\", "\\\\\\\\");
        			mapRequest.put(para, common);
        		} else {
        			mapRequest.put(para, common);
            		model.addAttribute(para, common);
        		}
        	}
    	}
    }
    
    /**
     * 参数处理：用request取值、放入条件map
     * @param paras 参数按逗号分隔
     * @param mapRequest
     * @param request
     */
    public static void handlePara(String paras,  Map<String,String> mapRequest, HttpServletRequest request) {
    	if (StringUtils.isNotBlank(paras)) {
    		String common = "";
        	for(String para : paras.split(",")) {
        		common = request.getParameter(para);
        		if(common != null) {
        			common = common.trim().replace("'", "");
        			common = common.replace("\\", "\\\\\\\\");
        			mapRequest.put(para, common);
        		} else {
        			mapRequest.put(para, common);
        		}
        	}
    	}
    }


    /**
     * 设置订单保留时间 
     * @author yakun.bai
     * @Date 2016-5-19
     */
    public static ProductOrderCommon setOrderRemainDays(ProductOrderCommon productOrder, TravelActivity activity) {
    	Double remainDays = setCommonOrderRemainDays(productOrder, null, activity);
    	productOrder.setRemainDays(remainDays);
    	return productOrder;
    }
    
    /**
     * 设置预报名订单保留时间 
     * @author yakun.bai
     * @Date 2016-5-19
     */
    public static PreProductOrderCommon setPreOrderRemainDays(PreProductOrderCommon productOrder, TravelActivity activity) {
    	Double remainDays = setCommonOrderRemainDays(null, productOrder, activity);
    	productOrder.setRemainDays(remainDays);
    	return productOrder;
    }
    
    /**
     * 获取订单保留时限 
     * @author yakun.bai
     * @Date 2016-5-19
     */
    private static Double setCommonOrderRemainDays(ProductOrderCommon commonProductOrder, 
    		PreProductOrderCommon preProductOrder, TravelActivity activity) {
    	
    	String payMode = null;
    	if (commonProductOrder != null) {
    		payMode = commonProductOrder.getPayMode();
    	} else if (preProductOrder != null) {
    		payMode = preProductOrder.getPayMode();
    	}
    	
    	if("1".equals(payMode)) {
        	// 获取保留时限的天、时、分
        	Double day = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_deposit());
        	Double hour = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_deposit_hour());
        	Double minute = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_deposit_fen());
        	return getRemainDay(day, hour, minute);
        } else if("2".equals(payMode)) {
        	// 获取保留时限的天、时、分
        	Double day = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_advance());
        	Double hour = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_advance_hour());
        	Double minute = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_advance_fen());
        	return getRemainDay(day, hour, minute);
        }  else if("4".equals(payMode)) {
        	// 获取保留时限的天、时、分
        	Double day = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_data());
        	Double hour = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_data_hour());
        	Double minute = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_data_fen());
        	return getRemainDay(day, hour, minute);
        }  else if("5".equals(payMode)) {
        	// 获取保留时限的天、时、分
        	Double day = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_guarantee());
        	Double hour = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_guarantee_hour());
        	Double minute = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_guarantee_fen());
        	return getRemainDay(day, hour, minute);
        }  else if("6".equals(payMode)) {
        	// 获取保留时限的天、时、分
        	Double day = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_express());
        	Double hour = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_express_hour());
        	Double minute = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_express_fen());
        	return getRemainDay(day, hour, minute);
        } else {
        	return null;
        }
    }
    
    /**
     * 获取保留时限
     * @author yakun.bai
     * @Date 2016-5-20
     */
    public static Double getRemainDay(Double day, Double hour, Double minute) {
    	// 获取保留时限的天、时、分
    	Double remainDays_express = day;
    	Double remainDays_express_hour = hour;
    	Double remainDays_express_fen = minute;
    	// 保留时限折算成double类型
    	Double remainDays = 0.00 + remainDays_express + remainDays_express_hour/24 + remainDays_express_fen/(24*60);
    	// 保留五位小数
    	BigDecimal b = new BigDecimal(remainDays); 
    	return b.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    
    
    /**
 	 * 获取推广价（包含渠道费用）
 	 * 零售价：quauq价加上quauq产品费率加上quauq其他费用费率加上渠道产品费率加上渠道其他费用费率
 	 * @author yakun.bai
 	 * @Date 2016-8-30
 	 */
 	public static BigDecimal getRetailPrice(BigDecimal quauqPrice, Rate rate, Long currencyId) {
 		
 		BigDecimal lsQuauq = new BigDecimal(0);
 		
 		if (quauqPrice != null) {
 			// 如果有费率，则按费率值来算费用，如果没有则按默认0.01比例计算
 			if (rate != null) {
 				// 如果quauq费率类型是比例，则相乘，如果是金额则需要转化成对应币种费率（金额默认为人民币）
 				if (rate.getQuauqRateType() == 0) {
 					lsQuauq = quauqPrice.add(quauqPrice.multiply(rate.getQuauqRate()));
 				} else {
 					Currency currency = currencyDao.findById(currencyId);
 					lsQuauq = quauqPrice.add(rate.getQuauqRate().divide(currency.getConvertLowest(), 2));
 				}
 			} else {
 				lsQuauq = quauqPrice.add(quauqPrice.multiply(new BigDecimal(0.01)));
 			}
 		} else {
 			return null;
 		}
 		
 		// 如果有费率，则按费率值来算费用，如果没有则按默认0.01比例计算
 		if (lsQuauq != null) {
 			if (rate != null) {
 				// 如果渠道费率类型是比例，则相乘，如果是金额则需要转化成对应币种费率（金额默认为人民币）
 				if (rate.getAgentRateType() == 0) {
 					return lsQuauq.add(quauqPrice.multiply(rate.getAgentRate()));
 				} else {
 					Currency currency = currencyDao.findById(currencyId);
 					return lsQuauq.add(rate.getAgentRate().divide(currency.getConvertLowest(), 2));
 				}
 			} else {
 				return quauqPrice.add(quauqPrice.multiply(new BigDecimal(0.01)));
 			}
 		}
 		return null;
 	}
 	
    /**
 	 * 获取推广价（包含渠道费用）
 	 * 零售价：quauq价加上quauq产品费率加上quauq其他费用费率加上渠道产品费率加上渠道其他费用费率
 	 * @author yakun.bai
 	 * @Date 2016-8-30
 	 */
 	public static BigDecimal getRetailPrice(BigDecimal price, BigDecimal quauqPrice, Rate rate, Long currencyId) {
 		
 		BigDecimal lsQuauq = new BigDecimal(0);
 		
 		if (quauqPrice != null) {
 			// 如果有费率，则按费率值来算费用，如果没有则按默认0.01比例计算
 			if (rate != null) {
 				// 如果quauq费率类型是比例，则相乘，如果是金额则需要转化成对应币种费率（金额默认为人民币）
 				if (rate.getQuauqRateType() == 0) {
 					lsQuauq = quauqPrice.add(quauqPrice.multiply(rate.getQuauqRate()));
 				} else {
 					Currency currency = currencyDao.findById(currencyId);
 					lsQuauq = quauqPrice.add(rate.getQuauqRate().divide(currency.getConvertLowest(), 2));
 				}
 			} else {
 				lsQuauq = quauqPrice.add(quauqPrice.multiply(new BigDecimal(0.01)));
 			}
 		} else {
 			return null;
 		}
 		
 		// 如果有费率，则按费率值来算费用，如果没有则按默认0.01比例计算
 		if (lsQuauq != null) {
 			if (rate != null) {
 				// 如果渠道费率类型是比例，则相乘，如果是金额则需要转化成对应币种费率（金额默认为人民币）
 				if (rate.getAgentRateType() == 0) {
 					lsQuauq = lsQuauq.add(quauqPrice.multiply(rate.getAgentRate()));
 				} else {
 					Currency currency = currencyDao.findById(currencyId);
 					lsQuauq = lsQuauq.add(rate.getAgentRate().divide(currency.getConvertLowest(), 2));
 				}
 			} else {
 				lsQuauq = quauqPrice.add(quauqPrice.multiply(new BigDecimal(0.01)));
 			}
 		}
 		
 		// 如果零售大于同行价，则零售价设为同行价
 		if (null != price && null != lsQuauq && lsQuauq.compareTo(price) == 1) {
 			return price;
 		} else {
 			if (null != lsQuauq) {
 				return lsQuauq;
 			} else {
 				return null;
 			}
 		}
 	}
 	
 	public static BigDecimal getRetailPrice(Long groupId, Integer activityKind, BigDecimal price, BigDecimal quauqPrice, Long currencyId) {

		try {
			// 获取费率对象
			Rate rate = RateUtils.getRate(groupId, activityKind, UserUtils.getUser().getAgentId());
			// 获取对应推广价
			return getRetailPrice(price, quauqPrice, rate, currencyId);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 
	 * 获取订单收款、切位收款、签证押金收款、签证订单收款未进行收款确认操作的条数 
	 * @author haiming.zhao
	 * @param type 1:订单收款 ,2:切位收款,3:签证押金收款，4:签证订单收款
	 * @return Integer
	 * */
	public static Integer getCountForOrderListDZ(Integer type){
		return orderCommonService.getCountForOrderListDZ(type);
	}
	/**
	 * 查询金额信息
	 * @param serialNum		序列号
	 * @param index         1：币种名称 例如：美元  2：币种符号 例如：$200
	 * @param orderType     订单类型
	 * @author shijun.liu
	 * @return
	 */
	public static String getMoneyBySerialNumAndOrderType(String serialNum, Integer index, Integer orderType){
		String strMoney = "";
		if(serialNum == null || serialNum.trim().length() < 0){
			return "序列号不能为空";
		}
		if(orderType == Context.ORDER_TYPE_ISLAND){
			List<Object[]> moneyAmountList = islandMoneyAmountService.getMoneyAmonut(serialNum);
			if(moneyAmountList != null && moneyAmountList.size() > 0){
				DecimalFormat d= new DecimalFormat(",##0.00");
				for (int i = 0; i < moneyAmountList.size(); i++) {
					Object[] amount = moneyAmountList.get(i);
					String amonutMoney = "0";
					if(amount[3] != null){
						amonutMoney = amount[3].toString();
					}
					BigDecimal money = new BigDecimal(amonutMoney);
					String strAmount = d.format(money.abs());
					String currency = amount[index].toString();
					if(i == 0){
						if(money.compareTo(BigDecimal.ZERO) < 0){
							strMoney = "-" + currency + strAmount;
						}else{
							strMoney = currency + strAmount;
						}
					}else{
						if(money.compareTo(BigDecimal.ZERO) < 0){
							strMoney += "-" + currency + strAmount;
						}else{
							strMoney += "+" + currency + strAmount;
						}
					}
				}
			}else{
				if(index == 1){
					strMoney = "人民币 0";
				}else if(index == -1){
					strMoney = "";
				}else{
					strMoney = "¥ 0";
				}
			}
		}else if(orderType == Context.ORDER_TYPE_HOTEL){
			List<Object[]> moneyAmountList = hotelMoneyAmountService.getMoneyAmonut(serialNum);
			if(moneyAmountList != null && moneyAmountList.size() > 0){
				DecimalFormat d= new DecimalFormat(",##0.00");
				for (int i = 0; i < moneyAmountList.size(); i++) {
					Object[] amount = moneyAmountList.get(i);
					String amonutMoney = "0";
					if(amount[3] != null){
						amonutMoney = amount[3].toString();
					}
					BigDecimal money = new BigDecimal(amonutMoney);
					String strAmount = d.format(money.abs());
					String currency = amount[index].toString();
					if(i == 0){
						if(money.compareTo(BigDecimal.ZERO) < 0){
							strMoney = "-" + currency + strAmount;
						}else{
							strMoney = currency + strAmount;
						}
					}else{
						if(money.compareTo(BigDecimal.ZERO) < 0){
							strMoney += "-" + currency + strAmount;
						}else{
							strMoney += "+" + currency + strAmount;
						}
					}
				}
			}else{
				if(index == 1){
					strMoney = "人民币 0";
				}else if(index == -1){
					strMoney = "";
				}else{
					strMoney = "¥ 0";
				}
			}
		}else{
			strMoney = getMoneyAmountBySerialNum(serialNum,index);
		}
		return strMoney;
	}
	/**
	 * 根据流水号获取多币种金额加号连接
	 * @param serialNum
	 * @param index 1：币种名称 例如：美元  2：币种符号 例如：$
	 * @return
	 */
	public static String getMoneyAmountBySerialNum(String serialNum, Integer index){
		List<Object[]> moneyAmountList = Lists.newArrayList();
		if(null != serialNum && 0 < serialNum.trim().length()){
			 moneyAmountList = moneyAmountService.getMoneyAmonut(serialNum);
		}
		//538需求  如果存在 差额，那么在申请发票中减去 差额
		ProductOrderCommon orderCommon = orderCommonService.getBySerialNum(serialNum);
		MoneyAmount moneyAmount = null;
		if(orderCommon != null &&orderCommon.getDifferenceFlag() == 1 && StringUtils.isNotBlank(orderCommon.getDifferenceMoney())){
			moneyAmount = MoneyAmountUtils.getMoneyAmountByUUID(orderCommon.getDifferenceMoney());
		}
		String strMoney = "";
		if(moneyAmountList != null && moneyAmountList.size() > 0){
			DecimalFormat d= new DecimalFormat(",##0.00");
			for (int i = 0; i < moneyAmountList.size(); i++) {
				Object[] amount = moneyAmountList.get(i);
				String amonutMoney = "0";
				if(amount[3] != null){
					amonutMoney = amount[3].toString();
				}
				BigDecimal money = new BigDecimal(amonutMoney);
				//538需求 如果存在 差额，那么在申请发票中减去 差额
				if(orderCommon != null &&orderCommon.getDifferenceFlag() == 1 && StringUtils.isNotBlank(orderCommon.getDifferenceMoney())){
					if(amount[0].toString().equals(moneyAmount.getCurrencyId().toString())){
						money = money.subtract(moneyAmount.getAmount());
					}
				}
				String strAmount = d.format(money.abs());
				String currency = amount[2].toString();
				if(i == 0){
					if(money.compareTo(BigDecimal.ZERO) < 0){
						strMoney = "-" + currency + strAmount;
					}else{
						strMoney = currency + strAmount;
					}
				}else{
					if(money.compareTo(BigDecimal.ZERO) < 0){
						strMoney += "-" + currency + strAmount;
					}else{
						strMoney += "+" + currency + strAmount;
					}
				}
			}
		}else{
			if(index == 1){
				strMoney = "人民币 0.00";
			}else if(index == -1){
				strMoney = "";
			}
			else{
				strMoney = "-";
			}
		}
		return strMoney;
	}

	/**
	 * 根据流水号获取多币种金额加号连接
	 * @param serialNum List
	 * @param index 1：币种名称 例如：美元  2：币种符号 例如：$
	 * @return
	 */
	public static String getMoneyAmountBySerialNum(List<String> serialNum, Integer index){
		List<Object[]> moneyAmountList = Lists.newArrayList();
		if(CollectionUtils.isNotEmpty(serialNum)){
			 moneyAmountList = moneyAmountService.getMoneyAmonut(serialNum);
		}
		String strMoney = "";
		if(moneyAmountList != null && moneyAmountList.size() > 0){
			DecimalFormat d= new DecimalFormat(",##0.00");
			for (int i = 0; i < moneyAmountList.size(); i++) {
				Object[] amount = moneyAmountList.get(i);
				String amonutMoney = "0";
				if(amount[3] != null){
					amonutMoney = amount[3].toString();
				}
				BigDecimal money = new BigDecimal(amonutMoney);
				String strAmount = d.format(money.abs());
				String currency = amount[index].toString();
				if(i == 0){
					if(money.compareTo(BigDecimal.ZERO) < 0){
						strMoney = "-" + currency + strAmount;
					}else{
						strMoney = currency + strAmount;
					}
				}else{
					if(money.compareTo(BigDecimal.ZERO) < 0){
						strMoney = "-" + currency + strAmount;
					}else{
						strMoney += "+" + currency + strAmount;
					}
				}
			}
		}else{
			if(index == 1){
				strMoney = "人民币 0";
			}else if(index == -1){
				strMoney = "";
			}
			else{
				strMoney = "¥ 0";
			}
		}
		return strMoney;
	}

	/**
	 * 根据流水号获取多币种金额加号连接（海岛游）
	 * @param serialNum
	 * @param index 1：币种名称 例如：美元  2：币种符号 例如：$
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getIslandMoneyAmountBySerialNum(String serialNum, Integer index){
		String selectName = "c.currency_mark";
		if (index == 1) {
			selectName = "c.currency_name";
		} else {
			selectName = "c.currency_mark";
		}
		String sql = "SELECT " + selectName + ", sum(m.amount) from island_money_amount m, currency c "
				+ "where m.currencyId = c.currency_id and m.serialNum = '"
				+ serialNum + "' GROUP BY m.currencyId ORDER BY m.currencyId";
		List<Object[]> results = hotelMoneyAmountDao.getSession().createSQLQuery(sql).list();

		String money = "";

		if (CollectionUtils.isNotEmpty(results)) {
			for (int i = 0; i < results.size(); i++) {
				Object[] amount = results.get(i);
				DecimalFormat d = new DecimalFormat(",##0.00");
				if (i == results.size() - 1) {
					if (null != amount[1]) {
						money += amount[0] + " " + d.format(new BigDecimal(amount[1].toString()));
					}
				} else {
					if (null != amount[1]) {
						money += amount[0] + " " + d.format(new BigDecimal(amount[1].toString())) + " + ";
					}
				}
			}
		} else {
			money = "¥ 0.00";
		}

		return money;
	}
	/**
	 * 根据多个流水号获取多币种金额加号连接（海岛游）
	 * @param serialNum
	 * @param index 1：币种名称 例如：美元  2：币种符号 例如：$
	 * @return
	 */
	public static String getIslandMoneyAmountBySerialNums(List<String> serialNums, Integer index){
		List<Object[]> moneyAmountList = Lists.newArrayList();
		StringBuffer serialNum = new StringBuffer("");
		if(null != serialNums && 0 < serialNums.size()){
			
			if (CollectionUtils.isNotEmpty(serialNums)) {
				for (int i = 0; i < serialNums.size(); i++) {
					if (i != serialNums.size() - 1) {
						serialNum.append("'" + serialNums.get(i) + "',");
					} else {
						serialNum.append("'" + serialNums.get(i) + "'");
					}
				}
			} else {
				return null;
			}
			 moneyAmountList = moneyAmountService.getIslandMoneyAmountBySerialNums(serialNum.toString());
		}
		String strMoney = "";
		if(moneyAmountList != null && moneyAmountList.size() > 0){
			DecimalFormat d= new DecimalFormat(",##0.00");
			for (int i = 0; i < moneyAmountList.size(); i++) {
				Object[] amount = moneyAmountList.get(i);
				String amonutMoney = "0";
				if(amount[3] != null){
					amonutMoney = amount[3].toString();
				}
				BigDecimal money = new BigDecimal(amonutMoney);
				String strAmount = d.format(money.abs());
				String currency = amount[index].toString();
				if(i == 0){
					if(money.compareTo(BigDecimal.ZERO) < 0){
						strMoney = "-" + currency + strAmount;
					}else{
						strMoney = currency + strAmount;
					}
				}else{
					if(money.compareTo(BigDecimal.ZERO) < 0){
						strMoney = "-" + currency + strAmount;
					}else{
						strMoney += "+" + currency + strAmount;
					}
				}
			}
		}else{
			if(index == 1){
				strMoney = "人民币 0.00";
			}else if(index == -1){
				strMoney = "";
			}
			else{
				strMoney = "¥ 0.00";
			}
		}
		return strMoney;
	}
	
	/**
	 * 根据流水号获取多币种金额, 导出Excel使用
	 * @param orderType 产品类型
	 * @param serialNum 
	 * @param index  1：币种名称 例如：美元  2：币种符号 例如：$
	 * @author xianglei.dong
	 */
	@SuppressWarnings("unchecked")
	public static List<Object[]> getMoneyAmountForBG(Integer orderType,String serialNum,Integer index){
		String selectName = "c.currency_mark";
		String tableName = "money_amount";
		if (index == 1) {
			selectName = "c.currency_name";
		} else {
			selectName = "c.currency_mark";
		}
		if(orderType != null && orderType == Context.ORDER_TYPE_HOTEL){
			tableName="hotel_money_amount";
		}
		if(orderType!= null && orderType == Context.ORDER_TYPE_ISLAND){
			tableName="island_money_amount";
		}
		String sql = "SELECT " + selectName + ", sum(m.amount) from " +tableName+" m, currency c "
				+ "where m.currencyId = c.currency_id and m.serialNum = '"
				+ serialNum + "' GROUP BY m.currencyId ORDER BY m.currencyId";
		List<Object[]> results = hotelMoneyAmountDao.getSession().createSQLQuery(sql).list();
        
		return results;
	}
	
	
	 /**
	 * 根据流水号获取多币种金额加号连接
	 * @param orderType 产品类型
	 * @param serialNum 
	 * @param index  1：币种名称 例如：美元  2：币种符号 例如：$
	 * @author haiming.zhao
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public static String getMoneyAmountForDZ(Integer orderType,String serialNum,Integer index){
		String selectName = "c.currency_mark";
		String tableName = "money_amount";
		if (index == 1) {
			selectName = "c.currency_name";
		} else {
			selectName = "c.currency_mark";
		}
		if(orderType != null && orderType == Context.ORDER_TYPE_HOTEL){
			tableName="hotel_money_amount";
		}
		if(orderType!= null && orderType == Context.ORDER_TYPE_ISLAND){
			tableName="island_money_amount";
		}
			//用来判断是否是收款的
			List<Object[]> list = new ArrayList<>();
			if(orderType ==2){
				StringBuffer sbf = new StringBuffer();
				sbf.append("SELECT * FROM orderpay where moneySerialNum ='"+serialNum+"'");
				list = hotelMoneyAmountDao.getSession().createSQLQuery(sbf.toString()).list();
			}
			String sql = "SELECT " + selectName + ", sum(" ;
			sql+="m.amount) ";
			//收款
			if( orderType==2 && list.size() != 0){
				sql+="+sum(case WHEN rd.return_price is null THEN 0.00 ELSE rd.return_price END)";
			}
			sql+="from " +tableName+" m, currency c ";
			//收款
			if(orderType==2 && list.size() != 0){
				sql+=",orderpay op LEFT JOIN return_difference rd ON op.differenceUuid = rd.uuid ";
			}
			sql+= "where m.currencyId = c.currency_id and m.serialNum = '"+ serialNum+ "' ";
			//收款
			if(orderType==2 && list.size() != 0){
				sql+="and op.moneySerialNum=m.serialNum ";
			}
			sql+="GROUP BY m.currencyId ";
		sql+=" ORDER BY m.currencyId";
		List<Object[]> results = hotelMoneyAmountDao.getSession().createSQLQuery(sql).list();
		String money = "";
		if (CollectionUtils.isNotEmpty(results)) {
			for (int i = 0; i < results.size(); i++) {
				Object[] amount = results.get(i);
				DecimalFormat d = new DecimalFormat(",##0.00");
				if (i == results.size() - 1) {
					if (null != amount[1]) {
						money += amount[0] + " " + d.format(new BigDecimal(amount[1].toString()));
					}
				} else {
					if (null != amount[1]) {
						//如果是多币种，比如￥10000.00+$20000.00,在页面每个币种显示一行，各行之间也加号连接，比如下面两行
						//￥10000.00 +
						// $20000.00
						money += amount[0] + " " + d.format(new BigDecimal(amount[1].toString())) + "+ </br>";
					}
				}
			}
		} else {
			money = "";
		}
        
		return money;
	}
	
	/**
	 * 根据流水号获取多币种金额加号连接
	 * @param orderType 产品类型
	 * @param serialNum 
	 * @param index  1：币种名称 例如：美元  2：币种符号 例如：$
	 * @param payType 付款类型
	 * @author haiming.zhao
	 * 
	 * */
	@SuppressWarnings("unchecked")
	public static String getMoneyAmount(Integer orderType,String serialNum,Integer index,String payType,Integer orderId){
		String selectName = "c.currency_mark";
		String tableName = "money_amount";
		String uid = " and m.uid = " + orderId;
		if (index == 1) {
			selectName = "c.currency_name";
		} else {
			selectName = "c.currency_mark";
		}
		if(orderType != null && orderType == Context.ORDER_TYPE_HOTEL){
			tableName="hotel_money_amount";
			uid = "";
		}
		if(orderType!= null && orderType == Context.ORDER_TYPE_ISLAND){
			tableName="island_money_amount";
			uid = "";
		}
		String sql = "SELECT " + selectName + ", sum(m.amount),m.moneyType from " +tableName+" m, currency c "
				+ "where m.currencyId = c.currency_id and m.serialNum = '"
				+ serialNum + "' " + uid
				+ " GROUP BY m.currencyId ORDER BY m.currencyId ";
		List<Object[]> results = hotelMoneyAmountDao.getSession().createSQLQuery(sql).list();
		String money = "";
		int zeroCount = 0;//update by shijun.liu
		if (CollectionUtils.isNotEmpty(results)) {
			for (int i = 0; i < results.size(); i++) {
				Object[] amount = results.get(i);
				DecimalFormat d = new DecimalFormat(",##0.00");
				if (i == results.size() - 1) {
					if (null != amount[1]) {
						//订单收款全部撤销时，订单的累计达账为空，而不是显示0，如果不是全部撤销则正常显示，例如：$300.00+¥0.00+€400.00，即：是0的数据也显示
						if(null != amount[2]) {
							String moneyAmonut = amount[1].toString();
							Integer moneyType = Integer.parseInt(amount[2].toString());
							if(StringUtils.isNotBlank(payType) && payType.equals("accounted")) {
								money += amount[0] + " " + d.format(new BigDecimal(amount[1].toString()));
							}else if(StringUtils.isNotBlank(payType) && payType.equals("account")) {
								if(zeroCount == (results.size()-1)){//表示查询出来的数据除最后一条外，都是0。C362 update by shijun.liu
									if(!moneyAmonut.equals("0.00")) {
										money += amount[0] + " " + d.format(new BigDecimal(amount[1].toString()));
									}else {
										//如果查询出所有的数据都是0则默认显示空 C362 update by shijun.liu
										money = "";
									}
								}else{
									//查询出来的数据有不为0的数据，所以最后一条无论是否0都需要显示C362 update by shijun.liu
									money += amount[0] + " " + d.format(new BigDecimal(amount[1].toString()));
								}
							}else {
								if(moneyType == Context.MONEY_TYPE_DZ && moneyAmonut.equals("0.00")) {
									money = "";
								}else {
									money += amount[0] + " " + d.format(new BigDecimal(amount[1].toString()));
								}
							}
						}
					}
				} else {
					if (null != amount[1]) {
						if("0.00".equals(amount[1].toString())){
							zeroCount++;//update by shijun.liu
						}
						money += amount[0] + " " + d.format(new BigDecimal(amount[1].toString())) + " + ";
					}
				}
			}
		} else {
			money = "";
		}

		return money;
	}
	/**
	 * 根据流水号获取多币种金额加号连接（酒店）
	 * @param serialNum
	 * @param index 1：币种名称 例如：美元  2：币种符号 例如：$
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String getHotelMoneyAmountBySerialNum(String serialNum, Integer index) {
		String selectName = "c.currency_mark";
		if (index == 1) {
			selectName = "c.currency_name";
		} else {
			selectName = "c.currency_mark";
		}
		String sql = "SELECT " + selectName + ", sum(m.amount) from hotel_money_amount m, currency c "
				+ "where m.currencyId = c.currency_id and m.serialNum = '"
				+ serialNum + "' GROUP BY m.currencyId ORDER BY m.currencyId";
		List<Object[]> results = hotelMoneyAmountDao.getSession().createSQLQuery(sql).list();

		String money = "";

		if (CollectionUtils.isNotEmpty(results)) {
			for (int i = 0; i < results.size(); i++) {
				Object[] amount = results.get(i);
				DecimalFormat d = new DecimalFormat(",##0.00");
				if (i == results.size() - 1) {
					if (null != amount[1]) {
						money += amount[0] + " " + d.format(new BigDecimal(amount[1].toString()));
					}
				} else {
					if (null != amount[1]) {
						money += amount[0] + " " + d.format(new BigDecimal(amount[1].toString())) + " + ";
					}
				}
			}
		} else {
			money = "¥ 0.00";
		}

		return money;
	}
	/**
	 * 根据多个流水号获取多币种金额加号连接（海岛游）
	 * @param serialNum
	 * @param index 1：币种名称 例如：美元  2：币种符号 例如：$
	 * @return
	 */
	public static String getHotelMoneyAmountBySerialNums(List<String> serialNums, Integer index){
		List<Object[]> moneyAmountList = Lists.newArrayList();
		StringBuffer serialNum = new StringBuffer("");
		if(null != serialNums && 0 < serialNums.size()){
			
			if (CollectionUtils.isNotEmpty(serialNums)) {
				for (int i = 0; i < serialNums.size(); i++) {
					if (i != serialNums.size() - 1) {
						serialNum.append("'" + serialNums.get(i) + "',");
					} else {
						serialNum.append("'" + serialNums.get(i) + "'");
					}
				}
			} else {
				return null;
			}
			 moneyAmountList = moneyAmountService.getHotelMoneyAmounttBySerialNums(serialNum.toString());
		}
		String strMoney = "";
		if (moneyAmountList != null && moneyAmountList.size() > 0) {
			DecimalFormat d= new DecimalFormat(",##0.00");
			for (int i = 0; i < moneyAmountList.size(); i++) {
				Object[] amount = moneyAmountList.get(i);
				String amonutMoney = "0";
				if (amount[3] != null) {
					amonutMoney = amount[3].toString();
				}
				BigDecimal money = new BigDecimal(amonutMoney);
				String strAmount = d.format(money.abs());
				String currency = amount[index].toString();
				if (i == 0) {
					if (money.compareTo(BigDecimal.ZERO) < 0) {
						strMoney = "-" + currency + strAmount;
					} else {
						strMoney = currency + strAmount;
					}
				} else {
					if (money.compareTo(BigDecimal.ZERO) < 0) {
						strMoney = "-" + currency + strAmount;
					} else {
						strMoney += "+" + currency + strAmount;
					}
				}
			}
		} else {
			if (index == 1) {
				strMoney = "人民币 0.00";
			} else if (index == -1) {
				strMoney = "";
			} else {
				strMoney = "¥ 0.00";
			}
		}
		return strMoney;
	}
	/**
	 * 根据流水号获取多币种金额加号连接
	 * @param serialNum
	 * @param orderType 订单类型
	 * @param index 1：币种名称 例如：美元  2：币种符号 例如：$
	 * @author shijun.liu
	 * @return
	 */
	public static String getMoneyAmountByUUIDOrderType(String serialNum, Integer orderType, Integer moneyType, Integer index){
		List<Object[]> moneyAmountList = moneyAmountService.getMoneyAmonut(serialNum, orderType, moneyType);
		String strMoney = "";
		if(moneyAmountList != null && moneyAmountList.size() > 0){
			DecimalFormat d= new DecimalFormat(",##0.00");
			for (int i = 0; i < moneyAmountList.size(); i++) {
				Object[] amount = moneyAmountList.get(i);
				String amonutMoney = "0";
				if(amount[3] != null){
					amonutMoney = amount[3].toString();
				}
				BigDecimal money = new BigDecimal(amonutMoney);
				String strAmount = d.format(money.abs());
				String currency = amount[index].toString();
				if(i == 0){
					if(money.compareTo(BigDecimal.ZERO) < 0){
						strMoney = "-" + currency + strAmount;
					}else{
						strMoney = currency + strAmount;
					}
				}else{
					if(money.compareTo(BigDecimal.ZERO) < 0){
						strMoney = "-" + currency + strAmount;
					}else{
						strMoney += "+" + currency + strAmount;
					}
				}
			}
		}else{
			if(index == 1){
				strMoney = "人民币  0.00";
			}else{
				strMoney = "¥ 0.00";
			}
		}
		return strMoney;
	}
	
	/**
	 * 合计金额
	 * @author haiming.zhao
	 * @param list 
	 * @param columnName  列名（参数list中map的key值）
	 * */
	public static BigDecimal getSum(List<Map<String, Object>> list,String columnName){//
		BigDecimal sum= new BigDecimal(0);
		int size = (list==null)?0:list.size(); 
		if(size>0){
			for(int i =0;i<size;i++){
				 Object o = list.get(i).get(columnName);
				 if(o != null){
				 if (o instanceof BigDecimal) {
						sum = sum.add((BigDecimal) o);
						
					}
				  else if(o instanceof String ) { 
						sum = sum.add(new BigDecimal((String) o));
					}else{
						
						sum = sum.add(new BigDecimal(String.valueOf(o)));
					}
				 }
			}
			
		}
		return sum;
	}
	
	/**
	 * 获取财务付款功能的已付金额
	 * @param refundId 
	 * @param moneyType 1：成本录入付款；2：退款付款；3：返佣付款；4：借款付款
	 * @return 
	 * */
	public static String getRefundPayMoney(String recordid,String moneyType){
		String money=moneyAmountService.getRefundPaydMoney(recordid,moneyType);
		return money;
		
	}
	/**
	 * 获取应付账款列表的已付金额
	 * @param refundId 
	 * @param moneyType 1：成本录入付款；2：退款付款；3：返佣付款；4：借款付款
	 * @param orderType
	 * @return 
	 * */
	public static String getPayedTotalMoney(String recordid,Integer moneyType,Integer orderType){
		String money=moneyAmountService.getRefundPaydMoney(recordid,moneyType,orderType);
		return money;
		
	}
	/**
	 * 根据recordId获取付款单位名称
	 * @param recordId refund表recordId
	 * @param orderType 产品类型
	 * @param moneyType  1：成本录入付款；2：退款付款；3：返佣付款；4：借款付款
	 * @return String
	 * @author zhaohaiming
	 * **/
	public static String getPayee(String recordId,String orderType,String moneyType){
		String payee = moneyAmountService.getPayee(recordId, orderType, moneyType);
		return payee;
	}
	/**
	 * 获取财务付款功能的已付金额
	 * @param refundId 
	 * @param moneyType 1：成本录入付款；2：退款付款；3：返佣付款；4：借款付款
	 * @return 
	 * by chy 2015年6月30日22:57:25
	 * */
	public static String getRefundPayMoney(String recordid,String moneyType, String orderType){
		String money=moneyAmountService.getRefundPayedMoney2(recordid,moneyType,orderType);
		return money;
	}
	/**
	 * 
	 * @param serialNum
	 * @return
	 */
	public static String getHtmlMoneyAmountBySerialNum(String serialNum){
		if(null != serialNum && (!"".equals(serialNum.trim())) && 0 < serialNum.trim().length()){
			List<Object[]> moneyAmountList = moneyAmountService.getMoneyAmonut(serialNum);
			String strMoney = "";
			String control = "span";
			String plusSignCSS = "tdgreen";
			String minusSignCSS = "tdred fbold";
			String currencyCSS = "f14";
			String amountCSS = "f20";
			if(moneyAmountList != null && moneyAmountList.size() > 0){
				DecimalFormat d= new DecimalFormat(",##0.00");
				for (int i = 0; i < moneyAmountList.size(); i++) {
					Object[] amount = moneyAmountList.get(i);
					BigDecimal money = new BigDecimal(amount[3].toString());
					String strAmount = d.format(money.abs());
					String currency = amount[1].toString();
					if(i == 0){
						if(money.compareTo(BigDecimal.ZERO) < 0){
							strMoney = getControlByStyle(control, minusSignCSS,"-") + 
									getControlByStyle(control, currencyCSS, currency) + 
									getControlByStyle(control,amountCSS, strAmount);
						}else{
							strMoney = getControlByStyle(control, currencyCSS, currency) + 
									getControlByStyle(control,amountCSS, strAmount);
						}
					}else{
						if(money.compareTo(BigDecimal.ZERO) < 0){
							strMoney += getControlByStyle(control, minusSignCSS, "-") + 
									getControlByStyle(control, currencyCSS, currency) + 
									getControlByStyle(control,amountCSS, strAmount);;
						}else{
							strMoney += getControlByStyle(control, plusSignCSS, "+") + 
									getControlByStyle(control, currencyCSS, currency) + 
									getControlByStyle(control,amountCSS, strAmount);
						}
					}
				}
			}else{
				strMoney = getControlByStyle(control, currencyCSS, "人民币") + 
						getControlByStyle(control,amountCSS, "0");;
			}
			return strMoney;
		}else{
			return "";
		}
		
	}
	
	/**
	 * 根据审核uuid获取原返佣金额的字符串
	 * @param serialNum
	 * @return
	 */
	public static String getOldRebatesMoneyAmountBySerialNum(String serialNum){
		String strMoney = "";
		String control = "span";
		String name = "gaijiaCurencyOld";
		if(StringUtils.isBlank(serialNum)){
			return getControlByName(control, name, "人民币", BigDecimal.ZERO) + "<span>" + 0 + "</span>";
		}
		List<Object[]> moneyAmountList = moneyAmountService.getMoneyAmonut(serialNum);
		if(moneyAmountList != null && moneyAmountList.size() > 0){
			DecimalFormat d= new DecimalFormat(",##0.00");
			for (int i = 0; i < moneyAmountList.size(); i++) {
				Object[] amount = moneyAmountList.get(i);
				BigDecimal money = new BigDecimal(amount[3].toString());
				String strAmount = d.format(money.abs());
				String currency = amount[1].toString();
				if(i == 0){
					if(money.compareTo(BigDecimal.ZERO) < 0){
						strMoney = "-" + getControlByName(control, name, currency, money) + "<span>" + strAmount + "</span>";
					}else{
						strMoney = getControlByName(control, name, currency, money) + "<span>" + strAmount + "</span>";
					}
				}else{
					if(money.compareTo(BigDecimal.ZERO) < 0){
						strMoney += "<br/>-" + getControlByName(control, name, currency,money) + "<span>" + strAmount + "</span>";
					}else{
						strMoney += "<br/>" + getControlByName(control, name, currency,money) + "<span>" + strAmount + "</span>";
					}
				}
			}
		}else{
			strMoney = getControlByName(control, name, "人民币", BigDecimal.ZERO) + "<span>" + 0 + "</span>";
		}
		return strMoney;
	}
	/**
	 * 
	 * @param serialNum
	 * @return
	 */
	public static String getIslandOldRebatesMoneyAmountBySerialNum(String serialNum){
		String strMoney = "";
		String control = "span";
		String name = "gaijiaCurencyOld";
		if(StringUtils.isBlank(serialNum)){
			return getControlByName(control, name, "人民币", BigDecimal.ZERO) + "<span>" + 0 + "</span>";
		}
		List<Object[]> moneyAmountList = moneyAmountService.getIslandMoneyAmount(serialNum);
		if(moneyAmountList != null && moneyAmountList.size() > 0){
			DecimalFormat d= new DecimalFormat(",##0.00");
			for (int i = 0; i < moneyAmountList.size(); i++) {
				Object[] amount = moneyAmountList.get(i);
				BigDecimal money = new BigDecimal(amount[3].toString());
				String strAmount = d.format(money.abs());
				String currency = amount[1].toString();
				if(i == 0){
					if(money.compareTo(BigDecimal.ZERO) < 0){
						strMoney = "-" + getControlByName(control, name, currency, money) + "<span>" + strAmount + "</span>";
					}else{
						strMoney = getControlByName(control, name, currency, money) + "<span>" + strAmount + "</span>";
					}
				}else{
					if(money.compareTo(BigDecimal.ZERO) < 0){
						strMoney += "<br/>-" + getControlByName(control, name, currency,money) + "<span>" + strAmount + "</span>";
					}else{
						strMoney += "<br/>" + getControlByName(control, name, currency,money) + "<span>" + strAmount + "</span>";
					}
				}
			}
		}else{
			strMoney = getControlByName(control, name, "人民币", BigDecimal.ZERO) + "<span>" + 0 + "</span>";
		}
		return strMoney;
	}
	/**
	 * 
	 * @param serialNum
	 * @return
	 */
	public static String getHotelOldRebatesMoneyAmountBySerialNum(String serialNum){
		String strMoney = "";
		String control = "span";
		String name = "gaijiaCurencyOld";
		if(StringUtils.isBlank(serialNum)){
			return getControlByName(control, name, "人民币", BigDecimal.ZERO) + "<span>" + 0 + "</span>";
		}
		List<Object[]> moneyAmountList = moneyAmountService.getHotelMoneyAmount(serialNum);
		if(moneyAmountList != null && moneyAmountList.size() > 0){
			DecimalFormat d= new DecimalFormat(",##0.00");
			for (int i = 0; i < moneyAmountList.size(); i++) {
				Object[] amount = moneyAmountList.get(i);
				BigDecimal money = new BigDecimal(amount[3].toString());
				String strAmount = d.format(money.abs());
				String currency = amount[1].toString();
				if(i == 0){
					if(money.compareTo(BigDecimal.ZERO) < 0){
						strMoney = "-" + getControlByName(control, name, currency, money) + "<span>" + strAmount + "</span>";
					}else{
						strMoney = getControlByName(control, name, currency, money) + "<span>" + strAmount + "</span>";
					}
				}else{
					if(money.compareTo(BigDecimal.ZERO) < 0){
						strMoney += "<br/>-" + getControlByName(control, name, currency,money) + "<span>" + strAmount + "</span>";
					}else{
						strMoney += "<br/>" + getControlByName(control, name, currency,money) + "<span>" + strAmount + "</span>";
					}
				}
			}
		}else{
			strMoney = getControlByName(control, name, "人民币", BigDecimal.ZERO) + "<span>" + 0 + "</span>";
		}
		return strMoney;
	}
	private static String getControlByStyle(String control,String cssName,String content){
		return "<"+ control + " class='" + cssName + "'>" + content + "</" + control + ">";
	}
	
	private static String getControlByName(String control, String name, String currencyName,BigDecimal amount){
		return  "<"+ control + " name='" + name + "' data='" + amount + "'>" + currencyName + "</" + control + ">";
	}
	
	/**
	 * 查询产品名称
	 * @param productId     产品ID
	 * @param productType   产品类型
	 * @author shijun.liu
	 * @return
	 */
	public static String getProductName(String productId, String productType){
		String productName = "";
		if(StringUtils.isNotBlank(productType)){
			int type = Integer.parseInt(productType);
			if(type == Context.ORDER_TYPE_QZ){
				VisaProducts product = visaProductsService.findByVisaProductsId(Long.valueOf(productId));
				if(null != product){
					productName = product.getProductName();
				}else{
					productName = productId;
				}
			}else if(type == Context.ORDER_TYPE_JP){
				StringBuffer tempProductName = new StringBuffer();
				ActivityAirTicket activityAirTicket = activityAirTicketService.findById(Long.valueOf(productId));
				if(null != activityAirTicket){
					String airType = activityAirTicket.getAirType();
					String airTypeLabel = "1".equals(airType)?"多段":"2".equals(airType)?"往返":"3".equals(airType)?"单程":"其他";
					tempProductName.append(activityAirTicket.departureCityLabel()).append("-")
					               .append(areaDao.findOne(Long.valueOf(activityAirTicket.getArrivedCity())).getName()).append(":").append(airTypeLabel);
					productName = tempProductName.toString();
				}else{
					productName = productId;
				}
			}else if(type == Context.ORDER_TYPE_ISLAND){//新增海岛游 by chy 2015年12月15日11:10:42
				ActivityIsland island = islandService.getById(Integer.parseInt(productId));
				if(island == null || island.getActivityName() == null){
					productName = "";
				} else {
					productName = island.getActivityName();
				}
			} else if(type == Context.ORDER_TYPE_HOTEL){//新增酒店 by chy 2015年12月15日11:10:42
				ActivityHotel hotel = hotelService.getById(Integer.parseInt(productId));
				if(hotel == null || hotel.getActivityName() == null){
					productName = "";
				} else {
					productName = hotel.getActivityName();
				}
			}else {
				TravelActivity product = travelActivityService.findById(Long.valueOf(productId));
				if(null != product){
					productName = product.getAcitivityName();
				}else{
					productName = productId;
				}
			}
		}
		return productName;
	}
	
	/**
	 * 根据reviewId以及不同的Key查询不同的审核值,并且保证根据reviewId 和  key 确定唯一一条记录
	 * @param reviewId
	 * @param key
	 * @return
	 */
	public static String getReviewValue(String reviewId, String key){
		String value = "";
		List<ReviewDetail> list = reviewDetailDao.findReviewDetailByMykey(Long.valueOf(reviewId), key);
		if(null != list && list.size()>0){
			value = list.get(0).getMyvalue();
		}
		return value;
	}
	
	/**
	 * 根据reviewId查询多币种情况下带币种符号的金额。例如：$200.00,     $200.00 + ¥300.00
	 * 注意：此方法只适用于借款付款 金额的查询
	 * @param reviewId
	 * @param type          0 表示币种符号，1表示币种名称
	 * @author shijun.liu
	 * @return
	 */
	public static String getMoneyByReviewId(String reviewId, String type){
		if(StringUtils.isNotBlank(reviewId)){
			Integer id = Integer.parseInt(reviewId);
			Review review = reviewDao.findOne(Long.parseLong(reviewId));
			if(review == null){
				return null;
			}
			if(review.getProductType() == 11) {//酒店
				List<Map<String, String>> list = moneyAmountService.getMoneyHotelByReviewId(id);
				if("0".equals(type)){
					return list.get(0).get("mark_money");
				}else if("1".equals(type)){
					return list.get(0).get("name_money");
				}
			} else if(review.getProductType() == 12) {//海岛游
				List<Map<String, String>> list = moneyAmountService.getMoneyIslandByReviewId(id);
				if("0".equals(type)){
					return list.get(0).get("mark_money");
				}else if("1".equals(type)){
					return list.get(0).get("name_money");
				}
			} else {//其它
				List<Map<String, String>> list = moneyAmountService.getMoneyByReviewId(id);
				if("0".equals(type)){
					return list.get(0).get("mark_money");
				}else if("1".equals(type)){
					return list.get(0).get("name_money");
				}
			}
		}
		return null;
	}
	
	/**
	 * 统计订单退款字符串（海岛游、酒店专用）
	 * @author gao
	 * @param orderUuid 退款订单UUID
	 * @param orderType: 订单类型
	 * 客户退款类：refundBean
	 * @return
	 */
	public  static String getRefundPayMoneyByOrderType(String orderUuid,String orderType){
		 List<Map<String, String>> list = new ArrayList<Map<String, String>>(); 
		 DecimalFormat d= new DecimalFormat(",##0.00");
		 // 合并相同币种使用的hashMap
		 Map<String,String> refund = new HashMap<String,String>();
		 // 返回退款字符串
		 StringBuffer buffer = new StringBuffer();
		 String orderID = new String();
		if(StringUtils.isNotBlank(orderType) && orderType.equals(Context.ORDER_TYPE_ISLAND.toString())){
			 IslandOrder order = new IslandOrder();
			 order  = islandOrderService.getByUuid(orderUuid);
			 if(order!=null){
				 orderID = order.getId().toString();
			 }
		}else if(StringUtils.isNotBlank(orderType) && orderType.equals(Context.ORDER_TYPE_HOTEL.toString())){
			HotelOrder order = new HotelOrder();
			 order  = hotelOrderService.getByUuid(orderUuid);
			 if(order!=null){
				 orderID = order.getId().toString();
			 }
		}
		
		 if(StringUtils.isNotBlank(orderID)){
			 if(orderType.equals(Context.ORDER_TYPE_ISLAND.toString())){ // 海岛游
				 list = reviewService.findReviewListMap(Context.ORDER_TYPE_ISLAND,Context.REVIEW_FLOWTYPE_REFUND, orderID, true);
			 }else if(orderType.equals(Context.ORDER_TYPE_HOTEL.toString())){ // 酒店
				 list = reviewService.findReviewListMap(Context.ORDER_TYPE_HOTEL,Context.REVIEW_FLOWTYPE_REFUND, orderID, true);
			 }
			 if(list!=null && !list.isEmpty()){
				 Iterator<Map<String, String>> iter = list.iterator();
				 while(iter.hasNext()){
					 Map<String,String> map = iter.next();
					 //String currencyId = map.get("currencyId"); // 货币类型Id
					 String currencyMark = map.get("currencyMark");// 货币标识
					 String refundPrice = map.get("refundPrice"); // 退款金额
					// 对相同币种的款项进行合并
					// 获取指定币种的款项
					String price =  refund.get(currencyMark);
					if(StringUtils.isNotBlank(price) && StringUtils.isNotBlank(refundPrice)){ // 指定币种款项存在，则进行累计，并再次存入该币种中
						BigDecimal rePrice = new BigDecimal(refundPrice);// 新增款项
						BigDecimal getPrice = new BigDecimal(price);  // map中现有的款项
						BigDecimal addPrice =rePrice.add(getPrice); // 合并后的款项
						refund.put(currencyMark, addPrice.toString());
					}else if(StringUtils.isNotBlank(refundPrice)){ // 指定币种款项不存在，则新增币种款项
						BigDecimal getPrice = new BigDecimal(refundPrice); // 新增款项
						refund.put(currencyMark, getPrice.toString());
					}
				 }
			 }
		 }
		 // 对多币种款项进行字符串化
		 if(refund!=null && !refund.isEmpty()){
			 for(String key : refund.keySet()){
				 String value = refund.get(key); // 获取款项
				 BigDecimal backPrice =new BigDecimal(value); 
				 if(backPrice.compareTo(BigDecimal.ZERO)>=0){
					 // 如果只有一种币种，则不需要增加“+”
					 if(buffer.length()==0){
						 buffer.append(key+d.format(backPrice.doubleValue()));
					 }else if(buffer.length()>0){
						 buffer.append("+"+key+d.format(backPrice.doubleValue()));
					 }
					// buffer.append("+"+key+d.format(backPrice.doubleValue()));
				 }else{
					 buffer.append("-"+key+d.format(backPrice.doubleValue()));
				 }
			 }
		 }else{
			 buffer.append("￥0.00");
		 }
		 return buffer.toString();
	}
	
	/**
	 * 统计订单借款字符串（海岛游、酒店专用）
	 * @author gao
	 * @param orderUuid 借款订单UUID
	 * @param orderType: 订单类型
	 * 客户借款类：borrowingBean
	 * @return
	 */
	public static String getBorrowPayMoneyByOrderType(String orderUuid,String orderType){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>(); 
		 DecimalFormat d= new DecimalFormat(",##0.00");
		 // 合并相同币种使用的hashMap
		 Map<String,String> refund = new HashMap<String,String>();
		 // 返回借款字符串
		 StringBuffer buffer = new StringBuffer();
		 String orderID = new String();
		 if(StringUtils.isNotBlank(orderType) && orderType.equals(Context.ORDER_TYPE_ISLAND.toString())){
			 IslandOrder order = new IslandOrder();
			 order  = islandOrderService.getByUuid(orderUuid);
			 if(order!=null){
				 orderID = order.getId().toString();
			 }
		}else if(StringUtils.isNotBlank(orderType) && orderType.equals(Context.ORDER_TYPE_HOTEL.toString())){
			HotelOrder order = new HotelOrder();
			 order  = hotelOrderService.getByUuid(orderUuid);
			 if(order!=null){
				 orderID = order.getId().toString();
			 }
		}
		
		 if(StringUtils.isNotBlank(orderID)){
			 if(orderType.equals(Context.ORDER_TYPE_ISLAND.toString())){ // 海岛游
				 list = reviewService.findReviewListMap(Context.ORDER_TYPE_ISLAND,Context.REVIEW_FLOWTYPE_BORROWMONEY, orderID, true);
			 }else if(orderType.equals(Context.ORDER_TYPE_HOTEL.toString())){ // 酒店
				 list = reviewService.findReviewListMap(Context.ORDER_TYPE_HOTEL,Context.REVIEW_FLOWTYPE_BORROWMONEY, orderID, true);
			 }
			// list = reviewService.findReviewListMap(Context.ORDER_TYPE_ISLAND,Context.REVIEW_FLOWTYPE_BORROWMONEY, orderID, true);
			 if(list!=null && !list.isEmpty()){
				 Iterator<Map<String, String>> iter = list.iterator();
				 while(iter.hasNext()){
					 Map<String,String> map = iter.next();
					 //String currencyId = map.get("currencyId"); // 货币类型Id
					 String currencyMark = map.get("currencyMark");// 货币标识
					 String lendPrice = map.get("lendPrice"); // 借款金额
					// 对相同币种的款项进行合并
					// 获取指定币种的款项
					String price =  refund.get(currencyMark);
					if(StringUtils.isNotBlank(price) && StringUtils.isNotBlank(lendPrice)){ // 指定币种款项存在，则进行累计，并再次存入该币种中
						BigDecimal rePrice = new BigDecimal(lendPrice); // 新增款项
						BigDecimal getPrice = new BigDecimal(price); // map中现有的款项
						BigDecimal addPrice =rePrice.add(getPrice); // 合并后的款项
						refund.put(currencyMark, addPrice.toString());
					}else if(StringUtils.isNotBlank(lendPrice)){ // 指定币种款项不存在，则新增币种款项
						BigDecimal getPrice = new BigDecimal(lendPrice); // 新增款项
						refund.put(currencyMark, getPrice.toString());
					}
				 }
			 }
		 }
		 // 对多币种款项进行字符串化
		 if(refund!=null && !refund.isEmpty()){
			 for(String key : refund.keySet()){
				 String value = refund.get(key); // 获取款项
				 BigDecimal backPrice =new BigDecimal(value); 
				 if(backPrice.compareTo(BigDecimal.ZERO)>=0){
					 // 如果只有一种币种，则不需要增加“+”
					 if(buffer.length()==0){
						 buffer.append(key+d.format(backPrice.doubleValue()));
					 }else if(buffer.length()>0){
						 buffer.append("+"+key+d.format(backPrice.doubleValue()));
					 }
					// buffer.append("+"+key+d.format(backPrice.doubleValue()));
				 }else{
					 buffer.append("-"+key+d.format(backPrice.doubleValue()));
				 }
			 }
		 }else{
			 buffer.append("￥0.00");
		 }
		 return buffer.toString();
	}
	/**
	 * 根据uuid，获取舱位等级
	 * @author gao
	 * @param uuid
	 * @return
	 */
	public static String getLevelName(String uuid){
		SysDict sysDict = new SysDict();
		if(StringUtils.isNotBlank(uuid)){
			sysDict = sysDictService.getByUuId(uuid);
		}
		if(sysDict!=null){
			return sysDict.getDelFlag();
		}
		return null;
	}
	/**
	 * 统计单一客户退款字符串（海岛游、酒店专用）
	 * @author gao
	 * @param orderUuid 退款订单UUID
	 *  * @param travelUuid 游客UUID
	 * @param orderType: 订单类型
	 * 客户退款类：refundBean
	 * @return
	 */
	public  static String getRefundPayMoneyTravelByOrderType(String orderUuid,String travelUuid,String orderType){
		 List<Map<String, String>> list = new ArrayList<Map<String, String>>(); 
		 DecimalFormat d= new DecimalFormat(",##0.00");
		 // 合并相同币种使用的hashMap
		 Map<String,String> refund = new HashMap<String,String>();
		 // 返回退款字符串
		 StringBuffer buffer = new StringBuffer();
		 String orderID = new String(); // 订单ID
		 String travelID = new String(); // 游客ID
		if(StringUtils.isNotBlank(orderType) && orderType.equals(Context.ORDER_TYPE_ISLAND.toString())){
			 IslandOrder order = new IslandOrder();
			 order  = islandOrderService.getByUuid(orderUuid);
			 if(order!=null){
				 orderID = order.getId().toString();
			 }
		}else if(StringUtils.isNotBlank(orderType) && orderType.equals(Context.ORDER_TYPE_HOTEL.toString())){
			HotelOrder order = new HotelOrder();
			 order  = hotelOrderService.getByUuid(orderUuid);
			 if(order!=null){
				 orderID = order.getId().toString();
			 }
			 
		}
		// 根据travelUUid获取游客
		if(StringUtils.isNotBlank(travelUuid) && orderType.equals(Context.ORDER_TYPE_ISLAND.toString())){
			IslandTraveler travel = new IslandTraveler();
			travel = islandTravelerService.getByUuid(travelUuid);
			if(travel!=null){
				travelID = travel.getId().toString();
			}
		}else if(StringUtils.isNotBlank(travelUuid) && orderType.equals(Context.ORDER_TYPE_HOTEL.toString())){
			HotelTraveler  travel = new HotelTraveler();
			travel = hotelTravelerService.getByUuid(travelUuid);
			if(travel!=null){
				travelID = travel.getId().toString();
			}
		}
		
		 if(StringUtils.isNotBlank(orderID) && StringUtils.isNotBlank(travelID) ){
			 if(orderType.equals(Context.ORDER_TYPE_ISLAND.toString())){ // 海岛游
				 
				 list = reviewService.findReviewInfoActive(Context.ORDER_TYPE_ISLAND,Context.REVIEW_FLOWTYPE_REFUND, orderID, Long.valueOf(travelID));
			 }else if(orderType.equals(Context.ORDER_TYPE_HOTEL.toString())){ // 酒店
				 list = reviewService.findReviewInfoActive(Context.ORDER_TYPE_HOTEL,Context.REVIEW_FLOWTYPE_REFUND, orderID, Long.valueOf(travelID));
			 }
			 if(list!=null && !list.isEmpty()){
				 Iterator<Map<String, String>> iter = list.iterator();
				 while(iter.hasNext()){
					 Map<String,String> map = iter.next();
					 String currencyMark = map.get("currencyMark");// 货币标识
					 String refundPrice = map.get("refundPrice"); // 退款金额
					// 对相同币种的款项进行合并
					// 获取指定币种的款项
					String price =  refund.get(currencyMark);
					if(StringUtils.isNotBlank(price) && StringUtils.isNotBlank(refundPrice)){ // 指定币种款项存在，则进行累计，并再次存入该币种中
						BigDecimal rePrice = new BigDecimal(refundPrice);  // 新增款项
						BigDecimal getPrice = new BigDecimal(price);// map中现有的款项
						BigDecimal addPrice =rePrice.add(getPrice); // 合并后的款项
						refund.put(currencyMark, addPrice.toString());
					}else if(StringUtils.isNotBlank(refundPrice)){ // 指定币种款项不存在，则新增币种款项
						BigDecimal getPrice = new BigDecimal(refundPrice); // 新增款项
						refund.put(currencyMark, getPrice.toString());
					}
				 }
			 }
		 }
		 // 对多币种款项进行字符串化
		 if(refund!=null && !refund.isEmpty()){
			 for(String key : refund.keySet()){
				 String value = refund.get(key); // 获取款项
				 BigDecimal backPrice =new BigDecimal(value); 
				 if(backPrice.compareTo(BigDecimal.ZERO)>=0){
					 // 如果只有一种币种，则不需要增加“+”
					 if(buffer.length()==0){
						 buffer.append(key+d.format(backPrice.doubleValue()));
					 }else if(buffer.length()>0){
						 buffer.append("+"+key+d.format(backPrice.doubleValue()));
					 }
				 }else{
					 buffer.append("-"+key+d.format(backPrice.doubleValue()));
				 }
			 }
		 }else{
			 buffer.append("￥0.00");
		 }
		 return buffer.toString();
	}
	/**
	 * 统计单一客户借款字符串（海岛游、酒店专用）
	 * @author gao
	 * @param orderUuid 借款订单UUID
	 * @param orderType: 订单类型
	 * 客户借款类：borrowingBean
	 * @return
	 */
	public static String getBorrowPayMoneyTravelByOrderType(String orderUuid,String travelUuid,String orderType){
		if (StringUtils.isBlank(travelUuid)) {
			return "";
		}
		List<Map<String, String>> list = new ArrayList<Map<String, String>>(); 
		 DecimalFormat d= new DecimalFormat(",##0.00");
		 // 合并相同币种使用的hashMap
		 Map<String,String> refund = new HashMap<String,String>();
		 // 返回借款字符串
		 StringBuffer buffer = new StringBuffer();
		 String orderID = new String();
		 String travelID = new String();
		 if(StringUtils.isNotBlank(orderType) && orderType.equals(Context.ORDER_TYPE_ISLAND.toString())){
			 IslandOrder order = new IslandOrder();
			 order  = islandOrderService.getByUuid(orderUuid);
			 if(order!=null){
				 orderID = order.getId().toString();
			 }
		}else if(StringUtils.isNotBlank(orderType) && orderType.equals(Context.ORDER_TYPE_HOTEL.toString())){
			HotelOrder order = new HotelOrder();
			 order  = hotelOrderService.getByUuid(orderUuid);
			 if(order!=null){
				 orderID = order.getId().toString();
			 }
		}
		// 根据travelUUid获取游客
		if(StringUtils.isNotBlank(travelUuid) && orderType.equals(Context.ORDER_TYPE_ISLAND.toString())){
			IslandTraveler travel = new IslandTraveler();
			travel = islandTravelerService.getByUuid(travelUuid);
			if(travel!=null){
				travelID = travel.getId().toString();
			}
		}else if(StringUtils.isNotBlank(travelUuid) && orderType.equals(Context.ORDER_TYPE_HOTEL.toString())){
			HotelTraveler  travel = new HotelTraveler();
			travel = hotelTravelerService.getByUuid(travelUuid);
			if(travel!=null){
				travelID = travel.getId().toString();
			}
		}
		
		 if(StringUtils.isNotBlank(orderID)){
			 if(orderType.equals(Context.ORDER_TYPE_ISLAND.toString())){ // 海岛游
				 list = reviewService.findReviewInfoActive(Context.ORDER_TYPE_ISLAND,Context.REVIEW_FLOWTYPE_BORROWMONEY, orderID, Long.valueOf(travelID));
			 }else if(orderType.equals(Context.ORDER_TYPE_HOTEL.toString())){ // 酒店
				 list = reviewService.findReviewInfoActive(Context.ORDER_TYPE_HOTEL,Context.REVIEW_FLOWTYPE_BORROWMONEY, orderID, Long.valueOf(travelID));
			 }
			 if(list!=null && !list.isEmpty()){
				 Iterator<Map<String, String>> iter = list.iterator();
				 while(iter.hasNext()){
					 Map<String,String> map = iter.next();
					 //String currencyId = map.get("currencyId"); // 货币类型Id
					 String currencyMark = map.get("currencyMark");// 货币标识
					 String lendPrice = map.get("lendPrice"); // 借款金额
					// 对相同币种的款项进行合并
					// 获取指定币种的款项
					String price =  refund.get(currencyMark);
					if(StringUtils.isNotBlank(price) && StringUtils.isNotBlank(lendPrice)){ // 指定币种款项存在，则进行累计，并再次存入该币种中
						BigDecimal rePrice = new BigDecimal(lendPrice);// 新增款项
						BigDecimal getPrice = new BigDecimal(price);  // map中现有的款项
						BigDecimal addPrice =rePrice.add(getPrice); // 合并后的款项
						refund.put(currencyMark, addPrice.toString());
					}else if(StringUtils.isNotBlank(lendPrice)){ // 指定币种款项不存在，则新增币种款项
						BigDecimal getPrice = new BigDecimal(lendPrice); // 新增款项
						refund.put(currencyMark, getPrice.toString());
					}
				 }
			 }
		 }
		 // 对多币种款项进行字符串化
		 if(refund!=null && !refund.isEmpty()){
			 for(String key : refund.keySet()){
				 String value = refund.get(key); // 获取款项
				 BigDecimal backPrice =new BigDecimal(value); 
				 if(backPrice.compareTo(BigDecimal.ZERO)>=0){
					 // 如果只有一种币种，则不需要增加“+”
					 if(buffer.length()==0){
						 buffer.append(key+d.format(backPrice.doubleValue()));
					 }else if(buffer.length()>0){
						 buffer.append("+"+key+d.format(backPrice.doubleValue()));
					 }
				 }else{
					 buffer.append("-"+key+d.format(backPrice.doubleValue()));
				 }
			 }
		 }else{
			 buffer.append("￥0.00");
		 }
		 return buffer.toString();
	}
	/**
	 * 统计多客户退款字符串（海岛游、酒店专用）
	 * @author gao
	 * @param orderUuid 退款订单UUID
	 *  * @param travelUuidList 游客UUID
	 * @param orderType: 订单类型
	 * 客户退款类：refundBean
	 * @return
	 */
	public  static String getRefundPayMoneyTravelListByOrderType(String orderUuid,List<String> travelUuidList,String orderType){
		 List<Map<String, String>> list = new ArrayList<Map<String, String>>(); 
		 DecimalFormat d= new DecimalFormat(",##0.00");
		 
		 // 返回退款字符串
		 StringBuffer buffer = new StringBuffer();
		 String orderID = new String(); // 订单ID
		 String travelID = new String(); // 游客ID
		if(StringUtils.isNotBlank(orderType) && orderType.equals(Context.ORDER_TYPE_ISLAND.toString())){
			 IslandOrder order = new IslandOrder();
			 order  = islandOrderService.getByUuid(orderUuid);
			 if(order!=null){
				 orderID = order.getId().toString();
			 }
		}else if(StringUtils.isNotBlank(orderType) && orderType.equals(Context.ORDER_TYPE_HOTEL.toString())){
			HotelOrder order = new HotelOrder();
			 order  = hotelOrderService.getByUuid(orderUuid);
			 if(order!=null){
				 orderID = order.getId().toString();
			 }
			 
		}
		// 合并相同币种使用的hashMap
		 Map<String,String> refund = new HashMap<String,String>();
		for(String travelUuid : travelUuidList){
			
			// 根据travelUUid获取游客
			if(StringUtils.isNotBlank(travelUuid) && orderType.equals(Context.ORDER_TYPE_ISLAND.toString())){
				IslandTraveler travel = new IslandTraveler();
				travel = islandTravelerService.getByUuid(travelUuid);
				if(travel!=null){
					travelID = travel.getId().toString();
				}
			}else if(StringUtils.isNotBlank(travelUuid) && orderType.equals(Context.ORDER_TYPE_HOTEL.toString())){
				HotelTraveler  travel = new HotelTraveler();
				travel = hotelTravelerService.getByUuid(travelUuid);
				if(travel!=null){
					travelID = travel.getId().toString();
				}
			}
			
			 if(StringUtils.isNotBlank(orderID) && StringUtils.isNotBlank(travelID) ){
				 if(orderType.equals(Context.ORDER_TYPE_ISLAND.toString())){ // 海岛游
					 
					 list = reviewService.findReviewInfoActive(Context.ORDER_TYPE_ISLAND,Context.REVIEW_FLOWTYPE_REFUND, orderID, Long.valueOf(travelID));
				 }else if(orderType.equals(Context.ORDER_TYPE_HOTEL.toString())){ // 酒店
					 list = reviewService.findReviewInfoActive(Context.ORDER_TYPE_HOTEL,Context.REVIEW_FLOWTYPE_REFUND, orderID, Long.valueOf(travelID));
				 }
				 if(list!=null && !list.isEmpty()){
					 Iterator<Map<String, String>> iter = list.iterator();
					 while(iter.hasNext()){
						 Map<String,String> map = iter.next();
						 String currencyMark = map.get("currencyMark");// 货币标识
						 String refundPrice = map.get("refundPrice"); // 退款金额
						// 对相同币种的款项进行合并
						// 获取指定币种的款项
						String price =  refund.get(currencyMark);
						if(StringUtils.isNotBlank(price) && StringUtils.isNotBlank(refundPrice)){ // 指定币种款项存在，则进行累计，并再次存入该币种中
							BigDecimal rePrice = new BigDecimal(refundPrice); // 新增款项
							BigDecimal getPrice = new BigDecimal(price);// map中现有的款项 
							BigDecimal addPrice =rePrice.add(getPrice); // 合并后的款项
							refund.put(currencyMark, addPrice.toString());
						}else if(StringUtils.isNotBlank(refundPrice)){ // 指定币种款项不存在，则新增币种款项
							BigDecimal getPrice = new BigDecimal(refundPrice); // 新增款项
							refund.put(currencyMark, getPrice.toString());
						}
					 }
				 }
			 }
			 
		}
		// 对多币种款项进行字符串化
		 if(refund!=null && !refund.isEmpty()){
			 for(String key : refund.keySet()){
				 String value = refund.get(key); // 获取款项
				 BigDecimal backPrice =new BigDecimal(value); 
				 if(backPrice.compareTo(BigDecimal.ZERO)>=0){
					 // 如果只有一种币种，则不需要增加“+”
					 if(buffer.length()==0){
						 buffer.append(key+d.format(backPrice.doubleValue()));
					 }else if(buffer.length()>0){
						 buffer.append("+"+key+d.format(backPrice.doubleValue()));
					 }
					// buffer.append("+"+key+d.format(backPrice.doubleValue()));
				 }else{
					 buffer.append("-"+key+d.format(backPrice.doubleValue()));
				 }
			 }
		 }
		if(buffer==null || buffer.length()<1 ){
			buffer.append("￥0.00");
		}
		 return buffer.toString();
	}

	/**
	 * 统计多客户借款字符串（海岛游、酒店专用）
	 * @author gao
	 * @param orderUuid 借款订单UUID
	 * @param orderType: 订单类型
	 * 客户借款类：borrowingBean
	 * @return
	 */
	public static String getBorrowPayMoneyTravelByOrderType(String orderUuid,List<String> travelUuidList,String orderType){
		List<Map<String, String>> list = new ArrayList<Map<String, String>>(); 
		 DecimalFormat d= new DecimalFormat(",##0.00");
		 // 返回借款字符串
		 StringBuffer buffer = new StringBuffer();
		 String orderID = new String();
		 String travelID = new String();
		 if(StringUtils.isNotBlank(orderType) && orderType.equals(Context.ORDER_TYPE_ISLAND.toString())){
			 IslandOrder order = new IslandOrder();
			 order  = islandOrderService.getByUuid(orderUuid);
			 if(order!=null){
				 orderID = order.getId().toString();
			 }
		}else if(StringUtils.isNotBlank(orderType) && orderType.equals(Context.ORDER_TYPE_HOTEL.toString())){
			HotelOrder order = new HotelOrder();
			 order  = hotelOrderService.getByUuid(orderUuid);
			 if(order!=null){
				 orderID = order.getId().toString();
			 }
		}
		// 合并相同币种使用的hashMap
		 Map<String,String> refund = new HashMap<String,String>();
		 for(String travelUuid : travelUuidList){
			
			// 根据travelUUid获取游客
			if(StringUtils.isNotBlank(travelUuid) && orderType.equals(Context.ORDER_TYPE_ISLAND.toString())){
				IslandTraveler travel = new IslandTraveler();
				travel = islandTravelerService.getByUuid(travelUuid);
				if(travel!=null){
					travelID = travel.getId().toString();
				}
			}else if(StringUtils.isNotBlank(travelUuid) && orderType.equals(Context.ORDER_TYPE_HOTEL.toString())){
				HotelTraveler  travel = new HotelTraveler();
				travel = hotelTravelerService.getByUuid(travelUuid);
				if(travel!=null){
					travelID = travel.getId().toString();
				}
			}
			
			 if(StringUtils.isNotBlank(orderID)){
				 if(orderType.equals(Context.ORDER_TYPE_ISLAND.toString())){ // 海岛游
					 list = reviewService.findReviewInfoActive(Context.ORDER_TYPE_ISLAND,Context.REVIEW_FLOWTYPE_BORROWMONEY, orderID, Long.valueOf(travelID));
				 }else if(orderType.equals(Context.ORDER_TYPE_HOTEL.toString())){ // 酒店
					 list = reviewService.findReviewInfoActive(Context.ORDER_TYPE_HOTEL,Context.REVIEW_FLOWTYPE_BORROWMONEY, orderID, Long.valueOf(travelID));
				 }
				 if(list!=null && !list.isEmpty()){
					 Iterator<Map<String, String>> iter = list.iterator();
					 while(iter.hasNext()){
						 Map<String,String> map = iter.next();
						 //String currencyId = map.get("currencyId"); // 货币类型Id
						 String currencyMark = map.get("currencyMark");// 货币标识
						 String lendPrice = map.get("lendPrice"); // 借款金额
						// 对相同币种的款项进行合并
						// 获取指定币种的款项
						String price =  refund.get(currencyMark);
						if(StringUtils.isNotBlank(price) && StringUtils.isNotBlank(lendPrice)){ // 指定币种款项存在，则进行累计，并再次存入该币种中
							BigDecimal rePrice = new BigDecimal(lendPrice);// 新增款项 
							BigDecimal getPrice = new BigDecimal(price); // map中现有的款项
							BigDecimal addPrice =rePrice.add(getPrice); // 合并后的款项
							refund.put(currencyMark, addPrice.toString());
						}else if(StringUtils.isNotBlank(lendPrice)){ // 指定币种款项不存在，则新增币种款项
							BigDecimal getPrice = new BigDecimal(lendPrice); // 新增款项
							refund.put(currencyMark, getPrice.toString());
						}
					 }
				 }
			 }
			 
		 }
		// 对多币种款项进行字符串化
		 if(refund!=null && !refund.isEmpty()){
			 for(String key : refund.keySet()){
				 String value = refund.get(key); // 获取款项
				 BigDecimal backPrice =new BigDecimal(value);
				 if(backPrice.compareTo(BigDecimal.ZERO)>=0){
					 // 如果只有一种币种，则不需要增加“+”
					 if(buffer.length()==0){
						 buffer.append(key+d.format(backPrice.doubleValue()));
					 }else if(buffer.length()>0){
						 buffer.append("+"+key+d.format(backPrice.doubleValue()));
					 }
					// buffer.append("+"+key+d.format(backPrice.doubleValue()));
				 }else{
					 buffer.append("-"+key+d.format(backPrice.doubleValue()));
				 }
			 }
		 }
		if(buffer==null || buffer.length()<1 ){
			buffer.append("￥0.00");
		}
		 return buffer.toString();
	}
	
	/**
	 * 统计单一客户累计返佣总金额（海岛游、酒店专用）
	 * @author ruyi.chen
	 * @param orderUuid 返佣订单UUID
	 * @param orderType: 订单类型
	 * @return
	 */
	public static String getTravelRebatesByOrderType(String travelUuid,String orderType){
		 DecimalFormat d= new DecimalFormat(",##0.00");
		 // 合并相同币种使用的hashMap
		 Map<String,String> refund = new HashMap<String,String>();
		 StringBuffer buffer = new StringBuffer();
		 String travelID = new String();
		// 根据travelUUid获取游客
		if(StringUtils.isNotBlank(travelUuid) && orderType.equals(Context.ORDER_TYPE_ISLAND.toString())){
			IslandTraveler travel = new IslandTraveler();
			travel = islandTravelerService.getByUuid(travelUuid);
			if(travel!=null){
				travelID = travel.getId().toString();
			}
		}else if(StringUtils.isNotBlank(travelUuid) && orderType.equals(Context.ORDER_TYPE_HOTEL.toString())){
			HotelTraveler  travel = new HotelTraveler();
			travel = hotelTravelerService.getByUuid(travelUuid);
			if(travel!=null){
				travelID = travel.getId().toString();
			}
		}
		
		 if(StringUtils.isNotBlank(travelID)){
			 if(orderType.equals(Context.ORDER_TYPE_ISLAND.toString())){ // 海岛游
				 List<IslandRebates> rebatesList = islandReviewService.findRebatesByTravelerAndStatus(Long.parseLong(travelID));
				 for(IslandRebates rebates : rebatesList){
					 String currencyMark = rebates.getCurrency().getCurrencyMark();
					 String rebatesDiff = rebates.getRebatesDiff().toString();
					 String price =  refund.get(currencyMark);
					 if(StringUtils.isNotBlank(price) && StringUtils.isNotBlank(rebatesDiff)){ // 指定币种款项存在，则进行累计，并再次存入该币种中
							BigDecimal rePrice = new BigDecimal(rebatesDiff);// 新增款项
							BigDecimal getPrice = new BigDecimal(price);  // map中现有的款项
							BigDecimal addPrice =rePrice.add(getPrice); // 合并后的款项
							refund.put(currencyMark, addPrice.toString());
						}else if(StringUtils.isNotBlank(rebatesDiff)){ // 指定币种款项不存在，则新增币种款项
							BigDecimal getPrice = new BigDecimal(rebatesDiff); // 新增款项
							refund.put(currencyMark, getPrice.toString());
						}
				 }
			 }else if(orderType.equals(Context.ORDER_TYPE_HOTEL.toString())){ // 酒店
				 List<HotelRebates> rebatesList = islandReviewService.findHotelRebatesByTravelerAndStatus(Long.parseLong(travelID));
				 for(HotelRebates rebates : rebatesList){
					 String currencyMark = rebates.getCurrency().getCurrencyMark();
					 String rebatesDiff = rebates.getRebatesDiff().toString();
					 String price =  refund.get(currencyMark);
					 if(StringUtils.isNotBlank(price) && StringUtils.isNotBlank(rebatesDiff)){ // 指定币种款项存在，则进行累计，并再次存入该币种中
							BigDecimal rePrice = new BigDecimal(rebatesDiff);// 新增款项
							BigDecimal getPrice = new BigDecimal(price);  // map中现有的款项
							BigDecimal addPrice =rePrice.add(getPrice); // 合并后的款项
							refund.put(currencyMark, addPrice.toString());
						}else if(StringUtils.isNotBlank(rebatesDiff)){ // 指定币种款项不存在，则新增币种款项
							BigDecimal getPrice = new BigDecimal(rebatesDiff); // 新增款项
							refund.put(currencyMark, getPrice.toString());
						}
				 }
			 }
		 }
		 // 对多币种款项进行字符串化
		 if(refund!=null && !refund.isEmpty()){
			 for(String key : refund.keySet()){
				 String value = refund.get(key); // 获取款项
				 BigDecimal backPrice =new BigDecimal(value); 
				 if(backPrice.compareTo(BigDecimal.ZERO)>=0){
					 // 如果只有一种币种，则不需要增加“+”
					 if(buffer.length()==0){
						 buffer.append(key+d.format(backPrice.doubleValue()));
					 }else if(buffer.length()>0){
						 buffer.append("+"+key+d.format(backPrice.doubleValue()));
					 }
				 }else{
					 buffer.append("-"+key+d.format(backPrice.doubleValue()));
				 }
			 }
		 }else{
			 buffer.append("￥0.00");
		 }
		 return buffer.toString();
	}
	
	/**
	 * 统计多客户累计返佣总金额（海岛游、酒店专用）
	 * @author ruyi.chen
	 * @param travelUuidList 返佣订单游客UUID集合
	 * @param orderType: 订单类型
	 * @return
	 */
	public static String getTravelsRebatesByOrderType(List<String> travelUuidList, String orderType){
		 DecimalFormat d = new DecimalFormat(",##0.00");
		 StringBuffer buffer = new StringBuffer();
		 String travelID = new String();
		 for (String travelUuid : travelUuidList) {
			// 合并相同币种使用的hashMap
			 Map<String,String> refund = Maps.newHashMap();
			// 根据travelUUid获取游客
			if (StringUtils.isNotBlank(travelUuid) && orderType.equals(Context.ORDER_TYPE_ISLAND.toString())) {
				IslandTraveler travel = new IslandTraveler();
				travel = islandTravelerService.getByUuid(travelUuid);
				if (travel != null) {
					travelID = travel.getId().toString();
					List<IslandRebates> rebatesList = islandReviewService.findRebatesByTravelerAndStatus(Long.parseLong(travelID));
					for (IslandRebates rebates : rebatesList) {
						String currencyMark = rebates.getCurrency().getCurrencyMark();
						String rebatesDiff = rebates.getRebatesDiff().toString();
						String price =  refund.get(currencyMark);
						if (StringUtils.isNotBlank(price) && StringUtils.isNotBlank(rebatesDiff)) { // 指定币种款项存在，则进行累计，并再次存入该币种中
							BigDecimal rePrice = new BigDecimal(rebatesDiff);// 新增款项
							BigDecimal getPrice = new BigDecimal(price);  // map中现有的款项
							BigDecimal addPrice =rePrice.add(getPrice); // 合并后的款项
							refund.put(currencyMark, addPrice.toString());
						} else if (StringUtils.isNotBlank(rebatesDiff)) { // 指定币种款项不存在，则新增币种款项
							BigDecimal getPrice = new BigDecimal(rebatesDiff); // 新增款项
							refund.put(currencyMark, getPrice.toString());
						}
					}
				}
			} else if (StringUtils.isNotBlank(travelUuid) && orderType.equals(Context.ORDER_TYPE_HOTEL.toString())) {
				HotelTraveler  travel = new HotelTraveler();
				travel = hotelTravelerService.getByUuid(travelUuid);
				if (travel != null) {
					travelID = travel.getId().toString();
					List<HotelRebates> rebatesList = islandReviewService.findHotelRebatesByTravelerAndStatus(Long.parseLong(travelID));
					for (HotelRebates rebates : rebatesList) {
						String currencyMark = rebates.getCurrency().getCurrencyMark();
						String rebatesDiff = rebates.getRebatesDiff().toString();
						String price =  refund.get(currencyMark);
						if (StringUtils.isNotBlank(price) && StringUtils.isNotBlank(rebatesDiff)) { // 指定币种款项存在，则进行累计，并再次存入该币种中
							BigDecimal rePrice = new BigDecimal(rebatesDiff);// 新增款项
							BigDecimal getPrice = new BigDecimal(price);  // map中现有的款项
							BigDecimal addPrice = rePrice.add(getPrice); // 合并后的款项
							refund.put(currencyMark, addPrice.toString());
						} else if (StringUtils.isNotBlank(rebatesDiff)) { // 指定币种款项不存在，则新增币种款项
							BigDecimal getPrice = new BigDecimal(rebatesDiff); // 新增款项
							refund.put(currencyMark, getPrice.toString());
						}
					}
				}
			}
			
			 // 对多币种款项进行字符串化
			 if (refund != null && !refund.isEmpty()) {
				 for (String key : refund.keySet()) {
					 String value = refund.get(key); // 获取款项
					 BigDecimal backPrice = new BigDecimal(value);
					 if (backPrice.compareTo(BigDecimal.ZERO) >= 0) {
						 // 如果只有一种币种，则不需要增加“+”
						 if (buffer.length() == 0) {
							 buffer.append(key + " " + d.format(backPrice.doubleValue()));
						 } else if (buffer.length() > 0) {
							 buffer.append("+" + key + " " + d.format(backPrice.doubleValue()));
						 }
					 } else {
						 buffer.append("-" + key + " " + d.format(backPrice.doubleValue()));
					 }
				 }
			 }
		 }
		 if (buffer == null || buffer.length( )< 1 ) {
			 buffer.append("￥0.00");
		 } else {
			 String prices [] = buffer.toString().split("\\+");
			 if (prices != null && prices.length >= 2) {
				 String sumPrice = "";
				 for (String price : prices) {
					 sumPrice = islandMoneyAmountService.addOrSubtract(price, sumPrice, true);
				 }
				 return sumPrice.replace(" ", "");
			 }
		 }
		 return buffer.toString();
	}

	
	/**
	 * 查询环球行签证借款的已付金额
	 * @param batchId		批次Id
	 * @param orderType     订单类型
	 * @author shijun.liu
	 * @return
	 */
	public static String getTTSQZPayedMoney(String batchId, String orderType){
		String payedMoney = "";
		if(StringUtils.isNotBlank(batchId) && StringUtils.isNotBlank(orderType)){
			payedMoney = moneyAmountService.getTTSQZPayedMoney(Integer.parseInt(batchId), 
					Integer.parseInt(orderType));
		}
		return payedMoney;
	}
	
	
	
	/**
	 * 获取单个订单的退款金额
	 */
	public static String getOrderRefundMoney(String orderType, String orderId){
		if(orderType == null || "".equals(orderType) || orderId == null) {
			return "错误的订单数据";
		}
		Set<String> mAountOrderType = new HashSet<String>();//存在moneyAmount表中的退款数据的订单类型
		mAountOrderType.add("1");
		mAountOrderType.add("2");
		mAountOrderType.add("3");
		mAountOrderType.add("4");
		mAountOrderType.add("5");
		mAountOrderType.add("6");
		mAountOrderType.add("7");
		mAountOrderType.add("10");//低空游轮
		BigDecimal one = new BigDecimal(1);
		BigDecimal money = new BigDecimal(0);
		if(mAountOrderType.contains(orderType)){
			List<MoneyAmount> list = moneyAmountService.findAmount(Long.parseLong(orderId), Context.MONEY_TYPE_TK, Integer.parseInt(orderType));
			for(MoneyAmount m : list){
				BigDecimal amount = m.getAmount();
				BigDecimal exchangerate = m.getExchangerate();
				if(exchangerate == null || "".equals(exchangerate.toString())){
					exchangerate = one;
				}
				money = money.add(amount.multiply(exchangerate));
			}
		}
		if("12".equals(orderType)){//海岛游
			List<IslandMoneyAmount> list = islandMoneyAmountService.findAmount(orderId, Context.MONEY_TYPE_TK);
			for(IslandMoneyAmount m : list){
				Double amount = m.getAmount();
				Double exchangerate = m.getExchangerate();
				if(exchangerate == null || "".equals(exchangerate.toString())){
					exchangerate = (double) 1;
				}
				money = money.add(new BigDecimal(amount*exchangerate));
			}
		}
		if("11".equals(orderType)){//酒店
			List<HotelMoneyAmount> list = hotelMoneyAmountService.findAmount(orderId, Context.MONEY_TYPE_TK);
			for(HotelMoneyAmount m : list){
				Double amount = m.getAmount();
				Double exchangerate = m.getExchangerate();
				if(exchangerate == null || "".equals(exchangerate.toString())){
					exchangerate = (double) 1;
				}
				money = money.add(new BigDecimal(amount*exchangerate));
			}
		}
		String moneyStr = MoneyNumberFormat.getThousandsMoney(money.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO);
		return moneyStr;
	}
	
	/**
	 * 获取单个订单的已开发票金额
	 */
	public static String getOrderInvoiceMoney(String orderType, String orderId){
		
		BigDecimal result = new BigDecimal(0.00);
		if(orderType == null || "".equals(orderType) || orderId == null || "".equals(orderId)){
			return result.toString();
		}
		List<Orderinvoice> list = orderInvoiceService.findOrderinvoiceByOrderIdOrderType(Integer.parseInt(orderId), Integer.parseInt(orderType));
		for(Orderinvoice m : list){
			result = result.add(m.getInvoiceAmount());
		}
		return MoneyNumberFormat.getThousandsMoney(result.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO);
	}
	
	/**
	 * 获取单个订单的已开收据金额
	 */
	public static String getOrderReceiptMoney(String orderType, String orderId){
		
		Double result = new Double(0.00);
		if(orderType == null || "".equals(orderType) || orderId == null || "".equals(orderId)){
			return result.toString();
		}
		String sql = "select sum(invoiceAmount) from orderreceipt where orderId = " + Integer.parseInt(orderId) + " and orderType = "+Integer.parseInt(orderType)+" and createStatus in (1,2)";
		List<Object> list2 = orderReceiptDao.findBySql(sql);
		if(list2 == null || list2.size() == 0 || list2.get(0) == null){
			return MoneyNumberFormat.getThousandsMoney(result, MoneyNumberFormat.THOUSANDST_POINT_TWO);
		}
		result = Double.valueOf(list2.get(0).toString());
		return MoneyNumberFormat.getThousandsMoney(result.doubleValue(), MoneyNumberFormat.THOUSANDST_POINT_TWO);
	}
	
	/**
	 * 跟据订单id 产品类型 获取订单金额
	 * 1 订单总额 2 已付金额 3 达账金额  add by chy 2015-7-21 10:15:27
	 */
	public static String getOrderMoney(String orderType, String orderId, String moneyType){
		if((orderType == null || "".equals(orderType) || orderId == null || "".equals(orderId))){//验证参数
			if("3".equals(moneyType.trim())){//bug 号 11489 如果是达账金额的话，当没有时返回空 不返回￥ 0.00
				return "";
			}
			return "￥ 0.00";
		}
		if(moneyType == null || (!"1".equals(moneyType)&&!"2".equals(moneyType)&&!"3".equals(moneyType))){//验证参数
			if("3".equals(moneyType.trim())){//bug 号 11489 如果是达账金额的话，当没有时返回空 不返回￥ 0.00
				return "";
			}
			return "￥ 0.00";
		}
		if("7".equals(orderType)){//机票
			AirticketOrder order = airticketOrderDao.getAirticketOrderById(Long.parseLong(orderId));
			if(order == null){
				if("3".equals(moneyType.trim())){//bug 号 11489 如果是达账金额的话，当没有时返回空 不返回￥ 0.00
					return "";
				}
				return "￥ 0.00";
			}
			if("1".equals(moneyType)){
				return moneyAmountService.getMoney(order.getTotalMoney());
			} else if("2".equals(moneyType)){
				return moneyAmountService.getMoney(order.getPayedMoney());
			} else if("3".equals(moneyType)){
				if(order.getAccountedMoney() == null || "".equals(order.getAccountedMoney().trim())){//bug 号 11489 如果是达账金额的话，当没有时返回空 不返回￥ 0.00
					return "";
				}
				return moneyAmountService.getMoney(order.getAccountedMoney());
			}
		}else if("6".equals(orderType)){//签证
			VisaOrder order = visaOrderDao.findOne(Long.parseLong(orderId));
			if(order == null){
				if("3".equals(moneyType.trim())){//bug 号 11489 如果是达账金额的话，当没有时返回空 不返回￥ 0.00
					return "";
				}
				return "￥ 0.00";
			}
			if("1".equals(moneyType)){
				return moneyAmountService.getMoney(order.getTotalMoney());
			} else if("2".equals(moneyType)){
				return moneyAmountService.getMoney(order.getPayedMoney());
			} else if("3".equals(moneyType)){//bug 号 11489 如果是达账金额的话，当没有时返回空 不返回￥ 0.00
				if(order.getAccountedMoney() == null || "".equals(order.getAccountedMoney().trim())){
					return "";
				}
				return moneyAmountService.getMoney(order.getAccountedMoney());
			}
		}else if("12".equals(orderType)){//海岛游
			//TODO 暂时不支持海岛游和酒店
		}else if("11".equals(orderType)){//酒店
			//TODO 暂时不支持海岛游和酒店
		} else {//单团类
			ProductOrderCommon order = productOrderCommonDao.findOne(Long.parseLong(orderId));
			if(order == null){
				if("3".equals(moneyType.trim())){//bug 号 11489 如果是达账金额的话，当没有时返回空 不返回￥ 0.00
					return "";
				}
				return "￥ 0.00";
			}
			if("1".equals(moneyType)){
				return moneyAmountService.getMoney(order.getTotalMoney());
			} else if("2".equals(moneyType)){
				return moneyAmountService.getMoney(order.getPayedMoney());
			} else if("3".equals(moneyType)){
				if(order.getAccountedMoney() == null || "".equals(order.getAccountedMoney().trim())){//bug 号 11489 如果是达账金额的话，当没有时返回空 不返回￥ 0.00
					return "";
				}
				return moneyAmountService.getMoney(order.getAccountedMoney());
			}
		}
		if("3".equals(moneyType.trim())){//bug 号 11489 如果是达账金额的话，当没有时返回空 不返回￥ 0.00
			return "";
		}
		return "￥ 0.00";
	}
/**
 * 获取不同产品类型的达账金额
 * @param string
 * @param string2
 * @return
 * @author 何旭东
 */
	public static String getAccountMoney(String string, String string2)
	{
		// TODO Auto-generated method stub
		return moneyAmountService.getAccountMoney(string,string2);
	}
	
	/**
	 * add by ruyi.chen
	 * add date 2015-07-27
	 * describe 根据订单ID，订单类型，游客ID等获取相应类型的原返佣金额
	 * @param orderId
	 * @param orderType
	 * @param travelerId
	 * @return
	 */
	public static String getRebatesMoney(Long orderId,Integer orderType,String travelerId){
		String moneyStr = "";
		String serNum = "";
		if(orderType.toString().equals(Context.ORDER_TYPE_ISLAND.toString())){
			
			if(null != travelerId && StringUtils.isNotBlank(travelerId)){
				IslandTraveler t = islandTravelerService.getById(Integer.parseInt(travelerId));
				serNum = t.getRebatesMoneySerialNum();
			}else{
				IslandOrder order = islandOrderService.getById(orderId.intValue());
				//serNum = or
			}
		}else if(orderType.toString().equals(Context.ORDER_TYPE_HOTEL.toString())){
			if(null != travelerId && StringUtils.isNotBlank(travelerId)){
				HotelTraveler t = hotelTravelerService.getById(Integer.parseInt(travelerId));
				serNum = t.getRebatesMoneySerialNum();
			}else{
				HotelOrder order = hotelOrderService.getById(orderId.intValue());
				//serNum = or
			}
		}else if(orderType.toString().equals(Context.ORDER_TYPE_JP.toString())){
			if(null != travelerId && StringUtils.isNotBlank(travelerId)){
				Traveler t = travelerService.findTravelerById(Long.parseLong(travelerId));
				serNum = t.getRebatesMoneySerialNum();
			}else{
				AirticketOrder order = airTicketOrderService.getAirticketorderById(orderId);
				serNum = order.getScheduleBackUuid();
			}
		}else if(orderType.toString().equals(Context.ORDER_TYPE_QZ.toString())){
			if(null != travelerId && StringUtils.isNotBlank(travelerId)){
				Traveler t = travelerService.findTravelerById(Long.parseLong(travelerId));
				serNum = t.getRebatesMoneySerialNum();
			}else{
				VisaOrder order = visaOrderService.findVisaOrder(orderId);
				//serNum = order.getScheduleBackUuid();
			}
		}else{
			if(null != travelerId && StringUtils.isNotBlank(travelerId)){
				Traveler t = travelerService.findTravelerById(Long.parseLong(travelerId));
				serNum = t.getRebatesMoneySerialNum();
			}else{
				ProductOrderCommon order = orderCommonService.getProductorderById(orderId);
				serNum = order.getScheduleBackUuid();
			}
		}
		if(null == serNum || serNum.trim().length() <= 0){
			return null;
		}
		moneyStr = getMoneyBySerialNumAndOrderType(serNum, 2, orderType);
		return moneyStr;
	}
	/**
	 * add by ruyi.chen
	 * add date 2015-08-13
	 * mod by yang.jiang 2016-2-26 18:08:51
	 * describe 获取订单发票、收据的展示状态 (创建状态 0未开 1已开)
	 * @param type 1 发票  2 收据
	 * @param orderType  订单类型
	 * @param orderId 订单标识
	 * @return
	 */
	public static String getOrderInvoiceReceiptStatus(Integer type,Integer orderType,Long orderId){
		String orderStutusStr = "";
		List<Map<Object,Object>> list =null;
		StringBuffer sbf = new StringBuffer();
		
		if(type.intValue() == 1){
			sbf.append("select 1 from orderinvoice oi where oi.createStatus = 1 and oi.orderType =? and oi.orderId = ?");
			list = productOrderCommonDao.findBySql(sbf.toString(), orderType, orderId);
			if(null != list && 0 < list.size()){
				orderStutusStr ="已开发票";
			}else{
				orderStutusStr ="未开发票";
			}
		}else if(type.intValue() == 2){
			sbf.append("select 1 from orderreceipt oi where oi.createStatus = 1 and oi.orderType =? and oi.orderId = ?");
			list = productOrderCommonDao.findBySql(sbf.toString(), orderType, orderId);
			if(null != list && 0 < list.size()){
				orderStutusStr ="已开收据";
			}else{
				orderStutusStr ="未开收据";
			}
		}
		
		return orderStutusStr;
	}
	
	/**
	 * desc:判断是否可以取消
	 * @param type
	 * @param orderType
	 * @param orderId
	 * update by ruyi.chen
	 * update date 2015-10-12
	 * @return  0 无发票、收据记录   1 有申请发票记录 2 有申请收据记录
	 */
	public static String canCancel(Integer orderType,Long orderId){
		List<Map<Object,Object>> list =null;
		StringBuffer sbf = new StringBuffer();
		StringBuffer sbf2 = new StringBuffer();
		String isCanCancel = "0";
		sbf.append("select 1 from orderinvoice oi where oi.verifyStatus in(0,1)and oi.orderType =?")
		.append(" and oi.orderId = ?");
		list = productOrderCommonDao.findBySql(sbf.toString(), orderType,orderId);
		if(null != list && 0 < list.size()){
			isCanCancel = "1";
		}
		sbf2.append("select 1 from orderreceipt oi where oi.verifyStatus in(0,1) and oi.createStatus<>2 and oi.orderType =?")
		.append(" and oi.orderId = ?");
		list = productOrderCommonDao.findBySql(sbf2.toString(), orderType,orderId);
		if(null != list && 0 < list.size()){
			isCanCancel = "2";
		}
		
		return isCanCancel;
	}
	
	/**支持多币种，
	 * 获取未付金额= totalMoneySerialNum-serialNum
	 *@author zhaohaiming
	 * */
	public static String getNotPayMoney(String orderType,String totalMoneySerialNum,String serialNum){
		String str ="";
		List<Map<String,Object>> tmp = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> total = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> money = new ArrayList<Map<String,Object>>();
		if(StringUtils.isNotBlank(totalMoneySerialNum)){
			  total = moneyAmountService.getMoneyBySerialNum(orderType,totalMoneySerialNum);
		}
		if(StringUtils.isNotBlank(serialNum)){
			money = moneyAmountService.getMoneyBySerialNum(orderType,serialNum);
			if(orderType.equals("2") && UserUtils.getUser().getCompany().getUuid().equals("7a45838277a811e5bc1e000c29cf2586")){
				Map<String, Object> differenceMap = returnDifferenceServiece.getReturnDifferenceBySerialNum(serialNum);
				if(differenceMap != null && differenceMap.get("currencyId")!=null){
					for(Map<String,Object> moneyMap : money){
						if(moneyMap.get("currencyId").toString().equals(differenceMap.get("currencyId").toString())){
							moneyMap.put("amount", new BigDecimal(moneyMap.get("amount").toString()).add(new BigDecimal(differenceMap.get("amount").toString())));
						}
					}
				}
			}
		}
		
		if(CollectionUtils.isNotEmpty(total)){
			//total不为空，判断money是否为空
			if(CollectionUtils.isNotEmpty(money)){
				int alsize = total.size();
				int clsize = money.size();
				//遍历两个list，币种一样相减，相减的结果放到新的list里，并从原来的list移除这两个相减的元素
				
				for(int al=0;al<alsize;){
					boolean flag = false;
					Map<String,Object> almap = total.get(al);
					for(int cl = 0;cl<clsize;){
						Map<String,Object> clmap = money.get(cl);
						Map<String,Object> tmpmap = new HashMap<String,Object>();
						if(almap.get("currencyId").equals(clmap.get("currencyId"))){
							BigDecimal ao = (BigDecimal) almap.get("amount");
							BigDecimal co = (BigDecimal) clmap.get("amount");
							tmpmap.put("currencyId", almap.get("currencyId"));
							tmpmap.put("amount", ao.subtract(co));
							tmp.add(tmpmap);
							money.remove(cl);
							clsize=clsize-1;
							flag=true;
						}else{
							cl++;
						}
					}
					if(flag){
						  total.remove(al);
						  alsize = alsize-1;
					}else{
						al++;
					}
					
				}
				//相减完，并移除了元素，再次判断这个list是否为空，
				if(money.size()>0){
					//money不为空的话,金额取负数，放进tmp
					int csize = money.size();
					for(int i = 0;i<csize;i++){
						Map<String,Object> cmap = money.get(i);
						Map<String,Object> newmap = new HashMap<String,Object>();
						BigDecimal b = (BigDecimal) cmap.get("amount");
						String currencyId = cmap.get("currencyId").toString();
						newmap.put("currencyId",currencyId);
						newmap.put("amount", b.multiply(new BigDecimal(-1)));
						tmp.add(newmap);
					}
				}
				if(total.size()>0){
					//total不为空的话，放进tmp
					int asize = total.size();
					for(int in =0;in<asize;in++){
						tmp.add(total.get(in));
					}
				}
				str = getMoneySumStr(tmp);
			}else{
				//money为空，直接去total
				str = getMoneySumStr(total);
			}
		}else{
			//判断money是否为空
			if(CollectionUtils.isNotEmpty(money)){
				//应付不为空，利润=（-应付）
				int clsize = money.size();
				for(int cl=0;cl<clsize;cl++){
					Map<String,Object> map = money.get(cl);
					Map<String,Object> tmpmap = new HashMap<String,Object>();
					Object o = map.get("amount");
					if(o != null && !"0".equals(o.toString())){
						tmpmap.put("currencyId", map.get("currencyId"));
						BigDecimal bd = (BigDecimal) o;
						tmpmap.put("amount", bd.multiply(new BigDecimal(-1)));
						tmp.add(tmpmap);
					}
					
				}
				str = getMoneySumStr(tmp);
			}
		}
		if(StringUtils.isBlank(str)){
			str="0.00";
		}
		return str;
	}
	
	
	private static String getMoneySumStr(List<Map<String,Object>> list) {
		StringBuffer sb = new StringBuffer();
		StringBuffer s = new StringBuffer();
		if(CollectionUtils.isNotEmpty(list)){
			int size = list.size();
			for(int i=0;i<size;i++){
				Map<String,Object> map = list.get(i);
				Currency c = currencyService.findCurrency(Long.valueOf(map.get("currencyId").toString()));
				BigDecimal amount = (BigDecimal) map.get("amount");
				int flag = amount.compareTo(new BigDecimal(0)); //金额会有负数，所以和零比较大小-1表示小于,0是等于,1是大于.
				if(i==0){
					//判断币种是否
					if(c != null && StringUtils.isNotBlank(c.getCurrencyMark())){
						if(amount != null){
							if(flag== -1){//金额小于0
								if(sb.length()>0){
									sb.append("+").append(c.getCurrencyMark()).append(MoneyNumberFormat.getThousandsMoney(Double.valueOf(amount.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								}else{
									sb.append(c.getCurrencyMark()).append(MoneyNumberFormat.getThousandsMoney(Double.valueOf(amount.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								}
								
							}else if(flag ==1){
								if(s.length()>0){
									s.append("+").append(c.getCurrencyMark()).append(MoneyNumberFormat.getThousandsMoney(Double.valueOf(amount.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								}else{
									s.append(c.getCurrencyMark()).append(MoneyNumberFormat.getThousandsMoney(Double.valueOf(amount.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								}
								
							}
							
						}
					}else{
						if(amount != null){
							if(flag== -1){
								if(sb.length()>0){
									sb.append("+").append(c.getCurrencyMark()).append(MoneyNumberFormat.getThousandsMoney(Double.valueOf(amount.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								}else{
									sb.append(c.getCurrencyMark()).append(MoneyNumberFormat.getThousandsMoney(Double.valueOf(amount.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								}
								
							}else if(flag ==1){
								if(s.length()>0){
									s.append("+").append(MoneyNumberFormat.getThousandsMoney(Double.valueOf(amount.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								}else{
									s.append(MoneyNumberFormat.getThousandsMoney(Double.valueOf(amount.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								}
								
							}
						}
					}
				}else{
					if(c != null && StringUtils.isNotBlank(c.getCurrencyMark())){
						if(amount != null){
							if(flag== -1){
								if(sb.length()>0){
									sb.append("+").append(c.getCurrencyMark()).append(c.getCurrencyMark()).append(MoneyNumberFormat.getThousandsMoney(Double.valueOf(amount.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								}else{
									sb.append(c.getCurrencyMark()).append(c.getCurrencyMark()).append(MoneyNumberFormat.getThousandsMoney(Double.valueOf(amount.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								}
								
							}else if(flag ==1){
								if(s.length()>0){
									s.append("+").append(c.getCurrencyMark()).append(MoneyNumberFormat.getThousandsMoney(Double.valueOf(amount.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								}else{
									s.append(c.getCurrencyMark()).append(MoneyNumberFormat.getThousandsMoney(Double.valueOf(amount.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								}
								
							}
							
						}
					}else{
						if(amount != null){
							if(flag== -1){
								if(sb.length()>0){
									sb.append("+").append(c.getCurrencyMark()).append(MoneyNumberFormat.getThousandsMoney(Double.valueOf(amount.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								}else{
									sb.append(c.getCurrencyMark()).append(MoneyNumberFormat.getThousandsMoney(Double.valueOf(amount.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								}
								
							}else if(flag ==1){
								if(s.length()>0){
									s.append("+").append(MoneyNumberFormat.getThousandsMoney(Double.valueOf(amount.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								}else{
									s.append(MoneyNumberFormat.getThousandsMoney(Double.valueOf(amount.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
								}
								
							}
						}
					}
				}
			}
		}
		return s.append(sb).toString();
	}
	/**
	 * add by ruyi.chen
	 * add date 2015-08-17
	 * describe 根据订单ID，订单类型获取相应类型的订单原返佣总金额
	 * @param orderId
	 * @param orderType
	 * @return
	 */
	public static String getAllRebatesMoney(Long orderId,Integer orderType){
		String moneyStr = "";
		String serNum = "";
		List<String> serNumList = Lists.newArrayList();
		if(orderType.toString().equals(Context.ORDER_TYPE_ISLAND.toString())){
			IslandOrder order = islandOrderService.getById(orderId.intValue());
			List<IslandTraveler> ls = islandTravelerService.findTravelerByOrderUuid(order.getUuid());
			//TODO 添加海岛返佣数据
		}else if(orderType.toString().equals(Context.ORDER_TYPE_HOTEL.toString())){
			HotelOrder order = hotelOrderService.getById(orderId.intValue());
			List<HotelTraveler> ls = hotelTravelerService.findTravelerByOrderUuid(order.getUuid(), false);
			//TODO 添加酒店返佣数据
		}else if(orderType.toString().equals(Context.ORDER_TYPE_JP.toString())){
			AirticketOrder order = airTicketOrderService.getAirticketorderById(orderId);
			serNum = order.getScheduleBackUuid();
			if(null != serNum && StringUtils.isNotBlank(serNum)){
				serNumList.add(serNum);
			}
			List<Traveler> ls = travelerService.findTravelerByOrderIdAndOrderType(orderId, orderType);
			for(Traveler t : ls){
				if(null != t.getRebatesMoneySerialNum() && StringUtils.isNotBlank(t.getRebatesMoneySerialNum())){
					serNumList.add(t.getRebatesMoneySerialNum());
				}
			}
			if(serNumList.size() > 0){
				moneyStr = getMoneyAmountBySerialNum(serNumList, 2);
			}else{
				return null;
			}
			
		}else if(orderType.toString().equals(Context.ORDER_TYPE_QZ.toString())){
			VisaOrder order = visaOrderService.findVisaOrder(orderId);
			//TODO签证返佣完成再继续
			if(null != serNum && StringUtils.isNotBlank(serNum)){
				serNumList.add(serNum);
			}
			List<Traveler> ls = travelerService.findTravelerByOrderIdAndOrderType(orderId, orderType);
			for(Traveler t : ls){
				if(null != t.getRebatesMoneySerialNum() && StringUtils.isNotBlank(t.getRebatesMoneySerialNum())){
					serNumList.add(t.getRebatesMoneySerialNum());
				}
			}
			if(serNumList.size() > 0){
				moneyStr = getMoneyAmountBySerialNum(serNumList, 2);
			}else{
				return null;
			}
		}else{
			ProductOrderCommon order = orderCommonService.getProductorderById(orderId);
			serNum = order.getScheduleBackUuid();
			if(null != serNum && StringUtils.isNotBlank(serNum)){
				serNumList.add(serNum);
			}
			List<Traveler> ls = travelerService.findTravelerByOrderIdAndOrderType(orderId, orderType);
			for(Traveler t : ls){
				if(null != t.getRebatesMoneySerialNum() && StringUtils.isNotBlank(t.getRebatesMoneySerialNum())){
					serNumList.add(t.getRebatesMoneySerialNum());
				}
			}
			if(serNumList.size() > 0){
				moneyStr = getMoneyAmountBySerialNum(serNumList, 2);
			}else{
				return null;
			}
			
		}
		return moneyStr;
	}
	
	/**
	 * 两个UUID相减
	 * 
	 * */
	public static String getDecrease(String serialNum1,String serialNum2){
		//参数列表
		List<Object> par = new ArrayList<Object>();
		par.add(serialNum1);
		par.add(serialNum2);
		List<Map<Object, Object>> resultList = visaOrderDao.findBySql(SqlConstant.JISUAN_CHAJIA,Map.class, par.toArray());
		
		StringBuffer strBuffer = new StringBuffer();
		DecimalFormat d= new DecimalFormat("#,##0.00");
		int i = 0;
		for(Map<Object, Object> listin : resultList) {
			//货币id
			Object currencyId = listin.get("currencyId");
			//应收金额
			Object big = listin.get("big");
			//计算后的金额
			Object result = listin.get("result");
			//应收金额为空,进行下一次循环 计算结果为0 的时候,直接进行下一次循环,不再操作
			if(null == big || "".equals(big)) {
				continue;
			//应收不为空,已支付为空,则记录货币id和应收金额
			}else if (null == result || "".equals(result)) {
				if(i != 0){
					strBuffer.append("+");
				}
				Currency currency = currencyDao.findById(Long.valueOf(currencyId.toString()));
				strBuffer.append(currency.getCurrencyMark());
				strBuffer.append(d.format(big));
			//应收和已收都不为空,获取计算后的结果
			}else if("0.00".equals(result.toString())){
				continue;
			}else{
				if(i != 0){
					strBuffer.append("+");
				}
				Currency currency = currencyDao.findById(Long.valueOf(currencyId.toString()));
				strBuffer.append(currency.getCurrencyMark());
				strBuffer.append(d.format(result));
			}
			i++;
		}
		if(strBuffer.length() == 0){
			return "¥0.00";
		}else{
			return strBuffer.toString();
		}
	}
	
	/**
	 * @Description 封装订单信息数据
	 * @author yakun.bai
	 * @Date 2015-12-2
	 */
	public static Map<String, Object> getOrderInfo(ProductOrderCommon productOrder, ActivityGroup productGroup) {
		Map<String, Object> orderInfo = Maps.newHashMap();

		// 订单id
		Long orderId = productOrder.getId();
		orderInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID, orderId);
		// 下单人
		Long orderCreator = productOrder.getCreateBy().getId();
		orderInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR, orderCreator);
		// 下单人姓名
		String createName = productOrder.getCreateBy().getName();
		orderInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR_NAME, createName);
		// 销售
		Integer salerId = productOrder.getSalerId();
		orderInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER, salerId);
		// 销售姓名
		String salerName = productOrder.getSalerName();
		orderInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER_NAME, salerName);
		// 渠道
		Long agentId = productOrder.getOrderCompany();
		orderInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT, agentId);
		// 渠道名称
		String agentName = productOrder.getOrderCompanyName();
		orderInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT_NAME, agentName);
		// 下单时间
		Date orderTime = productOrder.getOrderTime();
		orderInfo.put(Context.REVIEW_VARIABLE_KEY_ORDER_TIME, orderTime);
		// 订单编号
		String orderNum = productOrder.getOrderNum();
		orderInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO, orderNum);
		// 订单总额
		String totalMoneySerialNum = productOrder.getTotalMoney();
		String totalMoney = moneyAmountService.getMoneyStr(totalMoneySerialNum);
		orderInfo.put(Context.REVIEW_VARIABLE_KEY_TOTAL_MONEY, totalMoney);
		// 团期ID
		Long groupId = productGroup.getId();
		orderInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_ID, groupId);
		// 团期编号
		String groupCode = productGroup.getGroupCode();
		orderInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, groupCode);

		return orderInfo;
	}

	/**
	 * @Description 封装产品信息
	 * @author yakun.bai
	 * @Date 2015-12-2
	 */
	public static Map<String, Object> getProductInfo(TravelActivity product) {
		Map<String, Object> productInfo = Maps.newHashMap();
		// 产品id
		Long productId = product.getId();
		productInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID, productId);
		// 产品名称
		String acitivityName = product.getAcitivityName();
		productInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, acitivityName);
		// 出团日期
		Date groupOpenDate = product.getGroupOpenDate();
		productInfo.put(Context.REVIEW_VARIABLE_KEY_GROUP_OPEN_DATE, groupOpenDate);
		// 行程天数
		Integer activityDuration = product.getActivityDuration();
		productInfo.put(Context.REVIEW_VARIABLE_KEY_ACTIVITY_DURATION, activityDuration);
		// 目的地
		String targetAreaNames = product.getTargetAreaNames();
		productInfo.put(Context.REVIEW_VARIABLE_KEY_TARGETAREANAMES, targetAreaNames);
		// 计调
		Long operator = product.getCreateBy().getId();
		productInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, operator);
		// 计调姓名
		String operatorName = product.getCreateBy().getName();
		productInfo.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR_NAME, operatorName);

		return productInfo;
	}
	
	
	/**
	 * @Description 查询订单应收、已收、达帐金额
	 * @author yakun.bai
	 * @Date 2015-12-9
	 */
	public static List<Map<String, Object>> queryOrderMoney(String tableName, String ids) {
		
		//订单查询sql语句
    	StringBuilder sql = new StringBuilder("");
    	sql.append("SELECT pro.id orderId, totalOuter.moneyStr totalMoney, payedOuter.moneyStr payedMoney, accountedOuter.moneyStr accountedMoney ")
    		.append("FROM " + tableName + " pro ")
    			
    			//订单应收金额多币种查询
    			.append("LEFT JOIN ( ")
    				.append("SELECT mao.serialNum ")
	    				.append(",GROUP_CONCAT(CONCAT ( ")
	    					.append("c.currency_mark, ' ', ")
	    					.append("FORMAT(mao.amount, 2) ")
	    					.append(") ORDER BY mao.currencyId separator '+') moneyStr ")
	    			.append("FROM " + tableName + " pro, money_amount mao ")
	    			.append("LEFT JOIN currency c ON mao.currencyId = c.currency_id ")
	    			.append("WHERE pro.total_money = mao.serialNum AND pro.id in (" + ids + ")")
	    			.append("GROUP BY mao.serialNum ")
	    			.append(") totalOuter ON totalOuter.serialNum = pro.total_money ")
	    			
	    		//订单实收金额多币种查询
    			.append("LEFT JOIN ( ")
    				.append("SELECT mao.serialNum ")
	    				.append(",GROUP_CONCAT(CONCAT ( ")
	    					.append("c.currency_mark, ' ', ")
	    					.append("FORMAT(mao.amount, 2) ")
	    					.append(") ORDER BY mao.currencyId separator '+') moneyStr ")
	    			.append("FROM " + tableName + " pro, money_amount mao ")
	    			.append("LEFT JOIN currency c ON mao.currencyId = c.currency_id ")
	    			.append("WHERE pro.payed_money = mao.serialNum AND pro.id in (" + ids + ")")
	    			.append("GROUP BY mao.serialNum ")
	    			.append(") payedOuter ON payedOuter.serialNum = pro.payed_money ")
	    			
	    		//订单已达帐金额多币种查询
    			.append("LEFT JOIN ( ")
    				.append("SELECT mao.serialNum ")
	    				.append(",GROUP_CONCAT(CONCAT ( ")
	    					.append("c.currency_mark, ' ', ")
	    					.append("FORMAT(mao.amount, 2) ")
	    					.append(") ORDER BY mao.currencyId separator '+') moneyStr ")
	    			.append("FROM " + tableName + " pro, money_amount mao ")
	    			.append("LEFT JOIN currency c ON mao.currencyId = c.currency_id ")
	    			.append("WHERE pro.accounted_money = mao.serialNum AND pro.id in (" + ids + ")")
	    			.append("GROUP BY mao.serialNum ")
	    			.append(") accountedOuter ON accountedOuter.serialNum = pro.accounted_money ")
    			
    			.append("WHERE pro.id in (" + ids + ")");
		
		List<Map<String, Object>> orderList = productOrderCommonDao.findBySql(sql.toString(), Map.class);
		return orderList;
	}
	
	/**
	 * 用于团期切位的币种标志查询 by chy 2016年1月12日22:07:57
	 * @param curArray 币种数组  以逗号 隔开
	 * @param index 取第几个币种 0 1 2 3 4 5 6
	 * @param cType 去币种符号还是汉子名称 0 符号 1 名称
	 * @return
	 */
	public static String getCurFlagByIndexAndCurArray(String curArray, Integer index, Integer cType){
		if(curArray == null){
			return "";
		}
		if(index == null || !NumberUtils.isNumber(index.toString())){
			index = 0;//默认取第一个
		}
		if(cType == null || !NumberUtils.isNumber(cType.toString())){
			cType = 0;//默认取符号
		}
		String[] curs = curArray.split(",");
		if(curs == null || curs.length == 0 || curs.length < index + 1){
			return "";
		}
		String curIdstr = curs[index];
		if(!NumberUtils.isNumber(curIdstr)){
			return "";
		}
		String nameOrFlag = CurrencyUtils.getCurrencyNameOrFlag(Long.parseLong(curIdstr), cType.toString());
		return nameOrFlag;
	}

	/**
	 * 根据docInfo ids 查询对应的 docInfo对象
     */
	public static List<DocInfo> getDocInfosByIds(String ids) {
		List<DocInfo> docInfos = null;
		if(StringUtils.isNotBlank(ids)) {
			docInfos = docInfoService.getDocInfoBydocids(ids);
		}
		return docInfos;
	}
	
	/**
	 * 通过订单号获取已付款的游客姓名(签证)
	 * @param orderNum
	 * @return String
	 * @throws
	 * @author wangyang
	 * @date 2016.3.15
	 * */
	public static String getPayedTravelerName(String orderNum, String orderType){
		
		if(StringUtils.isNotBlank(orderType) && orderType.equals("6")){
			
			List<String> namelist = new ArrayList<String>();
			namelist = orderCommonService.findTravelerNameList(orderNum);
			
			if(CollectionUtils.isNotEmpty(namelist)){
				StringBuffer buffer = new StringBuffer("");
				
				for(int i = 0; i < namelist.size()-1; i++){
					buffer.append(namelist.get(i)).append(",");
				}
				buffer.append(namelist.get(namelist.size()-1));
				
				return buffer.toString();
			} else {
				return null;
			}
			
		} else {
			return null;
		}
	}
}
