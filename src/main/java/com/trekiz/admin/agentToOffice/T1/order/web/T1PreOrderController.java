package com.trekiz.admin.agentToOffice.T1.order.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.agentToOffice.T1.order.service.T1PreOrderService;
import com.trekiz.admin.common.web.BaseController;

/**
 * 预报名
 * @author yakun.bai
 * @Date 2016-10-12
 */
@Controller
@RequestMapping(value = "${adminPath}/t1/preOrder/manage")
public class T1PreOrderController extends BaseController {
    
    protected static final Logger logger = LoggerFactory.getLogger(T1PreOrderController.class);

    @Autowired
    private T1PreOrderService preOrderService;
    
	/**
	 * 获取销售列表
	 * @author yakun.bai
	 * @Date 2016-10-14
	 */
	@RequestMapping(value ="getCompanySaler")
	@ResponseBody
	public Object getCompanySaler(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return preOrderService.getCompanySaler(request, response);
	}
	
	/**
	 * 订单详情（包括下单时接口和下单后订单详情）
	 * @author yakun.bai
	 * @Date 2016-10-14
	 */
	@RequestMapping(value ="t1OrderDetail")
	@ResponseBody
	public Object t1OrderDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return preOrderService.getOrderDetail(request, response);
	}
	
	/**
	 * 订单保存
	 * @author yakun.bai
	 * @Date 2016-10-14
	 */
	@RequestMapping(value ="saveOrder")
	@ResponseBody
	public Object saveOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return preOrderService.saveOrder(request, response);
	}
	
	/**
	 * T1订单页面跳转
	 * @author yakun.bai
	 * @Date 2016-10-19
	 */
	@RequestMapping(value ="showT1OrderList")
	public String showT1OrderList(Model model) throws Exception {
        model.addAttribute("ctxs", 1);
		return "/agentToOffice/T1/order/orderRecord";
	}
	
	/**
	 * 获取T1订单列表搜索初始数据
	 * @author yakun.bai
	 * @Date 2016-10-14
	 */
	@RequestMapping(value ="getCompany")
	@ResponseBody
	public Object getCompany(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return preOrderService.getCompany(request, response);
	}
   
	
	/**
	 * 订单查询：T1平台
	 * @author yakun.bai
	 * @Date 2016-10-19
	 */
	@RequestMapping(value ="showOrderList")
	@ResponseBody
	public Object showOrderList(HttpServletResponse response, HttpServletRequest request) throws Exception {
		return preOrderService.showOrderList(response, request);
	}
	

	/**
	 * T2订单页面跳转
	 * @author yakun.bai
	 * @Date 2016-10-19
	 */
	@RequestMapping(value ="showT2OrderList")
	public String showT2OrderList() throws Exception {
		return "/agentToOffice/T2/preOrderList"; 
	}
	
	/**
	 * 获取T2搜索信息
	 * @author yakun.bai
	 * @Date 2016-10-14
	 */
	@RequestMapping(value ="getSearchInfo")
	@ResponseBody
	public Object getSearchInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return preOrderService.getSearchInfo(request, response);
	}
	
	/**
	 * 取消订单
	 * @author yakun.bai
	 * @Date 2016-10-14
	 */
	@RequestMapping(value ="cancleOrder")
	@ResponseBody
	public Object cancleOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return preOrderService.cancleOrder(request, response);
	}
	
	/**
	 * 删除订单
	 * @author yakun.bai
	 * @Date 2016-10-14
	 */
	@RequestMapping(value ="deleteOrder")
	@ResponseBody
	public Object deleteOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return preOrderService.deleteOrder(request, response);
	}
	
	/**
	 * 修改订单是否已查看状态
	 * @author yakun.bai
	 * @Date 2016-10-14
	 */
    @RequestMapping(value = "changeNotSeenOrderFlag")
    @ResponseBody
    public Object changeNotSeenOrderFlag(HttpServletRequest request, HttpServletResponse response) {
    	return preOrderService.changeNotSeenOrderFlag(request, response);
    }
	
	/**
	 * T2订单详情页面跳转
	 * @author yakun.bai
	 * @Date 2016-10-21
	 */
	@RequestMapping(value ="showT2OrderDetail")
	public String showOrderDetail() throws Exception {
		return "/agentToOffice/T2/showT2OrderDetail";
	}
	
	/**
	 * 预定订单价格校验
	 * @author yakun.bai
	 * @Date 2016-12-22
	 */
	@RequestMapping(value ="validatePrice")
	@ResponseBody
	public Object validatePrice(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return preOrderService.validatePrice(request, response);
	}
	
	/**
	 * 预定订单校验
	 * @author yakun.bai
	 * @Date 2016-10-21
	 */
	@RequestMapping(value ="validateBookOrder")
	@ResponseBody
	public Object validateBookOrder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return preOrderService.validateBookOrder(request, response);
	}
	
	/**
	 * 获取渠道名称
	 * @author yakun.bai
	 * @Date 2016-10-25
	 */
	@RequestMapping(value ="getAgentName")
	@ResponseBody
	public Object getAgentName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return preOrderService.getAgentName(request, response);
	}
}

	