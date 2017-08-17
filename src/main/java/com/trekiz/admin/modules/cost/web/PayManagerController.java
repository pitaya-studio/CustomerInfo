package com.trekiz.admin.modules.cost.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.cost.service.PayManagerService;
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
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.entity.Refund;
import com.trekiz.admin.modules.order.pojo.OrderPayDetail;
import com.trekiz.admin.modules.order.pojo.OrderPayInput;
import com.trekiz.admin.modules.order.service.ProductOrderService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;

/**
 * 退款、返佣、借款的付款功能 controller
 * 
 * @author Administrator
 * @version v1.0 2015-5-13 20:15:17
 */
@Controller
@RequestMapping(value = "${adminPath}/cost/payManager")
public class PayManagerController extends BaseController {

	@Autowired
	PayManagerService payManagerService;

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
		// 确认付款配置：0-不需要，1-需要
		Integer confirmPay = UserUtils.getUser().getCompany().getConfirmPay();
		model.addAttribute("confirmPay", confirmPay);
		// 返回路径值 根据需求写自己的
		String returnPath = "";
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		if ("201".equals(payType)) {// 退款
			Map<String, Object> params = prepareParams(request);
			Page<Map<String, Object>> pageP = new Page<Map<String, Object>>(
					request, response);
			params.put("pageP", pageP);
			params.put("payType", payType);
			Page<Map<String, Object>> page = payManagerService
					.getReviewPayList(params);
			// 下单人
			List<Map<String, Object>> creatorList = UserUtils.getSalers(UserUtils.getUser().getCompany().getId());
			model.addAttribute("creatorList", creatorList);
			model.addAttribute("page", page);
			model.addAttribute("params", params);
			// 计调
			List<Map<String, Object>> jdList = UserUtils.getOperators(UserUtils.getUser().getCompany().getId());
			model.addAttribute("jdList", jdList);
			// 销售
			List<Map<String, Object>> salerList = UserUtils.getSalers(UserUtils.getUser().getCompany().getId());
			model.addAttribute("salerList", salerList);
			List<Dict> dicts = new ArrayList<Dict>();
			if(UserUtils.getUser().getCompany().getName().contains("俄风行") || UserUtils.getUser().getCompany().getName().contains("九州风行")){
				Dict dictAll = new Dict();
				dictAll.setValue("0");
				dictAll.setLabel("全部");
				Dict dictHotel = new Dict();
				dictHotel.setValue("11");
				dictHotel.setLabel("酒店");
				Dict dictIsland = new Dict();
				dictIsland.setValue("12");
				dictIsland.setLabel("海岛游");
				dicts.add(dictAll);
				dicts.add(dictHotel);
				dicts.add(dictIsland);
			} else {
				dicts = DictUtils.getDictList("order_type");
			}
			model.addAttribute("isConfirm", UserUtils.getUser().getCompany().getConfirmPay());//代表是否需要出纳确认
			model.addAttribute("dicts", dicts);
			returnPath = "modules/order/refundPayList";
		} else if ("203".equals(payType)) {//借款
			borrowMoneyList(request, response, model);
			returnPath = "modules/order/borrowMoney";
		}else if("202".equals(payType)){ // 返佣
			rebateCon(payType, request, response, model);
			returnPath = "modules/order/rebatePayList";
		}
		return returnPath;

	}

	private void rebateCon(String payType, HttpServletRequest request,
			HttpServletResponse response, Model model) {
		String groupCode = request.getParameter("groupCode");
		String orderTypes = request.getParameter("orderTypes");
		String agents = request.getParameter("agents");
		String jds = request.getParameter("jds");
		String payStatus = request.getParameter("payStatus");
		String currency = request.getParameter("currency");
		String startMoney = request.getParameter("startMoney");
		String endMoney = request.getParameter("endMoney");
		String creators = request.getParameter("creators");
		String salers = request.getParameter("salers");
		String printFlag = request.getParameter("printFlag");
		String payedMoney = request.getParameter("payedMoney");
		
		// 查询条件
		Map<String, Object> mapRequest = new HashMap<String, Object>();
		mapRequest.put("groupCode", groupCode);
		mapRequest.put("orderTypes", orderTypes);
		mapRequest.put("agents", agents);
		mapRequest.put("jds", jds);
		mapRequest.put("payStatus", payStatus);
		mapRequest.put("currency", currency);
		mapRequest.put("startMoney", startMoney);
		mapRequest.put("endMoney", endMoney);
		mapRequest.put("creators", creators);
		mapRequest.put("salers", salers);
		mapRequest.put("payType", payType);
		mapRequest.put("printFlag", printFlag);
		mapRequest.put("payedMoney", payedMoney);
		
		model.addAttribute("groupCode", groupCode);
		model.addAttribute("orderTypes", orderTypes);
		model.addAttribute("agents", agents);
		model.addAttribute("jds", jds);
		model.addAttribute("payStatus", payStatus);
		model.addAttribute("currency", currency);
		model.addAttribute("startMoney", startMoney);
		model.addAttribute("endMoney", endMoney);
		model.addAttribute("creators", creators);
		model.addAttribute("salers", salers);
		model.addAttribute("printFlag", printFlag);
		model.addAttribute("salers", salers);
		model.addAttribute("payedMoney", payedMoney);
		
		model.addAttribute("companyId", UserUtils.getUser().getCompany().getId());
		model.addAttribute("name", UserUtils.getUser().getCompany().getName());

		// 订单类型
		model.addAttribute("orderTypeList", DictUtils.getDictList("order_type"));
		// 渠道商
		model.addAttribute("agentList", agentinfoService.findAllAgentinfo(UserUtils.getUser().getCompany().getId()));
		// 计调
		List<Map<String, Object>> jdList = UserUtils.getOperators(UserUtils.getUser().getCompany().getId());
		model.addAttribute("jdList", jdList);
		// 付款状态
//			model.addAttribute("payStatus", request.getParameter("payStatus"));
		// 币种
		model.addAttribute("currencyList", currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId()));
		// 下单人
		List<Map<String, Object>> creatorList = UserUtils.getSalers(UserUtils.getUser().getCompany().getId());
		model.addAttribute("creatorList", creatorList);
		// 销售
		List<Map<String, Object>> salerList = UserUtils.getSalers(UserUtils.getUser().getCompany().getId());
		model.addAttribute("salerList", salerList);
		
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
		params.put("groupCode", request.getParameter("groupCode"));
		// 产品类型
		params.put("prdType", request.getParameter("prdType"));
		// 币种id
		params.put("currencyid", request.getParameter("currencyid"));
		// 钱数范围 1
		params.put("cAmountStart", request.getParameter("cAmountStart"));
		// 钱数范围 2
		params.put("cAmountEnd", request.getParameter("cAmountEnd"));
		// 供应商id
		params.put("companyId", request.getParameter("companyId"));
		// 渠道id
		params.put("agentId", request.getParameter("agentId")== null || "".equals(request.getParameter("agentId").trim()) ? "-99999" : request.getParameter("agentId"));
		// 下单人id
		params.put("creator", request.getParameter("creator")== null || "".equals(request.getParameter("creator").trim()) ? "-99999" : request.getParameter("creator"));
		// 销售id
		params.put("saler", request.getParameter("saler")== null || "".equals(request.getParameter("saler").trim()) ? "-99999" : request.getParameter("saler"));
		// 计调id
		params.put("jdsaler", request.getParameter("jdsaler")== null || "".equals(request.getParameter("jdsaler").trim()) ? "-99999" : request.getParameter("jdsaler"));
		// 支付状态
		params.put("payStatus", request.getParameter("payStatus"));
		// 已付金额
		params.put("payedStatus", request.getParameter("payedStatus"));
		// 打印状态
		params.put("printStatus", request.getParameter("printStatus"));
		// 支付的款项
		params.put("payType2", request.getParameter("payType2"));
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
				reviewId = Long.parseLong(reviewIdStr);
				orderPayDetail.setProjectId(reviewId);
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
					orderPayDetail.setRefundMoneyType(5);
					payTypeInt = 5;
				} else {
					orderPayDetail.setRefundMoneyType(2);
					payTypeInt = 2;
				}
				orderPayInput.setRefundMoneyTypeDesc("退款款项");
				orderPayInput.setPaymentListUrl("cost/payManager/payList/201");
			} else if("202".equals(payType)){
				orderPayDetail.setRefundMoneyType(3);
				orderPayInput.setRefundMoneyTypeDesc("返佣款项");
				orderPayInput.setPaymentListUrl("cost/payManager/payList/202");
				payTypeInt = 3;
			} else if("203".equals(payType)){
				orderPayDetail.setRefundMoneyType(4);
				orderPayInput.setRefundMoneyTypeDesc("借款款项");
				orderPayInput.setPaymentListUrl("cost/payManager/payList/203");
				payTypeInt = 4;
				//借款的借款金额数据需根据reviewId进行查询
				if("11".equals(orderType)){//酒店
					List<HotelMoneyAmount> amounts = hotelMoneyAmountService.getMoneyAmonutByReviewId(reviewId);
					currencyId = "";//初始化币种
					payPrice = "";//初始化金额
					int n = 0;
					for(HotelMoneyAmount temp : amounts) {
						if(n == 0){
							currencyId += temp.getCurrencyId();
							payPrice += temp.getAmount();
						} else {
							currencyId += "," + temp.getCurrencyId();
							payPrice += "," + temp.getAmount();
						}
						n++;
					}
				} else if("12".equals(orderType)){//海岛游
					List<IslandMoneyAmount> amounts = islandMoneyAmountService.findAmountByReviewId(reviewId);
					currencyId = "";//初始化币种
					payPrice = "";//初始化金额
					int n = 0;
					for(IslandMoneyAmount temp : amounts) {
						if(n == 0){
							currencyId += temp.getCurrencyId();
							payPrice += temp.getAmount();
						} else {
							currencyId += "," + temp.getCurrencyId();
							payPrice += "," + temp.getAmount();
						}
						n++;
					}
				} else {
					List<MoneyAmount> amounts = moneyAmountService.findAmountsByReviewId(reviewId);
					currencyId = "";//初始化币种
					payPrice = "";//初始化金额
					int n = 0;
					for(MoneyAmount temp : amounts) {
						if(n == 0){
							currencyId += temp.getCurrencyId();
							payPrice += temp.getAmount();
						} else {
							currencyId += "," + temp.getCurrencyId();
							payPrice += "," + temp.getAmount();
						}
						n++;
					}
				}
			} 
			//设置付款币种
			orderPayDetail.setPayCurrencyId(currencyId);
			//计算已付金额 用应付金额减去已付金额 调用付款功能
			if("11".equals(orderType)){
				List<HotelMoneyAmount> payedmoneys = payManagerService.getHotelPayedMoney(reviewId, payTypeInt);
				if(payedmoneys != null && payedmoneys.size() != 0) {
					String[] curs = currencyId.split(",");
					String[] pays = payPrice.split(",");
					String payPrice2 = "";
					Integer cur;
					boolean flag = false;
					BigDecimal payedMoney = null;
					BigDecimal payMoney = null;
					if(curs != null && curs.length != 0 && curs.length == pays.length){
						for(int i = 0; i < curs.length; i++){
							cur = Integer.parseInt(curs[i]);
							flag = false;
							for(HotelMoneyAmount payedmoney : payedmoneys){
								if(cur == payedmoney.getCurrencyId()){
									payedMoney = new BigDecimal(payedmoney.getAmount());//已付金额
									payMoney = new BigDecimal(pays[i]);//应付金额
									if(i == 0){
										payPrice2 += payMoney.subtract(payedMoney).toString();
									} else {
										payPrice2 += "," + payMoney.subtract(payedMoney).toString();
									}
									flag = true;
									break;
								}
							}
							if(!flag){
								if(i == 0){
									payPrice2 += pays[i];
								} else {
									payPrice2 += "," + pays[i];
								}
							}
						}
						orderPayDetail.setPayCurrencyPrice(payPrice2);
					}
				} else {
					orderPayDetail.setPayCurrencyPrice(payPrice);
				}
			} else if("12".equals(orderType)){
				List<IslandMoneyAmount> payedmoneys = payManagerService.getIslandPayedMoney(reviewId, payTypeInt);
				if(payedmoneys != null && payedmoneys.size() != 0) {
					String[] curs = currencyId.split(",");
					String[] pays = payPrice.split(",");
					String payPrice2 = "";
					Integer cur;
					boolean flag = false;
					BigDecimal payedMoney = null;
					BigDecimal payMoney = null;
					if(curs != null && curs.length != 0 && curs.length == pays.length){
						for(int i = 0; i < curs.length; i++){
							cur = Integer.parseInt(curs[i]);
							flag = false;
							for(IslandMoneyAmount payedmoney : payedmoneys){
								if(cur == payedmoney.getCurrencyId()){
									payedMoney = new BigDecimal(payedmoney.getAmount());//已付金额
									payMoney = new BigDecimal(pays[i]);//应付金额
									if(i == 0){
										payPrice2 += payMoney.subtract(payedMoney).toString();
									} else {
										payPrice2 += "," + payMoney.subtract(payedMoney).toString();
									}
									flag = true;
									break;
								}
							}
							if(!flag){
								if(i == 0){
									payPrice2 += pays[i];
								} else {
									payPrice2 += "," + pays[i];
								}
							}
						}
						orderPayDetail.setPayCurrencyPrice(payPrice2);
					}
				} else {
					orderPayDetail.setPayCurrencyPrice(payPrice);
				}
			} else {
				List<MoneyAmount> payedmoneys = payManagerService.getPayedMoney(reviewId, payTypeInt);
				if(payedmoneys != null && payedmoneys.size() != 0) {
					String[] curs = currencyId.split(",");
					String[] pays = payPrice.split(",");
					String payPrice2 = "";
					Integer cur;
					boolean flag = false;
					BigDecimal payedMoney = null;
					BigDecimal payMoney = null;
					if(curs != null && curs.length != 0 && curs.length == pays.length){
						for(int i = 0; i < curs.length; i++){
							cur = Integer.parseInt(curs[i]);
							flag = false;
							for(MoneyAmount payedmoney : payedmoneys){
								if(cur == payedmoney.getCurrencyId()){
									payedMoney = payedmoney.getAmount();//已付金额
									payMoney = new BigDecimal(pays[i]);//应付金额
									if(i == 0){
										payPrice2 += payMoney.subtract(payedMoney).toString();
									} else {
										payPrice2 += "," + payMoney.subtract(payedMoney).toString();
									}
									flag = true;
									break;
								}
							}
							if(!flag){
								if(i == 0){
									payPrice2 += pays[i];
								} else {
									payPrice2 += "," + pays[i];
								}
							}
						}
						orderPayDetail.setPayCurrencyPrice(payPrice2);
					}
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
			int n = 0;
			if(list != null && list.size() > 0){
				for(MoneyAmount temp : list) {
					if(n == 0) {
						totalCurrencyId +=temp.getCurrencyId();
						totalCurrencyPrice += temp.getAmount();
					} else {
						totalCurrencyId +="," + temp.getCurrencyId();
						totalCurrencyPrice += "," + temp.getAmount();
					}
					n++;
					 
				}
			}
			n = 0;
			if(list2 != null && list2.size() > 0){
				for(HotelMoneyAmount temp : list2) {
					if(n == 0) {
						totalCurrencyId +=temp.getCurrencyId();
						totalCurrencyPrice += temp.getAmount();
					} else {
						totalCurrencyId +="," + temp.getCurrencyId();
						totalCurrencyPrice += "," + temp.getAmount();
					}
					n++;
					 
				}
			}
			n = 0;
			if(list3 != null && list3.size() > 0){
				for(IslandMoneyAmount temp : list3) {
					if(n == 0) {
						totalCurrencyId +=temp.getCurrencyId();
						totalCurrencyPrice += temp.getAmount();
					} else {
						totalCurrencyId +="," + temp.getCurrencyId();
						totalCurrencyPrice += "," + temp.getAmount();
					}
					n++;
					 
				}
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
			orderPayInput.setServiceClassName("com.trekiz.admin.modules.cost.service.PayManagerService");
			orderPayInput.setServiceAfterMethodName("updateReviewUpdateDate");
			request.setAttribute("pay", orderPayInput);
			return "forward:"+Global.getAdminPath()+"/orderPayMore/pay";
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
		String groupCode = request.getParameter("groupCode"); // 团号
		String orderType = request.getParameter("orderType"); // 订单类型
		String operatorId = request.getParameter("operatorId"); // 计调
		String payStatus = request.getParameter("payStatus"); // 付款状态
		String currencyId = request.getParameter("currencyId"); // 币种ID
		String moneyBegin = request.getParameter("moneyBegin"); // 开始金额
		String moneyEnd = request.getParameter("moneyEnd"); // 结束金额
		String printStatus = request.getParameter("printStatus"); // 打印状态
		String payedMoneyStatus = request.getParameter("payedMoneyStatus"); // 已付金额
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
		if (StringUtils.isNotBlank(payedMoneyStatus)) {
			params.put("payedMoneyStatus", payedMoneyStatus);
		}
		Page<Map<String, Object>> page = payManagerService.findBorrowMoneyList(
				params, new Page<Map<String, Object>>(request, response));
		model.addAttribute("groupCode", groupCode);
		model.addAttribute("orderTypeValue", orderType);
		model.addAttribute("operatorId", operatorId);
		model.addAttribute("payStatus", payStatus);
		model.addAttribute("currencyId", currencyId);
		model.addAttribute("moneyBegin", moneyBegin);
		model.addAttribute("moneyEnd", moneyEnd);
		model.addAttribute("printStatus", printStatus);
		model.addAttribute("payedMoneyStatus", payedMoneyStatus);
		//是否需要确认付款，对应需求C354，1:表示需要，0:表示不需要
		model.addAttribute("confirmPay", UserUtils.getUser().getCompany().getConfirmPay());
		model.addAttribute("orderTypeCombox",
				DictUtils.getDictList("order_type"));// 团队类型
		model.addAttribute(
				"operator",
				UserUtils.getOperators(UserUtils.getUser().getCompany()
						.getId()));// 计调
		model.addAttribute(
				"currencyList",
				currencyService.findCurrencyList(UserUtils.getUser()
						.getCompany().getId()));// 币种
		model.addAttribute("orderType", DictUtils.getDictList("order_type"));// 团队类型
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
		String payedMoneyStatus = request.getParameter("payedMoneyStatus"); // 已付金额
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
		if (StringUtils.isNotBlank(payedMoneyStatus)) {
			params.put("payedMoneyStatus", payedMoneyStatus);
		}
		Long companyId = UserUtils.getUser().getCompany().getId();
		Page<Map<String, Object>> page = payManagerService.borrowMoneyForTTSQZ(
				params, companyId, new Page<Map<String, Object>>(request, response));
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
		model.addAttribute("currencyId", currencyId);
		model.addAttribute("payedMoneyStatus", payedMoneyStatus);
		//是否需要确认付款，对应需求C354，1:表示需要，0:表示不需要
		model.addAttribute("confirmPay", UserUtils.getUser().getCompany().getConfirmPay());
		model.addAttribute(
				"operatorList",agentinfoService.findInnerJd(companyId));// 计调
		List<Currency> list = currencyService.findCurrencyList(companyId);
		List<Currency> cnyList = new ArrayList<Currency>();
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
		return "modules/order/borrowMoneyforTTSQZ";
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
	@Deprecated
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
		orderPayInput.setRefundMoneyTypeDesc("环球行签证借款款项");
		orderPayInput.setPaymentListUrl("cost/payManager/borrowMoneyForTTSQZ/203");
		
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
		/*组织应收价数据 start*/
		orderPayDetail.setTotalCurrencyId(orderPayDetail.getPayCurrencyId());
		orderPayDetail.setTotalCurrencyPrice(orderPayDetail.getPayCurrencyPrice());
		orderPayInput.setAgentId(-1);
		/*组织应收价数据 end*/
		orderPays.add(orderPayDetail);
		orderPayInput.setOrderPayDetailList(orderPays);
		orderPayInput.setPayType("2");
		orderPayInput.setMoneyFlag(1);//显示金额 且可编辑
		//add by shijun.liu 因C162需求，付款之后，按更新时间排序要到第一位
		orderPayInput.setServiceClassName("com.trekiz.admin.modules.cost.service.PayManagerService");
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
	@Deprecated
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
	
}
