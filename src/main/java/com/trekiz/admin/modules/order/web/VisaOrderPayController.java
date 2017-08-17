package com.trekiz.admin.modules.order.web;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.invoice.entity.Orderinvoice;
import com.trekiz.admin.modules.invoice.repository.OrderinvoiceDao;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.repository.OrderpayDao;
import com.trekiz.admin.modules.order.service.OrderPayMoreService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.statistics.utils.ExcelUtils;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaOrderTravelerPayLogService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;

import freemarker.template.TemplateException;
@Controller
@RequestMapping(value = "${adminPath}/visaOrderPayLog/manage")
public class VisaOrderPayController extends BaseController {
    @Autowired
    private VisaOrderTravelerPayLogService logService;

    @Autowired
	private OrderPayMoreService orderPayMoreService;
    
    @Autowired
    private AgentinfoService agentinfoService;
    
    @Autowired
    private OrderpayDao orderpayDao;
    
    @Autowired
    private VisaOrderService visaOrderService;
    
    @Autowired
    private VisaProductsService VisaProductsService;
    
    @Autowired
    private OrderinvoiceDao orderinvoiceDao;
    
    /**
     * 查询签证订单支付记录
     * @param response
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value ="showVisaOrderTravelPayList/{showType}/{orderStatus}")
	public String showVisaTravelOrderPayList(@PathVariable String showType, @PathVariable String orderStatus,HttpServletResponse response,
	        Model model, HttpServletRequest request) throws Exception {
    	String companyUUID = UserUtils.getUser().getCompany().getUuid();
    	Page<Map<Object, Object>> pageOrder = null;
        Map<String, String> mapRequest = setQueryCondition(request);
        /*获取计调人员*/
        String jd = request.getParameter("jd");
        if(StringUtils.isNotBlank(jd)){
        	 mapRequest.put("jd", jd);
        }
        /*获取销售人员*/
        String saler = request.getParameter("saler");
        if(StringUtils.isNotBlank(saler)){
        	 mapRequest.put("saler", saler);
        }
    	pageOrder = logService.findVisaOrderList1(new Page<Map<Object, Object>>(request, response), mapRequest);
    	//获取签证类型 页面下拉框展示
    	List<Dict> visaTypeList  =logService.getVisaType();
    	setReturnValue(model, request, pageOrder, jd, saler, visaTypeList);
    	//String companyUuid = UserUtils.getUser().getCompany().getUuid();
    	model.addAttribute("companyUuid",companyUUID);
    	//拉美途
    	model.addAttribute("isLMT", Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUUID));
    	//起航假期
    	model.addAttribute("isQHJQ", Context.SUPPLIER_UUID_QHJQ.equals(companyUUID));
    	
    	return "modules/order/orderListForDzOrderPay";
    }
    /**
     * 设置查询参数
     * @param request
     * @return
     */
	private Map<String, String> setQueryCondition(HttpServletRequest request) {
		Map<String,String> mapRequest = Maps.newHashMap();
        mapRequest.put("orderNum", request.getParameter("orderNum"));
        mapRequest.put("bankTimeBegin", request.getParameter("bankTimeBegin"));
        mapRequest.put("bankTimeEnd", request.getParameter("bankTimeEnd"));
        mapRequest.put("payAmountStrat", request.getParameter("payAmountStrat"));
        mapRequest.put("payAmountEnd", request.getParameter("payAmountEnd"));
        mapRequest.put("visaType", request.getParameter("visaType"));
        mapRequest.put("visaCountry", request.getParameter("visaCountry"));
        mapRequest.put("saler", request.getParameter("saler"));
        mapRequest.put("orderTimeBegin", request.getParameter("orderTimeBegin"));
        mapRequest.put("orderTimeEnd", request.getParameter("orderTimeEnd"));
        mapRequest.put("payTimeBegin", request.getParameter("payTimeBegin"));
        mapRequest.put("payTimeEnd", request.getParameter("payTimeEnd"));
		//180日信观光需求财务结算管理签证订单收款页面收索列表添加出团日期查询条件-2016/02/29--wenchao.lv
		mapRequest.put("startOutBegin",request.getParameter("startOutBegin"));
		mapRequest.put("startOutEnd",request.getParameter("startOutEnd"));
		//180日信观光需求财务结算管理签证订单收款页面收索列表添加出团日期查询条件-2016/02/29--wenchao.lv
        mapRequest.put("isAccounted", request.getParameter("isAccounted"));
        mapRequest.put("agentId", request.getParameter("agentId"));
        mapRequest.put("printFlag", request.getParameter("printFlag"));
        mapRequest.put("travellerName", request.getParameter("travellerName"));
        mapRequest.put("payerName", request.getParameter("payerName"));
        mapRequest.put("toBankNname", request.getParameter("toBankNname"));
        mapRequest.put("jd", request.getParameter("jd"));
        mapRequest.put("createByName", request.getParameter("createByName"));
        mapRequest.put("payType", request.getParameter("payType"));
        //0405 收款确认日期
        mapRequest.put("confirmationDateStar", request.getParameter("confirmationDateStar"));
        mapRequest.put("confirmationDateEnd", request.getParameter("confirmationDateEnd"));
        //渠道结款方式
        mapRequest.put("paymentType", request.getParameter("paymentType"));
        
		return mapRequest;
	}
	/**
	 * 设定页面返回值
	 * @param model
	 * @param request
	 * @param pageOrder
	 * @param jd
	 * @param saler
	 * @param visaTypeList
	 */
	private void setReturnValue(Model model, HttpServletRequest request,
			Page<Map<Object, Object>> pageOrder, String jd, String saler,
			List<Dict> visaTypeList) {
		String Companyuuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("Companyuuid", Companyuuid);
		model.addAttribute("page", pageOrder);
    	model.addAttribute("orderNum", request.getParameter("orderNum"));
    	model.addAttribute("visaTypeList",visaTypeList);
    	model.addAttribute("orderTimeBegin",request.getParameter("orderTimeBegin"));
    	model.addAttribute("orderTimeEnd",request.getParameter("orderTimeEnd"));
    	model.addAttribute("agentId",request.getParameter("agentId"));
    	model.addAttribute("visaType",request.getParameter("visaType"));
    	model.addAttribute("payAmountStrat",request.getParameter("payAmountStrat"));
    	model.addAttribute("payAmountEnd",request.getParameter("payAmountEnd"));
    	model.addAttribute("visaCountry",request.getParameter("visaCountry"));
    	model.addAttribute("saler",request.getParameter("saler"));
    	model.addAttribute("orderTimeBegin",request.getParameter("orderTimeBegin"));
    	model.addAttribute("orderTimeEnd",request.getParameter("orderTimeEnd"));
    	model.addAttribute("payTimeBegin",request.getParameter("payTimeBegin"));
    	model.addAttribute("payTimeEnd",request.getParameter("payTimeEnd"));
		//180日信观光需求财务结算管理签证订单收款页面收索列表添加出团日期查询条件-2016/02/29--wenchao.lv
		model.addAttribute("startOutBegin",request.getParameter("startOutBegin"));
		model.addAttribute("startOutEnd",request.getParameter("startOutEnd"));
		//180日信观光需求财务结算管理签证订单收款页面收索列表添加出团日期查询条件-2016/02/29--wenchao.lv
    	model.addAttribute("payerName",request.getParameter("payerName"));
    	//渠道结款方式
    	model.addAttribute("paymentType",request.getParameter("paymentType"));
		//注销 yudong.xu 2016-4-9
    	model.addAttribute("agentinfoList", agentinfoService.findStockAgentinfo());
    	/*获取批发商下所有拥有计调角色人员的名单*/
		//注销 yudong.xu 2016-4-9
		model.addAttribute("agentJd", agentinfoService.findInnerJd(UserUtils.getUser().getCompany().getId()));
		model.addAttribute("jds", jd);
		/*获取批发商下所有拥有销售角色人员的名单*/
		//注销 yudong.xu 2016-4-9
		model.addAttribute("agentSalers", agentinfoService.findInnerSales(UserUtils.getUser().getCompany().getId()));
		model.addAttribute("saler", saler);
		model.addAttribute("isAccounted",request.getParameter("isAccounted"));
		model.addAttribute("printFlag",request.getParameter("printFlag"));
		model.addAttribute("travellerName",request.getParameter("travellerName"));
		model.addAttribute("toBankNname",request.getParameter("toBankNname"));
		model.addAttribute("bankTimeBegin",request.getParameter("bankTimeBegin"));
		model.addAttribute("bankTimeEnd",request.getParameter("bankTimeEnd"));
		//0405 收款确认日期 2016-4-27
		model.addAttribute("confirmationDateStar",request.getParameter("confirmationDateStar"));
		model.addAttribute("confirmationDateEnd",request.getParameter("confirmationDateEnd"));
		//0405 收款银行筛选项数据取供应商维护的银行名称
		Long companyId = UserUtils.getUser().getCompany().getId();
		model.addAttribute("receiptBankName", orderPayMoreService.getBankInfo(0, companyId.intValue()));
		//注销 yudong.xu 2016-4-9
		List<Map<String, Object>> createByNameList = visaOrderService.findVisaOrderCreateBy1();
		model.addAttribute("createByList", createByNameList);
		model.addAttribute("createByName", request.getParameter("createByName"));
		/*获取支付方式*/
        List<Map<String,Object>> payTypeList = DictUtils.getPayType();
        model.addAttribute("payTypeList",payTypeList);
        model.addAttribute("payType",request.getParameter("payType")); 
		
	}
    /**
     * 打印效果预览
     * @param response
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value ="showPrint")
   	public String showPrint(HttpServletResponse response,
   	        Model model, HttpServletRequest request) throws Exception {
    	String orderPaySerialNum =  request.getParameter("orderPaySerialNum");
    	String vorderid =  request.getParameter("vorderid");
    	String vpid =  request.getParameter("vpid");
    	
    	String companyUUID = UserUtils.getUser().getCompany().getUuid();
    	
    	/*
    	 * wangxinwei  added 20151012， 客人报名收款单  和打印  如果 已经确认收款要显示收款日期
    	 * ，需求C221
    	 */
        VisaOrder visaOrder= visaOrderService.findVisaOrder(Long.parseLong(vorderid));
    	List<Orderpay> orderpayList = orderpayDao.findOrderpaByOrderPaySerialNum(orderPaySerialNum);
    	Date dt = new Date();
    	if (null!=orderpayList&&orderpayList.size()>0) {
    		if(null == orderpayList.get(0).getPrintTime()){
        		model.addAttribute("printTime",DateUtils.formatCustomDate( dt, "yyyy/MM/dd HH:mm"));
    		}else{
        		model.addAttribute("printTime",DateUtils.formatCustomDate( (Date) orderpayList.get(0).getPrintTime(), "yyyy/MM/dd HH:mm"));
    		}
		}
		 
		 //计算多币种总价
		 String totalmoney = logService.getOrderTravellersPayed(orderPaySerialNum);
		 
		 model.addAttribute("toatalMoneyBig",digitUppercase(Double.parseDouble(totalmoney)));
		 model.addAttribute("toatalMoney",fmtMicrometer(totalmoney));
		 //填写日期
		 model.addAttribute("startDate",visaOrder.getCreateDate()); 
		 
		 //产品名称
		 VisaProducts visaProducts = VisaProductsService.findByVisaProductsId(Long.parseLong(vpid));
		 model.addAttribute("product",visaProducts.getProductName()); 
		 
		 //来款单位信息
		 model.addAttribute("payerName",orderpayList.get(0).getPayerName()); 
		 model.addAttribute("fromPayerName",orderpayList.get(0).getBankName()); 
		 model.addAttribute("fromBankAccount",orderpayList.get(0).getBankAccount());
		 //处理付款备注无法显示的问题
		 model.addAttribute("remarks",orderpayList.get(0).getRemarks()); 
		 
		 //----------收款账户-----------
		 model.addAttribute("toBankNname",orderpayList.get(0).getToBankNname()); 
		 model.addAttribute("toBankAccount",orderpayList.get(0).getToBankAccount());
		 
		 //----------交款人，经办人-----------
		 model.addAttribute("operator",orderpayList.get(0).getCreateBy().getName()); 
		 
		 //----------收款人-----------未达账不显示收款人
		 if(null == orderpayList.get(0).getIsAsAccount()) 
		 {
			 model.addAttribute("payeee",""); 
		 }else{
			 //model.addAttribute("payeee",map.get("payeee"));
			 model.addAttribute("payeee",orderpayList.get(0).getUpdateBy().getName());
		 }
		 
		 //-----------支付宝账号-------
		 model.addAttribute("toAlipayAccount", orderpayList.get(0).getToAlipayAccount());
		 model.addAttribute("toAlipayName", orderpayList.get(0).getToAlipayName());
		 
		 model.addAttribute("orderPaySerialNum",orderPaySerialNum); 
		 model.addAttribute("vorderid",vorderid); 
		 model.addAttribute("vpid",vpid); 
		 
		
		 //model.addAttribute("group_code",visaOrder.getGroupCode());//团号   vo.group_code 从list 带过来的
		 if ("7a816f5077a811e5bc1e000c29cf2586".equals(companyUUID)) {
			 model.addAttribute("group_code",visaOrder.getGroupCode());	
		 }else{
			 model.addAttribute("group_code",visaProducts.getGroupCode());// 对应需求C460V3  签证订单团号  统一取签证订单所关联的产品团号
		 }
		 
		 //model.addAttribute("money",map.get("countryName_cn")+" 签证费");
		 model.addAttribute("money"," 签证费");//改为只显示签证费
		 String countryName_cn =  CountryUtils.getCountryName(Long.parseLong(visaProducts.getSysCountryId()+""));
		 model.addAttribute("countryName_cn",countryName_cn);
		 
		 //-----获取批量付款游客名单-----
		 String trevelerNames = logService.getOrderTravellersPayedNames(orderPaySerialNum);
		 model.addAttribute("travllerNames",trevelerNames);
		 
		 model.addAttribute("accountDate", orderpayList.get(0).getAccountDate());
		 
		 /*
    	  * wangxinwei  added 20151012， 客人报名收款单  和打印  如如果 已经确认收款要显示收款日期
    	  * ----- 除拉美途，北京环球行国际旅行社有限责任公司  都按照此规则
    	  * 需求C221
		  */
		 Long companyId = UserUtils.getUser().getCompany().getId();
		 String  companyName = UserUtils.getUser().getCompany().getCompanyName();
		 if (companyId==88||companyId==68) {
			//对应需求编号0041
			 if(companyId==68){
				 model.addAttribute("isAsAccount", orderpayList.get(0).getIsAsAccount());
			 }else{
				 model.addAttribute("isAsAccount", 1000);
			 }
		 }else {
			 model.addAttribute("isAsAccount", orderpayList.get(0).getIsAsAccount());
		 };
		 
		 
		 if("拉美途".equals(companyName))
			 model.addAttribute("payConfirmDate", "");
		 else
			 model.addAttribute("payConfirmDate", orderpayList.get(0).getUpdateDate()); 

		 
		 
		 //对应需求号   0002，增加  开票状态   和  开票金额
		 
		 List<Orderinvoice> orderinvoices  = orderinvoiceDao.findOrderinvoiceByOrderIdOrderTypeStatusINOneTwo(Integer.parseInt(vorderid),6);
		 
		 if (null!=orderinvoices&&orderinvoices.size()>0) {
			 
			 String invoiceCount =  OrderCommonUtil.getOrderInvoiceMoney("6",vorderid);
			 if (!"0.00".equals(invoiceCount)) {
				 model.addAttribute("invoiceStatus", "已开票");
				 model.addAttribute("invoiceCount", "¥"+invoiceCount);
			 }else{
				 model.addAttribute("invoiceStatus", "待开票");
				 model.addAttribute("invoiceCount", "¥0.00");
			 }
			 
		 }else{
			 model.addAttribute("invoiceStatus", "");
			 model.addAttribute("invoiceCount", "");
		 }
		 
		//7a816f5077a811e5bc1e000c29cf2586  环球行
		 model.addAttribute("companyUUID", companyUUID);
		 
		 
		 return "modules/order/printPayLog";//客人报名收款单

    }
    
    
    @RequestMapping(value = "showBatchPrint")
    public String showBatchPrint(Model model, HttpServletRequest request) {
    	
    	String paylistJSON = request.getParameter("paylist");
    	if (StringUtils.isBlank(paylistJSON)) {
    		return null;
    	}
    	
    	Long companyId = UserUtils.getUser().getCompany().getId();
    	String companyName = UserUtils.getUser().getCompany().getCompanyName();
    	String companyUUID = UserUtils.getUser().getCompany().getUuid();
    	
    	//7a816f5077a811e5bc1e000c29cf2586  环球行
    	model.addAttribute("companyUUID", companyUUID);
    	
    	List<Map<Object, Object>> printList = new ArrayList<>();
    	JSONArray paylist = JSONArray.fromObject(paylistJSON);
    	for (int i = 0; i < paylist.size(); i++) {
    		Map<Object, Object> print = new HashMap<>();
    		
    		JSONObject pay = paylist.getJSONObject(i);
    		String orderPaySerialNum = pay.optString("orderPaySerialNum");
    		String vpid = pay.optString("vpid");
    		String vorderid = pay.optString("vorderid");
//    		String isAsAccount = pay.optString("isAsAccount");
    		
    		VisaOrder visaOrder= visaOrderService.findVisaOrder(Long.parseLong(vorderid));
    		List<Orderpay> orderpayList = orderpayDao.findOrderpaByOrderPaySerialNum(orderPaySerialNum);
    		Date dt = new Date();
    		
    		if (null != orderpayList && orderpayList.size() > 0) {
    			if (null == orderpayList.get(0).getPrintTime()) {
    				print.put("printTime", DateUtils.formatCustomDate(dt, "yyyy/MM/dd HH:mm"));
    			} else {
    				print.put("printTime", DateUtils.formatCustomDate((Date) orderpayList.get(0).getPrintTime(), "yyyy/MM/dd HH:mm"));
    			}
    		}
    		
    		//计算多币种总价
    		String totalmoney = logService.getOrderTravellersPayed(orderPaySerialNum);
    		print.put("toatalMoneyBig", digitUppercase(Double.parseDouble(totalmoney)));
    		print.put("toatalMoney", fmtMicrometer(totalmoney));
    		//填写日期
    		print.put("startDate", visaOrder.getCreateDate());
    		
    		//产品名称
    		VisaProducts visaProducts = VisaProductsService.findByVisaProductsId(Long.parseLong(vpid));
    		print.put("product",visaProducts.getProductName());
    		
    		//来款单位信息
    		print.put("payerName", orderpayList.get(0).getPayerName()); 
    		print.put("fromPayerName", orderpayList.get(0).getBankName()); 
    		print.put("fromBankAccount", orderpayList.get(0).getBankAccount());
    		//处理付款备注无法显示的问题
    		print.put("remarks", orderpayList.get(0).getRemarks()); 
    		
    		//----------收款账户-----------
    		print.put("toBankNname", orderpayList.get(0).getToBankNname()); 
    		print.put("toBankAccount", orderpayList.get(0).getToBankAccount());
   		 
    		//----------交款人，经办人-----------
    		print.put("operator", orderpayList.get(0).getCreateBy().getName()); 
   		 
    		//----------收款人-----------未达账不显示收款人
    		if(null == orderpayList.get(0).getIsAsAccount()) {
    			print.put("payeee", ""); 
    		}else{
    			print.put("payeee", orderpayList.get(0).getUpdateBy().getName());
    		}
   		 
    		//-----------支付宝账号-------
    		print.put("toAlipayAccount", orderpayList.get(0).getToAlipayAccount());
    		print.put("toAlipayName", orderpayList.get(0).getToAlipayName());
    		print.put("orderPaySerialNum", orderPaySerialNum); 
    		print.put("vorderid", vorderid); 
    		print.put("vpid", vpid);
    		
    		if ("7a816f5077a811e5bc1e000c29cf2586".equals(companyUUID)) {
    			print.put("group_code",visaOrder.getGroupCode());	
    		}else{
    			print.put("group_code",visaProducts.getGroupCode());// 对应需求C460V3  签证订单团号  统一取签证订单所关联的产品团号
    		}
    		
    		print.put("money"," 签证费");//改为只显示签证费
    		String countryName_cn =  CountryUtils.getCountryName(Long.parseLong(visaProducts.getSysCountryId()+""));
    		print.put("countryName_cn",countryName_cn);
    		
    		//-----获取批量付款游客名单-----
    		String trevelerNames = logService.getOrderTravellersPayedNames(orderPaySerialNum);
    		print.put("travllerNames", trevelerNames);
    		print.put("accountDate", orderpayList.get(0).getAccountDate());
    		
    		if (companyId==88||companyId==68) {
    			//对应需求编号0041
    			if (companyId==68) {
    				print.put("isAsAccount", orderpayList.get(0).getIsAsAccount());
    			}else{
    				print.put("isAsAccount", 1000);
    			}
    		}else {
    			print.put("isAsAccount", orderpayList.get(0).getIsAsAccount());
    		}
    		
    		if ("拉美途".equals(companyName)) {
    			print.put("payConfirmDate", "");
    		} else {
    			print.put("payConfirmDate", orderpayList.get(0).getUpdateDate());
    		}
    		
    		//对应需求号   0002，增加  开票状态   和  开票金额
    		List<Orderinvoice> orderinvoices  = orderinvoiceDao.findOrderinvoiceByOrderIdOrderTypeStatusINOneTwo(Integer.parseInt(vorderid),6);
    		if (null!=orderinvoices&&orderinvoices.size()>0) {
    			String invoiceCount = OrderCommonUtil.getOrderInvoiceMoney("6",vorderid);
    			if (!"0.00".equals(invoiceCount)) {
    				print.put("invoiceStatus", "已开票");
    				print.put("invoiceCount", "￥"+invoiceCount);
    			}else{
    				print.put("invoiceStatus", "待开票");
    				print.put("invoiceCount", "￥0.00");
    			}
    		}else{
    			print.put("invoiceStatus", "");
    			print.put("invoiceCount", "");
    		}
    		
    		printList.add(print);
    	}
    	
    	model.addAttribute("list", printList);
    	return "modules/order/batchPrintPayLog";
    }
    
    
    //---------处理付款金额大小写转换，数字千位符  开始---------
	 /**
     * 数字金额大写转换，思路，先写个完整的然后将如零拾替换成零
     * 要用到正则表达式
     */
    public static String digitUppercase(double n){
        String fraction[] = {"角", "分"};
        String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        String unit[][] = {{"元", "万", "亿"}, {"", "拾", "佰", "仟"}};
        String head = n < 0? "红字": ""; ////负 -》红字
        n = Math.abs(n);  
        String s = "";
        for (int i = 0; i < fraction.length; i++) {
            s += (digit[(int)(Math.floor(n * 10 * Math.pow(10, i)) % 10)] + fraction[i]).replaceAll("(零.)+", "");
        }
        if(s.length()<1){
            s = "整";   
        }
        int integerPart = (int)Math.floor(n);
 
        for (int i = 0; i < unit[0].length && integerPart > 0; i++) {
            String p ="";
            for (int j = 0; j < unit[1].length && n > 0; j++) {
                p = digit[integerPart%10]+unit[1][j] + p;
                integerPart = integerPart/10;
            }
            s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
        }
        return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
    }
    
	public String fmtMicrometer(String text){
		DecimalFormat df = null;
		df = new DecimalFormat("###,##0.00");
		double number = 0.0;
		try {
			number = Double.parseDouble(text);
		} catch (Exception e) {
			number = 0.0;
		}
		return df.format(number);
	}
    
    
    //---------处理付款金额大小写转换，数字千位符  结束---------
    
	/**
	 * 
	* @Title: downloadList
	* @Description: TODO(下载收款单、借款单)
	* @param @param id
	* @param @param orderType
	* @param @param type
	* @param @param request
	* @param @param response
	* @param @return
	* @param @throws IOException
	* @param @throws TemplateException
	* @return ResponseEntity<byte[]>    返回类型
	* @throws
	 */
	
	@RequestMapping(value ="dst")
	public ResponseEntity<byte[]> dst(String orderType,
			HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateException{
		
		String orderPaySerialNum =  request.getParameter("orderPaySerialNum");
    	String vorderid =  request.getParameter("vorderid");
    	String vpid =  request.getParameter("vpid");
    	//解决URL传中文乱码问题
    	//String productName = new String(request.getParameter("productName").getBytes("iso8859-1"),"utf-8");
    	
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName="";
    		fileName =  "客人报名收款单" + nowDate + ".doc"  ;
    	File file =logService.createReceiptFile(orderPaySerialNum,vorderid,vpid);
    	ServletUtil.downLoadFile(response, fileName, file.getPath());
		return null;
	}
 

    
    /**
     * 签证游客支付驳回
     * @param response
     * @param model
     * @param request
     * @throws Exception
     */
    @RequestMapping(value ="rejectConfirmOper")
	@ResponseBody
    public Object rejectConfirmOper(HttpServletResponse response,
       	    Model model, HttpServletRequest request) throws Exception { 
    	Map<String, Object> data = new HashMap<String, Object>();
    	
    	model.addAttribute("sign",1);
    	String orderPaySerialNum = request.getParameter("orderPaySerialNum");
		String vorderid = request.getParameter("vorderid");
		String reason = request.getParameter("reason");
		if(reason != null) {
			reason = reason.replaceAll(" ", "");
		}
		VisaOrder visaOrder= visaOrderService.findVisaOrder(Long.parseLong(vorderid));
		List<Orderpay> orderpayList = orderpayDao.findOrderpaByOrderPaySerialNumNew(orderPaySerialNum,visaOrder.getOrderNo());
		//进行批量驳回操作
		for (Orderpay orderpay : orderpayList) {
			orderPayMoreService.rejectConfirmOper(orderpay.getId()+"","1",reason);
	    	VisaOrder order = logService.getVisaOrderByOrderNo(visaOrder.getOrderNo());
	    	Traveler traveler = logService.getTravleById(orderpay.getTravelerId()+"");
	    	//订单支付总记录总价
	    	List<Map<Object, Object>> amount = logService.getAmountByUid(order.getPayedMoney()) ; 
	    	//驳回的金额
	    	List<Map<Object, Object>> amount2 = logService.getAmountByUid(orderpay.getMoneySerialNum());// amount  amount2执行减法操作
	    	//游客支付金额总计
	    	List<Map<Object, Object>> amounts = logService.getAmountByUid(traveler.getPayedMoneySerialNum()); //执行清零操作
	    	logService.updateAmountByUid(amount,amount2,amounts,orderpay.getTravelerId()+"");
		}
		 
    	data.put("rejectMark", "驳回成功！");
    	return data;
    }
    
    
    
    /**
     * 根据 orderPaySerialNum 更新打印状态
     * @param response
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value ="updatePrintTime")
	@ResponseBody
	@Transactional
    public Object updatePrintTime(HttpServletResponse response,
       	    Model model, HttpServletRequest request) throws Exception { 
    	Map<String, Object> data = new HashMap<String, Object>();
    	
	   	String orderPaySerialNum =  request.getParameter("orderPaySerialNum");
    	List<Orderpay> orderpayList = orderpayDao.findOrderpaByOrderPaySerialNum(orderPaySerialNum);
    	if (null!=orderpayList&&orderpayList.size()>0) {
    		if (null==orderpayList.get(0).getPrintFlag()||null==orderpayList.get(0).getPrintTime()) {
    			logService.updateOrderPayByOrderPaySerialNum(new Date(),orderPaySerialNum);
    			data.put("result", "1");
			}else {
				data.put("result", "0");
			}
    		
		} 
    	return data;
    }
    
    /**
     * 根据 orderPaySerialNum 序列批量更新打印状态
     * @author yang.wang@quauq.com
     * @date 2016.10.26
     * */
    @Transactional
    @ResponseBody
    @RequestMapping(value = "updateBatchPrintTime")
    public Map<String, Object> updateBatchPrintTime(String orderPaySerialNums) {
    	
    	if (StringUtils.isBlank(orderPaySerialNums)) {
    		return null;
    	}
    	
    	Map<String, Object> map = new HashMap<>();
    	String[] orderPaySerialNumList = orderPaySerialNums.split(",");
    	for (String orderPaySerialNum : orderPaySerialNumList) {
        	List<Orderpay> orderpayList = orderpayDao.findOrderpaByOrderPaySerialNum(orderPaySerialNum);
        	if (null != orderpayList && orderpayList.size() > 0) {
        		if (null == orderpayList.get(0).getPrintFlag() || null == orderpayList.get(0).getPrintTime()) {
        			logService.updateOrderPayByOrderPaySerialNum(new Date(), orderPaySerialNum);
    			}
//        		else {
//    				map.put("result", "0");
//    				return map;
//    			}
    		}
    	}
    	
    	map.put("result", "1");
    	return map;
    }
    
    /**
     * 驳回页面弹出框
     * @param request
     * @param response
     * @param model
     * @author hxd
     * @return
     */
	@RequestMapping(value = "getOrderPayForRejectOne", method = RequestMethod.GET)
	public Object getOrderPayForRejectOne(HttpServletRequest request,HttpServletResponse response,Model model){
	    model.addAttribute("sign", 1);
	    return "modules/order/list/rejectConfirm";
	}
    
	/**
	 * 销售身份签证修改页面（销售身份），保存游客及签证
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value ="showsss")
	public String showsss(HttpServletResponse response, Model model, HttpServletRequest request) throws Exception{
		String data2 = request.getParameter("data2");// xxxyy=33&inputClearPrice=1000.00&xxxyy=55&inputClearPrice=7.00
		String payPriceSerialNum= request.getParameter("payPriceSerialNum");
		String orderId = request.getParameter("orderId");
		logService.updateMoney(data2,payPriceSerialNum,orderId);
		return "sss";
	}
	
/**
 * 异步获取游客支付数据
 * @param response
 * @param model
 * @param request
 * @return
 * @throws Exception
 * @author hxd
 */
    @RequestMapping(value ="getTravellerList")
	@ResponseBody
	public Object getTravellerList(HttpServletResponse response,Model model, HttpServletRequest request) throws Exception {
    	Map<String, Object> data = new HashMap<String, Object>();
    	String travellerinfo  = logService.getTravellerInfo(request.getParameter("serialNum"),request);
    	data.put("travellerinfo", travellerinfo);
    	return data;
    }
    
    /**
	 * 导出预约表
	 * @param
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping(value={"exportVisaorderPayExcel"})
	public HttpServletResponse exportVisaorderPayExcel( HttpServletRequest req, HttpServletResponse resp) throws UnsupportedEncodingException {
		String data = req.getParameter("data");
		data = java.net.URLDecoder.decode(data, "utf-8");
		String [] dataArray = data.split("&");
		Map<String, String> map	 = new HashMap<>();
		for(int i=0;i<dataArray.length;i++)
		{
			String [] dt = 	dataArray[i].split("=");
			if(dt.length ==1) {
				map.put(dt[0], "");
			}else {
				map.put(dt[0], dt[1]);
			}
		}
		try {
			List<Map<Object, Object>> list = logService.findVisaOrderList1(map);
			Workbook workbook = ExcelUtils.exportVisaOrderPayList(list);
			ServletUtil.downLoadExcel(resp, "签证订单收款.xls", workbook);
			//ExportExcel.createExcleNew("签证订单收款",list, req, resp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	
}
