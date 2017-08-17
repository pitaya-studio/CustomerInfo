package com.trekiz.admin.review.airticketChange.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.airticket.repository.AirticketPreOrderDao;
import com.trekiz.admin.modules.order.airticket.service.AirticketPreOrderService;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.service.AirticketOrderStockService;
import com.trekiz.admin.modules.order.service.OrderContactsService;
import com.trekiz.admin.modules.order.service.OrderStatisticsService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.review.airticketChange.dao.INewAirticketChangeDao;
import com.trekiz.admin.review.prdreturn.common.service.IProductReturnService;




@Service
@Transactional(readOnly = true)
public class NewAirticketChangeServiceImpl implements INewAirticketChangeService{

	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	
	@Autowired
    private AirticketPreOrderDao airticketPreOrderDao;
	
	@Autowired
	private OrderStatisticsService orderStatisticsService;
	
	@Autowired
    private ActivityAirTicketDao airTicketDao;
	
	@Autowired
	private AirticketOrderStockService airticketOrderStockService;
	
	@Autowired
	private IProductReturnService productReturnService;
	
	@Autowired
	private IActivityAirTicketService activityAirTicketService;
	
	@Autowired
	private INewAirticketChangeDao airticketChangeDao;
	
	@Autowired
	private MoneyAmountDao amountDao;
	
	@Autowired
	private MoneyAmountService  moneyAmountService;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private com.quauq.review.core.engine.ReviewService processReviewService;
	
	@Autowired
	private OrderContactsService orderContactsService;
	
	@Override
	public List<Map<String, Object>> newAreaGaiQianCheck(Map map) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * @Description 退票和改签审批通过后共同调用方法，reviewId不为空则是改签，为空则是退票
	 * @author yakun.bai
	 * @Date 2016-2-18
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public synchronized Integer changeOrExit(String reviewId, String orderId, String travelId) throws Exception {
		if (StringUtils.isNotBlank(reviewId)) {
			planeReviewNew(reviewId);
			return null;
		} else {
			return productReturnService.returnPosition(orderId, travelId);
		}
	}

	@Transactional(readOnly = false,rollbackFor={Exception.class})
    public void planeReviewNew(String reviewId) throws Exception{
		
		Map<String, Object> mapPrecess =  processReviewService.getReviewDetailMapByReviewId(reviewId.toString());
		//订单ID
		Long orderId = Long.parseLong(mapPrecess.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID).toString());
		//游客ID
		Long tralverId = Long.parseLong(mapPrecess.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID).toString());
		Traveler traveler = airticketOrderDao.queryoneTravler(tralverId); // 获取原订单游客实体
		
		if ("0".equals(traveler.getIsAirticketFlag())) {
			throw new Exception("游客已退票，不能改签");
		}
		
			// 改签前的原订单实体
			AirticketOrder order = airticketOrderDao.getAirticketOrderById(orderId);
			// 被改签的产品实体
			ActivityAirTicket airticket = airTicketDao.findOne(order.getAirticketId());
			//余位归还
			Map<String,String> rMap = orderStatisticsService.getPlaceHolderInfo(order.getId(), Context.ORDER_STATUS_AIR_TICKET);
			if (null != rMap && null != rMap.get(Context.RESULT)) {
            	String resultP = rMap.get(Context.RESULT);
            	if (Context.ORDER_PLACEHOLDER_YES.toString().equals(resultP)) {
            		//当订单占位时归还余位
            		Map<String,String> pMap = orderStatisticsService.saveAirTicketActivityPlaceHolderChange(
            				order.getId(), Context.ORDER_STATUS_AIR_TICKET, 1, airticket);
            		//余位处理失败
            		if (null != pMap && Context.ORDER_PLACEHOLDER_ERROR.toString().equals( pMap.get(Context.RESULT))) {
            			throw new Exception(Context.MESSAGE);
            		}
            		//余位处理成功
            		else if (null != pMap && Context.ORDER_PLACEHOLDER_YES.toString().equals( pMap.get(Context.RESULT))) {
            			
            		} else {
            			throw new Exception("归还余位失败！");
            		}
            	} else if (Context.ORDER_PLACEHOLDER_ERROR.toString().equals(resultP)) {
            		throw new Exception(rMap.get(Context.MESSAGE));
            	}
            } else {
            	throw new Exception("游客改签失败！");
            }
			// TODO （从这里开始写创建新产品订单并按照支付类型扣减余位 ）
			
		    //查询新产品实体
			ActivityAirTicket newPro = airticketOrderDao.queryOneActivityAirTicket(Long.valueOf(mapPrecess.get("productId").toString()));//新产品
		    
		    //临时保存游客结算价
		    String  varTraPayPrice  = UUID.randomUUID().toString();
		    List<MoneyAmount> varList = moneyAmountService.findAmountBySerialNum(traveler.getPayPriceSerialNum());
		    List<MoneyAmount> saveList = new ArrayList<MoneyAmount>();
		    for (int i = 0; i < varList.size(); i++) {
		    	Integer currencyId  = varList.get(i).getCurrencyId();
				BigDecimal totprice = varList.get(i).getAmount();
				Currency currency = currencyService.findCurrency(currencyId.longValue());
				BigDecimal ex = currency.getCurrencyExchangerate();
				MoneyAmount moneyAmount = new MoneyAmount(varTraPayPrice,currencyId,totprice , Context.MONEY_TYPE_13, Context.PRODUCT_TYPE_AIRTICKET, Context.BUSINESS_TYPE_ORDER, UserUtils.getUser().getId(),ex);
				saveList.add(moneyAmount);
			}
		    moneyAmountService.saveMoneyAmounts(saveList);
		    //临时保存游客结算价
		    //旧订单中游客金额置为0
		    amountDao.updateOrderPayPriceAmount(traveler.getPayPriceSerialNum());
		    //旧订单中游客金额置为0
		    traveler.setDelFlag(6);
		    TravelerDao travelerDao = SpringContextHolder.getBean("travelerDao");
		    travelerDao.updateTravelerDelFlag(new Integer(6), tralverId); // 修正原订单游客状态
		    
		    // 获取原订单联系人
			List<OrderContacts> contactsList =  orderContactsService.findOrderContactsByOrderIdAndOrderType(order.getId(), 7);
		    
		    AirticketOrder airticketOrderNew = new AirticketOrder(); // 新订单实体
		    airticketOrderNew.setSalerId(order.getSalerId());
		    airticketOrderNew.setSalerName(order.getSalerName());
		    airticketOrderNew.setProductTypeId(Long.valueOf(newPro.getProduct_type_id() == null ? 0 : newPro.getProduct_type_id()));
		    OfficeService  officeService = SpringContextHolder.getBean("officeService");
				String companyName = officeService.get(newPro.getProCompany()).getName();
				SysIncreaseService	sysIncreaseService = SpringContextHolder.getBean("sysIncreaseService");
				String orderNum = sysIncreaseService.updateSysIncrease(companyName
						.length() > 3 ? companyName.substring(0, 3) : companyName,
								newPro.getProCompany(), null, Context.ORDER_NUM_TYPE); // 获取新订单编号
				airticketOrderNew.setOrderNo(orderNum);
				airticketOrderNew.setGroupCode(newPro.getGroupCode());
				airticketOrderNew.setLockStatus(0);
				airticketOrderNew.setNagentName(order.getNagentName());
				airticketOrderNew.setType(AirticketOrder.TYPE_DB);// 单团类型
			airticketOrderNew.setAirticketId(newPro.getId());
		    airticketOrderNew.setAgentinfoId(order.getAgentinfoId());
		    airticketOrderNew.setProductTypeId(Long.parseLong(newPro.getProduct_type_id()+""));
		    airticketOrderNew.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_YZW));
		    airticketOrderNew.setCreateBy(order.getCreateBy());
		    if(traveler.getPersonType().toString().equals("1")){
		    	airticketOrderNew.setTotalMoney(newPro.getSettlementAdultPrice().toString());
		    	airticketOrderNew.setAdultNum(1);
		    }
			if(traveler.getPersonType().toString().equals("2")){
				airticketOrderNew.setTotalMoney(newPro.getSettlementcChildPrice().toString());
				airticketOrderNew.setChildNum(1);
			}
			if(traveler.getPersonType().toString().equals("3")){
				airticketOrderNew.setTotalMoney(newPro.getSettlementSpecialPrice().toString());
				airticketOrderNew.setSpecialNum(1);
			}
			int totalPersion=Integer.valueOf(1);
			airticketOrderNew.setPersonNum(totalPersion); // 新订单生成完毕
			
			//旧订单总金额扣减
//			List<MoneyAmount> newOrderTotalPriceList = moneyAmountService.decreaseMoneyNew(order.getTotalMoney(), varTraPayPrice);
			List<MoneyAmount> newOrderTotalPriceList = moneyAmountService.decreaseMoneyNew(order.getTotalMoney(), traveler.getPayPriceSerialNum());
//			List<MoneyAmount> newOrderTotalPriceList = moneyAmountService.decreaseMoney("596bf314-67d8-4f5e-9b38-cb672dff4b8a","724cdfba-5487-420f-945f-645e900279b3");
			List<MoneyAmount> mList = new ArrayList<MoneyAmount>();
			String scheduleBackUuid = UuidUtils.generUuid();
			//扣减之后的金额放到旧订单的总额中
			order.setTotalMoney(scheduleBackUuid);
			for (int i = 0; i < newOrderTotalPriceList.size(); i++) {
				Integer currencyId  = newOrderTotalPriceList.get(i).getCurrencyId();
				BigDecimal totprice = newOrderTotalPriceList.get(i).getAmount();
				Currency currency = currencyService.findCurrency(currencyId.longValue());
				BigDecimal ex = currency.getCurrencyExchangerate();
				MoneyAmount moneyAmount = new MoneyAmount(scheduleBackUuid,currencyId,totprice , Context.MONEY_TYPE_13, Context.PRODUCT_TYPE_AIRTICKET, Context.BUSINESS_TYPE_ORDER, UserUtils.getUser().getId(),ex);
				mList.add(moneyAmount);
			}
			moneyAmountService.saveMoneyAmounts(mList);
			//旧订单总金额扣减
			
			// 新游客实体开始生成
			Traveler orderTravelAjax = new Traveler();
		    orderTravelAjax.setOrderId(airticketOrderNew.getId());
		    orderTravelAjax.setNationality(traveler.getNationality());
		    orderTravelAjax.setSex(traveler.getSex());
		    orderTravelAjax.setBirthDay(traveler.getBirthDay());
		    orderTravelAjax.setIssuePlace(traveler.getIssuePlace());
		    orderTravelAjax.setValidityDate(traveler.getValidityDate());
		    orderTravelAjax.setTelephone(traveler.getTelephone());
		    orderTravelAjax.setRemark(traveler.getRemark());
		    orderTravelAjax.setSrcPrice(traveler.getSrcPrice());
		    orderTravelAjax.setPassportCode(traveler.getPassportCode());
		    orderTravelAjax.setPassportValidity(traveler.getPassportValidity());
		    orderTravelAjax.setPassportType(traveler.getPassportType());
		    orderTravelAjax.setPassportStatus(traveler.getPassportStatus());
		    orderTravelAjax.setSrcPriceCurrency(traveler.getSrcPriceCurrency());
		    orderTravelAjax.setSingleDiffCurrency(traveler.getSingleDiffCurrency());
		    orderTravelAjax.setOrderType(traveler.getOrderType());
		    orderTravelAjax.setName(traveler.getName());
		    orderTravelAjax.setNameSpell(traveler.getNameSpell());
		    /**
		     * 计算新游客结算价
		     */
		    String newOrderTraSerialNum = UUID.randomUUID().toString();
		    List<MoneyAmount> Lists = new ArrayList<MoneyAmount>();
			Integer currencyId  = airticket.getCurrency_id().intValue();
			BigDecimal totprice = null;
			switch (traveler.getPersonType().toString()) { // forbug 改签后新生成的游客的结算价应该为新机票产品对应的人(成人儿童特殊人群)的成本价	
				case "1":
					totprice = newPro.getSettlementAdultPrice();
//					totprice = airticket.getSettlementAdultPrice();
					break;
				case "2":
					totprice = newPro.getSettlementcChildPrice();
//					totprice = airticket.getSettlementcChildPrice();
					break;
				default:
					totprice = newPro.getSettlementSpecialPrice();
//					totprice = airticket.getSettlementSpecialPrice();
					break;
			}
			Currency currency = currencyService.findCurrency(currencyId.longValue());
			BigDecimal ex = currency.getCurrencyExchangerate();
			MoneyAmount moneyAmount = new MoneyAmount(newOrderTraSerialNum,currencyId,totprice , Context.MONEY_TYPE_13, Context.PRODUCT_TYPE_AIRTICKET, Context.BUSINESS_TYPE_ORDER, UserUtils.getUser().getId(),ex);
			Lists.add(moneyAmount);
		    moneyAmountService.saveMoneyAmounts(Lists);
		    orderTravelAjax.setPayPriceSerialNum(newOrderTraSerialNum);
		    /**
		     * 计算新游客结算价
		     */
		    orderTravelAjax.setIntermodalId(null);
		    orderTravelAjax.setIntermodalType(null);
		    orderTravelAjax.setIdCard(traveler.getIdCard());
		    orderTravelAjax.setDelFlag(Context.TRAVELER_DELFLAG_NORMAL); //新生成的游客状态为正常
		    //orderTravelAjax.set(newproid+"");//新产品id
		    orderTravelAjax.setPersonType(traveler.getPersonType());
		   // orderTravelAjax.setDelFlag(traveler.getDelFlag());
		    List<Traveler> travelerList = new ArrayList<Traveler>();
		    travelerList.add(orderTravelAjax);
		    Long currency_id = newPro.getCurrency_id();
		    List<MoneyAmount> moneyAmounts=new ArrayList<MoneyAmount>();
				MoneyAmount ma= new MoneyAmount();
					ma.setCurrencyId(Integer.valueOf(currency_id.toString()));
					switch (traveler.getPersonType().toString()) {
					case "1":
						ma.setAmount(newPro.getSettlementAdultPrice());
						break;
					case "2":
						ma.setAmount(newPro.getSettlementcChildPrice());
						break;
					case "3":
						ma.setAmount(newPro.getSettlementSpecialPrice());
					break;

					default:
						break;
					}
					
					ma.setMoneyType(Context.MONEY_TYPE_YSH);
					ma.setOrderType(Context.ORDER_TYPE_JP);
					ma.setBusindessType(MoneyAmount.BUSINDESSTYPE_DD);
					moneyAmounts.add(ma);		
			AirticketPreOrderService airticketPreOrderService = SpringContextHolder.getBean("airticketPreOrderService");
			if(airticketOrderNew.getPlaceHolderType()==null){
				airticketOrderNew.setPlaceHolderType(AirticketOrder.PLACEHOLDERTYPE_ZW);
			}
			String payType= order.getOriginalFrontMoney();
			String temp="";
			if(airticket.getPayMode_full()==1&&payType.equals("1")){
				airticketOrderNew.setOriginalFrontMoney("1");
			}
			if(airticket.getPayMode_op()==1&&payType.equals("1")){
				airticketOrderNew.setOriginalFrontMoney("1");
			}
			if(airticket.getPayMode_cw()==1&&payType.equals("1")){
				airticketOrderNew.setOriginalFrontMoney("1");
			}
			if(airticket.getPayMode_advance()==1&&payType.equals("2")){
				airticketOrderNew.setOriginalFrontMoney("2");
			}
			if(airticket.getPayMode_deposit()==1&&payType.equals("3")){
				airticketOrderNew.setOriginalFrontMoney("3");
			}
			if(airticketOrderNew.getOriginalFrontMoney()==null){
				if(airticket.getPayMode_advance()==1){
					airticketOrderNew.setOriginalFrontMoney("2");
				}
				if(airticket.getPayMode_full()==1){
					airticketOrderNew.setOriginalFrontMoney("1");
				}
				if(airticket.getPayMode_op()==1 || airticket.getPayMode_cw()==1){
					airticketOrderNew.setOriginalFrontMoney("1");
				}
				if(airticket.getPayMode_deposit()==1){
					airticketOrderNew.setOriginalFrontMoney("3");
				}
			}
			User u = UserUtils.getUser(order.getCreateBy().getId());
			airticketOrderNew.setCreateBy(u);
			airticketOrderNew.setUpdateBy(u);
			// 设定新订单为预占位
			airticketOrderNew.setOccupyType(Integer.parseInt(Context.PAY_MODE_BEFOREHAND));
			setOrderNum(order, traveler);
	    	// 保存新产品
	    	activityAirTicketService.save(newPro);
	    	// 保存新订单
	    	airticketOrderNew = airticketPreOrderService.saveAirticketOrder(newPro, airticketOrderNew,contactsList,travelerList, moneyAmounts, null ,reviewId);
	}
	
	
	/**
	 * @Description 扣减订单人数和对应游客类型人数
	 * @author yakun.bai
	 * @Date 2015-12-3
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	private void setOrderNum(AirticketOrder airticketOrder, Traveler traveler) {
		 /** 扣减订单人数 */
        int personType = traveler.getPersonType();
        switch(personType){
        	case Context.PERSON_TYPE_ADULT:
        		airticketOrder.setAdultNum(airticketOrder.getAdultNum() - 1);
        	break;
        	case Context.PERSON_TYPE_CHILD:
        		airticketOrder.setChildNum(airticketOrder.getChildNum() - 1);
        	break;
        	case Context.PERSON_TYPE_SPECIAL:
        		airticketOrder.setSpecialNum(airticketOrder.getSpecialNum() - 1);
        	break;
        	default:
        		airticketOrder.setAdultNum(airticketOrder.getAdultNum() - 1);
            break;
        }
        airticketOrder.setPersonNum(airticketOrder.getPersonNum() - 1);
//        airticketPreOrderDao.save(airticketOrder);
        airticketPreOrderDao.saveObj(airticketOrder);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
	public Map<String, Object> queryApprovalDetailTravel(HttpServletRequest request, HttpServletResponse response,String reviewId,String orderId) {
//		ReviewDetailDao rd = SpringContextHolder.getBean("reviewDetailDao");
//		List<ReviewDetail> reviewDetaillist = rd.findReviewDetail(Long.valueOf(reviewId));
		List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
		Map<String, Object> mapPrecess =  processReviewService.getReviewDetailMapByReviewId(reviewId);
	    Long newproid=0l;
	    listMap.add(mapPrecess);
	    for(Map<String, Object> map : listMap){
	    	if(map.containsKey("newProId")){
	    		 newproid= Long.parseLong(map.get("newProId").toString());
	    	}
	    }
	    ActivityAirTicket newPro = airticketOrderDao.queryOneActivityAirTicket(newproid);//新产品
	    Map map = airticketChangeDao.queryApprovalDetailTravel(request, response, reviewId);
	    
	    CurrencyService cs = SpringContextHolder.getBean("currencyService");
	    com.trekiz.admin.modules.sys.entity.Currency c = cs.findCurrency(Long.valueOf(newPro.getCurrency_id()));
	    
	    map.put("BZ", c.getCurrencyName());
	    map.put("JE", newPro.getSettlementAdultPrice());
	    
		return map;
	}
	
}
