package com.trekiz.admin.modules.order.airticket.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.engine.repository.ReviewNewDao;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.mapper.BeanMapper;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.activity.entity.IntermodalStrategy;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.IntermodalStrategyService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.repository.AgentinfoDao;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.eprice.service.EstimatePriceRecordService;
import com.trekiz.admin.modules.geography.dao.SysGeographyDao;
import com.trekiz.admin.modules.geography.entity.SysGeography;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.airticket.repository.AirticketPreOrderDao;
import com.trekiz.admin.modules.order.airticket.repository.IAirticketPreOrderDao;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.OrderContactsDao;
import com.trekiz.admin.modules.order.repository.OrderpayDao;
import com.trekiz.admin.modules.order.service.AirticketOrderStockService;
import com.trekiz.admin.modules.order.service.OrderContactsService;
import com.trekiz.admin.modules.order.service.OrderPayMoreService;
import com.trekiz.admin.modules.order.service.OrderStatisticsService;
import com.trekiz.admin.modules.statisticAnalysis.common.OrderDataStatistics;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao;
import com.trekiz.admin.modules.stock.entity.AirticketActivityReserve;
import com.trekiz.admin.modules.stock.repository.AirticketActivityReserveDao;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.SysIncrease;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.UserDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;

@Service
public class AirticketPreOrderService extends BaseService implements
		IAirticketPreOrderService {
	@Autowired
	private AirticketPreOrderDao airticketOrderDao;
	@Autowired
	private OrderContactsDao orderContactsDao;
	@Autowired
	private TravelerDao travelerDao;
	@Autowired
	private IAirticketPreOrderDao airticketPreOrderDaoImpl;
	@Autowired
	private AirticketActivityReserveDao airticketActivityReserveDao;
	@Autowired
	private OrderpayDao orderpayDao;
	@Autowired
	private ActivityAirTicketDao airTicketDao;
	@Autowired
	private MoneyAmountDao moneyAmountDao;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private SysIncreaseService sysIncreaseService;
	@Autowired
	private EstimatePriceRecordService recordService;
	@Autowired
	private IAirticketOrderDao airticketOrder;
	@Autowired
	private OrderPayMoreService moreService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private OrderContactsService orderContactsService;
	@Autowired
	private AirticketOrderStockService airticketOrderStockService;
	@Autowired
	private OrderStatisticsService orderStatisticsService;
	@Autowired
    private OrderStatisticsService orderStatusticsService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private AgentinfoDao agentinfoDao;
	@Autowired
	private SysGeographyDao sysGeographyDao;
	@Autowired
	private ReviewNewDao reviewDao;
	@Autowired
	private OrderDateSaveOrUpdateDao orderDateSaveOrUpdateDao;
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public AirticketOrder saveAirticketOrder(ActivityAirTicket activity,
			AirticketOrder order, List<OrderContacts> contactsList,
			List<Traveler> travelerList,List<MoneyAmount> moneyAmounts, JSONArray rebatesObjects ,String reviewId) throws Exception {
		AirticketOrder returnOrder = order;
		Long activityId = activity.getId();
		
		airticketOrderStockService.changeGroupFreeNum(activity, returnOrder, null, Context.StockOpType.CREATE);
		
		String qkserialNum=UUID.randomUUID().toString();//全款流水号
		String djserialNum=UUID.randomUUID().toString();//订金流水号
		String cbjserialNum = UUID.randomUUID().toString();//成本价流水号
		String originalSerialNum = UUID.randomUUID().toString();//原始应收价流水号
		String originalDJSerialNum = UUID.randomUUID().toString();//原始订金流水号
		returnOrder.setGroupCode(activity.getGroupCode());
		returnOrder.setFrontMoney(djserialNum);
		returnOrder.setTotalMoney(qkserialNum);
		returnOrder.setCostTotalMoney(cbjserialNum);
		returnOrder.setOriginalTotalMoney(originalSerialNum);
		returnOrder.setOriginalFrontMoney(originalDJSerialNum);
		// 0434需求开始
		returnOrder.setSeenFlag(0);
		// 0434需求结束
		returnOrder = airticketOrderDao.save(returnOrder);
		
		// 0434需求开始
		// 根据审核主表id查询审核主表数据
		if(reviewId != null){
			ReviewNew reviewNew = reviewDao.findById(reviewId);
			if (reviewNew.getIdLong() == null) {
				String sql = "select MIN(id_long) from review_new";
				List<Integer> minVal = travelerDao.findBySql(sql.toString());
				Long minIdVal = null;
				if (CollectionUtils.isNotEmpty(minVal)) {
					minIdVal = minVal.get(0).longValue() - 1;
				} else {
					minIdVal = 0L;
				}
				reviewNew.setIdLong(minIdVal);
			}
			reviewNew.setExtend1(returnOrder.getId().toString());
			reviewDao.save(reviewNew);
		}
		// 0434需求结束
		
		// 保存联系人信息
		saveContactsList(returnOrder, contactsList);
		//保存游客
		saveTraveler(returnOrder.getId(),travelerList,activity.getCurrency_id(),activityId, rebatesObjects);
		//保存订金应收金额
		saveOrderDjMoney(returnOrder,activity,djserialNum,travelerList);
		//保存原始订金应收金额
		saveOrderDjOriginalMoney(returnOrder,activity,originalDJSerialNum);
		List<MoneyAmount> ysList = new ArrayList<MoneyAmount>();
		List<MoneyAmount> cbList = new ArrayList<MoneyAmount>();
		List<MoneyAmount> ysysList = new ArrayList<MoneyAmount>();
		MoneyAmount m;
		for(MoneyAmount ys : moneyAmounts){
			if(ys.getMoneyType() == Context.MONEY_TYPE_YSH){
				m = new MoneyAmount(ys);
				ysList.add(m);
				ys.setMoneyType(Context.MONEY_TYPE_YSYSH);
				m = new MoneyAmount(ys);
				ysysList.add(m);
			}
			if(ys.getMoneyType() == Context.MONEY_TYPE_CBJ){
				m = new MoneyAmount(ys);
				cbList.add(m);
			}
		}
		//保存订单应收金额
		saveOrderTotalMoney(returnOrder,ysList,qkserialNum);
		//保存订单成本价
		saveOrderCostMoney(returnOrder,cbList,cbjserialNum);
		//保存订单原始应收金额
		saveOrderOriginalMoney(returnOrder,ysysList,originalSerialNum);
		//调用询价接口
		if(activity.getRecordId()!=null){
			recordService.releaseProduct(activity.getRecordId(),"7");
		}
		if(activity.getRecordId()!=null){
			
			EstimatePriceRecordService es = SpringContextHolder.getBean("estimatePriceRecordService");
			es.releaseOrder(activity.getRecordId());
		}
		
//		//-------by------junhao.zhao-----2017-01-09-----主要通过airticket_order向表order_data_statistics中添加数据---开始-------------------------------------------------------------
//		// 向表order_data_statistics中添加数据
//		Long airticketId = returnOrder.getId();
//		OrderDataStatistics ods = new OrderDataStatistics();
//						
//		ods.setUuid(UUID.randomUUID().toString());
//		//数据库不能为空值，初始化数据
//		
//		if(returnOrder.getId()!=null && StringUtils.isNotBlank(returnOrder.getId().toString())){
//			ods.setOrderId(returnOrder.getId());
//		}else{
//			ods.setOrderId(Long.valueOf(0));
//		}
//		
//		if(returnOrder.getAirticketId()!=null && StringUtils.isNotBlank(returnOrder.getAirticketId().toString())){
//			ods.setProductId(returnOrder.getAirticketId());
//		}else{
//			ods.setProductId(Long.valueOf(0));
//		}
//		
//		if(returnOrder.getProductTypeId()!=null && StringUtils.isNotBlank(returnOrder.getProductTypeId().toString())){
//			ods.setOrderType(Integer.valueOf(returnOrder.getProductTypeId().toString()));
//		}else{
//			ods.setOrderType(0);
//		}
//		
//		
//		if(returnOrder.getTotalMoney()!=null && StringUtils.isNotBlank(returnOrder.getTotalMoney().toString())){
//			ods.setAmountUuid(returnOrder.getTotalMoney());
//		}else{
//			ods.setAmountUuid("");
//		}
//		
//		
//		ods.setOrderPersonNum(returnOrder.getPersonNum());
//		
//		
//		if(returnOrder.getCreateDate()!=null && StringUtils.isNotBlank(returnOrder.getCreateDate().toString())){
//			ods.setOrderCreatetime(returnOrder.getCreateDate());
//		}else{
//			ods.setOrderCreatetime(new Date());
//		}
//		
//		if(returnOrder.getAgentinfoId()!=null && StringUtils.isNotBlank(returnOrder.getAgentinfoId().toString())){
//			ods.setAgentinfoId(returnOrder.getAgentinfoId());
//		}else{
//			ods.setAgentinfoId(Long.valueOf(0));
//		}
//		
//		if(returnOrder.getNagentName()!=null && StringUtils.isNotBlank(returnOrder.getNagentName().toString())){
//			ods.setAgentinfoName(returnOrder.getNagentName());
//		}else{
//			ods.setAgentinfoName("");
//		}
//		
//		if(returnOrder.getSalerId()!=null && StringUtils.isNotBlank(returnOrder.getSalerId().toString())){
//			ods.setSalerId(returnOrder.getSalerId());
//		}else{
//			ods.setSalerId(0);
//		}
//		
//		if(returnOrder.getSalerName()!=null && StringUtils.isNotBlank(returnOrder.getSalerName().toString())){
//			ods.setSalerName(returnOrder.getSalerName());
//		}else{
//			ods.setSalerName("");
//		}
//		
//		ods.setCreateDate(new Date());
//		
//		if(returnOrder.getOrderState()!=null && StringUtils.isNotBlank(returnOrder.getOrderState().toString())){
//			ods.setOrderStatus(returnOrder.getOrderState());
//		}else{
//			ods.setOrderStatus(0);
//		}
//		
//		if(returnOrder.getDelFlag()!=null && StringUtils.isNotBlank(returnOrder.getDelFlag().toString())){
//			ods.setDelFlag(returnOrder.getDelFlag());
//		}else{
//			ods.setDelFlag("");
//		}
//							
//		//为了向表order_data_statistics中添加数据，需要根据airticket_order从四张表查询UUID()、product_name、company_uuid、amount
//		List<Map<String, Object>> addDate = orderDateSaveOrUpdateDao.getAirticketAddDate(airticketId);
//		for(Map<String,Object> mapKey : addDate){
//					
//			if(mapKey.get("UUID()")!= null && StringUtils.isNotBlank(mapKey.get("UUID()").toString())){
//				ods.setUuid(mapKey.get("UUID()").toString());
//			}
//			
//			if(mapKey.get("product_name")!= null && StringUtils.isNotBlank(mapKey.get("product_name").toString())){
//				ods.setProductName(mapKey.get("product_name").toString());
//			}else{
//				ods.setProductName("");
//			}
//			
//			if(mapKey.get("company_uuid")!= null && StringUtils.isNotBlank(mapKey.get("company_uuid").toString())){
//				ods.setCompanyUuid(mapKey.get("company_uuid").toString());
//			}else{
//				ods.setCompanyUuid("");
//			}
//			
//			if(mapKey.get("amount")!= null && StringUtils.isNotBlank(mapKey.get("amount").toString())){
//				ods.setAmount(new BigDecimal(mapKey.get("amount").toString()));
//			}else{
//				ods.setAmount(new BigDecimal("0"));
//			}
//		}
//				
//		orderDateSaveOrUpdateDao.saveObj(ods);
//						
//		//-------by------junhao.zhao-----2017-01-09-----主要通过airticket_order向表order_data_statistics中添加数据---结束-----------------------------------
//		
		return returnOrder;
	}

	/**
	 * 保存联系人信息
	 * 
	 * @param order
	 * @param contactsList
	 */
	private void saveContactsList(AirticketOrder order, List<OrderContacts> contactsList) {
		
		// 如果联系人不为空，则保存
		if (CollectionUtils.isNotEmpty(contactsList)) {

			// 删除订单原有联系人
			orderContactsDao.deleteOrderContactsByOrderIdAndOrderType(order.getId(), Context.ORDER_TYPE_JP);

			for (OrderContacts contacts : contactsList) {
				if (contacts.getId() != null) {
					OrderContacts loadcontact = orderContactsDao.findOne(contacts.getId());
					try {
						BeanMapper.copy(contacts, loadcontact);
						contacts = loadcontact;
					} catch (Exception e) {
						throw e;
					}
				}
				if (order.getId() != contacts.getOrderId()) {
					OrderContacts temp = new OrderContacts();
					BeanMapper.copy(contacts, temp);
					temp.setId(null);
					temp.setOrderId(order.getId());
					temp.setOrderType(Context.ORDER_TYPE_JP);
					temp.setAgentId(order.getAgentinfoId());
					orderContactsDao.save(temp);
				} else {
					contacts.setOrderId(order.getId());
					contacts.setOrderType(Context.ORDER_TYPE_JP);
					contacts.setAgentId(order.getAgentinfoId());
					orderContactsDao.save(contacts);
				}
			}
		}
	}
	
//	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	private Traveler saveTraveler(Long orderId, List<Traveler> travelerList,Long currencyid,Long inId, JSONArray rebatesObjects) {
//		if (null != orderId) {
//			travelerDao.deleteTravelerByOrderId(orderId);
//		}
		Currency cu=currencyService.findCurrency(currencyid);
//		IntermodalStrategyService is = SpringContextHolder.getBean("intermodalStrategyService");
//		List<IntermodalStrategy> list = is.getActivityIntermodalStrategies(inId);
		
		Traveler rTraveler = null;
//		MoneyAmount ma=null;
//		MoneyAmount ma1 = null;
		AirticketOrder airticketOrder = airticketOrderDao.findOne(orderId);
		String serialNum=null;
		String serialNumOriginal = null;
		int count = 0;
		for (Traveler traveler : travelerList) {
			if (traveler.getId() == null) {
				rTraveler = traveler;
			} else {
				rTraveler = travelerDao.findOne(traveler.getId());
				updateTravelerAttr(traveler, rTraveler);
			}
//			if(list.size()>0){
//				rTraveler.setIntermodalId(list.get(0).getId());
//			}
			rTraveler.setDelFlag(0);
			rTraveler.setOrderId(orderId);
			rTraveler.setOrderType(Context.ORDER_TYPE_JP);//机票类型
			rTraveler.setIsAirticketFlag("1");//1表示占票
			rTraveler.setSrcPriceCurrency(cu);//币种
			//游客结算价
			serialNum=UUID.randomUUID().toString();
			//游客原始应收价
			serialNumOriginal=UUID.randomUUID().toString();
			/**
			 * 新订单游客结算价
			 * by sy 二〇一五年十二月十日 17:04:19
			 */
			if(null != traveler.getPayPriceSerialNum()){
				rTraveler.setPayPriceSerialNum(traveler.getPayPriceSerialNum());
			}else{
				rTraveler.setPayPriceSerialNum(serialNum);
			}
			/**
			 * 新订单游客结算价
			 * by sy 二〇一五年十二月十日 17:04:19
			 */
			rTraveler.setOriginalPayPriceSerialNum(serialNumOriginal);
			rTraveler = travelerDao.save(rTraveler);
			
			List<String> list = rTraveler.getCostsettlementPrice();
			if(list != null && list.size()>0){
				for(String m : list){
					String[] ms = m.split(":");
					String curId = ms[0];
					String curMoney = ms[1];
					MoneyAmount tempM = new MoneyAmount();
					tempM.setAmount(new BigDecimal(curMoney.replaceAll(",", "")));
					tempM.setCurrencyId(Integer.parseInt(curId));
					tempM.setOrderType(Context.ORDER_TYPE_JP);
					tempM.setBusindessType(MoneyAmount.BUSINDESSTYPE_YK);
					tempM.setMoneyType(Context.MONEY_TYPE_JSJ);
					tempM.setUid(rTraveler.getId());
					tempM.setSerialNum(serialNum);
					moneyAmountService.saveOrUpdateMoneyAmount(tempM);
					tempM = new MoneyAmount();
					tempM.setAmount(new BigDecimal(curMoney.replaceAll(",", "")));
					tempM.setCurrencyId(Integer.parseInt(curId));
					tempM.setOrderType(Context.ORDER_TYPE_JP);
					tempM.setBusindessType(MoneyAmount.BUSINDESSTYPE_YK);
					tempM.setMoneyType(Context.MONEY_TYPE_YSJSJ);
					tempM.setUid(rTraveler.getId());
					tempM.setSerialNum(serialNumOriginal);
					moneyAmountService.saveOrUpdateMoneyAmount(tempM);
					
				}
			}
			
			MoneyAmount ma2 = new MoneyAmount();
		//	MoneyAmount ma3 = new MoneyAmount();
			MoneyAmount ma4 = new MoneyAmount();
		//	MoneyAmount ma5 = new MoneyAmount();
			
			
		    ActivityAirTicket activityAirTicket  = ((ActivityAirTicketDao)SpringContextHolder.getBean("activityAirTicketDao")).findOne(airticketOrder.getAirticketId());
		    activityAirTicket.getIstax();//是否含税
			if(activityAirTicket.getIstax()==1){
				//税费
				ma4.setAmount(activityAirTicket.getTaxamt());
				ma4.setCurrencyId(Integer.parseInt(activityAirTicket.getCurrency_id().toString()));
				ma4.setSerialNum(UUID.randomUUID().toString());
				ma4.setMoneyType(Context.MONEY_TYPE_SF);
				ma4.setOrderType(Context.ORDER_TYPE_JP);
				ma4.setBusindessType(MoneyAmount.BUSINDESSTYPE_YK);
				ma4.setUid(rTraveler.getId());
				
//				ma5.setAmount(activityAirTicket.getTaxamt());
//				ma5.setCurrencyId(Integer.parseInt(activityAirTicket.getCurrency_id().toString()));
//				ma5.setSerialNum(UUID.randomUUID().toString());
//				ma5.setMoneyType(Context.MONEY_TYPE_SF);
//				ma5.setOrderType(Context.ORDER_TYPE_JP);
//				ma5.setBusindessType(MoneyAmount.BUSINDESSTYPE_YK);
//				ma5.setUid(rTraveler.getId());
				
			}
			
			
			IntermodalStrategyService intermodalStrategyService = SpringContextHolder.getBean("intermodalStrategyService");
			if(rTraveler.getIntermodalId()!=null){
				IntermodalStrategy in= intermodalStrategyService.getOne(rTraveler.getIntermodalId());
				ma2.setAmount(in.getPrice());
				ma2.setCurrencyId(Integer.parseInt(in.getPriceCurrency().getId()+""));
				ma2.setSerialNum(UUID.randomUUID().toString());
				ma2.setMoneyType(Context.MONEY_TYPE_LYF);
				ma2.setOrderType(Context.ORDER_TYPE_JP);
				ma2.setBusindessType(MoneyAmount.BUSINDESSTYPE_YK);
				ma2.setUid(rTraveler.getId());
				
//				ma3.setAmount(in.getPrice());
//				ma3.setCurrencyId(Integer.parseInt(in.getPriceCurrency().getId()+""));
//				ma3.setSerialNum(UUID.randomUUID().toString());
//				ma3.setMoneyType(Context.MONEY_TYPE_LYF);
//				ma3.setOrderType(Context.ORDER_TYPE_JP);
//				ma3.setBusindessType(MoneyAmount.BUSINDESSTYPE_YK);
//				ma3.setUid(rTraveler.getId());
			}
			
			
			moneyAmountService.saveOrUpdateMoneyAmount(ma2);
			moneyAmountService.saveOrUpdateMoneyAmount(ma4);
			//返佣 add by jiangyang 2015年8月3日			
			if(rebatesObjects != null && rebatesObjects.getJSONObject(count) != null){
				String rebatesMoney = rebatesObjects.getJSONObject(count).getString("rebatesMoney");
				if(StringUtil.isNotBlank(rebatesMoney)){					
					String rebatesSerialNum = UUID.randomUUID().toString();
					MoneyAmount rebatesMA = new MoneyAmount();
					rebatesMA.setAmount(new BigDecimal(rebatesMoney));
					rebatesMA.setCurrencyId(Integer.parseInt(rebatesObjects.getJSONObject(count).getString("rebatesCurrencyId")));
					rebatesMA.setOrderType(Context.ORDER_TYPE_JP);
					rebatesMA.setBusindessType(MoneyAmount.BUSINDESSTYPE_YK);
					rebatesMA.setMoneyType(Context.MONEY_TYPE_FY);
					rebatesMA.setUid(rTraveler.getId());					
					rebatesMA.setSerialNum(rebatesSerialNum);
					moneyAmountService.saveOrUpdateMoneyAmount(rebatesMA);
					travelerDao.updateRebatesMoneySerialNumByTravelerId(rebatesSerialNum,rTraveler.getId());	//根据游客Id修改游客返佣费用流水号					
				}
				count++;
			}			
		}
		return rTraveler;
	}

	/**
	 * 更新游客属性
	 * @param traveler
	 * @param targetTraveler
	 */
	private void updateTravelerAttr(Traveler traveler, Traveler targetTraveler) {
		targetTraveler.setBirthDay(traveler.getBirthDay());
		targetTraveler.setIdCard(traveler.getIdCard());
		targetTraveler.setIntermodalId(traveler.getIntermodalId());
		targetTraveler.setName(traveler.getName());
		targetTraveler.setNameSpell(traveler.getNameSpell());
		targetTraveler.setNationality(traveler.getNationality());
		targetTraveler.setOrderId(traveler.getOrderId());
		targetTraveler.setPassportCode(traveler.getPassportCode());
		targetTraveler.setPassportValidity(traveler.getPassportValidity());
		targetTraveler.setPersonType(traveler.getPersonType());
		targetTraveler.setSex(traveler.getSex());
		targetTraveler.setRemark(traveler.getRemark());
		targetTraveler.setTelephone(traveler.getTelephone());
		targetTraveler.setValidityDate(traveler.getValidityDate());
	}

	/**
	 * 保存订金应收款
	 * @param order
	 * @param moneyAmounts 多币种
	 * @param serialNum 流水uuid
	 */
	private void saveOrderDjMoney(AirticketOrder order,ActivityAirTicket activity,String serialNum,List<Traveler> travelerList) {
		MoneyAmount ma=new MoneyAmount();
		if(travelerList.size()>0){
			ma.setAmount(activity.getDepositamt().multiply(new BigDecimal(travelerList.size())));
		}else{
			ma.setAmount(activity.getDepositamt().multiply(new BigDecimal(order.getPersonNum())));
		}
		//ma.setAmount(activity.getDepositamt().multiply(new BigDecimal(travelerList.size())));
		
		ma.setBusindessType(MoneyAmount.BUSINDESSTYPE_DD);
		ma.setCurrencyId(Integer.valueOf(activity.getCurrency_id().toString()));
		ma.setMoneyType(Context.MONEY_TYPE_DJ);
		ma.setOrderType(Context.ORDER_TYPE_JP);
		ma.setUid(order.getId());
		ma.setSerialNum(serialNum);
		moneyAmountService.saveOrUpdateMoneyAmount(ma);
	}
	
	private void saveOrderDjOriginalMoney(AirticketOrder order,ActivityAirTicket activity,String serialNum) {
		MoneyAmount ma=new MoneyAmount();
		ma.setAmount(activity.getDepositamt());
		ma.setBusindessType(MoneyAmount.BUSINDESSTYPE_DD);
		ma.setCurrencyId(Integer.valueOf(activity.getCurrency_id().toString()));
		ma.setMoneyType(Context.MONEY_TYPE_YSDJ);
		ma.setOrderType(Context.ORDER_TYPE_JP);
		ma.setUid(order.getId());
		ma.setSerialNum(serialNum);
		moneyAmountService.saveOrUpdateMoneyAmount(ma);
	}
	
	//保存订单成本价
	
	private void saveOrderCostMoney(AirticketOrder order,List<MoneyAmount> moneyAmounts,String serialNum) {
		
		for (MoneyAmount ma : moneyAmounts) {
			MoneyAmount tempMa = new MoneyAmount();
			tempMa.setAmount(ma.getAmount());
			tempMa.setBusindessType(ma.getBusindessType());
			tempMa.setCreatedBy(ma.getCreatedBy());
			tempMa.setCreateTime(ma.getCreateTime());
			tempMa.setCurrencyId(ma.getCurrencyId());
			tempMa.setMoneyType(ma.getMoneyType());
			tempMa.setSerialNum(serialNum);
			tempMa.setUid(order.getId());
			tempMa.setOrderType(ma.getOrderType());
			moneyAmountService.saveOrUpdateMoneyAmount(tempMa);
		}
		
	}
	
	
	/**
	 * 保存订单总金额应收款
	 * @param order
	 * @param moneyAmounts 多币种
	 * @param serialNum 流水uuid
	 */
	private void saveOrderTotalMoney(AirticketOrder order,List<MoneyAmount> moneyAmounts,String serialNum) {

		for (MoneyAmount ma : moneyAmounts) {
			MoneyAmount tempMa = new MoneyAmount();
			tempMa.setAmount(ma.getAmount());
			tempMa.setBusindessType(ma.getBusindessType());
			tempMa.setCreatedBy(ma.getCreatedBy());
			tempMa.setCreateTime(ma.getCreateTime());
			tempMa.setCurrencyId(ma.getCurrencyId());
			tempMa.setMoneyType(ma.getMoneyType());
			tempMa.setSerialNum(serialNum);
			tempMa.setUid(order.getId());
			tempMa.setOrderType(ma.getOrderType());
			moneyAmountService.saveOrUpdateMoneyAmount(tempMa);
		}
	}
	private void saveOrderOriginalMoney(AirticketOrder order,List<MoneyAmount> moneyAmounts,String serialNum){
		for (MoneyAmount ma : moneyAmounts) {
			MoneyAmount tempMa = new MoneyAmount();
			tempMa.setAmount(ma.getAmount());
			tempMa.setBusindessType(ma.getBusindessType());
			tempMa.setCreatedBy(ma.getCreatedBy());
			tempMa.setCreateTime(ma.getCreateTime());
			tempMa.setCurrencyId(ma.getCurrencyId());
			tempMa.setMoneyType(Context.MONEY_TYPE_YSDJ);
			tempMa.setSerialNum(serialNum);
			tempMa.setUid(order.getId());
			tempMa.setOrderType(ma.getOrderType());
			moneyAmountService.saveOrUpdateMoneyAmount(tempMa);
		}
	}
	

	/**
	 * @Description 查询此渠道对应切位数
	 * @author yakun.bai
	 * @Date 2015-11-3
	 */
	@Override
	public AirticketActivityReserve queryAirticketActivityReserve(Long activityId, Long agentId) {
		return airticketActivityReserveDao.findAgentReserve(activityId, agentId);
	}

	@Override
	public void deleteTraveler(Long travelerId) {
		this.travelerDao.delete(travelerId);
	}

	/** 
	 * 轮训服务，查询单办机票、预订方式为预占位方式、占位订单，并且没有支付的机票订单(前100个)
	 * 查看该订单的保留天数，如果该订单过期则退换机票余位并且取消该订单
	 */
	@Override
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	public void scheduledAirticketOrderService() {
		//1-单办 2-参团
		//预定方式 1-全款 2-定金占位 3-预占位
		List<Map<String,Object>> listmap=airticketPreOrderDaoImpl.getOrderListByNoPay(AirticketOrder.TYPE_DB, Integer.parseInt(Context.ORDER_PAYSTATUS_YZW) ,100);
		Date currentDate=new Date();//当前时间
		for(Map<String,Object> entity:listmap){
			Integer remainDays=entity.get("remaind_days")!=null?Integer.valueOf(entity.get("remaind_days").toString()):null;//保留天数
			Date createDate=entity.get("create_date")!=null?(Date)entity.get("create_date"):null;//下单时间
			//String createDate=entity.get("orderCreateTime");//下单时间
			if(remainDays!=null&&createDate!=null){
				int rd=Integer.valueOf(remainDays);
				Date dt=createDate;
				//rdDate下单日期+保留天数
				Date rdDate=new Date(dt.getTime() + rd * 24 * 60 * 60 * 1000);
				if(rdDate.before(currentDate)){//如果rdDate小于当前时间，说明保留天数已过期，那么改变订单状态
					String orderid=entity.get("id").toString();
					if(StringUtils.isNotEmpty(orderid)){
						try {
							returnFreePostion(Long.valueOf(orderid));
						} catch (NumberFormatException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	/**
	 * 返还余位
	 */
	@Transactional(readOnly=false, rollbackFor=Exception.class)
	private void returnFreePostion(Long orderid){
		//修改状态 
		airticketPreOrderDaoImpl.updateOrder(orderid, String.valueOf(Context.CANCEL_STATUS));
		
		AirticketOrder order = airticketOrderDao.findOne(orderid);
		ActivityAirTicket airticket=airTicketDao.findOne(order.getAirticketId());
		
		//获取有票的游客 此处用游客票数防止有退票问题
		List<Traveler> travelers=travelerDao.getTicketTravelersByOrderIdAndOrderType(orderid, Context.ORDER_TYPE_JP,1);
		int positionNum=travelers.size();//占位数
		if(positionNum>0){//归还余位
			int feePosition=airticket.getFreePosition()+positionNum;//余位+人数
			int nopayReservePosition=airticket.getNopayReservePosition()-positionNum;//各渠道总的占位人数-人数
			int soldNopayPosition=airticket.getSoldNopayPosition()-positionNum;//已支付占位人数-人数
			if(feePosition>=0&&nopayReservePosition>=0&&soldNopayPosition>=0)
				airTicketDao.updateAirTicketFreePosition(airticket.getId(),feePosition,nopayReservePosition,soldNopayPosition);
		}
	}
	
	/**
	 * 机票生成子订单调用接口
	 * airticketId 机票产品id
	 * po 产品订单
	 * */
	public AirticketOrder createAirticketOrder(Long airticketId,ProductOrderCommon po) throws Exception {
		ActivityAirTicket activity =airTicketDao.findOne(airticketId);
		AirticketOrder returnOrder = new AirticketOrder();
		String companyName = po.getOrderCompanyName();
		if (companyName == null) {
			companyName = "";
		}
		String orderNum = sysIncreaseService.updateSysIncrease(companyName
				.length() > 3 ? companyName.substring(0, 3) : companyName,
				activity.getProCompany(), null, Context.ORDER_NUM_TYPE);
//		String GroupNo = sysIncreaseService.updateSysIncrease(companyName
//				.length() > 3 ? companyName.substring(0, 3) : companyName,
//				activity.getProCompany(), null, Context.GROUP_NUM_TYPE);
		//修改机票订单团号修改
		ActivityGroupService activityGroupService = SpringContextHolder.getBean("activityGroupService");
//		List<OrderContacts> orderContacts = orderContactsService.findOrderContactsByOrderIdAndOrderType(po.getId(), Context.ORDER_TYPE_DT);
		String GroupNo = activityGroupService.getGroupNumForTTS(activity.getDeptId().toString(), null);
		returnOrder.setOrderNo(orderNum);
		returnOrder.setGroupCode(GroupNo);
		returnOrder.setOrderState(po.getPayStatus());
		returnOrder.setType(AirticketOrder.TYPE_CT);//参团类型
		returnOrder.setAdultNum(Integer.valueOf(po.getOrderPersonNumAdult()));
		returnOrder.setChildNum(Integer.valueOf(po.getOrderPersonNumChild()));
		returnOrder.setSpecialNum(Integer.valueOf(po.getOrderPersonNumSpecial()));
		returnOrder.setPersonNum(po.getOrderPersonNum());
		returnOrder.setMainOrderId(po.getId());
		returnOrder.setAirticketId(airticketId);
		returnOrder.setAgentinfoId(po.getOrderCompany());
		returnOrder.setSalerId(po.getSalerId());
		returnOrder.setSalerName(po.getSalerName());
		returnOrder.setPlaceHolderType(0);
		
		UUID sid = UUID.randomUUID();
		UUID sid2 = UUID.randomUUID();
		UUID sid3 = UUID.randomUUID();
		
		returnOrder.setTotalMoney(sid.toString());
		returnOrder.setOriginalTotalMoney(sid2.toString());
		returnOrder.setCostTotalMoney(sid3.toString());
		
		airticketOrderStockService.changeGroupFreeNum(activity, returnOrder, returnOrder.getPersonNum(), Context.StockOpType.CW_CONFIRM);
		
		returnOrder = airticketOrderDao.save(returnOrder);
		int anum = returnOrder.getAdultNum();
		int cnum = returnOrder.getChildNum();
		int snum = returnOrder.getSpecialNum();
		java.math.BigDecimal total=new  java.math.BigDecimal(0);
		if(anum>0){
			java.math.BigDecimal aprice = activity.getSettlementAdultPrice();
			java.math.BigDecimal atotal =aprice.multiply(new java.math.BigDecimal(anum));
			total = total.add(atotal);
		}
		if(cnum>0){
			java.math.BigDecimal aprice = activity.getSettlementcChildPrice();
			java.math.BigDecimal atotal =aprice.multiply(new java.math.BigDecimal(cnum));
			total = total.add(atotal);
		}
		if(snum>0){
			java.math.BigDecimal aprice = activity.getSettlementSpecialPrice();
			java.math.BigDecimal atotal =aprice.multiply(new java.math.BigDecimal(snum));
			total = total.add(atotal);
		}
		
		MoneyAmount moneyAmountYS = new MoneyAmount();
		moneyAmountYS.setSerialNum(sid.toString());
		moneyAmountYS.setCurrencyId(Integer.valueOf(activity.getCurrency_id().toString()));
		moneyAmountYS.setUid(returnOrder.getId());
		moneyAmountYS.setAmount(total);
		moneyAmountYS.setOrderType(7);
		moneyAmountYS.setBusindessType(1);
		moneyAmountYS.setMoneyType(Context.MONEY_TYPE_YSH);
		moneyAmountYS.setCreatedBy(UserUtils.getUser().getId());
		moneyAmountYS.setCreateTime(new Date());
		moneyAmountService.saveOrUpdateMoneyAmount(moneyAmountYS);
		
		MoneyAmount moneyAmountYSYSH = new MoneyAmount();
		moneyAmountYSYSH.setSerialNum(sid.toString());
		moneyAmountYSYSH.setCurrencyId(Integer.valueOf(activity.getCurrency_id().toString()));
		moneyAmountYSYSH.setUid(returnOrder.getId());
		moneyAmountYSYSH.setAmount(total);
		moneyAmountYSYSH.setOrderType(7);
		moneyAmountYSYSH.setBusindessType(1);
		moneyAmountYSYSH.setMoneyType(Context.MONEY_TYPE_YSYSH);
		moneyAmountYS.setCreatedBy(UserUtils.getUser().getId());
		moneyAmountYSYSH.setCreateTime(new Date());
		moneyAmountYSYSH.setSerialNum(sid2.toString());
		moneyAmountService.saveOrUpdateMoneyAmount(moneyAmountYSYSH);
		
		MoneyAmount moneyAmountCBJ = new MoneyAmount();
		moneyAmountCBJ.setSerialNum(sid.toString());
		moneyAmountCBJ.setCurrencyId(Integer.valueOf(activity.getCurrency_id().toString()));
		moneyAmountCBJ.setUid(returnOrder.getId());
		moneyAmountCBJ.setAmount(total);
		moneyAmountCBJ.setOrderType(7);
		moneyAmountCBJ.setBusindessType(1);
		moneyAmountCBJ.setMoneyType(Context.MONEY_TYPE_CBJ);
		moneyAmountYS.setCreatedBy(UserUtils.getUser().getId());
		moneyAmountCBJ.setCreateTime(new Date());
		moneyAmountCBJ.setSerialNum(sid2.toString());
		moneyAmountService.saveOrUpdateMoneyAmount(moneyAmountCBJ);
		// 保存联系人信息
//		saveContactsList(returnOrder, orderContacts);
		return returnOrder;
	}
	
	/**
	 * 返还机票子订单余位
	 * productOrderId 主的id
	 * personNum 退票人数
	 * update by ruyi.chen 
	 * update date 2015 01-22
	 * update describe  增加主订单无机票子订单判断，更新机票产品余位(未更新机票子订单游客信息)
	 * */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public boolean returnFreePosionByProductOrderId(Long productOrderId,Integer personNum){
		AirticketOrder airticketOrder=airticketOrderDao.findByProductOrderId(productOrderId);//获取机票子订单
		//TODO 更新机票子订单游客信息(待定)
		if(null != airticketOrder && airticketOrder.getId() > 0){
			ActivityAirTicket airticket=airTicketDao.findOne(airticketOrder.getAirticketId());//机票产品
			if(null != airticket && airticket.getId() > 0){
				//修改余位
				airticket.setFreePosition(airticket.getFreePosition()+personNum);
				airTicketDao.save(airticket);
			}
			
		}
		
		return true;
	}
	
	

	/**
	 * 机票驳回
	 * Long orderid airticketOrder主键 
	 * Long orderPayId orderPay主键
	 * Boolean flag 是否保持占位（否就归还）
	 * */
	
	@Override
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public boolean airticketRejected(Long orderid, Long orderPayId,Boolean flag,String reason) {
		AirticketOrder order =	airticketOrder.getAirticketOrderById(orderid);
		Orderpay orderpay = orderpayDao.getOrderPayById(orderPayId);
		List<MoneyAmount> payedMoney = moneyAmountDao.findAmountBySerialNum(orderpay.getMoneySerialNum());
		List<MoneyAmount> orderpayed = moneyAmountDao.findAmountBySerialNum(order.getPayedMoney());
		//更新机票订单已付
		for (int i = 0; i < payedMoney.size();i++) {
			for (int j = 0; j < orderpayed.size(); j++) {
				if((payedMoney.get(i).getCurrencyId()).equals(orderpayed.get(j).getCurrencyId())){
					BigDecimal om= orderpayed.get(j).getAmount();
					BigDecimal pm= payedMoney.get(i).getAmount();
					BigDecimal val= om.subtract(pm);
					orderpayed.get(j).setAmount(val);
					moneyAmountDao.updateOrderForAmount(orderpayed.get(j).getId(),val);
					continue;
				}
			}
//			break;
		}
		//归还
		if(flag==false){
			ActivityAirTicket activity = airTicketDao.findOne(order.getAirticketId());
			try {
				Map<String,String> pMap = orderStatisticsService.saveAirTicketActivityPlaceHolderChange(
						order.getId(), Context.ORDER_STATUS_AIR_TICKET, order.getPersonNum(), activity);
				if (null != pMap && Context.ORDER_PLACEHOLDER_ERROR.toString().equals( pMap.get(Context.RESULT))) {
        			throw new Exception(Context.MESSAGE);
        		}
        		//余位处理成功
        		else if (null != pMap && Context.ORDER_PLACEHOLDER_YES.toString().equals( pMap.get(Context.RESULT))) {
        			
        		} else {
        			throw new Exception("归还余位失败！");
        		}
			} catch (Exception e) {
				e.printStackTrace();
			}
			order.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_YQX));
		}
		//更新支付流水表状态
		moreService.rejectConfirmOper(orderpay.getId().toString(),"1",reason);
		return true;
	}
	
	/**
	 * 机票撤销
	 * Long orderid airticketOrder主键 
	 * Long orderPayId orderPay主键
	 * Boolean flag 是否保持占位（否就归还）
	 * @throws Exception 
	 * @throws PositionOutOfBoundException 
	 * @throws OptimisticLockHandleException 
	 * */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public boolean airticketOrderCancel(Long orderid, Long orderPayId) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
		AirticketOrder order =	airticketOrder.getAirticketOrderById(orderid);
		Orderpay orderpay = orderpayDao.getOrderPayById(orderPayId);
		List<MoneyAmount> payedMoney = moneyAmountDao.findAmountBySerialNum(orderpay.getMoneySerialNum());
		List<MoneyAmount> orderpayed = moneyAmountDao.findAmountBySerialNum(order.getAccountedMoney());
		//更新机票订单已付
		boolean flag = true;
		for (int i = 0; i < payedMoney.size();i++) {
			for (int j = 0; j < orderpayed.size(); j++) {
				if((payedMoney.get(i).getCurrencyId()).equals(orderpayed.get(j).getCurrencyId())){
					BigDecimal om = orderpayed.get(j).getAmount();
					BigDecimal pm = payedMoney.get(i).getAmount();
					BigDecimal val = om.subtract(pm);
					orderpayed.get(j).setAmount(val);
					moneyAmountDao.updateOrderForAmount(orderpayed.get(j).getId(),val);
					if (val != null && val.doubleValue() == 0.00) {
						moneyAmountService.deleteById(orderpayed.get(j).getId());
					} else {
						flag = false;
					}
					continue;
				}
				
			}
//			break;
		}
		if (flag && order.getOccupyType() == Integer.parseInt(Context.ORDER_PAYSTATUS_CW)) {
			revokeOrder(order.getId());
			order.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_CW));
			airticketPreOrderDaoImpl.saveObj(order);
		}
		//更新支付流水表状态
		moreService.cancelConfirmOper(orderpay.getId().toString(),"1");
		return true;
	}
	
	
	private String revokeOrder(Long orderId) throws Exception {
    	
    	AirticketOrder order = airticketOrderDao.findOne(orderId);
        if(order == null){
            return "fail";
        }
        ActivityAirTicket activity = airTicketDao.findOne(order.getAirticketId());
    	
    	//判断是否占位
        Map<String,String> rMap = orderStatusticsService.getPlaceHolderInfo(order.getId(), Context.ORDER_STATUS_AIR_TICKET);
        if (null != rMap && null != rMap.get(Context.RESULT)) {
        	String resultP = rMap.get(Context.RESULT);
        	if (Context.ORDER_PLACEHOLDER_YES.toString().equals(resultP)) {
        		//当订单占位时归还余位
        		Map<String,String> pMap = orderStatusticsService.saveAirTicketActivityPlaceHolderChange(
        				order.getId(), Context.ORDER_STATUS_AIR_TICKET, order.getPersonNum(), activity);
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
        	throw new Exception("撤销订单失败！");
        }
        order.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_CW_CX));
        airticketOrderDao.save(order);
        return "ok";
    }
	
	
	

	
	public List<ActivityAirTicket> findYwByActivityIds(List<Long> activityIdList) {
		return airticketOrderDao.findYwByActivityIds(activityIdList);
	}
	
	/**
	 * 传入产品编号或订单编号
	 * @author hhx
	 * @return
	 */
	public String genOrderNo(Integer serinum){
		Office office = UserUtils.getUser().getCompany();
		String companyName = office.getName();
		if(companyName.length()>3){
			companyName = companyName.substring(0, 3);
		}
		return sysIncreaseService.updateSysIncrease(companyName,office.getId(), null, serinum);
	}
	
	public String genGroupCode(AirticketOrder order){
		//重新加载用户数据，避免存在缓存问题
		User user = userDao.findById(UserUtils.getUser().getId());
		ActivityAirTicket activityAirTicket = airTicketDao.getById(order.getAirticketId());
		//机票缩写
		StringBuffer groupCode = new StringBuffer("JP");
		
		//团号的流水号
		SysIncrease increase = sysIncreaseService.findNextGroupNum(user.getCompany().getId(), Context.GROUP_NUM_TYPE);
		Integer codeNum = increase.getCodeNum();
		if(codeNum == 9999) {
			codeNum = 0;
		}
		groupCode.append(String.format("%04d", codeNum+1));
		increase.setCodeNum(codeNum+1);
		airticketOrderDao.getSession().saveOrUpdate(increase);
		
		//线路国家的简称
		SysGeography country = sysGeographyDao.getByUuid(activityAirTicket.getCountry());
		if(StringUtils.isNotEmpty(country.getNameShortEn())) {
			groupCode.append(country.getNameShortEn());
		}

		//预定渠道的简称
		//签约渠道直接取渠道维护的简称
		if(order.getAgentinfoId() != null && order.getAgentinfoId() != -1) {
			Agentinfo agentinfo = agentinfoDao.getById(order.getAgentinfoId());
			if(StringUtils.isNotEmpty(agentinfo.getAgentNameShort())) {
				groupCode.append(agentinfo.getAgentNameShort());
			}
		} else {
			//非签约渠道时直接调用订单提供的渠道简写
			groupCode.append(order.getNagentcode());
		}
		
		//员工号
		if(StringUtils.isNotEmpty(user.getNo())) {
			groupCode.append(user.getNo());
		}
		//出团日期
		groupCode.append(DateUtils.date2String(activityAirTicket.getStartingDate(), "yyMMdd"));
		
		
		return groupCode.toString();
	}
	
}