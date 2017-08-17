package com.trekiz.admin.review.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quauq.review.core.engine.ReviewService;
import com.trekiz.admin.common.utils.StringUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.common.entity.ResponseJson;
import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.review.common.exception.CommonReviewException;
import com.trekiz.admin.review.common.service.ICommonReviewService;
import com.trekiz.admin.review.rebates.singleGroup.service.RebatesNewService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 *
 * 新审批公共Controller
 * @author shijun.liu
 * @date 2015年11月30日
 */
@Controller
@RequestMapping(value="${adminPath}/review/common/web")
public class CommonReviewController {

	@Autowired
	private ICommonReviewService commonReviewService;
	@Autowired
	private RebatesNewService rebatesNewService;
	@Autowired
	private ReviewService reviewNewService;
	
	@RequestMapping(value="confimOrCancelPay")
	public void confimOrCancelPay(HttpServletRequest request, HttpServletResponse response, Model model){
		ResponseJson json = new ResponseJson();
		json.setFlag(true);//默认操作执行成功
		String reviewId = request.getParameter("reviewId");
		String status = request.getParameter("status");
		String rate = request.getParameter("rate");
		String flag = request.getParameter("flag");
		String price = request.getParameter("price");
		//出纳确认时间可以进行编辑
		String payConfirmDateStr = request.getParameter("cashierConfirmDate");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			//出纳确认时间
			Date payConfirmDate = StringUtils.isBlank(payConfirmDateStr) ?  null : sdf.parse(payConfirmDateStr);
			commonReviewService.confimOrCancelPay(reviewId, status,payConfirmDate);

			if(rate != null) {
				if("202".equals(flag)) {
					Long rebatesId = Long.parseLong(request.getParameter("rebatesId"));
					rebatesNewService.updateRebateRate(reviewId, rate, price, rebatesId);//返佣
				}else if("201".equals(flag)) {
					rebatesNewService.updateRefundRate(reviewId, rate, price);//退款
				}
			}

		} catch (CommonReviewException e) {
			e.printStackTrace();
			json.setFlag(false);
			json.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			json.setFlag(false);
			json.setMsg(e.getMessage());
		}
		String jsonStr = JSONObject.toJSONString(json);
		ServletUtil.print(response, jsonStr);
	}

	@RequestMapping(value="confimOrCancelPay2")
	public void confimOrCancelPay2(HttpServletRequest request, HttpServletResponse response, Model model){

		ResponseJson json = new ResponseJson();
		try {
			String uuid = request.getParameter("uuid");
			String payConfirmDateStr = request.getParameter("payConfirmDate");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date payConfirmDate = sdf.parse(payConfirmDateStr); //出纳确认时间
			if(StringUtils.isNotBlank(uuid)) {
				commonReviewService.confimOrCancelPay(uuid, "1",payConfirmDate);
			}
			
			JSONArray jsonArray = new JSONArray(request.getParameter("datas"));			
			for(int i = 0; i < jsonArray.length(); i++) {
				org.json.JSONObject jsonObject = jsonArray.getJSONObject(i);
//				String uuid = jsonObject.get("uuid").toString();
				String rate = jsonObject.get("rate").toString();
				String price = jsonObject.get("price").toString();
				String rebatesId = jsonObject.get("rebatesId").toString();
/*				String payConfirmDateStr = jsonObject.get("payConfirmDate").toString();
				if (StringUtils.isNotBlank(payConfirmDateStr)) {
					payConfirmDate = sdf.parse(payConfirmDateStr);
				}*/

//				commonReviewService.confimOrCancelPay(uuid, "1",payConfirmDate);
				
				String[] rebatesIds = rebatesId.split(",");
				for(String rId : rebatesIds) {
					rebatesNewService.updateRebateRate(uuid, rate, price, Long.parseLong(rId));
				}				
			}
		} catch (CommonReviewException e) {
			e.printStackTrace();
			json.setFlag(false);
			json.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			json.setFlag(false);
			json.setMsg(e.getMessage());
		}
		String jsonStr = JSONObject.toJSONString(json);
		ServletUtil.print(response, jsonStr);
	}
	// 针对奢华之旅   成本付款和退款付款的发票状态的修改    BY  jinxin.gao
	@RequestMapping(value="confimOrCancelInvoice")
	public void confimOrCancelInvoice(HttpServletRequest request, HttpServletResponse response, Model model){
		ResponseJson json = new ResponseJson();
		json.setFlag(true);//默认操作执行成功
		String reviewId = request.getParameter("reviewId");
		String status = request.getParameter("status");
		try {
			if(reviewId.contains(",")){
				// 批量确认
				String[] reviewIds = reviewId.split(",");
				for (String review_Id : reviewIds) {
					commonReviewService.confimOrCancelInvoiceAll(review_Id.split("_")[0]);
				}
			}else{
				// 单条的确认与取消
				commonReviewService.confimOrCancelInvoice(reviewId, status);
			}
		} catch (CommonReviewException e) {
			e.printStackTrace();
			json.setFlag(false);
			json.setMsg(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			json.setFlag(false);
			json.setMsg(e.getMessage());
		}
		String jsonStr = JSONObject.toJSONString(json);
		ServletUtil.print(response, jsonStr);
	}

	/**
	 * 使用ReviewNew的扩展字段extend4来保存是否在付款列表页面的出纳确认列显示 备注 图标。548,549需求。
	 * @param reviewUuid 新审批的uuid
	 * @return 不返回状态了，不使用
     */
	@RequestMapping(value = "/cancelShowRemark")
	public void cancelShowRemarks(String reviewUuid){
		Map<String,String> map = new HashMap<>();
		map.put("extend4","1"); // 1表示取消显示备注，null或者0表示正常显示。
		reviewNewService.updateExtendValues(reviewUuid,map);
	}
}
