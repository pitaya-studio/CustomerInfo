/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.epriceDistribution.web;

import java.util.HashMap;
import java.util.List;
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
import com.trekiz.admin.modules.agent.entity.Agentinfo;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.epriceDistribution.entity.EstimatePriceDistribution;
import com.trekiz.admin.modules.epriceDistribution.input.EstimatePriceDistributionInput;
import com.trekiz.admin.modules.epriceDistribution.query.EstimatePriceDistributionQuery;
import com.trekiz.admin.modules.epriceDistribution.service.EstimatePriceDistributionService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/estimatePriceDistribution")
public class EstimatePriceDistributionController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/epriceDistribution/estimatepricedistribution/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/estimatePriceDistribution/list";
	protected static final String FORM_PAGE = "modules/epriceDistribution/estimatepricedistribution/form";
	protected static final String SHOW_PAGE = "modules/epriceDistribution/estimatepricedistribution/show";
	
	@Autowired
	private EstimatePriceDistributionService estimatePriceDistributionService;

	@Autowired
	private AgentinfoService agentinfoService;
	
	private EstimatePriceDistribution dataObj;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dataObj=estimatePriceDistributionService.getByUuid(uuid);
		}
	}
	
	@RequestMapping(value = "list")
	public String list(EstimatePriceDistributionQuery estimatePriceDistributionQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		estimatePriceDistributionQuery.setDelFlag("0");
        Page<EstimatePriceDistribution> page = estimatePriceDistributionService.find(new Page<EstimatePriceDistribution>(request, response), estimatePriceDistributionQuery); 
        // 获取询价客户列表（同行）,即渠道商列表
     	List<Agentinfo> agentInfoList = agentinfoService.findAllAgentinfo();
		model.addAttribute("agentInfoList", agentInfoList);
        model.addAttribute("page", page);
        model.addAttribute("estimatePriceDistributionQuery", estimatePriceDistributionQuery);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(EstimatePriceDistributionInput estimatePriceDistributionInput, Model model) {
		model.addAttribute("estimatePriceDistributionInput", estimatePriceDistributionInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(EstimatePriceDistributionInput estimatePriceDistributionInput, Model model, RedirectAttributes redirectAttributes) {
		
		String result="1";
		try {
			estimatePriceDistributionService.save(estimatePriceDistributionInput);
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
		model.addAttribute("estimatePriceDistribution", estimatePriceDistributionService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		EstimatePriceDistribution estimatePriceDistribution = estimatePriceDistributionService.getByUuid(uuid);
		EstimatePriceDistributionInput estimatePriceDistributionInput = new EstimatePriceDistributionInput(estimatePriceDistribution);
		model.addAttribute("estimatePriceDistributionInput", estimatePriceDistributionInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(EstimatePriceDistributionInput estimatePriceDistributionInput, Model model, RedirectAttributes redirectAttributes) {
		String result="2";
		try {
			BeanUtil.copySimpleProperties(dataObj, estimatePriceDistributionInput,true);
			estimatePriceDistributionService.update(dataObj);
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
					estimatePriceDistributionService.removeByUuid(uuid);
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
