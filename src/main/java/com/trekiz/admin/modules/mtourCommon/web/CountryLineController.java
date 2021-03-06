/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.mtourCommon.web;

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
import com.trekiz.admin.modules.mtourCommon.entity.CountryLine;
import com.trekiz.admin.modules.mtourCommon.input.CountryLineInput;
import com.trekiz.admin.modules.mtourCommon.query.CountryLineQuery;
import com.trekiz.admin.modules.mtourCommon.service.CountryLineService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/countryLine")
public class CountryLineController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/mtourCommon/countryline/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/countryLine/list";
	protected static final String FORM_PAGE = "modules/mtourCommon/countryline/form";
	protected static final String SHOW_PAGE = "modules/mtourCommon/countryline/show";
	
	@Autowired
	private CountryLineService countryLineService;
	
	private CountryLine dataObj;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dataObj=countryLineService.getByUuid(uuid);
		}
	}
	
	@RequestMapping(value = "list")
	public String list(CountryLineQuery countryLineQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		countryLineQuery.setDelFlag("0");
        Page<CountryLine> page = countryLineService.find(new Page<CountryLine>(request, response), countryLineQuery); 
        model.addAttribute("page", page);
        model.addAttribute("countryLineQuery", countryLineQuery);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(CountryLineInput countryLineInput, Model model) {
		model.addAttribute("countryLineInput", countryLineInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(CountryLineInput countryLineInput, Model model, RedirectAttributes redirectAttributes) {
		
		String result="1";
		try {
			countryLineService.save(countryLineInput);
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
		model.addAttribute("countryLine", countryLineService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		CountryLine countryLine = countryLineService.getByUuid(uuid);
		CountryLineInput countryLineInput = new CountryLineInput(countryLine);
		model.addAttribute("countryLineInput", countryLineInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(CountryLineInput countryLineInput, Model model, RedirectAttributes redirectAttributes) {
		String result="2";
		try {
			BeanUtil.copySimpleProperties(dataObj, countryLineInput,true);
			countryLineService.update(dataObj);
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
				b = countryLineService.batchDelete(uuidArray);
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
