/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.preferential.web;

import java.util.HashMap;
import java.util.List;
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
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.preferential.entity.PreferentialDict;
import com.trekiz.admin.modules.preferential.entity.PreferentialDictUnitRel;
import com.trekiz.admin.modules.preferential.entity.PreferentialLogicOperation;
import com.trekiz.admin.modules.preferential.entity.PreferentialUnit;
import com.trekiz.admin.modules.preferential.input.PreferentialDictInput;
import com.trekiz.admin.modules.preferential.service.PreferentialDictService;
import com.trekiz.admin.modules.preferential.service.PreferentialDictUnitRelService;
import com.trekiz.admin.modules.preferential.service.PreferentialLogicOperationService;
import com.trekiz.admin.modules.preferential.service.PreferentialUnitService;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/preferentialDict")
public class PreferentialDictController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/preferential/preferentialdict/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/preferentialDict/list";
	protected static final String FORM_PAGE = "modules/preferential/preferentialdict/form";
	protected static final String SHOW_PAGE = "modules/preferential/preferentialdict/show";
	
	@Autowired
	private PreferentialDictService preferentialDictService;
	
	@Autowired
	private PreferentialUnitService preferentialUnitService;
	
	@Autowired
	private PreferentialLogicOperationService preferentialLogicOperationService;
	
	@Autowired
	private PreferentialDictUnitRelService preferentialDictUnitRelService;
	
	private PreferentialDict dictInput;
	@ModelAttribute  
    public void populateModel( HttpServletRequest request, Model model){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dictInput=preferentialDictService.getByUuid(uuid);
		}
	}
	
	@RequestMapping(value = "list")
	public String list(PreferentialDict preferentialDict, HttpServletRequest request, HttpServletResponse response, Model model) {
		preferentialDict.setDelFlag("0");
        Page<PreferentialDict> page = preferentialDictService.find(new Page<PreferentialDict>(request, response), preferentialDict); 
        model.addAttribute("page", page);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(PreferentialDictInput preferentialDictInput, Model model) {
		PreferentialUnit preferentialUnit = new PreferentialUnit();
		preferentialUnit.setDelFlag("0");
		List<PreferentialUnit> unitList = preferentialUnitService.find(preferentialUnit);
		model.addAttribute("unitList", unitList);
		
		PreferentialLogicOperation preferentialLogicOperation = new PreferentialLogicOperation();
		preferentialLogicOperation.setDelFlag("0");
		List<PreferentialLogicOperation> logicList = preferentialLogicOperationService.find(preferentialLogicOperation);
		model.addAttribute("logicList", logicList);
		
		model.addAttribute("preferentialDict", preferentialDictInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(PreferentialDictInput preferentialDictInput, Model model, RedirectAttributes redirectAttributes) {
		String result="";
		try {
			result = preferentialDictService.saveObj(preferentialDictInput);
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
		model.addAttribute("preferentialDict", preferentialDictService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		
		PreferentialUnit preferentialUnit = new PreferentialUnit();
		preferentialUnit.setDelFlag("0");
		List<PreferentialUnit> unitList = preferentialUnitService.find(preferentialUnit);
		model.addAttribute("unitList", unitList);
		
		PreferentialLogicOperation preferentialLogicOperation = new PreferentialLogicOperation();
		preferentialLogicOperation.setDelFlag("0");
		List<PreferentialLogicOperation> logicList = preferentialLogicOperationService.find(preferentialLogicOperation);
		model.addAttribute("logicList", logicList);
		
		PreferentialDict dict = preferentialDictService.getByUuid(uuid);
		
		PreferentialDictUnitRel preferentialDictUnitRel = new PreferentialDictUnitRel();
		preferentialDictUnitRel.setDictUuid(uuid);
		preferentialDictUnitRel.setDelFlag("0");
		List<PreferentialDictUnitRel> dictUnitList=preferentialDictUnitRelService.find(preferentialDictUnitRel);
		
		PreferentialDictInput preferentialDictInput = new PreferentialDictInput(dict,dictUnitList);
		model.addAttribute("preferentialDictInput", preferentialDictInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(PreferentialDictInput preferentialDictInput, Model model, RedirectAttributes redirectAttributes) {
		
		String result="";
		try {
			result = preferentialDictService.updateObj(preferentialDictInput);
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
					preferentialDictService.removeByUuid(uuid);
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
