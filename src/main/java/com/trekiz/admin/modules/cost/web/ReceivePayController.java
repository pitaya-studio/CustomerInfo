package com.trekiz.admin.modules.cost.web;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.servlet.ServletUtil;
import com.trekiz.admin.common.utils.DateUtils;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.cost.entity.ParamBean;
import com.trekiz.admin.modules.cost.service.IReceivePayService;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 
 * Copyright 2015 QUAUQ Technology Co. Ltd.
 *
 * 财务模块，应收，应付功能对应的Controller
 * @author shijun.liu
 * @date 2015年11月12日
 */
@Controller
@RequestMapping(value = "${adminPath}/receivepay/manager")
public class ReceivePayController extends BaseController{

	@Autowired IReceivePayService receivePayService;
	private static final Logger log = Logger.getLogger(ReceivePayController.class);
	
	@Autowired
	private AgentinfoService agentinfoService;
	@Autowired
    private DepartmentService departmentService;
	
	/**
	 * 查询应收账款账龄数据信息
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author shijun.liu
	 */
	@RequestMapping(value="getReceive")
	public String getReceive(HttpServletRequest request, HttpServletResponse response, Model model){
		ParamBean paramBean = new ParamBean();
		String groupCode = request.getParameter("groupCode");			//团号
		String orderNum = request.getParameter("orderNum");				//订单编号
		String orderType = request.getParameter("orderType");			//订单类型
		String agentId = request.getParameter("agentId");				//渠道
		String salerId = request.getParameter("salerId");				//销售
		String deptId = request.getParameter("deptId");					//部门
		String operatorId = request.getParameter("operatorId");			//计调
		String badAccount = request.getParameter("badAccount");			//是否坏账
		String paymentType = request.getParameter("paymentType");		//渠道结款方式
		paramBean.setGroupCode(groupCode);
		paramBean.setOrderNum(orderNum);
		paramBean.setBadAccount(badAccount);
		if(StringUtils.isNotBlank(orderType)){
			paramBean.setOrderType(Integer.parseInt(orderType));
		}
		if(StringUtils.isNotBlank(agentId)){
			paramBean.setAgentId(Long.valueOf(agentId));
		}
		if(StringUtils.isNotBlank(salerId)){
			paramBean.setSalerId(Long.valueOf(salerId));
		}
		if(StringUtils.isNotBlank(deptId)){
			paramBean.setDeptId(Long.valueOf(deptId));
		}
		if(StringUtils.isNotBlank(operatorId)){
			paramBean.setOperatorId(Long.valueOf(operatorId));
		}
		if(StringUtils.isNotBlank(paymentType)){
			paramBean.setPaymentType(paymentType);
			model.addAttribute("paymentType", paymentType);
		}
		Page<Map<String,Object>> page = receivePayService.getReceive(new Page<Map<String,Object>>(request, response), paramBean);
		model.addAttribute("page", page);
		model.addAttribute("params", paramBean);
		//团队类型
		model.addAttribute("orderTypes", DictUtils.getDictList("order_type"));
		//下单人
		model.addAttribute("orderPersonList", UserUtils.getSalers(UserUtils.getUser().getCompany().getId()));
		//渠道
        model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
		// 销售
		Map<String,String> salerList = agentinfoService.findInnerSales(UserUtils.getUser().getCompany().getId());
		model.addAttribute("salerList", salerList);
		//部门
        List<Department> departmentList = departmentService.findByOfficeId(UserUtils.getUser().getCompany().getId());
		model.addAttribute("departmentList",departmentList);
		/*获取批发商下所有拥有计调角色人员和发布产品人员*/
		model.addAttribute("agentJd", UserUtils.getOperators(UserUtils.getUser().getCompany().getId()));
		return "modules/cost/receive_account_age";
	}
	
	/**
	 * 查询应收账款账龄数据信息,并将其导出Excel
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author shijun.liu
	 */
	@RequestMapping(value="downloadReceive")
	public void downloadReceive(HttpServletRequest request, HttpServletResponse response, Model model){
		ParamBean paramBean = new ParamBean();
		String groupCode = request.getParameter("groupCode");			//团号
		String orderNum = request.getParameter("orderNum");				//订单编号
		String orderType = request.getParameter("orderType");			//订单类型
		String orderBeginDate = request.getParameter("orderBeginDate");	//下单时间
		String orderEndDate = request.getParameter("orderEndDate");		//下单时间
		String orderedId = request.getParameter("orderedId");			//下单人
		String agentId = request.getParameter("agentId");				//渠道
		String salerId = request.getParameter("salerId");				//销售
		String deptId = request.getParameter("deptId");					//部门
		String operatorId = request.getParameter("operatorId");			//计调
		String badAccount = request.getParameter("badAccount");			//是否坏账
		paramBean.setGroupCode(groupCode);
		paramBean.setOrderNum(orderNum);
		paramBean.setOrderBeginDate(orderBeginDate);
		paramBean.setOrderEndDate(orderEndDate);
		paramBean.setBadAccount(badAccount);
		if(StringUtils.isNotBlank(orderType)){
			paramBean.setOrderType(Integer.parseInt(orderType));
		}
		if(StringUtils.isNotBlank(orderedId)){
			paramBean.setOrderedId(Long.valueOf(orderedId));
		}
		if(StringUtils.isNotBlank(agentId)){
			paramBean.setAgentId(Long.valueOf(agentId));
		}
		if(StringUtils.isNotBlank(salerId)){
			paramBean.setSalerId(Long.valueOf(salerId));
		}
		if(StringUtils.isNotBlank(deptId)){
			paramBean.setDeptId(Long.valueOf(deptId));
		}
		if(StringUtils.isNotBlank(operatorId)){
			paramBean.setOperatorId(Long.valueOf(operatorId));
		}
		Workbook workbook = receivePayService.downloadReceive(new Page<Map<String,Object>>(request, response), paramBean);
		String fileName = "应收账款账龄-" +DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd") + ".xls";
		ServletUtil.downLoadExcel(response, fileName, workbook);
	}
	
	/**
	 * 应付账款列表
	 * @param request
	 * @param response
	 * @param model
	 * @return String
	 * @author zhaohaiming
	 * @date 2015-11-12
	 * */
	@RequestMapping(value="getPayList")
	public String getPayList(HttpServletRequest request,HttpServletResponse response,Model model){
		
        Page<Map<Object,Object>> page = receivePayService.getPayList(new Page<Map<Object, Object>>(request, response), request, response, model);
                
		model.addAttribute("page", page);
		//计调
		model.addAttribute("agentJd", agentinfoService.findAllUsers(UserUtils.getUser().getCompany().getId()));
		//订单类型
		model.addAttribute("orderTypes", DictUtils.getDictList("order_type"));
		//部门
		Date date = new Date();
		model.addAttribute("date", DateUtils.getDate("yyyy-MM-dd"));
		model.addAttribute("dept", departmentService.findByOfficeId(UserUtils.getUser().getCompany().getId()));
		return "modules/order/PayList";
	}
	
	/**
	 * 下载应付账款列表excel
	 * @param request
	 * @param response
	 * @param model
	 * @author zhaohaiming
	 * @date 2015-11-12
	 * */
	@RequestMapping(value="downLoadPayList")
	public void downLoadPayList(HttpServletRequest request,HttpServletResponse response,Model model){
		
		Workbook wb =receivePayService.downloadPayList(request, response, model);
		String fileName = "应付账款-" +DateUtils.formatCustomDate(new Date(), "yyyy-MM-dd") + ".xls";
		ServletUtil.downLoadExcel(response, fileName, wb);
	}
}
