package com.trekiz.admin.common.query.web;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.trekiz.admin.common.query.entity.SelectJson;
import com.trekiz.admin.common.query.service.ISelectService;
import com.trekiz.admin.common.query.utils.JSONUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;


@Controller
@RequestMapping(value="${adminPath}/query/select")
public class SelectController {

	@Autowired
    private ISelectService selectService;

	/**
	 * 获取部门信息
	 * @author wangyang
	 * */
	@ResponseBody
	@RequestMapping(value = "loadDepartment")
	public String loadDepartment(HttpServletResponse response){

		Long companyId = UserUtils.getUser().getCompany().getId();

		//SelectJson selectjson = new SelectJson();
		List<Map<String,Object>> data = selectService.loadDepartment(companyId);
		
		return JSONUtils.parseToJson(data);
	}


	/**
	 * 获取所有销售
	 * @author jinxin.gao
	 * @date 2016-04-08 16:36:30
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="loadAgentinfoList")
	public String loadAgentinfoList(){
		SelectJson selectJson = selectService.loadFindAllAgentinfo();
		//转json到ajax响应
		String json = JSONUtils.parseToJson(selectJson);
		return json;
	}

	
	/**
	 * 获取产品发布人和计调
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@ResponseBody
	@RequestMapping(value="getOperators")
	public  String	getOperators(){
		SelectJson selectJson = selectService.loadGetOperators(UserUtils.getUser().getCompany().getId());
		String json = JSONUtils.parseToJson(selectJson);
		return json;
	}

	/**
	 * 根据公司获取该公司下所有可发布信息的人
	 * @author chao.zhang@quauq.com
	 */
	@ResponseBody
	@RequestMapping(value="getAllUsersByCompanyId")
	public  String getAllUserByCompanyId(){
		SelectJson selectJson = selectService.loadAllUsers();
		String json = JSONUtils.parseToJson(selectJson);
		return json;
	}

	/**
	 * 查询审批发起人
	 * @return
	 * @author	shijun.liu
	 * @date	2016.04.09
     */
	@ResponseBody
	@RequestMapping(value="getReviewers")
	public String getReviewers(){
		SelectJson selectJson = selectService.loadReviewer();
		return JSONUtils.parseToJson(selectJson);
	}

	/**
	 * 查询该销售人员所属公司下的所有渠道商
	 * @author yudong.xu
	 * @return
     */
	@ResponseBody
	@RequestMapping(value = "getAgentsByCompanyId")
	public String getAgentsByCompanyId(){
		SelectJson selectJson = selectService.loadAgentInfoBySupplyId();
		String json = JSONUtils.parseToJson(selectJson);
		return json;
	}

	/**
	 * 查询该登陆销售员下的所有跟进的渠道商
	 * @author yudong.xu
	 * @return
     */
	@ResponseBody
	@RequestMapping(value = "getAgentsBySalerId")
	public String getAgentsBySalerId(){
		SelectJson selectJson = selectService.loadAgentInfoBySalerId();
		String json = JSONUtils.parseToJson(selectJson);
		return json;
	}
	
	/**
	 * 根据批发商id获得该批发商下所有的计调和计调主管
	 * @author chao.zhang@quauq.com
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getInnerOperator")
	public String getInnerOperator(){
		SelectJson operator = selectService.loadGetInnerOperator();
		String json = JSONUtils.parseToJson(operator);
		return json;
	}
	
	/*
	 * 在sys_user中获取下单人
	 * author xianglei.dong
	 * 2016-04-11
	 */
	@ResponseBody
	@RequestMapping(value="getSysPlaceOrderPersons")
	public String getSysPlaceOrderPersons() {
		SelectJson placePersons = selectService.loadSysUserPlaceOrderPersons();
		String json = JSONUtils.parseToJson(placePersons);
		return json;
	}
	
	/*
	 * 在多表联合查询中获取下单人
	 * author xianglei.dong
	 * 2016-04-11
	 */
	@ResponseBody
	@RequestMapping(value="getUnionPlaceOrderPersons")
	public String getUnionPlaceOrderPersons() {
		SelectJson placePersons = selectService.loadUnionPlaceOrderPersons();
		String json = JSONUtils.parseToJson(placePersons);
		return json;
	}
	
	/*
	 * 根据公司id获取地接社
	 * @author xianglei.dong
	 */
	@ResponseBody
	@RequestMapping(value="getSupplierByCompanyId")
	public String getSupplierByCompanyId() {
		SelectJson suppliers = selectService.loadSupplier();
		String json = JSONUtils.parseToJson(suppliers);
		return json;
	}

	/*
	 * 获取切位渠道商
	 * @author yudong.xu
	 */
	@ResponseBody
	@RequestMapping(value="getStockAgentinfo")
	public String getStockAgentinfo() {
		SelectJson selectJson = selectService.loadStockAgentinfo();
		String json = JSONUtils.parseToJson(selectJson);
		return json;
	}

	/*
	 * 获取渠道商,根据人员的不同，显示不同的渠道商列表。经理，管理员，财务显示公司的渠道商。其他显示自己跟进的渠道商。
	 * @author yudong.xu
	 */
	@ResponseBody
	@RequestMapping(value="getAgents")
	public String getAgents() {
		SelectJson selectJson = selectService.loadAgents();
		String json = JSONUtils.parseToJson(selectJson);
		return json;
	}
	
	/**
	 * 查询批发商下的来款行名称
	 * @return
	 * @author chao.zhang@quauq.com
	 */
	@ResponseBody
	@RequestMapping(value="getFromBanks")
	public List<Map<String, String>> getFromBanks(){
		List<Map<String, String>> fromBanks = selectService.getFromBanks(UserUtils.getUser().getCompany().getId().intValue());
		return fromBanks;
	}
}
