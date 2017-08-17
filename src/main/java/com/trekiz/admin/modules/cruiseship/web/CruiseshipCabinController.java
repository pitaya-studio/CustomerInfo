/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.cruiseship.web;

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
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipCabin;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipCabinInput;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipCabinQuery;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipCabinService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/cruiseshipCabin")
public class CruiseshipCabinController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/cruiseship/cruiseshipcabin/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/cruiseshipCabin/list";
	protected static final String FORM_PAGE = "modules/cruiseship/cruiseshipcabin/form";
	protected static final String SHOW_PAGE = "modules/cruiseship/cruiseshipcabin/show";
	
	@Autowired
	private CruiseshipCabinService cruiseshipCabinService;
	
	private CruiseshipCabin dataObj;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dataObj=cruiseshipCabinService.getByUuid(uuid);
		}
	}
	
	@RequestMapping(value = "list")
	public String list(CruiseshipCabinQuery cruiseshipCabinQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		cruiseshipCabinQuery.setDelFlag("0");
        Page<CruiseshipCabin> page = cruiseshipCabinService.find(new Page<CruiseshipCabin>(request, response), cruiseshipCabinQuery); 
        model.addAttribute("page", page);
        model.addAttribute("cruiseshipCabinQuery", cruiseshipCabinQuery);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(CruiseshipCabinInput cruiseshipCabinInput, Model model) {
		model.addAttribute("cruiseshipCabinInput", cruiseshipCabinInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(CruiseshipCabinInput cruiseshipCabinInput, Model model, RedirectAttributes redirectAttributes) {
		
		String result="1";
		try {
			cruiseshipCabinService.save(cruiseshipCabinInput);
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
		model.addAttribute("cruiseshipCabin", cruiseshipCabinService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		CruiseshipCabin cruiseshipCabin = cruiseshipCabinService.getByUuid(uuid);
		CruiseshipCabinInput cruiseshipCabinInput = new CruiseshipCabinInput(cruiseshipCabin);
		model.addAttribute("cruiseshipCabinInput", cruiseshipCabinInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(CruiseshipCabinInput cruiseshipCabinInput, Model model, RedirectAttributes redirectAttributes) {
		String result="2";
		try {
			BeanUtil.copySimpleProperties(dataObj, cruiseshipCabinInput,true);
			cruiseshipCabinService.update(dataObj);
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
				b = cruiseshipCabinService.batchDelete(uuidArray);
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
