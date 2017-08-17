package com.trekiz.admin.modules.review.deposittowarrantreview.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.ActivityGroup;
import com.trekiz.admin.modules.activity.service.ActivityGroupService;
import com.trekiz.admin.modules.review.common.ReviewCommonService;
import com.trekiz.admin.modules.review.deposittowarrantreview.service.DepositToWarrantReviewService;
import com.trekiz.admin.modules.reviewflow.service.ReviewService;
import com.trekiz.admin.modules.sys.entity.UserJob;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.entity.VisaOrder;
import com.trekiz.admin.modules.visa.entity.VisaProducts;
import com.trekiz.admin.modules.visa.service.VisaOrderService;
import com.trekiz.admin.modules.visa.service.VisaProductsService;

/**
 * 押金转担保审核Controller
 * @author chen.jia
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/review/deposit")
public class DepositToWarrantReviewController extends BaseController{

	@Autowired
	private ReviewService reviewService;
	@Autowired
	private DepositToWarrantReviewService depositToWarrantReviewService;
	@Autowired
	private ReviewCommonService reviewCommonService;
	@Autowired
	private VisaOrderService visaOrderService;
	@Autowired
	private ActivityGroupService activityGroupService;
	@Autowired
	private VisaProductsService visaProductsService;
	
	@RequestMapping(value={"reviewList",""})
	public String list(@RequestParam(value="reviewStatus", required=false) String reviewStatus, 
			HttpServletRequest request, HttpServletResponse response, Model model) {
		
		//根据筛选条件查找当前用户权限列表
		Map<String, String> paramMap = new HashMap<String, String>();
		
		paramMap.put("p.groupCode", request.getParameter("groupCode"));
		paramMap.put("o.agentinfo_id", request.getParameter("agent"));
		paramMap.put("o.create_by", request.getParameter("saler"));
		paramMap.put("p.createBy", request.getParameter("op"));
		paramMap.put("createByName", request.getParameter("createByName"));
		String orderCreateDateStart = request.getParameter("orderCreateDateStart") == null ? "" : request.getParameter("orderCreateDateStart");
		String orderCreateDateEnd = request.getParameter("orderCreateDateEnd") == null ? "" : request.getParameter("orderCreateDateEnd");
		paramMap.put("orderCreateDateStart", orderCreateDateStart);
		paramMap.put("orderCreateDateEnd", orderCreateDateEnd);
		
		String jobId = request.getParameter("jobId");
		//获取用户对于当前审批流程的审核职位
		List<UserJob> userJobList = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_DEPOSITTOWARRANT);
		
		if(StringUtils.isBlank(jobId) && !userJobList.isEmpty()) {
			jobId = userJobList.get(userJobList.size()-1).getId().toString();
		}
		//查找属于当前用户审批的审批流
		Page<Map<String, Object>> page = depositToWarrantReviewService.findReviewInfoPage(
				new Page<Map<String, Object>>(request, response), jobId, reviewStatus, paramMap, Context.REVIEW_FLOWTYPE_DEPOSITTOWARRANT);
	
		model.addAttribute("userJobList", userJobList);
		model.addAttribute("page", page);
		
		model.addAttribute("jobId", jobId);
		model.addAttribute("paramMap", paramMap);  //paramMap['createByName']
		List<Map<String, Object>> createByNameList = visaOrderService.findVisaOrderCreateBy1();
		model.addAttribute("createByList", createByNameList);
		
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		
		return "modules/review/depositToWarrantReviewList";
	}
	
	//财务审核模块下签证押金转担保审核
	@RequestMapping(value={"financeList"})
	public String financeList(@RequestParam(value="reviewStatus", required=false) String reviewStatus, 
			HttpServletRequest request, HttpServletResponse response, Model model) {
		
		//根据筛选条件查找当前用户权限列表
		Map<String, String> paramMap = new HashMap<String, String>();

		paramMap.put("p.groupCode", request.getParameter("groupCode"));
		paramMap.put("o.agentinfo_id", request.getParameter("agent"));
		paramMap.put("o.create_by", request.getParameter("saler"));
		paramMap.put("p.createBy", request.getParameter("op"));
		paramMap.put("createByName", request.getParameter("createByName"));
		//游客姓名
		paramMap.put("travelerName", request.getParameter("travelerName"));
		//报批日期
		String reviewCreateDateStart = request.getParameter("reviewCreateDateStart") == null ? "" : request.getParameter("reviewCreateDateStart");
		String reviewCreateDateEnd = request.getParameter("reviewCreateDateEnd") == null ? "" : request.getParameter("reviewCreateDateEnd");
		paramMap.put("reviewCreateDateStart", reviewCreateDateStart);
		paramMap.put("reviewCreateDateEnd", reviewCreateDateEnd);
		paramMap.put("paymentType", request.getParameter("paymentType"));//渠道结算方式
		String jobId = request.getParameter("jobId");
		//获取用户对于当前审批流程的审核职位
		List<UserJob> userJobList = reviewCommonService.getWorkFlowJobByFlowType(Context.REVIEW_FLOWTYPE_DEPOSITTOWARRANT);
		
		if(StringUtils.isBlank(jobId) && !userJobList.isEmpty()) {
			jobId = userJobList.get(userJobList.size()-1).getId().toString();
		}
		//查找属于当前用户审批的审批流
		Page<Map<String, Object>> page = depositToWarrantReviewService.findReviewInfoPage(
				new Page<Map<String, Object>>(request, response), jobId, reviewStatus, paramMap, Context.REVIEW_FLOWTYPE_DEPOSITTOWARRANT);
	
		if (null==reviewStatus) {
			reviewStatus = "1";
		}
		paramMap.put("reviewStatus", reviewStatus);
		
		model.addAttribute("userJobList", userJobList);
		model.addAttribute("page", page);
		
		model.addAttribute("jobId", jobId);
		model.addAttribute("paramMap", paramMap);
		List<Map<String, Object>> createByNameList = visaOrderService.findVisaOrderCreateBy1();
		model.addAttribute("createByList", createByNameList);
		
		return "modules/review/depositToWarrantFinanceList";
	}
	
	@RequestMapping("reviewForm")
	public String reviewForm(@RequestParam(value="reviewId", required=true)String reviewId, HttpServletRequest request, Model model) {
		
		Map<String, String> reviewMap = reviewService.findReview(StringUtils.toLong(reviewId));
		String proId = reviewMap.get("orderId");
		if(StringUtils.isNotBlank(proId)) {
			VisaOrder visaOrder = visaOrderService.findVisaOrder(StringUtils.toLong(proId));
			VisaProducts visaProdcut = null;
			if(visaOrder.getVisaProductId() != null) {
				visaProdcut = visaProductsService.findByVisaProductsId(visaOrder.getVisaProductId());
			}
			
			model.addAttribute("visaOrder", visaOrder);
			model.addAttribute("visaProdcut", visaProdcut);
		}
		model.addAttribute("shenfen", request.getParameter("shenfen"));
		model.addAttribute("reviewMap", reviewMap);
		model.addAttribute("nowLevel",request.getParameter("nowLevel"));
		return "modules/review/depositToWarrantReviewForm";
	}
	

	@RequestMapping("reviewDetail")
	public String reviewDetail(@RequestParam(value="reviewId", required=true)String reviewId, HttpServletRequest request, Model model) {
		
		Map<String, String> reviewMap = reviewService.findReview(StringUtils.toLong(reviewId));
		String proId = reviewMap.get("orderId");
		if(StringUtils.isNotBlank(proId)) {
			VisaOrder visaOrder = visaOrderService.findVisaOrder(StringUtils.toLong(proId));
			ActivityGroup activityGroup = null;
			if(visaOrder.getGroupId() != null) {
				activityGroup = activityGroupService.findById(visaOrder.getGroupId());
			}
			
			model.addAttribute("visaOrder", visaOrder);
			model.addAttribute("activityGroup", activityGroup);
		}
		model.addAttribute("reviewMap", reviewMap);
		model.addAttribute("rid", reviewId);
		
		return "modules/visa/depositToWarrantReviewDetail";
	}
	
	@RequestMapping("reviewDispose")
	public String reviewDispose(@RequestParam(value="result", required=true)String result,
			HttpServletRequest request) {
		
		String reviewId = request.getParameter("reviewId");
		String nowLevel = request.getParameter("nowLevel");
		String denyReason = request.getParameter("denyReason");
		String shenfen = request.getParameter("shenfen");
		
		int reviewResult = reviewService.UpdateReview(StringUtils.toLong(reviewId), StringUtils.toInteger(nowLevel), 
				StringUtils.toInteger(result), denyReason);
		
		Map<String,String> map = reviewService.findReview(StringUtils.toLong(reviewId));
		//审核成功状态
		//if(nowLevel.equals(map.get("topLevel").toString()) && reviewResult == 1){
		if(reviewResult == 1){
			try {
				depositToWarrantReviewService.updateWarrantType(map.get("travelerId"), map.get("warrantType"));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//shenhe:跳转到审核模块下，caiwu:跳转到财务审核模块下
		if("shenhe".equals(shenfen)){
			return "redirect:"+Global.getAdminPath()+"/review/deposit";
		}else{
			return "redirect:"+Global.getAdminPath()+"/review/deposit/financeList";
		}
	}
	
	
	
	//------------------------审核退回----------------------------
	
	/**
	 * 审核回退：根据revid进行批次回退操作,即返回上一级审核
	 * 规则如下：
	 * 1.第一层级 和 最后一层级没有退回操作
	 * 2.隔级审核后不能再进行退回操作
	 * 3.只有审核中状态的才可能进行退回操作
	 * wangxinwei 2015年06月01日21:23:00
	 */
	@RequestMapping(value = "depositToWarrantReviewCancelAjax")
	@ResponseBody
	public Map<String, Object> depositToWarrantReviewCancelAjax(Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();

		String revid = request.getParameter("revid");
		try {
			
			//审核回退
		    if(null!=revid) {
		    	reviewService.CancelReview(Long.parseLong(revid));
			}
				
		}catch (Exception e) {
			map.put("result",2);
			e.printStackTrace();
			return map;
		}	
		map.put("result",1);//1成功   2为申请失败
		
		return map;
	}
	
	
	
	//---------------押金转担保  选择多项同时审核  开始  wxw added -------------------
	/**
	 * wxw added 2015-08-14
	 * 签证押金转担保    选择多项同时审核
	 * @param model
	 * @param request 
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "multiReviewDepositToWarrant")
	public Object multiReviewDepositToWarrant(Model model,HttpServletRequest request, HttpServletResponse response) {
		
        String result =  request.getParameter("result"); //1审核通过，0，驳回
		String remarks = request.getParameter("remarks");//批量审核驳回原因
		/**
		 * 参数结构：
		 * 1@110,1@112
		 * 说明：(当前审核层级@审核id,当前审核层级@审核id,......)
		 */
		String revids = request.getParameter("revids");
		String[] levelandrevids = revids.split(",");	
		
		
		for (int i = 0; i < levelandrevids.length; i++) {
			StringBuffer reply = new StringBuffer();
			if (result == null || "".equals(result)) {
				reply.append("审批结果不能为空");
			}
			
			String nowLevel = levelandrevids[i].split("@")[0];
			String revid = levelandrevids[i].split("@")[1];
			
			int reviewResult = reviewService.UpdateReview(StringUtils.toLong(revid), StringUtils.toInteger(nowLevel), 
					StringUtils.toInteger(result), remarks);
			
			Map<String,String> map = reviewService.findReview(StringUtils.toLong(revid));
			//审核成功状态
			if(nowLevel.equals(map.get("topLevel").toString()) && reviewResult == 1){
				try {
					depositToWarrantReviewService.updateWarrantType(map.get("travelerId"), map.get("warrantType"));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}
		
		 Map<String, Object> resultMap = new HashMap<String, Object>();
		 resultMap.put("msg", "操作成功！");
		 return resultMap;
		
    }
	
	//---------------押金转担保   选择多项同时审核  结束-------------------
	
	
	
	
}
