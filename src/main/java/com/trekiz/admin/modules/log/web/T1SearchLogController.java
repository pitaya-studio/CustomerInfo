package com.trekiz.admin.modules.log.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.modules.log.service.T1SearchLogService;

@Controller
@RequestMapping(value = "${adminPath}/search/log")
public class T1SearchLogController {
	@Autowired
	private T1SearchLogService t1SearchLogService;
	
	@RequestMapping(value = "getLogList")
	public  String getLogList(HttpServletRequest request ,HttpServletResponse response,Model model){
		Page<Map<String,Object>> page = t1SearchLogService.getLogList(new Page<Map<String,Object>>(request,response));
		/*for(Map<String,Object> map  : page.getList()){
			if(map.get("input")==null){
				map.put("input", " ");
			}
			if(map.get("fromCity")==null){
				map.put("fromCity", " ");
			}
			if(map.get("target")==null){
				map.put("target", " ");
			}
			if(map.get("arrival")==null){
				map.put("arrival", " ");
			}
			if(map.get("supply")==null){
				map.put("supply", " ");
			}
			if(map.get("groupOpenDate")==null){
				map.put("groupOpenDate", " ");
			}
			if(map.get("days")==null){
				map.put("days", " ");
			}
			if(map.get("prices")==null){
				map.put("prices", " ");
			}
			if(map.get("seats")==null){
				map.put("seats", " ");
			}
		}*/
		model.addAttribute("page", page);
		return "agentToOffice/T1/log/pageHomeLog";
	}
}
