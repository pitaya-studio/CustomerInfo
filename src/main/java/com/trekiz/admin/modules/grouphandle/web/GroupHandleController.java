package com.trekiz.admin.modules.grouphandle.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.grouphandle.form.GroupHandleSearchForm;
import com.trekiz.admin.modules.grouphandle.service.GroupBatchHandleService;
import com.trekiz.admin.modules.grouphandle.service.GroupHandleService;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Dict;
import com.trekiz.admin.modules.sys.entity.Menu;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.visa.service.VisaOrderService;


/**  
 * @Title: GroupHandleController.java
 * @Package com.trekiz.admin.modules.grouphandle.web
 * @Description: TODO(用一句话描述该文件做什么)
 * @author   
 * @date 2016-2016年1月26日 下午3:13:21
 * @version V1.0  
 */
@Controller
@RequestMapping(value = "${adminPath}/grouphandle/")
public class GroupHandleController {
	
	@Autowired
	private GroupHandleService groupHandleService;

	//************autowired-tgy-start***********//
	@Autowired
	private AgentinfoService agentinfoService;

	@Autowired
	private VisaOrderService visaOrderService;
	//************autowired-tgy-end************//
	@Autowired
    private DepartmentService departmentService;
	@Autowired
	private SystemService systemService ;
	@Autowired
	private GroupBatchHandleService groupBatchHandleService;
	

     

	/**
	 * a/visa/interviewNotice/visaInterviewNoticeAddress
	 * @Description: 签务团控列表
	 * @author 
	 * @date 2016年1月26日下午8:10:12
	 * @param model
	 * @param response
	 * @param request
	 * @return    
	 * @throws
	 */
	@RequestMapping(value="grouphandleqwlist")
	public String grouphandleqwlist(Model model,HttpServletResponse response,HttpServletRequest request,GroupHandleSearchForm searchForm){
		
		DepartmentCommon common = setDepartmentPara();
		
		String showlist = searchForm.getShowList();
		showlist = StringUtils.isNoneBlank(showlist)?showlist:"group";
		if ("group".equals(showlist)) {
			Page<Map<String,Object>> pageList = groupHandleService.getGroupHandleList(request, response, searchForm, "", common);
			//团控list
			request.setAttribute("groupHandleList", pageList.getList());
			request.setAttribute("page", pageList);
			
		}else{
			Page<Map<String,Object>> pageList = groupHandleService.searchGroupControlTravelList(request, response, searchForm, "", common);
			//游客列表
			request.setAttribute("travelPageList", pageList.getList());
			request.setAttribute("travelPage", pageList);
		}
		
		//****************************tgy-start*************************//
		
		//获得所有渠道,并将所有渠道放入req中
		//渠道选择  智能匹配
		List<Agentinfo> agentinfoList = agentinfoService.findAllAgentinfo();
		request.setAttribute("agentinfoList", agentinfoList);
		//获得所有签证国家,并将其放入request中
/*		List<Object[]> countryObject = visaProductsService.findCountryInfoList();
		List<Country> countryList = new ArrayList<Country>();
		Country country = null;
		for (Object[] props : countryObject) {
			country = new Country();
			//签证国家id
			country.setId(Long.parseLong(props[0].toString())); 
			//签证国家名称
			country.setCountryName_cn(String.valueOf(props[1]));
			countryList.add(country);
		}
		request.setAttribute("countryList", countryList);*/
		
		List<Country> countryListOld  = CountryUtils.getCountrys();
		List<Country> countryList = new ArrayList<Country>();
		Country countryNew = null;
		for (Country country : countryListOld) {
			countryNew = new Country();
			countryNew.setId(country.getId());
			countryNew.setCountryName_cn(country.getCountryName_cn());
			countryList.add(countryNew);
		}
		model.addAttribute("countryList", countryList);
		
		
		//获得所有签证类型,并将其放入request中
		List<Dict> visaTypeList = visaOrderService.findDictByType("new_visa_type");
		request.setAttribute("visaTypeList", visaTypeList);
		//获得所有签证状态,并将其放入request中
		List<Dict> visaStatusList = visaOrderService.findDictByType("visa_status");
		request.setAttribute("visaStatusList", visaStatusList);
		//****************************tgy-end***************************//
		
		//查询条件是否展开
		request.setAttribute("flag", request.getParameter("showFlag"));
				
		model.addAttribute("searchForm", searchForm);

		 
		return "modules/grouphandle/grouphandleqwlist";
	}
	
	
	/**
	 * 
	 * @Description: 销售团控列表
	 * @author 
	 * @date 2016年1月26日下午8:10:18
	 * @param model
	 * @param response
	 * @param request
	 * @param searchForm
	 * @return    
	 * @throws
	 */
	@RequestMapping(value="grouphandlexslist")
	public String grouphandlexslist(Model model,HttpServletResponse response,HttpServletRequest request,GroupHandleSearchForm searchForm){
		
		DepartmentCommon common = setDepartmentPara();
		
		String showlist = searchForm.getShowList();
		showlist = StringUtils.isNoneBlank(showlist)?showlist:"group";
		if ("group".equals(showlist)) {
			Page<Map<String,Object>> pageList = groupHandleService.getGroupHandleList(request, response, searchForm, "", common);
			//团控list
			request.setAttribute("groupHandleList", pageList.getList());
			request.setAttribute("page", pageList);
			
		}else{
			Page<Map<String,Object>> pageList = groupHandleService.searchGroupControlTravelList(request, response, searchForm, "", common);
			//游客列表
			request.setAttribute("travelPageList", pageList.getList());
			request.setAttribute("travelPage", pageList);
		}
		
		//****************************tgy-start*************************//
		
		//获得所有渠道,并将所有渠道放入req中
		//渠道选择  智能匹配
		List<Agentinfo> agentinfoList = agentinfoService.findAllAgentinfo();
		request.setAttribute("agentinfoList", agentinfoList);
		//获得所有签证国家,并将其放入request中
/*		List<Object[]> countryObject = visaProductsService.findCountryInfoList();
		List<Country> countryList = new ArrayList<Country>();
		Country country = null;
		for (Object[] props : countryObject) {
			country = new Country();
			//签证国家id
			country.setId(Long.parseLong(props[0].toString())); 
			//签证国家名称
			country.setCountryName_cn(String.valueOf(props[1]));
			countryList.add(country);
		}
		request.setAttribute("countryList", countryList);*/
		
		List<Country> countryListOld  = CountryUtils.getCountrys();
		List<Country> countryList = new ArrayList<Country>();
		Country countryNew = null;
		for (Country country : countryListOld) {
			countryNew = new Country();
			countryNew.setId(country.getId());
			countryNew.setCountryName_cn(country.getCountryName_cn());
			countryList.add(countryNew);
		}
		model.addAttribute("countryList", countryList);
		
		
		//获得所有签证类型,并将其放入request中
		List<Dict> visaTypeList = visaOrderService.findDictByType("new_visa_type");
		request.setAttribute("visaTypeList", visaTypeList);
		//获得所有签证状态,并将其放入request中
		List<Dict> visaStatusList = visaOrderService.findDictByType("visa_status");
		request.setAttribute("visaStatusList", visaStatusList);
		//****************************tgy-end***************************//
		
		//查询条件是否展开
		request.setAttribute("flag", request.getParameter("showFlag"));
				
		model.addAttribute("searchForm", searchForm);
		 
		return "modules/grouphandle/grouphandlexslist";
	}
	
	
	/***
	 * 签务团控的订单的批量操作
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value="visagroupBatchEditListNew")
	public String grouphandBatchEditList(GroupHandleSearchForm searchForm ,HttpServletRequest request ,HttpServletResponse response){
		if(searchForm == null){
			searchForm = new GroupHandleSearchForm();
		}
		//部门ID及角色查询
		DepartmentCommon common = setDepartmentPara();
		//批量操作-游客列表
		Page<Map<String, Object>> visaGroup = groupBatchHandleService.visaGroupBatchEditView(request, response ,searchForm,"qianwu",common);
		List<Map<String, Object>> groupList = visaGroup.getList();
		request.setAttribute("visaGroupList", groupList);
		request.setAttribute("page", visaGroup);
		//填充初始数据
		initializationData(request);
		//填充查询条件
		request.setAttribute("searchForm", searchForm);
		
//		System.out.println("visagroupbatch was start!!");
		return "modules/grouphandle/visaGroupBatchEditListView" ;
	}

	/***
	 * 签务签务列表
	 * 批量更新签证状态
	 * type:visa_status 签证状态
	 */
	@ResponseBody
	@RequestMapping(value={"batchUpdateVisaStatus"})
	public Object batchUpdateVisaStatus(HttpServletRequest request){

		//要保存到数据库内的值
		//签证状态
		String visaStatus = request.getParameter("visaStatus");
		//visa表的主键
		String visaIds = request.getParameter("visaIds");

		//异常消息
		Map<String, Object> map = new HashMap<String, Object>();
		if(null == visaIds){
			map.put("message", "参数错误,更新失败");
			return map;
		}else{
			String updateResult =  groupBatchHandleService.batchUpdateVisaStatus(visaIds, visaStatus);
			map.put("message", updateResult);
			return map;
		}
	}
	
	/**签务团控列表
	 * 批量更新护照状态
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value={"batchUpdatePassportStatus"})
	public Object batchUpdatePassportStatus(String passportStatus,String travelerIds){
		Map<String, Object> map = new HashMap<String, Object>();
		if(null == travelerIds || ""==travelerIds){
			map.put("message", "参数错误,更新失败");
			return map;
		}else{
			String updateResult =  groupBatchHandleService.batchUpdatePassportStatus(passportStatus, travelerIds);
			map.put("message", updateResult);
			return map;
		}

	}
	
	/**
	 * 签务团控列表
	 * 批量更新设置时间
	 * @param groupHandleVisa
	 * @param visaIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value={"batchUpdateTime"})
	public Object batchUpdateTime(HttpServletRequest request,String visaIds){
		Map<String ,Object> map = new HashMap<String ,Object>();
		if(null==visaIds||""==visaIds){
			map.put("message", "参数错误，更新失败！");
			return map;
		}else{
			String updateTime;
			try {
				updateTime = groupBatchHandleService.batchUpdateTime(request, visaIds);
				map.put("message", updateTime);
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return map;
		}	
	}
	
	
	@ResponseBody
	@RequestMapping(value={"saveGroupControlVisa"})
	public Object saveGroupControlVisa(HttpServletRequest request,HttpServletResponse response){
		String groupId = request.getParameter("groupId");
		Map<String ,Object> map = new HashMap<String ,Object>();
		if(null==groupId||"".equals(groupId)){
			map.put("message", "参数错误，保存失败！");
			return map;
		}else{
			String groupControlVisa;
		try {
			 groupControlVisa = groupBatchHandleService.saveGroupControlVisa(request);
			map.put("message", groupControlVisa);
			} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			}
		}
		return map;
	}
	
	/**
	 * 保存护照状态
	 * @param passportStatus
	 * @param travelerIds
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value={"savePassportStatus"})
	public Object savePassportStatus(String passportStatus,String travelerIds){
		Map<String, Object> map = new HashMap<String, Object>();
		if(null == travelerIds || ""==travelerIds){
			map.put("message", "参数错误,保存失败");
			return map;
		}else{
			String updateResult =  groupBatchHandleService.saveTravlerPassportStatus(Integer.parseInt(passportStatus), travelerIds);
			map.put("message", updateResult);
			return map;
		}
	}
	/**
	 * @return
	 * 设置部门ID和用户角色
	 */
	private DepartmentCommon setDepartmentPara() {
		//当前登录用户信息
				User user = UserUtils.getUser();
				
				HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
				
				//部门
				DepartmentCommon common = new DepartmentCommon();
				
				//根据URL查询对应权限标识符
				Menu menu = departmentService.getMenuByUrl(request);
				if (menu != null) {
					common.setPermission(menu.getPermission());
				}
				
				//判断当前用户所拥有的角色类型
				List<Role> roleListOrderByDept = user.getRoleListOrderByDept();
				//部门ID
				if(user.getRoleListOrderByDept() != null && user.getRoleListOrderByDept().size()>0){
					Department dept = roleListOrderByDept.get(0).getDepartment();
					if (dept != null) {
						//部门ID
						common.setDepartmentId(dept.getId().toString());
					}
				}
				List<Role> roleList = user.getRoleList();
				List<String> roleTypeList = common.getRoleTypeList();
				if (CollectionUtils.isEmpty(roleTypeList)) {
					roleTypeList = new ArrayList<String>();
				}
				//如果用户所在部门与要显示部门相同，则把对应角色类型放入List
				if (CollectionUtils.isNotEmpty(roleList)) {
					for (Role role : roleList) {
						if (role.getDepartment() != null && role.getDepartment().getId().toString().equals(common.getDepartmentId())) {
							roleTypeList.add(role.getRoleType());
						}
					}
				}
				common.setRoleTypeList(roleTypeList);
				return common;
	}
	
	/**
	 * 填充页面 各下拉列表数据和初始化数据的方法
	 * 
	 * */
	private void initializationData(HttpServletRequest req){
		//签证类型  
		//从sys_dict表中查找type是new_visa_type的字段,初始化到页面的签证类型下拉列表中
		List<Dict> visaTypeList = visaOrderService.findDictByType("new_visa_type");
		
		//签证状态
		//从sys_dict表中查找type是visa_status的字段,初始化到页面的签证状态下拉列表中
		List<Dict> visaStatusList = visaOrderService.findDictByType("visa_status");	
		
		//yun签证国家  智能匹配
//		List<Object[]> countryObject = visaProductsService.findCountryInfoList();
//		List<Country> countryList = new ArrayList<Country>();
//		Country country = null;
//		for (Object[] props : countryObject) {
//			country = new Country();
//			//签证国家id
//			country.setId(Long.parseLong(props[0].toString())); 
//			//签证国家名称
//			country.setCountryName_cn(String.valueOf(props[1]));
//			countryList.add(country);
//		}
		List<Country> countryListOld  = CountryUtils.getCountrys();
		List<Country> countryList = new ArrayList<Country>();
		Country countryNew = null;
		for (Country country : countryListOld) {
			countryNew = new Country();
			//签证国家id
			countryNew.setId(country.getId());
			//签证国家名称
			countryNew.setCountryName_cn(country.getCountryName_cn());
			countryList.add(countryNew);
		}
		//model.addAttribute("countryList", countryList);
		
		//渠道选择  智能匹配
		List<Agentinfo> agentinfoList = agentinfoService.findAllAgentinfo();
		
		
		//所有的币种和币种id
		//List<Currency> currencyList = currencyService.findCurrencyList(UserUtils.getUser().getCompany().getId());
		 
		//req.setAttribute("currencyList", currencyList);
		//当前批发商下的所有销售
		List<User> users = systemService.getUserByCompanyId(UserUtils.getUser().getCompany().getId());
		req.setAttribute("user", users);
		req.setAttribute("agentinfoList", agentinfoList);
		req.setAttribute("countryList", countryList);
		req.setAttribute("visaStatusList", visaStatusList);
		req.setAttribute("visaTypeList", visaTypeList);
		//本公司的所有的下过订单的人
		req.setAttribute("createByList", visaOrderService.findCreateBy());
	}
	
	/**
	 * 根据group_control_visa的id查询游客信息
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value={"getGroupControlVisaInfo"})
	public Map<String,Object> getGroupControlVisaInfo(HttpServletRequest request){
    	String  arrGCVIds=request.getParameter("arrGCVIds");
    	String result="fail";
    	Map<String,Object>resultMap=new HashMap<String,Object>();
		//通过id查询group_control_visa表
		try {
			List<Map<String,Object>> groupHandleVisaList=groupHandleService.getInfoByGCVIds(arrGCVIds);
			resultMap.put("groupHandleVisaList",groupHandleVisaList);
			result="success";
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultMap.put("result",result);
		return resultMap;
	}
	/**
	 * 签务团控订单列表下编辑签证的部分信息
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value={"editPartVisaInfo"})
	public String editPartVisaInfo(HttpServletRequest request){
	   String result="fail";
	   String strGroupControlVisaIdValues=request.getParameter("groupControlVisaIdValues");
	   String strVisaHandleUnitsValues=request.getParameter("visaHandleUnitsValues");
	   String strVisaStatusSelectedValues=request.getParameter("visaStatusSelectedValues");
	   String strSigningTimeValues=request.getParameter("signingTimeValues");
	   String strVisaDeliveryTimeValues=request.getParameter("visaDeliveryTimeValues");
	   String strVisaGotTimeValues=request.getParameter("visaGotTimeValues");
	   String strSupplementaryInfoTimeValues=request.getParameter("supplementaryInfoTimeValues");
       //更新group_control_visa表中的部分信息	   
	   try {
		groupHandleService.updatePartVisaInfo(strGroupControlVisaIdValues,strVisaHandleUnitsValues,
				                                 strVisaStatusSelectedValues,strSigningTimeValues,
				                                 strVisaDeliveryTimeValues,strVisaGotTimeValues,
				                                 strSupplementaryInfoTimeValues);
		result="success";
	} catch (Exception e) {
		e.printStackTrace();
	}
	   return result;
	}	
}
