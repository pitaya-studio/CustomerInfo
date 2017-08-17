package com.trekiz.admin.modules.airticketorder.web;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.FileUtils;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.common.utils.excel.ExportExcel;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderListService;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Menu;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.service.TravelerService;


/**
 * 机票订单专用
 * @author yakun.bai
 * @Date 2015-9-25
 */
@Controller
@RequestMapping(value = "${adminPath}/airticketOrderList/manage")
public class AirTicketOrderListController {
	
	private static final Logger log = Logger.getLogger(AirTicketOrderListController.class);
	
	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	@Autowired
	private IAirTicketOrderListService airTicketOrderListService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private OrderCommonService orderService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private MoneyAmountService maService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private TravelerService travelerService;
	
	/**
	 * 机票订单查询
	 * @param type 1 销售 2 计调
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
    @RequestMapping(value="airticketOrderList/{queryType}")
	public String airticketOrderList(@PathVariable Integer queryType, Model model, HttpServletRequest request, HttpServletResponse response) {
		
    	//按部门展示
    	DepartmentCommon common = departmentService.setDepartmentPara("airticketOrder", model);
    	
		//参数处理：去除空格和处理特殊字符并传递到后台
		//参数解释：机票订单状态、订单编号或团期编号、机票类型：1-多段 2-往返 3-单程、机票区域类型 1-内陆+国际 2-国际 3-国内、 出发地、联系人、目的地ID、目的地名称
		//		        是否参团、销售、下单人、计调、渠道ID、出发开始日期、出发结束日期、返回开始日期、返回结束日期、发票状态、收据状态、参团订单编号或团号
		Map<String, String> mapRequest = Maps.newHashMap(); //查询条件
        String paras = "showType,orderNumOrOrderGroupCode,airType,ticketType,fromAreaId,contact,targetAreaIdList,targetAreaNameList,joinGroup," +
        		"saler,picker,op,agentId,startAirTime,endAirTime,returnStartAirTime,returnEndAirTime,invoiceStatus,receiptStatus," +
        		"joinGroupCodeOrOrderNum,queryType,jiekuanStatus,confirmOccupy,paymentType";

        OrderCommonUtil.handlePara(paras, mapRequest, model, request);
        
        //订单或团期列表标识：order为订单、group为团期，默认查询订单列表
        String orderOrGroup = request.getParameter("orderOrGroup");
        if (StringUtils.isBlank(orderOrGroup)) {
        	if (queryType == 1) {
        		orderOrGroup = "order";
        	} else {
        		orderOrGroup = "group";
        	}
        }
        mapRequest.put("orderOrGroup", orderOrGroup);
        model.addAttribute("orderOrGroup", orderOrGroup);
        
        //订单调用取消接口（过了保留时间订单需要取消）
        if (1 == UserUtils.getUser().getCompany().getIsCancleOrder()) {
        	orderService.cancelOrderWithRemainDays();
        }
	        
        //排序方式：默认为出团日期降序排列
        String orderBy = request.getParameter("orderBy");
        if (StringUtils.isBlank(orderBy)) {
            orderBy = "ao.id DESC";
        }
        mapRequest.put("orderBy", orderBy);
        
        //订单或团期查询
        Page<Map<Object, Object>> orderListPage = new Page<Map<Object, Object>>(request, response);
		Page<Map<Object, Object>> page = airTicketOrderListService.queryOrder(orderListPage, mapRequest, common);
        model.addAttribute("page", page);
        List<Map<Object, Object>> listorder = page.getList();
        orderBy = page.getOrderBy().replace("aa", "ao").replace("updateDate", "update_date");
        page.setOrderBy(orderBy);
	        
        List<String> groupIdList = Lists.newArrayList();
        List<Long> listProId = Lists.newArrayList();
        Date currDate = new Date();
        for (Map<Object, Object> listin : listorder) {
        	if ("group".equals(orderOrGroup) && listin.get("id") != null) {
        		groupIdList.add(listin.get("id").toString());
        	}
        	if ("order".equals(orderOrGroup) && listin.get("id") != null) {
        		listProId.add(Long.parseLong(listin.get("id").toString()));
        		//金额处理
            	handlePrice(listin);
            	//获取订单剩余时间
            	getOrderLeftTime(listin);
            	//航班
            	handleStartTm(currDate, listin);
        	}
        }
	        
        List<Map<Object, Object>> orderList = null;
        if ("group".equals(orderOrGroup) && CollectionUtils.isNotEmpty(groupIdList)) {
        	orderList = airTicketOrderListService.findByActivityIds(new Page<Map<Object, Object>>(request, response), groupIdList, mapRequest.get("orderSql")).getList();
        	for (Map<Object, Object> listin : orderList) {
        		//金额处理
        		handlePrice(listin);
        		//获取订单剩余时间
            	getOrderLeftTime(listin);
            	//航班
            	handleStartTm(currDate, listin);
        		
            	if (listin.get("id") != null) {
            		listProId.add(Long.parseLong(listin.get("id").toString()));
            	}
	        }
        	listorder = orderList;
        }
        
        //支付订单查询
        selectPayOrder(listProId, listorder, model);
        
        model.addAttribute("queryType", queryType);
		model.addAttribute("operate", UserUtils.getUser().getName());
		model.addAttribute("conditionsMap", mapRequest);
		model.addAttribute("orders",orderList);
		Menu menu = departmentService.getMenuByUrl(request);
        if (menu != null) {
        	request.setAttribute("_m", menu.getParent() != null ? menu.getParent().getId() : null);
        	request.setAttribute("_mc", menu.getId());
        }
        List<User> users = systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId());
	    model.addAttribute("users",users);
	  	initParam(model);
	  	model.addAttribute("companyUuid", UserUtils.getUser().getCompany().getUuid());
	  	model.addAttribute("dayangCompanyUuid", Context.DA_YANG_COMPANYUUID);
		model.addAttribute("qingdaokaisaCompanyUuid", Context.QINGDAO_KAISA_COMPANYUUID);
		//添加是否多返佣对象标识（0：否；1：是）
		model.addAttribute("isAllowMultiRebateObject", UserUtils.getUser().getCompany().getIsAllowMultiRebateObject());
		Long companyId = UserUtils.getUser().getCompany().getId();
		Office office = officeService.get(companyId);
		model.addAttribute("office", office);
		model.addAttribute("isOpManager", UserUtils.isOpManager());
		return "modules/order/airticketList/airticketOrderList";
	}
    
    @RequestMapping(value="exportAirticketUserList/{queryType}")
  	public void exportAirticketUserList(@PathVariable Integer queryType, Model model, HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException {
    	
    	//按部门展示
    	DepartmentCommon common = departmentService.setDepartmentPara("airticketOrder", model);
    	
		//参数处理：去除空格和处理特殊字符并传递到后台
		//参数解释：机票订单状态、订单编号或团期编号、机票类型：1-多段 2-往返 3-单程、机票区域类型 1-内陆+国际 2-国际 3-国内、 出发地、联系人、目的地ID、目的地名称
		//		        是否参团、销售、下单人、计调、渠道ID、出发开始日期、出发结束日期、返回开始日期、返回结束日期、发票状态、收据状态、参团订单编号或团号
		Map<String, String> mapRequest = Maps.newHashMap(); //查询条件
        String paras = "showType,orderNumOrOrderGroupCode,airType,ticketType,fromAreaId,contact,targetAreaIdList,targetAreaNameList,joinGroup," +
        		"saler,picker,op,agentId,startAirTime,endAirTime,returnStartAirTime,returnEndAirTime,invoiceStatus,receiptStatus," +
        		"joinGroupCodeOrOrderNum,queryType,jiekuanStatus,confirmOccupy";

        OrderCommonUtil.handlePara(paras, mapRequest, model, request);
        
        //订单或团期列表标识：order为订单、group为团期，默认查询订单列表
        String orderOrGroup = request.getParameter("orderOrGroup");
        if (StringUtils.isBlank(orderOrGroup)) {
        	if (queryType == 1) {
        		orderOrGroup = "order";
        	} else {
        		orderOrGroup = "group";
        	}
        }
        mapRequest.put("orderOrGroup", orderOrGroup);
        model.addAttribute("orderOrGroup", orderOrGroup);
        
        //订单调用取消接口（过了保留时间订单需要取消）
        if (1 == UserUtils.getUser().getCompany().getIsCancleOrder()) {
        	orderService.cancelOrderWithRemainDays();
        }
	        
        //排序方式：默认为出团日期降序排列
        String orderBy = request.getParameter("orderBy");
        if (StringUtils.isBlank(orderBy)) {
            orderBy = "ao.id DESC";
        }
        mapRequest.put("orderBy", orderBy);
        
        //订单或团期查询
        Page<Map<Object, Object>> orderListPage = new Page<Map<Object, Object>>(request, response,-1);
		Page<Map<Object, Object>> page = airTicketOrderListService.queryOrder(orderListPage, mapRequest, common);
		
		  //  文件名称str   文件表头列表Map   表头对应的属性  Map   class信息 class
        String fileName = "游客信息";
        String columnTitlesStr = "序号,销售,人数,单号,姓名,英文,性别,出生年月,护照号,发行地,职业,关系,押金,担保函,小费,自费,保险,备注,手机";
        String[] columnTitlesArray =  columnTitlesStr.split(",");
        
        String rowMsgsStr = "salerName,personNum,orderNo,travelName,nameSpell,travelSex,travelBirthDay,passportCode,passportPlace,,,,,,,,remark,telephone";
        String[] rowMsgsArray = rowMsgsStr.split(",");
        for(Map<Object,Object> entity:page.getList()){
    		//获取游客信息并过滤已改签游客
        	List<Map<String,Object>> travelInfoList = airTicketOrderService.queryAirticketOrderTravels(entity.get("id").toString());
        	entity.put("userList", travelInfoList);
        }
        try {
			ExportExcel.exportExcel(fileName,columnTitlesArray,rowMsgsArray,page.getList(),request,response,"orderAirticket");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	
	@SuppressWarnings("unchecked")
    private void handleStartTm(Date currDate, Map<Object, Object> map) {
		map.put("orderTypeFlag", "ng");

		Byte orderType = (Byte)map.get("orderType");
		if (1 == orderType) {
			List<Map<String, Object>> airticketOrderFlights = (List<Map<String, Object>>)map.get("airticketOrderFlights");
			if(airticketOrderFlights != null && airticketOrderFlights.size() > 0){
				Map<String, Object> subMap = airticketOrderFlights.get(0);
				Object obj = subMap.get("startTime");
				if(obj != null){
					Date dStartTime = new Date(((java.sql.Timestamp)obj).getTime());
					if(dStartTime.after(currDate)){
						map.put("orderTypeFlag", "ok");
					}
				}
			}
		} else if (2 == orderType) {
			Object obj = map.get("groupOpenDate");
			if (obj != null) {
				java.sql.Date groupOpenDate = (java.sql.Date) obj;
				Date dgroupOpenDate = new Date(groupOpenDate.getTime());
				if(dgroupOpenDate.after(currDate)){
					map.put("orderTypeFlag", "ok");
				}
			}
		}
	}
	
	/**
	 * 金额处理：金额千位符与金额多币种id和数值读取
	 * @param listin
	 */
    @SuppressWarnings("unchecked")
	private void handlePrice(Map<Object, Object> listin) {
		
		//尾款：用于页面尾款支付
		Object payStatusObj = listin.get("order_state");
		if(payStatusObj != null && 
				(Context.ORDER_PAYSTATUS_YZF.equals(payStatusObj.toString()) 
						|| Context.ORDER_PAYSTATUS_YZFDJ.equals(payStatusObj.toString()))) {
			
			if (listin.get("totalMoney") != null) {
				if (!listin.get("totalMoney").equals(listin.get("payedMoney"))) {
					listin.put("lastMoneyCurrencyId", "true");
				} 
			}
		}
		
		
		//千位符处理：订单总金额、已付金额、到账金额
		List<String> priceList = Lists.newArrayList();
		priceList.add("remainderMoney");
		priceList.add("totalMoney");
		priceList.add("payedMoney");
		priceList.add("accountedMoney");
 		handlePrice(listin, priceList);
		
		String formatedAdultPrice = formatMoney((String)listin.get("currencyMark"), listin.get("settlementAdultPrice"));
		listin.put("formatedAdultPrice", formatedAdultPrice);
		
		// 展开信息部分千位符、币种符号追加
		List<Map<String, Object>> airticketOrderFlights = (List<Map<String, Object>>)listin.get("airticketOrderFlights");
		for (Map<String, Object> airticketOrderFlight : airticketOrderFlights) {
			airticketOrderFlight.put(
					"formatedSubAdultPrice",
					formatMoney(
							(String) airticketOrderFlight.get("currencyMark"),
							airticketOrderFlight.get("settlementAdultPrice")));
		}
	}
	
	private String formatMoney(String currencyMark, Object money) {
		if (currencyMark == null) {
			currencyMark = "";
		}
		if (money == null) {
			money = new BigDecimal(0);
		}
		try {
			DecimalFormat d = new DecimalFormat(",##0.00");
			money = d.format(money);
		} catch (Exception e) {
			log.error("input para : currencyMark = " + currencyMark
					+ ", money = " + money + "\n" + e);
		}
		return currencyMark + money;
	}
	
	/**
	 * 订单金额千位符处理：订单总金额、已付金额、到账金额
	 * @param listin
	 * @param paraList
	 */
	private void handlePrice(Map<Object, Object> listin, List<String> paraList) {

		// 千位符
		DecimalFormat d = new DecimalFormat(",##0.00");
		if (CollectionUtils.isNotEmpty(paraList)) {
			for (String para : paraList) {

				if (listin.get("totalMoney") != null) {
					// 增加未收余额
					if ("remainderMoney".equals(para)) {
						// 获取总额、已付 字符串 eg: ￥ 200+$ 100
						String totalStr = listin.get("totalMoney").toString();
						if (listin.get("payedMoney") == null) {
							String totalArr[] = totalStr.split("\\+");
							List<String> totalList = Lists
									.newArrayList(totalArr);
							String tempMoneyStr = "";
							for (int i = 0; i < totalList.size(); i++) {
								String money[] = totalList.get(i).split(" ");
								if (money.length > 0
										&& !"0.00".equals(money[1])) {
									tempMoneyStr += money[0]
											+ d.format(new BigDecimal(money[1]))
											+ "+";
								}
							}
							if (StringUtils.isNotBlank(tempMoneyStr)) {
								listin.put(
										para,
										tempMoneyStr.substring(0,
												tempMoneyStr.length() - 1));
							}
						} else {
							String payedStr = listin.get("payedMoney")
									.toString();
							// 拆分字符串，得到 币种，金额 Map
							String totalArr[] = totalStr.split("\\+");
							String payedArr[] = payedStr.split("\\+");
							List<String> totalList = Lists
									.newArrayList(totalArr);
							List<String> payedList = Lists
									.newArrayList(payedArr);
							String remainderString = getRemainderMoneyStr(
									totalList, payedList);
							listin.put(para, remainderString);
						}
					}
				}
				
				if (listin.get(para) != null) {
					String moneyStr = listin.get(para).toString();
					String allMoney[] = moneyStr.split("\\+");
					if (allMoney.length > 1) {
						String tempMoneyStr = "";
						for (int i = 0; i < allMoney.length; i++) {
							String money[] = allMoney[i].split(" ");
							// 币种价格等于0的时候不显示
							if (money.length > 1 && !"0.00".equals(money[1])) {
								tempMoneyStr += money[0]
										+ d.format(new BigDecimal(money[1]))
										+ "+";
							}
						}
						if (StringUtils.isNotBlank(tempMoneyStr)) {
							listin.put(
									para,
									tempMoneyStr.substring(0,
											tempMoneyStr.length() - 1));
						}
					} else {
						String money[] = allMoney[0].split(" ");
						if (money.length > 1) {
							String currencyMark = money[0].toString();
							String currencyMoney = money[1].toString();
							String moneyAmonut = d.format(new BigDecimal(
									currencyMoney));
							listin.put(para, currencyMark + moneyAmonut);
						}
					}

				}
			}
		}
	}
	
	private String getRemainderMoneyStr(List<String> totalList, List<String> payedList){
		//千位符
		DecimalFormat d = new DecimalFormat(",##0.00");
		List<String> reusltList = maService.subtract(totalList, payedList);
		String tempMoneyStr = "";
		if (CollectionUtils.isNotEmpty(reusltList)) {			
			String currencyArr [] = reusltList.get(0).split("\\,");
			String amountArr [] = reusltList.get(1).split("\\,");
			for (int i = 0; i < amountArr.length; i++) {
				if (amountArr.length > 0 && !"0.00".equals(amountArr[0])) {
					tempMoneyStr += currencyArr[i] + d.format(new BigDecimal(amountArr[i])) + "+";
				}
			}
			if(StringUtils.isNotBlank(tempMoneyStr)) {
				tempMoneyStr = tempMoneyStr.substring(0, tempMoneyStr.length()-1);
			}
		}else{
			//如果未收余额为空，则默认显示为0.00
			tempMoneyStr = "0.00";
		}
		return tempMoneyStr;
	}
		
	/**
	 * 计算订单剩余天数
	 * @param listin
	 * 设为公共静态方法，供其他机票业务调用
	 */
	@SuppressWarnings("unused")
    public static void getOrderLeftTime(Map<Object, Object> listin) {
		
		//判断剩余时间用激活时间：新需求（2015-1-7）
        String activationDate = String.valueOf(listin.get("activationDate"));
        String proPayMode = String.valueOf(listin.get("occupyType"));
        String proRemainDays = String.valueOf(listin.get("remaindDays"));
        String payStatus = String.valueOf(listin.get("order_state"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        //支付方式不为全款支付且订单支付状态不为申请退团、已取消、同步订单、已支付、已支付订金
        if (proPayMode.equals("2") || proPayMode.equals("3")) {
        	try {
        		if (activationDate != null && !"null".equals(activationDate)) {
        			Date date = format.parse(activationDate);
    				Calendar cal = Calendar.getInstance();
    				cal.setTime(date);
    				Calendar currentDate = Calendar.getInstance();
    				
    				long betweens = currentDate.getTimeInMillis() - cal.getTimeInMillis();
    				long remains = Long.valueOf(proRemainDays) * (1000*3600*24);
    				long offTime = remains - betweens;
    				long distanceDays = offTime / (1000*3600*24);
    				long distanceHours = (offTime - (distanceDays * (1000*3600*24))) / (1000*3600);
    				if (offTime > 0) {
    					listin.put("leftDays", distanceDays + "天" + distanceHours + "小时");
    				} else {
    					listin.put("leftDays", "无");
    				}
        		} else {
        			listin.put("leftDays", "无");
        		}
			} catch (ParseException e) {
				log.error("日期转换异常", e);
			}
        } else {
        	listin.put("leftDays", "无");
        }
	}
	
	/**
	 * 查询达帐支付订单与支付订单
	 * @param listProId 订单ids
	 * @param listorder 订单list
	 * @param orderStatus 订单类型
	 * @param model
	 */
	private void selectPayOrder(List<Long> listProId, List<Map<Object, Object>> listorder, Model model) {
		
		if (CollectionUtils.isNotEmpty(listProId)) {
            List<Orderpay> orderpayList = orderService.findOrderpayByOrderIds(listProId, Context.ORDER_TYPE_JP);
            for (Map<Object, Object> map : listorder) {
                List<Orderpay> listTempOrderPay = new ArrayList<Orderpay>();
				for (int i = 0; i < orderpayList.size(); i++) {
                	
                	Orderpay orderpay = orderpayList.get(i);
                	Integer isDJPayed = 0;// 订金是否已支付
                	Integer isQKPayed = 0;// 全款是否已支付
                	
                    //如果orderpay的订单id  等于  pro的订单id
                    if (orderpay.getOrderId().intValue() == Integer.parseInt(map.get("id").toString())) {
                        listTempOrderPay.add(orderpay);
                        if (StringUtils.isNotBlank(orderpay.getMoneySerialNum())) {
                        	orderService.clearObject(orderpay);
                        	orderpay.setMoneySerialNum(moneyAmountService.getMoneyStr(orderpay.getMoneySerialNum()));
                        	//判断订单是否已支付订金
                        	if (isDJPayed == 0 && orderpay.getPayPriceType() == Integer.parseInt(Context.ORDER_ORDERTYPE_ZFDJ)) {
                        		isDJPayed = 1;
                        		map.put("isDJPayed", isDJPayed);
                        	}
                        	//判断订单是否已支付全款
                        	if (isQKPayed == 0 && orderpay.getPayPriceType() == Integer.parseInt(Context.ORDER_ORDERTYPE_ZFQK)) {
                        		isQKPayed = 1;
                        		map.put("isQKPayed", isQKPayed);
                        	}
                        }
                        
						if (orderpayList.size() - 1 == i) {
							if (orderpay.getPaymentStatus() != null && orderpay.getPaymentStatus() != 0) {
								map.put("paymentStatus", orderpay.getPaymentStatus());
							}
						}
                    }
                }
                
                //支付信息
                map.put("orderPayList", listTempOrderPay);
                
                //达帐和撤销提示
    			boolean isCanceledOrder = false;
                if (map.get("order_state") != null) {
                	isCanceledOrder = Context.ORDER_PAYSTATUS_YQX.equals(map.get("order_state").toString());
                }
    			String orderPrompt = orderService.getOrderPrompt("7", Long.valueOf(map.get("id").toString()), isCanceledOrder);
    			map.put("orderPrompt",orderPrompt);
            }
        }
	}
	
	/**
	 * 判断订单是否可以支付 
	 * @param orderId
	 * @param payPriceType 全款-1 订金-3 尾款-2
	 * @return
	 * @throws JSONException
	 */
	@ResponseBody
	@RequestMapping(value="whetherCanPay",method=RequestMethod.POST)
	public Object whetherCanPay(@RequestParam(value="orderId") String orderId, @RequestParam(value="payPriceType") String payPriceType) 
			throws JSONException {
		
		net.sf.json.JSONArray results = new net.sf.json.JSONArray();
		net.sf.json.JSONObject resobj = new net.sf.json.JSONObject();
		
		AirticketOrder order = airTicketOrderService.getAirticketorderById(Long.parseLong(orderId));
		
		//如果订单为空则返回错误
		if (order == null) {
			resobj.put("flag", "false");
			resobj.put("warning", "查询不到此订单");
			results.add(resobj);
			return results;
		}
		
		//如果支付订单状态为空则返回错误
		Integer payStatus = order.getOrderState();//订单状态
		if (payStatus == null) {
			resobj.put("flag", "false");
			resobj.put("warning", "订单支付状态为空");
			results.add(resobj);
			return results;
		}
		
		//查询订单支付记录，并判断订单是否已经支付订金或支付全款
		List<Long> ids = Lists.newArrayList();
		ids.add(order.getId());
		//根据订单id和订单类型查询订单支付记录
		List<Orderpay> orderpayList = orderService.findOrderpayByOrderIds(ids, Context.ORDER_TYPE_JP);
		boolean isDJPayed = false;
		boolean isQKPayed = false;
		if (CollectionUtils.isNotEmpty(orderpayList)) {
			for (Orderpay orderPay : orderpayList) {
				String priceType = orderPay.getPayPriceType().toString();
				//判断支付记录中是否有支付订金
				if (!isDJPayed && Context.ORDER_ORDERTYPE_ZFDJ.equals(priceType)) {
					isDJPayed = true;
				}
				//判断支付记录中是否有支付全款
				if (!isQKPayed && Context.ORDER_ORDERTYPE_ZFQK.equals(priceType)) {
					isQKPayed = true;
				}
			}
		}
			
		//订金金额：用于页面订金支付
		List<String> frontCurreney = Lists.newArrayList();
		if (StringUtils.isNotBlank(order.getFrontMoney()) && Context.ORDER_ORDERTYPE_ZFDJ.equals(payPriceType)) {
			
			//如果已支付订金或已支付全款则返回
			if (isDJPayed || isQKPayed) {
				resobj.put("flag", "false");
				resobj.put("warning", "订金已支付");
				results.add(resobj);
				return results;
			}
			
			List<Object[]> list = moneyAmountService.getMoneyAmonut(order.getFrontMoney());
			if (CollectionUtils.isNotEmpty(list)) {
				String currencyId = "";
	    		String currencyPrice = "";
				for (int i=0;i<list.size();i++) {
					frontCurreney.add(list.get(i)[0].toString() + " " + list.get(i)[3]);
    				if (i == list.size() -1) {
    					currencyId += list.get(i)[0];
	        			currencyPrice += list.get(i)[3];
    				} else {
    					currencyId += list.get(i)[0] + ",";
	        			currencyPrice += list.get(i)[3].toString() + ",";
    				}
    			}
				resobj.put("flag", "true");
				resobj.put("payPriceType", Context.ORDER_ORDERTYPE_ZFDJ);
				resobj.put("moneyCurrencyId", currencyId);
				resobj.put("moneyCurrencyPrice", currencyPrice);
			}
		}
		
		//订单总额：用于页面全款支付
		List<String> totalCurreney = Lists.newArrayList();
		if (StringUtils.isNotBlank(order.getTotalMoney()) && Context.ORDER_ORDERTYPE_ZFQK.equals(payPriceType)) {
			
			//如果已支付全款则返回
			if (isQKPayed) {
				resobj.put("flag", "false");
				resobj.put("warning", "全款已支付");
				results.add(resobj);
				return results;
			}
			
			//如果已支付订金，则如果点支付全款，实则支付尾款
			if (isDJPayed) {
				payPriceType = Context.ORDER_ORDERTYPE_ZFWK;
			} else {
				List<Object[]> list = moneyAmountService.getMoneyAmonut(order.getTotalMoney());
				if (CollectionUtils.isNotEmpty(list)) {
					String currencyId = "";
		    		String currencyPrice = "";
					for (int i=0;i<list.size();i++) {
						totalCurreney.add(list.get(i)[0].toString() + " " + list.get(i)[3]);
	    				if (i == list.size() -1) {
	    					currencyId += list.get(i)[0];
		        			currencyPrice += list.get(i)[3];
	    				} else {
	    					currencyId += list.get(i)[0] + ",";
		        			currencyPrice += list.get(i)[3] + ",";
	    				}
	    			}
					resobj.put("flag", "true");
					resobj.put("payPriceType", Context.ORDER_ORDERTYPE_ZFQK);
					resobj.put("moneyCurrencyId", currencyId);
					resobj.put("moneyCurrencyPrice", currencyPrice);
				}
			}
		}
		
		//尾款：用于页面尾款支付
		if(payStatus != null && (isDJPayed || isQKPayed) && Context.ORDER_ORDERTYPE_ZFWK.equals(payPriceType)) {
			
			BigDecimal payedMoney = new BigDecimal(0);
			BigDecimal totalMoney = new BigDecimal(0);
			
			//应收
			if (StringUtils.isNotBlank(order.getTotalMoney())) {
				List<Object[]> list = moneyAmountService.getMoneyAmonut(order.getTotalMoney());
				if (CollectionUtils.isNotEmpty(list)) {
					for (int i=0;i<list.size();i++) {
						totalCurreney.add(list.get(i)[0].toString() + " " + list.get(i)[3]);
						if (list.get(i)[3] != null && list.get(i)[4] != null) {
							totalMoney = totalMoney.add(new BigDecimal(list.get(i)[3].toString()).multiply(new BigDecimal(list.get(i)[4].toString())));
						}
	    			}
				}
			}
			
			//已收
			List<String> payedCurreney = Lists.newArrayList();
			if (StringUtils.isNotBlank(order.getPayedMoney())) {
				List<Object[]> list = moneyAmountService.getMoneyAmonut(order.getPayedMoney());
				if (CollectionUtils.isNotEmpty(list)) {
					for (int i=0;i<list.size();i++) {
						payedCurreney.add(list.get(i)[0].toString() + " " + list.get(i)[3]);
						if (list.get(i)[3] != null && list.get(i)[4] != null) {
							payedMoney = payedMoney.add(new BigDecimal(list.get(i)[3].toString()).multiply(new BigDecimal(list.get(i)[4].toString())));
						}
	    			}
				}
			}
			List<String> result = moneyAmountService.subtract(totalCurreney, payedCurreney);
			
			//如果有负值，则把尾款转换成人民币，如果为正则可支付，如果为负则不允许支付
			if (CollectionUtils.isNotEmpty(result) && result.get(1).contains("-")) {
				if (totalMoney.compareTo(payedMoney) < 0) {
					resobj.put("flag", "false");
					resobj.put("warning", "已支付金额大于订单金额，不能再支付尾款");
					results.add(resobj);
					return results;
				} else {
					resobj.put("flag", "true");
					resobj.put("payPriceType", Context.ORDER_ORDERTYPE_ZFWK);
					resobj.put("moneyCurrencyId", result.get(0));
					resobj.put("moneyCurrencyPrice", result.get(1));
				}
			} else {
				if (CollectionUtils.isNotEmpty(result)) {
					resobj.put("flag", "true");
					resobj.put("payPriceType", Context.ORDER_ORDERTYPE_ZFWK);
					resobj.put("moneyCurrencyId", result.get(0));
					resobj.put("moneyCurrencyPrice", result.get(1));
				} else {
					if (CollectionUtils.isNotEmpty(totalCurreney)) {
						resobj.put("flag", "true");
						resobj.put("payPriceType", Context.ORDER_ORDERTYPE_ZFWK);
						resobj.put("moneyCurrencyId", totalCurreney.get(0).split(" ")[0]);
						resobj.put("moneyCurrencyPrice", "0.00");
					} else {
						resobj.put("flag", "false");
						resobj.put("warning", "订单没有支付金额");
						results.add(resobj);
						return results;
					}
				}
			}
		}
		Map<String, String> totalMoneyMap = moneyContactVal(moneyAmountService
				.getMoneyAmonut(order.getTotalMoney()));
		resobj.put("totalMoneyCurrencyId", totalMoneyMap.get("currencyId") == null ? "" : totalMoneyMap.get("currencyId"));
		resobj.put("totalMoneyCurrencyPrice",
				totalMoneyMap.get("currencyPrice") == null ? "" :  totalMoneyMap.get("currencyPrice"));
		
		results.add(resobj);
		return results;
	}
	
	/**
	 * 取得金额连接的字符串
	 * @param list
	 * @return
	 */
	private Map<String, String> moneyContactVal(List<Object[]> list) {
		Map<String, String> map = new HashMap<String, String>();

		if (CollectionUtils.isNotEmpty(list)) {
			String currencyId = "";
			String currencyPrice = "";
			for (int i = 0; i < list.size(); i++) {
				if (i == list.size() - 1) {
					currencyId += list.get(i)[0];
					currencyPrice += list.get(i)[3];
				} else {
					currencyId += list.get(i)[0] + ",";
					currencyPrice += list.get(i)[3] + ",";
				}
			}
			map.put("currencyId", currencyId);
			map.put("currencyPrice", currencyPrice);
		}

		return map;
	}
	
	/**
	 * 根据查询条件查询机票订单列表-计调
	 * @param model
	 * @param showType
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("all")
	@RequestMapping(value="airticketOrderListForOp")
	public String queryOpOrderListByCondition(Model model,HttpServletRequest request,HttpServletResponse response){
		
		//1.初始化一些查询条件
		initParam(model);
		
		//2.参数处理：机票订单状态、订单编号或团期编号、机票类型：1-多段 2-往返 3-单程、机票区域类型 1-内陆+国际 2-国际 3-国内、
		//2.出发地、联系人、目的地ID、目的地名称、销售、下单人、计调、渠道ID、出发开始日期、出发结束日期、返回开始日期、返回结束日期、发票状态、收据状态
		Map<String, String> mapRequest = Maps.newHashMap(); //查询条件
        String paras = "showType,orderNumOrOrderGroupCode,airType,ticketType,fromAreaId,contact,targetAreaIdList,targetAreaNameList," +
        		"saler,picker,op,agentId、startAirTime、endAirTime、returnStartAirTime、returnEndAirTime、invoiceStatus,receiptStatus";
        OrderCommonUtil.handlePara(paras, mapRequest, model, request);
		
		//3.获取用户角色信息
		Map<String, Object> roleMap = prepareRole();
		
		//4.查询列表
		Page<Map<Object, Object>> orderListPage = new Page<Map<Object, Object>>(request, response);
		Page<Map<Object, Object>> page = airTicketOrderListService.queryOrder(orderListPage, mapRequest, null);
		
		List<Map<Object, Object>> list = page.getList();
	
		for(int i=0;i<list.size();i++){
			Map temp = list.get(i);
			getOrderLeftTime((temp));
			handlePrice(temp);
			list.set(i, temp);
		}
		page.setList(list);
		model.addAttribute("page",page );
		model.addAttribute("conditionsMap", mapRequest);
		
		List<User> users = systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId());
	    model.addAttribute("users",users);

		//5.订单列表页
		return "modules/order/airticketOrderListForOp";
	}
	
	/**
	 * 查询出发城市、渠道、国家和订单提醒参数
	 * @param model
	 */
	private void initParam(Model model) {
		
		//出发城市
		Map<String, String> fromAreasMap = DictUtils.findUserDict(UserUtils.getUser().getCompany().getId(), "fromarea");
		model.addAttribute("fromAreasMap", fromAreasMap);
		
		//渠道
		AgentinfoService agentinfoService = SpringContextHolder.getBean("agentinfoService");
		List<Agentinfo> agentinfoList = agentinfoService.findAllAgentinfo();
		Agentinfo agentinfo = new Agentinfo();
		agentinfo.setId(-1L);
		agentinfo.setAgentName("非签约渠道");
		agentinfoList.add(0,agentinfo);
		model.addAttribute("agentinfoList", agentinfoList);
		
		//国家和订单提醒
		model.addAttribute("countryList", CountryUtils.getCountrys());
		model.addAttribute("isNeedNoticeOrder", UserUtils.getUser().getCompany().getIsNeedAttention());
	}

	private Map<String,Object> prepareRole() {
		
		User userInfo = UserUtils.getUser();
		List<Role> roleList = userInfo.getRoleList();
		
		Map<String, Object> roleMap = Maps.newHashMap();
		for (Role role : roleList) {
			roleMap.put(role.getRoleType(), role.getDepartment());
		}
		return roleMap;
	}
	/**
	 * add by ruyi.chen 
	 * add date 2015-10-15
	 * 验证订单是否可取消或删除
	 * @param request
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value ="canCancelOrDelOrder")
    public String canCancelOrDelOrder(HttpServletRequest request) {
        String id = request.getParameter("orderId");
        String type = request.getParameter("type");
        if ("1".equals(type)) {
        	type = "取消";
        } else {
        	type = "删除";
        }
        if (StringUtils.isNotBlank(id)) {
        	AirticketOrder order = airTicketOrderService.getAirticketorderById(Long.parseLong(id));
        	String result = OrderCommonUtil.canCancel(7,order.getId());
			if ("1".equals(result)) {
				return "此订单已有开发票记录，不能" + type;
			} else if("2".equals(result)) {
				return "此订单已有开收据记录，不能" + type;
			} else {
				return "0";
			}
        } else {
        	return "订单不存在!";
        }
    }
	
	/**
	 * 上传确认单
	 * @param orderId
	 * @param orderConfirmation
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("uploadConfirmation")
	public String uploadConfirmation(String orderId,MultipartFile orderConfirmation,HttpServletRequest request,HttpServletResponse response) {
		
		try {
			//上传的文件名
			String srcFileName = orderConfirmation.getOriginalFilename();
			
			//文件上传到服务器,返回文件在存储的相对路径
			String relativePath = FileUtils.uploadFile(orderConfirmation.getInputStream(), srcFileName);
			
			//文件路径保存到数据库,保存订单文件关系
			airTicketOrderService.saveFilePathAndRelation(orderId,srcFileName,relativePath);
			
			
		} catch (IOException e) {
			log.error("文件上传失败。。。");
			e.printStackTrace();
		}
		return "redirect:airticketOrderList/1";
	}

	/**
	 * 已占位订单的 确认操作
	 * @param orderId 订单id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="seizedConfirm", method=RequestMethod.POST)
	public Map<String, String> seizedConfirm(Model model, HttpServletRequest request) {
		Map<String, String> returnResult = new HashMap<>();
		String orderId = request.getParameter("orderId");
		if (com.trekiz.admin.common.utils.StringUtils.isBlank(orderId)) {
			returnResult.put("flag", "faild");
			returnResult.put("message", "订单id获取失败！");
			return returnResult;
		}
		// 获取订单
		AirticketOrder airticketOrder = airTicketOrderService.getAirticketorderById(Long.parseLong(orderId));
		//判断订单占位状态 、确认状态
		if (airticketOrder == null) {
			returnResult.put("flag", "faild");
			returnResult.put("message", "订单获取失败！");
			return returnResult;
		}
		if (!(Context.ORDER_PAYSTATUS_YZW.equals(airticketOrder.getOrderState().toString()) || Context.ORDER_PAYSTATUS_YZF.equals(airticketOrder.getOrderState().toString()) || Context.ORDER_PAYSTATUS_YZFDJ.equals(airticketOrder.getOrderState().toString()))) {
			returnResult.put("flag", "faild");
			returnResult.put("message", "不是已占位订单！");
			return returnResult;
		}
		if (Context.SEIZEDCONFIRMATIONSTATUS_1 == airticketOrder.getSeizedConfirmationStatus()) {
			returnResult.put("flag", "faild");
			returnResult.put("message", "订单已确认！");
			return returnResult;
		}
		// 变更确认状态
		airTicketOrderService.handleConfirmStatus(airticketOrder);
		returnResult.put("flag", "success");
		return returnResult;
	}

}
