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
import com.trekiz.admin.modules.pay.entity.PayFee;
import com.trekiz.admin.modules.pay.input.PayFeeInput;
import com.trekiz.admin.modules.pay.query.PayFeeQuery;
import com.trekiz.admin.modules.pay.service.PayFeeService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/payFee")
public class PayFeeController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/pay/payfee/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/payFee/list";
	protected static final String FORM_PAGE = "modules/pay/payfee/form";
	protected static final String SHOW_PAGE = "modules/pay/payfee/show";
	
	@Autowired
	private PayFeeService payFeeService;
	
	private PayFee dataObj;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dataObj=payFeeService.getByUuid(uuid);
		}
	}
	
	@RequestMapping(value = "list")
	public String list(PayFeeQuery payFeeQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		payFeeQuery.setDelFlag("0");
        Page<PayFee> page = payFeeService.find(new Page<PayFee>(request, response), payFeeQuery); 
        model.addAttribute("page", page);
        model.addAttribute("payFeeQuery", payFeeQuery);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(PayFeeInput payFeeInput, Model model) {
		model.addAttribute("payFeeInput", payFeeInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(PayFeeInput payFeeInput, Model model, RedirectAttributes redirectAttributes) {
		
		String result="1";
		try {
			payFeeService.save(payFeeInput);
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
		model.addAttribute("payFee", payFeeService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		PayFee payFee = payFeeService.getByUuid(uuid);
		PayFeeInput payFeeInput = new PayFeeInput(payFee);
		model.addAttribute("payFeeInput", payFeeInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(PayFeeInput payFeeInput, Model model, RedirectAttributes redirectAttributes) {
		String result="2";
		try {
			BeanUtil.copySimpleProperties(dataObj, payFeeInput,true);
			payFeeService.update(dataObj);
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
				b = payFeeService.batchDelete(uuidArray);
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
