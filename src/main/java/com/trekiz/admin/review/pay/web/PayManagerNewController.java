package com.trekiz.admin.review.pay.web;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.entity.ReviewLogNew;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.query.service.ISelectService;
import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.agent.utils.AgentInfoUtils;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.cost.repository.CostRecordDao;
import com.trekiz.admin.modules.finance.util.ExcelExportCommonUtils;
import com.trekiz.admin.modules.finance.util.MoneyUtils;
import com.trekiz.admin.modules.hotel.entity.HotelMoneyAmount;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.hotel.query.HotelMoneyAmountQuery;
import com.trekiz.admin.modules.hotel.service.HotelMoneyAmountService;
import com.trekiz.admin.modules.hotel.service.HotelOrderService;
import com.trekiz.admin.modules.island.entity.IslandMoneyAmount;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.query.IslandMoneyAmountQuery;
import com.trekiz.admin.modules.island.service.IslandMoneyAmountService;
import com.trekiz.admin.modules.island.service.IslandOrderService;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.repository.MoneyAmountDao;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.entity.Refund;
import com.trekiz.admin.modules.order.pojo.OrderPayDetail;
import com.trekiz.admin.modules.order.pojo.OrderPayInput;
import com.trekiz.admin.modules.order.rebates.repository.RebatesDao;
import com.trekiz.admin.modules.order.service.ProductOrderService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.reviewflow.entity.ReviewNewExtend;
import com.trekiz.admin.modules.reviewflow.repository.ReviewDao;
import com.trekiz.admin.modules.reviewflow.service.ReviewLogService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.CurrencyUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.TravelerUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.review.common.service.ICommonReviewService;
import com.trekiz.admin.review.common.utils.ReviewUtils;
import com.trekiz.admin.review.pay.service.PayManagerNewService;
import com.trekiz.admin.review.rebates.singleGroup.service.RebatesNewService;

/**
 * 退款、返佣、借款的付款功能 controller
 * 
 * @author Administrator
 * @version v1.0 2015-5-13 20:15:17
 */
@Controller
@RequestMapping(value = "${adminPath}/costNew/payManager")
public class PayManagerNewController extends BaseController {
	
	private static final String PAGE_P = "pageP";

	@Autowired
	PayManagerNewService payManagerService;

	@Autowired
	private AgentinfoService agentinfoService;

	@Autowired
	private CurrencyService currencyService;

	@Autowired
	IslandOrderService islandOrderService;
	
	@Autowired
	HotelOrderService hotelOrderService;
	
	@Autowired
	private ProductOrderService productOrderService;
	@Autowired
	private MoneyAmountService moneyAmountService;

	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	
	@Autowired
	private VisaOrderService visaOrderService;
	
	@Autowired
	private HotelMoneyAmountService hotelMoneyAmountService;
	
	@Autowired
	private IslandMoneyAmountService islandMoneyAmountService;
	
	@Autowired
	private VisaProductsService visaProductsService;
	
	@Autowired
	private ReviewService reviewService;

	@Autowired
	private ICommonReviewService commonReviewService;
	@Autowired
	private RebatesNewService rebatesNewService;
	@Autowired
	private ReviewDao reviewDao;
	@Autowired
	private MoneyAmountDao moneyAmountDao;
	@Autowired
	private RebatesDao rebatesDao;
	@Autowired
	private CostRecordDao costRecordDao;
	@Autowired
	private ISelectService selectService;
	@Autowired
	private ReviewLogService reviewLogService;
	
	
	/**
	 * 查询支付列表
	 * 
	 * @param payType
	 *            201表示退款， 202 表示返佣 ，203表示借款
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "payList/{payType}")
	public String findPayList(@PathVariable String payType,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		//申请日期 付款列表加申请日期的筛选条件
		model.addAttribute("createTimeMin",request.getParameter("createTimeMin"));
		model.addAttribute("createTimeMax",request.getParameter("createTimeMax"));
		// 得到公司ID  奢华之旅：75895555346a4db9a96ba9237eae96a5
		Office company = UserUtils.getUser().getCompany();
		String companyUuid = company.getUuid();
		model.addAttribute("companyUuid", companyUuid);
		// C460V5 只针对环球行，签证团号还原成订单表的团号
		model.addAttribute("isHQX", Context.SUPPLIER_UUID_HQX.equals(companyUuid));
		//拉美途
		model.addAttribute("isLMT", Context.SUPPLIER_UUID_LAMEITOUR.equals(companyUuid));
		// 起航假期
		model.addAttribute("isQHJQ", Context.SUPPLIER_UUID_QHJQ.equals(companyUuid));
		// 返回路径值 根据需求写自己的
		String returnPath = Context.BLANK_STR;
		if (Context.PAY_REFUND.equals(payType)) {// 退款
			Map<String, Object> params = prepareParams(request);
			Page<Map<String, Object>> pageP = new Page<Map<String, Object>>(request, response);
			params.put(PAGE_P, pageP);
			params.put(Context.PAY_TYPE_DESC, payType);
			Page<Map<String, Object>> page = payManagerService.getReviewPayList(params);

			Long companyId = company.getId();
			// 下单人
			List<Map<String, Object>> creatorList = UserUtils.getSalers(companyId);
			model.addAttribute(Context.CREATOR_LIST, creatorList);
			model.addAttribute(Context.PAGE, page);
			model.addAttribute(Context.PARAMS, params);
			// 计调
			List<Map<String, Object>> jdList = UserUtils.getOperators(companyId);
			model.addAttribute(Context.JD_LIST, jdList);
			// 销售
			List<Map<String, Object>> salerList = UserUtils.getSalers(companyId);
			model.addAttribute(Context.SALER_LIST, salerList);
			List<Dict> dicts = new ArrayList<Dict>();
			if(company.getName().contains(Context.OFFICE_EFX) || company.getName().contains(Context.OFFICE_JZFX)){
				Dict dictAll = new Dict();
				dictAll.setValue(Context.ZERO_DESC);
				dictAll.setLabel(Context.ProductType.PRODUCT_ALL_DES);
				Dict dictHotel = new Dict();
				dictHotel.setValue(Context.ProductType.PRODUCT_HOTEL.toString());
				dictHotel.setLabel(Context.ProductType.PRODUCT_HOTEL_DESC);
				Dict dictIsland = new Dict();
				dictIsland.setValue(Context.ProductType.PRODUCT_ISLAND.toString());
				dictIsland.setLabel(Context.ProductType.PRODUCT_ISLAND_DESC);
				dicts.add(dictAll);
				dicts.add(dictHotel);
				dicts.add(dictIsland);
			} else {
				dicts = DictUtils.getDictList(Context.ORDER_TYPE);
			}
			model.addAttribute(Context.DICTS, dicts);
			//渠道
//	        model.addAttribute("agentinfoList", AgentInfoUtils.getAgentList(companyId));
	        model.addAttribute("agentinfoList", AgentInfoUtils.getQuauqAndOwnAgentList(companyId));
			//来款银行 yudong.xu
			model.addAttribute("fromBanks", selectService.getFromBanks(companyId.intValue()));
			returnPath = Context.PATH_NEW_REFUND_PAY_LIST;
		} else if (Context.PAY_BORROW.equals(payType)) {//借款
			borrowMoneyList(request, response, model);
			returnPath = "review/pay/borrowMoney";
		}else if(Context.PAY_REBATE.equals(payType)){ // 返佣
			rebateCon(payType, request, response, model);
			returnPath = "review/pay/rebatePayList";
		}
		return returnPath;

	}

	private void rebateCon(String payType, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		// 查询条件
		Map<String, Object> mapRequest = new HashMap<String, Object>();
		mapRequest.put("groupCode", request.getParameter("groupCode"));
		mapRequest.put("orderTypes", request.getParameter("orderTypes"));
		mapRequest.put("agents", request.getParameter("agents"));
		mapRequest.put("jds", request.getParameter("jds"));
		mapRequest.put("payStatus", request.getParameter("payStatus"));
		mapRequest.put("currency", request.getParameter("currency"));
		mapRequest.put("startMoney", request.getParameter("startMoney"));
		mapRequest.put("endMoney", request.getParameter("endMoney"));
		mapRequest.put("creators", request.getParameter("creators"));
		mapRequest.put("salers", request.getParameter("salers"));
		mapRequest.put("payType", payType);
		mapRequest.put("printFlag", request.getParameter("printFlag"));
		mapRequest.put("travelers", request.getParameter("travelers"));
		//409需求，添加来款银行、支付方式、出纳确认时间筛选项  xianglei.dong
		mapRequest.put("fromBankName", request.getParameter("fromBankName"));
		mapRequest.put("payMode", request.getParameter("payMode"));
		mapRequest.put("cashierConfirmDateBegin", request.getParameter("cashierConfirmDateBegin"));
		mapRequest.put("cashierConfirmDateEnd", request.getParameter("cashierConfirmDateEnd"));
		mapRequest.put("paymentType", request.getParameter("paymentType"));//渠道结算方式
		//0477需求，添加申请日期筛选条件 yang.wang 2016.7.25
		mapRequest.put("createTimeMin", request.getParameter("createTimeMin"));
		mapRequest.put("createTimeMax", request.getParameter("createTimeMax"));

		model.addAttribute("groupCode", request.getParameter("groupCode"));
		model.addAttribute("orderTypes", request.getParameter("orderTypes"));
		model.addAttribute("agents", request.getParameter("agents"));
		model.addAttribute("jds", request.getParameter("jds"));
		model.addAttribute("payStatus", request.getParameter("payStatus"));
		model.addAttribute("currency", request.getParameter("currency"));
		model.addAttribute("startMoney", request.getParameter("startMoney"));
		model.addAttribute("endMoney", request.getParameter("endMoney"));
		model.addAttribute("creators", request.getParameter("creators"));
		model.addAttribute("salers", request.getParameter("salers"));
		model.addAttribute("travelers", request.getParameter("travelers"));
		model.addAttribute("printFlag", request.getParameter("printFlag"));
		model.addAttribute("paymentType", request.getParameter("paymentType"));//渠道结算方式
		model.addAttribute("companyId", companyId);

		model.addAttribute("printFlag", request.getParameter("printFlag"));
		// 订单类型
		model.addAttribute("orderTypeList", DictUtils.getDict2List("order_type"));
		// 渠道商
//		model.addAttribute("agentList", AgentInfoUtils.getAgentList(companyId));
		model.addAttribute("agentList", AgentInfoUtils.getQuauqAndOwnAgentList(companyId));
		// 计调
		List<Map<String, Object>> jdList = UserUtils.getOperators(companyId);
		model.addAttribute("jdList", jdList);
		// 币种
		model.addAttribute("currencyList", currencyService.findCurrencyList(companyId));
		// 下单人
		List<Map<String, Object>> creatorList = UserUtils.getSalers(companyId);
		model.addAttribute("creatorList", creatorList);
		// 销售
		List<Map<String, Object>> salerList = UserUtils.getSalers(companyId);
		model.addAttribute("salerList", salerList);
		//409需求，添加来款银行、支付方式、出纳确认时间筛选项	 xianglei.dong
		model.addAttribute("fromBankName", request.getParameter("fromBankName"));
		model.addAttribute("fromBankNames", selectService.getFromBanks(UserUtils.getUser().getCompany().getId().intValue()));
		model.addAttribute("payMode", request.getParameter("payMode"));
		model.addAttribute("cashierConfirmDateBegin", request.getParameter("cashierConfirmDateBegin"));
		model.addAttribute("cashierConfirmDateEnd", request.getParameter("cashierConfirmDateEnd"));
		//0477需求，添加申请日期筛选条件 yang.wang 2016.7.25
		model.addAttribute("createTimeMin", request.getParameter("createTimeMin"));
		model.addAttribute("createTimeMax", request.getParameter("createTimeMax"));
		
		Page<Map<String, Object>> page = payManagerService.findRebateList(mapRequest, request, response);
		model.addAttribute("page", page);
	}

	/**
	 * 整理参数
	 * 
	 * @param request
	 * @return
	 */
	private Map<String, Object> prepareParams(HttpServletRequest request) {

		Map<String, Object> params = new HashMap<String, Object>();
		// 团号
		params.put(Context.RefundPayParams.GROUP_CODE, request.getParameter(Context.RefundPayParams.GROUP_CODE));
		// 产品类型
		params.put(Context.RefundPayParams.PRODUCT_TYPE, request.getParameter(Context.RefundPayParams.PRODUCT_TYPE));
		// 币种id
		params.put(Context.RefundPayParams.CURRENCY_ID, request.getParameter(Context.RefundPayParams.CURRENCY_ID));
		// 钱数范围 1
		params.put(Context.RefundPayParams.C_AMOUNT_START, request.getParameter(Context.RefundPayParams.C_AMOUNT_START));
		// 钱数范围 2
		params.put(Context.RefundPayParams.C_AMOUNT_END, request.getParameter(Context.RefundPayParams.C_AMOUNT_END));
		// 供应商id
//		params.put(Context.RefundPayParams.COMPANY_ID, request.getParameter(Context.RefundPayParams.COMPANY_ID));
		// 渠道id
		String agentId = request.getParameter(Context.RefundPayParams.AGENT_ID);
		params.put(Context.RefundPayParams.AGENT_ID, agentId == null || Context.BLANK_STR.equals(agentId.trim()) ? Context.ALL_VALUE : agentId);
		// 下单人id
		String creator = request.getParameter(Context.RefundPayParams.CREATOR_ID);
		params.put(Context.RefundPayParams.CREATOR_ID, creator== null || Context.BLANK_STR.equals(creator.trim()) ? Context.ALL_VALUE : creator);
		// 销售id
		String salerId = request.getParameter(Context.RefundPayParams.SALER_ID);
		params.put(Context.RefundPayParams.SALER_ID, salerId == null || Context.BLANK_STR.equals(salerId.trim()) ? Context.ALL_VALUE : salerId);
		// 计调id
		String operatorId = request.getParameter(Context.RefundPayParams.JD_ID);
		params.put(Context.RefundPayParams.JD_ID, operatorId == null || Context.BLANK_STR.equals(operatorId.trim()) ? Context.ALL_VALUE : operatorId);
		// 支付状态
		params.put(Context.RefundPayParams.PAY_STATUS, request.getParameter(Context.RefundPayParams.PAY_STATUS));
		// 打印状态
		params.put(Context.RefundPayParams.PRINT_STATUS, request.getParameter(Context.RefundPayParams.PRINT_STATUS));
		// 支付的款项
		params.put(Context.RefundPayParams.PAY_REFUND_TYPE, request.getParameter(Context.RefundPayParams.PAY_REFUND_TYPE));
		//支付方式
		params.put(Context.RefundPayParams.PAY_MODE, request.getParameter(Context.RefundPayParams.PAY_MODE));
		//来款银行
		params.put(Context.RefundPayParams.FROM_BANK, request.getParameter(Context.RefundPayParams.FROM_BANK));
		//出纳确认时间开始
		params.put(Context.RefundPayParams.CASHIER_CONFIRM_DATE_BEGIN, request.getParameter(Context.RefundPayParams.CASHIER_CONFIRM_DATE_BEGIN));
		//出纳确认时间结束
		params.put(Context.RefundPayParams.CASHIER_CONFIRM_DATE_END, request.getParameter(Context.RefundPayParams.CASHIER_CONFIRM_DATE_END));
		//渠道结算方式
		params.put("paymentType", request.getParameter("paymentType"));
		//申请日期
		params.put("createTimeMin", request.getParameter("createTimeMin"));
		params.put("createTimeMax", request.getParameter("createTimeMax"));
		return params;
	}

	/**
	 * 支付款项
	 * 
	 * @param payType
	 *            201表示退款， 202 表示返佣 ，203表示借款
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "pay/{payType}")
	public String pay(@PathVariable("payType") String payType,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		
			//调用支付接口的参数
			List<OrderPayDetail> orderPays = new ArrayList<OrderPayDetail>();
			OrderPayDetail orderPayDetail = new OrderPayDetail();
			Long reviewId = null;
			String reviewIdStr = request.getParameter("reviewId");
			String travelerId = request.getParameter("travelerId");
			String orderType = request.getParameter("orderType");
			String currencyId = request.getParameter("currencyId");
			String payPrice = request.getParameter("payPrice");
			String flowType = request.getParameter("flowType");
			if(StringUtils.isNotBlank(reviewIdStr)){
				ReviewNew reviewNew = reviewService.getReview(reviewIdStr);
				if(reviewNew != null){
					reviewId = reviewNew.getIdLong();
					orderPayDetail.setProjectId(reviewId);
				}
			}
			if(reviewId == null){
				throw new RuntimeException("无效的审核id");
			}
			if(StringUtils.isNotBlank(travelerId)){
				orderPayDetail.setTravelerId(Long.parseLong(travelerId));
			}
			if(StringUtils.isNotBlank(orderType)){
				orderPayDetail.setOrderType(Integer.parseInt(orderType));
			}
			OrderPayInput orderPayInput = new OrderPayInput();
			Integer payTypeInt = 0;
			if("201".equals(payType)){
				if("7".equals(flowType)){
					orderPayDetail.setRefundMoneyType(11);//新的退签证押金
					payTypeInt = 11;
				} else {
					orderPayDetail.setRefundMoneyType(8);//新审核退款付款
					payTypeInt = 8;
				}
				orderPayInput.setRefundMoneyTypeDesc("退款款项");
				orderPayInput.setPaymentListUrl("costNew/payManager/payList/201");
			} else if("202".equals(payType)){
				orderPayDetail.setRefundMoneyType(9);//新的返佣付款
				orderPayInput.setRefundMoneyTypeDesc("返佣款项");
				orderPayInput.setPaymentListUrl("costNew/payManager/payList/202");
				payTypeInt = 9;
			} else if("203".equals(payType)){
				orderPayDetail.setRefundMoneyType(10);//新的借款付款
				orderPayInput.setRefundMoneyTypeDesc("借款款项");
				orderPayInput.setPaymentListUrl("costNew/payManager/payList/203");
				payTypeInt = 10;
				//借款的借款金额数据需根据reviewId进行查询
				if("11".equals(orderType)){//酒店
					List<HotelMoneyAmount> amounts = hotelMoneyAmountService.getMoneyAmonutByReviewUuId(reviewIdStr);
					Map<String, String> map = productOrderService.getTotalCurrencyPriceAndCurrencyId(amounts);
					currencyId = map.get("currencyId");//初始化币种
					payPrice = map.get("currencyPrice");//初始化金额
				} else if("12".equals(orderType)){//海岛游
					List<IslandMoneyAmount> amounts = islandMoneyAmountService.findAmountByReviewUuId(reviewIdStr);
					Map<String, String> map = productOrderService.getTotalCurrencyPriceAndCurrencyId(amounts);
					currencyId = map.get("currencyId");//初始化币种
					payPrice = map.get("currencyPrice");//初始化金额
				} else {//参团
					List<MoneyAmount> amounts = moneyAmountService.findAmountsByReviewUuId(reviewIdStr);
					Map<String, String> map = productOrderService.getTotalCurrencyPriceAndCurrencyId(amounts);
					currencyId = map.get("currencyId");//初始化币种
					payPrice = map.get("currencyPrice");//初始化金额
				}
			} 
			//设置付款币种
			orderPayDetail.setPayCurrencyId(currencyId);
			//计算已付金额 用应付金额减去已付金额 调用付款功能
			if("11".equals(orderType)){//酒店
				List<HotelMoneyAmount> payedmoneys = payManagerService.getHotelPayedMoney(reviewId, payTypeInt);
				if(payedmoneys != null && payedmoneys.size() != 0) {
					String[] curs = currencyId.split(",");
					String[] pays = payPrice.split(",");
					//将金额存入orderpaydetail（orderPayDetail.setPayCurrencyPrice(price2)）
					productOrderService.setOrderPayCurrencyPrice(curs, pays, payedmoneys, orderPayDetail);
				} else {
					orderPayDetail.setPayCurrencyPrice(payPrice);
				}
			} else if("12".equals(orderType)){//海岛游
				List<IslandMoneyAmount> payedmoneys = payManagerService.getIslandPayedMoney(reviewId, payTypeInt);
				if(payedmoneys != null && payedmoneys.size() != 0) {
					String[] curs = currencyId.split(",");
					String[] pays = payPrice.split(",");
					//将金额存入orderpaydetail（orderPayDetail.setPayCurrencyPrice(price2)）
					productOrderService.setOrderPayCurrencyPrice(curs, pays, payedmoneys, orderPayDetail);
				} else {
					orderPayDetail.setPayCurrencyPrice(payPrice);
				}
			} else {
				List<MoneyAmount> payedmoneys = payManagerService.getPayedMoney(reviewId, payTypeInt);
				if(payedmoneys != null && payedmoneys.size() != 0) {
					String[] curs = currencyId.split(",");
					String[] pays = payPrice.split(",");
					//将金额存入orderpaydetail（orderPayDetail.setPayCurrencyPrice(price2)）
					productOrderService.setOrderPayCurrencyPrice(curs, pays, payedmoneys, orderPayDetail);
				} else {
					orderPayDetail.setPayCurrencyPrice(payPrice);
				}
			}
			if(StringUtils.isBlank(payPrice)){
				throw new RuntimeException("错误的款项数据 金额不能是空的");
			}
			orderPayDetail.setReviewId(reviewId);
			/*组织应收价数据 start*/
			String totalCurrencyId = "";
			String totalCurrencyPrice = "";
			String serialNums = "";
			List<MoneyAmount> list = new ArrayList<MoneyAmount>();
			List<HotelMoneyAmount> list2 = new ArrayList<HotelMoneyAmount>();
			List<IslandMoneyAmount> list3 = new ArrayList<IslandMoneyAmount>();
			if("7".equals(request.getParameter("orderType"))){//机票  PS : 暂时不处理切位订单
				AirticketOrder airticketOrder = airTicketOrderService.getAirticketorderById(Long.parseLong(request.getParameter("orderId")));
				serialNums = airticketOrder.getTotalMoney();
				list = moneyAmountService.findAmountBySerialNum(serialNums);
			} else if("6".equals(request.getParameter("orderType"))){//签证
				VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(request.getParameter("orderId")));
				serialNums = visaOrder.getTotalMoney();
				list = moneyAmountService.findAmountBySerialNum(serialNums);
			} else if("11".equals(request.getParameter("orderType"))){//酒店
				HotelOrder hotelOrder = hotelOrderService.getById(Integer.parseInt(request.getParameter("orderId")));
				serialNums = hotelOrder.getTotalMoney();
				HotelMoneyAmountQuery hotelMoneyAmountQuery = new HotelMoneyAmountQuery();
				hotelMoneyAmountQuery.setSerialNum(serialNums);
				list2 = hotelMoneyAmountService.find(hotelMoneyAmountQuery);
			} else if("12".equals(request.getParameter("orderType"))){//海岛游
				IslandOrder islandOrder = islandOrderService.getById(Integer.parseInt(request.getParameter("orderId")));
				serialNums = islandOrder.getTotalMoney();
				IslandMoneyAmountQuery islandMoneyAmountQuery = new IslandMoneyAmountQuery();
				islandMoneyAmountQuery.setSerialNum(serialNums);
				list3 = islandMoneyAmountService.find(islandMoneyAmountQuery);
			} else {//参团
				ProductOrderCommon productOrderCommon = productOrderService.getProductorderById(Long.parseLong(request.getParameter("orderId")));
				serialNums = productOrderCommon.getTotalMoney();
				list = moneyAmountService.findAmountBySerialNum(serialNums);
			}
			if(list != null && list.size() > 0){
				//参团的
				Map<String, String> map = productOrderService.getTotalCurrencyPriceAndCurrencyId(list);
				totalCurrencyId += map.get("currencyId");
				totalCurrencyPrice += map.get("currencyPrice");
			}
			if(list2 != null && list2.size() > 0){
				//酒店
				Map<String, String> map = productOrderService.getTotalCurrencyPriceAndCurrencyId(list2);
				totalCurrencyId += map.get("currencyId");
				totalCurrencyPrice += map.get("currencyPrice");
			}
			if(list3 != null && list3.size() > 0){
				//海岛游
				Map<String, String> map = productOrderService.getTotalCurrencyPriceAndCurrencyId(list3);
				totalCurrencyId += map.get("currencyId");
				totalCurrencyPrice += map.get("currencyPrice");
			}
			/*组织应收价数据 end*/
			orderPayDetail.setTotalCurrencyId(totalCurrencyId);
			orderPayDetail.setTotalCurrencyPrice(totalCurrencyPrice);
			
			orderPays.add(orderPayDetail);
			
			
			if(request.getParameter("agentId") == null || "".equals(request.getParameter("agentId"))){
				throw new RuntimeException("渠道不能为空");
			}
			orderPayInput.setAgentId(Integer.parseInt(request.getParameter("agentId")));
			orderPayInput.setOrderPayDetailList(orderPays);
			orderPayInput.setPayType("2");
			orderPayInput.setTotalCurrencyFlag(true);//设置显示总金额 add by chy 2015年7月22日14:03:32 根据 bug 需求变更 bug号6086
			orderPayInput.setMoneyFlag(1);//显示金额 切可编辑
//			orderPayInput.setAgentId(request.getParameter("agentId") == null ? null : Integer.parseInt(request.getParameter("agentId")));
			
			//add by shijun.liu 因C162需求，付款之后，按更新时间排序要到第一位
			orderPayInput.setServiceClassName("com.trekiz.admin.review.pay.service.PayManagerNewService");
			orderPayInput.setServiceAfterMethodName("updateReviewUpdateDate");
			request.setAttribute("pay", orderPayInput);
			return "forward:"+Global.getAdminPath()+"/orderPayMore/pay";
	}
	
	/**
	 * 根据reviewId查询该记录是否存在<br>
	 * 若存在，修改显示状态；若不存在，创建记录写入数据库表中，并修改显示状态。
	 * @param reviewId 
	 * */
	@ResponseBody
	@RequestMapping(value = "removeShowRemark")
	public Map<String, Object> removeShowRemark(String reviewId) {
		
		Map<String, Object> map = new HashMap<>();
		List<ReviewNewExtend> list = reviewLogService.findByReviewId(reviewId);
		ReviewNewExtend rne = null;
		if (list == null || list.size() == 0) {
			rne = new ReviewNewExtend();
			rne.setDelFlag(0);
			rne.setReviewId(reviewId);
			rne.setIsShowRemark(1);
			rne.setCompany(UserUtils.getUser().getCompany().getUuid());
			reviewLogService.save(rne);
		} 
		
		try {
			reviewLogService.removeIsShowRemark(reviewId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		map.put("flag", true);
		return map;
	}
	
	/**
	 * 借款列表数据查询
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @author shijun.liu
	 */
	private void borrowMoneyList(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Map<String, String> params = new HashMap<String, String>();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		String groupCode = request.getParameter("groupCode"); 		// 团号
		String orderType = request.getParameter("orderType"); 		// 订单类型
		String operatorId = request.getParameter("operatorId"); 	// 计调
		String payStatus = request.getParameter("payStatus"); 		// 付款状态
		String currencyId = request.getParameter("currencyId"); 	// 币种ID
		String moneyBegin = request.getParameter("moneyBegin"); 	// 开始金额
		String moneyEnd = request.getParameter("moneyEnd"); 		// 结束金额
		String printStatus = request.getParameter("printStatus"); 	// 打印状态
		String createTimeMin = request.getParameter("createTimeMin");     //申请日期开始
		String createTimeMax = request.getParameter("createTimeMax");     //申请日期结束
		if (StringUtils.isNotBlank(groupCode)) {
			params.put("groupCode", groupCode.trim());
		}
		params.put("orderType", orderType);
		if (StringUtils.isNotBlank(operatorId)) {
			params.put("operatorId", operatorId);
		}
		if (StringUtils.isNotBlank(payStatus)) {
			params.put("payStatus", payStatus);
		}
		if (StringUtils.isNotBlank(currencyId)) {
			params.put("currencyId", currencyId);
		}
		if (StringUtils.isNotBlank(moneyBegin)) {
			params.put("moneyBegin", moneyBegin.trim());
		}
		if (StringUtils.isNotBlank(moneyEnd)) {
			params.put("moneyEnd", moneyEnd.trim());
		}
		if (StringUtils.isNotBlank(printStatus)) {
			params.put("printStatus", printStatus);
		}
		if (StringUtils.isNotBlank(createTimeMin)) {
			params.put("createTimeMin", createTimeMin);
		}
		if (StringUtils.isNotBlank(createTimeMax)) {
			params.put("createTimeMax", createTimeMax);
		}
		Page<Map<String, Object>> page = payManagerService.findBorrowMoneyList(
				params, new Page<Map<String, Object>>(request, response));
		
		// 548&549 增加审批备注信息 modify by wangyang 2016.12.5
		for (Map<String, Object> map : page.getList()) {
			
			Integer isShowRemark = 1;
			List<ReviewNewExtend> list = reviewLogService.findByReviewId(map.get("reviewId").toString());
			if (list != null && list.size() > 0) {
				isShowRemark = list.get(0).getIsShowRemark();
			} 
			map.put("isShowRemark", isShowRemark);
			map.put("hasRemarks", false);
			
			if (isShowRemark == 0) {
				continue;
			}
			
			String reviewId = map.get("reviewId").toString(); // 获取审批id
			Integer	reviewFlag = Integer.parseInt(map.get("reviewflag").toString()); // 判断新/旧审批数据
			
			// 新审批数据
			if (reviewFlag == 2 && StringUtils.isNotBlank(reviewId)) {
				List<ReviewLogNew> logs = ReviewUtils.obtainReviewLogs(reviewId);
				for (ReviewLogNew log : logs) {
					if (StringUtils.isNotBlank(log.getRemark())) {
						map.put("hasRemarks", true);
						map.put("logs", logs);
						break;
					}
				}
			}
			
			// 旧审批数据
//			if (reviewFlag == 1 && StringUtils.isNotBlank(reviewId)) {
//				List<ReviewLog> logs = reviewLogService.getReviewLogsByReviewId(Long.parseLong(reviewId));
//				for (ReviewLog log : logs) {
//					if (StringUtils.isNotBlank(log.getRemark())) {
//						map.put("hasRemarks", true);
//						map.put("logs", logs);
//					}
//				}
//				
//			}
		}
		model.addAttribute("groupCode", groupCode);
		model.addAttribute("orderTypeValue", orderType);
		model.addAttribute("operatorId", operatorId);
		model.addAttribute("payStatus", payStatus);
		model.addAttribute("currencyId", currencyId);
		model.addAttribute("moneyBegin", moneyBegin);
		model.addAttribute("moneyEnd", moneyEnd);
		model.addAttribute("printStatus", printStatus);
		model.addAttribute("createTimeMin", createTimeMin);
		model.addAttribute("createTimeMax", createTimeMax);
		model.addAttribute("orderTypeCombox", DictUtils.getDictList("order_type"));// 团队类型
		model.addAttribute("operator", UserUtils.getOperators(UserUtils.getUser().getCompany().getId()));// 计调
		model.addAttribute("currencyList", currencyService.findCurrencyList(
				UserUtils.getUser().getCompany().getId()));// 币种
		if(Context.SUPPLIER_UUID_XXZ.equals(companyUuid)){
			model.addAttribute("isXXZ", true);
		}
		model.addAttribute("page", page);
	}
	
	/**
	 * 专门用于环球行供应商签证借款
	 * @param request
	 * @param response
	 * @param model
	 * @author shijun.liu
	 */
	@RequestMapping("borrowMoneyForTTSQZ/{payType}")
	public String borrowMoneyForTTSQZ(@PathVariable("payType") String payType, HttpServletRequest request, 
			HttpServletResponse response, Model model){
		String groupCode = request.getParameter("groupCode");
		String visaType = request.getParameter("visaType");
		String payStatus = request.getParameter("payStatus");
		String reportDateStart = request.getParameter("reportDateStart");
		String reportDateEnd = request.getParameter("reportDateEnd");
		String visaContry = request.getParameter("visaContry");
		String salerId = request.getParameter("salerId");
		String operatorId = request.getParameter("operatorId");
		String moneyBegin = request.getParameter("moneyBegin");
		String moneyEnd = request.getParameter("moneyEnd");
		String printStatus = request.getParameter("printStatus");
		String createTimeMin = request.getParameter("createTimeMin");   //申请日期开始
		String createTimeMax = request.getParameter("createTimeMax");   //申请日期结束
		//环球行签证借款均为人民币，此参数不作为sql条件
		String currencyId = request.getParameter("currencyId");
		Map<String, String> params = new HashMap<String, String>();
		if (StringUtils.isNotBlank(groupCode)) {
			params.put("groupCode", groupCode.trim());
		}
		if (StringUtils.isNotBlank(visaType)) {
			params.put("visaType", visaType);
		}
		if (StringUtils.isNotBlank(payStatus)) {
			params.put("payStatus", payStatus);
		}
		if (StringUtils.isNotBlank(reportDateStart)) {
			params.put("reportDateStart", reportDateStart);
		}
		if (StringUtils.isNotBlank(reportDateEnd)) {
			params.put("reportDateEnd", reportDateEnd);
		}
		if (StringUtils.isNotBlank(visaContry)) {
			params.put("visaContry", visaContry);
		}
		if (StringUtils.isNotBlank(salerId)) {
			params.put("salerId", salerId);
		}
		if (StringUtils.isNotBlank(operatorId)) {
			params.put("operatorId", operatorId);
		}
		if (StringUtils.isNotBlank(moneyBegin)) {
			params.put("moneyBegin", moneyBegin.trim());
		}
		if (StringUtils.isNotBlank(moneyEnd)) {
			params.put("moneyEnd", moneyEnd.trim());
		}
		if (StringUtils.isNotBlank(printStatus)) {
			params.put("printStatus", printStatus);
		}
		if (StringUtils.isNotBlank(createTimeMin)) {
			params.put("createTimeMin", createTimeMin);
		}
		if (StringUtils.isNotBlank(createTimeMax)) {
			params.put("createTimeMax", createTimeMax);
		}
		Long companyId = UserUtils.getUser().getCompany().getId();
		Page<Map<String, Object>> page = payManagerService.borrowMoneyForTTSQZ(
				params, new Page<Map<String, Object>>(request, response));
		
		// 548&549 增加审批备注信息 modify by wangyang 2016.12.5
		for (Map<String, Object> map : page.getList()) {
			
			Integer isShowRemark = 1;
			List<ReviewNewExtend> list = reviewLogService.findByReviewId(map.get("review_uuid").toString());
			if (list != null && list.size() > 0) {
				isShowRemark = list.get(0).getIsShowRemark();
			} 
			map.put("isShowRemark", isShowRemark);
			map.put("hasRemarks", false);
					
			if (isShowRemark == 0) {
				continue;
			}
			
			String reviewId = map.get("review_uuid").toString(); // 获取审批id
			Integer	reviewFlag = Integer.parseInt(map.get("reviewflag").toString()); // 判断新/旧审批数据
					
			// 新审批数据
			if (reviewFlag == 2 && StringUtils.isNotBlank(reviewId)) {
				List<ReviewLogNew> logs = ReviewUtils.obtainReviewLogs(reviewId);
				for (ReviewLogNew log : logs) {
					if (StringUtils.isNotBlank(log.getRemark())) {
						map.put("hasRemarks", true);
						map.put("logs", logs);
						break;
					}
				}
			}
		}
		
		model.addAttribute("page", page);
		model.addAttribute("groupCode", groupCode);
		model.addAttribute("visaType", visaType);
		model.addAttribute("payStatus", payStatus);
		model.addAttribute("reportDateStart", reportDateStart);
		model.addAttribute("reportDateEnd", reportDateEnd);
		model.addAttribute("visaContry", visaContry);
		model.addAttribute("salerId", salerId);
		model.addAttribute("operatorId", operatorId);
		model.addAttribute("moneyBegin", moneyBegin);
		model.addAttribute("moneyEnd", moneyEnd);
		model.addAttribute("printStatus", printStatus);
		model.addAttribute("createTimeMin", createTimeMin);
		model.addAttribute("createTimeMax", createTimeMax);
		model.addAttribute("currencyId", currencyId);
		model.addAttribute("operatorList",agentinfoService.findInnerJd(companyId));// 计调
		List<Currency> list = currencyService.findCurrencyList(companyId);
		List<Currency> cnyList = new ArrayList<Currency>();
		// 环球行只有人民币金额，不会出现其他币种，所以前端下拉框只显示人民币
		for (Currency cny : list) {
			if("人民币".equals(cny.getCurrencyName())){
				cnyList.add(cny);
				break;
			}
		}
		model.addAttribute("currencyList",cnyList);// 只列出人民币
		//签证类型
		model.addAttribute("visaTypeList", DictUtils.getSysDicMap("new_visa_type"));
		//签证国家
		List<Object[]> countryObject = visaProductsService.findCountryInfoList();
		Map<String, String> countryList = new HashMap<String, String>();
		for (Object[] props : countryObject) {
			countryList.put(String.valueOf(props[0]), String.valueOf(props[1]));
		}
		model.addAttribute("countryList", countryList);
		//销售
		model.addAttribute("salerList", agentinfoService.findInnerSales(companyId));
		return "review/pay/borrowMoneyforTTSQZ";
	}
	
	/**
	 * 专门针对环球行供应商的签证付款
	 * @param request
	 * @param response
	 * @param model
	 * @author shijun.liu
	 * @return
	 */
	@RequestMapping("borrowMoneyPayForTTSQZ")
	public String borrowMoneyPayForTTSQZ(HttpServletRequest request, 
			HttpServletResponse response, Model model){
		List<OrderPayDetail> orderPays = new ArrayList<OrderPayDetail>();
		OrderPayDetail orderPayDetail = new OrderPayDetail();
		OrderPayInput orderPayInput = new OrderPayInput();
		Long companyId = UserUtils.getUser().getCompany().getId();
		String orderType = request.getParameter("orderType");
		String batchId = request.getParameter("batchId");
		//应付金额
		String payPrice = request.getParameter("payPrice");
		if(StringUtils.isNotBlank(orderType)){
			orderPayDetail.setOrderType(Integer.parseInt(orderType));
		}
		if(StringUtils.isNotBlank(batchId)){
			orderPayDetail.setProjectId(Long.valueOf(batchId));
		}
		orderPayDetail.setRefundMoneyType(Refund.MONEY_TYPE_BATCHBORROW);
		orderPayInput.setRefundMoneyTypeDesc("签证批量借款款项");
		orderPayInput.setPaymentListUrl("costNew/payManager/borrowMoneyForTTSQZ/203");
		
		String currencyId = null;
		//设置付款币种
		List<Currency> cnyList = currencyService.findCurrencyList(companyId);
		for (Currency cny:cnyList) {
			if("人民币".equals(cny.getCurrencyName())){
				currencyId = String.valueOf(cny.getId());
				break;
			}
		}
		orderPayDetail.setPayCurrencyId(currencyId);
		//查询已付金额 用应付金额减去已付金额 调用付款功能
		List<MoneyAmount> payedMoneys = payManagerService.getPayedMoney(orderPayDetail.getProjectId(), 
				Refund.MONEY_TYPE_BATCHBORROW);
		if(CollectionUtils.isNotEmpty(payedMoneys)){
			MoneyAmount moneyAmount = payedMoneys.get(0);
			if(StringUtils.isNotBlank(payPrice)){
				BigDecimal needPay = new BigDecimal(payPrice).subtract(moneyAmount.getAmount());
				orderPayDetail.setPayCurrencyPrice(needPay.toString());
			}
		}else{
			if(StringUtils.isNotBlank(payPrice)){
				orderPayDetail.setPayCurrencyPrice(payPrice);
			}
		}
		orderPayDetail.setTotalCurrencyId(orderPayDetail.getPayCurrencyId());
		orderPayDetail.setTotalCurrencyPrice(orderPayDetail.getPayCurrencyPrice());
		
		orderPays.add(orderPayDetail);
		orderPayInput.setAgentId(-1);
		orderPayInput.setOrderPayDetailList(orderPays);
		orderPayInput.setPayType("2");
		orderPayInput.setMoneyFlag(1);//显示金额 且可编辑
		//add by shijun.liu 因C162需求，付款之后，按更新时间排序要到第一位
		orderPayInput.setServiceClassName("com.trekiz.admin.review.pay.service.PayManagerNewService");
		orderPayInput.setServiceAfterMethodName("updateBatchBorrowMoneyUpdateTime");
		request.setAttribute("pay", orderPayInput);
		return "forward:"+Global.getAdminPath()+"/orderPayMore/pay";
	}
	
	/**
	 * 环球行签证借款支付记录
	 * @param request
	 * @param response
	 * @param model
	 * @author shijun.liu
	 */
	@RequestMapping("getTTSQZPayRecord")
	public void getTTSQZPayRecord(HttpServletRequest request, 
			HttpServletResponse response, Model model){
		String json = "";
		String batchIdStr = request.getParameter("batchId");
		String orderTypeStr = request.getParameter("orderType");
		if(StringUtils.isNotBlank(batchIdStr) &&
				StringUtils.isNotBlank(orderTypeStr)){
			String temp = payManagerService.getTTSQZPayRecord(Integer.parseInt(batchIdStr),
					Integer.parseInt(orderTypeStr));
			if(null != temp){
				json = temp;
			}
		}
		ServletUtil.print(response, json);
	}
	
	/**
	 * 环球行供应商签证借款-->确认或者取消付款
	 * @param request
	 * @param response
	 * @param model
	 * @author shijun.liu
	 */
	@RequestMapping("confimOrCancelTTSQZPay")
	public void confimOrCancelTTSQZPay(HttpServletRequest request, 
			HttpServletResponse response, Model model){
		String json = "{\"flag\":\"success\"}";
		String batchId = request.getParameter("batchId");
		String payStatus = request.getParameter("payStatus");
		if(StringUtils.isNotBlank(batchId) && StringUtils.isNotBlank(payStatus)){
			String returnValue = payManagerService.confimOrCancelTTSQZPay(Long.valueOf(batchId),
					Integer.parseInt(payStatus));
			if("fail".equals(returnValue)){
				json = "{\"flag\":\"fail\",\"msg\":\"操作失败，请检查\"}";
			}
		}
		ServletUtil.print(response, json);
	}
	
	/**
	 * 借款付款导出游客明细列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author shijun.liu
	 * @date 2016.01.18
	 */
	@RequestMapping(value="downloadTraveler")
	public void downloadTraveler(HttpServletRequest request, HttpServletResponse response, Model model){
		String groupCode = request.getParameter("groupCode");
		String visaType = request.getParameter("visaType");
		String payStatus = request.getParameter("payStatus");
		String reportDateStart = request.getParameter("reportDateStart");
		String reportDateEnd = request.getParameter("reportDateEnd");
		String visaContry = request.getParameter("visaContry");
		String salerId = request.getParameter("salerId");
		String operatorId = request.getParameter("operatorId");
		String moneyBegin = request.getParameter("moneyBegin");
		String moneyEnd = request.getParameter("moneyEnd");
		String printStatus = request.getParameter("printStatus");
		String createTimeMin = request.getParameter("createTimeMin");
		String createTimeMax = request.getParameter("createTimeMax");
		//环球行签证借款均为人民币，此参数不作为sql条件
//		String currencyId = request.getParameter("currencyId");
		Map<String, String> params = new HashMap<String, String>();
		if (StringUtils.isNotBlank(groupCode)) {
			params.put("groupCode", groupCode.trim());
		}
		if (StringUtils.isNotBlank(visaType)) {
			params.put("visaType", visaType);
		}
		if (StringUtils.isNotBlank(payStatus)) {
			params.put("payStatus", payStatus);
		}
		if (StringUtils.isNotBlank(reportDateStart)) {
			params.put("reportDateStart", reportDateStart);
		}
		if (StringUtils.isNotBlank(reportDateEnd)) {
			params.put("reportDateEnd", reportDateEnd);
		}
		if (StringUtils.isNotBlank(visaContry)) {
			params.put("visaContry", visaContry);
		}
		if (StringUtils.isNotBlank(salerId)) {
			params.put("salerId", salerId);
		}
		if (StringUtils.isNotBlank(operatorId)) {
			params.put("operatorId", operatorId);
		}
		if (StringUtils.isNotBlank(moneyBegin)) {
			params.put("moneyBegin", moneyBegin.trim());
		}
		if (StringUtils.isNotBlank(moneyEnd)) {
			params.put("moneyEnd", moneyEnd.trim());
		}
		if (StringUtils.isNotBlank(printStatus)) {
			params.put("printStatus", printStatus);
		}
		if (StringUtils.isNotBlank(createTimeMin)) {
			params.put("createTimeMin", createTimeMin);
		}
		if (StringUtils.isNotBlank(createTimeMax)) {
			params.put("createTimeMax", createTimeMax);
		}
		Workbook workbook = payManagerService.downloadTraveler(params);
		String fileName = "游客列表-" +DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd") + ".xls";
		ServletUtil.downLoadExcel(response, fileName, workbook);
	}

	/**
	 * 获取返佣信息
	 * @param reviewUuid
	 * @return
	 */
	@RequestMapping(value="getRebateInfo")
	@ResponseBody
	public Map<String, Object> getRebateInfo(String reviewUuid, Integer prdtype) {
		Map<String, Object> rebate = payManagerService.getRebateInfo(reviewUuid, prdtype);
		return rebate;
	}

	/**
	 * 获取返佣信息
	 * @param reviewUuid
	 * @return
	 */
	@RequestMapping(value="getRebateInfo2")
	@ResponseBody
	public List<Map<String, Object>> getRebateInfo2(String reviewUuid, Integer prdtype) {
		List<Map<String, Object>> rebates = payManagerService.getRebateInfo2(reviewUuid, prdtype);
		return rebates;
	}
	
	/**
	 * 获取返佣信息
	 * @param reviewUuid
	 * @return
	 */
	@RequestMapping(value="getOldRebateInfo")
	@ResponseBody
	public Map<String, Object> getOldRebateInfo(String reviewUuid, String prdtype) {
		Map<String, Object> rebate = payManagerService.getOldRebateInfo(reviewUuid,prdtype);
		return rebate;
	}

	/**
	 * 获取返佣信息列表
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="getRebatesInfo")
	@ResponseBody
	public List<Map<String, Object>> getRebatesInfo(String ids) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if(StringUtils.isNotBlank(ids)) {
			String[] arr = ids.split(",");
			for(int i = 0; i < arr.length; i++) {
				String idArr = arr[i];
				String[] id = idArr.split("_");
				String reviewUuid = id[0];
				Integer prdtype = Integer.parseInt(id[1]);
				String newOrOld = id[2];
				Map<String, Object> rebate = null;
				if("1".equals(newOrOld)) {
					rebate = payManagerService.getOldRebateInfo(reviewUuid, prdtype.toString());
					list.add(rebate);
				}else if("2".equals(newOrOld)) {
					List<Map<String, Object>> rebates = payManagerService.getRebateList(reviewUuid, prdtype);
					list.addAll(rebates);
				}

			}
		}
		return list;
	}

	/**
	 * 获取退款信息
	 * @param reviewUuid
	 * @return
	 */
	@RequestMapping(value="getRefundInfo")
	@ResponseBody
	public Map<String, Object> getRefundInfo(String reviewUuid) {
		Map<String, Object> refund = payManagerService.getRefundInfo(reviewUuid);
		return refund;
	}

	/**
	 * 获取退款信息
	 * @param reviewUuid
	 * @return
	 */
	@RequestMapping(value="getOldRefundInfo")
	@ResponseBody
	public Map<String, Object> getOldRefundInfo(String reviewUuid) {
		Map<String, Object> refund = payManagerService.getOldRefundInfo(reviewUuid);
		return refund;
	}

	/**
	 * 获取退款信息列表
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="getRefundsInfo")
	@ResponseBody
	public List<Map<String, Object>> getRefundsInfo(String ids) {
		if(ids != null && !"".equals(ids)) {
			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			String[] arr = ids.split(",");
			for(int i = 0; i < arr.length; i++) {
				String idArr = arr[i];
				String[] id = idArr.split("_");
				String reviewUuid = id[0];
				String prdtype = id[1];
				String newOrOld = id[2];
				String payStatus = id[3];
				if(!"1".equals(payStatus)){
					Map<String, Object> refund = null;
					if("1".equals(newOrOld)) {
						refund = payManagerService.getOldRefundInfo(reviewUuid, prdtype);
					}else if("2".equals(newOrOld)) {
						refund = payManagerService.getRefundInfo(reviewUuid);
					}
					list.add(refund);
				}
			}
			return list;
		}
		return null;
	}

	/**
	 * 批量确认付款
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("batchConfirmPay")
	@Transactional
	public String batchConfirmPay(HttpServletRequest request, HttpServletResponse response) {
		try {
			Long userId = UserUtils.getUser().getId();
			JSONArray jsonArray = new JSONArray(request.getParameter("datas"));
			String flag = request.getParameter("flag");
			for(int i = 0; i < jsonArray.length(); i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String uuid = jsonObject.get("uuid").toString();
				String reviewflag = jsonObject.get("reviewflag").toString();
				String orderType = jsonObject.get("orderType").toString();
				String rate = jsonObject.get("rate").toString();
				String price = jsonObject.get("price").toString();
				//出纳确认时间 yudong.xu 2016.05.23
				String payConfirmDateStr = jsonObject.get("cashierConfirmDate").toString();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date payConfirmDate = StringUtils.isBlank(payConfirmDateStr) ? null : sdf.parse(payConfirmDateStr);

				if("202".equals(flag)) {
					if(reviewflag != null && "1".equals(reviewflag)) {
						reviewDao.confirmOrCancelPay(Integer.valueOf(1), userId,
								new Date(), Long.valueOf(uuid),payConfirmDate);
						if(StringUtils.isNotBlank(rate)) {
							if(!"6".equals(orderType) && !"7".equals(orderType)) {
		                		rebatesDao.updateRate(new BigDecimal(rate), Long.parseLong(uuid));
		                	}else{
		                		moneyAmountDao.updateRate(new BigDecimal(rate), Long.parseLong(uuid));
		                	}
							costRecordDao.updateRate(new BigDecimal(rate), Long.parseLong(uuid));
						}
					}else{
						commonReviewService.confimOrCancelPay(uuid, "1",payConfirmDate);
						Long rebatesId = Long.parseLong(jsonObject.get("rebatesId").toString());
						rebatesNewService.updateRebateRate(uuid, rate, price, rebatesId);//返佣
					}
				}else if("201".equals(flag)) {
					if(reviewflag != null && "1".equals(reviewflag)) {
						reviewDao.confirmOrCancelPay(Integer.valueOf(1), userId,
								new Date(), Long.valueOf(uuid),payConfirmDate);
						if(StringUtils.isNotBlank(rate)) {
							moneyAmountDao.updateRate(new BigDecimal(rate), Long.parseLong(uuid));
							costRecordDao.updateRate(new BigDecimal(rate), Long.parseLong(uuid));
						}
					}else{
						commonReviewService.confimOrCancelPay(uuid, "1",payConfirmDate);
						rebatesNewService.updateRefundRate(uuid, rate, price);//退款
					}
					
				}
			}

			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	/**
	 * 579需求：财务模块付款类列表页面，增加Excel导出功能
     * 退款付款列表Excel下载(获取方式和退款付款列表一样)
     * @author gaoyang
     * @Time 2017-3-21 下午3:59:43
     * @param
     */
    @RequestMapping(value="getRefundPayListExcel/{payType}")
    public void getRefundPayListExcel(@PathVariable String payType, HttpServletResponse response, HttpServletRequest request) {
		// 退款
    	// 得到公司ID
		Office company = UserUtils.getUser().getCompany();
		String companyUuid = company.getUuid();
		Map<String, Object> params = prepareParams(request);
		Page<Map<String, Object>> pageP = new Page<Map<String, Object>>(request, response);
		pageP.setPageNo(1);
		pageP.setMaxSize(Integer.MAX_VALUE);
		params.put(PAGE_P, pageP);
		params.put(Context.PAY_TYPE_DESC, payType);
		Page<Map<String, Object>> page = payManagerService.getReviewPayList(params);
        // 获取列表查询结果
    	List<Map<String, Object>> list = page.getList();
    	int i = 0;
    	try {
	    	// 特殊值处理
	    	for (Map<String, Object> map : list) {
	    		i++;
	    		// 获取列表序号
	    		map.put("count", i);
	    		// 申请日期
	    		String createtime = "";
	    		if (map.get("createtime") != null && !"".equals(map.get("createtime").toString())) {
	    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    		createtime = sdf.format(sdf.parse(map.get("createtime").toString()));
	    		}
	    		map.put("createtime", createtime);
	    		// 获取团队类型及团队名称
	    		String prdtypeName = ""; // 团队类型名称
	    		String productName = ""; // 团队名称
	    		if (map.get("prdtype") != null && !"".equals(map.get("prdtype").toString())) {
	    			// 团队类型
	    			prdtypeName = DictUtils.getDictLabel(map.get("prdtype").toString(), "order_type", "");
	    			// 团队名称
	    			if (!"11".equals(map.get("prdtype").toString()) && !"12".equals(map.get("prdtype").toString())) {
	    				if (map.get("chanpid") != null && !"".equals(map.get("chanpid").toString())) {
	    					productName = OrderCommonUtil.getProductName(map.get("chanpid").toString(), map.get("prdtype").toString());
	    				}
	        		}
	    			if ("11".equals(map.get("prdtype").toString()) || "12".equals(map.get("prdtype").toString())) {
	    				if (map.get("chanpname") != null && !"".equals(map.get("chanpname").toString())) {
	    					productName = map.get("chanpname").toString();
	    				}
	    			}
	    		}
	    		// 团队类型名称
	    		map.put("prdtypeName", prdtypeName);
	    		// 团队名称
	    		map.put("productName", productName);
	    		// 渠道商
	    		String agentName = ""; 
	    		if (map.get("agentid") != null && !"".equals(map.get("agentid").toString())) {
	    			String agentNameStr = AgentInfoUtils.getAgentName(Long.parseLong(map.get("agentid").toString()));
	    			// 315需求,针对越柬行踪，将非签约渠道改为签约渠道
	    			if ("7a81b21a77a811e5bc1e000c29cf2586".equals(companyUuid) && "非签约渠道".equals(agentNameStr)) {
	    				agentName = "直客";
	        		} else {
	        			agentName = agentNameStr;
	        		}
	    		}
	    		map.put("agentName", agentName);
	    		// 款项
	    		String refundName = "";
	    		if (map.get("refundName") != null && !"".equals(map.get("refundName").toString())
	    				&& map.get("flowtype") != null && !"".equals(map.get("flowtype").toString())) {
	    			if ("1".equals(map.get("flowtype").toString())) {
	    				refundName = map.get("refundName").toString();
	    			}
	    			if ("7".equals(map.get("flowtype").toString())) {
	    				refundName = "退签证押金";
	    			}
	    		}
	    		map.put("refundName", refundName);
	    		// 币种名称
	    		String currency = "";
	    		String currencyForPay = ""; // 付款币种
	    		String currencyForPayed = ""; // 已付币种
	    		String mamount = ""; // 付款金额
	    		if (map.get("mcurid") != null && !"".equals(map.get("mcurid").toString())) {
	    			currency = CurrencyUtils.getCurrencyNameOrFlag(Long.parseLong(map.get("mcurid").toString()), "1");
	    		}
	    		// 付款币种
	    		if (map.get("mamount") != null && !"".equals(map.get("mamount").toString())) {
	    			currencyForPay = currency;
	    			DecimalFormat df = new DecimalFormat(",###,##0.00");
	    			mamount = df.format(Double.parseDouble(map.get("mamount").toString()));
	    		}
	    		map.put("currencyForPay", currencyForPay);
	    		map.put("mamount", mamount);
	    		// 已付金额
	    		String payedMoney = "";
	    		if (map.get("reviewflag") != null && map.get("flowtype") != null 
	    				&& map.get("prdtype") != null
	    				&& !"".equals(map.get("prdtype").toString())) {
	    			if ("2".equals(map.get("reviewflag").toString()) && map.get("idlong") != null
		    				&& !"".equals(map.get("idlong").toString())) {
	    				if ("1".equals(map.get("flowtype").toString())) {
	        				payedMoney = OrderCommonUtil.getRefundPayMoney(map.get("idlong").toString(), "8", map.get("prdtype").toString());
	            		}
	        			if ("7".equals(map.get("flowtype").toString())) {
	        				payedMoney = OrderCommonUtil.getRefundPayMoney(map.get("idlong").toString(), "11", map.get("prdtype").toString());
	            		}
	        		}
	    			if ("1".equals(map.get("reviewflag").toString()) && map.get("revid") != null
		    				&& !"".equals(map.get("revid").toString())) {
	        			if ("1".equals(map.get("flowtype").toString())) {
	        				payedMoney = OrderCommonUtil.getRefundPayMoney(map.get("revid").toString(), "2", map.get("prdtype").toString());
	            		}
	        			if ("7".equals(map.get("flowtype").toString())) {
	        				payedMoney = OrderCommonUtil.getRefundPayMoney(map.get("revid").toString(), "5", map.get("prdtype").toString());
	            		}
	        		}
	    		}
	    		// 已付币种
	    		if (StringUtils.isNotBlank(payedMoney)) {
	    			currencyForPayed = currency;
	    			payedMoney = MoneyUtils.getMoneyRemoveCurrency(payedMoney);
	    		}
	    		map.put("currencyForPayed", currencyForPayed);
	    		map.put("payedMoney", payedMoney);
	    		// 获取计调名称
	    		String jidcreateby = "";
	    		if (map.get("jidcreateby") != null && !"".equals(map.get("jidcreateby").toString())) {
	    			jidcreateby = UserUtils.getUserNameByIds(map.get("jidcreateby").toString());
	    		}
	    		map.put("jidcreateby", jidcreateby);
	    		// 获取下单人
	    		String salecreateby = "";
	    		if (map.get("salecreateby") != null && !"".equals(map.get("salecreateby").toString())) {
	    			salecreateby =  UserUtils.getUserNameByIds(map.get("salecreateby").toString());
	    		}
	    		map.put("salecreateby", salecreateby);
	    		
	    		// 出纳确认处理及出纳确认时间
	    		String payConfirmDate = "";
	    		if (map.get("payStatus") != null && "1".equals(map.get("payStatus").toString())) {
	    			map.put("payStatusName", "已付款");
	    			if (map.get("payConfirmDate") != null && !"".equals(map.get("payConfirmDate").toString())) {
	    				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    				payConfirmDate = sdf.format(sdf.parse(map.get("payConfirmDate").toString()));
	    			}
	    		} else {
	    			map.put("payStatusName", "未付款");
	    		}
	    		map.put("payConfirmDate", payConfirmDate);
	    		
	    		// 打印状态处理
	    		if (map.get("printFlag") != null && "1".equals(map.get("printFlag").toString())) {
	    			map.put("printFlagName", "已打印");
	    		} else {
	    			map.put("printFlagName", "未打印");
	    		}
	    	}
			// Excel文件名称
			String fileName = "退款付款";
			// 当导出数据超出65535行时,打包下载, 下面命名zip压缩包的中文名称部分
			String zipChineseName = "退款付款";
			// 表头数据
			String[] secondTitle = new String[] {"序号", "申请日期", "团号", "团队类型", "团队名称",
					"渠道商", "款项", "币种", "付款金额", "币种" , "已付金额",
					"汇率", "计调", "销售", "下单人", "出纳确认", "出纳确认时间", "打印状态"};
			// 每个Map<Object, Object>中的所有键
			String[] commonName = {"count", "createtime", "groupcode", "prdtypeName", "productName",
					"agentName", "refundName", "currencyForPay", "mamount", "currencyForPayed", "payedMoney",
					"rate", "jidcreateby", "salerName", "salecreateby", "payStatusName", "payConfirmDate", "printFlagName"};
			// Excel生成及下载
			ExcelExportCommonUtils.downLoadExcelFileStr(fileName, zipChineseName, secondTitle, commonName, list, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * 579需求：财务模块付款类列表页面，增加Excel导出功能
     * 返佣付款列表Excel下载(获取方式和返佣付款列表一样)
     * @author gaoyang
     * @Time 2017-3-22 下午3:59:43
     * @param
     */
    @RequestMapping(value="getRebatePayListExcel/{payType}")
    public void getRebatePayListExcel(@PathVariable String payType, HttpServletResponse response, HttpServletRequest request) {
    	Office company = UserUtils.getUser().getCompany();
		String companyUuid = company.getUuid();
		// 查询条件
		Map<String, Object> mapRequest = new HashMap<String, Object>();
		mapRequest.put("groupCode", request.getParameter("groupCode"));
		mapRequest.put("orderTypes", request.getParameter("orderTypes"));
		mapRequest.put("agents", request.getParameter("agents"));
		mapRequest.put("jds", request.getParameter("jds"));
		mapRequest.put("payStatus", request.getParameter("payStatus"));
		mapRequest.put("currency", request.getParameter("currency"));
		mapRequest.put("startMoney", request.getParameter("startMoney"));
		mapRequest.put("endMoney", request.getParameter("endMoney"));
		mapRequest.put("creators", request.getParameter("creators"));
		mapRequest.put("salers", request.getParameter("salers"));
		mapRequest.put("payType", payType);
		mapRequest.put("printFlag", request.getParameter("printFlag"));
		mapRequest.put("travelers", request.getParameter("travelers"));
		//409需求，添加来款银行、支付方式、出纳确认时间筛选项  xianglei.dong
		mapRequest.put("fromBankName", request.getParameter("fromBankName"));
		mapRequest.put("payMode", request.getParameter("payMode"));
		mapRequest.put("cashierConfirmDateBegin", request.getParameter("cashierConfirmDateBegin"));
		mapRequest.put("cashierConfirmDateEnd", request.getParameter("cashierConfirmDateEnd"));
		mapRequest.put("paymentType", request.getParameter("paymentType"));//渠道结算方式
		//0477需求，添加申请日期筛选条件 yang.wang 2016.7.25
		mapRequest.put("createTimeMin", request.getParameter("createTimeMin"));
		mapRequest.put("createTimeMax", request.getParameter("createTimeMax"));
		mapRequest.put("isExcelExport", "1"); // 设置是否为excel导出
		Page<Map<String, Object>> page = payManagerService.findRebateList(mapRequest, request, response);
        // 获取列表查询结果
    	List<Map<String, Object>> list = page.getList();
    	int i = 0;
    	try {
	    	// 特殊值处理
	    	for (Map<String, Object> map : list) {
	    		i++;
	    		// 获取列表序号
	    		map.put("count", i);
	    		// 申请日期
	    		String createtime = "";
	    		if (map.get("createdate") != null && !"".equals(map.get("createdate").toString())) {
	    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    		createtime = sdf.format(sdf.parse(map.get("createdate").toString()));
	    		}
	    		map.put("createtime", createtime);
	    		// 获取团队类型及团队名称
	    		String prdtypeName = ""; // 团队类型名称
	    		String productName = ""; // 团队名称
	    		if (map.get("prdtype") != null && !"".equals(map.get("prdtype").toString())) {
	    			// 团队类型
	    			prdtypeName = OrderCommonUtil.getChineseOrderType(map.get("prdtype").toString());
	    			// 团队名称
	    			if (!"11".equals(map.get("prdtype").toString()) && !"12".equals(map.get("prdtype").toString())) {
	    				if (map.get("chanpid") != null && !"".equals(map.get("chanpid").toString())) {
	    					productName = OrderCommonUtil.getProductName(map.get("chanpid").toString(), map.get("prdtype").toString());
	    				}
	        		}
	    			if ("11".equals(map.get("prdtype").toString()) || "12".equals(map.get("prdtype").toString())) {
	    				if (map.get("chanpname") != null && !"".equals(map.get("chanpname").toString())) {
	    					productName = map.get("chanpname").toString();
	    				}
	    			}
	    		}
	    		// 团队类型名称
	    		map.put("prdtypeName", prdtypeName);
	    		// 团队名称
	    		map.put("productName", productName);
	    		// 渠道商
	    		String agentName = ""; 
	    		if (map.get("agentid") != null && !"".equals(map.get("agentid").toString())) {
	    			// 315需求,针对越柬行踪，将非签约渠道改为签约渠道
	    			String agentNameStr = AgentInfoUtils.getAgentName(Long.parseLong(map.get("agentid").toString()));
	    			if ("7a81b21a77a811e5bc1e000c29cf2586".equals(companyUuid) && "-1".equals(map.get("agentid").toString())) {
	    				agentName = "直客";
	        		} else {
	        			agentName = agentNameStr;
	        		}
	    		}
	    		map.put("agentName", agentName);
	    		// 游客
	    		String traveler = "";
	    		if (map.get("travelerid") != null && !"".equals(map.get("travelerid").toString())) {
	    			traveler = TravelerUtils.getTravelerNameById(Long.parseLong(map.get("travelerid").toString()));
	    		} else {
	    			traveler = "---";
	    		}
	    		map.put("traveler", traveler);
	    		// 款项
	    		String costname = "";
	    		// 265需求，针对鼎鸿假，将返佣字段改为宣传费
    			if (map.get("prdtype") != null && "6".equals(map.get("prdtype").toString())) {
    				if ("049984365af44db592d1cd529f3008c3".equals(companyUuid)) {
    					costname = "签证宣传费";
    				} else {
    					costname = "签证返佣";
    				}
    			} else if (map.get("prdtype") != null && "7".equals(map.get("prdtype").toString())) {
    				if ("049984365af44db592d1cd529f3008c3".equals(companyUuid)) {
    					costname = "机票宣传费";
    				} else {
    					costname = "机票返佣";
    				}
    			} else if (map.get("costname") != null) {
    				costname = map.get("costname").toString();
    			}
	    		map.put("costname", costname);
	    		// 币种名称
	    		String currency = "";
	    		String currencyForPay = ""; // 付款币种
	    		String rate = ""; // 汇率
	    		// 付款金额
	    		String payMoney = "";
	    		DecimalFormat df = new DecimalFormat(",###,##0.00");
	    		// 只有一个付款金额就有币种及汇率字段，付款金额前无币种符号
	    		// 如果付款金额为多个就，把币种和汇率置空，多个金额用 " + "关联，并且加上币种符号和汇率 如：¥50.00/1.0000 + $50.00/6.6000
	    		if (map.get("reviewflag") != null) {
	    			if ("1".equals(map.get("reviewflag").toString())) {
	    				if (map.get("currencyId") != null && !"".equals(map.get("currencyId").toString())) {
	    	    			currency = CurrencyUtils.getCurrencyNameOrFlag(Long.parseLong(map.get("currencyId").toString()), "1");
	    	    		}
	    				payMoney = map.get("rebatesDiff").toString();
	    				// 汇率
	    				if (map.get("rate") != null) {
    	    				rate = map.get("rate").toString();
    	    			}
	    			}
	    			if ("2".equals(map.get("reviewflag").toString()) 
	    					&& map.get("reviewUuid") != null 
	    					&& !"".equals(map.get("reviewUuid").toString())) {
	    				List<Map<String, Object>> reviewList = ReviewUtils.getPayedMoney(map.get("reviewUuid").toString());
	    				// 只有一个付款金额的时候有币种及汇率字段，付款金额前无币种符号
	    				if (reviewList.size() == 1) {
	    					if (reviewList.get(0).get("currencyId") != null && !"".equals(reviewList.get(0).get("currencyId").toString())) {
		    	    			currency = CurrencyUtils.getCurrencyNameOrFlag(Long.parseLong(reviewList.get(0).get("currencyId").toString()), "1");
		    	    		}
	    					payMoney = reviewList.get(0).get("rebatesDiff").toString();
	    					// 汇率
	    					if (reviewList.get(0).get("rate") != null) {
	    	    				rate = reviewList.get(0).get("rate").toString(); // 汇率
	    	    			}
		    	        // 多个付款金额的时候
	    				} else if (reviewList.size() > 1) {
	    					for (Map<String, Object> maprev : reviewList) {
	    						String oneCurrency = "";
	    						String onerate = "";
	    						if (maprev.get("currencyId") != null && !"".equals(maprev.get("currencyId").toString())) {
	    							oneCurrency = CurrencyUtils.getCurrencyNameOrFlag(Long.parseLong(maprev.get("currencyId").toString()), "0");
			    	    		}
	    						if (maprev.get("currencyId") != null) {
	    							onerate = maprev.get("rate").toString(); // 汇率
	    						}
		    					payMoney += oneCurrency + df.format(Double.parseDouble(maprev.get("rebatesDiff").toString())) + "/" + onerate + " + ";
		    				}
		    				// 去掉"+"
		    				if (!"".equals(payMoney)) {
		    					payMoney = payMoney.trim().substring(0, payMoney.length() - 2);
		    				}
	    				}
	    			}
	    		}
	    		// 如果付款金额为一个时
	    		if (StringUtils.isNotBlank(payMoney) && payMoney.indexOf("+") == -1) {
	    			currencyForPay = currency;
	    			payMoney = df.format(Double.parseDouble(payMoney));
	    		}
	    		map.put("currencyForPay", currencyForPay);
	    		map.put("payMoney", payMoney);
	    		map.put("rate", rate);
	    		// 已付金额
	    		String payedMoney = "";
	    		String currencyForPayed = ""; // 已付币种(如果只有一个付款金额则已付币种和付款币种相同)
	    		// 只有一个已付金额就有币种及汇率字段，已付金额前无币种符号
	    		// 已付金额为多个的时候去掉币种字段：多个金额用 " + "关联，并且加上币种符号和汇率 如：¥50.00 + $50.00
	    		if (map.get("reviewflag") != null 
	    				&& map.get("revid") != null && !"".equals(map.get("revid").toString())
	    				&& map.get("prdtype") != null && !"".equals(map.get("prdtype").toString())) {
	    			if ("1".equals(map.get("reviewflag").toString())) {
	    				payedMoney = OrderCommonUtil.getRefundPayMoney(map.get("revid").toString(), "3", map.get("prdtype").toString());
	    			}
	    			if ("2".equals(map.get("reviewflag").toString())) {
	        			payedMoney = OrderCommonUtil.getRefundPayMoney(map.get("revid").toString(), "9", map.get("prdtype").toString());
	        		}
	    		}
	    		// 只有一个已付金额
	    		if (StringUtils.isNotBlank(payedMoney) && payedMoney.indexOf("+") == -1) {
	    			currencyForPayed = currency;
	    			payedMoney = MoneyUtils.getMoneyRemoveCurrency(payedMoney);
	    		}
	    		map.put("currencyForPayed", currencyForPayed);
	    		map.put("payedMoney", payedMoney);
	    		// 获取计调名称
	    		String jidcreateby = "";
	    		if (map.get("jidcreateby") != null && !"".equals(map.get("jidcreateby").toString())) {
	    			jidcreateby = UserUtils.getUserNameByIds(map.get("jidcreateby").toString());
	    		}
	    		map.put("jidcreateby", jidcreateby);
	    		// 获取下单人
	    		String salecreateby = "";
	    		if (map.get("salecreateby") != null && !"".equals(map.get("salecreateby").toString())) {
	    			salecreateby =  UserUtils.getUserNameByIds(map.get("salecreateby").toString());
	    		}
	    		map.put("salecreateby", salecreateby);
	    		// 出纳确认处理及出纳确认时间
	    		String payConfirmDate = "";
	    		if (map.get("payStatus") != null) {
	    			if ("1".equals(map.get("payStatus").toString())) {
		    			map.put("payStatusName", "已付");
		    			if (map.get("payConfirmDate") != null && !"".equals(map.get("payConfirmDate").toString())) {
		    				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		    				payConfirmDate = sdf.format(sdf.parse(map.get("payConfirmDate").toString()));
		    			}
		    		} else if ("0".equals(map.get("payStatus").toString())) {
		    			map.put("payStatusName", "未付");
		    		}
	    		}
	    		map.put("payConfirmDate", payConfirmDate);
	    		// 打印状态处理
	    		if (map.get("printFlag") != null && "1".equals(map.get("printFlag").toString())) {
	    			map.put("printFlagName", "已打印");
	    		} else {
	    			map.put("printFlagName", "未打印");
	    		}
	    	}
			// Excel文件名称
			String fileName = "返佣付款";
			// 当导出数据超出65535行时,打包下载, 下面命名zip压缩包的中文名称部分
			String zipChineseName = "返佣付款";
			// 表头数据
			String[] secondTitle = new String[] {"序号", "申请日期", "团号", "团队类型", "团队名称",
					"渠道商", "游客", "款项", "币种", "付款金额", "币种" , "已付金额",
					"汇率", "计调", "销售", "下单人", "出纳确认", "出纳确认时间", "打印状态"};
			// 每个Map<Object, Object>中的所有键
			String[] commonName = {"count", "createtime", "groupcode", "prdtypeName", "productName",
					"agentName", "traveler", "costname", "currencyForPay", "payMoney", "currencyForPayed", "payedMoney",
					"rate", "jidcreateby", "salerName", "salecreateby", "payStatusName", "payConfirmDate", "printFlagName"};
			// Excel生成及下载
			ExcelExportCommonUtils.downLoadExcelFileStr(fileName, zipChineseName, secondTitle, commonName, list, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    /**
     * 579需求：财务模块付款类列表页面，增加Excel导出功能
     * 借款付款列表Excel下载(获取方式和借款付款列表一样)
     * @author gaoyang
     * @Time 2017-3-23 下午2:12:18
     * @param
     */
    @RequestMapping(value="getBorrowMoneyListExcel")
    public void getBorrowMoneyListExcel(HttpServletResponse response, HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		String groupCode = request.getParameter("groupCode"); 		  // 团号
		String orderType = request.getParameter("orderType"); 		  // 订单类型
		String operatorId = request.getParameter("operatorId"); 	  // 计调
		String payStatus = request.getParameter("payStatus"); 		  // 付款状态
		String currencyId = request.getParameter("currencyId"); 	  // 币种ID
		String moneyBegin = request.getParameter("moneyBegin"); 	  // 开始金额
		String moneyEnd = request.getParameter("moneyEnd"); 		  // 结束金额
		String printStatus = request.getParameter("printStatus"); 	  // 打印状态
		String createTimeMin = request.getParameter("createTimeMin"); //申请日期开始
		String createTimeMax = request.getParameter("createTimeMax"); //申请日期结束
		if (StringUtils.isNotBlank(groupCode)) {
			params.put("groupCode", groupCode.trim());
		}
		params.put("orderType", orderType);
		if (StringUtils.isNotBlank(operatorId)) {
			params.put("operatorId", operatorId);
		}
		if (StringUtils.isNotBlank(payStatus)) {
			params.put("payStatus", payStatus);
		}
		if (StringUtils.isNotBlank(currencyId)) {
			params.put("currencyId", currencyId);
		}
		if (StringUtils.isNotBlank(moneyBegin)) {
			params.put("moneyBegin", moneyBegin.trim());
		}
		if (StringUtils.isNotBlank(moneyEnd)) {
			params.put("moneyEnd", moneyEnd.trim());
		}
		if (StringUtils.isNotBlank(printStatus)) {
			params.put("printStatus", printStatus);
		}
		if (StringUtils.isNotBlank(createTimeMin)) {
			params.put("createTimeMin", createTimeMin);
		}
		if (StringUtils.isNotBlank(createTimeMax)) {
			params.put("createTimeMax", createTimeMax);
		}
		Page<Map<String, Object>> pageP = new Page<Map<String, Object>>(request, response);
		pageP.setPageNo(1);
		pageP.setMaxSize(Integer.MAX_VALUE);
		Page<Map<String, Object>> page = payManagerService.findBorrowMoneyList(params, pageP);
        // 获取列表查询结果
    	List<Map<String, Object>> list = page.getList();
    	int i = 0;
    	try {
	    	// 特殊值处理
	    	for (Map<String, Object> map : list) {
	    		i++;
	    		// 获取列表序号
	    		map.put("count", i);
	    		// 申请日期
	    		String createtime = "";
	    		if (map.get("createDate") != null && !"".equals(map.get("createDate").toString())) {
	    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    		createtime = sdf.format(sdf.parse(map.get("createDate").toString()));
	    		}
	    		map.put("createtime", createtime);
	    		// 获取团队类型及团队名称
	    		String prdtypeName = ""; // 团队类型名称
	    		String productName = ""; // 团队名称
	    		if (map.get("orderType") != null && !"".equals(map.get("orderType").toString())) {
	    			// 团队类型
	    			prdtypeName = OrderCommonUtil.getChineseOrderType(map.get("orderType").toString());
	    			// 团队名称
	    			if (!"11".equals(map.get("orderType").toString()) && !"12".equals(map.get("orderType").toString())) {
	    				if (map.get("productId") != null && !"".equals(map.get("productId").toString())) {
	    					productName = OrderCommonUtil.getProductName(map.get("productId").toString(), map.get("orderType").toString());
	    				}
	        		}
	    			if ("11".equals(map.get("orderType").toString()) || "12".equals(map.get("orderType").toString())) {
	    				if (map.get("chanpname") != null && !"".equals(map.get("chanpname").toString())) {
	    					productName = map.get("chanpname").toString();
	    				}
	    			}
	    		}
	    		// 团队类型名称
	    		map.put("prdtypeName", prdtypeName);
	    		// 团队名称
	    		map.put("productName", productName);
	    		// 获取计调名称
	    		String jidcreateby = "";
	    		if (map.get("operatorId") != null && !"".equals(map.get("operatorId").toString())) {
	    			jidcreateby = UserUtils.getUserNameByIds(map.get("operatorId").toString());
	    		}
	    		map.put("jidcreateby", jidcreateby);
	    		// 款项
	    		String costname = "";
	    		if (map.get("orderType") != null && !"".equals(map.get("orderType").toString())) {
	    			costname = OrderCommonUtil.getChineseOrderType(map.get("orderType").toString()) + "借款";
	    		}
	    		map.put("costname", costname);
	    		// 币种名称
	    		String currency = "";
	    		String currencyForPay = ""; // 付款币种
	    		String currencyForPayed = ""; // 已付币种
	    		if (map.get("currencyId") != null && !"".equals(map.get("currencyId").toString())) {
	    			currency = CurrencyUtils.getCurrencyNameOrFlag(Long.parseLong(map.get("currencyId").toString()), "1");
	    		}
	    		// 付款金额
	    		String payMoney = "";
	    		// 已付金额
	    		String payedMoney = "";
	    		if (map.get("reviewflag") != null) {
	    			// 旧审批的借款付款
	    			if ("1".equals(map.get("reviewflag").toString())) {
	    				if (map.get("reviewId") != null && !"".equals(map.get("reviewId").toString())) {
	    					// 付款金额
	    					payMoney = OrderCommonUtil.getMoneyByReviewId(map.get("reviewId").toString(), "0");
	    					// 如果只有一个金额就加上币种字段
	    					if (StringUtils.isNotBlank(payMoney) && payMoney.indexOf("+") == -1) {
	    						payMoney = MoneyUtils.getMoneyRemoveCurrency(payMoney);
	    						currencyForPay = currency;
	    					}
	    					// 已付金额
	    					payedMoney = OrderCommonUtil.getRefundPayMoney(map.get("reviewId").toString(), "4", map.get("orderType").toString());
	    					// 如果只有一个金额就加上币种字段
	    					if (StringUtils.isNotBlank(payedMoney) && payedMoney.indexOf("+") == -1) {
	    						payedMoney = MoneyUtils.getMoneyRemoveCurrency(payedMoney);
	    						currencyForPayed = currency;
	    					}
	    				}
	    			}
	    			// 新审批的借款付款
	    			if ("2".equals(map.get("reviewflag").toString())) {
	    				// 付款金额
	    				if (map.get("reviewId") != null && !"".equals(map.get("reviewId").toString())) {
	    					payMoney = ReviewUtils.getMoneyByReviewUuid(map.get("reviewId").toString(), "0");
	    					// 如果只有一个金额就加上币种字段
	    					if (StringUtils.isNotBlank(payMoney) && payMoney.indexOf("+") == -1) {
	    						payMoney = MoneyUtils.getMoneyRemoveCurrency(payMoney);
	    						currencyForPay = currency;
	    					}
	    				}
	    				// 已付金额
	    				if (map.get("id_long") != null && !"".equals(map.get("id_long").toString())
	    						&& map.get("orderType") != null && !"".equals(map.get("orderType").toString())) {
	    					payedMoney = OrderCommonUtil.getRefundPayMoney(map.get("id_long").toString(), "10", map.get("orderType").toString());
	    					// 如果只有一个金额就加上币种字段
	    					if (StringUtils.isNotBlank(payedMoney) && payedMoney.indexOf("+") == -1) {
	    						payedMoney = MoneyUtils.getMoneyRemoveCurrency(payedMoney);
	    						currencyForPayed = currency;
	    					}
	    				}
	    			}
	    		}
	    		map.put("currencyForPay", currencyForPay);
	    		map.put("currencyForPayed", currencyForPayed);
	    		map.put("payMoney", payMoney);
	    		map.put("payedMoney", payedMoney);
	    		// 出纳确认
	    		if (map.get("payStatus") != null) {
	    			if ("1".equals(map.get("payStatus").toString())) {
		    			map.put("payStatusName", "已付");
		    		} else if ("0".equals(map.get("payStatus").toString())) {
		    			map.put("payStatusName", "未付");
		    		}
	    		}
	    		// 打印状态处理
	    		if (map.get("printFlag") != null && "1".equals(map.get("printFlag").toString())) {
	    			map.put("printFlagName", "已打印");
	    		} else {
	    			map.put("printFlagName", "未打印");
	    		}
	    	}
			// Excel文件名称
			String fileName = "借款付款";
			// 当导出数据超出65535行时,打包下载, 下面命名zip压缩包的中文名称部分
			String zipChineseName = "借款付款";
			// 表头数据
			String[] secondTitle = new String[] {"序号", "申请日期", "团号", "团队类型", "团队名称",
					"计调", "款项", "币种", "付款金额", "币种" , "已付金额",
					"出纳确认", "打印状态"};
			// 每个Map<Object, Object>中的所有键
			String[] commonName = {"count", "createtime", "groupCode", "prdtypeName", "productName",
					"jidcreateby", "costname", "currencyForPay", "payMoney", "currencyForPayed", "payedMoney",
					"payStatusName", "printFlagName"};
			// Excel生成及下载
			ExcelExportCommonUtils.downLoadExcelFileStr(fileName, zipChineseName, secondTitle, commonName, list, request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}