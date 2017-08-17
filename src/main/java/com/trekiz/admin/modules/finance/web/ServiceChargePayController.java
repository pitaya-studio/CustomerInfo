package com.trekiz.admin.modules.finance.web;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.agent.utils.AgentInfoUtils;
import com.trekiz.admin.modules.finance.param.ServiceChargePayParam;
import com.trekiz.admin.modules.finance.result.ServiceChargePayListResult;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.quauq.review.core.engine.ReviewService;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.cost.service.PayManagerService;
import com.trekiz.admin.modules.finance.service.IServiceChargeService;
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
import com.trekiz.admin.modules.order.entity.OrderServiceCharge;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.pojo.OrderPayDetail;
import com.trekiz.admin.modules.order.pojo.OrderPayInput;
import com.trekiz.admin.modules.order.service.IOrderServiceChargeService;
import com.trekiz.admin.modules.order.service.ProductOrderService;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.service.VisaOrderService;

import freemarker.template.TemplateException;

/**
 * 服务费付款Controller
 * @author shijun.liu
 * @date   2016.08.30
 */
@Controller
@RequestMapping(value = "${adminPath}/finance/serviceCharge")
public class ServiceChargePayController {
	
	@Autowired
	private IServiceChargeService iServiceChargeService;
	
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private HotelMoneyAmountService hotelMoneyAmountService;
	@Autowired
	private IslandMoneyAmountService islandMoneyAmountService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private PayManagerService payManagerService;
	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	@Autowired
	private VisaOrderService visaOrderService;
	@Autowired
	private HotelOrderService hotelOrderService;
	@Autowired
	private IslandOrderService islandOrderService;
	@Autowired
	private ProductOrderService productOrderService;
	@Autowired
	private IOrderServiceChargeService iOrderServiceChargeService;
	@Autowired
	private AgentinfoService agentinfoService;

	/**
	 * 支付款项
	 * @param payType
	 *         204服务费
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author chao.zhang
	 * 2016-09-01
	 */
	@RequestMapping(value = "pay/{payType}")
	public String pay(@PathVariable("payType") String payType,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {
		
			//调用支付接口的参数
			List<OrderPayDetail> orderPays = new ArrayList<OrderPayDetail>();
			OrderPayDetail orderPayDetail = new OrderPayDetail();
			Long serviceChargeId = null;
			String serviceChargeIdStr = request.getParameter("serviceChargeId");
			String orderType = request.getParameter("orderType");
			String currencyId = request.getParameter("currencyId");
			String payPrice = request.getParameter("payPrice");
			//类型 14:quauq服务费; 15:总社服务费
			String moneyType = request.getParameter("moneyType");
		if(StringUtils.isNotBlank(serviceChargeIdStr)){
				OrderServiceCharge orderServiceCharge = iServiceChargeService.getServiceChargePayById(serviceChargeIdStr);
				if(orderServiceCharge != null){
					serviceChargeId = orderServiceCharge.getId();
					orderPayDetail.setProjectId(serviceChargeId);
				}
			}
			if(StringUtils.isNotBlank(orderType)){
				orderPayDetail.setOrderType(Integer.parseInt(orderType));
			}
			OrderPayInput orderPayInput = new OrderPayInput();
			Integer payTypeInt = 0;
			//判断是否是服务费类型
			if("204".equals(payType)){
				payTypeInt=Integer.parseInt(moneyType);
				orderPayDetail.setRefundMoneyType(payTypeInt);
				if(payTypeInt == 14){
					orderPayInput.setRefundMoneyTypeDesc("quauq交易服务费付款");
				}else if(payTypeInt == 15){
					orderPayInput.setRefundMoneyTypeDesc("总社交易服务费付款");
				}
				orderPayInput.setPaymentListUrl("finance/serviceCharge/list");
			}
			//设置付款币种
			orderPayDetail.setPayCurrencyId(currencyId);
			//计算已付金额 用应付金额减去已付金额 调用付款功能
			if("11".equals(orderType)){//酒店
			List<HotelMoneyAmount> payedmoneys = payManagerService.getHotelPayedMoney(serviceChargeId, payTypeInt);
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
								if(cur == payedmoney.getCurrencyId().intValue()){
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
			} else if("12".equals(orderType)){//海岛游
				List<IslandMoneyAmount> payedmoneys = payManagerService.getIslandPayedMoney(serviceChargeId, payTypeInt);
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
								if(cur == payedmoney.getCurrencyId().intValue()){
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
				List<MoneyAmount> payedmoneys = payManagerService.getPayedMoney(serviceChargeId, payTypeInt);
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
								if(cur == payedmoney.getCurrencyId().intValue()){
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
			orderPayDetail.setReviewId(serviceChargeId);
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
						totalCurrencyId += temp.getCurrencyId();
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
			
			//add by shijun.liu 因C162需求，付款之后，按更新时间排序要到第一位
			orderPayInput.setServiceClassName("com.trekiz.admin.review.pay.service.PayManagerNewService");
			orderPayInput.setServiceAfterMethodName("updateReviewUpdateDate");
			
			//add by zhangchao 504需求 结算方
			if("0".equals(request.getParameter("chargeType"))){
				orderPayInput.setSettleName("QUAUQ");
			}else{
				Agentinfo agentinfo = agentinfoService.findAgentInfoById(Long.parseLong(request.getParameter("agentId")));
				orderPayInput.setSettleName(agentinfo.getAgentName());
			}
			
			request.setAttribute("pay", orderPayInput);
			
			return "forward:"+Global.getAdminPath()+"/orderPayMore/pay";
	}
	
	
	/**
	 * 获取服务费付款确认信息
	 * @author yang.wang
	 * @date 2016.9.1
	 * */
	@ResponseBody
	@RequestMapping(value = "getPaymentConfirmInfo")
	public Map<String, Object> getPaymentConfirmInfo(HttpServletRequest request) {
		
		Map<String, Object> map = new HashMap<>();
		String orderId = request.getParameter("orderId");
		String type = request.getParameter("type");
		
		if (StringUtils.isBlank(orderId) || (!"0".equals(type) && !"1".equals(type))) {
			map.put("flag", false);
			map.put("msg", "未找到记录！");
			return map;
		}
		
		map = iServiceChargeService.getServiceCharge4Confirm(Long.valueOf(orderId), Integer.parseInt(type));
		if (map.isEmpty()) {
			map.put("flag", false);
			map.put("msg", "未找到记录！");
			return map;
		}
		
		map.put("flag", true);
		return map;
	}
	
	/**
	 * 服务费确认付款
	 * @author yang.wang
	 * @date 2016.9.1
	 * */
	@ResponseBody
	@RequestMapping(value = "paymentConfirm")
	public Map<String, Object> paymentConfirm(HttpServletRequest request) {

		String orderId = request.getParameter("orderId");
		String type = request.getParameter("type");
		String confirmDate = request.getParameter("confirmDate");
		
		Map<String, Object> map = new HashMap<String, Object>();
		//订单id、服务费类型数据校验
		if (StringUtils.isBlank(orderId) || StringUtils.isBlank(type)) {
			map.put("flag", false);
			map.put("msg", "未找到记录！");
			return map;
		}
		//出纳日期数据校验
		if (StringUtils.isBlank(confirmDate)) {
			map.put("flag", false);
			map.put("msg", "出纳确认日期为空！");
			return map;
		}
		
		map = iOrderServiceChargeService.paymentConfirm(Long.parseLong(orderId),
				Integer.parseInt(type), confirmDate);
		return map;
	}

	/**
	 * 获取支出凭单数据
	 * @author yang.wang
	 * @date 2016.9.2
	 * */
	@RequestMapping(value = "getPrintPaymentInfo/{orderId}/{type}")
	public String getPrintPaymentInfo(Model model, @PathVariable Long orderId, @PathVariable Integer type) {

		Map<String, Object> map = new HashMap<>();
		if (orderId <= 0 || (type != 0 && type != 1)) {
			model.addAttribute(Context.ERROR_MESSAGE_KEY, "未找到记录");
			return Context.ERROR_PAGE;
		}
		
		map = iServiceChargeService.getPrintPaymentInfo(orderId, type);
		map.put("orderId", orderId);//订单id
		map.put("type", type);//服务费类型

		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		// 拉美途和环球行确认日期置空
		if (Context.SUPPLIER_UUID_LMT.equals(companyUuid) || Context.SUPPLIER_UUID_HQX.equals(companyUuid)) {
			map.put("confirmPayDate", null);
		}
		
		model.addAttribute("map", map);
		return "modules/finance/getPrintInfo";
	}

	/**
	 * 获取支出单批量打印信息
	 * @param request
	 * @param model
     * @return
	 * @author yudong.xu 2016.10.25
     */
	@RequestMapping(value = "getBatchPrintInfo")
	public String getBatchPrintInfo(HttpServletRequest request,Model model) {

		String printInfo = request.getParameter("printInfo");
		JSONArray printArr = JSONArray.parseArray(printInfo);
		if (printArr == null || printArr.size() == 0){
			model.addAttribute(Context.ERROR_MESSAGE_KEY, "未找到记录");
			return Context.ERROR_PAGE;
		}

		List<Map<String,Object>> printList = new ArrayList<>();
		String companyUuid = UserUtils.getUser().getCompany().getUuid();

		for (int i = 0; i < printArr.size(); i++) {
			JSONObject printObj = printArr.getJSONObject(i);
			Long orderId = printObj.getLong("orderId");
			Integer type = printObj.getInteger("type");
			Map<String,Object> printMap = iServiceChargeService.getPrintPaymentInfo(orderId, type);

			printMap.put("orderId", orderId);//订单id
			printMap.put("type", type);//服务费类型
			if (Context.SUPPLIER_UUID_LMT.equals(companyUuid) || Context.SUPPLIER_UUID_HQX.equals(companyUuid)) {
				// 拉美途和环球行确认日期置空
				printMap.put("confirmPayDate", null);
			}
			printList.add(printMap);
		}
		model.addAttribute("printList",printList);
		return "modules/finance/serviceChargeBatchPrint";
	}

	/**
	 * 更新订单打印状态和打印时间，若已打印则无操作
	 * @author yang.wang
	 * @date 2016.9.1
	 * */
	@ResponseBody
	@RequestMapping(value = "updatePrintDate")
	public Map<String, Object> updatePrintDate(Long orderId, Integer type) {
		
		return iOrderServiceChargeService.updatePrintDate(orderId, type);
	}

	/**
	 * 批量更新打印状态，调用Service层的方法
	 * @param updateInfo
	 * @return
     */
	@ResponseBody
	@RequestMapping(value = "batchUpdatePrintDate")
	public Map<String, Object> batchUpdatePrintDate(String updateInfo) {
		Map<String,Object> result = new HashMap<>();
		JSONArray printArr = JSONArray.parseArray(updateInfo);
		if (printArr == null){
			result.put("flag",false);
			return result;
		}

		for (int i = 0; i < printArr.size(); i++) {
			String itemStr = printArr.getString(i);
			String[] itemArr = itemStr.split("_");
			Long orderId = Long.parseLong(itemArr[0]);
			Integer type = Integer.parseInt(itemArr[1]);
			result = iOrderServiceChargeService.updatePrintDate(orderId, type);
			if (!(boolean)result.get("flag")){
				return result;
			}
		}
		return result;
	}

	/**
	 * 创建并下载支出凭单word文件
	 * @author yang.wang
	 * @date 2016.9.2
	 * */
	@RequestMapping(value = "serviceChargePaymentDownload")
	public String serviceChargePaymentDownload(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException, TemplateException {

		String orderId = request.getParameter("orderId");//订单id
		String type = request.getParameter("type");//服务费类型

		if (StringUtils.isBlank(orderId) || (!"0".equals(type) && !"1".equals(type))) {
			model.addAttribute(Context.ERROR_MESSAGE_KEY, "未找到记录");
			return Context.ERROR_PAGE;
		}

		Map<String, Object> map = iServiceChargeService.getPrintPaymentInfo(Long.parseLong(orderId), Integer.parseInt(type));
		map.put("orderId", orderId);
		map.put("type", type);
		map.put("createDate", DateUtils.formatDate((Date) map.get("createDate"), "yyyy年 MM月 dd日"));
		
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		String payStatus = map.get("payStatus").toString();
		if (!Context.SUPPLIER_UUID_LMT.equals(companyUuid) && !Context.SUPPLIER_UUID_HQX.equals(companyUuid) 
				&& "1".equals(payStatus)) {
			map.put("confirmPayDate", DateUtils.formatDate((Date) map.get("confirmPayDate"), "yyyy 年 MM 月 dd 日"));
		} else {
			map.put("confirmPayDate", "   年   月   日");
		}
		
		if (null == map.get("remarks")) {
			map.put("remarks", "");
		}
		
		//更新打印时间
		this.updatePrintDate(Long.parseLong(orderId), Integer.parseInt(type));
		//创建下载文件
		File file = iServiceChargeService.createServiceChargePaymentDownloadFile(map);
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName = "支出凭单" + nowDate + ".doc";

		ServletUtil.downLoadFile(response, fileName, file.getAbsolutePath());
		return null;
	}

	/**
	 * 撤销服务费收款确认
	 * @author yang.wang
	 * @date 2016.9.2
	 * */
	@ResponseBody
	@RequestMapping(value = "cancelPaymentConfirm")
	public Map<String, Object> cancelPaymentConfirm(HttpServletRequest request) {

		Map<String, Object> map = new HashMap<>();

		String orderId = request.getParameter("orderId");
		String type = request.getParameter("type");

		if (StringUtils.isBlank(orderId) || StringUtils.isBlank(type)) {
			map.put("flag", false);
			map.put("msg", "未找到记录！");
			return map;
		}
		
		return iOrderServiceChargeService.cancelServiceChargePaymentConfirm(Long.parseLong(orderId),
				Integer.parseInt(type));
	}

	/**
	 * 服务费查询列表
	 * @author yudong.xu
	 * @date 2016.9.2
	 * */
	@RequestMapping(value = "list")
	public String getServiceChargePayList(HttpServletRequest request, HttpServletResponse response,
				  @ModelAttribute ServiceChargePayParam param, BindingResult result, Model model){
		if (result.hasErrors()){
			model.addAttribute(Context.ERROR_MESSAGE_KEY, "输入参数有误，请返回重新输入");
			return Context.ERROR_PAGE;
		}

		model.addAttribute("param",param); // 回显请求参数

		Page<ServiceChargePayListResult> page = new Page(request,response);
		iServiceChargeService.getServiceChargePayList(page,param);
		model.addAttribute("page",page);

		Long companyId = UserUtils.getUser().getCompany().getId();
		model.addAttribute("jdList",UserUtils.getOperators(companyId)); // 计调下拉选
		model.addAttribute("salerList",UserUtils.getSalers(companyId)); // 销售下拉选
		model.addAttribute("agentList",AgentInfoUtils.getQuauqAgent()); // 结算方(渠道商)下拉选
		model.addAttribute("orderTypes", DictUtils.getDict2List("order_type")); // 团队类型下拉选
		return "modules/finance/serviceChargePayList";
	}
	
	/**
	 * 批量付款确认信息
	 * @param request
	 * @return
	 * @author chao.zhang
	 * @time 2016-09-06
	 */
	@ResponseBody
	@RequestMapping(value = "getBatchPaymentConfirmInfo")
	public List<Map<String,Object>> getBatchPaymentConfirmInfo(HttpServletRequest request){
		//获得传过来的参数并转换成集合
		JSONArray array = JSONArray.parseArray(request.getParameter("datas"));
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		if(array != null && array.size() != 0){
			for(int j=0;array!=null && j<array.size();j++){
				//转换成json对象
				JSONObject jobj = (JSONObject)array.get(j);
				Long orderId = jobj.getLong("orderId");
				Integer type = jobj.getInteger("type");
				//根据orderId，type获得付款确认信息
				Map<String, Object> map = iServiceChargeService.getServiceCharge4Confirm(orderId,type);
				list.add(map);
			}
		}
		return list;
	}
	
	/**
	 * 服务费确认付款
	 * @author chao.zhang
	 * @date 2016.9.7
	 * */
	@ResponseBody
	@RequestMapping(value = "batchPaymentConfirm")
	public Map<String, Object> batchPaymentConfirm(HttpServletRequest request) {
		JSONArray array = JSONArray.parseArray(request.getParameter("datas"));
		Map<String, Object> map = new HashMap<String, Object>();
		for(int i = 0 ; i<array.size() ; i ++){
			JSONObject jobj = (JSONObject)array.get(i);
			String orderId = jobj.getString("orderId");
			String type = jobj.getString("type");
			String confirmDate = jobj.getString("confirmDate");
			//订单id、服务费类型数据校验
			if (StringUtils.isBlank(orderId) || StringUtils.isBlank(type)) {
				map.put("flag", false);
				map.put("msg", "未找到记录！");
				return map;
			}
			//出纳日期数据校验
			if (StringUtils.isBlank(confirmDate)) {
				map.put("flag", false);
				map.put("msg", "出纳确认日期为空！");
				return map;
			}
			map = iOrderServiceChargeService.paymentConfirm(Long.parseLong(orderId),
					Integer.parseInt(type), confirmDate);
		}
		return map;
	}
}

