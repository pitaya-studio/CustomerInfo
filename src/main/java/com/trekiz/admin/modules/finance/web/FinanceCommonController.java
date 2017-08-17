package com.trekiz.admin.modules.finance.web;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.query.entity.SelectJson;
import com.trekiz.admin.common.query.service.ISelectService;
import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityReserveOrderService;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.agent.utils.AgentInfoUtils;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.finance.FinanceUtils;
import com.trekiz.admin.modules.finance.service.IFinanceCommonService;
import com.trekiz.admin.modules.finance.service.ReturnDifferenceService;
import com.trekiz.admin.modules.finance.util.ExcelExportCommonUtils;
import com.trekiz.admin.modules.finance.util.MoneyUtils;
import com.trekiz.admin.modules.island.util.StringUtil;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.MoneyAmountUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;

/**
 * 抽取财务统计相关模块
 */
@Controller
@RequestMapping(value = "${adminPath}/finance/manage")
public class FinanceCommonController extends BaseController {

    @Autowired
    private ActivityReserveOrderService activityReserveOrderService;
    @Autowired
    private VisaOrderService visaOrderService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private CostManageService costManageService;
    @Autowired
    private OrderCommonService orderService;
    @Autowired
    private VisaProductsService visaProductsService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private AgentinfoService agentinfoService;
    @Autowired
    private ISelectService selectService;
    @Autowired
    private IFinanceCommonService financeCommonService;
    @Autowired
    private ReturnDifferenceService returnDifferenceService;
    
    @RequestMapping(value ="showFinanceList/{showType}/{orderStatus}")
    public String showFinanceList(@PathVariable String showType, @PathVariable String orderStatus,
                                @ModelAttribute TravelActivity travelActivity, HttpServletResponse response,
                                Model model, HttpServletRequest request) throws Exception {
        //来款银行
        String bankName = request.getParameter("bank");
        model.addAttribute("bankNamee",bankName);
        model.addAttribute("isSelect", StringUtil.isNotBlank(bankName));
        //支付方式
        model.addAttribute("payType",request.getParameter("payType"));
        Office office = UserUtils.getUser().getCompany();
        Long companyId = office.getId();
        String companyUUID = office.getUuid();

        //查询条件
        Map<String,String> mapRequest = Maps.newHashMap();
        //参数处理：渠道ID、订单编号或团期编号、联系人、计调名称、团期起始开始时间、团期起始结束时间、订单号、团期号
        String paras = "agentId,orderNumOrGroupCode,orderPersonName,activityCreateName,groupOpenDateBegin,groupOpenDateEnd,orderNum,groupCode," +
                "payCreateDateBegin,payCreateDateEnd,groupCloseDateBegin,groupCloseDateEnd,orderTimeBegin,orderTimeEnd,payAmountStrat,payAmountEnd," +
                "accountDateBegin,accountDateEnd,payerName,toBankNname,createDateBegin,createDateEnd,printFlag,paymentType,createTimeMin,createTimeMax";
        OrderCommonUtil.handlePara(paras, mapRequest, model, request);
        mapRequest.put("receiptConfirmationDateBegin", request.getParameter("receiptConfirmationDateBegin"));
        mapRequest.put("receiptConfirmationDateEnd", request.getParameter("receiptConfirmationDateEnd"));
        mapRequest.put("confirmCashierDateBegin", request.getParameter("confirmCashierDateBegin"));
        mapRequest.put("confirmCashierDateEnd", request.getParameter("confirmCashierDateEnd"));
        mapRequest.put("payType", request.getParameter("payType"));
        mapRequest.put("bankName", request.getParameter("bank"));
        model.addAttribute("receiptConfirmationDateBegin",request.getParameter("receiptConfirmationDateBegin"));
        model.addAttribute("receiptConfirmationDateEnd",request.getParameter("receiptConfirmationDateEnd"));
        model.addAttribute("confirmCashierDateBegin",request.getParameter("confirmCashierDateBegin"));
        model.addAttribute("confirmCashierDateEnd",request.getParameter("confirmCashierDateEnd"));
        SelectJson json = selectService.getOfficePlatBankInfoForSelectJson();
        model.addAttribute("toBank",json.getData());//下拉收款银行
        String orderShowType = request.getParameter("orderShowType");
        String orderBy = request.getParameter("orderBy");
        String saler = request.getParameter("saler");
        String jds = request.getParameter("jd");//查询时，获取选择的计调人员
        String isAccounted = request.getParameter("isAccounted");//查询时，获取是否到账的参数条件
        String option = request.getParameter("option");
        String creator = request.getParameter("creator");
        String payType = request.getParameter("payType");//支付方式
        //订单类型

        String orderS = StringUtils.isBlank(request.getParameter("orderS")) ?
                String.valueOf(Context.ORDER_TYPE_ALL) : request.getParameter("orderS");
        mapRequest.put("option", option);
        mapRequest.put("saler", saler);
        mapRequest.put("creator",creator);
        mapRequest.put("payType", payType);
        /*增加计调人员查询条件*/
        mapRequest.put("jdUserId", jds);
        /* 增加是否到账的查询条件*/
        mapRequest.put("isAccounted", isAccounted);
        if(StringUtils.isNotEmpty(saler)){
            //选择了销售人员
            mapRequest.put("saler", saler);
        }

        //排序方式
        if(StringUtils.isBlank(orderBy)){
        	//-------bug15459:将updateDate改为createDate放入mapRequest-------------------
            orderBy = "createDate DESC";
            if(Context.SUPPLIER_UUID_CPLY.equals(companyUUID)){
                //诚品旅游默认按照出团日期排序
                orderBy = "groupOpenDate DESC";
            }
            mapRequest.put("orderBy", orderBy);
        }

        if(StringUtils.isNotBlank(orderShowType) &&
                !StringUtils.equalsIgnoreCase("199", showType) &&
                !StringUtils.equalsIgnoreCase("101", showType)) {
            showType = orderShowType;
        }
        List<Map<String,Object>> list = AgentInfoUtils.getAgentList(companyId);	// 渠道选择下拉列表项
        Page<Map<Object, Object>> pageOrder = null;
        //切位订单列表
        if("reserve".equals(option)) {
            mapRequest.put("orderS", orderS);
            //确认收款日期
            mapRequest.put("receiptConfirmDateBegin", request.getParameter("receiptConfirmDateBegin"));
            mapRequest.put("receiptConfirmDateEnd", request.getParameter("receiptConfirmDateEnd"));
            model.addAttribute("receiptConfirmDateBegin", request.getParameter("receiptConfirmDateBegin"));
            model.addAttribute("receiptConfirmDateEnd", request.getParameter("receiptConfirmDateEnd"));
            pageOrder = activityReserveOrderService.findReserveOrderList(showType, new Page<Map<Object, Object>>(request, response), orderBy, orderShowType, mapRequest, null);
            list = AgentInfoUtils.getQuauqAndOwnAgentList(companyId);
        }else if("visa".equals(option)) { //签证订单押金列表
            //押金收款确认查询条件
            mapRequest.put("printFlag", request.getParameter("printFlag"));
            mapRequest.put("groupDateBegin", request.getParameter("groupDateBegin"));
            mapRequest.put("groupDateEnd", request.getParameter("groupDateEnd"));
            model.addAttribute("printFlag", request.getParameter("printFlag"));
            model.addAttribute("groupDateBegin", request.getParameter("groupDateBegin"));
            model.addAttribute("groupDateEnd", request.getParameter("groupDateEnd"));
            if(StringUtils.isNotBlank(orderBy) && orderBy.equals("groupOpenDate DESC")){
                orderBy = "updateDate DESC";
            }
            pageOrder = visaOrderService.findVisaOrderList(showType, new Page<Map<Object, Object>>(request, response), orderBy, orderShowType, mapRequest, null);
        }else if("pay".equals(option)) {//成本付款列表
            mapRequest.put("companyId", companyId.toString());
            mapRequest.put("companyUUID", companyUUID);
            mapRequest.put("supplier", request.getParameter("supplierInfo"));
            mapRequest.put("payState", request.getParameter("pay"));
            mapRequest.put("currency", request.getParameter("currency"));
            mapRequest.put("startMoney", request.getParameter("startMoney"));
            mapRequest.put("endMoney", request.getParameter("endMoney"));
            mapRequest.put("printFlag", request.getParameter("printFlag"));
            mapRequest.put("jds", request.getParameter("jd"));
            mapRequest.put("moneyNum", request.getParameter("moneyNum"));
            model.addAttribute("moneyNum", request.getParameter("moneyNum"));
            model.addAttribute("printFlag", request.getParameter("printFlag"));
            if(StringUtils.isNotBlank(orderBy) && orderBy.equals("groupOpenDate DESC")){
                orderBy = "updateDate DESC";
            }
            pageOrder = costManageService.findCostRecordList(orderS, new Page<Map<Object, Object>>(request, response), orderBy, mapRequest, null);
            list = AgentInfoUtils.getQuauqAndOwnAgentList(companyId);
        }
        else if("visaOrder".equals(option)) {
            //押金收款确认查询条件
            pageOrder = visaOrderService.findVisaOrderList(showType, new Page<Map<Object, Object>>(request, response), orderBy, orderShowType, mapRequest, null);
        }
        else {
            if(StringUtils.isBlank(option)) {
            	//未作处理
                pageOrder = orderService.findOrderListByPayType(showType, orderStatus, new Page<Map<Object, Object>>(request, response), travelActivity, orderBy, orderShowType, mapRequest, null);
            } else {
                String orderPersonId = request.getParameter("orderPersonId");
                mapRequest.put("orderPersonId", orderPersonId);
                model.addAttribute("orderPersonId", orderPersonId);
                mapRequest.put("pageNo", request.getParameter("pageNo"));
                mapRequest.put("pageSize", request.getParameter("pageSize"));
                model.addAttribute("orderPersonList", UserUtils.getSalers(companyId));
                if(!option.equals("order")) {
                    String queryDepartmentId = request.getParameter("queryDepartmentId");
                    mapRequest.put("queryDepartmentId", queryDepartmentId);
                    //增加按部门查询条件,结束
                    //20151026增加页面参数option,付款状态,达帐状态,分页参数,签证国家
                    mapRequest.put("option", option);
                    mapRequest.put("payStatus", request.getParameter("payStatus"));
                    mapRequest.put("accountStatus", request.getParameter("accountStatus"));
                    String sysCountryId = request.getParameter("sysCountryId");
                    sysCountryId = " ".equals(sysCountryId) ? null : sysCountryId;
                    mapRequest.put("sysCountryId", sysCountryId);
                    model.addAttribute("payStatus", request.getParameter("payStatus"));
                    model.addAttribute("accountStatus", request.getParameter("accountStatus"));
                    model.addAttribute("sysCountryId", sysCountryId);
                    //签证国家
                    List<Object[]> countryInfoList = visaProductsService.findCountryInfoList(companyId);
                    model.addAttribute("countryInfoList", countryInfoList);
                    String genre = "";
                    if("detail".equals(option)) {
                        genre = "1,2,3,4,5";
                    }else{
                        genre = "1,2,3,4";
                    }
                    if (StringUtils.isNotBlank(option)) {
                    	//查询应收总金额，已达账金额，已付金额，应收总金额，未收总金额，查询总人数
                        List<String[]> orderSum = orderService.findSum1(showType, orderS, genre, travelActivity, mapRequest, null);
                        if(orderSum != null && orderSum.size() != 0) {
                            if(orderSum.get(0) != null) {
                                model.addAttribute("sumTotalMoney",orderSum.get(0)[0]);
                                model.addAttribute("sumPayedMoney",orderSum.get(0)[1]);
                                model.addAttribute("sumPerson",orderSum.get(0)[2]);
                                model.addAttribute("sumNotPayedMoney",orderSum.get(0)[3]);
                            }
                        }
                    }
//                    //添加是否占位确认筛选条件
//                    String isSeizedConfirmed = request.getParameter("isSeizedConfirmed");
//                    mapRequest.put("isSeizedConfirmed", isSeizedConfirmed);
//                    model.addAttribute("isSeizedConfirmed", isSeizedConfirmed);
//                    if(office.getIsSeizedConfirmation() == 1){
//                    	model.addAttribute("isSeizedConfirmation", true);
//                    }else{
//                    	model.addAttribute("isSeizedConfirmation", false);
//                    }
                    //不分类别查询所有订单的信息
                    pageOrder = orderService.getOrderRecord(new Page<Map<Object, Object>>(request, response), orderS, mapRequest);
                    //获取渠道（包括该批发商下的自用渠道和所有已启用的quauq渠道）
                    list = AgentInfoUtils.getQuauqAndOwnAgentList(companyId);
                    //538 需求 若存在差额 ，总额中把差额减去
                    for(Map<Object,Object> map : pageOrder.getList()){
                    	if(map.get("orderType").toString().equals("2")){
                    		String strTotalMoney = returnDifferenceService.getOrderTotalMoneyStrByOrderId(Long.parseLong(map.get("id").toString()));
                        	map.put("strTotalMoney", strTotalMoney);
                    	}else{
                    		String strTotalMoney = MoneyAmountUtils.getMoneyAmount(map.get("totalMoney").toString());
                    		map.put("strTotalMoney", strTotalMoney);
                    	}
                    }
                }else{
                    String printFlag = request.getParameter("printFlag");
                    mapRequest.put("printFlag", printFlag);
                    model.addAttribute("printFlag", printFlag);
                    Page<Map<Object,Object>> page = new Page<Map<Object, Object>>(request, response);
                    page.setOrderBy(orderBy);
                    pageOrder = orderService.getByPayStatusAndNoatAccount( page,orderS, mapRequest);
                    list = AgentInfoUtils.getQuauqAndOwnAgentList(companyId);
                }
            }
        }
        model.addAttribute("page", pageOrder);
        //判断是否是越柬行踪
        model.addAttribute("isYJXZ", Context.SUPPLIER_UUID_YJXZ.equals(companyUUID));
        //判断是否是环球行
        model.addAttribute("isHQX", Context.SUPPLIER_UUID_HQX.equals(companyUUID));
        //判断是否是拉美途
        model.addAttribute("isLMT", Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUUID));
        //起航假期
        model.addAttribute("isQHJQ", Context.SUPPLIER_UUID_QHJQ.equals(companyUUID));

        model.addAttribute("companyUuid",companyUUID);
        model.addAttribute("supllierUuid", companyUUID);
        model.addAttribute("confirmPay",office.getConfirmPay());

        model.addAttribute("payType",payType);
        model.addAttribute("showType",showType);
        model.addAttribute("orderStatus",orderStatus);
        model.addAttribute("orderShowType",orderShowType);
        model.addAttribute("orderS", orderS);
        model.addAttribute("saler", saler);
        model.addAttribute("creator", creator);
        model.addAttribute("jds", jds); //计调人员
        model.addAttribute("isAccounted", isAccounted); //是否到账
        model.addAttribute("companyId", companyId);
        model.addAttribute("companyUUID", companyUUID);

        model.addAttribute("orderTypeStr", OrderCommonUtil.getStringOrdeType(orderStatus));

        int index = -1;
        for (Map map:list) {
            String agentName = FinanceUtils.blankReturnEmpty(map.get("agentName"));
            if("非签约渠道".equals(agentName)){
                index++;
                if(Context.SUPPLIER_UUID_DYGL.equals(companyUUID)){
                    map.put("agentName","未签");
                }
            }
        }
        if(-1 == index){
            Map map = new HashMap();
            map.put("id",-1L);
            map.put("agentName","非签约渠道");
            if(Context.SUPPLIER_UUID_DYGL.equals(companyUUID)){
                map.put("agentName","未签");
            }
            list.add(map);
        }
        model.addAttribute("agentinfoList", list);
        //部门
        model.addAttribute("departmentSet", UserUtils.getUserDepartment());
        //订单类型
        model.addAttribute("orderTypes", DictUtils.getDict2List("order_type"));
        // 销售,内部销售人员的名单
        if(!"pay".equals(option)) {
            Map<String, String> salerList = agentinfoService.findInnerSales(companyId);
            model.addAttribute("agentSalers", salerList);
        }
        // 下单人
        model.addAttribute("creatorList", UserUtils.getSalers(companyId));
		/*获取批发商下所有拥有计调角色人员和发布产品人员*/
        model.addAttribute("agentJd", UserUtils.getOperators(companyId));

        //日信观光UUID
        model.addAttribute("RXGG", Context.SUPPLIER_UUID_RXGG);
        //跳转链接
        if(Context.ORDER_PAYSTATUS_FINANCE.equalsIgnoreCase(showType)) {
            if("pay".equals(option)) {
                //环球行批发商
                model.addAttribute("supplierList", UserUtils.getSupplierInfoList(companyId, "supplierName"));
                //币种列表
                model.addAttribute("currencyList", currencyService.findCurrencyList(companyId));
                model.addAttribute("supplierId", request.getParameter("supplierInfo"));
                model.addAttribute("pay", request.getParameter("pay"));
                model.addAttribute("currencyId", request.getParameter("currency"));
                model.addAttribute("startMoney", request.getParameter("startMoney"));
                model.addAttribute("endMoney", request.getParameter("endMoney"));
                return "modules/order/orderListForPay";//成本付款
            }else if("reserve".equals(option)) {
                model.addAttribute("payTypes", DictUtils.getDicMap(Context.PAY_TYPE));
                return "modules/order/orderListForReserve";//切位收款
            }else if("visa".equals(option)) {
                model.addAttribute("payTypes", DictUtils.getDicMap(Context.PAY_TYPE));
                return "modules/order/orderListForVisa";//签证押金收款
            }else {
        		/*
        		 * author:wuqiang --交易明细
        		 * 当前用户所属批发商下的所有部门
        		 * */
                List<Department> departmentList = departmentService.findByOfficeId(companyId);
                model.addAttribute("departmentList",departmentList);
                model.addAttribute("queryDepartmentId", request.getParameter("queryDepartmentId"));

                model.addAttribute("menuId",90);
                model.addAttribute("hasPermission", hasPermission()); // 575 wangyang 2017.1.4
                return "modules/order/orderListForFinance";//交易明细
            }
        } else if(Context.ORDER_PAYSTATUS_SYNC_CHECK.equalsIgnoreCase(showType)) {
            model.addAttribute("menuId",90);
            return "modules/order/orderListForDz";//订单收款
        }
        return "modules/order/list/orderList";//订单列表
    }
    
    /**
     * 需求575 客户确认权限变更为订单可配置，判断用户是否具有其中一项权限
     * */
    private boolean hasPermission() { 
    	
    	String[] permissions = {"singleOrder", "looseOrder", "airticketOrderForSale", 
    			"airticketOrderForOp", "studyOrder", "bigCustomerOrder", "freeOrder", "cruiseOrder"};
    	
    	for (String permission : permissions) {
    		if (SecurityUtils.getSubject().isPermitted(permission + ":operation:customerConfirm")) {
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * 销售人员业绩列表
     * @param request
     * @param response
     * @param model
     * @return
     */
    @RequestMapping(value ="showSalesPerformance")
    public String showSalesPerformance(HttpServletRequest request,HttpServletResponse response,Model model){
        Long companyId = UserUtils.getUser().getCompany().getId();
        loadParams(request,model);
        model.addAttribute("salers",agentinfoService.findInnerSales(companyId));
        model.addAttribute("departments",selectService.loadDepartment(companyId));

        Page<Map<String,Object>> page = new Page<>(request,response);
        financeCommonService.getSalesPerformance(page,model.asMap());
        model.addAttribute("page",page);
        return "modules/order/salesPerformance";
    }

    private void loadParams(HttpServletRequest request,Model model){
        String groupCode = request.getParameter("groupCode");
        model.addAttribute("groupCode",groupCode);

        String departmentId = request.getParameter("departmentId");
        model.addAttribute("departmentId",departmentId);

        String salerId = request.getParameter("salerId");
        model.addAttribute("salerId",salerId);

        String productName = request.getParameter("productName");
        model.addAttribute("productName",productName);

        String beginDate = request.getParameter("beginDate");
        model.addAttribute("beginDate",beginDate);
    }
    
    /**
     * 下载销售人员业绩表
     * @param request
     * @param response
     * @param model
     * @author yudong.xu
     */
    @RequestMapping(value="downloadSalesPerformance")
    public void exportToExcel(HttpServletRequest request,HttpServletResponse response,Model model){
    	//参数加载（返回参数值）
        loadParams(request,model);
        String fileName;
        //开始时间
        String beginDate = request.getParameter("beginDate");
        if (StringUtils.isBlank(beginDate)){
            beginDate = "_月";
        }
        //文件名
        fileName = "销售人员" + beginDate + "销售业绩汇总表";
        //获得数据
        Map<String,Object> map = financeCommonService.getSalesPerformance(model.asMap());
        List<Map<String,Object>> result = (List<Map<String,Object>>)map.get("result");
        Map<Object,BigDecimal[]> sumInfo = (Map<Object,BigDecimal[]>)map.get("sumInfo");
        //生成excel
        Workbook wb = FinanceUtils.downloadSalesPerformance(result,sumInfo,fileName);
        //下载Excel
        ServletUtil.downLoadExcel(response,fileName + ".xls",wb);
    }

    /**
     * 579需求：财务模块付款类列表页面，增加Excel导出功能
     * 成本付款列表Excel下载(获取方式和成本付款列表一样)
     * @author gaoyang
     * @Time 2017-3-21 下午3:59:43
     * @param
     */
    @RequestMapping(value="getCostPaymentListExcel")
    public void getCostPaymentListExcel(HttpServletResponse response, HttpServletRequest request) {
    	String orderBy = request.getParameter("orderBy");
    	String option = request.getParameter("option");
        String creator = request.getParameter("creator");
        String payType = request.getParameter("payType"); // 支付方式
        String saler = request.getParameter("saler");
        String jds = request.getParameter("jd"); // 查询时，获取选择的计调人员
        String isAccounted = request.getParameter("isAccounted"); // 查询时，获取是否到账的参数条件
        Office office = UserUtils.getUser().getCompany();
        Long companyId = office.getId();
        String companyUUID = office.getUuid();
        // 查询条件
        Map<String,String> mapRequest = Maps.newHashMap();
        // 参数处理：渠道ID、订单编号或团期编号、联系人、计调名称、团期起始开始时间、团期起始结束时间、订单号、团期号
        String paras = "agentId,orderNumOrGroupCode,orderPersonName,activityCreateName,groupOpenDateBegin,groupOpenDateEnd,orderNum,groupCode," +
                "payCreateDateBegin,payCreateDateEnd,groupCloseDateBegin,groupCloseDateEnd,orderTimeBegin,orderTimeEnd,payAmountStrat,payAmountEnd," +
                "accountDateBegin,accountDateEnd,payerName,toBankNname,createDateBegin,createDateEnd,printFlag,paymentType,createTimeMin,createTimeMax";
        OrderCommonUtil.handlePara(paras, mapRequest, request);
        mapRequest.put("receiptConfirmationDateBegin", request.getParameter("receiptConfirmationDateBegin"));
        mapRequest.put("receiptConfirmationDateEnd", request.getParameter("receiptConfirmationDateEnd"));
        mapRequest.put("confirmCashierDateBegin", request.getParameter("confirmCashierDateBegin"));
        mapRequest.put("confirmCashierDateEnd", request.getParameter("confirmCashierDateEnd"));
        mapRequest.put("payType", request.getParameter("payType"));
        mapRequest.put("bankName", request.getParameter("bank"));
        mapRequest.put("option", option);
        mapRequest.put("saler", saler);
        mapRequest.put("creator",creator);
        mapRequest.put("payType", payType);
        // 增加计调人员查询条件
        mapRequest.put("jdUserId", jds);
        // 增加是否到账的查询条件
        mapRequest.put("isAccounted", isAccounted);
        if (StringUtils.isNotEmpty(saler)) {
            // 选择了销售人员
            mapRequest.put("saler", saler);
        }
        mapRequest.put("companyId", companyId.toString());
        mapRequest.put("companyUUID", companyUUID);
        mapRequest.put("supplier", request.getParameter("supplierInfo"));
        mapRequest.put("payState", request.getParameter("pay"));
        mapRequest.put("currency", request.getParameter("currency"));
        mapRequest.put("startMoney", request.getParameter("startMoney"));
        mapRequest.put("endMoney", request.getParameter("endMoney"));
        mapRequest.put("printFlag", request.getParameter("printFlag"));
        mapRequest.put("jds", request.getParameter("jd"));
        mapRequest.put("moneyNum", request.getParameter("moneyNum"));
    	// 订单类型
        String orderS = StringUtils.isBlank(request.getParameter("orderS")) ?
                String.valueOf(Context.ORDER_TYPE_ALL) : request.getParameter("orderS");
	    // 排序方式
	    if (StringUtils.isBlank(orderBy)) {
	    	//-------bug15459:将updateDate改为createDate放入mapRequest-------------------
	    	orderBy = "createDate DESC";
	    	if (Context.SUPPLIER_UUID_CPLY.equals(companyUUID)) {
	    		//诚品旅游默认按照出团日期排序
	    		orderBy = "groupOpenDate DESC";
	    	}
	    	mapRequest.put("orderBy", orderBy);
	    }        
        if (StringUtils.isNotBlank(orderBy) && orderBy.equals("groupOpenDate DESC")) {
            orderBy = "updateDate DESC";
        }
        // 取出全部数据
        Page<Map<Object, Object>> page = new Page<Map<Object, Object>>(request, response);
        page.setPageNo(1);
        page.setMaxSize(Integer.MAX_VALUE);
        Page<Map<Object, Object>> pageOrder = costManageService.findCostRecordList(orderS, page, orderBy, mapRequest, null);
        // 获取成本列表查询结果
    	List<Map<Object, Object>> list = pageOrder.getList();
    	int i = 0;
    	for (Map<Object, Object> map : list) {
    		i++;
    		// 获取列表序号
    		map.put("count", i);
    		// 申请日期
    		String payCreateDate = "";
    		if (map.get("payCreateDate") != null && !"".equals(map.get("payCreateDate").toString())) {
    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    			try {
					payCreateDate = sdf.format(sdf.parse(map.get("payCreateDate").toString()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
    		}
    		map.put("payCreateDate", payCreateDate);
    		// 列表页默认查询的就是付款审核通过的数据，所以此处直接写已通过
    		map.put("payCheck", "已通过");
    		// 获取团队类型
    		String orderTypeName = "";
    		if (map.get("orderType") != null && !"".equals(map.get("orderType").toString())) {
    			orderTypeName = DictUtils.getDictLabel(map.get("orderType").toString(), "order_type", "");
    		}
    		map.put("orderTypeName", orderTypeName);
    		// 地接社和渠道商
    		String djsName = ""; // 地接社名称
    		String qdsName = ""; // 渠道商名称
    		if (map.get("supplyName") != null && map.get("supplyType") != null) {
    			// 地接社名称
    			if ("0".equals(map.get("supplyType").toString())) {
    				djsName = map.get("supplyName").toString();
    			}
    			// 渠道商名称
    			if ("1".equals(map.get("supplyType").toString())) {
    				// 315需求,针对越柬行踪，将非签约渠道改为签约渠道
        			if ("非签约渠道".equals(map.get("supplyName").toString()) 
        					&& "7a81b21a77a811e5bc1e000c29cf2586".equals(companyUUID)) {
        				qdsName = "直客";
            		} else {
            			qdsName = map.get("supplyName").toString();
            		}
    			}
    		}
    		map.put("djsName", djsName);
    		map.put("qdsName", qdsName);
    		
    		// 币种名称
    		String currency = "";
    		String currencyForPay = ""; // 付款币种
    		String currencyForPayed = ""; // 已付币种
    		if (map.get("currencyId") != null && !"".equals(map.get("currencyId").toString())) {
    			currency = CurrencyUtils.getCurrencyNameOrFlag(Long.parseLong(map.get("currencyId").toString()), "1");
    		}
    		// 付款金额
    		String payMoney = "";
    		if (map.get("amount") != null && !"".equals(map.get("amount").toString())) {
    			DecimalFormat df = new DecimalFormat(",###,##0.00");
    			payMoney = df.format(Double.parseDouble(map.get("amount").toString()));
    			currencyForPay = currency;
    		}
    		map.put("payMoney", payMoney);
    		map.put("currencyForPay", currencyForPay);
    		// 已付金额
    		String payedMoney = "";
    		if (map.get("id") != null && !"".equals(map.get("id").toString()) 
    				&& map.get("orderType") != null && !"".equals(map.get("orderType").toString())) {
    			payedMoney = OrderCommonUtil.getRefundPayMoney(map.get("id").toString(), "1", map.get("orderType").toString());
    		}
    		if (StringUtils.isNotBlank(payedMoney)) {
    			payedMoney = MoneyUtils.getMoneyRemoveCurrency(payedMoney);
    			currencyForPayed = currency;
    		}
    		map.put("currencyForPayed", currencyForPayed);
    		map.put("payedMoney", payedMoney);
    		// 获取计调名称
    		String createByName = "";
    		if (map.get("createBy") != null && !"".equals(map.get("createBy").toString())) {
    			createByName = UserUtils.getUserNameByIds(map.get("createBy").toString());
    		}
    		map.put("createByName", createByName);
    		// 出纳确认处理
    		if (map.get("payFlag") != null) {
    			if ("0".equals(map.get("payFlag").toString())) {
        			map.put("payFlagName", "未付");
        		} else if ("1".equals(map.get("payFlag").toString())) {
        			map.put("payFlagName", "已付");
        		}
    		}
    		
    		// 出纳确认时间
    		String confirmCashierDate = "";
    		if (map.get("confirmCashierDate") != null && !"".equals(map.get("confirmCashierDate").toString())) {
    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    			try {
    				confirmCashierDate = sdf.format(sdf.parse(map.get("confirmCashierDate").toString()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
    		}
    		map.put("confirmCashierDate", confirmCashierDate);
    		
    		// 打印状态处理
    		if (map.get("printFlag") != null && "1".equals(map.get("printFlag").toString())) {
    			map.put("printFlagName", "已打印");
    		} else {
    			map.put("printFlagName", "未打印");
    		}
    	}
		// Excel文件名称
		String fileName = "成本付款";
		// 当导出数据超出65535行时,打包下载, 下面命名zip压缩包的中文名称部分
		String zipChineseName = "成本付款";
		// 表头数据
		String[] secondTitle = new String[] {"序号", "申请日期", "团号", "团队类型", "团队名称",
				"地接社", "渠道商", "款项", "币种", "付款金额", "币种" , "已付金额",
				"汇率", "计调", "付款审核状态", "出纳确认", "出纳确认时间", "打印状态"};
		// 每个Map<Object, Object>中的所有键
		String[]  commonName = {"count", "payCreateDate", "groupCode", "orderTypeName", "acitivityName",
				"djsName", "qdsName", "name", "currencyForPay", "payMoney", "currencyForPayed", "payedMoney",
				"rate", "createByName", "payCheck", "payFlagName", "confirmCashierDate", "printFlagName"};
		try {
			// Excel生成及下载
			ExcelExportCommonUtils.downLoadExcelFile(fileName, zipChineseName, secondTitle, commonName, list, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}