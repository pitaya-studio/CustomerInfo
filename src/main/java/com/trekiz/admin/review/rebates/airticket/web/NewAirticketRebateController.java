package com.trekiz.admin.review.rebates.airticket.web;

import java.io.File;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trekiz.admin.common.config.Context;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
//import com.trekiz.admin.modules.activity.service.ITravelActivityService;
//import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.airticket.entity.ActivityAirTicket;
//import com.trekiz.admin.modules.airticket.repository.ActivityAirTicketDao;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticketorder.entity.AirticketOrder;
import com.trekiz.admin.modules.airticketorder.entity.RefundBean;
import com.trekiz.admin.modules.airticketorder.repository.IAirticketOrderDao;
//import com.trekiz.admin.modules.airticketorder.service.AirTicketOrderLendMoneyService;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
//import com.trekiz.admin.modules.order.airticket.service.IAirticketPreOrderService;
import com.trekiz.admin.modules.order.rebates.entity.RebatesNew;
import com.trekiz.admin.modules.order.rebates.service.RebatesService;
//import com.trekiz.admin.modules.order.service.OrderCommonService;
//import com.trekiz.admin.modules.order.service.OrderReviewService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.review.airticketreturn.service.IAirTicketReturnService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
//import com.trekiz.admin.modules.sys.service.AirlineInfoService;
//import com.trekiz.admin.modules.sys.service.AirportService;
//import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
//import com.trekiz.admin.modules.sys.service.OfficeService;
//import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
//import com.trekiz.admin.modules.visa.repository.VisaOrderDao;
import com.trekiz.admin.review.common.service.ICommonReviewService;
import com.trekiz.admin.review.rebates.airticket.service.NewAirticketrebatesService;
import com.trekiz.admin.review.rebates.singleGroup.service.RebatesNewService;

@Controller
@RequestMapping(value = "${adminPath}/order/newAirticketRebate")
public class NewAirticketRebateController {

	@Autowired
	private RebatesNewService rebatesService;
	@Autowired
	private IActivityAirTicketService activityAirTicketService;
	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	@Autowired
	private TravelerService travelerService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private ActivityGroupService activityGroupService;
	@Autowired
	private IAirticketOrderDao airticketOrderDao;
	@Autowired
	private IAirTicketReturnService airTicketReturnService;
	@Autowired
	private NewAirticketrebatesService newAirticketrebatesService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private RebatesService rebatesOldService;
	@Autowired
	private ICommonReviewService commonReviewService;

	/**
	 * 跳转到返佣申请列表页
	 * @param model
	 * @param request
	 * @return
	 * @author wangxu
	 */
	@RequestMapping(value ="airticketRebatesList")
	public String airticketRebatesList(Model model, HttpServletRequest request){
		Long orderId = Long.parseLong(request.getParameter("orderId"));
		int orderType = Integer.parseInt(request.getParameter("productType"));
	
		model.addAttribute("orderId", orderId);
		model.addAttribute("orderType", orderType);
		
		List<RebatesNew> rebatesNewList = rebatesService.findRebatesList(orderId, orderType);
		//groupListMap将查出的记录按照审核id进行分组，页面展示使用
        Map<String,List<RebatesNew>> groupListMap = new LinkedHashMap<String,List<RebatesNew>>();
		for(RebatesNew rn:rebatesNewList){
			if(groupListMap.containsKey(rn.getRid())){
				groupListMap.get(rn.getRid()).add(rn);
			}else{
				List<RebatesNew> list = new ArrayList<RebatesNew>();
				list.add(rn);
				groupListMap.put(rn.getRid(), list);
			}
		}
		//组装页面显示结果集，主要确定是单游客还是团队，每个审批ID只显示一条记录
		List<RebatesNew> result = new ArrayList<RebatesNew>();
		for(Map.Entry<String, List<RebatesNew>> superEntry : groupListMap.entrySet()){
			String currencyName = "";//存放币种的ID
			String rebatesDiff = "";//存放对应币种的金额			
			//如果多条游客或团队记录，则默认是团队类型 
			if(superEntry.getValue().size()>1){
				superEntry.getValue().get(0).setTraveler(null);
			}
			result.add(superEntry.getValue().get(0));
			Map<Long, List<RebatesNew>> groupMap = new HashMap<Long, List<RebatesNew>>();
			//按照币种ID进行分组，统计金额
			for(RebatesNew rebate:superEntry.getValue()){
 				if(groupMap.containsKey(rebate.getCurrencyId())) {
	                groupMap.get(rebate.getCurrencyId()).add(rebate);
	            } else {
 	                List<RebatesNew> glist = new ArrayList<RebatesNew>();
	                glist.add(rebate);
	                groupMap.put(rebate.getCurrencyId(), glist);
	            }
			}
			List<String> molist = Lists.newArrayList();
			List<String> newMolist = Lists.newArrayList();
			for (Map.Entry<Long, List<RebatesNew>> childEntry : groupMap.entrySet()) {
	            BigDecimal amount = new BigDecimal("0");
	            currencyName += childEntry.getKey()+ ",";
	            List<RebatesNew> ls=childEntry.getValue(); 
	            for(RebatesNew reb : ls ){
	                amount = amount.add(reb.getRebatesDiff());
	                newMolist.add(reb.getOldRebates());
	                molist.add(reb.getTotalMoney());
	            }
	            rebatesDiff +=amount +",";
	        }
			String rcn = currencyName.substring(0, currencyName.length() - 1);
			String rbs = rebatesDiff.substring(0, rebatesDiff.length() - 1);
			
			superEntry.getValue().get(0).setRebatesdiffCurrName(rcn);
			superEntry.getValue().get(0).setRebatesdiffString(rbs);
			
			String newMonry = moneyAmountService.getMoneyStr(newMolist);
			String totalMoney = moneyAmountService.getMoneyStr(molist);//应该是应收金额
			//结算价总价
			superEntry.getValue().get(0).setRebatesdiffString2(totalMoney);
			superEntry.getValue().get(0).setCostname("机票返佣");
			//累计返佣金额
			superEntry.getValue().get(0).setRebatesdiffString1(newMonry);
			String allCumulative =superEntry.getValue().get(0).getAllCumulative();
			if(null!=allCumulative && !("").equals(allCumulative)){
				String[] arry = allCumulative.split("\\+");
		        Map<String, Double> map = new HashMap<String, Double>();
		        Map<String, String> map2 = new HashMap<String, String>();
				for (String str : arry) {
					String[] s = str.split("\\:");
					if (map.containsKey(s[0])) {
						map.put(s[0], map.get(s[0]) + Double.valueOf(s[1]));
					} else {
						map.put(s[0], Double.valueOf(s[1].replace(",", "")));
					}
				}
		        Double val;
		        String name;
		        Set<String> keySet = map.keySet();
		        DecimalFormat myformat = new DecimalFormat();
				myformat.applyPattern("##,###.00");
		        for (String  key : keySet) {
					val = map.get(key);
					name = currencyService.findCurrency(Long.parseLong(key)).getCurrencyName();
					map2.put(name, myformat.format(val));
				}
			    String mt = map2.toString();
			    mt=mt.replaceAll("\\=", " ");
			    mt=mt.substring(1, mt.length()-1);
			    superEntry.getValue().get(0).setAllCumulative(mt);
			}
		}
		Map<String, Object> orderDetail = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId.toString());
		String totalmoney =  orderDetail.get("total_money").toString();
		String tuantotalmoney = moneyAmountService.getMoneyCurrenName(totalmoney);
		model.addAttribute("tuantotalmoney", tuantotalmoney);
		/*//当前订单所有游客的返佣最新记录 
		List<Rebates> teamList = rebatesService.getRebates(orderId);
		Map<Long,Rebates> rebatesMap = new HashMap<Long, Rebates>();
		for(Rebates rb :teamList){
			rebatesMap.put(rb.getTravelerId(), rb);
		}
		model.addAttribute("rebatesMap", rebatesMap);*/
		model.addAttribute("rebatesList", result);
		return "review/rebates/airticket/airticketRebatesList";
	}
	/**
	 *跳转到机票返佣申请页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws ParseException
	 * @author wangxu
	 */
	@RequestMapping(value="airticketRebatesApply")
	public String airticketRebatesApply(HttpServletRequest request, HttpServletResponse response,Model model) throws ParseException {
		User user = UserUtils.getUser();
 		Long companyId = user.getCompany().getId();
		String orderId= request.getParameter("orderId");
//		String reviewId = request.getParameter("review_id");
		//订单详情信息
		Map<String, Object> orderDetail = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId);
		List<Currency> currencyList = currencyService.findCurrencyList(companyId);
		RefundBean reviewObj = null;
		//当前订单所有游客的返佣最新记录 
		List<RebatesNew> teamList = rebatesService.getRebates(Long.parseLong(orderId));
		Map<Long,RebatesNew> rebatesMap = new HashMap<Long, RebatesNew>();
		for(RebatesNew rb :teamList){
			rebatesMap.put(rb.getTravelerId(), rb);
		}
		model.addAttribute("rebatesMap", rebatesMap);
		model.addAttribute("productOrder", 7);
		model.addAttribute("reviewObj", reviewObj);
		model.addAttribute("orderId", orderId);
//		model.addAttribute("reviewId", reviewId);
		model.addAttribute("orderDetail", orderDetail);
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("isAllowMultiRebateObject", user.getCompany().getIsAllowMultiRebateObject());//是否多对象返佣
		return "review/rebates/airticket/airticketRebatesApply";
	}
	
	
	
	/**
	 * 机票返佣申请保存方法
	 * @param response
	 * @param request
	 * @return
	 * @author wangxu
	 */
	@ResponseBody
	@RequestMapping(value="airticketRebatesSave",method = RequestMethod.POST)
	public String airticketRebatesSave(HttpServletResponse response, HttpServletRequest request){
		String strmsg = "";
		String rebatesTotal = request.getParameter("rebatesTotal");//累计返佣金额($1,￥2,...)
		String supplyId = request.getParameter("supplyId");
		String supplyName = request.getParameter("supplyName");
		String accountId = request.getParameter("accountId");
		try{
			JSONArray rebatesJSONArr = JSONArray.fromObject(request.getParameter("rebatesList"));
			List<RebatesNew> rebatesList = Lists.newArrayList();
			RebatesNew rebates;
			Traveler traveler;
			String travelerIds = "";
			int j = rebatesJSONArr.size();
			for(int i = 0; i < j; i++){
				traveler = null;
				String totalMoney = "";
				JSONObject rebatesJSONObj = rebatesJSONArr.getJSONObject(i);
				String orderId =rebatesJSONObj.getString("orderId");
				Long currencyId = Long.parseLong(rebatesJSONObj.getString("currencyId"));
				BigDecimal rebatesDiff = new BigDecimal(rebatesJSONObj.getString("rebatesDiff"));
				String oldRebates = rebatesJSONObj.getString("oldRebates");
				String costname = rebatesJSONObj.getString("costname");
				String remark = rebatesJSONObj.getString("remark");
				String nowCumulative  = rebatesJSONObj.getString("nowCumulative");
				Integer orderType = rebatesJSONObj.getInt("orderType");
				String newRebates = rebatesJSONObj.getString("newRebates");
				if(StringUtils.isNotBlank(rebatesJSONObj.getString("travelerId"))){
 					traveler = travelerService.findTravelerById(Long.parseLong(rebatesJSONObj.getString("travelerId")));
					totalMoney = traveler.getPayPriceSerialNum();
					travelerIds += traveler.getId()+",";
				}else{
					totalMoney = airTicketOrderService.getAirticketorderById(Long.parseLong(orderId)).getTotalMoney();
				}
				rebates = new RebatesNew(Long.parseLong(orderId), traveler, currencyId, totalMoney, costname, oldRebates, rebatesDiff, newRebates, remark, orderType,nowCumulative);
				Currency currency = currencyService.findCurrency(currencyId);
				rebates.setCurrency(currency);
				rebates.setCurrencyExchangerate(currency.getCurrencyExchangerate());
				rebatesList.add(rebates);
			}
			strmsg = rebatesService.airticketReabesSave(rebatesList,rebatesTotal,travelerIds,supplyId,supplyName,accountId);
		}catch(Exception e){
			e.printStackTrace();  
			return e.toString();
		}
		return strmsg;
	}
	
	/**
	 * 验证是否能再次返佣信息
	 * @param response
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="validRebates",method = RequestMethod.POST)
	public String validRebates(HttpServletResponse response, HttpServletRequest request){
		String orderId = request.getParameter("orderId");
//		String orderType = request.getParameter("orderType");
		AirticketOrder productOrder = airTicketOrderService.getAirticketorderById(Long.parseLong(orderId));
		Long ProductId =productOrder.getAirticketId();
		ActivityAirTicket activityAirTicket =  activityAirTicketService.getActivityAirTicketById(ProductId);
		//对结算单状态进行判断
		if (1 == activityAirTicket.getLockStatus()) {
			return "jiesuan_locked";
		}
		//本规则暂不使用
        /*		
        List<RebatesNew> rebatesList = rebatesService.findRebatesListByStatus(orderType, "9", orderId);
		if(rebatesList != null && rebatesList.size() > 0){
			return "false";
		}
		*/
		return "true";
	}

	/**
	 * 取消返佣申请申请
	 * @param response
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="cancleRebates")
	public String cancleRebates(HttpServletRequest request){
		String rid = request.getParameter("rid");
		ReviewNew reviewnew = reviewService.getReview(rid);
		if(reviewnew.getStatus()!=1){
			return "error";
		}
		User user = UserUtils.getUser();
		String userId = user.getId().toString();
		String companyId = user.getCompany().getUuid();
		try{
			ReviewResult r =reviewService.cancel(userId, companyId, "",rid, "取消申请机票返佣", null);
			if(r.getSuccess()){
				// 对成本录入进行更改
				ReviewNew review = reviewService.getReview(rid);
				commonReviewService.updateCostRecordStatus(review, 3);
				return "success";
			}else {
				return "error";
			}
		}catch(Exception e){
			e.printStackTrace();  
			return "error";
		}
	}
	
	/**
	 * 跳转到返佣详情界面
	 * @return
	 * @author wnagxu
	 */
	@RequestMapping(value ="airticketRebatesDetail")
	public String airticketRebatesDetail(@RequestParam String reviewId,Model model, HttpServletRequest request) {
		getDetailList(reviewId, model);
		model.addAttribute("rid", reviewId);
		return "review/rebates/airticket/airticketRebatesDetail";
	}
	
	/**
	 * 跳转到返佣审批页面
	 * @author wangxu
	 */
	@RequestMapping(value ="airticketRebatesApproval")
	public String airticketRebatesApproval(@RequestParam String reviewId,Model model, HttpServletRequest request) {
		getDetailList(reviewId, model);
		model.addAttribute("rid", reviewId);
		return "review/rebates/common/airticketRebatesApproval";
	}

	@SuppressWarnings("unchecked")
	private void getDetailList(String reviewId, Model model) {
		List<RebatesNew> rebates = rebatesService.findRebatesListByRid(reviewId);
		AirticketOrder order = airticketOrderDao.getAirticketOrderById(rebates.get(0).getOrderId());
		String currencyName = ""; String rebatesDiff = "";
		if(CollectionUtils.isNotEmpty(rebates)){
			Map<Long, List<RebatesNew>> groupMap = new HashMap<Long, List<RebatesNew>>();
			for (RebatesNew rebate : rebates) {
				if (groupMap.containsKey(rebate.getCurrencyId())) {
					groupMap.get(rebate.getCurrencyId()).add(rebate);
				} else {
					List<RebatesNew> glist = new ArrayList<RebatesNew>();
					glist.add(rebate);
					groupMap.put(rebate.getCurrencyId(), glist);
				}
				
				//根据不同返佣申请类型加载对应类型的预计返佣金额和当前的累计返佣金额
				String travelerId = null;
				if(null != rebate.getTravelerId() && 0 < rebate.getTravelerId()){
					travelerId = rebate.getTravelerId().toString();
					List<String> newRebatesList= rebatesService.getNewRebatesList(rebate.getOrderId(), travelerId);//当前游客的所有通过的返佣金额
					String newRebate = moneyAmountService.getMoneyStr(newRebatesList);//返回当前累计返佣金额（人民币100，美元50）
					rebate.setRebatesdiffString1(newRebate);
				}
				String rebatesStr = OrderCommonUtil.getRebatesMoney(order.getId(), 7, travelerId);
				if(null == rebatesStr || 0 == rebatesStr.trim().length()){
					rebate.setRebatesSign(0);
				}else{
					rebate.setRebatesSign(1);
				}
				rebate.setRebatesStr(rebatesStr);
				
			}
			for (Map.Entry<Long, List<RebatesNew>> entry : groupMap.entrySet()) {
				BigDecimal amount = new BigDecimal("0");
				currencyName += entry.getKey()+ ",";
				List<RebatesNew> ls=entry.getValue(); 
				for(RebatesNew reb : ls ){
					amount = amount.add(reb.getRebatesDiff());
				}
				rebatesDiff +=amount +",";
			}
		}
		/**
		 * 详情里面获取团队返佣金额
		 */
		List<String> teamCum = rebatesService.getNotStatus2AllCumulative(rebates.get(0).getOrderId().toString());
		String teamMonery = "";
		String mt = "";
		for(String strMonery : teamCum){
			teamMonery = strMonery;
		}
		if(null!=teamMonery && !("").equals(teamMonery)){
			String[] arry = teamMonery.split("\\+");
	        Map<String, Double> map = new HashMap<String, Double>();
	        Map<String, String> map2 = new HashMap<String, String>();
			for (String str : arry) {
				String[] s = str.split("\\:");
				if (map.containsKey(s[0])) {
					map.put(s[0], map.get(s[0]) + Double.valueOf(s[1].replace(",", "")));
				} else {
					map.put(s[0], Double.valueOf(s[1].replace(",", "")));
				}
			}
	        Double val;
	        String name;
	        Set<String> keySet = map.keySet();
	        DecimalFormat myformat = new DecimalFormat();
			myformat.applyPattern("##,###.00");
	        for (String  key : keySet) {
				val = map.get(key);
				name = currencyService.findCurrency(Long.parseLong(key)).getCurrencyName();
				map2.put(name, myformat.format(val));
			}
		    mt = map2.toString();
		    mt=mt.replaceAll("\\=", " ");
		    mt=mt.substring(1, mt.length()-1);
		}
		//获取预计返佣 add start by jiangyang 2015-8-7
		Long orderId = null;
		if(CollectionUtils.isNotEmpty(rebates)){
			orderId = rebates.get(0).getOrderId();
		}
		Map<String, Object> orderDetail = new HashMap<String,Object>();
		if(orderId!=null){
			orderDetail = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId.toString());
		}
		String groupRebate = null;
		List<Map<String,Object>> travelInfoList = null;
		if (orderDetail != null){
			groupRebate = (String)orderDetail.get("groupRebate");
			travelInfoList = (List<Map<String,Object>>) orderDetail.get("travelInfoList");			
		}
		model.addAttribute("groupRebate", groupRebate);
		if(CollectionUtils.isNotEmpty(travelInfoList)){
			model.addAttribute("travelInfoList", travelInfoList);
		}
		//获取预计返佣 add end   by jiangyang 2015-8-7
		model.addAttribute("teamMonery", mt);
		model.addAttribute("paymentProject", "机票返佣");
		model.addAttribute("bremarks", rebates.get(0).getRemark());
		model.addAttribute("currencyName", currencyName);
		model.addAttribute("rebatesDiff", rebatesDiff);
		AirticketOrder productOrder = airticketOrderDao.getAirticketOrderById(rebates.get(0).getOrderId());
		model.addAttribute("productOrder", productOrder);
		ActivityAirTicket product = activityAirTicketService.findById(productOrder.getAirticketId());
		model.addAttribute("product", product);
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getAirticketId());
		model.addAttribute("productGroup", productGroup);
		model.addAttribute("orderStatusStr",OrderCommonUtil.getChineseOrderType(productOrder.getProductTypeId().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(String.valueOf(productOrder.getOccupyType())));
		model.addAttribute("rebates", rebates);
		Map<String, Object> airticketReturnDetailInfoMap = airTicketReturnService.queryAirTicketReturnInfoById(Long.toString(rebates.get(0).getOrderId()));
		model.addAttribute("airticketReturnDetailInfoMap", airticketReturnDetailInfoMap);
        //返佣对象相关信息
		if(UserUtils.getUser().getCompany().getIsAllowMultiRebateObject()==1){
			ReviewNew review = reviewService.getReview(reviewId);
			List<Map<String,Object>> accountInfo = rebatesService.getRebateSupplier(review.getExtend2());
			if(CollectionUtils.isNotEmpty(accountInfo)){
				model.addAttribute("accountInfo", accountInfo.get(0));
			}else{
				model.addAttribute("rebateObject", review.getAgentName());
			}
		}
		model.addAttribute("isAllowMultiRebateObject", UserUtils.getUser().getCompany().getIsAllowMultiRebateObject());//是否多对象返佣
	}
	
	
	/**
	 * 新机票返佣支出凭单显示--仅供返佣审批页面使用
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "airticketRebatesPrintforReview")
	public String airticketRebatesPrintforReview(Model model,HttpServletRequest request, HttpServletResponse response) {

		String reviewId = request.getParameter("reviewId");		//返佣申请审核ID
		String groupCode = request.getParameter("groupCode");	//团号
		
		Map<String,Object> map = newAirticketrebatesService.airticketRebatesPrintData(reviewId);
		map.put("reviewId", reviewId);
		map.put("groupCode", groupCode);
		
		model.addAttribute("map", map);
	
		return "review/rebates/airticket/airticketRebatesPrintforReview";
	}
	
	/**
	 * 新机票返佣支出凭单显示
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "airticketRebatesNewPrint")
	public String airticketRebatesNewPrint(Model model,HttpServletRequest request, HttpServletResponse response) {

		String reviewId = request.getParameter("reviewId");		//返佣申请审核ID
		String groupCode = request.getParameter("groupCode");	//团号
		String payId = request.getParameter("payId");           //refund id
		Map<String,Object> map = newAirticketrebatesService.airticketRebatesPrintData(reviewId, payId);
		map.put("reviewId", reviewId);
		map.put("groupCode", groupCode);
		map.put("payId", payId);
		model.addAttribute("map", map);
	
		return "review/rebates/airticket/airticketRebatesNewPrint";
	}
	
	/**
	 * 新机票返佣下载支出凭单  ---仅供返佣审批页面使用
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="airticketRebatesDownloadforReview")
	public ResponseEntity<byte[]> airticketRebatesDownloadforReview(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String reviewId = request.getParameter("reviewId");		//返佣申请审核ID
		String groupCode = request.getParameter("groupCode");	//团号
		
		Map<String,Object> map = newAirticketrebatesService.airticketRebatesPrintData(reviewId);
		map.put("reviewId", reviewId);
		map.put("groupCode", groupCode);
		map.put("createDate", DateUtils.formatDate((Date)map.get("createDate"), "yyyy年 MM月dd日 "));
		
		File file = rebatesService.createRebatesSheetDownloadFile(map);
		
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName =  "支出凭单" + nowDate + ".doc";
		OutputStream os = null;
    	try {
			if(file != null && file.exists()){
				response.reset();
				response.setHeader("Content-Disposition", "attachment; filename="+new String(fileName.getBytes("gb2312"), "ISO-8859-1"));
				response.setContentType("application/octet-stream; charset=utf-8");
		    	os = response.getOutputStream();
				os.write(FileUtils.readFileToByteArray(file));
	            os.flush();
			}       		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null)
				try {
					os.close();
				} catch (Exception e) {
				}
		}
		return null;
	}
	
	/**
	 * 新机票返佣下载支出凭单
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="airticketRebatesNewDownload")
	public ResponseEntity<byte[]> airticketRebatesNewDownload(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		String reviewId = request.getParameter("reviewId");		//返佣申请审核ID
		String groupCode = request.getParameter("groupCode");	//团号
		String payId = request.getParameter("payId");
		
		Map<String,Object> map = newAirticketrebatesService.airticketRebatesPrintData(reviewId, payId);
		map.put("reviewId", reviewId);
		map.put("groupCode", groupCode);
		map.put("createDate", DateUtils.formatDate((Date)map.get("createDate"), "yyyy年 MM月dd日 "));
		if(Context.SUPPLIER_UUID_YJXZ.equals(companyUuid)){
			map.put("isYJXZ", "isYJXZ");
		}
		//更新打印时间
		//updatePrintTime(reviewId);
		
		File file = rebatesService.createRebatesSheetDownloadFile(map);
		
		String nowDate = DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd");
		String fileName =  "支出凭单" + nowDate + ".doc";
		OutputStream os = null;
    	try {
			if(file != null && file.exists()){
				response.reset();
				response.setHeader("Content-Disposition", "attachment; filename="+new String(fileName.getBytes("gb2312"), "ISO-8859-1"));
				response.setContentType("application/octet-stream; charset=utf-8");
		    	os = response.getOutputStream();
				os.write(FileUtils.readFileToByteArray(file));
	            os.flush();
			}       		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null)
				try {
					os.close();
				} catch (Exception e) {
				}
		}
		return null;
	}
	
	/**
	 * 新机票返佣打印支出凭单前更新打印状态方法
	 * @param reviewId
	 */
	@RequestMapping(value="updatePrintTime")
	@ResponseBody
	public void  updatePrintTime(String reviewId){
		String userId = UserUtils.getUser().getId().toString();
		try {
//			 ReviewResult result = reviewService.updatePrintFlag(userId,"1",reviewId);
			 reviewService.updatePrintFlag(userId,"1",reviewId);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} 
	}
	
	/**
	 * 显示新旧机票返佣支出凭单选择方法
	 * @param model
	 * @param request  isNew 1 旧数据  ，2 新数据
	 * @return
	 */
	@RequestMapping(value = "chooseAirticketRebatesPrint")
	public String chooseAirticketRebatesPrint(Model model,HttpServletRequest request) {
		//新旧返佣支出凭单通用字段
		String reviewId = request.getParameter("reviewId");		//返佣申请审核ID
		String groupCode = request.getParameter("groupCode");	//团号
		String payId = request.getParameter("payId");
		
		//新旧返佣支出凭单辨别字段
		String isNew = request.getParameter("isNew");
		Map<String,Object> map = new HashMap<String,Object>();
		String resultPage;
		if("2".equals(isNew)){//显示新返佣支出凭单
			map = newAirticketrebatesService.airticketRebatesPrintData(reviewId, payId);
			resultPage= "review/rebates/airticket/airticketRebatesNewPrint";
		}else{
			map = rebatesOldService.airticketRebatesPrintData(reviewId, payId);
			resultPage= "modules/review/airticketRebatesPrint";
		}
		map.put("reviewId", reviewId);
		map.put("groupCode", groupCode);
		model.addAttribute("map", map);
		return resultPage;
	}
	
	@SuppressWarnings("all")
	@ResponseBody
	@RequestMapping(value = "ajaxCheck")
	public List<Map<String, Object>> ajaxCheck(HttpServletRequest request) {
		String val = request.getParameter("val");
		String type = request.getParameter("type");
		String accountType = request.getParameter("accountType");
		String accountName = request.getParameter("accountName");
		if("supplyInfo".equals(type)){
			return rebatesService.getSupplyInfoList();
		}else if("accountType".equals(type)){
			return rebatesService.getAccountNameList(val ,accountType);
		}else{//accountNo
			return rebatesService.getAccountNoList(val ,accountType , accountName);
		}
	}
	
}
