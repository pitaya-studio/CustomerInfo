package com.trekiz.admin.modules.sys.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.sys.entity.Department;
import com.trekiz.admin.modules.sys.entity.Office;
import com.trekiz.admin.modules.sys.entity.Role;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.service.OfficeService;
import com.trekiz.admin.modules.sys.service.SystemService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 角色Controller
 * @author zj
 * @version 2013-11-19 update 2013-06-08
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/role")
public class RoleController extends BaseController {

	@ModelAttribute("menuId")
	protected Integer getMenuId(){
		return 7;
	}
	
	@Autowired
	private SystemService systemService;
	
	@Autowired
	private OfficeService officeService;
	@Autowired
	private DepartmentService departmentService;
	
	@ModelAttribute("role")
	public Role get(@RequestParam(required=false) Long id) {
		if (id != null){
			return systemService.getRole(id);
		}else{
			return new Role();
		}
	}
	
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = {"list", ""})
	public String list(Role role, HttpServletRequest request, HttpServletResponse response, Model model) {
		
		Page<Role> page = systemService.findRole(new Page<Role>(request, response), role);
		Map<Long,String> offMap = null;
        if (page != null){
			offMap = new HashMap<Long,String>();
			for (Role r : page.getList()) {
				Long officeId = r.getCompanyId();
				if (officeId != null && officeId != 0) {
					offMap.put(r.getId(),officeService.get(officeId).getName());
				}
			}
		}
        model.addAttribute("page", page);
        model.addAttribute("offMap", offMap);
		return "modules/sys/roleList";
	}

	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "form")
	public String form(Role role, Model model) {
		List<Office> officeList = new ArrayList<Office>();
		List<Office> officeMapList = new ArrayList<Office>();
		officeList = officeService.findAll();
		if(officeList!=null) {
			officeMapList = officeList;
		}
		
		//如果角色关联的部门已经被删除，则赋空对象值
		if(role.getDepartment() != null && !Context.DEL_FLAG_NORMAL.equals(role.getDepartment().getDelFlag())) {
			role.setDepartment(null);
		}
		
		Map<String,String> roleTypeMap = new TreeMap<String,String>();
		roleTypeMap.put(Context.ROLE_TYPE_OTHER, "其他");
		roleTypeMap.put(Context.ROLE_TYPE_MANAGER, "系统管理员");
		roleTypeMap.put(Context.ROLE_TYPE_SALES, "销售");
		roleTypeMap.put(Context.ROLE_TYPE_SALES_EXECUTIVE, "销售主管");
		roleTypeMap.put(Context.ROLE_TYPE_OP, "计调");
		roleTypeMap.put(Context.ROLE_TYPE_OP_EXECUTIVE, "计调主管");
		model.addAttribute("roleType", roleTypeMap);
		model.addAttribute("officeMapList", officeMapList);
		model.addAttribute("role", role);
		model.addAttribute("menuList", systemService.findAllMenu());
		return "modules/sys/roleForm";
	}
	
	/**
	 * 保存（修改）：先校验用户名是否重复
	 * @param role
	 * @param model
	 * @param oldName
	 * @param redirectAttributes
	 * @return
	 */
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "save")
	public String save(Role role, Long departmentId,Model model, String oldName, RedirectAttributes redirectAttributes) {
//		systemService.clearRole(role);
		if (!beanValidator(model, role)){
			return form(role, model);
		}
		//验证如果是系统管理员，那么部门选择是否是总部
		if (Context.ROLE_TYPE_MANAGER.equals(role.getRoleType())) {
			boolean isHQ = systemService.isHQ(departmentId);
			if(!isHQ) {
				addMessage(model, "类型为'系统管理员'的角色只能属于总部");
				return form(role, model);
			}
		}
		
		//验证角色名是否在批发商的所有角色中已存在
		boolean flag = systemService.findRoleByCompanyIdAndName(role.getId(), role.getCompanyId(), role.getName());
		if (!flag){
			addMessage(model, "保存角色'" + role.getName() + "'失败, 角色名已存在");
			return form(role, model);
		} else {
			//add by ruyi.chen 修复角色修改部门问题
			if(departmentId == null) {
				role.setDepartment(null);
			}else{
				Department dep = departmentService.findById(departmentId);
				role.setDepartment(dep);
			}
			
			systemService.saveRole(role);
			addMessage(redirectAttributes, "保存角色'" + role.getName() + "'成功");
			return "redirect:"+Global.getAdminPath()+"/sys/role/?repage";
		}
	}
	
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "delete")
	public String delete(@RequestParam Long id, RedirectAttributes redirectAttributes) {
		if (Role.isAdmin(id)){
			addMessage(redirectAttributes, "删除角色失败, 不允许内置角色或编号空");
		} else if (UserUtils.getUser().getRoleIdList().contains(id)){
			addMessage(redirectAttributes, "删除角色失败, 不能删除当前用户所在角色");
		} else {
			systemService.deleteRole(id);
			addMessage(redirectAttributes, "删除角色成功");
		}
		return "redirect:"+Global.getAdminPath()+"/sys/role/?repage";
	}
	
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "assign")
	public String assign(Role role, Model model) {
		List<User> users = role.getUserList();
		model.addAttribute("users", users);
		return "modules/sys/roleAssign";
	}
	
	@RequiresPermissions("sys:role:view")
	@RequestMapping(value = "usertorole")
	public String selectUserToRole(Role role, Model model) {
		model.addAttribute("role", role);
		model.addAttribute("selectIds", role.getUserIds());
		return "modules/sys/selectUserToRole";
	}
	
	@RequiresPermissions("sys:role:view")
	@ResponseBody
	@RequestMapping(value = "users")
	public List<Map<String, Object>> users(Long officeId, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		return mapList;
	}
	
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "outrole")
	public String outrole(Long userId, Long roleId, RedirectAttributes redirectAttributes) {
		Role role = systemService.getRole(roleId);
		User user = systemService.getUser(userId);
		if (user.equals(UserUtils.getUser())) {
			addMessage(redirectAttributes, "无法从角色【" + role.getName() + "】中移除用户【" + user.getName() + "】自己！");
		}else {
			Boolean flag = systemService.outUserInRole(role, userId);
			if (!flag) {
				addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除失败！");
			}else {
				addMessage(redirectAttributes, "用户【" + user.getName() + "】从角色【" + role.getName() + "】中移除成功！");
			}			
		}
		return "redirect:"+Global.getAdminPath()+"/sys/role/assign?id="+role.getId();
	}
	
	@RequiresPermissions("sys:role:edit")
	@RequestMapping(value = "assignrole")
	public String assignRole(Role role, Long[] idsArr, RedirectAttributes redirectAttributes) {
		StringBuilder msg = new StringBuilder();
		int newNum = 0;
		for (int i = 0; i < idsArr.length; i++) {
			User user = systemService.assignUserToRole(role, idsArr[i]);
			if (null != user) {
				msg.append("<br/>新增用户【" + user.getName() + "】到角色【" + role.getName() + "】！");
				newNum++;
			}
		}
		addMessage(redirectAttributes, "已成功分配 "+newNum+" 个用户"+msg);
		return "redirect:"+Global.getAdminPath()+"/sys/role/assign?id="+role.getId();
	}
	
	/**
	 * 查询所有角色下用户
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "roleOfUsers")
	public String roleOfUsers(HttpServletRequest request) throws IOException {
		List<Role> roleList = systemService.findByRoleTypeAndCompanyId(Context.ROLE_TYPE_OP, Context.ROLE_TYPE_OP_EXECUTIVE, UserUtils.getUser().getCompany().getId());
		if(CollectionUtils.isNotEmpty(roleList)) {
			JSONObject json = new JSONObject();
			for(Role role : roleList) {
				List<User> userList = systemService.getUserByRoleId(role.getId());
				if(CollectionUtils.isNotEmpty(userList)) {
					JSONArray array = new JSONArray();
					for(User user : userList) {
						JSONObject userJson = new JSONObject();
						userJson.put("userId", user.getId());
						userJson.put("userName", user.getName());
						array.add(userJson);
					}
					json.put(role.getName(), array);
				} else {
					return "";
				}
			}
			return json.toString();
		} else {
			return "";
		}
	}
}
