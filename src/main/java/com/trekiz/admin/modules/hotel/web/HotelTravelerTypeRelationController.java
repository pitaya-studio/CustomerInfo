/*
 * 思锐创途网络技术（北京）有限公司
 * 北京市朝阳区慈云寺北里210号远洋国际中心E座9层
 * 邮编：100025
 * 网址：www.trekiz.com
 */

package com.trekiz.admin.modules.hotel.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.trekiz.admin.common.config.Context;
import com.trekiz.admin.common.config.Global;
import com.trekiz.admin.common.persistence.Page;
import com.trekiz.admin.common.utils.BeanUtil;
import com.trekiz.admin.common.web.BaseController;
import com.trekiz.admin.modules.hotel.entity.HotelTravelerTypeRelation;
import com.trekiz.admin.modules.hotel.input.HotelTravelerTypeRelationInput;
import com.trekiz.admin.modules.hotel.query.HotelTravelerTypeRelationQuery;
import com.trekiz.admin.modules.hotel.service.HotelTravelerTypeRelationService;
import com.trekiz.admin.modules.sys.utils.UserUtils;
import com.trekiz.admin.modules.traveler.entity.TravelerType;
import com.trekiz.admin.modules.traveler.service.TravelerTypeService;

/**
 * @author  quauq
 * @version 1.0
 * @since 1.0
 */

@Controller
@RequestMapping(value = "${adminPath}/hotelTravelerTypeRelation")
public class HotelTravelerTypeRelationController extends BaseController {
	
	//forward paths
	protected static final String LIST_PAGE = "modules/hotel/hoteltravelertyperelation/list";
	protected static final String RE_LIST_PAGE = "redirect:"+Global.getAdminPath()+"/hotelTravelerTypeRelation/list";
	protected static final String FORM_PAGE = "modules/hotel/hoteltravelertyperelation/form";
	protected static final String SHOW_PAGE = "modules/hotel/hoteltravelertyperelation/show";
	
	@Autowired
	private HotelTravelerTypeRelationService hotelTravelerTypeRelationService;
	
	@Autowired
	private TravelerTypeService travelerTypeService;
	
	
	private HotelTravelerTypeRelation dataObj;
	
	@ModelAttribute  
    public void populateModel( HttpServletRequest request){
		String uuid = request.getParameter("uuid");
		if(StringUtils.isNotBlank(uuid)){
			dataObj=hotelTravelerTypeRelationService.getByUuid(uuid);
		}
	}
	
	@RequestMapping(value = "list")
	public String list(HotelTravelerTypeRelationQuery hotelTravelerTypeRelationQuery, HttpServletRequest request, HttpServletResponse response, Model model) {
		hotelTravelerTypeRelationQuery.setDelFlag("0");
        Page<HotelTravelerTypeRelation> page = hotelTravelerTypeRelationService.find(new Page<HotelTravelerTypeRelation>(request, response), hotelTravelerTypeRelationQuery); 
        model.addAttribute("page", page);
        model.addAttribute("hotelTravelerTypeRelationQuery", hotelTravelerTypeRelationQuery);
        return LIST_PAGE;
	}

	@RequestMapping(value = "form")
	public String form(HotelTravelerTypeRelationInput hotelTravelerTypeRelationInput, Model model) {
		model.addAttribute("hotelTravelerTypeRelationInput", hotelTravelerTypeRelationInput);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "save")
	public String save(HotelTravelerTypeRelationInput hotelTravelerTypeRelationInput, Model model, RedirectAttributes redirectAttributes) {
		
		String result="1";
		try {
			hotelTravelerTypeRelationService.save(hotelTravelerTypeRelationInput);
		} catch (Exception e) {
			result="0";
		}
		return result;
		
	}
	
	@RequestMapping(value = "show/{uuid}")
	public String show(@PathVariable String uuid, HttpServletRequest request, HttpServletResponse response, Model model) {
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		model.addAttribute("hotelTravelerTypeRelation", hotelTravelerTypeRelationService.getByUuid(uuid));
		return SHOW_PAGE;
	}
	
	@RequestMapping(value = "edit/{uuid}")
	public String edit(@PathVariable String uuid, Model model) {
		Long companyId = UserUtils.getUser().getCompany().getId();
		if(StringUtils.isEmpty(uuid)) {
			return RE_LIST_PAGE;
		}
		HotelTravelerTypeRelation hotelTravelerTypeRelation = hotelTravelerTypeRelationService.getByUuid("");
		HotelTravelerTypeRelationInput hotelTravelerTypeRelationInput = new HotelTravelerTypeRelationInput(hotelTravelerTypeRelation);
        List<TravelerType> travelerTypes = travelerTypeService.find(new TravelerType(companyId.intValue(),"0","1"));//0代表是否删除，1代表是否启用
      
        HotelTravelerTypeRelationQuery query = new HotelTravelerTypeRelationQuery();
		query.setHotelUuid(uuid);
		query.setDelFlag(Context.DEL_FLAG_NORMAL);
		List<HotelTravelerTypeRelation> list = hotelTravelerTypeRelationService.find(query);
		String relationString = "";
		if(list != null && list.size()>0){
			for(HotelTravelerTypeRelation hgtrealtion: list){
				relationString = relationString + hgtrealtion.getTravelerTypeUuid() + ",";
			}
		}
		
        model.addAttribute("hotelUuid", uuid);
        model.addAttribute("travelerTypes", travelerTypes);
		model.addAttribute("hotelTravelerTypeRelationInput", hotelTravelerTypeRelationInput);
		model.addAttribute("relationString", relationString);
		return FORM_PAGE;
	}
	@ResponseBody
	@RequestMapping(value = "update")
	public String update(HotelTravelerTypeRelationInput hotelTravelerTypeRelationInput, Model model, RedirectAttributes redirectAttributes,HttpServletRequest request) {
		String result="2";
		try {
			if(StringUtils.isNotEmpty(hotelTravelerTypeRelationInput.getHotelUuid().toString())){
				hotelTravelerTypeRelationService.removeByHotelUuid(hotelTravelerTypeRelationInput.getHotelUuid().toString());
			}
			String[] travelerTypeUuids = request.getParameter("travelerTypeUuids").trim().split(",");
			String[] travelerTypeNames = request.getParameter("travelerTypeNames").trim().split(",");
			for(int i=0;i<travelerTypeUuids.length;i++){
				String s = travelerTypeUuids[i];
				if(s!=null&&!"".equals(s)){
					HotelTravelerTypeRelation typeRelation = new HotelTravelerTypeRelation();
					typeRelation.setTravelerTypeUuid(travelerTypeUuids[i]);
					typeRelation.setTravelerTypeName(travelerTypeNames[i]);
					typeRelation.setHotelUuid(hotelTravelerTypeRelationInput.getHotelUuid());
					hotelTravelerTypeRelationService.save(typeRelation);
				}
			}
			BeanUtil.copySimpleProperties(dataObj, hotelTravelerTypeRelationInput,true);
		} catch (Exception e) {
			result="0";
		}
		return result;
	}
	@ResponseBody
	@RequestMapping(value = "delete")
	public Object delete(String uuids) {
		
		Map<String,Object> datas = new HashMap<String, Object>();
		boolean b = true;
		try {
			if(StringUtils.isNotBlank(uuids)){
				String[] uuidArray = uuids.split(",");
				for(String uuid:uuidArray){
					hotelTravelerTypeRelationService.removeByUuid(uuid);
				}
				
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
}
