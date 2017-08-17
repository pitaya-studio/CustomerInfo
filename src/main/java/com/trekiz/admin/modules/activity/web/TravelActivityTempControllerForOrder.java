package com.trekiz.admin.modules.activity.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.agent.service.AgentinfoService;
import com.trekiz.admin.modules.sys.entity.DepartmentCommon;
import com.trekiz.admin.modules.sys.entity.User;
import com.trekiz.admin.modules.sys.service.DepartmentService;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 补单产品控制器
 * @author ZhengZiyu
 *
 */
@Controller
@RequestMapping(value="${adminPath}/activity/managerforOrder")
public class TravelActivityTempControllerForOrder extends BaseController {
	
	@Autowired
	@Qualifier("travelActivitySyncService")
	private ITravelActivityService travelActivityService;
	@Autowired
    private AgentinfoService agentinfoService;
	@Autowired
    private DepartmentService departmentService;
	
	@ModelAttribute("menuId")
	protected Integer getMenuId(){
		return 198;
	}
	
	@RequiresPermissions("sys:orderTemp")
	@RequestMapping(value="listTemp")
	public String list(@ModelAttribute TravelActivity travelActivity,HttpServletRequest request, HttpServletResponse response,Model model) {

		User user = UserUtils.getUser();
		Long companyId = user.getCompany().getId();
		//按部门展示
		DepartmentCommon common = departmentService.setDepartmentPara("bookOrder", model);
        
		String settlementAdultPriceStart = request.getParameter("settlementAdultPriceStart");
		String settlementAdultPriceEnd = request.getParameter("settlementAdultPriceEnd");
		String wholeSalerKey = request.getParameter("wholeSalerKey");
		String remainDays = request.getParameter("remainDays");
		if(StringUtils.isNotBlank(remainDays))
			travelActivity.setRemainDays(Integer.parseInt(remainDays));
		else
			travelActivity.setRemainDays(null);
        travelActivity.setAcitivityName(wholeSalerKey);
		String agentId = request.getParameter("agentId");//获取渠道id
		travelActivity.setActivityStatus(2);//只查询上架产品
		travelActivity.setIsAfterSupplement(1);
		Page<TravelActivity> page = travelActivityService.findTravelActivity(new Page<TravelActivity>(request, response), travelActivity, 
				settlementAdultPriceStart, settlementAdultPriceEnd, common);

		model.addAttribute("agentId", agentId);
		model.addAttribute("travelActivity", travelActivity);
        model.addAttribute("page", page);
        model.addAttribute("remainDays", remainDays);
        model.addAttribute("settlementAdultPriceStart", settlementAdultPriceStart);
        model.addAttribute("settlementAdultPriceEnd", settlementAdultPriceEnd);
        model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type", companyId));
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level", companyId));
		model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type", companyId));
		model.addAttribute("trafficModes", DictUtils.getDicMap(Context.TRAFFIC_MODE));
		model.addAttribute("trafficNames", DictUtils.findUserDict(companyId, "flight"));
		model.addAttribute("fromAreas", DictUtils.findUserDict(companyId, "fromarea"));
		model.addAttribute("payTypes", DictUtils.getSysDicMap(Context.PAY_TYPE));
		model.addAttribute("activityDuration", travelActivity.getActivityDuration());
		model.addAttribute("agentinfoList", agentinfoService.findAllAgentinfo());
		String companyUuid = UserUtils.getUser().getCompany().getUuid();
		model.addAttribute("companyUuid",companyUuid);
		return "modules/activity/spActivityListForOrder";
	}
}
