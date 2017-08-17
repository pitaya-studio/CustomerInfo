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
import com.trekiz.admin.modules.cruiseship.entity.CruiseshipStockGroupRel;
import com.trekiz.admin.modules.cruiseship.input.CruiseshipStockGroupRelInput;
import com.trekiz.admin.modules.cruiseship.query.CruiseshipStockGroupRelQuery;
import com.trekiz.admin.modules.cruiseship.service.CruiseshipStockGroupRelService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/cruiseshipStockGroupRel")
public class CruiseshipStockGroupRelController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/cruiseship/cruiseshipstockgrouprel/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/cruiseshipStockGroupRel/list";
	protected static final String FORM_PAGE = "modules/cruiseship/cruiseshipstockgrouprel/form";
	protected static final String SHOW_PAGE = "modules/cruiseship/cruiseshipstockgrouprel/show";
	
	@Autowired
	private CruiseshipStockGroupRelService cruiseshipStockGroupRelService;
	
	private CruiseshipStockGroupRel dataObj;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dataObj=cruiseshipStockGroupRelService.getByUuid(uuid);
		}
	}
	
	@RequestMapping(value = "list")
	public String list(CruiseshipStockGroupRelQuery cruiseshipStockGroupRelQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		cruiseshipStockGroupRelQuery.setDelFlag("0");
        Page<CruiseshipStockGroupRel> page = cruiseshipStockGroupRelService.find(new Page<CruiseshipStockGroupRel>(request, response), cruiseshipStockGroupRelQuery); 
        model.addAttribute("page", page);
        model.addAttribute("cruiseshipStockGroupRelQuery", cruiseshipStockGroupRelQuery);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(CruiseshipStockGroupRelInput cruiseshipStockGroupRelInput, Model model) {
		model.addAttribute("cruiseshipStockGroupRelInput", cruiseshipStockGroupRelInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(CruiseshipStockGroupRelInput cruiseshipStockGroupRelInput, Model model, RedirectAttributes redirectAttributes) {
		
		String result="1";
		try {
			cruiseshipStockGroupRelService.save(cruiseshipStockGroupRelInput);
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
		model.addAttribute("cruiseshipStockGroupRel", cruiseshipStockGroupRelService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		CruiseshipStockGroupRel cruiseshipStockGroupRel = cruiseshipStockGroupRelService.getByUuid(uuid);
		CruiseshipStockGroupRelInput cruiseshipStockGroupRelInput = new CruiseshipStockGroupRelInput(cruiseshipStockGroupRel);
		model.addAttribute("cruiseshipStockGroupRelInput", cruiseshipStockGroupRelInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(CruiseshipStockGroupRelInput cruiseshipStockGroupRelInput, Model model, RedirectAttributes redirectAttributes) {
		String result="2";
		try {
			BeanUtil.copySimpleProperties(dataObj, cruiseshipStockGroupRelInput,true);
			cruiseshipStockGroupRelService.update(dataObj);
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
				b = cruiseshipStockGroupRelService.batchDelete(uuidArray);
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
