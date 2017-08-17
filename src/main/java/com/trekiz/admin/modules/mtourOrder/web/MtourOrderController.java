/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourOrder.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.common.input.BaseInput4MT;
import com.trekiz.admin.common.input.BaseOut4MT;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.mtourCommon.dao.MtourCommonDao;
import com.trekiz.admin.modules.mtourCommon.service.SerialNumberService;
import com.trekiz.admin.modules.mtourCommon.web.MtourBaseController;
import com.trekiz.admin.modules.mtourOrder.entity.*;
import com.trekiz.admin.modules.mtourOrder.jsonbean.*;
import com.trekiz.admin.modules.mtourOrder.query.AirticketOrderMoneyAmountQuery;
import com.trekiz.admin.modules.mtourOrder.service.*;
import com.trekiz.admin.modules.mtourfinance.service.FinanceService;
import com.trekiz.admin.modules.supplier.repository.SupplierInfoDao;
import com.trekiz.admin.modules.sys.entity.AirportInfo;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.AirportInfoService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 美图国际通用数据接口
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/mtour/order")
public class MtourOrderController extends MtourBaseController {
	
	
	@Autowired
	private MtourOrderService mtourOrderService;
	
	@Autowired
	private AirticketOrderPnrService airticketOrderPnrService;
	
	@Autowired
	private AirticketOrderPnrGroupService airticketOrderPnrGroupService;
	
	@Autowired
	private AirticketOrderOperateRecordService airticketOrderOperateRecordService;
	
	@Autowired
	private SupplierInfoDao supplierInfoDao;
	
	@Autowired
	private MtourCommonDao mtourCommonDao;
	
	@Autowired
	private AirticketOrderMoneyAmountService airticketOrderMoneyAmountService;
	
	@Autowired
	private AirportInfoService airportInfoService;
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	@Autowired
	private IActivityAirTicketService  activityAirTicketService;
	@Autowired
	private SerialNumberService serialNumberService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private AirticketOrderPnrAirlinePriceService airticketOrderPnrAirlinePriceService;
	@Autowired
	private CostRecordDao costRecordDao;
	@Autowired
	private FinanceService financeService;
	
	
	@RequestMapping(value = "list")
	public String mtourIndex() {
        return "modules/mtour/orderlist";
	}
	
	/**
	 * PNR列表展开，输入：orderUuid
	 * @author ang.gao
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "showAirticketOrderPNRList")
	public String showAirticketOrderPNRList(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		//输入参数：机票订单id
		String orderId = input.getParamValue("orderUuid");
		if(StringUtils.isNotBlank(orderId)){
			
			//update by zhanghao 20160108 增加返回的结算单锁状态
			AirticketOrder order = airTicketOrderService.getAirticketorderById(Long.parseLong(orderId));
			ActivityAirTicket airticket = activityAirTicketService.getActivityAirTicketById(order.getAirticketId());
			//update by zhanghao 20160108 增加返回的结算单锁状态
			
			//要返回的JSON对象集合
			List<AirticketOrderPNRListJsonBean> jsonBeanList = new ArrayList<AirticketOrderPNRListJsonBean>();
			
			List<Map<String,Object>> PNRGroupList = mtourOrderService.getBaseInfoPNR(orderId);
			if(CollectionUtils.isNotEmpty(PNRGroupList)){
				for(Map<String,Object> PNRGroupMap : PNRGroupList){
					AirticketOrderPNRListJsonBean airticketOrderPNR = new AirticketOrderPNRListJsonBean();
					airticketOrderPNR.setOrderUuid(orderId);//订单id
					airticketOrderPNR.setInvoiceOriginalGroupUuid(PNRGroupMap.get("invoiceOriginalGroupUuid").toString());//大编号组Uuid
					airticketOrderPNR.setDrawerCount(Integer.parseInt(PNRGroupMap.get("drawerCount").toString()));//出票人数
					airticketOrderPNR.setReserveCount(Integer.parseInt(PNRGroupMap.get("reserveCount").toString()));//预定人数	
					
					//update by zhanghao 20160108 增加返回的结算单锁状态
					airticketOrderPNR.setLockStatus(airticket.getLockStatus());
					
					
					// invoiceOriginals
					List<Map<String,Object>> resultList = mtourOrderService.getPNRInvoiceOriginals(PNRGroupMap.get("invoiceOriginalGroupUuid").toString());
					if(CollectionUtils.isNotEmpty(resultList)){
						for(Map<String,Object> PNRMap : resultList){
							if(PNRMap.get("invoiceOriginalTypeCode").toString().equals("0")){//大编号类型为PNR
								PNRMap.put("tourOperatorName", "");//地接社Name
							}else if(PNRMap.get("invoiceOriginalTypeCode").toString().equals("1")){//大编号类型为地接社
								String sql = " SELECT si.supplierName AS tourOperatorName FROM supplier_info si WHERE si.id=?";
								List<Object> list = supplierInfoDao.findBySql(sql, PNRMap.get("PNR").toString());
								PNRMap.put("tourOperatorName", list.get(0).toString());//地接社Name				
								PNRMap.put("PNR","");//此时PNR无意义												
							}else{
								out.setResponseCode4Fail();
								out.putMsgCode(BaseOut4MT.CODE_0002);
								out.putMsgDescription("大编号类型错误");
								return toAndCacheJSONString(out);
							}
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							Map<String,Object> deadline = new HashMap<String,Object>();
							deadline.put("ticketDeadline", PNRMap.get("ticketDeadline")!=null?sdf.format(PNRMap.remove("ticketDeadline")):"");  //出票时限
							deadline.put("depositDeadline", PNRMap.get("depositDeadline")!=null?sdf.format(PNRMap.remove("depositDeadline")):"");//定金时限
							deadline.put("listDeadline", PNRMap.get("listDeadline")!=null?sdf.format(PNRMap.remove("listDeadline")):"");      //名单时限
							deadline.put("renameDeadline", PNRMap.get("renameDeadline")!=null?sdf.format(PNRMap.remove("renameDeadline")):"");  //改名时限
							deadline.put("cancelDeadline", PNRMap.get("cancelDeadline")!=null?sdf.format(PNRMap.remove("cancelDeadline")):"");  //取消时限
							PNRMap.put("deadline", deadline);
							
							if(StringUtils.isNotBlank(PNRMap.get("uuid").toString())){
								String sql = "SELECT aopa.airline_name AS airlineName FROM airticket_order_pnr_airline aopa LEFT JOIN airticket_order_pnr aop ON aopa.airticket_order_pnr_uuid=aop.uuid WHERE aopa.delFlag='0' AND aop.uuid=?";
								@SuppressWarnings("unchecked")
								List<Map<String,Object>> airLineNameList = mtourCommonDao.findBySql(sql, Map.class, PNRMap.get("uuid").toString());
								if(CollectionUtils.isNotEmpty(airLineNameList)){
									PNRMap.put("airlines", airLineNameList);//航段名称
								}else{
									PNRMap.put("airlines", "");
								}					
							}
							PNRMap.remove("uuid");
						}		
					}
					airticketOrderPNR.setInvoiceOriginals((resultList==null)? new ArrayList<Map<String,Object>>():resultList);					
					jsonBeanList.add(airticketOrderPNR);
				}							
			}
			out.setResponseCode4Success();
			out.setData(jsonBeanList);			
		}else{
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("传递参数异常");
		}
 		return toAndCacheJSONString(out);
	}
	
	/**
	 * 机票快速订单保存接口
	 * @author hhx
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveAirticketOrder")
	public String saveAirticketOrder(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			out = mtourOrderService.saveAirticketOrder(input);
		}catch (Exception e) {
			e.printStackTrace();
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("程序异常,请返回重试.");
		}
 		return toAndCacheJSONString(out);
	}
	
	/**
	 * 机票快速订单保存接口(非美图用)
	 * @author hhx
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveAirticketOrderForCommon")
	public String saveAirticketOrderForCommon(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			out = mtourOrderService.saveAirticketOrderForCommon(input);
		}catch (Exception e) {
			e.printStackTrace();
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("程序异常,请返回重试.");
		}
 		return toAndCacheJSONString(out);
	}
	
	/**
	 * 订单详情
	 * @param 
	 * @return
	 * @author wangxv
	 */ 
	@ResponseBody
	@RequestMapping(value = "showAirticketOrderDetail")
	public String showAirticketOrderDetail(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		//获得传递的参数值
		String orderId = input.getParamValue("orderUuid");
		String operatorType = input.getParamValue("operatorType"); // 0:快速生成订单；1:订单修改	2:订单详情
		String productId = null;
		if(StringUtils.isNotEmpty(orderId)){
			AirticketOrderDetailJsonBean mDetai = new AirticketOrderDetailJsonBean();
			List<Map<String,String>> baseInfoList = mtourOrderService.getBaseInfoDetail(orderId);
			if(CollectionUtils.isNotEmpty(baseInfoList)){
				Map<String,String> baseInfoMap = baseInfoList.get(0);
				mDetai.setBaseInfo(baseInfoMap);
				//备注信息的添加
				mDetai.setMemo(baseInfoMap.get("memo"));
				productId = String.valueOf(baseInfoMap.get("id"));
			}
			List<Map<String,Object>> reservationList = mtourOrderService.getReservationDetail(orderId);
			if(CollectionUtils.isNotEmpty(reservationList)){
				Map<String,Object> reservationMap = new HashMap<String,Object>();
				reservationMap.put("channelTypeUuid", reservationList.get(0).get("channelUuid").toString());
				reservationMap.put("channelUuid", reservationList.get(0).get("channelUuid").toString());
				if(StringUtils.isNotBlank((String)reservationList.get(0).get("channelCode"))){
					reservationMap.put("channelCode", reservationList.get(0).get("channelCode").toString());
				}else{
					reservationMap.put("channelCode", "");
				}
				reservationMap.put("channelName", reservationList.get(0).get("channelName").toString());
				reservationMap.put("channelTypeCode", reservationList.get(0).get("channelTypeCode").toString());
				reservationMap.put("contacts", reservationList);
				Boolean editable = false; //订单详情不可修改预订人渠道信息
				if ("1".equals(operatorType)){ //订单修改，需要看情况
					editable = mtourOrderService.reservationEditable(orderId);
				}
				reservationMap.put("editable", editable);
				mDetai.setReservations(reservationMap);
			}
			// fee
			Map<String, Object> feeMap = new HashMap<String,Object>();
			//invoiceOriginalGroups    
			List<Map<String,Object>> invoiceOriginalGroupsList = mtourOrderService.getinvoiceOriginalGroupDetail(orderId);
			// invoiceOriginals
			if(CollectionUtils.isNotEmpty(invoiceOriginalGroupsList)){
				for(Map<String,Object> invoiceOriginalGroup:invoiceOriginalGroupsList){
					List<Map<String,Object>> invoiceOriginalsList = mtourOrderService.getInvoiceOriginalDetail(invoiceOriginalGroup.get("invoiceOriginalGroupUuid").toString());
					if(CollectionUtils.isNotEmpty(invoiceOriginalsList)){
						for(Map<String,Object> temp : invoiceOriginalsList){
							Map<String,Object> deadline = new HashMap<String,Object>();
							deadline.put("ticketDeadline", temp.remove("ticketDeadline"));
							deadline.put("depositDeadline", temp.remove("depositDeadline"));
							deadline.put("listDeadline", temp.remove("listDeadline"));
							deadline.put("renameDeadline", temp.remove("renameDeadline"));
							deadline.put("cancelDeadline", temp.remove("cancelDeadline"));
							temp.put("deadline", deadline);
							Map<String,List<Map<String,Object>>> groupMap = new TreeMap<String,List<Map<String,Object>>>();
							List<Map<String,Object>> airlineList = mtourOrderService.getAirlineDetail(temp.get("uuid").toString());
							for(Map<String,Object> tempMap : airlineList){
								if(null!=tempMap.get("id")){
									String key = tempMap.get("id").toString();
									if(!groupMap.containsKey(key)){
										groupMap.put(key, new ArrayList<Map<String,Object>>());
									}
									groupMap.get(key).add(tempMap);
								}
							}
							List<Map<String,Object>> allAirlineList = new ArrayList<Map<String,Object>>();
							for(Map.Entry<String,List<Map<String,Object>>> entry : groupMap.entrySet()){
								Map<String,Object>  airMap = new TreeMap<String,Object>();
								airMap.put("airlineName", entry.getValue().get(0).get("airlineName"));
								airMap.put("aopaUuid", entry.getValue().get(0).get("aopaUuid"));
								List<Map<String,Object>> costList = new ArrayList<Map<String,Object>>();//存放成本
								List<Map<String,Object>> salePriceList = new ArrayList<Map<String,Object>>();//存放外报价
								for(Map<String,Object> priceDetail:entry.getValue()){
									Map<String,Object> costMap = new HashMap<String,Object>();
									Map<String,Object> salePriceMap = new HashMap<String,Object>();
									if("0".equals(priceDetail.get("priceType").toString())){//成本价
										costMap.put("currencyUuid", priceDetail.remove("currencyUuid"));
										costMap.put("currencyName", priceDetail.remove("currencyName"));
										costMap.put("exchangeRate", priceDetail.remove("exchangeRate"));
										costMap.put("peopleCount", priceDetail.remove("peopleCount"));
										costMap.put("costUnitPrice", priceDetail.remove("costUnitPrice"));
										costMap.put("totalCost", priceDetail.remove("totalCost"));
										costMap.put("totalDeposit", priceDetail.remove("totalDeposit"));
										
										costMap.put("crUuid", priceDetail.remove("crUuid"));
										costMap.put("aopapUuid", priceDetail.remove("aopapUuid"));
										costMap.put("costUuid", priceDetail.remove("costUuid"));
										costMap.put("orderCostStateCode", priceDetail.remove("orderCostStateCode"));
										
										//如果成本是未付款状态 则 转到前端的状态变成 已付款状态 update by zhanghao 20160109
										if(costMap.get("orderCostStateCode")!=null && costMap.get("orderCostStateCode").equals("0")){
											costMap.put("orderCostStateCode", "1");
										}
										//如果成本是未付款状态 则 转到前端的状态变成 已付款状态 update by zhanghao 20160109
										costList.add(costMap);
									}else{
										salePriceMap.put("currencyUuid", priceDetail.remove("currencyUuid"));
										salePriceMap.put("currencyName", priceDetail.remove("currencyName"));
										salePriceMap.put("exchangeRate", priceDetail.remove("exchangeRate"));
										salePriceMap.put("peopleCount", priceDetail.remove("peopleCount"));
										salePriceMap.put("costUnitPrice", priceDetail.remove("costUnitPrice"));
										salePriceMap.put("totalCost", priceDetail.remove("totalCost"));
										salePriceMap.put("totalDeposit", priceDetail.remove("totalDeposit"));
										salePriceMap.put("aopapUuid", priceDetail.remove("aopapUuid"));
										salePriceList.add(salePriceMap);
									}
								}
								airMap.put("costs", costList);
								airMap.put("salePrices", salePriceList);
								allAirlineList.add(airMap);
							}
							temp.put("airlines", allAirlineList);
						}
					}
					invoiceOriginalGroup.put("invoiceOriginals", invoiceOriginalsList);
				}
			}
			//priceChange
			Map<String,Object>  priceChange = new HashMap<String,Object>();
			List<Map<String,String>> additionalCost = mtourOrderService.getAdditionalPriceChangeDetail(orderId,3);
			if(CollectionUtils.isNotEmpty(additionalCost)){
				priceChange.put("additionalCosts", additionalCost);
			}
			List<Map<String,String>> priceChangeList = mtourOrderService.getSalePriceChangeDetail(orderId);
			if(CollectionUtils.isNotEmpty(priceChangeList)){
				priceChange.put("salePrices", priceChangeList);
			}
			feeMap.put("priceChange", priceChange);
			//all kinds of price
			List<Map<String,String>> orderAmount = mtourOrderService.getPriceDetail("total_money",orderId);
			List<Map<String,String>> receivableAmount = mtourOrderService.getPriceDetail("original_total_money",orderId);
			List<Map<String,String>> receivedAmount = mtourOrderService.getPriceDetail("payed_money",orderId);
			List<Map<String,String>> arrivedAmount = mtourOrderService.getPriceDetail("accounted_money",orderId);
			//退款
			AirticketOrderMoneyAmountQuery airticketOrderMoneyAmountQuery = new AirticketOrderMoneyAmountQuery();
			airticketOrderMoneyAmountQuery.setAirticketOrderId(Integer.valueOf(orderId));
			airticketOrderMoneyAmountQuery.setMoneyType(2);//款项类型（借款：1，退款：2，追加成本：3）
			List<Map<String,Object>> returnAmountList = airticketOrderMoneyAmountService.queryAirticketOrderMoneyAmountList(airticketOrderMoneyAmountQuery);
			feeMap.put("orderAmount", orderAmount);
			feeMap.put("receivableAmount", receivableAmount);
			feeMap.put("receivedAmount", receivedAmount);
			feeMap.put("arrivedAmount", arrivedAmount);
			feeMap.put("invoiceOriginalGroups", invoiceOriginalGroupsList);
			feeMap.put("returnAmount", returnAmountList);
			mDetai.setFee(feeMap);
			List<Map<String,String>> attachmentList = mtourOrderService.getAttachmentDetail(productId);
			if(CollectionUtils.isNotEmpty(attachmentList)){
				mDetai.setAttachment(attachmentList);
			}
			List<Map<String,Object>> travelerList = mtourOrderService.getTravelerDetail(orderId);
			if(CollectionUtils.isNotEmpty(travelerList)){
				for(Map<String,Object> temp : travelerList){
					 if(temp.get("travelerUuid")!=null){
							String travelerId = temp.get("travelerUuid").toString();
							List<Map<String,Object>> travelervisasList = mtourOrderService.getTravelerVisas(travelerId);
							List<Map<String,Object>> travelerCredentialsList = mtourOrderService.getTravelerCredentials(travelerId);
							temp.put("credentials", travelerCredentialsList);
							temp.put("visas", travelervisasList);
						}
				}
				mDetai.setTravelers(travelerList);
			}
			if(productId!=null){
				List<Map<String, String>> flightList = mtourOrderService.getFlightDetail(productId);
				if(CollectionUtils.isNotEmpty(flightList)){
					for(Map<String, String> temp:flightList){
						if(temp.get("departureAirportUuid")!=null){//出发机场信息
							AirportInfo  departureAirportInfo = airportInfoService.getAirportInfo(Long.parseLong(temp.get("departureAirportUuid")));
							temp.put("departureAirportName", departureAirportInfo.getAirportName());
							temp.put("departureAirportCode", departureAirportInfo.getAirportCode());
						}
						if(temp.get("arrivalAirportUuid")!=null){//到达机场信息
							AirportInfo  arrivalAirportInfo = airportInfoService.getAirportInfo(Long.parseLong(temp.get("arrivalAirportUuid")));
							temp.put("arrivalAirportName", arrivalAirportInfo.getAirportName());
							temp.put("arrivalAirportCode", arrivalAirportInfo.getAirportCode());
						}
					}
				}
				mDetai.setFlights(flightList);
			}
			out.setResponseCode4Success();
			out.setData(mDetai);
		}else{
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("传递参数异常");
		}
//		System.out.println(toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD));
 		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
	}
	
	/**
	 * 机票订单追散保存
	 * @author hhx
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addToAirticketOrder")
	public String addToAirticketOrder(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			out = mtourOrderService.addToAirticketOrder(input);
		}catch (Exception e) {
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("程序异常,请返回重试.");
		}
 		return toAndCacheJSONString(out);
	}
	
	/**
	 * 机票订单追散保存（非美图使用）
	 * @author hhx
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "addToAirticketOrderForCommon")
	public String addToAirticketOrderForCommon(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			out = mtourOrderService.addToAirticketOrderForCommon(input);
		}catch (Exception e) {
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("程序异常,请返回重试.");
		}
		return toAndCacheJSONString(out);
	}
	
	/**
	 * PNR(列表)-展开-查看记录
	 * @author ang.gao
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "showPNRRecord")
	public String showPNRRecord(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		String orderId = input.getParamValue("orderUuid");//订单id
		String invoiceOriginalGroupUuid = input.getParamValue("invoiceOriginalGroupUuid");//大编号组Uuid
		if(StringUtils.isNotBlank(invoiceOriginalGroupUuid) || StringUtils.isNotBlank(orderId)){
			List<PNRRecordJsonBean> list = mtourOrderService.getPNRRecord(orderId, invoiceOriginalGroupUuid);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for(PNRRecordJsonBean jsonBean : list){
				String name = UserUtils.getUserNameById(Long.parseLong(jsonBean.getModifier()));
				jsonBean.setModifier(name);
				try {
					jsonBean.setModifyDate(sdf.format(sdf.parse(jsonBean.getModifyDate())));
				} catch (ParseException e) {
					e.printStackTrace();
					out.setResponseCode4Fail();
					out.putMsgCode(BaseOut4MT.CODE_0002);
					out.putMsgDescription("日期转换发生错误!");
					return toAndCacheJSONString(out);
				}
			}
			out.setResponseCode4Success();
			out.setData(list);
		}else{
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("参数错误：请输入大编号组Uuid或订单id!");
		}
		return toAndCacheJSONString(out);
	}
	
	/**
	 * order/getAirticketOrderMoneyAmountList
	 {
		"orderUuid":"260",
		"fundsType":"2"
		}
	 * 借款/退款/追加成本记录列表接口查询   借款：1，退款：2，追加成本：3
	 * @author WangXK
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getAirticketOrderMoneyAmountList")
	public String getAirticketOrderMoneyAmountList(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		List<Map<String,Object>> list= new ArrayList<Map<String,Object>>();
		AirticketOrderMoneyAmountQuery airticketOrderMoneyAmountQuery = new AirticketOrderMoneyAmountQuery();
		//JSONObject o = JSONObject.parseObject(input.getParam());
		try{
			if(StringUtils.isNotBlank(input.getParamValue("orderUuid"))){
				airticketOrderMoneyAmountQuery.setAirticketOrderId(Integer.valueOf(input.getParamValue("orderUuid")));
				//airticketOrderMoneyAmountQuery.setMoneyType(o.getInteger("fundsType"));
				airticketOrderMoneyAmountQuery.setMoneyType(input.getParamValue4Object("fundsType", Integer.class));
				list = airticketOrderMoneyAmountService.queryAirticketOrderMoneyAmount(airticketOrderMoneyAmountQuery);
				out.setResponseCode4Success();
				if(CollectionUtils.isNotEmpty(list)){
					out.setData(list);
				}
			}else{
				out.setResponseCode4Fail();
				out.putMsgCode(BaseOut4MT.CODE_0001);
				if("1".equals(input.getParamValue("fundsTypeCode"))){
					out.putMsgDescription("参数错误：借款订单编号为空！");
				}else if("2".equals(input.getParamValue("fundsTypeCode"))){
					out.putMsgDescription("参数错误：退款订单编号为空！");
				}else if("3".equals(input.getParamValue("fundsTypeCode"))){
					out.putMsgDescription("参数错误：追加成本订单编号为空！");
				}else{
					out.putMsgDescription("参数错误：借款/退款/追加成本记录列表参数错误！");
				}
				out.putMsgDescription("程序异常,请返回重试.");
			}
		}catch(Exception e){
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("程序异常,请返回重试.");
		}
		return toAndCacheJSONString(out);
	}
	
	/**
	 * order/saveAirticketOrderMoneyAmount
	 {
        "orderUuid":"123456",
        "fundsType":"2",
        "fundsName":"退款",
        "currencyUuid":"111111",
        "exchangeRate":"1.1",
        "amount":"100",
        "memo":"备注"
     }
	 * 借款/退款/追加成本记录---提交   借款：1，退款：2，追加成本：3
	 * @author WangXK
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="saveAirticketOrderMoneyAmount")
	@Transactional
	public String saveAirticketOrderMoneyAmount(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		AirticketOrderMoneyAmount airticketOrderMoneyAmount = null;
		Currency currency = null;
		try{
			Long orderId = null;
			JSONArray array = JSONArray.parseArray(input.getParam());
			for(int i=0;array!=null && i<array.size();i++){
				airticketOrderMoneyAmount = new AirticketOrderMoneyAmount();
				JSONObject jobj = (JSONObject)array.get(i);
				if(jobj.getString("orderUuid")!=null){
					currency = mtourOrderService.getOriginalCurrency(Long.valueOf(jobj.getString("orderUuid")), jobj.getString("currencyUuid"));
					orderId = Long.valueOf(jobj.getString("orderUuid"));
				}
				
				airticketOrderMoneyAmount.setAirticketOrderId(Integer.valueOf(jobj.getString("orderUuid")));
				airticketOrderMoneyAmount.setMoneyType(jobj.getInteger("fundsType"));
				airticketOrderMoneyAmount.setFundsName(jobj.getString("fundsName"));
				airticketOrderMoneyAmount.setCurrencyId(Integer.valueOf(jobj.getString("currencyUuid")));
				//edit by majiancheng(2015-11-24)将最低汇率保存
//				airticketOrderMoneyAmount.setExchangerate(currency.getConvertLowest()!=null?currency.getConvertLowest().doubleValue():0.0);
				//0094 by songyang
				airticketOrderMoneyAmount.setExchangerate(Double.parseDouble(jobj.getString("exchangeRate")));
				airticketOrderMoneyAmount.setAmount(Double.valueOf(jobj.getString("amount")));
				airticketOrderMoneyAmount.setMemo(jobj.getString("memo"));
				airticketOrderMoneyAmount.setStatus(1); //已提交
				airticketOrderMoneyAmount.setPaystatus(0);
				airticketOrderMoneyAmountService.save(airticketOrderMoneyAmount);
				serialNumberService.genSerialNumber(SerialNumberService.TABLENAME_AIRTICKET_ORDER_MONEYAMOUNT, airticketOrderMoneyAmount.getId());
			}
			
			//----------更新订单的支付状态 add by majiancheng
			financeService.updateOrderRefundFlag(orderId);
			//----------更新订单的支付状态
			
			out.setResponseCode4Success();
			out.setData("保存成功");
		}catch(Exception e){
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("程序异常,请返回重试.");
		}
		
		return toAndCacheJSONString(out);
	}
	/**
	 order/cancelAirticketOrderMoneyAmount
	 {
        "orderUuid":"123456",
        "fundsType":"2",
        "uuid":"fd95e640204e474b853e504c56c837c3"
     }
	 * 借款/退款/追加成本记录---撤销   借款：1，退款：2，追加成本：3
	 * @param input
	 * @author WangXK
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="cancelAirticketOrderMoneyAmount")
	@Transactional
	public String cancelAirticketOrderMoneyAmount(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		AirticketOrderMoneyAmountQuery airticketOrderMoneyAmountQuery = new AirticketOrderMoneyAmountQuery();
		List<AirticketOrderMoneyAmount> list = new ArrayList<AirticketOrderMoneyAmount>();
		Integer orderid = input.getParamValue4Object("orderUuid", Integer.class);
		
		try{
			if(orderid!=null){
				airticketOrderMoneyAmountQuery.setAirticketOrderId(orderid);
			}
			airticketOrderMoneyAmountQuery.setMoneyType(input.getParamValue4Object("fundsType", Integer.class));
			airticketOrderMoneyAmountQuery.setUuid(input.getParamValue("uuid"));
			
			list = airticketOrderMoneyAmountService.find(airticketOrderMoneyAmountQuery);
		}catch(Exception e){
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("程序异常,请返回重试.");
		}
		if(list!=null && list.size()>0){
			for(int i=0;i<list.size();i++){
				AirticketOrderMoneyAmount amount = list.get(i);
				amount.setStatus(0); //0 已撤销
			}
			try{
				airticketOrderMoneyAmountService.batchSave(list);
				out.setResponseCode4Success();
				out.setData("保存成功");
				
				//----------更新订单的支付状态 add by majiancheng
				financeService.updateOrderRefundFlag(orderid.longValue());
				//----------更新订单的支付状态
			}catch(Exception e){
				out.setResponseCode4Fail();;
				out.putMsgCode(BaseOut4MT.CODE_0002);
				out.putMsgDescription("程序异常,请返回重试.");
				//out.setResponseType(input.getParamValue("responseType"));
			}
		}
		
		return toAndCacheJSONString(out);
	}
	
	/**
	 order/getAirticketOrderPNCByOrderUuid
	 {
         "orderUuid":"400"
     }
	 * 根据订单查询所有大编号接口
	 * @author WangXK
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getAirticketOrderPNCByOrderUuid")
	public String getAirticketOrderPNCByOrderUuid(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		String orderid = input.getParamValue("orderUuid");
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try{
			if(orderid!=null){
				list = airticketOrderPnrService.queryAirticketOrderPNCByOrderUuid(Integer.valueOf(orderid));
			}
			out.setResponseCode4Success();
			out.setData(list);
		}catch(Exception e){
			out.setResponseCode4Fail();;
			out.setData("保存失败");
		}
		
		return toAndCacheJSONString(out);
	}
	/**
	 order/getAirticketOrderPNCListByOrderUuid
	 {
        "orderUuid":"400"
    }
	 * 根据订单查询所有大编号接口
	 * @author WangXK
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getAirticketOrderPNCListByOrderUuid")
	public String getAirticketOrderPNCListByOrderUuid(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		String orderid = input.getParamValue("orderUuid");
		Map<String,Object> map = new HashMap<String,Object>();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String,Object> typemap = new HashMap<String,Object>();
		Map<String,Object> channelmap = new HashMap<String,Object>();
		AirticketOrder airticketOrder = new AirticketOrder();
		Agentinfo agentinfo = new Agentinfo();
		try{
			if(orderid!=null){
				list = airticketOrderPnrService.queryAirticketOrderPNCByOrderUuid(Integer.valueOf(orderid));
				
				airticketOrder = airTicketOrderService.getAirticketorderById(Long.valueOf(orderid));
				Long agentInfoId = airticketOrder.getAgentinfoId();
	    		if(agentInfoId==null || agentInfoId == -1){
	    			typemap.put("channelTypeCode", "2");
	    			typemap.put("channelTypeName", "非签约渠道");
	    			channelmap.put("channelUuid", "-1");
	    			channelmap.put("channelName", airticketOrder.getNagentName());
	    		}else{
	    			typemap.put("channelTypeCode", "1");
	    			typemap.put("channelTypeName", "签约渠道");
	    			
	    			agentinfo =  agentinfoService.findAgentInfoById(agentInfoId);
        			channelmap.put("channelUuid", agentinfo.getId());
        			channelmap.put("channelName", agentinfo.getAgentName());
	    			
	    		}
			}
			map.put("invoiceOriginals", list);
			map.put("channelType", typemap);
			map.put("channel", channelmap);
			out.setResponseCode4Success();
			out.setData(map);
		}catch(Exception e){
			out.setResponseCode4Fail();;
			out.setData("保存失败");
		}
		
		return toAndCacheJSONString(out);
	}
	/**
	 * PNR(列表)-展开-修改人数并增加修改记录
	 * @author ang.gao
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "modifyPNRGroupAddRecord")
	public String modifyPNRGroupAddRecord(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		String orderId = input.getParamValue("orderUuid");//订单id
		String invoiceOriginalGroupUuid = input.getParamValue("invoiceOriginalGroupUuid");//大编号组Uuid
		String drawerCount = input.getParamValue("drawerCount");//出票人数
		String reserveCount = input.getParamValue("reserveCount");//预定人数
		
		if(StringUtils.isNotBlank(orderId) && StringUtils.isNotBlank(invoiceOriginalGroupUuid) && StringUtils.isNotBlank(drawerCount) && StringUtils.isNotBlank(reserveCount) ){
			//拼接修改日志内容
			AirticketOrderPnrGroup pnrGroup = airticketOrderPnrGroupService.getByUuid(invoiceOriginalGroupUuid);
			Integer dCount = pnrGroup.getTicketPersonNum();//修改前的出票人数
			Integer rCount = pnrGroup.getPersonNum();//修改前的预定人数
			StringBuffer str = new StringBuffer();
			List<AirticketOrderPnr> pnrList = airticketOrderPnrService.findByPNRGroupUuid(invoiceOriginalGroupUuid);
			if(CollectionUtils.isNotEmpty(pnrList)){
				str.append("大编号");
				for(AirticketOrderPnr pnr : pnrList){
					str.append(pnr.getFlightPnr()).append(",");//PNR值
				}
			}
			str.append("总出票人数由").append(dCount).append("修改成").append(drawerCount).append(";");
			str.append("预订人数由").append(rCount).append("修改成").append(reserveCount).append(";");
			try{
				//更新机票订单的总人数
				AirticketOrder airticketOrder = airTicketOrderService.getAirticketorderById(Long.parseLong(orderId));
				if(Integer.parseInt(reserveCount)>rCount){//人数增加
					Integer num = Integer.parseInt(reserveCount) - rCount;//增加的人数
					Integer value = airticketOrder.getPersonNum() + num;
					mtourOrderService.updateAirticketOrderPersonNum(value, orderId);
				}
				if(Integer.parseInt(reserveCount)<rCount){//人数减少
					Integer num = rCount - Integer.parseInt(reserveCount);//减少的人数
					Integer value = airticketOrder.getPersonNum() - num;
					mtourOrderService.updateAirticketOrderPersonNum(value, orderId);
				}
				//更新PNR组表的值
				mtourOrderService.updatePNRTravelerNum(drawerCount, reserveCount, invoiceOriginalGroupUuid);
				//向record表插入一条记录
				AirticketOrderOperateRecord record = new AirticketOrderOperateRecord();
				String _uuid = UUID.randomUUID().toString().replace("-", "");
				record.setUuid(_uuid);
				record.setTargetType(0);//修改PNR组
				record.setTargetUuid(invoiceOriginalGroupUuid);
				record.setContent(str.toString());
				record.setCreateBy(Integer.parseInt(UserUtils.getUser().getId().toString()));
				record.setCreateDate(new Date());
				record.setDelFlag("0");
				airticketOrderOperateRecordService.save(record);
				
				out.setResponseCode4Success();
			}catch(Exception e){
				out.setResponseCode4Fail();
				out.putMsgCode(BaseOut4MT.CODE_0002);
				out.putMsgDescription("程序异常,请返回重试.");
			}
		}else{
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("参数传递错误!");
		}
			
		return toAndCacheJSONString(out);
	}
	
	/**
	 * 收款记录（订单）
	 * @param input
	 * @return
	 * @author wangxv
	 */
	@ResponseBody
	@RequestMapping(value = "getOrderReceiptList")
	public String getOrderReceiptList(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		String orderId = input.getParamValue("orderUuid");//订单Uuid
		if(StringUtils.isNotBlank(orderId)){
			List<Map<String,Object>> list = mtourOrderService.getOrderReceiptList(orderId);
			out.setResponseCode4Success();
			if(CollectionUtils.isNotEmpty(list)){
				for(Map<String,Object> temp : list){
					if(temp.get("docIds")!=null){
						String ids = temp.get("docIds").toString();
						if(StringUtils.isNotBlank(ids)){
							String docIds =ids.substring(0, ids.lastIndexOf(","));
							List<Map<String,String>> docDetail = mtourOrderService.getOrderPayDocList(docIds);
							temp.put("attachments", docDetail);
						}
					}
				}
				out.setData(list);
			}
		}else{
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("参数错误：订单Uuid为空！");
		}
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
	}
	
	/**
	 * 收款详情（订单）
	 * @param input
	 * @return
	 * @author wangxv
	 */
	@ResponseBody
	@RequestMapping(value = "getOrderReceiptDetail")
	public String getOrderReceiptDetail(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		JSONObject jsonObj = JSONObject.parseObject(input.getParam());
		//获得传递的参数值
		Integer receiveId = jsonObj.getInteger("receiveUuid");
		if(receiveId!=null){
			Map<String,Object> result = mtourOrderService.getOrderReceiptDetail(receiveId.toString());
			out.setResponseCode4Success();
			if(result!=null){
				if(result.get("docIds")!=null){
					String ids = result.remove("docIds").toString();
					if(StringUtils.isNotBlank(ids)){
						String docIds =ids.substring(0, ids.lastIndexOf(","));
						List<Map<String,String>> docDetail = mtourOrderService.getOrderPayDocList(docIds);
						result.put("attachments", docDetail);
					}
				}
			}
			out.setData(result);
		}else{
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("传递参数异常");
		}
 		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
	}
	
	/**
	 * 收款撤销
	 * @param input
	 * @return
	 * @author wangxv
	 */
	@ResponseBody
	@RequestMapping(value = "orderReceiptCancel")
	public String orderReceiptCancel(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		JSONObject jsonObj = JSONObject.parseObject(input.getParam());
		//获得传递的参数值
		Integer receiveId = jsonObj.getInteger("receiveUuid");
		Integer orderId = jsonObj.getInteger("orderUuid");
		if(receiveId!=null){
			try {
				mtourOrderService.orderReceiptCancel(receiveId.toString(), orderId);
			} catch (Exception e) {
				e.printStackTrace();
				out.setResponseCode4Error();
				out.putMsgCode(BaseOut4MT.CODE_0002);
				out.putMsgDescription("接口出现异常，请重新操作！");
			}
			out.setResponseCode4Success();
		}else{
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("传递参数异常");
		}
 		return toAndCacheJSONString(out);
	}
	
	/**
	 * 获取订单列表
	 * @author gao
	 * 2015年10月21日
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getOrderList")
	public String getOrderList(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			JSONObject obj = JSONObject.parseObject(input.getParam());
			JSONObject searchParam = obj.getJSONObject("searchParam");
			String searchType = searchParam.getString("searchType");
			String searchKey = searchParam.getString("searchKey");
			
			JSONObject filterParam = obj.getJSONObject("filterParam");
			String channelUuid = filterParam.getString("channelUuid");
			String ordererId = filterParam.getString("ordererId");
			String orderStatusCode = filterParam.getString("orderStatusCode");
			String receiveStatusCode = filterParam.getString("receiveStatusCode");
			
			JSONObject sortInfo = obj.getJSONObject("sortInfo");
			String sortKey = sortInfo.getString("sortKey");
			boolean dec = sortInfo.getObject("dec",Boolean.class);
			JSONObject pageParam = obj.getJSONObject("pageParam");
			String currentIndex = null;
			String rowCount = null;
			if(pageParam!=null){
				currentIndex = pageParam.getString("currentIndex");
				rowCount = pageParam.getString("rowCount");
			}
			
			MtourOrderParam paramvalue = new MtourOrderParam();
			if(StringUtils.isNotBlank(searchType)){
				paramvalue.setSearchType(searchType);
			}
			if(StringUtils.isNotBlank(searchKey)){
				paramvalue.setSearchKey(searchKey);
			}
			if(StringUtils.isNotBlank(channelUuid) && !"".equals(channelUuid)){
				paramvalue.setChannelUuid(channelUuid.split(","));
			}
			if(StringUtils.isNotBlank(ordererId) && !"".equals(ordererId)){
				paramvalue.setOrdererId(ordererId.split(","));
			}
			if(StringUtils.isNotBlank(orderStatusCode) && !"".equals(orderStatusCode)){
				paramvalue.setOrderStatusCode(orderStatusCode.split(","));
			}
			if(StringUtils.isNotBlank(receiveStatusCode) && !"".equals(receiveStatusCode)){
				paramvalue.setReceiveStatusCode(receiveStatusCode.split(","));
			}
			if(StringUtils.isNotBlank(currentIndex)){
				paramvalue.setCurrentIndex(currentIndex);
			}
			if(StringUtils.isNotBlank(rowCount)){
				paramvalue.setRowCount(rowCount);
			}
			if(StringUtils.isNotBlank(sortKey)){
				paramvalue.setSortKey(sortKey);
			}
			//新增下单日期收索条件
			String dateString = filterParam.getString("orderDateTime");
			if(StringUtils.isNotBlank(dateString)){
				paramvalue.setCreateDateStart(dateString.substring(0, dateString.indexOf("~")));
				paramvalue.setCreateDateEnd(dateString.substring(dateString.indexOf("~")+1, dateString.length()));
			}
			paramvalue.setDec(dec);
			MtourOrderJsonBean bean = mtourOrderService.getMtourOrderJsonBean(paramvalue);
			out.setResponseCode4Success();
			out.setData(bean);
		}catch (Exception e) {
			e.printStackTrace();
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("接口处理异常.");
		}
 		return toAndCacheJSONString(out);
	}
	
	/**
	 * 收款-保存(确认)接口（订单付款）
	 * @Title: saveOrderpay
	 * @return String
	 * @author majiancheng
	 * @date 2015-10-22 下午9:15:51
	 */
	@ResponseBody
	@RequestMapping(value = "saveOrderpayInfo", method = RequestMethod.POST)
	public String saveOrderpayInfo(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		JSONObject jsonObj = JSONObject.parseObject(input.getParam());
		if(null != jsonObj.getBoolean("Resubmit") && jsonObj.getBoolean("Resubmit")){
			Map<String, Object> map = mtourOrderService.getOrderReceiptDetail(jsonObj.getString("receiveUuid"));
			if("1".equals(map.get("repeatSubmit").toString())){
				Map<String,Boolean> mapSubmit=new HashMap<String,Boolean>();
				mapSubmit.put("Resubmit", false);
				return JSON.toJSONString(mapSubmit);
			}
			try {
				mtourOrderService.updateOrderPayRepeatSubmit(jsonObj.getLong("receiveUuid"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		try{
			boolean flag = mtourOrderService.saveOrderpayInfo(input);
			if(flag) {
				out.setResponseCode4Success();
			} else {
				out.setResponseCode4Fail();
				out.putMsgDescription("调用收款-保存(确认)接口失败，请重新操作！");
			}
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("调用收款-保存(确认)接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
	}
	
	/**
	 * 其他收款详情
	 * @param input
	 * @return
	 * @author wangxv
	 */
	@ResponseBody
	@RequestMapping(value = "getOtherReceiptDetail")
	public String getOtherReceiptDetail(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		JSONObject jsonObj = JSONObject.parseObject(input.getParam());
		//获得传递的参数值
		Integer costRecordId = jsonObj.getInteger("otherRevenueUuid");
		if(costRecordId!=null){
			Map<String, Object> result = mtourOrderService.getOtherReceiptDetail(costRecordId.toString());
			if(result!=null){
				if(result.get("docIds")!=null){
					String ids = result.remove("docIds").toString();
					if(StringUtils.isNotBlank(ids)){
						String docIds =ids.substring(0, ids.lastIndexOf(","));
						if(StringUtils.isNotBlank(docIds)){
							List<Map<String,String>> docDetail = mtourOrderService.getOrderPayDocList(docIds);
							result.put("attachments", docDetail);
						}
					}
				}
			}
			out.setData(result);
			out.setResponseCode4Success();
		}else{
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("传递参数异常");
		}
 		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
	}
	
	/**
	 * 成本记录和其他收入删除
	 * @param input
	 * @return
	 * @author wangxv
	 */
	@ResponseBody
	@RequestMapping(value = "deleteCostRecord")
	public String deleteCostRecord(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		JSONObject jsonObj = JSONObject.parseObject(input.getParam());
		//获得传递的参数值
		Integer costId = jsonObj.getInteger("costUuid")!=null?jsonObj.getInteger("costUuid"):jsonObj.getInteger("otherRevenueUuid");
		if(costId!=null){
			try {
				mtourOrderService.deleteCostRecord(costId.toString());
				//同时删除订单成本  上面方法以删除，故注释掉 update by shijun.liu
				//deleteOrderCost(costId);
			} catch (Exception e) {
				e.printStackTrace();
				out.setResponseCode4Error();
				out.putMsgCode(BaseOut4MT.CODE_0002);
				out.putMsgDescription("接口出现异常，请重新操作！");
			}
			out.setResponseCode4Success();
		}else{
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("传递参数异常");
		}
 		return toAndCacheJSONString(out);
	}
	
	/**
	 * 同步删除订单成本
	 * @param costId 成本id
	 */
	@Deprecated
	private void deleteOrderCost(Integer costId) {
		CostRecord costRecord = costRecordDao.findOne(Long.parseLong(costId.toString()));
		if(costRecord.getUuid() != null) {
			String uuid = costRecord.getUuid();
			AirticketOrderPnrAirlinePrice airticketOrderPnrAirlinePrice = airticketOrderPnrAirlinePriceService.getByUuid(uuid);
			if(airticketOrderPnrAirlinePrice != null) {
				airticketOrderPnrAirlinePrice.setDelFlag("1");
				airticketOrderPnrAirlinePriceService.update(airticketOrderPnrAirlinePrice);
			}
		}
	}
	
	/**
	 * 查询订单的未收金额和外保总额
	 * @param input
	 * @return
	 * @author wangxv
	 */
	@ResponseBody
	@RequestMapping(value = "getOrderPriceDetail")
	public String getOrderPriceDetail(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		//获得传递的参数值
		String orderId = input.getParamValue("orderUuid");
		if(StringUtils.isNotBlank(orderId)){
			Map<String, Object> result = mtourOrderService.getOrderPriceDetail(orderId);
			if(result!=null){
				String totalAmounts = (String)result.remove("totalAmounts");
				String payedAmounts = (String)result.remove("payedAmounts");
				List<Object[]>  totalAmountList = moneyAmountService.getMoneyAmonut(totalAmounts);
				List<MoneyAmount> unreceiveAmountList = null;
				if(StringUtils.isNotBlank(payedAmounts)){
					unreceiveAmountList = moneyAmountService.minusMoney(totalAmounts, payedAmounts,"decrease");
				}
				List<Map<String,Object>> totalAmountResult = new ArrayList<Map<String,Object>>();
				if(CollectionUtils.isNotEmpty(totalAmountList)){
					for(Object[] obj :totalAmountList){
						Map<String,Object> totalSalePrice = new HashMap<String,Object>();
						totalSalePrice.put("currencyUuid", obj[0]);
						totalSalePrice.put("amount", obj[3]);
						totalSalePrice.put("exchangerate", obj[4]);
						totalAmountResult.add(totalSalePrice);
					}
					result.put("totalSalePrice", totalAmountResult);
				}
				if(CollectionUtils.isNotEmpty(unreceiveAmountList)&&unreceiveAmountList.size()>1){
					List<Map<String,Object>> unreceiveAmountResult = new ArrayList<Map<String,Object>>();
					for(MoneyAmount obj :unreceiveAmountList){
						Map<String,Object> unreceiveAmount = new HashMap<String,Object>();
						if(obj.getAmount().compareTo(BigDecimal.ZERO)!=0){
							unreceiveAmount.put("currencyUuid", obj.getCurrencyId());
							unreceiveAmount.put("amount", obj.getAmount());
							unreceiveAmount.put("exchangerate", obj.getExchangerate());
							unreceiveAmountResult.add(unreceiveAmount);
						}
					}
					result.put("unreceiveAmount", unreceiveAmountResult);
					//添加集合非空校验 add by majiancheng (2015-12-10)
				}else if(CollectionUtils.isNotEmpty(unreceiveAmountList) && unreceiveAmountList.size()==1){
					List<Map<String,Object>> unreceiveAmountResult = new ArrayList<Map<String,Object>>();
					Map<String,Object> unreceiveAmount = new HashMap<String,Object>();
					MoneyAmount obj = unreceiveAmountList.get(0);
					unreceiveAmount.put("currencyUuid", obj.getCurrencyId());
					unreceiveAmount.put("amount", obj.getAmount());
					unreceiveAmount.put("exchangerate", obj.getExchangerate());
					unreceiveAmountResult.add(unreceiveAmount);
					result.put("unreceiveAmount", unreceiveAmountResult);
				}else{
					result.put("unreceiveAmount", totalAmountResult);
				}
			}
			out.setData(result);
			out.setResponseCode4Success();
		}else{
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0001);
			out.putMsgDescription("传递参数异常");
		}
 		return toAndCacheJSONString(out);
	}
	
	/**
	 * 订单取消验证
	 * 描述:
1:草稿状态:提示确认后,直接取消
2:订单还没有提交过借款、退款、追加成本、成本录入、订单收款、其他收入的（注意，保存操作不算提交），点击取消订单后，弹出取消确认弹窗，点击“是”后订单状态变更为“已取消”
3:单提交过借款、退款、追加成本、成本录入、订单收款、其他收入的（注意，保存操作不算提交），但还可撤销的（提交借款、退款、追加成本、成本录入但财务未付款，就可撤销；提交了订单收款、其他收入，但财务未确认收款，就可撤销），点击取消订单后，弹出确认弹窗，点击“是”后，已提交的借款、退款、追加成本、成本录入、订单收款、其他收入都自动撤销，且订单状态变更为“已取消”
4:订单提交过借款、退款、追加成本、成本录入、订单收款、其他收入的（注意，保存操作不算提交），但不可撤销的（提交借款、退款、追加成本、成本录入且财务已付款，就不可撤销；提交了订单收款、其他收入，但财务已确认收款，就不可撤销），点击取消订单后，弹出提示窗
		 * @Title: cancelOrderValidate
	     * @return String
	     * @author majiancheng       
	     * @date 2015-11-1 下午2:24:02
	 */
	@ResponseBody
	@RequestMapping(value = "cancelOrderValidate")
	public String cancelOrderValidate(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		String orderUuid = input.getParamValue("orderUuid");
		try{
			if(StringUtils.isNotEmpty(orderUuid)) {
				Map<String, String> datas = mtourOrderService.cancelOrderValidate(orderUuid);
				out.setResponseCode4Success();
				out.setData(datas);
			} else {
				out.setResponseCode4Fail();
				out.putMsgCode(BaseOut4MT.CODE_0001);
				out.putMsgDescription("传递参数异常");
			}
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("订单取消验证接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
	}

	/**
	 * 订单取消
	 * 描述:取消验证后,真正的取消订单
		 * @Title: cancelOrder
	     * @return String
	     * @author majiancheng
	     * @date 2015-11-1 下午4:58:20
	 */
	@ResponseBody
	@RequestMapping(value = "cancelOrder")
	public String cancelOrder(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		String orderUuid = input.getParamValue("orderUuid");
		try{
			if(StringUtils.isNotEmpty(orderUuid)) {
				boolean flag = mtourOrderService.cancelOrder(orderUuid);
				if(flag) {
					out.setResponseCode4Success();
				} else {
					out.setResponseCode4Fail();
					out.putMsgCode(BaseOut4MT.CODE_0002);
					out.putMsgDescription("订单取消接口出现异常，请重新操作！");
				}
			} else {
				out.setResponseCode4Fail();
				out.putMsgCode(BaseOut4MT.CODE_0001);
				out.putMsgDescription("传递参数异常");
			}
		} catch(Exception e) {
			e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("订单取消接口出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
	}
	
	
	/**
	 * 其他收入、订单收款单独查询货币
	 * @author hhx
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getExistsCurrency")
	public String getExistsCurrency(BaseInput4MT input) {
		BaseOut4MT out = new BaseOut4MT(input);
		try{
			JSONObject jsonObj = JSONObject.parseObject(input.getParam());
			String orderId = jsonObj.getString("orderId");
			String type = jsonObj.getString("type");
			if(StringUtils.isNotBlank(orderId) && StringUtils.isNotBlank(type)){
				List<Map<String,String>> list = null;
				if(UserUtils.isMtourUser()){
					//美途国际取基础信息表的汇率
					list = mtourCommonDao.getCurrencyList();
				}else{
					list = mtourOrderService.getExistsCurrency(Long.parseLong(orderId), type);
				}
				out.setData(list);
				out.setResponseCode4Success();
			}else{
				out.setResponseCode4Fail();
				out.putMsgCode(BaseOut4MT.CODE_0001);
				out.putMsgDescription("传递参数出错.");
			}
		}catch (Exception e) {
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("程序异常,请返回重试.");
		}
 		//return toAndCacheJSONString(out,DateUtils.DATE_PATTERN_YYYY_MM_DD);
		return JSON.toJSONString(out);
	}
	/**
	 * 根据币种状态，获取币种列表
	 * @author zhangchao
	 */
	@ResponseBody
	@RequestMapping(value="queryPayOrder")
	public String queryPayOrder(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		QueryOrderJsonBean payOrder = mtourOrderService.queryPayOrder(input.getParamValue("orderUuid"));
		out.setData(payOrder);
		out.setResponseCode4Success();
//		System.out.println(toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD));
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
	}
	
	/**
	 * 待确认订单生成正常订单
	 * @author wangyang
	 * @date 2016.6.15
	 * */
	@ResponseBody
	@RequestMapping(value = "confirmOrder")
	public String confirmOrder(BaseInput4MT input){
		
		BaseOut4MT out = new BaseOut4MT(input);
		
		try{
			String orderUuid = input.getParamValue("orderUuid");
			if(mtourOrderService.confirmOrder(orderUuid)){
				out.setResponseCode4Success();
			}else{
				out.setResponseCode4Fail();
				out.putMsgCode(BaseOut4MT.CODE_0001);
				out.putMsgDescription("订单确认失败.");
			}
		}catch (Exception e) {
			out.setResponseCode4Fail();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("程序异常,请返回重试.");
		}
		
		return JSON.toJSONString(out);		
	}
	
	/**
	 * 获取非签约渠道
	 * @param request
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@RequestMapping(value="getNoAgent")
	public String getNoAgent(HttpServletRequest request,HttpServletResponse response,Model model){
		Page<Map<String,Object>> page = mtourOrderService.getNoAgent(new Page<Map<String,Object>>(request, response), request);
		model.addAttribute("page",page);
		model.addAttribute("salerId",request.getParameter("salerId"));
		model.addAttribute("nagentName",request.getParameter("nagentName"));
		model.addAttribute("minCreateDate", request.getParameter("minCreateDate"));
		model.addAttribute("maxCreateDate", request.getParameter("maxCreateDate"));
		model.addAttribute("agentSalers", agentinfoService.findInnerSales(UserUtils.getUser().getCompany().getId()));
		return "modules/mtour/noAgent";
	}

	/**
	 * 批量生成收入单查询信息
	 * @param input
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="batchQueryReceiveOrder")
	public String batchQueryReceiveOrder(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("applicantId", UserUtils.getUser().getId());
		map.put("applicantName", UserUtils.getUser().getName());
		JSONObject jobj = JSONObject.parseObject(input.getParam());
		if(jobj.getJSONArray("orderUuid")!=null && jobj.getJSONArray("orderUuid").size()!=0){
			 JSONArray array = jobj.getJSONArray("orderUuid");
			 String orderUuids="";
			 for(int i=0;i<array.size();i++){
				 if(StringUtils.isNotEmpty(orderUuids)){
					 orderUuids=orderUuids+","+array.getString(i);
				 }else{
					 orderUuids=array.getString(i);
				 }
			 }
			 //获得团号
			 List<Map<String,Object>> expenditureList = mtourOrderService.getGroupCode(orderUuids);
			 for(Map<String,Object> mapExpend:expenditureList){
				 List<String> payObj=new ArrayList<String>();
				 //获得收款对象
				 List<Map<String, Object>> payMap = mtourOrderService.getPayObj(Integer.parseInt(mapExpend.get("id").toString()));
//				 for(Map<String,Object> objs:payMap){
//					 payObj.add(objs.get("agentName").toString());
//				 }
				 mapExpend.put("payObj", payMap);
				 //获得具体信息
				 List<Map<String,Object>> paymentObject = mtourOrderService.getPaymentObject(Integer.parseInt(mapExpend.get("id").toString()));
				 mapExpend.put("paymentObject", paymentObject);
			 }
			 map.put("expenditureList", expenditureList);
			 out.setData(map);
			 out.setResponseCode4Success();
		}
		return toAndCacheJSONString(out, DateUtils.DATE_PATTERN_YYYY_MM_DD);
	}

	/**
	 * 批量查询订单的支出单信息。 yudong.xu 2016.7.6
     */
	@ResponseBody
	@RequestMapping(value="batchQueryPayOrder")
	public String batchQueryPayOrder(BaseInput4MT input){
		BaseOut4MT out = new BaseOut4MT(input);
		JSONArray idArray = JSONArray.parseArray(input.getParamValue("orderUuid"));
		try {
			if (idArray != null && idArray.size() != 0){
				StringBuilder ids = new StringBuilder();
				int idx = idArray.size()-1;
				for (int i = 0; i < idx ; i++) {
					ids.append(idArray.getString(i)).append(",");
				}
				ids.append(idArray.getString(idx));
				BatchOrderPaymentJsonBean data = mtourOrderService.batchQueryPayOrder(ids.toString());
				out.setData(data);
				out.setResponseCode4Success();
			}else {
				out.setResponseCode4Fail();
				out.putMsgCode(BaseOut4MT.CODE_0001);
				out.putMsgDescription("传递参数异常");
			}
		}catch (Exception e){
			e.printStackTrace();
			out.setResponseCode4Error();
			out.putMsgCode(BaseOut4MT.CODE_0002);
			out.putMsgDescription("查询支出单出现异常，请重新操作！");
		}
		return toAndCacheJSONString(out);
	}
}
