package com.trekiz.admin.common.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;

/**
 * Servlet implementation class OrderNumGenServlet
 */
public class OrderNumGenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderNumGenServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext servletContext = this.getServletContext();
		WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
		SysIncreaseService sysIncreaseService = (SysIncreaseService) ctx.getBean("sysIncreaseService");
		ITravelActivityService travelActivityService = (ITravelActivityService) ctx.getBean("travelActivityService");
		String activityId = request.getParameter("activityId");
		if (activityId != null) {
			TravelActivity travelActivity = travelActivityService.findById(Long.parseLong(activityId));
			if(travelActivity != null){
				String orderNum = sysIncreaseService
						.updateSysIncrease(
								travelActivity.getProCompanyName().length() > 3 ? travelActivity.getProCompanyName().substring(0, 3) : travelActivity.getProCompanyName(),
								travelActivity.getProCompany(), null,
								Context.ORDER_NUM_TYPE);
				response.getWriter().write(orderNum);
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
