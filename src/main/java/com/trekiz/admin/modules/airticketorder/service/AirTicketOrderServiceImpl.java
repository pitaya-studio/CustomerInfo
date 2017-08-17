package com.trekiz.admin.modules.airticketorder.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import com.google.common.collect.Sets;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticket.service.ActivityAirTicketServiceImpl;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.entity.OrderAgentAjax;
import com.trekiz.admin.modules.airticketorder.entity.OrderTravelAjax;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.airticket.repository.AirticketPreOrderDao;
import com.trekiz.admin.modules.order.airticket.service.AirticketPreOrderService;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.repository.OrderContactsDao;
import com.trekiz.admin.modules.order.repository.OrderpayDao;
import com.trekiz.admin.modules.order.service.AirticketOrderStockService;
import com.trekiz.admin.modules.order.service.OrderContactsService;
import com.trekiz.admin.modules.order.service.OrderPayService;
import com.trekiz.admin.modules.order.service.OrderStatisticsService;
import com.trekiz.admin.modules.reviewflow.entity.ReviewDetail;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDetailDao;
import com.trekiz.admin.modules.statisticAnalysis.home.dao.OrderDateSaveOrUpdateDao;
import com.trekiz.admin.modules.stock.entity.AirticketActivityReserve;
import com.trekiz.admin.modules.stock.repository.AirticketActivityReserveDao;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.repository.CurrencyDao;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;

@Service
@Transactional(readOnly = true)
public class AirTicketOrderServiceImpl implements IAirTicketOrderService{
	
	/**
	 * 特殊人群
	 */
	@SuppressWarnings("unused")
    private static final String PERSON_TYPE_SPECIAL = "3";

	/**
	 * 儿童
	 */
	@SuppressWarnings("unused")
    private static final String PERSON_TYPE_CHILD = "2";

	/**
	 * 成人
	 */
	@SuppressWarnings("unused")
    private static final String PERSON_TYPE_ADULT = "1";

	@SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(AirTicketOrderServiceImpl.class);
	
	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	
	@Autowired
    private AirticketPreOrderDao airticketPreOrderDao;
	
	@Autowired
    private AirticketActivityReserveDao airticketActivityReserveDao;
	
	@Autowired
    private ActivityAirTicketDao airTicketDao;
	
	@Autowired
    private DocInfoDao docInfoDao;
	
	@Autowired
	private OrderpayDao orderpayDao;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Autowired
	private OrderStatisticsService orderStatisticsService;
	
	@Autowired
	private CurrencyDao currencyDao;
	
	@Autowired 
	private TravelerDao travelerDao;
	
	@Autowired
    private OrderStatisticsService orderStatusticsService;
	@Autowired
	private AirticketOrderStockService airticketOrderStockService;
	@Autowired
	private OrderContactsService orderContactsService;
	@Autowired
	CurrencyService currencyService;
	@Autowired
	OrderPayService orderPayService;
	@Autowired
	IActivityAirTicketService activityAirTicketService;

	@Autowired
	private MoneyAmountDao moneyAmountDao;
	@Autowired
	private OrderContactsDao orderContactsDao;
	@Autowired
    private OrderDateSaveOrUpdateDao orderDateSaveOrUpdateDao;
	

	/**
	 * 根据条件查询订单列表
	 */
	@Override
	public Page<Map<String, Object>> queryAirticketOrderListByCond(HttpServletRequest request, HttpServletResponse response, User userInfo, Map<String, Object> condMap, Map<String, Object> roleMap) {
		
		//1.组织where条件
		String whereSql = organizeWhereSql(userInfo, roleMap);
		
		//2.查询机票订单信息
		Page<Map<String,Object>> orderPage = airticketOrderDao.queryAirticketOrderListByCond(request,response,whereSql,condMap);
		
		//3.组织航段数据
		orderPage = organizeAirticketOrderFlightsData(orderPage,request,response);
		
		return orderPage;
	}

	private Page<Map<String, Object>> organizeAirticketOrderFlightsData(Page<Map<String, Object>> orderPage,HttpServletRequest request, HttpServletResponse response) {
		
		List<Map<String, Object>> airticketOrderList = orderPage.getList();
		
		if(airticketOrderList == null || airticketOrderList.size()<1) return new Page<Map<String,Object>>(request, response);
		
		for(Map<String,Object> airticketOrderMap : airticketOrderList){
			
			//机票产品id
			Integer airticketId = (Integer)airticketOrderMap.get("airticketId");
			Integer orderId = (Integer) airticketOrderMap.get("id");
			//航段信息
			List<Map<String, Object>> airticketOrderFlights = airticketOrderDao.queryAirticketOrderFlights(airticketId);
			//附件信息
			List<Map<String,Object>> airticketOrderAttachment = airticketOrderDao.queryAirticketOrderAttachment(orderId);
			//返佣信息 add start by jiangyang 2015-8-4
			String orderNo = (String) airticketOrderMap.get("orderNo");
			List<Map<String, Object>> airticketOrderRebatesList = new ArrayList<Map<String,Object>>();
			airticketOrderRebatesList.addAll(airticketOrderDao.queryAirticketOrderRebates(orderNo));
			airticketOrderRebatesList.addAll(airticketOrderDao.queryAirticketOrderRebatesInf(orderNo));
			airticketOrderMap.put("airticketOrderRebatesList", airticketOrderRebatesList);
			//返佣信息 add end   by jiangyang 2015-8-4
			
			airticketOrderMap.put("airticketOrderFlights", airticketOrderFlights);
			airticketOrderMap.put("airticketOrderAttachmentId", airticketOrderAttachment.size()>0?airticketOrderAttachment.get(0).get("attachmentId"):null);
		}
		
		orderPage.setList(airticketOrderList);
		
		return orderPage;
	}

	private String organizeWhereSql(User userInfo, Map<String, Object> roleMap) {
		
		String whereSql = "";
		
		//管理员 批发商下的所有订单
		if( roleMap.containsKey(Context.ROLE_TYPE_MANAGER)){
			whereSql = "OR ao.create_by IN (SELECT id FROM sys_user WHERE companyId = (SELECT companyId FROM sys_user WHERE id = "+userInfo.getId()+"))";
		}
		//销售主管
		else if(roleMap.containsKey(Context.ROLE_TYPE_SALES_EXECUTIVE)){
			
			//部门
			Department deptInfo = (Department) roleMap.get(Context.ROLE_TYPE_SALES_EXECUTIVE);
			
			//没有部门,查询该批发商所有的订单
			if(deptInfo==null){
				
				whereSql = "OR (ao.create_by IN (SELECT id FROM sys_user WHERE companyId = (" +
						"SELECT companyId FROM sys_user WHERE id = " + userInfo.getId() + "))" +
								" OR ao.salerId = " + UserUtils.getUser().getId() + ")";
			}
			//有部门,查询该部门下的所有的销售的订单
			else if(deptInfo!=null){
				
				whereSql = "OR (ao.create_by in( SELECT id FROM sys_user u LEFT JOIN sys_user_role r ON u.id = r.userId " +
						"WHERE r.roleId IN ( SELECT id FROM sys_role WHERE deptId = " + deptInfo.getId() + " AND roleType in (1,2)))" +
								" OR ao.salerId = " + UserUtils.getUser().getId() + ")";
			}
		}
		//计调主管
		else if (roleMap.containsKey(Context.ROLE_TYPE_OP_EXECUTIVE)) {
			
			//部门
			Department deptInfo = (Department) roleMap.get(Context.ROLE_TYPE_OP_EXECUTIVE);
			
			//没有部门,查询该批发商所有的订单
			if (deptInfo == null) {
				
				whereSql = "OR (ao.create_by IN (SELECT id FROM sys_user WHERE companyId = (" +
						"SELECT companyId FROM sys_user WHERE id = " + userInfo.getId() + "))" +
								" OR ao.salerId = " + UserUtils.getUser().getId() + ")";
			}
			//有部门,查询该部门下的所有的计调发的产品产生的订单
			else if (deptInfo != null) {
				whereSql = "OR (aa.createBy IN ( SELECT id FROM sys_user u LEFT JOIN sys_user_role r ON u.id = r.userId " +
						"WHERE r.roleId IN ( SELECT id FROM sys_role WHERE deptId = " + deptInfo.getId() + " AND roleType in (3,4)))" +
								" OR ao.salerId = " + UserUtils.getUser().getId() + ")";
			}
		}
		//销售
		else if (roleMap.containsKey(Context.ROLE_TYPE_SALES)) {
			whereSql = "OR (ao.create_by IN (" + userInfo.getId() + ") OR ao.salerId = " + UserUtils.getUser().getId() + ")";
		}
		//计调
		else if (roleMap.containsKey(Context.ROLE_TYPE_OP)) {
			whereSql = "OR (aa.createBy IN (" + userInfo.getId() + ") OR ao.salerId = " + UserUtils.getUser().getId() + ")";
		}
		
//		//开发票情况   0 全部   1未开    2  已开  
//        String invoiceStatus = map.get("invoiceStatus");
//        if (StringUtils.isNotBlank(invoiceStatus)) {
//        	if("1".equals(invoiceStatus)){
//        		whereSql.append(" and not exists(select 1 from orderinvoice oi where pro.id=oi.orderId " +
//                        " and oi.verifyStatus in(0,1))");
//        	}
//        	if("2".equals(invoiceStatus)){
//        		whereSql.append(" and exists(select 1 from orderinvoice oi where pro.id=oi.orderId " +
//                        " and oi.verifyStatus in(0,1))");
//        	}
//        }
//      //开收据情况   0 全部   1未开    2  已开  
//        String receiptStatus = map.get("receiptStatus");
//        if (StringUtils.isNotBlank(receiptStatus)) {
//        	if("1".equals(receiptStatus)){
//        		whereSql.append(" and not exists(select 1 from orderreceipt oi where pro.id=oi.orderId " +
//                        " and oi.verifyStatus in(0,1)and oi.orderType ="+ orderType+  ")");
//        	}
//        	if("2".equals(receiptStatus)){
//        		whereSql.append(" and exists(select 1 from orderreceipt oi where pro.id=oi.orderId " +
//        				" and oi.verifyStatus in(0,1)and oi.orderType ="+ orderType+  ")");
//        	}
//        }
			
		return whereSql;
	}
	
	
	
	/**
	 * 根据订单id查询机票订单详情内容
	 * 退款
	 */
	@SuppressWarnings("unchecked")
    public Map<String,Object> queryAirticketOrderDetailInfoByIdRefund(String orderId){
		Map<String, Object> orderDetailInfoMap = airticketOrderDao.queryAirticketOrderDetailInfoByIdNewRefund(orderId);
		List<Map<String,Object>> orderPayInfoList = (List<Map<String,Object>>)orderDetailInfoMap.get("orderPayInfoList");
		// 循环支付信息并给加上对应的支付凭证列表信息
		for(Map<String,Object> temp : orderPayInfoList){
			String payVoucher = temp.get("payVoucher") == null ? null : temp.get("payVoucher").toString();
			if(payVoucher == null || "".equals(payVoucher) || "null".equals(payVoucher)){
				continue;
			}
			String[] payVouchers = payVoucher.split(",");
			DocInfo docInfo = new DocInfo();
			List<DocInfo> listDocinfo = new ArrayList<DocInfo>();
			for(String tempPay : payVouchers){
				docInfo = docInfoDao.findOne(Long.parseLong(tempPay));
				listDocinfo.add(docInfo);
			}
			temp.put("docList", listDocinfo);
		}
		orderDetailInfoMap.remove("orderPayInfoList");
		orderDetailInfoMap.put("orderPayInfoList", orderPayInfoList);
		//处理游客结算价 
		List<Map<String,Object>> travelerInfoList = (List<Map<String,Object>>)orderDetailInfoMap.get("travelInfoList");
		
		if (CollectionUtils.isNotEmpty(travelerInfoList)) {
			Iterator<Map<String,Object>> travelerList = travelerInfoList.iterator();
			while (travelerList.hasNext()) {
				Map<String,Object> traveler = travelerList.next();
				String paySerialNum = "";
				if (null != traveler.get("traPayPrice")) {
					paySerialNum = traveler.get("traPayPrice").toString();
				}
				traveler.remove("traPayPrice");
				traveler.put("traPayPrice", moneyAmountService.getMoney(paySerialNum));
			}
		}
		orderDetailInfoMap.remove("travelInfoList");
		orderDetailInfoMap.put("travelInfoList", travelerInfoList);

		return orderDetailInfoMap;
	}
	
	
	
	/**
	 * 根据订单id查询机票订单详情内容
	 */
	@SuppressWarnings("unchecked")
    @Override
	public Map<String,Object> queryAirticketOrderDetailInfoById(String orderId){
		Map<String, Object> orderDetailInfoMap = airticketOrderDao.queryAirticketOrderDetailInfoByIdNew(orderId);
		List<Map<String,Object>> orderPayInfoList = (List<Map<String,Object>>)orderDetailInfoMap.get("orderPayInfoList");
		// 循环支付信息并给加上对应的支付凭证列表信息
		for(Map<String,Object> temp : orderPayInfoList){
			String payVoucher = temp.get("payVoucher") == null ? null : temp.get("payVoucher").toString();
			if(payVoucher == null || "".equals(payVoucher) || "null".equals(payVoucher)){
				continue;
			}
			String[] payVouchers = payVoucher.split(",");
			DocInfo docInfo = new DocInfo();
			List<DocInfo> listDocinfo = new ArrayList<DocInfo>();
			for(String tempPay : payVouchers){
				docInfo = docInfoDao.findOne(Long.parseLong(tempPay));
				listDocinfo.add(docInfo);
			}
			temp.put("docList", listDocinfo);
		}
		orderDetailInfoMap.remove("orderPayInfoList");
		orderDetailInfoMap.put("orderPayInfoList", orderPayInfoList);
		//处理游客结算价 
		List<Map<String,Object>> travelerInfoList = (List<Map<String,Object>>)orderDetailInfoMap.get("travelInfoList");
		
		if (CollectionUtils.isNotEmpty(travelerInfoList)) {
			Iterator<Map<String,Object>> travelerList = travelerInfoList.iterator();
			while (travelerList.hasNext()) {
				Map<String,Object> traveler = travelerList.next();
				String paySerialNum = "";
				if (null != traveler.get("traPayPrice")) {
					paySerialNum = traveler.get("traPayPrice").toString();
				}
				traveler.remove("traPayPrice");
				traveler.put("traPayPrice", moneyAmountService.getMoney(paySerialNum));
			}
		}
		orderDetailInfoMap.remove("travelInfoList");
		orderDetailInfoMap.put("travelInfoList", travelerInfoList);

		return orderDetailInfoMap;
	}
	
	/**
	 * 根据订单id查询机票订单详情内容
	 * by sy 20150908
	 */
	@SuppressWarnings("unchecked")
    @Override
	public Map<String,Object> queryAirticketOrderDetailInfoByIdAddcontacts(String orderId){
		Map<String, Object> orderDetailInfoMap = airticketOrderDao.queryAirticketOrderDetailInfoByIdAddcontact(orderId);		
		List<Map<String,Object>> orderPayInfoList = (List<Map<String,Object>>)orderDetailInfoMap.get("orderPayInfoList");
		// 循环支付信息并给加上对应的支付凭证列表信息
		for(Map<String,Object> temp : orderPayInfoList){
			String payVoucher = temp.get("payVoucher") == null ? null : temp.get("payVoucher").toString();
			if(payVoucher == null || "".equals(payVoucher) || "null".equals(payVoucher)){
				continue;
			}
			String[] payVouchers = payVoucher.split(",");
			DocInfo docInfo = new DocInfo();
			List<DocInfo> listDocinfo = new ArrayList<DocInfo>();
			for(String tempPay : payVouchers){
				docInfo = docInfoDao.findOne(Long.parseLong(tempPay));
				listDocinfo.add(docInfo);
			}
			temp.put("docList", listDocinfo);
		}
		orderDetailInfoMap.remove("orderPayInfoList");
		orderDetailInfoMap.put("orderPayInfoList", orderPayInfoList);
		//处理游客结算价 
		List<Map<String,Object>> travelerInfoList = (List<Map<String,Object>>)orderDetailInfoMap.get("travelInfoList");
		for(Map<String,Object> tra : travelerInfoList){
			String paySerialNum = tra.get("traPayPrice").toString();
			tra.remove("traPayPrice");
			tra.put("traPayPrice", moneyAmountService.getMoney(paySerialNum));
		}
		orderDetailInfoMap.remove("travelInfoList");
		orderDetailInfoMap.put("travelInfoList", travelerInfoList);
		//如果salerName为空，则从用户表中获取
		if (orderDetailInfoMap.get("salerName") == null || StringUtils.isBlank(orderDetailInfoMap.get("salerName").toString())) {
			if (orderDetailInfoMap.get("saler") != null) {				
				orderDetailInfoMap.put("salerName", UserUtils.getUser(orderDetailInfoMap.get("saler").toString()));
			}
		}
		return orderDetailInfoMap;
	}
	
	
	@Override
	public Map<String,Object> queryAirticketOrderDetailInfoById(String orderId,String travelId){
		Map<String, Object> orderDetailInfoMap = airticketOrderDao.queryAirticketOrderDetailInfoById(orderId,travelId);
		return orderDetailInfoMap;
	}
	
	
	/**
	 * 更新机票订单游客信息
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public boolean updateAirticketOrderTravel(OrderTravelAjax orderTravelAjax) {
		
		Integer count = airticketOrderDao.updateAirticketOrderTravel(orderTravelAjax);
		//更新款项信息
		Traveler traveler = travelerDao.findById(Long.parseLong(orderTravelAjax.getTravelId()));
		//获取上个sql的自增长id
		String lastInsertId = queryLastInsertId();
		//MoneyAmount对象填充值
		List<MoneyAmount> moneyAmount = fillMoneyAmount(orderTravelAjax,traveler.getPayPriceSerialNum(), null,lastInsertId);
		moneyAmountService.saveOrUpdateMoneyAmounts(traveler.getPayPriceSerialNum(),moneyAmount);
		return count==0?false:true;
	}
	
	/**
	 * 保存机票订单游客信息
	 * @throws ParseException 
	 * @throws NumberFormatException 
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public boolean saveAirticketOrderTravel(OrderTravelAjax orderTravelAjax) throws NumberFormatException, ParseException {
		
		//1.生成一个uuid
		String payPriceSerialNum = UUID.randomUUID().toString();
		orderTravelAjax.setPayPriceSerialNum(payPriceSerialNum);
		
		//2.游客信息和uuid写入游客表
		Integer count = airticketOrderDao.saveAirticketOrderTravel(orderTravelAjax);
		
		//获取上个sql的自增长id
		String lastInsertId = queryLastInsertId();
		
		//3.游客的结算价和uuid写入money_amount表
		MoneyAmountService moneyAmountService = SpringContextHolder.getBean("moneyAmountService");
		ActivityAirTicketServiceImpl activityAirTicketService = SpringContextHolder.getBean("activityAirTicketServiceImpl");
		
		ActivityAirTicket activityAirTicket = activityAirTicketService.getActivityAirTicketById(Long.parseLong(orderTravelAjax.getAirticketId()));
		
		//MoneyAmount对象填充值
		List<MoneyAmount> moneyAmount = fillMoneyAmount(orderTravelAjax,payPriceSerialNum, activityAirTicket,lastInsertId);
		for(MoneyAmount temp : moneyAmount){
			moneyAmountService.saveOrUpdateMoneyAmount(temp);
		}
		
		return count==0?false:true;
	}

	private List<MoneyAmount> fillMoneyAmount(OrderTravelAjax orderTravelAjax,String payPriceSerialNum, ActivityAirTicket activityAirTicket, String lastInsertId) {
		
		List<MoneyAmount> moneyAmounts = new ArrayList<MoneyAmount>();
		MoneyAmount moneyAmount; 
		String payPrice = orderTravelAjax.getPayPrice();//游客结算价字符串
		
		String[] payPrices = payPrice.split("[+]");
		List<Currency> currencyList = currencyDao.findListByCompanyId(UserUtils.getCompanyIdForData());//该公司的币种信息
		for(String tempMoney : payPrices){
			moneyAmount = new MoneyAmount();
			int nIndex = tempMoney.indexOf("-");
			String curMark = "";
			String curValue = "";
			if(nIndex == -1){
//				Pattern pattern = Pattern.compile("\\d+");  
//			    Matcher matcher = pattern.matcher(tempMoney);  
//			    if (matcher.find()) {  
//			    	curValue = matcher.group(0); 
//			    	curMark = tempMoney.split(matcher.group(0))[0];
//			    } 
				char[] charArr = tempMoney.toCharArray();
				int numIndex = 0;
				for(int n = 0;n < charArr.length; n++){
					if(Character.isDigit(charArr[n])){
						numIndex = n;
						break;
					}
				}
				curMark = tempMoney.substring(0, numIndex);
				curValue = tempMoney.substring(numIndex);
			} else {
				curMark = tempMoney.substring(0, nIndex);
				curValue = tempMoney.substring(nIndex);
			}
			if(curMark == null || "".equals(curMark)){
				return null;
			}
			for(Currency cur : currencyList){
				if(curMark.equals(cur.getCurrencyMark())){
					//币种
					moneyAmount.setCurrencyId(cur.getId().intValue());
					break;
				}
			}
			//金额
			moneyAmount.setAmount(BigDecimal.valueOf(Double.parseDouble(curValue.replace(",",""))));
			//业务类型 1-订单 2-游客
			moneyAmount.setBusindessType(2);
			//创建人id
			moneyAmount.setCreatedBy(UserUtils.getUser().getId());
			
			//创建时间
			moneyAmount.setCreateTime(new Date());
			
			//款项类型
			moneyAmount.setMoneyType(Context.MONEY_TYPE_JSJ);
			
			//机票类型
			moneyAmount.setOrderType(Context.ORDER_TYPE_JP);
			
			//uuid
			moneyAmount.setSerialNum(payPriceSerialNum);
			
			//游客id
			moneyAmount.setUid(Long.parseLong(lastInsertId));
			moneyAmounts.add(moneyAmount);
		}
		return moneyAmounts;
	}
	
	@Override
	public String queryLastInsertId() {
		List<Object> lastInsertIds = airticketOrderDao.queryLastInsertId();
		if(lastInsertIds == null || lastInsertIds.size()<=0) return "";
		Object lastInsertId = lastInsertIds.get(0);
		return String.valueOf(lastInsertId);
	}
	
	/**
	 * 查询机票订单最大游客id
	 */
	@Override
	public String queryAirticketOrderTravelMaxId(OrderTravelAjax orderTravelAjax) {
		List<Map<String, Object>> travelInfoList = airticketOrderDao.queryAirticketOrderTravelMaxId(orderTravelAjax);
		if(travelInfoList == null || travelInfoList.size()<=0) return "";
		Map<String, Object> travelInfo = travelInfoList.get(0);
		
		return String.valueOf((Integer)travelInfo.get("travelId"));
	}

	
	/**
	 * 查询机票订单的渠道信息
	 */
	@Override
	public Map<String, Object> queryAirticketOrderAgent(String agentId) {
		List<Map<String, Object>> agentInfoList = airticketOrderDao.queryAirticketOrderAgent(agentId);
		if(agentInfoList == null || agentInfoList.size()<1) return new HashMap<String,Object>();
		return agentInfoList.get(0);
	}
	
	/**
	 * 更新机票订单的渠道信息
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public boolean updateAirticketOrderAgent(OrderAgentAjax orderAgentAjax) {
		//1.更新航班信息
		boolean flightFlag = airticketOrderDao.updateAirticketOrderFlightRemark(orderAgentAjax);
		//2.更新机票订单的渠道id
		boolean agentIdFlag = airticketOrderDao.updateAirticketOrderAgentId(orderAgentAjax);
		//3.更新渠道信息
		boolean agentInfoFlag = airticketOrderDao.updateAirticketOrderAgentInfo(orderAgentAjax);
		//4.更新订单应收金额信息
		String orderIdStr = orderAgentAjax.getOrderId();
		AirticketOrder airticketOrder = airticketOrderDao.getAirticketOrderById(Long.parseLong(orderIdStr));
		String totalSerialNum = airticketOrder.getTotalMoney();
		List<MoneyAmount> allMoneys = fillMoneyAmountOrder(orderAgentAjax, totalSerialNum, orderIdStr);
		boolean allMoneyFlag = moneyAmountService.saveOrUpdateMoneyAmounts(totalSerialNum, allMoneys);
		return flightFlag & agentIdFlag & agentInfoFlag && allMoneyFlag;
	}
	
	/**
	 * 订单的应收总额 更新
	 * @param orderTravelAjax
	 * @param payPriceSerialNum
	 * @param activityAirTicket
	 * @param lastInsertId
	 * @return
	 */
	private List<MoneyAmount> fillMoneyAmountOrder(OrderAgentAjax orderAgentAjax,String payPriceSerialNum, String orderId) {
		
		List<MoneyAmount> moneyAmounts = new ArrayList<MoneyAmount>();
		MoneyAmount moneyAmount;
		String payPrice = orderAgentAjax.getOrderAllP();//游客结算价字符串
		
		String[] payPrices = payPrice.split("[+]");
		List<Currency> currencyList = currencyDao.findListByCompanyId(UserUtils.getCompanyIdForData());//该公司的币种信息
		for(String tempMoney : payPrices){
			moneyAmount = new MoneyAmount();
			int nIndex = tempMoney.indexOf("-");
			String curMark = "";
			String curValue = "";
			if(nIndex == -1){
//				Pattern pattern = Pattern.compile("\\d+");  
//			    Matcher matcher = pattern.matcher(tempMoney);  
//			    if (matcher.find()) {  
//			    } 
				char[] charArr = tempMoney.toCharArray();
				int numIndex = 0;
				for(int n = 0;n < charArr.length; n++){
					if(Character.isDigit(charArr[n])){
						numIndex = n;
						break;
					}
				}
				curMark = tempMoney.substring(0, numIndex);
				curValue = tempMoney.substring(numIndex);
			} else {
				curMark = tempMoney.substring(0, nIndex);
				curValue = tempMoney.substring(nIndex);
			}
			if(curMark == null || "".equals(curMark)){
				return null;
			}
			for(Currency cur : currencyList){
				if(curMark.equals(cur.getCurrencyMark())){
					//币种
					moneyAmount.setCurrencyId(cur.getId().intValue());
					break;
				}
			}
			//金额
			moneyAmount.setAmount(BigDecimal.valueOf(Double.parseDouble(curValue.replace(",",""))));
			//业务类型 1-订单 2-游客
			moneyAmount.setBusindessType(1);
			//创建人id
			moneyAmount.setCreatedBy(UserUtils.getUser().getId());
			
			//创建时间
			moneyAmount.setCreateTime(new Date());
			
			//款项类型
			moneyAmount.setMoneyType(Context.MONEY_TYPE_YSH);
			
			//机票类型
			moneyAmount.setOrderType(Context.ORDER_TYPE_JP);
			
			//uuid
			moneyAmount.setSerialNum(payPriceSerialNum);
			
			//订单id
			moneyAmount.setUid(Long.parseLong(orderId));
			moneyAmounts.add(moneyAmount);
		}
		return moneyAmounts;
	}
	
	/**
	 * 更新航班备注
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public boolean updateAirticketOrderFlightRemark(OrderAgentAjax orderAgentAjax){
		return airticketOrderDao.updateAirticketOrderFlightRemark(orderAgentAjax);
	}
	
	/**
	 * 根据订单id查询机票订单的所有游客
	 */
	@Override
	public List<Map<String, Object>> queryAirticketOrderTravels(String orderId) {
		return airticketOrderDao.queryAirticketOrderTravel(orderId);
	}
	
	/**
	 * 根据产品id查询所有机票订单
	 */
	@Override
	public List<Map<String, Object>> queryAirticketOrdersByProductId(String productId) {
		List<Map<String, Object>> airticketOrders = airticketOrderDao.queryAirticketOrdersByProductId(productId);
		if(airticketOrders == null || airticketOrders.size()<=0) return new ArrayList<Map<String,Object>>();
		return airticketOrders;
	}
	
	/**
	 * 查询售出切位明细
	 */
	@Override
	public List<Map<String, Object>> findSoldNopayPosition(String productId) {
		List<Map<String, Object>> airticketOrders = airticketOrderDao.findSoldNopayPosition(productId);
		if(airticketOrders == null || airticketOrders.size()<=0) return new ArrayList<Map<String,Object>>();
		return airticketOrders;
	}

	@SuppressWarnings("unused")
    @Override
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public boolean saveFilePathAndRelation(String orderId,String srcFileName,String relativePath) {
		//1.保存docinfo表
		DocInfoService docInfoService = SpringContextHolder.getBean("docInfoService");
		DocInfo docInfo = new DocInfo();
		docInfo.setDocName(srcFileName);
		docInfo.setDocPath(relativePath);
		docInfo.setCreateDate(new Date());
		DocInfo saveDocInfo = docInfoService.saveDocInfo(docInfo);
		String docInfoId = queryLastInsertId();
		//2.保存doc和订单的关系
		boolean flag = airticketOrderDao.saveDocinfoAndOrderRelation(Long.parseLong(orderId),Long.parseLong(docInfoId));
		return flag;
	}

	/* (non-Javadoc)    
     * @see com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService#cancelOrder(com.trekiz.admin.modules.airticketorder.entity.AirticketOrder)    
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = { Exception.class })
	public String cancelOrder(Long orderId, String description) throws Exception {
    	
    	AirticketOrder order = airticketPreOrderDao.findOne(orderId);
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
        	throw new Exception("取消订单失败！");
        }
        order.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_YQX));
        order.setCancelDescription(description);
        airticketPreOrderDao.save(order);
        //-------by------junhao.zhao-----2017-01-09-----表order_data_statistics中order_status改为取消---开始---
//        orderDateSaveOrUpdateDao.updateOrderStatus(order.getId(), order.getOrderState(), Context.ORDER_TYPE_JP);
        
        //-------by------junhao.zhao-----2017-01-09-----表order_data_statistics中order_status改为取消 ---结束--- 
        return "ok";
    }
    
    
    /**
	 * @Description 计调撤销订单占位
	 * @author yakun.bai
	 * @Date 2015-11-16
	 */
    @Override
    @Transactional(readOnly = false, rollbackFor = { Exception.class })
	public String revokeOrder(Long orderId) throws Exception {
    	
    	AirticketOrder order = airticketPreOrderDao.findOne(orderId);
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
        airticketPreOrderDao.save(order);
        return "ok";
    }
    
    /**
     *  功能:
     *  删除订单
     */
    @Transactional(readOnly=false, rollbackFor=Exception.class)
	public String deleteOrder(Long orderId) {
    	AirticketOrder order = airticketPreOrderDao.findOne(orderId);
        if(order == null){
            return "fail";
        }
        String orderState = String.valueOf(order.getOrderState());
        String placeHolderType = String.valueOf(order.getPlaceHolderType());
        
        if(!Context.ORDER_PAYSTATUS_WZF.equals(orderState) 
                && !Context.ORDER_PAYSTATUS_DJWZF.equals(orderState) 
                && !Context.ORDER_PAYSTATUS_YZW.equals(orderState)
                && !Context.ORDER_PAYSTATUS_YQX.equals(orderState)
                && !Context.ORDER_PAYSTATUS_CW.equals(orderState)
                && !Context.ORDER_PAYSTATUS_OP.equals(orderState)
                && !(Context.PLACEHOLDERTYPE_QW.equals(placeHolderType) && Context.ORDER_PAYSTATUS_YZFDJ.equals(orderState))){
            return "订单已支付不能取消";
        }
        
        //如果订单状态从取消到删除，则不需要归还余位
        if (order.getOrderState() != null 
        		&& !Context.ORDER_PAYSTATUS_YQX.equals(order.getOrderState().toString())
        		&& !Context.ORDER_PAYSTATUS_DEL.equals(order.getOrderState().toString())) {
        	try {
				cancelOrder(order.getId(), "");
			} catch (Exception e) {
				e.printStackTrace();
				return "删除订单失败";
			}
        }
        
        order.setOrderState(Integer.valueOf(Context.ORDER_PAYSTATUS_DEL));
        airticketPreOrderDao.save(order);
        
        //-------by------junhao.zhao-----2017-01-09-----表order_data_statistics中order_status改为删除---开始---
//        orderDateSaveOrUpdateDao.updateOrderStatus(order.getId(), order.getOrderState(), Context.ORDER_TYPE_JP);
        
        //-------by------junhao.zhao-----2017-01-09-----表order_data_statistics中order_status改为删除 ---结束--- 
       
        return "ok";
	}
    
    /**
     * 确认占位
     * @param orderId
     * @param request
     * @throws Exception 
     */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public String confirmOrder(Long orderId, HttpServletRequest request) throws Exception {
		
		//查询订单、团期、产品
		AirticketOrder order = airticketPreOrderDao.findOne(orderId);
		ActivityAirTicket activity = airTicketDao.findOne(order.getAirticketId());
		
		//判断是否可以占位
		order.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_YZW));
		airticketOrderStockService.changeGroupFreeNum(activity, order, null, Context.StockOpType.JD_CONFIRM);
        airticketOrderDao.saveOrUpdateAirticketOrderById(order);
		
		return "success";
	}

    /* (non-Javadoc)    
     * @see com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService#invokeOrder(java.lang.Long)    
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = { Exception.class })
    public String invokeOrder(Long orderId) throws Exception {
    	
    	//查询订单、产品
        AirticketOrder order = airticketPreOrderDao.findOne(orderId);
        if(order == null){
            return "订单不存在";
        }
        ActivityAirTicket activity = airTicketDao.findOne(order.getAirticketId());
        
        //查询订单最后一次支付记录的支付款类型：1：全款、3：订金、2：尾款
		Integer payPriceType = null;
		List<Orderpay> orderPayList = orderpayDao.findOrderpayByOrderId(orderId, Integer.parseInt(Context.ORDER_STATUS_AIR_TICKET));
		if (CollectionUtils.isNotEmpty(orderPayList)) {
			Orderpay orderPay = orderPayList.get(0);
			payPriceType = orderPay.getPayPriceType();
		}
        
		//激活条件：支付方式为订金占位或预占位的订单：包括已取消和没有取消的
		Set<Integer> payStatusSet = Sets.newHashSet();
		payStatusSet.add(Integer.parseInt(Context.ORDER_PAYSTATUS_YQX));//已取消
		payStatusSet.add(Integer.parseInt(Context.ORDER_PAYSTATUS_DJWZF));//订金未占位
		payStatusSet.add(Integer.parseInt(Context.ORDER_PAYSTATUS_YZW));//已占位 非订金占位
        
        //订单支付状态与付款类型
  		Integer payStatus = order.getOrderState();
        
        if (payStatusSet.contains(payStatus)) {

        	airticketOrderStockService.ifCanCut(activity, order, Context.StockOpType.INVOKE);
			
			//如果订单支付状态为已取消，则需要分情况占位
			if (Integer.parseInt(Context.ORDER_PAYSTATUS_YQX) == order.getOrderState()) {
				Integer payModeType = order.getOccupyType();
				setOrderPayStatusAndActivationDate(activity, order, payModeType, payPriceType);
			} else {
				order.setActivationDate(new Date());
			}
			
			airticketPreOrderDao.invokeOrder(order.getId(), order.getRemaindDays(), order.getOrderState(),order.getActivationDate());
			//-------by------junhao.zhao-----2017-01-09-----表order_data_statistics中order_status改为激活---开始---
//	        orderDateSaveOrUpdateDao.updateOrderStatus(order.getId(), order.getOrderState(), Context.ORDER_TYPE_JP);
	        
	        //-------by------junhao.zhao-----2017-01-09-----表order_data_statistics中order_status改为激活 ---结束--- 
			
        } else {
            return "已取消订单不能再次取消";
        }
        
        return "success";
    }
    
	
	/**
	 * 设置订单支付状态
	 * @param activity
	 * @param order
	 * @param payModeType 占位方式	订金占位 1；预占位 2；全款占位 3；资料占位 4；担保占位 5；确认单占位 6；
	 * @param payPriceType 付款方式类型	全款：1；尾款 2；订金 3
	 * @param dateString
	 * @throws Exception 
	 */
	private void setOrderPayStatusAndActivationDate(ActivityAirTicket activity, AirticketOrder order, Integer payModeType,
			Integer payPriceType) throws Exception {
		
		Integer paymentType = order.getPaymentStatus();
		
		if (Context.MONEY_TYPE_DJ == payPriceType) {
			order.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_YZFDJ));//改为已支付订金
			airticketOrderStockService.changeGroupFreeNum(activity, order, null, Context.StockOpType.INVOKE);
		} else if (Context.MONEY_TYPE_WK == payPriceType || Context.MONEY_TYPE_QK == payPriceType) {
			order.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_YZF));//改为已支付全款
			airticketOrderStockService.changeGroupFreeNum(activity, order, null, Context.StockOpType.INVOKE);
		} else {
			if (Context.ORDER_PAYSTATUS_WZF.equals(payModeType.toString())) {
				order.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_WZF));
			} else if (Context.ORDER_PAYSTATUS_DJWZF.equals(payModeType.toString())) {
				order.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_DJWZF));
			} else if (Context.ORDER_PAYSTATUS_CW.equals(payModeType.toString())) {
				order.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_CW));
			} else if (Context.ORDER_PAYSTATUS_OP.equals(payModeType.toString())) {
				order.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_OP));
			} else {
				order.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_YZW));
			}
			if (Context.PLACEHOLDERTYPE_QW.equals(order.getPlaceHolderType().toString()) || Context.PAYMENT_TYPE_JS != paymentType) {
				airticketOrderStockService.changeGroupFreeNum(activity, order, null, Context.StockOpType.INVOKE);
			} else {
				if (!Context.ORDER_PAYSTATUS_WZF.equals(payModeType.toString()) 
						&& !Context.ORDER_PAYSTATUS_DJWZF.equals(payModeType.toString())
						&& !Context.ORDER_PAYSTATUS_CW.equals(payModeType.toString())
						&& !Context.ORDER_PAYSTATUS_OP.equals(payModeType.toString())) {
					airticketOrderStockService.changeGroupFreeNum(activity, order, null, Context.StockOpType.INVOKE);
				}
			}
		}
		order.setActivationDate(new Date());
	}
	
    
    /* (non-Javadoc)    
     * @see com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService#lockOrder(java.lang.Long)    
     */
    @Override
    public void lockOrder(Long orderId) {
        airticketPreOrderDao.updateOrderLockStatus(orderId, AirticketOrder.LOCKSTATUS_SD);
        
    }

    /* (non-Javadoc)    
     * @see com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService#unLockOrder(java.lang.Long)    
     */
    @Override
    public void unLockOrder(Long orderId) {
        airticketPreOrderDao.updateOrderLockStatus(orderId, AirticketOrder.LOCKSTATUS_ZC);
        
    }
	
	public List<Map<String, Object>> queryAirtickeByProcode(String procode){
		
		return airticketOrderDao.queryAirtickeByProcode(procode);
	}
	

	@SuppressWarnings({ "all", "unchecked", "unused" })
    @Override
	public Page<Map<String, Object>> queryAirtickeToDo(
			HttpServletRequest request, HttpServletResponse response,
			Map condition) {
		
		return airticketOrderDao.queryAirtickeToDo(request,response,condition);
		
	}
	
	

	@SuppressWarnings("unused")
	@Transactional(readOnly = false,rollbackFor={Exception.class})
    public void planeReview(Long orderId,Long reviewId,Long tralverId) throws Exception{
		
			// 改签前的原订单实体
			AirticketOrder order = airticketOrderDao.getAirticketOrderById(orderId);
			// 被改签的游客实体
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
			
			
			ReviewDetailDao rd = SpringContextHolder.getBean("reviewDetailDao");
			// TODO （从这里开始写创建新产品订单并按照支付类型扣减余位 ）
			// 获取审核list
		    List<ReviewDetail> reviewDetaillist = rd.findReviewDetail(reviewId);
		    ReviewDetailDao rs =	(ReviewDetailDao)SpringContextHolder.getBean("reviewDetailDao");
		    Long newproid = Long.valueOf(rs.findReviewDetailByMykey(reviewId,"newProId").get(0).getMyvalue());
		    for(ReviewDetail cell:reviewDetaillist){
		    	if(cell.getMykey().equals("newProId")){
		    		 newproid= Long.parseLong(cell.getMyvalue());
		    	}
		    }
		    ActivityAirTicket newPro = airticketOrderDao.queryOneActivityAirTicket(newproid);//新产品
		
		    Traveler traveler = airticketOrderDao.queryoneTravler(tralverId); // 获取原订单游客实体
		    traveler.setDelFlag(6);
		    TravelerDao travelerDao = SpringContextHolder.getBean("travelerDao");
		    travelerDao.updateTravelerDelFlag(new Integer(6), tralverId); // 修正原订单游客状态
		    // 获取原订单联系人
		    List<OrderContacts> contactsList =  orderContactsService.findOrderContactsByOrderIdAndOrderType(order.getId(), 7);
		    AirticketOrder airticketOrderNew = new AirticketOrder(); // 新订单实体
		    
		    airticketOrderNew.setProductTypeId(Long.valueOf(newPro.getProduct_type_id() == null ? 0 : newPro.getProduct_type_id()));
		    OfficeService  officeService = SpringContextHolder.getBean("officeService");
				String companyName = officeService.get(newPro.getProCompany()).getName();
				SysIncreaseService	sysIncreaseService = SpringContextHolder.getBean("sysIncreaseService");
				String orderNum = sysIncreaseService.updateSysIncrease(companyName
						.length() > 3 ? companyName.substring(0, 3) : companyName,
								newPro.getProCompany(), null, Context.ORDER_NUM_TYPE); // 获取新订单编号

//				String GroupNo = sysIncreaseService.updateSysIncrease(companyName
//						.length() > 3 ? companyName.substring(0, 3) : companyName,
//								newPro.getProCompany(), null, Context.GROUP_NUM_TYPE);
				ActivityGroupService activityGroupService = SpringContextHolder.getBean("activityGroupService");
				String GroupNo = activityGroupService.getGroupNumForTTS(newPro.getDeptId().toString(), null); // 为批发商环球行提供特定的团号生成规则
				airticketOrderNew.setOrderNo(orderNum);
				airticketOrderNew.setGroupCode(GroupNo);
				airticketOrderNew.setLockStatus(0);
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
		    orderTravelAjax.setIntermodalId(null);
		    orderTravelAjax.setIntermodalType(null);
		    orderTravelAjax.setIdCard(traveler.getIdCard());
		    orderTravelAjax.setDelFlag(Context.TRAVELER_DELFLAG_PLANEREVIEW);
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
			// 判断余位
			airticketOrderStockService.ifCanCut(newPro, airticketOrderNew, Context.StockOpType.CREATE);
	    	// 保存新产品
	    	activityAirTicketService.save(newPro);
	    	// 保存新订单
	    	airticketOrderNew = airticketPreOrderService.saveAirticketOrder(newPro, airticketOrderNew,contactsList,travelerList, moneyAmounts, null, reviewId.toString());
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
        airticketPreOrderDao.save(airticketOrder);
	}

	@SuppressWarnings("rawtypes")
    @Override
	public Page<Map<String, Object>> airticketApprovalHistoryList(
			HttpServletRequest request, HttpServletResponse response,
			Map condition) {
		return airticketOrderDao.airticketApprovalHistoryList(request, response, condition);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
	public Map<String, Object> queryApprovalDetailTravel(HttpServletRequest request, HttpServletResponse response,String reviewId,String orderId) {
		ReviewDetailDao rd = SpringContextHolder.getBean("reviewDetailDao");
		List<ReviewDetail> reviewDetaillist = rd.findReviewDetail(Long.valueOf(reviewId));
	    Long newproid=0l;
	    for(ReviewDetail cell:reviewDetaillist){
	    	if(cell.getMykey().equals("newProId")){
	    		 newproid= Long.parseLong(cell.getMyvalue());
	    	}
	    }
	    ActivityAirTicket newPro = airticketOrderDao.queryOneActivityAirTicket(newproid);//新产品
	    Map map = airticketOrderDao.queryApprovalDetailTravel(request, response, reviewId);
	    
	    CurrencyService cs = SpringContextHolder.getBean("currencyService");
	    com.trekiz.admin.modules.sys.entity.Currency c = cs.findCurrency(Long.valueOf(newPro.getCurrency_id()));
	    
	    map.put("BZ", c.getCurrencyName());
	    map.put("JE", newPro.getSettlementAdultPrice());
	    
		return map;
	}
	
	/**
	 * 保存备注
	 * 
	 * @param orderId
	 * @param remark
	 * @return
	 */
	@Transactional(readOnly = false,rollbackFor={Exception.class})
	public boolean saveRemark(Long orderId, String remark) {
		if (orderId == null) {
			return false;
		}

		// 修改备注
		return airticketOrderDao.updateRemark(orderId, remark);
	}
	
	/**
	 * 根据订单的id和产品类型查询对应的支付记录信息
	 * @param orderId
	 * @param travelId
	 * @return
	 */
	@Override
	public List<Orderpay> queryOrderpayInfo(Long orderId, Integer prdType) {
		
		return orderpayDao.findOrderpayByOrderId(orderId, prdType);
	}

	@Override
	public Orderpay getOrderpay(Long payId) {
		
		return orderpayDao.findOne(payId);
	}

	@Override
	public AirticketOrder getAirticketorderById(Long orderId) {
		
		return airticketOrderDao.getAirticketOrderById(orderId);
	}
	
    /**
	 * 清除session中Orderpay的缓存
	 * @param role
	 * @return
	 */
	public Object clearObject(Object object){
		orderpayDao.getSession().evict(object);
		return object;
	}
	
	 /**
     * 修改  凭证 上传图片文件
     * @param docInfoList
     * @param payId
     * @param orderId
     * @param mode
     * @param request
     * @return
     * @throws OptimisticLockHandleException
     * @throws PositionOutOfBoundException
     * @throws Exception
     */
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Map<String, Object> updatepayVoucherFile(
			ArrayList<DocInfo> docInfoList, Orderpay orderPay, String orderId,
			ModelMap mode, HttpServletRequest request)
			throws OptimisticLockHandleException, PositionOutOfBoundException,
			Exception {

		ArrayList<DocInfo> old_docInfoList=(ArrayList<DocInfo>) docInfoDao.findDocInfoByPayOrderId(orderPay.getId());
		if (old_docInfoList != null && old_docInfoList.size() > 0) {
			Iterator<DocInfo> iter = old_docInfoList.iterator();
			while (iter.hasNext()) {
				DocInfo old_docInfo = iter.next();
				docInfoDao.delete(old_docInfo);
			}
		}
		AirticketOrder pra = airticketOrderDao.getAirticketOrderById(new Long(orderId));

		if (docInfoList != null && docInfoList.size() > 0) {
			Iterator<DocInfo> iter = docInfoList.iterator();
			String docInfoIds = null;
			while (iter.hasNext()) {
				DocInfo docInfo = iter.next();
				docInfo.setPayOrderId(orderPay.getId());
				docInfoDao.save(docInfo);
				// save 保存之后，docInfo对象在数据库生成的主键ID居然也加入了docInfo对象中。
				// save之前，getId()应该是null，save后getId()有值了。
				if(StringUtils.isNotBlank(docInfoIds)){
					docInfoIds += "," + docInfo.getId();
				}else{
					docInfoIds =  docInfo.getId().toString();
				}
			}
			orderPay.setPayVoucher(docInfoIds);
		}
		orderpayDao.updateBySql(
				"update orderpay set payVoucher = ?,remarks=? where id = ?", orderPay
						.getPayVoucher(),orderPay.getRemarks(), orderPay.getId());
		
		Map<String, Object> map = new HashMap<String, Object>();

		//支付订单金额千位符处理
	    if (StringUtils.isNotBlank(orderPay.getMoneySerialNum())) {
	 	    clearObject(orderPay);
	 	   orderPay.setMoneySerialNum(moneyAmountService.getMoneyStr(orderPay.getMoneySerialNum()));
	    }
		mode.addAttribute("orderPay", orderPay);
		mode.addAttribute("pra", pra);
		map.put("isSuccess", true);
		return map;
	}

	@SuppressWarnings("rawtypes")
    @Override
	public List<Map<String, Object>> areaGaiQianCheck(Map map) {
		// TODO Auto-generated method stub
		return airticketOrderDao.areaGaiQianCheck(map);
	}



	// 定时任务
	@Override
	public void changeOrderStatus() {

		// 取所有订单
		List<AirticketOrder> orderList = airticketOrderDao.allAirticketOrderList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		for (AirticketOrder airticketOrder : orderList) {  
			//创建时间
			String createDate = airticketOrder.getCreateDate().toString();
			//剩余天数
			int remaindDays = airticketOrder.getRemaindDays();
			try {
				Date dateDays = sdf.parse(createDate);
				Calendar cl = Calendar.getInstance();
				cl.setTime(dateDays);
				cl.add(Calendar.DATE, remaindDays);
				String datestr = sdf.format(cl.getTime());//创建时间加上保留天数的时间
//				System.out.println(datestr);
				SimpleDateFormat sdfnow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String nowdate = sdfnow.format(new Date()); //系统当前时间
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date dt1 = df.parse(datestr);
				Date dt2 = df.parse(nowdate);
				AirticketOrder order = airticketPreOrderDao.findOne(airticketOrder.getId());
				//如果用户所在批发商不允许自动取消则跳过此订单
				if (1 == order.getUpdateBy().getCompany().getIsCancleOrder()) {
					continue;
				}
		        String orderState = String.valueOf(order.getOrderState());
		        String placeHolderType = String.valueOf(order.getPlaceHolderType());
		        ActivityAirTicket activity = airTicketDao.findOne(order.getAirticketId());
		        //占位订单
		        if(Context.PLACEHOLDERTYPE_ZW.equals(placeHolderType)){
		            //已占位订单，则进行占位归还
		            if(Context.ORDER_PAYSTATUS_YZF.equals(orderState)){
		            	if(dt1.getTime()<dt2.getTime()){
		            		 activity.setFreePosition(activity.getFreePosition() + order.getPersonNum());
				                activity.setSoldNopayPosition(activity.getSoldNopayPosition() - order.getPersonNum());
		            	}
		            }
		            activity.setNopayReservePosition(activity.getNopayReservePosition() - order.getPersonNum());
		        }else {
		            //切位订单
		        	if(dt1.getTime()<dt2.getTime()){
		        		AirticketActivityReserve reserve = this.airticketActivityReserveDao.findAgentReserve(order.getAirticketId(), order.getAgentinfoId());
			           if(reserve!=null){
			        	    reserve.setLeftpayReservePosition(reserve.getLeftpayReservePosition() + order.getPersonNum());
				            reserve.setPayReservePosition(reserve.getPayReservePosition() - order.getPersonNum());
				            activity.setSoldPayPosition(activity.getSoldPayPosition() - order.getPersonNum());
				            airticketActivityReserveDao.save(reserve);
			           }
		        	}
		            
		        }
		        order.setOrderState(Integer.valueOf(Context.ORDER_PAYSTATUS_YQX));
		        airticketPreOrderDao.save(order);
		        airTicketDao.save(activity);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}  
		// 循环更新订单状态
		
	}
	
	
	
	/**
	 * 修改未查看订单状态
	 * @param notSeenOrderIdList
	 * @return
	 */
    public Integer changeNotSeenOrderFlag(Set<Long> notSeenOrderIdList) {
    	if (CollectionUtils.isNotEmpty(notSeenOrderIdList)) {
    		int i = airticketPreOrderDao.changeNotSeenOrderFlag(notSeenOrderIdList);
    		return i;
    	}
    	return 0;
    }
    
    /**
	 * 保存订单
	 * @param order
	 * @return
	 */
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void saveOrder(AirticketOrder order) {
    	airticketPreOrderDao.save(order);
    }
    
    /**
	 * 更新机票订单
	 * @Description: 
	 * @param @param airticketOrder   
	 * @return void  
	 * @throws
	 * @author majiancheng
	 * @date 2015-11-12 下午2:30:22
	 */
	public void updateAirticketOrder(AirticketOrder airticketOrder) {
		airticketPreOrderDao.updateObj(airticketOrder);
	}



	/**
     * 保存或更新机票订单
     * @author hhx
     * @param airticketOrder
     */
    @Transactional(propagation = Propagation.REQUIRED)
	public void saveOrUpdateAirticketOrder(AirticketOrder airticketOrder){
		airticketOrderDao.saveOrUpdateAirticketOrderById(airticketOrder);
	}
    
    
    public Map<String, Object> findMeituIncomeInfoByOrderId(Long orderId) {
    	return airticketOrderDao.findMeituIncomeInfoByOrderId(orderId);
    }

	/**
	 * 机票余位校验
	 */
	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public void validateFreePosition(Map<String, String> parameters) throws Exception {
		String airticketId = parameters.get("airticketId");
		String orgPersonNum = parameters.get("orgPersonNum");
		String newPersonNum = parameters.get("newPersonNum");
		String agentId = parameters.get("agentId");
		String placeHolderType = parameters.get("placeHolderType");

		// 添加的人数
		Integer diffPersonNum = Integer.parseInt(newPersonNum) - Integer.parseInt(orgPersonNum);

		// 机票产品信息
		ActivityAirTicket activityAirTicket = activityAirTicketService.getActivityAirTicketById(Long.parseLong(airticketId));

		// 机票产品切位信息
		List<AirticketActivityReserve> activityReserves = airticketActivityReserveDao.findByAgentIdAndActivityId(Long.parseLong(agentId), Long.parseLong(airticketId));

		if("0".equals(placeHolderType)) {
			int freePosition = activityAirTicket.getFreePosition();
			if(diffPersonNum > freePosition) {
				throw new Exception("余位不足!");
			}
		} else if("1".equals(placeHolderType)) {
			if(activityReserves != null && activityReserves.size() > 0) {
				AirticketActivityReserve airticketActivityReserve = activityReserves.get(0);
				if(diffPersonNum > airticketActivityReserve.getLeftpayReservePosition()) {
					throw new Exception("余位不足!");
				}
			}
		}

	}

	@Override
	@Transactional(readOnly = false)
	public void handleConfirmStatus(AirticketOrder airticketOrder) {
		if(airticketOrder != null) {
			airticketOrder.setSeizedConfirmationStatus(Context.SEIZEDCONFIRMATIONSTATUS_1);
			airticketOrderDao.saveOrUpdateAirticketOrderById(airticketOrder);
		}
	}

	/**
	 * 机票订单修改，核心业务处理
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void modifyAirticketOrder(Map<String, String> parameters, String travelers, List<OrderContacts> orderContactsList, List<MoneyAmount> orderTotalClearPriceList) throws Exception{
		// 1.处理（保存或修改）游客信息
		handlerTravelers(travelers, parameters);
		// 2.修改渠道联系人信息
		modifyOrderContacts(parameters, orderContactsList);
		// 3.修改产品余切位
		handlerAirticketPosition(parameters);
		// 4.修改订单(总额，人数，预计团队返佣)
		handlerAirticketOrder(parameters, orderTotalClearPriceList);
		
	}

	/**
	 * 处理游客信息（保存或修改游客信息）
	 */
	private void handlerTravelers(String travelersStr, Map<String, String> parameters) throws Exception {
		Long createBy = UserUtils.getUser().getId();
		String orderId = parameters.get("orderId");
		String airticketId = parameters.get("airticketId");
		ActivityAirTicket activityAirTicket = null;
		if(StringUtils.isNotBlank(airticketId)) {
			activityAirTicket = activityAirTicketService.getActivityAirTicketById(Long.parseLong(airticketId));
		}
		JSONArray travelerJsonArray = new JSONArray(travelersStr);
		int len = travelerJsonArray.length();
		if(len > 0) {
			for(int i = 0; i < len; i++) {
				JSONObject travelerJsonObj = travelerJsonArray.getJSONObject(i);
				Traveler traveler = new Traveler();
				// 游客id
				Object travelerId = travelerJsonObj.get("travelerId");
				if(travelerId!=null&& StringUtils.isNotBlank(travelerId.toString())){
					if(!"-1".equals(travelerId.toString())) {
						traveler = travelerDao.findById(Long.parseLong(travelerId.toString()));
					}
				}
				// 游客名字
				Object travelerName = travelerJsonObj.get("travelerName");
				if(travelerName!=null&& StringUtils.isNotBlank(travelerName.toString())){
					traveler.setName(travelerName.toString());
				}
				// 游客拼音名字
				Object travelerPinyin = travelerJsonObj.get("travelerPinyin");
				if(travelerPinyin!=null&& StringUtils.isNotBlank(travelerPinyin.toString())){
					traveler.setNameSpell(travelerPinyin.toString());
				}
				// 游客性别
				Object travelerSex = travelerJsonObj.get("travelerSex");
				if(travelerSex!=null&& StringUtils.isNotBlank(travelerSex.toString())){
					traveler.setSex(Integer.parseInt(travelerSex.toString()));
				}
				// 游客生日
				Object birthDay = travelerJsonObj.get("birthDay");
				if(birthDay!=null&& StringUtils.isNotBlank(birthDay.toString())){
					traveler.setBirthDay(new SimpleDateFormat("yyyy-MM-dd").parse(birthDay.toString()));
				}
				// 游客国籍
				Object nationality = travelerJsonObj.get("nationality");
				if(nationality!=null&& StringUtils.isNotBlank(nationality.toString())){
					traveler.setNationality(Integer.parseInt(nationality.toString()));
				}
				// 游客电话
				Object telephone = travelerJsonObj.get("telephone");
				if(telephone!=null&& StringUtils.isNotBlank(telephone.toString())){
					traveler.setTelephone(telephone.toString());
				}
				// 游客类型
				Object personType = travelerJsonObj.get("personType");
				if(personType!=null&& StringUtils.isNotBlank(personType.toString())){
					traveler.setPersonType(Integer.parseInt(personType.toString()));
				}
				// 游客身份证号
				Object idCard = travelerJsonObj.get("idCard");
				if(idCard!=null&& StringUtils.isNotBlank(idCard.toString())){
					traveler.setIdCard(idCard.toString());
				}
				// 游客护照号
				Object passportCode = travelerJsonObj.get("passportCode");
				if(passportCode!=null&& StringUtils.isNotBlank(passportCode.toString())){
					traveler.setPassportCode(passportCode.toString());
				}
				// 游客护照类型
				Object passportType = travelerJsonObj.get("passportType");
				if(passportType!=null&& StringUtils.isNotBlank(passportType.toString())){
					traveler.setPassportType(passportType.toString());
				}
				// 游客护照有效期
				Object passportValidity = travelerJsonObj.get("passportValidity");
				if(passportValidity!=null&& StringUtils.isNotBlank(passportValidity.toString())){
					traveler.setPassportValidity(new SimpleDateFormat("yyyy-MM-dd").parse(passportValidity.toString()));
				}
				// 游客备注
				Object remarks = travelerJsonObj.get("remarks");
				if(remarks!=null&& StringUtils.isNotBlank(remarks.toString())){
					traveler.setRemark(remarks.toString());
				}
				if(travelerJsonObj.has("isIntermodal")){  // forbug 销售机票订单修改报错
					// 是否联运
					Object isIntermodal = travelerJsonObj.get("isIntermodal");
					if(isIntermodal!=null&& StringUtils.isNotBlank(isIntermodal.toString())){
						traveler.setIntermodalType(Integer.parseInt(isIntermodal.toString()));
					}
				}
				// 联运id
				Object intermodalId = travelerJsonObj.get("IntermodalId");
				if(intermodalId!=null&& StringUtils.isNotBlank(intermodalId.toString())){
					traveler.setIntermodalId(Long.parseLong(intermodalId.toString()));
				}
				// 游客srcPrice
				Object srcPrice = travelerJsonObj.get("srcPrice");
				if(srcPrice!=null&& StringUtils.isNotBlank(srcPrice.toString())){
					traveler.setSrcPrice(new BigDecimal(srcPrice.toString()));
				}
				// 设置订单id
				traveler.setOrderId(Long.parseLong(orderId));
				if (StringUtils.isBlank(traveler.getIsAirticketFlag())) {
					traveler.setIsAirticketFlag("1");
				}

				// 设置订单类型
				traveler.setOrderType(Integer.parseInt(Context.ORDER_STATUS_AIR_TICKET));
				// 游客原始应收价
				String originalPayPriceSerialNum = traveler.getOriginalPayPriceSerialNum();
				if(!StringUtils.isNotBlank(originalPayPriceSerialNum)) {
					originalPayPriceSerialNum = UuidUtils.generUuid();
					if(StringUtils.isNotBlank(personType.toString()) && activityAirTicket != null) {
						BigDecimal adultPrice = activityAirTicket.getSettlementAdultPrice();
						BigDecimal childPrice = activityAirTicket.getSettlementcChildPrice();
						BigDecimal specialPrice = activityAirTicket.getSettlementSpecialPrice();
						Long currencyId = activityAirTicket.getCurrency_id();
						BigDecimal taxamt = activityAirTicket.getTaxamt();
						MoneyAmount orgMoneyAmount = new MoneyAmount();
						orgMoneyAmount.setDelFlag("0");
						orgMoneyAmount.setMoneyType(Context.MONEY_TYPE_YSJSJ);
						orgMoneyAmount.setOrderType(Context.ProductType.PRODUCT_AIR_TICKET);
						orgMoneyAmount.setSerialNum(originalPayPriceSerialNum);
						orgMoneyAmount.setBusindessType(2);
						orgMoneyAmount.setCreatedBy(UserUtils.getUser().getId());
						orgMoneyAmount.setCreateTime(new Date());
						orgMoneyAmount.setCurrencyId(currencyId.intValue());
						if("1".equals(personType.toString())) {
							orgMoneyAmount.setAmount(adultPrice.add(taxamt));
						} else if("2".equals(personType.toString())) {
							orgMoneyAmount.setAmount(childPrice.add(taxamt));
						} else if("3".equals(personType.toString())) {
							orgMoneyAmount.setAmount(specialPrice.add(taxamt));
						}
						moneyAmountDao.save(orgMoneyAmount);
					}
					traveler.setOriginalPayPriceSerialNum(originalPayPriceSerialNum);
				}

				// 预计个人返佣
				String rebatesMoneySerialNum = UuidUtils.generUuid();
				MoneyAmount moneyAmount = new MoneyAmount();
				moneyAmount.setSerialNum(rebatesMoneySerialNum);
				Object rebatesCurrency = travelerJsonObj.get("rebatesCurrency");
				if(rebatesCurrency!=null&& StringUtils.isNotBlank(rebatesCurrency.toString())){
					moneyAmount.setCurrencyId(Integer.parseInt(rebatesCurrency.toString()));
				}
				Object rebatesMoney = travelerJsonObj.get("rebatesMoney");
				if(rebatesMoney!=null&& StringUtils.isNotBlank(rebatesMoney.toString())){
					moneyAmount.setAmount(new BigDecimal(rebatesMoney.toString()));
				}
				moneyAmount.setBusindessType(Context.BUSINESS_TYPE_TRAVELER);
				moneyAmount.setMoneyType(24);
				moneyAmount.setCreatedBy(createBy);
				moneyAmount.setDelFlag("0");
				moneyAmount.setUid(Long.parseLong(orderId));

				moneyAmountDao.saveObj(moneyAmount);
				traveler.setRebatesMoneySerialNum(rebatesMoneySerialNum);

				//将当前游客的结算价清除掉
				String payPriceSerialNum = traveler.getPayPriceSerialNum();
				if(StringUtils.isNotBlank(payPriceSerialNum)) {
					List<MoneyAmount> tempMoneyAmountList = moneyAmountDao.getAmountByUid(payPriceSerialNum);
					if(tempMoneyAmountList != null && tempMoneyAmountList.size() > 0) {
						for(MoneyAmount amount : tempMoneyAmountList) {
							moneyAmountDao.delete(amount);
						}
					}
				}

				// 结算价
				Object jsPrices = travelerJsonObj.get("jsPrices");
				if(jsPrices!=null&& StringUtils.isNotBlank(jsPrices.toString())){
					JSONArray jsPricesArray = new JSONArray(jsPrices.toString());
					int jsPriceslen = jsPricesArray.length();
					if(jsPriceslen > 0) {
						String jsSerialNum = UuidUtils.generUuid();
						if(StringUtils.isNotBlank(traveler.getPayPriceSerialNum())) {
							jsSerialNum = traveler.getPayPriceSerialNum();
						}
						for(int j = 0; j < jsPriceslen; j++) {
							JSONObject jsPricesJsonObj = jsPricesArray.getJSONObject(j);
							MoneyAmount jsMoneyAmount = new MoneyAmount();
							jsMoneyAmount.setOrderType(Context.ProductType.PRODUCT_AIR_TICKET);
							jsMoneyAmount.setMoneyType(Context.MONEY_TYPE_JSJ);
							jsMoneyAmount.setBusindessType(Context.BUSINESS_TYPE_TRAVELER);
							jsMoneyAmount.setCreatedBy(UserUtils.getUser().getId());
							jsMoneyAmount.setDelFlag("0");
							Object jsCurrencyId = jsPricesJsonObj.get("currencyId");
							if(jsCurrencyId!=null&& StringUtils.isNotBlank(jsCurrencyId.toString())){
								jsMoneyAmount.setCurrencyId(Integer.parseInt(jsCurrencyId.toString()));
							}
							Object jsAmount = jsPricesJsonObj.get("jsAmount");
							if(jsAmount!=null&& StringUtils.isNotBlank(jsAmount.toString())){
								jsMoneyAmount.setAmount(new BigDecimal(jsAmount.toString()));
							}
							jsMoneyAmount.setSerialNum(jsSerialNum);
							moneyAmountDao.saveObj(jsMoneyAmount);
						}
						traveler.setPayPriceSerialNum(jsSerialNum);
					}
				}

				// 单价币种
				Object srcPriceCurrency = travelerJsonObj.get("srcPriceCurrency");
				if(srcPriceCurrency!=null&& StringUtils.isNotBlank(srcPriceCurrency.toString())){
					Currency currency = currencyDao.findById(Long.parseLong(srcPriceCurrency.toString()));
					traveler.setSrcPriceCurrency(currency);
				}

				try {
					travelerDao.save(traveler);
				} catch(Exception e) {
					e.printStackTrace();
					throw new Exception("保存或修改游客信息出错！");
				}
			}

		}

	}

	/**
	 * 修改订单渠道联系人信息
	 */
	private void modifyOrderContacts(Map<String, String> parameters, List<OrderContacts> orderContactsList) throws Exception {
		String orderId = parameters.get("orderId");
		AirticketOrder airticketOrder = airticketOrderDao.getAirticketOrderById(Long.parseLong(orderId));
		try {
			//删除原先的订单
			orderContactsService.deleteOrderContactsByIdAndType(Long.parseLong(orderId), Context.ORDER_TYPE_JP);
			//保存
			for (OrderContacts orderContacts : orderContactsList) {
				orderContacts.setOrderId(Long.parseLong(orderId));
				orderContacts.setOrderType(Context.ORDER_TYPE_JP);
				if (airticketOrder != null) {					
					orderContacts.setAgentId(airticketOrder.getAgentinfoId());
				}
				orderContactsDao.saveObj(orderContacts);
				//更新渠道商(签约渠道更新到agentinfoid字段，非签修改时不允许修改渠道名称)
				String newAgentId = parameters.get("agentId") == null ? null : parameters.get("agentId").toString();
				String nagentName = parameters.get("orderCompanyName") == null ? null : parameters.get("orderCompanyName").toString();
				if(!"-1".equals(newAgentId)){
					airticketOrder.setAgentinfoId(Long.parseLong(newAgentId));
				} else {
					airticketOrder.setNagentName(nagentName);
				}
				airticketOrderDao.saveOrUpdateAirticketOrderById(airticketOrder);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception("修改产品联系人信息出错!");
		}
	}
	
	/**
	 * 修改产品余切位
	 */
	private void handlerAirticketPosition(Map<String, String> parameters) throws Exception {
		// 获取数据
		String orgPersonNum = parameters.get("orgPersonNum");
		String newPersonNum = parameters.get("newPersonNum");
		String orderId = parameters.get("orderId");
		String airticketId = parameters.get("airticketId");
		// 添加的人数
		Integer diffPersonNum = Integer.parseInt(newPersonNum) - Integer.parseInt(orgPersonNum);
		// 机票订单信息
		AirticketOrder airticketOrder = airticketOrderDao.getAirticketOrderById(Long.parseLong(orderId));
		// 机票产品信息
		ActivityAirTicket activityAirTicket = activityAirTicketService.getActivityAirTicketById(Long.parseLong(airticketId));

		/** 更改机票产品信息 */
		/**机票订单付款状态：全款已支付 5 ，全款未支付 1， 订金已支付 4， 订金未支付 2， 已占位（预占位等等）3， 计调确认占位 7， 财务确认占位 8	*/
		if (diffPersonNum > 0) {
			airticketOrderStockService.changeGroupFreeNum(activityAirTicket, airticketOrder, diffPersonNum, Context.StockOpType.MODIFY);
		}


	}

	/**
	 * 修改订单(总额，人数，特殊需求，团队返佣)
	 */
	private void handlerAirticketOrder(Map<String, String> parameters, List<MoneyAmount> orderTotalClearPriceList) throws Exception {
		String adultNum = parameters.get("adultNum");
		String childNum = parameters.get("childNum");
		String specialNum = parameters.get("specialNum");
		String newPersonNum = parameters.get("newPersonNum");
		String comments = parameters.get("comments");
		String groupRebatesCurrency = parameters.get("groupRebatesCurrency");
		String groupRebatesMoney = parameters.get("groupRebatesMoney");
		String orderId = parameters.get("orderId");

		// 机票订单信息
		AirticketOrder airticketOrder = airticketOrderDao.getAirticketOrderById(Long.parseLong(orderId));

		//将当前游客的结算价清除掉
		String totalMoneySerialNum = airticketOrder.getTotalMoney();
		if(StringUtils.isNotBlank(totalMoneySerialNum)) {
			List<MoneyAmount> tempMoneyAmountList = moneyAmountDao.getAmountByUid(totalMoneySerialNum);
			if(tempMoneyAmountList != null && tempMoneyAmountList.size() > 0) {
				for(MoneyAmount amount : tempMoneyAmountList) {
					moneyAmountDao.delete(amount);
				}
			}
		}
		//为了获取订单总金额，先定义一个变量totalAmount
		BigDecimal totalAmount = new BigDecimal(0);
		Long createBy = UserUtils.getUser().getId();
		try {
			// 修改订单总额
			String totalMoneyUuid = UuidUtils.generUuid();
			if(StringUtils.isNotBlank(totalMoneySerialNum)) {
				totalMoneyUuid = totalMoneySerialNum;
			}
			if(orderTotalClearPriceList != null && orderTotalClearPriceList.size() > 0) {
                for(MoneyAmount tempMoneyAmount : orderTotalClearPriceList) {
					Currency currency = currencyDao.getById(tempMoneyAmount.getCurrencyId().longValue());
					MoneyAmount totalMoney = new MoneyAmount();
                    totalMoney.setSerialNum(totalMoneyUuid);
                    totalMoney.setCurrencyId(tempMoneyAmount.getCurrencyId());
                    totalMoney.setAmount(tempMoneyAmount.getAmount());
					totalMoney.setMoneyType(Context.MONEY_TYPE_YSH);
					totalMoney.setBusindessType(Context.MONEY_BUSINESSTYPE_ORDER);
					totalMoney.setCreatedBy(createBy);
					totalMoney.setDelFlag("0");
					totalMoney.setUid(Long.parseLong(orderId));
					totalMoney.setExchangerate(currency.getCurrencyExchangerate());
					totalMoney.setOrderType(Integer.parseInt(Context.ORDER_STATUS_AIR_TICKET));
					//对多币种进行循环记算，获取订单总金额-----by junhao.zhao--2017.01.11----开始---
					totalAmount = totalAmount.add(tempMoneyAmount.getAmount().multiply(currency.getCurrencyExchangerate()));
					//对多币种进行循环记算，获取订单总金额-----by junhao.zhao--2017.01.11----结束---
					moneyAmountDao.save(totalMoney);
                }
            }
			airticketOrder.setTotalMoney(totalMoneyUuid);
			// 修改订单人数
			airticketOrder.setAdultNum(Integer.parseInt(adultNum));
			airticketOrder.setChildNum(Integer.parseInt(childNum));
			airticketOrder.setSpecialNum(Integer.parseInt(specialNum));
			airticketOrder.setPersonNum(Integer.parseInt(newPersonNum));
			// 修改特殊需求
			airticketOrder.setComments(comments);
			// 修改订单团队返佣
			String groupRebatesUuid = UuidUtils.generUuid();

			MoneyAmount groupRebatesMoneyAmount = new MoneyAmount();
			groupRebatesMoneyAmount.setSerialNum(groupRebatesUuid);
			groupRebatesMoneyAmount.setCurrencyId(Integer.parseInt(groupRebatesCurrency));
			groupRebatesMoneyAmount.setAmount(new BigDecimal(groupRebatesMoney));
			groupRebatesMoneyAmount.setMoneyType(Context.MONEY_TYPE_FYFY);
			groupRebatesMoneyAmount.setBusindessType(Context.MONEY_BUSINESSTYPE_ORDER);
			groupRebatesMoneyAmount.setCreatedBy(createBy);
			groupRebatesMoneyAmount.setDelFlag("0");
			groupRebatesMoneyAmount.setUid(Long.parseLong(orderId));
			//获取总人数与订单id-----by junhao.zhao--2017.01.11----开始---
			int personNum = airticketOrder.getPersonNum();
			Long OrderId = airticketOrder.getId();
			//获取总人数与订单id-----by junhao.zhao--2017.01.11----结束---
			moneyAmountDao.save(groupRebatesMoneyAmount);
			//对表order_data_statistics中的人数与订单金额进行修改-----by junhao.zhao--2017.01.11----开始---
//			orderDateSaveOrUpdateDao.updateForAirticket(totalAmount,personNum,OrderId,Context.ORDER_STATUS_AIR_TICKET);
			//对表order_data_statistics中的人数与订单金额进行修改-----by junhao.zhao--2017.01.11----结束---
			airticketOrder.setScheduleBackUuid(groupRebatesUuid);
		} catch(Exception e) {
			e.printStackTrace();
			throw new Exception("修改订单信息出错!");
		}

	}

	/**
	 * 根据渠道id查找机票订单信息，并且排除99：已取消， 111：已删除的订单
	 */
	@Override
	public List<AirticketOrder> findByAgentId(Long agentId) {
		StringBuffer sb = new StringBuffer();
		sb.append("select * from airticket_order ao where (ao.order_state!=99 and ao.order_state!=111) and ao.agentinfo_id='");
		sb.append(agentId);
		sb.append("'");
		return airticketOrderDao.findBySql(sb.toString(),AirticketOrder.class);
	}
	
	/*
	 * 根据产品id获取该产品的所有订单id和订单No
	 * @param activityAirticketId  机票产品id
	 * @return 订单id和No的Map 列表
	 * @author xianglei.dong
	 */
	public List<Map<String, Object>> findOrderIdAndNoByActivityId(Long activityAirticketId){
		return airticketOrderDao.findOrderIdAndNoByActivityId(activityAirticketId);
	}
}
