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
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.modules.hotel.entity.HotelFloor;

import org.apache.commons.lang3.StringUtils;
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
@RequestMapping(value = "${adminPath}/hotelFloor")
public class HotelFloorController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/hotel/hotelfloor/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/hotelFloor/list";
	protected static final String FORM_PAGE = "modules/hotel/hotelfloor/form";
	protected static final String SHOW_PAGE = "modules/hotel/hotelfloor/show";
	
	@Autowired
	private HotelFloorService hotelFloorService;
	
	@Autowired
	private HotelRoomService hotelRoomService;
	
	@Autowired
	private HotelService hotelService;
	
	
	@RequestMapping(value = "list/{hotelUuid}/{hotelRoomUuid}")
	public String list(@PathVariable String hotelUuid, @PathVariable String hotelRoomUuid, HotelFloor hotelFloor, HttpServletRequest request, HttpServletResponse response, Model model) {
		hotelFloor.setDelFlag("0");
		hotelFloor.setWholesalerId(UserUtils.getCompanyIdForData().intValue());
		hotelFloor.setHotelUuid(hotelUuid);
		hotelFloor.setHotelRoomUuid(hotelRoomUuid);
		
		List<HotelFloor> hotelFloors = hotelFloorService.find(hotelFloor);
        model.addAttribute("hotelFloors", hotelFloors);
        model.addAttribute("hotelRoomUuid", hotelRoomUuid);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form/{hotelUuid}/{hotelRoomUuid}")
	public String form(@PathVariable String hotelUuid, @PathVariable String hotelRoomUuid, HotelFloor hotelFloor, Model model) {
		hotelFloor.setHotelRoomUuid(hotelRoomUuid);
		hotelFloor.setHotelUuid(hotelUuid);
		
		model.addAttribute("hotelFloor", hotelFloor);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public Object save(HotelFloor hotelFloor, Model model, RedirectAttributes redirectAttributes) {
		Map<String,Object> datas = new HashMap<String, Object>();
		
		if(hotelFloor != null && UserUtils.getCompanyIdForData() != null) {
			hotelFloor.setWholesalerId(UserUtils.getCompanyIdForData().intValue());
			
			hotelFloorService.save(hotelFloor);
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
		HotelFloor hotelFloor = hotelFloorService.getByUuid(uuid);
		if(hotelFloor.getWholesalerId()== null || hotelFloor.getWholesalerId() != UserUtils.getCompanyIdForData().intValue()) {
			return SHOW_PAGE;
		}
		model.addAttribute("hotelFloor", hotelFloor);
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		
		HotelFloor hotelFloor = hotelFloorService.getByUuid(uuid);
		if(hotelFloor.getWholesalerId()== null || hotelFloor.getWholesalerId() != UserUtils.getCompanyIdForData().intValue()) {
			return FORM_PAGE;
		}
		
		model.addAttribute("hotelFloor", hotelFloor);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public Object update(HotelFloor hotelFloor, Model model, RedirectAttributes redirectAttributes) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		
		HotelFloor entity = null;
		if(hotelFloor.getUuid() != null) {
			entity = hotelFloorService.getByUuid(hotelFloor.getUuid());
		} else {
			datas.put("message", "3");
			datas.put("error", "系统异常，请重新操作!");
			return datas;
		}
		
		entity.setFloorName(hotelFloor.getFloorName());
		entity.setDescription(hotelFloor.getDescription());
		entity.setSort(hotelFloor.getSort());
		
		hotelFloorService.update(entity);
		datas.put("message", "2");
		return datas;
	}
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(String uuid) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(uuid)){
				hotelFloorService.removeByUuid(uuid);
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
	@RequestMapping(value = "check/{hotelUuid}/{hotelRoomUuid}")
	public String check(@PathVariable String hotelUuid, @PathVariable String hotelRoomUuid, String uuid, HttpServletRequest request) throws IOException {
		String floorName = request.getParameter("floorName");
		if(StringUtils.isEmpty(uuid))
		{
			uuid = "0";
		}
		HotelFloor hotelFloor = new HotelFloor();
		hotelFloor.setHotelUuid(hotelUuid);
		hotelFloor.setHotelRoomUuid(hotelRoomUuid);
		hotelFloor.setUuid(uuid);
		hotelFloor.setFloorName(floorName);
		hotelFloor.setWholesalerId(UserUtils.getCompanyIdForData().intValue());
		
		if(hotelFloorService.findFloorNameIsExist(hotelFloor)) {
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
				
				//更新酒店楼层排序
				Set<String> uuids = uuidAndSortMap.keySet();
				if(uuids != null && !uuids.isEmpty()) {
					for(String uuid : uuids) {
						HotelFloor hotelFloor = hotelFloorService.getByUuid(uuid);
						hotelFloor.setSort(Integer.parseInt(uuidAndSortMap.get(uuid)));
						hotelFloorService.update(hotelFloor);
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
