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
@RequestMapping(value = "${adminPath}/agentType")
public class AgentTypeController {
	@Autowired
	private QuauqAgentInfoService agentInfoService;
	
	private String type="agent_type";
	
	/**
	 * 渠道类型列表
	 * @param request
	 * @param model
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@RequiresPermissions("agent:type:agent")
	@RequestMapping(value="list")
	public String getList(HttpServletRequest request,ModelMap model){
		Long companyId=UserUtils.getUser().getCompany().getId();
		List<Sysdefinedict> list = agentInfoService.getDefineDictByCompanyIdAndType(companyId, type);
		model.addAttribute("list", list);
		return "/agentToOffice/agentInfo/agentTypeList";
	}
	/**
	 * 添加渠道类型
	 * @param sysdefinedict
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@ResponseBody
	@RequestMapping(value="saveAgentType")
	public String saveAgentType(Sysdefinedict sysdefinedict){
		sysdefinedict.setCompanyId(UserUtils.getUser().getCompany().getId().intValue());
		sysdefinedict.setType(type);
		sysdefinedict.setValue(UuidUtils.generUuid());
		agentInfoService.saveSysDefineDict(sysdefinedict);
		return "success";
	}
	/**
	 * 删除渠道类型
	 * @param sysDefineDictId
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@ResponseBody
	@RequestMapping(value="delete")
	public boolean deleteAgentGrade(Long sysDefineDictId){
		boolean grade = agentInfoService.checkTypeOrGrade(sysDefineDictId.toString(), type);
		if(grade){
			return false;
		}else{
			agentInfoService.deleteSysDefineDict(sysDefineDictId);
			return true;
		}
	}
	@RequestMapping(value="save")
	public String save(){
		return "/agentToOffice/agentInfo/agentType";
	}
	/**
	 * 验证渠道类型是否重复
	 * @param label
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@ResponseBody
	@RequestMapping(value="check")
	public boolean check(String label){
		boolean check = agentInfoService.getDefineDictByLabel(UserUtils.getUser().getCompany().getId().intValue(), label, type);
		return check;
	}
}
