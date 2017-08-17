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
import com.trekiz.admin.modules.preferential.entity.PreferentialLogicOperation;
import com.trekiz.admin.modules.preferential.service.PreferentialLogicOperationService;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/preferentialLogicOperation")
public class PreferentialLogicOperationController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/preferential/preferentiallogicoperation/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/preferentialLogicOperation/list";
	protected static final String FORM_PAGE = "modules/preferential/preferentiallogicoperation/form";
	protected static final String SHOW_PAGE = "modules/preferential/preferentiallogicoperation/show";
	
	@Autowired
	private PreferentialLogicOperationService preferentialLogicOperationService;
	
	
	@RequestMapping(value = "list")
	public String list(PreferentialLogicOperation preferentialLogicOperation, HttpServletRequest request, HttpServletResponse response, Model model) {
		preferentialLogicOperation.setDelFlag("0");
        Page<PreferentialLogicOperation> page = preferentialLogicOperationService.find(new Page<PreferentialLogicOperation>(request, response), preferentialLogicOperation); 
        model.addAttribute("page", page);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(PreferentialLogicOperation preferentialLogicOperation, Model model) {
		model.addAttribute("preferentialLogicOperation", preferentialLogicOperation);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(PreferentialLogicOperation preferentialLogicOperation, Model model, RedirectAttributes redirectAttributes) {
		String result="";
		try {
			preferentialLogicOperationService.save(preferentialLogicOperation);
			result="1";
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
		model.addAttribute("preferentialLogicOperation", preferentialLogicOperationService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		model.addAttribute("preferentialLogicOperation", preferentialLogicOperationService.getByUuid(uuid));
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(PreferentialLogicOperation preferentialLogicOperation, Model model, RedirectAttributes redirectAttributes) {
		String result="2";
		try {
			preferentialLogicOperationService.update(preferentialLogicOperation);
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
					preferentialLogicOperationService.removeByUuid(uuid);
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
