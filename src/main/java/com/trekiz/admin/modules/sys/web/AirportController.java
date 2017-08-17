package com.trekiz.admin.modules.sys.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.sys.entity.AirportInfo;
import com.trekiz.admin.modules.sys.entity.Area;
import com.trekiz.admin.modules.sys.service.AirportService;
import com.trekiz.admin.modules.sys.service.AreaService;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * 交通信息维护-机场信息Controller
 * 
 * @author Li Xinyun
 * @version 2014-12-27
 */
@Controller
@RequestMapping(value = "${adminPath}/sys/airport")
public class AirportController extends BaseController {

	@Autowired
	private AirportService airportService;

	@Autowired
	private AreaService areaService;

	/**
	 * 获取机场列表分页信息
	 * 
	 * @param currency
	 * @return
	 */
	@RequiresPermissions("common:mtour:menu")
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping(value = { "list/{area}" })
	public String list(@PathVariable String area, Model model,
			HttpServletRequest request, HttpServletResponse response) {
		Page page = airportService.searchAirportPage(
				new Page(request, response), UserUtils.getUser().getCompany()
						.getId(), area);
		List<List<Object>> list = (List<List<Object>>) page.getList();
		String airportIds = airportService.getIdFromListObj(list);

		model.addAttribute("page", page);
		model.addAttribute("airportIds", airportIds);
		model.addAttribute("area", area);

		return "modules/sys/airportList";
	}
	
	/**
	 * 由国家名称取得国家ID
	 * @param countryName 国家名称
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
	 * 由国家ID取得相应的城市列表
	 * @param countryId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getCitysByCountryId", method = RequestMethod.GET)
	public Object getCitysByCountryId(@RequestParam String countryId) {
		Object result = areaService.findCityByCountryId(countryId);
		return result;
	}

	/**
	 * 检查相同区域下的同名机场名称
	 * @param area
	 * @param countryId
	 * @param cityId
	 * @param airportNames
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkSameAirPortName", method = RequestMethod.POST)
	public Object checkSameAirPortName(@RequestParam int area,
			@RequestParam Long countryId, @RequestParam Long cityId,
			@RequestParam String airportNames) {
		Map<String, String> data = new HashMap<String, String>();

		String[] airportNameArr = airportNames.split("\\|");
		for (int i = 0; i < airportNameArr.length; i++) {
			List<AirportInfo> airportList = airportService.checkSameName(
					UserUtils.getUser().getCompany().getId(), area, countryId,
					cityId, airportNameArr[i]);
			if (airportList != null && airportList.size() > 0) {
				data.put("flag", "error");
				data.put("sameName", airportNameArr[i]);
				return data;
			}
		}

		data.put("flag", "success");
		return data;
	}
	
	/**
	 * 检查相同区域下的同名机场三字码
	 * 
	 * @param area
	 * @param countryId
	 * @param airportNames
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "checkSameAirportCode", method = RequestMethod.POST)
	public Object checkSameAirportCode(@RequestParam Long airportId,
			@RequestParam int area, @RequestParam Long countryId,
			@RequestParam String airportCodes) {
		Map<String, String> data = new HashMap<String, String>();

		String[] airportCodeArr = airportCodes.split("\\|");
		for (int i = 0; i < airportCodeArr.length; i++) {
			List<AirportInfo> airportList = airportService.checkSameName(
					UserUtils.getUser().getCompany().getId(), 
					airportCodeArr[i],airportId);
			if (airportList != null && airportList.size() > 0) {
				data.put("flag", "error");
				data.put("sameName", airportCodeArr[i]);
				return data;
			}
		}

		data.put("flag", "success");
		return data;
	}

	/**
	 * 保存机场信息
	 * @param area
	 * @param countryId
	 * @param cityId
	 * @param airportNames
	 * @param airportCodes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@RequestParam Long airportId,
			@RequestParam Integer area, @RequestParam Long countryId,
			@RequestParam Long cityId, @RequestParam String airportNames,
			@RequestParam String airportCodes) {

		String[] airportNameArr = airportNames.split("\\|");
		String[] airportCodeArr = airportCodes.split("\\|");

		AirportInfo airportInfo = null;
		for (int i = 0; i < airportNameArr.length; i++) {
			if (airportId == -1) {
				airportInfo = new AirportInfo();
			} else {
				airportInfo = airportService.getAirportInfo(airportId);
			}
			
			// 所属批发商
			airportInfo.setCompanyId(UserUtils.getUser().getCompany().getId());
			// 区域
			airportInfo.setArea(area);
			// 国家ID
			airportInfo.setCountryId(countryId);
			// 城市ID
			airportInfo.setCityId(cityId);
			// 机场名称
			airportInfo.setAirportName(airportNameArr[i]);
			// 机场三字码
			airportInfo.setAirportCode(airportCodeArr[i]);
			// 启用状态
			airportInfo.setUserMode(new Integer(AirportInfo.SHOW));
			airportService.save(airportInfo);
		}

		return "[{\"result\":\"ok\"}]";
	}
	
	/**
	 * 保存显示状态信息
	 * @param request
	 * @param currencyIds
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "saveDispStatus")
	public String saveDispStatus(HttpServletRequest request,String area,String airportIds,RedirectAttributes redirectAttributes){
		String[] checkedIdArr = request.getParameterValues("activityId");
		if (checkedIdArr == null) {
			checkedIdArr = new String[0];
		}
		String[] airportIdArr = airportIds.split(",");
		
		List<String> cancelIdLst = new ArrayList<String>();
		
		for(String airportId : airportIdArr){
			boolean existFlg = false;
			for(String checkedId : checkedIdArr){
				if(airportId.equals(checkedId)){
					existFlg = true;
					break;
				}
			}
			
			if(!existFlg){
				cancelIdLst.add(airportId);
			}
		}
		
		List<Integer> airportIdlist = airportService.findDispAirportInfo(area);
		for(String currencyId : checkedIdArr){
			
			if(!airportIdlist.contains(new Integer(currencyId))){
				airportIdlist.add(new Integer(currencyId));
			}
		}
		
		for(String cancelId : cancelIdLst) {
			if(airportIdlist.contains(new Integer(cancelId))){
				airportIdlist.remove(new Integer(cancelId));
			}
		}
		
		if (airportIdlist.size() == 0) {
			addMessage(redirectAttributes, "显示的机场信息至少需要选择一个");
			return "redirect:"+Global.getAdminPath()+"/sys/airport/list/" + area;
		}
		
		airportService.saveDispStatus(airportIdArr,checkedIdArr);
		addMessage(redirectAttributes, "保存成功");

		return "redirect:"+Global.getAdminPath()+"/sys/airport/list/" + area;
	}
	
	/**
	 * 删除机场信息
	 * 
	 * @param request
	 * @param area
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = "delete")
	public String delete(HttpServletRequest request, Long airportId,
			String area, RedirectAttributes redirectAttributes) {

		try {
			airportService.deleteAirport(airportId);
			addMessage(redirectAttributes, "删除成功");
		} catch (Exception e) {
			addMessage(redirectAttributes, "删除失败");
		}

		return "redirect:" + Global.getAdminPath() + "/sys/airport/list/"
				+ area;
	}
	
	/**
	 * 由机场ID取得机场信息
	 * 
	 * @param airportId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "getAirportInfoById", method = RequestMethod.GET)
	public Object getAirportInfoById(@RequestParam Long airportId) {
		Map<String, Object> data = new HashMap<String, Object>();
		AirportInfo airportInfo = airportService.getAirportInfo(airportId);
		data.put("airportInfo", airportInfo);
		return data;
	}
}
