package com.trekiz.admin.modules.invoice.web;

import java.io.UnsupportedEncodingException;
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

import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.invoice.entity.Orderinvoice;
import com.trekiz.admin.modules.invoice.service.OrderinvoiceService;
import com.trekiz.admin.modules.invoice.utils.InvoiceExcelUtils;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 开发票的额度控制
 * @author Administrator
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/invoice/limit")
public class SupplyInvoicelimitController extends BaseController {

	@Autowired
	private OrderinvoiceService orderinvoiceService;
	
	@Autowired
    private CurrencyService currencyService;
	@Autowired
    private AgentinfoService agentinfoService;

	@ModelAttribute("menuId")
	protected Integer getMenuId(){
		return 90;
	}
	
	/**
	 * 审核开发票通过
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException 
	 */
    @RequestMapping(value={"verifyinvoice/{uuid}"})
	public String verifyinvoice(@PathVariable String uuid,HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException{
		String createInvoiceHid = request.getParameter("createInvoiceHid");
		String receiveInvoiceHid = request.getParameter("receiveInvoiceHid");
		String invoiceNumber = request.getParameter("invoiceNum");
		//20150805添加开票备注
		String invoiceRemark = request.getParameter("invoiceRemark");
		//20151016 C301需求， 添加审核人备注
		String reviewRemark = request.getParameter("reviewRemark");
		//20160426 0413需求    添加开票时间  王洋
		String invoiceTimeString = request.getParameter("invoiceTime");
		Date invoiceTime;
		if(StringUtils.isBlank(invoiceTimeString)){
			invoiceTime = new Date();
		}else{
			invoiceTime = new SimpleDateFormat("yyyy-MM-dd").parse(invoiceTimeString);
		}
		List<Orderinvoice> orderinvoiceList = orderinvoiceService.findInvoiceDetails(uuid, UserUtils.getUser().getCompany().getId());	
		//开票操作
		if("true".equals(createInvoiceHid)) {
			if(orderinvoiceList.size() > 0) {
				for (Orderinvoice orderinvoice : orderinvoiceList) {
					//更新开票状态
					orderinvoiceService.updateOrderinvoiceCreateStatus(uuid, invoiceNumber,invoiceRemark, 1, invoiceTime);
					//拼装发票领取成功日志信息
					StringBuffer sb = new StringBuffer("订单号:");
					sb.append(orderinvoice.getOrderNum()).append("  ").append(DateUtils.formatCustomDate(invoiceTime, "yyyy-MM-dd HH:mm:ss"))
					.append(" ").append(UserUtils.getUser().getName()).append("开票金额").append(orderinvoice.getCheckAmount())
					.append(" ").append("发票号：").append(invoiceNumber);
					//保存操作日志
					saveUserOpeLog("5", "财务", sb.toString(), "3", orderinvoice.getOrderType(), orderinvoice.getOrderId().longValue());
				}
			}
			//2015.06.15 发票管理 --> 开票 返回成功之后，回到开票页面
			return "redirect:"+Global.getAdminPath()+"/invoice/limit/supplyinvoicelist?verifyStatus=1";
		}else {
			if("true".equals(receiveInvoiceHid)) {
				if(orderinvoiceList.size() > 0) {
					for (Orderinvoice orderinvoice : orderinvoiceList) {
						orderinvoiceService.updateOrderinvoiceReceiveStatus(uuid, 1);
						//拼装开票成功日志信息
						StringBuffer sb = new StringBuffer("发票号:");
						sb.append(orderinvoice.getInvoiceNum()).append("  ").append(DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd HH:mm:ss"))
						.append("被").append(UserUtils.getUser().getName()).append("领取，金额").append(orderinvoice.getCheckAmount());
						//保存操作日志
						saveUserOpeLog("5", "财务", sb.toString(), "3", orderinvoice.getOrderType(), orderinvoice.getOrderId().longValue());
					}
				}
				return "redirect:"+Global.getAdminPath()+"/invoice/limit/supplyinvoicelist?verifyStatus=1";
			}else {
				if(orderinvoiceList.size() > 0) {
					for (Orderinvoice orderinvoice : orderinvoiceList) {
						orderinvoiceService.updateOrderinvoice(uuid,reviewRemark, 1);
						//拼装审核通过日志信息
						StringBuffer sb = new StringBuffer("订单号:");
						sb.append(orderinvoice.getOrderNum()).append("  ").append(DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd HH:mm:ss"))
						.append(" ").append(UserUtils.getUser().getName()).append("审核通过发票金额").append(orderinvoice.getCheckAmount());
						//保存操作日志
						saveUserOpeLog("5", "财务", sb.toString(), "3", orderinvoice.getOrderType(), orderinvoice.getOrderId().longValue());
					}
				}
				List<Detail> listDetail = new ArrayList<Detail>();
				Detail detail = new Detail();
				detail.setKey("uuid");
				detail.setValue(uuid);
				listDetail.add(detail);
				//2015.06.15 发票管理 --> 审核通过之后，回到待审核发票页面
				return "redirect:"+Global.getAdminPath()+"/invoice/limit/supplyinvoicelist?verifyStatus=0";
			}
		}
	}
	
	/**
	 * 驳回开发票申请
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
    @RequestMapping(value={"denyinvoice/{uuid}"})
	public String denyinvoice(@PathVariable String uuid,HttpServletRequest request, HttpServletResponse response,Model model){
		List<Orderinvoice> orderinvoiceList = orderinvoiceService.findInvoiceDetails(uuid, UserUtils.getUser().getCompany().getId());
		//20151016 C301需求， 添加审核人备注
		String reviewRemark = request.getParameter("reviewRemark");
		if(orderinvoiceList.size() > 0) {
			for (Orderinvoice orderinvoice : orderinvoiceList) {
				orderinvoiceService.updateOrderinvoice(uuid, reviewRemark, 2);
				//拼装驳回日志信息
				StringBuffer sb = new StringBuffer("订单号:");
				sb.append(orderinvoice.getOrderNum()).append("  ").append(DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd HH:mm:ss"))
				.append(" ").append(UserUtils.getUser().getName()).append("驳回发票金额").append(orderinvoice.getCheckAmount());
				//保存操作日志
				saveUserOpeLog("5", "财务", sb.toString(), "3", orderinvoice.getOrderType(), orderinvoice.getOrderId().longValue());
			}
		}		
		//调用通用的审核流程机制记录数据,单团产品
		List<Detail> listDetail = new ArrayList<Detail>();
		
		Detail detail = new Detail();
		detail.setKey("uuid");
		detail.setValue(uuid);
		listDetail.add(detail);
		//2015.06.15 待审核发票页面-->发票驳回后自动跳转到待审核发票页面
		return "redirect:"+Global.getAdminPath()+"/invoice/limit/supplyinvoicelist?verifyStatus=0";
			
	}
	
    /**
     * 发票管理（用verifyStatus判断查询列表：""为发票记录，0为待审核发票，ne0已审核列表，1为开票列表）
     * @param request
     * @param response
     * @param travelActivity
     * @param model
     * @return
     */
	@RequestMapping(value={"supplyinvoicelist",""})
	public String supplyinvoicelist(HttpServletRequest request, HttpServletResponse response, @ModelAttribute TravelActivity travelActivity, Model model){
		
		//更新发票表 把所有UUID为空的记录加上新的UUID
		orderinvoiceService.updateInvoiceUUID();
		
		//获取查询条件
		String invoiceNum = request.getParameter("invoiceNum");
		String invoiceType = request.getParameter("invoiceType");
		String selectVerifyStatus = request.getParameter("selectVerifyStatus");
		String invoiceSubject = request.getParameter("invoiceSubject");
		String invoiceTimeBegin = request.getParameter("invoiceTimeBegin");
		String invoiceTimeEnd = request.getParameter("invoiceTimeEnd");
		String invoiceHead = request.getParameter("invoiceHead");
		String createName = request.getParameter("createName");
		String invoiceCustomer = request.getParameter("invoiceCustomer");
		String applyInvoiceBegin = request.getParameter("applyInvoiceBegin");
		String applyInvoiceEnd = request.getParameter("applyInvoiceEnd");
		String invoiceMoneyBegin = request.getParameter("invoiceMoneyBegin");//发票金额开始
		String invoiceMoneyEnd = request.getParameter("invoiceMoneyEnd");//发票金额结束
		String orderNum = request.getParameter("orderNum");
		String groupCode = request.getParameter("groupCode");
		String orderTimeBegin = request.getParameter("orderTimeBegin");
		String orderTimeEnd = request.getParameter("orderTimeEnd");
		String createStatus = request.getParameter("createStatus");
		String verifyStatus = request.getParameter("verifyStatus");
		String orderBy = request.getParameter("orderBy");
		String invoiceMode = request.getParameter("invoiceMode");	//开票方式查询条件20150728
		String invoiceComingUnit = request.getParameter("invoiceComingUnit");   //来款单位		
		String jds = request.getParameter("jd");//查询时，获取选择的计调人员
		String applyInvoiceWay = request.getParameter("applyInvoiceWay");// 0444 发票申请方式
		
		//查询条件
        Map<String,String> mapRequest = new HashMap<String,String>();
        mapRequest.put("invoiceNum", invoiceNum);
        mapRequest.put("invoiceType", invoiceType);
        mapRequest.put("selectVerifyStatus", selectVerifyStatus);
        mapRequest.put("invoiceSubject", invoiceSubject);
        mapRequest.put("invoiceTimeBegin", invoiceTimeBegin);
        mapRequest.put("invoiceTimeEnd", invoiceTimeEnd);
        mapRequest.put("invoiceHead", invoiceHead);
        mapRequest.put("createName", createName);
        mapRequest.put("invoiceCustomer", invoiceCustomer);
        mapRequest.put("applyInvoiceBegin", applyInvoiceBegin);
        mapRequest.put("applyInvoiceEnd", applyInvoiceEnd);
        mapRequest.put("invoiceMoneyBegin", invoiceMoneyBegin);
        mapRequest.put("invoiceMoneyEnd", invoiceMoneyEnd);
        mapRequest.put("orderNum", orderNum);
        mapRequest.put("groupCode", groupCode);
        mapRequest.put("orderTimeBegin", orderTimeBegin);
        mapRequest.put("orderTimeEnd", orderTimeEnd);
        mapRequest.put("createStatus", createStatus);
        mapRequest.put("invoiceMode", invoiceMode);	//开票方式查询条件20150728
        mapRequest.put("invoiceComingUnit", invoiceComingUnit);	//来款单位查询条件-0414需求
        mapRequest.put("applyInvoiceWay", applyInvoiceWay);	// 0444 发票申请方式
        
        Page<Map<Object, Object>> pageOrder = new Page<Map<Object, Object>>(request, response);
        Page<Map<Object, Object>> page = orderinvoiceService.getSupplyinvoiceList(pageOrder, verifyStatus, mapRequest, 
	    		UserUtils.getUser().getCompany().getId(),travelActivity);
	    model.addAttribute("page", page);
	    
	    //查询条件传递回页面
	    model.addAttribute("invoiceNum", invoiceNum);
	    model.addAttribute("invoiceType", invoiceType);
	    model.addAttribute("selectVerifyStatus", selectVerifyStatus);
	    model.addAttribute("invoiceSubject", invoiceSubject);
	    model.addAttribute("invoiceTimeBegin", invoiceTimeBegin);
        model.addAttribute("invoiceTimeEnd", invoiceTimeEnd);
        model.addAttribute("invoiceHead",invoiceHead);
        model.addAttribute("createName", createName);
        model.addAttribute("invoiceCustomer", invoiceCustomer);
        model.addAttribute("applyInvoiceBegin", applyInvoiceBegin);
        model.addAttribute("applyInvoiceEnd", applyInvoiceEnd);
        model.addAttribute("invoiceMoneyBegin", invoiceMoneyBegin);
        model.addAttribute("invoiceMoneyEnd",invoiceMoneyEnd);
        model.addAttribute("orderNum", orderNum);
        model.addAttribute("groupCode", groupCode);
        model.addAttribute("orderTimeBegin", orderTimeBegin);
		model.addAttribute("orderTimeEnd", orderTimeEnd);
		model.addAttribute("createStatus", createStatus);        
		model.addAttribute("verifyStatus", verifyStatus);
		model.addAttribute("orderBy", orderBy);
		//开票方式查询条件20150728
		model.addAttribute("invoiceMode", invoiceMode);
		//来款单位传递回页面
		model.addAttribute("invoiceComingUnit", invoiceComingUnit);
		// 0444 申请方式
		model.addAttribute("applyInvoiceWay", applyInvoiceWay);
		//产品系列
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",
				StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		//开票类型
		model.addAttribute("invoiceTypes", DictUtils.getDictList("invoice_type"));
		//开票项目
		model.addAttribute("invoiceSubjects", DictUtils.getDictList("invoice_subject"));
		
		// 客户南亚大自然使用自定义字典表中字段
		if (Context.SUPPLIER_UUID_NYDZR.equals(UserUtils.getUser().getCompany().getUuid())) {			
			model.addAttribute("invoiceSubjects", DictUtils.getDefineDictByCompanyUuidAndType(Context.INVOICE_SUBJECT, 
					Context.SUPPLIER_UUID_NYDZR));
		}
		
		// 0453需求 客户为起航假期使用自定义字典表中字段
		if (Context.SUPPLIER_UUID_QHJQ.equals(UserUtils.getUser().getCompany().getUuid())) {			
			model.addAttribute("invoiceSubject_qhjq", DictUtils.getDefineDictByCompanyUuidAndType(Context.INVOICE_SUBJECT, 
					Context.SUPPLIER_UUID_QHJQ));
		}
		
		//针对越谏行踪，开票类型使用自定义字段，并且页面隐藏开票项目。需求0411. yudong.xu .2016-04-26
		Boolean isYJXZ = Context.SUPPLIER_UUID_YJXZ.equals(UserUtils.getUser().getCompany().getUuid());
		model.addAttribute("isYJXZ",isYJXZ);//是否是越谏行踪。
		if (isYJXZ){
			model.addAttribute("invoiceTypes", DictUtils.getDefineDictByCompanyUuidAndType(Context.INVOICE_TYPE, 
					Context.SUPPLIER_UUID_YJXZ));
		}
		//判断是否是环球行的客户
		model.addAttribute("isHQX",Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid()));
		//开票方式
		model.addAttribute("invoiceModes", DictUtils.getDictList("invoice_mode"));
		//币种列表
		model.addAttribute("currencyList", currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId()));
		//计调人员列表
		model.addAttribute("operatorList", agentinfoService.findInnerOperator(UserUtils.getUser().getCompany().getId()));
		
		/*获取批发商下所有拥有计调角色人员的名单*/
		model.addAttribute("agentJd", agentinfoService.findInnerJd(UserUtils.getUser().getCompany().getId()));
		model.addAttribute("jds",jds);
		
		return "modules/invoice/supplyInvoiceRecord";
	}
	
	/**
	 * 
	* @Title: viewsupplydetail 
	* @Description: TODO(批发商查看发票详情或者发票明细) 
	* @param @param invoiceNum 
	* @param @param request
	* @param @param response
	* @param @param model
	* @return String    返回类型 
	* @throws
	 */
	@RequestMapping(value = {"supplyviewrecorddetail/{uuid}/{verifyStatus}",""})
	public String viewsupplyrecorddetail(@PathVariable String uuid,@PathVariable String verifyStatus,
			HttpServletRequest request, HttpServletResponse response,Model model){
		
		Long companyId = UserUtils.getUser().getCompany().getId();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		
		List<String[]> limits = new ArrayList<String[]>();
		List<Orderinvoice> details = new ArrayList<Orderinvoice>();
		limits = orderinvoiceService.findSupplyLimitDetailList(uuid, companyId);
		//获取订单退款金额、订单已开收据金额
		for (Object[] m : limits) {
			String orderType = m[12].toString();
			String id = m[14].toString();
			//转化团队类型20150720
			String ordertype = m[12].toString();
			if (StringUtils.isNotBlank(ordertype)) {
				String order_type = OrderCommonUtil.getChineseOrderType(ordertype);
				m[12] = order_type;
			}else {
				m[12] = "";
			}
			//添加退款金额和开收据金额
			if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(orderType)) {
				String refundStr = OrderCommonUtil.getOrderRefundMoney(orderType,id);
				if(StringUtils.isNotBlank(refundStr)){
					m[14] = refundStr;
				}else{
					m[14] = 0;
				}
				String receiptStr = OrderCommonUtil.getOrderReceiptMoney(orderType,id);
				if (StringUtils.isNotBlank(receiptStr)) {
					m[4] = receiptStr;
				}else{
					m[4] = 0;
				}
			}
		}
		details = orderinvoiceService.findInvoiceDetails(uuid, companyId);
		BigDecimal moneyStr = BigDecimal.valueOf(0);
		if (null != details && details.size() > 0) {
			for (Orderinvoice o : details) {
				moneyStr = StringNumFormat.getBigDecimalAdd(moneyStr, o.getInvoiceAmount());
			}
		}
		model.addAttribute("companyUUID",companyUuid);
		model.addAttribute("invoiceMoney", moneyStr);
		model.addAttribute("verifyStatus", verifyStatus);
		model.addAttribute("limits", limits);
		model.addAttribute("details", details);
		model.addAttribute("invoiceModes", DictUtils.getKeyIntMap(Context.INVOICE_MODE));//开票方式
		model.addAttribute("invoiceTypes", DictUtils.getKeyIntMap(Context.INVOICE_TYPE));//开票类型
		model.addAttribute("invoiceSubjects", DictUtils.getKeyIntMap(Context.INVOICE_SUBJECT));//开票项目
		// 客户南亚大自然使用自定义字典表中字段
		if (Context.SUPPLIER_UUID_NYDZR.equals(companyUuid)) {			
			model.addAttribute("invoiceSubjects", DictUtils.getKeyIntMapFromDefinedDict(Context.INVOICE_SUBJECT, Context.SUPPLIER_UUID_NYDZR));
		}
		// 0453针对起航假期使用自定义字典表中字段
		if (Context.SUPPLIER_UUID_QHJQ.equals(companyUuid)) {			
			model.addAttribute("invoiceSubjects", DictUtils.getKeyIntMapFromDefinedDict(Context.INVOICE_SUBJECT, Context.SUPPLIER_UUID_QHJQ));
		}
		
		//需求0411-begin-判断是否是越谏行踪，yudong.xu
		Boolean isYJXZ = Context.SUPPLIER_UUID_YJXZ.equals(companyUuid);
		model.addAttribute("isYJXZ", isYJXZ);
		if (isYJXZ){
			model.addAttribute("invoiceTypes", DictUtils.getKeyIntMapFromDefinedDict(Context.INVOICE_TYPE, Context.SUPPLIER_UUID_YJXZ));
		}
		//需求0411-end-
		model.addAttribute("uuid", uuid);//新增唯一主键 by chy 2015年10月27日21:28:14
		return "modules/invoice/supplyInvoiceRecordDetail";
	}
	/**
	 * 
	* @Title: viewsupplydetailopen 
	* @Description: TODO(批发商发票订单展开详情) 
	* @param @param invoiceNum
	* @param @param request
	* @param @param response
	* @param @param model
	* @param @return    设定文件 
	* @return List<String[]>    返回类型 
	* @throws
	 */
	@ResponseBody
	@RequestMapping(value={"supplyviewdetailopen/{uuid}",""})
	public List<String[]> viewsupplydetailopen(@PathVariable String uuid,HttpServletRequest request, HttpServletResponse response,Model model){
		List<String[]> limits = new ArrayList<String[]>();
		String currencyId = request.getParameter("currencyId");
		String orderMoneyBegin = request.getParameter("orderMoneyBegin");
		String orderMoneyEnd = request.getParameter("orderMoneyEnd");
		String operator = request.getParameter("operator");//计调人员
		if(uuid == null || "".equals(uuid)){
			return null;
		}else {
			List<Orderinvoice> orderinvoceList = orderinvoiceService.findInvoiceDetails(uuid, UserUtils.getUser().getCompany().getId());
			if(null != orderinvoceList && orderinvoceList.size() > 0) {
				limits = orderinvoiceService.findSupplyLimitDetailList(orderinvoceList,currencyId,orderMoneyBegin,orderMoneyEnd,operator);
				/**
				 * add by ruyi.chen
				 * add date 2015-01-15
				 * 获取订单应收金额、统计订单退款金额
				 */
				for(Object[] m:limits){
					String orderType = m[12].toString();
					String id = m[13].toString();
					String totalMoney = m[14].toString();
					//添加退款金额
					if(StringUtils.isNotBlank(id) && StringUtils.isNotBlank(orderType)){
						List<Map<String,Object>> ls = orderinvoiceService.getOrderAllRefund(id, orderType);
						if(null != ls&&ls.size() > 0){
							String refundStr = ls.get(0).get("refundTotalStr").toString();
							if(StringUtils.isNotBlank(refundStr)){
								m[12] = refundStr;
							}else{
								m[12] = 0;
							}
						}else{
							m[12] = 0;
						}
					}else{
						m[12] = 0;
					}
					//添加总应收
					if(StringUtils.isNotBlank(totalMoney)){
						String total_money = OrderCommonUtil.getMoneyAmountBySerialNum(totalMoney,2);
						m[14] = total_money;
					}else{
						m[14] = 0;
					}
					//转化团队类型20150716
					if(StringUtils.isNotBlank(orderType)) {
						String order_type = OrderCommonUtil.getChineseOrderType(orderType);
						m[12] = order_type;
					}else {
						m[12] = "";
					}
				}
			} else {
				limits = null;
			}
			model.addAttribute("currencyId", currencyId);
	        model.addAttribute("orderMoneyBegin", orderMoneyBegin);
	        model.addAttribute("orderMoneyEnd",orderMoneyEnd);
	        return limits;
		}
	}	
	
	@ResponseBody
	@RequestMapping("invoiceNum")
	public String inoviceNum(@RequestParam("inoviceNum") String inoviceNum) {
		if(inoviceNum == null || "".equals(inoviceNum)){
			return "false";
		}
		Long inoviceCompany = UserUtils.getUser().getCompany().getId();
		return orderinvoiceService.invoiceNum(inoviceNum.trim(), inoviceCompany)?"true":"false";//新加trim by chy 2015年10月28日11:35:12
		
	}
	
	/**
	 * 开票页面发票撤销功能，将已开票发票改成待开票
	 * @param request
	 * @param response
	 * @param model
	 * @author shijun.liu
	 */
	@RequestMapping("revokeToUninvioce/{uuid}")
	public void revokeToUninvioce(@PathVariable("uuid") String uuid, HttpServletRequest request, 
			HttpServletResponse response, Model model){
		String json = null;
		if(StringUtils.isBlank(uuid)){
			json = "{\"flag\":\"fail\",\"msg\":\"发票关键字不能为空，请确认\"}";
		}else{
			int count = orderinvoiceService.revokeToUninvioce(uuid);
			json = "{\"flag\":\"success\",\"msg\":\"撤销成功\"}";
			if(count > 1){
				json = "{\"flag\":\"success\",\"msg\":\"成功撤销"+count+"条发票\"}";
			}
		}
		ServletUtil.print(response, json);
	}
	
	/**
	 * 已审核页面发票撤销功能，将已审核发票改成待审核
	 * @param request
	 * @param response
	 * @param model
	 * @author shijun.liu
	 */
	@RequestMapping("revokeToUncheck/{uuid}")
	public void revokeToUncheck(@PathVariable("uuid") String uuid, HttpServletRequest request, 
			HttpServletResponse response, Model model){
		String json = null;
		if(StringUtils.isBlank(uuid)){
			json = "{\"flag\":\"fail\",\"msg\":\"发票关键字不能为空，请确认\"}";
		}else{
			int count = orderinvoiceService.revokeToUncheck(uuid);
			json = "{\"flag\":\"success\",\"msg\":\"撤销成功\"}";
			if(count > 1){
				json = "{\"flag\":\"success\",\"msg\":\"成功撤销"+count+"条发票\"}";
			}
		}
		ServletUtil.print(response, json);
	}
	
	/**
	 * 开发票申请列表信息 add by chy 2016年1月18日16:06:06
	 */
	@ResponseBody
	@RequestMapping("invoceopenlist")
	public Map<String, Object> getOpenInvoiceList(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> res = new HashMap<String, Object>();
		String ids = request.getParameter("ids");
		if(StringUtils.isBlank(ids)){
			res.put("flag", "error");
			res.put("msg", "无效的参数：发票信息为空");
			return res;
		}
		List<Map<String, Object>> orderInvoiceList = orderinvoiceService.getInvoiceListByIds(ids);
		if(orderInvoiceList == null || orderInvoiceList.size() == 0){
			res.put("flag", "error");
			res.put("msg", "查询发票信息失败：找不到对应的发票信息");
			return res;
		}
		res.put("flag", "success");
		res.put("list", orderInvoiceList);
		return res;
	}
	
	/**
	 * 批量 开票 by chy 2016年1月18日18:16:11
	 */
	@ResponseBody
	@RequestMapping("invocebatchopen")
	@Transactional
	public String invoiceBatchOpen(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			JSONArray jsonArray = new JSONArray(request.getParameter("datas"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			for(int i = 0; i < jsonArray.length(); i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String uuid = jsonObject.get("uuid").toString();
				String invoiceNumber = jsonObject.get("invoiceNumber").toString();
				Date invoiceTime = sdf.parse(jsonObject.get("invoiceTime").toString());
				String numberRemark = jsonObject.get("numberRemark") == null ? "" : jsonObject.get("numberRemark").toString();
				synchronized (this) {
					List<Orderinvoice> list = orderinvoiceService.findByInvoiceNumAndCreateStatus(uuid, 1);
					if(list != null && list.size() > 0){
						return "所选数据中有已开票数据，重新选择！";
					}
					orderinvoiceService.updateOrderinvoiceCreateStatus(uuid, invoiceNumber, numberRemark, 1, invoiceTime);
				}
			}
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
	
	/**
	 * 批量领取
	 * @author zzk
	 * @param request
	 * @return
	 */
	@RequestMapping("batchReceive")
	public String batchReceive(HttpServletRequest request) {
		String uuids = request.getParameter("uuids");
		String[] uuidArr = uuids.split(",");
		for (int i = 0; i < uuidArr.length; i++) {
			String uuid = uuidArr[i];
			List<Orderinvoice> orderinvoiceList = orderinvoiceService.findInvoiceDetails(uuid, UserUtils.getUser().getCompany().getId());
			if(orderinvoiceList.size() > 0) {
				for (Orderinvoice orderinvoice : orderinvoiceList) {
					orderinvoiceService.updateOrderinvoiceReceiveStatus(uuid, 1);
					//拼装领取成功日志信息
					StringBuffer sb = new StringBuffer("发票号:");
					sb.append(orderinvoice.getInvoiceNum()).append("  ").append(DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd HH:mm:ss"))
					.append("被").append(UserUtils.getUser().getName()).append("领取，金额").append(orderinvoice.getCheckAmount());
					//保存操作日志
					saveUserOpeLog("5", "财务", sb.toString(), "3", orderinvoice.getOrderType(), orderinvoice.getOrderId().longValue());
				}
			}
		}
		
//		return "redirect:"+Global.getAdminPath()+"/invoice/limit/supplyinvoicelist?verifyStatus=1";
		return "批量领取成功！";
	}
	
	/**
	 * 导出到excel中
	 * @param request
	 * @param response
	 * createdBy yudong.xu --2016年3月28日--上午9:12:21
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value = "/exportexcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
		//更新发票表 把所有UUID为空的记录加上新的UUID
		orderinvoiceService.updateInvoiceUUID();
		//获取传入的查询参数
		String invoiceNum = request.getParameter("invoiceNum");
		String invoiceType = request.getParameter("invoiceType");
		String selectVerifyStatus = request.getParameter("selectVerifyStatus");
		String invoiceSubject = request.getParameter("invoiceSubject");
		String invoiceTimeBegin = request.getParameter("invoiceTimeBegin");
		String invoiceTimeEnd = request.getParameter("invoiceTimeEnd");
		String invoiceHead = request.getParameter("invoiceHead");
		String createName = request.getParameter("createName");
		String invoiceCustomer = request.getParameter("invoiceCustomer");
		String applyInvoiceBegin = request.getParameter("applyInvoiceBegin");
		String applyInvoiceEnd = request.getParameter("applyInvoiceEnd");
		String invoiceMoneyBegin = request.getParameter("invoiceMoneyBegin");//发票金额开始
		String invoiceMoneyEnd = request.getParameter("invoiceMoneyEnd");//发票金额结束
		String orderNum = request.getParameter("orderNum");
		String groupCode = request.getParameter("groupCode");
		String orderTimeBegin = request.getParameter("orderTimeBegin");
		String orderTimeEnd = request.getParameter("orderTimeEnd");
		String createStatus = request.getParameter("createStatus");
		String verifyStatus = request.getParameter("verifyStatus");
		// 0444需求 申请方式
		String applyInvoiceWay = request.getParameter("applyInvoiceWay");
		//开票方式查询条件20150728
		String invoiceMode = request.getParameter("invoiceMode");
		String invoiceComingUnit = request.getParameter("invoiceComingUnit");   //来款单位	
		//查询条件map
        Map<String,String> mapRequest = new HashMap<String,String>(25);
        mapRequest.put("invoiceNum", invoiceNum);
        mapRequest.put("invoiceType", invoiceType);
        mapRequest.put("selectVerifyStatus", selectVerifyStatus);
        mapRequest.put("invoiceSubject", invoiceSubject);
        mapRequest.put("invoiceTimeBegin", invoiceTimeBegin);
        mapRequest.put("invoiceTimeEnd", invoiceTimeEnd);
        mapRequest.put("invoiceHead", invoiceHead);
        mapRequest.put("createName", createName);
        mapRequest.put("invoiceCustomer", invoiceCustomer);
        mapRequest.put("applyInvoiceBegin", applyInvoiceBegin);
        mapRequest.put("applyInvoiceEnd", applyInvoiceEnd);
        mapRequest.put("invoiceMoneyBegin", invoiceMoneyBegin);
        mapRequest.put("invoiceMoneyEnd", invoiceMoneyEnd);
        mapRequest.put("orderNum", orderNum);
        mapRequest.put("groupCode", groupCode);
        mapRequest.put("orderTimeBegin", orderTimeBegin);
        mapRequest.put("orderTimeEnd", orderTimeEnd);
        mapRequest.put("createStatus", createStatus);
        mapRequest.put("verifyStatus", verifyStatus);
        //开票方式查询条件20150728
        mapRequest.put("invoiceMode", invoiceMode);
        mapRequest.put("invoiceComingUnit", invoiceComingUnit);
        // 0444需求 申请方式
        mapRequest.put("applyInvoiceWay", applyInvoiceWay);
        
        List<Map<Object, Object>> list = orderinvoiceService.getSupplyinvoiceListForExcel(mapRequest,UserUtils.getUser().getCompany().getId());
        Workbook workbook = InvoiceExcelUtils.createInvoiceExcel(list);
		String fileName = "发票导出-" +DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd") + ".xls";
		ServletUtil.downLoadExcel(response, fileName, workbook);
	}
}
