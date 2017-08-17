package com.trekiz.admin.modules.visa.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.utils.Arith;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.airticket.service.UPPricesGenerateHTML;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.service.OrderReviewService;
import com.trekiz.admin.modules.review.changeprice.entity.ChangePriceBean;
import com.trekiz.admin.modules.review.changeprice.service.IChangePriceReviewService;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaUpPricesService;

/**
 * VisaUpPricesController.java 功能:签证申请改价 控制器
 * @author HPT
 *
 */
@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
@Controller
@RequestMapping(value = "${adminPath}/visaUpProces/")
public class VisaUpPricesController extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(VisaUpPricesController.class); 
	private final String DEF_YS = "13" , DEF_YSYS = "19" ;  // 13应收, 19原始应收
	@Autowired
	private VisaUpPricesService visaUpPricesService ;
	@Autowired
	private ReviewService reviewService ;
	@Autowired
	private CurrencyService currencyService ;
	
	@Autowired
	private VisaOrderService visaOrderService;
	
	@Autowired
	private OrderCommonService orderCommonService;
	
	@Autowired
	private ActivityGroupService activityGroupService;
	
	@Autowired
	private VisaProductsService visaProductsService;
	@Autowired
	private MoneyAmountService moneyAmountService;
	/**
	 * 订单流程服务者
	 */
	@Autowired
	private OrderReviewService orderReviewService ;
	@Autowired
	private IChangePriceReviewService changePriceReviewService;
	
	private static Map moneyTypes = new HashMap<String, String>(); // 定义币种信息
	
	/**
	 * 查询签证改价记录
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="list")
	public String queryVisaHistoryRecord(Model model,HttpServletResponse response,HttpServletRequest request){
		String orderId = request.getParameter("orderId") ; // 订单编号
		String productType = request.getParameter("productType"); //订单类型
		String flowType = request.getParameter("flowType");  // 流程类型
		boolean flag = reviewService.verifyWorkFlowStatus(orderId, Integer.parseInt(productType), Integer.parseInt(flowType));
		// 调用公用工作流查询方法,取到流程表里的机票改价记录信息
		List<Map<String, Object>> list = reviewService.findReviewListMap(Integer.parseInt(productType), Integer.parseInt(flowType),orderId, false,"", visaOrderService.getProductPept(orderId));
		Map<String,String> map = new HashMap<String, String>();
		map.put("orderId", orderId);
		map.put("productType",productType);
		map.put("flowType", flowType);
		model.addAttribute("airticketReturnList", list);
		model.addAllAttributes(map);
		model.addAttribute("flag", flag);
		return "modules/visa/visaUpPrices";
	}
	/**
	 * 跳转到改价申请页面
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="upVisaPrices")
	public String gotoUpVisaPrices(Model model,HttpServletResponse response,HttpServletRequest request){
		Map<String, Object> resultMaps = new HashMap<String, Object>() ;  // 返回结果集合 
		String orderId = request.getParameter("orderId") ; // 订单编号
		String productType = request.getParameter("productType"); //订单类型
		String flowType = request.getParameter("flowType");  // 流程类型
		// 获取签证费
		List<Map<String,Object>> visaMoneyList = visaUpPricesService.queryVisaMoneyList(orderId);
		// 获取游客列表
		List<Map<String,Object>> travelerList = visaUpPricesService.queryTravelerList(orderId);
		// 生成HTML元素
		String travelers = UPPricesGenerateHTML.generateTravelers(travelerList);
		resultMaps.put("html", travelers);
		
		resultMaps.put("visaMoney",visaMoneyList);
		model.addAttribute("resultMaps", resultMaps);
		List<Currency> currencylist = currencyService.findCurrencyList(Currency.DEL_FLAG_NORMAL);
		model.addAttribute("curlist", currencylist);
		Map<String,String> map = new HashMap<String, String>();
		map.put("orderId", orderId);
		map.put("productType",productType);
		map.put("flowType", flowType);
		model.addAllAttributes(map);
		return "modules/visa/upVisaDetails";
	}
	
	/**
	 * 格式化定金
	 * @param visaMoneyList
	 * @return
	 */
	public List<Map<String,Object>> formatVisaTotalMoney(List<Map<String,Object>> visaMoneyList){
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		// 处理团队签证改价
		if(null != visaMoneyList && visaMoneyList.size()>0){
			for(Map<String,Object> temp : visaMoneyList){
				Map<String,Object> visaMoney = new HashMap<String, Object>();
				visaMoney.put(ChangePriceBean.CHANGED_FUND, "签证费");
				visaMoney.put(ChangePriceBean.CURRENCY_NAME,temp.get("currency_name")); 
				visaMoney.put(ChangePriceBean.CURRENCY_ID, temp.get("currency_id")); 
				if(temp.get("moneyType").toString().equals(DEF_YS)){  // 应收金额  
					visaMoney.put(ChangePriceBean.CUR_TOTAL_MONEY, temp.get("amount"));
				}else if(temp.get("moneyType").toString().equals(DEF_YSYS)){ // 原始应收金额
					visaMoney.put(ChangePriceBean.OLD_TOTAL_MONEY, temp.get("amount"));
				}
				
				/* 如果团队签证改价为空则赋上默认值*/
				if(null == visaMoney.get(ChangePriceBean.CHANGED_FUND)){
					visaMoney.put(ChangePriceBean.CHANGED_FUND, "订金");
				} if(null == visaMoney.get(ChangePriceBean.CURRENCY_NAME)){
					visaMoney.put(ChangePriceBean.CURRENCY_NAME, "人民币");
				} if(null == visaMoney.get(ChangePriceBean.OLD_TOTAL_MONEY)){
					visaMoney.put(ChangePriceBean.OLD_TOTAL_MONEY, "0.00");
				} if(null == visaMoney.get(ChangePriceBean.CUR_TOTAL_MONEY)){
					visaMoney.put(ChangePriceBean.CUR_TOTAL_MONEY, "0.00");
				} if(null == visaMoney.get(ChangePriceBean.CURRENCY_ID)){
					visaMoney.put(ChangePriceBean.CURRENCY_ID, "1");
				}
				// 放入到新list里
				resultList.add(visaMoney);
			}
			
		}
		
		return resultList;
	}
	/**
	 * 查询单个改价信息
	 * @param model
	 * @param response
	 * @param request
	 * @param orderId
	 * @param productType
	 * @param flowType
	 * @param idStr
	 * @return
	 */
	@RequestMapping(value="changePrice/{orderId}/{productType}/{flowType}/{idStr}")
	public String querySingleChangePrice(Model model,HttpServletResponse response,HttpServletRequest request,
		   @PathVariable String orderId,@PathVariable String productType,@PathVariable String flowType,
		   @PathVariable String idStr){
		
		String mainOrderCodeStr = request.getParameter("mainOrderCode");
		String details = request.getParameter("details");
		
		Long mainOrderCode = 0L;
		if(null != mainOrderCodeStr && !"".equals(mainOrderCodeStr) && mainOrderCodeStr.length() > 0){
			mainOrderCode = Long.parseLong(mainOrderCodeStr);
		}
		
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
		
		//订单封装
		VisaOrder visaOrder = visaOrderService.findVisaOrder(Long.parseLong(orderId));
		String totalMoney = moneyAmountService.getMoney(visaOrder.getTotalMoney());
		model.addAttribute("visaOrder", visaOrder);
		model.addAttribute("totalMoney", totalMoney);
		
		//产品封装
		VisaProducts visaProduct = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
		model.addAttribute("visaProduct", visaProduct);
		
		if(mainOrderCode!=null && mainOrderCode>0){
			//参团签证订单 暂时屏蔽
			ProductOrderCommon productOrder = orderCommonService.getProductorderById(mainOrderCode);
			ActivityGroup activityGroup = activityGroupService.findById(productOrder.getProductGroupId());
			model.addAttribute("productOrder", productOrder);
			model.addAttribute("activityGroup", activityGroup);
			
		}else{
			//单办签证订单
		}
		model.addAttribute("rid", idStr);
		return "modules/visa/visaChangePrices";
	}
	
	/**
	 * 申请改价
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="applyForUpVisaPrices")
	@ResponseBody
	public Object applyForUpVisaPrices(Model model,HttpServletResponse response,HttpServletRequest request){
		Map param = getRequestParamater(request);
		String orderId = request.getParameter("orderId") ; // 订单编号
		String productType = request.getParameter("productType"); //产品类型
		String flowType = request.getParameter("flowType");  // 流程类型
		if(null != param.get("sign")){  // 重新申请改价流程
			String revid = request.getParameter("revid");
			reviewService.removeReview(Long.parseLong(revid));
			return "redirect:" + Global.getAdminPath() + "/visaUpProces/upVisaPrices?orderId="+orderId+"&productType="+productType+"&flowType="+flowType+"&sign="+param.get("sign")+"&revid="+param.get("revid");
		}
		
		 moneyTypes = UPPricesGenerateHTML.getCurrencyParam(currencyService);
		 
		StringBuffer sbuffer = new StringBuffer();
		/* 1.游客改价申请**/
		// 
		String compUUID  = UserUtils.getUser().getCompany().getUuid();
		if(compUUID.equals("7a81b21a77a811e5bc1e000c29cf2586"))
			travelerForApplyFlowYuejian(param, orderId, productType, flowType, sbuffer);
		else
		travelerForApplyFlow(param, orderId, productType, flowType, sbuffer);
//		/* 2.团队签证改价申请**/
//		visaMoneyForApplyFlow(param,orderId, productType, flowType, sbuffer);
//		/* 3.订单其它改价申请**/
//		teamOrderForApplyFlow(param,orderId, productType, flowType, sbuffer);
		
		super.saveUserOpeLog(Context.log_type_orderform, Context.log_type_orderform, "签证申请改价-  回调流程接口" +
				"返回信息: " + sbuffer.toString(), Context.log_state_up, Context.ProductType.PRODUCT_VISA, null);
		
		Map<String,String> rMap = new HashMap<String, String>();
		rMap.put("sbinfo", sbuffer.toString());
		rMap.put("orderId",orderId);
		rMap.put("productType",productType);
		rMap.put("flowType",flowType);
		return rMap;
		// return "redirect:" + Global.getAdminPath() + "/visaUpProces/list?orderId="+orderId+"&productType="+productType+"&flowType="+flowType+"&sign="+param.get("sign")+"&revid="+param.get("revid");
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
		return queryVisaHistoryRecord(model, response, request);
	}
	
	/**
	 * 获取前台请求参数
	 * @param request
	 * @return
	 */
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
	 * 游客改价申请 只针对越柬行踪
	 * @param param 游客信息
	 * @param orderId  订单编号
	 * @param productType 产品类型
	 * @param flowType 流程类型
	 * @return 返回调用工作流的执行结果
	 */
	public void  travelerForApplyFlowYuejian(Map<String,Object> param,String orderId,String productType,String flowType,StringBuffer sbuffer){
		String [] plusys  = (String[]) param.get("plusys");  // 游客差价
		String [] travelerids = (String[]) param.get("travelerids"); // 游客编号
		String [] gaijiacurency =(String[]) param.get("gaijiacurency"); // 游客币种
		String [] travelerremark = (String[]) param.get("travelerremark"); // 游客备注
		
		if(loopCheckArrs(plusys)){ 
			sbuffer.append(" 游客差价列表为空！ ");
			return;
		}
		// 获取游客列表信息~
		List<Map<String,Object>> travelerList = visaUpPricesService.queryTravelerList(orderId);
		String trid = "";
		boolean flag = calculatePricesYuejian(plusys,gaijiacurency); //计算订单改价差额
		//对于人民币改价额度小于80直接通过
		if(flag){
			hqxShenQingTiaoJia(param, orderId, productType, flowType, sbuffer);
			return ;
		}
		double chae = calculatePrices(plusys); //计算订单改价差额
		if(flag == false && chae>0)
		{
			hqxShenQingTiaoJia(param, orderId, productType, flowType, sbuffer);
			return ;
		}
		
		/**
		 * 普通的改价申请流程
		 */
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
			Detail cname = new Detail(ChangePriceBean.CURRENCY_NAME,String.valueOf(moneyTypes.get(cid.getValue()))); // 币种名称
			Detail oldm = new Detail(ChangePriceBean.OLD_TOTAL_MONEY,"0.00"); //原始应收价
			Detail curm = new Detail(ChangePriceBean.CUR_TOTAL_MONEY,"0.00"); //当前应收价
			Detail cfund = new Detail(ChangePriceBean.CHANGED_PRICE,plusys[i]); // 改价差额
			Detail remark = new Detail(ChangePriceBean.CHANGEDREMARK,travelerremark[i]); // 改价备注
			Detail chm = new Detail(ChangePriceBean.CHANGED_TOTAL_MONEY,"0.00"); // 改价后的应收价
			Detail chp = new Detail(ChangePriceBean.CHANGED_FUND,"应收价");  // 改价款项
		
			// 获取游客的 原始应收价 和 当前应收价 
			/**
			 * 计算开始
			 */
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
						List<Map<String,Object>> moneys = (List<Map<String, Object>>) raw.get("travelers"); // 游客币种
					// 遍历游客的币种信息
					for(int mi =0;mi<moneys.size();mi++){
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
			/**
			 * 计算结束
			 */
			
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
			boolean flagSign = visaUpPricesService.verifyTravelerFlowsign(orderId, productType, flowType,gaijiacurency[i],id.getValue());
			if(flagSign){ // 验证此流程是否申请记录
				 reviewService.addReview(Integer.parseInt(productType), Integer.parseInt(flowType), 
						orderId, Long.parseLong(travelerids[i].equals("")?trid:travelerids[i]), (long) 0, travelerremark[i], sbuffer, 
						listDetail, visaOrderService.getProductPept(orderId)); 
				if(sbuffer.length()>0){
					break;
				}
			}
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 游客改价申请
	 * @param param 游客信息
	 * @param orderId  订单编号
	 * @param productType 产品类型
	 * @param flowType 流程类型
	 * @return 返回调用工作流的执行结果
	 */
	public void  travelerForApplyFlow(Map<String,Object> param,String orderId,String productType,String flowType,StringBuffer sbuffer){
		String [] plusys  = (String[]) param.get("plusys");  // 游客差价
		String [] travelerids = (String[]) param.get("travelerids"); // 游客编号
		String [] gaijiacurency =(String[]) param.get("gaijiacurency"); // 游客币种
		String [] travelerremark = (String[]) param.get("travelerremark"); // 游客备注
		
		if(loopCheckArrs(plusys)){ 
			sbuffer.append(" 游客差价列表为空！ ");
			return;
		}
		// 获取游客列表信息~
		List<Map<String,Object>> travelerList = visaUpPricesService.queryTravelerList(orderId);
		String trid = "";
		double chae = calculatePrices(plusys); //计算订单改价差额

		/**
		 * 验证环球行公司申请改价并且是向上调价 --- 直接修改差额不需要走审核流程
		 */
		if(chae>0){
			hqxShenQingTiaoJia(param, orderId, productType, flowType, sbuffer);
			return ;
		}
		/**
		 * 普通的改价申请流程
		 */
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
			Detail cname = new Detail(ChangePriceBean.CURRENCY_NAME,String.valueOf(moneyTypes.get(cid.getValue()))); // 币种名称
			Detail oldm = new Detail(ChangePriceBean.OLD_TOTAL_MONEY,"0.00"); //原始应收价
			Detail curm = new Detail(ChangePriceBean.CUR_TOTAL_MONEY,"0.00"); //当前应收价
			Detail cfund = new Detail(ChangePriceBean.CHANGED_PRICE,plusys[i]); // 改价差额
			Detail remark = new Detail(ChangePriceBean.CHANGEDREMARK,travelerremark[i]); // 改价备注
			Detail chm = new Detail(ChangePriceBean.CHANGED_TOTAL_MONEY,"0.00"); // 改价后的应收价
			Detail chp = new Detail(ChangePriceBean.CHANGED_FUND,"应收价");  // 改价款项
		
			// 获取游客的 原始应收价 和 当前应收价 
			/**
			 * 计算开始
			 */
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
						List<Map<String,Object>> moneys = (List<Map<String, Object>>) raw.get("travelers"); // 游客币种
					// 遍历游客的币种信息
					for(int mi =0;mi<moneys.size();mi++){
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
			/**
			 * 计算结束
			 */
			
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
			boolean flagSign = visaUpPricesService.verifyTravelerFlowsign(orderId, productType, flowType,gaijiacurency[i],id.getValue());
			if(flagSign){ // 验证此流程是否申请记录
				 reviewService.addReview(Integer.parseInt(productType), Integer.parseInt(flowType), 
						orderId, Long.parseLong(travelerids[i].equals("")?trid:travelerids[i]), (long) 0, travelerremark[i], sbuffer, 
						listDetail, visaOrderService.getProductPept(orderId)); 
				if(sbuffer.length()>0){
					break;
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
	public void teamOrderForApplyFlow(Map<String,Object> param,String orderId,String productType,String flowType,StringBuffer sbuffer){
		if(param.get("teammoney") ==null || param.get("teamkx")==null || param.get("teamcurrency") ==null ||param.get("teamremark") ==null){
			return ;
		}
		String [] teammoney = (String[]) param.get("teammoney") ; // 订单团队差价
		String [] teamkx = (String[]) param.get("teamkx"); // 订单团队款项
		String [] teamcurrency = (String[]) param.get("teamcurrency"); // 订单团队币种
		String [] teamremark = (String[]) param.get("teamremark"); // 订单团队备注
		List<Map<String,Object>> orderReceivable = visaUpPricesService.queryVisaMoneyList(orderId);
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
					orderId, 0l, 0l, teamremark[i], sbuffer, listDetail, visaOrderService.getProductPept(orderId));
			sbuffer.append(" 申请改价是否成功:"+l);
		}
	}
	/**
	 * 团队签证费的改价申请
	 * @param param 游客信息
	 * @param orderId  订单编号
	 * @param productType 产品类型
	 * @param flowType 流程类型
	 * @return 返回调用工作流的执行结果
	 */
	public void visaMoneyForApplyFlow(Map<String,Object> param,String orderId,String productType,String flowType,StringBuffer sbuffer){
		if(param.get("plusdj") == null || param.get("djremark") ==null ||param.get("djbz") ==null){
			return ;
		}
		String [] plusdj = (String[]) param.get("plusdj"); // 团队签证费差价
		String [] djremark = (String[]) param.get("djremark");  //团队签证费改价备注
		String [] djbz  = (String[]) param.get("djbz");  // 团队签证费改价币种
		
		int djs = plusdj.length;
		
		//　获取签证费用
		List<Map<String,Object>> visaMoneyList = visaUpPricesService.queryVisaMoneyList(orderId);
		if(djs>0){
			 // 如果定价信息不为空，则定价申请改价 
			if(!loopCheckArrs(plusdj)){
				for(int c =0;c<djs;c++){
					if(plusdj[c].equals("0.00")){ 
						continue;
					}
				
				Detail id = new Detail(ChangePriceBean.TRVALER_ID,"-2");// 订单定金id
				Detail cid = new Detail(ChangePriceBean.CURRENCY_ID,String.valueOf(djbz[c])); // 币种id   
				Detail cname = new Detail(ChangePriceBean.CURRENCY_NAME,String.valueOf(moneyTypes.get(String.valueOf(djbz[c])))); // 币种名称
				Detail oldm = new Detail(ChangePriceBean.OLD_TOTAL_MONEY,"0.00"); //原始应收价
				Detail curm = new Detail(ChangePriceBean.CUR_TOTAL_MONEY,"0.00"); //当前应收价
				Detail cfund = new Detail(ChangePriceBean.CHANGED_PRICE,plusdj[c]); // 改价差额
				Detail remark = new Detail(ChangePriceBean.CHANGEDREMARK,djremark[c]); // 改价备注
				Detail chm = new Detail(ChangePriceBean.CHANGED_TOTAL_MONEY,"0.00");
				Detail chp = new Detail(ChangePriceBean.CHANGED_FUND,"签证费团队改价");
				List<Detail> listDetail = new ArrayList<Detail>();
				
		for(int i =0;i<visaMoneyList.size();i++){
			Map raw = visaMoneyList.get(i);
			if(djbz[c].equals(String.valueOf(raw.get(ChangePriceBean.CURRENCY_ID)))){
			cid.setValue(String.valueOf(raw.get(ChangePriceBean.CURRENCY_ID)));
			cname.setValue(String.valueOf(raw.get(ChangePriceBean.CURRENCY_NAME)));
			// 原始应收价
			oldm.setValue(String.valueOf(raw.get(ChangePriceBean.OLD_TOTAL_MONEY))); 
			// 当前应收价
			curm.setValue(String.valueOf(raw.get(ChangePriceBean.CUR_TOTAL_MONEY))); 
			double f = Arith.add(Double.parseDouble(curm.getValue()),Double.parseDouble(plusdj[c]));// 计算 改后应收价
			chm.setValue(String.valueOf(f));  //赋值 改后应收价
			break;
			}else{
				double f = Arith.add(Double.parseDouble("0.00"),Double.parseDouble(plusdj[c]));// 计算 改后应收价
				chm.setValue(String.valueOf(f));  //赋值 改后应收价
				continue;
			}
			
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
		boolean flagSign = visaUpPricesService.verifyfrontMoneyFlowsign(orderId, productType, flowType, cid.getValue());
		if(flagSign){
			long l = reviewService.addReview(Integer.parseInt(productType), Integer.parseInt(flowType), 
					orderId, -1l, 0l, djremark[c], sbuffer, listDetail, visaOrderService.getProductPept(orderId)); 
			sbuffer.append(" 申请改价是否成功:"+l);
		}
		}
		
		}
		
		}
	}
	
	/**
	 * 检查数组(验证改差价数组是否为空或者默认值)
	 * @param arrs
	 * @return
	 */
	boolean loopCheckArrs(String [] arrs){
		int count = 0;
		if(arrs ==null) return true;
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
	 * checkMutex 检查签证改价流程是否存在互斥
	 * @param model
	 * @param response
	 * @param request
	 * @return
	 * @exception
	 * @since  1.0.0
	 */
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
			travelerList = visaUpPricesService.queryTravelerUpPrices(orderId);
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
	 * calculatePrices 判断差价是否大于80,差额大于80 返回false,需要走审批。
	 * @param pricess
	 * @return
	 * @exception
	 * @since  1.0.0
	 */
	public boolean calculatePricesYuejian(String [] pricess,String [] gaijiacurency){
		List<Map<Object, Object>> list = moneyAmountService.getMoneyIdList();
		
		double count = 0.0;
		for(int i =0;i<pricess.length;i++)
		{
			if(validateMoneyMark(list, gaijiacurency))
			{
				double temp = Double.parseDouble(pricess[i]);
				count =  count + temp;
			}
		}
		if(Double.valueOf("-80") <= count && gaijiacurency.length >0 && validateMoneyMark(list, gaijiacurency) )
			return true;
		else
		    return false;	// 多币种
	}
	
	/**
	 * 
	 */
	
	/**
	 * 判断传递进来的币种中是否全是人民币
	 */
	private boolean validateMoneyMark(List<Map<Object, Object>> list ,String [] gaijiacurency)
	{
		boolean flag =true;
		if(list.size()>0)
		{
			String moneyId =  list.get(0).get("currency_id").toString();
			for(int i=0;i<gaijiacurency.length;i++)
			{
				if(!moneyId.equals(gaijiacurency[i]))
				{
					flag = false;
					break;
				}
			}
		}
		return flag;
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
	 * 
	 * hqxShenQingTiaoJia 环球行申请改价 向上调价
	 * @return
	 * @exception
	 * @since  1.0.0
	 */
	public void hqxShenQingTiaoJia(Map<String,Object> param,String orderId,String productType,String flowType,StringBuffer sbuffer){
		String [] plusys  = (String[]) param.get("plusys"); 		 // 游客差价
		String [] travelerids = (String[]) param.get("travelerids"); // 游客编号
		String [] gaijiacurency =(String[]) param.get("gaijiacurency"); // 游客币种
		String [] travelerremark = (String[]) param.get("travelerremark"); // 游客备注
		
		if(loopCheckArrs(plusys)){ 
			sbuffer.append(" 游客差价列表为空！ ");
			return ;
		}
		// 获取游客列表信息~
				List<Map<String,Object>> travelerList = visaUpPricesService.queryTravelerList(orderId);
				String trid = "";
		/**
		 * 环球行申请改价业务代码开始
		 */
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
			Detail cname = new Detail(ChangePriceBean.CURRENCY_NAME,String.valueOf(moneyTypes.get(cid.getValue()))); // 币种名称
			Detail oldm = new Detail(ChangePriceBean.OLD_TOTAL_MONEY,"0.00"); //原始应收价
			Detail curm = new Detail(ChangePriceBean.CUR_TOTAL_MONEY,"0.00"); //当前应收价
			Detail cfund = new Detail(ChangePriceBean.CHANGED_PRICE,plusys[i]); // 改价差额
			Detail remark = new Detail(ChangePriceBean.CHANGEDREMARK,travelerremark[i]); // 改价备注
			Detail chm = new Detail(ChangePriceBean.CHANGED_TOTAL_MONEY,"0.00"); // 改价后的应收价
			Detail chp = new Detail(ChangePriceBean.CHANGED_FUND,"应收价");  // 改价款项
		
			// 获取游客的 原始应收价 和 当前应收价 
			/**
			 * 计算开始
			 */
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
						List<Map<String,Object>> moneys = (List<Map<String, Object>>) raw.get("travelers"); // 游客币种
					// 遍历游客的币种信息
					for(int mi =0;mi<moneys.size();mi++){
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
			/**
			 * 计算结束
			 */
			
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
			/**
			 *  环球行改价
			 */
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
							,(long) 0, travelerremark[i], sbuffer, listDetail, visaOrderService.getProductPept(orderId));
					  
					logger.info("--- > [申请改价日志] 此改价不需要走审核流程，直接改价, 是否成功:"+flag +" 改价信息:"+sbuffer.toString() +", 改价参数:"+params);
					
				

			
		}
		/**
		 * 环球行申请改价业务代码结束
		 */
	}
}
