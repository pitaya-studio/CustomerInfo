/**
 * 
 */
package com.trekiz.admin.modules.order.airticket.web;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.json.JSONException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.AreaUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.common.utils.excel.ExportExcel;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.entity.FlightInfo;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderListService;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.mtourCommon.utils.ThreadVariable;
import com.trekiz.admin.modules.order.airticket.service.IAirticketPreOrderService;
import com.trekiz.admin.modules.order.entity.ActivityInfo;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.order.util.OrderUtil;
import com.trekiz.admin.modules.stock.entity.AirticketActivityReserve;
import com.trekiz.admin.modules.supplier.entity.SupplierContacts;
import com.trekiz.admin.modules.supplier.entity.SupplierContactsView;
import com.trekiz.admin.modules.supplier.service.SupplierContactsService;
import com.trekiz.admin.modules.sys.entity.AirlineInfo;
import com.trekiz.admin.modules.sys.entity.AirportInfo;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.AirlineInfoService;
import com.trekiz.admin.modules.sys.service.AirportService;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;

/**
 * @author lixinzhao 机票预订控制器
 * 
 */
@Controller
@RequestMapping(value = "${adminPath}/order/airticket")
public class AirticketOrderController extends BaseController {

	@Autowired
	private IActivityAirTicketService activityAirTicketService;
	@Autowired
    private SupplierContactsService supplierContactsService;
	@Autowired
	private AirportService airportService;
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private SysIncreaseService sysIncreaseService;
	@Autowired
	private IAirticketPreOrderService airticketPreOrderService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private AreaService areaService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private AirlineInfoService airlineInfoService;
	@Autowired
    private SystemService systemService;
	@Autowired
	private IAirTicketOrderListService airTicketOrderListService;
	
	@RequestMapping(value = "activityList")
	public String getAirticketActivityList(
			@ModelAttribute ActivityAirTicket airTicket, Model model,
			HttpServletRequest request, HttpServletResponse response) throws ParseException {
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		//查询条件
        Map<String,String> mapRequest = Maps.newHashMap();
		//参数处理：起始同行价格、结束同行价格、产品种类、舱型、是否有余位、是否有切位
        String paras = "orderBy,haveYw,haveQw,groupCodeOrActSer";
        OrderCommonUtil.handlePara(paras, mapRequest, model, request);
        
		String ticketAreaType = request.getParameter("ticket_area_type");
		String airType = request.getParameter("airType");
		String departureCity = request.getParameter("departureCity");
		String startingDate = request.getParameter("startingDate");
		String returnDate = request.getParameter("returnDate");
		String trafficName = request.getParameter("airlines");
		String reserveNumber = request.getParameter("reserveNumber");
		String arrivedCity = request.getParameter("arrivedCity");//到达城市
		String spaceGrade = request.getParameter("spaceGrade");
		String activityCreate = request.getParameter("activityCreate");
		
		if (StringUtils.isNotEmpty(airType)) {
			airTicket.setAirType(airType);//类型 单程、往返、多段
		}
		if (StringUtils.isNotEmpty(ticketAreaType)) {
			airTicket.setTicket_area_type(ticketAreaType);//航段类型
		}
		if (StringUtils.isNotEmpty(departureCity)) {
			airTicket.setDepartureCity(departureCity);//出发城市
		}
		if (StringUtils.isNotEmpty(arrivedCity)) {
			airTicket.setArrivedCity(arrivedCity);//到达城市
		}
		if (StringUtils.isNotEmpty(reserveNumber)) {
			airTicket.setReserveNumber(reserveNumber);//预收人数
		}
		if (StringUtils.isNotEmpty(trafficName)) {
			airTicket.setAirlines(trafficName);//航空公司
		}
		if (StringUtils.isNotEmpty(startingDate)) {//出票日期
			airTicket.setStartingDate(DateUtils.dateFormat(startingDate, "yyyy-MM-dd"));
		}
		if (StringUtils.isNotEmpty(returnDate)) {//返回日期
			airTicket.setReturnDate(DateUtils.dateFormat(returnDate, "yyyy-MM-dd"));
		}
		if (StringUtils.isNotEmpty(spaceGrade)) {//舱位等级
			airTicket.setSpaceGrade(Long.valueOf(spaceGrade));
		}
		if (StringUtils.isNotEmpty(activityCreate) && !("-99999").equals(activityCreate)) {//操作人、计调、产品发布人
			User creator =UserUtils.getUser(Long.valueOf(activityCreate));
			if (creator == null) {
				creator = new User(Long.valueOf(activityCreate));
			}
			airTicket.setCreateBy(creator);
		}
//		airTicket.setCreateBy(user);
		
		Page<ActivityAirTicket> page = activityAirTicketService.findActivityAirTicketPageByOrder(new Page<ActivityAirTicket>(request, response), airTicket, user, mapRequest);
		
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//获取当前时间
		String nowDate = df.format(new Date());
		if(page!=null){
			for (int i = 0; i <page.getList().size(); i++) {
				
				if(page!=null && page.getList()!=null && page.getList().get(i).getFlightInfos()!=null && page.getList().get(i).getFlightInfos().size()>0 && page.getList().get(i).getFlightInfos().get(0).getStartTime() != null){
					//飞机起飞时间
					String startTime = page.getList().get(i).getFlightInfos().get(0).getStartTime().toString();
					//出票时间
					Date dt1 = df.parse(nowDate);
					Date dt2 = df.parse(startTime);
					if(dt2.getTime()<dt1.getTime()){
						//补单
						page.getList().get(i).setReservationStutas("0");
					}else{
						//正常状态
						page.getList().get(i).setReservationStutas("1");
					}
				}
			}
		}
		model.addAttribute("userList", systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId()));
		model.addAttribute("spaceGradelist", DictUtils.getDictList("spaceGrade_Type"));// 舱位等级
//		model.addAttribute("trafficNames",DictUtils.findUserDict(companyId, "flight"));// 航空公司
//		model.addAttribute("fromAreass", DictUtils.getDictList("from_area"));// 出发城市
//		model.addAttribute("arrivedareas", areaService.findByCityList());// 到达城市
		model.addAttribute("from_Areas",areaService.findFromCityList(""));
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
		model.addAttribute("page", page);
		model.addAttribute("traffic_namelist", activityAirTicketService.findAirlineByComid(companyId));// 航空公司
//		model.addAttribute("traffic_namelist",DictUtils.getDictList("traffic_name"));// 航空公司
		model.addAttribute("airports", airportService.queryAirportInfos());// 机场信息
		model.addAttribute("currency",currencyService.findCurrencyList(companyId));// 币种信息
		//form 查询条件
		model.addAttribute("ticket_area_type", DictUtils.getDictList("ticket_area_type"));
		model.addAttribute("air_Type", DictUtils.getDictList("air_Type"));
//		model.addAttribute("ticket_area_type", DictUtils.getValueAndLabelMap("ticket_area_type",companyId));
//		model.addAttribute("air_Type", DictUtils.getValueAndLabelMap("air_Type",companyId));
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		// 获取产品明细显示详情设置
		Long queryAirticketOrderList = UserUtils.getUser().getCompany().getQueryAirticketOrderList(); //是否查看明细
		model.addAttribute("queryAirticketOrderList",queryAirticketOrderList);
		model.addAttribute("activityCreate", activityCreate);
		
		if(UserUtils.getUser().getCompany().getQueryAirticketFields()!=null){
			String[] queryAirticketFields = UserUtils.getUser().getCompany().getQueryAirticketFields().split(",");//明细详情
			if(queryAirticketFields!=null && queryAirticketFields.length>0){
				for(String str : queryAirticketFields){
					if(str.equals("1")){// 订单号码
						model.addAttribute("orderNo",str);
					}else if(str.equals("2")){// 渠道名称
						model.addAttribute("agentName",str);
					}else if(str.equals("3")){// 销售人
						model.addAttribute("shell",str);
					}else if(str.equals("4")){ // 下单人
						model.addAttribute("orderUser",str);
					}else if(str.equals("5")){// 预订时间
						model.addAttribute("reserveDate",str);
					}else if(str.equals("6")){// 订单人数
						model.addAttribute("personNum",str);
					}else if(str.equals("7")){// 订单状态
						model.addAttribute("orderStatus",str);
					}else if(str.equals("8")){// 订单总额
						model.addAttribute("totalAmount",str);
					}else if(str.equals("9")){//已付金额&达帐金额
						model.addAttribute("payedAmount",str);
					}else if(str.equals("10")){//参团类型名称
						model.addAttribute("typeName",str);
					}else if(str.equals("11")){//机票类型名称
						model.addAttribute("airTypeName",str);
					}
				}
			}
		}

//		Office office = officeService.get(companyId);
//		if(office != null) {
//			model.addAttribute("isSeizedConfirmation", office.getIsSeizedConfirmation());
//		}
		
		// 575 wangyang 2017.1.4
		boolean isSale = SecurityUtils.getSubject().isPermitted("airticketOrderForSale:operation:customerConfirm");
		boolean isOp = SecurityUtils.getSubject().isPermitted("airticketOrderForOp:operation:customerConfirm");
		model.addAttribute("hasPermission", isSale || isOp);
		
		return "modules/order/airticket/airticketActivityList";
	}

	/**
	 * 创建机票订单
	 * 
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping(value = "createOrder")
	public String createOrder(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		String activityId = request.getParameter("activityId");
		String payType = request.getParameter("payType");//付款方式  1-全款 2-定金占位 3-预占位',
		String agentId = request.getParameter("agentId");//渠道
		String placeHolderType = request.getParameter("placeHolderType");//1表示切位订单，0表示占位订单
		String salerId = request.getParameter("salerId");//1表示切位订单，0表示占位订单
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		
		Integer freePosion=0;//剩余位置
		ActivityAirTicket activity = activityAirTicketService.getActivityAirTicketById(Long.valueOf(activityId)); 
		
		if (String.valueOf(AirticketOrder.PLACEHOLDERTYPE_QW).equals(placeHolderType)&&StringUtils.isNotEmpty(agentId)) {//切位
			AirticketActivityReserve activityReserve = airticketPreOrderService.queryAirticketActivityReserve(Long.valueOf(activityId),Long.valueOf(agentId));
			if (null != activityReserve) {
				//model.addAttribute("leftpayReservePosition",activityReserve.getLeftpayReservePosition());//剩余切位人数
				freePosion=activityReserve.getLeftpayReservePosition();//剩余切位人数
			}
		}else if (String.valueOf(AirticketOrder.PLACEHOLDERTYPE_ZW).equals(placeHolderType)){//占位
			freePosion=activity.getFreePosition();
		}
		Agentinfo agent = new Agentinfo();
		if(StringUtils.isNotEmpty(agentId)&&!("undefined").equals(agentId)){
			agent=agentinfoService.findOne(Long.valueOf(agentId));
		}
		model.addAttribute("agent", agent);
		Integer payPriceType = Context.MONEY_TYPE_DJ;
		if ("1".equals(payType)) {//全款
			payPriceType = Context.MONEY_TYPE_QK;
		} else if("3".equals(payType)) {//定金
			payPriceType = Context.MONEY_TYPE_DJ;
		} else if("2".equals(payType)) {//预占位
			payPriceType = Context.MONEY_TYPE_DJ;
		} 
		//select sum(child_num) orderPersonNumChild, sum(special_num) orderPersonNumSpecial  from airticket_order where airticket_id =1 and del_flag = 0
		Map<String,Object> counts = activityAirTicketService.getLastCount(activity.getId());
		model.addAttribute("companyId", companyId);
		model.addAttribute("freePosion",freePosion);//剩余位置数
		model.addAttribute("payMode", payType);
		model.addAttribute("activity", activity);
		model.addAttribute("counts", counts);
		model.addAttribute("spaceGradelist",DictUtils.getDictList("spaceGrade_Type"));// 舱位等级
		model.addAttribute("traffic_namelist", activityAirTicketService.findAirlineByComid(companyId));// 航空公司
//		model.addAttribute("traffic_namelist",DictUtils.getDictList("traffic_name"));// 航空公司
		model.addAttribute("fromAreas",areaService.findFromCityList(""));//出发城市
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
		model.addAttribute("airportMap", getAirportInfoMap(activity));// 机场信息Map
		model.addAttribute("countrys", CountryUtils.getCountryMap());
		model.addAttribute("areas", areaService.findAirportCityList(""));
	
		model.addAttribute("payPriceType", payPriceType);
		model.addAttribute("placeHolderType", placeHolderType);
		model.addAttribute("salerId", salerId);
		
		//多币种生成json串 前台计算用
		List<Currency> currencyList = currencyService.findCurrencyList(companyId);
		List<Map<String,String>> listMap=new ArrayList<Map<String,String>>();
		Map<String,String> currMap=null;
		for(Currency curr:currencyList){
			currMap=new HashMap<String,String>();
			currMap.put("currencyId", curr.getId().toString());
			currMap.put("currencyName", curr.getCurrencyName());
			currMap.put("currencyMark", curr.getCurrencyMark());
			currMap.put("je", "0");
			listMap.add(currMap);
		}
		model.addAttribute("bzList", listMap);
		JSONArray bzJson = JSONArray.fromObject(listMap);
		model.addAttribute("bzJson",bzJson);// 所有币种信息
		
		Currency curr=getCurrencyMark(currencyList, activity);
		currMap=new HashMap<String,String>();
		if(curr == null){ //没有对应的币种信息 add by chy 2015年1月7日14:52:03 
			model.addAttribute("currencyMark","");// 币种信息
			currMap.put("currencyId", "");
			currMap.put("currencyName", "");
			currMap.put("currencyMark", "");
			currMap.put("je", "0");
		}else{
			model.addAttribute("currencyMark",curr.getCurrencyMark());// 币种信息
			currMap.put("currencyId", curr.getId().toString());
			currMap.put("currencyName", curr.getCurrencyName());
			currMap.put("currencyMark", curr.getCurrencyMark());
			currMap.put("je", "0");
		}
		JSONObject airticketbz = JSONObject.fromObject(currMap);  
		model.addAttribute("airticketbz",airticketbz);// 机票产品币种信息
		model.addAttribute("orderType","7");
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		
		//start by yang.jiang 2016-1-17 15:26:01
		//订单是否允许添加多个渠道联系人信息
		Integer allowAddAgentInfo = UserUtils.getUser().getCompany().getIsAllowAddAgentInfo();
		model.addAttribute("allowAddAgentInfo", allowAddAgentInfo);
		//订单是否允许渠道联系人信息输入和修改
		Integer allowModifyAgentInfo = UserUtils.getUser().getCompany().getIsAllowModifyAgentInfo();
		model.addAttribute("allowModifyAgentInfo", allowModifyAgentInfo);
		//渠道商的联系地址
		List<SupplierContacts> contacts = supplierContactsService.findAllContactsByAgentInfo(Long.parseLong(agentId));  //取出渠道商所有联系人（包括第一联系人）
		String address = agentinfoService.getAddressStrById(Long.parseLong(agentId));
		model.addAttribute("address", address == null ? "" : address);
		model.addAttribute("zipCode", agent.getAgentPostcode() == null ? "" : agent.getAgentPostcode());
		//渠道商设置地址全中文字符
		for (SupplierContacts supplierContacts : contacts) {
			supplierContacts.setAgentAddressFull(address);
		}
		//转换成view实体
		List<SupplierContactsView> contactsView = Lists.newArrayList();
		for (SupplierContacts supplierContacts : contacts) {			
			SupplierContactsView splContactsView = new SupplierContactsView();
			BeanUtils.copyProperties(supplierContacts, splContactsView);
			contactsView.add(splContactsView);
		}
		model.addAttribute("contacts", contacts);
		model.addAttribute("contactsView", contactsView);
		String contactsJsonStr = supplierContactsService.contacts2Json(contacts);
		org.json.JSONArray contactArray = supplierContactsService.contacts2JsonArray(contacts);
		org.json.JSONArray contactArrayView = supplierContactsService.contacts2JsonArray4View(contactsView);
		model.addAttribute("contactsJsonStr", contactsJsonStr);
		model.addAttribute("contactArray", contactArray);
		model.addAttribute("contactArrayView", contactArrayView);
		
		//end
		
		return "modules/order/airticket/airticketOrder";
	}

	//获取当前币种
	private Currency getCurrencyMark(List<Currency> currencyList, ActivityAirTicket activity) {
		Currency tempcurrency=null;
		Long currencyId = activity.getCurrency_id();
		if (null == currencyId) {
			tempcurrency=currencyList.size()>0?currencyList.get(0):null;
		}
		for (Currency currency : currencyList) {
			if (currencyId.equals(currency.getId())) {
				tempcurrency= currency;
				break;
			}
		}
		return tempcurrency;
	}

	@ResponseBody
	@RequestMapping(value = "saveAirticketOrder")
	public Map<String, Object> saveAirticketOrder(HttpServletRequest request,
			HttpServletResponse response) throws JSONException {
		
		String activityId = request.getParameter("activityId");
		String payMode = request.getParameter("payMode");
		String orderId = request.getParameter("orderid");
		String orderPersonNumChild = request.getParameter("orderPersonNumChild");
		String orderPersonNumAdult = request.getParameter("orderPersonNumAdult");
		String orderPersonNumSpecial = request.getParameter("orderPersonNumSpecial");
		//String orderPersionNum = request.getParameter("orderPersionNum");
		String agentId = request.getParameter("agentId");
		String totalPrice = request.getParameter("totalPrice"); //成本价
		String placeHolderType = request.getParameter("placeHolderType");
		String specialRemark = request.getParameter("specialRemark");
		String nagentName = request.getParameter("nagentName");
		String orderContactsJSON = request.getParameter("orderContactsJSON");//渠道
		String orderTravelerJSON = request.getParameter("orderTravelerJSON");//游客
		String amountJSON = request.getParameter("amountJSON");//多币种应收
		//个人返佣 add by jiangyang 2015年8月3日
		String rebatesJSON = request.getParameter("rebatesJSON");
		JSONArray rebatesObjects = JSONArray.fromObject(rebatesJSON);		
		//团队返佣 add by jiangyang 2015-8-3
		String groupRebatesMoney = request.getParameter("groupRebatesMoney");
		if("金额".equals(groupRebatesMoney) || "——".equals(groupRebatesMoney)){
			groupRebatesMoney = null;
		}
		String groupRebatesCurrency = request.getParameter("groupRebatesCurrency");
		String orderType = request.getParameter("orderType");
		String salerId = request.getParameter("salerId");
		
		//结算价
		
		String strResult = "";
		String casttotalPrice = request.getParameter("casttotalPrice");
		char[] strs = casttotalPrice.toCharArray();
		for (char c : strs) {
			if (c >= '0' && c <= '9' || c == '.' || c == '+') {

				strResult += String.valueOf(c);
				
			}

		}
		
		
		List<OrderContacts> contactsList = OrderUtil.getContactsList(orderContactsJSON);
		List<Traveler> travelerList = jsonToTravelerBean(orderTravelerJSON);
		
		//airticketPreOrderService.saveSecondStep(orderId, specialRemark,travelerList);
		AirticketOrder order = new AirticketOrder();
		//如果供应商不允许订单提醒，则新生成的订单都设置为已经查看的订单，否则设置为没查看订单
    	if (UserUtils.getUser().getCompany().getIsNeedAttention() == 0) {
    		order.setSeenFlag(1);
    	} else {
    		order.setSeenFlag(0);
    	}
		
		order.setAirticketId(Long.valueOf(activityId));
		order.setPlaceHolderType(Integer.valueOf(placeHolderType));

		ActivityAirTicket activity = activityAirTicketService.getActivityAirTicketById(Long.valueOf(activityId));
		order.setProductTypeId(Long.valueOf(activity.getProduct_type_id() == null ? 0 : activity.getProduct_type_id()));
		if (StringUtils.isNotEmpty(orderId)) {
			order.setId(Long.valueOf(orderId));
		} else {
			if (activity.getLockStatus() != null && activity.getLockStatus() == 1) {
				order.setSettleLockedIn(1);
			}
    		if ("10".equals(activity.getForcastStatus())) {
    			order.setForecastLockedIn(1);
    		}
			String companyName = officeService.get(activity.getProCompany()).getName();
			String orderNum = sysIncreaseService.updateSysIncrease(companyName
					.length() > 3 ? companyName.substring(0, 3) : companyName,
					activity.getProCompany(), null, Context.ORDER_NUM_TYPE);

//			String GroupNo = sysIncreaseService.updateSysIncrease(companyName
//					.length() > 3 ? companyName.substring(0, 3) : companyName,
//					activity.getProCompany(), null, Context.GROUP_NUM_TYPE);
			order.setOrderNo(orderNum);
			
			//修改团号规则
//			ActivityGroupService activityGroupService = SpringContextHolder.getBean("activityGroupService");
//			airti = activityId
//			String GroupNo = activityGroupService.getGroupNumForTTS(deptId, null);
//			
			//order.setGroupCode(GroupNo);
			order.setType(AirticketOrder.TYPE_DB);// 单团类型
			
			if (StringUtils.isNotBlank(payMode)) {
				if ("1".equals(payMode)) {//订金
					order.setRemaindDays(activity.getRemainDays_deposit());
					order.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_DJWZF));
					order.setOccupyType(2);
				} else if ("2".equals(payMode)) {//预占位
					order.setRemaindDays(activity.getRemainDays_advance());
					order.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_YZW));
					order.setOccupyType(3);
				} else if("3".equals(payMode)) {//全款
					order.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_WZF));
					order.setOccupyType(1);
				} else if ("7".equals(payMode)) {
					order.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_OP));
					order.setOccupyType(7);
				} else if ("8".equals(payMode)) {
					order.setOrderState(Integer.parseInt(Context.ORDER_PAYSTATUS_CW));
					order.setOccupyType(8);
				}
			}
		}
		if((StringUtils.isEmpty(agentId) || agentId.equals("-1"))&&UserUtils.getUser().getCompany().getUuid().equals("7a81a26b77a811e5bc1e000c29cf2586"))
		{
			Long tempAgentId = agentinfoService.saveAgent(contactsList,request.getParameter("nagentName"),salerId);
			if(tempAgentId!=null){
				agentId = ""+tempAgentId;
			}else{
				Map<String,Object> result = new HashMap<String,Object>();
				result.put("errorMsg", "渠道商已存在");
				return result;
			}
		}
		order.setAgentinfoId(Long.valueOf(agentId));

		order.setTotalMoney(casttotalPrice); //总结算价
		order.setCostTotalMoney(totalPrice); //总成本价
		order.setAdultNum(Integer.valueOf(orderPersonNumAdult));
		order.setChildNum(Integer.valueOf(orderPersonNumChild));
		order.setSpecialNum(Integer.valueOf(orderPersonNumSpecial));
		int totalPersion=Integer.valueOf(orderPersonNumAdult)+Integer.valueOf(orderPersonNumChild)+Integer.valueOf(orderPersonNumSpecial);
		order.setPersonNum(totalPersion);
		order.setComments(specialRemark);
		order.setNagentName(nagentName);
		//团队返佣及设置uuid add by jiangyang 2015-8-3
		if(StringUtils.isNotBlank(groupRebatesMoney)){
			String scheduleBackUuid = UuidUtils.generUuid();
			MoneyAmount moneyAmount = new MoneyAmount(scheduleBackUuid, Integer.parseInt(groupRebatesCurrency), new BigDecimal(groupRebatesMoney), Long.valueOf(activityId), Integer.parseInt(payMode), Integer.parseInt(orderType), Context.BUSINESS_TYPE_ORDER, UserUtils.getUser().getId());
			List<MoneyAmount> moneyAmountss = new ArrayList<>();
			moneyAmountss.add(moneyAmount);
			moneyAmountService.saveOrUpdateMoneyAmounts(scheduleBackUuid, moneyAmountss);
			order.setScheduleBackUuid(scheduleBackUuid);
		}
		if (StringUtils.isNotBlank(salerId)) {
			order.setSalerId(Integer.parseInt(salerId));
			order.setSalerName(UserUtils.getUserNameById(Long.parseLong(salerId)));
        } else {
        	order.setSalerId(UserUtils.getUser().getId().intValue());
			order.setSalerName(UserUtils.getUser().getName());
        }
		//多币种应收款
		JSONObject jsonObject =  JSONObject.fromObject(amountJSON);
		String bzids =  jsonObject.getString("bzid");
		String[] bzidArray=bzids.split(",");
		//结算价
		String[] bzjeArray =  strResult.split("[+]");
 		List<MoneyAmount> moneyAmounts=new ArrayList<MoneyAmount>();
 		Currency currency = null;
		MoneyAmount ma=null;
		for(int i=0;i<bzidArray.length;i++){
			ma=new MoneyAmount();
			ma.setCurrencyId("".equals(bzidArray[i])?0:Integer.valueOf(bzidArray[i]));
			if(!"".equals(bzidArray[i])&&Integer.valueOf(bzidArray[i])!=0){
				currency = currencyService.findCurrency(Long.parseLong(bzidArray[i]));
				ma.setExchangerate(currency.getCurrencyExchangerate());
			}
			ma.setAmount(new BigDecimal("".equals(bzjeArray[i])?"0":bzjeArray[i]));
			ma.setMoneyType(Context.MONEY_TYPE_YSH);
			ma.setOrderType(Context.ORDER_TYPE_JP);
			ma.setBusindessType(MoneyAmount.BUSINDESSTYPE_DD);
			moneyAmounts.add(ma);
		}
		
		//订单成本价
		String cbbzids =  jsonObject.getString("bzid");
		String cbbzjes =  jsonObject.getString("bzje");
		String[] cbbzidArray=cbbzids.split(",");
	    String[] cbbzjeArray=cbbzjes.split(",");
		MoneyAmount cbma=null;
		for(int i=0;i<cbbzjeArray.length;i++){
			cbma=new MoneyAmount();
			cbma.setCurrencyId("".equals(cbbzidArray[i])?0:Integer.valueOf(cbbzidArray[i]));
			if(!"".equals(cbbzidArray[i])&&Integer.valueOf(cbbzidArray[i])!=0){
				currency = currencyService.findCurrency(Long.parseLong(cbbzidArray[i]));
				ma.setExchangerate(currency.getCurrencyExchangerate());
			}
			cbma.setAmount(new BigDecimal("".equals(cbbzjeArray[i])?"0":cbbzjeArray[i]));
			cbma.setMoneyType(Context.MONEY_TYPE_CBJ);
			cbma.setOrderType(Context.ORDER_TYPE_JP);
			cbma.setBusindessType(MoneyAmount.BUSINDESSTYPE_DD);
			moneyAmounts.add(cbma);
		}
		
		
		order.setLockStatus(0);
		//设置激活时间为当前时间
		order.setActivationDate(new Date());
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			order = airticketPreOrderService.saveAirticketOrder(activity, order,contactsList, travelerList, moneyAmounts, rebatesObjects, null);
			airticketOrderAddLog(order);
		} catch (Exception e) {
			map.put("result", "error");
			map.put("msg", e.getMessage());
			return map;
		}
		map.put("result", "success");
		map.put("orderId", order.getId());
		map.put("orderNo", order.getOrderNo());
		return map;
	}

	/**
	 * 
	 * 机票产品订单生成记录日志
	 * @param airticketOrder
	 */
	public void airticketOrderAddLog(AirticketOrder airticketOrder){
		String content = "机票产品添加一条订单信息，订单单号为:"+ airticketOrder.getOrderNo();
		logOpeService.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name,
				content, "1", Context.ProductType.PRODUCT_AIR_TICKET, airticketOrder.getId());
	}
	

	private List<Traveler> jsonToTravelerBean(String jsonStr)
			throws JSONException {
		JSONArray jsonArray = JSONArray.fromObject(jsonStr);
		for(int i =0;i < jsonArray.size();i++){
			JSONObject jsonObj = (JSONObject)jsonArray.get(i);
			
			Object dateObj = jsonObj.get("birthDay");
			if(dateObj == null || "".equals(dateObj.toString())){
				jsonObj.remove("birthDay");
			}
			
			dateObj = jsonObj.get("passportValidity");
			if(dateObj == null || "".equals(dateObj.toString())){
				jsonObj.remove("passportValidity");
			}
			
			jsonArray.set(i, jsonObj);
		}
		
		String[] dateFormats = new String[] {"yyyy/MM/dd","yyyy-MM-dd"}; 
		JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(dateFormats));
		@SuppressWarnings({ "unchecked", "deprecation" })
        List<Traveler> result = JSONArray.toList(jsonArray, Traveler.class);
		return result;
	}

	/**
	 * 获取渠道信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "agentList")
	public Map<String, Object> getAgentInfoJson() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Agentinfo> agentInfos = agentinfoService.findAllAgentinfo();// 渠道信息列表
		map.put("agentList", agentInfos);
		return map;
	}

	@ResponseBody
	@RequestMapping(value = "getPosition")
	public Map<String, Object> getLeftPositionJson(
			HttpServletRequest request, HttpServletResponse response) {
		String activityId = request.getParameter("activityId");
		String agentId = request.getParameter("agentId");
		String placeHolderType = request.getParameter("placeHolderType");

		Map<String, Object> map = new HashMap<String, Object>();
		if("1".equals(placeHolderType)){//1切位
			AirticketActivityReserve activityReserve = airticketPreOrderService.queryAirticketActivityReserve(Long.valueOf(activityId),
					Long.valueOf(agentId));
			map.put("freePosition", activityReserve!=null?activityReserve.getLeftpayReservePosition():0);
		}else{//0占位
			ActivityAirTicket activity = activityAirTicketService.getActivityAirTicketById(Long.valueOf(activityId));
			map.put("freePosition", activity.getFreePosition());
		}
		
		return map;
	}
	
	/**
	 * 预定选择渠道：查询此渠道对应切位数
	 * @Description 
	 * @author yakun.bai
	 * @Date 2015-11-3
	 */
	@ResponseBody
	@RequestMapping(value = "getReservePosition")
	public Map<String, Object> getReservePositionJson(HttpServletRequest request, HttpServletResponse response) {
		
		String activityId = request.getParameter("activityId");
		String agentId = request.getParameter("agentId");

		Map<String, Object> map = Maps.newHashMap();
		AirticketActivityReserve activityReserve = airticketPreOrderService.queryAirticketActivityReserve(Long.valueOf(activityId), Long.valueOf(agentId));
		map.put("freePosition", activityReserve != null ? activityReserve.getLeftpayReservePosition() : 0);
		
		return map;
	}

	/**
	 * 获取机场信息
	 * 
	 * @param activity
	 * @return
	 */
	private Map<Long, AirportInfo> getAirportInfoMap(ActivityAirTicket activity) {
		Map<Long, AirportInfo> airportMap = new HashMap<Long, AirportInfo>();
		List<FlightInfo> flightInfos = activity.getFlightInfos();
		if (null == flightInfos || flightInfos.size() == 0) {
			return airportMap;
		}
		List<Long> airportIdList = new ArrayList<Long>();
		for (FlightInfo info : flightInfos) {
			airportIdList.add(Long.valueOf(info.getDestinationAirpost()));
			airportIdList.add(Long.valueOf(info.getLeaveAirport()));
		}
		return this.airportService.queryAirportInfos(airportIdList);
	}

	@ResponseBody
	@RequestMapping(value = "deleteTraveler")
	public void deleteTraveler(HttpServletRequest request,
			HttpServletResponse response) {
		String travelerId = request.getParameter("travelerId");
		airticketPreOrderService.deleteTraveler(Long.valueOf(travelerId));
	}
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping(value = "planeReview")
	public void planeReview(HttpServletRequest request,
			HttpServletResponse response) {
		String travelerId = request.getParameter("travelerId");
		IAirTicketOrderService airTicketOrderService= (IAirTicketOrderService)SpringContextHolder.getBean("airTicketOrderService");
		try {
			airTicketOrderService.planeReview(1L, 156L, 89L);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value={"downloadAllYw"})
	public void downloadAllYw(@ModelAttribute ActivityAirTicket airTicket, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		
		User user = UserUtils.getUser();
		String orderBy = request.getParameter("orderBy");
		String ticketAreaType = request.getParameter("ticket_area_type");
		String airType = request.getParameter("airType");
		String departureCity = request.getParameter("departureCity");
		String startingDate = request.getParameter("startingDate");
		String returnDate = request.getParameter("returnDate");
		String trafficName = request.getParameter("airlines");
		String reserveNumber = request.getParameter("reserveNumber");
		String arrivedCity = request.getParameter("arrivedCity");//到达城市
		String spaceGrade = request.getParameter("spaceGrade");
		String haveYw = request.getParameter("haveYw");
		String haveQw = request.getParameter("haveQw");
		String groupCodeOrActSer = request.getParameter("groupCodeOrActSer");
		
		if (StringUtils.isNotEmpty(airType)) {
			airTicket.setAirType(airType);//类型 单程、往返、多段
		}
		if (StringUtils.isNotEmpty(ticketAreaType)) {
			airTicket.setTicket_area_type(ticketAreaType);//航段类型
		}
		if (StringUtils.isNotEmpty(departureCity)) {
			airTicket.setDepartureCity(departureCity);//出发城市
		}
		
		if (StringUtils.isNotEmpty(arrivedCity)) {
			airTicket.setArrivedCity(arrivedCity);//到达城市
		}
		if (StringUtils.isNotEmpty(reserveNumber)) {
			airTicket.setReserveNumber(reserveNumber);//预收人数
		}
		if (StringUtils.isNotEmpty(trafficName)) {
			airTicket.setAirlines(trafficName);//航空公司
		}
		
		if (StringUtils.isNotEmpty(startingDate)) {//出票日期
			airTicket.setStartingDate(DateUtils.dateFormat(startingDate, "yyyy-MM-dd"));
		}
		
		if (StringUtils.isBlank(startingDate)) {
			airTicket.setStartingDate(new Date());
        } else {
        	String tempDate = startingDate + " 23:59:59";
        	if (new Date().after(DateUtils.dateFormat(tempDate, DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS))) {
        		airTicket.setStartingDate(new Date());
        	}
        }
		
		
		if (StringUtils.isNotEmpty(returnDate)) {//返回日期
			airTicket.setReturnDate(DateUtils.dateFormat(returnDate, "yyyy-MM-dd"));
		}
		
		if (StringUtils.isNotEmpty(spaceGrade)) {//舱位等级
			airTicket.setSpaceGrade(Long.valueOf(spaceGrade));
		}
		if (StringUtils.isEmpty(orderBy)) {
			orderBy = "createDate";
		}
		airTicket.setCreateBy(user);
		
		Page<ActivityAirTicket> pages = new Page<ActivityAirTicket>(request, response);
		pages.setPageSize(10000);
		Page<ActivityAirTicket> page = activityAirTicketService.findActivityAirTicketPageByOrder(pages, airTicket, orderBy, user, haveYw, haveQw, groupCodeOrActSer);
		List<Long> activityIdList = Lists.newArrayList();
		if (page != null && page.getList() != null) {
			for (int i = 0; i < page.getList().size(); i++) {
				activityIdList.add(page.getList().get(i).getId());
			}
		}
		downloadYwCommon(activityIdList, request, response);
	}
	
	/**
	 * 导出团期余位表
	 * @param groupId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="downloadYw",method=RequestMethod.POST)
	public void downloadYw(@RequestParam(value="activityId") String activityId, HttpServletRequest request, HttpServletResponse response) {
		List<Long> activityIdList = Lists.newArrayList();
		if (StringUtils.isNotBlank(activityId)) {
			activityIdList.add(Long.parseLong(activityId));
		} else {
			return;
		}
		downloadYwCommon(activityIdList, request, response);
	}
	
	/**
	 * 导出团期余位表(指定的团期，可以属于不同产品)
	 * @param groupId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="downloadYwChosed",method=RequestMethod.POST)
	public void downloadYwChosed(@RequestParam(value="paramGroupIds") List<String> paramGroupIds, HttpServletRequest request, HttpServletResponse response) {
		List<Long> groupIdList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(paramGroupIds)) {
			for (String groupId : paramGroupIds) {				
				if (StringUtils.isNotBlank(groupId)) {
					groupIdList.add(Long.parseLong(groupId));
				}
			}
		}
		if (CollectionUtils.isNotEmpty(groupIdList)) {			
			downloadYwCommon(groupIdList, request, response);
		}
	}
	/**
	 * 根据机票团号，查询相关订单详情
	 * @author gao
	 * @DATE 20151116
	 * @param ticketGroupID
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="airticketInfo")
	public String airticketInfo(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		String ticketGroupID = request.getParameter("ticketGroupID");
		if(StringUtils.isNotBlank(ticketGroupID)){
			List<ActivityInfo> activityInfoList = airTicketOrderListService.getAirticketInfo(Long.valueOf(ticketGroupID));
			if(!activityInfoList.isEmpty()){
				map.put("res", "success");
				map.put("activityInfoList", activityInfoList);
				String json = JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);
				ThreadVariable.setMtourAjaxResponse(json);
				return json;
			}else{
				map.put("res", "error");
			}
		}
		String json = JSON.toJSONString(map, SerializerFeature.DisableCircularReferenceDetect);
		ThreadVariable.setMtourAjaxResponse(json);
		return json;
	}
	
	private void downloadYwCommon(List<Long> groupIdList, HttpServletRequest request, HttpServletResponse response) {
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		List<Currency> curreycyList = currencyService.findCurrencyList(companyId);
		Map<String, String> currencyMap = Maps.newHashMap();
		for (Currency currency : curreycyList) {
			currencyMap.put(currency.getId().toString(), currency.getCurrencyMark());
		}
		
		List<Object[]> groupYwList = Lists.newArrayList();
		try {
			List<ActivityAirTicket> groupList = Lists.newArrayList();
			if (CollectionUtils.isNotEmpty(groupIdList)) {
				groupList = airticketPreOrderService.findYwByActivityIds(groupIdList);
			} 
			
			if (CollectionUtils.isNotEmpty(groupList)) {
				for (ActivityAirTicket activity : groupList) {
					Object [] obj = new Object[17];
					//团队类型
					obj[0] = "机票";
					//产品编号
					obj[1] = getNotNullValue(activity.getProductCode());
					//团号
					obj[2] = getNotNullValue(activity.getGroupCode());
					//航空公司
					List<FlightInfo> flighInfoList = activity.getFlightInfos();
					String airType = activity.getAirType();// 单程、往返、多段，值分别为3、2、1
					if (CollectionUtils.isNotEmpty(flighInfoList)) {
						if ("3".equals(airType)) {
							FlightInfo flightInfo = flighInfoList.get(0);
							String airlines = flightInfo.getAirlines();
							if (StringUtils.isNotBlank(airlines) && !"-1".equals(airlines)) {
								List<AirlineInfo>  airlineInfoList = airlineInfoService.getByAirlineCode(companyId, 0, flightInfo.getAirlines());
								if (CollectionUtils.isNotEmpty(airlineInfoList)) {
									obj[3] = "单程：" + getNotNullValue(airlineInfoList.get(0).getAirlineName());
								} else {
									obj[3] = "单程：" + "无";
								}
							} else {
								obj[3] = "单程：" + "无";
							}
							obj[4] = "单程：" + getNotNullValue(flightInfo.getFlightNumber());
							obj[5] = "单程：" + getNotNullValue(DateUtils.formatDate(flightInfo.getStartTime(), DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM));
							obj[6] = "单程：" + getNotNullValue(DateUtils.formatDate(flightInfo.getArrivalTime(), DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM));
						} else if ("2".equals(airType)) {
							obj[3] = "";
							obj[4] = "";
							obj[5] = "";
							obj[6] = "";
							for (FlightInfo flightInfo : flighInfoList) {
								if (flightInfo.getNumber() == 1) {
									String airlines = flightInfo.getAirlines();
									if (StringUtils.isNotBlank(airlines) && !"-1".equals(airlines)) {
										List<AirlineInfo>  airlineInfoList = airlineInfoService.getByAirlineCode(companyId, 0, flightInfo.getAirlines());
										if (CollectionUtils.isNotEmpty(airlineInfoList)) {
											obj[3] += "去程：" + getNotNullValue(airlineInfoList.get(0).getAirlineName());
										} else {
											obj[3] += "去程：" + "无";
										}
									} else {
										obj[3] += "去程：" + "无";
									}
									obj[4] += "去程：" + getNotNullValue(flightInfo.getFlightNumber());
									obj[5] += "去程：" + getNotNullValue(DateUtils.formatDate(flightInfo.getStartTime(), DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM));
									obj[6] += "去程：" + getNotNullValue(DateUtils.formatDate(flightInfo.getArrivalTime(), DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM));
								} else {
									String airlines = flightInfo.getAirlines();
									if (StringUtils.isNotBlank(airlines) && !"-1".equals(airlines)) {
										List<AirlineInfo>  airlineInfoList = airlineInfoService.getByAirlineCode(companyId, 0, flightInfo.getAirlines());
										if (CollectionUtils.isNotEmpty(airlineInfoList)) {
											obj[3] += "返程：" + airlineInfoList.get(0).getAirlineName();
										} else {
											obj[3] += "返程：" + "无";
										}
									} else {
										obj[3] += "返程：" + "无";
									}
									obj[4] += "返程：" + getNotNullValue(flightInfo.getFlightNumber());
									obj[5] += "返程：" + getNotNullValue(DateUtils.formatDate(flightInfo.getStartTime(), DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM));
									obj[6] += "返程：" + getNotNullValue(DateUtils.formatDate(flightInfo.getArrivalTime(), DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM));
								}
							}
						} else if ("1".equals(airType)) {
							obj[3] = "";
							obj[4] = "";
							obj[5] = "";
							obj[6] = "";
							int i = 1;
							for (FlightInfo flightInfo : flighInfoList) {
								String airlines = flightInfo.getAirlines();
								if (StringUtils.isNotBlank(airlines) && !"-1".equals(airlines)) {
									List<AirlineInfo>  airlineInfoList = airlineInfoService.getByAirlineCode(companyId, 0, flightInfo.getAirlines());
									if (CollectionUtils.isNotEmpty(airlineInfoList)) {
										obj[3] += "第" + i + "段：" + airlineInfoList.get(0).getAirlineName();
									} else {
										obj[3] += "无";
									}
								} else {
									obj[3] += "无";
								}
								obj[4] += "第" + i + "段：" + getNotNullValue(flightInfo.getFlightNumber());
								obj[5] += "第" + i + "段：" + getNotNullValue(DateUtils.formatDate(flightInfo.getStartTime(), DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM));
								obj[6] += "第" + i + "段：" + getNotNullValue(DateUtils.formatDate(flightInfo.getArrivalTime(), DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM));
								i++;
							}
						}
					} else {
						obj[3] = "无";
						obj[4] = "无";
						obj[5] = "无";
						obj[6] = "无";
					}
					//出发城市
					if (StringUtils.isNotBlank(activity.getDepartureCity())) {
						obj[7] = DictUtils.getDict(activity.getDepartureCity(), "from_area").getLabel();
					} else {
						obj[7] = "无";
					}
					//到达城市
					if (StringUtils.isNotBlank(activity.getArrivedCity())) {
						obj[8] = AreaUtil.findAreaNameById(Long.parseLong(activity.getArrivedCity()));
					} else {
						obj[8] = "无";
					}
					if (activity.getCurrency_id() != null) {
						String currency = activity.getCurrency_id().toString();
						//成人同行价
						if (activity.getSettlementAdultPrice() != null) {
							obj[9] = currencyMap.get(currency) + activity.getSettlementAdultPrice();
						} else {
							obj[9] = "无";
						}
						//儿童同行价
						if (activity.getSettlementcChildPrice() != null) {
							obj[10] = currencyMap.get(currency) + activity.getSettlementcChildPrice();
						} else {
							obj[10] = "无";
						}
						//特殊人群同行价
						if (activity.getSettlementSpecialPrice() != null) {
							obj[11] = currencyMap.get(currency) + activity.getSettlementSpecialPrice();
						} else {
							obj[11] = "无";
						}
					} else {
						obj[9] = "无";
						obj[10] = "无";
						obj[11] = "无";
					}
					//预收
					obj[12] = activity.getReservationsNum();
					//余位
					obj[13] = activity.getFreePosition();
					//占位
					obj[14] = activity.getNopayReservePosition();
					//切位
					obj[15] = activity.getPayReservePosition();
					//售出切位
					obj[16] = activity.getSoldPayPosition();
					groupYwList.add(obj);
				}
			}
			
			//文件名称
			String fileName = "团期余位下载";
			//Excel各行名称
			String[] cellTitle =  {"团队类型","产品编号","团号","航空公司","航班号","出发时间","到达时间","出发城市","到达城市","成人成本价",
					"儿童成本价","特殊人群成本价","预收","余位","占位","切位","售出切位"};
			//文件首行标题
			String firstTitle = "团期余位表信息";
			ExportExcel.createExcle(fileName, groupYwList, cellTitle, firstTitle, request, response);
		} catch (Exception e) {
			logger.error("下载出错");
			e.printStackTrace();
		}
	}
	
	
	private static String getNotNullValue(Object o) {
		if (o != null) {
			if ("java.lang.String".equals(o.getClass().getName())) {
				String temp = o.toString();
				if (StringUtils.isNotBlank(temp)) {
					return temp;
				} else {
					return "无";
				}
			} else if ("java.util.Date".equals(o.getClass().getName()) 
					|| "java.sql.Timestamp".equals(o.getClass().getName())) {
				Date temp = (Date)o;
				return DateUtils.date2String(temp, DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS);
			} else {
				return o.toString();
			}
		} else {
			return "无";
		}
	}
	
}

