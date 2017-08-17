/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.web;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.hotel.entity.SysCompanyDictView;
import com.trekiz.admin.modules.hotel.entity.SysDict;
import com.trekiz.admin.modules.hotel.entity.Sysdefinedict;
import com.trekiz.admin.modules.hotel.service.SysCompanyDictViewService;
import com.trekiz.admin.modules.hotel.service.SysDictService;
import com.trekiz.admin.modules.hotel.service.SysdefinedictService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/sysCompanyDictView")
public class SysCompanyDictViewController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/hotel/syscompanydictview/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/sysCompanyDictView/list";
	protected static final String FORM_PAGE = "modules/hotel/syscompanydictview/form";
	protected static final String SHOW_PAGE = "modules/hotel/syscompanydictview/show";
	
	protected static final String FORWARD_HOTEL_STAR_LIST_PAGE = "forward:"+Global.getAdminPath()+"/hotelStar/list";
	protected static final String RE_HOTEL_ROOM_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/hotelRoom/list";
	protected static final String FORWARD_HOTEL_TOPIC_LIST_PAGE = "forward:"+Global.getAdminPath()+"/hotelTopic/list";
	
	@Autowired
	private SysCompanyDictViewService sysCompanyDictViewService;
	@Autowired
	private SysDictService sysDictService;
	@Autowired
	private SysdefinedictService sysdefinedictService;
	
	
	@ModelAttribute  
    public void populateModel( Model model, HttpServletRequest request) {  
		String titleName="";
		String type = request.getParameter("type");
		if(StringUtils.isBlank(type)){
			type=Context.BaseInfo.HOTEL_INSTALLATION;
			
			titleName=Context.BaseInfo.HOTEL_INSTALLATION_TITLE;
			
		}else{
			switch (type) {
				case Context.BaseInfo.HOTEL_INSTALLATION:
					titleName=Context.BaseInfo.HOTEL_INSTALLATION_TITLE;
					break;
				
				case Context.BaseInfo.HOTEL_TOPIC:
					titleName=Context.BaseInfo.HOTEL_TOPIC_TITLE;
					break;
				
				case Context.BaseInfo.HOTEL_STAR:
					titleName=Context.BaseInfo.HOTEL_STAR_TITLE;
					break;
				
				case Context.BaseInfo.HOTEL_GROUP:
					titleName=Context.BaseInfo.HOTEL_GROUP_TITLE;
					break;	
					
				case Context.BaseInfo.HOTEL_MEAL_TYPE:
					titleName=Context.BaseInfo.HOTEL_MEAL_TYPE_TITLE;
					break;
	
				case Context.BaseInfo.HOTEL_FLOOR:
					titleName=Context.BaseInfo.HOTEL_FLOOR_TITLE;
					break;
				
//				case Context.BaseInfo.HOTEL_FEATURE:
//					titleName=Context.BaseInfo.HOTEL_FEATURE_TITLE;
//					break;
				
				case Context.BaseInfo.HOTEL_TYPE:
					titleName=Context.BaseInfo.HOTEL_TYPE_TITLE;
					break;
				
				case Context.BaseInfo.HOTEL_ROOM_FEATURE:
					titleName=Context.BaseInfo.HOTEL_ROOM_FEATURE_TITLE;
					break;
				
				case Context.BaseInfo.HOTEL_BED_TYPE:
					titleName=Context.BaseInfo.HOTEL_BED_TYPE_TITLE;
					break;
				
				case Context.BaseInfo.ISLANDS_MANAGER:
					titleName=Context.BaseInfo.ISLANDS_MANAGER_TITLE;
					break;
	
				case Context.BaseInfo.ISLANDS_TOPIC:
					titleName=Context.BaseInfo.ISLANDS_TOPIC_TITLE;
					break;
				
				case Context.BaseInfo.ISLANDS_TYPE:
					titleName=Context.BaseInfo.ISLANDS_TYPE_TITLE;
					break;
				
				case Context.BaseInfo.ISLANDS_LANDING_STYLE:
					titleName=Context.BaseInfo.ISLANDS_LANDING_STYLE_TITLE;
					break;
				
				case Context.BaseInfo.WHOLESALER_TYPE:
					titleName=Context.BaseInfo.WHOLESALER_TYPE_TITLE;
					break;
				
				case Context.BaseInfo.WHOLESALER_LEVEL:
					titleName=Context.BaseInfo.WHOLESALER_LEVEL_TITLE;
					break;	
					
				case Context.BaseInfo.TRAVEL_AGENCY_TYPE:
					titleName=Context.BaseInfo.TRAVEL_AGENCY_TYPE_TITLE;
					break;	
				
				case Context.BaseInfo.TRAVEL_AGENCY_LEVEL:
					titleName=Context.BaseInfo.TRAVEL_AGENCY_LEVEL_TITLE;
					break;
				
				case Context.BaseInfo.TRAVELER_TYPE:
					titleName=Context.BaseInfo.TRAVELER_TYPE_TITLE;
					break;
					
				default:
					titleName=Context.BaseInfo.HOTEL_INSTALLATION_TITLE;
					break;
			}
		}
		
		//superadmin 禁止显示 页面上的基础信息 页签 
		String source = request.getParameter("source");
		if(StringUtils.isNotBlank(source)&&source.equals("super")){
			model.addAttribute("source", source);  
		}

		model.addAttribute("titleName", titleName);  
		model.addAttribute("type", type);  
    }

	@RequiresPermissions("common:mtour:menu")
	@RequestMapping(value = "list")
	public String list(SysCompanyDictView sysCompanyDictView, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isBlank(sysCompanyDictView.getType())){
			sysCompanyDictView.setType(Context.BaseInfo.HOTEL_INSTALLATION);
		}
		
		//超级管理员和普通批发商用户跳转不同页面
		if(getCompanyId() != -1) {
			if(Context.BaseInfo.HOTEL_STAR.equals(sysCompanyDictView.getType())) {
				return FORWARD_HOTEL_STAR_LIST_PAGE;
			}  else if(Context.BaseInfo.HOTEL_TOPIC.equals(sysCompanyDictView.getType())) {
				return FORWARD_HOTEL_TOPIC_LIST_PAGE;
			}
		}
		
		sysCompanyDictView.setDelFlag("0");
		sysCompanyDictView.setCompanyId(getCompanyId());
		
        List<SysCompanyDictView> list = sysCompanyDictViewService.find(sysCompanyDictView); 
        model.addAttribute("list", list);
        model.addAttribute("companyId", getCompanyId());  
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(SysCompanyDictView sysCompanyDictView, Model model) {
		model.addAttribute("sysCompanyDictView", sysCompanyDictView);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public Object save(SysCompanyDictView sysCompanyDictView, Model model, RedirectAttributes redirectAttributes) {
		Map<String,Object> datas = new HashMap<String, Object>();
		
		if(getCompanyId() == 0) {
			datas.put("message", "3");
			datas.put("error", "用户公司不能为空");
			return datas;
		}
		
		if(getCompanyId()==-1) {
			sysDictService.save(sysCompanyDictView);
		} else {
			sysdefinedictService.save(sysCompanyDictView, getCompanyId());
		}

		datas.put("message", "1");
		return datas;
	}
	
	@ResponseBody
	@RequestMapping(value = "saveAjax")
	public Object saveAjax(SysCompanyDictView sysCompanyDictView) {
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			//只有批发商用户才可以调用此处方法，屏蔽系统用户的增加权限 
			if(getCompanyId() == 0) {
				b=false;
				datas.put("message", "没有操作权限！");
				datas.put("result", "0");
				return datas;
				
			} else if(getCompanyId() == -1) {
				sysDictService.save(sysCompanyDictView);
			} else {
				sysdefinedictService.save(sysCompanyDictView, getCompanyId());
			}
			
			datas.put("uuid", sysCompanyDictView.getUuid());
			
		} catch (Exception e) {
			b = false;
			datas.put("message", "操作处理异常！");
		}
		if(b){
			datas.put("result", "1");
		}else{
			datas.put("result", "0");
		}
		return datas;
	}
	
	@RequestMapping(value = "show/{uuid}")
	public String show(@PathVariable String uuid, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		SysCompanyDictView sysCompanyDictView = sysCompanyDictViewService.getByUuId(uuid);
		
		model.addAttribute("sysCompanyDictView", sysCompanyDictView);
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		SysCompanyDictView sysCompanyDictView = sysCompanyDictViewService.getByUuId(uuid);
		
		if(sysCompanyDictView == null || sysCompanyDictView.getCompanyId().longValue() != getCompanyId()) {
			return FORM_PAGE;
		}
		
		model.addAttribute("sysCompanyDictView", sysCompanyDictView);
		return FORM_PAGE;
	}
	
	@ResponseBody
	@RequestMapping(value = "update")
	public Object update(SysCompanyDictView sysCompanyDictView, Model model, RedirectAttributes redirectAttributes) {
		Map<String,Object> datas = new HashMap<String, Object>();
		if(sysCompanyDictView == null) {
			datas.put("message", "3");
			datas.put("error", "系统异常，请重新操作!");
			return datas;
		}
		
		if(getCompanyId()==-1) {
			sysDictService.update(sysCompanyDictView);
		} else {
			sysdefinedictService.update(sysCompanyDictView);
		}
		
		datas.put("message", "2");
		return datas;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(String uuid,String companyId) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(uuid)&&StringUtils.isNotBlank(companyId)&&companyId.equals(getCompanyId()+"")){
				
				if(companyId.equals("-1")){
					sysDictService.removeByUuid(uuid);
				}else{
					sysdefinedictService.removeByUuid(uuid);
				}
			}else{
				b=false;
				datas.put("message", "传递参数异常或不具备操作该记录权限！");
			}
		} catch (Exception e) {
			b = false;
			datas.put("message", "操作处理异常！");
		}
		if(b){
			datas.put("result", "1");
		}else{
			datas.put("result", "0");
		}
		return datas;
	}
	
	
	@ResponseBody
	@RequestMapping(value = "updateOrder")
	public Object updateOrder(String idAndSorts,String companyId) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			
			if(StringUtils.isNotBlank(idAndSorts)){
				String[] idAndSortsArray = idAndSorts.split(";");
				if(idAndSortsArray!=null && idAndSortsArray.length>0){
					for(String idAndSort : idAndSortsArray){
						if(StringUtils.isNotBlank(idAndSort)){
							String[] idAndSortArray = idAndSort.split(",");
							if(idAndSortArray!=null && idAndSortArray.length>0){
								
								if(getCompanyId()==Long.parseLong(idAndSortArray[2])){
									if(companyId.equals("-1")){
										SysDict sd = sysDictService.getById(Long.parseLong(idAndSortArray[0]));
										sd.setSort(Integer.parseInt(idAndSortArray[1]));
										sysDictService.update(sd);
									}else{
										Sysdefinedict sd = sysdefinedictService.getById(Long.parseLong(idAndSortArray[0]));
										sd.setSort(Integer.parseInt(idAndSortArray[1]));
										sysdefinedictService.update(sd);
									}
								}else{
									if(datas.containsKey("sortException")){
										datas.put("sortException", datas.get("sortException")+idAndSortArray[1]+",");
									}else{
										datas.put("sortException", idAndSortArray[1]+",");
									}
									
								}
								
							}
						}
					}
				}
			}else{
				b=false;
				datas.put("message", "至少选择一行进行排序！");
			}
		} catch (Exception e) {
			b = false;
			datas.put("message", "操作处理异常！");
			e.printStackTrace();
		}
		if(b){
			datas.put("result", "1");
		}else{
			datas.put("result", "0");
		}
		return datas;
	}
	
	@ResponseBody
	@RequestMapping(value = "check")
	public String check(String type, Integer id, HttpServletRequest request) throws IOException {
		String label = request.getParameter("label");
		if(id==null)
		{
			id = -1;
		}
		
		if(sysCompanyDictViewService.findIsExist(type, id, label, getCompanyId())) {
			return "false";
		}
		return "true";
	}
	
	@ResponseBody
	@RequestMapping(value = "saveDict")
	public Object saveDict(SysCompanyDictView sysCompanyDictView, HttpServletRequest request) {
		Map<String, Object> datas = new HashMap<String, Object>();
		try{
			sysdefinedictService.save(sysCompanyDictView, getCompanyId());
			datas.put("result", "1");
			datas.put("uuid", sysCompanyDictView.getUuid());
			
		} catch(Exception e) {
			e.printStackTrace();
			datas.put("result", "0");
			datas.put("message", "系统发生异常,请重新操作!");
		}
		return datas;
	}
	
	@ResponseBody
	@RequestMapping(value = "getDescByUuid")
	public String getDescByUuid(String uuid, HttpServletRequest request) throws IOException {
		if(StringUtils.isEmpty(uuid)) {
			return "";
		}
		SysCompanyDictView sysCompanyDictView = sysCompanyDictViewService.findByUuid(uuid);
		if(sysCompanyDictView != null) {
			return sysCompanyDictView.getDescription();
		}
		return "";
	}
	
}
