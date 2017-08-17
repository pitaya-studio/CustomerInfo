/**
 *
 */
package com.trekiz.admin.modules.sys.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
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

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.sys.entity.Menu;
import com.trekiz.admin.modules.sys.model.MenuJsonBean;
import com.trekiz.admin.modules.sys.service.SystemService;

/**
 * 菜单Controller
 * @author zj
 * @version 2013-11-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/menu")
public class MenuController extends BaseController {

	@ModelAttribute("menuId")
	protected Integer getMenuId(){
		return 4;
	}
	
	@Autowired
	private SystemService systemService;
	
	@ModelAttribute("menu")
	public Menu get(@RequestParam(required=false) Long id) {
		if (id != null){
			return systemService.getMenu(id);
		}else{
			return new Menu();
		}
	}

	@RequiresPermissions("sys:menu:view")
	@RequestMapping(value = {"list", ""})
	public String list(Model model) {
		List<Menu> list = Lists.newArrayList();
		List<Menu> sourcelist = systemService.findAllMenu();
		Menu.sortList(list, sourcelist, 1L);
        model.addAttribute("list", list);
		return "modules/sys/menuList";
	}

	@RequiresPermissions("sys:menu:view")
	@RequestMapping(value = "form")
	public String form(Menu menu, Model model) {
		if (menu.getParent()==null||menu.getParent().getId()==null){
			menu.setParent(new Menu(1L));
		}
		menu.setParent(systemService.getMenu(menu.getParent().getId()));
		Integer level = menu.getLevel();
		model.addAttribute("level",level);
		model.addAttribute("menu", menu);
		return "modules/sys/menuForm";
	}
	
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "save")
	public String save(Menu menu, Model model, RedirectAttributes redirectAttributes) {
		if (!beanValidator(model, menu)){
			return form(menu, model);
		}
		systemService.saveMenu(menu);
		addMessage(redirectAttributes, "保存菜单'" + menu.getName() + "'成功");
		return "redirect:"+Global.getAdminPath()+"/sys/menu/";
	}
	
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "delete")
	public String delete(Long id, RedirectAttributes redirectAttributes) {
		if (Menu.isRoot(id)){
			addMessage(redirectAttributes, "删除菜单失败, 不允许删除顶级菜单或编号为空");
		}else{
			systemService.deleteMenu(id);
			addMessage(redirectAttributes, "删除菜单成功");
		}
		return "redirect:"+Global.getAdminPath()+"/sys/menu/";
	}

	@RequiresUser
	@RequestMapping(value = "tree")
	public String tree() {
		return "modules/sys/menuTree";
	}
	
	/**
	 * 批量修改菜单排序
	 */
	@RequiresPermissions("sys:menu:edit")
	@RequestMapping(value = "updateSort")
	public String updateSort(Long[] ids, Integer[] sorts, RedirectAttributes redirectAttributes) {
    	int len = ids.length;
    	Menu[] menus = new Menu[len];
    	for (int i = 0; i < len; i++) {
    		menus[i] = systemService.getMenu(ids[i]);
    		menus[i].setSort(sorts[i]);
    		systemService.saveMenu(menus[i]);
    	}
    	addMessage(redirectAttributes, "保存菜单排序成功!");
		return "redirect:"+Global.getAdminPath()+"/sys/menu/";
	}
	
	@RequiresUser
	@ResponseBody
	@RequestMapping(value = "treeData")
	public List<Map<String, Object>> treeData(@RequestParam(required=false) Long extId, HttpServletResponse response) {
		response.setContentType("application/json; charset=UTF-8");
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Menu> list = systemService.findAllMenu();
		for (int i=0; i<list.size(); i++){
			Menu e = list.get(i);
			if (extId == null || (extId!=null && !extId.equals(e.getId()) && e.getParentIds().indexOf(","+extId+",")==-1)){
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
	 *  跳转到菜单导入导出页面
		 * @Title: menuManage
	     * @return String
	     * @author majiancheng       
	     * @date 2015-10-27 下午9:33:17
	 */
	@RequestMapping(value = "menuManage")
	public String menuManage(HttpServletRequest request, HttpServletResponse response) {
		String entryData = request.getParameter("entryData");
		//只有entryData为3d8feff7959d4121a7825cff2b58f931时才可以访问
		//访问链接：/sys/menu/menuManage?entryData=3d8feff7959d4121a7825cff2b58f931
		if("3d8feff7959d4121a7825cff2b58f931".equals(entryData)) {
			return "modules/sys/menuManage";
		} else {
			return "redirect:"+Global.getAdminPath();
		}
	}
	
	/**
	 * 根据菜单id导出菜单信息
		 * @Title: exportMenu
	     * @return String
	     * @author majiancheng       
	     * @date 2015-10-27 下午7:46:51
	 */
	@ResponseBody
	@RequestMapping(value = "exportMenu")
	public Object exportMenu(HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, String> datas = new HashMap<String, String>();
		String menuIds = request.getParameter("menuIds");
		if(StringUtils.isEmpty(menuIds)) {
			datas.put("result", "2");
			datas.put("message", "菜单Id不能为空，请重新输入！");
			return datas;
		}
		
		try{
			//装载和该菜单相关联的所有子菜单和父菜单
			MenuJsonBean menuDatas = systemService.buildMenusJsonDataByParentId(menuIds);
			datas.put("result", "1");
			datas.put("menuDatas", JSON.toJSONStringWithDateFormat(menuDatas, "yyyy-MM-dd"));
			
		} catch(Exception e) {
			e.printStackTrace();
			datas.put("result", "3");
			datas.put("message", "系统出现异常，请重新输入！");
		}
		
		return datas;
	}
	
	/**
	 * 根据json导入菜单数据
		 * @Title: importMenu
	     * @return Object
	     * @author majiancheng       
	     * @date 2015-10-27 下午8:23:29
	 */
	@ResponseBody
	@RequestMapping(value = "importMenu")
	public Object importMenu(String menuJsonData, HttpServletRequest request, HttpServletResponse response, Model model) {
		Map<String, String> datas = new HashMap<String, String>();
		if(StringUtils.isEmpty(menuJsonData)) {
			datas.put("result", "2");
			datas.put("message", "json数据不能为空，请重新输入！");
			return datas;
		}
		
		try{
			//装载和该菜单相关联的所有子菜单和父菜单
			datas = systemService.saveMenusByMenuJsonData(menuJsonData);
			
		} catch(Exception e) {
			e.printStackTrace();
			datas.put("result", "3");
			datas.put("message", "系统出现异常，请重新输入！");
		}
		
		return datas;
	}
}
