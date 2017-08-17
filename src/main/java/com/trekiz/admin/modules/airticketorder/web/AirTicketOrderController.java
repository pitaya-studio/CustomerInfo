package com.trekiz.admin.modules.airticketorder.web;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.params.Params;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.FileUtils;
import com.trekiz.admin.common.utils.SpringContextHolder;
import com.trekiz.admin.common.utils.excel.ExportExcel;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.entity.OrderAgentAjax;
import com.trekiz.admin.modules.airticketorder.entity.OrderTravelAjax;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.airticketorder.utils.AirticketOrderUtil;
import com.trekiz.admin.modules.invoice.entity.Orderinvoice;
import com.trekiz.admin.modules.invoice.service.OrderinvoiceService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.airticket.service.IAirticketPreOrderService;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.entity.ProductOrder;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderContactsService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.receipt.entity.OrderReceipt;
import com.trekiz.admin.modules.receipt.service.OrderReceiptService;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.repository.ReviewCompanyDao;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewLogService;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.stock.entity.AirticketActivityReserve;
import com.trekiz.admin.modules.supplier.entity.SupplierContacts;
import com.trekiz.admin.modules.supplier.entity.SupplierContactsView;
import com.trekiz.admin.modules.supplier.service.SupplierContactsService;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.Menu;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.repository.DepartmentDao;
import com.trekiz.admin.modules.sys.repository.UserJobDao;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.service.LogOperateService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;


/**
 * 机票订单处理
 * @author wl
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/order/manage")
public class AirTicketOrderController {
	
	private static final Logger log = Logger.getLogger(AirTicketOrderController.class);
	@Autowired
	private LogOperateService logOpeService;
	@Autowired
	private IActivityAirTicketService activityAirTicketService;
	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	@Autowired
	private OrderCommonService  commonService;
	@Autowired
	private AreaService areaService;
    @Autowired
    private ReviewService reviewService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	// ==  修改 开始
	@Autowired
	private OrderCommonService orderService;
	@Autowired
	private ActivityGroupService activityGroupService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private ReviewLogService reviewLogService;
	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	@Autowired
	private DocInfoService docInfoService;
	@Autowired
	private IAirticketPreOrderService airticketPreOrderService;
	@Autowired
	private CurrencyService currencyService;
	// == 修改 结束
	@Autowired
    private OrderContactsService orderContactsService;
	@Autowired
    private AgentinfoService agentinfoService;
	@Autowired
    private SupplierContactsService supplierContactsService;
	@Autowired
    private OrderinvoiceService orderinvoiceService;
    @Autowired
    private OrderReceiptService orderreceiptService;
	
	@RequestMapping(value="cancelOrder")
	@ResponseBody
	public String cancelOrder(HttpServletRequest request) throws NumberFormatException, Exception{
	    String orderId = request.getParameter("orderId");
	    String description = request.getParameter("description");
	    if (StringUtils.isNotBlank(orderId)) {
	    	/**
			 * update by ruyi.chen
			 * update date 2015-10-12
			 *  判断发票收据状态是否符合取消条件
			 */
	    	AirticketOrder airticketOrder =	airTicketOrderService.getAirticketorderById(Long.parseLong(orderId));
			String result = OrderCommonUtil.canCancel(7,airticketOrder.getId());
			if("1".equals(result)){
				return "此订单已有开发票记录，不能取消!";
			}else if("2".equals(result)){
				return "此订单已有开收据记录，不能取消!";
			}
	    	return airTicketOrderService.cancelOrder(Long.valueOf(orderId), description);
	    } else {
	    	return "订单id不能为空";
	    }
	}
	
	/**
	 * @Description 撤销订单占位：仅针对财务确认占位生成订单
	 * @author yakun.bai
	 * @Date 2015-11-16
	 */
	@RequestMapping(value="revokeOrder")
	@ResponseBody
	public String revokeOrder(HttpServletRequest request) throws NumberFormatException, Exception{
	    String orderId = request.getParameter("orderId");
	    if (StringUtils.isNotBlank(orderId)) {
	    	return airTicketOrderService.revokeOrder(Long.valueOf(orderId));
	    } else {
	    	return "订单id不能为空";
	    }
	}
	
	
	/**
	 * add by ruyi.chen
	 * add date 2015-10-13
	 * 机票订单取消验证
	 * @param request
	 * @return
	 */
	@ResponseBody
    @RequestMapping(value ="canCancelOrder")
    public String canCancelOrder(HttpServletRequest request){
        String id = request.getParameter("orderId");
        if (StringUtils.isNotBlank(id)) {
        	AirticketOrder airticketOrder =	airTicketOrderService.getAirticketorderById(Long.parseLong(id));
			String result = OrderCommonUtil.canCancel(7,airticketOrder.getId());
			if("1".equals(result)){
				return "此订单已有开发票记录，不能取消!";
			}else if("2".equals(result)){
				return "此订单已有开收据记录，不能取消!";
			}else{
				return "0";
			}
        } else {
        	return "订单不存在!";
        }
        
    }
	/**
	 * 机票订单取消日志
	 */
	
//	public void cancelAirticketLog(AirticketOrder airticketOrder,String description){
//	 	String content = "取消机票订单成功,订单编号为:"+airticketOrder.getOrderNo()+"，取消原因为："+description;	
//		logOpeService.saveLogOperate(Context.log_type_orderform,Context. log_type_orderform_name, content, "3");
//		
//	}
	
	@ResponseBody
	@RequestMapping(value = "deleteOrderByFlag")
	public String deleteOrderByFlag(Model model, HttpServletRequest request)
			throws OptimisticLockHandleException, PositionOutOfBoundException,
			Exception {
		String id = request.getParameter("orderId");
		if (StringUtils.isNotBlank(id)) {
			AirticketOrder airticketOrder =	airTicketOrderService.getAirticketorderById(Long.parseLong(id));
			/**
			 * update by ruyi.chen
			 * update date 2015-10-12
			 *  判断发票收据状态是否符合取消条件
			 */
			String result = OrderCommonUtil.canCancel(7,airticketOrder.getId());
			if("1".equals(result)){
				return "此订单已有开发票记录，不能删除!";
			}else if("2".equals(result)){
				return "此订单已有开收据记录，不能删除!";
			}
			deleteAirticketLog(airticketOrder);
			return airTicketOrderService.deleteOrder(Long.parseLong(id));
		}
		return "ok";
	}
	
	/**
	 * 机票订单删除日志
	 */
	
	public void deleteAirticketLog(AirticketOrder airticketOrder){
	 	String content = "删除机票订单成功,订单编号为:" + airticketOrder.getOrderNo();
		logOpeService.saveLogOperate(Context.log_type_orderform,
				Context.log_type_orderform_name, content, "3", Context.ProductType.PRODUCT_AIR_TICKET, airticketOrder.getId());
		
	}
	
	@ResponseBody
    @RequestMapping(value ="invokeOrder")
    public String invokeOrder(HttpServletRequest request){
        String id = request.getParameter("orderId");
        if (StringUtils.isNotBlank(id)) {
        	try {
        		return airTicketOrderService.invokeOrder(Long.parseLong(id));
			} catch (Exception e) {
				e.printStackTrace();
				return e.getMessage();
			}
        } else {
        	return "订单id不能为空";
        }
        
    }
	@RequestMapping(value = "lockOrder")
    @ResponseBody
    public Object lockOrder(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        String orderId = request.getParameter("orderId");
        this.airTicketOrderService.lockOrder(Long.valueOf(orderId));
        map.put("success", "success");
        return map;
    }
    
    @RequestMapping(value = "unLockOrder")
    @ResponseBody
    public Object unLockOrder(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        String orderId = request.getParameter("orderId");
        this.airTicketOrderService.unLockOrder(Long.valueOf(orderId));
        map.put("success", "success");
        return map;
    }
    
    /**
	 * 确认占位
	 * @param model
	 * @param request
	 * @return string
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value ="confirmOrder")
	public String confirmOrder(Model model, HttpServletRequest request) {
	    String flag = "fail";
	    String id = request.getParameter("orderId");
	    if (StringUtils.isNotBlank(id)) {
	    	AirticketOrder airticketOrder =	airTicketOrderService.getAirticketorderById(Long.parseLong(id));
	    	if (airticketOrder != null) {
    			Integer payStatus = airticketOrder.getOrderState();
    			String status = payStatus.toString();
    			if (Context.ORDER_PAYSTATUS_OP.equals(status)) {
    				try {
    					flag = airTicketOrderService.confirmOrder(Long.parseLong(id), request);
					} catch (Exception e) {
						e.printStackTrace();
						return e.getMessage();
					}
    			} else {
    				return "只有订单状态为'待计调确认'的订单才能确认占位";
    			}
    		}	    	
	    }
	    if (StringUtils.isBlank(flag)) {
	    	flag = "fail";
	    }
	    return flag;
	}
	
	/**
	 * 根据查询条件查询机票订单列表-销售
	 * @param model
	 * @param showType
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
    @RequestMapping(value="airticketOrderListForSale")
	public String querySaleOrderListByCondition(Model model,HttpServletRequest request,HttpServletResponse response){
		
		
		
		//1.初始化一些查询条件
		initQueryCond(model);
		initParam(model);
		
		//2.获取查询参数
		Map<String, Object> conditionsMap = prepareQueryCond(request);
		
		//3.获取用户角色信息
		Map<String, Object> roleMap = prepareRole();
		
		//4.查询列表
		Page<Map<String,Object>> page = airTicketOrderService.queryAirticketOrderListByCond(request,response,UserUtils.getUser(),conditionsMap,roleMap);
		
		for (int i = 0; i < page.getList().size(); i++) {
			if(page.getList().get(i).get("accounted_money")!=null){
				String muuid=page.getList().get(i).get("accounted_money").toString();
				String moString =moneyAmountService.getMoAmount(muuid);
				if(moString.length()>3){
					moString = moString.substring(0,4);
				}
//				System.out.println(moString);
				if(("0.00").equals(moString)){
					page.getList().get(i).put("accounted_money", "未达账");
				}
			}
		}
			
			
		
		List<Map<String, Object>> list = page.getList();

		for (int i = 0; i < page.getList().size(); i++) {
			
			Map<String, Object> map  = page.getList().get(i);
			
			//达帐和撤销提示
			boolean isCanceledOrder = false;
            if (map.get("order_state") != null) {
//            	isCanceledOrder = Context.ORDER_PAYSTATUS_YQX.equals(map.get("payStatus").toString());
            	isCanceledOrder = Context.ORDER_PAYSTATUS_YQX.equals(map.get("order_state").toString());//把上面一行替换为了下面一行 2015年3月17日20:25:29 by chy
            }
			String orderPrompt = commonService.getOrderPrompt("7", Long.valueOf(map.get("id").toString()), isCanceledOrder);
			map.put("orderPrompt",orderPrompt);
		}
		
		List<Long> listProId = new ArrayList<Long>();
		
		Date currDate = new Date();
		
//		for (Map<String, Object> temp : list) {
//			
//			if(temp.get("id") != null) {
//        		listProId.add(Long.parseLong(temp.get("id").toString()));
//        	}
//			
//			//获取订单剩余时间
//			getOrderLeftTime(temp);
//			
//			//金额处理
//        	handlePrice(temp);
//        	
//        	handleStartTm(currDate, temp);
//}
		
		
		for(int i=0;i<list.size();i++){
			Map temp = list.get(i);
			if(temp.get("id") != null) {
        		listProId.add(Long.parseLong(temp.get("id").toString()));
        	}
			getOrderLeftTime2(temp);
			//金额处理
        	handlePrice(temp);
        	handleStartTm(currDate, temp);
			list.set(i, temp);
		}
		
		//支付订单查询
        selectPayOrder(listProId, list, "7", model);
        page.setList(list);
		model.addAttribute("page", page);
//		//计算剩余天数
//		for (int i = 0; i < page.getList().size(); i++) {
//			String createDate = page.getList().get(i).get("createDate")
//					.toString();
//			String remainDays = page.getList().get(i).get("remaindDays")
//					.toString();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//			try {
//				Date dateDays = sdf.parse(createDate);
//				Calendar cl = Calendar.getInstance();
//				cl.setTime(dateDays);
//				cl.add(Calendar.DATE, Integer.parseInt(remainDays));
//				String datestr = sdf.format(cl.getTime());//创建时间加上保留天数的时间
//				System.out.println(datestr);
//				SimpleDateFormat sdfnow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				String nowdate = sdfnow.format(new Date()); //系统当前时间
//				DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//				Date dt1 = df.parse(datestr);
//				Date dt2 = df.parse(nowdate);
//				if(dt1.getTime()>=dt2.getTime()){
//					int redays = dt1.getDate()-dt2.getDate();
//					int rehours = dt1.getHours()-dt2.getHours();
//					if(rehours<0){
//						redays -=1 ;
//						rehours += 24;
//					}
//					page.getList().get(i).put("remaindDays", "剩余时间"+redays+"天"+rehours+"小时");
//				}else{
//					page.getList().get(i).put("remaindDays", "无");
//				}
//			} catch (ParseException e) {
//				e.printStackTrace();
//			}
	//	}
		model.addAttribute("operate", UserUtils.getUser().getName());
		model.addAttribute("conditionsMap", conditionsMap);		
		//5.订单列表页
		
		Menu menu = departmentService.getMenuByUrl(request);
        if (menu != null) {
        	request.setAttribute("_m", menu.getParent() != null ? menu.getParent().getId() : null);
        	request.setAttribute("_mc", menu.getId());
        }
	    //add by jiangyang
        List<User> users = systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId());
	    model.addAttribute("users",users);
		
		return "modules/order/airticketOrderListForSale";
	}
	
	@SuppressWarnings("unchecked")
    private void handleStartTm(Date currDate, Map<String, Object> map) {
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
    private void handlePrice(Map<String, Object> listin) {
		
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
	private void handlePrice(Map<String, Object> listin, List<String> paraList) {
		
		//千位符
		DecimalFormat d = new DecimalFormat(",##0.00");
		if (CollectionUtils.isNotEmpty(paraList)) {
			for (String para : paraList) {
				if (listin.get(para) != null) {
					String moneyStr = listin.get(para).toString();
					String allMoney [] = moneyStr.split("\\+");
					if (allMoney.length > 1) {
						String tempMoneyStr = "";
						for (int i=0;i<allMoney.length;i++) {
							String money [] = allMoney[i].split(" ");
							//币种价格等于0的时候不显示
							if (money.length > 1 && !"0.00".equals(money[1])) {
								tempMoneyStr += money[0] + d.format(new BigDecimal(money[1])) + "+";
							}
						}
						if(StringUtils.isNotBlank(tempMoneyStr)) {
							listin.put(para, tempMoneyStr.substring(0, tempMoneyStr.length()-1));
						}
					} else {
						String money [] = allMoney[0].split(" ");
						if (money.length > 1) {
							String aa = money[0].toString();
							String bb = money[1].toString();
							String cc = d.format(new BigDecimal(bb));
							listin.put(para, aa + cc);
						}
					}
				}
			}
		}
	}
	
	
	
	/**
	 * 计算订单剩余天数
	 * @param listin
	 * 设为公共静态方法，供其他机票业务调用
	 */
	@SuppressWarnings("unused")
    public static void getOrderLeftTime2(Map<String, Object> listin) {
		
		//判断剩余时间用激活时间：新需求（2015-1-7）
        String activationDate = String.valueOf(listin.get("activationDate"));
        String proPayMode = String.valueOf(listin.get("occupyType"));
        String proRemainDays = String.valueOf(listin.get("remaindDays"));
        String payStatus = String.valueOf(listin.get("order_state"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        //支付方式不为全款支付且订单支付状态不为申请退团、已取消、同步订单、已支付、已支付订金
        if ((proPayMode.equals("2")||proPayMode.equals("3")) && StringUtils.isNotBlank(activationDate) && !"null".equals(activationDate)) {
        	try {
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
			} catch (ParseException e) {
				log.error("日期转换异常", e);
			}
        } else {
        	listin.put("leftDays", "无");
        }
	}
	
	
	/**
	 * 计算订单剩余天数
	 * @param listin
	 * 设为公共静态方法，供其他机票业务调用
	 */
	public static void getOrderLeftTime(Map<String, Object> listin) {
		
		//判断剩余时间用激活时间：新需求（2015-1-7）
        String activationDate = String.valueOf(listin.get("activationDate"));
        String proPayMode = String.valueOf(listin.get("occupyType"));
        String proRemainDays = String.valueOf(listin.get("remaindDays"));
        String payStatus = String.valueOf(listin.get("order_state"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        //支付方式不为全款支付且订单支付状态不为已取消、同步订单、已支付、已支付订金
        if (!proPayMode.equals("3") && StringUtils.isNotBlank(proRemainDays) && 
        		!"null".equals(proRemainDays) &&
        		!Context.ORDER_PAYSTATUS_YQX.equals(payStatus) &&
        		!Context.ORDER_PAYSTATUS_SYNC_CHECK.equals(payStatus) && !Context.ORDER_PAYSTATUS_YZF.equals(payStatus) &&
        		!Context.ORDER_PAYSTATUS_YZFDJ.equals(payStatus) && activationDate != null && !"".equals(activationDate)&& !"null".equals(activationDate)) {
        	try {
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
	private void selectPayOrder(List<Long> listProId, List<Map<String, Object>> listorder, String orderStatus, Model model) {
		
		if (CollectionUtils.isNotEmpty(listProId)) {
            List<Orderpay> orderpayList = orderService.findOrderpayByOrderIds(listProId, 7);
            for (Map<String, Object> map : listorder) {
                List<Orderpay> listTempOrderPay = new ArrayList<Orderpay>();
				for (int i = 0; i < orderpayList.size(); i++) {
                	
                	Orderpay orderpay = orderpayList.get(i);
                	
                    //如果orderpay的订单id  等于  pro的订单id
                    if (orderpay.getOrderId().intValue() == Integer.parseInt(map.get("id").toString())) {
                        listTempOrderPay.add(orderpay);
                        if (StringUtils.isNotBlank(orderpay.getMoneySerialNum())) {
                        	orderService.clearObject(orderpay);
                        	orderpay.setMoneySerialNum(moneyAmountService.getMoneyStr(orderpay.getMoneySerialNum()));
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
			resobj.put("warning", "订单收款状态为空");
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
				resobj.put("warning", "订金已收款");
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
				resobj.put("warning", "全款已收款");
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
					resobj.put("warning", "已收金额大于订单金额，不能再收尾款");
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
						resobj.put("warning", "订单没有收款金额");
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
	 * 根据订单id和订单支付id查询支付凭证
	 * @param payId
	 * @param orderId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="modifypayVoucher/{payId}/{orderId}")
	public String modifypayVoucher(@PathVariable String payId,@PathVariable String orderId, Model model,HttpServletRequest request){
		
		Orderpay orderpay = orderService.findOrderpayById(new Long(payId));
	    AirticketOrder order = airTicketOrderService.getAirticketorderById(Long.parseLong(orderId));
	    
	    //支付订单金额千位符处理
	    if (StringUtils.isNotBlank(orderpay.getMoneySerialNum())) {
	 	    orderService.clearObject(orderpay);
	 	    orderpay.setMoneySerialNum(moneyAmountService.getMoneyStr(orderpay.getMoneySerialNum()));
	    }
	    
	    //订单金额千位符处理
	    if (StringUtils.isNotBlank(order.getTotalMoney())) {
	 	    orderService.clearObject(order);
	 	   order.setTotalMoney(moneyAmountService.getMoneyStr(order.getTotalMoney()));
	    }
	  
		model.addAttribute("orderpay",orderpay);
		model.addAttribute("orderId",orderId);
		model.addAttribute("productorder",order);
	    return "modules/order/airticket/modifypayVoucher";

	}
	
	@RequestMapping(value ="modifypayVoucherFile")
	public String modifypayVoucherFile(@RequestParam(value = "payVoucher", required = false) MultipartFile[] files, HttpServletRequest request, ModelMap model) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
	    ArrayList<DocInfo> infoList = new ArrayList<DocInfo>();
		if(files!=null && files.length>0){
	    	for(int i=0;i<files.length;i++){
	    		MultipartFile file = files[i];
	    		if(file!=null){
	    			DocInfo docInfo =null;
	    	        String fileName = file.getOriginalFilename();
	    	        docInfo = new DocInfo();
//	    	        //保存  
	    	        try {
	    	            String path = FileUtils.uploadFile(file.getInputStream(),fileName);
	    	            docInfo.setDocName(fileName);
	    	            docInfo.setDocPath(path);
	    	            infoList.add(docInfo);
//	    	        //保存附件表数据
	    	        } catch (Exception e) {  
	    	            e.printStackTrace();  
	    	        }
	    		}
	    	}
	    }

		 String remarks = request.getParameter("remarks");
		 String payId = request.getParameter("payId");
		 String orderId = request.getParameter("orderId");
		Orderpay orderpay=this.orderService.findOrderpayById(new Long(payId));
		if(StringUtils.isNotBlank(remarks)){
			 orderpay.setRemarks(remarks);
	    }
		airTicketOrderService.updatepayVoucherFile(infoList, orderpay,orderId,model, request);
		
	    return "modules/order/airticket/uploadVoSuccess";
	   
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
		initQueryCond(model);
		initParam(model);
		
		//2.获取查询参数
		Map<String, Object> conditionsMap = prepareQueryCond(request);
		
		//3.获取用户角色信息
		Map<String, Object> roleMap = prepareRole();
		
		//4.查询列表
		Page<Map<String,Object>> page = airTicketOrderService.queryAirticketOrderListByCond(request,response,UserUtils.getUser(),conditionsMap,roleMap);
		
		List<Map<String, Object>> list = page.getList();
		
//		for(Map<String, Object> temp : list){
//			getOrderLeftTime(temp);
//			//金额处理
//        	handlePrice(temp);
//		}
	
		for(int i=0;i<list.size();i++){
			Map temp = list.get(i);
			getOrderLeftTime2((temp));
			handlePrice(temp);
			list.set(i, temp);
		}
		page.setList(list);
		model.addAttribute("page",page );
//		//计算剩余天数
//				for (int i = 0; i < page.getList().size(); i++) {
//					String createDate = page.getList().get(i).get("createDate")
//							.toString();
//					String remainDays = page.getList().get(i).get("remaindDays")
//							.toString();
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//					try {
//						Date dateDays = sdf.parse(createDate);
//						Calendar cl = Calendar.getInstance();
//						cl.setTime(dateDays);
//						cl.add(Calendar.DATE, Integer.parseInt(remainDays));
//						String datestr = sdf.format(cl.getTime());//创建时间加上保留天数的时间
//						System.out.println(datestr);
//						SimpleDateFormat sdfnow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//						String nowdate = sdfnow.format(new Date()); //系统当前时间
//						DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//						Date dt1 = df.parse(datestr);
//						Date dt2 = df.parse(nowdate);
//						if(dt1.getTime()>=dt2.getTime()){
//							int redays = dt1.getDate()-dt2.getDate();
//							int rehours = dt1.getHours()-dt2.getHours();
//							if(rehours<0){
//								redays -=1 ;
//								rehours += 24;
//							}
//							page.getList().get(i).put("remaindDays", "剩余时间"+redays+"天"+rehours+"小时");
//						}else{
//							page.getList().get(i).put("remaindDays", "无");
//						}
//					} catch (ParseException e) {
//						e.printStackTrace();
//					}
//				}
		model.addAttribute("conditionsMap", conditionsMap);
		
		List<User> users = systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId());
	    model.addAttribute("users",users);

		//5.订单列表页
		return "modules/order/airticketOrderListForOp";
	}
	
	/**
	 * 根据订单id查询订单详情改签
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="airticketOrderDetailChange")
	public String queryChangeOrderDetailById(Model model,HttpServletRequest request,HttpServletResponse response){
		
		String orderId = request.getParameter("orderId");
		Map<String, Object> orderDetailInfoMap = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId);
		
		//获取游客信息并过滤已改签游客
		List<Map<String,Object>> travelerList = (List<Map<String,Object>>)orderDetailInfoMap.get("travelerStrategy");
		//循环游客并删除已改签游客
		if (CollectionUtils.isNotEmpty(travelerList)) {
			Iterator<Map<String,Object>> it = travelerList.iterator();
			while(it.hasNext()) {
				Map<String,Object> traveler = it.next();
				String delFlag = traveler.get("delFlag").toString();
				//如果游客状态为改签则删除（改签游客不能再次改签）
				if (Context.TRAVELER_DELFLAG_PLANEREVIEW.toString().equals(delFlag)) {
					it.remove();
				}
			}
		}
		orderDetailInfoMap.put("travelerStrategy", travelerList);
		
		model.addAttribute("orderDetailInfoMap", orderDetailInfoMap);
		model.addAttribute("operate", UserUtils.getUser().getName());
		
		//----------以下代码为获取出发城市名称
		String fromAreaId = (String) orderDetailInfoMap.get("departureCity");
		String fromArea = "";
		List<Dict> list = areaService.findFromCityList("");
		for(Dict t : list){
			if((t.getId().toString()).equals(fromAreaId)){
				fromArea = t.getLabel();
			}
		}
		model.addAttribute("fromArea",fromArea);//出发城市
		
		return "modules/order/airticketOrderChange";
	}
	
	@RequestMapping(value="airticketApprovalHistoryList")
	public String airticketApprovalHistoryList(Model model,HttpServletRequest request,HttpServletResponse response){
		
//		String orderId = request.getParameter("orderId");
//		Map<String, Object> orderDetailInfoMap = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId);
//		model.addAttribute("orderDetailInfoMap", orderDetailInfoMap);
//		
//		return "modules/order/airticketApprovalHistoryList";
		
		Params params = new Params(request);
		model.addAttribute("deparCity",areaService.findFromCityList(""));
		model.addAttribute("area",areaService.findAirportCityList(""));
		model.addAttribute("params", params);
		model.addAttribute("page", airTicketOrderService.airticketApprovalHistoryList(request, response, params));
		return "modules/airticket/airticketApprovalHistoryList";
		
	}
	
	
	/**
	 * 根据订单id查询订单详情
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="airticketOrderDetail")
	public String queryOrderDetailById(Model model,HttpServletRequest request,HttpServletResponse response){
		
		String orderId = request.getParameter("orderId");
		AirticketOrder airticketOrder = airTicketOrderService.getAirticketorderById(Long.parseLong(orderId));
		model.addAttribute("airticketOrder", airticketOrder);
		// 发票
	    List<Orderinvoice> invoices = orderinvoiceService.findCreatedInvoiceByOrder(Integer.parseInt(airticketOrder.getId().toString()), Context.ORDER_TYPE_JP);
	    model.addAttribute("invoices", invoices);
	    // 收据
	    List<OrderReceipt> receipts = orderreceiptService.findCreatedReceiptByOrder(Integer.parseInt(airticketOrder.getId().toString()), Context.ORDER_TYPE_JP);
	    model.addAttribute("receipts", receipts);
		Map<String, Object> orderDetailInfoMap = airTicketOrderService.queryAirticketOrderDetailInfoByIdAddcontacts(orderId);
		//FIXME HPT~ 机票订单详情修改
			// ============== 订单详情修改 开始
		if(null != orderDetailInfoMap.get("mainOrderId")){
			String mainOrderId = String.valueOf(orderDetailInfoMap.get("mainOrderId")); // 主订单id 如果该订单类型是参团 则录入主订单id
			ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(mainOrderId)); 
			ActivityGroup activityGroup = activityGroupService.findById(productOrder.getProductGroupId()); 
			model.addAttribute("productOrder", productOrder);
			model.addAttribute("activityGroup", activityGroup);
		}	
		List<OrderContacts> orderContacts = orderContactsService.findOrderContactsByOrderIdAndOrderType(Long.parseLong(orderId), Context.ORDER_TYPE_JP);
		if (CollectionUtils.isEmpty(orderContacts)) {
			orderContacts.add(new OrderContacts());
		}
		model.addAttribute("orderContacts", orderContacts);  //保存的是最原始的联系人
		model.addAttribute("from_Areas",areaService.findFromCityList(""));//出发城市
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
			// ============== 订单详情修改 结束
		//by sy  2015.7.15
		if(null != orderDetailInfoMap.get("mainOrderId")){
			String mainorderId = orderDetailInfoMap.get("mainOrderId").toString();
			String groupNum = activityAirTicketService.getActivitygroupById(new Long(mainorderId));
			ProductOrder  porder=	activityAirTicketService.getProductById(new Long(mainorderId));
			model.addAttribute("porder", porder);
			model.addAttribute("groupNum", groupNum);
		}
		model.addAttribute("orderDetailInfoMap", orderDetailInfoMap);
		model.addAttribute("operate", UserUtils.getUser().getName());
	    String reviewId = request.getParameter("reviewId");
	    if(reviewId!=null&&!"".equals(reviewId)){
	    	List<ReviewLog> rLog=reviewService.findReviewLog(Long.parseLong(reviewId));
			model.addAttribute("rLog",rLog);
	    }
		if(!StringUtils.isBlank(reviewId)){
			Map<String, Long> condition = new HashMap<String, Long>();
			condition.put("reviewId", Long.parseLong(reviewId));
			Map<String, Object> reviewInfoMap = activityGroupService.reviewLogs(condition);
			model.addAllAttributes(reviewInfoMap);
		}
		
		return "modules/order/airticketOrderDetail";
	}
	
	/**
	 * 根据订单id修改订单-销售
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="airticketOrderSaleModify")
	public String modifySaleOrderById(Model model,HttpServletRequest request,HttpServletResponse response){
		
		//1.页面的一些初始化
		initParam(model);
		
		//2.查询订单内容
		String orderId = request.getParameter("orderId");
		Map<String, Object> orderDetailInfoMap = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId);
		
		//3.获取用户角色信息
		List<Role> roleInfoList = UserUtils.getUser().getRoleList();
		
		Map<String,Object> roleMap = new HashMap<String,Object>();
		if(roleInfoList!=null && roleInfoList.size()>0){
			for(Role roleInfo : roleInfoList){
				roleMap.put(roleInfo.getRoleType(), roleInfo);
			}
		}
		String groupNum = activityAirTicketService.getActivitygroupById(new Long(orderId));
		ProductOrder  porder=	activityAirTicketService.getProductById(new Long(orderId));
		model.addAttribute("porder", porder);
		model.addAttribute("groupNum", groupNum);
		model.addAttribute("orderDetailInfoMap", orderDetailInfoMap);
		model.addAttribute("from_Areas",areaService.findFromCityList(""));//出发城市
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
		return "modules/order/airticketOrderModifySale";
	}
	
	/**
	 * 根据订单id修改订单-计调
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="airticketOrderOpModify")
	public String modifyOpOrderById(Model model,HttpServletRequest request,HttpServletResponse response){
		
		//1.页面的一些初始化
		initParam(model);
		
		//2.查询订单内容
		String orderId = request.getParameter("orderId");
		Map<String, Object> orderDetailInfoMap = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId);
		
		//3.获取用户角色信息
		List<Role> roleInfoList = UserUtils.getUser().getRoleList();
		
		Map<String,Object> roleMap = new HashMap<String,Object>();
		if(roleInfoList!=null && roleInfoList.size()>0){
			for(Role roleInfo : roleInfoList){
				roleMap.put(roleInfo.getRoleType(), roleInfo);
			}
		}
		String groupNum = activityAirTicketService.getActivitygroupById(new Long(orderId));
		ProductOrder  porder=	activityAirTicketService.getProductById(new Long(orderId));
		model.addAttribute("porder", porder);
		model.addAttribute("groupNum", groupNum);
		model.addAttribute("orderDetailInfoMap", orderDetailInfoMap);
		model.addAttribute("from_Areas",areaService.findFromCityList(""));//出发城市
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
		return "modules/order/airticketOrderModifyOp";
	}
	
	/**
	 * 前往机票上传确认单
	 * @author yang.jiang 2016-1-7 14:21:23
	 * @param orderId
	 * @param orderConfirmation
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("toUploadConfirmation")
	public String toUploadConfirmation(Model model, HttpServletRequest request, HttpServletResponse response){		
		String orderId = request.getParameter("orderId");
		AirticketOrder order = airTicketOrderService.getAirticketorderById(Long.parseLong(orderId));
		model.addAttribute("orderId",orderId);
		model.addAttribute("productorder",order);
	    return "modules/order/airticket/uploadConfirmation4Air";
	}
		
	/**
	 * 上传确认单
	 * @author yang.jiang 2016-1-7 14:21:35
	 * @param orderId
	 * @param orderType
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="uploadConfirmation")
	@ResponseBody
	public Object uploadConfirmation(HttpServletRequest request) throws Exception {
		net.sf.json.JSONObject resobj = new net.sf.json.JSONObject();
		String orderId = request.getParameter("orderId");
		String docFileIds = request.getParameter("docFileIds");
		if( !StringUtils.isBlank(docFileIds)&& !StringUtils.isBlank(orderId)) {
			AirticketOrder order = airTicketOrderService.getAirticketorderById(Long.parseLong(orderId));
	        try {
	        	//保存文件
	        	//保存文件
	        	String[] docFileArray = ((order.getConfirmationFileId()==null?"":order.getConfirmationFileId())+docFileIds)==null?null:((order.getConfirmationFileId()==null?"":order.getConfirmationFileId())+docFileIds).replace(",", " ").trim().split(" ");
	        	List<String> docFileList = Arrays.asList(docFileArray);
	        	Collections.sort(docFileList);
	        	docFileIds ="";
	    		for(String docId:docFileList){
	    			docFileIds = docFileIds +docId+",";
	    		}
	    		order.setConfirmationFileId(docFileIds);
	    		airTicketOrderService.saveOrder(order);
	    		//添加操作日志
	    		orderService.saveLogOperate(Context.log_type_orderform,
	    				Context.log_type_orderform_name, "订单" + orderId + "上传确认单成功", "2", Context.ORDER_TYPE_JP, order.getId());
	    		resobj.put("success", "上传确认单成功");
	        } catch (Exception e) {
	        	//添加操作日志
	        	orderService.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name,
	    				"订单" + request.getParameter("orderId") + "上传确认单失败", "2", Context.ORDER_TYPE_JP, order.getId());
	            e.printStackTrace();  
	            resobj.put("error", "上传确认单失败");
	        }
		}
		 return resobj;
	}
	
	
	@RequestMapping(value ="downloadConfirmFiles")
	public String downloadConfirmFiles(HttpServletRequest request,Model model){
		String orderId = request.getParameter("orderId");
		if(!StringUtils.isBlank(orderId)) {
			AirticketOrder order = airTicketOrderService.getAirticketorderById(Long.parseLong(orderId));
    		String docFileIds = order.getConfirmationFileId();
    		if(!StringUtils.isBlank(docFileIds)) {
	    		String[] fileIds = docFileIds.replace(",", " ").trim().split(" ");
	    		List<DocInfo> fileList = new ArrayList<DocInfo>();
	    		for(String fileId:fileIds){
	    			fileList.add(docInfoService.getDocInfo(new Long(fileId)));
	    		}
	    		model.addAttribute("docList", fileList);
	    		model.addAttribute("orderId", orderId);
	    		model.addAttribute("downloadUrl", "order/manage/userDownloadConfirmFile");
	    		model.addAttribute("delUrl", "order/manage/deleteDoc");
    		}else{
    			//文件不存在
    			return "";
    		}
		}
	    return "include/downloadFiles";
	}
	
	@RequestMapping(value ="deleteDoc")
	@ResponseBody
	public Object deleteDoc(HttpServletRequest request) throws Exception {
		net.sf.json.JSONObject resobj = new net.sf.json.JSONObject();
		String orderId = request.getParameter("orderId");
		String docId = request.getParameter("docId");
		if( !StringUtils.isBlank(docId)&& !StringUtils.isBlank(orderId)) {
			AirticketOrder order = airTicketOrderService.getAirticketorderById(Long.parseLong(orderId));
	        try {
	        	DocInfo doc = docInfoService.getDocInfo(new Long(docId));
	        	if(doc.getCreateBy().getId().longValue() == UserUtils.getUser().getId().longValue()){
	        		String docIds = order.getConfirmationFileId();
	        		String downloadIds = order.getDownloadFileIds();
	        		docIds = docIds.replace(docId+",", "");
	        		order.setConfirmationFileId(docIds);
	        		if(!StringUtils.isBlank(downloadIds)){
	        			downloadIds = downloadIds.replace(docId+",", "");
	        			order.setDownloadFileIds(downloadIds);
	        		}
	        		airTicketOrderService.saveOrder(order);
	        		
	        		docInfoService.delDocInfoById(new Long(docId));
	        		//添加操作日志
	        		orderService.saveLogOperate(Context.log_type_orderform,
		    				Context.log_type_orderform_name, "订单" + orderId + "上传确认单成功", "2", Context.ORDER_TYPE_JP, order.getId());
	        		resobj.put("success", "ok");
	        	}else{
	        		resobj.put("error", "删除确认单失败");
	        	}
	        	//保存文件
	        } catch (Exception e) {
	        	//添加操作日志
	        	orderService.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name,
	    				"订单" + request.getParameter("orderId") + "上传确认单失败", "2", Context.ORDER_TYPE_JP, order.getId());
	            e.printStackTrace();  
	    		resobj.put("error", "删除确认单失败");
	        }
		}
	    return resobj;
	}
	
	@RequestMapping(value ="zipconfirmdownload/{orderIds}")
	public String zipconfirmdownload(@PathVariable("orderIds")String orderIds,HttpServletResponse response){
		String[] orderIdArray = orderIds.replace(",", " ").trim().split(" ");
		String docid = "";
		for(String str:orderIdArray){
			AirticketOrder order = airTicketOrderService.getAirticketorderById(Long.parseLong(str));
			docid = docid + (order.getConfirmationFileId()==null?"":order.getConfirmationFileId());
			order.setDownloadFileIds(order.getConfirmationFileId());
			airTicketOrderService.saveOrder(order);
		}
	    return "redirect:" + Global.getAdminPath() + "/sys/docinfo/zipdownload/" + docid+"/confirm";
	}
	
	@RequestMapping(value ="userDownloadConfirmFile/{orderId}/{docid}")
	public String userDownloadConfirmFile(@PathVariable("docid") Long docid,@PathVariable("orderId") Long orderId,HttpServletResponse response) {
		//
		AirticketOrder order = airTicketOrderService.getAirticketorderById(orderId);
		String downloadIds = order.getDownloadFileIds();
		if(!StringUtils.isBlank(downloadIds)){
			downloadIds = downloadIds.replace(docid+",", "");
			String[] docIdArray = (downloadIds+docid+",").replace(",", " ").trim().split(" ");
			List<String> docIdList =Arrays.asList(docIdArray);
			Collections.sort(docIdList);
			String downloadIdsString = "";
			for(String downLoadId:docIdList){
				downloadIdsString = downloadIdsString+downLoadId+",";
			}
			order.setDownloadFileIds(downloadIdsString);
		}else{
			order.setDownloadFileIds(docid+",");
		}
		airTicketOrderService.saveOrder(order);
		return "redirect:" + Global.getAdminPath() + "/sys/docinfo/download/" + docid;
	}
	
	/**
	 * ajax修改游客
	 * @param orderTravelAjax
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="airticketOrderTravel")
	public void modifyAirticketOrderTraveler(OrderTravelAjax orderTravelAjax,HttpServletRequest request,HttpServletResponse response){
		try{
			
			response.setContentType("application/json");
			PrintWriter writer = response.getWriter();
			
			//添加游客
			if(StringUtils.isEmpty(orderTravelAjax.getTravelId())){
				
				boolean flag = airTicketOrderService.saveAirticketOrderTravel(orderTravelAjax);
				if(flag){//成功 取游客id回填到页面
					
					String travelId = airTicketOrderService.queryAirticketOrderTravelMaxId(orderTravelAjax);
					
					writer.write("{\"result\":\"success\",\"msg\":\""+travelId+"\"}");
				}else{//失败
					writer.write("{\"result\":\"failed\",\"msg\":\"\"}");
				}
			}
			//更改游客
			else{
				boolean flag = airTicketOrderService.updateAirticketOrderTravel(orderTravelAjax);
				if(flag){//成功
					writer.write("{\"result\":\"success\",\"msg\":\"\"}");
				}else{//失败
					writer.write("{\"result\":\"failed\",\"msg\":\"\"}");
				}
			}
			
			
			
			
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * ajax改变渠道，拉取该渠道信息
	 * @param agentId
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="airticketOrderChangeAgent")
	public void changeirticketOrderAgent(String agentId,HttpServletRequest request,HttpServletResponse response){
		
		try{
			
			Map<String, Object> agentInfoMap = airTicketOrderService.queryAirticketOrderAgent(agentId);
			JSONObject jsonObject = JSONObject.fromObject(agentInfoMap);
			String json = jsonObject.toString();
			response.setContentType("application/json");
			PrintWriter writer = response.getWriter();
			writer.write(json);
			
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * Ajax修改渠道信息
	 * @param orderAgentAjax
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="airticketOrderAgent")
	public void modifyAirticketOrderAgent(OrderAgentAjax orderAgentAjax,HttpServletRequest request,HttpServletResponse response){
		try{
			
			boolean flag = airTicketOrderService.updateAirticketOrderAgent(orderAgentAjax);
			response.setContentType("application/json");
			PrintWriter writer = response.getWriter();
			
			if(flag){//成功
				writer.write("{\"result\":\"success\",\"msg\":\"\"}");
			}else{//失败
				writer.write("{\"result\":\"failed\",\"msg\":\"\"}");
			}
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 判断产品是否有对应的游客
	 * @param activityId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="existExportData",method=RequestMethod.POST)
	public Object existExportData(@RequestParam(value="activityId") String activityId) {
		List<Map<String, Object>> travelerList = Lists.newArrayList();
		JSONArray results = new JSONArray();
		JSONObject resobj = new JSONObject();
		if (StringUtils.isNotBlank(activityId)) {
			List<Map<String, Object>> list = airTicketOrderService.queryAirticketOrdersByProductId(activityId);
			if (CollectionUtils.isNotEmpty(list)) {
				for(Map<String, Object> map : list) {
					List<Map<String, Object>> tempList = airTicketOrderService.queryAirticketOrderTravels(map.get("orderId").toString());
					if (CollectionUtils.isNotEmpty(tempList)) {
						travelerList.addAll(tempList);
					}
				}
				if (travelerList.size() <= 0) {
					resobj.put("flag", "false");
					resobj.put("warning", "此产品没有对应的游客信息");
				} else {
					resobj.put("flag", "true");
				}
			} else {
				resobj.put("flag", "false");
				resobj.put("warning", "此产品没有对应的订单");
			}
		} else {
			resobj.put("flag", "false");
			resobj.put("warning", "没有产品ID");
		}
		results.add(resobj);
		return results;
	}
	
	/**
	 * 导出产品对应游客
	 * @param activityId 机票产品ID
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="exportExcel",method=RequestMethod.POST)
	public void exportExcel(@RequestParam(value="activityId") String activityId, HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> list = null; //产品对应的订单List
		List<Map<String, Object>> travelerList = Lists.newArrayList(); //订单对应游客List
		List<Object[]> exportList = Lists.newArrayList(); //导出游客List
		try {
			
			//根据机票产品ID查询所有订单
			if (StringUtils.isNotBlank(activityId)) {
				list = airTicketOrderService.queryAirticketOrdersByProductId(activityId);
			}
			
			//根据订单查询游客并放到导出List
			if (CollectionUtils.isNotEmpty(list)) {
				if (CollectionUtils.isNotEmpty(list)) {
					for (Map<String, Object> map : list) {
						List<Map<String, Object>> tempList = airTicketOrderService.queryAirticketOrderTravels(map.get("orderId").toString());
						if (CollectionUtils.isNotEmpty(tempList)) {
							travelerList.addAll(tempList);
						}
					}
					
					//获取游客信息并放入到导出游客信息List
					for (Map<String,Object> travelInfo : travelerList) {
						String travelName = (String) travelInfo.get("travelName");
						String nameSpell = (String) travelInfo.get("nameSpell");
						Object travelSex = travelInfo.get("travelSex");
						travelSex = travelSex+"";
						Object travelBirthDay = travelInfo.get("travelBirthDay") == null ? "" : travelInfo.get("travelBirthDay");
						String travelIdCard = (String) travelInfo.get("travelIdCard");
						String passportCode = (String) travelInfo.get("passportCode");
						Object passportValidity = travelInfo.get("passportValidity") == null ? "" : travelInfo.get("passportValidity");
						String remark = (String) travelInfo.get("remark");
						String telephone = (String) travelInfo.get("telephone");
						
						Object[] obj = new Object[]{travelName,nameSpell,travelSex.equals("1")?"男":"女",travelBirthDay,travelIdCard,passportCode,passportValidity,telephone,remark};
						
						exportList.add(obj);
					}
				}
			}
			
			//文件名称
			String fileName = "游客信息";
			//Excel各行名称
			String[] cellTitle =  {"中文姓名","英文姓名","性别","出生日期","身份证号","护照号","护照有效期","手机","备注"};
			//文件首行标题
			String firstTitle = "游客信息";
			ExportExcel.createExcle(fileName, exportList, cellTitle, firstTitle, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 导出游客信息
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="airticketOrderTravelExport")
	public void exportAllTravelerInfo(HttpServletRequest request,HttpServletResponse response){
		
		try {
			String orderId = request.getParameter("orderId");
			
			//1.查询游客信息
			List<Map<String,Object>> travelInfoList = airTicketOrderService.queryAirticketOrderTravels(orderId);
			List<Object[]> travelerList = new ArrayList<Object[]>();
			
			for(Map<String,Object> travelInfo :travelInfoList){
				
				String travelName = (String) travelInfo.get("travelName");
				if (travelName == null) {
					travelName = "";
				}
				String nameSpell = (String) travelInfo.get("nameSpell");
				if (nameSpell == null) {
					nameSpell = "";
				}
				Object travelSex = travelInfo.get("travelSex");
				travelSex = travelSex+"";
				Object travelBirthDay = travelInfo.get("travelBirthDay") == null ? "" : travelInfo.get("travelBirthDay");
//				travelBirthDay = travelBirthDay+"";
				String travelIdCard = (String) travelInfo.get("travelIdCard");
				if (travelIdCard == null) {
					travelIdCard = "";
				}
				String passportCode = (String) travelInfo.get("passportCode");
				if (passportCode == null) {
					passportCode = "";
				}
				Object passportValidity = travelInfo.get("passportValidity") == null ? "" : travelInfo.get("passportValidity");
//				passportValidity = passportValidity+"";
				String remark = (String) travelInfo.get("remark");
				if (remark == null) {
					remark = "";
				}
				String telephone = (String) travelInfo.get("telephone");
				if (telephone == null) {
					telephone = "";
				}
				
				Object[] obj = new Object[]{travelName,nameSpell,travelSex.equals("1")?"男":"女",travelBirthDay,travelIdCard,passportCode,passportValidity,telephone,remark};
				
				travelerList.add(obj);
			}
			
			//2.导出游客信息
			//文件名称
			String fileName = "游客信息";
			
			//Excel各行名称
			String[] cellTitle =  {"中文姓名","英文姓名","性别","出生日期","身份证号","护照号","护照有效期","手机","备注"};
			
			//文件首行标题
			String firstTitle = "游客信息";
			
			ExportExcel.createExcle(fileName, travelerList, cellTitle, firstTitle, request, response);
		} catch (Exception e) {
			log.error("导出发生错误");
			e.printStackTrace();
		}
	}
	
	/**
	 * 导出产品对应出票名单
	 * @param activityId 机票产品ID
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="exportAirticketNameExcel",method=RequestMethod.POST)
	public void exportAirticketNameExcel(@RequestParam(value="activityId") String activityId, HttpServletRequest request, HttpServletResponse response) {
		List<Map<String, Object>> list = null; //产品对应的订单List
		List<Map<String, Object>> travelerList = Lists.newArrayList(); //订单对应游客List
		List<Object[]> exportList = Lists.newArrayList(); //导出游客List
		try {
			
			//根据机票产品ID查询所有订单
			if (StringUtils.isNotBlank(activityId)) {
				list = airTicketOrderService.queryAirticketOrdersByProductId(activityId);
			}
			
			//根据订单查询游客并放到导出List
			if (CollectionUtils.isNotEmpty(list)) {
				if (CollectionUtils.isNotEmpty(list)) {
					for (Map<String, Object> map : list) {
						List<Map<String, Object>> tempList = airTicketOrderService.queryAirticketOrderTravels(map.get("orderId").toString());
						if (CollectionUtils.isNotEmpty(tempList)) {
							travelerList.addAll(tempList);
						}
					}
					
					//获取游客信息并放入到导出游客信息List
					for (int i = 0; i < travelerList.size(); i++) {
						Map<String,Object> travelInfo = travelerList.get(i);
						String travelName = (String) travelInfo.get("travelName");
						String nameSpell = (String) travelInfo.get("nameSpell");
						Object travelSex = travelInfo.get("travelSex");
						travelSex = travelSex+"";
						Object travelBirthDay = travelInfo.get("travelBirthDay") == null ? "" : travelInfo.get("travelBirthDay");
						String travelIdCard = (String) travelInfo.get("travelIdCard");
						String passportCode = (String) travelInfo.get("passportCode");
						Object passportValidity = travelInfo.get("passportValidity") == null ? "" : travelInfo.get("passportValidity");
						String telephone = (String) travelInfo.get("telephone");
						Object[] obj = new Object[]{i+1,travelName,nameSpell,travelSex.equals("1")?"男":"女",travelBirthDay,travelIdCard,"","",passportCode,passportValidity,telephone,""};
						exportList.add(obj);
					}
				}
			}
			
			//文件名称
			String fileName = "出票名单";
			//Excel各行名称
			String[] cellTitle =  {"序号","中文姓名","英文姓名","性别","出生日期","身份证号","分房","负责","护照号","护照有效期","手机","护照"};
			//文件首行标题
			String firstTitle = "出票名单";
			ExportExcel.createExcle(fileName, exportList, cellTitle, firstTitle, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 导出出票名单
	 * @param request
	 * @param response
	 */
	@RequestMapping(value="airticketOrderNameList")
	public void exportAirticketNameList(HttpServletRequest request,HttpServletResponse response){
		
		try {
			String orderId = request.getParameter("orderId");
			
			//1.查询游客信息
			List<Map<String,Object>> travelInfoList = airTicketOrderService.queryAirticketOrderTravels(orderId);
			List<Object[]> travelerList = new ArrayList<Object[]>();
			
			if(travelInfoList != null && travelInfoList.size()>0){
				
				for(int i=0;i<travelInfoList.size();i++){
					
					Map<String,Object> travelInfo = travelInfoList.get(i);
					
					String travelName = (String) travelInfo.get("travelName");
					String nameSpell = (String) travelInfo.get("nameSpell");
					Object travelSex = travelInfo.get("travelSex");
					travelSex = travelSex+"";
					Object travelBirthDay = travelInfo.get("travelBirthDay") == null ? "" : travelInfo.get("travelBirthDay");
//					travelBirthDay = travelBirthDay+"";
					String travelIdCard = (String) travelInfo.get("travelIdCard");
					String passportCode = (String) travelInfo.get("passportCode");
					Object passportValidity = travelInfo.get("passportValidity") == null ? "" : travelInfo.get("passportValidity");
//					passportValidity = passportValidity+"";
					String telephone = (String) travelInfo.get("telephone");
					
					Object[] obj = new Object[]{
							i+1, 
							travelName == null?"":travelName, 
							nameSpell == null?"":nameSpell,
							travelSex.equals("1")?"男":"女",
							travelBirthDay == null?"":travelBirthDay,
							travelIdCard == null?"":travelIdCard,
							"",
							"",
							passportCode == null?"":passportCode,
							passportValidity == null?"":passportValidity,
							telephone == null?"":telephone,
							""
					};
					
					travelerList.add(obj);
				}
			}
			
			//2.导出游客信息
			//文件名称
			String fileName = "出票名单";
			
			//Excel各行名称
			String[] cellTitle =  {"序号","中文姓名","英文姓名","性别","出生日期","身份证号","分房","负责","护照号","护照有效期","手机","护照"};
			
			//文件首行标题
			String firstTitle = "出票名单";
			
			ExportExcel.createExcle(fileName, travelerList, cellTitle, firstTitle, request, response);
		} catch (Exception e) {
			log.error("导出发生错误");
			e.printStackTrace();
		}
	}
	
	@RequestMapping("airticketOrderFlightRemark")
	public void modifyAirticketOrderFlightRemark(OrderAgentAjax orderAgentAjax,HttpServletRequest request,HttpServletResponse response){
		try{
			
			boolean flag = airTicketOrderService.updateAirticketOrderFlightRemark(orderAgentAjax);
			response.setContentType("application/json");
			PrintWriter writer = response.getWriter();
			if(flag){//成功
				writer.write("{\"result\":\"success\",\"msg\":\"\"}");
			}else{//失败
				writer.write("{\"result\":\"failed\",\"msg\":\"\"}");
			}
			
		}catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void initParam(Model model) {
		AgentinfoService agentinfoService = SpringContextHolder.getBean("agentinfoService");
		List<Agentinfo> agentinfoList = agentinfoService.findAllAgentinfo();
		Agentinfo agentinfo = new Agentinfo();
		agentinfo.setId(-1L);
		agentinfo.setAgentName("非签约渠道");
		agentinfoList.add(0,agentinfo);
		model.addAttribute("agentinfoList", agentinfoList);
		List<Country> countryList = CountryUtils.getCountrys();
		model.addAttribute("countryList", countryList);
		model.addAttribute("isNeedNoticeOrder", UserUtils.getUser().getCompany().getIsNeedAttention());
	}
	
	private Map<String,Object> prepareRole(){
		
		User userInfo = UserUtils.getUser();
		List<Role> roleList = userInfo.getRoleList();
		
		Map<String, Object> roleMap = new HashMap<String,Object>();
		for(Role role : roleList){
			roleMap.put(role.getRoleType(), role.getDepartment());
		}
		return roleMap;
	}

	private Map<String, Object> prepareQueryCond(HttpServletRequest request) {
		
		Map<String,Object> conditionsMap = new HashMap<String,Object>();
		
		String showType = request.getParameter("showType");//机票订单状态
		String orderNumOrOrderGroupCode = request.getParameter("orderNumOrOrderGroupCode");
		String airType = request.getParameter("airType");//机票类型 1-多段 2-往返 3-单程
		String ticketType = request.getParameter("ticketType");//机票区域类型 1-内陆+国际 2-国际 3-国内
		String fromAreaId = request.getParameter("fromAreaId");//出发地
		String contact = request.getParameter("contact");//联系人
		
		String targetAreaId = request.getParameter("targetAreaIdList");//目的地ID
		String targetAreaName = request.getParameter("targetAreaNameList");//目的地名称
		
		String saler = request.getParameter("saler");//销售
		String picker = request.getParameter("picker");//下单人//add by jyang
		String op = request.getParameter("op");//产品的发布人（计调）
		String agentId = request.getParameter("agentId");//渠道
		String orderCreateDateSort  = request.getParameter("orderCreateDateSort");//订单创建日期排序标识
		String orderUpdateDateSort = request.getParameter("orderUpdateDateSort");//订单更新日期排序标识
		String startFlightDateSort = request.getParameter("startFlightDateSort");//起飞日期排序标识
		String arrivalFlightDateSort = request.getParameter("arrivalFlightDateSort");//到达日期排序标识
		
		String orderCreateDateCss  = request.getParameter("orderCreateDateCss");//订单创建日期排序标识
		String orderUpdateDateCss = request.getParameter("orderUpdateDateCss");//订单更新日期排序标识
		String startFlightDateCss = request.getParameter("startFlightDateCss");//起飞日期排序标识
		String arrivalFlightDateCss = request.getParameter("arrivalFlightDateCss");//到达日期排序标识
		String showChooseSelect = request.getParameter("showChooseSelect");//展开筛选标示
		
		String startAirTime = request.getParameter("startAirTime");//出发日期1 1-->2
		String endAirTime = request.getParameter("endAirTime");//出发日期2
		
		String returnStartAirTime = request.getParameter("returnStartAirTime");//返回日期1 1-->2
		String returnEndAirTime = request.getParameter("returnEndAirTime");//返回日期2
		
		String invoiceStatus = request.getParameter("invoiceStatus");//发票状态
		String receiptStatus = request.getParameter("receiptStatus");//收据状态		
		
		String sortNum = request.getParameter("sortNum");//当前排序
		if(sortNum == null || "".equals(sortNum.trim())){//默认按创建时间排序
			sortNum = "0";
		}
		conditionsMap.put("sortNum", sortNum);
		
		conditionsMap.put("startAirTime", startAirTime);
		conditionsMap.put("endAirTime", endAirTime);
		conditionsMap.put("returnStartAirTime", returnStartAirTime);
		conditionsMap.put("returnEndAirTime", returnEndAirTime);
		
		conditionsMap.put("showType", showType);
		conditionsMap.put("orderNumOrOrderGroupCode", orderNumOrOrderGroupCode);
		conditionsMap.put("airType", airType);
		conditionsMap.put("ticketType", ticketType);
		conditionsMap.put("fromAreaId", fromAreaId);
		conditionsMap.put("targetAreaId", targetAreaId);
		conditionsMap.put("targetAreaName", targetAreaName);
		conditionsMap.put("contact", contact);
		conditionsMap.put("picker", picker);//下单人
		conditionsMap.put("saler", saler);//销售
		conditionsMap.put("op",op);//计调、发布人
		conditionsMap.put("agentId", agentId);
		if("0".equals(sortNum)){
			conditionsMap.put("orderCreateDateSort", orderCreateDateSort);
			conditionsMap.put("orderCreateDateCss", orderCreateDateCss);
		} else if("1".equals(sortNum)){
			conditionsMap.put("orderUpdateDateSort", orderUpdateDateSort);
			conditionsMap.put("orderUpdateDateCss", orderUpdateDateCss);
		} else if("2".equals(sortNum)){
			conditionsMap.put("startFlightDateSort", startFlightDateSort);
			conditionsMap.put("startFlightDateCss", startFlightDateCss);
		} else if("3".equals(sortNum)){
			conditionsMap.put("arrivalFlightDateSort", arrivalFlightDateSort);
			conditionsMap.put("arrivalFlightDateCss", arrivalFlightDateCss);
		}
		conditionsMap.put("showChooseSelect", showChooseSelect);
		if(orderCreateDateSort == null && orderUpdateDateSort == null && startFlightDateSort == null && arrivalFlightDateSort == null){
			conditionsMap.put("orderCreateDateSort", "desc");
			conditionsMap.put("orderCreateDateCss", "activitylist_paixu_moren");
		}
		conditionsMap.put("invoiceStatus",invoiceStatus);//发票
		conditionsMap.put("receiptStatus", receiptStatus);//收据
		
		return conditionsMap;
	}

	private void initQueryCond(Model model) {
		Map<String, String> fromAreasMap = DictUtils.findUserDict(UserUtils.getUser().getCompany().getId(), "fromarea");
		model.addAttribute("fromAreasMap", fromAreasMap);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@SuppressWarnings("unused")
    @RequestMapping(value="airticketOrderAppDetail")
	public String airticketOrderAppDetail(Model model,HttpServletRequest request,HttpServletResponse response){
		
		String orderId = request.getParameter("orderId");
		String trvalerId = request.getParameter("trvalerId");
		Map<String, Object> orderDetailInfoMap = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId);
		model.addAttribute("orderDetailInfoMap", orderDetailInfoMap);
		
		return "modules/order/airticketOrderChange";
	}
	
	
	
	private void printJSON(String json, HttpServletResponse response)
			throws Exception {
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(json);
	}
	@SuppressWarnings("all")
	@RequestMapping(value="cancelApp")
	public void cancelApp(Model model,HttpServletRequest request,HttpServletResponse response) throws Exception{
		Params p = new Params(request);
		ReviewService rs = SpringContextHolder.getBean("reviewService");
		Review r = rs.findReviewInfo(Long.valueOf(p.get("reviewId").toString()));
//		r.setStatus(new Integer(4));
		 Map jsonMap = new HashMap();
		try{
//			rs.updateRivew(r);
			rs.removeReview(Long.valueOf(p.get("reviewId").toString()));
			jsonMap.put("result", "操作成功");
		}catch(Exception e){
			e.printStackTrace();
			jsonMap.put("html", "操作失败");
		}
		 String data = JSONObject.fromObject(jsonMap).toString();
		 this.printJSON(data, response);
	}
	
	@SuppressWarnings("all")
	@RequestMapping(value="getAutoCompelte")
	public void getAutoCompelte(Model model,HttpServletRequest request,HttpServletResponse response) throws Exception{
		String codition=new String(request.getParameter("ac").getBytes("ISO-8859-1"),"UTF-8").trim(); 
		AreaService areaService = SpringContextHolder.getBean("areaService");
		
		Map map = new HashMap();
		map.put("areaName", codition);
		List<Map> list = areaService.findAirportCityList2(map);
		try{
			String data = JSONArray.fromObject(list).toString();

			this.printJSON(data, response);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		 

	}
	
	
	
	
	@SuppressWarnings("all")
	@RequestMapping(value="ajaxQueryProduct")
	public void ajaxQueryProduct(Model model,HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		String product_code = request.getParameter("product_code");
		// 增加前后空格处理
		if(StringUtils.isNotBlank(product_code)){
			product_code = product_code.trim();
		}
		List<Map<String, Object>> list = airTicketOrderService.queryAirtickeByProcode(product_code);
		//from_area出发城市 
		//DictUtils.get
		String html="";
		
		html+=" <table class='table table-striped table-bordered'>";
		html+="<thead><tr><th>机票类型</th><th>出发城市</th><th>到达城市</th><th>同行价</th><th>机位余位</th><th>订金时限/取消时限</th> <th>下载资料</th></tr> </thead> ";
		html+="<tbody><tr>";
            
//                <td>单程</td>
//                <td>北京</td>
//                <td>拉斯维加斯</td>
//                <td class="tr">¥<span class="tdred fbold">1,300</span>起</td>
//                <td class="tr">100</td>
//                <td class="p0">
//                 <div class="out-date">2014-12-11</div>
//                 <div class="close-date">2014-12-08</div>
//             </td>
//                <td class="tc tda"><a>相关资料</a></td>
//				</tr>
			           
	  Map jsonMap = new HashMap();
	  List<Dict> from_areas=DictUtils.getDictList("from_area");
	  AreaService areaService = SpringContextHolder.getBean("areaService");
	  List<Area> areas = areaService.findAll();
		if(list!=null&&list.size()>0){
			String airType="";
			String fromCity="";
			String toCity="";
			for(Map<String, Object> map:list){
				switch(map.get("airType").toString()){
					case "1":airType="多段";break; 
					case "2":airType="往返";break; 
					case "3":airType="单程";break; 
				}
				DictUtils.getDictList("from_area");
				html+="<td>"+airType+"</td>";
				for(Dict d:from_areas){
					if(d.getValue().equals(map.get("departureCity"))){
						fromCity=d.getLabel();
						break;
					}
				}
				for(Area area:areas){
					if(area.getId().toString().equals(map.get("arrivedCity").toString())){
						toCity=area.getName();
						break;
					}
				}
				if(map.get("depositTime")==null){
					map.put("depositTime", "");
				}
				if(map.get("cancelTimeLimit")==null){
					map.put("cancelTimeLimit", "");
				}
				html+=" <td>"+fromCity+"</td>";
				html+=" <td>"+toCity+"</td>";
				CurrencyService cs = SpringContextHolder.getBean("currencyService");
				Currency c = cs.findCurrency(Long.valueOf(map.get("currency_id").toString()));
				html+="<td class='tr'>"+c.getCurrencyMark()+"<span class=\"tdred fbold\">"+map.get("settlementAdultPrice")+"</span>起</td>";
				html+="<td class=\"tr\">"+map.get("freePosition")+"</td>";
				html+="<td class='p0'><div class='out-date'>"+map.get("depositTime")+"</div>";
				html+="<div class='close-date'>"+map.get("cancelTimeLimit")+"</div></td>";
				html+="<td class=\"tc tda\"><a>相关资料</a></td></tr>";
				html+="</tbody></table>";
				html+="<input type='hidden' value="+map.get("id")+"</input>";
			}
			String newProId = list.get(0).get("id").toString();
			
			jsonMap.put("html", html);
			jsonMap.put("freePosition", list.get(0).get("freePosition"));
			jsonMap.put("newProId", newProId);
			String data = JSONObject.fromObject(jsonMap).toString();
		 	this.printJSON(data, response);
		}
		if(list.size()==0){
			jsonMap.put("noPoduct", "0");
			String noPoduct = JSONObject.fromObject(jsonMap).toString();
			this.printJSON(noPoduct, response);
		}
		this.printJSON("", response);
		
	}
	
	
	@SuppressWarnings("all")
	@RequestMapping(value="areaGaiQian")
	public String areaGaiQian(Model model,HttpServletRequest request,HttpServletResponse response) throws Exception{
	
		String travelerIds = request.getParameter("travelerIds");
		String orderId = request.getParameter("orderId");
		//String oldProId = request.getParameter("oldProId");
		String newProId = request.getParameter("newProId");
		ReviewService reviewService = SpringContextHolder.getBean("reviewService");
		String[] tIds = travelerIds.split(",");
		Detail d = new Detail();
		StringBuffer reply= new StringBuffer("");
		Long result=0L;
		Long deptId=0L;
		
		deptId = activityAirTicketService.getActivityAirTicketById(airTicketOrderService.getAirticketorderById(Long.valueOf(orderId)).getAirticketId()).getDeptId();
		for(int i=0;i<tIds.length;i++){
			List listDetail = new ArrayList<>();
			d.setKey("travlId");
			d.setValue(tIds[i]);
			listDetail.add(d);
//			d.setKey("oldProId");
//			d.setValue(oldProId);
//			listDetail.add(d);
			d.setKey("newProId");
			d.setValue(newProId);
			result = reviewService.addReview(new Integer(7), new Integer(14), orderId,Long.parseLong(tIds[i]), 0L, "机票改签", reply, 
					listDetail,deptId);
			if(result==0){
				break;
			}
		}
		Map jsonMap = new HashMap();
		String data="";
		 if(result==0L){
			 jsonMap.put("result", reply.toString());
			 data = JSONObject.fromObject(jsonMap).toString();
		 }else{
			 jsonMap.put("result", "成功");
			 data = JSONObject.fromObject(jsonMap).toString();
		 }
		 
		
		this.printJSON(data, response);
		return null;
	}
	
	
	
	
	@SuppressWarnings("all")
	@RequestMapping(value="areaGaiQianCheck")
	public String areaGaiQianCheck(Model model,HttpServletRequest request,HttpServletResponse response) throws Exception{
	
		String travelerIds = request.getParameter("travelerIds");
		String orderId = request.getParameter("orderId");
		//String oldProId = request.getParameter("oldProId");
		String newProId = request.getParameter("newProId");
		ReviewService reviewService = SpringContextHolder.getBean("reviewService");
		String[] tIds = travelerIds.split(",");
		Detail d = new Detail();
		String ids="";
		for(int i=0;i<tIds.length;i++){
			if(i==tIds.length-1){
				ids +=tIds[i];
			}else{
				ids +=tIds[i]+",";
			}
		}
		Map map = new HashMap();
		map.put("ids", ids);
		map.put("orderId", orderId);
		List list = airTicketOrderService.areaGaiQianCheck(map);
		Map jsonMap = new HashMap();
		String names="";
		if(list==null||list.size()==0){
			 jsonMap.put("result", "1");
		}else{
			 for(int i=0;i<list.size();i++){
				 String tid = ((Map)list.get(i)).get("travelerId").toString();
				 //User u = UserUtils.getUser();
				 TravelerService ts = SpringContextHolder.getBean("travelerService");
				 Traveler t = ts.findTravelerById(Long.valueOf(tid));
				 names += t.getName()+",";
			 }
			 jsonMap.put("result", "0");
			 jsonMap.put("msg", names+"正在审核");
		
		}
		String data = JSONObject.fromObject(jsonMap).toString();
		this.printJSON(data, response);
		return null;
	}
	
	
	
	
	
	
	
	
	
	@SuppressWarnings("all")
	@RequestMapping(value = "aireGaiQianAppList")
	public String aireGaiQianAppList(Model model,
			HttpServletRequest request, HttpServletResponse response) {
//		Long id = UserUtils.getUser().getCompany().getId();
//		AgentinfoService agentinfoService = SpringContextHolder.getBean("agentinfoService");
//		List<Agentinfo> agentInfoList = agentinfoService.findAllAgentinfo();//渠道商
//		//待审核
//		Params condition = new Params(request);
//		condition.put("productType", 7);
//		condition.put("flowType", 14);
//		Page page = airTicketOrderService.queryAirtickeToDo(request, response,condition);
//		model.addAttribute("page", page);
//		model.addAttribute("agentInfoList", agentInfoList);
//		List<User> operatorUsers =  new ArrayList<User>();
//		operatorUsers = getAoperatorUsers(1);
//		model.addAttribute("operators", operatorUsers);
//		condition.put("roles", UserUtils.getUser().getRoleList());
//		model.addAttribute("conditionsMap", condition);
		Params condition = new Params(request);
		condition.put("flowType", 14);//condition.put("flowType", 15);把15改成了14 by chy 2015年3月17日15:50:27
		List<User> operatorUsers = getAoperatorUsers(1);
		//获取当前用户的职位信息
		List<UserJob> userjobs =  ((ReviewCommonService)SpringContextHolder.getBean("reviewCommonService")).getWorkFlowJobByFlowType(14);//;把15改成了14 by chy 2015年3月17日15:50:27
		List<UserJob> userJobs = new ArrayList<UserJob>();
		for(UserJob uj :userjobs){
			if(uj.getOrderType()==7){
				userJobs.add(uj);
			}
		}
		
		if(condition.get("userJob")==null||("").equals(condition.get("userJob").toString())){
			if(userJobs.size()>0){
				condition.put("userJob", userJobs.get(0).getJobId());
				condition.put("userJobId", userJobs.get(0).getId());
				condition.put("deptId", userJobs.get(0).getDeptId());
				condition.put("userJobCon", userJobs.get(0).getDeptId()+"-"+userJobs.get(0).getId());
				Long pDeptId;
				List<Long> subIds = new ArrayList<Long>();
				UserJob userJob = userJobs.get(0);
				if(userJob.getDeptLevel() == 1){
					pDeptId = userJob.getDeptId();
					DepartmentDao departmentDao = SpringContextHolder.getBean("departmentDao");
					subIds = departmentDao.findSubidsByParentId(pDeptId);
				} else {
					pDeptId = userJob.getParentDept();
					subIds.add(userJob.getDeptId());
				}
				//获取reviewComppanyid
				Long companyId = UserUtils.getUser().getCompany().getId();
				ReviewCompanyDao reviewCompanyDao = SpringContextHolder.getBean("reviewCompanyDao");
				List<Long> reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, 14, pDeptId);
				if(reviewCompanyList == null || reviewCompanyList.size() == 0) {
					return null;
				}
				Long reviewCompanyId = reviewCompanyList.get(0);
				condition.put("rcid", reviewCompanyId);
//				int nFlag = 0;
//				String deptidtemp="";
//				for(Long subId : subIds){
//					if(nFlag == 0){
//						deptidtemp += subId;
//						nFlag++;
//					} else {
//						deptidtemp += "," + subId;
//					}
//				}
//				condition.put("deptidtemp", reviewCompanyId);
				//获取userJob的审核层级
				List<Integer> levels = reviewService.getJobLevel(pDeptId, userJob.getJobId(), Context.REVIEW_FLOWTYPE_VISA_CHANGE);
				if(levels != null && levels.size() != 0){
					condition.put("revLevel", levels.get(0));
				}
			}
		}else{
			String[] temp = condition.get("userJob").toString().split("-");
			condition.put("deptId", temp[0]);
			UserJobDao userJobDao = SpringContextHolder.getBean("userJobDao");
			UserJob userJob = userJobDao.findOne(Long.parseLong(temp[1]));
			condition.put("userJob", userJob.getJobId());
			condition.put("userJobId", temp[1]);
			condition.put("userJobCon", temp[0]+"-"+temp[1]);
			Long pDeptId;
			List<Long> subIds = new ArrayList<Long>();
			if(userJob.getDeptLevel() == 1){
				pDeptId = userJob.getDeptId();
				DepartmentDao departmentDao = SpringContextHolder.getBean("departmentDao");
				subIds = departmentDao.findSubidsByParentId(pDeptId);
			} else {
				pDeptId = userJob.getParentDept();
				subIds.add(userJob.getDeptId());
			}
			List<Integer> levels = reviewService.getJobLevel(pDeptId, userJob.getJobId(), Context.REVIEW_FLOWTYPE_VISA_CHANGE);
			if(levels != null && levels.size() != 0){
				condition.put("revLevel", levels.get(0));
			}
			//获取reviewComppanyid
			Long companyId = UserUtils.getUser().getCompany().getId();
			ReviewCompanyDao reviewCompanyDao = SpringContextHolder.getBean("reviewCompanyDao");
			List<Long> reviewCompanyList = reviewCompanyDao.findReviewCompanyList(companyId, 14, pDeptId);
			if(reviewCompanyList == null || reviewCompanyList.size() == 0) {
				return null;
			}
			Long reviewCompanyId = reviewCompanyList.get(0);
			condition.put("rcid", reviewCompanyId);
//			int nFlag = 0;
//			String deptidtemp="";
//			for(Long subId : subIds){
//				if(nFlag == 0){
//					deptidtemp += subId;
//					nFlag++;
//				} else {
//					deptidtemp += "," + subId;
//				}
//			}
//			condition.put("deptidtemp", reviewCompanyId);
			
			
		}
		if(condition.get("status")==null){
//			condition.put("status", "all");
			condition.put("status", "todo");
		}
		
		List<User> userList =  systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId());
		Page page = airTicketOrderService.queryAirtickeToDo(request, response,condition);
		model.addAttribute("userJobs", userJobs);
		model.addAttribute("page", page);
		model.addAttribute("conditionsMap", condition);
		AgentinfoService agentinfoService = SpringContextHolder.getBean("agentinfoService");
		List<Agentinfo> agentinfoList = agentinfoService.findAllAgentinfo();
		model.addAttribute("agentinfoList", agentinfoList);
		model.addAttribute("operators", operatorUsers);
		model.addAttribute("userList", userList);
		
		return "modules/airticket/airticketApprovalList";
	}
	
	@SuppressWarnings("all")
	@RequestMapping(value = "airticketApprovalDetail")
	public String airticketApprovalDetail(Model model,
			HttpServletRequest request, HttpServletResponse response){
		
		String orderId = request.getParameter("orderId");
		String trvalerId = request.getParameter("trvalerId");
		String reviewId = request.getParameter("reviewId");
		
		Map<String, Object> orderDetailInfoMap = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId);
		
		orderDetailInfoMap.put("travlerDetail",airTicketOrderService.queryApprovalDetailTravel(request, response, reviewId,orderId));
		//Map<String, Object> orderDetailInfoMap = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId,trvalerId);
		model.addAttribute("orderDetailInfoMap", orderDetailInfoMap);
    //	model.addAttribute("from_areaslist", areaService.findFromCityList(""));//出发城市
	//	model.addAttribute("arrivedCitys", areaService.findAirportCityList2("")); //到达城市
		model.addAttribute("from_areaslist",areaService.findFromCityList(""));//出发城市
		model.addAttribute("arrivedCitys", areaService.findAirportCityList(""));// 到达城市
		model.addAttribute("orderId", orderId);
		model.addAttribute("trvalerId", trvalerId);
		model.addAttribute("reviewId", reviewId);
		return "modules/airticket/airticketApprovalDetail";
	}
	
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping(value = "planeReview")
	public void planeReview(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String reviewId = request.getParameter("reviewId");
		String travelerId = request.getParameter("travelerId");
		String orderId = request.getParameter("orderId");
		ReviewDao reviewDao = SpringContextHolder.getBean("reviewDao");
		Review review = reviewDao.findOne(Long.valueOf(reviewId));
		String operate = request.getParameter("operate");
		String reason = request.getParameter("reason");
		ReviewService reviewService = SpringContextHolder.getBean("reviewService");
		int nowl = review.getNowLevel();
		int flag = reviewService.UpdateReview(Long.parseLong(reviewId),nowl, Integer.parseInt(operate), reason);
		
		Map jsonMap = new HashMap();
		if(operate.equals("1")){
			try {
				if(flag==1){
					airTicketOrderService.planeReview(Long.valueOf(orderId), Long.valueOf(reviewId), Long.valueOf(travelerId));
				}
				jsonMap.put("result", "审核成功");
			} catch (Exception e) {
				e.printStackTrace();
				 jsonMap.put("result", "失败");
			}
		}else{
			jsonMap.put("result", "驳回操作成功");
		}
		
		 String data = JSONObject.fromObject(jsonMap).toString();
		 this.printJSON(data, response);
		
	}
	/**
	 * 机票改签批量审批
	 * @param response
	 * @param model
	 * @param request
	 */
	@ResponseBody
	@RequestMapping(value = "batchPlaneReview")
	public Map<String,Object> batchPlaneReview(HttpServletResponse response,
	        Model model, HttpServletRequest request){
		String result =  request.getParameter("result"); //1审核通过，0，驳回
		String remarks = request.getParameter("remarks");//批量审核驳回原因
		String userLevel = request.getParameter("userLevel"); // 当前用户审核等级
		/**
		 * 参数结构：
		 * 1@110,1@112
		 * 说明：(当前审核层级@审核id,当前审核层级@审核id,......)
		 */
		String revids = request.getParameter("revids");
		String[] levelandrevids = revids.split(",");	
		int num = 0;
		Map<String, Object> back =new HashMap<String,Object>();
		if (StringUtils.isBlank(result)) {
			back.put("res", "error");
			back.put("msg", "审批结果不能为空");
			return back;
		} else {
			
		}
		
		
		for(int i=0; i<levelandrevids.length;i++){
			String revid = levelandrevids[i].split("@")[1];
			try{
				if(StringUtils.isNotBlank(revid)){
					ReviewDao reviewDao = SpringContextHolder.getBean("reviewDao");
					ReviewService reviewService = SpringContextHolder.getBean("reviewService");
					Review review = reviewDao.findOne(Long.valueOf(revid));
					Long travelerId = review.getTravelerId();
					String orderId = review.getOrderId();
					int flag = 0;
					if(travelerId!=null && StringUtils.isNotBlank(orderId)){
						if("1".equals(result)){ // 审核通过
							flag = reviewService.UpdateReview(Long.valueOf(revid), Integer.valueOf(userLevel), 1, "");
						}else{ // 审核驳回
							flag = reviewService.UpdateReview(Long.valueOf(revid), Integer.valueOf(userLevel), 0, remarks);
						}
						ReviewLog reviewLog = new ReviewLog();
						reviewLog.setReviewId(Long.valueOf(revid));
						reviewLog.setNowLevel(Integer.valueOf(userLevel));
						reviewLog.setResult(result);
						reviewLog.setRemark(remarks);
						reviewLogService.saveReviewLog(reviewLog);
						num++;
					}
					
					if(result.equals("1")){
						try {
							if(flag==1){
								// 归还原产品余位，创建新产品订单，扣减新产品余位
								airTicketOrderService.planeReview(Long.valueOf(orderId), Long.valueOf(revid), Long.valueOf(travelerId));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} catch (NumberFormatException e) {
				log.error("机票批量改签申请: 审核ID为："+revid+"；  报错原因："+e.getMessage());
			} catch (Exception e) {
				log.error("机票批量改签申请: 审核ID为："+revid+"；  报错原因："+e.getMessage());
			}
		}
		back.put("res", "success");
		back.put("msg", "共有"+num+"条记录操作成功。");
		return back;
	}
	
	@RequestMapping(value = "planeReviewReuse")
	public void planeReviewReuse(HttpServletRequest request,
			HttpServletResponse response) {
		String reviewId = request.getParameter("reviewId");
		ReviewDao reviewDao = SpringContextHolder.getBean("reviewDao");
		Review review = reviewDao.findOne(Long.valueOf(reviewId));
		if(review.getNowLevel()<review.getTopLevel()){
			review.setNowLevel(review.getNowLevel()+1);
			reviewDao.UpdateReview(review.getId(), review.getNowLevel(), 0, UserUtils.getUser().getId(), UserUtils.getUser().getName(), new Date(), "");
		}
		
	}
	
	
	private List<User> getAoperatorUsers(Integer type){
		List<User> userList;
		List<Role> roleList;
		SystemService systemService = SpringContextHolder.getBean("systemService");
		// 判断，type = 1 ，地接计调主管列表
		if(type==1){
			roleList = systemService.findByRoleTypeAndCompanyId(Context.ROLE_TYPE_OP, Context.ROLE_TYPE_OP_EXECUTIVE, UserUtils.getUser().getCompany().getId());
		}else{ // type==2,机票计调主管列表
			roleList = systemService.findByRoleTypeAndCompanyId(Context.ROLE_TYPE_OP, Context.ROLE_TYPE_OP_EXECUTIVE, UserUtils.getUser().getCompany().getId());
		}
		
		if(roleList!=null && roleList.size()>0){
			userList = new ArrayList<User>();
			Set<User> userSet = new HashSet<User>();
			for(Role role : roleList) {
				for(User user : role.getUserList())
					userSet.add(user);
			}
			userList.addAll(userSet);
			Collections.sort(userList, new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return (o1.getName() == null ? "" : o1.getName()).compareTo(o2.getName());
                }
            });
			return userList;
		}
		return null;
	}
	
	/**
	 * 保存备注信息
	 * @param orderId
	 * @param remark
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveRemark", method = RequestMethod.POST)
	public Object saveRemark(@RequestParam Long orderId,
			@RequestParam String remark) {

		Map<String, String> data = new HashMap<String, String>();

		boolean bFLag = airTicketOrderService.saveRemark(orderId, remark);
		if (bFLag) {
			data.put("flag", "success");
		} else {
			data.put("flag", "error");
		}

		return data;
	}
	
	/**
	 * 打开修改支付凭证页面
	 */
	@RequestMapping(value="modifypayAirticketVoucher/{payId}/{orderId}")
	public String modifypayAirticketVoucherPage(@PathVariable String payId,@PathVariable String orderId, Model model,HttpServletRequest request){
		// 1 查询修改页面所需展示的信息
		Orderpay orderpay = airTicketOrderService.getOrderpay(new Long(payId));
	    AirticketOrder pro = airTicketOrderService.getAirticketorderById(Long.parseLong(orderId));
	    
	    //支付订单金额千位符处理
	    if (StringUtils.isNotBlank(orderpay.getMoneySerialNum())) {
	    	airTicketOrderService.clearObject(orderpay);
	 	    orderpay.setMoneySerialNum(moneyAmountService.getMoneyStr(orderpay.getMoneySerialNum()));
	    }
	    
	    //订单金额千位符处理
	    if (StringUtils.isNotBlank(pro.getTotalMoney())) {
	    	airTicketOrderService.clearObject(pro);
	 	    pro.setTotalMoney(moneyAmountService.getMoneyStr(pro.getTotalMoney()));
	    }
	  
		model.addAttribute("orderpay",orderpay);
		model.addAttribute("orderId",orderId);
		model.addAttribute("productorder",pro);
		// 2跳转到对应页面展示
		return "modules/order/modifypayAirticketVoucher";
	}
	
	/**
     * 修改订单是否已查看状态
     * @param request
     * @return
     */
    @RequestMapping(value = "changeNotSeenOrderFlag")
    @ResponseBody
    public Object changeNotSeenOrderFlag(HttpServletRequest request) {
        Map<String, Object> map = Maps.newHashMap();
        String notSeenOrderIds = request.getParameter("notSeenOrderIds");
        if (StringUtils.isNotBlank(notSeenOrderIds)) {
        	Set<Long> notSeenOrderIdList = Sets.newHashSet();
        	for (String orderId : notSeenOrderIds.split(",")) {
        		if (StringUtils.isNotBlank(orderId)) {
        			notSeenOrderIdList.add(Long.parseLong(orderId));
        		}
        	}
        	int changeSum = this.airTicketOrderService.changeNotSeenOrderFlag(notSeenOrderIdList);
        	map.put("result", "success");
        	map.put("changeSum", changeSum);
        } else {
        	map.put("result", "error");
        }
        return map;
    }
    
    /**
     * 依据团期Id，查找所有的附件Id
     */    
    @ResponseBody
    @RequestMapping(value="findAttachmentIdsByGroupId",method=RequestMethod.POST)
	public Object findAttachmentIdsByGroupId(@RequestParam(value="activityId") String activityId) {
		List<Map<String, Object>> list = null; //产品对应的订单List
		List<Map<String, Object>> attachmentIdList = Lists.newArrayList(); //订单对应附件ID List

			//根据机票产品ID查询所有订单
			if (StringUtils.isNotBlank(activityId)) {
				list = airTicketOrderService.queryAirticketOrdersByProductId(activityId);
			}
			
			//根据订单查询附件信息ID并放到导出List
				if (CollectionUtils.isNotEmpty(list)) {
					for (Map<String, Object> map : list) {
						//附件信息
						List<Map<String,Object>> airticketOrderAttachment = airticketOrderDao.queryAirticketOrderAttachment(Integer.valueOf(map.get("orderId").toString()));
						if (CollectionUtils.isNotEmpty(airticketOrderAttachment)) {
							attachmentIdList.addAll(airticketOrderAttachment);
						}
					}
				}
				if (CollectionUtils.isEmpty(attachmentIdList)) {
					return null;
				}
				List<String> atid = Lists.newArrayList();
				for (Map<String, Object> map : attachmentIdList) {
					Object xxObject = map.get("attachmentId");
					atid.add(String.valueOf(xxObject));
				}
		
		return atid;
	}

	/**
	 * 根据订单id修改订单-销售
	 * @author yunpeng.zhang
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @createDate 2015年12月31日
	 */
	@RequestMapping(value="airticketOrderSale4Modify")
	public String airticketOrderSale4Modify(Model model,HttpServletRequest request,HttpServletResponse response, String queryType) {
		//1.页面的一些初始化
		initParam(model);
		//2.查询订单内容
		String orderId = request.getParameter("orderId");
		Map<String, Object> orderDetailInfoMap = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId);
		//3.获取用户角色信息
		List<Role> roleInfoList = UserUtils.getUser().getRoleList();
		Map<String,Object> roleMap = new HashMap<String,Object>();
		if(roleInfoList!=null && roleInfoList.size()>0){
			for(Role roleInfo : roleInfoList){
				roleMap.put(roleInfo.getRoleType(), roleInfo);
			}
		}
		if(null != orderDetailInfoMap.get("mainOrderId")){
			String mainorderId = orderDetailInfoMap.get("mainOrderId").toString();
			String groupNum = activityAirTicketService.getActivitygroupById(new Long(mainorderId));
			ProductOrder  porder=	activityAirTicketService.getProductById(new Long(mainorderId));
			model.addAttribute("porder", porder);
			model.addAttribute("groupNum", groupNum);
		}

		// 通过订单id查询订单对象
		AirticketOrder airticketOrder = airTicketOrderService.getAirticketorderById(Long.parseLong(orderId));
		Long agentId = null;
		Integer placeHolderType = null;
		Long airticketId = null;
		Integer personNum = null;
		List<Traveler> travelerList = null;
		if(airticketOrder != null) {
			agentId = airticketOrder.getAgentinfoId();
			placeHolderType = airticketOrder.getPlaceHolderType();
			airticketId = airticketOrder.getAirticketId();
			// 查询订单总人数
			personNum = airticketOrder.getPersonNum();
			// 查询游客信息
			travelerList = activityAirTicketService.getAllTraveler(airticketOrder);
		}
		// 通过机票产品id查询机票产品信息
		ActivityAirTicket activityAirTicket = activityAirTicketService.getActivityAirTicketById(airticketId);
		// 通过机票产品id和渠道id查询机票产品切位信息
		AirticketActivityReserve activityReserve = airticketPreOrderService.queryAirticketActivityReserve(airticketId, agentId);

		// 4.查询机票余位
		Integer freePosition = null;
		// 5.查询特殊人群数
		Integer maxPeopleCount = null;
		Integer maxChildrenCount = null;
		if(activityAirTicket != null) {
			freePosition = activityAirTicket.getFreePosition();
			maxPeopleCount = activityAirTicket.getMaxPeopleCount();
			maxChildrenCount = activityAirTicket.getMaxChildrenCount();
			Map<String, Object> counts = activityAirTicketService.findByGoupid(airticketOrder.getAirticketId(),airticketOrder.getId()+"");
			int orderPersonNumChild = 0;
			int orderPersonNumSpecial = 0;
			if(counts != null){
				if(maxPeopleCount != null){
					orderPersonNumSpecial = maxPeopleCount-(counts.get("orderPersonNumSpecial")==null?0:new Integer(counts.get("orderPersonNumSpecial").toString()));
					model.addAttribute("currentPeopleCount", orderPersonNumSpecial);
				}else{
					model.addAttribute("currentPeopleCount", null);
				}
				if(maxChildrenCount != null){
					orderPersonNumChild = maxChildrenCount-(counts.get("orderPersonNumChild")==null?0:new Integer(counts.get("orderPersonNumChild").toString()));
					model.addAttribute("currentChildrenCount", orderPersonNumChild);
				}else{
					model.addAttribute("currentChildrenCount", null);
				}
			}
		}
		//获取儿童  特殊人群占位人数
		 
		 
		// 6.查询机票切位
		Integer leftpayReservePosition = null;
		if(activityReserve != null) {
			leftpayReservePosition = activityReserve.getLeftpayReservePosition();
		}

		// 7.币种列表
		Long companyId = UserUtils.getUser().getCompany().getId();
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

		JSONArray bzJson = JSONArray.fromObject(listMap);
		model.addAttribute("bzJson",bzJson);// 所有币种信息

		Currency curr=getCurrencyMark(currencyList, activityAirTicket);
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

		model.addAttribute("airticketOrder", airticketOrder);						// 机票订单
		model.addAttribute("activity", activityAirTicket);
		model.addAttribute("orderDetailInfoMap", orderDetailInfoMap);				// 订单内容
		model.addAttribute("from_Areas",areaService.findFromCityList(""));			// 出发城市
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));	// 到达城市
		model.addAttribute("freePosition", freePosition);							// 产品余位
		model.addAttribute("leftpayReservePosition", leftpayReservePosition);		// 产品切位
		model.addAttribute("placeHolderType", placeHolderType);						// 占位方式: 0占位 1切位
		model.addAttribute("personNum", personNum);									// 订单总人数
		model.addAttribute("bzList", listMap);										// 币种列表
		model.addAttribute("maxPeopleCount", maxPeopleCount);						// 最高特殊人群数
		model.addAttribute("maxChildrenCount", maxChildrenCount);						//儿童最高人数
		model.addAttribute("airticketbz",airticketbz);								// 机票产品币种信息
		model.addAttribute("travelerList", travelerList);							// 查询当前订单下所有游客信息
		model.addAttribute("queryType", queryType);
		// 发票
	    List<Orderinvoice> invoices = orderinvoiceService.findCreatedInvoiceByOrder(Integer.parseInt(airticketOrder.getId().toString()), Context.ORDER_TYPE_JP);
	    model.addAttribute("invoices", invoices);
	    // 收据
	    List<OrderReceipt> receipts = orderreceiptService.findCreatedReceiptByOrder(Integer.parseInt(airticketOrder.getId().toString()), Context.ORDER_TYPE_JP);
	    model.addAttribute("receipts", receipts);
		
		//add start by yang.jiang 2016-01-17 17:57:06		
		List<OrderContacts> orderContacts = orderContactsService.findOrderContactsByOrderIdAndOrderType(airticketOrder.getId(), Context.ORDER_TYPE_JP);
		if (CollectionUtils.isEmpty(orderContacts)) {
			orderContacts.add(new OrderContacts());
		}
		model.addAttribute("orderContactsSrc", orderContacts);  //保存的是最原始的联系人---供非签约渠道订单修改使用
		if (CollectionUtils.isNotEmpty(orderContacts)) {
			model.addAttribute("orderContacts", orderContacts);
			net.sf.json.JSONArray currencyListJsonArray = net.sf.json.JSONArray.fromObject(orderContacts);
	        model.addAttribute("orderContactsListJsonArray", currencyListJsonArray.toString());
		}
		//渠道商的联系地址
		List<SupplierContacts> contacts = supplierContactsService.findAllContactsByAgentInfo(agentId);  //取出渠道商所有联系人（包括第一联系人）
		String address = agentinfoService.getAddressStrById(agentId);
		model.addAttribute("address", address == null ? "" : address);
		//渠道商转换为json
		for (SupplierContacts supplierContacts : contacts) {
			supplierContacts.setAgentAddressFull(address);
		}
		//转换成view实体
		List<SupplierContactsView> contactsView = Lists.newArrayList();
		for (SupplierContacts supplierContacts : contacts) {			
			SupplierContactsView splContactsView = new SupplierContactsView();
			BeanUtils.copyProperties(splContactsView, supplierContacts);
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
		//订单是否允许添加多个渠道联系人信息
		Integer allowAddAgentInfo = UserUtils.getUser().getCompany().getIsAllowAddAgentInfo();
		model.addAttribute("allowAddAgentInfo", allowAddAgentInfo);
		//订单是否允许渠道联系人信息输入和修改
		Integer allowModifyAgentInfo = UserUtils.getUser().getCompany().getIsAllowModifyAgentInfo();
		model.addAttribute("allowModifyAgentInfo", allowModifyAgentInfo);
		Agentinfo agentinfo = agentinfoService.loadAgentInfoById(agentId);
		agentinfo.setAgentAddressFull(address);
		model.addAttribute("agentinfo", agentinfo);
		List<Agentinfo>  agentinfoList = agentinfoService.findAllAgentinfoBySalerId(airticketOrder.getSalerId());
	    model.addAttribute("agentinfoList", agentinfoList);	//add by zhangcl

		return "modules/order/airticketOrderModify4Sale";
	}

	/**
	 * 修改机票订单，保存游客信息，订单信息。扣减机票余位，订单总额改变。
	 */
	@ResponseBody
	@RequestMapping(value = "/modifyAirticketOrder")
	public Map<String, Object> modifyAirticketOrder(HttpServletRequest request) {
		Map<String, Object> returnResult = new HashMap<>();
		// 将请求参数放入了一个map中
		Map<String, String> parameters = handlerRequestParameters(request);

		String travelers = request.getParameter("travelers");												// 游客信息
		String orderContacts = request.getParameter("orderContacts");										// 预定人信息
		String orderTotalClearPrice = request.getParameter("orderTotalClearPrice");							// 订单总结算价

		List<OrderContacts> orderContactsList = null;														// 预定人信息
		List<MoneyAmount> orderTotalClearPriceList = null;													// 订单总结算价
		try {
			orderContactsList = AirticketOrderUtil.getContactsListNew(orderContacts);  						//联系人模块使用了新name
			orderTotalClearPriceList = AirticketOrderUtil.getOrderTotalClearPriceList(orderTotalClearPrice);
		} catch(Exception e) {
			e.printStackTrace();
			returnResult.put("result", "2");
			returnResult.put("msg", "JSON数据转换出错!");
			return returnResult;
		}
		try {
			// 服务器端的余位校验
			airTicketOrderService.validateFreePosition(parameters);
			// 核心业务的处理（保存预订人信息，游客信息， 修改订单信息）
			airTicketOrderService.modifyAirticketOrder(parameters, travelers, orderContactsList, orderTotalClearPriceList);
		} catch(Exception e) {
			e.printStackTrace();
			returnResult.put("result", "2");
			returnResult.put("msg", e.getMessage());
			return returnResult;
		}

		returnResult.put("result", "1");
		returnResult.put("msg", "success");

		return returnResult;
	}

	/**
	 * 对请求参数进行校验，同时存入一个Map中
	 */
	private Map<String, String> handlerRequestParameters(HttpServletRequest request) {
		Map<String, String> parameters = new HashMap<>();
		String adultNum = request.getParameter("adultNum");													// 修改后成人数
		String childNum = request.getParameter("childNum");													// 修改后儿童数
		String specialNum = request.getParameter("specialNum");												// 修改后特殊人群数
		String orgPersonNum = request.getParameter("orgPersonNum");											// 修改前订单人数
		String newPersonNum = request.getParameter("newPersonNum");											// 修改后订单人数
		String agentId = request.getParameter("agentId");													// 渠道id
		String comments = request.getParameter("comments");													// 特殊需求
		String groupRebatesCurrency = request.getParameter("groupRebatesCurrency");							// 团队返佣币种
		String groupRebatesMoney = request.getParameter("groupRebatesMoney") == "" ? "0" : request.getParameter("groupRebatesMoney");	// 团队返佣金额
		String orderId = request.getParameter("orderId");													// 订单id
		String airticketId = request.getParameter("airticketId");											// 机票产品id
		String placeHolderType = request.getParameter("placeHolderType");									// 占位类型
		String orderCompanyName = request.getParameter("orderCompanyName");									// 非签约渠道名称

		parameters.put("adultNum", adultNum);
		parameters.put("childNum", childNum);
		parameters.put("specialNum", specialNum);
		parameters.put("orgPersonNum", orgPersonNum);
		parameters.put("newPersonNum", newPersonNum);
		parameters.put("agentId", agentId);
		parameters.put("comments", comments);
		parameters.put("groupRebatesCurrency", groupRebatesCurrency);
		parameters.put("groupRebatesMoney", groupRebatesMoney);
		parameters.put("orderId", orderId);
		parameters.put("airticketId", airticketId);
		parameters.put("placeHolderType", placeHolderType);
		parameters.put("orderCompanyName", orderCompanyName);

		return parameters;
	}

	/**
	 * 获取当前币种
	 */
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
}
