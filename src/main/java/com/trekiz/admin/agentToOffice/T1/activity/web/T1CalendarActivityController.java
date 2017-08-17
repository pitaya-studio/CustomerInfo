package com.trekiz.admin.agentToOffice.T1.activity.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.agentToOffice.T1.activity.service.T1CalendarActivityService;
import com.trekiz.admin.common.web.BaseController;

/**
 * T1日历控件
 * @author yakun.bai
 * @Date 2016-11-22
 */
@Controller
@RequestMapping(value = "${adminPath}/t1/activity/calendar")
public class T1CalendarActivityController extends BaseController {
	
	@Autowired
    private T1CalendarActivityService calendarActivityService;
	

	/**
	 * 获取产品下团期信息
	 * @return
	 * @author yakun.bai
	 * @Date 2016-11-22
	 */
	@ResponseBody
	@RequestMapping(value = "/getActivityGroupInfo")
	public Object getActivityGroupInfo(HttpServletRequest request) {
		return calendarActivityService.getActivityGroupInfo(request);
	}
	
	/**
	 * 获取产品下团期信息
	 * @return
	 * @author yakun.bai
	 * @Date 2016-11-22
	 */
	@ResponseBody
	@RequestMapping(value = "/getGroupInfo")
	public Object getGroupInfo(HttpServletRequest request) {
		return calendarActivityService.getGroupInfo(request);
	}
}
