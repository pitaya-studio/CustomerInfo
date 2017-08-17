package com.trekiz.admin.modules.receipt.web;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.receipt.entity.OrderReceipt;
import com.trekiz.admin.modules.receipt.service.OrderReceiptService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 开发票的额度控制
 * @author Administrator
 *
 */

@Controller
@RequestMapping(value = "${adminPath}/receipt/limit")
public class SupplyReceiptlimitController extends BaseController {

	@Autowired
	private OrderReceiptService orderReceiptService;
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
	 */
    @RequestMapping(value={"verifyinvoice/{uuid}"})
	public String verifyinvoice(@PathVariable String uuid,HttpServletRequest request, HttpServletResponse response,Model model){
		String createInvoiceHid = request.getParameter("createInvoiceHid");
		String receiveInvoiceHid = request.getParameter("receiveInvoiceHid");
		String invoiceNumber = request.getParameter("invoiceNum");
		List<OrderReceipt> orderinvoiceList = orderReceiptService.findInvoiceDetails(uuid, UserUtils.getUser().getCompany().getId());	
		//开票操作
		if("true".equals(createInvoiceHid)) {
			if(orderinvoiceList.size() > 0) {
				orderReceiptService.updateOrderinvoiceCreateStatus(uuid, invoiceNumber, 1);
			}
			//2015.06.15 发票管理 --> 开票 返回成功之后，回到开票页面
			return "redirect:"+Global.getAdminPath()+"/receipt/limit/supplyreceiptlist/2";
		}else {
			if("true".equals(receiveInvoiceHid)) {
				if(orderinvoiceList.size() > 0) {
					orderReceiptService.updateOrderinvoiceReceiveStatus(uuid, 1);
				}
				return "redirect:"+Global.getAdminPath()+"/receipt/limit/supplyreceiptlist/2";
			}else {
				if(orderinvoiceList.size() > 0) {
					orderReceiptService.updateOrderinvoice(uuid, 1);
				}
				//2015.06.15 发票管理 --> 审核通过之后，回到待审核发票页面
				return "redirect:"+Global.getAdminPath()+"/receipt/limit/supplyreceiptlist/0";
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
		List<OrderReceipt> orderinvoiceList = orderReceiptService.findInvoiceDetails(uuid, UserUtils.getUser().getCompany().getId());
		if(orderinvoiceList.size() > 0) {
			orderReceiptService.updateOrderinvoice(uuid, 2);
		}		
		//2015.06.15 待审核发票页面-->发票驳回后自动跳转到待审核发票页面
		return "redirect:"+Global.getAdminPath()+"/receipt/limit/supplyreceiptlist/0";
			
	}
	
    /**
     * @param showType  区分页面的标志 0 待审核页面 9 收据记录页面  1 已审核收据页
     * @param request
     * @param response
     * @param travelActivity
     * @param model
     * @return
     */
	@RequestMapping(value={"supplyreceiptlist/{showType}"})
	public String supplyinvoicelist(@PathVariable String showType, HttpServletRequest request, HttpServletResponse response, @ModelAttribute TravelActivity travelActivity, Model model){
		
		Page<Map<Object, Object>> page = null;
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		Long companyId = UserUtils.getUser().getCompany().getId();
		
		// 获取页面参数
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
		if("0".equals(showType)){//待审核收据
			selectVerifyStatus = "0";
		}
		if("1".equals(showType) && (selectVerifyStatus == null || "".equals(selectVerifyStatus))){//已审核收据
			verifyStatus = "ne0";
		}
		if("2".equals(showType)){//开收据
			verifyStatus = "1";
			selectVerifyStatus = null;
		}
		Page<Map<Object, Object>> pageOrder = new Page<Map<Object, Object>>(request, response);
		
		String jds = request.getParameter("jd");//查询时，获取选择的计调人员
		
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
        
	    page = orderReceiptService.getSupplyinvoiceList(pageOrder, verifyStatus, mapRequest, companyId, travelActivity);

	    // 将查询条件回传页面
	    model.addAttribute("showType", showType);//showType
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
		model.addAttribute("page", page);
        
		//产品系列
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level", companyId));
		//开票类型
		model.addAttribute("invoiceTypes", DictUtils.getDictList("invoice_type"));
		//开票项目
		model.addAttribute("invoiceSubjects", DictUtils.getDictList("invoice_subject"));
		// 客户南亚大自然使用自定义字典表中字段
		if (Context.SUPPLIER_UUID_NYDZR.equals(companyUuid)) {			
			model.addAttribute("invoiceSubjects", DictUtils.getDefineDictByCompanyUuidAndType(Context.INVOICE_SUBJECT, Context.SUPPLIER_UUID_NYDZR));
		}
		//判断是否是环球行的客户
		model.addAttribute("isHQX",Context.SUPPLIER_UUID_HQX.equals(companyUuid));
		//开票方式
		model.addAttribute("invoiceModes", DictUtils.getDictList("invoice_mode"));
		//币种列表
		model.addAttribute("currencyList", currencyService.findCurrencyList(companyId));
		//计调人员列表
		model.addAttribute("operatorList", agentinfoService.findInnerOperator(companyId));
		
		/*获取批发商下所有拥有计调角色人员的名单*/
		model.addAttribute("agentJd", agentinfoService.findInnerJd(companyId));
		model.addAttribute("jds",jds);
		if("9".equals(showType)){//收据记录页
			return "modules/receipt/receiptRecords";
		} else if("0".equals(showType)){
			return "modules/receipt/receiptToBeReviewed";
		} else if("1".equals(showType)) {
			return "modules/receipt/receiptReviewed";
		} else if("2".equals(showType)) {
			return "modules/receipt/applyReceiptRecords";
		} else {
			return "";
		}
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
	@RequestMapping(value = {"supplyviewrecorddetail/{uuid}/{verifyStatus}/{orderType}",""})
	public String viewsupplyrecorddetail(@PathVariable String uuid,@PathVariable String verifyStatus,@PathVariable String orderType,
			HttpServletRequest request, HttpServletResponse response,Model model){
		List<String[]> limits = new ArrayList<String[]>();
		List<OrderReceipt> details = new ArrayList<OrderReceipt>();
		limits = orderReceiptService.findSupplyLimitDetailList(uuid, Integer.parseInt(orderType), UserUtils.getUser().getCompany().getId());
		details = orderReceiptService.findInvoiceDetails(uuid, UserUtils.getUser().getCompany().getId());
		BigDecimal moneyStr=BigDecimal.valueOf(0);
		if(null!=details&&details.size()>0){
			for(OrderReceipt o:details){
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
		// 客户南亚大自然使用自定义字典表中字段
		if (Context.SUPPLIER_UUID_NYDZR.equals(UserUtils.getUser().getCompany().getUuid())) {			
			model.addAttribute("invoiceSubjects",DictUtils.getKeyIntMapFromDefinedDict(Context.INVOICE_SUBJECT, Context.SUPPLIER_UUID_NYDZR));
		}
		// 环球行
		boolean isHQX = false;
		if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())){
			isHQX = true;
		}
		model.addAttribute("isHQX",isHQX);
		if("-1".equals(verifyStatus)){
			return "modules/receipt/applyReceiptDetail";
		}else{
			return "modules/receipt/supplyViewReceiptDetail";
		}
	}
	/**
	 * 审核页
	 * @param invoiceNum
	 * @param verifyStatus
	 * @param orderType
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"supplyreviewrecorddetail/{invoiceNum}/{verifyStatus}/{orderType}",""})
	public String viewsupplyReviewrecorddetail(@PathVariable String invoiceNum,@PathVariable String verifyStatus,@PathVariable String orderType,
			HttpServletRequest request, HttpServletResponse response,Model model){
		List<String[]> limits = new ArrayList<String[]>();
		List<OrderReceipt> details = new ArrayList<OrderReceipt>();
		
		limits = orderReceiptService.findSupplyLimitDetailList(invoiceNum, Integer.parseInt(orderType), UserUtils.getUser().getCompany().getId());
		details = orderReceiptService.findInvoiceDetails(invoiceNum, UserUtils.getUser().getCompany().getId());
		
		BigDecimal moneyStr = BigDecimal.valueOf(0);
		if (null != details && details.size() > 0) {
			for (OrderReceipt o : details) {
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
		// 客户南亚大自然使用自定义字典表中字段
		if (Context.SUPPLIER_UUID_NYDZR.equals(UserUtils.getUser().getCompany().getUuid())) {			
			model.addAttribute("invoiceSubjects",DictUtils.getKeyIntMapFromDefinedDict(Context.INVOICE_SUBJECT, Context.SUPPLIER_UUID_NYDZR));
		}
		return "modules/receipt/supplyReviewReceiptDetail";
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
	public List<String[]> viewsupplydetailopen(@PathVariable String uuid, 
			HttpServletRequest request, HttpServletResponse response, Model model){
		List<String[]> limits = new ArrayList<String[]>();
		List<String[]> limitsResult = new ArrayList<String[]>();
		String currencyId = request.getParameter("currencyId");
		String orderMoneyBegin = request.getParameter("orderMoneyBegin");
		String orderMoneyEnd = request.getParameter("orderMoneyEnd");
		String operator = request.getParameter("operator");//计调人员
		
		if(uuid == null || "".equals(uuid)){
			return null;
		}else {
			List<OrderReceipt> orderinvoceList = orderReceiptService.findInvoiceDetails(uuid, UserUtils.getUser().getCompany().getId());
			if(null != orderinvoceList && orderinvoceList.size() > 0) {
				limits = orderReceiptService.findSupplyLimitDetailList(orderinvoceList,currencyId,orderMoneyBegin,orderMoneyEnd,operator);

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
						List<Map<String,Object>> ls = orderReceiptService.getOrderAllRefund(id, orderType);
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
						String total_money = OrderCommonUtil.getMoneyAmountBySerialNum(totalMoney,1);
						m[14] = total_money;
					}else{
						m[14] = 0;
					}
					String[] mTemp = new String[m.length + 5];
					for(int i = 0; i < m.length; i++){
						mTemp[i] = m[i] == null ? "" : m[i].toString();
					}
					mTemp[16] = OrderCommonUtil.getOrderInvoiceMoney(orderType== null ? "" : orderType, m[13]== null ? "" : m[13].toString());
					mTemp[17] = OrderCommonUtil.getOrderRefundMoney(orderType== null ? "" : orderType, m[13]== null ? "" : m[13].toString());
					mTemp[18] = OrderCommonUtil.getOrderReceiptMoney(orderType== null ? "" : orderType, m[13]== null ? "" : m[13].toString());
					mTemp[19] = OrderCommonUtil.getAccountMoney(orderType== null ? "" : orderType, m[13]== null ? "" : m[13].toString());
					mTemp[20] = OrderCommonUtil.getOrderMoney(orderType== null ? "" : orderType, m[13]== null ? "" : m[13].toString(), "3");
					limitsResult.add(mTemp);
				}
			} else {
				limits = null;
			}
			model.addAttribute("currencyId", currencyId);
	        model.addAttribute("orderMoneyBegin", orderMoneyBegin);
	        model.addAttribute("orderMoneyEnd",orderMoneyEnd);
	        return limitsResult;
		}
	}	
	
	/**
	 * 开收据
	 * */
	@ResponseBody
	@RequestMapping("invoiceNum")
	public String inoviceNum(@RequestParam("inoviceNum") String inoviceNum) {
		Long inoviceCompany = UserUtils.getUser().getCompany().getId();
		return orderReceiptService.invoiceNum(inoviceNum, inoviceCompany)?"true":"false";
		
	}
	
	/**
	 * 开票页面发票撤销功能，将已开票发票改成待开票
	 * @param request
	 * @param response
	 * @param model
	 * @author shijun.liu
	 */
	@RequestMapping("revokeToUninvioce/{uuid}/{createStatus}")
	public void revokeToUninvioce(@PathVariable("uuid") String uuid,@PathVariable("createStatus") Integer createStatus,HttpServletRequest request, 
			HttpServletResponse response, Model model){
		String json = null;
		if(StringUtils.isBlank(uuid)){
			json = "{\"flag\":\"fail\",\"msg\":\"发票号不能为空，请确认\"}";
		}else{
			int count = orderReceiptService.revokeToUninvioce(uuid, createStatus);
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
			json = "{\"flag\":\"fail\",\"msg\":\"收据数据错误，请确认\"}";
		}else{
			int count = orderReceiptService.revokeToUncheck(uuid);
			json = "{\"flag\":\"success\",\"msg\":\"撤销成功\"}";
			if(count > 1){
				json = "{\"flag\":\"success\",\"msg\":\"成功撤销"+count+"条收据\"}";
			}
		}
		ServletUtil.print(response, json);
	}
	
	/**
	 * 还收据
	 */
	@ResponseBody
	@RequestMapping("returnReceipt/{uuid}")
	public Map<String, Object> returnReceipt(@PathVariable("uuid") String uuid){
		Map<String, Object> res = new HashMap<String, Object>();
		try{
			orderReceiptService.updateOrderinvoiceCreateStatus(uuid, 2);
		} catch (Exception e) {
			e.printStackTrace();
			res.put("fail", "还收据出现异常，请与技术人员联系");
			return res;
		}
		res.put("success", "success");
		return res;
	}
	
}
