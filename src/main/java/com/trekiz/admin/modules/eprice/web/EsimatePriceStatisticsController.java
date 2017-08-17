package com.trekiz.admin.modules.eprice.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.eprice.entity.EstimatePriceStatistics;
import com.trekiz.admin.modules.eprice.form.EstimatePriceStatisticsForm;
import com.trekiz.admin.modules.eprice.service.EsimatePriceStatisticsService;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/eprice/manager/statistics")
public class EsimatePriceStatisticsController {

	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private EsimatePriceStatisticsService esimatePriceStatisticsService;
	@Autowired
	@Qualifier("travelActivitySyncService")
	private ITravelActivityService travelActivityService;
	@Autowired
	private AreaService areaService;

	/**
	 * 跳转到询价统计
	 * 
	 * @author gao
	 * @时间 2014年12月03日
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
    @RequestMapping(value = "recordstatistics")
	public String recordStatistics(Model model, HttpServletRequest request,
			HttpServletResponse response) {
		// 获取查询条件
		String beginTime = request.getParameter("groupOpenDate");
		String endTime = request.getParameter("groupCloseDate");
		String deptId = request.getParameter("departmentId");
		String countryId = request.getParameter("countryId");
		String salerId = request.getParameter("salerId");
	
		// 按部门展示
		User user = UserUtils.getUser();
		DepartmentCommon common = departmentService.setDepartmentPara("orderStatistics", model);

		EstimatePriceStatisticsForm epsForm = new EstimatePriceStatisticsForm();
		boolean formbool = false; // 判断前台是否传入参数，如果未传入部门和销售人员参数，则查询当前用户可看到的全部统计
		// 载入页面参数
		if (StringUtils.isNotBlank(beginTime)) {
			epsForm.setBeginTime(beginTime);
		}
		if (StringUtils.isNotBlank(endTime)) {
			epsForm.setEndTime(endTime);
		}
		// 选定分组（部门）
		if (StringUtils.isNotBlank(deptId)) {
			// 判断是否是总部
			if(common.getIsManager()){
				epsForm.setDeptId(null);
			}else{
				epsForm.setDeptId(Integer.valueOf(deptId));
				formbool = true;
			}
		}
		// 线路国家
		List<Integer> cList = new ArrayList<Integer>();
		if (StringUtils.isNotBlank(countryId)) {
			epsForm.setCountryId(Integer.valueOf(countryId));
			cList.add(Integer.valueOf(countryId));
		}
		// 选定销售
		if (StringUtils.isNotBlank(salerId)) {
			epsForm.setSalerId(Integer.valueOf(salerId));
			formbool = true;
		}
		// 页面代码
		model.addAttribute("showType","202");
		
		// 线路国家
				List<Map<String, Object>> countryList =  esimatePriceStatisticsService.filterTreeData(null);
				epsForm.setCountryList(countryList);
				model.addAttribute("countryList", countryList);
				
				model.addAttribute("isParentsAndChildren", common.getIsParentsAndChildren());
				// 判断登录用户不属于任何部门的情况
				if(StringUtils.isBlank(common.getDepartmentId())){
					// 判断该批发商是否下分了部门
					if(common.getShowAreaList()!=null){
						// 如果当前用户是销售，抛出提示信息
						if(common.getIsSale()){
							model.addAttribute("departBoolean", 1);
							model.addAttribute("theUser",user);
							return "modules/eprice/recordStatistics";
						}
					}
				}
				// 全部统计详情列表
				List<Map<String, Object>> statisticsList = new ArrayList<Map<String, Object>>();
				
				// 查询到全部销售,销售列表使用
				List<User> userlist = getSalerUsers(user.getCompany().getId(), common);
				if (!userlist.isEmpty()) {
					// 普通销售看不到销售列表
					model.addAttribute("salerList", userlist);
				}else{
					// 当选定了部门，该部门却没有任何销售时，走这里
						Map<String, Object> statisticMap = new HashMap<String, Object>();
						// 保存该部门的统计结果
						statisticMap.put("mapStatistic", null);
						// 保存该部门的全部销售列表
						statisticMap.put("mapSalers", null);
						// 保存该部门名称
						if(StringUtils.isNotBlank(deptId)){
							statisticMap.put("depName", departmentService.findById(Long.valueOf(deptId)).getName());
						}else{
							statisticMap.put("depName", "询价统计");
						}
						
						statisticsList.add(statisticMap);
						model.addAttribute("statisticsList", statisticsList);
						model.addAttribute("epsForm", epsForm);
						return "modules/eprice/recordStatistics";
				}
				
				

				

				if(formbool){
					if(epsForm.getSalerId()==null && epsForm.getDeptId()!=null){	// 判断 分组不为空，销售为空时的统计结果
					//	DepartmentCommon commonForm = new DepartmentCommon();
						DepartmentCommon commonForm = common;
						//String isParentsAndChildren = "true";
					//	commonForm.setIsParentsAndChildren(Boolean.parseBoolean(isParentsAndChildren));
						commonForm.setDepartmentId(deptId);
						// 获取该分组下的全部销售
						List<User> ulist = getSalerUsers(user.getCompany().getId(), commonForm);
						model.addAttribute("salerList", ulist);
						if(!ulist.isEmpty()){
							List<EstimatePriceStatistics> sta =findStatistic(epsForm,changeSalerList(ulist));
							Map<String, Object> statisticMap = new HashMap<String, Object>();
							// 保存该部门的统计结果
							statisticMap.put("mapStatistic", sta);
							// 保存该部门的全部销售列表
							statisticMap.put("mapSalers", ulist);
							// 保存该部门名称
							statisticMap.put("depName", departmentService.findById(Long.valueOf(deptId)).getName());
							statisticsList.add(statisticMap);
						}else{ // 部门存在，但该部门下没有任何销售
							Map<String, Object> statisticMap = new HashMap<String, Object>();
							// 保存该部门的统计结果
							statisticMap.put("mapStatistic", null);
							// 保存该部门的全部销售列表
							statisticMap.put("mapSalers", null);
							// 保存该部门名称
							statisticMap.put("depName", departmentService.findById(Long.valueOf(deptId)).getName());
							statisticsList.add(statisticMap);
						}
					}else if(epsForm.getSalerId()!=null){	// 判断 分组不为空或为空，销售不为空时的统计结果
						List<User> ulist = new ArrayList<User>();
						User u = UserUtils.getUser(Long.valueOf(epsForm.getSalerId()));
						if(u!=null){
							ulist.add(u);
							
							List<EstimatePriceStatistics> sta =findStatistic(epsForm,changeSalerList(ulist));
							Map<String, Object> statisticMap = new HashMap<String, Object>();
							// 保存该部门的统计结果
							statisticMap.put("mapStatistic", sta);
							// 保存该部门的全部销售列表
							statisticMap.put("mapSalers", ulist);
							// 保存该部门名称
							if(StringUtils.isNotBlank(deptId)){
								statisticMap.put("depName", departmentService.findById(Long.valueOf(deptId)).getName());
							}else{
								// 如果部门为空，需要查询部门
								if(StringUtils.isNotBlank(common.getDepartmentId())){
									statisticMap.put("depName", departmentService.findById(Long.valueOf(common.getDepartmentId())).getName());
								}else{// 如果该批发商没有分任何部门
									statisticMap.put("depName", "询价统计");
								}
								
							}
							statisticsList.add(statisticMap);
						}
					}
				}else{// 如果前端未传入部门和销售人员参数，则查询能找到的全部统计
					// 获取全部部门分组
					Set<Department> depSet = common.getShowAreaList();
					statisticsList = getStatistic(depSet, userlist ,statisticsList, common,user,epsForm);
				}
				
				model.addAttribute("statisticsList", statisticsList);
				model.addAttribute("epsForm", epsForm);

				return "modules/eprice/recordStatistics";
	}

	/**
	 * 根据销售员列表和 查询条件类，获取一个销售组的统计结果
	 * 
	 * @param epsForm
	 * @param salerList
	 */
	private List<EstimatePriceStatistics> findStatistic(
			EstimatePriceStatisticsForm epsForm,
			ArrayList<Map<String, Object>> salerList) {
		List<EstimatePriceStatistics> est = esimatePriceStatisticsService.findList(salerList, epsForm);
		return est;
	}

	/**
	 * 将销售员列表转为指定格式
	 * 
	 * @return
	 */
	private ArrayList<Map<String, Object>> changeSalerList(List<User> userlist) {
		ArrayList<Map<String, Object>> back = new ArrayList<Map<String, Object>>();
		if (userlist.isEmpty()) {
			return back;
		}
		Iterator<User> iter = userlist.iterator();
		while (iter.hasNext()) {
			Map<String, Object> map = new HashMap<String, Object>();
			User user = iter.next();
			map.put("id", user.getId());
			map.put("name", user.getName());
			back.add(map);
		}
		return back;
	}

	/**
	 * 获取当前用户所在部门下所有子部门销售用户
	 * @param companyId
	 * @param departmentId
	 * @return
	 */
	private List<User> getSalerUsers(Long companyId, DepartmentCommon common) {
		String departmentId = common.getDepartmentId();
		List<User> userList = Lists.newArrayList();
		List<Role> roleList = UserUtils.getUser().getRoleList();
		//获取当前用户所在部门下所有人员包括自己部门下人员
		if(StringUtils.isNotBlank(departmentId)) {
			for(Role role : roleList) {
	        	String type = role.getRoleType();
	        	if(Context.ROLE_TYPE_MANAGER.equals(type) || Context.ROLE_TYPE_SALES_EXECUTIVE.equals(type)) {
	        		if(common.getIsParentsAndChildren() || 
	        				(role.getDepartment() != null && role.getDepartment().getId().toString().equals(departmentId))) {
	        			userList = systemService.getAllUserByDepartment(companyId, Long.parseLong(departmentId), "%," + departmentId + ",%");
	        			break;
	        		} 
	        	} else {
	        		boolean isContains = false;
	        		//因为一个销售用户可能同属于两个部门
	        		if(CollectionUtils.isNotEmpty(userList)) {
	        			for(User user : userList) {
	        				if(user.getId() == UserUtils.getUser().getId()) {
	        					isContains = true;
	        					break;
	        				}
	        			}
	        		}
	        		if(!isContains) {
	        			userList.add(UserUtils.getUser());
	        		}
	        	}
			}
		} else {
			//获取部门下销售人员：自己部门和子级部门销售
	    	for(Role role : roleList) {
	        	String type = role.getRoleType();
	        	if(Context.ROLE_TYPE_MANAGER.equals(type) || Context.ROLE_TYPE_SALES_EXECUTIVE.equals(type)) {
	        		userList = systemService.getUserByCompany(UserUtils.getUser().getCompany());
	        		break;
	        	} else {
	        		boolean isContains = false;
	        		//因为一个销售用户可能同属于两个部门
	        		if(CollectionUtils.isNotEmpty(userList)) {
	        			for(User user : userList) {
	        				if((UserUtils.getUser().getId()).equals(user.getId())) {
	        					isContains = true;
	        					break;
	        				}
	        			}
	        		}
	        		if(!isContains) {
	        			userList.add(UserUtils.getUser());
	        		}
	        	}
			}
		}
		//获取部门下销售人员：自己部门和子级部门销售
		if(CollectionUtils.isNotEmpty(userList)) {
			Iterator<User> it = userList.iterator();
			while(it.hasNext()) {
				User user = it.next();
				List<Role> roleLists = user.getRoleList();
				boolean isSaler = false;
				for(Role role : roleLists) {
					if(Context.ROLE_TYPE_SALES.equals(role.getRoleType()) || Context.ROLE_TYPE_SALES_EXECUTIVE.equals(role.getRoleType())) {
						isSaler = true;
						break;
					}
				}
				if(!isSaler) {
					it.remove();
				}
			}
		} 
		return userList;
	}
	
	/**
	 * 当没有前端条件时，统计结果如下
	 * @param depSet
	 * @param userlist
	 * @param statisticsList
	 * @param common
	 * @param user
	 */
	private List<Map<String, Object>> getStatistic(Set<Department> depSet, List<User> userlist ,List<Map<String, Object>> statisticsList, DepartmentCommon common,User user,EstimatePriceStatisticsForm epsForm){
		// 查看当前用户所在部门及其子部门
		if (CollectionUtils.isNotEmpty(depSet) && CollectionUtils.isNotEmpty(userlist)) {
			// 父部门统计
			Department parentDept = departmentService.findById(Long.parseLong(common.getDepartmentId()));
			// 需统计的销售列表
			common.setDepartmentId(parentDept.getId().toString());
			List<User> salers = getSalerUsers(user.getCompany().getId(),common);

			if (!salers.isEmpty()) {
				ArrayList<Map<String, Object>> salerList = changeSalerList(salers);
				Map<String, Object> statisticMap = new HashMap<String, Object>();
				List<EstimatePriceStatistics> est = findStatistic(epsForm,salerList);
				// 保存该部门的统计结果
				statisticMap.put("mapStatistic", est);
				// 保存该部门的全部销售列表
				statisticMap.put("mapSalers", salers);
				// 保存该部门名称
				statisticMap.put("depName", parentDept.getName());
				statisticsList.add(statisticMap);
			}
			// 子部门统计
			if (CollectionUtils.isNotEmpty(parentDept.getChildList())) {
				for (Department dept : parentDept.getChildList()) {
					// 判断该子部门是否在权限内可见
					for(Department d:depSet){
						// depSet 为登录用户可看见的部门（组），需判断与子部门中那些部门相等
						if(d.getId().equals(dept.getId())){
							common.setDepartmentId(dept.getId().toString());
							common.setIsParentsAndChildren(true);
							List<User> salerChilds = getSalerUsers(user.getCompany().getId(), common);
							if (!salerChilds.isEmpty()) {
								ArrayList<Map<String, Object>> salerList = changeSalerList(salerChilds);
								Map<String, Object> statisticMap = new HashMap<String, Object>();
								List<EstimatePriceStatistics> est = findStatistic(epsForm, salerList);
								// 保存该部门的统计结果
								statisticMap.put("mapStatistic", est);
								// 保存该部门的全部销售列表
								statisticMap.put("mapSalers", salerChilds);
								// 保存该部门名称
								statisticMap.put("depName", dept.getName());
								statisticsList.add(statisticMap);
							}
						}
					}
				}
			}
		} else {
			if (StringUtils.isNotBlank(common.getDepartmentId())) {	// 部门ID为空
				Department dept = departmentService.findById(Long.parseLong(common.getDepartmentId()));
				List<User> salers = getSalerUsers(user.getCompany().getId(),common);
				if (!salers.isEmpty()) {
					ArrayList<Map<String, Object>> salerList = changeSalerList(salers);
					Map<String, Object> statisticMap = new HashMap<String, Object>();
					List<EstimatePriceStatistics> est = findStatistic(new EstimatePriceStatisticsForm(),	salerList);
					// 保存该部门的统计结果
					statisticMap.put("mapStatistic", est);
					// 保存该部门的全部销售列表
					statisticMap.put("mapSalers", salers);
					// 保存该部门名称
					statisticMap.put("depName", dept.getName());
					statisticsList.add(statisticMap);
//					if (!est.isEmpty()) {
//						statisticsList.add(statisticMap);
//					}
				}
			}else if(CollectionUtils.isEmpty(depSet) && CollectionUtils.isNotEmpty(userlist)){ // 如果没有部门，且销售不为空
				List<EstimatePriceStatistics> sta =findStatistic(epsForm,changeSalerList(userlist));
				Map<String, Object> statisticMap = new HashMap<String, Object>();
				// 保存该部门的统计结果
				statisticMap.put("mapStatistic", sta);
				// 保存该部门的全部销售列表
				statisticMap.put("mapSalers", userlist);
				// 保存该部门名称
				statisticMap.put("depName", "全部统计");
				statisticsList.add(statisticMap);
			}else{ // 销售为空
				Map<String, Object> statisticMap = new HashMap<String, Object>();
				// 保存该部门的统计结果
				statisticMap.put("mapStatistic", null);
				// 保存该部门的全部销售列表
				statisticMap.put("mapSalers", null);
				// 保存该部门名称
				statisticMap.put("depName", "全部统计");
				statisticsList.add(statisticMap);
			}
		}
		return statisticsList;
	}
//	private List<Map<String, Object>> getStatistic(Set<Department> depSet, List<User> userlist ,List<Map<String, Object>> statisticsList, DepartmentCommon common,User user,EstimatePriceStatisticsForm epsForm){
//		// 查看当前用户所在部门及其子部门
//		if (CollectionUtils.isNotEmpty(depSet) && CollectionUtils.isNotEmpty(userlist)) {
//			Iterator<Department> iter = depSet.iterator();
//			while(iter.hasNext()){
//				Department  dep = iter.next();
//				// 需统计的销售列表
//				common.setDepartmentId(dep.getId().toString());
//				List<User> salers = getSalerUsers(user.getCompany().getId(),common);
//				if (!salers.isEmpty()) {
//					ArrayList<Map<String, Object>> salerList = changeSalerList(salers);
//					Map<String, Object> statisticMap = new HashMap<String, Object>();
//					List<EstimatePriceStatistics> est = findStatistic(epsForm,salerList);
//					// 保存该部门的统计结果
//					statisticMap.put("mapStatistic", est);
//					// 保存该部门的全部销售列表
//					statisticMap.put("mapSalers", salers);
//					// 保存该部门名称
//					statisticMap.put("depName", dep.getName());
//					if (!est.isEmpty()) {
//						statisticsList.add(statisticMap);
//					}
//				}
//			}
//		}
//		return statisticsList;
//	}
	
	
	/**
	 * 查询当前用户的线路国家
	 * @return
	 */
	@SuppressWarnings("unused")
    private List<Map<String,Object>>  getCountryList(){
		//response.setContentType("application/json; charset=UTF-8");
		List<Map<String,Object>> areamapList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> targetAreaIds = Lists.newArrayList();
		List<Long> childAreaIds = Lists.newArrayList();
		List<Area> targetAreas = Lists.newArrayList(); 
		//批发商ID
		Long companyId = UserUtils.getUser().getCompany().getId();	
		targetAreaIds = travelActivityService.findAreaIds(companyId);
		if(targetAreaIds!=null && targetAreaIds.size()!=0){
			for(Map<String, Object> map:targetAreaIds){			
				childAreaIds.add(Long.parseLong(String.valueOf(map.get("id"))));
			}
		}
		List<Long> areaIds = Lists.newArrayList(childAreaIds);
		areaService.appendParentArea(childAreaIds,areaIds,targetAreas);
		
		for(Area area : targetAreas){
			// 过滤后，只留下境外游国家
			String str = area.getParentIds();
			if(str.length()>12){
				String strback = str.substring(4,5);
				if(str!=null && strback!=null && "1".equals(strback)){
					Map<String,Object> areamap = new HashMap<String,Object>();
					areamap.put("id", area.getId());
					areamap.put("name", area.getName());
					areamapList.add(areamap);
				}
			}
			
		}
		return areamapList;
	}
}
