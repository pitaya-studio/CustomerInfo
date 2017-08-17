package com.trekiz.admin.modules.activity.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.service.ActivityReserveOrderService;

/**
 * 
* @ClassName: ActivityReserveOrderController 
* @Description: TODO
* @author kai.xiao 
* @date 2014年12月1日 下午9:12:57 
*
 */
@Controller
@RequestMapping(value = "${adminPath}/activityReserveOrder/manage")
public class ActivityReserveOrderController extends BaseController {

	@Autowired
	private ActivityReserveOrderService activityReserveOrderService;
	/**
	 * 
	* @Title: saveAsAcount 
	* @Description: TODO(切位订金达账功能) 
	* @param @param request
	* @return Object    返回类型 
	* @throws
	 */
	@RequestMapping(value ="saveAsAcount")
	@ResponseBody
	public Object saveAsAcount(HttpServletRequest request){
	    String id = request.getParameter("id");
	    return activityReserveOrderService.saveAsAcount(Long.parseLong(id));
	}
}
