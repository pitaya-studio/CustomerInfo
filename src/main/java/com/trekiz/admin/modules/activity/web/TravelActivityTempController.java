package com.trekiz.admin.modules.activity.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.utils.StringUtils;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.activity.entity.TravelActivity;
import com.trekiz.admin.modules.activity.service.ITravelActivityService;
import com.trekiz.admin.modules.sys.entity.Country;
import com.trekiz.admin.modules.sys.service.SysIncreaseService;
import com.trekiz.admin.modules.sys.utils.CountryUtils;
import com.trekiz.admin.modules.sys.utils.DictUtils;
import com.trekiz.admin.modules.sys.utils.UserUtils;
/**
 * 旅游产品信息控制器
 * @author liangjingming
 *
 */
@Controller
@RequestMapping(value="${adminPath}/activity/manager")
public class TravelActivityTempController extends BaseController{

	@Autowired
	@Qualifier("travelActivitySyncService")
	private ITravelActivityService travelActivityService;
	@Autowired
	private SysIncreaseService sysIncreaseService;
	@ModelAttribute("menuId")
	protected Integer getMenuId(){
		return 196;
	}
	
	@RequiresPermissions("sys:addActivityTemp")
	@RequestMapping(value="formTemp")
	public String formTemp(@ModelAttribute TravelActivity travelActivity,HttpServletRequest request, HttpServletResponse response,Model model){
		
		List<Country> countryList = CountryUtils.getCountrys();
		//获取产品编号
		travelActivity.setActivitySerNum(sysIncreaseService.updateSysIncrease(UserUtils.getUser().getCompany().getName(),UserUtils.getUser().getCompany().getId(),
				null,Context.PRODUCT_NUM_TYPE));
		model.addAttribute("visaTypes", DictUtils.getDictList(Context.DICT_TYPE_VISATYPE));
		model.addAttribute("travelTypes", DictUtils.getValueAndLabelMap("travel_type",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("productLevels", DictUtils.getValueAndLabelMap("product_level",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("productTypes", DictUtils.getValueAndLabelMap("product_type",StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("trafficModes", DictUtils.getValueAndLabelMap(Context.TRAFFIC_MODE,StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("trafficNames", DictUtils.findUserDict(UserUtils.getUser().getCompany().getId(),"flight"));
		model.addAttribute("relevanceFlagId", DictUtils.getRelevanceFlag(StringUtils.toLong(UserUtils.getUser().getCompany().getId())));
		model.addAttribute("fromAreas", DictUtils.findUserDict(UserUtils.getUser().getCompany().getId(),"fromarea"));
        model.addAttribute("outAreas", DictUtils.findUserDict(UserUtils.getUser().getCompany().getId(),"outarea"));
        Map<Integer, String> payMap = DictUtils.getKeyIntMap(Context.PAY_TYPE);
		model.addAttribute("payTypes", payMap);
		model.addAttribute("countryList", countryList);
		return "modules/activity/spActivityForm";
	}
	
	/**
	 * 产品添加
	 * @param introduction 产品行程介绍
	 * @param costagreement 自费补充协议
	 * @param otheragreement 其他补充协议
	 * @param otherfile 其他文件
	 * @param travelActivity
	 * @param groupOpenDateBegin
	 * @param groupCloseDateEnd
	 * @param request
	 * @param response
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value="saveTemp")
	public String save(TravelActivity travelActivity,String groupOpenDateBegin,String groupCloseDateEnd,HttpServletRequest request, HttpServletResponse response,
			Model model, RedirectAttributes redirectAttributes){
		return travelActivityService.save(travelActivity, groupOpenDateBegin, groupCloseDateEnd, request, response, model, redirectAttributes, true);
	}
	
}