/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.pay.web;

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
import com.trekiz.admin.modules.pay.entity.PayDraft;
import com.trekiz.admin.modules.pay.input.PayDraftInput;
import com.trekiz.admin.modules.pay.query.PayDraftQuery;
import com.trekiz.admin.modules.pay.service.PayDraftService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/payDraft")
public class PayDraftController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/pay/paydraft/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/payDraft/list";
	protected static final String FORM_PAGE = "modules/pay/paydraft/form";
	protected static final String SHOW_PAGE = "modules/pay/paydraft/show";
	
	@Autowired
	private PayDraftService payDraftService;
	
	private PayDraft dataObj;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dataObj=payDraftService.getByUuid(uuid);
		}
	}
	
	@RequestMapping(value = "list")
	public String list(PayDraftQuery payDraftQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		payDraftQuery.setDelFlag("0");
        Page<PayDraft> page = payDraftService.find(new Page<PayDraft>(request, response), payDraftQuery); 
        model.addAttribute("page", page);
        model.addAttribute("payDraftQuery", payDraftQuery);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(PayDraftInput payDraftInput, Model model) {
		model.addAttribute("payDraftInput", payDraftInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(PayDraftInput payDraftInput, Model model, RedirectAttributes redirectAttributes) {
		
		String result="1";
		try {
			payDraftService.save(payDraftInput);
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
		model.addAttribute("payDraft", payDraftService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		PayDraft payDraft = payDraftService.getByUuid(uuid);
		PayDraftInput payDraftInput = new PayDraftInput(payDraft);
		model.addAttribute("payDraftInput", payDraftInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(PayDraftInput payDraftInput, Model model, RedirectAttributes redirectAttributes) {
		String result="2";
		try {
			BeanUtil.copySimpleProperties(dataObj, payDraftInput,true);
			payDraftService.update(dataObj);
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
					payDraftService.removeByUuid(uuid);
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
