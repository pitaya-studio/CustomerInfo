package com.trekiz.admin.review.payment.comment.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.common.entity.ResponseJson;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.review.payment.comment.entity.PaymentParam;
import com.trekiz.admin.review.payment.comment.exception.PaymentException;
import com.trekiz.admin.review.payment.comment.service.ICommonPaymentReviewService;

/**
 * 
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 *
 * 财务模块，成本付款，付款审批的Controller
 * @author shijun.liu
 * @date 2015年11月17日
 */
@Controller
@RequestMapping(value = "${adminPath}/review/payment/web")
public class CommonPaymentReviewController extends BaseController {
	
	private static final Log LOG = LogFactory.getLog(CommonPaymentReviewController.class);
	
	@Autowired
	private ICommonPaymentReviewService commonPaymentReviewService;
	@Autowired
	private AgentinfoService agentinfoService;
	
	/**
	 * 审批-成本付款审批列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author shijun.liu
	 * @date 2015.11.17
	 */
	@RequestMapping(value="getPaymentReviewList")
	public String getPaymentReviewList(@ModelAttribute("paymentParam") PaymentParam paymentParam, Model model,
									HttpServletRequest request, HttpServletResponse response){
		if(null != paymentParam && StringUtils.isBlank(paymentParam.getTabStatus())){
			paymentParam.setTabStatus("1");	//默认为待本人审批
		}
		//出团日期单独设置，因为签证机票无团期，所以在后面查询中需要置空
		model.addAttribute("groupOpenDateBegin", paymentParam.getGroupOpenDateBegin());
		model.addAttribute("groupOpenDateEnd", paymentParam.getGroupOpenDateEnd());
		try{
			Page<Map<String,Object>> page = commonPaymentReviewService.getPaymentReviewList(new Page<Map<String,Object>>(request, response),
					paymentParam);
			model.addAttribute("page", page);
			//公司UUID 懿洋假期：f5c8969ee6b845bcbeb5c2b40bac3a23
			String currentCompanyId = UserUtils.getUser().getCompany().getUuid();
			model.addAttribute("CompanyId", currentCompanyId);
			//团队类型
			model.addAttribute("orderTypes", DictUtils.getDictList("order_type"));
			//地接社
			model.addAttribute("supplierList", UserUtils.getSupplierInfoList("supplierName", ""));
			//渠道商
			model.addAttribute("agentList", agentinfoService.findAllAgentinfo(UserUtils.getUser().getCompany().getId()));
			//审批发起人
			List<Map<String, Object>> reviewerList = UserUtils.getSalers(UserUtils.getUser().getCompany().getId());
			model.addAttribute("reviewerList", reviewerList);
		}catch(Exception e){
			e.printStackTrace();
		}
		return "review/payment/paymenRreviewList";
	}
	
	/**
	 * 批量审批，也适合单个审批
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @author shijun.liu
	 */
	@RequestMapping(value="batchApprove")
	public void batchApprove(Model model, HttpServletRequest request, HttpServletResponse response){
		ResponseJson json = new ResponseJson();
		json.setFlag(true);
		StringBuffer str = new StringBuffer();
		String items = request.getParameter("items");		//review_new 表的UUID，用逗号分隔
		String comment = request.getParameter("comment"); 	//审批通过备注
		if(null != comment && comment.length() > 600){
			LOG.error("备注长度过长，超过数据库字段所容纳的长度" + comment);
			comment = comment.substring(0, 600);
		}
		if(StringUtils.isBlank(items)){
			LOG.error("items 参数不能为空");
			str.append("items 参数不能为空");
		}else{
			String[] item = items.split(",");
			for (int i = 0; i < item.length; i++) {
				if(StringUtils.isBlank(item[i])){
					LOG.error("reviewId 存在空值，请检查");
					str.append("reviewId 存在空值，请检查");
				}
				try {
					commonPaymentReviewService.batchApprove(item[i], comment);
				} catch (PaymentException e) {
					e.printStackTrace();
					LOG.error(e.getMessage(), e);
					str.append(e.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
					LOG.error(e.getMessage(), e);
					str.append(e.getMessage());
				}
			}
		}
		if(!"".equals(str.toString())){
			json.setFlag(false);
			LOG.error(str.toString());
			json.setMsg(str.toString());
		}
		ServletUtil.print(response, JSONObject.toJSONString(json));
	}
	
	/**
	 * 批量驳回，也适合单个驳回
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @author shijun.liu
	 */
	@RequestMapping(value="batchReject")
	public void batchReject(Model model, HttpServletRequest request, HttpServletResponse response){
		ResponseJson json = new ResponseJson();
		json.setFlag(true);
		StringBuffer str = new StringBuffer();
		String items = request.getParameter("items");		//review_new 表的UUID，用逗号分隔
		String comment = request.getParameter("comment"); 	//审批通过备注
		if(null != comment && comment.length() > 600){
			LOG.error("备注长度过长，超过数据库字段所容纳的长度" + comment);
			comment = comment.substring(0, 600);
		}
		if(StringUtils.isBlank(items)){
			LOG.error("items 参数不能为空");
			str.append("items 参数不能为空");
		}else{
			String[] item = items.split(",");
			for (int i = 0; i < item.length; i++) {
				if(StringUtils.isBlank(item[i])){
					LOG.error("reviewId 存在空值，请检查");
					str.append("reviewId 存在空值，请检查");
				}
				try {
					commonPaymentReviewService.batchReject(item[i], comment);
				} catch (PaymentException e) {
					e.printStackTrace();
					LOG.error(e.getMessage(), e);
					str.append(e.getMessage());
				} catch (Exception e) {
					e.printStackTrace();
					LOG.error(e.getMessage(), e);
					str.append(e.getMessage());
				}
			}
		}
		if(!"".equals(str.toString())){
			json.setFlag(false);
			LOG.error(str.toString());
			json.setMsg(str.toString());
		}
		ServletUtil.print(response, JSONObject.toJSONString(json));
	}
	
	/**
	 * 撤销审批
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @author shijun.liu
	 */
	@RequestMapping(value="backReview")
	public void backReview(Model model, HttpServletRequest request, HttpServletResponse response){
		ResponseJson json = new ResponseJson();
		json.setFlag(true);
		StringBuffer str = new StringBuffer();
		String reviewId = request.getParameter("reviewId");	//review_new 表的UUID
		String comment = request.getParameter("comment"); 	//审批通过备注
		if(null != comment && comment.length() > 600){
			LOG.error("备注长度过长，超过数据库字段所容纳的长度" + comment);
			comment = comment.substring(0, 600);
		}
		if(StringUtils.isBlank(reviewId)){
			LOG.error("items 参数不能为空");
			str.append("items 参数不能为空");
		}else{
			try {
				commonPaymentReviewService.backReview(reviewId, comment);
			} catch (PaymentException e) {
				e.printStackTrace();
				LOG.error(e.getMessage(), e);
				str.append(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error(e.getMessage(), e);
				str.append(e.getMessage());
			}
		}
		if(!"".equals(str.toString())){
			json.setFlag(false);
			LOG.error(str.toString());
			json.setMsg(str.toString());
		}
		ServletUtil.print(response, JSONObject.toJSONString(json));
	}
	
	/**
	 * 取消审批
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @author shijun.liu
	 */
	@RequestMapping(value="cancelReview")
	public void cancelReview(Model model, HttpServletRequest request, HttpServletResponse response){
		ResponseJson json = new ResponseJson();
		json.setFlag(true);
		StringBuffer str = new StringBuffer();
		String reviewId = request.getParameter("reviewId");	//review_new 表的UUID
		if(StringUtils.isBlank(reviewId)){
			LOG.error("items 参数不能为空");
			str.append("items 参数不能为空");
		}else{
			try {
				commonPaymentReviewService.cancelReview(reviewId, "");//取消备注暂时为空，需要时再加上
			} catch (PaymentException e) {
				e.printStackTrace();
				LOG.error(e.getMessage(), e);
				str.append(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
				LOG.error(e.getMessage(), e);
				str.append(e.getMessage());
			}
		}
		if(!"".equals(str.toString())){
			json.setFlag(false);
			LOG.error(str.toString());
			json.setMsg(str.toString());
		}
		ServletUtil.print(response, JSONObject.toJSONString(json));
	}
}
