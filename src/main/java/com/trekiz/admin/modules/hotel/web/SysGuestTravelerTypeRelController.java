/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.web;

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
import com.trekiz.admin.modules.hotel.entity.SysGuestTravelerTypeRel;
import com.trekiz.admin.modules.hotel.input.SysGuestTravelerTypeRelInput;
import com.trekiz.admin.modules.hotel.query.SysGuestTravelerTypeRelQuery;
import com.trekiz.admin.modules.hotel.service.SysGuestTravelerTypeRelService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/sysGuestTravelerTypeRel")
public class SysGuestTravelerTypeRelController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/hotel/sysguesttravelertyperel/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/sysGuestTravelerTypeRel/list";
	protected static final String FORM_PAGE = "modules/hotel/sysguesttravelertyperel/form";
	protected static final String SHOW_PAGE = "modules/hotel/sysguesttravelertyperel/show";
	
	@Autowired
	private SysGuestTravelerTypeRelService sysGuestTravelerTypeRelService;
	
	private SysGuestTravelerTypeRel dataObj;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dataObj=sysGuestTravelerTypeRelService.getByUuid(uuid);
		}
	}
	
	@RequestMapping(value = "list")
	public String list(SysGuestTravelerTypeRelQuery sysGuestTravelerTypeRelQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		sysGuestTravelerTypeRelQuery.setDelFlag("0");
        Page<SysGuestTravelerTypeRel> page = sysGuestTravelerTypeRelService.find(new Page<SysGuestTravelerTypeRel>(request, response), sysGuestTravelerTypeRelQuery); 
        model.addAttribute("page", page);
        model.addAttribute("sysGuestTravelerTypeRelQuery", sysGuestTravelerTypeRelQuery);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(SysGuestTravelerTypeRelInput sysGuestTravelerTypeRelInput, Model model) {
		model.addAttribute("sysGuestTravelerTypeRelInput", sysGuestTravelerTypeRelInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(SysGuestTravelerTypeRelInput sysGuestTravelerTypeRelInput, Model model, RedirectAttributes redirectAttributes) {
		
		String result="1";
		try {
			sysGuestTravelerTypeRelService.save(sysGuestTravelerTypeRelInput);
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
		model.addAttribute("sysGuestTravelerTypeRel", sysGuestTravelerTypeRelService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		SysGuestTravelerTypeRel sysGuestTravelerTypeRel = sysGuestTravelerTypeRelService.getByUuid(uuid);
		SysGuestTravelerTypeRelInput sysGuestTravelerTypeRelInput = new SysGuestTravelerTypeRelInput(sysGuestTravelerTypeRel);
		model.addAttribute("sysGuestTravelerTypeRelInput", sysGuestTravelerTypeRelInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(SysGuestTravelerTypeRelInput sysGuestTravelerTypeRelInput, Model model, RedirectAttributes redirectAttributes) {
		String result="2";
		try {
			BeanUtil.copySimpleProperties(dataObj, sysGuestTravelerTypeRelInput,true);
			sysGuestTravelerTypeRelService.update(dataObj);
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
					sysGuestTravelerTypeRelService.removeByUuid(uuid);
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
