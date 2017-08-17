package com.trekiz.admin.modules.sys.web;

import java.io.UnsupportedEncodingException;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.sys.entity.AirlineInfo;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.service.AirlineInfoService;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 交通信息维护-航空公司Controller
 * 
 * @author Li Xinyun
 * @version 2014-12-31
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/airline")
public class AirlineController extends BaseController {

	@Autowired
	private AirlineInfoService airlineInfoService;
	
	@Autowired
	private AreaService areaService;

	@RequiresPermissions("common:mtour:menu")
	@RequestMapping(value = { "list/{area}" })
	public String list(@PathVariable String area, Model model,
			HttpServletRequest request, HttpServletResponse response) {

		String airlineNameKeyword = request.getParameter("airlineNameKeyword");
		if(StringUtils.isNotEmpty(airlineNameKeyword)) {
			airlineNameKeyword = airlineNameKeyword.trim();
		}

		@SuppressWarnings("rawtypes")
        Page page = airlineInfoService.searchAirlinePage(new Page(request,
				response), UserUtils.getUser().getCompany().getId(), area,
				airlineNameKeyword);
		@SuppressWarnings("unchecked")
        List<List<Object>> list = (List<List<Object>>) page.getList();
		String airlineIds = airlineInfoService.getIdFromListObj(list);

		model.addAttribute("page", page);
		model.addAttribute("airlineIds", airlineIds);
		model.addAttribute("area", area);
		model.addAttribute("airlineNameKeyword", airlineNameKeyword);

		return "modules/sys/airlineList";
	}

	@RequestMapping(value = { "form/{area}" })
	public String form(@PathVariable String area) {
		return "modules/sys/airlineForm";
	}

	@RequestMapping(value = "save")
	public String save(HttpServletRequest request) {

		String val1 = request.getParameter("input_" + 1);
		String val2 = request.getParameter("input_" + 2);

//		System.out.println(val1);
//		System.out.println(val2);

		return "modules/sys/airlineForm";
	}

	/**
	 * 保存显示状态信息
	 * 
	 * @param request
	 * @param currencyIds
	 * @param redirectAttributes
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(value = "saveDispStatus")
	public String saveDispStatus(HttpServletRequest request, String area,String airlineIds, Model model) {
		String airlineNameKeyword = request.getParameter("airlineNameKeyword");
		//获取当前页面所勾选的数据
		String[] checkedIdArr = request.getParameterValues("activityId");
		if (checkedIdArr == null) {
			checkedIdArr = new String[0];
		}
		//获取当前页面所有的数据
		String[] airlineIdArr = airlineIds.split(",");
		//获取当前页面为勾选的数据
		List<String> cancelIdLst = new ArrayList<String>();
		for(String airlineId : airlineIdArr){
			boolean existFlg = false;
			for(String checkedId : checkedIdArr){
				if(airlineId.equals(checkedId)){
					existFlg = true;
					break;
				}
			}
			if(!existFlg){
				cancelIdLst.add(airlineId);
			}
		}
		//获取所有的数据
		List<Integer> airlineIdlist = airlineInfoService.findDispAirlineInfoInfo(UserUtils.getUser().getCompany().getId(), area, airlineNameKeyword);
		
		/*
		     由于当前页面所勾选数据一定在所有数据里,无效代码注释   modify by hhx
		  for(String currencyId : checkedIdArr){
			if(!airlineIdlist.contains(new Integer(currencyId))){
				airlineIdlist.add(new Integer(currencyId));
			}
		}*/
		//如果所有数据均没有被勾选,则提示错误,页面重新进入
		for(String cancelId : cancelIdLst) {
			if(airlineIdlist.contains(new Integer(cancelId))){
				airlineIdlist.remove(new Integer(cancelId));
			}
		}
		if (airlineIdlist.size() == 0) {
			addMessage(model, "显示的航空公司至少需要选择一个,请重新选择");
			return "forward:" + Global.getAdminPath() + "/sys/airline/list/" + area;
		}
		//分别保存已勾选,为勾选数据
		airlineInfoService.saveDispStatus(airlineIdArr, checkedIdArr);
		addMessage(model, "保存成功");
		request.setAttribute("airlineNameKeyword", airlineNameKeyword);
		return "forward:" + Global.getAdminPath() + "/sys/airline/list/" + area;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/**
	 * 由国家名称取得国家ID
	 * 
	 * @param countryName
	 *            国家名称
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getCountryIdByName", method = RequestMethod.POST)
	public Object getCountryIdByName(@RequestParam String countryName) {
		Map<String, String> data = new HashMap<String, String>();

		Area country = areaService.findCountryByName(countryName);

		if (country != null) {
			data.put("flag", "success");
			data.put("countryId", country.getId().toString());
		} else {
			data.put("flag", "error");
		}

		return data;
	}

	/**
	 * 由区域类型取得国家列表
	 * 
	 * @param airportArea
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getCountryList", method = RequestMethod.GET)
	public Object getCountryList(@RequestParam String airportArea) {
		Object result = areaService.findCountrysNotChina();
		return result;
	}

	/**
	 * 检查相同区域下的同名航空公司名称
	 * 
	 * @param area
	 * @param countryId
	 * @param airlineName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkSameAirlineName", method = RequestMethod.POST)
	public Object checkSameAirlineName(@RequestParam int area,
			@RequestParam Long countryId, @RequestParam String airlineName) {
		Map<String, String> data = new HashMap<String, String>();

		List<AirlineInfo> airportList = airlineInfoService.checkSameName(
				UserUtils.getUser().getCompany().getId(), area, countryId,
				airlineName);
		if (airportList != null && airportList.size() > 0) {
			data.put("flag", "error");
			return data;
		}

		data.put("flag", "success");
		return data;
	}
	
	/**
	 * 检查相同区域下的同名航空公司二字码
	 * 
	 * @param area
	 * @param countryId
	 * @param airlineName
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkSameAirlineCode", method = RequestMethod.POST)
	public Object checkSameAirlineCode(@RequestParam int area,
			@RequestParam String airlineCode) {
		Map<String, String> data = new HashMap<String, String>();
		List<AirlineInfo> airportList = airlineInfoService.getByAirlineCode(UserUtils.getUser().getCompany().getId(),area,airlineCode);
		if (airportList != null && airportList.size() > 0) {
			data.put("flag", "error");
			return data;
		}

		data.put("flag", "success");
		return data;
	}

	/**
	 * 保存航空公司信息 add by hhx
	 * @param airlineinfo
	 * @param req
	 * @param resp
	 * @param red
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveForm")
	public Map<String, String> saveForm(HttpServletRequest req,HttpServletResponse resp) {
		Map<String, String> result = new HashMap<String, String>();
		try{
			airlineInfoService.saveAirlineInfo(req);
		} catch(Exception e){
			e.printStackTrace();
			result.put("msg", "系统发生异常");
			result.put("ret", "failure");
			return result;
		}
		result.put("msg", "保存成功");
		result.put("ret", "success");
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "checkSameSpace", method = RequestMethod.POST)
	public Object checkSameSpace(@RequestParam Integer area,
			@RequestParam Long countryId, @RequestParam String airlineName,
			@RequestParam String airlineCode,
			@RequestParam String spaceLevelArr,
			@RequestParam String spaceArr, @RequestParam String spaceLvlNum) {
		Map<String, String> data = new HashMap<String, String>();
		
		String[] spaceLvls = spaceLevelArr.split(",");
		String[] spaces = spaceArr.split(",");
		String[] shipGradeNumArr = spaceLvlNum.split(",");
		
		int shipGradeNumArrIndex = 0;
		int count = 0;
		
		for (int i = 0; i < spaces.length; i++) {
			
			boolean bFlag = airlineInfoService.checkSameSpace(UserUtils
					.getUser().getCompany().getId(), area, countryId,
					airlineName, airlineCode, spaceLvls[shipGradeNumArrIndex],
					spaces[i]);
			if(bFlag){
				data.put("flag", "error");
				data.put("msg", "舱位等级 = [ " + spaceLvls[shipGradeNumArrIndex]
						+ " ]下的舱位 = [ " + spaces[i] + " ]与已有数据重复");
				return data;
			}
			
			count = count + 1;
			if (count == Integer
					.parseInt(shipGradeNumArr[shipGradeNumArrIndex])) {
				shipGradeNumArrIndex = shipGradeNumArrIndex + 1;
				count = 0;
			}
		}

		return data;
	}
	
	/**
	 * 显示航空公司详情 add by hhx
	 * @param airlineCode
	 * @param area
	 * @return
	 */
	@RequestMapping(value = { "showDetail/{airlineCode}" })
	public ModelAndView showDetail(@PathVariable(value="airlineCode") String airlineCode,@RequestParam String area){
		Map<String, Map<String,List<Map<String,String>>>> airlineMap = airlineInfoService.getAirlineListGroupBySpaceLevel(airlineCode, getCompanyId(), area);
		ModelAndView mav = new ModelAndView();
		mav.addObject("airlineMap", airlineMap);
		mav.setViewName("modules/sys/airlineDetail");
		return mav;
	}
	
	/**
	 * 修改航空公司信息  add by hhx
	 * @param airlineCode
	 * @param area
	 * @return
	 */
	@RequestMapping(value = { "modify/{airlineCode}" })
	public ModelAndView modify(@PathVariable(value="airlineCode") String airlineCode,@RequestParam String area){
		Map<String, Map<String,List<Map<String,String>>>> airlineMap = airlineInfoService.getAirlineListGroupBySpaceLevel(airlineCode, getCompanyId(), area);
		ModelAndView mav = new ModelAndView();
		mav.addObject("airlineMap", airlineMap);
		mav.setViewName("modules/sys/airlineModify");
		return mav;
	}
	
	/**
	 * 修改航空公司信息 add by hhx
	 * @param req
	 * @param resp
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "saveModifyForm")
	public Map<String, String> saveModifyForm(HttpServletRequest req,HttpServletResponse resp) {
		Map<String, String> result = new HashMap<String, String>();
		try{
			airlineInfoService.deleteAirline(req);
			airlineInfoService.saveAirlineInfo(req);
		} catch(Exception e){
			e.printStackTrace();
			result.put("msg", "系统发生异常");
			result.put("ret", "failure");
			return result;
		}
		result.put("msg", "保存成功");
		result.put("ret", "success");
		return result;
	}
	
	/**
	 * 更新航空公司删除状态 add by hhx
	 * @param req
	 * @param resp
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "updateAirlineStatus")
	public Map<String, String> updateAirlineStatus(HttpServletRequest req,HttpServletResponse resp) {
		Map<String, String> result = new HashMap<String, String>();
		try{
			airlineInfoService.deleteAirline(req);
		} catch(Exception e){
			e.printStackTrace();
			result.put("msg", "系统发生异常");
			result.put("ret", "failure");
			return result;
		}
		result.put("msg", "删除成功");
		result.put("ret", "success");
		return result;
	}
}
