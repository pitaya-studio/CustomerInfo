package com.trekiz.admin.review.privilege.singlegroup.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quauq.review.core.support.ReviewResult;
import com.quauq.review.core.type.OrderByDirectionType;
import com.quauq.review.core.type.OrderByPropertiesType;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Context.ProductType;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.review.common.utils.ReviewUtils;
import com.trekiz.admin.review.privilege.singlegroup.entity.PrivilegeBean;
import com.trekiz.admin.review.privilege.singlegroup.service.ISingleGroupPrivilegeService;

/**
 * 散拼优惠申请控制类
 * @author songyang  二〇一六年一月十九日 10:39:30
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/singlegroup/privilege")
public class SingleGroupPrivilegeController {
	
	//singlegroup/privilege/privilegeApplyList
	
	private static final Logger log = Logger.getLogger(SingleGroupPrivilegeController.class);
	
	@Autowired
	private OrderCommonService orderService;
	
	@Autowired
	@Qualifier("travelActivitySyncService")
    private ITravelActivityService travelActivityService;
	
	@Autowired
	private ActivityGroupService activityGroupService;
	
	@Autowired
	private CurrencyService currencyService;
	
	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
	private ISingleGroupPrivilegeService iSingleGroupPrivilegeService;
	
	@Autowired
	private com.quauq.review.core.engine.ReviewService processReviewService;
	
	@Autowired
	private  TravelerService travelerService;
	/**
	 * 进入优惠申请页面
	 * songyang
	 * 2016年1月21日 15:31:58
	 */
	@RequestMapping(value = "toPrivilegeApply")
	public String toPrivilegeApply(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception{
		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		String orderId= request.getParameter("orderId");
		String  productType = request.getParameter("productType");
		ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		List<Currency> currencyList = currencyService.findSortCurrencyList(companyId);
		List<Map<String, Object>> travelerList=orderService.getBorrowingTravelerByOrderId(Long.parseLong(orderId),ProductType.PRODUCT_LOOSE);
		List<Map<String, Object>> newtravelerList = new ArrayList<Map<String, Object>>();
	    for (int i = 0; i < travelerList.size(); i++) {
			if(travelerList.get(i).get("org_discount_price") != null && travelerList.get(i).get("fixed_discount_price") != null) {
				if((travelerList.get(i).get("org_discount_price").toString()).equals(travelerList.get(i).get("fixed_discount_price").toString())){
					newtravelerList.add(travelerList.get(i));
				}
			}
		}
		PrivilegeBean privilegeBean = null;
		//多币种生成json串 前台计算用
		List<Currency> currencyLists = currencyService.findCurrencyList(companyId);
		List<Map<String,String>> listMap=new ArrayList<Map<String,String>>();
		Map<String,String> currMap=null;
		for(Currency curr:currencyLists){
			currMap=new HashMap<String,String>();
			currMap.put("currencyId", curr.getId().toString());
			currMap.put("currencyName", curr.getCurrencyName());
			currMap.put("currencyMark", curr.getCurrencyMark());
			currMap.put("je", "0");
			listMap.add(currMap);
		}
		JSONArray bzJson = JSONArray.fromObject(listMap);
		model.addAttribute("bzList", listMap);
		model.addAttribute("bzJson",bzJson);// 所有币种信息
		//多币种生成json串 前台计算用
		model.addAttribute("privilegeBean", privilegeBean);
		model.addAttribute("userName", productOrder.getCreateBy().getId());
		model.addAttribute("orderCompany", productOrder.getOrderCompanyName());
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		model.addAttribute("flowType", Context.REVIEW_FLOWTYPE_BORROWMONEY);
		model.addAttribute("currencyList", currencyList);
		model.addAttribute("productType",productType);
		model.addAttribute("travelerList", newtravelerList);
//		model.addAttribute("travelerList", travelerList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("product", product);
		model.addAttribute("productOrder", productOrder);
		return "review/privilege/singlegroup/singleGroupPrivilegeApply";
	}
	
	/**
	 * 优惠申请
	 * songyang
	 * 2016年1月21日 15:32:07
	 * @throws Exception 
	 */
	@RequestMapping(value = "privilegeApply")
	public void privilegeApply(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> result =new HashMap<String,String>();
		String data = "";
		try {
		    result =  iSingleGroupPrivilegeService.ApplyPrivilege(request,response);
		    data = JSONObject.fromObject(result).toString();
			ReviewUtils.printJSON(data,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 优惠申请记录
	 * songyang
	 * 2016年1月22日 17:57:39
	 */
	@RequestMapping(value = "privilegeApplyList")
	public String privilegeApplyList(HttpServletRequest request,Model model){
		String orderId = request.getParameter("orderId");
//		DecimalFormat d = new DecimalFormat(",##0.00");
		ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		List<Map<String, Object>> processList =  processReviewService.getReviewDetailMapListByOrderId(product.getDeptId(), Context.ORDER_TYPE_SP , Context.REVIEW_FLOWTYPE_SINGLEGROUP_PRIVILEGE, orderId,OrderByPropertiesType.CREATE_DATE,OrderByDirectionType.DESC);
//		Map<String,Double>  yhTotalPrice = new HashMap<String, Double>();
//		for (int i = 0; i <  processList.size(); i++) {
//			if("2".equals(processList.get(i).get("status").toString())){
//				if(processList.get(i).get("currencyId").toString().split(",").length>1){
//					String[]  currencyMark  = processList.get(i).get("currencyId").toString().split(",");
//					String[]  price = processList.get(i).get("inputsqyhtotalpriceNoMark").toString().split("\\+");
//					for (int j = 0; j <  processList.get(i).get("currencyId").toString().split(",").length; j++) {
//						if(yhTotalPrice.containsKey(currencyService.findCurrencyMark(currencyMark[j]))){
//							yhTotalPrice.put(currencyService.findCurrencyMark(currencyMark[j]), yhTotalPrice.get(currencyService.findCurrencyMark(currencyMark[j]))+Double.parseDouble(price[j]));
//						}else{
//							yhTotalPrice.put(currencyService.findCurrencyMark(currencyMark[j]), Double.parseDouble(price[j].toString()));
//						}
//					}
//				}else{
//					String  currencyMark  = currencyService.findCurrencyMark(processList.get(i).get("currencyId").toString().substring(0, processList.get(i).get("currencyId").toString().length()-1));
//					double price = Double.parseDouble(processList.get(i).get("inputsqyhtotalpriceNoMark").toString());
//					if(yhTotalPrice.containsKey(currencyMark)){
//						yhTotalPrice.put(currencyMark, yhTotalPrice.get(currencyMark)+price);
//					}else{
//						yhTotalPrice.put(currencyMark, price);
//					}
//				}
//			}
//		}
//		String zh = "";
//		for (Object key : yhTotalPrice.keySet()) {
//			zh = zh + key + d.format(yhTotalPrice.get(key)) + "+";
//		}
//		if(StringUtils.isBlank(zh)){
//			model.addAttribute("yhTotalPrice", "");
//		}else{
//			model.addAttribute("yhTotalPrice", zh.substring(0, zh.length()-1));
//		}

		model.addAttribute("processList", processList);
		model.addAttribute("orderId", orderId);
		model.addAttribute("orderDelFlag",productOrder.getDelFlag());
		return "review/privilege/singlegroup/singleGroupPrivilegeApplyList";
	}
	
	
	/**
	 * 优惠详情
	 * songyang
	 * 二〇一六年一月二十二日 14:57:12
	 */
	@RequestMapping(value = "privilegeDetail")
	public String privilegeDetail(HttpServletRequest request,Model model){
//		String orderId  = "11091";
//		String reviewId = "67b8173cb10f4fef9a73465f4a64f328";
//		String reviewId = "7ca1fc8835de46f2947b8da31e071b6e";
//		String reviewId = "c25211cfc5304d2e861e1ab8018969fa";
		String orderId = request.getParameter("orderId");
		String reviewId = request.getParameter("reviewId");
		//报错UUid
//		String reviewId = "718c5eed82494c9da98c25afdf3ee0e5";
		Long companyId = UserUtils.getUser().getCompany().getId();
		ProductOrderCommon productOrder = orderService.getProductorderById(Long.parseLong(orderId));
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		//处理rivew表中的业务数据
		List<Map<String,Object>> rdlist = new ArrayList<Map<String,Object>>();
		Map<String,Object>  processMap = null;
		try{
			if(reviewId!=null){
				processMap = processReviewService.getReviewDetailMapByReviewId(reviewId);
			}
		}catch(Exception e){
			log.error("根据reviewid： " + reviewId + " 查询出来reviewDetail明细报错 ",e);
		}
		
		if(null!=processMap.get("travellerId")){
			String[] travellerId =  processMap.get("travellerId").toString().split(",");
			String[] travellerName =  processMap.get("travellerName").toString().split(",");
			String[] travelerType =  processMap.get("travelerType").toString().split(",");
			String[] currencyId =  processMap.get("currencyId").toString().split(",");
			String[] thPrice = null;
			if(null!=processMap.get("thPrice")){
				 thPrice =  processMap.get("thPrice").toString().split(",");
			}
//			String[] inpuryhprice =  processMap.get("inpuryhprice").toString().split(",");
			String[] thjsjinputprice = null;
			if(null!=processMap.get("thjsjinputprice")){
				thjsjinputprice= processMap.get("thjsjinputprice").toString().split(",");
			}
			//可输入
			String[] sqyhPrice = null;
			if(null != processMap.get("sqyhPrice")){
				sqyhPrice = processMap.get("sqyhPrice").toString().split(",");
			}
			String[] privilegeReason = null;
			if(null!=processMap.get("privilegeReason")){
				privilegeReason = processMap.get("privilegeReason").toString().split(",");
			}
			for (int i = 0; i < travellerId.length; i++) {
				Map<String,Object>  rdMap = new  HashMap<String, Object>();
				rdMap.put("travellerName", travellerName[i]);
				rdMap.put("travelerType", travelerType[i]);
				rdMap.put("currencyId", currencyId[i]);
				if(null!=thPrice){
					rdMap.put("thPrice", thPrice[i]);
				}
//				rdMap.put("inpuryhprice", inpuryhprice[i]);
				rdMap.put("inpuryhprice", travelerService.findTravelerById(Long.parseLong(travellerId[i])).getOrgDiscountPrice());
				if(null != thjsjinputprice){
					rdMap.put("thjsjinputprice", thjsjinputprice[i]);
				}
				//可输入
				if(null!=sqyhPrice){
					rdMap.put("sqyhPrice", sqyhPrice[i]);
				}
				if(null!=privilegeReason){
					rdMap.put("privilegeReason", privilegeReason[i].split("@_@#_=").length>1?privilegeReason[i].split("@_@#_=")[1].toString():"");
				}
				rdlist.add(rdMap);
			}
		}
		//多币种生成json串 前台计算用
		List<Currency> currencyLists = currencyService.findCurrencyList(companyId);
		List<Map<String,String>> listMap=new ArrayList<Map<String,String>>();
		Map<String,String> currMap=null;
		for(Currency curr:currencyLists){
			currMap=new HashMap<String,String>();
			currMap.put("currencyId", curr.getId().toString());
			currMap.put("currencyName", curr.getCurrencyName());
			currMap.put("currencyMark", curr.getCurrencyMark());
			currMap.put("je", "0");
			listMap.add(currMap);
		}
		JSONArray bzJson = JSONArray.fromObject(listMap);
		model.addAttribute("bzList", listMap);
		model.addAttribute("bzJson",bzJson);// 所有币种信息
		//多币种生成json串 前台计算用
		model.addAttribute("reviewDataList", rdlist);
		model.addAttribute("reviewDataProcessMap", processMap);
		model.addAttribute("userName", productOrder.getCreateBy().getId());
		model.addAttribute("orderCompany", productOrder.getOrderCompanyName());
		model.addAttribute("orderStatusStr", OrderCommonUtil.getChineseOrderType(productOrder.getOrderStatus().toString()));
		model.addAttribute("payModeStr", OrderCommonUtil.getStringPayMode(productOrder.getPayMode()));
		model.addAttribute("flowType", Context.REVIEW_FLOWTYPE_BORROWMONEY);
		model.addAttribute("orderId", orderId);
		model.addAttribute("productGroup",productGroup);
		model.addAttribute("product", product);
		model.addAttribute("productOrder", productOrder);
		return "review/privilege/singlegroup/singleGroupPrivilegeDetail";
	}
	
	
	
	/**
	 *取消优惠申请  
	 *songyang
	 *2016年1月21日 15:32:22
	 */
	@ResponseBody
	@RequestMapping(value = "cancelPrivilegePrice")
	public String cancelPrivilegePrice( HttpServletRequest request) {
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
}
