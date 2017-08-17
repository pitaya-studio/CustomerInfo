/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.island.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.island.entity.ActivityIslandGroupAirline;
import com.trekiz.admin.modules.island.input.ActivityIslandGroupAirlineInput;
import com.trekiz.admin.modules.island.query.ActivityIslandGroupAirlineQuery;
import com.trekiz.admin.modules.island.service.ActivityIslandGroupAirlineService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/activityIslandGroupAirline")
public class ActivityIslandGroupAirlineController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/island/activityislandgroupairline/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/activityIslandGroupAirline/list";
	protected static final String FORM_PAGE = "modules/island/activityislandgroupairline/form";
	protected static final String SHOW_PAGE = "modules/island/activityislandgroupairline/show";
	
	@Autowired
	private ActivityIslandGroupAirlineService activityIslandGroupAirlineService;
	
	private ActivityIslandGroupAirline dataObj;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dataObj=activityIslandGroupAirlineService.getByUuid(uuid);
		}
	}
	
	@RequestMapping(value = "list")
	public String list(ActivityIslandGroupAirlineQuery activityIslandGroupAirlineQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		activityIslandGroupAirlineQuery.setDelFlag("0");
        Page<ActivityIslandGroupAirline> page = activityIslandGroupAirlineService.find(new Page<ActivityIslandGroupAirline>(request, response), activityIslandGroupAirlineQuery); 
        model.addAttribute("page", page);
        model.addAttribute("activityIslandGroupAirlineQuery", activityIslandGroupAirlineQuery);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(ActivityIslandGroupAirlineInput activityIslandGroupAirlineInput, Model model) {
		model.addAttribute("activityIslandGroupAirlineInput", activityIslandGroupAirlineInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(ActivityIslandGroupAirlineInput activityIslandGroupAirlineInput, Model model, RedirectAttributes redirectAttributes) {
		
		String result="1";
		try {
			activityIslandGroupAirlineService.save(activityIslandGroupAirlineInput);
		} catch (Exception e) {
			result="0";
		}
		return result;
		
	}
	
	@RequestMapping(value = "show/{uuid}")
	public String show(@PathVariable String uuid, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		model.addAttribute("activityIslandGroupAirline", activityIslandGroupAirlineService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		ActivityIslandGroupAirline activityIslandGroupAirline = activityIslandGroupAirlineService.getByUuid(uuid);
		ActivityIslandGroupAirlineInput activityIslandGroupAirlineInput = new ActivityIslandGroupAirlineInput(activityIslandGroupAirline);
		model.addAttribute("activityIslandGroupAirlineInput", activityIslandGroupAirlineInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(ActivityIslandGroupAirlineInput activityIslandGroupAirlineInput, Model model, RedirectAttributes redirectAttributes) {
		String result="2";
		try {
			BeanUtil.copySimpleProperties(dataObj, activityIslandGroupAirlineInput,true);
			activityIslandGroupAirlineService.update(dataObj);
		} catch (Exception e) {
			result="0";
		}
		return result;
	}
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(String uuids) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(uuids)){
				String[] uuidArray = uuids.split(",");
				for(String uuid:uuidArray){
					activityIslandGroupAirlineService.removeByUuid(uuid);
				}
				
			}else{
				b=false;
				datas.put("message", "fail");
			}
		} catch (Exception e) {
			b = false;
			datas.put("message", "系统发生异常，请重新操作!");
		}
		if(b){
			datas.put("result", "1");
			datas.put("message", "success");
		}else{
			datas.put("result", "0");
		}
		return datas;
	}
}
