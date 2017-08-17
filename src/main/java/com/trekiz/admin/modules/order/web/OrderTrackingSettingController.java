package com.trekiz.admin.modules.order.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.order.entity.OrderTrackingSetting;
import com.trekiz.admin.modules.order.service.OrderTrackingSettingService;
 
/**
 * 订单跟踪设置表
 * @author yakun.bai
 * @Date 2016-8-12
 */
@Controller
@RequestMapping(value = "${adminPath}/orderTrackingSetting/manage")
public class OrderTrackingSettingController extends BaseController {
    
    /** 订单跟踪设置列表地址 */
	private static final String LIST_PAGE = "/modules/order/orderTrackingSetting/orderTrackingSettingList";
	
	@Autowired
	private OrderTrackingSettingService orderTrackingSettingService;
   
   
	/**
	 * 订单跟踪设置列表
	 * @author yakun.bai
	 * @Date 2016-8-12
	 */
	@RequestMapping(value ="list")
	public String showOrderList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		Page<OrderTrackingSetting> page = new Page<OrderTrackingSetting>(request, response);
		orderTrackingSettingService.find(page);
		model.addAttribute("page", page); 
		return LIST_PAGE;
	}	
	
	@ResponseBody
	@RequestMapping(value ="saveSetting")
	public Object saveSetting(Model model, HttpServletRequest request) {
	    String settingId = request.getParameter("settingId");
	    String settingType = request.getParameter("settingType");
	    String lightTimeType = request.getParameter("lightTimeType");
	    String greenEndTime = request.getParameter("greenEndTime");
	    String yellowStartTime = request.getParameter("yellowStartTime");
	    String yellowEndTime = request.getParameter("yellowEndTime");
	    String redStartTime = request.getParameter("redStartTime");
	    
	    if (StringUtils.isBlank(settingId) || StringUtils.isBlank(settingType) || StringUtils.isBlank(lightTimeType) 
	    		|| StringUtils.isBlank(greenEndTime) || StringUtils.isBlank(yellowStartTime) 
	    		|| StringUtils.isBlank(yellowEndTime) || StringUtils.isBlank(redStartTime) ) {
	    	return "error";
	    } else {
	    	OrderTrackingSetting orderTrackingSetting = orderTrackingSettingService.findById(Long.parseLong(settingId));
	    	if (orderTrackingSetting != null) {
	    		orderTrackingSetting.setSettingType(Integer.parseInt(settingType));
	    		orderTrackingSetting.setGreenLightTimeType(Integer.parseInt(lightTimeType));
	    		orderTrackingSetting.setGreenLightTimeStart(0);
	    		orderTrackingSetting.setGreenLightTimeEnd(Integer.parseInt(greenEndTime));
	    		orderTrackingSetting.setYellowLightTimeType(Integer.parseInt(lightTimeType));
	    		orderTrackingSetting.setYellowLightTimeStart(Integer.parseInt(yellowStartTime));
	    		orderTrackingSetting.setYellowLightTimeEnd(Integer.parseInt(yellowEndTime));
	    		orderTrackingSetting.setRedLightTimeType(Integer.parseInt(lightTimeType));
	    		orderTrackingSetting.setRedLightTimeStart(Integer.parseInt(redStartTime));
	    		orderTrackingSetting.setRedLightTimeEnd(null);
	    		orderTrackingSettingService.save(orderTrackingSetting);
	    	}
	    }
	    return "success";
	}
}