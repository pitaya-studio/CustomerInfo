package com.trekiz.admin.modules.order.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.order.pojo.PayInfoDetail;
import com.trekiz.admin.modules.order.service.RefundService;
import com.trekiz.admin.modules.pay.entity.PayFee;
import com.trekiz.admin.modules.pay.service.PayFeeService;
@Controller
@RequestMapping(value="${adminPath}/refund/manager")
public class RefundManagerController extends BaseController {

	@Autowired
	RefundService refundService;
	
	@Autowired
	private PayFeeService payFeeService;
	
	@RequestMapping(value="cancelPayInfo",method = RequestMethod.GET)
	public Object  cancelPayInfo(HttpServletRequest request,HttpServletResponse response,Model model){
		String refundId = request.getParameter("refundId");
		String flag = request.getParameter("flag");
		String orderType = request.getParameter("orderType");
		String payType = request.getParameter("payType");//新增payType参数 2 代表付款
		PayInfoDetail payInfoDetail = refundService.getPayInfoByPayId(refundId,orderType);
		model.addAttribute("payInfoDetail", payInfoDetail);
		// 付款
		if ("2".equals(payType)) { //撤销新增 手续费 by chy 2016年1月11日13:44:01
			List<PayFee> payFees = payFeeService.findByRefundId(refundId);
			model.addAttribute("payFees", payFees);
		}
		String url="";
		if("edit".equals(flag)){
		    url= "modules/order/list/cannelPayInfo";
		} 
		if("view".equals(flag)){
			url= "modules/order/list/viewRefundInfo";
		}
		return url;
	}
	
	
	/**
	 * 成本付款支付记录撤销操作
	 * 
	 * */
	@ResponseBody
	@RequestMapping(value="undoRefundPayInfo",method=RequestMethod.GET)
	public Object undoRefundPayInfo(HttpServletRequest request,HttpServletResponse response,Model model){
		Map<String, String> data = new HashMap<String,String>();
		String refundId = request.getParameter("refundId");
		String recordId = request.getParameter("recordId");
		Boolean succ= refundService.undoOp(refundId, recordId);
		if(succ){
			data.put("flag", "ok");
		}else{
			data.put("flag", "false");
		}
		return data;
	}

}
