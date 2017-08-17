package com.trekiz.admin.agentToOffice.agentInfo.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.agentToOffice.agentInfo.service.CustomerTypeService;
import com.trekiz.admin.common.persistence.Page;

@Controller
@RequestMapping(value = "${adminPath}/customerType")
public class CustomerTypeController {

	@Autowired
	private CustomerTypeService customerTypeService;
	
	/**
	 * 获取客户类型分页列表
	 * @author wangyang
	 * @date 2016-08-09
	 * */
	@RequestMapping(value = "getCustomerTypeList", method = RequestMethod.GET)
	public String getCustomerTypeList(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		Page<Map<String, Object>> page = customerTypeService.getCustomerTypeList(new Page<Map<String, Object>>(request, response));
		model.addAttribute("page", page);
		return "/agentToOffice/agentInfo/customerTypeList";
	}
	
	/**
	 * 添加客户类型
	 * @author wangyang
	 * @date 2016-08-12
	 * */
	@ResponseBody
	@RequestMapping(value = "addCustomerType")
	public Map<String, Object> addCustomerType(String name, String remark) {
		
		Map<String, Object> data = new HashMap<String, Object>();
		Long index = customerTypeService.addCustomerType(name, remark);
		
		if (index > -1) {
			data.put("flag", true);
		} else {
			data.put("flag", false);
			data.put("msg", "客户类别添加失败！");
		}
		return data;
	}
	
	/**
	 * 删除客户类型
	 * @author wangyang
	 * @date 2016-08-12
	 * */
	@ResponseBody
	@RequestMapping(value = "delCustomerType")
	public Map<String, Object> delCustomerType(Long agentId) {
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		try {
			if (!customerTypeService.isUsed(agentId)) {
				customerTypeService.delCustomerType(agentId);
				data.put("flag", true);
			} else {
				data.put("flag", false);
				data.put("msg", "分类已被使用");
			}
		} catch (Exception e) {
			data.put("flag", false);
			data.put("msg", "客户分类删除失败");
			e.printStackTrace();
		}
		return data;
	}
	
	/**
	 * 在添加客户分类时，检验类型名称是否重复 ajax方法
	 * @author wangyang
	 * @date 2016-08-12
	 * */
	@ResponseBody
	@RequestMapping(value = "checkRepeat/{name}")
	public Map<String, Object> checkRepeat(@PathVariable String name) {
		
		Map<String, Object> data = new HashMap<String, Object>();
		
		try {
			boolean isRepeat = customerTypeService.checkRepeat(name);
			if (isRepeat) {
				data.put("flag", false);
				data.put("msg", "类型已存在");
			} else {
				data.put("flag", true);
			}
		} catch (Exception e) {
			data.put("flag", false);
			data.put("msg", "检验失败");
			e.printStackTrace();
		}
		return data;
	}
}
