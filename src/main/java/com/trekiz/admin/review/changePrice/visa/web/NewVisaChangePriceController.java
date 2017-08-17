package com.trekiz.admin.review.changePrice.visa.web;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quauq.review.core.engine.UserReviewPermissionChecker;
import com.quauq.review.core.engine.config.ReviewConstant;
import com.quauq.review.core.engine.config.ReviewVariableKey;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.ReviewResult;
import com.quauq.review.core.type.OrderByDirectionType;
import com.quauq.review.core.type.OrderByPropertiesType;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.utils.Arith;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.airticket.service.UPPricesGenerateHTML;
import com.trekiz.admin.modules.money.service.MoneyAmountService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.review.changeprice.entity.ChangePriceBean;
import com.trekiz.admin.modules.review.changeprice.service.IChangePriceReviewService;
import com.trekiz.admin.modules.reviewflow.entity.Detail;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;
import com.trekiz.admin.modules.visa.service.VisaUpPricesService;
import com.trekiz.admin.review.changePrice.visa.service.INewVisaChangePriceService;
import com.trekiz.admin.review.money.entity.NewProcessMoneyAmount;
import com.trekiz.admin.review.money.service.NewProcessMoneyAmountService;

/**
 * 新签证改价Controller
 * @author ang.gao
 * @date 2015年12月1日
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@Controller
@RequestMapping(value = "${adminPath}/changeprice/visa")
public class NewVisaChangePriceController {
	
	@Autowired
	private INewVisaChangePriceService newVisaChangePriceService;
	
	@Autowired
	private com.quauq.review.core.engine.ReviewService processReviewService;
	
	@Autowired
	private VisaUpPricesService visaUpPricesService;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private IChangePriceReviewService changePriceReviewService;
	
	@Autowired
	private NewProcessMoneyAmountService processMoneyAmountService;
	
	@Autowired
	private VisaOrderService visaOrderService;
	
	@Autowired
	private MoneyAmountService moneyAmountService;
	
	@Autowired
	private VisaProductsService visaProductsService;
	
	@Autowired
	private OrderCommonService orderCommonService;
	
	@Autowired
	private ActivityGroupService activityGroupService;
	
	@Autowired
	private UserReviewPermissionChecker permissionChecker;
	
	
	private static final int CHANGEPRICE_PRODUCTTYPE_VISA = 6;//签证产品
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
		String flowType = "10";  // 流程类型
		//String flowType = request.getParameter("flowType");  // 流程类型
		
		boolean flag = false;
		if(StringUtils.isNotBlank(orderId) && StringUtils.isNotBlank(productType) && StringUtils.isNotBlank(flowType)){
			List<ReviewNew> reviewList = processReviewService.getOrderReviewList(orderId, productType, flowType);
			for(ReviewNew reviewNew : reviewList){
				if(reviewNew.getDelFlag()==0 && reviewNew.getStatus()==ReviewConstant.REVIEW_STATUS_PROCESSING){//处理中(审批中)
					flag = true;
				}
			}
		}
		
		//查询产品部门ID
		List<Object> deptList = newVisaChangePriceService.getDeptIdByOrderId(orderId);
		Long deptId = Long.parseLong(deptList.get(0).toString());
		//查询改价申请列表
//		List<Map<String, Object>> list = reviewService.findReviewListMap(Integer.parseInt(productType), Integer.parseInt(flowType),orderId, false,"", visaOrderService.getProductPept(orderId));
		List<Map<String, Object>> list = processReviewService.getReviewDetailMapListByOrderId(deptId, Integer.parseInt(productType), 
				Context.REVIEW_FLOWTYPE_CHANGE_PRICE, orderId, OrderByPropertiesType.CREATE_DATE, OrderByDirectionType.DESC);
		Map<String,String> map = new HashMap<String, String>();
		map.put("orderId", orderId);
		map.put("productType",productType);
		map.put("flowType", flowType);
		model.addAttribute("visaChangePriceList", list);
		model.addAllAttributes(map);
		model.addAttribute("flag", flag);
		model.addAttribute("loginUser", UserUtils.getUser().getId().toString());
		return "review/changePrice/visa/visaUpPrices";
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
		return "review/changePrice/visa/upVisaDetails";
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
			String reviewId = request.getParameter("revid");
			processReviewService.cancel(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid(), null, reviewId, null, null);
			return "redirect:" + Global.getAdminPath() + "/visaUpProces/upVisaPrices?orderId="+orderId+"&productType="+productType+"&flowType="+flowType+"&sign="+param.get("sign")+"&revid="+param.get("revid");
		}
		//获取当前登陆用户的币种
		moneyTypes = UPPricesGenerateHTML.getCurrencyParam(currencyService);
		 
		StringBuffer sbuffer = new StringBuffer();
		/* 游客改价申请**/
		String applyResult = travelerForApplyFlow(param, orderId, productType, flowType, sbuffer);
		
		Map<String,String> rMap = new HashMap<String, String>();
		rMap.put("applyResult", applyResult);
		rMap.put("sbinfo", sbuffer.toString());
		rMap.put("orderId",orderId);
		rMap.put("productType",productType);
		rMap.put("flowType",flowType);
		
		return rMap;
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
	 * 游客改价申请
	 * @param param 游客信息
	 * @param orderId  订单编号
	 * @param productType 产品类型
	 * @param flowType 流程类型
	 * @return 返回调用工作流的执行结果
	 */
	public String  travelerForApplyFlow(Map<String,Object> param,String orderId,String productType,String flowType,StringBuffer sbuffer){
		String [] plusys  = (String[]) param.get("plusys");  // 游客差价
		String [] travelerids = (String[]) param.get("travelerids"); // 游客编号
		String [] gaijiacurency =(String[]) param.get("gaijiacurency"); // 游客币种
		String [] travelerremark = (String[]) param.get("travelerremark"); // 游客备注
		
		if(loopCheckArrs(plusys)){ 
			sbuffer.append(" 游客差价列表为空！ ");
			return sbuffer.toString();
		}
		//获取签证订单数据
		VisaOrder visaOrder = visaOrderService.findVisaOrder(StringUtils.toLong(orderId));
		Long deptId = visaOrderService.getDeptId(orderId);
		// 获取游客列表信息
		List<Map<String,Object>> travelerList = visaUpPricesService.queryTravelerList(orderId);
		//String trid = "";
		VisaProducts visaProducts = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());

//		/**
//		 * 验证环球行公司申请改价并且是向上调价 --- 直接修改差额不需要走审核流程
//		 */
//		if(chae>0 && UserUtils.getUser().getCompany().getUuid() == "7a816f5077a811e5bc1e000c29cf2586"){//环球行
//			hqxShenQingTiaoJia(param, orderId, productType, flowType, sbuffer);
//			return ;
//		}
		
		/**
		 * 普通的改价申请流程
		 */
		for(int i=0;i<plusys.length;i++){
			if(plusys[i].equals("0.00")){  // 改差价为默认的 ，就不启动工作流申请改价！
				continue;
			}
			// 流程属性数据
			List<Detail> listDetail = new ArrayList<Detail>(); 
			//trid = travelerids[i];
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
					//trid = travelerids[i-1];
					id.setValue(travelerids[i-1]);
					if(id.getValue().equals("")){
						//trid = travelerids[i-2];
						id.setValue(travelerids[i-2]);
						if(id.getValue().equals("")){
							//trid = travelerids[i-3];
							id.setValue(travelerids[i-3]);
							if(id.getValue().equals("")){
								//trid = travelerids[i-4];
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
			
			Map<String, Object> variables = new HashMap<String, Object>();
			for(Detail detail : listDetail){
				variables.put(detail.getKey(), detail.getValue());
			}
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT, visaOrder.getAgentinfoId());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID, orderId);
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, "6");
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID,visaOrder.getVisaProductId());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO, visaOrder.getOrderNo());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, visaProducts.getGroupCode());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, visaOrder.getCreateBy().getId());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR, visaOrder.getCreateBy().getId());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR_NAME, UserUtils.getUser(visaOrder.getCreateBy().getId()).getName());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER_NAME, visaOrder.getSalerName());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, travelerids[i]);
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, variables.get(ChangePriceBean.TRAVALER_NAME));
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK, variables.get(ChangePriceBean.CHANGEDREMARK));
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_NAME, visaProducts.getProductName());
			//variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_EXTEND_1, chae);
			variables.put("changePrice", Double.parseDouble(plusys[i]));
			
			/**
			 * 此次改价记录到流程列表中
			 */
			ReviewResult result = processReviewService.start(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid(), permissionChecker, "", Integer.parseInt(productType), Context.REVIEW_FLOWTYPE_CHANGE_PRICE, deptId, "", variables);
		    if(result.getSuccess()){// start 成功
		    	String rid = result.getReviewId();
			    Integer reviewStatus = result.getReviewStatus();//审核状态
			    if(reviewStatus == ReviewConstant.REVIEW_STATUS_PASSED){//不需审批直接通过，进行改价业务数据处理
					Map<String, String> params = new HashMap<String, String>();
					params.put("travelerId", travelerids[i]);
					params.put("orderId", orderId);
					params.put("orderType", productType);
					params.put("currencyId", gaijiacurency[i]);
					params.put("amount", plusys[i]);
				    changePriceReviewService.doChangePrice(params);
			    }
			    
			    NewProcessMoneyAmount newProcessMoneyAmount = new NewProcessMoneyAmount();
				newProcessMoneyAmount.setId(UuidUtils.generUuid()); 
				newProcessMoneyAmount.setCurrencyId(Integer.parseInt(gaijiacurency[i]));
				newProcessMoneyAmount.setAmount(new BigDecimal(plusys[i]));
				newProcessMoneyAmount.setMoneyType(Context.REVIEW_FLOWTYPE_CHANGE_PRICE);
				newProcessMoneyAmount.setOrderType(CHANGEPRICE_PRODUCTTYPE_VISA);
				newProcessMoneyAmount.setCreatedBy(UserUtils.getUser().getId());
				newProcessMoneyAmount.setCreateTime(new Date());
				newProcessMoneyAmount.setReviewId(rid);
				processMoneyAmountService.saveNewProcessMoneyAmount(newProcessMoneyAmount);
		    }else{
		    	return result.getMessage();
		    }
			
		}
		
		return "success";
	}
	
	/**
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
		//获取签证订单数据
		VisaOrder visaOrder = visaOrderService.findVisaOrder(StringUtils.toLong(orderId));
		Long deptId = visaOrderService.getDeptId(orderId);
		// 获取游客列表信息
		List<Map<String,Object>> travelerList = visaUpPricesService.queryTravelerList(orderId);
		//String trid = "";
				
		/**
		 * 环球行申请改价业务代码开始
		 */
		for(int i=0;i<plusys.length;i++){
			if(plusys[i].equals("0.00")){  // 改差价为默认的 ，就不启动工作流申请改价！
				continue;
			}
			// 流程属性数据
			List<Detail> listDetail = new ArrayList<Detail>(); 
			//trid = travelerids[i];
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
					//trid = travelerids[i-1];
					id.setValue(travelerids[i-1]);
					if(id.getValue().equals("")){
						//trid = travelerids[i-2];
						id.setValue(travelerids[i-2]);
						if(id.getValue().equals("")){
							//trid = travelerids[i-3];
							id.setValue(travelerids[i-3]);
							if(id.getValue().equals("")){
								//trid = travelerids[i-4];
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
			
			Map<String, Object> variables = new HashMap<String, Object>();
			for(Detail detail : listDetail){
				variables.put(detail.getKey(), detail.getValue());
			}
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_AGENT, visaOrder.getAgentinfoId());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_ID, orderId);
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_TYPE, "6");
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_PRODUCT_ID,visaOrder.getVisaProductId());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_NO, visaOrder.getOrderNo());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_GROUP_CODE, visaOrder.getGroupCode());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_OPERATOR, visaOrder.getCreateBy().getId());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR, visaOrder.getCreateBy().getId());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_ORDER_CREATOR_NAME, UserUtils.getUser(visaOrder.getCreateBy().getId()).getName());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_SALER_NAME, visaOrder.getSalerName());
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_ID, travelerids[i]);
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_TRAVELLER_NAME, variables.get(ChangePriceBean.TRAVALER_NAME));
			variables.put(ReviewVariableKey.REVIEW_VARIABLE_KEY_REMARK, variables.get(ChangePriceBean.CHANGEDREMARK));
			
			/**
			 *  环球行改价
			 */
					Map<String, String> params = new HashMap<String, String>();
					params.put("travelerId", travelerids[i]);
					params.put("orderId", orderId);
					params.put("orderType", productType);
					params.put("currencyId", gaijiacurency[i]);
					params.put("amount", plusys[i]);
					changePriceReviewService.doChangePrice(params);
					/**
					 * 此次改价记录到流程列表中
					 */
					ReviewResult result = processReviewService.start(UserUtils.getUser().getId().toString(), UserUtils.getUser().getCompany().getUuid(), permissionChecker, "", Integer.parseInt(productType), Context.REVIEW_FLOWTYPE_CHANGE_PRICE, deptId, "", variables);
				    String rid = result.getReviewId();
				    
				    NewProcessMoneyAmount newProcessMoneyAmount = new NewProcessMoneyAmount();
					newProcessMoneyAmount.setId(UuidUtils.generUuid()); 
					newProcessMoneyAmount.setCurrencyId(Integer.parseInt(gaijiacurency[i]));
					newProcessMoneyAmount.setAmount(new BigDecimal(plusys[i]));
					newProcessMoneyAmount.setMoneyType(Context.REVIEW_FLOWTYPE_CHANGE_PRICE);
					newProcessMoneyAmount.setOrderType(CHANGEPRICE_PRODUCTTYPE_VISA);
					newProcessMoneyAmount.setCreatedBy(UserUtils.getUser().getId());
					newProcessMoneyAmount.setCreateTime(new Date());
					newProcessMoneyAmount.setReviewId(rid);
					newProcessMoneyAmount.setDelFlag("0");
					newProcessMoneyAmount.setCompanyId(UserUtils.getUser().getCompany().getUuid());
					processMoneyAmountService.saveNewProcessMoneyAmount(newProcessMoneyAmount);
		}
		/**
		 * 环球行申请改价业务代码结束
		 */
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
		String reviewId = request.getParameter("revid");
		String companyId = UserUtils.getUser().getCompany().getId().toString();
		String userId = UserUtils.getUser().getId().toString();
		processReviewService.cancel(userId, companyId, "", reviewId, "", null);
		
		return queryVisaHistoryRecord(model, response, request);
	}
	
	/**
	 * 查询每条记录的改价详情
	 * @param model
	 * @param response
	 * @param request
	 * @param orderId
	 * @param productType
	 * @param flowType
	 * @param idStr
	 * @return
	 */
	@RequestMapping(value="changePriceNew/{orderId}/{productType}/{flowType}/{idStr}")
	public String querySingleChangePrice(Model model,HttpServletResponse response,HttpServletRequest request,
		   @PathVariable String orderId,@PathVariable String productType,@PathVariable String flowType,
		   @PathVariable String idStr){
		
		String mainOrderCodeStr = request.getParameter("mainOrderCode");
		
		Long mainOrderCode = 0L;
		if(null != mainOrderCodeStr && !"".equals(mainOrderCodeStr) && mainOrderCodeStr.length() > 0){
			mainOrderCode = Long.parseLong(mainOrderCodeStr);
		}
		
		if(StringUtils.isBlank(flowType)) {
			flowType = "10";
		}
		
		//查询产品部门ID
		List<Object> deptList = newVisaChangePriceService.getDeptIdByOrderId(orderId);
		Long deptId = Long.parseLong(deptList.get(0).toString());
		
		// 调用公用工作流查询方法,取到流程表里的签证改价记录信息
		//List<Map<String, Object>> list = reviewService.findReviewListMapById(Integer.parseInt(productType), Integer.parseInt(flowType),orderId,id);
		List<Map<String, Object>> list = processReviewService.getReviewDetailMapListByOrderId(deptId, Integer.parseInt(productType), 
				Context.REVIEW_FLOWTYPE_CHANGE_PRICE, orderId, OrderByPropertiesType.CREATE_DATE, OrderByDirectionType.DESC);
		model.addAttribute("airticketReturnList", list);
		
		Map<String,String> map = new HashMap<String, String>();
		map.put("orderId", orderId);
		map.put("productType",productType);
		map.put("flowType", flowType);
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
			
		}
		
		model.addAttribute("rid", idStr);
		return "review/changePrice/visa/visaChangePricesNew";
	}
}
