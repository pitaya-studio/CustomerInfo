package com.trekiz.admin.modules.mobile.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.mobile.entity.CorrelationUser;
import com.trekiz.admin.modules.mobile.service.MobileUserService;

@Controller
@RequestMapping(value="${adminPath}/mobileUser")
public class MobileUserController {
	
	@Autowired
	private MobileUserService mobileUserService;

	@Autowired
	private AgentinfoService agentinfoService;
	/**
	 * 跳转页面
	 * @return
	 */
	@RequestMapping(value="mobileUserPage")
	public String getMobileUserPage(){
		
		return "agentToOffice/T2/mobileUserList";
	}
	/**
	 * 微信用户查询列表
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="mobileUserList/1")
	public Object getMobileUserList(HttpServletRequest request, HttpServletResponse response){
		//获取参数
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("telephone", request.getParameter("telephone"));		//手机号
		params.put("phone", request.getParameter("phone"));				//座机
		params.put("areaCode", request.getParameter("areaCode"));		//座机区号
		params.put("agentName", request.getParameter("agentName"));		//供应商名称
		params.put("name",  request.getParameter("name"));				//姓名
		params.put("wechatCode",  request.getParameter("wechatCode"));	//微信号
		//params.put("isMatch", mobileUser.getIsMatch());				//是否匹配
		params.put("pageSize", request.getParameter("pageSize"));
		params.put("pageNo", request.getParameter("pageNo"));
		params.put("type", request.getParameter("type"));				//关联状态
		
		//获取微信用户列表
		JSONObject result = mobileUserService.selectUserList(params);
		return result;
	}

	@RequestMapping(value="confirmMatchingPage")
	public String confirmMatchingPage(HttpServletRequest request, HttpServletResponse response, Model model){
		String agentId = request.getParameter("agentId");
		String mobileUserId = request.getParameter("mobileUserId");

		Map<String, Object> map = null;
		if(StringUtils.isNotBlank(agentId) && StringUtils.isNotBlank(mobileUserId)){
			map = mobileUserService.confirmMatchingPage(Long.parseLong(agentId), Long.parseLong(mobileUserId));
		}else{
			return "";
		}

		// 根据ID把渠道商查出来
		Agentinfo ai = agentinfoService.findAgentInfoById(Long.parseLong(agentId));

		// 渠道信息
		model.addAttribute("agentinfo", ai);

		// 1.页面展示信息
		model.addAttribute("userInfo", map);

		// 2.国家信息
		model.addAttribute("areaMap", agentinfoService.findCountryInfo());
		// 3.省信息
		if(StringUtils.isNotBlank(ai.getAgentAddress())){
			Map<String,String> addressProvinceMap = agentinfoService.findAreaInfo(Long.parseLong(ai.getAgentAddress()));//根据公司地址所属国家id查省
			model.addAttribute("addressProvinceMap",addressProvinceMap );
		}
		// 4.市信息
		if(ai.getAgentAddressProvince()!= null){
			Map<String,String> addressCityMap = agentinfoService.findAreaInfo(ai.getAgentAddressProvince());//根据公司地址所属省id查市
			model.addAttribute("addressCityMap",addressCityMap );
		}

		return "modules/agent/confirmMatchingPage";
	}

	@RequestMapping(value = "cancelCorrelation")
	public String cancelCorrelation(HttpServletRequest request, HttpServletResponse response){

		return "redirect:" + Global.getAdminPath() + "/mobileUser/mobileUserPage";
	}

	@ResponseBody
	@RequestMapping(value = "confirmCorrelation")
	public Object confirmCorrelation(CorrelationUser correlationUser, HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> result = new HashMap<>();
		try {
			result = mobileUserService.confirmMatch(correlationUser);
			result.put("result", true);
			result.put("msg", "账号关联成功");
			result.put("url", "/mobileUser/mobileUserPage");
		} catch (Exception e) {
			result.put("result", false);
			result.put("msg", "账号关联成功");
		}
		return result;
	}
	
	/**
	 * 删除微信账号
	 * @param mobileUserId
	 * @return
	 */
	@RequestMapping(value = "delMobileUser",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> delMobileUser(@RequestParam("mobileUserId")Long mobileUserId){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean del = mobileUserService.delMobileUser(mobileUserId);
		if(del){
			map.put("result", true);
			map.put("msg", "删除成功");
		}else{
			map.put("result", false);
			map.put("msg", "删除失败");
		}
		return map;
	}
	
	/**
	 * 解绑微信账号
	 * @param mobileUserId
	 * @return
	 */
	@RequestMapping(value = "unBoundMobileUser",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> unBoundMobileUser(@RequestParam("mobileUserId")Long mobileUserId){
		Map<String,Object> map = new HashMap<String,Object>();
		boolean del = mobileUserService.unBoundMobileUser(mobileUserId);
		if(del){
			map.put("result", true);
			map.put("msg", "解绑成功");
		}else{
			map.put("result", false);
			map.put("msg", "解绑失败");
		}
		return map;
	}
	
}
