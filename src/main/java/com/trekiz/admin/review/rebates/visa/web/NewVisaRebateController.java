package com.trekiz.admin.review.rebates.visa.web;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.ReviewResult;
import com.quauq.review.core.type.OrderByDirectionType;
import com.quauq.review.core.type.OrderByPropertiesType;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.modules.money.entity.MoneyAmount;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.rebates.service.RebatesService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.review.visaborrowmoney.service.IVisaBorrowMoneyService;
import com.trekiz.admin.modules.review.visaborrowmoney.web.VisaBorrowMoneyController;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.Traveler;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.review.changePrice.airticket.bean.ReviewResultBean;
import com.trekiz.admin.review.common.service.ICommonReviewService;
import com.trekiz.admin.review.rebates.singleGroup.service.RebatesNewService;
import com.trekiz.admin.review.rebates.visa.bean.VisaRebateInput;
import com.trekiz.admin.review.rebates.visa.service.NewVisaRebateService;

@Controller
@RequestMapping(value = "${adminPath}/visa/rebate")
public class NewVisaRebateController {
	
	private static String VISA_REBATE_LIST = "review/rebates/visa/visaRebateList" ;//签证返佣申请记录页面
	private static String VISA_REBATE_FORM = "review/rebates/visa/visaRebateForm" ;//签证返佣申请页面
	private static String VISA_REBATE_DETAIL = "review/rebates/visa/visaRebateDetail";//签证返佣申请详情页面
	
	
	@Autowired
	private VisaProductsService visaProductsService;
	@Autowired
	private RebatesService rebatesService;
	@Autowired
	private VisaOrderService visaOrderService;
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	@Autowired
	private TravelerService travelerService;
	@Autowired
	private IVisaBorrowMoneyService visaBorrowMoneyService;
	@Autowired
	private NewVisaRebateService newVisaRebateService;
	@Autowired
	private com.quauq.review.core.engine.ReviewService processReviewService;
	@Autowired
	private ICommonReviewService commonReviewService;
	@Autowired
	private RebatesNewService rebatesNewService;
	
	/**
	 * 跳转到返佣列表
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="visaRebateList")
	public String visaRebateList(Model model, HttpServletRequest request){
		String orderId = request.getParameter("orderId");
		model.addAttribute("companyId",UserUtils.getUser().getCompany().getId());
		// 订单ID
		model.addAttribute("orderId", orderId);
		model.addAttribute("orderType", Context.ORDER_TYPE_QZ);
		model.addAttribute("currUserId", UserUtils.getUser().getId());
		// 返佣记录List
		model.addAttribute("rebateRecordList", newVisaRebateService.findReviewListMap(orderId));
		return VISA_REBATE_LIST;
	}
	
	/**
	 * 跳转到返佣申请界面
	 * @param orderId
	 * @param orderStatus
	 * @return
	 */
	@RequestMapping(value ="visaRebateForm/{orderId}")
	public String visaRebateForm(@PathVariable Long orderId, Model model, HttpServletRequest request){
			//签证订单
			VisaOrder visaOrder = visaOrderService.findVisaOrder(orderId);
			model.addAttribute("visaOrder", visaOrder);
			model.addAttribute("visaOrderId", orderId);
			
			//团队预计返佣
			String groupRebatesUUID = visaOrder.getGroupRebate();
			if(StringUtils.isBlank(groupRebatesUUID)){
				model.addAttribute("groupRebates", "—");
			}else{
				MoneyAmount moneyAmount = moneyAmountService.findOneAmountBySerialNum(groupRebatesUUID);
				if(moneyAmount != null){
				if(moneyAmount.getAmount().compareTo(BigDecimal.ZERO) != 0){
					model.addAttribute("groupRebates", OrderCommonUtil.getMoneyAmountBySerialNum(groupRebatesUUID, 2));
				}else{
					model.addAttribute("groupRebates", "—");
				}
				}else{
					model.addAttribute("groupRebates", "—");
				}
			}
			
			
			//签证产品
			Long proId = visaOrder.getVisaProductId();
			if(null != proId && 0 != proId) {
				VisaProducts visaProduct = visaProductsService.findByVisaProductsId(proId);
				model.addAttribute("visaProduct", visaProduct);
			}
			
			//游客列表
			List<Traveler> travelerList = visaBorrowMoneyService.getTravelerList(orderId.toString());
			model.addAttribute("travelerList", travelerList);
			
			//累计返佣金额
			//通过订单ID查询reviewId
			List<ReviewNew> reviewList = processReviewService.getOrderReviewList(String.valueOf(orderId),String.valueOf(Context.ORDER_TYPE_QZ), String.valueOf(Context.REBATES_FLOW_TYPE));
			
			StringBuilder sb_trvids = new StringBuilder("");
			StringBuilder sb_rebatestrvcurrents = new StringBuilder("");
			StringBuilder sb_trvamounts = new StringBuilder("");
			if (reviewList != null && reviewList.size() > 0) {
				//获取activiti审核信息
				Map<String,Object> reviewAndDetailInfoMap = null;
				for (ReviewNew review : reviewList) {
					if(review.getId()!=null){
						reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(review.getId());
					}
					if (review.getStatus() == 2) {
						Object trvids = reviewAndDetailInfoMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID);
						Object rebatestrvcurrents =reviewAndDetailInfoMap.get( "rebatestrvcurrents");
						Object trvamounts =reviewAndDetailInfoMap.get( "trvamounts");
						if (trvids != null  && rebatestrvcurrents != null && trvamounts != null) {
							sb_trvids.append(trvids.toString());
							sb_rebatestrvcurrents.append(rebatestrvcurrents.toString());
							sb_trvamounts.append(trvamounts.toString());
						}
					}
				}
				
				
				reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(reviewList.get(0).getId());
				//累计返佣金额隐藏域
				Object totalRebatesJe = reviewAndDetailInfoMap.get("totalRebatesJe");
				if (totalRebatesJe != null ) {
					model.addAttribute("totalRebatesJe", totalRebatesJe.toString());
				} else {
					model.addAttribute("totalRebatesJe", "0");
				}
				
				//累计返佣金额隐藏域
				Object totalRebatesJe4update = reviewList.get(0).getExtend3();//此字段可以直接从review中读取
				if (totalRebatesJe4update != null ) {
					model.addAttribute("totalRebatesJe4update", totalRebatesJe4update.toString());
				} else {
					model.addAttribute("totalRebatesJe4update", "0");
				}
			} else {
				model.addAttribute("totalRebatesJe", "0");
				model.addAttribute("totalRebatesJe4update", "0");
			}
			String[] trvids = sb_trvids.toString().split(VisaBorrowMoneyController.SPLITMARK);
			String[] rebatestrvcurrents = sb_rebatestrvcurrents.toString().split(VisaBorrowMoneyController.SPLITMARK);
			String[] trvamounts = sb_trvamounts.toString().split(VisaBorrowMoneyController.SPLITMARK);
			Map<String, BigDecimal> map = null;
			for (Traveler traveler : travelerList) {
				map = new LinkedHashMap<String, BigDecimal>();
				for (int i = 0; sb_trvids.length() > 0 && i < trvids.length; i++) {
					if (traveler.getId().toString().equals(trvids[i])) {
						String currencyId = rebatestrvcurrents[i];
						String currencyPrice = trvamounts[i];
						if (!map.containsKey(currencyId)) {
							map.put(currencyId, new BigDecimal(currencyPrice));
						} else {
							map.put(currencyId,map.get(currencyId).add(new BigDecimal(currencyPrice)));
						}
					}
				}
				if (map.size() > 0){
					StringBuilder totalRebateJe = new StringBuilder("");
					Iterator<Entry<String, BigDecimal>> iter = map.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("rawtypes")
						Map.Entry entry = (Map.Entry) iter.next();
						String key = entry.getKey().toString();
						DecimalFormat df = new DecimalFormat("#,##0.00");
						String val = df.format(new BigDecimal(entry.getValue().toString()));
						Currency currency = currencyService.findCurrency(Long.parseLong(key));
						totalRebateJe.append(currency.getCurrencyMark());
						totalRebateJe.append(val);
						if (iter.hasNext()) {
							totalRebateJe.append("</br>");
						}
					}
					traveler.setTotalRebateJe(totalRebateJe.toString());
					totalRebateJe.delete(0,totalRebateJe.length()-1);
				} else {
					traveler.setTotalRebateJe("￥0.00");
				}
			}
		//币种列表
		List<Currency> currencyList = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("isAllowMultiRebateObject", UserUtils.getUser().getCompany().getIsAllowMultiRebateObject());//是否多对象返佣
		return VISA_REBATE_FORM;
	}
	
	
	/**
	 * 签证返佣申请
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "visaRebatesAppliy")
	public Map<String, String> visaRebatesAppliy(VisaRebateInput input,HttpServletRequest request,HttpServletResponse response) {
		
		input.setSupplyInfo(request.getParameter("supplyInfo"));
		input.setAccountNo(request.getParameter("accountNo"));
		Map<String,String> rMap = new HashMap<String, String>();
		if(StringUtils.isBlank(input.getVisaOrderId())){
			rMap.put("visaJKreply", "返佣输入参数异常！");
		}else{
			try {
				ReviewResultBean result = newVisaRebateService.applyStart(input);
				rMap.put("visaJKreply", result.getMessage().toString());
				if(result.getCode() != null) {
					rMap.put("code", result.getCode().toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
				rMap.put("visaJKreply", e.getMessage());
			}
		}
		return rMap;
	}
	/**
	 * 
	 * checkMutex  检查改价流程是否存在互斥
	 * @param model
	 * @param response
	 * @param request
	 * @exception
	 * @since  1.0.0
	 */
	@RequestMapping("checkMutex")
	@ResponseBody
	public Object checkMutex(VisaRebateInput input,Model model,HttpServletResponse response,HttpServletRequest request){
		ReviewResultBean result = newVisaRebateService.checkReview(input);
		
		return result;
	}
	/**
	 * 将币种和金额汇总后转换成如下格式  借款金额：￥800+$800
	 * 
	 * @param currencyIds:{"1,2,34,5","1,3,34,7"};
	 * @param currencyPrices:{"120.00,50.00,40.00,70.00","100.00,50.00,40.00,50.00"}
	 * @return
	 */
	private Map<String, BigDecimal> getTotalMoney(String[] currencyIds,String[] currencyPrices ){
		//合并所有游客各个币种的钱数
		Map<String, BigDecimal> map = new LinkedHashMap<String, BigDecimal>();
		for (int i = 0; i < currencyIds.length; i++) {
			String currencyId = currencyIds[i];
			String currencyPrice = currencyPrices[i];
			String[] currIds = currencyId.split(",");
			String[] currPrices = currencyPrice.split(",");
			for (int j = 0; j < currIds.length; j++) {
				String currId = currIds[j];
				String currPrice = currPrices[j];

				if (!map.containsKey(currId)) {
					map.put(currId, new BigDecimal(currPrice));
				} else {
					map.put(currId,map.get(currId).add(new BigDecimal(currPrice)));
				}
			}
		}
		return map;
	}
	
	
	/**
	 * 签证返佣取消
	 * @param request
	 * @param response
	 * @return
	 * @throws JSONException
	 */
	@ResponseBody
	@RequestMapping(value = "cancelVisaRebates")
	@Transactional
	public Map<String, Object> cancelVisaRebates(HttpServletRequest request,
		HttpServletResponse response) throws JSONException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		try{
			String reviewId = request.getParameter("revId");
			ReviewNew reviewNew = processReviewService.getReview(reviewId);
			if (null != reviewNew && 1 == reviewNew.getStatus().intValue()) {
				/*String userId,String companyId, String consigner, String reviewId, String comment, Map<String, Object> variables, CallbackService... callbackService*/
				ReviewResult result = processReviewService.cancel(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid().toString(), "", reviewId, "", null);
				if(result.getSuccess()) {
					map.put("success", "取消成功！");
					// 对成本录入进行更改
					ReviewNew review = processReviewService.getReview(reviewId);
					commonReviewService.updateCostRecordStatus(review, 3);
				} else {
					map.put("success", "取消失败！");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			map.put("success", "取消失败！");
		}
		
		return map;
	}
	
	/**
	 * 跳转到返佣详情界面
	 * @param orderId
	 * @param orderStatus
	 * @return
	 */
	@RequestMapping(value="visaRebateDetail")
	public String visaRebateDetail(Model model, HttpServletRequest request){
		String orderId = request.getParameter("orderId");
		//String travelerId = request.getParameter("travelerId");
		//String flag = request.getParameter("flag");
		String reviewId = request.getParameter("reviewId");
		//String flowType = request.getParameter("flowType");
		//String nowLevel = request.getParameter("nowLevel");
		
		//签证订单
		VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderId));
		model.addAttribute("visaOrder", visaOrder);
		model.addAttribute("visaOrderId", orderId);
		
		//团队预计返佣
		String groupRebatesUUID = visaOrder.getGroupRebate();
		if(StringUtils.isBlank(groupRebatesUUID)){
			model.addAttribute("groupRebates", "—");
		}else{
			MoneyAmount moneyAmount = moneyAmountService.findOneAmountBySerialNum(groupRebatesUUID);
			if(moneyAmount != null){
				if(moneyAmount.getAmount().compareTo(BigDecimal.ZERO) != 0){
					model.addAttribute("groupRebates", OrderCommonUtil.getMoneyAmountBySerialNum(groupRebatesUUID, 2));
				}else{
					model.addAttribute("groupRebates", "—");
				}
			}else{
				model.addAttribute("groupRebates", "—");
			}
		}
		
		//签证产品
		Long proId = visaOrder.getVisaProductId();
		if(null != proId && 0 != proId) {
			VisaProducts visaProduct = visaProductsService.findByVisaProductsId(proId);
			model.addAttribute("visaProduct", visaProduct);
		}
		
		//获取activiti审核信息
		Map<String,Object> reviewAndDetailInfoMap = null;
		try{
			if(reviewId!=null){
				reviewAndDetailInfoMap = processReviewService.getReviewDetailMapByReviewId(reviewId);
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		List<Map<String,String>> trvList = new ArrayList<Map<String,String>>();
		List<Map<String, String>> groupList = new ArrayList<Map<String, String>>();
		Map<String, String> trvMap = null;
		Map<String, String> groupMap = null;
		if(reviewAndDetailInfoMap != null) {
			String[] travelerIdS = reviewAndDetailInfoMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID).toString().split(VisaBorrowMoneyController.SPLITMARK);
			String[] travelerNameS = reviewAndDetailInfoMap.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME).toString().split(VisaBorrowMoneyController.SPLITMARK);
			String[] trvcurrencyNameS = reviewAndDetailInfoMap.get("trvcurrencyNames").toString().split(VisaBorrowMoneyController.SPLITMARK);
			String[] trvcurrencyMarkS = reviewAndDetailInfoMap.get("trvcurrencyMarks").toString().split(VisaBorrowMoneyController.SPLITMARK);
			String[] trvborrownameS = reviewAndDetailInfoMap.get("trvborrownames").toString().split(VisaBorrowMoneyController.SPLITMARK);
			String[] trvsettlementpriceS = reviewAndDetailInfoMap.get("trvsettlementprices").toString().split(VisaBorrowMoneyController.SPLITMARK);
			String[] trvamountS = reviewAndDetailInfoMap.get("trvamounts").toString().split(VisaBorrowMoneyController.SPLITMARK);
			String[] trvrebatesnoteS = reviewAndDetailInfoMap.get("trvrebatesnotes").toString().split(VisaBorrowMoneyController.SPLITMARK);

			//通过orderId获取产品的发布部门
			Long deptId = visaOrderService.getProductPept(Long.valueOf(orderId));
			List<Map<String, Object>> processList = processReviewService.getReviewDetailMapListByOrderId(deptId, Context.ORDER_TYPE_QZ, Context.REBATES_FLOW_TYPE, orderId, OrderByPropertiesType.CREATE_DATE,OrderByDirectionType.DESC);
			
			//游客累计返佣金额
			StringBuilder sb_trvids = new StringBuilder("");
			StringBuilder sb_rebatestrvcurrents = new StringBuilder("");
			StringBuilder sb_trvamounts = new StringBuilder("");
			if(CollectionUtils.isNotEmpty(processList)) {
				for(Map<String, Object> review : processList) {
					if(review.get("status") != null && "2".equals(review.get("status").toString())) {
						Map<String, Object> reviewAndDetailInfoMapTemp = processReviewService.getReviewDetailMapByReviewId(review.get("id").toString());
						if(reviewAndDetailInfoMapTemp.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID)==null||reviewAndDetailInfoMap.get("rebatestrvcurrents")==null||reviewAndDetailInfoMap.get("trvamounts")==null){
							continue;
						}
						String[] tids = reviewAndDetailInfoMapTemp.get(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID).toString().split(VisaBorrowMoneyController.SPLITMARK);
						String[] rcurrents = reviewAndDetailInfoMapTemp.get("rebatestrvcurrents").toString().split(VisaBorrowMoneyController.SPLITMARK);
						String[] tamounts = reviewAndDetailInfoMapTemp.get("trvamounts").toString().split(VisaBorrowMoneyController.SPLITMARK);
						
						if (tids != null && tids.length > 0 && rcurrents != null && rcurrents.length > 0 && tamounts != null && tamounts.length > 0) {
							sb_trvids.append(tids[0]+VisaBorrowMoneyController.SPLITMARK);
							sb_rebatestrvcurrents.append(rcurrents[0]+VisaBorrowMoneyController.SPLITMARK);
							sb_trvamounts.append(tamounts[0]+VisaBorrowMoneyController.SPLITMARK);
						}
					}
				}
			}
			
			for (int i = 0; i < travelerIdS.length; i++) {
				//edit by majiancheng 2015-12-25 过滤空的游客信息
				if(StringUtils.isEmpty(travelerIdS[i])) {
					continue;
				}
				
				Map<String, BigDecimal> map = null;
				trvMap = new HashMap<String, String>();
				trvMap.put("travelerId", travelerIdS[i]);
				trvMap.put("travelerName", travelerNameS[i]);
				trvMap.put("trvcurrencyName", trvcurrencyNameS[i]);
				trvMap.put("trvcurrencyMark", trvcurrencyMarkS[i]);
				trvMap.put("trvborrowname", trvborrownameS[i]);
				trvMap.put("trvsettlementprice", trvsettlementpriceS[i]);
				trvMap.put("trvamount", trvamountS[i]);
				trvMap.put("trvrebatesnote", trvrebatesnoteS[i]);
				
				
				Traveler reTraveler = travelerService.findTravelerById(Long.parseLong(travelerIdS[i]));
				trvMap.put("rebatesMoneySerialNum", reTraveler.getRebatesMoneySerialNum());
				
				String[] tids = sb_trvids.toString().split(VisaBorrowMoneyController.SPLITMARK);
				String[] rcurrents = sb_rebatestrvcurrents.toString().split(VisaBorrowMoneyController.SPLITMARK);
				String[] tamounts = sb_trvamounts.toString().split(VisaBorrowMoneyController.SPLITMARK);
				map = new LinkedHashMap<String, BigDecimal>();
				for (int j = 0; sb_trvids.length() > 0 && j < tids.length; j++) {
					if (travelerIdS[i].toString().equals(tids[j])) {
						String currencyId = rcurrents[j];
						String currencyPrice = tamounts[j];
						if (!map.containsKey(currencyId)) {
							map.put(currencyId, new BigDecimal(currencyPrice));
						} else {
							map.put(currencyId,map.get(currencyId).add(new BigDecimal(currencyPrice)));
						}
					}
				}
				if (map.size() > 0){
					StringBuilder totalRebateJe = new StringBuilder("");
					Iterator<Entry<String, BigDecimal>> iter = map.entrySet().iterator();
					while (iter.hasNext()) {
						@SuppressWarnings("rawtypes")
						Map.Entry entry = (Map.Entry) iter.next();
						String key = entry.getKey().toString();
						DecimalFormat df = new DecimalFormat("#,##0.00");
						String val = df.format(new BigDecimal(entry.getValue().toString()));
						Currency currency = currencyService.findCurrency(Long.parseLong(key));
						totalRebateJe.append(currency.getCurrencyMark());
						totalRebateJe.append(val);
						if (iter.hasNext()) {
							totalRebateJe.append("</br>");
						}
					}
					trvMap.put("totalRebateJe", totalRebateJe.toString());
					totalRebateJe.delete(0,totalRebateJe.length()-1);
				} else {
					trvMap.put("totalRebateJe", "￥0.00");
				}
				trvList.add(trvMap);
			
			}
			model.addAttribute("travelerList", trvList);
		}
		

		//团队返佣
		String[] grouprebatesnameS = reviewAndDetailInfoMap.get("grouprebatesnames").toString().split(VisaBorrowMoneyController.SPLITMARK);
		String[] groupcurrencyNameS = reviewAndDetailInfoMap.get("groupcurrencyNames").toString().split(VisaBorrowMoneyController.SPLITMARK);
		String[] groupcurrencyMarkS = reviewAndDetailInfoMap.get("groupcurrencyMarks").toString().split(VisaBorrowMoneyController.SPLITMARK);
		String[] grouprebatesamountS = reviewAndDetailInfoMap.get("grouprebatesamounts").toString().split(VisaBorrowMoneyController.SPLITMARK);
		String[] grouprebatesnodeS = reviewAndDetailInfoMap.get("grouprebatesnodes").toString().split(VisaBorrowMoneyController.SPLITMARK);
		

		for (int i = 0; i < grouprebatesamountS.length; i++) {
			groupMap = new HashMap<String, String>();
			groupMap.put("grouprebatesname", grouprebatesnameS[i]);
			groupMap.put("groupcurrencyName", groupcurrencyNameS[i]);
			groupMap.put("groupcurrencyMark", groupcurrencyMarkS[i]);
			groupMap.put("grouprebatesamount", grouprebatesamountS[i]);
			groupMap.put("grouprebatesnode", grouprebatesnodeS[i]);
			groupList.add(groupMap);
		}
		
		model.addAttribute("groupList", groupList);
		
		String totalrebatesamount = reviewAndDetailInfoMap.get(Context.REVIEW_VARIABLE_KEY_REBATES_MARK_TOTAL_MONEY).toString();
		model.addAttribute("totalrebatesamount", totalrebatesamount);
		
		model.addAttribute("rid",reviewId);
		
		String shenfen = request.getParameter("shenfen");
		model.addAttribute("shenfen",shenfen);
		model.addAttribute("isReview", request.getParameter("isReview"));
		 //返佣对象相关信息
		if(UserUtils.getUser().getCompany().getIsAllowMultiRebateObject()==1){
			ReviewNew review = processReviewService.getReview(reviewId);
			List<Map<String,Object>> accountInfo = rebatesNewService.getRebateSupplier(review.getExtend2());
			if(CollectionUtils.isNotEmpty(accountInfo)){
				model.addAttribute("accountInfo", accountInfo.get(0));
			}else{
				model.addAttribute("rebateObject", review.getAgentName());
			}
		}
		model.addAttribute("isAllowMultiRebateObject", UserUtils.getUser().getCompany().getIsAllowMultiRebateObject());//是否多对象返佣
		return VISA_REBATE_DETAIL;
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
		Integer orderType = Integer.parseInt(request.getParameter("orderType"));
		@SuppressWarnings("rawtypes")
		List rebatesList = rebatesService.findVisaRebatesListByStatus(orderType, Context.REBATES_FLOW_TYPE, orderId);
		if(rebatesList != null && rebatesList.size() > 0){
			return "false1";
		}else{
			VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderId));
			VisaProducts visaProducts = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			//对结算单状态进行判断
			if (1 == visaProducts.getLockStatus()) {
				return "false2";
			}
		}
		return "true";
	}
	
}
