package com.trekiz.admin.modules.order.web;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.excel.ExportExcel;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.finance.entity.ReturnDifference;
import com.trekiz.admin.modules.finance.service.ReturnDifferenceService;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderListService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Menu;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.CommonUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.review.transfersMoney.singlegroup.service.NewTransferMoneyService;
 
 /**
  * 订单查询：仅用于单团订单查询（单团、散拼、游学、大客户、自由行统称为单团）
  * @author yakun.bai
  *
  */
@Controller
@RequestMapping(value = "${adminPath}/orderList/manage")
public class OrderListController extends BaseController {
    
    protected static final Logger logger = LoggerFactory.getLogger(OrderListController.class);
    
    @Autowired
    private OrderCommonService orderService;
    @Autowired
    private OrderListService orderListService;
    @Autowired
	@Qualifier("travelActivitySyncService")
    private ITravelActivityService travelActivityService;
    @Autowired
    private AgentinfoService agentinfoService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private MoneyAmountService moneyAmountService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private ProductOrderCommonDao productOrderCommonDao;
    @Autowired
    private OrderCommonService orderCommonService;
    @Autowired
    private NewTransferMoneyService newTransferMoneyService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private TravelerService travelerService;
	@Autowired
	private ReturnDifferenceService returnDifferenceService;
	@Autowired
	private CurrencyService currencyService;
   
   
	/**
	 * 订单查询
	 * @param showType 订单状态：0 全部订单；1 全款未支付；2 订金未支付；3 已占位；4 订金已支付；5 已经支付；7 计调占位；99 已取消；111 已删除订单
	 * @param orderStatus 订单类型：1 单团；2 散拼；3 游学；4 大客户；5 自由行  10 游轮
	 * @param travelActivity
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value ="showOrderList/{showType}/{orderStatus}")
	public String showOrderList(@PathVariable String showType, @PathVariable String orderStatus,
	        @ModelAttribute TravelActivity travelActivity, HttpServletResponse response,
	        Model model, HttpServletRequest request) throws Exception {
		
		long oneTime = System.currentTimeMillis();
		
		//按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("order", model);
		
		//查询条件
        Map<String, String> mapRequest = Maps.newHashMap();
        
        //参数处理：去除空格和处理特殊字符并传递到后台
        //参数解释：渠道ID、订单编号或团期编号、联系人、计调名称、团期起始开始时间、团期起始结束时间、团期号
        String paras = "agentId,orderNumOrGroupCode,orderPersonName,activityCreate,proCreateBy,salerId,groupOpenDateBegin,groupOpenDateEnd," +
        		"groupCode,orderTimeBegin,orderTimeEnd,invoiceStatus,receiptStatus,jiekuanStatus,confirmOccupy,payamentType";
        OrderCommonUtil.handlePara(paras, mapRequest, model, request);
        
        //订单或团期列表标识：order为订单、group为团期，默认查询订单列表
        String orderOrGroup = request.getParameter("orderOrGroup");
        String companyUuid = UserUtils.getUser().getCompany().getUuid();
        if (StringUtils.isBlank(orderOrGroup)) {
        	//如果是大洋批发商，则默认按订单展示：C322需求，添加时间为2015-10-30 yakun.bai
        	if (companyUuid != null 
        			&& (Context.SUPPLIER_UUID_DYGL.equals(companyUuid) 
        					|| (Context.SUPPLIER_UUID_SHZL.equals(companyUuid) && Context.ORDER_STATUS_SINGLE.equals(orderStatus)))) {
        		orderOrGroup = "order";
        	} else {
        		orderOrGroup = "group";
        	}
        }
        mapRequest.put("orderOrGroup", orderOrGroup);
        
        //根据订单编号查询此订单对应所在的部门
        getDepartmentIdByOrderNum(common, request, model);
        
        //订单调用取消接口（过了保留时间订单需要取消）
        if (1 == UserUtils.getUser().getCompany().getIsCancleOrder()) {
        	orderService.cancelOrderWithRemainDays();
        }
	        
        //排序方式：默认为出团日期降序排列
        String orderBy = request.getParameter("orderBy");
        if (StringUtils.isBlank(orderBy)) {
            orderBy = "groupOpenDate DESC";
        }
        mapRequest.put("orderBy", orderBy);
        
        //订单或团期查询
        Page<Map<Object, Object>> pageOrder = orderListService.findOrderListByPayType(showType, orderStatus, new Page<Map<Object, Object>>(request, response), travelActivity, mapRequest, common);
        model.addAttribute("page", pageOrder);
        List<Map<Object, Object>> listorder = pageOrder.getList();
        orderBy = pageOrder.getOrderBy().replace("agp", "pro");
        pageOrder.setOrderBy(orderBy);
	        
        List<String> groupIdList = Lists.newArrayList();
        
        for (Map<Object, Object> listin : listorder) {
        	if ("group".equals(orderOrGroup) && listin.get("id") != null) {
        		groupIdList.add(listin.get("id").toString());
        	} else if ("order".equals(orderOrGroup) && listin.get("id") != null) {
        		//金额处理
            	handlePrice(listin);
            	//获取订单剩余时间
            	getOrderLeftTime(listin);
            	//财务提醒
            	cwNotice(listin, orderStatus);
            	// 差额返还
            	if (null != listin.get("differenceFlag") && listin.get("differenceFlag").toString().equals("1")) {
            		String differenceMoney = moneyAmountService.getMoney(listin.get("differenceMoney").toString());
            		listin.put("differenceMoney", differenceMoney);
            	}
        	}
        	if (listin.get("createBy") != null) {
        		listin.put("carateUserName", UserUtils.getUser(StringUtils.toLong(listin.get("createBy"))).getName());
        	}
        }
        
        List<Map<Object, Object>> orderList = null;
        StringBuffer orderIds = new StringBuffer("");
        if ("group".equals(orderOrGroup) && CollectionUtils.isNotEmpty(groupIdList)) {
        	orderList = orderListService.findByGroupIds(new Page<Map<Object, Object>>(request, response), groupIdList, mapRequest.get("orderSql")).getList();
        	
        	for (Map<Object, Object> listin : orderList) {
        		//金额处理
        		handlePrice(listin);
        		if (listin.get("createBy") != null) {
            		listin.put("carateUserName", UserUtils.getUser(StringUtils.toLong(listin.get("createBy"))).getName());
            	}
        		//获取订单剩余时间
            	getOrderLeftTime(listin);
            	//获取orderId
            	Object object = listin.get("id");
            	orderIds.append(object.toString()+" ");
            	// 差额返还
            	if (null != listin.get("differenceFlag") && listin.get("differenceFlag").toString().equals("1")) {
            		String differenceMoney = moneyAmountService.getMoney(listin.get("differenceMoney").toString());
            		listin.put("differenceMoney", differenceMoney);
            	}
	        }
        }
        String sb =new String(orderIds);
        String userType = UserUtils.getUser().getUserType();
	        
        model.addAttribute("companyUuid", companyUuid);
        model.addAttribute("userType", userType);
        model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
        model.addAttribute("trafficModes", DictUtils.getDicMap(Context.TRAFFIC_MODE));
        model.addAttribute("trafficNames", DictUtils.getValueAndLabelMap("traffic_name",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
        model.addAttribute("payTypes", DictUtils.getDicMap(Context.PAY_TYPE));
        model.addAttribute("showType",showType); 
        model.addAttribute("orderStatus",orderStatus); 
        model.addAttribute("orderOrGroup",orderOrGroup);
        model.addAttribute("orders",orderList);
        model.addAttribute("orderTypeStr", OrderCommonUtil.getStringOrdeType(orderStatus));
        model.addAttribute("isNeedNoticeOrder", UserUtils.getUser().getCompany().getIsNeedAttention());
        model.addAttribute("userList", systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId()));
        if ("2".equals(orderStatus)) {
        	List<Agentinfo> agentinfoList = agentinfoService.findAllAgentinfo();
        	List<Agentinfo> allQuauqAgentinfo = agentinfoService.getAllQuauqAgentinfo();
        	agentinfoList.addAll(allQuauqAgentinfo);
			model.addAttribute("agentinfoList", agentinfoList);
		} else {
			model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
		}
		model.addAttribute("orderIds",sb);
        long twoTime = System.currentTimeMillis();
        
        //部门
        Set<Department> departmentSet = UserUtils.getUserDepartment();
		model.addAttribute("departmentSet", departmentSet);
		model.addAttribute("isOpManager", UserUtils.isOpManager());
        //订单类型
		model.addAttribute("orderTypes", DictUtils.getDictList("order_type"));
		//内部销售人员的名单
		model.addAttribute("agentSalers", agentinfoService.findInnerSales(UserUtils.getUser().getCompany().getId()));
		Long companyId = UserUtils.getUser().getCompany().getId();
		Office office = officeService.get(companyId);
		model.addAttribute("office", office);

		/**
         * 针对大洋国旅的旅行社，进行单独的修改，将单团、散拼、销售机票、计调机票、签务签证、销售签证订单中订单列表隐藏
         */
		model.addAttribute("companyUuid", UserUtils.getUser().getCompany().getUuid());
		model.addAttribute("dayangCompanyUuid", Context.DA_YANG_COMPANYUUID);
		/**
		 * C407 散拼订单隐藏“预报名订单”页签；
		 */
		model.addAttribute("youjiaCompanyUuid", Context.YOU_JIA_COMPANYUUID);
		
		long threeTime = System.currentTimeMillis();
		System.out.println("用户查询时间：" + (twoTime - oneTime) + "ms");
        System.out.println("渠道查询时间：" + (threeTime - twoTime) + "ms");
		
		
        return "modules/order/list/orderList";
	}	
	
	@RequestMapping(value="ExportExcelOfUserlist/{showType}/{orderStatus}")
	public void exportExcelOfuUserlist(@PathVariable String showType, @PathVariable String orderStatus,
	        @ModelAttribute TravelActivity travelActivity, HttpServletResponse response,
	        Model model, HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, IntrospectionException{
		
		//按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("order", model);
		
		//查询条件
        Map<String, String> mapRequest = Maps.newHashMap();
        
        //参数处理：去除空格和处理特殊字符并传递到后台
        //参数解释：渠道ID、订单编号或团期编号、联系人、计调名称、团期起始开始时间、团期起始结束时间、团期号
        String paras = "agentId,orderNumOrGroupCode,orderPersonName,activityCreate,proCreateBy,salerId,groupOpenDateBegin,groupOpenDateEnd," +
        		"groupCode,orderTimeBegin,orderTimeEnd,invoiceStatus,receiptStatus,jiekuanStatus,confirmOccupy";
        OrderCommonUtil.handlePara(paras, mapRequest, model, request);
        
        //订单或团期列表标识：order为订单、group为团期，默认查询订单列表
        String orderOrGroup = request.getParameter("orderOrGroup");
        String companyUuid = UserUtils.getUser().getCompany().getUuid();
        if (StringUtils.isBlank(orderOrGroup)) {
        	//如果是大洋批发商，则默认按订单展示：C322需求，添加时间为2015-10-30 yakun.bai
        	if (companyUuid != null 
        			&& (Context.SUPPLIER_UUID_DYGL.equals(companyUuid) 
        					|| (Context.SUPPLIER_UUID_SHZL.equals(companyUuid) && Context.ORDER_STATUS_SINGLE.equals(orderStatus)))) {
        		orderOrGroup = "order";
        	} else {
        		orderOrGroup = "group";
        	}
        }
        mapRequest.put("orderOrGroup", orderOrGroup);
        
        //根据订单编号查询此订单对应所在的部门
        getDepartmentIdByOrderNum(common, request, model);
        
        //订单调用取消接口（过了保留时间订单需要取消）
        if (1 == UserUtils.getUser().getCompany().getIsCancleOrder()) {
        	orderService.cancelOrderWithRemainDays();
        }
	        
        //排序方式：默认为出团日期降序排列
        String orderBy = request.getParameter("orderBy");
        if (StringUtils.isBlank(orderBy)) {
            orderBy = "groupOpenDate DESC";
        }
        mapRequest.put("orderBy", orderBy);
        
        //订单或团期查询
        Page<Map<Object, Object>> pageOrder = orderListService.findOrderListByPayType(showType, orderStatus, new Page<Map<Object, Object>>(request, response,-1), travelActivity, mapRequest, common);
        
        //  文件名称str   文件表头列表Map   表头对应的属性  Map   class信息 class
        String fileName = "游客信息";
        String columnTitlesStr = "序号,销售,人数,单号,姓名,英文,性别,出生年月,护照号,发行地,职业,关系,押金,担保函,小费,自费,保险,备注,手机";
        String[] columnTitlesArray =  columnTitlesStr.split(",");
        
        String rowMsgsStr = "salerName,orderPersonNum,orderNum,name,nameSpell,sex,birthDay,passportCode,passportPlace,,,,,,,,remark,telephone";
        String[] rowMsgsArray = rowMsgsStr.split(",");
        List<Integer> delFlag = Lists.newArrayList();
		delFlag.add(Context.TRAVELER_DELFLAG_NORMAL);
		delFlag.add(Context.TRAVELER_DELFLAG_EXIT);
//		delFlag.add(Context.TRAVELER_DELFLAG_EXITED);
		delFlag.add(Context.TRAVELER_DELFLAG_TURNROUND);
//		delFlag.add(Context.TRAVELER_DELFLAG_TURNROUNDED);
        for(Map<Object,Object> entity:pageOrder.getList()){
        	List<Traveler> travelerList = travelerService.findTravelerByOrderIdAndOrderType(new Long(entity.get("id").toString()), new Integer(entity.get("orderStatus").toString()), delFlag);
        	entity.put("userList", travelerList);
        }
        try {
			ExportExcel.exportExcel(fileName,columnTitlesArray,rowMsgsArray,pageOrder.getList(),request,response,"orderCommon");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 根据订单编号查询此订单对应所在的部门
	 * @param common
	 * @param request
	 * @param model
	 */
	private void getDepartmentIdByOrderNum(DepartmentCommon common, HttpServletRequest request, Model model) {
		
//		String orderNumOrGroupCode = request.getParameter("orderNumOrGroupCode");
//        //解决下完订单后不能跳到相应区域问题
//        if (StringUtils.isNotBlank(orderNumOrGroupCode)) {
//        	List<ProductOrderCommon> list = orderService.findByOrderNum(orderNumOrGroupCode);
//        	if (CollectionUtils.isNotEmpty(list) && list.size() == 1) {
//        		Long productId = list.get(0).getProductId();
//        		User tempUser = travelActivityService.findById(productId).getCreateBy();
//        		if (tempUser != null) {
//        			List<Role> roleList = tempUser.getRoleList();
//        			if (CollectionUtils.isNotEmpty(roleList)) {
//        				for (Role role : roleList) {
//        					if (role.getDepartment() != null && (Context.ROLE_TYPE_OP.equals(role.getRoleType()) || Context.ROLE_TYPE_SALES.equals(role.getRoleType()))) {
//        						common.setDepartmentId(role.getDepartment().getId().toString());
//        						model.addAttribute("departmentId", common.getDepartmentId());
//        						break;
//        					}
//        				}
//        			}
//        		}
//        	}
//        }
        Menu menu = departmentService.getMenuByUrl(request);
        if (menu != null) {
        	if (menu.getParentIds().split(",").length == 4) {
        		menu = menu.getParent();
        	} 
        	request.setAttribute("_m", menu.getParent() != null ? menu.getParent().getId() : null);
        	request.setAttribute("_mc", menu.getId());
        } 
	}
	
	/**
	 * 金额处理：金额千位符与金额多币种id和数值读取
	 * @param listin
	 */
	private void handlePrice(Map<Object, Object> listin) {
		
		
		//尾款：用于页面尾款支付
		Object payStatusObj = listin.get("payStatus");
		if (payStatusObj != null && 
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
	}
	
	/**
	 * 订单金额千位符处理：订单总金额、已付金额、到账金额
	 * @param listin
	 * @param paraList
	 */
	private void handlePrice(Map<Object, Object> listin, List<String> paraList) {
		
		//千位符
		DecimalFormat d = new DecimalFormat(",##0.00");
		if (CollectionUtils.isNotEmpty(paraList)) {
			for (String para : paraList) {
				if (listin.get(para) != null) {
					String moneyStr = listin.get(para).toString();
					String allMoney [] = moneyStr.split("\\+");
					
					if ( listin.get("totalMoney") != null && ("remainderMoney".equals(para))) {
						//获取总额、已付 字符串 eg: ￥ 200+$ 100 
						String totalStr = listin.get("totalMoney").toString();
						if (listin.get("payedMoney") == null) {
							String totalArr [] = totalStr.split("\\+");
							List<String> totalList = Lists.newArrayList(totalArr);
							String tempMoneyStr = "";
							for (int i = 0; i < totalList.size(); i++) {
								String money [] = totalList.get(i).split(" ");
								if (money.length > 0 && !"0.00".equals(money[1])) {
									tempMoneyStr += money[0] + d.format(new BigDecimal(money[1])) + "+";
								}
							}
							if(StringUtils.isNotBlank(tempMoneyStr)) {
								listin.put(para, tempMoneyStr.substring(0, tempMoneyStr.length()-1));
							}
						}else {							
							String payedStr = listin.get("payedMoney").toString();
							//拆分字符串，得到 币种，金额 Map
							String totalArr [] = totalStr.split("\\+");
							String payedArr [] = payedStr.split("\\+");
							List<String> totalList = Lists.newArrayList(totalArr);
							List<String> payedList = Lists.newArrayList(payedArr);
							String remainderString = getRemainderMoneyStr(totalList, payedList);
							listin.put(para, remainderString);
						}
					}else{
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
								String currencyMark = money[0].toString();
								String currencyMoney = money[1].toString();
								String moneyAmonut = d.format(new BigDecimal(currencyMoney));
								listin.put(para, currencyMark + moneyAmonut);
							}
						}
					}
				}
			}
		}
	}
	
	private String getRemainderMoneyStr(List<String> totalList, List<String> payedList){
		//千位符
		DecimalFormat d = new DecimalFormat(",##0.00");
		List<String> reusltList = moneyAmountService.subtract(totalList, payedList);
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
	 */
	private void getOrderLeftTime(Map<Object, Object> listin) {
		
		//判断剩余时间用激活时间：新需求（2014-07-10）
        String activationDateStr = String.valueOf(listin.get("activationDate"));
        String groupOpenDateStr = listin.get("groupOpenDate") + " 00:00:00";
        String proPayMode = String.valueOf(listin.get("proPayMode"));
        String proRemainDays = String.valueOf(listin.get("proRemainDays"));
        String payStatus = String.valueOf(listin.get("payStatus"));
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        //支付方式不为全款支付且订单支付状态不为申请退团、已取消、同步订单、已支付、已支付订金
        if (!proPayMode.equals("3") && StringUtils.isNotBlank(proRemainDays) && 
        		!"null".equals(proRemainDays) &&
        		!Context.ORDER_PAYSTATUS_YQX.equals(payStatus) &&
        		!Context.ORDER_PAYSTATUS_SYNC_CHECK.equals(payStatus) && !Context.ORDER_PAYSTATUS_YZF.equals(payStatus) &&
        		!Context.ORDER_PAYSTATUS_YZFDJ.equals(payStatus)) {
        	try {
        		// 激活日期
				Date activationDate = format.parse(activationDateStr);
				// 出团日期
				Date groupOpenDate = format.parse(groupOpenDateStr);
				
				// 设置激活日期、出团日期时间
				Calendar calForActivationDate = Calendar.getInstance();
				Calendar calForGroupOpenDate = Calendar.getInstance();
				calForActivationDate.setTime(activationDate);
				calForGroupOpenDate.setTime(groupOpenDate);
				Calendar currentDate = Calendar.getInstance();
				
				long curSubActTime = currentDate.getTimeInMillis() - calForActivationDate.getTimeInMillis();
				long openSubCurTime = calForGroupOpenDate.getTimeInMillis() - currentDate.getTimeInMillis();
				long remainsTime = (long) (Double.valueOf(proRemainDays) * (1000*3600*24));
				long offTime = remainsTime - curSubActTime;
				if (openSubCurTime < offTime) {
					offTime = openSubCurTime;
				}
				long distanceDays = offTime / (1000*3600*24);
				long distanceHours = (offTime - (distanceDays * (1000*3600*24))) / (1000*3600);
				long distanceMin = (offTime - (distanceDays * (1000*3600*24)) - (distanceHours * (1000*3600))) / (1000*60);
				if (offTime > 0) {
					listin.put("leftDays", distanceDays + "天" + distanceHours + "小时" + distanceMin + "分钟");
				} else {
					listin.put("leftDays", "无");
				}
			} catch (ParseException e) {
				logger.error("日期转换异常", e);
			}
        } else {
        	listin.put("leftDays", "无");
        }
	}
	
	/**
	 * @Description 财务提醒
	 * @author yakun.bai
	 * @Date 2016-4-11
	 */
	private void cwNotice(Map<Object, Object> orderMap, String orderType) {
		String orderId = orderMap.get("id").toString();
		String payStatus = orderMap.get("payStatus").toString();
		//达帐和撤销提示
        boolean isCanceledOrder = false;
        if (payStatus != null) {
        	isCanceledOrder = Context.ORDER_PAYSTATUS_YQX.equals(payStatus);
        }
        orderMap.put("promptStr", orderService.getOrderPrompt(orderType, Long.parseLong(orderId), isCanceledOrder));
	}
	
	
	@ResponseBody
    @RequestMapping(value ="getOrderRebates")
	public String getOrderRebates(String orderNo) {
		
		String rebates = "";
		
		List<String> orderRebatesList = orderCommonService.queryOrderRebates(orderNo);
		if (CollectionUtils.isNotEmpty(orderRebatesList)) {
			rebates += orderRebatesList.get(0) + ";";
		}
		orderRebatesList.addAll(orderCommonService.queryOrderRebates(orderNo));
		List<Map<String, Object>> oldRebatesInf= orderCommonService.queryOrderRebatesInf(orderNo);
		List<Map<String, Object>> newRebatesInf= orderCommonService.queryOrderNewRebatesInf(orderNo);
		String oldRebatesInfMoney = (String) oldRebatesInf.get(0).get("infbt");
		String newRebatesInfMoney = (String) newRebatesInf.get(0).get("infbt");

		String totalInfMoney = "";
		if(StringUtils.isNotBlank(oldRebatesInfMoney) && StringUtils.isNotBlank(newRebatesInfMoney)) {
			totalInfMoney = moneyAmountService.addOrSubtract(oldRebatesInfMoney, newRebatesInfMoney, true);
		} else if(StringUtils.isNotBlank(oldRebatesInfMoney) && StringUtils.isBlank(newRebatesInfMoney)) {
			totalInfMoney = oldRebatesInfMoney;
		} else if(StringUtils.isBlank(oldRebatesInfMoney) && StringUtils.isNotBlank(newRebatesInfMoney)) {
			totalInfMoney = newRebatesInfMoney;
		}

		rebates += totalInfMoney;

		return rebates;
	}
	
	@ResponseBody
    @RequestMapping(value ="transferCheck")
	public String transferCheck(String orderId) throws ParseException {
		
		String checkVal = "false";
		
		//转团转款标志位 1-可以转款  0-不可转款
    	ProductOrderCommon order = productOrderCommonDao.findOne(Long.parseLong(orderId));
    	if (order != null) {
    		if (CommonUtils.isNewReview(order.getCreateDate())) {
    			List<Review> list = reviewService.findReview(order.getOrderStatus(),Context.REVIEW_FLOWTYPE_TRANSFER_GROUP, orderId, Context.REVIEW_STATUS_DONE,Context.REVIEW_ACTIVE_EFFECTIVE);
            	if (list!=null&&list.size()>0){
            		checkVal = "true";
            	}
    		}
        	//查询新转团审核,用以判断是否可以转款
        	if (newTransferMoneyService.isTransferMoney(order)) {
        		checkVal = "true";
			}
    	}

		return checkVal;
	}
	
	@ResponseBody
	@RequestMapping(value ="getPayList")
	public Object getPayList(Model model,HttpServletRequest request){
		
		net.sf.json.JSONObject resobj = new net.sf.json.JSONObject();
	    String orderId = request.getParameter("orderId");
	    String orderType = request.getParameter("orderType");
	    if (StringUtils.isNotBlank(orderId) && StringUtils.isNotBlank(orderType)) {
	    	List<Map<String, Object>> orderpayList = orderService.findOrderpayByOrderIdAndType(Long.parseLong(orderId), Integer.parseInt(orderType));
	    	Integer isAsAccount = 0;// 空为未达帐 0为撤销 1为达帐 2为驳回
        	Integer isDJPayed = 0;// 订金是否已支付
        	Integer isQKPayed = 0;// 全款是否已支付
            boolean bCheckFlg = false;
            net.sf.json.JSONArray results = new net.sf.json.JSONArray();
            for (Map<String, Object> orderpay : orderpayList) {
                //如果orderpay的订单id  等于  pro的订单id
                    if (orderpay.get("moneySerialNum") != null) {
                    	//判断订单是否有达帐支付记录：用户退款判断
                    	if (isAsAccount != 1 && orderpay.get("isAsAccount") != null && orderpay.get("isAsAccount").toString().equals("1")) {
                    		isAsAccount = 1;
                    		resobj.put("isAsAccount", isAsAccount);
                    	}
                    	//判断订单是否已支付订金
                    	if (isDJPayed == 0 && orderpay.get("payPriceType").toString().equals(Context.ORDER_ORDERTYPE_ZFDJ)) {
                    		isDJPayed = 1;
                    		resobj.put("isDJPayed", isDJPayed);
                    	}
                    	//判断订单是否已支付全款
                    	if (isQKPayed == 0 && orderpay.get("payPriceType").toString().equals(Context.ORDER_ORDERTYPE_ZFQK)) {
                    		isQKPayed = 1;
                    		resobj.put("isQKPayed", isQKPayed);
                    	}
                    }
                    
                    if (!bCheckFlg) {
						if (orderpay.get("paymentStatus") != null && !orderpay.get("paymentStatus").toString().equals("0")) {
							resobj.put("paymentStatus", orderpay.get("paymentStatus").toString());
							bCheckFlg = true;
						}
					}
                    
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("payTypeName", orderpay.get("payTypeName"));
                    String payMoney = moneyAmountService.getMoneyStr(orderpay.get("moneySerialNum").toString());
                    jsonObject.put("payMoney", payMoney != null ? payMoney : "¥ 0.00");
                    
                	Object differenceUuid = orderpay.get("differenceUuid");
                    if (StringUtils.isNotBlank(differenceUuid.toString())) {
                    	ReturnDifference returnDifference = returnDifferenceService.getReturnDifferenceByUuid(differenceUuid.toString());
                    	if (null != returnDifference) {
                    		Currency currency = currencyService.findById(returnDifference.getCurrencyId().longValue());
                    		jsonObject.put("differenceMoney", returnDifference != null ? currency.getCurrencyMark() + " " +  returnDifference.getReturnPrice() : "¥ 0.00");
                    	} else {
                    		jsonObject.put("differenceMoney", "¥ 0.00");
                    	}
                    } else {
                    	jsonObject.put("differenceMoney", "");
                    }
                    
                    if (orderpay.get("createDate") != null) {
                    	jsonObject.put("createDate", orderpay.get("createDate").toString().substring(0, 19));
                    } else {
                    	jsonObject.put("createDate", "");
                    }
                    jsonObject.put("payPriceType", orderpay.get("payPriceType"));
                    jsonObject.put("isAsAccount", orderpay.get("isAsAccount"));
                    jsonObject.put("rejectReason", orderpay.get("rejectReason"));
                    jsonObject.put("payVoucher", orderpay.get("payVoucher"));
                    jsonObject.put("id", orderpay.get("id"));
                    results.add(jsonObject);
            }
            
            //支付信息
            resobj.put("orderPayList", results);
        }
	    
	    return resobj;
	}
	
}