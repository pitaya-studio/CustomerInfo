package com.trekiz.admin.modules.sys.web;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.Menu;
import com.trekiz.admin.modules.sys.entity.SysJobNew;
import com.trekiz.admin.modules.sys.repository.SysJobNewDao;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 
 * @description 部门管理Controller
 *
 * @author baiyakun
 *
 * @create_time 2014-9-15
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/department")
public class DepartmentController extends BaseController {

	@ModelAttribute("departmentId")
	protected Integer getDepartmentId(){
		return 214;
	}
	
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private SystemService systemService;
	@Autowired
	private SysJobNewDao sysJobNewDao;
	
	@ModelAttribute("department")
	public Department get(@RequestParam(required=false) Long id) {
		if (id != null) {
			return departmentService.findById(id);
		} else {
			return new Department();
		}
	}
	
	/**
	 * 查询部门列表
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:department:view")
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		Long officeId = UserUtils.getUser().getCompany().getId();
		List<Department> list = Lists.newArrayList();
		List<Department> sourcelist = departmentService.findByOfficeId(officeId);
		Department dept = departmentService.getParent(UserUtils.getUser().getCompany());
		if(dept != null) {
			model.addAttribute("deptParentId", dept != null ? dept.getId() : "");
			Department.sortList(list, sourcelist, dept.getId());
			list.add(0, dept);
	        model.addAttribute("list", list);
		}
		return "modules/sys/departmentList";
	}
	
	/**
	 * 添加批发商顶级部门
	 * @param model
	 * @return
	 */
	@RequestMapping(value = {"addParent"})
	public String addParent(Model model) {
		departmentService.addParent(UserUtils.getUser().getCompany());
		return "redirect:" + Global.getAdminPath() + "/sys/department/";
	}
	
	/**
	 * 添加或修改页面跳转
	 * @param department
	 * @param model
	 * @return
	 */
	@RequiresPermissions("sys:department:view")
	@RequestMapping(value = "form")
	public String form(Department department, Model model) {
		//添加：查找部门顶级部门；修改：查找部门父类部门
		if (department.getParent() == null || department.getParent().getId() == null) {
			Department dept = departmentService.getParent(UserUtils.getUser().getCompany());
			department.setParent(dept);
		}
		department.setParent(departmentService.findById(department.getParent().getId()));
		model.addAttribute("department", department);
		List<Menu> list = filterAuth(systemService.findAllMenu());
//		List<Menu> operationList = Lists.newArrayList();
		model.addAttribute("selectMenuList", list);
		model.addAttribute("operationMenuList", list);
		return "modules/sys/departmentForm";
	}
	
	/**
	 * 部门-职务管理页面
	 * @author gaoang
	 * @return
	 */
	@RequiresPermissions("sys:department:view")
	@RequestMapping(value = "jobManagement")
	public String jobManagement(HttpServletRequest request,HttpServletResponse response,Model model){
		Page<Map<String,Object>> JobManagementList = departmentService.searchJobManagementList(request, response);
		request.setAttribute("resultList", JobManagementList.getList());
		String pageStr = JobManagementList.toString();
		pageStr = pageStr.replace("<div style=\"clear:both;\"></div>", "");
		request.setAttribute("pageStr", pageStr);
		request.setAttribute("page", JobManagementList);
		
		return "modules/sys/departmentJobManagement";
	}
	
	/**
	 * 职务管理-新增和修改时对职务名称唯一性校验
	 * @param request
	 * @return
	 */
	@RequestMapping(value ="checkRepeat4JobManagement")
	@ResponseBody
	public String checkRepeat4JobManagement(HttpServletRequest request){
		String name = request.getParameter("name");
		String uuid = request.getParameter("uuid");
		List<Map<String, Object>> list = departmentService.searchJobNames(name.trim(),uuid);
		if(CollectionUtils.isEmpty(list)){
			return "success";
		}else{
			return "fail";
		}
		
	}
	
	/**
	 * 职务管理-新增保存
	 * @return
	 */
	@RequestMapping(value ="saveJobManagement")
	@ResponseBody
	public void saveJobManagement(HttpServletRequest request){
		String type = request.getParameter("type");
		String name = request.getParameter("name");
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		SysJobNew job = new SysJobNew();
		job.setCompanyId(UserUtils.getUser().getCompany().getId());
		job.setCompanyUuid(UserUtils.getUser().getCompany().getUuid());
		job.setCreateBy(UserUtils.getUser().getId());
		job.setCreateDate(new Date());
		job.setDelFlag("0");
		job.setName(name.trim());
		job.setType(Integer.parseInt(type));
		job.setUuid(uuid);
		sysJobNewDao.save(job);

	}
	
	/**
	 * 职务管理-修改
	 * @param request
	 */
	@RequestMapping(value = "modifyJobManagement")
	@ResponseBody
	public void modifyJobManagement(HttpServletRequest request){
		String type = request.getParameter("type");
		String name = request.getParameter("name");
		String uuid = request.getParameter("uuid");
		
		departmentService.modifyJobManagement(type, name.trim(), uuid);
	}
	
	/**
	 * 部门-职务管理-删除
	 * @param uuid
	 * @return
	 */
	@RequestMapping(value ="deleteJobManagement")
	@ResponseBody
	public String deleteJobManagement(HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		departmentService.deleteJobManagement(uuid);
		return "success";
	}
	
	
	@ResponseBody
	@RequestMapping(value = "checkParent")
	public String checkParent(Department department, Model model) {
		//添加：查找部门顶级部门；修改：查找部门父类部门
		if (department.getParent() == null || department.getParent().getId() == null) {
			Department dept = departmentService.getParent(UserUtils.getUser().getCompany());
			department.setParent(dept);
		}
		if(department.getParent() != null) {
			return "true";
		} else {
			return "error";
		}
	}
	
	/**
	 * 过滤不需要展示权限
	 * @param menuList
	 * @return
	 */
	private List<Menu> filterAuth(List<Menu> menuList) {
		//创建新的List并把顶级菜单放进去
		List<Menu> tempMenuList = Lists.newArrayList();
		tempMenuList.add(menuList.get(0));
		
		//查询"预订"、"订单"、"产品、询价、运控"五个菜单（目前只对这五个菜单下的查询进行控制）
		List<Menu> menusList = systemService.findMenuByName();
		
		//查询"预订"、"订单"、"产品、询价、运控"五个菜单下的子菜单并放入List
		if(CollectionUtils.isNotEmpty(menusList)) {
			for(Menu menu : menusList) {
				tempMenuList.add(menu);
				List<Menu> list = systemService.findByParentIdsLike(menu.getId());
				if("询价".equals(menu.getName())) {
					for(Menu temp : list) {
						if("销售询价列表".equals(temp.getName())) {
							tempMenuList.add(temp);
						}
					}
				} else if("订单".equals(menu.getName())) {
					for(Menu temp : list) {
						if("单团订单".equals(temp.getName()) || "散拼订单".equals(temp.getName()) || "游学订单".equals(temp.getName()) 
								|| "大客户订单".equals(temp.getName()) || "自由行订单".equals(temp.getName()) || "签务签证订单".equals(temp.getName()) || "销售签证订单".equals(temp.getName())) {
							tempMenuList.add(temp);
						} else if(StringUtils.isNotBlank(temp.getPermission()) && temp.getPermission().contains(":list:")) {
							if(!"销售机票订单".equals(temp.getName()) && !"计调机票订单".equals(temp.getName()) && !"销售签证订单(新)".equals(temp.getName()) && !"签务签证订单(新)".equals(temp.getName())) {
								tempMenuList.add(temp);
							}
						} 
					}
				} else if("产品".equals(menu.getName())) {
					for(Menu temp : list) {
						if("单团产品".equals(temp.getName()) || "散拼产品".equals(temp.getName()) || "游学产品".equals(temp.getName()) 
								|| "大客户产品".equals(temp.getName()) || "自由行产品".equals(temp.getName()) || "签证".equals(temp.getName())) {
							tempMenuList.add(temp);
						} else if(StringUtils.isNotBlank(temp.getPermission()) && temp.getPermission().contains(":list:")) {
							if(!"机票".equals(temp.getName()) && !"套餐".equals(temp.getName())) {
								tempMenuList.add(temp);
							}
						}
					}
				} else if("运控".equals(menu.getName())) {
					for(Menu temp : list) {
						if("库存切位".equals(temp.getName()) || "散拼产品".equals(temp.getName()) || "成本录入".equals(temp.getName())
								|| "单团成本录入".equals(temp.getName()) || "散拼成本录入".equals(temp.getName()) 
								|| "游学成本录入".equals(temp.getName()) || "大客户成本录入".equals(temp.getName())
								|| "自由行成本录入".equals(temp.getName()) || "签证成本录入".equals(temp.getName())) {
							if(StringUtils.isNotBlank(temp.getHref())) {
								tempMenuList.add(temp);
							}
						} 
					}
				}
			}
		}
		return tempMenuList;
	}
	
	/**
	 * 保存
	 * @param department
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:department:edit")
	@RequestMapping(value = "save")
	public String save(Department department, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, department)){
			return form(department, model);
		}
		department.setOffice(UserUtils.getUser().getCompany());
		//如果是修改，则菜单级别不用改变，如果是添加，则菜单级别加1
		if (department.getId() == null) {
			Department parent = departmentService.findById(department.getParent().getId());
			department.setLevel(parent.getLevel() + 1);
		}
		departmentService.save(department);
		addMessage(redirectAttributes, "保存部门'" + department.getName() + "'成功");
		return "redirect:" + Global.getAdminPath() + "/sys/department/";
	}
	
	/**
	 * 删除部门
	 * @param id
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:department:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		Map<String, String> map = departmentService.delDepartment(id);
		addMessage(redirectAttributes, map.get("msg"));
		return "redirect:"+Global.getAdminPath()+"/sys/department/";
	}
	
	/**
	 * 批量修改部门排序
	 * @param ids
	 * @param sorts
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:department:edit")
	@RequestMapping(value = "updateSort")
	public String updateSort(Long[] ids, Integer[] sorts, RedirectAttributes redirectAttributes) {
    	int len = ids.length;
    	Department[] departments = new Department[len];
    	for (int i = 0; i < len; i++) {
    		departments[i] = departmentService.findById(ids[i]);
    		departments[i].setSort(sorts[i]);
    		departmentService.save(departments[i]);
    	}
    	addMessage(redirectAttributes, "保存部门排序成功!");
		return "redirect:" + Global.getAdminPath() + "/sys/department/";
	}
	
	/**
	 * 查询批发商下部门
	 * @param officeId
	 * @param extId
	 * @param response
	 * @return
	 */
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) Long officeId, @RequestParam(required=false) Long extId, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		if(officeId == null) {
			officeId = UserUtils.getUser().getCompany().getId();
		}
		List<Department> list = departmentService.findByOfficeId(officeId);
		for (int i=0; i<list.size(); i++){
			Department e = list.get(i);
			if (extId == null || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf("," + extId + ",") == -1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParent()!=null?e.getParent().getId():0);
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	/**
	 * 校验部门编号是否重复
	 * @param id 部门ID
	 * @param code
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "check")
	public String check(Long id, String code, HttpServletRequest request) throws IOException {
		Long officeId = UserUtils.getUser().getCompany().getId();
		if(departmentService.findIsExist(officeId, id, code)) {
			  return "false";
		}
		return "true";
	}
}
