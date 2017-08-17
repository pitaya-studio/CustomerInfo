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
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.invoice.entity.Orderinvoice;
import com.trekiz.admin.modules.invoice.service.OrderinvoiceService;
import com.trekiz.admin.modules.order.entity.OrderInvoiceVO;
import com.trekiz.admin.modules.order.service.InvoiceService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
/**
 * 订单收据类相关操作
 * @author ruyi.chen
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/orderReceipt/manage")
public class OrderReceiptController extends BaseController{

	@Autowired
    private InvoiceService invoiceService;
	@Autowired
	private OrderinvoiceService orderinvoiceService;
	@Autowired
    private AgentinfoService agentinfoService;
	@Autowired
	private VisaOrderService visaOrderService;
	@Autowired
	private VisaProductsService visaProductsService;
	/**
	 * 跳转申请开收据页面
	 * @author  chenry
	 * add Date 2015-07-07
	 */
	@RequestMapping(value="applyReceipt")
	public String applyReceipt(Model model,HttpServletRequest request, HttpServletResponse response,String orderNum,String orderType,Long orderId) {
		
		String returnStr = "modules/order/receipt/applyReceipt";
        model.addAttribute("invoiceModes",DictUtils.getDictList(Context.INVOICE_MODE));
		model.addAttribute("invoiceTypes",DictUtils.getDictList(Context.INVOICE_TYPE));
		model.addAttribute("invoiceSubjects",DictUtils.getDictList(Context.INVOICE_SUBJECT));
		// 客户南亚大自然使用自定义字典表中字段
		if (Context.SUPPLIER_UUID_NYDZR.equals(UserUtils.getUser().getCompany().getUuid())) {			
			model.addAttribute("invoiceSubjects",DictUtils.getDefineDictByCompanyUuidAndType(Context.INVOICE_SUBJECT, Context.SUPPLIER_UUID_NYDZR));
		}
		
		List<Agentinfo> invoiceCustomers = agentinfoService.findAllAgentinfo();
		model.addAttribute("invoiceCustomers", invoiceCustomers);
		Map<Object,Object> map=invoiceService.getApplyReceiptInfo(orderNum,orderType);
		List<Map<Object,Object>>list=invoiceService.getReceiptOrderInfoByGroup(orderId,orderType,orderNum);
		//C460V3 签证所涉及的团号均取自产品团号
		if("6".equals(orderType)){
			VisaOrder visaOrder = visaOrderService.findVisaOrder(orderId);
			VisaProducts visaProducts = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			model.addAttribute("groupCode", visaProducts.getGroupCode());
		}
		
		model.addAttribute("orderNum", orderNum);
		model.addAttribute("orderType", orderType);
		model.addAttribute("orderId",orderId);
		model.addAttribute("invoiceOrderInfo",map);
		model.addAttribute("invoiceOrderInfos", list);
		switch(orderType){
			case "6":
			returnStr="modules/order/receipt/applyVisaReceipt";
				break;
			case "7":
				returnStr="modules/order/receipt/applyAirReceipt";
				break;
			default :
				returnStr="modules/order/receipt/applyReceipt";
				break;
		}
		return returnStr;
	}
	
	/**
	 * 保存申请收据信息
	 * @author  chenry
	 * add Date 2014-11-13
	 */
	@ResponseBody
	@RequestMapping(value="saveApplyReceipt")
	public Integer saveApplyReceipt(Model model,HttpServletRequest request, HttpServletResponse response,OrderInvoiceVO orderInvoice) {
		/**
		 * update by ruyi.chen
		 * update date 2015-10-15
		 *  判断发票收据状态是否符合取消条件
		 *  return 1 保存成功  0订单已删除或者取消
		 */
		Integer sign = 0;
		boolean check = invoiceService.checkOrder(orderInvoice.getOrderId()[0].toString(),orderInvoice.getOrderType());
		if(check){
			sign=invoiceService.saveApplyReceiptInfos(orderInvoice,orderInvoice.getOrderType());
		}
		return sign;
	}
	/**
	 * 订单收据信息
	 * @author  ruyi.chen 
	 * add Date 2014-11-14
	 */
	@RequestMapping(value={"viewReceiptInfo/{invoiceNum}/{verifyStatus}/{orderType}",""})
	public String viewReceiptInfo(@PathVariable String invoiceNum,@PathVariable String verifyStatus,@PathVariable String orderType, HttpServletRequest request, HttpServletResponse response,Model model) {
		
		List<String[]> limits = new ArrayList<String[]>();
		List<Orderinvoice> details = new ArrayList<Orderinvoice>();
		limits = orderinvoiceService.findSupplyLimitDetailList(invoiceNum, UserUtils.getUser().getCompany().getId());
		details = orderinvoiceService.findInvoiceDetails(invoiceNum, UserUtils.getUser().getCompany().getId());
		BigDecimal moneyStr=new BigDecimal(0);
		if(null!=details&&details.size()>0){
			for(Orderinvoice o:details){
				moneyStr=StringNumFormat.getBigDecimalAdd(moneyStr, o.getInvoiceAmount());
			}
		}
		model.addAttribute("invoiceMoney",moneyStr.toString());
		model.addAttribute("verifyStatus", verifyStatus);
		model.addAttribute("limits", limits);
		model.addAttribute("details", details);
		model.addAttribute("invoiceModes", DictUtils.getKeyIntMap(Context.INVOICE_MODE));//开票方式
		model.addAttribute("invoiceTypes", DictUtils.getKeyIntMap(Context.INVOICE_TYPE));//开票类型
		model.addAttribute("invoiceSubjects", DictUtils.getKeyIntMap(Context.INVOICE_SUBJECT));//开票项目
		return "modules/order/receipt/viewReceiptInfo";
	}
	
	/**
	 * 获取订单发票信息列表
	 * @author  chenry
	 * add Date 2014-11-14
	 */
	@RequestMapping(value="getReceiptListByOrderNum")
	public String getReceiptListByOrderNum(HttpServletRequest request, HttpServletResponse response, @ModelAttribute TravelActivity travelActivity,String orderNum,String orderType,Long orderId, Model model){
		
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
		Integer sign = invoiceService.getApplyInvoiceCheck(orderId, orderType);
		model.addAttribute("applySign",sign);
		return "modules/order/receipt/viewReceiptList";
	}
	/**
	 * 获取合开发票订单信息,包含支付记录等
	 * @author  chenry
	 * add Date 2014-11-19
	 */
	@ResponseBody
	@RequestMapping(value="getOrderOpenReceiptInfo")
	public Map<Object,Object> getOrderOpenReceiptInfo(Model model,HttpServletRequest request, HttpServletResponse response,String orderNum,String orderType,String salerId) {
		Map<Object,Object> map=invoiceService.getApplyReceiptInfoForAdd(orderNum,orderType,salerId);
//		if(map!=null&&map.size()>0){
//			JSONArray js=JSONArray.fromObject(map);
//			model.addAttribute("invoiceOrderInfo",js);
//			return map;
//		}else{
//			return null;
//		}
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
//		List<Map<Object,Object>>list=invoiceService.getInvoiceOrderInfoByGroup(orderId,Context.ORDER_STATUS_AIR_TICKET);
		
		model.addAttribute("orderNum", orderNum);
		model.addAttribute("orderType", orderType);
		model.addAttribute("orderId",orderId);
		model.addAttribute("invoiceOrderInfo",map);
//		model.addAttribute("invoiceOrderInfos", list);
		
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
		Map<Object,Object> map=invoiceService.getApplyInvoiceInfo(orderNum, Context.ORDER_STATUS_VISA);
//		List<Map<Object,Object>>list=invoiceService.getInvoiceOrderInfoByGroup(orderId, Context.ORDER_STATUS_VISA);
		
		model.addAttribute("orderNum", orderNum);
		model.addAttribute("orderType", orderType);
		model.addAttribute("orderId",orderId);
		model.addAttribute("invoiceOrderInfo",map);
//		model.addAttribute("invoiceOrderInfos", list);
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
	/**
	 * 跳转海岛游申请开收据页面
	 * @author  chenry
	 * add Date 2015-07-07
	 */
	@RequestMapping(value="applyIslandReceipt")
	public String applyIslandReceipt(Model model,HttpServletRequest request, HttpServletResponse response,String orderNum,String orderType,Long orderId) {
		
		
        model.addAttribute("invoiceModes",DictUtils.getDictList(Context.INVOICE_MODE));
		model.addAttribute("invoiceTypes",DictUtils.getDictList(Context.INVOICE_TYPE));
		model.addAttribute("invoiceSubjects",DictUtils.getDictList(Context.INVOICE_SUBJECT));
		Map<Object,Object> map=invoiceService.getApplyInvoiceInfo(orderNum,orderType);
		List<Map<Object,Object>>list=invoiceService.getInvoiceOrderInfoByGroup(orderId,orderType,orderNum);
		
		model.addAttribute("orderNum", orderNum);
		model.addAttribute("orderType", orderType);
		model.addAttribute("orderId",orderId);
		model.addAttribute("invoiceOrderInfo",map);
		model.addAttribute("invoiceOrderInfos", list);
		
		return "modules/order/receipt/applyIslandReceipt";
	}
	/**
	 * 跳转酒店申请开收据页面
	 * @author  chenry
	 * add Date 2015-07-07
	 */
	@RequestMapping(value="applyHotelReceipt")
	public String applyHotelReceipt(Model model,HttpServletRequest request, HttpServletResponse response,String orderNum,String orderType,Long orderId) {
		
		
        model.addAttribute("invoiceModes",DictUtils.getDictList(Context.INVOICE_MODE));
		model.addAttribute("invoiceTypes",DictUtils.getDictList(Context.INVOICE_TYPE));
		model.addAttribute("invoiceSubjects",DictUtils.getDictList(Context.INVOICE_SUBJECT));
		Map<Object,Object> map=invoiceService.getApplyInvoiceInfo(orderNum,orderType);
		List<Map<Object,Object>>list=invoiceService.getInvoiceOrderInfoByGroup(orderId,orderType,orderNum);
		
		model.addAttribute("orderNum", orderNum);
		model.addAttribute("orderType", orderType);
		model.addAttribute("orderId",orderId);
		model.addAttribute("invoiceOrderInfo",map);
		model.addAttribute("invoiceOrderInfos", list);
		
		return "modules/order/receipt/applyHotelReceipt";
	}
}
