/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.preferential.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.preferential.entity.PreferentialUnit;
import com.trekiz.admin.modules.preferential.service.PreferentialUnitService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/preferentialUnit")
public class PreferentialUnitController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/preferential/preferentialunit/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/preferentialUnit/list";
	protected static final String FORM_PAGE = "modules/preferential/preferentialunit/form";
	protected static final String SHOW_PAGE = "modules/preferential/preferentialunit/show";
	
	@Autowired
	private PreferentialUnitService preferentialUnitService;
	
	
	@RequestMapping(value = "list")
	public String list(PreferentialUnit preferentialUnit, HttpServletRequest request, HttpServletResponse response, Model model) {
		preferentialUnit.setDelFlag("0");
        Page<PreferentialUnit> page = preferentialUnitService.find(new Page<PreferentialUnit>(request, response), preferentialUnit); 
        model.addAttribute("page", page);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(PreferentialUnit preferentialUnit, Model model) {
		model.addAttribute("preferentialUnit", preferentialUnit);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(PreferentialUnit preferentialUnit, Model model, RedirectAttributes redirectAttributes) {
		
		preferentialUnitService.save(preferentialUnit);
		return "1";
	}
	
	@RequestMapping(value = "show/{uuid}")
	public String show(@PathVariable String uuid, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		model.addAttribute("preferentialUnit", preferentialUnitService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		model.addAttribute("preferentialUnit", preferentialUnitService.getByUuid(uuid));
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(PreferentialUnit preferentialUnit, Model model, RedirectAttributes redirectAttributes) {
		
		String result="2";
		try {
			preferentialUnitService.update(preferentialUnit);
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
					preferentialUnitService.removeByUuid(uuid);
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
