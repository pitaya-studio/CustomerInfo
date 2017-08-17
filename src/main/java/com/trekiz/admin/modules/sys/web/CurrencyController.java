/**
 *
 */
package com.trekiz.admin.modules.sys.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.sys.entity.Currency;
import com.trekiz.admin.modules.sys.service.CurrencyService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 币种Controller
 * @author jiachen
 * @version 2013-11-19
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/currency")
public class CurrencyController extends BaseController {

	@Autowired
	private CurrencyService currencyService;
	
	
	/**
     * 获取币种列表
     * @param currency
     * @return
     */
	@RequiresPermissions("common:mtour:menu")
	@RequestMapping(value = {"list", ""})
	public String list(Model model,HttpServletRequest request, HttpServletResponse response) {
		Long userId = UserUtils.getUser().getId();
		
		Page<Currency> page = currencyService.searchCurrencyPage(new Page<Currency>(request, response), UserUtils.getUser().getCompany().getId());
		model.addAttribute("page", page);
		
		List<Currency> list = page.getList();
		String currencyIds = currencyService.getIdFromListObj(list);
		model.addAttribute("currencyIds", currencyIds);
		//判断是否superadmin,展示不同的列表信息
		if(1 == userId.longValue()){
			return "modules/sys/superCurrencyList";
		}else{
			List<Currency> cList = currencyService.getMyAddCurrencyList();
			
			model.addAttribute("cList", cList);
			return "modules/sys/currencyList";
		}		
		
	}
    
    /**
     * 保存币种
     * @param currency
     * @param model
     * @return
     */
    @RequestMapping(value = "save")
    public String save(Currency currency, Model model) {
    	if(StringUtils.isNotBlank(currency.getCurrencyName())
    			&& StringUtils.isNotBlank(currency.getCurrencyMark()) 
    			&& currency.getCurrencyExchangerate() != null) {
    		
    		currencyService.createCurrency(currency);
    		return "redirect:"+Global.getAdminPath()+"/sys/currency";
    	}else{
    		model.addAttribute("currency", currency);
    		return "modules/sys/companyDictCurrencyForm";
    	}
    }
    /**
     * 保存币种
     * @param currency
     * @param model
     * @return
     */
    @RequestMapping(value = "modifyCurrency")
    public String modifyCurrency(Currency currency, Model model) {
    	if(StringUtils.isNotBlank(currency.getCurrencyName())
    			&& StringUtils.isNotBlank(currency.getCurrencyMark()) 
    			&& currency.getCurrencyExchangerate() != null) {
    		
    		currencyService.createCurrency(currency);
    		return "redirect:"+Global.getAdminPath()+"/sys/currency";
    	}else{
    		model.addAttribute("currency", currency);
    		return "modules/sys/companyDictCurrencyForm";
    	}
    }
    
    /**
     * 修改时回显
     * @param id
     * @param model
     * @return
     */
	@RequestMapping(value = "form")
	public String form(@RequestParam(required=false) Long id,Model model) {
		if(id != null) 
			model.addAttribute("currency", currencyService.findCurrency(id));
		
		return "modules/sys/currencyForm";
	}
	
	/**
	 * 删除币种信息
	 * 
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "delete")
	public String delete(HttpServletRequest request, Long id,
			RedirectAttributes redirectAttributes) {

		try {
			currencyService.delCurrency(id);
			addMessage(redirectAttributes, "删除成功");
		} catch (Exception e) {
			addMessage(redirectAttributes, "删除失败");
		}

		return "redirect:" + Global.getAdminPath() + "/sys/currency/list/";
	}
	
	/**
	 * 验证币种名称是否可以使用(重复)
	 * @param id
	 * @param currencyName
	 * @return 
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "check", method=RequestMethod.POST)
	public Object check(@RequestParam(required=false)Long id, String currencyName) throws IOException {
		Map<String,String> data = new HashMap<String, String>();
		if(id == null)
			id = 0l;
		
		String result = currencyService.check(currencyName, id);
		if("true".equals(result)){
			data.put("flag", "success");
		} else {
			data.put("flag", "error");
		}
		
		return data;
	}
	
	/**
	 * 验证币种符号是否可以使用(重复)
	 * @param id
	 * @param currencyName
	 * @return 
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "checkCurrencyMark", method=RequestMethod.POST)
	public Object checkCurrencyMark(@RequestParam(required=false)Long id, String currencyMark) throws IOException {
		Map<String,String> data = new HashMap<String, String>();
		if(id == null)
			id = 0l;
		
		String result = currencyService.checkCurrencyMark(currencyMark, id);
		if("true".equals(result)){
			data.put("flag", "success");
		} else {
			data.put("flag", "error");
		}
		
		return data;
	}
	
	/**
	 * 由ID取得相应的币种信息
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="findCurrencyById", method=RequestMethod.GET)
	public Object findCurrencyById(@RequestParam Long id) {	
		Map<String,Object> data = new HashMap<String, Object>();
		Currency resCurrency = currencyService.findCurrency(id);
		
		data.put("flag", "success");
		data.put("currency", resCurrency);
		return data;
	}
	
	/**
	 * 新增或修改币种信息
	 * @param id
	 * @param currencyName
	 * @param currencyMark
	 * @param currencyExchangerate
	 * @param convertCash
	 * @param convertForeign
	 * @param convertAbc
	 * @param convertLowest
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="save", method=RequestMethod.POST)
	public Object save(@RequestParam Long id,
			@RequestParam String currencyName,
			@RequestParam String currencyMark,
			@RequestParam BigDecimal currencyExchangerate,
			@RequestParam BigDecimal convertCash,
			@RequestParam BigDecimal convertForeign,
			@RequestParam BigDecimal convertAbc,
			@RequestParam Integer sort,
			@RequestParam String displayFlag,
			@RequestParam BigDecimal convertLowest) {
		
		Map<String,Object> data = new HashMap<String, Object>();
		
		Currency currency = new Currency();
		// id
		currency.setId(id);
		// 币种
		currency.setCurrencyName(currencyName);
		// 符号
		currency.setCurrencyMark(currencyMark);
		// 汇率
		currency.setCurrencyExchangerate(currencyExchangerate);
		// 现金收款
		currency.setConvertCash(convertCash);
		// 对公收款
		currency.setConvertForeign(convertForeign);
		// 中行折算价
		currency.setConvertAbc(convertAbc);
		// 最低汇率标准
		currency.setConvertLowest(convertLowest);
		if(null != sort){
			currency.setSort(sort);
		}
		
		currency.setDisplayFlag(displayFlag);
		currencyService.createCurrency(currency);
		
		if(currency.getId() != null){
			currency = currencyService.findCurrency(currency.getId());
			data.put("currency", currency);
		}
		
		data.put("flag", "success");
		
		return data;
	}
	
	/**
	 * 保存显示状态信息
	 * @param request
	 * @param currencyIds
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "saveDispStatus")
	public String saveDispStatus(HttpServletRequest request,String currencyIds,RedirectAttributes redirectAttributes){
		String[] checkedIdArr = request.getParameterValues("activityId");
		if (checkedIdArr == null) {
			checkedIdArr = new String[0];
		}
		String[] currencyIdArr = currencyIds.split(",");
		
		List<String> cancelIdLst = new ArrayList<String>();
		
		for(String currencyId : currencyIdArr){
			boolean existFlg = false;
			for(String checkedId : checkedIdArr){
				if(currencyId.equals(checkedId)){
					existFlg = true;
					break;
				}
			}
			
			if(!existFlg){
				cancelIdLst.add(currencyId);
			}
		}
		
		List<Integer> currencyIdlist = currencyService.findDispCurrencyInfo();
		for(String currencyId : checkedIdArr){
			
			if(!currencyIdlist.contains(new Integer(currencyId))){
				currencyIdlist.add(new Integer(currencyId));
			}
		}
		
		for(String cancelId : cancelIdLst) {
			if(currencyIdlist.contains(new Integer(cancelId))){
				currencyIdlist.remove(new Integer(cancelId));
			}
		}
		
		if (currencyIdlist.size() == 0) {
			addMessage(redirectAttributes, "显示的币种至少需要选择一个");
			return "redirect:"+Global.getAdminPath()+"/sys/currency/list";
		}
		
		currencyService.saveDispStatus(currencyIdArr,checkedIdArr);
		addMessage(redirectAttributes, "保存成功");

		return "redirect:"+Global.getAdminPath()+"/sys/currency/list?pageNo="+request.getParameter("pageNo") + "&pageSize=" + request.getParameter("pageSize");
	}
	/**
     * 保存币种
     * @param currency
     * @param model
     * @return
     */
	@ResponseBody
	@RequestMapping(value="saveSuperCurrency", method=RequestMethod.POST)
	public Object saveSuperCurrency(@RequestParam Long id,
			@RequestParam String currencyName,
			@RequestParam String currencyMark,
			@RequestParam Integer sort,
			@RequestParam String displayFlag,
			@RequestParam String remark) {
		
		Map<String,Object> data = new HashMap<String, Object>();
		
		Currency currency = new Currency();
		// id
		currency.setId(id);
		// 币种
		currency.setCurrencyName(currencyName);
		// 符号
		currency.setCurrencyMark(currencyMark);
		//排序
		currency.setSort(sort);
		//备注
		currency.setRemark(remark);
		currency.setDisplayFlag(displayFlag);
		currencyService.createCurrency(currency);
		
		if(currency.getId() != null){
			currency = currencyService.findCurrency(currency.getId());
			data.put("currency", currency);
		}
		
		data.put("flag", "success");
		
		return data;
	}
	@ResponseBody
	@RequestMapping(value = "currencyJson")
	public Map<String, Object> getCurrencyJson() {
		Map<String, Object> map = new HashMap<String, Object>();
		List<Currency> currencyList = currencyService
				.getMyCurrencyList();
		map.put("currencyList", currencyList);
		return map;
	}
	@RequestMapping(value = "viewCurrencyInfo")
	public String viewCurrencyInfo(Model model,Long id,HttpServletRequest request, HttpServletResponse response) {
		Currency currency = currencyService.findCurrency(id);
		model.addAttribute("currency", currency);
		if(1 == UserUtils.getUser().getCompany().getId().doubleValue()){
			return "modules/sys/viewSuperCurrencyInfo";
		}else{
			return "modules/sys/viewCurrencyInfo";
		}
		
	}
	@ResponseBody
	@RequestMapping(value = "saveSuperCurrencySort")
	public String saveSuperCurrencySort(Model model,Integer[]sort,Long[]currencyIds,HttpServletRequest request, HttpServletResponse response) {
		currencyService.saveSuperCurrencySort(sort,currencyIds);
		return "0";
	}
	
	/**
	 * 删除币种前，先判断是否引用了改币种
	 * @param model
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "checkCurrency")
	@ResponseBody
	public String checkCurrency(Model model,Integer id, HttpServletRequest request, HttpServletResponse response) {
		if(currencyService.checkCurrency(id)){
			// 引用了币种
			return "0";
		}
		// 没引用
		return "1";
	}
}
