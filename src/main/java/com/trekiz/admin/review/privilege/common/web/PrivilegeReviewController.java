package com.trekiz.admin.review.privilege.common.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.quauq.review.core.engine.ReviewService;
import com.quauq.review.core.engine.entity.ReviewNew;
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.service.TravelerService;
import com.trekiz.admin.review.privilege.common.service.PrivilegeReviewService;

/**
 * 优惠审批
 * @author xu.wang
 */
@Controller
@RequestMapping(value = "${adminPath}/privilegeReview")
public class PrivilegeReviewController extends BaseController {
	
	@Autowired
    private OrderCommonService orderService;
	@Autowired
	@Qualifier("travelActivitySyncService")
    private ITravelActivityService travelActivityService;
	@Autowired
	@Qualifier("activityGroupSyncService")
    private IActivityGroupService activityGroupService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private PrivilegeReviewService privilegeReviewService; 
	@Autowired
	private CurrencyService currencyService;
	@Autowired
	private  TravelerService travelerService;

	/** 优惠审批列表地址 */
	private static final String LIST_PAGE = "/review/privilege/common/privilegeReviewList";
	/** 优惠审批地址 */
	private static final String REVIEW_PAGE = "/review/privilege/common/privilegeRebatesReview";
	
	/**
	 * @Description 查询优惠审批记录
	 * @author xu.wang
	 * @Date 2016-01-21
	 */
	@RequestMapping(value ="list")
	public String list(Model model, HttpServletRequest request, HttpServletResponse response) {
		//查询条件
        Map<String, String> conditionsMap = Maps.newHashMap();
        //参数处理：去除空格和处理特殊字符并传递到后台
        //参数解释：团号、产品类型、渠道商、申请开始时间、申请结束时间、申请人、计调、审批状态、优惠开始金额、优惠结束金额、排序、切换框
        String paras = "groupCode,productType,agentId,applyDateFrom,applyDateTo,applyPerson," +
        			   "reviewStatus,rebatesDiffBegin,rebatesDiffEnd,payStatus,orderBy,tabStatus";
        OrderCommonUtil.handlePara(paras, conditionsMap, model, request);
        //优惠审批记录查询
        Page<Map<String, Object>> pagePara = new Page<Map<String, Object>>(request, response);
		Page<Map<String, Object>> page = privilegeReviewService.getPrivilegeReviewList(pagePara, conditionsMap);
		//值传递
		model.addAttribute("conditionsMap", conditionsMap);
		model.addAttribute("page", page);
		return LIST_PAGE;
	}
	
	/**
	 * 优惠审批页
	 */
	@RequestMapping(value = "/show")
	public String show(@RequestParam("reviewId") String reviewId,Model model) {
			privilegeDetail(reviewId, model);
			return REVIEW_PAGE;
	}
	
	/**
	 * @Description 优惠审批信息
	 * @author xu.wang
	 * @Date 2015-12-5
	 */
	private void privilegeDetail(String reviewId, Model model) {
		ReviewNew reviewNew = reviewService.getReview(reviewId);
		Long orderId = Long.valueOf(reviewNew.getOrderId());
		Long companyId = UserUtils.getUser().getCompany().getId();
		ProductOrderCommon productOrder = orderService.getProductorderById(orderId);
		TravelActivity product = travelActivityService.findById(productOrder.getProductId());
		ActivityGroup productGroup = activityGroupService.findById(productOrder.getProductGroupId());
		//处理rivew表中的业务数据
		List<Map<String,Object>> rdlist = new ArrayList<Map<String,Object>>();
		Map<String,Object> processMap = reviewService.getReviewDetailMapByReviewId(reviewId);
		if(null!=processMap.get("travellerId")){
			String[] travellerId =  processMap.get("travellerId").toString().split(",");
			String[] travellerName =  processMap.get("travellerName").toString().split(",");
			String[] travelerType =  processMap.get("travelerType").toString().split(",");
			String[] currencyId =  processMap.get("currencyId").toString().split(",");
			String[] thPrice =  processMap.get("thPrice").toString().split(",");
			String[] thjsjinputprice =  processMap.get("thjsjinputprice").toString().split(",");
			//可输入
			String[] sqyhPrice =  processMap.get("sqyhPrice").toString().split(",");
			String[] privilegeReason =  processMap.get("privilegeReason").toString().split(",");
			for (int i = 0; i < travellerId.length; i++) {
				Map<String,Object>  rdMap = new  HashMap<String, Object>();
				rdMap.put("travellerName", travellerName[i]);
				rdMap.put("travelerType", travelerType[i]);
				rdMap.put("currencyId", currencyId[i]);
				rdMap.put("thPrice", thPrice[i]);
				rdMap.put("thjsjinputprice", thjsjinputprice[i]);
				rdMap.put("inpuryhprice", travelerService.findTravelerById(Long.parseLong(travellerId[i])).getOrgDiscountPrice());
				//可输入
				rdMap.put("sqyhPrice", sqyhPrice[i]);
				rdMap.put("privilegeReason", privilegeReason[i].split("@_@#_=").length>1?privilegeReason[i].split("@_@#_=")[1].toString():"");
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
		model.addAttribute("rebates", reviewNew);
		model.addAttribute("rid", reviewId);
	}
	
	
	/**
	 * @Description 审批通过或驳回
	 * @author xu.wang
	 * @Date 2015-12-5
	 */
	@ResponseBody
	@RequestMapping(value = "/review")
	public Map<String, String> review(@RequestParam("reviewId") String reviewId, @RequestParam("result") String strResult
			, HttpServletRequest request) {
		//原因
		String denyReason = request.getParameter("denyReason");
		//审批
		Map<String, String> result = privilegeReviewService.privilegeReview(reviewId, strResult, denyReason, request);

		return result;
	}
	
	/**
	 * @Description 撤销审批
	 * @author xu.wang
	 * @Date 2015-12-5
	 */
	@ResponseBody
	@RequestMapping(value = "/backReview/{reviewId}")
	public Map<String, Object> backReview(@PathVariable String reviewId) {

		Map<String, Object> result = Maps.newHashMap();
		String companyId = UserUtils.getUser().getCompany().getUuid();
		ReviewResult reviewResult = reviewService.back(UserUtils.getUser().getId().toString(), companyId, null, reviewId, null, null);
		if (reviewResult.getSuccess()) {
			result.put("result", "success");
		} else {
			result.put("result", "error");
			result.put("msg", reviewResult.getMessage());
		}
		return result;
	}
	
	/**
	 * @Description 批量审批
	 * @author xu.wang
	 * @Date 2015-12-9
	 */
	@ResponseBody
	@RequestMapping(value = "/batchReview")
	public Map<String, String> batchReview(HttpServletRequest request) {

		Map<String, String> result = Maps.newHashMap();
		// 审批ID(ID@订单类型)
		String revids = request.getParameter("revids");
		// 备注
		String remark = request.getParameter("remark");
		// 审批通过或驳回标识
		String flag = request.getParameter("result");
		// 校验为空
		if (StringUtils.isNotBlank(revids) && StringUtils.isNotBlank(flag)) {
			result = privilegeReviewService.batchReview(revids, remark, flag, request);
		} else {
			result.put("result", "error");
			result.put("msg", "参数错误");
		}
		return result;
	}
	
}