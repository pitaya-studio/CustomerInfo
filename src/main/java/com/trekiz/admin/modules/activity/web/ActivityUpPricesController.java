package com.trekiz.admin.modules.activity.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.utils.Arith;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.activity.service.IActivityUpPricesService;
import com.trekiz.admin.modules.activity.service.TravelActivityService;
import com.trekiz.admin.modules.airticket.service.UPPricesGenerateHTML;
import com.trekiz.admin.modules.hotel.entity.ActivityHotel;
import com.trekiz.admin.modules.hotel.entity.HotelOrder;
import com.trekiz.admin.modules.hotel.service.ActivityHotelService;
import com.trekiz.admin.modules.hotel.service.HotelOrderService;
import com.trekiz.admin.modules.island.entity.ActivityIsland;
import com.trekiz.admin.modules.island.entity.IslandOrder;
import com.trekiz.admin.modules.island.service.ActivityIslandService;
import com.trekiz.admin.modules.island.service.IslandOrderService;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderReviewService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.review.changeprice.entity.ChangePriceBean;
import com.trekiz.admin.modules.review.changeprice.service.IChangePriceReviewService;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * ActivityUpPricesController.java 功能:单团、散拼类产品的订单改价 控制器
 * @author HPT
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/activityUpProces/")
public class ActivityUpPricesController extends BaseController{
	
	private static String DEF_MT_YSDJ = "20",DEF_MT_JSJ = "3" ;   // 原始定金结算价 ，当前结算价 的 moneyType
	@Autowired
	private IActivityUpPricesService activityUpPricesService;
	
	@Autowired
	private ReviewService reviewService ;
	
	@Autowired
	private MoneyAmountService moneyAmountService ;
	
	@Autowired
    private OrderCommonService orderService;
	
	@Autowired
	@Qualifier("activityGroupSyncService")
    private IActivityGroupService activityGroupService;
	
	@Autowired
	private CurrencyService currencyService;
	/**
	 * 订单流程服务者
	 */
	@Autowired
	private OrderReviewService orderReviewService ;
	@Autowired
	private IChangePriceReviewService changePriceReviewService;
	@Autowired
	private HotelOrderService hotelOrderService;
	@Autowired
	private ActivityHotelService activityHotelService;
	@Autowired
	private IslandOrderService islandOrderService;
	@Autowired
	private ActivityIslandService activityIslandService;
	@Autowired
	private TravelActivityService travelActivityService;
	@SuppressWarnings("rawtypes")
    private static Map  moneyTypes = new HashMap<String, String>(); // 定义币种信息
	
	/**
	 * 查询机票改价记录
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="list")
	public String queryHistroyAUP(Model model,HttpServletResponse response,HttpServletRequest request){
		String orderId = request.getParameter("orderId") ; // 订单编号
		String productType = request.getParameter("productType"); //订单类型
		String flowType = request.getParameter("flowType");  // 流程类型
		if(StringUtils.isBlank(flowType)) {
			flowType = "10";
		}
		boolean flag = reviewService.verifyWorkFlowStatus(orderId, Integer.parseInt(productType), Integer.parseInt(flowType));
		// 调用公用工作流查询方法,取到流程表里的机票改价记录信息
		List<Map<String, Object>> list = reviewService.findReviewListMap(Integer.parseInt(productType), Integer.parseInt(flowType),orderId, false,"");
		Map<String,String> map = new HashMap<String, String>();
		map.put("orderId", orderId);
		map.put("productType",productType);
		map.put("flowType", flowType);
		model.addAttribute("airticketReturnList", list);
		model.addAllAttributes(map);
		model.addAttribute("flag", flag);
		return "modules/activity/activityUpPrices";
	}
	
	/**
	 * 查询单个改价信息
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="changePrice")
	public String querySingleChangePrice(Model model,HttpServletResponse response,HttpServletRequest request){
		String orderId = request.getParameter("orderId") ; // 订单编号
		String productType = request.getParameter("productType"); //订单类型
		String flowType = request.getParameter("flowType");  // 流程类型
		String idStr = request.getParameter("id");
		Long id = Long.parseLong(idStr);
		
		if(StringUtils.isBlank(flowType)) {
			flowType = "10";
		}
		// 调用公用工作流查询方法,取到流程表里的机票改价记录信息
		List<Map<String, Object>> list = reviewService.findReviewListMapById(Integer.parseInt(productType), Integer.parseInt(flowType),orderId,id);
		Map<String,String> map = new HashMap<String, String>();
		map.put("orderId", orderId);
		map.put("productType",productType);
		map.put("flowType", flowType);
		model.addAttribute("airticketReturnList", list);
		model.addAllAttributes(map);
		
		ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		model.addAttribute("productOrder", productOrder);
		model.addAttribute("productGroup", productGroup);
		model.addAttribute("product", product);
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		model.addAttribute("rid", idStr);
		return "modules/activity/activityChangePrices";
	}
	
	/**
	 * 跳转到改价详情页面(申请改价页面)
	 * @param model
	 * @param response
	 * @param request
	 * @return 
	 */
	@RequestMapping(value="upAirPrices")
	public String gotoUpAirPrices(Model model,HttpServletResponse response,HttpServletRequest request){
		String orderId = request.getParameter("orderId") ; // 订单编号
		String productType = request.getParameter("productType"); //订单类型
		String flowType = request.getParameter("flowType");  // 流程类型
		String productTypeSecond = request.getParameter("productType");
		if(StringUtils.isBlank(orderId)	) {
			throw new RuntimeException("订单id不能为空");
		}
		
		ProductOrderCommon productOrderCommon = orderService.getProductorderById(StringUtils.toLong(orderId));
		//订单定金的原始应收价和游客的原始应收价的获取方式不一样		
//		List<Map<String,Object>> frontList = activityUpPricesService.queryFrontMoneyList(orderId);  //  获取订单定金~
//		Map<String,Object> rawMapDJ = activityUpPricesService.handleFrontMoney(frontList);  // 处理后的定金
//		model.addAttribute("resultMaps", new HashMap<String, Object>().put("frontmoney", rawMapDJ));// 返回结果集合 
		// 获取游客列表信息~
		List<Map<String,Object>> travelerList = activityUpPricesService.queryTravelerList(orderId);
		model.addAttribute("travelerList", travelerList);
		
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		model.addAttribute("curlist", currencylist);
		model.addAttribute("productTypeSecond", productTypeSecond);
		Map<String,String> map = new HashMap<String, String>();
		map.put("orderId", orderId);
		map.put("productType",productType);
		map.put("flowType", flowType);
		map.put("orderTotalMoney", productOrderCommon.getTotalMoney());
		model.addAllAttributes(map);
		
		model.addAttribute("productOrder", productOrderCommon);
		TravelActivity product = travelActivityService.findById(productOrderCommon.getProductId());
		model.addAttribute("product", product);
		ActivityGroup productGroup = activityGroupService.findById(productOrderCommon.getProductGroupId());
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrderCommon.getPayMode()));
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrderCommon.getOrderStatus().toString()));
		
		return "modules/activity/upActivityDetails";
	}
	/**
	 * 申请改价
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@SuppressWarnings({  "rawtypes", "unchecked" })
	@RequestMapping(value="applyForUpAirPrices")
	@ResponseBody
	public Object applyForUpAirTicketPrices(Model model, HttpServletResponse response, HttpServletRequest request) {
		Map param = getRequestParamater(request);
		
		String orderId = request.getParameter("orderId") ; // 订单编号
		String productType = request.getParameter("productType"); //产品类型
		String flowType = request.getParameter("flowType");  // 流程类型
		
		if (null != param.get("sign")) {  // 重新申请改价流程
			String revid = request.getParameter("revid");
			reviewService.removeReview(Long.parseLong(revid));
			return gotoUpAirPrices(model,response,request);
		}
	
		 
		StringBuffer sbuffer = new StringBuffer();
		 
		moneyTypes = UPPricesGenerateHTML.getCurrencyParam(currencyService);
		 
		/* 1.游客改价申请**/
		travelerForApplyFlow(param, orderId, productType, flowType, sbuffer);
//		/* 2.订单定价改价申请**/
//		frontMoneyForApplyFlow(param,orderId, productType, flowType, sbuffer);
//		/* 3.订单团队改价申请**/
//		teamOrderForApplyFlow(param,orderId, productType, flowType, sbuffer);
		
		super.saveUserOpeLog(Context.log_type_orderform, Context.log_type_orderform, "单团申请改价-  回调流程接口" +
				"返回信息: " + sbuffer.toString(), Context.log_state_up, productType != null ? Integer.parseInt(productType) : null, Long.parseLong(orderId));

		Map<String,String> rMap = new HashMap<String, String>();
		rMap.put("sbinfo", sbuffer.toString());
		rMap.put("orderId",orderId);
		rMap.put("productType",productType);
		rMap.put("flowType",flowType);
		
		return rMap;
		//return "redirect:"+Global.getAdminPath()+"/activityUpProces/list?orderId=" + orderId + "&productType=" + productType + "&flowType=" + flowType;
//		return queryHistroyAUP(model,response,request);
	}
	
	/**
	 * 申请改价
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@SuppressWarnings({  "rawtypes", "unchecked" })
	@RequestMapping(value="applyForUpHotelPrices")
	@ResponseBody
	public Object applyForUpHotelTicketPrices(Model model,HttpServletResponse response,HttpServletRequest request){
		Map param = getRequestParamater(request);
		
		String orderId = request.getParameter("orderId") ; // 订单编号
		String productType = request.getParameter("productType"); //产品类型
		String flowType = request.getParameter("flowType");  // 流程类型
		String orderUuid = request.getParameter("orderUuid");  // 流程类型
	
		HotelOrder order = hotelOrderService.getByUuid(orderUuid);
		ActivityHotel activity = activityHotelService.getByUuid(order.getActivityHotelUuid());
		StringBuffer sbuffer = new StringBuffer();
		 
		moneyTypes = UPPricesGenerateHTML.getCurrencyParam(currencyService);
		 
		/* 1.游客改价申请**/
		travelerForHotelApplyFlow(param, orderId, productType, flowType, sbuffer,orderUuid,activity.getDeptId().longValue());
		
		super.saveUserOpeLog(Context.log_type_orderform, Context.log_type_orderform, "酒店申请改价-  回调流程接口" +
				"返回信息: "+sbuffer.toString(), Context.log_state_up, productType != null ? Integer.parseInt(productType) : null, Long.parseLong(orderId));

		Map<String,String> rMap = new HashMap<String, String>();
		rMap.put("sbinfo", sbuffer.toString());
		rMap.put("orderId",orderId);
		rMap.put("productType",productType);
		rMap.put("flowType",flowType);
		
		return rMap;
	}
	/**
	 * 申请改价
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@SuppressWarnings({  "rawtypes", "unchecked" })
	@RequestMapping(value="applyForUpIslandPrices")
	@ResponseBody
	public Object applyForUpIslandPrices(Model model,HttpServletResponse response,HttpServletRequest request){
		Map param = getRequestParamater(request);
		
		String orderId = request.getParameter("orderId") ; // 订单编号
		String productType = request.getParameter("productType"); //产品类型
		String flowType = request.getParameter("flowType");  // 流程类型
		String orderUuid = request.getParameter("orderUuid");  // 流程类型
		param.put("orderUuid", orderUuid);
	
		IslandOrder order = islandOrderService.getByUuid(orderUuid);
		ActivityIsland activity = activityIslandService.getByUuid(order.getActivityIslandUuid());
		StringBuffer sbuffer = new StringBuffer();
		 
		moneyTypes = UPPricesGenerateHTML.getCurrencyParam(currencyService);
		 
		/* 1.游客改价申请**/
		travelerForIslandApplyFlow(param, orderId, productType, flowType, sbuffer,orderUuid,activity.getDeptId().longValue());
//		/* 2.订单定价改价申请**/
//		frontMoneyForApplyFlow(param,orderId, productType, flowType, sbuffer);
//		/* 3.订单团队改价申请**/
//		teamOrderForApplyFlow(param,orderId, productType, flowType, sbuffer);
		
		super.saveUserOpeLog(Context.log_type_orderform, Context.log_type_orderform, "酒店申请改价-  回调流程接口" +
				"返回信息: "+sbuffer.toString(), Context.log_state_up, productType != null ? Integer.parseInt(productType) : null, Long.parseLong(orderId));

		Map<String,String> rMap = new HashMap<String, String>();
		rMap.put("sbinfo", sbuffer.toString());
		rMap.put("orderId",orderId);
		rMap.put("productType",productType);
		rMap.put("flowType",flowType);
		
		return rMap;
		//return "redirect:"+Global.getAdminPath()+"/activityUpProces/list?orderId=" + orderId + "&productType=" + productType + "&flowType=" + flowType;
//		return queryHistroyAUP(model,response,request);
	}
	/**
	 * 取消申请
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="cancelTravlerReturnRequest")
	public String cancelTravlerReturnRequest(Model model,HttpServletResponse response,HttpServletRequest request){
		
		String orderId = request.getParameter("orderId") ; // 订单编号
		String productType = request.getParameter("productType"); //产品类型
		String flowType = request.getParameter("flowType");  // 流程类型
		
		String revid = request.getParameter("revid");
		reviewService.removeReview(Long.parseLong(revid));
		return "redirect:"+Global.getAdminPath()+"/activityUpProces/list?orderId=" + orderId + "&productType=" + productType + "&flowType=" + flowType;
//		return queryHistroyAUP(model,response,request);
	}
	/**
	 * 获取前台请求参数
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getRequestParamater(HttpServletRequest request) {
		Map parmap = request.getParameterMap();
		Set  set = parmap.entrySet();
		Iterator it = set.iterator();
		Map map = new HashMap();
		while(it.hasNext()){
			Entry<String, Object> entry = (Entry<String, Object>) it.next();
			map.put(entry.getKey().toLowerCase(), entry.getValue());
		}
		return  map;
	}
	
	/**
	 * 游客改价申请
	 * @param param 游客信息
	 * @param orderId  订单编号
	 * @param productType 产品类型
	 * @param flowType 流程类型
	 * @return 返回调用工作流的执行结果
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void  travelerForApplyFlow(Map<String,Object> param,String orderId,String productType,String flowType,StringBuffer sbuffer){
//		String [] plusys  = (String[]) param.get("plusys");  // 游客差价
//		String [] afterPrices  = (String[]) param.get("plusys");  // 游客改后金额
		String [] plusys  = (String[]) param.get("plusystrue");  // 游客差价
		String [] travelerids = (String[]) param.get("travelerids"); // 游客编号
		String [] gaijiacurency =(String[]) param.get("gaijiacurency"); // 游客币种
		String [] travelerremark = (String[]) param.get("travelerremark"); // 游客备注
		
		if(loopCheckArrs(plusys)){ 
			sbuffer.append(" 游客差价列表为空！ ");
			return;
		}
		
		for(int i=0;i<plusys.length;i++){//		把 改价差额"0"值 统一为"0.00"
			if(Double.parseDouble(plusys[i]) == 0){
				plusys[i] = "0.00";
			}
		}
		// 获取游客列表信息~
		List<Map<String,Object>> travelerList = activityUpPricesService.queryTravelerList(orderId);
		String trid = "";
		double chae = calculatePrices(plusys); //计算订单改价差额
		
		// add by yunpeng.zhang 针对需求 C369,越柬行踪改价
		Map<String, Double> timesAndRmbPrice = calculatePrices(plusys, gaijiacurency);
		
//		/**
//		 * 验证是否游轮产品改价 
//		 */
//		try{
//		if(productType.equals("10")){  // 游轮产品订单
//		    looseUppricess(param,orderId,productType,flowType,sbuffer); // 游轮产品订单改价
//		    return ;
//		}
//		}catch(IndexOutOfBoundsException e){
//		    sbuffer.append("没有配置改价流程!");
//		    return ;
//		}
		
		// 如果批发商为越柬行踪， 低改在80元内无需审核，直接通过。 add by yunpeng.zhang
		String companyName = UserUtils.getUser().getCompany().getCompanyName();
		if("越柬行踪".equals(companyName) && timesAndRmbPrice.get("rmbChae") >= 0.00 && timesAndRmbPrice.get("times") > 0 
			&& timesAndRmbPrice.get("wbChae").equals(timesAndRmbPrice.get("rmbChae"))) {
			hqxShenQingTiaoJia(param, orderId, productType, flowType, sbuffer);
			return ;
		}
		
		/**
		 * 验证环球行公司申请改价并且是向上调价 --- 直接修改差额不需要走审核流程
		 * update by ruyi.chen
		 * 所有公司暂都修改成不走审核流程
		 */
		if(chae>0){
			hqxShenQingTiaoJia(param, orderId, productType, flowType, sbuffer);
			return ;
		}
		
		for(int i=0;i<plusys.length;i++){
			if(plusys[i].equals("0.00")){  // 改差价为默认的 ，就不启动工作流申请改价！
				continue;
			}
			// 流程属性数据
			List<Detail> listDetail = new ArrayList<Detail>(); 
			trid = travelerids[i];
			Detail id = new Detail(ChangePriceBean.TRVALER_ID,travelerids[i]);// 游客id
			Detail name = new Detail(ChangePriceBean.TRAVALER_NAME,"");	// 游客名称
			Detail cid = new Detail(ChangePriceBean.CURRENCY_ID,gaijiacurency[i]); // 游客币种id
			Detail cname = new Detail(ChangePriceBean.CURRENCY_NAME,String.valueOf(moneyTypes.get(String.valueOf(gaijiacurency[i])))); // 币种名称
			Detail oldm = new Detail(ChangePriceBean.OLD_TOTAL_MONEY,"0.00"); //原始应收价
			Detail curm = new Detail(ChangePriceBean.CUR_TOTAL_MONEY,"0.00"); //当前应收价
			Detail cfund = new Detail(ChangePriceBean.CHANGED_PRICE,plusys[i]); // 改价差额
			Detail remark = new Detail(ChangePriceBean.CHANGEDREMARK,travelerremark[i]); // 改价备注
			Detail chm = new Detail(ChangePriceBean.CHANGED_TOTAL_MONEY,"0.00"); // 改价后的应收价
			Detail chp = new Detail(ChangePriceBean.CHANGED_FUND,"应收价");  // 改价款项
			// 获取游客的 原始应收价 和 当前应收价 
			for(int j =0;j<travelerList.size();j++){
				Map raw = travelerList.get(j);
				
				if(id.getValue().equals("")){
					trid = travelerids[i-1];
					id.setValue(travelerids[i-1]);
					if(id.getValue().equals("")){
						trid = travelerids[i-2];
						id.setValue(travelerids[i-2]);
						if(id.getValue().equals("")){
							trid = travelerids[i-3];
							id.setValue(travelerids[i-3]);
							if(id.getValue().equals("")){
								trid = travelerids[i-4];
								id.setValue(travelerids[i-4]);
							}
						}
					}
				}
				
				
				
				if(id.getValue().equals(raw.get("travelerid").toString())){ //  同一个游客的信息
					name.setValue(String.valueOf(raw.get("travelername"))); // 赋值游客名称
//					if(id.getValue().equals("3055")){
//						System.out.println("游客名称:" +raw.get("travelername"));
//						System.out.println("游客币种:"+ raw.get("travelers") );
//						
//					}
				//	if(moneys.size() ==0){
					List<Map<String,Object>> 	moneys = (List<Map<String,Object>>) raw.get("travelers") ; // 游客币种
				//	}
					
					// 遍历游客的币种信息
					for(int mi = 0;mi<moneys.size();mi++){
						Map temp = moneys.get(mi); // 游客的币种信息
						if(gaijiacurency[i].equals(temp.get("currency_id").toString())){   // 币种确认
							oldm.setValue(String.valueOf(temp.get("oldtotalmoney"))); // 赋值游客的原始应收价
							curm.setValue(String.valueOf(temp.get("amount"))); // 赋值游客的当前应收价
							double f = Arith.add(Double.parseDouble(curm.getValue()),Double.parseDouble(plusys[i]));// 计算 改后应收价
							chm.setValue(String.valueOf(f)); // 赋值改后应收价
							break;
						}else{
							double f = Arith.add(Double.parseDouble("0.00"),Double.parseDouble(plusys[i]));// 计算 改后应收价
							chm.setValue(String.valueOf(f));  //赋值 改后应收价
							continue;
						}
						
					}
				}
				continue;
			}
			listDetail.add(id);
			listDetail.add(name);
			listDetail.add(cid);
			listDetail.add(cname);
			listDetail.add(oldm);
			listDetail.add(curm);
			listDetail.add(cfund);
			listDetail.add(remark);
			listDetail.add(chm);
			listDetail.add(chp);
			/*
			 *  调用工作流 申请【游客信息】改价
			 */
			boolean flagSign = activityUpPricesService.verifyTravelerFlowsign(orderId, productType, flowType,gaijiacurency[i],id.getValue());
			if(flagSign){ // 验证此流程是否申请记录
				reviewService.addReview(Integer.parseInt(productType), Integer.parseInt(flowType), 
						orderId, Long.parseLong(travelerids[i].equals("")?trid:travelerids[i]), 0l, travelerremark[i], sbuffer, 
						listDetail, orderService.getProductPept(orderId)); 
				if(sbuffer.length()>0){
					break;
				}
			}
			
		}
		
	}
	
	public void  travelerForHotelApplyFlow(Map<String,Object> param,String orderId,String productType,String flowType,StringBuffer sbuffer,String orderUuid,Long deptId){
		/**
		 * 验证环球行公司申请改价并且是向上调价 --- 直接修改差额不需要走审核流程
		 * update by ruyi.chen
		 * 所有公司暂都修改成不走审核流程
		 * 酒店申请改价，不论高改低改都不走审核流程
		 * update by yunpeng.zhang 2015年9月7日
		 */

		hqxShenQingTiaoJiaHotel(param, orderId, productType, flowType, sbuffer,orderUuid,deptId);

		
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void  travelerForIslandApplyFlow(Map<String,Object> param,String orderId,String productType,String flowType,StringBuffer sbuffer,String orderUuid,Long deptId){
		String [] plusys  = (String[]) param.get("plusystrue");  // 游客差价
		String [] travelerids = (String[]) param.get("travelerids"); // 游客编号
		String [] gaijiacurency =(String[]) param.get("gaijiacurency"); // 游客币种
		String [] travelerremark = (String[]) param.get("travelerremark"); // 游客备注
		
		if(loopCheckArrs(plusys)){ 
			sbuffer.append(" 游客差价列表为空！ ");
			return;
		}
		for(int i=0;i<plusys.length;i++){//		把 改价差额"0"值 统一为"0.00"
			if(Double.parseDouble(plusys[i]) == 0){
				plusys[i] = "0.00";
			}
		}
		// 获取游客列表信息~
		List<Map<String,Object>> travelerList = activityUpPricesService.queryIslandTravelerList(orderUuid);
		String trid = "";
//		double chae = calculatePrices(plusys); //计算订单改价差额
//		/**
//		 * 验证是否游轮产品改价 
//		 */
//		try{
//		if(productType.equals("10")){  // 游轮产品订单
//		    looseUppricess(param,orderId,productType,flowType,sbuffer); // 游轮产品订单改价
//		    return ;
//		}
//		}catch(IndexOutOfBoundsException e){
//		    sbuffer.append("没有配置改价流程!");
//		    return ;
//		}
		
		/**
		 * 验证环球行公司申请改价并且是向上调价 --- 直接修改差额不需要走审核流程
		 * update by ruyi.chen
		 * 所有公司暂都修改成不走审核流程
		 */
		if(plusys.length>0){
			hqxShenQingTiaoJiaIsland(param, orderId, productType, flowType, sbuffer,orderUuid,deptId);
			return ;
		}
		
		for(int i=0;i<plusys.length;i++){
			if(plusys[i].equals("0.00")){  // 改差价为默认的 ，就不启动工作流申请改价！
				continue;
			}
			// 流程属性数据
			List<Detail> listDetail = new ArrayList<Detail>(); 
			trid = travelerids[i];
			Detail id = new Detail(ChangePriceBean.TRVALER_ID,travelerids[i]);// 游客id
			Detail name = new Detail(ChangePriceBean.TRAVALER_NAME,"");	// 游客名称
			Detail cid = new Detail(ChangePriceBean.CURRENCY_ID,gaijiacurency[i]); // 游客币种id
			Detail cname = new Detail(ChangePriceBean.CURRENCY_NAME,String.valueOf(moneyTypes.get(String.valueOf(gaijiacurency[i])))); // 币种名称
			Detail oldm = new Detail(ChangePriceBean.OLD_TOTAL_MONEY,"0.00"); //原始应收价
			Detail curm = new Detail(ChangePriceBean.CUR_TOTAL_MONEY,"0.00"); //当前应收价
			Detail cfund = new Detail(ChangePriceBean.CHANGED_PRICE,plusys[i]); // 改价差额
			Detail remark = new Detail(ChangePriceBean.CHANGEDREMARK,travelerremark[i]); // 改价备注
			Detail chm = new Detail(ChangePriceBean.CHANGED_TOTAL_MONEY,"0.00"); // 改价后的应收价
			Detail chp = new Detail(ChangePriceBean.CHANGED_FUND,"应收价");  // 改价款项
			// 获取游客的 原始应收价 和 当前应收价 
			for(int j =0;j<travelerList.size();j++){
				Map raw = travelerList.get(j);
				
				if(id.getValue().equals("")){
					trid = travelerids[i-1];
					id.setValue(travelerids[i-1]);
					if(id.getValue().equals("")){
						trid = travelerids[i-2];
						id.setValue(travelerids[i-2]);
						if(id.getValue().equals("")){
							trid = travelerids[i-3];
							id.setValue(travelerids[i-3]);
							if(id.getValue().equals("")){
								trid = travelerids[i-4];
								id.setValue(travelerids[i-4]);
							}
						}
					}
				}
				
				
				
				if(id.getValue().equals(raw.get("travelerid").toString())){ //  同一个游客的信息
					name.setValue(String.valueOf(raw.get("travelername"))); // 赋值游客名称
//					if(id.getValue().equals("3055")){
//						System.out.println("游客名称:" +raw.get("travelername"));
//						System.out.println("游客币种:"+ raw.get("travelers") );
//						
//					}
				//	if(moneys.size() ==0){
					List<Map<String,Object>> 	moneys = (List<Map<String,Object>>) raw.get("travelers") ; // 游客币种
				//	}
					
					// 遍历游客的币种信息
					for(int mi = 0;mi<moneys.size();mi++){
						Map temp = moneys.get(mi); // 游客的币种信息
						if(gaijiacurency[i].equals(temp.get("currency_id").toString())){   // 币种确认
							oldm.setValue(String.valueOf(temp.get("oldtotalmoney"))); // 赋值游客的原始应收价
							curm.setValue(String.valueOf(temp.get("amount"))); // 赋值游客的当前应收价
							double f = Arith.add(Double.parseDouble(curm.getValue()),Double.parseDouble(plusys[i]));// 计算 改后应收价
							chm.setValue(String.valueOf(f)); // 赋值改后应收价
							break;
						}else{
							double f = Arith.add(Double.parseDouble("0.00"),Double.parseDouble(plusys[i]));// 计算 改后应收价
							chm.setValue(String.valueOf(f));  //赋值 改后应收价
							continue;
						}
						
					}
				}
				continue;
			}
			listDetail.add(id);
			listDetail.add(name);
			listDetail.add(cid);
			listDetail.add(cname);
			listDetail.add(oldm);
			listDetail.add(curm);
			listDetail.add(cfund);
			listDetail.add(remark);
			listDetail.add(chm);
			listDetail.add(chp);
			/*
			 *  调用工作流 申请【游客信息】改价
			 */
			reviewService.addReview(Integer.parseInt(productType), Integer.parseInt(flowType), 
					orderId, Long.parseLong(travelerids[i].equals("")?trid:travelerids[i]), 0l, travelerremark[i], sbuffer, 
					listDetail, deptId); 
			if(sbuffer.length()>0){
				break;
			}
			
		}
		
	}
	/**
	 * 定金的改价申请
	 * @param param 游客信息
	 * @param orderId  订单编号
	 * @param productType 产品类型
	 * @param flowType 流程类型
	 * @return 返回调用工作流的执行结果
	 */
	@SuppressWarnings({ "static-access", "rawtypes" })
    public void frontMoneyForApplyFlow(Map<String,Object> param,String orderId,String productType,String flowType,StringBuffer sbuffer){
		if(param.get("plusdj") == null || param.get("djremark") ==null){
			return ;
		}
		String [] plusdj = (String[]) param.get("plusdj"); // 订单定金差价
		String [] djremark = (String[]) param.get("djremark");  //订单定金改价备注
		int djs = plusdj.length;
		//  获取订单定金~
		List<Map<String,Object>> frontList = activityUpPricesService.queryFrontMoneyList(orderId);
		if(djs>0){ // 如果定价信息不为空，则定价申请改价 
			if(!loopCheckArrs(plusdj)){
				Detail id = new Detail(ChangePriceBean.TRVALER_ID,"-1");// 订单定金id
				Detail cid = new Detail(ChangePriceBean.CURRENCY_ID,"1"); // 币种id 
				Detail cname = new Detail(ChangePriceBean.CURRENCY_NAME,"人民币"); // 币种名称
				Detail oldm = new Detail(ChangePriceBean.OLD_TOTAL_MONEY,"0.00"); //原始应收价
				Detail curm = new Detail(ChangePriceBean.CUR_TOTAL_MONEY,"0.00"); //当前应收价
				Detail cfund = new Detail(ChangePriceBean.CHANGED_PRICE,plusdj[0]); // 改价差额
				Detail remark = new Detail(ChangePriceBean.CHANGEDREMARK,djremark[0]); // 改价备注
				Detail chm = new Detail(ChangePriceBean.CHANGED_TOTAL_MONEY,"0.00");
				Detail chp = new Detail(ChangePriceBean.CHANGED_FUND,"订金");
				List<Detail> listDetail = new ArrayList<Detail>();
		for(int i =0;i<frontList.size();i++){
			Map raw = frontList.get(i);
			cid.setValue(String.valueOf(raw.get("currency_id")));
			cname.setValue(String.valueOf(raw.get("currency_name")));
			if(null != raw.get("moneyType") && raw.get("moneyType").toString().equals(this.DEF_MT_YSDJ)){ 		// 原始定金结算价
				oldm.setValue(String.valueOf(raw.get("amount"))); 
			}else if(null != raw.get("moneyType") && raw.get("moneyType").toString().equals(this.DEF_MT_JSJ)){	// 当前定金结算价
				curm.setValue(String.valueOf(raw.get("amount"))); 
			}
			double f = Arith.add(Double.parseDouble(curm.getValue()),Double.parseDouble(plusdj[0]));// 计算 改后应收价
			chm.setValue(String.valueOf(f));  //赋值 改后应收价
		}
		listDetail.add(id);
		listDetail.add(cid);
		listDetail.add(cname);
		listDetail.add(oldm);
		listDetail.add(curm);
		listDetail.add(cfund);
		listDetail.add(remark);
		listDetail.add(chm);
		listDetail.add(chp);
		/*
		 *  调用工作流 申请【订单定金信息】改价
		 */
		boolean flagSign = activityUpPricesService.verifyfrontMoneyFlowsign(orderId, productType, flowType, cid.getValue());
		if(flagSign){
			long l = reviewService.addReview(Integer.parseInt(productType), Integer.parseInt(flowType), 
					orderId, -1l, 0l, djremark[0], sbuffer, listDetail, orderService.getProductPept(orderId)); 
			sbuffer.append(" 申请改价是否成功:"+l);
		}
		
		}
		}
	}
	/**
	 * 对整个订单的改价（团队改价）
	 * @param param 游客信息
	 * @param orderId  订单编号
	 * @param productType 产品类型
	 * @param flowType 流程类型
	 * @return 返回调用工作流的执行结果
	 */
	@SuppressWarnings({ "rawtypes", "unused" })
    public void teamOrderForApplyFlow(Map<String,Object> param,String orderId,String productType,String flowType,StringBuffer sbuffer){
		
		if(param.get("teammoney") == null || param.get("teamkx") ==null || param.get("teamcurrency") ==null || param.get("teamremark") ==null){
			return ;
		}
		String [] teammoney = (String[]) param.get("teammoney") ; // 订单团队差价
		String [] teamkx = (String[]) param.get("teamkx"); // 订单团队款项
		String [] teamcurrency = (String[]) param.get("teamcurrency"); // 订单团队币种
		String [] teamremark = (String[]) param.get("teamremark"); // 订单团队备注
		
	
		List<Map<String,Object>> orderReceivable = activityUpPricesService.queryOrderReceivable(orderId);
		int les = orderReceivable.size();
		int less = teamcurrency.length;
		for(int i =0;i<less;i++){
			if(teammoney[i].equals("") || teamkx[i].equals("")){
				continue;
			}
			if(teammoney[i].equals("0.00")){ // 如果是默认值,不启动工作流申请改价
				continue;
			}
			
			 List<Detail> listDetail = new ArrayList<Detail>();
			 Detail id = new Detail(ChangePriceBean.TRVALER_ID,"0");// 订单团队id
			 Detail cid = new Detail(ChangePriceBean.CURRENCY_ID,String.valueOf(teamcurrency[i])); // 币种id
			 Detail cname = new Detail(ChangePriceBean.CURRENCY_NAME,String.valueOf(moneyTypes.get(String.valueOf(teamcurrency[i])))); // 币种名称 
			 Detail curm = new Detail(ChangePriceBean.CUR_TOTAL_MONEY,"0.00"); //当前应收价
			 Detail cfund = new Detail(ChangePriceBean.CHANGED_PRICE,teammoney[i]); // 改价差额
			 Detail remark = new Detail(ChangePriceBean.CHANGEDREMARK,teamremark[i]); // 改价备注
			 Detail chm = new Detail(ChangePriceBean.CHANGED_TOTAL_MONEY,"0.00");  // 改后应收
			 Detail chp = new Detail(ChangePriceBean.CHANGED_FUND,teamkx[i]);  // 改价款项
			for(int j =0;j<les;j++){
				Map raw = orderReceivable.get(j);
				if(teamcurrency[i].equals(String.valueOf(raw.get("currency_id")))){  // 币种一样
					curm.setValue(String.valueOf(raw.get("amount"))); 
					double f = Arith.add(Double.parseDouble(curm.getValue()),Double.parseDouble(teammoney[i]));// 计算 改后应收价
					chm.setValue(String.valueOf(f));  //赋值 改后应收价
				}else{
					double f = Arith.add(Double.parseDouble("0.00"),Double.parseDouble(teammoney[i]));// 计算 改后应收价
					chm.setValue(String.valueOf(f));  //赋值 改后应收价
				}
				 break;
			}
			listDetail.add(id);
			listDetail.add(cid);
			listDetail.add(cname);
			listDetail.add(curm);
			listDetail.add(cfund);
			listDetail.add(remark);
			listDetail.add(chm);
			listDetail.add(chp);
			/*
			 *  调用工作流 申请【订单团队信息】改价
			 */
			long l = reviewService.addReviewNoLimit(Integer.parseInt(productType), Integer.parseInt(flowType), 
					orderId, 0l, 0l, teamremark[i], sbuffer, listDetail, orderService.getProductPept(orderId));
			sbuffer.append(" 申请改价是否成功:"+l);
		}
	}
	/**
	 * 检查数组(验证改差价数组是否为空或者默认值)
	 * @param arrs
	 * @return
	 */
	boolean loopCheckArrs(String [] arrs){
		if(arrs == null ) return true;
		int count = 0;
		int len = arrs.length;
		for(int i=0;i<len;i++){
			if(arrs[i].equals("0.00")){
				count ++;
			}
		}
		if(count == len){
			return true;
		}
		return false;
	}
	
	
	
	@RequestMapping("checkMutex")
	@ResponseBody
	public Object checkMutex(Model model,HttpServletResponse response,HttpServletRequest request){
		Map<String,Object> rMap = new HashMap<String, Object>();
		/**
		 * 游客
		 */
		Map<Long,String> travelers = new HashMap<Long, String>();
		/**
		 *  订单编号
		 */
		String orderId = request.getParameter("orderId") ; 
		/**
		 * 订单类型
		 */
		String productType = request.getParameter("productType");
		/**
		 * 流程类型
		 */
		String flowType = request.getParameter("flowType");  
		/**
		 * 获取游客列表，组装游客id和name字段
		 */
		List<Map<String,Object>> travelerList = null;
		try{
			travelerList = activityUpPricesService.queryTravelerUpPrices(orderId);
			Iterator<Map<String,Object>> iter = travelerList.iterator();
			while(iter.hasNext()){
				Map<String,Object> row = iter.next();
				travelers.put(Long.parseLong(row.get("id").toString()), row.get("name").toString());
			}
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		/**
		 * 验证流程是否互斥 
		 */
		 rMap = orderReviewService.getTravelerReviewMutexInfo(Long.parseLong(orderId), productType, Integer.parseInt(flowType), travelers);
		return rMap;
	}
	
	/**
	 * 
	 * calculatePrices 计算订单改价差额
	 * @param pricess
	 * @return
	 * @exception
	 * @since  1.0.0
	 */
	public Double calculatePrices(String [] pricess){
		double d = 0.0;
		for(int i =0;i<pricess.length;i++){
			double temp = Double.parseDouble(pricess[i]);
			d =  d + temp;
		}
		return d;
	}
	
	/**
	 * 计算人民币改价差额，针对需求 C369
	 * add by yunpeng.zhang
	 * @param pricess
	 * @param gaijiaCurrencies
	 * @return
	 */
	public Map<String, Double> calculatePrices(String[] pricess, String[] gaijiaCurrencies) {
		Map<String, Double> map = new HashMap<>();
		Double rmbChae = 0.0;
		Double wbChae = 0.0;
		Integer times = 0;
		for(int i =0;i<pricess.length;i++){
			double temp = Double.parseDouble(pricess[i]);
			if(temp != 0.0) {
				String currencyId = gaijiaCurrencies[i];
				Currency currency = currencyService.findCurrency(Long.parseLong(currencyId));
				String currencyName = currency.getCurrencyName();
				if("人民币".equals(currencyName)) {
					times++;
					rmbChae += (temp + 80);
					wbChae += (temp + 80);
				} else {
					wbChae += temp;
				}
			}
		}
		map.put("wbChae", wbChae);
		map.put("times", times.doubleValue());
		map.put("rmbChae", rmbChae);
		return map;
	}
	
	
	/**
	 * 
	 * hqxShenQingTiaoJia 环球行申请改价 向上调价
	 * @return
	 * @exception
	 * @since  1.0.0
	 */
	@SuppressWarnings("unchecked")
	public void hqxShenQingTiaoJia(Map<String,Object> param,String orderId,String productType,String flowType,StringBuffer sbuffer){
//		String [] plusys  = (String[]) param.get("plusys"); 		 // 游客差价
		String [] plusys  = (String[]) param.get("plusystrue"); 		 // 游客差价
		String [] travelerids = (String[]) param.get("travelerids"); // 游客编号
		String [] gaijiacurency =(String[]) param.get("gaijiacurency"); // 游客币种
		String [] travelerremark = (String[]) param.get("travelerremark"); // 游客备注
		
		if(loopCheckArrs(plusys)){ 
			sbuffer.append(" 游客差价列表为空！ ");
			return ;
		}
		// 获取游客列表信息~
		List<Map<String,Object>> travelerList = activityUpPricesService.queryTravelerList(orderId);
		String trid = "";
		for(int i=0;i<plusys.length;i++){
			if(plusys[i].equals("0.00")){  // 改差价为默认的 ，就不启动工作流申请改价！
				continue;
			}
			
			// 流程属性数据
			List<Detail> listDetail = new ArrayList<Detail>(); 
			trid = travelerids[i];
			Detail id = new Detail(ChangePriceBean.TRVALER_ID,travelerids[i]);// 游客id
			Detail name = new Detail(ChangePriceBean.TRAVALER_NAME,"");	// 游客名称
			Detail cid = new Detail(ChangePriceBean.CURRENCY_ID,gaijiacurency[i]); // 游客币种id
			Detail cname = new Detail(ChangePriceBean.CURRENCY_NAME,String.valueOf(moneyTypes.get(String.valueOf(gaijiacurency[i])))); // 币种名称
			Detail oldm = new Detail(ChangePriceBean.OLD_TOTAL_MONEY,"0.00"); //原始应收价
			Detail curm = new Detail(ChangePriceBean.CUR_TOTAL_MONEY,"0.00"); //当前应收价
			Detail cfund = new Detail(ChangePriceBean.CHANGED_PRICE,plusys[i]); // 改价差额
			Detail remark = new Detail(ChangePriceBean.CHANGEDREMARK,travelerremark[i]); // 改价备注
			Detail chm = new Detail(ChangePriceBean.CHANGED_TOTAL_MONEY,"0.00"); // 改价后的应收价
			Detail chp = new Detail(ChangePriceBean.CHANGED_FUND,"应收价");  // 改价款项
			// 获取游客的 原始应收价 和 当前应收价 
			for(int j =0;j<travelerList.size();j++){
				Map<String,Object> raw = travelerList.get(j);
				
				if(id.getValue().equals("")){
					trid = travelerids[i-1];
					id.setValue(travelerids[i-1]);
					if(id.getValue().equals("")){
						trid = travelerids[i-2];
						id.setValue(travelerids[i-2]);
						if(id.getValue().equals("")){
							trid = travelerids[i-3];
							id.setValue(travelerids[i-3]);
							if(id.getValue().equals("")){
								trid = travelerids[i-4];
								id.setValue(travelerids[i-4]);
							}
						}
					}
				}
				
				
				
				if(id.getValue().equals(raw.get("travelerid").toString())){ //  同一个游客的信息
					name.setValue(String.valueOf(raw.get("travelername"))); // 赋值游客名称
//					if(id.getValue().equals("3055")){
//						System.out.println("游客名称:" +raw.get("travelername"));
//						System.out.println("游客币种:"+ raw.get("travelers") );
//						
//					}
				//	if(moneys.size() ==0){
					List<Map<String,Object>> 	moneys = (List<Map<String,Object>>) raw.get("travelers") ; // 游客币种
				//	}
					
					// 遍历游客的币种信息
					for(int mi = 0;mi<moneys.size();mi++){
						Map<String,Object> temp = moneys.get(mi); // 游客的币种信息
						if(gaijiacurency[i].equals(temp.get("currency_id").toString())){   // 币种确认
							oldm.setValue(String.valueOf(temp.get("oldtotalmoney"))); // 赋值游客的原始应收价
							curm.setValue(String.valueOf(temp.get("amount"))); // 赋值游客的当前应收价
							double f = Arith.add(Double.parseDouble(curm.getValue()),Double.parseDouble(plusys[i]));// 计算 改后应收价
							chm.setValue(String.valueOf(f)); // 赋值改后应收价
							break;
						}else{
							double f = Arith.add(Double.parseDouble("0.00"),Double.parseDouble(plusys[i]));// 计算 改后应收价
							chm.setValue(String.valueOf(f));  //赋值 改后应收价
							continue;
						}
						
					}
				}
				continue;
			}
			listDetail.add(id);
			listDetail.add(name);
			listDetail.add(cid);
			listDetail.add(cname);
			listDetail.add(oldm);
			listDetail.add(curm);
			listDetail.add(cfund);
			listDetail.add(remark);
			listDetail.add(chm);
			listDetail.add(chp);
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("travelerId", travelerids[i]);
			params.put("orderId", orderId);
			params.put("orderType", productType);
			params.put("currencyId", gaijiacurency[i]);
			params.put("amount", plusys[i]);
			boolean flag = changePriceReviewService.doChangePrice(params);
			/**
			 * 此次改价记录到流程列表中
			 */
			reviewService.addSuccessReview(Integer.parseInt(productType), Integer.parseInt(flowType), orderId,  Long.parseLong(travelerids[i].equals("")?trid:travelerids[i])
					,(long) 0, travelerremark[i], sbuffer, listDetail, orderService.getProductPept(orderId));
			  
			logger.info("--- > [申请改价日志] 此改价不需要走审核流程，直接改价, 是否成功:"+flag +" 改价信息:"+sbuffer.toString() +", 改价参数:"+params);
		}
	}
	@SuppressWarnings("unchecked")
	public void hqxShenQingTiaoJiaHotel(Map<String,Object> param, String orderId, String productType, 
									String flowType, StringBuffer sbuffer, String orderUuid, Long deptId){
		String [] plusys  = (String[]) param.get("plusystrue"); 	// 游客差价
		String [] travelerids = (String[]) param.get("travelerids"); // 游客编号
		String [] gaijiacurency =(String[]) param.get("gaijiacurency"); // 游客币种
		String [] travelerremark = (String[]) param.get("travelerremark"); // 游客备注
		
		if(loopCheckArrs(plusys)){ 
			sbuffer.append(" 游客差价列表为空！ ");
			return ;
		}
		// 获取游客列表信息~
		List<Map<String,Object>> travelerList = activityUpPricesService.queryHotelTravelerList(orderUuid);
		String trid = "";
		for(int i=0;i<plusys.length;i++){
			if(Double.parseDouble(plusys[i]) == 0){  // 改差价为默认的 ，就不启动工作流申请改价！
				continue;
			}
			
			// 流程属性数据
			List<Detail> listDetail = new ArrayList<Detail>(); 
			trid = travelerids[i];
			Detail id = new Detail(ChangePriceBean.TRVALER_ID,travelerids[i]);// 游客id
			Detail name = new Detail(ChangePriceBean.TRAVALER_NAME,"");	// 游客名称
			Detail cid = new Detail(ChangePriceBean.CURRENCY_ID,gaijiacurency[i]); // 游客币种id
			Detail cname = new Detail(ChangePriceBean.CURRENCY_NAME,String.valueOf(moneyTypes.get(String.valueOf(gaijiacurency[i])))); // 币种名称
			Detail oldm = new Detail(ChangePriceBean.OLD_TOTAL_MONEY,"0.00"); //原始应收价
			Detail curm = new Detail(ChangePriceBean.CUR_TOTAL_MONEY,"0.00"); //当前应收价
			Detail cfund = new Detail(ChangePriceBean.CHANGED_PRICE,plusys[i]); // 改价差额
			Detail remark = new Detail(ChangePriceBean.CHANGEDREMARK,travelerremark[i]); // 改价备注
			Detail chm = new Detail(ChangePriceBean.CHANGED_TOTAL_MONEY,"0.00"); // 改价后的应收价
			Detail chp = new Detail(ChangePriceBean.CHANGED_FUND,"应收价");  // 改价款项
			// 获取游客的 原始应收价 和 当前应收价 
			for(int j =0;j<travelerList.size();j++){
				Map<String,Object> raw = travelerList.get(j);
				
				if(id.getValue().equals("")){
					trid = travelerids[i-1];
					id.setValue(travelerids[i-1]);
					if(id.getValue().equals("")){
						trid = travelerids[i-2];
						id.setValue(travelerids[i-2]);
						if(id.getValue().equals("")){
							trid = travelerids[i-3];
							id.setValue(travelerids[i-3]);
							if(id.getValue().equals("")){
								trid = travelerids[i-4];
								id.setValue(travelerids[i-4]);
							}
						}
					}
				}
				
				
				
				if(id.getValue().equals(raw.get("travelerid").toString())){ //  同一个游客的信息
					name.setValue(String.valueOf(raw.get("travelername"))); // 赋值游客名称
//					if(id.getValue().equals("3055")){
//						System.out.println("游客名称:" +raw.get("travelername"));
//						System.out.println("游客币种:"+ raw.get("travelers") );
//						
//					}
				//	if(moneys.size() ==0){
					List<Map<String,Object>> 	moneys = (List<Map<String,Object>>) raw.get("travelers") ; // 游客币种
				//	}
					
					// 遍历游客的币种信息
					for(int mi = 0;mi<moneys.size();mi++){
						Map<String,Object> temp = moneys.get(mi); // 游客的币种信息
						if(gaijiacurency[i].equals(temp.get("currency_id").toString())){   // 币种确认
							oldm.setValue(String.valueOf(temp.get("oldtotalmoney"))); // 赋值游客的原始应收价
							curm.setValue(String.valueOf(temp.get("amount"))); // 赋值游客的当前应收价
							double f = Arith.add(Double.parseDouble(curm.getValue()),Double.parseDouble(plusys[i]));// 计算 改后应收价
							chm.setValue(String.valueOf(f)); // 赋值改后应收价
							break;
						}else{
							double f = Arith.add(Double.parseDouble("0.00"),Double.parseDouble(plusys[i]));// 计算 改后应收价
							chm.setValue(String.valueOf(f));  //赋值 改后应收价
							continue;
						}
						
					}
				}
				continue;
			}
			listDetail.add(id);
			listDetail.add(name);
			listDetail.add(cid);
			listDetail.add(cname);
			listDetail.add(oldm);
			listDetail.add(curm);
			listDetail.add(cfund);
			listDetail.add(remark);
			listDetail.add(chm);
			listDetail.add(chp);
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("travelerId", travelerids[i]);
			params.put("orderId", orderId);
			params.put("orderUuid", orderUuid);
			params.put("orderType", productType);
			params.put("currencyId", gaijiacurency[i]);
			params.put("amount", plusys[i]);
			/**
			 * 此次改价记录到流程列表中
			 */
			long rid = reviewService.addSuccessReview(Integer.parseInt(productType), Integer.parseInt(flowType), orderId,  Long.parseLong(travelerids[i].equals("")?trid:travelerids[i])
					,(long) 0, travelerremark[i], sbuffer, listDetail, deptId);
			if (rid > 0) {
				boolean flag = changePriceReviewService.doHotelChangePrice(params);
				logger.info("--- > [申请改价日志] 此改价不需要走审核流程，直接改价, 是否成功:"+flag +" 改价信息:"+sbuffer.toString() +", 改价参数:"+params);
			}
		}
	}
	@SuppressWarnings("unchecked")
	public void hqxShenQingTiaoJiaIsland(Map<String,Object> param,String orderId,String productType,String flowType,StringBuffer sbuffer,String orderUuid,Long deptId){
		String [] plusys  = (String[]) param.get("plusystrue"); 		 // 游客差价
		String [] travelerids = (String[]) param.get("travelerids"); // 游客编号
		String [] gaijiacurency =(String[]) param.get("gaijiacurency"); // 游客币种
		String [] travelerremark = (String[]) param.get("travelerremark"); // 游客备注
		
		if(loopCheckArrs(plusys)){ 
			sbuffer.append(" 游客差价列表为空！ ");
			return ;
		}
		// 获取游客列表信息~
		List<Map<String,Object>> travelerList = activityUpPricesService.queryIslandTravelerList(orderUuid);
		String trid = "";
		for(int i=0;i<plusys.length;i++){
			if(plusys[i].equals("0.00")){  // 改差价为默认的 ，就不启动工作流申请改价！
				continue;
			}
			
			// 流程属性数据
			List<Detail> listDetail = new ArrayList<Detail>(); 
			trid = travelerids[i];
			Detail id = new Detail(ChangePriceBean.TRVALER_ID,travelerids[i]);// 游客id
			Detail name = new Detail(ChangePriceBean.TRAVALER_NAME,"");	// 游客名称
			Detail cid = new Detail(ChangePriceBean.CURRENCY_ID,gaijiacurency[i]); // 游客币种id
			Detail cname = new Detail(ChangePriceBean.CURRENCY_NAME,String.valueOf(moneyTypes.get(String.valueOf(gaijiacurency[i])))); // 币种名称
			Detail oldm = new Detail(ChangePriceBean.OLD_TOTAL_MONEY,"0.00"); //原始应收价
			Detail curm = new Detail(ChangePriceBean.CUR_TOTAL_MONEY,"0.00"); //当前应收价
			Detail cfund = new Detail(ChangePriceBean.CHANGED_PRICE,plusys[i]); // 改价差额
			Detail remark = new Detail(ChangePriceBean.CHANGEDREMARK,travelerremark[i]); // 改价备注
			Detail chm = new Detail(ChangePriceBean.CHANGED_TOTAL_MONEY,"0.00"); // 改价后的应收价
			Detail chp = new Detail(ChangePriceBean.CHANGED_FUND,"应收价");  // 改价款项
			// 获取游客的 原始应收价 和 当前应收价 
			for(int j =0;j<travelerList.size();j++){
				Map<String,Object> raw = travelerList.get(j);
				
				if(id.getValue().equals("")){
					trid = travelerids[i-1];
					id.setValue(travelerids[i-1]);
					if(id.getValue().equals("")){
						trid = travelerids[i-2];
						id.setValue(travelerids[i-2]);
						if(id.getValue().equals("")){
							trid = travelerids[i-3];
							id.setValue(travelerids[i-3]);
							if(id.getValue().equals("")){
								trid = travelerids[i-4];
								id.setValue(travelerids[i-4]);
							}
						}
					}
				}
				
				
				
				if(id.getValue().equals(raw.get("travelerid").toString())){ //  同一个游客的信息
					name.setValue(String.valueOf(raw.get("travelername"))); // 赋值游客名称
//					if(id.getValue().equals("3055")){
//						System.out.println("游客名称:" +raw.get("travelername"));
//						System.out.println("游客币种:"+ raw.get("travelers") );
//						
//					}
				//	if(moneys.size() ==0){
					List<Map<String,Object>> 	moneys = (List<Map<String,Object>>) raw.get("travelers") ; // 游客币种
				//	}
					
					// 遍历游客的币种信息
					for(int mi = 0;mi<moneys.size();mi++){
						Map<String,Object> temp = moneys.get(mi); // 游客的币种信息
						if(gaijiacurency[i].equals(temp.get("currency_id").toString())){   // 币种确认
							oldm.setValue(String.valueOf(temp.get("oldtotalmoney"))); // 赋值游客的原始应收价
							curm.setValue(String.valueOf(temp.get("amount"))); // 赋值游客的当前应收价
							double f = Arith.add(Double.parseDouble(curm.getValue()),Double.parseDouble(plusys[i]));// 计算 改后应收价
							chm.setValue(String.valueOf(f)); // 赋值改后应收价
							break;
						}else{
							double f = Arith.add(Double.parseDouble("0.00"),Double.parseDouble(plusys[i]));// 计算 改后应收价
							chm.setValue(String.valueOf(f));  //赋值 改后应收价
							continue;
						}
						
					}
				}
				continue;
			}
			listDetail.add(id);
			listDetail.add(name);
			listDetail.add(cid);
			listDetail.add(cname);
			listDetail.add(oldm);
			listDetail.add(curm);
			listDetail.add(cfund);
			listDetail.add(remark);
			listDetail.add(chm);
			listDetail.add(chp);
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("travelerId", travelerids[i]);
			params.put("orderId", orderId);
			params.put("orderUuid", orderUuid);
			params.put("orderType", productType);
			params.put("currencyId", gaijiacurency[i]);
			params.put("amount", plusys[i]);
			
			/**
			 * 此次改价记录到流程列表中
			 */
			long rid = reviewService.addSuccessReview(Integer.parseInt(productType), Integer.parseInt(flowType), orderId,  Long.parseLong(travelerids[i].equals("")?trid:travelerids[i])
					,(long) 0, travelerremark[i], sbuffer, listDetail, deptId);
			if (rid > 0) {
				boolean flag = changePriceReviewService.doIslandChangePrice(params);
				logger.info("--- > [申请改价日志] 此改价不需要走审核流程，直接改价, 是否成功:"+flag +" 改价信息:"+sbuffer.toString() +", 改价参数:"+params);
			}
			  
		}
	}
	/**
	 * 
	 * looseUppricess 游轮订单改价
	 * (注意事项：游轮的改价业务比较特殊)
	 * @param param 
	 * @param orderId
	 * @param productType
	 * @param flowType
	 * @param sbuffer
	 * @exception
	 * @since  1.0.0
	 */
	@SuppressWarnings("unchecked")
	public void  looseUppricess(Map<String,Object> param,String orderId,String productType,String flowType,StringBuffer sbuffer){
        String [] plusys  = (String[]) param.get("plusys");  // 游客差价
        String [] travelerids = (String[]) param.get("travelerids"); // 游客编号
        String [] gaijiacurency =(String[]) param.get("gaijiacurency"); // 游客币种
        String [] travelerremark = (String[]) param.get("travelerremark"); // 游客备注
        
        if(loopCheckArrs(plusys)){ 
            sbuffer.append(" 游客差价列表为空！ ");
            return;
        }
        // 获取游客列表信息~
        List<Map<String,Object>> travelerList = activityUpPricesService.queryTravelerList(orderId);
        String trid = "";

       
        
        for(int i=0;i<plusys.length;i++){
            if(plusys[i].equals("0.00")){  // 改差价为默认的 ，就不启动工作流申请改价！
                continue;
            }
            // 流程属性数据
            List<Detail> listDetail = new ArrayList<Detail>(); 
            trid = travelerids[i];
            Detail id = new Detail(ChangePriceBean.TRVALER_ID,travelerids[i]);// 游客id
            Detail name = new Detail(ChangePriceBean.TRAVALER_NAME,""); // 游客名称
            Detail cid = new Detail(ChangePriceBean.CURRENCY_ID,gaijiacurency[i]); // 游客币种id
            Detail cname = new Detail(ChangePriceBean.CURRENCY_NAME,String.valueOf(moneyTypes.get(String.valueOf(gaijiacurency[i])))); // 币种名称
            Detail oldm = new Detail(ChangePriceBean.OLD_TOTAL_MONEY,"0.00"); //原始应收价
            Detail curm = new Detail(ChangePriceBean.CUR_TOTAL_MONEY,"0.00"); //当前应收价
            Detail cfund = new Detail(ChangePriceBean.CHANGED_PRICE,plusys[i]); // 改价差额
            Detail remark = new Detail(ChangePriceBean.CHANGEDREMARK,travelerremark[i]); // 改价备注
            Detail chm = new Detail(ChangePriceBean.CHANGED_TOTAL_MONEY,"0.00"); // 改价后的应收价
            Detail chp = new Detail(ChangePriceBean.CHANGED_FUND,"应收价");  // 改价款项
            // 获取游客的 原始应收价 和 当前应收价 
            for(int j =0;j<travelerList.size();j++){
                Map<String,Object> raw = travelerList.get(j);
                
                if(id.getValue().equals("")){
                    trid = travelerids[i-1];
                    id.setValue(travelerids[i-1]);
                    if(id.getValue().equals("")){
                        trid = travelerids[i-2];
                        id.setValue(travelerids[i-2]);
                        if(id.getValue().equals("")){
                            trid = travelerids[i-3];
                            id.setValue(travelerids[i-3]);
                            if(id.getValue().equals("")){
                                trid = travelerids[i-4];
                                id.setValue(travelerids[i-4]);
                            }
                        }
                    }
                }
                
                
                
                if(id.getValue().equals(raw.get("travelerid").toString())){ //  同一个游客的信息
                    name.setValue(String.valueOf(raw.get("travelername"))); // 赋值游客名称
//                  if(id.getValue().equals("3055")){
//                      System.out.println("游客名称:" +raw.get("travelername"));
//                      System.out.println("游客币种:"+ raw.get("travelers") );
//                      
//                  }
                //  if(moneys.size() ==0){
                    List<Map<String,Object>>    moneys = (List<Map<String,Object>>) raw.get("travelers") ; // 游客币种
                //  }
                    
                    // 遍历游客的币种信息
                    for(int mi = 0;mi<moneys.size();mi++){
                        Map<String,Object> temp = moneys.get(mi); // 游客的币种信息
                        if(gaijiacurency[i].equals(temp.get("currency_id").toString())){   // 币种确认
                            oldm.setValue(String.valueOf(temp.get("oldtotalmoney"))); // 赋值游客的原始应收价
                            curm.setValue(String.valueOf(temp.get("amount"))); // 赋值游客的当前应收价
                            double f = Arith.add(Double.parseDouble(curm.getValue()),Double.parseDouble(plusys[i]));// 计算 改后应收价
                            chm.setValue(String.valueOf(f)); // 赋值改后应收价
                            break;
                        }else{
                            double f = Arith.add(Double.parseDouble("0.00"),Double.parseDouble(plusys[i]));// 计算 改后应收价
                            chm.setValue(String.valueOf(f));  //赋值 改后应收价
                            continue;
                        }
                        
                    }
                }
                continue;
            }
            listDetail.add(id);
            listDetail.add(name);
            listDetail.add(cid);
            listDetail.add(cname);
            listDetail.add(oldm);
            listDetail.add(curm);
            listDetail.add(cfund);
            listDetail.add(remark);
            listDetail.add(chm);
            listDetail.add(chp);
            
            Map<String, String> params = new HashMap<String, String>();
            params.put("travelerId", travelerids[i]);
            params.put("orderId", orderId);
            params.put("orderType", productType);
            params.put("currencyId", gaijiacurency[i]);
            params.put("amount", plusys[i]);
            boolean flag = changePriceReviewService.doChangePrice(params);
            /**
             * 此次改价记录到流程列表中
             */
            reviewService.addSuccessReview(Integer.parseInt(productType), Integer.parseInt(flowType), orderId,  Long.parseLong(travelerids[i].equals("")?trid:travelerids[i])
                    ,(long) 0, travelerremark[i], sbuffer, listDetail, orderService.getProductPept(orderId));
              
            logger.info("--- > [申请改价日志] 此改价不需要走审核流程，直接改价, 是否成功:"+flag +" 改价信息:"+sbuffer.toString() +", 改价参数:"+params);
        }
        
    }
}
