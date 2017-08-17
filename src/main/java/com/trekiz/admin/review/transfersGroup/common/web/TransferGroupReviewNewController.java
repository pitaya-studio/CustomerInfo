package com.trekiz.admin.review.transfersGroup.common.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.quauq.review.core.support.ReviewResult;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.service.IActivityGroupService;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.order.service.OrderCommonService;
import com.trekiz.admin.modules.order.util.OrderCommonUtil;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.transfersGroup.common.service.TransferGroupReviewNewService;

/**
 * 新转团审批
 * @author yakun.bai
 * @Date 2015-12-7
 */
@Controller
@RequestMapping(value = "${adminPath}/newTransferGroupReview")
public class TransferGroupReviewNewController extends BaseController {
	
	@Autowired
	private TransferGroupReviewNewService transferGroupReviewService;
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
	
	/** 转团审批列表地址 */
	private static final String LIST_PAGE = "/review/transferGroup/common/transferGroupReviewList";

	
	/**
	 * @Description 查询转团审核记录
	 * @author yakun.bai
	 * @Date 2015-12-7
	 */
	@RequestMapping(value ="list")
	public String list(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		//查询条件
        Map<String, String> conditionsMap = Maps.newHashMap();
        
        //参数处理：去除空格和处理特殊字符并传递到后台
        //参数解释：团号、产品类型、渠道商、申请开始时间、申请结束时间、申请人、计调、审核状态、游客、排序、切换框
        String paras = "groupCode,productType,agentId,applyDateFrom,applyDateTo,applyPerson,operator,reviewStatus,travelerName,orderBy,tabStatus,paymentType";
        OrderCommonUtil.handlePara(paras, conditionsMap, model, request);
        
        //转团审核记录查询
        Page<Map<String, Object>> pagePara = new Page<Map<String, Object>>(request, response);
		Page<Map<String, Object>> page = transferGroupReviewService.getTransferGroupReviewList(pagePara, conditionsMap);
		
		//值传递
		model.addAttribute("conditionsMap", conditionsMap);
		model.addAttribute("page", page);
		return LIST_PAGE;
	}
	
	/**
	 * @Description 审核通过或驳回
	 * @author yakun.bai
	 * @Date 2015-12-7
	 */
	@ResponseBody
	@RequestMapping(value = "/transferGroupReview")
	public Map<String, String> transferGroupReview(@RequestParam("reviewId") String reviewId, @RequestParam("result") String strResult,
			Model model, HttpServletRequest request) {

		//驳回原因
		String denyReason = request.getParameter("denyReason");
		//转团审核
		Map<String, String> result;
		try {
			result = transferGroupReviewService.transferGroupReview(reviewId, strResult, denyReason, request);
		} catch (Exception e) {
			result = Maps.newHashMap();
			e.printStackTrace();
			result.put("result", "error");
			result.put("msg", e.getMessage());
			return result;
		}
		
		return result;
	}
	
	/**
	 * @Description 撤销审核
	 * @author yakun.bai
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
	 * @Description 批量审核
	 * @author yakun.bai
	 * @Date 2015-12-9
	 */
	@ResponseBody
	@RequestMapping(value = "/batchReview")
	public Map<String, String> batchReview(HttpServletRequest request) {

		Map<String, String> result = Maps.newHashMap();
		
		// 审核ID(ID@订单类型)
		String revids = request.getParameter("revids");
		// 备注
		String remark = request.getParameter("remark");
		// 审核通过或驳回标识
		String flag = request.getParameter("result");
		
		// 校验为空
		if (StringUtils.isNotBlank(revids) && StringUtils.isNotBlank(flag)) {
			try {
				result = transferGroupReviewService.batchReview(revids, remark, flag, request);
			} catch (Exception e) {
				e.printStackTrace();
				result.put("result", "error");
				result.put("msg", e.getMessage());
			}
		} else {
			result.put("result", "error");
			result.put("msg", "参数错误");
		}
		
		return result;
	}
}