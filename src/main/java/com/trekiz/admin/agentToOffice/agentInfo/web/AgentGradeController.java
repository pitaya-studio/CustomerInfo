package com.trekiz.admin.agentToOffice.agentInfo.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.agentToOffice.agentInfo.service.QuauqAgentInfoService;
import com.trekiz.admin.common.utils.UuidUtils;
import com.trekiz.admin.modules.hotel.entity.Sysdefinedict;
import com.trekiz.admin.modules.sys.utils.UserUtils;

@Controller
@RequestMapping(value = "${adminPath}/agentGrade")
public class AgentGradeController {
	@Autowired
	private QuauqAgentInfoService agentInfoService;
	
	private String type="agent_grade";
	
	/**
	 * 渠道等级列表
	 * @param request
	 * @param model
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@RequiresPermissions("agent:grade:agent")
	@RequestMapping(value="list")
	public String getList(HttpServletRequest request,ModelMap model){
		Long companyId=UserUtils.getUser().getCompany().getId();
		List<Sysdefinedict> list = agentInfoService.getDefineDictByCompanyIdAndType(companyId, type);
		model.addAttribute("list", list);
		return "/agentToOffice/agentInfo/agentGradeList";
	}
	/**
	 * 添加渠道等级
	 * @param sysdefinedict
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="saveAgentGrade")
	public String saveAgentGrade(Sysdefinedict sysdefinedict){
		sysdefinedict.setCompanyId(UserUtils.getUser().getCompany().getId().intValue());
		sysdefinedict.setType(type);
		sysdefinedict.setValue(UuidUtils.generUuid());
		agentInfoService.saveSysDefineDict(sysdefinedict);
		return "success";
	}
	/**
	 * 删除渠道等级
	 * @param sysDefineDictId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="delete")
	public boolean deleteAgentGrade(Long sysDefineDictId){
		boolean check = agentInfoService.checkTypeOrGrade(sysDefineDictId.toString(), type);
		if(check){
			return false;
		}else{
			agentInfoService.deleteSysDefineDict(sysDefineDictId);
			return true;
		}
	}
	@RequestMapping(value="save")
	public String save(){
		return "/agentToOffice/agentInfo/agentLevel";
	}
	/**
	 * 验证渠道等级是否重复
	 * @param label
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="check")
	public boolean check(String label){
		boolean check = agentInfoService.getDefineDictByLabel(UserUtils.getUser().getCompany().getId().intValue(), label, type);
		return check;
	}
}
