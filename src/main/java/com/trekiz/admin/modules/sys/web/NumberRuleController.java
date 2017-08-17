package com.trekiz.admin.modules.sys.web;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.trekiz.admin.common.utils.ChineseToEnglish;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.sys.entity.NumberRule;
import com.trekiz.admin.modules.sys.entity.RuleParameter;
import com.trekiz.admin.modules.sys.service.NumberRuleService;

/**
 * 编号规则Controller
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/numberRule")
public class NumberRuleController extends BaseController {

	@Autowired
	private NumberRuleService numberRuleService;
	
	
	/**
     * 获取编号规则列表
     * @param currency
     * @return
     */
	@RequestMapping(value = {"list", ""})
	public String list(Model model,HttpServletRequest request, HttpServletResponse response) {

		List<NumberRule> list = numberRuleService.findAllNumberRule();
		model.addAttribute("list",list);
		
		return "modules/sys/numberRuleList";
	}
    
	/**
	 * 展现用于添加或者修改编号规则页面
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value={"show"})
	public String showNumberRule(Model model,HttpServletRequest request, HttpServletResponse response) {
		
		List<RuleParameter> list = numberRuleService.findAllParam();
		model.addAttribute("paraList",list);
		
		String strId = request.getParameter("id");
		if(!StringUtils.isEmpty(strId)){
			NumberRule numberRule = numberRuleService.findOne(Long.parseLong(strId));
			model.addAttribute("numberRule", numberRule);
		}
		
		return "modules/sys/numberRule";
	}
	
	
	/**
	 * 添加或者修改操作
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value={"save"})
	@ResponseBody
	public String saveOrupdate(Model model,HttpServletRequest request, HttpServletResponse response) {
		
		String strId = request.getParameter("id").trim();
		String numberValue = request.getParameter("numberValue");
		
		NumberRule numberRule = new NumberRule();
		//修改操作
		if(!StringUtils.isEmpty(strId)){
			numberRule = numberRuleService.findOne(Long.parseLong(strId));
		}
		//添加操作
		else{
			numberRule.setNumberType(request.getParameter("numberType"));
			numberRule.setMarkName(request.getParameter("markName"));
		}
		numberRule.setNumberValue(numberValue);

		numberRuleService.saveOrupdate(numberRule);
		
		return "success";
	}
	
	/**
	 * 判断编号类型是否重复，如果不重复返回编号类型每个汉字的首字母
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value={"checkIsExist"})
	@ResponseBody
	public long checkIsExist(HttpServletRequest request, HttpServletResponse response){
		String numberType = request.getParameter("numberType").trim();	//编号类型
		return numberRuleService.checkIsExist(numberType);
	}
	
	
	/**
	 * 判断编号类型是否重复，如果不重复返回编号类型每个汉字的首字母
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value={"createMarkName"})
	@ResponseBody
	public String createMarkName(HttpServletRequest request, HttpServletResponse response){

		String numberType = request.getParameter("numberType").trim();	//编号类型
		
		//获取每个中文的首字母
	    return ChineseToEnglish.getPinYinHeadChar(numberType);
	}
}
