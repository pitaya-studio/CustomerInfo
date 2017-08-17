package com.trekiz.admin.modules.order.web;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.trekiz.admin.agentToOffice.T1.money.entity.T1MoneyAmount;
import com.trekiz.admin.agentToOffice.T1.money.service.T1MoneyAmountService;
import com.trekiz.admin.agentToOffice.T1.order.entity.T1PreOrder;
import com.trekiz.admin.agentToOffice.T1.order.service.T1PreOrderService;
import com.trekiz.admin.agentToOffice.T2.entity.Rate;
import com.trekiz.admin.agentToOffice.T2.utils.RateUtils;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.entity.ResponseJson;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.query.entity.SelectJson;
import com.trekiz.admin.common.query.service.ISelectService;
import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.ChineseToEnglish;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.FileUtils;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.StringNumFormat;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.excel.ExportExcel;
import com.trekiz.admin.common.utils.word.WordDownLoadUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.IntermodalStrategy;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.exception.OptimisticLockHandleException;
import com.trekiz.admin.modules.activity.exception.PositionOutOfBoundException;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.ActivityReserveOrderService;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.activity.service.IntermodalStrategyService;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.agent.service.SupplyContactsService;
import com.trekiz.admin.modules.agent.utils.AgentInfoUtils;
import com.trekiz.admin.modules.airticket.utils.AirTicketUtils;
import com.trekiz.admin.modules.airticketorder.entity.RefundBean;
import com.trekiz.admin.modules.cost.entity.CostRecord;
import com.trekiz.admin.modules.cost.service.CostManageService;
import com.trekiz.admin.modules.finance.entity.ReturnDifference;
import com.trekiz.admin.modules.finance.service.ReturnDifferenceService;
import com.trekiz.admin.modules.grouphandle.entity.GroupHandle;
import com.trekiz.admin.modules.grouphandle.service.GroupHandleService;
import com.trekiz.admin.modules.hotel.dao.HotelTravelerDao;
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.hotel.entity.HotelTraveler;
import com.trekiz.admin.modules.hotel.service.HotelMoneyAmountService;
import com.trekiz.admin.modules.hotel.service.HotelOrderService;
import com.trekiz.admin.modules.invoice.entity.Orderinvoice;
import com.trekiz.admin.modules.invoice.service.OrderinvoiceService;
import com.trekiz.admin.modules.island.dao.IslandTravelerDao;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.entity.IslandTraveler;
import com.trekiz.admin.modules.island.service.IslandMoneyAmountService;
import com.trekiz.admin.modules.island.service.IslandOrderService;
import com.trekiz.admin.modules.log.service.LogOrderService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.Costchange;
import com.trekiz.admin.modules.order.entity.OrderContacts;
import com.trekiz.admin.modules.order.entity.Orderpay;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.entity.Refund;
import com.trekiz.admin.modules.order.entity.TransFerGroup;
import com.trekiz.admin.modules.order.formBean.BorrowingBean;
import com.trekiz.admin.modules.order.repository.OrderContactsDao;
import com.trekiz.admin.modules.order.repository.OrderpayDao;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.service.ApplyOrderCommonService;
import com.trekiz.admin.modules.order.service.CruiseOrderService;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderContactsService;
import com.trekiz.admin.modules.order.service.OrderProgressTrackingService;
import com.trekiz.admin.modules.order.service.OrderReviewService;
import com.trekiz.admin.modules.order.service.OrderServiceForSaveAndModify;
import com.trekiz.admin.modules.order.service.OrderStockService;
import com.trekiz.admin.modules.order.service.RefundService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.order.util.OrderUtil;
import com.trekiz.admin.modules.pay.dao.PayHotelOrderDao;
import com.trekiz.admin.modules.pay.dao.PayIslandOrderDao;
import com.trekiz.admin.modules.pay.entity.PayHotelOrder;
import com.trekiz.admin.modules.pay.entity.PayIslandOrder;
import com.trekiz.admin.modules.receipt.entity.OrderReceipt;
import com.trekiz.admin.modules.receipt.service.OrderReceiptService;
import com.trekiz.admin.modules.reviewflow.entity.Review;
import com.trekiz.admin.modules.reviewflow.entity.ReviewDetail;
import com.trekiz.admin.modules.reviewflow.entity.ReviewLog;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.statistics.utils.ExcelUtils;
import com.trekiz.admin.modules.stock.entity.ActivityGroupReserve;
import com.trekiz.admin.modules.stock.repository.ActivityGroupReserveDao;
import com.trekiz.admin.modules.stock.service.StockService;
import com.trekiz.admin.modules.supplier.entity.SupplierContacts;
import com.trekiz.admin.modules.supplier.entity.SupplierContactsView;
import com.trekiz.admin.modules.supplier.service.SupplierContactsService;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.DocInfo;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.DictService;
import com.trekiz.admin.modules.sys.service.DocInfoService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.MoneyAmountUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.entity.TravelerFile;
import com.trekiz.admin.modules.traveler.entity.TravelerVisa;
import com.trekiz.admin.modules.traveler.entity.TravelerVisaInfo;
import com.trekiz.admin.modules.traveler.repository.TravelerFileDao;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.traveler.service.TravelerVisaService;
import com.trekiz.admin.modules.visa.entity.Visa;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.repository.VisaDao;
import com.trekiz.admin.modules.visa.service.VisaInterviewNoticeService;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.modules.visa.web.DownLoadController;

import freemarker.template.TemplateException;
 
 /**
  * 订单管理：单团、散拼、游学、大客户、自由行
  * @author yakun.bai
  *
  */
@Controller
@RequestMapping(value = "${adminPath}/orderCommon/manage")
public class OrderCommonController extends BaseController {
    
    protected static final Logger logger = LoggerFactory.getLogger(OrderCommonController.class);
    
    @Autowired
    private ActivityGroupReserveDao activityGroupReserveDao;
    @Autowired
    private OrderCommonService orderService;
    @Autowired
    private OrderServiceForSaveAndModify orderServiceForSaveAndModify;
    @Autowired
    private OrderStockService orderStockService;
    @Autowired
    private CruiseOrderService cruiseOrderService;
    @Autowired
    SysIncreaseService sysIncreaseService;
    @Autowired
    StockService StockService;
    @Autowired
	@Qualifier("travelActivitySyncService")
    private ITravelActivityService travelActivityService;
    @Autowired
	@Qualifier("activityGroupSyncService")
    private IActivityGroupService activityGroupService;
    @Autowired
    private AgentinfoService agentinfoService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private com.quauq.review.core.engine.ReviewService reviewServiceNew;
    @Autowired
	private com.quauq.review.core.engine.ReviewService reviewNewService;
    @Autowired
    private VisaProductsService visaProductsService;
    @Autowired
    private DictService dictService;
    @Autowired
    private MoneyAmountService moneyAmountService;
    @Autowired
    private OrderProgressTrackingService progressService;
    @Autowired
    private TravelerService travelerService;
    @Autowired
	private ApplyOrderCommonService applyOrderCommonService;
    @Autowired
    private TravelerVisaService travelerVisaService;
    @Autowired
    private OrderContactsService orderContactsService;
    @Autowired
    private IntermodalStrategyService intermodalStrategyService;
    @Autowired
    private ActivityReserveOrderService activityReserveOrderService;
    @Autowired
	private VisaInterviewNoticeService visaInterviewNoticeService;
    @Autowired
    private DocInfoService docInfoService;
    @Autowired
    private TravelerFileDao travelerFileDao;
    @Autowired
    private OrderpayDao orderpayDao;
    @Autowired
    private VisaOrderService visaOrderService;
    @Autowired
    private CostManageService costManageService;
    @Autowired
    private ProductOrderCommonDao productOrderCommonDao;
    @Autowired
    private VisaDao visaDao;
    @Autowired
    private OrderinvoiceService orderinvoiceService;
    @Autowired
    private OrderReceiptService orderreceiptService;
    @Autowired
    private RefundService refundService;
    @Autowired
    private OrderReviewService orderReviewService;
    @Autowired
	private IslandOrderService islandOrderService;
	@Autowired
	private IslandMoneyAmountService islandMoneyAmountService;
    @Autowired
    private IslandTravelerDao islandTravelerDao;
    @Autowired
    private PayHotelOrderDao payHotelOrderDao;
    @Autowired
    private PayIslandOrderDao payIslandOrderDao;
    @Autowired
    private HotelOrderService hotelOrderService;
    @Autowired
    private HotelMoneyAmountService hotelMoneyAmountService;
    @Autowired
    private SystemService systemService;
    @Autowired
    private HotelTravelerDao hotelTravelerDao;
    @Autowired
    private com.quauq.review.core.engine.ReviewService rs;
    @Autowired
    private ActivityGroupService groupService;
    @Autowired
    private T1PreOrderService preOrderService;
    @Autowired
    private T1MoneyAmountService t1MoneyAmountService;
    @Autowired
    private GroupHandleService groupHandleService;
    @Autowired
    private SupplierContactsService supplierContactsService;
	@Autowired
	private ISelectService selectService;
	@Autowired
	private SupplyContactsService supplyContactsService;
	@Autowired
	private OrderContactsDao orderContactsDao;
	@Autowired
	private LogOrderService logOrderService;
	@Autowired
	private ReturnDifferenceService returnDifferenceService;
	
  /*  @Autowired
    private ActivityGroupDao activityGroupDao;
    */
    private static final Integer REVIEW_UNAUDITED = 1;//审核中状态
    private Model modelTemp;
    
	@ModelAttribute("menuId")
	protected Integer getMenuId(){
		return 80;
	}
	
	/**
	 * 订单列表查询
	 * @param showType
	 * @param orderStatus 订单类型：1 单团；2 散拼；3 游学；4 大客户；5 自由行
	 * @param travelActivity
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 * @throws NumberFormatException
	 * @throws OptimisticLockHandleException
	 * @throws PositionOutOfBoundException
	 */
	@RequestMapping(value ="showOrderList/{showType}/{orderStatus}")
	public String showOrderList(@PathVariable String showType, @PathVariable String orderStatus,
	        @ModelAttribute TravelActivity travelActivity, HttpServletResponse response,
	        Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("bankName",request.getParameter("bank"));
		boolean isEFX = false; //是否是俄风行
		User user = UserUtils.getUser();
		//公司名称
		String companyName = user.getCompany().getName();
		Long companyId = user.getCompany().getId();
		String companyUUID = user.getCompany().getUuid();
		//
		if(StringUtils.isNotBlank(companyName) && companyName.contains("风行")){
			isEFX = true;
		}
		model.addAttribute("isEFX",isEFX);
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		//按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("order", model);
		
		//查询条件
        Map<String,String> mapRequest = Maps.newHashMap();
        
        //参数处理：渠道ID、订单编号或团期编号、联系人、计调名称、团期起始开始时间、团期起始结束时间、订单号、团期号
        String paras = "agentId,orderNumOrGroupCode,orderPersonName,activityCreateName,groupOpenDateBegin,groupOpenDateEnd,orderNum,groupCode," +
        		"payCreateDateBegin,payCreateDateEnd,groupCloseDateBegin,groupCloseDateEnd,orderTimeBegin,orderTimeEnd,payAmountStrat,payAmountEnd," +
        		"accountDateBegin,accountDateEnd,payerName,toBankNname,createDateBegin,createDateEnd";
        OrderCommonUtil.handlePara(paras, mapRequest, model, request);
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
        mapRequest.put("agentId", request.getParameter("agentId"));
        mapRequest.put("saler", request.getParameter("saler"));
        mapRequest.put("creator",creator);
        mapRequest.put("orderNum", request.getParameter("orderNum"));
        mapRequest.put("payType", payType);
        /*增加计调人员查询条件*/
        mapRequest.put("jdUserId", jds);
        /* 增加是否到账的查询条件*/
        mapRequest.put("isAccounted", isAccounted);
        
        if (1 == UserUtils.getUser().getCompany().getIsCancleOrder()) {
        	orderService.cancelOrderWithRemainDays();//调用下架
        }
	        
        if(StringUtils.isNotEmpty(saler)){
        	//选择了销售人员
        	mapRequest.put("saler", saler);
        }
	        
        //排序方式
        if(StringUtils.isBlank(orderBy)){
            orderBy = "updateDate DESC";
            if(Context.SUPPLIER_UUID_CPLY.equals(companyUuid)){
            	//诚品旅游默认按照出团日期排序
            	orderBy = "groupOpenDate DESC";
            }
        }
        
        if(StringUtils.isNotBlank(orderShowType) && 
        		!StringUtils.equalsIgnoreCase("199", showType) && 
        		!StringUtils.equalsIgnoreCase("101", showType)) {
            showType = orderShowType;
        }
	        
	        List<Long> listProId = new ArrayList<Long>();
	        Page<Map<Object, Object>> pageOrder = null;
	        if("reserve".equals(option)) {
	        	//orderS=Context.ORDER_TYPE_JP.toString();
	        	mapRequest.put("orderType", orderS);
	        	pageOrder = activityReserveOrderService.findReserveOrderList(showType, new Page<Map<Object, Object>>(request, response), orderBy, orderShowType, mapRequest, common);
	        }else if("visa".equals(option)) {
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
	        	pageOrder = visaOrderService.findVisaOrderList(showType, new Page<Map<Object, Object>>(request, response), orderBy, orderShowType, mapRequest, common);
	        }else if("pay".equals(option)) {
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
	        	if(isEFX){
	        		//俄风行成本付款查询
	        		pageOrder = costManageService.findCostRecordListForEFX(orderS, new Page<Map<Object, Object>>(request, response), orderBy, mapRequest, common);
	        	}else{
	        	    pageOrder = costManageService.findCostRecordList(orderS, new Page<Map<Object, Object>>(request, response), orderBy, mapRequest, common);
	        	}
	        }
	        else if("visaOrder".equals(option)) {
	        	//押金收款确认查询条件
	        	pageOrder = visaOrderService.findVisaOrderList(showType, new Page<Map<Object, Object>>(request, response), orderBy, orderShowType, mapRequest, common);
	        }
	        else {
	        	if(StringUtils.isBlank(option)) {
	        		pageOrder = orderService.findOrderListByPayType(showType, orderStatus, new Page<Map<Object, Object>>(request, response), travelActivity, orderBy, orderShowType, mapRequest, common);
	        	} else {
	        		/*
	        		 * author:wuqiang
	        		 * 增加按部门查询条件,开始
	        		 */
	        		String queryDepartmentId = request.getParameter("queryDepartmentId");
	        		String printFlag = request.getParameter("printFlag");
	        		String orderPersonId = request.getParameter("orderPersonId");
	        		
	        		mapRequest.put("queryDepartmentId", queryDepartmentId);
	        		mapRequest.put("orderPersonId", orderPersonId);
	        		mapRequest.put("printFlag", printFlag);
	        		model.addAttribute("printFlag", printFlag);
	        		model.addAttribute("orderPersonId", orderPersonId);
	        		model.addAttribute("orderPersonList", UserUtils.getSalers(UserUtils.getUser().getCompany().getId()));
	        		//增加按部门查询条件,结束
	        		//20151026增加页面参数option,付款状态,达帐状态,分页参数,签证国家
	        		mapRequest.put("option", option);
	        		mapRequest.put("payStatus", request.getParameter("payStatus"));
	        		mapRequest.put("accountStatus", request.getParameter("accountStatus"));
	        		mapRequest.put("pageNo", request.getParameter("pageNo"));
	        		mapRequest.put("pageSize", request.getParameter("pageSize"));
	        		String sysCountryId = request.getParameter("sysCountryId");
			         sysCountryId = " ".equals(sysCountryId)?null:sysCountryId;
	        		mapRequest.put("sysCountryId", sysCountryId);
	        		model.addAttribute("payStatus", request.getParameter("payStatus"));
	        		model.addAttribute("accountStatus", request.getParameter("accountStatus"));
	        		model.addAttribute("sysCountryId", sysCountryId);
	        		//签证国家
	        		List<Object[]> countryInfoList = visaProductsService.findCountryInfoList();
	        		model.addAttribute("countryInfoList", countryInfoList);
	        		//出团日期
	        		mapRequest.put("groupOpenDateStart", request.getParameter("groupOpenDateStart"));
	        		//出团日期
	        		mapRequest.put("groupOpenDateEnd", request.getParameter("groupOpenDateEnd"));
	        		model.addAttribute("groupOpenDateStart", request.getParameter("groupOpenDateStart"));
	        		model.addAttribute("groupOpenDateEnd", request.getParameter("groupOpenDateEnd"));
	        		pageOrder = orderService.findOrderListByPayType(showType, orderS, new Page<Map<Object, Object>>(request, response), 
	        				travelActivity, orderBy, orderShowType, mapRequest, common);
	        	}
	        }
	        model.addAttribute("page", pageOrder);
	        List<Map<Object, Object>> listorder = pageOrder.getList();
	        orderBy = pageOrder.getOrderBy().replace("agp", "pro");
	        pageOrder.setOrderBy(orderBy);
	        if(listorder != null && listorder.size() > 0) {
		        for(Map<Object, Object> listin : listorder) {
		        	if(listin.get("createBy") != null) {
		        		listin.put("carateUserName", UserUtils.getUser(StringUtils.toLong(listin.get("createBy"))).getName());
		        	}
		        	if(listin.get("id") != null) {
		        		listProId.add(Long.parseLong(listin.get("id").toString()));
		        	}
		            
		        	//金额处理
		        	handlePrice(listin);
		        	
		        	//获取订单剩余时间
		        	getOrderLeftTime(listin);
	
		            //转团转款标志位 1-可以转款  0-不可转款
		            listin.put("transferMoneyCheck", "0");
		            if(!"reserve".equals(option)&&!"visa".equals(option)&&!"pay".equals(option)) {
		            	ProductOrderCommon order = productOrderCommonDao.findOne(Long.parseLong(String.valueOf(listin.get("id"))));
		            	if(order != null) {
			            	List<Review> list = reviewService.findReview(order.getOrderStatus(),Context.REVIEW_FLOWTYPE_TRANSFER_GROUP,String.valueOf(listin.get("id")),Context.REVIEW_STATUS_DONE,Context.REVIEW_ACTIVE_EFFECTIVE);
			            	if(list!=null&&list.size()>0){
			            		listin.put("transferMoneyCheck", "1");
			            	}
		            	}
		            }
		           
		        }
	        }
	        List<Map<Object, Object>> orderList = null;
	        
	        //支付订单查询
	        selectPayOrder(listProId, listorder, orderStatus, model);
	        
	        String userType = UserUtils.getUser().getUserType();

	        //判断是否是越柬行踪
	        if(Context.SUPPLIER_UUID_YJXZ.equals(user.getCompany().getUuid())){
	        	model.addAttribute("isYJXZ", true);
	        }else{
	        	model.addAttribute("isYJXZ", false);
	        }
	        model.addAttribute("supllierUuid", user.getCompany().getUuid());
			model.addAttribute("confirmPay",user.getCompany().getConfirmPay());

	        model.addAttribute("payType",payType);

	        model.addAttribute("userType", userType);
	        model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
			model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
			model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
	        model.addAttribute("trafficModes", DictUtils.getDicMap(Context.TRAFFIC_MODE));
	        model.addAttribute("trafficNames", DictUtils.getValueAndLabelMap("traffic_name",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
	        model.addAttribute("payTypes", DictUtils.getDicMap(Context.PAY_TYPE));
	        model.addAttribute("showType",showType); 
	        model.addAttribute("orderStatus",orderStatus); 
	        model.addAttribute("orderShowType",orderShowType);
	        model.addAttribute("orders",orderList);
	        model.addAttribute("orderS", orderS); 
	        model.addAttribute("saler", saler);
	        model.addAttribute("creator", creator);
	        model.addAttribute("jds", jds); //计调人员
	        model.addAttribute("isAccounted", isAccounted); //是否到账
	        model.addAttribute("companyId", companyId);
	        model.addAttribute("companyUUID", companyUUID);
	        String genre = "";
	        if("detail".equals(option)) {
	        	genre = "1,2,3,4,5";
	        }else{
	        	genre = "1,2,3,4";
	        }
	        
	        if (StringUtils.isNotBlank(option)) {
	        	List<String[]> orderSum = orderService.findSum1(showType, orderS, genre, travelActivity, mapRequest, common);
		        if(orderSum != null && orderSum.size() != 0) {
		        	if(orderSum.get(0) != null) {
		        		model.addAttribute("sumTotalMoney",orderSum.get(0)[0]);
			        	model.addAttribute("sumPayedMoney",orderSum.get(0)[1]);
			        	model.addAttribute("sumPerson",orderSum.get(0)[2]);
			        	model.addAttribute("sumNotPayedMoney",orderSum.get(0)[3]);
		        	}
		        }
	        }
	        
	        model.addAttribute("orderTypeStr", OrderCommonUtil.getStringOrdeType(orderStatus));
	        //渠道
	        List<Agentinfo> list = agentinfoService.findAllAgentinfo();
	        int index = -1;
	        for (Agentinfo agentInfo:list) {
				String agentName = agentInfo.getAgentName();
				if("非签约渠道".equals(agentName)){
					index++;
					if(Context.SUPPLIER_UUID_DYGL.equals(companyUuid)){
						agentInfo.setAgentName("未签");
			        }
				}
			}
	        if(-1 == index){
	        	Agentinfo agentInfo = new Agentinfo();
	        	agentInfo.setId(-1L);
	        	agentInfo.setAgentName("非签约渠道");
	        	if(Context.SUPPLIER_UUID_DYGL.equals(companyUuid)){
					agentInfo.setAgentName("未签");
		        }
	        	list.add(agentInfo);
	        }
	        model.addAttribute("agentinfoList", list);
	        //部门
	        Set<Department> departmentSet = UserUtils.getUserDepartment();
			model.addAttribute("departmentSet", departmentSet);
	        //订单类型
			
			model.addAttribute("orderTypes", DictUtils.getDictList("order_type"));
			//内部销售人员的名单
			model.addAttribute("agentSalers", agentinfoService.findInnerSales(UserUtils.getUser().getCompany().getId()));
			// 下单人
			List<Map<String, Object>> creatorList = UserUtils.getSalers(UserUtils.getUser().getCompany().getId());
			model.addAttribute("creatorList", creatorList);
			// 销售
			Map<String,String> salerList = agentinfoService.findInnerSales(UserUtils.getUser().getCompany().getId());
			model.addAttribute("salerList", salerList);
			/*获取批发商下所有拥有计调角色人员和发布产品人员*/
			model.addAttribute("agentJd", UserUtils.getOperators(UserUtils.getUser().getCompany().getId()));

        //日信观光UUID
        model.addAttribute("RXGG", Context.SUPPLIER_UUID_RXGG);

	    //跳转链接
        if(Context.ORDER_PAYSTATUS_FINANCE.equalsIgnoreCase(showType)) {
        	if("pay".equals(option)) {
        		//环球行批发商
        		model.addAttribute("supplierList", UserUtils.getSupplierInfoList("supplierName", ""));
        		//币种列表
        		model.addAttribute("currencyList", currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId()));
        		model.addAttribute("supplierId", request.getParameter("supplierInfo"));
        		model.addAttribute("pay", request.getParameter("pay"));
        		model.addAttribute("currencyId", request.getParameter("currency"));
        		model.addAttribute("startMoney", request.getParameter("startMoney"));
        		model.addAttribute("endMoney", request.getParameter("endMoney"));
        		model.addAttribute("review_cost", DictUtils.getDictList("review_cost"));
        		// 得到公司ID  奢华之旅：75895555346a4db9a96ba9237eae96a5
//    			company_Id = "75895555346a4db9a96ba9237eae96a5";
    			model.addAttribute("companyUUID", companyUUID);
        		return "modules/order/orderListForPay";
        	}
        	else if("reserve".equals(option)) {
        		return "modules/order/orderListForReserve";
        	}else if("visa".equals(option)) {
        		return "modules/order/orderListForVisa";
        	}
        	else if("visaOrder".equals(option)) {
        		return "modules/order/orderListForDzOrderPay";
        	}
        	else {
        		
        		/*
        		 * author:wuqiang
        		 * 当前用户所属批发商下的所有部门
        		 * */
        		Long compangId = user.getCompany().getId();
        		List<Department> departmentList = departmentService.findByOfficeId(compangId);
        		model.addAttribute("departmentList",departmentList);
        		model.addAttribute("queryDepartmentId", request.getParameter("queryDepartmentId"));
        		
	            model.addAttribute("menuId",90);
	            return "modules/order/orderListForFinance";
        	}
        } else if(Context.ORDER_PAYSTATUS_SYNC_CHECK.equalsIgnoreCase(showType)) {
            model.addAttribute("menuId",90);
            return "modules/order/orderListForDz";
        }
        
        return "modules/order/list/orderList";
	}	
	
	/**
	 * 签证押金收款导出excel
	 * @author zhaohaiming
	 * */
	@RequestMapping(value="exportOrderListForVisa")
	public void exportOrderListForVisa(HttpServletRequest request,HttpServletResponse response,Model model){
		String orderBy = request.getParameter("orderBy");//排序
		String param = "groupCode,orderNum,accountDateBegin,accountDateEnd,createDateBegin,createDateEnd,creator,agentId,saler,jd,isAccounted,payerName,toBankNname,printFlag,payType";
		
		//处理查询条件并赋值
		Map<String,String> mapRequest = new HashMap<String,String>();
		OrderCommonUtil.handlePara(param, mapRequest, model, request);
		mapRequest.put("receiptConfirmationDateBegin", request.getParameter("receiptConfirmationDateBegin"));
	    mapRequest.put("receiptConfirmationDateEnd", request.getParameter("receiptConfirmationDateEnd"));
				
		List<Map<Object,Object>> list = visaOrderService.findVisaOrderList(orderBy, mapRequest);
		Workbook workbook = ExcelUtils.exportOrderListForVisaList(list);
		ServletUtil.downLoadExcel(response, "签证押金收款.xls", workbook);
	}
	/**
	 * 切位订单收款导出excel
	 * @author zhaohaiming
	 * */
	@RequestMapping(value="exportReserveOrderList")
	public void exportReserveOrderList(HttpServletRequest request,HttpServletResponse response,Model model){
		
		String orderBy = request.getParameter("orderBy");//排序
		//查询条件的name值
		String param ="groupCode,orderS,orderNum,accountDateBegin,accountDateEnd,isAccounted,agentId,saler,createDateBegin,createDateEnd,jd," +
				"printFlag,payerName,payAmountStrat,payAmountEnd,toBankNname,creator,payType,receiptConfirmDateBegin,receiptConfirmDateEnd,groupOpenDateBegin,groupOpenDateEnd";
		//处理查询条件并赋值
		Map<String,String> mapRequest = new HashMap<String,String>();
		OrderCommonUtil.handlePara(param, mapRequest, model, request);
		mapRequest.put("jdUserId", request.getParameter("jd"));
		mapRequest.put("exportExcel", "exportExcel");
		Page<Map<Object, Object>> pageOrder = activityReserveOrderService.findReserveOrderList(null,
				new Page<Map<Object, Object>>(request, response), orderBy, null, mapRequest, null);
		//生成workbook对象
		Workbook workbook = ExcelUtils.exportReserveOrderList(pageOrder.getList());
		//下载导出
		ServletUtil.downLoadExcel(response, "切位订单收款.xls", workbook);
	}
	/**
	 * 订单收款导出excel
	 * @author zhaohaiming
	 * */
	@RequestMapping(value="downLoadOrderListForDZ")
	public void downLoadOrderListForDZ(HttpServletRequest request, HttpServletResponse response,Model model){
		try{
			String orderBy = request.getParameter("orderBy");//排序
			//订单类型s
            String orderS = StringUtils.isBlank(request.getParameter("orderS"))?String.valueOf(Context.ORDER_TYPE_ALL) : request.getParameter("orderS");
            //处理查询条件参数
			String param = "groupOpenDateBegin,groupOpenDateEnd,groupCode,orderS,orderNum,accountDateBegin,accountDateEnd,agentId,saler,jd,isAccounted,payAmountStrat,payAmountEnd,printFlag,payerName,toBankNname,createDateBegin,createDateEnd,payType,creator";
			Map<String,String> mapRequest = new HashMap<String,String>();
			OrderCommonUtil.handlePara(param, mapRequest, model, request);
			mapRequest.put("receiptConfirmationDateBegin", request.getParameter("receiptConfirmationDateBegin"));
			mapRequest.put("receiptConfirmationDateEnd", request.getParameter("receiptConfirmationDateEnd"));
			model.addAttribute("receiptConfirmationDateBegin",request.getParameter("receiptConfirmationDateBegin"));
			model.addAttribute("receiptConfirmationDateEnd",request.getParameter("receiptConfirmationDateEnd"));
			//查询数据
			List<Map<Object,Object>> list = orderService.getByPayStatusAndNoatAccount(orderBy,orderS, mapRequest);
			//生成excel
			Workbook workbook = ExcelUtils.genOrderListForDZExel(list);
			ServletUtil.downLoadExcel(response, "订单收款.xls", workbook);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * 交易明细-导出Excel
	 * 405需求  并没有在导出Excel的列表中增加“收款确认日期”数据列
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ParseException 
	 */
	@RequestMapping(value="exportExcel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException {
		
		String groupCode = request.getParameter("groupCode");
		String orderS = StringUtils.isBlank(request.getParameter("orderS")) ? String.valueOf(Context.ORDER_TYPE_ALL) : request.getParameter("orderS");
		String orderTimeBegin = request.getParameter("orderTimeBegin");
		String orderTimeEnd = request.getParameter("orderTimeEnd");
		String orderPersonId = request.getParameter("orderPersonId");
		String agentId = request.getParameter("agentId");
		String saler = request.getParameter("saler");
		String queryDepartmentId = request.getParameter("queryDepartmentId");
		String jd = request.getParameter("jd");
		String payStatus = request.getParameter("payStatus");
		String accountStatus = request.getParameter("accountStatus");
		String sysCountryId = request.getParameter("sysCountryId");
		String groupOpenDateBegin = request.getParameter("groupOpenDateBegin");
		String groupOpenDateEnd = request.getParameter("groupOpenDateEnd");
		//添加是否占位确认筛选条件
		String isSeizedConfirmed = request.getParameter("isSeizedConfirmed");
		
		Map<String,String> mapRequest = Maps.newHashMap();
		mapRequest.put("isSeizedConfirmed", isSeizedConfirmed);
		mapRequest.put("groupCode", groupCode);
		mapRequest.put("orderS", orderS);
		mapRequest.put("orderTimeBegin", orderTimeBegin);
		mapRequest.put("orderTimeEnd", orderTimeEnd);
		mapRequest.put("orderPersonId", orderPersonId);
		mapRequest.put("agentId", agentId);
		mapRequest.put("option", "detail");
		mapRequest.put("saler", saler);
		mapRequest.put("queryDepartmentId", queryDepartmentId);
		mapRequest.put("jd", jd);
		mapRequest.put("payStatus", "0".equals(payStatus) ? null : payStatus);
		mapRequest.put("accountStatus", "0".equals(accountStatus) ? null : accountStatus);
		mapRequest.put("sysCountryId", sysCountryId);
		mapRequest.put("groupOpenDateBegin", groupOpenDateBegin);
		mapRequest.put("groupOpenDateEnd", groupOpenDateEnd);
		
		String showType = "199";
		String orderBy = request.getParameter("orderBy");
		if(StringUtils.isBlank(orderBy)){
			orderBy = "updateDate DESC";
		}
		DepartmentCommon common = departmentService.setDepartmentPara("order", modelTemp);
		Page<Map<Object, Object>> page = new Page<Map<Object, Object>>(request, response);
		page.setMaxSize(Integer.MAX_VALUE);//0069 需求 导出EXCEL 不限制条数  by chy 2016年1月7日13:36:46
		Page<Map<Object, Object>> pageOrder = orderService.findOrderListByPayType(showType, orderS, page, null, orderBy, null, mapRequest, common);
		
		String title = "交易明细";
		String[] headers = null;
		//0307交易明细 修改Excel模板  王洋  
		if(Context.SUPPLIER_UUID_QHJQ.equals(UserUtils.getUser().getCompany().getUuid())){
			if(orderS != null && orderS.equals("6")) {
				headers = new String[]{"序号", "订单号", "团号", "产品名称", "团队类型", "签证国家", "计调", "渠道", "销售", "预订时间", "人数", "出团日期", "截团日期", "订单状态", "订单总额", "已收金额","到账金额"};
			}else {
				headers = new String[]{"序号", "订单号", "团号", "产品名称", "团队类型", "计调", "渠道", "销售", "预订时间", "人数", "出团日期", "截团日期", "订单状态", "订单总额", "已收金额","到账金额"};
			}			
		}else {
			if(orderS != null && orderS.equals("6")) {
				headers = new String[]{"序号", "订单号", "团号/产品名称", "团队类型", "签证国家", "计调", "渠道", "销售/下单人", "预订时间", "人数", "出团日期","截团日期", "订单状态", "订单总额", "已收金额","到账金额"};
			}else{
				headers = new String[]{"序号", "订单号", "团号/产品名称", "团队类型", "计调", "渠道", "销售/下单人", "预订时间", "人数", "出团日期", "截团日期","订单状态", "订单总额", "已收金额","到账金额"};
			}
		}
		
		
		String fileName = "交易明细.xls";
		response.setContentType("octets/stream");
		response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("gb2312"), "ISO-8859-1"));
		
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth(20);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		
		//虚线
		HSSFCellStyle styleDot = workbook.createCellStyle();
		styleDot.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleDot.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		styleDot.setBorderBottom(HSSFCellStyle.BORDER_DOTTED);
				
		//订单总额
		HSSFCellStyle styleBold = workbook.createCellStyle();
		styleBold.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		styleBold.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFFont font1 = (HSSFFont) workbook.createFont();
		font1.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFDataFormat format = workbook.createDataFormat();
		styleBold.setDataFormat(format.getFormat("#,##0.00"));
		styleBold.setFont(font1);
		
		//已付金额
		HSSFCellStyle stylePay = workbook.createCellStyle();
		stylePay.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		stylePay.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFFont font2 = (HSSFFont) workbook.createFont();
		font2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font2.setColor(HSSFColor.RED.index);
		stylePay.setDataFormat(format.getFormat("#,##0.00"));
		stylePay.setFont(font2);
		
		//到账金额
		HSSFCellStyle styleAccount = workbook.createCellStyle();
		styleAccount.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		styleAccount.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		HSSFFont font3 = (HSSFFont) workbook.createFont();
		font3.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font3.setColor(HSSFColor.GREEN.index);
		styleAccount.setDataFormat(format.getFormat("#,##0.00"));
		styleAccount.setFont(font3);

		//要导出的excel数据
		List<Map<Object, Object>> list = pageOrder.getList();
		//付款金额
		int totalCount = 2;
		Set<String> totalSet = new HashSet<String>();
		List<String> totalList = null;
		//已付金额
		int payCount = 2;
		Set<String> paySet = new HashSet<String>();
		List<String> payList = null;
		//到账金额
		int recCount = 2;
		Set<String> recSet = new HashSet<String>();
		List<String> recList = null;
		for(Map<Object, Object> ite:list){
			Integer oType = null;
			if(ite.get("orderType") != null) {
				oType = Integer.parseInt(ite.get("orderType").toString());
			}
			String payedMoney = null;
			String payed = null;
			String accountedMoney = null;
			String accounted = null;
			String totalMoney = null;
			String total = null;
			if(ite.get("totalMoney") != null) {
				totalMoney = ite.get("totalMoney").toString();
				total = OrderCommonUtil.getMoneyAmountByUUIDOrderType(totalMoney, oType, 13, 2);
			}
			if(ite.get("payedMoney") != null){
				payedMoney = ite.get("payedMoney").toString();
				payed = OrderCommonUtil.getMoneyAmountByUUIDOrderType(payedMoney, oType, 5, 2);
			}
			if(ite.get("accountedMoney") != null){
				accountedMoney = ite.get("accountedMoney").toString();
				accounted = OrderCommonUtil.getMoneyAmountByUUIDOrderType(accountedMoney, oType, 4, 2);
			}
			if(total != null){
				total = total.replaceAll("(-?\\d+)(\\.\\d+)?", "");
				total = total.replaceAll(",", "").replaceAll(" ", "");
				totalSet.addAll(Arrays.asList(total.split("\\+")));
			}
			if(payed != null){
				payed = payed.replaceAll("(-?\\d+)(\\.\\d+)?", "");
				payed = payed.replaceAll(",", "").replaceAll(" ", "");
				paySet.addAll(Arrays.asList(payed.split("\\+")));
			}
			if(accounted != null){
				accounted = accounted.replaceAll("(-?\\d+)(\\.\\d+)?", "");
				accounted = accounted.replaceAll(",", "").replaceAll(" ", "");
				recSet.addAll(Arrays.asList(accounted.split("\\+")));
			}
		}
		
		if(totalSet.size() > 0){
			totalCount = totalSet.size()*2;
			totalList = new ArrayList<String>(totalSet);
			Collections.sort(totalList);
		}
		if(paySet.size() > 0){
			payCount = paySet.size()*2;
			payList = new ArrayList<String>(paySet);
			Collections.sort(payList);
		}
		if(recSet.size() > 0){
			recCount = recSet.size()*2;
			recList = new ArrayList<String>(recSet);
			Collections.sort(recList);
		}
		
		// 产生表格标题行
		int columnIndex = 0;
		HSSFRow row = sheet.createRow(0);
		int headersLength = headers.length;
		if (headersLength == 15) {
			for (int i = 0; i < totalCount + payCount + recCount; ++i) {
				sheet.setColumnWidth(12 + i * 2, 5 * 256);
			}
		} else if(headersLength == 16){
			for (int i = 0; i < totalCount + payCount + recCount; ++i) {
				sheet.setColumnWidth(13 + i * 2, 5 * 256);
			}
		}else {
			for (int i = 0; i < totalCount + payCount + recCount; ++i) {
				sheet.setColumnWidth(14 + i * 2, 5 * 256);
			}
		}
		for (int i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(columnIndex);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
			if ("订单总额".equals(headers[i])) {
				sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex,
						columnIndex + totalCount - 1));
				columnIndex = columnIndex + totalCount;
			} else if ("已收金额".equals(headers[i])) {
				sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex,
						columnIndex + payCount - 1));
				columnIndex = columnIndex + payCount;
			} else if ("到账金额".equals(headers[i])) {
				sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex,
						columnIndex + recCount - 1));
				columnIndex = columnIndex + recCount;
			} else {
				sheet.addMergedRegion(new CellRangeAddress(0, 0, columnIndex,
						columnIndex));
				columnIndex = columnIndex + 1;
			}
		}
		//数据填充
		for (int i = 0; i < list.size(); i++) {
			columnIndex = 0;
			row = sheet.createRow(i + 1);

			HSSFCell cell = row.createCell(columnIndex);
			cell.setCellValue(i + 1);// 序号
			cell.setCellStyle(style);
			columnIndex = columnIndex + 1;

			cell = row.createCell(columnIndex);
			if (list.get(i).get("orderNum") != null) {
				cell.setCellValue(list.get(i).get("orderNum").toString());// 订单号
			}
			cell.setCellStyle(style);
			columnIndex = columnIndex + 1;
			
			//针对起航假期
			if(Context.SUPPLIER_UUID_QHJQ.equals(UserUtils.getUser().getCompany().getUuid())) {
				//团号
				cell = row.createCell(columnIndex);
				if (list.get(i).get("groupCode") != null) {
					cell.setCellValue(list.get(i).get("groupCode").toString());
				}
				cell.setCellStyle(style);
				columnIndex = columnIndex + 1;
				//产品名称
				cell = row.createCell(columnIndex);
				if (list.get(i).get("acitivityName") != null) {
					cell.setCellValue(list.get(i).get("acitivityName").toString());
				}
				cell.setCellStyle(style);
				columnIndex = columnIndex + 1;
			} else {  //其他用户
				cell = row.createCell(columnIndex);
				String groupCode_activityName = "";
				if (list.get(i).get("groupCode") != null) {
					groupCode_activityName = list.get(i).get("groupCode").toString();
				}
				if(list.get(i).get("acitivityName") != null) {
					groupCode_activityName += ("/" + list.get(i).get("acitivityName").toString());					
				}
				cell.setCellValue(groupCode_activityName);// 团号/产品名称
				cell.setCellStyle(style);
				columnIndex = columnIndex + 1;
			}
			
			// 团队类型
			cell = row.createCell(columnIndex);
			if (list.get(i).get("orderType") != null) {
				String orderType = DictUtils.getDictLabel(list.get(i).get("orderType").toString(),"order_type", "");
				cell.setCellValue(orderType);
			}
			cell.setCellStyle(style);
			columnIndex = columnIndex + 1;
			
			//签证国家
			if(orderS != null && orderS.equals("6")){
				cell = row.createCell(columnIndex);
				if (list.get(i).get("sysCountryId") != null) {// 签证国家
					String countryName = CountryUtils.getCountryName(Long.parseLong(list.get(i).get("sysCountryId").toString()));
					cell.setCellValue(countryName);
				}
				cell.setCellStyle(style);
				columnIndex = columnIndex + 1;
			}
			
			//计调
			cell = row.createCell(columnIndex);
			if (list.get(i).get("w_operator") != null) {
				User u = UserUtils.getUser(list.get(i).get("w_operator").toString());
				cell.setCellValue(u.getName());// 计调
			}
			cell.setCellStyle(style);
			columnIndex = columnIndex + 1;
			
			//渠道
			Integer orderType = null;
			if (list.get(i).get("orderType") != null) {
				orderType = Integer.parseInt(list.get(i).get("orderType").toString());
			}
			if(Context.SUPPLIER_UUID_QHJQ.equals(UserUtils.getUser().getCompany().getUuid())) {
				 if(orderType<6 || orderType==10){
					cell = row.createCell(columnIndex);
					if (list.get(i).get("orderCompanyName") != null) {
						cell.setCellValue(list.get(i).get("orderCompanyName").toString());// 渠道
					}
					cell.setCellStyle(style);
					columnIndex = columnIndex + 1;
				}else{
					cell = row.createCell(columnIndex);
					if (list.get(i).get("w_agent") != null) {
						String agentName = AgentInfoUtils.getAgentName(Long.parseLong(list.get(i).get("w_agent").toString()));
						cell.setCellValue(agentName);
					}
					cell.setCellStyle(style);
					columnIndex = columnIndex + 1;
				}
			}else{
				if(orderS.equals("6") || orderType != null && orderType == 7) {
					cell = row.createCell(columnIndex);
					if (list.get(i).get("w_agent") != null) {
						String agentName = AgentInfoUtils.getAgentName(Long.parseLong(list.get(i).get("w_agent").toString()));
						cell.setCellValue(agentName);
					}
					cell.setCellStyle(style);
					columnIndex = columnIndex + 1;
				}else{
					cell = row.createCell(columnIndex);
					if (list.get(i).get("orderCompanyName") != null) {
						cell.setCellValue(list.get(i).get("orderCompanyName").toString());// 渠道
					}
					cell.setCellStyle(style);
					columnIndex = columnIndex + 1;
				}
			}
						
			//销售、下单人
			if(Context.SUPPLIER_UUID_QHJQ.equals(UserUtils.getUser().getCompany().getUuid()))  {  //针对起航假期：销售
				cell = row.createCell(columnIndex);
				if (list.get(i).get("w_saler") != null) {
					User u1 = UserUtils.getUser(list.get(i).get("w_saler").toString());
					cell.setCellValue(u1.getName());// 销售
				}
				cell.setCellStyle(style);
				columnIndex = columnIndex + 1;
			}else {
				cell = row.createCell(columnIndex);
				String saler_creater = "";
				if (list.get(i).get("w_saler") != null) {
					User u1 = UserUtils.getUser(list.get(i).get("w_saler").toString());
					if(u1 != null) {
						saler_creater = u1.getName();
					}
				}
				if(list.get(i).get("createUserId") != null) {
					User u2 = UserUtils.getUser(list.get(i).get("createUserId").toString());
					if(u2 != null) {
						saler_creater += ("/" + u2.getName());
					}
				}
				cell.setCellValue(saler_creater);// 销售/下单人
				cell.setCellStyle(style);
				columnIndex = columnIndex + 1;
			}
			
			// 预订时间
			cell = row.createCell(columnIndex);
			if (list.get(i).get("orderTime") != null) {
				String orderTime = list.get(i).get("orderTime").toString();
				cell.setCellValue(orderTime.substring(0, orderTime.length() - 2));
			}
			cell.setCellStyle(style);
			columnIndex = columnIndex + 1;	
			
			// 人数
			cell = row.createCell(columnIndex);
			if (list.get(i).get("orderPersonNum") != null) {
				cell.setCellValue(list.get(i).get("orderPersonNum").toString());
			}
			cell.setCellStyle(style);
			columnIndex = columnIndex + 1;

			// 出团日期
			cell = row.createCell(columnIndex);
			if (list.get(i).get("groupOpenDate") != null) {
				cell.setCellValue(list.get(i).get("groupOpenDate").toString());
			}
			cell.setCellStyle(style);
			columnIndex = columnIndex + 1;

			// 截团日期
			cell = row.createCell(columnIndex);
			if (list.get(i).get("groupCloseDate") != null) {
				cell.setCellValue(list.get(i).get("groupCloseDate").toString());
			}
			cell.setCellStyle(style);
			columnIndex = columnIndex + 1;

			// 订单状态
			cell = row.createCell(columnIndex);
			String name = DictUtils.getDictLabel(list.get(i).get("payStatus").toString(),"order_pay_status", "");
			cell.setCellValue(name);
			cell.setCellStyle(style);
			columnIndex = columnIndex + 1;

			// 订单总额
			if (list.get(i).get("totalMoney") != null) {
				String totalMoney = list.get(i).get("totalMoney").toString();
				String total = OrderCommonUtil.getMoneyAmountByUUIDOrderType(totalMoney,orderType, 13, 2);
				//538 需求 减去差额
				ProductOrderCommon orderCommon = orderService.getProductorderById(Long.parseLong(list.get(i).get("id").toString()));
				String currencyM = "";
				String currencyA = "" ;
				if(orderCommon != null && orderCommon.getDifferenceFlag() == 1 && StringUtils.isNotBlank(orderCommon.getDifferenceMoney())){
					total = total.replaceAll(",","" );
					for(int j = 0 ; j < total.length() ; j ++){
						 if (Character.isDigit(total.charAt(j))){
							    currencyA = total.substring(j);
							    currencyM = total.substring(0, j);
							    break;
						 }
					}
					MoneyAmount moneyAmount = MoneyAmountUtils.getMoneyAmountByUUID(orderCommon.getDifferenceMoney());
					BigDecimal bigDecimal = new BigDecimal(currencyA).subtract(moneyAmount.getAmount());
					DecimalFormat d= new DecimalFormat(",##0.00");
					total = currencyM + d.format(bigDecimal);
				}
				// 将数据分成数组
				List<Object[]> totals = MoneyNumberFormat.getMoneyFromString(total, "\\+");				
				for (String str : totalList) {
					boolean hasValue = false;
					Double initValue = null;
					if(CollectionUtils.isNotEmpty(totals)) {
						for (Object[] objects : totals) {
							if (str.equals(objects[0].toString())) {
								hasValue = true;
								initValue = Double.valueOf(objects[1].toString());
							}
						}
					}
					cell = row.createCell(columnIndex);
					cell.setCellValue(str);
					cell.setCellStyle(styleBold);
					columnIndex = columnIndex + 1;
					if (hasValue) {
						cell = row.createCell(columnIndex);
						cell.setCellValue(initValue);
						cell.setCellStyle(styleBold);
						columnIndex = columnIndex + 1;
					} else {
						cell = row.createCell(columnIndex);
						cell.setCellValue("");
						cell.setCellStyle(styleBold);
						columnIndex = columnIndex + 1;
					}
				}
			} else {
				for (String str : totalList) {
					cell = row.createCell(columnIndex);
					cell.setCellValue(str);
					cell.setCellStyle(styleBold);
					columnIndex = columnIndex + 1;
					cell = row.createCell(columnIndex);
					cell.setCellValue("");
					cell.setCellStyle(styleBold);
					columnIndex = columnIndex + 1;
				}
			}

			// 付款金额
			if (list.get(i).get("payedMoney") != null) {
				String payedMoney = list.get(i).get("payedMoney").toString();
				String payed = OrderCommonUtil.getMoneyAmountByUUIDOrderType(payedMoney,orderType, 5, 2);
				List<Object[]> pays = MoneyNumberFormat.getMoneyFromString(payed, "\\+");
				for (String str : payList) {
					boolean hasValue = false;
					Double initValue = null;
					if(CollectionUtils.isNotEmpty(pays)){
						for (Object[] objects : pays) {
							if (str.equals(objects[0].toString())) {
								hasValue = true;
								initValue = Double.valueOf(objects[1].toString());
							}
						}
					}
					cell = row.createCell(columnIndex);
					cell.setCellValue(str);
					cell.setCellStyle(stylePay);
					columnIndex = columnIndex + 1;
					if (hasValue) {
						cell = row.createCell(columnIndex);
						cell.setCellValue(initValue);
						cell.setCellStyle(stylePay);
						columnIndex = columnIndex + 1;
					} else {
						cell = row.createCell(columnIndex);
						cell.setCellValue("");
						cell.setCellStyle(stylePay);
						columnIndex = columnIndex + 1;
					}
				}
			} else {
				for (String str : payList) {
					cell = row.createCell(columnIndex);
					cell.setCellValue(str);
					cell.setCellStyle(stylePay);
					columnIndex = columnIndex + 1;
					cell = row.createCell(columnIndex);
					cell.setCellValue("");
					cell.setCellStyle(stylePay);
					columnIndex = columnIndex + 1;
				}
			}

			// 到账金额
			cell = row.createCell(columnIndex);
			if (list.get(i).get("accountedMoney") != null) {
				String accountedMoney = list.get(i).get("accountedMoney").toString();
				String accounted = OrderCommonUtil.getMoneyAmountByUUIDOrderType(accountedMoney,orderType, 4, 2);
				List<Object[]> accounts = MoneyNumberFormat.getMoneyFromString(accounted, "\\+");
				for (String str : recList) {
					boolean hasValue = false;
					Double initValue = null;
					if(CollectionUtils.isNotEmpty(accounts)) {
						for (Object[] objects : accounts) {
							if (str.equals(objects[0].toString())) {
								hasValue = true;
								initValue = Double.valueOf(objects[1].toString());
							}
						}
					}
					cell = row.createCell(columnIndex);
					cell.setCellValue(str);
					cell.setCellStyle(styleAccount);
					columnIndex = columnIndex + 1;
					if (hasValue) {
						cell = row.createCell(columnIndex);
						cell.setCellValue(initValue);
						cell.setCellStyle(styleAccount);
						columnIndex = columnIndex + 1;
					} else {
						cell = row.createCell(columnIndex);
						cell.setCellValue("");
						cell.setCellStyle(styleAccount);
						columnIndex = columnIndex + 1;
					}
				}
			} else {
				for (String str : recList) {
					cell = row.createCell(columnIndex);
					cell.setCellValue(str);// 订单总额
					cell.setCellStyle(styleAccount);
					columnIndex = columnIndex + 1;
					cell = row.createCell(columnIndex);
					cell.setCellValue("");
					cell.setCellStyle(styleAccount);
					columnIndex = columnIndex + 1;
				}
			}
		}
		OutputStream os = response.getOutputStream();
		workbook.write(os);
		os.close();
	}
	
	/**
	 * 财务收款-其他收入收款列表页
	 * */
	@RequestMapping(value ="showOrderListForOther")
	public String showOrderListForOther(HttpServletRequest request,HttpServletResponse response, Model model) throws Exception {
        String groupCode= request.getParameter("groupCode");
		String orderType = request.getParameter("orderS");
		String startCreateDate = request.getParameter("startCreateDate");
		String endCreateDate = request.getParameter("endCreateDate");
		String agentId = request.getParameter("agentId");
		String supplierInfo = request.getParameter("supplierInfo");
		String jd = request.getParameter("jd");
		String currency = request.getParameter("currency");
		String startMoney = request.getParameter("startMoney");
		String endMoney = request.getParameter("endMoney");
		String isAsAccount = request.getParameter("isAsAccount");
		String printFlag = request.getParameter("printFlag");
		String payerName = request.getParameter("payerName");
		String toBankNname = request.getParameter("toBankNname");
		String accountDateBegin = request.getParameter("accountDateBegin");
		String accountDateEnd = request.getParameter("accountDateEnd");
		String payType = request.getParameter("payType");
		String isKb = request.getParameter("isKb");//是否是kb款 C472需求 新增查询筛选条件 by chy 2015年12月23日11:33:20
        String groupDateBegin = request.getParameter("groupDateBegin"); //出团日期--开始
        String groupDateEnd = request.getParameter("groupDateEnd"); //出团日期--结束
		String receiveConfirmDateBegin = request.getParameter("receiveConfirmDateBegin"); //收款确认日期--开始
		String receiveConfirmDateEnd = request.getParameter("receiveConfirmDateEnd"); //收款确认日期--结束
		String paymentType = request.getParameter("paymentType"); //渠道结算方式
		Map<String,String> map = new HashMap<String,String>();
		map.put("payerName", payerName);
		map.put("toBankNname", toBankNname);
		map.put("accountDateBegin", accountDateBegin);
		map.put("accountDateEnd",accountDateEnd);
		map.put("groupCode", groupCode);
		map.put("orderType",orderType);
		map.put("paymentType",paymentType);
		map.put("startCreateDate", startCreateDate);
		map.put("endCreateDate", endCreateDate);
		map.put("agentId", agentId);
		map.put("supplierInfo", supplierInfo);
		map.put("jd", jd);
		map.put("currency", currency);
		map.put("startMoney", startMoney);
		map.put("endMoney", endMoney);
		map.put("isAsAccount", isAsAccount);
		map.put("printFlag", printFlag);
		map.put("payType", payType);
		map.put("isKb", isKb);
        map.put("groupDateBegin", groupDateBegin); //出团日期--开始
        map.put("groupDateEnd", groupDateEnd); //出团日期--结束
		map.put("receiveConfirmDateBegin", receiveConfirmDateBegin); //收款确认日期--开始
		map.put("receiveConfirmDateEnd", receiveConfirmDateEnd); //收款确认日期--结束
		String showKb = "0";//是否展示KB款 KB款是针对天马运通的 C472需求 by chy 2015年12月23日11:41:54
        Office company = UserUtils.getUser().getCompany();
        String companyUuid = company.getUuid();
        Long companyId = company.getId();
        model.addAttribute("companyUuid",companyUuid);
		if(Context.SUPPLIER_UUID_TMYT.equals(companyUuid)){
			showKb = "1";
		}
		map.put("showKb", showKb);
		model.addAttribute("showKb", showKb);
		model.addAttribute("isKb", isKb);
		model.addAttribute("payType", payType);
		model.addAttribute("payerName", payerName);
		model.addAttribute("toBankNname", toBankNname);
		model.addAttribute("accountDateBegin", accountDateBegin);
		model.addAttribute("accountDateEnd",accountDateEnd);
		model.addAttribute("groupCode", groupCode);
		model.addAttribute("startCreateDate", startCreateDate);
		model.addAttribute("endCreateDate", endCreateDate);
		model.addAttribute("agentId", agentId);
		model.addAttribute("supplierInfo", supplierInfo);
		model.addAttribute("jd", jd);
		model.addAttribute("currency", currency);
		model.addAttribute("startMoney", startMoney);
		model.addAttribute("endMoney", endMoney);
		model.addAttribute("isAsAccount", isAsAccount);
		model.addAttribute("printFlag", printFlag);
        model.addAttribute("groupDateBegin", groupDateBegin); //出团日期--开始
        model.addAttribute("groupDateEnd", groupDateEnd); //出团日期--结束
		model.addAttribute("receiveConfirmDateBegin", receiveConfirmDateBegin); //收款确认日期--开始
		model.addAttribute("receiveConfirmDateEnd", receiveConfirmDateEnd); //收款确认日期--结束
		model.addAttribute("paymentType", paymentType); //渠道结算方式
		//收款银行
		SelectJson selectJson = selectService.getOfficePlatBankInfoForSelectJson();
		model.addAttribute("banks", selectJson.getData());

        //订单类型
		model.addAttribute("orderTypes", DictUtils.getDict2List("order_type"));
		 //渠道 注销 yudong.xu 2016-04-09
//        model.addAttribute("agentinfoList", AgentInfoUtils.getAgentList(companyId));
        model.addAttribute("agentinfoList", AgentInfoUtils.getQuauqAndOwnAgentList(companyId));
        /*计调 注销 yudong.xu 2016-04-09*/
		model.addAttribute("agentJd", agentinfoService.findAllUsers(companyId));
		//地接社
		model.addAttribute("supplierList", UserUtils.getSupplierInfoList(companyId, ""));
		//币种列表
		model.addAttribute("currencyList", currencyService.findCurrencyList(companyId));
		
		Page<Map<Object, Object>> pageOrder = null;
		model.addAttribute("orderType",orderType);
		pageOrder = costManageService.findCostRecordListForOtherIncome(orderType, new Page<Map<Object, Object>>(request, response), map);
		model.addAttribute("page", pageOrder);

        model.addAttribute("RXGG", Context.SUPPLIER_UUID_RXGG);//日信观光UUID
        model.addAttribute("isLMT", Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid));
        model.addAttribute("isQHJQ", Context.SUPPLIER_UUID_QHJQ.equals(companyUuid));//起航假期
		return "modules/order/orderlistforotherincome";
	}
	
	/**
	 * 其它收入收款列表页的导出功能 0004需求 by chy 2015-12-23 14:25:37
	 * @return
	 */
	@RequestMapping(value = "exporexcel")
	public void exportExcel(Model model, HttpServletRequest request, HttpServletResponse response){
		String groupCode= request.getParameter("groupCode");
		String orderType = request.getParameter("orderS");
		String startCreateDate = request.getParameter("startCreateDate");
		String endCreateDate = request.getParameter("endCreateDate");
		String agentId = request.getParameter("agentId");
		String supplierInfo = request.getParameter("supplierInfo");
		String jd = request.getParameter("jd");
		String currency = request.getParameter("currency");
		String startMoney = request.getParameter("startMoney");
		String endMoney = request.getParameter("endMoney");
		String isAsAccount = request.getParameter("isAsAccount");
		String printFlag = request.getParameter("printFlag");
		String payerName = request.getParameter("payerName");
		String toBankNname = request.getParameter("toBankNname");
		String accountDateBegin = request.getParameter("accountDateBegin");
		String accountDateEnd = request.getParameter("accountDateEnd");
		String payType = request.getParameter("payType");
		String isKb = request.getParameter("isKb");//是否是kb款 C472需求 新增查询筛选条件 by chy 2015年12月23日11:33:20
		String groupDateBegin = request.getParameter("groupDateBegin"); //出团日期--开始
		String groupDateEnd = request.getParameter("groupDateEnd"); //出团日期--结束
		String receiveConfirmDateBegin = request.getParameter("receiveConfirmDateBegin"); //收款确认日期--开始
		String receiveConfirmDateEnd = request.getParameter("receiveConfirmDateEnd"); //收款确认日期--结束

		Map<String,String> map = new HashMap<String,String>();
		map.put("payerName", payerName);
		map.put("toBankNname", toBankNname);
		map.put("accountDateBegin", accountDateBegin);
		map.put("accountDateEnd",accountDateEnd);
		map.put("groupCode", groupCode);
		map.put("orderType",orderType);
		map.put("startCreateDate", startCreateDate);
		map.put("endCreateDate", endCreateDate);
		map.put("agentId", agentId);
		map.put("supplierInfo", supplierInfo);
		map.put("jd", jd);
		map.put("currency", currency);
		map.put("startMoney", startMoney);
		map.put("endMoney", endMoney);
		map.put("isAsAccount", isAsAccount);
		map.put("printFlag", printFlag);
		map.put("payType", payType);
		map.put("isKb", isKb);
		map.put("groupDateBegin", groupDateBegin); //出团日期--开始
		map.put("groupDateEnd", groupDateEnd); //出团日期--结束
		map.put("receiveConfirmDateBegin", receiveConfirmDateBegin); //收款确认日期--开始
		map.put("receiveConfirmDateEnd", receiveConfirmDateEnd); //收款确认日期--结束

		String showKb = "0";//是否展示KB款 KB款是针对天马运通的 C472需求 by chy 2015年12月23日11:41:54
		String companyUUID = UserUtils.getUser().getCompany().getUuid();
		if(Context.SUPPLIER_UUID_TMYT.equals(companyUUID)){
			showKb = "1";
		}
		map.put("showKb", showKb);
		map.put("flag", "1");
		Page<Map<Object, Object>> pageOrder = null;
		pageOrder = costManageService.findCostRecordListForOtherIncome(orderType, new Page<Map<Object, Object>>(request, response), map);
		Workbook workbook = costManageService.makeExcel(pageOrder.getList());
		String fileName = "其它收入收款-" +DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd") + ".xls";
		ServletUtil.downLoadExcel(response, fileName, workbook);
	}
	
	@RequestMapping(value ="print/{payid}/{orderType}")
	public String orderPrint(@PathVariable Long payid,@PathVariable Integer orderType,Model model){
		model.addAttribute("payid", payid);	
		model.addAttribute("orderType", orderType);
		List<Map<Object, Object>> payList=null;
		if (orderType==Context.ORDER_TYPE_JP){
			payList=orderService.getAirPayPrint(payid.toString());
		}else if (orderType <  Context.ORDER_TYPE_QZ || orderType == Context.ORDER_TYPE_CRUISE){			
			payList=orderService.getActivityPayPrint(payid.toString(), orderType);
		}else if (orderType == Context.ORDER_TYPE_HOTEL){			
			payList=orderService.getHotelPayPrint(payid.toString());
		}else if (orderType == Context.ORDER_TYPE_ISLAND){			
			payList=orderService.getIslandPayPrint(payid.toString());
		}
		if (payList.size()==1){
			if (orderType==Context.ORDER_TYPE_JP){
				    model.addAttribute("groupCode", payList.get(0).get("groupCode"));
					model.addAttribute("from_area", DictUtils.getDictList("from_area"));// 出发城市
					String departureCity=DictUtils.getDictLabel(String.valueOf( payList.get(0).get("departureCity")), "from_area", "");
					String arrivedCity=areaService.get(Long.parseLong(payList.get(0).get("arrivedCity").toString())).getName();
				
					model.addAttribute("areas", areaService.findAirportCityList(""));//到达城市
					String airTypeValue=payList.get(0).get("airType").toString();
					String airType = "";
					if(StringUtils.isNotBlank(airTypeValue)){
						airType = AirTicketUtils.getAirType(Integer.parseInt(airTypeValue));
					}
					String ticket_area_type = "";
					String ticketAreaType = payList.get(0).get("ticket_area_type").toString();
					if(ticketAreaType.equals("1")) ticket_area_type = "（内陆）";
					else if(ticketAreaType.equals("2")) ticket_area_type = "（国际）";
					else if(ticketAreaType.equals("3")) ticket_area_type = "（内陆+国际）";
					model.addAttribute("groupName", departureCity + " &mdash;"+ arrivedCity + airType + ticket_area_type);	
					
			}else if (orderType <  Context.ORDER_TYPE_QZ || orderType == Context.ORDER_TYPE_CRUISE){			
				    model.addAttribute("groupCode", payList.get(0).get("groupCode"));
				    model.addAttribute("groupName", payList.get(0).get("acitivityName"));
			}else if (orderType == Context.ORDER_TYPE_HOTEL || orderType == Context.ORDER_TYPE_ISLAND){
				model.addAttribute("groupCode", payList.get(0).get("groupCode"));
				model.addAttribute("groupName", payList.get(0).get("activityName"));
			}

			//未达帐的不显示银行到账日期
			if(payList.get(0).get("isAsAccount") != null && !"1".equals(payList.get(0).get("isAsAccount").toString())){
				payList.get(0).put("accountDate", null);
			}
		   model.addAttribute("pay", payList.get(0));
		   
		   String orderid=payList.get(0).get("orderid").toString();
			String traveler="";
			if (orderType != Context.ORDER_TYPE_HOTEL && orderType != Context.ORDER_TYPE_ISLAND) {
				List<Map<Object, Object>> list=orderService.getTraveler(orderid,orderType);
				for(int i = 0;i < list.size(); i ++){
					if (i==0) traveler =list.get(i).get("name").toString();
					else  traveler +="； "+list.get(i).get("name");
				}
			} else if (orderType == Context.ORDER_TYPE_HOTEL) {
				List<HotelTraveler> hotelTravelerList=hotelTravelerDao.findTravelerByOrderUuid(payList.get(0).get("orderUuid").toString(), false);

				for(int i = 0;i < hotelTravelerList.size(); i ++){
					if (i==0) traveler =hotelTravelerList.get(i).getName();
					else  traveler +="； "+hotelTravelerList.get(i).getName();
				}
			} else if (orderType == Context.ORDER_TYPE_ISLAND) {
				List<IslandTraveler> islandTravelerList=islandTravelerDao.findTravelerByOrderUuid(payList.get(0).get("orderUuid").toString(), false);

				for(int i = 0;i < islandTravelerList.size(); i ++){
					if (i==0) traveler =islandTravelerList.get(i).getName();
					else  traveler +="； "+islandTravelerList.get(i).getName();
				}
			}
		   StringBuffer str = new StringBuffer();
		   if(StringUtils.isNotBlank(traveler)) {
				String[] array = traveler.split("；");
				for (int i=0;i<array.length;i++) {
					if(i%10!=0 || i==0){
						str.append(array[i]);
					}else{
						str.append(array[i]).append("").append("</br>");//去掉了分号 by chy 2016年1月27日10:43:21 bug号 12230
					}
				}
		   }
		   model.addAttribute("traveler", str.toString());
		   String capitalMoney = "";
		   String payPriceType=payList.get(0).get("payPriceType").toString();
		   //538需求（打印时需要加入差额）
		   Orderpay orderpay = orderpayDao.getById(payid);
		   String differenceCurrencyName = "";
		   BigDecimal differenceAmount = new BigDecimal(0);
		   if(StringUtils.isNotBlank(orderpay.getDifferenceUuid())){
			   ReturnDifference returnDifference = returnDifferenceService.getReturnDifferenceByUuid(orderpay.getDifferenceUuid());
			   Currency currency1 = currencyService.findById(returnDifference.getCurrencyId().longValue());
			   differenceAmount = differenceAmount.add(new BigDecimal(returnDifference.getReturnPrice().toString()));
			   differenceCurrencyName = currency1.getCurrencyName();
		   }
		   if(payPriceType!=null){
			   if(payPriceType.equals("11")||payPriceType.equals("21")|| payPriceType.equals("31")){
				  String payedMoney = payList.get(0).get("payedMoney").toString();
				  //当前批发商的美元、加元汇率（目前环球行）
				  List<Currency> currencylist = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
				  //其它币种汇率
				  BigDecimal currencyExchangerate = null;
				  //人民币计算
				  BigDecimal amountCHN = new BigDecimal("0");
				  //增加多币种金额判断20150422
				  String [] moneys = payedMoney.split("\\+");
				  if(moneys.length > 0) {
					  for(int i = 0 ; i < moneys.length ; i++) {
						  //韩元-2,000.00
						  Pattern p = Pattern.compile("\\-?\\d+\\.\\d+");
						  String notThundsMoney = moneys[i].replaceAll(",", "");
						  Matcher matcher = p.matcher(notThundsMoney);
						  matcher.find();
						  String money = matcher.group();
						  String currencyName = notThundsMoney.replaceAll(money, "").trim();
						  //如果币种名称相同，加上达账差额
						  if(differenceCurrencyName.equals(currencyName)){
							  BigDecimal moneyDifference = differenceAmount.add(new BigDecimal(money));
							  money = moneyDifference.toString(); 
						  }
						  for (Currency currency : currencylist) {
							  if(currency.getCurrencyName().equals(currencyName)) {
								  currencyExchangerate = currency.getConvertLowest();
								  break;
							  }
						  }
						  if(null == currencyExchangerate){
							  currencyExchangerate = new BigDecimal(1);
						  }
						  amountCHN = amountCHN.add(BigDecimal.valueOf(Double.parseDouble(money)).multiply(currencyExchangerate));
					  }
					  if(amountCHN.doubleValue() < 0) {
						  capitalMoney = "人民币" + "红字" + StringNumFormat.changeAmount(amountCHN.toString().replaceAll("-", ""));
						  model.addAttribute("payedMoney",   MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
					  }else {
						  capitalMoney = "人民币" + StringNumFormat.changeAmount(amountCHN.toString());
						  model.addAttribute("payedMoney",  MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
					  }
					  model.addAttribute("capitalMoney", capitalMoney);
				  }
			   }
		   }
		   boolean isHQX = false;
		   if(Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())){
			   isHQX = true;
		   }
		 //0002 需求 新增开票状态和开票金额 只针对环球行 by chy 2016年1月5日19:57:08 start
			if(isHQX){
			   //开票状态
				String invoiceStatus = "已开票";
				List<Orderinvoice> list = orderinvoiceService.findOrderinvoiceByOrderIdOrderType(Integer.parseInt(orderid), orderType); 
				List<Orderinvoice> list2 = orderinvoiceService.findOrderinvoiceByOrderIdOrderType2(Integer.parseInt(orderid), orderType); 
				if(list == null || list.size() == 0){//如果没有已开票的记录 则 肯定是待开票 或空
					if(list2 == null || list2.size() == 0){//如果没有待开票的记录 则为空
						invoiceStatus = "";
					} else {//否则就是待开票状态
						invoiceStatus = "待开票";
					}
				}
				 model.addAttribute("invoiceStatus", invoiceStatus);
				//开票金额
				String invoiceMoney = OrderCommonUtil.getOrderInvoiceMoney(orderType.toString(), orderid);
				if("0.00".equals(invoiceMoney) && "".equals(invoiceStatus)){
					invoiceMoney = "";
				} else {
					invoiceMoney = "¥ " + invoiceMoney;
				}
				 model.addAttribute("invoiceMoney", invoiceMoney);
			}
			 model.addAttribute("isHQX", isHQX); 
			//0002 需求 新增开票状态和开票金额 by chy 2016年1月5日19:57:08 end
		  model.addAttribute("groupOpenDate", payList.get(0).get("groupOpenDate")); 
		}
		
		Date printTime = null;
		if (orderType == Context.ORDER_TYPE_HOTEL) {
			PayHotelOrder payHotelOrder= payHotelOrderDao.getById(payid.intValue());
			printTime = payHotelOrder.getPrintTime();
		} else if (orderType == Context.ORDER_TYPE_ISLAND) {
			PayIslandOrder payIslandOrder= payIslandOrderDao.getById(payid.intValue());
			printTime = payIslandOrder.getPrintTime();
		} else {
			Orderpay orderpay = orderpayDao.findOne(payid);
			printTime = orderpay.getPrintTime();
		}
		//Integer printFlag=orderpay.getPrintFlag();
		if(null != printTime){
			model.addAttribute("firstPrintTime", printTime);
		}else{
			model.addAttribute("firstPrintTime", new Date());//处理已打印，但是无打印时间的情况（特殊情况）
		}
		/*if (printFlag==null || printFlag==0){
			orderpay.setPrintFlag(1);
			orderpay.setPrintTime(tempDateTime);
			model.addAttribute("firstPrintTime", tempDateTime);
			orderpayDao.save(orderpay);
		}*/
		//20151016环球行、拉美途客户确认到账时间为空
		String companyName = UserUtils.getUser().getCompany().getCompanyName();
		model.addAttribute("companyName", companyName);
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("isHQX", Context.SUPPLIER_UUID_HQX.equals(companyUuid));
		model.addAttribute("isLMT", Context.SUPPLIER_UUID_LMT.equals(companyUuid));
		return "modules/order/print";		
	}
	
	@RequestMapping(value="updatePrinted", method=RequestMethod.GET)
	public void updatePrinted(HttpServletRequest request, HttpServletResponse response, Model model){
		String json = "";
		Long payId = null;
		Long orderType = null;
		String payIdValue = request.getParameter("payId");
		String orderTypeValue = request.getParameter("orderType");
		if(StringUtils.isNotBlank(payIdValue)){
			payId = Long.valueOf(payIdValue);
			orderType = Long.valueOf(orderTypeValue);
			Date date = null;
			if (orderType == 11) {
				date = updateHotelOrderPayPrinted(payId);
			} else if (orderType == 12) {
				date = updateIslandOrderPayPrinted(payId);
			} else {
				date = updateOrderPayPrinted(payId);
			}
			if(null != date){
				String dateStr = DateUtils.date2String(new Date(), DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS);
				json = "{\"flag\":\"success\",\"msg\":\""+dateStr+"\"}";
			}
		}else{
			json = "{\"flag\":\"fail\",\"msg\":\"payId的值应该是数值，请检查\"}";
		}
		ServletUtil.print(response, json);
	}
	
	/**
	 * 更新打印状态以及打印时间，如果是已打印状态则不进行更新操作
	 * @param payId
	 * @return
	 */
	private Date updateOrderPayPrinted(Long payId){
		Date nowDate = null;
		try {
			Orderpay orderpay = orderpayDao.findOne(payId);
			Integer printFlag = orderpay.getPrintFlag();
			if (printFlag == null || printFlag == 0){
				orderpay.setPrintFlag(1);
				nowDate = new Date();
				orderpay.setPrintTime(nowDate);
				orderpayDao.save(orderpay);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nowDate;
	}
	
	/**
	 * 更新酒店打印状态以及打印时间，如果是已打印状态则不进行更新操作
	 * @param payId
	 * @return
	 */
	private Date updateHotelOrderPayPrinted(Long payId){
		Date nowDate = null;
		try {
			PayHotelOrder payHotelOrder = payHotelOrderDao.getById(payId.intValue());
			Integer printFlag = payHotelOrder.getPrintFlag();
			if (printFlag == null || printFlag == 0){
				payHotelOrder.setPrintFlag(1);
				nowDate = new Date();
				payHotelOrder.setPrintTime(nowDate);
				payHotelOrderDao.saveObj(payHotelOrder);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nowDate;
	}
	
	/**
	 * 更新海岛游打印状态以及打印时间，如果是已打印状态则不进行更新操作
	 * @param payId
	 * @return
	 */
	private Date updateIslandOrderPayPrinted(Long payId){
		Date nowDate = null;
		try {
			PayIslandOrder payIslandOrder = payIslandOrderDao.getById(payId.intValue());
			Integer printFlag = payIslandOrder.getPrintFlag();
			if (printFlag == null || printFlag == 0){
				payIslandOrder.setPrintFlag(1);
				nowDate = new Date();
				payIslandOrder.setPrintTime(nowDate);
				payIslandOrderDao.saveObj(payIslandOrder);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nowDate;
	}
	
	/**
	 * 
	* @Title: downloadList
	* @Description: TODO(下载收款单)
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
	
	@RequestMapping(value ="downloadList/{payid}/{orderType}")
	public ResponseEntity<byte[]> downloadList(@PathVariable String payid,
			@PathVariable String orderType,
			HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateException{
		if(StringUtils.isBlank(payid) || StringUtils.isBlank(orderType) ){
			return null;
		}
		File file = orderService.createReceiptFile(Long.parseLong(payid), Integer.parseInt(orderType));
		
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName = new StringBuilder().append("客人报名收款单").append(nowDate).append(".doc").toString();
		if (Context.ORDER_STATUS_HOTEL.equals(orderType)) {
			updateHotelOrderPayPrinted(Long.parseLong(payid));
		} else if (Context.ORDER_STATUS_ISLAND.equals(orderType)) {
			updateIslandOrderPayPrinted(Long.parseLong(payid));
		} else {
			updateOrderPayPrinted(Long.parseLong(payid));
		}
		WordDownLoadUtils.downLoadWordByAttachment(file, fileName, response);
		return null;
	}

	/**
	 * 金额处理：金额千位符与金额多币种id和数值读取
	 * @param listin
	 */
	private void handlePrice(Map<Object, Object> listin) {
		
		
		//尾款：用于页面尾款支付
		Object payStatusObj = listin.get("payStatus");
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
							if (!"0.00".equals(moneyAmonut)) {
								listin.put(para, currencyMark + moneyAmonut);
							} else {
								listin.put(para, "");
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 计算订单剩余天数
	 * @param listin
	 */
	private void getOrderLeftTime(Map<Object, Object> listin) {
		
		//判断剩余时间用激活时间：新需求（2014-07-10）
        String activationDate = String.valueOf(listin.get("activationDate"));
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
				logger.error("日期转换异常", e);
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
	private void selectPayOrder(List<Long> listProId, List<Map<Object, Object>> listorder, String orderStatus, Model model) {
		
		if (CollectionUtils.isNotEmpty(listProId)) {
			List<Orderpay> orderpayList = new ArrayList<Orderpay>();
			if(StringUtils.isNotBlank(orderStatus))
				orderpayList = orderService.findOrderpayByOrderIds(listProId, Integer.parseInt(orderStatus));
            for (Map<Object, Object> map : listorder) {
            	Integer isAsAccount = 0;// 空为未达帐 0为撤销 1为达帐 2为驳回
                List<Orderpay> listTempOrderPay = new ArrayList<Orderpay>();
                for (Orderpay orderpay : orderpayList) {
                    //如果orderpay的订单id  等于  pro的订单id
                    if (orderpay.getOrderId().intValue() == Integer.parseInt(map.get("id").toString())) {
                        listTempOrderPay.add(orderpay);
                        if (StringUtils.isNotBlank(orderpay.getMoneySerialNum())) {
                        	orderService.clearObject(orderpay);
                        	orderpay.setMoneySerialNum(moneyAmountService.getMoneyStr(orderpay.getMoneySerialNum()));
                        	//判断订单是否有达帐支付记录：用户退款判断
                        	if (isAsAccount != 1 && orderpay.getIsAsAccount() != null && orderpay.getIsAsAccount() == 1) {
                        		isAsAccount = 1;
                        		map.put("isAsAccount", isAsAccount);
                        	}
                        }
                    }
                }
                
                //支付信息
                map.put("orderPayList", listTempOrderPay);
                //达帐和撤销提示
                boolean isCanceledOrder = false;
                if (map.get("payStatus") != null) {
                	isCanceledOrder = Context.ORDER_PAYSTATUS_YQX.equals(map.get("payStatus").toString());
                }
                map.put("promptStr", orderService.getOrderPrompt(orderStatus, Long.parseLong(map.get("id").toString()), isCanceledOrder));
            }
        }
	}
	
	@ResponseBody
	@RequestMapping(value ="getPayList")
	public Object getPayList(Model model,HttpServletRequest request){
	    String orderId = request.getParameter("orderId");
	    List<Orderpay> list = orderService.findOrderpayByOrderId(Long.parseLong(orderId));
	    Map<String, Object> map = new HashMap<String, Object> ();
	    map.put("orderList", list);
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
	    ProductOrderCommon pro = orderService.getProductorderById(Long.parseLong(orderId));
	    
	    //支付订单金额千位符处理
	    if (StringUtils.isNotBlank(orderpay.getMoneySerialNum())) {
	 	    orderService.clearObject(orderpay);
	 	    orderpay.setMoneySerialNum(moneyAmountService.getMoneyStr(orderpay.getMoneySerialNum()));
	    }
	    
	    //订单金额千位符处理
	    if (StringUtils.isNotBlank(pro.getTotalMoney())) {
	 	    orderService.clearObject(pro);
	 	    pro.setTotalMoney(moneyAmountService.getMoneyStr(pro.getTotalMoney()));
	    }
	  
		model.addAttribute("orderpay",orderpay);
		model.addAttribute("orderId",orderId);
		model.addAttribute("productorder",pro);
	    return "modules/order/modifypayVoucher";
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
		
		ProductOrderCommon order = orderService.getProductorderById(Long.parseLong(orderId));
		
		//如果订单为空则返回错误
		if (order == null) {
			resobj.put("flag", "false");
			resobj.put("warning", "查询不到此订单");
			results.add(resobj);
			return results;
		}
		
		//如果支付订单状态为空则返回错误
		Integer payStatus = order.getPayStatus();//订单状态
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
		List<Orderpay> orderpayList = orderService.findOrderpayByOrderIds(ids, order.getOrderStatus());
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
					String price = (list.get(i)[3] != null ? list.get(i)[3].toString() : "0");
					frontCurreney.add(list.get(i)[0].toString() + " " + price);
    				if (i == list.size() -1) {
    					currencyId += list.get(i)[0];
	        			currencyPrice += price;
    				} else {
    					currencyId += list.get(i)[0] + ",";
	        			currencyPrice += price + ",";
    				}
    			}
				resobj.put("flag", "true");
				resobj.put("payPriceType", Context.ORDER_ORDERTYPE_ZFDJ);
				resobj.put("moneyCurrencyId", currencyId);
				resobj.put("moneyCurrencyPrice", currencyPrice);
			} else {
				resobj.put("flag", "false");
				resobj.put("warning", "订单没有订金金额");
				results.add(resobj);
				return results;
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
					//全款的时候 538需求 收款的最终金额减去差额
					if(order.getDifferenceFlag() == 1 && StringUtils.isNotBlank(order.getDifferenceMoney())){
						String[] totalCurrencyIds = currencyId.split(",");
						String[] totalMoneys = currencyPrice.split(",");
						List<Object[]> amonut = moneyAmountService.getMoneyAmonut(order.getDifferenceMoney());
						Object[] objects = amonut.get(0);
						BigDecimal big = new BigDecimal(0);
						String s = "";
						for(int i = 0 ; i < totalMoneys.length; i ++){
							Currency currency = currencyService.findById(Long.parseLong(totalCurrencyIds[i]));
							if(currency.getCurrencyMark().equals(objects[2])){
								big = new BigDecimal(totalMoneys[i]).subtract(new BigDecimal(objects[3].toString()));
								totalMoneys[i] = big.toString();
							}
							if(StringUtils.isBlank(s)){
								s = totalMoneys[i];
							}else{
								s =  s+","+totalMoneys[i];
							}
						}
						currencyPrice = s;
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
							//转换成人民币
							totalMoney = totalMoney.add(new BigDecimal(list.get(i)[3].toString()).multiply(new BigDecimal(list.get(i)[4].toString())));
						}
	    			}
					//从总额中减去差额
					if(order.getDifferenceFlag() != null && order.getDifferenceFlag() == 1){
						List<Object[]> amonut = moneyAmountService.getMoneyAmonut(order.getDifferenceMoney());
						Object[] objects = amonut.get(0);
						for(int i = 0 ; i < totalCurreney.size() ; i ++){
							String[] split = totalCurreney.get(i).split(" ");
							if(split[0].equals(objects[0].toString())){
								BigDecimal bigDecimal = new BigDecimal(split[1]).subtract(new BigDecimal(objects[3].toString()));
								totalCurreney.remove(i);
								totalCurreney.add(split[0]+" "+bigDecimal);
							}
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
							//转换成人民币
							payedMoney = payedMoney.add(new BigDecimal(list.get(i)[3].toString()).multiply(new BigDecimal(list.get(i)[4].toString())));
						}
	    			}
				}
			}
			//从已收中减去已收差额
			if(order.getDifferenceFlag()==1){
				ReturnDifference returnDifference = returnDifferenceService.getDifferenceByOrderId(order.getId().intValue());
				if (null != returnDifference) {
					Currency currency = currencyService.findById(returnDifference.getCurrencyId().longValue());
					payedMoney = payedMoney.subtract(new BigDecimal(returnDifference.getReturnPrice().toString()).multiply(currency.getCurrencyExchangerate())); 
					for(int i = 0 ; i < payedCurreney.size() ; i ++){
						String[] split = payedCurreney.get(i).split(" ");
						if(returnDifference.getCurrencyId().toString().equals(split[0])){
							BigDecimal bigDecimal = new BigDecimal(split[1]).subtract(new BigDecimal(returnDifference.getReturnPrice().toString()));
							payedCurreney.remove(i);
							payedCurreney.add(split[0]+" "+bigDecimal);
						}
					}
				}
			}
			List<String> result = moneyAmountService.subtract(totalCurreney, payedCurreney);
			
			//如果有负值，则把尾款转换成人民币，如果为正则可支付，如果为负则不允许支付
			if (CollectionUtils.isNotEmpty(result) && result.get(1).contains("-")) {
				if (totalMoney.compareTo(payedMoney) < 0) {
					resobj.put("flag", "choose");
					resobj.put("payPriceType", Context.ORDER_ORDERTYPE_ZFWK);
					resobj.put("moneyCurrencyId", result.get(0));
					resobj.put("moneyCurrencyPrice", result.get(1));
					resobj.put("warning", "已收金额大于订单金额，是否还要继续收款？");
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
		
		// 如果有门店返还差额，则需要把这个订单金额给去除
//		subtractDifferenceMoney(order, resobj.getString("moneyCurrencyId"), resobj.getString("moneyCurrencyPrice"), resobj);
		
		Map<String, String> totalMoneyMap = moneyContactVal(moneyAmountService.getMoneyAmonut(order.getTotalMoney()));
		//全款的时候 538需求
		if(order.getDifferenceFlag() == 1 && StringUtils.isNotBlank(order.getDifferenceMoney())){
			String[] totalCurrencyIds = totalMoneyMap.get("currencyId").split(",");
			String[] totalMoneys = totalMoneyMap.get("currencyPrice").split(",");
			List<Object[]> amonut = moneyAmountService.getMoneyAmonut(order.getDifferenceMoney());
			Object[] objects = amonut.get(0);
			BigDecimal big = new BigDecimal(0);
			String s = "";
			for(int i = 0 ; i < totalMoneys.length; i ++){
				Currency currency = currencyService.findById(Long.parseLong(totalCurrencyIds[i]));
				if(currency.getCurrencyMark().equals(objects[2])){
					big = new BigDecimal(totalMoneys[i]).subtract(new BigDecimal(objects[3].toString()));
					totalMoneys[i] = big.toString();
				}
				if(StringUtils.isBlank(s)){
					s = totalMoneys[i];
				}else{
					s =  s+","+totalMoneys[i];
				}
			}
			totalMoneyMap.put("currencyPrice", s);
		}
		resobj.put("totalMoneyCurrencyId", totalMoneyMap.get("currencyId"));
		resobj.put("totalMoneyCurrencyPrice", totalMoneyMap.get("currencyPrice"));
		
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
	 * 如果有门店返还差额，则需要把这个订单金额给去除
	 * @param order
	 * @param currencyId
	 * @param currencyPrice
	 * @param resobj
	 * @author yakun.bai
	 * @Date 2016-10-27
	 */
//	private void subtractDifferenceMoney(ProductOrderCommon order, String currencyId, String currencyPrice, net.sf.json.JSONObject resobj) {
//		Integer differenceFlag = order.getDifferenceFlag();
//		String differenceMoneyUuid = order.getDifferenceMoney();
//		if (null != differenceFlag && differenceFlag == 1 && StringUtils.isNotBlank(differenceMoneyUuid)) {
//			List<MoneyAmount> moneyAmountList = moneyAmountService.findBySerialNum(differenceMoneyUuid);
//			if (CollectionUtils.isNotEmpty(moneyAmountList)) {
//				MoneyAmount amount = moneyAmountList.get(0);
//				Integer differenceCurrencyId = amount.getCurrencyId();
//				BigDecimal differenceMoney = amount.getAmount();
//				String[] currencyArr = currencyId.split(",");
//				for (int i = 0; i < currencyArr.length; i++) {
//					if (currencyArr[i].equals(differenceCurrencyId.toString())) {
//						currencyPrice.split(",")[i] = new BigDecimal(currencyPrice.split(",")[i]).subtract(differenceMoney).toString();
//						String newCurrencyPrice = "";
//						for (int j = 0; j < currencyArr.length; j++) {
//							String tempCurrencyPrice = 
//							newCurrencyPrice += "," + currencyArr[j];
//						}
//						resobj.put("moneyCurrencyPrice", currencyPrice);
//					}
//				}
//			}
//		}
//	}

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
		orderService.updatepayVoucherFile(infoList, orderpay,orderId,model, request);
		
	    return "modules/order/uploadVoSuccess";
	   
	}
	
    /**
     *  功能: 设置model  通过orderId获取该订单的信息
     *
     *  @author xuziqian
     *  @DateTime 2014-1-18 下午6:35:51
     *  @param orderId
     *  @param model
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     */
    private void setModelWhenGetInfoByOrderId(Model model, ProductOrderCommon productOrder, String activityKind, Agentinfo agentinfo) {
    	TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
    	productGroup.setOrderPersonNum(Integer.parseInt(applyOrderCommonService.sumOrderPersonNumByGroupId(productOrder.getProductGroupId()).get(0)[0].toString()));
		setControlModel(model, product);
		
        productOrder.setOrderPersonNum(0);
        productOrder.setOrderPersonNumAdult(0);
        productOrder.setOrderPersonNumChild(0);
        productOrder.setOrderPersonNumSpecial(0);
        productOrder = orderService.saveProductorderOnReserve(productOrder, product, agentinfo);
        
        setCurrency(model, productOrder, productGroup, activityKind, null);
        
		//add by yang.jiang 2016-01-12 11:20:55
		//组织渠道商对应的联系人（由于第一联系人跟其他联系人不在一起） 
		if(agentinfo != null){
			List<SupplierContacts> contacts;
			if (agentinfo.getId() == -1) {
				contacts = null;
			} else {
				contacts = supplierContactsService.findAllContactsByAgentInfo(agentinfo.getId());  //取出渠道商所有联系人（包括第一联系人）
			}
//		List<SupplierContacts> otherContacts = supplierContactsService.findContactsByAgentInfo(Long.parseLong(agentId));  //此方法只能取出渠道商非第一联系人
//		model.addAttribute("otherContacts", otherContacts);
			// 渠道商邮编
			model.addAttribute("zipCode", agentinfo.getAgentPostcode() == null ? "" : agentinfo.getAgentPostcode());
			//渠道商的联系地址
			String address = agentinfoService.getAddressStrById(agentinfo.getId());
			model.addAttribute("address", address == null ? "" : address);
			if (CollectionUtils.isNotEmpty(contacts)) {
				//渠道商转换为json
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
				model.addAttribute("contactsView", contactsView);
				model.addAttribute("contacts", contacts);
				org.json.JSONArray contactArrayView = supplierContactsService.contacts2JsonArray4View(contactsView);
				model.addAttribute("contactArrayView", contactArrayView);
				String contactsJsonStr = supplierContactsService.contacts2Json(contacts);
				model.addAttribute("contactsJsonStr", contactsJsonStr);
				org.json.JSONArray contactArray = supplierContactsService.contacts2JsonArray(contacts);
				model.addAttribute("contactArray", contactArray);
			} else {
				model.addAttribute("contactsView", Lists.newArrayList());
				model.addAttribute("contacts", null);
				model.addAttribute("contactArrayView", null);
				model.addAttribute("contactsJsonStr", "{}");
				model.addAttribute("contactArray", null);
			}
//			List<String> contactList = supplierContactsService.contacts2JsonList(contacts);
			model.addAttribute("agentId",agentinfo.getId());
//			model.addAttribute("contactList", contactList);
		}
		//订单是否允许添加多个渠道联系人信息
		Integer allowAddAgentInfo = UserUtils.getUser().getCompany().getIsAllowAddAgentInfo();
		model.addAttribute("allowAddAgentInfo", allowAddAgentInfo);
		//订单是否允许渠道联系人信息输入和修改
		Integer allowModifyAgentInfo = UserUtils.getUser().getCompany().getIsAllowModifyAgentInfo();
		model.addAttribute("allowModifyAgentInfo", allowModifyAgentInfo);
		
        ActivityGroupReserve groupReserve = StockService.findGroupReserve(productOrder.getOrderCompany(), product.getId(), productGroup.getId());
        model.addAttribute("groupReserve",groupReserve);
        
        //获取团期剩余儿童人数、剩余特殊人数
        Map<String, Object>  counts = activityGroupService.countOrderChildAndSpecialNum(productOrder.getProductGroupId(),null);
        
	    model.addAttribute("product",product);
	    model.addAttribute("productGroup",productGroup);
	    model.addAttribute("counts",counts);
	    model.addAttribute("productorder",productOrder);
	    model.addAttribute("agentinfo", agentinfo);
	    model.addAttribute("activityKind", activityKind);
	    model.addAttribute("orderTypeStr", OrderCommonUtil.getStringOrdeType(activityKind));
	    model.addAttribute("travelerKind", activityKind);
	    // 20150716 增加币种列表   定义订单应收币种集合
		List<Currency> currencyList = Lists.newArrayList();
		currencyList = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
		if(!currencyList.isEmpty()){
			model.addAttribute("currencyList", currencyList);
		}
		Currency RMB = currencyService.findRMBCurrency(UserUtils.getUser().getCompany().getId());
		if (RMB != null) {			
			model.addAttribute("RMB_currencyId", RMB.getId());
		}
    }
	
	@ResponseBody
	@RequestMapping(value ="deleteCost")
	public String deleteCost(Model model,HttpServletRequest request){
	    String costId = request.getParameter("costId");
	    if(StringUtils.isNotBlank(costId)){
	        orderService.deleteCostChange(Long.parseLong(costId));
	    }
	    return "ok";
	}
	
	
	@ResponseBody
	@RequestMapping(value ="deleteOrderByFlag")
	public String deleteOrderByFlag(Model model,HttpServletRequest request) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
	    String id = request.getParameter("orderId");
	    /**
	     * update by ruyi.chen
	     * update date 2015-10-12
	     * 增加由于发票、收据的状态判断是否能取消删除订单
	     */
	    ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(id));
	    if(null != productOrder && 0 < productOrder.getId()){
	    	String result = OrderCommonUtil.canCancel(productOrder.getOrderStatus(),productOrder.getId());
	    	if("0".equals(result)){
	    		if(StringUtils.isNotBlank(id)){
	    	        try {
	    				orderService.deleteOrder(Long.parseLong(id), request);
	    			} catch (NumberFormatException e) {
	    				logger.error("数据格式错误", e);
	    			} 
	    	    }
	    	    return "ok";
	    	}else if("1".equals(result)){
	    		return "此订单已有开发票记录，不能删除!";
	    	}else if("2".equals(result)){
	    		return "此订单已有开收据记录，不能删除!";
	    	}
	    }
	    return "";
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
	    	ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(id));
	    	if (productOrder != null) {
    			Integer payStatus = productOrder.getPayStatus();
    			String status = payStatus.toString();
    			if (Context.ORDER_PAYSTATUS_OP.equals(status)) {
    				try {
						flag = orderService.confirmOrder(Long.parseLong(id), request);
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
	 * 激活订单
	 * @param model
	 * @param request
	 * @return string
	 * @throws NumberFormatException
	 * @throws OptimisticLockHandleException
	 * @throws PositionOutOfBoundException
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value ="invokeOrder")
	public String invokeOrder(Model model, HttpServletRequest request) throws NumberFormatException, OptimisticLockHandleException, PositionOutOfBoundException, Exception {
		String flag = "fail";
	    String id = request.getParameter("orderId");
	    if (StringUtils.isNotBlank(id)) {
	    	ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(id));
	    	if (productOrder != null) {
    			Integer payStatus = productOrder.getPayStatus();
    			String status = payStatus.toString();
    			if ((Context.ORDER_PAYSTATUS_YQX.equals(status) 
    					|| Context.ORDER_PAYSTATUS_DJWZF.equals(status) 
    					|| Context.ORDER_PAYSTATUS_YZW.equals(status))) {
    				flag = orderService.invokeOrder(Long.parseLong(id), request);
    			} else {
    				return "只有订单状态为'取消'或'订金未支付'或'预占位'的订单才能激活";
    			}
    		}	    	
	    }
	    if (StringUtils.isBlank(flag)){
	    	flag = "fail";
	    }
	    return flag;
	}
	
	@ResponseBody
	@RequestMapping(value ="getProductRemainDayInfo")
	public Map<String, String> getProductRemainDayInfo(HttpServletRequest request) {
		Map<String, String> result = Maps.newHashMap();
		String orderId = request.getParameter("orderId");
	    if (StringUtils.isNotBlank(orderId)) {
	    	ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
	    	ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
        	// 判断订单是否已过出团日期
        	if (1 == UserUtils.getUser().getCompany().getIsCancleOrder()) {
        		Calendar currentDate = Calendar.getInstance();
        		Calendar groupOpenDate = Calendar.getInstance();
        		groupOpenDate.setTime(productGroup.getGroupOpenDate());
        		if (currentDate.getTimeInMillis() - groupOpenDate.getTimeInMillis() > 0) {
        			result.put("result", "error");
        			result.put("msg", "超过出团日期");
        			return result;
        		}
        	}
        	TravelActivity activity = travelActivityService.findById(productOrder.getProductId());
        	String payMode = productOrder.getPayMode();
	    	result.put("result", "success");
	    	result.put("day", "0");
        	result.put("hour", "0");
        	result.put("minute", "0");
        	
	    	if("1".equals(payMode)) {
	        	// 获取保留时限的天、时、分
	        	Double day = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_deposit());
	        	Double hour = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_deposit_hour());
	        	Double minute = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_deposit_fen());
	        	result.put("day", day.toString());
	        	result.put("hour", hour.toString());
	        	result.put("minute", minute.toString());
	        } else if("2".equals(payMode)) {
	        	// 获取保留时限的天、时、分
	        	Double day = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_advance());
	        	Double hour = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_advance_hour());
	        	Double minute = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_advance_fen());
	        	result.put("day", day.toString());
	        	result.put("hour", hour.toString());
	        	result.put("minute", minute.toString());
	        }  else if("4".equals(payMode)) {
	        	// 获取保留时限的天、时、分
	        	Double day = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_data());
	        	Double hour = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_data_hour());
	        	Double minute = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_data_fen());
	        	result.put("day", day.toString());
	        	result.put("hour", hour.toString());
	        	result.put("minute", minute.toString());
	        }  else if("5".equals(payMode)) {
	        	// 获取保留时限的天、时、分
	        	Double day = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_guarantee());
	        	Double hour = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_guarantee_hour());
	        	Double minute = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_guarantee_fen());
	        	result.put("day", day.toString());
	        	result.put("hour", hour.toString());
	        	result.put("minute", minute.toString());
	        }  else if("6".equals(payMode)) {
	        	// 获取保留时限的天、时、分
	        	Double day = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_express());
	        	Double hour = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_express_hour());
	        	Double minute = com.trekiz.admin.common.utils.NumberUtils.integerToDouble(activity.getRemainDays_express_fen());
	        	result.put("day", day.toString());
	        	result.put("hour", hour.toString());
	        	result.put("minute", minute.toString());
	        }
	    	result.put("payMode", payMode);
	    } else {
	    	result.put("msg", "订单ID不能为空");
	    }
		return result;
	}


	/**
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
        	ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(id));
        	String result = OrderCommonUtil.canCancel(productOrder.getOrderStatus(),productOrder.getId());
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
	 * 取消订单
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value ="cancelOrder")
	public String cancelOrder(Model model, HttpServletRequest request) throws Exception {
	    String id = request.getParameter("orderId");
	    String description = request.getParameter("description");
	    if (StringUtils.isNotBlank(id)) {
	        try {
        		ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(id));
        		if (productOrder != null) {
        			/**
        			 * update by ruyi.chen
        			 * update date 2015-10-12
        			 *  判断发票收据状态是否符合取消条件
        			 */
        			
        			String result = OrderCommonUtil.canCancel(productOrder.getOrderStatus(),productOrder.getId());
        			if("1".equals(result)){
        				return "此订单已有开发票记录，不能取消!";
        			}else if("2".equals(result)){
        				return "此订单已有开收据记录，不能取消!";
        			}
        			Integer payStatus = productOrder.getPayStatus();
        			String status = payStatus.toString();
//        			//订单除了已取消的，其余都可以取消，包括已支付订单
//        			if(!Context.ORDER_PAYSTATUS_YQX.equals(status)) {
        			if(Context.ORDER_PAYSTATUS_WZF.equals(status) 
        					|| Context.ORDER_PAYSTATUS_DJWZF.equals(status) 
        					|| Context.ORDER_PAYSTATUS_YZW.equals(status)
        					|| Context.ORDER_PAYSTATUS_OP.equals(status)
        					|| Context.ORDER_PAYSTATUS_CW.equals(status)
        					|| (Context.PLACEHOLDERTYPE_ZW.equals(productOrder.getPlaceHolderType()) && Context.ORDER_PAYSTATUS_YZFDJ.equals(status))) {
        				
        				//单团类订单取消
        				if (Integer.parseInt(Context.ORDER_STATUS_CRUISE) != productOrder.getOrderStatus()) {
        					orderService.cancelOrder(productOrder, description, 8, request);
        				} 
        				//游轮订单取消
        				else {
        					cruiseOrderService.cancelOrder(productOrder, description, request);
        				}
        			} else {
        				if (Context.ORDER_PAYSTATUS_YZFDJ.equals(status)) {
        					return "订单已支付订金，不能取消";
        				} else if(Context.ORDER_PAYSTATUS_YZF.equals(status)) {
        					return "订单已支付全款，不能取消";
        				} else if(Context.ORDER_PAYSTATUS_YQX.equals(status)) {
        					return "订单已取消，不能再次取消";
        				}
        				return "fail";
//        				return "订单已取消，不能再次取消";
        			}
        		}
			} catch (NumberFormatException e) {
				logger.error("数据格式错误", e);
			} catch (OptimisticLockHandleException e) {
				logger.error("存入团期内容失败", e);
				throw new OptimisticLockHandleException("存入团期内容失败，请重试。");
			}
	    }
	    return "ok";
	}
	
	
	/**
	 * @Description 已撤销占位
	 * @author yakun.bai
	 * @Date 2015-11-16
	 */
	@ResponseBody
	@RequestMapping(value ="revokeOrder")
	public String revokeOrder(Model model, HttpServletRequest request) throws Exception {
		String id = request.getParameter("orderId");
		if (StringUtils.isNotBlank(id)) {
			try {
				ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(id));
				if (productOrder != null) {
					//获取订单状态
					Integer payStatus = productOrder.getPayStatus();
					String status = payStatus.toString();
					
					//只有当订单状态为已支付订金或已支付全款的时候才可以撤销占位
					if(Context.ORDER_PAYSTATUS_YZFDJ.equals(status) 
							|| Context.ORDER_PAYSTATUS_YZF.equals(status)) {
						
						//单团类订单撤销占位
						if (Integer.parseInt(Context.ORDER_STATUS_CRUISE) != productOrder.getOrderStatus()) {
							orderService.revokeOrder(productOrder, request);
						} 
						//游轮订单撤销占位
						else {
							cruiseOrderService.revokeOrder(productOrder, request);
						}
					} else {
						if (Context.ORDER_PAYSTATUS_CW_CX.equals(status)) {
							return "订单已撤销占位，不能再次撤销";
						}
						return "fail";
					}
				}
			} catch (NumberFormatException e) {
				logger.error("数据格式错误", e);
			} catch (OptimisticLockHandleException e) {
				logger.error("存入团期内容失败", e);
				throw new OptimisticLockHandleException("存入团期内容失败，请重试。");
			}
		}
		return "ok";
	}
	
    @RequestMapping(value = "lockOrder")
    @ResponseBody
    public Object lockOrder(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        String orderId = request.getParameter("orderId");
        if (StringUtils.isNotBlank(orderId)) {
        	this.orderService.lockOrder(Long.valueOf(orderId));
        	map.put("success", "success");
        } else {
        	map.put("error", "订单Id为空");
        }
        return map;
    }
    
    @RequestMapping(value = "unLockOrder")
    @ResponseBody
    public Object unLockOrder(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        String orderId = request.getParameter("orderId");
        if (StringUtils.isNotBlank(orderId)) {
        	this.orderService.unLockOrder(Long.valueOf(orderId));
            map.put("success", "success");
        } else {
        	map.put("error", "订单Id为空");
        }
        return map;
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
        	int changeSum = this.orderService.changeNotSeenOrderFlag(notSeenOrderIdList);
        	map.put("result", "success");
        	map.put("changeSum", changeSum);
        } else {
        	map.put("result", "error");
        }
        return map;
    }
	
	 /**
	 *  功能:
	 *         点击达账
	 *  @author xuziqian
	 *  @DateTime 2014-1-22 下午2:44:33
	 *  @param request
	 *  @return
	 */
	@RequestMapping(value ="saveAsAcount")
	@ResponseBody
	public Object saveAsAcount(HttpServletRequest request){
	    String orderId = request.getParameter("orderId");
	    String moneyType = request.getParameter("moneyType");
	    String orderType = request.getParameter("orderType");
	    String serialNum = request.getParameter("serialNum");
	    moneyAmountService.updateOrderAccountedMoney(Long.parseLong(orderId), Integer.parseInt(orderType), Integer.parseInt(moneyType),serialNum);
	    return null;
	}
	/**
	 * 
	* @Title: payPal
	* @Description: TODO(付款确认)
	* @param @param request
	* @param @return    设定文件
	* @return Object    返回类型
	* @throws
	 */
	@RequestMapping(value ="payPal")
	@ResponseBody
	public Object payPal(HttpServletRequest request) {
		String id = request.getParameter("id");
		if(StringUtils.isNotBlank(id)) {
			CostRecord costRecord = costManageService.findOne(Long.parseLong(id));
			costRecord.setPayStatus(1);
			costManageService.saveCostRecord(costRecord);
		}
		return null;
	}
	
	@RequestMapping(value="confirmPay")
	@ResponseBody
	@Transactional
	public Object confirmPay(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> data = new HashMap<String, Object>();
		String id = request.getParameter("id");
		String type= request.getParameter("type"); //1：成本录入付款；2：退款付款；3：返佣付款；4：借款付款
		String status = request.getParameter("status");//0表示未付，1表示已付
		String orderType = request.getParameter("orderType");
		String rate = request.getParameter("rate");
		String afterPrice = request.getParameter("afterAmount");
		String confirmCashierDate = request.getParameter("confirmCashierDate");// 确认出纳时间
		try{
			if(confirmCashierDate != null && !"".equals(confirmCashierDate)){
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date date_confirmPayDate = df.parse(confirmCashierDate);
				orderService.confirmOrCancelPay(id, type, status,orderType,rate,afterPrice, date_confirmPayDate);
			}else{
				orderService.confirmOrCancelPay(id, type, status, orderType, rate, afterPrice, null);
			}
			data.put("flag", "ok");
		}catch (Exception e) {
			data.put("flag", "no");
			e.printStackTrace();
		}
		return data;
	}
	
	// 针对奢华之旅   成本付款和退款付款的发票状态的修改    BY  jinxin.gao
	@RequestMapping(value="confimOrCancelInvoice")
	public void confimOrCancelInvoice(HttpServletRequest request, HttpServletResponse response, Model model){
		ResponseJson json = new ResponseJson();
		json.setFlag(true);//默认操作执行成功
		String id = request.getParameter("id");
		String status = request.getParameter("status");
		try {
			if(id.contains(",")){
				// 批量确认
				String[] ids = id.split(",");
				for (String cost_recordId : ids) {
					orderService.confimOrCancelInvoiceAll(cost_recordId.split("_")[0]);
				}
			}else{
				// 单条的确认与取消
				orderService.confimOrCancelInvoice(id, status);
			}


		} catch (Exception e) {
			e.printStackTrace();
			json.setFlag(false);
			json.setMsg(e.getMessage());
		}
		String jsonStr = com.alibaba.fastjson.JSONObject.toJSONString(json);
		ServletUtil.print(response, jsonStr);
	}
	
	@RequestMapping(value="batchConfirmPay")
	@ResponseBody
	public Object batchConfirmPay(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> data = new HashMap<String, Object>();
		String id = request.getParameter("id");//id组成 recordId_orderType(产品类型)_isNew 示例 1720_7_1,q721_6_2,
       //type=1：成本录入付款；type=2：退款付款；type=3：返佣付款；type=4：借款付款；type=7：新审核成本录入付款；type=8：新审核退款付款；type=9：新审核返佣付款;type=10：新审核借款付款；
		String type= request.getParameter("type"); 
		String status = request.getParameter("status");//0表示未付，1表示已付
		String rates = request.getParameter("rates");
		String afterAmounts = request.getParameter("afterAmounts");
		String confirmCashierDates = request.getParameter("confirmCashierDates");// 出纳确认时间
//		String payConfirmDates = request.getParameter("payConfirmDate");
		try{
			if(StringUtils.isNotBlank(id)){
				String [] tmp = id.split(",");
				int len = tmp.length;
				if(len>0){
					String[] ratess = null;
					String[] afterAmountss = null;
					String[] confirmCashierDate = null;
                    if(StringUtils.isNotBlank(rates)){
						ratess = rates.split("_");
						afterAmountss = afterAmounts.split("_");
						confirmCashierDate = confirmCashierDates.split("@");
					}
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
					for(int i =0;i<len;i++){
						String str = tmp[i];
						Date date_confirmPayDate = null;
						if(confirmCashierDate != null){
							date_confirmPayDate = df.parse(confirmCashierDate[i]);
						}
						if(StringUtils.isNotBlank(str)){
							String[] arr = str.split("_");
							String isNew = arr[2];//1表示旧审核数据，2表示新审核数据
							if("1".equals(isNew)){//旧审核成本、退款、返佣、退款付款确认或取消
								String rate = null;
								String amount = null;
								if(null != ratess){
									rate = ratess[i];
								}
								if(null != afterAmountss){
									amount = afterAmountss[i];
								}
								if (String.valueOf(Refund.MONEY_TYPE_BATCHBORROW).equals(type)) {
									/**
									 *  批量借款付款状态更新
									 * 1、根据批量Id获取reviewId
									 * 2、根据reviewId跟新review表的付款状态
									 * */
								    String s = "";
									String sql = "select review_id from review_detail d where d.myKey = 'visaBorrowMoneyBatchNo' and myValue = ?";
									List<Map<String, Integer>> list = visaDao.findBySql(sql, Map.class, arr[0]);
									for (Map<String, Integer> m : list) {
										s += m.get("review_id") + ",";
									}
									if (StringUtils.isNotBlank(s)) {
										s = s.substring(0,s.length()-1);
										orderService.batchConfirmOrCancelPay(s, type, status,arr[1], rate, amount,date_confirmPayDate);
									}
								} else {
									/**
									 * 更新除环球行旧批量借款付款之外的付款（包括旧成本、退款、返佣付款）的付款状态
									 *
									 * */
									orderService.batchConfirmOrCancelPay(arr[0], type, status,arr[1], rate, amount,date_confirmPayDate);
								}	
							}else{
								if(String.valueOf(Refund.MONEY_TYPE_BATCHBORROW).equals(type)){
									//环球行新借款审核付款确认或取消
									rs.batchUpdatePayStatus(arr[0], UserUtils.getUser().getId().toString(), new Date(), Integer.valueOf(status));
								}else{
									//新退款、返佣、退款付款确认或取消
									rs.updatePayStatus(UserUtils.getUser().getId().toString(), status, arr[0],null);
								}
							}
						}
					}
				}
			}
			data.put("flag", "ok");
		}catch (Exception e) {
			data.put("flag", "no");
			e.printStackTrace();
		}
		return data;
	}
	
	 /**
	 *  功能:
	 *         生成拼音
	 *  @author xuziqian
	 *  @DateTime 2014-3-26 下午5:08:04
	 *  @param request
	 *  @return
	 */
	@RequestMapping(value ="getPingying")
	@ResponseBody
	public Object getPingying(HttpServletRequest request){
	    String source = request.getParameter("srcname");
	    return ChineseToEnglish.getPingYin(source);
	}
	
	@RequestMapping(value ="showAgentOrderList/{agentId}")
	public String showAgentOrderList(@PathVariable String agentId,
	        @ModelAttribute TravelActivity travelActivity, HttpServletResponse response,
	        Model model,HttpServletRequest request){
		
			String type = request.getParameter("type");
	        Page<Map<Object, Object>> pageOrder = orderService.findAgentOrderListByPayType(Integer.parseInt(agentId),new Page<Map<Object, Object>>(request, response));
	        model.addAttribute("page", pageOrder);
	        List<Map<Object, Object>> listorder = pageOrder.getList();
	        List<Long> listProId = new ArrayList<Long>();
	        for(Map<Object, Object> listin :listorder){
	            listProId.add(Long.parseLong(listin.get("id").toString()));
	        }
	        if(listProId.size()>0){
	            List<Orderpay> orderpayList = orderService.findOrderpayByOrderIds(listProId, 2);
	            for(Map<Object, Object> map : listorder){
	                List<Orderpay> listTempOrderPay = new ArrayList<Orderpay>();
	                for(Orderpay orderpay:orderpayList){
	                    //如果orderpay的订单id  等于  pro的订单id
	                    if(orderpay.getOrderId().intValue()==Integer.parseInt(map.get("id").toString())){
	                        listTempOrderPay.add(orderpay);
	                    }
	                }
	                //支付信息
	                map.put("orderPayList", listTempOrderPay);
	            }
	        }
	        model.addAttribute("agentId", agentId);
	        if(StringUtils.isNotBlank(agentId)){
	            Agentinfo agentInfo = agentinfoService.loadAgentInfoById(Long.parseLong(agentId));
	            model.addAttribute("agentName", agentInfo.getAgentName());
	        }
	        model.addAttribute("listtype", type);
	        model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
			model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
			model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
	        model.addAttribute("trafficModes", DictUtils.getDicMap(Context.TRAFFIC_MODE));
	        model.addAttribute("trafficNames", DictUtils.getValueAndLabelMap("traffic_name",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
	        model.addAttribute("payTypes", DictUtils.getDicMap(Context.PAY_TYPE));
	    return "modules/order/agentorderList";
	}
	
	/**
	 * 订单详情
	 * @param orderId
	 * @param model
	 * @param request
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping(value ="orderDetail/{orderId}")
	public String orderDetail(@PathVariable Long orderId, Model model, HttpServletRequest request) throws IllegalAccessException, InvocationTargetException {
		ProductOrderCommon product = getOrderInfoCommon(orderId.toString(), model);
		TravelActivity productActivity = travelActivityService.findById(product.getProductId());
		Office productOffice = officeService.findWholeOfficeById(productActivity.getProCompany());
		getOrderPayByOrderId(orderId.toString(), product.getOrderStatus(), model);
		if (product != null && product.getPriceType() != null) {
			model.addAttribute("priceType", product.getPriceType());
		} else {
			model.addAttribute(0);
		}
		model.addAttribute("orderTitle","详情");
		// 20150728 获取预定团队返佣金额
		if(product!=null && StringUtils.isNotBlank(product.getScheduleBackUuid())){
			MoneyAmount mo = moneyAmountService.findOneAmountBySerialNum(product.getScheduleBackUuid());
			
			if(mo!=null){
				//model.addAttribute("ScheduleBack","mo");
				Currency currency = currencyService.findCurrency(Long.valueOf(mo.getCurrencyId()));
				if(currency!=null){
					model.addAttribute("currencyName",currency.getCurrencyName());
					model.addAttribute("currencyMark",currency.getCurrencyMark());
					model.addAttribute("amount",mo.getAmount());
				}
			}
		}
		model.addAttribute("companyUuid", UserUtils.getUser().getCompany().getUuid());
		model.addAttribute("RMB_currencyId", currencyService.getRMBCurrencyByOffice(productOffice.getId()).getId());
	    return "modules/order/orderDetailCommon";
	}
	
	/**
	  * 生成订单前的验证
	  * @param request
	  * @return
	  * xu.wang
	  */
	@ResponseBody
	@RequestMapping(value = "generateOrderValidate",method = RequestMethod.POST)
	public Object generateOrderValidate(HttpServletRequest request) {
		Map<String, Object> result = new  HashMap<String, Object>();
	    String orderId = request.getParameter("orderId");  
	    try{
		    ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
		    List<ReviewNew> reviewNewList = reviewNewService.getOrderReviewList(orderId, "2", "21");
		    if(CollectionUtils.isNotEmpty(reviewNewList)){
		    	int i = 0;
		    	for(ReviewNew review :reviewNewList){
		    		i++;
		    		if(review.getStatus()!=2){
		    			result.put("code", 1);
		    			result.put("msg",productOrder.getOrderNum()+"订单优惠审批中，是否按照原同行价生成订单");
		    			break;
		    		}
		    		if(i==reviewNewList.size()){
		    			result.put("code", 2);
		    		}
		    	}
		    }else{
		    	 result.put("code", 2);
		    }
	    }catch(Exception e ){
	    	 result.put("code", 3);
	    }
	    return result;
	}
	
	/**
	  * 生成订单前的验证
	  * @param request
	  * @return
	  * xu.wang
	  */
	@ResponseBody
	@RequestMapping(value = "generateOrder",method = RequestMethod.POST)
	public Object generateOrder(HttpServletRequest request) {
		Map<String, Object> result = new  HashMap<String, Object>();
	    String orderId = request.getParameter("orderId");  
	    try{
		    ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
		    result.put("code", 1);
		    orderStockService.changeGroupFreeNum(productOrder, null, Context.StockOpType.CREATE);
		    ProductOrderCommon order = productOrderCommonDao.findOne(productOrder.getId());
		    order.setDelFlag("0");
		    productOrderCommonDao.updateObj(order);
	    }catch(Exception e ){
	    	e.printStackTrace();
	    	ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
	    	ProductOrderCommon order = productOrderCommonDao.findOne(productOrder.getId());
			order.setDelFlag("5");
	    	result.put("code", 2);
	    	productOrderCommonDao.updateObj(order);
	    }
	    return result;
	}
	
	/**
	 * 
	 *  修改订单：显示
	 *
	 *  @param model
	 *  @param request
	 *  @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping(value = "getOderInfoById")
	public String getOderInfoById(Model model, HttpServletRequest request) throws IllegalAccessException, InvocationTargetException {
		String orderId = request.getParameter("productorderById");
		getOrderInfoCommon(orderId, model);
		model.addAttribute("orderTitle","修改");
		Office loginOffice = UserUtils.getUser().getCompany();
		model.addAttribute("office", loginOffice);
		model.addAttribute("companyId", UserUtils.getUser().getCompany().getId());
		model.addAttribute("companyUuid", UserUtils.getUser().getCompany().getUuid());
		model.addAttribute("RMB_currencyId", currencyService.getRMBCurrencyId().getId());
		model.addAttribute("update","update"); // 修改符号，该符号存在，允许修改预订团队/个人返佣
	    return "modules/order/modify/manageOrder";
	}
	
	/**
	 * 订单修改和详情公用方法
	 * @param orderId
	 * @param model
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	private ProductOrderCommon getOrderInfoCommon(String orderId, Model model) throws IllegalAccessException, InvocationTargetException {
		ProductOrderCommon productOrder = new ProductOrderCommon();
		String activityKind = "";
		String agentId = "";
        if(StringUtils.isNotBlank(orderId)){
        	productOrder = orderService.getProductorderById(Long.parseLong(orderId));
        	activityKind = productOrder.getOrderStatus().toString();
        	//设置权限
        	model.addAttribute("shiroType", OrderCommonUtil.getStringOrdeType(activityKind));
        	agentId = productOrder.getOrderCompany().toString();

        	// 20150728 获取预定团队返佣金额
    		if(productOrder!=null && StringUtils.isNotBlank(productOrder.getScheduleBackUuid())){
    			MoneyAmount mo = moneyAmountService.findOneAmountBySerialNum(productOrder.getScheduleBackUuid());
    			
    			if(mo!=null){
    				Currency currency = currencyService.findCurrency(Long.valueOf(mo.getCurrencyId()));
    				if(currency!=null){
    					model.addAttribute("currencyName",currency.getCurrencyName());
    					model.addAttribute("currencyMark",currency.getCurrencyMark());
    					model.addAttribute("amount",mo.getAmount());
    				}
    			}
    		}
        }
        getInfoByOrderId(orderId, model, productOrder, activityKind, agentId);
        return productOrder;
	}
	
	/**
	 * 根据订单id查询支付订单
	 * @param orderId
	 * @param model
	 */
	private void getOrderPayByOrderId(String orderId, Integer orderType, Model model) {
		List<Long> idList = Lists.newArrayList();
		idList.add(Long.parseLong(orderId));
		List<Orderpay> orderpayList = orderService.findOrderpayByOrderIds(idList, orderType);
		if (CollectionUtils.isNotEmpty(orderpayList)) {
			for (Orderpay orderPay : orderpayList) {
				if (StringUtils.isNotBlank(orderPay.getMoneySerialNum())) {
					orderService.clearObject(orderPay);
					orderPay.setMoneySerialNum(moneyAmountService.getMoneyStr(orderPay.getMoneySerialNum()));
	            }
			}
		}
		model.addAttribute("orderPayList", orderpayList);
	}
	
	 /**
	  * 订单修改保存
	  * @param model
	  * @param response
	  * @param request
	  * @return
	 * @throws JSONException 
	  */
	@ResponseBody
	@RequestMapping(value = "modSaveOrder",method = RequestMethod.POST)
	public Object modSaveOrder(Model model, HttpServletResponse response,
	        HttpServletRequest request) throws JSONException {
		Map<String, Object> result = new  HashMap<String, Object>();
	    String orderPersonNumChilds = request.getParameter("orderPersonNumChild");
	    String orderPersonNumAdults = request.getParameter("orderPersonNumAdult");
	    String orderPersonNumSpecials = request.getParameter("orderPersonNumSpecial");
	    String productOrderId = request.getParameter("productOrderId");
	    
	    // 26需求，记录订单修改操作日志
	    Map<String, String> modifyMap = new HashMap<>();
	    ProductOrderCommon productOrder =  orderService.getProductorderById(Long.parseLong(productOrderId));
	    List<OrderContacts> preOrderContactsList = orderContactsDao.findOrderContactsByOrderIdAndOrderType(Long.valueOf(productOrderId), productOrder.getOrderStatus());
	    modifyMap.put("orderPersonNumChild", productOrder.getOrderPersonNumChild().toString());
	    modifyMap.put("orderPersonNumAdult", productOrder.getOrderPersonNumAdult().toString());
	    modifyMap.put("orderPersonNumSpecial", productOrder.getOrderPersonNumSpecial().toString());
	    modifyMap.put("orderCompanyName", productOrder.getOrderCompanyName());
	    modifyMap.put("preSpecialDemand", productOrder.getSpecialDemand() == null ? "" : productOrder.getSpecialDemand().trim());  // 特殊需求
	    modifyMap.put("preSpecialDamandFileIds", productOrder.getSpecialDemandFileIds()); // 特殊需求文件ids
	    modifyMap.put("orderId", productOrderId);
	    modifyMap.put("orderStatus", productOrder.getOrderStatus().toString());  // 订单类型
	    
	    if(StringUtils.isNotBlank(orderPersonNumChilds)) {
	        productOrder.setOrderPersonNumChild(Integer.parseInt(orderPersonNumChilds));
	    }
	    if(StringUtils.isNotBlank(orderPersonNumAdults)) {
	        productOrder.setOrderPersonNumAdult(Integer.parseInt(orderPersonNumAdults));
	    }
	    if(StringUtils.isNotBlank(orderPersonNumSpecials)) {
	    	productOrder.setOrderPersonNumSpecial(Integer.parseInt(orderPersonNumSpecials));
	    }
	    try {
	    	//json组装成订单联系人List
	    	List<OrderContacts> orderCotacts = OrderUtil.getContactsList(request.getParameter("orderContactsJSON"));
//	    	orderService.saveOrder(productOrder, orderCotacts, Integer.parseInt(request.getParameter("orderPersonNum")));
	    	Integer NewRoomNumber = null;
	    	if( productOrder.getOrderStatus() == Context.ORDER_TYPE_CRUISE) {
	    		NewRoomNumber = request.getParameter("roomNumber") != null ? Integer.parseInt(request.getParameter("roomNumber")) : productOrder.getRoomNumber();
	    		modifyMap.put("afterNewRoomNumber", NewRoomNumber == null ? "" : NewRoomNumber.toString());
	    		modifyMap.put("preNewRoomNumber", productOrder.getRoomNumber() == null ? "" : productOrder.getRoomNumber().toString());
	    	}
	    	Integer newTotalPersonNum = Integer.parseInt(request.getParameter("orderPersonNum"));
//	    	Integer orgTotalPersonNum = Integer.parseInt(request.getParameter("orgTotalPersonNum"));
//	    	productOrder.setOrderPersonNum(orgTotalPersonNum);//由于修改订单时保存游客会更新orderPersonNum,但是如果没有保存游客，则订单的orderPersonNum会不准确.先恢复到修改前的人数。
	    	productOrder.setOrderCompany(Long.parseLong(request.getParameter("orderCompany")));
	    	if(StringUtils.isBlank(request.getParameter("orderCompanyName"))){
	    		productOrder.setOrderCompanyName(agentinfoService.getAgentNameById(Long.parseLong(request.getParameter("orderCompany"))));
	    	}else{
	    		productOrder.setOrderCompanyName(request.getParameter("orderCompanyName"));
	    	}
	    	modifyMap.put("afterOrderCompanyName", productOrder.getOrderCompanyName());
	    	//修改订单时，保存订单人数、金额、定金、修改余位切位（C333）
	    	Map<String, Object> resultMap = orderService.saveOrderMoneyAndPosition(productOrder, newTotalPersonNum, NewRoomNumber == null? 0 : NewRoomNumber, orderCotacts);
	    	if (!"0".equals(resultMap.get("Code"))) {
	    		result.put("errorMsg", resultMap.get("Message"));
			}
		} catch (Exception e) {
			logger.error("JSON转换出错");
		}
	    orderServiceForSaveAndModify.modSaveOrder(result, request, false);
    	try {
    		// 26需求
			logOrderService.saveLogSingleGroupOrder4Agent(modifyMap, preOrderContactsList, request);
		} catch (Exception e) {
			logger.error("订单修改日志记录出错!");
		}
	    return result;
	}
	
	
	/**
	 * 设置model  通过orderId获取该订单的信息：订单修改和详情会用到此方法
	 * @param productOrderId
	 * @param model
	 * @param productOrder
	 * @param activityKind
	 * @param agentId
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
    private void getInfoByOrderId(String productOrderId, Model model, ProductOrderCommon productOrder, String activityKind, String agentId) throws IllegalAccessException, InvocationTargetException {
    	// 获取产品所属批发商
    	TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		GroupHandle groupHandle = groupHandleService.findByOrderId(productOrder.getId());
		model.addAttribute("groupHandleId", groupHandle == null ? null : groupHandle.getId());
		setControlModel(model, product);
		
		List<String> seriNumList = new ArrayList<String>();
		seriNumList.add(productOrder.getSettlementAdultPrice());//成人价
		seriNumList.add(productOrder.getSettlementcChildPrice());//儿童价
		seriNumList.add(productOrder.getSettlementSpecialPrice());//特殊价
		seriNumList.add(productOrder.getSingleDiff());//单房差
		seriNumList.add(productOrder.getPayDeposit());//下订单时收的订金
		setCurrency(model, productOrder, productGroup, activityKind, seriNumList);

		//后台计算同行价（成人价*人数+儿童价*人数+特殊价*人数） added by zhenxing.yan
		calculateSumPrice(model,productOrder);

		//查询正常游客和已成功转团游客
		List<Integer> delFlag = Lists.newArrayList();
		delFlag.add(Context.TRAVELER_DELFLAG_NORMAL);
		delFlag.add(Context.TRAVELER_DELFLAG_EXIT);
		delFlag.add(Context.TRAVELER_DELFLAG_EXITED);
		delFlag.add(Context.TRAVELER_DELFLAG_TURNROUND);
		delFlag.add(Context.TRAVELER_DELFLAG_TURNROUNDED);
		List<Traveler> travelerList = travelerService.findTravelerByOrderIdAndOrderType(Long.parseLong(productOrderId), productOrder.getOrderStatus(), delFlag);
		if (StringUtils.isNotBlank(productOrder.getTotalMoney())) {
			//根据游客查询金额币种id和数组
			getMoneyIdAndPrice(productOrder, travelerList, model);
			orderService.clearObject(productOrder);
			String totalMoneySerilNum=productOrder.getTotalMoney();
			productOrder.setTotalMoney(moneyAmountService.getMoneyStr(totalMoneySerilNum));

			//设置币种名称总结算价
			if (Context.PRICE_TYPE_QUJ == productOrder.getPriceType() && Context.ORDER_TYPE_SP == productOrder.getOrderStatus()) {
				model.addAttribute("travelerSumClearPrice", moneyAmountService.getMoneyNameStr4Quauq(totalMoneySerilNum, productOrder.getQuauqServiceCharge()));
			} else {
				model.addAttribute("travelerSumClearPrice", moneyAmountService.getMoneyNameStr(totalMoneySerilNum));
			}
		}
		
		List<HashMap<String, Object>> travelerMapList = Lists.newArrayList();
		HashMap<String, Object> travelerMap = null;
		List<Costchange> allCostChangeList = Lists.newArrayList();
		for (Traveler traveler : travelerList) {
			
			travelerMap = new HashMap<String, Object>();
			
			//转团成功后查询新团期信息
			if (Context.TRAVELER_DELFLAG_TURNROUNDED == traveler.getDelFlag()) {
				getChangeGroupInfo(productOrder, traveler, travelerMap);
			}
			
			List<Costchange> costChangeList = orderService.findCostchangeByTravelerId(traveler.getId());
			// 如果已退团或已转团，则不计算其他费用
			if (Context.TRAVELER_DELFLAG_TURNROUNDED != traveler.getDelFlag() && Context.TRAVELER_DELFLAG_EXITED != traveler.getDelFlag()) {
				allCostChangeList.addAll(costChangeList);
			}
			List<TravelerVisa> visaList = travelerVisaService.findVisaListByPid(traveler.getId());
			if(null != traveler.getIntermodalType() && traveler.getIntermodalType() == 1){
				if(traveler.getIntermodalId() != null){
					IntermodalStrategy intermodal = intermodalStrategyService.getOne(traveler.getIntermodalId());
					travelerMap.put("intermodal", intermodal);
				}
			}
			List<MoneyAmount> moneyList = moneyAmountService.findAmountBySerialNum(traveler.getPayPriceSerialNum());
			JSONArray payPriceArr = new JSONArray();
			for(MoneyAmount money:moneyList){
				JSONObject moneyJson = new JSONObject();
				moneyJson.put("currencyId", money.getCurrencyId());
				moneyJson.put("price", money.getAmount());
				payPriceArr.put(moneyJson);
			}
			travelerMap.put("payPriceJson", payPriceArr);
			travelerMap.put("traveler", traveler);
			travelerMap.put("costChangeList", costChangeList);
		    boolean isAllowModifyDiscount = true;  //是否允许手动修改优惠
		    List<ReviewNew> reviewNewList = reviewServiceNew.getOrderReviewList(productOrder.getId().toString(), productOrder.getOrderStatus().toString(), Context.REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE.toString());
		    if (CollectionUtils.isNotEmpty(reviewNewList)) {
		    	Set<String> travelerIdsSet = new HashSet<>(); 
		    	for (ReviewNew reviewNew : reviewNewList) {
		    		// 只要订单有一条优惠审批是在处理中的，就不允许修改
					if (reviewNew.getStatus() == ReviewConstant.REVIEW_STATUS_PROCESSING) {
						isAllowModifyDiscount = false;
						break;
					}
					// 获取订单中被审批通过的所有游客
					if (reviewNew.getStatus() == ReviewConstant.REVIEW_STATUS_PASSED) {
						String[] tempStr = null;
						if (StringUtils.isNotBlank(reviewNew.getTravellerId())) {
							tempStr = reviewNew.getTravellerId().split(",");
							travelerIdsSet.addAll(new HashSet<>(Arrays.asList(tempStr)));
						}
					}
				}
		    	// “通过的游客集合”中包含的游客不允许修改
		    	if (travelerIdsSet.contains(traveler.getId().toString())) {
		    		isAllowModifyDiscount = false;
				}
			}
		    traveler.setIsAllowModifyDiscount(isAllowModifyDiscount);
		    
			//某游客的总优惠金额 = 输入优惠金额 + 累计审批通过优惠金额（只有当优惠审批通过之后才算）
		    BigDecimal totalDiscountPrice = BigDecimal.ZERO;
			BigDecimal fixeDiscountPrice = traveler.getFixedDiscountPrice() == null ? BigDecimal.ZERO : traveler.getFixedDiscountPrice();  //输入优惠价 
			BigDecimal reviewedDiscountPrice = traveler.getReviewedDiscountPrice() == null ? BigDecimal.ZERO : traveler.getReviewedDiscountPrice();  //累计审批通过优惠金额
			totalDiscountPrice = fixeDiscountPrice.add(reviewedDiscountPrice);
			travelerMap.put("totalDiscountPrice", totalDiscountPrice.toString());
			//同行结算价
			List<MoneyAmount> costmoneyList = moneyAmountService.findAmountBySerialNum(traveler.getCostPriceSerialNum());  //同行价
			BigDecimal settleClearPrice;
			if (CollectionUtils.isEmpty(costmoneyList)) {
				settleClearPrice = BigDecimal.ZERO;
			} else {
				settleClearPrice = costmoneyList.get(0).getAmount();
			}
			travelerMap.put("settleClearPrice", settleClearPrice.subtract(totalDiscountPrice).toString());
			//查询返佣费用信息   add by zhangcl 2015年3月6日
			MoneyAmount rebatesMoney = moneyAmountService.findOneAmountBySerialNum(traveler.getRebatesMoneySerialNum());
			travelerMap.put("rebatesMoney", rebatesMoney);
			if(rebatesMoney != null && rebatesMoney.getCurrencyId() != null){
				Currency currency = currencyService.findCurrency(rebatesMoney.getCurrencyId().longValue());
				travelerMap.put("rebatesCurrency", currency);	//设置返佣币种的详情信息
			}
			
			//查询成本价信息   add by zhangcl 2015年3月6日
			List<MoneyAmount> costMoneyList = moneyAmountService.findAmountBySerialNum(traveler.getCostPriceSerialNum());
			JSONArray costPriceArr = new JSONArray();
			for(MoneyAmount money:costMoneyList){
				JSONObject moneyJson = new JSONObject();
				moneyJson.put("currencyId", money.getCurrencyId());
				moneyJson.put("price", money.getAmount());
				costPriceArr.put(moneyJson);
			}
			travelerMap.put("costPriceJson", costPriceArr);
			
			
			for (TravelerVisa tv: visaList){
				if (tv.getApplyCountry()!=null&&CountryUtils.getCountryId(tv.getApplyCountry().getName()) != null) {
					Long countryId = CountryUtils.getCountryId(tv.getApplyCountry().getName()).getId();
					tv.setSysCountryId(countryId);
					List<VisaProducts> visaProductList = visaProductsService.findVisaProductsByCountryId(countryId.intValue());
					Map<String,String> manorMap = new HashMap<String, String>();
				    List<Dict> manorList = Lists.newArrayList();
				    List<Dict> visaTypeList = Lists.newArrayList();
				    for (VisaProducts vp : visaProductList){
						String manorKey = vp.getCollarZoning();
						Dict manorDict = dictService.findByValueAndType(manorKey, Context.FROM_AREA);
						if (!manorMap.containsKey(manorKey)  && manorDict != null) {
							manorMap.put(manorKey, manorDict.getLabel());
							manorList.add(manorDict);
						}
				    }
				    tv.setManorList(manorList);
				    if (manorList.size() > 0) {
				    	String manorId =  tv.getManorId() != null ? tv.getManorId().toString() : "";
				    	if (StringUtils.isBlank(manorId)) {
				    		manorId =  manorList.get(0).getValue();
				    	}
				    	List<VisaProducts> vpList = visaProductsService.findVisaProductsByCountryIdAndManor(countryId.intValue(), manorId);
						for (VisaProducts vp: vpList) {
							Dict dict = dictService.findByValueAndType(vp.getVisaType().toString(), Context.DICT_TYPE_NEW_VISATYPE);
							if (!visaTypeList.contains(dict)) {
								visaTypeList.add(dict);
							}
						}
				    }
					tv.setVisaTypeList(visaTypeList);
				}
			}
			
			StringBuffer docIds = new StringBuffer("");
			List<TravelerFile> passportfile = travelerFileDao.findBySrcTravelerIdAndFileTypeAndDelFlag(traveler.getId(), TravelerFile.PASSPORTS_TYPE,"0");
			List<TravelerFile> idcardfrontfile = travelerFileDao.findBySrcTravelerIdAndFileTypeAndDelFlag(traveler.getId(), TravelerFile.IDCARD_FRONT_TYPE,"0");
			List<TravelerFile> entryformfile = travelerFileDao.findBySrcTravelerIdAndFileTypeAndDelFlag(traveler.getId(), TravelerFile.ENTRY_FORM_TYPE,"0");
			List<TravelerFile> housefile = travelerFileDao.findBySrcTravelerIdAndFileTypeAndDelFlag(traveler.getId(), TravelerFile.HOUSE_TYPE,"0");
			List<TravelerFile> photofile = travelerFileDao.findBySrcTravelerIdAndFileTypeAndDelFlag(traveler.getId(), TravelerFile.PHOTO_TYPE,"0");
			List<TravelerFile> idcardbackfile = travelerFileDao.findBySrcTravelerIdAndFileTypeAndDelFlag(traveler.getId(), TravelerFile.IDCARD_BACK_TYPE,"0");
			List<TravelerFile> residencefile = travelerFileDao.findBySrcTravelerIdAndFileTypeAndDelFlag(traveler.getId(), TravelerFile.RESIDENCE_TYPE,"0");
			List<TravelerFile> otherfile = travelerFileDao.findBySrcTravelerIdAndFileTypeAndDelFlag(traveler.getId(), TravelerFile.OTHER_TYPE,"0");
			List<TravelerFile> visaannexfile = travelerFileDao.findBySrcTravelerIdAndFileTypeAndDelFlag(traveler.getId(), TravelerFile.VISA_TYPE,"0");
			travelerMap.put("visaList", visaList);
			travelerMap.put("passportfile", passportfile);
			travelerMap.put("passportfileNameStr", getFileNameStr(passportfile));
			travelerMap.put("idcardfrontfile", idcardfrontfile);
			travelerMap.put("idcardfrontfileNameStr", getFileNameStr(idcardfrontfile));
			travelerMap.put("entryformfile", entryformfile);
			travelerMap.put("entryformfileNameStr", getFileNameStr(entryformfile));
			travelerMap.put("housefile", housefile);
			travelerMap.put("housefileNameStr", getFileNameStr(housefile));
			travelerMap.put("photofile", photofile);
			travelerMap.put("photofileNameStr", getFileNameStr(photofile));
			travelerMap.put("idcardbackfile", idcardbackfile);
			travelerMap.put("idcardbackfileNameStr", getFileNameStr(idcardbackfile));
			travelerMap.put("residencefile", residencefile);
			travelerMap.put("residencefileNameStr", getFileNameStr(residencefile));
			travelerMap.put("otherfile", otherfile);
			travelerMap.put("otherfileNameStr", getFileNameStr(otherfile));
			travelerMap.put("visaannexfile", visaannexfile);
			travelerMap.put("visaannexfileNameStr", getFileNameStr(visaannexfile));
			if (passportfile != null && passportfile.size() > 0) {
				for (TravelerFile travelerFile : passportfile) {
					if(travelerFile.getDocInfo()!=null){
						docIds.append(travelerFile.getDocInfo().getId() + ",");
					}
				}
			}
			if (photofile != null && photofile.size() > 0) {
				for (TravelerFile travelerFile : photofile) {
					if(travelerFile.getDocInfo()!=null){
						docIds.append(travelerFile.getDocInfo().getId() + ",");
					}
				}
			}
			if (idcardfrontfile != null && idcardfrontfile.size() > 0) {
				for (TravelerFile travelerFile : idcardfrontfile) {
					if(travelerFile.getDocInfo()!=null){
						docIds.append(travelerFile.getDocInfo().getId() + ",");
					}
				}
			}
			if (idcardbackfile != null && idcardbackfile.size() > 0) {
				for (TravelerFile travelerFile : idcardbackfile) {
					if(travelerFile.getDocInfo()!=null){
						docIds.append(travelerFile.getDocInfo().getId() + ",");
					}
				}
			}
			if (entryformfile != null && entryformfile.size() > 0) {
				for (TravelerFile travelerFile : entryformfile) {
					if(travelerFile.getDocInfo()!=null){
						docIds.append(travelerFile.getDocInfo().getId() + ",");
					}
				}
			}
			if (housefile != null && housefile.size() > 0) {
				for (TravelerFile travelerFile : housefile) {
					if(travelerFile.getDocInfo()!=null){
						docIds.append(travelerFile.getDocInfo().getId() + ",");
					}
				}
			}
			if (residencefile != null && residencefile.size() > 0) {
				for (TravelerFile travelerFile : residencefile) {
					if(travelerFile.getDocInfo()!=null){
						docIds.append(travelerFile.getDocInfo().getId() + ",");
					}
				}
			}
			if (otherfile != null && otherfile.size() > 0) {
				for (TravelerFile travelerFile : otherfile) {
					if(travelerFile.getDocInfo()!=null){
						docIds.append(travelerFile.getDocInfo().getId() + ",");
					}
				}
			}
			if (visaannexfile != null && visaannexfile.size() > 0) {
				for (TravelerFile travelerFile : visaannexfile) {
					if(travelerFile.getDocInfo()!=null){
						docIds.append(travelerFile.getDocInfo().getId() + ",");
					}
				}
			}
			if (StringUtils.isNotBlank(docIds)) {
				travelerMap.put("docIds", docIds.substring(0, docIds.length()-1));
			}
			
			//查询签证子订单游客：一个单团游客可能有多个签证，所以会有多个签证对应游客
			List<Traveler> visaTravelerList = travelerService.findTravelersByMainTravelerId(traveler.getId());
			if (CollectionUtils.isNotEmpty(visaTravelerList)) {
				List<String> serialNumList = Lists.newArrayList();
				String visaOrderIdStr = "";
				for (int i=0;i<visaTravelerList.size();i++) {
					Traveler visaTraveler = visaTravelerList.get(i);
					Visa visa = visaDao.findByTravelerId(visaTraveler.getId());
					if (visa != null && visa.getTotalDeposit() != null) {
						serialNumList.add(visa.getTotalDeposit());
					}
					if (i != visaTravelerList.size()-1) {
						visaOrderIdStr += visaTraveler.getOrderId() + ",";
					} else {
						visaOrderIdStr += visaTraveler.getOrderId();
					}
				}
				travelerMap.put("visaOrderIdStr", visaOrderIdStr);
				
				if (CollectionUtils.isNotEmpty(serialNumList)) {
					travelerMap.put("visaDeposit", moneyAmountService.getMoneyStr(serialNumList));
				}
			}
			
			
			travelerMapList.add(travelerMap);
		}
		getCostChangeStr(allCostChangeList, model);
		model.addAttribute("travelers", travelerMapList);
		List<OrderContacts> orderContacts = orderContactsService.findOrderContactsByOrderIdAndOrderType(productOrder.getId(), productOrder.getOrderStatus());
		if (CollectionUtils.isEmpty(orderContacts)) {
			orderContacts.add(new OrderContacts());
		}
		model.addAttribute("orderContactsSrc", orderContacts);  //保存的是最原始的联系人---供非签约渠道订单修改使用
		if (CollectionUtils.isNotEmpty(orderContacts)) {
			model.addAttribute("orderContacts", orderContacts);
			net.sf.json.JSONArray orderContactsListJsonArray = net.sf.json.JSONArray.fromObject(orderContacts);
	        model.addAttribute("orderContactsListJsonArray", orderContactsListJsonArray.toString());
		}
		//渠道商的联系地址
		List<SupplierContacts> contacts = supplierContactsService.findAllContactsByAgentInfo(Long.parseLong(agentId));  //取出渠道商所有联系人（包括第一联系人）
		String address = agentinfoService.getAddressStrById(Long.parseLong(agentId));
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
		model.addAttribute("contactsJsonStr", contactsJsonStr);
		model.addAttribute("contactArray", contactArray);
				
		//订单是否允许添加多个渠道联系人信息
		Integer allowAddAgentInfo = UserUtils.getUser().getCompany().getIsAllowAddAgentInfo();
		model.addAttribute("allowAddAgentInfo", allowAddAgentInfo);
		//订单是否允许渠道联系人信息输入和修改
		Integer allowModifyAgentInfo = UserUtils.getUser().getCompany().getIsAllowModifyAgentInfo();
		model.addAttribute("allowModifyAgentInfo", allowModifyAgentInfo);
		Agentinfo agentinfo = agentinfoService.loadAgentInfoById(Long.parseLong(agentId));
		if(agentinfo != null){
			agentinfo.setAgentAddressFull(address);
		}
		model.addAttribute("agentinfo", agentinfo);
        ActivityGroupReserve groupReserve = StockService.findGroupReserve(productOrder.getOrderCompany(), product.getId(), productGroup.getId());
        model.addAttribute("groupReserve",groupReserve);
        
        model.addAttribute("orderTypeStr", OrderCommonUtil.getStringOrdeType(activityKind));
	    model.addAttribute("product",product);
	    model.addAttribute("productGroup",productGroup);
	    model.addAttribute("productorder",productOrder);
	    model.addAttribute("agentId",agentId);
	    model.addAttribute("activityKind", activityKind);
	    List<Agentinfo>  agentinfoList = agentinfoService.findAgents4Ordermod(productOrder);
	    model.addAttribute("agentinfoList", agentinfoList);	//add by zhangcl
	    //add by yang.jiang 团期优惠额度
	    Map<String, Object> discountMap = groupService.getDiscountMapByGroupId(productGroup.getId().toString());
	    model.addAttribute("discountMap", discountMap);
	    // 发票
	    List<Orderinvoice> invoices = orderinvoiceService.findCreatedInvoiceByOrder(Integer.parseInt(productOrder.getId().toString()), productOrder.getOrderStatus());
	    model.addAttribute("invoices", invoices);
	    // 收据
	    List<OrderReceipt> receipts = orderreceiptService.findCreatedReceiptByOrder(Integer.parseInt(productOrder.getId().toString()), productOrder.getOrderStatus());
	    model.addAttribute("receipts", receipts);
	    //获取订单所在团期已占位最高儿童、特殊人群人数
	    Map<String, Object>  counts = activityGroupService.countOrderChildAndSpecialNum(productGroup.getId(),productOrderId);
	    int orderPersonNumChild = 0;
	    int orderPersonNumSpecial = 0;
		if(counts != null){
			orderPersonNumSpecial = counts.get("orderPersonNumSpecial")==null?0:new Integer(counts.get("orderPersonNumSpecial").toString());
			orderPersonNumChild = counts.get("orderPersonNumChild")==null?0:new Integer(counts.get("orderPersonNumChild").toString());
		}
		if(productGroup.getMaxChildrenCount() != null){
			orderPersonNumChild = productGroup.getMaxChildrenCount()-orderPersonNumChild;
			model.addAttribute("currentChildrenCount", orderPersonNumChild);
		}else{
			model.addAttribute("currentChildrenCount", null);
		}
		if(productGroup.getMaxPeopleCount() != null){
			orderPersonNumSpecial = productGroup.getMaxPeopleCount() - orderPersonNumSpecial;
			model.addAttribute("currentPeopleCount", orderPersonNumSpecial);
		}else{
			model.addAttribute("currentPeopleCount", null);
		}
		
        if (null != productOrder.getPreOrderId()) {
        	T1PreOrder t1PreOrder = preOrderService.findById(productOrder.getPreOrderId());
        	model.addAttribute("differenceFlag", "1");
        	model.addAttribute("preOrderId", t1PreOrder.getId());
        	
        	List<MoneyAmount> differenceMoneyList = moneyAmountService.findBySerialNum(productOrder.getDifferenceMoney());
        	model.addAttribute("differenceMoney", CollectionUtils.isNotEmpty(differenceMoneyList) ? differenceMoneyList.get(0).getAmount() : 0);
        }
    }
    
    /**
     * 获取文件名字字符串
     * @param passportfile
     * @return
     */
    private String getFileNameStr(List<TravelerFile> passportfile) {
    	String nameStr = "";
    	for (TravelerFile travelerFile : passportfile) {			
    		nameStr += travelerFile.getFileName() + ";";
		}
		return nameStr;
	}

	/**
     * 依据agentInfoId 获取所有联系人信息
     */
    @ResponseBody
    @RequestMapping(value = "getAllAgentContactInfo")
    public String getAllAgentContactInfo(String agentId, Model model, HttpServletResponse response){
    	List<SupplierContacts> contacts = supplierContactsService.findAllContactsByAgentInfo(Long.parseLong(agentId));  //取出渠道商所有联系人（包括第一联系人）
    	//由于数据库中数据在存储的时候并没有存储渠道商或者地接社ID，在此处添加一下（不反写入数据库，仅用在前端等地方）
    	for (SupplierContacts supplierContacts : contacts) {
    		supplierContacts.setSupplierId(Long.parseLong(agentId));
    		supplierContacts.setAgentAddressFull(agentinfoService.getAddressStrById(Long.parseLong(agentId)));
		}
    	org.json.JSONArray contactArrayNew = supplierContactsService.contacts2JsonArray(contacts);
    	return contactArrayNew.toString();
    }
    
    /**
     * 查询转团后团期信息
     * @param order
     * @param traveler
     * @param travelerMap
     */
    private void getChangeGroupInfo(ProductOrderCommon order, Traveler traveler, HashMap<String, Object> travelerMap) {
    	List<Review> listReview = reviewService.findReview(order.getOrderStatus(), Context.REVIEW_FLOWTYPE_TRANSFER_GROUP, 
    			order.getId().toString(), traveler.getId(), true);
    	if (CollectionUtils.isNotEmpty(listReview)) {
    		Map<String,String> map = reviewService.findReview(listReview.get(0).getId());
    		String groupCode = map.get(TransFerGroup.KEY_NEW_GROUPCODE);
    		if (StringUtils.isNotBlank(groupCode)) {
    			ActivityGroup group = activityGroupService.findByGroupCode(groupCode);
    			if (group != null) {
					travelerMap.put("changeGroupCode", group.getGroupCode());
					travelerMap.put("changeProductType", OrderCommonUtil.getChineseOrderType(group.getTravelActivity().getActivityKind().toString()));
					travelerMap.put("changeProductName", group.getTravelActivity().getAcitivityName());
					travelerMap.put("changeCreateName", UserUtils.getUser(listReview.get(0).getCreateBy()).getName());
				}
    		}
    	} else {
//    		List<ReviewNew> reviewNewList = reviewNewService.getReviewList(order.getId().toString(), traveler.getId().toString(), order.getOrderStatus().toString(), Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.toString());
    		// 本订单所有的转团审批
    		List<ReviewNew> reviewNewList = reviewNewService.getOrderReviewList(order.getId().toString(), order.getOrderStatus().toString(), Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.toString());
    		if (CollectionUtils.isNotEmpty(reviewNewList)) {
    			boolean isInThisReview = false;
    			for (ReviewNew reviewNew : reviewNewList) {
    				if (reviewNew.getStatus().intValue() == ReviewConstant.REVIEW_STATUS_PASSED) {
    					// 比对本审批中参与转团的游客中，是否存在该游客
    					String travelerIdString = reviewNew.getTravellerId();
    					if (StringUtils.isNotBlank(travelerIdString) && travelerIdString.split(",") != null && travelerIdString.split(",").length > 0) {
    						String [] travelerIdArray = travelerIdString.split(",");
    						for (int i = 0; i < travelerIdArray.length; i++) {
    							if (travelerIdArray[i].toString().equals(traveler.getId().toString())) {
    								isInThisReview = true;
    								break;
    							}
    						}
    						// 如果游客在本审批游客列表中
    						if (isInThisReview) {						
    							String groupId = reviewNew.getExtend3();
    							if (StringUtils.isNoneBlank(groupId)) {
    								ActivityGroup group = activityGroupService.findById(Long.parseLong(groupId));
    								if (group != null) {
    									travelerMap.put("changeGroupCode", group.getGroupCode());
    									travelerMap.put("changeProductType", OrderCommonUtil.getChineseOrderType(group.getTravelActivity().getActivityKind().toString()));
    									travelerMap.put("changeProductName", group.getTravelActivity().getAcitivityName());
    									travelerMap.put("changeCreateName", UserUtils.getUser(reviewNew.getCreateBy()).getName());
    								}
    							}
    							break;
    						}
    					}
					}
				}
    		}
    	}
    }
    
    /**
     * 计算其他费用总和
     * @param allCostChangeList
     * @param model
     */
    private void getCostChangeStr(List<Costchange> allCostChangeList, Model model) {
    	if (CollectionUtils.isNotEmpty(allCostChangeList)) {
			String costChangeStr = "";
			BigDecimal costChangePrice = null;
			List<String> currencyMarkList = Lists.newArrayList();
			List<String> currencyMarkTempList = Lists.newArrayList();
			List<BigDecimal> currencyPriceList = Lists.newArrayList();
			for (Costchange cost : allCostChangeList) {
				if (cost.getCostSum() != null && cost.getPriceCurrency() != null) {
					currencyMarkList.add(cost.getPriceCurrency().getCurrencyMark());
					currencyPriceList.add(cost.getCostSum());
				}
			}
			if (CollectionUtils.isNotEmpty(currencyMarkList)) {
				for (int i=0;i<currencyMarkList.size();i++) {
					String mark = currencyMarkList.get(i);
					if (i==0 || !currencyMarkTempList.contains(mark)) {
						costChangePrice = currencyPriceList.get(i);
						for (int j=i+1;j<currencyMarkList.size();j++) {
							String markTwo = currencyMarkList.get(j);
							if (mark.equals(markTwo)) {
								costChangePrice = costChangePrice.add(currencyPriceList.get(j));
							}
						}
						costChangeStr += mark + costChangePrice.toString() + " ";
					}
					currencyMarkTempList.add(mark);
				}
			}
			model.addAttribute("costChangeStr", costChangeStr);
		}
    }
    
    /**
     * 获取游客结算价总和与订单金额差价
     * @param travelerList
     * @param model
     * @return
     */
    private void getMoneyIdAndPrice(ProductOrderCommon order, List<Traveler> travelerList, Model model) {
    	List<String> serialNumList = Lists.newArrayList();
    	if (CollectionUtils.isNotEmpty(travelerList)) {
    		for (Traveler traveler : travelerList) {
    			serialNumList.add(traveler.getPayPriceSerialNum());
    		}
    	}
    }
    
	/**
	 * 加载订单维护页面 / 当前看到的是 报名第一页
	 * @param model
	 * @param request
	 * @return
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 */
	@RequestMapping(value ="showforModify")
	public String showforModify(Model model, HttpServletRequest request) throws IllegalAccessException, InvocationTargetException {
		
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		String salerId = request.getParameter("salerId");
		String placeHolderType = request.getParameter("placeHolderType");
	    String productId = request.getParameter("productId");
	    String productGroupId = request.getParameter("productGroupId");
	    String agentId = request.getParameter("agentId");
	    String payMode = request.getParameter("payMode");
	    String payStatus = request.getParameter("payStatus");
	    String activityKind = request.getParameter("activityKind");
	    String groupCoverId = request.getParameter("coverId");
	    String groupCoverNum = request.getParameter("groupCoverNum");
	    String agentSourceType = request.getParameter("agentSourceType");
	    String preOrderId = request.getParameter("preOrderId");
	    
	    //添加权限校验
	    model.addAttribute("shiroType", OrderCommonUtil.getStringOrdeType(activityKind));
	   
        ProductOrderCommon pro = new ProductOrderCommon();
        pro.setProductId(Long.parseLong(productId));
        pro.setProductGroupId(Long.parseLong(productGroupId));
        pro.setPayMode(payMode);
        pro.setPayStatus(Integer.parseInt(payStatus));
        pro.setPlaceHolderType(Integer.parseInt(placeHolderType));
        
        if (StringUtils.isNotBlank(preOrderId)) {
        	T1PreOrder t1PreOrder = preOrderService.findById(Long.parseLong(preOrderId));
        	pro.setPreOrderId(t1PreOrder.getId());
        	model.addAttribute("differenceFlag", "1");
        	model.addAttribute("preOrderId", preOrderId);
        	
        	// 如果差额小于0则默认为0
        	List<T1MoneyAmount> differenceMoneyList = t1MoneyAmountService.findBySerialNum(t1PreOrder.getDifferenceMoney());
        	String differenceMoney = null;
        	if (CollectionUtils.isNotEmpty(differenceMoneyList)) {
        		if (differenceMoneyList.get(0).getAmount().doubleValue() < 0) {
        			differenceMoney = "0";
        		} else {
        			differenceMoney = differenceMoneyList.get(0).getAmount().toString();
        		}
        	} else {
        		differenceMoney = "0";
        	}
        	model.addAttribute("differenceMoney", differenceMoney);
        	model.addAttribute("allNum", null == t1PreOrder.getOrderPersonNum() ? 0 : t1PreOrder.getOrderPersonNum());
        	model.addAttribute("adultNum", null == t1PreOrder.getOrderPersonNumAdult() ? 0 : t1PreOrder.getOrderPersonNumAdult());
        	model.addAttribute("childNum", null == t1PreOrder.getOrderPersonNumChild() ? 0 : t1PreOrder.getOrderPersonNumChild());
        	model.addAttribute("specialNum", null == t1PreOrder.getOrderPersonNumSpecial() ? 0 : t1PreOrder.getOrderPersonNumSpecial());
        }
        
        // 判断订单渠道类型和支付类型
        Agentinfo agentinfo = null;
        if ("-1".equals(agentId)) {
        	agentinfo = new Agentinfo();
        	agentinfo.setId(-1L);
        	agentinfo.setAgentName("非签约渠道");
		} else {
			agentinfo = agentinfoService.loadAgentInfoById(Long.parseLong(agentId));
		}
        if ("2".equals(activityKind) && "2".equals(agentSourceType)) {
        	pro.setPriceType(2);
            
            String agentType = agentinfo.getAgentType();
            if (StringUtils.isNotBlank(agentType)) {
            	pro.setAgentType(Integer.parseInt(agentType));
            } else {
            	pro.setAgentType(-1);
            }
        }
        
        model.addAttribute("companyId", companyId);
        if (StringUtils.isNotBlank(salerId)) {
        	pro.setSalerId(Integer.parseInt(salerId));
        }
	    setModelWhenGetInfoByOrderId(model, pro, activityKind, agentinfo);
	    String companyUuid = UserUtils.getUser().getCompany().getUuid();
	    model.addAttribute("companyUuid", companyUuid);
	    // 加载报名信息填写页面，取用的是 批发商此时的服务费率
	    model.addAttribute("chargeRate", UserUtils.getUser().getCompany().getChargeRate());

		if(StringUtils.isNotBlank(productGroupId)) {
			ActivityGroup activityGroup = activityGroupService.findById(Long.parseLong(productGroupId));
			String priceJson = activityGroup.getPriceJson();
			model.addAttribute("priceJson", priceJson);
			model.addAttribute("activityGroup", activityGroup);
			if("2".equals(activityKind)) {
				
				// 如果此团期已下架则，返回错误页面
				Integer isT1 = activityGroup.getIsT1();
				Integer priceType = pro.getPriceType();
				if ((null != priceType && priceType == 2) && (null == isT1 || isT1 == 0)) {
					model.addAttribute(Context.ERROR_MESSAGE_KEY, "此团期已在旅游交易系统下架");
					return Context.ERROR_PAGE;
				}
				
				String currencyType = activityGroup.getCurrencyType();//38,37,37,39,37,37,37,37

				if(StringUtils.isNotBlank(currencyType)) {
					String[] currencyTypes = currencyType.split(",");
					if(currencyTypes.length > 0) {
						model.addAttribute("adultDiscountCurrencyId", currencyTypes[currencyTypes.length - 3]);
						model.addAttribute("childDiscountCurrencyId", currencyTypes[currencyTypes.length - 2]);
						model.addAttribute("specialDiscountCurrencyId", currencyTypes[currencyTypes.length - 1]);
					}
				}

				BigDecimal adultDiscountPrice = activityGroup.getAdultDiscountPrice();
				BigDecimal childDiscountPrice = activityGroup.getChildDiscountPrice();
				BigDecimal specialDiscountPrice = activityGroup.getSpecialDiscountPrice();
				model.addAttribute("adultDiscountPrice", adultDiscountPrice);
				model.addAttribute("childDiscountPrice", childDiscountPrice);
				model.addAttribute("specialDiscountPrice", specialDiscountPrice);
				model.addAttribute("groupCoverId", groupCoverId);
				model.addAttribute("groupCoverNum", groupCoverNum);
			}
			model.addAttribute("agentSourceType", agentSourceType);
		}
		// 26需求，订单中添加游客时，区分是新生成的订单还是修改订单
		model.addAttribute("newOrderFlag", "newOrder");


	    return getReturnUrl("manageOrder", activityKind);
	}

	/**
	 *  功能:订单保存第一步
	 *	保存信息仅包括，订单基本信息及预定联系人
	 *	游客信息和特殊需求在第二部保存
	 */
	@ResponseBody
	@RequestMapping(value = "firstSave",method = RequestMethod.POST)
	public Map<String, Object> firstSave(Model model, HttpServletRequest request) {
		
		Map<String, Object> result = Maps.newHashMap();
		
		try {
			result = orderServiceForSaveAndModify.firstSave(model, request);
		} catch (Exception e) {
			logger.error("数据格式错误", e);
			result.put("result", false);
			if (e.getMessage() != null) {
				result.put("msg", e.getMessage());
			} else {
				result.put("msg", "数据格式错误，请重试。");
			}
		}

		return result;
	}
	
	/**
	 * 功能:订单最后一步保存
	 * @author yakun.bai
	 * @Date 2016-9-5
	 */
	@ResponseBody
	@RequestMapping(value = "lastSave")
	public Object lastSave(Model model, HttpServletRequest request) throws JSONException {
		Map<String, Object> result = new  HashMap<String, Object>();
		orderServiceForSaveAndModify.modSaveOrder(result, request, true);
		return result;
	}


	/**
	* 保存订单并发起优惠审批 for 109 优加
	*/
	@ResponseBody
	@RequestMapping(value = "saveOrderAndApplyReview")
	public Map<String, String> saveOrderAndApplyReview(HttpServletRequest request) {
		Map<String, String> returnResult = new  HashMap<String, String>();
		Map<String, String> parameters = getParameters(request);
		String groupCode = null;
		try {
			groupCode = orderServiceForSaveAndModify.handleOrderAndReview(parameters, request);
		} catch(Exception e) {
			e.printStackTrace();
			returnResult.put("result", "2");
			returnResult.put("msg", e.getMessage());
			return returnResult;
		}
		returnResult.put("result", "1");
		returnResult.put("groupCode", groupCode);
		return returnResult;
	}

	 /**
	  * 获取参数
      */
	private Map<String,String> getParameters(HttpServletRequest request) {
		Map<String, String> parameters = new HashMap<>();
		String productOrderId = request.getParameter("productOrderId");
		String currencyId = request.getParameter("currencyId");
		String currencyPrice = request.getParameter("currencyPrice");
		String costCurrencyId = request.getParameter("costCurrencyId");
		String costCurrencyPrice = request.getParameter("costCurrencyPrice");
		String specialDemand = request.getParameter("specialDemand");
		String rebatesCurrency = request.getParameter("rebatesCurrency");
		String rebatesMoney = request.getParameter("rebatesMoney");
		String orderCompany = request.getParameter("orderCompany");
		String orderCompanyName = request.getParameter("orderCompanyName");
		String groupHandleId = request.getParameter("groupHandleId");
		String applyTravelerIds = request.getParameter("applyTravelerIds");

		parameters.put("productOrderId", productOrderId);
		parameters.put("currencyId", currencyId);
		parameters.put("currencyPrice", currencyPrice);
		parameters.put("specialDemand", specialDemand);
		parameters.put("rebatesCurrency", rebatesCurrency);
		parameters.put("rebatesMoney", rebatesMoney);
		parameters.put("orderCompany", orderCompany);
		parameters.put("orderCompanyName", orderCompanyName);
		parameters.put("groupHandleId", groupHandleId);
		parameters.put("applyTravelerIds", applyTravelerIds);
		parameters.put("costCurrencyId", costCurrencyId);
		parameters.put("costCurrencyPrice", costCurrencyPrice);

		return parameters;
	}

	/**
	 * 
	 *  功能: 根据出境游或者国内游的城市列表
	 *
	 *  @author xiaoyang.tao
	 *  @DateTime 2014-10-22 下午4:25:47
	 *  @param targetAreaIdList
	 *  @return
	 */
	private List<TravelerVisaInfo> getTargetForeignCountry(List<Area> targetAreaIdList, Model model){
		List<TravelerVisaInfo> targetList = Lists.newArrayList();
		StringBuilder sbOriginal = new StringBuilder();
		String strOriginalOther = "";
		StringBuilder sbCopyOriginal = new StringBuilder();
		String strCopyOriginalOther = "";
		List<Long> countryIds = Lists.newArrayList();
		for(Area area: targetAreaIdList){
			//外国区域祖类都为"出境游"，编号为100000，国内区域祖类为"国内游"，编号为200000
			if(area.getParentIds().indexOf(Context.FREE_TRAVEL_FOREIGN) > 0){
				TravelerVisaInfo tv = null;
				Long countryId = null;
				Map<String,String> manorMap = new HashMap<String, String>();
			    List<Dict> manorList =Lists.newArrayList(); 
			    List<Dict> visaTypeList =Lists.newArrayList();
			    //判断国家字典中是否存在目的地国家
			    if (("3".equals(area.getType()) || "4".equals(area.getType())) && area.getParent() != null) {
			    	area = area.getParent();
			    	if (countryIds.contains(area.getId())) {
			    		continue;
			    	} else {
			    		countryIds.add(area.getId());
			    	}
			    } else if ("2".equals(area.getType())) {
			    	countryIds.add(area.getId());
			    } else if ("1".equals(area.getType())) {
			    	continue;
			    }
				if(CountryUtils.getCountryId(area.getName()) == null){
				/** 不是国家的地区给忽略 */
//					tv = new TravelerVisaInfo();
//					tv.setApplyCountryId(area.getId());
//					tv.setApplyCountryName(area.getName());
//				    tv.setManorList(manorList);
//					tv.setVisaTypeList(visaTypeList);
//					targetList.add(tv);
				}else{
					countryId = CountryUtils.getCountryId(area.getName()).getId();
					List<VisaProducts> visaProductList = visaProductsService.findVisaProductsByCountryId(countryId.intValue());
					if(visaProductList != null && visaProductList.size() > 0){
						tv = new TravelerVisaInfo();
						tv.setApplyCountryId(area.getId());
						tv.setSysCountryId(countryId);
						tv.setApplyCountryName(area.getName());
						for(VisaProducts vp: visaProductList){
							String manorKey = vp.getCollarZoning();
							Dict manorDict = dictService.findByValueAndType(manorKey, Context.FROM_AREA);
							if(!manorMap.containsKey(manorKey) && manorDict != null){
								manorMap.put(manorKey, manorDict.getLabel());
								manorList.add(manorDict);
							} 
							if(StringUtils.isNotBlank(vp.getOriginal_Project_Type())){
								if(sbOriginal.length() == 0){
									sbOriginal.append(vp.getOriginal_Project_Type());
								}else{
									sbOriginal.append("," + vp.getOriginal_Project_Type());
								}
							}
							if(StringUtils.isNotBlank(vp.getOriginal_Project_Name())){
								if(StringUtils.isNotBlank(strOriginalOther)){
									if(StringUtils.isNotBlank(vp.getOriginal_Project_Name())){
										if(!strOriginalOther.contains(vp.getOriginal_Project_Name())){
											strOriginalOther += "、" + vp.getOriginal_Project_Name();
										}
									}
								}else{
									strOriginalOther += vp.getOriginal_Project_Name();
								}
							}
							if(StringUtils.isNotBlank(vp.getCopy_Project_Type())){
								if(sbCopyOriginal.length() == 0){
									sbCopyOriginal.append(vp.getCopy_Project_Type());
								}else{
									sbCopyOriginal.append("," + vp.getCopy_Project_Type());
								}
							}
							if(StringUtils.isNotBlank(vp.getCopy_Project_Name())){
								if(StringUtils.isNotBlank(strCopyOriginalOther)){
									if(StringUtils.isNotBlank(vp.getCopy_Project_Name())){
										if(!strCopyOriginalOther.contains(vp.getCopy_Project_Name())){
											strCopyOriginalOther  +=  "、" + vp.getCopy_Project_Name();
										}
									}
								}else{
									strCopyOriginalOther  += vp.getCopy_Project_Name();
								}
							}
						}
						tv.setManorList(manorList);
						List<VisaProducts> vpList = visaProductsService.findVisaProductsByCountryIdAndManor(countryId.intValue(), manorList.get(0).getValue());
						for(VisaProducts vp: vpList){
							Dict dict = dictService.findByValueAndType(vp.getVisaType().toString(), Context.DICT_TYPE_NEW_VISATYPE);
							if (!visaTypeList.contains(dict)) {
								visaTypeList.add(dict);
							}
						}
						tv.setVisaTypeList(visaTypeList);
						targetList.add(tv);
					}else{
						tv = new TravelerVisaInfo();
						tv.setApplyCountryId(area.getId());
						tv.setApplyCountryName(area.getName());
					    tv.setManorList(manorList);
						tv.setVisaTypeList(visaTypeList);
						targetList.add(tv);
					}
				}
			}
		}
		String strOriginal = "";
		if(sbOriginal.length() > 0){
			String[] arrOriginal = sbOriginal.toString().split(",");
			for(String original : arrOriginal){
				if(StringUtils.isBlank(strOriginal)){
					strOriginal += original;
				}else{
					if(!strOriginal.contains(original) && !"2".equals(original)){
						strOriginal += "," + original;
					}
				}
			}
		}
		if(StringUtils.isNotBlank(strOriginal)){
			if(StringUtils.isNotBlank(strOriginalOther)){
				strOriginal = strOriginal.replace("0", "护照").replace("1", "身份证")
						.replace("3", "电子照片").replace("4", "申请表格").replace("5", "户口本").replace("6", "房产证")
						.replace(",", "、") + "、" + strOriginalOther;
			}else{
				strOriginal = strOriginal.replace("0", "护照").replace("1", "身份证")
						.replace("3", "电子照片").replace("4", "申请表格").replace("5", "户口本").replace("6", "房产证")
						.replace(",", "、");
			}
		}
		else{
			if(StringUtils.isNotBlank(strOriginalOther)){
				strOriginal = strOriginalOther;
			}
		}
		model.addAttribute("original", strOriginal);
		String strCopyOriginal = "";
		if(sbCopyOriginal.length() > 0){
			String[] arrCopyOriginal = sbCopyOriginal.toString().split(",");
			for(String copyOriginal : arrCopyOriginal){
				if(StringUtils.isBlank(strCopyOriginal)){
					strCopyOriginal += copyOriginal;
				}else{
					if(!strCopyOriginal.contains(copyOriginal) && !"2".equals(copyOriginal)){
						strCopyOriginal += "," + copyOriginal;
					}
				}
			}
		}
		if(StringUtils.isNotBlank(strCopyOriginal)){
			if(StringUtils.isNotBlank(strCopyOriginalOther)){
				strCopyOriginal = strCopyOriginal.replace("0", "户口本").replace("1", "房产证")
						.replace("3", "护照").replace("4", "身份证").replace("5", "电子照片").replace("6", "申请表格")
						.replace(",", "、") + "、" + strCopyOriginalOther;
			}else{
				strCopyOriginal = strCopyOriginal.replace("0", "户口本").replace("1", "房产证")
						.replace("3", "护照").replace("4", "身份证").replace("5", "电子照片").replace("6", "申请表格")
						.replace(",", "、");
			}
		}else{
			if(StringUtils.isNotBlank(strCopyOriginalOther)){
				strCopyOriginal = strCopyOriginalOther;
			}
		}
		model.addAttribute("copyoriginal", strCopyOriginal);
		return targetList;
	}
	
	/**
	 * 查看团签
	 * @author  chenry
	 */
	@RequestMapping(value = "viewGroupVisa")
	public String viewGroupVisa(Model model,HttpServletRequest request,String id){
		model.addAttribute("id", id);
		
		return "modules/order/viewGroupVisa";
	}
	/**
	 * 查看退团列表
	 * @author  chenry
	 */
	@RequestMapping(value = "viewExitGroup")
	public String viewExitGroup(Model model,HttpServletRequest request,HttpServletResponse response,Long orderId,Integer productType){
						
		List<Map<String, String>> pageOrder = orderService.getExitGroupReviewInfo(productType,Context.REVIEW_FLOWTYPE_EXIT_GROUP,orderId);
		ProductOrderCommon order = productOrderCommonDao.findOne(orderId);
		
		if(null!=order&&order.getId()>0){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			model.addAttribute("orderTime", sdf.format(order.getOrderTime()));
		}
		model.addAttribute("page", pageOrder);
		model.addAttribute("orderId", orderId);
		model.addAttribute("productType",productType);
		model.addAttribute("flowType",Context.REVIEW_FLOWTYPE_EXIT_GROUP);
		return "modules/order/viewExitGroup";
	}
	/**
	 * 申请退团
	 * @author  chenry
	 * update date 2014-12-30 
	 */
	@RequestMapping(value = "applyExitGroup")
	public String applyExitGroup(Model model,HttpServletRequest request,HttpServletResponse response,Long id,String productType,String flowType){
		List<Map<Object, Object>> travelerList = orderService.getTravelerByOrderId(id,Integer.parseInt(productType));
		model.addAttribute("orderId", id);
		model.addAttribute("flowType", Context.REVIEW_FLOWTYPE_EXIT_GROUP);
		model.addAttribute("productType",productType);
		model.addAttribute("travelerList", travelerList);
		ProductOrderCommon productOrder = orderService.getProductorderById(id);
		model.addAttribute("productOrder", productOrder);
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		model.addAttribute("product", product);
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		return "modules/order/applyExitGroup";
	}
	/**
	 * 查看退团审核详情
	 * @author ruyi.chen
	 * add Date 2014 12-15
	 */
	@RequestMapping(value = "viewApplyExitGroupInfo")
	public String viewApplyExitGroupInfo(Model model,HttpServletRequest request,HttpServletResponse response,Integer productType,Long rid,Long orderId) {
		Map<String, Object> travelerList = orderService.getExitGroupReviewInfoById(rid);
		model.addAttribute("hashMap",travelerList);
		model.addAttribute("productType",productType);
		ProductOrderCommon productOrder = orderService.getProductorderById(orderId);
		model.addAttribute("productOrder", productOrder);
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		model.addAttribute("product", product);
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		return "modules/order/viewExitGroupInfo";
	}
	/**
	 * 查看退款审核详情
	 * @author ruyi.chen
	 * add Date 2014 12-15
	 */
	@RequestMapping(value = "viewApplyRefundInfo")
	public String viewApplyRefundInfo(Model model,HttpServletRequest request,HttpServletResponse response,Integer productType,Long rid,Long orderId){
		Map<String, Object> travelerList=orderService.getReviewRefundInfo(rid);
			model.addAttribute("hashMap",travelerList);
			model.addAttribute("productType",productType);
			ProductOrderCommon productOrder = orderService.getProductorderById(orderId);
			model.addAttribute("productOrder", productOrder);
			TravelActivity product = travelActivityService.findById(productOrder.getProductId());
			model.addAttribute("product", product);
			ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
			model.addAttribute("productGroup",productGroup);
			model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
			model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		return "modules/order/viewRefundInfo";
	}
	/**
	 * 查看海岛游退款审核详情
	 * @author ruyi.chen
	 * add Date 2014 12-15
	 */
	@RequestMapping(value = "viewIslandApplyRefundInfo")
	public String viewIslandApplyRefundInfo(Model model,HttpServletRequest request,HttpServletResponse response,Integer productType,Long rid,Long orderId,String orderUuid){
		Map<String, Object> travelerList=orderService.getIslandReviewRefundInfo(rid);
		model.addAttribute("hashMap",travelerList);
		model.addAttribute("productType",productType);
		IslandOrder islandOrder = islandOrderService.getByUuid(orderUuid);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(islandOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(islandOrder.getPayMode()));
		
		//TODO 添加海岛游相关产品、団期、订单信息
		islandOrderService.getOrderBaseInfo(orderUuid, model);
		return "modules/order/viewIslandRefundInfo";
	}
	/**
	 * 查看酒店退款审核详情
	 * @author ruyi.chen
	 * add Date 2014 12-15
	 */
	@RequestMapping(value = "viewHotelApplyRefundInfo")
	public String viewHotelApplyRefundInfo(Model model,HttpServletRequest request,HttpServletResponse response,Integer productType,Long rid,Long orderId,String orderUuid){
		Map<String, Object> travelerList=orderService.getHotelReviewRefundInfo(rid);
		model.addAttribute("hashMap",travelerList);
		model.addAttribute("productType",productType);
		HotelOrder hotelOrder = hotelOrderService.getByUuid(orderUuid);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(hotelOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(hotelOrder.getPayMode()));
			
		//TODO 添加海岛游相关产品、団期、订单信息
		//订单基本信息
		hotelOrderService.getOrderBaseInfo(orderUuid, model);
		return "modules/order/viewHotelRefundInfo";
	}
	/**
	 * 查看退款列表
	 * @author  chenry
	 * add Date 2014-11-05
	 */
	@RequestMapping(value = "viewGroupRefund")
	public String viewGroupRefund(Model model,HttpServletRequest request,HttpServletResponse response,Long orderId,Integer productType,Integer flowType){
		StringBuffer sbf=new StringBuffer();
		LinkedHashMap<String, List<RefundBean>> pageOrder=orderService.getGroupRefundReviewInfo(productType, flowType, orderId,sbf);
		
		List<Currency> currencyList = currencyService
				.findCurrencyList(UserUtils.getUser().getCompany().getId());
		int sign = 0;

		// 定义订单应收币种集合  取订单达帐金额所有的币种
		ProductOrderCommon productOrder = orderService.getProductorderById(orderId);
		List<MoneyAmount> totalMoneyList = moneyAmountService.findAmountBySerialNum(productOrder.getAccountedMoney());
		if(null == totalMoneyList || 0 >= totalMoneyList.size()){
			sign = 1;
		}
//		//判断批次审核是否结束
//		if(sbf.length() > 0){
//			sign = 1;
//		}
		Map<String,Object> rMap = orderReviewService.getOrderReviewMutexInfo(orderId, productType.toString(), Context.REVIEW_FLOWTYPE_REFUND, null);
		if("3".equals(rMap.get(Context.MUTEX_CODE).toString())){
			sign = 1;
		}
		model.addAttribute("sign", sign);
		model.addAttribute("page", pageOrder);
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("flowType", flowType);
		model.addAttribute("productType",productType);		
		return "modules/order/viewGroupRefund";
	}
	/**
	 * 查看海岛游退款列表
	 * @author  chenry
	 * add Date 2015-06-18
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@RequestMapping(value = "viewIslandGroupRefund")
	public String viewIslandGroupRefund(Model model,HttpServletRequest request,HttpServletResponse response,Long orderId,Integer productType,Integer flowType,String orderUuid) throws IllegalArgumentException, IllegalAccessException{
//		StringBuffer sbf=new StringBuffer();
		List<Map<String, String>> reviewMapList = reviewService.findReviewListMap(productType,flowType, orderId+"", false);
		Collections.reverse(reviewMapList);
		List<RefundBean> reviewList = getRefundBeanList(reviewMapList);
		List<Currency> currencyList = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
		int sign = 0;

		// 定义订单应收币种集合  取订单达帐金额所有的币种
		IslandOrder islandOrder = islandOrderService.getByUuid(orderUuid);
		List<IslandMoneyAmount> totalMoneyList = islandMoneyAmountService.getMoneyAmonutBySerialNum(islandOrder.getAccountedMoney());
		if(null == totalMoneyList || 0 >= totalMoneyList.size()){
			sign = 1;
		}
		//暂时去掉海岛游退款流程互斥判断
//		Map<String,Object> rMap = orderReviewService.getOrderReviewMutexInfo(orderId, productType.toString(), Context.REVIEW_FLOWTYPE_REFUND, null);
//		if("3".equals(rMap.get(Context.MUTEX_CODE).toString())){
//			sign = 1;
//		}
		List<IslandTraveler> travelerList = islandTravelerDao.findTravelerByOrderUuid(orderUuid, false);
		List<Map<String,Object>> rMap = Lists.newArrayList();
		Map<String, String> t4priceMap = Maps.newHashMap();//不同游客累计金额
		String sumRefundPrice = ""; //累计退款金额
		for (RefundBean borr : reviewList) {
			Map <String,Object> map = Maps.newHashMap();
			map.put("currencyName", borr.getCurrencyName());
			map.put("currencyMark", borr.getCurrencyMark());
			map.put("refundPrice", borr.getRefundPrice());
			map.put("refundName", borr.getRefundName());
			map.put("reviewId", borr.getReviewId());
			map.put("applyDate", borr.getApplyDate());
			map.put("remark", borr.getRemark());
			//获取累计退款金额
			String travelerId = borr.getTravelerId();
			if (t4priceMap.containsKey(travelerId)) {
				sumRefundPrice = t4priceMap.get(travelerId);
			} else {
				sumRefundPrice = "";
			}
			sumRefundPrice = islandMoneyAmountService.addOrSubtract(borr.getCurrencyMark() + " " + borr.getRefundPrice(), sumRefundPrice, true);
			t4priceMap.put(borr.getTravelerId(), sumRefundPrice);
			map.put("sumRefundPrice", sumRefundPrice);
			for (IslandTraveler t : travelerList) {
				if (null != borr.getTravelerId() && t.getId().toString().equals(borr.getTravelerId())) {
					map.put("totalMoney", t.getPayPriceSerialNum());
					map.put("personType", t.getPersonType());
					map.put("travelerName", t.getName());
					map.put("travelerId", t.getId());
					map.put("spaceLevel", t.getSpaceLevel());
					map.put("travelerUuid", t.getUuid());
				}
				
			}
			rMap.add(map);
		}
		List<String> travlerIds = Lists.newArrayList();
		for (IslandTraveler t : travelerList) {
			travlerIds.add(t.getUuid());
		}
		String refundTotal = OrderCommonUtil.getRefundPayMoneyTravelListByOrderType(orderUuid, travlerIds, 12+"");
		model.addAttribute("refundTotal", refundTotal);
		model.addAttribute("sign", sign);
		//对结果集进行翻转（前台按创建时间倒叙排列）
		Collections.reverse(rMap);
		model.addAttribute("bAList", rMap);
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("flowType", flowType);
		model.addAttribute("productType",productType);
		model.addAttribute("orderUuid",orderUuid);
		return "modules/order/viewIslandGroupRefund";
	}
	/**
	 * 获取退款bean列表
	 * 
	 * @param reviewMapList
	 * @return
	 */
	private List<RefundBean> getRefundBeanList(List<Map<String, String>> reviewMapList) {
		List<RefundBean> aList = new ArrayList<RefundBean>();
		if (null == reviewMapList || reviewMapList.isEmpty()) {
			return aList;
		}
		for (Map<String, String> map : reviewMapList) {
			aList.add(new RefundBean(map));
		}
		return aList;
	}
	/**
	 * 查看酒店退款列表
	 * @author  chenry
	 * add Date 2015-06-18
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@RequestMapping(value = "viewHotelGroupRefund")
	public String viewHotelGroupRefund(Model model,HttpServletRequest request,HttpServletResponse response,Long orderId,Integer productType,Integer flowType,String orderUuid) throws IllegalArgumentException, IllegalAccessException{
//		StringBuffer sbf=new StringBuffer();
		List<Map<String, String>> reviewMapList = reviewService.findReviewListMap(productType,flowType, orderId+"", false);
		Collections.reverse(reviewMapList);
		List<RefundBean> reviewList = getRefundBeanList(reviewMapList);
		List<Currency> currencyList = currencyService
				.findCurrencyList(UserUtils.getUser().getCompany().getId());
		int sign = 0;

		// 定义订单应收币种集合  取订单达帐金额所有的币种
		HotelOrder hotelOrder = hotelOrderService.getByUuid(orderUuid);
		List<HotelMoneyAmount> totalMoneyList = hotelMoneyAmountService.getMoneyAmonutBySerialNum(hotelOrder.getAccountedMoney());
		if(null == totalMoneyList || 0 >= totalMoneyList.size()){
			sign = 1;
		}
		//暂时去掉海岛游退款流程互斥判断
//		Map<String,Object> rMap = orderReviewService.getOrderReviewMutexInfo(orderId, productType.toString(), Context.REVIEW_FLOWTYPE_REFUND, null);
//		if("3".equals(rMap.get(Context.MUTEX_CODE).toString())){
//			sign = 1;
//		}
		List<HotelTraveler> travelerList = hotelTravelerDao.findTravelerByOrderUuid(orderUuid, false);
		List<Map<String,Object>> rMap = Lists.newArrayList();
		Map<String, String> t4priceMap = Maps.newHashMap();//不同游客累计金额
		String sumRefundPrice = ""; //累计退款金额
		for (RefundBean borr : reviewList) {
			Map <String,Object> map = Maps.newHashMap();
			map.put("currencyName", borr.getCurrencyName());
			map.put("currencyMark", borr.getCurrencyMark());
			map.put("refundPrice", borr.getRefundPrice());
			map.put("refundName", borr.getRefundName());
			map.put("reviewId", borr.getReviewId());
			map.put("applyDate", borr.getApplyDate());
			map.put("remark", borr.getRemark());
			//获取累计退款金额
			String travelerId = borr.getTravelerId();
			if (t4priceMap.containsKey(travelerId)) {
				sumRefundPrice = t4priceMap.get(travelerId);
			} else {
				sumRefundPrice = "";
			}
			sumRefundPrice = islandMoneyAmountService.addOrSubtract(borr.getCurrencyMark() + " " + borr.getRefundPrice(), sumRefundPrice, true);
			t4priceMap.put(borr.getTravelerId(), sumRefundPrice);
			map.put("sumRefundPrice", sumRefundPrice);
			for (HotelTraveler t : travelerList) {
				if (null != borr.getTravelerId() && t.getId().toString().equals(borr.getTravelerId())) {
					map.put("totalMoney", t.getPayPriceSerialNum());
					map.put("personType", t.getPersonType());
					map.put("travelerName", t.getName());
					map.put("travelerId", t.getId());
					map.put("travelerUuid", t.getUuid());
				}
			}
			rMap.add(map);
		}
		List<String> travlerIds = Lists.newArrayList();
		for(HotelTraveler t : travelerList){
			travlerIds.add(t.getUuid());
		}
		String refundTotal = OrderCommonUtil.getRefundPayMoneyTravelListByOrderType(orderUuid, travlerIds, 11+"");
		model.addAttribute("refundTotal", refundTotal);
		model.addAttribute("sign", sign);
		//对结果集进行翻转（前台按创建时间倒叙排列）
		Collections.reverse(rMap);
		model.addAttribute("bAList", rMap);
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("flowType", flowType);
		model.addAttribute("productType",productType);
		model.addAttribute("orderUuid",orderUuid);
		return "modules/order/viewHotelGroupRefund";
	}
	/**
	 * 申请退款
	 * @author  chenry
	 * add Date 2014-11-05
	 */
	@RequestMapping(value = "applyGroupRefund")
	public String applyGroupRefund(Model model,HttpServletRequest request,HttpServletResponse response,Long id,Integer productType){
//		Page<Map<Object, Object>> pageOrder =orderService.findTravelerRecordList(type, new Page<Map<Object, Object>>(request, response));
		List<Map<String, Object>> travelerList = orderService.getRefundTravelerByOrderId(id,productType);
		//添加退款互斥情况验证，若游客或者团队有退款流程或者互斥流程进行中，则不能进行退款申请操作
		List<Map<String,Object>> rList = Lists.newArrayList();
//		if(null != travelerList && 0 < travelerList.size()){
			List<Long> travelerIds = Lists.newArrayList();
			travelerIds.add((long)0);
			if(null != travelerList && 0 < travelerList.size()){
				for(Map<String,Object> m : travelerList){
					travelerIds.add(Long.parseLong(m.get("id").toString()));
				}
			}
			
			Map<String,Object> rMap = orderReviewService.getOrderReviewMutexInfo(id, productType.toString(), Context.REVIEW_FLOWTYPE_REFUND, travelerIds);
			@SuppressWarnings("unchecked")
			Map<String,Object> resultMap =(Map<String,Object>) rMap.get(Context.MUTEX_RESULT_lIST);
			if(null != travelerList && 0 < travelerList.size()){
				for(Map<String,Object> m : travelerList){
					String resultStr = resultMap.get(m.get("id").toString()).toString();
					if("0".equals(resultStr.split("/")[0])){
						rList.add(m);
					}
				}
			}
			
			if("0".equals(resultMap.get("0").toString().split("/")[0])){
				model.addAttribute("groupRefundSign", 0);
			}
//		}
		///////////////////////////////////////////流程互斥部分结束
		// 定义订单应收币种集合  取订单达帐金额所有的币种
		ProductOrderCommon productOrder = orderService.getProductorderById(id);
		List<MoneyAmount> totalMoneyList = moneyAmountService.findAmountBySerialNum(productOrder.getAccountedMoney());
		List<Currency> currencyList = Lists.newArrayList();
			for(MoneyAmount moneyAmount: totalMoneyList){
				Currency currency = currencyService.findCurrency(moneyAmount.getCurrencyId().longValue());
				currencyList.add(currency);
			}

		model.addAttribute("orderId", id);
		model.addAttribute("productType", productType);
		model.addAttribute("travelerList", rList);
		model.addAttribute("currencyList", currencyList);
		
		model.addAttribute("productOrder", productOrder);
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		model.addAttribute("product", product);
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		return "modules/order/applyGroupRefund";
	}
	/**
	 * 海岛游申请退款
	 * @author  chenry
	 * add Date 2014-11-05
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@RequestMapping(value = "applyIslandGroupRefund")
	public String applyIslandGroupRefund(Model model,HttpServletRequest request,HttpServletResponse response,Long id,Integer productType,String orderUuid) throws IllegalArgumentException, IllegalAccessException{
		///////////////////////////////////////////流程互斥部分结束
		// 定义订单应收币种集合  取订单达帐金额所有的币种
		IslandOrder islandOrder = islandOrderService.getByUuid(orderUuid);
		List<IslandMoneyAmount> totalMoneyList = islandMoneyAmountService.getMoneyAmonutBySerialNum(islandOrder.getAccountedMoney());
		List<Currency> currencyList = Lists.newArrayList();
			for(IslandMoneyAmount moneyAmount: totalMoneyList){
				Currency currency = currencyService.findCurrency(moneyAmount.getCurrencyId().longValue());
				currencyList.add(currency);
			}
			
		List<IslandTraveler> travelerList = islandTravelerDao.findTravelerByOrderUuid(islandOrder.getUuid(), false);
		List<Map<String, String>> reviewMapList = reviewService.findReviewListMap(productType,1, id+"", false);
		Collections.reverse(reviewMapList);
		List<RefundBean> reviewList = getRefundBeanList(reviewMapList);
		List<Map<String,Object>> rMap = Lists.newArrayList();
		Map<String, String> t4priceMap = Maps.newHashMap();//不同游客累计金额
		String sumRefundPrice = ""; //累计退款金额
		for (RefundBean borr :reviewList) {
			Map <String,Object> map = Maps.newHashMap();
			map.put("currencyName", borr.getCurrencyName());
			map.put("currencyMark", borr.getCurrencyMark());
			map.put("refundPrice", borr.getRefundPrice());
			map.put("refundName", borr.getRefundName());
			map.put("reviewId", borr.getReviewId());
			map.put("applyDate", borr.getApplyDate());
			map.put("remark", borr.getRemark());
			//获取累计退款金额
			String travelerId = borr.getTravelerId();
			if (t4priceMap.containsKey(travelerId)) {
				sumRefundPrice = t4priceMap.get(travelerId);
			} else {
				sumRefundPrice = "";
			}
			sumRefundPrice = islandMoneyAmountService.addOrSubtract(borr.getCurrencyMark() + " " + borr.getRefundPrice(), sumRefundPrice, true);
			t4priceMap.put(borr.getTravelerId(), sumRefundPrice);
			map.put("sumRefundPrice", sumRefundPrice);
			for (IslandTraveler t : travelerList) {
				if (null != borr.getTravelerId() && t.getId().toString().equals(borr.getTravelerId())) {
					map.put("totalMoney", t.getPayPriceSerialNum());
					map.put("personType", t.getPersonType());
					map.put("travelerName", t.getName());
					map.put("travelerId", t.getId());
					map.put("travelerUuid", t.getUuid());
					map.put("spaceLevel", t.getSpaceLevel());
				}
			}
			rMap.add(map);
		}
		model.addAttribute("productOrder", islandOrder);
		model.addAttribute("orderId", id);
		model.addAttribute("productType", productType);
		model.addAttribute("travelerList", travelerList);
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("orderUuid", orderUuid);
		//对结果集进行翻转（前台按创建时间倒叙排列）
		Collections.reverse(rMap);
		model.addAttribute("bAList", rMap);
		List<String> travlerIds = Lists.newArrayList();
		List<String> travelPriceList = Lists.newArrayList();
		for(IslandTraveler t : travelerList){
			travlerIds.add(t.getUuid());
			travelPriceList.add(t.getPayPriceSerialNum());
		}
		String refundTotal = OrderCommonUtil.getRefundPayMoneyTravelListByOrderType(orderUuid, travlerIds, 12+"");
		model.addAttribute("refundTotal", refundTotal);
		String totalTravelerPrice = OrderCommonUtil.getIslandMoneyAmountBySerialNums(travelPriceList,2);
		model.addAttribute("totalTravelerPrice",totalTravelerPrice);
		//订单基本信息
		islandOrderService.getOrderBaseInfo(orderUuid, model);
		return "modules/order/applyIslandGroupRefund";
	}
	@ResponseBody
	@RequestMapping(value = "currencyJsonIsland")
	public Map<String, Object> getCurrencyJson(Model model,HttpServletRequest request,HttpServletResponse response,String orderUuid) {
		Map<String, Object> map = new HashMap<String, Object>();
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		List<Currency> currencyList = currencyService
				.findCurrencyList(companyId);
		IslandOrder islandOrder = islandOrderService.getByUuid(orderUuid);
		List<IslandMoneyAmount> totalMoneyList = islandMoneyAmountService.getMoneyAmonutBySerialNum(islandOrder.getAccountedMoney());
		for(IslandMoneyAmount moneyAmount: totalMoneyList){
			Currency currency = currencyService.findCurrency(moneyAmount.getCurrencyId().longValue());
			currencyList.add(currency);
		}
		map.put("currencyList", currencyList);
		return map;
	}
	/**
	 * 酒店申请退款
	 * @author  chenry
	 * add Date 2015-06-18
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@RequestMapping(value = "applyHotelGroupRefund")
	public String applyHotelGroupRefund(Model model,HttpServletRequest request,HttpServletResponse response,Long id,Integer productType,String orderUuid) throws IllegalArgumentException, IllegalAccessException{
		// 定义订单应收币种集合  取订单达帐金额所有的币种
		HotelOrder hotelOrder = hotelOrderService.getByUuid(orderUuid);
		List<HotelMoneyAmount> totalMoneyList = hotelMoneyAmountService.getMoneyAmonutBySerialNum(hotelOrder.getAccountedMoney());
		List<Currency> currencyList = Lists.newArrayList();
			for(HotelMoneyAmount moneyAmount: totalMoneyList){
				Currency currency = currencyService.findCurrency(moneyAmount.getCurrencyId().longValue());
				currencyList.add(currency);
			}
			List<HotelTraveler> travelerList = hotelTravelerDao.findTravelerByOrderUuid(hotelOrder.getUuid(), true);
			List<Map<String, String>> reviewMapList = reviewService.findReviewListMap(productType,1, id+"", false);
			Collections.reverse(reviewMapList);
			List<RefundBean> reviewList = getRefundBeanList(reviewMapList);
			List<Map<String,Object>> rMap = Lists.newArrayList();
			Map<String, String> t4priceMap = Maps.newHashMap();//不同游客累计金额
			String sumRefundPrice = ""; //累计退款金额
			for (RefundBean borr : reviewList) {
				Map <String,Object> map = Maps.newHashMap();
				map.put("currencyName", borr.getCurrencyName());
				map.put("currencyMark", borr.getCurrencyMark());
				map.put("refundPrice", borr.getRefundPrice());
				map.put("refundName", borr.getRefundName());
				map.put("reviewId", borr.getReviewId());
				map.put("applyDate", borr.getApplyDate());
				map.put("remark", borr.getRemark());
				//获取累计退款金额
				String travelerId = borr.getTravelerId();
				if (t4priceMap.containsKey(travelerId)) {
					sumRefundPrice = t4priceMap.get(travelerId);
				} else {
					sumRefundPrice = "";
				}
				sumRefundPrice = islandMoneyAmountService.addOrSubtract(borr.getCurrencyMark() + " " + borr.getRefundPrice(), sumRefundPrice, true);
				t4priceMap.put(borr.getTravelerId(), sumRefundPrice);
				map.put("sumRefundPrice", sumRefundPrice);
				for (HotelTraveler t : travelerList) {
					if (null != borr.getTravelerId() && t.getId().toString().equals(borr.getTravelerId())) {
						map.put("totalMoney", t.getPayPriceSerialNum());
						map.put("personType", t.getPersonType());
						map.put("travelerName", t.getName());
						map.put("travelerId", t.getId());
						map.put("travelerUuid", t.getUuid());
					}
				}
				rMap.add(map);
			}
		model.addAttribute("productOrder", hotelOrder);
		model.addAttribute("travelerList", travelerList);
		model.addAttribute("orderId", id);
		model.addAttribute("productType", productType);
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("orderUuid", orderUuid);
		//对结果集进行翻转（前台按创建时间倒叙排列）
		Collections.reverse(rMap);
		model.addAttribute("bAList", rMap);
		List<String> travlerIds = Lists.newArrayList();
		List<String> travelPriceList = Lists.newArrayList();
		for(HotelTraveler t : travelerList){
			travlerIds.add(t.getUuid());
			travelPriceList.add(t.getPayPriceSerialNum());
		}
		String refundTotal = OrderCommonUtil.getRefundPayMoneyTravelListByOrderType(orderUuid, travlerIds, 11+"");
		model.addAttribute("refundTotal", refundTotal);
		String totalTravelerPrice = OrderCommonUtil.getHotelMoneyAmountBySerialNums(travelPriceList,2);
		model.addAttribute("totalTravelerPrice",totalTravelerPrice);
		//订单基本信息
		hotelOrderService.getOrderBaseInfo(orderUuid, model);
		return "modules/order/applyHotelGroupRefund";
	}
	/**
	 * 提交保存退团申请，发起审核流程(验证流程互斥情况，只有当无互斥流程及其他流程性操作时，发起退团审核流程，否则返回错误信息)
	 * @author  chenry
	 * update Date 2015-03-30
	 */
	@ResponseBody
	@RequestMapping(value="saveExitGroupInfo")
	public Map<String,Object> saveExitGroupInfo(Model model,HttpServletRequest request, HttpServletResponse response,String[]travelerName,Long[]travelerId,String[]exitReason,Integer productType,Integer flowType,Long orderId,String[] subtractMoneyArr) {
		Map<String, Object> map = new HashMap<String,Object>();
		Map<String, Object> result = orderReviewService.checkExitGroupInfo(productType.toString(), flowType, orderId, travelerId, travelerName);
		if ("1".equals(result.get("sign").toString())) {
			map.put(Context.RESULT, 1);
			map.put(Context.MESSAGE, "退团流程审核中！请刷新页面重试！");
			return map;
		} else if("2".equals(result.get("sign").toString())) {
			map.put(Context.RESULT, 3);
			map.put(Context.MESSAGE, result.get(Context.MESSAGE));
			return map;
		} else if("0".equals(result.get("sign").toString())) {
			try {
				map = orderService.saveExitGroupReviewInfo(productType, flowType, travelerId,travelerName, exitReason, orderId,subtractMoneyArr);
				map.put(Context.RESULT, 2);
			} catch (Exception e) {
				map.put(Context.RESULT, 0);
				map.put(Context.MESSAGE, e.getMessage());
				return map;
			}
		}
		return map;
	}
	/**
	 * 提交保存退团申请，发起审核流程(无验证互斥情况，先取消该订单所有与退团流程互斥或相关的流程，然后发起退团审核流程)
	 * @author  chenry
	 * add Date 2015-03-30
	 */
	@ResponseBody
	@RequestMapping(value="saveCommonExitGroupInfo")
	public Map<String,Object> saveCommonExitGroupInfo(Model model,HttpServletRequest request, HttpServletResponse response,String[]travelerName,Long[]travelerId,String[]exitReason,Integer productType,Integer flowType,Long orderId,String[] subtractMoneyArr) {
		Map<String, Object> map = new HashMap<String,Object>();
		Map<String, Object> result = orderReviewService.checkExitGroupInfo(productType.toString(), flowType, orderId, travelerId, travelerName);
		if("1".equals(result.get("sign").toString())){
			map.put(Context.RESULT, 1);
			map.put(Context.MESSAGE, "退团流程审核中！请刷新页面重试！");
			return map;
		}else if("2".equals(result.get("sign").toString())){
			try {
				orderReviewService.removeOrderGroupReviewForExitGroup(orderId, productType);
				for(Long tid : travelerId){
					orderReviewService.removeTravelerReviewForExitGroup(orderId, productType, tid);
				}
				
				map = orderService.saveExitGroupReviewInfo(productType, flowType, travelerId,travelerName, exitReason, orderId,subtractMoneyArr);
				map.put(Context.RESULT, 2);
			} catch (Exception e) {
				map.put(Context.RESULT, 0);
				map.put(Context.MESSAGE, e.getMessage());
				return map;
			}
		}else if("0".equals(result.get("sign").toString())){
			try {
				
				map = orderService.saveExitGroupReviewInfo(productType, flowType, travelerId,travelerName, exitReason, orderId,subtractMoneyArr);
				map.put(Context.RESULT, 2);
			} catch (Exception e) {
				map.put(Context.RESULT, 0);
				map.put(Context.MESSAGE, e.getMessage());
				return map;
			}
		}
								
		return map;
	}
	/**
	 * 由于退团申请优先级较高，此方法是检查在保存退团申请之前，当前订单与退团申请的流程互斥情况
	 * @author  chenry
	 * add Date 2015-03-30
	 */
	@ResponseBody
	@RequestMapping(value="checkExitGroupInfo")
	public Map<String,Object> checkExitGroupInfo(Model model,HttpServletRequest request, HttpServletResponse response,String orderType,Integer flowType,Long orderId,Long[]travelerId,String[]travelerName) {
		Map<String, Object> result = orderReviewService.checkExitGroupInfo(orderType, flowType, orderId, travelerId, travelerName);
		return result;
	}
	/**
	 * 提交保存团队退款申请，发起审核流程
	 * @author  chenry
	 * add Date 2014-11-06
	 */
	@SuppressWarnings({ "unchecked", "deprecation" })
	@ResponseBody
	@RequestMapping(value="saveGroupRefundInfo")
	public Map<String,Object> saveGroupRefundInfo(Model model,HttpServletRequest request, HttpServletResponse response,Integer productType,Integer flowType,String[]travelerId,int[]currencyId,String[] currencyName,String[]refund,String[] remark,String[]refundAmount, Long orderId,String refundRecordsStr) {
		
		Map<String, Object> result =new HashMap<String,Object>();
		try {
			List<RefundBean> refundRecords = net.sf.json.JSONArray.toList(
					net.sf.json.JSONArray.fromObject(refundRecordsStr), RefundBean.class);
			/////////////////////////////////////////////////////////////
			//添加退款互斥情况验证，若游客或者团队有退款流程或者互斥流程进行中，则不能进行退款申请操作
			Map<String,String> travelers = Maps.newHashMap();
			List<Long> travelerIds = getRefundBeanIds(refundRecords,travelers);			
			Map<String,Object> rMap = orderReviewService.getOrderReviewMutexInfo(orderId, productType.toString(), Context.REVIEW_FLOWTYPE_REFUND, travelerIds);
			Map<String,Object> resultMap = (Map<String,Object>) rMap.get(Context.MUTEX_RESULT_lIST);
			boolean flag = false;
			StringBuffer sf = new StringBuffer();
			for(Long tid : travelerIds){
				if("1".equals(resultMap.get(tid.toString()).toString().split("/")[0])){
					flag = true;
					sf.append(travelers.get(tid.toString())+" "+resultMap.get(tid.toString()).toString().split("/")[2]+" ");
				}
			}
			if(flag){
				result.put("sign", 0);
				result.put("result", sf);
				return result;
			}
			//流程互斥部分结束
			/////////////////////////////////////////////////////////////
			result = orderService.saveGroupRefundReviewInfo(refundRecords, productType, flowType, orderId);
		} catch (Exception e) {
			result.put("sign", 0);
			result.put("result", e.getMessage());
			return result;
		}
		
		return result;
	}
	/**
		 * 提交保存团队退款申请，发起审核流程
		 * @author  chenry
		 * add Date 2014-11-06
		 */
		@SuppressWarnings({ "unchecked", "deprecation" })
		@ResponseBody
		@RequestMapping(value="saveIslandGroupRefundInfo")
		public Map<String,Object> saveIslandGroupRefundInfo(Model model,HttpServletRequest request, HttpServletResponse response,Integer productType,Integer flowType,String[]travelerId,int[]currencyId,String[] currencyName,String[]refund,String[] remark,String[]refundAmount, Long orderId,String refundRecordsStr) {
			
			Map<String, Object> result =new HashMap<String,Object>();
			try {
				List<RefundBean> refundRecords = net.sf.json.JSONArray.toList(
						net.sf.json.JSONArray.fromObject(refundRecordsStr), RefundBean.class);
				/////////////////////////////////////////////////////////////
				//添加退款互斥情况验证，若游客或者团队有退款流程或者互斥流程进行中，则不能进行退款申请操作
//				Map<String,String> travelers = Maps.newHashMap();
//				List<Long> travelerIds = getRefundBeanIds(refundRecords,travelers);			
//				Map<String,Object> rMap = orderReviewService.getOrderReviewMutexInfo(orderId, productType.toString(), Context.REVIEW_FLOWTYPE_REFUND, travelerIds);
//				Map<String,Object> resultMap = (Map<String,Object>) rMap.get(Context.MUTEX_RESULT_lIST);
//				boolean flag = false;
//				StringBuffer sf = new StringBuffer();
//				for(Long tid : travelerIds){
//					if("1".equals(resultMap.get(tid.toString()).toString().split("/")[0])){
//						flag = true;
//						sf.append(travelers.get(tid.toString())+" "+resultMap.get(tid.toString()).toString().split("/")[2]+" ");
//					}
//				}
//				if(flag){
//					result.put("sign", 0);
//					result.put("result", sf);
//					return result;
//				}
				//流程互斥部分结束
				/////////////////////////////////////////////////////////////
				result = orderService.saveIslandGroupRefundReviewInfo(refundRecords, productType, flowType, orderId);
			} catch (Exception e) {
				result.put("sign", 0);
				result.put("result", e.getMessage());
				return result;
			}
			
			return result;
		}
		/**
		 * 提交保存团队退款申请，发起审核流程
		 * @author  chenry
		 * add Date 2015-06-18
		 */
		@SuppressWarnings({ "unchecked", "deprecation" })
		@ResponseBody
		@RequestMapping(value="saveHotelGroupRefundInfo")
		public Map<String,Object> saveHotelGroupRefundInfo(Model model,HttpServletRequest request, HttpServletResponse response,Integer productType,Integer flowType,String[]travelerId,int[]currencyId,String[] currencyName,String[]refund,String[] remark,String[]refundAmount, Long orderId,String refundRecordsStr) {
			
			Map<String, Object> result =new HashMap<String,Object>();
			try {
				List<RefundBean> refundRecords = net.sf.json.JSONArray.toList(
						net.sf.json.JSONArray.fromObject(refundRecordsStr), RefundBean.class);
				/////////////////////////////////////////////////////////////
				//添加退款互斥情况验证，若游客或者团队有退款流程或者互斥流程进行中，则不能进行退款申请操作
//				Map<String,String> travelers = Maps.newHashMap();
//				List<Long> travelerIds = getRefundBeanIds(refundRecords,travelers);			
//				Map<String,Object> rMap = orderReviewService.getOrderReviewMutexInfo(orderId, productType.toString(), Context.REVIEW_FLOWTYPE_REFUND, travelerIds);
//				Map<String,Object> resultMap = (Map<String,Object>) rMap.get(Context.MUTEX_RESULT_lIST);
//				boolean flag = false;
//				StringBuffer sf = new StringBuffer();
//				for(Long tid : travelerIds){
//					if("1".equals(resultMap.get(tid.toString()).toString().split("/")[0])){
//						flag = true;
//						sf.append(travelers.get(tid.toString())+" "+resultMap.get(tid.toString()).toString().split("/")[2]+" ");
//					}
//				}
//				if(flag){
//					result.put("sign", 0);
//					result.put("result", sf);
//					return result;
//				}
				//流程互斥部分结束
				/////////////////////////////////////////////////////////////
				result = orderService.saveHotelGroupRefundReviewInfo(refundRecords, productType, flowType, orderId);
			} catch (Exception e) {
				result.put("sign", 0);
				result.put("result", e.getMessage());
				return result;
			}
			
			return result;
		}
		/**
		 * 提交保存团队退款申请，发起审核流程
		 * @author  chenry
		 * add Date 2014-11-06
		 */
		@SuppressWarnings({ "unchecked", "deprecation" })
		@ResponseBody
		@RequestMapping(value="saveIslandBorrowing")
		public Map<String,Object> saveIslandBorrowing(Integer productType,Integer flowType, Long orderId,String refundRecordsStr) {
			
			Map<String, Object> result =new HashMap<String,Object>();
			try {
				List<BorrowingBean> refundRecords = net.sf.json.JSONArray.toList(
						net.sf.json.JSONArray.fromObject(refundRecordsStr), BorrowingBean.class);
				result = orderService.saveIslandBorrowing(refundRecords, productType, flowType, orderId);
			} catch (Exception e) {
				result.put("sign", 0);
				result.put("result", e.getMessage());
				return result;
			}
			
			return result;
		}
		/**
		 * 提交保存酒店借款申请，发起审核流程
		 * @author  chenry
		 * add Date 2014-11-06
		 */
		@SuppressWarnings({ "unchecked", "deprecation" })
		@ResponseBody
		@RequestMapping(value="saveHotelBorrowing")
		public Map<String,Object> saveHotelBorrowing(Model model,HttpServletRequest request, HttpServletResponse response,Integer productType,Integer flowType,String[]travelerId,int[]currencyId,String[] currencyName,String[]refund,String[] remark,String[]refundAmount, Long orderId,String refundRecordsStr) {
			
			Map<String, Object> result =new HashMap<String,Object>();
			try {
				List<BorrowingBean> refundRecords = net.sf.json.JSONArray.toList(
						net.sf.json.JSONArray.fromObject(refundRecordsStr), BorrowingBean.class);
				result = orderService.saveHotelBorrowing(refundRecords, 11, flowType, orderId);
			} catch (Exception e) {
				result.put("sign", 0);
				result.put("result", e.getMessage());
				return result;
			}
			
			return result;
		}
	private List<Long>getRefundBeanIds(List<RefundBean> beans,Map<String,String> travelers){
		Set<Long> set = Sets.newHashSet();
		List<Long> beanIds = Lists.newArrayList();
		
		for(RefundBean bean : beans){
			if(null == bean.getTravelerId()){
				set.add((long)0);
				if(null == bean.getTravelerName()){
					bean.setTravelerName("团队");
				}
				travelers.put("0", bean.getTravelerName());
			}else{
				set.add(Long.parseLong(bean.getTravelerId()));
				travelers.put(bean.getTravelerId(), bean.getTravelerName());
			}
		}
		Iterator<Long> it = set.iterator();
		while(it.hasNext()){
			beanIds.add(it.next());
		}
		return beanIds;
	}
	
	/**
	 * 取消审核
	 * @author  chenry
	 * add Date 2014-11-07
	 */
	@ResponseBody
	@RequestMapping(value="cancleAudit")
	public int cancleAudit(Model model,HttpServletRequest request, HttpServletResponse response, Long id) {
		Review r = reviewService.findReviewInfo(id);
		int sign = 0;
		if (null != r && REVIEW_UNAUDITED.intValue() == r.getStatus().intValue()) {
			sign = reviewService.removeReview(id);
		}
		if(null != r && r.getFlowType()!=null && r.getFlowType()!=null && r.getTravelerId()!=null){
			if (r.getFlowType().intValue() == Context.REVIEW_FLOWTYPE_EXIT_GROUP.intValue()  || r.getFlowType().intValue() == Context.REVIEW_FLOWTYPE_TRANSFER_GROUP.intValue()) {
				orderService.updateTravelerStatus(Context.TRAVELER_DELFLAG_NORMAL, r.getTravelerId());
			} 
		}
		return sign;
	}
	
	
	
	/**
	 * 
	 *  功能:获取占位余位信息
	 *
	 *  @author xiaoyang.tao
	 *  @DateTime 2014-10-31 下午6:18:22
	 *  @param model
	 *  @param request
	 *  @return
	 */
	@ResponseBody
	@RequestMapping(value="getLeftPayReservePosition")
	public Object getLeftPayReservePosition(Model model,HttpServletRequest request){
		Map<String, Object> result = new  HashMap<String, Object>();
		String agentId = request.getParameter("agentId");
		String productId = request.getParameter("productId");
		String productGroupId = request.getParameter("productGroupId");
		ActivityGroupReserve activityGroupReserve = activityGroupReserveDao.findByAgentIdAndSrcActivityIdAndActivityGroupId(Long.parseLong(agentId), Long.parseLong(productId), Long.parseLong(productGroupId));
		if(activityGroupReserve != null){
			result.put("leftNum", activityGroupReserve.getLeftpayReservePosition());
		}else{
			result.put("leftNum", 0);
		}
		return result;
	}
	
	/**
	 * 
	 *  功能: 设置预定页面控件数据源
	 *
	 *  @author xiaoyang.tao
	 *  @DateTime 2014-11-12 下午2:32:05
	 *  @param model
	 *  @param product
	 */
	private void setControlModel(Model model,TravelActivity product){
		//获取联运信息
		if(product.getActivityAirTicket() != null){
			List<IntermodalStrategy> intermodalList =  product.getActivityAirTicket().getIntermodalStrategies();
		    model.addAttribute("intermodalList", intermodalList);
		}
	    
		//获取国籍信息
	    List<Country> countryList = CountryUtils.getCountrys();
        model.addAttribute("countryList", countryList);
	    
	    //用于显示签证国家
        model.addAttribute("targetForeignCountry", getTargetForeignCountry(areaService.findAreasByActivity(product.getId()),model));
	    
        //获取护照类型信息
        Map<String,String> passportTypeList = DictUtils.getVisaTypeMap(Context.PASSPORT_TYPE);
        model.addAttribute("passportTypeList", passportTypeList);
	}
	
	/**
	 *  功能: 设置币种相关信息
	 *  @author xiaoyang.tao
	 *  @DateTime 2014-11-12 下午2:33:00
	 *  @param model
	 *  @param activityKind 产品类型
	 *   priceType 报名时使用的价格类型 0：同行价 1：直客价
	 */
	private void setCurrency(Model model, ProductOrderCommon productOrder, ActivityGroup productGroup, String activityKind, List<String> serialNumList){
		// 获取产品所属批发商
		Integer productId = productGroup.getSrcActivityId();
		TravelActivity product = travelActivityService.findById(Long.parseLong(productId.toString()));
		Long officeId = product.getProCompany();
		Office userOffice = officeService.findWholeOfficeById(officeId);
		//同行价：币种id、金额（成人、儿童、特殊）
		Long adultCurrencyId = null;
		BigDecimal adultPrice = null;
		Long childCurrencyId = null;
		BigDecimal childPrice = null;
		Long specialCurrencyId = null;
		BigDecimal specialPrice = null;
		//直客价：币种id、金额（成人、儿童、特殊）
		Long suggestAdultCurrencyId = null;
		BigDecimal suggestAdultPrice = null;
		Long suggestChildCurrencyId = null;
		BigDecimal suggestChildPrice = null;
		Long suggestSpecialCurrencyId = null;
		BigDecimal suggestSpecialPrice = null;
		// 推广价：币种id、金额（成人、儿童、特殊）
		Long retailAdultCurrencyId = null;
		BigDecimal retailAdultPrice = null;
		Long retailChildCurrencyId = null;
		BigDecimal retailChildPrice = null;
		Long retailSpecialCurrencyId = null;
		BigDecimal retailSpecialPrice = null;
		//单房差
		Long singleDiffCurrencyId = null;
		BigDecimal singleDiff = null;
		//定金
		Long payDepositCurrencyId = null;
		BigDecimal payDeposit = null;
		if (productOrder.getId() != null) {
			//(订单使用价类型)默认同行价
			MoneyAmount adultMoneyAmount = moneyAmountService.findAmountBySerialNum(serialNumList.get(0)).get(0);
			adultCurrencyId = adultMoneyAmount.getCurrencyId().longValue();
			adultPrice = adultMoneyAmount.getAmount();//
			MoneyAmount childMoneyAmount = moneyAmountService.findAmountBySerialNum(serialNumList.get(1)).get(0);
			childCurrencyId = childMoneyAmount.getCurrencyId().longValue();
			childPrice = childMoneyAmount.getAmount();//
			List<MoneyAmount> specialMoneyAmountList = moneyAmountService.findAmountBySerialNum(serialNumList.get(2));
			if (CollectionUtils.isNotEmpty(specialMoneyAmountList)) {
				MoneyAmount specialMoneyAmount = specialMoneyAmountList.get(0);
				specialCurrencyId = specialMoneyAmount.getCurrencyId().longValue();
				specialPrice = specialMoneyAmount.getAmount();//
			} else {
				specialPrice = new BigDecimal(0);
			}
			
			//单房差、定金
			MoneyAmount singleDiffMoneyAmount = moneyAmountService.findAmountBySerialNum(serialNumList.get(3)).get(0);
			singleDiffCurrencyId = singleDiffMoneyAmount.getCurrencyId().longValue();
			singleDiff = singleDiffMoneyAmount.getAmount();//
			MoneyAmount payDepositMoneyAmount = moneyAmountService.findAmountBySerialNum(serialNumList.get(4)).get(0);
			payDepositCurrencyId = payDepositMoneyAmount.getCurrencyId().longValue();
			payDeposit = payDepositMoneyAmount.getAmount();//
			
		    // 如果订单渠道不是门店，而是总社或集团客户，则需要考虑渠道费率
			if(StringUtils.isNotBlank(activityKind) && activityKind.equals("2")){
				if (productOrder.getAgentType() != -1) {
					// 获取推广价（包含渠道费率）
					retailAdultPrice = adultPrice;
					retailChildPrice = childPrice;
					retailSpecialPrice = specialPrice;
				}
			}
		}else{
			String currency = productGroup.getCurrencyType();
		    String[] currencyArr = {"1","1","1","1","1","1","1","1","1","1","1"};
		    if(StringUtils.isNotBlank(currency)){
		    	currencyArr = currency.split(",");
		    	//游轮产品有6个curreny值，分别是 同行价1/2、同行价3/4、直客价1/2、直客价3/4、定金、单房差
		    	if (Context.ORDER_TYPE_CRUISE.toString().equals(activityKind)) {
		    		adultCurrencyId = Long.parseLong(currencyArr[0]);
					childCurrencyId = Long.parseLong(currencyArr[1]);
					specialCurrencyId = 1L;
					suggestAdultCurrencyId = Long.parseLong(currencyArr[2]);
					suggestChildCurrencyId = Long.parseLong(currencyArr[3]);
					suggestSpecialCurrencyId = 1L;
					payDepositCurrencyId = Long.parseLong(currencyArr[4]);
		    		singleDiffCurrencyId = Long.parseLong(currencyArr[5]);
				//散拼产品有11个值，分别是 同行价（成人、儿童、特殊）、直客价（成人、儿童、特殊）、QUAUQ价（权限控制）（成人、儿童、特殊）、定金、单房差	
				} else if (Context.ORDER_TYPE_SP.toString().equals(activityKind)) {
					adultCurrencyId = Long.parseLong(currencyArr[0]);
					childCurrencyId = Long.parseLong(currencyArr[1]);
					specialCurrencyId = Long.parseLong(currencyArr[2]);
					suggestAdultCurrencyId = Long.parseLong(currencyArr[3]);
					suggestChildCurrencyId = Long.parseLong(currencyArr[4]);
					suggestSpecialCurrencyId = Long.parseLong(currencyArr[5]);
					if (currencyArr.length == 14) {  // 同行价 + 直客价 + 优惠价 + quauq价 + 定金 + 单房差
						retailAdultCurrencyId = Long.parseLong(currencyArr[9]);
						retailChildCurrencyId = Long.parseLong(currencyArr[10]);
						retailSpecialCurrencyId = Long.parseLong(currencyArr[11]);
						payDepositCurrencyId = Long.parseLong(currencyArr[12]);
						singleDiffCurrencyId = Long.parseLong(currencyArr[13]);
					} else if (currencyArr.length == 11) {  // 同行价 + 直客价 + quauq价/优惠价 + 定金 + 单房差 
						if (productGroup.getAdultDiscountPrice() != null && productGroup.getAdultDiscountPrice().compareTo(BigDecimal.ZERO) > 0) {  // 如果团期填写了优惠价，而且位数只有11位，则quauq价币种没有保存直接取同行价币种
							retailAdultCurrencyId = Long.parseLong(currencyArr[0]);
							retailChildCurrencyId = Long.parseLong(currencyArr[1]);
							retailSpecialCurrencyId = Long.parseLong(currencyArr[2]);
						} else {
							retailAdultCurrencyId = Long.parseLong(currencyArr[6]);
							retailChildCurrencyId = Long.parseLong(currencyArr[7]);
							retailSpecialCurrencyId = Long.parseLong(currencyArr[8]);
						}
						payDepositCurrencyId = Long.parseLong(currencyArr[9]);
						singleDiffCurrencyId = Long.parseLong(currencyArr[10]);
					} else if (currencyArr.length == 8) {  // 同行价 + 直客价 + 定金 + 单房差 (旧团无quauq价币种)
						retailAdultCurrencyId = Long.parseLong(currencyArr[0]);
						retailChildCurrencyId = Long.parseLong(currencyArr[1]);
						retailSpecialCurrencyId = Long.parseLong(currencyArr[2]);
						payDepositCurrencyId = Long.parseLong(currencyArr[6]);
						singleDiffCurrencyId = Long.parseLong(currencyArr[7]);
					} else {
						//币种不足8个未知
					}
		    	//其他团期类型产品有5个值，同行价（成人、儿童、特殊）、定金、单房差
				} else {
					adultCurrencyId = Long.parseLong(currencyArr[0]);
					childCurrencyId = Long.parseLong(currencyArr[1]);
					specialCurrencyId = Long.parseLong(currencyArr[2]);
					payDepositCurrencyId = Long.parseLong(currencyArr[3]);
		    		singleDiffCurrencyId = Long.parseLong(currencyArr[4]);
				}
		    }else{
		    	adultCurrencyId = 1L;
				childCurrencyId = 1L;
				specialCurrencyId = 1L;
				suggestAdultCurrencyId = 1L;
				suggestChildCurrencyId = 1L;
				suggestSpecialCurrencyId = 1L;
				retailAdultCurrencyId = 1L;
				retailChildCurrencyId = 1L;
				retailSpecialCurrencyId = 1L;
				singleDiffCurrencyId = 1L;
				payDepositCurrencyId = 1L;
		    }
		    adultPrice = productGroup.getSettlementAdultPrice();
		    childPrice = productGroup.getSettlementcChildPrice();
		    specialPrice = productGroup.getSettlementSpecialPrice();		    
		    suggestAdultPrice = productGroup.getSuggestAdultPrice();
		    suggestChildPrice = productGroup.getSuggestChildPrice();
		    suggestSpecialPrice = productGroup.getSuggestSpecialPrice();
		    
		    // 如果订单渠道不是门店，而是总社或集团客户，则需要考虑渠道费率
		    if (StringUtils.isNotBlank(activityKind) && activityKind.equals("2")) {
		    	if (productOrder.getAgentType() != -1) {
		    		Rate rate = RateUtils.getRate(productGroup.getId(), 2, productOrder.getOrderCompany());
		    		// 获取推广价（包含渠道费率）
		    		retailAdultPrice = OrderCommonUtil.getRetailPrice(productGroup.getSettlementAdultPrice(), productGroup.getQuauqAdultPrice(), rate, adultCurrencyId);
		    		retailChildPrice = OrderCommonUtil.getRetailPrice(productGroup.getSettlementcChildPrice(), productGroup.getQuauqChildPrice(), rate, childCurrencyId);
		    		retailSpecialPrice = OrderCommonUtil.getRetailPrice(productGroup.getSettlementSpecialPrice(), productGroup.getQuauqSpecialPrice(), rate, specialCurrencyId);
		    		// 订单保存费率（包含渠道费率）
		    		if (rate != null) {
		    			productOrder.setQuauqProductChargeType(rate.getQuauqRateType());
		    			productOrder.setQuauqProductChargeRate(rate.getQuauqRate());
		    			productOrder.setQuauqOtherChargeType(rate.getQuauqOtherRateType());
		    			productOrder.setQuauqOtherChargeRate(rate.getQuauqOtherRate());
		    			productOrder.setPartnerProductChargeType(rate.getAgentRateType());
		    			productOrder.setPartnerProductChargeRate(rate.getAgentRate());
		    			productOrder.setPartnerOtherChargeType(rate.getAgentOtherRateType());
		    			productOrder.setPartnerOtherChargeRate(rate.getAgentOtherRate());
		    			productOrder.setCutChargeType(rate.getChouchengRateType());
		    			productOrder.setCutChargeRate(rate.getChouchengRate());
		    			// 如果是预报名订单，则需要按保存当初汇率
		    			if (null != productOrder.getPreOrderId()) {
		    				T1PreOrder preOrder = preOrderService.findById(productOrder.getPreOrderId());
		    				if (null != preOrder.getQuauqProductChargeType()) {
		    					productOrder.setQuauqProductChargeType(preOrder.getQuauqProductChargeType());
		    					productOrder.setQuauqProductChargeRate(preOrder.getQuauqProductChargeRate());
		    					productOrder.setQuauqOtherChargeType(preOrder.getQuauqOtherChargeType());
		    					productOrder.setQuauqOtherChargeRate(preOrder.getQuauqOtherChargeRate());
		    					productOrder.setPartnerProductChargeType(preOrder.getPartnerProductChargeType());
		    					productOrder.setPartnerProductChargeRate(preOrder.getPartnerProductChargeRate());
		    					productOrder.setPartnerOtherChargeType(preOrder.getPartnerOtherChargeType());
		    					productOrder.setPartnerOtherChargeRate(preOrder.getPartnerOtherChargeRate());
		    					productOrder.setCutChargeType(preOrder.getCutChargeType());
		    					productOrder.setCutChargeRate(preOrder.getCutChargeRate());
		    				}
		    			}
		    		}
		    	}
		    }
		    
		    singleDiff = productGroup.getSingleDiff();
		    payDeposit = productGroup.getPayDeposit();
		}
		this.setCurrencyModel(model, "adult", adultCurrencyId, adultPrice);
		this.setCurrencyModel(model, "child", childCurrencyId, childPrice);
		this.setCurrencyModel(model, "special", specialCurrencyId, specialPrice);
		this.setCurrencyModel(model, "suggestAdult", suggestAdultCurrencyId, suggestAdultPrice);
		this.setCurrencyModel(model, "suggestChild", suggestChildCurrencyId, suggestChildPrice);
		this.setCurrencyModel(model, "suggestSpecial", suggestSpecialCurrencyId, suggestSpecialPrice);
		this.setCurrencyModel(model, "retailAdult", retailAdultCurrencyId, retailAdultPrice);
		this.setCurrencyModel(model, "retailChild", retailChildCurrencyId, retailChildPrice);
		this.setCurrencyModel(model, "retailSpecial", retailSpecialCurrencyId, retailSpecialPrice);
		this.setCurrencyModel(model, "singleDiff", singleDiffCurrencyId, singleDiff);

	    model.addAttribute("payDepositCurrencyId", payDepositCurrencyId);
	    model.addAttribute("payDeposit", payDeposit);
	    //获取币种信息
	    List<Currency> currencyList = null;
	    if (userOffice != null) {
	    	currencyList = currencyService.findCurrencyList(userOffice.getId());
		} else {
			currencyList = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
		}
        StringBuilder sbBuilder = new StringBuilder();
        for(Currency curr:currencyList){
        	// 如果是人民币，将币种汇率 和 换汇汇率-公司最低汇率标准 设成1
        	if("人民币".equals(curr.getCurrencyName())){
        		curr.setCurrencyExchangerate(new BigDecimal(1));
        		curr.setConvertLowest(new BigDecimal(1));
        	}
        	sbBuilder.append("<option ");
        	sbBuilder.append(" value=\"");
        	sbBuilder.append(curr.getId()).append("\" lang=\"" + curr.getCurrencyMark() + "\">").append(curr.getCurrencyName()).append("</option>");
        }
        model.addAttribute("currencyOptions", sbBuilder.toString()); 
        model.addAttribute("currencyList", currencyList); 
        JsonConfig currencyconfig = new JsonConfig();
        currencyconfig.setJsonPropertyFilter(new PropertyFilter() {
			@Override
			public boolean apply(Object arg0, String arg1, Object arg2) {
				if (arg1.equals("id") || arg1.equals("currencyName")|| arg1.equals("currencyMark")|| arg1.equals("convertLowest")) {
					return false;
				} else {
					return true;
				}
			}
		});
        net.sf.json.JSONArray currencyListJsonArray = net.sf.json.JSONArray.fromObject(currencyList, currencyconfig);
        model.addAttribute("currencyListJsonArray", currencyListJsonArray.toString());
        model.addAttribute("priceType", productOrder.getPriceType());
	}
	
	 /**
	  * 计算订单同行价（成人价*数量+儿童价*数量+特殊价*数量）
	  * @return
      */
	 private void calculateSumPrice(Model model,ProductOrderCommon productOrder){
		 Map<String,BigDecimal> totalPrice=new LinkedHashMap<>();
		 Map<String,String> currencyMarkMap=new HashMap<>();
		 Map<String,String> currencyNameMap=new HashMap<>();

		 Map<String,Object> modelMap=model.asMap();
		 if(modelMap.containsKey("adultCurrencyId")){//计算成人价格
			 String currencyId = modelMap.get("adultCurrencyId").toString();
			 if (currencyMarkMap.containsKey(currencyId)) {
				 BigDecimal adultPrice;
				 Object crj = modelMap.get("adultPrice");
				 if (crj != null && StringUtils.isNotBlank(crj.toString())) {
					 adultPrice = new BigDecimal(crj.toString());
				 } else {
					 adultPrice = new BigDecimal(0);
				 }
				 totalPrice.put(currencyId, totalPrice.get(currencyId).add(adultPrice).multiply(new BigDecimal(productOrder.getOrderPersonNumAdult())));
			 } else {
				 currencyMarkMap.put(currencyId,modelMap.get("adultCurrencyMark").toString());
				 currencyNameMap.put(currencyId,modelMap.get("adultCurrencyName").toString());
				 BigDecimal adultPrice;
				 Object crj = modelMap.get("adultPrice");
				 if (crj != null && StringUtils.isNotBlank(crj.toString())) {
					 adultPrice = new BigDecimal(crj.toString());
				 } else {
					 adultPrice = new BigDecimal(0);
				 }
				 totalPrice.put(currencyId, adultPrice.multiply(new BigDecimal(productOrder.getOrderPersonNumAdult())));
			 }
		 
		 }
		 if(modelMap.containsKey("childCurrencyId")){//计算儿童价
			 String currencyId=modelMap.get("childCurrencyId").toString();
			 if (currencyMarkMap.containsKey(currencyId)) {
				 BigDecimal childPrice;
				 Object etj = modelMap.get("childPrice");
				 if (etj != null && StringUtils.isNotBlank(etj.toString())) {
					 childPrice = new BigDecimal(etj.toString());
				 } else {
					 childPrice = new BigDecimal(0);
				 }
				 totalPrice.put(currencyId, totalPrice.get(currencyId).add(childPrice).multiply(new BigDecimal(productOrder.getOrderPersonNumChild())));
			 } else {
				 currencyMarkMap.put(currencyId,modelMap.get("childCurrencyMark").toString());
				 currencyNameMap.put(currencyId,modelMap.get("childCurrencyName").toString());
				 BigDecimal childPrice;
				 Object etj = modelMap.get("childPrice");
				 if (etj != null && StringUtils.isNotBlank(etj.toString())) {
					 childPrice = new BigDecimal(etj.toString());
				 } else {
					 childPrice = new BigDecimal(0);
				 }
				 totalPrice.put(currencyId, childPrice.multiply(new BigDecimal(productOrder.getOrderPersonNumChild())));
			 }
		 }
		 if(modelMap.containsKey("specialCurrencyId")){//计算特殊价
			 Object specialCurrencyIdObject = modelMap.get("specialCurrencyId");
			 String currencyId= null;
			 if (specialCurrencyIdObject != null && StringUtils.isNotBlank(specialCurrencyIdObject.toString())) {
				 currencyId = specialCurrencyIdObject.toString();
				 if(currencyMarkMap.containsKey(currencyId)){
					 BigDecimal specialPrice;
					 Object tsj = modelMap.get("specialPrice");
					 if (tsj != null && StringUtils.isNotBlank(tsj.toString())) {
						 specialPrice = new BigDecimal(tsj.toString());
					 } else {
						 specialPrice = new BigDecimal(0);
					 }
					 totalPrice.put(currencyId,totalPrice.get(currencyId).add(specialPrice).multiply(new BigDecimal(productOrder.getOrderPersonNumSpecial())));
				 }else{
					 
					 BigDecimal specialPrice;
					 Object tsj = modelMap.get("specialPrice");
					 if (tsj != null && StringUtils.isNotBlank(tsj.toString())) {
						 specialPrice = new BigDecimal(tsj.toString());
					 } else {
						 specialPrice = new BigDecimal(0);
					 }
					 currencyMarkMap.put(currencyId,modelMap.get("specialCurrencyMark").toString());
					 currencyNameMap.put(currencyId,modelMap.get("specialCurrencyName").toString());
					 totalPrice.put(currencyId,specialPrice.multiply(new BigDecimal(productOrder.getOrderPersonNumSpecial()==null?0:productOrder.getOrderPersonNumSpecial())));
				 }
			 }
		 }

		 //组装同行价字符串
		 StringBuilder stringBuilder=new StringBuilder();
		 for (String currencyId : totalPrice.keySet()) {
			 stringBuilder.append(currencyNameMap.get(currencyId)).append(totalPrice.get(currencyId).toString()).append("+");
		 }
		 //设置同行价
		 model.addAttribute("travelerSumPrice",stringBuilder.subSequence(0,stringBuilder.length()-1));
	 }
	
    /**
     * 
     * @param model
     * @param keyName
     * @param currencyId
     */
	private void setCurrencyModel(Model model, String keyName, Long currencyId, BigDecimal price) {
		Currency currency = currencyService.findCurrency(currencyId);
		if(currency != null){
			model.addAttribute(keyName + "CurrencyId", currencyId);
			model.addAttribute(keyName + "CurrencyMark", currency.getCurrencyMark());
			model.addAttribute(keyName + "CurrencyName", currency.getCurrencyName());
			model.addAttribute(keyName + "Price", price == null ? "" : price.setScale(2, BigDecimal.ROUND_HALF_UP));
		}
	}
	
	/**
	 * 根据订单类型获取要返回URL
	 * @param returnType 订单为：orderList 
	 * @param orderStatus
	 * @return
	 */
	private String getReturnUrl(String returnType, String orderStatus) {
		String suffix = "";
		if(Context.ORDER_STATUS_SINGLE.equals(orderStatus)) {
			if("orderList".equals(returnType)) {
				suffix = "list/orderList";
			} else if("manageOrder".equals(returnType)) {
				suffix = "single/manageOrderForSingle";
			}
	    } else if(Context.ORDER_STATUS_LOOSE.equals(orderStatus)) {
	    	if("orderList".equals(returnType)) {
				suffix = "loose/looseOrderList";
			} else if("manageOrder".equals(returnType)) {
				suffix = "loose/manageOrderForLoose";
			}
	    } else if(Context.ORDER_STATUS_STUDY.equals(orderStatus)) {
	    	if("orderList".equals(returnType)) {
				suffix = "study/studyOrderList";
			} else if("manageOrder".equals(returnType)) {
				suffix = "study/manageOrderForStudy";
			}
	    } else if(Context.ORDER_STATUS_BIG_CUSTOMER.equals(orderStatus)) {
	    	if("orderList".equals(returnType)) {
				suffix = "bigcustomer/bigCustomerOrderList";
			} else if("manageOrder".equals(returnType)) {
				suffix = "bigcustomer/manageOrderForBigCustomer";
			}
	    } else if(Context.ORDER_STATUS_FREE.equals(orderStatus)) {
	    	if("orderList".equals(returnType)) {
				suffix = "bigcustomer/bigCustomerOrderList";
			} else if("manageOrder".equals(returnType)) {
				suffix = "free/manageOrderForFreeTravel";
			}
	    } else {
	    	if("orderList".equals(returnType)) {
	    		suffix = "loose/looseOrderList";
			} else if("manageOrder".equals(returnType)) {
				suffix = "loose/manageOrderForLoose";
			}
	    }
		
		return "modules/order/" + suffix;
	}
	
	/******************************************************* 上传下载相关方法 ***************************************************************/
	
	
	@RequestMapping(value="interviewNotice/{orderId}")
	public ResponseEntity<byte[]> downloadInterviewNotice(@PathVariable String orderId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//面签通知文件列表
		List<File> fileList = new ArrayList<File>();
		Set<Long> idList = Sets.newHashSet();
		
		//根据主订单查询签证子订单ids
		List<Traveler> travelerList = visaOrderService.findTravelersByMainOrderId(Long.parseLong(orderId));
		if (CollectionUtils.isNotEmpty(travelerList)) {
			for (Traveler traveler : travelerList) {
				idList.add(traveler.getOrderId());
			}
		}
		
		//循环数组,获取每个签证订单下的面签通知
		//将单个订单的面签通知文件列表,汇总 到总的面签通知文件列表中,
		for (Long visaOrderId : idList) {
			List<File> tempFileList = visaInterviewNoticeService.mianqiantongzhiByOrderId(visaOrderId);
			if (CollectionUtils.isNotEmpty(tempFileList)) {
				for (int j=0;j<tempFileList.size();j++) {
					fileList.add(tempFileList.get(j));
				}
			}
		}
		
		if (CollectionUtils.isEmpty(fileList)) {
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter pw = response.getWriter();
			pw.println("没有面签通知!");
			return null;
		}
		
		try {
			//将文件列表变成压缩包,进行下载
			DownLoadController.downLoadFiles(fileList, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="getOrderModifyRecord/{orderId}")
	public Object getOrderModifyRecord(@PathVariable String orderId,Model model,
			HttpServletRequest request) throws Exception {
		List<Map<String,Object>> logOrderList = logOrderService.getLogSingleGroupOrderList(Long.parseLong(orderId));
		model.addAttribute("orderId", orderId);
		model.addAttribute("logOrderList", logOrderList);
		return "/modules/order/orderModifyRecord";
	}
	
	@ResponseBody
	@RequestMapping(value="getOrderModifyByTime")
	public Object getOrderModifyByTime(Model model, HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<>();
		String orderId = request.getParameter("orderId");
		String expend = request.getParameter("expend");
		List<Map<String,Object>> logOrderListByExtend = logOrderService.getLogSingleGroupOrderListByExpand(Long.parseLong(orderId), expend);
		map.put("result", "success");
		map.put("logOrderListByExtend", logOrderListByExtend);
		return map;
	}
	
	/**
	 * 判断订单是否有对应游客信息
	 * @param orderId
	 * @param status 游客资料、出团通知、确认单、面签通知
	 * @return
	 * @throws JSONException 
	 */
	@ResponseBody
	@RequestMapping(value="existExportData",method=RequestMethod.POST)
	public Object existExportData(@RequestParam(value="orderId") String orderId) throws JSONException {
		List<Traveler> travelerList = Lists.newArrayList();
		net.sf.json.JSONArray results = new net.sf.json.JSONArray();
		net.sf.json.JSONObject resobj = new net.sf.json.JSONObject();
		if(StringUtils.isNotBlank(orderId)) {
			travelerList = orderService.findTravelerByOrderId(Long.parseLong(orderId));
			if(CollectionUtils.isNotEmpty(travelerList)) {
				resobj.put("flag", "true");
			} else {
				resobj.put("flag", "false");
				resobj.put("warning", "此订单没有对应的游客信息");
			}
		} else {
			resobj.put("flag", "false");
			resobj.put("warning", "订单ID不能为空");
		}
		results.add(resobj);
		return results;
	}
	
	/**
	 * 导出订单对应游客信息
	 * @param orderId
	 */
	@RequestMapping(value="downloadData",method=RequestMethod.POST)
	public void downloadData(@RequestParam(value="orderId") String orderId,@RequestParam(value="orderNum") String orderNum, @RequestParam(value="orderType") String orderType, 
			HttpServletRequest request, HttpServletResponse response) {
		List<Object[]> travelerList = new ArrayList<Object[]>();
		//2016年4月12日  update by pengfei.shang 需求0228
		if("dfafad3ebab448bea81ca13b2eb0673e".equals(UserUtils.getUser().getCompany().getUuid())){
			try {
				List<Object[]> tempList = new ArrayList<Object[]>();
				int i = 0;
				tempList = orderService.selectRdhytTravelerByOrderId(Long.parseLong(orderId), Integer.parseInt(orderType));
				if(tempList != null && tempList.size() > 0) {
					/** 第一行数据领队以后数据置为空 */
					Object[] oneLine = new Object[21];
					oneLine[0] = tempList.get(0)[0];
					oneLine[1] = tempList.get(0)[1];
					oneLine[2] = tempList.get(0)[2];
					oneLine[3] = tempList.get(0)[3];
					oneLine[4] = tempList.get(0)[4];
					oneLine[5] = tempList.get(0)[5];
					oneLine[6] = "领队";
					travelerList.add(oneLine);
					for (Object[] o : tempList) {
						o[0] = "";
						o[1] = "";
						o[2] = "";
						o[3] = "";
						o[4] = "";
						o[5] = "";
						o[6] = ++i;
						String temp = null;
						if (o[17] != null && o[17].toString().length() >= 1) {
							temp = o[17].toString();
							temp = temp.replace("1", "因公护照").replace("2", "因私护照");
							o[17] = temp;
						}
					}
					travelerList.addAll(tempList);
				}
				//文件名称
				String fileName = orderId + "-游客信息";
				//Excel各行名称
				String[] cellTitle =  {"组团社序号","团队编号","年份","领队姓名","领队证号","编号","序号","中文姓名","英文/拼音","性别","出生日期","出生地","身份证号码","护照号码","发证机关","发证日期","护照有效期","护照类型","电话号码","渠道名字","渠道跟进销售","备注"};
				String firstTitle = orderId;
				ExportExcel.createExcle(fileName, travelerList, cellTitle, firstTitle, request, response);
			} catch (Exception e) {
				logger.error("下载出错");
				e.printStackTrace();
			}
		}else{
			try {
				List<Object[]> tempList = new ArrayList<Object[]>();
				int i = 0;
				tempList = orderService.selectTravelerByOrderId(Long.parseLong(orderId), Integer.parseInt(orderType));
				if(tempList != null && tempList.size() > 0) {
					for(Object[] o : tempList) {
						i++;
						o[0] = i;
						String temp = null;
						if(o[9] != null && o[9].toString().length() >= 1) {
							temp = o[9].toString();
							temp = temp.replace("1", "因公护照").replace("2", "因私护照");
							o[9] = temp;
						}
					}
					travelerList.addAll(tempList);
				}
				
				//文件名称
				String fileName = orderId + "-游客信息";
				//Excel各行名称
				String[] cellTitle =  {"序号","中文姓名","英文姓名","性别","出生日期","出生地","身份证号码","护照号码","护照有效期","护照类型","电话号码","渠道名字","渠道跟进销售","备注"};
				//文件首行标题
				String firstTitle = orderId;
				//导出 excel 首行显示 订单编号 20150108
				//String firstTitle = orderNum;
				ExportExcel.createExcle(fileName, travelerList, cellTitle, firstTitle, request, response);
			} catch (Exception e) {
				logger.error("下载出错");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 上传确认单：查询订单
	 * @param orderId
	 * @param orderType
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="uploadConfirmationHref/{orderId}/{orderType}")
	public String uploadConfirmationHref(@PathVariable String orderId, @PathVariable String orderType, Model model, HttpServletRequest request) {
	    ProductOrderCommon pro = orderService.getProductorderById(Long.parseLong(orderId));
		model.addAttribute("orderId",orderId);
		model.addAttribute("productorder",pro);
	    return "modules/order/uploadConfirmation";
	}
	
	/**
	 * 上传确认单 && 出团通知
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
		String inputId = request.getParameter("inputId"); // 区分确认单和出团通知
		String logContentSuc = "openNoticeFile".equals(inputId) == true ? "上传出团通知成功" : "上传确认单成功";
		String logContentFai = "openNoticeFile".equals(inputId) == true ? "上传出团通知失败" : "上传确认单失败";
		if( !StringUtils.isBlank(docFileIds)&& !StringUtils.isBlank(orderId)) {
    		ProductOrderCommon order = orderService.getProductorderById(Long.parseLong(orderId));
	        try {
	        	//保存出团通知
	    		if("openNoticeFile".equals(inputId)){
	    			String docFileId = "";
	    			String[] docFileArray = docFileIds.split(",");
	    			if(docFileArray.length > 0){ // 保留最后一个文件id
	    				docFileId = docFileArray[docFileArray.length -1];
	    			}
	    			order.setOpenNoticeFileId(docFileId);
	    			orderService.saveProductorder(order);
	    		}else{
	    			// 保存多文件id 以逗号分隔拼接
	    			String[] docFileArray = docFileIds.split(",");
	    			docFileIds = "";
	    			for(String docId:docFileArray){
	    				docFileIds += docId + ",";
	    			}
	    			docFileIds = StringUtils.blankReturnEmpty(order.getConfirmationFileId()) + docFileIds;
	    			order.setConfirmationFileId(docFileIds);
	    			orderService.saveProductorder(order);
	    			// 添加订单跟踪记录
	    			progressService.addConfirmationInfo(order);
	    		}
	    		//添加操作日志
	    		orderService.saveLogOperate(Context.log_type_orderform,
	    				Context.log_type_orderform_name, "订单" + orderId + logContentSuc, "2", order.getOrderStatus(), order.getId());
	    		
	    		
	    		resobj.put("success", logContentSuc);

	        } catch (Exception e) {
	        	//添加操作日志
	    		orderService.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name,
	    				"订单" + request.getParameter("orderId") + logContentFai, "2", order.getOrderStatus(), order.getId());
	    		resobj.put("error", logContentFai);
	            e.printStackTrace();
	        }
		}
	    return resobj;
	}
	
	//deleteDoc
	@RequestMapping(value ="deleteDoc")
	@ResponseBody
	public Object deleteDoc(HttpServletRequest request) throws Exception {
		net.sf.json.JSONObject resobj = new net.sf.json.JSONObject();
		String orderId = request.getParameter("orderId");
		String docId = request.getParameter("docId");
		if( !StringUtils.isBlank(docId)&& !StringUtils.isBlank(orderId)) {
    		ProductOrderCommon order = orderService.getProductorderById(Long.parseLong(orderId));
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
	        		orderService.saveProductorder(order);
	        		
	        		docInfoService.delDocInfoById(new Long(docId));
	        		
	        		// 如果全部确认单已被删除，则订单跟踪
	        		if (StringUtils.isBlank(docIds)) {
	        			progressService.removeConfirmationInfo(order);
	        		}
	        		
	        		//添加操作日志
	        		orderService.saveLogOperate(Context.log_type_orderform,
	        				Context.log_type_orderform_name, "订单" + orderId + "修改单成功", "2", order.getOrderStatus(), order.getId());
	        		resobj.put("success", "ok");
	        	}else{
	        		resobj.put("error", "删除确认单失败");
	        	}
	        	//保存文件
	        } catch (Exception e) {
	        	//添加操作日志
	    		orderService.saveLogOperate(Context.log_type_orderform, Context.log_type_orderform_name,
	    				"订单" + request.getParameter("orderId") + "修改确认单失败", "2", order.getOrderStatus(), order.getId());
	    		resobj.put("error", "删除确认单失败");
	            e.printStackTrace();  
	        }
		}
	    return resobj;
	}
	
	@RequestMapping(value ="downloadConfirmFiles")
	public String downloadConfirmFiles(HttpServletRequest request,Model model){
		String orderId = request.getParameter("orderId");
		if(!StringUtils.isBlank(orderId)) {
    		ProductOrderCommon order = orderService.getProductorderById(Long.parseLong(orderId));
    		String docFileIds = order.getConfirmationFileId();
    		if(!StringUtils.isBlank(docFileIds)){
    			String[] fileIds = docFileIds.replace(",", " ").trim().split(" ");
    			List<DocInfo> fileList = new ArrayList<DocInfo>();
    			for(String fileId:fileIds){
    				fileList.add(docInfoService.getDocInfo(new Long(fileId)));
    			}
    			model.addAttribute("docList", fileList);
    			model.addAttribute("orderId", orderId);
    			model.addAttribute("downloadUrl", "orderCommon/manage/userDownloadConfirmFile");
    			model.addAttribute("delUrl", "orderCommon/manage/deleteDoc");
    		}else{
    			//问价不存在
    			return "";
    		}
		}
	    return "include/downloadFiles";
	}
	
	@RequestMapping(value ="zipconfirmdownload/{orderIds}")
	public String zipconfirmdownload(@PathVariable("orderIds")String orderIds,HttpServletResponse response){
		String[] orderIdArray = orderIds.replace(",", " ").trim().split(" ");
		String docid = "";
		for(String str:orderIdArray){
			ProductOrderCommon order = orderService.getProductorderById(Long.parseLong(str));
			docid = docid + (order.getConfirmationFileId()==null?"":order.getConfirmationFileId() + ",");
			order.setDownloadFileIds(order.getConfirmationFileId());
			orderService.saveProductorder(order);
		}
	    return "redirect:" + Global.getAdminPath() + "/sys/docinfo/zipdownload/" + docid+"/confirm";
	}
	
	@RequestMapping(value ="userDownloadConfirmFile/{orderId}/{docid}")
	public String userDownloadConfirmFile(@PathVariable("docid") Long docid,@PathVariable("orderId") Long orderId,HttpServletResponse response) {
		//
		ProductOrderCommon order = orderService.getProductorderById(orderId);
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
		orderService.saveProductorder(order);
		return "redirect:" + Global.getAdminPath() + "/sys/docinfo/download/" + docid;
	}
	
	/**
	 * 上传游客资料：根据订单ID查询游客列表
	 * @param orderId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="uploadTravelerInfoHref/{orderId}/{orderType}")
	public String uploadTravelerInfoHref(@PathVariable String orderId, @PathVariable String orderType, Model model, HttpServletRequest request) {
	    List<HashMap<String, Object>> list = Lists.newArrayList();
	    
	    //查询正常游客和已成功转团游客
		List<Integer> delFlag = Lists.newArrayList();
		delFlag.add(Context.TRAVELER_DELFLAG_NORMAL);
		delFlag.add(Context.TRAVELER_DELFLAG_EXIT);
		delFlag.add(Context.TRAVELER_DELFLAG_TURNROUND);
		List<Traveler> travelerList = travelerService.findTravelerByOrderIdAndOrderType(Long.parseLong(orderId), Integer.parseInt(orderType), delFlag);
	    for (Traveler tra : travelerList) {
            HashMap<String, Object> map = Maps.newHashMap();
            List<TravelerFile> fileList = travelerService.findFileListByPid(tra.getId());
            if (CollectionUtils.isNotEmpty(fileList)) {
				Set<Integer> fileTypeSet = new HashSet<Integer>();
				for (TravelerFile fileListType: fileList){
					fileTypeSet.add(fileListType.getFileType());
				}
				map.put("fileSize", fileTypeSet.size());
//				map.put("fileSize", fileList.size());	//旧逻辑
            } else {
            	map.put("fileSize", 0);
            }
            map.put("traveler", tra);
            list.add(map);
        }
	    model.addAttribute("orderType",orderType);
	    model.addAttribute("orderTypeStr", OrderCommonUtil.getChineseOrderType(orderType));
	    model.addAttribute("travelers",list);
	    return "modules/order/travelerList";
	}
	
	/**
	 * 上传游客资料：根据订单ID查询游客文件列表
	 * @param orderId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="uploadTravelerFilesHref/{travelerId}/{orderType}")
	public String uploadTravelerFilesHref(@PathVariable String travelerId, @PathVariable String orderType, Model model, HttpServletRequest request) {
		
		//查询游客信息
		Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerId));
		//查询游客相对应的上传资料
        List<TravelerFile> fileList = travelerService.findFileListByPid(traveler.getId());
        
        //页面下载时要用到的所有文件ID
	    StringBuffer docIds = new StringBuffer("");
	    //上传文件类型和名称json字符串
	    net.sf.json.JSONArray array = new net.sf.json.JSONArray();
	    if(CollectionUtils.isNotEmpty(fileList)) {
	    	for(int i=0;i<fileList.size();i++) {
	    		TravelerFile travelerFile = fileList.get(i);
	    		net.sf.json.JSONObject object = new net.sf.json.JSONObject();
	    		object.put(travelerFile.getFileType(), travelerFile.getFileName());
	    		if(i != fileList.size()-1) {
	    			docIds.append(travelerFile.getSrcDocId() + ",");
	    		} else {
	    			docIds.append(travelerFile.getSrcDocId());
	    		}
	    		array.add(object);
	    	}
	    }
	    
	    model.addAttribute("orderTypeStr", OrderCommonUtil.getChineseOrderType(orderType));
	    model.addAttribute("travelerName",traveler.getName());
	    model.addAttribute("travelerId",travelerId);
	    model.addAttribute("fileList",fileList);
	    model.addAttribute("docIds",docIds);
	    model.addAttribute("fileArray",array.toString());
	    return "modules/order/travelerFiles";
	}
	
	/**
	 * 上传游客资料
	 * @param passportfile 护照首页
	 * @param idcardfrontfile 身份证正面
	 * @param entryformfile 申请表格
	 * @param photofile 电子照片
	 * @param idcardbackfile 身份证反面
	 * @param otherfile 其他
	 */
	@RequestMapping(value ="uploadTravelerFiles")
	public String uploadTravelerFiles(@RequestParam(value="passportfile",required=false) MultipartFile passportfile, 
			@RequestParam(value="idcardfrontfile",required=false) MultipartFile idcardfrontfile,
			@RequestParam(value="entryformfile",required=false) MultipartFile entryformfile,
			@RequestParam(value="housefile",required=false) MultipartFile housefile,
			@RequestParam(value="photofile",required=false) MultipartFile photofile,
			@RequestParam(value="idcardbackfile",required=false) MultipartFile idcardbackfile,
			@RequestParam(value="residencefile",required=false) MultipartFile residencefile,
			@RequestParam(value="otherfile",required=false) MultipartFile otherfile,
			@RequestParam(value="visaannexfile",required=false) MultipartFile visaannexfile,
			HttpServletRequest request, RedirectAttributes redirectAttributes) throws OptimisticLockHandleException, PositionOutOfBoundException, Exception {
		
		//根据游客ID查询游客
		String travelerId = request.getParameter("travelerId");
		Traveler traveler = travelerService.findTravelerById(Long.parseLong(travelerId));
		
		//查询订单
		ProductOrderCommon order = orderService.getProductorderById(traveler.getOrderId());
		
		//把资料类型和对应要上传文件放到Map中
		Map<Integer, MultipartFile> fileList = Maps.newHashMap();
		fileList.put(TravelerFile.PASSPORTS_TYPE, passportfile);
		fileList.put(TravelerFile.IDCARD_FRONT_TYPE, idcardfrontfile);
		fileList.put(TravelerFile.ENTRY_FORM_TYPE, entryformfile);
		fileList.put(TravelerFile.HOUSE_TYPE, housefile);
		fileList.put(TravelerFile.PHOTO_TYPE, photofile);
		fileList.put(TravelerFile.IDCARD_BACK_TYPE, idcardbackfile);
		fileList.put(TravelerFile.RESIDENCE_TYPE, residencefile);
		fileList.put(TravelerFile.OTHER_TYPE, otherfile);
		fileList.put(TravelerFile.VISA_TYPE, visaannexfile);
		//保存或更新上传文件
		travelerService.saveOrUpdateTravelerFile(fileList, traveler);
		addMessage(redirectAttributes, "上传成功");
		return "redirect:" + Global.getAdminPath() + "/orderCommon/manage/uploadTravelerInfoHref/" + order.getId() + "/" + order.getOrderStatus() + "?repage";
	}
	
	@RequestMapping(value="refundPayInfo")
	@ResponseBody
	public List<Map<Object, Object>> refundPayInfo(Model model,HttpServletRequest request,HttpServletResponse response){
		String id = request.getParameter("id");
		//付款类型：1：成本录入付款；2：退款付款；3：返佣付款；4：借款付款；5：退签证押金；6：追加成本付款;7：新审核成本录入付款；8：新审核退款付款；9：新审核返佣付款；10：新审核借款付款；11：新审核退签证押金；12：新审核追加成本付款；13：批量借款
		String type = request.getParameter("type");
		String orderType = request.getParameter("orderType");
		List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
		if(StringUtils.isNotBlank(id)){
			list = refundService.findbyRecordId(id,type,orderType);
		}
		for(int i=0;i<list.size();i++){
			String tmp = (String) list.get(i).get("payvoucher");
			StringBuffer name = new StringBuffer();
			if(StringUtils.isNotBlank(tmp)){
			
				List<DocInfo> docInfoList = docInfoService
						.getDocInfoBydocids(tmp);
				int docInfoListSize = docInfoList.size();
				for(int ds=0;ds<docInfoListSize;ds++){
					if(ds==docInfoListSize-1){
					    name.append(docInfoList.get(ds).getDocName());
					}else{
						name.append(docInfoList.get(ds).getDocName()).append("|");
					}
				}
			}
			list.get(i).put("payvoucher", name.toString());
		}
		return list; 
	}
	/**
	 * 组装BorrowingBean对象
	 */
	private List<BorrowingBean> getBorrowingBeanList(
			List<Map<String, String>> reviewMapList) {
		List<BorrowingBean> aList = new ArrayList<BorrowingBean>();
		if (null == reviewMapList || reviewMapList.isEmpty()) {
			return aList;
		}
		for (Map<String, String> map : reviewMapList) {
			aList.add(new BorrowingBean(map));
		}
		return aList;
	}
	/**
	 * 查看借款列表
	 * @author  chenry
	 * add Date 2015-05-06
	 */
	@RequestMapping(value = "viewBorrowingList")
	public String viewBorrowingList(Model model,HttpServletRequest request,HttpServletResponse response,Long orderId,Integer productType,Integer flowType){
		ProductOrderCommon productOrder = orderService.getProductorderById(orderId);
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		List<Map<String, String>> reviewMapList = reviewService.findReviewListMap(productType,flowType, orderId+"", false,product.getDeptId());
		
		List<BorrowingBean> reviewList = getBorrowingBeanList(reviewMapList);
		for(BorrowingBean borr :reviewList){
			if(borr.getTravelerId().contains(BorrowingBean.REGEX)|| "0".equals(borr.getTravelerId())){
				borr.setTravelerName("团队");
			}
			if(StringUtils.isNotBlank(borr.getCurrencyIds())&&borr.getCurrencyIds().contains(BorrowingBean.REGEX)){
				String compPrice = "";
				if(StringUtils.isNotBlank(borr.getCurrencyMarks())&&StringUtils.isNotBlank(borr.getBorrowPrices())){
					String[] cMarks = borr.getCurrencyMarks().split(BorrowingBean.REGEX);	
					String[] cPrices = borr.getBorrowPrices().split(BorrowingBean.REGEX);
					for(int i=0;i<cMarks.length;i++){
						compPrice+=cMarks[i]+cPrices[i]+"+";
					}
					borr.setCurrencyIds(compPrice.substring(0, compPrice.length()-1));
				}
				
			}
			else{
			    borr.setCurrencyIds(borr.getCurrencyMarks()+borr.getBorrowPrices());
			}
		}
		Collections.reverse(reviewList);
		model.addAttribute("bAList", reviewList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("productType", productType);
		return "modules/order/viewBorrowing";
	}
	/**
	 * 借款详情展示
	 * @param request
	 * @param response
	 * @param modasdel
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value="viewBorrowingInfo")
	public String viewBorrowingInfo(HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException {
		String orderId = request.getParameter("orderId");
		String reviewId = request.getParameter("reviewId");
		
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		
		List<ReviewDetail> rdlist = new ArrayList<ReviewDetail>();
		try{
			if(reviewId!=null){
				rdlist= reviewService.queryReviewDetailList(reviewId);
			}
		}catch(Exception e){
			logger.error("根据reviewid： " + reviewId + " 查询出来reviewDetail明细报错 ",e);
		}
		//将总金额和币种显示出来
		List<ReviewDetail> borrowPricesList = new ArrayList<ReviewDetail>();
		if(rdlist!=null&&rdlist.size()>0){
			for(int i = 0;i<rdlist.size();i++){
				if("borrowPrices".equals(rdlist.get(i).getMykey())){
					borrowPricesList.add(rdlist.get(i));
				}
				if("currencyMarks".equals(rdlist.get(i).getMykey())){
					borrowPricesList.add(rdlist.get(i));
				}
			}
		}
		
		List<BorrowingBean> blist = BorrowingBean.transferReviewDetail2BorrowingBean(rdlist);
		List<BorrowingBean> tralist = new ArrayList<BorrowingBean>();
		List<BorrowingBean> teamlist= new ArrayList<BorrowingBean>();
		List<BorrowingBean> borrowList = BorrowingBean.transferReviewDetail2BorrowingBean(borrowPricesList);
		//将blist 拆分为团队借款和游客借款两部分
		if(blist!=null&&blist.size()>0){
			int size = blist.size();
			model.addAttribute("applyDate", blist.get(0).getApplyDate());
			for(int i = 0;i<size;i++){
				BorrowingBean bean = blist.get(i);
				if("0".equals(bean.getTravelerId())){//团队借款
					teamlist.add(bean);
				}else{//游客借款
					tralist.add(bean);
				}
			}
			
		}
		
		if(reviewId!=null&&!"".equals(reviewId)){//显示动态审核的标志
	    	List<ReviewLog> rLog=reviewService.findReviewLog(Long.parseLong(reviewId));
			model.addAttribute("rLog",rLog);
	    }
		
		model.addAttribute("tralist", tralist);
		model.addAttribute("teamlist",teamlist);
		model.addAttribute("borrowList", borrowList);
		model.addAttribute("totalsize", borrowList.size());
		model.addAttribute("reviewId", reviewId);
		List<Currency> currencyList = currencyService.findCurrencyList(companyId);
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("orderId", orderId);
		ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
		model.addAttribute("productOrder", productOrder);
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		model.addAttribute("product", product);
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		model.addAttribute("productType", productOrder.getOrderStatus());
		return "modules/order/viewBorrowingInfo";
	}
	/**
	 * 订单借款申请
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value="applyBorrowing")
	public String applyBorrowing(HttpServletRequest request,String productType, HttpServletResponse response,Model model) throws ParseException {
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		String orderId= request.getParameter("orderId");
		ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
		model.addAttribute("productOrder", productOrder);
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		model.addAttribute("product", product);
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		List<Currency> currencyList = currencyService.findSortCurrencyList(companyId);
		model.addAttribute("orderId", orderId);
		model.addAttribute("currencyList", currencyList);
		List<Map<String, Object>> travelerList=orderService.getBorrowingTravelerByOrderId(Long.parseLong(orderId),Integer.parseInt(productType));
		model.addAttribute("flowType", Context.REVIEW_FLOWTYPE_BORROWMONEY);
		model.addAttribute("productType",productType);
		model.addAttribute("travelerList", travelerList);
		return "modules/order/applyBorrowing";
	}
	/**
	 * 流程申请
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value="saveBorrowing")
	public String saveBorrowing(HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException {
		
		return orderService.saveBorrowing(request);
	}
	
	/**
	 * 根据渠道Id获取跟进销售
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getSalerByAgentId")
	public Object getSalerByAgentId(Model model, HttpServletRequest request){
		Map<String, String> result = Maps.newHashMap();
		String agentId = request.getParameter("agentId");
		if (StringUtils.isNotBlank(agentId)) {
			if(agentId.equals("-1") && UserUtils.getUser().getCompany().getUuid().equals("33ab2de5fdc842caba057296b28f5bae")){
				result.put(UserUtils.getUser().getId().toString(), UserUtils.getUser().getName());
			} else {
				Agentinfo agentInfo = agentinfoService.findAgentInfoById(Long.parseLong(agentId));
				if (agentInfo != null) {
					if (agentInfo.getId() == -1) {
						List<User> userList = systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId());
						if (CollectionUtils.isNotEmpty(userList)) {
							for (User user : userList) {
								result.put(user.getId().toString(), user.getName().toString());
							}
							result.put("userNum", Integer.valueOf(userList.size()).toString());
						}
					} else {
						List<User> salers = UserUtils.getUserListByIds(agentInfo.getAgentSalerId());
						for (User user : salers) {
							result.put(user.getId().toString(), user.getName().toString());
						}
					}
					result.put("loginUserId", UserUtils.getUser().getId().toString());
				} else {
					result.put(UserUtils.getUser().getId().toString(), UserUtils.getUser().getName());
				}
			}
		} else {
			result.put(UserUtils.getUser().getId().toString(), UserUtils.getUser().getName());
		}
		return result;
	}
	
	/**
	 * 当选择quauq渠道报名时，销售列取当前批发商所有账户（TODO 是否有别的条件：如拥有quauq报名权限）
	 * @param model
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getSalers4QUAUQ")
	public Object getSalers4QUAUQ(Model model, HttpServletRequest request){
		Map<String, String> result = Maps.newHashMap();
		// 获取quauq
		List<User> userList = systemService.getQuauqBookerByCompanyId(UserUtils.getUser().getCompany().getId());
		if (CollectionUtils.isNotEmpty(userList)) {
			for (User user : userList) {
				result.put(user.getId().toString(), user.getName().toString());
			}
			result.put("userNum", Integer.valueOf(userList.size()).toString());
		}
		// 存放当前登录人信息
		result.put("loginUserId", UserUtils.getUser().getId().toString());
		return result;
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
		if (StringUtils.isBlank(orderId)) {
			returnResult.put("flag", "faild");
			returnResult.put("message", "订单id获取失败！");
			return returnResult;
		}
		// 获取订单 
		ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
		//判断订单占位状态 、确认状态 
		if (productOrder == null) {
			returnResult.put("flag", "faild");
			returnResult.put("message", "订单获取失败！");
			return returnResult;
		}
		if (!(Context.ORDER_PAYSTATUS_YZW.equals(productOrder.getPayStatus().toString()) || Context.ORDER_PAYSTATUS_YZF.equals(productOrder.getPayStatus().toString()) || Context.ORDER_PAYSTATUS_YZFDJ.equals(productOrder.getPayStatus().toString()))) {
			returnResult.put("flag", "faild");
			returnResult.put("message", "不是已占位订单！");
			return returnResult;
		}
		if (Context.SEIZEDCONFIRMATIONSTATUS_1 == productOrder.getSeizedConfirmationStatus()) {
			returnResult.put("flag", "faild");
			returnResult.put("message", "订单已确认！");
			return returnResult;
		}
		// 变更确认状态
		orderService.handleConfirmStatus(productOrder);
		returnResult.put("flag", "success");
		return returnResult;
	}
	
	/**
	 * 获取订单信息 
	 * @author yakun.bai
	 * @Date 2016-5-25
	 */
	@ResponseBody
	@RequestMapping(value ="getOrderInfoById")
	public Map<String, String> getOrderInfoById(Model model, HttpServletRequest request) {
		Map<String, String> result = Maps.newHashMap();
	    String orderId = request.getParameter("orderId");
	    if (StringUtils.isNotBlank(orderId)) {
	    	ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
	    	Double remainDays = productOrder.getRemainDays();
	    	Date activationDate = productOrder.getActivationDate();
	    	Calendar currentDate = Calendar.getInstance();
    		Calendar calActivationDate = Calendar.getInstance();
    		calActivationDate.setTime(activationDate);
    		result.put("outOfTime", "0");
    		// 批发商配置自动取消  +  订单未被确认
    		if (1 == UserUtils.getUser().getCompany().getIsCancleOrder()  && !Context.SEIZEDCONFIRMATIONSTATUS_1.equals(productOrder.getSeizedConfirmationStatus()) 
    				&& remainDays != null && 1 != productOrder.getIsAfterSupplement()) {
    			String payMode = productOrder.getPayMode();
    			boolean isOutOfRemainDay = (calActivationDate.getTimeInMillis() + remainDays*24*3600*1000 - currentDate.getTimeInMillis() < 0 
    					&& (payMode.equals("1") || payMode.equals("2")));
    			if (isOutOfRemainDay) {
    				result.put("outOfTime", "1");
    			}
    		}
	    	result.put("orderType", productOrder.getPayStatus().toString());
	    }
	    return result;
	}

	
	/**
	 * 获取订单信息 
	 * @author yakun.bai
	 * @Date 2016-5-25
	 */
	@ResponseBody
	@RequestMapping(value ="getOrderIdByPreOrderId")
	public Map<String, String> getOrderIdByPreOrderId(Model model, HttpServletRequest request) {
		Map<String, String> result = Maps.newHashMap();
		String preOrderId = request.getParameter("preOrderId");
		if (StringUtils.isNotBlank(preOrderId)) {
			Long orderId = orderService.getOrderIdByPreOrderId(Long.parseLong(preOrderId));
			if (null != orderId) {
				result.put("orderId", orderId.toString());
			}
		}
		return result;
	}

	
	/**
	 * 需求527 增加批量打印功能
	 * @author yang.wang
	 * @date 2016.10.24
	 * */
	@RequestMapping(value = "batchPrint")
	public String batchPrint(Long[] payids, Integer[] ordertypes, Model model) {
		
//		model.addAttribute("payids", payids);
//		model.addAttribute("ordertypes", ordertypes);
		boolean isHQX = false;
		if (Context.SUPPLIER_UUID_HQX.equals(UserUtils.getUser().getCompany().getUuid())) {
			isHQX = true;
		}
		model.addAttribute("isHQX", isHQX);
		model.addAttribute("isLMT", Context.SUPPLIER_UUID_LMT.equals(UserUtils.getUser().getCompany().getUuid()));
		
		String companyName = UserUtils.getUser().getCompany().getCompanyName();
		model.addAttribute("companyName", companyName);
		
		List<Map<Object, Object>> printList = new ArrayList<>();// 打印列表
		
		for (int index = 0; index < payids.length; index++) {
			Long payid = payids[index];
			Integer orderType = ordertypes[index];
			
			Map<Object, Object> print = new HashMap<>();
			print.put("payid", payid.toString());
			print.put("orderType", orderType.toString());
			
			List<Map<Object, Object>> payList = null;
			if (orderType == Context.ORDER_TYPE_JP){
				payList = orderService.getAirPayPrint(payid.toString());
			}else if (orderType < Context.ORDER_TYPE_QZ || orderType == Context.ORDER_TYPE_CRUISE){			
				payList = orderService.getActivityPayPrint(payid.toString(), orderType);
			}else if (orderType == Context.ORDER_TYPE_HOTEL){			
				payList = orderService.getHotelPayPrint(payid.toString());
			}else if (orderType == Context.ORDER_TYPE_ISLAND){			
				payList = orderService.getIslandPayPrint(payid.toString());
			}
			
			if (payList.size() == 1) {
				if (orderType == Context.ORDER_TYPE_JP) {
					print.put("groupCode", payList.get(0).get("groupCode"));
					print.put("from_area", DictUtils.getDictList("from_area"));// 出发城市
					String departureCity = DictUtils.getDictLabel(String.valueOf(payList.get(0).get("departureCity")), "from_area", "");
					String arrivedCity = areaService.get(Long.parseLong(payList.get(0).get("arrivedCity").toString())).getName();

					print.put("areas", areaService.findAirportCityList("")); //到达城市
					String airTypeValue = payList.get(0).get("airType").toString();
					String airType = "";
					if(StringUtils.isNotBlank(airTypeValue)){
						airType = AirTicketUtils.getAirType(Integer.parseInt(airTypeValue));
					}
					String ticket_area_type = "";
					String ticketAreaType = payList.get(0).get("ticket_area_type").toString();
					if (ticketAreaType.equals("1")) {
						ticket_area_type = "（内陆）";
					} else if (ticketAreaType.equals("2")) {
						ticket_area_type = "（国际）";
					} else if (ticketAreaType.equals("3")) {
						ticket_area_type = "（内陆+国际）";
					}
					print.put("groupName", departureCity + " &mdash;" + arrivedCity + airType + ticket_area_type);	
				} else if (orderType < Context.ORDER_TYPE_QZ || orderType == Context.ORDER_TYPE_CRUISE 
						|| orderType == Context.ORDER_TYPE_HOTEL || orderType == Context.ORDER_TYPE_ISLAND) {
					print.put("groupCode", payList.get(0).get("groupCode"));
				    print.put("groupName", payList.get(0).get("acitivityName"));
				}
				
				if (payList.get(0).get("isAsAccount") != null && !"1".equals(payList.get(0).get("isAsAccount").toString())) {
					payList.get(0).put("accountDate", null);
				}
				print.put("pay", payList.get(0));
				
				String orderid = payList.get(0).get("orderid").toString();
				String traveler = "";
				if (orderType != Context.ORDER_TYPE_HOTEL && orderType != Context.ORDER_TYPE_ISLAND) {
					List<Map<Object, Object>> list = orderService.getTraveler(orderid,orderType);
					for(int i = 0;i < list.size(); i ++){
						if (i == 0) {
							traveler = list.get(i).get("name").toString();
						} else {
							traveler += "； " + list.get(i).get("name");
						}
					}
				} else if (orderType == Context.ORDER_TYPE_HOTEL) {
					List<HotelTraveler> hotelTravelerList = hotelTravelerDao.findTravelerByOrderUuid(payList.get(0).get("orderUuid").toString(), false);
					for (int i = 0; i < hotelTravelerList.size(); i++) {
						if (i == 0) {
							traveler = hotelTravelerList.get(i).getName();
						} else {
							traveler += "； " + hotelTravelerList.get(i).getName();
						}
					}
				} else if (orderType == Context.ORDER_TYPE_ISLAND) {
					List<IslandTraveler> islandTravelerList=islandTravelerDao.findTravelerByOrderUuid(payList.get(0).get("orderUuid").toString(), false);
					for (int i = 0; i < islandTravelerList.size(); i++) {
						if (i==0) {
							traveler = islandTravelerList.get(i).getName();
						} else {
							traveler += "； " + islandTravelerList.get(i).getName();
						}
					}
				}
				StringBuffer str = new StringBuffer();
				if (StringUtils.isNotBlank(traveler)) {
					String[] array = traveler.split("；");
					for (int i = 0; i < array.length; i++) {
						if(i%10 != 0 || i == 0){
							str.append(array[i]);
						}else{
							str.append(array[i]).append("").append("</br>");//去掉了分号 by chy 2016年1月27日10:43:21 bug号 12230
						}
					}
				}
				print.put("traveler", str.toString());
				String capitalMoney = "";
				String payPriceType = payList.get(0).get("payPriceType").toString();
				if (payPriceType!=null) {
					if(payPriceType.equals("11") || payPriceType.equals("21") || payPriceType.equals("31")) {
						String payedMoney = payList.get(0).get("payedMoney").toString();
						//当前批发商的美元、加元汇率（目前环球行）
						List<Currency> currencylist = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
						//其它币种汇率
						BigDecimal currencyExchangerate = null;
						//人民币计算
						BigDecimal amountCHN = new BigDecimal("0");
						//增加多币种金额判断20150422
						String[] moneys = payedMoney.split("\\+");
						if (moneys.length > 0) {
							for(int i = 0 ; i < moneys.length; i++) {
								//韩元-2,000.00
								Pattern p = Pattern.compile("\\-?\\d+\\.\\d+");
								String notThundsMoney = moneys[i].replaceAll(",", "");
								Matcher matcher = p.matcher(notThundsMoney);
								matcher.find();
								String money = matcher.group();
								String currencyName = notThundsMoney.replaceAll(money, "").trim();
								for (Currency currency : currencylist) {
									if (currency.getCurrencyName().equals(currencyName)) {
										currencyExchangerate = currency.getConvertLowest();
										break;
									}
								}
								if (null == currencyExchangerate) {
									currencyExchangerate = new BigDecimal(1);
								}
								amountCHN = amountCHN.add(BigDecimal.valueOf(Double.parseDouble(money)).multiply(currencyExchangerate));
							}
							if (amountCHN.doubleValue() < 0) {
								capitalMoney = "人民币" + "红字" + StringNumFormat.changeAmount(amountCHN.toString().replaceAll("-", ""));
								print.put("payedMoney", MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
							}else {
								capitalMoney = "人民币" + StringNumFormat.changeAmount(amountCHN.toString());
								print.put("payedMoney", MoneyNumberFormat.getThousandsMoney(Double.parseDouble(amountCHN.toString()), MoneyNumberFormat.THOUSANDST_POINT_TWO));
							}
							print.put("capitalMoney", capitalMoney);
						}
					}
				}
				
				//0002 需求 新增开票状态和开票金额 只针对环球行 by chy 2016年1月5日19:57:08 start
				if (isHQX) {
					//开票状态
					String invoiceStatus = "已开票";
					List<Orderinvoice> list = orderinvoiceService.findOrderinvoiceByOrderIdOrderType(Integer.parseInt(orderid), orderType); 
					List<Orderinvoice> list2 = orderinvoiceService.findOrderinvoiceByOrderIdOrderType2(Integer.parseInt(orderid), orderType); 
					if (list == null || list.size() == 0) {//如果没有已开票的记录 则 肯定是待开票 或空
						if (list2 == null || list2.size() == 0) {//如果没有待开票的记录 则为空
							invoiceStatus = "";
						} else {//否则就是待开票状态
							invoiceStatus = "待开票";
						}
					}
					print.put("invoiceStatus", invoiceStatus);
					//开票金额
					String invoiceMoney = OrderCommonUtil.getOrderInvoiceMoney(orderType.toString(), orderid);
					if ("0.00".equals(invoiceMoney) && "".equals(invoiceStatus)) {
						invoiceMoney = "";
					} else {
						invoiceMoney = "¥ " + invoiceMoney;
					}
					print.put("invoiceMoney", invoiceMoney);
				}
				//0002 需求 新增开票状态和开票金额 by chy 2016年1月5日19:57:08 end
				print.put("groupOpenDate", payList.get(0).get("groupOpenDate")); 
			}
			
			Date printTime = null;
			if (orderType == Context.ORDER_TYPE_HOTEL) {
				PayHotelOrder payHotelOrder = payHotelOrderDao.getById(payid.intValue());
				printTime = payHotelOrder.getPrintTime();
			} else if (orderType == Context.ORDER_TYPE_ISLAND) {
				PayIslandOrder payIslandOrder = payIslandOrderDao.getById(payid.intValue());
				printTime = payIslandOrder.getPrintTime();
			} else {
				Orderpay orderpay = orderpayDao.findOne(payid);
				printTime = orderpay.getPrintTime();
			}
			
			if (null != printTime) {
				print.put("firstPrintTime", printTime);
			} else {
				print.put("firstPrintTime", new Date());
			}
			
			printList.add(print);
			
		}
		
		model.addAttribute("list", printList);
		return "modules/order/batchPrint";
		
	}
	
	@ResponseBody
	@RequestMapping(value = "updateBatchPrint")
	public Map<String, Object> updateBatchPrint(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> result = new HashMap<>();
		List<Map<String, Object>> msg = new ArrayList<>();
		
		String[] payids = request.getParameter("payids").split(",");
		String[] ordertypes = request.getParameter("ordertypes").split(",");
		
		if (payids != null && ordertypes != null && payids.length > 0 && ordertypes.length > 0) {
			for (int index = 0; index < payids.length; index++) {
				Map<String, Object> map = new HashMap<>();
				Long payId = Long.parseLong(payids[index]);
				Integer orderType = Integer.parseInt(ordertypes[index]);
				
				if (payId != null && payId.longValue() != 0 && orderType != null) {
					Date date = null;
					if (orderType == 11) {
						date = updateHotelOrderPayPrinted(payId);
					} else if (orderType == 12) {
						date = updateIslandOrderPayPrinted(payId);
					} else {
						date = updateOrderPayPrinted(payId);
					}
					
					map.put("payId", payId);
					map.put("orderType", orderType);
					map.put("date", date == null ? "" : DateUtils.date2String(date, DateUtils.DATE_PATTERN_YYYY_MM_DD_HH_MM_SS));
					
					msg.add(map);
				} else {
					result.put("flag", "fail");
					result.put("msg", "");
					return result;
				}
				
			}
			
			result.put("flag", "success");
			result.put("msg", msg);
		} else {
			result.put("flag", "fail");
			result.put("msg", "");
		}
		return result;
	}
}