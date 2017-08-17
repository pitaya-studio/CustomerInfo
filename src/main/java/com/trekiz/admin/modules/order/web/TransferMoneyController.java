package com.trekiz.admin.modules.order.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.order.entity.ProductOrderCommon;
import com.trekiz.admin.modules.order.entity.TransferMoneyApplyForm;
import com.trekiz.admin.modules.order.repository.ProductOrderCommonDao;
import com.trekiz.admin.modules.order.service.OrderReviewService;
import com.trekiz.admin.modules.order.service.TransferMoneyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
@Controller
@RequestMapping(value = "${adminPath}/orderCommon/transferMoney")
public class TransferMoneyController extends BaseController {
	
	@Autowired
	private TransferMoneyService transferMoneyService;
	
	@Autowired
    private ProductOrderCommonDao productorderDao;
	@Autowired
	private OrderReviewService orderReviewService;
	/**
	 * 转款申请页面跳转
	 * @param orderId
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="transfersMoneyHref/{orderId}")
	public String transfersMoneyHref(@PathVariable String orderId,  Model model, HttpServletRequest request,HttpServletResponse response) {
		Map<String, String> condition = new HashMap<String, String>();
		condition.put("orderId", orderId);
		ProductOrderCommon order = productorderDao.findOne(Long.parseLong(orderId));
		Page<Map<String,Object>> page = transferMoneyService.getTransferMoneyAppList(request,response,UserUtils.getUser(),condition);
		String pageStr = page.toString();
		pageStr = pageStr.replace("<div style=\"clear:both;\"></div>", "");
		model.addAttribute("pageStr", pageStr);
		model.addAttribute("pageStr", pageStr);
		model.addAttribute("page", page);
		model.addAttribute("order", order);
		model.addAttribute("orderId",orderId);
	    return "modules/order/transferMoney/transferMoneyList";
	}
	
	
	@RequestMapping(value ="transfersMoneyApply/{orderId}")
	public String transfersMoneyApply(@PathVariable String orderId,  Model model, HttpServletRequest request) {
		Map<String, String> condition = new HashMap<String, String>();
		condition.put("orderId", orderId);
		Map<String, Object> resultMap = transferMoneyService.getResultMap(condition);
		model.addAttribute("orderId",orderId);
		model.addAllAttributes(resultMap);
	    return "modules/order/transferMoney/transferMoneyApply";
	}
	
	
	/**
	 * 异步提交转款申请 
	 * @author yue.wang
	 * @时间 2014年12月29日
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value ="transfersMoneyApplySub"  , method=RequestMethod.POST)
	public Map<String,String> transfersMoneyApplySub(@Valid TransferMoneyApplyForm appForm,  Model model, HttpServletRequest request) {

		String orderId = appForm.getOrderId();
		ProductOrderCommon order = productorderDao.findOne(Long.parseLong(orderId));
		Integer[] travolerIds = appForm.getTravelorId();
		String[] travolerNames = appForm.getTravelorName();
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap.put("orderId", appForm.getOrderId());
		////////////////////////////////////////////////////////////////
		/**
		 * add by ruyi.chen 
		 * add date 2014-04-13
		 * 转款申请增加流程互斥判断
		 */
		//组装申请转款判断游客Map
		Map<Long,String> travelerMap = Maps.newHashMap();
		List<Long> travelerIds = Lists.newArrayList();
		if(travolerIds.length > 0){
			for(int i =0;i<travolerIds.length;i++){
				travelerMap.put(travolerIds[i].longValue(), travolerNames[i]);
				travelerIds.add(travolerIds[i].longValue());
			}
			
		}
		Map<String,Object> rMap = orderReviewService.getOrderReviewMutexInfo(Long.parseLong(orderId), order.getOrderStatus().toString(), Context.REVIEW_FLOWTYPE_TRANSFER_MONEY, travelerIds);
		@SuppressWarnings("unchecked")
		Map<String,Object> travelerResultMap = (Map<String,Object>) rMap.get(Context.MUTEX_RESULT_lIST);
		boolean flag = false;
		StringBuffer sf = new StringBuffer();
		for(Long tid : travelerIds){
			if("1".equals(travelerResultMap.get(tid.toString()).toString().split("/")[0])){
				flag = true;
				sf.append(travelerMap.get(tid.toString())+" "+travelerResultMap.get(tid.toString()).toString().split("/")[2]+" ");
			}
		}
		if(flag){
			resultMap.put("res", sf.toString());
			return resultMap	;
		}
		//流程互斥部分结束
		////////////////////////////////////////////////////////////////
		else{
			resultMap = transferMoneyService.transfersMoneyApplySub(appForm);
			
		    return  resultMap;
		}
		
	}
	
	
	/**
	 * 异步提交取消申请
	 * @author yue.wang
	 * @时间 2014年12月30日
	 * @param result
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value ="transfersMoneyCancel/{reviewId}"  , method=RequestMethod.POST)
	public Map<String, String> transfersMoneyCancel(@PathVariable String reviewId, Model model, HttpServletRequest request) {
		Map<String, String> condition = new HashMap<String, String>();
		condition.put("reviewId", reviewId);
		Map<String, String> resultMap = transferMoneyService.transfersMoneyCancel(condition);
	    return  resultMap;
	}
	
	/**
	 * 异步提交取消申请
	 * @author yue.wang
	 * @时间 2014年12月30日
	 * @param result
	 * @return
	 */
	@RequestMapping(value ="transferMoneyDetails/{reviewId}")
	public String transferMoneyDetails(@PathVariable Long reviewId,  Model model, HttpServletRequest request) {
		Map<String, Long> condition = new HashMap<String, Long>();
		condition.put("reviewId", reviewId);
		Map<String, Object> baseInfoMap = transferMoneyService.transferMoneyDetails(condition);
		Map<String, Object> reviewInfoMap = transferMoneyService.transferMoneyReviewInfo(condition);
		model.addAllAttributes(baseInfoMap);
		model.addAllAttributes(reviewInfoMap);
	    return "modules/order/transferMoney/transferMoneyDetials";
	}
	

}
