package com.trekiz.admin.agentToOffice.officeCertification.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trekiz.admin.agentToOffice.officeCertification.service.WholesalersCertificationService;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.modules.sys.entity.Office;

@Controller
@RequestMapping(value="${adminPath}/wholesalers/certification")
public class WholesalersCertificationController {
	
	@Autowired
	private WholesalersCertificationService wholesalersCertificationService;
	
	/**
	 * 批发商认证查询列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @author chao.zhang
	 * @time 2016-09-13
	 */
	@RequestMapping(value="getOfficeList")
	public String getOfficeList(HttpServletRequest request,HttpServletResponse response,Model model){
		try {
			//接受查询参数：此项现今没用，根据需求隐藏了搜索项
			String groupCodeOrOfficeNameOrActivityName = request.getParameter("groupCodeOrOfficeNameOrActivityName");
			model.addAttribute("groupCodeOrOfficeNameOrActivityName", groupCodeOrOfficeNameOrActivityName);
			Page<Map<String,Object>> page = null;
			//根据需求分页改为默认每页显示20条
			if(StringUtils.isBlank(request.getParameter("pageSize"))){
				page = new Page<Map<String,Object>>(1, 20);
			}else{
				page = new Page<Map<String,Object>>(request, response);
			}
			//查询所有的批发商和每个批发商上架到T1的产品团期数
			page = wholesalersCertificationService.getAllOffice(page,groupCodeOrOfficeNameOrActivityName);
			model.addAttribute("page", page);
			//用于区别页面头部是QuauQ（等于1时是QUAUQ）
			model.addAttribute("ctxs", 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "agentToOffice/certification/officeCertification";
	}
	
	/**
	 * 批发商认证列表操作：查看详情
	 * @param request
	 * @param model
	 * @return
	 * @author chao.zhang
	 * @time 2016-09-18
	 */
	@RequestMapping(value="officeDetail")
	public String officeDetail(HttpServletRequest request,Model model){
		try {
			String companyId = request.getParameter("companyId");
			//若companyId不存在跳转到错误页面
			if(StringUtils.isBlank(companyId)){
				model.addAttribute(Context.ERROR_MESSAGE_KEY, "批发商ID不存在，数据异常");
				return Context.ERROR_PAGE;
			}
			//根据id查询office,获得批发商的详细信息
			Office office = wholesalersCertificationService.getOfficeDetail(Long.parseLong(companyId));
			//将";"换成","
			if(StringUtils.isNotBlank(office.getBusinessLicense())){
				office.setBusinessLicense(office.getBusinessLicense().replace(";", ","));
			}
			if(StringUtils.isNotBlank(office.getBusinessCertificate())){
				office.setBusinessCertificate(office.getBusinessCertificate().replace(";", ","));
			}
			if(StringUtils.isNotBlank(office.getCooperationProtocol())){
				office.setCooperationProtocol(office.getCooperationProtocol().replace(";", ","));
			}
			//查询销售名片
			List<Map<String,String>> docIds = wholesalersCertificationService.getDocIds(Long.parseLong(companyId));
			model.addAttribute("office", office);
			//用于区别页面头部是QuauQ（等于1时是QUAUQ）
			model.addAttribute("ctxs", 1);
			model.addAttribute("docIds", docIds);
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute(Context.ERROR_MESSAGE_KEY,"该批发商ID错误，数据异常");
			return Context.ERROR_PAGE;
		}
		return "agentToOffice/certification/officeDetail";
	}
}
