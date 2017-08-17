package com.trekiz.admin.modules.airticket.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.Arith;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.airticket.service.AirTicketUpPricesService;
import com.trekiz.admin.modules.airticket.service.IActivityAirTicketService;
import com.trekiz.admin.modules.airticket.service.UPPricesGenerateHTML;
import com.trekiz.admin.modules.airticketorder.service.IAirTicketOrderService;
import com.trekiz.admin.modules.order.service.OrderReviewService;
import com.trekiz.admin.modules.review.changeprice.entity.ChangePriceBean;
import com.trekiz.admin.modules.review.changeprice.service.IChangePriceReviewService;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * AirTicketUpPricesController.java 功能:飞机票改价 控制器
 * @author HPT
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/airTicketUpProces/")
public class AirTicketUpPricesController extends BaseController{
	
    private static Logger logger = LoggerFactory.getLogger(AirTicketUpPricesController.class);
	private static String DEF_MT_YSDJ = "20",DEF_MT_JSJ = "13" ;   // 原始定金结算价 ，当前结算价 的 moneyType
	@Autowired
	private AirTicketUpPricesService airTicketUpPricesService;
	
	@Autowired
	private ReviewService reviewService ;
	
	@Autowired
	private IAirTicketOrderService airTicketOrderService;
	@Autowired
	private CurrencyService currencyService ;
	
	@Autowired
	private IActivityAirTicketService activityAirTicketService;
	/**
	 * 订单流程服务者
	 */
	@Autowired
	private OrderReviewService orderReviewService ;
	
	@Autowired
	private IChangePriceReviewService changePriceReviewService;
	
	@SuppressWarnings("rawtypes")
    private static Map moneyTypes = new HashMap<String, String>(); // 定义币种信息
	
	@Autowired
	private AreaService areaService;
	
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
		boolean flag = reviewService.verifyWorkFlowStatus(orderId, Integer.parseInt(productType), Integer.parseInt(flowType));
		// 调用公用工作流查询方法,取到流程表里的机票改价记录信息
		Long airTicketId = airTicketOrderService.getAirticketorderById(StringUtils.toLong(orderId)).getAirticketId();
		Long deptId = activityAirTicketService.getActivityAirTicketById(airTicketId).getDeptId();
		List<Map<String, Object>> list = reviewService.findReviewListMap(Integer.parseInt(productType), Integer.parseInt(flowType),orderId, false,"",deptId);
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("orderId", orderId);
		map.put("productType",productType);
		map.put("flowType", flowType);
		model.addAttribute("airticketReturnList", list);
		model.addAllAttributes(map);
		model.addAttribute("flag", flag);
		 
		return "modules/airticket/airticketUpPrices";
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
		
		
//		AirticketOrder airticketOrder = airTicketOrderService.getAirticketorderById(Long.parseLong(orderId));
//		Map<String, Object> orderDetailInfoMap = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId);
//		model.addAttribute("orderDetailInfoMap", orderDetailInfoMap);
		
		
		// 获取游客列表信息~
		List<Map<String,Object>> travelerList = airTicketUpPricesService.queryTravelerList(orderId);
		model.addAttribute("travelerList", travelerList);
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		model.addAttribute("curlist", currencylist);
		Map<String,String> map = new HashMap<String, String>();
		map.put("orderId", orderId);
		map.put("productType",productType);
		map.put("flowType", flowType);
		model.addAllAttributes(map);
		model.addAttribute("orderDetailInfoMap", airTicketOrderService.queryAirticketOrderDetailInfoById(orderId));
		model.addAttribute("from_Areas",areaService.findFromCityList(""));//出发城市
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
		
		return "modules/airticket/upAirticketDetails";
	}
	
	/**
	 * 查询单个改价信息(改价详情页面)
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
		
		//=========订单详情开始
		Map<String, Object> orderDetailInfoMap = airTicketOrderService.queryAirticketOrderDetailInfoById(orderId);
		model.addAttribute("orderDetailInfoMap", orderDetailInfoMap);
		model.addAttribute("from_Areas",areaService.findFromCityList(""));//出发城市
		model.addAttribute("arrivedareas", areaService.findAirportCityList(""));// 到达城市
		//=========订单详情结束
		
		model.addAttribute("rid", id);
		
		return "modules/airticket/airticketChangePrices";
	}
	
	/**
	 * 申请改价
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked"})
	@RequestMapping(value="applyForUpAirPrices")
	@ResponseBody
	public Object applyForUpAirTicketPrices(Model model,HttpServletResponse response,HttpServletRequest request){
		Map param = getRequestParamater(request);
		
		String orderId = request.getParameter("orderId") ; // 订单编号
		String productType = request.getParameter("productType"); //产品类型
		String flowType = request.getParameter("flowType");  // 流程类型
		
		if(null != param.get("sign")){  // 重新申请改价流程
			String revid = request.getParameter("revid");
			reviewService.removeReview(Long.parseLong(revid));
			return gotoUpAirPrices(model,response,request);
		}
	
		 moneyTypes = UPPricesGenerateHTML.getCurrencyParam(currencyService);
		
		StringBuffer sbuffer = new StringBuffer();
	
		/* 1.游客改价申请**/
		travelerForApplyFlow(param, orderId, productType, flowType, sbuffer);
//		/* 2.订单定价改价申请**/
//		frontMoneyForApplyFlow(param,orderId, productType, flowType, sbuffer);
//		/* 3.订单团队改价申请**/
//		teamOrderForApplyFlow(param,orderId, productType, flowType, sbuffer);
		

		super.saveUserOpeLog(Context.log_type_orderform, Context.log_type_orderform, "机票申请改价-  回调流程接口" +
				"返回信息: " + sbuffer.toString(), Context.log_state_up, Context.ProductType.PRODUCT_AIR_TICKET, Long.parseLong(orderId));
		
		Map<String,String> rMap = new HashMap<String, String>();
		rMap.put("sbinfo", sbuffer.toString());
		rMap.put("orderId",orderId);
		rMap.put("productType",productType);
		rMap.put("flowType",flowType);
		
		return rMap;
		// return "redirect:" + Global.getAdminPath() + "/airTicketUpProces/list?orderId="+orderId+"&productType="+productType+"&flowType="+flowType+"&sign="+param.get("sign")+"&revid="+param.get("revid")+"&sbinfo="+sbuffer.toString();


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
		String revid = request.getParameter("revid");
		reviewService.removeReview(Long.parseLong(revid));
		return queryHistroyAUP(model,response,request);
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
		String [] plusys  = (String[]) param.get("plusystrue");  // 游客差价
		String [] travelerids = (String[]) param.get("travelerids"); // 游客编号
		String [] gaijiacurency =(String[]) param.get("gaijiacurency"); // 游客币种
		String [] travelerremark = (String[]) param.get("travelerremark"); // 游客备注
		
		
		if(loopCheckArrs(plusys)){ 
			sbuffer.append(" 游客差价列表为空！ ");
			return;
		}
		for(int i=0;i<plusys.length;i++){
			if(Double.parseDouble(plusys[i]) == 0){  // 改差价为默认的 ，就不启动工作流申请改价！
				plusys[i] = "0.00";
			}
		}		
		// 获取游客列表信息~
		List<Map<String,Object>> travelerList = airTicketUpPricesService.queryTravelerList(orderId);
		String trid = "";
		double chae = calculatePrices(plusys); //计算订单改价差额
		// add by yunpeng.zhang 针对需求 C369,越柬行踪改价
		Map<String, Double> timesAndRmbPrice = calculatePrices(plusys, gaijiacurency);
		// 如果批发商为越柬行踪， 低改在80元人民币内无需审核，直接通过。 add by yunpeng.zhang
		String companyName = UserUtils.getUser().getCompany().getCompanyName();
		if("越柬行踪".equals(companyName) && timesAndRmbPrice.get("rmbChae") >= 0.00 && timesAndRmbPrice.get("times") > 0
				&&  timesAndRmbPrice.get("wbChae").equals(timesAndRmbPrice.get("rmbChae"))) {
			hqxShenQingTiaoJia(param, orderId, productType, flowType, sbuffer);
			return ;
		}
		
		/**
		 * 验证环球行公司申请改价并且是向上调价 --- 直接修改差额不需要走审核流程
		 * update by ruyi.chen （改价均改为高改不需要审核）
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
						List<Map<String,Object>>  moneys = (List<Map<String,Object>>) raw.get("travelers") ; // 游客币种
					
					// 遍历游客的币种信息
					for(int mi=0;mi<moneys.size();mi++){
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
			boolean flagSign = airTicketUpPricesService.verifyTravelerFlowsign(orderId, productType, flowType,gaijiacurency[i],id.getValue());
			if(flagSign){ // 验证此流程是否申请记录
				Long airTicketId = airTicketOrderService.getAirticketorderById(StringUtils.toLong(orderId)).getAirticketId();
				Long deptId = activityAirTicketService.getActivityAirTicketById(airTicketId).getDeptId();
				reviewService.addReview(Integer.parseInt(productType), Integer.parseInt(flowType), orderId, Long.parseLong(travelerids[i].equals("")?trid:travelerids[i]), 0l, travelerremark[i], sbuffer, listDetail, deptId); 
				if(sbuffer.length() >0){   // 只要sbuffer的长度大于0了说明没有申请成功!
					break; 
				}
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
	@SuppressWarnings({ "rawtypes", "static-access" })
    public void frontMoneyForApplyFlow(Map<String,Object> param,String orderId,String productType,String flowType,StringBuffer sbuffer){
		if(param.get("plusdj") ==null || param.get("djremark")==null){
			return ;
		}
				
		String [] plusdj = (String[]) param.get("plusdj"); // 订单定金差价
		String [] djremark = (String[]) param.get("djremark");  //订单定金改价备注
		int djs = plusdj.length;
		//  获取订单定金~
		List<Map<String,Object>> frontList = airTicketUpPricesService.queryFrontMoneyList(orderId);
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
			if(raw.get("moneyType").toString().equals(this.DEF_MT_YSDJ)){ 		// 原始定金结算价
				oldm.setValue(String.valueOf(raw.get("amount"))); 
			}else if(raw.get("moneyType").toString().equals(this.DEF_MT_JSJ)){	// 当前定金结算价
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
		boolean flagSign = airTicketUpPricesService.verifyfrontMoneyFlowsign(orderId, productType, flowType, cid.getValue());
		if(flagSign){
			Long airTicketId = airTicketOrderService.getAirticketorderById(StringUtils.toLong(orderId)).getAirticketId();
			Long deptId = activityAirTicketService.getActivityAirTicketById(airTicketId).getDeptId();
			long l = reviewService.addReview(Integer.parseInt(productType), Integer.parseInt(flowType), orderId, -1l, 0l, djremark[0], sbuffer, listDetail, deptId); 
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
		if(param.get("teammoney") ==null || param.get("teamkx")==null ||  param.get("teamcurrency") ==null || param.get("teamremark") ==null){
			return ;
		}
		String [] teammoney = (String[]) param.get("teammoney") ; // 订单团队差价
		String [] teamkx = (String[]) param.get("teamkx"); // 订单团队款项
		String [] teamcurrency = (String[]) param.get("teamcurrency"); // 订单团队币种
		String [] teamremark = (String[]) param.get("teamremark"); // 订单团队备注
		
	
		List<Map<String,Object>> orderReceivable = airTicketUpPricesService.queryOrderReceivable(orderId);
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
			Long airTicketId = airTicketOrderService.getAirticketorderById(StringUtils.toLong(orderId)).getAirticketId();
			Long deptId = activityAirTicketService.getActivityAirTicketById(airTicketId).getDeptId();
			long l = reviewService.addReviewNoLimit(Integer.parseInt(productType), Integer.parseInt(flowType), orderId, 0l, 0l, teamremark[i], sbuffer, listDetail, deptId);
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
	public Object checkMutex(Model model,HttpServletResponse response,HttpServletRequest request){
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
			travelerList = airTicketUpPricesService.queryTravelerUpPrices(orderId);
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
		Map<String,Object> rMap = orderReviewService.getTravelerReviewMutexInfo(Long.parseLong(orderId), productType, Integer.parseInt(flowType), travelers);
		
		
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
					wbChae += rmbChae;
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
		String [] plusys  = (String[]) param.get("plusystrue"); 		 // 游客差价
		String [] travelerids = (String[]) param.get("travelerids"); // 游客编号
		String [] gaijiacurency =(String[]) param.get("gaijiacurency"); // 游客币种
		String [] travelerremark = (String[]) param.get("travelerremark"); // 游客备注
		
		if(loopCheckArrs(plusys)){ 
			sbuffer.append(" 游客差价列表为空！ ");
			return ;
		}
		for(int i=0;i<plusys.length;i++){
			if(Double.parseDouble(plusys[i]) == 0){  // 改差价为默认的 ，就不启动工作流申请改价！
				plusys[i] = "0.00";
			}
		}
		// 获取游客列表信息~
		List<Map<String,Object>> travelerList = airTicketUpPricesService.queryTravelerList(orderId);
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
						List<Map<String,Object>>  moneys = (List<Map<String,Object>>) raw.get("travelers") ; // 游客币种
					
					// 遍历游客的币种信息
					for(int mi=0;mi<moneys.size();mi++){
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
		
			Long airTicketId = airTicketOrderService.getAirticketorderById(StringUtils.toLong(orderId)).getAirticketId();
			Long deptId = activityAirTicketService.getActivityAirTicketById(airTicketId).getDeptId();
			
			long rid = reviewService.addSuccessReview(Integer.parseInt(productType), Integer.parseInt(flowType), orderId,  Long.parseLong(travelerids[i].equals("")?trid:travelerids[i])
					,(long) 0, travelerremark[i], sbuffer, listDetail, deptId);
			if (rid > 0) {
				boolean flag = changePriceReviewService.doChangePrice(params);
				logger.info("--- > [申请改价日志] 此改价不需要走审核流程，直接改价, 是否成功:"+flag +" 改价信息:"+sbuffer.toString() +", 改价参数:"+params);
			}
		}
	}
	
	
}
