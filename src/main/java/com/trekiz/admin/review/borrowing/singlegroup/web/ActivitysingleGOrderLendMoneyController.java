package com.trekiz.admin.review.borrowing.singlegroup.web;

import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewErrorCode;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.engine.entity.ReviewLogNew;
import com.quauq.review.core.support.CommonResult;
import com.quauq.review.core.support.ReviewResult;
import com.quauq.review.core.type.OrderByDirectionType;
import com.quauq.review.core.type.OrderByPropertiesType;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.MoneyNumberFormat;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
import com.trekiz.admin.modules.airticketorder.service.AirTicketOrderLendMoneyService;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.airticket.service.IAirticketPreOrderService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.*;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.repository.VisaOrderDao;
import com.trekiz.admin.review.borrowing.airticket.formbean.NewBorrowingBean;
import com.trekiz.admin.review.borrowing.airticket.service.NewOrderReviewService;
import com.trekiz.admin.review.money.entity.NewProcessMoneyAmount;
import com.trekiz.admin.review.money.service.NewProcessMoneyAmountService;
import com.trekiz.admin.review.mutex.ReviewMutexService;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

import static com.quauq.review.core.engine.config.ReviewVariableKey.*;

/**
 * 单团类借款审批控制类
 * @author 宋扬 
 */
@Controller
@RequestMapping(value = "${adminPath}/singlegrouporder/lendmoney")
public class ActivitysingleGOrderLendMoneyController {

//	private static final int REFUND_PRODUCTTYPE_AIRTICKET = 7;
	
	private static final Logger log = Logger.getLogger(ActivitysingleGOrderLendMoneyController.class);
	
	@Autowired
	private IActivityAirTicketService activityAirTicketService;
	
	@Autowired
	private VisaOrderDao visaOrderDao;
	
	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	
	@Autowired
	private AirportService airportService;
	
	@Autowired
	private AirlineInfoService airlineInfoService;

	@Autowired
	private AgentinfoService agentinfoService;
	
	@Autowired
	private OfficeService officeService;

	@Autowired
	private SysIncreaseService sysIncreaseService;
	
	@Autowired
	private IAirticketPreOrderService airticketPreOrderService;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private AreaService areaService;
	 
	@Autowired
    private ReviewService reviewService;
	
	@Autowired
	private MoneyAmountService moneyAmountService;

	@Autowired
	private OrderCommonService orderService;
	
	@Autowired
	private ActivityGroupService activityGroupService;
	
	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	
	@Autowired
	private ActivityAirTicketDao activityAirTicketDao;
	
	@Autowired
	private AirTicketOrderLendMoneyService airTicketOrderLendMoneyService;
	
	@Autowired
	private com.quauq.review.core.engine.ReviewService processReviewService;
	
	@Autowired
	private NewProcessMoneyAmountService processMoneyAmountService;
	
	@Autowired
	private UserReviewPermissionChecker userReviewPermissionChecker;
	
	@Autowired
	private ReviewMutexService reviewMutexService;
	
    @Autowired
	@Qualifier("travelActivitySyncService")
    private ITravelActivityService travelActivityService;
    
	@Autowired
	private NewOrderReviewService newOrderReviewService;
	
	/**
	 * 单团类订单借款明细查询
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value="airticketLendMoneyList")
	public String airticketLendMoneyList(HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException {
		
		return "review/airticket/borrowing/airticketLendMoneyList";
	}

	/**
	 * 借款明细查询
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value="airticketLendMoneyByReviewId")
	public String airticketLendMoneyByReviewId(HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException {
		String orderId = request.getParameter("orderId");
		String reviewId = request.getParameter("reviewId");

		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());

		List<Map<String,Object>> rdlist = new ArrayList<Map<String,Object>>();
		Map<String,Object>  processMap = null;
		try{
			if(reviewId!=null){
				processMap = processReviewService.getReviewDetailMapByReviewId(reviewId);
				rdlist.add(processMap);
			}
		}catch(Exception e){
			log.error("根据reviewid： " + reviewId + " 查询出来reviewDetail明细报错 ",e);
		}
		//将总金额和币种显示出来
		List<Map<String,Object>> borrowPricesList = new ArrayList<Map<String,Object>>();
		Map<String,Object> borrowPricesMap = new HashMap<String, Object>();
//		rdlist.get(0).get("borrowPrices");
//		rdlist.get(0).get("borrowRemark");

		if(rdlist!=null&&rdlist.size()>0){
			for(int i = 0;i<rdlist.size();i++){
				if(rdlist.get(i).containsKey("borrowPrices")){
					borrowPricesMap.put("borrowPrices", rdlist.get(i).get("borrowPrices").toString());
				}
				if(rdlist.get(i).containsKey("currencyMarks")){
					borrowPricesMap.put("currencyMarks", rdlist.get(i).get("currencyMarks").toString());
				}
				if(rdlist.get(i).containsKey("borrowRemark")){
					model.addAttribute("borrowRemark",rdlist.get(i).get("borrowRemark"));
				}
			}
		}
		borrowPricesList.add(borrowPricesMap);

		List<NewBorrowingBean> blist = NewBorrowingBean.transferReviewDetail2BorrowingBeanMap(rdlist);
		List<NewBorrowingBean> tralist = new ArrayList<NewBorrowingBean>();
		List<NewBorrowingBean> teamlist= new ArrayList<NewBorrowingBean>();
		List<NewBorrowingBean> borrowList = NewBorrowingBean.transferReviewDetail2BorrowingBeanMap(borrowPricesList);
		//将blist 拆分为团队借款和游客借款两部分
		if(blist!=null&&blist.size()>0){
			int size = blist.size();
			for(int i = 0;i<size;i++){
				NewBorrowingBean bean = blist.get(i);
				if("0".equals(bean.getTravelerId())){//团队借款
					teamlist.add(bean);
				}else{//游客借款
					tralist.add(bean);
				}
			}

		}	
		if(reviewId!=null&&!"".equals(reviewId)){//显示动态审批的标志
				
			List<ReviewLogNew> rLog = processReviewService.getReviewLogByReviewId(reviewId);
		    	
			model.addAttribute("rLog",rLog);
		}

		//金额千分位显示
		if (tralist.size() > 0) {
			for (NewBorrowingBean nbb : tralist) {
				if (nbb.getLendPrice() != null && !"".equals(nbb.getLendPrice())) {
					nbb.setLendPrice(MoneyNumberFormat.getThousandsByRegex(nbb.getLendPrice(), 2));
				}
				
				if (nbb.getPayPrice() != null && !"".equals(nbb.getPayPrice())) {
					//nbb.setPayPrice(MoneyNumberFormat.getThousandsByRegex(nbb.getPayPrice(), 2));
					String price = nbb.getPayPrice();
					String[] prices = price.split("\\+");
					String payPriceStr = "";
					for(String prece:prices){
						String reg = "[^\u4e00-\u9fa5]";
						String mark = prece.replaceAll(reg, "");
						String number = MoneyNumberFormat.getThousandsByRegex(prece.replace(mark, ""), 2);
						payPriceStr = payPriceStr+number + mark+"+";
					}
					nbb.setPayPrice(payPriceStr.substring(0,payPriceStr.length()-1));
				}
			}
		}
		
		if (teamlist.size() > 0) {
			for (NewBorrowingBean nbb : teamlist) {
				if (nbb.getLendPrice() != null && !"".equals(nbb.getLendPrice())) {
					nbb.setLendPrice(MoneyNumberFormat.getThousandsByRegex(nbb.getLendPrice(), 2));
				}
			}
		}
		
		if (borrowList.size() > 0) {
			for (NewBorrowingBean nbb : borrowList) {
				if (nbb.getBorrowPrices() != null && !"".equals(nbb.getBorrowPrices())) {
					nbb.setBorrowPrices(MoneyNumberFormat.getThousandsByRegex(nbb.getBorrowPrices(), 2));
				}
			}
		}
		
		model.addAttribute("refundDate", blist.get(0).getRefundDate());
		model.addAttribute("productType", request.getParameter("productType"));
		model.addAttribute("productOrder", productOrder);
		model.addAttribute("product", product);
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", DictUtils.getDictLabel(String.valueOf(productOrder.getPayStatus()),"order_pay_status",""));
		model.addAttribute("orderId", orderId);
		model.addAttribute("flowType", Context.REVIEW_FLOWTYPE_BORROWMONEY);
		model.addAttribute("tralist", tralist);
		model.addAttribute("teamlist",teamlist);
		model.addAttribute("borrowList", borrowList);
		model.addAttribute("totalsize", borrowList.size());
		model.addAttribute("reviewId", reviewId);
		model.addAttribute("rid", reviewId);
		
		List<Currency> currencyList = currencyService.findCurrencyList(companyId);
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("spaceGradelist", DictUtils.getDictList("spaceGrade_Type"));// 舱位等级
		model.addAttribute("airspacelist", DictUtils.getDictList("airspace_Type"));// 舱位
		model.addAttribute("fromAreas", areaService.findFromCityList(""));// 出发城市
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
		model.addAttribute("airlineList", airlineInfoService.getAirlineInfoList(companyId));

		return "review/borrowing/singlegroup/singlegroupLendMoneyDetail";
	}
		
	/**
	 * 根据订单编号，查询新行者借款订单的明细
	 * @param orderId
	 * @return
	 */
	public Map<String, Object> queryAirticketOrderDetailById(String orderId){
		Map<String, Object> orderDetailInfoMap = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId);
		
		return orderDetailInfoMap;
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
		model.addAttribute("office", UserUtils.getUser().getCompany());
		return "review/borrowing/singlegroup/singlegroupLendMoneyList";
	}
	
	/**
	 * 流程互斥校验
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value="beforeAddLendMoneyApply")
	public String beforeAddLendMoneyApply(HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException {
//		String orderId = request.getParameter("orderId");
//		int validate = airTicketOrderLendMoneyService.validateProcess(Integer.parseInt(orderId));
		String result = "";
		//注释掉暂时取消互斥
//		if(validate>0){
//		 result = "还有未完成申请"; 
//		}   
		return result;
	}
	
	/**
	 * 新行者单团类订单借款列表
	 * @author songyang 2015年11月11日15:50:59
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "borrowAmountList")
	public String borrowAmountList( HttpServletRequest request, HttpServletResponse response, Model model, String orderId,Integer flowType,Integer productType) {
		ProductOrderCommon productOrder = orderService.getProductorderById(Long.valueOf(orderId));
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		Long deptId = product.getDeptId();
		List<NewBorrowingBean> reviewList  = new ArrayList<NewBorrowingBean>();
		if(null!=deptId){
			List<Map<String, Object>> processList =  processReviewService.getReviewDetailMapListByOrderId(deptId, productType, Context.REVIEW_FLOWTYPE_BORROWMONEY,productOrder.getId().toString() ,OrderByPropertiesType.CREATE_DATE,OrderByDirectionType.DESC);
			reviewList = getBorrowingBeanListnew(processList);
			for(NewBorrowingBean borr :reviewList){
				if(borr.getTravelerId().contains(NewBorrowingBean.REGEX)||"0".equals(borr.getTravelerId())){
					borr.setTravelerName("团队");
				}
				if(StringUtils.isNotBlank(borr.getCurrencyIds())&&borr.getCurrencyIds().contains(NewBorrowingBean.REGEX)){
					String compPrice = "";
					if(StringUtils.isNotBlank(borr.getCurrencyMarks())&&StringUtils.isNotBlank(borr.getBorrowPrices())){
						String[] cMarks = borr.getCurrencyMarks().split(NewBorrowingBean.REGEX);	
						String[] cPrices = borr.getBorrowPrices().split(NewBorrowingBean.REGEX);
						for(int i=0;i<cMarks.length;i++){
							compPrice+=cMarks[i]+cPrices[i]+"+";
						}
						borr.setCurrencyIds(compPrice.substring(0, compPrice.length()-1));
					}
					
				}else{
					    borr.setCurrencyIds(borr.getCurrencyMarks()+borr.getBorrowPrices());
				}
			}
		
			Collections.reverse(reviewList);
		}
		model.addAttribute("processList", reviewList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("productType", productType);
		return "review/borrowing/singlegroup/singlegroupborrowAmountList";
	}
	/**
	 * 组装BorrowingBean对象
	 */
//	private List<NewBorrowingBean> getBorrowingBeanList(
//			List<Map<String, String>> reviewMapList) {
//		List<NewBorrowingBean> aList = new ArrayList<NewBorrowingBean>();
//		if (null == reviewMapList || reviewMapList.isEmpty()) {
//			return aList;
//		}
//		for (Map<String, String> map : reviewMapList) {
//			aList.add(new NewBorrowingBean(null));
//		}
//		return aList;
//	}
//	
	
	/**
	 * by sy  2015年11月3日13:49:23
	 * 
	 * 组装BorrowingBean对象
	 */
	private List<NewBorrowingBean> getBorrowingBeanListnew(
			List<Map<String, Object>> reviewMapList) {
		List<NewBorrowingBean> aList = new ArrayList<NewBorrowingBean>();
		if (null == reviewMapList || reviewMapList.isEmpty()) {
			return aList;
		}
		for (Map<String, Object> map : reviewMapList) {
			aList.add(new NewBorrowingBean(map));
		}
		return aList;
	}

	/**
	 * by  sy  2015年11月4日15:25:31
	 * 借款撤销申请
	 */
	@ResponseBody
	@RequestMapping(value = "backBorrowAmount")
	public String backBorrowAmount( HttpServletRequest request) {
		String rid = request.getParameter("id");
		Integer status = Integer.parseInt(request.getParameter("status"));
		Long userCompanyId = UserUtils.getUser().getCompany().getId();
		ReviewResult r = null;
		String msg = "";
		if(status!=1){
			msg="当前状态不许撤销";
		    return msg;
		}
			
		r =processReviewService.back(UserUtils.getUser().getId().toString(), userCompanyId.toString(), "", rid,"",null, null);
		
		if(r.getSuccess()){
			msg="撤销成功";
		}else if(!r.getSuccess()){
			msg="撤销失败";
		}
		
        return msg;
	}

	
	

	/**
	 *by 宋扬 2015年11月20日15:41:29
	 *取消申请
	 */
	@ResponseBody
	@RequestMapping(value = "cancelBorrowAmount")
	public String cancelBorrowAmount( HttpServletRequest request) {
		String rid = request.getParameter("id");
		Integer status = Integer.parseInt(request.getParameter("status"));
		ReviewResult r = null;
		String msg = "";
		if(status!=1){
			msg="当前状态不许取消";
		    return msg;
		}
			
		r =processReviewService.cancel(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid().toString(), "", rid, "", null);
		
		if(r.getSuccess()){
			msg="取消申请成功";
		}else if(!r.getSuccess()){
			msg="取消申请失败";
		}
		
        return msg;
	}
	
	
	
	/**
	 * 流程互斥
	 * add by songyang 二〇一五年十二月十一日 15:44:12
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping(value="lendMoneyApplyCheck")
	public Map<String , String > lendMoneyApplyCheck(HttpServletRequest request, HttpServletResponse response,Model model) throws Exception{
		Map<String , String > jsonMap = new HashMap<String, String>();
		String data = "";
		Boolean isGroup = true;
		String travellerId = "0";
		List<String> paramNamesList = new ArrayList<String>();
 		paramNamesList.add("travelerId");
		List<String> paramList = new ArrayList<String>();
		//借款审批数据组装成MAP
		Map<String,String> mapReview = NewBorrowingBean.exportDetail4RequestMap(request, paramNamesList,paramList,"lendPrice");
		String[] travels = mapReview.get("travelerId").split("#_");
//		if(travels!=null&&travels.length>0){
//			if(travels.length>1){
//				travellerId="0";
//			}else if(travels.length==1 && ("0").equals(travels[0])){
//				travellerId="0";
//			}else{
//				travellerId=travels[0];
//				isGroup = false;
//			}
//		}
		
		List<String> travelsList = new ArrayList<String>();
		for(String traveStr : travels){
			travelsList.add(traveStr);
		}
		String orderId = request.getParameter("orderId");
		String singleorderType = request.getParameter("singleorderType");
		CommonResult  commonResult =  reviewMutexService.check(orderId, travelsList, singleorderType, Context.REVIEW_FLOWTYPE_BORROWMONEY.toString());
//		CommonResult  commonResult =  reviewMutexService.check(orderId, travellerId, singleorderType, Context.REVIEW_FLOWTYPE_BORROWMONEY.toString(), isGroup);
		if(commonResult.getSuccess()){
			jsonMap.put("result", "0");
		}else{
			jsonMap.put("message", commonResult.getMessage());
		}
		return jsonMap;
	}
	
	/**
	 * 流程申请
	 * by sy 2015年11月4日13:49:52
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@ResponseBody
	@RequestMapping(value="LendMoneyApply")
	public String LendMoneyApply(HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException {
		String orderId = request.getParameter("orderId");
		String reviewId = request.getParameter("reviewId");
		//流程变量表储存数据
		Map<String, Object> variables = new HashMap<String, Object>();
		String msg="";
		String travelerId="0";
		List<String> paramNamesList = new ArrayList<String>();
 		paramNamesList.add("travelerId");
		paramNamesList.add("travelerName");
		paramNamesList.add("currencyId");
		paramNamesList.add("currencyName");
		paramNamesList.add("currencyMark");
		paramNamesList.add("currencyExchangerate");
		paramNamesList.add("payPrice");
		paramNamesList.add("lendPrice");
		paramNamesList.add("remark");
		paramNamesList.add("lendName");
		paramNamesList.add(Context.REVIEW_VARIABLE_KEY_REFUND_DATE);
		List<String> paramList = new ArrayList<String>();
		paramList.add("currencyIds");
		paramList.add("currencyNames");
		paramList.add("currencyMarks");
		paramList.add("borrowPrices");
		int singleorderType = Integer.parseInt(request.getParameter("singleorderType"));
		Map<String, Object> map =new HashMap<String,Object>();
		//借款审批数据组装成MAP
		Map<String,String> mapReview = NewBorrowingBean.exportDetail4RequestMap(request, paramNamesList,paramList,"lendPrice");
		if(request.getParameter("borrowPrices") != null&& request.getParameter("currencyIds")!= null ){
			float  fc = currencyConverter(request.getParameter("borrowPrices"),request.getParameter("currencyIds"));
			mapReview.put("currencyConverter", fc+"");
		}
		if(request.getParameter("borrowRemark") != null&& request.getParameter("borrowRemark")!= null ){
			mapReview.put("borrowRemark", request.getParameter("borrowRemark"));
		}
		if(request.getParameter("currencyExchangerates") != null ){
			mapReview.put("currencyExchangerates",request.getParameter("currencyExchangerates"));
		}
		
		if (StringUtils.isEmpty(reviewId)) {
			reviewId = "0";
		}
		
		ProductOrderCommon productOrder = orderService.getProductorderById(Long.valueOf(orderId));
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		
		Long deptId = product.getDeptId();
		if(deptId == null){
			return msg="部门为空！";
		}

		//travelerId 一个游客 --游客ID 多个游客--0
		String[] travels = mapReview.get("travelerId").split("#_");
		if(travels!=null&&travels.length>0){
			if(travels.length>1){
				travelerId="0";
			}else if(travels.length==1 && ("0").equals(travels[0])){
				travelerId="0";
			}else{
				travelerId=travels[0];
				variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID,travels[0]);
			}
		}
		
		
		//借款审批数据全部放到变量中
		variables.putAll(mapReview);
		if(("0").equals(travelerId)){
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, "团队");
		}else{
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, mapReview.get("travelerName"));
		}
		variables.put(REVIEW_VARIABLE_KEY_ORDER_ID, orderId);
		variables.put(REVIEW_VARIABLE_KEY_PRODUCT_TYPE, "1");
		variables.put(REVIEW_VARIABLE_KEY_PRODUCT_ID,product.getId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO,productOrder.getOrderNum());
//		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, product.getGroupOpenCode());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_ID, productGroup.getId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, productGroup.getGroupCode());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, product.getCreateBy().getId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR,product.getCreateBy().getId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, product.getAcitivityName());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER,productOrder.getSalerId());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME,product.getAcitivityName());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT, productOrder.getOrderCompany());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT_NAME, productOrder.getOrderCompanyName());
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, singleorderType);
		variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_1, mapReview.get("travelerId").toString().replaceAll("#_", ","));
		
		//把游客ID循环放到list里面
		List<String> travellerIsList = new ArrayList<String>();
		for(String travellerStr : travels){
			travellerIsList.add(travellerStr);
		}
		
		//判断流程互斥是否是团队
		Boolean  isGroup  = false; 
		if(("0").equals(travelerId)){
			isGroup = true;
		}
		CommonResult commonResult = null;
		//流程互斥判断
//		if(travels.length>1){
			 commonResult = reviewMutexService.check(orderId, travellerIsList, request.getParameter("singleorderType"), Context.REVIEW_FLOWTYPE_BORROWMONEY.toString());
//		}else{
//			 commonResult = reviewMutexService.check(orderId, travelerId,request.getParameter("singleorderType"), Context.REVIEW_FLOWTYPE_BORROWMONEY.toString(), isGroup);
//		}
		
		//如果不互斥
		if(commonResult.getSuccess()){
			ReviewResult result=null;
			try {
				//发送流程申请
				result = processReviewService.start(UserUtils.getUser().getId().toString(),UserUtils.getUser().getCompany().getUuid().toString(),userReviewPermissionChecker,null,singleorderType,Context.REVIEW_FLOWTYPE_BORROWMONEY,product.getDeptId(),"",variables);				
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}
			
			//流程ID
			String  rid = result.getReviewId();
			
			//返回结果
			Boolean rResult = result.getSuccess(); 
			
			if(rResult){
				//审批通过时候的业务数据操作
//				if(result.getReviewStatus()==2){
//					map = newOrderReviewService.reviewBorrowing(result.getReviewId(), 1, request);
//				}
				NewProcessMoneyAmount newProcessMoneyAmount = new NewProcessMoneyAmount();
				String[] currencyIds = mapReview.get("currencyIds").split(NewBorrowingBean.REGEX);
				String[] borrowPrices = mapReview.get("borrowPrices").split(NewBorrowingBean.REGEX);
				String[] currencyExchangerates = mapReview.get("currencyExchangerates").split(NewBorrowingBean.REGEX);
				for (int i = 0; i < currencyIds.length; i++) {
					//审批申请金额保存到新审批流程金额表
					newProcessMoneyAmount.setId(UuidUtils.generUuid()); 
					newProcessMoneyAmount.setCurrencyId(Integer.parseInt(currencyIds[i]));
					newProcessMoneyAmount.setAmount(new BigDecimal(borrowPrices[i]));
					newProcessMoneyAmount.setMoneyType(Context.REVIEW_FLOWTYPE_BORROWMONEY);
					newProcessMoneyAmount.setOrderType(singleorderType);
					newProcessMoneyAmount.setCreatedBy(UserUtils.getUser().getId());
					newProcessMoneyAmount.setCreateTime(new Date());
					newProcessMoneyAmount.setExchangerate(new BigDecimal(currencyExchangerates[i]));
					newProcessMoneyAmount.setReviewId(rid);
					newProcessMoneyAmount.setCompanyId(UserUtils.getUser().getCompany().getId().toString());
					processMoneyAmountService.saveNewProcessMoneyAmount(newProcessMoneyAmount);

					if(result.getReviewStatus() == 2) {
						MoneyAmount moneyAmount = new MoneyAmount();
						moneyAmount.setSerialNum(UUID.randomUUID().toString());
						moneyAmount.setCurrencyId(Integer.parseInt(currencyIds[i]));
						moneyAmount.setAmount(new BigDecimal(borrowPrices[i]));
						moneyAmount.setUid(Long.parseLong(orderId));
						moneyAmount.setMoneyType(12);
						moneyAmount.setOrderType(singleorderType);
						moneyAmount.setBusindessType(1);
						moneyAmount.setExchangerate(new BigDecimal(request.getParameter("currencyExchangerates")));
						moneyAmount.setReviewUuid(rid);
						moneyAmountService.saveOrUpdateMoneyAmount(moneyAmount);
					}
				}
//				msg="借款申请成功!";
				msg="applySuccess";
			} else{
				if(ReviewErrorCode.ERROR_CODE_MSG_CN.containsKey(result.getCode())){
					msg=ReviewErrorCode.ERROR_CODE_MSG_CN.get(result.getCode());
				}
			}
		}else{
			msg=commonResult.getMessage()+"，不能发起申请！";
		}
		return msg;
	}

	
	/**
	 * 其他币种转换成人民币
	 */
	@ResponseBody
	public float currencyConverter(String count,String currencyId)
	{
		Long userCompanyId = UserUtils.getUser().getCompany().getId();
		String [] ct= count.split("#");
		String [] ci= currencyId.split("#");
		double totalMoney =0;
		for(int i=0;i<ct.length;i++)
		{
			if(StringUtils.isNotBlank(ct[i])){
				String ctStr = ct[i];
				if(ctStr.indexOf(",")>-1){
					ctStr = ct[i].replaceAll(",", "");
				}
				//String ctStr = ct[i].replaceAll(",", "");
				StringBuffer buffer = new StringBuffer();
				buffer.append("SELECT c.currency_id,c.create_company_id,c.convert_lowest FROM currency c WHERE c.currency_id=?");
				//buffer.append(ci[i]);
				buffer.append(" AND c.create_company_id=");
				buffer.append(userCompanyId);
				List<Map<String, Object>> list = visaOrderDao.findBySql(buffer.toString(), Map.class,ci[i]);
				Map<String, Object>  mp =  list.get(0);
				totalMoney= totalMoney +Double.parseDouble((mp.get("convert_lowest").toString()))*Double.parseDouble(ctStr);
			}
			
		}
		 BigDecimal   b   =   new   BigDecimal(totalMoney); 
		return b.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue();
	}
	/**
	 * add by 宋扬  2015年11月5日11:20:57
	 * add date 2015-05-12
	 * 审批借款申请信息
	 */
	/**
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	/**
	 * @param response
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="reviewPlaneBorrowingInfo")
	public String reviewPlaneBorrowingInfo(HttpServletResponse response,
	        Model model, HttpServletRequest request){
		String reviewId = request.getParameter("rid");
		String orderId = request.getParameter("orderId");
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
//		Map<String, Object> orderDetail = queryAirticketOrderDetailById(orderId);//查询订单明细
		ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		List<Map<String,Object>> rdlist = new ArrayList<Map<String,Object>>();
		Map<String,Object>  processMap = null;
		try{
			if(reviewId!=null){
				processMap = processReviewService.getReviewDetailMapByReviewId(reviewId);
				rdlist.add(processMap);
			}
		}catch(Exception e){
			log.error("根据reviewid： " + reviewId + " 查询出来reviewDetail明细报错 ",e);
		}
		//将总金额和币种显示出来
		List<Map<String, Object>> borrowPricesList = new ArrayList<Map<String, Object>>();
		Map<String, Object> borrowPricesMap = new HashMap<String, Object>();
		rdlist.get(0).get("borrowPrices");
		rdlist.get(0).get("borrowRemark");

		if (rdlist != null && rdlist.size() > 0) {
			for (int i = 0; i < rdlist.size(); i++) {
				if (rdlist.get(i).containsKey("borrowPrices")) {
					borrowPricesMap.put("borrowPrices", rdlist.get(i).get("borrowPrices").toString());
				}
				if (rdlist.get(i).containsKey("currencyMarks")) {
					borrowPricesMap.put("currencyMarks", rdlist.get(i).get("currencyMarks").toString());
				}
				if (rdlist.get(i).containsKey("borrowRemark")) {
					model.addAttribute("borrowRemark", rdlist.get(i).get("borrowRemark"));
				}
			}
		}
		borrowPricesList.add(borrowPricesMap);
		List<NewBorrowingBean> blist = NewBorrowingBean
				.transferReviewDetail2BorrowingBeanMap(rdlist);
		List<NewBorrowingBean> tralist = new ArrayList<NewBorrowingBean>();
		List<NewBorrowingBean> teamlist = new ArrayList<NewBorrowingBean>();
		List<NewBorrowingBean> borrowList = NewBorrowingBean
				.transferReviewDetail2BorrowingBeanMap(borrowPricesList);

		// 将blist 拆分为团队借款和游客借款两部分
		if (blist != null && blist.size() > 0) {
			int size = blist.size();
			for (int i = 0; i < size; i++) {
				NewBorrowingBean bean = blist.get(i);
				if ("0".equals(bean.getTravelerId())) {// 团队借款
					teamlist.add(bean);
				} else {// 游客借款
					tralist.add(bean);
				}
			}
		}

		if (reviewId != null && !"".equals(reviewId)) {// 显示动态审批的标志
			List<ReviewLogNew> rLog = processReviewService.getReviewLogByReviewId(reviewId);
			model.addAttribute("rLog", rLog);
		}
		// if(review!=null){
		// model.addAttribute("applyDate", review.getCreateDate());
		// }
		model.addAttribute("refundDate", blist.get(0).getRefundDate());
		model.addAttribute("payModeStr", 
				DictUtils.getDictLabel(String.valueOf(productOrder.getPayStatus()), "order_pay_status", ""));
		model.addAttribute("orderStatusStr", 
				OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		model.addAttribute("tralist", tralist);
		model.addAttribute("teamlist", teamlist);
		model.addAttribute("borrowList", borrowList);
		model.addAttribute("totalsize", borrowList.size());
		model.addAttribute("reviewId", reviewId);
		List<Currency> currencyList = currencyService.findCurrencyList(companyId);
		// model.addAttribute("orderDetail", orderDetail);
		model.addAttribute("productOrder", productOrder);
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("product", product);
		model.addAttribute("productGroup", productGroup);
		model.addAttribute("orderId", orderId);
		model.addAttribute("spaceGradelist", DictUtils.getDictList("spaceGrade_Type"));// 舱位等级
		model.addAttribute("airspacelist", DictUtils.getDictList("airspace_Type"));// 舱位
		model.addAttribute("fromAreas", areaService.findFromCityList(""));// 出发城市
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
		model.addAttribute("airlineList", airlineInfoService.getAirlineInfoList(companyId));

		model.addAttribute("rid",reviewId);
//		model.addAttribute("roleId",roleId);
//		model.addAttribute("userLevel",userLevel);
//		return "review/airticket/borrowing/newreviewPlaneBorrowingInfo";
		return "review/borrowing/common/newreviewSingleGroupBorrowingInfo";
	}
	
	

}
