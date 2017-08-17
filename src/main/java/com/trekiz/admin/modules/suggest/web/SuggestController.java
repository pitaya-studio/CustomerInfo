package com.trekiz.admin.modules.suggest.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.mapper.JsonMapper;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.geography.entity.SysGeography;
import com.trekiz.admin.modules.geography.service.SysGeographyService;


/**
 * 
 * @author majiancheng
 * @Time 2015-5-7
 */

@Controller
@RequestMapping(value = "${adminPath}/suggest")
public class SuggestController extends BaseController {
	
	@Autowired
	private SysGeographyService sysGeographyService;

	@ResponseBody
	@RequestMapping(value = "getCountryData")
	public String getCountryData(HttpServletResponse response) {
		SysGeography query = new SysGeography();
		query.setDelFlag("0");
		query.setLevel(1);
		
		List<SysGeography> sysGeographys = sysGeographyService.find(query);
		String result = JsonMapper.getInstance().toJson(sysGeographys);
		return result;
	}

}
