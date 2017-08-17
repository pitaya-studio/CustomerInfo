/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.web;

import java.io.IOException;
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
import com.trekiz.admin.modules.hotel.entity.SysTravelerType;
import com.trekiz.admin.modules.hotel.input.SysTravelerTypeInput;
import com.trekiz.admin.modules.hotel.query.SysTravelerTypeQuery;
import com.trekiz.admin.modules.hotel.service.SysTravelerTypeService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/sysTravelerType")
public class SysTravelerTypeController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/hotel/systravelertype/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/sysTravelerType/list";
	protected static final String FORM_PAGE = "modules/hotel/systravelertype/form";
	protected static final String SHOW_PAGE = "modules/hotel/systravelertype/show";
	
	@Autowired
	private SysTravelerTypeService sysTravelerTypeService;
	
	private SysTravelerType dataObj;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dataObj=sysTravelerTypeService.getByUuid(uuid);
		}
	}
	
	@RequestMapping(value = "list")
	public String list(SysTravelerTypeQuery sysTravelerTypeQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		sysTravelerTypeQuery.setDelFlag("0");
        Page<SysTravelerType> page = sysTravelerTypeService.find(new Page<SysTravelerType>(request, response), sysTravelerTypeQuery); 
        model.addAttribute("page", page);
        model.addAttribute("sysTravelerTypeQuery", sysTravelerTypeQuery);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(SysTravelerTypeInput sysTravelerTypeInput, Model model) {
		model.addAttribute("sysTravelerTypeInput", sysTravelerTypeInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(SysTravelerTypeInput sysTravelerTypeInput, Model model, RedirectAttributes redirectAttributes) {
		
		String result="1";
		try {
			sysTravelerTypeService.save(sysTravelerTypeInput);
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
		model.addAttribute("sysTravelerType", sysTravelerTypeService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		SysTravelerType sysTravelerType = sysTravelerTypeService.getByUuid(uuid);
		SysTravelerTypeInput sysTravelerTypeInput = new SysTravelerTypeInput(sysTravelerType);
		model.addAttribute("sysTravelerTypeInput", sysTravelerTypeInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(SysTravelerTypeInput sysTravelerTypeInput, Model model, RedirectAttributes redirectAttributes) {
		String result="2";
		try {
			BeanUtil.copySimpleProperties(dataObj, sysTravelerTypeInput,true);
			dataObj.setPersonType(sysTravelerTypeInput.getPersonType());
			sysTravelerTypeService.update(dataObj);
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
					sysTravelerTypeService.removeByUuid(uuid);
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
	
	@ResponseBody
	@RequestMapping(value = "checkName")
	public String checkName(String uuid, HttpServletRequest request) throws IOException {
		String travelerTypeName = request.getParameter("name");
		if (StringUtils.isEmpty(uuid)) {
			uuid = "0";
		}
		if(sysTravelerTypeService.findIsNameExist(uuid, travelerTypeName)) {
			return "false";
		}
		return "true";
	}
	
	@ResponseBody
	@RequestMapping(value = "checkShortName")
	public String checkShortName(String uuid, HttpServletRequest request) throws IOException {
		String travelerTypeName = request.getParameter("shortName");
		if (StringUtils.isEmpty(uuid)) {
			uuid = "0";
		}
		if(sysTravelerTypeService.findIsShortNameExist(uuid, travelerTypeName)) {
			return "false";
		}
		return "true";
	}
}
