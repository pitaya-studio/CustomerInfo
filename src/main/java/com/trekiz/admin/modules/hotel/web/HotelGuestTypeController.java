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

import com.trekiz.admin.common.persistence.BaseEntity;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.modules.hotel.entity.HotelGuestType;
import com.trekiz.admin.modules.hotel.entity.SysGuestType;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trekiz.admin.modules.hotel.query.SysGuestTypeQuery;
import com.trekiz.admin.modules.hotel.service.*;
import com.trekiz.admin.modules.sys.utils.UserUtils;

/**
 * @author  trekiz
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/hotelGuestType")
public class HotelGuestTypeController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/hotel/hotelguesttype/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/hotelGuestType/list";
	protected static final String FORM_PAGE = "modules/hotel/hotelguesttype/form";
	protected static final String SHOW_PAGE = "modules/hotel/hotelguesttype/show";
	
	@Autowired
	private HotelGuestTypeService hotelGuestTypeService;
	@Autowired
	private SysGuestTypeService sysGuestTypeService;
	
	
	@RequestMapping(value = "list")
	public String list(HotelGuestType hotelGuestType, HttpServletRequest request, HttpServletResponse response, Model model) {
		hotelGuestType.setDelFlag("0");
		hotelGuestType.setWholesalerId(UserUtils.getUser().getCompany().getId().intValue());
        List<HotelGuestType> hotelGuestTypes = hotelGuestTypeService.find(hotelGuestType);
        model.addAttribute("hotelGuestTypes", hotelGuestTypes);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(HotelGuestType hotelGuestType, Model model) {
		SysGuestTypeQuery sysGuestTypeQuery = new SysGuestTypeQuery();
		sysGuestTypeQuery.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
		List<SysGuestType> typeList=sysGuestTypeService.find(sysGuestTypeQuery);
		model.addAttribute("typeList", typeList);
		model.addAttribute("hotelGuestType", hotelGuestType);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public Object save(HotelGuestType hotelGuestType, Model model, RedirectAttributes redirectAttributes) {
		
		
		Map<String,Object> datas = new HashMap<String, Object>();
		Long companyId = UserUtils.getUser().getCompany().getId();
		if(hotelGuestType != null && companyId != null) {
			hotelGuestType.setWholesalerId(companyId.intValue());
			
			hotelGuestTypeService.save(hotelGuestType);
			datas.put("message","1");
			
			//保存成功后，将住客类型的排序做累加操作
			hotelGuestTypeService.cumulationSortByWholesalerId(companyId.intValue());
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
		HotelGuestType hotelGuestType = hotelGuestTypeService.getByUuid(uuid);
		if(hotelGuestType == null || hotelGuestType.getWholesalerId() != UserUtils.getUser().getCompany().getId().intValue()) {
			return SHOW_PAGE;
		}
		model.addAttribute("hotelGuestType", hotelGuestType);
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		
		HotelGuestType hotelGuestType = hotelGuestTypeService.getByUuid(uuid);
		if(hotelGuestType == null || hotelGuestType.getWholesalerId() != UserUtils.getUser().getCompany().getId().intValue()) {
			return FORM_PAGE;
		}
		SysGuestTypeQuery sysGuestTypeQuery = new SysGuestTypeQuery();
		sysGuestTypeQuery.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
		List<SysGuestType> typeList=sysGuestTypeService.find(sysGuestTypeQuery);
		model.addAttribute("typeList", typeList);
		model.addAttribute("hotelGuestType", hotelGuestType);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public Object update(HotelGuestType hotelGuestType, Model model, RedirectAttributes redirectAttributes) {
		Map<String,Object> datas = new HashMap<String, Object>();
		
		HotelGuestType entity = null;
		if(hotelGuestType.getUuid() != null) {
			entity = hotelGuestTypeService.getByUuid(hotelGuestType.getUuid());
		} else {
			datas.put("message", "3");
			datas.put("error", "系统异常，请重新操作!");
			return datas;
		}
		entity.setSysGuestType(hotelGuestType.getSysGuestType());
		entity.setName(hotelGuestType.getName());
		entity.setUseRange(hotelGuestType.getUseRange());
		if(hotelGuestType.getStatus() == null) {
			entity.setStatus("0");
		} else {
			entity.setStatus(hotelGuestType.getStatus());
		}
		entity.setSort(hotelGuestType.getSort());
		entity.setDescription(hotelGuestType.getDescription());

		hotelGuestTypeService.update(entity);
		
		datas.put("message", "2");

		//保存成功后，将住客类型的排序做累加操作
		Long companyId = UserUtils.getUser().getCompany().getId();
		hotelGuestTypeService.cumulationSortByWholesalerId(companyId.intValue());
		
		return datas;
	}
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(String uuid) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(uuid)){
				hotelGuestTypeService.removeByUuid(uuid);
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
	@RequestMapping(value = "updateOrder")
	public Object updateOrder(String uuidAndSortsStr) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		Map<String, String> uuidAndSortMap = new HashMap<String, String>();
		boolean b = true;
		try {
			if(StringUtils.isEmpty(uuidAndSortsStr)) {
				
			} else {
				String[] records = uuidAndSortsStr.split(",");
				
				//组装数据
				if(records != null && records.length != 0) {
					for(String record : records) {
						String[] uuidAndSorts = record.split("-");
						uuidAndSortMap.put(uuidAndSorts[0], uuidAndSorts[1]);
					}
				}
				
				//更新酒店星级排序
				Set<String> uuids = uuidAndSortMap.keySet();
				if(uuids != null && !uuids.isEmpty()) {
					for(String uuid : uuids) {
						HotelGuestType hotelGuestType = hotelGuestTypeService.getByUuid(uuid);
						hotelGuestType.setSort(Integer.parseInt(uuidAndSortMap.get(uuid)));
						hotelGuestTypeService.update(hotelGuestType);
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
	

	@ResponseBody
	@RequestMapping(value = "check")
	public String check(String uuid, HttpServletRequest request) throws IOException {
		String name = request.getParameter("name");
		if(StringUtils.isEmpty(uuid))
		{
			uuid = "0";
		}

		if(hotelGuestTypeService.findIsExist(uuid, name, UserUtils.getUser().getCompany().getId())) {
			return "false";
		}
		return "true";
	}
	
	@ResponseBody
	@RequestMapping(value = "validateTravelerType")
	public boolean validateTravelerType(String uuid,HttpServletRequest request) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		String sysGuestType = request.getParameter("sysGuestType");
		boolean flag = hotelGuestTypeService.findIsExistBySysGuestType(uuid, sysGuestType, companyId.intValue());
		
		return !flag;
	}
	
}
