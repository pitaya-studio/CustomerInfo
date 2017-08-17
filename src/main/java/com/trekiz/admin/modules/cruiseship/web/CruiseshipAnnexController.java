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
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipAnnex;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipAnnexInput;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipAnnexQuery;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipAnnexService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/cruiseshipAnnex")
public class CruiseshipAnnexController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/cruiseship/cruiseshipannex/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/cruiseshipAnnex/list";
	protected static final String FORM_PAGE = "modules/cruiseship/cruiseshipannex/form";
	protected static final String SHOW_PAGE = "modules/cruiseship/cruiseshipannex/show";
	
	@Autowired
	private CruiseshipAnnexService cruiseshipAnnexService;
	
	private CruiseshipAnnex dataObj;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dataObj=cruiseshipAnnexService.getByUuid(uuid);
		}
	}
	
	@RequestMapping(value = "list")
	public String list(CruiseshipAnnexQuery cruiseshipAnnexQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		cruiseshipAnnexQuery.setDelFlag("0");
        Page<CruiseshipAnnex> page = cruiseshipAnnexService.find(new Page<CruiseshipAnnex>(request, response), cruiseshipAnnexQuery); 
        model.addAttribute("page", page);
        model.addAttribute("cruiseshipAnnexQuery", cruiseshipAnnexQuery);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(CruiseshipAnnexInput cruiseshipAnnexInput, Model model) {
		model.addAttribute("cruiseshipAnnexInput", cruiseshipAnnexInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(CruiseshipAnnexInput cruiseshipAnnexInput, Model model, RedirectAttributes redirectAttributes) {
		
		String result="1";
		try {
			cruiseshipAnnexService.save(cruiseshipAnnexInput);
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
		model.addAttribute("cruiseshipAnnex", cruiseshipAnnexService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		CruiseshipAnnex cruiseshipAnnex = cruiseshipAnnexService.getByUuid(uuid);
		CruiseshipAnnexInput cruiseshipAnnexInput = new CruiseshipAnnexInput(cruiseshipAnnex);
		model.addAttribute("cruiseshipAnnexInput", cruiseshipAnnexInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(CruiseshipAnnexInput cruiseshipAnnexInput, Model model, RedirectAttributes redirectAttributes) {
		String result="2";
		try {
			BeanUtil.copySimpleProperties(dataObj, cruiseshipAnnexInput,true);
			cruiseshipAnnexService.update(dataObj);
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
				b = cruiseshipAnnexService.batchDelete(uuidArray);
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
