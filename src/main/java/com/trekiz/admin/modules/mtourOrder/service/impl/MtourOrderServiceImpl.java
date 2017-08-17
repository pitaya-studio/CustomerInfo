/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.input.BaseInput4MT;
import com.trekiz.admin.common.input.BaseOut4MT;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.service.BaseService;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.service.ActivityFileService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.entity.AirTicketFile;
import com.trekiz.admin.modules.airticket.entity.FlightInfo;
import com.trekiz.admin.modules.airticket.repository.IAirticketDao;
import com.trekiz.admin.modules.airticket.service.IFlightInfoService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.mtourCommon.dao.MtourCommonDao;
import com.trekiz.admin.modules.mtourCommon.service.SerialNumberService;
import com.trekiz.admin.modules.mtourOrder.dao.*;
import com.trekiz.admin.modules.mtourOrder.entity.*;
import com.trekiz.admin.modules.mtourOrder.jsonbean.*;
import com.trekiz.admin.modules.mtourOrder.service.*;
import com.trekiz.admin.modules.mtourfinance.json.DocInfoJsonBean;
import com.trekiz.admin.modules.mtourfinance.json.OrderpayJsonBean;
import com.trekiz.admin.modules.mtourfinance.repository.FinanceDao;
import com.trekiz.admin.modules.order.airticket.service.AirticketPreOrderService;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.entity.Refund;
import com.trekiz.admin.modules.order.repository.OrderContactsDao;
import com.trekiz.admin.modules.order.repository.OrderpayDao;
import com.trekiz.admin.modules.order.repository.RefundDao;
import com.trekiz.admin.modules.order.service.OrderPayService;
import com.trekiz.admin.modules.pay.dao.PayGroupDao;
import com.trekiz.admin.modules.pay.entity.PayGroup;
import com.trekiz.admin.modules.supplier.entity.SupplierInfo;
import com.trekiz.admin.modules.supplier.repository.SupplierInfoDao;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.repository.DocInfoDao;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.entity.TravelerVisa;
import com.trekiz.admin.modules.traveler.repository.TravelerDao;
import com.trekiz.admin.modules.traveler.service.TravelerVisaService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */


@Service
@Transactional(readOnly = true)
public class MtourOrderServiceImpl  extends BaseService implements MtourOrderService{
	@Autowired
	private MtourCommonDao mtourCommonDao;
	@Autowired
	private AirticketPreOrderService airticketPreOrderService;
	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	@Autowired
	private MoneyAmountDao moneyAmountDao;
	@Autowired
	private AirticketOrderMtourDao airticketOrderDao;
	@Autowired
	private DocInfoDao docInfoDao;
	@Autowired
	private OrderpayDao orderpayDao;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private PayGroupDao payGroupDao;
	@Autowired
	private AirticketOrderMoneyAmountDao airticketOrderMoneyAmountDao;
	@Autowired
	private CostRecordDao costRecordDao;
	@Autowired
	private ActivityFileService activityFileService;
	@Autowired
	private RefundDao refundDao;
	@Autowired
	private IFlightInfoService flightService;
	@Autowired
	private AirticketOrderMoneyAmountService orderMoneyAmountService;
	@Autowired
	private OrderContactsDao orderContactsDao;
	@Autowired
	private AirticketOrderChangePriceService orderChangePriceService;
	@Autowired
	private TravelerDao travelerDao;
	@Autowired
	private TravelerVisaService travelerVisaService;
	@Autowired
	private TravelerPapersTypeService papersTypeService;
	@Autowired
	private AirticketOrderPnrService airticketOrderPnrService;
	@Autowired
	private OrderPayService orderpayService;
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private SupplierInfoDao supplierInfoDao;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private AirticketOrderPnrAirlinePriceDao airticketOrderPnrAirlinePriceDao;
	@Autowired
	private AirticketOrderPnrGroupService pnrGroupService;
	@Autowired
	private IAirticketDao iAirticketDao;
	@Autowired
	private AirticketOrderPnrDao airticketOrderPnrDao;
	@Autowired
	private AirticketOrderPnrAirlineDao airticketOrderPnrAirlineDao;
	@Autowired
	private FinanceDao financeDao;
	@Autowired
	private IAirticketOrderDao iAirticketOrderDao;

	/**
	 * 机票快速生成订单保存
	 * @author hhx
	 * @param input
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public BaseOut4MT saveAirticketOrder(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT();
		JSONObject jsonObj = JSONObject.parseObject(input.getParam());
		//机票产品基本信息
		JSONObject baseInfo = jsonObj.getJSONObject("baseInfo");
		//加入修改功能--回传订单id,重新设值    主表不变
		Long id  = baseInfo.getLong("orderUuid");
		AirticketOrder order = null;
		ActivityAirTicket activityAirTicket = null;
		if(id!=null){
			order =  (AirticketOrder) mtourCommonDao.getSession().load(AirticketOrder.class, id);
			activityAirTicket = (ActivityAirTicket) mtourCommonDao.getSession().load(ActivityAirTicket.class, order.getAirticketId());
			//activityAirTicket.setGroupCode(baseInfo.getString("groupNo"));
			//可以修改的子表删除重新保存
			removeSubInfo(order, activityAirTicket);
		}else{
			order = new AirticketOrder();
			order.setOrderState(1);//设置一个默认值财务留作统计
			activityAirTicket = new ActivityAirTicket();
			activityAirTicket.setLockStatus(0);
		}
		if(baseInfo!=null){
			activityAirTicket.setActivityName(baseInfo.getString("productName"));
			//activityAirTicket.setGroupCode(baseInfo.getString("groupNo"));
			activityAirTicket.setJourney(baseInfo.getString("itinerary"));
			activityAirTicket.setDepartureCity(baseInfo.getString("departureCityUuid"));
			activityAirTicket.setArrivedCity(baseInfo.getString("arrivalCityUuid"));
			activityAirTicket.setStartingDate(baseInfo.getDate("departureDate"));
			activityAirTicket.setCountry(baseInfo.getString("lineCountryUuid"));
			if(StringUtils.isNotBlank(baseInfo.getString("lineType"))){
				activityAirTicket.setLineType(baseInfo.getString("lineType"));
			}
			activityAirTicket.setProCompany(UserUtils.getUser().getCompany().getId());
			activityAirTicket.setProductStatus(baseInfo.getIntValue("orderStatusCode"));
			if(id==null){
				activityAirTicket.setProductCode(airticketPreOrderService.genOrderNo(Context.PRODUCT_NUM_TYPE));
			}
			String operatorIds = parseBaseInfo(baseInfo,"ticketCZ");
			activityAirTicket.setOperator(operatorIds);//多个操作的id
			this.setOptInfo(activityAirTicket, id==null?BaseService.OPERATION_ADD:BaseService.OPERATION_UPDATE);
			mtourCommonDao.getSession().saveOrUpdate(activityAirTicket);

			String salerIds = parseBaseInfo(baseInfo,"ticketXS");//多个销售的id
			order.setSalerName(salerIds); //使用order表中的salerName保存多个销售的id。createBy没法用了。
		}
		//航班信息
		JSONArray flights = jsonObj.getJSONArray("flights");
		if(flights!=null){
			for(int j=0;j<flights.size();j++){
				JSONObject jsonObjSub = flights.getJSONObject(j);
				FlightInfo flight = new FlightInfo();
				flight.setAirticketId(activityAirTicket.getId());
				flight.setFlightNumber(jsonObjSub.getString("flightNo"));
				//前端多次修改数据不统一,机场信息多处理一次
				flight.setLeaveAirport(jsonObjSub.getJSONObject("departureAirport").getString("id"));
				if(StringUtils.isBlank(flight.getLeaveAirport())){
					flight.setLeaveAirport(jsonObjSub.getJSONObject("departureAirport").getString("airportUuid"));
				}
				flight.setDestinationAirpost(jsonObjSub.getJSONObject("arrivalAirport").getString("id"));
				if(StringUtils.isBlank(flight.getDestinationAirpost())){
					flight.setDestinationAirpost(jsonObjSub.getJSONObject("arrivalAirport").getString("airportUuid"));
				}
				//前端没有时间,这里组装一下
				StringBuilder startTime = new StringBuilder("");
				if(StringUtils.isNotBlank(jsonObjSub.getString("departureDate"))){
					startTime.append(jsonObjSub.getString("departureDate"));
					if(StringUtils.isNotBlank(jsonObjSub.getString("departureHour"))){
						startTime.append(" "+jsonObjSub.getString("departureHour"));
					}else{
						startTime.append(" 00");
					}
					if(StringUtils.isNotBlank(jsonObjSub.getString("departureMinute"))){
						startTime.append(":"+jsonObjSub.getString("departureMinute"));
					}else{
						startTime.append(":00");
					}
					flight.setStartTime(DateUtils.dateFormat(startTime.toString(), DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM));
				}
				
				StringBuilder arrivalTime = new StringBuilder("");
				if(StringUtils.isNotBlank(jsonObjSub.getString("arrivalDate"))){
					arrivalTime.append(jsonObjSub.getString("arrivalDate"));
					if(StringUtils.isNotBlank(jsonObjSub.getString("arrivalHour"))){
						arrivalTime.append(" "+jsonObjSub.getString("arrivalHour"));
					}else{
						arrivalTime.append(" 00");
					}
					if(StringUtils.isNotBlank(jsonObjSub.getString("arrivalMinute"))){
						arrivalTime.append(":"+jsonObjSub.getString("arrivalMinute"));
					}else{
						arrivalTime.append(":00");
					}
					flight.setArrivalTime(DateUtils.dateFormat(arrivalTime.toString(), DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM));
				}
				flight.setRemark(jsonObjSub.getString("memo"));
				flight.setActivityAirTicket(activityAirTicket);
				this.setOptInfo(flight, BaseService.OPERATION_ADD);
				mtourCommonDao.saveObj(flight);
			}
		}
		//机票订单信息
		JSONObject fee = jsonObj.getJSONObject("fee");
		order.setGroupCode(activityAirTicket.getGroupCode());//此处是空值
		order.setAirticketId(activityAirTicket.getId());
		order.setPersonNum(baseInfo.getIntValue("reservationCount"));
		if(id==null){
			order.setOrderNo(airticketPreOrderService.genOrderNo(Context.ORDER_NUM_TYPE));
		}
		order.setProductTypeId(Long.valueOf(Context.ORDER_STATUS_AIR_TICKET));
		if(order.getPaymentStatus()==1){
			order.setPaymentStatus(101);
		}
		//订单总额
		JSONArray orderAmount = fee.getJSONArray("orderAmount");
		String uuid = null;
		if(orderAmount!=null){
			uuid = UuidUtils.generUuid();
			for(int j=0;j<orderAmount.size();j++){
				JSONObject obj = orderAmount.getJSONObject(j);
				MoneyAmount amount = new MoneyAmount();
				amount.setSerialNum(uuid);
				amount.setCurrencyId(obj.getInteger("currencyUuid"));
				amount.setAmount(obj.getBigDecimal("amount"));
				amount.setExchangerate(obj.getBigDecimal("exchangeRate"));
				//amount.setExchangerate(new BigDecimal(1));//暂时支持人名币，汇率1
				amount.setOrderType(Integer.valueOf(Context.ORDER_STATUS_AIR_TICKET));
				amount.setBusindessType(Context.BUSINESS_TYPE_ORDER);
				amount.setMoneyType(Context.MONEY_TYPE_YSH);
				amount.setUid(order.getId());
				this.setOptInfo(amount, BaseService.OPERATION_ADD);
				mtourCommonDao.saveObj(amount);
			}
			order.setTotalMoney(uuid);
		}
		//应收总额
		JSONArray receivableAmount = fee.getJSONArray("receivableAmount");
		if(receivableAmount!=null){
			uuid = UuidUtils.generUuid();
			for(int j=0;j<receivableAmount.size();j++){
				JSONObject obj = receivableAmount.getJSONObject(j);
				MoneyAmount amount = new MoneyAmount();
				amount.setSerialNum(uuid);
				amount.setCurrencyId(obj.getInteger("currencyUuid"));
				amount.setAmount(obj.getBigDecimal("amount"));
				amount.setExchangerate(obj.getBigDecimal("exchangeRate"));
				//amount.setExchangerate(new BigDecimal(1));
				amount.setOrderType(Integer.valueOf(Context.ORDER_STATUS_AIR_TICKET));
				amount.setBusindessType(Context.BUSINESS_TYPE_ORDER);
				amount.setUid(order.getId());
				amount.setMoneyType(Context.MONEY_TYPE_YSYSH);
				this.setOptInfo(amount, BaseService.OPERATION_ADD);
				mtourCommonDao.saveObj(amount);
			}
			order.setOriginalTotalMoney(uuid);
		}
		//定金总额
		JSONArray frontAmount = fee.getJSONArray("totalDeposit");
		if(frontAmount!=null){
			uuid = UuidUtils.generUuid();
			for(int j=0;j<frontAmount.size();j++){
				JSONObject obj = frontAmount.getJSONObject(j);
				MoneyAmount amount = new MoneyAmount();
				amount.setSerialNum(uuid);
				amount.setCurrencyId(obj.getInteger("currencyUuid"));
				amount.setAmount(obj.getBigDecimal("amount"));
				amount.setExchangerate(obj.getBigDecimal("exchangeRate"));
				//amount.setExchangerate(new BigDecimal(1));
				amount.setOrderType(Integer.valueOf(Context.ORDER_STATUS_AIR_TICKET));
				amount.setBusindessType(Context.BUSINESS_TYPE_ORDER);
				amount.setUid(order.getId());
				amount.setMoneyType(Context.MONEY_TYPE_YSDJ);
				this.setOptInfo(amount, BaseService.OPERATION_ADD);
				mtourCommonDao.saveObj(amount);
			}
			order.setFrontMoney(uuid);
		}
		order.setComments(jsonObj.getString("memo"));
		order.setLockStatus(baseInfo.getIntValue("orderStatusCode"));
		String channelName = jsonObj.getJSONObject("reservations").getString("channelName");
		String channelCode = jsonObj.getJSONObject("reservations").getString("channelCode");
		order.setNagentName(channelName);
		order.setNagentcode(channelCode);
		if(StringUtils.isNotBlank(jsonObj.getJSONObject("reservations").getString("channelUuid"))){
			order.setAgentinfoId(jsonObj.getJSONObject("reservations").getLong("channelUuid"));
		}else{
			if (StringUtils.isBlank(channelName)){
				order.setAgentinfoId(-1L);
			}else {
				//查一下渠道表中的对应该公司的非签约渠道有没有该名称。有：把对应id放入order对象中。
				//没有：新建一个该名称的非签约渠道对象保存。把保存后生成的id放入order对象中。
				String agentSql = "SELECT a.id FROM agentinfo a WHERE a.is_uncontract='1' AND a.supplyId=? AND a.agentName=?";
				Long companyId = UserUtils.getUser().getCompany().getId();
				List<Integer> agentIdList = mtourCommonDao.findBySql(agentSql,companyId,channelName.trim());
				if (CollectionUtils.isEmpty(agentIdList)){
					String userIdStr = UserUtils.getUser().getId().toString();
					Agentinfo agentinfo = new Agentinfo(channelName,userIdStr,channelCode,companyId,"1");
					mtourCommonDao.saveObj(agentinfo);
					order.setAgentinfoId(agentinfo.getId());
				}else {
					order.setAgentinfoId((long)agentIdList.get(0));
				}
			}
		}
		this.setOptInfo(order, id==null?BaseService.OPERATION_ADD:BaseService.OPERATION_UPDATE);
		if (order.getId()!=null){//更改其他收入的渠道商，costRecord表中，也进行更改。
			StringBuilder updateCost = new StringBuilder();
			updateCost.append("UPDATE cost_record c SET c.supplyId =?,c.supplyName =? WHERE c.orderId =? ")
					  .append(" AND c.delFlag = 0 AND c.orderType = 7 AND c.supplyType = 2 AND c.budgetType = 2");
			mtourCommonDao.updateBySql(updateCost.toString(),order.getAgentinfoId(),order.getNagentName(),order.getId());
		}
		mtourCommonDao.getSession().saveOrUpdate(order);
		//大编号信息
		JSONArray invoiceOriginalGroups = fee.getJSONArray("invoiceOriginalGroups");
		
		if(invoiceOriginalGroups!=null && id==null){
			for(int j=0;j<invoiceOriginalGroups.size();j++){
				JSONObject invoiceObj = invoiceOriginalGroups.getJSONObject(j);
				JSONArray invoiceOriginals = invoiceObj.getJSONArray("invoiceOriginals");
				//pnr组信息
				AirticketOrderPnrGroup pnrgroup = new AirticketOrderPnrGroup();
				pnrgroup.setAirticketOrderId(order.getId().intValue());
				pnrgroup.setTicketPersonNum(baseInfo.getInteger("drawerCount"));
				pnrgroup.setPersonNum(baseInfo.getInteger("reservationCount"));
				pnrgroup.setPnrGroupIndex(0);
				this.setOptInfo(pnrgroup, BaseService.OPERATION_ADD);
				mtourCommonDao.saveObj(pnrgroup);
				if(invoiceOriginals!=null){
					for(int k=0;k<invoiceOriginals.size();k++){
						//pnr信息
						JSONObject obj = invoiceOriginals.getJSONObject(k);
						AirticketOrderPnr pnr = new AirticketOrderPnr();
						pnr.setAirticketOrderId(order.getId().intValue());
						int type = obj.getInteger("invoiceOriginalTypeCode");
						pnr.setCodeType(obj.getInteger("invoiceOriginalTypeCode"));
						if(type==0){//pnr
							pnr.setFlightPnr(obj.getString("PNR"));
						}else if(type==1){//地接社
							pnr.setFlightPnr(obj.getString("tourOperatorUuid"));
						}
						pnr.setAirline(obj.getString("airlineCompanyUuid"));
						pnr.setAirticketOrderPnrGroupUuid(pnrgroup.getUuid());
						pnr.setCostAccountNo(obj.getString("costAccountNo"));
						pnr.setCostBankName(obj.getString("costBankName"));
						pnr.setCostSupplyId(obj.getInteger("costTourOperatorUuid"));
						pnr.setCostSupplyType(obj.getString("costTourOperatorTypeCode"));
						//期限
						JSONObject subobj = obj.getJSONObject("deadline");
						if(subobj!=null){
							pnr.setTicketDeadline(DateUtils.string2Date(subobj.getString("ticketDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
							pnr.setFrontDeadline(DateUtils.string2Date(subobj.getString("depositDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
							pnr.setListDeadline(DateUtils.string2Date(subobj.getString("listDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
							pnr.setRenameDeadline(DateUtils.string2Date(subobj.getString("renameDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
							pnr.setCancelDeadline(DateUtils.string2Date(subobj.getString("cancelDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
						}
						this.setOptInfo(pnr, BaseService.OPERATION_ADD);
						mtourCommonDao.saveObj(pnr);
						//航段信息
						JSONArray airlines = obj.getJSONArray("airlines");
						if(airlines!=null){
							for(int s=0;s<airlines.size();s++){
								JSONObject objtriple = airlines.getJSONObject(s);
								AirticketOrderPnrAirline airline = new AirticketOrderPnrAirline();
								airline.setAirticketOrderId(order.getId().intValue());
								airline.setAirticketOrderPnrUuid(pnr.getUuid());
								airline.setAirlineName(objtriple.getString("airlineName"));
								this.setOptInfo(airline, BaseService.OPERATION_ADD);
								mtourCommonDao.saveObj(airline);
								//航段价格表--成本价
								JSONArray costs = objtriple.getJSONArray("costs");
								if(costs!=null){
									for(int t=0;t<costs.size();t++){
										JSONObject objmultiple = costs.getJSONObject(t);
										//新的保存成本记录
										CostRecord costRecord = new CostRecord();
										costRecord.setActivityId(activityAirTicket.getId());
										costRecord.setPayStatus(2);
										costRecord.setOrderId(order.getId());
										costRecord.setOrderType(7);
										costRecord.setReviewType(0);
//										costRecord.setName(airline.getAirlineName());
										costRecord.setName(((SupplierInfo) supplierInfoDao.getSession().load(SupplierInfo.class, pnr.getCostSupplyId().longValue())).getSupplierName());
										costRecord.setAirlineUuid(airline.getUuid());
										
										//冗余存放 航空公司二字码和航段名称，如果当前成本已付款则不会更新任何信息否则会同步更新
										//update by zhanghao 20160109 
										costRecord.setAirline(pnr.getAirline());
										costRecord.setAirlineName(airline.getAirlineName());
										//冗余存放 航空公司二字码和航段名称，如果当前成本已付款则不会更新任何信息否则会同步更新
										
										costRecord.setCostTotalDeposit(objmultiple.getBigDecimal("totalDeposit"));
										costRecord.setQuantity(objmultiple.getInteger("peopleCount"));
										costRecord.setPrice(objmultiple.getBigDecimal("costUnitPrice"));
										costRecord.setSupplyType(1);
										if(StringUtils.isNotBlank(pnr.getCostSupplyType())){
											costRecord.setSupplierType(Integer.parseInt(pnr.getCostSupplyType()));
										}
										costRecord.setSupplyName(((SupplierInfo) supplierInfoDao.getSession().load(SupplierInfo.class, pnr.getCostSupplyId().longValue())).getSupplierName());
										costRecord.setSupplyId(pnr.getCostSupplyId());
										costRecord.setRate(objmultiple.getBigDecimal("exchangeRate"));
										costRecord.setCurrencyId(objmultiple.getInteger("currencyUuid"));
										costRecord.setCurrencyAfter(currencyService.getRMBCurrencyId().getId().intValue());
										costRecord.setPriceAfter(objmultiple.getBigDecimal("peopleCount").multiply(objmultiple.getBigDecimal("costUnitPrice")).multiply(objmultiple.getBigDecimal("exchangeRate")).setScale(2, BigDecimal.ROUND_HALF_UP));
										costRecord.setIsJoin(pnr.getCodeType());
										costRecord.setBigCode(pnr.getFlightPnr());
										costRecord.setBankName(pnr.getCostBankName());
										costRecord.setBankAccount(pnr.getCostAccountNo());
										costRecord.setReviewType(0);
										costRecord.setBudgetType(1);
										costRecord.setOverseas(0);
										costRecord.setCreateBy(UserUtils.getUser());
										costRecord.setPnrUuid(pnr.getUuid());
										
										this.setOptInfo(costRecord, BaseService.OPERATION_ADD);
										mtourCommonDao.saveObj(costRecord);
										serialNumberService.genSerialNumber(SerialNumberService.TABLENAME_COST_RECORD, costRecord.getId().intValue());
										//航段中的成本记录表
										AirticketOrderPnrAirlinePrice price = new AirticketOrderPnrAirlinePrice();
										price.setAirticketOrderId(order.getId().intValue());
										price.setAirticketOrderPnrAirlineUuid(airline.getUuid());
										price.setAirticketOrderPnrUuid(pnr.getUuid());
										price.setCurrencyId(objmultiple.getInteger("currencyUuid"));
										price.setPersonNum(objmultiple.getInteger("peopleCount"));
										price.setFrontMoney(objmultiple.getDouble("totalDeposit"));
										price.setExchangerate(objmultiple.getDoubleValue("exchangeRate"));
										price.setPrice(objmultiple.getDouble("costUnitPrice"));
										price.setPriceType(0);
										price.setCostRecordUuid(costRecord.getUuid());
										this.setOptInfo(price, BaseService.OPERATION_ADD);
										mtourCommonDao.saveObj(price);
									}
								}
								//航段价格表--外部价
								JSONArray salePrices = objtriple.getJSONArray("salePrices");
								if(salePrices!=null){
									for(int t=0;t<salePrices.size();t++){
										JSONObject objmultiple = salePrices.getJSONObject(t);
										AirticketOrderPnrAirlinePrice price = new AirticketOrderPnrAirlinePrice();
										price.setAirticketOrderId(order.getId().intValue());
										price.setAirticketOrderPnrAirlineUuid(airline.getUuid());
										price.setAirticketOrderPnrUuid(pnr.getUuid());
										price.setCurrencyId(objmultiple.getInteger("currencyUuid"));
										price.setExchangerate(objmultiple.getDoubleValue("exchangeRate"));
										price.setPrice(objmultiple.getDouble("costUnitPrice"));
										price.setPersonNum(objmultiple.getInteger("peopleCount"));
										price.setFrontMoney(objmultiple.getDouble("totalDeposit"));
										price.setPriceType(1);
										this.setOptInfo(price, BaseService.OPERATION_ADD);
										mtourCommonDao.saveObj(price);
									}
								}
							}
						}
					}
				}
			}
		}else if(invoiceOriginalGroups!=null && id!=null){
			//修改的方法
			for(int j=0;j<invoiceOriginalGroups.size();j++){
				JSONObject invoiceObj = invoiceOriginalGroups.getJSONObject(j);
				JSONArray invoiceOriginals = invoiceObj.getJSONArray("invoiceOriginals");
				if(invoiceOriginals!=null){
					for(int k=0;k<invoiceOriginals.size();k++){
						//pnr信息
						JSONObject obj = invoiceOriginals.getJSONObject(k);
						AirticketOrderPnr pnr = airticketOrderPnrDao.getByUuid(obj.getString("uuid"));
						pnr.setAirticketOrderId(order.getId().intValue());
						int type = obj.getInteger("invoiceOriginalTypeCode");
						pnr.setCodeType(obj.getInteger("invoiceOriginalTypeCode"));
						if(type==0){//pnr
							pnr.setFlightPnr(obj.getString("PNR"));
						}else if(type==1){//地接社
							pnr.setFlightPnr(obj.getString("tourOperatorUuid"));
						}
						pnr.setAirline(obj.getString("airlineCompanyUuid"));
						pnr.setCostAccountNo(obj.getString("costAccountNo"));
						pnr.setCostBankName(obj.getString("costBankName"));
						pnr.setCostSupplyId(obj.getInteger("costTourOperatorUuid"));
						pnr.setCostSupplyType(obj.getString("costTourOperatorTypeCode"));
						//期限
						JSONObject subobj = obj.getJSONObject("deadline");
						if(subobj!=null){
							pnr.setTicketDeadline(DateUtils.string2Date(subobj.getString("ticketDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
							pnr.setFrontDeadline(DateUtils.string2Date(subobj.getString("depositDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
							pnr.setListDeadline(DateUtils.string2Date(subobj.getString("listDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
							pnr.setRenameDeadline(DateUtils.string2Date(subobj.getString("renameDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
							pnr.setCancelDeadline(DateUtils.string2Date(subobj.getString("cancelDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
						}
						this.setOptInfo(pnr, BaseService.OPERATION_UPDATE);
						mtourCommonDao.getSession().saveOrUpdate(pnr);
						//航段信息
						JSONArray airlines = obj.getJSONArray("airlines");
						List<AirticketOrderPnrAirline> airlineList = airticketOrderPnrAirlineDao.findByOrderPpnrUuid(obj.getString("uuid"));
						//全部置为删除状态
						for(AirticketOrderPnrAirline airlineDel : airlineList){
							airlineDel.setDelFlag("1");
						}
						if(airlines!=null){
							for(int s=0;s<airlines.size();s++){
								JSONObject objtriple = airlines.getJSONObject(s);
								String aopaUuid = objtriple.getString("aopaUuid");
								if(StringUtils.isNotBlank(aopaUuid)){//修改
									AirticketOrderPnrAirline airline = new AirticketOrderPnrAirline();
									for(AirticketOrderPnrAirline airlineUpdate : airlineList){
										if(airlineUpdate.getUuid().equals(aopaUuid)){
											airline = airlineUpdate;
											break;
										}
									}
									airline.setAirlineName(objtriple.getString("airlineName"));
									airline.setDelFlag("0");
									this.setOptInfo(airline, BaseService.OPERATION_UPDATE);
									mtourCommonDao.getSession().saveOrUpdate(airline);
									//航段价格表--成本价
									JSONArray costs = objtriple.getJSONArray("costs");
									List<AirticketOrderPnrAirlinePrice> costPricesList = airticketOrderPnrAirlinePriceDao.getAirlinePricesByPnrAirlineUuid(aopaUuid, 0);
									for(AirticketOrderPnrAirlinePrice costDel : costPricesList){
										
										CostRecord costRecord = costRecordDao.findByUUID(costDel.getCostRecordUuid());
										//bug 11731  现在PayStatus 的状态 0和1  都需要判断 add by songyang 2016年1月11日 20:50:34
										if(costRecord!=null&&(costRecord.getPayStatus()==1||costRecord.getPayStatus()==0))//如果是已付款状态，成本记录不进行数据同步 update by zhanghao 20160109
											continue;
										costDel.setDelFlag("1");
									}
									if(costs!=null){
										for(int t=0;t<costs.size();t++){
											JSONObject objmultiple = costs.getJSONObject(t);
											String aopapUuid = objmultiple.getString("aopapUuid");
											if(StringUtils.isNotBlank(aopapUuid)){
												AirticketOrderPnrAirlinePrice price = new AirticketOrderPnrAirlinePrice();
												for(AirticketOrderPnrAirlinePrice priceUpdate : costPricesList){
													if(priceUpdate.getUuid().equals(aopapUuid)){
														price = priceUpdate;
														break;
													}
												}
												CostRecord costRecord = costRecordDao.findByUUID(price.getCostRecordUuid());
												if(costRecord!=null){
													//bug 11731  现在PayStatus 的状态 0和1  都需要判断 add by songyang 2016年1月11日 20:50:34
													if(costRecord.getPayStatus()==1||costRecord.getPayStatus()==0){//如果是已付款状态，成本记录不进行数据同步 update by zhanghao 20160109
														continue;
													}
													costRecord.setAirlineUuid(aopaUuid);
													
													//冗余存放 航空公司二字码和航段名称，如果当前成本已付款则不会更新任何信息否则会同步更新
													//update by zhanghao 20160109 
													costRecord.setAirline(pnr.getAirline());
													costRecord.setAirlineName(airline.getAirlineName());
													//冗余存放 航空公司二字码和航段名称，如果当前成本已付款则不会更新任何信息否则会同步更新
													
													costRecord.setQuantity(objmultiple.getInteger("peopleCount"));
													costRecord.setCostTotalDeposit(objmultiple.getBigDecimal("totalDeposit"));
													costRecord.setPrice(objmultiple.getBigDecimal("costUnitPrice"));
													if(StringUtils.isNotBlank(pnr.getCostSupplyType())){
														costRecord.setSupplierType(Integer.parseInt(pnr.getCostSupplyType()));
													}
													// 订单修改时，成本款项名称全部改成地接社的名字   update by shijun.liu  2016.05.23   bug:14035
													costRecord.setName(((SupplierInfo) supplierInfoDao.getSession().load(SupplierInfo.class, pnr.getCostSupplyId().longValue())).getSupplierName());
													costRecord.setSupplyName(((SupplierInfo) supplierInfoDao.getSession().load(SupplierInfo.class, pnr.getCostSupplyId().longValue())).getSupplierName());
													costRecord.setSupplyId(pnr.getCostSupplyId());
													costRecord.setRate(objmultiple.getBigDecimal("exchangeRate"));
													costRecord.setCurrencyId(objmultiple.getInteger("currencyUuid"));
													costRecord.setCurrencyAfter(currencyService.getRMBCurrencyId().getId().intValue());
													costRecord.setPriceAfter(objmultiple.getBigDecimal("peopleCount").multiply(objmultiple.getBigDecimal("costUnitPrice")).multiply(objmultiple.getBigDecimal("exchangeRate")).setScale(2, BigDecimal.ROUND_HALF_UP));
													costRecord.setIsJoin(pnr.getCodeType());
													costRecord.setBigCode(pnr.getFlightPnr());
													costRecord.setBankName(pnr.getCostBankName());
													costRecord.setBankAccount(pnr.getCostAccountNo());
													this.setOptInfo(costRecord, BaseService.OPERATION_UPDATE);
													mtourCommonDao.getSession().saveOrUpdate(costRecord);
													serialNumberService.genSerialNumber(SerialNumberService.TABLENAME_COST_RECORD, costRecord.getId().intValue());
												}
												//航段中的成本记录表
												price.setCurrencyId(objmultiple.getInteger("currencyUuid"));
												price.setPersonNum(objmultiple.getInteger("peopleCount"));
												price.setFrontMoney(objmultiple.getDouble("totalDeposit"));
												price.setExchangerate(objmultiple.getDoubleValue("exchangeRate"));
												price.setPrice(objmultiple.getDouble("costUnitPrice"));
												price.setDelFlag("0");
												this.setOptInfo(price, BaseService.OPERATION_UPDATE);
												mtourCommonDao.getSession().saveOrUpdate(price);
											}else{
												//新的保存成本记录
												CostRecord costRecord = new CostRecord();
												costRecord.setActivityId(activityAirTicket.getId());
												costRecord.setPayStatus(2);
												costRecord.setOrderId(order.getId());
												costRecord.setOrderType(7);
												costRecord.setReviewType(0);
												costRecord.setAirlineUuid(aopaUuid);
												//冗余存放 航空公司二字码和航段名称，如果当前成本已付款则不会更新任何信息否则会同步更新
												//update by zhanghao 20160109 
												costRecord.setAirline(pnr.getAirline());
												costRecord.setAirlineName(airline.getAirlineName());
												// 订单修改时，成本款项名称全部改成地接社的名字   update by shijun.liu  2016.05.23   bug:14035
												costRecord.setName(((SupplierInfo) supplierInfoDao.getSession().load(SupplierInfo.class, pnr.getCostSupplyId().longValue())).getSupplierName());
												//冗余存放 航空公司二字码和航段名称，如果当前成本已付款则不会更新任何信息否则会同步更新
												costRecord.setCostTotalDeposit(objmultiple.getBigDecimal("totalDeposit"));
												costRecord.setQuantity(objmultiple.getInteger("peopleCount"));
												costRecord.setPrice(objmultiple.getBigDecimal("costUnitPrice"));
												costRecord.setSupplyType(1);
												if(StringUtils.isNotBlank(pnr.getCostSupplyType())){
													costRecord.setSupplierType(Integer.parseInt(pnr.getCostSupplyType()));
												}
												costRecord.setSupplyName(((SupplierInfo) supplierInfoDao.getSession().load(SupplierInfo.class, pnr.getCostSupplyId().longValue())).getSupplierName());
												costRecord.setSupplyId(pnr.getCostSupplyId());
												costRecord.setRate(objmultiple.getBigDecimal("exchangeRate"));
												costRecord.setCurrencyId(objmultiple.getInteger("currencyUuid"));
												costRecord.setCurrencyAfter(currencyService.getRMBCurrencyId().getId().intValue());
												costRecord.setPriceAfter(objmultiple.getBigDecimal("peopleCount").multiply(objmultiple.getBigDecimal("costUnitPrice")).multiply(objmultiple.getBigDecimal("exchangeRate")).setScale(2, BigDecimal.ROUND_HALF_UP));
												costRecord.setIsJoin(pnr.getCodeType());
												costRecord.setBigCode(pnr.getFlightPnr());
												costRecord.setBankName(pnr.getCostBankName());
												costRecord.setBankAccount(pnr.getCostAccountNo());
												costRecord.setReviewType(0);
												costRecord.setBudgetType(1);
												costRecord.setOverseas(0);
												costRecord.setCreateBy(UserUtils.getUser());
												costRecord.setPnrUuid(pnr.getUuid());
												this.setOptInfo(costRecord, BaseService.OPERATION_ADD);
												mtourCommonDao.saveObj(costRecord);
												serialNumberService.genSerialNumber(SerialNumberService.TABLENAME_COST_RECORD, costRecord.getId().intValue());
												//航段中的成本记录表
												AirticketOrderPnrAirlinePrice price = new AirticketOrderPnrAirlinePrice();
												price.setAirticketOrderId(order.getId().intValue());
												price.setAirticketOrderPnrAirlineUuid(airline.getUuid());
												price.setAirticketOrderPnrUuid(pnr.getUuid());
												price.setCurrencyId(objmultiple.getInteger("currencyUuid"));
												price.setPersonNum(objmultiple.getInteger("peopleCount"));
												price.setFrontMoney(objmultiple.getDouble("totalDeposit"));
												price.setExchangerate(objmultiple.getDoubleValue("exchangeRate"));
												price.setPrice(objmultiple.getDouble("costUnitPrice"));
												price.setPriceType(0);
												price.setCostRecordUuid(costRecord.getUuid());
												this.setOptInfo(price, BaseService.OPERATION_ADD);
												mtourCommonDao.saveObj(price);
											}
										}
										//处理成本中处于删除状态的数据
										for(AirticketOrderPnrAirlinePrice costTodo : costPricesList){
											if("1".equals(costTodo.getDelFlag())){
												CostRecord costRecord = costRecordDao.findByUUID(costTodo.getCostRecordUuid());
												if(costRecord!=null){
													//bug 11731  现在PayStatus 的状态 0和1  都需要判断 add by songyang 2016年1月11日 20:50:34
													if(costRecord.getPayStatus()==1||costRecord.getPayStatus()==0){//如果是已付款状态，成本记录不进行数据同步 update by zhanghao 20160109
														continue;
													}
													costRecord.setDelFlag("1");
													mtourCommonDao.getSession().saveOrUpdate(costRecord);
												}
											}
										}
									}
									//航段价格表--外部价
									JSONArray salePrices = objtriple.getJSONArray("salePrices");
									List<AirticketOrderPnrAirlinePrice> salePricesList = airticketOrderPnrAirlinePriceDao.getAirlinePricesByPnrAirlineUuid(aopaUuid, 1);
									for(AirticketOrderPnrAirlinePrice priceDel : salePricesList){
										priceDel.setDelFlag("1");
									}
									if(salePrices!=null){
										for(int t=0;t<salePrices.size();t++){
											JSONObject objmultiple = salePrices.getJSONObject(t);
											String aopapUuid = objmultiple.getString("aopapUuid");
											if(StringUtils.isNotBlank(aopapUuid)){
												AirticketOrderPnrAirlinePrice price = new AirticketOrderPnrAirlinePrice();
												for(AirticketOrderPnrAirlinePrice priceUpdate : salePricesList){
													if(priceUpdate.getUuid().equals(aopapUuid)){
														price = priceUpdate;
														break;
													}
												}
												price.setCurrencyId(objmultiple.getInteger("currencyUuid"));
												price.setExchangerate(objmultiple.getDoubleValue("exchangeRate"));
												price.setPrice(objmultiple.getDouble("costUnitPrice"));
												price.setPersonNum(objmultiple.getInteger("peopleCount"));
												price.setFrontMoney(objmultiple.getDouble("totalDeposit"));
												price.setDelFlag("0");
												this.setOptInfo(price, BaseService.OPERATION_UPDATE);
												mtourCommonDao.getSession().saveOrUpdate(price);
											}else{
												AirticketOrderPnrAirlinePrice price = new AirticketOrderPnrAirlinePrice();
												price.setAirticketOrderId(order.getId().intValue());
												price.setAirticketOrderPnrAirlineUuid(airline.getUuid());
												price.setAirticketOrderPnrUuid(pnr.getUuid());
												price.setCurrencyId(objmultiple.getInteger("currencyUuid"));
												price.setExchangerate(objmultiple.getDoubleValue("exchangeRate"));
												price.setPrice(objmultiple.getDouble("costUnitPrice"));
												price.setPersonNum(objmultiple.getInteger("peopleCount"));
												price.setFrontMoney(objmultiple.getDouble("totalDeposit"));
												price.setPriceType(1);
												this.setOptInfo(price, BaseService.OPERATION_ADD);
												mtourCommonDao.saveObj(price);
											}
										}
									}
								}else{//新增
									AirticketOrderPnrAirline airline = new AirticketOrderPnrAirline();
									airline.setAirticketOrderId(order.getId().intValue());
									airline.setAirticketOrderPnrUuid(pnr.getUuid());
									airline.setAirlineName(objtriple.getString("airlineName"));
									this.setOptInfo(airline, BaseService.OPERATION_ADD);
									mtourCommonDao.saveObj(airline);
									//航段价格表--成本价
									JSONArray costs = objtriple.getJSONArray("costs");
									if(costs!=null){
										for(int t=0;t<costs.size();t++){
											JSONObject objmultiple = costs.getJSONObject(t);
											//新的保存成本记录
											CostRecord costRecord = new CostRecord();
											costRecord.setActivityId(activityAirTicket.getId());
											costRecord.setPayStatus(2);
											costRecord.setOrderId(order.getId());
											costRecord.setOrderType(7);
											costRecord.setReviewType(0);
//											costRecord.setName(airline.getAirlineName());
											// 订单修改时，成本款项名称全部改成地接社的名字   update by shijun.liu  2016.05.23   bug:14035
											costRecord.setName(((SupplierInfo) supplierInfoDao.getSession().load(SupplierInfo.class, pnr.getCostSupplyId().longValue())).getSupplierName());
											//costRecord.setName("成本");
											costRecord.setAirlineUuid(airline.getUuid());
											//冗余存放 航空公司二字码和航段名称，如果当前成本已付款则不会更新任何信息否则会同步更新
											//update by zhanghao 20160109 
											costRecord.setAirline(pnr.getAirline());
											costRecord.setAirlineName(airline.getAirlineName());
											//冗余存放 航空公司二字码和航段名称，如果当前成本已付款则不会更新任何信息否则会同步更新
											costRecord.setCostTotalDeposit(objmultiple.getBigDecimal("totalDeposit"));
											costRecord.setQuantity(objmultiple.getInteger("peopleCount"));
											costRecord.setPrice(objmultiple.getBigDecimal("costUnitPrice"));
											costRecord.setSupplyType(1);
											if(StringUtils.isNotBlank(pnr.getCostSupplyType())){
												costRecord.setSupplierType(Integer.parseInt(pnr.getCostSupplyType()));
											}
											costRecord.setSupplyName(((SupplierInfo) supplierInfoDao.getSession().load(SupplierInfo.class, pnr.getCostSupplyId().longValue())).getSupplierName());
											costRecord.setSupplyId(pnr.getCostSupplyId());
											costRecord.setRate(objmultiple.getBigDecimal("exchangeRate"));
											costRecord.setCurrencyId(objmultiple.getInteger("currencyUuid"));
											costRecord.setCurrencyAfter(currencyService.getRMBCurrencyId().getId().intValue());
											costRecord.setPriceAfter(objmultiple.getBigDecimal("peopleCount").multiply(objmultiple.getBigDecimal("costUnitPrice")).multiply(objmultiple.getBigDecimal("exchangeRate")).setScale(2, BigDecimal.ROUND_HALF_UP));
											costRecord.setIsJoin(pnr.getCodeType());
											costRecord.setBigCode(pnr.getFlightPnr());
											costRecord.setBankName(pnr.getCostBankName());
											costRecord.setBankAccount(pnr.getCostAccountNo());
											costRecord.setReviewType(0);
											costRecord.setBudgetType(1);
											costRecord.setOverseas(0);
											costRecord.setCreateBy(UserUtils.getUser());
											costRecord.setPnrUuid(pnr.getUuid());
											this.setOptInfo(costRecord, BaseService.OPERATION_ADD);
											mtourCommonDao.saveObj(costRecord);
											serialNumberService.genSerialNumber(SerialNumberService.TABLENAME_COST_RECORD, costRecord.getId().intValue());
											//航段中的成本记录表
											AirticketOrderPnrAirlinePrice price = new AirticketOrderPnrAirlinePrice();
											price.setAirticketOrderId(order.getId().intValue());
											price.setAirticketOrderPnrAirlineUuid(airline.getUuid());
											price.setAirticketOrderPnrUuid(pnr.getUuid());
											price.setCurrencyId(objmultiple.getInteger("currencyUuid"));
											price.setPersonNum(objmultiple.getInteger("peopleCount"));
											price.setFrontMoney(objmultiple.getDouble("totalDeposit"));
											price.setExchangerate(objmultiple.getDoubleValue("exchangeRate"));
											price.setPrice(objmultiple.getDouble("costUnitPrice"));
											price.setPriceType(0);
											price.setCostRecordUuid(costRecord.getUuid());
											this.setOptInfo(price, BaseService.OPERATION_ADD);
											mtourCommonDao.saveObj(price);
										}
									}
									//航段价格表--外部价
									JSONArray salePrices = objtriple.getJSONArray("salePrices");
									if(salePrices!=null){
										for(int t=0;t<salePrices.size();t++){
											JSONObject objmultiple = salePrices.getJSONObject(t);
											AirticketOrderPnrAirlinePrice price = new AirticketOrderPnrAirlinePrice();
											price.setAirticketOrderId(order.getId().intValue());
											price.setAirticketOrderPnrAirlineUuid(airline.getUuid());
											price.setAirticketOrderPnrUuid(pnr.getUuid());
											price.setCurrencyId(objmultiple.getInteger("currencyUuid"));
											price.setExchangerate(objmultiple.getDoubleValue("exchangeRate"));
											price.setPrice(objmultiple.getDouble("costUnitPrice"));
											price.setPersonNum(objmultiple.getInteger("peopleCount"));
											price.setFrontMoney(objmultiple.getDouble("totalDeposit"));
											price.setPriceType(1);
											this.setOptInfo(price, BaseService.OPERATION_ADD);
											mtourCommonDao.saveObj(price);
										}
									}
								}
							}
						}
					    //对于已删除的航段，删除相关的航段价格表和成本记录表记录
						String airSql = "UPDATE airticket_order_pnr_airline_price aopap SET aopap.delFlag = '1' WHERE aopap.airticket_order_pnr_airline_uuid = ?";//删除航段价格表语句
						String costSql = "UPDATE cost_record cr SET cr.delFlag = 1 WHERE cr.uuid IN  " +
										 "(SELECT aopap.cost_record_uuid FROM airticket_order_pnr_airline_price aopap  " +
										 "WHERE aopap.airticket_order_pnr_airline_uuid = ? AND aopap.delFlag = '0' AND aopap.cost_record_uuid IS NOT NULL)";
						for(AirticketOrderPnrAirline airlineDel : airlineList){
							if("1".equals(airlineDel.getDelFlag())){
								String airlineUuid = airlineDel.getUuid();//航段表UUID
								mtourCommonDao.updateBySql(costSql, airlineUuid);
								mtourCommonDao.updateBySql(airSql, airlineUuid);
							}
						}
					}
				}
			}
		}
		if(id==null){
			//成本追加信息
			JSONObject priceChange = fee.getJSONObject("priceChange");
			if(priceChange!=null){
				JSONArray additionalCost = priceChange.getJSONArray("additionalCosts");
				if(additionalCost!=null){
					for(int m=0;m<additionalCost.size();m++){
						JSONObject objsub = additionalCost.getJSONObject(m);
						AirticketOrderMoneyAmount price = new AirticketOrderMoneyAmount();
						price.setAirticketOrderId(order.getId().intValue());
						price.setMoneyType(3);
						price.setSerialNum(UuidUtils.generUuid());
						price.setExchangerate(objsub.getDoubleValue("exchangeRate"));
						price.setCurrencyId(objsub.getIntValue("currencyUuid"));
						price.setAmount(objsub.getDouble("amount"));
						price.setFundsName(objsub.getString("fundsName"));
						price.setStatus(1);
						price.setPaystatus(0);
						this.setOptInfo(price, BaseService.OPERATION_ADD);
						mtourCommonDao.saveObj(price);
						serialNumberService.genSerialNumber(SerialNumberService.TABLENAME_AIRTICKET_ORDER_MONEYAMOUNT, price.getId());
					}
				}
				//外报价修改表
				JSONArray salePrices = priceChange.getJSONArray("salePrices");
				if(salePrices!=null){
					for(int m=0;m<salePrices.size();m++){
						JSONObject objsub = salePrices.getJSONObject(m);
						AirticketOrderChangePrice price = new AirticketOrderChangePrice();
						price.setAirticketOrderId(order.getId().intValue());
						price.setExchangerate(objsub.getDoubleValue("exchangeRate"));
						if("+".equals(objsub.getString("changeType")) || "0".equals(objsub.getString("changeType"))){
							price.setComputeType(0);
						}else{
							price.setComputeType(1);
						}
						price.setCurrencyId(objsub.getIntValue("currencyUuid"));
						price.setPrice(objsub.getDouble("amount"));
						price.setMemo(objsub.getString("memo"));
						this.setOptInfo(price, BaseService.OPERATION_ADD);
						mtourCommonDao.saveObj(price);
					}
				}
			}
		}else{
			JSONObject priceChange = fee.getJSONObject("priceChange");
			//外报价修改表
			JSONArray salePrices = priceChange.getJSONArray("salePrices");
			if(salePrices!=null){
				//先删除，再新增
				orderChangePriceService.deleteByOrderId(id);
				for(int m=0;m<salePrices.size();m++){
					JSONObject objsub = salePrices.getJSONObject(m);
					AirticketOrderChangePrice price = new AirticketOrderChangePrice();
					price.setAirticketOrderId(order.getId().intValue());
					price.setExchangerate(objsub.getDoubleValue("exchangeRate"));
					if("+".equals(objsub.getString("changeType")) || "0".equals(objsub.getString("changeType"))){
						price.setComputeType(0);
					}else{
						price.setComputeType(1);
					}
					price.setCurrencyId(objsub.getIntValue("currencyUuid"));
					price.setPrice(objsub.getDouble("amount"));
					price.setMemo(objsub.getString("memo"));
					this.setOptInfo(price, BaseService.OPERATION_ADD);
					mtourCommonDao.saveObj(price);
				}
			}
		}
		//预订人信息
		JSONObject reservations = jsonObj.getJSONObject("reservations");
		if(reservations!=null){
			JSONArray contactsArr = reservations.getJSONArray("contacts");
			if(contactsArr!=null){
				for(int j=0;j<contactsArr.size();j++){
					JSONObject jsonObjSub = contactsArr.getJSONObject(j);
					OrderContacts orderContacts = new OrderContacts();
					orderContacts.setOrderId(order.getId());
					if(StringUtils.isNotBlank(reservations.getString("channelUuid"))){
						orderContacts.setAgentId(reservations.getLongValue("channelUuid"));
					}else{
						orderContacts.setAgentId(-1L);
					}
					orderContacts.setContactsName(jsonObjSub.getString("name"));
					orderContacts.setContactsTel(jsonObjSub.getString("phone"));
					orderContacts.setContactsAddress(jsonObjSub.getString("address"));
					orderContacts.setContactsTixedTel(jsonObjSub.getString("tel"));
					orderContacts.setContactsZipCode(jsonObjSub.getString("zipcode"));
					orderContacts.setContactsFax(jsonObjSub.getString("tax"));
					orderContacts.setOrderType(Integer.valueOf(Context.ORDER_STATUS_AIR_TICKET));
					this.setOptInfo(orderContacts, BaseService.OPERATION_ADD);
					mtourCommonDao.saveObj(orderContacts);
				}
			}
		}
		//上传附件信息attachment
		JSONArray attachment = jsonObj.getJSONArray("attachment");
		if(attachment!=null){
			for(int h=0;h<attachment.size();h++){
				JSONObject jsonObjSub = attachment.getJSONObject(h);
				AirTicketFile file = new AirTicketFile();
				file.setActivityAirTicket(activityAirTicket);
				Long docId = jsonObjSub.getLong("attachmentUuid");
				if(docId!=null){
					DocInfo doc = (DocInfo) mtourCommonDao.getSession().load(DocInfo.class, docId);
					file.setDocInfo(doc);
				}
				file.setFileName(jsonObjSub.getString("fileName"));
				this.setOptInfo(file, BaseService.OPERATION_ADD);
				mtourCommonDao.saveObj(file);
			}
		}
		//游客信息
		JSONArray travelers = jsonObj.getJSONArray("travelers");
		if(travelers!=null){
			for(int h=0;h<travelers.size();h++){
				JSONObject travelersObj = travelers.getJSONObject(h);
				Traveler traveler = new Traveler();
				traveler.setOrderType(7);
				traveler.setOrderId(order.getId());
				traveler.setName(travelersObj.getString("name"));
				traveler.setNameSpell(travelersObj.getString("englishName"));
				traveler.setSex(travelersObj.getIntValue("sexCode"));
				traveler.setSrcPrice(travelersObj.getBigDecimal("amount"));
				Currency c = currencyService.findCurrency(Long.parseLong(travelersObj.getString("currencyUuid")));
				traveler.setSrcPriceCurrency(c);
				traveler.setRemark(travelersObj.getString("memo"));
				this.setOptInfo(traveler, BaseService.OPERATION_ADD);
				mtourCommonDao.saveObj(traveler);
				//签证信息
				JSONArray visas = travelersObj.getJSONArray("visas");
				if(visas!=null){
					for(int j=0;j<visas.size();j++){
						JSONObject visaObj = visas.getJSONObject(j);
						TravelerVisa travelervisa = new TravelerVisa();
						travelervisa.setTraveler(traveler);
						JSONObject country = visaObj.getJSONObject("country");
						if(country!=null){
							Long areaId = visaObj.getJSONObject("country").getLong("id");
							if(areaId==null && StringUtils.isNotBlank(visaObj.getJSONObject("country").getString("countryId"))){
								areaId = visaObj.getJSONObject("country").getLong("countryId");
							}
							if(areaId!=null){
								Area area = (Area) mtourCommonDao.getSession().load(Area.class, areaId);
								travelervisa.setApplyCountry(area);
							}
						}
						if(StringUtils.isNotBlank(visaObj.getString("visaTypeCode"))){
							travelervisa.setVisaTypeId(visaObj.getInteger("visaTypeCode"));
						}
						this.setOptInfo(travelervisa, BaseService.OPERATION_ADD);
						mtourCommonDao.saveObj(travelervisa);
					}
				}
				//证件信息
				JSONArray credentials = travelersObj.getJSONArray("credentials");
				if(credentials!=null){
					for(int j=0;j<credentials.size();j++){
						JSONObject paperObj = credentials.getJSONObject(j);
						TravelerPapersType travelerpaper = new TravelerPapersType();
						travelerpaper.setOrderId(order.getId().intValue());
						travelerpaper.setTravelerId(traveler.getId().intValue());
						if(StringUtils.isNotBlank(paperObj.getString("credentialsTypeCode"))){
							travelerpaper.setPapersType(paperObj.getInteger("credentialsTypeCode"));
						}
						travelerpaper.setIdCard(paperObj.getString("credentialsNo"));
						if(StringUtils.isNotBlank(paperObj.getString("credentialsExpire"))){
							travelerpaper.setValidityDate(paperObj.getDate("credentialsExpire"));
						}
						this.setOptInfo(travelerpaper, BaseService.OPERATION_ADD);
						mtourCommonDao.saveObj(travelerpaper);
					}
				}
			}
		}
		//这里更新一下产品团号
		 if(id==null){
		     activityAirTicket.setGroupCode(airticketPreOrderService.genGroupCode(order));
		     order.setGroupCode(activityAirTicket.getGroupCode());//订单中的团号更新
		     mtourCommonDao.updateObj(activityAirTicket);
		 }
		//更新机票订单的付款状态.
		financeDao.updateOrderRefundFlag(order.getId());
		Map<String,Map<String,String>> outmap = new HashMap<String,Map<String,String>>();
		Map<String,String> submap = new HashMap<String,String>();
		submap.put("orderUuid", order.getId().toString());
		if(order.getLockStatus()==0){
			submap.put("orderStatus", "正常");
		}else if(order.getLockStatus()==2){
			submap.put("orderStatus", "取消");
		}else if(order.getLockStatus()==3){
			submap.put("orderStatus", "草稿");
		}
		submap.put("orderStatusCode", String.valueOf(order.getLockStatus()));
		outmap.put("baseInfo", submap);
		out.setData(outmap);
		out.setResponseCode4Success();
		return out;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public BaseOut4MT saveAirticketOrderForCommon(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT();
		JSONObject jsonObj = JSONObject.parseObject(input.getParam());
		//机票产品基本信息
		JSONObject baseInfo = jsonObj.getJSONObject("baseInfo");
		//加入修改功能--回传订单id,重新设值    主表不变
		Long id  = baseInfo.getLong("orderUuid");
		AirticketOrder order = null;
		ActivityAirTicket activityAirTicket = null;
		if(id!=null){
			order =  (AirticketOrder) mtourCommonDao.getSession().load(AirticketOrder.class, id);
			activityAirTicket = (ActivityAirTicket) mtourCommonDao.getSession().load(ActivityAirTicket.class, order.getAirticketId());
			//activityAirTicket.setGroupCode(baseInfo.getString("groupNo"));
			//可以修改的子表删除重新保存
			removeSubInfo(order, activityAirTicket);
		}else{
			order = new AirticketOrder();
			order.setOrderState(1);//设置一个默认值财务留作统计
			activityAirTicket = new ActivityAirTicket();
		}
		if(baseInfo!=null){
			activityAirTicket.setActivityName(baseInfo.getString("productName"));
			//activityAirTicket.setGroupCode(baseInfo.getString("groupNo"));
			activityAirTicket.setJourney(baseInfo.getString("itinerary"));
			activityAirTicket.setDepartureCity(baseInfo.getString("departureCityUuid"));
			activityAirTicket.setArrivedCity(baseInfo.getString("arrivalCityUuid"));
			activityAirTicket.setStartingDate(baseInfo.getDate("departureDate"));
			activityAirTicket.setCountry(baseInfo.getString("lineCountryUuid"));
			activityAirTicket.setProCompany(UserUtils.getUser().getCompany().getId());
			activityAirTicket.setProductStatus(baseInfo.getIntValue("orderStatusCode"));
			if(id==null){
				activityAirTicket.setProductCode(airticketPreOrderService.genOrderNo(Context.PRODUCT_NUM_TYPE));
			}
			activityAirTicket.setOperator(baseInfo.getString("ticketId"));
			this.setOptInfo(activityAirTicket, id==null?BaseService.OPERATION_ADD:BaseService.OPERATION_UPDATE);
			mtourCommonDao.getSession().saveOrUpdate(activityAirTicket);

			activityAirTicket.setCreateBy(null);
			if(StringUtils.isNotBlank(baseInfo.getString("operatorId"))){
				activityAirTicket.setCreateBy(UserUtils.getUser(baseInfo.getString("operatorId")));
			}
		}
		//航班信息
		JSONArray flights = jsonObj.getJSONArray("flights");
		if(flights!=null){
			for(int j=0;j<flights.size();j++){
				JSONObject jsonObjSub = flights.getJSONObject(j);
				FlightInfo flight = new FlightInfo();
				flight.setAirticketId(activityAirTicket.getId());
				flight.setFlightNumber(jsonObjSub.getString("flightNo"));
				//前端多次修改数据不统一,机场信息多处理一次
				flight.setLeaveAirport(jsonObjSub.getJSONObject("departureAirport").getString("id"));
				if(StringUtils.isBlank(flight.getLeaveAirport())){
					flight.setLeaveAirport(jsonObjSub.getJSONObject("departureAirport").getString("airportUuid"));
				}
				flight.setDestinationAirpost(jsonObjSub.getJSONObject("arrivalAirport").getString("id"));
				if(StringUtils.isBlank(flight.getDestinationAirpost())){
					flight.setDestinationAirpost(jsonObjSub.getJSONObject("arrivalAirport").getString("airportUuid"));
				}
				//前端没有时间,这里组装一下
				StringBuilder startTime = new StringBuilder("");
				if(StringUtils.isNotBlank(jsonObjSub.getString("departureDate"))){
					startTime.append(jsonObjSub.getString("departureDate"));
					if(StringUtils.isNotBlank(jsonObjSub.getString("departureHour"))){
						startTime.append(" "+jsonObjSub.getString("departureHour"));
					}else{
						startTime.append(" 00");
					}
					if(StringUtils.isNotBlank(jsonObjSub.getString("departureMinute"))){
						startTime.append(":"+jsonObjSub.getString("departureMinute"));
					}else{
						startTime.append(":00");
					}
					flight.setStartTime(DateUtils.dateFormat(startTime.toString(), DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM));
				}
				
				StringBuilder arrivalTime = new StringBuilder("");
				if(StringUtils.isNotBlank(jsonObjSub.getString("arrivalDate"))){
					arrivalTime.append(jsonObjSub.getString("arrivalDate"));
					if(StringUtils.isNotBlank(jsonObjSub.getString("arrivalHour"))){
						arrivalTime.append(" "+jsonObjSub.getString("arrivalHour"));
					}else{
						arrivalTime.append(" 00");
					}
					if(StringUtils.isNotBlank(jsonObjSub.getString("arrivalMinute"))){
						arrivalTime.append(":"+jsonObjSub.getString("arrivalMinute"));
					}else{
						arrivalTime.append(":00");
					}
					flight.setArrivalTime(DateUtils.dateFormat(arrivalTime.toString(), DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM));
				}
				flight.setRemark(jsonObjSub.getString("memo"));
				flight.setActivityAirTicket(activityAirTicket);
				this.setOptInfo(flight, BaseService.OPERATION_ADD);
				mtourCommonDao.saveObj(flight);
			}
		}
		//机票订单信息
		JSONObject fee = jsonObj.getJSONObject("fee");
		order.setGroupCode(activityAirTicket.getGroupCode());//此处是空值
		order.setAirticketId(activityAirTicket.getId());
		order.setPersonNum(baseInfo.getIntValue("reservationCount"));
		if(id==null){
			order.setOrderNo(airticketPreOrderService.genOrderNo(Context.ORDER_NUM_TYPE));
		}
		order.setProductTypeId(Long.valueOf(Context.ORDER_STATUS_AIR_TICKET));
		if(order.getPaymentStatus()==1){
			order.setPaymentStatus(101);
		}
		//订单总额
		JSONArray orderAmount = fee.getJSONArray("orderAmount");
		String uuid = null;
		if(orderAmount!=null){
			uuid = UuidUtils.generUuid();
			for(int j=0;j<orderAmount.size();j++){
				JSONObject obj = orderAmount.getJSONObject(j);
				MoneyAmount amount = new MoneyAmount();
				amount.setSerialNum(uuid);
				amount.setCurrencyId(obj.getInteger("currencyUuid"));
				amount.setAmount(obj.getBigDecimal("amount"));
				amount.setExchangerate(obj.getBigDecimal("exchangeRate"));
				//amount.setExchangerate(new BigDecimal(1));//暂时支持人名币，汇率1
				amount.setOrderType(Integer.valueOf(Context.ORDER_STATUS_AIR_TICKET));
				amount.setBusindessType(Context.BUSINESS_TYPE_ORDER);
				amount.setMoneyType(Context.MONEY_TYPE_YSH);
				amount.setUid(order.getId());
				this.setOptInfo(amount, BaseService.OPERATION_ADD);
				mtourCommonDao.saveObj(amount);
			}
			order.setTotalMoney(uuid);
		}
		//应收总额
		JSONArray receivableAmount = fee.getJSONArray("receivableAmount");
		if(receivableAmount!=null){
			uuid = UuidUtils.generUuid();
			for(int j=0;j<receivableAmount.size();j++){
				JSONObject obj = receivableAmount.getJSONObject(j);
				MoneyAmount amount = new MoneyAmount();
				amount.setSerialNum(uuid);
				amount.setCurrencyId(obj.getInteger("currencyUuid"));
				amount.setAmount(obj.getBigDecimal("amount"));
				amount.setExchangerate(obj.getBigDecimal("exchangeRate"));
				//amount.setExchangerate(new BigDecimal(1));
				amount.setOrderType(Integer.valueOf(Context.ORDER_STATUS_AIR_TICKET));
				amount.setBusindessType(Context.BUSINESS_TYPE_ORDER);
				amount.setUid(order.getId());
				amount.setMoneyType(Context.MONEY_TYPE_YSYSH);
				this.setOptInfo(amount, BaseService.OPERATION_ADD);
				mtourCommonDao.saveObj(amount);
			}
			order.setOriginalTotalMoney(uuid);
		}
		//定金总额
		JSONArray frontAmount = fee.getJSONArray("totalDeposit");
		if(frontAmount!=null){
			uuid = UuidUtils.generUuid();
			for(int j=0;j<frontAmount.size();j++){
				JSONObject obj = frontAmount.getJSONObject(j);
				MoneyAmount amount = new MoneyAmount();
				amount.setSerialNum(uuid);
				amount.setCurrencyId(obj.getInteger("currencyUuid"));
				amount.setAmount(obj.getBigDecimal("amount"));
				amount.setExchangerate(obj.getBigDecimal("exchangeRate"));
				//amount.setExchangerate(new BigDecimal(1));
				amount.setOrderType(Integer.valueOf(Context.ORDER_STATUS_AIR_TICKET));
				amount.setBusindessType(Context.BUSINESS_TYPE_ORDER);
				amount.setUid(order.getId());
				amount.setMoneyType(Context.MONEY_TYPE_YSDJ);
				this.setOptInfo(amount, BaseService.OPERATION_ADD);
				mtourCommonDao.saveObj(amount);
			}
			order.setFrontMoney(uuid);
		}
		order.setComments(jsonObj.getString("memo"));
		order.setLockStatus(baseInfo.getIntValue("orderStatusCode"));
		String channelName = jsonObj.getJSONObject("reservations").getString("channelName");
		String channelCode = jsonObj.getJSONObject("reservations").getString("channelCode");
		order.setNagentName(channelName);
		order.setNagentcode(channelCode);
		if(StringUtils.isNotBlank(jsonObj.getJSONObject("reservations").getString("channelUuid"))){
			order.setAgentinfoId(jsonObj.getJSONObject("reservations").getLong("channelUuid"));
		}else{
			if (StringUtils.isBlank(channelName)){
				order.setAgentinfoId(-1L);
			}else {
				//查一下渠道表中的对应该公司的非签约渠道有没有该名称。有：把对应id放入order对象中。
				//没有：新建一个该名称的非签约渠道对象保存。把保存后生成的id放入order对象中。
				String agentSql = "SELECT a.id FROM agentinfo a WHERE a.is_uncontract='1' AND a.supplyId=? AND a.agentName=?";
				Long companyId = UserUtils.getUser().getCompany().getId();
				List<Integer> agentIdList = mtourCommonDao.findBySql(agentSql,companyId,channelName.trim());
				if (CollectionUtils.isEmpty(agentIdList)){
					String userIdStr = UserUtils.getUser().getId().toString();
					Agentinfo agentinfo = new Agentinfo(channelName,userIdStr,channelCode,companyId,"1");
					mtourCommonDao.saveObj(agentinfo);
					order.setAgentinfoId(agentinfo.getId());
				}else {
					order.setAgentinfoId((long)agentIdList.get(0));
				}
			}
		}
		this.setOptInfo(order, id==null?BaseService.OPERATION_ADD:BaseService.OPERATION_UPDATE);
		mtourCommonDao.getSession().saveOrUpdate(order);
		//大编号信息
		JSONArray invoiceOriginalGroups = fee.getJSONArray("invoiceOriginalGroups");
		if(invoiceOriginalGroups!=null && id==null){
			for(int j=0;j<invoiceOriginalGroups.size();j++){
				JSONObject invoiceObj = invoiceOriginalGroups.getJSONObject(j);
				JSONArray invoiceOriginals = invoiceObj.getJSONArray("invoiceOriginals");
				//pnr组信息
				AirticketOrderPnrGroup pnrgroup = new AirticketOrderPnrGroup();
				pnrgroup.setAirticketOrderId(order.getId().intValue());
				pnrgroup.setTicketPersonNum(baseInfo.getInteger("drawerCount"));
				pnrgroup.setPersonNum(baseInfo.getInteger("reservationCount"));
				pnrgroup.setPnrGroupIndex(0);
				this.setOptInfo(pnrgroup, BaseService.OPERATION_ADD);
				mtourCommonDao.saveObj(pnrgroup);
				if(invoiceOriginals!=null){
					for(int k=0;k<invoiceOriginals.size();k++){
						//pnr信息
						JSONObject obj = invoiceOriginals.getJSONObject(k);
						AirticketOrderPnr pnr = new AirticketOrderPnr();
						pnr.setAirticketOrderId(order.getId().intValue());
						int type = obj.getInteger("invoiceOriginalTypeCode");
						pnr.setCodeType(obj.getInteger("invoiceOriginalTypeCode"));
						if(type==0){//pnr
							pnr.setFlightPnr(obj.getString("PNR"));
						}else if(type==1){//地接社
							pnr.setFlightPnr(obj.getString("tourOperatorUuid"));
						}
						pnr.setAirline(obj.getString("airlineCompanyUuid"));
						pnr.setAirticketOrderPnrGroupUuid(pnrgroup.getUuid());
						pnr.setCostAccountNo(obj.getString("costAccountNo"));
						pnr.setCostBankName(obj.getString("costBankName"));
						pnr.setCostSupplyId(obj.getInteger("costTourOperatorUuid"));
						pnr.setCostSupplyType(obj.getString("costTourOperatorTypeCode"));
						//期限
						JSONObject subobj = obj.getJSONObject("deadline");
						if(subobj!=null){
							pnr.setTicketDeadline(DateUtils.string2Date(subobj.getString("ticketDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
							pnr.setFrontDeadline(DateUtils.string2Date(subobj.getString("depositDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
							pnr.setListDeadline(DateUtils.string2Date(subobj.getString("listDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
							pnr.setRenameDeadline(DateUtils.string2Date(subobj.getString("renameDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
							pnr.setCancelDeadline(DateUtils.string2Date(subobj.getString("cancelDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
						}
						this.setOptInfo(pnr, BaseService.OPERATION_ADD);
						mtourCommonDao.saveObj(pnr);
						//航段信息
						JSONArray airlines = obj.getJSONArray("airlines");
						if(airlines!=null){
							for(int s=0;s<airlines.size();s++){
								JSONObject objtriple = airlines.getJSONObject(s);
								AirticketOrderPnrAirline airline = new AirticketOrderPnrAirline();
								airline.setAirticketOrderId(order.getId().intValue());
								airline.setAirticketOrderPnrUuid(pnr.getUuid());
								airline.setAirlineName(objtriple.getString("airlineName"));
								this.setOptInfo(airline, BaseService.OPERATION_ADD);
								mtourCommonDao.saveObj(airline);
								//航段价格表--成本价
								JSONArray costs = objtriple.getJSONArray("costs");
								if(costs!=null){
									for(int t=0;t<costs.size();t++){
										JSONObject objmultiple = costs.getJSONObject(t);
										AirticketOrderPnrAirlinePrice price = new AirticketOrderPnrAirlinePrice();
										price.setAirticketOrderId(order.getId().intValue());
										price.setAirticketOrderPnrAirlineUuid(airline.getUuid());
										price.setAirticketOrderPnrUuid(pnr.getUuid());
										price.setCurrencyId(objmultiple.getInteger("currencyUuid"));
										price.setPersonNum(objmultiple.getInteger("peopleCount"));
										price.setFrontMoney(objmultiple.getDouble("totalDeposit"));
										price.setExchangerate(objmultiple.getDoubleValue("exchangeRate"));
										price.setPrice(objmultiple.getDouble("costUnitPrice"));
										price.setPriceType(0);
										this.setOptInfo(price, BaseService.OPERATION_ADD);
										mtourCommonDao.saveObj(price);
										
										//新的保存成本记录
										CostRecord costRecord = new CostRecord();
										costRecord.setActivityId(activityAirTicket.getId());
										costRecord.setPayStatus(2);
										costRecord.setOrderId(order.getId());
										costRecord.setOrderType(7);
										costRecord.setReviewType(0);
										costRecord.setName(airline.getAirlineName());
										costRecord.setQuantity(objmultiple.getInteger("peopleCount"));
										costRecord.setPrice(objmultiple.getBigDecimal("costUnitPrice"));
										costRecord.setSupplyType(1);
										if(StringUtils.isNotBlank(pnr.getCostSupplyType())){
											costRecord.setSupplierType(Integer.parseInt(pnr.getCostSupplyType()));
										}
										costRecord.setSupplyName(((SupplierInfo) supplierInfoDao.getSession().load(SupplierInfo.class, pnr.getCostSupplyId().longValue())).getSupplierName());
										costRecord.setSupplyId(pnr.getCostSupplyId());
										costRecord.setRate(objmultiple.getBigDecimal("exchangeRate"));
										costRecord.setCurrencyId(objmultiple.getInteger("currencyUuid"));
										costRecord.setCurrencyAfter(currencyService.getRMBCurrencyId().getId().intValue());
										costRecord.setPriceAfter(objmultiple.getBigDecimal("peopleCount").multiply(objmultiple.getBigDecimal("costUnitPrice")).multiply(objmultiple.getBigDecimal("exchangeRate")).setScale(2, BigDecimal.ROUND_HALF_UP));
										costRecord.setIsJoin(pnr.getCodeType());
										costRecord.setBigCode(pnr.getFlightPnr());
										costRecord.setBankName(pnr.getCostBankName());
										costRecord.setBankAccount(pnr.getCostAccountNo());
										costRecord.setReviewType(0);
										costRecord.setBudgetType(1);
										costRecord.setOverseas(0);
										costRecord.setCreateBy(UserUtils.getUser());
										costRecord.setPnrUuid(pnr.getUuid());
										this.setOptInfo(costRecord, BaseService.OPERATION_ADD);
										mtourCommonDao.saveObj(costRecord);
										serialNumberService.genSerialNumber(SerialNumberService.TABLENAME_COST_RECORD, costRecord.getId().intValue());
									}
								}
								//航段价格表--外部价
								JSONArray salePrices = objtriple.getJSONArray("salePrices");
								if(salePrices!=null){
									for(int t=0;t<salePrices.size();t++){
										JSONObject objmultiple = salePrices.getJSONObject(t);
										AirticketOrderPnrAirlinePrice price = new AirticketOrderPnrAirlinePrice();
										price.setAirticketOrderId(order.getId().intValue());
										price.setAirticketOrderPnrAirlineUuid(airline.getUuid());
										price.setAirticketOrderPnrUuid(pnr.getUuid());
										price.setCurrencyId(objmultiple.getInteger("currencyUuid"));
										price.setExchangerate(objmultiple.getDoubleValue("exchangeRate"));
										price.setPrice(objmultiple.getDouble("costUnitPrice"));
										price.setPersonNum(objmultiple.getInteger("peopleCount"));
										price.setFrontMoney(objmultiple.getDouble("totalDeposit"));
										price.setPriceType(1);
										this.setOptInfo(price, BaseService.OPERATION_ADD);
										mtourCommonDao.saveObj(price);
									}
								}
							}
						}
					}
				}
			}
		}else if(invoiceOriginalGroups!=null && id!=null){
			List<AirticketOrderPnr> pnr = airticketOrderPnrService.findByOrderId(id);
			for(int j=0;j<invoiceOriginalGroups.size();j++){
				JSONObject invoiceObj = invoiceOriginalGroups.getJSONObject(j);
				JSONArray invoiceOriginals = invoiceObj.getJSONArray("invoiceOriginals");
				if(invoiceOriginals.size()>0){
					//pnr信息
					JSONObject obj = invoiceOriginals.getJSONObject(0);
					//期限
					JSONObject subobj = obj.getJSONObject("deadline");
					if(subobj!=null){
						AirticketOrderPnr temp = pnr.get(j);
						temp.setTicketDeadline(DateUtils.string2Date(subobj.getString("ticketDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
						temp.setFrontDeadline(DateUtils.string2Date(subobj.getString("depositDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
						temp.setListDeadline(DateUtils.string2Date(subobj.getString("listDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
						temp.setRenameDeadline(DateUtils.string2Date(subobj.getString("renameDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
						temp.setCancelDeadline(DateUtils.string2Date(subobj.getString("cancelDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
						this.setOptInfo(temp, BaseService.OPERATION_UPDATE);
						mtourCommonDao.updateObj(temp);
					}
				}
			}
		}
		if(id==null){
			//成本追加信息
			JSONObject priceChange = fee.getJSONObject("priceChange");
			if(priceChange!=null){
				JSONArray additionalCost = priceChange.getJSONArray("additionalCosts");
				if(additionalCost!=null){
					for(int m=0;m<additionalCost.size();m++){
						JSONObject objsub = additionalCost.getJSONObject(m);
						AirticketOrderMoneyAmount price = new AirticketOrderMoneyAmount();
						price.setAirticketOrderId(order.getId().intValue());
						price.setMoneyType(3);
						price.setSerialNum(UuidUtils.generUuid());
						price.setExchangerate(objsub.getDoubleValue("exchangeRate"));
						price.setCurrencyId(objsub.getIntValue("currencyUuid"));
						price.setAmount(objsub.getDouble("amount"));
						price.setFundsName(objsub.getString("fundsName"));
						price.setStatus(1);
						price.setPaystatus(0);
						this.setOptInfo(price, BaseService.OPERATION_ADD);
						mtourCommonDao.saveObj(price);
						serialNumberService.genSerialNumber(SerialNumberService.TABLENAME_AIRTICKET_ORDER_MONEYAMOUNT, price.getId());
					}
				}
				//外报价修改表
				JSONArray salePrices = priceChange.getJSONArray("salePrices");
				if(salePrices!=null){
					for(int m=0;m<salePrices.size();m++){
						JSONObject objsub = salePrices.getJSONObject(m);
						AirticketOrderChangePrice price = new AirticketOrderChangePrice();
						price.setAirticketOrderId(order.getId().intValue());
						price.setExchangerate(objsub.getDoubleValue("exchangeRate"));
						if("+".equals(objsub.getString("changeType")) || "0".equals(objsub.getString("changeType"))){
							price.setComputeType(0);
						}else{
							price.setComputeType(1);
						}
						price.setCurrencyId(objsub.getIntValue("currencyUuid"));
						price.setPrice(objsub.getDouble("amount"));
						price.setMemo(objsub.getString("memo"));
						this.setOptInfo(price, BaseService.OPERATION_ADD);
						mtourCommonDao.saveObj(price);
					}
				}
			}
		}else{
			JSONObject priceChange = fee.getJSONObject("priceChange");
			//外报价修改表
			JSONArray salePrices = priceChange.getJSONArray("salePrices");
			if(salePrices!=null){
				//先删除，再新增
				orderChangePriceService.deleteByOrderId(id);
				for(int m=0;m<salePrices.size();m++){
					JSONObject objsub = salePrices.getJSONObject(m);
					AirticketOrderChangePrice price = new AirticketOrderChangePrice();
					price.setAirticketOrderId(order.getId().intValue());
					price.setExchangerate(objsub.getDoubleValue("exchangeRate"));
					if("+".equals(objsub.getString("changeType")) || "0".equals(objsub.getString("changeType"))){
						price.setComputeType(0);
					}else{
						price.setComputeType(1);
					}
					price.setCurrencyId(objsub.getIntValue("currencyUuid"));
					price.setPrice(objsub.getDouble("amount"));
					price.setMemo(objsub.getString("memo"));
					this.setOptInfo(price, BaseService.OPERATION_ADD);
					mtourCommonDao.saveObj(price);
				}
			}
		}
		//预订人信息
		JSONObject reservations = jsonObj.getJSONObject("reservations");
		if(reservations!=null){
			JSONArray contactsArr = reservations.getJSONArray("contacts");
			if(contactsArr!=null){
				for(int j=0;j<contactsArr.size();j++){
					JSONObject jsonObjSub = contactsArr.getJSONObject(j);
					OrderContacts orderContacts = new OrderContacts();
					orderContacts.setOrderId(order.getId());
					if(StringUtils.isNotBlank(reservations.getString("channelUuid"))){
						orderContacts.setAgentId(reservations.getLongValue("channelUuid"));
					}else{
						orderContacts.setAgentId(-1L);
					}
					orderContacts.setContactsName(jsonObjSub.getString("name"));
					orderContacts.setContactsTel(jsonObjSub.getString("phone"));
					orderContacts.setContactsAddress(jsonObjSub.getString("address"));
					orderContacts.setContactsTixedTel(jsonObjSub.getString("tel"));
					orderContacts.setContactsZipCode(jsonObjSub.getString("zipcode"));
					orderContacts.setContactsFax(jsonObjSub.getString("tax"));
					orderContacts.setOrderType(Integer.valueOf(Context.ORDER_STATUS_AIR_TICKET));
					this.setOptInfo(orderContacts, BaseService.OPERATION_ADD);
					mtourCommonDao.saveObj(orderContacts);
				}
			}
		}
		//上传附件信息attachment
		JSONArray attachment = jsonObj.getJSONArray("attachment");
		if(attachment!=null){
			for(int h=0;h<attachment.size();h++){
				JSONObject jsonObjSub = attachment.getJSONObject(h);
				AirTicketFile file = new AirTicketFile();
				file.setActivityAirTicket(activityAirTicket);
				Long docId = jsonObjSub.getLong("attachmentUuid");
				if(docId!=null){
					DocInfo doc = (DocInfo) mtourCommonDao.getSession().load(DocInfo.class, docId);
					file.setDocInfo(doc);
				}
				file.setFileName(jsonObjSub.getString("fileName"));
				this.setOptInfo(file, BaseService.OPERATION_ADD);
				mtourCommonDao.saveObj(file);
			}
		}
		//游客信息
		JSONArray travelers = jsonObj.getJSONArray("travelers");
		if(travelers!=null){
			for(int h=0;h<travelers.size();h++){
				JSONObject travelersObj = travelers.getJSONObject(h);
				Traveler traveler = new Traveler();
				traveler.setOrderType(7);
				traveler.setOrderId(order.getId());
				traveler.setName(travelersObj.getString("name"));
				traveler.setNameSpell(travelersObj.getString("englishName"));
				traveler.setSex(travelersObj.getIntValue("sexCode"));
				traveler.setSrcPrice(travelersObj.getBigDecimal("amount"));
				Currency c = currencyService.findCurrency(Long.parseLong(travelersObj.getString("currencyUuid")));
				traveler.setSrcPriceCurrency(c);
				traveler.setRemark(travelersObj.getString("memo"));
				this.setOptInfo(traveler, BaseService.OPERATION_ADD);
				mtourCommonDao.saveObj(traveler);
				//签证信息
				JSONArray visas = travelersObj.getJSONArray("visas");
				if(visas!=null){
					for(int j=0;j<visas.size();j++){
						JSONObject visaObj = visas.getJSONObject(j);
						TravelerVisa travelervisa = new TravelerVisa();
						travelervisa.setTraveler(traveler);
						JSONObject country = visaObj.getJSONObject("country");
						if(country!=null){
							Long areaId = visaObj.getJSONObject("country").getLong("id");
							if(areaId==null && StringUtils.isNotBlank(visaObj.getJSONObject("country").getString("countryId"))){
								areaId = visaObj.getJSONObject("country").getLong("countryId");
							}
							if(areaId!=null){
								Area area = (Area) mtourCommonDao.getSession().load(Area.class, areaId);
								travelervisa.setApplyCountry(area);
							}
						}
						if(StringUtils.isNotBlank(visaObj.getString("visaTypeCode"))){
							travelervisa.setVisaTypeId(visaObj.getInteger("visaTypeCode"));
						}
						this.setOptInfo(travelervisa, BaseService.OPERATION_ADD);
						mtourCommonDao.saveObj(travelervisa);
					}
				}
				//证件信息
				JSONArray credentials = travelersObj.getJSONArray("credentials");
				if(credentials!=null){
					for(int j=0;j<credentials.size();j++){
						JSONObject paperObj = credentials.getJSONObject(j);
						TravelerPapersType travelerpaper = new TravelerPapersType();
						travelerpaper.setOrderId(order.getId().intValue());
						travelerpaper.setTravelerId(traveler.getId().intValue());
						if(StringUtils.isNotBlank(paperObj.getString("credentialsTypeCode"))){
							travelerpaper.setPapersType(paperObj.getInteger("credentialsTypeCode"));
						}
						travelerpaper.setIdCard(paperObj.getString("credentialsNo"));
						if(StringUtils.isNotBlank(paperObj.getString("credentialsExpire"))){
							travelerpaper.setValidityDate(paperObj.getDate("credentialsExpire"));
						}
						this.setOptInfo(travelerpaper, BaseService.OPERATION_ADD);
						mtourCommonDao.saveObj(travelerpaper);
					}
				}
			}
		}
		//这里更新一下产品团号
		 if(id==null){
		     activityAirTicket.setGroupCode(airticketPreOrderService.genGroupCode(order));
		     order.setGroupCode(activityAirTicket.getGroupCode());//订单中的团号更新
		     mtourCommonDao.updateObj(activityAirTicket);
		 }
		Map<String,Map<String,String>> outmap = new HashMap<String,Map<String,String>>();
		Map<String,String> submap = new HashMap<String,String>();
		submap.put("orderUuid", order.getId().toString());
		if(order.getLockStatus()==0){
			submap.put("orderStatus", "正常");
		}else if(order.getLockStatus()==2){
			submap.put("orderStatus", "取消");
		}else if(order.getLockStatus()==3){
			submap.put("orderStatus", "草稿");
		}
		submap.put("orderStatusCode", String.valueOf(order.getLockStatus()));
		outmap.put("baseInfo", submap);
		out.setData(outmap);
		out.setResponseCode4Success();
		return out;
	}
	
	/**
	 * 删除原有的子表信息
	 * @param order
	 * @param activityAirTicket
	 */
	private void removeSubInfo(AirticketOrder order,
			ActivityAirTicket activityAirTicket) {
		List<FlightInfo> listflight = flightService.findByFlightInfoByAirTicketId(activityAirTicket.getId());
		if(!listflight.isEmpty()){
			flightService.delFlightInfoList(listflight);
		}
		if(StringUtils.isNotBlank(order.getTotalMoney())){
			moneyAmountService.delMoneyAmountBySerialNum(order.getTotalMoney());
		}
		if(StringUtils.isNotBlank(order.getOriginalTotalMoney())){
			moneyAmountService.delMoneyAmountBySerialNum(order.getOriginalTotalMoney());
		}
		if(StringUtils.isNotBlank(order.getFrontMoney())){
			moneyAmountService.delMoneyAmountBySerialNum(order.getFrontMoney());
		}
		Long id = order.getId();
//		orderMoneyAmountService.deleteByOrderId(id);
		orderContactsDao.deleteOrderContactsByOrderIdAndOrderType(id, Integer.parseInt(Context.ORDER_STATUS_AIR_TICKET));
		travelerDao.deleteTravelerByOrderId(id);
		travelerVisaService.deleteByOrderId(id);
		papersTypeService.deleteByOrderId(id);
		papersTypeService.deleteFileByActivityId(activityAirTicket.getId());
	}

	/**
	 * 解析baseInfo对象中的key名称的数组名称。获取其中的userId，然后返回以逗号分隔的userId字符串。 yudong.xu 2016.6.21
	 * @param baseInfo
	 * @param key 待解析的数组对象名称，销售或者计调。根据前端传入的json数组名称而定。
     * @return
     */
	private String parseBaseInfo(JSONObject baseInfo,String key){
		JSONArray array = baseInfo.getJSONArray(key);
		if (null == array || array.size() == 0)
			return null;
		int size = array.size();
		StringBuilder userIds = new StringBuilder();
		for (int i = 0; i < size - 1; i++) {
			JSONObject jsonObject = array.getJSONObject(i);
			userIds.append(jsonObject.getString("userId")).append(",");
		}
		String lastUserId = array.getJSONObject(size - 1).getString("userId");
		userIds.append(lastUserId);
		return userIds.toString();
	}
	/**
	 * 机票订单追散保存
	 * @author hhx
	 * @param input
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public BaseOut4MT addToAirticketOrder(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT();
		JSONObject jsonObj = JSONObject.parseObject(input.getParam());
		if(jsonObj!=null && jsonObj.getLong("orderUuid")!=null){
			//更新原订单
			AirticketOrder order = airTicketOrderService.getAirticketorderById(jsonObj.getLong("orderUuid"));
			order.setPersonNum(order.getPersonNum()+jsonObj.getIntValue("addFITCount"));
			this.setOptInfo(order, BaseService.OPERATION_UPDATE);
			mtourCommonDao.updateObj(order);
			//订单总额同币种追加
			JSONArray addAmountList = jsonObj.getJSONArray("totalSalePrice");
			if(addAmountList!=null){
				List<MoneyAmount> moneyAmountList = moneyAmountService.findAmountBySerialNum(order.getTotalMoney());
				Map<Integer, MoneyAmount> map = new TreeMap<Integer, MoneyAmount>();
				String amountUuid = null;
				if (!moneyAmountList.isEmpty()) {
					amountUuid = moneyAmountList.get(0).getSerialNum();
					for (MoneyAmount amount : moneyAmountList) {
						map.put(amount.getCurrencyId(), amount);
					}
				}
				for(int j=0;j<addAmountList.size();j++){
					JSONObject obj = addAmountList.getJSONObject(j);
					Integer currencyId = obj.getInteger("currencyUuid");
					MoneyAmount amount = map.get(currencyId);
					if(amount!=null&&(amount.getExchangerate().compareTo(obj.getBigDecimal("exchangeRate"))==0)){
						amount.setAmount(amount.getAmount().add(obj.getBigDecimal("amount")).setScale(2, BigDecimal.ROUND_HALF_UP));
						this.setOptInfo(amount, BaseService.OPERATION_UPDATE);
						mtourCommonDao.updateObj(amount);
					}else{
						amount = new MoneyAmount();
						amount.setCurrencyId(currencyId);
						amount.setSerialNum(amountUuid);
						amount.setAmount(obj.getBigDecimal("amount"));
						amount.setExchangerate(obj.getBigDecimal("exchangeRate"));
						amount.setOrderType(Integer.valueOf(Context.ORDER_STATUS_AIR_TICKET));
						amount.setBusindessType(Context.BUSINESS_TYPE_ORDER);
						amount.setUid(order.getId());
						amount.setMoneyType(Context.MONEY_TYPE_YSH);
						//获取创建订单时的币种信息
						/*Currency currency = this.getOriginalCurrency(order.getId(), String.valueOf(currencyId));
						amount.setExchangerate(currency.getConvertLowest());*/
						this.setOptInfo(amount, BaseService.OPERATION_ADD);
						mtourCommonDao.saveObj(amount);
					}
				}
			}
			//订金同币种追加
			JSONArray frontAmountList = jsonObj.getJSONArray("totalDeposit");
			if(frontAmountList!=null){
				List<MoneyAmount> moneyAmountList = moneyAmountService.findAmountBySerialNum(order.getFrontMoney());
				Map<Integer, MoneyAmount> map = new TreeMap<Integer, MoneyAmount>();
				String amountUuid = null;
				if (!moneyAmountList.isEmpty()) {
					amountUuid = moneyAmountList.get(0).getSerialNum();
					for (MoneyAmount amount : moneyAmountList) {
						map.put(amount.getCurrencyId(), amount);
					}
				}
				for(int j=0;j<frontAmountList.size();j++){
					JSONObject obj = frontAmountList.getJSONObject(j);
					Integer currencyId = obj.getInteger("currencyUuid");
					MoneyAmount amount = map.get(currencyId);
					if(amount!=null&&(amount.getExchangerate().compareTo(obj.getBigDecimal("exchangeRate"))==0)){
						amount.setAmount(amount.getAmount().add(obj.getBigDecimal("amount")).setScale(2, BigDecimal.ROUND_HALF_UP));
						this.setOptInfo(amount, BaseService.OPERATION_UPDATE);
						mtourCommonDao.updateObj(amount);
					}else{
						amount = new MoneyAmount();
						amount.setCurrencyId(currencyId);
						amount.setSerialNum(amountUuid);
						amount.setAmount(obj.getBigDecimal("amount"));
						amount.setExchangerate(obj.getBigDecimal("exchangeRate"));
						amount.setOrderType(Integer.valueOf(Context.ORDER_STATUS_AIR_TICKET));
						amount.setBusindessType(Context.BUSINESS_TYPE_ORDER);
						amount.setUid(order.getId());
						this.setOptInfo(amount, BaseService.OPERATION_ADD);
						mtourCommonDao.saveObj(amount);
					}
				}
			}
			
			//pnr组信息
			AirticketOrderPnrGroup pnrgroup = new AirticketOrderPnrGroup();
			pnrgroup.setAirticketOrderId(order.getId().intValue());
			pnrgroup.setTicketPersonNum(0);
			pnrgroup.setPersonNum(jsonObj.getInteger("addFITCount"));
			List<AirticketOrderPnrGroup> tempList = pnrGroupService.getListByOrderId(order.getId().toString());
			if(CollectionUtils.isNotEmpty(tempList)){
				pnrgroup.setPnrGroupIndex(tempList.size());
			}
			this.setOptInfo(pnrgroup, BaseService.OPERATION_ADD);
			mtourCommonDao.saveObj(pnrgroup);
			JSONArray invoiceOriginals = jsonObj.getJSONArray("invoiceOriginals");
			if(invoiceOriginals!=null){
				for(int k=0;k<invoiceOriginals.size();k++){
					//pnr信息
					JSONObject obj = invoiceOriginals.getJSONObject(k);
					AirticketOrderPnr pnr = new AirticketOrderPnr();
					pnr.setAirticketOrderPnrGroupUuid(pnrgroup.getUuid());
					pnr.setAirticketOrderId(order.getId().intValue());
					int type = obj.getInteger("invoiceOriginalTypeCode");
					pnr.setCodeType(obj.getInteger("invoiceOriginalTypeCode"));
					if(type==0){//pnr
						pnr.setFlightPnr(obj.getString("PNR"));
					}else if(type==1){//地接社
						pnr.setFlightPnr(obj.getString("tourOperatorUuid"));
					}
					pnr.setAirline(obj.getString("airlineCompanyUuid"));
					pnr.setCostAccountNo(obj.getString("costAccountNo"));
					pnr.setCostBankName(obj.getString("costBankName"));
					pnr.setCostSupplyId(obj.getInteger("costTourOperatorUuid"));
					pnr.setCostSupplyType(obj.getString("costTourOperatorTypeCode"));
					//期限
					JSONObject subobj = obj.getJSONObject("deadline");
					if(subobj!=null){
						pnr.setTicketDeadline(DateUtils.string2Date(subobj.getString("ticketDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
						pnr.setFrontDeadline(DateUtils.string2Date(subobj.getString("depositDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
						pnr.setListDeadline(DateUtils.string2Date(subobj.getString("listDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
						pnr.setRenameDeadline(DateUtils.string2Date(subobj.getString("renameDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
						pnr.setCancelDeadline(DateUtils.string2Date(subobj.getString("cancelDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
					}
					this.setOptInfo(pnr, BaseService.OPERATION_ADD);
					mtourCommonDao.saveObj(pnr);
					//航段信息
					JSONArray airlines = obj.getJSONArray("airlines");
					if(airlines!=null){
						for(int s=0;s<airlines.size();s++){
							JSONObject objtriple = airlines.getJSONObject(s);
							AirticketOrderPnrAirline airline = new AirticketOrderPnrAirline();
							airline.setAirticketOrderId(order.getId().intValue());
							airline.setAirticketOrderPnrUuid(pnr.getUuid());
							airline.setAirlineName(objtriple.getString("airlineName"));
							this.setOptInfo(airline, BaseService.OPERATION_ADD);
							mtourCommonDao.saveObj(airline);
							//航段价格表--成本价
							JSONArray costs = objtriple.getJSONArray("costs");
							if(costs!=null){
								for(int t=0;t<costs.size();t++){
									JSONObject objmultiple = costs.getJSONObject(t);
									//新的保存成本记录
									CostRecord costRecord = new CostRecord();
									costRecord.setActivityId(order.getAirticketId());
									costRecord.setPayStatus(2);
									costRecord.setOrderId(order.getId());
									costRecord.setOrderType(7);
									costRecord.setReviewType(0);
									costRecord.setName("成本");
									//冗余存放 航空公司二字码和航段名称，如果当前成本已付款则不会更新任何信息否则会同步更新
									//update by zhanghao 20160109 
									costRecord.setAirline(pnr.getAirline());
									costRecord.setAirlineName(airline.getAirlineName());
									//冗余存放 航空公司二字码和航段名称，如果当前成本已付款则不会更新任何信息否则会同步更新
									costRecord.setAirlineUuid(airline.getUuid());
									costRecord.setQuantity(objmultiple.getInteger("peopleCount"));
									costRecord.setPrice(objmultiple.getBigDecimal("costUnitPrice"));
									costRecord.setSupplyType(1);
									if(StringUtils.isNotBlank(pnr.getCostSupplyType())){
										costRecord.setSupplierType(Integer.parseInt(pnr.getCostSupplyType()));
									}
									costRecord.setSupplyName(((SupplierInfo) supplierInfoDao.getSession().load(SupplierInfo.class, pnr.getCostSupplyId().longValue())).getSupplierName());
									costRecord.setSupplyId(pnr.getCostSupplyId());
									costRecord.setRate(objmultiple.getBigDecimal("exchangeRate"));
									costRecord.setCurrencyId(objmultiple.getInteger("currencyUuid"));
									costRecord.setCurrencyAfter(currencyService.getRMBCurrencyId().getId().intValue());
									costRecord.setPriceAfter(objmultiple.getBigDecimal("peopleCount").multiply(objmultiple.getBigDecimal("costUnitPrice")).multiply(objmultiple.getBigDecimal("exchangeRate")).setScale(2, BigDecimal.ROUND_HALF_UP));
									costRecord.setIsJoin(pnr.getCodeType());
									costRecord.setBigCode(pnr.getFlightPnr());
									costRecord.setBankName(pnr.getCostBankName());
									costRecord.setBankAccount(pnr.getCostAccountNo());
									costRecord.setReviewType(0);
									costRecord.setBudgetType(1);
									costRecord.setOverseas(0);
									costRecord.setCreateBy(UserUtils.getUser());
									costRecord.setPnrUuid(pnr.getUuid());
									this.setOptInfo(costRecord, BaseService.OPERATION_ADD);
									mtourCommonDao.saveObj(costRecord);
									serialNumberService.genSerialNumber(SerialNumberService.TABLENAME_COST_RECORD, costRecord.getId().intValue());
									//航段中的成本记录表
									AirticketOrderPnrAirlinePrice price = new AirticketOrderPnrAirlinePrice();
									price.setAirticketOrderId(order.getId().intValue());
									price.setAirticketOrderPnrAirlineUuid(airline.getUuid());
									price.setAirticketOrderPnrUuid(pnr.getUuid());
									price.setCurrencyId(objmultiple.getInteger("currencyUuid"));
									price.setPersonNum(objmultiple.getInteger("peopleCount"));
									price.setFrontMoney(objmultiple.getDouble("totalDeposit"));
									price.setExchangerate(objmultiple.getDouble("exchangeRate"));
									price.setPrice(objmultiple.getDouble("costUnitPrice"));
									price.setPriceType(0);
									price.setCostRecordUuid(costRecord.getUuid());
									this.setOptInfo(price, BaseService.OPERATION_ADD);
									mtourCommonDao.saveObj(price);
								}
							}
							//航段价格表--外部价
							JSONArray salePrices = objtriple.getJSONArray("salePrices");
							if(salePrices!=null){
								for(int t=0;t<salePrices.size();t++){
									JSONObject objmultiple = salePrices.getJSONObject(t);
									AirticketOrderPnrAirlinePrice price = new AirticketOrderPnrAirlinePrice();
									price.setAirticketOrderId(order.getId().intValue());
									price.setAirticketOrderPnrAirlineUuid(airline.getUuid());
									price.setAirticketOrderPnrUuid(pnr.getUuid());
									price.setCurrencyId(objmultiple.getInteger("currencyUuid"));
									price.setPersonNum(objmultiple.getInteger("peopleCount"));
									price.setFrontMoney(objmultiple.getDouble("totalDeposit"));
									price.setExchangerate(objmultiple.getDouble("exchangeRate"));
									price.setPrice(objmultiple.getDouble("costUnitPrice"));
									price.setPriceType(1);
									this.setOptInfo(price, BaseService.OPERATION_ADD);
									mtourCommonDao.saveObj(price);
								}
							}
						}
					}
				}
			}
			out.setResponseCode4Success();
		}else{
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("订单id为空,程序异常.");
		}
		return out;
	}
	
	/**
	 * 机票订单追散保存(原)
	 * @author hhx
	 * @param input
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public BaseOut4MT addToAirticketOrderForCommon(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT();
		JSONObject jsonObj = JSONObject.parseObject(input.getParam());
		if(jsonObj!=null && jsonObj.getLong("orderUuid")!=null){
			//更新原订单
			AirticketOrder order = airTicketOrderService.getAirticketorderById(jsonObj.getLong("orderUuid"));
			order.setPersonNum(order.getPersonNum()+jsonObj.getIntValue("addFITCount"));
			this.setOptInfo(order, BaseService.OPERATION_UPDATE);
			mtourCommonDao.updateObj(order);
			//订单总额同币种追加
			JSONArray addAmountList = jsonObj.getJSONArray("totalSalePrice");
			if(addAmountList!=null){
				List<MoneyAmount> moneyAmountList = moneyAmountService.findAmountBySerialNum(order.getTotalMoney());
				Map<Integer, MoneyAmount> map = new TreeMap<Integer, MoneyAmount>();
				String amountUuid = null;
				if (!moneyAmountList.isEmpty()) {
					amountUuid = moneyAmountList.get(0).getSerialNum();
					for (MoneyAmount amount : moneyAmountList) {
						map.put(amount.getCurrencyId(), amount);
					}
				}
				for(int j=0;j<addAmountList.size();j++){
					JSONObject obj = addAmountList.getJSONObject(j);
					Integer currencyId = obj.getInteger("currencyUuid");
					MoneyAmount amount = map.get(currencyId);
					if(amount==null){
						amount = new MoneyAmount();
						amount.setCurrencyId(currencyId);
						amount.setSerialNum(amountUuid);
						amount.setAmount(obj.getBigDecimal("amount"));
						amount.setOrderType(Integer.valueOf(Context.ORDER_STATUS_AIR_TICKET));
						amount.setBusindessType(Context.BUSINESS_TYPE_ORDER);
						amount.setUid(order.getId());
						amount.setMoneyType(Context.MONEY_TYPE_YSH);
						//获取创建订单时的币种信息
						Currency currency = this.getOriginalCurrency(order.getId(), String.valueOf(currencyId));
						amount.setExchangerate(currency.getConvertLowest());
						
						this.setOptInfo(amount, BaseService.OPERATION_ADD);
						mtourCommonDao.saveObj(amount);
					}else{
						amount.setAmount(amount.getAmount().add(obj.getBigDecimal("amount")).setScale(2, BigDecimal.ROUND_HALF_UP));
						this.setOptInfo(amount, BaseService.OPERATION_UPDATE);
						mtourCommonDao.updateObj(amount);
					}
				}
			}
			//订金同币种追加
			JSONArray frontAmountList = jsonObj.getJSONArray("totalDeposit");
			if(frontAmountList!=null){
				List<MoneyAmount> moneyAmountList = moneyAmountService.findAmountBySerialNum(order.getFrontMoney());
				Map<Integer, MoneyAmount> map = new TreeMap<Integer, MoneyAmount>();
				String amountUuid = null;
				if (!moneyAmountList.isEmpty()) {
					amountUuid = moneyAmountList.get(0).getSerialNum();
					for (MoneyAmount amount : moneyAmountList) {
						map.put(amount.getCurrencyId(), amount);
					}
				}
				for(int j=0;j<frontAmountList.size();j++){
					JSONObject obj = frontAmountList.getJSONObject(j);
					Integer currencyId = obj.getInteger("currencyUuid");
					MoneyAmount amount = map.get(currencyId);
					if(amount==null){
						amount = new MoneyAmount();
						amount.setCurrencyId(currencyId);
						amount.setSerialNum(amountUuid);
						amount.setAmount(obj.getBigDecimal("amount"));
						amount.setOrderType(Integer.valueOf(Context.ORDER_STATUS_AIR_TICKET));
						amount.setBusindessType(Context.BUSINESS_TYPE_ORDER);
						amount.setUid(order.getId());
						this.setOptInfo(amount, BaseService.OPERATION_ADD);
						mtourCommonDao.saveObj(amount);
					}else{
						amount.setAmount(amount.getAmount().add(obj.getBigDecimal("amount")).setScale(2, BigDecimal.ROUND_HALF_UP));
						this.setOptInfo(amount, BaseService.OPERATION_UPDATE);
						mtourCommonDao.updateObj(amount);
					}
				}
			}
			
			//pnr组信息
			AirticketOrderPnrGroup pnrgroup = new AirticketOrderPnrGroup();
			pnrgroup.setAirticketOrderId(order.getId().intValue());
			pnrgroup.setTicketPersonNum(0);
			pnrgroup.setPersonNum(jsonObj.getInteger("addFITCount"));
			List<AirticketOrderPnrGroup> tempList = pnrGroupService.getListByOrderId(order.getId().toString());
			if(CollectionUtils.isNotEmpty(tempList)){
				pnrgroup.setPnrGroupIndex(tempList.size());
			}
			this.setOptInfo(pnrgroup, BaseService.OPERATION_ADD);
			mtourCommonDao.saveObj(pnrgroup);
			JSONArray invoiceOriginals = jsonObj.getJSONArray("invoiceOriginals");
			if(invoiceOriginals!=null){
				for(int k=0;k<invoiceOriginals.size();k++){
					//pnr信息
					JSONObject obj = invoiceOriginals.getJSONObject(k);
					AirticketOrderPnr pnr = new AirticketOrderPnr();
					pnr.setAirticketOrderPnrGroupUuid(pnrgroup.getUuid());
					pnr.setAirticketOrderId(order.getId().intValue());
					int type = obj.getInteger("invoiceOriginalTypeCode");
					pnr.setCodeType(obj.getInteger("invoiceOriginalTypeCode"));
					if(type==0){//pnr
						pnr.setFlightPnr(obj.getString("PNR"));
					}else if(type==1){//地接社
						pnr.setFlightPnr(obj.getString("tourOperatorUuid"));
					}
					pnr.setAirline(obj.getString("airlineCompanyUuid"));
					pnr.setCostAccountNo(obj.getString("costAccountNo"));
					pnr.setCostBankName(obj.getString("costBankName"));
					pnr.setCostSupplyId(obj.getInteger("costTourOperatorUuid"));
					pnr.setCostSupplyType(obj.getString("costTourOperatorTypeCode"));
					//期限
					JSONObject subobj = obj.getJSONObject("deadline");
					if(subobj!=null){
						pnr.setTicketDeadline(DateUtils.string2Date(subobj.getString("ticketDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
						pnr.setFrontDeadline(DateUtils.string2Date(subobj.getString("depositDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
						pnr.setListDeadline(DateUtils.string2Date(subobj.getString("listDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
						pnr.setRenameDeadline(DateUtils.string2Date(subobj.getString("renameDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
						pnr.setCancelDeadline(DateUtils.string2Date(subobj.getString("cancelDeadline"), DateUtils.DATE_PATTERN_YYYY_MM_DD));
					}
					this.setOptInfo(pnr, BaseService.OPERATION_ADD);
					mtourCommonDao.saveObj(pnr);
					//航段信息
					JSONArray airlines = obj.getJSONArray("airlines");
					if(airlines!=null){
						for(int s=0;s<airlines.size();s++){
							JSONObject objtriple = airlines.getJSONObject(s);
							AirticketOrderPnrAirline airline = new AirticketOrderPnrAirline();
							airline.setAirticketOrderId(order.getId().intValue());
							airline.setAirticketOrderPnrUuid(pnr.getUuid());
							airline.setAirlineName(objtriple.getString("airlineName"));
							this.setOptInfo(airline, BaseService.OPERATION_ADD);
							mtourCommonDao.saveObj(airline);
							//航段价格表--成本价
							JSONArray costs = objtriple.getJSONArray("costs");
							if(costs!=null){
								for(int t=0;t<costs.size();t++){
									JSONObject objmultiple = costs.getJSONObject(t);
									AirticketOrderPnrAirlinePrice price = new AirticketOrderPnrAirlinePrice();
									price.setAirticketOrderId(order.getId().intValue());
									price.setAirticketOrderPnrAirlineUuid(airline.getUuid());
									price.setAirticketOrderPnrUuid(pnr.getUuid());
									price.setCurrencyId(objmultiple.getInteger("currencyUuid"));
									price.setPersonNum(objmultiple.getInteger("peopleCount"));
									price.setFrontMoney(objmultiple.getDouble("totalDeposit"));
									price.setExchangerate(objmultiple.getDouble("exchangeRate"));
									price.setPrice(objmultiple.getDouble("costUnitPrice"));
									price.setPriceType(0);
									this.setOptInfo(price, BaseService.OPERATION_ADD);
									mtourCommonDao.saveObj(price);
									
									//新的保存成本记录
									CostRecord costRecord = new CostRecord();
									costRecord.setActivityId(order.getAirticketId());
									costRecord.setPayStatus(2);
									costRecord.setOrderId(order.getId());
									costRecord.setOrderType(7);
									costRecord.setReviewType(0);
									costRecord.setName(airline.getAirlineName());
									costRecord.setQuantity(objmultiple.getInteger("peopleCount"));
									costRecord.setPrice(objmultiple.getBigDecimal("costUnitPrice"));
									costRecord.setSupplyType(1);
									if(StringUtils.isNotBlank(pnr.getCostSupplyType())){
										costRecord.setSupplierType(Integer.parseInt(pnr.getCostSupplyType()));
									}
									costRecord.setSupplyName(((SupplierInfo) supplierInfoDao.getSession().load(SupplierInfo.class, pnr.getCostSupplyId().longValue())).getSupplierName());
									costRecord.setSupplyId(pnr.getCostSupplyId());
									costRecord.setRate(objmultiple.getBigDecimal("exchangeRate"));
									costRecord.setCurrencyId(objmultiple.getInteger("currencyUuid"));
									costRecord.setCurrencyAfter(currencyService.getRMBCurrencyId().getId().intValue());
									costRecord.setPriceAfter(objmultiple.getBigDecimal("peopleCount").multiply(objmultiple.getBigDecimal("costUnitPrice")).multiply(objmultiple.getBigDecimal("exchangeRate")).setScale(2, BigDecimal.ROUND_HALF_UP));
									costRecord.setIsJoin(pnr.getCodeType());
									costRecord.setBigCode(pnr.getFlightPnr());
									costRecord.setBankName(pnr.getCostBankName());
									costRecord.setBankAccount(pnr.getCostAccountNo());
									costRecord.setReviewType(0);
									costRecord.setBudgetType(1);
									costRecord.setOverseas(0);
									costRecord.setCreateBy(UserUtils.getUser());
									costRecord.setPnrUuid(pnr.getUuid());
									this.setOptInfo(costRecord, BaseService.OPERATION_ADD);
									mtourCommonDao.saveObj(costRecord);
									serialNumberService.genSerialNumber(SerialNumberService.TABLENAME_COST_RECORD, costRecord.getId().intValue());
								}
							}
							//航段价格表--外部价
							JSONArray salePrices = objtriple.getJSONArray("salePrices");
							if(salePrices!=null){
								for(int t=0;t<salePrices.size();t++){
									JSONObject objmultiple = salePrices.getJSONObject(t);
									AirticketOrderPnrAirlinePrice price = new AirticketOrderPnrAirlinePrice();
									price.setAirticketOrderId(order.getId().intValue());
									price.setAirticketOrderPnrAirlineUuid(airline.getUuid());
									price.setAirticketOrderPnrUuid(pnr.getUuid());
									price.setCurrencyId(objmultiple.getInteger("currencyUuid"));
									price.setPersonNum(objmultiple.getInteger("peopleCount"));
									price.setFrontMoney(objmultiple.getDouble("totalDeposit"));
									price.setExchangerate(objmultiple.getDouble("exchangeRate"));
									price.setPrice(objmultiple.getDouble("costUnitPrice"));
									price.setPriceType(1);
									this.setOptInfo(price, BaseService.OPERATION_ADD);
									mtourCommonDao.saveObj(price);
								}
							}
						}
					}
				}
			}
			out.setResponseCode4Success();
		}else{
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("订单id为空,程序异常.");
		}
		return out;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> getBaseInfoDetail(String orderId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ao.id as orderUuid,ao.lockstatus as orderStatusCode,ao.paymentStatus as orderFinanceStatusCode,aa.activity_airticket_name as productName,");
		sql.append(" aa.departureCity as departureCityUuid,(SELECT sg.name_cn  FROM  sys_geography sg WHERE sg.uuid =aa.departureCity) AS departureCityName,");
		sql.append(" aa.group_code as groupNo,aa.startingDate as departureDate,ao.comments AS memo,ao.person_num AS reservationCount,");
		sql.append(" aa.arrivedCity as arrivalCityUuid,(SELECT sg.name_cn  FROM  sys_geography sg WHERE sg.uuid =aa.arrivedCity) AS arrivalCityName,aa.lineType,");
		sql.append(" aa.country as lineCountryUuid,(SELECT sg.name_cn  FROM  sys_geography sg WHERE sg.uuid =aa.country) AS lineCountryName, ");
		//sql.append(" ao.salerId as ticketId,ao.salerName AS ticketName,aa.createBy as operatorId,ao.airticket_id AS airticketId,aa.operator AS operatorName,");
		sql.append(" aa.id,aa.operator as ticketId,aa.createBy as operatorId,");
		if (UserUtils.isMtourUser()){//只针对美途国际，操作，销售可以多选
			sql.append("(select GROUP_CONCAT(CONCAT_WS(',',su.id,su.`name`) SEPARATOR ';' ) from sys_user su where FIND_IN_SET(su.id,aa.operator)) as ticketName,");
			sql.append("(select GROUP_CONCAT(CONCAT_WS(',',su.id,su.`name`) SEPARATOR ';' ) from sys_user su where FIND_IN_SET(su.id,ao.salerName)) as operatorName, ");
		}else {
			sql.append("(select su.name from sys_user su where su.id = aa.operator) as ticketName,");
			sql.append("(select su.name from sys_user su where su.id = aa.createBy) as operatorName,");
		}
		sql.append(" aa.journey as itinerary,ao.create_date as orderDateTime,");
		sql.append(" (SELECT name FROM sys_user su WHERE su.id = ao.create_by) AS orderOwnerName, ");
		sql.append(" (select sum(ticket_personNum) from airticket_order_pnrGroup aopg where aopg.airticket_order_id=ao.id and delflag=0) as drawerCount");
		sql.append(" FROM  airticket_order ao LEFT JOIN activity_airticket aa ON ao.airticket_id = aa.id WHERE ao.del_flag = 0 AND ao.id = ?");
		return mtourCommonDao.findBySql(sql.toString(), Map.class, orderId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getReservationDetail(String orderId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT o.contactsName AS name,o.contactsTel AS phone,o.contactsAddress AS address,o.contactsTixedTel AS tel,");
		// 20151107 gao 修改，在渠道商ID<0时，使用airticket_order中的nagentName字段作为渠道商名称
		sql.append(" o.contactsZipCode AS zipcode,o.contactsFax AS tax,CASE WHEN a.id <0 THEN ao.nagentName ELSE  a.agentName END  AS channelName,a.id AS channelUuid,");
		sql.append(" ao.nagentcode AS channelCode, CASE WHEN o.agentId>0 THEN 1 ELSE 2 END  AS channelTypeCode");
		sql.append(" FROM ordercontacts o LEFT JOIN agentinfo a ON o.agentId=a.id LEFT JOIN airticket_order ao ON ao.id=o.orderid WHERE o.orderId = ? AND o.orderType = 7");
		return mtourCommonDao.findBySql(sql.toString(), Map.class, orderId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getFlightDetail(String productId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT afi.flightNumber AS flightNo,DATE_FORMAT(afi.startTime,'%Y-%m-%d') AS departureDate,DATE_FORMAT(afi.startTime,'%H') AS departureHour,DATE_FORMAT(afi.startTime,'%i') AS departureMinute,");
		sql.append(" DATE_FORMAT(afi.arrivalTime,'%Y-%m-%d') AS arrivalDate,DATE_FORMAT(afi.arrivalTime,'%H') AS arrivalHour,DATE_FORMAT(afi.arrivalTime,'%i') AS arrivalMinute,");
		sql.append(" afi.leaveAirport AS departureAirportUuid, afi.destinationAirpost AS arrivalAirportUuid,afi.remark AS memo");
		sql.append(" FROM  activity_flight_info afi WHERE afi.airticketId = ?");
		return mtourCommonDao.findBySql(sql.toString(), Map.class, productId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getTravelerDetail(String orderId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  t.id AS travelerUuid,t.name AS name,t.nameSpell AS englishName,t.personType AS travelerTypeCode,CAST(t.sex AS char) AS sexCode,");
		sql.append(" t.remark AS memo,t.srcPrice AS amount,t.srcPriceCurrency AS currencyUuid, c.currency_name AS currencyName");
//		sql.append(" GROUP_CONCAT( DISTINCT IFNULL (tpt.papers_type,' ')) AS credentialsTypeCode,GROUP_CONCAT( DISTINCT IFNULL (tpt.idCard,' ')) AS credentialsNo,GROUP_CONCAT( DISTINCT IFNULL (tpt.validityDate ,' ')) AS credentialsExpire,");
//		sql.append(" GROUP_CONCAT( DISTINCT IFNULL (tv.visaTypeId ,' ')) AS visaTypeCode,GROUP_CONCAT( DISTINCT IFNULL (tv.applyCountryId ,' ')) AS visaCountryId,GROUP_CONCAT( DISTINCT IFNULL (sg.uuid ,' ')) AS visaCountryUuid,GROUP_CONCAT( DISTINCT IFNULL (sg.name_cn ,' ')) AS visaCountryName");
		sql.append(" FROM traveler t LEFT JOIN currency c ON t.srcPriceCurrency=c.currency_id ");
		sql.append(" WHERE t.orderId=?  AND t.nationality is null AND t.order_type =7");
		return mtourCommonDao.findBySql(sql.toString(), Map.class, orderId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getAttachmentDetail(String productId) {
		String sql = "SELECT d.id AS attachmentUuid,d.docName AS fileName,d.docPath AS attachmentUrl FROM  airTicketFile a LEFT JOIN docinfo d ON a.docId = d.id WHERE a.delFlag = 0 AND a.airticketId = ?";
		return mtourCommonDao.findBySql(sql, Map.class, productId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getSalePriceChangeDetail(String orderId) {
		StringBuffer sql = new  StringBuffer();
		sql.append(" SELECT  case aoc.compute_type WHEN 1 THEN '-' ELSE '+' END  AS changeType,aoc.currency_id AS currencyUuid,c.currency_name AS currencyName,aoc.price AS amount,aoc.memo AS memo,aoc.exchangerate AS exchangeRate");
		sql.append(" FROM  airticket_order_changePrice aoc LEFT JOIN currency c ON aoc.currency_id=c.currency_id WHERE aoc.delFlag = 0 AND aoc.airticket_order_id = ?");
		return mtourCommonDao.findBySql(sql.toString(), Map.class, orderId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getPriceDetail(String priceType,String orderId) {
		StringBuffer sql = new  StringBuffer("SELECT ma.currencyId AS currencyUuid,ma.amount AS amount,ma.exchangerate AS exchangerate,c.currency_name AS currencyName");
		sql.append(" FROM  money_amount ma LEFT JOIN currency c ON ma.currencyId=c.currency_id WHERE  ma.delFlag=0 AND ma.serialNum = (");
		sql.append(" SELECT ao."+priceType+" FROM  airticket_order ao WHERE ao.id = ?) and c.create_company_id = ?");
		return mtourCommonDao.findBySql(sql.toString(), Map.class, orderId, UserUtils.getUser().getCompany().getId());
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getInvoiceOriginalDetail(String pnrGroupUuid) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		StringBuffer sql = new  StringBuffer();
		sql.append(" SELECT aop.uuid AS uuid,CAST(aop.code_type AS char) AS invoiceOriginalTypeCode,aop.flight_pnr AS PNR,aop.flight_pnr AS tourOperatorUuid,aop.airline AS airlineCompanyUuid,");
		//sql.append(" (SELECT si.supplierName FROM supplier_info si WHERE si.id=aop.flight_pnr) AS tourOperatorName,");
		sql.append(" aop.cost_account_no as costAccountNo,aop.cost_bank_name as costBankName,aop.cost_supply_id as costTourOperatorUuid,si.supplierName AS costTourOperatorName,");
		sql.append(" (SELECT  MAX(sai.airline_name) FROM sys_airline_info sai WHERE aop.airline=sai.airline_code) AS airlineCompanyName,");
		sql.append(" aop.ticket_deadline AS ticketDeadline,aop.front_deadline AS depositDeadline,aop.list_deadline AS listDeadline,aop.rename_deadline AS renameDeadline,aop.cancel_deadline AS cancelDeadline,");
		sql.append(" aop.cost_supply_type AS costTourOperatorTypeCode,case when sd.label IS NULL  then s.label else sd.label end AS costTourOperatorTypeName");
		sql.append(" FROM  airticket_order_pnr aop LEFT JOIN airticket_order_pnrGroup aog ON aop.airticket_order_pnrGroup_uuid = aog.uuid ");
		sql.append(" LEFT JOIN sys_dict sd ON aop.cost_supply_type =sd.value AND sd.type ='travel_agency_type'");
		sql.append(" LEFT JOIN supplier_info  si ON aop.cost_supply_id =si.id ");
		sql.append(" LEFT JOIN sysdefinedict s ON aop.cost_supply_type=s.value AND s.type ='travel_agency_type' AND s.companyId ="+companyId);
		sql.append(" WHERE aog.delFlag = 0 AND aop.delFlag=0 AND aop.airticket_order_pnrGroup_uuid = ?");
		return mtourCommonDao.findBySql(sql.toString(), Map.class, pnrGroupUuid);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getAirlineDetail(String PnrUUID) {
		StringBuffer sql = new  StringBuffer();
		sql.append(" SELECT aopa.airline_name AS airlineName,aopa.id as id,aopa.uuid as aopaUuid, aopap.currency_id AS currencyUuid,c.currency_name AS currencyName,aopap.exchangerate AS exchangeRate,aopap.personNum AS peopleCount,");
		sql.append(" aopap.price_type AS priceType,aopap.price AS costUnitPrice,aopap.personNum*aopap.price AS totalCost,aopap.front_money AS totalDeposit,");
		sql.append(" aopap.uuid AS aopapUuid,cr.id AS costUuid,cr.uuid AS crUuid,");
		sql.append(" CASE WHEN cr.payStatus IS NULL THEN 2 WHEN cr.payStatus=0 THEN 1 ELSE cr.payStatus END AS orderCostStateCode");
		sql.append(" FROM airticket_order_pnr_airline_price aopap LEFT JOIN airticket_order_pnr_airline aopa ON aopap.airticket_order_pnr_airline_uuid=aopa.uuid");
		sql.append(" LEFT JOIN cost_record cr ON aopap.cost_record_uuid=cr.uuid");
		sql.append(" LEFT JOIN currency c ON aopap.currency_id=c.currency_id WHERE aopap.delFlag=0 AND aopa.delFlag=0 AND aopap.airticket_order_pnr_uuid = ?");
		return mtourCommonDao.findBySql(sql.toString(), Map.class, PnrUUID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getBaseInfoPNR(String orderId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT aopg.uuid AS invoiceOriginalGroupUuid,aopg.ticket_personNum AS drawerCount,aopg.person_num AS reserveCount ");
		sql.append(" FROM  airticket_order_pnrGroup aopg  WHERE  aopg.delFlag='0' AND aopg.airticket_order_id= ? order by aopg.id DESC ");
		
		return mtourCommonDao.findBySql(sql.toString(), Map.class, orderId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> getAdditionalPriceChangeDetail(String orderId, int moneyType) {
		StringBuffer sql = new  StringBuffer();
		sql.append(" SELECT  aom.currency_id AS currencyUuid,aom.exchangerate AS exchangeRate,aom.amount AS amount,aom.funds_name AS fundsName,aom.memo AS memo,c.currency_name AS currencyName");
		sql.append(" FROM airticket_order_moneyAmount aom LEFT JOIN currency c ON c.currency_id=aom.currency_id");
		sql.append(" WHERE aom.delFlag=0 AND aom.status = 1 AND aom.airticket_order_id = ? AND aom.moneyType = ?");
		return mtourCommonDao.findBySql(sql.toString(), Map.class, orderId,moneyType);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getPNRInvoiceOriginals(String pnrGroupUuid) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT DISTINCT aop.uuid AS uuid,aop.code_type AS invoiceOriginalTypeCode,aop.flight_pnr AS PNR,sai.airline_name AS airlineCompanyName,");
		sql.append(" aop.ticket_deadline AS ticketDeadline,aop.front_deadline AS depositDeadline,aop.list_deadline AS listDeadline,aop.rename_deadline AS renameDeadline,aop.cancel_deadline AS cancelDeadline ");
		sql.append(" FROM  airticket_order_pnr aop LEFT JOIN sys_airline_info sai ON aop.airline=sai.airline_code ");
		sql.append(" WHERE aop.delFlag=0 AND aop.airticket_order_pnrGroup_uuid = ? AND sai.company_id=? ");
		
		return mtourCommonDao.findBySql(sql.toString(), Map.class, pnrGroupUuid,UserUtils.getUser().getCompany().getId());
	}

	@Override
	public List<PNRRecordJsonBean> getPNRRecord(String orderId,String invoiceOriginalGroupUuid) {	
		List<PNRRecordJsonBean> jsonBeanList = mtourCommonDao.getPNRRecord(orderId, invoiceOriginalGroupUuid);
		
		return jsonBeanList;
	}

	@Override
	public String updatePNRTravelerNum(String drawerCount,String reserveCount,String invoiceOriginalGroupUuid) {
		StringBuffer sql = new StringBuffer();
		sql.append(" UPDATE airticket_order_pnrGroup SET ticket_personNum=?,person_num=? WHERE uuid=?");
		
		mtourCommonDao.updateBySql(sql.toString(), drawerCount, reserveCount, invoiceOriginalGroupUuid);
		return "success";
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getOrderReceiptList(String orderId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT o.repeatSubmit,o.id AS receiveUuid,o.payType AS receiveMethodCode,o.payTypeName AS receiveMethodName,o.payPriceType AS receiveType,ma.currencyId AS currencyUuid,o.remarks AS memo,o.receivePeopleCount AS receivePeopleCount,");
		sql.append(" ma.exchangerate AS exchangeRate,ma.amount AS receiveAmount,c.currency_name AS currencyName,o.createDate AS receiveDate,o.isAsAccount AS completeCheck,su.name AS receiver,o.orderId AS orderUuid, ");
		sql.append(" o.payVoucher AS docIds FROM  orderpay o LEFT JOIN money_amount ma ON o.moneySerialNum=ma.serialNum LEFT JOIN currency c ON c.currency_id=ma.currencyId ");
		sql.append(" LEFT JOIN sys_user su ON o.createBy=su.id  WHERE o.orderId = ? AND o.orderType = 7 ORDER BY o.id DESC");
		return mtourCommonDao.findBySql(sql.toString(), Map.class, orderId);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> getOrderPayDocList(String docIds) {
		StringBuffer sql = new  StringBuffer();
		sql.append(" SELECT d.id AS attachmentUuid,d.docName AS fileName,d.docPath AS attachmentUrl,su.name AS uploadUserName,d.createBy AS uploadUserId");
		sql.append(" FROM  docinfo d LEFT JOIN sys_user su ON d.createBy=su.id WHERE d.id IN (" + docIds + ")");
		return mtourCommonDao.findBySql(sql.toString(), Map.class);
	}

	/**
	 * 订单列表接口
	 * @author gao
	 * 2015年10月21日
	 * @return
	 */
	@Override
	public MtourOrderJsonBean getMtourOrderJsonBean(MtourOrderParam mtourOrderParam) {
		MtourOrderJsonBean bean = new MtourOrderJsonBean();
		Map<String,Object> mapback = airticketOrderDao.getMtourOrderJsonBean(mtourOrderParam);
		List<MtourOrderDetail> backList = new ArrayList<MtourOrderDetail>();
		MtourOrderPage page = new MtourOrderPage();
		if(mapback==null || mapback.get("backList")==null || mapback.get("allCounts")==null){
			bean.setResults(null);
			page.setCurrentIndex(1);
			page.setRowCount(10);
			page.setTotalRowCount(0);
			bean.setPage(page);
			return bean;
		}
		@SuppressWarnings("unchecked")
		List<MtourOrderDetail> lickList = (List<MtourOrderDetail>)mapback.get("backList");
		if(!lickList.isEmpty()){
			Iterator<MtourOrderDetail> iter = lickList.iterator();
			while(iter.hasNext()){
				MtourOrderDetail det = iter.next();
				List<MtourOrderMoney> deposit = new ArrayList<MtourOrderMoney>();// 定金
				List<MtourOrderMoney> fullPayment = new ArrayList<MtourOrderMoney>();// 全款
				List<MtourOrderMoney> balancePayment = new ArrayList<MtourOrderMoney>();// 尾款
				List<MtourOrderMoney> receivedAmount = new ArrayList<MtourOrderMoney>();// 已收金额
				List<MtourOrderMoney> arrivedAmount = new ArrayList<MtourOrderMoney>();// 到帐金额
				// 定金相关
				String depUuid = det.getDepositUuid();
				List<String> depUuidList = new ArrayList<String>();
				if(StringUtils.isNotBlank(depUuid)){
					//List<MoneyAmount> list = moneyAmountDao.mergeAmountBySerialNum(depUuid);
					List<MoneyAmount> list = moneyAmountDao.findAmountListBySerialNum(depUuid);
					deposit = changeMtourOrder(list,new ArrayList<MtourOrderMoney>(),depUuidList);
					// 将全部金额转换成人民币并累加
					List<MtourOrderMoney> depositBack = changeToRMB(deposit);
					det.setDeposit(depositBack);
				}
				// 全款相关
				String fullPaymentUuid = det.getFullPaymentUuid();
				List<String> fullPaymentUuidList = new ArrayList<String>();
				if(StringUtils.isNotBlank(fullPaymentUuid)){
				//	List<MoneyAmount> list = moneyAmountDao.mergeAmountBySerialNum(fullPaymentUuid);
					List<MoneyAmount> list = moneyAmountDao.findAmountListBySerialNum(fullPaymentUuid);
					fullPayment = changeMtourOrder(list,new ArrayList<MtourOrderMoney>(),fullPaymentUuidList);
					// 将全部金额转换成人民币并累加
					List<MtourOrderMoney> fullPaymentBack = changeToRMB(fullPayment);
					det.setFullPayment(fullPaymentBack);
				}
				// 已收金额相关
				String receivedAmountUuid = det.getReceivedAmountUuid();
				List<String> receivedAmountUuidList = new ArrayList<String>();
				if(StringUtils.isNotBlank(receivedAmountUuid)){
				//	List<MoneyAmount> list = moneyAmountDao.mergeAmountBySerialNum(receivedAmountUuid);
					List<MoneyAmount> list = moneyAmountDao.findAmountListBySerialNum(receivedAmountUuid);
					receivedAmount = changeMtourOrder(list,new ArrayList<MtourOrderMoney>(),receivedAmountUuidList);
					// 将全部金额转换成人民币并累加
					List<MtourOrderMoney> receivedAmountBack = changeToRMB(receivedAmount);
					det.setReceivedAmount(receivedAmountBack);
				}
				// 到帐金额相关
				String arrivedAmountUuid = det.getArrivedAmountUuid();
				List<String> arrivedAmountUuidList = new ArrayList<String>();
				if(StringUtils.isNotBlank(arrivedAmountUuid)){
				//	List<MoneyAmount> list = moneyAmountDao.mergeAmountBySerialNum(arrivedAmountUuid);
					List<MoneyAmount> list = moneyAmountDao.findAmountListBySerialNum(arrivedAmountUuid);
					arrivedAmount = changeMtourOrder(list,new ArrayList<MtourOrderMoney>(),arrivedAmountUuidList);
					// 将全部金额转换成人民币并累加
					List<MtourOrderMoney> arrivedAmountBack = changeToRMB(arrivedAmount);
					det.setArrivedAmount(arrivedAmountBack);
				}
				// 尾款相关
				MtourOrderMoney balancePaymentMt = new MtourOrderMoney();
				BigDecimal fullPayMt = new BigDecimal(0); // 全部金额
				BigDecimal depMt = new BigDecimal(0); // 定金金额
				BigDecimal balaMt = new BigDecimal(0); // 尾款金额
				if(!det.getFullPayment().isEmpty()){
					fullPayMt = det.getFullPayment().get(0).getAmount();
				}
				if(!det.getDeposit().isEmpty()){
					depMt = det.getDeposit().get(0).getAmount();
				}
				// 计算尾款金额
				balaMt = fullPayMt.subtract(depMt);
				balancePaymentMt.setAmount(balaMt);
				Long companyId = UserUtils.getUser().getCompany().getId();
				// 获取当前用户的币种列表
				List<Currency> findSortCurrencyList = currencyService.findSortCurrencyList(companyId);
				if(!findSortCurrencyList.isEmpty()){
					for(Currency cu : findSortCurrencyList){
						// 将人民币ID和汇率写入
						if("人民币".equals(cu.getCurrencyName())){
							// 将人民币币种ID写入
							balancePaymentMt.setCurrencyUuid(Integer.valueOf(cu.getId().toString()));
							// 将人民币币种汇率写入
							balancePaymentMt.setExchangeRate(cu.getCurrencyExchangerate());
						}
					}
				}
				balancePayment.add(balancePaymentMt);
				det.setBalancePayment(balancePayment);
				// 附件相关
				String pid = det.getProductId(); // 产品ID
				if(StringUtils.isNotBlank(pid)){
					List<Map<String, Object>> attList = iAirticketDao.getInfoByAirTicketId(Long.valueOf(pid));
					//List<ActivityFile> attList = activityFileService.findFileListByPid(Integer.valueOf(pid));
					if(attList!=null && !attList.isEmpty()){
						det.setAttachment(changeMtourActivityFile(attList));
					}
				}
				
				backList.add(det);
			}
		}
		bean.setResults(backList);
		
		// 总行数
		if(StringUtils.isNotBlank(mapback.get("allCounts").toString())){
			Integer allCounts = Integer.valueOf(mapback.get("allCounts").toString());
			page.setTotalRowCount(allCounts);
		}else{
			page.setTotalRowCount(0);
		}
		// 当前页行数
		if(mtourOrderParam!=null && StringUtils.isNotBlank(mtourOrderParam.getRowCount())){
			page.setRowCount(Integer.valueOf(mtourOrderParam.getRowCount()));
		}else{
			page.setRowCount(10);
		}
		// 当前页码
		if(mtourOrderParam!=null && StringUtils.isNotBlank(mtourOrderParam.getCurrentIndex())){
			page.setCurrentIndex(Integer.valueOf(mtourOrderParam.getCurrentIndex()));
		}else{
			page.setCurrentIndex(1);
		}
		bean.setPage(page);
		return bean;
	}

	
	/**
	 * 将List<MoneyAmount> 转换为 List<MtourOrderMoney>
	 * @author gao
	 * 2015年10月22日
	 * @param list
	 */
	private List<MtourOrderMoney> changeMtourOrder(List<MoneyAmount> list,List<MtourOrderMoney> deposit,List<String> moneyStr){
		if(list!=null && !list.isEmpty()){
			for(MoneyAmount amount : list){
				MtourOrderMoney money = new MtourOrderMoney();
				money.setCurrencyUuid(amount.getCurrencyId());
				money.setExchangeRate(amount.getExchangerate());
				money.setAmount(amount.getAmount());
				deposit.add(money);
				String str = amount.getCurrencyId()+" "+amount.getAmount();
				moneyStr.add(str);
			}
		}
		return deposit;
	}
	
	/**
	 * 全部金额按照汇率累计为人民币
	 * @author gao
	 * @date 2015 11 20 
	 * @param list
	 * @return
	 */
	private List<MtourOrderMoney> changeToRMB(List<MtourOrderMoney> list){
		List<MtourOrderMoney> RMBlist = new ArrayList<MtourOrderMoney>();
		if(!list.isEmpty()){
			MtourOrderMoney RMBMoney = new MtourOrderMoney(); // 返回的人民币实体
			RMBMoney.setAmount(new BigDecimal(0));
			for(MtourOrderMoney money : list){
				if(money.getCurrencyUuid()!=null){
					Currency currency = currencyService.findCurrency(Long.valueOf(money.getCurrencyUuid()));
					if(currency!=null && StringUtils.isNotBlank(currency.getCurrencyName()) ){
						// 判断是否是人民币,不是人民币，则转换为人民币
						if(!"人民币".equals(currency.getCurrencyName())){
							// 将非人民币币种转换为人民币，并累加
							if(money.getAmount()!=null && RMBMoney.getAmount()!=null){
								 // 当moneyAmount 金额表中汇率不为空时，进行计算
								if(money.getExchangeRate()!=null){
									BigDecimal de = RMBMoney.getAmount().add(money.getAmount().multiply(money.getExchangeRate()));
									RMBMoney.setAmount(de);
								}
								// 当moneyAmount 金额表中汇率为空时，抓取基础信息currency币种表中的汇率进行计算
								else if(currency.getCurrencyExchangerate()!=null){
									BigDecimal de = RMBMoney.getAmount().add(money.getAmount().multiply(currency.getCurrencyExchangerate()));
									RMBMoney.setAmount(de);
								}
							}
						}else{ // 人民币直接累加
							BigDecimal de = RMBMoney.getAmount().add(money.getAmount());
							RMBMoney.setAmount(de);
						}
					}
				}
			}
			Long companyId = UserUtils.getUser().getCompany().getId();
			// 获取当前用户的币种列表
			List<Currency> findSortCurrencyList = currencyService.findSortCurrencyList(companyId);
			if(!findSortCurrencyList.isEmpty()&& (RMBMoney.getCurrencyUuid()==null || RMBMoney.getExchangeRate()==null)){
				for(Currency cu : findSortCurrencyList){
					// 将人民币ID和汇率写入
					if("人民币".equals(cu.getCurrencyName())){
						// 将人民币币种ID写入RMBMoney
						RMBMoney.setCurrencyUuid(Integer.valueOf(cu.getId().toString()));
						// 将人民币币种汇率写入RMBMoney
						RMBMoney.setExchangeRate(cu.getCurrencyExchangerate());
					}
				}
			}
			RMBlist.add(RMBMoney);
		}
		return RMBlist;
	}
	
	/**
	 * 将List<ActivityFile> 转换为 List<MtourOrderAttachment>
	 * @author gao
	 * 2015年11月02日
	 * @param activityFile
	 * @return
	 */
	private List<MtourOrderAttachment> changeMtourActivityFile(List<Map<String, Object>> activityFile){
		List<MtourOrderAttachment> attList = new ArrayList<MtourOrderAttachment>();
		if(activityFile!=null && !activityFile.isEmpty()){
			Iterator<Map<String, Object>> iter = activityFile.iterator();
			while(iter.hasNext()){
				Map<String, Object> file = iter.next();
				MtourOrderAttachment ment = new MtourOrderAttachment();
				ment.setAttachmentUuid(Integer.valueOf(file.get("fid").toString()));
				ment.setAttachmentUrl(file.get("docPath")!=null?file.get("docPath").toString():null);
				ment.setFileName(file.get("docName")!=null?file.get("docName").toString():null);
				ment.setUploadUserName(UserUtils.getUserNameById(Long.valueOf(file.get("createBy").toString())));
				attList.add(ment);
			}
		}
		return attList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getOrderReceiptDetail(String receiveId) {
		StringBuffer sql = new  StringBuffer();
		sql.append(" SELECT o.id AS receiveUuid,o.orderId AS orderUuid,o.paymentStatus AS speedyClearance,o.payPriceType AS receiveTypeCode,ma.amount AS receiveAmount,ma.exchangerate AS exchangeRate,ma.currencyId AS currency,");
		sql.append(" o.payerName AS payer,o.payType AS paymentMethodCode,o.checkNumber AS checkNo,o.invoiceDate AS checkIssueDate,o.bankName AS paymentBank,o.bankAccount AS paymentAccount,");
		//sql.append(" ma1.amount AS totalAmount,ma1.currencyId AS totalCurrencyUuid,(ma1.amount-ma2.amount) AS unreceivedAmount,ma2.currencyId AS unreceivedCurrencyUuid,");
		sql.append(" o.toBankNname AS receiveBank,o.toBankAccount AS receiveAccount,o.remarks AS memo,o.payVoucher AS docIds,o.receivePeopleCount AS receivePeopleCount, repeatSubmit ");
		sql.append(" FROM orderpay o LEFT JOIN airticket_order ao ON o.orderId=ao.id LEFT JOIN money_amount ma ON o.moneySerialNum=ma.serialNum ");
		//sql.append(" LEFT JOIN money_amount ma1 ON ao.total_money=ma1.serialNum LEFT JOIN money_amount ma2 ON ao.payed_money=ma2.serialNum");
		sql.append(" WHERE o.id = ?");
		Map<String, Object> result = (Map<String, Object>)mtourCommonDao.getSession().createSQLQuery(sql.toString()).setParameter(0, receiveId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getOrderPriceDetail(String orderId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  ao.agentinfo_id AS channelUuid,  ao.id AS orderUuid,  ao.person_num AS reservationCount,  ao.total_money AS totalAmounts, ao.payed_money AS payedAmounts,ao.group_code AS groupNo FROM airticket_order ao WHERE ao.id = ? ");
		Map<String, Object> result = (Map<String, Object>)mtourCommonDao.getSession().createSQLQuery(sql.toString()).setParameter(0, orderId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getinvoiceOriginalGroupDetail(String orderId) {
		String sql = "SELECT aog.uuid AS invoiceOriginalGroupUuid,aog.pnr_group_index AS groupMark FROM  airticket_order_pnrGroup aog  WHERE aog.airticket_order_id = ?";
		return mtourCommonDao.findBySql(sql, Map.class,orderId);
	}
	
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public boolean saveOrderpayInfo(BaseInput4MT input) {
		boolean flag = false;
		//System.out.println(input.getParam());
		try{
			//组装订单付款信息
			OrderpayJsonBean jsonBean = input.getParamObj(OrderpayJsonBean.class);
			
			Long orderId = Long.parseLong(jsonBean.getOrderUuid());
			//设置流水号uuid
			String moneySerialNum = UuidUtils.generUuid();

			// 获取订单号信息
			AirticketOrder airticketOrder = airTicketOrderService.getAirticketorderById(orderId);
			
			//更新订单付款金额
			String serialNum = "";
			if(StringUtils.isEmpty(airticketOrder.getPayedMoney())) {
				serialNum = UuidUtils.generUuid();
				airticketOrder.setPayedMoney(serialNum);
			} else {
				serialNum = airticketOrder.getPayedMoney();
			}

			//组装金额信息
			String[] currencyIds = new String[]{jsonBean.getCurrencyUuid()};
			String[] amounts = new String[]{jsonBean.getReceiveAmount().toString()};
			
			//获取创建订单时的币种汇率信息
			Currency currency = this.getOriginalCurrency(orderId, jsonBean.getCurrencyUuid());
			String[] convertLowests = new String[]{currency.getConvertLowest().toString()};
			//0094需求，美途国际账号，存储金额时，汇率取前端页面传递的汇率 update by shijun.liu 2016.03.08
			if(UserUtils.isMtourUser()){
				convertLowests = new String[]{jsonBean.getExchangeRate().toString()};
			}
			// 保存订单的支付金额
			moneyAmountService.addPayedMoneyAmount(serialNum, moneySerialNum, orderId.intValue(), Context.ORDER_TYPE_JP, amounts, currencyIds, convertLowests);
			
			//设置支付方式信息。1、即时支付(默认为即时支付)
			Integer paymentStatus ;
			if(StringUtils.isEmpty(jsonBean.getSpeedyClearance())) {
				paymentStatus = 1;
			} else {
				paymentStatus = Integer.parseInt(jsonBean.getSpeedyClearance());
			}
			
			//设置支付方式
			Integer payType = Integer.parseInt(jsonBean.getPaymentMethodCode());
			//支付方式名称
			String payTypeName = orderpayDao.getPayTypeName(paymentStatus, payType);

			
			//为订单付款表设置文件id字符串
			List<String> attachmentIds = new ArrayList<String>();
			List<Long> docIdList = new ArrayList<Long>();
			if(CollectionUtils.isNotEmpty(jsonBean.getAttachments())) {
				for(DocInfoJsonBean docInfo : jsonBean.getAttachments()) {
					attachmentIds.add(docInfo.getAttachmentUuid());
					
					docIdList.add(Long.parseLong(docInfo.getAttachmentUuid()));
				}
			}

			//收款类别（1、定金；2、尾款；3、追散；4、全款；5、其他收入）
			String receiveType = jsonBean.getReceiveType();
			
			//保存订单收款信息
			Orderpay orderpay = new Orderpay();
			orderpay.setOrderType(Context.ORDER_TYPE_JP);
			orderpay.setOrderId(orderId);
			
			//设置支付方式信息。1、即时支付(默认为即时支付)
			orderpay.setPaymentStatus(paymentStatus);
			orderpay.setPayPriceType(Integer.parseInt(receiveType));
			orderpay.setPayType(payType);
			orderpay.setPayTypeName(payTypeName);
			orderpay.setPayerName(jsonBean.getPayer());
			orderpay.setCheckNumber(jsonBean.getCheckNo());
			orderpay.setInvoiceDate(jsonBean.getCheckIssueDate());
			orderpay.setBankName(jsonBean.getPaymentBank());
			orderpay.setBankAccount(jsonBean.getPaymentAccount());
			orderpay.setToBankNname(jsonBean.getReceiveBank());
			orderpay.setToBankAccount(jsonBean.getReceiveAccount());
			orderpay.setReceivePeopleCount(jsonBean.getReceivePeopleCount());
			orderpay.setRemarks(jsonBean.getMemo());
			
			orderpay.setPayVoucher(BeanUtil.joinListWithSign(attachmentIds, ","));
			//更新文档表里数据,重置文档中的订单id属性
			if(CollectionUtils.isNotEmpty(docIdList)) {
				docInfoDao.updateDocInfoPayOrderId(orderId, docIdList);
			}
			
			orderpay.setMoneySerialNum(moneySerialNum);
			orderpay.setOrderNum(airticketOrder.getOrderNo());
			//默认未达账为99
			orderpay.setIsAsAccount(99);
			orderpayDao.saveObj(orderpay);

			//保存moneyAmount数据
			flag = moneyAmountService.saveMoneyAmountByOrderpayInfo(orderpay, currencyIds, amounts, convertLowests, Context.BUSINESS_TYPE_ORDER, orderId);
			
			//订单收款时更新收款状态
			updateAirticketOrderPaymentStatus(String.valueOf(orderId));

			//生成订单收款流水号信息
			serialNumberService.genSerialNumber(SerialNumberService.TABLENAME_ORDERPAY, orderpay.getId().intValue());
			
			//假如添加记录失败，程序执行手动回滚操作
			if(!flag) {
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
			throw e;
		}
		return flag;
	}
	
	/**
	 * 其他收款详情
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getOtherReceiptDetail(String costRecordId) {
		StringBuffer sql = new  StringBuffer();
		sql.append(" SELECT cr.id AS otherRevenueUuid,cr.orderId AS orderUuid,pg.paymentStatus AS speedyClearance,")
		   .append(" cr.name AS fundsName,cr.rate AS exchangeRate,cr.price AS amount ,")
		   .append(" cr.currencyAfter AS convertedCurrencyUuid,cr.priceAfter AS convertedAmount,")
		   .append(" pg.payPriceType AS receiveTypeCode,cr.comment AS memo,pg.payVoucher AS docIds,")
		   .append(" pg.payType AS paymentMethodCode,pg.payTypeName AS paymentMethodName,cr.currencyId AS currencyUuid,")
		   .append(" CAST(cr.supplierType AS char) AS tourOperatorOrChannelTypeCode,CAST(cr.supplyType AS char) AS ")
		   .append(" tourOperatorChannelCategoryCode,cr.supplyId AS tourOperatorOrChannelUuid,")
		   .append(" cr.supplyName AS tourOperatorOrChannelName, pg.payerName AS payer,pg.checkNumber AS checkNo,")
		   .append(" pg.invoiceDate AS checkIssueDate,pg.bankName AS paymentBank,pg.bankAccount AS paymentAccount,")
		   .append(" pg.toBankNname AS receiveBank,pg.toBankAccount AS receiveAccount ")
		   .append(" FROM pay_group pg LEFT JOIN cost_record cr ON pg.cost_record_id = cr.id WHERE cr.id = ?");
		Map<String, Object> result = (Map<String, Object>)mtourCommonDao.getSession().createSQLQuery(sql.toString())
				.setParameter(0, costRecordId).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
		return result;
	}

	@Override
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public void orderReceiptCancel(String receiveId, Integer orderId) {
		String sql = "UPDATE  orderpay o set o.isAsAccount = 0 WHERE o.id = ? ";
		mtourCommonDao.updateBySql(sql, receiveId);
		//修改订单已付金额-----edit by majiancheng(2015-11-6)
		orderpayService.orderReceiptCancel(Integer.parseInt(receiveId), orderId, Context.ORDER_TYPE_JP);
		//撤销后更新订单的付款状态
		Integer status = this.checkAirticketOrderPaymentStatus(String.valueOf(orderId));
		String updateSql = "UPDATE airticket_order ao set ao.paymentStatus = ? WHERE ao.id = ?";
		mtourCommonDao.updateBySql(updateSql, status,orderId);
	}
	
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	@Override
	public void deleteCostRecord(String costId) {
		String sql = "UPDATE cost_record cr SET cr.delFlag = "+Context.DEL_FLAG_DELETE+" WHERE cr.id = ?";
		CostRecord costRecord = costRecordDao.findOne(Long.parseLong(costId));
		//同步删除订单中的成本记录,如果是其他收入收款，则不进行删除
		if(null != costRecord && costRecord.getBudgetType() != 2){
			String delSql = "UPDATE airticket_order_pnr_airline_price aopap SET aopap.delFlag = "+Context.DEL_FLAG_DELETE+" WHERE aopap.cost_record_uuid = ?";
			mtourCommonDao.updateBySql(delSql, costRecord.getUuid());
			// 成本记录删除时，更新订单的付款状态 update by shijun.liu 2016.03.16
			financeDao.updateOrderRefundFlag(costRecord.getOrderId());
		}else if (null != costRecord && costRecord.getBudgetType().intValue() == 2){
			//其他收入，删除其他收入收款记录
			PayGroup payGroup = payGroupDao.findByOrderTypeAndCostRecordId(costRecord.getOrderType(), costRecord.getId().intValue());
			payGroup.setDelFlag(Context.DEL_FLAG_DELETE);
			payGroupDao.save(payGroup);
			//其他收入，删除其他收入收款金额
			MoneyAmount ma = moneyAmountDao.findOneAmountBySerialNum(payGroup.getPayPrice());
			ma.setDelFlag(Context.DEL_FLAG_DELETE);
			moneyAmountDao.save(ma);
		}
		mtourCommonDao.updateBySql(sql, costId);
	}
	
	/**
	 * 订单取消验证
	 * 1:草稿状态:提示确认后,直接取消
	2:订单还没有提交过借款、退款、追加成本、成本录入、订单收款、其他收入的（注意，保存操作不算提交），点击取消订单后，弹出取消确认弹窗，点击“是”后订单状态变更为“已取消”
	3:单提交过借款、退款、追加成本、成本录入、订单收款、其他收入的（注意，保存操作不算提交），但还可撤销的（提交借款、退款、追加成本、成本录入但财务未付款，就可撤销；
	     提交了订单收款、其他收入，但财务未确认收款，就可撤销），点击取消订单后，弹出确认弹窗，点击“是”后，已提交的借款、退款、追加成本、成本录入、订单收款、其他收入都自动撤销，且订单状态变更为“已取消”
	4:订单提交过借款、退款、追加成本、成本录入、订单收款、其他收入的（注意，保存操作不算提交），但不可撤销的（提交借款、退款、追加成本、成本录入且财务已付款，就不可撤销；提交了订单收款、其他收入，但财务已确认收款，就不可撤销），
	     点击取消订单后，弹出提示窗
		 * @Title: cancelOrderValidate
	     * @return Map<String,String>
	     * @author majiancheng       
	     * @date 2015-11-1 下午3:12:59
	 */
	public Map<String, String> cancelOrderValidate(String orderUuid) {
		Map<String, String> datas = new HashMap<String, String>();
		//初始化订单状态信息
		boolean step_2 = false;
		boolean step_3 = false;
		boolean step_4 = false;
		Long orderId = Long.parseLong(orderUuid);
		AirticketOrder order = airTicketOrderService.getAirticketorderById(orderId);
		
		//假如订单是已草稿状态，直接返回
		if(order.getLockStatus() == AirticketOrder.LOCKSTATUS_CG) {
			datas.put("cancelCode", "1");
			datas.put("msg", "是否确认取消");
			return datas;
		}
		
		step_2 = true;
		//获取该机票订单下所有的借款、退款、追加成本信息
		List<AirticketOrderMoneyAmount> moneyAmounts = airticketOrderMoneyAmountDao.getByOrderId(orderId.intValue());
		List<CostRecord> costRecords = costRecordDao.getByOrderTypeAndActivityId(Context.ORDER_TYPE_JP, order.getAirticketId());
		List<Orderpay> orderpays = orderpayDao.findOrderpayByOrderId(orderId, Context.ORDER_TYPE_JP);
		List<PayGroup> payGroups = payGroupDao.findByOrderTypeAndGroupId(Context.ORDER_TYPE_JP, order.getAirticketId().intValue());
		
		//收款、退款、追加成本（是否可取消校验）
		if(CollectionUtils.isNotEmpty(moneyAmounts)) {
			String companyUuid = UserUtils.getUser().getCompany().getUuid();
			for(AirticketOrderMoneyAmount moneyAmount : moneyAmounts) {
				List<Refund> refunds = refundDao.findByRecordIdAndOrderType(moneyAmount.getId().longValue(), Context.ORDER_TYPE_JP, companyUuid);
				if(CollectionUtils.isNotEmpty(refunds)) {
					step_4 = true;
				}
			}
			step_3 = true;
		}

		//成本录入（是否可取消校验）
		if(CollectionUtils.isNotEmpty(costRecords)) {
			boolean costRecordStep_3 = false;
			for(CostRecord costRecord : costRecords) {
				//'付款状态:0 没付款, 1:已付款, 2:待提交, 3:已提交'
				if(costRecord.getPayStatus() == 1) {
					step_4 = true;
				//当机票订单成本录入为已提交的时候，该成本录入可撤销
				} else if(costRecord.getPayStatus() == 3) {
					costRecordStep_3 = true;
				}
			}
			if(!step_3) {
				step_3 = costRecordStep_3;
			}
		}

		//订单收款（是否可取消校验）
		if(CollectionUtils.isNotEmpty(orderpays)) {
			boolean orderpayStep_3 = false;
			for(Orderpay orderpay : orderpays) {
				//'99：默认值 0 ：已撤销  1：已达账 2：已驳回'
				if(orderpay.getIsAsAccount() == 1) {
					step_4 = true;
				} else if(orderpay.getIsAsAccount() == 99) {
					orderpayStep_3 = true;
				}
			}
			if(!step_3){
				step_3 = orderpayStep_3;
			}
		}

		//其他收入收款（是否可取消校验）
		if(CollectionUtils.isNotEmpty(payGroups)) {
			boolean payGroupStep_3 = false;
			for(PayGroup payGroup : payGroups) {
				//null为默认值，1：已达帐；101：撤销；102：驳回；
				if(payGroup.getIsAsAccount() == null) {
					payGroupStep_3 = true;
				} else if(payGroup.getIsAsAccount() == 1) {
					step_4 = true;
				}
			}
			if(!step_3) {
				step_3 = payGroupStep_3;
			}
		}
		
		if(step_4) {
			datas.put("cancelCode", "4");
			datas.put("msg", "订单已产生借款/收款，无法取消");
		} else if(step_3) {
			datas.put("cancelCode", "3");
			datas.put("msg", "订单已提交借款/收款，是否撤销借款/收款并取消订单？");
		} else if(step_2) {
			datas.put("cancelCode", "2");
			datas.put("msg", "是否确认取消");
		}
		
		return datas;
	}
	
	/**
	 *  订单取消
		描述:取消验证后,真正的取消订单
		 * @Title: cancelOrder
	     * @return boolean
	     * @author majiancheng       
	     * @date 2015-11-1 下午5:00:10
	 */
	public boolean cancelOrder(String orderUuid) {
		boolean flag = false;
		
		Long orderId = Long.parseLong(orderUuid);
		AirticketOrder order = airTicketOrderService.getAirticketorderById(orderId);
		Integer lockStatus = order.getLockStatus();//获取订单锁定状态
		
		//取消订单操作 lockStatus：订单锁定状态:0正常.1锁定(当前订单所挂接的主订单锁定时修改状态)主订单锁定,子订单不能解锁.2取消状态.3草稿状态
		order.setLockStatus(AirticketOrder.LOCKSTATUS_QX);
		order.setOrderState(99);
		airTicketOrderService.updateAirticketOrder(order);

		//假如订单是草稿状态，直接取消该订单    
		if(lockStatus == AirticketOrder.LOCKSTATUS_CG) {
			return true;
		}
		
		//获取该机票订单下所有的借款、退款、追加成本信息
		List<AirticketOrderMoneyAmount> moneyAmounts = airticketOrderMoneyAmountDao.getByOrderId(orderId.intValue());
		List<CostRecord> costRecords = costRecordDao.getByOrderTypeAndActivityId(Context.ORDER_TYPE_JP, order.getAirticketId());
		List<Orderpay> orderpays = orderpayDao.findOrderpayByOrderId(orderId, Context.ORDER_TYPE_JP);
		List<PayGroup> payGroups = payGroupDao.findByOrderTypeAndGroupId(Context.ORDER_TYPE_JP, orderId.intValue());
		
		//收款、退款、追加成本（机票订单取消操作）
		if(CollectionUtils.isNotEmpty(moneyAmounts)) {
			for(AirticketOrderMoneyAmount moneyAmount : moneyAmounts) {
				moneyAmount.setStatus(AirticketOrderMoneyAmount.STATUS_CANCEL);
			}
		}
		
		//成本录入（机票订单取消操作）
		if(CollectionUtils.isNotEmpty(costRecords)) {
			//'付款状态:0 没付款, 1:已付款, 2:待提交, 3:已提交, 4:已撤销'
			for(CostRecord costRecord : costRecords) {
				costRecord.setPayStatus(4);
			}
		}
		
		//订单收款（机票订单取消操作）
		if(CollectionUtils.isNotEmpty(orderpays)) {
			for(Orderpay orderpay : orderpays) {
				//'99：默认值 0 ：已撤销  1：已达账 2：已驳回'
				orderpay.setIsAsAccount(0);
			}
		}
		
		//其他收入收款（机票订单取消操作）
		if(CollectionUtils.isNotEmpty(payGroups)) {
			for(PayGroup payGroup : payGroups) {
				//null为默认值，1：已达帐；101：撤销；102：驳回；
				payGroup.setIsAsAccount(Context.MONEY_TYPE_CANCEL);
			}
		}
		
		//批量更新操作
		airticketOrderMoneyAmountDao.batchUpdate(moneyAmounts);
		costRecordDao.batchUpdate(costRecords);
		orderpayDao.batchUpdate(orderpays);
		payGroupDao.batchUpdate(payGroups);
		flag = true;
		
		return flag;
	}
	
	/**
	 * 查看给定订单的支付状态，返回对应的代码（通过到账金额判断）
	 * @param orderId 机票订单id
	 * @return
	 */
	@Override
	public Integer checkAirticketOrderPaymentStatus(String orderId){
		StringBuffer paysql = new  StringBuffer();
		paysql.append(" SELECT SUM(ma.amount*ma.exchangerate) AS payedAmount FROM money_amount ma WHERE ma.serialNum IN (SELECT o.moneySerialNum FROM orderpay o WHERE o.delFlag=0 AND o.isAsAccount=1 AND o.orderType = 7 AND o.orderId = '"+orderId+"')");
		SQLQuery query = mtourCommonDao.getSession().createSQLQuery(paysql.toString()).addScalar("payedAmount", StandardBasicTypes.DOUBLE);
		Double payedAmount = (Double)query.uniqueResult();//目前到账金额
		if(payedAmount==null||payedAmount==0){
			return 101;
		}
		//查询订单总金额、定金金额
		//update by zhanghao 2015-12-17 订单总额需要转成人民币再和到账金额做比较
		String frontAmountSql = "SELECT SUM(ma.amount*ma.exchangerate) AS amount FROM  money_amount ma WHERE ma.serialNum = (SELECT ao.front_money FROM  airticket_order ao WHERE ao.id ="+orderId+")";
		String totalAmountSql = "SELECT SUM(ma.amount*ma.exchangerate) AS amount FROM  money_amount ma WHERE ma.serialNum = (SELECT ao.total_money FROM  airticket_order ao WHERE ao.id ="+orderId+")";
		Double frontAmount = (Double)mtourCommonDao.getSession().createSQLQuery(frontAmountSql).addScalar("amount", StandardBasicTypes.DOUBLE).uniqueResult();
		Double totalAmount = (Double)mtourCommonDao.getSession().createSQLQuery(totalAmountSql).addScalar("amount", StandardBasicTypes.DOUBLE).uniqueResult();
		frontAmount = frontAmount!=null?frontAmount:0;
		totalAmount = totalAmount!=null?totalAmount:0;
		
		if(payedAmount>=totalAmount){
			return 104;
		}else if(payedAmount>=frontAmount){
			return 103;
		}else{
			return 102;
		}
	}
	
	/**
	 * 查看给定订单的支付状态，返回对应的代码（通过已收金额判断）
	 * @param orderId 机票订单id
	 * @return
	 */
	@Override
	public Integer getAirticketOrderPaymentStatus(String orderId){
		StringBuffer paysql = new  StringBuffer();
		paysql.append(" SELECT SUM(ma.amount*ma.exchangerate) AS payedAmount FROM money_amount ma WHERE ma.serialNum IN (SELECT o.moneySerialNum FROM orderpay o WHERE o.delFlag=0 AND o.isAsAccount!=0 AND o.orderType = 7 AND o.orderId = '"+orderId+"')");
		SQLQuery query = mtourCommonDao.getSession().createSQLQuery(paysql.toString()).addScalar("payedAmount", StandardBasicTypes.DOUBLE);
		Double payedAmount = (Double)query.uniqueResult();//目前已付金额
		if(payedAmount==null||payedAmount==0){
			return 101;
		}
		//查询订单总金额、定金金额
		//update by zhanghao 2015-12-17 订单总额需要转成人民币在和已付金额做比较
		String frontAmountSql = "SELECT SUM(ma.amount*ma.exchangerate) AS amount FROM  money_amount ma WHERE ma.serialNum = (SELECT ao.front_money FROM  airticket_order ao WHERE ao.id ="+orderId+")";
		String totalAmountSql = "SELECT SUM(ma.amount*ma.exchangerate) AS amount FROM  money_amount ma WHERE ma.serialNum = (SELECT ao.total_money FROM  airticket_order ao WHERE ao.id ="+orderId+")";
		Double frontAmount = (Double)mtourCommonDao.getSession().createSQLQuery(frontAmountSql).addScalar("amount", StandardBasicTypes.DOUBLE).uniqueResult();
		Double totalAmount = (Double)mtourCommonDao.getSession().createSQLQuery(totalAmountSql).addScalar("amount", StandardBasicTypes.DOUBLE).uniqueResult();
		frontAmount = frontAmount!=null?frontAmount:0;
		totalAmount = totalAmount!=null?totalAmount:0;
		//
		if(payedAmount>=totalAmount){
			return 104;
		}else if(payedAmount>=frontAmount){
			return 103;
		}else{
			return 102;
		}
	}
	
	/**
	 * 更新订单的付款状态
	 * @param orderId
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public void updateAirticketOrderPaymentStatus(String orderId){
		Integer status = this.checkAirticketOrderPaymentStatus(orderId);
		String sql = "UPDATE airticket_order ao set ao.paymentStatus = ? WHERE ao.id = ?";
		mtourCommonDao.updateBySql(sql, status,orderId);
	}
	
	/**
	 * PNR修改预定人数时更新机票订单总人数
	 */
	@Override
	@Transactional(readOnly = false, rollbackFor = { Exception.class })
	public void updateAirticketOrderPersonNum(Integer value,String orderId) {
		String sql = "UPDATE airticket_order ao SET ao.person_num =? WHERE ao.id =? ";
		mtourCommonDao.updateBySql(sql, value,orderId);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getTravelerCredentials(String travelerId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT  CAST(IFNULL (tpt.papers_type,' ') AS char) AS credentialsTypeCode,CAST(IFNULL (tpt.idCard,' ') AS char) AS credentialsNo,CAST(IFNULL (tpt.validityDate ,' ') AS char) AS credentialsExpire  FROM  traveler_papers_type tpt WHERE tpt.traveler_id = ?");
		return mtourCommonDao.findBySql(sql.toString(), Map.class, travelerId);
	}
	@Override
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getTravelerVisas(String travelerId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT CAST(IFNULL (tv.visaTypeId ,' ') AS char)  AS visaTypeCode,CAST(IFNULL (tv.applyCountryId ,' ') AS char)  AS visaCountryId,CAST(IFNULL (sg.uuid ,' ') AS char)  AS visaCountryUuid,CAST(IFNULL (sg.name_cn ,' ') AS char)  AS visaCountryName");
		sql.append(" FROM  travelervisa tv LEFT JOIN sys_geography sg ON tv.applyCountryId=sg.id WHERE tv.travelerId = ?");
		return mtourCommonDao.findBySql(sql.toString(), Map.class, travelerId);
	}
	
	/**
	 * 根据订单id,币种id查询订单汇率.
	 * 查询为空时,返回基础信息Currency
	 * @author hhx
	 * @param orderId
	 * @param currencyId
	 * @return
	 */
	public Currency getOriginalCurrency(Long orderId,String currencyId){
		Currency currency = null;
		if (orderId != null) {
			AirticketOrder order = airTicketOrderService.getAirticketorderById(orderId);
			List<MoneyAmount> list = moneyAmountDao.getAmountByUid(order.getTotalMoney());
			if (!list.isEmpty()) {
				Map<String, MoneyAmount> map = new TreeMap<String, MoneyAmount>();
				for (MoneyAmount m : list) {
					map.put(m.getCurrencyId().toString(), m);
				}
				if (StringUtils.isNotBlank(currencyId)) {
					MoneyAmount temp = map.get(currencyId);
					if (temp != null) {
						currency = new Currency();
						currency.setCurrencyExchangerate(temp.getExchangerate());
						currency.setConvertLowest(temp.getExchangerate());
					} else {
						currency = currencyService.findCurrency(Long.parseLong(currencyId));
					}
				}
			}else{
				currency = currencyService.findCurrency(Long.parseLong(currencyId));
			}
		}
		return currency;
	}
	
	/**
	 * 其他收入、订单收款单独查询货币
	 * @author hhx
	 * @param orderId
	 * @param type
	 * @return
	 */
	public List<Map<String,String>> getExistsCurrency(Long orderId,String type){
		Map<String,Map<String,String>> map = new HashMap<String,Map<String,String>>();
		
		//edit by majiancheng(2015-11-24)
		/*if("1".equals(type)){//1其它收入
			String sql = "select cr.currencyId,cr.rate from cost_record cr where cr.budgetType='2' and delflag='0' and orderid=? and orderType=?";
			List<Map<String,Object>> listCostRecord = moneyAmountDao.findBySql(sql,Map.class,orderId,Context.ORDER_TYPE_JP);
			for(Map<String,Object> mapCR:listCostRecord){
				Map<String,String> temp = new HashMap<String,String>();
				temp.put("currencyUuid", mapCR.get("currencyId")==null?null:mapCR.get("currencyId").toString());
				temp.put("exchangeRate", mapCR.get("rate")==null?null:mapCR.get("rate").toString());
				Currency currency = currencyService.findCurrency(((Integer)mapCR.get("currencyId")).longValue());
				temp.put("currencyCode", currency.getCurrencyMark());
				temp.put("currencyName", currency.getCurrencyName());
				map.put(mapCR.get("currencyId").toString(), temp);
			}
		}else if("2".equals(type)){//2订单收款
*/			AirticketOrder order = airTicketOrderService.getAirticketorderById(orderId);
			List<MoneyAmount> listMoneyAmount = moneyAmountDao.getAmountByUid(order.getTotalMoney());
			for(MoneyAmount mapMA:listMoneyAmount){
				Map<String,String> temp = new HashMap<String,String>();
				temp.put("currencyUuid", mapMA.getCurrencyId().toString());
				temp.put("exchangeRate", mapMA.getExchangerate()==null?null:mapMA.getExchangerate().toString());
				Currency currency = currencyService.findCurrency(mapMA.getCurrencyId().longValue());
				temp.put("currencyCode", currency.getCurrencyMark());
				temp.put("currencyName", currency.getCurrencyName());
				map.put(mapMA.getCurrencyId().toString(), temp);
			}
			//获取订单外报价集合信息 edit by majiancheng 2015-11-20
			List<AirticketOrderPnrAirlinePrice> airlinePrices = airticketOrderPnrAirlinePriceDao.getAirlinePricesByOrdereId(order.getId());
			if(CollectionUtils.isNotEmpty(airlinePrices)) {
				for(AirticketOrderPnrAirlinePrice airlinePrice : airlinePrices) {
					if(map.get(airlinePrice.getCurrencyId().toString()) == null) {
						Map<String,String> temp = new HashMap<String,String>();
						temp.put("currencyUuid", airlinePrice.getCurrencyId().toString());
						temp.put("exchangeRate", airlinePrice.getExchangerate()==null?null:airlinePrice.getExchangerate().toString());
						Currency currency = currencyService.findCurrency(airlinePrice.getCurrencyId().longValue());
						temp.put("currencyCode", currency.getCurrencyMark());
						temp.put("currencyName", currency.getCurrencyName());
						map.put(airlinePrice.getCurrencyId().toString(), temp);
					}
				}
			}
		/*}*/
		List<Map<String,String>> listRet = new ArrayList<Map<String,String>>();
		List<Currency> currency = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
		for(Currency c:currency){
			if(map.get(c.getId().toString())!=null){
				listRet.add(map.get(c.getId().toString()));
			}else{
				Map<String,String> temp = new HashMap<String,String>();
				temp.put("currencyUuid", c.getId().toString());
				temp.put("exchangeRate", c.getConvertLowest().toString());
				temp.put("currencyCode", c.getCurrencyMark());
				temp.put("currencyName", c.getCurrencyName());
				listRet.add(temp);
			}
		}
		return listRet;
	}
	/**
	 * 订单列表支出单查询接口
	 * @author zhangchao
	 * @time 2016/1/29
	 */
	@SuppressWarnings("unchecked")
	@Override
	public QueryOrderJsonBean queryPayOrder(String orderUuid) {
		QueryOrderJsonBean queryOrderJsonBean=new QueryOrderJsonBean();
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT ao.group_code as groupNo,cr.createBy as applicantId,");
		sbf.append("4 as fundsType ");
		sbf.append("from airticket_order ao ")
			.append("left join cost_record cr on cr.orderId=ao.id ")
			.append("where ao.id=? and ao.del_flag=0 and cr.delFlag=0  and cr.orderType=7 and cr.budgetType=1 ")
			.append(" union ");
		sbf.append("SELECT ao.group_code as groupNo,amount.createBy as applicantId,");
		sbf.append("amount.moneyType as fundsType ");
		sbf.append("from airticket_order ao ")
			.append("left join airticket_order_moneyAmount amount on ao.id=amount.airticket_order_id ")
			.append("where ao.id=? and ao.del_flag=0  and amount.delFlag=0  ");
		List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
		list=mtourCommonDao.findBySql(sbf.toString(), Map.class,Integer.parseInt(orderUuid),Integer.parseInt(orderUuid));
		//String groupNo="";
		//Integer applicantId=null;
		//String applicant="";
		Integer fundsType=null;
		if (!list.isEmpty()) {
			//groupNo=list.get(0).get("groupNo").toString();
			//applicantId=Integer.parseInt(list.get(0).get("applicantId").toString());
			fundsType=Integer.parseInt(list.get(0).get("fundsType").toString());
		}
		AirticketOrder order = iAirticketOrderDao.getAirticketOrderById(Long.parseLong(orderUuid));
		queryOrderJsonBean.setGroupNo(order.getGroupCode());
		queryOrderJsonBean.setApplicantId(UserUtils.getUser().getId().intValue());
		queryOrderJsonBean.setApplicant(UserUtils.getUser().getName());
		queryOrderJsonBean.setFundsType(fundsType);
		StringBuffer sb=new StringBuffer();
		sb.append("SELECT supplyId as paymentObjectUuid,supplyName as paymentObjectName ,1 as tourOperatorChannelCategoryCode from cost_record ");
		sb.append("where orderId=? and orderType=7 and delFlag=0 and budgetType=1 and payStatus!=2 and payStatus!=4 ");
		sb.append("union ");
		sb.append("SELECT case when ao.agentinfo_id=-1 then -1 else ao.agentinfo_id END as paymentObjectUuid,ao.nagentName as paymentObjectName, ")
		.append("2 as tourOperatorChannelCategoryCode ")	
		.append("from airticket_order ao ")
			.append("left join airticket_order_moneyAmount ma on ao.id=ma.airticket_order_id ")
			.append("where ma.moneyType!=4 and ao.id=? and ma.delFlag=0 and  status=1 ");
		List<Map<String,Object>> objs=new ArrayList<Map<String,Object>>();
		objs=mtourCommonDao.findBySql(sb.toString(), Map.class,Integer.parseInt(orderUuid),Integer.parseInt(orderUuid));
		queryOrderJsonBean.setPaymentObject(objs);
		return queryOrderJsonBean;
	}

	@Override
	public void updateOrderPayRepeatSubmit(Long receiveUuid) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("UPDATE orderpay o SET o.repeatSubmit=? WHERE o.id=?");
		mtourCommonDao.updateBySql(sbf.toString(),1, receiveUuid);
	}

	/**
	 * 根据订单的id，查询是否有已确认的收款数据，或者是否有已付款的退款和已付款的追加成本数据。如果能够查到该订单的上面3项数据，
	 * 则预订人信息项不可以修改，返回false。查询不到则可以修改，返回true。对应需求0294. yudong.xu 2016.6.7
	 * @param orderId
	 * @return
     */
	public Boolean reservationEditable(String orderId){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT flag FROM (SELECT 'x' AS flag FROM orderpay o WHERE o.orderId=? AND o.orderType =7 AND o.isAsAccount=1 ")
		   .append(" UNION ALL  ")
		   .append("SELECT 'x' AS flag FROM airticket_order_moneyAmount a WHERE a.airticket_order_id =? AND a.paystatus=1 ")
		   .append("AND a.moneyType IN (2,3)) oa WHERE 1=1 LIMIT 1");
		List<Map<String,Object>> result = mtourCommonDao.findBySql(sql.toString(), Map.class, orderId,orderId);
		if (result == null || result.size()== 0) {
			return true;
		}
		return false;
	}

	/**
	 * 待确认订单生成正常订单
	 * @author wangyang
	 * @date 2016.6.15
	 * */
	@Override
	public boolean confirmOrder(String orderUuid) {

		Long orderId = Long.valueOf(orderUuid);
		AirticketOrder order = airTicketOrderService.getAirticketorderById(orderId);
		
		if(order != null){
			order.setLockStatus(AirticketOrder.LOCKSTATUS_ZC);
			airTicketOrderService.updateAirticketOrder(order);
			return true;
		}

		return false;
	}

	/**
	 * 获取非签约渠道
	 * @param page
	 * @param request
	 * @return
	 * @author chao.zhang@quauq.com
	 * 美图专用
	 */
	@Override
	public Page<Map<String, Object>> getNoAgent(Page<Map<String, Object>> page,
			HttpServletRequest request) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT ");
		sbf.append("a.id,a.agentName AS nagentName, u. NAME AS createName ");
		sbf.append("FROM ");
		sbf.append("agentinfo a ");
		sbf.append("LEFT JOIN sys_user u ON u.id=a.agentSalerId  ");
		sbf.append("WHERE ");
		sbf.append("a.is_uncontract=? ");
		sbf.append("AND a.delFlag=? ");
		sbf.append("AND a.supplyId=? ");
		if(StringUtils.isNotEmpty(request.getParameter("salerId"))){
			sbf.append("AND a.agentSalerId="+request.getParameter("salerId")+" ");
		}
		if(StringUtils.isNotEmpty(request.getParameter("minCreateDate"))){
			sbf.append("AND a.createDate>='"+request.getParameter("minCreateDate")+"' ");
		}
		if(StringUtils.isNotEmpty(request.getParameter("maxCreateDate"))){
			sbf.append("AND a.createDate<='"+request.getParameter("maxCreateDate")+"' ");
		}
		if(StringUtils.isNotEmpty(request.getParameter("nagentName"))){
			sbf.append("AND a.agentName LIKE '%"+request.getParameter("nagentName")+"%' ");
		}
		//sbf.append("GROUP BY o.nagentName");
		page=mtourCommonDao.findBySql(page, sbf.toString(),Map.class, 1,0,UserUtils.getUser().getCompany().getId());
		return page;
	}

	/**
	 * 获得团号（用于组装批量下载收入单的团号）
	 * @param orderUuids
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getGroupCode(String orderUuids) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT ");
		sbf.append("group_code AS groupNo, ");
		sbf.append("id ");
		sbf.append("FROM ");
		sbf.append("airticket_order ");
		sbf.append("WHERE ");
		sbf.append("id in("+orderUuids+") AND del_flag=0 ");
		List<Map<String,Object>> list=mtourCommonDao.findBySql(sbf.toString(),Map.class);
		return list;
	}

	/**
	 * 获得收款对象
	 * @param orderUuid
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getPayObj(Integer orderUuid) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT DISTINCT ");
		sbf.append("a.nagentName AS paymentObjectName, ");
		sbf.append("a.group_code AS groupCode, ");
		sbf.append("a.agentinfo_id AS paymentObjectUuid ");
		sbf.append("FROM ");
		sbf.append("airticket_order a ");
		sbf.append("LEFT JOIN orderpay op ON a.id=op.orderId  ");
		sbf.append("LEFT JOIN sys_user u ON op.createBy=u.id  ");
		sbf.append("LEFT JOIN money_amount ma ON op.moneySerialNum = ma.serialNum  ");
		sbf.append("LEFT JOIN currency currency ON currency.currency_id = ma.currencyId  ");
		sbf.append("WHERE ");
		sbf.append("a.id = ? ");
		sbf.append("AND u.delFlag=0 ");
		sbf.append("AND ma.delFlag=0 ");
		sbf.append("AND op.delFlag=0 ");
		sbf.append("UNION ");
		sbf.append("SELECT DISTINCT ");
		sbf.append("cr.supplyName AS paymentObjectName, ");
		sbf.append("o.group_code AS groupCode, ");
		sbf.append("cr.supplyId AS paymentObjectUuid ");
		sbf.append("FROM ");
		sbf.append("cost_record cr ");
		sbf.append("LEFT JOIN airticket_order o ON o.id = cr.orderId ");
		sbf.append("LEFT JOIN pay_group pay ON pay.cost_record_id = cr.id  ");
		sbf.append("LEFT JOIN currency currency ON currency.currency_id= cr.currencyId ");
		sbf.append("WHERE ");
		sbf.append("cr.orderId = ? ");
		sbf.append("AND cr.orderType = ? ");
		sbf.append("AND cr.delFlag = ? ");
		sbf.append("AND pay.delFlag = 0 ");
		List<Map<String,Object>> list=mtourCommonDao.findBySql(sbf.toString(), Map.class,orderUuid,orderUuid,7,0);
		return list;
	}

	/**
	 * 获得具体收款信息
	 * @param orderUuid
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getPaymentObject(Integer orderUuid) {
		StringBuffer sbf=new StringBuffer();
		sbf.append("SELECT ");
		sbf.append("ao.nagentName AS paymentObjectName,op.createDate AS approvalDate, ");
		sbf.append("op.payPriceType AS payPriceType, ");
		sbf.append("CASE op.payPriceType WHEN 1 THEN '订金' ");
		sbf.append("WHEN 2 THEN '尾款' ");
		sbf.append("WHEN 3 THEN '全款' ");
		sbf.append("ELSE  '追散' ");
		sbf.append("END AS fundsType, ");
		sbf.append("op.createBy AS createBy, ");
		sbf.append("u.name AS fundsName, ");
		sbf.append("case op.payTypeName when '现金支付' then '现金' else op.payTypeName end AS fundsMode,op.id AS payObjectUuid,ma.amount AS fundsCost, ");
		sbf.append("currency.currency_mark AS currencyMark, ");
		sbf.append("ma.currencyId AS currencyId, ");
		sbf.append("ao.agentinfo_id as paymentObjectUuid ");
		sbf.append("FROM airticket_order ao ");
		sbf.append("LEFT JOIN orderpay op ON ao.id=op.orderId  ");
		sbf.append("LEFT JOIN sys_user u ON op.createBy=u.id  ");
		sbf.append("LEFT JOIN money_amount ma ON op.moneySerialNum = ma.serialNum  ");
		sbf.append("LEFT JOIN currency currency ON currency.currency_id = ma.currencyId  ");
		sbf.append("WHERE ao.id =? AND op.delFlag=? AND op.orderType=? AND (op.isAsAccount IS NULL OR op.isAsAccount = 99 OR op.isAsAccount = 1) ");
		sbf.append("UNION  ");
		sbf.append("SELECT  ");
		sbf.append("cr.supplyName AS paymentObjectName,pay.createDate AS approvalDate,  ");
		sbf.append("5 AS payPriceType,'其他收入' AS fundsType,  ");
		sbf.append("pay.createBy AS createBy,u.name AS fundsName,  ");
		sbf.append("pay.payTypeName AS fundsMode,pay.id AS payObjectUuid,  ");
		sbf.append("cr.price AS fundsCost,currency.currency_mark AS currencyMark,cr.currencyId AS currencyId,  ");
		sbf.append("cr.supplyId as paymentObjectUuid ");
		sbf.append("FROM cost_record cr  ");
		sbf.append("LEFT JOIN pay_group pay ON pay.cost_record_id = cr.id  ");
		sbf.append("LEFT JOIN currency currency ON currency.currency_id= cr.currencyId ");
		sbf.append("AND pay.orderType = cr.orderType  ");
		sbf.append("LEFT JOIN sys_user u ON u.id = pay.createBy  ");
		sbf.append("WHERE cr.orderId =? and cr.orderType=? AND (pay.isAsAccount IS NULL OR pay.isAsAccount = 99 OR pay.isAsAccount = 1) ");
		sbf.append("AND pay.delFlag=? AND cr.payStatus!=2 ");
		List<Map<String,Object>> list=mtourCommonDao.findBySql(sbf.toString(),Map.class, orderUuid,0,7,orderUuid,7,0);
		return list;
	}

	/**
	 * 根据以11,12,13字符串形式传入的参数，查询数据库获取这些订单的所有支出信息，用于批量查询订单的支出单。
	 * yudong.xu 2016.7.6
     */
	private List<Map<String,Object>> getPaymentInfo(String ids){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT o.id AS orderUuid,o.group_code AS groupNo,cr.id AS paymentUuid,")
		   .append("DATE_FORMAT(cr.createDate,'%Y-%m-%d') AS approvalDate,cr.supplyId AS paymentObjectUuid,")
		   .append("cr.supplyName AS paymentObjectName,'4' AS fundsType,'成本' AS fundsTypeName,cr.`name` AS fundsName,")
		   .append("CASE cr.isJoin WHEN 1 THEN '' ELSE cr.bigCode END AS fundsPNR,cr.price*cr.quantity AS amount,cr.currencyId,")
		   .append("(SELECT c.currency_mark FROM currency c WHERE cr.currencyId=c.currency_id) AS currencyMark")
		   .append(" FROM cost_record cr,airticket_order o ")
		   .append(" WHERE cr.orderId = o.id AND cr.orderType = 7 AND cr.delFlag = '0' AND cr.budgetType = 1 ")
		   .append(" AND cr.payStatus NOT IN (2, 4) AND cr.orderId IN (").append(ids).append(")")
		   .append(" UNION ALL ")
		   .append("SELECT o.id AS orderId,o.group_code AS groupNo,ma.id AS paymentUuid,")
		   .append("DATE_FORMAT(ma.createDate,'%Y-%m-%d') AS approvalDate,o.agentinfo_id AS paymentObjectUuid,")
		   .append("o.nagentName AS paymentObjectName,ma.moneyType AS fundsType,")
		   .append("CASE ma.moneyType WHEN 1 THEN '借款' WHEN 2 THEN '退款' WHEN 3 THEN '追加成本' END AS fundsTypeName,")
		   .append("ma.funds_name AS fundsName,'' AS fundsPNR,ma.amount,ma.currency_id AS currencyId,")
		   .append("(SELECT currency_mark FROM currency c WHERE c.currency_id=ma.currency_id) AS currencyMark ")
		   .append(" FROM airticket_order o LEFT JOIN airticket_order_moneyAmount ma ON o.id = ma.airticket_order_id ")
		   .append(" WHERE ma.moneyType != 4 AND ma.delFlag = 0 AND ma.`status` = 1 ")
		   .append(" AND o.id IN(").append(ids).append(")");
		return mtourCommonDao.findBySql(sql.toString(),Map.class);
	}

	/**
	 * 根据订单的id列表，如1,2,3,4 查询对应的订单团号和订单id。yudong.xu 2016.7.19
     */
	public List<Map<String,Object>> getOrderGroupNo(String ids){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT o.id AS orderUuid,o.group_code AS groupNo FROM airticket_order o ")
		.append("WHERE o.id IN (").append(ids).append(")");
		return mtourCommonDao.findBySql(sql.toString(),Map.class);
	}



	/**
	 * 参数是以逗号分开的多个订单id字符串。该方法功能是查询数据库获取这些订单的支出信息，并根据这些查询出来的原始信息进行分组，组装。
	 * 最终组装成前端需要的格式。yudong.xu 2016.7.6
     */
	public BatchOrderPaymentJsonBean batchQueryPayOrder(String ids){
		if (StringUtils.isBlank(ids)) return null;
		List<Map<String,Object>> groupNos = getOrderGroupNo(ids);
		List<Map<String,Object>> result = getPaymentInfo(ids);
		Map<Integer,OrderPaymentJsonBean> groupByOrder = new LinkedHashMap<>();

		for (Map<String, Object> map : groupNos) {
			OrderPaymentJsonBean dataBean = new OrderPaymentJsonBean();
			Integer orderUuid = (Integer)map.get("orderUuid");
			dataBean.setGroupNo((String)map.get("groupNo"));
			dataBean.setOrderUuid(orderUuid);

			HashSet<PaymentObjJsonBean> newObjSet = new LinkedHashSet();//初始化支付对象Set
			dataBean.setPayObj(newObjSet);

			List<PaymentItemData> paymentItemList = new ArrayList<>();//初始化支付内容List
			dataBean.setPaymentObject(paymentItemList);

			groupByOrder.put(orderUuid,dataBean);//放入分组map中。
		}

		for (Map<String, Object> map : result) {
			PaymentItemData paymentItem = new PaymentItemData();
			paymentItem.setPaymentUuid((Integer)(map.get("paymentUuid")));
			paymentItem.setApprovalDate(map.get("approvalDate").toString());
			paymentItem.setFundsType(map.get("fundsType").toString());
			paymentItem.setFundsTypeName(map.get("fundsTypeName").toString());
			paymentItem.setFundsName(map.get("fundsName").toString());
			paymentItem.setFundsPNR((String) map.get("fundsPNR"));
			BigDecimal amount = (BigDecimal)map.get("amount");
			String currencyMark = map.get("currencyMark").toString();
			paymentItem.setCurrencyId((Integer)map.get("currencyId"));
			paymentItem.setFundsCost(currencyMark+amount);
			String paymentObjectUuid = map.get("paymentObjectUuid").toString();
			String paymentObjectName = map.get("paymentObjectName").toString();
			paymentItem.setPaymentObjectUuid(paymentObjectUuid);
			paymentItem.setPaymentObjectName(paymentObjectName);

			//创建支付对象的JsonBean，在OrderPaymentJsonBean以Set形式保存。去重。
			PaymentObjJsonBean payObject = new PaymentObjJsonBean(paymentObjectUuid,paymentObjectName);

			//根据订单id在map中获取对应的Bean。
			Integer orderUuid = (Integer)map.get("orderUuid");
			OrderPaymentJsonBean orderPayment = groupByOrder.get(orderUuid);
			orderPayment.getPayObj().add(payObject);//放入支付对象
			orderPayment.getPaymentObject().add(paymentItem);//增加支付条目item
		}
		//组装最终返回的结果JsonBean
		BatchOrderPaymentJsonBean bean = new BatchOrderPaymentJsonBean();
		bean.setApplicantId(UserUtils.getUser().getId());
		bean.setApplicantName(UserUtils.getUser().getName());
		bean.setExpenditureList(groupByOrder.values());
		return bean;
	}

}
