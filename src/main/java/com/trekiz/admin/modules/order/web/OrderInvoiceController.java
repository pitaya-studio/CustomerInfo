package com.trekiz.admin.modules.order.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.invoice.entity.Orderinvoice;
import com.trekiz.admin.modules.invoice.service.OrderinvoiceService;
import com.trekiz.admin.modules.order.entity.OrderInvoiceVO;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.InvoiceService;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.receipt.service.OrderReceiptService;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.SysDefineDict;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.IVisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
/**
 * 订单发票类相关操作
 * @author ruyi.chen
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/orderInvoice/manage")
public class OrderInvoiceController extends BaseController{

	@Autowired
    private InvoiceService invoiceService;
	@Autowired
	private OrderinvoiceService orderinvoiceService;
	@Autowired
	private OrderReceiptService orderReceiptService;
	@Autowired
    private AgentinfoService agentinfoService;
	@Autowired
	private ActivityGroupService activityGroupService;
	@Autowired
	private IActivityAirTicketService activityAirTicketService;
	@Autowired
	private IVisaProductsService visaProductsService;
	@Autowired
	private OrderCommonService orderService;
	@Autowired
	private IAirTicketOrderService airticketOrderService;
	@Autowired
	private VisaOrderService visaOrderService;
	
	/**
	 * 跳转申请发票页面
	 * @author  chenry
	 * add Date 2014-11-13
	 */
	@SuppressWarnings("unused")
	@RequestMapping(value="applyInvoice")
	public String applyInvoice(Model model,HttpServletRequest request, HttpServletResponse response,String orderNum,String orderType,Long orderId) {
		
		String returnStr = "modules/order/invoice/applyInvoice";
        model.addAttribute("invoiceModes",DictUtils.getDictList(Context.INVOICE_MODE));
        List<Dict> dictList = DictUtils.getDictList(Context.INVOICE_TYPE);
        // 0411需求，针对越柬行踪,uuid=7a81b21a77a811e5bc1e000c29cf2586
        if(Context.SUPPLIER_UUID_YJXZ.equals(UserUtils.getUser().getCompany().getUuid())){
        	model.addAttribute("invoiceTypes", DictUtils.getDefineDictByCompanyUuidAndType(Context.INVOICE_TYPE, Context.SUPPLIER_UUID_YJXZ));
        }else{
        	model.addAttribute("invoiceTypes",DictUtils.getDictList(Context.INVOICE_TYPE));
        }
		model.addAttribute("invoiceSubjects",DictUtils.getDictList(Context.INVOICE_SUBJECT));
		
		// 客户南亚大自然使用自定义字典表中字段
		if (Context.SUPPLIER_UUID_NYDZR.equals(UserUtils.getUser().getCompany().getUuid())) {			
			model.addAttribute("invoiceSubjects", DictUtils.getDefineDictByCompanyUuidAndType(Context.INVOICE_SUBJECT, Context.SUPPLIER_UUID_NYDZR));
		}
		
		// 0453需求 客户为起航假期使用自定义字典表中字段
		if (Context.SUPPLIER_UUID_QHJQ.equals(UserUtils.getUser().getCompany().getUuid())) {			
			model.addAttribute("invoiceSubject_qhjq", DictUtils.getDefineDictByCompanyUuidAndType(Context.INVOICE_SUBJECT, Context.SUPPLIER_UUID_QHJQ));
		}
		// 0453需求 
		
		List<Agentinfo> invoiceCustomers = agentinfoService.findAllAgentinfo();
		model.addAttribute("invoiceCustomers", invoiceCustomers);
		Map<Object,Object> map=invoiceService.getApplyInvoiceInfo(orderNum,orderType);
		List<Map<Object,Object>>list=invoiceService.getInvoiceOrderInfoByGroup(orderId,orderType,orderNum);
		//C460V3 签证所涉及的团号均取自产品团号
		if("6".equals(orderType)){
			VisaOrder visaOrder = visaOrderService.findVisaOrder(orderId);
			VisaProducts visaProducts = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			model.addAttribute("groupCode", visaProducts.getGroupCode());
		}
		String companyUuid = UserUtils.getCompanyUuid();
		model.addAttribute("companyUUID",companyUuid);
		model.addAttribute("orderNum", orderNum);
		model.addAttribute("orderType", orderType);
		model.addAttribute("orderId",orderId);
		model.addAttribute("invoiceOrderInfo",map);
		model.addAttribute("invoiceOrderInfos", list);
		switch(orderType){
			case "6":
				returnStr="modules/order/invoice/applyVisaInvoice";
				break;
			case "7":
				returnStr="modules/order/invoice/applyAirInvoice";
				break;
			default :
				returnStr="modules/order/invoice/applyInvoice";
				break;
		}
		int length = DictUtils.getDictList(Context.INVOICE_SUBJECT).size();
		return returnStr;
	}
	
	/**
	 * 保存申请发票信息
	 * @author  chenry
	 * add Date 2014-11-13
	 */
	@ResponseBody
	@RequestMapping(value="saveApplyInvoice")
	public Integer saveApplyInvoice(Model model,HttpServletRequest request, HttpServletResponse response,OrderInvoiceVO orderInvoice) {
		/**
		 * update by ruyi.chen
		 * update date 2015-10-15
		 *  判断发票收据状态是否符合取消条件
		 *  return 1 保存成功  0订单已删除或者取消
		 */
		Integer sign = 0;
		boolean check = invoiceService.checkOrder(orderInvoice.getOrderId()[0].toString(),orderInvoice.getOrderType());
		if(check){
			sign=invoiceService.saveApplyInvoiceInfos(orderInvoice,orderInvoice.getOrderType());
		}
		
		return sign;
	}
	/**
	 * 订单发票信息
	 * @author  ruyi.chen 
	 * add Date 2014-11-14
	 */
	@RequestMapping(value={"viewInvoiceInfo/{uuid}/{verifyStatus}/{orderType}",""})
	public String viewInvoiceInfo(@PathVariable String uuid,@PathVariable String verifyStatus,@PathVariable String orderType, HttpServletRequest request, HttpServletResponse response,Model model) {
		
		List<String[]> limits = new ArrayList<String[]>();
		List<Orderinvoice> details = new ArrayList<Orderinvoice>();
		limits = orderinvoiceService.findSupplyLimitDetailList(uuid, UserUtils.getUser().getCompany().getId());
		//转换订单类型字符串20150720
		//by sy20150720
		
		if(null != limits && limits.size() > 0) {
			for(Object[] m:limits){
				//转化团队类型20150720
				String ordertype = m[12].toString();
				if(StringUtils.isNotBlank(ordertype)) {
					String order_type = OrderCommonUtil.getChineseOrderType(ordertype);
					m[12] = order_type;
				}else {
					m[12] = "";
				}
			}
		}
		
		details = orderinvoiceService.findInvoiceDetails(uuid, UserUtils.getUser().getCompany().getId());
		BigDecimal moneyStr=new BigDecimal(0);
		if(null!=details&&details.size()>0){
			for(Orderinvoice o:details){
				moneyStr=StringNumFormat.getBigDecimalAdd(moneyStr, o.getInvoiceAmount());
			}
		}
		model.addAttribute("companyUUid",UserUtils.getCompanyUuid());
		model.addAttribute("invoiceMoney",moneyStr.toString());
		model.addAttribute("verifyStatus", verifyStatus);
		model.addAttribute("limits", limits);
		model.addAttribute("details", details);
		model.addAttribute("invoiceModes", DictUtils.getKeyIntMap(Context.INVOICE_MODE));//开票方式
		// 0411需求，针对越柬行踪,uuid=7a81b21a77a811e5bc1e000c29cf2586
        if(Context.SUPPLIER_UUID_YJXZ.equals(UserUtils.getUser().getCompany().getUuid())){
        	// 将list转map
        	List<SysDefineDict> sysDefineDictList = DictUtils.getDefineDictByCompanyUuidAndType(Context.INVOICE_TYPE, Context.SUPPLIER_UUID_YJXZ);
        	Map<Integer, String> dicMap = new HashMap<Integer, String>();
    		if(sysDefineDictList!=null){
    			for(SysDefineDict dict:sysDefineDictList){
    				dicMap.put(Integer.parseInt(dict.getValue()), dict.getLabel());
    			}
    		}
        	model.addAttribute("invoiceTypes", dicMap);//开票类型，针对越柬行踪
        }else{
        	model.addAttribute("invoiceTypes", DictUtils.getKeyIntMap(Context.INVOICE_TYPE));//开票类型
        }
		if (Context.SUPPLIER_UUID_NYDZR.equals(UserUtils.getUser().getCompany().getUuid())) {	
			model.addAttribute("invoiceSubjects", DictUtils.getKeyIntMapFromDefinedDict(Context.INVOICE_SUBJECT,UserUtils.getUser().getCompany().getUuid()));//开票项目
		}else if(Context.SUPPLIER_UUID_QHJQ.equals(UserUtils.getUser().getCompany().getUuid())){
			// 0453针对起航假期
			model.addAttribute("invoiceSubjects", DictUtils.getKeyIntMapFromDefinedDict(Context.INVOICE_SUBJECT, Context.SUPPLIER_UUID_QHJQ));//开票项目
		}else{
			model.addAttribute("invoiceSubjects", DictUtils.getKeyIntMap(Context.INVOICE_SUBJECT));//开票项目
		}
		
		
		return "modules/order/invoice/viewInvoiceInfo";
	}
	
	/**
	 * 获取订单发票信息列表
	 * @author  chenry
	 * add Date 2014-11-14
	 */
	@RequestMapping(value="getInvoiceListByOrderNum")
	public String getInvoiceListByOrderNum(HttpServletRequest request, HttpServletResponse response, @ModelAttribute TravelActivity travelActivity,String orderNum,String orderType,Long orderId,Long activityId, Model model){
		
		// 获取对应产品
		Integer lockStatus = 0;
		if (Context.PRODUCT_TYPE_AIRTICKET.toString().equals(orderType)) {
			AirticketOrder order = airticketOrderService.getAirticketorderById(orderId);
			if (order != null) {
				ActivityAirTicket activityAirTicket = activityAirTicketService.getActivityAirTicketById(order.getAirticketId());
				if (activityAirTicket != null) {
					lockStatus = activityAirTicket.getLockStatus();
				}
			}
		} else if (Context.PRODUCT_TYPE_QIAN_ZHENG.toString().equals(orderType)) {
			VisaOrder visaOrder = visaOrderService.findVisaOrder(orderId);
			if (visaOrder != null) {
				VisaProducts visaProduct = visaProductsService.findById(visaOrder.getVisaProductId());
				if (visaProduct != null) {
					lockStatus = visaProduct.getLockStatus();
				}
			}
		} else {
			ProductOrderCommon order = orderService.getProductorderById(orderId);
			if (order != null) {
				ActivityGroup activityGroup = activityGroupService.findById(order.getProductGroupId());
				if (activityGroup != null) {
					lockStatus = activityGroup.getLockStatus();
				}
			}
		}
		
		//查询条件
        Map<String,String> mapRequest = new HashMap<String,String>();
        mapRequest.put("orderNum", orderNum);
        mapRequest.put("orderType", orderType);
        Page<Map<Object, Object>> newPage=new Page<Map<Object, Object>>(request, response);
        newPage.setPageSize(10000);
        Page<Map<Object, Object>> page=orderinvoiceService.getSupplyinvoiceList(newPage,"",mapRequest,UserUtils.getUser().getCompany().getId(),travelActivity);
//        List<ProductOrderCommon> p=orderService.findByOrderNum(orderNum);
        model.addAttribute("invoiceModes", DictUtils.getSysDicMap(Context.INVOICE_MODE));
		model.addAttribute("invoiceTypes", DictUtils.getSysDicMap(Context.INVOICE_TYPE));
		model.addAttribute("invoiceSubjects", DictUtils.getSysDicMap(Context.INVOICE_SUBJECT));
		//产品系列
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("page", page);
		model.addAttribute("orderNum",orderNum);
		model.addAttribute("orderType",orderType);
		model.addAttribute("orderId",orderId);
		model.addAttribute("lockStatus",lockStatus);
		model.addAttribute("activityId",activityId);
		Integer sign = invoiceService.getApplyInvoiceCheck(orderId, orderType);
		model.addAttribute("applySign",sign);
		return "modules/order/invoice/viewInvoiceList";
	}
	
	
	
	/**
	 * 获取订单发票信息列表
	 * @author  chenry
	 * add Date 2014-11-14
	 */
	
	@RequestMapping(value={"supplyreceiptlist"})
	public String invoiceLists( HttpServletRequest request, HttpServletResponse response, 
			@ModelAttribute TravelActivity travelActivity,String orderNum,String orderType,
			Long orderId, Long activityId, Model model) {
		
		// 获取对应产品
		Integer lockStatus = 0;
		if (Context.PRODUCT_TYPE_AIRTICKET.toString().equals(orderType)) {
			AirticketOrder order = airticketOrderService.getAirticketorderById(orderId);
			if (order != null) {
				ActivityAirTicket activityAirTicket = activityAirTicketService.getActivityAirTicketById(order.getAirticketId());
				if (activityAirTicket != null) {
					lockStatus = activityAirTicket.getLockStatus();
				}
			}
		} else if (Context.PRODUCT_TYPE_QIAN_ZHENG.toString().equals(orderType)) {
			VisaOrder visaOrder = visaOrderService.findVisaOrder(orderId);
			if (visaOrder != null) {
				VisaProducts visaProduct = visaProductsService.findById(visaOrder.getVisaProductId());
				if (visaProduct != null) {
					lockStatus = visaProduct.getLockStatus();
				}
			}
		} else {
			ProductOrderCommon order = orderService.getProductorderById(orderId);
			if (order != null) {
				ActivityGroup activityGroup = activityGroupService.findById(order.getProductId());
				if (activityGroup != null) {
					lockStatus = activityGroup.getLockStatus();
				}
			}
		}

		String verifyStatus = request.getParameter("verifyStatus");

		Page<Map<Object, Object>> pageOrder = new Page<Map<Object, Object>>(request, response);
//		//查询条件
        Map<String,String> mapRequest = new HashMap<String,String>();
        
        mapRequest.put("orderNum", orderNum);
        mapRequest.put("orderId", String.valueOf(orderId));
        mapRequest.put("orderType", orderType);
        List<Map<Object, Object>>  ls= orderReceiptService.getSupplyinvoiceLists(pageOrder, verifyStatus, mapRequest, UserUtils.getUser().getCompany().getId(),travelActivity);
		model.addAttribute("page", ls);
        
		//开票类型
		model.addAttribute("invoiceTypes", DictUtils.getDictList("invoice_type"));
//		//开票项目
		model.addAttribute("invoiceSubjects", DictUtils.getDictList("invoice_subject"));
//		//开票方式
		model.addAttribute("invoiceModes", DictUtils.getDictList("invoice_mode"));
		model.addAttribute("orderNum",orderNum);
		model.addAttribute("orderType",orderType);
		model.addAttribute("orderId",orderId);
		model.addAttribute("lockStatus",lockStatus);
		model.addAttribute("activityId",activityId);
		return "modules/order/invoice/receiptRecords";
		
	}
	
	
	
	
	
	
	
	/**
	 * 获取合开发票订单信息,包含支付记录等
	 * @author  chenry
	 * add Date 2014-11-19
	 */
	@ResponseBody
	@RequestMapping(value="getOrderOpenInvoiceInfo")
	public Map<Object,Object> getOrderOpenInvoiceInfo(Model model,HttpServletRequest request, 
			HttpServletResponse response,String orderNum,String orderType,String salerId) {
		Map<Object,Object> map=invoiceService.getApplyInvoiceInfoForAdd(orderNum,orderType,salerId);
		return map;
	}
	/**
	 * 跳转申请机票发票页面
	 * @author  chenry
	 * add Date 2014-11-13
	 */
	@RequestMapping(value="applyAirInvoice")
	public String applyAirInvoice(Model model,HttpServletRequest request, HttpServletResponse response,String orderNum,String orderType,Long orderId) {
		
		
        model.addAttribute("invoiceModes",DictUtils.getDictList(Context.INVOICE_MODE));
		model.addAttribute("invoiceTypes",DictUtils.getDictList(Context.INVOICE_TYPE));
		model.addAttribute("invoiceSubjects",DictUtils.getDictList(Context.INVOICE_SUBJECT));
		Map<Object,Object> map=invoiceService.getApplyInvoiceInfo(orderNum,Context.ORDER_STATUS_AIR_TICKET);
		
		model.addAttribute("orderNum", orderNum);
		model.addAttribute("orderType", orderType);
		model.addAttribute("orderId",orderId);
		model.addAttribute("invoiceOrderInfo",map);
		
		return "modules/order/invoice/applyAirInvoice";
	}
	
	/**
	 * 保存机票申请发票信息
	 * @author  chenry
	 * add Date 2014-11-13
	 */
	@ResponseBody
	@RequestMapping(value="saveAirApplyInvoice")
	public Integer saveAirApplyInvoice(Model model,HttpServletRequest request, HttpServletResponse response,OrderInvoiceVO orderInvoice) {
		
		Integer sign=invoiceService.saveApplyInvoiceInfos(orderInvoice,Context.ORDER_STATUS_AIR_TICKET);
		return sign;
	}
	/**
	 * 订单发票信息
	 * @author  chenry
	 * add Date 2014-11-14
	 */
	@RequestMapping(value={"viewAirInvoiceInfo/{invoiceNum}/{verifyStatus}{orderType}",""})
	public String viewAirInvoiceInfo(@PathVariable String invoiceNum,@PathVariable String verifyStatus,@PathVariable String orderType, HttpServletRequest request, HttpServletResponse response,Model model) {
		
		List<String[]> limits = new ArrayList<String[]>();
		List<Orderinvoice> details = new ArrayList<Orderinvoice>();
		limits = orderinvoiceService.findSupplyLimitDetailList(invoiceNum, UserUtils.getUser().getCompany().getId());
		details = orderinvoiceService.findInvoiceDetails(invoiceNum, UserUtils.getUser().getCompany().getId());
		BigDecimal moneyStr=BigDecimal.valueOf(0);
		if(null!=details&&details.size()>0){
			for(Orderinvoice o:details){
				moneyStr=StringNumFormat.getBigDecimalAdd(moneyStr, o.getInvoiceAmount());
			}
		}
		model.addAttribute("invoiceMoney",moneyStr);
		model.addAttribute("verifyStatus", verifyStatus);
		model.addAttribute("limits", limits);
		model.addAttribute("details", details);
		model.addAttribute("invoiceModes", DictUtils.getKeyIntMap(Context.INVOICE_MODE));//开票方式
		model.addAttribute("invoiceTypes", DictUtils.getKeyIntMap(Context.INVOICE_TYPE));//开票类型
		model.addAttribute("invoiceSubjects", DictUtils.getKeyIntMap(Context.INVOICE_SUBJECT));//开票项目
		return "modules/order/invoice/viewAirInvoiceInfo";
	}
	
	/**
	 * 获取机票订单发票信息列表
	 * @author  chenry
	 * add Date 2014-11-14
	 */
	@RequestMapping(value="getAirInvoiceListByOrderNum")
	public String getAirInvoiceListByOrderNum(HttpServletRequest request, HttpServletResponse response, @ModelAttribute TravelActivity travelActivity,Long orderId,String orderNum, Model model){
		
		//查询条件
        Map<String,String> mapRequest = new HashMap<String,String>();
        mapRequest.put("orderNum", orderNum);
        mapRequest.put("orderType", Context.ORDER_STATUS_AIR_TICKET);
        Page<Map<Object, Object>> newPage=new Page<Map<Object, Object>>(request, response);
        newPage.setPageSize(10000);
        Page<Map<Object, Object>> page=orderinvoiceService.getSupplyinvoiceList(newPage,"",mapRequest,UserUtils.getUser().getCompany().getId(),travelActivity);
        model.addAttribute("invoiceModes", DictUtils.getSysDicMap(Context.INVOICE_MODE));
		model.addAttribute("invoiceTypes", DictUtils.getSysDicMap(Context.INVOICE_TYPE));
		model.addAttribute("invoiceSubjects", DictUtils.getSysDicMap(Context.INVOICE_SUBJECT));
		//产品系列
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("page", page);
		model.addAttribute("orderNum",orderNum);
		model.addAttribute("orderId",orderId);
		model.addAttribute("orderType",Context.ORDER_STATUS_AIR_TICKET);
		Integer sign = invoiceService.getApplyInvoiceCheck(orderId, Context.ORDER_STATUS_AIR_TICKET);
		model.addAttribute("applySign",sign);
		return "modules/order/invoice/viewAirInvoiceList";
	}
	/**
	 * 获取合开发票订单信息,包含支付记录等
	 * @author  chenry
	 * add Date 2014-11-19
	 */
	@ResponseBody
	@RequestMapping(value="getAirOrderOpenInvoiceInfo")
	public Map<Object,Object> getAirOrderOpenInvoiceInfo(Model model,HttpServletRequest request, HttpServletResponse response,String orderNum,String salerId) {
		Map<Object,Object> map=invoiceService.getApplyInvoiceInfoForAdd(orderNum, Context.ORDER_STATUS_AIR_TICKET,salerId);
		return map;
		
	}
	/**
	 * 跳转申请签证发票页面
	 * @author  chenry
	 * add Date 2014-11-13
	 */
	@RequestMapping(value="applyVisaInvoice")
	public String applyVisaInvoice(Model model,HttpServletRequest request, HttpServletResponse response,String orderNum,String orderType,Long orderId) {
		
		
        model.addAttribute("invoiceModes",DictUtils.getDictList(Context.INVOICE_MODE));
		model.addAttribute("invoiceTypes",DictUtils.getDictList(Context.INVOICE_TYPE));
		model.addAttribute("invoiceSubjects",DictUtils.getDictList(Context.INVOICE_SUBJECT));
		Map<Object,Object> map = invoiceService.getApplyInvoiceInfo(orderNum, Context.ORDER_STATUS_VISA);
		
		model.addAttribute("orderNum", orderNum);
		model.addAttribute("orderType", orderType);
		model.addAttribute("orderId",orderId);
		model.addAttribute("invoiceOrderInfo",map);
		model.addAttribute("orderId",orderId);
		
		return "modules/order/invoice/applyVisaInvoice";
	}
	
	/**
	 * 保存签证类申请发票信息
	 * @author  chenry
	 * add Date 2014-11-13
	 */
	@ResponseBody
	@RequestMapping(value="saveVisaApplyInvoice")
	public Integer saveVisaApplyInvoice(Model model,HttpServletRequest request, HttpServletResponse response,OrderInvoiceVO orderInvoice) {
		
		Integer sign=invoiceService.saveApplyInvoiceInfos(orderInvoice, Context.ORDER_STATUS_VISA);
		return sign;
	}
	/**
	 * 签证订单发票信息
	 * @author  chenry
	 * add Date 2014-11-14
	 */
	@RequestMapping(value={"viewVisaInvoiceInfo/{invoiceNum}/{verifyStatus}/{orderType}",""})
	public String viewVisaInvoiceInfo(@PathVariable String invoiceNum,@PathVariable String verifyStatus,@PathVariable String orderType, HttpServletRequest request, HttpServletResponse response,Model model) {
		
		List<String[]> limits = new ArrayList<String[]>();
		List<Orderinvoice> details = new ArrayList<Orderinvoice>();
		limits = orderinvoiceService.findSupplyLimitDetailList(invoiceNum, UserUtils.getUser().getCompany().getId());
		details = orderinvoiceService.findInvoiceDetails(invoiceNum, UserUtils.getUser().getCompany().getId());
		BigDecimal moneyStr=BigDecimal.valueOf(0);
		if(null!=details&&details.size()>0){
			for(Orderinvoice o:details){
				moneyStr=StringNumFormat.getBigDecimalAdd(moneyStr, o.getInvoiceAmount());
			}
		}
		model.addAttribute("invoiceMoney",moneyStr);
		model.addAttribute("verifyStatus", verifyStatus);
		model.addAttribute("limits", limits);
		model.addAttribute("details", details);
		model.addAttribute("invoiceModes", DictUtils.getKeyIntMap(Context.INVOICE_MODE));//开票方式
		model.addAttribute("invoiceTypes", DictUtils.getKeyIntMap(Context.INVOICE_TYPE));//开票类型
		model.addAttribute("invoiceSubjects", DictUtils.getKeyIntMap(Context.INVOICE_SUBJECT));//开票项目
		return "modules/order/invoice/viewVisaInvoiceInfo";
	}
	
	/**
	 * 获取签证订单发票信息列表
	 * @author  chenry
	 * add Date 2014-11-14
	 */
	@RequestMapping(value="getVisaInvoiceListByOrderNum")
	public String getVisaInvoiceListByOrderNum(HttpServletRequest request, HttpServletResponse response, @ModelAttribute TravelActivity travelActivity,String orderNum, Model model,Long orderId){
		
		//查询条件
        Map<String,String> mapRequest = new HashMap<String,String>();
        mapRequest.put("orderNum", orderNum);
        mapRequest.put("orderType", Context.ORDER_STATUS_VISA);
        Page<Map<Object, Object>> newPage=new Page<Map<Object, Object>>(request, response);
        newPage.setPageSize(10000);
        Page<Map<Object, Object>> page=orderinvoiceService.getSupplyinvoiceList(newPage,"",mapRequest,UserUtils.getUser().getCompany().getId(),travelActivity);
        model.addAttribute("invoiceModes", DictUtils.getSysDicMap(Context.INVOICE_MODE));
		model.addAttribute("invoiceTypes", DictUtils.getSysDicMap(Context.INVOICE_TYPE));
		model.addAttribute("invoiceSubjects", DictUtils.getSysDicMap(Context.INVOICE_SUBJECT));
		//产品系列
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("page", page);
		model.addAttribute("orderNum",orderNum);
		model.addAttribute("orderId",orderId);
		model.addAttribute("orderType",Context.ORDER_STATUS_VISA);
		Integer sign = invoiceService.getApplyInvoiceCheck(orderId, Context.ORDER_STATUS_VISA);
		model.addAttribute("applySign",sign);
		return "modules/order/invoice/viewVisaInvoiceList";
	}
	/**
	 * 获取签证合开发票订单信息
	 * @author  chenry
	 * add Date 2014-11-27
	 */
	@ResponseBody
	@RequestMapping(value="getVisaOrderOpenInvoiceInfo")
	public Map<Object,Object> getVisaOrderOpenInvoiceInfo(Model model,HttpServletRequest request, HttpServletResponse response,String orderNum,String salerId) {
		Map<Object,Object> map=invoiceService.getApplyInvoiceInfoForAdd(orderNum, Context.ORDER_STATUS_VISA,salerId);
		return map;
		
	}
	
	// validateOrder
	/**
	 * 获取签证订单发票信息列表
	 * @author  xudong.he
	 * add Date 2014-11-14
	 */
	@RequestMapping(value="validateOrder")
	@ResponseBody
	public Object validateOrder (HttpServletRequest request, HttpServletResponse response){
		Map<Object, String> map = new HashMap<Object, String>();
		
		String orderNum = request.getParameter("orderNum");
		String orderId = request.getParameter("orderId");
		String orderType = request.getParameter("orderType");
		boolean check = invoiceService.checkOrder(orderId,orderType);
		if(check == true){
			// 474需求  判断是否解除发票申请限制
			if(UserUtils.getUser().getCompany().getIsRemoveApplyInvoiceLimit() == 1){
				map.put("msg", "success");
				return map;
			}
			// 474需求  判断是否解除发票申请限制
			boolean flag = 	invoiceService.validateOrder(orderNum, orderType);
			if(flag == true)
				map.put("msg", "success");
			else{
				map.put("msg", "fail"); 
			}
		}else{
			map.put("msg", "canOrDel");
		}
		return map;
	}
	
}
