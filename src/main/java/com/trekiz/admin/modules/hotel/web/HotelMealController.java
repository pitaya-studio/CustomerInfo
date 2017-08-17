/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.modules.hotel.entity.HotelMeal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.trekiz.admin.modules.hotel.service.*;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/hotelMeal")
public class HotelMealController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/hotel/hotelmeal/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/hotelMeal/list";
	protected static final String FORM_PAGE = "modules/hotel/hotelmeal/form";
	protected static final String SHOW_PAGE = "modules/hotel/hotelmeal/show";
	
	@Autowired
	private HotelMealService hotelMealService;
	@ModelAttribute  
    public void populateModel( Model model,HttpServletRequest request) {
		model.addAttribute("type", Context.BaseInfo.HOTEL_MEAL);  
	}
	
	@RequestMapping(value = "list/{hotelUuid}")
	public String list(@PathVariable String hotelUuid,HotelMeal hotelMeal, HttpServletRequest request, HttpServletResponse response, Model model) {
		hotelMeal.setDelFlag("0");
		hotelMeal.setHotelUuid(hotelUuid);
		hotelMeal.setWholesalerId(Integer.parseInt(UserUtils.getCompanyIdForData().toString()));
        List<HotelMeal> hotelMeals = hotelMealService.find(hotelMeal);
        model.addAttribute("hotelMeals", hotelMeals);
        model.addAttribute("hotelUuid",hotelUuid);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form/{hotelUuid}")
	public String form(@PathVariable String hotelUuid,HotelMeal hotelMeal, Model model) {
		hotelMeal.setHotelUuid(hotelUuid);
		model.addAttribute("hotelMeal", hotelMeal);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public Object save(HotelMeal hotelMeal, Model model, RedirectAttributes redirectAttributes) {
		Map<String,Object> datas = new HashMap<String, Object>();
		
		if(hotelMeal != null) {
			hotelMeal.setWholesalerId(UserUtils.getCompanyIdForData().intValue());
			
			hotelMealService.save(hotelMeal);
			datas.put("message","1");
		} else {
			datas.put("message","3");
			datas.put("error", "系统异常，请重新操作!");
		}
		
		return datas;
	}
	
	@RequestMapping(value = "show/{uuid}")
	public String show(@PathVariable String uuid, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		model.addAttribute("hotelMeal", hotelMealService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		model.addAttribute("hotelMeal", hotelMealService.getByUuid(uuid));
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public Object update(HotelMeal hotelMeal, Model model, RedirectAttributes redirectAttributes) {
		Map<String,Object> datas = new HashMap<String, Object>();
		HotelMeal entity = null;
		if(!StringUtils.isEmpty(hotelMeal.getUuid())) {
			entity = hotelMealService.getByUuid(hotelMeal.getUuid());
		} else {
			datas.put("message","3");
			datas.put("error", "系统异常，请重新操作!");
			return datas;
		}
		
		entity.setMealName(hotelMeal.getMealName());
		entity.setMealType(hotelMeal.getMealType());
		entity.setSuitableNum(hotelMeal.getSuitableNum());
		entity.setPrice(hotelMeal.getPrice());
		entity.setSort(hotelMeal.getSort());
		entity.setMealDescription(hotelMeal.getMealDescription());
		
		hotelMealService.update(entity);
		datas.put("message","2");
		return datas;
	}
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(String uuid) {
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(uuid)){
				hotelMealService.removeByUuid(uuid);
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
	
	@ResponseBody
	@RequestMapping(value = "check/{hotelUuid}")
	public String check(@PathVariable String hotelUuid, String uuid, HttpServletRequest request) throws IOException {
		String mealName = request.getParameter("mealName");
		if(StringUtils.isEmpty(uuid))
		{
			uuid = "0";
		}
		
		HotelMeal hotelMeal = new HotelMeal();
		hotelMeal.setHotelUuid(hotelUuid);
		hotelMeal.setUuid(uuid);
		hotelMeal.setMealName(mealName);
		hotelMeal.setWholesalerId(UserUtils.getCompanyIdForData().intValue());
		
		if(hotelMealService.findIsExist(hotelMeal)) {
			return "false";
		}
		return "true";
	}
	
	@ResponseBody
	@RequestMapping(value = "updateOrder")
	public Object updateOrder(String uuidAndSortsStr) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		Map<String, String> uuidAndSortMap = new HashMap<String, String>();
		boolean b = true;
		try {
			if(StringUtils.isEmpty(uuidAndSortsStr)) {
				
			} else {
				String[] records = uuidAndSortsStr.split(";");
				
				//组装数据
				if(records != null && records.length != 0) {
					for(String record : records) {
						String[] uuidAndSorts = record.split(",");
						uuidAndSortMap.put(uuidAndSorts[0], uuidAndSorts[1]);
					}
				}
				
				//更新酒店餐型排序
				Set<String> uuids = uuidAndSortMap.keySet();
				if(uuids != null && !uuids.isEmpty()) {
					for(String uuid : uuids) {
						HotelMeal hotelMeal = hotelMealService.getByUuid(uuid);
						hotelMeal.setSort(Integer.parseInt(uuidAndSortMap.get(uuid)));
						hotelMealService.update(hotelMeal);
					}
				}
			}
		} catch (Exception e) {
			b = false;
			datas.put("message", "系统发生异常,请重新操作!");
			e.printStackTrace();
		}
		
		if(b){
			datas.put("result", "1");
		}else{
			datas.put("result", "0");
		}
		return datas;
	}
	
}
