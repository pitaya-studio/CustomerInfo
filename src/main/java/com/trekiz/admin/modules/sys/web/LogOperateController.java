package com.trekiz.admin.modules.sys.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.sys.entity.LogOperate;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.LogOperateService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 操作日志查看
 * @author wangxk
 *
 */
@Controller
@RequestMapping(value="${adminPath}/modules/logoperate")
public class LogOperateController {

	@Autowired
	private LogOperateService logOpeService;
	@Autowired
	private OfficeService officeService;
	@Autowired
	private SystemService systemService;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value="logQueryList")
	public String logQueryList(@ModelAttribute LogOperate logOperate,HttpServletRequest request,HttpServletResponse response,Model model){
		
		String modular_id = request.getParameter("modular_id");
		String ope_type = request.getParameter("opetype");
		String ope_loginname = request.getParameter("opeloginname");
		String beginDate = request.getParameter("beginDate");
		String endDate = request.getParameter("endDate");
		String opecomid = request.getParameter("opecomid");//批发商即操作人所属公司ID，即批发商ID 在这个表中sysofficeid
		logOperate.setModular_id(modular_id);
		logOperate.setOpe_type(ope_type);
		logOperate.setOpe_loginname(ope_loginname);
		
		String logOpeIds = request.getParameter("logOpeIds");
		User user = UserUtils.getUser();//获取权限的问题
		
		Office office = user.getCompany();//登陆人所在的公司
		// 显示模块项,从数据字典中查询出来，存到里面，不查询的话，只能写死了
		Map morMap = new HashMap();//模块
		morMap.put(Context.log_type_product, Context.log_type_product_name);
		morMap.put(Context.log_type_control, Context.log_type_control_name);
		morMap.put(Context.log_type_schedule, Context.log_type_schedule_name);
		morMap.put(Context.log_type_orderform, Context.log_type_orderform_name);
		morMap.put(Context.log_type_financial, Context.log_type_financial_name);
		morMap.put(Context.log_type_price, Context.log_type_price_name);
		morMap.put(Context.log_type_examine, Context.log_type_examine_name);//"审核"
		morMap.put(Context.log_type_notice, Context.log_type_notice_name);//"公告"
		//操作项
		Map<String,String> typeMap = new HashMap<String,String>();
		typeMap.put("1", "新增");
		typeMap.put("2", "修改");
		typeMap.put("3", "删除");
		//批发商: sys_office
		List <Office> officelist= officeService.findSyncOffice();
		/*Map merMap = new HashMap();
		merMap.put("1", "环球行");
		merMap.put("2", "恶风行");*/
		//操作人  id    loginname
		/*Map opeMap = new HashMap();
		opeMap.put("1", "大刀王五");
		opeMap.put("2", "李四");*/
		//查询出所有的登陆名,感觉不是太对，根据公司进行查询
		/**
		 * 查询所有批发商的用户名
		 */
		List<User> userlist = new ArrayList();
		if("superadmin".equals(user.getLoginName())){//查询所有的公司
			userlist = systemService.getAllUser();
			//System.out.println((userlist.get(0)).[4]);
			
		}else{
			userlist = systemService.getUserByCompany(office);
		}
		
		
		/*if(opecomid!=null&&!"".equals(opecomid)){
			logOperate.setOpe_comid(Long.valueOf(opecomid));
		}*/
		Map<String,Object> conditionsMap = new HashMap<String,Object>();

		String orderModelarIdSort = request.getParameter("orderModelarIdSort"); // 创建日期
		String orderModelarIdCss  = request.getParameter("orderModelarIdCss");// 创建日期排序标识
		String orderOpeLoginameSort = request.getParameter("orderOpeLoginameSort");// 更新日期排序标识
		String orderOpeLoginameCss  = request.getParameter("orderOpeLoginameCss");//订单创建日期排序标识
		String orderCreateDateSort = request.getParameter("orderCreateDateSort");//订单更新日期排序标识
		String orderCreateDateCss  = request.getParameter("orderCreateDateCss");// 创建日期排序标识
		String orderOpeComIdSort = request.getParameter("orderOpeComIdSort");// 更新日期排序标识
		String orderOpeComIdCss  = request.getParameter("orderOpeComIdCss");//订单创建日期排序标识
		
		conditionsMap.put("orderModelarIdSort", orderModelarIdSort);
		conditionsMap.put("orderModelarIdCss", orderModelarIdCss);
		conditionsMap.put("orderOpeLoginameSort", orderOpeLoginameSort);
		conditionsMap.put("orderOpeLoginameCss", orderOpeLoginameCss);
		conditionsMap.put("orderCreateDateSort", orderCreateDateSort);
		conditionsMap.put("orderCreateDateCss", orderCreateDateCss);
		conditionsMap.put("orderOpeComIdSort", orderOpeComIdSort);
		conditionsMap.put("orderOpeComIdCss", orderOpeComIdCss);
		//conditionsMap.put("search", search);
		if(StringUtils.isNotBlank(logOpeIds)) {
			model.addAttribute("logOpeIds", logOpeIds);
		}else{
			model.addAttribute("logOpeIds", "");
		}
		Page<Map<String,Object>> page = logOpeService.findLogOperate(request, response, conditionsMap, logOperate, beginDate, endDate, opecomid, null);
		model.addAttribute("page", page);
		model.addAttribute("morMap", morMap);
		model.addAttribute("typeMap", typeMap);
		model.addAttribute("officelist", officelist);
		model.addAttribute("userlist", userlist);
		model.addAttribute("opeloginname", ope_loginname);
		model.addAttribute("modular_id", modular_id);
		model.addAttribute("opetype", ope_type);
		model.addAttribute("beginDate", beginDate);
		model.addAttribute("endDate", endDate);
		model.addAttribute("opecomid", opecomid);
		model.addAttribute("conditionsMap",conditionsMap);
		return "modules/logoperate/logOperateList";
	}
	//查看删除明细
	@RequestMapping(value="queryLogOperateById")
	public String queryLogOperateById(@ModelAttribute LogOperate logOperate,HttpServletRequest request,HttpServletResponse response,Model model){
		//User user = UserUtils.getUser();//获取权限的问题
		// 显示模块项,从数据字典中查询出来，存到里面，不查询的话，只能写死了
		String id = request.getParameter("id");
		String flag = request.getParameter("flag");
//		System.out.println("id: " + id + "   flag: " + flag);
		LogOperate logope = logOpeService.queryLogOperateById(id);
		model.addAttribute("logope", logope);
		if("1".equals(flag)){//新增
			
			return "modules/logoperate/addLogOperate";
		}
		if("2".equals(flag)){//修改
			return "modules/logoperate/upLogOperate";
		}
		//删除
		
		return "modules/logoperate/delLogOperate";
	}
	/*//查看修改明细
	@RequestMapping(value="queryUpLogDetail")
	public String queryUpLogDetail(@ModelAttribute LogOperate logOperate,HttpServletRequest request,HttpServletResponse response,Model model){
		User user = UserUtils.getUser();//获取权限的问题
		// 显示模块项,从数据字典中查询出来，存到里面，不查询的话，只能写死了
		return "modules/logoperate/upLogOperate";
		
	}
	//查看增加明细
	@RequestMapping(value="queryAddLogDetail")
	public String queryAddLogDetail(@ModelAttribute LogOperate logOperate,HttpServletRequest request,HttpServletResponse response,Model model){
		User user = UserUtils.getUser();//获取权限的问题
		// 显示模块项,从数据字典中查询出来，存到里面，不查询的话，只能写死了
		
		return "modules/logoperate/addLogOperate";
	}*/
}
